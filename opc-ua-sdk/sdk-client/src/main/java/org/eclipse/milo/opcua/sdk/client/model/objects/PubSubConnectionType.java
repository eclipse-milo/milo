/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.sdk.client.model.variables.SelectionListType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.2</a>
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

  /**
   * Get the local value of the PublisherId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PublisherId Node.
   * @throws UaException if an error occurs creating or getting the PublisherId Node.
   */
  Object getPublisherId() throws UaException;

  /**
   * Set the local value of the PublisherId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PublisherId Node.
   * @throws UaException if an error occurs creating or getting the PublisherId Node.
   */
  void setPublisherId(Object value) throws UaException;

  /**
   * Read the value of the PublisherId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Object} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object readPublisherId() throws UaException;

  /**
   * Write a new value for the PublisherId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Object} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePublisherId(Object value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPublisherId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<?> readPublisherIdAsync();

  /**
   * An asynchronous implementation of {@link #writePublisherId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePublisherIdAsync(Object value);

  /**
   * Get the PublisherId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PublisherId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPublisherIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPublisherIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPublisherIdNodeAsync();

  /**
   * Get the local value of the ConnectionProperties Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConnectionProperties Node.
   * @throws UaException if an error occurs creating or getting the ConnectionProperties Node.
   */
  KeyValuePair[] getConnectionProperties() throws UaException;

  /**
   * Set the local value of the ConnectionProperties Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConnectionProperties Node.
   * @throws UaException if an error occurs creating or getting the ConnectionProperties Node.
   */
  void setConnectionProperties(KeyValuePair[] value) throws UaException;

  /**
   * Read the value of the ConnectionProperties Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link KeyValuePair[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  KeyValuePair[] readConnectionProperties() throws UaException;

  /**
   * Write a new value for the ConnectionProperties Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link KeyValuePair[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConnectionProperties(KeyValuePair[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConnectionProperties}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends KeyValuePair[]> readConnectionPropertiesAsync();

  /**
   * An asynchronous implementation of {@link #writeConnectionProperties}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConnectionPropertiesAsync(KeyValuePair[] value);

  /**
   * Get the ConnectionProperties {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConnectionProperties {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConnectionPropertiesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConnectionPropertiesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConnectionPropertiesNodeAsync();

  /**
   * Get the local value of the TransportProfileUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TransportProfileUri Node.
   * @throws UaException if an error occurs creating or getting the TransportProfileUri Node.
   */
  String getTransportProfileUri() throws UaException;

  /**
   * Set the local value of the TransportProfileUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TransportProfileUri Node.
   * @throws UaException if an error occurs creating or getting the TransportProfileUri Node.
   */
  void setTransportProfileUri(String value) throws UaException;

  /**
   * Read the value of the TransportProfileUri Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readTransportProfileUri() throws UaException;

  /**
   * Write a new value for the TransportProfileUri Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTransportProfileUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTransportProfileUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readTransportProfileUriAsync();

  /**
   * An asynchronous implementation of {@link #writeTransportProfileUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTransportProfileUriAsync(String value);

  /**
   * Get the TransportProfileUri {@link SelectionListType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransportProfileUri {@link SelectionListType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SelectionListType getTransportProfileUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransportProfileUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SelectionListType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SelectionListType> getTransportProfileUriNodeAsync();

  /**
   * Get the Address {@link NetworkAddressType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Address {@link NetworkAddressType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  NetworkAddressType getAddressNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAddressNode()}.
   *
   * @return a CompletableFuture that completes successfully with the NetworkAddressType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends NetworkAddressType> getAddressNodeAsync();

  /**
   * Get the TransportSettings {@link ConnectionTransportType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransportSettings {@link ConnectionTransportType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  ConnectionTransportType getTransportSettingsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransportSettingsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the ConnectionTransportType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends ConnectionTransportType> getTransportSettingsNodeAsync();

  /**
   * Get the Status {@link PubSubStatusType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Status {@link PubSubStatusType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubStatusType getStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PubSubStatusType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PubSubStatusType> getStatusNodeAsync();

  /**
   * Get the Diagnostics {@link PubSubDiagnosticsConnectionType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Diagnostics {@link PubSubDiagnosticsConnectionType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubDiagnosticsConnectionType getDiagnosticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiagnosticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the
   *     PubSubDiagnosticsConnectionType Node or completes exceptionally if an error occurs creating
   *     or getting the Node.
   */
  CompletableFuture<? extends PubSubDiagnosticsConnectionType> getDiagnosticsNodeAsync();
}
