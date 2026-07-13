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

import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.MILO_SOURCE_KEY;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.NULL_UUID;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.deriveBuiltInType;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.toKeyValuePairs;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.jspecify.annotations.Nullable;

/**
 * Derives the Part 14 {@link DataSetMetaDataType} describing a {@link PublishedDataSetConfig}.
 *
 * <p>This class is public only so the runtime engine (the UADP discovery responder) can reach it
 * from another package; it is an internal seam, not part of the supported API. Applications obtain
 * the same metadata as part of {@link PubSubConfig#toDataType(NamespaceTable)}.
 *
 * <p>This is the single source of truth for published dataset metadata derivation: the config
 * mapper builds the {@code PublishedDataSetDataType.dataSetMetaData} emitted by {@link
 * PubSubConfig#toDataType(NamespaceTable)} through this class.
 */
public final class DataSetMetaDataMapper {

  private DataSetMetaDataMapper() {}

  /**
   * Derive the {@link DataSetMetaDataType} describing {@code dataSet}.
   *
   * <p>With {@code stripMiloSourceKey = false} the result is identical to the {@code
   * dataSetMetaData} of the {@code PublishedDataSetDataType} emitted for {@code dataSet} by {@link
   * PubSubConfig#toDataType(NamespaceTable)}. With {@code stripMiloSourceKey = true} the reserved
   * {@code 0:MiloSourceKey} property, under which a {@link KeyFieldAddress} source round-trips
   * through the Part 14 configuration model, is omitted from {@link FieldMetaData} properties:
   * discovery announcements strip it because a remote subscriber consuming announced metadata never
   * runs the Milo config mapping, and the reserved name is not a real namespace 0 property
   * BrowseName. User-authored field properties are included either way.
   *
   * <p>Field metadata is derived from the dataset's source: {@link FieldDefinition}s for a {@link
   * PublishedDataItemsConfig} source, {@link EventFieldDefinition}s for a {@link
   * PublishedEventsConfig} source. Event fields carry no {@code 0:MiloSourceKey} property (they
   * have no field address source), so {@code stripMiloSourceKey} has no effect on them.
   *
   * <p>The metadata configuration version is the dataset's {@code (major, minor)} configuration
   * version. Per OPC UA 10000-14 §7.2.4.6.4 the ConfigurationVersion in DataSetMessage headers
   * shall match the ConfigurationVersion in the DataSetMetaData; the dataset writer runtime stamps
   * headers with the version read from the same {@link PublishedDataSetConfig} getters, so callers
   * must derive from the same (live) config generation the writers read, not from a stale snapshot.
   *
   * <p>The DataTypeSchemaHeader content of the emitted metadata (namespaces, structureDataTypes,
   * enumDataTypes, simpleDataTypes; OPC UA 10000-14 §6.2.3.2.2) is derived as follows:
   *
   * <ul>
   *   <li>Type descriptions come from the dataset's authored {@link
   *       PublishedDataSetConfig#getStructureDataTypes()}, {@link
   *       PublishedDataSetConfig#getEnumDataTypes()}, and {@link
   *       PublishedDataSetConfig#getSimpleDataTypes()}, in authored order; each list maps to {@code
   *       null} when empty.
   *   <li>The {@code namespaces} array follows OPC UA 10000-5 §12.31 (Table 283): "NamespaceIndex 0
   *       is reserved for the OPC UA namespace and it is not included in this array", and "the
   *       first entry in this array maps to NamespaceIndex 1". Per that clause the metadata-local
   *       NamespaceIndex "is used in NodeIds and QualifiedNames": every non-zero-namespace NodeId
   *       and QualifiedName contained in the emitted metadata — {@link FieldMetaData} dataTypes and
   *       property keys, and all NodeIds and description names inside the type descriptions — has
   *       its publisher-local namespace index resolved against {@code namespaceTable} and remapped
   *       to a metadata-local index backed by that array, so a remote subscriber with no access to
   *       the publisher's namespace table can resolve them. The array holds the referenced URIs in
   *       first-use order (fields in wire order, then descriptions grouped
   *       structures/enums/simples, each in authored order) and is {@code null} when no non-zero
   *       namespace is referenced.
   * </ul>
   *
   * <p>Note that per OPC UA 10000-14 §6.2.3.2.6 the ConfigurationVersion {@code majorVersion} shall
   * be updated "if the namespaces in the DataTypeSchemaHeader change"; the namespaces array derived
   * here changes only when the dataset configuration changes, and the application owns the version
   * (see {@link PublishedDataSetConfig.Builder#structureDataType(StructureDescription)}).
   *
   * @param dataSet the {@link PublishedDataSetConfig} to derive metadata for.
   * @param stripMiloSourceKey {@code true} to omit the reserved {@code 0:MiloSourceKey} property
   *     from field properties.
   * @param namespaceTable the publisher's namespace table, defining the publisher-local namespace
   *     indexes of DataType NodeIds in the dataset configuration. Consulted only for non-zero
   *     namespace indexes.
   * @return the derived {@link DataSetMetaDataType}.
   * @throws PubSubConfigValidationException if a non-zero namespace index contained in the metadata
   *     is not present in {@code namespaceTable}.
   */
  public static DataSetMetaDataType toDataSetMetaDataType(
      PublishedDataSetConfig dataSet, boolean stripMiloSourceKey, NamespaceTable namespaceTable) {

    var namespaces =
        new MetaDataNamespaces(
            namespaceTable, "publishedDataSet '%s'".formatted(dataSet.getName()));

    FieldMetaData[] fieldMetaData;
    if (dataSet.getSource() instanceof PublishedEventsConfig events) {
      fieldMetaData = eventFieldMetaData(events.getFields(), namespaces);
    } else {
      fieldMetaData = dataItemsFieldMetaData(dataSet.getFields(), stripMiloSourceKey, namespaces);
    }

    StructureDescription[] structureDataTypes =
        dataSet.getStructureDataTypes().isEmpty()
            ? null
            : dataSet.getStructureDataTypes().stream()
                .map(description -> remap(description, namespaces))
                .toArray(StructureDescription[]::new);

    EnumDescription[] enumDataTypes =
        dataSet.getEnumDataTypes().isEmpty()
            ? null
            : dataSet.getEnumDataTypes().stream()
                .map(description -> remap(description, namespaces))
                .toArray(EnumDescription[]::new);

    SimpleTypeDescription[] simpleDataTypes =
        dataSet.getSimpleDataTypes().isEmpty()
            ? null
            : dataSet.getSimpleDataTypes().stream()
                .map(description -> remap(description, namespaces))
                .toArray(SimpleTypeDescription[]::new);

    return new DataSetMetaDataType(
        namespaces.toArray(),
        structureDataTypes,
        enumDataTypes,
        simpleDataTypes,
        dataSet.getName(),
        LocalizedText.NULL_VALUE,
        fieldMetaData.length == 0 ? null : fieldMetaData,
        NULL_UUID,
        new ConfigurationVersionDataType(
            dataSet.getConfigurationVersionMajor(), dataSet.getConfigurationVersionMinor()));
  }

