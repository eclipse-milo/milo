package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the 3DOrientationType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.26">Model
 *     documentation</a>
 */
public interface ThreeDOrientationType extends OrientationType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18781L);

  /**
   * Returns the mandatory A child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getANode();

  /**
   * Returns the Value of the A child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getA();

  /**
   * Sets the Value of the A child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setA(@Nullable Double value);

  /**
   * Returns the mandatory B child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getBNode();

  /**
   * Returns the Value of the B child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getB();

  /**
   * Sets the Value of the B child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setB(@Nullable Double value);

  /**
   * Returns the mandatory C child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCNode();

  /**
   * Returns the Value of the C child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getC();

  /**
   * Sets the Value of the C child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setC(@Nullable Double value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable ThreeDOrientation getThreeDOrientationValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setThreeDOrientationValue(@Nullable ThreeDOrientation value);
}
