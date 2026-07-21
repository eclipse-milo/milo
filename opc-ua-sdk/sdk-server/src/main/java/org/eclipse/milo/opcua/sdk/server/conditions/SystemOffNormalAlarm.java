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

  @Override
  public SystemOffNormalAlarmTypeNode getNode() {
    return (SystemOffNormalAlarmTypeNode) super.getNode();
  }
}
