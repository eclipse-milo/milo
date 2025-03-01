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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.4">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.4</a>
 */
public class AttributeOperand extends FilterOperand implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=598");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=600");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=599");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15209");

  private final NodeId nodeId;

  private final @Nullable String alias;

  private final RelativePath browsePath;

  private final UInteger attributeId;

  private final String indexRange;

  public AttributeOperand(
      NodeId nodeId,
      @Nullable String alias,
      RelativePath browsePath,
      UInteger attributeId,
      String indexRange) {
    this.nodeId = nodeId;
    this.alias = alias;
    this.browsePath = browsePath;
    this.attributeId = attributeId;
    this.indexRange = indexRange;
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

  public NodeId getNodeId() {
    return nodeId;
  }

  public @Nullable String getAlias() {
    return alias;
  }

  public RelativePath getBrowsePath() {
    return browsePath;
  }

  public UInteger getAttributeId() {
    return attributeId;
  }

  public String getIndexRange() {
    return indexRange;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AttributeOperand that = (AttributeOperand) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getAlias(), that.getAlias());
    eqb.append(getBrowsePath(), that.getBrowsePath());
    eqb.append(getAttributeId(), that.getAttributeId());
    eqb.append(getIndexRange(), that.getIndexRange());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNodeId());
    hcb.append(getAlias());
    hcb.append(getBrowsePath());
    hcb.append(getAttributeId());
    hcb.append(getIndexRange());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AttributeOperand.class.getSimpleName() + "[", "]");
    joiner.add("nodeId=" + getNodeId());
    joiner.add("alias='" + getAlias() + "'");
    joiner.add("browsePath=" + getBrowsePath());
    joiner.add("attributeId=" + getAttributeId());
    joiner.add("indexRange='" + getIndexRange() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 600),
        new NodeId(0, 589),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Alias",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BrowsePath",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 540),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AttributeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IndexRange",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 291),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AttributeOperand> {
    @Override
    public Class<AttributeOperand> getType() {
      return AttributeOperand.class;
    }

    @Override
    public AttributeOperand decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId nodeId;
      final String alias;
      final RelativePath browsePath;
      final UInteger attributeId;
      final String indexRange;
      nodeId = decoder.decodeNodeId("NodeId");
      alias = decoder.decodeString("Alias");
      browsePath = (RelativePath) decoder.decodeStruct("BrowsePath", RelativePath.TYPE_ID);
      attributeId = decoder.decodeUInt32("AttributeId");
      indexRange = decoder.decodeString("IndexRange");
      return new AttributeOperand(nodeId, alias, browsePath, attributeId, indexRange);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, AttributeOperand value) {
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeString("Alias", value.getAlias());
      encoder.encodeStruct("BrowsePath", value.getBrowsePath(), RelativePath.TYPE_ID);
      encoder.encodeUInt32("AttributeId", value.getAttributeId());
      encoder.encodeString("IndexRange", value.getIndexRange());
    }
  }
}
