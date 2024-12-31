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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.2</a>
 */
public interface HistoryServerCapabilitiesType extends BaseObjectType {
  QualifiedProperty<Boolean> ACCESS_HISTORY_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AccessHistoryDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> ACCESS_HISTORY_EVENTS_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AccessHistoryEventsCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<UInteger> MAX_RETURN_DATA_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxReturnDataValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_RETURN_EVENT_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxReturnEventValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<Boolean> INSERT_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InsertDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> REPLACE_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReplaceDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> UPDATE_DATA_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdateDataCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DELETE_RAW_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DeleteRawCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DELETE_AT_TIME_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DeleteAtTimeCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> INSERT_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InsertEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> REPLACE_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReplaceEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> UPDATE_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdateEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> DELETE_EVENT_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DeleteEventCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> INSERT_ANNOTATION_CAPABILITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InsertAnnotationCapability",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SERVER_TIMESTAMP_SUPPORTED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerTimestampSupported",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /**
   * Get the local value of the AccessHistoryDataCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AccessHistoryDataCapability Node.
   * @throws UaException if an error occurs creating or getting the AccessHistoryDataCapability
   *     Node.
   */
  Boolean getAccessHistoryDataCapability() throws UaException;

  /**
   * Set the local value of the AccessHistoryDataCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AccessHistoryDataCapability Node.
   * @throws UaException if an error occurs creating or getting the AccessHistoryDataCapability
   *     Node.
   */
  void setAccessHistoryDataCapability(Boolean value) throws UaException;

  /**
   * Read the value of the AccessHistoryDataCapability Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readAccessHistoryDataCapability() throws UaException;

  /**
   * Write a new value for the AccessHistoryDataCapability Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAccessHistoryDataCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAccessHistoryDataCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readAccessHistoryDataCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeAccessHistoryDataCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAccessHistoryDataCapabilityAsync(Boolean value);

  /**
   * Get the AccessHistoryDataCapability {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AccessHistoryDataCapability {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAccessHistoryDataCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAccessHistoryDataCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAccessHistoryDataCapabilityNodeAsync();

  /**
   * Get the local value of the AccessHistoryEventsCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AccessHistoryEventsCapability Node.
   * @throws UaException if an error occurs creating or getting the AccessHistoryEventsCapability
   *     Node.
   */
  Boolean getAccessHistoryEventsCapability() throws UaException;

  /**
   * Set the local value of the AccessHistoryEventsCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AccessHistoryEventsCapability Node.
   * @throws UaException if an error occurs creating or getting the AccessHistoryEventsCapability
   *     Node.
   */
  void setAccessHistoryEventsCapability(Boolean value) throws UaException;

  /**
   * Read the value of the AccessHistoryEventsCapability Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readAccessHistoryEventsCapability() throws UaException;

  /**
   * Write a new value for the AccessHistoryEventsCapability Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAccessHistoryEventsCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAccessHistoryEventsCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readAccessHistoryEventsCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeAccessHistoryEventsCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAccessHistoryEventsCapabilityAsync(Boolean value);

  /**
   * Get the AccessHistoryEventsCapability {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AccessHistoryEventsCapability {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAccessHistoryEventsCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAccessHistoryEventsCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAccessHistoryEventsCapabilityNodeAsync();

  /**
   * Get the local value of the MaxReturnDataValues Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxReturnDataValues Node.
   * @throws UaException if an error occurs creating or getting the MaxReturnDataValues Node.
   */
  UInteger getMaxReturnDataValues() throws UaException;

  /**
   * Set the local value of the MaxReturnDataValues Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxReturnDataValues Node.
   * @throws UaException if an error occurs creating or getting the MaxReturnDataValues Node.
   */
  void setMaxReturnDataValues(UInteger value) throws UaException;

