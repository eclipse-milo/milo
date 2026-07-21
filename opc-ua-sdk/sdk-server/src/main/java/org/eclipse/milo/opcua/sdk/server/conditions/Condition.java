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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConditionType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ConditionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.jspecify.annotations.Nullable;

/**
 * Behavior for a Condition instance: state-transition coherence across the instance's
 * TwoStateVariables and ConditionVariables, Retain computation, EventId minting, and event
 * generation per Part 9 §5.5.2–5.5.3.
 *
 * <p>The Condition instance node itself is the event node: transitions update the node's variables
 * and fire it through the Server's event notifier, extracting fields synchronously while the
 * Condition's lock is held.
 *
 * <p>Events are only generated while Retain is {@code true}, plus the single Retain {@code
 * true}→{@code false} retraction (§5.5.2).
 *
 * <p>All state transitions are serialized by one coarse re-entrant lock per Condition.
 */
public class Condition {

  private static final ConditionMethodInterceptor NOOP_INTERCEPTOR =
      new ConditionMethodInterceptor() {};

  /**
   * The Value texts of a TwoStateVariable's true and false states; also used as the Message of
   * events generated for the state's transitions.
   */
  record StateTexts(String whenTrue, String whenFalse) {
    String forState(boolean state) {
      return state ? whenTrue : whenFalse;
    }
  }

  static final StateTexts ENABLED_TEXTS = new StateTexts("Enabled", "Disabled");

  private final ReentrantLock lock = new ReentrantLock();

  private final ConditionBranch trunk = new ConditionBranch(null);

  private volatile ConditionMethodInterceptor interceptor = NOOP_INTERCEPTOR;

  /**
   * Shadow of EnabledState/Id, kept so the disabled-read filter and per-transition checks don't
   * re-resolve nodes by reference scans. Updated only under the lock; volatile for filter reads.
   */
  private volatile boolean enabled;

  private final ConditionTypeNode node;
  private final OpcUaServer server;

  private final @Nullable TwoStateVariableTypeNode enabledState;
  private final @Nullable ConditionVariableTypeNode quality;
  private final @Nullable ConditionVariableTypeNode lastSeverity;
  private final @Nullable ConditionVariableTypeNode comment;

  /**
   * Create Condition behavior wrapping {@code node}.
   *
   * <p>Missing state defaults (EnabledState, variable SourceTimestamps) are populated, and reads of
   * the node's condition variables are gated to return {@code Bad_ConditionDisabled} while the
   * Condition is disabled.
   *
   * @param node the {@link ConditionTypeNode} to wrap.
   */
  public Condition(ConditionTypeNode node) {
    this.node = node;
    this.server = node.getNodeContext().getServer();

    enabledState = node.getEnabledStateNode();
    quality = node.getQualityNode();
    lastSeverity = node.getLastSeverityNode();
    comment = node.getCommentNode();

    ensureTwoStateDefaults(enabledState, true, ENABLED_TEXTS);
    ensureConditionVariableDefaults(quality, new Variant(StatusCode.GOOD));
    ensureConditionVariableDefaults(lastSeverity, new Variant(UShort.MIN));
    ensureConditionVariableDefaults(comment, new Variant(LocalizedText.NULL_VALUE));

    enabled = booleanId(enabledState, true);

    installDisabledReadFilter(quality);
    installDisabledReadFilter(lastSeverity);
    installDisabledReadFilter(comment);

    initializeRetain();
  }

  /**
   * Recompute Retain from current state and store it on the node and trunk without generating an
   * event.
   *
   * <p>Called at construction — subclasses call it again after seeding their state — so a wrapped
   * pre-existing node's state variables and its Retain property agree from the start, rather than
   * trusting a possibly stale stored Retain value.
   */
  protected final void initializeRetain() {
    boolean retained = isEnabled() && computeRetain();
    node.setRetain(retained);
    trunk.setRetained(retained);
  }

  /**
   * Get the ConditionId, i.e. the {@link NodeId} of the Condition instance node.
   *
   * @return the ConditionId.
   */
  public NodeId getConditionId() {
    return node.getNodeId();
  }

  /**
   * Get the Condition instance node this behavior wraps.
   *
   * @return the {@link ConditionTypeNode} this behavior wraps.
   */
  public ConditionTypeNode getNode() {
    return node;
  }

