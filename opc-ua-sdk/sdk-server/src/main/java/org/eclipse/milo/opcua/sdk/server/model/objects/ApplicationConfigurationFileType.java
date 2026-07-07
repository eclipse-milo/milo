/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.20">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.20</a>
 */
public interface ApplicationConfigurationFileType extends ConfigurationFileType {
  QualifiedProperty<String[]> AVAILABLE_NETWORKS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AvailableNetworks",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<String> AVAILABLE_PORTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AvailablePorts",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=291"),
          -1,
          String.class);

  QualifiedProperty<UShort> MAX_ENDPOINTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxEndpoints",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> MAX_CERTIFICATE_GROUPS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxCertificateGroups",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<String[]> SECURITY_POLICY_URIS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityPolicyUris",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          1,
          String[].class);

  QualifiedProperty<UserTokenPolicy[]> USER_TOKEN_TYPES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UserTokenTypes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=304"),
          1,
          UserTokenPolicy[].class);

  QualifiedProperty<NodeId[]> CERTIFICATE_TYPES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateTypes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<NodeId[]> CERTIFICATE_GROUP_PURPOSES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateGroupPurposes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  String[] getAvailableNetworks();

  void setAvailableNetworks(String[] value);

  PropertyType getAvailableNetworksNode();

  String getAvailablePorts();

  void setAvailablePorts(String value);

  PropertyType getAvailablePortsNode();

  UShort getMaxEndpoints();

  void setMaxEndpoints(UShort value);

  PropertyType getMaxEndpointsNode();

  UShort getMaxCertificateGroups();

  void setMaxCertificateGroups(UShort value);

  PropertyType getMaxCertificateGroupsNode();

  String[] getSecurityPolicyUris();

  void setSecurityPolicyUris(String[] value);

  PropertyType getSecurityPolicyUrisNode();

  UserTokenPolicy[] getUserTokenTypes();

  void setUserTokenTypes(UserTokenPolicy[] value);

  PropertyType getUserTokenTypesNode();

  NodeId[] getCertificateTypes();

  void setCertificateTypes(NodeId[] value);

  PropertyType getCertificateTypesNode();

  NodeId[] getCertificateGroupPurposes();

  void setCertificateGroupPurposes(NodeId[] value);

  PropertyType getCertificateGroupPurposesNode();
}
