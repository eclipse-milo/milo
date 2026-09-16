/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.milo.opcua.sdk.core.types;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicCodecFactory;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicStructCodec;
import org.eclipse.milo.opcua.sdk.core.types.util.AbstractDataType;
import org.eclipse.milo.opcua.sdk.core.types.util.DynamicEncodingContext;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

class NullStructuredFieldTest {
  // Empty XML subtype fields occur in published companion Nodesets and must remain null.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void emptyXmlSubtypeFieldSurvivesBinaryRoundTrip(boolean dynamic) throws Exception {
    var fixture = new Fixture(NodeIds.Range, -1, dynamic);
    try (var decoder = new OpcUaXmlDecoder(fixture.context, "<T><Item/></T>")) {
      DynamicStructType decoded = (DynamicStructType) decoder.decodeStruct(null, fixture.codec);
      assertTrue(decoded.getMembers().containsKey("Item"));
      assertNull(decoded.getMembers().get("Item"));
      assertNull(fixture.roundTrip(decoded).getMembers().get("Item"));
    }
  }

  // Nulls must survive alongside both generated Java values and dynamic values, at every position.
  @ParameterizedTest
  @MethodSource("cases")
  void structuredNullsSurviveDecodeAndReencode(
      NodeId fieldType, int rank, boolean dynamic, String shape) throws Exception {
    var fixture = new Fixture(fieldType, rank, dynamic);
    ExtensionObject xo = ExtensionObject.encode(fixture.context, fixture.range());
    ExtensionObject nil = ExtensionObject.of(ByteString.NULL_VALUE, NodeId.NULL_VALUE);
    Object wireValue =
        switch (shape) {
          case "null", "nullArray" -> null;
          case "scalarNull" -> nil;
          case "scalar" -> xo;
          case "empty" -> new ExtensionObject[0];
          case "allNull" -> new ExtensionObject[] {nil, nil};
          case "mixed" -> new ExtensionObject[] {nil, xo, nil};
          case "nullMatrix" -> Matrix.ofNull();
          case "emptyMatrix" ->
              new Matrix(new ExtensionObject[0], new int[] {0, 0}, OpcUaDataType.ExtensionObject);
          case "allNullMatrix" ->
              new Matrix(
                  new ExtensionObject[] {nil, nil},
                  new int[] {1, 2},
                  OpcUaDataType.ExtensionObject);
          case "matrix" ->
              new Matrix(
                  new ExtensionObject[] {nil, xo, nil},
                  new int[] {1, 3},
                  OpcUaDataType.ExtensionObject);
          default -> throw new IllegalArgumentException(shape);
        };
    ByteBuf buffer = Unpooled.buffer();
    try {
      var encoder = new OpcUaBinaryEncoder(fixture.context).setBuffer(buffer);
      if (fieldType.equals(NodeIds.BaseDataType)) {
        encoder.encodeVariant("Item", new Variant(wireValue));
      } else if (rank == -1) {
        encoder.encodeExtensionObject("Item", (ExtensionObject) wireValue);
      } else if (rank == 1) {
        encoder.encodeExtensionObjectArray("Item", (ExtensionObject[]) wireValue);
      } else {
        encoder.encodeMatrix("Item", (Matrix) wireValue);
      }
      DynamicStructType decoded =
          (DynamicStructType)
              new OpcUaBinaryDecoder(fixture.context)
                  .setBuffer(buffer)
                  .decodeStruct(null, fixture.codec);
      assertFalse(buffer.isReadable());
      Object actual = decoded.getMembers().get("Item");
      if (actual instanceof Variant variant) actual = variant.getValue();
      Object expected =
          switch (shape) {
            case "null", "nullArray", "scalarNull" -> null;
            case "scalar" -> fixture.range();
            case "empty", "emptyMatrix" -> new UaStructuredType[0];
            case "allNull", "allNullMatrix" -> new UaStructuredType[] {null, null};
            case "mixed", "matrix" -> new UaStructuredType[] {null, fixture.range(), null};
            case "nullMatrix" -> null;
            default -> throw new IllegalArgumentException(shape);
          };
      if (actual instanceof Matrix matrix) {
        assertArrayEquals(((Matrix) wireValue).getDimensions(), matrix.getDimensions());
        actual = matrix.getElements();
      }
      if (expected instanceof UaStructuredType[] array) {
        assertArrayEquals(array, assertInstanceOf(UaStructuredType[].class, actual));
      } else {
        assertEquals(expected, actual);
      }
      assertTrue(
          Objects.deepEquals(
              decoded.getMembers().get("Item"),
              fixture.roundTrip(decoded).getMembers().get("Item")));
    } finally {
      buffer.release();
    }
  }

  // XML encoding must accept matrices whose first element cannot supply type metadata.
  @ParameterizedTest
  @MethodSource("matrixCases")
  void decodedMatricesCanBeEncodedAsXml(NodeId fieldType, boolean dynamic, String shape)
      throws Exception {
    var fixture = new Fixture(fieldType, fieldType.equals(NodeIds.BaseDataType) ? -1 : 2, dynamic);
    Matrix matrix =
        switch (shape) {
          case "null" -> Matrix.ofNull();
          case "empty" ->
              new Matrix(
                  new UaStructuredType[0],
                  new int[] {0, 0},
                  OpcUaDataType.ExtensionObject,
                  NodeIds.Structure.expanded());
          case "allNull" ->
              new Matrix(
                  new UaStructuredType[] {null, null},
                  new int[] {1, 2},
                  OpcUaDataType.ExtensionObject,
                  NodeIds.Structure.expanded());
          case "mixed" ->
              new Matrix(
                  new UaStructuredType[] {null, fixture.range(), null},
                  new int[] {1, 3},
                  OpcUaDataType.ExtensionObject,
                  NodeIds.Structure.expanded());
          default -> throw new IllegalArgumentException(shape);
        };
    var members = new LinkedHashMap<String, Object>();
    members.put("Item", fieldType.equals(NodeIds.BaseDataType) ? new Variant(matrix) : matrix);
    DynamicStructType decoded = fixture.roundTrip(new DynamicStructType(fixture.parent, members));
    String xml;
    try (var encoder = new OpcUaXmlEncoder(fixture.context)) {
      encoder.encodeStruct("T", decoded, fixture.codec);
      xml = encoder.getOutputString();
    }
    var factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    Document document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    NodeList dimensions = document.getElementsByTagNameNS("*", "Dimensions");
    NodeList elements = document.getElementsByTagNameNS("*", "ExtensionObject");
    // Binary Variants normalize a zero-length matrix to an empty array.
    boolean emptyVariantArray = fieldType.equals(NodeIds.BaseDataType) && shape.equals("empty");
    boolean hasDimensions = !shape.equals("null") && !emptyVariantArray;
    assertEquals(hasDimensions ? 1 : 0, dimensions.getLength(), xml);
    assertEquals(
        emptyVariantArray ? 1 : 0,
        document.getElementsByTagNameNS("*", "ListOfExtensionObject").getLength(),
        xml);
    assertEquals(
        switch (shape) {
          case "allNull" -> 2;
          case "mixed" -> 3;
          default -> 0;
        },
        elements.getLength(),
        xml);
    NodeList dimensionValues = document.getElementsByTagNameNS("*", "Int32");
    assertEquals(
        hasDimensions ? matrix.getDimensions().length : 0, dimensionValues.getLength(), xml);
    for (int i = 0; i < dimensionValues.getLength(); i++) {
      assertEquals(
          Integer.toString(matrix.getDimensions()[i]),
          dimensionValues.item(i).getTextContent(),
          xml);
    }
    for (int i = 0; i < elements.getLength(); i++) {
      boolean isNull = !shape.equals("mixed") || i != 1;
      assertEquals(
          isNull ? 0 : 1,
          ((Element) elements.item(i)).getElementsByTagNameNS("*", "Body").getLength(),
          xml);
    }
  }

  static Stream<Arguments> matrixCases() {
    return Stream.of(NodeIds.Range, NodeIds.Structure, NodeIds.BaseDataType)
        .flatMap(
            type ->
                Stream.of(false, true)
                    .flatMap(
                        dynamic ->
                            Stream.of("null", "empty", "allNull", "mixed")
                                .map(shape -> Arguments.of(type, dynamic, shape))));
  }

  static Stream<Arguments> cases() {
    return Stream.of(false, true)
        .flatMap(
            dynamic ->
                Stream.of(NodeIds.Range, NodeIds.Structure, NodeIds.BaseDataType)
                    .flatMap(
                        type ->
                            Stream.of(
                                    "null",
                                    "nullArray",
                                    "scalarNull",
                                    "scalar",
                                    "empty",
                                    "allNull",
                                    "mixed",
                                    "nullMatrix",
                                    "emptyMatrix",
                                    "allNullMatrix",
                                    "matrix")
                                .map(
                                    shape ->
                                        Arguments.of(
                                            type,
                                            type.equals(NodeIds.BaseDataType)
                                                ? -1
                                                : switch (shape) {
                                                  case "nullArray", "empty", "allNull", "mixed" ->
                                                      1;
                                                  case "nullMatrix",
                                                      "emptyMatrix",
                                                      "allNullMatrix",
                                                      "matrix" ->
                                                      2;
                                                  default -> -1;
                                                },
                                            dynamic,
                                            shape))));
  }

  private static class Fixture {
    final DynamicEncodingContext context = new DynamicEncodingContext();
    final DataType rangeType;
    final DataType parent;
    final DynamicStructCodec codec;
    final boolean dynamic;

    Fixture(NodeId fieldType, int rank, boolean dynamic) {
      this.dynamic = dynamic;
      rangeType =
          new AbstractDataType(
              NodeIds.Range,
              new QualifiedName(0, "Range"),
              new StructureDefinition(
                  NodeIds.Range_Encoding_DefaultBinary,
                  NodeIds.Structure,
                  StructureType.Structure,
                  new StructureField[] {
                    field("Low", NodeIds.Double, -1, false),
                    field("High", NodeIds.Double, -1, false)
                  }),
              false) {
            @Override
            public NodeId getBinaryEncodingId() {
              return NodeIds.Range_Encoding_DefaultBinary;
            }

            @Override
            public NodeId getXmlEncodingId() {
              return NodeIds.Range_Encoding_DefaultXml;
            }
          };
      context.dataTypeManager.registerType(
          NodeIds.Range,
          dynamic ? DynamicCodecFactory.create(rangeType, context.dataTypeTree) : new Range.Codec(),
          NodeIds.Range_Encoding_DefaultBinary,
          NodeIds.Range_Encoding_DefaultXml,
          NodeIds.Range_Encoding_DefaultJson);
      when(context.dataTypeTree.isStructType(NodeIds.Range)).thenReturn(true);
      parent =
          new AbstractDataType(
              new NodeId(1, "T"),
              new QualifiedName(1, "T"),
              new StructureDefinition(
                  new NodeId(1, "T.Binary"),
                  NodeIds.Structure,
                  StructureType.StructureWithSubtypedValues,
                  new StructureField[] {field("Item", fieldType, rank, true)}),
              false) {};
      codec = (DynamicStructCodec) DynamicCodecFactory.create(parent, context.dataTypeTree);
    }

    UaStructuredType range() {
      return dynamic
          ? new DynamicStructType(rangeType, new LinkedHashMap<>(Map.of("Low", 1.0, "High", 2.0)))
          : new Range(1.0, 2.0);
    }

    DynamicStructType roundTrip(DynamicStructType value) {
      ByteBuf buffer = Unpooled.buffer();
      try {
        new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeStruct(null, value, codec);
        DynamicStructType decoded =
            (DynamicStructType)
                new OpcUaBinaryDecoder(context).setBuffer(buffer).decodeStruct(null, codec);
        assertFalse(buffer.isReadable());
        return decoded;
      } finally {
        buffer.release();
      }
    }
  }

  private static StructureField field(String name, NodeId type, int rank, boolean subtypes) {
    return new StructureField(
        name, LocalizedText.NULL_VALUE, type, rank, null, UInteger.MIN, subtypes);
  }
}
