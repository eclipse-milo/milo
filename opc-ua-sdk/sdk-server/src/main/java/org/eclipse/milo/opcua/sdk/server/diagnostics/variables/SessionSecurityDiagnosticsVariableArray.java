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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionSecurityDiagnosticsAccessMode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.AttributeObserver;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Publishes security diagnostics for all active Sessions and manages the per-Session Variables
 * created beneath the standard diagnostics array.
 *
 * <p>Elements are created and removed as Sessions enter and leave the server. Access to the array,
 * its runtime-created elements, and the diagnostics enabled flag is derived from the standard
 * nodes' role metadata unless legacy access is explicitly configured.
 */
public class SessionSecurityDiagnosticsVariableArray extends AbstractLifecycle {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicBoolean diagnosticsEnabled = new AtomicBoolean(false);

  private final List<SessionSecurityDiagnosticsVariable> sessionSecurityDiagnosticsVariables =
      Collections.synchronizedList(new ArrayList<>());

  private AttributeObserver attributeObserver;
  private SessionListener sessionListener;
  private AttributeFilter enabledFlagAccessFilter;

  private final OpcUaServer server;

  private final SessionSecurityDiagnosticsArrayTypeNode node;
  private final NodeManager<UaNode> diagnosticsNodeManager;

  public SessionSecurityDiagnosticsVariableArray(
      SessionSecurityDiagnosticsArrayTypeNode node, NodeManager<UaNode> diagnosticsNodeManager) {

    this.node = node;
    this.diagnosticsNodeManager = diagnosticsNodeManager;

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

    if (diagnosticsEnabled.get()) {
      addSessionListener();
    }

    attributeObserver =
        (node, attributeId, value) -> {
          if (attributeId == AttributeId.Value) {
            DataValue dataValue = (DataValue) value;
            Object o = dataValue.value().value();
            if (o instanceof Boolean) {
              boolean current = (boolean) o;
              boolean previous = diagnosticsEnabled.getAndSet(current);

              if (!previous && current) {
                server
                    .getSessionManager()
                    .getAllSessions()
                    .forEach(this::createSessionSecurityDiagnosticsVariable);

                if (sessionListener == null) {
                  addSessionListener();
                }
              } else if (previous && !current) {
                if (sessionListener != null) {
                  server.getSessionManager().removeSessionListener(sessionListener);
                  sessionListener = null;
                }

                sessionSecurityDiagnosticsVariables.forEach(Lifecycle::shutdown);
                sessionSecurityDiagnosticsVariables.clear();
              }
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

  private void addSessionListener() {
    server
        .getSessionManager()
        .addSessionListener(
            sessionListener =
                new SessionListener() {
                  @Override
                  public void onSessionCreated(Session session) {
                    createSessionSecurityDiagnosticsVariable(session);
                  }

                  @Override
                  public void onSessionClosed(Session session) {
                    for (int i = 0; i < sessionSecurityDiagnosticsVariables.size(); i++) {
                      SessionSecurityDiagnosticsVariable v =
                          sessionSecurityDiagnosticsVariables.get(i);
                      if (v.getSession().getSessionId().equals(session.getSessionId())) {
                        sessionSecurityDiagnosticsVariables.remove(i);
                        v.shutdown();
                        break;
                      }
                    }
                  }
                });
  }

  private void createSessionSecurityDiagnosticsVariable(Session session) {
    try {
      int index = sessionSecurityDiagnosticsVariables.size();
      String id = Util.buildBrowseNamePath(node) + "[" + index + "]";
      NodeId elementNodeId = new NodeId(1, id);

      InstantiationRequest<SessionSecurityDiagnosticsTypeNode> request =
          InstantiationRequest.of(
                  SessionSecurityDiagnosticsTypeNode.class, NodeIds.SessionSecurityDiagnosticsType)
              .nodeId(elementNodeId)
              .browseName(new QualifiedName(1, "SessionSecurityDiagnostics"))
              .displayName(
                  new LocalizedText(node.getDisplayName().locale(), "SessionSecurityDiagnostics"))
              .rootAttribute(AttributeId.ArrayDimensions, null)
              .rootAttribute(AttributeId.ValueRank, ValueRank.Scalar.getValue())
              .rootAttribute(AttributeId.DataType, NodeIds.SessionSecurityDiagnosticsDataType)
              .rootAttribute(AttributeId.AccessLevel, AccessLevel.toValue(AccessLevel.READ_ONLY))
              .rootAttribute(
                  AttributeId.UserAccessLevel, AccessLevel.toValue(AccessLevel.READ_ONLY))
              .parent(node.getNodeId(), NodeIds.HasComponent)
              .target(diagnosticsNodeManager)
              .legacyPathStrings()
              .onNode(securityAccessControl(node))
              .build();

      SessionSecurityDiagnosticsTypeNode elementNode =
          server.getNodeInstantiator().instantiate(request).root();

      SessionSecurityDiagnosticsVariable sessionSecurityDiagnosticsVariable =
          new SessionSecurityDiagnosticsVariable(elementNode, session);
      sessionSecurityDiagnosticsVariable.startup();

      sessionSecurityDiagnosticsVariables.add(sessionSecurityDiagnosticsVariable);
    } catch (UaException e) {
      logger.warn(
          "Failed to create SessionDiagnosticsVariableTypeNode for session id={}",
          session.getSessionId(),
          e);
    }
  }

  /**
   * Creates a hook that copies the standard array instance's security attributes to dynamically
   * instantiated element Variables and their fields, while the nodes are still staged — before
   * anything is published.
   *
   * <p>The type definition describes the element shape, but the runtime nodes must carry the array
   * instance's authorization and secure-channel requirements.
   *
   * @param arrayNode the standard array node that supplies the security attributes.
   * @return a hook that applies those attributes to instantiated Variables.
   */
  static InstantiationRequest.OnNode securityAccessControl(
      SessionSecurityDiagnosticsArrayTypeNode arrayNode) {

    return (declaration, node, parent, graph) -> {
      if (node instanceof UaVariableNode instance) {
        instance.setRolePermissions(arrayNode.getRolePermissions());
        instance.setUserRolePermissions(arrayNode.getUserRolePermissions());
        instance.setAccessRestrictions(arrayNode.getAccessRestrictions());
      }
    };
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

    if (sessionListener != null) {
      server.getSessionManager().removeSessionListener(sessionListener);
      sessionListener = null;
    }

    sessionSecurityDiagnosticsVariables.forEach(Lifecycle::shutdown);
    sessionSecurityDiagnosticsVariables.clear();

    node.delete();
  }
}
