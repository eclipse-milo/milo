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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.2">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.2</a>
 */
public class IssuedIdentityToken extends UserIdentityToken implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=938");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=940");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=939");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15144");

  private final ByteString tokenData;

  private final @Nullable String encryptionAlgorithm;

  public IssuedIdentityToken(
      @Nullable String policyId, ByteString tokenData, @Nullable String encryptionAlgorithm) {
    super(policyId);
    this.tokenData = tokenData;
    this.encryptionAlgorithm = encryptionAlgorithm;
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

  public ByteString getTokenData() {
    return tokenData;
  }

  public @Nullable String getEncryptionAlgorithm() {
    return encryptionAlgorithm;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    IssuedIdentityToken that = (IssuedIdentityToken) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getTokenData(), that.getTokenData());
    eqb.append(getEncryptionAlgorithm(), that.getEncryptionAlgorithm());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTokenData());
    hcb.append(getEncryptionAlgorithm());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", IssuedIdentityToken.class.getSimpleName() + "[", "]");
    joiner.add("tokenData=" + getTokenData());
    joiner.add("encryptionAlgorithm='" + getEncryptionAlgorithm() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 940),
        new NodeId(0, 316),
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
              "TokenData",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EncryptionAlgorithm",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<IssuedIdentityToken> {
    @Override
    public Class<IssuedIdentityToken> getType() {
      return IssuedIdentityToken.class;
    }

    @Override
    public IssuedIdentityToken decodeType(EncodingContext context, UaDecoder decoder) {
      final String policyId;
      final ByteString tokenData;
      final String encryptionAlgorithm;
      policyId = decoder.decodeString("PolicyId");
      tokenData = decoder.decodeByteString("TokenData");
      encryptionAlgorithm = decoder.decodeString("EncryptionAlgorithm");
      return new IssuedIdentityToken(policyId, tokenData, encryptionAlgorithm);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, IssuedIdentityToken value) {
      encoder.encodeString("PolicyId", value.getPolicyId());
      encoder.encodeByteString("TokenData", value.getTokenData());
      encoder.encodeString("EncryptionAlgorithm", value.getEncryptionAlgorithm());
    }
  }
}