  private static FieldMetaData[] dataItemsFieldMetaData(
      List<FieldDefinition> fields, boolean stripMiloSourceKey, MetaDataNamespaces namespaces) {

    FieldMetaData[] fieldMetaData = new FieldMetaData[fields.size()];

    for (int i = 0; i < fields.size(); i++) {
      FieldDefinition field = fields.get(i);

      NodeId dataType = namespaces.remap(field.getDataType());

      Map<QualifiedName, Variant> properties = new LinkedHashMap<>();
      field.getProperties().forEach((name, value) -> properties.put(namespaces.remap(name), value));

      if (!stripMiloSourceKey && field.getSource() instanceof KeyFieldAddress key) {
        properties.put(MILO_SOURCE_KEY, Variant.ofString(key.key()));
      }

      fieldMetaData[i] =
          new FieldMetaData(
              field.getName(),
              LocalizedText.NULL_VALUE,
              field.isPromoted()
                  ? DataSetFieldFlags.of(DataSetFieldFlags.Field.PromotedField)
                  : DataSetFieldFlags.of(),
              deriveBuiltInType(field.getDataType()),
              dataType,
              field.getValueRank(),
              field.getArrayDimensions(),
              uint(0),
              field.getDataSetFieldId(),
              toKeyValuePairs(properties));
    }

    return fieldMetaData;
  }