  /**
   * Shared tail of the condition/alarm {@code create} entry points: instantiate the typed instance
   * node from {@code typeDefinitionId}, wrap it in its behavior via {@code behavior}, and install
   * the instance's method handlers. The {@code behavior} function re-casts the node to the concrete
   * type it wraps, so a single {@link ConditionTypeNode}-typed helper serves every alarm family.
   *
   * @param builder the configured {@link ConditionBuilder}.
   * @param typeDefinitionId the type definition to instantiate.
   * @param behavior wraps the instantiated node in its behavior class.
   * @return the created behavior.
   * @throws UaException if instantiating the instance node fails.
   */
  static <T extends Condition> T build(
      ConditionBuilder builder, NodeId typeDefinitionId, Function<ConditionTypeNode, T> behavior)
      throws UaException {

    ConditionTypeNode node = builder.buildNode(typeDefinitionId);

    T condition = behavior.apply(node);
    condition.installMethodHandlers(builder.getMethodNodes());

    return condition;
  }

  /**
   * Get the branch representing the Condition's current state (the trunk, BranchId=NULL).
   *
   * @return the trunk {@link ConditionBranch}.
   */
  public ConditionBranch currentBranch() {
    return trunk;
  }

  /**
   * Find the {@link ConditionBranch} that {@code eventId} identifies a state of.
   *
   * <p>Safe to call from any thread; the Condition's lock is not acquired.
   *
   * @param eventId an EventId from an event previously issued for this Condition.
   * @return the {@link ConditionBranch} that accepted {@code eventId}, if any.
   */
  public Optional<ConditionBranch> findBranch(ByteString eventId) {
    return trunk.acceptsEventId(eventId) ? Optional.of(trunk) : Optional.empty();
  }

  /**
   * Set the {@link ConditionMethodInterceptor} consulted before default method semantics apply.
   *
   * @param interceptor the {@link ConditionMethodInterceptor} to set.
   */
  public void setInterceptor(ConditionMethodInterceptor interceptor) {
    this.interceptor = interceptor;
  }

  /**
   * Check if this Condition is enabled.
   *
   * @return {@code true} if this Condition is enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Check if this Condition is retained, i.e. considered interesting per the Retain property.
   *
   * @return {@code true} if this Condition is retained.
   */
  public boolean isRetained() {
    return trunk.isRetained();
  }

  /**
   * Set the Quality of this Condition.
   *
   * <p>Updates the Quality ConditionVariable and its SourceTimestamp and generates an event per
   * §5.5.2. A call that does not change the stored Quality is a no-op: §5.5.2 keys event generation
   * to <i>changes</i>, so re-asserting the same value must not generate events (or consume EventId
   * window slots).
   *
   * @param quality the new Quality.
   */
  public void setQuality(StatusCode quality) {
    withStateChange(
        "Quality changed",
        () -> this.quality != null && !Objects.equals(currentValue(this.quality), quality),
        now -> setConditionVariable(this.quality, new Variant(quality), now));
  }

  /**
   * Set the Severity of this Condition.
   *
   * <p>Maintains LastSeverity (the Severity of the last issued event) and generates an event per
   * §5.5.2. A call that does not change the Severity is a no-op.
   *
   * @param severity the new Severity.
   */
  public void setSeverity(UShort severity) {
    withStateChange(
        "Severity changed",
        () -> !Objects.equals(node.getSeverity(), severity),
        now -> applySeverity(severity, now));
  }

  /**
   * Apply a Severity change inside a state mutation, maintaining LastSeverity (the Severity of the
   * last issued event); a call that does not change the Severity is a no-op.
   *
   * <p>Must be called while holding the Condition's lock, inside a {@link StateMutation}.
   */
  void applySeverity(UShort severity, DateTime time) {
    UShort previousSeverity = node.getSeverity();
    if (Objects.equals(previousSeverity, severity)) {
      return;
    }

    if (previousSeverity != null) {
      setConditionVariable(lastSeverity, new Variant(previousSeverity), time);
    }
    node.setSeverity(severity);
  }

