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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.5">https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.5</a>
 */
public class ServiceCertificateDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=23724");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23725");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23735");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=23739");

  private final ByteString certificate;

  private final ByteString @Nullable [] issuers;

  private final DateTime validFrom;

  private final DateTime validTo;

  public ServiceCertificateDataType(
      ByteString certificate,
      ByteString @Nullable [] issuers,
      DateTime validFrom,
      DateTime validTo) {
    this.certificate = certificate;
    this.issuers = issuers;
    this.validFrom = validFrom;
    this.validTo = validTo;
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

  public ByteString getCertificate() {
    return certificate;
  }

  public ByteString @Nullable [] getIssuers() {
    return issuers;
  }

  public DateTime getValidFrom() {
    return validFrom;
  }

  public DateTime getValidTo() {
    return validTo;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ServiceCertificateDataType that = (ServiceCertificateDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getCertificate(), that.getCertificate());
    eqb.append(getIssuers(), that.getIssuers());
    eqb.append(getValidFrom(), that.getValidFrom());
    eqb.append(getValidTo(), that.getValidTo());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getCertificate());
    hcb.append(getIssuers());
    hcb.append(getValidFrom());
    hcb.append(getValidTo());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", ServiceCertificateDataType.class.getSimpleName() + "[", "]");
    joiner.add("certificate=" + getCertificate());
    joiner.add("issuers=" + java.util.Arrays.toString(getIssuers()));
    joiner.add("validFrom=" + getValidFrom());
    joiner.add("validTo=" + getValidTo());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 23725),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Certificate",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Issuers",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ValidFrom",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ValidTo",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ServiceCertificateDataType> {
    @Override
    public Class<ServiceCertificateDataType> getType() {
      return ServiceCertificateDataType.class;
    }

    @Override
    public ServiceCertificateDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final ByteString certificate;
      final ByteString[] issuers;
      final DateTime validFrom;
      final DateTime validTo;
      certificate = decoder.decodeByteString("Certificate");
      issuers = decoder.decodeByteStringArray("Issuers");
      validFrom = decoder.decodeDateTime("ValidFrom");
      validTo = decoder.decodeDateTime("ValidTo");
      return new ServiceCertificateDataType(certificate, issuers, validFrom, validTo);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ServiceCertificateDataType value) {
      encoder.encodeByteString("Certificate", value.getCertificate());
      encoder.encodeByteStringArray("Issuers", value.getIssuers());
      encoder.encodeDateTime("ValidFrom", value.getValidFrom());
      encoder.encodeDateTime("ValidTo", value.getValidTo());
    }
  }
}
