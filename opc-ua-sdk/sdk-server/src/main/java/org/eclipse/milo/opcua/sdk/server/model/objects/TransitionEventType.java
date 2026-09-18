package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.StateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TransitionVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the TransitionEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.16">Model
 *     documentation</a>
 */
public interface TransitionEventType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2311L);

  /**
   * Returns the mandatory FromState child, a StateVariableType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3">StateVariableType
   *     documentation</a>
   */
  StateVariableTypeNode getFromStateNode();

  /**
   * Returns the Value of the FromState child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getFromState();

  /**
   * Sets the Value of the FromState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setFromState(@Nullable LocalizedText value);

  /**
   * Returns the mandatory ToState child, a StateVariableType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3">StateVariableType
   *     documentation</a>
   */
  StateVariableTypeNode getToStateNode();

  /**
   * Returns the Value of the ToState child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getToState();

  /**
   * Sets the Value of the ToState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setToState(@Nullable LocalizedText value);

  /**
   * Returns the mandatory Transition child, a TransitionVariableType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.4">TransitionVariableType
   *     documentation</a>
   */
  TransitionVariableTypeNode getTransitionNode();

  /**
   * Returns the Value of the Transition child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getTransition();

  /**
   * Sets the Value of the Transition child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTransition(@Nullable LocalizedText value);
}
