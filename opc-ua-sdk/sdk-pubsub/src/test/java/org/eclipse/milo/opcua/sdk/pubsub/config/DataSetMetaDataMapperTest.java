/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code DataSetMetaDataMapper}, the published dataset metadata derivation seam used by
 * the UADP discovery responder: identity with the metadata emitted inside whole-config {@code
 * PubSubConfig.toDataType}, the strip mode that removes only the reserved {@code 0:MiloSourceKey}
 * property, configuration version passthrough, empty-fields handling, source-address derivation
 * that never resolves field source URIs, event dataset field derivation, and the
 * DataTypeSchemaHeader content (namespaces array building, metadata-local namespace remapping, and
 * authored type descriptions).
 */
class DataSetMetaDataMapperTest {

  private static final String URI_1 = "urn:milo:test:ns1";
  private static final String URI_2 = "urn:milo:test:ns2";
  private static final String UNRESOLVABLE_URI = "urn:milo:test:unresolvable";

  private static final QualifiedName MILO_SOURCE_KEY = new QualifiedName(0, "MiloSourceKey");
  private static final UUID NULL_UUID = new UUID(0L, 0L);

  private static final UUID FIELD_LABEL_ID = new UUID(0xE1L, 1L);
  private static final UUID FIELD_PRESSURE_ID = new UUID(0xE1L, 2L);
  private static final UUID FIELD_TEMP_ID = new UUID(0xE2L, 1L);
  private static final UUID FIELD_ENVELOPE_ID = new UUID(0xE2L, 2L);
  private static final UUID FIELD_MIXED_NODE_ID = new UUID(0xE3L, 1L);
  private static final UUID FIELD_MIXED_KEY_ID = new UUID(0xE3L, 2L);
  private static final UUID FIELD_REMOTE_ID = new UUID(0xE4L, 1L);
  private static final UUID FIELD_EVENT_MESSAGE_ID = new UUID(0xE6L, 1L);
  private static final UUID FIELD_EVENT_SEVERITY_ID = new UUID(0xE6L, 2L);
  private static final UUID FIELD_EVENT_PAYLOAD_ID = new UUID(0xE6L, 3L);
  private static final UUID FIELD_CUSTOM_ID = new UUID(0xE7L, 1L);
  private static final UUID FIELD_XV_ID = new UUID(0xE7L, 2L);
  private static final UUID FIELD_EVENT_CUSTOM_ID = new UUID(0xE7L, 3L);

  private static NamespaceTable table() {
    return new NamespaceTable(URI_1, URI_2);
  }

  // region Dataset fixtures

  /** All KeyFieldAddress sources: explicit key with user properties, and a defaulted source. */
  private static PublishedDataSetConfig keyFieldDataSet() {
    return PublishedDataSetConfig.builder("key-ds")
        .field(
            FieldDefinition.builder("label")
                .source(KeyFieldAddress.of("label.key"))
                .dataType(NodeIds.String)
                .dataSetFieldId(FIELD_LABEL_ID)
                .property(new QualifiedName(1, "engineeringUnits"), Variant.ofString("kPa"))
                .property(new QualifiedName(2, "displayHint"), Variant.ofInt32(3))
                .build())
        .field(
            // No source: defaults to a KeyFieldAddress keyed by the field name.
            FieldDefinition.builder("pressure")
                .dataType(NodeIds.Double)
                .dataSetFieldId(FIELD_PRESSURE_ID)
                .promoted(true)
                .build())
        .configurationVersion(uint(3), uint(11))
        .build();
  }

  /** All NodeFieldAddress sources: promoted field, user property, non-builtin array field. */
  private static PublishedDataSetConfig nodeFieldDataSet() {
    return PublishedDataSetConfig.builder("node-ds")
        .field(
            FieldDefinition.builder("temperature")
                .source(
                    new NodeFieldAddress(
                        ExpandedNodeId.of(URI_1, "Sensor.Temperature"), AttributeId.Value))
                .dataType(NodeIds.Double)
                .dataSetFieldId(FIELD_TEMP_ID)
                .promoted(true)
                .property(new QualifiedName(1, "unit"), Variant.ofString("celsius"))
                .build())
        .field(
            FieldDefinition.builder("envelope")
                .source(
                    new NodeFieldAddress(
                        ExpandedNodeId.of(URI_1, uint(5001)), AttributeId.DisplayName))
                .dataType(NodeIds.Range)
                .dataSetFieldId(FIELD_ENVELOPE_ID)
                .valueRank(1)
                .arrayDimensions(new UInteger[] {uint(4)})
                .build())
        .configurationVersion(uint(2), uint(9))
        .build();
  }

