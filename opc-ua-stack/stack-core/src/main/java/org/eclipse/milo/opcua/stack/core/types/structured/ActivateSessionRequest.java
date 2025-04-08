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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.3/#5.7.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.3/#5.7.3.2</a>
 */
public class ActivateSessionRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=465");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=467");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=466");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15145");

  private final RequestHeader requestHeader;

  private final SignatureData clientSignature;

  private final SignedSoftwareCertificate @Nullable [] clientSoftwareCertificates;

  private final String @Nullable [] localeIds;

  private final ExtensionObject userIdentityToken;

  private final SignatureData userTokenSignature;

  public ActivateSessionRequest(
      RequestHeader requestHeader,
      SignatureData clientSignature,
      SignedSoftwareCertificate @Nullable [] clientSoftwareCertificates,
      String @Nullable [] localeIds,
      ExtensionObject userIdentityToken,
      SignatureData userTokenSignature) {
    this.requestHeader = requestHeader;
    this.clientSignature = clientSignature;
    this.clientSoftwareCertificates = clientSoftwareCertificates;
    this.localeIds = localeIds;
    this.userIdentityToken = userIdentityToken;
    this.userTokenSignature = userTokenSignature;
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

  public SignatureData getClientSignature() {
    return clientSignature;
  }

  public SignedSoftwareCertificate @Nullable [] getClientSoftwareCertificates() {
    return clientSoftwareCertificates;
  }

  public String @Nullable [] getLocaleIds() {
    return localeIds;
  }

  public ExtensionObject getUserIdentityToken() {
    return userIdentityToken;
  }

  public SignatureData getUserTokenSignature() {
    return userTokenSignature;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ActivateSessionRequest that = (ActivateSessionRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getClientSignature(), that.getClientSignature());
    eqb.append(getClientSoftwareCertificates(), that.getClientSoftwareCertificates());
    eqb.append(getLocaleIds(), that.getLocaleIds());
    eqb.append(getUserIdentityToken(), that.getUserIdentityToken());
    eqb.append(getUserTokenSignature(), that.getUserTokenSignature());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getClientSignature());
    hcb.append(getClientSoftwareCertificates());
    hcb.append(getLocaleIds());
    hcb.append(getUserIdentityToken());
    hcb.append(getUserTokenSignature());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ActivateSessionRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("clientSignature=" + getClientSignature());
    joiner.add(
        "clientSoftwareCertificates=" + java.util.Arrays.toString(getClientSoftwareCertificates()));
    joiner.add("localeIds=" + java.util.Arrays.toString(getLocaleIds()));
    joiner.add("userIdentityToken=" + getUserIdentityToken());
    joiner.add("userTokenSignature=" + getUserTokenSignature());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 467),
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
              "ClientSignature",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 456),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ClientSoftwareCertificates",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 344),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LocaleIds",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 295),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserIdentityToken",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserTokenSignature",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 456),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ActivateSessionRequest> {
    @Override
    public Class<ActivateSessionRequest> getType() {
      return ActivateSessionRequest.class;
    }

    @Override
    public ActivateSessionRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final SignatureData clientSignature;
      final SignedSoftwareCertificate[] clientSoftwareCertificates;
      final String[] localeIds;
      final ExtensionObject userIdentityToken;
      final SignatureData userTokenSignature;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      clientSignature =
          (SignatureData) decoder.decodeStruct("ClientSignature", SignatureData.TYPE_ID);
      clientSoftwareCertificates =
          (SignedSoftwareCertificate[])
              decoder.decodeStructArray(
                  "ClientSoftwareCertificates", SignedSoftwareCertificate.TYPE_ID);
      localeIds = decoder.decodeStringArray("LocaleIds");
      userIdentityToken = decoder.decodeExtensionObject("UserIdentityToken");
      userTokenSignature =
          (SignatureData) decoder.decodeStruct("UserTokenSignature", SignatureData.TYPE_ID);
      return new ActivateSessionRequest(
          requestHeader,
          clientSignature,
          clientSoftwareCertificates,
          localeIds,
          userIdentityToken,
          userTokenSignature);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ActivateSessionRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeStruct("ClientSignature", value.getClientSignature(), SignatureData.TYPE_ID);
      encoder.encodeStructArray(
          "ClientSoftwareCertificates",
          value.getClientSoftwareCertificates(),
          SignedSoftwareCertificate.TYPE_ID);
      encoder.encodeStringArray("LocaleIds", value.getLocaleIds());
      encoder.encodeExtensionObject("UserIdentityToken", value.getUserIdentityToken());
      encoder.encodeStruct(
          "UserTokenSignature", value.getUserTokenSignature(), SignatureData.TYPE_ID);
    }
  }
}
