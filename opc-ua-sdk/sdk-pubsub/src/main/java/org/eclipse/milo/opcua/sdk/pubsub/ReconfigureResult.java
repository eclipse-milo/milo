/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import java.util.List;

/**
 * The result of a {@link PubSubService#reconfigure} or {@link PubSubService#update} call, listing
 * the affected components by path.
 *
 * @param addedPaths the paths of components added by the reconfiguration.
 * @param removedPaths the paths of components removed by the reconfiguration; their handles are
 *     invalidated.
 * @param restartedPaths the paths of components restarted by the reconfiguration.
 */
public record ReconfigureResult(
    List<String> addedPaths, List<String> removedPaths, List<String> restartedPaths) {

  /**
   * Create a new {@link ReconfigureResult}.
   *
   * @param addedPaths the paths of components added by the reconfiguration.
   * @param removedPaths the paths of components removed by the reconfiguration; their handles are
   *     invalidated.
   * @param restartedPaths the paths of components restarted by the reconfiguration.
   */
  public ReconfigureResult {
    addedPaths = List.copyOf(addedPaths);
    removedPaths = List.copyOf(removedPaths);
    restartedPaths = List.copyOf(restartedPaths);
  }
}
