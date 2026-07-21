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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.jspecify.annotations.Nullable;

/**
 * The server-scoped instantiation facade: {@link #describe} exposes a type's compiled {@link
 * TypeInstantiationModel} (through the server's {@link TypeModelCache}), and {@link #plan} joins a
 * model with an {@link InstantiationRequest} into a pure-data {@link InstantiationPlan} — no nodes
 * created, nothing mutated.
 *
 * <p>Source type information always comes from the server's address space; the destination is
 * entirely the request's business (its target {@link NodeManager}) — plan only ever <em>reads</em>
 * the target, for collision preflight.
 */
public final class NodeInstantiator {

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

    TypeInstantiationModel model = describe(request.typeDefinitionId());

    return new PlanComputation<>(this, request, model).compute();
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
    private final Map<NodeId, TypeInstantiationModel> memberModels = new HashMap<>();

    private @Nullable Set<BrowsePath> effectiveIncludedPaths;

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

      allocateNodeIds();
      preflightCollisions();

      List<Reference> references = resolveReferences();

      warnUnmatchedPaths();

      List<PlannedNode> plannedNodes =
          planned.values().stream().map(Working::toPlannedNode).toList();

      return new InstantiationPlan<>(
          request, model, plannedNodes, List.copyOf(skipped.values()), references, diagnostics);
    }

    // region Root

    private void planRoot() {
      AttributeSnapshot typeAttributes = model.rootAttributes();

      NodeClass typeNodeClass = (NodeClass) typeAttributes.getOrNull(AttributeId.NodeClass);
      NodeClass instanceNodeClass =
          typeNodeClass == NodeClass.VariableType ? NodeClass.Variable : NodeClass.Object;

      if (Boolean.TRUE.equals(typeAttributes.getOrNull(AttributeId.IsAbstract))) {
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
        case OPTIONAL_PLACEHOLDER -> skip(declaration, SkippedDeclaration.Reason.PLACEHOLDER);
        case MANDATORY_PLACEHOLDER -> {
          skip(declaration, SkippedDeclaration.Reason.PLACEHOLDER);
          // Placeholder expansion does not exist yet; the ≥1-realization obligation becomes an
          // error once a request can satisfy it.
          diagnostics.add(
              InstantiationDiagnostic.planWarning(
                  InstantiationDiagnostic.Code.MANDATORY_PLACEHOLDER_UNSATISFIED,
                  "MandatoryPlaceholder at "
                      + path
                      + " requires at least one realization; none is bound by this request",
                  path,
                  declaration.declarationNodeId()));
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

      for (BrowsePath included : effectiveIncludedPaths()) {
        // Selecting a nested path implies its ancestors — but only a path that resolves in the
        // model implies anything; a mistyped include must not materialize its ancestors (it is
        // reported by the UNMATCHED_PATH warning instead).
        if (included.startsWith(path)) {
          return true;
        }
      }

      if (isOptional && request.includeAllOptionals()) {
        return true;
      }

      return request.includePredicate().map(p -> p.test(declaration)).orElse(false);
    }

    /** The request's included paths that resolve to a declaration in the model. */
    private Set<BrowsePath> effectiveIncludedPaths() {
      if (effectiveIncludedPaths == null) {
        Set<BrowsePath> resolved = new HashSet<>();
        for (BrowsePath included : request.includedPaths()) {
          if (model.get(included).isPresent()) {
            resolved.add(included);
          }
        }
        effectiveIncludedPaths = resolved;
      }
      return effectiveIncludedPaths;
    }

    private void skip(InstanceDeclaration declaration, SkippedDeclaration.Reason reason) {
      skipped.put(declaration.browsePath(), new SkippedDeclaration(declaration, reason));
    }

    // endregion

    // region Member resolution

    private void plan(InstanceDeclaration declaration) throws UaException {
      BrowsePath path = declaration.browsePath();

      if (declaration.nodeClass() == NodeClass.Method) {
        planMethod(declaration);
        return;
      }

      NodeId declaredTypeId =
          Objects.requireNonNull(declaration.typeDefinitionId(), "typeDefinitionId");
      NodeId effectiveTypeId = declaredTypeId;

      NodeId concreteTypeId = request.concreteTypes().get(path);
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
        validateMemberType(declaration, memberModel, effectiveTypeId);
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
        NodeId effectiveTypeId) {

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

      if (isConcreteOverride) {
        // The plan realizes the *declared* type's members; a concrete subtype's additional
        // members are not expanded in this release. Say so instead of silently omitting them.
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

    /**
     * Validate reuse compatibility: namespace-qualified BrowseName, NodeClass, TypeDefinition (the
     * planned type or a subtype), and — for the root — the expected Java class the typed result
     * promises.
     *
     * @return {@code null} if compatible, otherwise a description of the incompatibility.
     */
    private @Nullable String reuseIncompatibility(@Nullable UaNode existing, Working working) {
      if (existing == null) {
        return "the node disappeared between preflight checks";
      }

      QualifiedName plannedBrowseName =
          (QualifiedName) working.effectiveAttributes.getOrNull(AttributeId.BrowseName);
      if (!Objects.equals(existing.getBrowseName(), plannedBrowseName)) {
        return "BrowseName "
            + existing.getBrowseName()
            + " does not match planned "
            + plannedBrowseName;
      }

      if (existing.getNodeClass() != working.nodeClass) {
        return "NodeClass " + existing.getNodeClass() + " does not match " + working.nodeClass;
      }

      if (working.isRoot() && !request.rootClass().isInstance(existing)) {
        return "existing node is not a " + request.rootClass().getName();
      }

      if (working.typeDefinitionId != null) {
        NodeId existingTypeId =
            request.target().getReferences(working.nodeId).stream()
                .filter(Reference.HAS_TYPE_DEFINITION_PREDICATE)
                .map(r -> r.getTargetNodeId().toNodeId(server.getNamespaceTable()).orElse(null))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        if (existingTypeId == null
            || !isTypeSubtypeOfOrEqual(existingTypeId, working.typeDefinitionId)) {
          return "TypeDefinition "
              + existingTypeId
              + " is not "
              + working.typeDefinitionId
              + " or a subtype";
        }
      }

      return null;
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

      if (request.parentNodeId().isPresent() && request.parentReferenceTypeId().isPresent()) {
        NodeId parentNodeId = request.parentNodeId().get();
        NodeId parentReferenceTypeId = request.parentReferenceTypeId().get();

        if (!isReferenceSubtypeOfOrEqual(parentReferenceTypeId, NodeIds.HierarchicalReferences)) {
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
          // fallback; a missing parent is advisory only — commit is where a dangling attachment
          // actually fails.
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
        Working parent = planned.get(edge.parentPath());
        Working child = planned.get(edge.childPath());
        if (parent == null || child == null) {
          continue;
        }
        references.add(
            new Reference(
                Objects.requireNonNull(parent.nodeId),
                edge.referenceTypeId(),
                Objects.requireNonNull(child.nodeId).expanded(),
                true));
      }

      ReferenceReplicationPolicy policy = request.referenceReplication();

      for (ReferenceRow row : model.references()) {
        Working source = planned.get(row.sourcePath());
        if (source == null || source.materialization != PlannedNode.Materialization.CREATE) {
          // Skipped sources have no instance; reused and shared nodes already carry their own
          // non-hierarchy references.
          continue;
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
          continue;
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
          continue;
        }

        if (row.isRelative()) {
          if (policy.internal() == ReferenceReplicationPolicy.InternalReferences.OMIT) {
            continue;
          }
          Working target = planned.get(row.targetPath());
          if (target == null) {
            // No planned edge to an omitted target.
            continue;
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

      return references;
    }

    // endregion

    // region Unmatched request paths

    private void warnUnmatchedPaths() {
      for (BrowsePath path : sorted(request.includedPaths())) {
        if (model.get(path).isEmpty()) {
          warnUnmatched("includeOptional", path);
        } else if (model.get(path).map(InstanceDeclaration::isPlaceholder).orElse(false)) {
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
        if (model.get(path).isEmpty()) {
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
      return TypeSpaceRules.isTypeSubtypeOfOrEqual(
          typeId,
          supertypeId,
          id -> server.getAddressSpaceManager().getManagedReferences(id),
          server.getNamespaceTable());
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
}
