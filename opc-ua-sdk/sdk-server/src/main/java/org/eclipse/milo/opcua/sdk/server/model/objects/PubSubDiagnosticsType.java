package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PubSubDiagnosticsCounterTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubDiagnosticsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2">Model
 *     documentation</a>
 */
public interface PubSubDiagnosticsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19677L);

  /**
   * Returns the mandatory Counters child, a BaseObjectType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.2">BaseObjectType
   *     documentation</a>
   */
  BaseObjectTypeNode getCountersNode();

  /**
   * Returns the mandatory DiagnosticsLevel child, a BaseDataVariableType with DataType
   * DiagnosticsLevel.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDiagnosticsLevelNode();

  /**
   * Returns the Value of the DiagnosticsLevel child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DiagnosticsLevel getDiagnosticsLevel();

  /**
   * Sets the Value of the DiagnosticsLevel child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDiagnosticsLevel(@Nullable DiagnosticsLevel value);

  /**
   * Returns the mandatory LiveValues child, a BaseObjectType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.2">BaseObjectType
   *     documentation</a>
   */
  BaseObjectTypeNode getLiveValuesNode();

  /**
   * Returns the mandatory SubError child, a BaseDataVariableType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSubErrorNode();

  /**
   * Returns the Value of the SubError child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getSubError();

  /**
   * Sets the Value of the SubError child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSubError(@Nullable Boolean value);

  /**
   * Returns the mandatory TotalError child, a PubSubDiagnosticsCounterType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5">PubSubDiagnosticsCounterType
   *     documentation</a>
   */
  PubSubDiagnosticsCounterTypeNode getTotalErrorNode();

  /**
   * Returns the Value of the TotalError child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getTotalError();

  /**
   * Sets the Value of the TotalError child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTotalError(@Nullable UInteger value);

  /**
   * Returns the mandatory TotalInformation child, a PubSubDiagnosticsCounterType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5">PubSubDiagnosticsCounterType
   *     documentation</a>
   */
  PubSubDiagnosticsCounterTypeNode getTotalInformationNode();

  /**
   * Returns the Value of the TotalInformation child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getTotalInformation();

  /**
   * Sets the Value of the TotalInformation child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTotalInformation(@Nullable UInteger value);

  /**
   * Returns the mandatory Reset Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3">Model
   *     documentation</a>
   */
  UaMethodNode getResetMethodNode();

  /**
   * Sets this instance's Reset handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setResetHandler(@Nullable ResetHandler handler);

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
   * Handles calls to the Reset Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ResetHandler {
    /**
     * Handles a call to the Reset Method.
     *
     * @throws UaException if the call fails.
     */
    void reset(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /** Handles a call to the Reset Method; see {@link ResetHandler#reset}. */
    default void reset(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
