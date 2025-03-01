package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.4/#5.11.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.4/#5.11.4.2</a>
 */
public class WriteValue extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=668");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=670");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=669");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15276");

  private final NodeId nodeId;

  private final UInteger attributeId;

  private final String indexRange;

  private final DataValue value;

  public WriteValue(NodeId nodeId, UInteger attributeId, String indexRange, DataValue value) {
    this.nodeId = nodeId;
    this.attributeId = attributeId;
    this.indexRange = indexRange;
    this.value = value;
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

  public UInteger getAttributeId() {
    return attributeId;
  }

  public String getIndexRange() {
    return indexRange;
  }

  public DataValue getValue() {
    return value;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    WriteValue that = (WriteValue) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getAttributeId(), that.getAttributeId());
    eqb.append(getIndexRange(), that.getIndexRange());
    eqb.append(getValue(), that.getValue());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNodeId());
    hcb.append(getAttributeId());
    hcb.append(getIndexRange());
    hcb.append(getValue());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", WriteValue.class.getSimpleName() + "[", "]");
    joiner.add("nodeId=" + getNodeId());
    joiner.add("attributeId=" + getAttributeId());
    joiner.add("indexRange='" + getIndexRange() + "'");
    joiner.add("value=" + getValue());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 670),
        new NodeId(0, 22),
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
              false),
          new StructureField(
              "Value",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<WriteValue> {
    @Override
    public Class<WriteValue> getType() {
      return WriteValue.class;
    }

    @Override
    public WriteValue decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId nodeId;
      final UInteger attributeId;
      final String indexRange;
      final DataValue value;
      nodeId = decoder.decodeNodeId("NodeId");
      attributeId = decoder.decodeUInt32("AttributeId");
      indexRange = decoder.decodeString("IndexRange");
      value = decoder.decodeDataValue("Value");
      return new WriteValue(nodeId, attributeId, indexRange, value);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, WriteValue value) {
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeUInt32("AttributeId", value.getAttributeId());
      encoder.encodeString("IndexRange", value.getIndexRange());
      encoder.encodeDataValue("Value", value.getValue());
    }
  }
}
