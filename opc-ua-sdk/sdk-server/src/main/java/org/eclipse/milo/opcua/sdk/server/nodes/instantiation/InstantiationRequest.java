/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.jspecify.annotations.Nullable;

/**
 * An immutable, builder-produced description of one instantiation: what type to instantiate, as
 * what Java class, with what identity, into which {@link NodeManager}, with which optional members
 * selected, which NodeIds, and which behavior hooks. Requests are pure data plus behavior hooks —
 * they perform nothing themselves; {@code NodeInstantiator.plan} joins a request with the type's
 * {@link TypeInstantiationModel} and {@code apply} materializes the result.
 *
 * <p>Structural configuration is declarative (browse paths, policies) so every decision is
 * inspectable in the resulting {@link InstantiationPlan}; the two hooks ({@link Builder#onNode
 * onNode}, {@link Builder#bindMethod bindMethod}) exist for <em>behavior</em> — filters,
 * permissions, value sources, invocation handlers — and run during apply against the complete
 * staged graph, before anything is published.
 *
 * @param <T> the expected Java class of the instance root, checked at plan time.
 */
public final class InstantiationRequest<T extends UaNode> {

  private final Class<T> rootClass;
  private final NodeId typeDefinitionId;
  private final NodeId rootNodeId;
  private final AttributeSnapshot rootAttributeOverrides;
  private final @Nullable NodeId parentNodeId;
  private final @Nullable NodeId parentReferenceTypeId;
  private final NodeManager<UaNode> target;
  private final Set<BrowsePath> includedPaths;
  private final Set<BrowsePath> excludedPaths;
  private final boolean includeAllOptionals;
  private final @Nullable Predicate<InstanceDeclaration> includePredicate;
  private final @Nullable Function<NodeIdContext, NodeId> nodeIdStrategy;
  private final Map<BrowsePath, NodeId> assignedNodeIds;
  private final boolean legacyPathStrings;
  private final ConflictPolicy conflictPolicy;
  private final MethodInstantiation methodInstantiation;
  private final Map<BrowsePath, NodeId> concreteTypes;
  private final Map<BrowsePath, List<PlaceholderRealization>> placeholderExpansions;
  private final @Nullable PlaceholderOrigin placeholderOrigin;
  private final InstantiationPurpose purpose;
  private final ReferenceReplicationPolicy referenceReplication;
  private final ClassResolution classResolution;
  private final boolean allowAbstractType;
  private final List<OnNode> onNodeHooks;
  private final Map<BrowsePath, Consumer<UaMethodNode>> methodBinders;
  private final List<Consumer<InstantiationResult<T>>> afterCommitObservers;

  private InstantiationRequest(Builder<T> builder) {
    this.rootClass = builder.rootClass;
    this.typeDefinitionId = builder.typeDefinitionId;
    this.rootNodeId = requireNonNull(builder.rootNodeId, "nodeId");
    this.rootAttributeOverrides = builder.rootAttributeOverrides.build();
    this.parentNodeId = builder.parentNodeId;
    this.parentReferenceTypeId = builder.parentReferenceTypeId;
    this.target = requireNonNull(builder.target, "target");
    this.includedPaths = Set.copyOf(builder.includedPaths);
    this.excludedPaths = Set.copyOf(builder.excludedPaths);
    this.includeAllOptionals = builder.includeAllOptionals;
    this.includePredicate = builder.includePredicate;
    this.nodeIdStrategy = builder.nodeIdStrategy;
    this.assignedNodeIds = Map.copyOf(builder.assignedNodeIds);
    this.legacyPathStrings = builder.legacyPathStrings;
    this.conflictPolicy = builder.conflictPolicy;
    this.methodInstantiation = builder.methodInstantiation;
    this.concreteTypes = Map.copyOf(builder.concreteTypes);

    var expansions = new LinkedHashMap<BrowsePath, List<PlaceholderRealization>>();
    builder.placeholderExpansions.forEach((path, list) -> expansions.put(path, List.copyOf(list)));
    this.placeholderExpansions = Collections.unmodifiableMap(expansions);

    this.placeholderOrigin = builder.placeholderOrigin;
    this.purpose = builder.purpose;
    this.referenceReplication = builder.referenceReplication;
    this.classResolution = builder.classResolution;
    this.allowAbstractType = builder.allowAbstractType;
    this.onNodeHooks = List.copyOf(builder.onNodeHooks);
    this.methodBinders = Map.copyOf(builder.methodBinders);
    this.afterCommitObservers = List.copyOf(builder.afterCommitObservers);
  }

