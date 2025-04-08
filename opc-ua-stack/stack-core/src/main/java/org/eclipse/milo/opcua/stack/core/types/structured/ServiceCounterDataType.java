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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.13">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.13</a>
 */
public class ServiceCounterDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=871");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=873");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=872");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15370");

  private final UInteger totalCount;

  private final UInteger errorCount;

  public ServiceCounterDataType(UInteger totalCount, UInteger errorCount) {
    this.totalCount = totalCount;
    this.errorCount = errorCount;
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

  public UInteger getTotalCount() {
    return totalCount;
  }

  public UInteger getErrorCount() {
    return errorCount;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ServiceCounterDataType that = (ServiceCounterDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getTotalCount(), that.getTotalCount());
    eqb.append(getErrorCount(), that.getErrorCount());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTotalCount());
    hcb.append(getErrorCount());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ServiceCounterDataType.class.getSimpleName() + "[", "]");
    joiner.add("totalCount=" + getTotalCount());
    joiner.add("errorCount=" + getErrorCount());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 873),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "TotalCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ErrorCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ServiceCounterDataType> {
    @Override
    public Class<ServiceCounterDataType> getType() {
      return ServiceCounterDataType.class;
    }

    @Override
    public ServiceCounterDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger totalCount;
      final UInteger errorCount;
      totalCount = decoder.decodeUInt32("TotalCount");
      errorCount = decoder.decodeUInt32("ErrorCount");
      return new ServiceCounterDataType(totalCount, errorCount);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ServiceCounterDataType value) {
      encoder.encodeUInt32("TotalCount", value.getTotalCount());
      encoder.encodeUInt32("ErrorCount", value.getErrorCount());
    }
  }
}
