package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.CartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.Frame;
import org.eclipse.milo.opcua.stack.core.types.structured.Orientation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the FrameType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27">Model
 *     documentation</a>
 */
public interface FrameType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18786L);

  /**
   * Returns the optional BaseFrame child, a BaseDataVariableType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getBaseFrameNode();

  /**
   * Returns the Value of the BaseFrame child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getBaseFrame();

  /**
   * Sets the Value of the BaseFrame child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBaseFrame(@Nullable NodeId value);

  /**
   * Returns the mandatory CartesianCoordinates child, a CartesianCoordinatesType with DataType
   * CartesianCoordinates.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.23">CartesianCoordinatesType
   *     documentation</a>
   */
  CartesianCoordinatesTypeNode getCartesianCoordinatesNode();

  /**
   * Returns the Value of the CartesianCoordinates child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable CartesianCoordinates getCartesianCoordinates();

  /**
   * Sets the Value of the CartesianCoordinates child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCartesianCoordinates(@Nullable CartesianCoordinates value);

  /**
   * Returns the optional Constant child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getConstantNode();

  /**
   * Returns the Value of the Constant child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getConstant();

  /**
   * Sets the Value of the Constant child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConstant(@Nullable Boolean value);

  /**
   * Returns the optional FixedBase child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getFixedBaseNode();

  /**
   * Returns the Value of the FixedBase child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getFixedBase();

  /**
   * Sets the Value of the FixedBase child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setFixedBase(@Nullable Boolean value);

  /**
   * Returns the mandatory Orientation child, a OrientationType with DataType Orientation.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.25">OrientationType
   *     documentation</a>
   */
  OrientationTypeNode getOrientationNode();

  /**
   * Returns the Value of the Orientation child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Orientation getOrientation();

  /**
   * Sets the Value of the Orientation child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOrientation(@Nullable Orientation value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable Frame getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable Frame value);
}
