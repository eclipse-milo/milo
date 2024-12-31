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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.2</a>
 */
public interface DatagramWriterGroupTransportType extends WriterGroupTransportType {
  QualifiedProperty<UByte> MESSAGE_REPEAT_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MessageRepeatCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          -1,
          UByte.class);

  QualifiedProperty<Double> MESSAGE_REPEAT_DELAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MessageRepeatDelay",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<String> QOS_CATEGORY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "QosCategory",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<TransmitQosDataType[]> DATAGRAM_QOS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DatagramQos",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23604"),
          1,
          TransmitQosDataType[].class);

  QualifiedProperty<UInteger> DISCOVERY_ANNOUNCE_RATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DiscoveryAnnounceRate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<String> TOPIC =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Topic",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /**
   * Get the local value of the MessageRepeatCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MessageRepeatCount Node.
   * @throws UaException if an error occurs creating or getting the MessageRepeatCount Node.
   */
  UByte getMessageRepeatCount() throws UaException;

  /**
   * Set the local value of the MessageRepeatCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MessageRepeatCount Node.
   * @throws UaException if an error occurs creating or getting the MessageRepeatCount Node.
   */
  void setMessageRepeatCount(UByte value) throws UaException;

  /**
   * Read the value of the MessageRepeatCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UByte} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte readMessageRepeatCount() throws UaException;

  /**
   * Write a new value for the MessageRepeatCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UByte} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMessageRepeatCount(UByte value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMessageRepeatCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte> readMessageRepeatCountAsync();

  /**
   * An asynchronous implementation of {@link #writeMessageRepeatCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMessageRepeatCountAsync(UByte value);

  /**
   * Get the MessageRepeatCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MessageRepeatCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMessageRepeatCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMessageRepeatCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMessageRepeatCountNodeAsync();

  /**
   * Get the local value of the MessageRepeatDelay Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MessageRepeatDelay Node.
   * @throws UaException if an error occurs creating or getting the MessageRepeatDelay Node.
   */
  Double getMessageRepeatDelay() throws UaException;

  /**
   * Set the local value of the MessageRepeatDelay Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MessageRepeatDelay Node.
   * @throws UaException if an error occurs creating or getting the MessageRepeatDelay Node.
   */
  void setMessageRepeatDelay(Double value) throws UaException;

  /**
   * Read the value of the MessageRepeatDelay Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readMessageRepeatDelay() throws UaException;

  /**
   * Write a new value for the MessageRepeatDelay Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMessageRepeatDelay(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMessageRepeatDelay}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readMessageRepeatDelayAsync();

  /**
   * An asynchronous implementation of {@link #writeMessageRepeatDelay}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMessageRepeatDelayAsync(Double value);

  /**
   * Get the MessageRepeatDelay {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MessageRepeatDelay {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMessageRepeatDelayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMessageRepeatDelayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMessageRepeatDelayNodeAsync();

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
  TransmitQosDataType[] getDatagramQos() throws UaException;

  /**
   * Set the local value of the DatagramQos Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DatagramQos Node.
   * @throws UaException if an error occurs creating or getting the DatagramQos Node.
   */
  void setDatagramQos(TransmitQosDataType[] value) throws UaException;

  /**
   * Read the value of the DatagramQos Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link TransmitQosDataType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  TransmitQosDataType[] readDatagramQos() throws UaException;

  /**
   * Write a new value for the DatagramQos Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link TransmitQosDataType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDatagramQos(TransmitQosDataType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDatagramQos}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends TransmitQosDataType[]> readDatagramQosAsync();

  /**
   * An asynchronous implementation of {@link #writeDatagramQos}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDatagramQosAsync(TransmitQosDataType[] value);

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
