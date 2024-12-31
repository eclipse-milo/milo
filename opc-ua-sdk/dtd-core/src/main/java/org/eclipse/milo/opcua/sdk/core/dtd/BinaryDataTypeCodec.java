/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd;

import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;

public interface BinaryDataTypeCodec extends DataTypeCodec {

  @Override
  default Object decode(EncodingContext context, UaDecoder decoder)
      throws UaSerializationException {
    return decode(context, (OpcUaBinaryDecoder) decoder);
  }

  @Override
  default void encode(EncodingContext context, UaEncoder encoder, Object value)
      throws UaSerializationException {
    encode(context, (OpcUaBinaryEncoder) encoder, value);
  }

  /**
   * Decode an Object using the provided {@link OpcUaBinaryDecoder}.
   *
   * @param context the {@link EncodingContext}.
   * @param decoder the {@link OpcUaBinaryDecoder} to decode from.
   * @return a decoded Object.
   */
  Object decode(EncodingContext context, OpcUaBinaryDecoder decoder)
      throws UaSerializationException;

  /**
   * Encode an Object using the provided {@link OpcUaBinaryEncoder}.
   *
   * @param context the {@link EncodingContext}.
   * @param encoder the {@link OpcUaBinaryEncoder} to encode to.
   * @param value the Object to encode.
   */
  void encode(EncodingContext context, OpcUaBinaryEncoder encoder, Object value)
      throws UaSerializationException;

  static BinaryDataTypeCodec from(DataTypeCodec codec) {
    return new BinaryDataTypeCodecImpl(codec);
  }

  class BinaryDataTypeCodecImpl implements BinaryDataTypeCodec {

    private final DataTypeCodec codec;

    public BinaryDataTypeCodecImpl(DataTypeCodec codec) {
      this.codec = codec;
    }

    @Override
    public Class<?> getType() {
      return codec.getType();
    }

    @Override
    public Object decode(EncodingContext context, OpcUaBinaryDecoder decoder)
        throws UaSerializationException {
      return codec.decode(context, decoder);
    }

    @Override
    public void encode(EncodingContext context, OpcUaBinaryEncoder encoder, Object value)
        throws UaSerializationException {
      codec.encode(context, encoder, value);
    }
  }
}
