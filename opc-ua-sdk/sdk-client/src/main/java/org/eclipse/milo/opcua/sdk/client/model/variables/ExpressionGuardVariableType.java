package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ExpressionGuardVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.6.5">Model
 *     documentation</a>
 */
public interface ExpressionGuardVariableType extends GuardVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15128L);

  QualifiedProperty<ContentFilter> Expression_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Expression",
          ExpandedNodeId.of(Namespaces.OPC_UA, 586L),
          -1,
          ContentFilter.class);

  /**
   * Resolves the mandatory Expression child, a PropertyType with DataType ContentFilter.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getExpressionNode() throws UaException;

  /** Asynchronous form of {@link #getExpressionNode()}. */
  CompletableFuture<? extends PropertyType> getExpressionNodeAsync();

  /**
   * Reads the Value of the Expression child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ContentFilter readExpression() throws UaException;

  /**
   * Writes the Value of the Expression child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeExpression(@Nullable ContentFilter value) throws UaException;

  /** Asynchronous form of {@link #readExpression()}. */
  CompletableFuture<? extends @Nullable ContentFilter> readExpressionAsync();

  /** Asynchronous form of {@link #writeExpression}; completes with the operation status. */
  CompletableFuture<StatusCode> writeExpressionAsync(@Nullable ContentFilter value);
}