  /**
   * Read the value of the MaxReturnDataValues Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxReturnDataValues() throws UaException;

  /**
   * Write a new value for the MaxReturnDataValues Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxReturnDataValues(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxReturnDataValues}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxReturnDataValuesAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxReturnDataValues}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxReturnDataValuesAsync(UInteger value);

  /**
   * Get the MaxReturnDataValues {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxReturnDataValues {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxReturnDataValuesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxReturnDataValuesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxReturnDataValuesNodeAsync();

  /**
   * Get the local value of the MaxReturnEventValues Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxReturnEventValues Node.
   * @throws UaException if an error occurs creating or getting the MaxReturnEventValues Node.
   */
  UInteger getMaxReturnEventValues() throws UaException;

  /**
   * Set the local value of the MaxReturnEventValues Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxReturnEventValues Node.
   * @throws UaException if an error occurs creating or getting the MaxReturnEventValues Node.
   */
  void setMaxReturnEventValues(UInteger value) throws UaException;

  /**
   * Read the value of the MaxReturnEventValues Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxReturnEventValues() throws UaException;

  /**
   * Write a new value for the MaxReturnEventValues Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxReturnEventValues(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxReturnEventValues}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxReturnEventValuesAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxReturnEventValues}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxReturnEventValuesAsync(UInteger value);

  /**
   * Get the MaxReturnEventValues {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxReturnEventValues {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxReturnEventValuesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxReturnEventValuesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxReturnEventValuesNodeAsync();

  /**
   * Get the local value of the InsertDataCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the InsertDataCapability Node.
   * @throws UaException if an error occurs creating or getting the InsertDataCapability Node.
   */
  Boolean getInsertDataCapability() throws UaException;

  /**
   * Set the local value of the InsertDataCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the InsertDataCapability Node.
   * @throws UaException if an error occurs creating or getting the InsertDataCapability Node.
   */
  void setInsertDataCapability(Boolean value) throws UaException;

  /**
   * Read the value of the InsertDataCapability Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readInsertDataCapability() throws UaException;

  /**
   * Write a new value for the InsertDataCapability Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInsertDataCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInsertDataCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readInsertDataCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeInsertDataCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeInsertDataCapabilityAsync(Boolean value);

  /**
   * Get the InsertDataCapability {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InsertDataCapability {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getInsertDataCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInsertDataCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getInsertDataCapabilityNodeAsync();

  /**
   * Get the local value of the ReplaceDataCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ReplaceDataCapability Node.
   * @throws UaException if an error occurs creating or getting the ReplaceDataCapability Node.
   */
  Boolean getReplaceDataCapability() throws UaException;

  /**
   * Set the local value of the ReplaceDataCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ReplaceDataCapability Node.
   * @throws UaException if an error occurs creating or getting the ReplaceDataCapability Node.
   */
  void setReplaceDataCapability(Boolean value) throws UaException;

  /**
   * Read the value of the ReplaceDataCapability Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readReplaceDataCapability() throws UaException;

  /**
   * Write a new value for the ReplaceDataCapability Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeReplaceDataCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readReplaceDataCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readReplaceDataCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeReplaceDataCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeReplaceDataCapabilityAsync(Boolean value);

  /**
   * Get the ReplaceDataCapability {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReplaceDataCapability {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getReplaceDataCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReplaceDataCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getReplaceDataCapabilityNodeAsync();

  /**
   * Get the local value of the UpdateDataCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UpdateDataCapability Node.
   * @throws UaException if an error occurs creating or getting the UpdateDataCapability Node.
   */
  Boolean getUpdateDataCapability() throws UaException;

  /**
   * Set the local value of the UpdateDataCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UpdateDataCapability Node.
   * @throws UaException if an error occurs creating or getting the UpdateDataCapability Node.
   */
  void setUpdateDataCapability(Boolean value) throws UaException;

  /**
   * Read the value of the UpdateDataCapability Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readUpdateDataCapability() throws UaException;

  /**
   * Write a new value for the UpdateDataCapability Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUpdateDataCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUpdateDataCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readUpdateDataCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeUpdateDataCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUpdateDataCapabilityAsync(Boolean value);

  /**
   * Get the UpdateDataCapability {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UpdateDataCapability {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUpdateDataCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUpdateDataCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUpdateDataCapabilityNodeAsync();

  /**
   * Get the local value of the DeleteRawCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DeleteRawCapability Node.
   * @throws UaException if an error occurs creating or getting the DeleteRawCapability Node.
   */
  Boolean getDeleteRawCapability() throws UaException;

