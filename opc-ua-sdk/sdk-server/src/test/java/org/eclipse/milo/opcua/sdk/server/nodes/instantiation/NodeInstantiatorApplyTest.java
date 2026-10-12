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
import static org.eclipse.milo.opcua.sdk.server.nodes.instantiation.TypeFixtures.path;
import static org.eclipse.milo.opcua.sdk.server.nodes.instantiation.TypeFixtures.qn;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.NodeManagerBatch;
import org.eclipse.milo.opcua.sdk.server.NodeManagerBatchException;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@code NodeInstantiator.apply} and {@code instantiate}: staged construction with full
 * attributes, hooks against the staged graph, journaled commit into a real {@link UaNodeManager},
 * failure injection at every apply stage with zero residue, and the ownership-explicit result.
 */
public class NodeInstantiatorApplyTest {

  private TypeFixtures fx;
  private UaNodeManager target;

  @BeforeEach
  void setUp() {
    fx = TypeFixtures.create();
    target = fx.newTargetManager();
  }

  /** An ObjectType with one Mandatory Variable member {@code Speed}. */
  private UaObjectTypeNode simpleType(String name) {
    UaObjectTypeNode typeNode = fx.addObjectType(name, NodeIds.BaseObjectType);
    fx.addVariableDeclaration(
        typeNode, "Speed", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
    return typeNode;
  }

  private InstantiationRequest.Builder<UaObjectNode> objectRequest(NodeId typeId, String rootId) {
    return InstantiationRequest.of(UaObjectNode.class, typeId)
        .nodeId(fx.newNodeId(rootId))
        .target(target);
  }

  private static boolean hasDiagnostic(
      InstantiationException e, InstantiationDiagnostic.Code code) {
    return e.getDiagnostics().stream().anyMatch(d -> d.code() == code);
  }

  private static long countReferences(
      UaNodeManager manager,
      NodeId sourceNodeId,
      NodeId referenceTypeId,
      NodeId targetNodeId,
      boolean forward) {

    return manager.getReferences(sourceNodeId).stream()
        .filter(
            r ->
                r.getReferenceTypeId().equals(referenceTypeId)
                    && r.getTargetNodeId().equals(targetNodeId.expanded())
                    && r.isForward() == forward)
        .count();
  }

  /** Assert the target holds nothing and was never effectively mutated. */
  private void assertNoResidue(NodeManager.Generation preApply) {
    assertTrue(target.getNodes().isEmpty(), "expected no nodes in the target");
    assertTrue(preApply.isCurrent(), "expected the target generation to be unchanged");
  }

  /** A distinct registrable node class proving typed construction reached the registry. */
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

  private UaObjectNode newObjectNode(NodeId nodeId, QualifiedName browseName) {
    return new UaObjectNode(
        fx.context(),
        nodeId,
        browseName,
        LocalizedText.english(browseName.name()),
        LocalizedText.NULL_VALUE,
        UInteger.MIN,
        UInteger.MIN,
        ubyte(0));
  }

  private UaVariableNode newVariableNode(NodeId nodeId, QualifiedName browseName) {
    return new UaVariableNode(
        fx.context(),
        nodeId,
        browseName,
        LocalizedText.english(browseName.name()),
        LocalizedText.NULL_VALUE,
        UInteger.MIN,
        UInteger.MIN);
  }

  @Nested
  class ExampleCallSite {

    /**
     * The design's example call site, executed end to end against a real {@link UaNodeManager}: one
     * request, typed result, zero post-hoc mutation.
     */
    @Test
    void exampleCallSiteEndToEnd() throws Exception {
      VariableTypeInitializer.initialize(fx.namespaceTable(), fx.variableTypeManager());
      fx.registerWithAddressSpace(target);

      UaObjectNode press1 = newObjectNode(fx.newNodeId("Press1"), qn("Press1"));
      target.addNode(press1);

      NodeId rootNodeId = fx.newNodeId("Devices/Press1/Pressure");
      NodeId euRangeNodeId = fx.newNodeId("Devices/Press1/Pressure/EURange");
      AtomicInteger hookInvocations = new AtomicInteger();

      InstantiationResult<AnalogItemTypeNode> result =
          fx.instantiator()
              .instantiate(
                  InstantiationRequest.of(AnalogItemTypeNode.class, NodeIds.AnalogItemType)
                      .nodeId(rootNodeId)
                      .browseName(qn("Pressure"))
                      .displayName(LocalizedText.english("Pressure"))
                      .parent(press1.getNodeId(), NodeIds.HasComponent)
                      .target(target)
                      .includeOptional(BrowsePath.of(new QualifiedName(0, "EngineeringUnits")))
                      .nodeIdStrategy(NodeIdContext::defaultNodeId)
                      .assignNodeId(BrowsePath.of(new QualifiedName(0, "EURange")), euRangeNodeId)
                      .onNode(
                          (declaration, node, parent, graph) -> hookInvocations.incrementAndGet())
                      .build());

      // Typed root, no cast; committed into the target with request identity.
      AnalogItemTypeNode root = result.root();
      assertSame(root, target.get(rootNodeId));
      assertEquals(qn("Pressure"), root.getBrowseName());
      assertEquals(LocalizedText.english("Pressure"), root.getDisplayName());

      // The unset root Value follows the Bad_NoValue convention.
      assertEquals(new StatusCode(StatusCodes.Bad_NoValue), root.getValue().getStatusCode());

      // Parent attachment committed with the graph, both observable directions.
      assertEquals(
          1, countReferences(target, press1.getNodeId(), NodeIds.HasComponent, rootNodeId, true));
      assertEquals(
          1, countReferences(target, rootNodeId, NodeIds.HasComponent, press1.getNodeId(), false));

      // Root HasTypeDefinition committed.
      assertEquals(
          1,
          countReferences(
              target, rootNodeId, NodeIds.HasTypeDefinition, NodeIds.AnalogItemType, true));

      // The pinned EURange id and the selected optional both materialized.
      assertTrue(target.containsNode(euRangeNodeId));
      assertTrue(result.node(BrowsePath.of(new QualifiedName(0, "EngineeringUnits"))).isPresent());

      // Generated typed getters resolve planned children through the committed references.
      assertNotNull(root.getEuRangeNode());
      assertNotNull(root.getEngineeringUnitsNode());

      assertTrue(hookInvocations.get() > 0);
      assertEquals(NodeManager.StorageGuarantee.ATOMIC, result.storageGuarantee());
      assertEquals(result.materializedNodes().size() + 1, target.getNodes().size());
    }
  }

  @Nested
  class RootConstruction {

    @Test
    void tupleConstructorPlusOverlayDeliversFullAttributes() throws Exception {
      UaObjectTypeNode typeNode = simpleType("TupleType");

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
                TestObjectNode node = new TestObjectNode(context, nodeId, browseName, displayName);
                node.setDescription(description);
                node.setWriteMask(writeMask);
                node.setUserWriteMask(userWriteMask);
                node.setRolePermissions(rolePermissions);
                node.setUserRolePermissions(userRolePermissions);
                node.setAccessRestrictions(accessRestrictions);
                return node;
              });

      RolePermissionType[] rolePermissions = {
        new RolePermissionType(NodeIds.WellKnownRole_Anonymous, new PermissionType(uint(1)))
      };

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(typeNode.getNodeId(), "Tuple1")
                      .browseName(qn("Tuple1"))
                      .displayName(LocalizedText.english("Tuple1"))
                      .rootAttribute(AttributeId.WriteMask, uint(3))
                      .rootAttribute(AttributeId.RolePermissions, rolePermissions)
                      .rootAttribute(
                          AttributeId.AccessRestrictions,
                          new AccessRestrictionType(UShort.valueOf(2)))
                      .rootAttribute(AttributeId.EventNotifier, ubyte(1))
                      .build());

