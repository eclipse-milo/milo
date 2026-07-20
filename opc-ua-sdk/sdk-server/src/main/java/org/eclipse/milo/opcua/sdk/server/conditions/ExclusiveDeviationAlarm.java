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
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * Behavior for an ExclusiveDeviationAlarm instance (Part 9 §5.8.22.3): an {@link
 * ExclusiveLimitAlarm} on the deviation from the setpoint identified by the mandatory SetpointNode
 * Property. The SDK does not compute the deviation: pass it to {@link #evaluate} directly.
 */
public class ExclusiveDeviationAlarm extends ExclusiveLimitAlarm {

  /**
   * Create ExclusiveDeviationAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link ExclusiveDeviationAlarmTypeNode} to wrap.
   */
  public ExclusiveDeviationAlarm(ExclusiveDeviationAlarmTypeNode node) {
    super(node);
  }

  /**
   * Create an ExclusiveDeviationAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure; at least one limit and the
   *     {@link ConditionBuilder#setpointNode setpointNode} must be configured.
   * @return the created {@link ExclusiveDeviationAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static ExclusiveDeviationAlarm create(
      UaNodeContext context, Consumer<ConditionBuilder> configure) throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);
    builder.validateDeviationLimits(false);
    builder.requireSetpointNode();

    return build(
        builder,
        NodeIds.ExclusiveDeviationAlarmType,
        node -> new ExclusiveDeviationAlarm((ExclusiveDeviationAlarmTypeNode) node));
  }

  @Override
  public ExclusiveDeviationAlarmTypeNode getNode() {
    return (ExclusiveDeviationAlarmTypeNode) super.getNode();
  }
}
