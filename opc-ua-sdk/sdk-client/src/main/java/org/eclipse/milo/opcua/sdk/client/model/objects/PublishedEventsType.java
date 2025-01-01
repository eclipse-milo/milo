/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1</a>
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

  /**
   * Get the local value of the PubSubEventNotifier Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PubSubEventNotifier Node.
   * @throws UaException if an error occurs creating or getting the PubSubEventNotifier Node.
   */
  NodeId getPubSubEventNotifier() throws UaException;

  /**
   * Set the local value of the PubSubEventNotifier Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PubSubEventNotifier Node.
   * @throws UaException if an error occurs creating or getting the PubSubEventNotifier Node.
   */
  void setPubSubEventNotifier(NodeId value) throws UaException;

  /**
   * Read the value of the PubSubEventNotifier Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readPubSubEventNotifier() throws UaException;

  /**
   * Write a new value for the PubSubEventNotifier Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePubSubEventNotifier(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPubSubEventNotifier}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readPubSubEventNotifierAsync();

  /**
   * An asynchronous implementation of {@link #writePubSubEventNotifier}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePubSubEventNotifierAsync(NodeId value);

  /**
   * Get the PubSubEventNotifier {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PubSubEventNotifier {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPubSubEventNotifierNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPubSubEventNotifierNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPubSubEventNotifierNodeAsync();

  /**
   * Get the local value of the SelectedFields Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SelectedFields Node.
   * @throws UaException if an error occurs creating or getting the SelectedFields Node.
   */
  SimpleAttributeOperand[] getSelectedFields() throws UaException;

  /**
   * Set the local value of the SelectedFields Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SelectedFields Node.
   * @throws UaException if an error occurs creating or getting the SelectedFields Node.
   */
  void setSelectedFields(SimpleAttributeOperand[] value) throws UaException;

  /**
   * Read the value of the SelectedFields Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link SimpleAttributeOperand[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SimpleAttributeOperand[] readSelectedFields() throws UaException;

  /**
   * Write a new value for the SelectedFields Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link SimpleAttributeOperand[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSelectedFields(SimpleAttributeOperand[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSelectedFields}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SimpleAttributeOperand[]> readSelectedFieldsAsync();

  /**
   * An asynchronous implementation of {@link #writeSelectedFields}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSelectedFieldsAsync(SimpleAttributeOperand[] value);

  /**
   * Get the SelectedFields {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SelectedFields {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSelectedFieldsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSelectedFieldsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSelectedFieldsNodeAsync();

  /**
   * Get the local value of the Filter Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Filter Node.
   * @throws UaException if an error occurs creating or getting the Filter Node.
   */
  ContentFilter getFilter() throws UaException;

  /**
   * Set the local value of the Filter Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Filter Node.
   * @throws UaException if an error occurs creating or getting the Filter Node.
   */
  void setFilter(ContentFilter value) throws UaException;

  /**
   * Read the value of the Filter Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link ContentFilter} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ContentFilter readFilter() throws UaException;

  /**
   * Write a new value for the Filter Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link ContentFilter} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeFilter(ContentFilter value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readFilter}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ContentFilter> readFilterAsync();

  /**
   * An asynchronous implementation of {@link #writeFilter}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeFilterAsync(ContentFilter value);

  /**
   * Get the Filter {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Filter {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getFilterNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getFilterNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getFilterNodeAsync();
}
