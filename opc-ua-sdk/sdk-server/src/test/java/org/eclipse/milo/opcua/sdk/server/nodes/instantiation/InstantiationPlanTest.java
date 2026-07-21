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

import static org.eclipse.milo.opcua.sdk.server.nodes.instantiation.TypeFixtures.path;
import static org.eclipse.milo.opcua.sdk.server.nodes.instantiation.TypeFixtures.qn;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code NodeInstantiator.plan}: declaration selection, typed class resolution, NodeId
 * allocation and collision preflight, effective attributes, and reference rewrite — all as pure
 * computation over a model snapshot, mutating nothing.
 */
public class InstantiationPlanTest {

  private TypeFixtures fx;
  private UaNodeManager target;

  @BeforeEach
  void setUp() {
    fx = TypeFixtures.create();
    target = fx.newTargetManager();
  }

  private InstantiationRequest.Builder<UaObjectNode> objectRequest(NodeId typeId, String rootId) {
    return InstantiationRequest.of(UaObjectNode.class, typeId)
        .nodeId(fx.newNodeId(rootId))
        .target(target);
  }

  private static boolean hasDiagnostic(
      InstantiationPlan<?> plan,
      InstantiationDiagnostic.Severity severity,
      InstantiationDiagnostic.Code code) {

    return plan.diagnostics().stream().anyMatch(d -> d.severity() == severity && d.code() == code);
  }

  private static boolean hasError(InstantiationPlan<?> plan, InstantiationDiagnostic.Code code) {
    return hasDiagnostic(plan, InstantiationDiagnostic.Severity.ERROR, code);
  }

  private static boolean hasWarning(InstantiationPlan<?> plan, InstantiationDiagnostic.Code code) {
    return hasDiagnostic(plan, InstantiationDiagnostic.Severity.WARNING, code);
  }

  private static SkippedDeclaration skippedAt(InstantiationPlan<?> plan, BrowsePath path) {
    return plan.skippedDeclarations().stream()
        .filter(s -> s.declaration().browsePath().equals(path))
        .findFirst()
        .orElseThrow(() -> new AssertionError("expected a skipped declaration at " + path));
  }

  private static long countReferences(
      InstantiationPlan<?> plan, NodeId sourceNodeId, NodeId referenceTypeId, NodeId targetNodeId) {

    return plan.references().stream()
        .filter(
            r ->
                r.getSourceNodeId().equals(sourceNodeId)
                    && r.getReferenceTypeId().equals(referenceTypeId)
                    && r.getTargetNodeId().equals(targetNodeId.expanded())
                    && r.isForward())
        .count();
  }

  /** A distinct registrable node class for typed-registry resolution tests. */
  static class TestObjectNode extends UaObjectNode {
    TestObjectNode(
        UaNodeContext context, NodeId nodeId, QualifiedName browseName, LocalizedText displayName) {
      super(
          context,
          nodeId,
          browseName,
          displayName,
          LocalizedText.NULL_VALUE,
          UInteger.MIN,
          UInteger.MIN,
          ubyte(0));
    }
  }

  @Nested
  class Selection {

    /**
     * The defaults per Part 3 §6.4.4.4.2–.3: Mandatory members exist at every depth, Optional
     * members do not, and every skipped declaration is accounted for with a reason.
     */
    @Test
    void mandatoryPlannedAtEveryDepthOptionalOmittedByDefault() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("DeviceType", NodeIds.BaseObjectType);
      UaObjectNode child =
          fx.addObjectDeclaration(
              typeNode, "Child", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          child, "Leaf", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          typeNode, "Opt", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev").build());

      assertFalse(plan.hasErrors());
      assertTrue(plan.plannedNode(path("Child")).isPresent());
      assertTrue(plan.plannedNode(path("Child", "Leaf")).isPresent());
      assertTrue(plan.plannedNode(path("Opt")).isEmpty());
      assertEquals(
          SkippedDeclaration.Reason.OPTIONAL_NOT_SELECTED, skippedAt(plan, path("Opt")).reason());

      // Every declaration is accounted for: planned + skipped = model declarations.
      assertEquals(
          plan.model().declarations().size(),
          (plan.plannedNodes().size() - 1) + plan.skippedDeclarations().size());
    }

    /** Selecting a nested path implies its ancestors (U4). */
    @Test
    void includingNestedPathImpliesAncestors() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("ImplyType", NodeIds.BaseObjectType);
      UaObjectNode optA =
          fx.addObjectDeclaration(
              typeNode, "OptA", NodeIds.BaseObjectType, NodeIds.ModellingRule_Optional);
      fx.addVariableDeclaration(
          optA, "Leaf", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          optA, "LeafOpt", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .includeOptional(path("OptA", "LeafOpt"))
                      .build());

      assertFalse(plan.hasErrors());
      assertTrue(plan.plannedNode(path("OptA")).isPresent(), "ancestor implied");
      assertTrue(plan.plannedNode(path("OptA", "LeafOpt")).isPresent());
      assertTrue(
          plan.plannedNode(path("OptA", "Leaf")).isPresent(),
          "Mandatory under a selected Optional exists");
    }

