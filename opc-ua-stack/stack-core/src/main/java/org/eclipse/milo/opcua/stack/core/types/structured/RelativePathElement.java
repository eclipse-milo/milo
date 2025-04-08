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
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.31">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.31</a>
 */
public class RelativePathElement extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=537");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=539");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=538");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15188");

  private final NodeId referenceTypeId;

  private final Boolean isInverse;

  private final Boolean includeSubtypes;

  private final QualifiedName targetName;

  public RelativePathElement(
      NodeId referenceTypeId,
      Boolean isInverse,
      Boolean includeSubtypes,
      QualifiedName targetName) {
    this.referenceTypeId = referenceTypeId;
    this.isInverse = isInverse;
    this.includeSubtypes = includeSubtypes;
    this.targetName = targetName;
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

  public NodeId getReferenceTypeId() {
    return referenceTypeId;
  }

  public Boolean getIsInverse() {
    return isInverse;
  }

  public Boolean getIncludeSubtypes() {
    return includeSubtypes;
  }

  public QualifiedName getTargetName() {
    return targetName;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RelativePathElement that = (RelativePathElement) object;
    var eqb = new EqualsBuilder();
    eqb.append(getReferenceTypeId(), that.getReferenceTypeId());
    eqb.append(getIsInverse(), that.getIsInverse());
    eqb.append(getIncludeSubtypes(), that.getIncludeSubtypes());
    eqb.append(getTargetName(), that.getTargetName());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getReferenceTypeId());
    hcb.append(getIsInverse());
    hcb.append(getIncludeSubtypes());
    hcb.append(getTargetName());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", RelativePathElement.class.getSimpleName() + "[", "]");
    joiner.add("referenceTypeId=" + getReferenceTypeId());
    joiner.add("isInverse=" + getIsInverse());
    joiner.add("includeSubtypes=" + getIncludeSubtypes());
    joiner.add("targetName=" + getTargetName());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 539),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ReferenceTypeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IsInverse",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IncludeSubtypes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TargetName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<RelativePathElement> {
    @Override
    public Class<RelativePathElement> getType() {
      return RelativePathElement.class;
    }

    @Override
    public RelativePathElement decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId referenceTypeId;
      final Boolean isInverse;
      final Boolean includeSubtypes;
      final QualifiedName targetName;
      referenceTypeId = decoder.decodeNodeId("ReferenceTypeId");
      isInverse = decoder.decodeBoolean("IsInverse");
      includeSubtypes = decoder.decodeBoolean("IncludeSubtypes");
      targetName = decoder.decodeQualifiedName("TargetName");
      return new RelativePathElement(referenceTypeId, isInverse, includeSubtypes, targetName);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, RelativePathElement value) {
      encoder.encodeNodeId("ReferenceTypeId", value.getReferenceTypeId());
      encoder.encodeBoolean("IsInverse", value.getIsInverse());
      encoder.encodeBoolean("IncludeSubtypes", value.getIncludeSubtypes());
      encoder.encodeQualifiedName("TargetName", value.getTargetName());
    }
  }
}
