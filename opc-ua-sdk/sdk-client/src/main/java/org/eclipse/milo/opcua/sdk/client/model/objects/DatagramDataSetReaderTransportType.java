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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ReceiveQosDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.4">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.4</a>
 */
public interface DatagramDataSetReaderTransportType extends DataSetReaderTransportType {
  QualifiedProperty<String> QOS_CATEGORY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "QosCategory",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<ReceiveQosDataType[]> DATAGRAM_QOS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DatagramQos",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23608"),
          1,
          ReceiveQosDataType[].class);

  QualifiedProperty<String> TOPIC =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Topic",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /**
   * Get the local value of the QosCategory Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the QosCategory Node.
   * @throws UaException if an error occurs creating or getting the QosCategory Node.
   */
  String getQosCategory() throws UaException;

  /**
   * Set the local value of the QosCategory Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the QosCategory Node.
   * @throws UaException if an error occurs creating or getting the QosCategory Node.
   */
  void setQosCategory(String value) throws UaException;

  /**
   * Read the value of the QosCategory Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readQosCategory() throws UaException;

  /**
   * Write a new value for the QosCategory Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeQosCategory(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readQosCategory}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readQosCategoryAsync();

  /**
   * An asynchronous implementation of {@link #writeQosCategory}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeQosCategoryAsync(String value);

  /**
   * Get the QosCategory {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the QosCategory {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getQosCategoryNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getQosCategoryNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getQosCategoryNodeAsync();

  /**
   * Get the local value of the DatagramQos Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DatagramQos Node.
   * @throws UaException if an error occurs creating or getting the DatagramQos Node.
   */
  ReceiveQosDataType[] getDatagramQos() throws UaException;

  /**
   * Set the local value of the DatagramQos Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DatagramQos Node.
   * @throws UaException if an error occurs creating or getting the DatagramQos Node.
   */
  void setDatagramQos(ReceiveQosDataType[] value) throws UaException;

  /**
   * Read the value of the DatagramQos Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ReceiveQosDataType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ReceiveQosDataType[] readDatagramQos() throws UaException;

  /**
   * Write a new value for the DatagramQos Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ReceiveQosDataType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDatagramQos(ReceiveQosDataType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDatagramQos}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ReceiveQosDataType[]> readDatagramQosAsync();

  /**
   * An asynchronous implementation of {@link #writeDatagramQos}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDatagramQosAsync(ReceiveQosDataType[] value);

  /**
   * Get the DatagramQos {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DatagramQos {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDatagramQosNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDatagramQosNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDatagramQosNodeAsync();

  /**
   * Get the local value of the Topic Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Topic Node.
   * @throws UaException if an error occurs creating or getting the Topic Node.
   */
  String getTopic() throws UaException;

  /**
   * Set the local value of the Topic Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Topic Node.
   * @throws UaException if an error occurs creating or getting the Topic Node.
   */
  void setTopic(String value) throws UaException;

  /**
   * Read the value of the Topic Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readTopic() throws UaException;

  /**
   * Write a new value for the Topic Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTopic(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTopic}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readTopicAsync();

  /**
   * An asynchronous implementation of {@link #writeTopic}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTopicAsync(String value);

  /**
   * Get the Topic {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Topic {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getTopicNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTopicNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getTopicNodeAsync();

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
}
