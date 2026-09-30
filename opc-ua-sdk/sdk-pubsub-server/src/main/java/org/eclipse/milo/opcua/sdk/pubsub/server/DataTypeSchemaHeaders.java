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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.jspecify.annotations.Nullable;

/**
 * Fills in the DataTypeSchemaHeader descriptions of published datasets from a server's {@link
 * DataTypeTree}.
 *
 * <p>OPC UA 10000-14 §6.2.3.2.2 requires the DataSetMetaData of a published dataset to describe
 * every field DataType that is neither built-in nor one of the abstract DataTypes Number, Integer,
 * UInteger, and Enumeration. The metadata mapper in {@code sdk-pubsub} announces each field's
 * BuiltInType from those descriptions, so a dataset that lacks them announces its derived,
 * enumeration, and structure fields as Variant. {@link ServerPubSub} applies this class to the
 * effective configuration at attach and before every configuration apply, so that node-backed
 * datasets announce UtcTime as DateTime, an enumeration as Int32, and a structure as
 * ExtensionObject without the application authoring the descriptions itself.
 *
 * <p>Both methods are pure functions of their arguments and idempotent. Authored descriptions win;
 * a description is added only for a DataType the dataset does not already describe, and only when
 * the tree knows the DataType and carries what the description needs. The DataTypes considered are
 * the field DataTypes plus, transitively, the member DataTypes of every structure description in
 * the header, authored or added, since §6.2.3.2.2 covers nested structures too:
 *
 * <ul>
 *   <li>a subtype of a built-in DataType gets a {@link SimpleTypeDescription} whose base DataType
 *       is the tree parent and whose BuiltInType is the first built-in ancestor;
 *   <li>a concrete subtype of Enumeration with an {@link EnumDefinition} gets an {@link
 *       EnumDescription} with BuiltInType Int32;
 *   <li>an OptionSet, a subtype of a UInteger type with an {@link EnumDefinition}, gets an {@link
 *       EnumDescription} with the BuiltInType of that UInteger type; without a definition it is
 *       described as a simple type derived from the UInteger type, which announces the same
 *       BuiltInType;
 *   <li>a concrete subtype of Structure with a {@link StructureDefinition} gets a {@link
 *       StructureDescription}.
 * </ul>
 *
 * <p>Abstract DataTypes, DataTypes the tree does not contain, and enumerations or structures whose
 * node has no {@code DataTypeDefinition} are left undescribed and are announced as Variant, the
 * BuiltInType a subscriber can always decode.
 */
final class DataTypeSchemaHeaders {

  private DataTypeSchemaHeaders() {}

  /**
   * Add the missing type descriptions of every published dataset in {@code config}.
   *
   * @param config the configuration to complete.
   * @param dataTypeTree the server's DataType hierarchy.
   * @return {@code config} itself when no dataset needed a description, otherwise a copy whose
   *     published datasets carry the added descriptions.
   */
  static PubSubConfig populate(PubSubConfig config, DataTypeTree dataTypeTree) {
    List<PublishedDataSetConfig> dataSets = config.publishedDataSets();
    var populated = new ArrayList<PublishedDataSetConfig>(dataSets.size());

    boolean changed = false;
    for (PublishedDataSetConfig dataSet : dataSets) {
      PublishedDataSetConfig populatedDataSet = populate(dataSet, dataTypeTree);
      changed |= populatedDataSet != dataSet;
      populated.add(populatedDataSet);
    }

    if (!changed) {
      return config;
    }

    PubSubConfig.Builder builder = PubSubConfig.builder().enabled(config.isEnabled());
    config.connections().forEach(builder::connection);
    populated.forEach(builder::publishedDataSet);
    config.standaloneSubscribedDataSets().forEach(builder::standaloneSubscribedDataSet);
    config.securityGroups().forEach(builder::securityGroup);
    config.defaultSecurityKeyServices().forEach(builder::defaultSecurityKeyService);
    config.properties().forEach(builder::property);
    return builder.build();
  }

