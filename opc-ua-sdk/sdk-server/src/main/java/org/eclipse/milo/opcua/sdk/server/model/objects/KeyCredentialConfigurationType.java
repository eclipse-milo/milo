package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.KeyCredentialConfigurationTypeGetEncryptingKey;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the KeyCredentialConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.5">Model
 *     documentation</a>
 */
public interface KeyCredentialConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18001L);

  /**
   * Returns the optional CredentialId child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getCredentialIdNode();

  /**
   * Returns the Value of the CredentialId child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getCredentialId();

  /**
   * Sets the Value of the CredentialId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCredentialId(@Nullable String value);

  /**
   * Returns the optional EndpointUrls child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getEndpointUrlsNode();

  /**
   * Returns the Value of the EndpointUrls child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getEndpointUrls();

  /**
   * Sets the Value of the EndpointUrls child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEndpointUrls(@Nullable String @Nullable [] value);

  /**
   * Returns the mandatory ProfileUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getProfileUriNode();

  /**
   * Returns the Value of the ProfileUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getProfileUri();

  /**
   * Sets the Value of the ProfileUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setProfileUri(@Nullable String value);

  /**
   * Returns the mandatory ResourceUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getResourceUriNode();

  /**
   * Returns the Value of the ResourceUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getResourceUri();

  /**
   * Sets the Value of the ResourceUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setResourceUri(@Nullable String value);

  /**
   * Returns the optional ServiceStatus child, a PropertyType with DataType StatusCode.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getServiceStatusNode();

  /**
   * Returns the Value of the ServiceStatus child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable StatusCode getServiceStatus();

  /**
   * Sets the Value of the ServiceStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServiceStatus(@Nullable StatusCode value);

  /**
   * Returns the optional DeleteCredential Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getDeleteCredentialMethodNode();

  /**
   * Sets this instance's DeleteCredential handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setDeleteCredentialHandler(@Nullable DeleteCredentialHandler handler);

  /**
   * Returns the optional GetEncryptingKey Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetEncryptingKeyMethodNode();

  /**
   * Sets this instance's GetEncryptingKey handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGetEncryptingKeyHandler(@Nullable GetEncryptingKeyHandler handler);

  /**
   * Returns the optional UpdateCredential Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getUpdateCredentialMethodNode();

  /**
   * Sets this instance's UpdateCredential handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setUpdateCredentialHandler(@Nullable UpdateCredentialHandler handler);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /**
   * Handles calls to the DeleteCredential Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.8">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface DeleteCredentialHandler {
    /**
     * Handles a call to the DeleteCredential Method.
     *
     * @throws UaException if the call fails.
     */
    void deleteCredential(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException;
  }

  /**
   * Handles calls to the GetEncryptingKey Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface GetEncryptingKeyHandler {
    /**
     * Handles a call to the GetEncryptingKey Method.
     *
     * @throws UaException if the call fails.
     */
    KeyCredentialConfigurationTypeGetEncryptingKey.Outputs getEncryptingKey(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String credentialId,
        @Nullable String requestedSecurityPolicyUri)
        throws UaException;
  }

  /**
   * Handles calls to the UpdateCredential Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.7">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface UpdateCredentialHandler {
    /**
     * Handles a call to the UpdateCredential Method.
     *
     * @throws UaException if the call fails.
     */
    void updateCredential(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String credentialId,
        @Nullable ByteString credentialSecret,
        @Nullable String certificateThumbprint,
        @Nullable String securityPolicyUri)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the DeleteCredential Method; see {@link
     * DeleteCredentialHandler#deleteCredential}.
     */
    default void deleteCredential(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the GetEncryptingKey Method; see {@link
     * GetEncryptingKeyHandler#getEncryptingKey}.
     */
    default KeyCredentialConfigurationTypeGetEncryptingKey.Outputs getEncryptingKey(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String credentialId,
        @Nullable String requestedSecurityPolicyUri)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the UpdateCredential Method; see {@link
     * UpdateCredentialHandler#updateCredential}.
     */
    default void updateCredential(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String credentialId,
        @Nullable ByteString credentialSecret,
        @Nullable String certificateThumbprint,
        @Nullable String securityPolicyUri)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
