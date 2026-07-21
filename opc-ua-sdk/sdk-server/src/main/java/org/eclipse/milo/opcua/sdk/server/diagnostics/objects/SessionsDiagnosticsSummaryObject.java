/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.objects;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.server.diagnostics.variables.SessionDiagnosticsVariableArray;
import org.eclipse.milo.opcua.sdk.server.diagnostics.variables.SessionSecurityDiagnosticsVariableArray;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionDiagnosticsObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionsDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the standard diagnostics arrays and the per-Session diagnostics Objects beneath the
 * server's SessionsDiagnosticsSummary node.
 *
 * <p>Ordinary session diagnostics and security diagnostics share a lifecycle but retain distinct
 * authorization policies. Security metadata is propagated only to each dynamically instantiated
 * security diagnostics subtree.
 */
public class SessionsDiagnosticsSummaryObject extends AbstractLifecycle {

  static final int MAX_BROWSE_NAME_LENGTH = 512;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Map<NodeId, SessionDiagnosticsObject> sessionDiagnosticsObjects =
      new ConcurrentHashMap<>();

  private SessionDiagnosticsVariableArray sessionDiagnosticsVariableArray;
  private SessionSecurityDiagnosticsVariableArray sessionSecurityDiagnosticsVariableArray;

  private SessionListener sessionListener;

  private final OpcUaServer server;
  private final NodeFactory nodeFactory;
  private final NodeManager<UaNode> diagnosticsNodeManager;

  private final SessionsDiagnosticsSummaryTypeNode node;

  public SessionsDiagnosticsSummaryObject(
      SessionsDiagnosticsSummaryTypeNode node, NodeManager<UaNode> diagnosticsNodeManager) {

    this.node = node;
    this.diagnosticsNodeManager = diagnosticsNodeManager;

    this.server = node.getNodeContext().getServer();

    this.nodeFactory =
        new NodeFactory(
            new UaNodeContext() {
              @Override
              public OpcUaServer getServer() {
                return server;
              }

              @Override
              public NodeManager<UaNode> getNodeManager() {
                return diagnosticsNodeManager;
              }
            });
  }

  @Override
  protected void onStartup() {
    sessionDiagnosticsVariableArray =
        new SessionDiagnosticsVariableArray(
            node.getSessionDiagnosticsArrayNode(), diagnosticsNodeManager);
    sessionDiagnosticsVariableArray.startup();

    sessionSecurityDiagnosticsVariableArray =
        new SessionSecurityDiagnosticsVariableArray(
            node.getSessionSecurityDiagnosticsArrayNode(), diagnosticsNodeManager);
    sessionSecurityDiagnosticsVariableArray.startup();

    server.getSessionManager().getAllSessions().forEach(this::createSessionDiagnosticsObject);

    server
        .getSessionManager()
        .addSessionListener(
            sessionListener =
                new SessionListener() {
                  @Override
                  public void onSessionCreated(Session session) {
                    synchronized (SessionsDiagnosticsSummaryObject.this) {
                      createSessionDiagnosticsObject(session);
                    }
                  }

                  @Override
                  public void onSessionClosed(Session session) {
                    synchronized (SessionsDiagnosticsSummaryObject.this) {
                      SessionDiagnosticsObject sdo =
                          sessionDiagnosticsObjects.remove(session.getSessionId());

                      if (sdo != null) {
                        sdo.shutdown();
                      }
                    }
                  }
                });
  }

  private void createSessionDiagnosticsObject(Session session) {
    try {
      String sessionName = session.getSessionName();
      QualifiedName browseName = new QualifiedName(1, sessionNameBrowseName(sessionName));

      SessionDiagnosticsObjectTypeNode sdoNode =
          (SessionDiagnosticsObjectTypeNode)
              nodeFactory.createNode(
                  new NodeId(1, UUID.randomUUID()),
                  NodeIds.SessionDiagnosticsObjectType,
                  securityDiagnosticsAccessControl(node));
      sdoNode.setBrowseName(browseName);
      sdoNode.setDisplayName(LocalizedText.english(sessionName));

      sdoNode.addReference(
          new Reference(
              sdoNode.getNodeId(),
              NodeIds.HasComponent,
              node.getNodeId().expanded(),
              Reference.Direction.INVERSE));
      diagnosticsNodeManager.addNode(sdoNode);

      SessionDiagnosticsObject sdo =
          new SessionDiagnosticsObject(sdoNode, session, diagnosticsNodeManager);
      sdo.startup();

      sessionDiagnosticsObjects.put(session.getSessionId(), sdo);
    } catch (UaException e) {
      LoggerFactory.getLogger(getClass()).warn("Failed to create SessionDiagnosticsObject", e);
    }
  }

  /**
   * Creates a callback that applies the standard security diagnostics array's access policy only to
   * the security diagnostics subtree of a dynamically instantiated Session diagnostics Object.
   *
   * @param summaryNode the standard summary node containing the security diagnostics array.
   * @return a callback that applies security attributes to security diagnostics Variables.
   */
  static NodeFactory.InstantiationCallback securityDiagnosticsAccessControl(
      SessionsDiagnosticsSummaryTypeNode summaryNode) {

    return new NodeFactory.InstantiationCallback() {
      @Override
      public void onVariableAdded(
          @Nullable UaNode parent, UaVariableNode instance, NodeId typeDefinitionId) {

        if (instance instanceof SessionSecurityDiagnosticsTypeNode
            || parent instanceof SessionSecurityDiagnosticsTypeNode) {

          // Ordinary SessionDiagnostics intentionally retain their separate standard permissions.
          SessionSecurityDiagnosticsArrayTypeNode securityArray =
              summaryNode.getSessionSecurityDiagnosticsArrayNode();

          instance.setRolePermissions(securityArray.getRolePermissions());
          instance.setUserRolePermissions(securityArray.getUserRolePermissions());
          instance.setAccessRestrictions(securityArray.getAccessRestrictions());
        }
      }
    };
  }

  static String sessionNameBrowseName(String sessionName) {
    if (sessionName != null && sessionName.length() > MAX_BROWSE_NAME_LENGTH) {
      return sessionName.substring(0, MAX_BROWSE_NAME_LENGTH);
    }

    return sessionName;
  }

  @Override
  protected void onShutdown() {
    logger.debug("SessionsDiagnosticsSummaryObject onShutdown()");

    if (sessionListener != null) {
      server.getSessionManager().removeSessionListener(sessionListener);
    }

    sessionDiagnosticsVariableArray.shutdown();
    sessionSecurityDiagnosticsVariableArray.shutdown();

    sessionDiagnosticsObjects.values().forEach(SessionDiagnosticsObject::shutdown);
    sessionDiagnosticsObjects.clear();

    node.delete();
  }
}
