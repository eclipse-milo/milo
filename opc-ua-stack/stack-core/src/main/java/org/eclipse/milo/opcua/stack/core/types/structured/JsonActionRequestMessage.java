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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ActionState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class JsonActionRequestMessage extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=19321");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  private final UShort dataSetWriterId;

  private final UShort actionTargetId;

  private final @Nullable String dataSetWriterName;

  private final @Nullable String writerGroupName;

  private final ConfigurationVersionDataType metaDataVersion;

  private final UInteger minorVersion;

  private final DateTime timestamp;

  private final @Nullable String messageType;

  private final UShort requestId;

  private final ActionState actionState;

  private final ExtensionObject payload;

  public JsonActionRequestMessage(
      UShort dataSetWriterId,
      UShort actionTargetId,
      @Nullable String dataSetWriterName,
      @Nullable String writerGroupName,
      ConfigurationVersionDataType metaDataVersion,
      UInteger minorVersion,
      DateTime timestamp,
      @Nullable String messageType,
      UShort requestId,
      ActionState actionState,
      ExtensionObject payload) {
    this.dataSetWriterId = dataSetWriterId;
    this.actionTargetId = actionTargetId;
    this.dataSetWriterName = dataSetWriterName;
    this.writerGroupName = writerGroupName;
    this.metaDataVersion = metaDataVersion;
    this.minorVersion = minorVersion;
    this.timestamp = timestamp;
    this.messageType = messageType;
    this.requestId = requestId;
    this.actionState = actionState;
    this.payload = payload;
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

  public UShort getDataSetWriterId() {
    return dataSetWriterId;
  }

  public UShort getActionTargetId() {
    return actionTargetId;
  }

  public @Nullable String getDataSetWriterName() {
    return dataSetWriterName;
  }

  public @Nullable String getWriterGroupName() {
    return writerGroupName;
  }

  public ConfigurationVersionDataType getMetaDataVersion() {
    return metaDataVersion;
  }

  public UInteger getMinorVersion() {
    return minorVersion;
  }

  public DateTime getTimestamp() {
    return timestamp;
  }

  public @Nullable String getMessageType() {
    return messageType;
  }

  public UShort getRequestId() {
    return requestId;
  }

  public ActionState getActionState() {
    return actionState;
  }

  public ExtensionObject getPayload() {
    return payload;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JsonActionRequestMessage that = (JsonActionRequestMessage) object;
    var eqb = new EqualsBuilder();
    eqb.append(getDataSetWriterId(), that.getDataSetWriterId());
    eqb.append(getActionTargetId(), that.getActionTargetId());
    eqb.append(getDataSetWriterName(), that.getDataSetWriterName());
    eqb.append(getWriterGroupName(), that.getWriterGroupName());
    eqb.append(getMetaDataVersion(), that.getMetaDataVersion());
    eqb.append(getMinorVersion(), that.getMinorVersion());
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getMessageType(), that.getMessageType());
    eqb.append(getRequestId(), that.getRequestId());
    eqb.append(getActionState(), that.getActionState());
    eqb.append(getPayload(), that.getPayload());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getDataSetWriterId());
    hcb.append(getActionTargetId());
    hcb.append(getDataSetWriterName());
    hcb.append(getWriterGroupName());
    hcb.append(getMetaDataVersion());
    hcb.append(getMinorVersion());
    hcb.append(getTimestamp());
    hcb.append(getMessageType());
    hcb.append(getRequestId());
    hcb.append(getActionState());
    hcb.append(getPayload());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", JsonActionRequestMessage.class.getSimpleName() + "[", "]");
    joiner.add("dataSetWriterId=" + getDataSetWriterId());
    joiner.add("actionTargetId=" + getActionTargetId());
    joiner.add("dataSetWriterName='" + getDataSetWriterName() + "'");
    joiner.add("writerGroupName='" + getWriterGroupName() + "'");
    joiner.add("metaDataVersion=" + getMetaDataVersion());
    joiner.add("minorVersion=" + getMinorVersion());
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("messageType='" + getMessageType() + "'");
    joiner.add("requestId=" + getRequestId());
    joiner.add("actionState=" + getActionState());
    joiner.add("payload=" + getPayload());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 0),
        new NodeId(0, 22),
        StructureType.StructureWithSubtypedValues,
        new StructureField[] {
          new StructureField(
              "DataSetWriterId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ActionTargetId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetWriterName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "WriterGroupName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MetaDataVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14593),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MinorVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20998),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Timestamp",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 13),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MessageType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ActionState",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18595),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Payload",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              -1,
              null,
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonActionRequestMessage> {
    @Override
    public Class<JsonActionRequestMessage> getType() {
      return JsonActionRequestMessage.class;
    }

    @Override
    public JsonActionRequestMessage decodeType(EncodingContext context, UaDecoder decoder) {
      final UShort dataSetWriterId;
      final UShort actionTargetId;
      final String dataSetWriterName;
      final String writerGroupName;
      final ConfigurationVersionDataType metaDataVersion;
      final UInteger minorVersion;
      final DateTime timestamp;
      final String messageType;
      final UShort requestId;
      final ActionState actionState;
      final ExtensionObject payload;
      dataSetWriterId = decoder.decodeUInt16("DataSetWriterId");
      actionTargetId = decoder.decodeUInt16("ActionTargetId");
      dataSetWriterName = decoder.decodeString("DataSetWriterName");
      writerGroupName = decoder.decodeString("WriterGroupName");
      metaDataVersion =
          (ConfigurationVersionDataType)
              decoder.decodeStruct("MetaDataVersion", ConfigurationVersionDataType.TYPE_ID);
      minorVersion = decoder.decodeUInt32("MinorVersion");
      timestamp = decoder.decodeDateTime("Timestamp");
      messageType = decoder.decodeString("MessageType");
      requestId = decoder.decodeUInt16("RequestId");
      actionState = ActionState.from(decoder.decodeEnum("ActionState"));
      payload = decoder.decodeExtensionObject("Payload");
      return new JsonActionRequestMessage(
          dataSetWriterId,
          actionTargetId,
          dataSetWriterName,
          writerGroupName,
          metaDataVersion,
          minorVersion,
          timestamp,
          messageType,
          requestId,
          actionState,
          payload);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, JsonActionRequestMessage value) {
      encoder.encodeUInt16("DataSetWriterId", value.getDataSetWriterId());
      encoder.encodeUInt16("ActionTargetId", value.getActionTargetId());
      encoder.encodeString("DataSetWriterName", value.getDataSetWriterName());
      encoder.encodeString("WriterGroupName", value.getWriterGroupName());
      encoder.encodeStruct(
          "MetaDataVersion", value.getMetaDataVersion(), ConfigurationVersionDataType.TYPE_ID);
      encoder.encodeUInt32("MinorVersion", value.getMinorVersion());
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeString("MessageType", value.getMessageType());
      encoder.encodeUInt16("RequestId", value.getRequestId());
      encoder.encodeEnum("ActionState", value.getActionState());
      encoder.encodeExtensionObject("Payload", value.getPayload());
    }
  }
}
