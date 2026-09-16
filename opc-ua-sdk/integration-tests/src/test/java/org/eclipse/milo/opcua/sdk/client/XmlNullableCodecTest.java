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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.eclipse.milo.opcua.sdk.core.types.DynamicStructType;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicStructCodec;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlDecoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class XmlNullableCodecTest {
  // Generated Java codecs and dynamic codecs must agree on the same nullable wire fields.
  @ParameterizedTest
  @ValueSource(strings = {"true", "1"})
  void javaAndDynamicCodecsPreserveNullFields(String nil) throws Exception {
    String xml =
        """
        <Argument xmlns="http://opcfoundation.org/UA/2008/02/Types.xsd"
            xmlns:x="http://www.w3.org/2001/XMLSchema-instance">
          <Name x:nil="%s"/>
          <DataType><Identifier>i=12</Identifier></DataType>
          <ValueRank>1</ValueRank>
          <ArrayDimensions x:nil="%s"/>
          <Description><Text>after null array</Text></Description>
        </Argument>
        """
            .formatted(nil, nil);
    try (var decoder = new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, xml)) {
      Argument value = (Argument) decoder.decodeStruct("Argument", new Argument.Codec());
      assertNull(value.getName());
      assertNull(value.getArrayDimensions());
      assertEquals("after null array", value.getDescription().getText());
    }
    DataType type = mock(DataType.class);
    when(type.getDataTypeDefinition())
        .thenReturn(
            new StructureDefinition(
                NodeIds.Argument_Encoding_DefaultBinary,
                NodeIds.Structure,
                StructureType.Structure,
                new StructureField[] {
                  field("Name", NodeIds.String, -1), field("DataType", NodeIds.NodeId, -1),
                  field("ValueRank", NodeIds.Int32, -1),
                      field("ArrayDimensions", NodeIds.UInt32, 1),
                  field("Description", NodeIds.LocalizedText, -1)
                }));
    var codec = new DynamicStructCodec(type, mock(DataTypeTree.class));
    try (var decoder = new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, xml)) {
      DynamicStructType value = (DynamicStructType) decoder.decodeStruct("Argument", codec);
      assertTrue(value.getMembers().containsKey("Name"));
      assertNull(value.getMembers().get("Name"));
      assertTrue(value.getMembers().containsKey("ArrayDimensions"));
      assertNull(value.getMembers().get("ArrayDimensions"));
      assertEquals(
          "after null array", ((LocalizedText) value.getMembers().get("Description")).getText());
    }
  }

  private static StructureField field(String name, NodeId type, int rank) {
    return new StructureField(name, LocalizedText.NULL_VALUE, type, rank, null, uint(0), false);
  }
}
