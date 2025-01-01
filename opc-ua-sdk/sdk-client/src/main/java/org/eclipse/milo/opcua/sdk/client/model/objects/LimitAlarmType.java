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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.18">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.18</a>
 */
public interface LimitAlarmType extends AlarmConditionType {
  QualifiedProperty<Double> HIGH_HIGH_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighHighLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> HIGH_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> LOW_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LowLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> LOW_LOW_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LowLowLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> BASE_HIGH_HIGH_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BaseHighHighLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> BASE_HIGH_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BaseHighLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> BASE_LOW_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BaseLowLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> BASE_LOW_LOW_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BaseLowLowLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<UShort> SEVERITY_HIGH_HIGH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SeverityHighHigh",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> SEVERITY_HIGH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SeverityHigh",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> SEVERITY_LOW =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SeverityLow",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> SEVERITY_LOW_LOW =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SeverityLowLow",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<Double> HIGH_HIGH_DEADBAND =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighHighDeadband",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> HIGH_DEADBAND =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HighDeadband",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> LOW_DEADBAND =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LowDeadband",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  QualifiedProperty<Double> LOW_LOW_DEADBAND =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LowLowDeadband",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  /**
   * Get the local value of the HighHighLimit Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HighHighLimit Node.
   * @throws UaException if an error occurs creating or getting the HighHighLimit Node.
   */
  Double getHighHighLimit() throws UaException;

  /**
   * Set the local value of the HighHighLimit Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HighHighLimit Node.
   * @throws UaException if an error occurs creating or getting the HighHighLimit Node.
   */
  void setHighHighLimit(Double value) throws UaException;

  /**
   * Read the value of the HighHighLimit Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readHighHighLimit() throws UaException;

  /**
   * Write a new value for the HighHighLimit Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHighHighLimit(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHighHighLimit}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readHighHighLimitAsync();

  /**
   * An asynchronous implementation of {@link #writeHighHighLimit}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHighHighLimitAsync(Double value);

  /**
   * Get the HighHighLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HighHighLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getHighHighLimitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHighHighLimitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getHighHighLimitNodeAsync();

  /**
   * Get the local value of the HighLimit Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HighLimit Node.
   * @throws UaException if an error occurs creating or getting the HighLimit Node.
   */
  Double getHighLimit() throws UaException;

  /**
   * Set the local value of the HighLimit Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HighLimit Node.
   * @throws UaException if an error occurs creating or getting the HighLimit Node.
   */
  void setHighLimit(Double value) throws UaException;

  /**
   * Read the value of the HighLimit Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readHighLimit() throws UaException;

  /**
   * Write a new value for the HighLimit Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHighLimit(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHighLimit}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readHighLimitAsync();

  /**
   * An asynchronous implementation of {@link #writeHighLimit}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHighLimitAsync(Double value);

  /**
   * Get the HighLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HighLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getHighLimitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHighLimitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getHighLimitNodeAsync();

  /**
   * Get the local value of the LowLimit Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LowLimit Node.
   * @throws UaException if an error occurs creating or getting the LowLimit Node.
   */
  Double getLowLimit() throws UaException;

  /**
   * Set the local value of the LowLimit Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LowLimit Node.
   * @throws UaException if an error occurs creating or getting the LowLimit Node.
   */
  void setLowLimit(Double value) throws UaException;

  /**
   * Read the value of the LowLimit Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readLowLimit() throws UaException;

  /**
   * Write a new value for the LowLimit Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLowLimit(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLowLimit}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readLowLimitAsync();

  /**
   * An asynchronous implementation of {@link #writeLowLimit}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLowLimitAsync(Double value);

  /**
   * Get the LowLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LowLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLowLimitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLowLimitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLowLimitNodeAsync();

  /**
   * Get the local value of the LowLowLimit Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LowLowLimit Node.
   * @throws UaException if an error occurs creating or getting the LowLowLimit Node.
   */
  Double getLowLowLimit() throws UaException;

  /**
   * Set the local value of the LowLowLimit Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LowLowLimit Node.
   * @throws UaException if an error occurs creating or getting the LowLowLimit Node.
   */
  void setLowLowLimit(Double value) throws UaException;

