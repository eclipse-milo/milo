/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.CartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.Orientation;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27</a>
 */
public interface FrameType extends BaseDataVariableType {
  QualifiedProperty<Boolean> CONSTANT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Constant",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> FIXED_BASE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "FixedBase",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /**
   * Get the local value of the Constant Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Constant Node.
   * @throws UaException if an error occurs creating or getting the Constant Node.
   */
  Boolean getConstant() throws UaException;

  /**
   * Set the local value of the Constant Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Constant Node.
   * @throws UaException if an error occurs creating or getting the Constant Node.
   */
  void setConstant(Boolean value) throws UaException;

  /**
   * Read the value of the Constant Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readConstant() throws UaException;

  /**
   * Write a new value for the Constant Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConstant(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConstant}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readConstantAsync();

  /**
   * An asynchronous implementation of {@link #writeConstant}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConstantAsync(Boolean value);

  /**
   * Get the Constant {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Constant {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConstantNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConstantNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConstantNodeAsync();

  /**
   * Get the local value of the FixedBase Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the FixedBase Node.
   * @throws UaException if an error occurs creating or getting the FixedBase Node.
   */
  Boolean getFixedBase() throws UaException;

  /**
   * Set the local value of the FixedBase Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the FixedBase Node.
   * @throws UaException if an error occurs creating or getting the FixedBase Node.
   */
  void setFixedBase(Boolean value) throws UaException;

  /**
   * Read the value of the FixedBase Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readFixedBase() throws UaException;

  /**
   * Write a new value for the FixedBase Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeFixedBase(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readFixedBase}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readFixedBaseAsync();

  /**
   * An asynchronous implementation of {@link #writeFixedBase}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeFixedBaseAsync(Boolean value);

  /**
   * Get the FixedBase {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the FixedBase {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getFixedBaseNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getFixedBaseNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getFixedBaseNodeAsync();

  /**
   * Get the local value of the CartesianCoordinates Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CartesianCoordinates Node.
   * @throws UaException if an error occurs creating or getting the CartesianCoordinates Node.
   */
  CartesianCoordinates getCartesianCoordinates() throws UaException;

  /**
   * Set the local value of the CartesianCoordinates Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CartesianCoordinates Node.
   * @throws UaException if an error occurs creating or getting the CartesianCoordinates Node.
   */
  void setCartesianCoordinates(CartesianCoordinates value) throws UaException;

  /**
   * Read the value of the CartesianCoordinates Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link CartesianCoordinates} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  CartesianCoordinates readCartesianCoordinates() throws UaException;

  /**
   * Write a new value for the CartesianCoordinates Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link CartesianCoordinates} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCartesianCoordinates(CartesianCoordinates value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCartesianCoordinates}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends CartesianCoordinates> readCartesianCoordinatesAsync();

  /**
   * An asynchronous implementation of {@link #writeCartesianCoordinates}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCartesianCoordinatesAsync(CartesianCoordinates value);

  /**
   * Get the CartesianCoordinates {@link CartesianCoordinatesType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CartesianCoordinates {@link CartesianCoordinatesType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  CartesianCoordinatesType getCartesianCoordinatesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCartesianCoordinatesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the CartesianCoordinatesType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends CartesianCoordinatesType> getCartesianCoordinatesNodeAsync();

  /**
   * Get the local value of the Orientation Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Orientation Node.
   * @throws UaException if an error occurs creating or getting the Orientation Node.
   */
  Orientation getOrientation() throws UaException;

  /**
   * Set the local value of the Orientation Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Orientation Node.
   * @throws UaException if an error occurs creating or getting the Orientation Node.
   */
  void setOrientation(Orientation value) throws UaException;

  /**
   * Read the value of the Orientation Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Orientation} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Orientation readOrientation() throws UaException;

  /**
   * Write a new value for the Orientation Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Orientation} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOrientation(Orientation value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOrientation}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Orientation> readOrientationAsync();

  /**
   * An asynchronous implementation of {@link #writeOrientation}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOrientationAsync(Orientation value);

  /**
   * Get the Orientation {@link OrientationType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Orientation {@link OrientationType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  OrientationType getOrientationNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOrientationNode()}.
   *
   * @return a CompletableFuture that completes successfully with the OrientationType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends OrientationType> getOrientationNodeAsync();

  /**
   * Get the local value of the BaseFrame Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the BaseFrame Node.
   * @throws UaException if an error occurs creating or getting the BaseFrame Node.
   */
  NodeId getBaseFrame() throws UaException;

  /**
   * Set the local value of the BaseFrame Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the BaseFrame Node.
   * @throws UaException if an error occurs creating or getting the BaseFrame Node.
   */
  void setBaseFrame(NodeId value) throws UaException;

  /**
   * Read the value of the BaseFrame Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readBaseFrame() throws UaException;

  /**
   * Write a new value for the BaseFrame Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeBaseFrame(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readBaseFrame}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readBaseFrameAsync();

  /**
   * An asynchronous implementation of {@link #writeBaseFrame}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeBaseFrameAsync(NodeId value);

  /**
   * Get the BaseFrame {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the BaseFrame {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getBaseFrameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getBaseFrameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getBaseFrameNodeAsync();
}
