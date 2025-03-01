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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.3/#5.10.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.3/#5.10.3.1</a>
 */
public class QueryDataDescription extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=570");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=572");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=571");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15200");

  private final RelativePath relativePath;

  private final UInteger attributeId;

  private final String indexRange;

  public QueryDataDescription(RelativePath relativePath, UInteger attributeId, String indexRange) {
    this.relativePath = relativePath;
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

  public RelativePath getRelativePath() {
    return relativePath;
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
    QueryDataDescription that = (QueryDataDescription) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRelativePath(), that.getRelativePath());
    eqb.append(getAttributeId(), that.getAttributeId());
    eqb.append(getIndexRange(), that.getIndexRange());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRelativePath());
    hcb.append(getAttributeId());
    hcb.append(getIndexRange());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", QueryDataDescription.class.getSimpleName() + "[", "]");
    joiner.add("relativePath=" + getRelativePath());
    joiner.add("attributeId=" + getAttributeId());
    joiner.add("indexRange='" + getIndexRange() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 572),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "RelativePath",
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

  public static final class Codec extends GenericDataTypeCodec<QueryDataDescription> {
    @Override
    public Class<QueryDataDescription> getType() {
      return QueryDataDescription.class;
    }

    @Override
    public QueryDataDescription decodeType(EncodingContext context, UaDecoder decoder) {
      final RelativePath relativePath;
      final UInteger attributeId;
      final String indexRange;
      relativePath = (RelativePath) decoder.decodeStruct("RelativePath", RelativePath.TYPE_ID);
      attributeId = decoder.decodeUInt32("AttributeId");
      indexRange = decoder.decodeString("IndexRange");
      return new QueryDataDescription(relativePath, attributeId, indexRange);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, QueryDataDescription value) {
      encoder.encodeStruct("RelativePath", value.getRelativePath(), RelativePath.TYPE_ID);
      encoder.encodeUInt32("AttributeId", value.getAttributeId());
      encoder.encodeString("IndexRange", value.getIndexRange());
    }
  }
}
