package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SubscriptionDiagnosticsArrayType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11">Model
 *     documentation</a>
 */
public interface SubscriptionDiagnosticsArrayType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2171L);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SubscriptionDiagnosticsDataType @Nullable [] readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable SubscriptionDiagnosticsDataType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType @Nullable []>
      readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value);
}
