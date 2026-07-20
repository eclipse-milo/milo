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

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredEventItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * Server-level Condition registry and owner of ConditionRefresh/ConditionRefresh2 execution.
 *
 * <p>Registration is what makes a Condition participate in ConditionRefresh/ConditionRefresh2
 * replay and cross-Condition EventId-keyed dispatch.
 */
public interface ConditionManager {

  /**
   * Register {@code condition}: it joins refresh replay and EventId-keyed dispatch.
   *
   * @param condition the {@link Condition} to register.
   */
  void register(Condition condition);

  /**
   * Unregister {@code condition}.
   *
   * <p>An in-flight method call against the Condition completes against the Condition's own state;
   * subsequent EventId lookups through this manager no longer resolve to it.
   *
   * @param condition the {@link Condition} to unregister.
   */
  void unregister(Condition condition);

  /**
   * Find the registered {@link Condition} identified by {@code conditionId}.
   *
   * @param conditionId the ConditionId, i.e. the {@link NodeId} of the Condition instance node.
   * @return the registered {@link Condition} identified by {@code conditionId}, if any.
   */
  Optional<Condition> findCondition(NodeId conditionId);

  /**
   * Find a nested Method exposed by a registered Condition for ConditionId dispatch.
   *
   * <p>The default implementation preserves compatibility for custom managers by resolving through
   * {@link #findCondition}.
   *
   * @param conditionId the ConditionId used as the Call ObjectId.
   * @param methodId the instance or type-declaration MethodId.
   * @return the matching nested {@link UaMethodNode}, if any.
   */
  default Optional<UaMethodNode> findMethodNode(NodeId conditionId, NodeId methodId) {
    return findCondition(conditionId).flatMap(condition -> condition.findMethodNode(methodId));
  }

  /**
   * Find the {@link ConditionBranch} of any registered Condition that {@code eventId} identifies a
   * state of.
   *
   * @param eventId an EventId from an event previously issued by a registered Condition.
   * @return the {@link ConditionBranch} that accepted {@code eventId}, if any.
   */
  Optional<ConditionBranch> findBranch(ByteString eventId);

  /**
   * Execute ConditionRefresh (Part 9 §5.5.7) for every event monitored item of the identified
   * Subscription: a RefreshStart marker delivered regardless of the item's filter, replay of every
   * registered Condition's Retained branch states (carrying their original EventId and Time)
   * through normal filtered delivery, then a RefreshEnd marker.
   *
   * @param session the {@link Session} making the call.
   * @param subscriptionId the id of the Subscription to refresh.
   * @throws UaException {@code Bad_SubscriptionIdInvalid} if no such Subscription exists, {@code
   *     Bad_UserAccessDenied} if it belongs to another Session, {@code Bad_NothingToDo} if it has
   *     no event monitored items, or {@code Bad_RefreshInProgress} if a refresh of the Subscription
   *     is already in progress.
   */
  void conditionRefresh(Session session, UInteger subscriptionId) throws UaException;

  /**
   * Execute ConditionRefresh2 (Part 9 §5.5.7): like {@link #conditionRefresh}, but markers and
   * replay are delivered only to the identified monitored item.
   *
   * @param session the {@link Session} making the call.
   * @param subscriptionId the id of the Subscription the monitored item belongs to.
   * @param monitoredItemId the id of the event monitored item to refresh.
   * @throws UaException {@code Bad_SubscriptionIdInvalid} if no such Subscription exists, {@code
   *     Bad_UserAccessDenied} if it belongs to another Session, {@code Bad_MonitoredItemIdInvalid}
   *     if the Subscription has no event monitored item with the given id, or {@code
   *     Bad_RefreshInProgress} if a refresh of the Subscription is already in progress.
   */
  void conditionRefresh2(Session session, UInteger subscriptionId, UInteger monitoredItemId)
      throws UaException;

  /**
   * Notification that {@code eventItem} discarded a notification that may have derived from a
   * ConditionType-descendant event and now has queue capacity again (Part 9 §5.11.4).
   *
   * <p>Invoked while the item's monitor is held, during a notification drain; implementations
   * deliver a RefreshRequired marker to the item so its Condition state is refreshed. The default
   * implementation does nothing.
   *
   * @param eventItem the {@link MonitoredEventItem} whose queue overflowed and has since drained.
   */
  default void onConditionEventOverflow(MonitoredEventItem eventItem) {}

  /**
   * Release runtime resources owned by registered Conditions during server shutdown.
   *
   * <p>The default implementation is a no-op so custom ConditionManager implementations remain
   * source-compatible and need not own Condition lifecycle.
   */
  default void shutdown() {}
}