  /** Node and key sources side by side. */
  private static PublishedDataSetConfig mixedDataSet() {
    return PublishedDataSetConfig.builder("mixed-ds")
        .field(
            FieldDefinition.builder("count")
                .source(
                    new NodeFieldAddress(ExpandedNodeId.of(URI_1, "Counter"), AttributeId.Value))
                .dataType(NodeIds.Int32)
                .dataSetFieldId(FIELD_MIXED_NODE_ID)
                .build())
        .field(
            FieldDefinition.builder("tag")
                .source(KeyFieldAddress.of("tag.key"))
                .dataType(NodeIds.String)
                .dataSetFieldId(FIELD_MIXED_KEY_ID)
                .promoted(true)
                .property(new QualifiedName(1, "origin"), Variant.ofString("mixed"))
                .build())
        .property(new QualifiedName(1, "dsProp"), Variant.ofInt32(5))
        .build();
  }

  private static PublishedDataSetConfig emptyDataSet() {
    return PublishedDataSetConfig.builder("empty-ds")
        .configurationVersion(uint(4), uint(2))
        .build();
  }

  /** Event source: promoted field, user properties, defaulted-dataType array field. */
  private static PublishedDataSetConfig eventDataSet() {
    return PublishedDataSetConfig.builder("event-ds")
        .source(
            PublishedEventsConfig.builder(ExpandedNodeId.of(URI_1, "Boiler.Notifier"))
                .field(
                    EventFieldDefinition.builder("message")
                        .selectedField(select("Message"))
                        .dataType(NodeIds.LocalizedText)
                        .dataSetFieldId(FIELD_EVENT_MESSAGE_ID)
                        .property(new QualifiedName(1, "origin"), Variant.ofString("event"))
                        .property(new QualifiedName(2, "displayHint"), Variant.ofInt32(7))
                        .build())
                .field(
                    EventFieldDefinition.builder("severity")
                        .selectedField(select("Severity"))
                        .dataType(NodeIds.UInt16)
                        .dataSetFieldId(FIELD_EVENT_SEVERITY_ID)
                        .promoted(true)
                        .build())
                .field(
                    // No dataType: defaults to BaseDataType, i.e. builtin type Variant.
                    EventFieldDefinition.builder("payload")
                        .selectedField(select("Payload"))
                        .dataSetFieldId(FIELD_EVENT_PAYLOAD_ID)
                        .valueRank(1)
                        .arrayDimensions(new UInteger[] {uint(3)})
                        .build())
                .build())
        .configurationVersion(uint(7), uint(21))
        .build();
  }

  // endregion

  // region Helpers

  /** The metadata emitted for {@code dataSet} inside whole-config {@code toDataType}. */
  private static DataSetMetaDataType wholeConfigMetaData(PublishedDataSetConfig dataSet) {
    PubSubConfig config = PubSubConfig.builder().publishedDataSet(dataSet).build();

    return metaDataByName(config.toDataType(table()), dataSet.getName());
  }

  private static DataSetMetaDataType metaDataByName(
      PubSubConfiguration2DataType dataType, String name) {

    PublishedDataSetDataType[] publishedDataSets = dataType.getPublishedDataSets();
    assertNotNull(publishedDataSets);
    for (PublishedDataSetDataType publishedDataSet : publishedDataSets) {
      if (name.equals(publishedDataSet.getName())) {
        return publishedDataSet.getDataSetMetaData();
      }
    }
    return fail("published dataset not found: " + name);
  }

  private static Variant propertyValue(KeyValuePair[] pairs, QualifiedName key) {
    if (pairs == null) {
      return null;
    }
    for (KeyValuePair pair : pairs) {
      if (pair != null && key.equals(pair.getKey())) {
        return pair.getValue();
      }
    }
    return null;
  }

