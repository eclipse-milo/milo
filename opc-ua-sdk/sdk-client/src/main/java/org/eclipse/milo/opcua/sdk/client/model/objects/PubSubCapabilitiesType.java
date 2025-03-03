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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1</a>
 */
public interface PubSubCapabilitiesType extends BaseObjectType {
  QualifiedProperty<UInteger> MAX_PUB_SUB_CONNECTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxPubSubConnections",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_WRITER_GROUPS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxWriterGroups",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_READER_GROUPS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxReaderGroups",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_DATA_SET_WRITERS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxDataSetWriters",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_DATA_SET_READERS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxDataSetReaders",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_FIELDS_PER_DATA_SET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxFieldsPerDataSet",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_DATA_SET_WRITERS_PER_GROUP =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxDataSetWritersPerGroup",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_SECURITY_GROUPS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxSecurityGroups",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_PUSH_TARGETS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxPushTargets",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_PUBLISHED_DATA_SETS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxPublishedDataSets",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_STANDALONE_SUBSCRIBED_DATA_SETS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxStandaloneSubscribedDataSets",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NETWORK_MESSAGE_SIZE_DATAGRAM =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNetworkMessageSizeDatagram",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NETWORK_MESSAGE_SIZE_BROKER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNetworkMessageSizeBroker",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<Boolean> SUPPORT_SECURITY_KEY_PULL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportSecurityKeyPull",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SUPPORT_SECURITY_KEY_PUSH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportSecurityKeyPush",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SUPPORT_SECURITY_KEY_SERVER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportSecurityKeyServer",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /**
   * Get the local value of the MaxPubSubConnections Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxPubSubConnections Node.
   * @throws UaException if an error occurs creating or getting the MaxPubSubConnections Node.
   */
  UInteger getMaxPubSubConnections() throws UaException;

  /**
   * Set the local value of the MaxPubSubConnections Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxPubSubConnections Node.
   * @throws UaException if an error occurs creating or getting the MaxPubSubConnections Node.
   */
  void setMaxPubSubConnections(UInteger value) throws UaException;

  /**
   * Read the value of the MaxPubSubConnections Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxPubSubConnections() throws UaException;

  /**
   * Write a new value for the MaxPubSubConnections Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxPubSubConnections(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxPubSubConnections}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxPubSubConnectionsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxPubSubConnections}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxPubSubConnectionsAsync(UInteger value);

  /**
   * Get the MaxPubSubConnections {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxPubSubConnections {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxPubSubConnectionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxPubSubConnectionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxPubSubConnectionsNodeAsync();

  /**
   * Get the local value of the MaxWriterGroups Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxWriterGroups Node.
   * @throws UaException if an error occurs creating or getting the MaxWriterGroups Node.
   */
  UInteger getMaxWriterGroups() throws UaException;

  /**
   * Set the local value of the MaxWriterGroups Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxWriterGroups Node.
   * @throws UaException if an error occurs creating or getting the MaxWriterGroups Node.
   */
  void setMaxWriterGroups(UInteger value) throws UaException;

  /**
   * Read the value of the MaxWriterGroups Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxWriterGroups() throws UaException;

  /**
   * Write a new value for the MaxWriterGroups Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxWriterGroups(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxWriterGroups}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxWriterGroupsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxWriterGroups}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxWriterGroupsAsync(UInteger value);

  /**
   * Get the MaxWriterGroups {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxWriterGroups {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxWriterGroupsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxWriterGroupsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxWriterGroupsNodeAsync();

  /**
   * Get the local value of the MaxReaderGroups Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxReaderGroups Node.
   * @throws UaException if an error occurs creating or getting the MaxReaderGroups Node.
   */
  UInteger getMaxReaderGroups() throws UaException;

  /**
   * Set the local value of the MaxReaderGroups Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxReaderGroups Node.
   * @throws UaException if an error occurs creating or getting the MaxReaderGroups Node.
   */
  void setMaxReaderGroups(UInteger value) throws UaException;

