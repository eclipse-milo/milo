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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.5">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.5</a>
 */
public class StructureDefinition extends DataTypeDefinition implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=99");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=122");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=14798");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15066");

  private final NodeId defaultEncodingId;

  private final NodeId baseDataType;

  private final StructureType structureType;

  private final StructureField @Nullable [] fields;

  public StructureDefinition(
      NodeId defaultEncodingId,
      NodeId baseDataType,
      StructureType structureType,
      StructureField @Nullable [] fields) {
    this.defaultEncodingId = defaultEncodingId;
    this.baseDataType = baseDataType;
    this.structureType = structureType;
    this.fields = fields;
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

  public NodeId getDefaultEncodingId() {
    return defaultEncodingId;
  }

  public NodeId getBaseDataType() {
    return baseDataType;
  }

  public StructureType getStructureType() {
    return structureType;
  }

  public StructureField @Nullable [] getFields() {
    return fields;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructureDefinition that = (StructureDefinition) object;
    var eqb = new EqualsBuilder();
    eqb.append(getDefaultEncodingId(), that.getDefaultEncodingId());
    eqb.append(getBaseDataType(), that.getBaseDataType());
    eqb.append(getStructureType(), that.getStructureType());
    eqb.append(getFields(), that.getFields());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getDefaultEncodingId());
    hcb.append(getBaseDataType());
    hcb.append(getStructureType());
    hcb.append(getFields());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", StructureDefinition.class.getSimpleName() + "[", "]");
    joiner.add("defaultEncodingId=" + getDefaultEncodingId());
    joiner.add("baseDataType=" + getBaseDataType());
    joiner.add("structureType=" + getStructureType());
    joiner.add("fields=" + java.util.Arrays.toString(getFields()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 122),
        new NodeId(0, 97),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "DefaultEncodingId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
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
              "StructureType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 98),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Fields",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 101),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructureDefinition> {
    @Override
    public Class<StructureDefinition> getType() {
      return StructureDefinition.class;
    }

    @Override
    public StructureDefinition decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId defaultEncodingId;
      final NodeId baseDataType;
      final StructureType structureType;
      final StructureField[] fields;
      defaultEncodingId = decoder.decodeNodeId("DefaultEncodingId");
      baseDataType = decoder.decodeNodeId("BaseDataType");
      structureType = StructureType.from(decoder.decodeEnum("StructureType"));
      fields = (StructureField[]) decoder.decodeStructArray("Fields", StructureField.TYPE_ID);
      return new StructureDefinition(defaultEncodingId, baseDataType, structureType, fields);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, StructureDefinition value) {
      encoder.encodeNodeId("DefaultEncodingId", value.getDefaultEncodingId());
      encoder.encodeNodeId("BaseDataType", value.getBaseDataType());
      encoder.encodeEnum("StructureType", value.getStructureType());
      encoder.encodeStructArray("Fields", value.getFields(), StructureField.TYPE_ID);
    }
  }
}
