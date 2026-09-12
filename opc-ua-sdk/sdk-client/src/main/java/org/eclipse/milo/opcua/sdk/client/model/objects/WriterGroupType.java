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
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface WriterGroupType extends PubSubGroupType {
  QualifiedProperty<UShort> WRITER_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "WriterGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<Double> PUBLISHING_INTERVAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishingInterval",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> KEEP_ALIVE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeepAliveTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UByte> PRIORITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Priority",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          -1,
          UByte.class);

  QualifiedProperty<String[]> LOCALE_IDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LocaleIds",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=295"),
          1,
          String[].class);

  QualifiedProperty<String> HEADER_LAYOUT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HeaderLayoutUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable UShort getWriterGroupId() throws UaException;

  /** Sets the existing node's local value. */
  void setWriterGroupId(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readWriterGroupId() throws UaException;

  /** Writes the value remotely. */
  void writeWriterGroupId(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readWriterGroupIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeWriterGroupIdAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getWriterGroupIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getWriterGroupIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getPublishingInterval() throws UaException;

  /** Sets the existing node's local value. */
  void setPublishingInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readPublishingInterval() throws UaException;

  /** Writes the value remotely. */
  void writePublishingInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readPublishingIntervalAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePublishingIntervalAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPublishingIntervalNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPublishingIntervalNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getKeepAliveTime() throws UaException;

  /** Sets the existing node's local value. */
  void setKeepAliveTime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readKeepAliveTime() throws UaException;

  /** Writes the value remotely. */
  void writeKeepAliveTime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readKeepAliveTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeKeepAliveTimeAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getKeepAliveTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getKeepAliveTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UByte getPriority() throws UaException;

  /** Sets the existing node's local value. */
  void setPriority(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte readPriority() throws UaException;

  /** Writes the value remotely. */
  void writePriority(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte> readPriorityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePriorityAsync(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPriorityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPriorityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getLocaleIds() throws UaException;

  /** Sets the existing node's local value. */
  void setLocaleIds(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readLocaleIds() throws UaException;

  /** Writes the value remotely. */
  void writeLocaleIds(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readLocaleIdsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLocaleIdsAsync(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLocaleIdsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLocaleIdsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getHeaderLayoutUri() throws UaException;

  /** Sets the existing node's local value. */
  void setHeaderLayoutUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readHeaderLayoutUri() throws UaException;

  /** Writes the value remotely. */
  void writeHeaderLayoutUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readHeaderLayoutUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHeaderLayoutUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getHeaderLayoutUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getHeaderLayoutUriNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable WriterGroupTransportType getTransportSettingsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable WriterGroupTransportType> getTransportSettingsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable WriterGroupMessageType getMessageSettingsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable WriterGroupMessageType> getMessageSettingsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubDiagnosticsWriterGroupType getDiagnosticsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsWriterGroupType> getDiagnosticsNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddDataSetWriterMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddDataSetWriterMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4
   *
   * <p>Invokes <code>AddDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callAddDataSetWriter(@Nullable DataSetWriterDataType configuration)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4
   *
   * <p>Invokes <code>AddDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddDataSetWriterAsync(
      @Nullable DataSetWriterDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4
   *
   * <p>Invokes <code>AddDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddDataSetWriterDetailed(
      @Nullable DataSetWriterDataType configuration) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4
   *
   * <p>Invokes <code>AddDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddDataSetWriterDetailed(
      MethodCallOptions options, @Nullable DataSetWriterDataType configuration) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4
   *
   * <p>Invokes <code>AddDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddDataSetWriterDetailedAsync(@Nullable DataSetWriterDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4
   *
   * <p>Invokes <code>AddDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddDataSetWriterDetailedAsync(
          MethodCallOptions options, @Nullable DataSetWriterDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveDataSetWriterMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveDataSetWriterMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5
   *
   * <p>Invokes <code>RemoveDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveDataSetWriter(@Nullable NodeId dataSetWriterNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5
   *
   * <p>Invokes <code>RemoveDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveDataSetWriterAsync(
      @Nullable NodeId dataSetWriterNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5
   *
   * <p>Invokes <code>RemoveDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveDataSetWriterDetailed(
      @Nullable NodeId dataSetWriterNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5
   *
   * <p>Invokes <code>RemoveDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveDataSetWriterDetailed(
      MethodCallOptions options, @Nullable NodeId dataSetWriterNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5
   *
   * <p>Invokes <code>RemoveDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveDataSetWriterDetailedAsync(@Nullable NodeId dataSetWriterNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5
   *
   * <p>Invokes <code>RemoveDataSetWriter</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveDataSetWriterDetailedAsync(
          MethodCallOptions options, @Nullable NodeId dataSetWriterNodeId);
}