  /**
   * Start a request to instantiate the type identified by {@code typeDefinitionId} as an instance
   * of {@code rootClass}.
   *
   * @param rootClass the expected Java class of the instance root; checked at plan time against the
   *     registered (or nearest-registered-ancestor) class, failing with {@link
   *     InstantiationDiagnostic.Code#INVALID_ROOT_CLASS} instead of a downstream cast.
   * @param typeDefinitionId the {@link NodeId} of the ObjectType or VariableType to instantiate.
   * @param <T> the expected root class.
   * @return a new {@link Builder}.
   */
  public static <T extends UaNode> Builder<T> of(Class<T> rootClass, NodeId typeDefinitionId) {
    return new Builder<>(rootClass, typeDefinitionId);
  }

  /**
   * Start a request realizing {@code placeholderDeclaration} under the existing instance node
   * {@code instanceNodeId} — post-hoc placeholder-driven creation, flowing through the same
   * plan/apply machinery as any other request.
   *
   * <p>The request instantiates the placeholder's declared type; parent placement is preset to
   * {@code instanceNodeId}, attached with the ReferenceType that connects the placeholder
   * declaration to its own parent (resolved at plan time; call {@link Builder#parent parent} to
   * override it). The caller still supplies identity: {@link Builder#nodeId nodeId} and, since a
   * placeholder's own BrowseName is only a marker, {@link Builder#browseName browseName}.
   *
   * <p>To instantiate a concrete subtype of the declared (possibly abstract) placeholder type,
   * select it with {@link Builder#concreteType concreteType} at {@link BrowsePath#root()}.
   *
   * @param instanceNodeId the existing instance node the realization is created under.
   * @param placeholderDeclaration the placeholder declaration being realized, from {@link
   *     TypeInstantiationModel#placeholders()}.
   * @return a new {@link Builder} expecting a {@link UaNode} root; use {@link
   *     #forPlaceholder(Class, NodeId, InstanceDeclaration)} for a typed root.
   * @throws IllegalArgumentException if {@code placeholderDeclaration} is not a placeholder or has
   *     no TypeDefinition (Method placeholders cannot be realized this way).
   */
  public static Builder<UaNode> forPlaceholder(
      NodeId instanceNodeId, InstanceDeclaration placeholderDeclaration) {
    return forPlaceholder(UaNode.class, instanceNodeId, placeholderDeclaration);
  }

