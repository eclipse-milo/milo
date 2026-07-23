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
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemOffNormalAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Behavior for a SystemOffNormalAlarm instance (Part 9 §5.8.24.3): an {@link OffNormalAlarm} used
 * to indicate that an underlying system providing Alarm information is having a communication
 * problem, and that the server may have invalid or incomplete Condition state — the type adds no
 * members.
 */
public class SystemOffNormalAlarm extends OffNormalAlarm {

  /**
   * Create SystemOffNormalAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link SystemOffNormalAlarmTypeNode} to wrap.
   */
  public SystemOffNormalAlarm(SystemOffNormalAlarmTypeNode node) {
    super(node);
  }

  /**
   * Create a SystemOffNormalAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure.
   * @return the created {@link SystemOffNormalAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static SystemOffNormalAlarm create(
      UaNodeContext context, Consumer<ConditionBuilder> configure) throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);

    return build(
        builder,
        NodeIds.SystemOffNormalAlarmType,
        node -> new SystemOffNormalAlarm((SystemOffNormalAlarmTypeNode) node));
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @return the attached, unregistered behavior.
   */
  public static SystemOffNormalAlarm attach(SystemOffNormalAlarmTypeNode node) {
    return attach(node, options -> {});
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @param configure receives source-wiring options.
   * @return the attached, unregistered behavior.
   */
  public static SystemOffNormalAlarm attach(
      SystemOffNormalAlarmTypeNode node, Consumer<AttachOptions> configure) {
    return attach(
        node,
        configure,
        attached -> new SystemOffNormalAlarm((SystemOffNormalAlarmTypeNode) attached));
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
  public static SystemOffNormalAlarm adopt(
      UaNodeContext context, NodeId nodeId, Consumer<ConditionBuilder> configure)
      throws UaException {
    ConditionBuilder builder =
        ConditionBuilder.forAdoption(context, nodeId, SystemOffNormalAlarmTypeNode.class);
    configure.accept(builder);
    return build(
        builder,
        NodeIds.SystemOffNormalAlarmType,
        node -> new SystemOffNormalAlarm((SystemOffNormalAlarmTypeNode) node));
  }

  @Override
  public SystemOffNormalAlarmTypeNode getNode() {
    return (SystemOffNormalAlarmTypeNode) super.getNode();
  }
}
