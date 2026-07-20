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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Default {@link ConditionManager} implementation.
 *
 * <p>Manager state locks independently of any Condition, and no Condition lock is acquired by any
 * operation: {@link #findBranch} traverses each registered Condition's lock-free EventId window
 * ({@link Condition#findBranch}), so it is safe to call even from code already holding a
 * Condition's lock (e.g. a {@link ConditionMethodInterceptor} hook).
 */
public class DefaultConditionManager implements ConditionManager {

  private final ConcurrentMap<NodeId, Condition> conditions = new ConcurrentHashMap<>();

  @Override
  public void register(Condition condition) {
    conditions.put(condition.getConditionId(), condition);
  }

  @Override
  public void unregister(Condition condition) {
    conditions.remove(condition.getConditionId(), condition);
  }

  @Override
  public Optional<Condition> findCondition(NodeId conditionId) {
    return Optional.ofNullable(conditions.get(conditionId));
  }

  @Override
  public Optional<ConditionBranch> findBranch(ByteString eventId) {
    for (Condition condition : conditions.values()) {
      Optional<ConditionBranch> branch = condition.findBranch(eventId);
      if (branch.isPresent()) {
        return branch;
      }
    }
    return Optional.empty();
  }
}
