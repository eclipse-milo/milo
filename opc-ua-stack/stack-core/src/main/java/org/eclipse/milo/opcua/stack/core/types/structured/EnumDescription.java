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
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.34">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.34</a>
 */
public class EnumDescription extends DataTypeDescription implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15488");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=127");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=15590");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15059");

  private final EnumDefinition enumDefinition;

  private final UByte builtInType;

  public EnumDescription(
      NodeId dataTypeId, QualifiedName name, EnumDefinition enumDefinition, UByte builtInType) {
    super(dataTypeId, name);
    this.enumDefinition = enumDefinition;
    this.builtInType = builtInType;
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

  public EnumDefinition getEnumDefinition() {
    return enumDefinition;
  }

  public UByte getBuiltInType() {
    return builtInType;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    EnumDescription that = (EnumDescription) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getEnumDefinition(), that.getEnumDefinition());
    eqb.append(getBuiltInType(), that.getBuiltInType());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getEnumDefinition());
    hcb.append(getBuiltInType());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", EnumDescription.class.getSimpleName() + "[", "]");
    joiner.add("enumDefinition=" + getEnumDefinition());
    joiner.add("builtInType=" + getBuiltInType());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 127),
        new NodeId(0, 14525),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "DataTypeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EnumDefinition",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 100),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BuiltInType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<EnumDescription> {
    @Override
    public Class<EnumDescription> getType() {
      return EnumDescription.class;
    }

    @Override
    public EnumDescription decodeType(EncodingContext context, UaDecoder decoder) {
      NodeId dataTypeId = decoder.decodeNodeId("DataTypeId");
      QualifiedName name = decoder.decodeQualifiedName("Name");
      EnumDefinition enumDefinition =
          (EnumDefinition) decoder.decodeStruct("EnumDefinition", EnumDefinition.TYPE_ID);
      UByte builtInType = decoder.decodeByte("BuiltInType");
      return new EnumDescription(dataTypeId, name, enumDefinition, builtInType);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, EnumDescription value) {
      encoder.encodeNodeId("DataTypeId", value.getDataTypeId());
      encoder.encodeQualifiedName("Name", value.getName());
      encoder.encodeStruct("EnumDefinition", value.getEnumDefinition(), EnumDefinition.TYPE_ID);
      encoder.encodeByte("BuiltInType", value.getBuiltInType());
    }
  }
}