  /**
   * Start a request realizing {@code placeholderDeclaration} under the existing instance node
   * {@code instanceNodeId}, with a typed root — see {@link #forPlaceholder(NodeId,
   * InstanceDeclaration)}.
   *
   * @param rootClass the expected Java class of the realized root, checked at plan time.
   * @param instanceNodeId the existing instance node the realization is created under.
   * @param placeholderDeclaration the placeholder declaration being realized.
   * @param <T> the expected root class.
   * @return a new {@link Builder}.
   * @throws IllegalArgumentException if {@code placeholderDeclaration} is not a placeholder or has
   *     no TypeDefinition (Method placeholders cannot be realized this way).
   */
  public static <T extends UaNode> Builder<T> forPlaceholder(
      Class<T> rootClass, NodeId instanceNodeId, InstanceDeclaration placeholderDeclaration) {

    requireNonNull(instanceNodeId, "instanceNodeId");
    requireNonNull(placeholderDeclaration, "placeholderDeclaration");

    if (!placeholderDeclaration.isPlaceholder()) {
      throw new IllegalArgumentException(
          "declaration at "
              + placeholderDeclaration.browsePath()
              + " is not a placeholder (ModellingRule "
              + placeholderDeclaration.rule()
              + ")");
    }

    NodeId typeDefinitionId = placeholderDeclaration.typeDefinitionId();
    if (typeDefinitionId == null) {
      throw new IllegalArgumentException(
          "placeholder declaration at "
              + placeholderDeclaration.browsePath()
              + " has no TypeDefinition; Method placeholders cannot be realized by forPlaceholder");
    }

    Builder<T> builder = new Builder<>(rootClass, typeDefinitionId);
    builder.parentNodeId = instanceNodeId;
    builder.placeholderOrigin = new PlaceholderOrigin(instanceNodeId, placeholderDeclaration);
    return builder;
  }

  /**
   * @return the expected Java class of the instance root.
   */
  public Class<T> rootClass() {
    return rootClass;
  }

  /**
   * @return the {@link NodeId} of the type to instantiate.
   */
  public NodeId typeDefinitionId() {
    return typeDefinitionId;
  }

  /**
   * @return the {@link NodeId} of the instance root.
   */
  public NodeId rootNodeId() {
    return rootNodeId;
  }

  /**
   * @return the request's root attribute overrides, applied over the type-derived defaults; absent
   *     entries defer to the type, explicit-null entries override with null.
   */
  public AttributeSnapshot rootAttributeOverrides() {
    return rootAttributeOverrides;
  }

  /**
   * @return the parent the root is attached under, if placement was requested.
   */
  public Optional<NodeId> parentNodeId() {
    return Optional.ofNullable(parentNodeId);
  }

  /**
   * @return the hierarchical ReferenceType the root is attached with, if placement was requested.
   */
  public Optional<NodeId> parentReferenceTypeId() {
    return Optional.ofNullable(parentReferenceTypeId);
  }

  /**
   * @return the destination {@link NodeManager} nodes are committed into.
   */
  public NodeManager<UaNode> target() {
    return target;
  }

  /**
   * @return the explicitly included occurrence paths; including a nested path implies its
   *     ancestors.
   */
  public Set<BrowsePath> includedPaths() {
    return includedPaths;
  }

  /**
   * @return the explicitly excluded occurrence paths; excluding a path prunes its whole subtree.
   */
  public Set<BrowsePath> excludedPaths() {
    return excludedPaths;
  }

  /**
   * @return {@code true} if every Optional declaration is selected.
   */
  public boolean includeAllOptionals() {
    return includeAllOptionals;
  }

  /**
   * @return the selection predicate escape hatch, if configured.
   */
  public Optional<Predicate<InstanceDeclaration>> includePredicate() {
    return Optional.ofNullable(includePredicate);
  }

  /**
   * @return the per-call NodeId strategy, if configured.
   */
  public Optional<Function<NodeIdContext, NodeId>> nodeIdStrategy() {
    return Optional.ofNullable(nodeIdStrategy);
  }

  /**
   * @return the per-path pinned NodeIds; pins win over the strategy and the defaults.
   */
  public Map<BrowsePath, NodeId> assignedNodeIds() {
    return assignedNodeIds;
  }

  /**
   * @return {@code true} if unpinned, non-strategy NodeIds use the legacy {@code NodeFactory}
   *     formula ({@link NodeIdContext#legacyNodeId()}) instead of the escaped default.
   */
  public boolean legacyPathStrings() {
    return legacyPathStrings;
  }

  /**
   * @return the collision policy; {@link ConflictPolicy#FAIL} unless configured otherwise.
   */
  public ConflictPolicy conflictPolicy() {
    return conflictPolicy;
  }

