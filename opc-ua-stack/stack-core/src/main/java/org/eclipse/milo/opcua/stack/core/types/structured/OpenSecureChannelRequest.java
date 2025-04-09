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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

public class OpenSecureChannelRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=444");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=446");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=445");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15132");

  private final RequestHeader requestHeader;

  private final UInteger clientProtocolVersion;

  private final SecurityTokenRequestType requestType;

  private final MessageSecurityMode securityMode;

  private final ByteString clientNonce;

  private final UInteger requestedLifetime;

  public OpenSecureChannelRequest(
      RequestHeader requestHeader,
      UInteger clientProtocolVersion,
      SecurityTokenRequestType requestType,
      MessageSecurityMode securityMode,
      ByteString clientNonce,
      UInteger requestedLifetime) {
    this.requestHeader = requestHeader;
    this.clientProtocolVersion = clientProtocolVersion;
    this.requestType = requestType;
    this.securityMode = securityMode;
    this.clientNonce = clientNonce;
    this.requestedLifetime = requestedLifetime;
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

  public UInteger getClientProtocolVersion() {
    return clientProtocolVersion;
  }

  public SecurityTokenRequestType getRequestType() {
    return requestType;
  }

  public MessageSecurityMode getSecurityMode() {
    return securityMode;
  }

  public ByteString getClientNonce() {
    return clientNonce;
  }

  public UInteger getRequestedLifetime() {
    return requestedLifetime;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    OpenSecureChannelRequest that = (OpenSecureChannelRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getClientProtocolVersion(), that.getClientProtocolVersion());
    eqb.append(getRequestType(), that.getRequestType());
    eqb.append(getSecurityMode(), that.getSecurityMode());
    eqb.append(getClientNonce(), that.getClientNonce());
    eqb.append(getRequestedLifetime(), that.getRequestedLifetime());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getClientProtocolVersion());
    hcb.append(getRequestType());
    hcb.append(getSecurityMode());
    hcb.append(getClientNonce());
    hcb.append(getRequestedLifetime());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", OpenSecureChannelRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("clientProtocolVersion=" + getClientProtocolVersion());
    joiner.add("requestType=" + getRequestType());
    joiner.add("securityMode=" + getSecurityMode());
    joiner.add("clientNonce=" + getClientNonce());
    joiner.add("requestedLifetime=" + getRequestedLifetime());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 446),
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
              "ClientProtocolVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 315),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityMode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 302),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ClientNonce",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestedLifetime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<OpenSecureChannelRequest> {
    @Override
    public Class<OpenSecureChannelRequest> getType() {
      return OpenSecureChannelRequest.class;
    }

    @Override
    public OpenSecureChannelRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final UInteger clientProtocolVersion;
      final SecurityTokenRequestType requestType;
      final MessageSecurityMode securityMode;
      final ByteString clientNonce;
      final UInteger requestedLifetime;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      clientProtocolVersion = decoder.decodeUInt32("ClientProtocolVersion");
      requestType = SecurityTokenRequestType.from(decoder.decodeEnum("RequestType"));
      securityMode = MessageSecurityMode.from(decoder.decodeEnum("SecurityMode"));
      clientNonce = decoder.decodeByteString("ClientNonce");
      requestedLifetime = decoder.decodeUInt32("RequestedLifetime");
      return new OpenSecureChannelRequest(
          requestHeader,
          clientProtocolVersion,
          requestType,
          securityMode,
          clientNonce,
          requestedLifetime);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, OpenSecureChannelRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeUInt32("ClientProtocolVersion", value.getClientProtocolVersion());
      encoder.encodeEnum("RequestType", value.getRequestType());
      encoder.encodeEnum("SecurityMode", value.getSecurityMode());
      encoder.encodeByteString("ClientNonce", value.getClientNonce());
      encoder.encodeUInt32("RequestedLifetime", value.getRequestedLifetime());
    }
  }
}
