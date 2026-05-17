/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

import io.netty.channel.Channel;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.transport.client.tcp.EndpointResolver;
import org.eclipse.milo.opcua.stack.transport.client.tcp.InboundChannelTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpMultiplexedReverseConnectTransportConfig;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SDK-level controller for unknown-server Reverse Connect resolution and on-demand client creation.
 *
 * <p>The listener remains a transport dispatcher. This controller owns resolver invocation,
 * discovery-channel consumption, {@link ClientCustomizer}, {@link OpcUaClient} creation, and {@link
 * ClientListener} notification.
 */
final class MultiplexedReverseConnectClientController {

  private static final Logger logger =
      LoggerFactory.getLogger(MultiplexedReverseConnectClientController.class);

  private final MultiplexedReverseConnectListenerConfig config;
  private final Function<String, OpcTcpMultiplexedReverseConnectTransport> transportFactory;
  private final DiscoveryClientFactory discoveryClientFactory;
  private final Executor executor;
  private final ScheduledExecutorService scheduledExecutor;
  private final Object pendingLock = new Object();
  private final AtomicInteger pendingConnections = new AtomicInteger(0);
  private final Set<PendingConnection> pending = ConcurrentHashMap.newKeySet();
  private final ConcurrentHashMap<String, PendingTransportHandoff> pendingHandoffs =
      new ConcurrentHashMap<>();
  private final AtomicBoolean stopped = new AtomicBoolean(false);

  MultiplexedReverseConnectClientController(
      MultiplexedReverseConnectListenerConfig config,
      Function<String, OpcTcpMultiplexedReverseConnectTransport> transportFactory) {

    this(config, transportFactory, MultiplexedReverseConnectClientController::newDiscoveryClient);
  }

  MultiplexedReverseConnectClientController(
      MultiplexedReverseConnectListenerConfig config,
      Function<String, OpcTcpMultiplexedReverseConnectTransport> transportFactory,
      DiscoveryClientFactory discoveryClientFactory) {

    this.config = Objects.requireNonNull(config, "config");
    this.transportFactory = Objects.requireNonNull(transportFactory, "transportFactory");
    this.discoveryClientFactory =
        Objects.requireNonNull(discoveryClientFactory, "discoveryClientFactory");
    this.executor = config.getTransportConfig().getExecutor();
    this.scheduledExecutor = config.getTransportConfig().getScheduledExecutor();
  }

  void dispatch(Channel channel, ReverseHelloMessage reverseHello) {
    var connection = new PendingConnection(channel, reverseHello);
    PendingTransportHandoff pendingHandoff = null;
    boolean connectionQueued = false;

    try {
      synchronized (pendingLock) {
        if (stopped.get()) {
          closeChannel(
              channel,
              reverseHello,
              StatusCodes.Bad_Shutdown,
              "unknown connection received after listener stop");
          return;
        }

        pendingHandoff = pendingHandoffs.get(reverseHello.serverUri());
        if (pendingHandoff == null) {
          int pendingCount = pendingConnections.incrementAndGet();
          if (pendingCount > config.getMaxPendingConnections()) {
            pendingConnections.decrementAndGet();

            logger.debug(
                "maxPendingConnections ({}) exceeded, closing channel from {}",
                config.getMaxPendingConnections(),
                channel.remoteAddress());

            closeChannel(
                channel,
                reverseHello,
                StatusCodes.Bad_TcpNotEnoughResources,
                "max pending connections exceeded");
            return;
          }

          pending.add(connection);
          connectionQueued = true;
          connection.timeoutFuture = scheduleTimeout(connection);
        }
      }

      if (pendingHandoff != null) {
        logger.debug(
            "Offering reverse connection for ServerUri={} to pending on-demand transport",
            reverseHello.serverUri());
        pendingHandoff.offer(channel, reverseHello);
        return;
      }

      executor.execute(() -> invokeResolver(connection));
    } catch (Throwable t) {
      if (connectionQueued) {
        fail(connection, t);
      } else {
        closeChannel(channel, reverseHello, StatusCodes.Bad_ConnectionRejected, t.getMessage());
      }
    }
  }

  CompletableFuture<Void> stop(Throwable cause) {
    PendingConnection[] connections;
    PendingTransportHandoff[] handoffs;

    synchronized (pendingLock) {
      stopped.set(true);
      connections = pending.toArray(PendingConnection[]::new);
      handoffs = pendingHandoffs.values().toArray(PendingTransportHandoff[]::new);
      pendingHandoffs.clear();
    }

    for (PendingTransportHandoff handoff : handoffs) {
      handoff.cancel();
    }

    var futures = new CompletableFuture<?>[connections.length];

    for (int i = 0; i < connections.length; i++) {
      futures[i] = fail(connections[i], cause);
    }

    return CompletableFuture.allOf(futures);
  }

