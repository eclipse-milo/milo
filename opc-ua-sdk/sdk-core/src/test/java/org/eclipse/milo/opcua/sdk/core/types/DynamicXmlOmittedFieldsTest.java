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

import com.digitalpetri.opcua.test.types.StructWithOptionalScalarFields;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.types.util.DynamicEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlDecoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DynamicXmlOmittedFieldsTest {
  private final DynamicEncodingContext context = new DynamicEncodingContext();

  // Dynamic codecs must receive the same trailing scalar defaults as generated Java codecs.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "<Value><Boolean>true</Boolean></Value>",
        "<Value>\n <Boolean>true</Boolean>\n</Value>"
      })
  void defaultsTrailingScalarFields(String xml) throws Exception {
    try (var decoder = new OpcUaXmlDecoder(context, xml)) {
      var value =
          (DynamicStructType)
              decoder.decodeStruct(null, context.structWithBuiltinScalarFields.getNodeId());
      assertEquals(true, value.getMembers().get("Boolean"));
      assertEquals(0, value.getMembers().get("Int32"));
      assertEquals(LocalizedText.NULL_VALUE, value.getMembers().get("LocalizedText"));
    }
  }

  // Entirely empty dynamic structures must still contain their mandatory members.
  @ParameterizedTest
  @ValueSource(strings = {"<Value/>", "<Value>\n <!-- empty -->\n</Value>"})
  void defaultsEmptyScalarStructure(String xml) throws Exception {
    try (var decoder = new OpcUaXmlDecoder(context, xml)) {
      var value =
          (DynamicStructType)
              decoder.decodeStruct(null, context.structWithBuiltinScalarFields.getNodeId());
      assertEquals(false, value.getMembers().get("Boolean"));
      assertEquals(0.0, value.getMembers().get("Double"));
      assertEquals(LocalizedText.NULL_VALUE, value.getMembers().get("LocalizedText"));
    }
  }

  // Missing trailing arrays must not be confused with explicitly empty arrays.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  "})
  void defaultsTrailingArrayFields(String gap) throws Exception {
    try (var decoder =
        new OpcUaXmlDecoder(context, "<Value>" + gap + "<Boolean/>" + gap + "</Value>")) {
      var value =
          (DynamicStructType)
              decoder.decodeStruct(null, context.structWithBuiltinArrayFields.getNodeId());
      assertArrayEquals(new Boolean[0], (Boolean[]) value.getMembers().get("Boolean"));
      assertTrue(value.getMembers().containsKey("Int32"));
      assertNull(value.getMembers().get("Int32"));
    }
  }

  // Part 6 §5.3.6: mandatory defaults and optional presence are separate decisions.
  @ParameterizedTest
  @ValueSource(
      strings = {"", "<EncodingMask>0</EncodingMask>", "\n <EncodingMask>0</EncodingMask>\n"})
  void defaultsMandatoryFieldsAndLeavesOptionalFieldsAbsent(String fields) throws Exception {
    String xml = "<Value>" + fields + "</Value>";
    try (var decoder = new OpcUaXmlDecoder(context, xml)) {
      var value =
          (DynamicStructType)
              decoder.decodeStruct(null, context.structWithOptionalScalarFields.getNodeId());
      assertEquals(
          Set.of("String", "Int32", "Duration", "ConcreteTestType"), value.getMembers().keySet());
      assertEquals(0, value.getMembers().get("Int32"));
      assertEquals(0.0, value.getMembers().get("Duration"));
      assertNull(value.getMembers().get("ConcreteTestType"));
    }
    try (var decoder = new OpcUaXmlDecoder(context, xml)) {
      var value =
          (StructWithOptionalScalarFields)
              decoder.decodeStruct(null, new StructWithOptionalScalarFields.Codec());
      assertEquals(0, value.getInt32());
      assertEquals(0.0, value.getDuration());
      assertNull(value.getOptionalInt32());
      assertNull(value.getConcreteTestType());
    }
  }

  // An explicitly selected optional field can itself use its scalar default.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "<Value><EncodingMask>2</EncodingMask></Value>",
        "<Value>\n <EncodingMask>2</EncodingMask>\n</Value>"
      })
  void defaultsSelectedOptionalField(String xml) throws Exception {
    try (var decoder = new OpcUaXmlDecoder(context, xml)) {
      var value =
          (DynamicStructType)
              decoder.decodeStruct(null, context.structWithOptionalScalarFields.getNodeId());
      assertEquals(0, value.getMembers().get("OptionalInt32"));
      assertFalse(value.getMembers().containsKey("OptionalString"));
    }
    try (var decoder = new OpcUaXmlDecoder(context, xml)) {
      var value =
          (StructWithOptionalScalarFields)
              decoder.decodeStruct(null, new StructWithOptionalScalarFields.Codec());
      assertEquals(0, value.getOptionalInt32());
      assertNull(value.getOptionalString());
    }
  }
}
