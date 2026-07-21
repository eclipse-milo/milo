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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.NodeManagerBatch;
import org.eclipse.milo.opcua.sdk.server.NodeManagerBatchException;
import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The server-scoped instantiation facade: {@link #describe} exposes a type's compiled {@link
 * TypeInstantiationModel} (through the server's {@link TypeModelCache}), {@link #plan} joins a
 * model with an {@link InstantiationRequest} into a pure-data {@link InstantiationPlan} — no nodes
 * created, nothing mutated — and {@link #apply} materializes a plan into the request's target as a
 * staged, journaled unit. {@link #instantiate} is the one-call convenience wrapping plan and apply.
 *
 * <p>Source type information always comes from the server's address space; the destination is
 * entirely the request's business (its target {@link NodeManager}) — plan only ever <em>reads</em>
 * the target, for collision preflight, and apply writes to nothing else.
 */
public final class NodeInstantiator {

  private static final Logger LOGGER = LoggerFactory.getLogger(NodeInstantiator.class);

  private final OpcUaServer server;

  /**
   * Create an instantiator scoped to {@code server}.
   *
   * @param server the server whose address space, model cache, and typed registries are used.
   */
  public NodeInstantiator(OpcUaServer server) {
    this.server = server;
  }

  /**
   * Get the compiled instantiation model of the type identified by {@code typeDefinitionId},
   * through the server's {@link TypeModelCache}.
   *
   * @param typeDefinitionId the {@link NodeId} of an ObjectType or VariableType node.
   * @return the compiled, validated {@link TypeInstantiationModel}.
   * @throws UaException if the type graph admits no correct instantiation (see {@link
   *     ModelCompilationException}).
   */
  public TypeInstantiationModel describe(NodeId typeDefinitionId) throws UaException {
    return server.getTypeModelCache().getOrCompile(typeDefinitionId);
  }

  /**
   * Compute the plan joining {@code request} with its type's model: every declaration accounted
   * for, all NodeIds allocated and collision-checked, every reference resolved to instance ids.
   *
   * <p>Pure computation: nothing is constructed and nothing — source or target — is mutated.
   * Request-level findings (collisions, invalid classes, omitted Mandatories, …) are returned
   * <em>on</em> the plan as {@link InstantiationDiagnostic}s rather than thrown; a plan carrying
   * errors is inspectable but refused by apply.
   *
   * @param request the request to plan.
   * @param <T> the expected root class.
   * @return the computed {@link InstantiationPlan}.
   * @throws UaException if the type's model cannot be compiled.
   */
  public <T extends UaNode> InstantiationPlan<T> plan(InstantiationRequest<T> request)
      throws UaException {

    // A concreteType selection at the root path substitutes the instantiated type wholesale —
    // how a forPlaceholder request selects a concrete subtype of an abstract declared type;
    // validated against the declared type when the root is planned.
    NodeId rootTypeId =
        request.concreteTypes().getOrDefault(BrowsePath.root(), request.typeDefinitionId());

    TypeInstantiationModel model = describe(rootTypeId);

    return new PlanComputation<>(this, request, model).compute();
  }

  /**
   * Materialize {@code plan} into its request's target {@link NodeManager} as a staged, journaled
   * unit.
   *
   * <p>Six stages: (1) revalidate the model fingerprints — a type changed since planning fails with
   * {@link InstantiationDiagnostic.Code#MODEL_CHANGED} before anything is constructed; (2) recheck
   * collisions, reused-node compatibility, and the parent attachment against the target; (3)
   * construct every planned node, unpublished; (4) run the request's {@code onNode} hooks against
   * the complete staged graph; (5) commit nodes and references as one journaled batch through the
   * target's storage primitives, then run the request's {@code bindMethod} binders against the
   * committed graph; (6) on failure, roll back exactly the journaled additions — never a recursive
   * delete from the root.
   *
   * <p>If the target does not override the storage primitives, the commit runs on the sequential
   * emulations and the result reports {@link NodeManager.StorageGuarantee#BEST_EFFORT} instead of
   * atomic — degraded, and said so, never silent.
   *
   * <p>A commit rejected because the target's generation advanced is rechecked and retried a small,
   * bounded number of times. Under sustained concurrent mutation of the same target — even by
   * non-conflicting writers — the retries can be exhausted, failing with {@link
   * InstantiationDiagnostic.Code#COMMIT_FAILED} and {@code Bad_InvalidState}; such a failure is
   * safe to retry by re-applying the plan.
   *
   * @param plan the plan to materialize.
   * @param <T> the expected root class.
   * @return the {@link InstantiationResult}.
   * @throws InstantiationException if {@code plan} carries errors, or an apply stage fails; apart
   *     from a failed rollback ({@link InstantiationDiagnostic.Code#ROLLBACK_FAILED}, reported
   *     loudly), a failed apply leaves no residue in the target.
   * @throws UaException if the model revalidation cannot read the type.
   */
  public <T extends UaNode> InstantiationResult<T> apply(InstantiationPlan<T> plan)
      throws UaException {

    if (plan.hasErrors()) {
      throw new InstantiationException(
          StatusCodes.Bad_InvalidArgument, InstantiationDiagnostic.Phase.PLAN, plan.errors(), null);
    }

    return new ApplyExecution<>(this, plan).execute();
  }

  /**
   * Plan and apply {@code request} in one call — the 90% case.
   *
   * @param request the request to instantiate.
   * @param <T> the expected root class.
   * @return the {@link InstantiationResult}.
   * @throws InstantiationException if the plan carries errors or apply fails.
   * @throws UaException if the type's model cannot be compiled.
   */
  public <T extends UaNode> InstantiationResult<T> instantiate(InstantiationRequest<T> request)
      throws UaException {

    return apply(plan(request));
  }

  /**
   * One plan computation: selection, class and attribute resolution, NodeId allocation, collision
   * preflight, and reference resolution over a single model snapshot.
   */
  private static final class PlanComputation<T extends UaNode> {

    private final NodeInstantiator instantiator;
    private final OpcUaServer server;
    private final InstantiationRequest<T> request;
    private final TypeInstantiationModel model;

    private final Map<BrowsePath, Working> planned = new LinkedHashMap<>();
    private final Map<BrowsePath, SkippedDeclaration> skipped = new LinkedHashMap<>();
    private final List<InstantiationDiagnostic> diagnostics = new ArrayList<>();
    private final Map<NodeId, @Nullable TypeInstantiationModel> memberModels = new HashMap<>();

    /** Expanded placeholder subtrees: realization path -> the member-type model grafted there. */
    private final Map<BrowsePath, TypeInstantiationModel> graftedModels = new LinkedHashMap<>();

    /** Hierarchy edges added by placeholder expansion, absolute-path keyed like the model's. */
    private final List<DeclarationEdge> graftedEdges = new ArrayList<>();

    /** Reference rows added by placeholder expansion, relocated to realized paths. */
    private final List<ReferenceRow> graftedRows = new ArrayList<>();

    /** Expansion paths that reached an expandable placeholder; any others are plan errors. */
    private final Set<BrowsePath> consumedExpansions = new HashSet<>();

    private PlanComputation(
        NodeInstantiator instantiator,
        InstantiationRequest<T> request,
        TypeInstantiationModel model) {

      this.instantiator = instantiator;
      this.server = instantiator.server;
      this.request = request;
      this.model = model;
    }

    InstantiationPlan<T> compute() throws UaException {
      planRoot();

      for (InstanceDeclaration declaration : model.declarations()) {
        classify(declaration);
      }

      validateExpansions();

      allocateNodeIds();
      preflightCollisions();

      List<Reference> references = resolveReferences();

      warnUnmatchedPaths();

      List<PlannedNode> plannedNodes =
          planned.values().stream().map(Working::toPlannedNode).toList();

      Map<NodeId, Long> consultedModelRevisions = new HashMap<>();
      consultedModelRevisions.put(model.typeDefinitionId(), model.modelRevision());
      memberModels.forEach(
          (typeId, memberModel) -> {
            if (memberModel != null) {
              consultedModelRevisions.put(typeId, memberModel.modelRevision());
            }
          });

      return new InstantiationPlan<>(
          request,
          model,
          plannedNodes,
          List.copyOf(skipped.values()),
          references,
          diagnostics,
          consultedModelRevisions);
    }

    // region Root

    private void planRoot() {
      NodeId declaredTypeId = request.typeDefinitionId();
      if (!model.typeDefinitionId().equals(declaredTypeId)
          && !isTypeSubtypeOfOrEqual(model.typeDefinitionId(), declaredTypeId)) {
        diagnostics.add(
            InstantiationDiagnostic.planError(
                InstantiationDiagnostic.Code.CONCRETE_TYPE_INVALID,
                "concreteType "
                    + model.typeDefinitionId()
                    + " for the instance root is not declared type "
                    + declaredTypeId
                    + " or a subtype",
                BrowsePath.root(),
                model.typeDefinitionId()));
      }

      AttributeSnapshot typeAttributes = model.rootAttributes();

      NodeClass typeNodeClass = (NodeClass) typeAttributes.getOrNull(AttributeId.NodeClass);
      NodeClass instanceNodeClass =
          typeNodeClass == NodeClass.VariableType ? NodeClass.Variable : NodeClass.Object;

      if (Boolean.TRUE.equals(typeAttributes.getOrNull(AttributeId.IsAbstract))
          && !request.allowAbstractType()) {
        diagnostics.add(
            InstantiationDiagnostic.planError(
                InstantiationDiagnostic.Code.ABSTRACT_TYPE,
                "type " + model.typeDefinitionId() + " is abstract; request a concrete subtype",
                BrowsePath.root(),
                model.typeDefinitionId()));
      }

      ResolvedClass resolved = resolveJavaClass(model.typeDefinitionId(), instanceNodeClass);

      if (!request.rootClass().isAssignableFrom(resolved.javaClass())) {
        diagnostics.add(
            InstantiationDiagnostic.planError(
                InstantiationDiagnostic.Code.INVALID_ROOT_CLASS,
                "type "
                    + model.typeDefinitionId()
                    + " resolves to "
                    + resolved.javaClass().getName()
                    + ", not assignable to expected root class "
                    + request.rootClass().getName(),
                BrowsePath.root(),
                model.typeDefinitionId()));
      }

      AttributeSnapshot effective = rootEffectiveAttributes(typeAttributes, instanceNodeClass);

      validateRootOverrideNulls(instanceNodeClass);

      if (instanceNodeClass == NodeClass.Variable) {
        validateRootNarrowing(effective, typeAttributes);
      }

      Working root =
          new Working(
              BrowsePath.root(),
              null,
              instanceNodeClass,
              model.typeDefinitionId(),
              resolved.javaClass(),
              resolved.constructorTypeId(),
              effective);
      root.nodeId = request.rootNodeId();

      planned.put(BrowsePath.root(), root);
    }

    /**
     * Root attributes come from the type per Part 3 §6.4.2 (minus type-only attributes), then the
     * request's overrides; BrowseName precedence is request over {@code DefaultInstanceBrowseName}
     * over the type's own BrowseName.
     */
    private AttributeSnapshot rootEffectiveAttributes(
        AttributeSnapshot typeAttributes, NodeClass instanceNodeClass) {

      AttributeSnapshot.Builder b = AttributeSnapshot.builder();

      for (AttributeId id : typeAttributes.attributeIds()) {
        if (id == AttributeId.IsAbstract || id == AttributeId.NodeClass) {
          continue;
        }
        b.put(id, typeAttributes.isNull(id) ? null : typeAttributes.getOrNull(id));
      }

      b.put(AttributeId.NodeClass, instanceNodeClass);

      AttributeSnapshot overrides = request.rootAttributeOverrides();

      if (!overrides.contains(AttributeId.BrowseName)) {
        model
            .defaultInstanceBrowseName()
            .ifPresent(defaultName -> b.put(AttributeId.BrowseName, defaultName));
      }

      for (AttributeId id : overrides.attributeIds()) {
        b.put(id, overrides.isNull(id) ? null : overrides.getOrNull(id));
      }

      return b.build();
    }

    /** Attributes every Object instance must carry; an explicit-null override cannot stand. */
    private static final Set<AttributeId> MANDATORY_OBJECT_ATTRIBUTES =
        EnumSet.of(
            AttributeId.NodeClass,
            AttributeId.BrowseName,
            AttributeId.DisplayName,
            AttributeId.EventNotifier);

    /**
     * Attributes every Variable instance must carry; an explicit-null override cannot stand. Value
     * is exempt: a null Value follows the Bad_NoValue convention rather than being invalid.
     */
    private static final Set<AttributeId> MANDATORY_VARIABLE_ATTRIBUTES =
        EnumSet.of(
            AttributeId.NodeClass,
            AttributeId.BrowseName,
            AttributeId.DisplayName,
            AttributeId.DataType,
            AttributeId.ValueRank,
            AttributeId.AccessLevel,
            AttributeId.UserAccessLevel,
            AttributeId.Historizing);

    /**
     * An explicit-null override of a mandatory attribute would survive construction defaults (the
     * overlay preserves explicit nulls by design) and commit a node no client can read correctly,
     * so it is rejected here; explicit nulls remain valid for optional attributes.
     */
    private void validateRootOverrideNulls(NodeClass instanceNodeClass) {
      Set<AttributeId> mandatory =
          instanceNodeClass == NodeClass.Variable
              ? MANDATORY_VARIABLE_ATTRIBUTES
              : MANDATORY_OBJECT_ATTRIBUTES;

      AttributeSnapshot overrides = request.rootAttributeOverrides();
      for (AttributeId id : overrides.attributeIds()) {
        if (overrides.isNull(id) && mandatory.contains(id)) {
          diagnostics.add(
              InstantiationDiagnostic.planError(
                  InstantiationDiagnostic.Code.INVALID_ATTRIBUTE,
                  "attribute override "
                      + id
                      + " is an explicit null, but "
                      + id
                      + " is mandatory for NodeClass "
                      + instanceNodeClass,
                  BrowsePath.root(),
                  model.typeDefinitionId()));
        }
      }
    }

    private void validateRootNarrowing(AttributeSnapshot effective, AttributeSnapshot base) {
      AttributeSnapshot overrides = request.rootAttributeOverrides();

      NodeId effectiveDataType = effective.dataType();
      NodeId baseDataType = base.dataType();
      if (overrides.contains(AttributeId.DataType)
          && effectiveDataType != null
          && baseDataType != null
          && !isTypeSubtypeOfOrEqual(effectiveDataType, baseDataType)) {
        diagnostics.add(
            InstantiationDiagnostic.planError(
                InstantiationDiagnostic.Code.VARIABLE_NARROWING,
                "requested DataType "
                    + effectiveDataType
                    + " is not "
                    + baseDataType
                    + " or a subtype (Part 3 §6.2.8)",
                BrowsePath.root(),
                model.typeDefinitionId()));
      }

      Integer effectiveRank = effective.valueRank();
      Integer baseRank = base.valueRank();
      if (overrides.contains(AttributeId.ValueRank)
          && effectiveRank != null
          && baseRank != null
          && !isValueRankRestriction(baseRank, effectiveRank)) {
        diagnostics.add(
            InstantiationDiagnostic.planError(
                InstantiationDiagnostic.Code.VARIABLE_NARROWING,
                "requested ValueRank "
                    + effectiveRank
                    + " is not a valid restriction of "
                    + baseRank
                    + " (Part 3 §6.2.8)",
                BrowsePath.root(),
                model.typeDefinitionId()));
      }

      UInteger[] effectiveDimensions = effective.arrayDimensions();
      UInteger[] baseDimensions = base.arrayDimensions();
      if (overrides.contains(AttributeId.ArrayDimensions)
          && effectiveDimensions != null
          && baseDimensions != null
          && !isArrayDimensionsRestriction(baseDimensions, effectiveDimensions)) {
        diagnostics.add(
            InstantiationDiagnostic.planError(
                InstantiationDiagnostic.Code.VARIABLE_NARROWING,
                "requested ArrayDimensions are not a valid restriction (Part 3 §6.2.8)",
                BrowsePath.root(),
                model.typeDefinitionId()));
      }
    }

    // endregion

    // region Selection

    private void classify(InstanceDeclaration declaration) throws UaException {
      BrowsePath path = declaration.browsePath();
      BrowsePath parentPath = path.parent();

      if (skipped.containsKey(parentPath)) {
        skip(declaration, SkippedDeclaration.Reason.ANCESTOR_OMITTED);
        return;
      }

      Working parent = planned.get(parentPath);
      if (parent == null) {
        // Defensive: paths are ordered parents-first, so an unaccounted parent cannot happen.
        skip(declaration, SkippedDeclaration.Reason.ANCESTOR_OMITTED);
        return;
      }

      if (parent.materialization == PlannedNode.Materialization.SHARE) {
        skip(declaration, SkippedDeclaration.Reason.METHOD_SHARED);
        return;
      }

      if (request.excludedPaths().contains(path)) {
        if (declaration.rule() == ModellingRule.MANDATORY) {
          diagnostics.add(
              InstantiationDiagnostic.planError(
                  InstantiationDiagnostic.Code.MANDATORY_OMITTED,
                  "Mandatory declaration at "
                      + path
                      + " was excluded but its parent path exists (Part 3 §6.4.4.4.2)",
                  path,
                  declaration.declarationNodeId()));
        }
        if (declaration.rule() == ModellingRule.MANDATORY_PLACEHOLDER) {
          diagnostics.add(
              InstantiationDiagnostic.planError(
                  InstantiationDiagnostic.Code.MANDATORY_PLACEHOLDER_UNSATISFIED,
                  "MandatoryPlaceholder at "
                      + path
                      + " was excluded, but it requires at least one realization while its parent"
                      + " path exists (Part 3 §6.4.4.4.5)",
                  path,
                  declaration.declarationNodeId()));
        }
        skip(declaration, SkippedDeclaration.Reason.EXCLUDED);
        return;
      }

      switch (declaration.rule()) {
        case MANDATORY -> plan(declaration);
        case OPTIONAL -> {
          if (isSelected(declaration, true)) {
            plan(declaration);
          } else {
            skip(declaration, SkippedDeclaration.Reason.OPTIONAL_NOT_SELECTED);
          }
        }
        case OTHER -> {
          if (isSelected(declaration, false)) {
            plan(declaration);
          } else {
            skip(declaration, SkippedDeclaration.Reason.VENDOR_RULE);
          }
        }
        case OPTIONAL_PLACEHOLDER, MANDATORY_PLACEHOLDER -> {
          // The placeholder occurrence itself is never materialized; its realizations are planned
          // as sibling paths by expansion.
          skip(declaration, SkippedDeclaration.Reason.PLACEHOLDER);

          List<PlaceholderRealization> realizations =
              request.placeholderExpansions().getOrDefault(path, List.of());

          if (!realizations.isEmpty()) {
            consumedExpansions.add(path);
            expandPlaceholder(declaration, realizations);
          } else if (declaration.rule() == ModellingRule.MANDATORY_PLACEHOLDER) {
            diagnostics.add(
                InstantiationDiagnostic.planError(
                    InstantiationDiagnostic.Code.MANDATORY_PLACEHOLDER_UNSATISFIED,
                    "MandatoryPlaceholder at "
                        + path
                        + " requires at least one realization; none is bound by this request"
                        + " (Part 3 §6.4.4.4.5)",
                    path,
                    declaration.declarationNodeId()));
          }
        }
        case EXPOSES_ITS_ARRAY -> {
          skip(declaration, SkippedDeclaration.Reason.EXPOSES_ITS_ARRAY);
          diagnostics.add(
              InstantiationDiagnostic.planWarning(
                  InstantiationDiagnostic.Code.EXPOSES_ITS_ARRAY_NOT_MATERIALIZED,
                  "ExposesItsArray declaration at " + path + " is not materialized",
                  path,
                  declaration.declarationNodeId()));
        }
      }
    }

    private boolean isSelected(InstanceDeclaration declaration, boolean isOptional) {
      BrowsePath path = declaration.browsePath();

      // Selecting a nested path implies its ancestors — but only a path that resolves (in the
      // model or in an expanded placeholder's grafted subtree) implies anything; a mistyped
      // include must not materialize its ancestors (it is reported by the UNMATCHED_PATH warning
      // instead).
      for (BrowsePath included : request.includedPaths()) {
        if (included.startsWith(path) && resolvesInPlan(included)) {
          return true;
        }
      }

      // Binding realizations to a nested placeholder implies its ancestors the same way.
      for (BrowsePath expansion : request.placeholderExpansions().keySet()) {
        if (expansion.startsWith(path) && resolvesInPlan(expansion)) {
          return true;
        }
      }

      if (isOptional && request.includeAllOptionals()) {
        return true;
      }

      return request.includePredicate().map(p -> p.test(declaration)).orElse(false);
    }

    /**
     * Resolve the declaration occurrence a request path names: a declaration of the root model, or
     * — inside an expanded placeholder's realized subtree — a declaration of the grafted
     * member-type model. A grafted declaration is returned as the member model holds it, with its
     * BrowsePath relative to the realization.
     */
    private Optional<InstanceDeclaration> declarationAt(BrowsePath path) {
      Optional<InstanceDeclaration> declaration = model.get(path);
      if (declaration.isPresent()) {
        return declaration;
      }

      for (Map.Entry<BrowsePath, TypeInstantiationModel> graft : graftedModels.entrySet()) {
        BrowsePath realizedPath = graft.getKey();
        if (path.startsWith(realizedPath) && path.depth() > realizedPath.depth()) {
          BrowsePath relative =
              BrowsePath.of(path.elements().subList(realizedPath.depth(), path.depth()));
          Optional<InstanceDeclaration> grafted = graft.getValue().get(relative);
          if (grafted.isPresent()) {
            return grafted;
          }
        }
      }

      return Optional.empty();
    }

    /** Whether a request path names a declaration this plan can reach. */
    private boolean resolvesInPlan(BrowsePath path) {
      return declarationAt(path).isPresent();
    }

    private void skip(InstanceDeclaration declaration, SkippedDeclaration.Reason reason) {
      skipped.put(declaration.browsePath(), new SkippedDeclaration(declaration, reason));
    }

    // endregion

    // region Placeholder expansion

    /**
     * Realize the request's bindings for one placeholder. Each realization is planned as a sibling
     * of the placeholder path, carrying the placeholder declaration's attributes over its
     * (concrete-resolved) type's, attached by the same edges that attached the placeholder — and
     * the effective type's full member hierarchy is grafted beneath it, relocated under the
     * realized path and classified through the ordinary machinery. Grafting happens here, per
     * realization, because placeholders are recorded shallowly in the model (Part 3 §6.4.4.4.4–.5).
     */
    private void expandPlaceholder(
        InstanceDeclaration placeholder, List<PlaceholderRealization> realizations)
        throws UaException {

      BrowsePath placeholderPath = placeholder.browsePath();
      BrowsePath parentPath = placeholderPath.parent();

      // Every edge that attaches the placeholder attaches each realization: several references
      // between one declaration pair map onto the same realized pair (Part 3 §6.4.3).
      List<DeclarationEdge> attachingEdges =
          Stream.concat(model.hierarchy().stream(), graftedEdges.stream())
              .filter(edge -> edge.childPath().equals(placeholderPath))
              .toList();

      // Rows recorded from the placeholder declaration (HasTypeDefinition included) re-source at
      // each realization; the HasTypeDefinition substitution then targets the effective type.
      List<ReferenceRow> placeholderRows =
          Stream.concat(model.references().stream(), graftedRows.stream())
              .filter(row -> row.sourcePath().equals(placeholderPath))
              .toList();

      for (PlaceholderRealization realization : realizations) {
        BrowsePath realizedPath = parentPath.append(realization.browseName());

        if (planned.containsKey(realizedPath)
            || skipped.containsKey(realizedPath)
            || resolvesInPlan(realizedPath)) {
          diagnostics.add(
              InstantiationDiagnostic.planError(
                  InstantiationDiagnostic.Code.PLACEHOLDER_EXPANSION_INVALID,
                  "realization "
                      + realization.browseName()
                      + " of placeholder "
                      + placeholderPath
                      + " collides with the declaration or realization at "
                      + realizedPath,
                  realizedPath,
                  realization.nodeId()));
          continue;
        }

        InstanceDeclaration realized =
            new InstanceDeclaration(
                realizedPath,
                realization.browseName(),
                placeholder.nodeClass(),
                placeholder.declarationNodeId(),
                placeholder.declaringTypeId(),
                placeholder.overriddenDeclarations(),
                placeholder.modellingRuleId(),
                placeholder.rule(),
                placeholder.typeDefinitionId(),
                realizationAttributes(placeholder, realization));

        NodeId concreteTypeId =
            realization.typeDefinitionId() != null
                ? realization.typeDefinitionId()
                : request.concreteTypes().get(realizedPath);

        NodeId effectiveTypeId = planMember(realized, concreteTypeId, true);

        Working working = planned.get(realizedPath);
        if (working != null && working.nodeId == null && realization.nodeId() != null) {
          working.nodeId = realization.nodeId();
        }

        for (DeclarationEdge edge : attachingEdges) {
          graftedEdges.add(
              new DeclarationEdge(edge.parentPath(), edge.referenceTypeId(), realizedPath));
        }

        for (ReferenceRow row : placeholderRows) {
          graftedRows.add(relocateSource(row, realizedPath));
        }

        if (effectiveTypeId == null) {
          // A Method placeholder realization has no member model to graft.
          continue;
        }

        TypeInstantiationModel memberModel = memberModel(effectiveTypeId, realizedPath);
        if (memberModel == null) {
          // The effective type does not compile; the error is already on the plan.
          continue;
        }

        // Registered before the subtree is classified so nested includes, nested expansion
        // bindings, and collision checks resolve against this graft.
        graftedModels.put(realizedPath, memberModel);

        for (DeclarationEdge edge : memberModel.hierarchy()) {
          graftedEdges.add(
              new DeclarationEdge(
                  realizedPath.concat(edge.parentPath()),
                  edge.referenceTypeId(),
                  realizedPath.concat(edge.childPath())));
        }

        for (ReferenceRow row : memberModel.references()) {
          graftedRows.add(relocate(row, realizedPath));
        }

        for (InstanceDeclaration member : memberModel.declarations()) {
          classify(member.withBrowsePath(realizedPath.concat(member.browsePath())));
        }
      }
    }

    /**
     * The realization's attributes are the placeholder declaration's — the attribute policy's
     * "declaration wins" tier — with BrowseName and DisplayName replaced by the realization's name:
     * a placeholder's own names are markers like {@code <ChannelName>}, not instance names.
     */
    private static AttributeSnapshot realizationAttributes(
        InstanceDeclaration placeholder, PlaceholderRealization realization) {

      AttributeSnapshot declared = placeholder.attributes();

      AttributeSnapshot.Builder b = AttributeSnapshot.builder();
      for (AttributeId id : declared.attributeIds()) {
        b.put(id, declared.isNull(id) ? null : declared.getOrNull(id));
      }
      b.put(AttributeId.BrowseName, realization.browseName());
      b.put(AttributeId.DisplayName, new LocalizedText(realization.browseName().name()));
      return b.build();
    }

    /**
     * Relocate a member-model row under a realization: relative rows move both ends; absolute rows
     * keep their verbatim target.
     */
    private static ReferenceRow relocate(ReferenceRow row, BrowsePath realizedPath) {
      if (row.isRelative()) {
        return ReferenceRow.relative(
            realizedPath.concat(row.sourcePath()),
            row.referenceTypeId(),
            realizedPath.concat(Objects.requireNonNull(row.targetPath())));
      }
      return ReferenceRow.absolute(
          realizedPath.concat(row.sourcePath()),
          row.referenceTypeId(),
          row.direction(),
          Objects.requireNonNull(row.targetNodeId()));
    }

    /** Re-source a placeholder-declaration row at the realization's path. */
    private static ReferenceRow relocateSource(ReferenceRow row, BrowsePath realizedPath) {
      if (row.isRelative()) {
        return ReferenceRow.relative(
            realizedPath, row.referenceTypeId(), Objects.requireNonNull(row.targetPath()));
      }
      return ReferenceRow.absolute(
          realizedPath,
          row.referenceTypeId(),
          row.direction(),
          Objects.requireNonNull(row.targetNodeId()));
    }

    /**
     * Expansion is a directive to create, so any binding that never reached an expandable
     * placeholder — mistyped, excluded, or under an omitted ancestor — is an error, not a silent
     * drop.
     */
    private void validateExpansions() {
      for (BrowsePath path : sorted(request.placeholderExpansions().keySet())) {
        if (!consumedExpansions.contains(path)) {
          diagnostics.add(
              InstantiationDiagnostic.planError(
                  InstantiationDiagnostic.Code.PLACEHOLDER_EXPANSION_INVALID,
                  "expandPlaceholder path "
                      + path
                      + " does not name an expandable placeholder in this plan",
                  path,
                  null));
        }
      }
    }

    // endregion

    // region Member resolution

    private void plan(InstanceDeclaration declaration) throws UaException {
      planMember(declaration, request.concreteTypes().get(declaration.browsePath()), false);
    }

    /**
     * Plan one member occurrence. {@code expandsSubtree} marks a placeholder realization, whose
     * effective type's full member hierarchy is grafted beneath it — suppressing the
     * CONCRETE_TYPE_NOT_EXPANDED warning that applies to ordinary members.
     *
     * @return the effective (concrete-resolved) member type; {@code null} for Methods.
     */
    private @Nullable NodeId planMember(
        InstanceDeclaration declaration, @Nullable NodeId concreteTypeId, boolean expandsSubtree)
        throws UaException {

      BrowsePath path = declaration.browsePath();

      if (declaration.nodeClass() == NodeClass.Method) {
        planMethod(declaration);
        return null;
      }

      NodeId declaredTypeId =
          Objects.requireNonNull(declaration.typeDefinitionId(), "typeDefinitionId");
      NodeId effectiveTypeId = declaredTypeId;

      if (concreteTypeId != null && !concreteTypeId.equals(declaredTypeId)) {
        if (isTypeSubtypeOfOrEqual(concreteTypeId, declaredTypeId)) {
          effectiveTypeId = concreteTypeId;
        } else {
          diagnostics.add(
              InstantiationDiagnostic.planError(
                  InstantiationDiagnostic.Code.CONCRETE_TYPE_INVALID,
                  "concreteType "
                      + concreteTypeId
                      + " for "
                      + path
                      + " is not declared type "
                      + declaredTypeId
                      + " or a subtype",
                  path,
                  concreteTypeId));
        }
      }

      TypeInstantiationModel memberModel = memberModel(effectiveTypeId, path);

      if (memberModel != null) {
        validateMemberType(declaration, memberModel, effectiveTypeId, expandsSubtree);
      }

      AttributeSnapshot effective = memberEffectiveAttributes(declaration, memberModel);

      ResolvedClass resolved = resolveJavaClass(effectiveTypeId, declaration.nodeClass());

      planned.put(
          path,
          new Working(
              path,
              declaration,
              declaration.nodeClass(),
              effectiveTypeId,
              resolved.javaClass(),
              resolved.constructorTypeId(),
              effective));

      return effectiveTypeId;
    }

    private void planMethod(InstanceDeclaration declaration) {
      BrowsePath path = declaration.browsePath();

      // A Method signature finding on the declaration's own path is worth surfacing per request.
      model.diagnostics().stream()
          .filter(d -> d.code() == ModelDiagnostic.Code.METHOD_SIGNATURE_MISMATCH)
          .filter(d -> path.equals(d.browsePath()))
          .findFirst()
          .ifPresent(
              d ->
                  diagnostics.add(
                      InstantiationDiagnostic.planWarning(
                          InstantiationDiagnostic.Code.METHOD_SIGNATURE_MISMATCH,
                          d.message(),
                          path,
                          declaration.declarationNodeId())));

      Working working =
          new Working(
              path,
              declaration,
              NodeClass.Method,
              null,
              UaMethodNode.class,
              null,
              declaration.attributes());

      if (request.methodInstantiation() == MethodInstantiation.SHARE) {
        working.materialization = PlannedNode.Materialization.SHARE;
        working.nodeId = declaration.declarationNodeId();
      }

      planned.put(path, working);
    }

    private void validateMemberType(
        InstanceDeclaration declaration,
        TypeInstantiationModel memberModel,
        NodeId effectiveTypeId,
        boolean expandsSubtree) {

      BrowsePath path = declaration.browsePath();
      boolean isConcreteOverride = !effectiveTypeId.equals(declaration.typeDefinitionId());

      AttributeSnapshot memberTypeAttributes = memberModel.rootAttributes();

      NodeClass memberTypeNodeClass =
          (NodeClass) memberTypeAttributes.getOrNull(AttributeId.NodeClass);
      boolean classCompatible =
          (declaration.nodeClass() == NodeClass.Object
                  && memberTypeNodeClass == NodeClass.ObjectType)
              || (declaration.nodeClass() == NodeClass.Variable
                  && memberTypeNodeClass == NodeClass.VariableType);
      if (isConcreteOverride && !classCompatible) {
        diagnostics.add(
            InstantiationDiagnostic.planError(
                InstantiationDiagnostic.Code.CONCRETE_TYPE_INVALID,
                "concreteType "
                    + effectiveTypeId
                    + " for "
                    + path
                    + " has NodeClass "
                    + memberTypeNodeClass,
                path,
                effectiveTypeId));
      }

      if (Boolean.TRUE.equals(memberTypeAttributes.getOrNull(AttributeId.IsAbstract))) {
        if (isConcreteOverride) {
          diagnostics.add(
              InstantiationDiagnostic.planError(
                  InstantiationDiagnostic.Code.CONCRETE_TYPE_INVALID,
                  "concreteType " + effectiveTypeId + " for " + path + " is itself abstract",
                  path,
                  effectiveTypeId));
        } else {
          diagnostics.add(
              InstantiationDiagnostic.planError(
                  InstantiationDiagnostic.Code.ABSTRACT_MEMBER_TYPE,
                  "member type "
                      + effectiveTypeId
                      + " at "
                      + path
                      + " is abstract; select a concrete subtype via concreteType(path, typeId)",
                  path,
                  effectiveTypeId));
        }
      }

      if (isConcreteOverride && !expandsSubtree) {
        // The plan realizes the *declared* type's members; a concrete subtype's additional
        // members are not expanded in this release. Say so instead of silently omitting them.
        // (A placeholder realization is exempt: its effective type's full hierarchy IS grafted.)
        boolean hasUnmodeledMembers =
            memberModel.declarations().stream()
                .map(md -> path.concat(md.browsePath()))
                .anyMatch(p -> model.get(p).isEmpty());
        if (hasUnmodeledMembers) {
          diagnostics.add(
              InstantiationDiagnostic.planWarning(
                  InstantiationDiagnostic.Code.CONCRETE_TYPE_NOT_EXPANDED,
                  "concreteType "
                      + effectiveTypeId
                      + " for "
                      + path
                      + " declares members beyond "
                      + declaration.typeDefinitionId()
                      + "; they are not expanded",
                  path,
                  effectiveTypeId));
        }
      }
    }

    /**
     * Member-type attribute defaults per the attribute policy: a Variable declaration's absent (or
     * explicitly unset Value) attributes fall back to its (concrete-resolved) VariableType's,
     * before class defaults apply at construction.
     */
    private AttributeSnapshot memberEffectiveAttributes(
        InstanceDeclaration declaration, @Nullable TypeInstantiationModel memberModel) {

      AttributeSnapshot declared = declaration.attributes();

      if (declaration.nodeClass() != NodeClass.Variable || memberModel == null) {
        return declared;
      }

      AttributeSnapshot typeAttributes = memberModel.rootAttributes();

      AttributeSnapshot.Builder b = AttributeSnapshot.builder();
      for (AttributeId id : declared.attributeIds()) {
        b.put(id, declared.isNull(id) ? null : declared.getOrNull(id));
      }

      for (AttributeId id :
          List.of(
              AttributeId.Value,
              AttributeId.DataType,
              AttributeId.ValueRank,
              AttributeId.ArrayDimensions)) {
        boolean declaredProvides;
        if (id == AttributeId.Value) {
          // A Variable's Value attribute always exists; "no value declared" is a DataValue
          // carrying a null Variant (the Bad_NoValue convention), not an absent attribute.
          DataValue declaredValue = declared.value();
          declaredProvides = declaredValue != null && declaredValue.getValue().getValue() != null;
        } else {
          declaredProvides = declared.contains(id) && !declared.isNull(id);
        }
        if (!declaredProvides && typeAttributes.contains(id) && !typeAttributes.isNull(id)) {
          b.put(id, typeAttributes.getOrNull(id));
        }
      }

      return b.build();
    }

    private @Nullable TypeInstantiationModel memberModel(NodeId typeId, BrowsePath path)
        throws UaException {

      if (memberModels.containsKey(typeId)) {
        return memberModels.get(typeId);
      }

      TypeInstantiationModel memberModel;
      try {
        memberModel = instantiator.describe(typeId);
      } catch (ModelCompilationException e) {
        // The main model compiled, so a failing member compile means a concrete-type selection
        // that does not itself compile.
        diagnostics.add(
            InstantiationDiagnostic.planError(
                InstantiationDiagnostic.Code.CONCRETE_TYPE_INVALID,
                "type " + typeId + " for " + path + " does not compile: " + e.getMessage(),
                path,
                typeId));
        memberModel = null;
      }

      memberModels.put(typeId, memberModel);
      return memberModel;
    }

    // endregion

    // region NodeId allocation and collision preflight

    private void allocateNodeIds() {
      for (Working working : planned.values()) {
        if (working.nodeId != null) {
          // The root and shared Methods are already resolved.
          continue;
        }

        NodeId pinned = request.assignedNodeIds().get(working.path);
        if (pinned != null) {
          working.nodeId = pinned;
          continue;
        }

        NodeIdContext context =
            new NodeIdContext(
                request.rootNodeId(),
                working.path,
                Objects.requireNonNull(working.declaration, "declaration"));

        working.nodeId =
            request
                .nodeIdStrategy()
                .map(strategy -> strategy.apply(context))
                .orElseGet(
                    () ->
                        request.legacyPathStrings()
                            ? context.legacyNodeId()
                            : context.defaultNodeId());
      }
    }

    private void preflightCollisions() {
      Map<NodeId, BrowsePath> intraPlan = new HashMap<>();

      for (Working working : planned.values()) {
        if (working.materialization == PlannedNode.Materialization.SHARE) {
          continue;
        }

        NodeId nodeId = Objects.requireNonNull(working.nodeId);

        BrowsePath previous = intraPlan.putIfAbsent(nodeId, working.path);
        if (previous != null) {
          diagnostics.add(
              InstantiationDiagnostic.planError(
                  InstantiationDiagnostic.Code.NODE_ID_COLLISION,
                  "planned NodeId "
                      + nodeId
                      + " at "
                      + working.path
                      + " collides with the node planned at "
                      + previous,
                  working.path,
                  nodeId));
          continue;
        }

        if (!request.target().containsNode(nodeId)) {
          continue;
        }

        if (request.conflictPolicy() == ConflictPolicy.FAIL) {
          diagnostics.add(
              InstantiationDiagnostic.planError(
                  InstantiationDiagnostic.Code.NODE_ID_COLLISION,
                  "planned NodeId "
                      + nodeId
                      + " at "
                      + working.path
                      + " already exists in the target NodeManager",
                  working.path,
                  nodeId));
        } else {
          UaNode existing = request.target().getNode(nodeId).orElse(null);
          String incompatibility = reuseIncompatibility(existing, working);
          if (incompatibility == null) {
            working.materialization = PlannedNode.Materialization.REUSE;
          } else {
            diagnostics.add(
                InstantiationDiagnostic.planError(
                    InstantiationDiagnostic.Code.INCOMPATIBLE_REUSE,
                    "existing node at "
                        + nodeId
                        + " cannot be reused for "
                        + working.path
                        + ": "
                        + incompatibility,
                    working.path,
                    nodeId));
          }
        }
      }
    }

    private @Nullable String reuseIncompatibility(@Nullable UaNode existing, Working working) {
      return instantiator.reuseIncompatibility(
          existing,
          (QualifiedName) working.effectiveAttributes.getOrNull(AttributeId.BrowseName),
          working.nodeClass,
          working.isRoot(),
          request.rootClass(),
          working.typeDefinitionId,
          Objects.requireNonNull(working.nodeId),
          request.target());
    }

    // endregion

    // region References

    private List<Reference> resolveReferences() {
      List<Reference> references = new ArrayList<>();

      NodeId rootNodeId = request.rootNodeId();

      Working root = planned.get(BrowsePath.root());
      if (root != null && root.materialization == PlannedNode.Materialization.CREATE) {
        references.add(
            new Reference(
                rootNodeId, NodeIds.HasTypeDefinition, model.typeDefinitionId().expanded(), true));
      }

      if (request.parentNodeId().isPresent()) {
        NodeId parentNodeId = request.parentNodeId().get();
        NodeId parentReferenceTypeId =
            request.parentReferenceTypeId().orElseGet(this::placeholderParentReferenceTypeId);

        //noinspection StatementWithEmptyBody
        if (parentReferenceTypeId == null) {
          // A forPlaceholder request whose declaration attachment could not be read back; the
          // error diagnostic is already recorded.
        } else if (!isReferenceSubtypeOfOrEqual(
            parentReferenceTypeId, NodeIds.HierarchicalReferences)) {
          diagnostics.add(
              InstantiationDiagnostic.planError(
                  InstantiationDiagnostic.Code.INVALID_PARENT,
                  "parent reference type "
                      + parentReferenceTypeId
                      + " is not a hierarchical ReferenceType",
                  BrowsePath.root(),
                  parentNodeId));
        } else {
          // The parent may live in any NodeManager, so resolution is server-wide with a target
          // fallback; a missing parent is advisory at plan time — the apply-stage recheck fails
          // if it is still missing, so no dangling attachment is committed.
          if (server.getAddressSpaceManager().getManagedNode(parentNodeId).isEmpty()
              && !request.target().containsNode(parentNodeId)) {
            diagnostics.add(
                InstantiationDiagnostic.planWarning(
                    InstantiationDiagnostic.Code.INVALID_PARENT,
                    "parent node " + parentNodeId + " does not resolve in this server at plan time",
                    BrowsePath.root(),
                    parentNodeId));
          }
          references.add(
              new Reference(parentNodeId, parentReferenceTypeId, rootNodeId.expanded(), true));
        }
      }

      for (DeclarationEdge edge : model.hierarchy()) {
        resolveEdge(edge, references);
      }
      for (DeclarationEdge edge : graftedEdges) {
        resolveEdge(edge, references);
      }

      ReferenceReplicationPolicy policy = request.referenceReplication();

      for (ReferenceRow row : model.references()) {
        resolveRow(row, policy, references);
      }
      for (ReferenceRow row : graftedRows) {
        resolveRow(row, policy, references);
      }

      return references;
    }

    private void resolveEdge(DeclarationEdge edge, List<Reference> references) {
      Working parent = planned.get(edge.parentPath());
      Working child = planned.get(edge.childPath());
      if (parent == null || child == null) {
        return;
      }
      references.add(
          new Reference(
              Objects.requireNonNull(parent.nodeId),
              edge.referenceTypeId(),
              Objects.requireNonNull(child.nodeId).expanded(),
              true));
    }

    private void resolveRow(
        ReferenceRow row, ReferenceReplicationPolicy policy, List<Reference> references) {

      Working source = planned.get(row.sourcePath());
      if (source == null || source.materialization != PlannedNode.Materialization.CREATE) {
        // Skipped sources have no instance; reused and shared nodes already carry their own
        // non-hierarchy references.
        return;
      }

      NodeId sourceNodeId = Objects.requireNonNull(source.nodeId);
      NodeId referenceTypeId = row.referenceTypeId();

      if (isReferenceSubtypeOfOrEqual(referenceTypeId, NodeIds.HasTypeDefinition)) {
        // Substituted rather than copied: a concreteType selection redirects the instance's
        // type; every created instance gets exactly its effective type.
        references.add(
            new Reference(
                sourceNodeId,
                referenceTypeId,
                Objects.requireNonNull(source.typeDefinitionId).expanded(),
                true));
        return;
      }

      if (isReferenceSubtypeOfOrEqual(referenceTypeId, NodeIds.HasModellingRule)) {
        if (request.purpose() == InstantiationPurpose.INSTANCE_DECLARATION) {
          references.add(
              new Reference(
                  sourceNodeId,
                  referenceTypeId,
                  Objects.requireNonNull(row.targetNodeId()),
                  row.direction() == Reference.Direction.FORWARD));
        }
        return;
      }

      if (row.isRelative()) {
        if (policy.internal() == ReferenceReplicationPolicy.InternalReferences.OMIT) {
          return;
        }
        Working target = planned.get(row.targetPath());
        if (target == null) {
          // No planned edge to an omitted target.
          return;
        }
        references.add(
            new Reference(
                sourceNodeId,
                referenceTypeId,
                Objects.requireNonNull(target.nodeId).expanded(),
                true));
      } else {
        if (policy.external() == ReferenceReplicationPolicy.ExternalReferences.COPY) {
          references.add(
              new Reference(
                  sourceNodeId,
                  referenceTypeId,
                  Objects.requireNonNull(row.targetNodeId()),
                  row.direction() == Reference.Direction.FORWARD));
        }
      }
    }

    /**
     * A forPlaceholder request that did not set the parent ReferenceType explicitly attaches its
     * realization with the ReferenceType connecting the placeholder declaration to its own parent,
     * read back from the declaration node's inverse hierarchical references (deterministically the
     * smallest ReferenceType NodeId if several parents attach it). Unresolvable — the declaration
     * node is gone or carries no inverse hierarchical reference — is a plan error naming the fix.
     */
    private @Nullable NodeId placeholderParentReferenceTypeId() {
      InstantiationRequest.PlaceholderOrigin origin = request.placeholderOrigin().orElse(null);
      if (origin == null) {
        // Unreachable via the builder: parent() requires both values, so a missing reference type
        // implies a forPlaceholder request. Guard with the common default anyway.
        return NodeIds.HasComponent;
      }

      NodeId declarationNodeId = origin.declaration().declarationNodeId();

      Optional<NodeId> referenceTypeId =
          server.getAddressSpaceManager().getManagedReferences(declarationNodeId).stream()
              .filter(r -> !r.isForward())
              .map(Reference::getReferenceTypeId)
              .filter(typeId -> isReferenceSubtypeOfOrEqual(typeId, NodeIds.HierarchicalReferences))
              .min(TypeSpaceRules.NODE_ID_ORDER);

      if (referenceTypeId.isEmpty()) {
        diagnostics.add(
            InstantiationDiagnostic.planError(
                InstantiationDiagnostic.Code.INVALID_PARENT,
                "the ReferenceType attaching placeholder declaration "
                    + declarationNodeId
                    + " to its parent cannot be resolved; set parent(parentNodeId, referenceTypeId)"
                    + " explicitly",
                BrowsePath.root(),
                declarationNodeId));
        return null;
      }

      return referenceTypeId.get();
    }

    // endregion

    // region Unmatched request paths

    private void warnUnmatchedPaths() {
      for (BrowsePath path : sorted(request.includedPaths())) {
        Optional<InstanceDeclaration> declaration = declarationAt(path);
        if (declaration.isEmpty()) {
          warnUnmatched("includeOptional", path);
        } else if (declaration.get().isPlaceholder()) {
          diagnostics.add(
              InstantiationDiagnostic.planWarning(
                  InstantiationDiagnostic.Code.UNMATCHED_PATH,
                  "includeOptional path "
                      + path
                      + " names a placeholder; placeholders are realized by expansion, not"
                      + " selection",
                  path,
                  null));
        }
      }

      for (BrowsePath path : sorted(request.excludedPaths())) {
        if (!resolvesInPlan(path)) {
          warnUnmatched("excludeOptional", path);
        }
      }

      for (BrowsePath path : sorted(request.assignedNodeIds().keySet())) {
        Working working = planned.get(path);
        if (working == null || working.materialization == PlannedNode.Materialization.SHARE) {
          warnUnmatched("assignNodeId", path);
        }
      }

      for (BrowsePath path : sorted(request.concreteTypes().keySet())) {
        if (!planned.containsKey(path)) {
          warnUnmatched("concreteType", path);
        }
      }

      for (BrowsePath path : sorted(request.methodBinders().keySet())) {
        Working working = planned.get(path);
        if (working == null || working.nodeClass != NodeClass.Method) {
          warnUnmatched("bindMethod", path);
        }
      }
    }

    private void warnUnmatched(String axis, BrowsePath path) {
      diagnostics.add(
          InstantiationDiagnostic.planWarning(
              InstantiationDiagnostic.Code.UNMATCHED_PATH,
              axis + " path " + path + " matches nothing this plan can affect",
              path,
              null));
    }

    private static List<BrowsePath> sorted(Set<BrowsePath> paths) {
      return paths.stream().sorted().toList();
    }

    // endregion

    // region Type space helpers

    private ResolvedClass resolveJavaClass(NodeId typeId, NodeClass instanceNodeClass) {
      if (instanceNodeClass == NodeClass.Object) {
        ObjectTypeManager typeManager = server.getObjectTypeManager();

        NodeId current = typeId;
        Set<NodeId> visited = new HashSet<>();
        while (current != null && visited.add(current)) {
          ObjectTypeManager.RegisteredObjectType registered =
              typeManager.getRegisteredType(current).orElse(null);
          if (registered != null) {
            return new ResolvedClass(registered.nodeClass(), current);
          }
          if (request.classResolution() == InstantiationRequest.ClassResolution.EXACT) {
            break;
          }
          current = immediateSupertype(current);
        }

        return new ResolvedClass(UaObjectNode.class, null);
      } else {
        VariableTypeManager typeManager = server.getVariableTypeManager();

        NodeId current = typeId;
        Set<NodeId> visited = new HashSet<>();
        while (current != null && visited.add(current)) {
          VariableTypeManager.RegisteredVariableType registered =
              typeManager.getRegisteredType(current).orElse(null);
          if (registered != null) {
            return new ResolvedClass(registered.nodeClass(), current);
          }
          if (request.classResolution() == InstantiationRequest.ClassResolution.EXACT) {
            break;
          }
          current = immediateSupertype(current);
        }

        return new ResolvedClass(UaVariableNode.class, null);
      }
    }

    private boolean isTypeSubtypeOfOrEqual(NodeId typeId, NodeId supertypeId) {
      return instantiator.isTypeSubtypeOfOrEqual(typeId, supertypeId);
    }

    private @Nullable NodeId immediateSupertype(NodeId typeId) {
      return TypeSpaceRules.immediateSupertype(
          typeId,
          id -> server.getAddressSpaceManager().getManagedReferences(id),
          server.getNamespaceTable());
    }

    private boolean isReferenceSubtypeOfOrEqual(NodeId referenceTypeId, NodeId supertypeId) {
      return TypeSpaceRules.isReferenceSubtypeOfOrEqual(
          server.getReferenceTypeTree(), referenceTypeId, supertypeId);
    }

    private static boolean isValueRankRestriction(int base, int restricted) {
      return TypeSpaceRules.isValueRankRestriction(base, restricted);
    }

    private static boolean isArrayDimensionsRestriction(UInteger[] base, UInteger[] restricted) {
      return TypeSpaceRules.isArrayDimensionsRestriction(base, restricted);
    }

    // endregion

    private record ResolvedClass(
        Class<? extends UaNode> javaClass, @Nullable NodeId constructorTypeId) {}

    /** Mutable per-node working state, finalized into a {@link PlannedNode}. */
    private static final class Working {

      final BrowsePath path;
      final @Nullable InstanceDeclaration declaration;
      final NodeClass nodeClass;
      final @Nullable NodeId typeDefinitionId;
      final Class<? extends UaNode> javaClass;
      final @Nullable NodeId constructorTypeId;
      final AttributeSnapshot effectiveAttributes;

      @Nullable NodeId nodeId;
      PlannedNode.Materialization materialization = PlannedNode.Materialization.CREATE;

      Working(
          BrowsePath path,
          @Nullable InstanceDeclaration declaration,
          NodeClass nodeClass,
          @Nullable NodeId typeDefinitionId,
          Class<? extends UaNode> javaClass,
          @Nullable NodeId constructorTypeId,
          AttributeSnapshot effectiveAttributes) {

        this.path = path;
        this.declaration = declaration;
        this.nodeClass = nodeClass;
        this.typeDefinitionId = typeDefinitionId;
        this.javaClass = javaClass;
        this.constructorTypeId = constructorTypeId;
        this.effectiveAttributes = effectiveAttributes;
      }

      boolean isRoot() {
        return path.isRoot();
      }

      PlannedNode toPlannedNode() {
        return new PlannedNode(
            path,
            declaration,
            Objects.requireNonNull(nodeId),
            nodeClass,
            typeDefinitionId,
            javaClass,
            constructorTypeId,
            effectiveAttributes,
            materialization);
      }
    }
  }

  private boolean isTypeSubtypeOfOrEqual(NodeId typeId, NodeId supertypeId) {
    return TypeSpaceRules.isTypeSubtypeOfOrEqual(
        typeId,
        supertypeId,
        id -> server.getAddressSpaceManager().getManagedReferences(id),
        server.getNamespaceTable());
  }

  /**
   * Validate reuse compatibility, shared by plan preflight and apply recheck: namespace-qualified
   * BrowseName, NodeClass, TypeDefinition (the planned type or a subtype, read from the target's
   * reference rows), and — for the root — the expected Java class the typed result promises.
   *
   * @return {@code null} if compatible, otherwise a description of the incompatibility.
   */
  private @Nullable String reuseIncompatibility(
      @Nullable UaNode existing,
      @Nullable QualifiedName plannedBrowseName,
      NodeClass plannedNodeClass,
      boolean isRoot,
      Class<? extends UaNode> rootClass,
      @Nullable NodeId plannedTypeDefinitionId,
      NodeId nodeId,
      NodeManager<UaNode> target) {

    if (existing == null) {
      return "the node disappeared between preflight checks";
    }

    if (!Objects.equals(existing.getBrowseName(), plannedBrowseName)) {
      return "BrowseName "
          + existing.getBrowseName()
          + " does not match planned "
          + plannedBrowseName;
    }

    if (existing.getNodeClass() != plannedNodeClass) {
      return "NodeClass " + existing.getNodeClass() + " does not match " + plannedNodeClass;
    }

    if (isRoot && !rootClass.isInstance(existing)) {
      return "existing node is not a " + rootClass.getName();
    }

    if (plannedTypeDefinitionId != null) {
      NamespaceTable namespaceTable = server.getNamespaceTable();
      NodeId existingTypeId =
          target.getReferences(nodeId, Reference.HAS_TYPE_DEFINITION_PREDICATE).stream()
              .map(r -> r.getTargetNodeId().toNodeId(namespaceTable).orElse(null))
              .filter(Objects::nonNull)
              .findFirst()
              .orElse(null);

      if (existingTypeId == null
          || !isTypeSubtypeOfOrEqual(existingTypeId, plannedTypeDefinitionId)) {
        return "TypeDefinition "
            + existingTypeId
            + " is not "
            + plannedTypeDefinitionId
            + " or a subtype";
      }
    }

    return null;
  }

  /**
   * One apply: model revalidation, target recheck, unpublished staging, hooks, journaled commit
   * with rollback — the six stages over a single error-free plan.
   */
  private static final class ApplyExecution<T extends UaNode> {

    /**
     * A stale-generation commit means the target advanced between the stage-2 recheck and the
     * commit; the recheck is re-run and the commit retried, bounded so a busy target degrades to a
     * clean failure rather than livelock.
     */
    private static final int MAX_COMMIT_ATTEMPTS = 3;

    /** Attributes consumed by every node constructor form's common prefix. */
    private static final Set<AttributeId> COMMON_CONSTRUCTED =
        EnumSet.of(
            AttributeId.NodeClass,
            AttributeId.BrowseName,
            AttributeId.DisplayName,
            AttributeId.Description,
            AttributeId.WriteMask,
            AttributeId.UserWriteMask,
            AttributeId.RolePermissions,
            AttributeId.UserRolePermissions,
            AttributeId.AccessRestrictions);

    /** Attributes consumed by the Variable tuple constructor beyond the common prefix. */
    private static final Set<AttributeId> VARIABLE_CONSTRUCTED = variableConstructed();

    private static Set<AttributeId> variableConstructed() {
      EnumSet<AttributeId> set = EnumSet.copyOf(COMMON_CONSTRUCTED);
      set.addAll(
          EnumSet.of(
              AttributeId.Value,
              AttributeId.DataType,
              AttributeId.ValueRank,
              AttributeId.ArrayDimensions));
      return set;
    }

    /** Attributes consumed by the Method constructor (security attributes are overlaid). */
    private static final Set<AttributeId> METHOD_CONSTRUCTED =
        EnumSet.of(
            AttributeId.NodeClass,
            AttributeId.BrowseName,
            AttributeId.DisplayName,
            AttributeId.Description,
            AttributeId.WriteMask,
            AttributeId.UserWriteMask,
            AttributeId.Executable,
            AttributeId.UserExecutable);

    private final NodeInstantiator instantiator;
    private final OpcUaServer server;
    private final InstantiationPlan<T> plan;
    private final InstantiationRequest<T> request;
    private final NodeManager<UaNode> target;

    /** Constructed-but-unpublished nodes, by path; CREATE entries only. */
    private final Map<BrowsePath, UaNode> staged = new LinkedHashMap<>();

    /** Existing nodes serving REUSE and SHARE entries, resolved by the recheck. */
    private final Map<BrowsePath, UaNode> adopted = new HashMap<>();

    private ApplyExecution(NodeInstantiator instantiator, InstantiationPlan<T> plan) {
      this.instantiator = instantiator;
      this.server = instantiator.server;
      this.plan = plan;
      this.request = plan.request();
      this.target = request.target();
    }

    InstantiationResult<T> execute() throws UaException {
      revalidateModel();

      long generation = recheckTarget();

      stageNodes();
      runHooks();

      InstantiationResult<T> result = commitAndBuildResult(generation);

      notifyObservers(result);

      return result;
    }

    // region Stage 1: model revalidation

    /**
     * Every model the plan consulted is revalidated, not just the root's: a {@code concreteType}
     * selection outside the root model's dependency closure has its own cache entry, and a stale
     * one would silently commit outdated attribute defaults or an instance of a now-abstract type.
     */
    private void revalidateModel() throws InstantiationException {
      for (Map.Entry<NodeId, Long> consulted : plan.consultedModelRevisions().entrySet()) {
        NodeId typeDefinitionId = consulted.getKey();

        TypeInstantiationModel current;
        try {
          current = instantiator.describe(typeDefinitionId);
        } catch (UaException e) {
          throw failure(
              StatusCodes.Bad_InvalidState,
              InstantiationDiagnostic.applyError(
                  InstantiationDiagnostic.Code.MODEL_CHANGED,
                  "type " + typeDefinitionId + " no longer compiles: " + e.getMessage(),
                  BrowsePath.root(),
                  typeDefinitionId),
              e);
        }

        if (current.modelRevision() != consulted.getValue()) {
          throw failure(
              StatusCodes.Bad_InvalidState,
              InstantiationDiagnostic.applyError(
                  InstantiationDiagnostic.Code.MODEL_CHANGED,
                  "type "
                      + typeDefinitionId
                      + " changed since this plan was computed (model revision "
                      + consulted.getValue()
                      + " -> "
                      + current.modelRevision()
                      + "); re-plan against the current model",
                  BrowsePath.root(),
                  typeDefinitionId),
              null);
        }
      }
    }

    // endregion

    // region Stage 2: target recheck

    /**
     * Recheck the plan's target assumptions and capture the generation the commit will expect, so
     * recheck-and-commit is atomic on targets whose primitives track generations.
     *
     * @return the target generation captured before the recheck reads.
     */
    private long recheckTarget() throws InstantiationException {
      long generation = target.getGeneration().value();

      for (PlannedNode planned : plan.plannedNodes()) {
        switch (planned.materialization()) {
          case CREATE -> {
            if (target.containsNode(planned.nodeId())) {
              throw failure(
                  StatusCodes.Bad_NodeIdExists,
                  InstantiationDiagnostic.applyError(
                      InstantiationDiagnostic.Code.NODE_ID_COLLISION,
                      "planned NodeId "
                          + planned.nodeId()
                          + " at "
                          + planned.browsePath()
                          + " appeared in the target since the plan was computed; re-plan"
                          + " (adoption is a plan-time decision)",
                      planned.browsePath(),
                      planned.nodeId()),
                  null);
            }
          }
          case REUSE -> {
            UaNode existing = target.getNode(planned.nodeId()).orElse(null);

            String incompatibility =
                instantiator.reuseIncompatibility(
                    existing,
                    (QualifiedName) planned.effectiveAttributes().getOrNull(AttributeId.BrowseName),
                    planned.nodeClass(),
                    planned.isRoot(),
                    request.rootClass(),
                    planned.typeDefinitionId(),
                    planned.nodeId(),
                    target);

            if (incompatibility != null) {
              throw failure(
                  StatusCodes.Bad_NodeIdExists,
                  InstantiationDiagnostic.applyError(
                      InstantiationDiagnostic.Code.INCOMPATIBLE_REUSE,
                      "existing node at "
                          + planned.nodeId()
                          + " can no longer be reused for "
                          + planned.browsePath()
                          + ": "
                          + incompatibility,
                      planned.browsePath(),
                      planned.nodeId()),
                  null);
            }

            adopted.put(planned.browsePath(), existing);
          }
          case SHARE -> {
            UaNode shared =
                server.getAddressSpaceManager().getManagedNode(planned.nodeId()).orElse(null);

            if (!(shared instanceof UaMethodNode)) {
              throw failure(
                  StatusCodes.Bad_InvalidState,
                  InstantiationDiagnostic.applyError(
                      InstantiationDiagnostic.Code.MODEL_CHANGED,
                      "shared Method declaration "
                          + planned.nodeId()
                          + " at "
                          + planned.browsePath()
                          + " no longer resolves to a Method node",
                      planned.browsePath(),
                      planned.nodeId()),
                  null);
            }

            adopted.put(planned.browsePath(), shared);
          }
        }
      }

      // The commit validates only the batch's own nodes, so a parent that disappeared since plan
      // time would otherwise commit as a reference row sourced at a nonexistent NodeId — and a
      // node later created at that id would silently acquire a phantom child. This covers a
      // forPlaceholder request too, whose parent attachment resolves its ReferenceType lazily and
      // so leaves parentReferenceTypeId absent even though a parent attachment is planned.
      if (request.parentNodeId().isPresent()) {
        NodeId parentNodeId = request.parentNodeId().get();
        if (server.getAddressSpaceManager().getManagedNode(parentNodeId).isEmpty()
            && !target.containsNode(parentNodeId)) {
          throw failure(
              StatusCodes.Bad_ParentNodeIdInvalid,
              InstantiationDiagnostic.applyError(
                  InstantiationDiagnostic.Code.INVALID_PARENT,
                  "parent node "
                      + parentNodeId
                      + " does not resolve in this server; the planned parent attachment would"
                      + " dangle",
                  BrowsePath.root(),
                  parentNodeId),
              null);
        }
      }

      return generation;
    }

    // endregion

    // region Stage 3: staging

    private void stageNodes() throws InstantiationException {
      // Staged nodes see the *destination* as their context: post-commit, node-level navigation
      // (typed getters, getComponent, property lookups) resolves against the target's references.
      UaNodeContext context =
          new UaNodeContext() {
            @Override
            public OpcUaServer getServer() {
              return server;
            }

            @Override
            public NodeManager<UaNode> getNodeManager() {
              return target;
            }
          };

      for (PlannedNode planned : plan.plannedNodes()) {
        if (planned.materialization() != PlannedNode.Materialization.CREATE) {
          continue;
        }

        UaNode node;
        try {
          node = construct(context, planned);
        } catch (Exception e) {
          throw failure(
              StatusCodes.Bad_InternalError,
              InstantiationDiagnostic.applyError(
                  InstantiationDiagnostic.Code.CONSTRUCTOR_FAILED,
                  "constructor failed at " + planned.browsePath() + ": " + e,
                  planned.browsePath(),
                  planned.nodeId()),
              e);
        }

        if (!planned.javaClass().isInstance(node)) {
          throw failure(
              StatusCodes.Bad_InternalError,
              InstantiationDiagnostic.applyError(
                  InstantiationDiagnostic.Code.CONSTRUCTOR_FAILED,
                  "constructor for "
                      + planned.constructorTypeId()
                      + " returned a "
                      + node.getClass().getName()
                      + ", expected "
                      + planned.javaClass().getName(),
                  planned.browsePath(),
                  planned.nodeId()),
              null);
        }

        staged.put(planned.browsePath(), node);
      }
    }

    private UaNode construct(UaNodeContext context, PlannedNode planned) {
      AttributeSnapshot attributes = planned.effectiveAttributes();

      return switch (planned.nodeClass()) {
        case Object -> constructObject(context, planned, attributes);
        case Variable -> constructVariable(context, planned, attributes);
        case Method -> constructMethod(context, planned, attributes);
        default ->
            throw new IllegalStateException("unexpected instance NodeClass " + planned.nodeClass());
      };
    }

    private UaObjectNode constructObject(
        UaNodeContext context, PlannedNode planned, AttributeSnapshot attributes) {

      ObjectTypeManager.RegisteredObjectType registered =
          planned.constructorTypeId() != null
              ? server
                  .getObjectTypeManager()
                  .getRegisteredType(planned.constructorTypeId())
                  .orElse(null)
              : null;

      if (registered != null && registered.snapshotConstructor() != null) {
        return registered.snapshotConstructor().apply(context, planned.nodeId(), attributes);
      }

      UaObjectNode node;
      if (registered != null && registered.nodeConstructor() != null) {
        node =
            registered
                .nodeConstructor()
                .apply(
                    context,
                    planned.nodeId(),
                    (QualifiedName) attributes.getOrNull(AttributeId.BrowseName),
                    (LocalizedText) attributes.getOrNull(AttributeId.DisplayName),
                    (LocalizedText) attributes.getOrNull(AttributeId.Description),
                    (UInteger) attributes.getOrNull(AttributeId.WriteMask),
                    (UInteger) attributes.getOrNull(AttributeId.UserWriteMask),
                    (RolePermissionType[]) attributes.getOrNull(AttributeId.RolePermissions),
                    (RolePermissionType[]) attributes.getOrNull(AttributeId.UserRolePermissions),
                    (AccessRestrictionType) attributes.getOrNull(AttributeId.AccessRestrictions));
      } else {
        UByte eventNotifier = (UByte) attributes.getOrNull(AttributeId.EventNotifier);
        node =
            new UaObjectNode(
                context,
                planned.nodeId(),
                (QualifiedName) attributes.getOrNull(AttributeId.BrowseName),
                (LocalizedText) attributes.getOrNull(AttributeId.DisplayName),
                (LocalizedText) attributes.getOrNull(AttributeId.Description),
                (UInteger) attributes.getOrNull(AttributeId.WriteMask),
                (UInteger) attributes.getOrNull(AttributeId.UserWriteMask),
                (RolePermissionType[]) attributes.getOrNull(AttributeId.RolePermissions),
                (RolePermissionType[]) attributes.getOrNull(AttributeId.UserRolePermissions),
                (AccessRestrictionType) attributes.getOrNull(AttributeId.AccessRestrictions),
                eventNotifier != null ? eventNotifier : ubyte(0));
      }

      // The remaining planned attributes (EventNotifier for the tuple form) — the R4 adaptation
      // overlay that lets tuple-signature registrations receive newly modeled attributes.
      overlay(node, attributes, COMMON_CONSTRUCTED);

      return node;
    }

    private UaVariableNode constructVariable(
        UaNodeContext context, PlannedNode planned, AttributeSnapshot attributes) {

      VariableTypeManager.RegisteredVariableType registered =
          planned.constructorTypeId() != null
              ? server
                  .getVariableTypeManager()
                  .getRegisteredType(planned.constructorTypeId())
                  .orElse(null)
              : null;

      // Committed Variables never share the declaration's DataValue instance: values are re-issued
      // with a fresh source timestamp, and an unset value follows the Bad_NoValue convention.
      DataValue freshValue = freshValue(attributes.value());

      UaVariableNode node;
      if (registered != null && registered.snapshotConstructor() != null) {
        node = registered.snapshotConstructor().apply(context, planned.nodeId(), attributes);
      } else if (registered != null && registered.nodeConstructor() != null) {
        node =
            registered
                .nodeConstructor()
                .apply(
                    context,
                    planned.nodeId(),
                    (QualifiedName) attributes.getOrNull(AttributeId.BrowseName),
                    (LocalizedText) attributes.getOrNull(AttributeId.DisplayName),
                    (LocalizedText) attributes.getOrNull(AttributeId.Description),
                    (UInteger) attributes.getOrNull(AttributeId.WriteMask),
                    (UInteger) attributes.getOrNull(AttributeId.UserWriteMask),
                    (RolePermissionType[]) attributes.getOrNull(AttributeId.RolePermissions),
                    (RolePermissionType[]) attributes.getOrNull(AttributeId.UserRolePermissions),
                    (AccessRestrictionType) attributes.getOrNull(AttributeId.AccessRestrictions),
                    freshValue,
                    attributes.dataType(),
                    attributes.valueRank(),
                    attributes.arrayDimensions());
        overlay(node, attributes, VARIABLE_CONSTRUCTED);
      } else {
        node =
            new UaVariableNode(
                context,
                planned.nodeId(),
                (QualifiedName) attributes.getOrNull(AttributeId.BrowseName),
                (LocalizedText) attributes.getOrNull(AttributeId.DisplayName),
                (LocalizedText) attributes.getOrNull(AttributeId.Description),
                (UInteger) attributes.getOrNull(AttributeId.WriteMask),
                (UInteger) attributes.getOrNull(AttributeId.UserWriteMask),
                (RolePermissionType[]) attributes.getOrNull(AttributeId.RolePermissions),
                (RolePermissionType[]) attributes.getOrNull(AttributeId.UserRolePermissions),
                (AccessRestrictionType) attributes.getOrNull(AttributeId.AccessRestrictions),
                freshValue,
                attributes.dataType(),
                attributes.valueRank(),
                attributes.arrayDimensions());
        overlay(node, attributes, VARIABLE_CONSTRUCTED);
      }

      // Uniform value policy across all constructor forms (a snapshot constructor received the
      // snapshot's normalized value, not the freshened one).
      node.setAttribute(AttributeId.Value, freshValue);

      return node;
    }

    private UaMethodNode constructMethod(
        UaNodeContext context, PlannedNode planned, AttributeSnapshot attributes) {

      Boolean executable = (Boolean) attributes.getOrNull(AttributeId.Executable);
      Boolean userExecutable = (Boolean) attributes.getOrNull(AttributeId.UserExecutable);

      UaMethodNode node =
          new UaMethodNode(
              context,
              planned.nodeId(),
              (QualifiedName) attributes.getOrNull(AttributeId.BrowseName),
              (LocalizedText) attributes.getOrNull(AttributeId.DisplayName),
              (LocalizedText) attributes.getOrNull(AttributeId.Description),
              (UInteger) attributes.getOrNull(AttributeId.WriteMask),
              (UInteger) attributes.getOrNull(AttributeId.UserWriteMask),
              executable != null ? executable : Boolean.TRUE,
              userExecutable != null ? userExecutable : Boolean.TRUE);

      overlay(node, attributes, METHOD_CONSTRUCTED);

      return node;
    }

    /**
     * Apply every planned attribute the constructor did not consume, preserving explicit nulls.
     * Direct field writes ({@link UaNode#setAttribute}), bypassing any filters a constructor may
     * have installed — this is construction, not runtime mutation.
     */
    private static void overlay(
        UaNode node, AttributeSnapshot attributes, Set<AttributeId> consumed) {
      for (AttributeId attributeId : attributes.attributeIds()) {
        if (consumed.contains(attributeId)) {
          continue;
        }
        node.setAttribute(
            attributeId, attributes.isNull(attributeId) ? null : attributes.getOrNull(attributeId));
      }
    }

    private static DataValue freshValue(@Nullable DataValue declared) {
      if (declared == null || declared.getValue().getValue() == null) {
        return new DataValue(
            Variant.NULL_VALUE, new StatusCode(StatusCodes.Bad_NoValue), DateTime.now());
      }

      return new DataValue(declared.getValue(), declared.getStatusCode(), DateTime.now());
    }

    // endregion

    // region Stage 4: hooks

    private void runHooks() throws InstantiationException {
      if (request.onNodeHooks().isEmpty()) {
        return;
      }

      StagedGraph graph = new StagedGraphView(realizedInOrder());

      for (PlannedNode planned : plan.plannedNodes()) {
        UaNode node = staged.get(planned.browsePath());
        if (node == null) {
          // Hooks customize what this apply constructs; adopted and shared nodes are not modified.
          continue;
        }

        UaNode parent = planned.isRoot() ? null : realizedAt(planned.browsePath().parent());

        for (InstantiationRequest.OnNode hook : request.onNodeHooks()) {
          try {
            hook.accept(planned.declaration(), node, parent, graph);
          } catch (Exception e) {
            throw failure(
                StatusCodes.Bad_InternalError,
                InstantiationDiagnostic.applyError(
                    InstantiationDiagnostic.Code.CUSTOMIZATION_FAILED,
                    "onNode hook failed at " + planned.browsePath() + ": " + e,
                    planned.browsePath(),
                    planned.nodeId()),
                e);
          }
        }
      }
    }

    /**
     * Binders run only after a successful commit: they may target reused or shared nodes the
     * instantiation does not own, so binding earlier would leave mutations on published nodes if
     * the commit failed, and a stale-generation retry can re-adopt a different instance — binding
     * pre-commit would customize an instance the committed graph no longer contains.
     */
    private void bindMethods() throws InstantiationException {
      for (BrowsePath path : request.methodBinders().keySet().stream().sorted().toList()) {
        UaNode node = realizedAt(path);
        if (node instanceof UaMethodNode method) {
          try {
            request.methodBinders().get(path).accept(method);
          } catch (Exception e) {
            throw failure(
                StatusCodes.Bad_InternalError,
                InstantiationDiagnostic.applyError(
                    InstantiationDiagnostic.Code.CUSTOMIZATION_FAILED,
                    "bindMethod binder failed at " + path + ": " + e,
                    path,
                    node.getNodeId()),
                e);
          }
        }
        // A path matching no realized Method was already warned about at plan time.
      }
    }

    private @Nullable UaNode realizedAt(BrowsePath path) {
      UaNode node = staged.get(path);
      return node != null ? node : adopted.get(path);
    }

    private Map<BrowsePath, UaNode> realizedInOrder() {
      Map<BrowsePath, UaNode> ordered = new LinkedHashMap<>();
      for (PlannedNode planned : plan.plannedNodes()) {
        UaNode node = realizedAt(planned.browsePath());
        if (node != null) {
          ordered.put(planned.browsePath(), node);
        }
      }
      return ordered;
    }

    // endregion

    // region Stages 5 and 6: commit, rollback, result

    private InstantiationResult<T> commitAndBuildResult(long generation) throws UaException {
      for (int attempt = 1; ; attempt++) {
        NodeManagerBatch<UaNode> batch = buildBatch(generation);

        NodeManager.CommitResult journal;
        try {
          journal = target.commit(batch);
        } catch (NodeManagerBatchException e) {
          List<InstantiationDiagnostic> rollbackFindings = rollBack(e.getApplied());

          // The typed signal, not the status code: implementations may throw Bad_InvalidState for
          // unrelated failures, which must not be retried as if the target had merely advanced.
          boolean staleGeneration = e.isStaleGeneration() && rollbackFindings.isEmpty();

          if (staleGeneration && attempt < MAX_COMMIT_ATTEMPTS) {
            // The target advanced between recheck and commit (nothing was applied); revalidate the
            // plan's target assumptions against the new state and try again.
            generation = recheckTarget();
            continue;
          }

          List<InstantiationDiagnostic> diagnostics = new ArrayList<>();
          diagnostics.add(
              InstantiationDiagnostic.applyError(
                  InstantiationDiagnostic.Code.COMMIT_FAILED,
                  "batch commit into the target NodeManager failed: " + e.getMessage(),
                  null,
                  null));
          diagnostics.addAll(rollbackFindings);

          throw new InstantiationException(
              e.getStatusCode().getValue(), InstantiationDiagnostic.Phase.APPLY, diagnostics, e);
        } catch (RuntimeException e) {
          // An unchecked failure crossing the commit seam. AbstractNodeManager's atomic commit
          // wraps these itself (rolled back, empty journal); a third-party emulation may not, and
          // without a journal nothing can be removed deterministically — on a best-effort target
          // the residue is unknown.
          throw failure(
              StatusCodes.Bad_InternalError,
              InstantiationDiagnostic.applyError(
                  InstantiationDiagnostic.Code.COMMIT_FAILED,
                  "batch commit into the target NodeManager failed unchecked ("
                      + e
                      + "); no journal is available, residue on a best-effort target is unknown",
                  null,
                  null),
              e);
        }

        try {
          bindMethods();
        } catch (InstantiationException bindFailure) {
          // The commit succeeded but a binder failed; remove the journaled additions so the
          // no-residue contract holds for everything this apply created. A mutation an earlier
          // binder made to a reused or shared node cannot be undone and remains.
          List<InstantiationDiagnostic> rollbackFindings = rollBack(journal);
          if (rollbackFindings.isEmpty()) {
            throw bindFailure;
          }

          List<InstantiationDiagnostic> diagnostics = new ArrayList<>(bindFailure.getDiagnostics());
          diagnostics.addAll(rollbackFindings);

          throw new InstantiationException(
              bindFailure.getStatusCode().getValue(),
              InstantiationDiagnostic.Phase.APPLY,
              diagnostics,
              bindFailure);
        }

        return buildResult(batch, journal);
      }
    }

    private NodeManagerBatch<UaNode> buildBatch(long generation) {
      NodeManagerBatch.Builder<UaNode> batch = NodeManagerBatch.builder();

      batch.expectGeneration(generation);

      for (UaNode node : staged.values()) {
        batch.addNode(node);
      }

      for (PlannedNode planned : plan.plannedNodes()) {
        if (planned.materialization() == PlannedNode.Materialization.REUSE) {
          batch.reuseNode(planned.nodeId());
        }
      }

      NamespaceTable namespaceTable = server.getNamespaceTable();
      for (Reference reference : plan.references()) {
        batch.addReference(reference);
        reference.invert(namespaceTable).ifPresent(batch::addReference);
      }

      return batch.build();
    }

    /**
     * Remove exactly the additions in {@code applied} — journal removal only, never a recursive
     * delete from the root, which would follow HasChild into reused or shared nodes. Empty on the
     * atomic path (a failed atomic commit applied nothing); the partial journal of a failed
     * best-effort commit otherwise.
     *
     * @return findings for anything that could not be removed; empty on full success.
     */
    private List<InstantiationDiagnostic> rollBack(NodeManager.CommitResult applied) {
      List<InstantiationDiagnostic> findings = new ArrayList<>();

      for (Reference reference : applied.addedReferences()) {
        try {
          target.removeReference(reference);
        } catch (Exception e) {
          findings.add(
              InstantiationDiagnostic.applyError(
                  InstantiationDiagnostic.Code.ROLLBACK_FAILED,
                  "rollback could not remove reference " + reference + ": " + e,
                  null,
                  reference.getSourceNodeId()));
        }
      }

      for (NodeId nodeId : applied.addedNodes()) {
        try {
          target.removeNode(nodeId);
        } catch (Exception e) {
          findings.add(
              InstantiationDiagnostic.applyError(
                  InstantiationDiagnostic.Code.ROLLBACK_FAILED,
                  "rollback could not remove node " + nodeId + ": " + e,
                  null,
                  nodeId));
        }
      }

      return findings;
    }

    private InstantiationResult<T> buildResult(
        NodeManagerBatch<UaNode> batch, NodeManager.CommitResult journal) {

      List<MaterializedNode> materializedNodes = new ArrayList<>();
      for (PlannedNode planned : plan.plannedNodes()) {
        UaNode node = Objects.requireNonNull(realizedAt(planned.browsePath()));

        MaterializedNode.Provenance provenance =
            switch (planned.materialization()) {
              case CREATE -> MaterializedNode.Provenance.CREATED;
              case REUSE -> MaterializedNode.Provenance.REUSED;
              case SHARE -> MaterializedNode.Provenance.SHARED;
            };

        materializedNodes.add(new MaterializedNode(planned.browsePath(), node, provenance));
      }

      Set<Reference> added = new HashSet<>(journal.addedReferences());
      List<MaterializedReference> references =
          batch.getReferenceAdditions().stream()
              .map(r -> new MaterializedReference(r, added.contains(r)))
              .toList();

      T root = request.rootClass().cast(Objects.requireNonNull(realizedAt(BrowsePath.root())));

      return new InstantiationResult<>(
          root,
          materializedNodes,
          plan.skippedDeclarations(),
          references,
          plan.diagnostics(),
          plan.modelRevision(),
          target.getStorageGuarantee(),
          target);
    }

    private void notifyObservers(InstantiationResult<T> result) {
      for (Consumer<InstantiationResult<T>> observer : request.afterCommitObservers()) {
        try {
          observer.accept(result);
        } catch (Exception e) {
          LOGGER.warn(
              "after-commit observer failed for instantiation of {}: {}",
              plan.model().typeDefinitionId(),
              e,
              e);
        }
      }
    }

    // endregion

    private static InstantiationException failure(
        long statusCode, InstantiationDiagnostic diagnostic, @Nullable Throwable cause) {
      return new InstantiationException(
          statusCode, InstantiationDiagnostic.Phase.APPLY, List.of(diagnostic), cause);
    }
  }

  /** The read-only staged-graph view handed to {@code onNode} hooks. */
  private static final class StagedGraphView implements StagedGraph {

    private final Map<BrowsePath, UaNode> nodes;
    private final List<UaNode> nodeList;

    private StagedGraphView(Map<BrowsePath, UaNode> nodes) {
      this.nodes = nodes;
      // The backing map is complete before the view is constructed, so one immutable copy serves
      // every nodes() call.
      this.nodeList = List.copyOf(nodes.values());
    }

    @Override
    public UaNode root() {
      return Objects.requireNonNull(nodes.get(BrowsePath.root()));
    }

    @Override
    public Optional<UaNode> node(BrowsePath path) {
      return Optional.ofNullable(nodes.get(path));
    }

    @Override
    public List<UaNode> nodes() {
      return nodeList;
    }
  }
}
