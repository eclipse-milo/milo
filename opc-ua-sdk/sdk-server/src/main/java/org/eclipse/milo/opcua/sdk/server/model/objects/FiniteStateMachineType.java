package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the FiniteStateMachineType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5">Model
 *     documentation</a>
 */
public interface FiniteStateMachineType extends StateMachineType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2771L);

  /**
   * Returns the optional AvailableStates child, a BaseDataVariableType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getAvailableStatesNode();

  /**
   * Returns the Value of the AvailableStates child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getAvailableStates();

  /**
   * Sets the Value of the AvailableStates child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAvailableStates(NodeId @Nullable [] value);

  /**
   * Returns the optional AvailableTransitions child, a BaseDataVariableType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getAvailableTransitionsNode();

  /**
   * Returns the Value of the AvailableTransitions child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getAvailableTransitions();

  /**
   * Sets the Value of the AvailableTransitions child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAvailableTransitions(NodeId @Nullable [] value);

  /**
   * Returns the mandatory CurrentState child, a FiniteStateVariableType with DataType
   * LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.6">FiniteStateVariableType
   *     documentation</a>
   */
  FiniteStateVariableTypeNode getCurrentStateNode();

  /**
   * Returns the optional LastTransition child, a FiniteTransitionVariableType with DataType
   * LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.7">FiniteTransitionVariableType
   *     documentation</a>
   */
  @Nullable FiniteTransitionVariableTypeNode getLastTransitionNode();
}