  /**
   * Read the value of the MaxReaderGroups Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxReaderGroups() throws UaException;

  /**
   * Write a new value for the MaxReaderGroups Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxReaderGroups(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxReaderGroups}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxReaderGroupsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxReaderGroups}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxReaderGroupsAsync(UInteger value);

  /**
   * Get the MaxReaderGroups {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxReaderGroups {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxReaderGroupsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxReaderGroupsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxReaderGroupsNodeAsync();

  /**
   * Get the local value of the MaxDataSetWriters Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxDataSetWriters Node.
   * @throws UaException if an error occurs creating or getting the MaxDataSetWriters Node.
   */
  UInteger getMaxDataSetWriters() throws UaException;

  /**
   * Set the local value of the MaxDataSetWriters Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxDataSetWriters Node.
   * @throws UaException if an error occurs creating or getting the MaxDataSetWriters Node.
   */
  void setMaxDataSetWriters(UInteger value) throws UaException;

  /**
   * Read the value of the MaxDataSetWriters Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxDataSetWriters() throws UaException;

  /**
   * Write a new value for the MaxDataSetWriters Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxDataSetWriters(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxDataSetWriters}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxDataSetWritersAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxDataSetWriters}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxDataSetWritersAsync(UInteger value);

  /**
   * Get the MaxDataSetWriters {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxDataSetWriters {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxDataSetWritersNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxDataSetWritersNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxDataSetWritersNodeAsync();

  /**
   * Get the local value of the MaxDataSetReaders Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxDataSetReaders Node.
   * @throws UaException if an error occurs creating or getting the MaxDataSetReaders Node.
   */
  UInteger getMaxDataSetReaders() throws UaException;

  /**
   * Set the local value of the MaxDataSetReaders Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxDataSetReaders Node.
   * @throws UaException if an error occurs creating or getting the MaxDataSetReaders Node.
   */
  void setMaxDataSetReaders(UInteger value) throws UaException;

  /**
   * Read the value of the MaxDataSetReaders Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxDataSetReaders() throws UaException;

  /**
   * Write a new value for the MaxDataSetReaders Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxDataSetReaders(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxDataSetReaders}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxDataSetReadersAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxDataSetReaders}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxDataSetReadersAsync(UInteger value);

  /**
   * Get the MaxDataSetReaders {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxDataSetReaders {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxDataSetReadersNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxDataSetReadersNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxDataSetReadersNodeAsync();

  /**
   * Get the local value of the MaxFieldsPerDataSet Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxFieldsPerDataSet Node.
   * @throws UaException if an error occurs creating or getting the MaxFieldsPerDataSet Node.
   */
  UInteger getMaxFieldsPerDataSet() throws UaException;

  /**
   * Set the local value of the MaxFieldsPerDataSet Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxFieldsPerDataSet Node.
   * @throws UaException if an error occurs creating or getting the MaxFieldsPerDataSet Node.
   */
  void setMaxFieldsPerDataSet(UInteger value) throws UaException;

  /**
   * Read the value of the MaxFieldsPerDataSet Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxFieldsPerDataSet() throws UaException;

  /**
   * Write a new value for the MaxFieldsPerDataSet Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxFieldsPerDataSet(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxFieldsPerDataSet}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxFieldsPerDataSetAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxFieldsPerDataSet}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxFieldsPerDataSetAsync(UInteger value);

  /**
   * Get the MaxFieldsPerDataSet {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxFieldsPerDataSet {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxFieldsPerDataSetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxFieldsPerDataSetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxFieldsPerDataSetNodeAsync();

  /**
   * Get the local value of the MaxDataSetWritersPerGroup Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxDataSetWritersPerGroup Node.
   * @throws UaException if an error occurs creating or getting the MaxDataSetWritersPerGroup Node.
   */
  UInteger getMaxDataSetWritersPerGroup() throws UaException;

