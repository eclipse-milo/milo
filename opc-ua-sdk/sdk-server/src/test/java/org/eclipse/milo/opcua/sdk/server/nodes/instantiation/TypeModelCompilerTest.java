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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TypeModelCompiler}: core model semantics plus validation, diagnostics, reference
 * rows, and robustness.
 *
 * <p>The nested-inheritance and declaration-override tests encode the <em>spec-correct</em>
 * behavior — the opposite of the legacy defects pinned in {@code NodeFactoryCharacterizationTest}
 * ({@code nestedMemberTypeMissingSupertypeMembers}, {@code
 * typeDeclaredChildShadowsOnDeclarationOverride}); they fail against legacy semantics by
 * construction.
 */
public class TypeModelCompilerTest {

  private TypeFixtures fx;

  @BeforeEach
  void setUp() {
    fx = TypeFixtures.create();
  }

  /** Members typed as subtypes expand with their full inherited hierarchy. */
  @Nested
  class NestedMemberTypeExpansion {

    /**
     * Spec-correct per Part 3 §6.3.3.2 ("instances are described by the fully-inherited IDH ... at
     * every level of nesting"): a member typed as a subtype includes its supertype-declared
     * mandatory members. Legacy omits them (pinned in the characterization tests).
     */
    @Test
    void nestedMemberTypeIncludesSupertypeDeclaredMembers() throws Exception {
      UaObjectTypeNode baseSmType = fx.addObjectType("BaseSMType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          baseSmType,
          "CurrentState",
          NodeIds.BaseDataVariableType,
          NodeIds.ModellingRule_Mandatory);
      UaObjectTypeNode derivedSmType = fx.addObjectType("DerivedSMType", baseSmType.getNodeId());

      UaObjectTypeNode typeNode = fx.addObjectType("D2Type", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          typeNode, "SM", derivedSmType.getNodeId(), NodeIds.ModellingRule_Mandatory);
      fx.addObjectDeclaration(
          typeNode, "SM2", baseSmType.getNodeId(), NodeIds.ModellingRule_Mandatory);

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      InstanceDeclaration nested =
          model
              .get(path("SM", "CurrentState"))
              .orElseThrow(
                  () ->
                      new AssertionError(
                          "member typed as a subtype must include supertype-declared members"));
      assertEquals(ModellingRule.MANDATORY, nested.rule());
      assertEquals(
          baseSmType.getNodeId(),
          nested.declaringTypeId(),
          "provenance: contributed by the member type's supertype");

      assertTrue(
          model.get(path("SM2", "CurrentState")).isPresent(),
          "control: member typed directly as the declaring type has the member too");
    }
  }

  /**
   * Explicit declarations override member-type defaults, retaining distinct inherited references.
   */
  @Nested
  class Overrides {

