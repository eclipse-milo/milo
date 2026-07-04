/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import io.netty.buffer.ByteBuf;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportStateListener;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageMappingProvider;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.ExecutionQueue;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runtime for one PubSubConnection: owns the transport channels, the optional per-connection event
 * loop, the writer/reader group runtimes, and — for UDP connections — the {@link DiscoveryRuntime}.
 *
 * <p>Channels are opened lazily by {@link #activate()} (or by a group runtime requiring one) and
 * closed on deactivation. The per-connection event loop, when configured, lives for the lifetime of
 * this runtime instance and is shut down by {@link #dispose()}; the shared Stack resources are
 * never shut down here.
 */
final class ConnectionRuntime extends AbstractComponentRuntime {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionRuntime.class);

  private final PubSubServiceImpl service;
  private final PubSubConnectionConfig config;
  private final @Nullable TransportProvider transportProvider;
  private final @Nullable DiscoveryRuntime discoveryRuntime;
  private final @Nullable MetaDataPublisher metaDataPublisher;
  private final @Nullable EventLoopGroup dedicatedEventLoop;

  /**
   * Serializes subscriber dispatch on the transport executor so datagrams are decoded and delivered
   * in event-loop arrival order, one at a time per connection.
   */
  private final ExecutionQueue dispatchQueue;

  /**
   * Reassembles chunked NetworkMessages received on this connection; confined to the {@link
   * #dispatchQueue} like all subscriber dispatch state.
   */
  private final ChunkReassembler chunkReassembler = new ChunkReassembler();

  /**
   * Maps the transport's liveness edges onto this connection's PubSubState; one edge-tracked
   * handler shared by both data channels. Discovery channels never carry a listener.
   */
  private final TransportStateHandler transportStateHandler = new TransportStateHandler();

  private volatile List<WriterGroupRuntime> writerGroups;
  private volatile List<ReaderGroupRuntime> readerGroups;
  private volatile Map<String, MessageMappingProvider> subscriberMappings = Map.of();

  private volatile @Nullable PublisherChannel publisherChannel;
  private volatile @Nullable SubscriberChannel subscriberChannel;
  private volatile boolean disposed = false;

  ConnectionRuntime(PubSubServiceImpl service, PubSubConnectionConfig config) {
    super(ComponentType.CONNECTION, config.name(), null, config.enabled());

    this.service = service;
    this.config = config;

    this.dispatchQueue = new ExecutionQueue(service.getTransportExecutor());

    this.dedicatedEventLoop =
        service.getServiceConfig().isEventLoopPerConnection() ? new NioEventLoopGroup(1) : null;

    // the rest of construction calls user SPI (TransportProvider.supports,
    // MessageMappingProvider.mappingName); make sure a throw cannot leak the
    // dedicated event loop group created above
    try {
      this.transportProvider = service.resolveTransportProvider(config);

      var writerGroups = new ArrayList<WriterGroupRuntime>();
      for (WriterGroupConfig groupConfig : config.writerGroups()) {
        writerGroups.add(new WriterGroupRuntime(service, this, groupConfig));
      }
      this.writerGroups = List.copyOf(writerGroups);

      var readerGroups = new ArrayList<ReaderGroupRuntime>();
      for (ReaderGroupConfig groupConfig : config.readerGroups()) {
        readerGroups.add(new ReaderGroupRuntime(service, this, groupConfig));
      }
      this.readerGroups = List.copyOf(readerGroups);

      refreshSubscriberMappings();

      this.discoveryRuntime =
          config instanceof UdpConnectionConfig udpConfig
              ? new DiscoveryRuntime(service, this, udpConfig)
              : null;

      // broker connections publish retained DataSetMetaData to metadata queues; on UDP
      // connections UADP discovery handles metadata instead
      this.metaDataPublisher =
          config instanceof UdpConnectionConfig ? null : new MetaDataPublisher(service, this);
    } catch (RuntimeException | Error e) {
      if (dedicatedEventLoop != null) {
        dedicatedEventLoop.shutdownGracefully(0, 2, TimeUnit.SECONDS);
      }
      throw e;
    }
  }

  PubSubConnectionConfig config() {
    return config;
  }

  List<WriterGroupRuntime> writerGroupRuntimes() {
    return writerGroups;
  }

  List<ReaderGroupRuntime> readerGroupRuntimes() {
    return readerGroups;
  }

  /** The mapping providers used to decode datagrams received on this connection, by name. */
  Map<String, MessageMappingProvider> subscriberMappings() {
    return subscriberMappings;
  }

  @Nullable PublisherChannel publisherChannel() {
    return publisherChannel;
  }

  /** The discovery runtime of this connection, or null when it is not a UDP connection. */
  @Nullable DiscoveryRuntime discoveryRuntime() {
    return discoveryRuntime;
  }

  /** The chunk reassembler of this connection; use only from the dispatch queue. */
  ChunkReassembler chunkReassembler() {
    return chunkReassembler;
  }

  /** The metadata publisher of this connection, or null when it is not a broker connection. */
  @Nullable MetaDataPublisher metaDataPublisher() {
    return metaDataPublisher;
  }

  /**
   * Submit a task to the per-connection dispatch queue, serializing it with datagram decode and
   * listener delivery.
   *
   * @throws RejectedExecutionException if the transport executor rejects the task.
   */
  void submitToDispatchQueue(Runnable task) {
    dispatchQueue.submit(task);
  }

  @Override
  List<? extends AbstractComponentRuntime> children() {
    var children =
        new ArrayList<AbstractComponentRuntime>(writerGroups.size() + readerGroups.size());
    children.addAll(writerGroups);
    children.addAll(readerGroups);
    return children;
  }

  @Override
  void activate() throws UaException {
    openChannels();
  }

  @Override
  void deactivate() {
    closeChannels();
  }

  /** Open the channels this connection's configured groups require; idempotent. */
  void openChannels() throws UaException {
    if (!writerGroups.isEmpty()) {
      ensurePublisherChannel();
    }
    if (!readerGroups.isEmpty()) {
      ensureSubscriberChannel();
    }
  }

  /** Open the publisher channel if it is not already open. Called under the engine lock. */
  void ensurePublisherChannel() throws UaException {
    if (disposed) {
      throw new UaException(
          StatusCodes.Bad_InvalidState,
          "connection '%s' has been disposed".formatted(config.name()));
    }

    // a writer group enabled after startup may activate discovery (responder leg) on demand;
    // discovery open failures are recorded in diagnostics, never thrown
    if (discoveryRuntime != null) {
      discoveryRuntime.ensureChannels();
    }

    if (publisherChannel != null) {
      return;
    }

    TransportProvider provider = requireTransportProvider();

    try {
      publisherChannel =
          provider.openPublisher(
              PublisherTransportContext.of(config, eventLoopGroup(), transportStateHandler));
    } catch (UaException e) {
      service
          .getDiagnostics()
          .error(
              path(),
              e.getStatusCode(),
              "failed to open publisher channel for connection '%s': %s"
                  .formatted(config.name(), e.getMessage()),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      throw e;
    }
  }

  /** Open the subscriber channel if it is not already open. Called under the engine lock. */
  void ensureSubscriberChannel() throws UaException {
    if (disposed) {
      throw new UaException(
          StatusCodes.Bad_InvalidState,
          "connection '%s' has been disposed".formatted(config.name()));
    }

    // a reader group enabled after startup may activate discovery (announcement listening or
    // probe emission) on demand; discovery open failures are recorded, never thrown
    if (discoveryRuntime != null) {
      discoveryRuntime.ensureChannels();
    }

    if (subscriberChannel != null) {
      return;
    }

    TransportProvider provider = requireTransportProvider();

    try {
      subscriberChannel =
          provider.openSubscriber(
              SubscriberTransportContext.of(
                  config,
                  eventLoopGroup(),
                  this::onDatagram,
                  this::onTopicMessage,
                  transportStateHandler));
    } catch (UaException e) {
      service
          .getDiagnostics()
          .error(
              path(),
              e.getStatusCode(),
              "failed to open subscriber channel for connection '%s': %s"
                  .formatted(config.name(), e.getMessage()),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      throw e;
    }
  }

  /** Close any open channels; idempotent. */
  void closeChannels() {
    PublisherChannel publisherChannel = this.publisherChannel;
    this.publisherChannel = null;
    if (publisherChannel != null) {
      publisherChannel
          .closeAsync()
          .whenComplete(
              (v, ex) -> {
                if (ex != null) {
                  LOGGER.warn("Error closing publisher channel of '{}'", path(), ex);
                }
              });
    }

    SubscriberChannel subscriberChannel = this.subscriberChannel;
    this.subscriberChannel = null;
    if (subscriberChannel != null) {
      subscriberChannel
          .closeAsync()
          .whenComplete(
              (v, ex) -> {
                if (ex != null) {
                  LOGGER.warn("Error closing subscriber channel of '{}'", path(), ex);
                }
              });
    }

    if (discoveryRuntime != null) {
      discoveryRuntime.closeChannels();
    }
  }

  /**
   * Release all resources of this runtime: channels, child runtimes, and the per-connection event
   * loop, if any. The runtime is unusable afterwards.
   *
   * <p>Shutdown of the per-connection event loop is only triggered, never awaited: this is called
   * under the engine lock, and awaiting here would stall unrelated connections. The caller awaits
   * the returned future after releasing the engine lock.
   *
   * @return a future that completes when the per-connection event loop has terminated, or {@code
   *     null} if there is no per-connection event loop.
   */
  @Nullable Future<?> dispose() {
    disposed = true;

    closeChannels();

    if (discoveryRuntime != null) {
      discoveryRuntime.dispose();
    }

    if (metaDataPublisher != null) {
      metaDataPublisher.dispose();
    }

    writerGroups.forEach(WriterGroupRuntime::dispose);
    readerGroups.forEach(ReaderGroupRuntime::dispose);

    if (dedicatedEventLoop != null) {
      return dedicatedEventLoop.shutdownGracefully(0, 2, TimeUnit.SECONDS);
    } else {
      return null;
    }
  }

  void addWriterGroupRuntime(WriterGroupRuntime group) {
    var groups = new ArrayList<>(writerGroups);
    groups.add(group);
    writerGroups = List.copyOf(groups);
  }

  void removeWriterGroupRuntime(WriterGroupRuntime group) {
    var groups = new ArrayList<>(writerGroups);
    groups.remove(group);
    writerGroups = List.copyOf(groups);
  }

  @Nullable WriterGroupRuntime findWriterGroupRuntime(String name) {
    for (WriterGroupRuntime group : writerGroups) {
      if (group.config().getName().equals(name)) {
        return group;
      }
    }
    return null;
  }

  void addReaderGroupRuntime(ReaderGroupRuntime group) {
    var groups = new ArrayList<>(readerGroups);
    groups.add(group);
    readerGroups = List.copyOf(groups);
    refreshSubscriberMappings();
  }

  void removeReaderGroupRuntime(ReaderGroupRuntime group) {
    var groups = new ArrayList<>(readerGroups);
    groups.remove(group);
    readerGroups = List.copyOf(groups);
    refreshSubscriberMappings();
  }

  @Nullable ReaderGroupRuntime findReaderGroupRuntime(String name) {
    for (ReaderGroupRuntime group : readerGroups) {
      if (group.config().getName().equals(name)) {
        return group;
      }
    }
    return null;
  }

  /** Recompute the decode mappings from the current reader runtimes. */
  void refreshSubscriberMappings() {
    var mappings = new LinkedHashMap<String, MessageMappingProvider>();

    for (ReaderGroupRuntime group : readerGroups) {
      for (DataSetReaderRuntime reader : group.readerRuntimes()) {
        String mappingName = reader.mappingName();
        if (!mappings.containsKey(mappingName)) {
          MessageMappingProvider provider = service.resolveMappingProvider(mappingName);
          if (provider != null) {
            mappings.put(mappingName, provider);
          }
        }
      }
    }

    subscriberMappings = Map.copyOf(mappings);
  }

  EventLoopGroup eventLoopGroup() {
    return dedicatedEventLoop != null
        ? dedicatedEventLoop
        : service.getServiceConfig().getEventLoopGroup();
  }

  private TransportProvider requireTransportProvider() throws UaException {
    TransportProvider provider = transportProvider;

    if (provider == null) {
      var e =
          new UaException(
              StatusCodes.Bad_ConfigurationError,
              "no TransportProvider supports connection '%s'".formatted(config.name()));

      service
          .getDiagnostics()
          .error(
              path(),
              e.getStatusCode(),
              e.getMessage(),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);

      throw e;
    }

    return provider;
  }

  /**
   * Invoked by topic-aware transports for messages with a known source topic. Decode is
   * content-based (every payload is offered to the connection's mappings), so the topic is not
   * consulted yet; the surface exists so broker transports can deliver uniformly.
   */
  private void onTopicMessage(String topic, ByteBuf buffer) {
    onDatagram(buffer);
  }

  /**
   * Invoked by the transport on its event loop; hops to the transport executor immediately, via the
   * per-connection {@link #dispatchQueue} so decode and listener delivery preserve arrival order.
   */
  private void onDatagram(ByteBuf buffer) {
    if (disposed) {
      return;
    }

    buffer.retain();
    try {
      dispatchQueue.submit(
          () -> {
            try {
              service.getReaderDispatcher().dispatch(this, buffer);
            } catch (Throwable t) {
              LOGGER.warn("Error dispatching datagram on '{}'", path(), t);
            } finally {
              buffer.release();
            }
          });
    } catch (RejectedExecutionException e) {
      buffer.release();
    }
  }

  /**
   * Maps transport liveness edges onto the connection's PubSubState: a down edge fails the
   * connection ({@code Bad_ServerNotConnected} for broker transports) and a subsequent up edge
   * recovers it, cascading to the child runtimes per the engine's state rules.
   *
   * <p>Edge tracking (CAS on {@link #transportDown}) makes duplicate notifications (both channels
   * of a connection may share one session) and the initial connect (an up with no prior down)
   * harmless, and prevents a transport-up from "recovering" an Error the transport did not cause —
   * an activation-failure Error has no preceding down edge.
   *
   * <p>Threading: callbacks arrive on transport threads and only CAS + enqueue; the state machine
   * runs on the serialized per-connection dispatch queue (order-preserving down→up), where {@code
   * fail}/{@code recover} take the engine lock. A handler firing on a disposed runtime (reconfigure
   * or shutdown race) is inert: the {@code disposed} check plus the state machine's no-op
   * transitions leave a replaced runtime untouched.
   */
  private final class TransportStateHandler implements TransportStateListener {

    private final AtomicBoolean transportDown = new AtomicBoolean(false);

    @Override
    public void onTransportDown(StatusCode statusCode) {
      if (disposed || !transportDown.compareAndSet(false, true)) {
        return;
      }
      submitSafely(
          () -> {
            if (!disposed) {
              service.getStateMachine().fail(ConnectionRuntime.this, statusCode);
            }
          });
    }

    @Override
    public void onTransportUp() {
      if (disposed || !transportDown.compareAndSet(true, false)) {
        return;
      }
      submitSafely(
          () -> {
            if (!disposed) {
              service.getStateMachine().recover(ConnectionRuntime.this);
            }
          });
    }

    private void submitSafely(Runnable task) {
      try {
        submitToDispatchQueue(task);
      } catch (RejectedExecutionException e) {
        // the service is shutting down; the edge is moot
      }
    }
  }
}
