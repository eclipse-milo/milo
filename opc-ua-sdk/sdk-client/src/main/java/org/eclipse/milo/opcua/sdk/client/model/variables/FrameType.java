package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.CartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.Frame;
import org.eclipse.milo.opcua.stack.core.types.structured.Orientation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the FrameType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27">Model
 *     documentation</a>
 */
public interface FrameType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18786L);

  QualifiedProperty<Boolean> Constant_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Constant",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> FixedBase_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "FixedBase",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  /**
   * Resolves the mandatory Orientation child, a OrientationType with DataType Orientation.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.25">OrientationType
   *     documentation</a>
   */
  OrientationType getOrientationNode() throws UaException;

  /** Asynchronous form of {@link #getOrientationNode()}. */
  CompletableFuture<? extends OrientationType> getOrientationNodeAsync();

  /**
   * Reads the Value of the Orientation child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Orientation readOrientation() throws UaException;

  /**
   * Writes the Value of the Orientation child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOrientation(@Nullable Orientation value) throws UaException;

  /** Asynchronous form of {@link #readOrientation()}. */
  CompletableFuture<? extends @Nullable Orientation> readOrientationAsync();

  /** Asynchronous form of {@link #writeOrientation}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOrientationAsync(@Nullable Orientation value);

  /**
   * Resolves the mandatory CartesianCoordinates child, a CartesianCoordinatesType with DataType
   * CartesianCoordinates.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.23">CartesianCoordinatesType
   *     documentation</a>
   */
  CartesianCoordinatesType getCartesianCoordinatesNode() throws UaException;

  /** Asynchronous form of {@link #getCartesianCoordinatesNode()}. */
  CompletableFuture<? extends CartesianCoordinatesType> getCartesianCoordinatesNodeAsync();

  /**
   * Reads the Value of the CartesianCoordinates child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable CartesianCoordinates readCartesianCoordinates() throws UaException;

  /**
   * Writes the Value of the CartesianCoordinates child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCartesianCoordinates(@Nullable CartesianCoordinates value) throws UaException;

  /** Asynchronous form of {@link #readCartesianCoordinates()}. */
  CompletableFuture<? extends @Nullable CartesianCoordinates> readCartesianCoordinatesAsync();

  /**
   * Asynchronous form of {@link #writeCartesianCoordinates}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeCartesianCoordinatesAsync(
      @Nullable CartesianCoordinates value);

  /**
   * Resolves the optional Constant child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConstantNode() throws UaException;

  /** Asynchronous form of {@link #getConstantNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConstantNodeAsync();

  /**
   * Reads the Value of the Constant child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readConstant() throws UaException;

  /**
   * Writes the Value of the Constant child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConstant(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readConstant()}. */
  CompletableFuture<? extends @Nullable Boolean> readConstantAsync();

  /** Asynchronous form of {@link #writeConstant}; completes with the operation status. */
  CompletableFuture<StatusCode> writeConstantAsync(@Nullable Boolean value);

  /**
   * Resolves the optional BaseFrame child, a BaseDataVariableType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getBaseFrameNode() throws UaException;

  /** Asynchronous form of {@link #getBaseFrameNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getBaseFrameNodeAsync();

  /**
   * Reads the Value of the BaseFrame child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readBaseFrame() throws UaException;

  /**
   * Writes the Value of the BaseFrame child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBaseFrame(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readBaseFrame()}. */
  CompletableFuture<? extends @Nullable NodeId> readBaseFrameAsync();

  /** Asynchronous form of {@link #writeBaseFrame}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBaseFrameAsync(@Nullable NodeId value);

  /**
   * Resolves the optional FixedBase child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getFixedBaseNode() throws UaException;

  /** Asynchronous form of {@link #getFixedBaseNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getFixedBaseNodeAsync();

  /**
   * Reads the Value of the FixedBase child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readFixedBase() throws UaException;

  /**
   * Writes the Value of the FixedBase child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeFixedBase(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readFixedBase()}. */
  CompletableFuture<? extends @Nullable Boolean> readFixedBaseAsync();

  /** Asynchronous form of {@link #writeFixedBase}; completes with the operation status. */
  CompletableFuture<StatusCode> writeFixedBaseAsync(@Nullable Boolean value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Frame readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable Frame value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable Frame> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable Frame value);
}
