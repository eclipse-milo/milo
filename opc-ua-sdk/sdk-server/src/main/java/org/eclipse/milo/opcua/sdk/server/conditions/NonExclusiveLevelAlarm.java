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
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveLevelAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * Behavior for a NonExclusiveLevelAlarm instance (Part 9 §5.8.21.2): a {@link
 * NonExclusiveLimitAlarm} on an absolute level — the type adds no members; {@link #evaluate} takes
 * the process value.
 */
public class NonExclusiveLevelAlarm extends NonExclusiveLimitAlarm {

  /**
   * Create NonExclusiveLevelAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link NonExclusiveLevelAlarmTypeNode} to wrap.
   */
  public NonExclusiveLevelAlarm(NonExclusiveLevelAlarmTypeNode node) {
    super(node);
  }

  /**
   * Create a NonExclusiveLevelAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure; at least a High or Low
   *     limit must be configured, obeying HighHigh &gt; High &gt; Low &gt; LowLow.
   * @return the created {@link NonExclusiveLevelAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static NonExclusiveLevelAlarm create(
      UaNodeContext context, Consumer<ConditionBuilder> configure) throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);
    builder.validateLimits(true);

    return build(
        builder,
        NodeIds.NonExclusiveLevelAlarmType,
        node -> new NonExclusiveLevelAlarm((NonExclusiveLevelAlarmTypeNode) node));
  }

  @Override
  public NonExclusiveLevelAlarmTypeNode getNode() {
    return (NonExclusiveLevelAlarmTypeNode) super.getNode();
  }
}