  /**
   * @return the Method realization mode; {@link MethodInstantiation#COPY} unless configured
   *     otherwise.
   */
  public MethodInstantiation methodInstantiation() {
    return methodInstantiation;
  }

  /**
   * @return the per-path concrete-subtype selections for abstract member types.
   */
  public Map<BrowsePath, NodeId> concreteTypes() {
    return concreteTypes;
  }

  /**
   * @return the realizations bound to placeholder paths, in registration order per path.
   */
  public Map<BrowsePath, List<PlaceholderRealization>> placeholderExpansions() {
    return placeholderExpansions;
  }

  /**
   * @return the placeholder this request realizes, if it was created by {@link #forPlaceholder}.
   */
  public Optional<PlaceholderOrigin> placeholderOrigin() {
    return Optional.ofNullable(placeholderOrigin);
  }

  /**
   * @return the instantiation purpose; {@link InstantiationPurpose#NORMAL_INSTANCE} unless
   *     configured otherwise.
   */
  public InstantiationPurpose purpose() {
    return purpose;
  }

  /**
   * @return the reference replication policy; {@link ReferenceReplicationPolicy#DEFAULT} unless
   *     configured otherwise.
   */
  public ReferenceReplicationPolicy referenceReplication() {
    return referenceReplication;
  }

  /**
   * @return how Java classes are resolved from the typed registries; {@link
   *     ClassResolution#NEAREST_ANCESTOR} unless configured otherwise.
   */
  public ClassResolution classResolution() {
    return classResolution;
  }

  /**
   * @return {@code true} if the instantiated root type may be abstract; {@code false} (reject at
   *     plan time) unless configured otherwise.
   */
  public boolean allowAbstractType() {
    return allowAbstractType;
  }

  /**
   * @return the {@code onNode} hooks, in registration order.
   */
  public List<OnNode> onNodeHooks() {
    return onNodeHooks;
  }

  /**
   * @return the per-path Method binders.
   */
  public Map<BrowsePath, Consumer<UaMethodNode>> methodBinders() {
    return methodBinders;
  }

  /**
   * @return the after-commit observers, in registration order.
   */
  public List<Consumer<InstantiationResult<T>>> afterCommitObservers() {
    return afterCommitObservers;
  }

  /**
   * How the engine resolves the Java class (and constructor) for a type from the typed registries.
   * This is a per-request knob; the legacy {@code NodeFactory} lookup keeps its exact-match
   * semantics regardless.
   */
  public enum ClassResolution {

    /**
     * Walk the {@code HasSubtype} chain to the nearest registered ancestor before degrading to
     * {@code UaObjectNode}/{@code UaVariableNode}, so unregistered subtypes get their closest
     * registered ancestor's class. The default.
     */
    NEAREST_ANCESTOR,

    /** Exact-match only: an unregistered type resolves directly to the base node class. */
    EXACT
  }

  /**
   * The provenance of a {@link #forPlaceholder} request: the existing instance node the realization
   * is created under and the placeholder declaration being realized. Carried so plan time can
   * resolve the parent ReferenceType from the placeholder declaration's own attachment when the
   * request does not set one explicitly.
   *
   * @param instanceNodeId the existing instance node the realization is created under.
   * @param declaration the placeholder declaration being realized.
   */
  public record PlaceholderOrigin(NodeId instanceNodeId, InstanceDeclaration declaration) {}

  /**
   * A per-node behavior hook, run during apply after the complete staged graph is constructed and
   * before anything is committed or published: attach filters, permissions, value sources, and
   * initial state here instead of mutating nodes after creation.
   *
   * <p>Configure node-local state only. Graph-mutating conveniences ({@link UaNode#addReference},
   * {@code setProperty}, {@code addComponent}, …) write through the node's context straight into
   * the live target NodeManager — outside the journaled batch, so they are never rolled back on
   * failure, never removed by {@link InstantiationResult#deleteCreated()}, and they advance the
   * target generation, forcing a commit retry. Structure belongs in the request (selection, {@code
   * assignNodeId}, reference replication), not in a hook.
   */
  @FunctionalInterface
  public interface OnNode {

