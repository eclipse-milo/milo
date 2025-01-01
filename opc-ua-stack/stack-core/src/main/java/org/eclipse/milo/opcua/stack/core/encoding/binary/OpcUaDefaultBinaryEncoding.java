/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import static io.netty.buffer.Unpooled.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

public class OpcUaDefaultBinaryEncoding implements DataTypeEncoding {

  public static final QualifiedName ENCODING_NAME = new QualifiedName(0, "Default Binary");

  public static OpcUaDefaultBinaryEncoding getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {
    private static final OpcUaDefaultBinaryEncoding INSTANCE = new OpcUaDefaultBinaryEncoding();
  }

  private OpcUaDefaultBinaryEncoding() {}

  @Override
  public QualifiedName getEncodingName() {
    return ENCODING_NAME;
  }

  @Override
  public Object encode(EncodingContext context, Object decodedBody, NodeId encodingId) {

    DataTypeCodec codec = context.getDataTypeManager().getCodec(encodingId);

    if (codec != null) {
      ByteBuf buffer = buffer();

      try {
        OpcUaBinaryEncoder encoder = new OpcUaBinaryEncoder(context);
        encoder.setBuffer(buffer);

        encoder.encodeStruct(null, decodedBody, codec);

        return ByteString.of(ByteBufUtil.getBytes(buffer));
      } finally {
        buffer.release();
      }
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "no codec registered for encodingId=" + encodingId);
    }
  }

  @Override
  public Object decode(EncodingContext context, Object encodedBody, NodeId encodingId) {

    DataTypeCodec codec = context.getDataTypeManager().getCodec(encodingId);

    if (codec != null) {
      ByteString binaryBody = (ByteString) encodedBody;
      byte[] bs = binaryBody.bytesOrEmpty();

      ByteBuf buffer = Unpooled.wrappedBuffer(bs);

      OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(context);
      decoder.setBuffer(buffer);

      return decoder.decodeStruct(null, codec);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "no codec registered for encodingId=" + encodingId);
    }
  }
}