  /**
   * Read the value of the LowLowLimit Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readLowLowLimit() throws UaException;

  /**
   * Write a new value for the LowLowLimit Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLowLowLimit(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLowLowLimit}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readLowLowLimitAsync();

  /**
   * An asynchronous implementation of {@link #writeLowLowLimit}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLowLowLimitAsync(Double value);

  /**
   * Get the LowLowLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LowLowLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLowLowLimitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLowLowLimitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLowLowLimitNodeAsync();

  /**
   * Get the local value of the BaseHighHighLimit Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the BaseHighHighLimit Node.
   * @throws UaException if an error occurs creating or getting the BaseHighHighLimit Node.
   */
  Double getBaseHighHighLimit() throws UaException;

  /**
   * Set the local value of the BaseHighHighLimit Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the BaseHighHighLimit Node.
   * @throws UaException if an error occurs creating or getting the BaseHighHighLimit Node.
   */
  void setBaseHighHighLimit(Double value) throws UaException;

  /**
   * Read the value of the BaseHighHighLimit Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readBaseHighHighLimit() throws UaException;

  /**
   * Write a new value for the BaseHighHighLimit Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeBaseHighHighLimit(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readBaseHighHighLimit}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readBaseHighHighLimitAsync();

  /**
   * An asynchronous implementation of {@link #writeBaseHighHighLimit}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeBaseHighHighLimitAsync(Double value);

  /**
   * Get the BaseHighHighLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the BaseHighHighLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getBaseHighHighLimitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getBaseHighHighLimitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getBaseHighHighLimitNodeAsync();

  /**
   * Get the local value of the BaseHighLimit Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the BaseHighLimit Node.
   * @throws UaException if an error occurs creating or getting the BaseHighLimit Node.
   */
  Double getBaseHighLimit() throws UaException;

  /**
   * Set the local value of the BaseHighLimit Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the BaseHighLimit Node.
   * @throws UaException if an error occurs creating or getting the BaseHighLimit Node.
   */
  void setBaseHighLimit(Double value) throws UaException;

  /**
   * Read the value of the BaseHighLimit Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readBaseHighLimit() throws UaException;

  /**
   * Write a new value for the BaseHighLimit Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeBaseHighLimit(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readBaseHighLimit}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readBaseHighLimitAsync();

  /**
   * An asynchronous implementation of {@link #writeBaseHighLimit}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeBaseHighLimitAsync(Double value);

  /**
   * Get the BaseHighLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the BaseHighLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getBaseHighLimitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getBaseHighLimitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getBaseHighLimitNodeAsync();

  /**
   * Get the local value of the BaseLowLimit Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the BaseLowLimit Node.
   * @throws UaException if an error occurs creating or getting the BaseLowLimit Node.
   */
  Double getBaseLowLimit() throws UaException;

  /**
   * Set the local value of the BaseLowLimit Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the BaseLowLimit Node.
   * @throws UaException if an error occurs creating or getting the BaseLowLimit Node.
   */
  void setBaseLowLimit(Double value) throws UaException;

  /**
   * Read the value of the BaseLowLimit Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readBaseLowLimit() throws UaException;

  /**
   * Write a new value for the BaseLowLimit Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeBaseLowLimit(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readBaseLowLimit}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readBaseLowLimitAsync();

  /**
   * An asynchronous implementation of {@link #writeBaseLowLimit}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeBaseLowLimitAsync(Double value);

  /**
   * Get the BaseLowLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the BaseLowLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getBaseLowLimitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getBaseLowLimitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getBaseLowLimitNodeAsync();

  /**
   * Get the local value of the BaseLowLowLimit Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the BaseLowLowLimit Node.
   * @throws UaException if an error occurs creating or getting the BaseLowLowLimit Node.
   */
  Double getBaseLowLowLimit() throws UaException;

  /**
   * Set the local value of the BaseLowLowLimit Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the BaseLowLowLimit Node.
   * @throws UaException if an error occurs creating or getting the BaseLowLowLimit Node.
   */
  void setBaseLowLowLimit(Double value) throws UaException;

