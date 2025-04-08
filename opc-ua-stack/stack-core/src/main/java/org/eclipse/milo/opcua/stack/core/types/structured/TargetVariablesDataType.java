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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.10/#6.2.10.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.10/#6.2.10.2.2</a>
 */
public class TargetVariablesDataType extends SubscribedDataSetDataType implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15631");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15712");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=16011");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=16310");

  private final FieldTargetDataType @Nullable [] targetVariables;

  public TargetVariablesDataType(FieldTargetDataType @Nullable [] targetVariables) {
    this.targetVariables = targetVariables;
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

  public FieldTargetDataType @Nullable [] getTargetVariables() {
    return targetVariables;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    TargetVariablesDataType that = (TargetVariablesDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getTargetVariables(), that.getTargetVariables());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTargetVariables());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", TargetVariablesDataType.class.getSimpleName() + "[", "]");
    joiner.add("targetVariables=" + java.util.Arrays.toString(getTargetVariables()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15712),
        new NodeId(0, 15630),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "TargetVariables",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14744),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<TargetVariablesDataType> {
    @Override
    public Class<TargetVariablesDataType> getType() {
      return TargetVariablesDataType.class;
    }

    @Override
    public TargetVariablesDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final FieldTargetDataType[] targetVariables;
      targetVariables =
          (FieldTargetDataType[])
              decoder.decodeStructArray("TargetVariables", FieldTargetDataType.TYPE_ID);
      return new TargetVariablesDataType(targetVariables);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, TargetVariablesDataType value) {
      encoder.encodeStructArray(
          "TargetVariables", value.getTargetVariables(), FieldTargetDataType.TYPE_ID);
    }
  }
}
