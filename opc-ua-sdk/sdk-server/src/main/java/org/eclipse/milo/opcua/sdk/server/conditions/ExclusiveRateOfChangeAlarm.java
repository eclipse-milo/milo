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
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Behavior for an ExclusiveRateOfChangeAlarm instance (Part 9 §5.8.23.3): an {@link
 * ExclusiveLimitAlarm} on the rate at which the input changes, optionally described by the
 * EngineeringUnits Property (absent: the input's unit per second). The SDK does not compute the
 * rate: pass it to {@link #evaluate} directly.
 */
public class ExclusiveRateOfChangeAlarm extends ExclusiveLimitAlarm {

  /**
   * Create ExclusiveRateOfChangeAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link ExclusiveRateOfChangeAlarmTypeNode} to wrap.
   */
  public ExclusiveRateOfChangeAlarm(ExclusiveRateOfChangeAlarmTypeNode node) {
    super(node);
  }

  /**
   * Create an ExclusiveRateOfChangeAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure; at least one limit must be
   *     configured, obeying HighHigh &gt; High &gt; Low &gt; LowLow.
   * @return the created {@link ExclusiveRateOfChangeAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static ExclusiveRateOfChangeAlarm create(
      UaNodeContext context, Consumer<ConditionBuilder> configure) throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);
    builder.validateLimits(false);

    return build(
        builder,
        NodeIds.ExclusiveRateOfChangeAlarmType,
        node -> new ExclusiveRateOfChangeAlarm((ExclusiveRateOfChangeAlarmTypeNode) node));
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @return the attached, unregistered behavior.
   */
  public static ExclusiveRateOfChangeAlarm attach(ExclusiveRateOfChangeAlarmTypeNode node) {
    return attach(node, options -> {});
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @param configure receives source-wiring options.
   * @return the attached, unregistered behavior.
   */
  public static ExclusiveRateOfChangeAlarm attach(
      ExclusiveRateOfChangeAlarmTypeNode node, Consumer<AttachOptions> configure) {
    return attach(
        node,
        configure,
        attached -> new ExclusiveRateOfChangeAlarm((ExclusiveRateOfChangeAlarmTypeNode) attached));
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
  public static ExclusiveRateOfChangeAlarm adopt(
      UaNodeContext context, NodeId nodeId, Consumer<ConditionBuilder> configure)
      throws UaException {
    ConditionBuilder builder =
        ConditionBuilder.forAdoption(context, nodeId, ExclusiveRateOfChangeAlarmTypeNode.class);
    configure.accept(builder);
    builder.validateLimits(false);
    return build(
        builder,
        NodeIds.ExclusiveRateOfChangeAlarmType,
        node -> new ExclusiveRateOfChangeAlarm((ExclusiveRateOfChangeAlarmTypeNode) node));
  }

  @Override
  public ExclusiveRateOfChangeAlarmTypeNode getNode() {
    return (ExclusiveRateOfChangeAlarmTypeNode) super.getNode();
  }
}
