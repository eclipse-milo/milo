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
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PublishedEventsType extends PublishedDataSetType {
  QualifiedProperty<NodeId> PUB_SUB_EVENT_NOTIFIER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EventNotifier",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<SimpleAttributeOperand[]> SELECTED_FIELDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SelectedFields",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=601"),
          1,
          SimpleAttributeOperand[].class);

  QualifiedProperty<ContentFilter> FILTER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Filter",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=586"),
          -1,
          ContentFilter.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId getPubSubEventNotifier() throws UaException;

  /** Sets the existing node's local value. */
  void setPubSubEventNotifier(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readPubSubEventNotifier() throws UaException;

  /** Writes the value remotely. */
  void writePubSubEventNotifier(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readPubSubEventNotifierAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePubSubEventNotifierAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPubSubEventNotifierNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPubSubEventNotifierNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable SimpleAttributeOperand @Nullable [] getSelectedFields() throws UaException;

  /** Sets the existing node's local value. */
  void setSelectedFields(@Nullable SimpleAttributeOperand @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SimpleAttributeOperand @Nullable [] readSelectedFields() throws UaException;

  /** Writes the value remotely. */
  void writeSelectedFields(@Nullable SimpleAttributeOperand @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SimpleAttributeOperand @Nullable []>
      readSelectedFieldsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSelectedFieldsAsync(
      @Nullable SimpleAttributeOperand @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSelectedFieldsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSelectedFieldsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ContentFilter getFilter() throws UaException;

  /** Sets the existing node's local value. */
  void setFilter(@Nullable ContentFilter value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ContentFilter readFilter() throws UaException;

  /** Writes the value remotely. */
  void writeFilter(@Nullable ContentFilter value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ContentFilter> readFilterAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeFilterAsync(@Nullable ContentFilter value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getFilterNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getFilterNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getModifyFieldSelectionMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getModifyFieldSelectionMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Invokes <code>ModifyFieldSelection</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable ConfigurationVersionDataType callModifyFieldSelection(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      @Nullable Boolean @Nullable [] promotedFields,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Invokes <code>ModifyFieldSelection</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable ConfigurationVersionDataType> callModifyFieldSelectionAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable String @Nullable [] fieldNameAliases,
      @Nullable Boolean @Nullable [] promotedFields,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Invokes <code>ModifyFieldSelection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ConfigurationVersionDataType>
      callModifyFieldSelectionDetailed(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable Boolean @Nullable [] promotedFields,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Invokes <code>ModifyFieldSelection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ConfigurationVersionDataType>
      callModifyFieldSelectionDetailed(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable Boolean @Nullable [] promotedFields,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Invokes <code>ModifyFieldSelection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ConfigurationVersionDataType>>
      callModifyFieldSelectionDetailedAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable Boolean @Nullable [] promotedFields,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Invokes <code>ModifyFieldSelection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ConfigurationVersionDataType>>
      callModifyFieldSelectionDetailedAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable Boolean @Nullable [] promotedFields,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields);
}
