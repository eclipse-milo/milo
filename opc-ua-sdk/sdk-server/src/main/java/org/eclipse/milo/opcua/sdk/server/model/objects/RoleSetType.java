package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the RoleSetType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.1">Model
 *     documentation</a>
 */
public interface RoleSetType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15607L);

  /**
   * Returns the mandatory AddRole Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2">Model
   *     documentation</a>
   */
  UaMethodNode getAddRoleMethodNode();

  /**
   * Sets this instance's AddRole handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddRoleHandler(@Nullable AddRoleHandler handler);

  /**
   * Returns the mandatory RemoveRole Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3">Model
   *     documentation</a>
   */
  UaMethodNode getRemoveRoleMethodNode();

  /**
   * Sets this instance's RemoveRole handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveRoleHandler(@Nullable RemoveRoleHandler handler);

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
   * Handles calls to the AddRole Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddRoleHandler {
    /**
     * Handles a call to the AddRole Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addRole(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String roleName,
        @Nullable String namespaceUri)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveRole Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveRoleHandler {
    /**
     * Handles a call to the RemoveRole Method.
     *
     * @throws UaException if the call fails.
     */
    void removeRole(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId roleNodeId)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /** Handles a call to the AddRole Method; see {@link AddRoleHandler#addRole}. */
    default @Nullable NodeId addRole(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String roleName,
        @Nullable String namespaceUri)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the RemoveRole Method; see {@link RemoveRoleHandler#removeRole}. */
    default void removeRole(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId roleNodeId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
