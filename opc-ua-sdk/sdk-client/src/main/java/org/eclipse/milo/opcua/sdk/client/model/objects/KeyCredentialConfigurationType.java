package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.KeyCredentialConfigurationTypeGetEncryptingKey;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the KeyCredentialConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.5">Model
 *     documentation</a>
 */
public interface KeyCredentialConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18001L);

  QualifiedProperty<String> ProfileUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ProfileUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> ResourceUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ResourceUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> CredentialId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CredentialId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String[]> EndpointUrls_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EndpointUrls",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          1,
          String[].class);

  QualifiedProperty<StatusCode> ServiceStatus_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServiceStatus",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
          -1,
          StatusCode.class);

  /**
   * Resolves the mandatory ProfileUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getProfileUriNode() throws UaException;

  /** Asynchronous form of {@link #getProfileUriNode()}. */
  CompletableFuture<? extends PropertyType> getProfileUriNodeAsync();

  /**
   * Reads the Value of the ProfileUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readProfileUri() throws UaException;

  /**
   * Writes the Value of the ProfileUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeProfileUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readProfileUri()}. */
  CompletableFuture<? extends @Nullable String> readProfileUriAsync();

  /** Asynchronous form of {@link #writeProfileUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeProfileUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory ResourceUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getResourceUriNode() throws UaException;

  /** Asynchronous form of {@link #getResourceUriNode()}. */
  CompletableFuture<? extends PropertyType> getResourceUriNodeAsync();

  /**
   * Reads the Value of the ResourceUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readResourceUri() throws UaException;

  /**
   * Writes the Value of the ResourceUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeResourceUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readResourceUri()}. */
  CompletableFuture<? extends @Nullable String> readResourceUriAsync();

  /** Asynchronous form of {@link #writeResourceUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeResourceUriAsync(@Nullable String value);

  /**
   * Resolves the optional CredentialId child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getCredentialIdNode() throws UaException;

  /** Asynchronous form of {@link #getCredentialIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getCredentialIdNodeAsync();

  /**
   * Reads the Value of the CredentialId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readCredentialId() throws UaException;

  /**
   * Writes the Value of the CredentialId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCredentialId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readCredentialId()}. */
  CompletableFuture<? extends @Nullable String> readCredentialIdAsync();

  /** Asynchronous form of {@link #writeCredentialId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCredentialIdAsync(@Nullable String value);

  /**
   * Resolves the optional EndpointUrls child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getEndpointUrlsNode() throws UaException;

  /** Asynchronous form of {@link #getEndpointUrlsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getEndpointUrlsNodeAsync();

  /**
   * Reads the Value of the EndpointUrls child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readEndpointUrls() throws UaException;

  /**
   * Writes the Value of the EndpointUrls child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEndpointUrls(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readEndpointUrls()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readEndpointUrlsAsync();

  /** Asynchronous form of {@link #writeEndpointUrls}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEndpointUrlsAsync(@Nullable String @Nullable [] value);

  /**
   * Resolves the optional ServiceStatus child, a PropertyType with DataType StatusCode.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getServiceStatusNode() throws UaException;

  /** Asynchronous form of {@link #getServiceStatusNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getServiceStatusNodeAsync();

  /**
   * Reads the Value of the ServiceStatus child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable StatusCode readServiceStatus() throws UaException;

  /**
   * Writes the Value of the ServiceStatus child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServiceStatus(@Nullable StatusCode value) throws UaException;

  /** Asynchronous form of {@link #readServiceStatus()}. */
  CompletableFuture<? extends @Nullable StatusCode> readServiceStatusAsync();

  /** Asynchronous form of {@link #writeServiceStatus}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServiceStatusAsync(@Nullable StatusCode value);

  /**
   * Resolves the optional DeleteCredential Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getDeleteCredentialMethodNode() throws UaException;

  /** Asynchronous form of {@link #getDeleteCredentialMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getDeleteCredentialMethodNodeAsync();

  /**
   * Calls the DeleteCredential Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8">Model
   *     documentation</a>
   */
  void deleteCredential() throws UaException;

  /**
   * Calls the DeleteCredential Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDeleteCredential() throws UaException;

  /**
   * Calls the DeleteCredential Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDeleteCredentialWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #deleteCredential}. */
  CompletableFuture<Void> deleteCredentialAsync();

  /** Asynchronous form of {@link #callDeleteCredential}. */
  CompletableFuture<MethodCallResult<Void>> callDeleteCredentialAsync();

  /** Asynchronous form of {@link #callDeleteCredentialWith}. */
  CompletableFuture<MethodCallResult<Void>> callDeleteCredentialWithAsync(
      MethodCallOptions options);

  /**
   * Resolves the optional GetEncryptingKey Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetEncryptingKeyMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGetEncryptingKeyMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getGetEncryptingKeyMethodNodeAsync();

  /**
   * Calls the GetEncryptingKey Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6">Model
   *     documentation</a>
   */
  KeyCredentialConfigurationTypeGetEncryptingKey.Outputs getEncryptingKey(
      @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri)
      throws UaException;

  /**
   * Calls the GetEncryptingKey Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<KeyCredentialConfigurationTypeGetEncryptingKey.Outputs> callGetEncryptingKey(
      @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri)
      throws UaException;

  /**
   * Calls the GetEncryptingKey Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<KeyCredentialConfigurationTypeGetEncryptingKey.Outputs> callGetEncryptingKeyWith(
      MethodCallOptions options,
      @Nullable String credentialId,
      @Nullable String requestedSecurityPolicyUri)
      throws UaException;

  /** Asynchronous form of {@link #getEncryptingKey}. */
  CompletableFuture<KeyCredentialConfigurationTypeGetEncryptingKey.Outputs> getEncryptingKeyAsync(
      @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri);

  /** Asynchronous form of {@link #callGetEncryptingKey}. */
  CompletableFuture<MethodCallResult<KeyCredentialConfigurationTypeGetEncryptingKey.Outputs>>
      callGetEncryptingKeyAsync(
          @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri);

  /** Asynchronous form of {@link #callGetEncryptingKeyWith}. */
  CompletableFuture<MethodCallResult<KeyCredentialConfigurationTypeGetEncryptingKey.Outputs>>
      callGetEncryptingKeyWithAsync(
          MethodCallOptions options,
          @Nullable String credentialId,
          @Nullable String requestedSecurityPolicyUri);

  /**
   * Resolves the optional UpdateCredential Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getUpdateCredentialMethodNode() throws UaException;

  /** Asynchronous form of {@link #getUpdateCredentialMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getUpdateCredentialMethodNodeAsync();

  /**
   * Calls the UpdateCredential Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7">Model
   *     documentation</a>
   */
  void updateCredential(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException;

  /**
   * Calls the UpdateCredential Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callUpdateCredential(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException;

  /**
   * Calls the UpdateCredential Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callUpdateCredentialWith(
      MethodCallOptions options,
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException;

  /** Asynchronous form of {@link #updateCredential}. */
  CompletableFuture<Void> updateCredentialAsync(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri);

  /** Asynchronous form of {@link #callUpdateCredential}. */
  CompletableFuture<MethodCallResult<Void>> callUpdateCredentialAsync(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri);

  /** Asynchronous form of {@link #callUpdateCredentialWith}. */
  CompletableFuture<MethodCallResult<Void>> callUpdateCredentialWithAsync(
      MethodCallOptions options,
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri);
}
