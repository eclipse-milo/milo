/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.variables;

import static org.eclipse.milo.opcua.sdk.server.diagnostics.variables.Util.DEFAULT_ENABLED_FLAG_ROLE_PERMISSIONS;
import static org.eclipse.milo.opcua.sdk.server.diagnostics.variables.Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS;
import static org.eclipse.milo.opcua.sdk.server.diagnostics.variables.Util.diagnosticValueFilter;
import static org.eclipse.milo.opcua.sdk.server.diagnostics.variables.Util.roleBasedUserAccessLevelFilter;
import static org.eclipse.milo.opcua.sdk.server.diagnostics.variables.Util.roleBasedUserRolePermissionsFilter;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionSecurityDiagnosticsAccessMode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.AttributeObserver;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;

/**
 * Publishes the security diagnostics of all active Sessions as the Value of the standard
 * SessionSecurityDiagnosticsArray Variable.
 *
 * <p>The Value follows the server-wide diagnostics enabled flag. The array's element Variables are
 * the SessionSecurityDiagnostics Variables of the per-Session diagnostics Objects; {@link
 * org.eclipse.milo.opcua.sdk.server.diagnostics.objects.SessionsDiagnosticsSummaryObject} adds and
 * removes them with those Objects and gives them this array's access metadata. In restricted access
 * mode, access to the array and to the diagnostics enabled flag follows explicit {@code
 * RolePermissions} configured on those nodes, or the standard default role policies when none are
 * set.
 */
public class SessionSecurityDiagnosticsVariableArray extends AbstractLifecycle {

  private final AtomicBoolean diagnosticsEnabled = new AtomicBoolean(false);

  private AttributeObserver attributeObserver;
  private AttributeFilter enabledFlagAccessFilter;
  private AttributeFilter enabledFlagRolePermissionsFilter;

  private final OpcUaServer server;

  private final SessionSecurityDiagnosticsArrayTypeNode node;

  public SessionSecurityDiagnosticsVariableArray(SessionSecurityDiagnosticsArrayTypeNode node) {
    this.node = node;

    this.server = node.getNodeContext().getServer();
  }

  @Override
  protected void onStartup() {
    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow(
                    () ->
                        new NoSuchElementException("NodeId: " + NodeIds.Server_ServerDiagnostics));

    diagnosticsEnabled.set(diagnosticsNode.getEnabledFlag());

    if (server.getConfig().getSessionSecurityDiagnosticsAccessMode()
        == SessionSecurityDiagnosticsAccessMode.RESTRICTED) {

      // EnabledFlag grants read access broadly but reserves writes for ConfigureAdmin and
      // SecurityAdmin by default, while honoring explicit RolePermissions if configured.
      enabledFlagAccessFilter =
          roleBasedUserAccessLevelFilter(DEFAULT_ENABLED_FLAG_ROLE_PERMISSIONS);
      enabledFlagRolePermissionsFilter =
          roleBasedUserRolePermissionsFilter(DEFAULT_ENABLED_FLAG_ROLE_PERMISSIONS);
      diagnosticsNode
          .getEnabledFlagNode()
          .getFilterChain()
          .addLast(enabledFlagAccessFilter, enabledFlagRolePermissionsFilter);

      // Apply the restricted security diagnostics policy to Value access and internal Browse
      // checks, while exposing UserRolePermissions only when explicit RolePermissions are
      // configured on the node.
      node.getFilterChain()
          .addLast(
              roleBasedUserAccessLevelFilter(DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS),
              roleBasedUserRolePermissionsFilter(
                  DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS));
    }

    attributeObserver =
        (node, attributeId, value) -> {
          if (attributeId == AttributeId.Value) {
            DataValue dataValue = (DataValue) value;
            Object o = dataValue.value().value();
            if (o instanceof Boolean) {
              diagnosticsEnabled.set((Boolean) o);
            }
          }
        };
    diagnosticsNode.getEnabledFlagNode().addAttributeObserver(attributeObserver);

    node.getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject[] xos =
                      ExtensionObject.encodeArray(
                          server.getStaticEncodingContext(),
                          server.getSessionManager().getAllSessions().stream()
                              .map(
                                  s ->
                                      s.getSessionSecurityDiagnostics()
                                          .getSessionSecurityDiagnosticsDataType())
                              .toArray(SessionSecurityDiagnosticsDataType[]::new));
                  return new DataValue(new Variant(xos));
                }));
  }

  @Override
  protected void onShutdown() {
    AttributeFilter accessFilter = enabledFlagAccessFilter;
    AttributeFilter rolePermissionsFilter = enabledFlagRolePermissionsFilter;
    AttributeObserver observer = attributeObserver;

    if (accessFilter != null || rolePermissionsFilter != null || observer != null) {
      ServerDiagnosticsTypeNode diagnosticsNode =
          (ServerDiagnosticsTypeNode)
              server
                  .getAddressSpaceManager()
                  .getManagedNode(NodeIds.Server_ServerDiagnostics)
                  .orElseThrow(
                      () ->
                          new NoSuchElementException(
                              "NodeId: " + NodeIds.Server_ServerDiagnostics));

      if (accessFilter != null) {
        diagnosticsNode.getEnabledFlagNode().getFilterChain().remove(accessFilter);
        enabledFlagAccessFilter = null;
      }

      if (rolePermissionsFilter != null) {
        diagnosticsNode.getEnabledFlagNode().getFilterChain().remove(rolePermissionsFilter);
        enabledFlagRolePermissionsFilter = null;
      }

      if (observer != null) {
        diagnosticsNode.getEnabledFlagNode().removeAttributeObserver(observer);
        attributeObserver = null;
      }
    }

    node.delete();
  }
}
