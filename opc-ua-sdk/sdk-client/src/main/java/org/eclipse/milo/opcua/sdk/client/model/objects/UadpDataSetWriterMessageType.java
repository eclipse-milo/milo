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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.2</a>
 */
public interface UadpDataSetWriterMessageType extends DataSetWriterMessageType {
  QualifiedProperty<UadpDataSetMessageContentMask> DATA_SET_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15646"),
          -1,
          UadpDataSetMessageContentMask.class);

  QualifiedProperty<UShort> CONFIGURED_SIZE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConfiguredSize",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

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
   * Get the local value of the ConfiguredSize Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConfiguredSize Node.
   * @throws UaException if an error occurs creating or getting the ConfiguredSize Node.
   */
  UShort getConfiguredSize() throws UaException;

  /**
   * Set the local value of the ConfiguredSize Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConfiguredSize Node.
   * @throws UaException if an error occurs creating or getting the ConfiguredSize Node.
   */
  void setConfiguredSize(UShort value) throws UaException;

  /**
   * Read the value of the ConfiguredSize Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readConfiguredSize() throws UaException;

  /**
   * Write a new value for the ConfiguredSize Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConfiguredSize(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConfiguredSize}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readConfiguredSizeAsync();

  /**
   * An asynchronous implementation of {@link #writeConfiguredSize}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConfiguredSizeAsync(UShort value);

  /**
   * Get the ConfiguredSize {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConfiguredSize {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConfiguredSizeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConfiguredSizeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConfiguredSizeNodeAsync();

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
}
