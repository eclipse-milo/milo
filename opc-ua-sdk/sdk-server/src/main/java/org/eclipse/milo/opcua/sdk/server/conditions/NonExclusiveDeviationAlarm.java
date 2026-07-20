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
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * Behavior for a NonExclusiveDeviationAlarm instance (Part 9 §5.8.22.2): a {@link
 * NonExclusiveLimitAlarm} on the deviation from the setpoint identified by the mandatory
 * SetpointNode Property. The SDK does not compute the deviation: pass it to {@link #evaluate}
 * directly.
 */
public class NonExclusiveDeviationAlarm extends NonExclusiveLimitAlarm {

  /**
   * Create NonExclusiveDeviationAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link NonExclusiveDeviationAlarmTypeNode} to wrap.
   */
  public NonExclusiveDeviationAlarm(NonExclusiveDeviationAlarmTypeNode node) {
    super(node);
  }

  /**
   * Create a NonExclusiveDeviationAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure; at least a High or Low
   *     limit and the {@link ConditionBuilder#setpointNode setpointNode} must be configured.
   * @return the created {@link NonExclusiveDeviationAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static NonExclusiveDeviationAlarm create(
      UaNodeContext context, Consumer<ConditionBuilder> configure) throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);
    builder.validateDeviationLimits(true);
    builder.requireSetpointNode();

    return build(
        builder,
        NodeIds.NonExclusiveDeviationAlarmType,
        node -> new NonExclusiveDeviationAlarm((NonExclusiveDeviationAlarmTypeNode) node));
  }

  @Override
  public NonExclusiveDeviationAlarmTypeNode getNode() {
    return (NonExclusiveDeviationAlarmTypeNode) super.getNode();
  }
}
