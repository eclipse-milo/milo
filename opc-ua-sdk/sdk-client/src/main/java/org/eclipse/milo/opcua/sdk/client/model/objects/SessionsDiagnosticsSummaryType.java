package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsArrayType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SessionsDiagnosticsSummaryType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4">Model
 *     documentation</a>
 */
public interface SessionsDiagnosticsSummaryType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2026L);

  /**
   * Resolves the mandatory SessionDiagnosticsArray child, a SessionDiagnosticsArrayType with
   * DataType SessionDiagnosticsDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.13">SessionDiagnosticsArrayType
   *     documentation</a>
   */
  SessionDiagnosticsArrayType getSessionDiagnosticsArrayNode() throws UaException;

  /** Asynchronous form of {@link #getSessionDiagnosticsArrayNode()}. */
  CompletableFuture<? extends SessionDiagnosticsArrayType> getSessionDiagnosticsArrayNodeAsync();

  /**
   * Reads the Value of the SessionDiagnosticsArray child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SessionDiagnosticsDataType @Nullable [] readSessionDiagnosticsArray()
      throws UaException;

  /**
   * Writes the Value of the SessionDiagnosticsArray child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSessionDiagnosticsArray(@Nullable SessionDiagnosticsDataType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readSessionDiagnosticsArray()}. */
  CompletableFuture<? extends @Nullable SessionDiagnosticsDataType @Nullable []>
      readSessionDiagnosticsArrayAsync();

  /**
   * Asynchronous form of {@link #writeSessionDiagnosticsArray}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSessionDiagnosticsArrayAsync(
      @Nullable SessionDiagnosticsDataType @Nullable [] value);

  /**
   * Resolves the mandatory SessionSecurityDiagnosticsArray child, a
   * SessionSecurityDiagnosticsArrayType with DataType SessionSecurityDiagnosticsDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.15">SessionSecurityDiagnosticsArrayType
   *     documentation</a>
   */
  SessionSecurityDiagnosticsArrayType getSessionSecurityDiagnosticsArrayNode() throws UaException;

  /** Asynchronous form of {@link #getSessionSecurityDiagnosticsArrayNode()}. */
  CompletableFuture<? extends SessionSecurityDiagnosticsArrayType>
      getSessionSecurityDiagnosticsArrayNodeAsync();

  /**
   * Reads the Value of the SessionSecurityDiagnosticsArray child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SessionSecurityDiagnosticsDataType @Nullable [] readSessionSecurityDiagnosticsArray()
      throws UaException;

  /**
   * Writes the Value of the SessionSecurityDiagnosticsArray child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSessionSecurityDiagnosticsArray(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSessionSecurityDiagnosticsArray()}. */
  CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType @Nullable []>
      readSessionSecurityDiagnosticsArrayAsync();

  /**
   * Asynchronous form of {@link #writeSessionSecurityDiagnosticsArray}; completes with the
   * operation status.
   */
  CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsArrayAsync(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value);
}