  /**
   * Read the value of the BaseLowLowLimit Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readBaseLowLowLimit() throws UaException;

  /**
   * Write a new value for the BaseLowLowLimit Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeBaseLowLowLimit(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readBaseLowLowLimit}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readBaseLowLowLimitAsync();

  /**
   * An asynchronous implementation of {@link #writeBaseLowLowLimit}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeBaseLowLowLimitAsync(Double value);

  /**
   * Get the BaseLowLowLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the BaseLowLowLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getBaseLowLowLimitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getBaseLowLowLimitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getBaseLowLowLimitNodeAsync();

  /**
   * Get the local value of the SeverityHighHigh Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SeverityHighHigh Node.
   * @throws UaException if an error occurs creating or getting the SeverityHighHigh Node.
   */
  UShort getSeverityHighHigh() throws UaException;

  /**
   * Set the local value of the SeverityHighHigh Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SeverityHighHigh Node.
   * @throws UaException if an error occurs creating or getting the SeverityHighHigh Node.
   */
  void setSeverityHighHigh(UShort value) throws UaException;

  /**
   * Read the value of the SeverityHighHigh Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readSeverityHighHigh() throws UaException;

  /**
   * Write a new value for the SeverityHighHigh Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSeverityHighHigh(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSeverityHighHigh}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readSeverityHighHighAsync();

  /**
   * An asynchronous implementation of {@link #writeSeverityHighHigh}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSeverityHighHighAsync(UShort value);

  /**
   * Get the SeverityHighHigh {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SeverityHighHigh {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSeverityHighHighNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSeverityHighHighNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSeverityHighHighNodeAsync();

  /**
   * Get the local value of the SeverityHigh Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SeverityHigh Node.
   * @throws UaException if an error occurs creating or getting the SeverityHigh Node.
   */
  UShort getSeverityHigh() throws UaException;

  /**
   * Set the local value of the SeverityHigh Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SeverityHigh Node.
   * @throws UaException if an error occurs creating or getting the SeverityHigh Node.
   */
  void setSeverityHigh(UShort value) throws UaException;

  /**
   * Read the value of the SeverityHigh Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readSeverityHigh() throws UaException;

  /**
   * Write a new value for the SeverityHigh Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSeverityHigh(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSeverityHigh}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readSeverityHighAsync();

  /**
   * An asynchronous implementation of {@link #writeSeverityHigh}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSeverityHighAsync(UShort value);

  /**
   * Get the SeverityHigh {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SeverityHigh {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSeverityHighNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSeverityHighNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSeverityHighNodeAsync();

  /**
   * Get the local value of the SeverityLow Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SeverityLow Node.
   * @throws UaException if an error occurs creating or getting the SeverityLow Node.
   */
  UShort getSeverityLow() throws UaException;

  /**
   * Set the local value of the SeverityLow Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SeverityLow Node.
   * @throws UaException if an error occurs creating or getting the SeverityLow Node.
   */
  void setSeverityLow(UShort value) throws UaException;

  /**
   * Read the value of the SeverityLow Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readSeverityLow() throws UaException;

  /**
   * Write a new value for the SeverityLow Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSeverityLow(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSeverityLow}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readSeverityLowAsync();

  /**
   * An asynchronous implementation of {@link #writeSeverityLow}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSeverityLowAsync(UShort value);

  /**
   * Get the SeverityLow {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SeverityLow {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSeverityLowNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSeverityLowNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSeverityLowNodeAsync();

  /**
   * Get the local value of the SeverityLowLow Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SeverityLowLow Node.
   * @throws UaException if an error occurs creating or getting the SeverityLowLow Node.
   */
  UShort getSeverityLowLow() throws UaException;

  /**
   * Set the local value of the SeverityLowLow Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SeverityLowLow Node.
   * @throws UaException if an error occurs creating or getting the SeverityLowLow Node.
   */
  void setSeverityLowLow(UShort value) throws UaException;

