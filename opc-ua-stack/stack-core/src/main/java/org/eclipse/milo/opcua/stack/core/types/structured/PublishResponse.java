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
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2</a>
 */
public class PublishResponse extends Structure implements UaResponseMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=827");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=829");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=828");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15353");

  private final ResponseHeader responseHeader;

  private final UInteger subscriptionId;

  private final UInteger @Nullable [] availableSequenceNumbers;

  private final Boolean moreNotifications;

  private final NotificationMessage notificationMessage;

  private final StatusCode @Nullable [] results;

  private final DiagnosticInfo @Nullable [] diagnosticInfos;

  public PublishResponse(
      ResponseHeader responseHeader,
      UInteger subscriptionId,
      UInteger @Nullable [] availableSequenceNumbers,
      Boolean moreNotifications,
      NotificationMessage notificationMessage,
      StatusCode @Nullable [] results,
      DiagnosticInfo @Nullable [] diagnosticInfos) {
    this.responseHeader = responseHeader;
    this.subscriptionId = subscriptionId;
    this.availableSequenceNumbers = availableSequenceNumbers;
    this.moreNotifications = moreNotifications;
    this.notificationMessage = notificationMessage;
    this.results = results;
    this.diagnosticInfos = diagnosticInfos;
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

  public UInteger getSubscriptionId() {
    return subscriptionId;
  }

  public UInteger @Nullable [] getAvailableSequenceNumbers() {
    return availableSequenceNumbers;
  }

  public Boolean getMoreNotifications() {
    return moreNotifications;
  }

  public NotificationMessage getNotificationMessage() {
    return notificationMessage;
  }

  public StatusCode @Nullable [] getResults() {
    return results;
  }

  public DiagnosticInfo @Nullable [] getDiagnosticInfos() {
    return diagnosticInfos;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PublishResponse that = (PublishResponse) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResponseHeader(), that.getResponseHeader());
    eqb.append(getSubscriptionId(), that.getSubscriptionId());
    eqb.append(getAvailableSequenceNumbers(), that.getAvailableSequenceNumbers());
    eqb.append(getMoreNotifications(), that.getMoreNotifications());
    eqb.append(getNotificationMessage(), that.getNotificationMessage());
    eqb.append(getResults(), that.getResults());
    eqb.append(getDiagnosticInfos(), that.getDiagnosticInfos());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResponseHeader());
    hcb.append(getSubscriptionId());
    hcb.append(getAvailableSequenceNumbers());
    hcb.append(getMoreNotifications());
    hcb.append(getNotificationMessage());
    hcb.append(getResults());
    hcb.append(getDiagnosticInfos());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PublishResponse.class.getSimpleName() + "[", "]");
    joiner.add("responseHeader=" + getResponseHeader());
    joiner.add("subscriptionId=" + getSubscriptionId());
    joiner.add(
        "availableSequenceNumbers=" + java.util.Arrays.toString(getAvailableSequenceNumbers()));
    joiner.add("moreNotifications=" + getMoreNotifications());
    joiner.add("notificationMessage=" + getNotificationMessage());
    joiner.add("results=" + java.util.Arrays.toString(getResults()));
    joiner.add("diagnosticInfos=" + java.util.Arrays.toString(getDiagnosticInfos()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 829),
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
              "SubscriptionId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AvailableSequenceNumbers",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MoreNotifications",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NotificationMessage",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 803),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Results",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiagnosticInfos",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PublishResponse> {
    @Override
    public Class<PublishResponse> getType() {
      return PublishResponse.class;
    }

    @Override
    public PublishResponse decodeType(EncodingContext context, UaDecoder decoder) {
      final ResponseHeader responseHeader;
      final UInteger subscriptionId;
      final UInteger[] availableSequenceNumbers;
      final Boolean moreNotifications;
      final NotificationMessage notificationMessage;
      final StatusCode[] results;
      final DiagnosticInfo[] diagnosticInfos;
      responseHeader =
          (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
      subscriptionId = decoder.decodeUInt32("SubscriptionId");
      availableSequenceNumbers = decoder.decodeUInt32Array("AvailableSequenceNumbers");
      moreNotifications = decoder.decodeBoolean("MoreNotifications");
      notificationMessage =
          (NotificationMessage)
              decoder.decodeStruct("NotificationMessage", NotificationMessage.TYPE_ID);
      results = decoder.decodeStatusCodeArray("Results");
      diagnosticInfos = decoder.decodeDiagnosticInfoArray("DiagnosticInfos");
      return new PublishResponse(
          responseHeader,
          subscriptionId,
          availableSequenceNumbers,
          moreNotifications,
          notificationMessage,
          results,
          diagnosticInfos);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, PublishResponse value) {
      encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
      encoder.encodeUInt32("SubscriptionId", value.getSubscriptionId());
      encoder.encodeUInt32Array("AvailableSequenceNumbers", value.getAvailableSequenceNumbers());
      encoder.encodeBoolean("MoreNotifications", value.getMoreNotifications());
      encoder.encodeStruct(
          "NotificationMessage", value.getNotificationMessage(), NotificationMessage.TYPE_ID);
      encoder.encodeStatusCodeArray("Results", value.getResults());
      encoder.encodeDiagnosticInfoArray("DiagnosticInfos", value.getDiagnosticInfos());
    }
  }
}