    /** {@code includeAllOptionals()} selects Optionals but not vendor-rule declarations. */
    @Test
    void includeAllOptionalsSelectsOptionalsNotVendorRules() throws Exception {
      NodeId vendorRuleId = addVendorModellingRule("VendorRule1");

      UaObjectTypeNode typeNode = fx.addObjectType("AllOptType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "Opt", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);
      fx.addVariableDeclaration(typeNode, "Vendor", NodeIds.BaseDataVariableType, vendorRuleId);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(objectRequest(typeNode.getNodeId(), "Dev").includeAllOptionals().build());

      assertFalse(plan.hasErrors());
      assertTrue(plan.plannedNode(path("Opt")).isPresent());
      assertTrue(plan.plannedNode(path("Vendor")).isEmpty());
      assertEquals(SkippedDeclaration.Reason.VENDOR_RULE, skippedAt(plan, path("Vendor")).reason());
    }

    /**
     * A vendor-rule declaration is planned when explicitly selected (classification, not
     * filtering).
     */
    @Test
    void vendorRuleDeclarationPlannedWhenExplicitlyIncluded() throws Exception {
      NodeId vendorRuleId = addVendorModellingRule("VendorRule2");

      UaObjectTypeNode typeNode = fx.addObjectType("VendorType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(typeNode, "Vendor", NodeIds.BaseDataVariableType, vendorRuleId);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .includeOptional(path("Vendor"))
                      .build());

      assertFalse(plan.hasErrors());
      assertTrue(plan.plannedNode(path("Vendor")).isPresent());
    }

    /** The context-rich predicate escape hatch decides otherwise-undecided declarations (L2). */
    @Test
    void predicateEscapeHatchSelectsByDeclarationContext() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("PredType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "OptYes", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);
      fx.addVariableDeclaration(
          typeNode, "OptNo", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .includeOptionals(d -> "OptYes".equals(d.browseName().name()))
                      .build());

      assertFalse(plan.hasErrors());
      assertTrue(plan.plannedNode(path("OptYes")).isPresent());
      assertTrue(plan.plannedNode(path("OptNo")).isEmpty());
    }

    /** Optional Methods obey selection like everything else — the D6 fix. */
    @Test
    void optionalMethodObeysSelection() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("MethodSelType", NodeIds.BaseObjectType);
      fx.addMethodDeclaration(typeNode, "OptMethod", NodeIds.ModellingRule_Optional);

      InstantiationPlan<UaObjectNode> omitted =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev1").build());
      assertTrue(omitted.plannedNode(path("OptMethod")).isEmpty(), "not created unconditionally");
      assertEquals(
          SkippedDeclaration.Reason.OPTIONAL_NOT_SELECTED,
          skippedAt(omitted, path("OptMethod")).reason());

      InstantiationPlan<UaObjectNode> included =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev2")
                      .includeOptional(path("OptMethod"))
                      .build());
      assertTrue(included.plannedNode(path("OptMethod")).isPresent());
    }

    /**
     * Excluding an Optional prunes its whole subtree: mandatory descendants are {@code
     * ANCESTOR_OMITTED} (their BrowsePath does not exist — the valid exception, input b), never
     * planned, never referenced — the D1 orphan fix.
     */
    @Test
    void excludingOptionalPrunesSubtreeWithoutOrphans() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("PruneType", NodeIds.BaseObjectType);
      UaObjectNode optA =
          fx.addObjectDeclaration(
              typeNode, "OptA", NodeIds.BaseObjectType, NodeIds.ModellingRule_Optional);
      fx.addVariableDeclaration(
          optA, "MandLeaf", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .includeAllOptionals()
                      .excludeOptional(path("OptA"))
                      .build());

      assertFalse(plan.hasErrors(), "pruning an omitted ancestor's subtree is not an error");
      assertEquals(SkippedDeclaration.Reason.EXCLUDED, skippedAt(plan, path("OptA")).reason());
      assertEquals(
          SkippedDeclaration.Reason.ANCESTOR_OMITTED,
          skippedAt(plan, path("OptA", "MandLeaf")).reason());

      // No reference row may point at anything that was not planned.
      List<NodeId> plannedIds = plan.plannedNodes().stream().map(PlannedNode::nodeId).toList();
      NodeId parentlessTarget = fx.newNodeId("Dev/1:OptA/1:MandLeaf");
      assertTrue(
          plan.references().stream()
              .noneMatch(r -> r.getTargetNodeId().equals(parentlessTarget.expanded())),
          "no planned edge to an omitted target");
      assertFalse(plannedIds.contains(parentlessTarget));
    }

    /** Omitting a Mandatory declaration whose parent path exists is always a plan error. */
    @Test
    void mandatoryOmissionAtExistingPathIsPlanError() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("MandType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "Mand", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev").excludeOptional(path("Mand")).build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.MANDATORY_OMITTED));
      assertEquals(SkippedDeclaration.Reason.EXCLUDED, skippedAt(plan, path("Mand")).reason());
    }

    /** Two same-named Optionals at different paths select independently — the D7 fix. */
    @Test
    void sameNamedOptionalsAtDifferentPathsSelectIndependently() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("D7Type", NodeIds.BaseObjectType);
      UaObjectNode a =
          fx.addObjectDeclaration(
              typeNode, "A", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      UaObjectNode b =
          fx.addObjectDeclaration(
              typeNode, "B", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          a, "Opt", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);
      fx.addVariableDeclaration(
          b, "Opt", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .includeOptional(path("A", "Opt"))
                      .build());

      assertFalse(plan.hasErrors());
      assertTrue(plan.plannedNode(path("A", "Opt")).isPresent());
      assertTrue(plan.plannedNode(path("B", "Opt")).isEmpty());
    }

    /**
     * Placeholders are never auto-expanded; ExposesItsArray is surfaced but not materialized
     * (KD10).
     */
    @Test
    void placeholdersAndExposesItsArrayAreSkippedWithDiagnostics() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("PlaceholderType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          typeNode,
          "OptPlaceholder",
          NodeIds.BaseObjectType,
          NodeIds.ModellingRule_OptionalPlaceholder);
      fx.addObjectDeclaration(
          typeNode,
          "MandPlaceholder",
          NodeIds.BaseObjectType,
          NodeIds.ModellingRule_MandatoryPlaceholder);
      fx.addVariableDeclaration(
          typeNode, "Exposed", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_ExposesItsArray);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(objectRequest(typeNode.getNodeId(), "Dev").includeAllOptionals().build());

      assertFalse(plan.hasErrors());
      assertEquals(
          SkippedDeclaration.Reason.PLACEHOLDER, skippedAt(plan, path("OptPlaceholder")).reason());
      assertEquals(
          SkippedDeclaration.Reason.PLACEHOLDER, skippedAt(plan, path("MandPlaceholder")).reason());
      assertEquals(
          SkippedDeclaration.Reason.EXPOSES_ITS_ARRAY, skippedAt(plan, path("Exposed")).reason());

      assertTrue(hasWarning(plan, InstantiationDiagnostic.Code.MANDATORY_PLACEHOLDER_UNSATISFIED));
      assertTrue(hasWarning(plan, InstantiationDiagnostic.Code.EXPOSES_ITS_ARRAY_NOT_MATERIALIZED));
    }

    private NodeId addVendorModellingRule(String name) {
      NodeId ruleId = fx.newNodeId(name);
      UaObjectNode rule =
          new UaObjectNode(
              fx.context(),
              ruleId,
              qn(name),
              LocalizedText.english(name),
              LocalizedText.NULL_VALUE,
              UInteger.MIN,
              UInteger.MIN,
              ubyte(0));
      rule.addReference(
          new Reference(
              ruleId, NodeIds.HasTypeDefinition, NodeIds.ModellingRuleType.expanded(), true));
      fx.nodeManager().addNode(rule);
      return ruleId;
    }
  }

  @Nested
  class CollisionPreflight {

    /** Two planned nodes may not share a NodeId, whatever the conflict policy. */
    @Test
    void intraPlanCollisionIsPlanError() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("IntraType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "A", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          typeNode, "B", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      NodeId sharedId = fx.newNodeId("shared");
      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .assignNodeId(path("A"), sharedId)
                      .assignNodeId(path("B"), sharedId)
                      .build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.NODE_ID_COLLISION));
    }

    /** Under {@link ConflictPolicy#FAIL} an existing node in the target is a precise plan error. */
    @Test
    void targetCollisionUnderFailIsPlanError() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("FailType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "M", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      NodeId collidingId = fx.newNodeId("Dev/1:M");
      target.addNode(newTargetVariable(collidingId, qn("Other")));

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev").build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.NODE_ID_COLLISION));
      InstantiationDiagnostic diagnostic =
          plan.errors().stream()
              .filter(d -> d.code() == InstantiationDiagnostic.Code.NODE_ID_COLLISION)
              .findFirst()
              .orElseThrow();
      assertEquals(path("M"), diagnostic.browsePath());
      assertEquals(collidingId, diagnostic.nodeId());
    }

    /**
     * {@link ConflictPolicy#REUSE_COMPATIBLE} adopts a compatible existing node as {@code REUSE} —
     * never modified, and no HasTypeDefinition reference is planned for it.
     */
    @Test
    void reuseCompatibleAdoptsCompatibleExistingNode() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("ReuseType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "M", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      NodeId existingId = fx.newNodeId("Dev/1:M");
      target.addNode(newTargetVariable(existingId, qn("M")));
      target.addReference(
          new Reference(
              existingId,
              NodeIds.HasTypeDefinition,
              NodeIds.BaseDataVariableType.expanded(),
              true));

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .conflictPolicy(ConflictPolicy.REUSE_COMPATIBLE)
                      .build());

      assertFalse(plan.hasErrors());
      PlannedNode reused = plan.plannedNode(path("M")).orElseThrow();
      assertEquals(PlannedNode.Materialization.REUSE, reused.materialization());
      assertEquals(existingId, reused.nodeId());
      assertEquals(
          0,
          countReferences(
              plan, existingId, NodeIds.HasTypeDefinition, NodeIds.BaseDataVariableType),
          "a reused node keeps its own references; none are planned for it");
    }

    /**
     * An incompatible existing node fails the plan instead of being silently adopted or replaced.
     */
    @Test
    void incompatibleExistingNodeIsPlanError() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("IncompatType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "M", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      NodeId existingId = fx.newNodeId("Dev/1:M");
      target.addNode(newTargetVariable(existingId, qn("SomethingElse")));

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .conflictPolicy(ConflictPolicy.REUSE_COMPATIBLE)
                      .build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.INCOMPATIBLE_REUSE));
    }

    private UaVariableNode newTargetVariable(NodeId nodeId, QualifiedName browseName) {
      return new UaVariableNode(
          fx.context(),
          nodeId,
          browseName,
          LocalizedText.english(browseName.name()),
          LocalizedText.NULL_VALUE,
          UInteger.MIN,
          UInteger.MIN);
    }
  }

  @Nested
  class EffectiveAttributes {

    /**
     * The full attribute set propagates from declarations by rule — including
     * MinimumSamplingInterval, Historizing, and the three security attributes legacy dropped (D10)
     * — with the absent / explicit-null distinction preserved.
     */
    @Test
    void fullAttributeSetPropagatesIncludingSecurityAttributes() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("SecType", NodeIds.BaseObjectType);

      RolePermissionType[] rolePermissions = {
        new RolePermissionType(NodeIds.WellKnownRole_Anonymous, new PermissionType(uint(1)))
      };

      NodeId declId = fx.newNodeId("SecType.V");
      UaVariableNode declaration =
          new UaVariableNode(
              fx.context(),
              declId,
              qn("V"),
              LocalizedText.english("V"),
              LocalizedText.NULL_VALUE,
              UInteger.MIN,
              UInteger.MIN,
              rolePermissions,
              rolePermissions,
              new AccessRestrictionType(UShort.valueOf(2)),
              new DataValue(new Variant(42)),
              NodeIds.Int32,
              -1,
              null,
              ubyte(3),
              ubyte(3),
              250.0,
              true,
              null);
      declaration.addReference(
          new Reference(
              declId, NodeIds.HasTypeDefinition, NodeIds.BaseDataVariableType.expanded(), true));
      declaration.addReference(
          new Reference(
              declId, NodeIds.HasModellingRule, NodeIds.ModellingRule_Mandatory.expanded(), true));
      fx.nodeManager().addNode(declaration);
      typeNode.addReference(
          new Reference(typeNode.getNodeId(), NodeIds.HasComponent, declId.expanded(), true));

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev").build());

      AttributeSnapshot attributes =
          plan.plannedNode(path("V")).orElseThrow().effectiveAttributes();

      assertEquals(NodeIds.Int32, attributes.dataType());
      assertEquals(42, ((DataValue) attributes.getOrNull(AttributeId.Value)).getValue().getValue());
      assertEquals(250.0, attributes.getOrNull(AttributeId.MinimumSamplingInterval));
      assertEquals(true, attributes.getOrNull(AttributeId.Historizing));
      assertNotNull(attributes.getOrNull(AttributeId.RolePermissions));
      assertNotNull(attributes.getOrNull(AttributeId.UserRolePermissions));
      assertEquals(
          new AccessRestrictionType(UShort.valueOf(2)),
          attributes.getOrNull(AttributeId.AccessRestrictions));

      // Unset optional attributes stay absent, not null.
      assertFalse(attributes.contains(AttributeId.ArrayDimensions));
      assertFalse(attributes.contains(AttributeId.AccessLevelEx));
    }

    /**
     * Member-type defaults fill what the declaration leaves unset (declaration &gt; member type
     * &gt; class defaults): a declaration with no Value gets its VariableType's default Value; a
     * declaration providing its own keeps it.
     */
    @Test
    void memberTypeValueDefaultFillsUnsetDeclarationValue() throws Exception {
      DataValue typeDefault = new DataValue(new Variant(7));
      UaVariableTypeNode varType =
          fx.addVariableType(
              "DefaultingVarType", NodeIds.BaseDataVariableType, typeDefault, NodeIds.Int32, -1);

      UaObjectTypeNode typeNode = fx.addObjectType("DefaultsType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "Unset", varType.getNodeId(), NodeIds.ModellingRule_Mandatory);
      UaVariableNode withValue =
          fx.addVariableDeclaration(
              typeNode, "Set", varType.getNodeId(), NodeIds.ModellingRule_Mandatory);
      withValue.setValue(new DataValue(new Variant(99)));

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev").build());

      DataValue unsetValue =
          (DataValue)
              plan.plannedNode(path("Unset"))
                  .orElseThrow()
                  .effectiveAttributes()
                  .getOrNull(AttributeId.Value);
      assertNotNull(unsetValue, "type default fills the unset declaration Value");
      assertEquals(7, unsetValue.getValue().getValue());

      DataValue setValue =
          (DataValue)
              plan.plannedNode(path("Set"))
                  .orElseThrow()
                  .effectiveAttributes()
                  .getOrNull(AttributeId.Value);
      assertNotNull(setValue);
      assertEquals(99, setValue.getValue().getValue(), "declaration wins over member type");
    }

    /** Root attributes come from the type per Part 3 §6.4.2, then request overrides. */
    @Test
    void rootAttributesComeFromTypeThenRequestOverrides() throws Exception {
      UaVariableTypeNode varType =
          fx.addVariableType(
              "RootVarType",
              NodeIds.BaseDataVariableType,
              new DataValue(new Variant(3.14)),
              NodeIds.Double,
              -1);

      InstantiationPlan<UaVariableNode> plan =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(UaVariableNode.class, varType.getNodeId())
                      .nodeId(fx.newNodeId("Rooted"))
                      .target(target)
                      .value(new DataValue(new Variant(2.71)))
                      .displayName(LocalizedText.english("Rooted"))
                      .build());

      assertFalse(plan.hasErrors());
      PlannedNode root = plan.root();
      assertEquals(NodeClass.Variable, root.nodeClass());

      AttributeSnapshot attributes = root.effectiveAttributes();
      assertEquals(NodeIds.Double, attributes.dataType(), "DataType from the type");
      assertEquals(
          2.71,
          ((DataValue) attributes.getOrNull(AttributeId.Value)).getValue().getValue(),
          "Value overridden by the request");
      assertEquals(LocalizedText.english("Rooted"), attributes.getOrNull(AttributeId.DisplayName));
      assertFalse(attributes.contains(AttributeId.IsAbstract), "type-only attributes dropped");
      assertEquals(NodeClass.Variable, attributes.getOrNull(AttributeId.NodeClass));
    }

    /**
     * Root BrowseName precedence: request &gt; DefaultInstanceBrowseName &gt; type BrowseName
     * (D11/U8).
     */
    @Test
    void rootBrowseNamePrecedence() throws Exception {
      UaObjectTypeNode plainType = fx.addObjectType("PlainType", NodeIds.BaseObjectType);
      UaObjectTypeNode defaultingType = fx.addObjectType("DefaultingType", NodeIds.BaseObjectType);
      fx.setDefaultInstanceBrowseName(defaultingType, qn("DefaultName"));

      InstantiationPlan<UaObjectNode> requested =
          fx.instantiator()
              .plan(
                  objectRequest(defaultingType.getNodeId(), "R1")
                      .browseName(qn("Requested"))
                      .build());
      assertEquals(
          qn("Requested"),
          requested.root().effectiveAttributes().getOrNull(AttributeId.BrowseName));

      InstantiationPlan<UaObjectNode> defaulted =
          fx.instantiator().plan(objectRequest(defaultingType.getNodeId(), "R2").build());
      assertEquals(
          qn("DefaultName"),
          defaulted.root().effectiveAttributes().getOrNull(AttributeId.BrowseName));

      InstantiationPlan<UaObjectNode> typeName =
          fx.instantiator().plan(objectRequest(plainType.getNodeId(), "R3").build());
      assertEquals(
          qn("PlainType"), typeName.root().effectiveAttributes().getOrNull(AttributeId.BrowseName));
    }

    /** A request override violating Part 3 §6.2.8 narrowing is a plan error. */
    @Test
    void rootNarrowingViolationIsPlanError() throws Exception {
      UaVariableTypeNode varType =
          fx.addVariableType(
              "NumberVarType", NodeIds.BaseDataVariableType, null, NodeIds.Number, -1);

      InstantiationPlan<UaVariableNode> violating =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(UaVariableNode.class, varType.getNodeId())
                      .nodeId(fx.newNodeId("V1"))
                      .target(target)
                      .rootAttribute(AttributeId.DataType, NodeIds.String)
                      .build());
      assertTrue(hasError(violating, InstantiationDiagnostic.Code.VARIABLE_NARROWING));

      InstantiationPlan<UaVariableNode> narrowing =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(UaVariableNode.class, varType.getNodeId())
                      .nodeId(fx.newNodeId("V2"))
                      .target(target)
                      .rootAttribute(AttributeId.DataType, NodeIds.Int32)
                      .build());
      assertFalse(
          hasError(narrowing, InstantiationDiagnostic.Code.VARIABLE_NARROWING),
          "narrowing to a subtype is valid");
    }
  }

  @Nested
  class TypedClasses {

    /**
     * A registered snapshot constructor's class is selected at plan time, with its registry key.
     */
    @Test
    void registeredClassSelectedAtPlanTime() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("RegisteredType", NodeIds.BaseObjectType);
      fx.objectTypeManager()
          .registerObjectType(
              typeNode.getNodeId(),
              TestObjectNode.class,
              (context, nodeId, attributes) -> {
                throw new UnsupportedOperationException("plan never constructs");
              });

      InstantiationPlan<TestObjectNode> plan =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(TestObjectNode.class, typeNode.getNodeId())
                      .nodeId(fx.newNodeId("Dev"))
                      .target(target)
                      .build());

      assertFalse(plan.hasErrors());
      assertEquals(TestObjectNode.class, plan.root().javaClass());
      assertEquals(typeNode.getNodeId(), plan.root().constructorTypeId());
    }

    /**
     * An unsatisfiable expected root class is {@code INVALID_ROOT_CLASS} at plan time, not a cast.
     */
    @Test
    void invalidRootClassIsPlanTimeError() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("UnregisteredType", NodeIds.BaseObjectType);

      InstantiationPlan<TestObjectNode> plan =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(TestObjectNode.class, typeNode.getNodeId())
                      .nodeId(fx.newNodeId("Dev"))
                      .target(target)
                      .build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.INVALID_ROOT_CLASS));
    }

    /**
     * Q2: an unregistered subtype resolves to the nearest registered ancestor's class by walking
     * the {@code HasSubtype} chain, two levels deep here.
     */
    @Test
    void unregisteredSubtypeFallsBackToNearestRegisteredAncestor() throws Exception {
      UaObjectTypeNode baseType = fx.addObjectType("FallbackBase", NodeIds.BaseObjectType);
      UaObjectTypeNode midType = fx.addObjectType("FallbackMid", baseType.getNodeId());
      UaObjectTypeNode leafType = fx.addObjectType("FallbackLeaf", midType.getNodeId());

      fx.objectTypeManager()
          .registerObjectType(
              baseType.getNodeId(),
              TestObjectNode.class,
              (context, nodeId, attributes) -> {
                throw new UnsupportedOperationException("plan never constructs");
              });

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(leafType.getNodeId(), "Dev").build());

      assertFalse(plan.hasErrors());
      assertEquals(TestObjectNode.class, plan.root().javaClass());
      assertEquals(
          baseType.getNodeId(),
          plan.root().constructorTypeId(),
          "resolved from the nearest registered ancestor");
    }

    /** The per-request {@code classResolution(EXACT)} knob opts out of the fallback walk. */
    @Test
    void exactClassResolutionOptsOutOfFallback() throws Exception {
      UaObjectTypeNode baseType = fx.addObjectType("ExactBase", NodeIds.BaseObjectType);
      UaObjectTypeNode leafType = fx.addObjectType("ExactLeaf", baseType.getNodeId());

      fx.objectTypeManager()
          .registerObjectType(
              baseType.getNodeId(),
              TestObjectNode.class,
              (context, nodeId, attributes) -> {
                throw new UnsupportedOperationException("plan never constructs");
              });

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(leafType.getNodeId(), "Dev")
                      .classResolution(InstantiationRequest.ClassResolution.EXACT)
                      .build());

      assertFalse(plan.hasErrors());
      assertEquals(UaObjectNode.class, plan.root().javaClass());
      assertNull(plan.root().constructorTypeId());
    }

    /**
     * R4: a legacy tuple-signature registration resolves under the new engine — same class, same
     * registry key — while remaining resolvable by the untouched legacy lookup.
     */
    @Test
    void legacyTupleRegistrationResolvesUnderBothEngines() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("TupleType", NodeIds.BaseObjectType);
      fx.objectTypeManager()
          .registerObjectType(
              typeNode.getNodeId(),
              TestObjectNode.class,
              (context,
                  nodeId,
                  browseName,
                  displayName,
                  description,
                  writeMask,
                  userWriteMask,
                  rolePermissions,
                  userRolePermissions,
                  accessRestrictions) -> {
                throw new UnsupportedOperationException("plan never constructs");
              });

      InstantiationPlan<TestObjectNode> plan =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(TestObjectNode.class, typeNode.getNodeId())
                      .nodeId(fx.newNodeId("Dev"))
                      .target(target)
                      .build());

      assertFalse(plan.hasErrors());
      assertEquals(TestObjectNode.class, plan.root().javaClass());
      assertEquals(typeNode.getNodeId(), plan.root().constructorTypeId());

      assertTrue(
          fx.objectTypeManager().getNodeConstructor(typeNode.getNodeId()).isPresent(),
          "legacy lookup unchanged");
    }

    /** Instantiating an abstract root type is a plan error. */
    @Test
    void abstractRootTypeIsPlanError() throws Exception {
      UaObjectTypeNode abstractType =
          fx.addObjectType("AbstractRootType", NodeIds.BaseObjectType, true);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(abstractType.getNodeId(), "Dev").build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.ABSTRACT_TYPE));
    }
  }

  @Nested
  class ConcreteTypes {

    private UaObjectTypeNode typeNode;
    private NodeId abstractChannelId;
    private NodeId concreteChannelId;
    private NodeId unrelatedTypeId;

    @BeforeEach
    void buildGraph() {
      UaObjectTypeNode abstractChannel =
          fx.addObjectType("AbstractChannel", NodeIds.BaseObjectType, true);
      abstractChannelId = abstractChannel.getNodeId();
      fx.addVariableDeclaration(
          abstractChannel, "Base", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode concreteChannel = fx.addObjectType("ConcreteChannel", abstractChannelId);
      concreteChannelId = concreteChannel.getNodeId();
      fx.addVariableDeclaration(
          concreteChannel, "Extra", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      unrelatedTypeId = fx.addObjectType("UnrelatedType", NodeIds.BaseObjectType).getNodeId();

      typeNode = fx.addObjectType("DeviceWithChannel", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          typeNode, "Channel", abstractChannelId, NodeIds.ModellingRule_Mandatory);
    }

    /** An abstract member type without a concreteType resolution is a plan error (input i). */
    @Test
    void abstractMemberWithoutResolutionIsPlanError() throws Exception {
      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev").build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.ABSTRACT_MEMBER_TYPE));
    }

    /**
     * A valid concreteType selection substitutes the member's TypeDefinition — on the planned node
     * and its HasTypeDefinition reference — and warns that concrete-only members are not expanded.
     */
    @Test
    void concreteTypeSelectionSubstitutesTypeDefinition() throws Exception {
      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .concreteType(path("Channel"), concreteChannelId)
                      .build());

      assertFalse(plan.hasErrors());
      PlannedNode channel = plan.plannedNode(path("Channel")).orElseThrow();
      assertEquals(concreteChannelId, channel.typeDefinitionId());
      assertEquals(
          1, countReferences(plan, channel.nodeId(), NodeIds.HasTypeDefinition, concreteChannelId));
      assertEquals(
          0, countReferences(plan, channel.nodeId(), NodeIds.HasTypeDefinition, abstractChannelId));

      // The declared type's members are planned; the concrete subtype's additions are not (yet).
      assertTrue(plan.plannedNode(path("Channel", "Base")).isPresent());
      assertTrue(plan.plannedNode(path("Channel", "Extra")).isEmpty());
      assertTrue(hasWarning(plan, InstantiationDiagnostic.Code.CONCRETE_TYPE_NOT_EXPANDED));
    }

    /** A concreteType that is not the declared type or a subtype of it is rejected. */
    @Test
    void unrelatedConcreteTypeIsPlanError() throws Exception {
      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .concreteType(path("Channel"), unrelatedTypeId)
                      .build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.CONCRETE_TYPE_INVALID));
    }

    /** A concreteType that is itself abstract is rejected. */
    @Test
    void abstractConcreteTypeIsPlanError() throws Exception {
      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .concreteType(path("Channel"), abstractChannelId)
                      .build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.ABSTRACT_MEMBER_TYPE));
    }
  }

  @Nested
  class References {

    /** Hierarchy edges are rewritten onto instance ids with their ReferenceTypes preserved. */
    @Test
    void hierarchyEdgesRewrittenWithReferenceTypesPreserved() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("EdgeType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode,
          "Comp",
          NodeIds.BaseDataVariableType,
          NodeIds.ModellingRule_Mandatory,
          NodeIds.HasComponent);
      fx.addVariableDeclaration(
          typeNode,
          "Prop",
          NodeIds.PropertyType,
          NodeIds.ModellingRule_Mandatory,
          NodeIds.HasProperty);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev").build());

      assertFalse(plan.hasErrors());
      NodeId rootId = plan.root().nodeId();
      NodeId compId = plan.plannedNode(path("Comp")).orElseThrow().nodeId();
      NodeId propId = plan.plannedNode(path("Prop")).orElseThrow().nodeId();

      assertEquals(1, countReferences(plan, rootId, NodeIds.HasComponent, compId));
      assertEquals(1, countReferences(plan, rootId, NodeIds.HasProperty, propId));
    }

    /**
     * A declaration-internal non-hierarchical reference is re-mapped onto the corresponding
     * instances exactly once, forward — the D4 fix, §6.4.3 direct-connection consistency.
     */
    @Test
    void internalNonHierarchicalRowRemappedExactlyOnce() throws Exception {
      UaReferenceTypeNode causeType =
          fx.addReferenceType("HasCauseX", NodeIds.NonHierarchicalReferences);

      UaObjectTypeNode typeNode = fx.addObjectType("SMType", NodeIds.BaseObjectType);
      UaObjectNode a =
          fx.addObjectDeclaration(
              typeNode, "A", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      UaObjectNode b =
          fx.addObjectDeclaration(
              typeNode, "B", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      a.addReference(
          new Reference(a.getNodeId(), causeType.getNodeId(), b.getNodeId().expanded(), true));

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev").build());

      assertFalse(plan.hasErrors());
      NodeId aId = plan.plannedNode(path("A")).orElseThrow().nodeId();
      NodeId bId = plan.plannedNode(path("B")).orElseThrow().nodeId();

      assertEquals(1, countReferences(plan, aId, causeType.getNodeId(), bId));
      // Nothing points back into the type's declarations.
      assertTrue(
          plan.references().stream()
              .noneMatch(r -> r.getTargetNodeId().equals(a.getNodeId().expanded())),
          "no instance reference may point at a declaration node");
    }

    /** An internal row whose target is omitted produces no reference at all. */
    @Test
    void internalRowToOmittedTargetIsDropped() throws Exception {
      UaReferenceTypeNode causeType =
          fx.addReferenceType("HasCauseY", NodeIds.NonHierarchicalReferences);

      UaObjectTypeNode typeNode = fx.addObjectType("OmitType", NodeIds.BaseObjectType);
      UaObjectNode a =
          fx.addObjectDeclaration(
              typeNode, "A", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      UaObjectNode opt =
          fx.addObjectDeclaration(
              typeNode, "Opt", NodeIds.BaseObjectType, NodeIds.ModellingRule_Optional);
      a.addReference(
          new Reference(a.getNodeId(), causeType.getNodeId(), opt.getNodeId().expanded(), true));

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev").build());

      assertFalse(plan.hasErrors());
      NodeId aId = plan.plannedNode(path("A")).orElseThrow().nodeId();
      assertTrue(
          plan.references().stream()
              .noneMatch(
                  r ->
                      r.getSourceNodeId().equals(aId)
                          && r.getReferenceTypeId().equals(causeType.getNodeId())),
          "no planned edge to an omitted target");
    }

    /** External rows are copied verbatim by default and droppable via the named policy (KD7). */
    @Test
    void externalRowsFollowReplicationPolicy() throws Exception {
      UaReferenceTypeNode refType =
          fx.addReferenceType("RefersToX", NodeIds.NonHierarchicalReferences);

      UaObjectTypeNode typeNode = fx.addObjectType("ExtType", NodeIds.BaseObjectType);
      UaObjectNode a =
          fx.addObjectDeclaration(
              typeNode, "A", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      a.addReference(
          new Reference(a.getNodeId(), refType.getNodeId(), NodeIds.Server.expanded(), true));

      InstantiationPlan<UaObjectNode> copied =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev1").build());
      assertFalse(copied.hasErrors());
      NodeId aId = copied.plannedNode(path("A")).orElseThrow().nodeId();
      assertEquals(
          1,
          countReferences(copied, aId, refType.getNodeId(), NodeIds.Server),
          "external target copied verbatim (absolute)");

      InstantiationPlan<UaObjectNode> omitted =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev2")
                      .referenceReplication(
                          new ReferenceReplicationPolicy(
                              ReferenceReplicationPolicy.InternalReferences.REMAP,
                              ReferenceReplicationPolicy.ExternalReferences.OMIT))
                      .build());
      assertFalse(omitted.hasErrors());
      NodeId aId2 = omitted.plannedNode(path("A")).orElseThrow().nodeId();
      assertTrue(
          omitted.references().stream()
              .noneMatch(
                  r ->
                      r.getSourceNodeId().equals(aId2)
                          && r.getReferenceTypeId().equals(refType.getNodeId())),
          "external rows droppable by policy");
    }

    /**
     * {@code HasModellingRule} is dropped for normal instances, retained for INSTANCE_DECLARATION
     * (U14).
     */
    @Test
    void hasModellingRuleRetentionIsPurposeDependent() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("PurposeType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "M", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      InstantiationPlan<UaObjectNode> normal =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev1").build());
      assertTrue(
          normal.references().stream()
              .noneMatch(r -> r.getReferenceTypeId().equals(NodeIds.HasModellingRule)),
          "normal instances carry no ModellingRules (Part 3 §6.4.4.3)");

      InstantiationPlan<UaObjectNode> declarationPurpose =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev2")
                      .purpose(InstantiationPurpose.INSTANCE_DECLARATION)
                      .build());
      NodeId mId = declarationPurpose.plannedNode(path("M")).orElseThrow().nodeId();
      assertEquals(
          1,
          countReferences(
              declarationPurpose, mId, NodeIds.HasModellingRule, NodeIds.ModellingRule_Mandatory));
    }

    /**
     * The root's HasTypeDefinition and the parent attachment are planned references like any other.
     */
    @Test
    void rootTypeDefinitionAndParentAttachmentPlanned() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("AttachType", NodeIds.BaseObjectType);
      NodeId parentId = fx.newNodeId("SomeFolder");

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .parent(parentId, NodeIds.Organizes)
                      .build());

      assertFalse(plan.hasErrors());
      NodeId rootId = plan.root().nodeId();
      assertEquals(
          1, countReferences(plan, rootId, NodeIds.HasTypeDefinition, typeNode.getNodeId()));
      assertEquals(1, countReferences(plan, parentId, NodeIds.Organizes, rootId));
    }
  }

  @Nested
  class NodeIdAllocation {

    /**
     * The strategy receives full context and supports any identifier scheme; pins win over it (U6).
     */
    @Test
    void strategyReceivesFullContextAndPinningWins() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("IdType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "A", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          typeNode, "B", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      NodeId rootId = fx.newNodeId("Dev");
      NodeId pinnedId = new NodeId(1, uint(4242));
      List<NodeIdContext> contexts = new ArrayList<>();

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(UaObjectNode.class, typeNode.getNodeId())
                      .nodeId(rootId)
                      .target(target)
                      .nodeIdStrategy(
                          ctx -> {
                            contexts.add(ctx);
                            return new NodeId(1, uint(1000 + ctx.path().depth()));
                          })
                      .assignNodeId(path("B"), pinnedId)
                      .build());

      assertFalse(plan.hasErrors());
      assertEquals(new NodeId(1, uint(1001)), plan.plannedNode(path("A")).orElseThrow().nodeId());
      assertEquals(pinnedId, plan.plannedNode(path("B")).orElseThrow().nodeId());

      assertEquals(1, contexts.size(), "pinned paths bypass the strategy");
      assertEquals(rootId, contexts.get(0).rootNodeId());
      assertEquals(path("A"), contexts.get(0).path());
      assertEquals(qn("A"), contexts.get(0).declaration().browseName());
    }

    /**
     * The default allocator escapes reserved characters, so the two WP1-pinned legacy ambiguities
     * cannot collide: a member named {@code "A/1:B"} vs a nested member {@code A}/{@code B}, and a
     * numeric root {@code 7} vs a String root {@code "7"}.
     */
    @Test
    void defaultAllocatorEscapesReservedCharacters() throws Exception {
      UaObjectTypeNode nestedType = fx.addObjectType("NestedPathType", NodeIds.BaseObjectType);
      UaObjectNode a =
          fx.addObjectDeclaration(
              nestedType, "A", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          a, "B", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode slashType = fx.addObjectType("SlashNameType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          slashType, "A/1:B", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      NodeId rootId = fx.newNodeId("AmbigRoot");

      InstantiationPlan<UaObjectNode> nested =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(UaObjectNode.class, nestedType.getNodeId())
                      .nodeId(rootId)
                      .target(target)
                      .build());
      InstantiationPlan<UaObjectNode> slash =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(UaObjectNode.class, slashType.getNodeId())
                      .nodeId(rootId)
                      .target(target)
                      .build());

      NodeId nestedLeafId = nested.plannedNode(path("A", "B")).orElseThrow().nodeId();
      NodeId slashLeafId = slash.plannedNode(path("A/1:B")).orElseThrow().nodeId();
      assertNotEquals(nestedLeafId, slashLeafId, "escaping disambiguates the WP1 collision");

      // Numeric vs String root identifiers cannot derive identical child ids either.
      UaObjectTypeNode rootAmbigType = fx.addObjectType("RootAmbigType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          rootAmbigType, "Foo", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      InstantiationPlan<UaObjectNode> numericRoot =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(UaObjectNode.class, rootAmbigType.getNodeId())
                      .nodeId(new NodeId(1, uint(7)))
                      .target(target)
                      .build());
      InstantiationPlan<UaObjectNode> stringRoot =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(UaObjectNode.class, rootAmbigType.getNodeId())
                      .nodeId(new NodeId(1, "7"))
                      .target(target)
                      .build());

      assertNotEquals(
          numericRoot.plannedNode(path("Foo")).orElseThrow().nodeId(),
          stringRoot.plannedNode(path("Foo")).orElseThrow().nodeId());
    }

    /**
     * {@code legacyPathStrings()} reproduces the WP1-pinned formula exactly: root identifier
     * string-concatenated with {@code /ns:name} per element, a String identifier in the root's
     * namespace — including the pinned numeric-root case ({@code 7} deriving {@code "7/1:Foo"}).
     */
    @Test
    void legacyPathStringsReproducesCharacterizedFormula() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("FormulaType", NodeIds.BaseObjectType);
      UaObjectNode child =
          fx.addObjectDeclaration(
              typeNode, "Child", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          child, "Leaf", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(UaObjectNode.class, typeNode.getNodeId())
                      .nodeId(new NodeId(1, "TestRoot"))
                      .target(target)
                      .legacyPathStrings()
                      .build());

      assertFalse(plan.hasErrors());
      assertEquals(
          new NodeId(1, "TestRoot/1:Child"),
          plan.plannedNode(path("Child")).orElseThrow().nodeId());
      assertEquals(
          new NodeId(1, "TestRoot/1:Child/1:Leaf"),
          plan.plannedNode(path("Child", "Leaf")).orElseThrow().nodeId());

      UaObjectTypeNode fooType = fx.addObjectType("FooType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          fooType, "Foo", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      InstantiationPlan<UaObjectNode> numericRoot =
          fx.instantiator()
              .plan(
                  InstantiationRequest.of(UaObjectNode.class, fooType.getNodeId())
                      .nodeId(new NodeId(1, uint(7)))
                      .target(target)
                      .legacyPathStrings()
                      .build());

      assertEquals(
          new NodeId(1, "7/1:Foo"), numericRoot.plannedNode(path("Foo")).orElseThrow().nodeId());
    }
  }

  @Nested
  class Methods {

    /** Methods are copied by default; SHARE references the type's Method node per call (KD9/L6). */
    @Test
    void methodCopyByDefaultShareByRequest() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("MethodType", NodeIds.BaseObjectType);
      UaMethodNode method =
          fx.addMethodDeclaration(typeNode, "Start", NodeIds.ModellingRule_Mandatory);
      fx.addArgumentsProperty(method, "InputArguments", new Object[0]);

      // The InputArguments property carries a ns0 BrowseName, so its path element is 0:.
      BrowsePath argumentsPath = BrowsePath.of(qn("Start"), new QualifiedName(0, "InputArguments"));

      InstantiationPlan<UaObjectNode> copied =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Dev1").build());
      assertFalse(copied.hasErrors());
      PlannedNode copiedMethod = copied.plannedNode(path("Start")).orElseThrow();
      assertEquals(PlannedNode.Materialization.CREATE, copiedMethod.materialization());
      assertEquals(UaMethodNode.class, copiedMethod.javaClass());
      assertNotEquals(method.getNodeId(), copiedMethod.nodeId());
      assertTrue(
          copied.plannedNode(argumentsPath).isPresent(),
          "argument properties copied with the method");

      InstantiationPlan<UaObjectNode> shared =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev2")
                      .methodInstantiation(MethodInstantiation.SHARE)
                      .build());
      assertFalse(shared.hasErrors());
      PlannedNode sharedMethod = shared.plannedNode(path("Start")).orElseThrow();
      assertEquals(PlannedNode.Materialization.SHARE, sharedMethod.materialization());
      assertEquals(method.getNodeId(), sharedMethod.nodeId(), "the type's own Method node");
      assertEquals(
          SkippedDeclaration.Reason.METHOD_SHARED, skippedAt(shared, argumentsPath).reason());

      // The hierarchy edge targets the shared node; nothing else is planned for it.
      assertEquals(
          1,
          countReferences(
              shared, shared.root().nodeId(), NodeIds.HasComponent, method.getNodeId()));
      assertTrue(
          shared.references().stream()
              .noneMatch(r -> r.getSourceNodeId().equals(method.getNodeId())),
          "a shared node keeps its own references; none are planned for it");
    }

    /** A bindMethod path that matches no planned Method produces a plan warning. */
    @Test
    void bindMethodPathMatchingNothingWarns() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("BindType", NodeIds.BaseObjectType);
      fx.addMethodDeclaration(typeNode, "Start", NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          typeNode, "NotAMethod", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      InstantiationPlan<UaObjectNode> matched =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev1")
                      .bindMethod(path("Start"), m -> {})
                      .build());
      assertFalse(hasWarning(matched, InstantiationDiagnostic.Code.UNMATCHED_PATH));

      InstantiationPlan<UaObjectNode> unmatched =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev2")
                      .bindMethod(path("NoSuchMethod"), m -> {})
                      .bindMethod(path("NotAMethod"), m -> {})
                      .build());
      assertEquals(
          2,
          unmatched.warnings().stream()
              .filter(d -> d.code() == InstantiationDiagnostic.Code.UNMATCHED_PATH)
              .count());
    }
  }

  @Nested
  class PurityAndDeterminism {

    /**
     * Plan is provably side-effect free: neither the target manager nor the source address space is
     * mutated — checked with WP4's generation tracking plus node/reference counts and a sampled
     * identity check.
     */
    @Test
    void planPerformsZeroMutation() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("PureType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "M", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          typeNode, "Opt", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

      // Pre-existing target content, so collision preflight has something to read.
      NodeId existingId = fx.newNodeId("Dev/1:M");
      UaVariableNode existing =
          new UaVariableNode(
              fx.context(),
              existingId,
              qn("M"),
              LocalizedText.english("M"),
              LocalizedText.NULL_VALUE,
              UInteger.MIN,
              UInteger.MIN);
      target.addNode(existing);
      target.addReference(
          new Reference(
              existingId,
              NodeIds.HasTypeDefinition,
              NodeIds.BaseDataVariableType.expanded(),
              true));

      NodeManager.Generation targetGeneration = target.getGeneration();
      NodeManager.Generation sourceGeneration = fx.nodeManager().getGeneration();
      List<NodeId> targetNodeIds = List.copyOf(target.getNodeIds());
      int sourceNodeCount = fx.nodeManager().getNodes().size();

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .includeAllOptionals()
                      .conflictPolicy(ConflictPolicy.REUSE_COMPATIBLE)
                      .build());

      assertFalse(plan.hasErrors());
      assertTrue(targetGeneration.isCurrent(), "plan wrote nothing to the target");
      assertTrue(sourceGeneration.isCurrent(), "plan wrote nothing to the source");
      assertEquals(targetNodeIds, target.getNodeIds());
      assertEquals(sourceNodeCount, fx.nodeManager().getNodes().size());

      Optional<UaNode> sampled = target.getNode(existingId);
      assertTrue(sampled.isPresent());
      assertTrue(sampled.get() == existing, "sampled node is byte-identical (same instance)");
      assertEquals(qn("M"), sampled.get().getBrowseName());
    }

    /** Plans are deterministic: the same model and request produce identical output, twice. */
    @Test
    void plansAreDeterministicAcrossRuns() throws Exception {
      UaReferenceTypeNode causeType =
          fx.addReferenceType("HasCauseZ", NodeIds.NonHierarchicalReferences);

      UaObjectTypeNode typeNode = fx.addObjectType("DetType", NodeIds.BaseObjectType);
      UaObjectNode a =
          fx.addObjectDeclaration(
              typeNode, "A", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      UaObjectNode b =
          fx.addObjectDeclaration(
              typeNode, "B", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      a.addReference(
          new Reference(a.getNodeId(), causeType.getNodeId(), b.getNodeId().expanded(), true));
      fx.addVariableDeclaration(
          typeNode, "Opt", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

      InstantiationRequest<UaObjectNode> request =
          objectRequest(typeNode.getNodeId(), "Dev").includeAllOptionals().build();

      InstantiationPlan<UaObjectNode> first = fx.instantiator().plan(request);
      InstantiationPlan<UaObjectNode> second = fx.instantiator().plan(request);

      assertEquals(
          first.plannedNodes().stream().map(PlannedNode::browsePath).toList(),
          second.plannedNodes().stream().map(PlannedNode::browsePath).toList());
      assertEquals(
          first.plannedNodes().stream().map(PlannedNode::nodeId).toList(),
          second.plannedNodes().stream().map(PlannedNode::nodeId).toList());
      assertEquals(first.references(), second.references());
      assertEquals(
          first.skippedDeclarations().stream().map(SkippedDeclaration::reason).toList(),
          second.skippedDeclarations().stream().map(SkippedDeclaration::reason).toList());
      assertEquals(
          first.diagnostics().stream().map(InstantiationDiagnostic::message).toList(),
          second.diagnostics().stream().map(InstantiationDiagnostic::message).toList());
    }

    /** Request paths that match nothing produce warnings instead of silent no-ops. */
    @Test
    void unmatchedRequestPathsWarn() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("WarnType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "Opt", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "Dev")
                      .includeOptional(path("NoSuchMember"))
                      .assignNodeId(
                          path("Opt"), fx.newNodeId("unused")) // skipped: pin has no effect
                      .build());

      assertFalse(plan.hasErrors());
      assertEquals(
          2,
          plan.warnings().stream()
              .filter(d -> d.code() == InstantiationDiagnostic.Code.UNMATCHED_PATH)
              .count());
    }
  }
}
