/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.UaDataType;

public abstract class GenericDataTypeCodec<T extends UaDataType> implements DataTypeCodec {

  public abstract Class<T> getType();

  @Override
  public Object decode(EncodingContext context, UaDecoder decoder) throws UaSerializationException {
    Object untypedValue = decodeType(context, decoder);
    try {
      return getType().cast(untypedValue);
    } catch (ClassCastException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public void encode(EncodingContext context, UaEncoder encoder, Object value)
      throws UaSerializationException {
    T typedValue;
    try {
      typedValue = getType().cast(value);
    } catch (ClassCastException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
    encodeType(context, encoder, typedValue);
  }

  public abstract T decodeType(EncodingContext context, UaDecoder decoder)
      throws UaSerializationException;

  public abstract void encodeType(EncodingContext context, UaEncoder encoder, T value)
      throws UaSerializationException;
}
