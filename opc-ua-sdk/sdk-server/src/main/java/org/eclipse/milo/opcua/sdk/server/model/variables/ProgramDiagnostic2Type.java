package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ProgramDiagnostic2Type VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9">Model
 *     documentation</a>
 */
public interface ProgramDiagnostic2Type extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15383L);

  /**
   * Returns the mandatory CreateClientName child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCreateClientNameNode();

  /**
   * Returns the Value of the CreateClientName child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getCreateClientName();

  /**
   * Sets the Value of the CreateClientName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCreateClientName(@Nullable String value);

  /**
   * Returns the mandatory CreateSessionId child, a BaseDataVariableType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCreateSessionIdNode();

  /**
   * Returns the Value of the CreateSessionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getCreateSessionId();

  /**
   * Sets the Value of the CreateSessionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCreateSessionId(@Nullable NodeId value);

  /**
   * Returns the mandatory InvocationCreationTime child, a BaseDataVariableType with DataType
   * UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getInvocationCreationTimeNode();

  /**
   * Returns the Value of the InvocationCreationTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getInvocationCreationTime();

  /**
   * Sets the Value of the InvocationCreationTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInvocationCreationTime(@Nullable DateTime value);

  /**
   * Returns the mandatory LastMethodCall child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getLastMethodCallNode();

  /**
   * Returns the Value of the LastMethodCall child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getLastMethodCall();

  /**
   * Sets the Value of the LastMethodCall child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastMethodCall(@Nullable String value);

  /**
   * Returns the mandatory LastMethodCallTime child, a BaseDataVariableType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getLastMethodCallTimeNode();

  /**
   * Returns the Value of the LastMethodCallTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getLastMethodCallTime();

  /**
   * Sets the Value of the LastMethodCallTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastMethodCallTime(@Nullable DateTime value);

  /**
   * Returns the mandatory LastMethodInputArguments child, a BaseDataVariableType with DataType
   * Argument.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getLastMethodInputArgumentsNode();

  /**
   * Returns the Value of the LastMethodInputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Argument @Nullable [] getLastMethodInputArguments();

  /**
   * Sets the Value of the LastMethodInputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastMethodInputArguments(@Nullable Argument @Nullable [] value);

  /**
   * Returns the mandatory LastMethodInputValues child, a BaseDataVariableType with DataType
   * BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getLastMethodInputValuesNode();

  /**
   * Returns the Value of the LastMethodInputValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant @Nullable [] getLastMethodInputValues();

  /**
   * Sets the Value of the LastMethodInputValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastMethodInputValues(@Nullable Variant @Nullable [] value);

  /**
   * Returns the mandatory LastMethodOutputArguments child, a BaseDataVariableType with DataType
   * Argument.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getLastMethodOutputArgumentsNode();

  /**
   * Returns the Value of the LastMethodOutputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Argument @Nullable [] getLastMethodOutputArguments();

  /**
   * Sets the Value of the LastMethodOutputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastMethodOutputArguments(@Nullable Argument @Nullable [] value);

  /**
   * Returns the mandatory LastMethodOutputValues child, a BaseDataVariableType with DataType
   * BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getLastMethodOutputValuesNode();

  /**
   * Returns the Value of the LastMethodOutputValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant @Nullable [] getLastMethodOutputValues();

  /**
   * Sets the Value of the LastMethodOutputValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastMethodOutputValues(@Nullable Variant @Nullable [] value);

  /**
   * Returns the mandatory LastMethodReturnStatus child, a BaseDataVariableType with DataType
   * StatusCode.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getLastMethodReturnStatusNode();

  /**
   * Returns the Value of the LastMethodReturnStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable StatusCode getLastMethodReturnStatus();

  /**
   * Sets the Value of the LastMethodReturnStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastMethodReturnStatus(@Nullable StatusCode value);

  /**
   * Returns the mandatory LastMethodSessionId child, a BaseDataVariableType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getLastMethodSessionIdNode();

  /**
   * Returns the Value of the LastMethodSessionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getLastMethodSessionId();

  /**
   * Sets the Value of the LastMethodSessionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastMethodSessionId(@Nullable NodeId value);

  /**
   * Returns the mandatory LastTransitionTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastTransitionTimeNode();

  /**
   * Returns the Value of the LastTransitionTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getLastTransitionTime();

  /**
   * Sets the Value of the LastTransitionTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastTransitionTime(@Nullable DateTime value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable ProgramDiagnostic2DataType getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable ProgramDiagnostic2DataType value);
}
