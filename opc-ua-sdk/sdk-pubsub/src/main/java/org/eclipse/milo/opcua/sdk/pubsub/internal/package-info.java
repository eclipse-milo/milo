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
 * PubSub engine internals: the {@code PubSubService} implementation, per-component runtimes and the
 * Part 14 state machine, publish scheduling and keep-alive, reader matching and dispatch, the
 * metadata cache, diagnostics collection, and the reconfigure diff. Everything in this package is
 * implementation detail and not part of the public API; it may change without notice.
 *
 * <p>Publishers check transport readiness before encoding or consuming data and events. Service
 * startup activates the component tree without waiting for broker availability. The connection's
 * initial transport-up callback triggers deferred retained metadata and status publication and
 * wakes event-triggered writer groups. Later outages and recovery use the normal state cascade to
 * pause and reactivate writers. Closing channels resets transport edge tracking for their next
 * activation. Queued transport callbacks are checked against the current channel lifetime under the
 * engine lock, so a closed session cannot change the replacement session's state.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.internal;

import org.jspecify.annotations.NullMarked;
