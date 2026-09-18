package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the IetfBaseNetworkInterfaceType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.1/#5.5.1.2">Model
 *     documentation</a>
 */
public interface IetfBaseNetworkInterfaceType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 25221L);

  /**
   * Returns the mandatory AdminStatus child, a BaseDataVariableType with DataType
   * InterfaceAdminStatus.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getAdminStatusNode();

  /**
   * Returns the Value of the AdminStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable InterfaceAdminStatus getAdminStatus();

  /**
   * Sets the Value of the AdminStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAdminStatus(@Nullable InterfaceAdminStatus value);

  /**
   * Returns the mandatory OperStatus child, a BaseDataVariableType with DataType
   * InterfaceOperStatus.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getOperStatusNode();

  /**
   * Returns the Value of the OperStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable InterfaceOperStatus getOperStatus();

  /**
   * Sets the Value of the OperStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOperStatus(@Nullable InterfaceOperStatus value);

  /**
   * Returns the optional PhysAddress child, a BaseDataVariableType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getPhysAddressNode();

  /**
   * Returns the Value of the PhysAddress child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getPhysAddress();

  /**
   * Sets the Value of the PhysAddress child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPhysAddress(@Nullable String value);

  /**
   * Returns the mandatory Speed child, a AnalogUnitType with DataType UInt64.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.4">AnalogUnitType
   *     documentation</a>
   */
  AnalogUnitTypeNode getSpeedNode();

  /**
   * Returns the Value of the Speed child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ULong getSpeed();

  /**
   * Sets the Value of the Speed child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSpeed(@Nullable ULong value);
}