    /**
     * Customize one staged node.
     *
     * @param declaration the declaration occurrence realized by {@code node}; {@code null} for the
     *     instance root, which realizes the type itself rather than a declaration.
     * @param node the staged, fully constructed node.
     * @param parent the staged parent node; non-null for every non-root node, {@code null} for the
     *     root.
     * @param graph a read-only view of the complete staged graph, for cross-node wiring.
     */
    void accept(
        @Nullable InstanceDeclaration declaration,
        UaNode node,
        @Nullable UaNode parent,
        StagedGraph graph);
  }

  /** Builder for {@link InstantiationRequest}s. */
  public static final class Builder<T extends UaNode> {

    private final Class<T> rootClass;
    private final NodeId typeDefinitionId;

    private @Nullable NodeId rootNodeId;
    private final AttributeSnapshot.Builder rootAttributeOverrides = AttributeSnapshot.builder();
    private @Nullable NodeId parentNodeId;
    private @Nullable NodeId parentReferenceTypeId;
    private @Nullable NodeManager<UaNode> target;
    private final Set<BrowsePath> includedPaths = new LinkedHashSet<>();
    private final Set<BrowsePath> excludedPaths = new LinkedHashSet<>();
    private boolean includeAllOptionals = false;
    private @Nullable Predicate<InstanceDeclaration> includePredicate;
    private @Nullable Function<NodeIdContext, NodeId> nodeIdStrategy;
    private final Map<BrowsePath, NodeId> assignedNodeIds = new LinkedHashMap<>();
    private boolean legacyPathStrings = false;
    private ConflictPolicy conflictPolicy = ConflictPolicy.FAIL;
    private MethodInstantiation methodInstantiation = MethodInstantiation.COPY;
    private final Map<BrowsePath, NodeId> concreteTypes = new LinkedHashMap<>();
    private final Map<BrowsePath, List<PlaceholderRealization>> placeholderExpansions =
        new LinkedHashMap<>();
    private @Nullable PlaceholderOrigin placeholderOrigin;
    private InstantiationPurpose purpose = InstantiationPurpose.NORMAL_INSTANCE;
    private ReferenceReplicationPolicy referenceReplication = ReferenceReplicationPolicy.DEFAULT;
    private ClassResolution classResolution = ClassResolution.NEAREST_ANCESTOR;
    private boolean allowAbstractType = false;
    private final List<OnNode> onNodeHooks = new ArrayList<>();
    private final Map<BrowsePath, Consumer<UaMethodNode>> methodBinders = new LinkedHashMap<>();
    private final List<Consumer<InstantiationResult<T>>> afterCommitObservers = new ArrayList<>();

    private Builder(Class<T> rootClass, NodeId typeDefinitionId) {
      this.rootClass = requireNonNull(rootClass, "rootClass");
      this.typeDefinitionId = requireNonNull(typeDefinitionId, "typeDefinitionId");
    }

    /**
     * Set the {@link NodeId} of the instance root. Required.
     *
     * @param nodeId the root {@link NodeId}.
     * @return this builder.
     */
    public Builder<T> nodeId(NodeId nodeId) {
      this.rootNodeId = requireNonNull(nodeId, "nodeId");
      return this;
    }

    /**
     * Set the root's BrowseName. Precedence when absent: the type's {@code
     * DefaultInstanceBrowseName}, then the type's own BrowseName.
     *
     * @param browseName the root BrowseName.
     * @return this builder.
     */
    public Builder<T> browseName(QualifiedName browseName) {
      return rootAttribute(AttributeId.BrowseName, requireNonNull(browseName, "browseName"));
    }

