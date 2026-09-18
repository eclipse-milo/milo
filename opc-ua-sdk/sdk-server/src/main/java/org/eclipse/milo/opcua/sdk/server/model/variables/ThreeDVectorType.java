package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the 3DVectorType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.22">Model
 *     documentation</a>
 */
public interface ThreeDVectorType extends VectorType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17716L);

  /**
   * Returns the mandatory X child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getXNode();

  /**
   * Returns the Value of the X child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getX();

  /**
   * Sets the Value of the X child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setX(@Nullable Double value);

  /**
   * Returns the mandatory Y child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getYNode();

  /**
   * Returns the Value of the Y child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getY();

  /**
   * Sets the Value of the Y child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setY(@Nullable Double value);

  /**
   * Returns the mandatory Z child, a BaseDataVariableType with DataType Double.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getZNode();

  /**
   * Returns the Value of the Z child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getZ();

  /**
   * Sets the Value of the Z child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setZ(@Nullable Double value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable ThreeDVector getThreeDVectorValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setThreeDVectorValue(@Nullable ThreeDVector value);
}
