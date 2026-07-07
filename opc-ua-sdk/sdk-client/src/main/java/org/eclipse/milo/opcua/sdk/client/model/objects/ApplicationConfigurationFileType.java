/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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

  /**
   * Get the local value of the AvailableNetworks Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AvailableNetworks Node.
   * @throws UaException if an error occurs creating or getting the AvailableNetworks Node.
   */
  String[] getAvailableNetworks() throws UaException;

  /**
   * Set the local value of the AvailableNetworks Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AvailableNetworks Node.
   * @throws UaException if an error occurs creating or getting the AvailableNetworks Node.
   */
  void setAvailableNetworks(String[] value) throws UaException;

  /**
   * Read the value of the AvailableNetworks Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readAvailableNetworks() throws UaException;

  /**
   * Write a new value for the AvailableNetworks Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAvailableNetworks(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAvailableNetworks}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readAvailableNetworksAsync();

  /**
   * An asynchronous implementation of {@link #writeAvailableNetworks}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAvailableNetworksAsync(String[] value);

  /**
   * Get the AvailableNetworks {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AvailableNetworks {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAvailableNetworksNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAvailableNetworksNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAvailableNetworksNodeAsync();

  /**
   * Get the local value of the AvailablePorts Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AvailablePorts Node.
   * @throws UaException if an error occurs creating or getting the AvailablePorts Node.
   */
  String getAvailablePorts() throws UaException;

  /**
   * Set the local value of the AvailablePorts Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AvailablePorts Node.
   * @throws UaException if an error occurs creating or getting the AvailablePorts Node.
   */
  void setAvailablePorts(String value) throws UaException;

  /**
   * Read the value of the AvailablePorts Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readAvailablePorts() throws UaException;

  /**
   * Write a new value for the AvailablePorts Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAvailablePorts(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAvailablePorts}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readAvailablePortsAsync();

  /**
   * An asynchronous implementation of {@link #writeAvailablePorts}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAvailablePortsAsync(String value);

  /**
   * Get the AvailablePorts {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AvailablePorts {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAvailablePortsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAvailablePortsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAvailablePortsNodeAsync();

  /**
   * Get the local value of the MaxEndpoints Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxEndpoints Node.
   * @throws UaException if an error occurs creating or getting the MaxEndpoints Node.
   */
  UShort getMaxEndpoints() throws UaException;

  /**
   * Set the local value of the MaxEndpoints Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxEndpoints Node.
   * @throws UaException if an error occurs creating or getting the MaxEndpoints Node.
   */
  void setMaxEndpoints(UShort value) throws UaException;

  /**
   * Read the value of the MaxEndpoints Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readMaxEndpoints() throws UaException;

  /**
   * Write a new value for the MaxEndpoints Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxEndpoints(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxEndpoints}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readMaxEndpointsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxEndpoints}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxEndpointsAsync(UShort value);

  /**
   * Get the MaxEndpoints {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxEndpoints {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxEndpointsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxEndpointsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxEndpointsNodeAsync();

  /**
   * Get the local value of the MaxCertificateGroups Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxCertificateGroups Node.
   * @throws UaException if an error occurs creating or getting the MaxCertificateGroups Node.
   */
  UShort getMaxCertificateGroups() throws UaException;

  /**
   * Set the local value of the MaxCertificateGroups Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxCertificateGroups Node.
   * @throws UaException if an error occurs creating or getting the MaxCertificateGroups Node.
   */
  void setMaxCertificateGroups(UShort value) throws UaException;

  /**
   * Read the value of the MaxCertificateGroups Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readMaxCertificateGroups() throws UaException;

  /**
   * Write a new value for the MaxCertificateGroups Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxCertificateGroups(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxCertificateGroups}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readMaxCertificateGroupsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxCertificateGroups}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxCertificateGroupsAsync(UShort value);

  /**
   * Get the MaxCertificateGroups {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxCertificateGroups {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxCertificateGroupsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxCertificateGroupsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxCertificateGroupsNodeAsync();

  /**
   * Get the local value of the SecurityPolicyUris Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityPolicyUris Node.
   * @throws UaException if an error occurs creating or getting the SecurityPolicyUris Node.
   */
  String[] getSecurityPolicyUris() throws UaException;

  /**
   * Set the local value of the SecurityPolicyUris Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityPolicyUris Node.
   * @throws UaException if an error occurs creating or getting the SecurityPolicyUris Node.
   */
  void setSecurityPolicyUris(String[] value) throws UaException;

  /**
   * Read the value of the SecurityPolicyUris Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readSecurityPolicyUris() throws UaException;

  /**
   * Write a new value for the SecurityPolicyUris Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityPolicyUris(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityPolicyUris}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readSecurityPolicyUrisAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityPolicyUris}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityPolicyUrisAsync(String[] value);

  /**
   * Get the SecurityPolicyUris {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityPolicyUris {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecurityPolicyUrisNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityPolicyUrisNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecurityPolicyUrisNodeAsync();

  /**
   * Get the local value of the UserTokenTypes Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UserTokenTypes Node.
   * @throws UaException if an error occurs creating or getting the UserTokenTypes Node.
   */
  UserTokenPolicy[] getUserTokenTypes() throws UaException;

  /**
   * Set the local value of the UserTokenTypes Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UserTokenTypes Node.
   * @throws UaException if an error occurs creating or getting the UserTokenTypes Node.
   */
  void setUserTokenTypes(UserTokenPolicy[] value) throws UaException;

  /**
   * Read the value of the UserTokenTypes Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UserTokenPolicy[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UserTokenPolicy[] readUserTokenTypes() throws UaException;

  /**
   * Write a new value for the UserTokenTypes Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UserTokenPolicy[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUserTokenTypes(UserTokenPolicy[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUserTokenTypes}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UserTokenPolicy[]> readUserTokenTypesAsync();

  /**
   * An asynchronous implementation of {@link #writeUserTokenTypes}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUserTokenTypesAsync(UserTokenPolicy[] value);

  /**
   * Get the UserTokenTypes {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UserTokenTypes {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUserTokenTypesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUserTokenTypesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUserTokenTypesNodeAsync();

  /**
   * Get the local value of the CertificateTypes Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CertificateTypes Node.
   * @throws UaException if an error occurs creating or getting the CertificateTypes Node.
   */
  NodeId[] getCertificateTypes() throws UaException;

  /**
   * Set the local value of the CertificateTypes Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CertificateTypes Node.
   * @throws UaException if an error occurs creating or getting the CertificateTypes Node.
   */
  void setCertificateTypes(NodeId[] value) throws UaException;

  /**
   * Read the value of the CertificateTypes Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId[] readCertificateTypes() throws UaException;

  /**
   * Write a new value for the CertificateTypes Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCertificateTypes(NodeId[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCertificateTypes}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId[]> readCertificateTypesAsync();

  /**
   * An asynchronous implementation of {@link #writeCertificateTypes}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCertificateTypesAsync(NodeId[] value);

  /**
   * Get the CertificateTypes {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CertificateTypes {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCertificateTypesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCertificateTypesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCertificateTypesNodeAsync();

  /**
   * Get the local value of the CertificateGroupPurposes Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CertificateGroupPurposes Node.
   * @throws UaException if an error occurs creating or getting the CertificateGroupPurposes Node.
   */
  NodeId[] getCertificateGroupPurposes() throws UaException;

  /**
   * Set the local value of the CertificateGroupPurposes Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CertificateGroupPurposes Node.
   * @throws UaException if an error occurs creating or getting the CertificateGroupPurposes Node.
   */
  void setCertificateGroupPurposes(NodeId[] value) throws UaException;

  /**
   * Read the value of the CertificateGroupPurposes Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link NodeId[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId[] readCertificateGroupPurposes() throws UaException;

  /**
   * Write a new value for the CertificateGroupPurposes Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link NodeId[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCertificateGroupPurposes(NodeId[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCertificateGroupPurposes}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId[]> readCertificateGroupPurposesAsync();

  /**
   * An asynchronous implementation of {@link #writeCertificateGroupPurposes}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCertificateGroupPurposesAsync(NodeId[] value);

  /**
   * Get the CertificateGroupPurposes {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CertificateGroupPurposes {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCertificateGroupPurposesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCertificateGroupPurposesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCertificateGroupPurposesNodeAsync();
}
