package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SubscriptionDiagnosticsType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12">Model
 *     documentation</a>
 */
public interface SubscriptionDiagnosticsType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2172L);

  /**
   * Resolves the mandatory EnableCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getEnableCountNode() throws UaException;

  /** Asynchronous form of {@link #getEnableCountNode()}. */
  CompletableFuture<? extends VariableNode> getEnableCountNodeAsync();

  /**
   * Reads the Value of the EnableCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readEnableCount() throws UaException;

  /**
   * Writes the Value of the EnableCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEnableCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readEnableCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readEnableCountAsync();

  /** Asynchronous form of {@link #writeEnableCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEnableCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory ModifyCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getModifyCountNode() throws UaException;

  /** Asynchronous form of {@link #getModifyCountNode()}. */
  CompletableFuture<? extends VariableNode> getModifyCountNodeAsync();

  /**
   * Reads the Value of the ModifyCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readModifyCount() throws UaException;

  /**
   * Writes the Value of the ModifyCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeModifyCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readModifyCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readModifyCountAsync();

  /** Asynchronous form of {@link #writeModifyCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeModifyCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory DisableCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDisableCountNode() throws UaException;

  /** Asynchronous form of {@link #getDisableCountNode()}. */
  CompletableFuture<? extends VariableNode> getDisableCountNodeAsync();

  /**
   * Reads the Value of the DisableCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readDisableCount() throws UaException;

  /**
   * Writes the Value of the DisableCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDisableCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readDisableCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readDisableCountAsync();

  /** Asynchronous form of {@link #writeDisableCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDisableCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory SubscriptionId child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSubscriptionIdNode() throws UaException;

  /** Asynchronous form of {@link #getSubscriptionIdNode()}. */
  CompletableFuture<? extends VariableNode> getSubscriptionIdNodeAsync();

  /**
   * Reads the Value of the SubscriptionId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readSubscriptionId() throws UaException;

  /**
   * Writes the Value of the SubscriptionId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSubscriptionId(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readSubscriptionId()}. */
  CompletableFuture<? extends @Nullable UInteger> readSubscriptionIdAsync();

  /** Asynchronous form of {@link #writeSubscriptionId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSubscriptionIdAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxLifetimeCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMaxLifetimeCountNode() throws UaException;

  /** Asynchronous form of {@link #getMaxLifetimeCountNode()}. */
  CompletableFuture<? extends VariableNode> getMaxLifetimeCountNodeAsync();

  /**
   * Reads the Value of the MaxLifetimeCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxLifetimeCount() throws UaException;

  /**
   * Writes the Value of the MaxLifetimeCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxLifetimeCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxLifetimeCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxLifetimeCountAsync();

  /** Asynchronous form of {@link #writeMaxLifetimeCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxLifetimeCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxKeepAliveCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMaxKeepAliveCountNode() throws UaException;

  /** Asynchronous form of {@link #getMaxKeepAliveCountNode()}. */
  CompletableFuture<? extends VariableNode> getMaxKeepAliveCountNodeAsync();

  /**
   * Reads the Value of the MaxKeepAliveCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxKeepAliveCount() throws UaException;

  /**
   * Writes the Value of the MaxKeepAliveCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxKeepAliveCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxKeepAliveCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxKeepAliveCountAsync();

  /** Asynchronous form of {@link #writeMaxKeepAliveCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxKeepAliveCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory PublishingEnabled child, a BaseDataVariableType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getPublishingEnabledNode() throws UaException;

  /** Asynchronous form of {@link #getPublishingEnabledNode()}. */
  CompletableFuture<? extends VariableNode> getPublishingEnabledNodeAsync();

  /**
   * Reads the Value of the PublishingEnabled child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readPublishingEnabled() throws UaException;

  /**
   * Writes the Value of the PublishingEnabled child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePublishingEnabled(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readPublishingEnabled()}. */
  CompletableFuture<? extends @Nullable Boolean> readPublishingEnabledAsync();

  /** Asynchronous form of {@link #writePublishingEnabled}; completes with the operation status. */
  CompletableFuture<StatusCode> writePublishingEnabledAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory MonitoredItemCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMonitoredItemCountNode() throws UaException;

  /** Asynchronous form of {@link #getMonitoredItemCountNode()}. */
  CompletableFuture<? extends VariableNode> getMonitoredItemCountNodeAsync();

  /**
   * Reads the Value of the MonitoredItemCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMonitoredItemCount() throws UaException;

  /**
   * Writes the Value of the MonitoredItemCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMonitoredItemCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMonitoredItemCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readMonitoredItemCountAsync();

  /** Asynchronous form of {@link #writeMonitoredItemCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMonitoredItemCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory NextSequenceNumber child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getNextSequenceNumberNode() throws UaException;

  /** Asynchronous form of {@link #getNextSequenceNumberNode()}. */
  CompletableFuture<? extends VariableNode> getNextSequenceNumberNodeAsync();

  /**
   * Reads the Value of the NextSequenceNumber child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readNextSequenceNumber() throws UaException;

  /**
   * Writes the Value of the NextSequenceNumber child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNextSequenceNumber(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readNextSequenceNumber()}. */
  CompletableFuture<? extends @Nullable UInteger> readNextSequenceNumberAsync();

  /** Asynchronous form of {@link #writeNextSequenceNumber}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNextSequenceNumberAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory NotificationsCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getNotificationsCountNode() throws UaException;

  /** Asynchronous form of {@link #getNotificationsCountNode()}. */
  CompletableFuture<? extends VariableNode> getNotificationsCountNodeAsync();

  /**
   * Reads the Value of the NotificationsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readNotificationsCount() throws UaException;

  /**
   * Writes the Value of the NotificationsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNotificationsCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readNotificationsCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readNotificationsCountAsync();

  /** Asynchronous form of {@link #writeNotificationsCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNotificationsCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory PublishingInterval child, a BaseDataVariableType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getPublishingIntervalNode() throws UaException;

  /** Asynchronous form of {@link #getPublishingIntervalNode()}. */
  CompletableFuture<? extends VariableNode> getPublishingIntervalNodeAsync();

  /**
   * Reads the Value of the PublishingInterval child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readPublishingInterval() throws UaException;

  /**
   * Writes the Value of the PublishingInterval child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePublishingInterval(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readPublishingInterval()}. */
  CompletableFuture<? extends @Nullable Double> readPublishingIntervalAsync();

  /** Asynchronous form of {@link #writePublishingInterval}; completes with the operation status. */
  CompletableFuture<StatusCode> writePublishingIntervalAsync(@Nullable Double value);

  /**
   * Resolves the mandatory PublishRequestCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getPublishRequestCountNode() throws UaException;

  /** Asynchronous form of {@link #getPublishRequestCountNode()}. */
  CompletableFuture<? extends VariableNode> getPublishRequestCountNodeAsync();

  /**
   * Reads the Value of the PublishRequestCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readPublishRequestCount() throws UaException;

  /**
   * Writes the Value of the PublishRequestCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePublishRequestCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readPublishRequestCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readPublishRequestCountAsync();

  /**
   * Asynchronous form of {@link #writePublishRequestCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writePublishRequestCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory CurrentLifetimeCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCurrentLifetimeCountNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentLifetimeCountNode()}. */
  CompletableFuture<? extends VariableNode> getCurrentLifetimeCountNodeAsync();

  /**
   * Reads the Value of the CurrentLifetimeCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readCurrentLifetimeCount() throws UaException;

  /**
   * Writes the Value of the CurrentLifetimeCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentLifetimeCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readCurrentLifetimeCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentLifetimeCountAsync();

  /**
   * Asynchronous form of {@link #writeCurrentLifetimeCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeCurrentLifetimeCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory TransferRequestCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getTransferRequestCountNode() throws UaException;

  /** Asynchronous form of {@link #getTransferRequestCountNode()}. */
  CompletableFuture<? extends VariableNode> getTransferRequestCountNodeAsync();

  /**
   * Reads the Value of the TransferRequestCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readTransferRequestCount() throws UaException;

  /**
   * Writes the Value of the TransferRequestCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTransferRequestCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readTransferRequestCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readTransferRequestCountAsync();

  /**
   * Asynchronous form of {@link #writeTransferRequestCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeTransferRequestCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory CurrentKeepAliveCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCurrentKeepAliveCountNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentKeepAliveCountNode()}. */
  CompletableFuture<? extends VariableNode> getCurrentKeepAliveCountNodeAsync();

  /**
   * Reads the Value of the CurrentKeepAliveCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readCurrentKeepAliveCount() throws UaException;

  /**
   * Writes the Value of the CurrentKeepAliveCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentKeepAliveCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readCurrentKeepAliveCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentKeepAliveCountAsync();

  /**
   * Asynchronous form of {@link #writeCurrentKeepAliveCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeCurrentKeepAliveCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory DiscardedMessageCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDiscardedMessageCountNode() throws UaException;

  /** Asynchronous form of {@link #getDiscardedMessageCountNode()}. */
  CompletableFuture<? extends VariableNode> getDiscardedMessageCountNodeAsync();

  /**
   * Reads the Value of the DiscardedMessageCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readDiscardedMessageCount() throws UaException;

  /**
   * Writes the Value of the DiscardedMessageCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDiscardedMessageCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readDiscardedMessageCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readDiscardedMessageCountAsync();

  /**
   * Asynchronous form of {@link #writeDiscardedMessageCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeDiscardedMessageCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory RepublishMessageCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRepublishMessageCountNode() throws UaException;

  /** Asynchronous form of {@link #getRepublishMessageCountNode()}. */
  CompletableFuture<? extends VariableNode> getRepublishMessageCountNodeAsync();

  /**
   * Reads the Value of the RepublishMessageCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readRepublishMessageCount() throws UaException;

  /**
   * Writes the Value of the RepublishMessageCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRepublishMessageCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readRepublishMessageCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readRepublishMessageCountAsync();

  /**
   * Asynchronous form of {@link #writeRepublishMessageCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeRepublishMessageCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory RepublishRequestCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRepublishRequestCountNode() throws UaException;

  /** Asynchronous form of {@link #getRepublishRequestCountNode()}. */
  CompletableFuture<? extends VariableNode> getRepublishRequestCountNodeAsync();

  /**
   * Reads the Value of the RepublishRequestCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readRepublishRequestCount() throws UaException;

  /**
   * Writes the Value of the RepublishRequestCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRepublishRequestCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readRepublishRequestCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readRepublishRequestCountAsync();

  /**
   * Asynchronous form of {@link #writeRepublishRequestCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeRepublishRequestCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory EventNotificationsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getEventNotificationsCountNode() throws UaException;

  /** Asynchronous form of {@link #getEventNotificationsCountNode()}. */
  CompletableFuture<? extends VariableNode> getEventNotificationsCountNodeAsync();

  /**
   * Reads the Value of the EventNotificationsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readEventNotificationsCount() throws UaException;

  /**
   * Writes the Value of the EventNotificationsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEventNotificationsCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readEventNotificationsCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readEventNotificationsCountAsync();

  /**
   * Asynchronous form of {@link #writeEventNotificationsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeEventNotificationsCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory EventQueueOverflowCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getEventQueueOverflowCountNode() throws UaException;

  /** Asynchronous form of {@link #getEventQueueOverflowCountNode()}. */
  CompletableFuture<? extends VariableNode> getEventQueueOverflowCountNodeAsync();

  /**
   * Reads the Value of the EventQueueOverflowCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readEventQueueOverflowCount() throws UaException;

  /**
   * Writes the Value of the EventQueueOverflowCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEventQueueOverflowCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readEventQueueOverflowCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readEventQueueOverflowCountAsync();

  /**
   * Asynchronous form of {@link #writeEventQueueOverflowCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeEventQueueOverflowCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory LatePublishRequestCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getLatePublishRequestCountNode() throws UaException;

  /** Asynchronous form of {@link #getLatePublishRequestCountNode()}. */
  CompletableFuture<? extends VariableNode> getLatePublishRequestCountNodeAsync();

  /**
   * Reads the Value of the LatePublishRequestCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readLatePublishRequestCount() throws UaException;

  /**
   * Writes the Value of the LatePublishRequestCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLatePublishRequestCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readLatePublishRequestCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readLatePublishRequestCountAsync();

  /**
   * Asynchronous form of {@link #writeLatePublishRequestCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeLatePublishRequestCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory DisabledMonitoredItemCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDisabledMonitoredItemCountNode() throws UaException;

  /** Asynchronous form of {@link #getDisabledMonitoredItemCountNode()}. */
  CompletableFuture<? extends VariableNode> getDisabledMonitoredItemCountNodeAsync();

  /**
   * Reads the Value of the DisabledMonitoredItemCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readDisabledMonitoredItemCount() throws UaException;

  /**
   * Writes the Value of the DisabledMonitoredItemCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDisabledMonitoredItemCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readDisabledMonitoredItemCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readDisabledMonitoredItemCountAsync();

  /**
   * Asynchronous form of {@link #writeDisabledMonitoredItemCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDisabledMonitoredItemCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxNotificationsPerPublish child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMaxNotificationsPerPublishNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNotificationsPerPublishNode()}. */
  CompletableFuture<? extends VariableNode> getMaxNotificationsPerPublishNodeAsync();

  /**
   * Reads the Value of the MaxNotificationsPerPublish child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNotificationsPerPublish() throws UaException;

  /**
   * Writes the Value of the MaxNotificationsPerPublish child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNotificationsPerPublish(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNotificationsPerPublish()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNotificationsPerPublishAsync();

  /**
   * Asynchronous form of {@link #writeMaxNotificationsPerPublish}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxNotificationsPerPublishAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory UnacknowledgedMessageCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getUnacknowledgedMessageCountNode() throws UaException;

  /** Asynchronous form of {@link #getUnacknowledgedMessageCountNode()}. */
  CompletableFuture<? extends VariableNode> getUnacknowledgedMessageCountNodeAsync();

  /**
   * Reads the Value of the UnacknowledgedMessageCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readUnacknowledgedMessageCount() throws UaException;

  /**
   * Writes the Value of the UnacknowledgedMessageCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUnacknowledgedMessageCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readUnacknowledgedMessageCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readUnacknowledgedMessageCountAsync();

  /**
   * Asynchronous form of {@link #writeUnacknowledgedMessageCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeUnacknowledgedMessageCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory TransferredToAltClientCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getTransferredToAltClientCountNode() throws UaException;

  /** Asynchronous form of {@link #getTransferredToAltClientCountNode()}. */
  CompletableFuture<? extends VariableNode> getTransferredToAltClientCountNodeAsync();

  /**
   * Reads the Value of the TransferredToAltClientCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readTransferredToAltClientCount() throws UaException;

  /**
   * Writes the Value of the TransferredToAltClientCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTransferredToAltClientCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readTransferredToAltClientCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readTransferredToAltClientCountAsync();

  /**
   * Asynchronous form of {@link #writeTransferredToAltClientCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeTransferredToAltClientCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory DataChangeNotificationsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDataChangeNotificationsCountNode() throws UaException;

  /** Asynchronous form of {@link #getDataChangeNotificationsCountNode()}. */
  CompletableFuture<? extends VariableNode> getDataChangeNotificationsCountNodeAsync();

  /**
   * Reads the Value of the DataChangeNotificationsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readDataChangeNotificationsCount() throws UaException;

  /**
   * Writes the Value of the DataChangeNotificationsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataChangeNotificationsCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readDataChangeNotificationsCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readDataChangeNotificationsCountAsync();

  /**
   * Asynchronous form of {@link #writeDataChangeNotificationsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDataChangeNotificationsCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MonitoringQueueOverflowCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMonitoringQueueOverflowCountNode() throws UaException;

  /** Asynchronous form of {@link #getMonitoringQueueOverflowCountNode()}. */
  CompletableFuture<? extends VariableNode> getMonitoringQueueOverflowCountNodeAsync();

  /**
   * Reads the Value of the MonitoringQueueOverflowCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMonitoringQueueOverflowCount() throws UaException;

  /**
   * Writes the Value of the MonitoringQueueOverflowCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMonitoringQueueOverflowCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMonitoringQueueOverflowCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readMonitoringQueueOverflowCountAsync();

  /**
   * Asynchronous form of {@link #writeMonitoringQueueOverflowCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMonitoringQueueOverflowCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory RepublishMessageRequestCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRepublishMessageRequestCountNode() throws UaException;

  /** Asynchronous form of {@link #getRepublishMessageRequestCountNode()}. */
  CompletableFuture<? extends VariableNode> getRepublishMessageRequestCountNodeAsync();

  /**
   * Reads the Value of the RepublishMessageRequestCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readRepublishMessageRequestCount() throws UaException;

  /**
   * Writes the Value of the RepublishMessageRequestCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRepublishMessageRequestCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readRepublishMessageRequestCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readRepublishMessageRequestCountAsync();

  /**
   * Asynchronous form of {@link #writeRepublishMessageRequestCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeRepublishMessageRequestCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory TransferredToSameClientCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getTransferredToSameClientCountNode() throws UaException;

  /** Asynchronous form of {@link #getTransferredToSameClientCountNode()}. */
  CompletableFuture<? extends VariableNode> getTransferredToSameClientCountNodeAsync();

  /**
   * Reads the Value of the TransferredToSameClientCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readTransferredToSameClientCount() throws UaException;

  /**
   * Writes the Value of the TransferredToSameClientCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTransferredToSameClientCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readTransferredToSameClientCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readTransferredToSameClientCountAsync();

  /**
   * Asynchronous form of {@link #writeTransferredToSameClientCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeTransferredToSameClientCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory Priority child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getPriorityNode() throws UaException;

  /** Asynchronous form of {@link #getPriorityNode()}. */
  CompletableFuture<? extends VariableNode> getPriorityNodeAsync();

  /**
   * Reads the Value of the Priority child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UByte readPriority() throws UaException;

  /**
   * Writes the Value of the Priority child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePriority(@Nullable UByte value) throws UaException;

  /** Asynchronous form of {@link #readPriority()}. */
  CompletableFuture<? extends @Nullable UByte> readPriorityAsync();

  /** Asynchronous form of {@link #writePriority}; completes with the operation status. */
  CompletableFuture<StatusCode> writePriorityAsync(@Nullable UByte value);

  /**
   * Resolves the mandatory SessionId child, a BaseDataVariableType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSessionIdNode() throws UaException;

  /** Asynchronous form of {@link #getSessionIdNode()}. */
  CompletableFuture<? extends VariableNode> getSessionIdNodeAsync();

  /**
   * Reads the Value of the SessionId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readSessionId() throws UaException;

  /**
   * Writes the Value of the SessionId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSessionId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readSessionId()}. */
  CompletableFuture<? extends @Nullable NodeId> readSessionIdAsync();

  /** Asynchronous form of {@link #writeSessionId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSessionIdAsync(@Nullable NodeId value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SubscriptionDiagnosticsDataType readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable SubscriptionDiagnosticsDataType value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable SubscriptionDiagnosticsDataType value);
}
