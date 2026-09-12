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

import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.IdentityHashMap;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
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

    context.getNamespaceTable().add("urn:milo:test:recursive-structure");
    context
        .getDataTypeManager()
        .registerType(
            RecursiveStructure.TYPE_ID,
            new RecursiveStructureCodec(),
            RecursiveStructure.BINARY_ENCODING_ID,
            null,
            null);

    // Root depth is zero. Four recursive fields plus the XVType leaf reach, but do not exceed,
    // the configured limit. Unlike a null wrapper, the leaf performs a real codec decode.
    UaStructuredType atLimit = recursiveStructure(context, maxRecursionDepth).decode(context);
    for (int i = 0; i < maxRecursionDepth; i++) {
      atLimit = assertInstanceOf(RecursiveStructure.class, atLimit).child();
    }
    assertEquals(new XVType(1.0, 2.0f), atLimit);

    ExtensionObject recursive = recursiveStructure(context, maxRecursionDepth + 1);

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
      private final DataTypeManager dataTypes =
          DefaultDataTypeManager.createAndInitialize(getNamespaceTable());

      @Override
      public DataTypeManager getDataTypeManager() {
        return dataTypes;
      }

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

  private static ExtensionObject recursiveStructure(EncodingContext context, int depth) {
    ExtensionObject nested = ExtensionObject.encode(context, new XVType(1.0, 2.0f));

    for (int i = 0; i < depth; i++) {
      ByteBuf buffer = Unpooled.buffer();
      try {
        // Build already encoded child bodies so only decoding is constrained by this test.
        new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeExtensionObject("Child", nested);
        nested =
            ExtensionObject.of(
                ByteString.of(ByteBufUtil.getBytes(buffer)), RecursiveStructure.BINARY_ENCODING_ID);
      } finally {
        buffer.release();
      }
    }
    return nested;
  }

  // A Structure-typed field may legally contain another RecursiveStructure or the XVType leaf.
  // KeyValuePair's Variant field would leave ExtensionObject bodies undecoded, so it cannot test
  // the shared depth guard across default-binary codec invocations.
  private record RecursiveStructure(UaStructuredType child) implements UaStructuredType {
    private static final NodeId TYPE_ID = new NodeId(1, 1);
    private static final NodeId BINARY_ENCODING_ID = new NodeId(1, 2);

    @Override
    public ExpandedNodeId getTypeId() {
      return TYPE_ID.expanded();
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
      return BINARY_ENCODING_ID.expanded();
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
      return NodeId.NULL_VALUE.expanded();
    }

    @Override
    public ExpandedNodeId getJsonEncodingId() {
      return NodeId.NULL_VALUE.expanded();
    }
  }

  private static final class RecursiveStructureCodec
      extends GenericDataTypeCodec<RecursiveStructure> {
    @Override
    public Class<RecursiveStructure> getType() {
      return RecursiveStructure.class;
    }

    @Override
    public RecursiveStructure decodeType(EncodingContext context, UaDecoder decoder) {
      ExtensionObject child = decoder.decodeExtensionObject("Child");
      return new RecursiveStructure(child == null || child.isNull() ? null : child.decode(context));
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, RecursiveStructure value) {
      encoder.encodeExtensionObject(
          "Child", value.child() == null ? null : ExtensionObject.encode(context, value.child()));
    }
  }

  private record DecodeChain(ExtensionObject root, DataTypeEncoding encoding) {}
}
