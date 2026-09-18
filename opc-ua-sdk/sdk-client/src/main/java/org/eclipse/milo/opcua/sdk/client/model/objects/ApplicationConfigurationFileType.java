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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ApplicationConfigurationFileType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.20">Model
 *     documentation</a>
 */
public interface ApplicationConfigurationFileType extends ConfigurationFileType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15550L);

  QualifiedProperty<UShort> MaxEndpoints_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxEndpoints",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<String> AvailablePorts_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AvailablePorts",
          ExpandedNodeId.of(Namespaces.OPC_UA, 291L),
          -1,
          String.class);

  QualifiedProperty<UserTokenPolicy[]> UserTokenTypes_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UserTokenTypes",
          ExpandedNodeId.of(Namespaces.OPC_UA, 304L),
          1,
          UserTokenPolicy[].class);

  QualifiedProperty<NodeId[]> CertificateTypes_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CertificateTypes",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          1,
          NodeId[].class);

  QualifiedProperty<String[]> AvailableNetworks_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AvailableNetworks",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          1,
          String[].class);

  QualifiedProperty<String[]> SecurityPolicyUris_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityPolicyUris",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23751L),
          1,
          String[].class);

  QualifiedProperty<UShort> MaxCertificateGroups_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxCertificateGroups",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<NodeId[]> CertificateGroupPurposes_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CertificateGroupPurposes",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          1,
          NodeId[].class);

  /**
   * Resolves the mandatory MaxEndpoints child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxEndpointsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxEndpointsNode()}. */
  CompletableFuture<? extends PropertyType> getMaxEndpointsNodeAsync();

  /**
   * Reads the Value of the MaxEndpoints child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readMaxEndpoints() throws UaException;

  /**
   * Writes the Value of the MaxEndpoints child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxEndpoints(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readMaxEndpoints()}. */
  CompletableFuture<? extends @Nullable UShort> readMaxEndpointsAsync();

  /** Asynchronous form of {@link #writeMaxEndpoints}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxEndpointsAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory AvailablePorts child, a PropertyType with DataType NumericRange.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAvailablePortsNode() throws UaException;

  /** Asynchronous form of {@link #getAvailablePortsNode()}. */
  CompletableFuture<? extends PropertyType> getAvailablePortsNodeAsync();

  /**
   * Reads the Value of the AvailablePorts child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readAvailablePorts() throws UaException;

  /**
   * Writes the Value of the AvailablePorts child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAvailablePorts(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readAvailablePorts()}. */
  CompletableFuture<? extends @Nullable String> readAvailablePortsAsync();

  /** Asynchronous form of {@link #writeAvailablePorts}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAvailablePortsAsync(@Nullable String value);

  /**
   * Resolves the mandatory UserTokenTypes child, a PropertyType with DataType UserTokenPolicy.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUserTokenTypesNode() throws UaException;

  /** Asynchronous form of {@link #getUserTokenTypesNode()}. */
  CompletableFuture<? extends PropertyType> getUserTokenTypesNodeAsync();

  /**
   * Reads the Value of the UserTokenTypes child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UserTokenPolicy @Nullable [] readUserTokenTypes() throws UaException;

  /**
   * Writes the Value of the UserTokenTypes child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUserTokenTypes(@Nullable UserTokenPolicy @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readUserTokenTypes()}. */
  CompletableFuture<? extends @Nullable UserTokenPolicy @Nullable []> readUserTokenTypesAsync();

  /** Asynchronous form of {@link #writeUserTokenTypes}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUserTokenTypesAsync(
      @Nullable UserTokenPolicy @Nullable [] value);

  /**
   * Resolves the mandatory CertificateTypes child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCertificateTypesNode() throws UaException;

  /** Asynchronous form of {@link #getCertificateTypesNode()}. */
  CompletableFuture<? extends PropertyType> getCertificateTypesNodeAsync();

  /**
   * Reads the Value of the CertificateTypes child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readCertificateTypes() throws UaException;

  /**
   * Writes the Value of the CertificateTypes child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCertificateTypes(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readCertificateTypes()}. */
  CompletableFuture<? extends NodeId @Nullable []> readCertificateTypesAsync();

  /** Asynchronous form of {@link #writeCertificateTypes}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCertificateTypesAsync(NodeId @Nullable [] value);

  /**
   * Resolves the mandatory AvailableNetworks child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAvailableNetworksNode() throws UaException;

  /** Asynchronous form of {@link #getAvailableNetworksNode()}. */
  CompletableFuture<? extends PropertyType> getAvailableNetworksNodeAsync();

  /**
   * Reads the Value of the AvailableNetworks child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readAvailableNetworks() throws UaException;

  /**
   * Writes the Value of the AvailableNetworks child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAvailableNetworks(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readAvailableNetworks()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readAvailableNetworksAsync();

  /** Asynchronous form of {@link #writeAvailableNetworks}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAvailableNetworksAsync(@Nullable String @Nullable [] value);

  /**
   * Resolves the mandatory SecurityPolicyUris child, a PropertyType with DataType UriString.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSecurityPolicyUrisNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityPolicyUrisNode()}. */
  CompletableFuture<? extends PropertyType> getSecurityPolicyUrisNodeAsync();

  /**
   * Reads the Value of the SecurityPolicyUris child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readSecurityPolicyUris() throws UaException;

  /**
   * Writes the Value of the SecurityPolicyUris child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityPolicyUris(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSecurityPolicyUris()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readSecurityPolicyUrisAsync();

  /** Asynchronous form of {@link #writeSecurityPolicyUris}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecurityPolicyUrisAsync(@Nullable String @Nullable [] value);

  /**
   * Resolves the mandatory MaxCertificateGroups child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxCertificateGroupsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxCertificateGroupsNode()}. */
  CompletableFuture<? extends PropertyType> getMaxCertificateGroupsNodeAsync();

  /**
   * Reads the Value of the MaxCertificateGroups child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readMaxCertificateGroups() throws UaException;

  /**
   * Writes the Value of the MaxCertificateGroups child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxCertificateGroups(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readMaxCertificateGroups()}. */
  CompletableFuture<? extends @Nullable UShort> readMaxCertificateGroupsAsync();

  /**
   * Asynchronous form of {@link #writeMaxCertificateGroups}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxCertificateGroupsAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory CertificateGroupPurposes child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6">Model
   *     documentation</a>
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCertificateGroupPurposesNode() throws UaException;

  /** Asynchronous form of {@link #getCertificateGroupPurposesNode()}. */
  CompletableFuture<? extends PropertyType> getCertificateGroupPurposesNodeAsync();

  /**
   * Reads the Value of the CertificateGroupPurposes child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readCertificateGroupPurposes() throws UaException;

  /**
   * Writes the Value of the CertificateGroupPurposes child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCertificateGroupPurposes(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readCertificateGroupPurposes()}. */
  CompletableFuture<? extends NodeId @Nullable []> readCertificateGroupPurposesAsync();

  /**
   * Asynchronous form of {@link #writeCertificateGroupPurposes}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeCertificateGroupPurposesAsync(NodeId @Nullable [] value);
}
