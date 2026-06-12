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
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageMappingProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MetaDataEncodeContext;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * The built-in {@link MessageMappingProvider} for the JSON message mapping (OPC UA Part 14 §7.2.5),
 * mapping name {@code "json"}, delegating to {@link JsonMessageMapping}.
 *
 * <p>Registered automatically by the runtime engine; a user-registered {@code "json"} provider
 * takes precedence (configured providers are consulted before the built-ins).
 */
public final class JsonMessageMappingProvider implements MessageMappingProvider {

  /** The name of the JSON message mapping: {@code "json"}. */
  public static final String MAPPING_NAME = "json";

  private final JsonMessageMapping mapping = new JsonMessageMapping();

  @Override
  public String mappingName() {
    return MAPPING_NAME;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The JSON mapping returns one NetworkMessage per DataSetMessage when {@code
   * SingleDataSetMessage} is set and one per distinct DataSetClassId when {@code DataSetClassId} is
   * set; otherwise a singleton list.
   */
  @Override
  public List<EncodedNetworkMessage> encode(EncodeContext context) throws UaException {
    return mapping.encode(context);
  }

  /**
   * {@inheritDoc}
   *
   * <p>The JSON mapping encodes a {@code ua-metadata} message (Part 14 §7.2.5.5.2). {@code
   * sequenceNumber} is ignored: the JSON metadata message has no sequence number member.
   */
  @Override
  public EncodedNetworkMessage encodeMetaData(
      MetaDataEncodeContext context, DataSetMetaDataType metaData, UShort sequenceNumber)
      throws UaException {
    return mapping.encodeMetaData(context, metaData, sequenceNumber);
  }

  @Override
  public DecodedNetworkMessage decode(DecodeContext context, ByteBuf payload) throws UaException {
    return mapping.decode(context, payload);
  }
}
