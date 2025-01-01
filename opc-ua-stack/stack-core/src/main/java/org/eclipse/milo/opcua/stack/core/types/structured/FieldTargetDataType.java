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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OverrideValueHandling;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.10/#6.2.10.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.10/#6.2.10.2.3</a>
 */
public class FieldTargetDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=14744");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=14848");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=14804");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15061");

  private final UUID dataSetFieldId;

  private final String receiverIndexRange;

  private final NodeId targetNodeId;

  private final UInteger attributeId;

  private final String writeIndexRange;

  private final OverrideValueHandling overrideValueHandling;

  private final Variant overrideValue;

  public FieldTargetDataType(
      UUID dataSetFieldId,
      String receiverIndexRange,
      NodeId targetNodeId,
      UInteger attributeId,
      String writeIndexRange,
      OverrideValueHandling overrideValueHandling,
      Variant overrideValue) {
    this.dataSetFieldId = dataSetFieldId;
    this.receiverIndexRange = receiverIndexRange;
    this.targetNodeId = targetNodeId;
    this.attributeId = attributeId;
    this.writeIndexRange = writeIndexRange;
    this.overrideValueHandling = overrideValueHandling;
    this.overrideValue = overrideValue;
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

  public UUID getDataSetFieldId() {
    return dataSetFieldId;
  }

  public String getReceiverIndexRange() {
    return receiverIndexRange;
  }

  public NodeId getTargetNodeId() {
    return targetNodeId;
  }

  public UInteger getAttributeId() {
    return attributeId;
  }

  public String getWriteIndexRange() {
    return writeIndexRange;
  }

  public OverrideValueHandling getOverrideValueHandling() {
    return overrideValueHandling;
  }

  public Variant getOverrideValue() {
    return overrideValue;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    FieldTargetDataType that = (FieldTargetDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getDataSetFieldId(), that.getDataSetFieldId());
    eqb.append(getReceiverIndexRange(), that.getReceiverIndexRange());
    eqb.append(getTargetNodeId(), that.getTargetNodeId());
    eqb.append(getAttributeId(), that.getAttributeId());
    eqb.append(getWriteIndexRange(), that.getWriteIndexRange());
    eqb.append(getOverrideValueHandling(), that.getOverrideValueHandling());
    eqb.append(getOverrideValue(), that.getOverrideValue());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getDataSetFieldId());
    hcb.append(getReceiverIndexRange());
    hcb.append(getTargetNodeId());
    hcb.append(getAttributeId());
    hcb.append(getWriteIndexRange());
    hcb.append(getOverrideValueHandling());
    hcb.append(getOverrideValue());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", FieldTargetDataType.class.getSimpleName() + "[", "]");
    joiner.add("dataSetFieldId=" + getDataSetFieldId());
    joiner.add("receiverIndexRange='" + getReceiverIndexRange() + "'");
    joiner.add("targetNodeId=" + getTargetNodeId());
    joiner.add("attributeId=" + getAttributeId());
    joiner.add("writeIndexRange='" + getWriteIndexRange() + "'");
    joiner.add("overrideValueHandling=" + getOverrideValueHandling());
    joiner.add("overrideValue=" + getOverrideValue());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 14848),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "DataSetFieldId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReceiverIndexRange",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 291),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TargetNodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AttributeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "WriteIndexRange",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 291),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OverrideValueHandling",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15874),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OverrideValue",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<FieldTargetDataType> {
    @Override
    public Class<FieldTargetDataType> getType() {
      return FieldTargetDataType.class;
    }

    @Override
    public FieldTargetDataType decodeType(EncodingContext context, UaDecoder decoder) {
      UUID dataSetFieldId = decoder.decodeGuid("DataSetFieldId");
      String receiverIndexRange = decoder.decodeString("ReceiverIndexRange");
      NodeId targetNodeId = decoder.decodeNodeId("TargetNodeId");
      UInteger attributeId = decoder.decodeUInt32("AttributeId");
      String writeIndexRange = decoder.decodeString("WriteIndexRange");
      OverrideValueHandling overrideValueHandling =
          OverrideValueHandling.from(decoder.decodeEnum("OverrideValueHandling"));
      Variant overrideValue = decoder.decodeVariant("OverrideValue");
      return new FieldTargetDataType(
          dataSetFieldId,
          receiverIndexRange,
          targetNodeId,
          attributeId,
          writeIndexRange,
          overrideValueHandling,
          overrideValue);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, FieldTargetDataType value) {
      encoder.encodeGuid("DataSetFieldId", value.getDataSetFieldId());
      encoder.encodeString("ReceiverIndexRange", value.getReceiverIndexRange());
      encoder.encodeNodeId("TargetNodeId", value.getTargetNodeId());
      encoder.encodeUInt32("AttributeId", value.getAttributeId());
      encoder.encodeString("WriteIndexRange", value.getWriteIndexRange());
      encoder.encodeEnum("OverrideValueHandling", value.getOverrideValueHandling());
      encoder.encodeVariant("OverrideValue", value.getOverrideValue());
    }
  }
}