  private ScheduledFuture<?> scheduleTimeout(PendingConnection connection) {
    return scheduledExecutor.schedule(
        () -> {
          var timeout =
              new TimeoutException(
                  "endpoint resolver timed out after " + config.getResolverTimeout() + "ms");

          logger.warn(
              "EndpointResolver timed out for ServerUri={} after {}ms",
              connection.reverseHello.serverUri(),
              config.getResolverTimeout());

          fail(connection, timeout);
        },
        config.getResolverTimeout(),
        TimeUnit.MILLISECONDS);
  }

  private void invokeResolver(PendingConnection connection) {
    if (connection.isComplete()) {
      return;
    }

    if (stopped.get()) {
      fail(connection, new UaException(StatusCodes.Bad_Shutdown, "listener stopped"));
      return;
    }

    EndpointResolver resolver = config.getEndpointResolver();
    if (resolver == null) {
      fail(
          connection,
          new UaException(StatusCodes.Bad_ServerUriInvalid, "EndpointResolver is not configured"));
      return;
    }

    CompletableFuture<EndpointDescription> resolverFuture;
    try {
      resolverFuture =
          resolver.resolve(
              connection.reverseHello.serverUri(),
              connection.reverseHello.endpointUrl(),
              newDiscovery(connection));
    } catch (Throwable t) {
      logger.warn(
          "EndpointResolver threw for ServerUri={}", connection.reverseHello.serverUri(), t);
      fail(connection, t);
      return;
    }

    if (resolverFuture == null) {
      fail(connection, new NullPointerException("EndpointResolver returned null future"));
      return;
    }

    if (connection.isComplete()) {
      resolverFuture.cancel(false);
      return;
    }

    connection.resolverFuture = resolverFuture;
    resolverFuture.whenCompleteAsync(
        (endpoint, failure) -> {
          if (failure != null) {
            Throwable cause = unwrap(failure);
            logger.warn(
                "EndpointResolver failed for ServerUri={}",
                connection.reverseHello.serverUri(),
                cause);
            fail(connection, cause);
            return;
          }

          if (endpoint == null) {
            fail(connection, new NullPointerException("EndpointResolver returned null endpoint"));
            return;
          }

          resolved(connection, endpoint);
        },
        executor);
  }

  private EndpointResolver.Discovery newDiscovery(PendingConnection connection) {
    return () -> {
      if (!connection.consumeForDiscovery()) {
        return CompletableFuture.failedFuture(
            new IllegalStateException("discovery channel already consumed"));
      }

      var discoveryEndpoint =
          new EndpointDescription(
              connection.reverseHello.endpointUrl(),
              null,
              null,
              MessageSecurityMode.None,
              SecurityPolicy.None.getUri(),
              null,
              Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
              ubyte(0));

      DiscoveryClientHandle discoveryClient =
          discoveryClientFactory.create(
              discoveryEndpoint,
              createDiscoveryTransportConfig(),
              connection.channel,
              connection.reverseHello);

      return discoveryClient
          .connectAsync()
          .thenComposeAsync(
              ignored ->
                  discoveryClient.getEndpoints(
                      connection.reverseHello.endpointUrl(),
                      new String[] {Stack.TCP_UASC_UABINARY_TRANSPORT_URI}),
              executor)
          .handleAsync(DiscoveryOutcome::new, executor)
          .thenCompose(outcome -> disconnectDiscoveryClient(connection, discoveryClient, outcome));
    };
  }

  private CompletableFuture<List<EndpointDescription>> disconnectDiscoveryClient(
      PendingConnection connection,
      DiscoveryClientHandle discoveryClient,
      DiscoveryOutcome outcome) {

    CompletableFuture<Void> disconnectFuture;
    try {
      disconnectFuture = discoveryClient.disconnectAsync();
    } catch (Throwable t) {
      disconnectFuture = CompletableFuture.failedFuture(t);
    }

    return disconnectFuture.handleAsync(
        (ignored, disconnectFailure) -> {
          Throwable failure = unwrap(outcome.failure());
          Throwable cleanupFailure = unwrap(disconnectFailure);

          if (cleanupFailure != null) {
            logger.warn(
                "Discovery disconnect failed for ServerUri={}",
                connection.reverseHello.serverUri(),
                cleanupFailure);

            if (failure != null) {
              failure.addSuppressed(cleanupFailure);
            } else {
              failure = cleanupFailure;
            }
          }

          if (failure != null) {
            throw new CompletionException(failure);
          }

          List<EndpointDescription> endpoints = copyEndpoints(outcome.endpoints());
          connection.discoveryEndpoints = endpoints;

          return endpoints;
        },
        executor);
  }

