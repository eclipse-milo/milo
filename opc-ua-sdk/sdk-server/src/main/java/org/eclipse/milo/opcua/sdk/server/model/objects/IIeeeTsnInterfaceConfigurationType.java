package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the IIeeeTsnInterfaceConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.10">Model
 *     documentation</a>
 */
public interface IIeeeTsnInterfaceConfigurationType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24188L);

  /**
   * Returns the optional InterfaceName child, a BaseDataVariableType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getInterfaceNameNode();

  /**
   * Returns the Value of the InterfaceName child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getInterfaceName();

  /**
   * Sets the Value of the InterfaceName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInterfaceName(@Nullable String value);

  /**
   * Returns the mandatory MacAddress child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMacAddressNode();

  /**
   * Returns the Value of the MacAddress child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getMacAddress();

  /**
   * Sets the Value of the MacAddress child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMacAddress(@Nullable String value);
}
