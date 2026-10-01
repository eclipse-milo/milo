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
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SubscriptionDiagnosticsVariableArray extends AbstractLifecycle {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicLong nextElementId = new AtomicLong();

  private final AtomicBoolean diagnosticsEnabled = new AtomicBoolean(false);

  private AttributeObserver attributeObserver;
  private EventSubscriber eventSubscriber;

  private final List<SubscriptionDiagnosticsVariable> subscriptionDiagnosticsVariables =
      Collections.synchronizedList(new ArrayList<>());

  /**
   * The ServerDiagnostics EnabledFlag node. The EnabledFlag observer runs while this node's monitor
   * is held, so every other path that adds or removes an element takes the monitor first and holds
   * it throughout. That keeps one lock order with the observer and serializes element changes with
   * flag transitions, so the catch-up, a creation event, a deletion event, and a flag Write for the
   * same Subscription never interleave.
   */
  private final PropertyTypeNode enabledFlagNode;

  private final OpcUaServer server;
  private final NodeManager<UaNode> diagnosticsNodeManager;

  private final SubscriptionDiagnosticsArrayTypeNode node;

  public SubscriptionDiagnosticsVariableArray(
      SubscriptionDiagnosticsArrayTypeNode node, NodeManager<UaNode> diagnosticsNodeManager) {

    this.node = node;
    this.diagnosticsNodeManager = diagnosticsNodeManager;

    this.server = node.getNodeContext().getServer();
    this.enabledFlagNode = getServerDiagnosticsNode().getEnabledFlagNode();
  }

  protected abstract List<Subscription> getSubscriptions();

  private ServerDiagnosticsTypeNode getServerDiagnosticsNode() {
    return (ServerDiagnosticsTypeNode)
        server
            .getAddressSpaceManager()
            .getManagedNode(NodeIds.Server_ServerDiagnostics)
            .orElseThrow(
                () -> new NoSuchElementException("NodeId: " + NodeIds.Server_ServerDiagnostics));
  }

  @Override
  protected void onStartup() {
    ServerDiagnosticsTypeNode diagnosticsNode = getServerDiagnosticsNode();

    diagnosticsEnabled.set(diagnosticsNode.getEnabledFlag());

    if (diagnosticsEnabled.get()) {
      startTrackingSubscriptions();
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
                startTrackingSubscriptions();
              } else if (previous && !current) {
                if (eventSubscriber != null) {
                  server.getInternalEventBus().unregister(eventSubscriber);
                  eventSubscriber = null;
                }

                removeAllSubscriptionDiagnosticsNodes();
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
      enabledFlagNode.removeAttributeObserver(observer);
      attributeObserver = null;
    }

    if (eventSubscriber != null) {
      server.getInternalEventBus().unregister(eventSubscriber);
      eventSubscriber = null;
    }

    removeAllSubscriptionDiagnosticsNodes();

    node.delete();
  }

  /**
   * Register for Subscription events, then add an element for each Subscription that already exists
   * and has none.
   *
   * <p>Registering first means a Subscription created during the catch-up raises an event instead
   * of being missed. An array started while diagnostics are already enabled, such as one beneath a
   * Session Object created when the EnabledFlag turned on, picks up the Session's existing
   * Subscriptions here.
   */
  private void startTrackingSubscriptions() {
    synchronized (enabledFlagNode) {
      if (eventSubscriber == null) {
        server.getInternalEventBus().register(eventSubscriber = new EventSubscriber());
      }

      getSubscriptions().forEach(this::createSubscriptionDiagnosticsNode);
    }
  }

  private boolean hasSubscriptionDiagnosticsNode(UInteger subscriptionId) {
    synchronized (subscriptionDiagnosticsVariables) {
      return subscriptionDiagnosticsVariables.stream()
          .anyMatch(v -> v.getSubscription().getId().equals(subscriptionId));
    }
  }

  /**
   * Create an element for {@code subscription} unless it already has one. Runs under the
   * EnabledFlag node's monitor, so the catch-up and a creation event for the same Subscription
   * cannot both pass the check, and neither can run while a flag Write is removing or adding
   * elements.
   */
  private void createSubscriptionDiagnosticsNode(Subscription subscription) {
    synchronized (enabledFlagNode) {
      if (hasSubscriptionDiagnosticsNode(subscription.getId())) {
        return;
      }

      try {
        long index = nextElementId.getAndIncrement();
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
                .rootAttribute(
                    AttributeId.AccessLevelEx,
                    AccessLevelExType.of(AccessLevelExType.Field.CurrentRead))
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
  }

  private void removeSubscriptionDiagnosticsNode(UInteger subscriptionId) {
    synchronized (enabledFlagNode) {
      for (int i = 0; i < subscriptionDiagnosticsVariables.size(); i++) {
        Subscription subscription = subscriptionDiagnosticsVariables.get(i).getSubscription();
        if (subscription.getId().equals(subscriptionId)) {
          subscriptionDiagnosticsVariables.remove(i).shutdown();
          break;
        }
      }
    }
  }

  private void removeAllSubscriptionDiagnosticsNodes() {
    synchronized (enabledFlagNode) {
      subscriptionDiagnosticsVariables.forEach(AbstractLifecycle::shutdown);
      subscriptionDiagnosticsVariables.clear();
    }
  }

  private class EventSubscriber {

    @Subscribe
    public void onSubscriptionCreated(SubscriptionCreatedEvent event) {
      synchronized (enabledFlagNode) {
        // Unregistration happens under this monitor too, so an event dispatched just before a
        // disable must not add an element to the now-disabled array.
        if (diagnosticsEnabled.get()
            && getSubscriptions().stream()
                .anyMatch(s -> s.getId().equals(event.getSubscription().getId()))) {
          createSubscriptionDiagnosticsNode(event.getSubscription());
        }
      }
    }

    @Subscribe
    public void onSubscriptionDeleted(SubscriptionDeletedEvent event) {
      removeSubscriptionDiagnosticsNode(event.getSubscription().getId());
    }
  }
}
