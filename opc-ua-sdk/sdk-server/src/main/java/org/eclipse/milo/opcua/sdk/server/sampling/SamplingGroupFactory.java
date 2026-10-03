/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;

/**
 * Creates the {@link SamplingGroup} for a sampling interval.
 *
 * <p>A {@link SamplingManager} calls this once per distinct interval, when the first item at that
 * interval arrives, and shuts the group down when its last item leaves.
 *
 * <pre>{@code
 * SamplingGroupFactory factory =
 *     (server, intervalMillis) -> new AddressSpaceSamplingGroup(server, addressSpace, intervalMillis);
 * }</pre>
 */
@FunctionalInterface
public interface SamplingGroupFactory {

  /**
   * Create the group that samples items at {@code intervalMillis}.
   *
   * @param server the server the group runs on.
   * @param intervalMillis the group's sampling interval, after bucketing and the minimum interval
   *     have been applied.
   * @return a new, not yet started, {@link SamplingGroup}.
   */
  SamplingGroup create(OpcUaServer server, long intervalMillis);
}
