package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PubSubDiagnosticsCounterType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubDiagnosticsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2">Model
 *     documentation</a>
 */
public interface PubSubDiagnosticsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19677L);

  /**
   * Resolves the mandatory LiveValues child, a BaseObjectType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.2">BaseObjectType
   *     documentation</a>
   */
  ObjectNode getLiveValuesNode() throws UaException;

  /** Asynchronous form of {@link #getLiveValuesNode()}. */
  CompletableFuture<? extends ObjectNode> getLiveValuesNodeAsync();

  /**
   * Resolves the mandatory TotalError child, a PubSubDiagnosticsCounterType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5">PubSubDiagnosticsCounterType
   *     documentation</a>
   */
  PubSubDiagnosticsCounterType getTotalErrorNode() throws UaException;

  /** Asynchronous form of {@link #getTotalErrorNode()}. */
  CompletableFuture<? extends PubSubDiagnosticsCounterType> getTotalErrorNodeAsync();

  /**
   * Reads the Value of the TotalError child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readTotalError() throws UaException;

  /**
   * Writes the Value of the TotalError child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTotalError(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readTotalError()}. */
  CompletableFuture<? extends @Nullable UInteger> readTotalErrorAsync();

  /** Asynchronous form of {@link #writeTotalError}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTotalErrorAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory DiagnosticsLevel child, a BaseDataVariableType with DataType
   * DiagnosticsLevel.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDiagnosticsLevelNode() throws UaException;

  /** Asynchronous form of {@link #getDiagnosticsLevelNode()}. */
  CompletableFuture<? extends VariableNode> getDiagnosticsLevelNodeAsync();

  /**
   * Reads the Value of the DiagnosticsLevel child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DiagnosticsLevel readDiagnosticsLevel() throws UaException;

  /**
   * Writes the Value of the DiagnosticsLevel child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDiagnosticsLevel(@Nullable DiagnosticsLevel value) throws UaException;

  /** Asynchronous form of {@link #readDiagnosticsLevel()}. */
  CompletableFuture<? extends @Nullable DiagnosticsLevel> readDiagnosticsLevelAsync();

  /** Asynchronous form of {@link #writeDiagnosticsLevel}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDiagnosticsLevelAsync(@Nullable DiagnosticsLevel value);

  /**
   * Resolves the mandatory TotalInformation child, a PubSubDiagnosticsCounterType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5">PubSubDiagnosticsCounterType
   *     documentation</a>
   */
  PubSubDiagnosticsCounterType getTotalInformationNode() throws UaException;

  /** Asynchronous form of {@link #getTotalInformationNode()}. */
  CompletableFuture<? extends PubSubDiagnosticsCounterType> getTotalInformationNodeAsync();

  /**
   * Reads the Value of the TotalInformation child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readTotalInformation() throws UaException;

  /**
   * Writes the Value of the TotalInformation child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTotalInformation(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readTotalInformation()}. */
  CompletableFuture<? extends @Nullable UInteger> readTotalInformationAsync();

  /** Asynchronous form of {@link #writeTotalInformation}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTotalInformationAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory Counters child, a BaseObjectType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.2">BaseObjectType
   *     documentation</a>
   */
  ObjectNode getCountersNode() throws UaException;

  /** Asynchronous form of {@link #getCountersNode()}. */
  CompletableFuture<? extends ObjectNode> getCountersNodeAsync();

  /**
   * Resolves the mandatory SubError child, a BaseDataVariableType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSubErrorNode() throws UaException;

  /** Asynchronous form of {@link #getSubErrorNode()}. */
  CompletableFuture<? extends VariableNode> getSubErrorNodeAsync();

  /**
   * Reads the Value of the SubError child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readSubError() throws UaException;

  /**
   * Writes the Value of the SubError child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSubError(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readSubError()}. */
  CompletableFuture<? extends @Nullable Boolean> readSubErrorAsync();

  /** Asynchronous form of {@link #writeSubError}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSubErrorAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory Reset Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3">Model
   *     documentation</a>
   */
  UaMethodNode getResetMethodNode() throws UaException;

  /** Asynchronous form of {@link #getResetMethodNode()}. */
  CompletableFuture<UaMethodNode> getResetMethodNodeAsync();

  /**
   * Calls the Reset Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3">Model
   *     documentation</a>
   */
  void reset() throws UaException;

  /**
   * Calls the Reset Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callReset() throws UaException;

  /**
   * Calls the Reset Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callResetWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #reset}. */
  CompletableFuture<Void> resetAsync();

  /** Asynchronous form of {@link #callReset}. */
  CompletableFuture<MethodCallResult<Void>> callResetAsync();

  /** Asynchronous form of {@link #callResetWith}. */
  CompletableFuture<MethodCallResult<Void>> callResetWithAsync(MethodCallOptions options);
}
