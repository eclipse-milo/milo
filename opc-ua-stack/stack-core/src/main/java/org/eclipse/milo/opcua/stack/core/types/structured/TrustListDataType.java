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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.6</a>
 */
public class TrustListDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=12554");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=12680");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=12676");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15044");

  private final UInteger specifiedLists;

  private final ByteString @Nullable [] trustedCertificates;

  private final ByteString @Nullable [] trustedCrls;

  private final ByteString @Nullable [] issuerCertificates;

  private final ByteString @Nullable [] issuerCrls;

  public TrustListDataType(
      UInteger specifiedLists,
      ByteString @Nullable [] trustedCertificates,
      ByteString @Nullable [] trustedCrls,
      ByteString @Nullable [] issuerCertificates,
      ByteString @Nullable [] issuerCrls) {
    this.specifiedLists = specifiedLists;
    this.trustedCertificates = trustedCertificates;
    this.trustedCrls = trustedCrls;
    this.issuerCertificates = issuerCertificates;
    this.issuerCrls = issuerCrls;
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

  public UInteger getSpecifiedLists() {
    return specifiedLists;
  }

  public ByteString @Nullable [] getTrustedCertificates() {
    return trustedCertificates;
  }

  public ByteString @Nullable [] getTrustedCrls() {
    return trustedCrls;
  }

  public ByteString @Nullable [] getIssuerCertificates() {
    return issuerCertificates;
  }

  public ByteString @Nullable [] getIssuerCrls() {
    return issuerCrls;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    TrustListDataType that = (TrustListDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getSpecifiedLists(), that.getSpecifiedLists());
    eqb.append(getTrustedCertificates(), that.getTrustedCertificates());
    eqb.append(getTrustedCrls(), that.getTrustedCrls());
    eqb.append(getIssuerCertificates(), that.getIssuerCertificates());
    eqb.append(getIssuerCrls(), that.getIssuerCrls());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSpecifiedLists());
    hcb.append(getTrustedCertificates());
    hcb.append(getTrustedCrls());
    hcb.append(getIssuerCertificates());
    hcb.append(getIssuerCrls());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", TrustListDataType.class.getSimpleName() + "[", "]");
    joiner.add("specifiedLists=" + getSpecifiedLists());
    joiner.add("trustedCertificates=" + java.util.Arrays.toString(getTrustedCertificates()));
    joiner.add("trustedCrls=" + java.util.Arrays.toString(getTrustedCrls()));
    joiner.add("issuerCertificates=" + java.util.Arrays.toString(getIssuerCertificates()));
    joiner.add("issuerCrls=" + java.util.Arrays.toString(getIssuerCrls()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 12680),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SpecifiedLists",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TrustedCertificates",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TrustedCrls",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IssuerCertificates",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IssuerCrls",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<TrustListDataType> {
    @Override
    public Class<TrustListDataType> getType() {
      return TrustListDataType.class;
    }

    @Override
    public TrustListDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger specifiedLists;
      final ByteString[] trustedCertificates;
      final ByteString[] trustedCrls;
      final ByteString[] issuerCertificates;
      final ByteString[] issuerCrls;
      specifiedLists = decoder.decodeUInt32("SpecifiedLists");
      trustedCertificates = decoder.decodeByteStringArray("TrustedCertificates");
      trustedCrls = decoder.decodeByteStringArray("TrustedCrls");
      issuerCertificates = decoder.decodeByteStringArray("IssuerCertificates");
      issuerCrls = decoder.decodeByteStringArray("IssuerCrls");
      return new TrustListDataType(
          specifiedLists, trustedCertificates, trustedCrls, issuerCertificates, issuerCrls);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, TrustListDataType value) {
      encoder.encodeUInt32("SpecifiedLists", value.getSpecifiedLists());
      encoder.encodeByteStringArray("TrustedCertificates", value.getTrustedCertificates());
      encoder.encodeByteStringArray("TrustedCrls", value.getTrustedCrls());
      encoder.encodeByteStringArray("IssuerCertificates", value.getIssuerCertificates());
      encoder.encodeByteStringArray("IssuerCrls", value.getIssuerCrls());
    }
  }
}
