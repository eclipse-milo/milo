/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerStatusType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeGetMonitoredItemsOutputs;
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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ServerType extends BaseObjectType {
  QualifiedProperty<String[]> SERVER_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<String[]> NAMESPACE_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NamespaceArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<UInteger> URIS_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UrisVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<UByte> SERVICE_LEVEL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServiceLevel",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          -1,
          UByte.class);

  QualifiedProperty<Boolean> AUDITING =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Auditing",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<DateTime> ESTIMATED_RETURN_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EstimatedReturnTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  QualifiedProperty<TimeZoneDataType> LOCAL_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LocalTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=8912"),
          -1,
          TimeZoneDataType.class);

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getServerArray() throws UaException;

  /** Sets the existing node's local value. */
  void setServerArray(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readServerArray() throws UaException;

  /** Writes the value remotely. */
  void writeServerArray(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readServerArrayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerArrayAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServerArrayNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getServerArrayNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getNamespaceArray() throws UaException;

  /** Sets the existing node's local value. */
  void setNamespaceArray(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readNamespaceArray() throws UaException;

  /** Writes the value remotely. */
  void writeNamespaceArray(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readNamespaceArrayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNamespaceArrayAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNamespaceArrayNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getNamespaceArrayNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getUrisVersion() throws UaException;

  /** Sets the existing node's local value. */
  void setUrisVersion(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readUrisVersion() throws UaException;

  /** Writes the value remotely. */
  void writeUrisVersion(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readUrisVersionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUrisVersionAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getUrisVersionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getUrisVersionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UByte getServiceLevel() throws UaException;

  /** Sets the existing node's local value. */
  void setServiceLevel(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte readServiceLevel() throws UaException;

  /** Writes the value remotely. */
  void writeServiceLevel(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte> readServiceLevelAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServiceLevelAsync(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServiceLevelNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getServiceLevelNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getAuditing() throws UaException;

  /** Sets the existing node's local value. */
  void setAuditing(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readAuditing() throws UaException;

  /** Writes the value remotely. */
  void writeAuditing(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readAuditingAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAuditingAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAuditingNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAuditingNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getEstimatedReturnTime() throws UaException;

  /** Sets the existing node's local value. */
  void setEstimatedReturnTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readEstimatedReturnTime() throws UaException;

  /** Writes the value remotely. */
  void writeEstimatedReturnTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readEstimatedReturnTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEstimatedReturnTimeAsync(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEstimatedReturnTimeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getEstimatedReturnTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable TimeZoneDataType getLocalTime() throws UaException;

  /** Sets the existing node's local value. */
  void setLocalTime(@Nullable TimeZoneDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable TimeZoneDataType readLocalTime() throws UaException;

  /** Writes the value remotely. */
  void writeLocalTime(@Nullable TimeZoneDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable TimeZoneDataType> readLocalTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLocalTimeAsync(@Nullable TimeZoneDataType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLocalTimeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getLocalTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServerStatusDataType getServerStatus() throws UaException;

  /** Sets the existing node's local value. */
  void setServerStatus(@Nullable ServerStatusDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServerStatusDataType readServerStatus() throws UaException;

  /** Writes the value remotely. */
  void writeServerStatus(@Nullable ServerStatusDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServerStatusDataType> readServerStatusAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerStatusAsync(@Nullable ServerStatusDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ServerStatusType getServerStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends ServerStatusType> getServerStatusNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ServerCapabilitiesType getServerCapabilitiesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends ServerCapabilitiesType> getServerCapabilitiesNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ServerDiagnosticsType getServerDiagnosticsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends ServerDiagnosticsType> getServerDiagnosticsNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  VendorServerInfoType getVendorServerInfoNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends VendorServerInfoType> getVendorServerInfoNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ServerRedundancyType getServerRedundancyNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends ServerRedundancyType> getServerRedundancyNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable NamespacesType getNamespacesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable NamespacesType> getNamespacesNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getGetMonitoredItemsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getGetMonitoredItemsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  ServerTypeGetMonitoredItemsOutputs callGetMonitoredItems(@Nullable UInteger subscriptionId)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends ServerTypeGetMonitoredItemsOutputs> callGetMonitoredItemsAsync(
      @Nullable UInteger subscriptionId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends ServerTypeGetMonitoredItemsOutputs> callGetMonitoredItemsDetailed(
      @Nullable UInteger subscriptionId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends ServerTypeGetMonitoredItemsOutputs> callGetMonitoredItemsDetailed(
      MethodCallOptions options, @Nullable UInteger subscriptionId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends ServerTypeGetMonitoredItemsOutputs>>
      callGetMonitoredItemsDetailedAsync(@Nullable UInteger subscriptionId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends ServerTypeGetMonitoredItemsOutputs>>
      callGetMonitoredItemsDetailedAsync(
          MethodCallOptions options, @Nullable UInteger subscriptionId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getResendDataMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getResendDataMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callResendData(@Nullable UInteger subscriptionId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callResendDataAsync(
      @Nullable UInteger subscriptionId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResendDataDetailed(
      @Nullable UInteger subscriptionId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResendDataDetailed(
      MethodCallOptions options, @Nullable UInteger subscriptionId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callResendDataDetailedAsync(@Nullable UInteger subscriptionId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callResendDataDetailedAsync(MethodCallOptions options, @Nullable UInteger subscriptionId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getSetSubscriptionDurableMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getSetSubscriptionDurableMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable UInteger callSetSubscriptionDurable(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UInteger> callSetSubscriptionDurableAsync(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable UInteger> callSetSubscriptionDurableDetailed(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable UInteger> callSetSubscriptionDurableDetailed(
      MethodCallOptions options,
      @Nullable UInteger subscriptionId,
      @Nullable UInteger lifetimeInHours)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable UInteger>>
      callSetSubscriptionDurableDetailedAsync(
          @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable UInteger>>
      callSetSubscriptionDurableDetailedAsync(
          MethodCallOptions options,
          @Nullable UInteger subscriptionId,
          @Nullable UInteger lifetimeInHours);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRequestServerStateChangeMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRequestServerStateChangeMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRequestServerStateChange(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRequestServerStateChangeAsync(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRequestServerStateChangeDetailed(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRequestServerStateChangeDetailed(
      MethodCallOptions options,
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRequestServerStateChangeDetailedAsync(
          @Nullable ServerState state,
          @Nullable DateTime estimatedReturnTime,
          @Nullable UInteger secondsTillShutdown,
          @Nullable LocalizedText reason,
          @Nullable Boolean restart);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRequestServerStateChangeDetailedAsync(
          MethodCallOptions options,
          @Nullable ServerState state,
          @Nullable DateTime estimatedReturnTime,
          @Nullable UInteger secondsTillShutdown,
          @Nullable LocalizedText reason,
          @Nullable Boolean restart);
}
