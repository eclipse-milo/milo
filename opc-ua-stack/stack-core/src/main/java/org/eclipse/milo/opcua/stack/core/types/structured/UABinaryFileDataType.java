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
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.36">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.36</a>
 */
public class UABinaryFileDataType extends DataTypeSchemaHeader implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15006");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15422");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=15531");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15714");

  private final @Nullable String schemaLocation;

  private final KeyValuePair @Nullable [] fileHeader;

  private final Variant body;

  public UABinaryFileDataType(
      String @Nullable [] namespaces,
      StructureDescription @Nullable [] structureDataTypes,
      EnumDescription @Nullable [] enumDataTypes,
      SimpleTypeDescription @Nullable [] simpleDataTypes,
      @Nullable String schemaLocation,
      KeyValuePair @Nullable [] fileHeader,
      Variant body) {
    super(namespaces, structureDataTypes, enumDataTypes, simpleDataTypes);
    this.schemaLocation = schemaLocation;
    this.fileHeader = fileHeader;
    this.body = body;
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

  public @Nullable String getSchemaLocation() {
    return schemaLocation;
  }

  public KeyValuePair @Nullable [] getFileHeader() {
    return fileHeader;
  }

  public Variant getBody() {
    return body;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UABinaryFileDataType that = (UABinaryFileDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getSchemaLocation(), that.getSchemaLocation());
    eqb.append(getFileHeader(), that.getFileHeader());
    eqb.append(getBody(), that.getBody());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSchemaLocation());
    hcb.append(getFileHeader());
    hcb.append(getBody());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", UABinaryFileDataType.class.getSimpleName() + "[", "]");
    joiner.add("schemaLocation='" + getSchemaLocation() + "'");
    joiner.add("fileHeader=" + java.util.Arrays.toString(getFileHeader()));
    joiner.add("body=" + getBody());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15422),
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
              "SchemaLocation",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "FileHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Body",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<UABinaryFileDataType> {
    @Override
    public Class<UABinaryFileDataType> getType() {
      return UABinaryFileDataType.class;
    }

    @Override
    public UABinaryFileDataType decodeType(EncodingContext context, UaDecoder decoder) {
      String[] namespaces = decoder.decodeStringArray("Namespaces");
      StructureDescription[] structureDataTypes =
          (StructureDescription[])
              decoder.decodeStructArray("StructureDataTypes", StructureDescription.TYPE_ID);
      EnumDescription[] enumDataTypes =
          (EnumDescription[]) decoder.decodeStructArray("EnumDataTypes", EnumDescription.TYPE_ID);
      SimpleTypeDescription[] simpleDataTypes =
          (SimpleTypeDescription[])
              decoder.decodeStructArray("SimpleDataTypes", SimpleTypeDescription.TYPE_ID);
      String schemaLocation = decoder.decodeString("SchemaLocation");
      KeyValuePair[] fileHeader =
          (KeyValuePair[]) decoder.decodeStructArray("FileHeader", KeyValuePair.TYPE_ID);
      Variant body = decoder.decodeVariant("Body");
      return new UABinaryFileDataType(
          namespaces,
          structureDataTypes,
          enumDataTypes,
          simpleDataTypes,
          schemaLocation,
          fileHeader,
          body);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, UABinaryFileDataType value) {
      encoder.encodeStringArray("Namespaces", value.getNamespaces());
      encoder.encodeStructArray(
          "StructureDataTypes", value.getStructureDataTypes(), StructureDescription.TYPE_ID);
      encoder.encodeStructArray("EnumDataTypes", value.getEnumDataTypes(), EnumDescription.TYPE_ID);
      encoder.encodeStructArray(
          "SimpleDataTypes", value.getSimpleDataTypes(), SimpleTypeDescription.TYPE_ID);
      encoder.encodeString("SchemaLocation", value.getSchemaLocation());
      encoder.encodeStructArray("FileHeader", value.getFileHeader(), KeyValuePair.TYPE_ID);
      encoder.encodeVariant("Body", value.getBody());
    }
  }
}