  /**
   * Set the local value of the MaxDataSetWritersPerGroup Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxDataSetWritersPerGroup Node.
   * @throws UaException if an error occurs creating or getting the MaxDataSetWritersPerGroup Node.
   */
  void setMaxDataSetWritersPerGroup(UInteger value) throws UaException;

  /**
   * Read the value of the MaxDataSetWritersPerGroup Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxDataSetWritersPerGroup() throws UaException;

  /**
   * Write a new value for the MaxDataSetWritersPerGroup Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxDataSetWritersPerGroup(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxDataSetWritersPerGroup}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxDataSetWritersPerGroupAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxDataSetWritersPerGroup}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxDataSetWritersPerGroupAsync(UInteger value);

  /**
   * Get the MaxDataSetWritersPerGroup {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxDataSetWritersPerGroup {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxDataSetWritersPerGroupNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxDataSetWritersPerGroupNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxDataSetWritersPerGroupNodeAsync();

  /**
   * Get the local value of the MaxSecurityGroups Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxSecurityGroups Node.
   * @throws UaException if an error occurs creating or getting the MaxSecurityGroups Node.
   */
  UInteger getMaxSecurityGroups() throws UaException;

  /**
   * Set the local value of the MaxSecurityGroups Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxSecurityGroups Node.
   * @throws UaException if an error occurs creating or getting the MaxSecurityGroups Node.
   */
  void setMaxSecurityGroups(UInteger value) throws UaException;

  /**
   * Read the value of the MaxSecurityGroups Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxSecurityGroups() throws UaException;

  /**
   * Write a new value for the MaxSecurityGroups Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxSecurityGroups(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxSecurityGroups}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxSecurityGroupsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxSecurityGroups}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxSecurityGroupsAsync(UInteger value);

  /**
   * Get the MaxSecurityGroups {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxSecurityGroups {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxSecurityGroupsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxSecurityGroupsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxSecurityGroupsNodeAsync();

  /**
   * Get the local value of the MaxPushTargets Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxPushTargets Node.
   * @throws UaException if an error occurs creating or getting the MaxPushTargets Node.
   */
  UInteger getMaxPushTargets() throws UaException;

  /**
   * Set the local value of the MaxPushTargets Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxPushTargets Node.
   * @throws UaException if an error occurs creating or getting the MaxPushTargets Node.
   */
  void setMaxPushTargets(UInteger value) throws UaException;

  /**
   * Read the value of the MaxPushTargets Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxPushTargets() throws UaException;

  /**
   * Write a new value for the MaxPushTargets Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxPushTargets(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxPushTargets}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxPushTargetsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxPushTargets}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxPushTargetsAsync(UInteger value);

  /**
   * Get the MaxPushTargets {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxPushTargets {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxPushTargetsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxPushTargetsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxPushTargetsNodeAsync();

  /**
   * Get the local value of the MaxPublishedDataSets Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxPublishedDataSets Node.
   * @throws UaException if an error occurs creating or getting the MaxPublishedDataSets Node.
   */
  UInteger getMaxPublishedDataSets() throws UaException;

  /**
   * Set the local value of the MaxPublishedDataSets Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxPublishedDataSets Node.
   * @throws UaException if an error occurs creating or getting the MaxPublishedDataSets Node.
   */
  void setMaxPublishedDataSets(UInteger value) throws UaException;

  /**
   * Read the value of the MaxPublishedDataSets Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxPublishedDataSets() throws UaException;

  /**
   * Write a new value for the MaxPublishedDataSets Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxPublishedDataSets(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxPublishedDataSets}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxPublishedDataSetsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxPublishedDataSets}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxPublishedDataSetsAsync(UInteger value);

  /**
   * Get the MaxPublishedDataSets {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxPublishedDataSets {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxPublishedDataSetsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxPublishedDataSetsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxPublishedDataSetsNodeAsync();

  /**
   * Get the local value of the MaxStandaloneSubscribedDataSets Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxStandaloneSubscribedDataSets Node.
   * @throws UaException if an error occurs creating or getting the MaxStandaloneSubscribedDataSets
   *     Node.
   */
  UInteger getMaxStandaloneSubscribedDataSets() throws UaException;