  /**
   * Add the missing type descriptions for the field DataTypes of {@code dataSet}.
   *
   * @param dataSet the published dataset to complete.
   * @param dataTypeTree the server's DataType hierarchy.
   * @return {@code dataSet} itself when no description was needed, otherwise a copy carrying the
   *     added descriptions after the authored ones.
   */
  static PublishedDataSetConfig populate(
      PublishedDataSetConfig dataSet, DataTypeTree dataTypeTree) {

    Set<NodeId> described = new HashSet<>();
    for (List<? extends DataTypeDescription> descriptions :
        List.of(
            dataSet.getStructureDataTypes(),
            dataSet.getEnumDataTypes(),
            dataSet.getSimpleDataTypes())) {
      for (DataTypeDescription description : descriptions) {
        described.add(description.getDataTypeId());
      }
    }

    // field DataTypes first, then the members of authored structures; members of added
    // structures are appended as they are described
    Deque<NodeId> pending = new ArrayDeque<>(fieldDataTypes(dataSet));
    for (StructureDescription structure : dataSet.getStructureDataTypes()) {
      pending.addAll(memberDataTypes(structure));
    }

    PublishedDataSetConfig.Builder builder = null;

    while (!pending.isEmpty()) {
      NodeId dataTypeId = pending.removeFirst();

      if (OpcUaDataType.isBuiltin(dataTypeId)
          || isAbstractNonBuiltin(dataTypeId)
          || described.contains(dataTypeId)) {
        continue;
      }

      DataTypeDescription description = describe(dataTypeId, dataTypeTree);
      if (description == null) {
        continue;
      }

      if (builder == null) {
        builder = dataSet.toBuilder();
      }
      if (description instanceof StructureDescription structure) {
        builder.structureDataType(structure);
        pending.addAll(memberDataTypes(structure));
      } else if (description instanceof EnumDescription enumeration) {
        builder.enumDataType(enumeration);
      } else if (description instanceof SimpleTypeDescription simple) {
        builder.simpleDataType(simple);
      }
      described.add(dataTypeId);
    }

    return builder != null ? builder.build() : dataSet;
  }

  /** The field DataType NodeIds of {@code dataSet} in wire order (duplicates included). */
  private static List<NodeId> fieldDataTypes(PublishedDataSetConfig dataSet) {
    var dataTypes = new ArrayList<NodeId>();
    if (dataSet.getSource() instanceof PublishedEventsConfig events) {
      for (EventFieldDefinition field : events.getFields()) {
        dataTypes.add(field.getDataType());
      }
    } else {
      for (FieldDefinition field : dataSet.getFields()) {
        dataTypes.add(field.getDataType());
      }
    }
    return dataTypes;
  }

  /** The member DataType NodeIds of a structure description, in definition order. */
  private static List<NodeId> memberDataTypes(StructureDescription structure) {
    var dataTypes = new ArrayList<NodeId>();
    StructureDefinition definition = structure.getStructureDefinition();
    StructureField[] fields = definition != null ? definition.getFields() : null;
    if (fields != null) {
      for (StructureField field : fields) {
        if (field != null && field.getDataType() != null) {
          dataTypes.add(field.getDataType());
        }
      }
    }
    return dataTypes;
  }

  /** Table 7 rule 1: the abstract DataTypes that §6.2.3.2.2 excludes from the header. */
  private static boolean isAbstractNonBuiltin(NodeId dataTypeId) {
    return NodeIds.Number.equals(dataTypeId)
        || NodeIds.Integer.equals(dataTypeId)
        || NodeIds.UInteger.equals(dataTypeId)
        || NodeIds.Enumeration.equals(dataTypeId);
  }

  /**
   * Build the description of {@code dataTypeId} from the tree, or {@code null} when the tree does
   * not know the DataType, the DataType is abstract, or its node lacks the definition the
   * description requires.
   */
  private static @Nullable DataTypeDescription describe(
      NodeId dataTypeId, DataTypeTree dataTypeTree) {

    Tree<DataType> node = dataTypeTree.getTreeNode(dataTypeId);
    Tree<DataType> parent = node != null ? node.getParent() : null;
    if (node == null || parent == null) {
      return null;
    }

    DataType dataType = node.getValue();
    if (Boolean.TRUE.equals(dataType.isAbstract())) {
      return null;
    }

    DataTypeDefinition definition = dataType.getDataTypeDefinition();

    if (dataTypeTree.isStructType(dataTypeId)) {
      if (definition instanceof StructureDefinition structureDefinition) {
        return new StructureDescription(dataTypeId, dataType.getBrowseName(), structureDefinition);
      }
      return null;
    }

    if (dataTypeTree.isEnumType(dataTypeId)) {
      if (definition instanceof EnumDefinition enumDefinition) {
        return new EnumDescription(
            dataTypeId,
            dataType.getBrowseName(),
            enumDefinition,
            ubyte(OpcUaDataType.Int32.getTypeId()));
      }
      return null;
    }

    OpcUaDataType builtInType = dataTypeTree.getBuiltinType(dataTypeId);

    if (definition instanceof EnumDefinition enumDefinition) {
      // an OptionSet: a subtype of a UInteger type carrying an EnumDefinition
      return new EnumDescription(
          dataTypeId, dataType.getBrowseName(), enumDefinition, ubyte(builtInType.getTypeId()));
    }

    return new SimpleTypeDescription(
        dataTypeId,
        dataType.getBrowseName(),
        parent.getValue().getNodeId(),
        ubyte(builtInType.getTypeId()));
  }
}
