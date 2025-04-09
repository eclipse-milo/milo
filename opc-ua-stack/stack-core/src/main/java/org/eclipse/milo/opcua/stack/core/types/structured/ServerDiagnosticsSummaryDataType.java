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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.9">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.9</a>
 */
public class ServerDiagnosticsSummaryDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=859");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=861");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=860");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15366");

  private final UInteger serverViewCount;

  private final UInteger currentSessionCount;

  private final UInteger cumulatedSessionCount;

  private final UInteger securityRejectedSessionCount;

  private final UInteger rejectedSessionCount;

  private final UInteger sessionTimeoutCount;

  private final UInteger sessionAbortCount;

  private final UInteger currentSubscriptionCount;

  private final UInteger cumulatedSubscriptionCount;

  private final UInteger publishingIntervalCount;

  private final UInteger securityRejectedRequestsCount;

  private final UInteger rejectedRequestsCount;

  public ServerDiagnosticsSummaryDataType(
      UInteger serverViewCount,
      UInteger currentSessionCount,
      UInteger cumulatedSessionCount,
      UInteger securityRejectedSessionCount,
      UInteger rejectedSessionCount,
      UInteger sessionTimeoutCount,
      UInteger sessionAbortCount,
      UInteger currentSubscriptionCount,
      UInteger cumulatedSubscriptionCount,
      UInteger publishingIntervalCount,
      UInteger securityRejectedRequestsCount,
      UInteger rejectedRequestsCount) {
    this.serverViewCount = serverViewCount;
    this.currentSessionCount = currentSessionCount;
    this.cumulatedSessionCount = cumulatedSessionCount;
    this.securityRejectedSessionCount = securityRejectedSessionCount;
    this.rejectedSessionCount = rejectedSessionCount;
    this.sessionTimeoutCount = sessionTimeoutCount;
    this.sessionAbortCount = sessionAbortCount;
    this.currentSubscriptionCount = currentSubscriptionCount;
    this.cumulatedSubscriptionCount = cumulatedSubscriptionCount;
    this.publishingIntervalCount = publishingIntervalCount;
    this.securityRejectedRequestsCount = securityRejectedRequestsCount;
    this.rejectedRequestsCount = rejectedRequestsCount;
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

  public UInteger getServerViewCount() {
    return serverViewCount;
  }

  public UInteger getCurrentSessionCount() {
    return currentSessionCount;
  }

  public UInteger getCumulatedSessionCount() {
    return cumulatedSessionCount;
  }

  public UInteger getSecurityRejectedSessionCount() {
    return securityRejectedSessionCount;
  }

  public UInteger getRejectedSessionCount() {
    return rejectedSessionCount;
  }

  public UInteger getSessionTimeoutCount() {
    return sessionTimeoutCount;
  }

  public UInteger getSessionAbortCount() {
    return sessionAbortCount;
  }

  public UInteger getCurrentSubscriptionCount() {
    return currentSubscriptionCount;
  }

  public UInteger getCumulatedSubscriptionCount() {
    return cumulatedSubscriptionCount;
  }

  public UInteger getPublishingIntervalCount() {
    return publishingIntervalCount;
  }

  public UInteger getSecurityRejectedRequestsCount() {
    return securityRejectedRequestsCount;
  }

  public UInteger getRejectedRequestsCount() {
    return rejectedRequestsCount;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ServerDiagnosticsSummaryDataType that = (ServerDiagnosticsSummaryDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getServerViewCount(), that.getServerViewCount());
    eqb.append(getCurrentSessionCount(), that.getCurrentSessionCount());
    eqb.append(getCumulatedSessionCount(), that.getCumulatedSessionCount());
    eqb.append(getSecurityRejectedSessionCount(), that.getSecurityRejectedSessionCount());
    eqb.append(getRejectedSessionCount(), that.getRejectedSessionCount());
    eqb.append(getSessionTimeoutCount(), that.getSessionTimeoutCount());
    eqb.append(getSessionAbortCount(), that.getSessionAbortCount());
    eqb.append(getCurrentSubscriptionCount(), that.getCurrentSubscriptionCount());
    eqb.append(getCumulatedSubscriptionCount(), that.getCumulatedSubscriptionCount());
    eqb.append(getPublishingIntervalCount(), that.getPublishingIntervalCount());
    eqb.append(getSecurityRejectedRequestsCount(), that.getSecurityRejectedRequestsCount());
    eqb.append(getRejectedRequestsCount(), that.getRejectedRequestsCount());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getServerViewCount());
    hcb.append(getCurrentSessionCount());
    hcb.append(getCumulatedSessionCount());
    hcb.append(getSecurityRejectedSessionCount());
    hcb.append(getRejectedSessionCount());
    hcb.append(getSessionTimeoutCount());
    hcb.append(getSessionAbortCount());
    hcb.append(getCurrentSubscriptionCount());
    hcb.append(getCumulatedSubscriptionCount());
    hcb.append(getPublishingIntervalCount());
    hcb.append(getSecurityRejectedRequestsCount());
    hcb.append(getRejectedRequestsCount());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", ServerDiagnosticsSummaryDataType.class.getSimpleName() + "[", "]");
    joiner.add("serverViewCount=" + getServerViewCount());
    joiner.add("currentSessionCount=" + getCurrentSessionCount());
    joiner.add("cumulatedSessionCount=" + getCumulatedSessionCount());
    joiner.add("securityRejectedSessionCount=" + getSecurityRejectedSessionCount());
    joiner.add("rejectedSessionCount=" + getRejectedSessionCount());
    joiner.add("sessionTimeoutCount=" + getSessionTimeoutCount());
    joiner.add("sessionAbortCount=" + getSessionAbortCount());
    joiner.add("currentSubscriptionCount=" + getCurrentSubscriptionCount());
    joiner.add("cumulatedSubscriptionCount=" + getCumulatedSubscriptionCount());
    joiner.add("publishingIntervalCount=" + getPublishingIntervalCount());
    joiner.add("securityRejectedRequestsCount=" + getSecurityRejectedRequestsCount());
    joiner.add("rejectedRequestsCount=" + getRejectedRequestsCount());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 861),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ServerViewCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CurrentSessionCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CumulatedSessionCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityRejectedSessionCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RejectedSessionCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SessionTimeoutCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SessionAbortCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CurrentSubscriptionCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CumulatedSubscriptionCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PublishingIntervalCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityRejectedRequestsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RejectedRequestsCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ServerDiagnosticsSummaryDataType> {
    @Override
    public Class<ServerDiagnosticsSummaryDataType> getType() {
      return ServerDiagnosticsSummaryDataType.class;
    }

    @Override
    public ServerDiagnosticsSummaryDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger serverViewCount;
      final UInteger currentSessionCount;
      final UInteger cumulatedSessionCount;
      final UInteger securityRejectedSessionCount;
      final UInteger rejectedSessionCount;
      final UInteger sessionTimeoutCount;
      final UInteger sessionAbortCount;
      final UInteger currentSubscriptionCount;
      final UInteger cumulatedSubscriptionCount;
      final UInteger publishingIntervalCount;
      final UInteger securityRejectedRequestsCount;
      final UInteger rejectedRequestsCount;
      serverViewCount = decoder.decodeUInt32("ServerViewCount");
      currentSessionCount = decoder.decodeUInt32("CurrentSessionCount");
      cumulatedSessionCount = decoder.decodeUInt32("CumulatedSessionCount");
      securityRejectedSessionCount = decoder.decodeUInt32("SecurityRejectedSessionCount");
      rejectedSessionCount = decoder.decodeUInt32("RejectedSessionCount");
      sessionTimeoutCount = decoder.decodeUInt32("SessionTimeoutCount");
      sessionAbortCount = decoder.decodeUInt32("SessionAbortCount");
      currentSubscriptionCount = decoder.decodeUInt32("CurrentSubscriptionCount");
      cumulatedSubscriptionCount = decoder.decodeUInt32("CumulatedSubscriptionCount");
      publishingIntervalCount = decoder.decodeUInt32("PublishingIntervalCount");
      securityRejectedRequestsCount = decoder.decodeUInt32("SecurityRejectedRequestsCount");
      rejectedRequestsCount = decoder.decodeUInt32("RejectedRequestsCount");
      return new ServerDiagnosticsSummaryDataType(
          serverViewCount,
          currentSessionCount,
          cumulatedSessionCount,
          securityRejectedSessionCount,
          rejectedSessionCount,
          sessionTimeoutCount,
          sessionAbortCount,
          currentSubscriptionCount,
          cumulatedSubscriptionCount,
          publishingIntervalCount,
          securityRejectedRequestsCount,
          rejectedRequestsCount);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ServerDiagnosticsSummaryDataType value) {
      encoder.encodeUInt32("ServerViewCount", value.getServerViewCount());
      encoder.encodeUInt32("CurrentSessionCount", value.getCurrentSessionCount());
      encoder.encodeUInt32("CumulatedSessionCount", value.getCumulatedSessionCount());
      encoder.encodeUInt32("SecurityRejectedSessionCount", value.getSecurityRejectedSessionCount());
      encoder.encodeUInt32("RejectedSessionCount", value.getRejectedSessionCount());
      encoder.encodeUInt32("SessionTimeoutCount", value.getSessionTimeoutCount());
      encoder.encodeUInt32("SessionAbortCount", value.getSessionAbortCount());
      encoder.encodeUInt32("CurrentSubscriptionCount", value.getCurrentSubscriptionCount());
      encoder.encodeUInt32("CumulatedSubscriptionCount", value.getCumulatedSubscriptionCount());
      encoder.encodeUInt32("PublishingIntervalCount", value.getPublishingIntervalCount());
      encoder.encodeUInt32(
          "SecurityRejectedRequestsCount", value.getSecurityRejectedRequestsCount());
      encoder.encodeUInt32("RejectedRequestsCount", value.getRejectedRequestsCount());
    }
  }
}
