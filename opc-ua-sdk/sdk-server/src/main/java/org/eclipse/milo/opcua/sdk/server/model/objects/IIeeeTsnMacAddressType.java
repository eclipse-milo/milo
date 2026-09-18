package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the IIeeeTsnMacAddressType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.13">Model
 *     documentation</a>
 */
public interface IIeeeTsnMacAddressType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24199L);

  /**
   * Returns the mandatory DestinationAddress child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDestinationAddressNode();

  /**
   * Returns the Value of the DestinationAddress child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  UByte @Nullable [] getDestinationAddress();

  /**
   * Sets the Value of the DestinationAddress child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDestinationAddress(UByte @Nullable [] value);

  /**
   * Returns the optional SourceAddress child, a BaseDataVariableType with DataType Byte.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getSourceAddressNode();

  /**
   * Returns the Value of the SourceAddress child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  UByte @Nullable [] getSourceAddress();

  /**
   * Sets the Value of the SourceAddress child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSourceAddress(UByte @Nullable [] value);
}
