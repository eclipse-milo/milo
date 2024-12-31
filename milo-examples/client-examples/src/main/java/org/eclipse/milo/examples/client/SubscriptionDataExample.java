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
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.MonitoredItemSynchronizationException;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionDataExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    var example = new SubscriptionDataExample();

    new ClientExampleRunner(example).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    var subscription = new OpcUaSubscription(client);

    // Set a listener for data changes at the subscription level.
    subscription.setSubscriptionListener(
        new OpcUaSubscription.SubscriptionListener() {
          @Override
          public void onDataReceived(
              OpcUaSubscription subscription,
              List<OpcUaMonitoredItem> items,
              List<DataValue> values) {

            for (int i = 0; i < items.size(); i++) {
              logger.info(
                  "subscription onDataReceived: nodeId={}, value={}",
                  items.get(i).getReadValueId().getNodeId(),
                  values.get(i).getValue());
            }
          }
        });

    // Create the subscription on the server.
    subscription.create();

    var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);

    // Set a listener for data changes at the monitored item level.
    monitoredItem.setDataValueListener(
        (item, value) ->
            logger.info(
                "monitoredItem onDataReceived: nodeId={}, value={}",
                item.getReadValueId().getNodeId(),
                value.getValue()));

    // Add the MonitoredItem to the Subscription
    subscription.addMonitoredItem(monitoredItem);

    // Synchronize the MonitoredItems with the server.
    // This will create, modify, and delete items as necessary.
    try {
      subscription.synchronizeMonitoredItems();
    } catch (MonitoredItemSynchronizationException e) {
      e.getCreateResults()
          .forEach(
              result ->
                  logger.warn(
                      "failed to create item: nodeId={}, serviceResult={}, operationResult={}",
                      result.monitoredItem().getReadValueId().getNodeId(),
                      result.serviceResult(),
                      result.operationResult()));
    }

    Thread.sleep(5000);

    subscription.delete();

    future.complete(client);
  }
}
