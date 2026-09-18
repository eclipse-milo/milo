package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDCartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDFrame;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the 3DFrameType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.28">Model
 *     documentation</a>
 */
public interface ThreeDFrameType extends FrameType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18791L);

  /**
   * Returns the mandatory CartesianCoordinates child, a 3DCartesianCoordinatesType with DataType
   * 3DCartesianCoordinates.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.24">3DCartesianCoordinatesType
   *     documentation</a>
   */
  ThreeDCartesianCoordinatesTypeNode getCartesianCoordinatesNode();

  /**
   * Returns the Value of the CartesianCoordinates child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ThreeDCartesianCoordinates getThreeDFrameTypeCartesianCoordinates();

  /**
   * Sets the Value of the CartesianCoordinates child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setThreeDFrameTypeCartesianCoordinates(@Nullable ThreeDCartesianCoordinates value);

  /**
   * Returns the mandatory Orientation child, a 3DOrientationType with DataType 3DOrientation.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.26">3DOrientationType
   *     documentation</a>
   */
  ThreeDOrientationTypeNode getOrientationNode();

  /**
   * Returns the Value of the Orientation child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ThreeDOrientation getThreeDFrameTypeOrientation();

  /**
   * Sets the Value of the Orientation child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setThreeDFrameTypeOrientation(@Nullable ThreeDOrientation value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable ThreeDFrame getThreeDFrameValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setThreeDFrameValue(@Nullable ThreeDFrame value);
}
