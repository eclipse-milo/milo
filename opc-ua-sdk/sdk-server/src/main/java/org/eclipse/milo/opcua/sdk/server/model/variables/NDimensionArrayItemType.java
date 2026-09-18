package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the NDimensionArrayItemType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.6">Model
 *     documentation</a>
 */
public interface NDimensionArrayItemType extends ArrayItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12068L);

  /**
   * Returns the mandatory AxisDefinition child, a PropertyType with DataType AxisInformation.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAxisDefinitionNode();

  /**
   * Returns the Value of the AxisDefinition child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable AxisInformation @Nullable [] getAxisDefinition();

  /**
   * Sets the Value of the AxisDefinition child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAxisDefinition(@Nullable AxisInformation @Nullable [] value);
}
