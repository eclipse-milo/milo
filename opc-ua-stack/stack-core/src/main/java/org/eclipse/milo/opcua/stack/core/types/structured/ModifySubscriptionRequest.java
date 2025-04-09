/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.3/#5.14.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.3/#5.14.3.2</a>
 */
public class ModifySubscriptionRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=791");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=793");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=792");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15339");

  private final RequestHeader requestHeader;

  private final UInteger subscriptionId;

  private final Double requestedPublishingInterval;

  private final UInteger requestedLifetimeCount;

  private final UInteger requestedMaxKeepAliveCount;

  private final UInteger maxNotificationsPerPublish;

  private final UByte priority;

  public ModifySubscriptionRequest(
      RequestHeader requestHeader,
      UInteger subscriptionId,
      Double requestedPublishingInterval,
      UInteger requestedLifetimeCount,
      UInteger requestedMaxKeepAliveCount,
      UInteger maxNotificationsPerPublish,
      UByte priority) {
    this.requestHeader = requestHeader;
    this.subscriptionId = subscriptionId;
    this.requestedPublishingInterval = requestedPublishingInterval;
    this.requestedLifetimeCount = requestedLifetimeCount;
    this.requestedMaxKeepAliveCount = requestedMaxKeepAliveCount;
    this.maxNotificationsPerPublish = maxNotificationsPerPublish;
    this.priority = priority;
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

  public Double getRequestedPublishingInterval() {
    return requestedPublishingInterval;
  }

  public UInteger getRequestedLifetimeCount() {
    return requestedLifetimeCount;
  }

  public UInteger getRequestedMaxKeepAliveCount() {
    return requestedMaxKeepAliveCount;
  }

  public UInteger getMaxNotificationsPerPublish() {
    return maxNotificationsPerPublish;
  }

  public UByte getPriority() {
    return priority;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ModifySubscriptionRequest that = (ModifySubscriptionRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getSubscriptionId(), that.getSubscriptionId());
    eqb.append(getRequestedPublishingInterval(), that.getRequestedPublishingInterval());
    eqb.append(getRequestedLifetimeCount(), that.getRequestedLifetimeCount());
    eqb.append(getRequestedMaxKeepAliveCount(), that.getRequestedMaxKeepAliveCount());
    eqb.append(getMaxNotificationsPerPublish(), that.getMaxNotificationsPerPublish());
    eqb.append(getPriority(), that.getPriority());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getSubscriptionId());
    hcb.append(getRequestedPublishingInterval());
    hcb.append(getRequestedLifetimeCount());
    hcb.append(getRequestedMaxKeepAliveCount());
    hcb.append(getMaxNotificationsPerPublish());
    hcb.append(getPriority());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ModifySubscriptionRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("subscriptionId=" + getSubscriptionId());
    joiner.add("requestedPublishingInterval=" + getRequestedPublishingInterval());
    joiner.add("requestedLifetimeCount=" + getRequestedLifetimeCount());
    joiner.add("requestedMaxKeepAliveCount=" + getRequestedMaxKeepAliveCount());
    joiner.add("maxNotificationsPerPublish=" + getMaxNotificationsPerPublish());
    joiner.add("priority=" + getPriority());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 793),
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
              "RequestedPublishingInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestedLifetimeCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestedMaxKeepAliveCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxNotificationsPerPublish",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Priority",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ModifySubscriptionRequest> {
    @Override
    public Class<ModifySubscriptionRequest> getType() {
      return ModifySubscriptionRequest.class;
    }

    @Override
    public ModifySubscriptionRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final UInteger subscriptionId;
      final Double requestedPublishingInterval;
      final UInteger requestedLifetimeCount;
      final UInteger requestedMaxKeepAliveCount;
      final UInteger maxNotificationsPerPublish;
      final UByte priority;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      subscriptionId = decoder.decodeUInt32("SubscriptionId");
      requestedPublishingInterval = decoder.decodeDouble("RequestedPublishingInterval");
      requestedLifetimeCount = decoder.decodeUInt32("RequestedLifetimeCount");
      requestedMaxKeepAliveCount = decoder.decodeUInt32("RequestedMaxKeepAliveCount");
      maxNotificationsPerPublish = decoder.decodeUInt32("MaxNotificationsPerPublish");
      priority = decoder.decodeByte("Priority");
      return new ModifySubscriptionRequest(
          requestHeader,
          subscriptionId,
          requestedPublishingInterval,
          requestedLifetimeCount,
          requestedMaxKeepAliveCount,
          maxNotificationsPerPublish,
          priority);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ModifySubscriptionRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeUInt32("SubscriptionId", value.getSubscriptionId());
      encoder.encodeDouble("RequestedPublishingInterval", value.getRequestedPublishingInterval());
      encoder.encodeUInt32("RequestedLifetimeCount", value.getRequestedLifetimeCount());
      encoder.encodeUInt32("RequestedMaxKeepAliveCount", value.getRequestedMaxKeepAliveCount());
      encoder.encodeUInt32("MaxNotificationsPerPublish", value.getMaxNotificationsPerPublish());
      encoder.encodeByte("Priority", value.getPriority());
    }
  }
}
