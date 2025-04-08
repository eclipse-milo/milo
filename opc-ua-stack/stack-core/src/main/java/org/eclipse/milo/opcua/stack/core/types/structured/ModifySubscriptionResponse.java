package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.3/#5.14.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.3/#5.14.3.2</a>
 */
public class ModifySubscriptionResponse extends Structure implements UaResponseMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=794");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=796");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=795");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15340");

  private final ResponseHeader responseHeader;

  private final Double revisedPublishingInterval;

  private final UInteger revisedLifetimeCount;

  private final UInteger revisedMaxKeepAliveCount;

  public ModifySubscriptionResponse(
      ResponseHeader responseHeader,
      Double revisedPublishingInterval,
      UInteger revisedLifetimeCount,
      UInteger revisedMaxKeepAliveCount) {
    this.responseHeader = responseHeader;
    this.revisedPublishingInterval = revisedPublishingInterval;
    this.revisedLifetimeCount = revisedLifetimeCount;
    this.revisedMaxKeepAliveCount = revisedMaxKeepAliveCount;
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

  public ResponseHeader getResponseHeader() {
    return responseHeader;
  }

  public Double getRevisedPublishingInterval() {
    return revisedPublishingInterval;
  }

  public UInteger getRevisedLifetimeCount() {
    return revisedLifetimeCount;
  }

  public UInteger getRevisedMaxKeepAliveCount() {
    return revisedMaxKeepAliveCount;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ModifySubscriptionResponse that = (ModifySubscriptionResponse) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResponseHeader(), that.getResponseHeader());
    eqb.append(getRevisedPublishingInterval(), that.getRevisedPublishingInterval());
    eqb.append(getRevisedLifetimeCount(), that.getRevisedLifetimeCount());
    eqb.append(getRevisedMaxKeepAliveCount(), that.getRevisedMaxKeepAliveCount());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResponseHeader());
    hcb.append(getRevisedPublishingInterval());
    hcb.append(getRevisedLifetimeCount());
    hcb.append(getRevisedMaxKeepAliveCount());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", ModifySubscriptionResponse.class.getSimpleName() + "[", "]");
    joiner.add("responseHeader=" + getResponseHeader());
    joiner.add("revisedPublishingInterval=" + getRevisedPublishingInterval());
    joiner.add("revisedLifetimeCount=" + getRevisedLifetimeCount());
    joiner.add("revisedMaxKeepAliveCount=" + getRevisedMaxKeepAliveCount());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 796),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ResponseHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 392),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RevisedPublishingInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RevisedLifetimeCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RevisedMaxKeepAliveCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ModifySubscriptionResponse> {
    @Override
    public Class<ModifySubscriptionResponse> getType() {
      return ModifySubscriptionResponse.class;
    }

    @Override
    public ModifySubscriptionResponse decodeType(EncodingContext context, UaDecoder decoder) {
      final ResponseHeader responseHeader;
      final Double revisedPublishingInterval;
      final UInteger revisedLifetimeCount;
      final UInteger revisedMaxKeepAliveCount;
      responseHeader =
          (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
      revisedPublishingInterval = decoder.decodeDouble("RevisedPublishingInterval");
      revisedLifetimeCount = decoder.decodeUInt32("RevisedLifetimeCount");
      revisedMaxKeepAliveCount = decoder.decodeUInt32("RevisedMaxKeepAliveCount");
      return new ModifySubscriptionResponse(
          responseHeader,
          revisedPublishingInterval,
          revisedLifetimeCount,
          revisedMaxKeepAliveCount);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ModifySubscriptionResponse value) {
      encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
      encoder.encodeDouble("RevisedPublishingInterval", value.getRevisedPublishingInterval());
      encoder.encodeUInt32("RevisedLifetimeCount", value.getRevisedLifetimeCount());
      encoder.encodeUInt32("RevisedMaxKeepAliveCount", value.getRevisedMaxKeepAliveCount());
    }
  }
}