  /**
   * Set the local value of the MaxStandaloneSubscribedDataSets Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxStandaloneSubscribedDataSets Node.
   * @throws UaException if an error occurs creating or getting the MaxStandaloneSubscribedDataSets
   *     Node.
   */
  void setMaxStandaloneSubscribedDataSets(UInteger value) throws UaException;

  /**
   * Read the value of the MaxStandaloneSubscribedDataSets Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxStandaloneSubscribedDataSets() throws UaException;

  /**
   * Write a new value for the MaxStandaloneSubscribedDataSets Node to the server and update the
   * local value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxStandaloneSubscribedDataSets(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxStandaloneSubscribedDataSets}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxStandaloneSubscribedDataSetsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxStandaloneSubscribedDataSets}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxStandaloneSubscribedDataSetsAsync(UInteger value);

  /**
   * Get the MaxStandaloneSubscribedDataSets {@link PropertyType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxStandaloneSubscribedDataSets {@link PropertyType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxStandaloneSubscribedDataSetsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxStandaloneSubscribedDataSetsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxStandaloneSubscribedDataSetsNodeAsync();

  /**
   * Get the local value of the MaxNetworkMessageSizeDatagram Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxNetworkMessageSizeDatagram Node.
   * @throws UaException if an error occurs creating or getting the MaxNetworkMessageSizeDatagram
   *     Node.
   */
  UInteger getMaxNetworkMessageSizeDatagram() throws UaException;

  /**
   * Set the local value of the MaxNetworkMessageSizeDatagram Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxNetworkMessageSizeDatagram Node.
   * @throws UaException if an error occurs creating or getting the MaxNetworkMessageSizeDatagram
   *     Node.
   */
  void setMaxNetworkMessageSizeDatagram(UInteger value) throws UaException;

  /**
   * Read the value of the MaxNetworkMessageSizeDatagram Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxNetworkMessageSizeDatagram() throws UaException;

  /**
   * Write a new value for the MaxNetworkMessageSizeDatagram Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxNetworkMessageSizeDatagram(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxNetworkMessageSizeDatagram}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxNetworkMessageSizeDatagramAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxNetworkMessageSizeDatagram}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxNetworkMessageSizeDatagramAsync(UInteger value);

  /**
   * Get the MaxNetworkMessageSizeDatagram {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxNetworkMessageSizeDatagram {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxNetworkMessageSizeDatagramNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxNetworkMessageSizeDatagramNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxNetworkMessageSizeDatagramNodeAsync();

  /**
   * Get the local value of the MaxNetworkMessageSizeBroker Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxNetworkMessageSizeBroker Node.
   * @throws UaException if an error occurs creating or getting the MaxNetworkMessageSizeBroker
   *     Node.
   */
  UInteger getMaxNetworkMessageSizeBroker() throws UaException;

  /**
   * Set the local value of the MaxNetworkMessageSizeBroker Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxNetworkMessageSizeBroker Node.
   * @throws UaException if an error occurs creating or getting the MaxNetworkMessageSizeBroker
   *     Node.
   */
  void setMaxNetworkMessageSizeBroker(UInteger value) throws UaException;

