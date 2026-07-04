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

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataMapper;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpDecodedMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpDiscoveryProbe;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMetaDataAnnouncement;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UADP discovery runtime for one UDP connection (OPC UA Part 14 §7.2.4.6): the publisher-side probe
 * responder and the subscriber-side probe emitter for {@link MetadataPolicy#REQUEST_IF_MISSING}
 * readers, owned by the connection's {@link ConnectionRuntime}.
 *
 * <p>Discovery channels are opened through the built-in UDP transport provider using a synthetic
 * {@link UdpConnectionConfig} that points at the connection's effective discovery address: the
 * configured {@link UdpConnectionConfig#getDiscoveryAddress()} or the Part 14 default {@code
 * opc.udp://224.0.2.14:4840} (§7.3.2.1) on the connection's network interface. Both legs share the
 * same need predicate: the responder is active iff the connection has writer groups, and the
 * subscriber leg is needed when the responder is active (to hear probes) or any reader has the
 * {@code REQUEST_IF_MISSING} policy (to hear announcements); the send leg additionally serves probe
 * emission, which targets the discovery address only. Channel open failures are not fatal to the
 * connection: they are logged and recorded in diagnostics, and discovery stays inactive.
 *
 * <p><b>Responder:</b> answers DataSetMetaData probes whose target PublisherId equals the
 * connection's PublisherId with one announcement per requested DataSetWriterId — metadata derived
 * from the live config via {@link DataSetMetaDataMapper} (MiloSourceKey stripped), or a {@code
 * Bad_NotFound} denial for unknown ids. Repeat answers for the same (probe type, DataSetWriterId)
 * are suppressed for 500 ms (§7.2.4.6.12.2; duplicate probes inside the window are thereby
 * discarded). Announcements carry a UInt16 sequence number scoped per PublisherId (§7.2.4.6.3 Table
 * 168; shared by all connections using the same PublisherId, maintained by the service) and are
 * sent to both the discovery address and the connection's data address (UA-.NETStandard subscribers
 * listen on the data address only). When a reconfiguration changes the metadata of a live writer's
 * dataset, the changed metadata is announced once, unsolicited (§7.2.4.6.4). Periodic announcements
 * (DiscoveryAnnounceRate) are not implemented; the responder is respond-and-push-on-change only.
 *
 * <p><b>Probe emission:</b> a {@code REQUEST_IF_MISSING} reader that activates without effective
 * metadata probes the discovery address: the first probe after a randomized 100-500 ms delay, then
 * retries with doubling intervals from 500 ms up to a 60 s cap, until metadata is obtained or the
 * publisher denies the request (denial → diagnostics error). Readers with wildcard PublisherId or
 * DataSetWriterId filters defer probing until a first matching data NetworkMessage reveals the
 * identifiers.
 *
 * <p><b>Threading:</b> probe timers run on the service scheduled executor; received discovery
 * datagrams and the unsolicited announcement check hop to the connection's dispatch {@link
 * org.eclipse.milo.opcua.stack.core.util.ExecutionQueue}, serializing them with data-plane decode
 * and listener delivery. All mutable state is guarded by this runtime's own lock; the engine lock
 * is never acquired from inside it (engine-lock holders may call in — channel management and reader
 * lifecycle hooks run under the engine lock, matching the data-channel rules).
 */
final class DiscoveryRuntime {

  /** The Part 14 default discovery multicast group (§7.3.2.1). */
  private static final String DEFAULT_DISCOVERY_GROUP = "224.0.2.14";

  /** The recommended discovery port (§7.3.2.1; the spec default URL is portless). */
  private static final int DEFAULT_DISCOVERY_PORT = 4840;

  /** The default DiscoveryMaxMessageSize for UDP (§7.3.2.1); not configurable in this version. */
  private static final int DISCOVERY_MAX_MESSAGE_SIZE = 4096;

  /** Repeat-answer suppression per (probe type, DataSetWriterId) (§7.2.4.6.12.2). */
  private static final long REPEAT_SUPPRESSION_NANOS = TimeUnit.MILLISECONDS.toNanos(500);

