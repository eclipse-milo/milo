/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventSubscriptionExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    var example = new EventSubscriptionExample();

    new ClientExampleRunner(example, true).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    // create a subscription and a monitored item
    final var subscription = new OpcUaSubscription(client);
    subscription.create();

    EventFilter eventFilter =
        new EventFilter(
            new SimpleAttributeOperand[] {
              new SimpleAttributeOperand(
                  NodeIds.BaseEventType,
                  new QualifiedName[] {new QualifiedName(0, "EventId")},
                  AttributeId.Value.uid(),
                  null),
              new SimpleAttributeOperand(
                  NodeIds.BaseEventType,
                  new QualifiedName[] {new QualifiedName(0, "EventType")},
                  AttributeId.Value.uid(),
                  null),
              new SimpleAttributeOperand(
                  NodeIds.BaseEventType,
                  new QualifiedName[] {new QualifiedName(0, "Severity")},
                  AttributeId.Value.uid(),
                  null),
              new SimpleAttributeOperand(
                  NodeIds.BaseEventType,
                  new QualifiedName[] {new QualifiedName(0, "Time")},
                  AttributeId.Value.uid(),
                  null),
              new SimpleAttributeOperand(
                  NodeIds.BaseEventType,
                  new QualifiedName[] {new QualifiedName(0, "Message")},
                  AttributeId.Value.uid(),
                  null)
            },
            new ContentFilter(null));

    final AtomicInteger eventCount = new AtomicInteger(0);

    var monitoredItem = OpcUaMonitoredItem.newEventItem(NodeIds.Server, eventFilter);

    monitoredItem.setEventValueListener(
        (item, vs) -> {
          logger.info("Event Received from {}", item.getReadValueId().getNodeId());

          for (int i = 0; i < vs.length; i++) {
            logger.info("\tvariant[{}]: {}", i, vs[i].value());
          }

          if (eventCount.incrementAndGet() == 3) {
            try {
              subscription.delete();
            } catch (UaException e) {
              throw new RuntimeException(e);
            }
            future.complete(client);
          }
        });

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();
  }
}
