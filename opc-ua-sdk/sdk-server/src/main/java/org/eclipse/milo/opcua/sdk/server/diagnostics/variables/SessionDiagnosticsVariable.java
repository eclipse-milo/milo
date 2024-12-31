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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.AttributeObserver;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public class SessionDiagnosticsVariable extends AbstractLifecycle {

  private final AtomicBoolean diagnosticsEnabled = new AtomicBoolean(false);

  private AttributeObserver attributeObserver;

  private final OpcUaServer server;

  private final SessionDiagnosticsVariableTypeNode node;
  private final Session session;

  public SessionDiagnosticsVariable(SessionDiagnosticsVariableTypeNode node, Session session) {
    this.node = node;
    this.session = session;

    this.server = node.getNodeContext().getServer();
  }

  public SessionDiagnosticsVariableTypeNode getNode() {
    return node;
  }

  public Session getSession() {
    return session;
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

    attributeObserver =
        (node, attributeId, value) -> {
          if (attributeId == AttributeId.Value) {
            DataValue dataValue = (DataValue) value;
            Object o = dataValue.getValue().getValue();
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
                  ExtensionObject xo =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getSessionDiagnosticsDataType());
                  return new DataValue(new Variant(xo));
                }));

    node.getSessionIdNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  NodeId value = session.getSessionDiagnostics().getSessionId();
                  return new DataValue(new Variant(value));
                }));
    node.getSessionNameNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  String value = session.getSessionDiagnostics().getSessionName();
                  return new DataValue(new Variant(value));
                }));
    node.getClientDescriptionNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getClientDescription());
                  return new DataValue(new Variant(value));
                }));
    node.getServerUriNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  String value = session.getSessionDiagnostics().getServerUri();
                  return new DataValue(new Variant(value));
                }));
    node.getEndpointUrlNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  String value = session.getSessionDiagnostics().getEndpointUrl();
                  return new DataValue(new Variant(value));
                }));
    node.getLocaleIdsNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  String[] value = session.getSessionDiagnostics().getLocaleIds();
                  return new DataValue(new Variant(value));
                }));
    node.getActualSessionTimeoutNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  Double value = session.getSessionDiagnostics().getActualSessionTimeout();
                  return new DataValue(new Variant(value));
                }));
    node.getMaxResponseMessageSizeNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = session.getSessionDiagnostics().getMaxResponseMessageSize();
                  return new DataValue(new Variant(value));
                }));
    node.getClientConnectionTimeNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  DateTime value = session.getSessionDiagnostics().getClientConnectionTime();
                  return new DataValue(new Variant(value));
                }));
    node.getClientLastContactTimeNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  DateTime value = session.getSessionDiagnostics().getClientLastContactTime();
                  return new DataValue(new Variant(value));
                }));
    node.getCurrentSubscriptionsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = session.getSessionDiagnostics().getCurrentSubscriptionsCount();
                  return new DataValue(new Variant(value));
                }));
    node.getCurrentMonitoredItemsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = session.getSessionDiagnostics().getCurrentMonitoredItemsCount();
                  return new DataValue(new Variant(value));
                }));
    node.getCurrentPublishRequestsInQueueNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      session.getSessionDiagnostics().getCurrentPublishRequestsInQueue();
                  return new DataValue(new Variant(value));
                }));
    node.getTotalRequestCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getTotalRequestCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getUnauthorizedRequestCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(
                          session
                              .getSessionDiagnostics()
                              .getUnauthorizedRequestCount()
                              .longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getReadCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getReadCount().getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getHistoryReadCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getHistoryReadCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getWriteCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getWriteCount().getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getHistoryUpdateCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getHistoryUpdateCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getCallCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getCallCount().getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getCreateMonitoredItemsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getCreateMonitoredItemsCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getModifyMonitoredItemsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getModifyMonitoredItemsCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getSetMonitoringModeCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getSetMonitoringModeCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getSetTriggeringCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getSetTriggeringCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getDeleteMonitoredItemsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getDeleteMonitoredItemsCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getCreateSubscriptionCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getCreateSubscriptionCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getModifySubscriptionCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getModifySubscriptionCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getSetPublishingModeCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getSetPublishingModeCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getPublishCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getPublishCount().getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getRepublishCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getRepublishCount().getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getTransferSubscriptionsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getTransferSubscriptionsCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getDeleteSubscriptionsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getDeleteSubscriptionsCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getAddNodesCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getAddNodesCount().getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getAddReferencesCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getAddReferencesCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getDeleteNodesCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getDeleteNodesCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getDeleteReferencesCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getDeleteReferencesCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getBrowseCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getBrowseCount().getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getBrowseNextCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getBrowseNextCount().getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getTranslateBrowsePathsToNodeIdsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getTranslateBrowsePathsToNodeIdsCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getQueryFirstCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getQueryFirstCount().getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getQueryNextCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session.getSessionDiagnostics().getQueryNextCount().getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getRegisterNodesCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getRegisterNodesCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
    node.getUnregisterNodesCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject value =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          session
                              .getSessionDiagnostics()
                              .getUnregisterNodesCount()
                              .getServiceCounter());
                  return new DataValue(new Variant(value));
                }));
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

    node.delete();
  }
}
