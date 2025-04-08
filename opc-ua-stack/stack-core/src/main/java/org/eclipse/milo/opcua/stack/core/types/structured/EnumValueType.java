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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.6">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.6</a>
 */
public class EnumValueType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=7594");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=8251");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=7616");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15082");

  private final Long value;

  private final LocalizedText displayName;

  private final LocalizedText description;

  public EnumValueType(Long value, LocalizedText displayName, LocalizedText description) {
    this.value = value;
    this.displayName = displayName;
    this.description = description;
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

  public Long getValue() {
    return value;
  }

  public LocalizedText getDisplayName() {
    return displayName;
  }

  public LocalizedText getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    EnumValueType that = (EnumValueType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getValue(), that.getValue());
    eqb.append(getDisplayName(), that.getDisplayName());
    eqb.append(getDescription(), that.getDescription());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getValue());
    hcb.append(getDisplayName());
    hcb.append(getDescription());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", EnumValueType.class.getSimpleName() + "[", "]");
    joiner.add("value=" + getValue());
    joiner.add("displayName=" + getDisplayName());
    joiner.add("description=" + getDescription());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 8251),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Value",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 8),
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
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<EnumValueType> {
    @Override
    public Class<EnumValueType> getType() {
      return EnumValueType.class;
    }

    @Override
    public EnumValueType decodeType(EncodingContext context, UaDecoder decoder) {
      final Long value;
      final LocalizedText displayName;
      final LocalizedText description;
      value = decoder.decodeInt64("Value");
      displayName = decoder.decodeLocalizedText("DisplayName");
      description = decoder.decodeLocalizedText("Description");
      return new EnumValueType(value, displayName, description);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, EnumValueType value) {
      encoder.encodeInt64("Value", value.getValue());
      encoder.encodeLocalizedText("DisplayName", value.getDisplayName());
      encoder.encodeLocalizedText("Description", value.getDescription());
    }
  }
}
