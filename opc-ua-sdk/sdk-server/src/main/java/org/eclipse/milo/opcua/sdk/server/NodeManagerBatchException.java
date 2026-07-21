/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import org.eclipse.milo.opcua.sdk.server.NodeManager.CommitResult;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Thrown when {@link NodeManager#commit(NodeManagerBatch)} cannot apply a batch in full.
 *
 * <p>{@link #getApplied()} journals the additions that were applied before the failure. A manager
 * whose commit is atomic always reports an empty journal — a failed commit changed nothing. A
 * manager using the sequential default emulation may report a partial journal; callers can attempt
 * best-effort removal of exactly those additions.
 */
public class NodeManagerBatchException extends UaException {

  private final CommitResult applied;

  public NodeManagerBatchException(long statusCode, String message, CommitResult applied) {
    super(statusCode, message);
    this.applied = applied;
  }

  public NodeManagerBatchException(
      long statusCode, String message, Throwable cause, CommitResult applied) {

    super(statusCode, message, cause);
    this.applied = applied;
  }

  /**
   * Create an exception for a batch whose declared expected generation no longer matches the target
   * {@link NodeManager}, indicating the manager was mutated since the batch was planned.
   *
   * @param expected the generation the batch expected.
   * @param actual the manager's current generation.
   * @param applied the journal of additions applied before the failure.
   * @return a {@link NodeManagerBatchException} with status {@link StatusCodes#Bad_InvalidState}.
   */
  static NodeManagerBatchException staleGeneration(
      long expected, long actual, CommitResult applied) {

    return new NodeManagerBatchException(
        StatusCodes.Bad_InvalidState,
        "expected generation %s but was %s".formatted(expected, actual),
        applied);
  }

  /**
   * Create an exception for a batch that reuses a Node not present in the target {@link
   * NodeManager}.
   *
   * @param nodeId the {@link NodeId} of the missing reused Node.
   * @param applied the journal of additions applied before the failure.
   * @return a {@link NodeManagerBatchException} with status {@link StatusCodes#Bad_NodeIdUnknown}.
   */
  static NodeManagerBatchException reusedNodeUnknown(NodeId nodeId, CommitResult applied) {
    return new NodeManagerBatchException(
        StatusCodes.Bad_NodeIdUnknown, "reused Node not present: " + nodeId, applied);
  }

  /**
   * Create an exception for a batch that would add a Node already present in the target {@link
   * NodeManager}.
   *
   * @param nodeId the {@link NodeId} of the colliding Node.
   * @param applied the journal of additions applied before the failure.
   * @return a {@link NodeManagerBatchException} with status {@link StatusCodes#Bad_NodeIdExists}.
   */
  static NodeManagerBatchException nodeExists(NodeId nodeId, CommitResult applied) {
    return new NodeManagerBatchException(
        StatusCodes.Bad_NodeIdExists, "Node already exists: " + nodeId, applied);
  }

  /**
   * Get the journal of additions that were applied before the commit failed.
   *
   * @return the journal of additions that were applied before the commit failed; empty if the
   *     failed commit changed nothing.
   */
  public CommitResult getApplied() {
    return applied;
  }
}
