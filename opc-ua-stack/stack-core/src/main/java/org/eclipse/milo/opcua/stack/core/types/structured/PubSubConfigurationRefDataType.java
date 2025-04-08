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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.3</a>
 */
public class PubSubConfigurationRefDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=25519");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=25531");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=25547");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=25563");

  private final PubSubConfigurationRefMask configurationMask;

  private final UShort elementIndex;

  private final UShort connectionIndex;

  private final UShort groupIndex;

  public PubSubConfigurationRefDataType(
      PubSubConfigurationRefMask configurationMask,
      UShort elementIndex,
      UShort connectionIndex,
      UShort groupIndex) {
    this.configurationMask = configurationMask;
    this.elementIndex = elementIndex;
    this.connectionIndex = connectionIndex;
    this.groupIndex = groupIndex;
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

  public PubSubConfigurationRefMask getConfigurationMask() {
    return configurationMask;
  }

  public UShort getElementIndex() {
    return elementIndex;
  }

  public UShort getConnectionIndex() {
    return connectionIndex;
  }

  public UShort getGroupIndex() {
    return groupIndex;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PubSubConfigurationRefDataType that = (PubSubConfigurationRefDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getConfigurationMask(), that.getConfigurationMask());
    eqb.append(getElementIndex(), that.getElementIndex());
    eqb.append(getConnectionIndex(), that.getConnectionIndex());
    eqb.append(getGroupIndex(), that.getGroupIndex());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getConfigurationMask());
    hcb.append(getElementIndex());
    hcb.append(getConnectionIndex());
    hcb.append(getGroupIndex());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", PubSubConfigurationRefDataType.class.getSimpleName() + "[", "]");
    joiner.add("configurationMask=" + getConfigurationMask());
    joiner.add("elementIndex=" + getElementIndex());
    joiner.add("connectionIndex=" + getConnectionIndex());
    joiner.add("groupIndex=" + getGroupIndex());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 25531),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ConfigurationMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25517),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ElementIndex",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ConnectionIndex",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "GroupIndex",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PubSubConfigurationRefDataType> {
    @Override
    public Class<PubSubConfigurationRefDataType> getType() {
      return PubSubConfigurationRefDataType.class;
    }

    @Override
    public PubSubConfigurationRefDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final PubSubConfigurationRefMask configurationMask;
      final UShort elementIndex;
      final UShort connectionIndex;
      final UShort groupIndex;
      configurationMask = new PubSubConfigurationRefMask(decoder.decodeUInt32("ConfigurationMask"));
      elementIndex = decoder.decodeUInt16("ElementIndex");
      connectionIndex = decoder.decodeUInt16("ConnectionIndex");
      groupIndex = decoder.decodeUInt16("GroupIndex");
      return new PubSubConfigurationRefDataType(
          configurationMask, elementIndex, connectionIndex, groupIndex);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PubSubConfigurationRefDataType value) {
      encoder.encodeUInt32("ConfigurationMask", value.getConfigurationMask().getValue());
      encoder.encodeUInt16("ElementIndex", value.getElementIndex());
      encoder.encodeUInt16("ConnectionIndex", value.getConnectionIndex());
      encoder.encodeUInt16("GroupIndex", value.getGroupIndex());
    }
  }
}
