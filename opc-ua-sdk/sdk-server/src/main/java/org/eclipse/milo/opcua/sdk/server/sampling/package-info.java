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
 * Read access decisions for sampled data MonitoredItems.
 *
 * <p>Part 4 §5.13.2.1 requires a data MonitoredItem the Session may not read to report the denial
 * in the Publish response, including when access rights change after the item was created. The
 * {@link org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem} enforces the most recent
 * decision it was given; this package supplies the decisions.
 *
 * <h2>Cache and invalidation</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.sampling.ReadAccessCache} is server-wide, owned by
 * {@link org.eclipse.milo.opcua.sdk.server.OpcUaServer}, and keyed by Session, Node, and Attribute.
 * A miss is answered by the server's {@code AccessController}, which reads the Node's access
 * control attributes through the AddressSpace; a hit is a map lookup. Entries stay until an
 * invalidation drops them or their Session closes.
 *
 * <p>Nothing in the server observes every input of an access decision, so an invalidation is a
 * message from whoever does know: {@link
 * org.eclipse.milo.opcua.sdk.server.OpcUaServer#invalidateReadAccess} takes a {@link
 * org.eclipse.milo.opcua.sdk.server.sampling.ReadAccessScope} naming the Sessions and Nodes
 * concerned, drops the matching entries, and posts a {@link
 * org.eclipse.milo.opcua.sdk.server.sampling.ReadAccessChangedEvent} on the internal EventBus for
 * components that refresh results on their own. The SDK posts for a Session whose identity or
 * endpoint changed and for the Nodes of a transferred Subscription; an application posts for its
 * own security configuration changes.
 *
 * <h2>Runtime boundaries</h2>
 *
 * <p>The cache is safe to use from any thread. Invalidations run on the caller's thread and hold
 * the cache's write lock while they scan, so a scope's predicate must be cheap. The event is
 * delivered synchronously on the same thread.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.sampling;

import org.jspecify.annotations.NullMarked;
