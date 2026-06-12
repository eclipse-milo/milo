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
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * SPI for PubSub message mappings: encodes and decodes NetworkMessages for one mapping, e.g. UADP
 * or JSON.
 *
 * <p>The UADP mapping is built in; additional providers are registered via {@code
 * PubSubServiceConfig.Builder#messageMappingProvider(MessageMappingProvider)}.
 *
 * <p>UADP discovery (OPC UA Part 14 §7.2.4.6) is not part of this SPI: the runtime always encodes
 * and decodes discovery probes and DataSetMetaData announcements with the built-in {@link
 * UadpMessageMapping} and opens its discovery channels with the built-in UDP transport provider
 * (see {@code UdpConnectionConfig#getDiscoveryAddress()}). Registering a custom {@code "uadp"}
 * provider affects the data plane only; probes and Bad-status denial announcements received on the
 * data address are not surfaced through a custom provider's {@link #decode(DecodeContext,
 * ByteBuf)}.
 */
public interface MessageMappingProvider {

  /**
   * Get the name of the mapping this provider implements, e.g. {@code "uadp"} or {@code "json"}.
   *
   * @return the name of the mapping this provider implements.
   */
  String mappingName();

  /**
   * Encode the draft DataSetMessages in {@code context} into one or more NetworkMessages.
   *
   * <p>Mappings that combine all drafts into a single NetworkMessage (UADP) return a singleton
   * list; mappings with per-message or size-based splitting rules (e.g. the JSON mapping's {@code
   * SingleDataSetMessage}) return one entry per resulting NetworkMessage, each attributed to the
   * writers whose DataSetMessages it carries. The engine sends every returned message.
   *
   * @param context the encode context carrying the group config, header values, and draft
   *     DataSetMessages.
   * @return the encoded NetworkMessages, in send order; the caller assumes ownership of every
   *     returned buffer.
   * @throws UaException if the messages could not be encoded; no buffers are returned (the
   *     implementation releases anything it allocated).
   */
  List<EncodedNetworkMessage> encode(EncodeContext context) throws UaException;

  /**
   * Encode one DataSetMetaData NetworkMessage for the writer in {@code context}.
   *
   * <p>Used by the engine's metadata publication on broker connections (Part 14 §6.4.2.5.5/.6):
   * retained metadata is published at startup, on configuration version changes, and periodically
   * when a {@code MetaDataUpdateTime} is configured. The UADP mapping encodes a discovery
   * DataSetMetaData announcement (§7.2.4.6.4); the JSON mapping encodes a {@code ua-metadata}
   * message (§7.2.5.5.2).
   *
   * @param context the context identifying the publishing writer.
   * @param metaData the metadata to publish; the wire-bound surface, with Milo-internal field
   *     properties already stripped.
   * @param sequenceNumber the metadata sequence number, maintained by the engine per PublisherId.
   * @return the encoded NetworkMessage; the caller assumes ownership of its buffer.
   * @throws UaException if the message could not be encoded; {@code Bad_NotSupported} if this
   *     mapping does not support metadata emission (the default).
   */
  default EncodedNetworkMessage encodeMetaData(
      MetaDataEncodeContext context, DataSetMetaDataType metaData, UShort sequenceNumber)
      throws UaException {

    throw new UaException(
        StatusCodes.Bad_NotSupported,
        "mapping '%s' does not support DataSetMetaData emission".formatted(mappingName()));
  }

  /**
   * Decode one NetworkMessage from {@code payload}.
   *
   * <p>The payload buffer is only valid for the duration of the call; the caller retains ownership
   * and releases it afterwards.
   *
   * @param context the decode context.
   * @param payload the buffer containing the received NetworkMessage.
   * @return the decoded NetworkMessage.
   * @throws UaException if the message could not be decoded.
   */
  DecodedNetworkMessage decode(DecodeContext context, ByteBuf payload) throws UaException;
}
