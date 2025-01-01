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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListValidationOptions;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.1</a>
 */
public interface TrustListType extends FileType {
  QualifiedProperty<DateTime> LAST_UPDATE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastUpdateTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<Double> UPDATE_FREQUENCY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UpdateFrequency",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> ACTIVITY_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ActivityTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<TrustListValidationOptions> DEFAULT_VALIDATION_OPTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultValidationOptions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23564"),
          -1,
          TrustListValidationOptions.class);

  /**
   * Get the local value of the LastUpdateTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastUpdateTime Node.
   * @throws UaException if an error occurs creating or getting the LastUpdateTime Node.
   */
  DateTime getLastUpdateTime() throws UaException;

  /**
   * Set the local value of the LastUpdateTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastUpdateTime Node.
   * @throws UaException if an error occurs creating or getting the LastUpdateTime Node.
   */
  void setLastUpdateTime(DateTime value) throws UaException;

  /**
   * Read the value of the LastUpdateTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readLastUpdateTime() throws UaException;

  /**
   * Write a new value for the LastUpdateTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastUpdateTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastUpdateTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readLastUpdateTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeLastUpdateTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastUpdateTimeAsync(DateTime value);

  /**
   * Get the LastUpdateTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastUpdateTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastUpdateTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastUpdateTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastUpdateTimeNodeAsync();

  /**
   * Get the local value of the UpdateFrequency Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UpdateFrequency Node.
   * @throws UaException if an error occurs creating or getting the UpdateFrequency Node.
   */
  Double getUpdateFrequency() throws UaException;

  /**
   * Set the local value of the UpdateFrequency Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UpdateFrequency Node.
   * @throws UaException if an error occurs creating or getting the UpdateFrequency Node.
   */
  void setUpdateFrequency(Double value) throws UaException;

  /**
   * Read the value of the UpdateFrequency Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readUpdateFrequency() throws UaException;

  /**
   * Write a new value for the UpdateFrequency Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUpdateFrequency(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUpdateFrequency}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readUpdateFrequencyAsync();

  /**
   * An asynchronous implementation of {@link #writeUpdateFrequency}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUpdateFrequencyAsync(Double value);

  /**
   * Get the UpdateFrequency {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UpdateFrequency {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUpdateFrequencyNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUpdateFrequencyNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUpdateFrequencyNodeAsync();

  /**
   * Get the local value of the ActivityTimeout Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ActivityTimeout Node.
   * @throws UaException if an error occurs creating or getting the ActivityTimeout Node.
   */
  Double getActivityTimeout() throws UaException;

  /**
   * Set the local value of the ActivityTimeout Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ActivityTimeout Node.
   * @throws UaException if an error occurs creating or getting the ActivityTimeout Node.
   */
  void setActivityTimeout(Double value) throws UaException;

  /**
   * Read the value of the ActivityTimeout Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readActivityTimeout() throws UaException;

  /**
   * Write a new value for the ActivityTimeout Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeActivityTimeout(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readActivityTimeout}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readActivityTimeoutAsync();

  /**
   * An asynchronous implementation of {@link #writeActivityTimeout}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeActivityTimeoutAsync(Double value);

  /**
   * Get the ActivityTimeout {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ActivityTimeout {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getActivityTimeoutNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getActivityTimeoutNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getActivityTimeoutNodeAsync();

  /**
   * Get the local value of the DefaultValidationOptions Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DefaultValidationOptions Node.
   * @throws UaException if an error occurs creating or getting the DefaultValidationOptions Node.
   */
  TrustListValidationOptions getDefaultValidationOptions() throws UaException;

  /**
   * Set the local value of the DefaultValidationOptions Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DefaultValidationOptions Node.
   * @throws UaException if an error occurs creating or getting the DefaultValidationOptions Node.
   */
  void setDefaultValidationOptions(TrustListValidationOptions value) throws UaException;

  /**
   * Read the value of the DefaultValidationOptions Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link TrustListValidationOptions} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  TrustListValidationOptions readDefaultValidationOptions() throws UaException;

  /**
   * Write a new value for the DefaultValidationOptions Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link TrustListValidationOptions} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDefaultValidationOptions(TrustListValidationOptions value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDefaultValidationOptions}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends TrustListValidationOptions> readDefaultValidationOptionsAsync();

  /**
   * An asynchronous implementation of {@link #writeDefaultValidationOptions}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDefaultValidationOptionsAsync(
      TrustListValidationOptions value);

  /**
   * Get the DefaultValidationOptions {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DefaultValidationOptions {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDefaultValidationOptionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDefaultValidationOptionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDefaultValidationOptionsNodeAsync();
}
