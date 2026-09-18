package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the IIeeeTsnVlanTagType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.14">Model
 *     documentation</a>
 */
public interface IIeeeTsnVlanTagType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24202L);

  /**
   * Returns the mandatory PriorityCodePoint child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getPriorityCodePointNode();

  /**
   * Returns the Value of the PriorityCodePoint child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UByte getPriorityCodePoint();

  /**
   * Sets the Value of the PriorityCodePoint child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPriorityCodePoint(@Nullable UByte value);

  /**
   * Returns the mandatory VlanId child, a BaseDataVariableType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getVlanIdNode();

  /**
   * Returns the Value of the VlanId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getVlanId();

  /**
   * Sets the Value of the VlanId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setVlanId(@Nullable UShort value);
}
