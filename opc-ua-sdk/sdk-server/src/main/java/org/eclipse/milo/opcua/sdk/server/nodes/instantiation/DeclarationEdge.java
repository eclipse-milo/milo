/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * One hierarchical edge of a {@link TypeInstantiationModel}: a forward hierarchical reference from
 * the declaration (or type root) at {@code parentPath} to the declaration at {@code childPath}.
 *
 * <p>Edges are kept separate from {@link InstanceDeclaration}s because one parent/child pair may be
 * connected by several references — each with its own ReferenceType — and all of them must map onto
 * the same realized instance pair (Part 3 §6.4.3). Edges are a logical set: duplicate (parent,
 * referenceType, child) rows produced by merging are canonicalized to one edge (Part 3 §4.4.4).
 *
 * @param parentPath the parent declaration's path; {@link BrowsePath#root()} for direct children of
 *     the type.
 * @param referenceTypeId the hierarchical ReferenceType connecting the pair.
 * @param childPath the child declaration's path.
 */
public record DeclarationEdge(
    BrowsePath parentPath, NodeId referenceTypeId, BrowsePath childPath) {}
