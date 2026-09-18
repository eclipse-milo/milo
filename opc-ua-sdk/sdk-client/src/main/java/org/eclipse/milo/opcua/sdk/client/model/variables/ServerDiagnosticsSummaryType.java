package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ServerDiagnosticsSummaryType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8">Model
 *     documentation</a>
 */
public interface ServerDiagnosticsSummaryType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2150L);

  /**
   * Resolves the mandatory ServerViewCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getServerViewCountNode() throws UaException;

  /** Asynchronous form of {@link #getServerViewCountNode()}. */
  CompletableFuture<? extends VariableNode> getServerViewCountNodeAsync();

  /**
   * Reads the Value of the ServerViewCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readServerViewCount() throws UaException;

  /**
   * Writes the Value of the ServerViewCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerViewCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readServerViewCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readServerViewCountAsync();

  /** Asynchronous form of {@link #writeServerViewCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServerViewCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory SessionAbortCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSessionAbortCountNode() throws UaException;

  /** Asynchronous form of {@link #getSessionAbortCountNode()}. */
  CompletableFuture<? extends VariableNode> getSessionAbortCountNodeAsync();

  /**
   * Reads the Value of the SessionAbortCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readSessionAbortCount() throws UaException;

  /**
   * Writes the Value of the SessionAbortCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSessionAbortCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readSessionAbortCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readSessionAbortCountAsync();

  /** Asynchronous form of {@link #writeSessionAbortCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSessionAbortCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory CurrentSessionCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCurrentSessionCountNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentSessionCountNode()}. */
  CompletableFuture<? extends VariableNode> getCurrentSessionCountNodeAsync();

  /**
   * Reads the Value of the CurrentSessionCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readCurrentSessionCount() throws UaException;

  /**
   * Writes the Value of the CurrentSessionCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentSessionCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readCurrentSessionCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentSessionCountAsync();

  /**
   * Asynchronous form of {@link #writeCurrentSessionCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeCurrentSessionCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory SessionTimeoutCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSessionTimeoutCountNode() throws UaException;

  /** Asynchronous form of {@link #getSessionTimeoutCountNode()}. */
  CompletableFuture<? extends VariableNode> getSessionTimeoutCountNodeAsync();

  /**
   * Reads the Value of the SessionTimeoutCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readSessionTimeoutCount() throws UaException;

  /**
   * Writes the Value of the SessionTimeoutCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSessionTimeoutCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readSessionTimeoutCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readSessionTimeoutCountAsync();

  /**
   * Asynchronous form of {@link #writeSessionTimeoutCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSessionTimeoutCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory RejectedSessionCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRejectedSessionCountNode() throws UaException;

  /** Asynchronous form of {@link #getRejectedSessionCountNode()}. */
  CompletableFuture<? extends VariableNode> getRejectedSessionCountNodeAsync();

  /**
   * Reads the Value of the RejectedSessionCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readRejectedSessionCount() throws UaException;

  /**
   * Writes the Value of the RejectedSessionCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRejectedSessionCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readRejectedSessionCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readRejectedSessionCountAsync();

  /**
   * Asynchronous form of {@link #writeRejectedSessionCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeRejectedSessionCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory CumulatedSessionCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCumulatedSessionCountNode() throws UaException;

  /** Asynchronous form of {@link #getCumulatedSessionCountNode()}. */
  CompletableFuture<? extends VariableNode> getCumulatedSessionCountNodeAsync();

  /**
   * Reads the Value of the CumulatedSessionCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readCumulatedSessionCount() throws UaException;

  /**
   * Writes the Value of the CumulatedSessionCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCumulatedSessionCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readCumulatedSessionCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readCumulatedSessionCountAsync();

  /**
   * Asynchronous form of {@link #writeCumulatedSessionCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeCumulatedSessionCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory RejectedRequestsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRejectedRequestsCountNode() throws UaException;

  /** Asynchronous form of {@link #getRejectedRequestsCountNode()}. */
  CompletableFuture<? extends VariableNode> getRejectedRequestsCountNodeAsync();

  /**
   * Reads the Value of the RejectedRequestsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readRejectedRequestsCount() throws UaException;

  /**
   * Writes the Value of the RejectedRequestsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRejectedRequestsCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readRejectedRequestsCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readRejectedRequestsCountAsync();

  /**
   * Asynchronous form of {@link #writeRejectedRequestsCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeRejectedRequestsCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory PublishingIntervalCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getPublishingIntervalCountNode() throws UaException;

  /** Asynchronous form of {@link #getPublishingIntervalCountNode()}. */
  CompletableFuture<? extends VariableNode> getPublishingIntervalCountNodeAsync();

  /**
   * Reads the Value of the PublishingIntervalCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readPublishingIntervalCount() throws UaException;

  /**
   * Writes the Value of the PublishingIntervalCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePublishingIntervalCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readPublishingIntervalCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readPublishingIntervalCountAsync();

  /**
   * Asynchronous form of {@link #writePublishingIntervalCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writePublishingIntervalCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory CurrentSubscriptionCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCurrentSubscriptionCountNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentSubscriptionCountNode()}. */
  CompletableFuture<? extends VariableNode> getCurrentSubscriptionCountNodeAsync();

  /**
   * Reads the Value of the CurrentSubscriptionCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readCurrentSubscriptionCount() throws UaException;

  /**
   * Writes the Value of the CurrentSubscriptionCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentSubscriptionCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readCurrentSubscriptionCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentSubscriptionCountAsync();

  /**
   * Asynchronous form of {@link #writeCurrentSubscriptionCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeCurrentSubscriptionCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory CumulatedSubscriptionCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCumulatedSubscriptionCountNode() throws UaException;

  /** Asynchronous form of {@link #getCumulatedSubscriptionCountNode()}. */
  CompletableFuture<? extends VariableNode> getCumulatedSubscriptionCountNodeAsync();

  /**
   * Reads the Value of the CumulatedSubscriptionCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readCumulatedSubscriptionCount() throws UaException;

  /**
   * Writes the Value of the CumulatedSubscriptionCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCumulatedSubscriptionCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readCumulatedSubscriptionCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readCumulatedSubscriptionCountAsync();

  /**
   * Asynchronous form of {@link #writeCumulatedSubscriptionCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeCumulatedSubscriptionCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory SecurityRejectedSessionCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSecurityRejectedSessionCountNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityRejectedSessionCountNode()}. */
  CompletableFuture<? extends VariableNode> getSecurityRejectedSessionCountNodeAsync();

  /**
   * Reads the Value of the SecurityRejectedSessionCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readSecurityRejectedSessionCount() throws UaException;

  /**
   * Writes the Value of the SecurityRejectedSessionCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityRejectedSessionCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readSecurityRejectedSessionCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readSecurityRejectedSessionCountAsync();

  /**
   * Asynchronous form of {@link #writeSecurityRejectedSessionCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSecurityRejectedSessionCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory SecurityRejectedRequestsCount child, a BaseDataVariableType with
   * DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSecurityRejectedRequestsCountNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityRejectedRequestsCountNode()}. */
  CompletableFuture<? extends VariableNode> getSecurityRejectedRequestsCountNodeAsync();

  /**
   * Reads the Value of the SecurityRejectedRequestsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readSecurityRejectedRequestsCount() throws UaException;

  /**
   * Writes the Value of the SecurityRejectedRequestsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityRejectedRequestsCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readSecurityRejectedRequestsCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readSecurityRejectedRequestsCountAsync();

  /**
   * Asynchronous form of {@link #writeSecurityRejectedRequestsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSecurityRejectedRequestsCountAsync(@Nullable UInteger value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServerDiagnosticsSummaryDataType readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable ServerDiagnosticsSummaryDataType value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable ServerDiagnosticsSummaryDataType> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable ServerDiagnosticsSummaryDataType value);
}
