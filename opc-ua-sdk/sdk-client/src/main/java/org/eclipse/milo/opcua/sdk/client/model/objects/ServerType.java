package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerStatusType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeGetMonitoredItems;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ServerType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.1">Model
 *     documentation</a>
 */
public interface ServerType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2004L);

  QualifiedProperty<String[]> ServerArray_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServerArray",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          1,
          String[].class);

  QualifiedProperty<UInteger> UrisVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UrisVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
          -1,
          UInteger.class);

  QualifiedProperty<UByte> ServiceLevel_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServiceLevel",
          ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
          -1,
          UByte.class);

  QualifiedProperty<String[]> NamespaceArray_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NamespaceArray",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          1,
          String[].class);

  QualifiedProperty<DateTime> EstimatedReturnTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EstimatedReturnTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
          -1,
          DateTime.class);

  QualifiedProperty<Boolean> Auditing_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Auditing",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<TimeZoneDataType> LocalTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LocalTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 8912L),
          -1,
          TimeZoneDataType.class);

  /**
   * Resolves the optional Namespaces child, a NamespacesType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.14">NamespacesType
   *     documentation</a>
   */
  @Nullable NamespacesType getNamespacesNode() throws UaException;

  /** Asynchronous form of {@link #getNamespacesNode()}. */
  CompletableFuture<? extends @Nullable NamespacesType> getNamespacesNodeAsync();

  /**
   * Resolves the mandatory ServerArray child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getServerArrayNode() throws UaException;

  /** Asynchronous form of {@link #getServerArrayNode()}. */
  CompletableFuture<? extends PropertyType> getServerArrayNodeAsync();

  /**
   * Reads the Value of the ServerArray child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readServerArray() throws UaException;

  /**
   * Writes the Value of the ServerArray child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerArray(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readServerArray()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readServerArrayAsync();

  /** Asynchronous form of {@link #writeServerArray}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServerArrayAsync(@Nullable String @Nullable [] value);

  /**
   * Resolves the optional UrisVersion child, a PropertyType with DataType VersionTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getUrisVersionNode() throws UaException;

  /** Asynchronous form of {@link #getUrisVersionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getUrisVersionNodeAsync();

  /**
   * Reads the Value of the UrisVersion child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readUrisVersion() throws UaException;

  /**
   * Writes the Value of the UrisVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUrisVersion(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readUrisVersion()}. */
  CompletableFuture<? extends @Nullable UInteger> readUrisVersionAsync();

  /** Asynchronous form of {@link #writeUrisVersion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUrisVersionAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory ServerStatus child, a ServerStatusType with DataType
   * ServerStatusDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6">ServerStatusType
   *     documentation</a>
   */
  ServerStatusType getServerStatusNode() throws UaException;

  /** Asynchronous form of {@link #getServerStatusNode()}. */
  CompletableFuture<? extends ServerStatusType> getServerStatusNodeAsync();

  /**
   * Reads the Value of the ServerStatus child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServerStatusDataType readServerStatus() throws UaException;

  /**
   * Writes the Value of the ServerStatus child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerStatus(@Nullable ServerStatusDataType value) throws UaException;

  /** Asynchronous form of {@link #readServerStatus()}. */
  CompletableFuture<? extends @Nullable ServerStatusDataType> readServerStatusAsync();

  /** Asynchronous form of {@link #writeServerStatus}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServerStatusAsync(@Nullable ServerStatusDataType value);

  /**
   * Resolves the mandatory ServiceLevel child, a PropertyType with DataType Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getServiceLevelNode() throws UaException;

  /** Asynchronous form of {@link #getServiceLevelNode()}. */
  CompletableFuture<? extends PropertyType> getServiceLevelNodeAsync();

  /**
   * Reads the Value of the ServiceLevel child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UByte readServiceLevel() throws UaException;

  /**
   * Writes the Value of the ServiceLevel child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServiceLevel(@Nullable UByte value) throws UaException;

  /** Asynchronous form of {@link #readServiceLevel()}. */
  CompletableFuture<? extends @Nullable UByte> readServiceLevelAsync();

  /** Asynchronous form of {@link #writeServiceLevel}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServiceLevelAsync(@Nullable UByte value);

  /**
   * Resolves the mandatory NamespaceArray child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNamespaceArrayNode() throws UaException;

  /** Asynchronous form of {@link #getNamespaceArrayNode()}. */
  CompletableFuture<? extends PropertyType> getNamespaceArrayNodeAsync();

  /**
   * Reads the Value of the NamespaceArray child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readNamespaceArray() throws UaException;

  /**
   * Writes the Value of the NamespaceArray child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNamespaceArray(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readNamespaceArray()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readNamespaceArrayAsync();

  /** Asynchronous form of {@link #writeNamespaceArray}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNamespaceArrayAsync(@Nullable String @Nullable [] value);

  /**
   * Resolves the mandatory ServerRedundancy child, a ServerRedundancyType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.7">ServerRedundancyType
   *     documentation</a>
   */
  ServerRedundancyType getServerRedundancyNode() throws UaException;

  /** Asynchronous form of {@link #getServerRedundancyNode()}. */
  CompletableFuture<? extends ServerRedundancyType> getServerRedundancyNodeAsync();

  /**
   * Resolves the mandatory VendorServerInfo child, a VendorServerInfoType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.6">VendorServerInfoType
   *     documentation</a>
   */
  VendorServerInfoType getVendorServerInfoNode() throws UaException;

  /** Asynchronous form of {@link #getVendorServerInfoNode()}. */
  CompletableFuture<? extends VendorServerInfoType> getVendorServerInfoNodeAsync();

  /**
   * Resolves the mandatory ServerDiagnostics child, a ServerDiagnosticsType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3">ServerDiagnosticsType
   *     documentation</a>
   */
  ServerDiagnosticsType getServerDiagnosticsNode() throws UaException;

  /** Asynchronous form of {@link #getServerDiagnosticsNode()}. */
  CompletableFuture<? extends ServerDiagnosticsType> getServerDiagnosticsNodeAsync();

  /**
   * Resolves the mandatory ServerCapabilities child, a ServerCapabilitiesType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.2">ServerCapabilitiesType
   *     documentation</a>
   */
  ServerCapabilitiesType getServerCapabilitiesNode() throws UaException;

  /** Asynchronous form of {@link #getServerCapabilitiesNode()}. */
  CompletableFuture<? extends ServerCapabilitiesType> getServerCapabilitiesNodeAsync();

  /**
   * Resolves the optional EstimatedReturnTime child, a PropertyType with DataType DateTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getEstimatedReturnTimeNode() throws UaException;

  /** Asynchronous form of {@link #getEstimatedReturnTimeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getEstimatedReturnTimeNodeAsync();

  /**
   * Reads the Value of the EstimatedReturnTime child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readEstimatedReturnTime() throws UaException;

  /**
   * Writes the Value of the EstimatedReturnTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEstimatedReturnTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readEstimatedReturnTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readEstimatedReturnTimeAsync();

  /**
   * Asynchronous form of {@link #writeEstimatedReturnTime}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeEstimatedReturnTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory Auditing child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAuditingNode() throws UaException;

  /** Asynchronous form of {@link #getAuditingNode()}. */
  CompletableFuture<? extends PropertyType> getAuditingNodeAsync();

  /**
   * Reads the Value of the Auditing child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readAuditing() throws UaException;

  /**
   * Writes the Value of the Auditing child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAuditing(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readAuditing()}. */
  CompletableFuture<? extends @Nullable Boolean> readAuditingAsync();

  /** Asynchronous form of {@link #writeAuditing}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAuditingAsync(@Nullable Boolean value);

  /**
   * Resolves the optional LocalTime child, a PropertyType with DataType TimeZoneDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getLocalTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLocalTimeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getLocalTimeNodeAsync();

  /**
   * Reads the Value of the LocalTime child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable TimeZoneDataType readLocalTime() throws UaException;

  /**
   * Writes the Value of the LocalTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLocalTime(@Nullable TimeZoneDataType value) throws UaException;

  /** Asynchronous form of {@link #readLocalTime()}. */
  CompletableFuture<? extends @Nullable TimeZoneDataType> readLocalTimeAsync();

  /** Asynchronous form of {@link #writeLocalTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLocalTimeAsync(@Nullable TimeZoneDataType value);

  /**
   * Resolves the optional GetMonitoredItems Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetMonitoredItemsMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGetMonitoredItemsMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getGetMonitoredItemsMethodNodeAsync();

  /**
   * Calls the GetMonitoredItems Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1">Model
   *     documentation</a>
   */
  ServerTypeGetMonitoredItems.Outputs getMonitoredItems(@Nullable UInteger subscriptionId)
      throws UaException;

  /**
   * Calls the GetMonitoredItems Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<ServerTypeGetMonitoredItems.Outputs> callGetMonitoredItems(
      @Nullable UInteger subscriptionId) throws UaException;

  /**
   * Calls the GetMonitoredItems Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<ServerTypeGetMonitoredItems.Outputs> callGetMonitoredItemsWith(
      MethodCallOptions options, @Nullable UInteger subscriptionId) throws UaException;

  /** Asynchronous form of {@link #getMonitoredItems}. */
  CompletableFuture<ServerTypeGetMonitoredItems.Outputs> getMonitoredItemsAsync(
      @Nullable UInteger subscriptionId);

  /** Asynchronous form of {@link #callGetMonitoredItems}. */
  CompletableFuture<MethodCallResult<ServerTypeGetMonitoredItems.Outputs>>
      callGetMonitoredItemsAsync(@Nullable UInteger subscriptionId);

  /** Asynchronous form of {@link #callGetMonitoredItemsWith}. */
  CompletableFuture<MethodCallResult<ServerTypeGetMonitoredItems.Outputs>>
      callGetMonitoredItemsWithAsync(MethodCallOptions options, @Nullable UInteger subscriptionId);

  /**
   * Resolves the optional RequestServerStateChange Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRequestServerStateChangeMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRequestServerStateChangeMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRequestServerStateChangeMethodNodeAsync();

  /**
   * Calls the RequestServerStateChange Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4">Model
   *     documentation</a>
   */
  void requestServerStateChange(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException;

  /**
   * Calls the RequestServerStateChange Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRequestServerStateChange(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException;

  /**
   * Calls the RequestServerStateChange Method with explicit options and returns the complete
   * result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRequestServerStateChangeWith(
      MethodCallOptions options,
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException;

  /** Asynchronous form of {@link #requestServerStateChange}. */
  CompletableFuture<Void> requestServerStateChangeAsync(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart);

  /** Asynchronous form of {@link #callRequestServerStateChange}. */
  CompletableFuture<MethodCallResult<Void>> callRequestServerStateChangeAsync(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart);

  /** Asynchronous form of {@link #callRequestServerStateChangeWith}. */
  CompletableFuture<MethodCallResult<Void>> callRequestServerStateChangeWithAsync(
      MethodCallOptions options,
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart);

  /**
   * Resolves the optional ResendData Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getResendDataMethodNode() throws UaException;

  /** Asynchronous form of {@link #getResendDataMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getResendDataMethodNodeAsync();

  /**
   * Calls the ResendData Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2">Model
   *     documentation</a>
   */
  void resendData(@Nullable UInteger subscriptionId) throws UaException;

  /**
   * Calls the ResendData Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callResendData(@Nullable UInteger subscriptionId) throws UaException;

  /**
   * Calls the ResendData Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callResendDataWith(
      MethodCallOptions options, @Nullable UInteger subscriptionId) throws UaException;

  /** Asynchronous form of {@link #resendData}. */
  CompletableFuture<Void> resendDataAsync(@Nullable UInteger subscriptionId);

  /** Asynchronous form of {@link #callResendData}. */
  CompletableFuture<MethodCallResult<Void>> callResendDataAsync(@Nullable UInteger subscriptionId);

  /** Asynchronous form of {@link #callResendDataWith}. */
  CompletableFuture<MethodCallResult<Void>> callResendDataWithAsync(
      MethodCallOptions options, @Nullable UInteger subscriptionId);

  /**
   * Resolves the optional SetSubscriptionDurable Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSetSubscriptionDurableMethodNode() throws UaException;

  /** Asynchronous form of {@link #getSetSubscriptionDurableMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getSetSubscriptionDurableMethodNodeAsync();

  /**
   * Calls the SetSubscriptionDurable Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3">Model
   *     documentation</a>
   */
  @Nullable UInteger setSubscriptionDurable(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) throws UaException;

  /**
   * Calls the SetSubscriptionDurable Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable UInteger> callSetSubscriptionDurable(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) throws UaException;

  /**
   * Calls the SetSubscriptionDurable Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable UInteger> callSetSubscriptionDurableWith(
      MethodCallOptions options,
      @Nullable UInteger subscriptionId,
      @Nullable UInteger lifetimeInHours)
      throws UaException;

  /** Asynchronous form of {@link #setSubscriptionDurable}. */
  CompletableFuture<@Nullable UInteger> setSubscriptionDurableAsync(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours);

  /** Asynchronous form of {@link #callSetSubscriptionDurable}. */
  CompletableFuture<MethodCallResult<@Nullable UInteger>> callSetSubscriptionDurableAsync(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours);

  /** Asynchronous form of {@link #callSetSubscriptionDurableWith}. */
  CompletableFuture<MethodCallResult<@Nullable UInteger>> callSetSubscriptionDurableWithAsync(
      MethodCallOptions options,
      @Nullable UInteger subscriptionId,
      @Nullable UInteger lifetimeInHours);
}
