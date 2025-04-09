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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class JsonStatusMessage extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=19316");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  private final @Nullable String messageId;

  private final @Nullable String messageType;

  private final @Nullable String publisherId;

  private final DateTime timestamp;

  private final Boolean isCyclic;

  private final PubSubState status;

  private final DateTime nextReportTime;

  public JsonStatusMessage(
      @Nullable String messageId,
      @Nullable String messageType,
      @Nullable String publisherId,
      DateTime timestamp,
      Boolean isCyclic,
      PubSubState status,
      DateTime nextReportTime) {
    this.messageId = messageId;
    this.messageType = messageType;
    this.publisherId = publisherId;
    this.timestamp = timestamp;
    this.isCyclic = isCyclic;
    this.status = status;
    this.nextReportTime = nextReportTime;
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

  public Boolean getIsCyclic() {
    return isCyclic;
  }

  public PubSubState getStatus() {
    return status;
  }

  public DateTime getNextReportTime() {
    return nextReportTime;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JsonStatusMessage that = (JsonStatusMessage) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMessageId(), that.getMessageId());
    eqb.append(getMessageType(), that.getMessageType());
    eqb.append(getPublisherId(), that.getPublisherId());
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getIsCyclic(), that.getIsCyclic());
    eqb.append(getStatus(), that.getStatus());
    eqb.append(getNextReportTime(), that.getNextReportTime());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMessageId());
    hcb.append(getMessageType());
    hcb.append(getPublisherId());
    hcb.append(getTimestamp());
    hcb.append(getIsCyclic());
    hcb.append(getStatus());
    hcb.append(getNextReportTime());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", JsonStatusMessage.class.getSimpleName() + "[", "]");
    joiner.add("messageId='" + getMessageId() + "'");
    joiner.add("messageType='" + getMessageType() + "'");
    joiner.add("publisherId='" + getPublisherId() + "'");
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("isCyclic=" + getIsCyclic());
    joiner.add("status=" + getStatus());
    joiner.add("nextReportTime=" + getNextReportTime());
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
              "IsCyclic",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Status",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14647),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NextReportTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonStatusMessage> {
    @Override
    public Class<JsonStatusMessage> getType() {
      return JsonStatusMessage.class;
    }

    @Override
    public JsonStatusMessage decodeType(EncodingContext context, UaDecoder decoder) {
      final String messageId;
      final String messageType;
      final String publisherId;
      final DateTime timestamp;
      final Boolean isCyclic;
      final PubSubState status;
      final DateTime nextReportTime;
      messageId = decoder.decodeString("MessageId");
      messageType = decoder.decodeString("MessageType");
      publisherId = decoder.decodeString("PublisherId");
      timestamp = decoder.decodeDateTime("Timestamp");
      isCyclic = decoder.decodeBoolean("IsCyclic");
      status = PubSubState.from(decoder.decodeEnum("Status"));
      nextReportTime = decoder.decodeDateTime("NextReportTime");
      return new JsonStatusMessage(
          messageId, messageType, publisherId, timestamp, isCyclic, status, nextReportTime);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, JsonStatusMessage value) {
      encoder.encodeString("MessageId", value.getMessageId());
      encoder.encodeString("MessageType", value.getMessageType());
      encoder.encodeString("PublisherId", value.getPublisherId());
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeBoolean("IsCyclic", value.getIsCyclic());
      encoder.encodeEnum("Status", value.getStatus());
      encoder.encodeDateTime("NextReportTime", value.getNextReportTime());
    }
  }
}
