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
 * SDK-level server-side Reverse Connect target lifecycle support.
 *
 * <p>The package owns the model used by {@link org.eclipse.milo.opcua.sdk.server.OpcUaServer} to
 * open reverse UA-TCP connections toward client listeners. It sits above the low-level stack
 * transport primitive: targets describe <em>what</em> should be dialed and when, while the
 * transport package owns the actual TCP connect, {@code ReverseHello}, and server UASC handoff.
 *
 * <h2>Main entry points</h2>
 *
 * <ul>
 *   <li>{@link org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget} describes where a
 *       client reverse listener is reachable and which server endpoint is advertised in {@code
 *       ReverseHello}.
 *   <li>{@link org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetManager} validates
 *       targets, schedules attempts, applies retry policy, and tracks active reverse-opened
 *       channels.
 *   <li>{@link org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetHandle}, {@link
 *       org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetSnapshot}, and {@link
 *       org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetListener} expose runtime
 *       control and observability to applications.
 * </ul>
 *
 * <h2>Lifecycle and ownership</h2>
 *
 * <p>Targets can be configured before server startup or added at runtime. Enabled, unpaused targets
 * are validated against the server's current {@code opc.tcp} endpoint descriptions before attempts
 * are scheduled. Each target owns its scheduled retry task, at most one in-flight attempt, and the
 * active reverse-opened channels associated with successful handoffs.
 *
 * <p>The target lifecycle is: registered, optionally enabled and unpaused, scheduled, in-flight,
 * handed off, active channel, retry, update/remove, and shutdown. Disabled targets remain
 * registered but are not scheduled. Paused targets remain observable and can be resumed later.
 * Scheduling creates one future attempt time; when it fires, the manager starts one low-level
 * transport attempt and marks the target in flight. A successful transport handoff moves the
 * channel into the normal server UASC path and increments the target's active channel count. A
 * failed attempt or later active-channel close asks the target's retry policy for the next delay
 * before scheduling again.
 *
 * <p>Updating a target replaces the future-attempt configuration, including enabled and paused
 * state, without closing reverse-opened channels that have already been handed to the normal server
 * path. Removing a target is stronger: it cancels scheduled work, closes an in-flight attempt, and
 * closes active channels owned by that target. Asynchronous callbacks belong to one registration
 * instance; reusing a removed target's UUID creates a separate owner. Late handoffs from the
 * removed registration close their channels, and stale timers or retry callbacks cannot change its
 * replacement.
 *
 * <h2>Runtime boundaries</h2>
 *
 * <p>The manager borrows two execution resources from the server configuration and owns neither.
 * The scheduled executor is a timer only: its callbacks decide whether a fired timer is still
 * current and hand the work to the server executor. The server executor, the one configured with
 * {@code OpcUaServerConfigBuilder.setExecutor(...)}, runs everything that can block or that
 * application code controls: client listener hostname resolution and transport startup for an
 * attempt, custom retry-policy evaluation, and listener callbacks. Listener callbacks additionally
 * pass through a serialized queue so they are observed in order; attempt and retry work never runs
 * through that queue. Transport attempt events and channel-close events arrive on Netty threads and
 * only update bookkeeping and hand off before returning.
 *
 * <p>Because a timer can fire before a pause, update, removal, or shutdown takes effect, cancelling
 * the timer is not enough to discard work already queued on the executor. Executor tasks therefore
 * revalidate the owning registration, its generation, and lifecycle state when they begin, and a
 * transport attempt that outruns one of those operations is closed rather than adopted. Because the
 * server executor is shared with service dispatch, applications may size it or supply a
 * virtual-thread-per-task executor without any reverse-connect specific configuration.
 *
 * <h2>Failure handling</h2>
 *
 * <p>Attempt failures are translated into immutable {@link
 * org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectAttemptEvent}s and retained on target
 * snapshots as the last status or a defensive copy of the last exception. Retry timing is delegated
 * to {@link org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectRetryPolicy}; the default
 * policy uses the target registration period. Listener dispatch uses a serial execution queue;
 * executor rejection runs callbacks on the submitting thread so notification failures cannot
 * abandon an attempt transition. Callbacks should return promptly even during executor overload.
 *
 * <p>If the server executor rejects attempt startup, the attempt is reported as {@code FAILED} with
 * {@code Bad_ResourceUnavailable} and the rejection as its cause. If it rejects retry-policy
 * evaluation, the terminal event is still delivered. In both cases the target is rescheduled after
 * its registration period without consulting the custom retry policy, so a saturated executor
 * delays reconnection but never leaves a target stranded, marked in progress, or silently dropped.
 *
 * <p>Successful outbound UA-TCP connections are handed back to the normal server SecureChannel and
 * Session paths after the stack transport installs the standard server Hello handler. Client-side
 * reverse-listener concerns live in the client SDK package.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.reverse;

import org.jspecify.annotations.NullMarked;
