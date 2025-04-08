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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.2</a>
 */
public class MonitoredItemNotification extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=806");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=808");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=807");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15346");

  private final UInteger clientHandle;

  private final DataValue value;

  public MonitoredItemNotification(UInteger clientHandle, DataValue value) {
    this.clientHandle = clientHandle;
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

  public UInteger getClientHandle() {
    return clientHandle;
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
    MonitoredItemNotification that = (MonitoredItemNotification) object;
    var eqb = new EqualsBuilder();
    eqb.append(getClientHandle(), that.getClientHandle());
    eqb.append(getValue(), that.getValue());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getClientHandle());
    hcb.append(getValue());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", MonitoredItemNotification.class.getSimpleName() + "[", "]");
    joiner.add("clientHandle=" + getClientHandle());
    joiner.add("value=" + getValue());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 808),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ClientHandle",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
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

  public static final class Codec extends GenericDataTypeCodec<MonitoredItemNotification> {
    @Override
    public Class<MonitoredItemNotification> getType() {
      return MonitoredItemNotification.class;
    }

    @Override
    public MonitoredItemNotification decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger clientHandle;
      final DataValue value;
      clientHandle = decoder.decodeUInt32("ClientHandle");
      value = decoder.decodeDataValue("Value");
      return new MonitoredItemNotification(clientHandle, value);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, MonitoredItemNotification value) {
      encoder.encodeUInt32("ClientHandle", value.getClientHandle());
      encoder.encodeDataValue("Value", value.getValue());
    }
  }
}
