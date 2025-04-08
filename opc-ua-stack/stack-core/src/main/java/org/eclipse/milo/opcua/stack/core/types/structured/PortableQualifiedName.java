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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.37">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.37</a>
 */
public class PortableQualifiedName extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=24105");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=24108");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=24120");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=24132");

  private final @Nullable String namespaceUri;

  private final @Nullable String name;

  public PortableQualifiedName(@Nullable String namespaceUri, @Nullable String name) {
    this.namespaceUri = namespaceUri;
    this.name = name;
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

  public @Nullable String getName() {
    return name;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PortableQualifiedName that = (PortableQualifiedName) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNamespaceUri(), that.getNamespaceUri());
    eqb.append(getName(), that.getName());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNamespaceUri());
    hcb.append(getName());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PortableQualifiedName.class.getSimpleName() + "[", "]");
    joiner.add("namespaceUri='" + getNamespaceUri() + "'");
    joiner.add("name='" + getName() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 24108),
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
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PortableQualifiedName> {
    @Override
    public Class<PortableQualifiedName> getType() {
      return PortableQualifiedName.class;
    }

    @Override
    public PortableQualifiedName decodeType(EncodingContext context, UaDecoder decoder) {
      final String namespaceUri;
      final String name;
      namespaceUri = decoder.decodeString("NamespaceUri");
      name = decoder.decodeString("Name");
      return new PortableQualifiedName(namespaceUri, name);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PortableQualifiedName value) {
      encoder.encodeString("NamespaceUri", value.getNamespaceUri());
      encoder.encodeString("Name", value.getName());
    }
  }
}
