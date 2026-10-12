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

import static java.util.Objects.requireNonNull;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * One non-hierarchy reference row of a {@link TypeInstantiationModel}, recorded from a declaration
 * node. Rows cover everything the walk's {@link DeclarationEdge}s do not: non-hierarchical
 * references in both directions, including {@code HasTypeDefinition} and {@code HasModellingRule}
 * (retention of the latter on instances is a purpose-dependent planning decision, not a model one).
 *
 * <p>A row is either <b>relative</b> — both ends are declarations of the model, the target
 * identified by {@link #targetPath}, canonicalized to forward direction — or <b>absolute</b> — the
 * target is outside the model, kept verbatim as {@link #targetNodeId} with the recorded direction.
 * Relative rows are the precondition for re-mapping declaration-internal references onto instances;
 * absolute rows are copied or dropped per the reference replication policy at plan time.
 *
 * @param sourcePath the path of the declaration the row was recorded from (relative rows: the
 *     forward source).
 * @param referenceTypeId the ReferenceType of the row.
 * @param direction the row's direction; always {@link Reference.Direction#FORWARD} for relative
 *     rows.
 * @param targetPath the target declaration's path; non-null iff the row is relative.
 * @param targetNodeId the absolute target; non-null iff the row is absolute.
 */
public record ReferenceRow(
    BrowsePath sourcePath,
    NodeId referenceTypeId,
    Reference.Direction direction,
    @Nullable BrowsePath targetPath,
    @Nullable ExpandedNodeId targetNodeId) {

  public ReferenceRow {
    if ((targetPath == null) == (targetNodeId == null)) {
      throw new IllegalArgumentException("exactly one of targetPath / targetNodeId must be set");
    }
  }

  /**
   * Create a relative row: both ends are declarations of the model; direction is canonicalized to
   * forward.
   *
   * @param sourcePath the forward source declaration's path.
   * @param referenceTypeId the ReferenceType.
   * @param targetPath the forward target declaration's path.
   * @return a relative {@link ReferenceRow}.
   */
  public static ReferenceRow relative(
      BrowsePath sourcePath, NodeId referenceTypeId, BrowsePath targetPath) {

    return new ReferenceRow(
        sourcePath, referenceTypeId, Reference.Direction.FORWARD, targetPath, null);
  }

  /**
   * Create an absolute row: the target is outside the model and kept verbatim.
   *
   * @param sourcePath the declaration's path the row was recorded from.
   * @param referenceTypeId the ReferenceType.
   * @param direction the recorded direction.
   * @param targetNodeId the absolute target.
   * @return an absolute {@link ReferenceRow}.
   */
  public static ReferenceRow absolute(
      BrowsePath sourcePath,
      NodeId referenceTypeId,
      Reference.Direction direction,
      ExpandedNodeId targetNodeId) {

    return new ReferenceRow(sourcePath, referenceTypeId, direction, null, targetNodeId);
  }

  /**
   * @return {@code true} if both ends of this row are declarations of the model.
   */
  public boolean isRelative() {
    return targetPath != null;
  }

  /**
   * @return a stable string identifying this row's target — the target {@link BrowsePath} for a
   *     relative row, the parseable target {@link ExpandedNodeId} for an absolute one — for
   *     deterministic ordering and fingerprinting.
   */
  String targetKey() {
    return targetPath != null
        ? targetPath.toString()
        : requireNonNull(targetNodeId).toParseableString();
  }
}
