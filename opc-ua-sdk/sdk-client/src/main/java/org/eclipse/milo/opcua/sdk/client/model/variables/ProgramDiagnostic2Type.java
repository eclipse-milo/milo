package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ProgramDiagnostic2Type VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9">Model
 *     documentation</a>
 */
public interface ProgramDiagnostic2Type extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15383L);

  QualifiedProperty<DateTime> LastTransitionTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastTransitionTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  /**
   * Resolves the mandatory LastMethodCall child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getLastMethodCallNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodCallNode()}. */
  CompletableFuture<? extends VariableNode> getLastMethodCallNodeAsync();

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
   * Resolves the mandatory CreateSessionId child, a BaseDataVariableType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCreateSessionIdNode() throws UaException;

  /** Asynchronous form of {@link #getCreateSessionIdNode()}. */
  CompletableFuture<? extends VariableNode> getCreateSessionIdNodeAsync();

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
   * Resolves the mandatory CreateClientName child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCreateClientNameNode() throws UaException;

  /** Asynchronous form of {@link #getCreateClientNameNode()}. */
  CompletableFuture<? extends VariableNode> getCreateClientNameNodeAsync();

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
   * Resolves the mandatory LastMethodCallTime child, a BaseDataVariableType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getLastMethodCallTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodCallTimeNode()}. */
  CompletableFuture<? extends VariableNode> getLastMethodCallTimeNodeAsync();

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
   * Resolves the mandatory LastMethodSessionId child, a BaseDataVariableType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getLastMethodSessionIdNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodSessionIdNode()}. */
  CompletableFuture<? extends VariableNode> getLastMethodSessionIdNodeAsync();

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
   * Resolves the mandatory LastMethodInputValues child, a BaseDataVariableType with DataType
   * BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getLastMethodInputValuesNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodInputValuesNode()}. */
  CompletableFuture<? extends VariableNode> getLastMethodInputValuesNodeAsync();

  /**
   * Reads the Value of the LastMethodInputValues child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant @Nullable [] readLastMethodInputValues() throws UaException;

  /**
   * Writes the Value of the LastMethodInputValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastMethodInputValues(@Nullable Variant @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readLastMethodInputValues()}. */
  CompletableFuture<? extends @Nullable Variant @Nullable []> readLastMethodInputValuesAsync();

  /**
   * Asynchronous form of {@link #writeLastMethodInputValues}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeLastMethodInputValuesAsync(
      @Nullable Variant @Nullable [] value);

  /**
   * Resolves the mandatory InvocationCreationTime child, a BaseDataVariableType with DataType
   * UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getInvocationCreationTimeNode() throws UaException;

  /** Asynchronous form of {@link #getInvocationCreationTimeNode()}. */
  CompletableFuture<? extends VariableNode> getInvocationCreationTimeNodeAsync();

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
   * Resolves the mandatory LastMethodOutputValues child, a BaseDataVariableType with DataType
   * BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getLastMethodOutputValuesNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodOutputValuesNode()}. */
  CompletableFuture<? extends VariableNode> getLastMethodOutputValuesNodeAsync();

  /**
   * Reads the Value of the LastMethodOutputValues child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant @Nullable [] readLastMethodOutputValues() throws UaException;

  /**
   * Writes the Value of the LastMethodOutputValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastMethodOutputValues(@Nullable Variant @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readLastMethodOutputValues()}. */
  CompletableFuture<? extends @Nullable Variant @Nullable []> readLastMethodOutputValuesAsync();

  /**
   * Asynchronous form of {@link #writeLastMethodOutputValues}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeLastMethodOutputValuesAsync(
      @Nullable Variant @Nullable [] value);

  /**
   * Resolves the mandatory LastMethodReturnStatus child, a BaseDataVariableType with DataType
   * StatusCode.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getLastMethodReturnStatusNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodReturnStatusNode()}. */
  CompletableFuture<? extends VariableNode> getLastMethodReturnStatusNodeAsync();

  /**
   * Reads the Value of the LastMethodReturnStatus child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable StatusCode readLastMethodReturnStatus() throws UaException;

  /**
   * Writes the Value of the LastMethodReturnStatus child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastMethodReturnStatus(@Nullable StatusCode value) throws UaException;

  /** Asynchronous form of {@link #readLastMethodReturnStatus()}. */
  CompletableFuture<? extends @Nullable StatusCode> readLastMethodReturnStatusAsync();

  /**
   * Asynchronous form of {@link #writeLastMethodReturnStatus}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeLastMethodReturnStatusAsync(@Nullable StatusCode value);

  /**
   * Resolves the mandatory LastMethodInputArguments child, a BaseDataVariableType with DataType
   * Argument.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getLastMethodInputArgumentsNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodInputArgumentsNode()}. */
  CompletableFuture<? extends VariableNode> getLastMethodInputArgumentsNodeAsync();

  /**
   * Reads the Value of the LastMethodInputArguments child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Argument @Nullable [] readLastMethodInputArguments() throws UaException;

  /**
   * Writes the Value of the LastMethodInputArguments child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastMethodInputArguments(@Nullable Argument @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readLastMethodInputArguments()}. */
  CompletableFuture<? extends @Nullable Argument @Nullable []> readLastMethodInputArgumentsAsync();

  /**
   * Asynchronous form of {@link #writeLastMethodInputArguments}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeLastMethodInputArgumentsAsync(
      @Nullable Argument @Nullable [] value);

  /**
   * Resolves the mandatory LastMethodOutputArguments child, a BaseDataVariableType with DataType
   * Argument.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getLastMethodOutputArgumentsNode() throws UaException;

  /** Asynchronous form of {@link #getLastMethodOutputArgumentsNode()}. */
  CompletableFuture<? extends VariableNode> getLastMethodOutputArgumentsNodeAsync();

  /**
   * Reads the Value of the LastMethodOutputArguments child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Argument @Nullable [] readLastMethodOutputArguments() throws UaException;

  /**
   * Writes the Value of the LastMethodOutputArguments child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastMethodOutputArguments(@Nullable Argument @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readLastMethodOutputArguments()}. */
  CompletableFuture<? extends @Nullable Argument @Nullable []> readLastMethodOutputArgumentsAsync();

  /**
   * Asynchronous form of {@link #writeLastMethodOutputArguments}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeLastMethodOutputArgumentsAsync(
      @Nullable Argument @Nullable [] value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ProgramDiagnostic2DataType readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable ProgramDiagnostic2DataType value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable ProgramDiagnostic2DataType> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable ProgramDiagnostic2DataType value);
}
