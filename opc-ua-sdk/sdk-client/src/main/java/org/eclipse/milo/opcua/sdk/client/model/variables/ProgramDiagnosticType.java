package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnosticDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusResult;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/** Client API for the ProgramDiagnosticType VariableType. */
public interface ProgramDiagnosticType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2380L);

  QualifiedProperty<String> LastMethodCall_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastMethodCall",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<NodeId> CreateSessionId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CreateSessionId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<String> CreateClientName_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CreateClientName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<DateTime> LastMethodCallTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastMethodCallTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> LastTransitionTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastTransitionTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<NodeId> LastMethodSessionId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastMethodSessionId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<DateTime> InvocationCreationTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "InvocationCreationTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<StatusResult> LastMethodReturnStatus_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastMethodReturnStatus",
          ExpandedNodeId.of(Namespaces.OPC_UA, 299L),
          -1,
          StatusResult.class);

  QualifiedProperty<Variant[]> LastMethodInputArguments_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastMethodInputArguments",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          1,
          Variant[].class);

  QualifiedProperty<Variant[]> LastMethodOutputArguments_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastMethodOutputArguments",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          1,
          Variant[].class);

  /**
   * Resolves the mandatory LastMethodCall child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastMethodCallNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodCallNode()}. */
  CompletableFuture<? extends PropertyType> getLastMethodCallNodeAsync();

  /**
   * Reads the Value of the LastMethodCall child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readLastMethodCall() throws UaException;

  /**
   * Writes the Value of the LastMethodCall child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastMethodCall(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readLastMethodCall()}. */
  CompletableFuture<? extends @Nullable String> readLastMethodCallAsync();

  /** Asynchronous form of {@link #writeLastMethodCall}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastMethodCallAsync(@Nullable String value);

  /**
   * Resolves the mandatory CreateSessionId child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCreateSessionIdNode() throws UaException;

  /** Asynchronous form of {@link #getCreateSessionIdNode()}. */
  CompletableFuture<? extends PropertyType> getCreateSessionIdNodeAsync();

  /**
   * Reads the Value of the CreateSessionId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readCreateSessionId() throws UaException;

  /**
   * Writes the Value of the CreateSessionId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCreateSessionId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readCreateSessionId()}. */
  CompletableFuture<? extends @Nullable NodeId> readCreateSessionIdAsync();

  /** Asynchronous form of {@link #writeCreateSessionId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCreateSessionIdAsync(@Nullable NodeId value);

  /**
   * Resolves the mandatory CreateClientName child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCreateClientNameNode() throws UaException;

  /** Asynchronous form of {@link #getCreateClientNameNode()}. */
  CompletableFuture<? extends PropertyType> getCreateClientNameNodeAsync();

  /**
   * Reads the Value of the CreateClientName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readCreateClientName() throws UaException;

  /**
   * Writes the Value of the CreateClientName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCreateClientName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readCreateClientName()}. */
  CompletableFuture<? extends @Nullable String> readCreateClientNameAsync();

  /** Asynchronous form of {@link #writeCreateClientName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCreateClientNameAsync(@Nullable String value);

  /**
   * Resolves the mandatory LastMethodCallTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastMethodCallTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodCallTimeNode()}. */
  CompletableFuture<? extends PropertyType> getLastMethodCallTimeNodeAsync();

  /**
   * Reads the Value of the LastMethodCallTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readLastMethodCallTime() throws UaException;

  /**
   * Writes the Value of the LastMethodCallTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastMethodCallTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readLastMethodCallTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readLastMethodCallTimeAsync();

  /** Asynchronous form of {@link #writeLastMethodCallTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastMethodCallTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory LastTransitionTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastTransitionTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLastTransitionTimeNode()}. */
  CompletableFuture<? extends PropertyType> getLastTransitionTimeNodeAsync();

  /**
   * Reads the Value of the LastTransitionTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readLastTransitionTime() throws UaException;

  /**
   * Writes the Value of the LastTransitionTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastTransitionTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readLastTransitionTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readLastTransitionTimeAsync();

  /** Asynchronous form of {@link #writeLastTransitionTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastTransitionTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory LastMethodSessionId child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastMethodSessionIdNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodSessionIdNode()}. */
  CompletableFuture<? extends PropertyType> getLastMethodSessionIdNodeAsync();

  /**
   * Reads the Value of the LastMethodSessionId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readLastMethodSessionId() throws UaException;

  /**
   * Writes the Value of the LastMethodSessionId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastMethodSessionId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readLastMethodSessionId()}. */
  CompletableFuture<? extends @Nullable NodeId> readLastMethodSessionIdAsync();

  /**
   * Asynchronous form of {@link #writeLastMethodSessionId}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeLastMethodSessionIdAsync(@Nullable NodeId value);

  /**
   * Resolves the mandatory InvocationCreationTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getInvocationCreationTimeNode() throws UaException;

  /** Asynchronous form of {@link #getInvocationCreationTimeNode()}. */
  CompletableFuture<? extends PropertyType> getInvocationCreationTimeNodeAsync();

  /**
   * Reads the Value of the InvocationCreationTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readInvocationCreationTime() throws UaException;

  /**
   * Writes the Value of the InvocationCreationTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInvocationCreationTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readInvocationCreationTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readInvocationCreationTimeAsync();

  /**
   * Asynchronous form of {@link #writeInvocationCreationTime}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeInvocationCreationTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory LastMethodReturnStatus child, a PropertyType with DataType StatusResult.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastMethodReturnStatusNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodReturnStatusNode()}. */
  CompletableFuture<? extends PropertyType> getLastMethodReturnStatusNodeAsync();

  /**
   * Reads the Value of the LastMethodReturnStatus child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable StatusResult readLastMethodReturnStatus() throws UaException;

  /**
   * Writes the Value of the LastMethodReturnStatus child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastMethodReturnStatus(@Nullable StatusResult value) throws UaException;

  /** Asynchronous form of {@link #readLastMethodReturnStatus()}. */
  CompletableFuture<? extends @Nullable StatusResult> readLastMethodReturnStatusAsync();

  /**
   * Asynchronous form of {@link #writeLastMethodReturnStatus}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeLastMethodReturnStatusAsync(@Nullable StatusResult value);

  /**
   * Resolves the mandatory LastMethodInputArguments child, a PropertyType with DataType
   * BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastMethodInputArgumentsNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodInputArgumentsNode()}. */
  CompletableFuture<? extends PropertyType> getLastMethodInputArgumentsNodeAsync();

  /**
   * Reads the Value of the LastMethodInputArguments child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant @Nullable [] readLastMethodInputArguments() throws UaException;

  /**
   * Writes the Value of the LastMethodInputArguments child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastMethodInputArguments(@Nullable Variant @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readLastMethodInputArguments()}. */
  CompletableFuture<? extends @Nullable Variant @Nullable []> readLastMethodInputArgumentsAsync();

  /**
   * Asynchronous form of {@link #writeLastMethodInputArguments}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeLastMethodInputArgumentsAsync(
      @Nullable Variant @Nullable [] value);

  /**
   * Resolves the mandatory LastMethodOutputArguments child, a PropertyType with DataType
   * BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastMethodOutputArgumentsNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodOutputArgumentsNode()}. */
  CompletableFuture<? extends PropertyType> getLastMethodOutputArgumentsNodeAsync();

  /**
   * Reads the Value of the LastMethodOutputArguments child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant @Nullable [] readLastMethodOutputArguments() throws UaException;

  /**
   * Writes the Value of the LastMethodOutputArguments child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastMethodOutputArguments(@Nullable Variant @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readLastMethodOutputArguments()}. */
  CompletableFuture<? extends @Nullable Variant @Nullable []> readLastMethodOutputArgumentsAsync();

  /**
   * Asynchronous form of {@link #writeLastMethodOutputArguments}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeLastMethodOutputArgumentsAsync(
      @Nullable Variant @Nullable [] value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ProgramDiagnosticDataType readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable ProgramDiagnosticDataType value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable ProgramDiagnosticDataType> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable ProgramDiagnosticDataType value);
}
