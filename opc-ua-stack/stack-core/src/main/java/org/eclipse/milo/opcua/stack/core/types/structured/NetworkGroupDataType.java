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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.19">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.19</a>
 */
public class NetworkGroupDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=11944");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=11958");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=11950");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15364");

  private final @Nullable String serverUri;

  private final EndpointUrlListDataType @Nullable [] networkPaths;

  public NetworkGroupDataType(
      @Nullable String serverUri, EndpointUrlListDataType @Nullable [] networkPaths) {
    this.serverUri = serverUri;
    this.networkPaths = networkPaths;
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

  public @Nullable String getServerUri() {
    return serverUri;
  }

  public EndpointUrlListDataType @Nullable [] getNetworkPaths() {
    return networkPaths;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    NetworkGroupDataType that = (NetworkGroupDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getServerUri(), that.getServerUri());
    eqb.append(getNetworkPaths(), that.getNetworkPaths());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getServerUri());
    hcb.append(getNetworkPaths());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", NetworkGroupDataType.class.getSimpleName() + "[", "]");
    joiner.add("serverUri='" + getServerUri() + "'");
    joiner.add("networkPaths=" + java.util.Arrays.toString(getNetworkPaths()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 11958),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ServerUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NetworkPaths",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11943),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<NetworkGroupDataType> {
    @Override
    public Class<NetworkGroupDataType> getType() {
      return NetworkGroupDataType.class;
    }

    @Override
    public NetworkGroupDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String serverUri;
      final EndpointUrlListDataType[] networkPaths;
      serverUri = decoder.decodeString("ServerUri");
      networkPaths =
          (EndpointUrlListDataType[])
              decoder.decodeStructArray("NetworkPaths", EndpointUrlListDataType.TYPE_ID);
      return new NetworkGroupDataType(serverUri, networkPaths);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, NetworkGroupDataType value) {
      encoder.encodeString("ServerUri", value.getServerUri());
      encoder.encodeStructArray(
          "NetworkPaths", value.getNetworkPaths(), EndpointUrlListDataType.TYPE_ID);
    }
  }
}