  /** Bounds of the randomized first-probe delay (§7.2.4.6.12.2). */
  private static final long FIRST_PROBE_MIN_DELAY_MILLIS = 100;

  private static final long FIRST_PROBE_MAX_DELAY_MILLIS = 500;

  /** The initial probe retry delay; doubled after every probe up to the cap. */
  private static final long RETRY_BASE_DELAY_MILLIS = 500;

  private static final long RETRY_MAX_DELAY_MILLIS = 60_000;

  private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryRuntime.class);

  private final PubSubServiceImpl service;
  private final ConnectionRuntime connection;
  private final UdpConnectionConfig config;
  private final UdpConnectionConfig discoveryConfig;

  private final Object lock = new Object();

  private volatile @Nullable PublisherChannel sendChannel;
  private volatile @Nullable SubscriberChannel receiveChannel;
  private volatile boolean disposed = false;

  /** Whether any probe task is still waiting for wire-learned identifiers. */
  private volatile boolean deferredProbes = false;

  /** Guarded by {@link #lock}. */
  private final Map<ResponseKey, Long> lastAnsweredNanos = new HashMap<>();

  /** Guarded by {@link #lock}. */
  private final Map<PubSubHandle, ProbeTask> probeTasks = new HashMap<>();

  /**
   * The metadata last known per live DataSetWriterId, the comparison baseline for unsolicited
   * announcements on reconfigure. Guarded by {@link #lock}.
   */
  private final Map<UShort, DataSetMetaDataType> announcedMetaData = new LinkedHashMap<>();

  DiscoveryRuntime(
      PubSubServiceImpl service, ConnectionRuntime connection, UdpConnectionConfig config) {

    this.service = service;
    this.connection = connection;
    this.config = config;

    this.discoveryConfig =
        UdpConnectionConfig.builder(config.name() + "-discovery")
            .address(effectiveDiscoveryAddress(config))
            .build();
  }

  /**
   * The discovery address the runtime uses: the configured one, or the spec default on the
   * connection's network interface.
   */
  private static UdpDatagramAddress effectiveDiscoveryAddress(UdpConnectionConfig config) {
    UdpDatagramAddress configured = config.getDiscoveryAddress();
    if (configured != null) {
      return configured;
    }

    UdpDatagramAddress address =
        UdpDatagramAddress.multicast(DEFAULT_DISCOVERY_GROUP, DEFAULT_DISCOVERY_PORT);

    String networkInterface = config.getAddress().networkInterface();
    return networkInterface != null ? address.networkInterface(networkInterface) : address;
  }

  // region channel lifecycle (engine lock)

  /**
   * Open the discovery channels if discovery is required and they are not already open; idempotent.
   * Open failures are recorded in diagnostics but never thrown: discovery is auxiliary and must not
   * fail the connection. Called under the engine lock.
   */
  void ensureChannels() {
    if (!discoveryRequired()) {
      return;
    }

    synchronized (lock) {
      if (disposed) {
        return;
      }

      boolean openedSendChannel = false;

      if (sendChannel == null) {
        try {
          sendChannel =
              service
                  .getUdpTransportProvider()
                  .openPublisher(
                      PublisherTransportContext.of(discoveryConfig, connection.eventLoopGroup()));
          openedSendChannel = true;
        } catch (Exception e) {
          service
              .getDiagnostics()
              .error(
                  connection.path(),
                  DiagnosticsCollector.statusCodeOf(e),
                  "failed to open discovery send channel for connection '%s': %s"
                      .formatted(config.name(), e.getMessage()),
                  e,
                  PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
        }
      }

      if (receiveChannel == null) {
        try {
          receiveChannel =
              service
                  .getUdpTransportProvider()
                  .openSubscriber(
                      SubscriberTransportContext.of(
                          discoveryConfig, connection.eventLoopGroup(), this::onDiscoveryDatagram));
        } catch (Exception e) {
          service
              .getDiagnostics()
              .error(
                  connection.path(),
                  DiagnosticsCollector.statusCodeOf(e),
                  "failed to open discovery receive channel for connection '%s': %s"
                      .formatted(config.name(), e.getMessage()),
                  e,
                  PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
        }
      }

      if (openedSendChannel && responderActive()) {
        // baseline for unsolicited change announcements; activation itself announces nothing
        announcedMetaData.clear();
        announcedMetaData.putAll(deriveLiveMetaData());
      }
    }
  }

  /**
   * Close the discovery channels if no live component requires them anymore; idempotent. Called
   * under the engine lock after a reconfiguration was applied, so a connection whose last writer
   * group (responder) and last {@code REQUEST_IF_MISSING} reader (prober) were removed releases its
   * discovery sockets instead of holding them until dispose. A later reconfigure that re-adds a
   * discovery-requiring component reopens through {@link #ensureChannels()}, which reseeds the
   * responder announcement baseline.
   */
  void closeChannelsIfUnused() {
    if (!discoveryRequired()) {
      closeChannels();
    }
  }

  /** Close any open discovery channels; idempotent. Called under the engine lock. */
  void closeChannels() {
    PublisherChannel sendChannel;
    SubscriberChannel receiveChannel;

    synchronized (lock) {
      sendChannel = this.sendChannel;
      receiveChannel = this.receiveChannel;
      this.sendChannel = null;
      this.receiveChannel = null;
    }

    if (sendChannel != null) {
      sendChannel
          .closeAsync()
          .whenComplete(
              (v, ex) -> {
                if (ex != null) {
                  LOGGER.warn(
                      "Error closing discovery send channel of '{}'", connection.path(), ex);
                }
              });
    }

    if (receiveChannel != null) {
      receiveChannel
          .closeAsync()
          .whenComplete(
              (v, ex) -> {
                if (ex != null) {
                  LOGGER.warn(
                      "Error closing discovery receive channel of '{}'", connection.path(), ex);
                }
              });
    }
  }

  /**
   * Release all resources of this runtime: cancel probe tasks and close the discovery channels (the
   * close is triggered, never awaited, like the data channels). The runtime is unusable afterwards.
   */
  void dispose() {
    synchronized (lock) {
      disposed = true;

      probeTasks.values().forEach(this::cancelTaskLocked);
      probeTasks.clear();
      deferredProbes = false;

      lastAnsweredNanos.clear();
      announcedMetaData.clear();
    }

    closeChannels();
  }

  /** Whether discovery channels are needed at all: responder or prober. */
  private boolean discoveryRequired() {
    return responderActive() || hasRequestIfMissingReaders();
  }

  /** The responder is active iff the connection has writer groups (pinned predicate). */
  private boolean responderActive() {
    return !connection.writerGroupRuntimes().isEmpty();
  }

  private boolean hasRequestIfMissingReaders() {
    for (ReaderGroupRuntime group : connection.readerGroupRuntimes()) {
      for (DataSetReaderRuntime reader : group.readerRuntimes()) {
        if (reader.config().getMetadataPolicy() == MetadataPolicy.REQUEST_IF_MISSING) {
          return true;
        }
      }
    }
    return false;
  }

  // endregion

  // region discovery socket receive path

  /**
   * Invoked by the transport on its event loop; hops to the connection's dispatch queue so decode
   * and listener delivery are serialized with the data plane. Datagrams of one byte or less are
   * dropped before decode: UA-.NETStandard emits a 1-byte connectivity test datagram to the
   * discovery endpoint at client creation.
   */
  private void onDiscoveryDatagram(ByteBuf buffer) {
    if (disposed || buffer.readableBytes() <= 1) {
      return;
    }

    buffer.retain();
    try {
      connection.submitToDispatchQueue(
          () -> {
            try {
              dispatchDiscovery(buffer);
            } catch (Throwable t) {
              LOGGER.warn("Error dispatching discovery datagram on '{}'", connection.path(), t);
            } finally {
              buffer.release();
            }
          });
    } catch (RejectedExecutionException e) {
      buffer.release();
    }
  }

  /** Decode and route one datagram received on the discovery socket. Transport executor. */
  private void dispatchDiscovery(ByteBuf buffer) {
    if (disposed) {
      return;
    }

    UadpDecodedMessage decoded =
        service
            .getUadpMapping()
            .decodeMessage(DecodeContext.of(service.getEncodingContext()), buffer.slice());

    if (decoded instanceof UadpDiscoveryProbe probe) {
      service.getDiagnostics().networkMessageReceived(connection.path());
      onProbeReceived(probe);
    } else if (decoded instanceof UadpMetaDataAnnouncement announcement) {
      service.getDiagnostics().networkMessageReceived(connection.path());
      service
          .getReaderDispatcher()
          .handleAnnouncement(connection, UadpMessageMapping.MAPPING_NAME, announcement);
    }
    // data-plane NetworkMessages cross-delivered to the discovery socket (shared port with the
    // data group on some platforms) are tolerated and ignored
  }

  // endregion

  // region responder

  /**
   * Handle a discovery probe received on either the discovery socket or the connection's data
   * socket. Transport executor.
   */
  void onProbeReceived(UadpDiscoveryProbe probe) {
    if (disposed || !responderActive()) {
      return;
    }

    // the probe's PublisherId identifies the probed Publisher (§7.2.4.6.12.1)
    PublisherId publisherId = config.publisherId();
    if (publisherId == null || !publisherId.equals(probe.targetPublisherId())) {
      return;
    }

    if (probe.dataSetWriterIds().isEmpty()) {
      return;
    }

    long now = System.nanoTime();
    var toAnswer = new ArrayList<UShort>();

    synchronized (lock) {
      if (disposed) {
        return;
      }

      for (UShort dataSetWriterId : new LinkedHashSet<>(probe.dataSetWriterIds())) {
        var key = new ResponseKey(probe.probeType(), dataSetWriterId);

        // §7.2.4.6.12.2: suppress repeat answers for a (probe type, identifier) combination for
        // at least 500 ms; duplicate probes inside the window are thereby discarded
        Long last = lastAnsweredNanos.get(key);
        if (last != null && now - last < REPEAT_SUPPRESSION_NANOS) {
          continue;
        }

        lastAnsweredNanos.put(key, now);
        toAnswer.add(dataSetWriterId);
      }
    }

    for (UShort dataSetWriterId : toAnswer) {
      DataSetMetaDataType metaData = deriveMetaDataForWriter(dataSetWriterId);

      if (metaData != null) {
        announce(dataSetWriterId, metaData, StatusCode.GOOD);
      } else {
        // Table 170: a Bad statusCode is the designed-in denial for ids we cannot provide
        announce(dataSetWriterId, denialMetaData(), new StatusCode(StatusCodes.Bad_NotFound));
      }
    }
  }

  /**
   * Schedule the unsolicited announcement check after a reconfiguration: metadata of a live
   * writer's dataset that changed is announced once (§7.2.4.6.4). Called under the engine lock; the
   * check itself runs on the connection's dispatch queue, off the engine lock.
   */
  void onConfigurationApplied() {
    try {
      connection.submitToDispatchQueue(this::announceChangedMetaData);
    } catch (RejectedExecutionException e) {
      // executor shut down; nothing to announce
    }
  }

  /**
   * Snapshot the unsolicited-announcement comparison baseline: the metadata last announced (or
   * captured at channel open) per live DataSetWriterId. Called under the engine lock before a
   * reconfiguration is applied.
   */
  Map<UShort, DataSetMetaDataType> announcedMetaDataSnapshot() {
    synchronized (lock) {
      return new LinkedHashMap<>(announcedMetaData);
    }
  }

  /**
   * Seed the unsolicited-announcement comparison baseline with the metadata last announced by this
   * runtime's predecessor (the connection was rebuilt by a reconfiguration). The replacement
   * runtime's channel-open baseline derives from the already-updated config and would mask dataset
   * metadata changes applied by the same reconfiguration, skipping the mandatory §7.2.4.6.4
   * announcement. Called under the engine lock, before {@link #onConfigurationApplied()}.
   */
  void seedAnnouncedMetaData(Map<UShort, DataSetMetaDataType> baseline) {
    synchronized (lock) {
      if (disposed) {
        return;
      }
      announcedMetaData.clear();
      announcedMetaData.putAll(baseline);
    }
  }

  private void announceChangedMetaData() {
    if (disposed || !responderActive()) {
      return;
    }

    Map<UShort, DataSetMetaDataType> current = deriveLiveMetaData();

    if (sendChannel == null && connection.publisherChannel() == null) {
      // nothing can be sent (connection inactive); just refresh the comparison baseline
      synchronized (lock) {
        if (!disposed) {
          announcedMetaData.clear();
          announcedMetaData.putAll(current);
        }
      }
      return;
    }

    var changed = new ArrayList<Map.Entry<UShort, DataSetMetaDataType>>();
    long now = System.nanoTime();

    synchronized (lock) {
      if (disposed) {
        return;
      }

      for (Map.Entry<UShort, DataSetMetaDataType> entry : current.entrySet()) {
        if (!entry.getValue().equals(announcedMetaData.get(entry.getKey()))) {
          changed.add(entry);

          // an unsolicited announcement provides the information a probe would request; count
          // it for the repeat-answer suppression window
          lastAnsweredNanos.put(
              new ResponseKey(UadpDiscoveryProbe.PROBE_TYPE_PUBLISHER_INFORMATION, entry.getKey()),
              now);
        }
      }

      announcedMetaData.clear();
      announcedMetaData.putAll(current);
    }

    for (Map.Entry<UShort, DataSetMetaDataType> entry : changed) {
      announce(entry.getKey(), entry.getValue(), StatusCode.GOOD);
    }
  }

  /**
   * Encode and send one DataSetMetaData announcement to both the discovery address and the
   * connection's data address (same bytes, same sequence number).
   */
  private void announce(
      UShort dataSetWriterId, DataSetMetaDataType metaData, StatusCode statusCode) {

    PublisherId publisherId = config.publisherId();
    if (publisherId == null) {
      return;
    }

    PublisherChannel discoveryChannel = sendChannel;
    PublisherChannel dataChannel = connection.publisherChannel();
    if (discoveryChannel == null && dataChannel == null) {
      return;
    }

    UShort sequenceNumber;
    synchronized (lock) {
      if (disposed) {
        return;
      }
      // Table 168: the counter is scoped per PublisherId, which multiple connections may share;
      // consumed even if the encoded message is later skipped for size
      sequenceNumber = service.nextAnnouncementSequenceNumber(publisherId);
    }

    var announcement =
        UadpMetaDataAnnouncement.of(
            publisherId, sequenceNumber, dataSetWriterId, metaData, statusCode);

    EncodedNetworkMessage encoded;
    try {
      encoded =
          service.getUadpMapping().encodeAnnouncement(service.getEncodingContext(), announcement);
    } catch (UaException e) {
      service
          .getDiagnostics()
          .error(
              connection.path(),
              e.getStatusCode(),
              "failed to encode DataSetMetaData announcement (dataSetWriterId=%s): %s"
                  .formatted(dataSetWriterId, e.getMessage()),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      return;
    }

    if (encoded.data().readableBytes() > DISCOVERY_MAX_MESSAGE_SIZE) {
      int size = encoded.data().readableBytes();
      encoded.data().release();
      LOGGER.warn(
          "DataSetMetaData announcement for '{}' (dataSetWriterId={}) exceeds"
              + " DiscoveryMaxMessageSize: {} > {}",
          connection.path(),
          dataSetWriterId,
          size,
          DISCOVERY_MAX_MESSAGE_SIZE);
      service
          .getDiagnostics()
          .error(
              connection.path(),
              new StatusCode(StatusCodes.Bad_EncodingLimitsExceeded),
              "DataSetMetaData announcement (dataSetWriterId=%s) exceeds DiscoveryMaxMessageSize"
                      .formatted(dataSetWriterId)
                  + " %d > %d".formatted(size, DISCOVERY_MAX_MESSAGE_SIZE),
              null,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      return;
    }

    ByteBuf data = encoded.data();

    if (discoveryChannel != null && dataChannel != null) {
      send(dataChannel, data.retainedDuplicate(), "data");
      send(discoveryChannel, data, "discovery");
    } else if (discoveryChannel != null) {
      send(discoveryChannel, data, "discovery");
    } else {
      send(dataChannel, data, "data");
    }

    service.getDiagnostics().networkMessageSent(connection.path());
  }

  private void send(PublisherChannel channel, ByteBuf data, String leg) {
    channel
        .send(data)
        .whenComplete(
            (v, ex) -> {
              if (ex != null && !suppressSendFailure()) {
                service
                    .getDiagnostics()
                    .error(
                        connection.path(),
                        DiagnosticsCollector.statusCodeOf(ex),
                        "failed to send discovery NetworkMessage (%s leg): %s"
                            .formatted(leg, ex.getMessage()),
                        ex,
                        PubSubDiagnosticsEvent.Kind.SEND_FAILURE);
              }
            });
  }

  /**
   * Whether a discovery send failure is teardown noise: the runtime is disposed or the discovery
   * send channel was nulled by {@link #closeChannels()} — channels are nulled before their close
   * completes, so a failure surfacing after that is the teardown itself.
   */
  private boolean suppressSendFailure() {
    synchronized (lock) {
      return disposed || sendChannel == null;
    }
  }

  /**
   * Derive the announcement metadata for one live writer from the live config, or null when no
   * writer with that id exists on this connection or its dataset is not in the config.
   */
  private @Nullable DataSetMetaDataType deriveMetaDataForWriter(UShort dataSetWriterId) {
    PubSubConfig serviceConfig = service.getConfig();

    for (WriterGroupRuntime group : connection.writerGroupRuntimes()) {
      for (DataSetWriterRuntime writer : group.writerRuntimes()) {
        if (dataSetWriterId.equals(writer.config().getDataSetWriterId())) {
          return serviceConfig
              .publishedDataSet(writer.config().getDataSet().name())
              .map(dataSet -> DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true))
              .orElse(null);
        }
      }
    }

    return null;
  }

  /** Derive the announcement metadata of every live writer's dataset from the live config. */
  private Map<UShort, DataSetMetaDataType> deriveLiveMetaData() {
    PubSubConfig serviceConfig = service.getConfig();
    var result = new LinkedHashMap<UShort, DataSetMetaDataType>();

    for (WriterGroupRuntime group : connection.writerGroupRuntimes()) {
      for (DataSetWriterRuntime writer : group.writerRuntimes()) {
        UShort dataSetWriterId = writer.config().getDataSetWriterId();
        serviceConfig
            .publishedDataSet(writer.config().getDataSet().name())
            .ifPresent(
                dataSet ->
                    result.put(
                        dataSetWriterId,
                        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true)));
      }
    }

    return result;
  }

  /** The metadata structure carried by a denial; Table 170 has no slot for omitting it. */
  private static DataSetMetaDataType denialMetaData() {
    return new DataSetMetaDataType(
        null,
        null,
        null,
        null,
        null,
        LocalizedText.NULL_VALUE,
        null,
        new UUID(0L, 0L),
        new ConfigurationVersionDataType(uint(0), uint(0)));
  }

  // endregion

  // region probe emission (REQUEST_IF_MISSING)

  /**
   * Start probing for a {@code REQUEST_IF_MISSING} reader that activated without effective
   * metadata. Wildcard PublisherId/DataSetWriterId filters defer probing until {@link
   * #onDataSetMessageMatched} learns the identifiers from the wire. Called under the engine lock.
   */
  void onReaderActivated(DataSetReaderRuntime reader) {
    if (reader.config().getMetadataPolicy() != MetadataPolicy.REQUEST_IF_MISSING) {
      return;
    }
    if (hasEffectiveMetaData(reader)) {
      return;
    }

    // a reader added to an already-active group is the one path where the discovery channels
    // may not be open yet; safe here because the engine lock is held
    ensureChannels();

    synchronized (lock) {
      if (disposed || probeTasks.containsKey(reader.handle())) {
        return;
      }

      var task = new ProbeTask(reader);

      PublisherId publisherIdFilter = reader.config().getPublisherId();
      UShort dataSetWriterIdFilter = reader.config().getDataSetWriterId();

      task.targetPublisherId = publisherIdFilter;
      task.targetDataSetWriterId =
          dataSetWriterIdFilter != null && dataSetWriterIdFilter.intValue() != 0
              ? dataSetWriterIdFilter
              : null;

      probeTasks.put(reader.handle(), task);

      if (task.targetPublisherId != null && task.targetDataSetWriterId != null) {
        scheduleFirstProbeLocked(task);
      }

      updateDeferredFlagLocked();
    }
  }

  /** Stop probing for a reader leaving the active states. Called under the engine lock. */
  void onReaderDeactivated(DataSetReaderRuntime reader) {
    synchronized (lock) {
      ProbeTask task = probeTasks.remove(reader.handle());
      if (task != null) {
        cancelTaskLocked(task);
        updateDeferredFlagLocked();
      }
    }
  }

  /** Whether any probe task is still waiting for wire-learned identifiers. */
  boolean hasDeferredProbes() {
    return deferredProbes;
  }

  /**
   * Learn probe identifiers from a data DataSetMessage that matched a reader with wildcard filters
   * (the first matching data NetworkMessage reveals the ids). Transport executor.
   */
  void onDataSetMessageMatched(
      DataSetReaderRuntime reader,
      @Nullable PublisherId publisherId,
      @Nullable UShort dataSetWriterId) {

    synchronized (lock) {
      ProbeTask task = probeTasks.get(reader.handle());
      if (task == null || task.cancelled || task.started) {
        return;
      }

      if (task.targetPublisherId == null && publisherId != null) {
        task.targetPublisherId = publisherId;
      }
      if (task.targetDataSetWriterId == null
          && dataSetWriterId != null
          && dataSetWriterId.intValue() != 0) {
        task.targetDataSetWriterId = dataSetWriterId;
      }

      if (task.targetPublisherId != null && task.targetDataSetWriterId != null) {
        scheduleFirstProbeLocked(task);
      }

      updateDeferredFlagLocked();
    }
  }

  /** Stop probing for a reader whose discovered metadata was applied. Transport executor. */
  void onMetaDataApplied(DataSetReaderRuntime reader) {
    synchronized (lock) {
      ProbeTask task = probeTasks.remove(reader.handle());
      if (task != null) {
        cancelTaskLocked(task);
        updateDeferredFlagLocked();
      }
    }
  }

  /**
   * Handle a Bad-status DataSetMetaData announcement (a denial): probe tasks whose resolved target
   * matches the denial stop and record a diagnostics error (§7.2.4.6.12.2: retry "until all needed
   * announcements are received or denied"). Transport executor.
   */
  void onMetaDataDenied(UadpMetaDataAnnouncement announcement) {
    var denied = new ArrayList<ProbeTask>();

    synchronized (lock) {
      var iterator = probeTasks.values().iterator();
      while (iterator.hasNext()) {
        ProbeTask task = iterator.next();
        if (task.started
            && announcement.publisherId().equals(task.targetPublisherId)
            && announcement.dataSetWriterId().equals(task.targetDataSetWriterId)) {
          cancelTaskLocked(task);
          iterator.remove();
          denied.add(task);
        }
      }
      updateDeferredFlagLocked();
    }

    for (ProbeTask task : denied) {
      service
          .getDiagnostics()
          .error(
              task.reader.path(),
              announcement.statusCode(),
              "publisher denied DataSetMetaData for dataSetWriterId=%s: %s"
                  .formatted(announcement.dataSetWriterId(), announcement.statusCode()),
              null,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
    }
  }

  /** Guarded by {@link #lock}. */
  private void scheduleFirstProbeLocked(ProbeTask task) {
    task.started = true;

    long delayMillis =
        ThreadLocalRandom.current()
            .nextLong(FIRST_PROBE_MIN_DELAY_MILLIS, FIRST_PROBE_MAX_DELAY_MILLIS + 1);

    task.future =
        service
            .getScheduledExecutor()
            .schedule(() -> runProbe(task), delayMillis, TimeUnit.MILLISECONDS);
  }

  /** One probe attempt; runs on the scheduled executor. */
  private void runProbe(ProbeTask task) {
    PublisherId targetPublisherId;
    UShort targetDataSetWriterId;

    synchronized (lock) {
      task.future = null;

      if (disposed || task.cancelled || task.reader.isDisposed()) {
        return;
      }

      // §7.2.4.6.12.2: skip the probe if the information arrived in the meantime (another
      // subscriber's probe may have been answered already)
      if (hasEffectiveMetaData(task.reader)) {
        probeTasks.remove(task.reader.handle());
        updateDeferredFlagLocked();
        return;
      }

      targetPublisherId = task.targetPublisherId;
      targetDataSetWriterId = task.targetDataSetWriterId;
      if (targetPublisherId == null || targetDataSetWriterId == null) {
        return;
      }
    }

    UadpDiscoveryProbe probe =
        UadpDiscoveryProbe.of(targetPublisherId, List.of(targetDataSetWriterId));

    EncodedNetworkMessage encoded;
    try {
      encoded = service.getUadpMapping().encodeProbe(service.getEncodingContext(), probe);
    } catch (UaException e) {
      // deterministic failure: retrying would fail the same way
      service
          .getDiagnostics()
          .error(
              task.reader.path(),
              e.getStatusCode(),
              "failed to encode discovery probe: " + e.getMessage(),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      synchronized (lock) {
        cancelTaskLocked(task);
        probeTasks.remove(task.reader.handle());
        updateDeferredFlagLocked();
      }
      return;
    }

    // probes go to the discovery address only
    PublisherChannel channel = sendChannel;
    if (channel != null) {
      String readerPath = task.reader.path();
      channel
          .send(encoded.data())
          .whenComplete(
              (v, ex) -> {
                if (ex != null && !suppressSendFailure()) {
                  service
                      .getDiagnostics()
                      .error(
                          readerPath,
                          DiagnosticsCollector.statusCodeOf(ex),
                          "failed to send discovery probe: " + ex.getMessage(),
                          ex,
                          PubSubDiagnosticsEvent.Kind.SEND_FAILURE);
                }
              });
      service.getDiagnostics().networkMessageSent(connection.path());
    } else {
      encoded.data().release();
      LOGGER.debug(
          "discovery send channel of '{}' is not open; probe for '{}' skipped",
          connection.path(),
          task.reader.path());
    }

    synchronized (lock) {
      if (disposed || task.cancelled) {
        return;
      }

      long delayMillis = task.nextDelayMillis;
      task.nextDelayMillis = Math.min(delayMillis * 2, RETRY_MAX_DELAY_MILLIS);
      task.future =
          service
              .getScheduledExecutor()
              .schedule(() -> runProbe(task), delayMillis, TimeUnit.MILLISECONDS);
    }
  }

  private boolean hasEffectiveMetaData(DataSetReaderRuntime reader) {
    return reader.configuredMetaData() != null
        || service.getMetadataCache().getDiscovered(reader.handle()) != null;
  }

  /** Guarded by {@link #lock}. */
  private void cancelTaskLocked(ProbeTask task) {
    task.cancelled = true;

    ScheduledFuture<?> future = task.future;
    task.future = null;
    if (future != null) {
      future.cancel(false);
    }
  }

  /** Guarded by {@link #lock}. */
  private void updateDeferredFlagLocked() {
    boolean deferred = false;
    for (ProbeTask task : probeTasks.values()) {
      if (!task.started && !task.cancelled) {
        deferred = true;
        break;
      }
    }
    deferredProbes = deferred;
  }

  // endregion

  /** Suppression key: probe type plus the probed identifier (§7.2.4.6.12.2). */
  private record ResponseKey(int probeType, UShort dataSetWriterId) {}

  /**
   * Probe state for one REQUEST_IF_MISSING reader; all mutable fields guarded by the outer lock.
   */
  private static final class ProbeTask {

    final DataSetReaderRuntime reader;

    @Nullable PublisherId targetPublisherId;
    @Nullable UShort targetDataSetWriterId;

    /** Whether the first probe has been scheduled (identifiers resolved). */
    boolean started = false;

    boolean cancelled = false;

    long nextDelayMillis = RETRY_BASE_DELAY_MILLIS;

    @Nullable ScheduledFuture<?> future;

    private ProbeTask(DataSetReaderRuntime reader) {
      this.reader = reader;
    }
  }
}
