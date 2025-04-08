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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.2/#5.13.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.2/#5.13.2.2</a>
 */
public class MonitoredItemCreateRequest extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=743");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=745");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=744");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15321");

  private final ReadValueId itemToMonitor;

  private final MonitoringMode monitoringMode;

  private final MonitoringParameters requestedParameters;

  public MonitoredItemCreateRequest(
      ReadValueId itemToMonitor,
      MonitoringMode monitoringMode,
      MonitoringParameters requestedParameters) {
    this.itemToMonitor = itemToMonitor;
    this.monitoringMode = monitoringMode;
    this.requestedParameters = requestedParameters;
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

  public ReadValueId getItemToMonitor() {
    return itemToMonitor;
  }

  public MonitoringMode getMonitoringMode() {
    return monitoringMode;
  }

  public MonitoringParameters getRequestedParameters() {
    return requestedParameters;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    MonitoredItemCreateRequest that = (MonitoredItemCreateRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getItemToMonitor(), that.getItemToMonitor());
    eqb.append(getMonitoringMode(), that.getMonitoringMode());
    eqb.append(getRequestedParameters(), that.getRequestedParameters());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getItemToMonitor());
    hcb.append(getMonitoringMode());
    hcb.append(getRequestedParameters());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", MonitoredItemCreateRequest.class.getSimpleName() + "[", "]");
    joiner.add("itemToMonitor=" + getItemToMonitor());
    joiner.add("monitoringMode=" + getMonitoringMode());
    joiner.add("requestedParameters=" + getRequestedParameters());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 745),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ItemToMonitor",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 626),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MonitoringMode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 716),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestedParameters",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 740),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<MonitoredItemCreateRequest> {
    @Override
    public Class<MonitoredItemCreateRequest> getType() {
      return MonitoredItemCreateRequest.class;
    }

    @Override
    public MonitoredItemCreateRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final ReadValueId itemToMonitor;
      final MonitoringMode monitoringMode;
      final MonitoringParameters requestedParameters;
      itemToMonitor = (ReadValueId) decoder.decodeStruct("ItemToMonitor", ReadValueId.TYPE_ID);
      monitoringMode = MonitoringMode.from(decoder.decodeEnum("MonitoringMode"));
      requestedParameters =
          (MonitoringParameters)
              decoder.decodeStruct("RequestedParameters", MonitoringParameters.TYPE_ID);
      return new MonitoredItemCreateRequest(itemToMonitor, monitoringMode, requestedParameters);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, MonitoredItemCreateRequest value) {
      encoder.encodeStruct("ItemToMonitor", value.getItemToMonitor(), ReadValueId.TYPE_ID);
      encoder.encodeEnum("MonitoringMode", value.getMonitoringMode());
      encoder.encodeStruct(
          "RequestedParameters", value.getRequestedParameters(), MonitoringParameters.TYPE_ID);
    }
  }
}
