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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;
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

  // Owns all mutable manager state: lifecycle, pending registrations,
  // published FSMs, and restart/shutdown coordination.
  private final ReentrantLock lock = new ReentrantLock();

  private enum RunState {
    STOPPED,
    RUNNING,
    STOPPING
  }

  private final Map<ReverseConnectHandle, Fsm<State, Event>> connections = new HashMap<>();

  private final Map<String, Set<ReverseConnectHandle>> handlesByClientUrl = new HashMap<>();

  /**
   * Pending registrations added before {@link #start} is called. Each entry is a (handle,
   * clientEndpointUrl, resolvedEndpointUrl) triple whose FSM will be created during start.
   */
  private final List<PendingRegistration> pendingRegistrations = new ArrayList<>();

  private final ReverseConnectConfig config;
  private final OpcTcpServerTransportConfig transportConfig;
  private final OpcTcpReverseConnectServerTransport transport;

  private @Nullable ServerApplicationContext applicationContext;
  private @Nullable String serverUri;
  private RunState runState = RunState.STOPPED;
  private @Nullable PendingStart pendingStart;
  private CompletableFuture<Void> stopFuture = CompletableFuture.completedFuture(null);

  /**
   * Create a new {@link ReverseConnectManager}.
   *
   * @param config the Reverse Connect configuration (timeouts, backoff, etc.).
   * @param transportConfig the transport configuration for outbound connections.
   */
  public ReverseConnectManager(
      ReverseConnectConfig config, OpcTcpServerTransportConfig transportConfig) {

    this(config, transportConfig, new OpcTcpReverseConnectServerTransport(transportConfig));
  }

  /** Package-private constructor for testing with a stub transport. */
  ReverseConnectManager(
      ReverseConnectConfig config,
      OpcTcpServerTransportConfig transportConfig,
      OpcTcpReverseConnectServerTransport transport) {

    this.config = config;
    this.transportConfig = transportConfig;
    this.transport = transport;
  }

  /**
   * Start the Reverse Connect manager. Called by {@code OpcUaServer.startup()}.
   *
   * @param applicationContext the server application context.
   * @param serverUri the server's ApplicationUri.
   * @return a future that completes when this start request has been published. If a stop is
   *     already in progress, the request is queued and the returned future completes after the
   *     queued restart has been published.
   */
  CompletableFuture<Void> start(ServerApplicationContext applicationContext, String serverUri) {
    List<Fsm<State, Event>> fsmsToStart;

    lock.lock();
    try {
      if (runState == RunState.RUNNING) {
        return CompletableFuture.completedFuture(null);
      }
      if (runState == RunState.STOPPING) {
        return queuePendingStartLocked(applicationContext, serverUri).future();
      }

      fsmsToStart = prepareStartLocked(applicationContext, serverUri);
    } catch (Throwable t) {
      return CompletableFuture.failedFuture(t);
    } finally {
      lock.unlock();
    }

    try {
      fireStartEvents(fsmsToStart);
      return CompletableFuture.completedFuture(null);
    } catch (Throwable t) {
      return CompletableFuture.failedFuture(t);
    }
  }

  /**
   * Stop the Reverse Connect manager. Called by {@code OpcUaServer.shutdown()}.
   *
   * @return a future that completes when all connections are stopped.
   */
  CompletableFuture<Void> stop() {
    List<Fsm<State, Event>> fsmsToStop;
    CompletableFuture<Void> stopCompletion;

    lock.lock();
    try {
      if (runState != RunState.RUNNING) {
        return stopFuture;
      }

      runState = RunState.STOPPING;
      fsmsToStop = List.copyOf(connections.values());
      stopCompletion = new CompletableFuture<>();
      stopFuture = stopCompletion;
    } finally {
      lock.unlock();
    }

    List<CompletableFuture<Void>> stopFutures =
        fsmsToStop.stream()
            .map(
                fsm -> {
                  var stop = new Event.Stop(new CompletableFuture<>());
                  fsm.fireEvent(stop);
                  return stop.future();
                })
            .toList();

    CompletableFuture.allOf(stopFutures.toArray(CompletableFuture[]::new))
        .whenComplete(
            (v, ex) -> {
              PendingStart queuedStart = null;
              List<Fsm<State, Event>> fsmsToStart = List.of();
              Throwable startFailure = null;

              lock.lock();
              try {
                runState = RunState.STOPPED;
                applicationContext = null;
                serverUri = null;

                if (ex == null) {
                  stopCompletion.complete(null);

                  if (pendingStart != null) {
                    queuedStart = pendingStart;
                    pendingStart = null;

                    try {
                      fsmsToStart =
                          prepareStartLocked(
                              queuedStart.applicationContext(), queuedStart.serverUri());
                    } catch (Throwable t) {
                      startFailure = t;
                    }
                  }
                } else {
                  stopCompletion.completeExceptionally(ex);

                  if (pendingStart != null) {
                    pendingStart.future().completeExceptionally(ex);
                    pendingStart = null;
                  }
                }
              } finally {
                lock.unlock();
              }

              if (queuedStart != null) {
                if (startFailure != null) {
                  queuedStart.future().completeExceptionally(startFailure);
                } else {
                  try {
                    fireStartEvents(fsmsToStart);
                    queuedStart.future().complete(null);
                  } catch (Throwable t) {
                    queuedStart.future().completeExceptionally(t);
                  }
                }
              }
            });

    return stopCompletion;
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
    ReverseConnectHandle handle;
    Fsm<State, Event> fsmToStart;

    lock.lock();
    try {
      if (runState != RunState.RUNNING) {
        handle =
            new ReverseConnectHandle(clientEndpointUrl, endpointUrl != null ? endpointUrl : "");
        pendingRegistrations.add(new PendingRegistration(handle, clientEndpointUrl, endpointUrl));
        return handle;
      }

      ServerApplicationContext ctx = requireApplicationContextLocked();
      String uri = requireServerUriLocked();
      String resolvedEndpointUrl =
          endpointUrl != null ? endpointUrl : resolvePrimaryEndpointUrl(ctx);

      handle = new ReverseConnectHandle(clientEndpointUrl, resolvedEndpointUrl);
      fsmToStart = createFsm(clientEndpointUrl, resolvedEndpointUrl, ctx, uri);

      connections.put(handle, fsmToStart);
      handlesByClientUrl.computeIfAbsent(clientEndpointUrl, k -> new HashSet<>()).add(handle);
    } finally {
      lock.unlock();
    }

    fsmToStart.fireEvent(new Event.Start());

    return handle;
  }

  /**
   * Remove a Reverse Connect registration. Also stops any auto-spawned idle sibling connections for
   * the same client.
   *
   * @param handle the handle returned by {@link #addReverseConnect}.
   */
  public void removeReverseConnect(ReverseConnectHandle handle) {
    List<Fsm<State, Event>> fsmsToStop = new ArrayList<>();

    lock.lock();
    try {
      pendingRegistrations.removeIf(reg -> reg.handle() == handle);

      Fsm<State, Event> fsm = connections.remove(handle);
      if (fsm != null) {
        fsmsToStop.add(fsm);
      }

      Set<ReverseConnectHandle> handles = handlesByClientUrl.get(handle.getClientEndpointUrl());
      if (handles != null) {
        handles.remove(handle);

        // Stop auto-spawned idle siblings for this client URL.
        List<ReverseConnectHandle> autoSpawned =
            handles.stream().filter(h -> h.autoSpawned).toList();
        for (ReverseConnectHandle sibling : autoSpawned) {
          handles.remove(sibling);
          Fsm<State, Event> siblingFsm = connections.remove(sibling);
          if (siblingFsm != null) {
            fsmsToStop.add(siblingFsm);
          }
        }

        if (handles.isEmpty()) {
          handlesByClientUrl.remove(handle.getClientEndpointUrl(), handles);
        }
      }
    } finally {
      lock.unlock();
    }

    // Fire stop events outside the lock to avoid holding the lock during
    // FSM processing, which could deadlock with ensureIdleConnection.
    for (Fsm<State, Event> fsm : fsmsToStop) {
      fsm.fireEvent(new Event.Stop(new CompletableFuture<>()));
    }
  }

  /**
   * Ensure there is at least one idle (non-Active, non-Disconnected) connection to the given
   * client. If there is none, spawn a new FSM. This enforces the OPC UA idle socket invariant.
   *
   * @param clientEndpointUrl the client's listening address.
   * @param endpointUrl the server endpoint URL for the ReverseHello.
   */
  void ensureIdleConnection(String clientEndpointUrl, String endpointUrl) {
    // Snapshot the FSM list under the lock, then check states outside the lock.
    // This avoids a lock-ordering deadlock: FSM transition actions run under the
    // FSM write lock and may call this method, while getState() acquires the FSM
    // read lock — holding both ReverseConnectManager.lock and an FSM lock in
    // opposite orders would deadlock.
    List<Fsm<State, Event>> snapshot;
    Set<ReverseConnectHandle> snapshotHandles;

    lock.lock();
    try {
      if (runState != RunState.RUNNING) {
        return;
      }

      Set<ReverseConnectHandle> handles = handlesByClientUrl.get(clientEndpointUrl);
      if (handles == null) {
        return;
      }
      snapshotHandles = Set.copyOf(handles);
      snapshot = handles.stream().map(connections::get).filter(Objects::nonNull).toList();
    } finally {
      lock.unlock();
    }

    // Check states without holding the lock. getState() acquires the FSM read lock.
    boolean hasIdleConnection =
        snapshot.stream()
            .anyMatch(
                fsm -> {
                  State state = fsm.getState();
                  return state != State.Active && state != State.Disconnected;
                });

    if (!hasIdleConnection) {
      Fsm<State, Event> idleFsm = null;

      lock.lock();
      try {
        if (runState != RunState.RUNNING) {
          return;
        }

        Set<ReverseConnectHandle> handles = handlesByClientUrl.get(clientEndpointUrl);
        if (handles != null) {
          // If any new handle appeared since our snapshot, another thread
          // already registered a replacement connection for this client URL.
          // Compare handle identity rather than set size so concurrent
          // remove/add races do not produce duplicate idle FSMs, while still
          // keeping getState() outside Manager.lock.
          boolean replacementHandleAdded =
              handles.stream().anyMatch(handle -> !snapshotHandles.contains(handle));

          if (!replacementHandleAdded) {
            ServerApplicationContext ctx = requireApplicationContextLocked();
            String uri = requireServerUriLocked();
            var idleHandle = new ReverseConnectHandle(clientEndpointUrl, endpointUrl, true);
            idleFsm = createFsm(clientEndpointUrl, endpointUrl, ctx, uri);
            connections.put(idleHandle, idleFsm);
            handles.add(idleHandle);
          }
        }
      } finally {
        lock.unlock();
      }

      if (idleFsm != null) {
        idleFsm.fireEvent(new Event.Start());
      }
    }
  }

  /** Package-private for testing. */
  int connectionCount() {
    lock.lock();
    try {
      return connections.size();
    } finally {
      lock.unlock();
    }
  }

  /** Package-private for testing. */
  int handleGroupCount() {
    lock.lock();
    try {
      return handlesByClientUrl.size();
    } finally {
      lock.unlock();
    }
  }

  private Fsm<State, Event> createFsm(
      String clientEndpointUrl,
      String resolvedEndpointUrl,
      ServerApplicationContext applicationContext,
      String serverUri) {
    return ReverseConnectConnectionFsm.newFsm(
        clientEndpointUrl,
        resolvedEndpointUrl,
        serverUri,
        transport,
        applicationContext,
        config,
        transportConfig.getExecutor(),
        Stack.sharedScheduledExecutor(),
        () ->
            transportConfig
                .getExecutor()
                .execute(() -> ensureIdleConnection(clientEndpointUrl, resolvedEndpointUrl)));
  }

  private String resolvePrimaryEndpointUrl(ServerApplicationContext applicationContext) {
    return applicationContext.getEndpointDescriptions().stream()
        .map(EndpointDescription::getEndpointUrl)
        .filter(url -> url != null && !url.endsWith("/discovery"))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No non-discovery endpoints configured"));
  }

  private ServerApplicationContext requireApplicationContextLocked() {
    if (applicationContext == null) {
      throw new IllegalStateException("ReverseConnectManager is not started");
    }

    return applicationContext;
  }

  private String requireServerUriLocked() {
    if (serverUri == null) {
      throw new IllegalStateException("ReverseConnectManager is not started");
    }

    return serverUri;
  }

  private PendingStart queuePendingStartLocked(
      ServerApplicationContext applicationContext, String serverUri) {

    if (pendingStart == null) {
      pendingStart = new PendingStart(applicationContext, serverUri, new CompletableFuture<>());
    }

    return pendingStart;
  }

  private List<Fsm<State, Event>> prepareStartLocked(
      ServerApplicationContext applicationContext, String serverUri) {

    List<PendingRegistration> pending = List.copyOf(pendingRegistrations);
    Set<ReverseConnectHandle> staleHandles = Set.copyOf(connections.keySet());
    List<Fsm<State, Event>> fsmsToStart = new ArrayList<>(staleHandles.size() + pending.size());

    Map<ReverseConnectHandle, Fsm<State, Event>> rebuiltFsms = new HashMap<>();
    List<MaterializedRegistration> materializedPendingRegistrations = new ArrayList<>();

    for (ReverseConnectHandle handle : staleHandles) {
      Fsm<State, Event> newFsm =
          createFsm(
              handle.getClientEndpointUrl(),
              handle.getEndpointUrl(),
              applicationContext,
              serverUri);
      rebuiltFsms.put(handle, newFsm);
      fsmsToStart.add(newFsm);
    }

    for (PendingRegistration reg : pending) {
      String resolvedEndpointUrl =
          reg.endpointUrl != null ? reg.endpointUrl : resolvePrimaryEndpointUrl(applicationContext);

      // Update the handle with the resolved endpoint URL so callers
      // see the real value instead of the placeholder empty string.
      reg.handle.setEndpointUrl(resolvedEndpointUrl);

      Fsm<State, Event> fsm =
          createFsm(reg.clientEndpointUrl, resolvedEndpointUrl, applicationContext, serverUri);
      materializedPendingRegistrations.add(
          new MaterializedRegistration(reg.handle, reg.clientEndpointUrl, fsm));
      fsmsToStart.add(fsm);
    }

    this.applicationContext = applicationContext;
    this.serverUri = serverUri;
    runState = RunState.RUNNING;
    pendingRegistrations.clear();
    connections.putAll(rebuiltFsms);

    for (MaterializedRegistration registration : materializedPendingRegistrations) {
      connections.put(registration.handle, registration.fsm);
      handlesByClientUrl
          .computeIfAbsent(registration.clientEndpointUrl, k -> new HashSet<>())
          .add(registration.handle);
    }

    return fsmsToStart;
  }

  private void fireStartEvents(List<Fsm<State, Event>> fsmsToStart) {
    fsmsToStart.forEach(fsm -> fsm.fireEvent(new Event.Start()));
  }

  private record PendingRegistration(
      ReverseConnectHandle handle, String clientEndpointUrl, @Nullable String endpointUrl) {}

  private record PendingStart(
      ServerApplicationContext applicationContext,
      String serverUri,
      CompletableFuture<Void> future) {}

  private record MaterializedRegistration(
      ReverseConnectHandle handle, String clientEndpointUrl, Fsm<State, Event> fsm) {}
}
