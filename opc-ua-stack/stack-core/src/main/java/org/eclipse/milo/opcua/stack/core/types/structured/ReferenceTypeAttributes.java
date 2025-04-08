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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.24.7">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.24.7</a>
 */
public class ReferenceTypeAttributes extends NodeAttributes implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=367");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=369");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=368");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15160");

  private final Boolean isAbstract;

  private final Boolean symmetric;

  private final LocalizedText inverseName;

  public ReferenceTypeAttributes(
      UInteger specifiedAttributes,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      Boolean isAbstract,
      Boolean symmetric,
      LocalizedText inverseName) {
    super(specifiedAttributes, displayName, description, writeMask, userWriteMask);
    this.isAbstract = isAbstract;
    this.symmetric = symmetric;
    this.inverseName = inverseName;
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

  public Boolean getIsAbstract() {
    return isAbstract;
  }

  public Boolean getSymmetric() {
    return symmetric;
  }

  public LocalizedText getInverseName() {
    return inverseName;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReferenceTypeAttributes that = (ReferenceTypeAttributes) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getIsAbstract(), that.getIsAbstract());
    eqb.append(getSymmetric(), that.getSymmetric());
    eqb.append(getInverseName(), that.getInverseName());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getIsAbstract());
    hcb.append(getSymmetric());
    hcb.append(getInverseName());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReferenceTypeAttributes.class.getSimpleName() + "[", "]");
    joiner.add("isAbstract=" + getIsAbstract());
    joiner.add("symmetric=" + getSymmetric());
    joiner.add("inverseName=" + getInverseName());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 369),
        new NodeId(0, 349),
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
              false),
          new StructureField(
              "IsAbstract",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Symmetric",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "InverseName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReferenceTypeAttributes> {
    @Override
    public Class<ReferenceTypeAttributes> getType() {
      return ReferenceTypeAttributes.class;
    }

    @Override
    public ReferenceTypeAttributes decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger specifiedAttributes;
      final LocalizedText displayName;
      final LocalizedText description;
      final UInteger writeMask;
      final UInteger userWriteMask;
      final Boolean isAbstract;
      final Boolean symmetric;
      final LocalizedText inverseName;
      specifiedAttributes = decoder.decodeUInt32("SpecifiedAttributes");
      displayName = decoder.decodeLocalizedText("DisplayName");
      description = decoder.decodeLocalizedText("Description");
      writeMask = decoder.decodeUInt32("WriteMask");
      userWriteMask = decoder.decodeUInt32("UserWriteMask");
      isAbstract = decoder.decodeBoolean("IsAbstract");
      symmetric = decoder.decodeBoolean("Symmetric");
      inverseName = decoder.decodeLocalizedText("InverseName");
      return new ReferenceTypeAttributes(
          specifiedAttributes,
          displayName,
          description,
          writeMask,
          userWriteMask,
          isAbstract,
          symmetric,
          inverseName);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ReferenceTypeAttributes value) {
      encoder.encodeUInt32("SpecifiedAttributes", value.getSpecifiedAttributes());
      encoder.encodeLocalizedText("DisplayName", value.getDisplayName());
      encoder.encodeLocalizedText("Description", value.getDescription());
      encoder.encodeUInt32("WriteMask", value.getWriteMask());
      encoder.encodeUInt32("UserWriteMask", value.getUserWriteMask());
      encoder.encodeBoolean("IsAbstract", value.getIsAbstract());
      encoder.encodeBoolean("Symmetric", value.getSymmetric());
      encoder.encodeLocalizedText("InverseName", value.getInverseName());
    }
  }
}
