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
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code DataSetMetaDataMapper}, the published dataset metadata derivation seam used by
 * the UADP discovery responder: identity with the metadata emitted inside whole-config {@code
 * PubSubConfig.toDataType}, the strip mode that removes only the reserved {@code 0:MiloSourceKey}
 * property, configuration version passthrough, empty-fields handling, and table-free derivation for
 * URI-form node addresses.
 */
class DataSetMetaDataMapperTest {

  private static final String URI_1 = "urn:milo:test:ns1";
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

  private static NamespaceTable table() {
    return new NamespaceTable(URI_1);
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

    DataSetMetaDataType metaData = DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false);

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

    DataSetMetaDataType metaData = DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false);

    assertEquals(wholeConfigMetaData(dataSet), metaData);

    // Node-sourced fields never carry the reserved key property.
    assertNull(propertyValue(metaData.getFields()[0].getProperties(), MILO_SOURCE_KEY));
    assertNull(propertyValue(metaData.getFields()[1].getProperties(), MILO_SOURCE_KEY));
  }

  @Test
  void seamMatchesWholeConfigMetadataForMixedDataSet() {
    PublishedDataSetConfig dataSet = mixedDataSet();

    assertEquals(
        wholeConfigMetaData(dataSet), DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false));
  }

  @Test
  void seamMatchesWholeConfigMetadataForEmptyDataSet() {
    PublishedDataSetConfig dataSet = emptyDataSet();

    assertEquals(
        wholeConfigMetaData(dataSet), DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false));
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
          DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false),
          dataSet.getName());
    }
  }

  // endregion

  // region Strip mode

  @Test
  void stripRemovesOnlyMiloSourceKeyProperty() {
    PublishedDataSetConfig dataSet = keyFieldDataSet();

    DataSetMetaDataType stripped = DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true);
    DataSetMetaDataType unstripped = DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false);

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
    assertNull(stripped.getNamespaces());
    assertNull(stripped.getStructureDataTypes());
    assertNull(stripped.getEnumDataTypes());
    assertNull(stripped.getSimpleDataTypes());
  }

  @Test
  void stripIsIdentityForNodeFieldDataSets() {
    PublishedDataSetConfig dataSet = nodeFieldDataSet();

    assertEquals(
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false),
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true));
  }

  @Test
  void stripAffectsOnlyKeySourcedFieldsInMixedDataSet() {
    PublishedDataSetConfig dataSet = mixedDataSet();

    DataSetMetaDataType stripped = DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true);
    DataSetMetaDataType unstripped = DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false);

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
        DataSetMetaDataMapper.toDataSetMetaDataType(emptyDataSet(), false);

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
    assertEquals(metaData, DataSetMetaDataMapper.toDataSetMetaDataType(emptyDataSet(), true));
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
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false).getConfigurationVersion());
    assertEquals(
        expected,
        DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true).getConfigurationVersion());

    // The builder default is (1, 1).
    PublishedDataSetConfig defaulted = PublishedDataSetConfig.builder("d-ds").build();
    assertEquals(
        new ConfigurationVersionDataType(uint(1), uint(1)),
        DataSetMetaDataMapper.toDataSetMetaDataType(defaulted, false).getConfigurationVersion());
  }

  @Test
  void fieldMetaDataWireShape() {
    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(nodeFieldDataSet(), false);

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

    // ...but metadata derivation is table-free: same dataset, no throw, in either mode.
    for (boolean strip : new boolean[] {false, true}) {
      DataSetMetaDataType metaData = DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, strip);

      assertEquals("foreign-ds", metaData.getName());
      FieldMetaData field = metaData.getFields()[0];
      assertEquals("remote", field.getName());
      assertEquals(ubyte(8), field.getBuiltInType()); // Int64
      assertEquals(NodeIds.Int64, field.getDataType());
      assertEquals(FIELD_REMOTE_ID, field.getDataSetFieldId());
    }
  }

  // endregion
}
