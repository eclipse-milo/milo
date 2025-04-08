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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2</a>
 */
public class PublishRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=824");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=826");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=825");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15352");

  private final RequestHeader requestHeader;

  private final SubscriptionAcknowledgement @Nullable [] subscriptionAcknowledgements;

  public PublishRequest(
      RequestHeader requestHeader,
      SubscriptionAcknowledgement @Nullable [] subscriptionAcknowledgements) {
    this.requestHeader = requestHeader;
    this.subscriptionAcknowledgements = subscriptionAcknowledgements;
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

  public SubscriptionAcknowledgement @Nullable [] getSubscriptionAcknowledgements() {
    return subscriptionAcknowledgements;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PublishRequest that = (PublishRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getSubscriptionAcknowledgements(), that.getSubscriptionAcknowledgements());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getSubscriptionAcknowledgements());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PublishRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add(
        "subscriptionAcknowledgements="
            + java.util.Arrays.toString(getSubscriptionAcknowledgements()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 826),
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
              "SubscriptionAcknowledgements",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 821),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PublishRequest> {
    @Override
    public Class<PublishRequest> getType() {
      return PublishRequest.class;
    }

    @Override
    public PublishRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final SubscriptionAcknowledgement[] subscriptionAcknowledgements;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      subscriptionAcknowledgements =
          (SubscriptionAcknowledgement[])
              decoder.decodeStructArray(
                  "SubscriptionAcknowledgements", SubscriptionAcknowledgement.TYPE_ID);
      return new PublishRequest(requestHeader, subscriptionAcknowledgements);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, PublishRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeStructArray(
          "SubscriptionAcknowledgements",
          value.getSubscriptionAcknowledgements(),
          SubscriptionAcknowledgement.TYPE_ID);
    }
  }
}
