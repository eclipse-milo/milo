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
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Behavior for a NonExclusiveRateOfChangeAlarm instance (Part 9 §5.8.23.2): a {@link
 * NonExclusiveLimitAlarm} on the rate at which the input changes, optionally described by the
 * EngineeringUnits Property (absent: the input's unit per second). The SDK does not compute the
 * rate: pass it to {@link #evaluate} directly.
 */
public class NonExclusiveRateOfChangeAlarm extends NonExclusiveLimitAlarm {

  /**
   * Create NonExclusiveRateOfChangeAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link NonExclusiveRateOfChangeAlarmTypeNode} to wrap.
   */
  public NonExclusiveRateOfChangeAlarm(NonExclusiveRateOfChangeAlarmTypeNode node) {
    super(node);
  }

  /**
   * Create a NonExclusiveRateOfChangeAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure; at least a High or Low
   *     limit must be configured, obeying HighHigh &gt; High &gt; Low &gt; LowLow.
   * @return the created {@link NonExclusiveRateOfChangeAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static NonExclusiveRateOfChangeAlarm create(
      UaNodeContext context, Consumer<ConditionBuilder> configure) throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);
    builder.validateLimits(true);

    return build(
        builder,
        NodeIds.NonExclusiveRateOfChangeAlarmType,
        node -> new NonExclusiveRateOfChangeAlarm((NonExclusiveRateOfChangeAlarmTypeNode) node));
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @return the attached, unregistered behavior.
   */
  public static NonExclusiveRateOfChangeAlarm attach(NonExclusiveRateOfChangeAlarmTypeNode node) {
    return attach(node, options -> {});
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @param configure receives source-wiring options.
   * @return the attached, unregistered behavior.
   */
  public static NonExclusiveRateOfChangeAlarm attach(
      NonExclusiveRateOfChangeAlarmTypeNode node, Consumer<AttachOptions> configure) {
    return attach(
        node,
        configure,
        attached ->
            new NonExclusiveRateOfChangeAlarm((NonExclusiveRateOfChangeAlarmTypeNode) attached));
  }

  /**
   * Complete and attach behavior to a pre-existing instance without replacing its identity.
   *
   * @param context the context whose NodeManager owns the instance.
   * @param nodeId the existing ConditionId.
   * @param configure receives the adopt-mode builder.
   * @return the adopted, unregistered behavior.
   * @throws UaException if validation or in-place completion fails.
   */
  public static NonExclusiveRateOfChangeAlarm adopt(
      UaNodeContext context, NodeId nodeId, Consumer<ConditionBuilder> configure)
      throws UaException {
    ConditionBuilder builder =
        ConditionBuilder.forAdoption(context, nodeId, NonExclusiveRateOfChangeAlarmTypeNode.class);
    configure.accept(builder);
    builder.validateLimits(true);
    return build(
        builder,
        NodeIds.NonExclusiveRateOfChangeAlarmType,
        node -> new NonExclusiveRateOfChangeAlarm((NonExclusiveRateOfChangeAlarmTypeNode) node));
  }

  @Override
  public NonExclusiveRateOfChangeAlarmTypeNode getNode() {
    return (NonExclusiveRateOfChangeAlarmTypeNode) super.getNode();
  }
}
