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
import org.eclipse.milo.opcua.sdk.server.diagnostics.SubscriptionDiagnostics;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.AttributeObserver;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public class SubscriptionDiagnosticsVariable extends AbstractLifecycle {

  private final AtomicBoolean diagnosticsEnabled = new AtomicBoolean(false);

  private AttributeObserver attributeObserver;

  private final OpcUaServer server;
  private final SubscriptionDiagnosticsTypeNode node;
  private final Subscription subscription;

  public SubscriptionDiagnosticsVariable(
      SubscriptionDiagnosticsTypeNode node, Subscription subscription) {

    this.node = node;
    this.subscription = subscription;

    this.server = node.getNodeContext().getServer();
  }

  public Subscription getSubscription() {
    return subscription;
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

    SubscriptionDiagnostics subscriptionDiagnostics = subscription.getSubscriptionDiagnostics();

    node.getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  ExtensionObject xo =
                      ExtensionObject.encode(
                          server.getEncodingContext(),
                          subscriptionDiagnostics.getSubscriptionDiagnosticsDataType());
                  return new DataValue(new Variant(xo));
                }));
    node.getSessionIdNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  NodeId value = subscriptionDiagnostics.getSessionId();
                  return new DataValue(new Variant(value));
                }));
    node.getSubscriptionIdNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = subscriptionDiagnostics.getSubscriptionId();
                  return new DataValue(new Variant(value));
                }));
    node.getPriorityNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UByte value = subscriptionDiagnostics.getPriority();
                  return new DataValue(new Variant(value));
                }));
    node.getPublishingIntervalNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  Double value = subscriptionDiagnostics.getPublishingInterval();
                  return new DataValue(new Variant(value));
                }));
    node.getMaxKeepAliveCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = subscriptionDiagnostics.getMaxKeepAliveCount();
                  return new DataValue(new Variant(value));
                }));
    node.getMaxLifetimeCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = subscriptionDiagnostics.getMaxLifetimeCount();
                  return new DataValue(new Variant(value));
                }));
    node.getMaxNotificationsPerPublishNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = subscriptionDiagnostics.getMaxNotificationsPerPublish();
                  return new DataValue(new Variant(value));
                }));
    node.getPublishingEnabledNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  Boolean value = subscriptionDiagnostics.isPublishingEnabled();
                  return new DataValue(new Variant(value));
                }));
    node.getModifyCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = uint(subscriptionDiagnostics.getModifyCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getEnableCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = uint(subscriptionDiagnostics.getEnableCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getDisableCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value = uint(subscriptionDiagnostics.getDisableCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getRepublishRequestCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getRepublishRequestCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getRepublishMessageRequestCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getRepublishMessageRequestCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getRepublishMessageCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getRepublishMessageCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getTransferRequestCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getTransferRequestCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getTransferredToAltClientCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getTransferredToAltClientCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getTransferredToSameClientCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getTransferredToSameClientCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getPublishRequestCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getPublishRequestCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getDataChangeNotificationsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getDataChangeNotificationsCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getEventNotificationsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getEventNotificationsCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getNotificationsCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getNotificationsCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getLatePublishRequestCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getLatePublishRequestCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getCurrentKeepAliveCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getCurrentKeepAliveCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getCurrentLifetimeCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getCurrentLifetimeCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getUnacknowledgedMessageCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getUnacknowledgedMessageCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getDiscardedMessageCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getDiscardedMessageCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getMonitoredItemCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getMonitoredItemCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getDisabledMonitoredItemCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getDisabledMonitoredItemCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getMonitoringQueueOverflowCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getMonitoringQueueOverflowCount().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getNextSequenceNumberNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getNextSequenceNumber().longValue());
                  return new DataValue(new Variant(value));
                }));
    node.getEventQueueOverflowCountNode()
        .getFilterChain()
        .addLast(
            diagnosticValueFilter(
                diagnosticsEnabled,
                ctx -> {
                  UInteger value =
                      uint(subscriptionDiagnostics.getEventQueueOverflowCount().longValue());
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
