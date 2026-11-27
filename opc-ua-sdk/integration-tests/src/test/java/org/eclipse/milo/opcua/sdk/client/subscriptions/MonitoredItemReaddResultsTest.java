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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.SyncState;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingMonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * Integration tests verifying that {@link OpcUaMonitoredItem} service results remain readable after
 * deletion and are cleared when the item is added to a Subscription again, including across
 * different Subscriptions, in-flight deletions, and failed creations.
 */
class MonitoredItemReaddResultsTest {

  @Test
  void deleteAndReaddClearsStaleResultsAndAllowsFilteredRecreate() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();
      assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());

      // Populate failed modify and SetMonitoringMode results on the first server-side item.
      fixture.serviceSet.refuseModifyAndMode.set(true);
      item.setSamplingInterval(250.0);
      List<MonitoredItemServiceOperationResult> modifyResults = subscription.modifyMonitoredItems();
      assertEquals(
          StatusCodes.Bad_UserAccessDenied,
          modifyResults.get(0).operationResult().orElseThrow().value());

      List<MonitoredItemServiceOperationResult> modeResults =
          subscription.setMonitoringMode(MonitoringMode.Sampling, List.of(item));
      assertEquals(
          StatusCodes.Bad_UserAccessDenied,
          modeResults.get(0).operationResult().orElseThrow().value());

      // Remove and delete: all four results remain readable while the item is detached.
      subscription.removeMonitoredItem(item);
      List<MonitoredItemServiceOperationResult> deleteResults = subscription.deleteMonitoredItems();
      assertTrue(deleteResults.get(0).isGood());

      assertEquals(SyncState.INITIAL, item.getSyncState());
      assertTrue(item.getClientHandle().isEmpty());
      assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());
      assertEquals(StatusCodes.Bad_UserAccessDenied, item.getModifyResult().orElseThrow().value());
      assertEquals(
          StatusCodes.Bad_UserAccessDenied,
          item.getSetMonitoringModeResult().orElseThrow().value());
      assertEquals(StatusCode.GOOD, item.getDeleteResult().orElseThrow());

      // Re-adding to the same Subscription clears all four stale results.
      subscription.addMonitoredItem(item);
      assertTrue(item.getClientHandle().isPresent());
      assertAllResultsEmpty(item);

      // Filtered create selects the re-added item and leaves only the new createResult set.
      List<MonitoredItemServiceOperationResult> recreateResults =
          subscription.createMonitoredItems(i -> i.getCreateResult().isEmpty());
      assertEquals(1, recreateResults.size());
      assertTrue(recreateResults.get(0).isGood());
      assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
      assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());
      assertTrue(item.getModifyResult().isEmpty());
      assertTrue(item.getDeleteResult().isEmpty());
      assertTrue(item.getSetMonitoringModeResult().isEmpty());

      subscription.delete();
    }
  }

  @Test
  void deleteAndReaddToDifferentSubscriptionClearsStaleResults() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription1 = new OpcUaSubscription(fixture.client);
      var subscription2 = new OpcUaSubscription(fixture.client);
      subscription1.create();
      subscription2.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription1.addMonitoredItem(item);
      subscription1.synchronizeMonitoredItems();

      fixture.serviceSet.refuseModifyAndMode.set(true);
      item.setSamplingInterval(250.0);
      subscription1.modifyMonitoredItems();
      subscription1.setMonitoringMode(MonitoringMode.Sampling, List.of(item));

      subscription1.removeMonitoredItem(item);
      assertTrue(subscription1.deleteMonitoredItems().get(0).isGood());
      assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());
      assertEquals(StatusCodes.Bad_UserAccessDenied, item.getModifyResult().orElseThrow().value());
      assertEquals(
          StatusCodes.Bad_UserAccessDenied,
          item.getSetMonitoringModeResult().orElseThrow().value());
      assertEquals(StatusCode.GOOD, item.getDeleteResult().orElseThrow());

      // Adding the deleted item to a different Subscription clears all four results.
      subscription2.addMonitoredItem(item);
      assertAllResultsEmpty(item);

      List<MonitoredItemServiceOperationResult> recreateResults =
          subscription2.createMonitoredItems(i -> i.getCreateResult().isEmpty());
      assertEquals(1, recreateResults.size());
      assertTrue(recreateResults.get(0).isGood());
      assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
      assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());
      assertTrue(item.getModifyResult().isEmpty());
      assertTrue(item.getDeleteResult().isEmpty());
      assertTrue(item.getSetMonitoringModeResult().isEmpty());

      subscription1.delete();
      subscription2.delete();
    }
  }

  @Test
  void removeAndReaddBeforeDeletePreservesResultsOnLiveItem() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();
      UInteger originalId = item.getMonitoredItemId().orElseThrow();

      fixture.serviceSet.refuseModifyAndMode.set(true);
      item.setSamplingInterval(250.0);
      subscription.modifyMonitoredItems();
      subscription.setMonitoringMode(MonitoringMode.Sampling, List.of(item));

      // Remove locally and add back before deleteMonitoredItems(): server item stays alive.
      subscription.removeMonitoredItem(item);
      subscription.addMonitoredItem(item);

      assertTrue(subscription.deleteMonitoredItems().isEmpty());
      assertEquals(originalId, item.getMonitoredItemId().orElseThrow());
      assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());
      assertEquals(StatusCodes.Bad_UserAccessDenied, item.getModifyResult().orElseThrow().value());
      assertEquals(
          StatusCodes.Bad_UserAccessDenied,
          item.getSetMonitoringModeResult().orElseThrow().value());
      assertTrue(item.getDeleteResult().isEmpty());

      subscription.delete();
    }
  }

  @Test
  void readdWhileDeleteInFlightClearsResultsWhenDeleteCompletes() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();

      fixture.serviceSet.refuseModifyAndMode.set(true);
      item.setSamplingInterval(250.0);
      subscription.modifyMonitoredItems();
      subscription.setMonitoringMode(MonitoringMode.Sampling, List.of(item));

      subscription.removeMonitoredItem(item);
      fixture.serviceSet.gateNextDelete();

      CompletableFuture<List<MonitoredItemServiceOperationResult>> inFlightDelete =
          CompletableFuture.supplyAsync(subscription::deleteMonitoredItems);
      try {
        fixture.serviceSet.awaitDeleteArrived();
        subscription.addMonitoredItem(item);
      } finally {
        fixture.serviceSet.releaseDelete();
      }

      List<MonitoredItemServiceOperationResult> deleteResults =
          inFlightDelete.get(10, TimeUnit.SECONDS);
      assertEquals(1, deleteResults.size());
      assertTrue(deleteResults.get(0).isGood());

      assertEquals(SyncState.INITIAL, item.getSyncState());
      assertTrue(item.getServerState().isEmpty());
      assertTrue(item.getClientHandle().isPresent());
      assertAllResultsEmpty(item);

      List<MonitoredItemServiceOperationResult> recreateResults =
          subscription.createMonitoredItems(i -> i.getCreateResult().isEmpty());
      assertEquals(1, recreateResults.size());
      assertTrue(recreateResults.get(0).isGood());
      assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
      assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());
      assertTrue(item.getModifyResult().isEmpty());
      assertTrue(item.getDeleteResult().isEmpty());
      assertTrue(item.getSetMonitoringModeResult().isEmpty());

      subscription.delete();
    }
  }

  @Test
  void failedCreateRemoveDeleteAndReaddClearsCreateResult() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      OpcUaMonitoredItem item = OpcUaMonitoredItem.newDataItem(new NodeId(2, "does-not-exist"));
      subscription.addMonitoredItem(item);

      List<MonitoredItemServiceOperationResult> createResults = subscription.createMonitoredItems();
      assertEquals(
          StatusCodes.Bad_NodeIdUnknown,
          createResults.get(0).operationResult().orElseThrow().value());
      assertEquals(StatusCodes.Bad_NodeIdUnknown, item.getCreateResult().orElseThrow().value());

      // Removing and calling deleteMonitoredItems() detaches without a server call and keeps
      // createResult readable while detached.
      subscription.removeMonitoredItem(item);
      assertTrue(subscription.deleteMonitoredItems().isEmpty());
      assertEquals(0, fixture.serviceSet.deleteCalls.get());
      assertTrue(item.getClientHandle().isEmpty());
      assertEquals(StatusCodes.Bad_NodeIdUnknown, item.getCreateResult().orElseThrow().value());

      // Re-adding clears the prior failed createResult so filtered create attempts it again.
      subscription.addMonitoredItem(item);
      assertAllResultsEmpty(item);

      List<MonitoredItemServiceOperationResult> retryResults =
          subscription.createMonitoredItems(i -> i.getCreateResult().isEmpty());
      assertEquals(1, retryResults.size());
      assertEquals(
          StatusCodes.Bad_NodeIdUnknown,
          retryResults.get(0).operationResult().orElseThrow().value());

      subscription.delete();
    }
  }

  private static void assertAllResultsEmpty(OpcUaMonitoredItem item) {
    assertTrue(item.getCreateResult().isEmpty());
    assertTrue(item.getModifyResult().isEmpty());
    assertTrue(item.getDeleteResult().isEmpty());
    assertTrue(item.getSetMonitoringModeResult().isEmpty());
  }

  private static final class ScriptableServiceSet extends DelegatingMonitoredItemServiceSet {

    final AtomicBoolean refuseModifyAndMode = new AtomicBoolean(false);
    final AtomicInteger deleteCalls = new AtomicInteger();

    private final AtomicBoolean gateNextDelete = new AtomicBoolean(false);
    private volatile CountDownLatch deleteArrived = new CountDownLatch(1);
    private volatile CountDownLatch deleteGate = new CountDownLatch(1);

    ScriptableServiceSet(OpcUaServer server) {
      super(server);
    }

    void gateNextDelete() {
      deleteArrived = new CountDownLatch(1);
      deleteGate = new CountDownLatch(1);
      gateNextDelete.set(true);
    }

    void awaitDeleteArrived() throws InterruptedException {
      assertTrue(
          deleteArrived.await(10, TimeUnit.SECONDS),
          "gated DeleteMonitoredItems request never arrived");
    }

    void releaseDelete() {
      deleteGate.countDown();
    }

    @Override
    public ModifyMonitoredItemsResponse onModifyMonitoredItems(
        ServiceRequestContext context, ModifyMonitoredItemsRequest request) throws UaException {

      if (refuseModifyAndMode.get()) {
        int count = request.getItemsToModify() == null ? 0 : request.getItemsToModify().length;
        var results = new MonitoredItemModifyResult[count];
        Arrays.fill(
            results,
            new MonitoredItemModifyResult(
                new StatusCode(StatusCodes.Bad_UserAccessDenied), 0.0, uint(0), null));

        return new ModifyMonitoredItemsResponse(
            header(request.getRequestHeader().getRequestHandle()), results, null);
      }

      return super.onModifyMonitoredItems(context, request);
    }

    @Override
    public SetMonitoringModeResponse onSetMonitoringMode(
        ServiceRequestContext context, SetMonitoringModeRequest request) throws UaException {

      if (refuseModifyAndMode.get()) {
        int count =
            request.getMonitoredItemIds() == null ? 0 : request.getMonitoredItemIds().length;
        var results = new StatusCode[count];
        Arrays.fill(results, new StatusCode(StatusCodes.Bad_UserAccessDenied));

        return new SetMonitoringModeResponse(
            header(request.getRequestHeader().getRequestHandle()), results, null);
      }

      return super.onSetMonitoringMode(context, request);
    }

    @Override
    public DeleteMonitoredItemsResponse onDeleteMonitoredItems(
        ServiceRequestContext context, DeleteMonitoredItemsRequest request) throws UaException {

      deleteCalls.incrementAndGet();

      if (gateNextDelete.compareAndSet(true, false)) {
        deleteArrived.countDown();
        try {
          if (!deleteGate.await(10, TimeUnit.SECONDS)) {
            throw new UaException(StatusCodes.Bad_Timeout, "delete gate was never released");
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new UaException(StatusCodes.Bad_UnexpectedError, e);
        }
      }

      return super.onDeleteMonitoredItems(context, request);
    }

    private static ResponseHeader header(UInteger requestHandle) {
      return new ResponseHeader(DateTime.now(), requestHandle, StatusCode.GOOD, null, null, null);
    }
  }

  private static final class Fixture implements AutoCloseable {

    final OpcUaServer server;
    final OpcUaClient client;
    final ScriptableServiceSet serviceSet;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      serviceSet = new ScriptableServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), serviceSet);
      }

      server.startup().get();

      client = TestClient.create(server, cfg -> cfg.setRequestTimeout(uint(10_000)));
      client.connect();
    }

    @Override
    public void close() throws Exception {
      serviceSet.releaseDelete();
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
  }
}
