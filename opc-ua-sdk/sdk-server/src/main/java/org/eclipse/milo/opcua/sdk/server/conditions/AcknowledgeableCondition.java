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
import org.eclipse.milo.opcua.sdk.server.conditions.ConditionNodeTraversal.DiscoveredMethod;
import org.eclipse.milo.opcua.sdk.server.conditions.ConditionNodeTraversal.MethodSurface;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.AcknowledgeableConditionType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AcknowledgeableConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * Behavior for an AcknowledgeableCondition instance: AckedState (mandatory) and opt-in
 * ConfirmedState coherence, their contribution to Retain, and the default Acknowledge/Confirm
 * method semantics.
 *
 * <p>Retain is {@code ¬acked ∨ ¬confirmed}-shaped at this level of the hierarchy; alarm activity
 * joins the formula in the alarm subclasses.
 */
public class AcknowledgeableCondition extends Condition {

  static final StateTexts ACKED_TEXTS = new StateTexts("Acknowledged", "Unacknowledged");
  static final StateTexts CONFIRMED_TEXTS = new StateTexts("Confirmed", "Unconfirmed");

  private final @Nullable TwoStateVariableTypeNode ackedState;
  private final @Nullable TwoStateVariableTypeNode confirmedState;

  /**
   * Create AcknowledgeableCondition behavior wrapping {@code node}.
   *
   * @param node the {@link AcknowledgeableConditionTypeNode} to wrap.
   */
  public AcknowledgeableCondition(AcknowledgeableConditionTypeNode node) {
    super(node);

    ackedState = node.getAckedStateNode();
    confirmedState = node.getConfirmedStateNode();

    ensureTwoStateDefaults(ackedState, true, ACKED_TEXTS);
    ensureTwoStateDefaults(confirmedState, true, CONFIRMED_TEXTS);

    installDisabledReadFilter(ackedState);
    installDisabledReadFilter(confirmedState);

    currentBranch().setAcked(isAcked());
    currentBranch().setConfirmed(isConfirmed());

    // Recompute Retain from the seeded state: a wrapped pre-existing node's stored Retain
    // property may disagree with its AckedState/ConfirmedState (e.g. unset while unacked).
    initializeRetain();
  }

  /**
   * Create an AcknowledgeableCondition instance in the address space and its wrapping behavior.
   *
   * <p>The instance node is created via the server's node instantiator from the
   * AcknowledgeableConditionType type definition; method handlers are installed on the instance's
   * method nodes iff their backing state exists.
   *
   * @param context the {@link UaNodeContext} the instance is created under.
   * @param configure receives the {@link ConditionBuilder} to configure.
   * @return the created {@link AcknowledgeableCondition}.
   * @throws UaException if instantiating the instance node fails.
   */
  public static AcknowledgeableCondition create(
      UaNodeContext context, Consumer<ConditionBuilder> configure) throws UaException {

    ConditionBuilder builder = new ConditionBuilder(context);
    configure.accept(builder);

    return build(
        builder,
        NodeIds.AcknowledgeableConditionType,
        node -> new AcknowledgeableCondition((AcknowledgeableConditionTypeNode) node));
  }

  /**
   * Attach behavior to a complete, pre-existing AcknowledgeableCondition instance.
   *
   * <p>The node's identity and state are retained. The returned behavior is not registered; pass it
   * to {@link ConditionManager#register(Condition)} before serving client calls.
   *
   * @param node the complete generated typed node.
   * @return the attached behavior.
   */
  public static AcknowledgeableCondition attach(AcknowledgeableConditionTypeNode node) {
    return attach(node, options -> {});
  }

  /**
   * Attach behavior to a complete, pre-existing AcknowledgeableCondition instance.
   *
   * @param node the complete generated typed node.
   * @param configure receives source-wiring options.
   * @return the attached behavior.
   */
  public static AcknowledgeableCondition attach(
      AcknowledgeableConditionTypeNode node, Consumer<AttachOptions> configure) {

    return attach(
        node,
        configure,
        attached -> new AcknowledgeableCondition((AcknowledgeableConditionTypeNode) attached));
  }

