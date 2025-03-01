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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.3/#5.5.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.3/#5.5.3.2</a>
 */
public class ServerOnNetwork extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=12189");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=12207");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=12195");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15095");

  private final UInteger recordId;

  private final @Nullable String serverName;

  private final @Nullable String discoveryUrl;

  private final String @Nullable [] serverCapabilities;

  public ServerOnNetwork(
      UInteger recordId,
      @Nullable String serverName,
      @Nullable String discoveryUrl,
      String @Nullable [] serverCapabilities) {
    this.recordId = recordId;
    this.serverName = serverName;
    this.discoveryUrl = discoveryUrl;
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

  public UInteger getRecordId() {
    return recordId;
  }

  public @Nullable String getServerName() {
    return serverName;
  }

  public @Nullable String getDiscoveryUrl() {
    return discoveryUrl;
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
    ServerOnNetwork that = (ServerOnNetwork) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRecordId(), that.getRecordId());
    eqb.append(getServerName(), that.getServerName());
    eqb.append(getDiscoveryUrl(), that.getDiscoveryUrl());
    eqb.append(getServerCapabilities(), that.getServerCapabilities());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRecordId());
    hcb.append(getServerName());
    hcb.append(getDiscoveryUrl());
    hcb.append(getServerCapabilities());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ServerOnNetwork.class.getSimpleName() + "[", "]");
    joiner.add("recordId=" + getRecordId());
    joiner.add("serverName='" + getServerName() + "'");
    joiner.add("discoveryUrl='" + getDiscoveryUrl() + "'");
    joiner.add("serverCapabilities=" + java.util.Arrays.toString(getServerCapabilities()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 12207),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "RecordId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiscoveryUrl",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
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

  public static final class Codec extends GenericDataTypeCodec<ServerOnNetwork> {
    @Override
    public Class<ServerOnNetwork> getType() {
      return ServerOnNetwork.class;
    }

    @Override
    public ServerOnNetwork decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger recordId;
      final String serverName;
      final String discoveryUrl;
      final String[] serverCapabilities;
      recordId = decoder.decodeUInt32("RecordId");
      serverName = decoder.decodeString("ServerName");
      discoveryUrl = decoder.decodeString("DiscoveryUrl");
      serverCapabilities = decoder.decodeStringArray("ServerCapabilities");
      return new ServerOnNetwork(recordId, serverName, discoveryUrl, serverCapabilities);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ServerOnNetwork value) {
      encoder.encodeUInt32("RecordId", value.getRecordId());
      encoder.encodeString("ServerName", value.getServerName());
      encoder.encodeString("DiscoveryUrl", value.getDiscoveryUrl());
      encoder.encodeStringArray("ServerCapabilities", value.getServerCapabilities());
    }
  }
}
