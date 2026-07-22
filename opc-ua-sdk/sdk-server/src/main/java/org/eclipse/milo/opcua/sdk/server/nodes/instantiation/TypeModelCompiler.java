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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.jspecify.annotations.Nullable;

/**
 * Compiles the {@link TypeInstantiationModel} of a TypeDefinition from live address-space state.
 *
 * <p>Given a type NodeId, the compiler produces either a validated, immutable, fully-inherited
 * model or a {@link ModelCompilationException} carrying structured diagnostics — never a silently
 * truncated hierarchy. It implements, per OPC UA Part 3:
 *
 * <ul>
 *   <li>the §6.3.3.2 fully-inherited table merge, folding the supertype chain in with the subtype's
 *       entry winning at an already-present BrowsePath (§6.3.3.3);
 *   <li>recursion through <em>model compilation</em> for member TypeDefinitions, so nested types
 *       are fully inherited at every depth, with explicit on-declaration children overriding
 *       same-path member-type defaults;
 *   <li>classification (not filtering) of every ModellingRule, placeholders recorded shallowly per
 *       §6.4.4.4.4–.5 and vendor rules preserved as {@link ModellingRule#OTHER};
 *   <li>browse-path extension along forward hierarchical references only, with explicit rejection
 *       of subtype cycles and declaration loops;
 *   <li>non-hierarchy reference rows with direction, relative targets whenever both ends are in the
 *       model, and validation of what the legacy walk silently tolerated (missing targets,
 *       duplicate sibling BrowseNames, NodeClass/type incompatibility, Variable narrowing, Method
 *       signatures, modelling-rule tightening, abstract member types, {@code HasInterface}
 *       compatibility).
 * </ul>
 *
 * <p>Address-space state is read exclusively through {@link
 * AddressSpaceManager#getManagedNode(NodeId)} and {@link
 * AddressSpaceManager#getManagedReferences(NodeId)}. Every node read this way is recorded in the
 * model's {@link TypeInstantiationModel#dependencies()}.
 *
 * <p>The compiler is uncached: every {@link #compile(NodeId)} call is a fresh read of the address
 * space (types compiled more than once within a single call — shared supertypes, repeated member
 * types — are memoized for that call only). Model output is deterministic: declarations, edges, and
 * rows are browse-path ordered, and an unchanged type recompiles to an equal {@link
 * TypeInstantiationModel#modelRevision()}.
 *
 * <p>Graph depth (supertype/member-type recursion and declaration hierarchy alike) is bounded by
 * {@link #MAX_DEPTH}: a deeper graph rejects with a structured {@code DEPTH_LIMIT_EXCEEDED}
 * diagnostic rather than a {@link StackOverflowError}.
 */
public class TypeModelCompiler {

  /** The maximum type-recursion and declaration-hierarchy depth compiled before rejecting. */
  static final int MAX_DEPTH = 512;

  private static final QualifiedName DEFAULT_INSTANCE_BROWSE_NAME =
      new QualifiedName(0, "DefaultInstanceBrowseName");

  private static final QualifiedName INPUT_ARGUMENTS = new QualifiedName(0, "InputArguments");
  private static final QualifiedName OUTPUT_ARGUMENTS = new QualifiedName(0, "OutputArguments");

  private static final Comparator<NodeId> NODE_ID_ORDER = TypeSpaceRules.NODE_ID_ORDER;

  private final AddressSpaceManager addressSpaceManager;
  private final NamespaceTable namespaceTable;
  private final ReferenceTypeTree referenceTypeTree;

  /**
   * Create a compiler reading through {@code server}'s {@link AddressSpaceManager}, using its
   * namespace table and ReferenceType tree.
   *
   * @param server the server whose address space is compiled from.
   */
  public TypeModelCompiler(OpcUaServer server) {
    this(
        server.getAddressSpaceManager(), server.getNamespaceTable(), server.getReferenceTypeTree());
  }

  /**
   * Create a compiler from its collaborators.
   *
   * @param addressSpaceManager the read surface for nodes and references.
   * @param namespaceTable the table used to resolve {@link ExpandedNodeId} targets.
   * @param referenceTypeTree the ReferenceType hierarchy, used to classify hierarchical references.
   */
  public TypeModelCompiler(
      AddressSpaceManager addressSpaceManager,
      NamespaceTable namespaceTable,
      ReferenceTypeTree referenceTypeTree) {

    this.addressSpaceManager = addressSpaceManager;
    this.namespaceTable = namespaceTable;
    this.referenceTypeTree = referenceTypeTree;
  }

  /**
   * Compile the instantiation model of the type identified by {@code typeDefinitionId}.
   *
   * @param typeDefinitionId the NodeId of an ObjectType or VariableType node.
   * @return the validated model; its diagnostics contain warnings and infos only.
   * @throws ModelCompilationException if the type graph admits no correct instantiation; the
   *     exception carries the full diagnostic list and no partial model is produced.
   */
  public TypeInstantiationModel compile(NodeId typeDefinitionId) throws ModelCompilationException {
    CompileContext ctx = new CompileContext();

    CompiledType compiled = compileType(typeDefinitionId, ctx);

    if (compiled == null || ctx.hasErrors) {
      throw new ModelCompilationException(typeDefinitionId, dedupe(ctx.diagnostics));
    }

    List<InstanceDeclaration> declarations =
        compiled.declarations.values().stream()
            .sorted(Comparator.comparing(InstanceDeclaration::browsePath))
            .toList();

    List<DeclarationEdge> hierarchy =
        compiled.edges.stream()
            .sorted(
                Comparator.comparing(DeclarationEdge::parentPath)
                    .thenComparing(DeclarationEdge::childPath)
                    .thenComparing(e -> e.referenceTypeId().toParseableString()))
            .toList();

    List<ReferenceRow> references =
        compiled.rows.stream()
            .map(Row::row)
            .sorted(
                Comparator.comparing(ReferenceRow::sourcePath)
                    .thenComparing(r -> r.referenceTypeId().toParseableString())
                    .thenComparing(r -> r.direction().ordinal())
                    .thenComparing(ReferenceRow::targetKey))
            .distinct()
            .toList();

    List<ModelDiagnostic> diagnostics = dedupe(ctx.diagnostics);

    long revision =
        fingerprint(
            typeDefinitionId,
            compiled.rootAttributes,
            compiled.defaultInstanceBrowseName,
            declarations,
            hierarchy,
            references,
            diagnostics);

    return new CompiledModel(
        typeDefinitionId,
        revision,
        Set.copyOf(ctx.dependencies),
        declarations,
        hierarchy,
        references,
        diagnostics,
        compiled.rootAttributes,
        compiled.defaultInstanceBrowseName);
  }

  // region Type compilation

