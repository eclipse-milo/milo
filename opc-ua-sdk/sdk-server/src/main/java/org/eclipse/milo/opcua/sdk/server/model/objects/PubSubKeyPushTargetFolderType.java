package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubKeyPushTargetFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1">Model
 *     documentation</a>
 */
public interface PubSubKeyPushTargetFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 25346L);

  /**
   * Returns the mandatory AddPushTarget Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2">Model
   *     documentation</a>
   */
  UaMethodNode getAddPushTargetMethodNode();

  /**
   * Sets this instance's AddPushTarget handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddPushTargetHandler(@Nullable AddPushTargetHandler handler);

  /**
   * Returns the optional AddPushTargetFolder Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPushTargetFolderMethodNode();

  /**
   * Sets this instance's AddPushTargetFolder handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddPushTargetFolderHandler(@Nullable AddPushTargetFolderHandler handler);

  /**
   * Returns the mandatory RemovePushTarget Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3">Model
   *     documentation</a>
   */
  UaMethodNode getRemovePushTargetMethodNode();

  /**
   * Sets this instance's RemovePushTarget handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemovePushTargetHandler(@Nullable RemovePushTargetHandler handler);

  /**
   * Returns the optional RemovePushTargetFolder Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemovePushTargetFolderMethodNode();

  /**
   * Sets this instance's RemovePushTargetFolder handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemovePushTargetFolderHandler(@Nullable RemovePushTargetFolderHandler handler);

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
   * Handles calls to the AddPushTarget Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddPushTargetHandler {
    /**
     * Handles a call to the AddPushTarget Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addPushTarget(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String applicationUri,
        @Nullable String endpointUrl,
        @Nullable String securityPolicyUri,
        @Nullable UserTokenPolicy userTokenType,
        @Nullable UShort requestedKeyCount,
        @Nullable Double retryInterval)
        throws UaException;
  }

  /**
   * Handles calls to the AddPushTargetFolder Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddPushTargetFolderHandler {
    /**
     * Handles a call to the AddPushTargetFolder Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addPushTargetFolder(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String name)
        throws UaException;
  }

  /**
   * Handles calls to the RemovePushTarget Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemovePushTargetHandler {
    /**
     * Handles a call to the RemovePushTarget Method.
     *
     * @throws UaException if the call fails.
     */
    void removePushTarget(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId pushTargetId)
        throws UaException;
  }

  /**
   * Handles calls to the RemovePushTargetFolder Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemovePushTargetFolderHandler {
    /**
     * Handles a call to the RemovePushTargetFolder Method.
     *
     * @throws UaException if the call fails.
     */
    void removePushTargetFolder(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId pushTargetFolderNodeId)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddPushTarget Method; see {@link AddPushTargetHandler#addPushTarget}.
     */
    default @Nullable NodeId addPushTarget(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String applicationUri,
        @Nullable String endpointUrl,
        @Nullable String securityPolicyUri,
        @Nullable UserTokenPolicy userTokenType,
        @Nullable UShort requestedKeyCount,
        @Nullable Double retryInterval)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the AddPushTargetFolder Method; see {@link
     * AddPushTargetFolderHandler#addPushTargetFolder}.
     */
    default @Nullable NodeId addPushTargetFolder(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String name)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemovePushTarget Method; see {@link
     * RemovePushTargetHandler#removePushTarget}.
     */
    default void removePushTarget(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId pushTargetId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemovePushTargetFolder Method; see {@link
     * RemovePushTargetFolderHandler#removePushTargetFolder}.
     */
    default void removePushTargetFolder(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId pushTargetFolderNodeId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
