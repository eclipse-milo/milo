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
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.16">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.16</a>
 */
public class ModelChangeStructureDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=877");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=879");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=878");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15373");

  private final NodeId affected;

  private final NodeId affectedType;

  private final UByte verb;

  public ModelChangeStructureDataType(NodeId affected, NodeId affectedType, UByte verb) {
    this.affected = affected;
    this.affectedType = affectedType;
    this.verb = verb;
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

  public UByte getVerb() {
    return verb;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ModelChangeStructureDataType that = (ModelChangeStructureDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getAffected(), that.getAffected());
    eqb.append(getAffectedType(), that.getAffectedType());
    eqb.append(getVerb(), that.getVerb());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAffected());
    hcb.append(getAffectedType());
    hcb.append(getVerb());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", ModelChangeStructureDataType.class.getSimpleName() + "[", "]");
    joiner.add("affected=" + getAffected());
    joiner.add("affectedType=" + getAffectedType());
    joiner.add("verb=" + getVerb());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 879),
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
              false),
          new StructureField(
              "Verb",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ModelChangeStructureDataType> {
    @Override
    public Class<ModelChangeStructureDataType> getType() {
      return ModelChangeStructureDataType.class;
    }

    @Override
    public ModelChangeStructureDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId affected;
      final NodeId affectedType;
      final UByte verb;
      affected = decoder.decodeNodeId("Affected");
      affectedType = decoder.decodeNodeId("AffectedType");
      verb = decoder.decodeByte("Verb");
      return new ModelChangeStructureDataType(affected, affectedType, verb);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ModelChangeStructureDataType value) {
      encoder.encodeNodeId("Affected", value.getAffected());
      encoder.encodeNodeId("AffectedType", value.getAffectedType());
      encoder.encodeByte("Verb", value.getVerb());
    }
  }
}
