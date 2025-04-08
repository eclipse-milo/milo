/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types.codec;

import org.eclipse.milo.opcua.sdk.core.types.DynamicOptionSetType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;

class DynamicOptionSetCodec extends GenericDataTypeCodec<DynamicOptionSetType> {

  private final DataType dataType;

  DynamicOptionSetCodec(DataType dataType) {
    this.dataType = dataType;
  }

  @Override
  public Class<DynamicOptionSetType> getType() {
    return DynamicOptionSetType.class;
  }

  @Override
  public DynamicOptionSetType decodeType(EncodingContext context, UaDecoder decoder)
      throws UaSerializationException {
    ByteString value = decoder.decodeByteString("Value");
    ByteString validBits = decoder.decodeByteString("ValidBits");

    return new DynamicOptionSetType(dataType, value, validBits);
  }

  @Override
  public void encodeType(EncodingContext context, UaEncoder encoder, DynamicOptionSetType value)
      throws UaSerializationException {
    encoder.encodeByteString("Valid", value.getValue());
    encoder.encodeByteString("ValidBits", value.getValidBits());
  }
}
