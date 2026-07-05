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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetFieldValue;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PublisherStatusReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyMaterial;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedField;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedMetaData;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedStatusMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageMappingProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.ReceivedSecurity;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.SecurityContextResolver;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.SecurityOutcome;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpDecodedMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpDiscoveryProbe;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMetaDataAnnouncement;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Subscriber-side dispatch: decodes received datagrams and routes decoded DataSetMessages,
 * DataSetMetaData, discovery messages, and remote publisher status to their subscriber-side
 * consumers. DataSetMessages are matched to DataSetReaders via the Part 14 filter chain (§6.2.9.x /
 * §6.3.1.4.x), where null/0 filter values are wildcards; status messages are correlated to local
 * readers by mapping and PublisherId but do not mutate reader state.
 *
 * <p>Runs entirely on the transport executor; decode and match failures are counted in diagnostics
 * and never thrown. The connection's {@code networkMessagesReceived} counter ticks once per
 * arrival, before any decode attempt; decode failures tick {@code decodeErrors} at the connection
 * path unless another of the connection's mappings decoded content — DataSetMessages, metadata,
 * status, or a discovery message — from the same buffer (on mixed-mapping broker connections every
 * message is offered to every mapping, so the "wrong" mapping's failure on a message the right
 * mapping decoded is expected and not an error). A mapping's own partial content never suppresses
 * its own failure: a truncated message that still delivered a decodable prefix ticks that mapping's
 * failure once while suppressing the other mappings' failures on the same bytes. A tolerated skip
 * that decoded nothing, such as the UADP decoder skipping foreign input by its version nibble,
 * suppresses nothing: a buffer no mapping could decode ticks {@code decodeErrors} once per failed
 * mapping. A reader group with a non-zero {@code maxNetworkMessageSize} never sees messages larger
 * than it; such messages tick {@code decodeErrors} at the group path with {@code
 * Bad_EncodingLimitsExceeded}.
 *
 * <p>Data and event DataSetMessages that carry sequence numbers pass a per-reader, per-stream Part
 * 14 §7.2.3 recency window before delivery — the NetworkMessage window first (a stale or invalid
 * NetworkMessage suppresses all its matched DataSetMessages for that reader), then the
 * DataSetMessage window. The DataSetMessage window advances only on delivered messages; the
 * NetworkMessage window is classified — and, on a NEW verdict, advanced — exactly once per (reader,
 * NetworkMessage) for every NetworkMessage that passes {@code matchesNetworkMessage} and carries a
 * sequence number, regardless of whether any contained DataSetMessage matches the reader's
 * dataSetWriterId filter: the publisher consumes one NetworkMessage sequence number for every
 * NetworkMessage it sends (§7.2.3 "incremented by exactly one for each message"), so
 * NetworkMessages carrying only keep-alives, only DataSetMessages dropped at the DataSetMessage
 * window, or only other writers' DataSetMessages all advance the window too. Stale and invalid
 * messages are dropped: not delivered, counted in the reader's {@code staleSequenceMessages} /
 * {@code invalidSequenceMessages} (not in {@code dataSetMessagesReceived}) once per matched
 * DataSetMessage they suppress — a NetworkMessage with no matched DataSetMessages ticks nothing —
 * and, per §6.2.9.6, where a data DataSetMessage is "new" only if its sequence number increments,
 * they do not reset the receive timeout, complete startup, or recover the reader from Error.
 * Messages without a sequence number bypass the window entirely ("each received DataSetMessage is
 * considered new", §6.2.9.6). Keep-alives never advance a seeded DataSetMessage window on a
 * consistent carried value — they may seed an unseeded stream with their carried next-expected
 * number, and they reseed a seeded window whose verdict on the carried value is stale or invalid,
 * per §7.2.4.5.8 (the carried value is the publisher's authoritative next expected sequence number,
 * so a restarted publisher's keep-alives recover its streams; see {@link ReaderSequenceTracker}) —
 * they refresh the stream's §7.2.3 record-discard clock (a keep-alive is a received message), and
 * they always reset the receive timeout — while the NetworkMessage that carries them is an ordinary
 * NetworkMessage whose sequence number advances the NetworkMessage window normally.
 *
 * <p>For secured NetworkMessages the §7.2.3 windows run downstream of the codec's
 * verify-before-parse order: the decoder verifies the signature (and decrypts) before it surfaces
 * any content, so every sequence number observed here is post-verification — the recency window is
 * a genuine anti-replay control within a key's lifetime (no additional nonce monotonicity check is
 * applied). Chunk NetworkMessages observe the windows like any other NetworkMessage (each chunk
 * consumes a NetworkMessage sequence number); their reassembled payload re-enters decode with no
 * NetworkMessage sequence number and only its DataSetMessage sequence number drives the
 * DataSetMessage window.
 *
 * <p>The dispatcher supplies the codec's {@code SecurityContextResolver} (keys are resolved by
 * plaintext wire identity, BEFORE group matching, because decode runs once per (connection,
 * mapping)), enforces the §7.2.4.3 receive-mode gate per (group, NetworkMessage), and maps security
 * outcomes to the security drop counters — quiet counters, never {@code decodeErrors}, never
 * per-message events (see {@code PubSubDiagnostics.ComponentDiagnostics}).
 */
