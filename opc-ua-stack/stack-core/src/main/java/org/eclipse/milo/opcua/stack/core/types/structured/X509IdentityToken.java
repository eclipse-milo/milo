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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.4">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.4</a>
 */
public class X509IdentityToken extends UserIdentityToken implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=325");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=327");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=326");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15143");

  private final ByteString certificateData;

  public X509IdentityToken(@Nullable String policyId, ByteString certificateData) {
    super(policyId);
    this.certificateData = certificateData;
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

  public ByteString getCertificateData() {
    return certificateData;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    X509IdentityToken that = (X509IdentityToken) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getCertificateData(), that.getCertificateData());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getCertificateData());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", X509IdentityToken.class.getSimpleName() + "[", "]");
    joiner.add("certificateData=" + getCertificateData());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 327),
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
              "CertificateData",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<X509IdentityToken> {
    @Override
    public Class<X509IdentityToken> getType() {
      return X509IdentityToken.class;
    }

    @Override
    public X509IdentityToken decodeType(EncodingContext context, UaDecoder decoder) {
      final String policyId;
      final ByteString certificateData;
      policyId = decoder.decodeString("PolicyId");
      certificateData = decoder.decodeByteString("CertificateData");
      return new X509IdentityToken(policyId, certificateData);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, X509IdentityToken value) {
      encoder.encodeString("PolicyId", value.getPolicyId());
      encoder.encodeByteString("CertificateData", value.getCertificateData());
    }
  }
}