  /**
   * Set the Comment of this Condition, applying the NULL-comment convention (§5.5.6): a NULL
   * comment leaves the stored Comment unchanged, so a NULL comment is a complete no-op here;
   * clearing requires empty text with a locale.
   *
   * @param comment the comment to apply.
   * @param clientUserId the ClientUserId to record for the change, may be {@code null}.
   */
  public void setComment(@Nullable LocalizedText comment, @Nullable String clientUserId) {
    withStateChange(
        "Comment changed",
        () -> comment != null && (comment.text() != null || comment.locale() != null),
        now -> applyComment(comment, clientUserId, now));
  }

  /** The current value stored in {@code variable}, or {@code null} if none. */
  static @Nullable Object currentValue(UaVariableNode variable) {
    DataValue value = variable.getValue();
    return value != null ? value.getValue().getValue() : null;
  }

  /**
   * Compute the value of the Retain property from current state.
   *
   * <p>The base Condition has no state contributing to Retain; subclasses override with their
   * type's formula.
   *
   * @return the computed Retain value.
   */
  protected boolean computeRetain() {
    return false;
  }

  /**
   * Apply {@code mutation} under the Condition's lock, recompute Retain, and generate an event if
   * the emission rules of §5.5.2 call for one: an event while Retain is {@code true}, or the single
   * retraction when Retain transitions {@code true}→{@code false}.
   *
   * <p>This single rule also yields the Disable retraction (Retain was {@code true} before the
   * mutation) and silence while disabled (Retain is pinned {@code false}, so both terms are {@code
   * false} for any later mutation): values change (and survive re-enable) but no event is
   * generated.
   *
   * @param message the Message for the generated event, if one is generated.
   * @param mutation the state mutation to apply; receives the transition time.
   */
  protected void withStateChange(String message, StateMutation mutation) {
    lock.lock();
    try {
      DateTime now = DateTime.now();

      boolean wasRetained = trunk.isRetained();

      mutation.apply(now);

      boolean nowRetained = isEnabled() && computeRetain();
      node.setRetain(nowRetained);
      trunk.setRetained(nowRetained);

      if (nowRetained || wasRetained) {
        fireEvent(message, now);
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Like {@link #withStateChange(String, StateMutation)}, but the mutation is applied only if
   * {@code changeGuard} evaluates {@code true} under the Condition's lock; otherwise the call is a
   * complete, silent no-op — no state change, no Retain recomputation, no event.
   *
   * @param message the Message for the generated event, if one is generated.
   * @param changeGuard evaluated under the lock; {@code true} iff the mutation would change state.
   * @param mutation the state mutation to apply; receives the transition time.
   */
  protected void withStateChange(
      String message, BooleanSupplier changeGuard, StateMutation mutation) {
    lock.lock();
    try {
      if (changeGuard.getAsBoolean()) {
        withStateChange(message, mutation);
      }
    } finally {
      lock.unlock();
    }
  }

  /** A state mutation applied under the Condition's lock at transition time {@code time}. */
  @FunctionalInterface
  protected interface StateMutation {

    /**
     * Apply the mutation.
     *
     * @param time the transition time shared by all variables this mutation touches.
     */
    void apply(DateTime time);
  }

  /**
   * Create refresh replay snapshots: one immutable snapshot per Retained branch, trunk first, each
   * materializing a never-registered event node carrying this Condition's NodeId and the branch's
   * last EventId/Time; empty if nothing is retained.
   *
   * <p>This is the internal replay contract between a Condition and the ConditionManager (the
   * design's {@code RetainedConditionSource}); v1 has exactly the trunk. Snapshots are taken under
   * the Condition's lock so their values are mutually consistent, and must be {@link
   * ConditionEventSnapshot#delete() deleted} by the caller after delivery.
   *
   * @return one {@link ConditionEventSnapshot} per Retained branch.
   * @throws UaException if materializing a snapshot fails.
   */
  List<ConditionEventSnapshot> createRefreshSnapshots() throws UaException {
    lock.lock();
    try {
      if (!trunk.isRetained()) {
        return List.of();
      }

      return List.of(ConditionEventSnapshot.create(server, node, trunk));
    } finally {
      lock.unlock();
    }
  }

  /**
   * Mint a unique EventId, record it on the trunk, and fire the Condition instance node through the
   * Server's event notifier.
   *
   * <p>Must be called while holding the Condition's lock: fields are extracted synchronously inside
   * {@code fire()}, so queued notifications cannot observe later mutations.
   */
  void fireEvent(String message, DateTime time) {
    ByteString eventId = NonceUtil.generateNonce(16);

    node.setEventId(eventId);
    node.setTime(time);
    node.setMessage(LocalizedText.english(message));

    trunk.recordEvent(eventId, time);

    server.getEventNotifier().fire(node);
  }

  void handleEnable(InvocationContext context) throws UaException {
    handleSetEnabled(context, true);
  }

  void handleDisable(InvocationContext context) throws UaException {
    handleSetEnabled(context, false);
  }

  private void handleSetEnabled(InvocationContext context, boolean enable) throws UaException {
    lock.lock();
    try {
      if (isEnabled() == enable) {
        throw new UaException(
            enable
                ? StatusCodes.Bad_ConditionAlreadyEnabled
                : StatusCodes.Bad_ConditionAlreadyDisabled);
      }

      InterceptorCall hook =
          enable ? () -> interceptor.onEnable(context) : () -> interceptor.onDisable(context);

      if (consultInterceptor(hook) == ConditionMethodInterceptor.Outcome.HANDLED) {
        return;
      }

      // Enable: per-branch re-assertion — the event fires iff Retain becomes true (v1 has exactly
      // the trunk). Disable: the single Retain true→false retraction fires iff the Condition was
      // retained. Both are the one emission rule in withStateChange (§5.5.2).
      withStateChange(ENABLED_TEXTS.forState(enable), now -> setEnabledState(enable, now));

      // Dormant audit emission point: AuditConditionEnableEventType.
    } finally {
      lock.unlock();
    }
  }

  void handleAddComment(
      InvocationContext context, ByteString eventId, @Nullable LocalizedText comment)
      throws UaException {

    handleBranchMethod(
        eventId,
        "Comment added",
        branch -> {
          // §5.5.6 (clarified by Mantis 9675): a NULL comment to AddComment shall be ignored and
          // Bad_InvalidArgument returned. Acknowledge and Confirm tolerate NULL (§5.7.3/§5.7.4).
          if (comment == null || comment.isNull()) {
            throw new UaException(StatusCodes.Bad_InvalidArgument);
          }
        },
        branch -> interceptor.onAddComment(context, branch, comment),
        (branch, now) -> applyComment(comment, clientUserId(context), now));

    // Dormant audit emission point: AuditConditionCommentEventType.
  }

  /**
   * Shared template for the operator methods keyed by EventId (AddComment, Acknowledge, Confirm,
   * …): disabled gating, branch resolution, a per-method precondition, the interceptor consult,
   * then the default state transition through {@link #withStateChange}.
   */
  void handleBranchMethod(
      ByteString eventId,
      String message,
      BranchPrecondition precondition,
      InterceptorConsult hook,
      BranchMutation mutation)
      throws UaException {

    lock.lock();
    try {
      if (!isEnabled()) {
        throw new UaException(StatusCodes.Bad_ConditionDisabled);
      }

      ConditionBranch branch =
          findBranch(eventId).orElseThrow(() -> new UaException(StatusCodes.Bad_EventIdUnknown));

      precondition.check(branch);

      if (consultInterceptor(() -> hook.consult(branch))
          == ConditionMethodInterceptor.Outcome.HANDLED) {
        return;
      }

      withStateChange(message, now -> mutation.apply(branch, now));
    } finally {
      lock.unlock();
    }
  }

  @FunctionalInterface
  interface BranchPrecondition {
    void check(ConditionBranch branch) throws UaException;
  }

  @FunctionalInterface
  interface InterceptorConsult {
    ConditionMethodInterceptor.Outcome consult(ConditionBranch branch) throws UaException;
  }

  @FunctionalInterface
  interface BranchMutation {
    void apply(ConditionBranch branch, DateTime time);
  }

  /**
   * Install this Condition's method handlers on the instance method nodes recorded during
   * instantiation, iff their backing state exists.
   *
   * <p>Subclasses override to install their own level's handlers and call {@code super}.
   *
   * @param methodNodes the instance's method nodes, keyed by BrowseName.
   */
  void installMethodHandlers(Map<QualifiedName, UaMethodNode> methodNodes) {
    UaMethodNode enable = methodNodes.get(new QualifiedName(0, "Enable"));
    if (enable != null) {
      enable.setInvocationHandler(
          new ConditionType.EnableMethod(enable) {
            @Override
            protected void invoke(InvocationContext context) throws UaException {
              handleEnable(context);
            }
          });
    }

    UaMethodNode disable = methodNodes.get(new QualifiedName(0, "Disable"));
    if (disable != null) {
      disable.setInvocationHandler(
          new ConditionType.DisableMethod(disable) {
            @Override
            protected void invoke(InvocationContext context) throws UaException {
              handleDisable(context);
            }
          });
    }

    UaMethodNode addComment = methodNodes.get(new QualifiedName(0, "AddComment"));
    if (addComment != null) {
      addComment.setInvocationHandler(
          new ConditionType.AddCommentMethod(addComment) {
            @Override
            protected void invoke(
                InvocationContext context, ByteString eventId, LocalizedText comment)
                throws UaException {
              handleAddComment(context, eventId, comment);
            }
          });
    }
  }

  /**
   * Find a Method exposed through ConditionId dispatch but owned by a nested state machine.
   * Subclasses override for the nested methods they support.
   */
  Optional<UaMethodNode> findMethodNode(NodeId methodId) {
    return Optional.empty();
  }

  /** Release runtime resources owned by this Condition during server shutdown. */
  void shutdown() {}

  /**
   * Apply the NULL-comment convention used by comment-taking methods: a NULL comment leaves the
   * stored Comment unchanged; any non-NULL comment (including empty text with a locale) is stored.
   * AddComment rejects NULL before reaching this helper, while Acknowledge and Confirm ignore it.
   * The ClientUserId is recorded for every accepted comment-taking action.
   */
  void applyComment(@Nullable LocalizedText comment, @Nullable String clientUserId, DateTime time) {
    if (comment != null && comment.isNotNull()) {
      setConditionVariable(this.comment, new Variant(comment), time);
    }
    node.setClientUserId(clientUserId);
  }

  /**
   * Consult the interceptor, mapping unexpected {@link RuntimeException}s to {@code
   * Bad_InternalError} so default semantics are not applied and state is unchanged.
   */
  ConditionMethodInterceptor.Outcome consultInterceptor(InterceptorCall call) throws UaException {
    try {
      return call.invoke();
    } catch (RuntimeException e) {
      throw new UaException(StatusCodes.Bad_InternalError, e);
    }
  }

  @FunctionalInterface
  interface InterceptorCall {
    ConditionMethodInterceptor.Outcome invoke() throws UaException;
  }

  ConditionMethodInterceptor getInterceptor() {
    return interceptor;
  }

  /**
   * Run {@code action} while holding the lock serializing this Condition's state transitions, for
   * behavior collaborators (e.g. the shelving runtime) that must validate and mutate atomically.
   *
   * @param action the action to run under the lock.
   * @param <E> the checked exception the action may throw.
   * @throws E if the action throws.
   */
  <E extends Exception> void runLocked(LockedAction<E> action) throws E {
    lock.lock();
    try {
      action.run();
    } finally {
      lock.unlock();
    }
  }

  @FunctionalInterface
  interface LockedAction<E extends Exception> {
    void run() throws E;
  }

  OpcUaServer getServer() {
    return server;
  }

  static @Nullable String clientUserId(InvocationContext context) {
    return context.getSession().map(Session::getClientUserId).orElse(null);
  }

  /** Set EnabledState coherently and update the shadow read by the disabled-read filter. */
  private void setEnabledState(boolean value, DateTime time) {
    setTwoState(enabledState, value, ENABLED_TEXTS, time);
    enabled = value;
  }

  /**
   * Set a TwoStateVariable coherently: Value text, Id, and TransitionTime share one transition
   * time, and the optional EffectiveDisplayName, where present, tracks the Value text.
   */
  void setTwoState(
      @Nullable TwoStateVariableTypeNode state, boolean value, StateTexts texts, DateTime time) {

    if (state == null) {
      return;
    }

    LocalizedText text = LocalizedText.english(texts.forState(value));

    state.setValue(new DataValue(new Variant(text)));
    state.setId(value);
    state.setTransitionTime(time);

    PropertyTypeNode effectiveDisplayName = state.getEffectiveDisplayNameNode();
    if (effectiveDisplayName != null) {
      effectiveDisplayName.setValue(new DataValue(new Variant(text)));
    }
  }

  /** Set a ConditionVariable's value and its SourceTimestamp. */
  void setConditionVariable(
      @Nullable ConditionVariableTypeNode variable, Variant value, DateTime time) {
    if (variable == null) {
      return;
    }

    variable.setValue(new DataValue(value));
    variable.setSourceTimestamp(time);
  }

  /**
   * Resolve a TwoStateVariable's boolean Id, treating a null node or an unset Id as {@code
   * defaultValue}. The default differs by state — EnabledState/AckedState/ConfirmedState default
   * {@code true}, ActiveState defaults {@code false} — so it is spelled out at each call site.
   */
  static boolean booleanId(@Nullable TwoStateVariableTypeNode state, boolean defaultValue) {
    Boolean id = state != null ? state.getId() : null;
    return id != null ? id : defaultValue;
  }

  /**
   * Populate a TwoStateVariable's Id, Value, and TransitionTime if it has never been set, so its
   * property nodes exist before disabled-read filters are installed.
   */
  final void ensureTwoStateDefaults(
      @Nullable TwoStateVariableTypeNode state, boolean value, StateTexts texts) {

    if (state != null && state.getId() == null) {
      setTwoState(state, value, texts, DateTime.now());
    }
  }

  /**
   * Populate a ConditionVariable's value and SourceTimestamp if it has never been set, so its
   * property nodes exist before disabled-read filters are installed.
   */
  final void ensureConditionVariableDefaults(
      @Nullable ConditionVariableTypeNode variable, Variant initialValue) {

    if (variable != null && variable.getSourceTimestamp() == null) {
      setConditionVariable(variable, initialValue, DateTime.now());
    }
  }

  /**
   * Gate reads of the variable subtree rooted at {@code root}: while the Condition is disabled, the
   * Value attribute of every variable node in the subtree reads {@code Bad_ConditionDisabled}
   * (§5.5.2). Internal access and the underlying values are unaffected, so state survives
   * re-enable.
   *
   * <p>Call after the subtree's property nodes exist (see the {@code ensure*Defaults} helpers);
   * EnabledState must never be gated.
   */
  final void installDisabledReadFilter(@Nullable UaNode root) {
    if (root != null) {
      installDisabledReadFilter(root, new DisabledReadFilter(), new HashSet<>());
    }
  }

  private void installDisabledReadFilter(
      UaNode target, DisabledReadFilter filter, Set<NodeId> visited) {

    if (!visited.add(target.getNodeId())) {
      return;
    }

    if (target instanceof UaVariableNode variable) {
      variable.getFilterChain().addLast(filter);
    }

    for (Reference reference : target.getReferences()) {
      if (reference.isForward() && isChildReference(reference.getReferenceTypeId())) {
        // Resolve through the node's own NodeManager first: the owning manager may not be
        // registered with the AddressSpaceManager yet (e.g. Conditions created before namespace
        // startup), and skipping children here would silently leave them ungated.
        target
            .getNodeManager()
            .getNode(reference.getTargetNodeId(), server.getNamespaceTable())
            .or(() -> server.getAddressSpaceManager().getManagedNode(reference.getTargetNodeId()))
            .ifPresent(child -> installDisabledReadFilter(child, filter, visited));
      }
    }
  }

  /**
   * Check if {@code referenceTypeId} aggregates child nodes: the common concrete types are matched
   * directly (also serving contexts with no ReferenceTypeTree, e.g. unit-test mocks), other
   * Aggregates subtypes through the server's {@link ReferenceTypeTree}.
   */
  private boolean isChildReference(NodeId referenceTypeId) {
    if (NodeIds.HasComponent.equals(referenceTypeId)
        || NodeIds.HasProperty.equals(referenceTypeId)
        || NodeIds.HasOrderedComponent.equals(referenceTypeId)) {
      return true;
    }

    ReferenceTypeTree referenceTypeTree = server.getReferenceTypeTree();
    return referenceTypeTree != null
        && referenceTypeTree.isSubtypeOf(referenceTypeId, NodeIds.Aggregates);
  }

  private class DisabledReadFilter implements AttributeFilter {
    @Override
    public @Nullable Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
        throws UaException {

      if (attributeId == AttributeId.Value && !enabled) {
        throw new UaException(StatusCodes.Bad_ConditionDisabled);
      }

      return ctx.readAttribute(attributeId);
    }
  }
}
