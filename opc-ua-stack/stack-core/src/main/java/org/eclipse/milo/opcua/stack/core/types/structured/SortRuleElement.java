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
import org.eclipse.milo.opcua.stack.core.types.enumerated.SortOrderType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.7">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.7</a>
 */
public class SortRuleElement extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=18648");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=18650");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=18652");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=18654");

  private final SortOrderType sortOrder;

  private final SimpleAttributeOperand eventField;

  public SortRuleElement(SortOrderType sortOrder, SimpleAttributeOperand eventField) {
    this.sortOrder = sortOrder;
    this.eventField = eventField;
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

  public SortOrderType getSortOrder() {
    return sortOrder;
  }

  public SimpleAttributeOperand getEventField() {
    return eventField;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SortRuleElement that = (SortRuleElement) object;
    var eqb = new EqualsBuilder();
    eqb.append(getSortOrder(), that.getSortOrder());
    eqb.append(getEventField(), that.getEventField());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSortOrder());
    hcb.append(getEventField());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", SortRuleElement.class.getSimpleName() + "[", "]");
    joiner.add("sortOrder=" + getSortOrder());
    joiner.add("eventField=" + getEventField());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 18650),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SortOrder",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18646),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EventField",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 601),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SortRuleElement> {
    @Override
    public Class<SortRuleElement> getType() {
      return SortRuleElement.class;
    }

    @Override
    public SortRuleElement decodeType(EncodingContext context, UaDecoder decoder) {
      final SortOrderType sortOrder;
      final SimpleAttributeOperand eventField;
      sortOrder = SortOrderType.from(decoder.decodeEnum("SortOrder"));
      eventField =
          (SimpleAttributeOperand)
              decoder.decodeStruct("EventField", SimpleAttributeOperand.TYPE_ID);
      return new SortRuleElement(sortOrder, eventField);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, SortRuleElement value) {
      encoder.encodeEnum("SortOrder", value.getSortOrder());
      encoder.encodeStruct("EventField", value.getEventField(), SimpleAttributeOperand.TYPE_ID);
    }
  }
}
