package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/** Server API for the ProgramTransitionAuditEventType ObjectType. */
public interface ProgramTransitionAuditEventType extends AuditUpdateStateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 3806L);

  /**
   * Returns the mandatory Transition child, a FiniteTransitionVariableType with DataType
   * LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.7">FiniteTransitionVariableType
   *     documentation</a>
   */
  FiniteTransitionVariableTypeNode getTransitionNode();

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