      UaObjectNode root = assertInstanceOf(TestObjectNode.class, result.root());
      assertEquals(uint(3), root.getWriteMask());
      assertEquals(rolePermissions[0], root.getRolePermissions()[0]);
      assertEquals(new AccessRestrictionType(UShort.valueOf(2)), root.getAccessRestrictions());

      // EventNotifier is not part of the tuple signature; the engine's adaptation overlay set it.
      assertEquals(ubyte(1), root.getEventNotifier());
    }

    @Test
    void snapshotConstructorReceivesEffectiveAttributes() throws Exception {
      UaObjectTypeNode typeNode = simpleType("SnapshotType");

      AtomicReference<@Nullable AttributeSnapshot> received = new AtomicReference<>();
      fx.objectTypeManager()
          .registerObjectType(
              typeNode.getNodeId(),
              TestObjectNode.class,
              (context, nodeId, attributes) -> {
                received.set(attributes);
                return new TestObjectNode(
                    context,
                    nodeId,
                    (QualifiedName) requireNonNull(attributes.getOrNull(AttributeId.BrowseName)),
                    (LocalizedText) requireNonNull(attributes.getOrNull(AttributeId.DisplayName)));
              });

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(typeNode.getNodeId(), "Snap1").browseName(qn("Snap1")).build());

      assertInstanceOf(TestObjectNode.class, result.root());
      AttributeSnapshot snapshot = received.get();
      assertNotNull(snapshot);
      assertEquals(qn("Snap1"), snapshot.getOrNull(AttributeId.BrowseName));
    }

    @Test
    void declarationAttributesPropagateAndValueIsFreshened() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("FreshType", NodeIds.BaseObjectType);
      UaVariableNode declaration =
          fx.addVariableDeclaration(
              typeNode, "Speed", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      DataValue declaredValue =
          new DataValue(new Variant(42.0), StatusCode.GOOD, new DateTime(1234567890L));
      declaration.setValue(declaredValue);
      declaration.setDataType(NodeIds.Double);
      declaration.setMinimumSamplingInterval(250.0);
      declaration.setAccessLevel(ubyte(3));

      InstantiationResult<UaObjectNode> result =
          fx.instantiator().instantiate(objectRequest(typeNode.getNodeId(), "Fresh1").build());

      UaVariableNode speed = result.require(path("Speed"), UaVariableNode.class);

      // Attributes legacy never copied now propagate by policy.
      assertEquals(250.0, speed.getMinimumSamplingInterval());
      assertEquals(ubyte(3), speed.getAccessLevel());
      assertEquals(NodeIds.Double, speed.getDataType());

      // The committed value is a fresh DataValue: same content, new source timestamp, never the
      // declaration's shared instance.
      DataValue committed = speed.getValue();
      assertNotSame(declaredValue, committed);
      assertEquals(new Variant(42.0), committed.getValue());
      assertNotNull(committed.getSourceTime());
      assertNotEquals(new DateTime(1234567890L), committed.getSourceTime());
    }

    @Test
    void unsetVariableValueFollowsBadNoValueConvention() throws Exception {
      UaObjectTypeNode typeNode = simpleType("NoValueType");

      InstantiationResult<UaObjectNode> result =
          fx.instantiator().instantiate(objectRequest(typeNode.getNodeId(), "NoValue1").build());

      UaVariableNode speed = result.require(path("Speed"), UaVariableNode.class);
      assertEquals(new StatusCode(StatusCodes.Bad_NoValue), speed.getValue().getStatusCode());
    }
  }

  @Nested
  class FailureInjection {

    @Test
    void planWithErrorsIsRefusedBeforeAnyStage() {
      UaObjectTypeNode typeNode = simpleType("RefusedType");

      NodeManager.Generation generation = target.getGeneration();

      InstantiationException e =
          assertThrows(
              InstantiationException.class,
              () ->
                  fx.instantiator()
                      .instantiate(
                          objectRequest(typeNode.getNodeId(), "Refused1")
                              .excludeOptional(path("Speed")) // Mandatory omission: a plan error
                              .build()));

      assertEquals(InstantiationDiagnostic.Phase.PLAN, e.getPhase());
      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.MANDATORY_OMITTED));
      assertNoResidue(generation);
    }

    @Test
    void stalePlanFailsWithModelChangedAndNoMutation() throws Exception {
      UaObjectTypeNode typeNode = simpleType("StaleType");

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Stale1").build());

      // The type mutates after planning; mutators must invalidate the cached model so apply can
      // detect the stale plan by its recorded revision.
      fx.addVariableDeclaration(
          typeNode, "Extra", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.typeModelCache().invalidate(typeNode.getNodeId());

      NodeManager.Generation generation = target.getGeneration();

      InstantiationException e =
          assertThrows(InstantiationException.class, () -> fx.instantiator().apply(plan));

      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.MODEL_CHANGED));
      assertNoResidue(generation);
    }

    @Test
    void constructorFailureLeavesNoResidue() {
      UaObjectTypeNode typeNode = simpleType("CtorFailType");

      fx.objectTypeManager()
          .registerObjectType(
              typeNode.getNodeId(),
              TestObjectNode.class,
              (org.eclipse.milo.opcua.sdk.server.ObjectTypeManager.SnapshotConstructor)
                  (context, nodeId, attributes) -> {
                    throw new IllegalStateException("injected constructor failure");
                  });

      NodeManager.Generation generation = target.getGeneration();

      InstantiationException e =
          assertThrows(
              InstantiationException.class,
              () ->
                  fx.instantiator()
                      .instantiate(objectRequest(typeNode.getNodeId(), "CtorFail1").build()));

      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.CONSTRUCTOR_FAILED));
      assertNoResidue(generation);
    }

    @Test
    void constructorReturningWrongClassFails() {
      UaObjectTypeNode typeNode = simpleType("WrongClassType");

      // Registered as TestObjectNode but constructs a plain UaObjectNode.
      fx.objectTypeManager()
          .registerObjectType(
              typeNode.getNodeId(),
              TestObjectNode.class,
              (org.eclipse.milo.opcua.sdk.server.ObjectTypeManager.SnapshotConstructor)
                  (context, nodeId, attributes) ->
                      new UaObjectNode(
                          context,
                          nodeId,
                          (QualifiedName) attributes.getOrNull(AttributeId.BrowseName),
                          LocalizedText.english("wrong"),
                          LocalizedText.NULL_VALUE,
                          UInteger.MIN,
                          UInteger.MIN,
                          ubyte(0)));

      NodeManager.Generation generation = target.getGeneration();

      InstantiationException e =
          assertThrows(
              InstantiationException.class,
              () ->
                  fx.instantiator()
                      .instantiate(objectRequest(typeNode.getNodeId(), "WrongClass1").build()));

      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.CONSTRUCTOR_FAILED));
      assertNoResidue(generation);
    }

    @Test
    void onNodeHookFailureLeavesNoResidue() {
      UaObjectTypeNode typeNode = simpleType("HookFailType");

      NodeManager.Generation generation = target.getGeneration();

      InstantiationException e =
          assertThrows(
              InstantiationException.class,
              () ->
                  fx.instantiator()
                      .instantiate(
                          objectRequest(typeNode.getNodeId(), "HookFail1")
                              .onNode(
                                  (declaration, node, parent, graph) -> {
                                    throw new IllegalStateException("injected hook failure");
                                  })
                              .build()));

      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.CUSTOMIZATION_FAILED));
      assertNoResidue(generation);
    }

    @Test
    void bindMethodFailureLeavesNoResidue() {
      UaObjectTypeNode typeNode = fx.addObjectType("BindFailType", NodeIds.BaseObjectType);
      fx.addMethodDeclaration(typeNode, "Start", NodeIds.ModellingRule_Mandatory);

      InstantiationException e =
          assertThrows(
              InstantiationException.class,
              () ->
                  fx.instantiator()
                      .instantiate(
                          objectRequest(typeNode.getNodeId(), "BindFail1")
                              .bindMethod(
                                  path("Start"),
                                  method -> {
                                    throw new IllegalStateException("injected binder failure");
                                  })
                              .build()));

      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.CUSTOMIZATION_FAILED));
      // Binders run after the commit, so the failure rolls the committed batch back: the target
      // holds no nodes, but its generation has necessarily advanced.
      assertTrue(target.getNodes().isEmpty(), "expected no nodes in the target");
    }

    @Test
    void collisionAppearingAfterPlanFailsWithNoStaleState() throws Exception {
      UaObjectTypeNode typeNode = simpleType("LateCollisionType");

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(typeNode.getNodeId(), "Late1").build());

      // A direct write claims a planned child id between plan and apply.
      NodeId speedNodeId = plan.plannedNode(path("Speed")).orElseThrow().nodeId();
      UaVariableNode squatter = newVariableNode(speedNodeId, qn("Squatter"));
      target.addNode(squatter);

      InstantiationException e =
          assertThrows(InstantiationException.class, () -> fx.instantiator().apply(plan));

      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.NODE_ID_COLLISION));

      // No stale state: the squatter is untouched and nothing else landed.
      assertEquals(List.of(squatter), target.getNodes());
      assertTrue(target.getReferences(speedNodeId).isEmpty());
    }

    @Test
    void atomicCommitFailureLeavesNoResidue() {
      UaObjectTypeNode typeNode = simpleType("AtomicFailType");
      FailingCommitManager failingTarget = new FailingCommitManager();

      InstantiationException e =
          assertThrows(
              InstantiationException.class,
              () ->
                  fx.instantiator()
                      .instantiate(
                          InstantiationRequest.of(UaObjectNode.class, typeNode.getNodeId())
                              .nodeId(fx.newNodeId("AtomicFail1"))
                              .target(failingTarget)
                              .build()));

      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.COMMIT_FAILED));
      assertFalse(hasDiagnostic(e, InstantiationDiagnostic.Code.ROLLBACK_FAILED));
      assertTrue(failingTarget.getNodes().isEmpty());
    }

    @Test
    void bestEffortPartialCommitIsRolledBackExactly() {
      UaObjectTypeNode typeNode = simpleType("PartialFailType");
      PartialCommitManager partialTarget = new PartialCommitManager();

      InstantiationException e =
          assertThrows(
              InstantiationException.class,
              () ->
                  fx.instantiator()
                      .instantiate(
                          InstantiationRequest.of(UaObjectNode.class, typeNode.getNodeId())
                              .nodeId(fx.newNodeId("PartialFail1"))
                              .target(partialTarget)
                              .build()));

      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.COMMIT_FAILED));
      assertFalse(hasDiagnostic(e, InstantiationDiagnostic.Code.ROLLBACK_FAILED));

      // The partial journal was rolled back: the node and reference it applied are gone.
      assertTrue(partialTarget.getNodes().isEmpty());
      assertTrue(partialTarget.getReferences(fx.newNodeId("PartialFail1")).isEmpty());
    }

    @Test
    void uncheckedCommitFailureIsReportedAsCommitFailed() {
      UaObjectTypeNode typeNode = simpleType("UncheckedFailType");

      UaNodeManager uncheckedTarget =
          new UaNodeManager() {
            @Override
            public CommitResult commit(NodeManagerBatch<UaNode> batch) {
              throw new IllegalStateException("injected unchecked commit failure");
            }
          };

      InstantiationException e =
          assertThrows(
              InstantiationException.class,
              () ->
                  fx.instantiator()
                      .instantiate(
                          InstantiationRequest.of(UaObjectNode.class, typeNode.getNodeId())
                              .nodeId(fx.newNodeId("UncheckedFail1"))
                              .target(uncheckedTarget)
                              .build()));

      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.COMMIT_FAILED));
      assertTrue(uncheckedTarget.getNodes().isEmpty());
    }

    @Test
    void rollbackFailureIsLoudAndNamesTheResidue() {
      UaObjectTypeNode typeNode = simpleType("RollbackFailType");
      RollbackFailingManager rollbackFailingTarget = new RollbackFailingManager();

      InstantiationException e =
          assertThrows(
              InstantiationException.class,
              () ->
                  fx.instantiator()
                      .instantiate(
                          InstantiationRequest.of(UaObjectNode.class, typeNode.getNodeId())
                              .nodeId(fx.newNodeId("RollbackFail1"))
                              .target(rollbackFailingTarget)
                              .build()));

      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.COMMIT_FAILED));

      InstantiationDiagnostic rollbackFailed =
          e.getDiagnostics().stream()
              .filter(d -> d.code() == InstantiationDiagnostic.Code.ROLLBACK_FAILED)
              .findFirst()
              .orElseThrow(() -> new AssertionError("expected a ROLLBACK_FAILED diagnostic"));

      // The diagnostic names what could not be removed, and the residue is really there.
      assertNotNull(rollbackFailed.nodeId());
      assertTrue(rollbackFailingTarget.containsNode(rollbackFailed.nodeId()));
    }
  }

  @Nested
  class ApplyCollisions {

    @Test
    void reuseRecheckFailsWhenAdoptedNodeTurnsIncompatible() throws Exception {
      UaObjectTypeNode typeNode = simpleType("ReuseDriftType");

      // A compatible node exists at the planned Speed id; the plan adopts it.
      NodeId speedNodeId = fx.newNodeId("ReuseDrift1/1:Speed");
      UaVariableNode compatible = newVariableNode(speedNodeId, qn("Speed"));
      target.addNode(compatible);
      target.addReference(
          new Reference(
              speedNodeId,
              NodeIds.HasTypeDefinition,
              NodeIds.BaseDataVariableType.expanded(),
              true));

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(typeNode.getNodeId(), "ReuseDrift1")
                      .legacyPathStrings()
                      .conflictPolicy(ConflictPolicy.REUSE_COMPATIBLE)
                      .build());

      assertEquals(
          PlannedNode.Materialization.REUSE,
          plan.plannedNode(path("Speed")).orElseThrow().materialization());

      // The adopted node is replaced by something incompatible before apply.
      target.removeNode(speedNodeId);
      target.removeReference(
          new Reference(
              speedNodeId,
              NodeIds.HasTypeDefinition,
              NodeIds.BaseDataVariableType.expanded(),
              true));
      UaObjectNode incompatible = newObjectNode(speedNodeId, qn("NotSpeed"));
      target.addNode(incompatible);

      InstantiationException e =
          assertThrows(InstantiationException.class, () -> fx.instantiator().apply(plan));

      assertTrue(hasDiagnostic(e, InstantiationDiagnostic.Code.INCOMPATIBLE_REUSE));

      // No mutation beyond the pre-existing state.
      assertEquals(List.of(incompatible), target.getNodes());
    }
  }

  @Nested
  class References {

    @Test
    void nonHierarchicalRowsMappedOntoInstancesExactlyOncePerDirection() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("WiredType", NodeIds.BaseObjectType);
      NodeId relatesToId =
          fx.addReferenceType("RelatesTo", NodeIds.NonHierarchicalReferences).getNodeId();

      UaObjectNode declarationA =
          fx.addObjectDeclaration(
              typeNode, "A", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
      UaVariableNode declarationB =
          fx.addVariableDeclaration(
              typeNode, "B", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

      declarationA.addReference(
          new Reference(
              declarationA.getNodeId(), relatesToId, declarationB.getNodeId().expanded(), true));

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(typeNode.getNodeId(), "Wired1").includeOptional(path("B")).build());

      NodeId instanceA = result.node(path("A")).orElseThrow().getNodeId();
      NodeId instanceB = result.node(path("B")).orElseThrow().getNodeId();

      // Exactly once in each observable direction, in the manager itself (not just the result).
      assertEquals(1, countReferences(target, instanceA, relatesToId, instanceB, true));
      assertEquals(1, countReferences(target, instanceB, relatesToId, instanceA, false));

      // With B omitted, no edge to the omitted target lands anywhere.
      UaNodeManager target2 = fx.newTargetManager();
      InstantiationResult<UaObjectNode> result2 =
          fx.instantiator()
              .instantiate(
                  InstantiationRequest.of(UaObjectNode.class, typeNode.getNodeId())
                      .nodeId(fx.newNodeId("Wired2"))
                      .target(target2)
                      .build());

      NodeId instanceA2 = result2.node(path("A")).orElseThrow().getNodeId();
      assertTrue(
          target2.getReferences(instanceA2).stream()
              .noneMatch(r -> r.getReferenceTypeId().equals(relatesToId)));
    }

    @Test
    void hierarchyEdgesCommittedWithInverses() throws Exception {
      UaObjectTypeNode typeNode = simpleType("EdgeType");

      InstantiationResult<UaObjectNode> result =
          fx.instantiator().instantiate(objectRequest(typeNode.getNodeId(), "Edge1").build());

      NodeId rootNodeId = result.root().getNodeId();
      NodeId speedNodeId = result.node(path("Speed")).orElseThrow().getNodeId();

      assertEquals(1, countReferences(target, rootNodeId, NodeIds.HasComponent, speedNodeId, true));
      assertEquals(
          1, countReferences(target, speedNodeId, NodeIds.HasComponent, rootNodeId, false));
      assertEquals(
          1,
          countReferences(
              target, speedNodeId, NodeIds.HasTypeDefinition, NodeIds.BaseDataVariableType, true));
    }
  }

  @Nested
  class HooksAndResult {

    @Test
    void hooksFireExactlyOncePerCreatedNodeAtTheStagedPhase() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("HookType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "Speed", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addObjectDeclaration(
          typeNode, "Motor", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);

      Map<NodeId, Integer> invocations = new LinkedHashMap<>();
      List<String> failures = new ArrayList<>();

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(typeNode.getNodeId(), "Hook1")
                      .onNode(
                          (declaration, node, parent, graph) -> {
                            invocations.merge(node.getNodeId(), 1, Integer::sum);

                            if (target.containsNode(node.getNodeId())) {
                              failures.add(node.getNodeId() + " already published during hook");
                            }
                            boolean isRoot = declaration == null;
                            if (isRoot != (parent == null)) {
                              failures.add("parent nullity wrong for " + node.getNodeId());
                            }
                            if (graph.node(BrowsePath.root()).isEmpty()) {
                              failures.add("staged graph missing the root");
                            }
                          })
                      .build());

      assertTrue(failures.isEmpty(), failures::toString);
      assertEquals(3, invocations.size());
      invocations.forEach((nodeId, count) -> assertEquals(1, count, nodeId.toString()));
      assertEquals(3, result.materializedNodes().size());
    }

    @Test
    void bindMethodBindsTheCopiedMethodBeforeApplyReturns() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("BindType", NodeIds.BaseObjectType);
      fx.addMethodDeclaration(typeNode, "Start", NodeIds.ModellingRule_Mandatory);

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(typeNode.getNodeId(), "Bind1")
                      .bindMethod(
                          path("Start"),
                          method -> method.setDescription(LocalizedText.english("bound")))
                      .build());

      UaMethodNode start = result.require(path("Start"), UaMethodNode.class);
      assertEquals(LocalizedText.english("bound"), start.getDescription());
      assertTrue(target.containsNode(start.getNodeId()));
      assertEquals(
          MaterializedNode.Provenance.CREATED,
          result.materializedNode(path("Start")).orElseThrow().provenance());
    }

    @Test
    void sharedMethodBinderReceivesTheDeclarationNode() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("ShareType", NodeIds.BaseObjectType);
      UaMethodNode declaration =
          fx.addMethodDeclaration(typeNode, "Start", NodeIds.ModellingRule_Mandatory);

      AtomicReference<@Nullable UaMethodNode> bound = new AtomicReference<>();

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(typeNode.getNodeId(), "Share1")
                      .methodInstantiation(MethodInstantiation.SHARE)
                      .bindMethod(path("Start"), bound::set)
                      .build());

      assertSame(declaration, bound.get());

      MaterializedNode shared = result.materializedNode(path("Start")).orElseThrow();
      assertEquals(MaterializedNode.Provenance.SHARED, shared.provenance());
      assertSame(declaration, shared.node());

      // The shared Method is referenced, not copied into the target.
      assertFalse(target.containsNode(declaration.getNodeId()));
      assertEquals(
          1,
          countReferences(
              target,
              result.root().getNodeId(),
              NodeIds.HasComponent,
              declaration.getNodeId(),
              true));
    }

    @Test
    void afterCommitObserverIsNotificationOnly() throws Exception {
      UaObjectTypeNode typeNode = simpleType("ObserverType");

      AtomicReference<@Nullable InstantiationResult<UaObjectNode>> observed =
          new AtomicReference<>();

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(typeNode.getNodeId(), "Observer1")
                      .afterCommit(
                          r -> {
                            throw new IllegalStateException("observer failure is swallowed");
                          })
                      .afterCommit(observed::set)
                      .build());

      // The throwing observer affected nothing; the second still ran, after commit.
      assertSame(result, observed.get());
      assertTrue(target.containsNode(result.root().getNodeId()));
    }

    @Test
    void resultLookupsAreCompleteAndTyped() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("LookupType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "Speed", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          typeNode, "Extra", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

      InstantiationResult<UaObjectNode> result =
          fx.instantiator().instantiate(objectRequest(typeNode.getNodeId(), "Lookup1").build());

      // No null entries: every materialized entry has a node, every planned path resolves.
      assertFalse(result.materializedNodes().isEmpty());
      result
          .materializedNodes()
          .forEach(
              m -> {
                assertNotNull(m.node());
                assertTrue(result.node(m.browsePath()).isPresent());
              });

      // Skipped occurrences are indexed, not silently absent.
      assertTrue(
          result.skippedDeclarations().stream()
              .anyMatch(
                  s ->
                      s.declaration().browsePath().equals(path("Extra"))
                          && s.reason() == SkippedDeclaration.Reason.OPTIONAL_NOT_SELECTED));

      // Checked lookups fail with precise status codes instead of returning null.
      UaException notFound =
          assertThrows(
              UaException.class, () -> result.require(path("Extra"), UaVariableNode.class));
      assertEquals(StatusCodes.Bad_NotFound, notFound.getStatusCode().getValue());

      UaException mismatch =
          assertThrows(UaException.class, () -> result.require(path("Speed"), UaMethodNode.class));
      assertEquals(StatusCodes.Bad_TypeMismatch, mismatch.getStatusCode().getValue());
    }
  }

  @Nested
  class ReuseAndCleanup {

    @Test
    void compatibleReuseIsAdoptedAndDeleteCreatedIsOwnershipSafe() throws Exception {
      UaObjectTypeNode typeNode = simpleType("ReuseType");

      // A compatible node pre-exists at the planned Speed id, with its own reference row.
      NodeId speedNodeId = fx.newNodeId("Reuse1/1:Speed");
      UaVariableNode existing = newVariableNode(speedNodeId, qn("Speed"));
      target.addNode(existing);
      Reference preExistingRow =
          new Reference(
              speedNodeId,
              NodeIds.HasTypeDefinition,
              NodeIds.BaseDataVariableType.expanded(),
              true);
      target.addReference(preExistingRow);

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(typeNode.getNodeId(), "Reuse1")
                      .legacyPathStrings()
                      .conflictPolicy(ConflictPolicy.REUSE_COMPATIBLE)
                      .build());

      MaterializedNode speed = result.materializedNode(path("Speed")).orElseThrow();
      assertEquals(MaterializedNode.Provenance.REUSED, speed.provenance());
      assertSame(existing, speed.node());

      NodeId rootNodeId = result.root().getNodeId();
      assertEquals(1, countReferences(target, rootNodeId, NodeIds.HasComponent, speedNodeId, true));

      // Cleanup removes exactly what this instantiation created: the root and its rows go, the
      // reused node and its pre-existing row stay. Idempotent.
      result.deleteCreated();
      result.deleteCreated();

      assertFalse(target.containsNode(rootNodeId));
      assertTrue(target.containsNode(speedNodeId));
      assertTrue(target.getReferences(speedNodeId).contains(preExistingRow));
      assertEquals(
          0, countReferences(target, speedNodeId, NodeIds.HasComponent, rootNodeId, false));
    }
  }

  @Nested
  class Concurrency {

    @Test
    void concurrentDifferentRootsBothSucceed() throws Exception {
      UaObjectTypeNode typeNode = simpleType("ConcurrentType");
      NodeInstantiator instantiator = fx.instantiator();

      ExecutorService executor = Executors.newFixedThreadPool(2);
      try {
        CountDownLatch start = new CountDownLatch(1);

        List<Future<InstantiationResult<UaObjectNode>>> futures = new ArrayList<>();
        for (String rootId : List.of("Concurrent1", "Concurrent2")) {
          futures.add(
              executor.submit(
                  () -> {
                    start.await();
                    return instantiator.instantiate(
                        objectRequest(typeNode.getNodeId(), rootId).build());
                  }));
        }
        start.countDown();

        for (Future<InstantiationResult<UaObjectNode>> future : futures) {
          assertNotNull(future.get().root());
        }

        assertTrue(target.containsNode(fx.newNodeId("Concurrent1")));
        assertTrue(target.containsNode(fx.newNodeId("Concurrent2")));
      } finally {
        executor.shutdownNow();
      }
    }

    @Test
    void concurrentSameRootExactlyOneWins() throws Exception {
      UaObjectTypeNode typeNode = simpleType("RaceType");
      NodeInstantiator instantiator = fx.instantiator();

      ExecutorService executor = Executors.newFixedThreadPool(2);
      try {
        CountDownLatch start = new CountDownLatch(1);

        List<Future<InstantiationResult<UaObjectNode>>> futures = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
          futures.add(
              executor.submit(
                  () -> {
                    start.await();
                    return instantiator.instantiate(
                        objectRequest(typeNode.getNodeId(), "Race1").build());
                  }));
        }
        start.countDown();

        int successes = 0;
        InstantiationResult<UaObjectNode> winner = null;
        for (Future<InstantiationResult<UaObjectNode>> future : futures) {
          try {
            winner = future.get();
            successes++;
          } catch (java.util.concurrent.ExecutionException e) {
            assertInstanceOf(InstantiationException.class, e.getCause());
          }
        }

        assertEquals(1, successes);
        assertNotNull(winner);

        // Exactly one instance graph landed; the loser left no residue.
        assertEquals(winner.materializedNodes().size(), target.getNodes().size());
      } finally {
        executor.shutdownNow();
      }
    }
  }

  @Nested
  class Regressions {

    /** Request state must never contaminate the cached, shared model. */
    @Test
    void cachedModelIsIndependentOfRequestState() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("SharedModelType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "Speed", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addObjectDeclaration(
          typeNode, "Motor", NodeIds.BaseObjectType, NodeIds.ModellingRule_Optional);

      NodeInstantiator instantiator = fx.instantiator();

      InstantiationPlan<UaObjectNode> plan1 =
          instantiator.plan(
              objectRequest(typeNode.getNodeId(), "Shared1")
                  .includeOptional(path("Motor"))
                  .onNode((declaration, node, parent, graph) -> {})
                  .build());

      InstantiationPlan<UaObjectNode> plan2 =
          instantiator.plan(objectRequest(typeNode.getNodeId(), "Shared2").build());

      // One cached, request-free model instance serves both plans.
      assertSame(plan1.model(), plan2.model());

      InstantiationResult<UaObjectNode> result1 = instantiator.apply(plan1);
      InstantiationResult<UaObjectNode> result2 = instantiator.apply(plan2);

      // The first request's selection did not leak into the second.
      assertTrue(result1.node(path("Motor")).isPresent());
      assertTrue(result2.node(path("Motor")).isEmpty());
      assertTrue(
          result2.skippedDeclarations().stream()
              .anyMatch(s -> s.declaration().browsePath().equals(path("Motor"))));
    }

    /** Cross-namespace identity: ids follow the root's namespace, BrowseNames keep their own. */
    @Test
    void crossNamespaceIdentityIsPreserved() throws Exception {
      UaObjectTypeNode typeNode = simpleType("CrossNsType");

      UShort otherNamespaceIndex = fx.namespaceTable().add("urn:eclipse:milo:test:other");

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  InstantiationRequest.of(UaObjectNode.class, typeNode.getNodeId())
                      .nodeId(new NodeId(otherNamespaceIndex, "CrossNs1"))
                      .target(target)
                      .build());

      UaNode speed = result.node(path("Speed")).orElseThrow();
      assertEquals(otherNamespaceIndex, speed.getNodeId().getNamespaceIndex());
      assertEquals(qn("Speed"), speed.getBrowseName());
      assertEquals(1, speed.getBrowseName().namespaceIndex().intValue());
    }

    /**
     * An unselected optional produces nothing — no orphaned descendants. Legacy NodeFactory created
     * an omitted optional's subtree and then deleted it, which could leave residue.
     */
    @Test
    void exclusionProducesNoOrphansInTheManager() throws Exception {
      UaObjectTypeNode motorType = fx.addObjectType("OrphanMotorType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          motorType, "Temp", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode typeNode = fx.addObjectType("OrphanType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          typeNode, "Motor", motorType.getNodeId(), NodeIds.ModellingRule_Optional);

      InstantiationResult<UaObjectNode> result =
          fx.instantiator().instantiate(objectRequest(typeNode.getNodeId(), "Orphan1").build());

      // The manager holds exactly the materialized set: the root, nothing under Motor.
      assertEquals(result.materializedNodes().size(), target.getNodes().size());
      assertTrue(target.getNodes().stream().noneMatch(n -> n.getBrowseName().equals(qn("Temp"))));
    }

    /** Nested-hierarchy correctness end to end, including supertype members of a member's type. */
    @Test
    void nestedHierarchyMaterializesAtEveryDepth() throws Exception {
      UaObjectTypeNode baseMotorType =
          fx.addObjectType("NestedBaseMotorType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          baseMotorType,
          "CurrentState",
          NodeIds.BaseDataVariableType,
          NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode derivedMotorType =
          fx.addObjectType("NestedDerivedMotorType", baseMotorType.getNodeId());
      fx.addVariableDeclaration(
          derivedMotorType, "Temp", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode typeNode = fx.addObjectType("NestedType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          typeNode, "Motor", derivedMotorType.getNodeId(), NodeIds.ModellingRule_Mandatory);

      InstantiationResult<UaObjectNode> result =
          fx.instantiator().instantiate(objectRequest(typeNode.getNodeId(), "Nested1").build());

      // The member's own member and its supertype-declared member both exist, correctly wired —
      // supertype-declared members of a member's type are exactly what legacy NodeFactory loses
      // in nested hierarchies, held correct here end to end through apply.
      UaNode motor = result.node(path("Motor")).orElseThrow();
      UaNode temp = result.node(path("Motor", "Temp")).orElseThrow();
      UaNode currentState = result.node(path("Motor", "CurrentState")).orElseThrow();

      assertEquals(NodeClass.Object, motor.getNodeClass());
      assertEquals(
          1,
          countReferences(target, motor.getNodeId(), NodeIds.HasComponent, temp.getNodeId(), true));
      assertEquals(
          1,
          countReferences(
              target, motor.getNodeId(), NodeIds.HasComponent, currentState.getNodeId(), true));
      assertEquals(
          1,
          countReferences(
              target,
              motor.getNodeId(),
              NodeIds.HasTypeDefinition,
              derivedMotorType.getNodeId(),
              true));
    }
  }

  // region Failure-injection managers

  /** A target whose commit always fails atomically: nothing applied, empty journal. */
  static class FailingCommitManager extends UaNodeManager {
    @Override
    public CommitResult commit(NodeManagerBatch<UaNode> batch) throws NodeManagerBatchException {
      throw new NodeManagerBatchException(
          StatusCodes.Bad_InternalError,
          "injected commit failure",
          new CommitResult(List.of(), List.of()));
    }
  }

  /**
   * A degraded-path target: applies the first node and first reference of the batch, then fails
   * reporting that partial journal — the shape a failed best-effort emulation produces.
   */
  static class PartialCommitManager extends UaNodeManager {
    @Override
    public CommitResult commit(NodeManagerBatch<UaNode> batch) throws NodeManagerBatchException {
      List<NodeId> addedNodes = new ArrayList<>();
      List<Reference> addedReferences = new ArrayList<>();

      if (!batch.getNodeAdditions().isEmpty()) {
        UaNode first = batch.getNodeAdditions().get(0);
        addNode(first);
        addedNodes.add(first.getNodeId());
      }
      if (!batch.getReferenceAdditions().isEmpty()) {
        Reference first = batch.getReferenceAdditions().get(0);
        addReference(first);
        addedReferences.add(first);
      }

      throw new NodeManagerBatchException(
          StatusCodes.Bad_InternalError,
          "injected mid-batch failure",
          new CommitResult(addedNodes, addedReferences));
    }

    @Override
    public StorageGuarantee getStorageGuarantee() {
      return StorageGuarantee.BEST_EFFORT;
    }
  }

  /** A partial-commit target whose rollback removals also fail. */
  static class RollbackFailingManager extends PartialCommitManager {
    private boolean committed = false;

    @Override
    public CommitResult commit(NodeManagerBatch<UaNode> batch) throws NodeManagerBatchException {
      committed = true;
      return super.commit(batch);
    }

    @Override
    public java.util.Optional<UaNode> removeNode(NodeId nodeId) {
      if (committed) {
        throw new IllegalStateException("injected rollback failure");
      }
      return super.removeNode(nodeId);
    }
  }

  // endregion
}
