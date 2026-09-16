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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

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
   *     limit and the {@link ConditionBuilder#setpointNode(NodeId) setpointNode} must be
   *     configured.
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

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @return the attached, unregistered behavior.
   */
  public static NonExclusiveDeviationAlarm attach(NonExclusiveDeviationAlarmTypeNode node) {
    return attach(node, options -> {});
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @param configure receives source-wiring options.
   * @return the attached, unregistered behavior.
   */
  public static NonExclusiveDeviationAlarm attach(
      NonExclusiveDeviationAlarmTypeNode node, Consumer<AttachOptions> configure) {
    return attach(
        node,
        configure,
        attached -> new NonExclusiveDeviationAlarm((NonExclusiveDeviationAlarmTypeNode) attached));
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
  public static NonExclusiveDeviationAlarm adopt(
      UaNodeContext context, NodeId nodeId, Consumer<ConditionBuilder> configure)
      throws UaException {
    ConditionBuilder builder =
        ConditionBuilder.forAdoption(context, nodeId, NonExclusiveDeviationAlarmTypeNode.class);
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
