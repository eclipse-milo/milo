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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.38">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.38</a>
 */
public class PortableNodeId extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=24106");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=24109");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=24121");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=24133");

  private final @Nullable String namespaceUri;

  private final NodeId identifier;

  public PortableNodeId(@Nullable String namespaceUri, NodeId identifier) {
    this.namespaceUri = namespaceUri;
    this.identifier = identifier;
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

  public @Nullable String getNamespaceUri() {
    return namespaceUri;
  }

  public NodeId getIdentifier() {
    return identifier;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PortableNodeId that = (PortableNodeId) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNamespaceUri(), that.getNamespaceUri());
    eqb.append(getIdentifier(), that.getIdentifier());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNamespaceUri());
    hcb.append(getIdentifier());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PortableNodeId.class.getSimpleName() + "[", "]");
    joiner.add("namespaceUri='" + getNamespaceUri() + "'");
    joiner.add("identifier=" + getIdentifier());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 24109),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NamespaceUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Identifier",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PortableNodeId> {
    @Override
    public Class<PortableNodeId> getType() {
      return PortableNodeId.class;
    }

    @Override
    public PortableNodeId decodeType(EncodingContext context, UaDecoder decoder) {
      final String namespaceUri;
      final NodeId identifier;
      namespaceUri = decoder.decodeString("NamespaceUri");
      identifier = decoder.decodeNodeId("Identifier");
      return new PortableNodeId(namespaceUri, identifier);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, PortableNodeId value) {
      encoder.encodeString("NamespaceUri", value.getNamespaceUri());
      encoder.encodeNodeId("Identifier", value.getIdentifier());
    }
  }
}
