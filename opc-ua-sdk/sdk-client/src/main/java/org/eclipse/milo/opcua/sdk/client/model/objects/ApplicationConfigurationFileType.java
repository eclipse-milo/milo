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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.20">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.20</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getAvailableNetworks() throws UaException;

  /** Sets the existing node's local value. */
  void setAvailableNetworks(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readAvailableNetworks() throws UaException;

  /** Writes the value remotely. */
  void writeAvailableNetworks(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readAvailableNetworksAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAvailableNetworksAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAvailableNetworksNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAvailableNetworksNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getAvailablePorts() throws UaException;

  /** Sets the existing node's local value. */
  void setAvailablePorts(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readAvailablePorts() throws UaException;

  /** Writes the value remotely. */
  void writeAvailablePorts(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readAvailablePortsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAvailablePortsAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAvailablePortsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAvailablePortsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxEndpoints() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxEndpoints(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readMaxEndpoints() throws UaException;

  /** Writes the value remotely. */
  void writeMaxEndpoints(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readMaxEndpointsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxEndpointsAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxEndpointsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxEndpointsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxCertificateGroups() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxCertificateGroups(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readMaxCertificateGroups() throws UaException;

  /** Writes the value remotely. */
  void writeMaxCertificateGroups(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readMaxCertificateGroupsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxCertificateGroupsAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxCertificateGroupsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxCertificateGroupsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getSecurityPolicyUris() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityPolicyUris(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readSecurityPolicyUris() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityPolicyUris(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readSecurityPolicyUrisAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityPolicyUrisAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityPolicyUrisNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSecurityPolicyUrisNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UserTokenPolicy @Nullable [] getUserTokenTypes() throws UaException;

  /** Sets the existing node's local value. */
  void setUserTokenTypes(@Nullable UserTokenPolicy @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UserTokenPolicy @Nullable [] readUserTokenTypes() throws UaException;

  /** Writes the value remotely. */
  void writeUserTokenTypes(@Nullable UserTokenPolicy @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UserTokenPolicy @Nullable []> readUserTokenTypesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUserTokenTypesAsync(
      @Nullable UserTokenPolicy @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUserTokenTypesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUserTokenTypesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getCertificateTypes() throws UaException;

  /** Sets the existing node's local value. */
  void setCertificateTypes(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readCertificateTypes() throws UaException;

  /** Writes the value remotely. */
  void writeCertificateTypes(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readCertificateTypesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCertificateTypesAsync(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCertificateTypesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCertificateTypesNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Gets the existing node's local value.
   */
  @Nullable NodeId @Nullable [] getCertificateGroupPurposes() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Sets the existing node's local value.
   */
  void setCertificateGroupPurposes(@Nullable NodeId @Nullable [] value) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Reads the value remotely; requires Good status.
   */
  @Nullable NodeId @Nullable [] readCertificateGroupPurposes() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Writes the value remotely.
   */
  void writeCertificateGroupPurposes(@Nullable NodeId @Nullable [] value) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Reads the value remotely; requires Good status.
   */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readCertificateGroupPurposesAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Writes the value remotely.
   */
  CompletableFuture<StatusCode> writeCertificateGroupPurposesAsync(
      @Nullable NodeId @Nullable [] value);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCertificateGroupPurposesNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCertificateGroupPurposesNodeAsync();
}
