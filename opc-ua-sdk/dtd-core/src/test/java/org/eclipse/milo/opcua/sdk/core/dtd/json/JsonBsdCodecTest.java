/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import jakarta.xml.bind.JAXBException;
import org.eclipse.milo.opcua.sdk.core.dtd.AbstractBsdCodecTest;
import org.eclipse.milo.opcua.sdk.core.dtd.BinaryDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.opcfoundation.opcua.binaryschema.StructuredType;

@Disabled
public class JsonBsdCodecTest extends AbstractBsdCodecTest {

  public JsonBsdCodecTest() throws JAXBException {}

  @Override
  protected BinaryDataTypeCodec createCodec(
      String namespaceUri, NodeId dataTypeId, NodeId encodingId, StructuredType structuredType) {

    return new JsonObjectCodec(namespaceUri, dataTypeId, encodingId, structuredType);
  }

  @Test
  public void testFoo() {
    JsonObject foo = new JsonObject();
    foo.add("Bar", new JsonPrimitive(0));
    foo.add("Baz", new JsonPrimitive("hello"));

    BinaryDataTypeCodec codec = getCodec("Foo");

    assertRoundTrip("Foo", foo, codec);
  }

  @Test
  public void testOptionals_AllPresent() {
    JsonObject optionals = new JsonObject();
    optionals.add("OptionalInt32Specified", new JsonPrimitive(1));
    optionals.add("OptionalStringSpecified", new JsonPrimitive(1));
    optionals.add("Reserved1", new JsonPrimitive(0));
    optionals.add("OptionalInt32", new JsonPrimitive(0));
    optionals.add("OptionalString", new JsonPrimitive("hello"));

    BinaryDataTypeCodec codec = getCodec("Optionals");

    assertRoundTrip("Optionals", optionals, codec);
  }

  @Test
  public void testOptionals_OnePresent() {
    JsonObject optionals = new JsonObject();
    optionals.add("OptionalInt32Specified", new JsonPrimitive(1));
    optionals.add("OptionalStringSpecified", new JsonPrimitive(0));
    optionals.add("Reserved1", new JsonPrimitive(0));
    optionals.add("OptionalInt32", new JsonPrimitive(0));

    BinaryDataTypeCodec codec = getCodec("Optionals");

    assertRoundTrip("Optionals", optionals, codec);
  }

  @Test
  public void testOptionals_NonePresent() {
    JsonObject optionals = new JsonObject();
    optionals.add("OptionalInt32Specified", new JsonPrimitive(0));
    optionals.add("OptionalStringSpecified", new JsonPrimitive(0));
    optionals.add("Reserved1", new JsonPrimitive(0));

    BinaryDataTypeCodec codec = getCodec("Optionals");

    assertRoundTrip("Optionals", optionals, codec);
  }

  @Test
  public void testArrayContainer() {
    JsonObject arrayContainer = new JsonObject();

    JsonArray intArray = new JsonArray();
    intArray.add(1);
    intArray.add(2);
    intArray.add(3);
    arrayContainer.add("IntArray", intArray);

    arrayContainer.add("BitField", new JsonPrimitive(0b10001111));

    JsonArray stringArray = new JsonArray();
    stringArray.add("hello");
    stringArray.add("world");
    arrayContainer.add("StringArray", stringArray);

    BinaryDataTypeCodec codec = getCodec("ArrayContainer");

    assertRoundTrip("ArrayContainer", arrayContainer, codec);
  }

  @Test
  public void testBar() {
    JsonObject foo = new JsonObject();
    foo.add("Bar", new JsonPrimitive(0));
    foo.add("Baz", new JsonPrimitive("hello"));

    JsonObject bar = new JsonObject();
    bar.add("Foo", foo);
    bar.add("Int", new JsonPrimitive(1));
    bar.add("Str", new JsonPrimitive("goodbye"));

    BinaryDataTypeCodec codec = getCodec("Bar");

    assertRoundTrip("Bar", bar, codec);
  }

  @Test
  public void testScanSettings() {
    /*
    {
        "LocationTypeSpecified": 1,
        "Reserved1": 0,
        "Duration": 0.0,
        "Cycles": 0,
        "DataAvailable": false,
        "LocationType": 0
    }
    */

    JsonObject scanSettings = new JsonObject();
    scanSettings.add("LocationTypeSpecified", new JsonPrimitive(1));
    scanSettings.add("Reserved1", new JsonPrimitive(0));
    scanSettings.add("Duration", new JsonPrimitive(0.0));
    scanSettings.add("Cycles", new JsonPrimitive(0));
    scanSettings.add("DataAvailable", new JsonPrimitive(false));
    scanSettings.add("LocationType", new JsonPrimitive(0));

    BinaryDataTypeCodec codec = getCodec("ScanSettings");

    assertRoundTrip("ScanSettings", scanSettings, codec);
  }

  @Test
  public void testScanSettingsJson() {
    String json =
        """
        {
            "LocationTypeSpecified": 1,
            "Reserved1": 0,
            "Duration": 0.0,
            "Cycles": 0,
            "DataAvailable": false,
            "LocationType": 0
        }\
        """;

    JsonObject scanSettings = JsonParser.parseString(json).getAsJsonObject();

    BinaryDataTypeCodec codec = getCodec("ScanSettings");

    assertRoundTrip("ScanSettings", scanSettings, codec);
  }
}
