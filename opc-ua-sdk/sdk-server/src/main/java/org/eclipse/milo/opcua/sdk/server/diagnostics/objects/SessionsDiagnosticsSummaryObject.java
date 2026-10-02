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

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.server.diagnostics.variables.SessionDiagnosticsVariableArray;
import org.eclipse.milo.opcua.sdk.server.diagnostics.variables.SessionSecurityDiagnosticsVariableArray;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionDiagnosticsObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionsDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.AttributeObserver;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the standard diagnostics arrays and the per-Session diagnostics Objects beneath the
 * server's SessionsDiagnosticsSummary node.
 *
 * <p>The per-Session Objects exist only while the server's diagnostics EnabledFlag is {@code true}.
 * Writing the flag {@code true} creates an Object for every current Session before the Write
 * returns, and writing it {@code false} removes them all before the Write returns, so a client that
 * browses SessionsDiagnosticsSummary while diagnostics are disabled sees no Session Objects (Part 5
 * §6.3.3).
 *
 * <p>The elements of SessionDiagnosticsArray and SessionSecurityDiagnosticsArray are each Session
 * Object's own SessionDiagnostics and SessionSecurityDiagnostics Variables, referenced from the
 * arrays with HasComponent (Part 5 §7.13 and §7.15). They appear and disappear with the Object.
 *
 * <p>Ordinary session diagnostics and security diagnostics share a lifecycle but retain distinct
 * authorization policies. The security array's access metadata is applied only to each Object's
 * security diagnostics subtree, which is also what the security array exposes.
 */
public class SessionsDiagnosticsSummaryObject extends AbstractLifecycle {

