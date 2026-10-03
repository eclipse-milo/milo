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

import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.access.ReadAccessCache;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;

/**
 * Decides, before a group samples, which of its items the items' Sessions may read.
 *
 * <p>A {@link SamplingGroup} calls {@link #check} on the items it is about to sample, on every
 * cycle and before every initial sample, applies each result with {@link
 * DataItem#setReadAccessResult} to the items still in the group, and then samples only the items
 * whose result allows it. The item enforces the result whatever the sampler delivers afterwards.
 *
 * <p>{@link #perCycle()} asks the server's {@code AccessController} once per Session per check and
 * is correct without any help from the application. {@link #cached()} answers from the server's
 * {@link ReadAccessCache} and is only as current as the last {@link
 * org.eclipse.milo.opcua.sdk.server.access.AccessControlManager#invalidateReadAccess}; choose it
 * for a namespace that knows when its answers change.
 */
public interface ReadAccessPolicy {

  /**
   * Check, for each of {@code items}, whether its current Session may read it.
   *
   * <p>Items are grouped by {@link DataItem#getSession()} as it is at the time of the call, so an
   * item transferred to another Session is checked for that Session. The returned map holds a
   * result for every item a check answered for. Items whose Session has closed, and items of a
   * Session whose check failed, are absent so that they keep their last result; an answer that is
   * not a decision, {@link AccessResult#NODE_UNKNOWN}, is returned as it is and leaves the last
   * result in place when applied.
   *
   * <p>The caller applies the results. A {@link SamplingGroup} applies them only to items still in
   * the group, so a result from a check that started before an item moved to another group never
   * lands over the other group's newer result.
   *
   * @param server the server whose {@code AccessController} and {@link ReadAccessCache} to use.
   * @param items the items about to be sampled.
   * @return the result for each item a check answered for.
   */
  Map<DataItem, AccessResult> check(OpcUaServer server, List<DataItem> items);

  /**
   * A policy that checks every item on every call: one {@code AccessController.checkReadAccess} per
   * Session, which for the default controller and Value items reads four attributes per distinct
   * Node.
   *
   * <p>This is the default. It costs what the Read service costs and needs no invalidation.
   *
   * @return the per-cycle policy.
   */
  static ReadAccessPolicy perCycle() {
    return PerCycleReadAccessPolicy.INSTANCE;
  }

  /**
   * A policy that answers from the server's {@link ReadAccessCache}, checking only the misses.
   *
   * <p>A hit costs a map lookup. The answers stay until {@link
   * org.eclipse.milo.opcua.sdk.server.access.AccessControlManager#invalidateReadAccess} drops them,
   * so a namespace on this policy must invalidate when its security configuration changes; the SDK
   * invalidates for Session identity and endpoint changes and for transfers. A stale allowed entry
   * exposes no more than the per-cycle policy did one cycle earlier, and a stale denied entry only
   * withholds data.
   *
   * @return the cached policy.
   */
  static ReadAccessPolicy cached() {
    return CachedReadAccessPolicy.INSTANCE;
  }
}