  private void resolved(PendingConnection connection, EndpointDescription endpoint) {
    if (!connection.beginTerminal()) {
      return;
    }

    if (stopped.get()) {
      cleanupResolvedAfterStop(connection)
          .whenComplete((ignored, failure) -> finish(connection, failure));
      return;
    }

    String serverUri = connection.reverseHello.serverUri();
    boolean channelConsumed = connection.channelConsumed.get();

    logger.debug("Resolved endpoint for ServerUri={}: {}", serverUri, endpoint.getEndpointUrl());

    ClientListener clientListener = config.getClientListener();
    if (clientListener == null) {
      logger.debug(
          "No ClientListener configured for resolved ServerUri={}, closing channel {}",
          serverUri,
          connection.channel.remoteAddress());

      if (!channelConsumed) {
        closeChannel(
                connection.channel,
                connection.reverseHello,
                StatusCodes.Bad_ConnectionRejected,
                "resolved without client listener")
            .whenComplete((ignored, failure) -> finish(connection, failure));
      } else {
        finish(connection, null);
      }
      return;
    }

    OpcTcpMultiplexedReverseConnectTransport transport = null;
    OpcUaClient client = null;
    try {
      OpcUaClientConfigBuilder builder = OpcUaClientConfig.builder();
      builder.setEndpoint(endpoint);
      builder.setDiscoveryEndpoints(connection.discoveryEndpoints);
      builder.setSessionEndpointValidationEnabled(false);

      ClientCustomizer clientCustomizer = config.getClientCustomizer();
      if (clientCustomizer != null) {
        clientCustomizer.configure(serverUri, builder);
      }

      transport = transportFactory.apply(serverUri);
      client = new OnDemandOpcUaClient(builder.build(), transport, serverUri);

      registerPendingHandoff(serverUri, transport);

      if (!channelConsumed) {
        // offerChannel is the transport-owner boundary; the owner will not start a handshake until
        // the real ClientApplicationContext is installed by OpcUaClient.connectAsync().
        transport.offerChannel(connection.channel, connection.reverseHello);
      }

      clientListener.onClientCreated(client);
      finish(connection, null);
    } catch (Throwable t) {
      logger.warn("Failed to create on-demand client for ServerUri={}", serverUri, t);

      if (transport != null) {
        removePendingHandoff(serverUri, transport);
      }

      CompletableFuture<Void> cleanupFuture;
      if (client != null) {
        cleanupFuture =
            cleanupClientAfterListenerFailure(serverUri, client, connection, channelConsumed);
      } else if (!channelConsumed) {
        cleanupFuture =
            closeChannel(
                connection.channel,
                connection.reverseHello,
                StatusCodes.Bad_ConnectionRejected,
                "client creation failed");
      } else {
        cleanupFuture = CompletableFuture.completedFuture(null);
      }

      cleanupFuture.whenComplete((ignored, failure) -> finish(connection, failure));
    }
  }

  private CompletableFuture<Void> fail(PendingConnection connection, Throwable failure) {
    if (!connection.beginTerminal()) {
      return connection.terminalFuture;
    }

    CompletableFuture<EndpointDescription> resolverFuture = connection.resolverFuture;
    if (resolverFuture != null) {
      resolverFuture.cancel(false);
    }

    closeChannel(
            connection.channel,
            connection.reverseHello,
            statusCodeForFailure(failure),
            reasonForFailure(failure))
        .whenComplete((ignored, closeFailure) -> finish(connection, closeFailure));

    return connection.terminalFuture;
  }

  private void finish(PendingConnection connection, @Nullable Throwable failure) {
    synchronized (pendingLock) {
      if (pending.remove(connection)) {
        pendingConnections.decrementAndGet();
      }
    }

    ScheduledFuture<?> timeoutFuture = connection.timeoutFuture;
    if (timeoutFuture != null) {
      timeoutFuture.cancel(false);
    }

    if (failure != null) {
      connection.terminalFuture.completeExceptionally(unwrap(failure));
    } else {
      connection.terminalFuture.complete(null);
    }
  }

