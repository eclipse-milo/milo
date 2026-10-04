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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessControlManager;
import org.eclipse.milo.opcua.sdk.server.access.AccessController;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.access.ReadAccessCache;
import org.eclipse.milo.opcua.sdk.server.access.ReadAccessScope;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** What the two built-in {@link ReadAccessPolicy} implementations apply to items. */
class ReadAccessPolicyTest {

  private final OpcUaServer server = mock(OpcUaServer.class);
  private final AccessController accessController = mock(AccessController.class);
  private final Session session = mock(Session.class);
  private final Session otherSession = mock(Session.class);

  private MonitoredDataItem a;
  private MonitoredDataItem b;
  private MonitoredDataItem c;

  @BeforeEach
  void setUp() {
    when(server.getAccessController()).thenReturn(accessController);

    a = SamplingTestItems.item(server, session, "a");
    b = SamplingTestItems.item(server, session, "b");
    c = SamplingTestItems.item(server, otherSession, "c");
  }

  /** What a group does with a check: apply every result it returned. */
  private void refresh(ReadAccessPolicy policy, List<MonitoredDataItem> items) {
    Map<DataItem, AccessResult> results = policy.check(server, List.copyOf(items));

    results.forEach(DataItem::setReadAccessResult);
  }

  @Nested
  class PerCycle {

    private final ReadAccessPolicy policy = ReadAccessPolicy.perCycle();

    // Items of one group can belong to different Sessions. Each must be checked for its own
    // Session, not the first item's.
    @Test
    void checksEachSessionsItemsForThatSession() {
      when(accessController.checkReadAccess(eq(session), anyList()))
          .thenReturn(Map.of(a.getReadValueId(), AccessResult.ALLOWED));
      when(accessController.checkReadAccess(eq(otherSession), anyList()))
          .thenReturn(Map.of(c.getReadValueId(), AccessResult.DENIED_USER_ACCESS));

      refresh(policy, List.of(a, c));

      verify(accessController).checkReadAccess(session, List.of(a.getReadValueId()));
      verify(accessController).checkReadAccess(otherSession, List.of(c.getReadValueId()));
      assertEquals(AccessResult.ALLOWED, a.getReadAccessResult());
      assertEquals(AccessResult.DENIED_USER_ACCESS, c.getReadAccessResult());
    }

    // A closed Session keeps its items until its Subscriptions expire or are transferred. Checking
    // for it is wasted work, and the result would be for a Session nothing can read through.
    @Test
    void skipsItemsWhoseSessionHasClosed() {
      when(otherSession.isClosed()).thenReturn(true);
      when(accessController.checkReadAccess(eq(session), anyList()))
          .thenReturn(Map.of(a.getReadValueId(), AccessResult.ALLOWED));

      refresh(policy, List.of(a, c));

      verify(accessController, never()).checkReadAccess(eq(otherSession), anyList());
    }

    // A fault in one Session's check is not an access decision for its items, and must not keep
    // the other Sessions' items from being checked.
    @Test
    void aFailedCheckKeepsTheLastResultAndStillChecksOtherSessions() {
      a.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
      when(accessController.checkReadAccess(eq(session), anyList()))
          .thenThrow(new IllegalStateException("address space unavailable"));
      when(accessController.checkReadAccess(eq(otherSession), anyList()))
          .thenReturn(Map.of(c.getReadValueId(), AccessResult.DENIED_NOT_READABLE));

      refresh(policy, List.of(a, c));

      assertEquals(AccessResult.DENIED_USER_ACCESS, a.getReadAccessResult());
      assertEquals(AccessResult.DENIED_NOT_READABLE, c.getReadAccessResult());
    }

    // A Node the AddressSpace no longer knows is no decision; the item keeps enforcing what it had.
    @Test
    void noDecisionLeavesTheLastResultInPlace() {
      a.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
      when(accessController.checkReadAccess(eq(session), anyList()))
          .thenReturn(Map.of(a.getReadValueId(), AccessResult.NODE_UNKNOWN));

      refresh(policy, List.of(a));

      assertEquals(AccessResult.DENIED_USER_ACCESS, a.getReadAccessResult());
    }
  }

  @Nested
  class Cached {

    private final ReadAccessPolicy policy = ReadAccessPolicy.cached();

    @BeforeEach
    void setUp() {
      AccessControlManager accessControlManager = mock(AccessControlManager.class);
      when(accessControlManager.getReadAccessCache())
          .thenReturn(new ReadAccessCache(accessController));
      when(server.getAccessControlManager()).thenReturn(accessControlManager);
    }

    @Test
    void aHitIsAppliedWithoutAnotherCheck() {
      when(accessController.checkReadAccess(eq(session), anyList()))
          .thenReturn(
              Map.of(
                  a.getReadValueId(),
                  AccessResult.ALLOWED,
                  b.getReadValueId(),
                  AccessResult.DENIED_USER_ACCESS));

      refresh(policy, List.of(a, b));
      refresh(policy, List.of(a, b));

      verify(accessController, times(1)).checkReadAccess(eq(session), anyList());
      assertEquals(AccessResult.ALLOWED, a.getReadAccessResult());
      assertEquals(AccessResult.DENIED_USER_ACCESS, b.getReadAccessResult());
    }

    /**
     * The cost of the cached policy is staleness: a revoked right stays allowed until something
     * posts an invalidation, and is reported as a denial on the first refresh after it.
     */
    @Test
    void aStaleAllowedResultStaysUntilInvalidated() {
      when(accessController.checkReadAccess(eq(session), anyList()))
          .thenReturn(Map.of(a.getReadValueId(), AccessResult.ALLOWED));
      refresh(policy, List.of(a));
      a.setValue(new DataValue(new org.eclipse.milo.opcua.stack.core.types.builtin.Variant(1)));
      SamplingTestItems.drain(a);

      when(accessController.checkReadAccess(eq(session), anyList()))
          .thenReturn(Map.of(a.getReadValueId(), AccessResult.DENIED_USER_ACCESS));
      refresh(policy, List.of(a));

      assertEquals(AccessResult.ALLOWED, a.getReadAccessResult(), "stale until invalidated");
      assertTrue(SamplingTestItems.drain(a).isEmpty());

      server
          .getAccessControlManager()
          .getReadAccessCache()
          .invalidate(ReadAccessScope.node(a.getReadValueId().getNodeId()));
      refresh(policy, List.of(a));

      assertEquals(AccessResult.DENIED_USER_ACCESS, a.getReadAccessResult());
      List<DataValue> queued = SamplingTestItems.drain(a);
      assertEquals(1, queued.size());
      assertEquals(StatusCodes.Bad_UserAccessDenied, queued.get(0).statusCode().getValue());
    }

    // The cache is keyed by Session, so an item transferred to another Session misses and is
    // checked for that Session, without any invalidation.
    @Test
    void aTransferredItemIsCheckedForItsNewSession() {
      when(accessController.checkReadAccess(eq(session), anyList()))
          .thenReturn(Map.of(a.getReadValueId(), AccessResult.ALLOWED));
      when(accessController.checkReadAccess(eq(otherSession), anyList()))
          .thenReturn(Map.of(a.getReadValueId(), AccessResult.DENIED_USER_ACCESS));
      refresh(policy, List.of(a));

      a.setSession(otherSession);
      refresh(policy, List.of(a));

      verify(accessController).checkReadAccess(otherSession, List.of(a.getReadValueId()));
      assertEquals(AccessResult.DENIED_USER_ACCESS, a.getReadAccessResult());
    }
  }
}
