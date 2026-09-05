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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OperationLimits;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** Deterministic response, reset, and executor boundaries in the subscription lifecycle. */
class SubscriptionLifecycleRecoveryTest {
  private final ExecutorService workers = Executors.newCachedThreadPool();

  @AfterEach
  void stopWorkers() throws InterruptedException {
    workers.shutdownNow();
    assertTrue(workers.awaitTermination(5, TimeUnit.SECONDS));
  }

  @Test
  void resetAndReaddPreserveTheMappedItemsClientHandle() throws Exception {
    var fixture = new Fixture(Runnable::run);
    fixture.create();
    var item = new GatedResetItem();
    fixture.subscription.addMonitoredItem(item);
    item.applyCreateResult(createdItem(41));
    fixture.subscription.removeMonitoredItem(item);

    var reset = workers.submit(fixture.subscription::reset);
    assertTrue(item.entered.await(5, TimeUnit.SECONDS));
    var readded = new CompletableFuture<Void>();
    Thread adding =
        new Thread(
            () -> {
              try {
                fixture.subscription.addMonitoredItem(item);
                readded.complete(null);
              } catch (Throwable t) {
                readded.completeExceptionally(t);
              }
            });
    adding.start();
    try {
      // Wait until add either acquires the collection lock or is blocked on reset's ownership.
      // Releasing the reset before add reaches this boundary would miss the original interleaving.
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
      while (!readded.isDone()
          && adding.getState() != Thread.State.BLOCKED
          && System.nanoTime() < deadline) {
        Thread.yield();
      }
      assertTrue(readded.isDone() || adding.getState() == Thread.State.BLOCKED);
    } finally {
      item.release.countDown();
      adding.join(5000);
    }
    reset.get(5, TimeUnit.SECONDS);
    readded.get(5, TimeUnit.SECONDS);
    assertTrue(fixture.subscription.getMonitoredItems().contains(item));
    assertTrue(item.getClientHandle().isPresent(), "mapped items must retain a requestable handle");
    fixture.create();
    when(fixture.client.createMonitoredItems(any(), any(), anyList()))
        .thenReturn(
            new CreateMonitoredItemsResponse(
                null, new MonitoredItemCreateResult[] {createdItem(42)}, null));
    assertTrue(fixture.subscription.createMonitoredItems().get(0).isGood());
  }

  @Test
  void rejectedTransitionHandoffPreservesSuccessAndDrainsWaiters() throws Exception {
    assertHandoffCompletes(
        command -> {
          throw new RejectedExecutionException("executor stopped");
        });
  }

  @Test
  void directTransitionHandoffDrainsWithoutRecursion() throws Exception {
    assertHandoffCompletes(Runnable::run);
  }

  private void assertHandoffCompletes(Executor executor) throws Exception {
    var fixture = new Fixture(executor);
    var response = new CompletableFuture<CreateSubscriptionResponse>();
    when(fixture.client.createSubscriptionAsync(
            anyDouble(), any(), any(), any(), anyBoolean(), any()))
        .thenReturn(response);
    var create = fixture.subscription.createAsync().toCompletableFuture();
    var queued = new ArrayList<CompletableFuture<?>>();
    for (int i = 0; i < 10_000; i++) {
      queued.add(fixture.subscription.modifyAsync().toCompletableFuture());
    }
    response.complete(Fixture.created(21));
    create.get(5, TimeUnit.SECONDS);
    CompletableFuture.allOf(queued.toArray(CompletableFuture[]::new)).get(5, TimeUnit.SECONDS);
    fixture
        .subscription
        .setPublishingModeAsync(false)
        .toCompletableFuture()
        .get(5, TimeUnit.SECONDS);
  }

  @Test
  void delayedCreateCannotPopulateRecreatedSubscription() throws Exception {
    assertDelayedResponseIgnored(Operation.CREATE);
  }

  @Test
  void delayedModifyCannotOverwriteRecreatedItem() throws Exception {
    assertDelayedResponseIgnored(Operation.MODIFY);
  }

  @Test
  void delayedDeleteCannotDetachRecreatedItem() throws Exception {
    assertDelayedResponseIgnored(Operation.DELETE);
  }

  @Test
  void delayedMonitoringModeCannotChangeRecreatedItem() throws Exception {
    assertDelayedResponseIgnored(Operation.MODE);
  }

