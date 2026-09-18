package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ShelvedStateMachineType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1">Model
 *     documentation</a>
 */
public interface ShelvedStateMachineType extends FiniteStateMachineType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2929L);

  /**
   * Returns the mandatory UnshelveTime child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUnshelveTimeNode();

  /**
   * Returns the Value of the UnshelveTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getUnshelveTime();

  /**
   * Sets the Value of the UnshelveTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUnshelveTime(@Nullable Double value);

  /**
   * Returns the mandatory OneShotShelve Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6">Model
   *     documentation</a>
   */
  UaMethodNode getOneShotShelveMethodNode();

  /**
   * Sets this instance's OneShotShelve handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setOneShotShelveHandler(@Nullable OneShotShelveHandler handler);

  /**
   * Returns the optional OneShotShelve2 Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getOneShotShelve2MethodNode();

  /**
   * Sets this instance's OneShotShelve2 handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setOneShotShelve2Handler(@Nullable OneShotShelve2Handler handler);

  /**
   * Returns the mandatory TimedShelve Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4">Model
   *     documentation</a>
   */
  UaMethodNode getTimedShelveMethodNode();

  /**
   * Sets this instance's TimedShelve handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setTimedShelveHandler(@Nullable TimedShelveHandler handler);

  /**
   * Returns the optional TimedShelve2 Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getTimedShelve2MethodNode();

  /**
   * Sets this instance's TimedShelve2 handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setTimedShelve2Handler(@Nullable TimedShelve2Handler handler);

  /**
   * Returns the mandatory Unshelve Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2">Model
   *     documentation</a>
   */
  UaMethodNode getUnshelveMethodNode();

  /**
   * Sets this instance's Unshelve handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setUnshelveHandler(@Nullable UnshelveHandler handler);

  /**
   * Returns the optional Unshelve2 Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getUnshelve2MethodNode();

  /**
   * Sets this instance's Unshelve2 handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setUnshelve2Handler(@Nullable Unshelve2Handler handler);

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
   * Handles calls to the OneShotShelve Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface OneShotShelveHandler {
    /**
     * Handles a call to the OneShotShelve Method.
     *
     * @throws UaException if the call fails.
     */
    void oneShotShelve(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException;
  }

  /**
   * Handles calls to the OneShotShelve2 Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface OneShotShelve2Handler {
    /**
     * Handles a call to the OneShotShelve2 Method.
     *
     * @throws UaException if the call fails.
     */
    void oneShotShelve2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException;
  }

  /**
   * Handles calls to the TimedShelve Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface TimedShelveHandler {
    /**
     * Handles a call to the TimedShelve Method.
     *
     * @throws UaException if the call fails.
     */
    void timedShelve(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable Double shelvingTime)
        throws UaException;
  }

  /**
   * Handles calls to the TimedShelve2 Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface TimedShelve2Handler {
    /**
     * Handles a call to the TimedShelve2 Method.
     *
     * @throws UaException if the call fails.
     */
    void timedShelve2(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable Double shelvingTime,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /**
   * Handles calls to the Unshelve Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface UnshelveHandler {
    /**
     * Handles a call to the Unshelve Method.
     *
     * @throws UaException if the call fails.
     */
    void unshelve(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /**
   * Handles calls to the Unshelve2 Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface Unshelve2Handler {
    /**
     * Handles a call to the Unshelve2 Method.
     *
     * @throws UaException if the call fails.
     */
    void unshelve2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the OneShotShelve Method; see {@link OneShotShelveHandler#oneShotShelve}.
     */
    default void oneShotShelve(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the OneShotShelve2 Method; see {@link
     * OneShotShelve2Handler#oneShotShelve2}.
     */
    default void oneShotShelve2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the TimedShelve Method; see {@link TimedShelveHandler#timedShelve}. */
    default void timedShelve(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable Double shelvingTime)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the TimedShelve2 Method; see {@link TimedShelve2Handler#timedShelve2}. */
    default void timedShelve2(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable Double shelvingTime,
        @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Unshelve Method; see {@link UnshelveHandler#unshelve}. */
    default void unshelve(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Unshelve2 Method; see {@link Unshelve2Handler#unshelve2}. */
    default void unshelve2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