  private CompletableFuture<Void> cleanupResolvedAfterStop(PendingConnection connection) {
    if (!connection.channelConsumed.get()) {
      return closeChannel(
          connection.channel,
          connection.reverseHello,
          StatusCodes.Bad_Shutdown,
          "resolved after listener stop");
    }

    return CompletableFuture.completedFuture(null);
  }

  private CompletableFuture<Void> cleanupClientAfterListenerFailure(
      String serverUri, OpcUaClient client, PendingConnection connection, boolean channelConsumed) {

    CompletableFuture<OpcUaClient> disconnectFuture;
    try {
      disconnectFuture = client.disconnectAsync();
    } catch (Throwable cleanupFailure) {
      logger.warn(
          "Failed to clean up on-demand client for ServerUri={}", serverUri, cleanupFailure);

      if (!channelConsumed) {
        return closeChannel(
            connection.channel,
            connection.reverseHello,
            StatusCodes.Bad_ConnectionRejected,
            "client listener failed");
      }

      return CompletableFuture.completedFuture(null);
    }

    return disconnectFuture
        .handleAsync(
            (ignored, cleanupFailure) -> {
              if (cleanupFailure != null) {
                logger.warn(
                    "Failed to clean up on-demand client for ServerUri={}",
                    serverUri,
                    unwrap(cleanupFailure));
              }

              return null;
            },
            executor)
        .thenCompose(
            ignored -> {
              if (!channelConsumed && connection.channel.isOpen()) {
                return closeChannel(
                    connection.channel,
                    connection.reverseHello,
                    StatusCodes.Bad_ConnectionRejected,
                    "client listener failed");
              }

              return CompletableFuture.completedFuture(null);
            });
  }

  private void registerPendingHandoff(
      String serverUri, OpcTcpMultiplexedReverseConnectTransport transport) {

    var handoff = new PendingTransportHandoff(transport);

    transport.addTransitionListener(
        connected -> {
          if (connected) {
            removePendingHandoff(serverUri, transport);
          }
        });

    PendingTransportHandoff previous;
    synchronized (pendingLock) {
      if (stopped.get()) {
        handoff.cancel();
        return;
      }

      previous = pendingHandoffs.put(serverUri, handoff);
    }

    if (previous != null) {
      previous.cancel();
    }
  }

  private void removePendingHandoff(
      String serverUri, OpcTcpMultiplexedReverseConnectTransport transport) {

    PendingTransportHandoff handoff = pendingHandoffs.get(serverUri);
    if (handoff != null
        && handoff.transport == transport
        && pendingHandoffs.remove(serverUri, handoff)) {
      handoff.cancel();
    }
  }

  private OpcTcpMultiplexedReverseConnectTransportConfig createDiscoveryTransportConfig() {
    return OpcTcpMultiplexedReverseConnectTransportConfig.newBuilder()
        .setExecutor(config.getTransportConfig().getExecutor())
        .setScheduledExecutor(config.getTransportConfig().getScheduledExecutor())
        .setEventLoop(config.getTransportConfig().getEventLoop())
        .setWheelTimer(config.getTransportConfig().getWheelTimer())
        .build();
  }

  private CompletableFuture<Void> closeChannel(
      Channel channel, ReverseHelloMessage reverseHello, long statusCode, @Nullable String reason) {

    if (reason != null) {
      logger.debug(
          "Closing unmatched reverse connection for ServerUri={} from {}: {}",
          reverseHello.serverUri(),
          channel.remoteAddress(),
          reason);
    }

    if (!channel.isOpen()) {
      return CompletableFuture.completedFuture(null);
    }

    return ReverseConnectRejection.sendErrorAndClose(channel, statusCode, reason);
  }

  private static long statusCodeForFailure(Throwable failure) {
    Throwable cause = unwrap(failure);

    if (cause instanceof UaException uaException) {
      return uaException.getStatusCode().value();
    }

    if (cause instanceof TimeoutException) {
      return StatusCodes.Bad_Timeout;
    }

    return StatusCodes.Bad_ConnectionRejected;
  }

  private static @Nullable String reasonForFailure(Throwable failure) {
    Throwable cause = unwrap(failure);

    return cause != null ? cause.getMessage() : null;
  }

  private static DiscoveryClientHandle newDiscoveryClient(
      EndpointDescription endpoint,
      OpcTcpMultiplexedReverseConnectTransportConfig transportConfig,
      Channel channel,
      ReverseHelloMessage reverseHello) {

    var transport = new InboundChannelTransport(transportConfig, channel, reverseHello);
    var discoveryClient = new DiscoveryClient(endpoint, transport);

    return new DiscoveryClientAdapter(discoveryClient);
  }

