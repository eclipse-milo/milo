package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SessionDiagnosticsObjectType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5">Model
 *     documentation</a>
 */
public interface SessionDiagnosticsObjectType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2029L);

  /**
   * Returns the optional CurrentRoleIds child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getCurrentRoleIdsNode();

  /**
   * Returns the Value of the CurrentRoleIds child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getCurrentRoleIds();

  /**
   * Sets the Value of the CurrentRoleIds child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentRoleIds(NodeId @Nullable [] value);

  /**
   * Returns the mandatory SessionDiagnostics child, a SessionDiagnosticsVariableType with DataType
   * SessionDiagnosticsDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14">SessionDiagnosticsVariableType
   *     documentation</a>
   */
  SessionDiagnosticsVariableTypeNode getSessionDiagnosticsNode();

  /**
   * Returns the Value of the SessionDiagnostics child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SessionDiagnosticsDataType getSessionDiagnostics();

  /**
   * Sets the Value of the SessionDiagnostics child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSessionDiagnostics(@Nullable SessionDiagnosticsDataType value);

  /**
   * Returns the mandatory SessionSecurityDiagnostics child, a SessionSecurityDiagnosticsType with
   * DataType SessionSecurityDiagnosticsDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.16">SessionSecurityDiagnosticsType
   *     documentation</a>
   */
  SessionSecurityDiagnosticsTypeNode getSessionSecurityDiagnosticsNode();

  /**
   * Returns the Value of the SessionSecurityDiagnostics child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics();

  /**
   * Sets the Value of the SessionSecurityDiagnostics child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value);

  /**
   * Returns the mandatory SubscriptionDiagnosticsArray child, a SubscriptionDiagnosticsArrayType
   * with DataType SubscriptionDiagnosticsDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11">SubscriptionDiagnosticsArrayType
   *     documentation</a>
   */
  SubscriptionDiagnosticsArrayTypeNode getSubscriptionDiagnosticsArrayNode();

  /**
   * Returns the Value of the SubscriptionDiagnosticsArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SubscriptionDiagnosticsDataType @Nullable [] getSubscriptionDiagnosticsArray();

  /**
   * Sets the Value of the SubscriptionDiagnosticsArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value);
}
