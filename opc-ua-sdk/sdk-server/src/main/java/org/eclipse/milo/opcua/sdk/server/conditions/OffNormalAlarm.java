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

import java.util.Objects;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.server.model.objects.OffNormalAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

/**
 * Behavior for an OffNormalAlarm instance (Part 9 §5.8.24.2): a {@link DiscreteAlarm} representing
 * a discrete Condition that is considered to be not normal. The mandatory NormalState Property
 * identifies the Variable whose value is the normal state of the Variable identified by InputNode;
 * the alarm is active while the two values differ.
 *
 * <p>The SDK does not sample the input or the normal-state Variable: the application observes both
 * and either drives {@link #setActive} directly or passes the two sampled values to {@link
 * #evaluate}.
 */
public class OffNormalAlarm extends DiscreteAlarm {

  /**
   * Create OffNormalAlarm behavior wrapping {@code node}.
   *
   * @param node the {@link OffNormalAlarmTypeNode} to wrap.
   */
  public OffNormalAlarm(OffNormalAlarmTypeNode node) {
    super(node);
  }

  /**
   * Create an OffNormalAlarm instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure; {@link
   *     ConditionBuilder#normalState(NodeId) normalState} identifies the Variable holding the
   *     normal state value.
   * @return the created {@link OffNormalAlarm}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static OffNormalAlarm create(UaNodeContext context, Consumer<ConditionBuilder> configure)
      throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);

    return build(
        builder,
        NodeIds.OffNormalAlarmType,
        node -> new OffNormalAlarm((OffNormalAlarmTypeNode) node));
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @return the attached, unregistered behavior.
   */
  public static OffNormalAlarm attach(OffNormalAlarmTypeNode node) {
    return attach(node, options -> {});
  }

  /**
   * Attach behavior to a complete, pre-existing instance.
   *
   * @param node the complete generated typed node.
   * @param configure receives source-wiring options.
   * @return the attached, unregistered behavior.
   */
  public static OffNormalAlarm attach(
      OffNormalAlarmTypeNode node, Consumer<AttachOptions> configure) {
    return attach(
        node, configure, attached -> new OffNormalAlarm((OffNormalAlarmTypeNode) attached));
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
  public static OffNormalAlarm adopt(
      UaNodeContext context, NodeId nodeId, Consumer<ConditionBuilder> configure)
      throws UaException {
    ConditionBuilder builder =
        ConditionBuilder.forAdoption(context, nodeId, OffNormalAlarmTypeNode.class);
    configure.accept(builder);
    return build(
        builder,
        NodeIds.OffNormalAlarmType,
        node -> new OffNormalAlarm((OffNormalAlarmTypeNode) node));
  }

  @Override
  public OffNormalAlarmTypeNode getNode() {
    return (OffNormalAlarmTypeNode) super.getNode();
  }

  /**
   * Evaluate the alarm against a sampled input value and normal-state value: active iff the two
   * differ.
   *
   * <p>A compare-and-set convenience over {@link #setActive}: the application samples both values —
   * the SDK reads and subscribes to nothing. Values are compared with {@link Variant} equality
   * semantics: {@link Variant} wrappers are unwrapped, and a primitive array and a boxed array with
   * equal elements are equal. A call that does not change the active state is a no-op.
   *
   * @param value the sampled value of the Variable identified by InputNode.
   * @param normalValue the sampled value of the Variable identified by NormalState.
   */
  public void evaluate(@Nullable Object value, @Nullable Object normalValue) {
    setActive(!valuesEqual(value, normalValue));
  }

  private static boolean valuesEqual(@Nullable Object value, @Nullable Object normalValue) {
    if (value instanceof Variant v) {
      value = v.value();
    }
    if (normalValue instanceof Variant v) {
      normalValue = v.value();
    }
    if (value == null || normalValue == null) {
      return value == normalValue;
    }

    Class<?> valueClass = value.getClass();
    Class<?> normalValueClass = normalValue.getClass();

    if (valueClass.isArray()
        && normalValueClass.isArray()
        && valueClass.getComponentType().isPrimitive()
            != normalValueClass.getComponentType().isPrimitive()) {

      return Objects.deepEquals(ArrayUtil.box(value), ArrayUtil.box(normalValue));
    } else {
      return Objects.deepEquals(value, normalValue);
    }
  }
}
