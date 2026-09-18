package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundantServerMode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the NonTransparentBackupRedundancyType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.15">Model
 *     documentation</a>
 */
public interface NonTransparentBackupRedundancyType extends NonTransparentRedundancyType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32411L);

  /**
   * Returns the mandatory Mode child, a PropertyType with DataType RedundantServerMode.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getModeNode();

  /**
   * Returns the Value of the Mode child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable RedundantServerMode getMode();

  /**
   * Sets the Value of the Mode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMode(@Nullable RedundantServerMode value);

  /**
   * Returns the mandatory RedundantServerArray child, a PropertyType with DataType
   * RedundantServerDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getRedundantServerArrayNode();

  /**
   * Returns the mandatory Failover Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   */
  UaMethodNode getFailoverMethodNode();

  /**
   * Sets this instance's Failover handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setFailoverHandler(@Nullable FailoverHandler handler);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /** Handles calls to the Failover Method. */
  @FunctionalInterface
  interface FailoverHandler {
    /**
     * Handles a call to the Failover Method.
     *
     * @throws UaException if the call fails.
     */
    void failover(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /** Handles a call to the Failover Method; see {@link FailoverHandler#failover}. */
    default void failover(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
