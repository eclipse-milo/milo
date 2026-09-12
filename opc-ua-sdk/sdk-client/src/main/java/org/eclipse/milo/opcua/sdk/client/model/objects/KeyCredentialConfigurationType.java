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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.KeyCredentialConfigurationTypeGetEncryptingKeyOutputs;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.5">https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.5</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface KeyCredentialConfigurationType extends BaseObjectType {
  QualifiedProperty<String> RESOURCE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ResourceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> PROFILE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProfileUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String[]> ENDPOINT_URLS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EndpointUrls",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<String> CREDENTIAL_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CredentialId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<StatusCode> SERVICE_STATUS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServiceStatus",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  /** Gets the existing node's local value. */
  @Nullable String getResourceUri() throws UaException;

  /** Sets the existing node's local value. */
  void setResourceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readResourceUri() throws UaException;

  /** Writes the value remotely. */
  void writeResourceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readResourceUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeResourceUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getResourceUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getResourceUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getProfileUri() throws UaException;

  /** Sets the existing node's local value. */
  void setProfileUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readProfileUri() throws UaException;

  /** Writes the value remotely. */
  void writeProfileUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readProfileUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeProfileUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getProfileUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getProfileUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getEndpointUrls() throws UaException;

  /** Sets the existing node's local value. */
  void setEndpointUrls(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readEndpointUrls() throws UaException;

  /** Writes the value remotely. */
  void writeEndpointUrls(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readEndpointUrlsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEndpointUrlsAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEndpointUrlsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getEndpointUrlsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getCredentialId() throws UaException;

  /** Sets the existing node's local value. */
  void setCredentialId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readCredentialId() throws UaException;

  /** Writes the value remotely. */
  void writeCredentialId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readCredentialIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCredentialIdAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCredentialIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getCredentialIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable StatusCode getServiceStatus() throws UaException;

  /** Sets the existing node's local value. */
  void setServiceStatus(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable StatusCode readServiceStatus() throws UaException;

  /** Writes the value remotely. */
  void writeServiceStatus(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable StatusCode> readServiceStatusAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServiceStatusAsync(@Nullable StatusCode value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getServiceStatusNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getServiceStatusNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getGetEncryptingKeyMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getGetEncryptingKeyMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6
   *
   * <p>Invokes <code>GetEncryptingKey</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  KeyCredentialConfigurationTypeGetEncryptingKeyOutputs callGetEncryptingKey(
      @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6
   *
   * <p>Invokes <code>GetEncryptingKey</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>
      callGetEncryptingKeyAsync(
          @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6
   *
   * <p>Invokes <code>GetEncryptingKey</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>
      callGetEncryptingKeyDetailed(
          @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6
   *
   * <p>Invokes <code>GetEncryptingKey</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>
      callGetEncryptingKeyDetailed(
          MethodCallOptions options,
          @Nullable String credentialId,
          @Nullable String requestedSecurityPolicyUri)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6
   *
   * <p>Invokes <code>GetEncryptingKey</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends
              MethodCallResult<? extends KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>>
      callGetEncryptingKeyDetailedAsync(
          @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6
   *
   * <p>Invokes <code>GetEncryptingKey</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends
              MethodCallResult<? extends KeyCredentialConfigurationTypeGetEncryptingKeyOutputs>>
      callGetEncryptingKeyDetailedAsync(
          MethodCallOptions options,
          @Nullable String credentialId,
          @Nullable String requestedSecurityPolicyUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getUpdateCredentialMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getUpdateCredentialMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7
   *
   * <p>Invokes <code>UpdateCredential</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callUpdateCredential(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7
   *
   * <p>Invokes <code>UpdateCredential</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callUpdateCredentialAsync(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7
   *
   * <p>Invokes <code>UpdateCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callUpdateCredentialDetailed(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7
   *
   * <p>Invokes <code>UpdateCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callUpdateCredentialDetailed(
      MethodCallOptions options,
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7
   *
   * <p>Invokes <code>UpdateCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callUpdateCredentialDetailedAsync(
          @Nullable String credentialId,
          @Nullable ByteString credentialSecret,
          @Nullable String certificateThumbprint,
          @Nullable String securityPolicyUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7
   *
   * <p>Invokes <code>UpdateCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callUpdateCredentialDetailedAsync(
          MethodCallOptions options,
          @Nullable String credentialId,
          @Nullable ByteString credentialSecret,
          @Nullable String certificateThumbprint,
          @Nullable String securityPolicyUri);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getDeleteCredentialMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getDeleteCredentialMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8
   *
   * <p>Invokes <code>DeleteCredential</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callDeleteCredential() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8
   *
   * <p>Invokes <code>DeleteCredential</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callDeleteCredentialAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8
   *
   * <p>Invokes <code>DeleteCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callDeleteCredentialDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8
   *
   * <p>Invokes <code>DeleteCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callDeleteCredentialDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8
   *
   * <p>Invokes <code>DeleteCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeleteCredentialDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8
   *
   * <p>Invokes <code>DeleteCredential</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeleteCredentialDetailedAsync(MethodCallOptions options);
}
