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

import java.util.concurrent.ConcurrentLinkedDeque;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * The EventId-keyed unit of dispatch and replay for a {@link Condition}.
 *
 * <p>A branch holds its current state view, the identity of its last event (EventId and Time), and
 * a bounded window of recently issued EventIds and the acknowledge/confirm state represented by
 * each notification. Operator methods use that per-EventId state so an identifier from an older
 * state cycle cannot act on a newer cycle of the same branch.
 *
 * <p>The current state of a Condition is its <i>trunk</i> branch, identified by a NULL BranchId.
 * This is the only branch that exists today; previous states preserved as additional branches (Part
 * 9 §4.4) are future work that slots in behind {@link Condition#findBranch(ByteString)}.
 *
 * <p>Instances are owned by a {@link Condition}: mutation is guarded by that Condition's lock,
 * while the getters and {@link #acceptsEventId(ByteString)} are safe to call from any thread
 * without holding it.
 */
public class ConditionBranch {

  /**
   * Number of issued EventIds accepted by {@link #acceptsEventId(ByteString)} before the oldest is
   * evicted.
   */
  static final int EVENT_ID_WINDOW_SIZE = 32;

  private record EventState(
      ByteString eventId,
      boolean acked,
      boolean confirmed,
      long ackedStateGeneration,
      long confirmedStateGeneration) {}

  private final ConcurrentLinkedDeque<EventState> eventStateWindow = new ConcurrentLinkedDeque<>();

  private volatile ByteString lastEventId = ByteString.NULL_VALUE;
  private volatile DateTime lastEventTime = DateTime.NULL_VALUE;

  private volatile boolean acked = true;
  private volatile boolean confirmed = true;
  private volatile boolean retained = false;

  private volatile long ackedStateGeneration = 0;
  private volatile long confirmedStateGeneration = 0;

  private final @Nullable NodeId branchId;

  ConditionBranch(@Nullable NodeId branchId) {
    this.branchId = branchId;
  }

  /**
   * Get the BranchId of this branch.
   *
   * @return the BranchId of this branch, or {@code null} if this is the trunk.
   */
  public @Nullable NodeId getBranchId() {
    return branchId;
  }

  /**
   * Check if this branch is the trunk, i.e. the branch representing the Condition's current state.
   *
   * @return {@code true} if this branch is the trunk.
   */
  public boolean isTrunk() {
    return branchId == null || branchId.isNull();
  }

  /**
   * Get the EventId of the most recent event issued for this branch.
   *
   * @return the EventId of the most recent event issued for this branch, or {@link
   *     ByteString#NULL_VALUE} if no event has been issued.
   */
  public ByteString getLastEventId() {
    return lastEventId;
  }

  /**
   * Get the Time of the most recent event issued for this branch.
   *
   * @return the Time of the most recent event issued for this branch, or {@link
   *     DateTime#NULL_VALUE} if no event has been issued.
   */
  public DateTime getLastEventTime() {
    return lastEventTime;
  }

  /**
   * Check if this branch's state has been acknowledged.
   *
   * @return {@code true} if this branch's state has been acknowledged.
   */
  public boolean isAcked() {
    return acked;
  }

  /**
   * Check if this branch's state has been confirmed.
   *
   * <p>Always {@code true} for Conditions without a ConfirmedState.
   *
   * @return {@code true} if this branch's state has been confirmed.
   */
  public boolean isConfirmed() {
    return confirmed;
  }

  /**
   * Check if this branch is retained, i.e. considered interesting per the Retain property rules.
   *
   * @return {@code true} if this branch is retained.
   */
  public boolean isRetained() {
    return retained;
  }

  void setAcked(boolean acked) {
    if (this.acked != acked && !acked) {
      ackedStateGeneration++;
    }
    this.acked = acked;
  }

  /**
   * Start a new acknowledgement obligation while the branch is already unacknowledged. The
   * generation changes so EventIds from the preceding obligation cannot acknowledge the new state,
   * but AckedState itself remains false and therefore has no new transition time.
   */
  void renewAcknowledgementCycle() {
    ackedStateGeneration++;
    acked = false;
  }

  void setConfirmed(boolean confirmed) {
    if (this.confirmed != confirmed && !confirmed) {
      confirmedStateGeneration++;
    }
    this.confirmed = confirmed;
  }

  void setRetained(boolean retained) {
    this.retained = retained;
  }

  /**
   * Record an event issued for this branch: the EventId joins the bounded acceptance window and
   * becomes the last EventId/Time. Called under the owning Condition's lock (single writer).
   */
  void recordEvent(ByteString eventId, DateTime time) {
    lastEventId = eventId;
    lastEventTime = time;

    eventStateWindow.addLast(
        new EventState(eventId, acked, confirmed, ackedStateGeneration, confirmedStateGeneration));
    while (eventStateWindow.size() > EVENT_ID_WINDOW_SIZE) {
      eventStateWindow.pollFirst();
    }
  }

  /**
   * Check if {@code eventId} identifies a state of this branch, i.e. it is within the bounded
   * window of recently issued EventIds. Safe to call from any thread.
   */
  boolean acceptsEventId(ByteString eventId) {
    return findEventState(eventId) != null;
  }

  /**
   * Check whether the notification identified by {@code eventId} represents this branch's current
   * acknowledgement obligation.
   *
   * <p>Part 9 §5.7.3: "Only Event Notifications where AckedState/Id was False can be acknowledged"
   * — and the generation check additionally rejects notifications from an earlier,
   * already-satisfied acknowledgement cycle whose EventIds are still within the window.
   */
  boolean isAcknowledgeable(ByteString eventId) {
    EventState eventState = findEventState(eventId);

    return eventState != null
        && !eventState.acked()
        && !acked
        && eventState.ackedStateGeneration() == ackedStateGeneration;
  }

  /**
   * Check whether the notification identified by {@code eventId} represents this branch's current
   * confirmation obligation.
   *
   * <p>Part 9 §5.7.4: "Only Event Notifications where the Id property of the ConfirmedState is
   * False can be confirmed" — and the generation check additionally rejects notifications from an
   * earlier, already-satisfied confirmation cycle whose EventIds are still within the window.
   */
  boolean isConfirmable(ByteString eventId) {
    EventState eventState = findEventState(eventId);

    return eventState != null
        && !eventState.confirmed()
        && !confirmed
        && eventState.confirmedStateGeneration() == confirmedStateGeneration;
  }

  private @Nullable EventState findEventState(ByteString eventId) {
    for (EventState eventState : eventStateWindow) {
      if (eventState.eventId().equals(eventId)) {
        return eventState;
      }
    }

    return null;
  }
}
