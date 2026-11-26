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
 * Helpers that implement the View Service Set operations behind {@link
 * org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultViewServiceSet}.
 *
 * <h2>Responsibilities</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers.BrowseHelper} validates
 * starting Nodes and filters, queries {@link
 * org.eclipse.milo.opcua.sdk.server.AddressSpaceManager}, enforces target browse access through the
 * {@link org.eclipse.milo.opcua.sdk.server.access.AccessController}, and paginates results into
 * per- {@link org.eclipse.milo.opcua.sdk.server.Session} browse continuation points. {@link
 * org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers.BrowsePathsHelper} translates relative
 * paths under the same access-control rules. {@link
 * org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers.BrowseUtil} normalizes target {@link
 * org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId}s to relative form for local Nodes
 * and absolute URI form for remote Nodes.
 *
 * <h2>Continuation point ownership</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers.BrowseHelper} commits all
 * continuation points required by a single Browse request to the {@link
 * org.eclipse.milo.opcua.sdk.server.Session} in one atomic call so prior-request reclamation,
 * single-request {@code Bad_NoContinuationPoints} enforcement, and concurrent {@code BrowseNext}
 * slot reuse share one lock.
 */
package org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers;
