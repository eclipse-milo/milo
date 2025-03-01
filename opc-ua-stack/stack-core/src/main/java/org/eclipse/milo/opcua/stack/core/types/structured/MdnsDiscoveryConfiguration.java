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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.13.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.13.2</a>
 */
public class MdnsDiscoveryConfiguration extends DiscoveryConfiguration implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=12891");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=12901");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=12893");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15106");

  private final @Nullable String mdnsServerName;

  private final String @Nullable [] serverCapabilities;

  public MdnsDiscoveryConfiguration(
      @Nullable String mdnsServerName, String @Nullable [] serverCapabilities) {
    this.mdnsServerName = mdnsServerName;
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

  public @Nullable String getMdnsServerName() {
    return mdnsServerName;
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
    MdnsDiscoveryConfiguration that = (MdnsDiscoveryConfiguration) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMdnsServerName(), that.getMdnsServerName());
    eqb.append(getServerCapabilities(), that.getServerCapabilities());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMdnsServerName());
    hcb.append(getServerCapabilities());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", MdnsDiscoveryConfiguration.class.getSimpleName() + "[", "]");
    joiner.add("mdnsServerName='" + getMdnsServerName() + "'");
    joiner.add("serverCapabilities=" + java.util.Arrays.toString(getServerCapabilities()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 12901),
        new NodeId(0, 12890),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "MdnsServerName",
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

  public static final class Codec extends GenericDataTypeCodec<MdnsDiscoveryConfiguration> {
    @Override
    public Class<MdnsDiscoveryConfiguration> getType() {
      return MdnsDiscoveryConfiguration.class;
    }

    @Override
    public MdnsDiscoveryConfiguration decodeType(EncodingContext context, UaDecoder decoder) {
      final String mdnsServerName;
      final String[] serverCapabilities;
      mdnsServerName = decoder.decodeString("MdnsServerName");
      serverCapabilities = decoder.decodeStringArray("ServerCapabilities");
      return new MdnsDiscoveryConfiguration(mdnsServerName, serverCapabilities);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, MdnsDiscoveryConfiguration value) {
      encoder.encodeString("MdnsServerName", value.getMdnsServerName());
      encoder.encodeStringArray("ServerCapabilities", value.getServerCapabilities());
    }
  }
}
