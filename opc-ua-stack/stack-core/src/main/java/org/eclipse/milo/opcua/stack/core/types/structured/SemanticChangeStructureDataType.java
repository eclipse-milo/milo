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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.17">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.17</a>
 */
public class SemanticChangeStructureDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=897");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=899");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=898");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15374");

  private final NodeId affected;

  private final NodeId affectedType;

  public SemanticChangeStructureDataType(NodeId affected, NodeId affectedType) {
    this.affected = affected;
    this.affectedType = affectedType;
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

  public NodeId getAffected() {
    return affected;
  }

  public NodeId getAffectedType() {
    return affectedType;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SemanticChangeStructureDataType that = (SemanticChangeStructureDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getAffected(), that.getAffected());
    eqb.append(getAffectedType(), that.getAffectedType());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAffected());
    hcb.append(getAffectedType());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", SemanticChangeStructureDataType.class.getSimpleName() + "[", "]");
    joiner.add("affected=" + getAffected());
    joiner.add("affectedType=" + getAffectedType());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 899),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Affected",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AffectedType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SemanticChangeStructureDataType> {
    @Override
    public Class<SemanticChangeStructureDataType> getType() {
      return SemanticChangeStructureDataType.class;
    }

    @Override
    public SemanticChangeStructureDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId affected;
      final NodeId affectedType;
      affected = decoder.decodeNodeId("Affected");
      affectedType = decoder.decodeNodeId("AffectedType");
      return new SemanticChangeStructureDataType(affected, affectedType);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SemanticChangeStructureDataType value) {
      encoder.encodeNodeId("Affected", value.getAffected());
      encoder.encodeNodeId("AffectedType", value.getAffectedType());
    }
  }
}
