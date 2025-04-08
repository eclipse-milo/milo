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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.35">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.35</a>
 */
public class SimpleTypeDescription extends DataTypeDescription implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15005");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15421");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15529");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15700");

  private final NodeId baseDataType;

  private final UByte builtInType;

  public SimpleTypeDescription(
      NodeId dataTypeId, QualifiedName name, NodeId baseDataType, UByte builtInType) {
    super(dataTypeId, name);
    this.baseDataType = baseDataType;
    this.builtInType = builtInType;
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

  public NodeId getBaseDataType() {
    return baseDataType;
  }

  public UByte getBuiltInType() {
    return builtInType;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SimpleTypeDescription that = (SimpleTypeDescription) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getBaseDataType(), that.getBaseDataType());
    eqb.append(getBuiltInType(), that.getBuiltInType());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getBaseDataType());
    hcb.append(getBuiltInType());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", SimpleTypeDescription.class.getSimpleName() + "[", "]");
    joiner.add("baseDataType=" + getBaseDataType());
    joiner.add("builtInType=" + getBuiltInType());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15421),
        new NodeId(0, 14525),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "DataTypeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BaseDataType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BuiltInType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SimpleTypeDescription> {
    @Override
    public Class<SimpleTypeDescription> getType() {
      return SimpleTypeDescription.class;
    }

    @Override
    public SimpleTypeDescription decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId dataTypeId;
      final QualifiedName name;
      final NodeId baseDataType;
      final UByte builtInType;
      dataTypeId = decoder.decodeNodeId("DataTypeId");
      name = decoder.decodeQualifiedName("Name");
      baseDataType = decoder.decodeNodeId("BaseDataType");
      builtInType = decoder.decodeByte("BuiltInType");
      return new SimpleTypeDescription(dataTypeId, name, baseDataType, builtInType);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SimpleTypeDescription value) {
      encoder.encodeNodeId("DataTypeId", value.getDataTypeId());
      encoder.encodeQualifiedName("Name", value.getName());
      encoder.encodeNodeId("BaseDataType", value.getBaseDataType());
      encoder.encodeByte("BuiltInType", value.getBuiltInType());
    }
  }
}
