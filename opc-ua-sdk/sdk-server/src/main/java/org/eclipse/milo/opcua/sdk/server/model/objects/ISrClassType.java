package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ISrClassType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.6">Model
 *     documentation</a>
 */
public interface ISrClassType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24169L);

  /**
   * Returns the mandatory Id child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getIdNode();

  /**
   * Returns the Value of the Id child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UByte getId();

  /**
   * Sets the Value of the Id child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setId(@Nullable UByte value);

  /**
   * Returns the mandatory Priority child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getPriorityNode();

  /**
   * Returns the Value of the Priority child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UByte getPriority();

  /**
   * Sets the Value of the Priority child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPriority(@Nullable UByte value);

  /**
   * Returns the mandatory Vid child, a BaseDataVariableType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getVidNode();

  /**
   * Returns the Value of the Vid child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getVid();

  /**
   * Sets the Value of the Vid child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setVid(@Nullable UShort value);
}
