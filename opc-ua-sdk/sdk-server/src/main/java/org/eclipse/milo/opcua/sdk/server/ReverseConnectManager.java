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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.locks.ReentrantLock;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpReverseConnectServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectTarget;
import org.jspecify.annotations.Nullable;

/**
 * SDK-level registry and lifecycle facade for server-side OPC UA Reverse Connect targets.
 *
 * <p>The manager owns target registrations and server start/stop coordination. Target-level retry
 * timers, per-attempt state, and idle-socket policy are owned by stack transport targets.
 */
public class ReverseConnectManager {

  private final ReentrantLock lock = new ReentrantLock();

  private enum RunState {
    STOPPED,
    RUNNING,
    STOPPING
  }

  private final Map<ReverseConnectHandle, TargetRegistration> registrations = new LinkedHashMap<>();

  private final Map<ReverseConnectHandle, ManagedTarget> targets = new LinkedHashMap<>();

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
   * @param config the Reverse Connect configuration.
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
   * @return a future that completes when target owners have accepted the start request. If a stop
   *     is already in progress, compatible repeated starts share one queued restart; incompatible
   *     starts fail without replacing the queued restart.
   */
  CompletableFuture<Void> start(ServerApplicationContext applicationContext, String serverUri) {
    List<ManagedTarget> targetsToStart;

    lock.lock();
    try {
      if (runState == RunState.RUNNING) {
        if (isCurrentStartLocked(applicationContext, serverUri)) {
          return CompletableFuture.completedFuture(null);
        } else {
          return CompletableFuture.failedFuture(
              new IllegalStateException(
                  "ReverseConnectManager is already running with different start arguments"));
        }
      }
      if (runState == RunState.STOPPING) {
        return queuePendingStartLocked(applicationContext, serverUri);
      }

      targetsToStart = prepareStartLocked(applicationContext, serverUri);
    } catch (Throwable t) {
      return CompletableFuture.failedFuture(t);
    } finally {
      lock.unlock();
    }

    return finishStart(targetsToStart, applicationContext, serverUri);
  }

  /**
   * Stop the Reverse Connect manager. Called by {@code OpcUaServer.shutdown()}.
   *
   * @return a future that completes when all target owners have stopped.
   */
  CompletableFuture<Void> stop() {
    List<ManagedTarget> targetsToStop;
    CompletableFuture<Void> stopCompletion;

    lock.lock();
    try {
      if (runState != RunState.RUNNING) {
        return stopFuture;
      }

      runState = RunState.STOPPING;
      targetsToStop = List.copyOf(targets.values());
      targetsToStop.forEach(ManagedTarget::markStopped);
      targets.clear();

      stopCompletion = new CompletableFuture<>();
      stopFuture = stopCompletion;
    } finally {
      lock.unlock();
    }

    stopTargets(targetsToStop).whenComplete((v, ex) -> finishStop(stopCompletion, ex));

    return stopCompletion;
  }

  /**
   * Register a client for Reverse Connect. The server endpoint URL sent in ReverseHello messages
   * defaults to the server's primary (non-discovery) endpoint.
   *
   * @param clientEndpointUrl the client's OPC TCP listening address.
   * @return a handle for removing this registration.
   * @throws IllegalArgumentException if {@code clientEndpointUrl} does not use the {@code opc.tcp}
   *     scheme.
   */
  public ReverseConnectHandle addReverseConnect(String clientEndpointUrl) {
    return addReverseConnect(clientEndpointUrl, null);
  }

  /**
   * Register a client for Reverse Connect with an explicit server endpoint URL.
   *
   * @param clientEndpointUrl the client's OPC TCP listening address.
   * @param endpointUrl the server endpoint URL to include in the ReverseHello. If {@code null},
   *     defaults to the server's primary endpoint.
   * @return a handle for removing this registration.
   * @throws IllegalArgumentException if {@code clientEndpointUrl} does not use the {@code opc.tcp}
   *     scheme.
   */
  public ReverseConnectHandle addReverseConnect(
      String clientEndpointUrl, @Nullable String endpointUrl) {
    requireOpcTcpClientEndpointUrl(clientEndpointUrl);

    ReverseConnectHandle handle;
    @Nullable ManagedTarget targetToStart = null;

    lock.lock();
    try {
      if (runState == RunState.RUNNING) {
        ServerApplicationContext ctx = requireApplicationContextLocked();
        String uri = requireServerUriLocked();
        String resolvedEndpointUrl = resolveEndpointUrl(ctx, endpointUrl);

        handle = new ReverseConnectHandle(clientEndpointUrl, resolvedEndpointUrl);
        var registration = new TargetRegistration(handle, clientEndpointUrl, endpointUrl);
        targetToStart = createTarget(registration, resolvedEndpointUrl, ctx, uri);

        registrations.put(handle, registration);
        targets.put(handle, targetToStart);
      } else {
        handle =
            new ReverseConnectHandle(clientEndpointUrl, endpointUrl != null ? endpointUrl : "");
        registrations.put(handle, new TargetRegistration(handle, clientEndpointUrl, endpointUrl));
      }
    } finally {
      lock.unlock();
    }

    if (targetToStart != null) {
      targetToStart.start();
    }

    return handle;
  }

