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
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.2/#5.5.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.2/#5.5.2.2</a>
 */
public class OpenSecureChannelResponse extends Structure implements UaResponseMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=447");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=449");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=448");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15133");

  private final ResponseHeader responseHeader;

  private final UInteger serverProtocolVersion;

  private final ChannelSecurityToken securityToken;

  private final ByteString serverNonce;

  public OpenSecureChannelResponse(
      ResponseHeader responseHeader,
      UInteger serverProtocolVersion,
      ChannelSecurityToken securityToken,
      ByteString serverNonce) {
    this.responseHeader = responseHeader;
    this.serverProtocolVersion = serverProtocolVersion;
    this.securityToken = securityToken;
    this.serverNonce = serverNonce;
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

  public UInteger getServerProtocolVersion() {
    return serverProtocolVersion;
  }

  public ChannelSecurityToken getSecurityToken() {
    return securityToken;
  }

  public ByteString getServerNonce() {
    return serverNonce;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    OpenSecureChannelResponse that = (OpenSecureChannelResponse) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResponseHeader(), that.getResponseHeader());
    eqb.append(getServerProtocolVersion(), that.getServerProtocolVersion());
    eqb.append(getSecurityToken(), that.getSecurityToken());
    eqb.append(getServerNonce(), that.getServerNonce());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResponseHeader());
    hcb.append(getServerProtocolVersion());
    hcb.append(getSecurityToken());
    hcb.append(getServerNonce());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", OpenSecureChannelResponse.class.getSimpleName() + "[", "]");
    joiner.add("responseHeader=" + getResponseHeader());
    joiner.add("serverProtocolVersion=" + getServerProtocolVersion());
    joiner.add("securityToken=" + getSecurityToken());
    joiner.add("serverNonce=" + getServerNonce());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 449),
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
              "ServerProtocolVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityToken",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 441),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerNonce",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<OpenSecureChannelResponse> {
    @Override
    public Class<OpenSecureChannelResponse> getType() {
      return OpenSecureChannelResponse.class;
    }

    @Override
    public OpenSecureChannelResponse decodeType(EncodingContext context, UaDecoder decoder) {
      ResponseHeader responseHeader =
          (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
      UInteger serverProtocolVersion = decoder.decodeUInt32("ServerProtocolVersion");
      ChannelSecurityToken securityToken =
          (ChannelSecurityToken)
              decoder.decodeStruct("SecurityToken", ChannelSecurityToken.TYPE_ID);
      ByteString serverNonce = decoder.decodeByteString("ServerNonce");
      return new OpenSecureChannelResponse(
          responseHeader, serverProtocolVersion, securityToken, serverNonce);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, OpenSecureChannelResponse value) {
      encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
      encoder.encodeUInt32("ServerProtocolVersion", value.getServerProtocolVersion());
      encoder.encodeStruct("SecurityToken", value.getSecurityToken(), ChannelSecurityToken.TYPE_ID);
      encoder.encodeByteString("ServerNonce", value.getServerNonce());
    }
  }
}
