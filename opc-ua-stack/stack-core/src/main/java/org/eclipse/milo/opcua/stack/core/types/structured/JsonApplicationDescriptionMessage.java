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
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class JsonApplicationDescriptionMessage extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=19314");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.NULL_VALUE;

  private final @Nullable String messageId;

  private final @Nullable String messageType;

  private final @Nullable String publisherId;

  private final DateTime timestamp;

  private final ApplicationDescription description;

  private final String @Nullable [] serverCapabilities;

  public JsonApplicationDescriptionMessage(
      @Nullable String messageId,
      @Nullable String messageType,
      @Nullable String publisherId,
      DateTime timestamp,
      ApplicationDescription description,
      String @Nullable [] serverCapabilities) {
    this.messageId = messageId;
    this.messageType = messageType;
    this.publisherId = publisherId;
    this.timestamp = timestamp;
    this.description = description;
    this.serverCapabilities = serverCapabilities;
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

  public ApplicationDescription getDescription() {
    return description;
  }

  public String @Nullable [] getServerCapabilities() {
    return serverCapabilities;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JsonApplicationDescriptionMessage that = (JsonApplicationDescriptionMessage) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMessageId(), that.getMessageId());
    eqb.append(getMessageType(), that.getMessageType());
    eqb.append(getPublisherId(), that.getPublisherId());
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getDescription(), that.getDescription());
    eqb.append(getServerCapabilities(), that.getServerCapabilities());
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
    hcb.append(getServerCapabilities());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", JsonApplicationDescriptionMessage.class.getSimpleName() + "[", "]");
    joiner.add("messageId='" + getMessageId() + "'");
    joiner.add("messageType='" + getMessageType() + "'");
    joiner.add("publisherId='" + getPublisherId() + "'");
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("description=" + getDescription());
    joiner.add("serverCapabilities=" + java.util.Arrays.toString(getServerCapabilities()));
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
              "ServerCapabilities",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonApplicationDescriptionMessage> {
    @Override
    public Class<JsonApplicationDescriptionMessage> getType() {
      return JsonApplicationDescriptionMessage.class;
    }

    @Override
    public JsonApplicationDescriptionMessage decodeType(
        EncodingContext context, UaDecoder decoder) {
      final String messageId;
      final String messageType;
      final String publisherId;
      final DateTime timestamp;
      final ApplicationDescription description;
      final String[] serverCapabilities;
      messageId = decoder.decodeString("MessageId");
      messageType = decoder.decodeString("MessageType");
      publisherId = decoder.decodeString("PublisherId");
      timestamp = decoder.decodeDateTime("Timestamp");
      description =
          (ApplicationDescription)
              decoder.decodeStruct("Description", ApplicationDescription.TYPE_ID);
      serverCapabilities = decoder.decodeStringArray("ServerCapabilities");
      return new JsonApplicationDescriptionMessage(
          messageId, messageType, publisherId, timestamp, description, serverCapabilities);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, JsonApplicationDescriptionMessage value) {
      encoder.encodeString("MessageId", value.getMessageId());
      encoder.encodeString("MessageType", value.getMessageType());
      encoder.encodeString("PublisherId", value.getPublisherId());
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeStruct("Description", value.getDescription(), ApplicationDescription.TYPE_ID);
      encoder.encodeStringArray("ServerCapabilities", value.getServerCapabilities());
    }
  }
}
