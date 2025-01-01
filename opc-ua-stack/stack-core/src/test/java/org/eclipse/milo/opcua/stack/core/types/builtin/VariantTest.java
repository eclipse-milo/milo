/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
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
  @MethodSource("getBuiltinDataTypeSource")
  void getBuiltinDataType(Object input, BuiltinDataType expected) {
    assertEquals(expected, Variant.of(input).getBuiltinDataType().orElseThrow());
  }

  @Test
  void variantCanContainAbstractType() {
    Number n = 1.0f;
    Variant v = Variant.of(n);
    assertEquals(BuiltinDataType.Float, v.getBuiltinDataType().orElseThrow());
  }

  private static Stream<Arguments> getBuiltinDataTypeSource() {
    return Stream.of(
        Arguments.of(true, BuiltinDataType.Boolean),
        Arguments.of((byte) 1, BuiltinDataType.SByte),
        Arguments.of((short) 1, BuiltinDataType.Int16),
        Arguments.of(1, BuiltinDataType.Int32),
        Arguments.of(1L, BuiltinDataType.Int64),
        Arguments.of(ubyte(1), BuiltinDataType.Byte),
        Arguments.of(ushort(1), BuiltinDataType.UInt16),
        Arguments.of(uint(1), BuiltinDataType.UInt32),
        Arguments.of(ulong(1), BuiltinDataType.UInt64),
        Arguments.of(1.0f, BuiltinDataType.Float),
        Arguments.of(1.0d, BuiltinDataType.Double),
        Arguments.of("foo", BuiltinDataType.String),
        Arguments.of(ByteString.of(new byte[] {1, 2, 3}), BuiltinDataType.ByteString),
        Arguments.of(new XmlElement("<foo/>"), BuiltinDataType.XmlElement),
        Arguments.of(new NodeId(1, 1), BuiltinDataType.NodeId),
        Arguments.of(new ExpandedNodeId(ushort(1), "foo", "bar"), BuiltinDataType.ExpandedNodeId),
        Arguments.of(new StatusCode(0), BuiltinDataType.StatusCode),
        Arguments.of(new QualifiedName(1, "foo"), BuiltinDataType.QualifiedName),
        Arguments.of(new LocalizedText("foo"), BuiltinDataType.LocalizedText),
        Arguments.of(
            new ExtensionObject(ByteString.NULL_VALUE, NodeId.NULL_VALUE),
            BuiltinDataType.ExtensionObject),
        Arguments.of(new DataValue(new Variant(1)), BuiltinDataType.DataValue),
        Arguments.of(new Variant[] {new Variant(1)}, BuiltinDataType.Variant),
        Arguments.of(ApplicationType.Client, BuiltinDataType.Int32),
        Arguments.of(new XVType(1.0d, 2.0f), BuiltinDataType.ExtensionObject),
        Arguments.of(new AccessLevelExType(uint(1)), BuiltinDataType.UInt32));
  }
}
