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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class JsonActionMetaDataMessage extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=19318");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  private final @Nullable String messageId;

  private final @Nullable String messageType;

  private final @Nullable String publisherId;

  private final UShort dataSetWriterId;

  private final @Nullable String dataSetWriterName;

  private final DateTime timestamp;

  private final ActionTargetDataType @Nullable [] actionTargets;

  private final DataSetMetaDataType request;

  private final DataSetMetaDataType response;

  private final ActionMethodDataType @Nullable [] actionMethods;

  public JsonActionMetaDataMessage(
      @Nullable String messageId,
      @Nullable String messageType,
      @Nullable String publisherId,
      UShort dataSetWriterId,
      @Nullable String dataSetWriterName,
      DateTime timestamp,
      ActionTargetDataType @Nullable [] actionTargets,
      DataSetMetaDataType request,
      DataSetMetaDataType response,
      ActionMethodDataType @Nullable [] actionMethods) {
    this.messageId = messageId;
    this.messageType = messageType;
    this.publisherId = publisherId;
    this.dataSetWriterId = dataSetWriterId;
    this.dataSetWriterName = dataSetWriterName;
    this.timestamp = timestamp;
    this.actionTargets = actionTargets;
    this.request = request;
    this.response = response;
    this.actionMethods = actionMethods;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  public @Nullable String getMessageId() {
    return messageId;
  }

  public @Nullable String getMessageType() {
    return messageType;
  }

  public @Nullable String getPublisherId() {
    return publisherId;
  }

  public UShort getDataSetWriterId() {
    return dataSetWriterId;
  }

  public @Nullable String getDataSetWriterName() {
    return dataSetWriterName;
  }

  public DateTime getTimestamp() {
    return timestamp;
  }

  public ActionTargetDataType @Nullable [] getActionTargets() {
    return actionTargets;
  }

  public DataSetMetaDataType getRequest() {
    return request;
  }

  public DataSetMetaDataType getResponse() {
    return response;
  }

  public ActionMethodDataType @Nullable [] getActionMethods() {
    return actionMethods;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JsonActionMetaDataMessage that = (JsonActionMetaDataMessage) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMessageId(), that.getMessageId());
    eqb.append(getMessageType(), that.getMessageType());
    eqb.append(getPublisherId(), that.getPublisherId());
    eqb.append(getDataSetWriterId(), that.getDataSetWriterId());
    eqb.append(getDataSetWriterName(), that.getDataSetWriterName());
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getActionTargets(), that.getActionTargets());
    eqb.append(getRequest(), that.getRequest());
    eqb.append(getResponse(), that.getResponse());
    eqb.append(getActionMethods(), that.getActionMethods());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMessageId());
    hcb.append(getMessageType());
    hcb.append(getPublisherId());
    hcb.append(getDataSetWriterId());
    hcb.append(getDataSetWriterName());
    hcb.append(getTimestamp());
    hcb.append(getActionTargets());
    hcb.append(getRequest());
    hcb.append(getResponse());
    hcb.append(getActionMethods());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", JsonActionMetaDataMessage.class.getSimpleName() + "[", "]");
    joiner.add("messageId='" + getMessageId() + "'");
    joiner.add("messageType='" + getMessageType() + "'");
    joiner.add("publisherId='" + getPublisherId() + "'");
    joiner.add("dataSetWriterId=" + getDataSetWriterId());
    joiner.add("dataSetWriterName='" + getDataSetWriterName() + "'");
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("actionTargets=" + java.util.Arrays.toString(getActionTargets()));
    joiner.add("request=" + getRequest());
    joiner.add("response=" + getResponse());
    joiner.add("actionMethods=" + java.util.Arrays.toString(getActionMethods()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 0),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "MessageId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
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
              "PublisherId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetWriterId",
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
              "Timestamp",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
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
              false),
          new StructureField(
              "Request",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14523),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Response",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14523),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ActionMethods",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18597),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonActionMetaDataMessage> {
    @Override
    public Class<JsonActionMetaDataMessage> getType() {
      return JsonActionMetaDataMessage.class;
    }

    @Override
    public JsonActionMetaDataMessage decodeType(EncodingContext context, UaDecoder decoder) {
      final String messageId;
      final String messageType;
      final String publisherId;
      final UShort dataSetWriterId;
      final String dataSetWriterName;
      final DateTime timestamp;
      final ActionTargetDataType[] actionTargets;
      final DataSetMetaDataType request;
      final DataSetMetaDataType response;
      final ActionMethodDataType[] actionMethods;
      messageId = decoder.decodeString("MessageId");
      messageType = decoder.decodeString("MessageType");
      publisherId = decoder.decodeString("PublisherId");
      dataSetWriterId = decoder.decodeUInt16("DataSetWriterId");
      dataSetWriterName = decoder.decodeString("DataSetWriterName");
      timestamp = decoder.decodeDateTime("Timestamp");
      actionTargets =
          (ActionTargetDataType[])
              decoder.decodeStructArray("ActionTargets", ActionTargetDataType.TYPE_ID);
      request = (DataSetMetaDataType) decoder.decodeStruct("Request", DataSetMetaDataType.TYPE_ID);
      response =
          (DataSetMetaDataType) decoder.decodeStruct("Response", DataSetMetaDataType.TYPE_ID);
      actionMethods =
          (ActionMethodDataType[])
              decoder.decodeStructArray("ActionMethods", ActionMethodDataType.TYPE_ID);
      return new JsonActionMetaDataMessage(
          messageId,
          messageType,
          publisherId,
          dataSetWriterId,
          dataSetWriterName,
          timestamp,
          actionTargets,
          request,
          response,
          actionMethods);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, JsonActionMetaDataMessage value) {
      encoder.encodeString("MessageId", value.getMessageId());
      encoder.encodeString("MessageType", value.getMessageType());
      encoder.encodeString("PublisherId", value.getPublisherId());
      encoder.encodeUInt16("DataSetWriterId", value.getDataSetWriterId());
      encoder.encodeString("DataSetWriterName", value.getDataSetWriterName());
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeStructArray(
          "ActionTargets", value.getActionTargets(), ActionTargetDataType.TYPE_ID);
      encoder.encodeStruct("Request", value.getRequest(), DataSetMetaDataType.TYPE_ID);
      encoder.encodeStruct("Response", value.getResponse(), DataSetMetaDataType.TYPE_ID);
      encoder.encodeStructArray(
          "ActionMethods", value.getActionMethods(), ActionMethodDataType.TYPE_ID);
    }
  }
}