  /** A select clause for the BaseEventType field named {@code browseName}. */
  private static SimpleAttributeOperand select(String browseName) {
    return new SimpleAttributeOperand(
        NodeIds.BaseEventType,
        new QualifiedName[] {new QualifiedName(0, browseName)},
        AttributeId.Value.uid(),
        null);
  }

  /** Asserts every {@link FieldMetaData} slot except properties is identical. */
  private static void assertSameExceptProperties(FieldMetaData expected, FieldMetaData actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getDescription(), actual.getDescription());
    assertEquals(expected.getFieldFlags(), actual.getFieldFlags());
    assertEquals(expected.getBuiltInType(), actual.getBuiltInType());
    assertEquals(expected.getDataType(), actual.getDataType());
    assertEquals(expected.getValueRank(), actual.getValueRank());
    assertArrayEquals(expected.getArrayDimensions(), actual.getArrayDimensions());
    assertEquals(expected.getMaxStringLength(), actual.getMaxStringLength());
    assertEquals(expected.getDataSetFieldId(), actual.getDataSetFieldId());
  }

  // endregion

  // region Seam identity with the whole-config mapper

  @Test
  void seamMatchesWholeConfigMetadataForKeyFieldDataSet() {
    PublishedDataSetConfig dataSet = keyFieldDataSet();

    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table());

    assertEquals(wholeConfigMetaData(dataSet), metaData);

    // Both key-sourced fields carry the reserved MiloSourceKey property in unstripped mode.
    assertEquals(
        Variant.ofString("label.key"),
        propertyValue(metaData.getFields()[0].getProperties(), MILO_SOURCE_KEY));
    assertEquals(
        Variant.ofString("pressure"),
        propertyValue(metaData.getFields()[1].getProperties(), MILO_SOURCE_KEY));
  }

  @Test
  void seamMatchesWholeConfigMetadataForNodeFieldDataSet() {
    PublishedDataSetConfig dataSet = nodeFieldDataSet();

    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table());

    assertEquals(wholeConfigMetaData(dataSet), metaData);

    // Node-sourced fields never carry the reserved key property.
    assertNull(propertyValue(metaData.getFields()[0].getProperties(), MILO_SOURCE_KEY));
    assertNull(propertyValue(metaData.getFields()[1].getProperties(), MILO_SOURCE_KEY));
  }

  @Test
  void seamMatchesWholeConfigMetadataForMixedDataSet() {
    PublishedDataSetConfig dataSet = mixedDataSet();

    assertEquals(
        wholeConfigMetaData(dataSet),
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table()));
  }

  @Test
  void seamMatchesWholeConfigMetadataForEmptyDataSet() {
    PublishedDataSetConfig dataSet = emptyDataSet();

    assertEquals(
        wholeConfigMetaData(dataSet),
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table()));
  }

  @Test
  void seamMatchesEveryDataSetInAMultiDataSetConfig() {
    PublishedDataSetConfig keyDataSet = keyFieldDataSet();
    PublishedDataSetConfig nodeDataSet = nodeFieldDataSet();
    PublishedDataSetConfig mixed = mixedDataSet();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(keyDataSet)
            .publishedDataSet(nodeDataSet)
            .publishedDataSet(mixed)
            .build();

    PubSubConfiguration2DataType dataType = config.toDataType(table());

    for (PublishedDataSetConfig dataSet : config.publishedDataSets()) {
      assertEquals(
          metaDataByName(dataType, dataSet.getName()),
          DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table()),
          dataSet.getName());
    }
  }

  // endregion

  // region Strip mode

  @Test
  void stripRemovesOnlyMiloSourceKeyProperty() {
    PublishedDataSetConfig dataSet = keyFieldDataSet();

    DataSetMetaDataType stripped =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true, table());
    DataSetMetaDataType unstripped =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table());

    // "label": user properties survive, in order; MiloSourceKey is appended last when unstripped.
    KeyValuePair unitsPair =
        new KeyValuePair(new QualifiedName(1, "engineeringUnits"), Variant.ofString("kPa"));
    KeyValuePair hintPair =
        new KeyValuePair(new QualifiedName(2, "displayHint"), Variant.ofInt32(3));

    assertArrayEquals(
        new KeyValuePair[] {unitsPair, hintPair}, stripped.getFields()[0].getProperties());
    assertArrayEquals(
        new KeyValuePair[] {
          unitsPair, hintPair, new KeyValuePair(MILO_SOURCE_KEY, Variant.ofString("label.key"))
        },
        unstripped.getFields()[0].getProperties());

    // "pressure" has no user properties: stripping leaves none at all.
    assertNull(stripped.getFields()[1].getProperties());
    assertArrayEquals(
        new KeyValuePair[] {new KeyValuePair(MILO_SOURCE_KEY, Variant.ofString("pressure"))},
        unstripped.getFields()[1].getProperties());

    // Every other field slot and the metadata header are unchanged by stripping.
    assertEquals(unstripped.getFields().length, stripped.getFields().length);
    for (int i = 0; i < unstripped.getFields().length; i++) {
      assertSameExceptProperties(unstripped.getFields()[i], stripped.getFields()[i]);
    }
    assertEquals(unstripped.getName(), stripped.getName());
    assertEquals(unstripped.getDescription(), stripped.getDescription());
    assertEquals(unstripped.getDataSetClassId(), stripped.getDataSetClassId());
    assertEquals(unstripped.getConfigurationVersion(), stripped.getConfigurationVersion());
    // The non-zero-namespace property keys put both URIs into the namespaces array (their
    // publisher-local indexes 1 and 2 coincide with the metadata-local first-use order here).
    assertArrayEquals(new String[] {URI_1, URI_2}, stripped.getNamespaces());
    assertArrayEquals(unstripped.getNamespaces(), stripped.getNamespaces());
    assertNull(stripped.getStructureDataTypes());
    assertNull(stripped.getEnumDataTypes());
    assertNull(stripped.getSimpleDataTypes());
  }

  @Test
  void stripIsIdentityForNodeFieldDataSets() {
    PublishedDataSetConfig dataSet = nodeFieldDataSet();

    assertEquals(
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table()),
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true, table()));
  }

  @Test
  void stripAffectsOnlyKeySourcedFieldsInMixedDataSet() {
    PublishedDataSetConfig dataSet = mixedDataSet();

    DataSetMetaDataType stripped =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true, table());
    DataSetMetaDataType unstripped =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table());

    // The node-sourced field is byte-identical in both modes.
    assertEquals(unstripped.getFields()[0], stripped.getFields()[0]);

    // The key-sourced field loses only MiloSourceKey; its user property survives.
    FieldMetaData strippedTag = stripped.getFields()[1];
    assertNull(propertyValue(strippedTag.getProperties(), MILO_SOURCE_KEY));
    assertEquals(
        Variant.ofString("mixed"),
        propertyValue(strippedTag.getProperties(), new QualifiedName(1, "origin")));
    assertEquals(
        Variant.ofString("tag.key"),
        propertyValue(unstripped.getFields()[1].getProperties(), MILO_SOURCE_KEY));
    assertSameExceptProperties(unstripped.getFields()[1], strippedTag);
  }

  // endregion

  // region Shape, version, and empty fields

  @Test
  void emptyFieldsMapToNullFieldsArray() {
    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(emptyDataSet(), false, table());

    assertNull(metaData.getFields());
    assertEquals("empty-ds", metaData.getName());
    assertEquals(LocalizedText.NULL_VALUE, metaData.getDescription());
    assertEquals(NULL_UUID, metaData.getDataSetClassId());
    assertNull(metaData.getNamespaces());
    assertNull(metaData.getStructureDataTypes());
    assertNull(metaData.getEnumDataTypes());
    assertNull(metaData.getSimpleDataTypes());
    assertEquals(
        new ConfigurationVersionDataType(uint(4), uint(2)), metaData.getConfigurationVersion());

    // Strip mode does not change the empty shape.
    assertEquals(
        metaData, DataSetMetaDataMapper.toDataSetMetaDataType(emptyDataSet(), true, table()));
  }

  @Test
  void configurationVersionPassesThroughInBothModes() {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("v-ds")
            .field(
                FieldDefinition.builder("f")
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(new UUID(0xE5L, 1L))
                    .build())
            .configurationVersion(uint(123456789), uint(987654321))
            .build();

    ConfigurationVersionDataType expected =
        new ConfigurationVersionDataType(uint(123456789), uint(987654321));

    assertEquals(
        expected,
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table())
            .getConfigurationVersion());
    assertEquals(
        expected,
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true, table())
            .getConfigurationVersion());

    // The builder default is (1, 1).
    PublishedDataSetConfig defaulted = PublishedDataSetConfig.builder("d-ds").build();
    assertEquals(
        new ConfigurationVersionDataType(uint(1), uint(1)),
        DataSetMetaDataMapper.toDataSetMetaDataType(defaulted, false, table())
            .getConfigurationVersion());
  }

  @Test
  void fieldMetaDataWireShape() {
    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(nodeFieldDataSet(), false, table());

    FieldMetaData[] fields = metaData.getFields();
    assertNotNull(fields);
    assertEquals(2, fields.length);

    FieldMetaData temperature = fields[0];
    assertEquals("temperature", temperature.getName());
    assertEquals(LocalizedText.NULL_VALUE, temperature.getDescription());
    assertTrue(temperature.getFieldFlags().getPromotedField());
    assertEquals(ubyte(11), temperature.getBuiltInType()); // Double
    assertEquals(NodeIds.Double, temperature.getDataType());
    assertEquals(-1, temperature.getValueRank());
    assertNull(temperature.getArrayDimensions());
    assertEquals(uint(0), temperature.getMaxStringLength());
    assertEquals(FIELD_TEMP_ID, temperature.getDataSetFieldId());
    assertEquals(
        Variant.ofString("celsius"),
        propertyValue(temperature.getProperties(), new QualifiedName(1, "unit")));

    FieldMetaData envelope = fields[1];
    assertEquals("envelope", envelope.getName());
    assertFalse(envelope.getFieldFlags().getPromotedField());
    // Range is not a builtin type: builtInType falls back to ExtensionObject (22), and the raw
    // config DataType NodeId is carried alongside.
    assertEquals(ubyte(22), envelope.getBuiltInType());
    assertEquals(NodeIds.Range, envelope.getDataType());
    assertEquals(1, envelope.getValueRank());
    assertArrayEquals(new UInteger[] {uint(4)}, envelope.getArrayDimensions());
    assertEquals(uint(0), envelope.getMaxStringLength());
    assertEquals(FIELD_ENVELOPE_ID, envelope.getDataSetFieldId());
    assertNull(envelope.getProperties());
  }

  @Test
  void uriFormNodeAddressesNeverRequireNamespaceResolution() {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("foreign-ds")
            .field(
                FieldDefinition.builder("remote")
                    .source(
                        new NodeFieldAddress(
                            ExpandedNodeId.of(UNRESOLVABLE_URI, "Remote.Node"), AttributeId.Value))
                    .dataType(NodeIds.Int64)
                    .dataSetFieldId(FIELD_REMOTE_ID)
                    .build())
            .build();

    // The whole-config mapper resolves field sources against the namespace table and throws when
    // the URI is absent...
    PubSubConfig config = PubSubConfig.builder().publishedDataSet(dataSet).build();
    assertThrows(
        PubSubConfigValidationException.class, () -> config.toDataType(new NamespaceTable()));

    // ...but metadata derivation never resolves field source addresses (the table is consulted
    // only for non-zero-namespace NodeIds contained in the metadata itself): same dataset, no
    // throw, in either mode.
    for (boolean strip : new boolean[] {false, true}) {
      DataSetMetaDataType metaData =
          DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, strip, table());

      assertEquals("foreign-ds", metaData.getName());
      FieldMetaData field = metaData.getFields()[0];
      assertEquals("remote", field.getName());
      assertEquals(ubyte(8), field.getBuiltInType()); // Int64
      assertEquals(NodeIds.Int64, field.getDataType());
      assertEquals(FIELD_REMOTE_ID, field.getDataSetFieldId());
    }
  }

  // endregion

  // region Event datasets

  @Test
  void seamMatchesWholeConfigMetadataForEventDataSet() {
    PublishedDataSetConfig dataSet = eventDataSet();

    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table());

    assertEquals(wholeConfigMetaData(dataSet), metaData);
  }

  @Test
  void eventFieldMetaDataWireShape() {
    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(eventDataSet(), false, table());

    FieldMetaData[] fields = metaData.getFields();
    assertNotNull(fields);
    assertEquals(3, fields.length);

    FieldMetaData message = fields[0];
    assertEquals("message", message.getName());
    assertEquals(LocalizedText.NULL_VALUE, message.getDescription());
    assertFalse(message.getFieldFlags().getPromotedField());
    assertEquals(ubyte(21), message.getBuiltInType()); // LocalizedText
    assertEquals(NodeIds.LocalizedText, message.getDataType());
    assertEquals(-1, message.getValueRank());
    assertNull(message.getArrayDimensions());
    assertEquals(uint(0), message.getMaxStringLength());
    assertEquals(FIELD_EVENT_MESSAGE_ID, message.getDataSetFieldId());
    // User properties pass through, in insertion order.
    assertArrayEquals(
        new KeyValuePair[] {
          new KeyValuePair(new QualifiedName(1, "origin"), Variant.ofString("event")),
          new KeyValuePair(new QualifiedName(2, "displayHint"), Variant.ofInt32(7))
        },
        message.getProperties());

    FieldMetaData severity = fields[1];
    assertEquals("severity", severity.getName());
    assertEquals(LocalizedText.NULL_VALUE, severity.getDescription());
    assertTrue(severity.getFieldFlags().getPromotedField());
    assertEquals(ubyte(5), severity.getBuiltInType()); // UInt16
    assertEquals(NodeIds.UInt16, severity.getDataType());
    assertEquals(-1, severity.getValueRank());
    assertNull(severity.getArrayDimensions());
    assertEquals(uint(0), severity.getMaxStringLength());
    assertEquals(FIELD_EVENT_SEVERITY_ID, severity.getDataSetFieldId());
    assertNull(severity.getProperties());

    FieldMetaData payload = fields[2];
    assertEquals("payload", payload.getName());
    assertFalse(payload.getFieldFlags().getPromotedField());
    // The dataType default is BaseDataType, whose builtin type is Variant (24).
    assertEquals(ubyte(24), payload.getBuiltInType());
    assertEquals(NodeIds.BaseDataType, payload.getDataType());
    assertEquals(1, payload.getValueRank());
    assertArrayEquals(new UInteger[] {uint(3)}, payload.getArrayDimensions());
    assertEquals(uint(0), payload.getMaxStringLength());
    assertEquals(FIELD_EVENT_PAYLOAD_ID, payload.getDataSetFieldId());
    assertNull(payload.getProperties());
  }

  @Test
  void eventFieldsNeverCarryMiloSourceKeyAndStripIsIdentity() {
    PublishedDataSetConfig dataSet = eventDataSet();

    DataSetMetaDataType unstripped =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table());
    DataSetMetaDataType stripped =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true, table());

    // Event fields have no field address source, so no reserved key property in unstripped
    // mode...
    for (FieldMetaData field : unstripped.getFields()) {
      assertNull(propertyValue(field.getProperties(), MILO_SOURCE_KEY), field.getName());
    }

    // ...and strip mode is a no-op.
    assertEquals(unstripped, stripped);
  }

  @Test
  void configurationVersionPassesThroughInBothModesForEventDataSets() {
    PublishedDataSetConfig dataSet = eventDataSet();

    ConfigurationVersionDataType expected = new ConfigurationVersionDataType(uint(7), uint(21));

    assertEquals(
        expected,
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table())
            .getConfigurationVersion());
    assertEquals(
        expected,
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true, table())
            .getConfigurationVersion());
  }

  // endregion

  // region DataTypeSchemaHeader: namespaces and type descriptions

  /** A hand-built definition of a custom structure living in namespace URI_2 (local index 2). */
  private static StructureDefinition customStructDefinition() {
    return new StructureDefinition(
        new NodeId(2, 3003),
        NodeIds.Structure,
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "X", LocalizedText.NULL_VALUE, NodeIds.Double, -1, null, uint(0), false),
          new StructureField(
              "Nested", LocalizedText.NULL_VALUE, new NodeId(2, 3010), -1, null, uint(0), false)
        });
  }

  @Test
  void fieldDataTypeNamespacesRemapToMetaDataLocalIndexes() {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("custom-ds")
            .field(
                FieldDefinition.builder("custom")
                    .dataType(new NodeId(2, 3001))
                    .dataSetFieldId(FIELD_CUSTOM_ID)
                    .build())
            .build();

    for (boolean strip : new boolean[] {false, true}) {
      DataSetMetaDataType metaData =
          DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, strip, table());

      // Per OPC 10000-5 §12.31 the namespaces array excludes namespace 0 and its first entry maps
      // to NamespaceIndex 1; only the referenced URI_2 appears, so publisher-local index 2 becomes
      // metadata-local index 1.
      assertArrayEquals(new String[] {URI_2}, metaData.getNamespaces());
      FieldMetaData field = metaData.getFields()[0];
      assertEquals(new NodeId(1, 3001), field.getDataType());
      assertEquals(ubyte(22), field.getBuiltInType()); // non-builtin: ExtensionObject
    }
  }

  @Test
  void generatedTypeDescriptionAuthoredViaDefinitionMethod() {
    NamespaceTable table = table();

    // The one-liner authoring path for Milo code-generated types: DataType NodeId, name, and the
    // generated definition(NamespaceTable) method.
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("xv-ds")
            .field(
                FieldDefinition.builder("xv")
                    .dataType(NodeIds.XVType)
                    .dataSetFieldId(FIELD_XV_ID)
                    .build())
            .structureDataType(
                NodeIds.XVType, new QualifiedName(0, "XVType"), XVType.definition(table))
            .build();

    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table);

    // Everything lives in namespace 0: no namespaces array, and the description is untouched.
    assertNull(metaData.getNamespaces());
    assertArrayEquals(
        new StructureDescription[] {
          new StructureDescription(
              NodeIds.XVType, new QualifiedName(0, "XVType"), XVType.definition(table))
        },
        metaData.getStructureDataTypes());
    assertNull(metaData.getEnumDataTypes());
    assertNull(metaData.getSimpleDataTypes());

    assertEquals(ubyte(22), metaData.getFields()[0].getBuiltInType());
    assertEquals(NodeIds.XVType, metaData.getFields()[0].getDataType());
  }

  @Test
  void handBuiltNonZeroNamespaceDescriptionIsFullyRemapped() {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("custom-ds")
            .field(
                FieldDefinition.builder("custom")
                    .dataType(new NodeId(2, 3001))
                    .dataSetFieldId(FIELD_CUSTOM_ID)
                    .build())
            .structureDataType(
                new NodeId(2, 3001), new QualifiedName(2, "Custom"), customStructDefinition())
            .build();

    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table());

    assertArrayEquals(new String[] {URI_2}, metaData.getNamespaces());

    StructureDescription description = metaData.getStructureDataTypes()[0];
    assertEquals(new NodeId(1, 3001), description.getDataTypeId());
    assertEquals(new QualifiedName(1, "Custom"), description.getName());

    StructureDefinition definition = description.getStructureDefinition();
    assertEquals(new NodeId(1, 3003), definition.getDefaultEncodingId());
    assertEquals(NodeIds.Structure, definition.getBaseDataType());
    assertEquals(StructureType.Structure, definition.getStructureType());

    StructureField[] fields = definition.getFields();
    assertEquals("X", fields[0].getName());
    assertEquals(NodeIds.Double, fields[0].getDataType()); // namespace 0: unchanged
    assertEquals("Nested", fields[1].getName());
    assertEquals(new NodeId(1, 3010), fields[1].getDataType());

    // The announced field DataType NodeId and the description id agree after remapping.
    assertEquals(metaData.getFields()[0].getDataType(), description.getDataTypeId());
  }

  @Test
  void namespacesCollectInFirstUseOrderFieldsBeforeDescriptions() {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("order-ds")
            .field(
                FieldDefinition.builder("custom")
                    .dataType(new NodeId(2, 3001)) // URI_2 first (wire order)
                    .dataSetFieldId(FIELD_CUSTOM_ID)
                    .build())
            .structureDataType(
                new NodeId(1, 4001), // URI_1 second (description order)
                new QualifiedName(1, "Other"),
                new StructureDefinition(
                    NodeId.NULL_VALUE, NodeIds.Structure, StructureType.Structure, null))
            .build();

    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table());

    assertArrayEquals(new String[] {URI_2, URI_1}, metaData.getNamespaces());
    assertEquals(new NodeId(1, 3001), metaData.getFields()[0].getDataType());
    assertEquals(new NodeId(2, 4001), metaData.getStructureDataTypes()[0].getDataTypeId());
  }

  @Test
  void sharedNamespaceCollapsesToOneEntry() {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("shared-ds")
            .field(
                FieldDefinition.builder("custom")
                    .dataType(new NodeId(2, 3001))
                    .dataSetFieldId(FIELD_CUSTOM_ID)
                    .build())
            .structureDataType(
                new NodeId(2, 3001), new QualifiedName(2, "Custom"), customStructDefinition())
            .enumDataType(
                new EnumDescription(
                    new NodeId(2, 4001),
                    new QualifiedName(2, "Color"),
                    new EnumDefinition(
                        new EnumField[] {
                          new EnumField(
                              0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Red")
                        }),
                    ubyte(6)))
            .simpleDataType(
                new SimpleTypeDescription(
                    new NodeId(2, 5001), new QualifiedName(2, "Meters"), NodeIds.Double, ubyte(11)))
            .build();

    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false, table());

    assertArrayEquals(new String[] {URI_2}, metaData.getNamespaces());

    EnumDescription enumDescription = metaData.getEnumDataTypes()[0];
    assertEquals(new NodeId(1, 4001), enumDescription.getDataTypeId());
    assertEquals(new QualifiedName(1, "Color"), enumDescription.getName());
    assertEquals(ubyte(6), enumDescription.getBuiltInType());
    assertEquals("Red", enumDescription.getEnumDefinition().getFields()[0].getName());

    SimpleTypeDescription simpleDescription = metaData.getSimpleDataTypes()[0];
    assertEquals(new NodeId(1, 5001), simpleDescription.getDataTypeId());
    assertEquals(new QualifiedName(1, "Meters"), simpleDescription.getName());
    assertEquals(NodeIds.Double, simpleDescription.getBaseDataType());
    assertEquals(ubyte(11), simpleDescription.getBuiltInType());
  }

  @Test
  void propertyKeyNamespacesRemapToMetaDataLocalIndexes() {
    // Only URI_2 is referenced (by a property key): it becomes metadata-local index 1, so the
    // emitted key's index differs from the authored publisher-local index 2.
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("prop-ds")
            .field(
                FieldDefinition.builder("plain")
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(FIELD_CUSTOM_ID)
                    .property(new QualifiedName(2, "hint"), Variant.ofInt32(7))
                    .build())
            .build();

    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true, table());

    assertArrayEquals(new String[] {URI_2}, metaData.getNamespaces());
    assertArrayEquals(
        new KeyValuePair[] {new KeyValuePair(new QualifiedName(1, "hint"), Variant.ofInt32(7))},
        metaData.getFields()[0].getProperties());
  }

  @Test
  void unresolvableMetaDataNamespaceIndexThrows() {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("bad-ds")
            .field(
                FieldDefinition.builder("custom")
                    .dataType(new NodeId(7, 3001))
                    .dataSetFieldId(FIELD_CUSTOM_ID)
                    .build())
            .build();

    for (boolean strip : new boolean[] {false, true}) {
      assertThrows(
          PubSubConfigValidationException.class,
          () -> DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, strip, table()));
    }
  }

  @Test
  void eventFieldDataTypeNamespacesRemapToo() {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("event-custom-ds")
            .source(
                PublishedEventsConfig.builder(ExpandedNodeId.of(URI_1, "Boiler.Notifier"))
                    .field(
                        EventFieldDefinition.builder("payload")
                            .selectedField(select("Payload"))
                            .dataType(new NodeId(2, 3001))
                            .dataSetFieldId(FIELD_EVENT_CUSTOM_ID)
                            .build())
                    .build())
            .build();

    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true, table());

    assertArrayEquals(new String[] {URI_2}, metaData.getNamespaces());
    assertEquals(new NodeId(1, 3001), metaData.getFields()[0].getDataType());
  }

  @Test
  void duplicateTypeDescriptionDataTypeIdIsRejectedAtBuild() {
    PublishedDataSetConfig.Builder builder =
        PublishedDataSetConfig.builder("dup-ds")
            .structureDataType(
                new NodeId(2, 3001), new QualifiedName(2, "Custom"), customStructDefinition())
            .simpleDataType(
                new SimpleTypeDescription(
                    new NodeId(2, 3001),
                    new QualifiedName(2, "Custom"),
                    NodeIds.Double,
                    ubyte(11)));

    assertThrows(PubSubConfigValidationException.class, builder::build);
  }

  // endregion
}
