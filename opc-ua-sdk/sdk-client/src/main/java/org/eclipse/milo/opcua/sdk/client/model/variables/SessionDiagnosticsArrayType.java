package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SessionDiagnosticsArrayType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.13">Model
 *     documentation</a>
 */
public interface SessionDiagnosticsArrayType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2196L);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SessionDiagnosticsDataType @Nullable [] readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable SessionDiagnosticsDataType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable SessionDiagnosticsDataType @Nullable []>
      readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable SessionDiagnosticsDataType @Nullable [] value);
}
