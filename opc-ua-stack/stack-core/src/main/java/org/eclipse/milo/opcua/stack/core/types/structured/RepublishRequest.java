package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.6/#5.14.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.6/#5.14.6.2</a>
 */
public class RepublishRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=830");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=832");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=831");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15354");

  private final RequestHeader requestHeader;

  private final UInteger subscriptionId;

  private final UInteger retransmitSequenceNumber;

  public RepublishRequest(
      RequestHeader requestHeader, UInteger subscriptionId, UInteger retransmitSequenceNumber) {
    this.requestHeader = requestHeader;
    this.subscriptionId = subscriptionId;
    this.retransmitSequenceNumber = retransmitSequenceNumber;
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

  public RequestHeader getRequestHeader() {
    return requestHeader;
  }

  public UInteger getSubscriptionId() {
    return subscriptionId;
  }

  public UInteger getRetransmitSequenceNumber() {
    return retransmitSequenceNumber;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RepublishRequest that = (RepublishRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getSubscriptionId(), that.getSubscriptionId());
    eqb.append(getRetransmitSequenceNumber(), that.getRetransmitSequenceNumber());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getSubscriptionId());
    hcb.append(getRetransmitSequenceNumber());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", RepublishRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("subscriptionId=" + getSubscriptionId());
    joiner.add("retransmitSequenceNumber=" + getRetransmitSequenceNumber());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 832),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "RequestHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 389),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SubscriptionId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RetransmitSequenceNumber",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<RepublishRequest> {
    @Override
    public Class<RepublishRequest> getType() {
      return RepublishRequest.class;
    }

    @Override
    public RepublishRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final UInteger subscriptionId;
      final UInteger retransmitSequenceNumber;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      subscriptionId = decoder.decodeUInt32("SubscriptionId");
      retransmitSequenceNumber = decoder.decodeUInt32("RetransmitSequenceNumber");
      return new RepublishRequest(requestHeader, subscriptionId, retransmitSequenceNumber);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, RepublishRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeUInt32("SubscriptionId", value.getSubscriptionId());
      encoder.encodeUInt32("RetransmitSequenceNumber", value.getRetransmitSequenceNumber());
    }
  }
}
