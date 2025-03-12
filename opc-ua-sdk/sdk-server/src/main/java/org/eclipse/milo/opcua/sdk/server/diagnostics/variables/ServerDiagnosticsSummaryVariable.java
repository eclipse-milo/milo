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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.AttributeObserver;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public class ServerDiagnosticsSummaryVariable extends AbstractLifecycle {

  private final AtomicBoolean diagnosticsEnabled = new AtomicBoolean(false);

  private AttributeObserver attributeObserver;

  private final OpcUaServer server;
  private final ServerDiagnosticsSummaryTypeNode node;

  public ServerDiagnosticsSummaryVariable(ServerDiagnosticsSummaryTypeNode node) {
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
                  ExtensionObject xo =
                      ExtensionObject.encode(
                          server.getStaticEncodingContext(),
                          server.getDiagnosticsSummary().getServerDiagnosticsSummaryDataType());
                  return new DataValue(new Variant(xo));
                }));
    node.getServerViewCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(server.getDiagnosticsSummary().getCurrentViewCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getCurrentSessionCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(server.getDiagnosticsSummary().getCurrentSessionCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getCumulatedSessionCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(server.getDiagnosticsSummary().getCumulatedSessionCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getSecurityRejectedSessionCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(
                          server
                              .getDiagnosticsSummary()
                              .getSecurityRejectedSessionCount()
                              .longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getRejectedSessionCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(server.getDiagnosticsSummary().getRejectedSessionCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getSessionTimeoutCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(server.getDiagnosticsSummary().getSessionTimeoutCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getSessionAbortCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(server.getDiagnosticsSummary().getSessionAbortCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getPublishingIntervalCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(server.getDiagnosticsSummary().getPublishingIntervalCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getCurrentSubscriptionCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(
                          server.getDiagnosticsSummary().getCurrentSubscriptionCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getCumulatedSubscriptionCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(
                          server
                              .getDiagnosticsSummary()
                              .getCumulatedSubscriptionCount()
                              .longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getSecurityRejectedRequestsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = server.getDiagnosticsSummary().getSecurityRejectedRequestCount();
                  return new DataValue(new Variant(value));
                }));
    node.getRejectedRequestsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = server.getDiagnosticsSummary().getRejectedRequestCount();
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
