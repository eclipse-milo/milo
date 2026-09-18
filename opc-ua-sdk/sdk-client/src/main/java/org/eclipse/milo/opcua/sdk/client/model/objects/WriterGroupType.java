package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the WriterGroupType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3">Model
 *     documentation</a>
 */
public interface WriterGroupType extends PubSubGroupType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17725L);

  QualifiedProperty<Double> KeepAliveTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "KeepAliveTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<UShort> WriterGroupId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "WriterGroupId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<String> HeaderLayoutUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "HeaderLayoutUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<Double> PublishingInterval_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PublishingInterval",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<UByte> Priority_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Priority", ExpandedNodeId.of(Namespaces.OPC_UA, 3L), -1, UByte.class);

  QualifiedProperty<String[]> LocaleIds_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LocaleIds",
          ExpandedNodeId.of(Namespaces.OPC_UA, 295L),
          1,
          String[].class);

  /**
   * Resolves the optional Diagnostics child, a PubSubDiagnosticsWriterGroupType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.9">PubSubDiagnosticsWriterGroupType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsWriterGroupType getDiagnosticsNode() throws UaException;

  /** Asynchronous form of {@link #getDiagnosticsNode()}. */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsWriterGroupType> getDiagnosticsNodeAsync();

  /**
   * Resolves the mandatory KeepAliveTime child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getKeepAliveTimeNode() throws UaException;

  /** Asynchronous form of {@link #getKeepAliveTimeNode()}. */
  CompletableFuture<? extends PropertyType> getKeepAliveTimeNodeAsync();

  /**
   * Reads the Value of the KeepAliveTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readKeepAliveTime() throws UaException;

  /**
   * Writes the Value of the KeepAliveTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeKeepAliveTime(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readKeepAliveTime()}. */
  CompletableFuture<? extends @Nullable Double> readKeepAliveTimeAsync();

  /** Asynchronous form of {@link #writeKeepAliveTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeKeepAliveTimeAsync(@Nullable Double value);

  /**
   * Resolves the mandatory WriterGroupId child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getWriterGroupIdNode() throws UaException;

  /** Asynchronous form of {@link #getWriterGroupIdNode()}. */
  CompletableFuture<? extends PropertyType> getWriterGroupIdNodeAsync();

  /**
   * Reads the Value of the WriterGroupId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readWriterGroupId() throws UaException;

  /**
   * Writes the Value of the WriterGroupId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeWriterGroupId(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readWriterGroupId()}. */
  CompletableFuture<? extends @Nullable UShort> readWriterGroupIdAsync();

  /** Asynchronous form of {@link #writeWriterGroupId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeWriterGroupIdAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory HeaderLayoutUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getHeaderLayoutUriNode() throws UaException;

  /** Asynchronous form of {@link #getHeaderLayoutUriNode()}. */
  CompletableFuture<? extends PropertyType> getHeaderLayoutUriNodeAsync();

  /**
   * Reads the Value of the HeaderLayoutUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readHeaderLayoutUri() throws UaException;

  /**
   * Writes the Value of the HeaderLayoutUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHeaderLayoutUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readHeaderLayoutUri()}. */
  CompletableFuture<? extends @Nullable String> readHeaderLayoutUriAsync();

  /** Asynchronous form of {@link #writeHeaderLayoutUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeHeaderLayoutUriAsync(@Nullable String value);

  /**
   * Resolves the optional MessageSettings child, a WriterGroupMessageType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.8">WriterGroupMessageType
   *     documentation</a>
   */
  @Nullable WriterGroupMessageType getMessageSettingsNode() throws UaException;

  /** Asynchronous form of {@link #getMessageSettingsNode()}. */
  CompletableFuture<? extends @Nullable WriterGroupMessageType> getMessageSettingsNodeAsync();

  /**
   * Resolves the optional TransportSettings child, a WriterGroupTransportType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.7">WriterGroupTransportType
   *     documentation</a>
   */
  @Nullable WriterGroupTransportType getTransportSettingsNode() throws UaException;

  /** Asynchronous form of {@link #getTransportSettingsNode()}. */
  CompletableFuture<? extends @Nullable WriterGroupTransportType> getTransportSettingsNodeAsync();

  /**
   * Resolves the mandatory PublishingInterval child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPublishingIntervalNode() throws UaException;

  /** Asynchronous form of {@link #getPublishingIntervalNode()}. */
  CompletableFuture<? extends PropertyType> getPublishingIntervalNodeAsync();

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
   * Resolves the mandatory Priority child, a PropertyType with DataType Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPriorityNode() throws UaException;

  /** Asynchronous form of {@link #getPriorityNode()}. */
  CompletableFuture<? extends PropertyType> getPriorityNodeAsync();

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
   * Resolves the mandatory LocaleIds child, a PropertyType with DataType LocaleId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLocaleIdsNode() throws UaException;

  /** Asynchronous form of {@link #getLocaleIdsNode()}. */
  CompletableFuture<? extends PropertyType> getLocaleIdsNodeAsync();

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
   * Resolves the optional AddDataSetWriter Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddDataSetWriterMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddDataSetWriterMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddDataSetWriterMethodNodeAsync();

  /**
   * Calls the AddDataSetWriter Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4">Model
   *     documentation</a>
   */
  @Nullable NodeId addDataSetWriter(@Nullable DataSetWriterDataType configuration)
      throws UaException;

  /**
   * Calls the AddDataSetWriter Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddDataSetWriter(
      @Nullable DataSetWriterDataType configuration) throws UaException;

  /**
   * Calls the AddDataSetWriter Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddDataSetWriterWith(
      MethodCallOptions options, @Nullable DataSetWriterDataType configuration) throws UaException;

  /** Asynchronous form of {@link #addDataSetWriter}. */
  CompletableFuture<@Nullable NodeId> addDataSetWriterAsync(
      @Nullable DataSetWriterDataType configuration);

  /** Asynchronous form of {@link #callAddDataSetWriter}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetWriterAsync(
      @Nullable DataSetWriterDataType configuration);

  /** Asynchronous form of {@link #callAddDataSetWriterWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetWriterWithAsync(
      MethodCallOptions options, @Nullable DataSetWriterDataType configuration);

  /**
   * Resolves the optional RemoveDataSetWriter Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveDataSetWriterMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveDataSetWriterMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveDataSetWriterMethodNodeAsync();

  /**
   * Calls the RemoveDataSetWriter Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5">Model
   *     documentation</a>
   */
  void removeDataSetWriter(@Nullable NodeId dataSetWriterNodeId) throws UaException;

  /**
   * Calls the RemoveDataSetWriter Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveDataSetWriter(@Nullable NodeId dataSetWriterNodeId)
      throws UaException;

  /**
   * Calls the RemoveDataSetWriter Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveDataSetWriterWith(
      MethodCallOptions options, @Nullable NodeId dataSetWriterNodeId) throws UaException;

  /** Asynchronous form of {@link #removeDataSetWriter}. */
  CompletableFuture<Void> removeDataSetWriterAsync(@Nullable NodeId dataSetWriterNodeId);

  /** Asynchronous form of {@link #callRemoveDataSetWriter}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveDataSetWriterAsync(
      @Nullable NodeId dataSetWriterNodeId);

  /** Asynchronous form of {@link #callRemoveDataSetWriterWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveDataSetWriterWithAsync(
      MethodCallOptions options, @Nullable NodeId dataSetWriterNodeId);
}
