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
 *
 * <h2>Remote and unreadable targets</h2>
 *
 * <p>Browse resolves and normalizes each reference target once against the server's {@link
 * org.eclipse.milo.opcua.stack.core.ServerTable} and {@link
 * org.eclipse.milo.opcua.stack.core.NamespaceTable} before reading attributes, applying {@code
 * nodeClassMask}, or checking browse access. A target addressed by this server's own URI (index
 * {@code 0}) is normalized to local form and subjected to the same local reads, filters, and {@link
 * org.eclipse.milo.opcua.sdk.server.access.AccessController#checkBrowseAccess} checks as {@code
 * serverIndex == 0}. Unrepresentable targets (unregistered remote server URIs or server indices,
 * remote targets with unmapped numeric namespace indices, or local targets with unregistered
 * namespace URIs or unregistered numeric namespace indices) are logged and skipped rather than
 * emitting malformed {@code ReferenceDescription}s or aborting binary encoding. Valid remote
 * targets skip local reads and local browse-access checks and fall back to {@code
 * QualifiedName.NULL_VALUE}, {@code LocalizedText.NULL_VALUE}, and {@code NodeClass.Unspecified}.
 * Per OPC 10000-4 §5.9.2.2 Table 34, a zero {@code nodeClassMask} returns all targets, whereas a
 * non-zero mask ignores unknown {@code NodeClass} for remote targets only and excludes local
 * targets whose {@code NodeClass} is unknown.
 */
package org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers;