  /**
   * Set the local value of the DeleteRawCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DeleteRawCapability Node.
   * @throws UaException if an error occurs creating or getting the DeleteRawCapability Node.
   */
  void setDeleteRawCapability(Boolean value) throws UaException;

  /**
   * Read the value of the DeleteRawCapability Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readDeleteRawCapability() throws UaException;

  /**
   * Write a new value for the DeleteRawCapability Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDeleteRawCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDeleteRawCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readDeleteRawCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeDeleteRawCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDeleteRawCapabilityAsync(Boolean value);

  /**
   * Get the DeleteRawCapability {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DeleteRawCapability {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDeleteRawCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDeleteRawCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDeleteRawCapabilityNodeAsync();

  /**
   * Get the local value of the DeleteAtTimeCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DeleteAtTimeCapability Node.
   * @throws UaException if an error occurs creating or getting the DeleteAtTimeCapability Node.
   */
  Boolean getDeleteAtTimeCapability() throws UaException;

  /**
   * Set the local value of the DeleteAtTimeCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DeleteAtTimeCapability Node.
   * @throws UaException if an error occurs creating or getting the DeleteAtTimeCapability Node.
   */
  void setDeleteAtTimeCapability(Boolean value) throws UaException;

  /**
   * Read the value of the DeleteAtTimeCapability Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readDeleteAtTimeCapability() throws UaException;

  /**
   * Write a new value for the DeleteAtTimeCapability Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDeleteAtTimeCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDeleteAtTimeCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readDeleteAtTimeCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeDeleteAtTimeCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDeleteAtTimeCapabilityAsync(Boolean value);

  /**
   * Get the DeleteAtTimeCapability {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DeleteAtTimeCapability {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDeleteAtTimeCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDeleteAtTimeCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDeleteAtTimeCapabilityNodeAsync();

  /**
   * Get the local value of the InsertEventCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the InsertEventCapability Node.
   * @throws UaException if an error occurs creating or getting the InsertEventCapability Node.
   */
  Boolean getInsertEventCapability() throws UaException;

  /**
   * Set the local value of the InsertEventCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the InsertEventCapability Node.
   * @throws UaException if an error occurs creating or getting the InsertEventCapability Node.
   */
  void setInsertEventCapability(Boolean value) throws UaException;

  /**
   * Read the value of the InsertEventCapability Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readInsertEventCapability() throws UaException;

  /**
   * Write a new value for the InsertEventCapability Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInsertEventCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInsertEventCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readInsertEventCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeInsertEventCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeInsertEventCapabilityAsync(Boolean value);

  /**
   * Get the InsertEventCapability {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InsertEventCapability {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getInsertEventCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInsertEventCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getInsertEventCapabilityNodeAsync();

  /**
   * Get the local value of the ReplaceEventCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ReplaceEventCapability Node.
   * @throws UaException if an error occurs creating or getting the ReplaceEventCapability Node.
   */
  Boolean getReplaceEventCapability() throws UaException;

  /**
   * Set the local value of the ReplaceEventCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ReplaceEventCapability Node.
   * @throws UaException if an error occurs creating or getting the ReplaceEventCapability Node.
   */
  void setReplaceEventCapability(Boolean value) throws UaException;

  /**
   * Read the value of the ReplaceEventCapability Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readReplaceEventCapability() throws UaException;

  /**
   * Write a new value for the ReplaceEventCapability Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeReplaceEventCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readReplaceEventCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readReplaceEventCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeReplaceEventCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeReplaceEventCapabilityAsync(Boolean value);

  /**
   * Get the ReplaceEventCapability {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReplaceEventCapability {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getReplaceEventCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReplaceEventCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getReplaceEventCapabilityNodeAsync();

  /**
   * Get the local value of the UpdateEventCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UpdateEventCapability Node.
   * @throws UaException if an error occurs creating or getting the UpdateEventCapability Node.
   */
  Boolean getUpdateEventCapability() throws UaException;

