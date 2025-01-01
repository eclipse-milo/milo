/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.objects;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.diagnostics.variables.ServerDiagnosticsSummaryVariable;
import org.eclipse.milo.opcua.sdk.server.diagnostics.variables.SubscriptionDiagnosticsVariableArray;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerDiagnosticsObject extends AbstractLifecycle {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private ServerDiagnosticsSummaryVariable serverDiagnosticsSummary;
  private SubscriptionDiagnosticsVariableArray subscriptionDiagnosticsVariableArray;
  private SessionsDiagnosticsSummaryObject sessionsDiagnosticsSummaryObject;

  private final OpcUaServer server;

  private final ServerDiagnosticsTypeNode node;
  private final NodeManager<UaNode> diagnosticsNodeManager;

  public ServerDiagnosticsObject(
      ServerDiagnosticsTypeNode node, NodeManager<UaNode> diagnosticsNodeManager) {
    this.node = node;
    this.diagnosticsNodeManager = diagnosticsNodeManager;

    this.server = node.getNodeContext().getServer();
  }

  @Override
  protected synchronized void onStartup() {
    logger.debug("ServerDiagnosticsNode onStartup()");

    configureServerDiagnosticsSummary();

    configureSubscriptionDiagnosticsArray();

    configureSessionDiagnosticsSummary();

    node.getSamplingIntervalDiagnosticsArrayNode().delete();
  }

  @Override
  protected synchronized void onShutdown() {
    logger.debug("ServerDiagnosticsNode onShutdown()");

    serverDiagnosticsSummary.shutdown();
    subscriptionDiagnosticsVariableArray.shutdown();
    sessionsDiagnosticsSummaryObject.shutdown();

    node.delete();
  }

  private void configureServerDiagnosticsSummary() {
    serverDiagnosticsSummary =
        new ServerDiagnosticsSummaryVariable(node.getServerDiagnosticsSummaryNode());
    serverDiagnosticsSummary.startup();
  }

  private void configureSubscriptionDiagnosticsArray() {
    subscriptionDiagnosticsVariableArray =
        new SubscriptionDiagnosticsVariableArray(
            node.getSubscriptionDiagnosticsArrayNode(), diagnosticsNodeManager) {
          @Override
          protected List<Subscription> getSubscriptions() {
            return new ArrayList<>(server.getSubscriptions().values());
          }
        };

    subscriptionDiagnosticsVariableArray.startup();
  }

  private void configureSessionDiagnosticsSummary() {
    sessionsDiagnosticsSummaryObject =
        new SessionsDiagnosticsSummaryObject(
            node.getSessionsDiagnosticsSummaryNode(), diagnosticsNodeManager);
    sessionsDiagnosticsSummaryObject.startup();
  }
}
