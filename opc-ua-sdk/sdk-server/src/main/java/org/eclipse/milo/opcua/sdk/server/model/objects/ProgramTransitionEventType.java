package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ProgramTransitionEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.5/#5.2.5.2">Model
 *     documentation</a>
 */
public interface ProgramTransitionEventType extends TransitionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2378L);

  /**
   * Returns the mandatory IntermediateResult child, a BaseDataVariableType with DataType
   * BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getIntermediateResultNode();

  /**
   * Returns the Value of the IntermediateResult child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant getIntermediateResult();

  /**
   * Sets the Value of the IntermediateResult child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIntermediateResult(@Nullable Variant value);
}
