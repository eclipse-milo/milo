package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.StateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TransitionVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the StateMachineType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.2">Model
 *     documentation</a>
 */
public interface StateMachineType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2299L);

  /**
   * Returns the mandatory CurrentState child, a StateVariableType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3">StateVariableType
   *     documentation</a>
   */
  StateVariableTypeNode getCurrentStateNode();

  /**
   * Returns the Value of the CurrentState child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getCurrentState();

  /**
   * Sets the Value of the CurrentState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentState(@Nullable LocalizedText value);

  /**
   * Returns the optional LastTransition child, a TransitionVariableType with DataType
   * LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.4">TransitionVariableType
   *     documentation</a>
   */
  @Nullable TransitionVariableTypeNode getLastTransitionNode();

  /**
   * Returns the Value of the LastTransition child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getLastTransition();

  /**
   * Sets the Value of the LastTransition child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastTransition(@Nullable LocalizedText value);
}