  static final int MAX_BROWSE_NAME_LENGTH = 512;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Guards {@link #sessionDiagnosticsObjects} and {@link #diagnosticsEnabled}.
   *
   * <p>Always acquired after the EnabledFlag node's monitor; see {@link #serialized(Runnable)}.
   */
  private final Object lock = new Object();

  private final Map<NodeId, SessionDiagnosticsObject> sessionDiagnosticsObjects = new HashMap<>();

  private boolean diagnosticsEnabled;

  private SessionDiagnosticsVariableArray sessionDiagnosticsVariableArray;
  private SessionSecurityDiagnosticsVariableArray sessionSecurityDiagnosticsVariableArray;

  private PropertyTypeNode enabledFlagNode;
  private AttributeObserver attributeObserver;
  private SessionListener sessionListener;

  private final OpcUaServer server;
  private final NodeManager<UaNode> diagnosticsNodeManager;

  private final SessionsDiagnosticsSummaryTypeNode node;

  public SessionsDiagnosticsSummaryObject(
      SessionsDiagnosticsSummaryTypeNode node, NodeManager<UaNode> diagnosticsNodeManager) {

    this.node = node;
    this.diagnosticsNodeManager = diagnosticsNodeManager;

    this.server = node.getNodeContext().getServer();
  }

  @Override
  protected void onStartup() {
    sessionDiagnosticsVariableArray =
        new SessionDiagnosticsVariableArray(node.getSessionDiagnosticsArrayNode());
    sessionDiagnosticsVariableArray.startup();

    sessionSecurityDiagnosticsVariableArray =
        new SessionSecurityDiagnosticsVariableArray(node.getSessionSecurityDiagnosticsArrayNode());
    sessionSecurityDiagnosticsVariableArray.startup();

    ServerDiagnosticsTypeNode diagnosticsNode = getServerDiagnosticsNode();
    enabledFlagNode = diagnosticsNode.getEnabledFlagNode();

    attributeObserver =
        (observedNode, attributeId, value) -> {
          if (attributeId == AttributeId.Value) {
            DataValue dataValue = (DataValue) value;
            Object o = dataValue.value().value();
            if (o instanceof Boolean) {
              setDiagnosticsEnabled((boolean) o);
            }
          }
        };

    sessionListener =
        new SessionListener() {
          @Override
          public void onSessionCreated(Session session) {
            serialized(() -> createSessionDiagnosticsObjectIfEnabled(session));
          }

          @Override
          public void onSessionClosed(Session session) {
            serialized(() -> removeSessionDiagnosticsObject(session.getSessionId()));
          }
        };

    serialized(
        () -> {
          diagnosticsEnabled = Boolean.TRUE.equals(diagnosticsNode.getEnabledFlag());
          enabledFlagNode.addAttributeObserver(attributeObserver);
          server.getSessionManager().addSessionListener(sessionListener);

          if (diagnosticsEnabled) {
            createSessionDiagnosticsObjects();
          }
        });
  }

  /**
   * Run {@code action} holding the EnabledFlag node's monitor and then {@link #lock}, in that
   * order.
   *
   * <p>The EnabledFlag observer is invoked while that monitor is held, and starting or stopping a
   * {@link SessionDiagnosticsObject} takes it too. Acquiring it first from every other entry point
   * keeps one lock order, so a flag Write can neither deadlock with a session listener callback nor
   * interleave with one: when the Write returns, every Object it created or removed is final.
   */
  private void serialized(Runnable action) {
    synchronized (enabledFlagNode) {
      synchronized (lock) {
        action.run();
      }
    }
  }

  /** Called by the EnabledFlag observer, which already holds the EnabledFlag node's monitor. */
  private void setDiagnosticsEnabled(boolean enabled) {
    synchronized (lock) {
      boolean previous = diagnosticsEnabled;
      diagnosticsEnabled = enabled;

      if (!previous && enabled) {
        createSessionDiagnosticsObjects();
      } else if (previous && !enabled) {
        removeSessionDiagnosticsObjects();
      }
    }
  }

  private void createSessionDiagnosticsObjects() {
    server
        .getSessionManager()
        .getAllSessions()
        .forEach(this::createSessionDiagnosticsObjectIfEnabled);
  }

  private void removeSessionDiagnosticsObjects() {
    sessionDiagnosticsObjects.values().forEach(SessionDiagnosticsObject::shutdown);
    sessionDiagnosticsObjects.clear();
  }

  /**
   * Create an Object for {@code session} if diagnostics are enabled and it has none. A created
   * callback can run after the flag turned off, or after the enable pass already covered its
   * Session, so both conditions are checked here.
   */
  private void createSessionDiagnosticsObjectIfEnabled(Session session) {
    NodeId sessionId = session.getSessionId();

    if (!diagnosticsEnabled || sessionDiagnosticsObjects.containsKey(sessionId)) {
      return;
    }

    try {
      String sessionName = session.getSessionName();
      QualifiedName browseName = new QualifiedName(1, sessionNameBrowseName(sessionName));

      InstantiationRequest<SessionDiagnosticsObjectTypeNode> request =
          InstantiationRequest.of(
                  SessionDiagnosticsObjectTypeNode.class, NodeIds.SessionDiagnosticsObjectType)
              .nodeId(new NodeId(1, UUID.randomUUID()))
              .browseName(browseName)
              .displayName(LocalizedText.english(sessionName))
              .parent(node.getNodeId(), NodeIds.HasComponent)
              .target(diagnosticsNodeManager)
              .legacyPathStrings()
              .onNode(securityDiagnosticsAccessControl(node))
              .build();

      SessionDiagnosticsObjectTypeNode sdoNode =
          server.getNodeInstantiator().instantiate(request).root();

      SessionDiagnosticsObject sdo =
          new SessionDiagnosticsObject(sdoNode, session, diagnosticsNodeManager);
      sdo.startup();

      addArrayElement(node.getSessionDiagnosticsArrayNode(), sdoNode.getSessionDiagnosticsNode());
      addArrayElement(
          node.getSessionSecurityDiagnosticsArrayNode(),
          sdoNode.getSessionSecurityDiagnosticsNode());

      sessionDiagnosticsObjects.put(sessionId, sdo);
    } catch (UaException e) {
      logger.warn("Failed to create SessionDiagnosticsObject", e);
    }
  }

  /**
   * Make {@code element}, a Variable of a Session Object, an element of {@code array}.
   *
   * <p>The HasComponent Reference and its inverse are stored in the diagnostics NodeManager that
   * holds {@code element}, so deleting {@code element} when its Session Object is removed also
   * removes it from {@code array}.
   */
  private void addArrayElement(UaVariableNode array, UaVariableNode element) {
    diagnosticsNodeManager.addReferences(
        new Reference(
            array.getNodeId(), NodeIds.HasComponent, element.getNodeId().expanded(), true),
        server.getNamespaceTable());
  }

  private void removeSessionDiagnosticsObject(NodeId sessionId) {
    SessionDiagnosticsObject sdo = sessionDiagnosticsObjects.remove(sessionId);

    if (sdo != null) {
      sdo.shutdown();
    }
  }

  private ServerDiagnosticsTypeNode getServerDiagnosticsNode() {
    return (ServerDiagnosticsTypeNode)
        server
            .getAddressSpaceManager()
            .getManagedNode(NodeIds.Server_ServerDiagnostics)
            .orElseThrow(
                () -> new NoSuchElementException("NodeId: " + NodeIds.Server_ServerDiagnostics));
  }

  /**
   * Creates a hook that applies the standard security diagnostics array's access policy only to the
   * security diagnostics subtree of a dynamically instantiated Session diagnostics Object, while
   * the nodes are still staged — before anything is published.
   *
   * @param summaryNode the standard summary node containing the security diagnostics array.
   * @return a hook that applies security attributes to security diagnostics Variables.
   */
  static InstantiationRequest.OnNode securityDiagnosticsAccessControl(
      SessionsDiagnosticsSummaryTypeNode summaryNode) {

    return (declaration, node, parent, graph) -> {
      if (node instanceof UaVariableNode instance
          && (instance instanceof SessionSecurityDiagnosticsTypeNode
              || parent instanceof SessionSecurityDiagnosticsTypeNode)) {

        // Ordinary SessionDiagnostics intentionally retain their separate standard permissions.
        SessionSecurityDiagnosticsArrayTypeNode securityArray =
            summaryNode.getSessionSecurityDiagnosticsArrayNode();

        instance.setRolePermissions(securityArray.getRolePermissions());
        instance.setUserRolePermissions(securityArray.getUserRolePermissions());
        instance.setAccessRestrictions(securityArray.getAccessRestrictions());
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

    if (enabledFlagNode != null) {
      serialized(
          () -> {
            if (attributeObserver != null) {
              enabledFlagNode.removeAttributeObserver(attributeObserver);
              attributeObserver = null;
            }

            if (sessionListener != null) {
              server.getSessionManager().removeSessionListener(sessionListener);
              sessionListener = null;
            }

            removeSessionDiagnosticsObjects();
          });
    }

    sessionDiagnosticsVariableArray.shutdown();
    sessionSecurityDiagnosticsVariableArray.shutdown();

    node.delete();
  }
}
