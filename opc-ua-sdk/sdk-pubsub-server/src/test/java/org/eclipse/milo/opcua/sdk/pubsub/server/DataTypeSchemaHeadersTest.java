/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataMapper;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * {@link DataTypeSchemaHeaders} against the namespace 0 DataType hierarchy of an embedded server:
 * which field DataTypes get which description (OPC UA 10000-14 §6.2.3.2.2 and Table 7), which are
 * left alone, and that the result is idempotent and preserves the rest of the configuration.
 */
class DataTypeSchemaHeadersTest {

  private static TestPubSubServer testServer;
  private static DataTypeTree tree;

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
    tree = testServer.getServer().getDataTypeTree();
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  private static PublishedDataSetConfig dataSet(String name, NodeId... dataTypes) {
    PublishedDataSetConfig.Builder builder = PublishedDataSetConfig.builder(name);
    for (int i = 0; i < dataTypes.length; i++) {
      builder.field(
          FieldDefinition.builder("f" + i)
              .dataType(dataTypes[i])
              .dataSetFieldId(new UUID(0xF1L, i + 1))
              .build());
    }
    return builder.build();
  }

  /** A subtype of a built-in DataType gets a SimpleTypeDescription naming the built-in type. */
  @Test
  void subtypeOfBuiltInGetsSimpleTypeDescription() {
    PublishedDataSetConfig populated =
        DataTypeSchemaHeaders.populate(
            dataSet("simple", NodeIds.UtcTime, NodeIds.Duration, NodeIds.LocaleId), tree);

    assertEquals(
        List.of(
            new SimpleTypeDescription(
                NodeIds.UtcTime, new QualifiedName(0, "UtcTime"), NodeIds.DateTime, ubyte(13)),
            new SimpleTypeDescription(
                NodeIds.Duration, new QualifiedName(0, "Duration"), NodeIds.Double, ubyte(11)),
            new SimpleTypeDescription(
                NodeIds.LocaleId, new QualifiedName(0, "LocaleId"), NodeIds.String, ubyte(12))),
        populated.getSimpleDataTypes());
    assertTrue(populated.getEnumDataTypes().isEmpty());
    assertTrue(populated.getStructureDataTypes().isEmpty());
  }

  /** An enumeration gets an EnumDescription with the node's definition and BuiltInType Int32. */
  @Test
  void enumerationGetsEnumDescriptionWithInt32() {
    PublishedDataSetConfig populated =
        DataTypeSchemaHeaders.populate(dataSet("enum", NodeIds.ServerState), tree);

    assertEquals(
        List.of(
            new EnumDescription(
                NodeIds.ServerState,
                new QualifiedName(0, "ServerState"),
                ServerState.definition(),
                ubyte(6))),
        populated.getEnumDataTypes());
  }

  /**
   * The namespace 0 node of AccessLevelExType carries no DataTypeDefinition, so it cannot be
   * described as an OptionSet; as a subtype of UInt32 it is described as a simple type, which
   * announces the same BuiltInType (7) Table 7 rule 5 requires.
   */
  @Test
  void optionSetWithoutDefinitionIsDescribedAsSimpleUIntegerSubtype() {
    PublishedDataSetConfig populated =
        DataTypeSchemaHeaders.populate(dataSet("optionset", NodeIds.AccessLevelExType), tree);

    assertEquals(
        List.of(
            new SimpleTypeDescription(
                NodeIds.AccessLevelExType,
                new QualifiedName(0, "AccessLevelExType"),
                NodeIds.UInt32,
                ubyte(7))),
        populated.getSimpleDataTypes());
    assertTrue(populated.getEnumDataTypes().isEmpty());
  }

  /** A structure gets a StructureDescription carrying the node's StructureDefinition. */
  @Test
  void structureGetsStructureDescription() {
    PublishedDataSetConfig populated =
        DataTypeSchemaHeaders.populate(dataSet("struct", NodeIds.Range), tree);

    StructureDescription description = populated.getStructureDataTypes().get(0);
    assertEquals(NodeIds.Range, description.getDataTypeId());
    assertEquals(new QualifiedName(0, "Range"), description.getName());
    StructureField[] fields = description.getStructureDefinition().getFields();
    assertEquals("Low", fields[0].getName());
    assertEquals("High", fields[1].getName());
  }

  /**
   * Built-in DataTypes need no description; the abstract Number, Integer, UInteger, and Enumeration
   * are excluded by §6.2.3.2.2; other abstract DataTypes (Union) are announced as Variant per Table
   * 7 rule 1 and get none either. Nothing to add means the same instance comes back.
   */
  @Test
  void builtInAndAbstractDataTypesAreLeftAlone() {
    PublishedDataSetConfig dataSet =
        dataSet(
            "abstract",
            NodeIds.Double,
            NodeIds.BaseDataType,
            NodeIds.Number,
            NodeIds.Integer,
            NodeIds.UInteger,
            NodeIds.Enumeration,
            NodeIds.Union);

    assertSame(dataSet, DataTypeSchemaHeaders.populate(dataSet, tree));
  }

  /** A DataType the tree does not know cannot be described and is left to the Variant fallback. */
  @Test
  void unknownDataTypeIsLeftAlone() {
    PublishedDataSetConfig dataSet = dataSet("unknown", testServer.nodeId("NoSuchDataType"));

    assertSame(dataSet, DataTypeSchemaHeaders.populate(dataSet, tree));
  }

