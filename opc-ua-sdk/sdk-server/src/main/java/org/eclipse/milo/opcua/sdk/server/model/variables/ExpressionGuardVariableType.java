package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ExpressionGuardVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.6.5">Model
 *     documentation</a>
 */
public interface ExpressionGuardVariableType extends GuardVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15128L);

  /**
   * Returns the mandatory Expression child, a PropertyType with DataType ContentFilter.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getExpressionNode();

  /**
   * Returns the Value of the Expression child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ContentFilter getExpression();

  /**
   * Sets the Value of the Expression child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setExpression(@Nullable ContentFilter value);
}
