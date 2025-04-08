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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class JsonDataSetMessage extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=19312");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  private final UShort dataSetWriterId;

  private final @Nullable String dataSetWriterName;

  private final @Nullable String publisherId;

  private final @Nullable String writerGroupName;

  private final UInteger sequenceNumber;

  private final ConfigurationVersionDataType metaDataVersion;

  private final UInteger minorVersion;

  private final DateTime timestamp;

  private final StatusCode status;

  private final @Nullable String messageType;

  private final ExtensionObject payload;

  public JsonDataSetMessage(
      UShort dataSetWriterId,
      @Nullable String dataSetWriterName,
      @Nullable String publisherId,
      @Nullable String writerGroupName,
      UInteger sequenceNumber,
      ConfigurationVersionDataType metaDataVersion,
      UInteger minorVersion,
      DateTime timestamp,
      StatusCode status,
      @Nullable String messageType,
      ExtensionObject payload) {
    this.dataSetWriterId = dataSetWriterId;
    this.dataSetWriterName = dataSetWriterName;
    this.publisherId = publisherId;
    this.writerGroupName = writerGroupName;
    this.sequenceNumber = sequenceNumber;
    this.metaDataVersion = metaDataVersion;
    this.minorVersion = minorVersion;
    this.timestamp = timestamp;
    this.status = status;
    this.messageType = messageType;
    this.payload = payload;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  public UShort getDataSetWriterId() {
    return dataSetWriterId;
  }

  public @Nullable String getDataSetWriterName() {
    return dataSetWriterName;
  }

  public @Nullable String getPublisherId() {
    return publisherId;
  }

  public @Nullable String getWriterGroupName() {
    return writerGroupName;
  }

  public UInteger getSequenceNumber() {
    return sequenceNumber;
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

  public StatusCode getStatus() {
    return status;
  }

  public @Nullable String getMessageType() {
    return messageType;
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
    JsonDataSetMessage that = (JsonDataSetMessage) object;
    var eqb = new EqualsBuilder();
    eqb.append(getDataSetWriterId(), that.getDataSetWriterId());
    eqb.append(getDataSetWriterName(), that.getDataSetWriterName());
    eqb.append(getPublisherId(), that.getPublisherId());
    eqb.append(getWriterGroupName(), that.getWriterGroupName());
    eqb.append(getSequenceNumber(), that.getSequenceNumber());
    eqb.append(getMetaDataVersion(), that.getMetaDataVersion());
    eqb.append(getMinorVersion(), that.getMinorVersion());
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getStatus(), that.getStatus());
    eqb.append(getMessageType(), that.getMessageType());
    eqb.append(getPayload(), that.getPayload());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getDataSetWriterId());
    hcb.append(getDataSetWriterName());
    hcb.append(getPublisherId());
    hcb.append(getWriterGroupName());
    hcb.append(getSequenceNumber());
    hcb.append(getMetaDataVersion());
    hcb.append(getMinorVersion());
    hcb.append(getTimestamp());
    hcb.append(getStatus());
    hcb.append(getMessageType());
    hcb.append(getPayload());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", JsonDataSetMessage.class.getSimpleName() + "[", "]");
    joiner.add("dataSetWriterId=" + getDataSetWriterId());
    joiner.add("dataSetWriterName='" + getDataSetWriterName() + "'");
    joiner.add("publisherId='" + getPublisherId() + "'");
    joiner.add("writerGroupName='" + getWriterGroupName() + "'");
    joiner.add("sequenceNumber=" + getSequenceNumber());
    joiner.add("metaDataVersion=" + getMetaDataVersion());
    joiner.add("minorVersion=" + getMinorVersion());
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("status=" + getStatus());
    joiner.add("messageType='" + getMessageType() + "'");
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
              "DataSetWriterName",
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
              "WriterGroupName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SequenceNumber",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
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
              "Status",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
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
              "Payload",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              -1,
              null,
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonDataSetMessage> {
    @Override
    public Class<JsonDataSetMessage> getType() {
      return JsonDataSetMessage.class;
    }

    @Override
    public JsonDataSetMessage decodeType(EncodingContext context, UaDecoder decoder) {
      final UShort dataSetWriterId;
      final String dataSetWriterName;
      final String publisherId;
      final String writerGroupName;
      final UInteger sequenceNumber;
      final ConfigurationVersionDataType metaDataVersion;
      final UInteger minorVersion;
      final DateTime timestamp;
      final StatusCode status;
      final String messageType;
      final ExtensionObject payload;
      dataSetWriterId = decoder.decodeUInt16("DataSetWriterId");
      dataSetWriterName = decoder.decodeString("DataSetWriterName");
      publisherId = decoder.decodeString("PublisherId");
      writerGroupName = decoder.decodeString("WriterGroupName");
      sequenceNumber = decoder.decodeUInt32("SequenceNumber");
      metaDataVersion =
          (ConfigurationVersionDataType)
              decoder.decodeStruct("MetaDataVersion", ConfigurationVersionDataType.TYPE_ID);
      minorVersion = decoder.decodeUInt32("MinorVersion");
      timestamp = decoder.decodeDateTime("Timestamp");
      status = decoder.decodeStatusCode("Status");
      messageType = decoder.decodeString("MessageType");
      payload = decoder.decodeExtensionObject("Payload");
      return new JsonDataSetMessage(
          dataSetWriterId,
          dataSetWriterName,
          publisherId,
          writerGroupName,
          sequenceNumber,
          metaDataVersion,
          minorVersion,
          timestamp,
          status,
          messageType,
          payload);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, JsonDataSetMessage value) {
      encoder.encodeUInt16("DataSetWriterId", value.getDataSetWriterId());
      encoder.encodeString("DataSetWriterName", value.getDataSetWriterName());
      encoder.encodeString("PublisherId", value.getPublisherId());
      encoder.encodeString("WriterGroupName", value.getWriterGroupName());
      encoder.encodeUInt32("SequenceNumber", value.getSequenceNumber());
      encoder.encodeStruct(
          "MetaDataVersion", value.getMetaDataVersion(), ConfigurationVersionDataType.TYPE_ID);
      encoder.encodeUInt32("MinorVersion", value.getMinorVersion());
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeStatusCode("Status", value.getStatus());
      encoder.encodeString("MessageType", value.getMessageType());
      encoder.encodeExtensionObject("Payload", value.getPayload());
    }
  }
}
