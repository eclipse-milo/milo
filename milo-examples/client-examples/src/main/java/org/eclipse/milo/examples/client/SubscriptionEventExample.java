/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.EventFilterBuilder;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionEventExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    var example = new SubscriptionEventExample();

    new ClientExampleRunner(example).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    final CountDownLatch eventLatch = new CountDownLatch(3);

    var subscription = new OpcUaSubscription(client);

    // Set a listener for data changes at the subscription level.
    subscription.setSubscriptionListener(
        new OpcUaSubscription.SubscriptionListener() {
          @Override
          public void onEventReceived(
              OpcUaSubscription subscription,
              List<OpcUaMonitoredItem> items,
              List<Variant[]> fields) {

            for (int i = 0; i < items.size(); i++) {
              Variant[] variants = fields.get(i);

              for (int j = 0; j < variants.length; j++) {
                logger.info(
                    "subscription onEventReceived: nodeId={}, field[{}]={}",
                    items.get(i).getReadValueId().getNodeId(),
                    j,
                    variants[j].getValue());
              }
            }

            eventLatch.countDown();
          }
        });

    // Create the subscription on the server.
    subscription.create();

    EventFilter eventFilter =
        new EventFilterBuilder()
            .select(NodeIds.BaseEventType, new QualifiedName(0, "EventId"))
            .select(NodeIds.BaseEventType, new QualifiedName(0, "EventType"))
            .select(NodeIds.BaseEventType, new QualifiedName(0, "Severity"))
            .select(NodeIds.BaseEventType, new QualifiedName(0, "Time"))
            .select(NodeIds.BaseEventType, new QualifiedName(0, "Message"))
            .build();

    var monitoredItem = OpcUaMonitoredItem.newEventItem(NodeIds.Server, eventFilter);

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    // wait for some events to arrive before completing
    eventLatch.await();

    subscription.removeMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();
    subscription.delete();

    future.complete(client);
  }
}
