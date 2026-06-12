/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.json;

import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.decode;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedMetaData;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.junit.jupiter.api.Test;

/**
 * Pins the {@code ua-metadata} ConfigurationVersion normalization against captured ground truth: a
 * v1.05 CompactEncoding publisher may legally omit the all-default {@code ConfigurationVersion}
 * member, which the stock struct decoder would otherwise reject, so the decoder synthesizes (0, 0)
 * for an omitted or JSON-null member. Also pins the intentional residual strictness (unknown vendor
 * members inside {@code MetaData} still fail decode) and that the normalization did not leak into
 * the data plane.
 */
class JsonMetaDataNormalizationTest {

  /**
   * Verbatim {@code ua-metadata} document captured 2026-06-12 from Prosys OPC UA Simulation Server
   * (OPC UA 1.05, MQTT/JSON): the SampleVariableDataSet announcement, with no {@code
   * ConfigurationVersion} member inside {@code MetaData}.
   */
  private static final String PROSYS_VARIABLE_METADATA =
      """
      {
        "MessageId": "def3faa3-6d6e-4103-956d-53fcaafefbf3",
        "MessageType": "ua-metadata",
        "PublisherId": "urn:DV-kevin.herron-MBP:OPCUA:SimulationServer",
        "DataSetWriterId": 1,
        "Timestamp": "2026-06-12T10:56:11.233Z",
        "MetaData": {
          "Name": "SampleVariableDataSet",
          "Fields": [
            {
              "Name": "Counter",
              "Description": {},
              "BuiltInType": 6,
              "DataType": "i=6",
              "ValueRank": -1,
              "DataSetFieldId": "9711e0b6-9ed0-4a00-99a5-200220929af1"
            },
            {
              "Name": "Random",
              "Description": {},
              "BuiltInType": 11,
              "DataType": "i=11",
              "ValueRank": -1,
              "DataSetFieldId": "b27abacb-6b55-4317-8eae-ea0cc5626b66"
            },
            {
              "Name": "Sawtooth",
              "Description": {},
              "BuiltInType": 11,
              "DataType": "i=11",
              "ValueRank": -1,
              "DataSetFieldId": "cc889fbb-c39b-4985-9b7f-9d91446bed14"
            },
            {
              "Name": "Sinusoid",
              "Description": {},
              "BuiltInType": 11,
              "DataType": "i=11",
              "ValueRank": -1,
              "DataSetFieldId": "1c27d6bd-16a7-4f6f-9259-b833eb5f8b05"
            },
            {
              "Name": "Square",
              "Description": {},
              "BuiltInType": 11,
              "DataType": "i=11",
              "ValueRank": -1,
              "DataSetFieldId": "f9ab4c46-04d7-4d1c-8a9f-15f4257d3859"
            },
            {
              "Name": "Triangle",
              "Description": {},
              "BuiltInType": 11,
              "DataType": "i=11",
              "ValueRank": -1,
              "DataSetFieldId": "cd3549b8-2f74-459c-a0b2-5d250be83a13"
            }
          ]
        }
      }
      """;

