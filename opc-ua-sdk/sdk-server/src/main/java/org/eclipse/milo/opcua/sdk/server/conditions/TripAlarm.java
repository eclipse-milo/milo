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
import org.eclipse.milo.opcua.sdk.server.model.objects.TripAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * Behavior for a TripAlarm instance (Part 9 §5.8.24.4): an {@link OffNormalAlarm} representing an
 * equipment trip Condition, becoming active when the monitored piece of equipment experiences an
 * abnormal fault such as a motor shutting down due to an overload — the type adds no members and is
 * mainly used for categorization.
 */
public class TripAlarm extends OffNormalAlarm {

  /**
   * Create TripAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link TripAlarmTypeNode} to wrap.
   */
  public TripAlarm(TripAlarmTypeNode node) {
    super(node);
  }

  /**
   * Create a TripAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure.
   * @return the created {@link TripAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static TripAlarm create(UaNodeContext context, Consumer<ConditionBuilder> configure)
      throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);

    return build(builder, NodeIds.TripAlarmType, node -> new TripAlarm((TripAlarmTypeNode) node));
  }

  @Override
  public TripAlarmTypeNode getNode() {
    return (TripAlarmTypeNode) super.getNode();
  }
}
