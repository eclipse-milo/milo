/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;

public interface BinaryDataTypeCodec extends DataTypeCodec {

  @Override
  default UaStructuredType decode(EncodingContext context, UaDecoder decoder)
      throws UaSerializationException {
    return decodeBinary(context, (OpcUaBinaryDecoder) decoder);
  }

  @Override
  default void encode(EncodingContext context, UaEncoder encoder, UaStructuredType value)
      throws UaSerializationException {
    encodeBinary(context, (OpcUaBinaryEncoder) encoder, value);
  }

  /**
   * Decode an Object using the provided {@link OpcUaBinaryDecoder}.
   *
   * @param context the {@link EncodingContext}.
   * @param decoder the {@link OpcUaBinaryDecoder} to decode from.
   * @return a decoded {@link UaStructuredType}.
   */
  UaStructuredType decodeBinary(EncodingContext context, OpcUaBinaryDecoder decoder)
      throws UaSerializationException;

  /**
   * Encode an Object using the provided {@link OpcUaBinaryEncoder}.
   *
   * @param context the {@link EncodingContext}.
   * @param encoder the {@link OpcUaBinaryEncoder} to encode to.
   * @param value the {@link UaStructuredType} to encode.
   */
  void encodeBinary(EncodingContext context, OpcUaBinaryEncoder encoder, UaStructuredType value)
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
    public UaStructuredType decodeBinary(EncodingContext context, OpcUaBinaryDecoder decoder)
        throws UaSerializationException {
      return codec.decode(context, decoder);
    }

    @Override
    public void encodeBinary(
        EncodingContext context, OpcUaBinaryEncoder encoder, UaStructuredType value)
        throws UaSerializationException {
      codec.encode(context, encoder, value);
    }
  }
}