  private static FieldMetaData[] eventFieldMetaData(
      List<EventFieldDefinition> fields, MetaDataNamespaces namespaces) {

    FieldMetaData[] fieldMetaData = new FieldMetaData[fields.size()];

    for (int i = 0; i < fields.size(); i++) {
      EventFieldDefinition field = fields.get(i);

      NodeId dataType = namespaces.remap(field.getDataType());

      Map<QualifiedName, Variant> properties = new LinkedHashMap<>();
      field.getProperties().forEach((name, value) -> properties.put(namespaces.remap(name), value));

      fieldMetaData[i] =
          new FieldMetaData(
              field.getName(),
              LocalizedText.NULL_VALUE,
              field.isPromoted()
                  ? DataSetFieldFlags.of(DataSetFieldFlags.Field.PromotedField)
                  : DataSetFieldFlags.of(),
              deriveBuiltInType(field.getDataType()),
              dataType,
              field.getValueRank(),
              field.getArrayDimensions(),
              uint(0),
              field.getDataSetFieldId(),
              toKeyValuePairs(properties));
    }

    return fieldMetaData;
  }

  private static StructureDescription remap(
      StructureDescription description, MetaDataNamespaces namespaces) {

    return new StructureDescription(
        namespaces.remap(description.getDataTypeId()),
        namespaces.remap(description.getName()),
        remap(description.getStructureDefinition(), namespaces));
  }

  private static StructureDefinition remap(
      StructureDefinition definition, MetaDataNamespaces namespaces) {

    StructureField[] fields = definition.getFields();
    StructureField[] remappedFields = null;
    if (fields != null) {
      remappedFields = new StructureField[fields.length];
      for (int i = 0; i < fields.length; i++) {
        StructureField field = fields[i];
        remappedFields[i] =
            new StructureField(
                field.getName(),
                field.getDescription(),
                namespaces.remap(field.getDataType()),
                field.getValueRank(),
                field.getArrayDimensions(),
                field.getMaxStringLength(),
                field.getIsOptional());
      }
    }

    return new StructureDefinition(
        namespaces.remap(definition.getDefaultEncodingId()),
        namespaces.remap(definition.getBaseDataType()),
        definition.getStructureType(),
        remappedFields);
  }

  private static EnumDescription remap(EnumDescription description, MetaDataNamespaces namespaces) {

    // an EnumDefinition contains no NodeIds or QualifiedNames
    return new EnumDescription(
        namespaces.remap(description.getDataTypeId()),
        namespaces.remap(description.getName()),
        description.getEnumDefinition(),
        description.getBuiltInType());
  }

  private static SimpleTypeDescription remap(
      SimpleTypeDescription description, MetaDataNamespaces namespaces) {

    return new SimpleTypeDescription(
        namespaces.remap(description.getDataTypeId()),
        namespaces.remap(description.getName()),
        namespaces.remap(description.getBaseDataType()),
        description.getBuiltInType());
  }

  /**
   * Collects the namespace URIs referenced by the metadata being derived and remaps publisher-local
   * namespace indexes to metadata-local ones per OPC UA 10000-5 §12.31: index 0 is the OPC UA
   * namespace and is never remapped or collected; {@code toArray()[k]} corresponds to
   * metadata-local NamespaceIndex {@code k + 1}.
   */
  private static final class MetaDataNamespaces {

    private final Map<String, UShort> indexByUri = new LinkedHashMap<>();

    private final NamespaceTable namespaceTable;
    private final String path;

    private MetaDataNamespaces(NamespaceTable namespaceTable, String path) {
      this.namespaceTable = namespaceTable;
      this.path = path;
    }

    NodeId remap(NodeId nodeId) {
      UShort index = nodeId.getNamespaceIndex();
      if (index.intValue() == 0) {
        return nodeId;
      }
      return nodeId.withNamespaceIndex(metaDataIndex(index));
    }

    QualifiedName remap(QualifiedName name) {
      UShort index = name.namespaceIndex();
      if (index.intValue() == 0) {
        return name;
      }
      return name.withNamespaceIndex(metaDataIndex(index));
    }

    private UShort metaDataIndex(UShort publisherLocalIndex) {
      String uri = namespaceTable.get(publisherLocalIndex);
      if (uri == null) {
        throw new PubSubConfigValidationException(
            "%s: namespace index %s not present in the namespace table"
                .formatted(path, publisherLocalIndex));
      }
      return indexByUri.computeIfAbsent(uri, k -> ushort(indexByUri.size() + 1));
    }

    /** The referenced URIs in first-use order, or {@code null} when none. */
    String @Nullable [] toArray() {
      return indexByUri.isEmpty() ? null : indexByUri.keySet().toArray(new String[0]);
    }
  }
}