  private @Nullable CompiledType compileType(NodeId typeId, CompileContext ctx) {
    CompiledType memoized = ctx.memo.get(typeId);
    if (memoized != null) {
      return memoized;
    }

    if (ctx.inProgress.size() >= MAX_DEPTH) {
      ctx.add(
          ModelDiagnostic.error(
              ModelDiagnostic.Code.DEPTH_LIMIT_EXCEEDED,
              "type recursion depth exceeds " + MAX_DEPTH + " at " + typeId,
              null,
              typeId));
      return null;
    }

    if (!ctx.inProgress.add(typeId)) {
      ctx.add(
          ModelDiagnostic.error(
              ModelDiagnostic.Code.TYPE_CYCLE,
              "type " + typeId + " re-entered while compiling (subtype or member-type cycle)",
              null,
              typeId));
      return null;
    }

    try {
      UaNode typeNode = getNode(typeId, ctx);
      if (typeNode == null) {
        ctx.add(
            ModelDiagnostic.error(
                ModelDiagnostic.Code.TYPE_NOT_FOUND,
                "type " + typeId + " not found",
                null,
                typeId));
        return null;
      }
      if (!(typeNode instanceof UaObjectTypeNode) && !(typeNode instanceof UaVariableTypeNode)) {
        ctx.add(
            ModelDiagnostic.error(
                ModelDiagnostic.Code.TYPE_NODE_CLASS_INVALID,
                "node "
                    + typeId
                    + " is a "
                    + typeNode.getNodeClass()
                    + ", not an ObjectType or VariableType",
                null,
                typeId));
        return null;
      }

      List<Reference> typeRefs = getReferences(typeId, ctx);

      // Malformed supertype ancestry rejects rather than compiling a silently truncated model:
      // an unresolvable (remote) supertype must not demote the type to a root, and multiple
      // supertypes have no defined merge order (single inheritance, Part 3 §6.3.1).
      List<ExpandedNodeId> supertypeTargets =
          typeRefs.stream()
              .filter(Reference.SUBTYPE_OF)
              .map(Reference::getTargetNodeId)
              .distinct()
              .toList();

      List<ExpandedNodeId> unresolvable =
          supertypeTargets.stream().filter(x -> x.toNodeId(namespaceTable).isEmpty()).toList();
      if (!unresolvable.isEmpty()) {
        ctx.add(
            ModelDiagnostic.error(
                ModelDiagnostic.Code.TYPE_NOT_FOUND,
                "supertype "
                    + unresolvable.get(0)
                    + " of type "
                    + typeId
                    + " is not resolvable in this server",
                null,
                typeId));
        return null;
      }

      List<NodeId> supertypeIds =
          supertypeTargets.stream()
              .map(x -> x.toNodeId(namespaceTable).orElseThrow())
              .distinct()
              .sorted(NODE_ID_ORDER)
              .toList();
      if (supertypeIds.size() > 1) {
        ctx.add(
            ModelDiagnostic.error(
                ModelDiagnostic.Code.MULTIPLE_SUPERTYPES,
                "type "
                    + typeId
                    + " has "
                    + supertypeIds.size()
                    + " supertypes "
                    + supertypeIds
                    + "; single inheritance is required",
                null,
                typeId));
        return null;
      }

      NodeId supertypeId = supertypeIds.isEmpty() ? null : supertypeIds.get(0);
      CompiledType supertype = supertypeId != null ? compileType(supertypeId, ctx) : null;

      if (supertype != null) {
        NodeClass supertypeClass =
            (NodeClass) supertype.rootAttributes.getOrNull(AttributeId.NodeClass);
        if (typeNode.getNodeClass() != supertypeClass) {
          ctx.add(
              ModelDiagnostic.error(
                  ModelDiagnostic.Code.NODE_CLASS_MISMATCH,
                  "type "
                      + typeId
                      + " is a "
                      + typeNode.getNodeClass()
                      + " but its supertype "
                      + supertypeId
                      + " is a "
                      + supertypeClass,
                  null,
                  typeId));
          return null;
        }
      }

      CompiledType result =
          new CompiledType(
              typeId,
              AttributeSnapshot.of(typeNode),
              findDefaultInstanceBrowseName(typeRefs, ctx, supertype));

      Set<NodeId> pathNodeIds = new HashSet<>();
      pathNodeIds.add(typeId);
      walkChildren(typeId, BrowsePath.root(), typeId, result, ctx, pathNodeIds);

      if (supertype != null) {
        foldInto(result, supertype, BrowsePath.root(), ctx);
      }

      resolveRows(result, ctx);

      validateInterfaces(typeId, typeRefs, result, ctx);

      ctx.memo.put(typeId, result);
      return result;
    } finally {
      ctx.inProgress.remove(typeId);
    }
  }

  private void walkChildren(
      NodeId sourceNodeId,
      BrowsePath parentPath,
      NodeId declaringTypeId,
      CompiledType table,
      CompileContext ctx,
      Set<NodeId> pathNodeIds) {

    Deque<ChildWalkFrame> frames = new ArrayDeque<>();
    frames.push(childWalkFrame(sourceNodeId, parentPath, declaringTypeId, null, ctx));

    while (!frames.isEmpty()) {
      ChildWalkFrame frame = frames.peek();
      if (frame.hasNext()) {
        Child child = frame.next();
        ChildExpansion expansion =
            processChild(
                child.reference,
                child.targetId,
                child.node,
                frame.parentPath,
                frame.declaringTypeId,
                table,
                ctx,
                pathNodeIds);

        if (expansion != null) {
          frames.push(
              childWalkFrame(
                  expansion.targetId, expansion.path, expansion.declaringTypeId, expansion, ctx));
        }
      } else {
        frames.pop();

        ChildExpansion expansion = frame.expansion;
        if (expansion != null) {
          pathNodeIds.remove(expansion.targetId);

          if (expansion.memberTypeId != null) {
            expandMemberType(
                expansion.declaration,
                expansion.memberTypeId,
                expansion.path,
                expansion.targetId,
                table,
                ctx);
          }
        }
      }
    }
  }

  private ChildWalkFrame childWalkFrame(
      NodeId sourceNodeId,
      BrowsePath parentPath,
      NodeId declaringTypeId,
      @Nullable ChildExpansion expansion,
      CompileContext ctx) {

    List<Reference> refs = getReferences(sourceNodeId, ctx);

    List<Child> children = new ArrayList<>();

    for (Reference r : refs) {
      if (!r.isForward()) {
        continue;
      }
      NodeId refTypeId = r.getReferenceTypeId();
      // HasSubtype targets are types, never InstanceDeclarations; skipping before the fetch
      // keeps every subtype of a base type out of the walk and the dependency set.
      if (NodeIds.HasSubtype.equals(refTypeId) || !isHierarchical(refTypeId)) {
        continue;
      }

      NodeId targetId = r.getTargetNodeId().toNodeId(namespaceTable).orElse(null);
      if (targetId == null) {
        ctx.add(
            ModelDiagnostic.error(
                ModelDiagnostic.Code.REFERENCE_TARGET_MISSING,
                "hierarchical reference target "
                    + r.getTargetNodeId()
                    + " from "
                    + sourceNodeId
                    + " is not resolvable in this server",
                parentPath,
                sourceNodeId));
        continue;
      }

      UaNode target = getNode(targetId, ctx);
      if (target == null) {
        ctx.add(
            ModelDiagnostic.error(
                ModelDiagnostic.Code.REFERENCE_TARGET_MISSING,
                "hierarchical reference target "
                    + targetId
                    + " from "
                    + sourceNodeId
                    + " not found",
                parentPath,
                sourceNodeId));
        continue;
      }

      NodeClass nodeClass = target.getNodeClass();
      if (nodeClass != NodeClass.Object
          && nodeClass != NodeClass.Variable
          && nodeClass != NodeClass.Method) {
        // Structurally never an InstanceDeclaration (Part 3 §6.2.1); not an exclusion worth noting.
        continue;
      }

      children.add(new Child(r, targetId, target));
    }

    children.sort(
        Comparator.comparing((Child c) -> c.node.getBrowseName().namespaceIndex().intValue())
            .thenComparing(c -> Objects.requireNonNullElse(c.node.getBrowseName().name(), ""))
            .thenComparing(c -> c.reference.getReferenceTypeId().toParseableString())
            .thenComparing(c -> c.targetId.toParseableString()));

    return new ChildWalkFrame(parentPath, declaringTypeId, children, expansion);
  }

