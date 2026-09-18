package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SessionDiagnosticsObjectType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5">Model
 *     documentation</a>
 */
public interface SessionDiagnosticsObjectType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2029L);

  QualifiedProperty<NodeId[]> CurrentRoleIds_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CurrentRoleIds",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          1,
          NodeId[].class);

  /**
   * Resolves the optional CurrentRoleIds child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getCurrentRoleIdsNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentRoleIdsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getCurrentRoleIdsNodeAsync();

  /**
   * Reads the Value of the CurrentRoleIds child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readCurrentRoleIds() throws UaException;

  /**
   * Writes the Value of the CurrentRoleIds child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentRoleIds(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readCurrentRoleIds()}. */
  CompletableFuture<? extends NodeId @Nullable []> readCurrentRoleIdsAsync();

  /** Asynchronous form of {@link #writeCurrentRoleIds}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCurrentRoleIdsAsync(NodeId @Nullable [] value);

  /**
   * Resolves the mandatory SessionDiagnostics child, a SessionDiagnosticsVariableType with DataType
   * SessionDiagnosticsDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14">SessionDiagnosticsVariableType
   *     documentation</a>
   */
  SessionDiagnosticsVariableType getSessionDiagnosticsNode() throws UaException;

  /** Asynchronous form of {@link #getSessionDiagnosticsNode()}. */
  CompletableFuture<? extends SessionDiagnosticsVariableType> getSessionDiagnosticsNodeAsync();

  /**
   * Reads the Value of the SessionDiagnostics child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SessionDiagnosticsDataType readSessionDiagnostics() throws UaException;

  /**
   * Writes the Value of the SessionDiagnostics child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSessionDiagnostics(@Nullable SessionDiagnosticsDataType value) throws UaException;

  /** Asynchronous form of {@link #readSessionDiagnostics()}. */
  CompletableFuture<? extends @Nullable SessionDiagnosticsDataType> readSessionDiagnosticsAsync();

  /** Asynchronous form of {@link #writeSessionDiagnostics}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSessionDiagnosticsAsync(
      @Nullable SessionDiagnosticsDataType value);

  /**
   * Resolves the mandatory SessionSecurityDiagnostics child, a SessionSecurityDiagnosticsType with
   * DataType SessionSecurityDiagnosticsDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16">SessionSecurityDiagnosticsType
   *     documentation</a>
   */
  SessionSecurityDiagnosticsType getSessionSecurityDiagnosticsNode() throws UaException;

  /** Asynchronous form of {@link #getSessionSecurityDiagnosticsNode()}. */
  CompletableFuture<? extends SessionSecurityDiagnosticsType>
      getSessionSecurityDiagnosticsNodeAsync();

  /**
   * Reads the Value of the SessionSecurityDiagnostics child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SessionSecurityDiagnosticsDataType readSessionSecurityDiagnostics() throws UaException;

  /**
   * Writes the Value of the SessionSecurityDiagnostics child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value)
      throws UaException;

  /** Asynchronous form of {@link #readSessionSecurityDiagnostics()}. */
  CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType>
      readSessionSecurityDiagnosticsAsync();

  /**
   * Asynchronous form of {@link #writeSessionSecurityDiagnostics}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsAsync(
      @Nullable SessionSecurityDiagnosticsDataType value);

  /**
   * Resolves the mandatory SubscriptionDiagnosticsArray child, a SubscriptionDiagnosticsArrayType
   * with DataType SubscriptionDiagnosticsDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11">SubscriptionDiagnosticsArrayType
   *     documentation</a>
   */
  SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArrayNode() throws UaException;

  /** Asynchronous form of {@link #getSubscriptionDiagnosticsArrayNode()}. */
  CompletableFuture<? extends SubscriptionDiagnosticsArrayType>
      getSubscriptionDiagnosticsArrayNodeAsync();

  /**
   * Reads the Value of the SubscriptionDiagnosticsArray child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SubscriptionDiagnosticsDataType @Nullable [] readSubscriptionDiagnosticsArray()
      throws UaException;

  /**
   * Writes the Value of the SubscriptionDiagnosticsArray child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSubscriptionDiagnosticsArray()}. */
  CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType @Nullable []>
      readSubscriptionDiagnosticsArrayAsync();

  /**
   * Asynchronous form of {@link #writeSubscriptionDiagnosticsArray}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value);
}
