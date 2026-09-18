package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDCartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDFrame;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the 3DFrameType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.28">Model
 *     documentation</a>
 */
public interface ThreeDFrameType extends FrameType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18791L);

  /**
   * Resolves the mandatory Orientation child, a 3DOrientationType with DataType 3DOrientation.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.26">3DOrientationType
   *     documentation</a>
   */
  ThreeDOrientationType getOrientationNode() throws UaException;

  /** Asynchronous form of {@link #getOrientationNode()}. */
  CompletableFuture<? extends ThreeDOrientationType> getOrientationNodeAsync();

  /**
   * Reads the Value of the Orientation child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ThreeDOrientation readThreeDFrameTypeOrientation() throws UaException;

  /**
   * Writes the Value of the Orientation child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeThreeDFrameTypeOrientation(@Nullable ThreeDOrientation value) throws UaException;

  /** Asynchronous form of {@link #readThreeDFrameTypeOrientation()}. */
  CompletableFuture<? extends @Nullable ThreeDOrientation> readThreeDFrameTypeOrientationAsync();

  /**
   * Asynchronous form of {@link #writeThreeDFrameTypeOrientation}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeThreeDFrameTypeOrientationAsync(
      @Nullable ThreeDOrientation value);

  /**
   * Resolves the mandatory CartesianCoordinates child, a 3DCartesianCoordinatesType with DataType
   * 3DCartesianCoordinates.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.24">3DCartesianCoordinatesType
   *     documentation</a>
   */
  ThreeDCartesianCoordinatesType getCartesianCoordinatesNode() throws UaException;

  /** Asynchronous form of {@link #getCartesianCoordinatesNode()}. */
  CompletableFuture<? extends ThreeDCartesianCoordinatesType> getCartesianCoordinatesNodeAsync();

  /**
   * Reads the Value of the CartesianCoordinates child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ThreeDCartesianCoordinates readThreeDFrameTypeCartesianCoordinates() throws UaException;

  /**
   * Writes the Value of the CartesianCoordinates child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeThreeDFrameTypeCartesianCoordinates(@Nullable ThreeDCartesianCoordinates value)
      throws UaException;

  /** Asynchronous form of {@link #readThreeDFrameTypeCartesianCoordinates()}. */
  CompletableFuture<? extends @Nullable ThreeDCartesianCoordinates>
      readThreeDFrameTypeCartesianCoordinatesAsync();

  /**
   * Asynchronous form of {@link #writeThreeDFrameTypeCartesianCoordinates}; completes with the
   * operation status.
   */
  CompletableFuture<StatusCode> writeThreeDFrameTypeCartesianCoordinatesAsync(
      @Nullable ThreeDCartesianCoordinates value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ThreeDFrame readThreeDFrameValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeThreeDFrameValue(@Nullable ThreeDFrame value) throws UaException;

  /** Asynchronous form of {@link #readThreeDFrameValue()}. */
  CompletableFuture<? extends @Nullable ThreeDFrame> readThreeDFrameValueAsync();

  /** Asynchronous form of {@link #writeThreeDFrameValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeThreeDFrameValueAsync(@Nullable ThreeDFrame value);
}
