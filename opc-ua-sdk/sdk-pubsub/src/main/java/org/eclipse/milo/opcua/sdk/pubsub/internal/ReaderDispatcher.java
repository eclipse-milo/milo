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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetFieldValue;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedField;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedMetaData;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageMappingProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpDecodedMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpDiscoveryProbe;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMetaDataAnnouncement;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.jspecify.annotations.Nullable;

/**
 * Subscriber-side dispatch: decodes received datagrams and routes the decoded DataSetMessages to
 * the matching DataSetReaders via the Part 14 filter chain (§6.2.9.x / §6.3.1.4.x), where null/0
 * filter values are wildcards.
 *
 * <p>Runs entirely on the transport executor; decode and match failures are counted in diagnostics
 * and never thrown. The connection's {@code networkMessagesReceived} counter ticks once per
 * arrival, before any decode attempt; decode failures tick {@code decodeErrors} at the connection
 * path unless another of the connection's mappings decoded content — DataSetMessages, metadata, or
 * a discovery message — from the same buffer (on mixed-mapping broker connections every message is
 * offered to every mapping, so the "wrong" mapping's failure on a message the right mapping decoded
 * is expected and not an error). A mapping's own partial content never suppresses its own failure:
 * a truncated message that still delivered a decodable prefix ticks that mapping's failure once
 * while suppressing the other mappings' failures on the same bytes. A tolerated skip that decoded
 * nothing, such as the UADP decoder skipping foreign input by its version nibble, suppresses
 * nothing: a buffer no mapping could decode ticks {@code decodeErrors} once per failed mapping. A
 * reader group with a non-zero {@code maxNetworkMessageSize} never sees messages larger than it;
 * such messages tick {@code decodeErrors} at the group path with {@code
 * Bad_EncodingLimitsExceeded}. Subscriber-side sequence number tracking (duplicate/reordering
 * detection) is not implemented in this version.
 */
final class ReaderDispatcher {

  /** dataSetFieldId used when no metadata names a decoded field. */
  private static final UUID NULL_FIELD_ID = new UUID(0L, 0L);

  private final PubSubServiceImpl service;

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
    // "received" means "a message arrived and was offered to decode": tick once per arrival,
    // before any mapping runs, so the counter is independent of the decode outcome
    service.getDiagnostics().networkMessageReceived(connection.path());

    // Milo extension: a reader group with a non-zero maxNetworkMessageSize does not accept
    // messages larger than it (Part 14 gives the parameter no receive-side semantics)
    Set<ReaderGroupRuntime> oversizeGroups = oversizeGroups(connection, buffer.readableBytes());

    Map<String, MessageMappingProvider> mappings = connection.subscriberMappings();

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
        DecodeContext context = DecodeContext.of(service.getEncodingContext());
        if (provider instanceof UadpMessageMapping uadpMapping) {
          decoded = uadpMapping.decodeMessage(context, buffer.slice());
        } else {
          decoded = provider.decode(context, buffer.slice());
        }
      } catch (Exception e) {
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
        handleDecoded(connection, mappingName, networkMessage, oversizeGroups);
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
   * Whether a decode actually decoded something from the buffer: DataSetMessages, metadata, or a
   * discovery probe/announcement — including content decoded before a surfaced failure point.
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
      return !networkMessage.messages().isEmpty() || !networkMessage.metaData().isEmpty();
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
      if (!isReceiving(group.state())) {
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
      DecodedNetworkMessage decoded,
      Set<ReaderGroupRuntime> oversizeGroups) {

    for (ReaderGroupRuntime group : connection.readerGroupRuntimes()) {
      if (oversizeGroups.contains(group)) {
        continue;
      }

      boolean groupMatched = false;

      for (DataSetReaderRuntime reader : group.readerRuntimes()) {
        if (!reader.mappingName().equals(mappingName)) {
          continue;
        }
        if (!isReceiving(reader.state())) {
          continue;
        }

        // metadata announcements match on (PublisherId, DataSetWriterId) only
        for (DecodedMetaData metaData : decoded.metaData()) {
          if (publisherIdMatches(reader.config(), decoded.publisherId())
              && dataSetWriterIdMatches(reader.config(), metaData.dataSetWriterId())) {
            handleMetaData(connection, reader, metaData);
          }
        }

        if (!decoded.messages().isEmpty() && matchesNetworkMessage(reader.config(), decoded)) {
          for (DecodedDataSetMessage message : decoded.messages()) {
            if (dataSetWriterIdMatches(reader.config(), message.dataSetWriterId())) {
              groupMatched = true;
              deliver(connection, group, reader, decoded, message);
            }
          }
        }
      }

      if (groupMatched) {
        service.getDiagnostics().networkMessageReceived(group.path());
      }
    }
  }

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
        if (!isReceiving(reader.state())) {
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
      DecodedDataSetMessage message) {

    service.getDiagnostics().dataSetMessageReceived(group.path());
    service.getDiagnostics().dataSetMessageReceived(reader.path());

    // a matched message reveals the identifiers a wildcard-filtered REQUEST_IF_MISSING reader
    // needs before it can emit discovery probes
    DiscoveryRuntime discovery = connection.discoveryRuntime();
    if (discovery != null && discovery.hasDeferredProbes()) {
      discovery.onDataSetMessageMatched(
          reader, networkMessage.publisherId(), message.dataSetWriterId());
    }

    if (message.kind() == DataSetMessageKind.KEEP_ALIVE) {
      reader.onMessageAccepted();
      return;
    }

    if (!message.valid()) {
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

    reader.onMessageAccepted();

    // Part 14 §6.2.1 Table 2: a DataSetReader changes to Operational after the first key frame
    // or event DataSetMessage; pre-key delta frames are still delivered and reset the receive
    // timeout but do not complete startup
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
            dataSetName,
            metaData,
            fields);

    service.getEventDispatcher().notifyDataSet(reader.readerRef(), event);
  }

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

  private static boolean isReceiving(PubSubState state) {
    return state == PubSubState.PreOperational
        || state == PubSubState.Operational
        || state == PubSubState.Error;
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

      if (settings.getNetworkMessageNumber().intValue() != 0
          && decoded.networkMessageNumber() != null
          && !settings.getNetworkMessageNumber().equals(decoded.networkMessageNumber())) {
        return false;
      }
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
