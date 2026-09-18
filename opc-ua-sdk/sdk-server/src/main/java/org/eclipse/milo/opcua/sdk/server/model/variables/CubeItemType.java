package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the CubeItemType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.5">Model
 *     documentation</a>
 */
public interface CubeItemType extends ArrayItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12057L);

  /**
   * Returns the mandatory XAxisDefinition child, a PropertyType with DataType AxisInformation.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getXAxisDefinitionNode();

  /**
   * Returns the Value of the XAxisDefinition child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable AxisInformation getXAxisDefinition();

  /**
   * Sets the Value of the XAxisDefinition child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setXAxisDefinition(@Nullable AxisInformation value);

  /**
   * Returns the mandatory YAxisDefinition child, a PropertyType with DataType AxisInformation.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getYAxisDefinitionNode();

  /**
   * Returns the Value of the YAxisDefinition child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable AxisInformation getYAxisDefinition();

  /**
   * Sets the Value of the YAxisDefinition child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setYAxisDefinition(@Nullable AxisInformation value);

  /**
   * Returns the mandatory ZAxisDefinition child, a PropertyType with DataType AxisInformation.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getZAxisDefinitionNode();

  /**
   * Returns the Value of the ZAxisDefinition child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable AxisInformation getZAxisDefinition();

  /**
   * Sets the Value of the ZAxisDefinition child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setZAxisDefinition(@Nullable AxisInformation value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable Variant getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable Variant value);
}
