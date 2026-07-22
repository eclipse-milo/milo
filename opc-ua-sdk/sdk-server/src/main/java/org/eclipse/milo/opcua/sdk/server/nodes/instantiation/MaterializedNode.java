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

import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * One node an apply realized, with the provenance that makes cleanup ownership-safe: {@link
 * InstantiationResult#deleteCreated()} removes {@link Provenance#CREATED} resources only — a node
 * that was adopted or shared is never this instantiation's to delete.
 *
 * @param browsePath the occurrence path; {@link BrowsePath#root()} for the instance root.
 * @param node the realized node.
 * @param provenance how the node was realized.
 */
public record MaterializedNode(BrowsePath browsePath, UaNode node, Provenance provenance) {

  /**
   * @return the realized node's {@link NodeId}.
   */
  public NodeId nodeId() {
    return node.getNodeId();
  }

  /** How a {@link MaterializedNode} was realized. */
  public enum Provenance {

    /** A new node was constructed and committed; owned by this instantiation. */
    CREATED,

    /**
     * A compatible existing node in the target was adopted ({@link
     * ConflictPolicy#REUSE_COMPATIBLE}); never modified, never deleted by cleanup.
     */
    REUSED,

    /**
     * The type's own Method node serves this instance ({@link MethodInstantiation#SHARE}); nothing
     * was constructed and cleanup never touches it.
     */
    SHARED
  }
}
