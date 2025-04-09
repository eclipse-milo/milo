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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class JsonActionNetworkMessage extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=19320");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  private final @Nullable String messageId;

  private final @Nullable String messageType;

  private final @Nullable String publisherId;

  private final DateTime timestamp;

  private final @Nullable String responseAddress;

  private final ByteString correlationData;

  private final @Nullable String requestorId;

  private final Double timeoutHint;

  private final ExtensionObject @Nullable [] messages;

  public JsonActionNetworkMessage(
      @Nullable String messageId,
      @Nullable String messageType,
      @Nullable String publisherId,
      DateTime timestamp,
      @Nullable String responseAddress,
      ByteString correlationData,
      @Nullable String requestorId,
      Double timeoutHint,
      ExtensionObject @Nullable [] messages) {
    this.messageId = messageId;
    this.messageType = messageType;
    this.publisherId = publisherId;
    this.timestamp = timestamp;
    this.responseAddress = responseAddress;
    this.correlationData = correlationData;
    this.requestorId = requestorId;
    this.timeoutHint = timeoutHint;
    this.messages = messages;
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

  public @Nullable String getResponseAddress() {
    return responseAddress;
  }

  public ByteString getCorrelationData() {
    return correlationData;
  }

  public @Nullable String getRequestorId() {
    return requestorId;
  }

  public Double getTimeoutHint() {
    return timeoutHint;
  }

  public ExtensionObject @Nullable [] getMessages() {
    return messages;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JsonActionNetworkMessage that = (JsonActionNetworkMessage) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMessageId(), that.getMessageId());
    eqb.append(getMessageType(), that.getMessageType());
    eqb.append(getPublisherId(), that.getPublisherId());
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getResponseAddress(), that.getResponseAddress());
    eqb.append(getCorrelationData(), that.getCorrelationData());
    eqb.append(getRequestorId(), that.getRequestorId());
    eqb.append(getTimeoutHint(), that.getTimeoutHint());
    eqb.append(getMessages(), that.getMessages());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMessageId());
    hcb.append(getMessageType());
    hcb.append(getPublisherId());
    hcb.append(getTimestamp());
    hcb.append(getResponseAddress());
    hcb.append(getCorrelationData());
    hcb.append(getRequestorId());
    hcb.append(getTimeoutHint());
    hcb.append(getMessages());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", JsonActionNetworkMessage.class.getSimpleName() + "[", "]");
    joiner.add("messageId='" + getMessageId() + "'");
    joiner.add("messageType='" + getMessageType() + "'");
    joiner.add("publisherId='" + getPublisherId() + "'");
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("responseAddress='" + getResponseAddress() + "'");
    joiner.add("correlationData=" + getCorrelationData());
    joiner.add("requestorId='" + getRequestorId() + "'");
    joiner.add("timeoutHint=" + getTimeoutHint());
    joiner.add("messages=" + java.util.Arrays.toString(getMessages()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 0),
        new NodeId(0, 22),
        StructureType.StructureWithSubtypedValues,
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
              "ResponseAddress",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CorrelationData",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestorId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TimeoutHint",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Messages",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              1,
              null,
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonActionNetworkMessage> {
    @Override
    public Class<JsonActionNetworkMessage> getType() {
      return JsonActionNetworkMessage.class;
    }

    @Override
    public JsonActionNetworkMessage decodeType(EncodingContext context, UaDecoder decoder) {
      final String messageId;
      final String messageType;
      final String publisherId;
      final DateTime timestamp;
      final String responseAddress;
      final ByteString correlationData;
      final String requestorId;
      final Double timeoutHint;
      final ExtensionObject[] messages;
      messageId = decoder.decodeString("MessageId");
      messageType = decoder.decodeString("MessageType");
      publisherId = decoder.decodeString("PublisherId");
      timestamp = decoder.decodeDateTime("Timestamp");
      responseAddress = decoder.decodeString("ResponseAddress");
      correlationData = decoder.decodeByteString("CorrelationData");
      requestorId = decoder.decodeString("RequestorId");
      timeoutHint = decoder.decodeDouble("TimeoutHint");
      messages = decoder.decodeExtensionObjectArray("Messages");
      return new JsonActionNetworkMessage(
          messageId,
          messageType,
          publisherId,
          timestamp,
          responseAddress,
          correlationData,
          requestorId,
          timeoutHint,
          messages);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, JsonActionNetworkMessage value) {
      encoder.encodeString("MessageId", value.getMessageId());
      encoder.encodeString("MessageType", value.getMessageType());
      encoder.encodeString("PublisherId", value.getPublisherId());
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeString("ResponseAddress", value.getResponseAddress());
      encoder.encodeByteString("CorrelationData", value.getCorrelationData());
      encoder.encodeString("RequestorId", value.getRequestorId());
      encoder.encodeDouble("TimeoutHint", value.getTimeoutHint());
      encoder.encodeExtensionObjectArray("Messages", value.getMessages());
    }
  }
}