    /**
     * Set the root's DisplayName; the type's DisplayName is the default.
     *
     * @param displayName the root DisplayName.
     * @return this builder.
     */
    public Builder<T> displayName(LocalizedText displayName) {
      return rootAttribute(AttributeId.DisplayName, requireNonNull(displayName, "displayName"));
    }

    /**
     * Set the root's Description.
     *
     * @param description the root Description.
     * @return this builder.
     */
    public Builder<T> description(LocalizedText description) {
      return rootAttribute(AttributeId.Description, description);
    }

    /**
     * Set the root's Value (VariableType instantiation).
     *
     * @param value the root Value.
     * @return this builder.
     */
    public Builder<T> value(DataValue value) {
      return rootAttribute(AttributeId.Value, value);
    }

    /**
     * Override any root attribute over the type-derived defaults. A {@code null} value records an
     * explicit null (distinct from not overriding at all).
     *
     * @param attributeId the attribute to override.
     * @param value the value, or {@code null} for explicit null.
     * @return this builder.
     */
    public Builder<T> rootAttribute(AttributeId attributeId, @Nullable Object value) {
      rootAttributeOverrides.put(attributeId, value);
      return this;
    }

    /**
     * Attach the root under {@code parentNodeId} via a forward {@code referenceTypeId} reference,
     * committed atomically with the rest of the graph. Optional: transient or hidden destinations
     * legitimately have no parent.
     *
     * @param parentNodeId the parent node.
     * @param referenceTypeId the hierarchical ReferenceType to attach with.
     * @return this builder.
     */
    public Builder<T> parent(NodeId parentNodeId, NodeId referenceTypeId) {
      this.parentNodeId = requireNonNull(parentNodeId, "parentNodeId");
      this.parentReferenceTypeId = requireNonNull(referenceTypeId, "referenceTypeId");
      return this;
    }

    /**
     * Set the destination {@link NodeManager} nodes are committed into. Required. The source of
     * type information is always the server's address space; the destination is entirely the
     * request's business.
     *
     * @param target the destination manager.
     * @return this builder.
     */
    public Builder<T> target(NodeManager<UaNode> target) {
      this.target = requireNonNull(target, "target");
      return this;
    }

    /**
     * Select the Optional (or vendor-rule) declaration at {@code path}. Selecting a nested path
     * implies its ancestors.
     *
     * @param path the occurrence path to include.
     * @return this builder.
     */
    public Builder<T> includeOptional(BrowsePath path) {
      includedPaths.add(requireNonNull(path, "path"));
      return this;
    }

    /**
     * Exclude the declaration at {@code path}, pruning its whole subtree (descendants are skipped
     * as {@link SkippedDeclaration.Reason#ANCESTOR_OMITTED}). Excluding a Mandatory declaration
     * whose parent path exists is a plan error. Exclusion wins over every inclusion mechanism.
     *
     * @param path the occurrence path to exclude.
     * @return this builder.
     */
    public Builder<T> excludeOptional(BrowsePath path) {
      excludedPaths.add(requireNonNull(path, "path"));
      return this;
    }

    /**
     * Select every Optional declaration (vendor-rule declarations stay unselected).
     *
     * @return this builder.
     */
    public Builder<T> includeAllOptionals() {
      this.includeAllOptionals = true;
      return this;
    }

    /**
     * Selection escape hatch: consulted for each Optional or vendor-rule declaration not decided by
     * an explicit path; receives the full {@link InstanceDeclaration} occurrence.
     *
     * @param predicate returns {@code true} to select the declaration.
     * @return this builder.
     */
    public Builder<T> includeOptionals(Predicate<InstanceDeclaration> predicate) {
      this.includePredicate = requireNonNull(predicate, "predicate");
      return this;
    }