  /**
   * Complete and attach behavior to a pre-existing AcknowledgeableCondition instance.
   *
   * <p>Existing identities and stored values are preserved unless explicitly overridden. The
   * returned behavior is not registered.
   *
   * @param context the context whose NodeManager owns the loaded instance.
   * @param nodeId the existing ConditionId.
   * @param configure receives the adopt-mode builder.
   * @return the adopted behavior.
   * @throws UaException if validation or in-place completion fails.
   */
  public static AcknowledgeableCondition adopt(
      UaNodeContext context, NodeId nodeId, Consumer<ConditionBuilder> configure)
      throws UaException {

    ConditionBuilder builder =
        ConditionBuilder.forAdoption(context, nodeId, AcknowledgeableConditionTypeNode.class);
    configure.accept(builder);

    return build(
        builder,
        NodeIds.AcknowledgeableConditionType,
        node -> new AcknowledgeableCondition((AcknowledgeableConditionTypeNode) node));
  }

  @Override
  public AcknowledgeableConditionTypeNode getNode() {
    return (AcknowledgeableConditionTypeNode) super.getNode();
  }

  /**
   * Check if the Condition's current state (the trunk) is acknowledged.
   *
   * @return {@code true} if the current state is acknowledged.
   */
  public boolean isAcked() {
    return booleanId(ackedState, true);
  }

  /**
   * Check if the Condition's current state (the trunk) is confirmed.
   *
   * <p>Always {@code true} when the Condition has no ConfirmedState.
   *
   * @return {@code true} if the current state is confirmed.
   */
  public boolean isConfirmed() {
    return booleanId(confirmedState, true);
  }

  /**
   * Check if this Condition was built with the optional ConfirmedState.
   *
   * @return {@code true} if the ConfirmedState component exists.
   */
  public boolean hasConfirmedState() {
    return confirmedState != null;
  }

  /**
   * Set the acknowledged state of the Condition's current state (the trunk).
   *
   * <p>Applications drive this {@code true}→{@code false} when a new state needing acknowledgement
   * occurs; operators drive it back via the Acknowledge method.
   *
   * @param acked the new acknowledged state.
   */
  public void setAcked(boolean acked) {
    withStateChange(
        ACKED_TEXTS.forState(acked),
        () -> currentBranch().isAcked() != acked,
        now -> applyAcked(currentBranch(), acked, now));
  }

  /**
   * Set the confirmed state of the Condition's current state (the trunk).
   *
   * <p>No-op if the Condition has no ConfirmedState.
   *
   * @param confirmed the new confirmed state.
   */
  public void setConfirmed(boolean confirmed) {
    if (!hasConfirmedState()) {
      return;
    }

    withStateChange(
        CONFIRMED_TEXTS.forState(confirmed),
        () -> currentBranch().isConfirmed() != confirmed,
        now -> applyConfirmed(currentBranch(), confirmed, now));
  }

  @Override
  protected boolean computeRetain() {
    ConditionBranch trunk = currentBranch();
    return !trunk.isAcked() || (hasConfirmedState() && !trunk.isConfirmed());
  }

  @Override
  void applySnapshot(
      ConditionSnapshot snapshot,
      ConditionSnapshot.@Nullable BranchSnapshot trunkSnapshot,
      DateTime time) {

    super.applySnapshot(snapshot, trunkSnapshot, time);

    // The trunk branch was restored (with the §4.12 Acked/Confirmed=false recovery defaults for
    // absent fields) before this hook runs; mirror its state into the node's TwoStateVariables.
    setTwoState(ackedState, currentBranch().isAcked(), ACKED_TEXTS, time);
    if (hasConfirmedState()) {
      setTwoState(confirmedState, currentBranch().isConfirmed(), CONFIRMED_TEXTS, time);
    } else {
      // Without the optional ConfirmedState the branch invariant is confirmed == true; the §4.12
      // Confirmed=false default the branch restore applied would contradict isConfirmed().
      currentBranch().setConfirmed(true);
    }
  }

