/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.variables;

import static org.eclipse.milo.opcua.sdk.server.diagnostics.variables.Util.diagnosticValueFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.AttributeObserver;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionDiagnosticsVariableArray extends AbstractLifecycle {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicBoolean diagnosticsEnabled = new AtomicBoolean(false);

  private final List<SessionDiagnosticsVariable> sessionDiagnosticsVariables =
      Collections.synchronizedList(new ArrayList<>());

  private AttributeObserver attributeObserver;
  private SessionListener sessionListener;

  private final OpcUaServer server;
  private final NodeFactory nodeFactory;

  private final SessionDiagnosticsArrayTypeNode node;
  private final NodeManager<UaNode> diagnosticsNodeManager;

  public SessionDiagnosticsVariableArray(
      SessionDiagnosticsArrayTypeNode node, NodeManager<UaNode> diagnosticsNodeManager) {

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
    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow(
                    () ->
                        new NoSuchElementException("NodeId: " + NodeIds.Server_ServerDiagnostics));

    diagnosticsEnabled.set(diagnosticsNode.getEnabledFlag());

    if (diagnosticsEnabled.get()) {
      addSessionListener();
    }

    attributeObserver =
        (node, attributeId, value) -> {
          if (attributeId == AttributeId.Value) {
            DataValue dataValue = (DataValue) value;
            Object o = dataValue.getValue().getValue();
            if (o instanceof Boolean) {
              boolean current = (boolean) o;
              boolean previous = diagnosticsEnabled.getAndSet(current);

              if (!previous && current) {
                server
                    .getSessionManager()
                    .getAllSessions()
                    .forEach(this::createSessionDiagnosticsVariable);

                if (sessionListener == null) {
                  addSessionListener();
                }
              } else if (previous && !current) {
                if (sessionListener != null) {
                  server.getSessionManager().removeSessionListener(sessionListener);
                  sessionListener = null;
                }

                sessionDiagnosticsVariables.forEach(Lifecycle::shutdown);
                sessionDiagnosticsVariables.clear();
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
                          server.getEncodingContext(),
                          server.getSessionManager().getAllSessions().stream()
                              .map(s -> s.getSessionDiagnostics().getSessionDiagnosticsDataType())
                              .toArray(SessionDiagnosticsDataType[]::new));
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
                    createSessionDiagnosticsVariable(session);
                  }

                  @Override
                  public void onSessionClosed(Session session) {
                    for (int i = 0; i < sessionDiagnosticsVariables.size(); i++) {
                      SessionDiagnosticsVariable v = sessionDiagnosticsVariables.get(i);
                      if (v.getSession().getSessionId().equals(session.getSessionId())) {
                        sessionDiagnosticsVariables.remove(i);
                        v.shutdown();
                        break;
                      }
                    }
                  }
                });
  }

  private void createSessionDiagnosticsVariable(Session session) {
    try {
      int index = sessionDiagnosticsVariables.size();
      String id = Util.buildBrowseNamePath(node) + "[" + index + "]";
      NodeId elementNodeId = new NodeId(1, id);

      SessionDiagnosticsVariableTypeNode elementNode =
          (SessionDiagnosticsVariableTypeNode)
              nodeFactory.createNode(elementNodeId, NodeIds.SessionDiagnosticsVariableType);

      elementNode.setBrowseName(new QualifiedName(1, "SessionDiagnostics"));
      elementNode.setDisplayName(
          new LocalizedText(node.getDisplayName().getLocale(), "SessionDiagnostics"));
      elementNode.setArrayDimensions(null);
      elementNode.setValueRank(ValueRank.Scalar.getValue());
      elementNode.setDataType(NodeIds.SessionDiagnosticsDataType);
      elementNode.setAccessLevel(AccessLevel.toValue(AccessLevel.READ_ONLY));
      elementNode.setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_ONLY));

      elementNode.addReference(
          new Reference(
              elementNode.getNodeId(),
              NodeIds.HasComponent,
              node.getNodeId().expanded(),
              Reference.Direction.INVERSE));
      diagnosticsNodeManager.addNode(elementNode);

      SessionDiagnosticsVariable sessionDiagnosticsVariable =
          new SessionDiagnosticsVariable(elementNode, session);
      sessionDiagnosticsVariable.startup();

      sessionDiagnosticsVariables.add(sessionDiagnosticsVariable);
    } catch (UaException e) {
      logger.warn(
          "Failed to create SessionDiagnosticsVariableTypeNode for session id={}",
          session.getSessionId(),
          e);
    }
  }

  @Override
  protected void onShutdown() {
    AttributeObserver observer = attributeObserver;
    if (observer != null) {
      ServerDiagnosticsTypeNode diagnosticsNode =
          (ServerDiagnosticsTypeNode)
              server
                  .getAddressSpaceManager()
                  .getManagedNode(NodeIds.Server_ServerDiagnostics)
                  .orElseThrow(
                      () ->
                          new NoSuchElementException(
                              "NodeId: " + NodeIds.Server_ServerDiagnostics));

      diagnosticsNode.getEnabledFlagNode().removeAttributeObserver(observer);
      attributeObserver = null;
    }

    if (sessionListener != null) {
      server.getSessionManager().removeSessionListener(sessionListener);
      sessionListener = null;
    }

    sessionDiagnosticsVariables.forEach(Lifecycle::shutdown);
    sessionDiagnosticsVariables.clear();

    node.delete();
  }
}
