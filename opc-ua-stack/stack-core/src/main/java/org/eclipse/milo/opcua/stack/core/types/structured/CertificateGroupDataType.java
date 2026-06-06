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
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.4">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.4</a>
 */
public class CertificateGroupDataType extends BaseConfigurationRecordDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15436");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=16540");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16589");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16634");

  private final NodeId purpose;

  private final NodeId @Nullable [] certificateTypes;

  private final Boolean @Nullable [] isCertificateAssigned;

  private final TrustListValidationOptions validationOptions;

  public CertificateGroupDataType(
      @Nullable String name,
      KeyValuePair @Nullable [] recordProperties,
      NodeId purpose,
      NodeId @Nullable [] certificateTypes,
      Boolean @Nullable [] isCertificateAssigned,
      TrustListValidationOptions validationOptions) {
    super(name, recordProperties);
    this.purpose = purpose;
    this.certificateTypes = certificateTypes;
    this.isCertificateAssigned = isCertificateAssigned;
    this.validationOptions = validationOptions;
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

  public NodeId getPurpose() {
    return purpose;
  }

  public NodeId @Nullable [] getCertificateTypes() {
    return certificateTypes;
  }

  public Boolean @Nullable [] getIsCertificateAssigned() {
    return isCertificateAssigned;
  }

  public TrustListValidationOptions getValidationOptions() {
    return validationOptions;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    CertificateGroupDataType that = (CertificateGroupDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getPurpose(), that.getPurpose());
    eqb.append(getCertificateTypes(), that.getCertificateTypes());
    eqb.append(getIsCertificateAssigned(), that.getIsCertificateAssigned());
    eqb.append(getValidationOptions(), that.getValidationOptions());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getPurpose());
    hcb.append(getCertificateTypes());
    hcb.append(getIsCertificateAssigned());
    hcb.append(getValidationOptions());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", CertificateGroupDataType.class.getSimpleName() + "[", "]");
    joiner.add("purpose=" + getPurpose());
    joiner.add("certificateTypes=" + java.util.Arrays.toString(getCertificateTypes()));
    joiner.add("isCertificateAssigned=" + java.util.Arrays.toString(getIsCertificateAssigned()));
    joiner.add("validationOptions=" + getValidationOptions());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 16540),
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
              "Purpose",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CertificateTypes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IsCertificateAssigned",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ValidationOptions",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23564),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<CertificateGroupDataType> {
    @Override
    public Class<CertificateGroupDataType> getType() {
      return CertificateGroupDataType.class;
    }

    @Override
    public CertificateGroupDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String name;
      final KeyValuePair[] recordProperties;
      final NodeId purpose;
      final NodeId[] certificateTypes;
      final Boolean[] isCertificateAssigned;
      final TrustListValidationOptions validationOptions;
      name = decoder.decodeString("Name");
      recordProperties =
          (KeyValuePair[]) decoder.decodeStructArray("RecordProperties", KeyValuePair.TYPE_ID);
      purpose = decoder.decodeNodeId("Purpose");
      certificateTypes = decoder.decodeNodeIdArray("CertificateTypes");
      isCertificateAssigned = decoder.decodeBooleanArray("IsCertificateAssigned");
      validationOptions = new TrustListValidationOptions(decoder.decodeUInt32("ValidationOptions"));
      return new CertificateGroupDataType(
          name,
          recordProperties,
          purpose,
          certificateTypes,
          isCertificateAssigned,
          validationOptions);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, CertificateGroupDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeStructArray(
          "RecordProperties", value.getRecordProperties(), KeyValuePair.TYPE_ID);
      encoder.encodeNodeId("Purpose", value.getPurpose());
      encoder.encodeNodeIdArray("CertificateTypes", value.getCertificateTypes());
      encoder.encodeBooleanArray("IsCertificateAssigned", value.getIsCertificateAssigned());
      encoder.encodeUInt32("ValidationOptions", value.getValidationOptions().getValue());
    }
  }
}
