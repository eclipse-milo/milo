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

import java.util.List;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.diagnostics.variables.SessionDiagnosticsVariable;
import org.eclipse.milo.opcua.sdk.server.diagnostics.variables.SessionSecurityDiagnosticsVariable;
import org.eclipse.milo.opcua.sdk.server.diagnostics.variables.SubscriptionDiagnosticsVariableArray;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionDiagnosticsObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;

public class SessionDiagnosticsObject extends AbstractLifecycle {

  private SessionDiagnosticsVariable sessionDiagnosticsVariable;
  private SessionSecurityDiagnosticsVariable sessionSecurityDiagnosticsVariable;
  private SubscriptionDiagnosticsVariableArray subscriptionDiagnosticsVariableArray;

  private final SessionDiagnosticsObjectTypeNode node;
  private final Session session;
  private final NodeManager<UaNode> diagnosticsNodeManager;

  public SessionDiagnosticsObject(
      SessionDiagnosticsObjectTypeNode node,
      Session session,
      NodeManager<UaNode> diagnosticsNodeManager) {

    this.node = node;
    this.session = session;
    this.diagnosticsNodeManager = diagnosticsNodeManager;
  }

  @Override
  protected void onStartup() {
    sessionDiagnosticsVariable =
        new SessionDiagnosticsVariable(node.getSessionDiagnosticsNode(), session);
    sessionDiagnosticsVariable.startup();

    sessionSecurityDiagnosticsVariable =
        new SessionSecurityDiagnosticsVariable(node.getSessionSecurityDiagnosticsNode(), session);
    sessionSecurityDiagnosticsVariable.startup();

    subscriptionDiagnosticsVariableArray =
        new SubscriptionDiagnosticsVariableArray(
            node.getSubscriptionDiagnosticsArrayNode(), diagnosticsNodeManager) {
          @Override
          protected List<Subscription> getSubscriptions() {
            return session.getSubscriptionManager().getSubscriptions();
          }
        };

    subscriptionDiagnosticsVariableArray.startup();
  }

  @Override
  protected void onShutdown() {
    sessionDiagnosticsVariable.shutdown();
    sessionSecurityDiagnosticsVariable.shutdown();
    subscriptionDiagnosticsVariableArray.shutdown();

    node.delete();
  }
}
