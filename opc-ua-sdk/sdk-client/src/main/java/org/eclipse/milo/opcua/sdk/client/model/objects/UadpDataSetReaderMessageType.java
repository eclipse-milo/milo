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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.3</a>
 */
public interface UadpDataSetReaderMessageType extends DataSetReaderMessageType {
  QualifiedProperty<UInteger> GROUP_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "GroupVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<UShort> NETWORK_MESSAGE_NUMBER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageNumber",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> DATA_SET_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UUID> DATA_SET_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14"),
          -1,
          UUID.class);

  QualifiedProperty<UadpNetworkMessageContentMask> NETWORK_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15642"),
          -1,
          UadpNetworkMessageContentMask.class);

  QualifiedProperty<UadpDataSetMessageContentMask> DATA_SET_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15646"),
          -1,
          UadpDataSetMessageContentMask.class);

  QualifiedProperty<Double> PUBLISHING_INTERVAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishingInterval",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> PROCESSING_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProcessingOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> RECEIVE_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReceiveOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /**
   * Get the local value of the GroupVersion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the GroupVersion Node.
   * @throws UaException if an error occurs creating or getting the GroupVersion Node.
   */
  UInteger getGroupVersion() throws UaException;

  /**
   * Set the local value of the GroupVersion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the GroupVersion Node.
   * @throws UaException if an error occurs creating or getting the GroupVersion Node.
   */
  void setGroupVersion(UInteger value) throws UaException;

  /**
   * Read the value of the GroupVersion Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readGroupVersion() throws UaException;

  /**
   * Write a new value for the GroupVersion Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeGroupVersion(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readGroupVersion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readGroupVersionAsync();

  /**
   * An asynchronous implementation of {@link #writeGroupVersion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeGroupVersionAsync(UInteger value);

  /**
   * Get the GroupVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the GroupVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getGroupVersionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getGroupVersionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getGroupVersionNodeAsync();

  /**
   * Get the local value of the NetworkMessageNumber Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NetworkMessageNumber Node.
   * @throws UaException if an error occurs creating or getting the NetworkMessageNumber Node.
   */
  UShort getNetworkMessageNumber() throws UaException;

  /**
   * Set the local value of the NetworkMessageNumber Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NetworkMessageNumber Node.
   * @throws UaException if an error occurs creating or getting the NetworkMessageNumber Node.
   */
  void setNetworkMessageNumber(UShort value) throws UaException;

  /**
   * Read the value of the NetworkMessageNumber Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readNetworkMessageNumber() throws UaException;

  /**
   * Write a new value for the NetworkMessageNumber Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNetworkMessageNumber(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNetworkMessageNumber}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readNetworkMessageNumberAsync();

  /**
   * An asynchronous implementation of {@link #writeNetworkMessageNumber}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNetworkMessageNumberAsync(UShort value);

  /**
   * Get the NetworkMessageNumber {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NetworkMessageNumber {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNetworkMessageNumberNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNetworkMessageNumberNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNetworkMessageNumberNodeAsync();

  /**
   * Get the local value of the DataSetOffset Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataSetOffset Node.
   * @throws UaException if an error occurs creating or getting the DataSetOffset Node.
   */
  UShort getDataSetOffset() throws UaException;

  /**
   * Set the local value of the DataSetOffset Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataSetOffset Node.
   * @throws UaException if an error occurs creating or getting the DataSetOffset Node.
   */
  void setDataSetOffset(UShort value) throws UaException;

  /**
   * Read the value of the DataSetOffset Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readDataSetOffset() throws UaException;

  /**
   * Write a new value for the DataSetOffset Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataSetOffset(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataSetOffset}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readDataSetOffsetAsync();

  /**
   * An asynchronous implementation of {@link #writeDataSetOffset}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataSetOffsetAsync(UShort value);

  /**
   * Get the DataSetOffset {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetOffset {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDataSetOffsetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetOffsetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDataSetOffsetNodeAsync();

  /**
   * Get the local value of the DataSetClassId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataSetClassId Node.
   * @throws UaException if an error occurs creating or getting the DataSetClassId Node.
   */
  UUID getDataSetClassId() throws UaException;

  /**
   * Set the local value of the DataSetClassId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataSetClassId Node.
   * @throws UaException if an error occurs creating or getting the DataSetClassId Node.
   */
  void setDataSetClassId(UUID value) throws UaException;

  /**
   * Read the value of the DataSetClassId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UUID} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UUID readDataSetClassId() throws UaException;

  /**
   * Write a new value for the DataSetClassId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UUID} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataSetClassId(UUID value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataSetClassId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UUID> readDataSetClassIdAsync();

  /**
   * An asynchronous implementation of {@link #writeDataSetClassId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataSetClassIdAsync(UUID value);

  /**
   * Get the DataSetClassId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetClassId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDataSetClassIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetClassIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDataSetClassIdNodeAsync();

  /**
   * Get the local value of the NetworkMessageContentMask Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NetworkMessageContentMask Node.
   * @throws UaException if an error occurs creating or getting the NetworkMessageContentMask Node.
   */
  UadpNetworkMessageContentMask getNetworkMessageContentMask() throws UaException;

  /**
   * Set the local value of the NetworkMessageContentMask Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NetworkMessageContentMask Node.
   * @throws UaException if an error occurs creating or getting the NetworkMessageContentMask Node.
   */
  void setNetworkMessageContentMask(UadpNetworkMessageContentMask value) throws UaException;

  /**
   * Read the value of the NetworkMessageContentMask Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UadpNetworkMessageContentMask} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UadpNetworkMessageContentMask readNetworkMessageContentMask() throws UaException;

  /**
   * Write a new value for the NetworkMessageContentMask Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UadpNetworkMessageContentMask} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNetworkMessageContentMask(UadpNetworkMessageContentMask value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNetworkMessageContentMask}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UadpNetworkMessageContentMask> readNetworkMessageContentMaskAsync();

  /**
   * An asynchronous implementation of {@link #writeNetworkMessageContentMask}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNetworkMessageContentMaskAsync(
      UadpNetworkMessageContentMask value);

  /**
   * Get the NetworkMessageContentMask {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NetworkMessageContentMask {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNetworkMessageContentMaskNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNetworkMessageContentMaskNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNetworkMessageContentMaskNodeAsync();

  /**
   * Get the local value of the DataSetMessageContentMask Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataSetMessageContentMask Node.
   * @throws UaException if an error occurs creating or getting the DataSetMessageContentMask Node.
   */
  UadpDataSetMessageContentMask getDataSetMessageContentMask() throws UaException;

  /**
   * Set the local value of the DataSetMessageContentMask Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataSetMessageContentMask Node.
   * @throws UaException if an error occurs creating or getting the DataSetMessageContentMask Node.
   */
  void setDataSetMessageContentMask(UadpDataSetMessageContentMask value) throws UaException;

  /**
   * Read the value of the DataSetMessageContentMask Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UadpDataSetMessageContentMask} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UadpDataSetMessageContentMask readDataSetMessageContentMask() throws UaException;

  /**
   * Write a new value for the DataSetMessageContentMask Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UadpDataSetMessageContentMask} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataSetMessageContentMask(UadpDataSetMessageContentMask value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataSetMessageContentMask}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UadpDataSetMessageContentMask> readDataSetMessageContentMaskAsync();

  /**
   * An asynchronous implementation of {@link #writeDataSetMessageContentMask}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataSetMessageContentMaskAsync(
      UadpDataSetMessageContentMask value);

  /**
   * Get the DataSetMessageContentMask {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetMessageContentMask {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDataSetMessageContentMaskNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetMessageContentMaskNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDataSetMessageContentMaskNodeAsync();

  /**
   * Get the local value of the PublishingInterval Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PublishingInterval Node.
   * @throws UaException if an error occurs creating or getting the PublishingInterval Node.
   */
  Double getPublishingInterval() throws UaException;

  /**
   * Set the local value of the PublishingInterval Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PublishingInterval Node.
   * @throws UaException if an error occurs creating or getting the PublishingInterval Node.
   */
  void setPublishingInterval(Double value) throws UaException;

  /**
   * Read the value of the PublishingInterval Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readPublishingInterval() throws UaException;

  /**
   * Write a new value for the PublishingInterval Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePublishingInterval(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPublishingInterval}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readPublishingIntervalAsync();

  /**
   * An asynchronous implementation of {@link #writePublishingInterval}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePublishingIntervalAsync(Double value);

  /**
   * Get the PublishingInterval {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PublishingInterval {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPublishingIntervalNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPublishingIntervalNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPublishingIntervalNodeAsync();

  /**
   * Get the local value of the ProcessingOffset Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ProcessingOffset Node.
   * @throws UaException if an error occurs creating or getting the ProcessingOffset Node.
   */
  Double getProcessingOffset() throws UaException;

  /**
   * Set the local value of the ProcessingOffset Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ProcessingOffset Node.
   * @throws UaException if an error occurs creating or getting the ProcessingOffset Node.
   */
  void setProcessingOffset(Double value) throws UaException;

  /**
   * Read the value of the ProcessingOffset Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readProcessingOffset() throws UaException;

  /**
   * Write a new value for the ProcessingOffset Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeProcessingOffset(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readProcessingOffset}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readProcessingOffsetAsync();

  /**
   * An asynchronous implementation of {@link #writeProcessingOffset}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeProcessingOffsetAsync(Double value);

  /**
   * Get the ProcessingOffset {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ProcessingOffset {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getProcessingOffsetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getProcessingOffsetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getProcessingOffsetNodeAsync();

  /**
   * Get the local value of the ReceiveOffset Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ReceiveOffset Node.
   * @throws UaException if an error occurs creating or getting the ReceiveOffset Node.
   */
  Double getReceiveOffset() throws UaException;

  /**
   * Set the local value of the ReceiveOffset Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ReceiveOffset Node.
   * @throws UaException if an error occurs creating or getting the ReceiveOffset Node.
   */
  void setReceiveOffset(Double value) throws UaException;

  /**
   * Read the value of the ReceiveOffset Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readReceiveOffset() throws UaException;

  /**
   * Write a new value for the ReceiveOffset Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeReceiveOffset(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readReceiveOffset}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readReceiveOffsetAsync();

  /**
   * An asynchronous implementation of {@link #writeReceiveOffset}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeReceiveOffsetAsync(Double value);

  /**
   * Get the ReceiveOffset {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReceiveOffset {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getReceiveOffsetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReceiveOffsetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getReceiveOffsetNodeAsync();
}
