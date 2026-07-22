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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * An immutable snapshot of a {@link Condition}'s state, produced by {@link
 * Condition#captureSnapshot()} and consumed by {@link Condition#restoreSnapshot(ConditionSnapshot)}
 * to carry Condition state across server restarts (Part 9 §4.12).
 *
 * <p>The snapshot holds condition-level state and one {@link BranchSnapshot} per branch, trunk
 * (BranchId=NULL) first — exactly the trunk in v1. Event field values beyond the branch's last
 * EventId and Time are deliberately not carried: §5.5.7 permits regenerating a refresh replay from
 * current state, which is what restored branches do.
 *
 * <p>Any component may be {@code null} (or empty): restore substitutes the §4.12 recovery defaults
 * — Enabled, Acked and Confirmed {@code false}, Unshelved — so older or partial snapshots restore
 * safely.
 *
 * <p>Snapshots may contain operator identity (ClientUserId) and operator comments. Storage and
 * serialization — including protecting that data — are the application's concern; the SDK provides
 * no persistence backend.
 *
 * @param enabled the EnabledState, or {@code null} to default to enabled.
 * @param severity the Severity of the last event, or {@code null} if not captured.
 * @param lastSeverity the LastSeverity value, or {@code null} if not captured.
 * @param quality the Quality value, or {@code null} if not captured.
 * @param comment the Comment value, or {@code null} if not captured.
 * @param clientUserId the ClientUserId that accompanied the comment, or {@code null} if none.
 * @param shelving the shelving state, or {@code null} to default to Unshelved.
 * @param branches one {@link BranchSnapshot} per branch, trunk first; possibly empty.
 */
public record ConditionSnapshot(
    @Nullable Boolean enabled,
    @Nullable UShort severity,
    @Nullable UShort lastSeverity,
    @Nullable StatusCode quality,
    @Nullable LocalizedText comment,
    @Nullable String clientUserId,
    @Nullable ShelvingSnapshot shelving,
    List<BranchSnapshot> branches) {

  public ConditionSnapshot {
    branches = List.copyOf(branches);
  }

  /**
   * Create a snapshot with no captured state: restoring it applies the §4.12 recovery defaults
   * (Enabled, Acked and Confirmed {@code false}, Unshelved, no event identity).
   *
   * @return an empty {@link ConditionSnapshot}.
   */
  public static ConditionSnapshot empty() {
    return new ConditionSnapshot(null, null, null, null, null, null, null, List.of());
  }

  /**
   * Get the trunk branch snapshot, i.e. the one with a NULL BranchId.
   *
   * @return the trunk {@link BranchSnapshot}, if present.
   */
  public Optional<BranchSnapshot> trunk() {
    return branches.stream()
        .filter(branch -> branch.branchId() == null || branch.branchId().isNull())
        .findFirst();
  }

  /**
   * The shelving state of an alarm's ShelvedStateMachine.
   *
   * @param state the shelved state.
   * @param unshelveDeadline the absolute time at which shelving expires, or {@code null} if no
   *     deadline applies. A deadline that passed while the server was down is restored as-is: the
   *     alarm unshelves lazily on its first touch.
   */
  public record ShelvingSnapshot(ShelvedState state, @Nullable DateTime unshelveDeadline) {}

  /**
   * The state of one {@link ConditionBranch}.
   *
   * @param branchId the BranchId, or {@code null} for the trunk.
   * @param acked the acknowledged state, or {@code null} to default to {@code false} (§4.12).
   * @param confirmed the confirmed state, or {@code null} to default to {@code false} (§4.12).
   * @param active the alarm active state, or {@code null} for non-alarm Conditions (defaults to
   *     inactive on restore).
   * @param activeLimits the violated limits of a limit alarm: at most one for an exclusive alarm,
   *     any combination for a non-exclusive alarm, empty when inactive or not a limit alarm.
   * @param retained the branch's Retain state; recomputed from the restored state for the trunk.
   * @param lastEventId the EventId of the branch's last event, or {@code null} if none.
   * @param lastEventTime the Time of the branch's last event, or {@code null} if none.
   * @param eventIdWindow the branch's accepted-EventId window, oldest first.
   */
  public record BranchSnapshot(
      @Nullable NodeId branchId,
      @Nullable Boolean acked,
      @Nullable Boolean confirmed,
      @Nullable Boolean active,
      Set<ExclusiveLimitState> activeLimits,
      boolean retained,
      @Nullable ByteString lastEventId,
      @Nullable DateTime lastEventTime,
      List<AcceptedEventId> eventIdWindow) {

    public BranchSnapshot {
      activeLimits = Set.copyOf(activeLimits);
      eventIdWindow = List.copyOf(eventIdWindow);
    }
  }

  /**
   * One entry of a branch's accepted-EventId window: the EventId and the acknowledge/confirm state
   * its notification reported, so operator methods keyed by a restored EventId apply the same
   * obligation-cycle checks as before capture (§5.7.3/§5.7.4).
   *
   * @param eventId the EventId the notification carried.
   * @param acked the AckedState/Id the notification reported.
   * @param confirmed the ConfirmedState/Id the notification reported.
   * @param ackedCurrentCycle whether the notification belongs to the branch's current
   *     acknowledgement obligation cycle.
   * @param confirmedCurrentCycle whether the notification belongs to the branch's current
   *     confirmation obligation cycle.
   */
  public record AcceptedEventId(
      ByteString eventId,
      boolean acked,
      boolean confirmed,
      boolean ackedCurrentCycle,
      boolean confirmedCurrentCycle) {}
}
