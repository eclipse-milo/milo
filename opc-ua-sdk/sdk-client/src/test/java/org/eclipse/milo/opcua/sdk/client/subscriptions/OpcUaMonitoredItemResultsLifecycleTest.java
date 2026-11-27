/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.SyncState;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Test;

/**
 * Verifies that {@link OpcUaMonitoredItem} service results remain readable after deletion and are
 * cleared when the item is claimed by a Subscription again, completes an in-flight delete after
 * being re-added, or is reset.
 */
class OpcUaMonitoredItemResultsLifecycleTest {

  @Test
  void applyDeleteResultRetainsResultsUntilReclaimedBySubscription() {
    OpcUaSubscription subscription1 = newTestSubscription();
    OpcUaSubscription subscription2 = newTestSubscription();

    var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    assertTrue(item.tryClaim(subscription1, uint(1)));
    populateServerResults(item);

    item.applyDeleteResult(StatusCode.GOOD);

    assertEquals(SyncState.INITIAL, item.getSyncState());
    assertTrue(item.getServerState().isEmpty());
    assertTrue(item.getClientHandle().isEmpty());
    assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());
    assertEquals(StatusCodes.Bad_UserAccessDenied, item.getModifyResult().orElseThrow().value());
    assertEquals(
        StatusCodes.Bad_UserAccessDenied, item.getSetMonitoringModeResult().orElseThrow().value());
    assertEquals(StatusCode.GOOD, item.getDeleteResult().orElseThrow());

    // Claiming the detached item for a new Subscription lifecycle clears all four results.
    assertTrue(item.tryClaim(subscription2, uint(2)));
    assertEquals(uint(2), item.getClientHandle().orElseThrow());
    assertAllResultsEmpty(item);

    // A rejected claim on an already-owned item must not clear active results.
    populateServerResults(item);
    assertFalse(item.tryClaim(subscription1, uint(3)));
    assertEquals(uint(2), item.getClientHandle().orElseThrow());
    assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());
    assertEquals(StatusCodes.Bad_UserAccessDenied, item.getModifyResult().orElseThrow().value());
    assertEquals(
        StatusCodes.Bad_UserAccessDenied, item.getSetMonitoringModeResult().orElseThrow().value());
  }

  @Test
  void applyDeleteResultForReaddedItemPreservesHandleAndClearsAllResults() {
    OpcUaSubscription subscription = newTestSubscription();

    var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    assertTrue(item.tryClaim(subscription, uint(5)));
    populateServerResults(item);

    item.applyDeleteResult(StatusCode.GOOD, uint(5));

    assertEquals(SyncState.INITIAL, item.getSyncState());
    assertTrue(item.getServerState().isEmpty());
    assertEquals(uint(5), item.getClientHandle().orElseThrow());
    assertAllResultsEmpty(item);
  }

  @Test
  void detachIfNotCreatedRetainsCreateResultUntilReclaimed() {
    OpcUaSubscription subscription = newTestSubscription();

    var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    assertTrue(item.tryClaim(subscription, uint(1)));
    item.applyCreateResult(
        new MonitoredItemCreateResult(
            new StatusCode(StatusCodes.Bad_NodeIdUnknown), uint(0), 0.0, uint(0), null));

    assertTrue(item.detachIfNotCreated());
    assertTrue(item.getClientHandle().isEmpty());
    assertEquals(StatusCodes.Bad_NodeIdUnknown, item.getCreateResult().orElseThrow().value());

    assertTrue(item.tryClaim(subscription, uint(2)));
    assertEquals(uint(2), item.getClientHandle().orElseThrow());
    assertAllResultsEmpty(item);
  }

  @Test
  void resetClearsAllFourResults() {
    OpcUaSubscription subscription = newTestSubscription();

    var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    assertTrue(item.tryClaim(subscription, uint(1)));
    populateServerResults(item);
    item.applyDeleteResult(StatusCode.GOOD);

    item.reset();

    assertEquals(SyncState.INITIAL, item.getSyncState());
    assertTrue(item.getServerState().isEmpty());
    assertAllResultsEmpty(item);
  }

  private static void populateServerResults(OpcUaMonitoredItem item) {
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(10), 1000.0, uint(1), null));
    item.applyModifyResult(
        new MonitoredItemModifyResult(
            new StatusCode(StatusCodes.Bad_UserAccessDenied), 0.0, uint(0), null));
    item.applySetMonitoringModeResult(
        new StatusCode(StatusCodes.Bad_UserAccessDenied), MonitoringMode.Sampling);
  }

  private static void assertAllResultsEmpty(OpcUaMonitoredItem item) {
    assertTrue(item.getCreateResult().isEmpty());
    assertTrue(item.getModifyResult().isEmpty());
    assertTrue(item.getDeleteResult().isEmpty());
    assertTrue(item.getSetMonitoringModeResult().isEmpty());
  }

  private static OpcUaSubscription newTestSubscription() {
    var server =
        new ApplicationDescription(
            "urn:eclipse:milo:test:server",
            "urn:eclipse:milo:test:product",
            LocalizedText.english("Test Server"),
            ApplicationType.Server,
            null,
            null,
            null);
    var endpoint =
        new EndpointDescription(
            "opc.tcp://localhost:4840",
            server,
            ByteString.NULL_VALUE,
            MessageSecurityMode.None,
            SecurityPolicy.None.getUri(),
            new UserTokenPolicy[] {
              new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
            },
            Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
            ubyte(0));
    OpcUaClientConfig config =
        OpcUaClientConfig.builder().setEndpoint(endpoint).setDiscoveryEndpoints(List.of()).build();
    OpcClientTransportConfig transportConfig = OpcTcpClientTransportConfig.newBuilder().build();
    OpcClientTransport transport =
        new OpcClientTransport() {
          @Override
          public OpcClientTransportConfig getConfig() {
            return transportConfig;
          }

          @Override
          public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
            return CompletableFuture.completedFuture(Unit.VALUE);
          }

          @Override
          public CompletableFuture<Unit> disconnect() {
            return CompletableFuture.completedFuture(Unit.VALUE);
          }

          @Override
          public CompletableFuture<UaResponseMessageType> sendRequestMessage(
              UaRequestMessageType requestMessage) {
            return CompletableFuture.failedFuture(new UnsupportedOperationException());
          }
        };

    return new OpcUaSubscription(new OpcUaClient(config, transport));
  }
}
