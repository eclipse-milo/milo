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

public class ChannelSecurityToken extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=441");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=443");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=442");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15131");

  private final UInteger channelId;

  private final UInteger tokenId;

  private final DateTime createdAt;

  private final UInteger revisedLifetime;

  public ChannelSecurityToken(
      UInteger channelId, UInteger tokenId, DateTime createdAt, UInteger revisedLifetime) {
    this.channelId = channelId;
    this.tokenId = tokenId;
    this.createdAt = createdAt;
    this.revisedLifetime = revisedLifetime;
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

  public UInteger getChannelId() {
    return channelId;
  }

  public UInteger getTokenId() {
    return tokenId;
  }

  public DateTime getCreatedAt() {
    return createdAt;
  }

  public UInteger getRevisedLifetime() {
    return revisedLifetime;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ChannelSecurityToken that = (ChannelSecurityToken) object;
    var eqb = new EqualsBuilder();
    eqb.append(getChannelId(), that.getChannelId());
    eqb.append(getTokenId(), that.getTokenId());
    eqb.append(getCreatedAt(), that.getCreatedAt());
    eqb.append(getRevisedLifetime(), that.getRevisedLifetime());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getChannelId());
    hcb.append(getTokenId());
    hcb.append(getCreatedAt());
    hcb.append(getRevisedLifetime());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ChannelSecurityToken.class.getSimpleName() + "[", "]");
    joiner.add("channelId=" + getChannelId());
    joiner.add("tokenId=" + getTokenId());
    joiner.add("createdAt=" + getCreatedAt());
    joiner.add("revisedLifetime=" + getRevisedLifetime());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 443),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ChannelId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TokenId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CreatedAt",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RevisedLifetime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ChannelSecurityToken> {
    @Override
    public Class<ChannelSecurityToken> getType() {
      return ChannelSecurityToken.class;
    }

    @Override
    public ChannelSecurityToken decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger channelId;
      final UInteger tokenId;
      final DateTime createdAt;
      final UInteger revisedLifetime;
      channelId = decoder.decodeUInt32("ChannelId");
      tokenId = decoder.decodeUInt32("TokenId");
      createdAt = decoder.decodeDateTime("CreatedAt");
      revisedLifetime = decoder.decodeUInt32("RevisedLifetime");
      return new ChannelSecurityToken(channelId, tokenId, createdAt, revisedLifetime);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ChannelSecurityToken value) {
      encoder.encodeUInt32("ChannelId", value.getChannelId());
      encoder.encodeUInt32("TokenId", value.getTokenId());
      encoder.encodeDateTime("CreatedAt", value.getCreatedAt());
      encoder.encodeUInt32("RevisedLifetime", value.getRevisedLifetime());
    }
  }
}
