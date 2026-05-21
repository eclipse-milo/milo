/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.reverse;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Runtime control handle for one server-side Reverse Connect target.
 *
 * <p>Handles are returned when a target is registered with the server. Each operation addresses the
 * target by its stable id and returns the resulting runtime snapshot when the manager has applied
 * the change.
 */
public final class ReverseConnectTargetHandle {

  private final ReverseConnectTargetManager manager;
  private final UUID targetId;

  ReverseConnectTargetHandle(ReverseConnectTargetManager manager, UUID targetId) {
    this.manager = manager;
    this.targetId = targetId;
  }

  /**
   * Get the id of the target controlled by this handle.
   *
   * @return the target id.
   */
  public UUID getTargetId() {
    return targetId;
  }

  /**
   * Get the current runtime snapshot for the target.
   *
   * @return the current snapshot, or {@link Optional#empty()} if the target has been removed.
   */
  public Optional<ReverseConnectTargetSnapshot> snapshot() {
    return manager.snapshot(targetId);
  }

  /**
   * Pause the target and cancel any scheduled or in-flight attempt.
   *
   * <p>Existing reverse-opened channels remain open; pausing only prevents new attempts from being
   * scheduled.
   *
   * @return a completed future containing the updated snapshot, or a failed future if the target
   *     has been removed.
   */
  public CompletableFuture<ReverseConnectTargetSnapshot> pause() {
    return manager.pause(targetId);
  }

  /**
   * Resume the target and schedule an attempt if the server is running.
   *
   * @return a completed future containing the updated snapshot, or a failed future if the target
   *     has been removed or cannot be scheduled.
   */
  public CompletableFuture<ReverseConnectTargetSnapshot> resume() {
    return manager.resume(targetId);
  }

  /**
   * Request an immediate attempt for the target when it is schedulable.
   *
   * <p>The request is ignored when the target is disabled, paused, the server is stopped, or an
   * attempt is already pending.
   *
   * @return a completed future containing the updated snapshot, or a failed future if the target
   *     has been removed or cannot be scheduled.
   */
  public CompletableFuture<ReverseConnectTargetSnapshot> trigger() {
    return manager.trigger(targetId);
  }

  /**
   * Remove the target and close any resources it owns.
   *
   * <p>Removal cancels scheduled work, closes any in-flight attempt, and closes active
   * reverse-opened channels for this target.
   *
   * @return a completed future containing the final snapshot, or a failed future if the target has
   *     already been removed.
   */
  public CompletableFuture<ReverseConnectTargetSnapshot> remove() {
    return manager.remove(targetId);
  }
}
