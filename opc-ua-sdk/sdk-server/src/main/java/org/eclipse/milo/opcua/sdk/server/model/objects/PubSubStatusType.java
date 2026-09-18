package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubStatusType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">Model
 *     documentation</a>
 */
public interface PubSubStatusType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14643L);

  /**
   * Returns the mandatory State child, a BaseDataVariableType with DataType PubSubState.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getStateNode();

  /**
   * Returns the Value of the State child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable PubSubState getState();

  /**
   * Sets the Value of the State child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setState(@Nullable PubSubState value);

  /**
   * Returns the optional Disable Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getDisableMethodNode();

  /**
   * Sets this instance's Disable handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setDisableHandler(@Nullable DisableHandler handler);

  /**
   * Returns the optional Enable Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getEnableMethodNode();

  /**
   * Sets this instance's Enable handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setEnableHandler(@Nullable EnableHandler handler);

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
   * Handles calls to the Disable Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface DisableHandler {
    /**
     * Handles a call to the Disable Method.
     *
     * @throws UaException if the call fails.
     */
    void disable(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /**
   * Handles calls to the Enable Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface EnableHandler {
    /**
     * Handles a call to the Enable Method.
     *
     * @throws UaException if the call fails.
     */
    void enable(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /** Handles a call to the Disable Method; see {@link DisableHandler#disable}. */
    default void disable(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Enable Method; see {@link EnableHandler#enable}. */
    default void enable(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
