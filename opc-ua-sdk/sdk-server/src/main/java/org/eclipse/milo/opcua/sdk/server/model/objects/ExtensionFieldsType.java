package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ExtensionFieldsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.2">Model
 *     documentation</a>
 */
public interface ExtensionFieldsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15489L);

  /**
   * Returns the mandatory AddExtensionField Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.3">Model
   *     documentation</a>
   */
  UaMethodNode getAddExtensionFieldMethodNode();

  /**
   * Sets this instance's AddExtensionField handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddExtensionFieldHandler(@Nullable AddExtensionFieldHandler handler);

  /**
   * Returns the mandatory RemoveExtensionField Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.4">Model
   *     documentation</a>
   */
  UaMethodNode getRemoveExtensionFieldMethodNode();

  /**
   * Sets this instance's RemoveExtensionField handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveExtensionFieldHandler(@Nullable RemoveExtensionFieldHandler handler);

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
   * Handles calls to the AddExtensionField Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddExtensionFieldHandler {
    /**
     * Handles a call to the AddExtensionField Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addExtensionField(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable QualifiedName fieldName,
        @Nullable Variant fieldValue)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveExtensionField Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveExtensionFieldHandler {
    /**
     * Handles a call to the RemoveExtensionField Method.
     *
     * @throws UaException if the call fails.
     */
    void removeExtensionField(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId fieldId)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddExtensionField Method; see {@link
     * AddExtensionFieldHandler#addExtensionField}.
     */
    default @Nullable NodeId addExtensionField(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable QualifiedName fieldName,
        @Nullable Variant fieldValue)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveExtensionField Method; see {@link
     * RemoveExtensionFieldHandler#removeExtensionField}.
     */
    default void removeExtensionField(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId fieldId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
