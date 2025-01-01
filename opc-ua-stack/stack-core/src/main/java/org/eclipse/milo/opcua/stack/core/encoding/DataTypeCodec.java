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

import org.eclipse.milo.opcua.stack.core.UaSerializationException;

public interface DataTypeCodec {

  /**
   * @return the {@link Class} of the DataType this codec encodes.
   */
  Class<?> getType();

  /**
   * Decode an Object using the provided {@link UaDecoder}.
   *
   * @param context the {@link EncodingContext}.
   * @param decoder the {@link UaDecoder} to decode from.
   * @return a decoded Object.
   */
  Object decode(EncodingContext context, UaDecoder decoder) throws UaSerializationException;

  /**
   * Encode an Object using the provided {@link UaEncoder}.
   *
   * @param context the {@link EncodingContext}.
   * @param encoder the {@link UaEncoder} to encode to.
   * @param value the Object to encode.
   */
  void encode(EncodingContext context, UaEncoder encoder, Object value)
      throws UaSerializationException;
}