  /**
   * Read the value of the SeverityLowLow Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readSeverityLowLow() throws UaException;

  /**
   * Write a new value for the SeverityLowLow Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSeverityLowLow(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSeverityLowLow}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readSeverityLowLowAsync();

  /**
   * An asynchronous implementation of {@link #writeSeverityLowLow}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSeverityLowLowAsync(UShort value);

  /**
   * Get the SeverityLowLow {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SeverityLowLow {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSeverityLowLowNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSeverityLowLowNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSeverityLowLowNodeAsync();

  /**
   * Get the local value of the HighHighDeadband Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HighHighDeadband Node.
   * @throws UaException if an error occurs creating or getting the HighHighDeadband Node.
   */
  Double getHighHighDeadband() throws UaException;

  /**
   * Set the local value of the HighHighDeadband Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HighHighDeadband Node.
   * @throws UaException if an error occurs creating or getting the HighHighDeadband Node.
   */
  void setHighHighDeadband(Double value) throws UaException;

  /**
   * Read the value of the HighHighDeadband Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readHighHighDeadband() throws UaException;

  /**
   * Write a new value for the HighHighDeadband Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHighHighDeadband(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHighHighDeadband}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readHighHighDeadbandAsync();

  /**
   * An asynchronous implementation of {@link #writeHighHighDeadband}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHighHighDeadbandAsync(Double value);

  /**
   * Get the HighHighDeadband {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HighHighDeadband {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getHighHighDeadbandNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHighHighDeadbandNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getHighHighDeadbandNodeAsync();

  /**
   * Get the local value of the HighDeadband Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HighDeadband Node.
   * @throws UaException if an error occurs creating or getting the HighDeadband Node.
   */
  Double getHighDeadband() throws UaException;

  /**
   * Set the local value of the HighDeadband Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HighDeadband Node.
   * @throws UaException if an error occurs creating or getting the HighDeadband Node.
   */
  void setHighDeadband(Double value) throws UaException;

  /**
   * Read the value of the HighDeadband Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readHighDeadband() throws UaException;

  /**
   * Write a new value for the HighDeadband Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHighDeadband(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHighDeadband}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readHighDeadbandAsync();

  /**
   * An asynchronous implementation of {@link #writeHighDeadband}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHighDeadbandAsync(Double value);

  /**
   * Get the HighDeadband {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HighDeadband {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getHighDeadbandNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHighDeadbandNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getHighDeadbandNodeAsync();

  /**
   * Get the local value of the LowDeadband Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LowDeadband Node.
   * @throws UaException if an error occurs creating or getting the LowDeadband Node.
   */
  Double getLowDeadband() throws UaException;

  /**
   * Set the local value of the LowDeadband Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LowDeadband Node.
   * @throws UaException if an error occurs creating or getting the LowDeadband Node.
   */
  void setLowDeadband(Double value) throws UaException;

  /**
   * Read the value of the LowDeadband Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readLowDeadband() throws UaException;

  /**
   * Write a new value for the LowDeadband Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLowDeadband(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLowDeadband}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readLowDeadbandAsync();

  /**
   * An asynchronous implementation of {@link #writeLowDeadband}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLowDeadbandAsync(Double value);

  /**
   * Get the LowDeadband {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LowDeadband {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLowDeadbandNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLowDeadbandNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLowDeadbandNodeAsync();

  /**
   * Get the local value of the LowLowDeadband Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LowLowDeadband Node.
   * @throws UaException if an error occurs creating or getting the LowLowDeadband Node.
   */
  Double getLowLowDeadband() throws UaException;

  /**
   * Set the local value of the LowLowDeadband Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LowLowDeadband Node.
   * @throws UaException if an error occurs creating or getting the LowLowDeadband Node.
   */
  void setLowLowDeadband(Double value) throws UaException;

  /**
   * Read the value of the LowLowDeadband Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readLowLowDeadband() throws UaException;

  /**
   * Write a new value for the LowLowDeadband Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLowLowDeadband(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLowLowDeadband}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readLowLowDeadbandAsync();

  /**
   * An asynchronous implementation of {@link #writeLowLowDeadband}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLowLowDeadbandAsync(Double value);

  /**
   * Get the LowLowDeadband {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LowLowDeadband {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLowLowDeadbandNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLowLowDeadbandNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLowLowDeadbandNodeAsync();
}
