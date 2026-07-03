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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.security.UadpMessageSecurity;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;
import org.jspecify.annotations.Nullable;

/**
 * Encodes one UADP NetworkMessage (OPC UA Part 14 §7.2.4) from an {@link EncodeContext}.
 *
 * <p>Scope: Data Key Frame, Data Delta Frame, and Keep Alive DataSetMessages with Variant or
 * DataValue field encoding, security modes None, Sign, and SignAndEncrypt. Event message emission,
 * the RawData field encoding, PromotedFields, and chunk emission are not supported and are rejected
 * with {@code Bad_NotSupported}. (The blanket RawData rejection also covers §7.2.4.5.11's "RawField
 * encoding shall only be applied to Data Key Frame DataSetMessages".) The RawData and
 * PromotedFields rejections are backstops: for the built-in mapping, service validation and the
 * group- and writer-level activation re-checks reject those configurations with the same status
 * code before a publish cycle ever runs, whatever the enablement order, so the encode-time throws
 * are reachable only via direct invocation.
 *
 * <p>Message security (§7.2.4.4.3) is applied only when the {@link EncodeContext} carries a
 * non-null {@link MessageSecurityContext} — a null context is security mode None and produces
 * today's wire bytes unchanged. When present: ExtendedFlags1 bit 4 is set and a SecurityHeader is
 * written (sign-only also carries the real SecurityTokenId and the 8-byte MessageNonce, the Annex
 * A.2.1.5 form; a SecurityFooter is never emitted), the payload region is AES-CTR encrypted in
 * place for SignAndEncrypt, and the whole NetworkMessage is HMAC-signed with the 32-byte signature
 * appended. A secured group configuration without a resolved context (or vice versa) is rejected
 * with {@code Bad_ConfigurationError}: the engine resolves security before calling, and the codec
 * never invents or silently drops it.
 *
 * <p>Delta frames signal their type in the DataSetFlags2 type bits ({@code 0001}, §7.2.4.5.4 Table
 * 162) and write the Table 164 body: {@code FieldCount} followed by {@code FieldCount} pairs of
 * {@code FieldIndex} (UInt16) and the field value in the same Variant/DataValue field encoding as
 * key frames. A delta frame draft combined with a non-zero ConfiguredSize is rejected with {@code
 * Bad_ConfigurationError}: the fixed-size layout is key-frame-only (Annex A.2.1.7), and an
 * overflowing delta would otherwise be valid-bit cleared (§6.3.1.3.3) yet still sent — silently
 * losing the changed values without any transmission failure the publisher could observe.
 *
 * <p>Stateless: sequence numbers, GroupVersion, and timestamps are supplied by the caller via the
 * {@link EncodeContext}.
 */
final class UadpNetworkMessageEncoder {

  private static final int UADP_VERSION = 1;

  private UadpNetworkMessageEncoder() {}

  /**
   * Encode one NetworkMessage from the draft DataSetMessages in {@code context}.
   *
   * @param context the encode context.
   * @return the encoded NetworkMessage; the caller assumes ownership of its buffer.
   * @throws UaException if the message could not be encoded; {@code Bad_NotSupported} if the
   *     configuration requires a feature that is out of scope (RawData field encoding,
   *     PromotedFields, event message emission), {@code Bad_ConfigurationError} if the group or a
   *     writer does not carry UADP message settings, the group's configured security mode disagrees
   *     with the context's {@link MessageSecurityContext} (a secured mode requires a matching
   *     non-null context, mode None or Invalid requires a null one), or a delta frame draft carries
   *     a non-zero ConfiguredSize (fixed-size layouts are key-frame-only, Part 14 Annex A.2.1.7),
   *     {@code Bad_EncodingLimitsExceeded} if a draft's sequence number would be transmitted but
   *     exceeds the UInt16 wire range (Part 14 Table 162) or a count/size limit of the wire format
   *     is exceeded.
   */
  static EncodedNetworkMessage encode(EncodeContext context) throws UaException {
    if (context.messages().isEmpty()) {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "no DataSetMessages to encode");
    }

