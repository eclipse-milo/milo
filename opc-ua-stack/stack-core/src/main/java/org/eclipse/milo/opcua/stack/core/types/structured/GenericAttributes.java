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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.24.10">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.24.10</a>
 */
public class GenericAttributes extends NodeAttributes implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=17607");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=17611");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=17609");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15164");

  private final GenericAttributeValue @Nullable [] attributeValues;

  public GenericAttributes(
      UInteger specifiedAttributes,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      GenericAttributeValue @Nullable [] attributeValues) {
    super(specifiedAttributes, displayName, description, writeMask, userWriteMask);
    this.attributeValues = attributeValues;
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

  public GenericAttributeValue @Nullable [] getAttributeValues() {
    return attributeValues;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    GenericAttributes that = (GenericAttributes) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getAttributeValues(), that.getAttributeValues());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAttributeValues());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", GenericAttributes.class.getSimpleName() + "[", "]");
    joiner.add("attributeValues=" + java.util.Arrays.toString(getAttributeValues()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 17611),
        new NodeId(0, 349),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SpecifiedAttributes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DisplayName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
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
              "WriteMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserWriteMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AttributeValues",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17606),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<GenericAttributes> {
    @Override
    public Class<GenericAttributes> getType() {
      return GenericAttributes.class;
    }

    @Override
    public GenericAttributes decodeType(EncodingContext context, UaDecoder decoder) {
      UInteger specifiedAttributes = decoder.decodeUInt32("SpecifiedAttributes");
      LocalizedText displayName = decoder.decodeLocalizedText("DisplayName");
      LocalizedText description = decoder.decodeLocalizedText("Description");
      UInteger writeMask = decoder.decodeUInt32("WriteMask");
      UInteger userWriteMask = decoder.decodeUInt32("UserWriteMask");
      GenericAttributeValue[] attributeValues =
          (GenericAttributeValue[])
              decoder.decodeStructArray("AttributeValues", GenericAttributeValue.TYPE_ID);
      return new GenericAttributes(
          specifiedAttributes, displayName, description, writeMask, userWriteMask, attributeValues);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, GenericAttributes value) {
      encoder.encodeUInt32("SpecifiedAttributes", value.getSpecifiedAttributes());
      encoder.encodeLocalizedText("DisplayName", value.getDisplayName());
      encoder.encodeLocalizedText("Description", value.getDescription());
      encoder.encodeUInt32("WriteMask", value.getWriteMask());
      encoder.encodeUInt32("UserWriteMask", value.getUserWriteMask());
      encoder.encodeStructArray(
          "AttributeValues", value.getAttributeValues(), GenericAttributeValue.TYPE_ID);
    }
  }
}
