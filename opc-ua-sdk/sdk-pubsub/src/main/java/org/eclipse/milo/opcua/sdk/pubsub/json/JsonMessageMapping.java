/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.json;

import io.netty.buffer.ByteBuf;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MetaDataEncodeContext;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * The JSON message mapping codec (OPC UA Part 14 §7.2.5): encodes and decodes {@code ua-data} and
 * {@code ua-metadata} JSON NetworkMessages. Registered with the runtime via {@link
 * JsonMessageMappingProvider}.
 *
 * <p>The codec is stateless; header values that evolve over time (sequence numbers, timestamps) are
 * supplied by the caller via the {@link EncodeContext}. JSON documents are encoded and decoded as
 * complete UTF-8 documents — broker transports (MQTT) deliver whole NetworkMessages without
 * framing.
 *
 * <p><b>Encoding</b> honors the {@link JsonWriterGroupSettings} and {@link
 * JsonDataSetWriterSettings} content masks exactly, with two adjustments:
 *
 * <ul>
 *   <li>Empty masks (the configuration default) select the recommended defaults: {@code
 *       NetworkMessageHeader | DataSetMessageHeader | PublisherId} at the NetworkMessage level and
 *       {@code DataSetWriterId | SequenceNumber | MetaDataVersion | Timestamp | Status |
 *       FieldEncoding2} (VerboseEncoding, §7.2.5.4.2) at the DataSetMessage level. A consequence is
 *       that the fully headerless layout (all NetworkMessage mask bits clear) is not emittable: an
 *       all-clear mask is indistinguishable from the unset default. Every other collapsed layout is
 *       reachable through non-empty masks.
 *   <li>The deprecated field encodings of OPC 10000-6 Annex H are upgraded on encode:
 *       NonReversibleEncoding becomes VerboseEncoding and ReversibleFieldEncoding becomes
 *       CompactEncoding, each with a one-time WARN.
 * </ul>
 *
 * <p>One NetworkMessage is produced per DataSetMessage when {@code SingleDataSetMessage} is set,
 * and per distinct DataSetClassId when {@code DataSetClassId} is set; otherwise all drafts of a
 * call share one NetworkMessage. {@code MessageId} is a random UUID per NetworkMessage; {@code
 * ReplyTo} is never emitted; the DataSetMessage {@code Status} header is omitted when Good. Key
 * frame, delta frame, and keep-alive DataSetMessages are emitted ({@code ua-keyframe}/{@code
 * ua-deltaframe}/{@code ua-keepalive}); a delta frame carries only the changed fields and requires
 * the DataSetMessageHeader and the MessageType member in the effective masks (Part 14 Annex
 * A.3.3.4). Event emission is not supported in this version.
 *
 * <p><b>Decoding</b> is tolerant across all eight structural layouts of §7.2.5.3 and all four field
 * encodings (current Compact/Verbose plus the deprecated Reversible/NonReversible spellings),
 * detecting the layout from the JSON shape; see {@link JsonNetworkMessageDecoder} and {@link
 * JsonFieldDecoder}. Payload fields surface name-keyed; {@code ua-deltaframe} messages decode as
 * delta frames; {@code ua-keepalive} (and header-less empty objects) decode as keep-alives; {@code
 * ua-metadata} messages surface on the metadata path; a missing DataSetMessage {@code Status}
 * decodes as Good. The expected-layout masks of {@link JsonDataSetReaderSettings} are not required
 * for decoding.
 */
public final class JsonMessageMapping {

  /**
   * Encode the draft DataSetMessages in {@code context} into one or more {@code ua-data}
   * NetworkMessages.
   *
   * @param context the encode context; {@link DataSetMessageDraft#metaData()} must be present on
   *     data drafts (the JSON payload is name-keyed).
   * @return the encoded NetworkMessages, in send order; the caller assumes ownership of every
   *     returned buffer.
   * @throws UaException with {@code Bad_ConfigurationError} if the group or writer settings are not
   *     JSON settings, a draft carries no resolved metadata, or a delta frame draft's effective
   *     masks omit the DataSetMessageHeader or the MessageType member (Part 14 Annex A.3.3.4);
   *     {@code Bad_NotSupported} if a draft is an event message; {@code Bad_InvalidArgument} if
   *     {@code context} carries no drafts; {@code Bad_EncodingError} if encoding fails.
   */
  public List<EncodedNetworkMessage> encode(EncodeContext context) throws UaException {
    return JsonNetworkMessageEncoder.encode(context);
  }

  /**
   * Encode one {@code ua-metadata} NetworkMessage (§7.2.5.5.2) for the writer in {@code context}.
   *
   * <p>All Table 188 members are emitted, CompactEncoding throughout. The JSON metadata message
   * carries no sequence number member; ordering on the metadata queue is positional.
   *
   * @param context the context identifying the publishing writer.
   * @param metaData the metadata to publish.
   * @return the encoded NetworkMessage; the caller assumes ownership of its buffer.
   * @throws UaException if the message could not be encoded.
   */
  public EncodedNetworkMessage encodeMetaData(
      MetaDataEncodeContext context, DataSetMetaDataType metaData) throws UaException {
    return JsonNetworkMessageEncoder.encodeMetaData(context, metaData);
  }

  /**
   * Decode one JSON NetworkMessage from {@code payload}, a complete UTF-8 JSON document.
   *
   * <p>The payload buffer is only valid for the duration of the call; the caller retains ownership
   * and releases it afterwards.
   *
   * @param context the decode context.
   * @param payload the buffer containing the received NetworkMessage.
   * @return the decoded NetworkMessage; {@code ua-metadata} content surfaces in {@link
   *     DecodedNetworkMessage#metaData()}.
   * @throws UaException with {@code Bad_DecodingError} if the payload is not a JSON document of any
   *     recognizable NetworkMessage shape.
   */
  public DecodedNetworkMessage decode(DecodeContext context, ByteBuf payload) throws UaException {
    return JsonNetworkMessageDecoder.decode(context, payload);
  }

  /**
   * Encode one {@code ua-metadata} NetworkMessage, ignoring {@code sequenceNumber}: the JSON
   * metadata message has no sequence number member (Table 188).
   *
   * @see #encodeMetaData(MetaDataEncodeContext, DataSetMetaDataType)
   */
  EncodedNetworkMessage encodeMetaData(
      MetaDataEncodeContext context, DataSetMetaDataType metaData, UShort sequenceNumber)
      throws UaException {
    return encodeMetaData(context, metaData);
  }
}
