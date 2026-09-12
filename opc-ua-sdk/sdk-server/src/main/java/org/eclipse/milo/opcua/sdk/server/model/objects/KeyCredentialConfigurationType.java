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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.KeyCredentialConfigurationTypeGetEncryptingKeyOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
  @Nullable String getResourceUri();

  /** Sets the existing node's local value. */
  void setResourceUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getResourceUriNode();

  /** Gets the existing node's local value. */
  @Nullable String getProfileUri();

  /** Sets the existing node's local value. */
  void setProfileUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getProfileUriNode();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getEndpointUrls();

  /** Sets the existing node's local value. */
  void setEndpointUrls(@Nullable String @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEndpointUrlsNode();

  /** Gets the existing node's local value. */
  @Nullable String getCredentialId();

  /** Sets the existing node's local value. */
  void setCredentialId(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCredentialIdNode();

  /** Gets the existing node's local value. */
  @Nullable StatusCode getServiceStatus();

  /** Sets the existing node's local value. */
  void setServiceStatus(@Nullable StatusCode value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getServiceStatusNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getGetEncryptingKeyMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetEncryptingKey(MethodBindings bindings, GetEncryptingKeyHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetEncryptingKeyDetailed(
      MethodBindings bindings, GetEncryptingKeyDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getUpdateCredentialMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUpdateCredential(MethodBindings bindings, UpdateCredentialHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUpdateCredentialDetailed(
      MethodBindings bindings, UpdateCredentialDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getDeleteCredentialMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDeleteCredential(MethodBindings bindings, DeleteCredentialHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDeleteCredentialDetailed(
      MethodBindings bindings, DeleteCredentialDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6 */
  @FunctionalInterface
  interface GetEncryptingKeyHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    KeyCredentialConfigurationTypeGetEncryptingKeyOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String credentialId,
        @Nullable String requestedSecurityPolicyUri)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6 */
  @FunctionalInterface
  interface GetEncryptingKeyDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<KeyCredentialConfigurationTypeGetEncryptingKeyOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String credentialId,
        @Nullable String requestedSecurityPolicyUri)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7 */
  @FunctionalInterface
  interface UpdateCredentialHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String credentialId,
        @Nullable ByteString credentialSecret,
        @Nullable String certificateThumbprint,
        @Nullable String securityPolicyUri)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7 */
  @FunctionalInterface
  interface UpdateCredentialDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String credentialId,
        @Nullable ByteString credentialSecret,
        @Nullable String certificateThumbprint,
        @Nullable String securityPolicyUri)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8 */
  @FunctionalInterface
  interface DeleteCredentialHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8 */
  @FunctionalInterface
  interface DeleteCredentialDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }
}
