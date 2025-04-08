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

public class JsonServerEndpointsMessage extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=19315");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  private final @Nullable String messageId;

  private final @Nullable String messageType;

  private final @Nullable String publisherId;

  private final DateTime timestamp;

  private final ApplicationDescription description;

  private final EndpointDescription @Nullable [] endpoints;

  public JsonServerEndpointsMessage(
      @Nullable String messageId,
      @Nullable String messageType,
      @Nullable String publisherId,
      DateTime timestamp,
      ApplicationDescription description,
      EndpointDescription @Nullable [] endpoints) {
    this.messageId = messageId;
    this.messageType = messageType;
    this.publisherId = publisherId;
    this.timestamp = timestamp;
    this.description = description;
    this.endpoints = endpoints;
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

  public ApplicationDescription getDescription() {
    return description;
  }

  public EndpointDescription @Nullable [] getEndpoints() {
    return endpoints;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JsonServerEndpointsMessage that = (JsonServerEndpointsMessage) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMessageId(), that.getMessageId());
    eqb.append(getMessageType(), that.getMessageType());
    eqb.append(getPublisherId(), that.getPublisherId());
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getDescription(), that.getDescription());
    eqb.append(getEndpoints(), that.getEndpoints());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMessageId());
    hcb.append(getMessageType());
    hcb.append(getPublisherId());
    hcb.append(getTimestamp());
    hcb.append(getDescription());
    hcb.append(getEndpoints());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", JsonServerEndpointsMessage.class.getSimpleName() + "[", "]");
    joiner.add("messageId='" + getMessageId() + "'");
    joiner.add("messageType='" + getMessageType() + "'");
    joiner.add("publisherId='" + getPublisherId() + "'");
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("description=" + getDescription());
    joiner.add("endpoints=" + java.util.Arrays.toString(getEndpoints()));
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
              "Description",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 308),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Endpoints",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 312),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonServerEndpointsMessage> {
    @Override
    public Class<JsonServerEndpointsMessage> getType() {
      return JsonServerEndpointsMessage.class;
    }

    @Override
    public JsonServerEndpointsMessage decodeType(EncodingContext context, UaDecoder decoder) {
      final String messageId;
      final String messageType;
      final String publisherId;
      final DateTime timestamp;
      final ApplicationDescription description;
      final EndpointDescription[] endpoints;
      messageId = decoder.decodeString("MessageId");
      messageType = decoder.decodeString("MessageType");
      publisherId = decoder.decodeString("PublisherId");
      timestamp = decoder.decodeDateTime("Timestamp");
      description =
          (ApplicationDescription)
              decoder.decodeStruct("Description", ApplicationDescription.TYPE_ID);
      endpoints =
          (EndpointDescription[])
              decoder.decodeStructArray("Endpoints", EndpointDescription.TYPE_ID);
      return new JsonServerEndpointsMessage(
          messageId, messageType, publisherId, timestamp, description, endpoints);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, JsonServerEndpointsMessage value) {
      encoder.encodeString("MessageId", value.getMessageId());
      encoder.encodeString("MessageType", value.getMessageType());
      encoder.encodeString("PublisherId", value.getPublisherId());
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeStruct("Description", value.getDescription(), ApplicationDescription.TYPE_ID);
      encoder.encodeStructArray("Endpoints", value.getEndpoints(), EndpointDescription.TYPE_ID);
    }
  }
}
