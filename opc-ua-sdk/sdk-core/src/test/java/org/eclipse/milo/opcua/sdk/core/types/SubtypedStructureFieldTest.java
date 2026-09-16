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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicCodecFactory;
import org.eclipse.milo.opcua.sdk.core.types.util.AbstractDataType;
import org.eclipse.milo.opcua.sdk.core.types.util.DynamicEncodingContext;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SubtypedStructureFieldTest {

  // Nested codecs must retain their Java representation and produce the same binary wire values.
  @ParameterizedTest
  @MethodSource("structuredFields")
  void decodesAndReencodesPermittedStructuredValues(
      String scenario, int rank, StructureType parentType) {
    var context = new DynamicEncodingContext();
    context.dataTypeManager.registerType(
        NodeIds.Range,
        new Range.Codec(),
        NodeIds.Range_Encoding_DefaultBinary,
        NodeIds.Range_Encoding_DefaultXml,
        NodeIds.Range_Encoding_DefaultJson);

    DataType rangeSubtype =
        register(
            context,
            "RangeSubtype",
            new StructureDefinition(
                new NodeId(1, "RangeSubtype.Binary"),
                NodeIds.Range,
                StructureType.Structure,
                new StructureField[] {
                  field("Low", NodeIds.Double, -1, false),
                  field("High", NodeIds.Double, -1, false),
                  field("Label", NodeIds.String, -1, false)
                }));
    DataType optionSet = register(context, "Flags", new EnumDefinition(new EnumField[0]));
    var range = new Range(1.0, 2.0);
    var dynamicRange =
        new DynamicStructType(
            rangeSubtype, new LinkedHashMap<>(Map.of("Low", 3.0, "High", 4.0, "Label", "subtype")));
    var union =
        new DynamicUnionType(
            context.unionOfScalar, new DynamicUnionType.UnionValue("SByte", (byte) 42));
    var flags =
        new DynamicOptionSetType(
            optionSet, ByteString.of(new byte[] {1}), ByteString.of(new byte[] {3}));

    UaStructuredType[] values =
        switch (scenario) {
          case "java" -> new Range[] {range};
          case "struct" -> new DynamicStructType[] {dynamicRange};
          case "union" -> new DynamicUnionType[] {union};
          case "optionSet" -> new DynamicOptionSetType[] {flags};
          case "mixed" -> new UaStructuredType[] {range, dynamicRange};
          case "empty" -> new UaStructuredType[0];
          default -> throw new IllegalArgumentException(scenario);
        };
    NodeId declaredType =
        switch (scenario) {
          case "union" -> context.unionOfScalar.getNodeId();
          case "optionSet" -> optionSet.getNodeId();
          default -> NodeIds.Range;
        };
    when(context.dataTypeTree.isStructType(declaredType)).thenReturn(true);
    DataType parent =
        register(
            context,
            "Poly",
            new StructureDefinition(
                new NodeId(1, "Poly.Binary"),
                NodeIds.Structure,
                parentType,
                new StructureField[] {field("Items", declaredType, rank, true)}));
    var codec = DynamicCodecFactory.create(parent, context.dataTypeTree);
    ExtensionObject[] expected =
        Arrays.stream(values)
            .map(value -> ExtensionObject.encode(context, value))
            .toArray(ExtensionObject[]::new);

    ByteBuf input = Unpooled.buffer();
    ByteBuf output = Unpooled.buffer();
    try {
      // Write the wire layout independently to check both representation and encoding semantics.
      var encoder = new OpcUaBinaryEncoder(context).setBuffer(input);
      if (parentType == StructureType.UnionWithSubtypedValues) {
        encoder.encodeUInt32(null, uint(1));
      }
      switch (rank) {
        case -1 -> encoder.encodeExtensionObject("Items", expected[0]);
        case 1 -> encoder.encodeExtensionObjectArray("Items", expected);
        default ->
            encoder.encodeMatrix("Items", new Matrix(expected, new int[] {1, expected.length}));
      }
      UaStructuredType decoded =
          new OpcUaBinaryDecoder(context).setBuffer(input).decodeStruct(null, codec);
      assertFalse(input.isReadable());
      assertValues(values, fieldValue(decoded), rank);

      // Encode through the public ExtensionObject API, then verify decoded semantic content.
      ExtensionObject encoded = ExtensionObject.encode(context, decoded);
      UaStructuredType roundTripped = encoded.decode(context);
      assertValues(values, fieldValue(roundTripped), rank);

      new OpcUaBinaryEncoder(context).setBuffer(output).encodeStruct(null, decoded, codec);
      var decoder = new OpcUaBinaryDecoder(context).setBuffer(output);
      if (parentType == StructureType.UnionWithSubtypedValues) {
        assertEquals(uint(1), decoder.decodeUInt32(null));
      }
      switch (rank) {
        case -1 -> assertEquals(expected[0], decoder.decodeExtensionObject("Items"));
        case 1 -> assertArrayEquals(expected, decoder.decodeExtensionObjectArray("Items"));
        default -> {
          Matrix matrix = decoder.decodeMatrix("Items", OpcUaDataType.ExtensionObject);
          assertArrayEquals(new int[] {1, expected.length}, matrix.getDimensions());
          assertArrayEquals(expected, (ExtensionObject[]) matrix.getElements());
        }
      }
      assertFalse(output.isReadable());
    } finally {
      input.release();
      output.release();
    }
  }

  private static Stream<Arguments> structuredFields() {
    return Stream.of(
            StructureType.StructureWithSubtypedValues, StructureType.UnionWithSubtypedValues)
        .flatMap(
            parentType ->
                Stream.of(-1, 1, 2)
                    .flatMap(
                        rank ->
                            Stream.of("java", "struct", "union", "optionSet", "mixed", "empty")
                                .filter(
                                    scenario ->
                                        rank != -1
                                            || !(scenario.equals("mixed")
                                                || scenario.equals("empty")))
                                .map(scenario -> Arguments.of(scenario, rank, parentType))));
  }

  private static Object fieldValue(UaStructuredType value) {
    if (value instanceof DynamicStructType struct) {
      return struct.getMembers().get("Items");
    }
    var union = assertInstanceOf(DynamicUnionType.class, value);
    var active = union.getValue().orElseThrow();
    assertEquals("Items", active.fieldName());
    return active.fieldValue();
  }

  private static void assertValues(UaStructuredType[] expected, Object value, int rank) {
    UaStructuredType[] actual;
    if (rank == -1) {
      actual = new UaStructuredType[] {assertInstanceOf(UaStructuredType.class, value)};
    } else if (rank == 1) {
      actual = assertInstanceOf(UaStructuredType[].class, value);
    } else {
      var matrix = assertInstanceOf(Matrix.class, value);
      assertArrayEquals(new int[] {1, expected.length}, matrix.getDimensions());
      actual = assertInstanceOf(UaStructuredType[].class, matrix.getElements());
    }
    assertEquals(expected.length, actual.length);
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i].getClass(), actual[i].getClass());
      if (expected[i] instanceof DynamicOptionSetType flags) {
        var actualFlags = (DynamicOptionSetType) actual[i];
        assertEquals(flags.getValue(), actualFlags.getValue());
        assertEquals(flags.getValidBits(), actualFlags.getValidBits());
      } else {
        assertEquals(expected[i], actual[i]);
      }
    }
  }

  private static StructureField field(String name, NodeId type, int rank, boolean subtypes) {
    return new StructureField(name, LocalizedText.NULL_VALUE, type, rank, null, uint(0), subtypes);
  }

  private static DataType register(
      DynamicEncodingContext context, String name, DataTypeDefinition definition) {
    DataType dataType =
        new AbstractDataType(new NodeId(1, name), new QualifiedName(1, name), definition, false) {
          @Override
          public NodeId getBinaryEncodingId() {
            return new NodeId(1, name + ".Binary");
          }
        };
    context.dataTypeManager.registerType(
        dataType.getNodeId(),
        DynamicCodecFactory.create(dataType, context.dataTypeTree),
        dataType.getBinaryEncodingId(),
        null,
        null);
    return dataType;
  }
}
