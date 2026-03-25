/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import com.digitalpetri.fsm.Fsm;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpReverseConnectServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConnectionFsm;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConnectionFsm.Event;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConnectionFsm.State;
import org.jspecify.annotations.Nullable;

/**
 * SDK-level orchestrator for OPC UA Reverse Connect. Manages per-client connection FSMs, enforces
 * the idle socket invariant, and exposes the public API for adding/removing Reverse Connect
 * targets.
 */
public class ReverseConnectManager {

  private final ConcurrentHashMap<ReverseConnectHandle, Fsm<State, Event>> connections =
      new ConcurrentHashMap<>();

  private final ConcurrentHashMap<String, Set<ReverseConnectHandle>> handlesByClientUrl =
      new ConcurrentHashMap<>();

  /**
   * Pending registrations added before {@link #start} is called. Each entry is a (handle,
   * clientEndpointUrl, resolvedEndpointUrl) triple whose FSM will be created during start.
   */
  private final List<PendingRegistration> pendingRegistrations = new ArrayList<>();

  private final ReverseConnectConfig config;
  private final OpcTcpServerTransportConfig transportConfig;
  private final OpcTcpReverseConnectServerTransport transport;

  private volatile @Nullable ServerApplicationContext applicationContext;
  private volatile @Nullable String serverUri;
  private volatile boolean running = false;

  /**
   * Create a new {@link ReverseConnectManager}.
   *
   * @param config the Reverse Connect configuration (timeouts, backoff, etc.).
   * @param transportConfig the transport configuration for outbound connections.
   */
  public ReverseConnectManager(
      ReverseConnectConfig config, OpcTcpServerTransportConfig transportConfig) {

    this.config = config;
    this.transportConfig = transportConfig;
    this.transport = new OpcTcpReverseConnectServerTransport(transportConfig);
  }

  /**
   * Start the Reverse Connect manager. Called by {@code OpcUaServer.startup()}.
   *
   * @param applicationContext the server application context.
   * @param serverUri the server's ApplicationUri.
   */
  void start(ServerApplicationContext applicationContext, String serverUri) {
    this.applicationContext = applicationContext;
    this.serverUri = serverUri;
    this.running = true;

    // Materialize any pre-registered connections that were deferred
    // because applicationContext was not yet available.
    List<PendingRegistration> pending;
    synchronized (pendingRegistrations) {
      pending = List.copyOf(pendingRegistrations);
      pendingRegistrations.clear();
    }

    for (PendingRegistration reg : pending) {
      String resolvedEndpointUrl =
          reg.endpointUrl != null ? reg.endpointUrl : resolvePrimaryEndpointUrl();

      // Update the handle with the resolved endpoint URL so callers
      // see the real value instead of the placeholder empty string.
      reg.handle.setEndpointUrl(resolvedEndpointUrl);

      Fsm<State, Event> fsm = createFsm(reg.clientEndpointUrl, resolvedEndpointUrl);
      connections.put(reg.handle, fsm);
      handlesByClientUrl
          .computeIfAbsent(reg.clientEndpointUrl, k -> new CopyOnWriteArraySet<>())
          .add(reg.handle);
    }

    connections.values().forEach(fsm -> fsm.fireEvent(new Event.Start()));
  }

  /**
   * Stop the Reverse Connect manager. Called by {@code OpcUaServer.shutdown()}.
   *
   * @return a future that completes when all connections are stopped.
   */
  CompletableFuture<Void> stop() {
    running = false;

    List<CompletableFuture<Void>> stopFutures =
        connections.values().stream()
            .map(
                fsm -> {
                  var stop = new Event.Stop(new CompletableFuture<>());
                  fsm.fireEvent(stop);
                  return stop.future();
                })
            .toList();

    return CompletableFuture.allOf(stopFutures.toArray(CompletableFuture[]::new));
  }

  /**
   * Register a client for Reverse Connect. The server endpoint URL sent in ReverseHello messages
   * defaults to the server's primary (non-discovery) endpoint.
   *
   * @param clientEndpointUrl the client's listening address (e.g. "opc.tcp://client-host:48060").
   * @return a handle for removing this registration.
   */
  public ReverseConnectHandle addReverseConnect(String clientEndpointUrl) {
    return addReverseConnect(clientEndpointUrl, null);
  }

