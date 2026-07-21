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
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ShelvedStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.jspecify.annotations.Nullable;

/**
 * Behavior for an AlarmCondition instance: ActiveState coherence, the alarm contribution to Retain,
 * SuppressedOrShelved aggregation, and the optional shelving runtime.
 *
 * <p>The SDK does not sample the alarm's input; the application observes its process value and
 * drives {@link #setActive(boolean)}. Activation is a state needing acknowledgement: AckedState
 * transitions to {@code false} with it.
 *
 * <p>A shelved alarm keeps generating events; shelving is reflected in the SuppressedOrShelved
 * field of generated events, and clients are expected to filter on it (Part 9 §5.8.10).
 */
public class AlarmCondition extends AcknowledgeableCondition {

  static final StateTexts ACTIVE_TEXTS = new StateTexts("Active", "Inactive");

  /**
   * Optional AlarmConditionType methods with no backing state in v1 (SilenceState, SuppressedState,
   * OutOfServiceState, LatchedState, alarm groups, the {@code *2} comment variants): their instance
   * nodes are deleted rather than left exposed without semantics.
   */
  private static final List<String> UNSUPPORTED_METHODS =
      List.of(
          "Silence",
          "Suppress",
          "Suppress2",
          "Unsuppress",
          "Unsuppress2",
          "RemoveFromService",
          "RemoveFromService2",
          "PlaceInService",
          "PlaceInService2",
          "Reset",
          "Reset2",
          "GetGroupMemberships",
          "TimedShelve2",
          "Unshelve2",
          "OneShotShelve2");

  /**
   * Shadow of ActiveState/Id, kept so Retain computation and per-transition checks don't re-read
   * nodes. Updated only under the lock; volatile for unlocked reads.
   */
  private volatile boolean active;

  private final @Nullable TwoStateVariableTypeNode activeState;
  private final @Nullable ShelvingRuntime shelvingRuntime;

  /**
   * Create AlarmCondition behavior wrapping {@code node}.
   *
   * @param node the {@link AlarmConditionTypeNode} to wrap.
   */
  public AlarmCondition(AlarmConditionTypeNode node) {
    super(node);

    activeState = node.getActiveStateNode();

    ensureTwoStateDefaults(activeState, false, ACTIVE_TEXTS);
    if (node.getSuppressedOrShelved() == null) {
      node.setSuppressedOrShelved(false);
    }

    active = booleanId(activeState, false);

    installDisabledReadFilter(activeState);
    installDisabledReadFilter(node.getSuppressedOrShelvedNode());

    ShelvedStateMachineTypeNode shelvingNode = node.getShelvingStateNode();
    if (shelvingNode != null) {
      // The runtime seeds the state machine's initial values before the disabled-read filter
      // walks the subtree, and its UnshelveTime read filter is installed after it so disabled
      // gating applies before the computed value.
      shelvingRuntime = new ShelvingRuntime(this, shelvingNode);
      installDisabledReadFilter(shelvingNode);
      shelvingRuntime.installUnshelveTimeFilter();
    } else {
      shelvingRuntime = null;
    }

    initializeRetain();
  }

  /**
   * Create an AlarmCondition instance in the address space and its wrapping behavior.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure.
   * @return the created {@link AlarmCondition}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static AlarmCondition create(UaNodeContext context, Consumer<ConditionBuilder> configure)
      throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);

    return build(
        builder,
        NodeIds.AlarmConditionType,
        node -> new AlarmCondition((AlarmConditionTypeNode) node));
  }

  @Override
  public AlarmConditionTypeNode getNode() {
    return (AlarmConditionTypeNode) super.getNode();
  }

  /**
   * Check if the alarm is active.
   *
   * @return {@code true} if the alarm is active.
   */
  public boolean isActive() {
    return active;
  }

  /**
   * Set the active state of the alarm.
   *
   * <p>Activation is a state needing acknowledgement: AckedState transitions to {@code false} along
   * with ActiveState. Deactivation releases one-shot shelving. A call that does not change the
   * active state is a no-op.
   *
   * @param active the new active state.
   */
  public void setActive(boolean active) {
    runLocked(
        () -> {
          applyShelvingExpiryIfDue();

          withStateChange(
              ACTIVE_TEXTS.forState(active),
              () -> isActive() != active,
              now -> applyActive(active, now));
        });
  }

