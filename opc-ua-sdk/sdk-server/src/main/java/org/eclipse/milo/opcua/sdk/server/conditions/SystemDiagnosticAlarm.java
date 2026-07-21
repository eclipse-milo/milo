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
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemDiagnosticAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * Behavior for a SystemDiagnosticAlarm instance (Part 9 §5.8.24.6): an {@link OffNormalAlarm}
 * representing a fault in a system or sub-system, becoming active when the monitored system
 * experiences a fault — the type adds no members and is mainly used for categorization.
 */
public class SystemDiagnosticAlarm extends OffNormalAlarm {

  /**
   * Create SystemDiagnosticAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link SystemDiagnosticAlarmTypeNode} to wrap.
   */
  public SystemDiagnosticAlarm(SystemDiagnosticAlarmTypeNode node) {
    super(node);
  }

  /**
   * Create a SystemDiagnosticAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure.
   * @return the created {@link SystemDiagnosticAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static SystemDiagnosticAlarm create(
      UaNodeContext context, Consumer<ConditionBuilder> configure) throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);

    return build(
        builder,
        NodeIds.SystemDiagnosticAlarmType,
        node -> new SystemDiagnosticAlarm((SystemDiagnosticAlarmTypeNode) node));
  }

  @Override
  public SystemDiagnosticAlarmTypeNode getNode() {
    return (SystemDiagnosticAlarmTypeNode) super.getNode();
  }
}