  /**
   * Verbatim {@code ua-metadata} document from the same 2026-06-12 Prosys 1.05 capture: the
   * SampleEventDataSet announcement (writer 2), also without {@code ConfigurationVersion}.
   */
  private static final String PROSYS_EVENT_METADATA =
      """
      {
        "MessageId": "2179b50c-5fe6-417d-a51b-e3a7639b1004",
        "MessageType": "ua-metadata",
        "PublisherId": "urn:DV-kevin.herron-MBP:OPCUA:SimulationServer",
        "DataSetWriterId": 2,
        "Timestamp": "2026-06-12T10:56:11.230Z",
        "MetaData": {
          "Name": "SampleEventDataSet",
          "Fields": [
            {
              "Name": "ActiveState",
              "Description": {},
              "BuiltInType": 21,
              "DataType": "i=21",
              "ValueRank": -1,
              "DataSetFieldId": "247bbb07-7fae-4a25-b8d5-6dfdaa044eda"
            },
            {
              "Name": "AckedState",
              "Description": {},
              "BuiltInType": 21,
              "DataType": "i=21",
              "ValueRank": -1,
              "DataSetFieldId": "d5c62390-1123-46c1-a1f6-6ed4e000f881"
            },
            {
              "Name": "SourceName",
              "Description": {},
              "BuiltInType": 12,
              "DataType": "i=12",
              "ValueRank": -1,
              "DataSetFieldId": "25309cab-6136-4ece-8c9d-80aa609e0939"
            },
            {
              "Name": "Severity",
              "Description": {},
              "BuiltInType": 5,
              "DataType": "i=5",
              "ValueRank": -1,
              "DataSetFieldId": "300ab1de-415e-4f25-addf-0c2090f161a3"
            },
            {
              "Name": "Message",
              "Description": {},
              "BuiltInType": 21,
              "DataType": "i=21",
              "ValueRank": -1,
              "DataSetFieldId": "9ae86dd1-d3d7-4cd1-8c7e-d23c3305a4cf"
            },
            {
              "Name": "Time",
              "Description": {},
              "BuiltInType": 13,
              "DataType": "i=294",
              "ValueRank": -1,
              "DataSetFieldId": "af472a3b-6071-4d28-a95f-b0d8dd886991"
            }
          ]
        }
      }
      """;

  /** Verbatim {@code ua-data} document from the same 2026-06-12 Prosys 1.05 capture. */
  private static final String PROSYS_DATA =
      """
      {
        "MessageId": "d186ef7b-27f5-4081-b20c-b0da02ea9760",
        "MessageType": "ua-data",
        "PublisherId": "urn:DV-kevin.herron-MBP:OPCUA:SimulationServer",
        "Messages": [
          {
            "DataSetWriterId": 1,
            "Timestamp": "2026-06-12T10:56:21.897Z",
            "Payload": {
              "Counter": 14,
              "Random": 1.122459,
              "Sawtooth": 0.4,
              "Sinusoid": -1.902113,
              "Triangle": -1.6
            }
          }
        ]
      }
      """;

  @Test
  void prosysVariableMetaDataDecodes() throws UaException {
    DecodedNetworkMessage decoded = decode(PROSYS_VARIABLE_METADATA);

    assertEquals(1, decoded.metaData().size());
    DecodedMetaData announcement = decoded.metaData().get(0);
    assertEquals(ushort(1), announcement.dataSetWriterId());

    DataSetMetaDataType metaData = announcement.metaData();
    assertEquals("SampleVariableDataSet", metaData.getName());

    FieldMetaData[] fields = metaData.getFields();
    assertNotNull(fields);
    assertEquals(6, fields.length);

    String[] names = {"Counter", "Random", "Sawtooth", "Sinusoid", "Square", "Triangle"};
    int[] builtInTypes = {6, 11, 11, 11, 11, 11};
    String[] fieldIds = {
      "9711e0b6-9ed0-4a00-99a5-200220929af1",
      "b27abacb-6b55-4317-8eae-ea0cc5626b66",
      "cc889fbb-c39b-4985-9b7f-9d91446bed14",
      "1c27d6bd-16a7-4f6f-9259-b833eb5f8b05",
      "f9ab4c46-04d7-4d1c-8a9f-15f4257d3859",
      "cd3549b8-2f74-459c-a0b2-5d250be83a13"
    };

    for (int i = 0; i < fields.length; i++) {
      assertEquals(names[i], fields[i].getName());
      assertEquals(ubyte(builtInTypes[i]), fields[i].getBuiltInType());
      // the string-form DataType NodeIds ("i=6"/"i=11") resolve to the standard NodeIds
      assertEquals(builtInTypes[i] == 6 ? NodeIds.Int32 : NodeIds.Double, fields[i].getDataType());
      assertEquals(UUID.fromString(fieldIds[i]), fields[i].getDataSetFieldId());
    }

    assertEquals(
        new ConfigurationVersionDataType(uint(0), uint(0)), metaData.getConfigurationVersion());
  }