    /**
     * Set the NodeId derivation used for every planned node not pinned via {@link #assignNodeId}.
     * The strategy receives full context and may return any identifier type; {@link
     * NodeIdContext#defaultNodeId()} and {@link NodeIdContext#legacyNodeId()} are available as
     * fallbacks.
     *
     * @param strategy the derivation function.
     * @return this builder.
     */
    public Builder<T> nodeIdStrategy(Function<NodeIdContext, NodeId> strategy) {
      this.nodeIdStrategy = requireNonNull(strategy, "strategy");
      return this;
    }

    /**
     * Pin the {@link NodeId} of the planned node at {@code path}; wins over the strategy and the
     * defaults.
     *
     * @param path the occurrence path to pin.
     * @param nodeId the pinned id.
     * @return this builder.
     */
    public Builder<T> assignNodeId(BrowsePath path, NodeId nodeId) {
      assignedNodeIds.put(requireNonNull(path, "path"), requireNonNull(nodeId, "nodeId"));
      return this;
    }

    /**
     * Derive unpinned, non-strategy NodeIds with the legacy {@code NodeFactory} formula ({@link
     * NodeIdContext#legacyNodeId()}) so derived ids match existing deployments during migration.
     *
     * @return this builder.
     */
    public Builder<T> legacyPathStrings() {
      this.legacyPathStrings = true;
      return this;
    }

    /**
     * Set the collision policy.
     *
     * @param conflictPolicy the policy.
     * @return this builder.
     */
    public Builder<T> conflictPolicy(ConflictPolicy conflictPolicy) {
      this.conflictPolicy = requireNonNull(conflictPolicy, "conflictPolicy");
      return this;
    }

    /**
     * Set the Method realization mode.
     *
     * @param methodInstantiation the mode.
     * @return this builder.
     */
    public Builder<T> methodInstantiation(MethodInstantiation methodInstantiation) {
      this.methodInstantiation = requireNonNull(methodInstantiation, "methodInstantiation");
      return this;
    }

    /**
     * Choose the concrete subtype instantiated for the (typically abstract-typed) member at {@code
     * path}. Must be the declared member type or a subtype of it.
     *
     * <p>At {@link BrowsePath#root()}, chooses the concrete subtype the whole request instantiates
     * in place of its declared type (validated the same way) — this is how a {@link
     * #forPlaceholder} request selects a concrete subtype of the placeholder's declared type.
     *
     * @param path the member occurrence path.
     * @param typeDefinitionId the concrete type to instantiate.
     * @return this builder.
     */
    public Builder<T> concreteType(BrowsePath path, NodeId typeDefinitionId) {
      concreteTypes.put(
          requireNonNull(path, "path"), requireNonNull(typeDefinitionId, "typeDefinitionId"));
      return this;
    }

    /**
     * Bind realizations to the placeholder declaration at {@code path} — request-time expansion.
     * Each realization is planned as a sibling of the placeholder (the placeholder's path with its
     * last element replaced by the realization's BrowseName), with the full member hierarchy of its
     * (concrete-resolved) type realized beneath it.
     *
     * <p>Repeated calls for the same path accumulate. Binding a nested placeholder implies its
     * ancestors, like {@link #includeOptional}; a MandatoryPlaceholder left without a single
     * realization is a plan error ({@link
     * InstantiationDiagnostic.Code#MANDATORY_PLACEHOLDER_UNSATISFIED}); a path naming no expandable
     * placeholder — mistyped, excluded, or under an omitted ancestor — is a plan error too ({@link
     * InstantiationDiagnostic.Code#PLACEHOLDER_EXPANSION_INVALID}): expansion is a directive to
     * create, never silently dropped.
     *
     * @param path the placeholder occurrence path (for a placeholder inside another expansion's
     *     realized subtree: the realized path, e.g. {@code Channel1/<Signal>}).
     * @param realizations the realizations to bind; at least one.
     * @return this builder.
     */
    public Builder<T> expandPlaceholder(BrowsePath path, PlaceholderRealization... realizations) {
      requireNonNull(path, "path");
      if (realizations.length == 0) {
        throw new IllegalArgumentException("at least one realization is required");
      }
      List<PlaceholderRealization> list =
          placeholderExpansions.computeIfAbsent(path, p -> new ArrayList<>());
      for (PlaceholderRealization realization : realizations) {
        list.add(requireNonNull(realization, "realization"));
      }
      return this;
    }

