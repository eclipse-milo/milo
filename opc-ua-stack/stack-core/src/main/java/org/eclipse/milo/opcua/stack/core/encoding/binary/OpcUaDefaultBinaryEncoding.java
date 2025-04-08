/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
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
  public ExtensionObject encode(EncodingContext context, UaStructuredType struct) {
    NodeId encodingId =
        struct
            .getBinaryEncodingId()
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "no namespace registered: "
                            + struct.getBinaryEncodingId().toParseableString()));

    DataTypeCodec codec = context.getDataTypeManager().getCodec(encodingId);

    if (codec == null) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError,
          "no codec registered for encodingId=" + encodingId.toParseableString());
    }

    ByteBuf buffer = buffer();

    try {
      OpcUaBinaryEncoder encoder = new OpcUaBinaryEncoder(context);
      encoder.setBuffer(buffer);

      encoder.encodeStruct(null, struct, codec);

      return ExtensionObject.of(ByteString.of(ByteBufUtil.getBytes(buffer)), encodingId);
    } finally {
      buffer.release();
    }
  }

  @Override
  public UaStructuredType decode(EncodingContext context, ExtensionObject encoded) {
    if (encoded instanceof ExtensionObject.Binary xo) {
      DataTypeCodec codec = context.getDataTypeManager().getCodec(encoded.getEncodingOrTypeId());

      if (codec == null) {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError,
            "no codec registered for encodingId="
                + encoded.getEncodingOrTypeId().toParseableString());
      }

      ByteString binaryBody = xo.getBody();
      byte[] bs = binaryBody.bytesOrEmpty();

      ByteBuf buffer = Unpooled.wrappedBuffer(bs);

      OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(context);
      decoder.setBuffer(buffer);

      return decoder.decodeStruct(null, codec);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "not binary encoded: " + encoded);
    }
  }
}
