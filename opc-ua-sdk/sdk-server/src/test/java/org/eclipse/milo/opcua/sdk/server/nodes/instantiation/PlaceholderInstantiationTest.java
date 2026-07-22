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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for placeholder-driven creation: request-time expansion binding realizations to a
 * placeholder path, MandatoryPlaceholder cardinality, post-hoc {@code forPlaceholder} creation
 * under an existing instance, and ExposesItsArray's surfaced-but-never-materialized contract — all
 * flowing through the same plan/apply machinery as ordinary requests.
 */
public class PlaceholderInstantiationTest {

  private TypeFixtures fx;
  private UaNodeManager target;

  @BeforeEach
  void setUp() {
    fx = TypeFixtures.create();
    target = fx.newTargetManager();
  }

  /** An ObjectType with one Mandatory Variable {@code Setpoint} and one Optional {@code Units}. */
  private UaObjectTypeNode channelType() {
    UaObjectTypeNode channelType = fx.addObjectType("ChannelType", NodeIds.BaseObjectType);
    fx.addVariableDeclaration(
        channelType, "Setpoint", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
    fx.addVariableDeclaration(
        channelType, "Units", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);
    return channelType;
  }

  /**
   * An ObjectType with one Mandatory Variable {@code Status} and an OptionalPlaceholder {@code
   * <Channel>} of {@code channelTypeId}, attached by {@code placeholderReferenceTypeId}.
   */
  private UaObjectTypeNode deviceType(NodeId channelTypeId, NodeId placeholderReferenceTypeId) {
    UaObjectTypeNode deviceType = fx.addObjectType("DeviceType", NodeIds.BaseObjectType);
    fx.addVariableDeclaration(
        deviceType, "Status", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
    fx.addObjectDeclaration(
        deviceType,
        "<Channel>",
        channelTypeId,
        NodeIds.ModellingRule_OptionalPlaceholder,
        placeholderReferenceTypeId);
    return deviceType;
  }

  private InstantiationRequest.Builder<UaObjectNode> objectRequest(NodeId typeId, String rootId) {
    return InstantiationRequest.of(UaObjectNode.class, typeId)
        .nodeId(fx.newNodeId(rootId))
        .target(target);
  }

  private static boolean hasError(InstantiationPlan<?> plan, InstantiationDiagnostic.Code code) {
    return plan.diagnostics().stream()
        .anyMatch(d -> d.severity() == InstantiationDiagnostic.Severity.ERROR && d.code() == code);
  }

  private static long countReferences(
      UaNodeManager manager, NodeId sourceNodeId, NodeId referenceTypeId, NodeId targetNodeId) {

    return manager.getReferences(sourceNodeId).stream()
        .filter(
            r ->
                r.getReferenceTypeId().equals(referenceTypeId)
                    && r.getTargetNodeId().equals(targetNodeId.expanded())
                    && r.isForward())
        .count();
  }

  @Nested
  class Expansion {

    /**
     * Placeholders never auto-instantiate, whatever the selection: a default request (and an
     * all-optionals one) applies with zero realizations and the placeholder accounted for as
     * skipped — expansion to zero members is simply not binding any.
     */
    @Test
    void placeholdersNeverAutoInstantiate() throws Exception {
      UaObjectTypeNode channelType = channelType();
      UaObjectTypeNode deviceType = deviceType(channelType.getNodeId(), NodeIds.HasComponent);

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(deviceType.getNodeId(), "Dev").includeAllOptionals().build());

      assertTrue(result.node(path("<Channel>")).isEmpty());
      assertEquals(
          SkippedDeclaration.Reason.PLACEHOLDER,
          result.skippedDeclarations().stream()
              .filter(s -> s.declaration().browsePath().equals(path("<Channel>")))
              .findFirst()
              .orElseThrow()
              .reason());

      // Exactly the root and Status were committed; nothing placeholder-derived exists.
      assertEquals(2, target.getNodes().size());
    }

    /** Expansion binds N realizations: names, pinned ids, and the placeholder's attachment. */
    @Test
    void optionalPlaceholderExpandsToNMembers() throws Exception {
      UaObjectTypeNode channelType = channelType();
      UaObjectTypeNode deviceType = deviceType(channelType.getNodeId(), NodeIds.HasComponent);

      NodeId pinnedId = fx.newNodeId("pinned-channel-2");

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(deviceType.getNodeId(), "Dev")
                      .expandPlaceholder(
                          path("<Channel>"),
                          PlaceholderRealization.of(qn("Channel1")),
                          PlaceholderRealization.of(qn("Channel2")).withNodeId(pinnedId))
                      .build());

      UaObjectNode channel1 = result.require(path("Channel1"), UaObjectNode.class);
      UaObjectNode channel2 = result.require(path("Channel2"), UaObjectNode.class);

      assertEquals(qn("Channel1"), channel1.getBrowseName());
      assertEquals("Channel1", channel1.getDisplayName().getText());
      assertEquals(pinnedId, channel2.getNodeId());

      // The full member hierarchy of the placeholder's type is realized beneath each realization:
      // Mandatory members exist, Optional members follow ordinary selection (not selected here).
      assertTrue(result.node(path("Channel1", "Setpoint")).isPresent());
      assertTrue(result.node(path("Channel2", "Setpoint")).isPresent());
      assertTrue(result.node(path("Channel1", "Units")).isEmpty());

      // Each realization attaches with the placeholder's own reference and carries the
      // placeholder's type.
      NodeId rootNodeId = result.root().getNodeId();
      assertEquals(
          1, countReferences(target, rootNodeId, NodeIds.HasComponent, channel1.getNodeId()));
      assertEquals(
          1,
          countReferences(
              target, channel1.getNodeId(), NodeIds.HasTypeDefinition, channelType.getNodeId()));

      // The placeholder itself is still accounted for as skipped, not silently absent.
      assertTrue(
          result.skippedDeclarations().stream()
              .anyMatch(
                  s ->
                      s.declaration().browsePath().equals(path("<Channel>"))
                          && s.reason() == SkippedDeclaration.Reason.PLACEHOLDER));
    }

    /** Optional members inside a realized subtree are selectable like any other path. */
    @Test
    void optionalMemberInsideRealizedSubtreeIsSelectable() throws Exception {
      UaObjectTypeNode channelType = channelType();
      UaObjectTypeNode deviceType = deviceType(channelType.getNodeId(), NodeIds.HasComponent);

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(deviceType.getNodeId(), "Dev")
                      .expandPlaceholder(
                          path("<Channel>"), PlaceholderRealization.of(qn("Channel1")))
                      .includeOptional(path("Channel1", "Units"))
                      .build());

      assertTrue(result.node(path("Channel1", "Units")).isPresent());
      assertFalse(
          result.diagnostics().stream()
              .anyMatch(d -> d.code() == InstantiationDiagnostic.Code.UNMATCHED_PATH));
    }

