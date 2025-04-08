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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.4/#5.14.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.4/#5.14.4.2</a>
 */
public class SetPublishingModeRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=797");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=799");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=798");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15341");

  private final RequestHeader requestHeader;

  private final Boolean publishingEnabled;

  private final UInteger @Nullable [] subscriptionIds;

  public SetPublishingModeRequest(
      RequestHeader requestHeader,
      Boolean publishingEnabled,
      UInteger @Nullable [] subscriptionIds) {
    this.requestHeader = requestHeader;
    this.publishingEnabled = publishingEnabled;
    this.subscriptionIds = subscriptionIds;
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

  public Boolean getPublishingEnabled() {
    return publishingEnabled;
  }

  public UInteger @Nullable [] getSubscriptionIds() {
    return subscriptionIds;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SetPublishingModeRequest that = (SetPublishingModeRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getPublishingEnabled(), that.getPublishingEnabled());
    eqb.append(getSubscriptionIds(), that.getSubscriptionIds());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getPublishingEnabled());
    hcb.append(getSubscriptionIds());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", SetPublishingModeRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("publishingEnabled=" + getPublishingEnabled());
    joiner.add("subscriptionIds=" + java.util.Arrays.toString(getSubscriptionIds()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 799),
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
              "PublishingEnabled",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SubscriptionIds",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SetPublishingModeRequest> {
    @Override
    public Class<SetPublishingModeRequest> getType() {
      return SetPublishingModeRequest.class;
    }

    @Override
    public SetPublishingModeRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final Boolean publishingEnabled;
      final UInteger[] subscriptionIds;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      publishingEnabled = decoder.decodeBoolean("PublishingEnabled");
      subscriptionIds = decoder.decodeUInt32Array("SubscriptionIds");
      return new SetPublishingModeRequest(requestHeader, publishingEnabled, subscriptionIds);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SetPublishingModeRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeBoolean("PublishingEnabled", value.getPublishingEnabled());
      encoder.encodeUInt32Array("SubscriptionIds", value.getSubscriptionIds());
    }
  }
}
