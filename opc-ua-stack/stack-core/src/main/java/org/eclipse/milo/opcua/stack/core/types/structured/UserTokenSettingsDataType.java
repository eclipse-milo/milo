/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.25">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.25</a>
 */
public class UserTokenSettingsDataType extends BaseConfigurationRecordDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15560");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=16547");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16596");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16645");

  private final UserTokenType tokenType;

  private final @Nullable String issuedTokenType;

  private final @Nullable String issuerEndpointUrl;

  private final @Nullable String securityPolicyUri;

  private final @Nullable String certificateGroupName;

  private final @Nullable String authorizationServiceName;

  public UserTokenSettingsDataType(
      @Nullable String name,
      KeyValuePair @Nullable [] recordProperties,
      UserTokenType tokenType,
      @Nullable String issuedTokenType,
      @Nullable String issuerEndpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable String certificateGroupName,
      @Nullable String authorizationServiceName) {
    super(name, recordProperties);
    this.tokenType = tokenType;
    this.issuedTokenType = issuedTokenType;
    this.issuerEndpointUrl = issuerEndpointUrl;
    this.securityPolicyUri = securityPolicyUri;
    this.certificateGroupName = certificateGroupName;
    this.authorizationServiceName = authorizationServiceName;
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

  public UserTokenType getTokenType() {
    return tokenType;
  }

  public @Nullable String getIssuedTokenType() {
    return issuedTokenType;
  }

  public @Nullable String getIssuerEndpointUrl() {
    return issuerEndpointUrl;
  }

  public @Nullable String getSecurityPolicyUri() {
    return securityPolicyUri;
  }

  public @Nullable String getCertificateGroupName() {
    return certificateGroupName;
  }

  public @Nullable String getAuthorizationServiceName() {
    return authorizationServiceName;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UserTokenSettingsDataType that = (UserTokenSettingsDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getTokenType(), that.getTokenType());
    eqb.append(getIssuedTokenType(), that.getIssuedTokenType());
    eqb.append(getIssuerEndpointUrl(), that.getIssuerEndpointUrl());
    eqb.append(getSecurityPolicyUri(), that.getSecurityPolicyUri());
    eqb.append(getCertificateGroupName(), that.getCertificateGroupName());
    eqb.append(getAuthorizationServiceName(), that.getAuthorizationServiceName());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTokenType());
    hcb.append(getIssuedTokenType());
    hcb.append(getIssuerEndpointUrl());
    hcb.append(getSecurityPolicyUri());
    hcb.append(getCertificateGroupName());
    hcb.append(getAuthorizationServiceName());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", UserTokenSettingsDataType.class.getSimpleName() + "[", "]");
    joiner.add("tokenType=" + getTokenType());
    joiner.add("issuedTokenType='" + getIssuedTokenType() + "'");
    joiner.add("issuerEndpointUrl='" + getIssuerEndpointUrl() + "'");
    joiner.add("securityPolicyUri='" + getSecurityPolicyUri() + "'");
    joiner.add("certificateGroupName='" + getCertificateGroupName() + "'");
    joiner.add("authorizationServiceName='" + getAuthorizationServiceName() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 16547),
        new NodeId(0, 15435),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RecordProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TokenType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 303),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IssuedTokenType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IssuerEndpointUrl",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityPolicyUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CertificateGroupName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AuthorizationServiceName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<UserTokenSettingsDataType> {
    @Override
    public Class<UserTokenSettingsDataType> getType() {
      return UserTokenSettingsDataType.class;
    }

    @Override
    public UserTokenSettingsDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String name;
      final KeyValuePair[] recordProperties;
      final UserTokenType tokenType;
      final String issuedTokenType;
      final String issuerEndpointUrl;
      final String securityPolicyUri;
      final String certificateGroupName;
      final String authorizationServiceName;
      name = decoder.decodeString("Name");
      recordProperties =
          (KeyValuePair[]) decoder.decodeStructArray("RecordProperties", KeyValuePair.TYPE_ID);
      tokenType = UserTokenType.from(decoder.decodeEnum("TokenType"));
      issuedTokenType = decoder.decodeString("IssuedTokenType");
      issuerEndpointUrl = decoder.decodeString("IssuerEndpointUrl");
      securityPolicyUri = decoder.decodeString("SecurityPolicyUri");
      certificateGroupName = decoder.decodeString("CertificateGroupName");
      authorizationServiceName = decoder.decodeString("AuthorizationServiceName");
      return new UserTokenSettingsDataType(
          name,
          recordProperties,
          tokenType,
          issuedTokenType,
          issuerEndpointUrl,
          securityPolicyUri,
          certificateGroupName,
          authorizationServiceName);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, UserTokenSettingsDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeStructArray(
          "RecordProperties", value.getRecordProperties(), KeyValuePair.TYPE_ID);
      encoder.encodeEnum("TokenType", value.getTokenType());
      encoder.encodeString("IssuedTokenType", value.getIssuedTokenType());
      encoder.encodeString("IssuerEndpointUrl", value.getIssuerEndpointUrl());
      encoder.encodeString("SecurityPolicyUri", value.getSecurityPolicyUri());
      encoder.encodeString("CertificateGroupName", value.getCertificateGroupName());
      encoder.encodeString("AuthorizationServiceName", value.getAuthorizationServiceName());
    }
  }
}
