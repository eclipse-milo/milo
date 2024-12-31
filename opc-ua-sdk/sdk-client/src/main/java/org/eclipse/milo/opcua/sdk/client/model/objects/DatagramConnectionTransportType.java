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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.QosDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.1</a>
 */
public interface DatagramConnectionTransportType extends ConnectionTransportType {
  QualifiedProperty<UInteger> DISCOVERY_ANNOUNCE_RATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DiscoveryAnnounceRate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> DISCOVERY_MAX_MESSAGE_SIZE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DiscoveryMaxMessageSize",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<String> QOS_CATEGORY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "QosCategory",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<QosDataType[]> DATAGRAM_QOS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DatagramQos",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23603"),
          1,
          QosDataType[].class);

  /**
   * Get the local value of the DiscoveryAnnounceRate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DiscoveryAnnounceRate Node.
   * @throws UaException if an error occurs creating or getting the DiscoveryAnnounceRate Node.
   */
  UInteger getDiscoveryAnnounceRate() throws UaException;

  /**
   * Set the local value of the DiscoveryAnnounceRate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DiscoveryAnnounceRate Node.
   * @throws UaException if an error occurs creating or getting the DiscoveryAnnounceRate Node.
   */
  void setDiscoveryAnnounceRate(UInteger value) throws UaException;

  /**
   * Read the value of the DiscoveryAnnounceRate Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readDiscoveryAnnounceRate() throws UaException;

  /**
   * Write a new value for the DiscoveryAnnounceRate Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDiscoveryAnnounceRate(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDiscoveryAnnounceRate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readDiscoveryAnnounceRateAsync();

  /**
   * An asynchronous implementation of {@link #writeDiscoveryAnnounceRate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDiscoveryAnnounceRateAsync(UInteger value);

  /**
   * Get the DiscoveryAnnounceRate {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DiscoveryAnnounceRate {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDiscoveryAnnounceRateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiscoveryAnnounceRateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDiscoveryAnnounceRateNodeAsync();

  /**
   * Get the local value of the DiscoveryMaxMessageSize Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DiscoveryMaxMessageSize Node.
   * @throws UaException if an error occurs creating or getting the DiscoveryMaxMessageSize Node.
   */
  UInteger getDiscoveryMaxMessageSize() throws UaException;

  /**
   * Set the local value of the DiscoveryMaxMessageSize Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DiscoveryMaxMessageSize Node.
   * @throws UaException if an error occurs creating or getting the DiscoveryMaxMessageSize Node.
   */
  void setDiscoveryMaxMessageSize(UInteger value) throws UaException;

  /**
   * Read the value of the DiscoveryMaxMessageSize Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readDiscoveryMaxMessageSize() throws UaException;

  /**
   * Write a new value for the DiscoveryMaxMessageSize Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDiscoveryMaxMessageSize(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDiscoveryMaxMessageSize}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readDiscoveryMaxMessageSizeAsync();

  /**
   * An asynchronous implementation of {@link #writeDiscoveryMaxMessageSize}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDiscoveryMaxMessageSizeAsync(UInteger value);

  /**
   * Get the DiscoveryMaxMessageSize {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DiscoveryMaxMessageSize {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDiscoveryMaxMessageSizeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiscoveryMaxMessageSizeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDiscoveryMaxMessageSizeNodeAsync();

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
  QosDataType[] getDatagramQos() throws UaException;

  /**
   * Set the local value of the DatagramQos Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DatagramQos Node.
   * @throws UaException if an error occurs creating or getting the DatagramQos Node.
   */
  void setDatagramQos(QosDataType[] value) throws UaException;

  /**
   * Read the value of the DatagramQos Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link QosDataType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  QosDataType[] readDatagramQos() throws UaException;

  /**
   * Write a new value for the DatagramQos Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link QosDataType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDatagramQos(QosDataType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDatagramQos}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends QosDataType[]> readDatagramQosAsync();

  /**
   * An asynchronous implementation of {@link #writeDatagramQos}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDatagramQosAsync(QosDataType[] value);

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
   * Get the DiscoveryAddress {@link NetworkAddressType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DiscoveryAddress {@link NetworkAddressType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  NetworkAddressType getDiscoveryAddressNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiscoveryAddressNode()}.
   *
   * @return a CompletableFuture that completes successfully with the NetworkAddressType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends NetworkAddressType> getDiscoveryAddressNodeAsync();
}
