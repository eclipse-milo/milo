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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.Test;

/** Tests runtime-resource ownership in {@link DefaultConditionManager}. */
public class DefaultConditionManagerTest {

  /**
   * Unregistering must release the Condition's runtime resources (otherwise shelving timers leak
   * and keep firing against a removed Condition), but only when the call actually removes the
   * Condition: a stale or repeated unregister must be a no-op, not a second shutdown() call.
   */
  @Test
  void unregisterReleasesConditionResourcesExactlyOnce() {
    OpcUaServer server = mock(OpcUaServer.class);
    var manager = new DefaultConditionManager(server);
    Condition condition = mock(Condition.class);
    NodeId conditionId = new NodeId(1, "ManagedCondition");
    when(condition.getConditionId()).thenReturn(conditionId);

    manager.register(condition);
    manager.unregister(condition);
    manager.unregister(condition);

    verify(condition).shutdown();
    assertTrue(manager.findCondition(conditionId).isEmpty());
  }

  /**
   * A Condition displaced by registering a different instance under the same ConditionId leaves the
   * registry without ever being unregistered, so register() itself must release the displaced
   * instance's resources — otherwise its shelving timers leak for the server's lifetime and fire
   * against an abandoned Condition.
   */
  @Test
  void registerReleasesDisplacedConditionResources() {
    OpcUaServer server = mock(OpcUaServer.class);
    var manager = new DefaultConditionManager(server);
    NodeId conditionId = new NodeId(1, "ManagedCondition");
    Condition original = mock(Condition.class);
    when(original.getConditionId()).thenReturn(conditionId);
    Condition replacement = mock(Condition.class);
    when(replacement.getConditionId()).thenReturn(conditionId);

    manager.register(original);
    manager.register(replacement);

    verify(original).shutdown();
    verify(replacement, never()).shutdown();
    assertTrue(manager.findCondition(conditionId).isPresent());
  }
}
