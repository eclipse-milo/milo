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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@code VersionTime} computation and persistence behavior of {@link AliasVersionManager}
 * in isolation, against a Server whose AddressSpace is empty: no ancestor categories are discovered
 * and no {@code LastChange} Property Nodes exist, so only the version arithmetic and store
 * interaction are exercised.
 */
class AliasVersionManagerTest {

  /** The {@code VersionTime} epoch, 2000-01-01T00:00:00Z, as a Unix epoch second (Part 4 §7.43). */
  private static final long VERSION_TIME_EPOCH_SECOND = 946684800L;

  /** A VersionTime value far in the future (~year 2126), safely ahead of any test-run clock. */
  private static final long FAR_FUTURE_VERSION = 4_000_000_000L;

  private static final String TEST_NAMESPACE_URI = "urn:eclipse:milo:test";

  private static OpcUaServer serverWithEmptyAddressSpace() {
    OpcUaServer server = mock(OpcUaServer.class);
    AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);

    var namespaceTable = new NamespaceTable();
    namespaceTable.add(TEST_NAMESPACE_URI);

    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    when(server.getNamespaceTable()).thenReturn(namespaceTable);
    when(addressSpaceManager.getManagedReferences(any(NodeId.class), any())).thenReturn(List.of());
    when(addressSpaceManager.getManagedNode(any(NodeId.class))).thenReturn(Optional.empty());

    return server;
  }

  private static long secondsSince2000() {
    return Instant.now().getEpochSecond() - VERSION_TIME_EPOCH_SECOND;
  }

  // Part 4 §7.43: VersionTime is seconds since 2000-01-01T00:00:00Z. A category's
  // first bump must produce the current wall-clock VersionTime, not a counter
  // starting at 1, so Clients can compare it against real time.
  @Test
  void firstTouchSeedsCategoryWithCurrentVersionTime() throws UaException {
    var manager =
        new AliasVersionManager(serverWithEmptyAddressSpace(), new InMemoryAliasVersionStore());
    var categoryId = new NodeId(1, "category");

    long before = secondsSince2000();
    manager.touch(categoryId);
    long after = secondsSince2000();

    long value = manager.get(categoryId).orElseThrow().longValue();
    assertTrue(before <= value && value <= after, "expected current VersionTime, got " + value);
  }

  // Design invariant: next = max(secondsSince2000(now), previous + 1). Two mutations
  // within the same wall-clock second must still produce distinct, increasing
  // LastChange values, or Clients caching by LastChange would miss the second change.
  @Test
  void consecutiveTouchesProduceStrictlyIncreasingValues() throws UaException {
    var manager =
        new AliasVersionManager(serverWithEmptyAddressSpace(), new InMemoryAliasVersionStore());
    var categoryId = new NodeId(1, "category");

    manager.touch(categoryId);
    long first = manager.get(categoryId).orElseThrow().longValue();

    manager.touch(categoryId);
    long second = manager.get(categoryId).orElseThrow().longValue();

    assertTrue(second > first, "expected " + second + " > " + first);
  }

  // The previous+1 arm of the formula protects monotonicity under clock rollback:
  // when the persisted version is ahead of the clock (e.g. the clock regressed
  // across a restart), the next value must advance by one, never regress to "now".
  @Test
  void touchAdvancesByOneWhenPersistedVersionIsAheadOfTheClock() throws UaException {
    var store = new InMemoryAliasVersionStore();
    var categoryId = new NodeId(1, "category");
    var storeKey = ExpandedNodeId.of(TEST_NAMESPACE_URI, "category");
    store.save(storeKey, uint(FAR_FUTURE_VERSION));

    var manager = new AliasVersionManager(serverWithEmptyAddressSpace(), store);

    // loadPersisted resolves the URI-qualified store keys against the namespace table and
    // seeds the in-memory state.
    Map<NodeId, UInteger> loaded = manager.loadPersisted();
    assertEquals(Map.of(categoryId, uint(FAR_FUTURE_VERSION)), loaded);
    assertEquals(Optional.of(uint(FAR_FUTURE_VERSION)), manager.get(categoryId));

    manager.touch(categoryId);

    assertEquals(Optional.of(uint(FAR_FUTURE_VERSION + 1)), manager.get(categoryId));

    // Part 17 §6.3.1: every bump persists the new value through the store, under the same
    // URI-qualified key it was loaded from.
    assertEquals(uint(FAR_FUTURE_VERSION + 1), store.load().get(storeKey));
  }

  // A store entry whose namespace URI is not registered cannot belong to any live
  // category; loadPersisted must skip it rather than fail startup, leaving the inert
  // entry in the store untouched.
  @Test
  void loadPersistedSkipsEntriesWithUnregisteredNamespaceUris() throws UaException {
    var store = new InMemoryAliasVersionStore();
    store.save(ExpandedNodeId.of("urn:not:registered", "category"), uint(FAR_FUTURE_VERSION));

    var manager = new AliasVersionManager(serverWithEmptyAddressSpace(), store);

    assertTrue(manager.loadPersisted().isEmpty());
  }

  // Save-before-mutate: a version is persisted by prepare BEFORE the mutation it
  // describes is applied, and a failed save aborts with the version state unchanged.
  // Advancing in memory despite a failed save would let LastChange re-produce an
  // observed value after a restart — undetectable staleness for Client caches.
  @Test
  void failedSaveFailsPrepareWithoutAdvancingTheVersion() {
    var failingStore =
        new AliasVersionStore() {
          @Override
          public Map<ExpandedNodeId, UInteger> load() {
            return Map.of();
          }

          @Override
          public void save(ExpandedNodeId categoryId, UInteger value) throws UaException {
            throw new UaException(StatusCodes.Bad_ResourceUnavailable, "save failed");
          }
        };

    var manager = new AliasVersionManager(serverWithEmptyAddressSpace(), failingStore);
    var categoryId = new NodeId(1, "category");

    UaException e = assertThrows(UaException.class, () -> manager.prepare(List.of(categoryId)));

    assertEquals(StatusCodes.Bad_InternalError, e.getStatusCode().value());
    assertEquals(Optional.empty(), manager.get(categoryId), "version must not advance");

    // Publishing after the failed prepare must be a harmless no-op.
    manager.publishPending();
    assertEquals(Optional.empty(), manager.get(categoryId));
  }

  // The two-phase contract: prepare persists immediately, publishPending only writes
  // Property Nodes (none exist here) and clears the pending set, so a category is
  // prepared at most once between publishes no matter how many entries touch it.
  @Test
  void prepareIsIdempotentPerCategoryUntilPublished() throws UaException {
    var manager =
        new AliasVersionManager(serverWithEmptyAddressSpace(), new InMemoryAliasVersionStore());
    var categoryId = new NodeId(1, "category");

    manager.prepare(List.of(categoryId));
    long first = manager.get(categoryId).orElseThrow().longValue();

    manager.prepare(List.of(categoryId));
    assertEquals(first, manager.get(categoryId).orElseThrow().longValue());

    manager.publishPending();

    manager.prepare(List.of(categoryId));
    long second = manager.get(categoryId).orElseThrow().longValue();
    assertTrue(second > first, "expected " + second + " > " + first);
  }
}