  /**
   * Apply due lazy shelving expiry, for {@code setActive}-parallel entry points (e.g. {@code
   * evaluate} in the limit subclasses): a shelving deadline that passed with no timer prompt is
   * applied before the new transition so the two state changes generate separate events.
   *
   * <p>Must be called while holding the Condition's lock.
   */
  void applyShelvingExpiryIfDue() {
    if (shelvingRuntime != null) {
      shelvingRuntime.applyExpiryIfDue();
    }
  }

  /**
   * Get the ShelvingState state machine node, present iff the alarm was built with {@link
   * ConditionBuilder#withShelving()}.
   *
   * @return the {@link ShelvedStateMachineTypeNode}, or {@code null} if the alarm has no shelving
   *     support.
   */
  public @Nullable ShelvedStateMachineTypeNode getShelvingState() {
    return shelvingRuntime != null ? shelvingRuntime.getNode() : null;
  }

  @Nullable ShelvingRuntime getShelvingRuntime() {
    return shelvingRuntime;
  }

  @Override
  Optional<UaMethodNode> findMethodNode(NodeId methodId) {
    return shelvingRuntime != null ? shelvingRuntime.findMethodNode(methodId) : Optional.empty();
  }

  @Override
  void shutdown() {
    if (shelvingRuntime != null) {
      shelvingRuntime.shutdown();
    }
  }

  @Override
  protected boolean computeRetain() {
    return active || super.computeRetain();
  }

  @Override
  ConditionSnapshot.@Nullable ShelvingSnapshot captureShelving() {
    return shelvingRuntime != null ? shelvingRuntime.captureState() : null;
  }

  @Override
  @Nullable Boolean captureActive() {
    return isActive();
  }

  @Override
  void applySnapshot(
      ConditionSnapshot snapshot,
      ConditionSnapshot.@Nullable BranchSnapshot trunkSnapshot,
      DateTime time) {

    super.applySnapshot(snapshot, trunkSnapshot, time);

    boolean restoredActive = trunkSnapshot != null && Boolean.TRUE.equals(trunkSnapshot.active());
    if (isActive() != restoredActive) {
      setTwoState(activeState, restoredActive, ACTIVE_TEXTS, time);
      active = restoredActive;
    }

    // Absent shelving state takes the §4.12 Unshelved recovery default rather than leaving a
    // previous restore's shelved state (and its armed timer) in place.
    ConditionSnapshot.ShelvingSnapshot shelving = snapshot.shelving();
    if (shelvingRuntime != null) {
      shelvingRuntime.restoreState(
          shelving != null ? shelving.state() : ShelvedState.UNSHELVED,
          shelving != null ? shelving.unshelveDeadline() : null);
    }
  }

  /**
   * Apply an ActiveState change inside a state mutation: ActiveState coherence, the
   * acknowledgement-needed transition on activation, and one-shot shelving release on deactivation.
   *
   * <p>Must be called while holding the Condition's lock, inside a {@link StateMutation}, and only
   * when the active state actually changes.
   */
  void applyActive(boolean active, DateTime time) {
    setTwoState(activeState, active, ACTIVE_TEXTS, time);
    this.active = active;

    if (active) {
      // A new activation is a state needing acknowledgement (§5.8.2).
      applyAcked(currentBranch(), false, time);
    } else if (shelvingRuntime != null) {
      // One-shot shelving releases on the active→inactive transition (§5.8.10).
      shelvingRuntime.onActiveTransition(false, time);
    }
  }

  @Override
  void installMethodHandlers(Map<QualifiedName, UaMethodNode> methodNodes) {
    super.installMethodHandlers(methodNodes);

    for (String methodName : UNSUPPORTED_METHODS) {
      UaMethodNode methodNode = methodNodes.get(new QualifiedName(0, methodName));
      if (methodNode != null) {
        methodNode.delete();
      }
    }

    if (shelvingRuntime != null) {
      shelvingRuntime.installMethodHandlers(methodNodes);
    }

    // Acknowledge-auto-silences (§5.8.2) is a no-op until SilenceState ships.
  }
}