  /**
   * Remove a Reverse Connect registration.
   *
   * @param handle the handle returned by {@link #addReverseConnect}.
   */
  public void removeReverseConnect(ReverseConnectHandle handle) {
    removeReverseConnectAsync(handle);
  }

  /** Package-private for tests that need to observe deterministic target shutdown. */
  CompletableFuture<Void> removeReverseConnectAsync(ReverseConnectHandle handle) {
    @Nullable ManagedTarget targetToRemove;

    lock.lock();
    try {
      registrations.remove(handle);
      targetToRemove = targets.remove(handle);

      if (targetToRemove != null) {
        targetToRemove.markStopped();
      }
    } finally {
      lock.unlock();
    }

    if (targetToRemove != null) {
      return targetToRemove.remove();
    } else {
      return CompletableFuture.completedFuture(null);
    }
  }

  /** Package-private for testing. */
  int connectionCount() {
    lock.lock();
    try {
      return targets.size();
    } finally {
      lock.unlock();
    }
  }

  /** Package-private for testing. */
  int registrationCount() {
    lock.lock();
    try {
      return registrations.size();
    } finally {
      lock.unlock();
    }
  }

  /** Package-private for testing. */
  int handleGroupCount() {
    lock.lock();
    try {
      return (int)
          registrations.values().stream()
              .map(TargetRegistration::clientEndpointUrl)
              .distinct()
              .count();
    } finally {
      lock.unlock();
    }
  }

