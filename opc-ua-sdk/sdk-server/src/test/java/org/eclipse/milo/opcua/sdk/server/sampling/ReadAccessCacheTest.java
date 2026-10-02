/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** What {@link ReadAccessCache} answers from memory, what it checks, and what drops its entries. */
class ReadAccessCacheTest {

  private final OpcUaServer server = mock(OpcUaServer.class);
  private final AccessController accessController = mock(AccessController.class);
  private final Session session = mock(Session.class);
  private final Session otherSession = mock(Session.class);

  private final ReadValueId a = readValueId(new NodeId(2, "a"));
  private final ReadValueId b = readValueId(new NodeId(2, "b"));
  private final ReadValueId c = readValueId(new NodeId(3, "c"));

  private ReadAccessCache cache;

  @BeforeEach
  void setUp() {
    when(server.getAccessController()).thenReturn(accessController);
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenAnswer(invocation -> allowAll(invocation.getArgument(1)));
    when(accessController.checkReadAccess(eq(otherSession), anyList()))
        .thenAnswer(invocation -> allowAll(invocation.getArgument(1)));

    cache = new ReadAccessCache(server);
  }

  // The point of the cache: the second cycle pays a lookup, not a read of the Node's attributes.
  @Test
  void aHitIsAnsweredWithoutAnotherCheck() {
    cache.getOrCheck(session, List.of(a, b));
    Map<ReadValueId, AccessResult> second = cache.getOrCheck(session, List.of(a, b));

    assertEquals(Map.of(a, AccessResult.ALLOWED, b, AccessResult.ALLOWED), second);
    verify(accessController, times(1)).checkReadAccess(eq(session), anyList());
  }

  // Only the misses are checked, in one call, and merged with the hits.
  @Test
  void onlyMissesAreChecked() {
    cache.getOrCheck(session, List.of(a));

    Map<ReadValueId, AccessResult> results = cache.getOrCheck(session, List.of(a, b));

    assertEquals(Map.of(a, AccessResult.ALLOWED, b, AccessResult.ALLOWED), results);
    verify(accessController).checkReadAccess(session, List.of(b));
  }

  // The key is the Session object: a transferred Subscription or a reconnected user must not
  // inherit decisions made for another Session.
  @Test
  void decisionsAreNotSharedBetweenSessions() {
    cache.getOrCheck(session, List.of(a));

    assertEquals(Optional.empty(), cache.get(otherSession, a));
    cache.getOrCheck(otherSession, List.of(a));
    verify(accessController).checkReadAccess(eq(otherSession), anyList());
  }

  // No decision is no answer: a Node that is unknown now may be known on the next cycle, so the
  // question has to be asked again.
  @Test
  void anUnknownNodeResultIsNotStored() {
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(Map.of(a, AccessResult.NODE_UNKNOWN));

    assertEquals(Map.of(a, AccessResult.NODE_UNKNOWN), cache.getOrCheck(session, List.of(a)));
    assertEquals(Optional.empty(), cache.get(session, a));
  }

  // A closed Session keeps its items until its Subscriptions expire; refilling for it would only
  // hold memory for a Session that cannot read anything again.
  @Test
  void nothingIsStoredForAClosedSession() {
    when(session.isClosed()).thenReturn(true);

    cache.getOrCheck(session, List.of(a));

    assertEquals(0, cache.size());
  }

  @Test
  void invalidationScopesDropExactlyWhatTheyCover() {
    cache.getOrCheck(session, List.of(a, b, c));
    cache.getOrCheck(otherSession, List.of(a, b, c));
    assertEquals(6, cache.size());

    cache.invalidate(ReadAccessScope.node(a.getNodeId()));
    assertEquals(4, cache.size(), "one Node for every Session");

    cache.invalidate(ReadAccessScope.namespace(ushort(3)).forSession(session));
    assertEquals(3, cache.size(), "one namespace for one Session");
    assertEquals(Optional.of(AccessResult.ALLOWED), cache.get(otherSession, c));

    cache.invalidate(ReadAccessScope.matching(nodeId -> "b".equals(nodeId.getIdentifier())));
    assertEquals(1, cache.size(), "every Node matching the predicate, for every Session");

    cache.invalidate(ReadAccessScope.session(otherSession));
    assertEquals(0, cache.size(), "every Node for one Session");

    cache.getOrCheck(session, List.of(a));
    cache.invalidate(ReadAccessScope.all());
    assertEquals(0, cache.size(), "everything");
  }

  /**
   * A check reads the Node's attributes before the answer is stored. If the attributes change and
   * an invalidation arrives in between, the answer describes the state before the change: storing
   * it would keep the stale decision until the next invalidation, and returning it would authorize
   * one more sample. The invalidation says so, and the cache asks again.
   */
  @Test
  void aCheckOvertakenByAnInvalidationIsMadeAgain() throws Exception {
    var checkStarted = new CountDownLatch(1);
    var releaseCheck = new CountDownLatch(1);
    var checks = new AtomicInteger();

    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenAnswer(
            invocation -> {
              if (checks.getAndIncrement() == 0) {
                // The first check is overtaken: access is revoked while it is in flight.
                checkStarted.countDown();
                assertTrue(releaseCheck.await(5, TimeUnit.SECONDS));
                return Map.of(a, AccessResult.ALLOWED);
              }
              return Map.of(a, AccessResult.DENIED_USER_ACCESS);
            });

    ExecutorService pool = Executors.newSingleThreadExecutor();
    try {
      Future<Map<ReadValueId, AccessResult>> check =
          pool.submit(() -> cache.getOrCheck(session, List.of(a)));
      assertTrue(checkStarted.await(5, TimeUnit.SECONDS));

      cache.invalidate(ReadAccessScope.node(a.getNodeId()));
      releaseCheck.countDown();

      assertEquals(
          Map.of(a, AccessResult.DENIED_USER_ACCESS),
          check.get(5, TimeUnit.SECONDS),
          "the caller gets the answer from after the invalidation");
      assertEquals(2, checks.get(), "the overtaken check was made once more");
      assertEquals(
          Optional.of(AccessResult.DENIED_USER_ACCESS), cache.get(session, a), "and it is cached");
    } finally {
      pool.shutdownNow();
    }
  }

  // Invalidations that keep arriving must not keep the check going forever: after a few attempts
  // the last answers are returned without being stored, no staler than a check one cycle earlier.
  @Test
  void aCheckOvertakenRepeatedlyGivesUpWithTheLastAnswersUnstored() {
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenAnswer(
            invocation -> {
              cache.invalidate(ReadAccessScope.node(a.getNodeId()));
              return Map.of(a, AccessResult.ALLOWED);
            });

    assertEquals(Map.of(a, AccessResult.ALLOWED), cache.getOrCheck(session, List.of(a)));

    verify(accessController, times(3)).checkReadAccess(eq(session), anyList());
    assertEquals(Optional.empty(), cache.get(session, a), "nothing overtaken is cached");
  }

  private static Map<ReadValueId, AccessResult> allowAll(List<ReadValueId> readValueIds) {
    return readValueIds.stream()
        .collect(java.util.stream.Collectors.toMap(id -> id, id -> AccessResult.ALLOWED));
  }

  private static ReadValueId readValueId(NodeId nodeId) {
    return new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);
  }
}