    /**
     * Spec-correct per Part 3 §6.3.3.3: a same-path on-declaration child overrides the member
     * type's default, and the two walks' hierarchical rows canonicalize to one logical edge. Legacy
     * discards the override and wires the duplicate rows (pinned in the characterization tests).
     */
    @Test
    void explicitDeclarationWinsOverTypeDefaultWithSingleLogicalEdge() throws Exception {
      UaObjectTypeNode memberType = fx.addObjectType("D5MemberType", NodeIds.BaseObjectType);
      UaVariableNode typeChild =
          fx.addVariableDeclaration(
              memberType, "Child", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      typeChild.setDataType(NodeIds.BaseDataType);
      typeChild.setValue(new DataValue(new Variant("fromType")));

      UaObjectTypeNode typeNode = fx.addObjectType("D5Type", NodeIds.BaseObjectType);
      UaObjectNode m =
          fx.addObjectDeclaration(
              typeNode, "M", memberType.getNodeId(), NodeIds.ModellingRule_Mandatory);
      UaVariableNode declChild =
          fx.addVariableDeclaration(
              m, "Child", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      declChild.setDataType(NodeIds.Int32);
      declChild.setValue(new DataValue(new Variant(42)));

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      InstanceDeclaration child = model.get(path("M", "Child")).orElseThrow();
      assertEquals(declChild.getNodeId(), child.declarationNodeId(), "explicit declaration wins");
      assertEquals(NodeIds.Int32, child.attributes().dataType());
      DataValue childValue = child.attributes().value();
      assertNotNull(childValue);
      assertEquals(new Variant(42), childValue.getValue());
      assertEquals(
          List.of(typeChild.getNodeId()),
          child.overriddenDeclarations(),
          "the member-type default is recorded as overridden");

      long edges =
          model.hierarchy().stream()
              .filter(
                  e -> e.parentPath().equals(path("M")) && e.childPath().equals(path("M", "Child")))
              .count();
      assertEquals(1, edges, "duplicate hierarchical rows canonicalize to one logical edge");
    }

    /**
     * Overriding a declaration replaces only same-or-subtype references between the same pair (Part
     * 3 §6.3.3.3); distinct inherited relationships — a different hierarchical ReferenceType, a
     * non-hierarchical row — remain in the merged model. Only the singular HasTypeDefinition /
     * HasModellingRule rows are replaced by the winner's own.
     */
    @Test
    void overrideRetainsDistinctInheritedReferences() throws Exception {
      UaObjectTypeNode base = fx.addObjectType("RefBase", NodeIds.BaseObjectType);
      UaObjectNode baseB =
          fx.addObjectDeclaration(
              base, "B", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      baseB.addReference(
          new Reference(
              baseB.getNodeId(), NodeIds.HasCause, NodeIds.ObjectsFolder.expanded(), true));

      UaObjectTypeNode sub = fx.addObjectType("RefSub", base.getNodeId());
      fx.addObjectDeclaration(
          sub, "B", NodeIds.FolderType, NodeIds.ModellingRule_Mandatory, NodeIds.HasNotifier);

      TypeInstantiationModel model = fx.compiler().compile(sub.getNodeId());

      assertTrue(
          model
              .hierarchy()
              .contains(new DeclarationEdge(BrowsePath.root(), NodeIds.HasNotifier, path("B"))),
          "the subtype's own edge stands");
      assertTrue(
          model
              .hierarchy()
              .contains(new DeclarationEdge(BrowsePath.root(), NodeIds.HasComponent, path("B"))),
          "the inherited HasComponent edge is a distinct relationship and remains");

      assertTrue(
          model
              .references()
              .contains(
                  ReferenceRow.absolute(
                      path("B"),
                      NodeIds.HasCause,
                      Reference.Direction.FORWARD,
                      NodeIds.ObjectsFolder.expanded())),
          "inherited non-hierarchical rows remain on the overridden path");

      List<ReferenceRow> typeDefinitionRows =
          model.references().stream()
              .filter(
                  r ->
                      path("B").equals(r.sourcePath())
                          && NodeIds.HasTypeDefinition.equals(r.referenceTypeId()))
              .toList();
      assertEquals(
          List.of(
              ReferenceRow.absolute(
                  path("B"),
                  NodeIds.HasTypeDefinition,
                  Reference.Direction.FORWARD,
                  NodeIds.FolderType.expanded())),
          typeDefinitionRows,
          "the singular HasTypeDefinition is replaced by the winner's");
    }

    /**
     * A supertype's explicit specialization of a member type's child survives into the subtype's
     * model when the subtype re-declares the member without re-declaring the child: the inherited
     * explicit declaration beats the member-type default the subtype's own walk re-grafted, and
     * validation runs in the matching direction (no spurious narrowing warning).
     */
    @Test
    void inheritedExplicitSpecializationBeatsRegraftedMemberTypeDefault() throws Exception {
      UaObjectTypeNode memberType = fx.addObjectType("GraftMemberType", NodeIds.BaseObjectType);
      UaVariableNode typeChild =
          fx.addVariableDeclaration(
              memberType, "B", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      typeChild.setDataType(NodeIds.Number);

      UaObjectTypeNode superType = fx.addObjectType("GraftSuperType", NodeIds.BaseObjectType);
      UaObjectNode d =
          fx.addObjectDeclaration(
              superType, "D", memberType.getNodeId(), NodeIds.ModellingRule_Mandatory);
      UaVariableNode specialized =
          fx.addVariableDeclaration(
              d, "B", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      specialized.setDataType(NodeIds.Int32);

      UaObjectTypeNode subType = fx.addObjectType("GraftSubType", superType.getNodeId());
      fx.addObjectDeclaration(
          subType, "D", memberType.getNodeId(), NodeIds.ModellingRule_Mandatory);

      TypeInstantiationModel model = fx.compiler().compile(subType.getNodeId());

      InstanceDeclaration child = model.get(path("D", "B")).orElseThrow();
      assertEquals(
          specialized.getNodeId(),
          child.declarationNodeId(),
          "the inherited explicit specialization wins over the re-grafted member-type default");
      assertEquals(NodeIds.Int32, child.attributes().dataType());
      assertTrue(
          child.overriddenDeclarations().contains(typeChild.getNodeId()),
          "the member-type default is recorded as overridden");
      assertTrue(
          model.diagnostics().stream()
              .noneMatch(diag -> diag.code() == ModelDiagnostic.Code.VARIABLE_NARROWING),
          "no narrowing warning: Int32 narrowing Number is validated in the correct direction");
    }

    /**
     * Two inherited references between the same declaration pair (Part 3 §6.4.3) both survive an
     * override of the child by an unrelated ReferenceType: an edge folded from the supertype must
     * not suppress a sibling edge folded by the same merge, whatever their subtype relationship or
     * iteration order.
     */
    @Test
    void siblingInheritedEdgesAtOverriddenPathDoNotSuppressEachOther() throws Exception {
      // Named so the subtype reference sorts (and folds) before its supertype reference.
      UaReferenceTypeNode vSuper = fx.addReferenceType("BWidgetRef", NodeIds.HasComponent);
      UaReferenceTypeNode vSub = fx.addReferenceType("AWidgetRef", vSuper.getNodeId());

      UaObjectTypeNode base = fx.addObjectType("EdgePairBase", NodeIds.BaseObjectType);
      UaObjectNode baseC =
          fx.addObjectDeclaration(
              base, "C", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory, vSub.getNodeId());
      base.addReference(
          new Reference(base.getNodeId(), vSuper.getNodeId(), baseC.getNodeId().expanded(), true));

      UaObjectTypeNode sub = fx.addObjectType("EdgePairSub", base.getNodeId());
      fx.addObjectDeclaration(
          sub, "C", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory, NodeIds.HasNotifier);

      TypeInstantiationModel model = fx.compiler().compile(sub.getNodeId());

      assertTrue(
          model
              .hierarchy()
              .contains(new DeclarationEdge(BrowsePath.root(), NodeIds.HasNotifier, path("C"))),
          "the subtype's own edge stands");
      assertTrue(
          model
              .hierarchy()
              .contains(new DeclarationEdge(BrowsePath.root(), vSub.getNodeId(), path("C"))),
          "the first inherited edge remains");
      assertTrue(
          model
              .hierarchy()
              .contains(new DeclarationEdge(BrowsePath.root(), vSuper.getNodeId(), path("C"))),
          "a sibling inherited edge folded by the same merge must not suppress this one");
    }
  }

  /** Expansion depth, override precedence and provenance, and non-declaration exclusion. */
  @Nested
  class Inheritance {

    /** Nested Mandatory members expand at every depth and edges keep their ReferenceTypes. */
    @Test
    void nestedMandatoryExpansionAtEveryDepthPreservesReferenceTypes() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("DeepType", NodeIds.BaseObjectType);
      UaObjectNode obj =
          fx.addObjectDeclaration(
              typeNode, "Obj", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      UaObjectNode inner =
          fx.addObjectDeclaration(
              obj, "Inner", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          inner,
          "Leaf",
          NodeIds.PropertyType,
          NodeIds.ModellingRule_Mandatory,
          NodeIds.HasProperty);

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      assertTrue(model.get(path("Obj")).isPresent());
      assertTrue(model.get(path("Obj", "Inner")).isPresent());
      assertTrue(model.get(path("Obj", "Inner", "Leaf")).isPresent());

      assertTrue(
          model
              .hierarchy()
              .contains(new DeclarationEdge(BrowsePath.root(), NodeIds.HasComponent, path("Obj"))));
      assertTrue(
          model
              .hierarchy()
              .contains(
                  new DeclarationEdge(path("Obj"), NodeIds.HasComponent, path("Obj", "Inner"))));
      assertTrue(
          model
              .hierarchy()
              .contains(
                  new DeclarationEdge(
                      path("Obj", "Inner"), NodeIds.HasProperty, path("Obj", "Inner", "Leaf"))),
          "HasProperty preserved on the deep edge");
    }

    /**
     * Three-level inheritance with a same-path override at each level: the most-derived entry wins,
     * provenance names the declaring type, and the override chain runs nearest-first to base-most.
     */
    @Test
    void threeLevelInheritanceOverrideProvenanceAndPrecedence() throws Exception {
      UaObjectTypeNode grand = fx.addObjectType("GrandType", NodeIds.BaseObjectType);
      UaVariableNode grandV =
          fx.addVariableDeclaration(
              grand, "V", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);
      fx.addVariableDeclaration(
          grand, "W", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode mid = fx.addObjectType("MidType", grand.getNodeId());
      UaVariableNode midV =
          fx.addVariableDeclaration(
              mid, "V", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode leaf = fx.addObjectType("LeafType", mid.getNodeId());
      UaVariableNode leafV =
          fx.addVariableDeclaration(
              leaf, "V", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      TypeInstantiationModel model = fx.compiler().compile(leaf.getNodeId());

      InstanceDeclaration v = model.get(path("V")).orElseThrow();
      assertEquals(leafV.getNodeId(), v.declarationNodeId());
      assertEquals(leaf.getNodeId(), v.declaringTypeId());
      assertEquals(ModellingRule.MANDATORY, v.rule());
      assertEquals(
          List.of(midV.getNodeId(), grandV.getNodeId()),
          v.overriddenDeclarations(),
          "override chain nearest first, base-most last");

      InstanceDeclaration w = model.get(path("W")).orElseThrow();
      assertEquals(grand.getNodeId(), w.declaringTypeId(), "un-overridden member keeps provenance");
      assertTrue(w.overriddenDeclarations().isEmpty());

      assertTrue(
          model.diagnostics().stream()
              .noneMatch(d -> d.code() == ModelDiagnostic.Code.MODELLING_RULE_TIGHTENING),
          "Optional -> Mandatory and Mandatory -> Mandatory are valid tightenings");
    }

    /** A rule-less node is excluded with a traceable diagnostic, never silently (Part 3 §6.2.2). */
    @Test
    void ruleLessNodeExcludedWithTraceableDiagnostic() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("RuleLessType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "Configured", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      UaVariableNode plain =
          fx.addRuleLessVariable(typeNode, "Plain", NodeIds.BaseDataVariableType);

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      assertTrue(model.get(path("Configured")).isPresent());
      assertTrue(model.get(path("Plain")).isEmpty(), "rule-less node is not a declaration");

      assertTrue(
          model.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.NOT_AN_INSTANCE_DECLARATION
                          && d.severity() == ModelDiagnostic.Severity.INFO
                          && path("Plain").equals(d.browsePath())
                          && plain.getNodeId().equals(d.nodeId())),
          "exclusion is traceable to the path and node");
    }
  }

  /** Malformed input rejects with diagnostics and no partial model. */
  @Nested
  class ErrorCases {

    @Test
    void unknownTypeRejected() {
      NodeId bogus = fx.newNodeId("DoesNotExist");

      ModelCompilationException e =
          assertThrows(ModelCompilationException.class, () -> fx.compiler().compile(bogus));

      assertEquals(bogus, e.getTypeDefinitionId());
      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(d -> d.isError() && d.code() == ModelDiagnostic.Code.TYPE_NOT_FOUND));
    }

    @Test
    void wrongNodeClassRejected() {
      UaObjectTypeNode typeNode = fx.addObjectType("HostType", NodeIds.BaseObjectType);
      UaVariableNode notAType =
          fx.addVariableDeclaration(
              typeNode, "V", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(notAType.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(
                  d -> d.isError() && d.code() == ModelDiagnostic.Code.TYPE_NODE_CLASS_INVALID));
    }

    /** A member whose TypeDefinition targets a remote (unresolvable) node rejects the compile. */
    @Test
    void remoteMemberTypeRejected() {
      UaObjectTypeNode typeNode = fx.addObjectType("RemoteMemberType", NodeIds.BaseObjectType);

      NodeId declId = fx.newNodeId("RemoteMemberType.R");
      UaObjectNode declaration =
          new UaObjectNode(
              fx.context(),
              declId,
              qn("R"),
              LocalizedText.english("R"),
              LocalizedText.NULL_VALUE,
              UInteger.MIN,
              UInteger.MIN,
              ubyte(0));
      declaration.addReference(
          new Reference(
              declId,
              NodeIds.HasTypeDefinition,
              ExpandedNodeId.of("urn:remote:server:namespace", "RemoteType"),
              true));
      declaration.addReference(
          new Reference(
              declId, NodeIds.HasModellingRule, NodeIds.ModellingRule_Mandatory.expanded(), true));
      fx.nodeManager().addNode(declaration);
      typeNode.addReference(
          new Reference(typeNode.getNodeId(), NodeIds.HasComponent, declId.expanded(), true));

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(typeNode.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(
                  d ->
                      d.isError()
                          && d.code() == ModelDiagnostic.Code.TYPE_NOT_FOUND
                          && path("R").equals(d.browsePath())));
    }

    /**
     * A forward hierarchical reference to a missing node rejects the compile — never a silently
     * truncated hierarchy.
     */
    @Test
    void missingHierarchicalTargetRejected() {
      UaObjectTypeNode typeNode = fx.addObjectType("MissingChildType", NodeIds.BaseObjectType);
      typeNode.addReference(
          new Reference(
              typeNode.getNodeId(),
              NodeIds.HasComponent,
              fx.newNodeId("MissingChildType.Gone").expanded(),
              true));

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(typeNode.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(
                  d -> d.isError() && d.code() == ModelDiagnostic.Code.REFERENCE_TARGET_MISSING));
    }

    /** An unresolvable (remote) supertype rejects — it must not demote the type to a root. */
    @Test
    void unresolvableSupertypeRejected() {
      NodeId remoteId = fx.newNodeId("RemoteSupertypeType");
      UaObjectTypeNode remote =
          new UaObjectTypeNode(
              fx.context(),
              remoteId,
              qn("RemoteSupertypeType"),
              LocalizedText.english("RemoteSupertypeType"),
              LocalizedText.NULL_VALUE,
              UInteger.MIN,
              UInteger.MIN,
              false);
      remote.addReference(
          new Reference(
              remoteId,
              NodeIds.HasSubtype,
              ExpandedNodeId.of("urn:remote:server:namespace", "SuperType"),
              false));
      fx.nodeManager().addNode(remote);

      ModelCompilationException e =
          assertThrows(ModelCompilationException.class, () -> fx.compiler().compile(remoteId));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(d -> d.isError() && d.code() == ModelDiagnostic.Code.TYPE_NOT_FOUND));
    }

    /** Multiple supertypes have no defined merge order (single inheritance): reject. */
    @Test
    void multipleSupertypesRejected() {
      UaObjectTypeNode multi = fx.addObjectType("MultiParentType", NodeIds.BaseObjectType);
      multi.addReference(
          new Reference(
              multi.getNodeId(), NodeIds.HasSubtype, NodeIds.FolderType.expanded(), false));

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(multi.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(d -> d.isError() && d.code() == ModelDiagnostic.Code.MULTIPLE_SUPERTYPES));
    }

    /** Cross-family ancestry — an ObjectType inheriting from a VariableType — rejects. */
    @Test
    void crossFamilySupertypeRejected() {
      UaObjectTypeNode cross = fx.addObjectType("CrossFamilyType", NodeIds.BaseDataVariableType);

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(cross.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(d -> d.isError() && d.code() == ModelDiagnostic.Code.NODE_CLASS_MISMATCH));
    }

    /** Objects and Variables require exactly one TypeDefinition; several reject the compile. */
    @Test
    void multipleTypeDefinitionsRejected() {
      UaObjectTypeNode typeNode = fx.addObjectType("TwoTypeDefsType", NodeIds.BaseObjectType);
      UaObjectNode declaration =
          fx.addObjectDeclaration(
              typeNode, "D", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      declaration.addReference(
          new Reference(
              declaration.getNodeId(),
              NodeIds.HasTypeDefinition,
              NodeIds.FolderType.expanded(),
              true));

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(typeNode.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(
                  d ->
                      d.isError()
                          && d.code() == ModelDiagnostic.Code.MULTIPLE_TYPE_DEFINITIONS
                          && path("D").equals(d.browsePath())));
    }

    /**
     * An override changing the NodeClass is one no instance can satisfy: reject the compile instead
     * of escaping as a usable warning (Part 3 §6.3.3.3).
     */
    @Test
    void nodeClassChangingOverrideRejected() {
      UaObjectTypeNode classBase = fx.addObjectType("ClassBase", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          classBase, "V", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      UaObjectTypeNode classSub = fx.addObjectType("ClassSub", classBase.getNodeId());
      fx.addObjectDeclaration(
          classSub, "V", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(classSub.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(
                  d ->
                      d.isError()
                          && d.code() == ModelDiagnostic.Code.NODE_CLASS_MISMATCH
                          && path("V").equals(d.browsePath())));
    }

    /**
     * An override replacing the TypeDefinition with an unrelated (non-subtype) type is one no
     * instance can satisfy: reject the compile (Part 3 §6.3.3.3).
     */
    @Test
    void unrelatedTypeDefinitionOverrideRejected() {
      UaObjectTypeNode unrelated = fx.addObjectType("UnrelatedType", NodeIds.BaseObjectType);
      UaObjectTypeNode typeBase = fx.addObjectType("TypeDefBase", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(typeBase, "M", NodeIds.FolderType, NodeIds.ModellingRule_Mandatory);
      UaObjectTypeNode typeSub = fx.addObjectType("TypeDefSub", typeBase.getNodeId());
      fx.addObjectDeclaration(typeSub, "M", unrelated.getNodeId(), NodeIds.ModellingRule_Mandatory);

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(typeSub.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(
                  d ->
                      d.isError()
                          && d.code() == ModelDiagnostic.Code.TYPE_INCOMPATIBLE
                          && path("M").equals(d.browsePath())));
    }

    /**
     * An abstract member type compiles with a classifying warning: the model must stay usable
     * (every supertype chain ends at abstract base types), and requiring a concrete-subtype
     * resolution is a plan-time obligation the warning marks.
     */
    @Test
    void abstractMemberTypeWarnsButModelIsUsable() throws Exception {
      UaObjectTypeNode abstractType =
          fx.addObjectType("AbstractMember", NodeIds.BaseObjectType, true);
      fx.addVariableDeclaration(
          abstractType, "Inner", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode typeNode = fx.addObjectType("AbstractHostType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          typeNode, "A", abstractType.getNodeId(), NodeIds.ModellingRule_Mandatory);

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      assertTrue(
          model.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.ABSTRACT_MEMBER_TYPE
                          && d.severity() == ModelDiagnostic.Severity.WARNING
                          && path("A").equals(d.browsePath())));

      assertTrue(model.get(path("A")).isPresent());
      assertTrue(model.get(path("A", "Inner")).isPresent(), "expansion is not truncated");
    }
  }

  /** Snapshot fidelity, sourcing, and isolation. */
  @Nested
  class AttributeSnapshots {

    /**
     * The full 1.04+ attribute set is snapshotted from the declaration: the declaration's Value
     * constituents verbatim (serverTime normalized away), MinimumSamplingInterval and Historizing
     * carried (legacy dropped them), security attributes from the declaration (a deliberate fix;
     * legacy took them from the type node), unset optionals absent.
     */
    @Test
    void attributeSnapshotFidelity() throws Exception {
      NodeId customVarTypeId = fx.newNodeId("AttrVarType");
      UaVariableTypeNode customVarType =
          new UaVariableTypeNode(
              fx.context(),
              customVarTypeId,
              qn("AttrVarType"),
              LocalizedText.english("AttrVarType"),
              LocalizedText.NULL_VALUE,
              UInteger.MIN,
              UInteger.MIN,
              null,
              NodeIds.BaseDataType,
              -1,
              null,
              false);
      customVarType.addReference(
          new Reference(
              customVarTypeId, NodeIds.HasSubtype, NodeIds.BaseDataVariableType.expanded(), false));
      fx.nodeManager().addNode(customVarType);

      UaObjectTypeNode typeNode = fx.addObjectType("AttrType", NodeIds.BaseObjectType);

      DataValue declarationValue = new DataValue(new Variant(42));
      RolePermissionType[] declarationRolePermissions = {
        new RolePermissionType(NodeIds.WellKnownRole_Anonymous, new PermissionType(uint(1)))
      };

      NodeId declId = fx.newNodeId("AttrType.V");
      UaVariableNode declaration =
          new UaVariableNode(
              fx.context(),
              declId,
              qn("V"),
              LocalizedText.english("V"),
              LocalizedText.NULL_VALUE,
              UInteger.MIN,
              UInteger.MIN,
              declarationRolePermissions,
              declarationRolePermissions,
              new AccessRestrictionType(UShort.valueOf(2)),
              declarationValue,
              NodeIds.Int32,
              -1,
              null,
              AccessLevel.toValue(AccessLevel.READ_WRITE),
              AccessLevel.toValue(AccessLevel.READ_WRITE),
              100.0,
              true,
              null);
      declaration.addReference(
          new Reference(declId, NodeIds.HasTypeDefinition, customVarTypeId.expanded(), true));
      declaration.addReference(
          new Reference(
              declId, NodeIds.HasModellingRule, NodeIds.ModellingRule_Mandatory.expanded(), true));
      fx.nodeManager().addNode(declaration);
      typeNode.addReference(
          new Reference(typeNode.getNodeId(), NodeIds.HasComponent, declId.expanded(), true));

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());
      AttributeSnapshot attributes = model.get(path("V")).orElseThrow().attributes();

      // Value: source-side constituents verbatim, serverTime normalized away.
      DataValue value = attributes.value();
      assertNotNull(value);
      assertSame(declarationValue.getValue(), value.getValue());
      assertSame(declarationValue.getStatusCode(), value.getStatusCode());
      assertSame(declarationValue.getSourceTime(), value.getSourceTime());
      assertNull(value.getServerTime(), "serverTime normalized for snapshot stability");

      assertEquals(NodeIds.Int32, attributes.dataType());
      assertEquals(Integer.valueOf(-1), attributes.valueRank());
      assertEquals(
          AccessLevel.toValue(AccessLevel.READ_WRITE),
          attributes.getOrNull(AttributeId.AccessLevel));
      assertEquals(
          AccessLevel.toValue(AccessLevel.READ_WRITE),
          attributes.getOrNull(AttributeId.UserAccessLevel));

      // Carried where legacy dropped them:
      assertEquals(100.0, attributes.getOrNull(AttributeId.MinimumSamplingInterval));
      assertEquals(true, attributes.getOrNull(AttributeId.Historizing));

      // Security attributes come from the declaration (legacy read the type node),
      // defensively copied per the immutable-snapshot contract:
      RolePermissionType[] snapshotRolePermissions =
          (RolePermissionType[]) attributes.getOrNull(AttributeId.RolePermissions);
      assertArrayEquals(declarationRolePermissions, snapshotRolePermissions);
      assertNotSame(declarationRolePermissions, snapshotRolePermissions);
      assertEquals(
          new AccessRestrictionType(UShort.valueOf(2)),
          attributes.getOrNull(AttributeId.AccessRestrictions));

      // Unset optional attributes are absent, not null:
      assertFalse(attributes.contains(AttributeId.ArrayDimensions));
      assertFalse(attributes.contains(AttributeId.AccessLevelEx));
    }

    /**
     * Root attributes are snapshotted from the type node itself (Part 3 §6.4.2 source), with unset
     * optional attributes absent — a VariableType with no Value yields a root snapshot without one.
     */
    @Test
    void rootAttributesSnapshotFromTypeNode() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("RootAttrType", NodeIds.BaseObjectType);

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      assertEquals(
          typeNode.getBrowseName(), model.rootAttributes().getOrNull(AttributeId.BrowseName));
      assertEquals(NodeClass.ObjectType, model.rootAttributes().getOrNull(AttributeId.NodeClass));
      assertEquals(false, model.rootAttributes().getOrNull(AttributeId.IsAbstract));

      UaVariableTypeNode noValueVarType =
          fx.addVariableType(
              "NoValueVarType", NodeIds.BaseDataVariableType, null, NodeIds.BaseDataType, -1);

      TypeInstantiationModel varTypeModel = fx.compiler().compile(noValueVarType.getNodeId());
      assertFalse(
          varTypeModel.rootAttributes().contains(AttributeId.Value),
          "a VariableType without a Value yields a root snapshot with the attribute absent");
    }

    /**
     * The explicit-null attribute state is representable and distinct from absent, so a consumer
     * can tell "clear this attribute" apart from "not specified".
     */
    @Test
    void attributeSnapshotDistinguishesExplicitNullFromAbsent() {
      AttributeSnapshot withNull =
          AttributeSnapshot.builder().put(AttributeId.Description, null).build();

      assertTrue(withNull.contains(AttributeId.Description));
      assertTrue(withNull.isNull(AttributeId.Description));
      assertTrue(withNull.get(AttributeId.Description).isEmpty());
      assertFalse(withNull.contains(AttributeId.DisplayName));
    }

    /**
     * The snapshot is deeply isolated: mutating node-owned arrays after compilation, or arrays read
     * back from the snapshot, never changes the snapshot — a prerequisite for caching compiled
     * models.
     */
    @Test
    void attributeSnapshotDefensiveCopies() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("CopyType", NodeIds.BaseObjectType);
      UaVariableNode declaration =
          fx.addVariableDeclaration(
              typeNode, "V", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      Integer[] valueArray = {1, 2, 3};
      UInteger[] dimensions = {uint(3)};
      RolePermissionType[] rolePermissions = {
        new RolePermissionType(NodeIds.WellKnownRole_Anonymous, new PermissionType(uint(1)))
      };
      declaration.setValue(new DataValue(new Variant(valueArray)));
      declaration.setDataType(NodeIds.Int32);
      declaration.setArrayDimensions(dimensions);
      declaration.setRolePermissions(rolePermissions);

      AttributeSnapshot attributes =
          fx.compiler().compile(typeNode.getNodeId()).get(path("V")).orElseThrow().attributes();

      // Ingress isolation: mutating the node-owned values does not change the snapshot.
      valueArray[0] = 99;
      dimensions[0] = uint(9);
      rolePermissions[0] =
          new RolePermissionType(
              NodeIds.WellKnownRole_AuthenticatedUser, new PermissionType(uint(2)));

      assertEquals(1, snapshotValueArray(attributes)[0]);
      assertEquals(uint(3), snapshotArrayDimensions(attributes)[0]);
      RolePermissionType[] snapshotRolePermissions =
          (RolePermissionType[]) attributes.getOrNull(AttributeId.RolePermissions);
      assertNotNull(snapshotRolePermissions);
      assertEquals(NodeIds.WellKnownRole_Anonymous, snapshotRolePermissions[0].getRoleId());

      // Egress isolation: mutating a value read from the snapshot does not change the snapshot.
      snapshotArrayDimensions(attributes)[0] = uint(7);
      assertEquals(uint(3), snapshotArrayDimensions(attributes)[0]);
      snapshotValueArray(attributes)[0] = 42;
      assertEquals(1, snapshotValueArray(attributes)[0]);
    }

    /** Reads the snapshot's Value attribute as an {@code Integer[]}, asserting it is present. */
    private static Integer[] snapshotValueArray(AttributeSnapshot attributes) {
      DataValue value = attributes.value();
      assertNotNull(value);
      Integer[] array = (Integer[]) value.getValue().getValue();
      assertNotNull(array);
      return array;
    }

    /** Reads the snapshot's ArrayDimensions attribute, asserting it is present. */
    private static UInteger[] snapshotArrayDimensions(AttributeSnapshot attributes) {
      UInteger[] dimensions = attributes.arrayDimensions();
      assertNotNull(dimensions);
      return dimensions;
    }
  }

  /** Variable narrowing and Method signature compatibility across overrides. */
  @Nested
  class SignatureValidation {

    @Test
    void variableNarrowingViolationDiagnosed() throws Exception {
      UaObjectTypeNode base = fx.addObjectType("NarrowBase", NodeIds.BaseObjectType);
      UaVariableNode baseV =
          fx.addVariableDeclaration(
              base, "V", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      baseV.setDataType(NodeIds.Int32);

      UaObjectTypeNode sub = fx.addObjectType("NarrowSub", base.getNodeId());
      UaVariableNode subV =
          fx.addVariableDeclaration(
              sub, "V", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      subV.setDataType(NodeIds.String);

      TypeInstantiationModel model = fx.compiler().compile(sub.getNodeId());

      assertTrue(
          model.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.VARIABLE_NARROWING
                          && d.severity() == ModelDiagnostic.Severity.WARNING
                          && path("V").equals(d.browsePath())),
          "String does not narrow Int32 (Part 3 §6.2.8)");

      assertEquals(
          NodeIds.String,
          model.get(path("V")).orElseThrow().attributes().dataType(),
          "the override still stands, classified");
    }

    @Test
    void methodSignatureMismatchDiagnosed() throws Exception {
      UaObjectTypeNode base = fx.addObjectType("SigBase", NodeIds.BaseObjectType);
      UaMethodNode baseMethod =
          fx.addMethodDeclaration(base, "Op", NodeIds.ModellingRule_Mandatory);
      fx.addArgumentsProperty(
          baseMethod, "InputArguments", new Argument[] {argument("A"), argument("B")});

      UaObjectTypeNode sub = fx.addObjectType("SigSub", base.getNodeId());
      UaMethodNode subMethod = fx.addMethodDeclaration(sub, "Op", NodeIds.ModellingRule_Mandatory);
      fx.addArgumentsProperty(subMethod, "InputArguments", new Argument[] {argument("A")});

      TypeInstantiationModel model = fx.compiler().compile(sub.getNodeId());

      assertTrue(
          model.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.METHOD_SIGNATURE_MISMATCH
                          && path("Op").equals(d.browsePath())),
          "removing arguments violates Part 3 §6.3.3.3");
    }

    /**
     * Method overrides validate argument content, not only count and name: a concrete DataType
     * shall not change, an abstract DataType may be specialized to a subtype, and the argument
     * property cannot disappear (Part 3 §6.3.3.3).
     */
    @Test
    void methodArgumentCompatibilityValidated() throws Exception {
      UaObjectTypeNode base = fx.addObjectType("ArgBase", NodeIds.BaseObjectType);
      UaMethodNode baseTyped =
          fx.addMethodDeclaration(base, "Typed", NodeIds.ModellingRule_Mandatory);
      fx.addArgumentsProperty(
          baseTyped,
          "InputArguments",
          new Argument[] {new Argument("A", NodeIds.Int32, -1, null, LocalizedText.english("A"))});
      UaMethodNode baseGone =
          fx.addMethodDeclaration(base, "Gone", NodeIds.ModellingRule_Mandatory);
      fx.addArgumentsProperty(baseGone, "InputArguments", new Argument[] {argument("A")});
      UaMethodNode baseSpec =
          fx.addMethodDeclaration(base, "Spec", NodeIds.ModellingRule_Mandatory);
      fx.addArgumentsProperty(
          baseSpec,
          "InputArguments",
          new Argument[] {new Argument("A", NodeIds.Number, -1, null, LocalizedText.english("A"))});

      UaObjectTypeNode sub = fx.addObjectType("ArgSub", base.getNodeId());
      UaMethodNode subTyped =
          fx.addMethodDeclaration(sub, "Typed", NodeIds.ModellingRule_Mandatory);
      fx.addArgumentsProperty(
          subTyped,
          "InputArguments",
          new Argument[] {new Argument("A", NodeIds.String, -1, null, LocalizedText.english("A"))});
      fx.addMethodDeclaration(sub, "Gone", NodeIds.ModellingRule_Mandatory);
      UaMethodNode subSpec = fx.addMethodDeclaration(sub, "Spec", NodeIds.ModellingRule_Mandatory);
      fx.addArgumentsProperty(
          subSpec,
          "InputArguments",
          new Argument[] {new Argument("A", NodeIds.Int32, -1, null, LocalizedText.english("A"))});

      TypeInstantiationModel model = fx.compiler().compile(sub.getNodeId());

      List<ModelDiagnostic> mismatches =
          model.diagnostics().stream()
              .filter(d -> d.code() == ModelDiagnostic.Code.METHOD_SIGNATURE_MISMATCH)
              .toList();

      assertTrue(
          mismatches.stream()
              .anyMatch(
                  d -> path("Typed").equals(d.browsePath()) && d.message().contains("DataType")),
          "changing a concrete argument DataType is diagnosed");
      assertTrue(
          mismatches.stream()
              .anyMatch(
                  d -> path("Gone").equals(d.browsePath()) && d.message().contains("removes")),
          "removing the whole argument property is diagnosed");
      assertTrue(
          mismatches.stream().noneMatch(d -> path("Spec").equals(d.browsePath())),
          "specializing an abstract argument DataType to a subtype is permitted");
    }

    private static Argument argument(String name) {
      return new Argument(name, NodeIds.String, -1, null, LocalizedText.english(name));
    }
  }

  /** Occurrences, duplicate BrowseNames, placeholders, and vendor modelling rules. */
  @Nested
  class DeclarationClassification {

    /**
     * Multiple BrowsePaths reaching one declaration node are distinct occurrences (Part 3
     * §6.3.3.2).
     */
    @Test
    void multiplePathsToOneDeclarationAreDistinctOccurrences() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("MultiPathType", NodeIds.BaseObjectType);
      UaObjectNode a =
          fx.addObjectDeclaration(
              typeNode, "A", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      UaObjectNode b =
          fx.addObjectDeclaration(
              typeNode, "B", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      UaVariableNode n =
          fx.addVariableDeclaration(
              a, "N", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      b.addReference(
          new Reference(b.getNodeId(), NodeIds.HasComponent, n.getNodeId().expanded(), true));

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      InstanceDeclaration underA = model.get(path("A", "N")).orElseThrow();
      InstanceDeclaration underB = model.get(path("B", "N")).orElseThrow();
      assertEquals(n.getNodeId(), underA.declarationNodeId());
      assertEquals(n.getNodeId(), underB.declarationNodeId());
      assertNotEquals(underA.browsePath(), underB.browsePath());
    }

    /** Duplicate sibling BrowseNames reject the compile with a precise diagnostic. */
    @Test
    void duplicateSiblingBrowseNamesRejectedPrecisely() {
      UaObjectTypeNode typeNode = fx.addObjectType("DupType", NodeIds.BaseObjectType);
      UaVariableNode first =
          fx.addVariableDeclaration(
              typeNode, "X", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      NodeId secondId = fx.newNodeId("DupType.X2");
      UaVariableNode second =
          new UaVariableNode(
              fx.context(),
              secondId,
              qn("X"),
              LocalizedText.english("X"),
              LocalizedText.NULL_VALUE,
              UInteger.MIN,
              UInteger.MIN);
      second.addReference(
          new Reference(
              secondId, NodeIds.HasTypeDefinition, NodeIds.BaseDataVariableType.expanded(), true));
      second.addReference(
          new Reference(
              secondId,
              NodeIds.HasModellingRule,
              NodeIds.ModellingRule_Mandatory.expanded(),
              true));
      fx.nodeManager().addNode(second);
      typeNode.addReference(
          new Reference(typeNode.getNodeId(), NodeIds.HasComponent, secondId.expanded(), true));

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(typeNode.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(
                  d ->
                      d.isError()
                          && d.code() == ModelDiagnostic.Code.DUPLICATE_BROWSE_NAME
                          && path("X").equals(d.browsePath())
                          && secondId.equals(d.nodeId())),
          "the diagnostic pinpoints the colliding path and sibling; first kept: "
              + first.getNodeId());
    }

    /**
     * Sibling BrowseName uniqueness (Part 3 §4.6.4) covers rule-less children too, so validity is
     * not NodeId-order dependent: a rule-less X walked before a Mandatory X still collides.
     */
    @Test
    void ruleLessSiblingCollisionIsOrderIndependent() {
      UaObjectTypeNode typeNode = fx.addObjectType("RuleLessDupType", NodeIds.BaseObjectType);
      fx.addRuleLessVariable(typeNode, "X", NodeIds.BaseDataVariableType);

      NodeId declaredId = fx.newNodeId("RuleLessDupType.X2");
      UaVariableNode declared =
          new UaVariableNode(
              fx.context(),
              declaredId,
              qn("X"),
              LocalizedText.english("X"),
              LocalizedText.NULL_VALUE,
              UInteger.MIN,
              UInteger.MIN);
      declared.addReference(
          new Reference(
              declaredId,
              NodeIds.HasTypeDefinition,
              NodeIds.BaseDataVariableType.expanded(),
              true));
      declared.addReference(
          new Reference(
              declaredId,
              NodeIds.HasModellingRule,
              NodeIds.ModellingRule_Mandatory.expanded(),
              true));
      fx.nodeManager().addNode(declared);
      typeNode.addReference(
          new Reference(typeNode.getNodeId(), NodeIds.HasComponent, declaredId.expanded(), true));

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(typeNode.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(
                  d ->
                      d.isError()
                          && d.code() == ModelDiagnostic.Code.DUPLICATE_BROWSE_NAME
                          && path("X").equals(d.browsePath())));
    }

    /**
     * Placeholders are surfaced as extension points and recorded shallowly: neither their type's
     * members nor their on-declaration children enter the model (Part 3 §6.4.4.4.4–.5).
     */
    @Test
    void placeholdersSurfacedShallowly() throws Exception {
      UaObjectTypeNode phMemberType = fx.addObjectType("PHMemberType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          phMemberType, "Inner", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode typeNode = fx.addObjectType("PHType", NodeIds.BaseObjectType);
      UaObjectNode optionalPh =
          fx.addObjectDeclaration(
              typeNode,
              "<Element>",
              phMemberType.getNodeId(),
              NodeIds.ModellingRule_OptionalPlaceholder);
      fx.addVariableDeclaration(
          optionalPh, "DeclChild", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addObjectDeclaration(
          typeNode,
          "<Required>",
          phMemberType.getNodeId(),
          NodeIds.ModellingRule_MandatoryPlaceholder);

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      assertEquals(
          List.of(path("<Element>"), path("<Required>")),
          model.placeholders().stream().map(InstanceDeclaration::browsePath).toList());
      assertEquals(
          ModellingRule.OPTIONAL_PLACEHOLDER, model.get(path("<Element>")).orElseThrow().rule());
      assertEquals(
          ModellingRule.MANDATORY_PLACEHOLDER, model.get(path("<Required>")).orElseThrow().rule());
      assertEquals(
          phMemberType.getNodeId(),
          model.get(path("<Element>")).orElseThrow().typeDefinitionId(),
          "the placeholder's type is visible for later placeholder-driven creation");

      assertTrue(model.get(path("<Element>", "Inner")).isEmpty(), "type members not expanded");
      assertTrue(model.get(path("<Element>", "DeclChild")).isEmpty(), "subtree not considered");
      assertEquals(2, model.declarations().size());
    }

    /** Shallow recording skips subtree expansion, not validation of the recorded type metadata. */
    @Test
    void shallowDeclarationTypeClassValidated() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("BadPlaceholderType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          typeNode, "<P>", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_OptionalPlaceholder);

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      assertTrue(
          model.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.NODE_CLASS_MISMATCH
                          && d.severity() == ModelDiagnostic.Severity.WARNING
                          && path("<P>").equals(d.browsePath())),
          "an Object placeholder typed by a VariableType is diagnosed");
      assertTrue(model.get(path("<P>")).isPresent(), "the placeholder itself stays surfaced");
    }

    /** ExposesItsArray is classified and surfaced but not expanded: model visibility only. */
    @Test
    void exposesItsArraySurfacedNotExpanded() throws Exception {
      UaObjectTypeNode elementType = fx.addObjectType("ElementType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          elementType, "Inner", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode typeNode = fx.addObjectType("ArrayType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          typeNode, "Element", elementType.getNodeId(), NodeIds.ModellingRule_ExposesItsArray);

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      InstanceDeclaration element = model.get(path("Element")).orElseThrow();
      assertEquals(ModellingRule.EXPOSES_ITS_ARRAY, element.rule());
      assertFalse(element.isPlaceholder());
      assertTrue(model.placeholders().isEmpty());
      assertTrue(model.get(path("Element", "Inner")).isEmpty(), "not expanded");
    }

    /** Vendor rules classify as OTHER with the raw id preserved and the walk continuing. */
    @Test
    void vendorRuleClassifiedOtherWithRawIdAndWalkContinues() throws Exception {
      NodeId vendorRuleId = fx.newNodeId("VendorRule");

      UaObjectTypeNode typeNode = fx.addObjectType("VendorType", NodeIds.BaseObjectType);
      UaObjectNode member =
          fx.addObjectDeclaration(typeNode, "V", NodeIds.BaseObjectType, vendorRuleId);
      fx.addVariableDeclaration(
          member, "Child", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      InstanceDeclaration v = model.get(path("V")).orElseThrow();
      assertEquals(ModellingRule.OTHER, v.rule());
      assertEquals(vendorRuleId, v.modellingRuleId(), "raw rule id preserved");

      assertTrue(
          model.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.VENDOR_MODELLING_RULE
                          && d.severity() == ModelDiagnostic.Severity.WARNING));

      assertTrue(
          model.get(path("V", "Child")).isPresent(),
          "classification, not filtering: the walk continues beneath a vendor rule");
    }

    /** A custom hierarchical ReferenceType subtype extends the hierarchy like its ancestors. */
    @Test
    void customReferenceTypeSubtypeExtendsHierarchy() throws Exception {
      UaReferenceTypeNode hasWidget = fx.addReferenceType("HasWidget", NodeIds.HasComponent);

      UaObjectTypeNode typeNode = fx.addObjectType("WidgetType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode,
          "W",
          NodeIds.BaseDataVariableType,
          NodeIds.ModellingRule_Mandatory,
          hasWidget.getNodeId());

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      assertTrue(model.get(path("W")).isPresent());
      assertTrue(
          model
              .hierarchy()
              .contains(new DeclarationEdge(BrowsePath.root(), hasWidget.getNodeId(), path("W"))),
          "the edge keeps the custom ReferenceType");
    }
  }

  /** Reference rows: relative vs absolute, canonicalization, occurrence resolution. */
  @Nested
  class ReferenceRows {

    /**
     * A non-hierarchical reference between two declarations becomes exactly one relative row,
     * canonicalized forward (its auto-added inverse is the same logical reference); rows to nodes
     * outside the model — HasTypeDefinition, HasModellingRule — stay absolute with direction.
     */
    @Test
    void declarationInternalRowsRelativeExternalRowsAbsolute() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("RowType", NodeIds.BaseObjectType);
      UaObjectNode foo =
          fx.addObjectDeclaration(
              typeNode, "Foo", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      UaVariableNode bar =
          fx.addVariableDeclaration(
              typeNode, "Bar", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      foo.addReference(
          new Reference(foo.getNodeId(), NodeIds.HasCause, bar.getNodeId().expanded(), true));

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      List<ReferenceRow> causeRows =
          model.references().stream()
              .filter(r -> NodeIds.HasCause.equals(r.referenceTypeId()))
              .toList();
      assertEquals(
          List.of(ReferenceRow.relative(path("Foo"), NodeIds.HasCause, path("Bar"))),
          causeRows,
          "one relative forward row per logical reference (both stored directions canonicalized)");

      assertTrue(
          model
              .references()
              .contains(
                  ReferenceRow.absolute(
                      path("Foo"),
                      NodeIds.HasTypeDefinition,
                      Reference.Direction.FORWARD,
                      NodeIds.BaseObjectType.expanded())),
          "HasTypeDefinition stays absolute");
      assertTrue(
          model
              .references()
              .contains(
                  ReferenceRow.absolute(
                      path("Foo"),
                      NodeIds.HasModellingRule,
                      Reference.Direction.FORWARD,
                      NodeIds.ModellingRule_Mandatory.expanded())),
          "HasModellingRule is recorded (retention is a purpose-dependent planning decision)");
    }

    /**
     * A multi-occurrence declaration's internal references resolve through occurrence context: each
     * occurrence's self-reference maps to its own occurrence rather than freezing absolute.
     */
    @Test
    void multiOccurrenceSelfReferenceResolvesPerOccurrence() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("SelfRefType", NodeIds.BaseObjectType);
      UaObjectNode a =
          fx.addObjectDeclaration(
              typeNode, "A", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      UaObjectNode b =
          fx.addObjectDeclaration(
              typeNode, "B", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      UaVariableNode n =
          fx.addVariableDeclaration(
              a, "N", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      b.addReference(
          new Reference(b.getNodeId(), NodeIds.HasComponent, n.getNodeId().expanded(), true));
      n.addReference(
          new Reference(n.getNodeId(), NodeIds.HasCause, n.getNodeId().expanded(), true));

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      List<ReferenceRow> causeRows =
          model.references().stream()
              .filter(r -> NodeIds.HasCause.equals(r.referenceTypeId()))
              .toList();
      assertEquals(
          List.of(
              ReferenceRow.relative(path("A", "N"), NodeIds.HasCause, path("A", "N")),
              ReferenceRow.relative(path("B", "N"), NodeIds.HasCause, path("B", "N"))),
          causeRows,
          "each occurrence's self-reference stays relative to its own occurrence");
      assertTrue(
          model.diagnostics().stream()
              .noneMatch(d -> d.code() == ModelDiagnostic.Code.AMBIGUOUS_REFERENCE_TARGET));
    }
  }

  /**
   * Modelling rule classification, tightening transitions, rule targets, and
   * DefaultInstanceBrowseName.
   */
  @Nested
  class ModellingRules {

    @Test
    void modellingRuleTighteningValidation() throws Exception {
      UaObjectTypeNode base = fx.addObjectType("TightBase", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          base, "O", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);
      fx.addVariableDeclaration(
          base, "M", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addObjectDeclaration(
          base, "P", NodeIds.BaseObjectType, NodeIds.ModellingRule_OptionalPlaceholder);

      UaObjectTypeNode sub = fx.addObjectType("TightSub", base.getNodeId());
      fx.addVariableDeclaration(
          sub, "O", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          sub, "M", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);
      fx.addObjectDeclaration(
          sub, "P", NodeIds.BaseObjectType, NodeIds.ModellingRule_MandatoryPlaceholder);

      TypeInstantiationModel model = fx.compiler().compile(sub.getNodeId());

      List<ModelDiagnostic> tightening =
          model.diagnostics().stream()
              .filter(d -> d.code() == ModelDiagnostic.Code.MODELLING_RULE_TIGHTENING)
              .toList();

      assertTrue(
          tightening.stream().noneMatch(d -> path("O").equals(d.browsePath())),
          "Optional -> Mandatory is a valid tightening, silent");
      assertTrue(
          tightening.stream()
              .anyMatch(
                  d ->
                      path("M").equals(d.browsePath())
                          && d.severity() == ModelDiagnostic.Severity.WARNING),
          "Mandatory -> Optional violates Table 21: warn, model stays usable");
      assertTrue(
          tightening.stream()
              .anyMatch(d -> path("P").equals(d.browsePath()) && d.message().contains("contested")),
          "OptionalPlaceholder -> MandatoryPlaceholder is a contested transition: warn");
    }

    /**
     * Method placeholder overrides follow §6.4.4.4.4–.5, not the generic Table 21: the rule shall
     * change to Optional/Mandatory (OptionalPlaceholder) or Mandatory (MandatoryPlaceholder), and
     * an unchanged placeholder on an override is diagnosed.
     */
    @Test
    void methodPlaceholderTransitionsUseMethodRules() throws Exception {
      UaObjectTypeNode base = fx.addObjectType("MethodRuleBase", NodeIds.BaseObjectType);
      fx.addMethodDeclaration(base, "Op", NodeIds.ModellingRule_OptionalPlaceholder);
      fx.addMethodDeclaration(base, "Keep", NodeIds.ModellingRule_OptionalPlaceholder);
      fx.addMethodDeclaration(base, "Must", NodeIds.ModellingRule_MandatoryPlaceholder);

      UaObjectTypeNode sub = fx.addObjectType("MethodRuleSub", base.getNodeId());
      fx.addMethodDeclaration(sub, "Op", NodeIds.ModellingRule_Optional);
      fx.addMethodDeclaration(sub, "Keep", NodeIds.ModellingRule_OptionalPlaceholder);
      fx.addMethodDeclaration(sub, "Must", NodeIds.ModellingRule_Mandatory);

      TypeInstantiationModel model = fx.compiler().compile(sub.getNodeId());

      List<ModelDiagnostic> tightening =
          model.diagnostics().stream()
              .filter(d -> d.code() == ModelDiagnostic.Code.MODELLING_RULE_TIGHTENING)
              .toList();

      assertTrue(
          tightening.stream().noneMatch(d -> path("Op").equals(d.browsePath())),
          "OptionalPlaceholder -> Optional is the required Method transition: silent");
      assertTrue(
          tightening.stream().noneMatch(d -> path("Must").equals(d.browsePath())),
          "MandatoryPlaceholder -> Mandatory is the required Method transition: silent");
      assertTrue(
          tightening.stream().anyMatch(d -> path("Keep").equals(d.browsePath())),
          "keeping the placeholder rule on a Method override is diagnosed");
    }

    /**
     * A HasModellingRule target that only resolves remotely rejects the compile: excluding the
     * declaration instead would silently truncate the hierarchy.
     */
    @Test
    void unresolvableModellingRuleTargetRejected() {
      UaObjectTypeNode unresolvableHost =
          fx.addObjectType("UnresolvableRuleType", NodeIds.BaseObjectType);
      NodeId declId = fx.newNodeId("UnresolvableRuleType.D");
      UaObjectNode decl =
          new UaObjectNode(
              fx.context(),
              declId,
              qn("D"),
              LocalizedText.english("D"),
              LocalizedText.NULL_VALUE,
              UInteger.MIN,
              UInteger.MIN,
              ubyte(0));
      decl.addReference(
          new Reference(
              declId, NodeIds.HasTypeDefinition, NodeIds.BaseObjectType.expanded(), true));
      decl.addReference(
          new Reference(
              declId,
              NodeIds.HasModellingRule,
              ExpandedNodeId.of("urn:remote:server:namespace", "Rule"),
              true));
      fx.nodeManager().addNode(decl);
      unresolvableHost.addReference(
          new Reference(
              unresolvableHost.getNodeId(), NodeIds.HasComponent, declId.expanded(), true));

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class,
              () -> fx.compiler().compile(unresolvableHost.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(
                  d ->
                      d.isError()
                          && d.code() == ModelDiagnostic.Code.MODELLING_RULE_INVALID
                          && path("D").equals(d.browsePath())));
    }

    /**
     * A HasModellingRule target node missing locally warns, with the declaration still classified.
     */
    @Test
    void missingModellingRuleNodeWarnsAndStillClassifies() throws Exception {
      UaObjectTypeNode missingHost =
          fx.addObjectType("MissingRuleNodeType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(missingHost, "M", NodeIds.BaseObjectType, fx.newNodeId("NoSuchRule"));

      TypeInstantiationModel missingModel = fx.compiler().compile(missingHost.getNodeId());

      assertTrue(
          missingModel.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.MODELLING_RULE_INVALID
                          && d.severity() == ModelDiagnostic.Severity.WARNING
                          && path("M").equals(d.browsePath())),
          "a missing rule node is diagnosed with the declaration still classified");
      assertEquals(ModellingRule.OTHER, missingModel.get(path("M")).orElseThrow().rule());
    }

    /** A HasModellingRule target of the wrong NodeClass warns with a classifying message. */
    @Test
    void wrongClassModellingRuleTargetWarns() throws Exception {
      UaObjectTypeNode wrongClassHost =
          fx.addObjectType("WrongClassRuleType", NodeIds.BaseObjectType);
      UaVariableNode notARule =
          fx.addRuleLessVariable(wrongClassHost, "NotARule", NodeIds.BaseDataVariableType);
      fx.addObjectDeclaration(wrongClassHost, "W", NodeIds.BaseObjectType, notARule.getNodeId());

      TypeInstantiationModel wrongClassModel = fx.compiler().compile(wrongClassHost.getNodeId());

      assertTrue(
          wrongClassModel.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.MODELLING_RULE_INVALID
                          && path("W").equals(d.browsePath())
                          && d.message().contains("not an Object")),
          "a wrong-class rule target is diagnosed");
    }

    @Test
    void defaultInstanceBrowseNameExposedAndInherited() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("NamedType", NodeIds.BaseObjectType);
      fx.setDefaultInstanceBrowseName(typeNode, qn("DefaultName"));

      UaObjectTypeNode subType = fx.addObjectType("NamedSubType", typeNode.getNodeId());

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());
      assertEquals(qn("DefaultName"), model.defaultInstanceBrowseName().orElseThrow());
      assertTrue(
          model.diagnostics().stream()
              .noneMatch(d -> d.code() == ModelDiagnostic.Code.NOT_AN_INSTANCE_DECLARATION),
          "the consumed DefaultInstanceBrowseName property is not noise");

      TypeInstantiationModel subModel = fx.compiler().compile(subType.getNodeId());
      assertEquals(
          qn("DefaultName"),
          subModel.defaultInstanceBrowseName().orElseThrow(),
          "inherited from the nearest supertype providing one");
    }
  }

  /** Deterministic output, revision fingerprints, and dependency tracking. */
  @Nested
  class Determinism {

    /**
     * Output is deterministic across independent compilations: browse-path ordering and revision.
     */
    @Test
    void deterministicOrderingAndStableFingerprint() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("OrderType", NodeIds.BaseObjectType);
      // Insertion order deliberately unsorted.
      fx.addVariableDeclaration(
          typeNode, "Zed", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      UaObjectNode mid =
          fx.addObjectDeclaration(
              typeNode, "Mid", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          mid, "Nested", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);
      fx.addVariableDeclaration(
          typeNode, "Alpha", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      TypeInstantiationModel first = fx.compiler().compile(typeNode.getNodeId());
      TypeInstantiationModel second = fx.compiler().compile(typeNode.getNodeId());

      List<BrowsePath> expectedOrder =
          List.of(path("Alpha"), path("Mid"), path("Mid", "Nested"), path("Zed"));
      assertEquals(
          expectedOrder,
          first.declarations().stream().map(InstanceDeclaration::browsePath).toList());
      assertEquals(
          expectedOrder,
          second.declarations().stream().map(InstanceDeclaration::browsePath).toList());

      assertEquals(first.modelRevision(), second.modelRevision(), "unchanged type, equal revision");
      assertEquals(first.hierarchy(), second.hierarchy());
      assertEquals(first.references(), second.references());
    }

    /** The revision fingerprint tracks content, and dependencies name every contributing node. */
    @Test
    void fingerprintChangesWithDependenciesAndDependenciesAreComplete() throws Exception {
      UaObjectTypeNode memberType = fx.addObjectType("DepMemberType", NodeIds.BaseObjectType);
      UaVariableNode inner =
          fx.addVariableDeclaration(
              memberType, "Inner", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode typeNode = fx.addObjectType("DepType", NodeIds.BaseObjectType);
      UaObjectNode member =
          fx.addObjectDeclaration(
              typeNode, "M", memberType.getNodeId(), NodeIds.ModellingRule_Mandatory);

      TypeInstantiationModel before = fx.compiler().compile(typeNode.getNodeId());

      assertTrue(before.dependencies().contains(typeNode.getNodeId()));
      assertTrue(before.dependencies().contains(NodeIds.BaseObjectType), "supertype chain");
      assertTrue(before.dependencies().contains(member.getNodeId()), "declaration node");
      assertTrue(before.dependencies().contains(memberType.getNodeId()), "member type");
      assertTrue(before.dependencies().contains(inner.getNodeId()), "member type's declaration");

      // A change to a node in the dependency closure produces a different fingerprint.
      inner.setValue(new DataValue(new Variant("changed")));

      TypeInstantiationModel after = fx.compiler().compile(typeNode.getNodeId());
      assertNotEquals(before.modelRevision(), after.modelRevision());
    }

    /**
     * The revision fingerprints full value content: deterministic Java hash collisions ("Aa"/"BB")
     * must not collide the revision.
     */
    @Test
    void fingerprintDistinguishesCollidingValueHashes() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("FpType", NodeIds.BaseObjectType);
      UaVariableNode v =
          fx.addVariableDeclaration(
              typeNode, "V", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      v.setDisplayName(LocalizedText.english("Aa"));

      long before = fx.compiler().compile(typeNode.getNodeId()).modelRevision();
      v.setDisplayName(LocalizedText.english("BB"));
      long after = fx.compiler().compile(typeNode.getNodeId()).modelRevision();

      assertNotEquals(before, after, "colliding 32-bit value hashes must not collide the revision");
    }

    /**
     * The revision fingerprints diagnostics-relevant metadata of nested member types: an edit that
     * changes plan-time requirements (IsAbstract) must move the containing type's revision.
     */
    @Test
    void fingerprintTracksNestedMemberTypeEdits() throws Exception {
      UaObjectTypeNode memberType = fx.addObjectType("FpMemberType", NodeIds.BaseObjectType);
      UaObjectTypeNode host = fx.addObjectType("FpHostType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(host, "M", memberType.getNodeId(), NodeIds.ModellingRule_Mandatory);

      long concrete = fx.compiler().compile(host.getNodeId()).modelRevision();
      memberType.setIsAbstract(true);
      long abstractRevision = fx.compiler().compile(host.getNodeId()).modelRevision();

      assertNotEquals(
          concrete,
          abstractRevision,
          "a nested TypeDefinition's IsAbstract edit must change the containing revision");
    }
  }

  /** Cycles, self-recursion, and depth/width limits. */
  @Nested
  class Robustness {

    @Test
    void subtypeCycleRejected() {
      NodeId aId = fx.newNodeId("CycleA");
      NodeId bId = fx.newNodeId("CycleB");
      fx.addObjectType("CycleA", bId);
      fx.addObjectType("CycleB", aId);

      ModelCompilationException e =
          assertThrows(ModelCompilationException.class, () -> fx.compiler().compile(aId));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(d -> d.isError() && d.code() == ModelDiagnostic.Code.TYPE_CYCLE));
    }

    @Test
    void memberTypeSelfRecursionRejected() {
      UaObjectTypeNode typeNode = fx.addObjectType("SelfType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          typeNode, "Self", typeNode.getNodeId(), NodeIds.ModellingRule_Mandatory);

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(typeNode.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(d -> d.isError() && d.code() == ModelDiagnostic.Code.TYPE_CYCLE));
    }

    @Test
    void declarationLoopRejectedWithoutStackOverflow() {
      UaObjectTypeNode typeNode = fx.addObjectType("LoopType", NodeIds.BaseObjectType);
      UaObjectNode x =
          fx.addObjectDeclaration(
              typeNode, "X", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      x.addReference(
          new Reference(x.getNodeId(), NodeIds.HasComponent, x.getNodeId().expanded(), true));

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(typeNode.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(d -> d.isError() && d.code() == ModelDiagnostic.Code.DECLARATION_CYCLE));
    }

    /** Deep and wide hierarchies compile without stack overflow, fully ordered. */
    @Test
    void deepAndWideHierarchyCompiles() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("BigType", NodeIds.BaseObjectType);

      UaNode parent = typeNode;
      String[] deepNames = new String[150];
      for (int i = 0; i < 150; i++) {
        deepNames[i] = "D" + i;
        parent =
            fx.addObjectDeclaration(
                parent, "D" + i, NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      }
      for (int i = 0; i < 40; i++) {
        fx.addVariableDeclaration(
            typeNode, "Wide" + i, NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      }

      TypeInstantiationModel model = fx.compiler().compile(typeNode.getNodeId());

      assertEquals(190, model.declarations().size());
      assertTrue(model.get(path(deepNames)).isPresent(), "deepest path present");

      List<BrowsePath> paths =
          model.declarations().stream().map(InstanceDeclaration::browsePath).toList();
      assertEquals(paths.stream().sorted().toList(), paths, "declarations are browse-path ordered");
    }

    /** A graph deeper than the compiler's bound rejects with a structured diagnostic. */
    @Test
    void depthBeyondLimitRejectedWithStructuredDiagnostic() {
      UaObjectTypeNode typeNode = fx.addObjectType("VeryDeepType", NodeIds.BaseObjectType);
      UaNode parent = typeNode;
      for (int i = 0; i < TypeModelCompiler.MAX_DEPTH + 10; i++) {
        parent =
            fx.addObjectDeclaration(
                parent, "D" + i, NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      }

      ModelCompilationException e =
          assertThrows(
              ModelCompilationException.class, () -> fx.compiler().compile(typeNode.getNodeId()));

      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(d -> d.isError() && d.code() == ModelDiagnostic.Code.DEPTH_LIMIT_EXCEEDED));
    }
  }

  /** Applied-interface validation (Part 3 §4.10.2). */
  @Nested
  class Interfaces {

    /**
     * Applied interfaces are validated, not materialized (Part 3 §4.10.2): a Mandatory interface
     * member with no counterpart on the implementing type is diagnosed as a warning, but interface
     * declarations never contribute planned nodes; a type that satisfies the interface compiles
     * with no interface diagnostics.
     */
    @Test
    void interfaceMembersValidatedButNotMaterialized() throws Exception {
      UaObjectTypeNode machineInterface = fx.addInterfaceType("IMachine");
      fx.addVariableDeclaration(
          machineInterface, "Speed", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode missing = fx.addObjectType("NoSpeedType", NodeIds.BaseObjectType);
      missing.addReference(
          new Reference(
              missing.getNodeId(),
              NodeIds.HasInterface,
              machineInterface.getNodeId().expanded(),
              true));

      TypeInstantiationModel missingModel = fx.compiler().compile(missing.getNodeId());
      assertTrue(
          missingModel.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.INTERFACE_MEMBER_MISSING
                          && d.severity() == ModelDiagnostic.Severity.WARNING
                          && path("Speed").equals(d.browsePath())));
      assertTrue(
          missingModel.get(path("Speed")).isEmpty(),
          "validation only: the interface contributes no planned nodes");

      UaObjectTypeNode satisfied = fx.addObjectType("SpeedType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          satisfied, "Speed", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      satisfied.addReference(
          new Reference(
              satisfied.getNodeId(),
              NodeIds.HasInterface,
              machineInterface.getNodeId().expanded(),
              true));

      TypeInstantiationModel satisfiedModel = fx.compiler().compile(satisfied.getNodeId());
      assertTrue(
          satisfiedModel.diagnostics().stream()
              .noneMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.INTERFACE_MEMBER_MISSING
                          || d.code() == ModelDiagnostic.Code.INTERFACE_MEMBER_INCOMPATIBLE),
          "control: a satisfying implementer compiles with no interface diagnostics");
    }

    /**
     * A Mandatory interface member must be represented by a Mandatory declaration on the
     * implementer (Part 3 §4.10.2); an Optional local declaration is diagnosed as incompatible.
     */
    @Test
    void optionalDeclarationDoesNotSatisfyMandatoryInterfaceMember() throws Exception {
      UaObjectTypeNode machineInterface = fx.addInterfaceType("ISpeed");
      fx.addVariableDeclaration(
          machineInterface, "Speed", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode optionalImpl = fx.addObjectType("OptionalSpeedType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          optionalImpl, "Speed", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);
      optionalImpl.addReference(
          new Reference(
              optionalImpl.getNodeId(),
              NodeIds.HasInterface,
              machineInterface.getNodeId().expanded(),
              true));

      TypeInstantiationModel optionalModel = fx.compiler().compile(optionalImpl.getNodeId());

      assertTrue(
          optionalModel.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.INTERFACE_MEMBER_INCOMPATIBLE
                          && path("Speed").equals(d.browsePath())),
          "an Optional local declaration does not satisfy a Mandatory interface member");
    }

    /**
     * Every interface BrowsePath collision is validated, Optional interface members included: an
     * Optional interface member colliding with a dissimilar local node is diagnosed (Part 3
     * §4.10.2).
     */
    @Test
    void interfaceMemberCollidingWithDissimilarLocalNodeDiagnosed() throws Exception {
      UaObjectTypeNode modeInterface = fx.addInterfaceType("IMode");
      fx.addVariableDeclaration(
          modeInterface, "Mode", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

      UaObjectTypeNode collidingImpl =
          fx.addObjectType("CollidingModeType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          collidingImpl, "Mode", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      collidingImpl.addReference(
          new Reference(
              collidingImpl.getNodeId(),
              NodeIds.HasInterface,
              modeInterface.getNodeId().expanded(),
              true));

      TypeInstantiationModel collidingModel = fx.compiler().compile(collidingImpl.getNodeId());

      assertTrue(
          collidingModel.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == ModelDiagnostic.Code.INTERFACE_MEMBER_INCOMPATIBLE
                          && path("Mode").equals(d.browsePath())),
          "an Optional interface member colliding with a dissimilar local node is diagnosed");
    }
  }
}
