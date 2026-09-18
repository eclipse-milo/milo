package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SessionsDiagnosticsSummaryType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4">Model
 *     documentation</a>
 */
public interface SessionsDiagnosticsSummaryType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2026L);

  /**
   * Returns the mandatory SessionDiagnosticsArray child, a SessionDiagnosticsArrayType with
   * DataType SessionDiagnosticsDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.13">SessionDiagnosticsArrayType
   *     documentation</a>
   */
  SessionDiagnosticsArrayTypeNode getSessionDiagnosticsArrayNode();

  /**
   * Returns the Value of the SessionDiagnosticsArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SessionDiagnosticsDataType @Nullable [] getSessionDiagnosticsArray();

  /**
   * Sets the Value of the SessionDiagnosticsArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSessionDiagnosticsArray(@Nullable SessionDiagnosticsDataType @Nullable [] value);

  /**
   * Returns the mandatory SessionSecurityDiagnosticsArray child, a
   * SessionSecurityDiagnosticsArrayType with DataType SessionSecurityDiagnosticsDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.15">SessionSecurityDiagnosticsArrayType
   *     documentation</a>
   */
  SessionSecurityDiagnosticsArrayTypeNode getSessionSecurityDiagnosticsArrayNode();

  /**
   * Returns the Value of the SessionSecurityDiagnosticsArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SessionSecurityDiagnosticsDataType @Nullable [] getSessionSecurityDiagnosticsArray();

  /**
   * Sets the Value of the SessionSecurityDiagnosticsArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSessionSecurityDiagnosticsArray(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value);
}
