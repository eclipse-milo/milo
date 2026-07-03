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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
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
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageSecurityContext;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
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
 * B.3.1). Any NetworkMessage that is not transmitted — encode failure, oversize skip, send failure
 * — invalidates the delta baselines of the writers whose data DataSetMessages it carried, forcing
 * their next message to be a key frame (see {@link DataSetWriterRuntime}).
 *
 * <p>Keep-alive: when a keep-alive time is configured, a keep-alive DataSetMessage is emitted for
 * every writer that has not had a DataSetMessage handed to the transport channel within the
 * keep-alive time (Part 14 §6.2.6.3: "no DataSetMessage was sent in this period"). That covers
 * non-operational writers, operational writers whose delta cycles are suppressed because no fields
 * changed (§6.2.4.3), and — via a second keep-alive pass after the partitions were published —
 * operational writers whose data DataSetMessages were produced but never transmitted (oversize
 * skip, encode failure, synchronous send failure): "sent" means handed to the channel, never merely
 * drafted, so a writer whose data perpetually exceeds the size budget still announces itself with
 * keep-alives, which are header-only and fit budgets its data does not (§7.2.4.5.8). Writers in
 * {@code PubSubState.Error} emit no keep-alives: a keep-alive asserts the writer is still active
 * (§6.2.6.3), which an Error writer is not — emitting one would mask e.g. an activation failure
 * from subscribers. Disabled and Paused writers keep emitting keep-alives.
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

  /** Whether this group's configured mode is Sign or SignAndEncrypt (Invalid counts as None). */
  private final boolean secured;

  /** The SecurityGroup providing this group's keys; non-null when {@link #secured}. */
  private final @Nullable SecurityGroupRef securityGroupRef;

  /**
   * Whether the current run of key-unavailable cycle skips has emitted its edge-triggered
   * diagnostics event: the first skip after a successful cycle records an error event, further
   * skips tick {@code encryptionErrors} quietly. Effectively publish-thread confined; the reset in
   * {@link #activate()} races an in-flight stale cycle at worst into one extra or one suppressed
   * event, never into corrupt state.
   */
  private boolean securitySkipSignaled = false;

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

    MessageSecurityConfig security = config.getMessageSecurity();
    this.secured = PubSubServiceImpl.isSecured(security);
    this.securityGroupRef = security != null && secured ? security.getSecurityGroup() : null;

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

  /**
   * Whether startup completes as soon as {@link #activate()} returns: a secured group stays {@code
   * PreOperational} until the {@link SecurityKeyManager} completes its first key fetch.
   */
  @Override
  boolean startupCompletesImmediately() {
    return !secured;
  }

  /**
   * Activate the group: re-run the activation-time subset of the startup validation (message
   * security, unsupported UADP emission features, mapping-provider resolution) and schedule the
   * publish task. Validation only ever sees enabled components, so a group configured disabled at
   * startup and enabled later is first checked here; a throw is mapped by {@link
   * PubSubStateMachine} to {@code PubSubState.Error} with the exception's status code.
   *
   * <p>A secured group additionally attaches to the {@link SecurityKeyManager}, which initiates the
   * (asynchronous — never awaited under the engine lock) first key fetch and completes this group's
   * startup from its callback.
   */
  @Override
  void activate() throws UaException {
    checkMessageSecurity();
    checkUnsupportedUadpFeatures();

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

    if (secured) {
      MessageSecurityConfig security = config.getMessageSecurity();
      SecurityGroupRef ref = securityGroupRef;
      if (security == null || ref == null) {
        // unreachable: checkMessageSecurity() rejects a secured group without a ref
        throw new UaException(
            StatusCodes.Bad_ConfigurationError,
            "secured writer group '%s' has no SecurityGroup reference".formatted(path()));
      }
      securitySkipSignaled = false;
      service
          .getSecurityKeyManager()
          .attachPublisher(this, security, service.requireSecurityGroup(ref));
    }

    groupVersion = resolveGroupVersion();

    long now = System.nanoTime();
    writers.forEach(
        w -> {
          w.resetLastSent(now);
          // first message after a (re)activation is a key frame. The reset is only REQUESTED
          // here: a stale publish cycle that passed its generation check before this point may
          // still be executing, and the frame state is publish-thread-confined, so the writer
          // applies the reset at the top of its next data draft on the publish task thread —
          // before any frame decision — instead of this thread mutating it mid-cycle.
          w.requestFrameStateReset();
        });

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

    if (secured && securityGroupRef != null) {
      // reconfigure and dispose both flow through deactivate, so detach is covered everywhere
      service.getSecurityKeyManager().detach(this, securityGroupRef);
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

  /**
   * Activation-time copy of the startup/reconfigure message security gate ({@link
   * PubSubServiceImpl#checkWriterGroupMessageSecurity}), covering groups enabled after startup: the
   * state machine maps the throw to {@code PubSubState.Error} with the {@code
   * Bad_ConfigurationError} status.
   */
  private void checkMessageSecurity() throws UaException {
    try {
      service.checkWriterGroupMessageSecurity(config, path());
    } catch (UaException e) {
      service.getDiagnostics().error(path(), e.getStatusCode(), e.getMessage(), e);
      throw e;
    }
  }

  /**
   * Re-check the unsupported-UADP-feature rules ({@link
   * PubSubServiceImpl#unsupportedUadpFeatureError}) at activation: group-level PromotedFields and
   * the RawData field-content mask of currently enabled writers, applied only when the "uadp"
   * mapping resolves to the built-in provider. Startup/reconfigure validation tolerates disabled
   * components (config round-trip posture), so this closes the disabled-at-startup-then-enabled gap
   * — the group fails into {@code PubSubState.Error} with {@code Bad_NotSupported} instead of
   * activating and having the encoder backstop reject every publish cycle. A writer enabled later
   * still, after this group activated, is covered by the writer-level re-check of its own RawData
   * bit in {@link DataSetWriterRuntime#activate}, so no enablement order reaches the encoder with
   * an unsupported feature.
   */
  private void checkUnsupportedUadpFeatures() throws UaException {
    List<DataSetWriterConfig> enabledWriters =
        writers.stream()
            .filter(AbstractComponentRuntime::isEnabled)
            .map(DataSetWriterRuntime::config)
            .toList();

    String error = service.unsupportedUadpFeatureError(config, enabledWriters, path());
    if (error != null) {
      var e = new UaException(StatusCodes.Bad_NotSupported, error);
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

    // Resolve the message security context ONCE per cycle, BEFORE any draft is created: drafts
    // commit delta baselines, so a secured cycle without usable keys must skip in its entirety
    // here — a post-draft skip would diff future deltas against values no subscriber received.
    // One cycle = one token, even if the switch deadline passes mid-cycle (subscriber overlap
    // tolerance absorbs it).
    MessageSecurityContext security = null;
    if (secured) {
      security = resolveSecurityContext();
      if (security == null) {
        return; // skipped; counted by resolveSecurityContext
      }
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
      DataSetMessageDraft draft = null;
      if (writer.state() == PubSubState.Operational) {
        // null = suppressed no-change delta cycle, nothing is sent (Part 14 §6.2.4.3); the
        // suppressed writer falls through to the keep-alive check like a non-operational one
        draft = writer.createDataDraft(now);
      }
      if (draft == null
          && writer.state() != PubSubState.Error
          && keepAliveNanos != null
          && nowNanos - writer.lastSentNanos() >= keepAliveNanos) {
        // an Error writer emits no keep-alives: a keep-alive asserts the writer is still active
        // (§6.2.6.3), and emitting one would mask e.g. an activation failure from subscribers
        draft = writer.createKeepAliveDraft(now);
      }
      if (draft == null) {
        continue;
      }

      Partition partition;
      if (broker && hasQueueOverride(writer.config())) {
        partition = new Partition(writer.config());
        partitions.add(partition);
      } else {
        partition = sharedPartition;
      }
      partition.add(writer, draft);
    }

    var notTransmitted = new LinkedHashSet<DataSetWriterRuntime>();
    for (Partition partition : partitions) {
      if (!partition.drafts.isEmpty()) {
        publishPartition(
            mapping,
            channel,
            publisherId,
            now,
            nowNanos,
            broker,
            partition,
            security,
            notTransmitted);
      }
    }

    if (keepAliveNanos != null && !notTransmitted.isEmpty()) {
      publishKeepAlivesForUntransmitted(
          mapping,
          channel,
          publisherId,
          now,
          nowNanos,
          keepAliveNanos,
          broker,
          security,
          notTransmitted);
    }
  }

  /**
   * Build the {@link MessageSecurityContext} for one publish cycle from the key manager's publisher
   * snapshot, or record the skipped cycle and return {@code null} when no usable key material is
   * available (keys expired without replacement, a failed key source, an exhausted nonce sequence —
   * the key manager nulls the snapshot in each case). Skips tick {@code encryptionErrors} once per
   * cycle; the error event is edge-triggered: emitted on the first skip after a successful cycle,
   * quiet ticks thereafter.
   */
  private @Nullable MessageSecurityContext resolveSecurityContext() {
    MessageSecurityConfig security = config.getMessageSecurity();
    SecurityGroupRef ref = securityGroupRef;

    SecurityKeyManager.PublisherKeys keys =
        ref != null ? service.getSecurityKeyManager().currentPublisherKeys(ref) : null;

    if (keys == null || security == null || ref == null) {
      service.getDiagnostics().encryptionError(path());
      if (!securitySkipSignaled) {
        securitySkipSignaled = true;
        service
            .getDiagnostics()
            .error(
                path(),
                new StatusCode(StatusCodes.Bad_SecurityChecksFailed),
                "publish cycle skipped: no usable security keys for SecurityGroup '%s'"
                    .formatted(ref != null ? ref.name() : "<none>"),
                null);
      }
      return null;
    }

    securitySkipSignaled = false;

    return new MessageSecurityContext(
        security.getMode(), uint(keys.tokenId()), keys.keys(), keys.nonceSupplier());
  }

  /**
   * The current token id and time to the next key switch of this group's SecurityGroup, or {@code
   * null} when the group is not secured (or not attached): the source for the future Part 14
   * §9.1.11 Table 331 {@code SecurityTokenID}/{@code TimeToNextTokenID} LiveValues.
   */
  SecurityKeyManager.@Nullable SecurityGroupKeyView securityKeyView() {
    SecurityGroupRef ref = securityGroupRef;
    return ref != null ? service.getSecurityKeyManager().view(ref) : null;
  }

  /**
   * Second keep-alive pass for the writers whose data DataSetMessages this cycle produced but never
   * transmitted (oversize skip, encode failure, synchronous send failure): such a writer bypassed
   * the regular keep-alive branch — it HAD a draft — yet nothing of it reached the wire, and its
   * {@code lastSentNanos} correctly did not advance. Once the keep-alive time has elapsed since its
   * last actual transmission, a keep-alive is emitted in its place (Part 14 §6.2.6.3: "no
   * DataSetMessage was sent in this period"); keep-alives are header-only (§7.2.4.5.8) and fit size
   * budgets the data does not, so a writer whose data is perpetually skipped still announces itself
   * instead of going silent until its subscribers' receive timeouts expire. Writers in {@code
   * PubSubState.Error} are excluded, like in the regular keep-alive branch.
   */
  private void publishKeepAlivesForUntransmitted(
      MessageMappingProvider mapping,
      PublisherChannel channel,
      PublisherId publisherId,
      DateTime now,
      long nowNanos,
      long keepAliveNanos,
      boolean broker,
      @Nullable MessageSecurityContext security,
      Set<DataSetWriterRuntime> notTransmitted) {

    var sharedPartition = new Partition(null);
    var partitions = new ArrayList<Partition>(1 + notTransmitted.size());
    partitions.add(sharedPartition);

    for (DataSetWriterRuntime writer : notTransmitted) {
      if (writer.state() == PubSubState.Error
          || nowNanos - writer.lastSentNanos() < keepAliveNanos) {
        continue;
      }
      DataSetMessageDraft draft = writer.createKeepAliveDraft(now);

      Partition partition;
      if (broker && hasQueueOverride(writer.config())) {
        partition = new Partition(writer.config());
        partitions.add(partition);
      } else {
        partition = sharedPartition;
      }
      partition.add(writer, draft);
    }

    for (Partition partition : partitions) {
      if (!partition.drafts.isEmpty()) {
        // null: a keep-alive that is itself not transmitted gets no further recovery this
        // cycle; lastSentNanos stays behind, so the next cycle simply tries again
        publishPartition(
            mapping, channel, publisherId, now, nowNanos, broker, partition, security, null);
      }
    }
  }

  /**
   * Encode one partition's drafts and send the resulting NetworkMessages to its address.
   *
   * <p>A NetworkMessage that is NOT transmitted — encode failure, oversize skip, send failure —
   * additionally invalidates the delta baseline of every writer whose data DataSetMessage it
   * carried ({@link DataSetWriterRuntime#invalidateDeltaBaseline()}): the §5.3.3 baseline is the
   * last TRANSMITTED per-field values, and the writers' drafts already committed theirs, so without
   * the invalidation the next delta would diff against values no subscriber received. The forced
   * key frame on the next cycle retransmits the full field image; the dropped message's consumed
   * sequence number is not rolled back (the gap is wire-indistinguishable from network loss).
   *
   * <p>Conversely, a NetworkMessage that IS handed to the transport channel advances {@code
   * lastSentNanos} for every writer it is attributed to — keep-alive drafts included — so the
   * keep-alive cadence (§6.2.6.3 "no DataSetMessage was sent in this period") reflects actual
   * transmissions, never drafts that died at the size gate. Hand-off is the commit point: an
   * asynchronous send failure reported later still invalidates the baseline but does not rewind
   * {@code lastSentNanos}.
   *
   * @param notTransmitted collects the writers whose data DataSetMessages were synchronously not
   *     transmitted (encode failure, oversize skip, synchronous send failure), so the caller can
   *     run the keep-alive recovery pass for them; {@code null} to not collect (the recovery pass
   *     itself). Writers whose only loss was a keep-alive draft are not collected.
   */
  private void publishPartition(
      MessageMappingProvider mapping,
      PublisherChannel channel,
      PublisherId publisherId,
      DateTime now,
      long nowNanos,
      boolean broker,
      Partition partition,
      @Nullable MessageSecurityContext security,
      @Nullable Set<DataSetWriterRuntime> notTransmitted) {

    var encodeContext =
        EncodeContext.of(
            service.getEncodingContext(),
            publisherId,
            config,
            groupVersion,
            ushort(1),
            nextNetworkMessageSequenceNumber(),
            networkMessageTimestamp(now),
            partition.drafts,
            security);

    List<EncodedNetworkMessage> encodedMessages;
    try {
      encodedMessages = mapping.encode(encodeContext);
    } catch (Exception e) {
      service
          .getDiagnostics()
          .error(path(), statusCodeOf(e), "failed to encode NetworkMessage: " + e.getMessage(), e);
      // nothing of this partition was transmitted
      invalidateDeltaBaselines(partition, List.of(), notTransmitted);
      return;
    }

    encodedMessages = dropOversizeMessages(encodedMessages, partition, notTransmitted);

    if (encodedMessages.isEmpty()) {
      return;
    }

    MessageAddress address =
        broker ? brokerDataAddress(publisherId, partition.overrideWriter) : udpDataAddress();

    int index = 0;
    try {
      for (; index < encodedMessages.size(); index++) {
        EncodedNetworkMessage encoded = encodedMessages.get(index);
        channel
            .send(encoded.data(), address)
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
                    // not transmitted; may run on a transport thread, after later cycles —
                    // invalidateDeltaBaseline is the thread-safe seam and heals at the latest
                    // one cycle after it lands (lastSentNanos is NOT rewound: the message was
                    // handed to the channel, which is the keep-alive commit point)
                    invalidateDeltaBaselines(partition, encoded.writers(), null);
                  }
                });

        // the message was handed to the channel: commit the keep-alive reference of every
        // writer it is attributed to, keep-alive drafts included (§6.2.6.3 counts any sent
        // DataSetMessage)
        commitLastSent(partition, encoded.writers(), nowNanos);

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
      // neither the message whose send threw nor the unsent remainder was transmitted
      for (int i = index; i < encodedMessages.size(); i++) {
        invalidateDeltaBaselines(partition, encodedMessages.get(i).writers(), notTransmitted);
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
   *
   * <p>A skipped message invalidates its contributing writers' delta baselines (it was never
   * transmitted) and does not advance their {@code lastSentNanos}. A writer whose KEY frame exceeds
   * the budget therefore re-forces (and re-drops) a key frame every cycle and transmits nothing but
   * keep-alives, once due, when a keep-alive time is configured — an honest, per-cycle-diagnosed
   * failure — rather than emitting deltas against a baseline that can never be transmitted.
   */
  private List<EncodedNetworkMessage> dropOversizeMessages(
      List<EncodedNetworkMessage> encodedMessages,
      Partition partition,
      @Nullable Set<DataSetWriterRuntime> notTransmitted) {

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
        invalidateDeltaBaselines(partition, encoded.writers(), notTransmitted);
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

  /**
   * Invalidate the delta baselines of the partition writers whose data DataSetMessages were carried
   * by a NetworkMessage that was not transmitted, so their next data draft is a key frame (the
   * §5.3.3 baseline is the last TRANSMITTED values). Keep-alive drafts are skipped: they carry no
   * field values, so losing one leaves the baseline accurate (and the writer's {@code
   * lastSentNanos} not advancing already retries the keep-alive on the next cycle).
   *
   * @param attribution the {@link EncodedNetworkMessage#writers()} of the dropped message; empty
   *     means unattributed (e.g. a custom mapping), conservatively treated as carrying every data
   *     draft of the partition — a spurious key frame is always safe, a missed one is not.
   * @param notTransmitted when non-null, additionally collects the affected writers for the
   *     caller's keep-alive recovery pass.
   */
  private static void invalidateDeltaBaselines(
      Partition partition,
      List<EncodedNetworkMessage.Writer> attribution,
      @Nullable Set<DataSetWriterRuntime> notTransmitted) {

    for (int i = 0; i < partition.drafts.size(); i++) {
      DataSetMessageDraft draft = partition.drafts.get(i);
      if (draft.keepAlive()) {
        continue;
      }
      if (attribution.isEmpty() || attributionContains(attribution, draft.writer().getName())) {
        partition.contributors.get(i).invalidateDeltaBaseline();
        if (notTransmitted != null) {
          notTransmitted.add(partition.contributors.get(i));
        }
      }
    }
  }

  /**
   * Advance the keep-alive reference ({@code lastSentNanos}) of the partition writers whose
   * DataSetMessages — data or keep-alive — were carried by a NetworkMessage that was handed to the
   * transport channel. The transmit-time commit (rather than draft-time) keeps the §6.2.6.3
   * keep-alive cadence honest: a draft dropped at the size gate (or never encoded) leaves the
   * writer's reference untouched, so keep-alive emission stays reachable for it.
   *
   * @param attribution the {@link EncodedNetworkMessage#writers()} of the sent message; empty means
   *     unattributed (e.g. a custom mapping), treated as carrying every draft of the partition,
   *     mirroring {@link #invalidateDeltaBaselines}.
   */
  private static void commitLastSent(
      Partition partition, List<EncodedNetworkMessage.Writer> attribution, long nowNanos) {

    for (int i = 0; i < partition.drafts.size(); i++) {
      DataSetMessageDraft draft = partition.drafts.get(i);
      if (attribution.isEmpty() || attributionContains(attribution, draft.writer().getName())) {
        partition.contributors.get(i).resetLastSent(nowNanos);
      }
    }
  }

  private static boolean attributionContains(
      List<EncodedNetworkMessage.Writer> attribution, String writerName) {

    for (EncodedNetworkMessage.Writer writer : attribution) {
      if (writer.name().equals(writerName)) {
        return true;
      }
    }
    return false;
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
   * One NetworkMessage partition of a publish cycle: its drafts, the runtimes that contributed them
   * (parallel to {@link #drafts}; consulted when a NetworkMessage is not transmitted and the
   * contributing writers' delta baselines must be invalidated), and the writer owning its queue.
   */
  private static final class Partition {

    /** The writer whose queue override this partition publishes to; null = the shared partition. */
    final @Nullable DataSetWriterConfig overrideWriter;

    final List<DataSetMessageDraft> drafts = new ArrayList<>();
    final List<DataSetWriterRuntime> contributors = new ArrayList<>();

    private Partition(@Nullable DataSetWriterConfig overrideWriter) {
      this.overrideWriter = overrideWriter;
    }

    void add(DataSetWriterRuntime contributor, DataSetMessageDraft draft) {
      drafts.add(draft);
      contributors.add(contributor);
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