  @Test
  void prosysEventMetaDataDecodes() throws UaException {
    DecodedNetworkMessage decoded = decode(PROSYS_EVENT_METADATA);

    assertEquals(1, decoded.metaData().size());
    DecodedMetaData announcement = decoded.metaData().get(0);
    assertEquals(ushort(2), announcement.dataSetWriterId());

    DataSetMetaDataType metaData = announcement.metaData();
    assertEquals("SampleEventDataSet", metaData.getName());

    FieldMetaData[] fields = metaData.getFields();
    assertNotNull(fields);
    assertEquals(6, fields.length);

    String[] names = {"ActiveState", "AckedState", "SourceName", "Severity", "Message", "Time"};
    for (int i = 0; i < fields.length; i++) {
      assertEquals(names[i], fields[i].getName());
    }
  }

  @Test
  void nullConfigurationVersionDecodesAsDefault() throws UaException {
    DataSetMetaDataType metaData = decodeMetaData(withConfigurationVersion(JsonNull.INSTANCE));

    assertEquals(
        new ConfigurationVersionDataType(uint(0), uint(0)), metaData.getConfigurationVersion());
  }

  @Test
  void explicitConfigurationVersionIsNotTouched() throws UaException {
    var version = new JsonObject();
    version.addProperty("MajorVersion", 5);
    version.addProperty("MinorVersion", 7);

    DataSetMetaDataType metaData = decodeMetaData(withConfigurationVersion(version));

    assertEquals(
        new ConfigurationVersionDataType(uint(5), uint(7)), metaData.getConfigurationVersion());
  }

  @Test
  void emptyObjectConfigurationVersionDecodesAsDefault() throws UaException {
    DataSetMetaDataType metaData = decodeMetaData(withConfigurationVersion(new JsonObject()));

    assertEquals(
        new ConfigurationVersionDataType(uint(0), uint(0)), metaData.getConfigurationVersion());
  }

  @Test
  void unknownMemberInsideMetaDataStillFailsDecode() {
    // residual strictness, intentional: normalization covers only the omitted/null
    // ConfigurationVersion member; unknown vendor members inside MetaData still fail decode
    JsonObject document = JsonParser.parseString(PROSYS_VARIABLE_METADATA).getAsJsonObject();
    document.getAsJsonObject("MetaData").addProperty("VendorExtra", 1);

    UaException e = assertThrows(UaException.class, () -> decode(document.toString()));
    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().getValue());
  }

  @Test
  void dataPlaneDecodingIsUntouched() throws UaException {
    DecodedNetworkMessage decoded = decode(PROSYS_DATA);

    assertTrue(decoded.metaData().isEmpty());
    assertEquals(1, decoded.messages().size());

    DecodedDataSetMessage message = decoded.messages().get(0);
    assertEquals(ushort(1), message.dataSetWriterId());
    assertEquals(5, message.fields().size());
    assertEquals("Counter", message.fields().get(0).fieldName());
  }

  /** The golden Prosys document with {@code version} inserted as {@code ConfigurationVersion}. */
  private static String withConfigurationVersion(JsonElement version) {
    JsonObject document = JsonParser.parseString(PROSYS_VARIABLE_METADATA).getAsJsonObject();
    document.getAsJsonObject("MetaData").add("ConfigurationVersion", version);
    return document.toString();
  }

  private static DataSetMetaDataType decodeMetaData(String json) throws UaException {
    DecodedNetworkMessage decoded = decode(json);
    assertEquals(1, decoded.metaData().size());
    return decoded.metaData().get(0).metaData();
  }
}
