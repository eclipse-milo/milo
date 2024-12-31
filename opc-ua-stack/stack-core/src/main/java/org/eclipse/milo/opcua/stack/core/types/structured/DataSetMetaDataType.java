/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.2</a>
 */
public class DataSetMetaDataType extends DataTypeSchemaHeader implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=14523");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=124");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=14794");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15050");

  private final @Nullable String name;

  private final LocalizedText description;

  private final FieldMetaData @Nullable [] fields;

  private final UUID dataSetClassId;

  private final ConfigurationVersionDataType configurationVersion;

  public DataSetMetaDataType(
      String @Nullable [] namespaces,
      StructureDescription @Nullable [] structureDataTypes,
      EnumDescription @Nullable [] enumDataTypes,
      SimpleTypeDescription @Nullable [] simpleDataTypes,
      @Nullable String name,
      LocalizedText description,
      FieldMetaData @Nullable [] fields,
      UUID dataSetClassId,
      ConfigurationVersionDataType configurationVersion) {
    super(namespaces, structureDataTypes, enumDataTypes, simpleDataTypes);
    this.name = name;
    this.description = description;
    this.fields = fields;
    this.dataSetClassId = dataSetClassId;
    this.configurationVersion = configurationVersion;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    return BINARY_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  public @Nullable String getName() {
    return name;
  }

  public LocalizedText getDescription() {
    return description;
  }

  public FieldMetaData @Nullable [] getFields() {
    return fields;
  }

  public UUID getDataSetClassId() {
    return dataSetClassId;
  }

  public ConfigurationVersionDataType getConfigurationVersion() {
    return configurationVersion;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DataSetMetaDataType that = (DataSetMetaDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getName(), that.getName());
    eqb.append(getDescription(), that.getDescription());
    eqb.append(getFields(), that.getFields());
    eqb.append(getDataSetClassId(), that.getDataSetClassId());
    eqb.append(getConfigurationVersion(), that.getConfigurationVersion());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getName());
    hcb.append(getDescription());
    hcb.append(getFields());
    hcb.append(getDataSetClassId());
    hcb.append(getConfigurationVersion());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DataSetMetaDataType.class.getSimpleName() + "[", "]");
    joiner.add("name='" + getName() + "'");
    joiner.add("description=" + getDescription());
    joiner.add("fields=" + java.util.Arrays.toString(getFields()));
    joiner.add("dataSetClassId=" + getDataSetClassId());
    joiner.add("configurationVersion=" + getConfigurationVersion());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 124),
        new NodeId(0, 15534),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Namespaces",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "StructureDataTypes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15487),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EnumDataTypes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15488),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SimpleDataTypes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15005),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Description",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Fields",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14524),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetClassId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ConfigurationVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14593),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DataSetMetaDataType> {
    @Override
    public Class<DataSetMetaDataType> getType() {
      return DataSetMetaDataType.class;
    }

    @Override
    public DataSetMetaDataType decodeType(EncodingContext context, UaDecoder decoder) {
      String[] namespaces = decoder.decodeStringArray("Namespaces");
      StructureDescription[] structureDataTypes =
          (StructureDescription[])
              decoder.decodeStructArray("StructureDataTypes", StructureDescription.TYPE_ID);
      EnumDescription[] enumDataTypes =
          (EnumDescription[]) decoder.decodeStructArray("EnumDataTypes", EnumDescription.TYPE_ID);
      SimpleTypeDescription[] simpleDataTypes =
          (SimpleTypeDescription[])
              decoder.decodeStructArray("SimpleDataTypes", SimpleTypeDescription.TYPE_ID);
      String name = decoder.decodeString("Name");
      LocalizedText description = decoder.decodeLocalizedText("Description");
      FieldMetaData[] fields =
          (FieldMetaData[]) decoder.decodeStructArray("Fields", FieldMetaData.TYPE_ID);
      UUID dataSetClassId = decoder.decodeGuid("DataSetClassId");
      ConfigurationVersionDataType configurationVersion =
          (ConfigurationVersionDataType)
              decoder.decodeStruct("ConfigurationVersion", ConfigurationVersionDataType.TYPE_ID);
      return new DataSetMetaDataType(
          namespaces,
          structureDataTypes,
          enumDataTypes,
          simpleDataTypes,
          name,
          description,
          fields,
          dataSetClassId,
          configurationVersion);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, DataSetMetaDataType value) {
      encoder.encodeStringArray("Namespaces", value.getNamespaces());
      encoder.encodeStructArray(
          "StructureDataTypes", value.getStructureDataTypes(), StructureDescription.TYPE_ID);
      encoder.encodeStructArray("EnumDataTypes", value.getEnumDataTypes(), EnumDescription.TYPE_ID);
      encoder.encodeStructArray(
          "SimpleDataTypes", value.getSimpleDataTypes(), SimpleTypeDescription.TYPE_ID);
      encoder.encodeString("Name", value.getName());
      encoder.encodeLocalizedText("Description", value.getDescription());
      encoder.encodeStructArray("Fields", value.getFields(), FieldMetaData.TYPE_ID);
      encoder.encodeGuid("DataSetClassId", value.getDataSetClassId());
      encoder.encodeStruct(
          "ConfigurationVersion",
          value.getConfigurationVersion(),
          ConfigurationVersionDataType.TYPE_ID);
    }
  }
}