  private void assertDelayedResponseIgnored(Operation operation) throws Exception {
    var fixture = new Fixture(Runnable::run);
    fixture.create();
    var item = fixture.addItem();
    if (operation != Operation.CREATE) item.applyCreateResult(createdItem(41));
    if (operation == Operation.MODIFY) item.setSamplingInterval(250.0);
    if (operation == Operation.DELETE) fixture.subscription.removeMonitoredItem(item);
    var entered = new CountDownLatch(1);
    var release = new CountDownLatch(1);
    var firstCall = new AtomicBoolean(true);
    org.mockito.stubbing.Answer<Object> gate =
        invocation -> {
          if (firstCall.getAndSet(false)) {
            entered.countDown();
            assertTrue(release.await(5, TimeUnit.SECONDS));
          }
          return switch (operation) {
            case CREATE ->
                new CreateMonitoredItemsResponse(
                    null, new MonitoredItemCreateResult[] {createdItem(41)}, null);
            case MODIFY ->
                new ModifyMonitoredItemsResponse(
                    null,
                    new MonitoredItemModifyResult[] {
                      new MonitoredItemModifyResult(StatusCode.GOOD, 250.0, uint(1), null)
                    },
                    null);
            case DELETE ->
                new DeleteMonitoredItemsResponse(null, new StatusCode[] {StatusCode.GOOD}, null);
            case MODE ->
                new SetMonitoringModeResponse(null, new StatusCode[] {StatusCode.GOOD}, null);
          };
        };
    switch (operation) {
      case CREATE ->
          when(fixture.client.createMonitoredItems(any(), any(), anyList())).thenAnswer(gate);
      case MODIFY ->
          when(fixture.client.modifyMonitoredItems(any(), any(), anyList())).thenAnswer(gate);
      case DELETE -> when(fixture.client.deleteMonitoredItems(any(), anyList())).thenAnswer(gate);
      case MODE -> when(fixture.client.setMonitoringMode(any(), any(), anyList())).thenAnswer(gate);
    }
    var oldId = fixture.subscription.getSubscriptionId().orElseThrow();
    var pending =
        workers.submit(
            () ->
                switch (operation) {
                  case CREATE -> fixture.subscription.createMonitoredItems();
                  case MODIFY -> fixture.subscription.modifyMonitoredItems();
                  case DELETE -> fixture.subscription.deleteMonitoredItems();
                  case MODE ->
                      fixture.subscription.setMonitoringMode(
                          MonitoringMode.Disabled, List.of(item));
                });
    assertTrue(entered.await(5, TimeUnit.SECONDS));
    try {
      fixture.subscription.reset();
      fixture.create();
      assertNotEquals(oldId, fixture.subscription.getSubscriptionId().orElseThrow());
      if (operation == Operation.DELETE) fixture.subscription.addMonitoredItem(item);
      if (operation != Operation.CREATE) item.applyCreateResult(createdItem(99));
    } finally {
      release.countDown();
    }
    var results = pending.get(5, TimeUnit.SECONDS);
    assertEquals(1, results.size());
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidState), results.get(0).serviceResult());
    assertTrue(item.getClientHandle().isPresent());
    if (operation == Operation.CREATE) {
      assertEquals(OpcUaMonitoredItem.SyncState.INITIAL, item.getSyncState());
      assertTrue(item.getMonitoredItemId().isEmpty());
      assertTrue(fixture.subscription.createMonitoredItems().get(0).isGood());
    } else {
      assertEquals(OpcUaMonitoredItem.SyncState.SYNCHRONIZED, item.getSyncState());
      assertEquals(uint(99), item.getMonitoredItemId().orElseThrow());
      assertEquals(1000.0, item.getRevisedSamplingInterval().orElseThrow());
      assertEquals(MonitoringMode.Reporting, item.getMonitoringMode());
    }
  }

  private enum Operation {
    CREATE,
    MODIFY,
    DELETE,
    MODE
  }

  private static MonitoredItemCreateResult createdItem(int id) {
    return new MonitoredItemCreateResult(StatusCode.GOOD, uint(id), 1000.0, uint(1), null);
  }

  private static ReadValueId readValueId() {
    return new ReadValueId(new NodeId(2, "value"), uint(13), null, QualifiedName.NULL_VALUE);
  }

  private static class GatedResetItem extends OpcUaMonitoredItem {
    final CountDownLatch entered = new CountDownLatch(1);
    final CountDownLatch release = new CountDownLatch(1);

    GatedResetItem() {
      super(readValueId());
    }

    @Override
    void reset() {
      entered.countDown();
      try {
        assertTrue(release.await(5, TimeUnit.SECONDS));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new AssertionError(e);
      }
      super.reset();
    }
  }

  private static class Fixture {
    final OpcUaClient client = mock(OpcUaClient.class, RETURNS_DEEP_STUBS);
    final AtomicInteger ids = new AtomicInteger(10);
    final OpcUaSubscription subscription;

    Fixture(Executor executor) throws Exception {
      var executorService = mock(ExecutorService.class);
      doAnswer(
              invocation -> {
                executor.execute(invocation.getArgument(0));
                return null;
              })
          .when(executorService)
          .execute(any());
      when(client.getTransport().getConfig().getExecutor()).thenReturn(executorService);
      when(client.getPublishingManager().isPublishingSuspended()).thenReturn(true);
      when(client.getOperationLimits()).thenReturn(limits());
      when(client.createSubscriptionAsync(anyDouble(), any(), any(), any(), anyBoolean(), any()))
          .thenAnswer(
              invocation -> CompletableFuture.completedFuture(created(ids.incrementAndGet())));
      when(client.setPublishingModeAsync(anyBoolean(), anyList()))
          .thenReturn(
              CompletableFuture.completedFuture(
                  new SetPublishingModeResponse(null, new StatusCode[] {StatusCode.GOOD}, null)));
      subscription = new OpcUaSubscription(client);
    }

    static OperationLimits limits() {
      return new OperationLimits(
          null, null, null, null, null, null, null, uint(100), null, null, null, null);
    }

    static CreateSubscriptionResponse created(int id) {
      return new CreateSubscriptionResponse(null, uint(id), 1000.0, uint(50), uint(10));
    }

    void create() throws Exception {
      subscription.createAsync().toCompletableFuture().get(5, TimeUnit.SECONDS);
    }

    OpcUaMonitoredItem addItem() {
      var item = new OpcUaMonitoredItem(readValueId());
      subscription.addMonitoredItem(item);
      return item;
    }
  }
}
