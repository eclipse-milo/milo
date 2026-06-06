/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.2">https://reference.opcfoundation.org/v105/Core/docs/Part26/5.2</a>
 */
public interface LogObjectType extends BaseObjectType {
  QualifiedProperty<UInteger> MAX_RECORDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxRecords",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<Double> MAX_STORAGE_DURATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxStorageDuration",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UShort> MINIMUM_SEVERITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MinimumSeverity",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  /**
   * Get the local value of the MaxRecords Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxRecords Node.
   * @throws UaException if an error occurs creating or getting the MaxRecords Node.
   */
  UInteger getMaxRecords() throws UaException;

  /**
   * Set the local value of the MaxRecords Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxRecords Node.
   * @throws UaException if an error occurs creating or getting the MaxRecords Node.
   */
  void setMaxRecords(UInteger value) throws UaException;

  /**
   * Read the value of the MaxRecords Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxRecords() throws UaException;

  /**
   * Write a new value for the MaxRecords Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxRecords(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxRecords}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxRecordsAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxRecords}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxRecordsAsync(UInteger value);

  /**
   * Get the MaxRecords {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxRecords {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxRecordsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxRecordsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxRecordsNodeAsync();

  /**
   * Get the local value of the MaxStorageDuration Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxStorageDuration Node.
   * @throws UaException if an error occurs creating or getting the MaxStorageDuration Node.
   */
  Double getMaxStorageDuration() throws UaException;

  /**
   * Set the local value of the MaxStorageDuration Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxStorageDuration Node.
   * @throws UaException if an error occurs creating or getting the MaxStorageDuration Node.
   */
  void setMaxStorageDuration(Double value) throws UaException;

  /**
   * Read the value of the MaxStorageDuration Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readMaxStorageDuration() throws UaException;

  /**
   * Write a new value for the MaxStorageDuration Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxStorageDuration(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxStorageDuration}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readMaxStorageDurationAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxStorageDuration}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxStorageDurationAsync(Double value);

  /**
   * Get the MaxStorageDuration {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxStorageDuration {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxStorageDurationNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxStorageDurationNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxStorageDurationNodeAsync();

  /**
   * Get the local value of the MinimumSeverity Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MinimumSeverity Node.
   * @throws UaException if an error occurs creating or getting the MinimumSeverity Node.
   */
  UShort getMinimumSeverity() throws UaException;

  /**
   * Set the local value of the MinimumSeverity Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MinimumSeverity Node.
   * @throws UaException if an error occurs creating or getting the MinimumSeverity Node.
   */
  void setMinimumSeverity(UShort value) throws UaException;

  /**
   * Read the value of the MinimumSeverity Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readMinimumSeverity() throws UaException;

  /**
   * Write a new value for the MinimumSeverity Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMinimumSeverity(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMinimumSeverity}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readMinimumSeverityAsync();

  /**
   * An asynchronous implementation of {@link #writeMinimumSeverity}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMinimumSeverityAsync(UShort value);

  /**
   * Get the MinimumSeverity {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MinimumSeverity {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMinimumSeverityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMinimumSeverityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMinimumSeverityNodeAsync();
}
