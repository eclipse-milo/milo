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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.IdentityHashMap;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;

class OpcUaDefaultBinaryEncodingTest {

  @Test
  void serializationRoundTrip() throws Exception {
    EncodingContext context = new DefaultEncodingContext();
    OpcUaDefaultBinaryEncoding encoding = OpcUaDefaultBinaryEncoding.getInstance();

    var value = new XVType(1.0, 2.0f);
    NodeId encodingId = XVType.XML_ENCODING_ID.toNodeIdOrThrow(context.getNamespaceTable());

    ExtensionObject encoded = encoding.encode(context, value);

    UaStructuredType decoded = encoding.decode(context, encoded);

    assertEquals(value, decoded);
  }

  @Test
  void nestedExtensionObjectsShareRecursionLimit() throws Exception {
    int maxRecursionDepth = 4;
    EncodingContext context = limitedContext(maxRecursionDepth);

    ExtensionObject recursive = recursiveDataSetWriter(context, maxRecursionDepth + 1);

    UaSerializationException exception =
        assertThrows(UaSerializationException.class, () -> recursive.decode(context));

    assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, exception.getStatusCode().value());

    var value = new XVType(1.0, 2.0f);
    assertEquals(value, ExtensionObject.encode(context, value).decode(context));
  }

  @Test
  void extensionObjectRecursionLimitBoundary() {
    int maxRecursionDepth = 4;
    EncodingContext context = limitedContext(maxRecursionDepth);

    DecodeChain atLimit = decodeChain(maxRecursionDepth);
    assertInstanceOf(XVType.class, atLimit.root.decode(context, atLimit.encoding));

    DecodeChain overLimit = decodeChain(maxRecursionDepth + 1);
    UaSerializationException exception =
        assertThrows(
            UaSerializationException.class,
            () -> overLimit.root.decode(context, overLimit.encoding));

    assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, exception.getStatusCode().value());
  }

  private static EncodingContext limitedContext(int maxRecursionDepth) {
    EncodingLimits encodingLimits =
        new EncodingLimits(
            EncodingLimits.DEFAULT_MAX_CHUNK_SIZE,
            EncodingLimits.DEFAULT_MAX_CHUNK_COUNT,
            EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE,
            maxRecursionDepth);

    return new DefaultEncodingContext() {
      @Override
      public EncodingLimits getEncodingLimits() {
        return encodingLimits;
      }
    };
  }

  private static DecodeChain decodeChain(int depth) {
    Map<ExtensionObject, ExtensionObject> children = new IdentityHashMap<>();
    ExtensionObject root = ExtensionObject.of(ByteString.of(new byte[] {0}), new NodeId(1, 1));

    for (int i = 0; i < depth; i++) {
      ExtensionObject parent =
          ExtensionObject.of(ByteString.of(new byte[] {(byte) (i + 1)}), new NodeId(1, 1));
      children.put(parent, root);
      root = parent;
    }

    DataTypeEncoding encoding =
        new DataTypeEncoding() {
          @Override
          public QualifiedName getEncodingName() {
            return new QualifiedName(1, "Test");
          }

          @Override
          public ExtensionObject encode(EncodingContext context, UaStructuredType struct) {
            throw new UnsupportedOperationException();
          }

          @Override
          public UaStructuredType decode(EncodingContext context, ExtensionObject encoded) {
            ExtensionObject child = children.get(encoded);
            return child != null ? child.decode(context, this) : new XVType(1.0, 2.0f);
          }
        };

    return new DecodeChain(root, encoding);
  }

  private static ExtensionObject recursiveDataSetWriter(EncodingContext context, int depth)
      throws UaException {
    NodeId encodingId =
        DataSetWriterDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(context.getNamespaceTable());
    ExtensionObject nested = ExtensionObject.of(ByteString.NULL_VALUE, NodeId.NULL_VALUE);

    for (int i = 0; i < depth; i++) {
      ByteBuf buffer = Unpooled.buffer();

      try {
        OpcUaBinaryEncoder encoder = new OpcUaBinaryEncoder(context).setBuffer(buffer);
        encoder.encodeString("Name", "writer");
        encoder.encodeBoolean("Enabled", true);
        encoder.encodeUInt16("DataSetWriterId", ushort(1));
        encoder.encodeUInt32("DataSetFieldContentMask", uint(0));
        encoder.encodeUInt32("KeyFrameCount", uint(1));
        encoder.encodeString("DataSetName", "data-set");
        encoder.encodeStructArray("DataSetWriterProperties", null, KeyValuePair.TYPE_ID);
        encoder.encodeExtensionObject("TransportSettings", nested);
        encoder.encodeExtensionObject(
            "MessageSettings", ExtensionObject.of(ByteString.NULL_VALUE, NodeId.NULL_VALUE));

        nested = ExtensionObject.of(ByteString.of(ByteBufUtil.getBytes(buffer)), encodingId);
      } finally {
        buffer.release();
      }
    }

    return nested;
  }

  private record DecodeChain(ExtensionObject root, DataTypeEncoding encoding) {}
}