  /**
   * Register a client for Reverse Connect with an explicit server endpoint URL.
   *
   * @param clientEndpointUrl the client's listening address.
   * @param endpointUrl the server endpoint URL to include in the ReverseHello. If {@code null},
   *     defaults to the server's primary endpoint.
   * @return a handle for removing this registration.
   */
  public ReverseConnectHandle addReverseConnect(
      String clientEndpointUrl, @Nullable String endpointUrl) {

    if (!running) {
      // Before start(): defer FSM creation until applicationContext is available.
      // If endpointUrl is null it will be resolved during start().
      var handle =
          new ReverseConnectHandle(clientEndpointUrl, endpointUrl != null ? endpointUrl : "");

      synchronized (pendingRegistrations) {
        pendingRegistrations.add(new PendingRegistration(handle, clientEndpointUrl, endpointUrl));
      }

      return handle;
    }

    String resolvedEndpointUrl = endpointUrl != null ? endpointUrl : resolvePrimaryEndpointUrl();

    var handle = new ReverseConnectHandle(clientEndpointUrl, resolvedEndpointUrl);

    Fsm<State, Event> fsm = createFsm(clientEndpointUrl, resolvedEndpointUrl);

    connections.put(handle, fsm);
    handlesByClientUrl
        .computeIfAbsent(clientEndpointUrl, k -> new CopyOnWriteArraySet<>())
        .add(handle);

    fsm.fireEvent(new Event.Start());

    return handle;
  }

  /**
   * Remove a Reverse Connect registration. Also stops any auto-spawned idle sibling connections for
   * the same client.
   *
   * @param handle the handle returned by {@link #addReverseConnect}.
   */
  public void removeReverseConnect(ReverseConnectHandle handle) {
    Fsm<State, Event> fsm = connections.remove(handle);

    if (fsm != null) {
      var stop = new Event.Stop(new CompletableFuture<>());
      fsm.fireEvent(stop);
    }

    // Remove from secondary index and stop all remaining connections for this client
    Set<ReverseConnectHandle> handles = handlesByClientUrl.remove(handle.getClientEndpointUrl());
    if (handles != null) {
      handles.remove(handle);
      for (ReverseConnectHandle sibling : handles) {
        Fsm<State, Event> siblingFsm = connections.remove(sibling);
        if (siblingFsm != null) {
          siblingFsm.fireEvent(new Event.Stop(new CompletableFuture<>()));
        }
      }
    }
  }

  /**
   * Ensure there is at least one idle (non-Active, non-Disconnected) connection to the given
   * client. If there is none, spawn a new FSM. This enforces the OPC UA idle socket invariant.
   *
   * @param clientEndpointUrl the client's listening address.
   * @param endpointUrl the server endpoint URL for the ReverseHello.
   */
  private void ensureIdleConnection(String clientEndpointUrl, String endpointUrl) {
    Set<ReverseConnectHandle> handles = handlesByClientUrl.get(clientEndpointUrl);
    if (handles == null) {
      return;
    }

    boolean hasIdleConnection =
        handles.stream()
            .map(connections::get)
            .anyMatch(
                fsm -> {
                  if (fsm == null) {
                    return false;
                  }
                  State state = fsm.getState();
                  return state != State.Active && state != State.Disconnected;
                });

    if (!hasIdleConnection && running) {
      var idleHandle = new ReverseConnectHandle(clientEndpointUrl, endpointUrl);
      Fsm<State, Event> idleFsm = createFsm(clientEndpointUrl, endpointUrl);

      connections.put(idleHandle, idleFsm);
      handles.add(idleHandle);

      idleFsm.fireEvent(new Event.Start());
    }
  }

  private Fsm<State, Event> createFsm(String clientEndpointUrl, String resolvedEndpointUrl) {
    String uri = this.serverUri;
    ServerApplicationContext ctx = this.applicationContext;

    if (uri == null || ctx == null) {
      throw new IllegalStateException("createFsm() called before start()");
    }

    return ReverseConnectConnectionFsm.newFsm(
        clientEndpointUrl,
        resolvedEndpointUrl,
        uri,
        transport,
        ctx,
        config,
        transportConfig.getExecutor(),
        Stack.sharedScheduledExecutor(),
        () -> ensureIdleConnection(clientEndpointUrl, resolvedEndpointUrl));
  }

  private String resolvePrimaryEndpointUrl() {
    ServerApplicationContext ctx = this.applicationContext;
    if (ctx == null) {
      throw new IllegalStateException("Cannot resolve endpoint URL: manager has not been started");
    }

    return ctx.getEndpointDescriptions().stream()
        .map(EndpointDescription::getEndpointUrl)
        .filter(url -> url != null && !url.endsWith("/discovery"))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No non-discovery endpoints configured"));
  }

  private record PendingRegistration(
      ReverseConnectHandle handle, String clientEndpointUrl, @Nullable String endpointUrl) {}
}
