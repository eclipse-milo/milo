/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UaNodeValueConversionTest extends AbstractClientServerTest {

  private static final Argument ARGUMENT =
      new Argument("value", NodeIds.String, ValueRanks.Scalar, null, LocalizedText.english("d"));

  private static final Argument[] ARGUMENTS = {ARGUMENT, null};

  private static Matrix argumentMatrix() {
    return new Matrix(
        new Argument[] {null, ARGUMENT},
        new int[] {1, 2},
        OpcUaDataType.ExtensionObject,
        NodeIds.Argument.expanded());
  }

  // A typed wrapper for a ScalarOrOneDimension Variable accepts either shape through one method,
  // so the encoded form must follow the actual value, not the declaration.
  @Test
  public void scalarOrOneDimensionEncodesByActualShape() {
    ValueNode node = new ValueNode(client, "Flexible", ValueRanks.ScalarOrOneDimension);

    assertInstanceOf(ExtensionObject.class, node.encode(ARGUMENT));
    ExtensionObject[] encoded = (ExtensionObject[]) node.encode(ARGUMENTS);
    assertEquals(2, encoded.length);
    assertNull(encoded[1]);
    assertNull(node.encode(null));
    assertNull(node.encode(Matrix.ofNull()));
  }

  // Reading back what was written must produce the declared Java type in the declared shape,
  // including a typed array with its null position and a Matrix with its datatype id.
  @Test
  public void decodeRestoresTheDeclaredTypeInEveryShape() {
    ValueNode node = new ValueNode(client, "Any", ValueRanks.Any);

    assertEquals(ARGUMENT, node.decode(node.encode(ARGUMENT)));
    assertArrayEquals(ARGUMENTS, (Argument[]) node.decode(node.encode(ARGUMENTS)));

    Matrix decoded = (Matrix) node.decode(node.encode(argumentMatrix()));
    assertArrayEquals(argumentMatrix().getDimensions(), decoded.getDimensions());
    assertEquals(argumentMatrix().getDataTypeId(), decoded.getDataTypeId());
    assertArrayEquals(new Argument[] {null, ARGUMENT}, (Argument[]) decoded.getElements());

    assertNull(node.decode(null));
    assertNull(node.decode(Matrix.ofNull()));
    assertSame(ARGUMENT, node.decode(ARGUMENT), "already decoded values pass through");
  }

  // An empty or all-null array carries no element to derive a class from, but a wrapper still
  // casts the result to the declared array type, so the node must supply that type itself.
  @Test
  public void emptyAndAllNullArraysDecodeToTheDeclaredArrayType() {
    ValueNode node = new ValueNode(client, "Empty", ValueRanks.Any);
    Matrix allNull =
        new Matrix(
            new Argument[] {null, null},
            new int[] {1, 2},
            OpcUaDataType.ExtensionObject,
            NodeIds.Argument.expanded());

    assertEquals(Argument[].class, node.decode(new ExtensionObject[0]).getClass());
    assertEquals(Argument[].class, node.decode(new ExtensionObject[] {null}).getClass());
    assertEquals(Argument[].class, node.decode(new UaStructuredType[0]).getClass());

    Matrix decoded = (Matrix) node.decode(node.encode(allNull));
    assertEquals(Argument[].class, decoded.getElements().getClass());
    assertEquals(allNull, decoded);
  }

  static Stream<Arguments> shapes() {
    Argument[] empty = new Argument[0];
    return Stream.of(
        Arguments.of(ValueRanks.Scalar, ARGUMENT, true),
        Arguments.of(ValueRanks.Scalar, ARGUMENTS, false),
        Arguments.of(ValueRanks.Scalar, empty, false),
        Arguments.of(ValueRanks.OneDimension, ARGUMENTS, true),
        Arguments.of(ValueRanks.OneDimension, ARGUMENT, false),
        Arguments.of(ValueRanks.OneDimension, argumentMatrix(), false),
        Arguments.of(ValueRanks.ScalarOrOneDimension, argumentMatrix(), false),
        Arguments.of(ValueRanks.OneOrMoreDimensions, ARGUMENT, false),
        Arguments.of(ValueRanks.OneOrMoreDimensions, argumentMatrix(), true),
        Arguments.of(ValueRanks.Any, argumentMatrix(), true),
        Arguments.of(2, argumentMatrix(), true),
        Arguments.of(2, ARGUMENTS, false),
        Arguments.of(2, empty, true),
        Arguments.of(3, argumentMatrix(), false));
  }

  // Part 3 §5.6.2 defines which shapes a ValueRank permits. The wrapper rejects the rest before
  // a write so the caller gets an exception naming the node instead of Bad_TypeMismatch from the
  // server. The empty one-dimensional array is the wire form of an empty value of any rank.
  @ParameterizedTest
  @MethodSource("shapes")
  public void valueRankPermitsOnlyMatchingShapes(int valueRank, Object value, boolean permitted) {
    ValueNode node = new ValueNode(client, "Shape", valueRank);

    if (permitted) {
      node.decode(node.encode(value));
    } else {
      IllegalArgumentException e =
          assertThrows(IllegalArgumentException.class, () -> node.encode(value));
      assertTrue(e.getMessage().contains("Shape"), e.getMessage());
      assertTrue(e.getMessage().contains("ValueRank=" + valueRank), e.getMessage());
      assertThrows(IllegalArgumentException.class, () -> node.decode(value));
    }
  }

  // A value of the wrong structure type would otherwise be encoded and rejected by the server,
  // or decoded and fail a cast far from the node that read it.
  @Test
  public void elementsMustBeInstancesOfTheDeclaredType() {
    ValueNode node = new ValueNode(client, "Typed", ValueRanks.ScalarOrOneDimension);
    Range range = new Range(0.0, 1.0);

    IllegalArgumentException scalar =
        assertThrows(IllegalArgumentException.class, () -> node.encode(range));
    assertTrue(scalar.getMessage().contains(Argument.class.getName()), scalar.getMessage());
    assertTrue(scalar.getMessage().contains(Range.class.getName()), scalar.getMessage());

    assertThrows(
        IllegalArgumentException.class,
        () -> node.encode(new UaStructuredType[] {ARGUMENT, range}));
    assertThrows(
        IllegalArgumentException.class,
        () -> node.decode(ExtensionObject.encode(client.getStaticEncodingContext(), range)));
  }

  private static class ValueNode extends UaVariableNode {

    ValueNode(OpcUaClient client, String name, int valueRank) {
      super(
          client,
          new NodeId(1, name),
          NodeClass.Variable,
          new QualifiedName(1, name),
          LocalizedText.english(name),
          LocalizedText.NULL_VALUE,
          UInteger.MIN,
          UInteger.MIN,
          DataValue.valueOnly(Variant.NULL_VALUE),
          NodeIds.Argument,
          valueRank,
          null,
          UByte.MIN,
          UByte.MIN,
          0.0,
          false);
    }

    Object encode(Object value) {
      return encodeValue(value, Argument.class, getValueRank());
    }

    Object decode(Object value) {
      return decodeValue(value, Argument.class, getValueRank());
    }
  }
}
