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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.24.1">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.24.1</a>
 */
public class NodeAttributes extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=349");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=351");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=350");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15151");

  private final UInteger specifiedAttributes;

  private final LocalizedText displayName;

  private final LocalizedText description;

  private final UInteger writeMask;

  private final UInteger userWriteMask;

  public NodeAttributes(
      UInteger specifiedAttributes,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask) {
    this.specifiedAttributes = specifiedAttributes;
    this.displayName = displayName;
    this.description = description;
    this.writeMask = writeMask;
    this.userWriteMask = userWriteMask;
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

  public UInteger getSpecifiedAttributes() {
    return specifiedAttributes;
  }

  public LocalizedText getDisplayName() {
    return displayName;
  }

  public LocalizedText getDescription() {
    return description;
  }

  public UInteger getWriteMask() {
    return writeMask;
  }

  public UInteger getUserWriteMask() {
    return userWriteMask;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    NodeAttributes that = (NodeAttributes) object;
    var eqb = new EqualsBuilder();
    eqb.append(getSpecifiedAttributes(), that.getSpecifiedAttributes());
    eqb.append(getDisplayName(), that.getDisplayName());
    eqb.append(getDescription(), that.getDescription());
    eqb.append(getWriteMask(), that.getWriteMask());
    eqb.append(getUserWriteMask(), that.getUserWriteMask());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSpecifiedAttributes());
    hcb.append(getDisplayName());
    hcb.append(getDescription());
    hcb.append(getWriteMask());
    hcb.append(getUserWriteMask());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", NodeAttributes.class.getSimpleName() + "[", "]");
    joiner.add("specifiedAttributes=" + getSpecifiedAttributes());
    joiner.add("displayName=" + getDisplayName());
    joiner.add("description=" + getDescription());
    joiner.add("writeMask=" + getWriteMask());
    joiner.add("userWriteMask=" + getUserWriteMask());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 351),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SpecifiedAttributes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DisplayName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Description",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "WriteMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserWriteMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<NodeAttributes> {
    @Override
    public Class<NodeAttributes> getType() {
      return NodeAttributes.class;
    }

    @Override
    public NodeAttributes decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger specifiedAttributes;
      final LocalizedText displayName;
      final LocalizedText description;
      final UInteger writeMask;
      final UInteger userWriteMask;
      specifiedAttributes = decoder.decodeUInt32("SpecifiedAttributes");
      displayName = decoder.decodeLocalizedText("DisplayName");
      description = decoder.decodeLocalizedText("Description");
      writeMask = decoder.decodeUInt32("WriteMask");
      userWriteMask = decoder.decodeUInt32("UserWriteMask");
      return new NodeAttributes(
          specifiedAttributes, displayName, description, writeMask, userWriteMask);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, NodeAttributes value) {
      encoder.encodeUInt32("SpecifiedAttributes", value.getSpecifiedAttributes());
      encoder.encodeLocalizedText("DisplayName", value.getDisplayName());
      encoder.encodeLocalizedText("Description", value.getDescription());
      encoder.encodeUInt32("WriteMask", value.getWriteMask());
      encoder.encodeUInt32("UserWriteMask", value.getUserWriteMask());
    }
  }
}
