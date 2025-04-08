/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd;

import jakarta.xml.bind.JAXBException;
import org.eclipse.milo.opcua.sdk.core.dtd.generic.Struct;
import org.eclipse.milo.opcua.sdk.core.dtd.generic.StructCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.opcfoundation.opcua.binaryschema.StructuredType;

@Disabled
public class GenericBsdCodecTest extends AbstractBsdCodecTest {

  public GenericBsdCodecTest() throws JAXBException {}

  @Override
  protected BinaryDataTypeCodec createCodec(
      String namespaceUri, NodeId dataTypeId, NodeId encodingId, StructuredType structuredType) {

    return new StructCodec(namespaceUri, dataTypeId, encodingId, structuredType);
  }

  @Test
  public void testFoo() {
    Struct foo = Struct.builder("Foo").addMember("Bar", 0).addMember("Baz", "hello").build();

    BinaryDataTypeCodec codec = getCodec("Foo");

    assertRoundTrip("Foo", foo, codec);
  }

  @Test
  public void testOptionals_AllPresent() {
    Struct optionals =
        Struct.builder("Optionals")
            .addMember("OptionalInt32Specified", 1)
            .addMember("OptionalStringSpecified", 1)
            .addMember("Reserved1", 0)
            .addMember("OptionalInt32", 0)
            .addMember("OptionalString", "hello")
            .build();

    BinaryDataTypeCodec codec = getCodec("Optionals");

    assertRoundTrip("Optionals", optionals, codec);
  }

  @Test
  public void testOptionals_OnePresent() {
    Struct optionals =
        Struct.builder("Optionals")
            .addMember("OptionalInt32Specified", 1)
            .addMember("OptionalStringSpecified", 0)
            .addMember("Reserved1", 0)
            .addMember("OptionalInt32", 0)
            .build();

    BinaryDataTypeCodec codec = getCodec("Optionals");

    assertRoundTrip("Optionals", optionals, codec);
  }

  @Test
  public void testOptionals_NonePresent() {
    Struct optionals =
        Struct.builder("Optionals")
            .addMember("OptionalInt32Specified", 0)
            .addMember("OptionalStringSpecified", 0)
            .addMember("Reserved1", 0)
            .build();

    BinaryDataTypeCodec codec = getCodec("Optionals");

    assertRoundTrip("Optionals", optionals, codec);
  }

  @Test
  public void testArrayContainer() {
    Struct arrayContainer =
        Struct.builder("ArrayContainer")
            .addMember("IntArray", new Integer[] {1, 2, 3})
            .addMember("BitField", 0b10001111)
            .addMember("StringArray", new String[] {"hello", "world"})
            .build();

    BinaryDataTypeCodec codec = getCodec("ArrayContainer");

    assertRoundTrip("ArrayContainer", arrayContainer, codec);
  }

  @Test
  public void testNestedUaStruct() {
    Struct profilePoint =
        Struct.builder("ProfilePointStruct")
            .addMember("rangeXSpecified", 1)
            .addMember("rangeYSpecified", 0)
            .addMember("Reserved1", 0)
            .addMember("x", 1.0)
            .addMember("y", 2.0)
            .addMember("rangeX", new Range(3.0, 4.0))
            .build();

    BinaryDataTypeCodec codec = getCodec("ProfilePointStruct");

    assertRoundTripUsingToString("ProfilePointStruct", profilePoint, codec);
  }
}