  /**
   * Read the value of the MaxNetworkMessageSizeBroker Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxNetworkMessageSizeBroker() throws UaException;

  /**
   * Write a new value for the MaxNetworkMessageSizeBroker Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxNetworkMessageSizeBroker(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxNetworkMessageSizeBroker}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxNetworkMessageSizeBrokerAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxNetworkMessageSizeBroker}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxNetworkMessageSizeBrokerAsync(UInteger value);

  /**
   * Get the MaxNetworkMessageSizeBroker {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxNetworkMessageSizeBroker {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxNetworkMessageSizeBrokerNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxNetworkMessageSizeBrokerNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxNetworkMessageSizeBrokerNodeAsync();

  /**
   * Get the local value of the SupportSecurityKeyPull Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SupportSecurityKeyPull Node.
   * @throws UaException if an error occurs creating or getting the SupportSecurityKeyPull Node.
   */
  Boolean getSupportSecurityKeyPull() throws UaException;

  /**
   * Set the local value of the SupportSecurityKeyPull Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SupportSecurityKeyPull Node.
   * @throws UaException if an error occurs creating or getting the SupportSecurityKeyPull Node.
   */
  void setSupportSecurityKeyPull(Boolean value) throws UaException;

  /**
   * Read the value of the SupportSecurityKeyPull Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readSupportSecurityKeyPull() throws UaException;

  /**
   * Write a new value for the SupportSecurityKeyPull Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSupportSecurityKeyPull(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSupportSecurityKeyPull}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readSupportSecurityKeyPullAsync();

  /**
   * An asynchronous implementation of {@link #writeSupportSecurityKeyPull}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSupportSecurityKeyPullAsync(Boolean value);

  /**
   * Get the SupportSecurityKeyPull {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SupportSecurityKeyPull {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSupportSecurityKeyPullNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSupportSecurityKeyPullNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSupportSecurityKeyPullNodeAsync();

  /**
   * Get the local value of the SupportSecurityKeyPush Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SupportSecurityKeyPush Node.
   * @throws UaException if an error occurs creating or getting the SupportSecurityKeyPush Node.
   */
  Boolean getSupportSecurityKeyPush() throws UaException;

  /**
   * Set the local value of the SupportSecurityKeyPush Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SupportSecurityKeyPush Node.
   * @throws UaException if an error occurs creating or getting the SupportSecurityKeyPush Node.
   */
  void setSupportSecurityKeyPush(Boolean value) throws UaException;

  /**
   * Read the value of the SupportSecurityKeyPush Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readSupportSecurityKeyPush() throws UaException;

  /**
   * Write a new value for the SupportSecurityKeyPush Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSupportSecurityKeyPush(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSupportSecurityKeyPush}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readSupportSecurityKeyPushAsync();

  /**
   * An asynchronous implementation of {@link #writeSupportSecurityKeyPush}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSupportSecurityKeyPushAsync(Boolean value);

  /**
   * Get the SupportSecurityKeyPush {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SupportSecurityKeyPush {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSupportSecurityKeyPushNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSupportSecurityKeyPushNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSupportSecurityKeyPushNodeAsync();

  /**
   * Get the local value of the SupportSecurityKeyServer Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SupportSecurityKeyServer Node.
   * @throws UaException if an error occurs creating or getting the SupportSecurityKeyServer Node.
   */
  Boolean getSupportSecurityKeyServer() throws UaException;

  /**
   * Set the local value of the SupportSecurityKeyServer Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SupportSecurityKeyServer Node.
   * @throws UaException if an error occurs creating or getting the SupportSecurityKeyServer Node.
   */
  void setSupportSecurityKeyServer(Boolean value) throws UaException;

  /**
   * Read the value of the SupportSecurityKeyServer Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readSupportSecurityKeyServer() throws UaException;

  /**
   * Write a new value for the SupportSecurityKeyServer Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSupportSecurityKeyServer(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSupportSecurityKeyServer}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readSupportSecurityKeyServerAsync();

  /**
   * An asynchronous implementation of {@link #writeSupportSecurityKeyServer}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSupportSecurityKeyServerAsync(Boolean value);

  /**
   * Get the SupportSecurityKeyServer {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SupportSecurityKeyServer {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSupportSecurityKeyServerNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSupportSecurityKeyServerNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSupportSecurityKeyServerNodeAsync();
}
