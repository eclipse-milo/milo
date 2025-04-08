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
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.2/#5.13.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.2/#5.13.2.2</a>
 */
public class CreateMonitoredItemsRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=749");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=751");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=750");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15323");

  private final RequestHeader requestHeader;

  private final UInteger subscriptionId;

  private final TimestampsToReturn timestampsToReturn;

  private final MonitoredItemCreateRequest @Nullable [] itemsToCreate;

  public CreateMonitoredItemsRequest(
      RequestHeader requestHeader,
      UInteger subscriptionId,
      TimestampsToReturn timestampsToReturn,
      MonitoredItemCreateRequest @Nullable [] itemsToCreate) {
    this.requestHeader = requestHeader;
    this.subscriptionId = subscriptionId;
    this.timestampsToReturn = timestampsToReturn;
    this.itemsToCreate = itemsToCreate;
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

  public TimestampsToReturn getTimestampsToReturn() {
    return timestampsToReturn;
  }

  public MonitoredItemCreateRequest @Nullable [] getItemsToCreate() {
    return itemsToCreate;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    CreateMonitoredItemsRequest that = (CreateMonitoredItemsRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getSubscriptionId(), that.getSubscriptionId());
    eqb.append(getTimestampsToReturn(), that.getTimestampsToReturn());
    eqb.append(getItemsToCreate(), that.getItemsToCreate());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getSubscriptionId());
    hcb.append(getTimestampsToReturn());
    hcb.append(getItemsToCreate());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", CreateMonitoredItemsRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("subscriptionId=" + getSubscriptionId());
    joiner.add("timestampsToReturn=" + getTimestampsToReturn());
    joiner.add("itemsToCreate=" + java.util.Arrays.toString(getItemsToCreate()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 751),
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
              "TimestampsToReturn",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 625),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ItemsToCreate",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 743),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<CreateMonitoredItemsRequest> {
    @Override
    public Class<CreateMonitoredItemsRequest> getType() {
      return CreateMonitoredItemsRequest.class;
    }

    @Override
    public CreateMonitoredItemsRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final UInteger subscriptionId;
      final TimestampsToReturn timestampsToReturn;
      final MonitoredItemCreateRequest[] itemsToCreate;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      subscriptionId = decoder.decodeUInt32("SubscriptionId");
      timestampsToReturn = TimestampsToReturn.from(decoder.decodeEnum("TimestampsToReturn"));
      itemsToCreate =
          (MonitoredItemCreateRequest[])
              decoder.decodeStructArray("ItemsToCreate", MonitoredItemCreateRequest.TYPE_ID);
      return new CreateMonitoredItemsRequest(
          requestHeader, subscriptionId, timestampsToReturn, itemsToCreate);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, CreateMonitoredItemsRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeUInt32("SubscriptionId", value.getSubscriptionId());
      encoder.encodeEnum("TimestampsToReturn", value.getTimestampsToReturn());
      encoder.encodeStructArray(
          "ItemsToCreate", value.getItemsToCreate(), MonitoredItemCreateRequest.TYPE_ID);
    }
  }
}
