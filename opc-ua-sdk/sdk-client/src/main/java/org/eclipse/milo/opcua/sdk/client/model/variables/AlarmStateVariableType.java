/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2</a>
 */
public interface AlarmStateVariableType extends BaseDataVariableType {
  QualifiedProperty<UShort> HIGHEST_ACTIVE_SEVERITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighestActiveSeverity",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> HIGHEST_UNACK_SEVERITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighestUnackSeverity",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UInteger> ACTIVE_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ActiveCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> UNACKNOWLEDGED_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnacknowledgedCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> UNCONFIRMED_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnconfirmedCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<ContentFilter> FILTER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Filter",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=586"),
          -1,
          ContentFilter.class);

  /**
   * Get the local value of the HighestActiveSeverity Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HighestActiveSeverity Node.
   * @throws UaException if an error occurs creating or getting the HighestActiveSeverity Node.
   */
  UShort getHighestActiveSeverity() throws UaException;

  /**
   * Set the local value of the HighestActiveSeverity Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HighestActiveSeverity Node.
   * @throws UaException if an error occurs creating or getting the HighestActiveSeverity Node.
   */
  void setHighestActiveSeverity(UShort value) throws UaException;

  /**
   * Read the value of the HighestActiveSeverity Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readHighestActiveSeverity() throws UaException;

  /**
   * Write a new value for the HighestActiveSeverity Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHighestActiveSeverity(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHighestActiveSeverity}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readHighestActiveSeverityAsync();

  /**
   * An asynchronous implementation of {@link #writeHighestActiveSeverity}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHighestActiveSeverityAsync(UShort value);

  /**
   * Get the HighestActiveSeverity {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HighestActiveSeverity {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getHighestActiveSeverityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHighestActiveSeverityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getHighestActiveSeverityNodeAsync();

  /**
   * Get the local value of the HighestUnackSeverity Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HighestUnackSeverity Node.
   * @throws UaException if an error occurs creating or getting the HighestUnackSeverity Node.
   */
  UShort getHighestUnackSeverity() throws UaException;

  /**
   * Set the local value of the HighestUnackSeverity Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HighestUnackSeverity Node.
   * @throws UaException if an error occurs creating or getting the HighestUnackSeverity Node.
   */
  void setHighestUnackSeverity(UShort value) throws UaException;

  /**
   * Read the value of the HighestUnackSeverity Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readHighestUnackSeverity() throws UaException;

  /**
   * Write a new value for the HighestUnackSeverity Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHighestUnackSeverity(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHighestUnackSeverity}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readHighestUnackSeverityAsync();

  /**
   * An asynchronous implementation of {@link #writeHighestUnackSeverity}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHighestUnackSeverityAsync(UShort value);

  /**
   * Get the HighestUnackSeverity {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HighestUnackSeverity {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getHighestUnackSeverityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHighestUnackSeverityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getHighestUnackSeverityNodeAsync();

  /**
   * Get the local value of the ActiveCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ActiveCount Node.
   * @throws UaException if an error occurs creating or getting the ActiveCount Node.
   */
  UInteger getActiveCount() throws UaException;

  /**
   * Set the local value of the ActiveCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ActiveCount Node.
   * @throws UaException if an error occurs creating or getting the ActiveCount Node.
   */
  void setActiveCount(UInteger value) throws UaException;

  /**
   * Read the value of the ActiveCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readActiveCount() throws UaException;

  /**
   * Write a new value for the ActiveCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeActiveCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readActiveCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readActiveCountAsync();

  /**
   * An asynchronous implementation of {@link #writeActiveCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeActiveCountAsync(UInteger value);

  /**
   * Get the ActiveCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ActiveCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getActiveCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getActiveCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getActiveCountNodeAsync();

  /**
   * Get the local value of the UnacknowledgedCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UnacknowledgedCount Node.
   * @throws UaException if an error occurs creating or getting the UnacknowledgedCount Node.
   */
  UInteger getUnacknowledgedCount() throws UaException;

  /**
   * Set the local value of the UnacknowledgedCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UnacknowledgedCount Node.
   * @throws UaException if an error occurs creating or getting the UnacknowledgedCount Node.
   */
  void setUnacknowledgedCount(UInteger value) throws UaException;

  /**
   * Read the value of the UnacknowledgedCount Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readUnacknowledgedCount() throws UaException;

  /**
   * Write a new value for the UnacknowledgedCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUnacknowledgedCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUnacknowledgedCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readUnacknowledgedCountAsync();

  /**
   * An asynchronous implementation of {@link #writeUnacknowledgedCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUnacknowledgedCountAsync(UInteger value);

  /**
   * Get the UnacknowledgedCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UnacknowledgedCount {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUnacknowledgedCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUnacknowledgedCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUnacknowledgedCountNodeAsync();

  /**
   * Get the local value of the UnconfirmedCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UnconfirmedCount Node.
   * @throws UaException if an error occurs creating or getting the UnconfirmedCount Node.
   */
  UInteger getUnconfirmedCount() throws UaException;

  /**
   * Set the local value of the UnconfirmedCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UnconfirmedCount Node.
   * @throws UaException if an error occurs creating or getting the UnconfirmedCount Node.
   */
  void setUnconfirmedCount(UInteger value) throws UaException;

  /**
   * Read the value of the UnconfirmedCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readUnconfirmedCount() throws UaException;

  /**
   * Write a new value for the UnconfirmedCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUnconfirmedCount(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUnconfirmedCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readUnconfirmedCountAsync();

  /**
   * An asynchronous implementation of {@link #writeUnconfirmedCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUnconfirmedCountAsync(UInteger value);

  /**
   * Get the UnconfirmedCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UnconfirmedCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUnconfirmedCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUnconfirmedCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUnconfirmedCountNodeAsync();

  /**
   * Get the local value of the Filter Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Filter Node.
   * @throws UaException if an error occurs creating or getting the Filter Node.
   */
  ContentFilter getFilter() throws UaException;

  /**
   * Set the local value of the Filter Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Filter Node.
   * @throws UaException if an error occurs creating or getting the Filter Node.
   */
  void setFilter(ContentFilter value) throws UaException;

  /**
   * Read the value of the Filter Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link ContentFilter} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ContentFilter readFilter() throws UaException;

  /**
   * Write a new value for the Filter Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link ContentFilter} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeFilter(ContentFilter value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readFilter}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ContentFilter> readFilterAsync();

  /**
   * An asynchronous implementation of {@link #writeFilter}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeFilterAsync(ContentFilter value);

  /**
   * Get the Filter {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Filter {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getFilterNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getFilterNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getFilterNodeAsync();
}