    /**
     * A realization may instantiate a concrete subtype of the placeholder's declared type; the
     * subtype's full hierarchy — inherited and additional members — is realized, and the
     * realization's HasTypeDefinition names the subtype.
     */
    @Test
    void realizationWithConcreteSubtype() throws Exception {
      UaObjectTypeNode channelType = channelType();
      UaObjectTypeNode specialType =
          fx.addObjectType("SpecialChannelType", channelType.getNodeId());
      fx.addVariableDeclaration(
          specialType, "Gain", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      UaObjectTypeNode deviceType = deviceType(channelType.getNodeId(), NodeIds.HasComponent);

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(deviceType.getNodeId(), "Dev")
                      .expandPlaceholder(
                          path("<Channel>"),
                          PlaceholderRealization.of(qn("ChannelX"))
                              .withConcreteType(specialType.getNodeId()))
                      .build());

      UaObjectNode channelX = result.require(path("ChannelX"), UaObjectNode.class);

      assertTrue(result.node(path("ChannelX", "Setpoint")).isPresent());
      assertTrue(result.node(path("ChannelX", "Gain")).isPresent());
      assertEquals(
          1,
          countReferences(
              target, channelX.getNodeId(), NodeIds.HasTypeDefinition, specialType.getNodeId()));
    }