  /**
   * Set the local value of the UpdateEventCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UpdateEventCapability Node.
   * @throws UaException if an error occurs creating or getting the UpdateEventCapability Node.
   */
  void setUpdateEventCapability(Boolean value) throws UaException;

  /**
   * Read the value of the UpdateEventCapability Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readUpdateEventCapability() throws UaException;

  /**
   * Write a new value for the UpdateEventCapability Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUpdateEventCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUpdateEventCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readUpdateEventCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeUpdateEventCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUpdateEventCapabilityAsync(Boolean value);

  /**
   * Get the UpdateEventCapability {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UpdateEventCapability {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUpdateEventCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUpdateEventCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUpdateEventCapabilityNodeAsync();

  /**
   * Get the local value of the DeleteEventCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DeleteEventCapability Node.
   * @throws UaException if an error occurs creating or getting the DeleteEventCapability Node.
   */
  Boolean getDeleteEventCapability() throws UaException;

  /**
   * Set the local value of the DeleteEventCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DeleteEventCapability Node.
   * @throws UaException if an error occurs creating or getting the DeleteEventCapability Node.
   */
  void setDeleteEventCapability(Boolean value) throws UaException;

  /**
   * Read the value of the DeleteEventCapability Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readDeleteEventCapability() throws UaException;

  /**
   * Write a new value for the DeleteEventCapability Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDeleteEventCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDeleteEventCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readDeleteEventCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeDeleteEventCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDeleteEventCapabilityAsync(Boolean value);

  /**
   * Get the DeleteEventCapability {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DeleteEventCapability {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDeleteEventCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDeleteEventCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDeleteEventCapabilityNodeAsync();

  /**
   * Get the local value of the InsertAnnotationCapability Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the InsertAnnotationCapability Node.
   * @throws UaException if an error occurs creating or getting the InsertAnnotationCapability Node.
   */
  Boolean getInsertAnnotationCapability() throws UaException;

  /**
   * Set the local value of the InsertAnnotationCapability Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the InsertAnnotationCapability Node.
   * @throws UaException if an error occurs creating or getting the InsertAnnotationCapability Node.
   */
  void setInsertAnnotationCapability(Boolean value) throws UaException;

  /**
   * Read the value of the InsertAnnotationCapability Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readInsertAnnotationCapability() throws UaException;

  /**
   * Write a new value for the InsertAnnotationCapability Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInsertAnnotationCapability(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInsertAnnotationCapability}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readInsertAnnotationCapabilityAsync();

  /**
   * An asynchronous implementation of {@link #writeInsertAnnotationCapability}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeInsertAnnotationCapabilityAsync(Boolean value);

  /**
   * Get the InsertAnnotationCapability {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InsertAnnotationCapability {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getInsertAnnotationCapabilityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInsertAnnotationCapabilityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getInsertAnnotationCapabilityNodeAsync();

  /**
   * Get the local value of the ServerTimestampSupported Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ServerTimestampSupported Node.
   * @throws UaException if an error occurs creating or getting the ServerTimestampSupported Node.
   */
  Boolean getServerTimestampSupported() throws UaException;

  /**
   * Set the local value of the ServerTimestampSupported Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ServerTimestampSupported Node.
   * @throws UaException if an error occurs creating or getting the ServerTimestampSupported Node.
   */
  void setServerTimestampSupported(Boolean value) throws UaException;

  /**
   * Read the value of the ServerTimestampSupported Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readServerTimestampSupported() throws UaException;

  /**
   * Write a new value for the ServerTimestampSupported Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeServerTimestampSupported(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readServerTimestampSupported}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readServerTimestampSupportedAsync();

  /**
   * An asynchronous implementation of {@link #writeServerTimestampSupported}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeServerTimestampSupportedAsync(Boolean value);

  /**
   * Get the ServerTimestampSupported {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ServerTimestampSupported {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getServerTimestampSupportedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServerTimestampSupportedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getServerTimestampSupportedNodeAsync();

  /**
   * Get the AggregateFunctions {@link FolderType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AggregateFunctions {@link FolderType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FolderType getAggregateFunctionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAggregateFunctionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FolderType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FolderType> getAggregateFunctionsNodeAsync();
}
