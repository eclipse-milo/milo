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
 * closes active channels owned by that target.
 *
 * <h2>Failure handling</h2>
 *
 * <p>Attempt failures are translated into immutable {@link
 * org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectAttemptEvent}s and retained on target
 * snapshots as the last status or a defensive copy of the last exception. Retry timing is delegated
 * to {@link org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectRetryPolicy}; the default
 * policy uses the target registration period.
 *
 * <p>Successful outbound UA-TCP connections are handed back to the normal server SecureChannel and
 * Session paths after the stack transport installs the standard server Hello handler. Client-side
 * reverse-listener concerns live in the client SDK package.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.reverse;

import org.jspecify.annotations.NullMarked;
