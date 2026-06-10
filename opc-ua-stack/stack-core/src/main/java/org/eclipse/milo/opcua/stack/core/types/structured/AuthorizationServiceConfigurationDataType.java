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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.5">https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.5</a>
 */
public class AuthorizationServiceConfigurationDataType extends BaseConfigurationRecordDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=23744");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23755");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23763");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=23777");

  private final String serviceUri;

  private final ServiceCertificateDataType @Nullable [] serviceCertificates;

  private final @Nullable String issuerEndpointSettings;

  public AuthorizationServiceConfigurationDataType(
      @Nullable String name,
      KeyValuePair @Nullable [] recordProperties,
      String serviceUri,
      ServiceCertificateDataType @Nullable [] serviceCertificates,
      @Nullable String issuerEndpointSettings) {
    super(name, recordProperties);
    this.serviceUri = serviceUri;
    this.serviceCertificates = serviceCertificates;
    this.issuerEndpointSettings = issuerEndpointSettings;
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

  public String getServiceUri() {
    return serviceUri;
  }

  public ServiceCertificateDataType @Nullable [] getServiceCertificates() {
    return serviceCertificates;
  }

  public @Nullable String getIssuerEndpointSettings() {
    return issuerEndpointSettings;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AuthorizationServiceConfigurationDataType that =
        (AuthorizationServiceConfigurationDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getServiceUri(), that.getServiceUri());
    eqb.append(getServiceCertificates(), that.getServiceCertificates());
    eqb.append(getIssuerEndpointSettings(), that.getIssuerEndpointSettings());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getServiceUri());
    hcb.append(getServiceCertificates());
    hcb.append(getIssuerEndpointSettings());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(
            ", ", AuthorizationServiceConfigurationDataType.class.getSimpleName() + "[", "]");
    joiner.add("serviceUri='" + getServiceUri() + "'");
    joiner.add("serviceCertificates=" + java.util.Arrays.toString(getServiceCertificates()));
    joiner.add("issuerEndpointSettings='" + getIssuerEndpointSettings() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 23755),
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
              "ServiceUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23751),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServiceCertificates",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23724),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IssuerEndpointSettings",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec
      extends GenericDataTypeCodec<AuthorizationServiceConfigurationDataType> {
    @Override
    public Class<AuthorizationServiceConfigurationDataType> getType() {
      return AuthorizationServiceConfigurationDataType.class;
    }

    @Override
    public AuthorizationServiceConfigurationDataType decodeType(
        EncodingContext context, UaDecoder decoder) {
      final String name;
      final KeyValuePair[] recordProperties;
      final String serviceUri;
      final ServiceCertificateDataType[] serviceCertificates;
      final String issuerEndpointSettings;
      name = decoder.decodeString("Name");
      recordProperties =
          (KeyValuePair[]) decoder.decodeStructArray("RecordProperties", KeyValuePair.TYPE_ID);
      serviceUri = decoder.decodeString("ServiceUri");
      serviceCertificates =
          (ServiceCertificateDataType[])
              decoder.decodeStructArray("ServiceCertificates", ServiceCertificateDataType.TYPE_ID);
      issuerEndpointSettings = decoder.decodeString("IssuerEndpointSettings");
      return new AuthorizationServiceConfigurationDataType(
          name, recordProperties, serviceUri, serviceCertificates, issuerEndpointSettings);
    }

    @Override
    public void encodeType(
        EncodingContext context,
        UaEncoder encoder,
        AuthorizationServiceConfigurationDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeStructArray(
          "RecordProperties", value.getRecordProperties(), KeyValuePair.TYPE_ID);
      encoder.encodeString("ServiceUri", value.getServiceUri());
      encoder.encodeStructArray(
          "ServiceCertificates",
          value.getServiceCertificates(),
          ServiceCertificateDataType.TYPE_ID);
      encoder.encodeString("IssuerEndpointSettings", value.getIssuerEndpointSettings());
    }
  }
}
