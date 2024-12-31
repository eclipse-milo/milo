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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggeringExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    var example = new TriggeringExample();

    new ClientExampleRunner(example).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    // create a subscription @ 1000ms
    var subscription = new OpcUaSubscription(client);
    subscription.create();

    // subscribe to a static value that reports
    ReadValueId readValueId1 =
        new ReadValueId(
            new NodeId(2, "HelloWorld/ScalarTypes/Float"),
            AttributeId.Value.uid(),
            null,
            QualifiedName.NULL_VALUE);

    var reportingItem = new OpcUaMonitoredItem(readValueId1);
    subscription.addMonitoredItem(reportingItem);

    // subscribe to a dynamic value that only samples
    ReadValueId readValueId2 =
        new ReadValueId(
            NodeIds.Server_ServerStatus_CurrentTime,
            AttributeId.Value.uid(),
            null,
            QualifiedName.NULL_VALUE);

    var samplingItem = new OpcUaMonitoredItem(readValueId2, MonitoringMode.Sampling);
    samplingItem.setDataValueListener(
        (item, value) -> logger.info("sampling item received value: {}", value));
    subscription.addMonitoredItem(samplingItem);

    subscription.synchronizeMonitoredItems();

    client.setTriggering(
        subscription.getSubscriptionId().orElseThrow(),
        reportingItem.getMonitoredItemId().orElseThrow(),
        List.of(samplingItem.getMonitoredItemId().orElseThrow()),
        Collections.emptyList());

    // trigger reporting of both by writing to the static item and changing its value
    client.writeValues(
        List.of(new NodeId(2, "HelloWorld/ScalarTypes/Float")),
        List.of(new DataValue(Variant.ofFloat(1.0f))));

    // let the example run for 5 seconds then terminate
    Thread.sleep(5000);
    subscription.delete();
    future.complete(client);
  }
}
