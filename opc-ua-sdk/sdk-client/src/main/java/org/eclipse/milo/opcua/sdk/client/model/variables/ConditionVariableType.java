package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ConditionVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.3">Model
 *     documentation</a>
 */
public interface ConditionVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 9002L);

  QualifiedProperty<DateTime> SourceTimestamp_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SourceTimestamp",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  /**
   * Resolves the mandatory SourceTimestamp child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSourceTimestampNode() throws UaException;

  /** Asynchronous form of {@link #getSourceTimestampNode()}. */
  CompletableFuture<? extends PropertyType> getSourceTimestampNodeAsync();

  /**
   * Reads the Value of the SourceTimestamp child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readSourceTimestamp() throws UaException;

  /**
   * Writes the Value of the SourceTimestamp child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSourceTimestamp(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readSourceTimestamp()}. */
  CompletableFuture<? extends @Nullable DateTime> readSourceTimestampAsync();

  /** Asynchronous form of {@link #writeSourceTimestamp}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSourceTimestampAsync(@Nullable DateTime value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable Variant> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable Variant value);
}
