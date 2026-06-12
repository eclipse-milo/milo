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

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * The built-in {@link MessageMappingProvider} for the UADP message mapping (OPC UA Part 14 §7.2.4),
 * mapping name {@code "uadp"}.
 *
 * <p>The codec is stateless; header values that evolve over time (sequence numbers, GroupVersion,
 * timestamps) are supplied by the caller via the {@link EncodeContext}.
 *
 * <p><b>Encoding</b> produces a single NetworkMessage per call containing all draft
 * DataSetMessages, as Data Key Frame or Keep Alive messages. The {@link UadpWriterGroupSettings} of
 * the WriterGroup decide which NetworkMessage header fields are emitted, and each writer's {@link
 * UadpDataSetWriterSettings} and {@link DataSetFieldContentMask} decide the DataSetMessage header
 * fields and the field representation (Variant or DataValue). The Sizes array is emitted iff the
 * PayloadHeader is enabled and there is more than one DataSetMessage. Out of scope and rejected
 * with {@code Bad_NotSupported}: the RawData field encoding, PromotedFields emission, and any
 * message security mode other than None.
 *
 * <p><b>Decoding</b> is tolerant and never throws on malformed or unsupported input: whatever was
 * decoded successfully is returned, possibly an empty {@link DecodedNetworkMessage}. Data Key
 * Frame, Data Delta Frame, Event, and Keep Alive DataSetMessages and discovery DataSetMetaData
 * announcements are decoded; DataSetMessages that cannot be decoded (RawData field encoding,
 * reserved types, the valid bit clear on the wire) are returned with {@code valid == false} and no
 * fields. NetworkMessages that are chunked, carry an ActionHeader, or indicate any security other
 * than None have their payloads skipped. Truncated or malformed input and chunked NetworkMessages
 * additionally report a {@link DecodedNetworkMessage#failure()} alongside whatever was decoded
 * before the failure point.
 *
 * <p><b>Discovery</b> (OPC UA Part 14 §7.2.4.6) is UADP-internal and not part of the {@link
 * MessageMappingProvider} SPI. {@link #decodeMessage(DecodeContext, ByteBuf)} surfaces
 * DataSetMetaData probes and announcements (including Bad-status denials) as a sealed {@link
 * UadpDecodedMessage}; {@link #encodeProbe(EncodingContext, UadpDiscoveryProbe)} and {@link
 * #encodeAnnouncement(EncodingContext, UadpMetaDataAnnouncement)} encode the corresponding
 * discovery NetworkMessages, security mode None only.
 */
public final class UadpMessageMapping implements MessageMappingProvider {

  /** The name of the UADP message mapping: {@code "uadp"}. */
  public static final String MAPPING_NAME = "uadp";

  @Override
  public String mappingName() {
    return MAPPING_NAME;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The UADP mapping combines all drafts into a single NetworkMessage and always returns a
   * singleton list.
   */
  @Override
  public List<EncodedNetworkMessage> encode(EncodeContext context) throws UaException {
    EncodedNetworkMessage encoded = UadpNetworkMessageEncoder.encode(context);

    var writers = new ArrayList<EncodedNetworkMessage.Writer>(context.messages().size());
    for (DataSetMessageDraft draft : context.messages()) {
      writers.add(
          new EncodedNetworkMessage.Writer(
              draft.writer().getName(), draft.writer().getDataSetWriterId()));
    }

    return List.of(new EncodedNetworkMessage(encoded.data(), writers));
  }

  /**
   * {@inheritDoc}
   *
   * <p>The UADP mapping encodes a DataSetMetaData discovery announcement (Part 14 §7.2.4.6.4) with
   * status {@code Good}, exactly as produced by {@link #encodeAnnouncement(EncodingContext,
   * UadpMetaDataAnnouncement)}.
   */
  @Override
  public EncodedNetworkMessage encodeMetaData(
      MetaDataEncodeContext context, DataSetMetaDataType metaData, UShort sequenceNumber)
      throws UaException {

    var announcement =
        UadpMetaDataAnnouncement.of(
            context.publisherId(),
            sequenceNumber,
            context.writer().getDataSetWriterId(),
            metaData,
            StatusCode.GOOD);

    EncodedNetworkMessage encoded =
        UadpDiscoveryMessageEncoder.encodeAnnouncement(context.encodingContext(), announcement);

    return new EncodedNetworkMessage(
        encoded.data(),
        List.of(
            new EncodedNetworkMessage.Writer(
                context.writer().getName(), context.writer().getDataSetWriterId())));
  }

  /**
   * {@inheritDoc}
   *
   * <p>Discovery content is folded into the data-plane shape: a DataSetMetaData announcement with a
   * non-Bad status surfaces in {@link DecodedNetworkMessage#metaData()}, while probes and
   * Bad-status announcements yield a header-only result. Use {@link #decodeMessage(DecodeContext,
   * ByteBuf)} to surface them.
   */
  @Override
  public DecodedNetworkMessage decode(DecodeContext context, ByteBuf payload) {
    return UadpNetworkMessageDecoder.decode(context, payload);
  }

  /**
   * Decode one NetworkMessage from {@code payload}, surfacing discovery messages.
   *
   * <p>Like {@link #decode(DecodeContext, ByteBuf)} this never throws: malformed or unsupported
   * input — including discovery content that is tolerated but not surfaced (FindApplications
   * probes, probe InformationTypes other than DataSetMetaData, announcement types other than
   * DataSetMetaData) — yields a possibly partial or empty {@link DecodedNetworkMessage}. Truncated
   * or malformed input and chunked NetworkMessages — including chunked discovery messages, whose
   * chunk flag is checked before the message type ({@code Bad_NotSupported}) — report a {@link
   * DecodedNetworkMessage#failure()} alongside the partial result.
   *
   * <p>The payload buffer is only valid for the duration of the call; the caller retains ownership
   * and releases it afterwards.
   *
   * @param context the decode context.
   * @param payload the buffer containing the received NetworkMessage.
   * @return a {@link UadpDiscoveryProbe} for a DataSetMetaData probe, a {@link
   *     UadpMetaDataAnnouncement} for a DataSetMetaData announcement of any status (a Bad status is
   *     a denial), or a {@link DecodedNetworkMessage} otherwise.
   */
  public UadpDecodedMessage decodeMessage(DecodeContext context, ByteBuf payload) {
    return UadpNetworkMessageDecoder.decodeMessage(context, payload);
  }

  /**
   * Encode one discovery probe NetworkMessage (Part 14 §7.2.4.6.12).
   *
   * <p>The NetworkMessage header PublisherId is the probe's {@link
   * UadpDiscoveryProbe#targetPublisherId()} — the id of the <b>probed</b> Publisher. The
   * SecurityHeader is emitted in mode None; probes carry no sequence number.
   *
   * @param encodingContext the {@link EncodingContext}.
   * @param probe the probe to encode.
   * @return the encoded NetworkMessage; the caller assumes ownership of its buffer.
   * @throws UaException if the probe could not be encoded; {@code Bad_NotSupported} if it is not a
   *     DataSetMetaData probe (ProbeType 1, InformationType 2).
   */
  public EncodedNetworkMessage encodeProbe(
      EncodingContext encodingContext, UadpDiscoveryProbe probe) throws UaException {
    return UadpDiscoveryMessageEncoder.encodeProbe(encodingContext, probe);
  }

  /**
   * Encode one DataSetMetaData discovery announcement NetworkMessage (Part 14 §7.2.4.6.4).
   *
   * <p>The NetworkMessage header PublisherId is the announcement's {@link
   * UadpMetaDataAnnouncement#publisherId()} — the announcing Publisher's own id. The SecurityHeader
   * is emitted in mode None; the per-PublisherId announcement sequence number is supplied by the
   * caller via {@link UadpMetaDataAnnouncement#sequenceNumber()}. Chunked emission is not
   * supported; enforcing DiscoveryMaxMessageSize against the encoded result is the caller's
   * responsibility.
   *
   * @param encodingContext the {@link EncodingContext} used to encode the metadata structure.
   * @param announcement the announcement to encode.
   * @return the encoded NetworkMessage; the caller assumes ownership of its buffer.
   * @throws UaException if the announcement could not be encoded.
   */
  public EncodedNetworkMessage encodeAnnouncement(
      EncodingContext encodingContext, UadpMetaDataAnnouncement announcement) throws UaException {
    return UadpDiscoveryMessageEncoder.encodeAnnouncement(encodingContext, announcement);
  }
}
