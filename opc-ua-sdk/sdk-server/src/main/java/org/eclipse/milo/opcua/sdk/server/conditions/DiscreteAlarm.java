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

import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.server.model.objects.DiscreteAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * Behavior for a DiscreteAlarm instance (Part 9 §5.8.24.1): an {@link AlarmCondition} whose input
 * may take on only a certain number of possible values (e.g. true/false,
 * running/stopped/terminating) — the type adds no members.
 *
 * <p>The SDK does not sample the alarm's input; the application observes its value and drives
 * {@link #setActive}.
 */
public class DiscreteAlarm extends AlarmCondition {

  /**
   * Create DiscreteAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link DiscreteAlarmTypeNode} to wrap.
   */
  public DiscreteAlarm(DiscreteAlarmTypeNode node) {
    super(node);
  }

  /**
   * Create a DiscreteAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure.
   * @return the created {@link DiscreteAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static DiscreteAlarm create(UaNodeContext context, Consumer<ConditionBuilder> configure)
      throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);

    return build(
        builder,
        NodeIds.DiscreteAlarmType,
        node -> new DiscreteAlarm((DiscreteAlarmTypeNode) node));
  }

  @Override
  public DiscreteAlarmTypeNode getNode() {
    return (DiscreteAlarmTypeNode) super.getNode();
  }
}
