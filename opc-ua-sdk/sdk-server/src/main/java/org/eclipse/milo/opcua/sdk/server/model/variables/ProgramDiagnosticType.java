package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnosticDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusResult;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/** Server API for the ProgramDiagnosticType VariableType. */
public interface ProgramDiagnosticType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2380L);

  /**
   * Returns the mandatory CreateClientName child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getCreateClientNameNode();

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
   * Returns the mandatory CreateSessionId child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getCreateSessionIdNode();

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
   * Returns the mandatory InvocationCreationTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getInvocationCreationTimeNode();

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
   * Returns the mandatory LastMethodCall child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastMethodCallNode();

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
   * Returns the mandatory LastMethodCallTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastMethodCallTimeNode();

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
   * Returns the mandatory LastMethodInputArguments child, a PropertyType with DataType
   * BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastMethodInputArgumentsNode();

  /**
   * Returns the Value of the LastMethodInputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant @Nullable [] getLastMethodInputArguments();

  /**
   * Sets the Value of the LastMethodInputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastMethodInputArguments(@Nullable Variant @Nullable [] value);

  /**
   * Returns the mandatory LastMethodOutputArguments child, a PropertyType with DataType
   * BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastMethodOutputArgumentsNode();

  /**
   * Returns the Value of the LastMethodOutputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant @Nullable [] getLastMethodOutputArguments();

  /**
   * Sets the Value of the LastMethodOutputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastMethodOutputArguments(@Nullable Variant @Nullable [] value);

  /**
   * Returns the mandatory LastMethodReturnStatus child, a PropertyType with DataType StatusResult.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastMethodReturnStatusNode();

  /**
   * Returns the Value of the LastMethodReturnStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable StatusResult getLastMethodReturnStatus();

  /**
   * Sets the Value of the LastMethodReturnStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastMethodReturnStatus(@Nullable StatusResult value);

  /**
   * Returns the mandatory LastMethodSessionId child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastMethodSessionIdNode();

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
  @Nullable ProgramDiagnosticDataType getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable ProgramDiagnosticDataType value);
}