    /** A realization's concrete type must be the declared placeholder type or a subtype of it. */
    @Test
    void realizationWithInvalidConcreteTypeIsPlanError() throws Exception {
      UaObjectTypeNode channelType = channelType();
      UaObjectTypeNode unrelatedType = fx.addObjectType("UnrelatedType", NodeIds.BaseObjectType);
      UaObjectTypeNode deviceType = deviceType(channelType.getNodeId(), NodeIds.HasComponent);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(deviceType.getNodeId(), "Dev")
                      .expandPlaceholder(
                          path("<Channel>"),
                          PlaceholderRealization.of(qn("Channel1"))
                              .withConcreteType(unrelatedType.getNodeId()))
                      .build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.CONCRETE_TYPE_INVALID));
    }

    /** Expansion is a directive to create: a binding that matches nothing is a plan error. */
    @Test
    void unmatchedExpansionPathIsPlanError() throws Exception {
      UaObjectTypeNode channelType = channelType();
      UaObjectTypeNode deviceType = deviceType(channelType.getNodeId(), NodeIds.HasComponent);

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(deviceType.getNodeId(), "Dev")
                      .expandPlaceholder(
                          path("<Mistyped>"), PlaceholderRealization.of(qn("Channel1")))
                      .build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.PLACEHOLDER_EXPANSION_INVALID));
    }

    /** A realization may not shadow a declared sibling or repeat another realization's name. */
    @Test
    void realizationNameCollisionsArePlanErrors() throws Exception {
      UaObjectTypeNode channelType = channelType();
      UaObjectTypeNode deviceType = deviceType(channelType.getNodeId(), NodeIds.HasComponent);

      InstantiationPlan<UaObjectNode> siblingCollision =
          fx.instantiator()
              .plan(
                  objectRequest(deviceType.getNodeId(), "Dev")
                      .expandPlaceholder(path("<Channel>"), PlaceholderRealization.of(qn("Status")))
                      .build());

      assertTrue(
          hasError(siblingCollision, InstantiationDiagnostic.Code.PLACEHOLDER_EXPANSION_INVALID));

      InstantiationPlan<UaObjectNode> duplicateNames =
          fx.instantiator()
              .plan(
                  objectRequest(deviceType.getNodeId(), "Dev")
                      .expandPlaceholder(
                          path("<Channel>"),
                          PlaceholderRealization.of(qn("Channel1")),
                          PlaceholderRealization.of(qn("Channel1")))
                      .build());

      assertTrue(
          hasError(duplicateNames, InstantiationDiagnostic.Code.PLACEHOLDER_EXPANSION_INVALID));
    }

    /**
     * A placeholder nested inside a realized subtree is expandable by its realized path, and its
     * MandatoryPlaceholder obligation applies there like anywhere else.
     */
    @Test
    void nestedPlaceholderInsideRealizedSubtree() throws Exception {
      UaObjectTypeNode signalType = fx.addObjectType("SignalType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          signalType, "Raw", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode channelType = fx.addObjectType("SigChannelType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          channelType,
          "<Signal>",
          signalType.getNodeId(),
          NodeIds.ModellingRule_MandatoryPlaceholder);

      UaObjectTypeNode deviceType = fx.addObjectType("SigDeviceType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          deviceType,
          "<Channel>",
          channelType.getNodeId(),
          NodeIds.ModellingRule_OptionalPlaceholder);

      // Realizing the channel without satisfying its own MandatoryPlaceholder fails the plan.
      InstantiationPlan<UaObjectNode> unsatisfied =
          fx.instantiator()
              .plan(
                  objectRequest(deviceType.getNodeId(), "Dev")
                      .expandPlaceholder(path("<Channel>"), PlaceholderRealization.of(qn("Ch1")))
                      .build());

      assertTrue(
          hasError(unsatisfied, InstantiationDiagnostic.Code.MANDATORY_PLACEHOLDER_UNSATISFIED));

      // Binding the nested placeholder by its realized path satisfies it end to end.
      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(deviceType.getNodeId(), "Dev")
                      .expandPlaceholder(path("<Channel>"), PlaceholderRealization.of(qn("Ch1")))
                      .expandPlaceholder(
                          path("Ch1", "<Signal>"), PlaceholderRealization.of(qn("Sig1")))
                      .build());

      assertTrue(result.node(path("Ch1", "Sig1")).isPresent());
      assertTrue(result.node(path("Ch1", "Sig1", "Raw")).isPresent());
    }
  }

  @Nested
  class MandatoryCardinality {

    private UaObjectTypeNode machineType(NodeId channelTypeId) {
      UaObjectTypeNode machineType = fx.addObjectType("MachineType", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          machineType, "<Slot>", channelTypeId, NodeIds.ModellingRule_MandatoryPlaceholder);
      return machineType;
    }

    /** MandatoryPlaceholder with zero bindings fails the plan, and apply refuses the plan. */
    @Test
    void zeroRealizationsFailsThePlan() throws Exception {
      UaObjectTypeNode machineType = machineType(channelType().getNodeId());

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator().plan(objectRequest(machineType.getNodeId(), "M1").build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.MANDATORY_PLACEHOLDER_UNSATISFIED));

      InstantiationException e =
          assertThrows(InstantiationException.class, () -> fx.instantiator().apply(plan));
      assertTrue(
          e.getDiagnostics().stream()
              .anyMatch(
                  d -> d.code() == InstantiationDiagnostic.Code.MANDATORY_PLACEHOLDER_UNSATISFIED));

      assertTrue(target.getNodes().isEmpty());
    }

    /** One realization satisfies the ≥1 obligation. */
    @Test
    void oneRealizationSatisfiesTheObligation() throws Exception {
      UaObjectTypeNode machineType = machineType(channelType().getNodeId());

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(machineType.getNodeId(), "M1")
                      .expandPlaceholder(path("<Slot>"), PlaceholderRealization.of(qn("Slot1")))
                      .build());

      assertTrue(result.node(path("Slot1")).isPresent());
      assertTrue(result.node(path("Slot1", "Setpoint")).isPresent());
    }

    /** Excluding a MandatoryPlaceholder does not silence its obligation. */
    @Test
    void excludedMandatoryPlaceholderFailsThePlan() throws Exception {
      UaObjectTypeNode machineType = machineType(channelType().getNodeId());

      InstantiationPlan<UaObjectNode> plan =
          fx.instantiator()
              .plan(
                  objectRequest(machineType.getNodeId(), "M1")
                      .excludeOptional(path("<Slot>"))
                      .expandPlaceholder(path("<Slot>"), PlaceholderRealization.of(qn("Slot1")))
                      .build());

      assertTrue(hasError(plan, InstantiationDiagnostic.Code.MANDATORY_PLACEHOLDER_UNSATISFIED));
      // The bindings could not be honored either — surfaced, not silently dropped.
      assertTrue(hasError(plan, InstantiationDiagnostic.Code.PLACEHOLDER_EXPANSION_INVALID));
    }
  }

  @Nested
  class ForPlaceholder {

    /**
     * Post-hoc creation under an existing instance: the realization attaches with the placeholder
     * declaration's own ReferenceType (Organizes here, proving it is resolved rather than
     * defaulted), member ids flow through the request's strategy, the result is typed, and
     * deleteCreated removes exactly the addition.
     */
    @Test
    void forPlaceholderCreatesUnderExistingInstance() throws Exception {
      UaObjectTypeNode channelType = channelType();
      UaObjectTypeNode deviceType = deviceType(channelType.getNodeId(), NodeIds.Organizes);

      InstantiationResult<UaObjectNode> device =
          fx.instantiator().instantiate(objectRequest(deviceType.getNodeId(), "Dev").build());
      NodeId deviceNodeId = device.root().getNodeId();

      TypeInstantiationModel model = fx.instantiator().describe(deviceType.getNodeId());
      InstanceDeclaration placeholder = model.placeholders().get(0);
      assertEquals(path("<Channel>"), placeholder.browsePath());

      List<BrowsePath> strategyPaths = new ArrayList<>();

      InstantiationResult<UaObjectNode> channel =
          fx.instantiator()
              .instantiate(
                  InstantiationRequest.forPlaceholder(UaObjectNode.class, deviceNodeId, placeholder)
                      .nodeId(fx.newNodeId("Dev/Channel1"))
                      .browseName(qn("Channel1"))
                      .target(target)
                      .nodeIdStrategy(
                          ctx -> {
                            strategyPaths.add(ctx.path());
                            return ctx.defaultNodeId();
                          })
                      .build());

      UaObjectNode channelRoot = channel.root();
      assertEquals(qn("Channel1"), channelRoot.getBrowseName());

      // Attached with the placeholder's own reference type, resolved from the declaration.
      assertEquals(
          1, countReferences(target, deviceNodeId, NodeIds.Organizes, channelRoot.getNodeId()));
      assertEquals(
          1,
          countReferences(
              target, channelRoot.getNodeId(), NodeIds.HasTypeDefinition, channelType.getNodeId()));

      // Member ids went through the request's strategy.
      assertTrue(strategyPaths.contains(path("Setpoint")));
      assertTrue(channel.node(path("Setpoint")).isPresent());

      // Cleanup removes only the addition: the device instance is untouched.
      channel.deleteCreated();
      assertTrue(target.getNode(channelRoot.getNodeId()).isEmpty());
      assertTrue(target.getNode(deviceNodeId).isPresent());
      assertTrue(
          device.node(path("Status")).map(n -> target.containsNode(n.getNodeId())).orElse(false));
      assertEquals(
          0, countReferences(target, deviceNodeId, NodeIds.Organizes, channelRoot.getNodeId()));
    }

    /** A concrete subtype of the placeholder's declared type is selected at the root path. */
    @Test
    void forPlaceholderWithConcreteSubtype() throws Exception {
      UaObjectTypeNode channelType = channelType();
      UaObjectTypeNode specialType =
          fx.addObjectType("SpecialChannelType", channelType.getNodeId());
      fx.addVariableDeclaration(
          specialType, "Gain", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      UaObjectTypeNode deviceType = deviceType(channelType.getNodeId(), NodeIds.HasComponent);

      InstantiationResult<UaObjectNode> device =
          fx.instantiator().instantiate(objectRequest(deviceType.getNodeId(), "Dev").build());

      TypeInstantiationModel model = fx.instantiator().describe(deviceType.getNodeId());
      InstanceDeclaration placeholder = model.placeholders().get(0);

      InstantiationResult<UaObjectNode> channel =
          fx.instantiator()
              .instantiate(
                  InstantiationRequest.forPlaceholder(
                          UaObjectNode.class, device.root().getNodeId(), placeholder)
                      .nodeId(fx.newNodeId("Dev/ChannelX"))
                      .browseName(qn("ChannelX"))
                      .concreteType(BrowsePath.root(), specialType.getNodeId())
                      .target(target)
                      .build());

      assertEquals(
          1,
          countReferences(
              target,
              channel.root().getNodeId(),
              NodeIds.HasTypeDefinition,
              specialType.getNodeId()));
      assertTrue(channel.node(path("Setpoint")).isPresent());
      assertTrue(channel.node(path("Gain")).isPresent());
    }

    /** A non-placeholder declaration is rejected before a request is even built. */
    @Test
    void forPlaceholderRejectsNonPlaceholderDeclarations() throws Exception {
      UaObjectTypeNode channelType = channelType();
      UaObjectTypeNode deviceType = deviceType(channelType.getNodeId(), NodeIds.HasComponent);

      TypeInstantiationModel model = fx.instantiator().describe(deviceType.getNodeId());
      InstanceDeclaration status = model.get(path("Status")).orElseThrow();

      assertThrows(
          IllegalArgumentException.class,
          () -> InstantiationRequest.forPlaceholder(fx.newNodeId("Dev"), status));
    }
  }

  @Nested
  class ExposesItsArray {

    /** ExposesItsArray is visible in the model and warned about, but never materialized. */
    @Test
    void surfacedInModelWarnedInPlanNeverMaterialized() throws Exception {
      UaObjectTypeNode arrayType = fx.addObjectType("ArrayHostType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          arrayType, "Elements", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      fx.addVariableDeclaration(
          arrayType,
          "Element",
          NodeIds.BaseDataVariableType,
          NodeIds.ModellingRule_ExposesItsArray);

      TypeInstantiationModel model = fx.instantiator().describe(arrayType.getNodeId());
      assertEquals(
          ModellingRule.EXPOSES_ITS_ARRAY, model.get(path("Element")).orElseThrow().rule());

      InstantiationResult<UaObjectNode> result =
          fx.instantiator()
              .instantiate(
                  objectRequest(arrayType.getNodeId(), "Host").includeAllOptionals().build());

      assertTrue(
          result.diagnostics().stream()
              .anyMatch(
                  d ->
                      d.code() == InstantiationDiagnostic.Code.EXPOSES_ITS_ARRAY_NOT_MATERIALIZED));
      assertTrue(result.node(path("Element")).isEmpty());
      assertTrue(
          result.skippedDeclarations().stream()
              .anyMatch(
                  s ->
                      s.declaration().browsePath().equals(path("Element"))
                          && s.reason() == SkippedDeclaration.Reason.EXPOSES_ITS_ARRAY));
    }
  }
}
