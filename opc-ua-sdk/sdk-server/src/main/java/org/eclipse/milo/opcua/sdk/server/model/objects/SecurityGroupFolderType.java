package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.SecurityGroupFolderTypeAddSecurityGroup;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SecurityGroupFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.1">Model
 *     documentation</a>
 */
public interface SecurityGroupFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15452L);

  /**
   * Returns the optional SupportedSecurityPolicyUris child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSupportedSecurityPolicyUrisNode();

  /**
   * Returns the Value of the SupportedSecurityPolicyUris child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getSupportedSecurityPolicyUris();

  /**
   * Sets the Value of the SupportedSecurityPolicyUris child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSupportedSecurityPolicyUris(@Nullable String @Nullable [] value);

  /**
   * Returns the mandatory AddSecurityGroup Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2">Model
   *     documentation</a>
   */
  UaMethodNode getAddSecurityGroupMethodNode();

  /**
   * Sets this instance's AddSecurityGroup handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddSecurityGroupHandler(@Nullable AddSecurityGroupHandler handler);

  /**
   * Returns the optional AddSecurityGroupFolder Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddSecurityGroupFolderMethodNode();

  /**
   * Sets this instance's AddSecurityGroupFolder handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddSecurityGroupFolderHandler(@Nullable AddSecurityGroupFolderHandler handler);

  /**
   * Returns the mandatory RemoveSecurityGroup Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3">Model
   *     documentation</a>
   */
  UaMethodNode getRemoveSecurityGroupMethodNode();

  /**
   * Sets this instance's RemoveSecurityGroup handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveSecurityGroupHandler(@Nullable RemoveSecurityGroupHandler handler);

  /**
   * Returns the optional RemoveSecurityGroupFolder Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveSecurityGroupFolderMethodNode();

  /**
   * Sets this instance's RemoveSecurityGroupFolder handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveSecurityGroupFolderHandler(@Nullable RemoveSecurityGroupFolderHandler handler);

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
   * Handles calls to the AddSecurityGroup Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddSecurityGroupHandler {
    /**
     * Handles a call to the AddSecurityGroup Method.
     *
     * @throws UaException if the call fails.
     */
    SecurityGroupFolderTypeAddSecurityGroup.Outputs addSecurityGroup(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String securityGroupName,
        @Nullable Double keyLifetime,
        @Nullable String securityPolicyUri,
        @Nullable UInteger maxFutureKeyCount,
        @Nullable UInteger maxPastKeyCount)
        throws UaException;
  }

  /**
   * Handles calls to the AddSecurityGroupFolder Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddSecurityGroupFolderHandler {
    /**
     * Handles a call to the AddSecurityGroupFolder Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addSecurityGroupFolder(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String name)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveSecurityGroup Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveSecurityGroupHandler {
    /**
     * Handles a call to the RemoveSecurityGroup Method.
     *
     * @throws UaException if the call fails.
     */
    void removeSecurityGroup(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId securityGroupNodeId)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveSecurityGroupFolder Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveSecurityGroupFolderHandler {
    /**
     * Handles a call to the RemoveSecurityGroupFolder Method.
     *
     * @throws UaException if the call fails.
     */
    void removeSecurityGroupFolder(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId securityGroupFolderNodeId)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddSecurityGroup Method; see {@link
     * AddSecurityGroupHandler#addSecurityGroup}.
     */
    default SecurityGroupFolderTypeAddSecurityGroup.Outputs addSecurityGroup(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String securityGroupName,
        @Nullable Double keyLifetime,
        @Nullable String securityPolicyUri,
        @Nullable UInteger maxFutureKeyCount,
        @Nullable UInteger maxPastKeyCount)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the AddSecurityGroupFolder Method; see {@link
     * AddSecurityGroupFolderHandler#addSecurityGroupFolder}.
     */
    default @Nullable NodeId addSecurityGroupFolder(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String name)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveSecurityGroup Method; see {@link
     * RemoveSecurityGroupHandler#removeSecurityGroup}.
     */
    default void removeSecurityGroup(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId securityGroupNodeId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveSecurityGroupFolder Method; see {@link
     * RemoveSecurityGroupFolderHandler#removeSecurityGroupFolder}.
     */
    default void removeSecurityGroupFolder(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId securityGroupFolderNodeId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
