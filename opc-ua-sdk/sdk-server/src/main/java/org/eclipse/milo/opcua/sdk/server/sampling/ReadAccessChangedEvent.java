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

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;

/**
 * Says that the read access answers in {@link #scope()} may have changed.
 *
 * <p>{@link OpcUaServer#invalidateReadAccess(ReadAccessScope)} posts this on the server's internal
 * EventBus after the server's {@link ReadAccessCache} has dropped the entries the scope covers, so
 * a subscriber that re-checks items in response sees the cache miss and fill with current answers.
 * The SDK posts it when a Session's identity or endpoint changes and when a Subscription is
 * transferred; an application posts it, through {@code invalidateReadAccess}, when something it
 * knows about changes an answer, such as a role mapping or a per-Session attribute filter.
 *
 * <p>Subscribe with a Guava {@code @Subscribe} method registered on {@link
 * OpcUaServer#getInternalEventBus()}. The event is delivered on the thread that called {@code
 * invalidateReadAccess}, so subscribers should return quickly. Post through {@code
 * invalidateReadAccess} rather than directly, or the cache keeps its stale entries.
 *
 * @param scope the Sessions and Nodes whose answers may have changed.
 */
public record ReadAccessChangedEvent(ReadAccessScope scope) {}
