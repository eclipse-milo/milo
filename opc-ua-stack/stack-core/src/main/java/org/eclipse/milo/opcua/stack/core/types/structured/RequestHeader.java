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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.33">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.33</a>
 */
public class RequestHeader extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=389");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=391");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=390");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15088");

  private final NodeId authenticationToken;

  private final DateTime timestamp;

  private final UInteger requestHandle;

  private final UInteger returnDiagnostics;

  private final @Nullable String auditEntryId;

  private final UInteger timeoutHint;

  private final ExtensionObject additionalHeader;

  public RequestHeader(
      NodeId authenticationToken,
      DateTime timestamp,
      UInteger requestHandle,
      UInteger returnDiagnostics,
      @Nullable String auditEntryId,
      UInteger timeoutHint,
      ExtensionObject additionalHeader) {
    this.authenticationToken = authenticationToken;
    this.timestamp = timestamp;
    this.requestHandle = requestHandle;
    this.returnDiagnostics = returnDiagnostics;
    this.auditEntryId = auditEntryId;
    this.timeoutHint = timeoutHint;
    this.additionalHeader = additionalHeader;
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

  public NodeId getAuthenticationToken() {
    return authenticationToken;
  }

  public DateTime getTimestamp() {
    return timestamp;
  }

  public UInteger getRequestHandle() {
    return requestHandle;
  }

  public UInteger getReturnDiagnostics() {
    return returnDiagnostics;
  }

  public @Nullable String getAuditEntryId() {
    return auditEntryId;
  }

  public UInteger getTimeoutHint() {
    return timeoutHint;
  }

  public ExtensionObject getAdditionalHeader() {
    return additionalHeader;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RequestHeader that = (RequestHeader) object;
    var eqb = new EqualsBuilder();
    eqb.append(getAuthenticationToken(), that.getAuthenticationToken());
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getRequestHandle(), that.getRequestHandle());
    eqb.append(getReturnDiagnostics(), that.getReturnDiagnostics());
    eqb.append(getAuditEntryId(), that.getAuditEntryId());
    eqb.append(getTimeoutHint(), that.getTimeoutHint());
    eqb.append(getAdditionalHeader(), that.getAdditionalHeader());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAuthenticationToken());
    hcb.append(getTimestamp());
    hcb.append(getRequestHandle());
    hcb.append(getReturnDiagnostics());
    hcb.append(getAuditEntryId());
    hcb.append(getTimeoutHint());
    hcb.append(getAdditionalHeader());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", RequestHeader.class.getSimpleName() + "[", "]");
    joiner.add("authenticationToken=" + getAuthenticationToken());
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("requestHandle=" + getRequestHandle());
    joiner.add("returnDiagnostics=" + getReturnDiagnostics());
    joiner.add("auditEntryId='" + getAuditEntryId() + "'");
    joiner.add("timeoutHint=" + getTimeoutHint());
    joiner.add("additionalHeader=" + getAdditionalHeader());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 391),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "AuthenticationToken",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 388),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Timestamp",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestHandle",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReturnDiagnostics",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AuditEntryId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TimeoutHint",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AdditionalHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<RequestHeader> {
    @Override
    public Class<RequestHeader> getType() {
      return RequestHeader.class;
    }

    @Override
    public RequestHeader decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId authenticationToken;
      final DateTime timestamp;
      final UInteger requestHandle;
      final UInteger returnDiagnostics;
      final String auditEntryId;
      final UInteger timeoutHint;
      final ExtensionObject additionalHeader;
      authenticationToken = decoder.decodeNodeId("AuthenticationToken");
      timestamp = decoder.decodeDateTime("Timestamp");
      requestHandle = decoder.decodeUInt32("RequestHandle");
      returnDiagnostics = decoder.decodeUInt32("ReturnDiagnostics");
      auditEntryId = decoder.decodeString("AuditEntryId");
      timeoutHint = decoder.decodeUInt32("TimeoutHint");
      additionalHeader = decoder.decodeExtensionObject("AdditionalHeader");
      return new RequestHeader(
          authenticationToken,
          timestamp,
          requestHandle,
          returnDiagnostics,
          auditEntryId,
          timeoutHint,
          additionalHeader);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, RequestHeader value) {
      encoder.encodeNodeId("AuthenticationToken", value.getAuthenticationToken());
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeUInt32("RequestHandle", value.getRequestHandle());
      encoder.encodeUInt32("ReturnDiagnostics", value.getReturnDiagnostics());
      encoder.encodeString("AuditEntryId", value.getAuditEntryId());
      encoder.encodeUInt32("TimeoutHint", value.getTimeoutHint());
      encoder.encodeExtensionObject("AdditionalHeader", value.getAdditionalHeader());
    }
  }
}
