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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerQualityOfService;
import org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerTopics;
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageMappingProvider;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runtime for one WriterGroup: a publish task scheduled at the publishing interval that pulls a
 * snapshot from each operational writer's source, encodes the writers' DataSetMessages into
 * NetworkMessages, and sends them via the connection's publisher channel with a fully resolved
 * {@link MessageAddress}.
 *
 * <p>By default all writers' DataSetMessages are combined into one partition per cycle (a single
 * NetworkMessage for the UADP mapping). On broker connections a writer-level queue override splits
 * that writer's DataSetMessages into their own partition, published to the writer's queue (Part 14
 * §6.4.2.5.1); queue names and delivery guarantees are resolved engine-side per §7.3.4.7/§7.3.4.5,
 * so transports apply the address verbatim. A mapping may also split one partition into multiple
 * NetworkMessages (e.g. the JSON mapping's SingleDataSetMessage); the engine sends every message
 * the mapping returns, except those exceeding the group's non-zero {@code maxNetworkMessageSize},
 * which are skipped and recorded with {@code Bad_EncodingLimitsExceeded} (Part 14 §6.2.5.5, Annex
 * B.3.1).
 *
 * <p>Keep-alive: when a keep-alive time is configured, a keep-alive DataSetMessage is emitted for
 * every writer that has not produced a DataSetMessage within the keep-alive time (in practice the
 * non-operational writers, since operational writers publish a key frame every cycle).
 *
 * <p>The publish task is the single writer of all sequence counters; everything it reads from the
 * runtime tree is either immutable config or a volatile snapshot, so reconfiguration takes effect
 * on the next cycle. Deactivation cancels the task without interrupting an in-flight cycle, so
 * cycles additionally hold {@link #publishLock} and bail when their activation generation is stale:
 * a cycle left over from before a deactivate/re-activate can never run concurrently with (or after)
 * the new task's cycles.
 */
final class WriterGroupRuntime extends AbstractComponentRuntime {

  /** Seconds between 1970-01-01T00:00:00Z and the spec VersionTime epoch 2000-01-01T00:00:00Z. */
  private static final long VERSION_TIME_EPOCH_SECONDS = 946_684_800L;

  private static final Logger LOGGER = LoggerFactory.getLogger(WriterGroupRuntime.class);

  private final PubSubServiceImpl service;
  private final ConnectionRuntime connection;
  private final WriterGroupConfig config;
  private final @Nullable Duration keepAliveTime;
  private final String mappingName;

  private volatile List<DataSetWriterRuntime> writers;
  private volatile @Nullable MessageMappingProvider mapping;
  private volatile UInteger groupVersion = uint(0);

  /** Only touched by the publish task, under {@link #publishLock}. */
  private int networkMessageSequenceNumber = 0;

  /** Serializes publish cycles across activation generations. */
  private final Object publishLock = new Object();

  /**
   * Activation generation: incremented under the engine lock on every activate/deactivate. A
   * publish cycle scheduled by an earlier activation bails when its generation is stale.
   */
  private volatile long generation = 0;

  /** Guarded by the engine lock. */
  private @Nullable ScheduledFuture<?> publishTask;

  WriterGroupRuntime(
      PubSubServiceImpl service, ConnectionRuntime connection, WriterGroupConfig config) {

    super(
        ComponentType.WRITER_GROUP,
        connection.path() + "/" + config.getName(),
        connection,
        config.isEnabled());

    this.service = service;
    this.connection = connection;
    this.config = config;
    this.keepAliveTime = config.getKeepAliveTime();
    this.mappingName = PubSubServiceImpl.mappingNameOf(config.getMessageSettings());

    var writers = new ArrayList<DataSetWriterRuntime>();
    for (DataSetWriterConfig writerConfig : config.getDataSetWriters()) {
      writers.add(new DataSetWriterRuntime(service, this, writerConfig));
    }
    this.writers = List.copyOf(writers);
  }

  WriterGroupConfig config() {
    return config;
  }

  ConnectionRuntime connectionRuntime() {
    return connection;
  }

  /** The name of the message mapping this group's messages are encoded with. */
  String mappingName() {
    return mappingName;
  }

  /** The resolved mapping provider; non-null while the group is active. */
  @Nullable MessageMappingProvider mapping() {
    return mapping;
  }

  List<DataSetWriterRuntime> writerRuntimes() {
    return writers;
  }

  @Override
  List<? extends AbstractComponentRuntime> children() {
    return writers;
  }

  @Override
  void activate() throws UaException {
    checkMessageSecurity();

    MessageMappingProvider provider = service.resolveMappingProvider(mappingName);
    if (provider == null) {
      var e =
          new UaException(
              StatusCodes.Bad_ConfigurationError,
              "no MessageMappingProvider for mapping '%s' (writer group '%s')"
                  .formatted(mappingName, path()));
      service.getDiagnostics().error(path(), e.getStatusCode(), e.getMessage(), e);
      throw e;
    }
    mapping = provider;

    connection.ensurePublisherChannel();

    groupVersion = resolveGroupVersion();

    long now = System.nanoTime();
    writers.forEach(w -> w.resetLastSent(now));

    long intervalNanos = config.getPublishingInterval().toNanos();
    long generation = ++this.generation;
    publishTask =
        service
            .getScheduledExecutor()
            .scheduleAtFixedRate(
                () -> publishSafely(generation), 0, intervalNanos, TimeUnit.NANOSECONDS);
  }

  @Override
  void deactivate() {
    generation++;

    ScheduledFuture<?> publishTask = this.publishTask;
    this.publishTask = null;
    if (publishTask != null) {
      publishTask.cancel(false);
    }
  }

  /** Release all resources of this runtime. The runtime is unusable afterwards. */
  void dispose() {
    deactivate();
    writers.forEach(DataSetWriterRuntime::dispose);
  }

  void addWriterRuntime(DataSetWriterRuntime writer) {
    var writers = new ArrayList<>(this.writers);
    writers.add(writer);
    this.writers = List.copyOf(writers);
  }

  void removeWriterRuntime(DataSetWriterRuntime writer) {
    var writers = new ArrayList<>(this.writers);
    writers.remove(writer);
    this.writers = List.copyOf(writers);
  }

  @Nullable DataSetWriterRuntime findWriterRuntime(String name) {
    for (DataSetWriterRuntime writer : writers) {
      if (writer.config().getName().equals(name)) {
        return writer;
      }
    }
    return null;
  }

  private void checkMessageSecurity() throws UaException {
    MessageSecurityConfig security = config.getMessageSecurity();

    if (security != null && security.getMode() != MessageSecurityMode.None) {
      var e =
          new UaException(
              StatusCodes.Bad_NotSupported,
              "MessageSecurityMode %s is not supported (writer group '%s'); only None is"
                      .formatted(security.getMode(), path())
                  + " supported in this version");
      service.getDiagnostics().error(path(), e.getStatusCode(), e.getMessage(), e);
      throw e;
    }
  }

  private UInteger resolveGroupVersion() {
    if (config.getMessageSettings() instanceof UadpWriterGroupSettings settings
        && settings.getGroupVersion().longValue() != 0L) {

      return settings.getGroupVersion();
    } else {
      long versionTime =
          (System.currentTimeMillis() / 1000L - VERSION_TIME_EPOCH_SECONDS) & 0xFFFF_FFFFL;
      return uint(versionTime);
    }
  }

  private void publishSafely(long generation) {
    try {
      synchronized (publishLock) {
        if (generation != this.generation) {
          // stale cycle from a previous activation; the current activation's task owns the
          // sequence counters now
          return;
        }
        publish();
      }
    } catch (Throwable t) {
      LOGGER.warn("Publish cycle of '{}' failed", path(), t);
      service
          .getDiagnostics()
          .error(
              path(),
              new StatusCode(StatusCodes.Bad_InternalError),
              "publish cycle failed: " + t.getMessage(),
              t);
    }
  }

  private void publish() {
    if (state() != PubSubState.Operational) {
      return;
    }

    MessageMappingProvider mapping = this.mapping;
    PublisherChannel channel = connection.publisherChannel();
    PublisherId publisherId = connection.config().publisherId();
    if (mapping == null || channel == null || publisherId == null) {
      return;
    }

    long nowNanos = System.nanoTime();
    DateTime now = DateTime.now();
    Long keepAliveNanos = keepAliveTime != null ? keepAliveTime.toNanos() : null;

    boolean broker = !(connection.config() instanceof UdpConnectionConfig);

    List<DataSetWriterRuntime> writers = this.writers;

    // the shared partition carries the DataSetMessages of every writer without a queue override;
    // on broker connections a writer-level queue override splits that writer's DataSetMessages
    // into their own NetworkMessage(s), published to the writer's queue (Part 14 §6.4.2.5.1)
    var sharedPartition = new Partition(null);
    var partitions = new ArrayList<Partition>(1 + writers.size());
    partitions.add(sharedPartition);

    for (DataSetWriterRuntime writer : writers) {
      DataSetMessageDraft draft;
      if (writer.state() == PubSubState.Operational) {
        draft = writer.createDataDraft(now, nowNanos);
      } else if (keepAliveNanos != null && nowNanos - writer.lastSentNanos() >= keepAliveNanos) {
        draft = writer.createKeepAliveDraft(now, nowNanos);
      } else {
        continue;
      }

      Partition partition;
      if (broker && hasQueueOverride(writer.config())) {
        partition = new Partition(writer.config());
        partitions.add(partition);
      } else {
        partition = sharedPartition;
      }
      partition.drafts.add(draft);
    }

    for (Partition partition : partitions) {
      if (!partition.drafts.isEmpty()) {
        publishPartition(mapping, channel, publisherId, now, broker, partition);
      }
    }
  }

  /** Encode one partition's drafts and send the resulting NetworkMessages to its address. */
  private void publishPartition(
      MessageMappingProvider mapping,
      PublisherChannel channel,
      PublisherId publisherId,
      DateTime now,
      boolean broker,
      Partition partition) {

    var encodeContext =
        EncodeContext.of(
            service.getEncodingContext(),
            publisherId,
            config,
            groupVersion,
            ushort(1),
            nextNetworkMessageSequenceNumber(),
            networkMessageTimestamp(now),
            partition.drafts);

    List<EncodedNetworkMessage> encodedMessages;
    try {
      encodedMessages = mapping.encode(encodeContext);
    } catch (Exception e) {
      service
          .getDiagnostics()
          .error(path(), statusCodeOf(e), "failed to encode NetworkMessage: " + e.getMessage(), e);
      return;
    }

    encodedMessages = dropOversizeMessages(encodedMessages);

    if (encodedMessages.isEmpty()) {
      return;
    }

    MessageAddress address =
        broker ? brokerDataAddress(publisherId, partition.overrideWriter) : udpDataAddress();

    int index = 0;
    try {
      for (; index < encodedMessages.size(); index++) {
        channel
            .send(encodedMessages.get(index).data(), address)
            .whenComplete(
                (v, ex) -> {
                  if (ex != null) {
                    service
                        .getDiagnostics()
                        .error(
                            path(),
                            new StatusCode(StatusCodes.Bad_CommunicationError),
                            "failed to send NetworkMessage: " + ex.getMessage(),
                            ex);
                  }
                });

        service.getDiagnostics().networkMessageSent(connection.path());
        service.getDiagnostics().networkMessageSent(path());
      }
    } catch (RuntimeException e) {
      // a conforming channel owns (and releases) the in-flight buffer even when send throws
      // synchronously (see PublisherChannel#send; never double-release it); the messages not yet
      // handed to the channel are still ours to release
      for (int i = index + 1; i < encodedMessages.size(); i++) {
        encodedMessages.get(i).data().release();
      }
      service
          .getDiagnostics()
          .error(
              path(),
              new StatusCode(StatusCodes.Bad_CommunicationError),
              "failed to send NetworkMessage: " + e.getMessage(),
              e);
      return;
    }

    // attribute dataSetMessagesSent from the messages actually handed to the channel, so
    // DataSetMessages carried only by skipped oversize NetworkMessages are not counted
    int dataSetMessagesSent = 0;
    for (EncodedNetworkMessage encoded : encodedMessages) {
      dataSetMessagesSent += encoded.writers().size();
      for (EncodedNetworkMessage.Writer writer : encoded.writers()) {
        service.getDiagnostics().dataSetMessagesSent(path() + "/" + writer.name(), 1);
      }
    }
    service.getDiagnostics().dataSetMessagesSent(path(), dataSetMessagesSent);
  }

  /**
   * Enforce the group's {@code maxNetworkMessageSize} encode budget (Part 14 §6.2.5.5: the size of
   * the complete encoded NetworkMessage, before transport headers): when non-zero, encoded
   * NetworkMessages exceeding it are released and skipped instead of sent, recording {@code
   * Bad_EncodingLimitsExceeded} with the actual and maximum sizes — the Annex B.3.1 behavior for
   * mappings that do not support chunking ("the NetworkMessages exceeding the maximum size must be
   * skipped. Diagnostic information for such error scenarios are provided."), mirroring the
   * Actual/Maximum properties of PubSubTransportLimitsExceedEventType (§9.1.13.2). 0 = no enforced
   * limit.
   */
  private List<EncodedNetworkMessage> dropOversizeMessages(
      List<EncodedNetworkMessage> encodedMessages) {

    long maxNetworkMessageSize = config.getMaxNetworkMessageSize().longValue();
    if (maxNetworkMessageSize == 0) {
      return encodedMessages;
    }

    List<EncodedNetworkMessage> accepted = null; // lazily created when a message is dropped
    for (int index = 0; index < encodedMessages.size(); index++) {
      EncodedNetworkMessage encoded = encodedMessages.get(index);

      int actualSize = encoded.data().readableBytes();
      if (actualSize > maxNetworkMessageSize) {
        if (accepted == null) {
          accepted = new ArrayList<>(encodedMessages.subList(0, index));
        }
        encoded.data().release();
        service
            .getDiagnostics()
            .error(
                path(),
                new StatusCode(StatusCodes.Bad_EncodingLimitsExceeded),
                "NetworkMessage skipped: actual size %d exceeds maximum size %d"
                    .formatted(actualSize, maxNetworkMessageSize),
                null);
      } else if (accepted != null) {
        accepted.add(encoded);
      }
    }

    return accepted != null ? accepted : encodedMessages;
  }

  /** The address of data NetworkMessages on transports without broker semantics. */
  private MessageAddress udpDataAddress() {
    return MessageAddress.of(
        null,
        BrokerTransportQualityOfService.AtMostOnce,
        false,
        MessageAddress.Kind.DATA,
        MessageAddress.contentTypeOfMapping(mappingName));
  }

  /**
   * The resolved address of one partition's data NetworkMessages on a broker connection: the
   * configured queue name or the Part 14 §7.3.4.7.3 derived topic, the §7.3.4.5 delivery guarantee,
   * and the Table 204 data defaults (not retained).
   */
  private MessageAddress brokerDataAddress(
      PublisherId publisherId, @Nullable DataSetWriterConfig overrideWriter) {

    String queueName =
        overrideWriter != null
            ? BrokerTopics.resolveDataQueueName(
                connection.config(), mappingName, config, overrideWriter)
            : BrokerTopics.resolveDataQueueName(
                connection.config(), mappingName, publisherId, config);

    BrokerTransportQualityOfService deliveryGuarantee =
        BrokerQualityOfService.resolveData(
            config.getBrokerTransport(),
            overrideWriter != null ? overrideWriter.getBrokerTransport() : null);

    return MessageAddress.of(
        queueName,
        deliveryGuarantee,
        false,
        MessageAddress.Kind.DATA,
        MessageAddress.contentTypeOfMapping(mappingName));
  }

  private static boolean hasQueueOverride(DataSetWriterConfig writer) {
    BrokerTransportSettings settings = writer.getBrokerTransport();
    return settings != null
        && settings.getQueueName() != null
        && !settings.getQueueName().isEmpty();
  }

  /**
   * One NetworkMessage partition of a publish cycle: its drafts and the writer owning its queue.
   */
  private static final class Partition {

    /** The writer whose queue override this partition publishes to; null = the shared partition. */
    final @Nullable DataSetWriterConfig overrideWriter;

    final List<DataSetMessageDraft> drafts = new ArrayList<>();

    private Partition(@Nullable DataSetWriterConfig overrideWriter) {
      this.overrideWriter = overrideWriter;
    }
  }

  private @Nullable DateTime networkMessageTimestamp(DateTime now) {
    if (config.getMessageSettings() instanceof UadpWriterGroupSettings settings
        && settings.getNetworkMessageContentMask().getTimestamp()) {

      return now;
    } else {
      return null;
    }
  }

  private UShort nextNetworkMessageSequenceNumber() {
    int value = networkMessageSequenceNumber;
    networkMessageSequenceNumber = (value + 1) & 0xFFFF;
    return ushort(value);
  }

  private static StatusCode statusCodeOf(Exception e) {
    return UaException.extractStatusCode(e).orElse(new StatusCode(StatusCodes.Bad_InternalError));
  }
}
