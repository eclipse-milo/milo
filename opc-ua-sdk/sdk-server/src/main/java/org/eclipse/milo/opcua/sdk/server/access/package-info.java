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
 * Authorization: whether a Session may read, write, browse, call, or manage a Node, and how a read
 * decision handed to a MonitoredItem stays current.
 *
 * <p>The {@link org.eclipse.milo.opcua.sdk.server.access.AccessController} decides. Every service
 * implementation asks it before touching an AddressSpace: Read and Write per attribute, Browse per
 * Node, Call per Object and Method, and the node-management services per source Node. {@link
 * org.eclipse.milo.opcua.sdk.server.access.DefaultAccessController} decides from the Node's access
 * attributes as read for the requesting Session, so an application shapes its answers through
 * attribute filters that supply {@code UserAccessLevel}, {@code UserWriteMask}, {@code
 * UserExecutable}, and {@code UserRolePermissions}, and through the server's {@code RoleMapper}. An
 * application whose rules go beyond attributes supplies its own controller through {@link
 * org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigBuilder#setAccessControllerFactory}.
 *
 * <h2>Read access for MonitoredItems</h2>
 *
 * <p>A data MonitoredItem carries the read access result it was created with and enforces it on
 * every value (Part 4 §5.13.2.1), so something has to refresh that result as permissions change. A
 * refresher asks the controller directly or, to spare a controller call per re-check, the {@link
 * org.eclipse.milo.opcua.sdk.server.access.ReadAccessCache}. The cache is keyed by Session, Node,
 * and Attribute, keeps grants and denials alike, and has no time-to-live: an entry stays until
 * {@link org.eclipse.milo.opcua.sdk.server.access.AccessControlManager#invalidateReadAccess} drops
 * it with a {@link org.eclipse.milo.opcua.sdk.server.access.ReadAccessScope} or the Session closes.
 * After dropping entries the manager tells every {@link
 * org.eclipse.milo.opcua.sdk.server.access.ReadAccessListener}, for components that refresh results
 * on their own.
 *
 * <h2>Ownership</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.access.AccessControlManager}, one per {@link
 * org.eclipse.milo.opcua.sdk.server.OpcUaServer}, owns the controller, the cache, and the
 * listeners. The SDK invalidates, and re-checks the affected data items, when a Session's identity
 * or endpoint changes and when a Subscription is transferred; the application invalidates for its
 * own changes, after committing them.
 *
 * <h2>Runtime boundaries</h2>
 *
 * <p>The controller runs on service request threads and on the threads of components that re-check
 * access. Its attribute reads go through the Node filter chain, so a filter that supplies an access
 * attribute must be quick and thread-safe and must not ask the controller in turn. The cache is
 * safe from any thread; an invalidation holds its write lock while it scans, so a scope's predicate
 * must be cheap, and listeners run synchronously on the invalidating thread.
 *
 * <h2>Boundaries with other packages</h2>
 *
 * <p>{@code items} owns the gate that enforces a result. This package never reads values and never
 * stores a result on an item.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.access;

import org.jspecify.annotations.NullMarked;
