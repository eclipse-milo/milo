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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

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

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @return the attached, unregistered behavior.
   */
  public static DiscreteAlarm attach(DiscreteAlarmTypeNode node) {
    return attach(node, options -> {});
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @param configure receives source-wiring options.
   * @return the attached, unregistered behavior.
   */
  public static DiscreteAlarm attach(
      DiscreteAlarmTypeNode node, Consumer<AttachOptions> configure) {
    return attach(node, configure, attached -> new DiscreteAlarm((DiscreteAlarmTypeNode) attached));
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
  public static DiscreteAlarm adopt(
      UaNodeContext context, NodeId nodeId, Consumer<ConditionBuilder> configure)
      throws UaException {
    ConditionBuilder builder =
        ConditionBuilder.forAdoption(context, nodeId, DiscreteAlarmTypeNode.class);
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
