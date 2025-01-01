/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.7/#5.13.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.7/#5.13.7.2</a>
 */
public class TransferSubscriptionsRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=839");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=841");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=840");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15357");

  private final RequestHeader requestHeader;

  private final UInteger @Nullable [] subscriptionIds;

  private final Boolean sendInitialValues;

  public TransferSubscriptionsRequest(
      RequestHeader requestHeader,
      UInteger @Nullable [] subscriptionIds,
      Boolean sendInitialValues) {
    this.requestHeader = requestHeader;
    this.subscriptionIds = subscriptionIds;
    this.sendInitialValues = sendInitialValues;
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

  public UInteger @Nullable [] getSubscriptionIds() {
    return subscriptionIds;
  }

  public Boolean getSendInitialValues() {
    return sendInitialValues;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    TransferSubscriptionsRequest that = (TransferSubscriptionsRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getSubscriptionIds(), that.getSubscriptionIds());
    eqb.append(getSendInitialValues(), that.getSendInitialValues());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getSubscriptionIds());
    hcb.append(getSendInitialValues());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", TransferSubscriptionsRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("subscriptionIds=" + java.util.Arrays.toString(getSubscriptionIds()));
    joiner.add("sendInitialValues=" + getSendInitialValues());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 841),
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
              "SubscriptionIds",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SendInitialValues",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<TransferSubscriptionsRequest> {
    @Override
    public Class<TransferSubscriptionsRequest> getType() {
      return TransferSubscriptionsRequest.class;
    }

    @Override
    public TransferSubscriptionsRequest decodeType(EncodingContext context, UaDecoder decoder) {
      RequestHeader requestHeader =
          (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      UInteger[] subscriptionIds = decoder.decodeUInt32Array("SubscriptionIds");
      Boolean sendInitialValues = decoder.decodeBoolean("SendInitialValues");
      return new TransferSubscriptionsRequest(requestHeader, subscriptionIds, sendInitialValues);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, TransferSubscriptionsRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeUInt32Array("SubscriptionIds", value.getSubscriptionIds());
      encoder.encodeBoolean("SendInitialValues", value.getSendInitialValues());
    }
  }
}
