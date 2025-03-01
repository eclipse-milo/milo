package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.21">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.21</a>
 */
public class MonitoringParameters extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=740");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=742");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=741");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15320");

  private final UInteger clientHandle;

  private final Double samplingInterval;

  private final ExtensionObject filter;

  private final UInteger queueSize;

  private final Boolean discardOldest;

  public MonitoringParameters(
      UInteger clientHandle,
      Double samplingInterval,
      ExtensionObject filter,
      UInteger queueSize,
      Boolean discardOldest) {
    this.clientHandle = clientHandle;
    this.samplingInterval = samplingInterval;
    this.filter = filter;
    this.queueSize = queueSize;
    this.discardOldest = discardOldest;
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

  public Double getSamplingInterval() {
    return samplingInterval;
  }

  public ExtensionObject getFilter() {
    return filter;
  }

  public UInteger getQueueSize() {
    return queueSize;
  }

  public Boolean getDiscardOldest() {
    return discardOldest;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    MonitoringParameters that = (MonitoringParameters) object;
    var eqb = new EqualsBuilder();
    eqb.append(getClientHandle(), that.getClientHandle());
    eqb.append(getSamplingInterval(), that.getSamplingInterval());
    eqb.append(getFilter(), that.getFilter());
    eqb.append(getQueueSize(), that.getQueueSize());
    eqb.append(getDiscardOldest(), that.getDiscardOldest());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getClientHandle());
    hcb.append(getSamplingInterval());
    hcb.append(getFilter());
    hcb.append(getQueueSize());
    hcb.append(getDiscardOldest());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", MonitoringParameters.class.getSimpleName() + "[", "]");
    joiner.add("clientHandle=" + getClientHandle());
    joiner.add("samplingInterval=" + getSamplingInterval());
    joiner.add("filter=" + getFilter());
    joiner.add("queueSize=" + getQueueSize());
    joiner.add("discardOldest=" + getDiscardOldest());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 742),
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
              "SamplingInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Filter",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "QueueSize",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiscardOldest",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<MonitoringParameters> {
    @Override
    public Class<MonitoringParameters> getType() {
      return MonitoringParameters.class;
    }

    @Override
    public MonitoringParameters decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger clientHandle;
      final Double samplingInterval;
      final ExtensionObject filter;
      final UInteger queueSize;
      final Boolean discardOldest;
      clientHandle = decoder.decodeUInt32("ClientHandle");
      samplingInterval = decoder.decodeDouble("SamplingInterval");
      filter = decoder.decodeExtensionObject("Filter");
      queueSize = decoder.decodeUInt32("QueueSize");
      discardOldest = decoder.decodeBoolean("DiscardOldest");
      return new MonitoringParameters(
          clientHandle, samplingInterval, filter, queueSize, discardOldest);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, MonitoringParameters value) {
      encoder.encodeUInt32("ClientHandle", value.getClientHandle());
      encoder.encodeDouble("SamplingInterval", value.getSamplingInterval());
      encoder.encodeExtensionObject("Filter", value.getFilter());
      encoder.encodeUInt32("QueueSize", value.getQueueSize());
      encoder.encodeBoolean("DiscardOldest", value.getDiscardOldest());
    }
  }
}