    /**
     * Set the instantiation purpose.
     *
     * @param purpose the purpose.
     * @return this builder.
     */
    public Builder<T> purpose(InstantiationPurpose purpose) {
      this.purpose = requireNonNull(purpose, "purpose");
      return this;
    }

    /**
     * Set the reference replication policy.
     *
     * @param policy the policy.
     * @return this builder.
     */
    public Builder<T> referenceReplication(ReferenceReplicationPolicy policy) {
      this.referenceReplication = requireNonNull(policy, "policy");
      return this;
    }

    /**
     * Set how Java classes are resolved from the typed registries.
     *
     * @param classResolution the resolution mode.
     * @return this builder.
     */
    public Builder<T> classResolution(ClassResolution classResolution) {
      this.classResolution = requireNonNull(classResolution, "classResolution");
      return this;
    }

    /**
     * Permit the instantiated root type to be abstract instead of rejecting it at plan time.
     *
     * <p>Abstract types admit no address-space instances, so plan ordinarily rejects them and
     * demands a concrete subtype ({@link #concreteType} at {@link BrowsePath#root()}). The
     * sanctioned exception is transient instances that never join the server's hierarchy — Event
     * instances of abstract EventTypes (BaseEventType itself is abstract), created into a private
     * NodeManager and deleted after being fired.
     *
     * @return this builder.
     */
    public Builder<T> allowAbstractType() {
      this.allowAbstractType = true;
      return this;
    }

    /**
     * Add a per-node behavior hook, run during apply against the complete staged graph. Hooks run
     * in registration order; a hook exception aborts apply with full rollback.
     *
     * @param hook the hook.
     * @return this builder.
     */
    public Builder<T> onNode(OnNode hook) {
      onNodeHooks.add(requireNonNull(hook, "hook"));
      return this;
    }

    /**
     * Bind behavior to the Method realized at {@code path} (under either {@link
     * MethodInstantiation} mode), within the same apply rather than post-hoc. A path matching no
     * planned Method produces a plan warning.
     *
     * <p>Binders run immediately after a successful commit — a binder may target a reused or shared
     * Method the instantiation does not own, so binding earlier would leave mutations on published
     * nodes if the commit failed. A binder exception aborts apply and rolls back the committed
     * additions; a mutation an earlier binder made to a reused or shared node cannot be rolled
     * back.
     *
     * @param path the Method occurrence path.
     * @param binder receives the realized {@link UaMethodNode}.
     * @return this builder.
     */
    public Builder<T> bindMethod(BrowsePath path, Consumer<UaMethodNode> binder) {
      methodBinders.put(requireNonNull(path, "path"), requireNonNull(binder, "binder"));
      return this;
    }

    /**
     * Add an observer notified after a successful commit, with the completed {@link
     * InstantiationResult}. Notification only: observers run after the graph is committed and
     * published, and an observer exception is logged and swallowed — it cannot affect the
     * instantiation's success. Initialization belongs in {@link #onNode} / {@link #bindMethod}.
     *
     * @param observer the observer.
     * @return this builder.
     */
    public Builder<T> afterCommit(Consumer<InstantiationResult<T>> observer) {
      afterCommitObservers.add(requireNonNull(observer, "observer"));
      return this;
    }

    /**
     * @return the immutable {@link InstantiationRequest}.
     * @throws NullPointerException if {@code nodeId} or {@code target} was not set.
     */
    public InstantiationRequest<T> build() {
      return new InstantiationRequest<>(this);
    }
  }
}
