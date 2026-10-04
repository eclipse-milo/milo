/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * Sampling of data MonitoredItems: interval groups, the sampling cycle, and the read access refresh
 * that runs as a step of it.
 *
 * <p>An OPC UA server has to produce a value for every data MonitoredItem at its sampling interval
 * and, by Part 4 §5.13.2.1, has to report a denial in the Publish response when the item's Session
 * may not read it, including when that changes after the item was created. This package owns the
 * first and supplies the decisions for the second. Three layers share the work:
 *
 * <ul>
 *   <li>The SDK core, in {@link org.eclipse.milo.opcua.sdk.server.subscriptions} and {@link
 *       org.eclipse.milo.opcua.sdk.server.items}, creates the items, seeds each with its
 *       create-time read access result, enforces the most recent result in {@link
 *       org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem#setValue}, and publishes.
 *   <li>This package decides when items are sampled and when their read access result is refreshed.
 *       It is the only place on the sampling path that asks the server's {@code AccessController}.
 *   <li>The AddressSpace decides how a value is read. It never sees access control.
 * </ul>
 *
 * <h2>Data flow</h2>
 *
 * <p>An AddressSpace forwards its data item callbacks to a {@link
 * org.eclipse.milo.opcua.sdk.server.sampling.SamplingManager}, one per AddressSpace. The manager
 * buckets items by interval and keeps one {@link
 * org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroup} per interval, created by a {@link
 * org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroupFactory}. Each cycle a group tells its
 * subclass about a changed item set, refreshes every item's read access result with the manager's
 * {@link org.eclipse.milo.opcua.sdk.server.sampling.ReadAccessPolicy}, and hands the items whose
 * Session may read them to the subclass's {@code sample}, which reads them however the protocol
 * allows and delivers each value with {@link
 * org.eclipse.milo.opcua.sdk.server.items.DataItem#setValue}. The same refresh precedes the initial
 * sample of a new or re-enabled item. {@link
 * org.eclipse.milo.opcua.sdk.server.sampling.AddressSpaceSamplingGroup} is the subclass that reads
 * through {@link org.eclipse.milo.opcua.sdk.server.AddressSpace#read}; a custom sampler subclasses
 * {@code SamplingGroup} and implements only the read.
 *
 * <h2>Read access</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.sampling.ReadAccessPolicy#perCycle()}, the default,
 * asks the {@code AccessController} once per Session per refresh and needs nothing from the
 * application. {@link org.eclipse.milo.opcua.sdk.server.sampling.ReadAccessPolicy#cached()} answers
 * from the server-wide {@link org.eclipse.milo.opcua.sdk.server.access.ReadAccessCache}, keyed by
 * Session, Node, and Attribute, and checks only misses. Nothing in the server observes every input
 * of an access decision, so a cached answer stays until an invalidation drops it: {@link
 * org.eclipse.milo.opcua.sdk.server.access.AccessControlManager#invalidateReadAccess} takes a
 * {@link org.eclipse.milo.opcua.sdk.server.access.ReadAccessScope}, drops the matching entries, and
 * tells every {@link org.eclipse.milo.opcua.sdk.server.access.ReadAccessListener}, for components
 * that refresh results on their own. The SDK invalidates for a Session whose identity or endpoint
 * changed and for the Nodes of a transferred Subscription; an application on the cached policy
 * invalidates for its own security configuration changes.
 *
 * <h2>Invariants</h2>
 *
 * <ul>
 *   <li>Every path into {@code sample} is preceded by a refresh of the items it receives, and only
 *       items whose Session may read them are passed.
 *   <li>A Session is resolved from the item on every refresh and every sample, never kept by a
 *       group between turns, and a check whose item moved to another Session, or whose Session's
 *       identity or endpoint changed, while it ran is not applied, so an item is checked and read
 *       for its Session as it is now.
 *   <li>A group runs one refresh-and-sample at a time, holding its turn until the sample's stage
 *       completes, and applies a check's results only to items still in the group, so no two checks
 *       for an item are in flight and a result from before an item moved to another group cannot
 *       land over the other group's newer one.
 *   <li>A check that fails or returns no decision leaves an item's last result in place: a fault is
 *       not an access decision, and a sampler that stops refreshing is stale, never leaky.
 *   <li>An exception from a sample or a refresh is logged and the group schedules its next cycle
 *       all the same.
 * </ul>
 *
 * <h2>Runtime boundaries</h2>
 *
 * <p>Groups schedule on the server's scheduled executor and run on its executor. The sample is a
 * {@link java.util.concurrent.CompletionStage} so a protocol that delivers asynchronously can keep
 * its own execution model; the next cycle is timed from the stage's completion. The cache is safe
 * to use from any thread; invalidations run on the caller's thread and hold the cache's write lock
 * while they scan, so a scope's predicate must be cheap, and listeners are called synchronously on
 * the same thread.
 *
 * <h2>Extension points</h2>
 *
 * <p>Subclass {@code SamplingGroup} for a protocol that reads items itself, and give the manager a
 * factory that creates it. Use {@code SamplingManagerConfig} for the bucket size, the minimum
 * interval a revised interval of zero becomes, the initial sample debounce, and the policy. A
 * server whose AddressSpaces sample without the framework keeps results current with {@link
 * org.eclipse.milo.opcua.sdk.server.DataItemListener} and a {@code ReadAccessListener} instead.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.sampling;

import org.jspecify.annotations.NullMarked;
