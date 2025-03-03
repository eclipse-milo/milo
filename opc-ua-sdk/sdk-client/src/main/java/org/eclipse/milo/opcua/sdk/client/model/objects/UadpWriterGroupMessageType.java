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
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.1</a>
 */
public interface UadpWriterGroupMessageType extends WriterGroupMessageType {
  QualifiedProperty<UInteger> GROUP_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "GroupVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<DataSetOrderingType> DATA_SET_ORDERING =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetOrdering",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20408"),
          -1,
          DataSetOrderingType.class);

  QualifiedProperty<UadpNetworkMessageContentMask> NETWORK_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15642"),
          -1,
          UadpNetworkMessageContentMask.class);

  QualifiedProperty<Double> SAMPLING_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SamplingOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double[]> PUBLISHING_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishingOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          1,
          Double[].class);

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
   * Get the local value of the DataSetOrdering Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataSetOrdering Node.
   * @throws UaException if an error occurs creating or getting the DataSetOrdering Node.
   */
  DataSetOrderingType getDataSetOrdering() throws UaException;

  /**
   * Set the local value of the DataSetOrdering Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataSetOrdering Node.
   * @throws UaException if an error occurs creating or getting the DataSetOrdering Node.
   */
  void setDataSetOrdering(DataSetOrderingType value) throws UaException;

  /**
   * Read the value of the DataSetOrdering Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DataSetOrderingType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DataSetOrderingType readDataSetOrdering() throws UaException;

  /**
   * Write a new value for the DataSetOrdering Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DataSetOrderingType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataSetOrdering(DataSetOrderingType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataSetOrdering}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DataSetOrderingType> readDataSetOrderingAsync();

  /**
   * An asynchronous implementation of {@link #writeDataSetOrdering}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataSetOrderingAsync(DataSetOrderingType value);

  /**
   * Get the DataSetOrdering {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetOrdering {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDataSetOrderingNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetOrderingNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDataSetOrderingNodeAsync();

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
   * Get the local value of the SamplingOffset Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SamplingOffset Node.
   * @throws UaException if an error occurs creating or getting the SamplingOffset Node.
   */
  Double getSamplingOffset() throws UaException;

  /**
   * Set the local value of the SamplingOffset Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SamplingOffset Node.
   * @throws UaException if an error occurs creating or getting the SamplingOffset Node.
   */
  void setSamplingOffset(Double value) throws UaException;

  /**
   * Read the value of the SamplingOffset Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readSamplingOffset() throws UaException;

  /**
   * Write a new value for the SamplingOffset Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSamplingOffset(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSamplingOffset}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readSamplingOffsetAsync();

  /**
   * An asynchronous implementation of {@link #writeSamplingOffset}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSamplingOffsetAsync(Double value);

  /**
   * Get the SamplingOffset {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SamplingOffset {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSamplingOffsetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSamplingOffsetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSamplingOffsetNodeAsync();

  /**
   * Get the local value of the PublishingOffset Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PublishingOffset Node.
   * @throws UaException if an error occurs creating or getting the PublishingOffset Node.
   */
  Double[] getPublishingOffset() throws UaException;

  /**
   * Set the local value of the PublishingOffset Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PublishingOffset Node.
   * @throws UaException if an error occurs creating or getting the PublishingOffset Node.
   */
  void setPublishingOffset(Double[] value) throws UaException;

  /**
   * Read the value of the PublishingOffset Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double[] readPublishingOffset() throws UaException;

  /**
   * Write a new value for the PublishingOffset Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePublishingOffset(Double[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPublishingOffset}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double[]> readPublishingOffsetAsync();

  /**
   * An asynchronous implementation of {@link #writePublishingOffset}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePublishingOffsetAsync(Double[] value);

  /**
   * Get the PublishingOffset {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PublishingOffset {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPublishingOffsetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPublishingOffsetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPublishingOffsetNodeAsync();
}
