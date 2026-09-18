package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SessionDiagnosticsVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14">Model
 *     documentation</a>
 */
public interface SessionDiagnosticsVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2197L);

  /**
   * Resolves the mandatory WriteCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getWriteCountNode() throws UaException;

  /** Asynchronous form of {@link #getWriteCountNode()}. */
  CompletableFuture<? extends VariableNode> getWriteCountNodeAsync();

  /**
   * Reads the Value of the WriteCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readWriteCount() throws UaException;

  /**
   * Writes the Value of the WriteCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeWriteCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readWriteCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readWriteCountAsync();

  /** Asynchronous form of {@link #writeWriteCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeWriteCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory BrowseCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getBrowseCountNode() throws UaException;

  /** Asynchronous form of {@link #getBrowseCountNode()}. */
  CompletableFuture<? extends VariableNode> getBrowseCountNodeAsync();

  /**
   * Reads the Value of the BrowseCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readBrowseCount() throws UaException;

  /**
   * Writes the Value of the BrowseCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBrowseCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readBrowseCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readBrowseCountAsync();

  /** Asynchronous form of {@link #writeBrowseCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBrowseCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory EndpointUrl child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getEndpointUrlNode() throws UaException;

  /** Asynchronous form of {@link #getEndpointUrlNode()}. */
  CompletableFuture<? extends VariableNode> getEndpointUrlNodeAsync();

  /**
   * Reads the Value of the EndpointUrl child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readEndpointUrl() throws UaException;

  /**
   * Writes the Value of the EndpointUrl child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEndpointUrl(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readEndpointUrl()}. */
  CompletableFuture<? extends @Nullable String> readEndpointUrlAsync();

  /** Asynchronous form of {@link #writeEndpointUrl}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEndpointUrlAsync(@Nullable String value);

  /**
   * Resolves the mandatory SessionName child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSessionNameNode() throws UaException;

  /** Asynchronous form of {@link #getSessionNameNode()}. */
  CompletableFuture<? extends VariableNode> getSessionNameNodeAsync();

  /**
   * Reads the Value of the SessionName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSessionName() throws UaException;

  /**
   * Writes the Value of the SessionName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSessionName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSessionName()}. */
  CompletableFuture<? extends @Nullable String> readSessionNameAsync();

  /** Asynchronous form of {@link #writeSessionName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSessionNameAsync(@Nullable String value);

  /**
   * Resolves the mandatory PublishCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getPublishCountNode() throws UaException;

  /** Asynchronous form of {@link #getPublishCountNode()}. */
  CompletableFuture<? extends VariableNode> getPublishCountNodeAsync();

  /**
   * Reads the Value of the PublishCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readPublishCount() throws UaException;

  /**
   * Writes the Value of the PublishCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePublishCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readPublishCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readPublishCountAsync();

  /** Asynchronous form of {@link #writePublishCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writePublishCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory AddNodesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getAddNodesCountNode() throws UaException;

  /** Asynchronous form of {@link #getAddNodesCountNode()}. */
  CompletableFuture<? extends VariableNode> getAddNodesCountNodeAsync();

  /**
   * Reads the Value of the AddNodesCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readAddNodesCount() throws UaException;

  /**
   * Writes the Value of the AddNodesCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAddNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readAddNodesCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readAddNodesCountAsync();

  /** Asynchronous form of {@link #writeAddNodesCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAddNodesCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory QueryNextCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getQueryNextCountNode() throws UaException;

  /** Asynchronous form of {@link #getQueryNextCountNode()}. */
  CompletableFuture<? extends VariableNode> getQueryNextCountNodeAsync();

  /**
   * Reads the Value of the QueryNextCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readQueryNextCount() throws UaException;

  /**
   * Writes the Value of the QueryNextCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeQueryNextCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readQueryNextCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readQueryNextCountAsync();

  /** Asynchronous form of {@link #writeQueryNextCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeQueryNextCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory RepublishCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRepublishCountNode() throws UaException;

  /** Asynchronous form of {@link #getRepublishCountNode()}. */
  CompletableFuture<? extends VariableNode> getRepublishCountNodeAsync();

  /**
   * Reads the Value of the RepublishCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readRepublishCount() throws UaException;

  /**
   * Writes the Value of the RepublishCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRepublishCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readRepublishCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readRepublishCountAsync();

  /** Asynchronous form of {@link #writeRepublishCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRepublishCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory BrowseNextCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getBrowseNextCountNode() throws UaException;

  /** Asynchronous form of {@link #getBrowseNextCountNode()}. */
  CompletableFuture<? extends VariableNode> getBrowseNextCountNodeAsync();

  /**
   * Reads the Value of the BrowseNextCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readBrowseNextCount() throws UaException;

  /**
   * Writes the Value of the BrowseNextCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBrowseNextCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readBrowseNextCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readBrowseNextCountAsync();

  /** Asynchronous form of {@link #writeBrowseNextCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBrowseNextCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory QueryFirstCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getQueryFirstCountNode() throws UaException;

  /** Asynchronous form of {@link #getQueryFirstCountNode()}. */
  CompletableFuture<? extends VariableNode> getQueryFirstCountNodeAsync();

  /**
   * Reads the Value of the QueryFirstCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readQueryFirstCount() throws UaException;

  /**
   * Writes the Value of the QueryFirstCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeQueryFirstCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readQueryFirstCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readQueryFirstCountAsync();

  /** Asynchronous form of {@link #writeQueryFirstCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeQueryFirstCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory DeleteNodesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDeleteNodesCountNode() throws UaException;

  /** Asynchronous form of {@link #getDeleteNodesCountNode()}. */
  CompletableFuture<? extends VariableNode> getDeleteNodesCountNodeAsync();

  /**
   * Reads the Value of the DeleteNodesCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readDeleteNodesCount() throws UaException;

  /**
   * Writes the Value of the DeleteNodesCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDeleteNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readDeleteNodesCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readDeleteNodesCountAsync();

  /** Asynchronous form of {@link #writeDeleteNodesCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDeleteNodesCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory HistoryReadCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getHistoryReadCountNode() throws UaException;

  /** Asynchronous form of {@link #getHistoryReadCountNode()}. */
  CompletableFuture<? extends VariableNode> getHistoryReadCountNodeAsync();

  /**
   * Reads the Value of the HistoryReadCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readHistoryReadCount() throws UaException;

  /**
   * Writes the Value of the HistoryReadCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHistoryReadCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readHistoryReadCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readHistoryReadCountAsync();

  /** Asynchronous form of {@link #writeHistoryReadCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeHistoryReadCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory ClientDescription child, a BaseDataVariableType with DataType
   * ApplicationDescription.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getClientDescriptionNode() throws UaException;

  /** Asynchronous form of {@link #getClientDescriptionNode()}. */
  CompletableFuture<? extends VariableNode> getClientDescriptionNodeAsync();

  /**
   * Reads the Value of the ClientDescription child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ApplicationDescription readClientDescription() throws UaException;

  /**
   * Writes the Value of the ClientDescription child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientDescription(@Nullable ApplicationDescription value) throws UaException;

  /** Asynchronous form of {@link #readClientDescription()}. */
  CompletableFuture<? extends @Nullable ApplicationDescription> readClientDescriptionAsync();

  /** Asynchronous form of {@link #writeClientDescription}; completes with the operation status. */
  CompletableFuture<StatusCode> writeClientDescriptionAsync(@Nullable ApplicationDescription value);

  /**
   * Resolves the mandatory TotalRequestCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getTotalRequestCountNode() throws UaException;

  /** Asynchronous form of {@link #getTotalRequestCountNode()}. */
  CompletableFuture<? extends VariableNode> getTotalRequestCountNodeAsync();

  /**
   * Reads the Value of the TotalRequestCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readTotalRequestCount() throws UaException;

  /**
   * Writes the Value of the TotalRequestCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTotalRequestCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readTotalRequestCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readTotalRequestCountAsync();

  /** Asynchronous form of {@link #writeTotalRequestCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTotalRequestCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory AddReferencesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getAddReferencesCountNode() throws UaException;

  /** Asynchronous form of {@link #getAddReferencesCountNode()}. */
  CompletableFuture<? extends VariableNode> getAddReferencesCountNodeAsync();

  /**
   * Reads the Value of the AddReferencesCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readAddReferencesCount() throws UaException;

  /**
   * Writes the Value of the AddReferencesCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAddReferencesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readAddReferencesCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readAddReferencesCountAsync();

  /** Asynchronous form of {@link #writeAddReferencesCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAddReferencesCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory HistoryUpdateCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getHistoryUpdateCountNode() throws UaException;

  /** Asynchronous form of {@link #getHistoryUpdateCountNode()}. */
  CompletableFuture<? extends VariableNode> getHistoryUpdateCountNodeAsync();

  /**
   * Reads the Value of the HistoryUpdateCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readHistoryUpdateCount() throws UaException;

  /**
   * Writes the Value of the HistoryUpdateCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHistoryUpdateCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readHistoryUpdateCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readHistoryUpdateCountAsync();

  /** Asynchronous form of {@link #writeHistoryUpdateCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeHistoryUpdateCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory RegisterNodesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getRegisterNodesCountNode() throws UaException;

  /** Asynchronous form of {@link #getRegisterNodesCountNode()}. */
  CompletableFuture<? extends VariableNode> getRegisterNodesCountNodeAsync();

  /**
   * Reads the Value of the RegisterNodesCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readRegisterNodesCount() throws UaException;

  /**
   * Writes the Value of the RegisterNodesCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRegisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readRegisterNodesCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readRegisterNodesCountAsync();

  /** Asynchronous form of {@link #writeRegisterNodesCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRegisterNodesCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory SetTriggeringCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSetTriggeringCountNode() throws UaException;

  /** Asynchronous form of {@link #getSetTriggeringCountNode()}. */
  CompletableFuture<? extends VariableNode> getSetTriggeringCountNodeAsync();

  /**
   * Reads the Value of the SetTriggeringCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readSetTriggeringCount() throws UaException;

  /**
   * Writes the Value of the SetTriggeringCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSetTriggeringCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readSetTriggeringCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readSetTriggeringCountAsync();

  /** Asynchronous form of {@link #writeSetTriggeringCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSetTriggeringCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory ActualSessionTimeout child, a BaseDataVariableType with DataType
   * Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getActualSessionTimeoutNode() throws UaException;

  /** Asynchronous form of {@link #getActualSessionTimeoutNode()}. */
  CompletableFuture<? extends VariableNode> getActualSessionTimeoutNodeAsync();

  /**
   * Reads the Value of the ActualSessionTimeout child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readActualSessionTimeout() throws UaException;

  /**
   * Writes the Value of the ActualSessionTimeout child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeActualSessionTimeout(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readActualSessionTimeout()}. */
  CompletableFuture<? extends @Nullable Double> readActualSessionTimeoutAsync();

  /**
   * Asynchronous form of {@link #writeActualSessionTimeout}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeActualSessionTimeoutAsync(@Nullable Double value);

  /**
   * Resolves the mandatory ClientConnectionTime child, a BaseDataVariableType with DataType
   * UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getClientConnectionTimeNode() throws UaException;

  /** Asynchronous form of {@link #getClientConnectionTimeNode()}. */
  CompletableFuture<? extends VariableNode> getClientConnectionTimeNodeAsync();

  /**
   * Reads the Value of the ClientConnectionTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readClientConnectionTime() throws UaException;

  /**
   * Writes the Value of the ClientConnectionTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientConnectionTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readClientConnectionTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readClientConnectionTimeAsync();

  /**
   * Asynchronous form of {@link #writeClientConnectionTime}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeClientConnectionTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory UnregisterNodesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getUnregisterNodesCountNode() throws UaException;

  /** Asynchronous form of {@link #getUnregisterNodesCountNode()}. */
  CompletableFuture<? extends VariableNode> getUnregisterNodesCountNodeAsync();

  /**
   * Reads the Value of the UnregisterNodesCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readUnregisterNodesCount() throws UaException;

  /**
   * Writes the Value of the UnregisterNodesCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUnregisterNodesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readUnregisterNodesCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readUnregisterNodesCountAsync();

  /**
   * Asynchronous form of {@link #writeUnregisterNodesCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeUnregisterNodesCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory ClientLastContactTime child, a BaseDataVariableType with DataType
   * UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getClientLastContactTimeNode() throws UaException;

  /** Asynchronous form of {@link #getClientLastContactTimeNode()}. */
  CompletableFuture<? extends VariableNode> getClientLastContactTimeNodeAsync();

  /**
   * Reads the Value of the ClientLastContactTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readClientLastContactTime() throws UaException;

  /**
   * Writes the Value of the ClientLastContactTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientLastContactTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readClientLastContactTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readClientLastContactTimeAsync();

  /**
   * Asynchronous form of {@link #writeClientLastContactTime}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeClientLastContactTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory DeleteReferencesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDeleteReferencesCountNode() throws UaException;

  /** Asynchronous form of {@link #getDeleteReferencesCountNode()}. */
  CompletableFuture<? extends VariableNode> getDeleteReferencesCountNodeAsync();

  /**
   * Reads the Value of the DeleteReferencesCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readDeleteReferencesCount() throws UaException;

  /**
   * Writes the Value of the DeleteReferencesCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDeleteReferencesCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readDeleteReferencesCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readDeleteReferencesCountAsync();

  /**
   * Asynchronous form of {@link #writeDeleteReferencesCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeDeleteReferencesCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory MaxResponseMessageSize child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMaxResponseMessageSizeNode() throws UaException;

  /** Asynchronous form of {@link #getMaxResponseMessageSizeNode()}. */
  CompletableFuture<? extends VariableNode> getMaxResponseMessageSizeNodeAsync();

  /**
   * Reads the Value of the MaxResponseMessageSize child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxResponseMessageSize() throws UaException;

  /**
   * Writes the Value of the MaxResponseMessageSize child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxResponseMessageSize(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxResponseMessageSize()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxResponseMessageSizeAsync();

  /**
   * Asynchronous form of {@link #writeMaxResponseMessageSize}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxResponseMessageSizeAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory SetMonitoringModeCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSetMonitoringModeCountNode() throws UaException;

  /** Asynchronous form of {@link #getSetMonitoringModeCountNode()}. */
  CompletableFuture<? extends VariableNode> getSetMonitoringModeCountNodeAsync();

  /**
   * Reads the Value of the SetMonitoringModeCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readSetMonitoringModeCount() throws UaException;

  /**
   * Writes the Value of the SetMonitoringModeCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSetMonitoringModeCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readSetMonitoringModeCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readSetMonitoringModeCountAsync();

  /**
   * Asynchronous form of {@link #writeSetMonitoringModeCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSetMonitoringModeCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory SetPublishingModeCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSetPublishingModeCountNode() throws UaException;

  /** Asynchronous form of {@link #getSetPublishingModeCountNode()}. */
  CompletableFuture<? extends VariableNode> getSetPublishingModeCountNodeAsync();

  /**
   * Reads the Value of the SetPublishingModeCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readSetPublishingModeCount() throws UaException;

  /**
   * Writes the Value of the SetPublishingModeCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSetPublishingModeCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readSetPublishingModeCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readSetPublishingModeCountAsync();

  /**
   * Asynchronous form of {@link #writeSetPublishingModeCount}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSetPublishingModeCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory CreateSubscriptionCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCreateSubscriptionCountNode() throws UaException;

  /** Asynchronous form of {@link #getCreateSubscriptionCountNode()}. */
  CompletableFuture<? extends VariableNode> getCreateSubscriptionCountNodeAsync();

  /**
   * Reads the Value of the CreateSubscriptionCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readCreateSubscriptionCount() throws UaException;

  /**
   * Writes the Value of the CreateSubscriptionCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCreateSubscriptionCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readCreateSubscriptionCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readCreateSubscriptionCountAsync();

  /**
   * Asynchronous form of {@link #writeCreateSubscriptionCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeCreateSubscriptionCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory ModifySubscriptionCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getModifySubscriptionCountNode() throws UaException;

  /** Asynchronous form of {@link #getModifySubscriptionCountNode()}. */
  CompletableFuture<? extends VariableNode> getModifySubscriptionCountNodeAsync();

  /**
   * Reads the Value of the ModifySubscriptionCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readModifySubscriptionCount() throws UaException;

  /**
   * Writes the Value of the ModifySubscriptionCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeModifySubscriptionCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readModifySubscriptionCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readModifySubscriptionCountAsync();

  /**
   * Asynchronous form of {@link #writeModifySubscriptionCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeModifySubscriptionCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory DeleteSubscriptionsCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDeleteSubscriptionsCountNode() throws UaException;

  /** Asynchronous form of {@link #getDeleteSubscriptionsCountNode()}. */
  CompletableFuture<? extends VariableNode> getDeleteSubscriptionsCountNodeAsync();

  /**
   * Reads the Value of the DeleteSubscriptionsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readDeleteSubscriptionsCount() throws UaException;

  /**
   * Writes the Value of the DeleteSubscriptionsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDeleteSubscriptionsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readDeleteSubscriptionsCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readDeleteSubscriptionsCountAsync();

  /**
   * Asynchronous form of {@link #writeDeleteSubscriptionsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDeleteSubscriptionsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory UnauthorizedRequestCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getUnauthorizedRequestCountNode() throws UaException;

  /** Asynchronous form of {@link #getUnauthorizedRequestCountNode()}. */
  CompletableFuture<? extends VariableNode> getUnauthorizedRequestCountNodeAsync();

  /**
   * Reads the Value of the UnauthorizedRequestCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readUnauthorizedRequestCount() throws UaException;

  /**
   * Writes the Value of the UnauthorizedRequestCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUnauthorizedRequestCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readUnauthorizedRequestCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readUnauthorizedRequestCountAsync();

  /**
   * Asynchronous form of {@link #writeUnauthorizedRequestCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeUnauthorizedRequestCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory CreateMonitoredItemsCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCreateMonitoredItemsCountNode() throws UaException;

  /** Asynchronous form of {@link #getCreateMonitoredItemsCountNode()}. */
  CompletableFuture<? extends VariableNode> getCreateMonitoredItemsCountNodeAsync();

  /**
   * Reads the Value of the CreateMonitoredItemsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readCreateMonitoredItemsCount() throws UaException;

  /**
   * Writes the Value of the CreateMonitoredItemsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCreateMonitoredItemsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readCreateMonitoredItemsCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readCreateMonitoredItemsCountAsync();

  /**
   * Asynchronous form of {@link #writeCreateMonitoredItemsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeCreateMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory CurrentSubscriptionsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCurrentSubscriptionsCountNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentSubscriptionsCountNode()}. */
  CompletableFuture<? extends VariableNode> getCurrentSubscriptionsCountNodeAsync();

  /**
   * Reads the Value of the CurrentSubscriptionsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readCurrentSubscriptionsCount() throws UaException;

  /**
   * Writes the Value of the CurrentSubscriptionsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentSubscriptionsCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readCurrentSubscriptionsCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentSubscriptionsCountAsync();

  /**
   * Asynchronous form of {@link #writeCurrentSubscriptionsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeCurrentSubscriptionsCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory DeleteMonitoredItemsCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDeleteMonitoredItemsCountNode() throws UaException;

  /** Asynchronous form of {@link #getDeleteMonitoredItemsCountNode()}. */
  CompletableFuture<? extends VariableNode> getDeleteMonitoredItemsCountNodeAsync();

  /**
   * Reads the Value of the DeleteMonitoredItemsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readDeleteMonitoredItemsCount() throws UaException;

  /**
   * Writes the Value of the DeleteMonitoredItemsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDeleteMonitoredItemsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readDeleteMonitoredItemsCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readDeleteMonitoredItemsCountAsync();

  /**
   * Asynchronous form of {@link #writeDeleteMonitoredItemsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDeleteMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory ModifyMonitoredItemsCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getModifyMonitoredItemsCountNode() throws UaException;

  /** Asynchronous form of {@link #getModifyMonitoredItemsCountNode()}. */
  CompletableFuture<? extends VariableNode> getModifyMonitoredItemsCountNodeAsync();

  /**
   * Reads the Value of the ModifyMonitoredItemsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readModifyMonitoredItemsCount() throws UaException;

  /**
   * Writes the Value of the ModifyMonitoredItemsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeModifyMonitoredItemsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readModifyMonitoredItemsCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readModifyMonitoredItemsCountAsync();

  /**
   * Asynchronous form of {@link #writeModifyMonitoredItemsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeModifyMonitoredItemsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory CurrentMonitoredItemsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCurrentMonitoredItemsCountNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentMonitoredItemsCountNode()}. */
  CompletableFuture<? extends VariableNode> getCurrentMonitoredItemsCountNodeAsync();

  /**
   * Reads the Value of the CurrentMonitoredItemsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readCurrentMonitoredItemsCount() throws UaException;

  /**
   * Writes the Value of the CurrentMonitoredItemsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentMonitoredItemsCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readCurrentMonitoredItemsCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentMonitoredItemsCountAsync();

  /**
   * Asynchronous form of {@link #writeCurrentMonitoredItemsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeCurrentMonitoredItemsCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory TransferSubscriptionsCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getTransferSubscriptionsCountNode() throws UaException;

  /** Asynchronous form of {@link #getTransferSubscriptionsCountNode()}. */
  CompletableFuture<? extends VariableNode> getTransferSubscriptionsCountNodeAsync();

  /**
   * Reads the Value of the TransferSubscriptionsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readTransferSubscriptionsCount() throws UaException;

  /**
   * Writes the Value of the TransferSubscriptionsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTransferSubscriptionsCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readTransferSubscriptionsCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readTransferSubscriptionsCountAsync();

  /**
   * Asynchronous form of {@link #writeTransferSubscriptionsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeTransferSubscriptionsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory CurrentPublishRequestsInQueue child, a BaseDataVariableType with
   * DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCurrentPublishRequestsInQueueNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentPublishRequestsInQueueNode()}. */
  CompletableFuture<? extends VariableNode> getCurrentPublishRequestsInQueueNodeAsync();

  /**
   * Reads the Value of the CurrentPublishRequestsInQueue child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readCurrentPublishRequestsInQueue() throws UaException;

  /**
   * Writes the Value of the CurrentPublishRequestsInQueue child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentPublishRequestsInQueue(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readCurrentPublishRequestsInQueue()}. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentPublishRequestsInQueueAsync();

  /**
   * Asynchronous form of {@link #writeCurrentPublishRequestsInQueue}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeCurrentPublishRequestsInQueueAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory TranslateBrowsePathsToNodeIdsCount child, a BaseDataVariableType with
   * DataType ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getTranslateBrowsePathsToNodeIdsCountNode() throws UaException;

  /** Asynchronous form of {@link #getTranslateBrowsePathsToNodeIdsCountNode()}. */
  CompletableFuture<? extends VariableNode> getTranslateBrowsePathsToNodeIdsCountNodeAsync();

  /**
   * Reads the Value of the TranslateBrowsePathsToNodeIdsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readTranslateBrowsePathsToNodeIdsCount() throws UaException;

  /**
   * Writes the Value of the TranslateBrowsePathsToNodeIdsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTranslateBrowsePathsToNodeIdsCount(@Nullable ServiceCounterDataType value)
      throws UaException;

  /** Asynchronous form of {@link #readTranslateBrowsePathsToNodeIdsCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType>
      readTranslateBrowsePathsToNodeIdsCountAsync();

  /**
   * Asynchronous form of {@link #writeTranslateBrowsePathsToNodeIdsCount}; completes with the
   * operation status.
   */
  CompletableFuture<StatusCode> writeTranslateBrowsePathsToNodeIdsCountAsync(
      @Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory CallCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCallCountNode() throws UaException;

  /** Asynchronous form of {@link #getCallCountNode()}. */
  CompletableFuture<? extends VariableNode> getCallCountNodeAsync();

  /**
   * Reads the Value of the CallCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readCallCount() throws UaException;

  /**
   * Writes the Value of the CallCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCallCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readCallCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readCallCountAsync();

  /** Asynchronous form of {@link #writeCallCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCallCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory LocaleIds child, a BaseDataVariableType with DataType LocaleId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getLocaleIdsNode() throws UaException;

  /** Asynchronous form of {@link #getLocaleIdsNode()}. */
  CompletableFuture<? extends VariableNode> getLocaleIdsNodeAsync();

  /**
   * Reads the Value of the LocaleIds child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readLocaleIds() throws UaException;

  /**
   * Writes the Value of the LocaleIds child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLocaleIds(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readLocaleIds()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readLocaleIdsAsync();

  /** Asynchronous form of {@link #writeLocaleIds}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLocaleIdsAsync(@Nullable String @Nullable [] value);

  /**
   * Resolves the mandatory ReadCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getReadCountNode() throws UaException;

  /** Asynchronous form of {@link #getReadCountNode()}. */
  CompletableFuture<? extends VariableNode> getReadCountNodeAsync();

  /**
   * Reads the Value of the ReadCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServiceCounterDataType readReadCount() throws UaException;

  /**
   * Writes the Value of the ReadCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReadCount(@Nullable ServiceCounterDataType value) throws UaException;

  /** Asynchronous form of {@link #readReadCount()}. */
  CompletableFuture<? extends @Nullable ServiceCounterDataType> readReadCountAsync();

  /** Asynchronous form of {@link #writeReadCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeReadCountAsync(@Nullable ServiceCounterDataType value);

  /**
   * Resolves the mandatory ServerUri child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getServerUriNode() throws UaException;

  /** Asynchronous form of {@link #getServerUriNode()}. */
  CompletableFuture<? extends VariableNode> getServerUriNodeAsync();

  /**
   * Reads the Value of the ServerUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readServerUri() throws UaException;

  /**
   * Writes the Value of the ServerUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readServerUri()}. */
  CompletableFuture<? extends @Nullable String> readServerUriAsync();

  /** Asynchronous form of {@link #writeServerUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServerUriAsync(@Nullable String value);

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
  @Nullable SessionDiagnosticsDataType readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable SessionDiagnosticsDataType value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable SessionDiagnosticsDataType> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable SessionDiagnosticsDataType value);
}
