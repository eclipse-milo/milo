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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.25">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.25</a>
 */
public interface DiscrepancyAlarmType extends AlarmConditionType {
  QualifiedProperty<NodeId> TARGET_VALUE_NODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TargetValueNode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Double> EXPECTED_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExpectedTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> TOLERANCE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Tolerance",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  /**
   * Get the local value of the TargetValueNode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TargetValueNode Node.
   * @throws UaException if an error occurs creating or getting the TargetValueNode Node.
   */
  NodeId getTargetValueNode() throws UaException;

  /**
   * Set the local value of the TargetValueNode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TargetValueNode Node.
   * @throws UaException if an error occurs creating or getting the TargetValueNode Node.
   */
  void setTargetValueNode(NodeId value) throws UaException;

  /**
   * Read the value of the TargetValueNode Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readTargetValueNode() throws UaException;

  /**
   * Write a new value for the TargetValueNode Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTargetValueNode(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTargetValueNode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readTargetValueNodeAsync();

  /**
   * An asynchronous implementation of {@link #writeTargetValueNode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTargetValueNodeAsync(NodeId value);

  /**
   * Get the TargetValueNode {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TargetValueNode {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getTargetValueNodeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTargetValueNodeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getTargetValueNodeNodeAsync();

  /**
   * Get the local value of the ExpectedTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ExpectedTime Node.
   * @throws UaException if an error occurs creating or getting the ExpectedTime Node.
   */
  Double getExpectedTime() throws UaException;

  /**
   * Set the local value of the ExpectedTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ExpectedTime Node.
   * @throws UaException if an error occurs creating or getting the ExpectedTime Node.
   */
  void setExpectedTime(Double value) throws UaException;

  /**
   * Read the value of the ExpectedTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readExpectedTime() throws UaException;

  /**
   * Write a new value for the ExpectedTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeExpectedTime(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readExpectedTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readExpectedTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeExpectedTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeExpectedTimeAsync(Double value);

  /**
   * Get the ExpectedTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ExpectedTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getExpectedTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getExpectedTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getExpectedTimeNodeAsync();

  /**
   * Get the local value of the Tolerance Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Tolerance Node.
   * @throws UaException if an error occurs creating or getting the Tolerance Node.
   */
  Double getTolerance() throws UaException;

  /**
   * Set the local value of the Tolerance Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Tolerance Node.
   * @throws UaException if an error occurs creating or getting the Tolerance Node.
   */
  void setTolerance(Double value) throws UaException;

  /**
   * Read the value of the Tolerance Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readTolerance() throws UaException;

  /**
   * Write a new value for the Tolerance Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTolerance(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTolerance}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readToleranceAsync();

  /**
   * An asynchronous implementation of {@link #writeTolerance}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeToleranceAsync(Double value);

  /**
   * Get the Tolerance {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Tolerance {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getToleranceNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getToleranceNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getToleranceNodeAsync();
}
