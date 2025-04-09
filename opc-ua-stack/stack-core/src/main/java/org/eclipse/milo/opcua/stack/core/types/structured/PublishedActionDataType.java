/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.4">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.4</a>
 */
public class PublishedActionDataType extends PublishedDataSetSourceDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=18594");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=18599");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=18611");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=18623");

  private final DataSetMetaDataType requestDataSetMetaData;

  private final ActionTargetDataType @Nullable [] actionTargets;

  public PublishedActionDataType(
      DataSetMetaDataType requestDataSetMetaData, ActionTargetDataType @Nullable [] actionTargets) {
    this.requestDataSetMetaData = requestDataSetMetaData;
    this.actionTargets = actionTargets;
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

  public DataSetMetaDataType getRequestDataSetMetaData() {
    return requestDataSetMetaData;
  }

  public ActionTargetDataType @Nullable [] getActionTargets() {
    return actionTargets;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PublishedActionDataType that = (PublishedActionDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestDataSetMetaData(), that.getRequestDataSetMetaData());
    eqb.append(getActionTargets(), that.getActionTargets());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestDataSetMetaData());
    hcb.append(getActionTargets());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PublishedActionDataType.class.getSimpleName() + "[", "]");
    joiner.add("requestDataSetMetaData=" + getRequestDataSetMetaData());
    joiner.add("actionTargets=" + java.util.Arrays.toString(getActionTargets()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 18599),
        new NodeId(0, 15580),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "RequestDataSetMetaData",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14523),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ActionTargets",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18593),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PublishedActionDataType> {
    @Override
    public Class<PublishedActionDataType> getType() {
      return PublishedActionDataType.class;
    }

    @Override
    public PublishedActionDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final DataSetMetaDataType requestDataSetMetaData;
      final ActionTargetDataType[] actionTargets;
      requestDataSetMetaData =
          (DataSetMetaDataType)
              decoder.decodeStruct("RequestDataSetMetaData", DataSetMetaDataType.TYPE_ID);
      actionTargets =
          (ActionTargetDataType[])
              decoder.decodeStructArray("ActionTargets", ActionTargetDataType.TYPE_ID);
      return new PublishedActionDataType(requestDataSetMetaData, actionTargets);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PublishedActionDataType value) {
      encoder.encodeStruct(
          "RequestDataSetMetaData", value.getRequestDataSetMetaData(), DataSetMetaDataType.TYPE_ID);
      encoder.encodeStructArray(
          "ActionTargets", value.getActionTargets(), ActionTargetDataType.TYPE_ID);
    }
  }
}
