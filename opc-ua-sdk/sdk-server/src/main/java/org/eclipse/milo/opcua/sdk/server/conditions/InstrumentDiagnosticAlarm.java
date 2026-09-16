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
import org.eclipse.milo.opcua.sdk.server.model.objects.InstrumentDiagnosticAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Behavior for an InstrumentDiagnosticAlarm instance (Part 9 §5.8.24.5): an {@link OffNormalAlarm}
 * representing a fault in a field device, becoming active when the monitored device experiences a
 * fault such as a sensor failure — the type adds no members and is mainly used for categorization.
 */
public class InstrumentDiagnosticAlarm extends OffNormalAlarm {

  /**
   * Create InstrumentDiagnosticAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link InstrumentDiagnosticAlarmTypeNode} to wrap.
   */
  public InstrumentDiagnosticAlarm(InstrumentDiagnosticAlarmTypeNode node) {
    super(node);
  }

  /**
   * Create an InstrumentDiagnosticAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure.
   * @return the created {@link InstrumentDiagnosticAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static InstrumentDiagnosticAlarm create(
      UaNodeContext context, Consumer<ConditionBuilder> configure) throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);

    return build(
        builder,
        NodeIds.InstrumentDiagnosticAlarmType,
        node -> new InstrumentDiagnosticAlarm((InstrumentDiagnosticAlarmTypeNode) node));
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @return the attached, unregistered behavior.
   */
  public static InstrumentDiagnosticAlarm attach(InstrumentDiagnosticAlarmTypeNode node) {
    return attach(node, options -> {});
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @param configure receives source-wiring options.
   * @return the attached, unregistered behavior.
   */
  public static InstrumentDiagnosticAlarm attach(
      InstrumentDiagnosticAlarmTypeNode node, Consumer<AttachOptions> configure) {
    return attach(
        node,
        configure,
        attached -> new InstrumentDiagnosticAlarm((InstrumentDiagnosticAlarmTypeNode) attached));
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
  public static InstrumentDiagnosticAlarm adopt(
      UaNodeContext context, NodeId nodeId, Consumer<ConditionBuilder> configure)
      throws UaException {
    ConditionBuilder builder =
        ConditionBuilder.forAdoption(context, nodeId, InstrumentDiagnosticAlarmTypeNode.class);
    configure.accept(builder);
    return build(
        builder,
        NodeIds.InstrumentDiagnosticAlarmType,
        node -> new InstrumentDiagnosticAlarm((InstrumentDiagnosticAlarmTypeNode) node));
  }

  @Override
  public InstrumentDiagnosticAlarmTypeNode getNode() {
    return (InstrumentDiagnosticAlarmTypeNode) super.getNode();
  }
}
