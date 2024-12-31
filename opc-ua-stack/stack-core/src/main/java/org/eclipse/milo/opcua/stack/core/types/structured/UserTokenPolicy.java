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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.42">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.42</a>
 */
public class UserTokenPolicy extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=304");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=306");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=305");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15098");

  private final @Nullable String policyId;

  private final UserTokenType tokenType;

  private final @Nullable String issuedTokenType;

  private final @Nullable String issuerEndpointUrl;

  private final @Nullable String securityPolicyUri;

  public UserTokenPolicy(
      @Nullable String policyId,
      UserTokenType tokenType,
      @Nullable String issuedTokenType,
      @Nullable String issuerEndpointUrl,
      @Nullable String securityPolicyUri) {
    this.policyId = policyId;
    this.tokenType = tokenType;
    this.issuedTokenType = issuedTokenType;
    this.issuerEndpointUrl = issuerEndpointUrl;
    this.securityPolicyUri = securityPolicyUri;
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

  public @Nullable String getPolicyId() {
    return policyId;
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

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UserTokenPolicy that = (UserTokenPolicy) object;
    var eqb = new EqualsBuilder();
    eqb.append(getPolicyId(), that.getPolicyId());
    eqb.append(getTokenType(), that.getTokenType());
    eqb.append(getIssuedTokenType(), that.getIssuedTokenType());
    eqb.append(getIssuerEndpointUrl(), that.getIssuerEndpointUrl());
    eqb.append(getSecurityPolicyUri(), that.getSecurityPolicyUri());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getPolicyId());
    hcb.append(getTokenType());
    hcb.append(getIssuedTokenType());
    hcb.append(getIssuerEndpointUrl());
    hcb.append(getSecurityPolicyUri());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", UserTokenPolicy.class.getSimpleName() + "[", "]");
    joiner.add("policyId='" + getPolicyId() + "'");
    joiner.add("tokenType=" + getTokenType());
    joiner.add("issuedTokenType='" + getIssuedTokenType() + "'");
    joiner.add("issuerEndpointUrl='" + getIssuerEndpointUrl() + "'");
    joiner.add("securityPolicyUri='" + getSecurityPolicyUri() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 306),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "PolicyId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
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
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<UserTokenPolicy> {
    @Override
    public Class<UserTokenPolicy> getType() {
      return UserTokenPolicy.class;
    }

    @Override
    public UserTokenPolicy decodeType(EncodingContext context, UaDecoder decoder) {
      String policyId = decoder.decodeString("PolicyId");
      UserTokenType tokenType = UserTokenType.from(decoder.decodeEnum("TokenType"));
      String issuedTokenType = decoder.decodeString("IssuedTokenType");
      String issuerEndpointUrl = decoder.decodeString("IssuerEndpointUrl");
      String securityPolicyUri = decoder.decodeString("SecurityPolicyUri");
      return new UserTokenPolicy(
          policyId, tokenType, issuedTokenType, issuerEndpointUrl, securityPolicyUri);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, UserTokenPolicy value) {
      encoder.encodeString("PolicyId", value.getPolicyId());
      encoder.encodeEnum("TokenType", value.getTokenType());
      encoder.encodeString("IssuedTokenType", value.getIssuedTokenType());
      encoder.encodeString("IssuerEndpointUrl", value.getIssuerEndpointUrl());
      encoder.encodeString("SecurityPolicyUri", value.getSecurityPolicyUri());
    }
  }
}