  private void finishStop(CompletableFuture<Void> stopCompletion, @Nullable Throwable failure) {
    @Nullable PendingStart queuedStart = null;
    List<ManagedTarget> targetsToStart = List.of();
    @Nullable Throwable startFailure = null;

    lock.lock();
    try {
      runState = RunState.STOPPED;
      applicationContext = null;
      serverUri = null;

      if (failure == null) {
        stopCompletion.complete(null);

        if (pendingStart != null) {
          queuedStart = pendingStart;
          pendingStart = null;

          try {
            targetsToStart =
                prepareStartLocked(queuedStart.applicationContext(), queuedStart.serverUri());
          } catch (Throwable t) {
            startFailure = t;
          }
        }
      } else {
        stopCompletion.completeExceptionally(unwrap(failure));

        if (pendingStart != null) {
          pendingStart.future().completeExceptionally(unwrap(failure));
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
        PendingStart start = queuedStart;
        finishStart(targetsToStart, start.applicationContext(), start.serverUri())
            .whenComplete(
                (v, ex) -> {
                  if (ex != null) {
                    start.future().completeExceptionally(unwrap(ex));
                  } else {
                    start.future().complete(null);
                  }
                });
      }
    }
  }

  private List<ManagedTarget> prepareStartLocked(
      ServerApplicationContext applicationContext, String serverUri) {

    var materializedTargets = new ArrayList<MaterializedTarget>();

    for (TargetRegistration registration : registrations.values()) {
      String resolvedEndpointUrl =
          resolveEndpointUrl(applicationContext, registration.requestedEndpointUrl());
      ManagedTarget target =
          createTarget(registration, resolvedEndpointUrl, applicationContext, serverUri);

      materializedTargets.add(new MaterializedTarget(registration, resolvedEndpointUrl, target));
    }

    this.applicationContext = applicationContext;
    this.serverUri = serverUri;
    runState = RunState.RUNNING;
    targets.clear();

    for (MaterializedTarget materializedTarget : materializedTargets) {
      TargetRegistration registration = materializedTarget.registration();
      registration.handle().setEndpointUrl(materializedTarget.resolvedEndpointUrl());
      targets.put(registration.handle(), materializedTarget.target());
    }

    return materializedTargets.stream().map(MaterializedTarget::target).toList();
  }

  private ManagedTarget createTarget(
      TargetRegistration registration,
      String resolvedEndpointUrl,
      ServerApplicationContext applicationContext,
      String serverUri) {

    ReverseConnectTarget target =
        transport.createTarget(
            registration.clientEndpointUrl(),
            resolvedEndpointUrl,
            serverUri,
            applicationContext,
            config,
            transportConfig.getExecutor(),
            Stack.sharedScheduledExecutor());

    return new ManagedTarget(target);
  }

  private CompletableFuture<Void> startTargets(List<ManagedTarget> targetsToStart) {
    List<CompletableFuture<Void>> startFutures =
        targetsToStart.stream().map(ManagedTarget::start).toList();

    return CompletableFuture.allOf(startFutures.toArray(CompletableFuture[]::new));
  }

  private CompletableFuture<Void> finishStart(
      List<ManagedTarget> targetsToStart,
      ServerApplicationContext applicationContext,
      String serverUri) {

    return startTargets(targetsToStart)
        .thenCompose(v -> completeStartIfCurrent(applicationContext, serverUri));
  }

  private CompletableFuture<Void> completeStartIfCurrent(
      ServerApplicationContext applicationContext, String serverUri) {

    lock.lock();
    try {
      if (runState == RunState.RUNNING && isCurrentStartLocked(applicationContext, serverUri)) {
        return CompletableFuture.completedFuture(null);
      }

      return CompletableFuture.failedFuture(
          new IllegalStateException("ReverseConnectManager start was superseded by stop"));
    } finally {
      lock.unlock();
    }
  }

  private CompletableFuture<Void> stopTargets(List<ManagedTarget> targetsToStop) {
    List<CompletableFuture<Void>> stopFutures =
        targetsToStop.stream().map(ManagedTarget::stop).toList();

    return CompletableFuture.allOf(stopFutures.toArray(CompletableFuture[]::new));
  }

  private String resolveEndpointUrl(
      ServerApplicationContext applicationContext, @Nullable String endpointUrl) {

    if (endpointUrl != null) {
      return endpointUrl;
    }

    return resolvePrimaryEndpointUrl(applicationContext);
  }

  private String resolvePrimaryEndpointUrl(ServerApplicationContext applicationContext) {
    return applicationContext.getEndpointDescriptions().stream()
        .filter(
            endpoint ->
                Objects.equals(
                    endpoint.getTransportProfileUri(), TransportProfile.TCP_UASC_UABINARY.getUri()))
        .map(EndpointDescription::getEndpointUrl)
        .filter(url -> url != null && !url.endsWith("/discovery"))
        .findFirst()
        .orElseThrow(
            () -> new IllegalStateException("No non-discovery TCP UASC endpoints configured"));
  }

  private static void requireOpcTcpClientEndpointUrl(String clientEndpointUrl) {
    String scheme = EndpointUtil.getScheme(clientEndpointUrl);

    if (!Objects.equals(scheme, TransportProfile.TCP_UASC_UABINARY.getScheme())) {
      throw new IllegalArgumentException(
          "Reverse Connect client endpoint URL must use opc.tcp scheme: " + clientEndpointUrl);
    }
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

  private boolean isCurrentStartLocked(
      ServerApplicationContext applicationContext, String serverUri) {

    return this.applicationContext == applicationContext
        && Objects.equals(this.serverUri, serverUri);
  }

  private CompletableFuture<Void> queuePendingStartLocked(
      ServerApplicationContext applicationContext, String serverUri) {

    if (pendingStart == null) {
      pendingStart = new PendingStart(applicationContext, serverUri, new CompletableFuture<>());
      return pendingStart.future();
    }

    if (pendingStart.applicationContext() == applicationContext
        && Objects.equals(pendingStart.serverUri(), serverUri)) {
      return pendingStart.future();
    }

    return CompletableFuture.failedFuture(
        new IllegalStateException(
            "ReverseConnectManager stop already has an incompatible queued start"));
  }

  private static Throwable unwrap(Throwable failure) {
    if (failure instanceof CompletionException && failure.getCause() != null) {
      return failure.getCause();
    }

    return failure;
  }

  private static final class ManagedTarget {

    private final ReverseConnectTarget target;
    private boolean stopped;

    private ManagedTarget(ReverseConnectTarget target) {
      this.target = Objects.requireNonNull(target);
    }

    private void markStopped() {
      synchronized (this) {
        stopped = true;
      }
    }

    private CompletableFuture<Void> start() {
      synchronized (this) {
        if (stopped) {
          return CompletableFuture.completedFuture(null);
        }

        return target.start();
      }
    }

    private CompletableFuture<Void> stop() {
      markStopped();

      return target.stop();
    }

    private CompletableFuture<Void> remove() {
      markStopped();

      return target.remove();
    }
  }

  private record TargetRegistration(
      ReverseConnectHandle handle,
      String clientEndpointUrl,
      @Nullable String requestedEndpointUrl) {}

  private record MaterializedTarget(
      TargetRegistration registration, String resolvedEndpointUrl, ManagedTarget target) {}

  private record PendingStart(
      ServerApplicationContext applicationContext,
      String serverUri,
      CompletableFuture<Void> future) {}
}