  /** An authored description is kept as authored and not duplicated. */
  @Test
  void authoredDescriptionWins() {
    SimpleTypeDescription authored =
        new SimpleTypeDescription(
            NodeIds.UtcTime, new QualifiedName(0, "MyUtcTime"), NodeIds.DateTime, ubyte(13));
    PublishedDataSetConfig dataSet =
        dataSet("authored", NodeIds.UtcTime).toBuilder().simpleDataType(authored).build();

    assertSame(dataSet, DataTypeSchemaHeaders.populate(dataSet, tree));
  }

  /** Two fields of one DataType produce one description; wire order is kept. */
  @Test
  void repeatedDataTypeIsDescribedOnceInFieldOrder() {
    PublishedDataSetConfig populated =
        DataTypeSchemaHeaders.populate(
            dataSet("repeat", NodeIds.Duration, NodeIds.UtcTime, NodeIds.Duration), tree);

    assertEquals(
        List.of(NodeIds.Duration, NodeIds.UtcTime),
        populated.getSimpleDataTypes().stream().map(SimpleTypeDescription::getDataTypeId).toList());
  }

  /** Event fields are described too; most event datasets carry a UtcTime Time field. */
  @Test
  void eventFieldsAreDescribed() {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("events")
            .source(
                PublishedEventsConfig.builder(NodeIds.Server.expanded())
                    .field(
                        EventFieldDefinition.builder("Time")
                            .selectedField(
                                new SimpleAttributeOperand(
                                    NodeIds.BaseEventType,
                                    new QualifiedName[] {new QualifiedName(0, "Time")},
                                    AttributeId.Value.uid(),
                                    null))
                            .dataType(NodeIds.UtcTime)
                            .build())
                    .build())
            .build();

    PublishedDataSetConfig populated = DataTypeSchemaHeaders.populate(dataSet, tree);

    assertEquals(NodeIds.UtcTime, populated.getSimpleDataTypes().get(0).getDataTypeId());
  }

  /** Populating an already populated dataset changes nothing. */
  @Test
  void populateIsIdempotent() {
    PublishedDataSetConfig once =
        DataTypeSchemaHeaders.populate(
            dataSet("idempotent", NodeIds.UtcTime, NodeIds.ServerState, NodeIds.Range), tree);

    assertSame(once, DataTypeSchemaHeaders.populate(once, tree));
  }

  /**
   * The point of the exercise: after population the metadata mapper announces the Table 7
   * BuiltInTypes for a node-backed dataset that authored no descriptions.
   */
  @Test
  void populatedDataSetAnnouncesTableSevenBuiltInTypes() {
    PublishedDataSetConfig populated =
        DataTypeSchemaHeaders.populate(
            dataSet(
                "announce",
                NodeIds.UtcTime,
                NodeIds.ServerState,
                NodeIds.AccessLevelExType,
                NodeIds.Range,
                NodeIds.Number),
            tree);

    DataSetMetaDataType metaData =
        DataSetMetaDataMapper.toDataSetMetaDataType(
            populated, true, testServer.getServer().getNamespaceTable());

    assertEquals(ubyte(13), metaData.getFields()[0].getBuiltInType(), "UtcTime as DateTime");
    assertEquals(ubyte(6), metaData.getFields()[1].getBuiltInType(), "ServerState as Int32");
    assertEquals(ubyte(7), metaData.getFields()[2].getBuiltInType(), "AccessLevelExType as UInt32");
    assertEquals(ubyte(22), metaData.getFields()[3].getBuiltInType(), "Range as ExtensionObject");
    assertEquals(ubyte(24), metaData.getFields()[4].getBuiltInType(), "Number stays Variant");
  }

  /** The whole-config form rebuilds only when a dataset changed and keeps every other element. */
  @Test
  void configIsRebuiltOnlyWhenADataSetChanged() {
    PubSubConnectionConfig connection =
        PubSubConnectionConfig.udp("conn")
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
            .build();

    PubSubConfig unchanged =
        PubSubConfig.builder()
            .connection(connection)
            .publishedDataSet(dataSet("plain", NodeIds.Double))
            .build();
    assertSame(unchanged, DataTypeSchemaHeaders.populate(unchanged, tree));

    PubSubConfig authored =
        PubSubConfig.builder()
            .enabled(false)
            .connection(connection)
            .publishedDataSet(dataSet("plain", NodeIds.Double))
            .publishedDataSet(dataSet("derived", NodeIds.UtcTime))
            .property(new QualifiedName(0, "prop"), Variant.ofInt32(1))
            .build();

    PubSubConfig populated = DataTypeSchemaHeaders.populate(authored, tree);

    assertNotSame(authored, populated);
    assertEquals(authored.isEnabled(), populated.isEnabled());
    assertEquals(authored.connections(), populated.connections());
    assertEquals(authored.properties(), populated.properties());
    assertSame(
        authored.publishedDataSets().get(0),
        populated.publishedDataSets().get(0),
        "untouched dataset reused");
    assertEquals(
        NodeIds.UtcTime,
        populated.publishedDataSets().get(1).getSimpleDataTypes().get(0).getDataTypeId());
  }
}
