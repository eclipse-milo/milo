package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AcknowledgeableConditionType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2">Model
 *     documentation</a>
 */
public interface AcknowledgeableConditionType extends ConditionType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2881L);

  /**
   * Returns the mandatory AckedState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableTypeNode getAckedStateNode();

  /**
   * Returns the Value of the AckedState child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getAckedState();

  /**
   * Sets the Value of the AckedState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAckedState(@Nullable LocalizedText value);

  /**
   * Returns the optional ConfirmedState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableTypeNode getConfirmedStateNode();

  /**
   * Returns the Value of the ConfirmedState child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getConfirmedState();

  /**
   * Sets the Value of the ConfirmedState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConfirmedState(@Nullable LocalizedText value);

  /**
   * Returns the mandatory Acknowledge Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3">Model
   *     documentation</a>
   */
  UaMethodNode getAcknowledgeMethodNode();

  /**
   * Sets this instance's Acknowledge handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAcknowledgeHandler(@Nullable AcknowledgeHandler handler);

  /**
   * Returns the optional Confirm Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getConfirmMethodNode();

  /**
   * Sets this instance's Confirm handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setConfirmHandler(@Nullable ConfirmHandler handler);

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
   * Handles calls to the Acknowledge Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AcknowledgeHandler {
    /**
     * Handles a call to the Acknowledge Method.
     *
     * @param eventId the identifier for the event to comment.
     * @param comment the comment to add to the condition.
     * @throws UaException if the call fails.
     */
    void acknowledge(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /**
   * Handles calls to the Confirm Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ConfirmHandler {
    /**
     * Handles a call to the Confirm Method.
     *
     * @param eventId the identifier for the event to comment.
     * @param comment the comment to add to the condition.
     * @throws UaException if the call fails.
     */
    void confirm(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods extends ConditionType.Methods {
    /** Handles a call to the Acknowledge Method; see {@link AcknowledgeHandler#acknowledge}. */
    default void acknowledge(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Confirm Method; see {@link ConfirmHandler#confirm}. */
    default void confirm(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
