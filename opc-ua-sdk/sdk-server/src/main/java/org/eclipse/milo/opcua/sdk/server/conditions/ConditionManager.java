/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Server-level Condition registry.
 *
 * <p>Registration is what makes a Condition participate in ConditionRefresh/ConditionRefresh2
 * replay and cross-Condition EventId-keyed dispatch.
 */
public interface ConditionManager {

  /**
   * Register {@code condition}: it joins refresh replay and EventId-keyed dispatch.
   *
   * @param condition the {@link Condition} to register.
   */
  void register(Condition condition);

  /**
   * Unregister {@code condition}.
   *
   * <p>An in-flight method call against the Condition completes against the Condition's own state;
   * subsequent EventId lookups through this manager no longer resolve to it.
   *
   * @param condition the {@link Condition} to unregister.
   */
  void unregister(Condition condition);

  /**
   * Find the registered {@link Condition} identified by {@code conditionId}.
   *
   * @param conditionId the ConditionId, i.e. the {@link NodeId} of the Condition instance node.
   * @return the registered {@link Condition} identified by {@code conditionId}, if any.
   */
  Optional<Condition> findCondition(NodeId conditionId);

  /**
   * Find the {@link ConditionBranch} of any registered Condition that {@code eventId} identifies a
   * state of.
   *
   * @param eventId an EventId from an event previously issued by a registered Condition.
   * @return the {@link ConditionBranch} that accepted {@code eventId}, if any.
   */
  Optional<ConditionBranch> findBranch(ByteString eventId);
}
