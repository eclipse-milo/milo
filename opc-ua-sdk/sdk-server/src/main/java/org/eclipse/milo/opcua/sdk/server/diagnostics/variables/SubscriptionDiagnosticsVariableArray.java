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

import com.google.common.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.AttributeObserver;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionCreatedEvent;
import org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionDeletedEvent;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SubscriptionDiagnosticsVariableArray extends AbstractLifecycle {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicBoolean diagnosticsEnabled = new AtomicBoolean(false);

  private AttributeObserver attributeObserver;
  private EventSubscriber eventSubscriber;

  private final List<SubscriptionDiagnosticsVariable> subscriptionDiagnosticsVariables =
      Collections.synchronizedList(new ArrayList<>());

  private final OpcUaServer server;
  private final NodeManager<UaNode> diagnosticsNodeManager;

  private final SubscriptionDiagnosticsArrayTypeNode node;

  public SubscriptionDiagnosticsVariableArray(
      SubscriptionDiagnosticsArrayTypeNode node, NodeManager<UaNode> diagnosticsNodeManager) {

    this.node = node;
    this.diagnosticsNodeManager = diagnosticsNodeManager;

    this.server = node.getNodeContext().getServer();
  }

  protected abstract List<Subscription> getSubscriptions();

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
      server.getInternalEventBus().register(eventSubscriber = new EventSubscriber());
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
                getSubscriptions().forEach(this::createSubscriptionDiagnosticsNode);

                if (eventSubscriber == null) {
                  server.getInternalEventBus().register(eventSubscriber = new EventSubscriber());
                }
              } else if (previous && !current) {
                if (eventSubscriber != null) {
                  server.getInternalEventBus().unregister(eventSubscriber);
                  eventSubscriber = null;
                }

                subscriptionDiagnosticsVariables.forEach(AbstractLifecycle::shutdown);
                subscriptionDiagnosticsVariables.clear();
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
                          getSubscriptions().stream()
                              .map(
                                  s ->
                                      s.getSubscriptionDiagnostics()
                                          .getSubscriptionDiagnosticsDataType())
                              .toArray(SubscriptionDiagnosticsDataType[]::new));
                  return new DataValue(new Variant(xos));
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

    if (eventSubscriber != null) {
      server.getInternalEventBus().unregister(eventSubscriber);
      eventSubscriber = null;
    }

    subscriptionDiagnosticsVariables.forEach(AbstractLifecycle::shutdown);
    subscriptionDiagnosticsVariables.clear();

    node.delete();
  }

  private void createSubscriptionDiagnosticsNode(Subscription subscription) {
    try {
      int index = subscriptionDiagnosticsVariables.size();
      String id = Util.buildBrowseNamePath(node) + "[" + index + "]";
      NodeId elementNodeId = new NodeId(1, id);

      InstantiationRequest<SubscriptionDiagnosticsTypeNode> request =
          InstantiationRequest.of(
                  SubscriptionDiagnosticsTypeNode.class, NodeIds.SubscriptionDiagnosticsType)
              .nodeId(elementNodeId)
              .browseName(new QualifiedName(1, subscription.getId().toString()))
              .displayName(
                  new LocalizedText(
                      node.getDisplayName().locale(), subscription.getId().toString()))
              .rootAttribute(AttributeId.ArrayDimensions, null)
              .rootAttribute(AttributeId.ValueRank, ValueRank.Scalar.getValue())
              .rootAttribute(AttributeId.DataType, NodeIds.SubscriptionDiagnosticsDataType)
              .rootAttribute(AttributeId.AccessLevel, AccessLevel.toValue(AccessLevel.READ_ONLY))
              .rootAttribute(
                  AttributeId.UserAccessLevel, AccessLevel.toValue(AccessLevel.READ_ONLY))
              .parent(node.getNodeId(), NodeIds.HasComponent)
              .target(diagnosticsNodeManager)
              .legacyPathStrings()
              .build();

      SubscriptionDiagnosticsTypeNode elementNode =
          server.getNodeInstantiator().instantiate(request).root();

      SubscriptionDiagnosticsVariable diagnosticsVariable =
          new SubscriptionDiagnosticsVariable(elementNode, subscription);
      diagnosticsVariable.startup();

      subscriptionDiagnosticsVariables.add(diagnosticsVariable);
    } catch (UaException e) {
      logger.error(
          "Failed to create SubscriptionDiagnosticsTypeNode for subscription id={}",
          subscription.getId(),
          e);
    }
  }

  private class EventSubscriber {

    @Subscribe
    public synchronized void onSubscriptionCreated(SubscriptionCreatedEvent event) {
      if (getSubscriptions().stream()
          .anyMatch(s -> s.getId().equals(event.getSubscription().getId()))) {
        createSubscriptionDiagnosticsNode(event.getSubscription());
      }
    }

    @Subscribe
    public synchronized void onSubscriptionDeleted(SubscriptionDeletedEvent event) {
      for (int i = 0; i < subscriptionDiagnosticsVariables.size(); i++) {
        Subscription subscription = subscriptionDiagnosticsVariables.get(i).getSubscription();
        if (event.getSubscription().getId().equals(subscription.getId())) {
          SubscriptionDiagnosticsVariable diagnosticsVariable =
              subscriptionDiagnosticsVariables.remove(i);
          diagnosticsVariable.shutdown();
          break;
        }
      }
    }
  }
}
