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
 * removes them with those Objects and gives them this array's access metadata. Access to the array
 * and to the diagnostics enabled flag is derived from the standard nodes' role metadata unless
 * legacy access is explicitly configured.
 */
public class SessionSecurityDiagnosticsVariableArray extends AbstractLifecycle {

  private final AtomicBoolean diagnosticsEnabled = new AtomicBoolean(false);

  private AttributeObserver attributeObserver;
  private AttributeFilter enabledFlagAccessFilter;

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
      // SecurityAdmin. Derive UserAccessLevel from those standard RolePermissions.
      enabledFlagAccessFilter = roleBasedUserAccessLevelFilter();
      diagnosticsNode.getEnabledFlagNode().getFilterChain().addLast(enabledFlagAccessFilter);

      // Apply the array's restricted role policy to Value access and to other node operations.
      node.getFilterChain()
          .addLast(roleBasedUserAccessLevelFilter(), roleBasedUserRolePermissionsFilter());
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
    AttributeObserver observer = attributeObserver;

    if (accessFilter != null || observer != null) {
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

      if (observer != null) {
        diagnosticsNode.getEnabledFlagNode().removeAttributeObserver(observer);
        attributeObserver = null;
      }
    }

    node.delete();
  }
}
