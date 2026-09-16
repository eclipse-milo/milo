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

import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLimitStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;

/**
 * Behavior for an ExclusiveLimitAlarm's LimitState component (Part 9 §5.8.19.2–5.8.19.3):
 * CurrentState/LastTransition coherence over the {@link ExclusiveLimitStateMachineTypeNode}.
 *
 * <p>While the alarm is inactive the machine holds no state and CurrentState reads NULL (§5.8.19.3:
 * "When the ActiveState ... is inactive the LimitState shall not be available and shall return NULL
 * on read"). Only the four intra-side transitions (HighHigh↔High, Low↔LowLow) are modelled;
 * entering or leaving the machine, and a cross-side jump, set LastTransition to NULL.
 */
final class ExclusiveLimitStateMachine {

  private final @Nullable FiniteStateVariableTypeNode currentState;
  private final @Nullable FiniteTransitionVariableTypeNode lastTransition;

  private volatile @Nullable ExclusiveLimitState state;

  ExclusiveLimitStateMachine(@Nullable ExclusiveLimitStateMachineTypeNode node) {
    currentState = node != null ? node.getCurrentStateNode() : null;
    lastTransition = node != null ? node.getLastTransitionNode() : null;

    NodeId currentStateId = currentState != null ? currentState.getId() : null;
    for (ExclusiveLimitState limitState : ExclusiveLimitState.values()) {
      if (limitState.stateId().equals(currentStateId)) {
        state = limitState;
      }
    }

    // Seed CurrentState/LastTransition so their property nodes exist before read filters install.
    if (state == null) {
      setCurrentStateUnavailable();
    }
    if (lastTransition != null && lastTransition.getTransitionTime() == null) {
      setLastTransition(null, null, null, DateTime.NULL_VALUE);
    }
  }

  @Nullable ExclusiveLimitState getState() {
    return state;
  }

  /**
   * Transition the machine to {@code target} ({@code null} = unavailable, the alarm is inactive).
   *
   * <p>Must be called while holding the owning Condition's lock, inside a state mutation.
   */
  void setState(@Nullable ExclusiveLimitState target, DateTime time) {
    ExclusiveLimitState from = state;
    if (from == target) {
      return;
    }

    state = target;

    if (target == null) {
      setCurrentStateUnavailable();
    } else if (currentState != null) {
      currentState.setValue(new DataValue(new Variant(LocalizedText.english(target.stateName()))));
      currentState.setId(target.stateId());
    }

    setLastTransition(from, target, transitionId(from, target), time);
  }

  private void setCurrentStateUnavailable() {
    if (currentState == null) {
      return;
    }

    currentState.setValue(new DataValue(new Variant(LocalizedText.NULL_VALUE)));
    currentState.setId(NodeId.NULL_VALUE);
  }

  private void setLastTransition(
      @Nullable ExclusiveLimitState from,
      @Nullable ExclusiveLimitState to,
      @Nullable NodeId transitionId,
      DateTime time) {

    if (lastTransition == null) {
      return;
    }

    LocalizedText transitionName =
        transitionId != null && from != null && to != null
            ? LocalizedText.english(from.stateName() + "To" + to.stateName())
            : LocalizedText.NULL_VALUE;

    lastTransition.setValue(new DataValue(new Variant(transitionName)));
    lastTransition.setId(transitionId != null ? transitionId : NodeId.NULL_VALUE);
    lastTransition.setTransitionTime(time);
  }

  private static @Nullable NodeId transitionId(
      @Nullable ExclusiveLimitState from, @Nullable ExclusiveLimitState to) {

    if (from == ExclusiveLimitState.HIGH_HIGH && to == ExclusiveLimitState.HIGH) {
      return NodeIds.ExclusiveLimitStateMachineType_HighHighToHigh;
    } else if (from == ExclusiveLimitState.HIGH && to == ExclusiveLimitState.HIGH_HIGH) {
      return NodeIds.ExclusiveLimitStateMachineType_HighToHighHigh;
    } else if (from == ExclusiveLimitState.LOW_LOW && to == ExclusiveLimitState.LOW) {
      return NodeIds.ExclusiveLimitStateMachineType_LowLowToLow;
    } else if (from == ExclusiveLimitState.LOW && to == ExclusiveLimitState.LOW_LOW) {
      return NodeIds.ExclusiveLimitStateMachineType_LowToLowLow;
    } else {
      return null;
    }
  }
}
