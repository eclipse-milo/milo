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

public class JsonDataSetMetaDataMessage extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=19313");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  private final @Nullable String messageId;

  private final @Nullable String messageType;

  private final @Nullable String publisherId;

  private final UShort dataSetWriterId;

  private final @Nullable String writerGroupName;

  private final @Nullable String dataSetWriterName;

  private final DateTime timestamp;

  private final DataSetMetaDataType metaData;

  public JsonDataSetMetaDataMessage(
      @Nullable String messageId,
      @Nullable String messageType,
      @Nullable String publisherId,
      UShort dataSetWriterId,
      @Nullable String writerGroupName,
      @Nullable String dataSetWriterName,
      DateTime timestamp,
      DataSetMetaDataType metaData) {
    this.messageId = messageId;
    this.messageType = messageType;
    this.publisherId = publisherId;
    this.dataSetWriterId = dataSetWriterId;
    this.writerGroupName = writerGroupName;
    this.dataSetWriterName = dataSetWriterName;
    this.timestamp = timestamp;
    this.metaData = metaData;
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

  public @Nullable String getWriterGroupName() {
    return writerGroupName;
  }

  public @Nullable String getDataSetWriterName() {
    return dataSetWriterName;
  }

  public DateTime getTimestamp() {
    return timestamp;
  }

  public DataSetMetaDataType getMetaData() {
    return metaData;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JsonDataSetMetaDataMessage that = (JsonDataSetMetaDataMessage) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMessageId(), that.getMessageId());
    eqb.append(getMessageType(), that.getMessageType());
    eqb.append(getPublisherId(), that.getPublisherId());
    eqb.append(getDataSetWriterId(), that.getDataSetWriterId());
    eqb.append(getWriterGroupName(), that.getWriterGroupName());
    eqb.append(getDataSetWriterName(), that.getDataSetWriterName());
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getMetaData(), that.getMetaData());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMessageId());
    hcb.append(getMessageType());
    hcb.append(getPublisherId());
    hcb.append(getDataSetWriterId());
    hcb.append(getWriterGroupName());
    hcb.append(getDataSetWriterName());
    hcb.append(getTimestamp());
    hcb.append(getMetaData());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", JsonDataSetMetaDataMessage.class.getSimpleName() + "[", "]");
    joiner.add("messageId='" + getMessageId() + "'");
    joiner.add("messageType='" + getMessageType() + "'");
    joiner.add("publisherId='" + getPublisherId() + "'");
    joiner.add("dataSetWriterId=" + getDataSetWriterId());
    joiner.add("writerGroupName='" + getWriterGroupName() + "'");
    joiner.add("dataSetWriterName='" + getDataSetWriterName() + "'");
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("metaData=" + getMetaData());
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
              "WriterGroupName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
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
              "MetaData",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14523),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonDataSetMetaDataMessage> {
    @Override
    public Class<JsonDataSetMetaDataMessage> getType() {
      return JsonDataSetMetaDataMessage.class;
    }

    @Override
    public JsonDataSetMetaDataMessage decodeType(EncodingContext context, UaDecoder decoder) {
      final String messageId;
      final String messageType;
      final String publisherId;
      final UShort dataSetWriterId;
      final String writerGroupName;
      final String dataSetWriterName;
      final DateTime timestamp;
      final DataSetMetaDataType metaData;
      messageId = decoder.decodeString("MessageId");
      messageType = decoder.decodeString("MessageType");
      publisherId = decoder.decodeString("PublisherId");
      dataSetWriterId = decoder.decodeUInt16("DataSetWriterId");
      writerGroupName = decoder.decodeString("WriterGroupName");
      dataSetWriterName = decoder.decodeString("DataSetWriterName");
      timestamp = decoder.decodeDateTime("Timestamp");
      metaData =
          (DataSetMetaDataType) decoder.decodeStruct("MetaData", DataSetMetaDataType.TYPE_ID);
      return new JsonDataSetMetaDataMessage(
          messageId,
          messageType,
          publisherId,
          dataSetWriterId,
          writerGroupName,
          dataSetWriterName,
          timestamp,
          metaData);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, JsonDataSetMetaDataMessage value) {
      encoder.encodeString("MessageId", value.getMessageId());
      encoder.encodeString("MessageType", value.getMessageType());
      encoder.encodeString("PublisherId", value.getPublisherId());
      encoder.encodeUInt16("DataSetWriterId", value.getDataSetWriterId());
      encoder.encodeString("WriterGroupName", value.getWriterGroupName());
      encoder.encodeString("DataSetWriterName", value.getDataSetWriterName());
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeStruct("MetaData", value.getMetaData(), DataSetMetaDataType.TYPE_ID);
    }
  }
}
