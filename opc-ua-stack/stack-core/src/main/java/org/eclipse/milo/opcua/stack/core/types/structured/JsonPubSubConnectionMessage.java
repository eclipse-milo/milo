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
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class JsonPubSubConnectionMessage extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=19317");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  private final @Nullable String messageId;

  private final @Nullable String messageType;

  private final @Nullable String publisherId;

  private final DateTime timestamp;

  private final PubSubConnectionDataType connection;

  public JsonPubSubConnectionMessage(
      @Nullable String messageId,
      @Nullable String messageType,
      @Nullable String publisherId,
      DateTime timestamp,
      PubSubConnectionDataType connection) {
    this.messageId = messageId;
    this.messageType = messageType;
    this.publisherId = publisherId;
    this.timestamp = timestamp;
    this.connection = connection;
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

  public DateTime getTimestamp() {
    return timestamp;
  }

  public PubSubConnectionDataType getConnection() {
    return connection;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JsonPubSubConnectionMessage that = (JsonPubSubConnectionMessage) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMessageId(), that.getMessageId());
    eqb.append(getMessageType(), that.getMessageType());
    eqb.append(getPublisherId(), that.getPublisherId());
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getConnection(), that.getConnection());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMessageId());
    hcb.append(getMessageType());
    hcb.append(getPublisherId());
    hcb.append(getTimestamp());
    hcb.append(getConnection());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", JsonPubSubConnectionMessage.class.getSimpleName() + "[", "]");
    joiner.add("messageId='" + getMessageId() + "'");
    joiner.add("messageType='" + getMessageType() + "'");
    joiner.add("publisherId='" + getPublisherId() + "'");
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("connection=" + getConnection());
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
              "Timestamp",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Connection",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15617),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonPubSubConnectionMessage> {
    @Override
    public Class<JsonPubSubConnectionMessage> getType() {
      return JsonPubSubConnectionMessage.class;
    }

    @Override
    public JsonPubSubConnectionMessage decodeType(EncodingContext context, UaDecoder decoder) {
      final String messageId;
      final String messageType;
      final String publisherId;
      final DateTime timestamp;
      final PubSubConnectionDataType connection;
      messageId = decoder.decodeString("MessageId");
      messageType = decoder.decodeString("MessageType");
      publisherId = decoder.decodeString("PublisherId");
      timestamp = decoder.decodeDateTime("Timestamp");
      connection =
          (PubSubConnectionDataType)
              decoder.decodeStruct("Connection", PubSubConnectionDataType.TYPE_ID);
      return new JsonPubSubConnectionMessage(
          messageId, messageType, publisherId, timestamp, connection);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, JsonPubSubConnectionMessage value) {
      encoder.encodeString("MessageId", value.getMessageId());
      encoder.encodeString("MessageType", value.getMessageType());
      encoder.encodeString("PublisherId", value.getPublisherId());
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeStruct("Connection", value.getConnection(), PubSubConnectionDataType.TYPE_ID);
    }
  }
}
