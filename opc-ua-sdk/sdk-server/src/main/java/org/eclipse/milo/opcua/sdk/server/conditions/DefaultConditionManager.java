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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.events.EventNotifierScope;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredEventItem;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link ConditionManager} implementation.
 *
 * <p>Manager state locks independently of any Condition, and no Condition lock is held while
 * delivering to monitored items: {@link #findBranch} traverses each registered Condition's
 * lock-free EventId window ({@link Condition#findBranch}), so it is safe to call even from code
 * already holding a Condition's lock (e.g. a {@link ConditionMethodInterceptor} hook), and refresh
 * replay delivers immutable {@link ConditionEventSnapshot}s taken per Condition under that
 * Condition's lock.
 *
 * <p>Via {@link #onConditionEventOverflow}: an event monitored item that discarded a
 * condition-derived notification receives a RefreshRequired marker once its queue has capacity
 * again (§5.11.4).
 */
public class DefaultConditionManager implements ConditionManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ConcurrentMap<NodeId, Condition> conditions = new ConcurrentHashMap<>();

  /** Ids of Subscriptions with a refresh in progress (§5.5.7's per-Subscription guard). */
  private final Set<UInteger> refreshInProgress = ConcurrentHashMap.newKeySet();

  private final OpcUaServer server;

  public DefaultConditionManager(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public void register(Condition condition) {
    conditions.put(condition.getConditionId(), condition);
  }

  @Override
  public void unregister(Condition condition) {
    conditions.remove(condition.getConditionId(), condition);
  }

  @Override
  public Optional<Condition> findCondition(NodeId conditionId) {
    return Optional.ofNullable(conditions.get(conditionId));
  }

  @Override
  public Optional<ConditionBranch> findBranch(ByteString eventId) {
    for (Condition condition : conditions.values()) {
      Optional<ConditionBranch> branch = condition.findBranch(eventId);
      if (branch.isPresent()) {
        return branch;
      }
    }
    return Optional.empty();
  }

  @Override
  public void conditionRefresh(Session session, UInteger subscriptionId) throws UaException {
    Subscription subscription = validateSubscriptionAccess(session, subscriptionId);

    List<MonitoredEventItem> eventItems =
        subscription.getMonitoredItems().values().stream()
            .filter(MonitoredEventItem.class::isInstance)
            .map(MonitoredEventItem.class::cast)
            .toList();

    if (eventItems.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    executeRefresh(subscriptionId, eventItems);
  }

  @Override
  public void conditionRefresh2(Session session, UInteger subscriptionId, UInteger monitoredItemId)
      throws UaException {

    Subscription subscription = validateSubscriptionAccess(session, subscriptionId);

    if (!(subscription.getMonitoredItems().get(monitoredItemId)
        instanceof MonitoredEventItem eventItem)) {

      throw new UaException(StatusCodes.Bad_MonitoredItemIdInvalid);
    }

    executeRefresh(subscriptionId, List.of(eventItem));
  }

  private Subscription validateSubscriptionAccess(Session session, UInteger subscriptionId)
      throws UaException {

    Subscription subscription =
        Optional.ofNullable(server.getSubscriptions().get(subscriptionId))
            .orElseThrow(() -> new UaException(StatusCodes.Bad_SubscriptionIdInvalid));

    if (!session.getSessionId().equals(subscription.getSession().getSessionId())) {
      throw new UaException(StatusCodes.Bad_UserAccessDenied);
    }

    return subscription;
  }

  /**
   * Deliver a refresh bracket to each target item: RefreshStart marker (one shared EventId per
   * bracket), every registered Condition's in-scope Retained branch snapshots through normal
   * filtered delivery, then the RefreshEnd marker. A snapshot failure skips that Condition;
   * RefreshEnd is attempted for every item whose RefreshStart was delivered so clients don't hang
   * in refresh state.
   */
  private void executeRefresh(UInteger subscriptionId, List<MonitoredEventItem> eventItems)
      throws UaException {

    if (!refreshInProgress.add(subscriptionId)) {
      throw new UaException(StatusCodes.Bad_RefreshInProgress);
    }

    try {
      executeRefresh(eventItems);
    } finally {
      refreshInProgress.remove(subscriptionId);
    }
  }

  private void executeRefresh(List<MonitoredEventItem> eventItems) throws UaException {
    BaseEventTypeNode refreshStart = createMarkerEvent(NodeIds.RefreshStartEventType);
    try {
      BaseEventTypeNode refreshEnd = createMarkerEvent(NodeIds.RefreshEndEventType);
      try {
        deliverRefresh(eventItems, refreshStart, refreshEnd);
      } finally {
        deleteMarker(refreshEnd, "RefreshEnd");
      }
    } finally {
      deleteMarker(refreshStart, "RefreshStart");
    }
  }

  private void deliverRefresh(
      List<MonitoredEventItem> eventItems,
      BaseEventTypeNode refreshStart,
      BaseEventTypeNode refreshEnd) {

    List<ConditionEventSnapshot> snapshots = List.of();
    var startedItems = new ArrayList<MonitoredEventItem>();

    try {
      try {
        // Establish the refresh boundary before taking any retained-state snapshots. A live
        // Retain=false transition before an item's marker is therefore also before snapshot
        // capture and cannot be followed by a stale retained snapshot inside that item's bracket.
        for (MonitoredEventItem eventItem : eventItems) {
          if (eventItem.isSamplingEnabled()) {
            eventItem.onRefreshMarker(refreshStart);
            startedItems.add(eventItem);
          }
        }

        snapshots = createRefreshSnapshots();
        List<ScopedSnapshot> scopedSnapshots = resolveNotifierScopes(snapshots);

        for (MonitoredEventItem eventItem : startedItems) {
          for (ScopedSnapshot scopedSnapshot : scopedSnapshots) {
            if (!EventNotifierScope.contains(eventItem, scopedSnapshot.notifierScope())) {
              continue;
            }

            ConditionEventSnapshot snapshot = scopedSnapshot.snapshot();
            try {
              eventItem.onEvent(snapshot.getEventNode());
            } catch (Throwable t) {
              logger.warn(
                  "Error replaying Condition {} to item {}",
                  snapshot.getEventNode().getNodeId(),
                  eventItem.getId(),
                  t);
            }
          }
        }
      } finally {
        // Close every bracket that was opened, even if a later start, snapshot, scope resolution,
        // or replay fails. One failed end delivery must not prevent the remaining items' ends.
        for (MonitoredEventItem eventItem : startedItems) {
          try {
            eventItem.onRefreshMarker(refreshEnd);
          } catch (Throwable t) {
            logger.warn("Error delivering RefreshEnd to item {}", eventItem.getId(), t);
          }
        }
      }
    } finally {
      // Cleanup is deliberately independent: one failing delete cannot suppress later deletes,
      // and the outer executeRefresh finally always releases the per-subscription guard.
      for (ConditionEventSnapshot snapshot : snapshots) {
        try {
          snapshot.delete();
        } catch (Throwable t) {
          logger.warn("Error deleting refresh snapshot", t);
        }
      }
    }
  }

  private List<ScopedSnapshot> resolveNotifierScopes(List<ConditionEventSnapshot> snapshots) {
    var scopedSnapshots = new ArrayList<ScopedSnapshot>(snapshots.size());

    for (ConditionEventSnapshot snapshot : snapshots) {
      Set<NodeId> notifierScope = EventNotifierScope.resolve(server, snapshot.getEventNode());
      scopedSnapshots.add(new ScopedSnapshot(snapshot, notifierScope));
    }

    return scopedSnapshots;
  }

  private void deleteMarker(BaseEventTypeNode marker, String name) {
    try {
      marker.delete();
    } catch (Throwable t) {
      logger.warn("Error deleting {} marker", name, t);
    }
  }

  private record ScopedSnapshot(ConditionEventSnapshot snapshot, Set<NodeId> notifierScope) {}

  private List<ConditionEventSnapshot> createRefreshSnapshots() {
    var snapshots = new ArrayList<ConditionEventSnapshot>();

    for (Condition condition : conditions.values()) {
      try {
        snapshots.addAll(condition.createRefreshSnapshots());
      } catch (Exception e) {
        logger.warn(
            "Error creating refresh snapshots for Condition {}", condition.getConditionId(), e);
      }
    }

    return snapshots;
  }

  @Override
  public void onConditionEventOverflow(MonitoredEventItem eventItem) {
    try {
      BaseEventTypeNode refreshRequired = createMarkerEvent(NodeIds.RefreshRequiredEventType);
      try {
        eventItem.onRefreshMarker(refreshRequired);
      } finally {
        refreshRequired.delete();
      }
    } catch (Exception e) {
      logger.warn("Error delivering RefreshRequired to item {}", eventItem.getId(), e);
    }
  }

  private BaseEventTypeNode createMarkerEvent(NodeId typeDefinitionId) throws UaException {
    String name = markerName(typeDefinitionId);

    // newEvent pre-populates the transient-event scaffolding (auto NodeId, EventId nonce, Time,
    // ReceiveTime); only the marker-specific fields are set here.
    BaseEventTypeNode event = server.newEvent(typeDefinitionId).getNode();

    event.setBrowseName(new QualifiedName(1, name));
    event.setDisplayName(LocalizedText.english(name));
    event.setSourceNode(NodeIds.Server);
    event.setSourceName("Server");
    event.setMessage(LocalizedText.english(name));
    event.setSeverity(ushort(0));

    return event;
  }

  private static String markerName(NodeId typeDefinitionId) {
    if (NodeIds.RefreshStartEventType.equals(typeDefinitionId)) {
      return "RefreshStart";
    } else if (NodeIds.RefreshEndEventType.equals(typeDefinitionId)) {
      return "RefreshEnd";
    } else {
      return "RefreshRequired";
    }
  }
}
