/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.aliases;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/** Tests the {@link AliasVersionStore} contract as implemented by the in-memory default store. */
class InMemoryAliasVersionStoreTest {

  private static final String TEST_NAMESPACE_URI = "urn:eclipse:milo:test";

  // The store's only job is preserving LastChange versions between save and load;
  // repeated saves for the same category must yield the latest value.
  @Test
  void savedVersionsRoundTripThroughLoadWithLatestValueWinning() throws Exception {
    var store = new InMemoryAliasVersionStore();
    ExpandedNodeId aliases = ExpandedNodeId.of(Namespaces.OPC_UA, uint(23470));
    ExpandedNodeId tagVariables = ExpandedNodeId.of(Namespaces.OPC_UA, uint(23479));

    store.save(aliases, uint(100));
    store.save(tagVariables, uint(200));
    store.save(aliases, uint(101));

    assertEquals(Map.of(aliases, uint(101), tagVariables, uint(200)), store.load());
  }

  // AliasVersionStore.load is called once at startup to seed the manager's state;
  // a live or mutable view would let later saves (or callers) corrupt that seed.
  @Test
  void loadReturnsAnImmutableSnapshotUnaffectedByLaterSaves() throws Exception {
    var store = new InMemoryAliasVersionStore();
    ExpandedNodeId categoryId = ExpandedNodeId.of(TEST_NAMESPACE_URI, "category");

    store.save(categoryId, uint(1));
    Map<ExpandedNodeId, UInteger> loaded = store.load();

    assertThrows(
        UnsupportedOperationException.class,
        () -> loaded.put(ExpandedNodeId.of(TEST_NAMESPACE_URI, "other"), uint(2)));

    store.save(categoryId, uint(2));
    assertEquals(uint(1), loaded.get(categoryId), "snapshot must not reflect later saves");
  }

  @Test
  void loadIsEmptyWhenNothingHasBeenSaved() throws Exception {
    assertTrue(new InMemoryAliasVersionStore().load().isEmpty());
  }

  // Removed categories must not leak entries in the store forever (AliasVersionStore.delete is
  // called on category removal); deleting an absent key is a harmless no-op.
  @Test
  void deleteRemovesTheEntryAndDeletingAnAbsentKeyIsANoOp() throws Exception {
    var store = new InMemoryAliasVersionStore();
    ExpandedNodeId categoryId = ExpandedNodeId.of(TEST_NAMESPACE_URI, "category");

    store.save(categoryId, uint(1));
    store.delete(categoryId);
    assertTrue(store.load().isEmpty());

    store.delete(categoryId);
    assertTrue(store.load().isEmpty());
  }

  // The store advertises thread safety; concurrent saves to distinct categories must
  // not lose entries. Distinct keys keep the expected end state deterministic.
  @Test
  void concurrentSavesToDistinctCategoriesAllSurvive() throws Exception {
    var store = new InMemoryAliasVersionStore();

    int threadCount = 4;
    int savesPerThread = 250;

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    try {
      var tasks = new ArrayList<Callable<@Nullable Void>>();
      for (int t = 0; t < threadCount; t++) {
        int thread = t;
        tasks.add(
            () -> {
              for (int i = 0; i < savesPerThread; i++) {
                int key = thread * savesPerThread + i;
                store.save(ExpandedNodeId.of(TEST_NAMESPACE_URI, "category-" + key), uint(key));
              }
              return null;
            });
      }

      List<Future<@Nullable Void>> futures = executor.invokeAll(tasks);
      for (Future<@Nullable Void> future : futures) {
        future.get(); // propagate any save failure
      }
    } finally {
      executor.shutdown();
      assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
    }

    Map<ExpandedNodeId, UInteger> loaded = store.load();
    assertEquals(threadCount * savesPerThread, loaded.size());
    for (int key = 0; key < threadCount * savesPerThread; key++) {
      assertEquals(uint(key), loaded.get(ExpandedNodeId.of(TEST_NAMESPACE_URI, "category-" + key)));
    }
  }
}
