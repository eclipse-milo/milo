package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the IBaseEthernetCapabilitiesType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.4">Model
 *     documentation</a>
 */
public interface IBaseEthernetCapabilitiesType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24167L);

  /**
   * Returns the mandatory VlanTagCapable child, a BaseDataVariableType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getVlanTagCapableNode();

  /**
   * Returns the Value of the VlanTagCapable child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getVlanTagCapable();

  /**
   * Sets the Value of the VlanTagCapable child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setVlanTagCapable(@Nullable Boolean value);
}