  void applyAcked(ConditionBranch branch, boolean acked, DateTime time) {
    boolean stateChanged = branch.isAcked() != acked;

    if (!acked && !stateChanged) {
      // A new alarm state can need a fresh acknowledgement while AckedState already reads false.
      // Renew the EventId generation without inventing a false→false TwoStateVariable transition.
      branch.renewAcknowledgementCycle();
    } else {
      branch.setAcked(acked);
    }

    if (branch.isTrunk() && stateChanged) {
      setTwoState(ackedState, acked, ACKED_TEXTS, time);
    }
  }

  void applyConfirmed(ConditionBranch branch, boolean confirmed, DateTime time) {
    branch.setConfirmed(confirmed);

    if (branch.isTrunk()) {
      setTwoState(confirmedState, confirmed, CONFIRMED_TEXTS, time);
    }
  }

  void handleAcknowledge(
      InvocationContext context, ByteString eventId, @Nullable LocalizedText comment)
      throws UaException {

    handleBranchMethod(
        eventId,
        ACKED_TEXTS.whenTrue(),
        branch -> {
          if (!branch.isAcknowledgeable(eventId)) {
            throw new UaException(StatusCodes.Bad_ConditionBranchAlreadyAcked);
          }
        },
        branch -> getInterceptor().onAcknowledge(context, branch, comment),
        (branch, now) -> {
          applyAcked(branch, true, now);
          if (hasConfirmedState() && branch.isConfirmed()) {
            // Acknowledgement opens the confirm cycle: the state is acked but unconfirmed.
            applyConfirmed(branch, false, now);
          }
          applyComment(comment, clientUserId(context), now);
        });

    // Dormant audit emission point: AuditConditionAcknowledgeEventType.
    // Acknowledge-auto-silences (§5.8.2) is a no-op until SilenceState ships.
  }

  void handleConfirm(InvocationContext context, ByteString eventId, @Nullable LocalizedText comment)
      throws UaException {

    handleBranchMethod(
        eventId,
        CONFIRMED_TEXTS.whenTrue(),
        branch -> {
          if (!branch.isConfirmable(eventId)) {
            throw new UaException(StatusCodes.Bad_ConditionBranchAlreadyConfirmed);
          }
        },
        branch -> getInterceptor().onConfirm(context, branch, comment),
        (branch, now) -> {
          applyConfirmed(branch, true, now);
          applyComment(comment, clientUserId(context), now);
        });

    // Dormant audit emission point: AuditConditionConfirmEventType.
  }

  @Override
  void installMethodHandlers(MethodSurface methodSurface) {
    super.installMethodHandlers(methodSurface);

    DiscoveredMethod acknowledge = methodSurface.get("Acknowledge");
    if (acknowledge != null) {
      installMethodHandler(
          acknowledge,
          new AcknowledgeableConditionType.AcknowledgeMethod(acknowledge.node()) {
            @Override
            protected void invoke(
                InvocationContext context, ByteString eventId, LocalizedText comment)
                throws UaException {
              handleAcknowledge(context, eventId, comment);
            }
          });
    }

    DiscoveredMethod confirm = methodSurface.get("Confirm");
    if (confirm != null) {
      if (hasConfirmedState()) {
        installMethodHandler(
            confirm,
            new AcknowledgeableConditionType.ConfirmMethod(confirm.node()) {
              @Override
              protected void invoke(
                  InvocationContext context, ByteString eventId, LocalizedText comment)
                  throws UaException {
                handleConfirm(context, eventId, comment);
              }
            });
      } else {
        // No backing ConfirmedState: the method is not provided by this instance.
        deleteUnsupportedMethod(methodSurface, "Confirm");
      }
    }
  }
}
