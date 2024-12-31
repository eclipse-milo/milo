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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2</a>
 */
public interface DataSetReaderType extends BaseObjectType {
  QualifiedProperty<Object> PUBLISHER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublisherId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<UShort> WRITER_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "WriterGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> DATA_SET_WRITER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetWriterId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<DataSetMetaDataType> DATA_SET_META_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMetaData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14523"),
          -1,
          DataSetMetaDataType.class);

  QualifiedProperty<DataSetFieldContentMask> DATA_SET_FIELD_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetFieldContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15583"),
          -1,
          DataSetFieldContentMask.class);

  QualifiedProperty<Double> MESSAGE_RECEIVE_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MessageReceiveTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UInteger> KEY_FRAME_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeyFrameCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<String> HEADER_LAYOUT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HeaderLayoutUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<MessageSecurityMode> SECURITY_MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityMode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=302"),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<String> SECURITY_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<EndpointDescription[]> SECURITY_KEY_SERVICES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityKeyServices",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=312"),
          1,
          EndpointDescription[].class);

  QualifiedProperty<KeyValuePair[]> DATA_SET_READER_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetReaderProperties",
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
   * Get the local value of the WriterGroupId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the WriterGroupId Node.
   * @throws UaException if an error occurs creating or getting the WriterGroupId Node.
   */
  UShort getWriterGroupId() throws UaException;

  /**
   * Set the local value of the WriterGroupId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the WriterGroupId Node.
   * @throws UaException if an error occurs creating or getting the WriterGroupId Node.
   */
  void setWriterGroupId(UShort value) throws UaException;

  /**
   * Read the value of the WriterGroupId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readWriterGroupId() throws UaException;

  /**
   * Write a new value for the WriterGroupId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeWriterGroupId(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readWriterGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readWriterGroupIdAsync();

  /**
   * An asynchronous implementation of {@link #writeWriterGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeWriterGroupIdAsync(UShort value);

  /**
   * Get the WriterGroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the WriterGroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getWriterGroupIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getWriterGroupIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getWriterGroupIdNodeAsync();

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
   * Get the local value of the DataSetMetaData Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataSetMetaData Node.
   * @throws UaException if an error occurs creating or getting the DataSetMetaData Node.
   */
  DataSetMetaDataType getDataSetMetaData() throws UaException;

  /**
   * Set the local value of the DataSetMetaData Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataSetMetaData Node.
   * @throws UaException if an error occurs creating or getting the DataSetMetaData Node.
   */
  void setDataSetMetaData(DataSetMetaDataType value) throws UaException;

  /**
   * Read the value of the DataSetMetaData Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DataSetMetaDataType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DataSetMetaDataType readDataSetMetaData() throws UaException;

  /**
   * Write a new value for the DataSetMetaData Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DataSetMetaDataType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataSetMetaData(DataSetMetaDataType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataSetMetaData}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DataSetMetaDataType> readDataSetMetaDataAsync();

  /**
   * An asynchronous implementation of {@link #writeDataSetMetaData}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataSetMetaDataAsync(DataSetMetaDataType value);

  /**
   * Get the DataSetMetaData {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetMetaData {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDataSetMetaDataNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetMetaDataNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDataSetMetaDataNodeAsync();

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
   * Get the local value of the MessageReceiveTimeout Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MessageReceiveTimeout Node.
   * @throws UaException if an error occurs creating or getting the MessageReceiveTimeout Node.
   */
  Double getMessageReceiveTimeout() throws UaException;

  /**
   * Set the local value of the MessageReceiveTimeout Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MessageReceiveTimeout Node.
   * @throws UaException if an error occurs creating or getting the MessageReceiveTimeout Node.
   */
  void setMessageReceiveTimeout(Double value) throws UaException;

  /**
   * Read the value of the MessageReceiveTimeout Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readMessageReceiveTimeout() throws UaException;

  /**
   * Write a new value for the MessageReceiveTimeout Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMessageReceiveTimeout(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMessageReceiveTimeout}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readMessageReceiveTimeoutAsync();

  /**
   * An asynchronous implementation of {@link #writeMessageReceiveTimeout}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMessageReceiveTimeoutAsync(Double value);

  /**
   * Get the MessageReceiveTimeout {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MessageReceiveTimeout {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMessageReceiveTimeoutNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMessageReceiveTimeoutNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMessageReceiveTimeoutNodeAsync();

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
   * Get the local value of the HeaderLayoutUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HeaderLayoutUri Node.
   * @throws UaException if an error occurs creating or getting the HeaderLayoutUri Node.
   */
  String getHeaderLayoutUri() throws UaException;

  /**
   * Set the local value of the HeaderLayoutUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HeaderLayoutUri Node.
   * @throws UaException if an error occurs creating or getting the HeaderLayoutUri Node.
   */
  void setHeaderLayoutUri(String value) throws UaException;

  /**
   * Read the value of the HeaderLayoutUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readHeaderLayoutUri() throws UaException;

  /**
   * Write a new value for the HeaderLayoutUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHeaderLayoutUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHeaderLayoutUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readHeaderLayoutUriAsync();

  /**
   * An asynchronous implementation of {@link #writeHeaderLayoutUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHeaderLayoutUriAsync(String value);

  /**
   * Get the HeaderLayoutUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HeaderLayoutUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getHeaderLayoutUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHeaderLayoutUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getHeaderLayoutUriNodeAsync();

  /**
   * Get the local value of the SecurityMode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityMode Node.
   * @throws UaException if an error occurs creating or getting the SecurityMode Node.
   */
  MessageSecurityMode getSecurityMode() throws UaException;

  /**
   * Set the local value of the SecurityMode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityMode Node.
   * @throws UaException if an error occurs creating or getting the SecurityMode Node.
   */
  void setSecurityMode(MessageSecurityMode value) throws UaException;

  /**
   * Read the value of the SecurityMode Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link MessageSecurityMode} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  MessageSecurityMode readSecurityMode() throws UaException;

  /**
   * Write a new value for the SecurityMode Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link MessageSecurityMode} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityMode(MessageSecurityMode value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityMode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends MessageSecurityMode> readSecurityModeAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityMode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityModeAsync(MessageSecurityMode value);

  /**
   * Get the SecurityMode {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityMode {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecurityModeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityModeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecurityModeNodeAsync();

  /**
   * Get the local value of the SecurityGroupId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityGroupId Node.
   * @throws UaException if an error occurs creating or getting the SecurityGroupId Node.
   */
  String getSecurityGroupId() throws UaException;

  /**
   * Set the local value of the SecurityGroupId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityGroupId Node.
   * @throws UaException if an error occurs creating or getting the SecurityGroupId Node.
   */
  void setSecurityGroupId(String value) throws UaException;

  /**
   * Read the value of the SecurityGroupId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readSecurityGroupId() throws UaException;

  /**
   * Write a new value for the SecurityGroupId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityGroupId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readSecurityGroupIdAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityGroupIdAsync(String value);

  /**
   * Get the SecurityGroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityGroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecurityGroupIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityGroupIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecurityGroupIdNodeAsync();

  /**
   * Get the local value of the SecurityKeyServices Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityKeyServices Node.
   * @throws UaException if an error occurs creating or getting the SecurityKeyServices Node.
   */
  EndpointDescription[] getSecurityKeyServices() throws UaException;

  /**
   * Set the local value of the SecurityKeyServices Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityKeyServices Node.
   * @throws UaException if an error occurs creating or getting the SecurityKeyServices Node.
   */
  void setSecurityKeyServices(EndpointDescription[] value) throws UaException;

  /**
   * Read the value of the SecurityKeyServices Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link EndpointDescription[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  EndpointDescription[] readSecurityKeyServices() throws UaException;

  /**
   * Write a new value for the SecurityKeyServices Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link EndpointDescription[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityKeyServices(EndpointDescription[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityKeyServices}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends EndpointDescription[]> readSecurityKeyServicesAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityKeyServices}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityKeyServicesAsync(EndpointDescription[] value);

  /**
   * Get the SecurityKeyServices {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityKeyServices {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecurityKeyServicesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityKeyServicesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecurityKeyServicesNodeAsync();

  /**
   * Get the local value of the DataSetReaderProperties Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DataSetReaderProperties Node.
   * @throws UaException if an error occurs creating or getting the DataSetReaderProperties Node.
   */
  KeyValuePair[] getDataSetReaderProperties() throws UaException;

  /**
   * Set the local value of the DataSetReaderProperties Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DataSetReaderProperties Node.
   * @throws UaException if an error occurs creating or getting the DataSetReaderProperties Node.
   */
  void setDataSetReaderProperties(KeyValuePair[] value) throws UaException;

  /**
   * Read the value of the DataSetReaderProperties Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link KeyValuePair[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  KeyValuePair[] readDataSetReaderProperties() throws UaException;

  /**
   * Write a new value for the DataSetReaderProperties Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link KeyValuePair[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDataSetReaderProperties(KeyValuePair[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDataSetReaderProperties}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends KeyValuePair[]> readDataSetReaderPropertiesAsync();

  /**
   * An asynchronous implementation of {@link #writeDataSetReaderProperties}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDataSetReaderPropertiesAsync(KeyValuePair[] value);

  /**
   * Get the DataSetReaderProperties {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetReaderProperties {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDataSetReaderPropertiesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetReaderPropertiesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDataSetReaderPropertiesNodeAsync();

  /**
   * Get the TransportSettings {@link DataSetReaderTransportType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransportSettings {@link DataSetReaderTransportType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  DataSetReaderTransportType getTransportSettingsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransportSettingsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the DataSetReaderTransportType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends DataSetReaderTransportType> getTransportSettingsNodeAsync();

  /**
   * Get the MessageSettings {@link DataSetReaderMessageType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MessageSettings {@link DataSetReaderMessageType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  DataSetReaderMessageType getMessageSettingsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMessageSettingsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the DataSetReaderMessageType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends DataSetReaderMessageType> getMessageSettingsNodeAsync();

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
   * Get the Diagnostics {@link PubSubDiagnosticsDataSetReaderType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Diagnostics {@link PubSubDiagnosticsDataSetReaderType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubDiagnosticsDataSetReaderType getDiagnosticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiagnosticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the
   *     PubSubDiagnosticsDataSetReaderType Node or completes exceptionally if an error occurs
   *     creating or getting the Node.
   */
  CompletableFuture<? extends PubSubDiagnosticsDataSetReaderType> getDiagnosticsNodeAsync();

  /**
   * Get the SubscribedDataSet {@link SubscribedDataSetType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SubscribedDataSet {@link SubscribedDataSetType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SubscribedDataSetType getSubscribedDataSetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSubscribedDataSetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SubscribedDataSetType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SubscribedDataSetType> getSubscribedDataSetNodeAsync();
}
