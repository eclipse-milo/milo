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
import org.eclipse.milo.opcua.sdk.client.model.variables.SelectionListType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubConnectionType extends BaseObjectType {
  QualifiedProperty<Object> PUBLISHER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublisherId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<KeyValuePair[]> CONNECTION_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConnectionProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  /** Gets the existing node's local value. */
  @Nullable Object getPublisherId() throws UaException;

  /** Sets the existing node's local value. */
  void setPublisherId(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object readPublisherId() throws UaException;

  /** Writes the value remotely. */
  void writePublisherId(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object> readPublisherIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePublisherIdAsync(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPublisherIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPublisherIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable KeyValuePair @Nullable [] getConnectionProperties() throws UaException;

  /** Sets the existing node's local value. */
  void setConnectionProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable KeyValuePair @Nullable [] readConnectionProperties() throws UaException;

  /** Writes the value remotely. */
  void writeConnectionProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []> readConnectionPropertiesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConnectionPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConnectionPropertiesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getConnectionPropertiesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getTransportProfileUri() throws UaException;

  /** Sets the existing node's local value. */
  void setTransportProfileUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readTransportProfileUri() throws UaException;

  /** Writes the value remotely. */
  void writeTransportProfileUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readTransportProfileUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTransportProfileUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SelectionListType getTransportProfileUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SelectionListType> getTransportProfileUriNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  NetworkAddressType getAddressNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends NetworkAddressType> getAddressNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ConnectionTransportType getTransportSettingsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable ConnectionTransportType> getTransportSettingsNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubStatusType getStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PubSubStatusType> getStatusNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubDiagnosticsConnectionType getDiagnosticsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsConnectionType> getDiagnosticsNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddWriterGroupMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddWriterGroupMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3
   *
   * <p>Invokes <code>AddWriterGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callAddWriterGroup(@Nullable WriterGroupDataType configuration)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3
   *
   * <p>Invokes <code>AddWriterGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddWriterGroupAsync(
      @Nullable WriterGroupDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3
   *
   * <p>Invokes <code>AddWriterGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddWriterGroupDetailed(
      @Nullable WriterGroupDataType configuration) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3
   *
   * <p>Invokes <code>AddWriterGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddWriterGroupDetailed(
      MethodCallOptions options, @Nullable WriterGroupDataType configuration) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3
   *
   * <p>Invokes <code>AddWriterGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddWriterGroupDetailedAsync(@Nullable WriterGroupDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3
   *
   * <p>Invokes <code>AddWriterGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddWriterGroupDetailedAsync(
          MethodCallOptions options, @Nullable WriterGroupDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddReaderGroupMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddReaderGroupMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4
   *
   * <p>Invokes <code>AddReaderGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callAddReaderGroup(@Nullable ReaderGroupDataType configuration)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4
   *
   * <p>Invokes <code>AddReaderGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddReaderGroupAsync(
      @Nullable ReaderGroupDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4
   *
   * <p>Invokes <code>AddReaderGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddReaderGroupDetailed(
      @Nullable ReaderGroupDataType configuration) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4
   *
   * <p>Invokes <code>AddReaderGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddReaderGroupDetailed(
      MethodCallOptions options, @Nullable ReaderGroupDataType configuration) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4
   *
   * <p>Invokes <code>AddReaderGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddReaderGroupDetailedAsync(@Nullable ReaderGroupDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4
   *
   * <p>Invokes <code>AddReaderGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddReaderGroupDetailedAsync(
          MethodCallOptions options, @Nullable ReaderGroupDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveGroupMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveGroupMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5
   *
   * <p>Invokes <code>RemoveGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveGroup(@Nullable NodeId groupId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5
   *
   * <p>Invokes <code>RemoveGroup</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveGroupAsync(@Nullable NodeId groupId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5
   *
   * <p>Invokes <code>RemoveGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveGroupDetailed(@Nullable NodeId groupId)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5
   *
   * <p>Invokes <code>RemoveGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveGroupDetailed(
      MethodCallOptions options, @Nullable NodeId groupId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5
   *
   * <p>Invokes <code>RemoveGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveGroupDetailedAsync(@Nullable NodeId groupId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5
   *
   * <p>Invokes <code>RemoveGroup</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveGroupDetailedAsync(MethodCallOptions options, @Nullable NodeId groupId);
}
