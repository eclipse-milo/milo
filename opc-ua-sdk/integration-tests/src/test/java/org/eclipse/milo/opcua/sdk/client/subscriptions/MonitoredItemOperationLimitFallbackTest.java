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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingMonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * Verifies recovery when a Server enforces a lower MaxMonitoredItemsPerCall than it advertises.
 *
 * <p>The service set records the actual wire operations and delegates accepted singleton requests
 * to the real Server. This lets each test verify both the retry shape and the client-side state
 * produced by successful responses.
 */
public class MonitoredItemOperationLimitFallbackTest {

  /**
   * A rejected Create partition must be replayed in order, and the learned singleton limit must
   * immediately apply to every monitored-item service owned by the Subscription.
   */
  @Test
  void createFallbackPreservesOrderAndTheLearnedLimitIsShared() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.setMaxMonitoredItemsPerCall(uint(2));
      subscription.create();

      try {
        addItems(subscription, 5);

        List<MonitoredItemServiceOperationResult> createResults =
            subscription.createMonitoredItems();

        assertGood(createResults, 5);
        assertEquals(
            List.of(2, 1, 1, 1, 1, 1),
            fixture.serviceSet.requestSizes(Operation.CREATE),
            "the failed pair must be retried as singletons and all later work must stay singleton");

        List<UInteger> createOrder =
            fixture.serviceSet.requests(Operation.CREATE).stream()
                .skip(1)
                .flatMap(List::stream)
                .toList();
        assertEquals(
            createOrder,
            createResults.stream()
                .map(result -> result.monitoredItem().getClientHandle().orElseThrow())
                .toList(),
            "the result list must retain the wire-operation order after the retry");

        List<OpcUaMonitoredItem> items =
            createResults.stream().map(MonitoredItemServiceOperationResult::monitoredItem).toList();

        fixture.serviceSet.clearRequests();
        for (int i = 0; i < items.size(); i++) {
          items.get(i).setSamplingInterval(100.0 + i);
        }

        List<MonitoredItemServiceOperationResult> modifyResults =
            subscription.modifyMonitoredItems();

        assertGood(modifyResults, 5);
        assertEquals(
            List.of(1, 1, 1, 1, 1),
            fixture.serviceSet.requestSizes(Operation.MODIFY),
            "Modify must immediately reuse the limit learned by Create");
        assertEquals(
            fixture.serviceSet.requests(Operation.MODIFY).stream().flatMap(List::stream).toList(),
            monitoredItemIds(modifyResults));

        fixture.serviceSet.clearRequests();
        List<MonitoredItemServiceOperationResult> modeResults =
            subscription.setMonitoringMode(MonitoringMode.Disabled, items);

        assertGood(modeResults, 5);
        assertEquals(
            List.of(1, 1, 1, 1, 1),
            fixture.serviceSet.requestSizes(Operation.SET_MONITORING_MODE),
            "SetMonitoringMode must immediately reuse the limit learned by Create");
        assertEquals(items, resultItems(modeResults));

        Map<UInteger, OpcUaMonitoredItem> itemsById = itemsByMonitoredItemId(items);
        fixture.serviceSet.clearRequests();
        subscription.removeMonitoredItems(items);

        List<MonitoredItemServiceOperationResult> deleteResults =
            subscription.deleteMonitoredItems();

        assertGood(deleteResults, 5);
        assertEquals(
            List.of(1, 1, 1, 1, 1),
            fixture.serviceSet.requestSizes(Operation.DELETE),
            "Delete must immediately reuse the limit learned by Create");
        assertEquals(
            fixture.serviceSet.requests(Operation.DELETE).stream()
                .flatMap(List::stream)
                .map(itemsById::get)
                .toList(),
            resultItems(deleteResults),
            "Delete results must retain request order even though successful deletion clears ids");
      } finally {
        subscription.delete();
      }
    }
  }

  // A subscription may first encounter the hidden limit while modifying existing items.
  @Test
  void modifyIndependentlyFallsBackToSingletonRequests() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      try {
        List<OpcUaMonitoredItem> items = prepareCreatedItems(subscription, 3);
        for (int i = 0; i < items.size(); i++) {
          items.get(i).setSamplingInterval(200.0 + i);
        }
        fixture.serviceSet.clearRequests();

        List<MonitoredItemServiceOperationResult> results = subscription.modifyMonitoredItems();

        assertGood(results, 3);
        assertEquals(List.of(2, 1, 1, 1), fixture.serviceSet.requestSizes(Operation.MODIFY));
        assertEquals(
            fixture.serviceSet.requests(Operation.MODIFY).stream()
                .skip(1)
                .flatMap(List::stream)
                .toList(),
            monitoredItemIds(results));
      } finally {
        subscription.delete();
      }
    }
  }

  // A subscription may first encounter the hidden limit while deleting existing items.
  @Test
  void deleteIndependentlyFallsBackToSingletonRequests() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      try {
        List<OpcUaMonitoredItem> items = prepareCreatedItems(subscription, 3);
        Map<UInteger, OpcUaMonitoredItem> itemsById = itemsByMonitoredItemId(items);
        subscription.removeMonitoredItems(items);
        fixture.serviceSet.clearRequests();

        List<MonitoredItemServiceOperationResult> results = subscription.deleteMonitoredItems();

        assertGood(results, 3);
        assertEquals(List.of(2, 1, 1, 1), fixture.serviceSet.requestSizes(Operation.DELETE));
        assertEquals(
            fixture.serviceSet.requests(Operation.DELETE).stream()
                .skip(1)
                .flatMap(List::stream)
                .map(itemsById::get)
                .toList(),
            resultItems(results));
      } finally {
        subscription.delete();
      }
    }
  }

  // A subscription may first encounter the hidden limit while changing MonitoringMode.
  @Test
  void setMonitoringModeIndependentlyFallsBackToSingletonRequests() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.create();

      try {
        List<OpcUaMonitoredItem> items = prepareCreatedItems(subscription, 3);
        fixture.serviceSet.clearRequests();

        List<MonitoredItemServiceOperationResult> results =
            subscription.setMonitoringMode(MonitoringMode.Disabled, items);

        assertGood(results, 3);
        assertEquals(
            List.of(2, 1, 1, 1), fixture.serviceSet.requestSizes(Operation.SET_MONITORING_MODE));
        assertEquals(items, resultItems(results));
        assertTrue(
            items.stream().allMatch(item -> item.getMonitoringMode() == MonitoringMode.Disabled));
      } finally {
        subscription.delete();
      }
    }
  }

  // Ambiguous service failures must remain visible; replaying Create could duplicate server state.
  @Test
  void timeoutDoesNotTriggerSingletonRetries() throws Exception {
    try (var fixture = new Fixture()) {
      fixture.serviceSet.setRejectionStatus(StatusCodes.Bad_Timeout);

      var subscription = new OpcUaSubscription(fixture.client);
      subscription.setMaxMonitoredItemsPerCall(uint(2));
      subscription.create();

      try {
        List<OpcUaMonitoredItem> items = addItems(subscription, 2);

        List<MonitoredItemServiceOperationResult> results = subscription.createMonitoredItems();

        assertServiceFailure(results, StatusCodes.Bad_Timeout, 2);
        assertEquals(List.of(2), fixture.serviceSet.requestSizes(Operation.CREATE));
        assertEquals(itemsByClientHandle(items), itemsByClientHandle(resultItems(results)));
        assertTrue(
            items.stream()
                .allMatch(item -> item.getSyncState() == OpcUaMonitoredItem.SyncState.INITIAL));
      } finally {
        subscription.delete();
      }
    }
  }

  // Retrying a rejected singleton cannot reduce the request and would loop forever.
  @Test
  void singletonTooManyOperationsIsReportedWithoutRetry() throws Exception {
    try (var fixture = new Fixture()) {
      fixture.serviceSet.setMaximumAcceptedOperations(0);

      var subscription = new OpcUaSubscription(fixture.client);
      subscription.setMaxMonitoredItemsPerCall(uint(2));
      subscription.create();

      try {
        addItems(subscription, 1);

        List<MonitoredItemServiceOperationResult> results = subscription.createMonitoredItems();

        assertServiceFailure(results, StatusCodes.Bad_TooManyOperations, 1);
        assertEquals(List.of(1), fixture.serviceSet.requestSizes(Operation.CREATE));
      } finally {
        subscription.delete();
      }
    }
  }

  /**
   * SetMonitoringMode partitions client items, but its operation limit applies only to ids sent on
   * the wire. One valid id mixed with an invalid item is still a singleton service request.
   */
  @Test
  void setMonitoringModeUsesTheActualWireOperationCountForTheRetryGuard() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.setMaxMonitoredItemsPerCall(uint(2));
      subscription.create();

      try {
        OpcUaMonitoredItem validItem = addItems(subscription, 1).get(0);
        assertGood(subscription.createMonitoredItems(), 1);

        var invalidItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        fixture.serviceSet.setMaximumAcceptedOperations(0);
        fixture.serviceSet.clearRequests();

        List<MonitoredItemServiceOperationResult> results =
            subscription.setMonitoringMode(
                MonitoringMode.Disabled, List.of(invalidItem, validItem));

        assertEquals(List.of(1), fixture.serviceSet.requestSizes(Operation.SET_MONITORING_MODE));
        assertEquals(2, results.size());
        assertEquals(invalidItem, results.get(0).monitoredItem());
        assertEquals(StatusCodes.Bad_InvalidState, results.get(0).serviceResult().value());
        assertFalse(results.get(0).operationResult().isPresent());
        assertEquals(validItem, results.get(1).monitoredItem());
        assertEquals(StatusCodes.Bad_TooManyOperations, results.get(1).serviceResult().value());
        assertFalse(results.get(1).operationResult().isPresent());
        assertEquals(MonitoringMode.Reporting, validItem.getMonitoringMode());
      } finally {
        subscription.delete();
      }
    }
  }

  /**
   * DeleteMonitoredItems also partitions client items while counting only ids sent on the wire. A
   * concurrent item reset must not turn one transmitted id into a retryable multi-operation call.
   */
  @Test
  void deleteUsesTheActualWireOperationCountForTheRetryGuard() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.setMaxMonitoredItemsPerCall(uint(1));
      subscription.create();

      try {
        var validItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
        var resetItem = new ResetOnMonitoredItemIdRead();
        subscription.addMonitoredItems(List.of(validItem, resetItem));
        assertGood(subscription.createMonitoredItems(), 2);

        subscription.setMaxMonitoredItemsPerCall(uint(2));
        subscription.removeMonitoredItems(List.of(validItem, resetItem));
        resetItem.resetOnNextMonitoredItemIdRead();
        fixture.serviceSet.setMaximumAcceptedOperations(0);
        fixture.serviceSet.clearRequests();

        List<MonitoredItemServiceOperationResult> results = subscription.deleteMonitoredItems();

        assertEquals(List.of(1), fixture.serviceSet.requestSizes(Operation.DELETE));
        assertEquals(2, results.size());
        assertEquals(
            1,
            results.stream()
                .filter(
                    result -> result.serviceResult().value() == StatusCodes.Bad_TooManyOperations)
                .count());
        assertEquals(
            1,
            results.stream()
                .filter(result -> result.serviceResult().value() == StatusCodes.Bad_InvalidState)
                .count());
        assertTrue(results.stream().allMatch(result -> result.operationResult().isEmpty()));
      } finally {
        subscription.delete();
      }
    }
  }

  // The learned limit belongs to one server-side Subscription incarnation.
  @Test
  void recreatingTheSubscriptionClearsTheLearnedLimit() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.setMaxMonitoredItemsPerCall(uint(2));
      subscription.create();

      try {
        addItems(subscription, 2);
        assertGood(subscription.createMonitoredItems(), 2);
        assertEquals(List.of(2, 1, 1), fixture.serviceSet.requestSizes(Operation.CREATE));

        subscription.delete();
        subscription.create();
        fixture.serviceSet.clearRequests();

        List<MonitoredItemServiceOperationResult> results = subscription.createMonitoredItems();

        assertGood(results, 2);
        assertEquals(
            List.of(2, 1, 1),
            fixture.serviceSet.requestSizes(Operation.CREATE),
            "the replacement Subscription must be allowed one new optimistic attempt");
      } finally {
        subscription.delete();
      }
    }
  }

  // Changing the configured cap intentionally starts a new partition-size calculation.
  @Test
  void changingTheConfiguredLimitClearsTheLearnedLimit() throws Exception {
    try (var fixture = new Fixture()) {
      var subscription = new OpcUaSubscription(fixture.client);
      subscription.setMaxMonitoredItemsPerCall(uint(2));
      subscription.create();

      try {
        addItems(subscription, 2);
        assertGood(subscription.createMonitoredItems(), 2);

        fixture.serviceSet.clearRequests();
        addItems(subscription, 2);
        assertGood(subscription.createMonitoredItems(), 2);
        assertEquals(
            List.of(1, 1),
            fixture.serviceSet.requestSizes(Operation.CREATE),
            "control: the Subscription must have retained its learned singleton limit");

        addItems(subscription, 2);
        subscription.setMaxMonitoredItemsPerCall(uint(3));
        fixture.serviceSet.clearRequests();

        List<MonitoredItemServiceOperationResult> results = subscription.createMonitoredItems();

        assertGood(results, 2);
        assertEquals(
            List.of(2, 1, 1),
            fixture.serviceSet.requestSizes(Operation.CREATE),
            "changing the configured cap must permit one new optimistic attempt");
      } finally {
        subscription.delete();
      }
    }
  }

  private static List<OpcUaMonitoredItem> addItems(OpcUaSubscription subscription, int itemCount) {

    var items = new ArrayList<OpcUaMonitoredItem>(itemCount);
    for (int i = 0; i < itemCount; i++) {
      items.add(OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime));
    }
    subscription.addMonitoredItems(items);
    return items;
  }

  private static List<OpcUaMonitoredItem> prepareCreatedItems(
      OpcUaSubscription subscription, int itemCount) {

    subscription.setMaxMonitoredItemsPerCall(uint(1));
    addItems(subscription, itemCount);

    List<MonitoredItemServiceOperationResult> results = subscription.createMonitoredItems();
    assertGood(results, itemCount);

    subscription.setMaxMonitoredItemsPerCall(uint(2));
    return resultItems(results);
  }

  private static void assertGood(
      List<MonitoredItemServiceOperationResult> results, int expectedCount) {

    assertEquals(expectedCount, results.size());
    assertTrue(results.stream().allMatch(MonitoredItemServiceOperationResult::isGood));
  }

  private static void assertServiceFailure(
      List<MonitoredItemServiceOperationResult> results,
      long expectedStatusCode,
      int expectedCount) {

    assertEquals(expectedCount, results.size());
    assertTrue(
        results.stream().allMatch(result -> result.serviceResult().value() == expectedStatusCode));
    assertTrue(results.stream().allMatch(result -> result.operationResult().isEmpty()));
  }

  private static List<OpcUaMonitoredItem> resultItems(
      List<MonitoredItemServiceOperationResult> results) {

    return results.stream().map(MonitoredItemServiceOperationResult::monitoredItem).toList();
  }

  private static List<UInteger> monitoredItemIds(
      List<MonitoredItemServiceOperationResult> results) {

    return results.stream()
        .map(result -> result.monitoredItem().getMonitoredItemId().orElseThrow())
        .toList();
  }

  private static Map<UInteger, OpcUaMonitoredItem> itemsByMonitoredItemId(
      List<OpcUaMonitoredItem> items) {

    var itemsById = new HashMap<UInteger, OpcUaMonitoredItem>();
    for (OpcUaMonitoredItem item : items) {
      itemsById.put(item.getMonitoredItemId().orElseThrow(), item);
    }
    return itemsById;
  }

  private static Map<UInteger, OpcUaMonitoredItem> itemsByClientHandle(
      List<OpcUaMonitoredItem> items) {

    var itemsByHandle = new HashMap<UInteger, OpcUaMonitoredItem>();
    for (OpcUaMonitoredItem item : items) {
      itemsByHandle.put(item.getClientHandle().orElseThrow(), item);
    }
    return itemsByHandle;
  }

  private enum Operation {
    CREATE,
    MODIFY,
    DELETE,
    SET_MONITORING_MODE
  }

  /** A real MonitoredItem service set with a scriptable hidden per-request operation limit. */
  private static final class OperationLimitingMonitoredItemServiceSet
      extends DelegatingMonitoredItemServiceSet {

    private final Map<Operation, List<List<UInteger>>> requests = new EnumMap<>(Operation.class);

    private volatile int maximumAcceptedOperations = 1;
    private volatile long rejectionStatus = StatusCodes.Bad_TooManyOperations;

    OperationLimitingMonitoredItemServiceSet(OpcUaServer server) {
      super(server);

      for (Operation operation : Operation.values()) {
        requests.put(operation, new ArrayList<>());
      }
    }

    void setMaximumAcceptedOperations(int maximumAcceptedOperations) {
      this.maximumAcceptedOperations = maximumAcceptedOperations;
    }

    void setRejectionStatus(long rejectionStatus) {
      this.rejectionStatus = rejectionStatus;
    }

    synchronized void clearRequests() {
      requests.values().forEach(List::clear);
    }

    synchronized List<List<UInteger>> requests(Operation operation) {
      return requests.get(operation).stream().map(List::copyOf).toList();
    }

    List<Integer> requestSizes(Operation operation) {
      return requests(operation).stream().map(List::size).toList();
    }

    @Override
    public CreateMonitoredItemsResponse onCreateMonitoredItems(
        ServiceRequestContext context, CreateMonitoredItemsRequest request) throws UaException {

      List<UInteger> clientHandles =
          Arrays.stream(requireNonNull(request.getItemsToCreate()))
              .map(item -> item.getRequestedParameters().getClientHandle())
              .toList();
      recordAndRejectIfNecessary(Operation.CREATE, clientHandles);

      return super.onCreateMonitoredItems(context, request);
    }

    @Override
    public ModifyMonitoredItemsResponse onModifyMonitoredItems(
        ServiceRequestContext context, ModifyMonitoredItemsRequest request) throws UaException {

      List<UInteger> monitoredItemIds =
          Arrays.stream(requireNonNull(request.getItemsToModify()))
              .map(item -> item.getMonitoredItemId())
              .toList();
      recordAndRejectIfNecessary(Operation.MODIFY, monitoredItemIds);

      return super.onModifyMonitoredItems(context, request);
    }

    @Override
    public DeleteMonitoredItemsResponse onDeleteMonitoredItems(
        ServiceRequestContext context, DeleteMonitoredItemsRequest request) throws UaException {

      List<UInteger> monitoredItemIds =
          Arrays.stream(requireNonNull(request.getMonitoredItemIds())).toList();
      recordAndRejectIfNecessary(Operation.DELETE, monitoredItemIds);

      return super.onDeleteMonitoredItems(context, request);
    }

    @Override
    public SetMonitoringModeResponse onSetMonitoringMode(
        ServiceRequestContext context, SetMonitoringModeRequest request) throws UaException {

      List<UInteger> monitoredItemIds =
          Arrays.stream(requireNonNull(request.getMonitoredItemIds())).toList();
      recordAndRejectIfNecessary(Operation.SET_MONITORING_MODE, monitoredItemIds);

      return super.onSetMonitoringMode(context, request);
    }

    private synchronized void recordAndRejectIfNecessary(
        Operation operation, List<UInteger> operationKeys) throws UaException {

      requests.get(operation).add(List.copyOf(operationKeys));

      if (operationKeys.size() > maximumAcceptedOperations) {
        throw new UaException(
            rejectionStatus,
            "scripted " + operation + " rejection for " + operationKeys.size() + " operations");
      }
    }
  }

  private static final class ResetOnMonitoredItemIdRead extends OpcUaMonitoredItem {

    private boolean resetOnNextMonitoredItemIdRead;

    private ResetOnMonitoredItemIdRead() {
      super(
          new ReadValueId(
              NodeIds.Server_ServerStatus_CurrentTime,
              AttributeId.Value.uid(),
              null,
              QualifiedName.NULL_VALUE));
    }

    void resetOnNextMonitoredItemIdRead() {
      resetOnNextMonitoredItemIdRead = true;
    }

    @Override
    public Optional<UInteger> getMonitoredItemId() {
      if (resetOnNextMonitoredItemIdRead) {
        resetOnNextMonitoredItemIdRead = false;
        reset();
      }

      return super.getMonitoredItemId();
    }
  }

  /** A running Server with the operation-limiting service set and a connected client. */
  private static final class Fixture implements AutoCloseable {

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final OperationLimitingMonitoredItemServiceSet serviceSet;

    Fixture() throws Exception {
      TestServer testServer = TestServer.create();
      server = testServer.getServer();

      serviceSet = new OperationLimitingMonitoredItemServiceSet(server);
      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), serviceSet);
      }

      server.startup().get();

      client = TestClient.create(server, configBuilder -> {});
      client.connect();
    }

    @Override
    public void close() throws Exception {
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
  }
}
