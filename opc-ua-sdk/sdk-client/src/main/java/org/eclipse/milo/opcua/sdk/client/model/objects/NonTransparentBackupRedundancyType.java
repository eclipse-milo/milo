package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundantServerMode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the NonTransparentBackupRedundancyType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.15">Model
 *     documentation</a>
 */
public interface NonTransparentBackupRedundancyType extends NonTransparentRedundancyType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32411L);

  QualifiedProperty<RedundantServerMode> Mode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Mode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 32417L),
          -1,
          RedundantServerMode.class);

  /**
   * Resolves the mandatory RedundantServerArray child, a PropertyType with DataType
   * RedundantServerDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRedundantServerArrayNode() throws UaException;

  /** Asynchronous form of {@link #getRedundantServerArrayNode()}. */
  CompletableFuture<? extends PropertyType> getRedundantServerArrayNodeAsync();

  /**
   * Resolves the mandatory Mode child, a PropertyType with DataType RedundantServerMode.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getModeNode() throws UaException;

  /** Asynchronous form of {@link #getModeNode()}. */
  CompletableFuture<? extends PropertyType> getModeNodeAsync();

  /**
   * Reads the Value of the Mode child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable RedundantServerMode readMode() throws UaException;

  /**
   * Writes the Value of the Mode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMode(@Nullable RedundantServerMode value) throws UaException;

  /** Asynchronous form of {@link #readMode()}. */
  CompletableFuture<? extends @Nullable RedundantServerMode> readModeAsync();

  /** Asynchronous form of {@link #writeMode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeModeAsync(@Nullable RedundantServerMode value);

  /**
   * Resolves the mandatory Failover Method node.
   *
   * @throws UaException if lookup or validation fails.
   */
  UaMethodNode getFailoverMethodNode() throws UaException;

  /** Asynchronous form of {@link #getFailoverMethodNode()}. */
  CompletableFuture<UaMethodNode> getFailoverMethodNodeAsync();

  /**
   * Calls the Failover Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   */
  void failover() throws UaException;

  /**
   * Calls the Failover Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callFailover() throws UaException;

  /**
   * Calls the Failover Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callFailoverWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #failover}. */
  CompletableFuture<Void> failoverAsync();

  /** Asynchronous form of {@link #callFailover}. */
  CompletableFuture<MethodCallResult<Void>> callFailoverAsync();

  /** Asynchronous form of {@link #callFailoverWith}. */
  CompletableFuture<MethodCallResult<Void>> callFailoverWithAsync(MethodCallOptions options);
}
