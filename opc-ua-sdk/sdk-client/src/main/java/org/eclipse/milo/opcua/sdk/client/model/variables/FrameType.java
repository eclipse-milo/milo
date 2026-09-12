/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable Boolean getConstant() throws UaException;

  /** Sets the existing node's local value. */
  void setConstant(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readConstant() throws UaException;

  /** Writes the value remotely. */
  void writeConstant(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readConstantAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConstantAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConstantNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConstantNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getFixedBase() throws UaException;

  /** Sets the existing node's local value. */
  void setFixedBase(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readFixedBase() throws UaException;

  /** Writes the value remotely. */
  void writeFixedBase(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readFixedBaseAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeFixedBaseAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getFixedBaseNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getFixedBaseNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable CartesianCoordinates getCartesianCoordinates() throws UaException;

  /** Sets the existing node's local value. */
  void setCartesianCoordinates(@Nullable CartesianCoordinates value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable CartesianCoordinates readCartesianCoordinates() throws UaException;

  /** Writes the value remotely. */
  void writeCartesianCoordinates(@Nullable CartesianCoordinates value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable CartesianCoordinates> readCartesianCoordinatesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCartesianCoordinatesAsync(
      @Nullable CartesianCoordinates value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  CartesianCoordinatesType getCartesianCoordinatesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends CartesianCoordinatesType> getCartesianCoordinatesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Orientation getOrientation() throws UaException;

  /** Sets the existing node's local value. */
  void setOrientation(@Nullable Orientation value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Orientation readOrientation() throws UaException;

  /** Writes the value remotely. */
  void writeOrientation(@Nullable Orientation value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Orientation> readOrientationAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOrientationAsync(@Nullable Orientation value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  OrientationType getOrientationNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends OrientationType> getOrientationNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getBaseFrame() throws UaException;

  /** Sets the existing node's local value. */
  void setBaseFrame(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readBaseFrame() throws UaException;

  /** Writes the value remotely. */
  void writeBaseFrame(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readBaseFrameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBaseFrameAsync(@Nullable NodeId value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getBaseFrameNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getBaseFrameNodeAsync();
}