  private static Throwable unwrap(@Nullable Throwable failure) {
    if (failure instanceof CompletionException && failure.getCause() != null) {
      return failure.getCause();
    }

    return failure;
  }

  private static List<EndpointDescription> copyEndpoints(
      @Nullable List<EndpointDescription> endpoints) {

    return endpoints != null ? List.copyOf(endpoints) : List.of();
  }

  private final class OnDemandOpcUaClient extends OpcUaClient {

    private final String serverUri;
    private final OpcTcpMultiplexedReverseConnectTransport transport;

    private OnDemandOpcUaClient(
        OpcUaClientConfig config,
        OpcTcpMultiplexedReverseConnectTransport transport,
        String serverUri) {

      super(config, transport);

      this.serverUri = serverUri;
      this.transport = transport;
    }

    @Override
    public CompletableFuture<OpcUaClient> connectAsync() {
      return super.connectAsync()
          .whenComplete(
              (client, failure) -> {
                if (failure == null) {
                  removePendingHandoff(serverUri, transport);
                }
              });
    }

    @Override
    public CompletableFuture<OpcUaClient> disconnectAsync() {
      removePendingHandoff(serverUri, transport);

      return super.disconnectAsync();
    }
  }

  @FunctionalInterface
  interface DiscoveryClientFactory {

    DiscoveryClientHandle create(
        EndpointDescription endpoint,
        OpcTcpMultiplexedReverseConnectTransportConfig transportConfig,
        Channel channel,
        ReverseHelloMessage reverseHello);
  }

  interface DiscoveryClientHandle {

    CompletableFuture<Void> connectAsync();

    CompletableFuture<List<EndpointDescription>> getEndpoints(
        String endpointUrl, String[] profileUris);

    CompletableFuture<Void> disconnectAsync();
  }

  private static final class DiscoveryClientAdapter implements DiscoveryClientHandle {

    private final DiscoveryClient discoveryClient;

    private DiscoveryClientAdapter(DiscoveryClient discoveryClient) {
      this.discoveryClient = discoveryClient;
    }

    @Override
    public CompletableFuture<Void> connectAsync() {
      return discoveryClient.connectAsync().thenApply(ignored -> null);
    }

    @Override
    public CompletableFuture<List<EndpointDescription>> getEndpoints(
        String endpointUrl, String[] profileUris) {

      return discoveryClient
          .getEndpoints(endpointUrl, new String[0], profileUris)
          .thenApply(response -> Lists.ofNullable(response.getEndpoints()));
    }

    @Override
    public CompletableFuture<Void> disconnectAsync() {
      return discoveryClient.disconnectAsync().thenApply(ignored -> null);
    }
  }

  private record DiscoveryOutcome(
      @Nullable List<EndpointDescription> endpoints, @Nullable Throwable failure) {}

  private static final class PendingTransportHandoff {

    private final OpcTcpMultiplexedReverseConnectTransport transport;
    private boolean active = true;

    private PendingTransportHandoff(OpcTcpMultiplexedReverseConnectTransport transport) {
      this.transport = transport;
    }

    private synchronized void offer(Channel channel, ReverseHelloMessage reverseHello) {
      if (!active) {
        ReverseConnectRejection.sendErrorAndClose(
            channel, StatusCodes.Bad_ConnectionRejected, "pending handoff is no longer active");
        return;
      }

      transport.offerChannel(channel, reverseHello);
    }

    private synchronized void cancel() {
      active = false;
    }
  }

  private static final class PendingConnection {

    private final Channel channel;
    private final ReverseHelloMessage reverseHello;
    private final AtomicBoolean terminalStarted = new AtomicBoolean(false);
    private final AtomicBoolean channelConsumed = new AtomicBoolean(false);
    private final CompletableFuture<Void> terminalFuture = new CompletableFuture<>();
    private volatile List<EndpointDescription> discoveryEndpoints = List.of();
    private volatile @Nullable CompletableFuture<EndpointDescription> resolverFuture;
    private volatile @Nullable ScheduledFuture<?> timeoutFuture;

    private PendingConnection(Channel channel, ReverseHelloMessage reverseHello) {
      this.channel = channel;
      this.reverseHello = reverseHello;
    }

    private boolean beginTerminal() {
      return terminalStarted.compareAndSet(false, true);
    }

    private boolean isComplete() {
      return terminalStarted.get();
    }

    private boolean consumeForDiscovery() {
      return channelConsumed.compareAndSet(false, true);
    }
  }
}