  private @Nullable ChildExpansion processChild(
      Reference reference,
      NodeId targetId,
      UaNode node,
      BrowsePath parentPath,
      NodeId declaringTypeId,
      CompiledType table,
      CompileContext ctx,
      Set<NodeId> pathNodeIds) {

    QualifiedName browseName = node.getBrowseName();
    NodeClass nodeClass = node.getNodeClass();
    BrowsePath path = parentPath.append(browseName);

    // Sibling BrowseName uniqueness (Part 3 §4.6.4 / §6.2.6) covers every walked child, rule-less
    // ones included, so validity is not identifier-order dependent.
    NodeId occupant = table.pathOccupants.get(path);
    if (occupant != null) {
      if (occupant.equals(targetId)) {
        if (table.declarations.containsKey(path)) {
          // The same declaration pair connected by another reference (Part 3 §6.4.3): one more
          // logical edge onto the same occurrence, nothing else changes.
          table.edges.add(new DeclarationEdge(parentPath, reference.getReferenceTypeId(), path));
        }
      } else {
        ctx.add(
            ModelDiagnostic.error(
                ModelDiagnostic.Code.DUPLICATE_BROWSE_NAME,
                "two sibling declarations under "
                    + parentPath
                    + " share BrowseName "
                    + browseName
                    + " ("
                    + occupant
                    + " and "
                    + targetId
                    + ")",
                path,
                targetId));
      }
      return null;
    }
    table.pathOccupants.put(path, targetId);

    List<Reference> nodeRefs = getReferences(targetId, ctx);

    List<Reference> ruleRefs =
        nodeRefs.stream().filter(Reference.HAS_MODELLING_RULE_PREDICATE).toList();

    List<NodeId> ruleIds =
        ruleRefs.stream()
            .map(r -> r.getTargetNodeId().toNodeId(namespaceTable).orElse(null))
            .filter(Objects::nonNull)
            .sorted(NODE_ID_ORDER)
            .toList();

    if (ruleRefs.isEmpty()) {
      // Belongs to the type node only (Part 3 §6.2.2); excluded, traceably. The type's own
      // DefaultInstanceBrowseName property is consumed by the model instead of noted.
      if (!DEFAULT_INSTANCE_BROWSE_NAME.equals(browseName)) {
        ctx.add(
            ModelDiagnostic.info(
                ModelDiagnostic.Code.NOT_AN_INSTANCE_DECLARATION,
                "node "
                    + targetId
                    + " at "
                    + path
                    + " has no ModellingRule and is not instantiated",
                path,
                targetId));
      }
      return null;
    }

    if (ruleIds.isEmpty()) {
      // A rule reference exists but no target resolves: the declaration cannot be classified,
      // and excluding it would silently truncate the hierarchy.
      ctx.add(
          ModelDiagnostic.error(
              ModelDiagnostic.Code.MODELLING_RULE_INVALID,
              "declaration "
                  + targetId
                  + " at "
                  + path
                  + " has no resolvable HasModellingRule target ("
                  + ruleRefs.get(0).getTargetNodeId()
                  + ")",
              path,
              targetId));
      return null;
    }

    if (ruleRefs.size() > 1) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.MULTIPLE_MODELLING_RULES,
              "declaration "
                  + targetId
                  + " at "
                  + path
                  + " has "
                  + ruleRefs.size()
                  + " HasModellingRule references; using "
                  + ruleIds.get(0),
              path,
              targetId));
    }

    NodeId modellingRuleId = ruleIds.get(0);
    ModellingRule rule = ModellingRule.of(modellingRuleId);

    if (rule == ModellingRule.OTHER) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.VENDOR_MODELLING_RULE,
              "declaration at " + path + " uses non-standard ModellingRule " + modellingRuleId,
              path,
              targetId));
    }

    validateModellingRuleTarget(modellingRuleId, path, targetId, ctx);

    NodeId memberTypeId = null;
    if (nodeClass != NodeClass.Method) {
      List<ExpandedNodeId> memberTypeXnis =
          nodeRefs.stream()
              .filter(Reference.HAS_TYPE_DEFINITION_PREDICATE)
              .map(Reference::getTargetNodeId)
              .distinct()
              .toList();

      if (memberTypeXnis.isEmpty()) {
        ctx.add(
            ModelDiagnostic.error(
                ModelDiagnostic.Code.TYPE_DEFINITION_MISSING,
                nodeClass + " declaration at " + path + " has no HasTypeDefinition reference",
                path,
                targetId));
        return null;
      }

      if (memberTypeXnis.size() > 1) {
        // Objects and Variables have exactly one TypeDefinition; picking one silently would let
        // expansion and validation use a type other copies of the reference contradict.
        ctx.add(
            ModelDiagnostic.error(
                ModelDiagnostic.Code.MULTIPLE_TYPE_DEFINITIONS,
                "declaration at "
                    + path
                    + " has "
                    + memberTypeXnis.size()
                    + " HasTypeDefinition references "
                    + memberTypeXnis
                    + "; exactly one is required",
                path,
                targetId));
        return null;
      }

      ExpandedNodeId memberTypeXni = memberTypeXnis.get(0);
      memberTypeId = memberTypeXni.toNodeId(namespaceTable).orElse(null);
      if (memberTypeId == null) {
        ctx.add(
            ModelDiagnostic.error(
                ModelDiagnostic.Code.TYPE_NOT_FOUND,
                "member type "
                    + memberTypeXni
                    + " of declaration at "
                    + path
                    + " is not resolvable in this server",
                path,
                targetId));
        return null;
      }
    }

    InstanceDeclaration declaration =
        new InstanceDeclaration(
            path,
            browseName,
            nodeClass,
            targetId,
            declaringTypeId,
            List.of(),
            modellingRuleId,
            rule,
            memberTypeId,
            AttributeSnapshot.of(node));

    table.declarations.put(path, declaration);
    table.edges.add(new DeclarationEdge(parentPath, reference.getReferenceTypeId(), path));

    recordRows(path, nodeRefs, table);

    if (rule.isShallow()) {
      // Recorded shallowly: complex structures beneath placeholders are not further considered
      // for instantiating (Part 3 §6.4.4.4.4–.5), and ExposesItsArray is visible in the model but
      // never materialized. Shallow recording skips subtree expansion, not validation of the
      // recorded type metadata.
      if (memberTypeId != null) {
        UaNode memberTypeNode = getNode(memberTypeId, ctx);
        if (memberTypeNode == null) {
          ctx.add(
              ModelDiagnostic.warning(
                  ModelDiagnostic.Code.PLACEHOLDER_TYPE_MISSING,
                  "type " + memberTypeId + " of placeholder at " + path + " not found",
                  path,
                  targetId));
        } else {
          boolean classCompatible =
              (nodeClass == NodeClass.Object && memberTypeNode instanceof UaObjectTypeNode)
                  || (nodeClass == NodeClass.Variable
                      && memberTypeNode instanceof UaVariableTypeNode);
          if (!classCompatible) {
            ctx.add(
                ModelDiagnostic.warning(
                    ModelDiagnostic.Code.NODE_CLASS_MISMATCH,
                    nodeClass
                        + " declaration at "
                        + path
                        + " has TypeDefinition "
                        + memberTypeId
                        + " of NodeClass "
                        + memberTypeNode.getNodeClass(),
                    path,
                    targetId));
          }
        }
      }
      return null;
    }

    if (path.depth() >= MAX_DEPTH) {
      ctx.add(
          ModelDiagnostic.error(
              ModelDiagnostic.Code.DEPTH_LIMIT_EXCEEDED,
              "declaration hierarchy exceeds depth " + MAX_DEPTH + " at " + path,
              path,
              targetId));
      return null;
    }

    if (!pathNodeIds.add(targetId)) {
      ctx.add(
          ModelDiagnostic.error(
              ModelDiagnostic.Code.DECLARATION_CYCLE,
              "declaration " + targetId + " reached again along its own path at " + path,
              path,
              targetId));
      return null;
    }

    // Explicit on-declaration children are traversed before member-type expansion, so they win over
    // same-path member-type defaults (Part 3 §6.3.3.3). The continuation also removes targetId from
    // the active path after its children have been processed.
    return new ChildExpansion(targetId, path, declaringTypeId, declaration, memberTypeId);
  }

  /**
   * Validate that a resolved HasModellingRule target actually exists and is an Object of {@code
   * ModellingRuleType} (or a subtype). The declaration stays classified either way — these are
   * warnings on a usable model.
   */
  private void validateModellingRuleTarget(
      NodeId modellingRuleId, BrowsePath path, NodeId declarationId, CompileContext ctx) {

    UaNode ruleNode = getNode(modellingRuleId, ctx);
    if (ruleNode == null) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.MODELLING_RULE_INVALID,
              "ModellingRule " + modellingRuleId + " of declaration at " + path + " not found",
              path,
              declarationId));
      return;
    }

    if (!(ruleNode instanceof UaObjectNode)) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.MODELLING_RULE_INVALID,
              "ModellingRule "
                  + modellingRuleId
                  + " of declaration at "
                  + path
                  + " is a "
                  + ruleNode.getNodeClass()
                  + ", not an Object",
              path,
              declarationId));
      return;
    }

    NodeId ruleTypeId =
        getReferences(modellingRuleId, ctx).stream()
            .filter(Reference.HAS_TYPE_DEFINITION_PREDICATE)
            .map(r -> r.getTargetNodeId().toNodeId(namespaceTable).orElse(null))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    if (ruleTypeId == null || !isTypeSubtypeOfOrEqual(ruleTypeId, NodeIds.ModellingRuleType, ctx)) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.MODELLING_RULE_INVALID,
              "ModellingRule "
                  + modellingRuleId
                  + " of declaration at "
                  + path
                  + " is not a ModellingRuleType Object",
              path,
              declarationId));
    }
  }

  private void expandMemberType(
      InstanceDeclaration declaration,
      NodeId memberTypeId,
      BrowsePath path,
      NodeId targetId,
      CompiledType table,
      CompileContext ctx) {

    CompiledType memberModel = compileType(memberTypeId, ctx);
    if (memberModel == null) {
      // The failure (TYPE_NOT_FOUND, TYPE_CYCLE, …) is already recorded; compile() will reject.
      return;
    }

    NodeClass typeNodeClass =
        (NodeClass) memberModel.rootAttributes.getOrNull(AttributeId.NodeClass);
    boolean classCompatible =
        (declaration.nodeClass() == NodeClass.Object && typeNodeClass == NodeClass.ObjectType)
            || (declaration.nodeClass() == NodeClass.Variable
                && typeNodeClass == NodeClass.VariableType);
    if (!classCompatible) {
      ctx.add(
          ModelDiagnostic.error(
              ModelDiagnostic.Code.NODE_CLASS_MISMATCH,
              declaration.nodeClass()
                  + " declaration at "
                  + path
                  + " has TypeDefinition "
                  + memberTypeId
                  + " of NodeClass "
                  + typeNodeClass,
              path,
              targetId));
      return;
    }

    if (memberModel.isAbstract()) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.ABSTRACT_MEMBER_TYPE,
              "declaration at "
                  + path
                  + " is typed by abstract type "
                  + memberTypeId
                  + "; instantiation requires an explicit concrete subtype",
              path,
              targetId));
    }

    if (declaration.nodeClass() == NodeClass.Variable) {
      validateNarrowing(declaration.attributes(), memberModel.rootAttributes, path, targetId, ctx);
    }

    // Graft the member type's fully-inherited model beneath this declaration, so nested types are
    // expanded at every depth. Explicit on-declaration entries were walked first and win per path
    // (Part 3 §6.3.3.3).
    foldInto(table, memberModel, path, ctx);
  }

  /**
   * Fold {@code source}'s fully-inherited tables into {@code target}, relocated beneath {@code
   * prefix} — the root path for a supertype merge (Part 3 §6.3.3.2), a declaration's path for a
   * member-type graft. An entry already present in {@code target} at a path wins (the subtype's own
   * entry, or an explicit on-declaration child), with its override chain appended and the
   * transition validated by {@code override} — unless the folded entry already overrode the present
   * one in its own model. That provenance identifies a supertype's explicit specialization arriving
   * at a path currently occupied by a member-type default the subtype re-grafted without
   * re-declaring the child: the specialization is the more derived declaration and wins, with the
   * override validated in the matching direction.
   *
   * <p>Inherited references at an overridden path are suppressed only where Part 3 §6.3.3.3
   * replaces them: a hierarchy edge is replaced when the winner connects the same parent/child pair
   * with the same ReferenceType or a subtype; of the non-hierarchical rows only the singular
   * HasTypeDefinition / HasModellingRule references are replaced by the winner's own. All other
   * inherited edges and rows describe distinct relationships and remain in the merged model.
   */
  private void foldInto(
      CompiledType target, CompiledType source, BrowsePath prefix, CompileContext ctx) {

    Set<BrowsePath> overriddenPaths = new HashSet<>();
    Set<BrowsePath> incomingWins = new HashSet<>();

    for (InstanceDeclaration d : source.declarations.values()) {
      BrowsePath path = prefix.concat(d.browsePath());
      InstanceDeclaration incoming = prefix.isRoot() ? d : d.withBrowsePath(path);

      InstanceDeclaration existing = target.declarations.get(path);
      if (existing == null) {
        target.declarations.put(path, incoming);
      } else if (incoming.overriddenDeclarations().contains(existing.declarationNodeId())) {
        overriddenPaths.add(path);
        incomingWins.add(path);
        override(incoming, existing, target, ctx);
      } else {
        overriddenPaths.add(path);
        override(existing, incoming, target, ctx);
      }
    }

    // Only edges present before this fold can replace an inherited edge; two edges folded by the
    // same loop describe distinct relationships and must not suppress each other.
    List<DeclarationEdge> preexistingEdges = List.copyOf(target.edges);

    for (DeclarationEdge e : source.edges) {
      BrowsePath parentPath = prefix.concat(e.parentPath());
      BrowsePath childPath = prefix.concat(e.childPath());
      if (incomingWins.contains(childPath)) {
        // The folded declaration won at this path, so its edge replaces the loser's.
        target.edges.removeIf(
            w ->
                w.parentPath().equals(parentPath)
                    && w.childPath().equals(childPath)
                    && isReferenceSubtypeOfOrEqual(e.referenceTypeId(), w.referenceTypeId()));
      } else if (overriddenPaths.contains(childPath)) {
        boolean replaced =
            preexistingEdges.stream()
                .anyMatch(
                    w ->
                        w.parentPath().equals(parentPath)
                            && w.childPath().equals(childPath)
                            && isReferenceSubtypeOfOrEqual(
                                w.referenceTypeId(), e.referenceTypeId()));
        if (replaced) {
          continue;
        }
      }
      target.edges.add(new DeclarationEdge(parentPath, e.referenceTypeId(), childPath));
    }

    for (Row r : source.rows) {
      BrowsePath rowPath = prefix.concat(r.row().sourcePath());
      if (overriddenPaths.contains(rowPath) && isSingularReference(r.row().referenceTypeId())) {
        if (incomingWins.contains(rowPath)) {
          // The folded declaration won at this path, so its singular rows replace the loser's.
          target.rows.removeIf(
              t ->
                  t.row().sourcePath().equals(rowPath)
                      && sameSingularFamily(t.row().referenceTypeId(), r.row().referenceTypeId()));
        } else {
          continue;
        }
      }
      target.rows.add(new Row(reroot(r.row(), prefix), r.frozen()));
    }
  }

  /**
   * ReferenceTypes that permit one reference per source node; the winner's replaces on override.
   */
  private boolean isSingularReference(NodeId referenceTypeId) {
    return isReferenceSubtypeOfOrEqual(referenceTypeId, NodeIds.HasTypeDefinition)
        || isReferenceSubtypeOfOrEqual(referenceTypeId, NodeIds.HasModellingRule);
  }

  /** Whether {@code a} and {@code b} are singular references of the same family. */
  private boolean sameSingularFamily(NodeId a, NodeId b) {
    return (isReferenceSubtypeOfOrEqual(a, NodeIds.HasTypeDefinition)
            && isReferenceSubtypeOfOrEqual(b, NodeIds.HasTypeDefinition))
        || (isReferenceSubtypeOfOrEqual(a, NodeIds.HasModellingRule)
            && isReferenceSubtypeOfOrEqual(b, NodeIds.HasModellingRule));
  }

  private boolean isReferenceSubtypeOfOrEqual(NodeId referenceTypeId, NodeId supertypeId) {
    return TypeSpaceRules.isReferenceSubtypeOfOrEqual(
        referenceTypeTree, referenceTypeId, supertypeId);
  }

  /**
   * Record {@code winner} as overriding {@code overridden} at the same BrowsePath, appending the
   * override chain and validating the transition per Part 3 §6.3.3.3 / §6.4.4.2.
   */
  private void override(
      InstanceDeclaration winner,
      InstanceDeclaration overridden,
      CompiledType table,
      CompileContext ctx) {

    BrowsePath path = winner.browsePath();

    // A LinkedHashSet because the winner's chain may already contain the overridden declaration
    // (a supertype specialization beating a re-grafted member-type default it overrode before).
    LinkedHashSet<NodeId> chain = new LinkedHashSet<>(winner.overriddenDeclarations());
    chain.add(overridden.declarationNodeId());
    chain.addAll(overridden.overriddenDeclarations());

    table.declarations.put(path, winner.withOverridden(List.copyOf(chain)));

    // No instance can conform to both the inherited and the overriding declaration when the
    // override changes NodeClass or replaces the TypeDefinition with an unrelated type (Part 3
    // §6.3.3.3) — these reject the compile rather than escaping as usable warnings.
    if (winner.nodeClass() != overridden.nodeClass()) {
      ctx.add(
          ModelDiagnostic.error(
              ModelDiagnostic.Code.NODE_CLASS_MISMATCH,
              "override at "
                  + path
                  + " changes NodeClass from "
                  + overridden.nodeClass()
                  + " to "
                  + winner.nodeClass(),
              path,
              winner.declarationNodeId()));
    }

    validateRuleTransition(overridden, winner, ctx);

    if (winner.typeDefinitionId() != null
        && overridden.typeDefinitionId() != null
        && !isTypeSubtypeOfOrEqual(winner.typeDefinitionId(), overridden.typeDefinitionId(), ctx)) {
      ctx.add(
          ModelDiagnostic.error(
              ModelDiagnostic.Code.TYPE_INCOMPATIBLE,
              "override at "
                  + path
                  + " has TypeDefinition "
                  + winner.typeDefinitionId()
                  + ", not "
                  + overridden.typeDefinitionId()
                  + " or a subtype",
              path,
              winner.declarationNodeId()));
    }

    if (winner.nodeClass() == NodeClass.Variable && overridden.nodeClass() == NodeClass.Variable) {
      validateNarrowing(
          winner.attributes(), overridden.attributes(), path, winner.declarationNodeId(), ctx);
    }

    if (winner.nodeClass() == NodeClass.Method && overridden.nodeClass() == NodeClass.Method) {
      validateMethodSignature(winner, overridden, path, ctx);
    }
  }

  private void validateRuleTransition(
      InstanceDeclaration overridden, InstanceDeclaration winner, CompileContext ctx) {

    boolean isMethod =
        winner.nodeClass() == NodeClass.Method && overridden.nodeClass() == NodeClass.Method;

    if (!isMethod && Objects.equals(overridden.modellingRuleId(), winner.modellingRuleId())) {
      return;
    }

    ModellingRule from = overridden.rule();
    ModellingRule to = winner.rule();

    if (from == ModellingRule.OTHER
        || to == ModellingRule.OTHER
        || from == ModellingRule.EXPOSES_ITS_ARRAY
        || to == ModellingRule.EXPOSES_ITS_ARRAY) {
      // No normative tightening table covers vendor rules or ExposesItsArray.
      return;
    }

    if (isMethod) {
      // Methods have their own transition rules: a placeholder Method only defines the BrowseName,
      // and an overriding subtype shall change OptionalPlaceholder to Optional or Mandatory and
      // MandatoryPlaceholder to Mandatory (Part 3 §6.4.4.4.4–.5).
      boolean allowed =
          switch (from) {
            case MANDATORY, MANDATORY_PLACEHOLDER -> to == ModellingRule.MANDATORY;
            case OPTIONAL, OPTIONAL_PLACEHOLDER ->
                to == ModellingRule.OPTIONAL || to == ModellingRule.MANDATORY;
            default -> true;
          };
      if (!allowed) {
        String requirement =
            switch (from) {
              case MANDATORY_PLACEHOLDER ->
                  "a MandatoryPlaceholder Method override shall change to Mandatory (§6.4.4.4.5)";
              case OPTIONAL_PLACEHOLDER ->
                  "an OptionalPlaceholder Method override shall change to Optional or Mandatory"
                      + " (§6.4.4.4.4)";
              default -> "not a valid tightening per Part 3 Table 21";
            };
        ctx.add(
            ModelDiagnostic.warning(
                ModelDiagnostic.Code.MODELLING_RULE_TIGHTENING,
                "Method override at "
                    + winner.browsePath()
                    + " keeps or changes ModellingRule from "
                    + from
                    + " to "
                    + to
                    + "; "
                    + requirement,
                winner.browsePath(),
                winner.declarationNodeId()));
      }
      return;
    }

    if (from == ModellingRule.OPTIONAL_PLACEHOLDER && to == ModellingRule.MANDATORY_PLACEHOLDER) {
      // Table 21 permits this transition but §6.4.4.4.4 contests it; warn, don't error.
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.MODELLING_RULE_TIGHTENING,
              "override at "
                  + winner.browsePath()
                  + " tightens OptionalPlaceholder to MandatoryPlaceholder (contested between"
                  + " Part 3 Table 21 and §6.4.4.4.4)",
              winner.browsePath(),
              winner.declarationNodeId()));
      return;
    }

    boolean allowed =
        switch (from) {
          case MANDATORY -> to == ModellingRule.MANDATORY;
          case OPTIONAL -> to == ModellingRule.MANDATORY || to == ModellingRule.OPTIONAL;
          case MANDATORY_PLACEHOLDER -> to == ModellingRule.MANDATORY_PLACEHOLDER;
          case OPTIONAL_PLACEHOLDER -> to == ModellingRule.OPTIONAL_PLACEHOLDER;
          default -> true;
        };

    if (!allowed) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.MODELLING_RULE_TIGHTENING,
              "override at "
                  + winner.browsePath()
                  + " changes ModellingRule from "
                  + from
                  + " to "
                  + to
                  + ", not a valid tightening per Part 3 Table 21",
              winner.browsePath(),
              winner.declarationNodeId()));
    }
  }

  private void validateNarrowing(
      AttributeSnapshot narrowed,
      AttributeSnapshot base,
      BrowsePath path,
      NodeId nodeId,
      CompileContext ctx) {

    NodeId narrowedDataType = narrowed.dataType();
    NodeId baseDataType = base.dataType();
    if (narrowedDataType != null
        && baseDataType != null
        && !isTypeSubtypeOfOrEqual(narrowedDataType, baseDataType, ctx)) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.VARIABLE_NARROWING,
              "DataType "
                  + narrowedDataType
                  + " at "
                  + path
                  + " is not "
                  + baseDataType
                  + " or a subtype (Part 3 §6.2.8)",
              path,
              nodeId));
    }

    Integer narrowedRank = narrowed.valueRank();
    Integer baseRank = base.valueRank();
    if (narrowedRank != null
        && baseRank != null
        && !isValueRankRestriction(baseRank, narrowedRank)) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.VARIABLE_NARROWING,
              "ValueRank "
                  + narrowedRank
                  + " at "
                  + path
                  + " is not a valid restriction of "
                  + baseRank
                  + " (Part 3 §6.2.8)",
              path,
              nodeId));
    }

    UInteger[] narrowedDimensions = narrowed.arrayDimensions();
    UInteger[] baseDimensions = base.arrayDimensions();
    if (narrowedDimensions != null
        && baseDimensions != null
        && !isArrayDimensionsRestriction(baseDimensions, narrowedDimensions)) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.VARIABLE_NARROWING,
              "ArrayDimensions "
                  + Arrays.toString(narrowedDimensions)
                  + " at "
                  + path
                  + " is not a valid restriction of "
                  + Arrays.toString(baseDimensions)
                  + " (Part 3 §6.2.8)",
              path,
              nodeId));
    }
  }

  private static boolean isValueRankRestriction(int base, int restricted) {
    return TypeSpaceRules.isValueRankRestriction(base, restricted);
  }

  private static boolean isArrayDimensionsRestriction(UInteger[] base, UInteger[] restricted) {
    return TypeSpaceRules.isArrayDimensionsRestriction(base, restricted);
  }

  private void validateMethodSignature(
      InstanceDeclaration winner,
      InstanceDeclaration overridden,
      BrowsePath path,
      CompileContext ctx) {

    for (QualifiedName property : List.of(INPUT_ARGUMENTS, OUTPUT_ARGUMENTS)) {
      Object winnerRaw =
          readPropertyValue(getReferences(winner.declarationNodeId(), ctx), property, ctx);
      Object overriddenRaw =
          readPropertyValue(getReferences(overridden.declarationNodeId(), ctx), property, ctx);

      if (overriddenRaw != null && winnerRaw == null) {
        ctx.add(
            ModelDiagnostic.warning(
                ModelDiagnostic.Code.METHOD_SIGNATURE_MISMATCH,
                "Method override at "
                    + path
                    + " removes the "
                    + property.name()
                    + " property (Part 3 §6.3.3.3 forbids removing arguments)",
                path,
                winner.declarationNodeId()));
        continue;
      }

      // Encoded argument lists (ExtensionObject[]) are not decodable without an encoding context;
      // signature validation is skipped for them (raw is not an Argument[]) in this release.
      if (!(winnerRaw instanceof Argument[] winnerArguments)
          || !(overriddenRaw instanceof Argument[] overriddenArguments)) {
        continue;
      }

      if (winnerArguments.length < overriddenArguments.length) {
        ctx.add(
            ModelDiagnostic.warning(
                ModelDiagnostic.Code.METHOD_SIGNATURE_MISMATCH,
                "Method override at "
                    + path
                    + " removes "
                    + property.name()
                    + " entries ("
                    + overriddenArguments.length
                    + " -> "
                    + winnerArguments.length
                    + "; Part 3 §6.3.3.3 permits appending only)",
                path,
                winner.declarationNodeId()));
        continue;
      }

      for (int i = 0; i < overriddenArguments.length; i++) {
        if (!Objects.equals(overriddenArguments[i].getName(), winnerArguments[i].getName())) {
          ctx.add(
              ModelDiagnostic.warning(
                  ModelDiagnostic.Code.METHOD_SIGNATURE_MISMATCH,
                  "Method override at "
                      + path
                      + " renames "
                      + property.name()
                      + "["
                      + i
                      + "] from "
                      + overriddenArguments[i].getName()
                      + " to "
                      + winnerArguments[i].getName(),
                  path,
                  winner.declarationNodeId()));
          continue;
        }

        validateArgumentCompatibility(
            overriddenArguments[i], winnerArguments[i], property, i, path, winner, ctx);
      }
    }
  }

  /**
   * Validate one overriding argument against the overridden one: a concrete DataType shall not
   * change, an abstract DataType may only be specialized to a subtype, and rank/dimensions may only
   * be restricted (Part 3 §6.3.3.3).
   */
  private void validateArgumentCompatibility(
      Argument base,
      Argument override,
      QualifiedName property,
      int index,
      BrowsePath path,
      InstanceDeclaration winner,
      CompileContext ctx) {

    NodeId baseDataType = base.getDataType();
    NodeId overrideDataType = override.getDataType();
    if (!Objects.equals(baseDataType, overrideDataType)) {
      boolean specialization =
          baseDataType != null
              && overrideDataType != null
              && isAbstractDataType(baseDataType, ctx)
              && isTypeSubtypeOfOrEqual(overrideDataType, baseDataType, ctx);
      if (!specialization) {
        ctx.add(
            ModelDiagnostic.warning(
                ModelDiagnostic.Code.METHOD_SIGNATURE_MISMATCH,
                "Method override at "
                    + path
                    + " changes "
                    + property.name()
                    + "["
                    + index
                    + "] DataType from "
                    + baseDataType
                    + " to "
                    + overrideDataType
                    + " (Part 3 §6.3.3.3 permits specializing abstract DataTypes only)",
                path,
                winner.declarationNodeId()));
      }
    }

    Integer baseRank = base.getValueRank();
    Integer overrideRank = override.getValueRank();
    if (baseRank != null
        && overrideRank != null
        && !isValueRankRestriction(baseRank, overrideRank)) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.METHOD_SIGNATURE_MISMATCH,
              "Method override at "
                  + path
                  + " changes "
                  + property.name()
                  + "["
                  + index
                  + "] ValueRank from "
                  + baseRank
                  + " to "
                  + overrideRank,
              path,
              winner.declarationNodeId()));
    }

    UInteger[] baseDimensions = base.getArrayDimensions();
    UInteger[] overrideDimensions = override.getArrayDimensions();
    if (baseDimensions != null
        && overrideDimensions != null
        && !isArrayDimensionsRestriction(baseDimensions, overrideDimensions)) {
      ctx.add(
          ModelDiagnostic.warning(
              ModelDiagnostic.Code.METHOD_SIGNATURE_MISMATCH,
              "Method override at "
                  + path
                  + " changes "
                  + property.name()
                  + "["
                  + index
                  + "] ArrayDimensions from "
                  + Arrays.toString(baseDimensions)
                  + " to "
                  + Arrays.toString(overrideDimensions),
              path,
              winner.declarationNodeId()));
    }
  }

  private boolean isAbstractDataType(NodeId dataTypeId, CompileContext ctx) {
    return getNode(dataTypeId, ctx) instanceof UaDataTypeNode dataTypeNode
        && Boolean.TRUE.equals(dataTypeNode.getIsAbstract());
  }

  /**
   * Read the Value of the forward {@code HasProperty} child in {@code refs} whose BrowseName is
   * {@code propertyName}, routing the node read through the dependency-tracking {@link #getNode}.
   *
   * @return the matched property's raw value, or {@code null} if no such property exists.
   */
  private @Nullable Object readPropertyValue(
      List<Reference> refs, QualifiedName propertyName, CompileContext ctx) {

    for (Reference r : refs) {
      if (!Reference.HAS_PROPERTY_PREDICATE.test(r)) {
        continue;
      }
      NodeId targetId = r.getTargetNodeId().toNodeId(namespaceTable).orElse(null);
      if (targetId == null) {
        continue;
      }
      UaNode target = getNode(targetId, ctx);
      if (target instanceof UaVariableNode variable
          && propertyName.equals(variable.getBrowseName())) {
        return ((DataValue) variable.getAttribute(AttributeId.Value)).getValue().getValue();
      }
    }
    return null;
  }

  private void validateInterfaces(
      NodeId typeId, List<Reference> typeRefs, CompiledType merged, CompileContext ctx) {

    List<NodeId> interfaceIds =
        typeRefs.stream()
            .filter(
                r ->
                    r.isForward()
                        && isReferenceSubtypeOfOrEqual(
                            r.getReferenceTypeId(), NodeIds.HasInterface))
            .map(r -> r.getTargetNodeId().toNodeId(namespaceTable).orElse(null))
            .filter(Objects::nonNull)
            .sorted(NODE_ID_ORDER)
            .toList();

    for (NodeId interfaceId : interfaceIds) {
      CompiledType interfaceModel = compileType(interfaceId, ctx);
      if (interfaceModel == null) {
        continue;
      }

      for (InstanceDeclaration member : interfaceModel.declarations.values()) {
        boolean mandatory = member.rule() == ModellingRule.MANDATORY;

        InstanceDeclaration local = merged.declarations.get(member.browsePath());
        if (local == null) {
          if (mandatory) {
            ctx.add(
                ModelDiagnostic.warning(
                    ModelDiagnostic.Code.INTERFACE_MEMBER_MISSING,
                    "mandatory member "
                        + member.browsePath()
                        + " of interface "
                        + interfaceId
                        + " has no matching declaration on "
                        + typeId,
                    member.browsePath(),
                    typeId));
          }
          continue;
        }

        // Every same-path collision is validated, not only the mandatory members: a local
        // declaration colliding with any interface member's BrowsePath must be similar to it.
        boolean compatible =
            local.nodeClass() == member.nodeClass()
                && (member.typeDefinitionId() == null
                    || (local.typeDefinitionId() != null
                        && isTypeSubtypeOfOrEqual(
                            local.typeDefinitionId(), member.typeDefinitionId(), ctx)));
        if (!compatible) {
          ctx.add(
              ModelDiagnostic.warning(
                  ModelDiagnostic.Code.INTERFACE_MEMBER_INCOMPATIBLE,
                  "declaration at "
                      + member.browsePath()
                      + " is not compatible with the member of interface "
                      + interfaceId,
                  member.browsePath(),
                  typeId));
          continue;
        }

        // A mandatory interface member must be represented as a mandatory declaration (Part 3
        // §4.10.2); an Optional local representation would let instances omit it.
        if (mandatory && local.rule() != ModellingRule.MANDATORY) {
          ctx.add(
              ModelDiagnostic.warning(
                  ModelDiagnostic.Code.INTERFACE_MEMBER_INCOMPATIBLE,
                  "mandatory member "
                      + member.browsePath()
                      + " of interface "
                      + interfaceId
                      + " is represented by a declaration with ModellingRule "
                      + local.rule()
                      + "; it shall be Mandatory",
                  member.browsePath(),
                  typeId));
        }
      }
    }
  }

  // endregion

  // region Reference rows

  private void recordRows(BrowsePath path, List<Reference> nodeRefs, CompiledType table) {
    for (Reference r : nodeRefs) {
      if (isHierarchical(r.getReferenceTypeId())) {
        // Forward hierarchical rows are the walk's domain (edges or traceable exclusions);
        // inverse hierarchical rows are their back-links.
        continue;
      }
      table.rows.add(
          new Row(
              ReferenceRow.absolute(
                  path, r.getReferenceTypeId(), r.getDirection(), r.getTargetNodeId()),
              false));
    }
  }

  /**
   * Resolve absolute rows whose targets are declarations of {@code table} into relative rows,
   * canonicalized to forward direction. A target matching more than one occurrence resolves through
   * the source's occurrence context when that disambiguates (the candidate sharing the longest
   * browse-path prefix with the source, if unique — a self-reference resolves to its own
   * occurrence); otherwise the row stays absolute (frozen) with a warning. A target matching
   * nothing stays absolute and may still resolve in an enclosing scope after grafting.
   */
  private void resolveRows(CompiledType table, CompileContext ctx) {
    Map<NodeId, List<BrowsePath>> pathsByDeclarationId = new HashMap<>();
    for (InstanceDeclaration d : table.declarations.values()) {
      pathsByDeclarationId
          .computeIfAbsent(d.declarationNodeId(), k -> new ArrayList<>())
          .add(d.browsePath());
    }

    List<Row> resolved = new ArrayList<>(table.rows.size());
    for (Row row : table.rows) {
      ReferenceRow ref = row.row();
      if (ref.isRelative() || row.frozen()) {
        resolved.add(row);
        continue;
      }

      ExpandedNodeId targetNodeId = requireNonNull(ref.targetNodeId());
      NodeId targetId = targetNodeId.toNodeId(namespaceTable).orElse(null);
      List<BrowsePath> targetPaths =
          targetId != null ? pathsByDeclarationId.getOrDefault(targetId, List.of()) : List.of();

      BrowsePath targetPath;
      if (targetPaths.isEmpty()) {
        resolved.add(row);
        continue;
      } else if (targetPaths.size() == 1) {
        targetPath = targetPaths.get(0);
      } else {
        targetPath = occurrenceContextTarget(ref.sourcePath(), targetPaths);
        if (targetPath == null) {
          ctx.add(
              ModelDiagnostic.warning(
                  ModelDiagnostic.Code.AMBIGUOUS_REFERENCE_TARGET,
                  "reference target "
                      + targetId
                      + " from "
                      + ref.sourcePath()
                      + " matches "
                      + targetPaths.size()
                      + " declaration occurrences; row kept absolute",
                  ref.sourcePath(),
                  targetId));
          resolved.add(new Row(ref, true));
          continue;
        }
      }

      ReferenceRow relative =
          ref.direction() == Reference.Direction.FORWARD
              ? ReferenceRow.relative(ref.sourcePath(), ref.referenceTypeId(), targetPath)
              : ReferenceRow.relative(targetPath, ref.referenceTypeId(), ref.sourcePath());
      resolved.add(new Row(relative, false));
    }

    table.rows.clear();
    table.rows.addAll(resolved);
  }

  /**
   * Pick the occurrence of a multi-occurrence target that the source's own occurrence context
   * identifies: the candidate sharing the longest non-empty browse-path prefix with the source
   * path, when that maximum is unique. Returns {@code null} when no candidate shares context or
   * several share equally (genuinely ambiguous).
   */
  private static @Nullable BrowsePath occurrenceContextTarget(
      BrowsePath sourcePath, List<BrowsePath> candidates) {

    BrowsePath best = null;
    int bestLength = 0;
    boolean unique = false;
    for (BrowsePath candidate : candidates) {
      int length = commonPrefixLength(sourcePath, candidate);
      if (length > bestLength) {
        bestLength = length;
        best = candidate;
        unique = true;
      } else if (length == bestLength) {
        unique = false;
      }
    }
    return unique ? best : null;
  }

  private static int commonPrefixLength(BrowsePath a, BrowsePath b) {
    List<QualifiedName> aElements = a.elements();
    List<QualifiedName> bElements = b.elements();
    int max = Math.min(aElements.size(), bElements.size());
    int i = 0;
    while (i < max && aElements.get(i).equals(bElements.get(i))) {
      i++;
    }
    return i;
  }

  private static ReferenceRow reroot(ReferenceRow row, BrowsePath prefix) {
    if (prefix.isRoot()) {
      return row;
    }
    BrowsePath targetPath = row.targetPath();
    if (targetPath != null) {
      return ReferenceRow.relative(
          prefix.concat(row.sourcePath()), row.referenceTypeId(), prefix.concat(targetPath));
    } else {
      return ReferenceRow.absolute(
          prefix.concat(row.sourcePath()),
          row.referenceTypeId(),
          row.direction(),
          requireNonNull(row.targetNodeId()));
    }
  }

  // endregion

  // region Address-space access

  private @Nullable UaNode getNode(NodeId nodeId, CompileContext ctx) {
    ctx.dependencies.add(nodeId);
    return ctx.nodes.computeIfAbsent(nodeId, addressSpaceManager::getManagedNode).orElse(null);
  }

  private List<Reference> getReferences(NodeId nodeId, CompileContext ctx) {
    ctx.dependencies.add(nodeId);
    return ctx.references.computeIfAbsent(nodeId, addressSpaceManager::getManagedReferences);
  }

  private boolean isHierarchical(NodeId referenceTypeId) {
    return isReferenceSubtypeOfOrEqual(referenceTypeId, NodeIds.HierarchicalReferences);
  }

  /**
   * Walk the inverse HasSubtype chain, reading through the dependency-tracking context, to decide
   * whether {@code typeId} is {@code supertypeId} or one of its subtypes. Used for TypeDefinition
   * and DataType compatibility, which the ReferenceType tree does not cover.
   */
  private boolean isTypeSubtypeOfOrEqual(NodeId typeId, NodeId supertypeId, CompileContext ctx) {
    return TypeSpaceRules.isTypeSubtypeOfOrEqual(
        typeId, supertypeId, id -> getReferences(id, ctx), namespaceTable);
  }

  /**
   * @return the immediate supertype of {@code typeId} — the lowest-ordered target of an inverse
   *     {@code HasSubtype} reference — or {@code null} if the type has none.
   */
  private @Nullable NodeId immediateSupertype(NodeId typeId, CompileContext ctx) {
    return TypeSpaceRules.immediateSupertype(typeId, id -> getReferences(id, ctx), namespaceTable);
  }

  private @Nullable QualifiedName findDefaultInstanceBrowseName(
      List<Reference> typeRefs, CompileContext ctx, @Nullable CompiledType supertype) {

    Object raw = readPropertyValue(typeRefs, DEFAULT_INSTANCE_BROWSE_NAME, ctx);
    if (raw instanceof QualifiedName qualifiedName) {
      return qualifiedName;
    }

    return supertype != null ? supertype.defaultInstanceBrowseName : null;
  }

  // endregion

  // region Fingerprint

  private static long fingerprint(
      NodeId typeId,
      AttributeSnapshot rootAttributes,
      @Nullable QualifiedName defaultInstanceBrowseName,
      List<InstanceDeclaration> declarations,
      List<DeclarationEdge> hierarchy,
      List<ReferenceRow> references,
      List<ModelDiagnostic> diagnostics) {

    long h = 0xcbf29ce484222325L; // FNV-1a 64 offset basis

    h = mix(h, typeId.toParseableString());
    h = mix(h, rootAttributes.contentHash());
    h = mix(h, String.valueOf(defaultInstanceBrowseName));

    for (InstanceDeclaration d : declarations) {
      h = mix(h, d.browsePath().toString());
      h = mix(h, String.valueOf(d.browseName()));
      h = mix(h, d.nodeClass().name());
      h = mix(h, d.declarationNodeId().toParseableString());
      h = mix(h, d.declaringTypeId().toParseableString());
      h = mix(h, d.overriddenDeclarations().toString());
      h = mix(h, d.modellingRuleId().toParseableString());
      h = mix(h, d.rule().name());
      h = mix(h, String.valueOf(d.typeDefinitionId()));
      h = mix(h, d.attributes().contentHash());
    }

    for (DeclarationEdge e : hierarchy) {
      h = mix(h, e.parentPath().toString());
      h = mix(h, e.referenceTypeId().toParseableString());
      h = mix(h, e.childPath().toString());
    }

    for (ReferenceRow r : references) {
      h = mix(h, r.sourcePath().toString());
      h = mix(h, r.referenceTypeId().toParseableString());
      h = mix(h, r.direction().name());
      h = mix(h, r.targetKey());
    }

    // Diagnostics capture validation-relevant state the tables alone do not — e.g. a nested
    // member type turning abstract changes the plan-time concrete-type requirement but no
    // declaration row — so semantic edits cannot leave the revision unchanged.
    for (ModelDiagnostic d : diagnostics) {
      h = mix(h, d.severity().name());
      h = mix(h, d.code().name());
      h = mix(h, String.valueOf(d.browsePath()));
      h = mix(h, String.valueOf(d.nodeId()));
      h = mix(h, d.message());
    }

    return h;
  }

  private static long mix(long h, String s) {
    for (int i = 0; i < s.length(); i++) {
      h ^= s.charAt(i);
      h *= 0x100000001b3L; // FNV-1a 64 prime
    }
    h ^= ' ';
    h *= 0x100000001b3L;
    return h;
  }

  // endregion

  // region Support types

  private static List<ModelDiagnostic> dedupe(List<ModelDiagnostic> diagnostics) {
    return List.copyOf(new LinkedHashSet<>(diagnostics));
  }

  private record Child(Reference reference, NodeId targetId, UaNode node) {}

  private record ChildExpansion(
      NodeId targetId,
      BrowsePath path,
      NodeId declaringTypeId,
      InstanceDeclaration declaration,
      @Nullable NodeId memberTypeId) {}

  private static final class ChildWalkFrame {
    final BrowsePath parentPath;
    final NodeId declaringTypeId;
    final List<Child> children;
    final @Nullable ChildExpansion expansion;

    int nextChild;

    ChildWalkFrame(
        BrowsePath parentPath,
        NodeId declaringTypeId,
        List<Child> children,
        @Nullable ChildExpansion expansion) {
      this.parentPath = parentPath;
      this.declaringTypeId = declaringTypeId;
      this.children = children;
      this.expansion = expansion;
    }

    boolean hasNext() {
      return nextChild < children.size();
    }

    Child next() {
      return children.get(nextChild++);
    }
  }

  private static final class CompileContext {
    final Map<NodeId, CompiledType> memo = new HashMap<>();
    final Set<NodeId> inProgress = new HashSet<>();
    final List<ModelDiagnostic> diagnostics = new ArrayList<>();
    final Set<NodeId> dependencies = new HashSet<>();

    // Address-space reads are cached for the lifetime of one compile() call: nodes and references
    // are read at least twice each (the type/declaration walk re-reads its own references, and the
    // subtype-chain walk revisits types), and the compiled model is a single point-in-time
    // snapshot.
    final Map<NodeId, Optional<UaNode>> nodes = new HashMap<>();
    final Map<NodeId, List<Reference>> references = new HashMap<>();

    boolean hasErrors;

    void add(ModelDiagnostic diagnostic) {
      diagnostics.add(diagnostic);
      hasErrors |= diagnostic.isError();
    }
  }

  /** A reference row plus its resolution state; frozen rows stay absolute (ambiguous target). */
  private record Row(ReferenceRow row, boolean frozen) {}

  /** One type's fully-inherited tables; memoized per compile call and grafted into parents. */
  private static final class CompiledType {
    final NodeId typeId;
    final AttributeSnapshot rootAttributes;
    final @Nullable QualifiedName defaultInstanceBrowseName;
    final LinkedHashMap<BrowsePath, InstanceDeclaration> declarations = new LinkedHashMap<>();
    final LinkedHashSet<DeclarationEdge> edges = new LinkedHashSet<>();
    final List<Row> rows = new ArrayList<>();

    // Every walked child by path, rule-less exclusions included, so sibling BrowseName
    // uniqueness (Part 3 §4.6.4) is validated independent of NodeId order.
    final Map<BrowsePath, NodeId> pathOccupants = new HashMap<>();

    CompiledType(
        NodeId typeId,
        AttributeSnapshot rootAttributes,
        @Nullable QualifiedName defaultInstanceBrowseName) {
      this.typeId = typeId;
      this.rootAttributes = rootAttributes;
      this.defaultInstanceBrowseName = defaultInstanceBrowseName;
    }

    boolean isAbstract() {
      return Boolean.TRUE.equals(rootAttributes.getOrNull(AttributeId.IsAbstract));
    }
  }

  private static final class CompiledModel implements TypeInstantiationModel {
    private final NodeId typeDefinitionId;
    private final long modelRevision;
    private final Set<NodeId> dependencies;
    private final List<InstanceDeclaration> declarations;
    private final Map<BrowsePath, InstanceDeclaration> declarationsByPath;
    private final List<InstanceDeclaration> placeholders;
    private final List<DeclarationEdge> hierarchy;
    private final List<ReferenceRow> references;
    private final List<ModelDiagnostic> diagnostics;
    private final AttributeSnapshot rootAttributes;
    private final @Nullable QualifiedName defaultInstanceBrowseName;

    CompiledModel(
        NodeId typeDefinitionId,
        long modelRevision,
        Set<NodeId> dependencies,
        List<InstanceDeclaration> declarations,
        List<DeclarationEdge> hierarchy,
        List<ReferenceRow> references,
        List<ModelDiagnostic> diagnostics,
        AttributeSnapshot rootAttributes,
        @Nullable QualifiedName defaultInstanceBrowseName) {

      this.typeDefinitionId = typeDefinitionId;
      this.modelRevision = modelRevision;
      this.dependencies = dependencies;
      this.declarations = declarations;
      this.hierarchy = hierarchy;
      this.references = references;
      this.diagnostics = diagnostics;
      this.rootAttributes = rootAttributes;
      this.defaultInstanceBrowseName = defaultInstanceBrowseName;

      this.declarationsByPath = new HashMap<>();
      declarations.forEach(d -> declarationsByPath.put(d.browsePath(), d));

      this.placeholders = declarations.stream().filter(InstanceDeclaration::isPlaceholder).toList();
    }

    @Override
    public NodeId typeDefinitionId() {
      return typeDefinitionId;
    }

    @Override
    public long modelRevision() {
      return modelRevision;
    }

    @Override
    public Set<NodeId> dependencies() {
      return dependencies;
    }

    @Override
    public List<InstanceDeclaration> declarations() {
      return declarations;
    }

    @Override
    public Optional<InstanceDeclaration> get(BrowsePath path) {
      return Optional.ofNullable(declarationsByPath.get(path));
    }

    @Override
    public List<InstanceDeclaration> placeholders() {
      return placeholders;
    }

    @Override
    public List<DeclarationEdge> hierarchy() {
      return hierarchy;
    }

    @Override
    public List<ReferenceRow> references() {
      return references;
    }

    @Override
    public List<ModelDiagnostic> diagnostics() {
      return diagnostics;
    }

    @Override
    public AttributeSnapshot rootAttributes() {
      return rootAttributes;
    }

    @Override
    public Optional<QualifiedName> defaultInstanceBrowseName() {
      return Optional.ofNullable(defaultInstanceBrowseName);
    }
  }

  // endregion
}
