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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.7">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.7</a>
 */
public class RedundantServerDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=853");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=855");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=854");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15362");

  private final @Nullable String serverId;

  private final UByte serviceLevel;

  private final ServerState serverState;

  public RedundantServerDataType(
      @Nullable String serverId, UByte serviceLevel, ServerState serverState) {
    this.serverId = serverId;
    this.serviceLevel = serviceLevel;
    this.serverState = serverState;
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

  public @Nullable String getServerId() {
    return serverId;
  }

  public UByte getServiceLevel() {
    return serviceLevel;
  }

  public ServerState getServerState() {
    return serverState;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RedundantServerDataType that = (RedundantServerDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getServerId(), that.getServerId());
    eqb.append(getServiceLevel(), that.getServiceLevel());
    eqb.append(getServerState(), that.getServerState());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getServerId());
    hcb.append(getServiceLevel());
    hcb.append(getServerState());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", RedundantServerDataType.class.getSimpleName() + "[", "]");
    joiner.add("serverId='" + getServerId() + "'");
    joiner.add("serviceLevel=" + getServiceLevel());
    joiner.add("serverState=" + getServerState());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 855),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ServerId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServiceLevel",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerState",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 852),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<RedundantServerDataType> {
    @Override
    public Class<RedundantServerDataType> getType() {
      return RedundantServerDataType.class;
    }

    @Override
    public RedundantServerDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String serverId;
      final UByte serviceLevel;
      final ServerState serverState;
      serverId = decoder.decodeString("ServerId");
      serviceLevel = decoder.decodeByte("ServiceLevel");
      serverState = ServerState.from(decoder.decodeEnum("ServerState"));
      return new RedundantServerDataType(serverId, serviceLevel, serverState);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, RedundantServerDataType value) {
      encoder.encodeString("ServerId", value.getServerId());
      encoder.encodeByte("ServiceLevel", value.getServiceLevel());
      encoder.encodeEnum("ServerState", value.getServerState());
    }
  }
}