    if (!(context.writerGroup().getMessageSettings()
        instanceof UadpWriterGroupSettings groupSettings)) {

      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "WriterGroup \""
              + context.writerGroup().getName()
              + "\" does not have UadpWriterGroupSettings");
    }

    UadpNetworkMessageContentMask networkMask = groupSettings.getNetworkMessageContentMask();

    if (networkMask.getPromotedFields()) {
      throw new UaException(
          StatusCodes.Bad_NotSupported, "PromotedFields emission is not supported");
    }

    // Backstop: the engine resolves the MessageSecurityContext from the group's configured mode
    // before calling; the codec never invents security and never silently drops it. Group-level
    // mode Invalid is treated as None, consistent with the startup/reconfigure gates.
    MessageSecurityConfig securityConfig = context.writerGroup().getMessageSecurity();
    MessageSecurityMode configuredMode =
        securityConfig != null ? securityConfig.getMode() : MessageSecurityMode.None;
    boolean configuredSecured =
        configuredMode == MessageSecurityMode.Sign
            || configuredMode == MessageSecurityMode.SignAndEncrypt;
    MessageSecurityContext security = context.security();

    if (configuredSecured != (security != null)
        || (security != null && security.mode() != configuredMode)) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "configured security mode %s does not match the resolved MessageSecurityContext (%s)"
              .formatted(configuredMode, security != null ? security.mode() : "null"));
    }

    for (DataSetMessageDraft draft : context.messages()) {
      DataSetWriterConfig writer = draft.writer();

      if (!(writer.getSettings() instanceof UadpDataSetWriterSettings writerSettings)) {
        throw new UaException(
            StatusCodes.Bad_ConfigurationError,
            "DataSetWriter \"" + writer.getName() + "\" does not have UadpDataSetWriterSettings");
      }
      if (writer.getFieldContentMask().getRawData()) {
        throw new UaException(
            StatusCodes.Bad_NotSupported,
            "RawData field encoding is not supported (DataSetWriter \"" + writer.getName() + "\")");
      }
      if (draft.kind() == DataSetMessageKind.EVENT) {
        throw new UaException(
            StatusCodes.Bad_NotSupported,
            "Event DataSetMessage emission is not supported (DataSetWriter \""
                + writer.getName()
                + "\")");
      }
      if (writerSettings.getDataSetMessageContentMask().getSequenceNumber()
          && draft.sequenceNumber().longValue() > 0xFFFFL) {
        // The DataSetMessageSequenceNumber is UInt16 on the wire (Part 14 §7.2.4.5.4 Table
        // 162). The engine's UADP sequence counter wraps at 2^16 — keep-alive drafts peek the
        // same counter, so they stay in range by construction — making this unreachable from a
        // publish cycle; it guards externally constructed drafts, whose out-of-range value
        // would otherwise be silently truncated on the wire.
        throw new UaException(
            StatusCodes.Bad_EncodingLimitsExceeded,
            "DataSetMessage sequence number exceeds the UInt16 wire range (Part 14 Table 162): "
                + draft.sequenceNumber()
                + " (DataSetWriter \""
                + writer.getName()
                + "\")");
      }
      if (draft.kind() == DataSetMessageKind.DELTA_FRAME
          && writerSettings.getConfiguredSize().intValue() > 0) {
        // Annex A.2.1.7: the fixed-size layout is key-frame-only. A delta exceeding the
        // ConfiguredSize would be valid-bit cleared (§6.3.1.3.3) yet still sent, silently
        // losing the changed values; the engine never produces this combination (validation
        // plus every-cycle key-frame degradation), so this guards external mapping users.
        throw new UaException(
            StatusCodes.Bad_ConfigurationError,
            "delta frame DataSetMessage with a non-zero ConfiguredSize is not supported:"
                + " fixed-size layouts are key-frame-only (Part 14 A.2.1.7) (DataSetWriter \""
                + writer.getName()
                + "\")");
      }
    }

    boolean payloadHeaderEnabled = networkMask.getPayloadHeader();
    int count = context.messages().size();

    if (payloadHeaderEnabled && count > 255) {
      throw new UaException(
          StatusCodes.Bad_EncodingLimitsExceeded, "too many DataSetMessages: " + count);
    }

    List<ByteBuf> bodies = new ArrayList<>(count);
    ByteBuf buffer = BufferUtil.pooledBuffer();
    boolean success = false;

    try {
      for (DataSetMessageDraft draft : context.messages()) {
        bodies.add(encodeDataSetMessage(context.encodingContext(), draft));
      }

      OpcUaBinaryEncoder encoder =
          new OpcUaBinaryEncoder(context.encodingContext()).setBuffer(buffer);

      byte[] messageNonce = encodeNetworkMessageHeader(context, networkMask, encoder, buffer);

      // The encrypted region (Part 14 §7.2.4.5.3, Table 161) starts here: Sizes + bodies. The
      // Sizes stay valid across encryption because AES-CTR is length-preserving.
      int payloadStart = buffer.writerIndex();

      // Sizes: present iff the PayloadHeader is enabled and there is more than one
      // DataSetMessage (Part 14 §7.2.4.5.3, Table 161).
      if (payloadHeaderEnabled && count > 1) {
        for (ByteBuf body : bodies) {
          int size = body.readableBytes();
          if (size > 65535) {
            throw new UaException(
                StatusCodes.Bad_EncodingLimitsExceeded,
                "DataSetMessage size exceeds 65535 bytes: " + size);
          }
          encoder.encodeUInt16(ushort(size));
        }
      }

      for (ByteBuf body : bodies) {
        buffer.writeBytes(body);
      }

      if (security != null && messageNonce != null) {
        if (security.mode() == MessageSecurityMode.SignAndEncrypt) {
          // Encrypt first: the payload region is transformed in place — legal here because the
          // encoder owns the buffer exclusively until ownership transfers below (slices share
          // memory, so the transform lands in this buffer).
          UadpMessageSecurity.ctrTransform(
              security.keys(),
              messageNonce,
              buffer.slice(payloadStart, buffer.writerIndex() - payloadStart));
        }

        // Sign after encrypting, over the entire NetworkMessage including any encrypted data
        // (§7.2.4.4.3.2); the signature itself is appended after the signed region.
        byte[] signature =
            UadpMessageSecurity.sign(security.keys(), buffer.nioBuffer(0, buffer.writerIndex()));
        buffer.writeBytes(signature);
      }

      success = true;

      return new EncodedNetworkMessage(buffer);
    } catch (UaSerializationException e) {
      throw new UaException(e.getStatusCode().value(), e.getMessage(), e);
    } finally {
      bodies.forEach(ByteBuf::release);
      if (!success) {
        buffer.release();
      }
    }
  }

  /**
   * Write the NetworkMessage header, including the SecurityHeader when the context carries a {@link
   * MessageSecurityContext}.
   *
   * @return the 8-byte MessageNonce written to the SecurityHeader — obtained from exactly one
   *     {@link MessageNonceSupplier#nextNonce()} call, it also seeds the AES-CTR counter block — or
   *     {@code null} when the message is not secured.
   * @throws UaException if the nonce supplier fails or returns a nonce of the wrong length.
   */
  private static byte @Nullable [] encodeNetworkMessageHeader(
      EncodeContext context,
      UadpNetworkMessageContentMask mask,
      OpcUaBinaryEncoder encoder,
      ByteBuf buffer)
      throws UaException {

    boolean publisherIdEnabled = mask.getPublisherId();
    boolean groupHeaderEnabled = mask.getGroupHeader();
    boolean payloadHeaderEnabled = mask.getPayloadHeader();
    boolean timestampEnabled = mask.getTimestamp();
    boolean picoSecondsEnabled = timestampEnabled && mask.getPicoSeconds();
    boolean dataSetClassIdEnabled = mask.getDataSetClassId();

    MessageSecurityContext security = context.security();

    int extendedFlags1 = 0;
    if (publisherIdEnabled) {
      extendedFlags1 |= publisherIdTypeBits(context.publisherId());
    }
    if (dataSetClassIdEnabled) {
      extendedFlags1 |= 0x08;
    }
    if (security != null) {
      // SecurityHeader enabled: "If the SecurityMode in the configuration is SIGN or
      // SIGNANDENCRYPT, this flag shall be set" (Table 154).
      extendedFlags1 |= 0x10;
    }
    if (timestampEnabled) {
      extendedFlags1 |= 0x20;
    }
    if (picoSecondsEnabled) {
      extendedFlags1 |= 0x40;
    }

    int byte0 = UADP_VERSION;
    if (publisherIdEnabled) {
      byte0 |= 0x10;
    }
    if (groupHeaderEnabled) {
      byte0 |= 0x20;
    }
    if (payloadHeaderEnabled) {
      byte0 |= 0x40;
    }
    if (extendedFlags1 != 0) {
      byte0 |= 0x80;
    }

    buffer.writeByte(byte0);
    if (extendedFlags1 != 0) {
      buffer.writeByte(extendedFlags1);
    }

    if (publisherIdEnabled) {
      encodePublisherId(encoder, context.publisherId());
    }

    if (dataSetClassIdEnabled) {
      // The EncodeContext does not carry a DataSetClassId; encode the null Guid.
      encoder.encodeGuid(new UUID(0L, 0L));
    }

    if (groupHeaderEnabled) {
      int groupFlags = 0;
      if (mask.getWriterGroupId()) {
        groupFlags |= 0x01;
      }
      if (mask.getGroupVersion()) {
        groupFlags |= 0x02;
      }
      if (mask.getNetworkMessageNumber()) {
        groupFlags |= 0x04;
      }
      if (mask.getSequenceNumber()) {
        groupFlags |= 0x08;
      }

      buffer.writeByte(groupFlags);

      if ((groupFlags & 0x01) != 0) {
        encoder.encodeUInt16(context.writerGroup().getWriterGroupId());
      }
      if ((groupFlags & 0x02) != 0) {
        encoder.encodeUInt32(context.groupVersion());
      }
      if ((groupFlags & 0x04) != 0) {
        encoder.encodeUInt16(context.networkMessageNumber());
      }
      if ((groupFlags & 0x08) != 0) {
        encoder.encodeUInt16(context.networkMessageSequenceNumber());
      }
    }

    if (payloadHeaderEnabled) {
      buffer.writeByte(context.messages().size());
      for (DataSetMessageDraft draft : context.messages()) {
        encoder.encodeUInt16(draft.writer().getDataSetWriterId());
      }
    }

    if (timestampEnabled) {
      DateTime timestamp = context.timestamp();
      encoder.encodeDateTime(timestamp != null ? timestamp : DateTime.now());
    }
    if (picoSecondsEnabled) {
      // No sub-100ns time source is available; encode 0.
      encoder.encodeUInt16(ushort(0));
    }

    // SecurityHeader (Table 154 slot: after PicoSeconds; PromotedFields are rejected and the
    // ActionHeader is unsupported).
    if (security != null) {
      return encodeSecurityHeader(security, encoder, buffer);
    }

    return null;
  }

  /**
   * Write the SecurityHeader: SecurityFlags, SecurityTokenId, NonceLength, and the MessageNonce.
   *
   * <p>Sign-only also emits the real SecurityTokenId and the 8-byte MessageNonce (the Annex A.2.1.5
   * form). The SecurityFooter flag is never set, so no SecurityFooterSize is written; the
   * force-key-reset bit (bit 3) is not emitted in this version — its publisher-side emission is
   * deferred with the key-invalidation path.
   *
   * @return the MessageNonce written, from exactly one {@link MessageNonceSupplier#nextNonce()}
   *     call.
   * @throws UaException if the nonce supplier fails; {@code Bad_InternalError} if it returns a
   *     nonce whose length does not match the policy's MessageNonce length.
   */
  private static byte[] encodeSecurityHeader(
      MessageSecurityContext security, OpcUaBinaryEncoder encoder, ByteBuf buffer)
      throws UaException {

    int securityFlags = security.mode() == MessageSecurityMode.SignAndEncrypt ? 0x03 : 0x01;
    buffer.writeByte(securityFlags);

    encoder.encodeUInt32(security.securityTokenId());

    byte[] messageNonce = security.nonceSupplier().nextNonce();
    int nonceLength = security.keys().getPolicy().getMessageNonceLength();
    if (messageNonce.length != nonceLength) {
      throw new UaException(
          StatusCodes.Bad_InternalError,
          "MessageNonce must be %d bytes, got %d".formatted(nonceLength, messageNonce.length));
    }

    buffer.writeByte(messageNonce.length);
    buffer.writeBytes(messageNonce);

    return messageNonce;
  }

  /** PublisherId type bits for ExtendedFlags1 bits 0-2; also used by the discovery encoder. */
  static int publisherIdTypeBits(PublisherId publisherId) {
    if (publisherId instanceof PublisherId.ByteId) {
      return 0x00;
    } else if (publisherId instanceof PublisherId.UInt16Id) {
      return 0x01;
    } else if (publisherId instanceof PublisherId.UInt32Id) {
      return 0x02;
    } else if (publisherId instanceof PublisherId.UInt64Id) {
      return 0x03;
    } else {
      return 0x04;
    }
  }

  /** Encode the PublisherId in its wire representation; also used by the discovery encoder. */
  static void encodePublisherId(OpcUaBinaryEncoder encoder, PublisherId publisherId) {
    if (publisherId instanceof PublisherId.ByteId id) {
      encoder.encodeByte(id.value());
    } else if (publisherId instanceof PublisherId.UInt16Id id) {
      encoder.encodeUInt16(id.value());
    } else if (publisherId instanceof PublisherId.UInt32Id id) {
      encoder.encodeUInt32(id.value());
    } else if (publisherId instanceof PublisherId.UInt64Id id) {
      encoder.encodeUInt64(id.value());
    } else if (publisherId instanceof PublisherId.StringId id) {
      encoder.encodeString(id.value());
    }
  }

  private static ByteBuf encodeDataSetMessage(
      EncodingContext encodingContext, DataSetMessageDraft draft) throws UaException {

    UadpDataSetWriterSettings settings = (UadpDataSetWriterSettings) draft.writer().getSettings();
    int configuredSize = settings.getConfiguredSize().intValue();

    ByteBuf buffer = BufferUtil.pooledBuffer();
    boolean success = false;

    try {
      writeDataSetMessage(encodingContext, draft, settings, buffer, true);

      if (configuredSize > 0) {
        if (buffer.readableBytes() < configuredSize) {
          // Pad with zero bytes up to ConfiguredSize (Part 14 §6.3.1.3.3).
          buffer.writeZero(configuredSize - buffer.readableBytes());
        } else if (buffer.readableBytes() > configuredSize) {
          // Too large for the fixed size: send the header only, with the "message is
          // valid" bit clear (Part 14 §6.3.1.3.3). Only key frames and keep-alives can
          // reach this branch (delta drafts with a ConfiguredSize are rejected up front);
          // a valid-cleared key frame self-heals on the next publish cycle.
          buffer.clear();
          writeDataSetMessage(encodingContext, draft, settings, buffer, false);

          if (buffer.readableBytes() > configuredSize) {
            throw new UaException(
                StatusCodes.Bad_EncodingLimitsExceeded,
                "DataSetMessage header exceeds ConfiguredSize ("
                    + configuredSize
                    + ") for DataSetWriter \""
                    + draft.writer().getName()
                    + "\"");
          }
          buffer.writeZero(configuredSize - buffer.readableBytes());
        }
      }

      success = true;

      return buffer;
    } finally {
      if (!success) {
        buffer.release();
      }
    }
  }

  private static void writeDataSetMessage(
      EncodingContext encodingContext,
      DataSetMessageDraft draft,
      UadpDataSetWriterSettings settings,
      ByteBuf buffer,
      boolean valid) {

    UadpDataSetMessageContentMask mask = settings.getDataSetMessageContentMask();
    DataSetFieldContentMask fieldMask = draft.writer().getFieldContentMask();
    boolean dataValueEncoding = isDataValueEncoding(fieldMask);

    boolean timestampEnabled = mask.getTimestamp();
    boolean picoSecondsEnabled = timestampEnabled && mask.getPicoSeconds();

    // DataSetFlags2 type bits, §7.2.4.5.4 Table 162; EVENT is rejected by encode() and listed
    // here only for exhaustiveness
    int flags2 =
        switch (draft.kind()) {
          case KEY_FRAME -> 0x00;
          case DELTA_FRAME -> 0x01;
          case EVENT -> 0x02;
          case KEEP_ALIVE -> 0x03;
        };
    if (timestampEnabled) {
      flags2 |= 0x10;
    }
    if (picoSecondsEnabled) {
      flags2 |= 0x20;
    }

    int flags1 = valid ? 0x01 : 0x00;
    if (dataValueEncoding) {
      // Field Encoding "10" = DataValue, at bits 1-2.
      flags1 |= 0x04;
    }
    if (mask.getSequenceNumber()) {
      flags1 |= 0x08;
    }
    if (mask.getStatus()) {
      flags1 |= 0x10;
    }
    if (mask.getMajorVersion()) {
      flags1 |= 0x20;
    }
    if (mask.getMinorVersion()) {
      flags1 |= 0x40;
    }
    if (flags2 != 0) {
      flags1 |= 0x80;
    }

    OpcUaBinaryEncoder encoder = new OpcUaBinaryEncoder(encodingContext).setBuffer(buffer);

    buffer.writeByte(flags1);
    if (flags2 != 0) {
      buffer.writeByte(flags2);
    }

    if (mask.getSequenceNumber()) {
      // UInt16 on the wire (Table 162): encode() rejected out-of-range drafts up front, and the
      // engine's UADP sequence counter wraps at 2^16, so the widened slot's value always fits
      encoder.encodeUInt16(ushort(draft.sequenceNumber().intValue()));
    }
    if (timestampEnabled) {
      DateTime timestamp = draft.timestamp();
      encoder.encodeDateTime(timestamp != null ? timestamp : DateTime.now());
    }
    if (picoSecondsEnabled) {
      encoder.encodeUInt16(ushort(0));
    }
    if (mask.getStatus()) {
      StatusCode status = draft.status();
      long statusValue = status != null ? status.value() : 0L;
      // The header Status is the high-order 16 bits of the StatusCode.
      encoder.encodeUInt16(ushort((int) ((statusValue >>> 16) & 0xFFFFL)));
    }
    if (mask.getMajorVersion()) {
      encoder.encodeUInt32(draft.configurationVersion().getMajorVersion());
    }
    if (mask.getMinorVersion()) {
      encoder.encodeUInt32(draft.configurationVersion().getMinorVersion());
    }

    if (valid && draft.kind() == DataSetMessageKind.DELTA_FRAME) {
      // Data Delta Frame body, §7.2.4.5.6 Table 164: FieldCount, then per changed field the
      // explicit FieldIndex (the field's position in the DataSetMetaData for the message's
      // ConfigurationVersion, each used at most once) and the field value in the same
      // Variant/DataValue field encoding as key frames
      List<DataSetMessageDraft.DeltaField> deltaFields = draft.deltaFields();

      if (deltaFields.size() > 65535) {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingLimitsExceeded, "too many fields: " + deltaFields.size());
      }

      encoder.encodeUInt16(ushort(deltaFields.size()));

      for (DataSetMessageDraft.DeltaField field : deltaFields) {
        int index = field.index();
        if (index < 0 || index > 65535) {
          throw new UaSerializationException(
              StatusCodes.Bad_EncodingLimitsExceeded, "FieldIndex exceeds UInt16 range: " + index);
        }
        encoder.encodeUInt16(ushort(index));

        if (dataValueEncoding) {
          encoder.encodeDataValue(filterDataValue(field.value(), fieldMask));
        } else {
          encodeVariantField(encoder, field.value());
        }
      }
    } else if (valid && !draft.keepAlive()) {
      List<DataValue> fields = draft.fields();

      if (fields.size() > 65535) {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingLimitsExceeded, "too many fields: " + fields.size());
      }

      encoder.encodeUInt16(ushort(fields.size()));

      for (DataValue field : fields) {
        if (dataValueEncoding) {
          encoder.encodeDataValue(filterDataValue(field, fieldMask));
        } else {
          encodeVariantField(encoder, field);
        }
      }
    }
  }

  private static boolean isDataValueEncoding(DataSetFieldContentMask fieldMask) {
    return fieldMask.getStatusCode()
        || fieldMask.getSourceTimestamp()
        || fieldMask.getServerTimestamp()
        || fieldMask.getSourcePicoSeconds()
        || fieldMask.getServerPicoSeconds();
  }

  /**
   * Build the DataValue actually encoded on the wire: only the members selected by the
   * DataSetFieldContentMask are retained (Part 14 §6.2.4.2).
   */
  private static DataValue filterDataValue(DataValue value, DataSetFieldContentMask mask) {
    boolean sourceTimestamp = mask.getSourceTimestamp();
    boolean serverTimestamp = mask.getServerTimestamp();

    return new DataValue(
        value.value(),
        mask.getStatusCode() ? value.statusCode() : StatusCode.GOOD,
        sourceTimestamp ? value.sourceTime() : null,
        sourceTimestamp && mask.getSourcePicoSeconds() ? value.sourcePicoseconds() : null,
        serverTimestamp ? value.serverTime() : null,
        serverTimestamp && mask.getServerPicoSeconds() ? value.serverPicoseconds() : null);
  }

  /**
   * Encode one field with the Variant field encoding, applying the status propagation rules of Part
   * 14 Table 34: Good fields encode the value, Uncertain fields encode a DataValue carrying value
   * and status, Bad fields encode the StatusCode itself.
   */
  private static void encodeVariantField(OpcUaBinaryEncoder encoder, DataValue value) {
    StatusCode status = value.statusCode();

    if (status.isBad()) {
      encoder.encodeVariant(Variant.ofStatusCode(status));
    } else if (status.isUncertain()) {
      DataValue uncertain = new DataValue(value.value(), status, null, null, null, null);
      encoder.encodeVariant(Variant.of(uncertain));
    } else {
      encoder.encodeVariant(value.value());
    }
  }
}