final class ReaderDispatcher {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReaderDispatcher.class);

  /** dataSetFieldId used when no metadata names a decoded field. */
  private static final UUID NULL_FIELD_ID = new UUID(0L, 0L);

  /** Floor between invalid-signature WARN logs: forged traffic must not flood the log. */
  private static final long INVALID_SIGNATURE_WARN_INTERVAL_NANOS = TimeUnit.SECONDS.toNanos(10);

  private static final long STATUS_TIMEOUT_GRACE_MILLIS = 250L;

  private final AtomicLong lastInvalidSignatureWarnNanos =
      new AtomicLong(System.nanoTime() - INVALID_SIGNATURE_WARN_INTERVAL_NANOS);

  private final PubSubServiceImpl service;
  private final ConcurrentMap<StatusTimeoutKey, StatusTimeout> statusTimeouts =
      new ConcurrentHashMap<>();
  private final AtomicLong statusTimeoutSerial = new AtomicLong();

  ReaderDispatcher(PubSubServiceImpl service) {
    this.service = service;
  }

  /**
   * Decode and dispatch one received datagram. Must be called on the transport executor; the caller
   * retains ownership of {@code buffer}.
   *
   * <p>For the built-in UADP mapping the discovery-aware decode is used, so probes arriving on the
   * data socket are routed to the connection's discovery responder and announcements — including
   * Bad-status denials — reach the metadata path regardless of which socket they arrived on.
   */
  void dispatch(ConnectionRuntime connection, ByteBuf buffer) {
    dispatch(connection, null, buffer);
  }

  void dispatch(ConnectionRuntime connection, @Nullable String topic, ByteBuf buffer) {
    // "received" means "a message arrived and was offered to decode": tick once per arrival,
    // before any mapping runs, so the counter is independent of the decode outcome
    service.getDiagnostics().networkMessageReceived(connection.path());

    // Milo extension: a reader group with a non-zero maxNetworkMessageSize does not accept
    // messages larger than it (Part 14 gives the parameter no receive-side semantics)
    Set<ReaderGroupRuntime> oversizeGroups = oversizeGroups(connection, buffer.readableBytes());

    Map<String, MessageMappingProvider> mappings = connection.subscriberMappings();

    // one resolver per dispatch: the decoder consults it for secured messages (verify/decrypt
    // keys resolved by wire identity, BEFORE group matching); the group it routed to is taken
    // back after each decode so security drops are attributed to the same group
    var securityResolver = new SecurityResolver(service, connection);

    // decode failures are deferred: on mixed-mapping connections every message is offered to
    // every mapping, so a mapping's failure only counts when no OTHER mapping decoded content
    // from the buffer; a mapping's own partial content never suppresses its own failure
    Set<String> mappingsWithContent = null;
    List<DecodeFailure> failures = null;

    for (Map.Entry<String, MessageMappingProvider> entry : mappings.entrySet()) {
      String mappingName = entry.getKey();
      MessageMappingProvider provider = entry.getValue();

      UadpDecodedMessage decoded;
      try {
        DecodeContext context = DecodeContext.of(service.getEncodingContext(), securityResolver);
        if (provider instanceof UadpMessageMapping uadpMapping) {
          decoded = uadpMapping.decodeMessage(context, buffer.slice());
        } else {
          decoded = provider.decode(context, buffer.slice());
        }
      } catch (Exception e) {
        securityResolver.takeResolvedGroup();
        if (failures == null) {
          failures = new ArrayList<>(1);
        }
        failures.add(
            new DecodeFailure(
                mappingName,
                UaException.extractStatusCode(e)
                    .orElse(new StatusCode(StatusCodes.Bad_DecodingError)),
                "failed to decode NetworkMessage: " + e.getMessage(),
                e));
        continue;
      }

      // taken (and cleared) per decode so a stale routing can never bleed into the next mapping
      ReaderGroupRuntime securityGroup = securityResolver.takeResolvedGroup();

      DecodedNetworkMessage.Failure failure =
          decoded instanceof DecodedNetworkMessage networkMessage ? networkMessage.failure() : null;
      if (failure != null) {
        // the tolerant UADP decode surfaced a failure: count it, but still deliver whatever was
        // decoded before the failure point (partial-delivery posture)
        if (failures == null) {
          failures = new ArrayList<>(1);
        }
        failures.add(
            new DecodeFailure(
                mappingName,
                failure.statusCode(),
                "failed to decode NetworkMessage: " + failure.message(),
                failure.cause()));
      }

      // content is tracked independently of failure: a truncated message that still delivered a
      // decodable prefix makes the OTHER mappings' failures on the same bytes expected, while its
      // own failure above stays observable
      if (decodedContent(decoded)) {
        if (mappingsWithContent == null) {
          mappingsWithContent = new HashSet<>(2);
        }
        mappingsWithContent.add(mappingName);
      }

      if (decoded instanceof DecodedNetworkMessage networkMessage) {
        handleDecoded(
            connection, mappingName, topic, networkMessage, oversizeGroups, securityGroup);

        // Chunk NetworkMessages: the normal handleDecoded pass above lets the per-reader
        // NetworkMessage windows observe the chunk NM (its empty messages list delivers
        // nothing); reassembly is connection-level and runs ONCE, not per reader. Only the
        // built-in UADP mapping produces and reassembles chunks. When a chunk completes its
        // payload, the reassembled DataSetMessage re-enters decode with sequenceNumber == null
        // (the chunk NMs already consumed theirs) and routes through handleDecoded again.
        if (networkMessage.chunk() != null && provider instanceof UadpMessageMapping uadpMapping) {
          ChunkReassembler.ReassembledMessage reassembled =
              connection.chunkReassembler().accept(networkMessage, System.nanoTime());

          if (reassembled != null) {
            ByteBuf payload = Unpooled.wrappedBuffer(reassembled.payload());
            try {
              DecodedNetworkMessage decodedReassembled =
                  uadpMapping.decodeReassembled(
                      DecodeContext.of(service.getEncodingContext()),
                      reassembled.header(),
                      reassembled.dataSetWriterId(),
                      payload);

              DecodedNetworkMessage.Failure reassembledFailure = decodedReassembled.failure();
              if (reassembledFailure != null) {
                if (failures == null) {
                  failures = new ArrayList<>(1);
                }
                failures.add(
                    new DecodeFailure(
                        mappingName,
                        reassembledFailure.statusCode(),
                        "failed to decode NetworkMessage: " + reassembledFailure.message(),
                        reassembledFailure.cause()));
              }

              // the reassembled message inherits the (VERIFIED) chunk security; no re-resolution
              handleDecoded(
                  connection, mappingName, null, decodedReassembled, oversizeGroups, null);
            } finally {
              payload.release();
            }
          }
        }
      } else if (decoded instanceof UadpDiscoveryProbe probe) {
        DiscoveryRuntime discovery = connection.discoveryRuntime();
        if (discovery != null) {
          discovery.onProbeReceived(probe);
        }
      } else if (decoded instanceof UadpMetaDataAnnouncement announcement) {
        handleAnnouncement(connection, mappingName, announcement, oversizeGroups);
      }
    }

    if (failures != null) {
      for (DecodeFailure failure : failures) {
        if (otherMappingDecodedContent(mappingsWithContent, failure.mappingName())) {
          continue;
        }
        service
            .getDiagnostics()
            .decodeError(
                connection.path(), failure.statusCode(), failure.message(), failure.error());
      }
    }
  }

  /** A decode failure deferred until every mapping has had its decode attempt. */
  private record DecodeFailure(
      String mappingName, StatusCode statusCode, String message, @Nullable Throwable error) {}

  /**
   * Whether any mapping other than {@code mappingName} decoded content from the buffer: the
   * mixed-mapping suppression condition for a failure recorded by {@code mappingName}. A mapping's
   * own partial content keeps its own failure observable.
   */
  private static boolean otherMappingDecodedContent(
      @Nullable Set<String> mappingsWithContent, String mappingName) {

    if (mappingsWithContent == null) {
      return false;
    }
    for (String name : mappingsWithContent) {
      if (!name.equals(mappingName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Whether a decode actually decoded something from the buffer: DataSetMessages, metadata, status,
   * or a discovery probe/announcement — including content decoded before a surfaced failure point.
   *
   * <p>The distinction carries the mixed-mapping suppression guard: only a mapping that decoded
   * content from a buffer makes another mapping's failure on the same buffer expected. The tolerant
   * UADP decode returns an empty, failure-free result for foreign input it merely skips (e.g. a
   * JSON document's '{' fails the version-nibble check), and an empty skip that decoded nothing
   * must not suppress another mapping's genuine failure — otherwise a malformed JSON payload on a
   * mixed uadp+json connection would never tick {@code decodeErrors}.
   */
  private static boolean decodedContent(UadpDecodedMessage decoded) {
    if (decoded instanceof DecodedNetworkMessage networkMessage) {
      // a chunk is decoded content: without it a chunk NetworkMessage would let a sibling
      // mapping's genuine failure on the same bytes tick decodeErrors
      return !networkMessage.messages().isEmpty()
          || !networkMessage.metaData().isEmpty()
          || networkMessage.status() != null
          || networkMessage.chunk() != null;
    }
    // a discovery probe or metadata announcement is decoded content by definition
    return true;
  }

  /**
   * The receiving reader groups whose non-zero maxNetworkMessageSize excludes a message of {@code
   * messageSize} bytes, each ticked with a group-path decodeError.
   */
  private Set<ReaderGroupRuntime> oversizeGroups(ConnectionRuntime connection, int messageSize) {
    Set<ReaderGroupRuntime> oversizeGroups = Set.of();

    for (ReaderGroupRuntime group : connection.readerGroupRuntimes()) {
      if (isNotReceiving(group.state())) {
        continue;
      }
      long maxNetworkMessageSize = group.config().getMaxNetworkMessageSize().longValue();
      if (maxNetworkMessageSize > 0 && messageSize > maxNetworkMessageSize) {
        service
            .getDiagnostics()
            .decodeError(
                group.path(),
                new StatusCode(StatusCodes.Bad_EncodingLimitsExceeded),
                "NetworkMessage not accepted: size %d exceeds maxNetworkMessageSize %d"
                    .formatted(messageSize, maxNetworkMessageSize),
                null);

        if (oversizeGroups.isEmpty()) {
          oversizeGroups = new HashSet<>(2);
        }
        oversizeGroups.add(group);
      }
    }

    return oversizeGroups;
  }

  private void handleDecoded(
      ConnectionRuntime connection,
      String mappingName,
      @Nullable String topic,
      DecodedNetworkMessage decoded,
      Set<ReaderGroupRuntime> oversizeGroups,
      @Nullable ReaderGroupRuntime securityResolvedGroup) {

    ReceivedSecurity security = decoded.security();
    if (security != null && security.outcome() != SecurityOutcome.VERIFIED) {
      // a header-only security skip: nothing was decoded, so nothing can be delivered — count
      // the drop against the group whose keys were (or would have been) used and stop here.
      // These are security drops, never decodeErrors.
      if (securityResolvedGroup != null) {
        countSecuritySkip(security, securityResolvedGroup);
      } else {
        // the resolver refused to route (no secured group covers this traffic), so the message
        // can never reach the receive-mode gate below — count its receive-mode drops here instead
        countUnroutedSecuritySkip(connection, mappingName, decoded, oversizeGroups, security);
      }
      return;
    }

    DecodedStatusMessage status = decoded.status();
    if (status != null) {
      handleStatus(connection, mappingName, topic, status);
      return;
    }

    // the received mode input to the §7.2.4.3 receive-mode gate: null security IS received None
    MessageSecurityMode receivedMode =
        security != null ? security.mode() : MessageSecurityMode.None;

    for (ReaderGroupRuntime group : connection.readerGroupRuntimes()) {
      if (oversizeGroups.contains(group)) {
        continue;
      }

      // §7.2.4.3 receive-mode gate, per (group, NetworkMessage): a received mode below the
      // group's configured mode is dropped (SHALL), a secured message to a mode-None group is
      // dropped (its keys never resolve anyway), and a received mode above the configured mode
      // is processed (MAY — the keys come from the same window). Dropped messages tick
      // staleKeyMessages once per group, but only when the message would otherwise have matched
      // one of the group's readers — unrelated traffic is not counted. Discovery/metadata
      // announcements travel as separate mode-None messages through handleAnnouncement,
      // which this gate deliberately does not cover.
      if (!receiveModeAccepts(group.config().getMessageSecurity(), receivedMode)) {
        if (anyReaderMatches(group, mappingName, decoded)) {
          service.getDiagnostics().staleKeyMessage(group.path());
        }
        continue;
      }

      boolean groupMatched = false;

      for (DataSetReaderRuntime reader : group.readerRuntimes()) {
        if (!reader.mappingName().equals(mappingName)) {
          continue;
        }
        if (isNotReceiving(reader.state())) {
          continue;
        }

        // metadata announcements match on (PublisherId, DataSetWriterId) only
        for (DecodedMetaData metaData : decoded.metaData()) {
          if (publisherIdMatches(reader.config(), decoded.publisherId())
              && dataSetWriterIdMatches(reader.config(), metaData.dataSetWriterId())) {
            handleMetaData(connection, reader, metaData);
          }
        }

        if (matchesNetworkMessage(reader.config(), decoded)) {
          long nowNanos = System.nanoTime();

          // one NetworkMessage-window observation per (reader, NetworkMessage), BEFORE the
          // dataSetWriterId filter loop: the (PublisherId, WriterGroupId) stream consumed this
          // sequence number whether or not any contained DataSetMessage matches this reader,
          // and the single observation also keeps a NEW verdict from making sibling
          // DataSetMessages of the same NetworkMessage classify as duplicates
          SequenceNumberWindow.Classification networkMessageClassification =
              observeNetworkMessage(reader.sequenceTracker(), decoded, nowNanos);

          for (DecodedDataSetMessage message : decoded.messages()) {
            if (dataSetWriterIdMatches(reader.config(), message.dataSetWriterId())) {
              groupMatched = true;
              deliver(
                  connection,
                  group,
                  reader,
                  decoded,
                  message,
                  networkMessageClassification,
                  nowNanos);
            }
          }
        }
      }

      if (groupMatched) {
        service.getDiagnostics().networkMessageReceived(group.path());
      }
    }
  }

  private void handleStatus(
      ConnectionRuntime connection,
      String mappingName,
      @Nullable String topic,
      DecodedStatusMessage status) {

    List<PubSubHandle> readers = correlatedStatusReaders(connection, mappingName, status);

    service
        .getEventDispatcher()
        .notifyPublisherStatus(
            new PublisherStatusReceivedEvent(
                connection.handle(),
                mappingName,
                topic,
                status.messageId(),
                status.publisherId(),
                status.status(),
                status.cyclic(),
                status.timestamp(),
                status.nextReportTime(),
                false,
                readers));

    StatusTimeoutKey key =
        new StatusTimeoutKey(connection.handle(), mappingName, status.publisherId());
    if (status.cyclic() && status.nextReportTime() != null) {
      scheduleStatusTimeout(connection, mappingName, topic, status, readers, key);
    } else {
      cancelStatusTimeout(key);
    }
  }

  private List<PubSubHandle> correlatedStatusReaders(
      ConnectionRuntime connection, String mappingName, DecodedStatusMessage status) {

    var readers = new ArrayList<PubSubHandle>();
    for (ReaderGroupRuntime group : connection.readerGroupRuntimes()) {
      for (DataSetReaderRuntime reader : group.readerRuntimes()) {
        if (!reader.mappingName().equals(mappingName)) {
          continue;
        }
        if (isNotReceiving(reader.state())) {
          continue;
        }
        if (publisherIdMatches(reader.config(), status.publisherId())) {
          readers.add(reader.handle());
        }
      }
    }
    return List.copyOf(readers);
  }

  private void scheduleStatusTimeout(
      ConnectionRuntime connection,
      String mappingName,
      @Nullable String topic,
      DecodedStatusMessage status,
      List<PubSubHandle> readers,
      StatusTimeoutKey key) {

    cancelStatusTimeout(key);

    long serial = statusTimeoutSerial.incrementAndGet();
    long delayMillis =
        Math.max(
            0L,
            status.nextReportTime().getJavaTime()
                - System.currentTimeMillis()
                + STATUS_TIMEOUT_GRACE_MILLIS);

    var timeout = new StatusTimeout(serial, new AtomicReference<>());
    statusTimeouts.put(key, timeout);

    ScheduledFuture<?> future =
        service
            .getScheduledExecutor()
            .schedule(
                () ->
                    submitStatusTimeout(
                        connection, mappingName, topic, status, readers, key, serial),
                delayMillis,
                TimeUnit.MILLISECONDS);

    timeout.future().set(future);
    if (statusTimeouts.get(key) != timeout) {
      future.cancel(false);
    }
  }

  private void cancelStatusTimeout(StatusTimeoutKey key) {
    StatusTimeout timeout = statusTimeouts.remove(key);
    if (timeout != null) {
      ScheduledFuture<?> future = timeout.future().get();
      if (future != null) {
        future.cancel(false);
      }
    }
  }

  private void submitStatusTimeout(
      ConnectionRuntime connection,
      String mappingName,
      @Nullable String topic,
      DecodedStatusMessage status,
      List<PubSubHandle> readers,
      StatusTimeoutKey key,
      long serial) {

    try {
      connection.submitToDispatchQueue(
          () -> notifyStatusTimeout(connection, mappingName, topic, status, readers, key, serial));
    } catch (RejectedExecutionException e) {
      // service is shutting down; no listener can reliably observe the timeout
    }
  }

  private void notifyStatusTimeout(
      ConnectionRuntime connection,
      String mappingName,
      @Nullable String topic,
      DecodedStatusMessage status,
      List<PubSubHandle> readers,
      StatusTimeoutKey key,
      long serial) {

    StatusTimeout currentTimeout = statusTimeouts.get(key);
    if (currentTimeout == null || currentTimeout.serial() != serial) {
      return;
    }
    statusTimeouts.remove(key, currentTimeout);

    service
        .getEventDispatcher()
        .notifyPublisherStatus(
            new PublisherStatusReceivedEvent(
                connection.handle(),
                mappingName,
                topic,
                status.messageId(),
                status.publisherId(),
                PubSubState.Error,
                true,
                status.timestamp(),
                status.nextReportTime(),
                true,
                readers));
  }

  void cancelStatusTimeouts(PubSubHandle connection) {
    for (Map.Entry<StatusTimeoutKey, StatusTimeout> entry : statusTimeouts.entrySet()) {
      StatusTimeoutKey key = entry.getKey();
      StatusTimeout timeout = entry.getValue();
      if (key.connection().equals(connection) && statusTimeouts.remove(key, timeout)) {
        ScheduledFuture<?> future = timeout.future().get();
        if (future != null) {
          future.cancel(false);
        }
      }
    }
  }

  private record StatusTimeoutKey(
      PubSubHandle connection, String mappingName, PublisherId publisherId) {}

  private record StatusTimeout(long serial, AtomicReference<ScheduledFuture<?>> future) {}

  /**
   * Route one decoded DataSetMetaData announcement, received on either the data socket or the
   * connection's discovery socket: non-Bad announcements are matched to readers on (PublisherId,
   * DataSetWriterId) and applied like the data-plane metadata path; Bad-status announcements are
   * denials, which terminate matching discovery probe tasks (Part 14 §7.2.4.6.12.2).
   */
  void handleAnnouncement(
      ConnectionRuntime connection, String mappingName, UadpMetaDataAnnouncement announcement) {
    handleAnnouncement(connection, mappingName, announcement, Set.of());
  }

  private void handleAnnouncement(
      ConnectionRuntime connection,
      String mappingName,
      UadpMetaDataAnnouncement announcement,
      Set<ReaderGroupRuntime> oversizeGroups) {

    if (announcement.statusCode().isBad()) {
      DiscoveryRuntime discovery = connection.discoveryRuntime();
      if (discovery != null) {
        discovery.onMetaDataDenied(announcement);
      }
      return;
    }

    var metaData = new DecodedMetaData(announcement.dataSetWriterId(), announcement.metaData());

    for (ReaderGroupRuntime group : connection.readerGroupRuntimes()) {
      if (oversizeGroups.contains(group)) {
        continue;
      }

      for (DataSetReaderRuntime reader : group.readerRuntimes()) {
        if (!reader.mappingName().equals(mappingName)) {
          continue;
        }
        if (isNotReceiving(reader.state())) {
          continue;
        }

        if (publisherIdMatches(reader.config(), announcement.publisherId())
            && dataSetWriterIdMatches(reader.config(), announcement.dataSetWriterId())) {
          handleMetaData(connection, reader, metaData);
        }
      }
    }
  }

  private void deliver(
      ConnectionRuntime connection,
      ReaderGroupRuntime group,
      DataSetReaderRuntime reader,
      DecodedNetworkMessage networkMessage,
      DecodedDataSetMessage message,
      SequenceNumberWindow.@Nullable Classification networkMessageClassification,
      long nowNanos) {

    // a matched message reveals the identifiers a wildcard-filtered REQUEST_IF_MISSING reader
    // needs before it can emit discovery probes
    DiscoveryRuntime discovery = connection.discoveryRuntime();
    if (discovery != null && discovery.hasDeferredProbes()) {
      discovery.onDataSetMessageMatched(
          reader, networkMessage.publisherId(), message.dataSetWriterId());
    }

    ReaderSequenceTracker tracker = reader.sequenceTracker();

    if (message.kind() == DataSetMessageKind.KEEP_ALIVE) {
      // the keep-alive is never dropped on the NetworkMessage verdict: keep-alives always reset
      // the receive timeout (§6.2.9.6 "The DataSetMessages that reset the period include
      // keep-alive and heartbeat messages").
      //
      // a keep-alive carries the next expected sequence number WITHOUT consuming it (Part 14
      // §7.2.4.5.8, §7.2.5.4.1): it may seed an unseeded stream window; on a seeded one it never
      // advances on a consistent carried value — advancing would make the next data message,
      // which carries the same number, classify as a duplicate — but reseeds the window when the
      // carried value classifies stale/invalid (the publisher is authoritative about its own
      // counter, §7.2.4.5.8). It refreshes the stream's §7.2.3 record-discard clock (a
      // keep-alive is a received message, so a keep-alive-only period must not discard the
      // record) and always resets the receive timeout (§6.2.9.6)
      UInteger keepAliveSequence = message.sequenceNumber();
      if (keepAliveSequence != null) {
        tracker.observeKeepAlive(
            networkMessage.publisherId(), message.dataSetWriterId(), keepAliveSequence, nowNanos);
      }
      service.getDiagnostics().dataSetMessageReceived(group.path());
      service.getDiagnostics().dataSetMessageReceived(reader.path());
      reader.onMessageAccepted();
      return;
    }

    if (!message.valid()) {
      service.getDiagnostics().dataSetMessageReceived(group.path());
      service.getDiagnostics().dataSetMessageReceived(reader.path());
      service
          .getDiagnostics()
          .decodeError(
              reader.path(),
              new StatusCode(StatusCodes.Bad_DecodingError),
              "DataSetMessage with valid=false dropped",
              null);
      return;
    }

    DataSetMetaDataConfig metaData = effectiveMetaData(reader);

    // The major version can only be checked when it is transmitted: the decoder substitutes 0
    // when DataSetFlags1 bit 5 is clear (e.g. the default UADP-Dynamic mask carries only the
    // minor version), and a VersionTime of 0 means "not used" per Part 14.
    ConfigurationVersionDataType version = message.configurationVersion();
    if (version != null
        && metaData != null
        && version.getMajorVersion().longValue() != 0L
        && !version.getMajorVersion().equals(metaData.getConfigurationVersionMajor())) {

      // do not reset the receive timeout: per §6.2.9.4 a reader that cannot obtain matching
      // metadata within messageReceiveTimeout goes to Error
      service.getDiagnostics().dataSetMessageReceived(group.path());
      service.getDiagnostics().dataSetMessageReceived(reader.path());
      service
          .getDiagnostics()
          .decodeError(
              reader.path(),
              new StatusCode(StatusCodes.Bad_ConfigurationError),
              "DataSetMetaData major version mismatch: message=%s, local=%s"
                  .formatted(version.getMajorVersion(), metaData.getConfigurationVersionMajor()),
              null);
      return;
    }

    // Part 14 §7.2.3 sequence-number windows: the NetworkMessage verdict first — a stale or
    // invalid NetworkMessage (observed once per (reader, NetworkMessage) by the caller, before
    // the dataSetWriterId filter) suppresses every matched DataSetMessage it carries for this
    // reader — then the DataSetMessage window. Messages without a sequence number bypass the
    // windows ("each received DataSetMessage is considered new", §6.2.9.6). Drops are not
    // delivered, do not reset the receive timeout, do not complete startup, and do not recover
    // from Error (§6.2.9.6: a data DataSetMessage is "new" only if the sequence number
    // increments).
    if (networkMessageClassification != null
        && networkMessageClassification != SequenceNumberWindow.Classification.NEW) {
      sequenceDrop(reader, networkMessageClassification);
      return;
    }

    UInteger dataSetMessageSequence = message.sequenceNumber();
    if (dataSetMessageSequence != null) {
      SequenceNumberWindow.Classification classification =
          tracker.classifyDataSetMessage(
              networkMessage.publisherId(),
              message.dataSetWriterId(),
              dataSetMessageSequence,
              nowNanos);
      if (classification != SequenceNumberWindow.Classification.NEW) {
        sequenceDrop(reader, classification);
        return;
      }

      // the DataSetMessage will be delivered: its window advances only on accept
      tracker.acceptDataSetMessage(
          networkMessage.publisherId(),
          message.dataSetWriterId(),
          dataSetMessageSequence,
          nowNanos);
    }

    service.getDiagnostics().dataSetMessageReceived(group.path());
    service.getDiagnostics().dataSetMessageReceived(reader.path());

    reader.onMessageAccepted();

    // Part 14 §6.2.1 Table 2 (SHALL): a DataSetReader changes to Operational only after the
    // first key frame or event DataSetMessage. Pre-baseline delta frames are DELIBERATELY still
    // delivered to listeners and still reset the receive timeout (their sequence numbers
    // increment, so they are "new" per §6.2.9.6; §7.2.4.3 leaves the delivery policy to the
    // application): listeners receive honest partial state while the reader's PreOperational
    // state signals "no full baseline seen yet", and the publisher-side keyFrameCount cadence
    // bounds the wait for a key frame.
    if (reader.state() == PubSubState.PreOperational
        && (message.kind() == DataSetMessageKind.KEY_FRAME
            || message.kind() == DataSetMessageKind.EVENT)) {
      service.getStateMachine().startupCompleted(reader);
    }

    List<DataSetMetaDataConfig.Field> metaFields = metaData != null ? metaData.fields() : List.of();

    var fields = new ArrayList<DataSetFieldValue>(message.fields().size());
    for (DecodedField field : message.fields()) {
      int index = field.index();
      String wireName = field.fieldName();

      String name;
      UUID fieldId;
      if (wireName != null) {
        // name-keyed mappings (JSON): prefer matching the wire name against the effective
        // metadata; the metadata position becomes the field index
        int metaIndex = indexOfMetaField(metaFields, wireName);
        if (metaIndex >= 0) {
          DataSetMetaDataConfig.Field metaField = metaFields.get(metaIndex);
          name = metaField.name();
          fieldId = metaField.dataSetFieldId();
          index = metaIndex;
        } else {
          name = wireName;
          fieldId = NULL_FIELD_ID;
        }
      } else if (index >= 0 && index < metaFields.size()) {
        DataSetMetaDataConfig.Field metaField = metaFields.get(index);
        name = metaField.name();
        fieldId = metaField.dataSetFieldId();
      } else {
        name = "Field_" + index;
        fieldId = NULL_FIELD_ID;
      }

      fields.add(new DataSetFieldValue(fieldId, name, index, field.value()));
    }

    String dataSetName = null;
    if (metaData != null && !metaData.getName().isEmpty()) {
      dataSetName = metaData.getName();
    }

    var event =
        new DataSetReceivedEvent(
            reader.handle(),
            eventPublisherId(connection, reader, networkMessage),
            eventWriterGroupId(reader, networkMessage),
            eventDataSetWriterId(reader, message),
            networkMessage.sequenceNumber(),
            dataSetMessageSequence,
            dataSetName,
            metaData,
            message.kind(),
            fields);

    service.getEventDispatcher().notifyDataSet(reader.readerRef(), event);
  }

  /**
   * Classify the NetworkMessage's sequence number against its (PublisherId, WriterGroupId) stream
   * window, advancing the window immediately on a NEW verdict. Called exactly once per (reader,
   * NetworkMessage) — from the dispatch loop, after {@code matchesNetworkMessage} and BEFORE the
   * dataSetWriterId filter — and the verdict is shared by every matched DataSetMessage of the
   * NetworkMessage, so the verdict that advanced the window does not make sibling DataSetMessages
   * classify as duplicates.
   *
   * <p>The observation is independent of DataSetMessage matching and delivery: the publisher
   * consumes one NetworkMessage sequence number for EVERY NetworkMessage (§7.2.3 "incremented by
   * exactly one for each message"), including NetworkMessages carrying only keep-alives, only
   * DataSetMessages subsequently dropped at the valid/metadata gates or the DataSetMessage window,
   * or only other writers' DataSetMessages. Observing any later — say, on delivery — would freeze
   * the window while the publisher's counter keeps advancing (e.g. for a reader filtered to one
   * quiet writer while another writer of the same group publishes): after 2^(N-2) unobserved
   * NetworkMessages, resumed data would be wrongly dropped — invalid, then stale — for up to 2^N -
   * 2^(N-2) consecutive messages.
   *
   * @return the verdict, or {@code null} when the NetworkMessage carries no sequence number and the
   *     NetworkMessage window is bypassed.
   */
  private static SequenceNumberWindow.@Nullable Classification observeNetworkMessage(
      ReaderSequenceTracker tracker, DecodedNetworkMessage networkMessage, long nowNanos) {

    UShort sequenceNumber = networkMessage.sequenceNumber();
    if (sequenceNumber == null) {
      return null;
    }

    SequenceNumberWindow.Classification classification =
        tracker.classifyNetworkMessage(
            networkMessage.publisherId(), networkMessage.writerGroupId(), sequenceNumber, nowNanos);

    if (classification == SequenceNumberWindow.Classification.NEW) {
      tracker.acceptNetworkMessage(
          networkMessage.publisherId(), networkMessage.writerGroupId(), sequenceNumber, nowNanos);
    }

    return classification;
  }

  /**
   * Count one DataSetMessage dropped by the §7.2.3 window. Sequence drops are normal operation
   * ("shall be ignored"), counted apart from {@code dataSetMessagesReceived} and from the
   * error-class counters: no {@code lastError}, no diagnostics event.
   */
  private void sequenceDrop(
      DataSetReaderRuntime reader, SequenceNumberWindow.Classification classification) {

    if (classification == SequenceNumberWindow.Classification.STALE) {
      service.getDiagnostics().staleSequenceMessage(reader.path());
    } else {
      service.getDiagnostics().invalidSequenceMessage(reader.path());
    }
  }

  // region message security

  /**
   * The §7.2.4.3 receive-mode gate: whether a group configured with {@code security} accepts a
   * NetworkMessage received with {@code receivedMode}.
   *
   * <ul>
   *   <li>configured None (or Invalid, or no config): accept only received None. The MAY (process
   *       higher) is not exercised: keys are only ever resolved for secured groups, so a secured
   *       message can never be processed by a mode-None group.
   *   <li>configured Sign: drop received None (SHALL); accept Sign and SignAndEncrypt (the MAY —
   *       the keys come from the same token window).
   *   <li>configured SignAndEncrypt: accept only received SignAndEncrypt (SHALL drop below).
   * </ul>
   */
  static boolean receiveModeAccepts(
      @Nullable MessageSecurityConfig security, MessageSecurityMode receivedMode) {

    MessageSecurityMode configured =
        security != null ? security.getMode() : MessageSecurityMode.None;

    boolean configuredSecured =
        configured == MessageSecurityMode.Sign || configured == MessageSecurityMode.SignAndEncrypt;

    if (!configuredSecured) {
      return receivedMode == MessageSecurityMode.None;
    }

    return modeRank(receivedMode) >= modeRank(configured);
  }

  /** The §7.2.4.3 numeric mode order; group-level Invalid ranks like None. */
  private static int modeRank(MessageSecurityMode mode) {
    return switch (mode) {
      case Invalid, None -> 1;
      case Sign -> 2;
      case SignAndEncrypt -> 3;
    };
  }

  /**
   * Count one header-only security skip against the reader group whose keys were used (or would
   * have been used): the group the resolver routed to by declaration order over the plaintext wire
   * identity (PublisherId, WriterGroupId, DataSetWriterIds). One tick per NetworkMessage; security
   * drops never tick {@code decodeErrors}. Secured traffic the resolver refused to route is counted
   * by {@link #countUnroutedSecuritySkip} instead.
   */
  private void countSecuritySkip(ReceivedSecurity security, ReaderGroupRuntime group) {
    switch (security.outcome()) {
      case UNKNOWN_TOKEN -> service.getDiagnostics().unknownTokenMessage(group.path());

      case NO_KEYS -> service.getDiagnostics().staleKeyMessage(group.path());

      case INVALID_SIGNATURE -> {
        service.getDiagnostics().invalidSignatureMessage(group.path());
        warnInvalidSignature(group, security);
      }

      case DECRYPT_FAILED ->
          service
              .getDiagnostics()
              .decryptionError(
                  group.path(),
                  new StatusCode(StatusCodes.Bad_SecurityChecksFailed),
                  "NetworkMessage payload decryption failed (token %s)"
                      .formatted(security.securityTokenId()),
                  null);

      case NO_RESOLVER, VERIFIED -> {
        // NO_RESOLVER cannot occur: dispatch always supplies a resolver; VERIFIED never routes
        // here
      }
    }
  }

  /**
   * Count one header-only security skip the resolver refused to route: no receiving secured group
   * matched the plaintext wire identity, so the message arrived payload-skipped and can never reach
   * the receive-mode gate in {@code handleDecoded}; the drop is counted rather than silent. Applies
   * the gate's own counting condition per group: {@code staleKeyMessages} ticks for each group
   * whose configured mode rejects the received mode and whose readers the message would have
   * matched (unrelated traffic is not counted). The canonical instance is a subscriber whose
   * matching reader groups are all mode-None receiving a publisher's secured traffic: without this
   * tick every security counter would sit at zero while its data silently drops.
   */
  private void countUnroutedSecuritySkip(
      ConnectionRuntime connection,
      String mappingName,
      DecodedNetworkMessage decoded,
      Set<ReaderGroupRuntime> oversizeGroups,
      ReceivedSecurity security) {

    for (ReaderGroupRuntime group : connection.readerGroupRuntimes()) {
      if (oversizeGroups.contains(group)) {
        continue;
      }
      if (!receiveModeAccepts(group.config().getMessageSecurity(), security.mode())
          && anyReaderMatches(group, mappingName, decoded)) {
        service.getDiagnostics().staleKeyMessage(group.path());
      }
    }
  }

  /** Rate-limited WARN for invalid signatures; the counter carries the per-message signal. */
  private void warnInvalidSignature(ReaderGroupRuntime group, ReceivedSecurity security) {
    long now = System.nanoTime();
    long last = lastInvalidSignatureWarnNanos.get();
    if (now - last >= INVALID_SIGNATURE_WARN_INTERVAL_NANOS
        && lastInvalidSignatureWarnNanos.compareAndSet(last, now)) {
      LOGGER.warn(
          "NetworkMessage signature verification failed (reader group '{}', token {});"
              + " counted in invalidSignatureMessages",
          group.path(),
          security.securityTokenId());
    }
  }

  /**
   * Whether the decoded NetworkMessage would have matched at least one receiving reader of {@code
   * group}: the condition for a receive-mode-gate drop to be counted against the group.
   */
  private static boolean anyReaderMatches(
      ReaderGroupRuntime group, String mappingName, DecodedNetworkMessage decoded) {

    for (DataSetReaderRuntime reader : group.readerRuntimes()) {
      if (!reader.mappingName().equals(mappingName)) {
        continue;
      }
      if (isNotReceiving(reader.state())) {
        continue;
      }
      if (matchesNetworkMessage(reader.config(), decoded)) {
        return true;
      }
    }
    return false;
  }

  /**
   * The first secured reader group, in declaration order, whose readers match the plaintext wire
   * identity: the SecurityHeader does not name the SecurityGroup — "The relation to the
   * SecurityGroup is done through DataSetWriterIds contained in the NetworkMessage" (Part 14 Table
   * 154) — so secured groups are routed by (PublisherId, WriterGroupId, DataSetWriterIds).
   * Disabled/Paused groups are skipped; the group's key window (or its absence) decides the rest.
   */
  static @Nullable ReaderGroupRuntime firstMatchingSecuredGroup(
      ConnectionRuntime connection,
      @Nullable PublisherId publisherId,
      @Nullable UShort writerGroupId,
      List<UShort> dataSetWriterIds) {

    for (ReaderGroupRuntime group : connection.readerGroupRuntimes()) {
      MessageSecurityConfig security = group.config().getMessageSecurity();
      if (!PubSubServiceImpl.isSecured(security) || security.getSecurityGroup() == null) {
        continue;
      }
      if (isNotReceiving(group.state())) {
        continue;
      }
      for (DataSetReaderRuntime reader : group.readerRuntimes()) {
        if (readerMatchesWire(reader.config(), publisherId, writerGroupId, dataSetWriterIds)) {
          return group;
        }
      }
    }
    return null;
  }

  /**
   * The pre-decode variant of {@link #matchesNetworkMessage}: matches a reader's identity filters
   * against the plaintext header values available BEFORE the (secured) payload is processed. Absent
   * wire values are wildcards, like everywhere else in the matching chain.
   */
  private static boolean readerMatchesWire(
      DataSetReaderConfig config,
      @Nullable PublisherId publisherId,
      @Nullable UShort writerGroupId,
      List<UShort> dataSetWriterIds) {

    if (!publisherIdMatches(config, publisherId)) {
      return false;
    }

    UShort writerGroupIdFilter = config.getWriterGroupId();
    if (writerGroupIdFilter != null && writerGroupIdFilter.intValue() != 0) {
      if (writerGroupId != null && !writerGroupIdFilter.equals(writerGroupId)) {
        return false;
      }
    }

    if (!dataSetWriterIds.isEmpty()) {
      for (UShort dataSetWriterId : dataSetWriterIds) {
        if (dataSetWriterIdMatches(config, dataSetWriterId)) {
          return true;
        }
      }
      return false;
    }

    return true;
  }

  /**
   * The engine-side {@link SecurityContextResolver}: routes a received secured NetworkMessage to
   * the first matching secured reader group (declaration order) and selects the key for the
   * requested token from the {@link SecurityKeyManager}'s window. One instance per dispatch call,
   * confined to the connection dispatch thread; it records the group it routed to so the dispatcher
   * attributes security drops to the same group.
   *
   * <p>Side effects hop to the scheduler inside the manager: an unknown token triggers the
   * single-flight refresh, force-key-reset (SecurityFlags bit 3) triggers a proactive refetch.
   * Never blocks, never throws for a normal miss.
   */
  private static final class SecurityResolver implements SecurityContextResolver {

    private final PubSubServiceImpl service;
    private final ConnectionRuntime connection;

    private @Nullable ReaderGroupRuntime resolvedGroup;

    private SecurityResolver(PubSubServiceImpl service, ConnectionRuntime connection) {
      this.service = service;
      this.connection = connection;
    }

    @Override
    public Resolution resolve(ResolveRequest request) {
      ReaderGroupRuntime group =
          firstMatchingSecuredGroup(
              connection,
              request.publisherId(),
              request.writerGroupId(),
              request.dataSetWriterIds());
      resolvedGroup = group;

      if (group == null) {
        return new Resolution.Refused(SecurityOutcome.NO_KEYS);
      }

      MessageSecurityConfig security = group.config().getMessageSecurity();
      SecurityGroupRef ref = security != null ? security.getSecurityGroup() : null;
      if (ref == null) {
        // unreachable: firstMatchingSecuredGroup requires a SecurityGroup reference
        return new Resolution.Refused(SecurityOutcome.NO_KEYS);
      }

      SecurityKeyManager manager = service.getSecurityKeyManager();

      if (request.forceKeyReset()) {
        manager.onForceKeyReset(ref);
      }

      SecurityKeyManager.SubscriberKeyWindow window = manager.subscriberKeyWindow(ref);
      if (window == null) {
        // no keys (never fetched, or expired past 2×KeyLifetime and wiped): stale-key drop
        return new Resolution.Refused(SecurityOutcome.NO_KEYS);
      }

      SecurityKeyMaterial keys = window.keyFor(request.securityTokenId().longValue());
      if (keys == null) {
        manager.onUnknownToken(ref, request.securityTokenId().longValue());
        return new Resolution.Refused(SecurityOutcome.UNKNOWN_TOKEN);
      }

      return new Resolution.Keys(keys);
    }

    /** The group the last {@link #resolve} routed to; cleared by this call. */
    @Nullable ReaderGroupRuntime takeResolvedGroup() {
      ReaderGroupRuntime group = resolvedGroup;
      resolvedGroup = null;
      return group;
    }
  }

  // endregion

  private void handleMetaData(
      ConnectionRuntime connection, DataSetReaderRuntime reader, DecodedMetaData metaData) {

    DataSetMetaDataConfig converted;
    try {
      converted = MetadataCache.fromDataType(metaData.metaData());
    } catch (RuntimeException e) {
      service
          .getDiagnostics()
          .decodeError(
              reader.path(),
              new StatusCode(StatusCodes.Bad_DecodingError),
              "invalid DataSetMetaData announcement: " + e.getMessage(),
              e);
      return;
    }

    if (reader.config().getMetadataPolicy() != MetadataPolicy.REQUIRE_CONFIGURED) {
      service.getMetadataCache().putDiscovered(reader.handle(), converted);

      // double-check: dispose() (which removes the entry) may have run concurrently on the
      // engine lock between the put landing and here; handles are never reused, so an entry
      // that lands after dispose would otherwise leak forever. dispose() sets the flag before
      // removing, so every interleaving leaves the entry removed.
      if (reader.isDisposed()) {
        service.getMetadataCache().remove(reader.handle());
      }

      // the reader now has effective metadata: any REQUEST_IF_MISSING probe loop can stop
      DiscoveryRuntime discovery = connection.discoveryRuntime();
      if (discovery != null) {
        discovery.onMetaDataApplied(reader);
      }
    }

    var event =
        new MetaDataReceivedEvent(
            reader.handle(),
            metaData.metaData().getName(),
            metaData.metaData(),
            metaData.metaData().getConfigurationVersion());

    service.getEventDispatcher().notifyMetaData(event);
  }

  /**
   * The metadata used to decode for this reader: discovered metadata when the policy accepts it and
   * an announcement has been received, otherwise the configured metadata.
   */
  private @Nullable DataSetMetaDataConfig effectiveMetaData(DataSetReaderRuntime reader) {
    if (reader.config().getMetadataPolicy() != MetadataPolicy.REQUIRE_CONFIGURED) {
      DataSetMetaDataConfig discovered = service.getMetadataCache().getDiscovered(reader.handle());
      if (discovered != null) {
        return discovered;
      }
    }
    return reader.configuredMetaData();
  }

  private static boolean isNotReceiving(PubSubState state) {
    return state != PubSubState.PreOperational
        && state != PubSubState.Operational
        && state != PubSubState.Error;
  }

  private static boolean matchesNetworkMessage(
      DataSetReaderConfig config, DecodedNetworkMessage decoded) {

    if (!publisherIdMatches(config, decoded.publisherId())) {
      return false;
    }

    // Identifier filters are applied only when the identifier is present in the message: a
    // publisher that omits it from the network message header (e.g. GroupFlags without
    // WriterGroupId) cannot be checked against the configured value (Part 14 6.2.9 matching;
    // open62541 behaves the same way).
    UShort writerGroupId = config.getWriterGroupId();
    if (writerGroupId != null && writerGroupId.intValue() != 0) {
      if (decoded.writerGroupId() != null && !writerGroupId.equals(decoded.writerGroupId())) {
        return false;
      }
    }

    if (config.getSettings() instanceof UadpDataSetReaderSettings settings) {
      if (settings.getGroupVersion().longValue() != 0L
          && decoded.groupVersion() != null
          && !settings.getGroupVersion().equals(decoded.groupVersion())) {
        return false;
      }

      return settings.getNetworkMessageNumber().intValue() == 0
          || decoded.networkMessageNumber() == null
          || settings.getNetworkMessageNumber().equals(decoded.networkMessageNumber());
    }

    return true;
  }

  /** Find the index of the metadata field named {@code name}, or -1. */
  private static int indexOfMetaField(List<DataSetMetaDataConfig.Field> metaFields, String name) {
    for (int i = 0; i < metaFields.size(); i++) {
      if (metaFields.get(i).name().equals(name)) {
        return i;
      }
    }
    return -1;
  }

  private static boolean publisherIdMatches(
      DataSetReaderConfig config, @Nullable PublisherId publisherId) {

    PublisherId filter = config.getPublisherId();

    // a message without a PublisherId can only be processed by readers without a PublisherId
    // filter; ids of the same type require an exact match
    if (filter == null) {
      return true;
    }
    if (filter.equals(publisherId)) {
      return true;
    }
    if (publisherId == null) {
      return false;
    }

    // ids of differing types compare by canonical String form: the JSON mapping carries every
    // PublisherId as a String (Part 14 §7.2.5.3 Table 184), so a reader filtering on a numeric
    // id must still match the decoded string form (and vice versa)
    return filter.getClass() != publisherId.getClass()
        && filter.toCanonicalString().equals(publisherId.toCanonicalString());
  }

  private static boolean dataSetWriterIdMatches(
      DataSetReaderConfig config, @Nullable UShort dataSetWriterId) {

    UShort filter = config.getDataSetWriterId();
    if (filter == null || filter.intValue() == 0) {
      return true;
    }
    // absent on the wire (payload header disabled, fixed-layout publisher): the filter cannot be
    // applied and the reader identifies the message by its configuration (Part 14 6.2.9)
    return dataSetWriterId == null || filter.equals(dataSetWriterId);
  }

  private static PublisherId eventPublisherId(
      ConnectionRuntime connection, DataSetReaderRuntime reader, DecodedNetworkMessage decoded) {

    if (decoded.publisherId() != null) {
      return decoded.publisherId();
    }
    if (reader.config().getPublisherId() != null) {
      return reader.config().getPublisherId();
    }
    PublisherId connectionPublisherId = connection.config().publisherId();
    if (connectionPublisherId != null) {
      return connectionPublisherId;
    }
    return PublisherId.string("");
  }

  private static UShort eventWriterGroupId(
      DataSetReaderRuntime reader, DecodedNetworkMessage decoded) {

    if (decoded.writerGroupId() != null) {
      return decoded.writerGroupId();
    }
    UShort configured = reader.config().getWriterGroupId();
    return configured != null ? configured : ushort(0);
  }

  private static UShort eventDataSetWriterId(
      DataSetReaderRuntime reader, DecodedDataSetMessage message) {

    if (message.dataSetWriterId() != null) {
      return message.dataSetWriterId();
    }
    UShort configured = reader.config().getDataSetWriterId();
    return configured != null ? configured : ushort(0);
  }
}
