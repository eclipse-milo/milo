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
 * Lifecycle owners for the standard server diagnostics Objects beneath Server.ServerDiagnostics.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.diagnostics.objects.ServerDiagnosticsObject} is the
 * entry point. The server namespace starts it once and it starts the summary Variable, the
 * server-wide SubscriptionDiagnosticsArray, and {@link
 * org.eclipse.milo.opcua.sdk.server.diagnostics.objects.SessionsDiagnosticsSummaryObject}. The
 * summary Object in turn owns the two Session arrays and one {@link
 * org.eclipse.milo.opcua.sdk.server.diagnostics.objects.SessionDiagnosticsObject} per Session, each
 * holding that Session's diagnostics Variables and its own SubscriptionDiagnosticsArray. The
 * Variable and array components themselves live in {@code
 * org.eclipse.milo.opcua.sdk.server.diagnostics.variables}.
 *
 * <h2>EnabledFlag</h2>
 *
 * <p>The ServerDiagnostics EnabledFlag Property gates everything dynamic. Static diagnostics
 * Variables stay in the address space while the flag is off. Their access levels still allow
 * reading, and a Value read answers Bad_OutOfService until the flag turns on. Dynamic nodes,
 * meaning per-Session Objects and array elements, exist only while the flag is on: turning it on
 * creates them for the Sessions and Subscriptions that exist at that moment, and turning it off
 * removes them. Each owner observes the flag node directly, so a Write to the flag takes effect
 * before the Write returns.
 *
 * <h2>Threads and locks</h2>
 *
 * <p>Three kinds of threads mutate this state. The EnabledFlag observer runs on the writer's thread
 * while the flag node's monitor is held. Session listener callbacks run one at a time on the
 * SessionManager's listener queue and can arrive after the flag has changed. Subscription events
 * arrive on the thread that created or deleted the Subscription. Starting or stopping any
 * diagnostics component reads the flag and registers or removes its observer, which takes the flag
 * node's monitor. The lock order is therefore the flag node's monitor first, then any private lock:
 * the summary Object acquires the monitor at the start of every listener callback and of its own
 * startup and shutdown, so a flag Write and a callback never run interleaved, and the Objects a
 * Write created or removed are final when the Write returns.
 *
 * <h2>Extension guidance</h2>
 *
 * <p>Add new dynamic diagnostics nodes as components owned by one of these Objects, gate them on
 * the flag the same way, and keep per-Session authorization in the Variables' attribute filters
 * rather than in the lifecycle owners.
 */
package org.eclipse.milo.opcua.sdk.server.diagnostics.objects;
