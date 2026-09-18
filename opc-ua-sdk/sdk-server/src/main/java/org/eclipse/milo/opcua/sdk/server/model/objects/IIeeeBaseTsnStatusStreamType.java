package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the IIeeeBaseTsnStatusStreamType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9">Model
 *     documentation</a>
 */
public interface IIeeeBaseTsnStatusStreamType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24183L);

  /**
   * Returns the mandatory FailureCode child, a BaseDataVariableType with DataType TsnFailureCode.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getFailureCodeNode();

  /**
   * Returns the Value of the FailureCode child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable TsnFailureCode getFailureCode();

  /**
   * Sets the Value of the FailureCode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setFailureCode(@Nullable TsnFailureCode value);

  /**
   * Returns the mandatory FailureSystemIdentifier child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getFailureSystemIdentifierNode();

  /**
   * Returns the Value of the FailureSystemIdentifier child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant getFailureSystemIdentifier();

  /**
   * Sets the Value of the FailureSystemIdentifier child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setFailureSystemIdentifier(@Nullable Variant value);

  /**
   * Returns the optional ListenerStatus child, a BaseDataVariableType with DataType
   * TsnListenerStatus.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getListenerStatusNode();

  /**
   * Returns the Value of the ListenerStatus child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable TsnListenerStatus getListenerStatus();

  /**
   * Sets the Value of the ListenerStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setListenerStatus(@Nullable TsnListenerStatus value);

  /**
   * Returns the optional TalkerStatus child, a BaseDataVariableType with DataType TsnTalkerStatus.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getTalkerStatusNode();

  /**
   * Returns the Value of the TalkerStatus child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable TsnTalkerStatus getTalkerStatus();

  /**
   * Sets the Value of the TalkerStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTalkerStatus(@Nullable TsnTalkerStatus value);
}
