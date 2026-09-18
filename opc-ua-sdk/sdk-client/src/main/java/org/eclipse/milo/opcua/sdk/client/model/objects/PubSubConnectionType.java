package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SelectionListType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubConnectionType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.2">Model
 *     documentation</a>
 */
public interface PubSubConnectionType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14209L);

  QualifiedProperty<Variant> PublisherId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PublisherId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          -1,
          Variant.class);

  QualifiedProperty<KeyValuePair[]> ConnectionProperties_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConnectionProperties",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
          1,
          KeyValuePair[].class);

  /**
   * Resolves the optional Diagnostics child, a PubSubDiagnosticsConnectionType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.8">PubSubDiagnosticsConnectionType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsConnectionType getDiagnosticsNode() throws UaException;

  /** Asynchronous form of {@link #getDiagnosticsNode()}. */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsConnectionType> getDiagnosticsNodeAsync();

  /**
   * Resolves the mandatory PublisherId child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPublisherIdNode() throws UaException;

  /** Asynchronous form of {@link #getPublisherIdNode()}. */
  CompletableFuture<? extends PropertyType> getPublisherIdNodeAsync();

  /**
   * Reads the Value of the PublisherId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readPublisherId() throws UaException;

  /**
   * Writes the Value of the PublisherId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePublisherId(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readPublisherId()}. */
  CompletableFuture<? extends @Nullable Variant> readPublisherIdAsync();

  /** Asynchronous form of {@link #writePublisherId}; completes with the operation status. */
  CompletableFuture<StatusCode> writePublisherIdAsync(@Nullable Variant value);

  /**
   * Resolves the optional TransportSettings child, a ConnectionTransportType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.8">ConnectionTransportType
   *     documentation</a>
   */
  @Nullable ConnectionTransportType getTransportSettingsNode() throws UaException;

  /** Asynchronous form of {@link #getTransportSettingsNode()}. */
  CompletableFuture<? extends @Nullable ConnectionTransportType> getTransportSettingsNodeAsync();

  /**
   * Resolves the mandatory TransportProfileUri child, a SelectionListType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18">SelectionListType
   *     documentation</a>
   */
  SelectionListType getTransportProfileUriNode() throws UaException;

  /** Asynchronous form of {@link #getTransportProfileUriNode()}. */
  CompletableFuture<? extends SelectionListType> getTransportProfileUriNodeAsync();

  /**
   * Reads the Value of the TransportProfileUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readTransportProfileUri() throws UaException;

  /**
   * Writes the Value of the TransportProfileUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTransportProfileUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readTransportProfileUri()}. */
  CompletableFuture<? extends @Nullable String> readTransportProfileUriAsync();

  /**
   * Asynchronous form of {@link #writeTransportProfileUri}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeTransportProfileUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory ConnectionProperties child, a PropertyType with DataType KeyValuePair.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getConnectionPropertiesNode() throws UaException;

  /** Asynchronous form of {@link #getConnectionPropertiesNode()}. */
  CompletableFuture<? extends PropertyType> getConnectionPropertiesNodeAsync();

  /**
   * Reads the Value of the ConnectionProperties child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable KeyValuePair @Nullable [] readConnectionProperties() throws UaException;

  /**
   * Writes the Value of the ConnectionProperties child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConnectionProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readConnectionProperties()}. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []> readConnectionPropertiesAsync();

  /**
   * Asynchronous form of {@link #writeConnectionProperties}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeConnectionPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Resolves the mandatory Status child, a PubSubStatusType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">PubSubStatusType
   *     documentation</a>
   */
  PubSubStatusType getStatusNode() throws UaException;

  /** Asynchronous form of {@link #getStatusNode()}. */
  CompletableFuture<? extends PubSubStatusType> getStatusNodeAsync();

  /**
   * Resolves the mandatory Address child, a NetworkAddressType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.6">NetworkAddressType
   *     documentation</a>
   */
  NetworkAddressType getAddressNode() throws UaException;

  /** Asynchronous form of {@link #getAddressNode()}. */
  CompletableFuture<? extends NetworkAddressType> getAddressNodeAsync();

  /**
   * Resolves the optional AddReaderGroup Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddReaderGroupMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddReaderGroupMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddReaderGroupMethodNodeAsync();

  /**
   * Calls the AddReaderGroup Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4">Model
   *     documentation</a>
   */
  @Nullable NodeId addReaderGroup(@Nullable ReaderGroupDataType configuration) throws UaException;

  /**
   * Calls the AddReaderGroup Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddReaderGroup(@Nullable ReaderGroupDataType configuration)
      throws UaException;

  /**
   * Calls the AddReaderGroup Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddReaderGroupWith(
      MethodCallOptions options, @Nullable ReaderGroupDataType configuration) throws UaException;

  /** Asynchronous form of {@link #addReaderGroup}. */
  CompletableFuture<@Nullable NodeId> addReaderGroupAsync(
      @Nullable ReaderGroupDataType configuration);

  /** Asynchronous form of {@link #callAddReaderGroup}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddReaderGroupAsync(
      @Nullable ReaderGroupDataType configuration);

  /** Asynchronous form of {@link #callAddReaderGroupWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddReaderGroupWithAsync(
      MethodCallOptions options, @Nullable ReaderGroupDataType configuration);

  /**
   * Resolves the optional AddWriterGroup Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddWriterGroupMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddWriterGroupMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddWriterGroupMethodNodeAsync();

  /**
   * Calls the AddWriterGroup Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3">Model
   *     documentation</a>
   */
  @Nullable NodeId addWriterGroup(@Nullable WriterGroupDataType configuration) throws UaException;

  /**
   * Calls the AddWriterGroup Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddWriterGroup(@Nullable WriterGroupDataType configuration)
      throws UaException;

  /**
   * Calls the AddWriterGroup Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddWriterGroupWith(
      MethodCallOptions options, @Nullable WriterGroupDataType configuration) throws UaException;

  /** Asynchronous form of {@link #addWriterGroup}. */
  CompletableFuture<@Nullable NodeId> addWriterGroupAsync(
      @Nullable WriterGroupDataType configuration);

  /** Asynchronous form of {@link #callAddWriterGroup}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddWriterGroupAsync(
      @Nullable WriterGroupDataType configuration);

  /** Asynchronous form of {@link #callAddWriterGroupWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddWriterGroupWithAsync(
      MethodCallOptions options, @Nullable WriterGroupDataType configuration);

  /**
   * Resolves the optional RemoveGroup Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveGroupMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveGroupMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveGroupMethodNodeAsync();

  /**
   * Calls the RemoveGroup Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5">Model
   *     documentation</a>
   */
  void removeGroup(@Nullable NodeId groupId) throws UaException;

  /**
   * Calls the RemoveGroup Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveGroup(@Nullable NodeId groupId) throws UaException;

  /**
   * Calls the RemoveGroup Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveGroupWith(MethodCallOptions options, @Nullable NodeId groupId)
      throws UaException;

  /** Asynchronous form of {@link #removeGroup}. */
  CompletableFuture<Void> removeGroupAsync(@Nullable NodeId groupId);

  /** Asynchronous form of {@link #callRemoveGroup}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveGroupAsync(@Nullable NodeId groupId);

  /** Asynchronous form of {@link #callRemoveGroupWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveGroupWithAsync(
      MethodCallOptions options, @Nullable NodeId groupId);
}
