/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyMaterial;
import org.eclipse.milo.opcua.sdk.pubsub.security.UadpMessageSecurity;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decodes one UADP NetworkMessage (OPC UA Part 14 §7.2.4) into a {@link DecodedNetworkMessage}.
 *
 * <p>Decoding is tolerant by design: malformed or unsupported input never raises an exception to
 * the caller. Whatever was decoded before the problem is returned, and skipped DataSetMessages are
 * represented as entries with {@code valid == false} and no fields. A NetworkMessage whose header
 * cannot be decoded at all yields an empty {@link DecodedNetworkMessage}.
 *
 * <p>Failures that end decoding early are surfaced via {@link DecodedNetworkMessage#failure()}
 * rather than thrown: truncated or malformed input that raises an exception mid-decode, any
 * explicit length field exceeding the remaining bytes (a DataSetMessage Sizes entry, the
 * PromotedFields Size, the SecurityHeader NonceLength, the ChunkData length, a secured message
 * shorter than its promised SecurityFooter and Signature, a discovery probe's DataSetWriterIds
 * count — the truncation signatures), and chunked NetworkMessages on surfaces that do not
 * reassemble them ({@code Bad_NotSupported}: the legacy decode surface, and chunked discovery
 * messages everywhere). Input that is merely tolerated and skipped — a non-UADP version nibble,
 * reserved flag or type values, unsupported discovery content, a security outcome other than {@link
 * SecurityOutcome#VERIFIED} — reports no failure; the one overlap is a secured message too short
 * for its trailing SecurityFooter and Signature, a truncation signature that reports {@code
 * Bad_DecodingError} and, because no truncation can verify, also records {@link
 * SecurityOutcome#INVALID_SIGNATURE}.
 *
 * <p>Scope and limitations:
 *
 * <ul>
 *   <li>Data Key Frame, Data Delta Frame, Event, and Keep Alive DataSetMessages with Variant or
 *       DataValue field encoding are decoded.
 *   <li>RawData field encoding requires metadata-driven offsets and is not supported; affected
 *       DataSetMessages are skipped ({@code valid == false}, empty fields).
 *   <li>Discovery announcements of type DataSetMetaData and DataSetMetaData probes (ProbeType 1,
 *       InformationType 2) are decoded; {@link #decodeMessage(DecodeContext, ByteBuf)} surfaces
 *       them as {@link UadpMetaDataAnnouncement} (any status, including Bad denials) and {@link
 *       UadpDiscoveryProbe}. The legacy {@link #decode(DecodeContext, ByteBuf)} surface folds
 *       non-Bad announcements into {@link DecodedMetaData} and drops probes and Bad-status
 *       announcements. FindApplications probes, other probe InformationTypes, and other
 *       announcement types are tolerated and skipped.
 *   <li>Message security (Part 14 §7.2.4.4.3) is processed when the {@link DecodeContext} carries a
 *       {@link SecurityContextResolver}: the trailing HMAC is verified BEFORE any payload byte is
 *       parsed, then — for SignAndEncrypt — a COPY of the payload region is decrypted (the shared
 *       transport buffer is never mutated) and parsed. Either sign-only SecurityHeader form is
 *       accepted (NonceLength is self-describing), a flagged SecurityFooter is skipped, and
 *       reserved SecurityFlags bits cause the whole message to be skipped like other reserved flag
 *       values. Any non-{@link SecurityOutcome#VERIFIED} outcome — no resolver, resolver refusal,
 *       signature mismatch, decrypt failure — yields a tolerated, header-only result whose {@link
 *       DecodedNetworkMessage#security()} reports the outcome; these are skips, not failures,
 *       except that truncation below the promised SecurityFooter and Signature also reports a
 *       {@code Bad_DecodingError} failure (a truncation signature, like the other explicit length
 *       overruns).
 *   <li>Chunk NetworkMessages (§7.2.4.4.4) are parsed AFTER verification/decryption — the Table 159
 *       chunk fields live inside the encrypted region — and surfaced as {@link
 *       DecodedNetworkMessage#chunk()} on the {@link #decodeMessage(DecodeContext, ByteBuf)}
 *       surface; reassembly happens downstream, per connection. The legacy {@link
 *       #decode(DecodeContext, ByteBuf)} surface keeps skipping chunked payloads with a {@code
 *       Bad_NotSupported} failure (legacy callers cannot reassemble), as do chunked discovery
 *       messages on both surfaces. ActionHeaders are detected and their payloads skipped. A
 *       PromotedFields block is skipped via its Size field and the payload after it is decoded
 *       normally.
 *   <li>If the PayloadHeader is absent the payload is assumed to contain a single DataSetMessage.
 * </ul>
 *
 * <p>Stateless: a new instance is used for each NetworkMessage.
 */
final class UadpNetworkMessageDecoder {

  private static final Logger LOGGER = LoggerFactory.getLogger(UadpNetworkMessageDecoder.class);

  /** UADP NetworkMessage type values, from ExtendedFlags2 bits 2-4. */
  private static final int TYPE_DATA = 0;

  private static final int TYPE_DISCOVERY_PROBE = 1;
  private static final int TYPE_DISCOVERY_ANNOUNCEMENT = 2;

  /** Discovery announcement type values (Part 14 §7.2.4.6.3, Table 168). */
  private static final int ANNOUNCEMENT_DATA_SET_META_DATA = 2;

  /** Field Encoding values, from DataSetFlags1 bits 1-2. */
  private static final int FIELD_ENCODING_RAW_DATA = 1;

  private static final int FIELD_ENCODING_DATA_VALUE = 2;
  private static final int FIELD_ENCODING_RESERVED = 3;

  private final List<DecodedDataSetMessage> messages = new ArrayList<>();
  private final List<DecodedMetaData> metaData = new ArrayList<>();

  private @Nullable UadpDiscoveryProbe probe;
  private @Nullable UadpMetaDataAnnouncement announcement;

  private DecodedNetworkMessage.@Nullable Failure failure;

  private @Nullable PublisherId publisherId;
  private @Nullable UShort writerGroupId;
  private @Nullable UInteger groupVersion;
  private @Nullable UShort networkMessageNumber;
  private @Nullable UShort sequenceNumber;
  private @Nullable DateTime timestamp;

  private @Nullable ReceivedSecurity security;
  private @Nullable DecodedChunk decodedChunk;
  private @Nullable UShort chunkDataSetWriterId;

  private final EncodingContext encodingContext;
  private final @Nullable SecurityContextResolver resolver;

  /**
   * Whether this decode runs on the legacy {@link #decode(DecodeContext, ByteBuf)} surface, which
   * keeps the historic detect-and-drop behavior for chunked NetworkMessages.
   */
  private final boolean legacySurface;

  private final ByteBuf buffer;
  private final OpcUaBinaryDecoder decoder;

  private UadpNetworkMessageDecoder(DecodeContext context, ByteBuf buffer, boolean legacySurface) {
    this.encodingContext = context.encodingContext();
    this.resolver = context.securityResolver();
    this.legacySurface = legacySurface;
    this.buffer = buffer;

    decoder = new OpcUaBinaryDecoder(encodingContext).setBuffer(buffer);
  }

  /**
   * Decode one NetworkMessage from {@code buffer} into the legacy data-plane shape.
   *
   * <p>Discovery content is folded into the data-plane result the way it always was: a
   * DataSetMetaData announcement with a non-Bad status becomes a {@link DecodedMetaData} entry,
   * while probes and Bad-status announcements yield a header-only result. Use {@link
   * #decodeMessage(DecodeContext, ByteBuf)} to surface them. Chunked NetworkMessages keep the
   * historic detect-and-drop behavior on this surface: a {@code Bad_NotSupported} failure and no
   * {@link DecodedNetworkMessage#chunk()} component.
   *
   * @param context the decode context.
   * @param buffer the buffer containing the received NetworkMessage; the caller retains ownership.
   * @return the decoded NetworkMessage; possibly partial or empty if the input was malformed or
   *     unsupported.
   */
  static DecodedNetworkMessage decode(DecodeContext context, ByteBuf buffer) {
    var decoder = new UadpNetworkMessageDecoder(context, buffer, true);

    try {
      decoder.decodeNetworkMessage();
    } catch (Exception e) {
      LOGGER.debug("failed to fully decode NetworkMessage: {}", e.getMessage(), e);
      decoder.recordFailure(e);
    }

    return decoder.legacyResult();
  }

  /**
   * Decode one NetworkMessage from {@code buffer}, surfacing discovery messages.
   *
   * @param context the decode context.
   * @param buffer the buffer containing the received NetworkMessage; the caller retains ownership.
   * @return a {@link UadpDiscoveryProbe} for a DataSetMetaData probe, a {@link
   *     UadpMetaDataAnnouncement} for a DataSetMetaData announcement of any status, or a {@link
   *     DecodedNetworkMessage} otherwise — possibly partial or empty if the input was malformed,
   *     unsupported, or discovery content that is tolerated but not surfaced.
   */
  static UadpDecodedMessage decodeMessage(DecodeContext context, ByteBuf buffer) {
    var decoder = new UadpNetworkMessageDecoder(context, buffer, false);

    try {
      decoder.decodeNetworkMessage();
    } catch (Exception e) {
      LOGGER.debug("failed to fully decode NetworkMessage: {}", e.getMessage(), e);
      decoder.recordFailure(e);
    }

    return decoder.result();
  }

  /**
   * Decode one reassembled chunk payload — a single DataSetMessage (header and body) — into a
   * {@link DecodedNetworkMessage} inheriting the completing chunk's header values.
   *
   * <p>The result's {@code sequenceNumber} is {@code null}: every chunk NetworkMessage already
   * consumed its own NetworkMessage sequence number through the per-reader recency windows when it
   * was dispatched, and re-observing the completing chunk's number would wrongly classify the
   * reassembled message as a duplicate. The reassembled DataSetMessage's own sequence number drives
   * the DataSetMessage window normally. The chunk header's {@code security} is inherited (every
   * chunk was individually verified); {@code chunk} is {@code null}.
   *
   * @param context the decode context.
   * @param chunkHeader the completing chunk's NetworkMessage, for the inherited header values.
   * @param dataSetWriterId the DataSetWriterId of the chunked stream, or {@code null} if the chunk
   *     NetworkMessages carried no PayloadHeader.
   * @param payload the reassembled payload; the caller retains ownership.
   * @return the decoded NetworkMessage; tolerant like the other surfaces — a malformed payload
   *     yields an empty result with a {@code Bad_DecodingError} failure.
   */
  static DecodedNetworkMessage decodeReassembled(
      DecodeContext context,
      DecodedNetworkMessage chunkHeader,
      @Nullable UShort dataSetWriterId,
      ByteBuf payload) {

    List<DecodedDataSetMessage> messages = new ArrayList<>(1);
    DecodedNetworkMessage.Failure failure = null;

    OpcUaBinaryDecoder decoder =
        new OpcUaBinaryDecoder(context.encodingContext()).setBuffer(payload);
    try {
      messages.add(decodeDataSetMessage(decoder, payload, dataSetWriterId));
    } catch (Exception e) {
      LOGGER.debug("failed to decode reassembled DataSetMessage: {}", e.getMessage(), e);
      failure =
          new DecodedNetworkMessage.Failure(
              new StatusCode(StatusCodes.Bad_DecodingError),
              "failed to decode reassembled DataSetMessage: " + e.getMessage(),
              e);
    }

    return DecodedNetworkMessage.of(
        chunkHeader.publisherId(),
        chunkHeader.writerGroupId(),
        chunkHeader.groupVersion(),
        chunkHeader.networkMessageNumber(),
        null,
        chunkHeader.timestamp(),
        messages,
        List.of(),
        failure,
        chunkHeader.security(),
        null);
  }

  /** Record the exception that ended decoding, unless a failure was already recorded. */
  private void recordFailure(Exception e) {
    if (failure == null) {
      failure =
          new DecodedNetworkMessage.Failure(
              new StatusCode(StatusCodes.Bad_DecodingError),
              "failed to fully decode NetworkMessage: " + e.getMessage(),
              e);
    }
  }

  private UadpDecodedMessage result() {
    if (probe != null) {
      return probe;
    }
    if (announcement != null) {
      return announcement;
    }
    return dataPlaneResult();
  }

  private DecodedNetworkMessage legacyResult() {
    if (announcement != null && !announcement.statusCode().isBad()) {
      metaData.add(new DecodedMetaData(announcement.dataSetWriterId(), announcement.metaData()));
    } else if (announcement != null) {
      // The Publisher cannot provide metadata for this DataSetWriter; the legacy surface has
      // no slot for denials.
      LOGGER.debug(
          "DataSetMetaData announcement with Bad status: {} (dataSetWriterId={})",
          announcement.statusCode(),
          announcement.dataSetWriterId());
    }

    return dataPlaneResult();
  }

  private DecodedNetworkMessage dataPlaneResult() {
    return DecodedNetworkMessage.of(
        publisherId,
        writerGroupId,
        groupVersion,
        networkMessageNumber,
        sequenceNumber,
        timestamp,
        messages,
        metaData,
        failure,
        security,
        decodedChunk);
  }

  private void decodeNetworkMessage() {
    int byte0 = buffer.readUnsignedByte();

    int version = byte0 & 0x0F;
    if (version != 1) {
      LOGGER.debug("unsupported UADP version: {}", version);
      return;
    }

    boolean publisherIdEnabled = (byte0 & 0x10) != 0;
    boolean groupHeaderEnabled = (byte0 & 0x20) != 0;
    boolean payloadHeaderEnabled = (byte0 & 0x40) != 0;

    int extendedFlags1 = (byte0 & 0x80) != 0 ? buffer.readUnsignedByte() : 0;
    int extendedFlags2 = (extendedFlags1 & 0x80) != 0 ? buffer.readUnsignedByte() : 0;

    int publisherIdType = extendedFlags1 & 0x07;
    boolean dataSetClassIdEnabled = (extendedFlags1 & 0x08) != 0;
    boolean securityEnabled = (extendedFlags1 & 0x10) != 0;
    boolean timestampEnabled = (extendedFlags1 & 0x20) != 0;
    boolean picoSecondsEnabled = (extendedFlags1 & 0x40) != 0;

    boolean chunk = (extendedFlags2 & 0x01) != 0;
    boolean promotedFieldsEnabled = (extendedFlags2 & 0x02) != 0;
    int messageType = (extendedFlags2 >> 2) & 0x07;
    boolean actionHeaderEnabled = (extendedFlags2 & 0x20) != 0;

    if ((extendedFlags2 & 0xC0) != 0) {
      LOGGER.debug("reserved ExtendedFlags2 bits set: 0x{}", Integer.toHexString(extendedFlags2));
      return;
    }
    if (messageType > TYPE_DISCOVERY_ANNOUNCEMENT) {
      LOGGER.debug("reserved NetworkMessage type: {}", messageType);
      return;
    }

    if (publisherIdEnabled) {
      publisherId = decodePublisherId(publisherIdType);
      if (publisherId == null) {
        return;
      }
    }

    if (dataSetClassIdEnabled) {
      // Consume; the DataSetClassId is not surfaced by DecodedNetworkMessage.
      decoder.decodeGuid();
    }

    if (groupHeaderEnabled) {
      int groupFlags = buffer.readUnsignedByte();

      if ((groupFlags & 0xF0) != 0) {
        LOGGER.debug("reserved GroupFlags bits set: 0x{}", Integer.toHexString(groupFlags));
        return;
      }

      if ((groupFlags & 0x01) != 0) {
        writerGroupId = decoder.decodeUInt16();
      }
      if ((groupFlags & 0x02) != 0) {
        groupVersion = decoder.decodeUInt32();
      }
      if ((groupFlags & 0x04) != 0) {
        networkMessageNumber = decoder.decodeUInt16();
      }
      if ((groupFlags & 0x08) != 0) {
        sequenceNumber = decoder.decodeUInt16();
      }
    }

    List<UShort> dataSetWriterIds = null;

    if (payloadHeaderEnabled) {
      if (chunk) {
        // Chunked NetworkMessage payload header: a single DataSetWriterId, no Count (Table 158).
        chunkDataSetWriterId = decoder.decodeUInt16();
      } else if (messageType == TYPE_DATA) {
        int count = buffer.readUnsignedByte();

        dataSetWriterIds = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
          dataSetWriterIds.add(decoder.decodeUInt16());
        }
      } else {
        // Discovery messages shall not have a PayloadHeader; the layout that follows is
        // undefined.
        LOGGER.debug("discovery NetworkMessage with PayloadHeader enabled");
        return;
      }
    }

    if (timestampEnabled) {
      timestamp = decoder.decodeDateTime();
    }
    if (picoSecondsEnabled) {
      // Consume; PicoSeconds are not surfaced by DecodedNetworkMessage.
      decoder.decodeUInt16();
    }

    if (promotedFieldsEnabled) {
      // Skip the PromotedFields block via its Size field; the payload that follows is still
      // decodable.
      int size = decoder.decodeUInt16().intValue();
      if (size > buffer.readableBytes()) {
        // a truncation signature: the Size field promised more bytes than arrived
        LOGGER.debug("PromotedFields size exceeds remaining bytes: {}", size);
        failure =
            new DecodedNetworkMessage.Failure(
                new StatusCode(StatusCodes.Bad_DecodingError),
                "PromotedFields size exceeds remaining bytes: " + size,
                null);
        return;
      }
      buffer.skipBytes(size);
    }

    SecurityHeader securityHeader = null;
    if (securityEnabled) {
      securityHeader = decodeSecurityHeader();
      if (securityHeader == null) {
        // reserved SecurityFlags bits (tolerated skip) or a truncation failure; both end decoding
        return;
      }
    }

    if (actionHeaderEnabled) {
      LOGGER.debug("ActionHeader is not supported; skipping payload");
      return;
    }

    // The payload window: for unsecured messages it is the shared buffer/decoder (the payload is
    // the rest of the buffer, as always); for secured messages it is bounded away from the
    // SecurityFooter and Signature — and, for SignAndEncrypt, a decoder-local decrypted copy.
    OpcUaBinaryDecoder payloadDecoder = decoder;
    ByteBuf payloadBuffer = buffer;

    if (securityHeader != null && (securityHeader.securityFlags() & 0x03) != 0) {
      ByteBuf securedPayload = processSecuredPayload(securityHeader, dataSetWriterIds);
      if (securedPayload == null) {
        // header-only skip; the outcome is recorded in `security`
        return;
      }
      payloadBuffer = securedPayload;
      payloadDecoder = new OpcUaBinaryDecoder(encodingContext).setBuffer(payloadBuffer);
    }

    if (chunk) {
      if (legacySurface || messageType != TYPE_DATA) {
        // Legacy surface callers cannot reassemble; chunked discovery messages are not
        // reassembled at all. Surface the skip as a failure so subscribers can observe chunked
        // traffic instead of silent loss.
        LOGGER.debug("chunked NetworkMessage is not supported; skipping payload");
        failure =
            new DecodedNetworkMessage.Failure(
                new StatusCode(StatusCodes.Bad_NotSupported),
                "chunked NetworkMessage is not supported",
                null);
        return;
      }

      // The Table 159 chunk fields live inside the (now decrypted) payload region.
      decodeChunkPayload(payloadDecoder, payloadBuffer);
      return;
    }

    if (messageType == TYPE_DISCOVERY_ANNOUNCEMENT) {
      decodeDiscoveryAnnouncement(payloadDecoder, payloadBuffer);
    } else if (messageType == TYPE_DISCOVERY_PROBE) {
      decodeDiscoveryProbe(payloadDecoder, payloadBuffer);
    } else {
      decodeDataPayload(payloadDecoder, payloadBuffer, payloadHeaderEnabled, dataSetWriterIds);
    }
  }

  private @Nullable PublisherId decodePublisherId(int publisherIdType) {
    switch (publisherIdType) {
      case 0x00:
        return PublisherId.ubyte(decoder.decodeByte());
      case 0x01:
        return PublisherId.uint16(decoder.decodeUInt16());
      case 0x02:
        return PublisherId.uint32(decoder.decodeUInt32());
      case 0x03:
        return PublisherId.uint64(decoder.decodeUInt64());
      case 0x04:
        String value = decoder.decodeString();
        if (value == null) {
          LOGGER.debug("null String PublisherId");
          return null;
        }
        return PublisherId.string(value);
      default:
        LOGGER.debug("reserved PublisherId type: {}", publisherIdType);
        return null;
    }
  }

  /**
   * The parsed SecurityHeader (Part 14 Table 154).
   *
   * @param securityFlags the SecurityFlags byte: bit 0 signed, bit 1 encrypted, bit 2 footer, bit 3
   *     force key reset.
   * @param securityTokenId the SecurityTokenId; 0 in the mode-None form.
   * @param messageNonce the MessageNonce, {@code NonceLength} bytes; the length is self-describing,
   *     so either sign-only form (8 or 0 bytes) is accepted.
   * @param securityFooterSize the SecurityFooterSize, or 0 when bit 2 is clear (the field is then
   *     absent).
   */
  private record SecurityHeader(
      int securityFlags, UInteger securityTokenId, byte[] messageNonce, int securityFooterSize) {}

  /**
   * Decode the SecurityHeader.
   *
   * @return the parsed header, or {@code null} if decoding of this NetworkMessage must end:
   *     reserved SecurityFlags bits are set (a tolerated skip, like other reserved flag values;
   *     Table 154: "the receiver shall skip messages where the reserved bits are not false") or the
   *     header is a truncation signature (recorded as a failure).
   */
  private @Nullable SecurityHeader decodeSecurityHeader() {
    int securityFlags = buffer.readUnsignedByte();

    if ((securityFlags & 0xF0) != 0) {
      LOGGER.debug(
          "reserved SecurityFlags bits set: 0x{}; skipping message",
          Integer.toHexString(securityFlags));
      return null;
    }

    UInteger securityTokenId = decoder.decodeUInt32();

    int nonceLength = buffer.readUnsignedByte();
    if (nonceLength > buffer.readableBytes()) {
      // a truncation signature: the NonceLength promised more bytes than arrived
      LOGGER.debug("NonceLength exceeds remaining bytes: {}", nonceLength);
      failure =
          new DecodedNetworkMessage.Failure(
              new StatusCode(StatusCodes.Bad_DecodingError),
              "SecurityHeader NonceLength exceeds remaining bytes: " + nonceLength,
              null);
      return null;
    }
    byte[] messageNonce = new byte[nonceLength];
    buffer.readBytes(messageNonce);

    int securityFooterSize = 0;
    if ((securityFlags & 0x04) != 0) {
      // SecurityFooterSize is present iff bit 2 is set; the footer itself sits between the
      // payload and the signature and is skipped via the payload window boundary math.
      securityFooterSize = decoder.decodeUInt16().intValue();
    }

    return new SecurityHeader(securityFlags, securityTokenId, messageNonce, securityFooterSize);
  }

  /**
   * Process the message security of a signed (and possibly encrypted) NetworkMessage: resolve key
   * material, verify the trailing signature BEFORE any payload byte is parsed (§7.2.4.4.3.2: "shall
   * verify the signature before processing the payload"), and build the bounded payload window —
   * for SignAndEncrypt, a decrypted COPY of the payload region, because the transport buffer is
   * shared across mappings and must never be mutated.
   *
   * @param header the parsed SecurityHeader; at least SecurityFlags bit 0 or 1 is set.
   * @param dataSetWriterIds the plaintext PayloadHeader DataSetWriterIds, or {@code null}.
   * @return the payload window to parse, or {@code null} for a header-only skip; {@link #security}
   *     is set to the observed {@link ReceivedSecurity} either way.
   */
  private @Nullable ByteBuf processSecuredPayload(
      SecurityHeader header, @Nullable List<UShort> dataSetWriterIds) {

    boolean encrypted = (header.securityFlags() & 0x02) != 0;
    MessageSecurityMode mode =
        encrypted ? MessageSecurityMode.SignAndEncrypt : MessageSecurityMode.Sign;

    if (resolver == null) {
      security = new ReceivedSecurity(mode, header.securityTokenId(), SecurityOutcome.NO_RESOLVER);
      return null;
    }

    List<UShort> requestWriterIds;
    if (chunkDataSetWriterId != null) {
      requestWriterIds = List.of(chunkDataSetWriterId);
    } else if (dataSetWriterIds != null) {
      requestWriterIds = dataSetWriterIds;
    } else {
      requestWriterIds = List.of();
    }

    var request =
        new SecurityContextResolver.ResolveRequest(
            publisherId,
            writerGroupId,
            requestWriterIds,
            mode,
            header.securityTokenId(),
            (header.securityFlags() & 0x08) != 0);

    SecurityContextResolver.Resolution resolution = resolver.resolve(request);

    if (resolution instanceof SecurityContextResolver.Resolution.Refused refused) {
      security = new ReceivedSecurity(mode, header.securityTokenId(), refused.reason());
      return null;
    }

    SecurityKeyMaterial keys = ((SecurityContextResolver.Resolution.Keys) resolution).keys();

    int signatureSize = keys.getPolicy().getSignatureLength();
    int payloadEnd = buffer.writerIndex() - signatureSize - header.securityFooterSize();

    if (payloadEnd < buffer.readerIndex()) {
      // fewer bytes remain than the SecurityFooter and Signature the header promises: a
      // truncation signature, surfaced as a Bad_DecodingError failure like the other explicit
      // length overruns — and, because no truncation can verify, also an INVALID_SIGNATURE
      // security drop
      LOGGER.debug("secured NetworkMessage too short for SecurityFooter and Signature");
      failure =
          new DecodedNetworkMessage.Failure(
              new StatusCode(StatusCodes.Bad_DecodingError),
              "secured NetworkMessage too short for SecurityFooter and Signature",
              null);
      security =
          new ReceivedSecurity(mode, header.securityTokenId(), SecurityOutcome.INVALID_SIGNATURE);
      return null;
    }

    byte[] signature = new byte[signatureSize];
    buffer.getBytes(buffer.writerIndex() - signatureSize, signature);

    try {
      // constant-time comparison inside; the signed region is the entire NetworkMessage
      // (including the SecurityFooter) up to, but not including, the signature
      if (!UadpMessageSecurity.verify(
          keys, signature, buffer.nioBuffer(0, buffer.writerIndex() - signatureSize))) {

        LOGGER.debug("NetworkMessage signature verification failed");
        security =
            new ReceivedSecurity(mode, header.securityTokenId(), SecurityOutcome.INVALID_SIGNATURE);
        return null;
      }
    } catch (UaException e) {
      LOGGER.debug("NetworkMessage signature verification failed: {}", e.getMessage(), e);
      security =
          new ReceivedSecurity(mode, header.securityTokenId(), SecurityOutcome.INVALID_SIGNATURE);
      return null;
    }

    ByteBuf payload;
    if (encrypted) {
      if (header.messageNonce().length != keys.getPolicy().getMessageNonceLength()) {
        LOGGER.debug(
            "encrypted NetworkMessage with unusable NonceLength: {}", header.messageNonce().length);
        security =
            new ReceivedSecurity(mode, header.securityTokenId(), SecurityOutcome.DECRYPT_FAILED);
        return null;
      }

      // Decrypt a COPY: the underlying buffer is shared with every other mapping of the
      // connection (and with the dispatcher's accounting), so in-place decryption is forbidden.
      byte[] copy = new byte[payloadEnd - buffer.readerIndex()];
      buffer.getBytes(buffer.readerIndex(), copy);
      payload = Unpooled.wrappedBuffer(copy);

      try {
        UadpMessageSecurity.ctrTransform(keys, header.messageNonce(), payload);
      } catch (UaException e) {
        LOGGER.debug("NetworkMessage payload decryption failed: {}", e.getMessage(), e);
        security =
            new ReceivedSecurity(mode, header.securityTokenId(), SecurityOutcome.DECRYPT_FAILED);
        return null;
      }
    } else {
      // sign-only: bound the window so the SecurityFooter and Signature are never parsed as
      // payload (the no-PayloadHeader path and the key-frame heartbeat check both consume
      // "the rest of the buffer")
      payload = buffer.readSlice(payloadEnd - buffer.readerIndex());
    }

    security = new ReceivedSecurity(mode, header.securityTokenId(), SecurityOutcome.VERIFIED);
    return payload;
  }

  /**
   * Decode the chunk payload (Part 14 §7.2.4.4.4, Table 159) into {@link #decodedChunk}. The chunk
   * bytes are copied out of the payload window, which does not survive this decode call.
   */
  private void decodeChunkPayload(OpcUaBinaryDecoder decoder, ByteBuf buffer) {
    UShort messageSequenceNumber = decoder.decodeUInt16();
    UInteger chunkOffset = decoder.decodeUInt32();
    UInteger totalSize = decoder.decodeUInt32();

    // ChunkData is a ByteString: Int32 length + bytes; -1 encodes null
    int length = decoder.decodeInt32();
    if (length > buffer.readableBytes()) {
      // a truncation signature: the ChunkData length promised more bytes than arrived
      LOGGER.debug("ChunkData length exceeds remaining bytes: {}", length);
      failure =
          new DecodedNetworkMessage.Failure(
              new StatusCode(StatusCodes.Bad_DecodingError),
              "ChunkData length exceeds remaining bytes: " + length,
              null);
      return;
    }

    byte[] chunkData = new byte[Math.max(length, 0)];
    buffer.readBytes(chunkData);

    decodedChunk =
        new DecodedChunk(
            chunkDataSetWriterId, messageSequenceNumber, chunkOffset, totalSize, chunkData);
  }

  private void decodeDiscoveryAnnouncement(OpcUaBinaryDecoder decoder, ByteBuf buffer) {
    int announcementType = buffer.readUnsignedByte();

    // Per-PublisherId announcement SequenceNumber, independent of the data-plane counters.
    UShort announcementSequenceNumber = decoder.decodeUInt16();

    if (announcementType != ANNOUNCEMENT_DATA_SET_META_DATA) {
      LOGGER.debug("unsupported discovery announcement type: {}", announcementType);
      return;
    }

    UShort dataSetWriterId = decoder.decodeUInt16();

    var dataSetMetaData =
        (DataSetMetaDataType) decoder.decodeStruct("MetaData", DataSetMetaDataType.TYPE_ID);

    StatusCode status = decoder.decodeStatusCode();

    if (publisherId == null) {
      // Discovery messages shall carry a PublisherId (§7.2.4.6.3); without one the
      // announcement cannot be correlated with a Publisher.
      LOGGER.debug("DataSetMetaData announcement without PublisherId");
      return;
    }

    // Surfaced regardless of status; a Bad status is a denial the subscriber needs to see.
    announcement =
        UadpMetaDataAnnouncement.of(
            publisherId, announcementSequenceNumber, dataSetWriterId, dataSetMetaData, status);
  }

  private void decodeDiscoveryProbe(OpcUaBinaryDecoder decoder, ByteBuf buffer) {
    int probeType = buffer.readUnsignedByte();

    if (probeType != UadpDiscoveryProbe.PROBE_TYPE_PUBLISHER_INFORMATION) {
      // FindApplications probes (2) and reserved values are tolerated and ignored.
      LOGGER.debug("unsupported discovery probe type: {}", probeType);
      return;
    }

    int informationType = buffer.readUnsignedByte();

    if (informationType != UadpDiscoveryProbe.INFORMATION_TYPE_DATA_SET_META_DATA) {
      // Publisher endpoints and writer/group/connection configuration probes are tolerated
      // and ignored.
      LOGGER.debug("unsupported discovery probe InformationType: {}", informationType);
      return;
    }

    // DataSetWriter settings (Table 180): standard OPC UA Binary array of UInt16; a null
    // (-1) or empty array surfaces as an empty list.
    int count = decoder.decodeInt32();
    if (count > buffer.readableBytes() / 2) {
      // a truncation signature: the array count promised more bytes than arrived
      LOGGER.debug("DataSetWriterIds count exceeds remaining bytes: {}", count);
      failure =
          new DecodedNetworkMessage.Failure(
              new StatusCode(StatusCodes.Bad_DecodingError),
              "discovery probe DataSetWriterIds count exceeds remaining bytes: " + count,
              null);
      return;
    }

    List<UShort> dataSetWriterIds = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      dataSetWriterIds.add(decoder.decodeUInt16());
    }

    if (publisherId == null) {
      // The header PublisherId identifies the probed Publisher (§7.2.4.6.12.1); without one
      // the probe cannot be answered.
      LOGGER.debug("discovery probe without PublisherId");
      return;
    }

    probe = UadpDiscoveryProbe.of(publisherId, dataSetWriterIds);
  }

  private void decodeDataPayload(
      OpcUaBinaryDecoder decoder,
      ByteBuf buffer,
      boolean payloadHeaderEnabled,
      @Nullable List<UShort> dataSetWriterIds) {

    // Without a PayloadHeader the number and sizes of DataSetMessages can only come from
    // reader configuration; assume a single DataSetMessage spanning the rest of the buffer
    // (the payload window: secured messages are bounded away from the trailing footer and
    // signature bytes by the caller).
    int count = dataSetWriterIds != null ? dataSetWriterIds.size() : 1;

    int[] sizes = null;
    if (payloadHeaderEnabled && count > 1) {
      sizes = new int[count];
      for (int i = 0; i < count; i++) {
        sizes[i] = decoder.decodeUInt16().intValue();
      }
    }

    for (int i = 0; i < count; i++) {
      UShort dataSetWriterId = dataSetWriterIds != null ? dataSetWriterIds.get(i) : null;

      if (sizes != null) {
        int size = sizes[i];
        if (size > buffer.readableBytes()) {
          // a truncated NetworkMessage: the Sizes array promised more payload than arrived;
          // everything decoded before this point is still delivered
          LOGGER.debug("DataSetMessage size exceeds remaining bytes: {}", size);
          failure =
              new DecodedNetworkMessage.Failure(
                  new StatusCode(StatusCodes.Bad_DecodingError),
                  "DataSetMessage size exceeds remaining bytes: " + size,
                  null);
          return;
        }

        ByteBuf slice = buffer.readSlice(size);
        OpcUaBinaryDecoder sliceDecoder = new OpcUaBinaryDecoder(encodingContext).setBuffer(slice);

        try {
          messages.add(decodeDataSetMessage(sliceDecoder, slice, dataSetWriterId));
        } catch (Exception e) {
          LOGGER.debug("failed to decode DataSetMessage {}: {}", i, e.getMessage(), e);

          messages.add(invalidDataSetMessage(dataSetWriterId));
        }
      } else {
        // Not size-bounded; an exception propagates and ends decoding of this
        // NetworkMessage with whatever was decoded so far.
        messages.add(decodeDataSetMessage(decoder, buffer, dataSetWriterId));
      }
    }
  }

  private static DecodedDataSetMessage decodeDataSetMessage(
      OpcUaBinaryDecoder decoder, ByteBuf buffer, @Nullable UShort dataSetWriterId) {

    int flags1 = buffer.readUnsignedByte();

    boolean valid = (flags1 & 0x01) != 0;
    if (!valid) {
      // "If the bit is false the rest of this DataSetMessage shall not be processed."
      return invalidDataSetMessage(dataSetWriterId);
    }

    int fieldEncoding = (flags1 >> 1) & 0x03;

    int flags2 = (flags1 & 0x80) != 0 ? buffer.readUnsignedByte() : 0;
    int messageType = flags2 & 0x0F;

    DataSetMessageKind kind =
        switch (messageType) {
          case 0x00 -> DataSetMessageKind.KEY_FRAME;
          case 0x01 -> DataSetMessageKind.DELTA_FRAME;
          case 0x02 -> DataSetMessageKind.EVENT;
          case 0x03 -> DataSetMessageKind.KEEP_ALIVE;
          default -> null;
        };

    // the DecodedDataSetMessage slot is UInt32 (JSON mapping range); UADP carries UInt16 values
    UInteger sequenceNumber = (flags1 & 0x08) != 0 ? uint(decoder.decodeUInt16().intValue()) : null;
    DateTime timestamp = (flags2 & 0x10) != 0 ? decoder.decodeDateTime() : null;
    if ((flags2 & 0x20) != 0) {
      // Consume; DataSetMessage PicoSeconds are not surfaced.
      decoder.decodeUInt16();
    }

    StatusCode status = null;
    if ((flags1 & 0x10) != 0) {
      // The header Status is the high-order 16 bits of the StatusCode.
      status = new StatusCode(decoder.decodeUInt16().longValue() << 16);
    }

    UInteger majorVersion = (flags1 & 0x20) != 0 ? decoder.decodeUInt32() : null;
    UInteger minorVersion = (flags1 & 0x40) != 0 ? decoder.decodeUInt32() : null;

    ConfigurationVersionDataType configurationVersion = null;
    if (majorVersion != null || minorVersion != null) {
      configurationVersion =
          new ConfigurationVersionDataType(
              majorVersion != null ? majorVersion : uint(0),
              minorVersion != null ? minorVersion : uint(0));
    }

    if (kind == null || fieldEncoding == FIELD_ENCODING_RESERVED) {
      LOGGER.debug(
          "reserved DataSetMessage type or field encoding: type={}, fieldEncoding={}",
          messageType,
          fieldEncoding);

      return new DecodedDataSetMessage(
          dataSetWriterId,
          DataSetMessageKind.KEY_FRAME,
          false,
          sequenceNumber,
          timestamp,
          status,
          configurationVersion,
          List.of());
    }

    if (kind == DataSetMessageKind.KEEP_ALIVE) {
      return new DecodedDataSetMessage(
          dataSetWriterId,
          kind,
          true,
          sequenceNumber,
          timestamp,
          status,
          configurationVersion,
          List.of());
    }

    if (fieldEncoding == FIELD_ENCODING_RAW_DATA) {
      // RawData decoding requires metadata-driven offsets; skip this DataSetMessage.
      LOGGER.debug("RawData field encoding is not supported; skipping DataSetMessage");

      return new DecodedDataSetMessage(
          dataSetWriterId,
          kind,
          false,
          sequenceNumber,
          timestamp,
          status,
          configurationVersion,
          List.of());
    }

    List<DecodedField> fields = new ArrayList<>();

    if (kind == DataSetMessageKind.DELTA_FRAME) {
      int fieldCount = decoder.decodeUInt16().intValue();

      for (int i = 0; i < fieldCount; i++) {
        int fieldIndex = decoder.decodeUInt16().intValue();
        fields.add(new DecodedField(fieldIndex, decodeFieldValue(decoder, fieldEncoding)));
      }
    } else if (buffer.readableBytes() > 0) {
      // Key frame or event. A key frame with no body at all is a heartbeat.
      int fieldCount = decoder.decodeUInt16().intValue();

      for (int i = 0; i < fieldCount; i++) {
        fields.add(new DecodedField(i, decodeFieldValue(decoder, fieldEncoding)));
      }
    }

    return new DecodedDataSetMessage(
        dataSetWriterId,
        kind,
        true,
        sequenceNumber,
        timestamp,
        status,
        configurationVersion,
        fields);
  }

  /**
   * Decode one field value, reversing the status propagation rules of Part 14 Table 34 for the
   * Variant field encoding: a Variant containing a Bad StatusCode is the field status, and a
   * Variant containing a DataValue carries an Uncertain value and status.
   */
  private static DataValue decodeFieldValue(OpcUaBinaryDecoder decoder, int fieldEncoding) {
    if (fieldEncoding == FIELD_ENCODING_DATA_VALUE) {
      return decoder.decodeDataValue();
    } else {
      Variant variant = decoder.decodeVariant();
      Object value = variant.value();

      if (value instanceof StatusCode statusCode && statusCode.isBad()) {
        return new DataValue(Variant.NULL_VALUE, statusCode, null, null, null, null);
      } else if (value instanceof DataValue dataValue) {
        return dataValue;
      } else {
        return new DataValue(variant, StatusCode.GOOD, null, null, null, null);
      }
    }
  }

  private static DecodedDataSetMessage invalidDataSetMessage(@Nullable UShort dataSetWriterId) {
    return new DecodedDataSetMessage(
        dataSetWriterId, DataSetMessageKind.KEY_FRAME, false, null, null, null, null, List.of());
  }
}
