/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** The read access refresh that runs at the start of every SubscriptionModel sampling cycle. */
class SubscriptionModelReadAccessTest {

  private final OpcUaServer server = mock(OpcUaServer.class);
  private final AccessController accessController = mock(AccessController.class);
  private final AddressSpace addressSpace = mock(AddressSpace.class);
  private final Session session = mock(Session.class);

  private final ReadValueId allowedId =
      new ReadValueId(new NodeId(2, "allowed"), AttributeId.Value.uid(), null, null);
  private final ReadValueId deniedId =
      new ReadValueId(new NodeId(2, "denied"), AttributeId.Value.uid(), null, null);

  private final DataItem allowed = item(allowedId);
  private final DataItem denied = item(deniedId);

  private SubscriptionModel model;

  @BeforeEach
  void setUp() {
    // Never schedule a second cycle, so each test observes only the cycles it triggers.
    ScheduledExecutorService scheduler = mock(ScheduledExecutorService.class);
    when(scheduler.schedule(any(Runnable.class), anyLong(), any()))
        .thenReturn(mock(ScheduledFuture.class));

    when(server.getScheduledExecutorService()).thenReturn(scheduler);
    when(server.getAccessController()).thenReturn(accessController);
  }

  /** Start a model whose queue and sampling cycles run on the calling thread. */
  private void startModelOnCallingThread() {
    ExecutorService executor = mock(ExecutorService.class);
    doAnswer(
            inv -> {
              ((Runnable) inv.getArgument(0)).run();
              return null;
            })
        .when(executor)
        .execute(any(Runnable.class));

    startModel(executor);
  }

  private void startModel(ExecutorService executor) {
    when(server.getExecutorService()).thenReturn(executor);

    model = new SubscriptionModel(server, addressSpace);
    model.startup();
  }

  // Part 4 §5.13.2.1: the sampler is what notices an access change, so every cycle must check and
  // tell each item the result. A denied item is not read at all; its denial is already queued.
  @Test
  void eachCycleRefreshesReadAccessAndReadsOnlyAllowedItems() {
    startModelOnCallingThread();

    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(
            Map.of(allowedId, AccessResult.ALLOWED, deniedId, AccessResult.DENIED_USER_ACCESS));
    when(addressSpace.read(any(), eq(0d), eq(TimestampsToReturn.Both), eq(List.of(allowedId))))
        .thenReturn(List.of(new DataValue(new Variant(1))));

    model.onDataItemsCreated(List.of(allowed, denied));

    verify(allowed).setReadAccessResult(AccessResult.ALLOWED);
    verify(denied).setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
    verify(allowed).setValue(any());
    verify(denied, never()).setValue(any());
    verify(addressSpace).read(any(), eq(0d), eq(TimestampsToReturn.Both), eq(List.of(allowedId)));
  }

  // A fault in the check is not an access decision: items keep their last result and are still
  // read, and the item substitutes a denial it already holds.
  @Test
  void failedCheckKeepsLastResultAndStillReads() {
    startModelOnCallingThread();

    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenThrow(new IllegalStateException("address space unavailable"));
    when(addressSpace.read(any(), eq(0d), eq(TimestampsToReturn.Both), anyList()))
        .thenReturn(List.of(new DataValue(new Variant(1)), new DataValue(new Variant(2))));

    model.onDataItemsCreated(List.of(allowed, denied));

    verify(allowed, never()).setReadAccessResult(any());
    verify(denied, never()).setReadAccessResult(any());
    verify(allowed).setValue(any());
    verify(denied).setValue(any());
  }

  // A change to the item set rebuilds the cycles while one may still be mid-check. The old cycle's
  // result is stale by then; applying it after the replacement's result would clear a denial.
  @Test
  void cancelledCycleDoesNotApplyItsStaleResult() throws Exception {
    ExecutorService pool = Executors.newCachedThreadPool();
    try {
      startModel(pool);

      var firstCheckStarted = new CountDownLatch(1);
      var releaseFirstCheck = new CountDownLatch(1);
      var denialApplied = new CountDownLatch(1);
      var checks = new AtomicInteger();

      when(accessController.checkReadAccess(eq(session), anyList()))
          .thenAnswer(
              inv -> {
                if (checks.getAndIncrement() == 0) {
                  // The first cycle's check is overtaken: access is revoked while it blocks.
                  firstCheckStarted.countDown();
                  assertTrue(releaseFirstCheck.await(5, TimeUnit.SECONDS));
                  return Map.of(allowedId, AccessResult.ALLOWED);
                }
                return Map.of(allowedId, AccessResult.DENIED_USER_ACCESS);
              });
      doAnswer(
              inv -> {
                if (inv.getArgument(0) == AccessResult.DENIED_USER_ACCESS) {
                  denialApplied.countDown();
                }
                return null;
              })
          .when(allowed)
          .setReadAccessResult(any());

      model.onDataItemsCreated(List.of(allowed));
      assertTrue(firstCheckStarted.await(5, TimeUnit.SECONDS));

      // Rebuild the cycles: the first is cancelled mid-check, the replacement applies the denial.
      model.onDataItemsModified(List.of(allowed));
      assertTrue(denialApplied.await(5, TimeUnit.SECONDS));

      releaseFirstCheck.countDown();

      verify(allowed, after(1000).never()).setReadAccessResult(AccessResult.ALLOWED);
      verify(addressSpace, never()).read(any(), any(), any(), anyList());
    } finally {
      pool.shutdownNow();
    }
  }

  private DataItem item(ReadValueId readValueId) {
    DataItem item = mock(DataItem.class);
    when(item.getSession()).thenReturn(session);
    when(item.getReadValueId()).thenReturn(readValueId);
    when(item.isSamplingEnabled()).thenReturn(true);
    when(item.getSamplingInterval()).thenReturn(100.0);
    when(item.getTimestampsToReturn()).thenReturn(TimestampsToReturn.Both);
    return item;
  }
}
