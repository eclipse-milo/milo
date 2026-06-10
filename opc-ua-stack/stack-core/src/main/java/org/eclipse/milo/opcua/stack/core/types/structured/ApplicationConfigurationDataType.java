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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.19">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.19</a>
 */
public class ApplicationConfigurationDataType extends BaseConfigurationDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=23743");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23754");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23762");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=23776");

  private final ApplicationIdentityDataType applicationIdentity;

  private final CertificateGroupDataType @Nullable [] certificateGroups;

  private final ServerEndpointDataType @Nullable [] serverEndpoints;

  private final EndpointDataType @Nullable [] clientEndpoints;

  private final SecuritySettingsDataType @Nullable [] securitySettings;

  private final UserTokenSettingsDataType @Nullable [] userTokenSettings;

  private final AuthorizationServiceConfigurationDataType @Nullable [] authorizationServices;

  public ApplicationConfigurationDataType(
      UInteger configurationVersion,
      KeyValuePair @Nullable [] configurationProperties,
      ApplicationIdentityDataType applicationIdentity,
      CertificateGroupDataType @Nullable [] certificateGroups,
      ServerEndpointDataType @Nullable [] serverEndpoints,
      EndpointDataType @Nullable [] clientEndpoints,
      SecuritySettingsDataType @Nullable [] securitySettings,
      UserTokenSettingsDataType @Nullable [] userTokenSettings,
      AuthorizationServiceConfigurationDataType @Nullable [] authorizationServices) {
    super(configurationVersion, configurationProperties);
    this.applicationIdentity = applicationIdentity;
    this.certificateGroups = certificateGroups;
    this.serverEndpoints = serverEndpoints;
    this.clientEndpoints = clientEndpoints;
    this.securitySettings = securitySettings;
    this.userTokenSettings = userTokenSettings;
    this.authorizationServices = authorizationServices;
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

  public ApplicationIdentityDataType getApplicationIdentity() {
    return applicationIdentity;
  }

  public CertificateGroupDataType @Nullable [] getCertificateGroups() {
    return certificateGroups;
  }

  public ServerEndpointDataType @Nullable [] getServerEndpoints() {
    return serverEndpoints;
  }

  public EndpointDataType @Nullable [] getClientEndpoints() {
    return clientEndpoints;
  }

  public SecuritySettingsDataType @Nullable [] getSecuritySettings() {
    return securitySettings;
  }

  public UserTokenSettingsDataType @Nullable [] getUserTokenSettings() {
    return userTokenSettings;
  }

  public AuthorizationServiceConfigurationDataType @Nullable [] getAuthorizationServices() {
    return authorizationServices;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ApplicationConfigurationDataType that = (ApplicationConfigurationDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getApplicationIdentity(), that.getApplicationIdentity());
    eqb.append(getCertificateGroups(), that.getCertificateGroups());
    eqb.append(getServerEndpoints(), that.getServerEndpoints());
    eqb.append(getClientEndpoints(), that.getClientEndpoints());
    eqb.append(getSecuritySettings(), that.getSecuritySettings());
    eqb.append(getUserTokenSettings(), that.getUserTokenSettings());
    eqb.append(getAuthorizationServices(), that.getAuthorizationServices());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getApplicationIdentity());
    hcb.append(getCertificateGroups());
    hcb.append(getServerEndpoints());
    hcb.append(getClientEndpoints());
    hcb.append(getSecuritySettings());
    hcb.append(getUserTokenSettings());
    hcb.append(getAuthorizationServices());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", ApplicationConfigurationDataType.class.getSimpleName() + "[", "]");
    joiner.add("applicationIdentity=" + getApplicationIdentity());
    joiner.add("certificateGroups=" + java.util.Arrays.toString(getCertificateGroups()));
    joiner.add("serverEndpoints=" + java.util.Arrays.toString(getServerEndpoints()));
    joiner.add("clientEndpoints=" + java.util.Arrays.toString(getClientEndpoints()));
    joiner.add("securitySettings=" + java.util.Arrays.toString(getSecuritySettings()));
    joiner.add("userTokenSettings=" + java.util.Arrays.toString(getUserTokenSettings()));
    joiner.add("authorizationServices=" + java.util.Arrays.toString(getAuthorizationServices()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 23754),
        new NodeId(0, 15434),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ConfigurationVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20998),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ConfigurationProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ApplicationIdentity",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15556),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CertificateGroups",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15436),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerEndpoints",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15558),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ClientEndpoints",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15557),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecuritySettings",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15559),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserTokenSettings",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15560),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AuthorizationServices",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23744),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ApplicationConfigurationDataType> {
    @Override
    public Class<ApplicationConfigurationDataType> getType() {
      return ApplicationConfigurationDataType.class;
    }

    @Override
    public ApplicationConfigurationDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger configurationVersion;
      final KeyValuePair[] configurationProperties;
      final ApplicationIdentityDataType applicationIdentity;
      final CertificateGroupDataType[] certificateGroups;
      final ServerEndpointDataType[] serverEndpoints;
      final EndpointDataType[] clientEndpoints;
      final SecuritySettingsDataType[] securitySettings;
      final UserTokenSettingsDataType[] userTokenSettings;
      final AuthorizationServiceConfigurationDataType[] authorizationServices;
      configurationVersion = decoder.decodeUInt32("ConfigurationVersion");
      configurationProperties =
          (KeyValuePair[])
              decoder.decodeStructArray("ConfigurationProperties", KeyValuePair.TYPE_ID);
      applicationIdentity =
          (ApplicationIdentityDataType)
              decoder.decodeStruct("ApplicationIdentity", ApplicationIdentityDataType.TYPE_ID);
      certificateGroups =
          (CertificateGroupDataType[])
              decoder.decodeStructArray("CertificateGroups", CertificateGroupDataType.TYPE_ID);
      serverEndpoints =
          (ServerEndpointDataType[])
              decoder.decodeStructArray("ServerEndpoints", ServerEndpointDataType.TYPE_ID);
      clientEndpoints =
          (EndpointDataType[])
              decoder.decodeStructArray("ClientEndpoints", EndpointDataType.TYPE_ID);
      securitySettings =
          (SecuritySettingsDataType[])
              decoder.decodeStructArray("SecuritySettings", SecuritySettingsDataType.TYPE_ID);
      userTokenSettings =
          (UserTokenSettingsDataType[])
              decoder.decodeStructArray("UserTokenSettings", UserTokenSettingsDataType.TYPE_ID);
      authorizationServices =
          (AuthorizationServiceConfigurationDataType[])
              decoder.decodeStructArray(
                  "AuthorizationServices", AuthorizationServiceConfigurationDataType.TYPE_ID);
      return new ApplicationConfigurationDataType(
          configurationVersion,
          configurationProperties,
          applicationIdentity,
          certificateGroups,
          serverEndpoints,
          clientEndpoints,
          securitySettings,
          userTokenSettings,
          authorizationServices);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ApplicationConfigurationDataType value) {
      encoder.encodeUInt32("ConfigurationVersion", value.getConfigurationVersion());
      encoder.encodeStructArray(
          "ConfigurationProperties", value.getConfigurationProperties(), KeyValuePair.TYPE_ID);
      encoder.encodeStruct(
          "ApplicationIdentity",
          value.getApplicationIdentity(),
          ApplicationIdentityDataType.TYPE_ID);
      encoder.encodeStructArray(
          "CertificateGroups", value.getCertificateGroups(), CertificateGroupDataType.TYPE_ID);
      encoder.encodeStructArray(
          "ServerEndpoints", value.getServerEndpoints(), ServerEndpointDataType.TYPE_ID);
      encoder.encodeStructArray(
          "ClientEndpoints", value.getClientEndpoints(), EndpointDataType.TYPE_ID);
      encoder.encodeStructArray(
          "SecuritySettings", value.getSecuritySettings(), SecuritySettingsDataType.TYPE_ID);
      encoder.encodeStructArray(
          "UserTokenSettings", value.getUserTokenSettings(), UserTokenSettingsDataType.TYPE_ID);
      encoder.encodeStructArray(
          "AuthorizationServices",
          value.getAuthorizationServices(),
          AuthorizationServiceConfigurationDataType.TYPE_ID);
    }
  }
}
