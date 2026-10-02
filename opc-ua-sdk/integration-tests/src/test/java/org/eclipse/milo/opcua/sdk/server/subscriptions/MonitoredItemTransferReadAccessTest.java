/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.subscriptions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.junit.jupiter.api.Test;

/**
 * A data MonitoredItem transferred to a Session with less access reports the denial on its next
 * sampling cycle, through the real client and server with {@link TestNamespace}'s default sampling.
 */
public class MonitoredItemTransferReadAccessTest extends AbstractClientServerTest {

  private static final int INITIAL_VALUE = 42;

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    namespace.configure(
        (context, nodeManager) ->
            UaVariableNode.build(
                context,
                b -> {
                  b.setNodeId(newNodeId("EncryptionRequired"));
                  b.setBrowseName(newQualifiedName("EncryptionRequired"));
                  b.setDisplayName(LocalizedText.english("EncryptionRequired"));
                  b.setDataType(NodeIds.Int32);
                  b.setValue(new DataValue(new Variant(INITIAL_VALUE)));
                  b.setAccessLevel(AccessLevel.READ_WRITE);
                  b.setUserAccessLevel(AccessLevel.READ_WRITE);
                  b.setAccessRestrictions(
                      AccessRestrictionType.of(AccessRestrictionType.Field.EncryptionRequired));
                  return b.buildAndAdd();
                }));
  }

  /**
   * Part 4 §5.14.7 lets the same user transfer a Subscription between Sessions, and §5.13.2.1 asks
   * for a denial in the Publish response when access rights change. The Variable requires an
   * encrypted channel, so the same user reading it over an unsecured channel is denied: the
   * transferred item, checked for its new Session on its next cycle, reports
   * Bad_SecurityModeInsufficient instead of values.
   */
  @Test
  void anItemTransferredToASessionWithLessAccessReportsTheDenialOnItsNextCycle() throws Exception {
    var values = new LinkedBlockingQueue<DataValue>();

    OpcUaClient secureClient = createSecureClient();
    OpcUaClient plainClient =
        TestClient.create(
            server, cfg -> cfg.setIdentityProvider(new UsernameProvider("user1", "password")));
    try {
      secureClient.connect();
      plainClient.connect();

      var subscription = new OpcUaSubscription(secureClient);
      subscription.setPublishingInterval(100.0);
      subscription.create();
      UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();

      OpcUaMonitoredItem item = OpcUaMonitoredItem.newDataItem(newNodeId("EncryptionRequired"));
      item.setSamplingInterval(100.0);
      item.setDataValueListener((i, v) -> values.add(v));
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();
      assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());

      DataValue initial = values.poll(5, TimeUnit.SECONDS);
      assertNotNull(initial);
      assertEquals(INITIAL_VALUE, initial.value().value());

      TransferSubscriptionsResponse response =
          plainClient.transferSubscriptions(List.of(subscriptionId), false);
      assertEquals(StatusCode.GOOD, response.getResults()[0].getStatusCode());

      // The plain client has no client-side Subscription to publish for, so the item's queue is
      // observed on the server: the denial is queued there once the next cycle has re-checked the
      // item for its new Session.
      MonitoredDataItem serverItem =
          serverItem(subscriptionId, item.getMonitoredItemId().orElseThrow());

      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
      while (serverItem.getReadAccessResult() != AccessResult.DENIED_SECURITY_MODE
          && System.nanoTime() < deadline) {
        Thread.sleep(20);
      }
      assertEquals(
          AccessResult.DENIED_SECURITY_MODE,
          serverItem.getReadAccessResult(),
          "the item is re-checked for the new Session on its next cycle");

      List<DataValue> queued = drain(serverItem);
      assertTrue(
          queued.stream()
              .anyMatch(v -> v.statusCode().getValue() == StatusCodes.Bad_SecurityModeInsufficient),
          "the denial is queued for the new Session's Publish: " + queued);
    } finally {
      plainClient.disconnect();
      secureClient.disconnect();
    }
  }

  private MonitoredDataItem serverItem(UInteger subscriptionId, UInteger itemId) {
    Subscription subscription = server.getSubscriptions().get(subscriptionId);
    assertNotNull(subscription, "the Subscription survives the transfer");

    BaseMonitoredItem<?> item = subscription.getMonitoredItems().get(itemId);
    assertNotNull(item);

    return (MonitoredDataItem) item;
  }

  private static List<DataValue> drain(MonitoredDataItem item) {
    var notifications = new ArrayList<UaStructuredType>();
    item.getNotifications(notifications, Integer.MAX_VALUE);

    return notifications.stream().map(n -> ((MonitoredItemNotification) n).getValue()).toList();
  }

  private OpcUaClient createSecureClient() throws Exception {
    var trustList = new MemoryTrustListManager();
    trustList.addTrustedCertificate(
        server.getConfig().getEndpoints().iterator().next().getCertificate());

    return OpcUaClient.create(
        server.getConfig().getEndpoints().iterator().next().getEndpointUrl(),
        endpoints ->
            endpoints.stream()
                .filter(
                    e ->
                        SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri())
                            && e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                .findFirst(),
        transportBuilder -> {},
        builder ->
            // The ApplicationUri is taken from the client certificate, as the server checks.
            builder
                .setApplicationName(LocalizedText.english("eclipse milo test client"))
                .setCertificateIdentity(
                    testServer.getClientKeyPair(), testServer.getClientCertificateChain())
                .setCertificateValidator(
                    new DefaultClientCertificateValidator(
                        trustList, new MemoryCertificateQuarantine()))
                .setIdentityProvider(new UsernameProvider("user1", "password"))
                .setRequestTimeout(uint(5_000)));
  }
}
