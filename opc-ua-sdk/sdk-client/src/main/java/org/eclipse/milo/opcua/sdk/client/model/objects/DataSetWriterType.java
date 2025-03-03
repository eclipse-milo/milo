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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.2</a>
 */
public interface DataSetWriterType extends BaseObjectType {
  QualifiedProperty<UShort> DATA_SET_WRITER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetWriterId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<DataSetFieldContentMask> DATA_SET_FIELD_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetFieldContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15583"),
          -1,
          DataSetFieldContentMask.class);

  QualifiedProperty<UInteger> KEY_FRAME_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeyFrameCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<KeyValuePair[]> DATA_SET_WRITER_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetWriterProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  /**
   * Get the local value of the DataSetWriterId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataSetWriterId Node.
   * @throws UaException if an error occurs creating or getting the DataSetWriterId Node.
   */
  UShort getDataSetWriterId() throws UaException;

  /**
   * Set the local value of the DataSetWriterId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataSetWriterId Node.
   * @throws UaException if an error occurs creating or getting the DataSetWriterId Node.
   */
  void setDataSetWriterId(UShort value) throws UaException;

  /**
   * Read the value of the DataSetWriterId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readDataSetWriterId() throws UaException;

  /**
   * Write a new value for the DataSetWriterId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataSetWriterId(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataSetWriterId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readDataSetWriterIdAsync();

  /**
   * An asynchronous implementation of {@link #writeDataSetWriterId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataSetWriterIdAsync(UShort value);

  /**
   * Get the DataSetWriterId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetWriterId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDataSetWriterIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetWriterIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDataSetWriterIdNodeAsync();

  /**
   * Get the local value of the DataSetFieldContentMask Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataSetFieldContentMask Node.
   * @throws UaException if an error occurs creating or getting the DataSetFieldContentMask Node.
   */
  DataSetFieldContentMask getDataSetFieldContentMask() throws UaException;

  /**
   * Set the local value of the DataSetFieldContentMask Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataSetFieldContentMask Node.
   * @throws UaException if an error occurs creating or getting the DataSetFieldContentMask Node.
   */
  void setDataSetFieldContentMask(DataSetFieldContentMask value) throws UaException;

  /**
   * Read the value of the DataSetFieldContentMask Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link DataSetFieldContentMask} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DataSetFieldContentMask readDataSetFieldContentMask() throws UaException;

  /**
   * Write a new value for the DataSetFieldContentMask Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link DataSetFieldContentMask} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataSetFieldContentMask(DataSetFieldContentMask value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataSetFieldContentMask}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DataSetFieldContentMask> readDataSetFieldContentMaskAsync();

  /**
   * An asynchronous implementation of {@link #writeDataSetFieldContentMask}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataSetFieldContentMaskAsync(DataSetFieldContentMask value);

  /**
   * Get the DataSetFieldContentMask {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetFieldContentMask {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDataSetFieldContentMaskNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetFieldContentMaskNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDataSetFieldContentMaskNodeAsync();

  /**
   * Get the local value of the KeyFrameCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the KeyFrameCount Node.
   * @throws UaException if an error occurs creating or getting the KeyFrameCount Node.
   */
  UInteger getKeyFrameCount() throws UaException;

  /**
   * Set the local value of the KeyFrameCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the KeyFrameCount Node.
   * @throws UaException if an error occurs creating or getting the KeyFrameCount Node.
   */
  void setKeyFrameCount(UInteger value) throws UaException;

  /**
   * Read the value of the KeyFrameCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readKeyFrameCount() throws UaException;

  /**
   * Write a new value for the KeyFrameCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeKeyFrameCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readKeyFrameCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readKeyFrameCountAsync();

  /**
   * An asynchronous implementation of {@link #writeKeyFrameCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeKeyFrameCountAsync(UInteger value);

  /**
   * Get the KeyFrameCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the KeyFrameCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getKeyFrameCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getKeyFrameCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getKeyFrameCountNodeAsync();

  /**
   * Get the local value of the DataSetWriterProperties Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataSetWriterProperties Node.
   * @throws UaException if an error occurs creating or getting the DataSetWriterProperties Node.
   */
  KeyValuePair[] getDataSetWriterProperties() throws UaException;

  /**
   * Set the local value of the DataSetWriterProperties Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataSetWriterProperties Node.
   * @throws UaException if an error occurs creating or getting the DataSetWriterProperties Node.
   */
  void setDataSetWriterProperties(KeyValuePair[] value) throws UaException;

  /**
   * Read the value of the DataSetWriterProperties Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link KeyValuePair[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  KeyValuePair[] readDataSetWriterProperties() throws UaException;

  /**
   * Write a new value for the DataSetWriterProperties Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link KeyValuePair[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataSetWriterProperties(KeyValuePair[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataSetWriterProperties}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends KeyValuePair[]> readDataSetWriterPropertiesAsync();

  /**
   * An asynchronous implementation of {@link #writeDataSetWriterProperties}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataSetWriterPropertiesAsync(KeyValuePair[] value);

  /**
   * Get the DataSetWriterProperties {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetWriterProperties {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDataSetWriterPropertiesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetWriterPropertiesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDataSetWriterPropertiesNodeAsync();

  /**
   * Get the TransportSettings {@link DataSetWriterTransportType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransportSettings {@link DataSetWriterTransportType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  DataSetWriterTransportType getTransportSettingsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransportSettingsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the DataSetWriterTransportType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends DataSetWriterTransportType> getTransportSettingsNodeAsync();

  /**
   * Get the MessageSettings {@link DataSetWriterMessageType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MessageSettings {@link DataSetWriterMessageType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  DataSetWriterMessageType getMessageSettingsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMessageSettingsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the DataSetWriterMessageType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends DataSetWriterMessageType> getMessageSettingsNodeAsync();

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
   * Get the Diagnostics {@link PubSubDiagnosticsDataSetWriterType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Diagnostics {@link PubSubDiagnosticsDataSetWriterType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubDiagnosticsDataSetWriterType getDiagnosticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiagnosticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the
   *     PubSubDiagnosticsDataSetWriterType Node or completes exceptionally if an error occurs
   *     creating or getting the Node.
   */
  CompletableFuture<? extends PubSubDiagnosticsDataSetWriterType> getDiagnosticsNodeAsync();
}
