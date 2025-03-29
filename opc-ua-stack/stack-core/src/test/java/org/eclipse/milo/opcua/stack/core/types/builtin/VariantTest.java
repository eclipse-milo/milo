/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class VariantTest {

  @Test
  public void variantCanContainDataValue() {
    Variant.of(new DataValue(Variant.NULL_VALUE));
  }

  @Test
  public void variantCanContainVariantArray() {
    Variant.of(new Variant[] {new Variant(0), new Variant(1), new Variant(2)});
  }

  @Test
  public void variantCannotContainVariant() {
    assertThrows(IllegalArgumentException.class, () -> Variant.of(Variant.NULL_VALUE));
  }

  @Test
  public void variantCannotContainDiagnosticInfo() {
    assertThrows(IllegalArgumentException.class, () -> Variant.of(DiagnosticInfo.NULL_VALUE));
  }

  @Test
  public void variantCannotContainUnknownTypes() {
    assertThrows(IllegalArgumentException.class, () -> Variant.of(new Object()));
  }

  @ParameterizedTest
  @MethodSource("getDataTypeSource")
  void getDataType(Object input, OpcUaDataType expected) {
    assertEquals(expected, Variant.of(input).getDataType().orElseThrow());
  }

  @Test
  void variantCanContainAbstractType() {
    Number n = 1.0f;
    Variant v = Variant.of(n);
    assertEquals(OpcUaDataType.Float, v.getDataType().orElseThrow());
  }

  @Test
  void multiDimensionalArraysMustUseMatrix() {
    assertThrows(IllegalArgumentException.class, () -> Variant.of(new int[2][2]));
    assertThrows(IllegalArgumentException.class, () -> Variant.of(new int[2][2][2]));
  }

  @Test
  void scalarEquality() {
    assertEquals(Variant.of(1), Variant.of(1));
  }

  @Test
  void arrayEquality() {
    assertEquals(Variant.of(new int[] {1, 2, 3}), Variant.of(new int[] {1, 2, 3}));
    assertEquals(Variant.of(new Integer[] {1, 2, 3}), Variant.of(new Integer[] {1, 2, 3}));
  }

  @Test
  void primitiveBoxedEquality() {
    int[] primitive = {1, 2, 3};
    Integer[] boxed = {1, 2, 3};

    assertEquals(Variant.of(primitive), Variant.of(boxed));
    assertEquals(Variant.of(boxed), Variant.of(primitive));
  }

  private static Stream<Arguments> getDataTypeSource() {
    return Stream.of(
        Arguments.of(true, OpcUaDataType.Boolean),
        Arguments.of((byte) 1, OpcUaDataType.SByte),
        Arguments.of((short) 1, OpcUaDataType.Int16),
        Arguments.of(1, OpcUaDataType.Int32),
        Arguments.of(1L, OpcUaDataType.Int64),
        Arguments.of(ubyte(1), OpcUaDataType.Byte),
        Arguments.of(ushort(1), OpcUaDataType.UInt16),
        Arguments.of(uint(1), OpcUaDataType.UInt32),
        Arguments.of(ulong(1), OpcUaDataType.UInt64),
        Arguments.of(1.0f, OpcUaDataType.Float),
        Arguments.of(1.0d, OpcUaDataType.Double),
        Arguments.of("foo", OpcUaDataType.String),
        Arguments.of(ByteString.of(new byte[] {1, 2, 3}), OpcUaDataType.ByteString),
        Arguments.of(new XmlElement("<foo/>"), OpcUaDataType.XmlElement),
        Arguments.of(new NodeId(1, 1), OpcUaDataType.NodeId),
        Arguments.of(ExpandedNodeId.of("bar"), OpcUaDataType.ExpandedNodeId),
        Arguments.of(new StatusCode(0), OpcUaDataType.StatusCode),
        Arguments.of(new QualifiedName(1, "foo"), OpcUaDataType.QualifiedName),
        Arguments.of(new LocalizedText("foo"), OpcUaDataType.LocalizedText),
        Arguments.of(
            ExtensionObject.of(ByteString.NULL_VALUE, NodeId.NULL_VALUE),
            OpcUaDataType.ExtensionObject),
        Arguments.of(new DataValue(new Variant(1)), OpcUaDataType.DataValue),
        Arguments.of(new Variant[] {new Variant(1)}, OpcUaDataType.Variant),
        Arguments.of(ApplicationType.Client, OpcUaDataType.Int32),
        Arguments.of(new XVType(1.0d, 2.0f), OpcUaDataType.ExtensionObject),
        Arguments.of(new AccessLevelExType(uint(1)), OpcUaDataType.UInt32));
  }
}
