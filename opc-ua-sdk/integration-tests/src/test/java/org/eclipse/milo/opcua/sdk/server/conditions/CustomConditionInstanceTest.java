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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemDiagnosticAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationDiagnostic;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationException;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.junit.jupiter.api.Test;

/** Coverage for creating, attaching, and adopting custom Condition types and behaviors. */
public class CustomConditionInstanceTest extends AbstractClientServerTest {

  // A registered generated node class and behavior factory must survive the complete create
  // lifecycle; a post-creation cast would fail too late and leave an unusable instance behind.
  @Test
  void createInstanceConstructsRegisteredCustomNodeAndBehavior() throws Exception {
    VendorType fixture = defineVendorType("RegisteredCreate", true);
    NodeId instanceId = newNodeId("CustomConditionInstanceTest/RegisteredCreate");

    VendorSystemDiagnosticAlarm alarm =
        Condition.createInstance(
            testNamespace.getNodeContext(),
            VendorSystemDiagnosticAlarmTypeNode.class,
            fixture.typeId(),
            builder ->
                builder
                    .nodeId(instanceId)
                    .browseName(newQualifiedName("RegisteredCreate"))
                    .severity(ushort(600)),
            VendorSystemDiagnosticAlarm::new);

    assertEquals(VendorSystemDiagnosticAlarmTypeNode.class, alarm.getNode().getClass());
    assertCustomIdentity(alarm, fixture);
    assertEquals(uint(73), alarm.getNode().getProperty(fixture.memberName()).orElseThrow());

    alarm.setActive(true);
    assertTrue(alarm.isActive());
    assertFalse(alarm.isAcked());
    assertTrue(alarm.isRetained());
  }

  // A custom information-model type does not require a generated Java class: callers can request
  // the nearest stock node class while preserving the custom HasTypeDefinition and EventType.
  @Test
  void createInstanceSupportsCustomTypeWithStockNodeAndBehavior() throws Exception {
    VendorType fixture = defineVendorType("StockBehavior", false);

    SystemDiagnosticAlarm alarm =
        Condition.createInstance(
            testNamespace.getNodeContext(),
            SystemDiagnosticAlarmTypeNode.class,
            fixture.typeId(),
            builder ->
                builder
                    .nodeId(newNodeId("CustomConditionInstanceTest/StockBehavior"))
                    .browseName(newQualifiedName("StockBehavior")),
            SystemDiagnosticAlarm::new);

    assertEquals(SystemDiagnosticAlarmTypeNode.class, alarm.getNode().getClass());
    assertCustomIdentity(alarm, fixture);
    assertEquals(uint(73), alarm.getNode().getProperty(fixture.memberName()).orElseThrow());
  }

  // Typed construction must diagnose a missing custom Java registration before committing any
  // nodes; this replaces the ClassCastException-after-creation failure mode.
  @Test
  void createInstanceRejectsUnregisteredCustomNodeClassWithoutResidue() {
    VendorType fixture = defineVendorType("MissingRegistration", false);
    NodeId instanceId = newNodeId("CustomConditionInstanceTest/MissingRegistration");

    InstantiationException exception =
        assertThrows(
            InstantiationException.class,
            () ->
                Condition.createInstance(
                    testNamespace.getNodeContext(),
                    VendorSystemDiagnosticAlarmTypeNode.class,
                    fixture.typeId(),
                    builder ->
                        builder
                            .nodeId(instanceId)
                            .browseName(newQualifiedName("MissingRegistration")),
                    VendorSystemDiagnosticAlarm::new));

    assertTrue(
        exception.getDiagnostics().stream()
            .anyMatch(
                diagnostic ->
                    diagnostic.code() == InstantiationDiagnostic.Code.INVALID_ROOT_CLASS));
    assertFalse(testNamespace.getNodeManager().containsNode(instanceId));
  }

  // Custom behavior attachment must preserve the exact generated node object rather than
  // replacing it with the stock node class or rebuilding its subtype-specific members.
  @Test
  void attachInstanceWrapsExistingCustomNode() throws Exception {
    VendorType fixture = defineVendorType("Attach", true);
    VendorSystemDiagnosticAlarm original = createVendorAlarm("Attach", fixture);

    VendorSystemDiagnosticAlarm attached =
        Condition.attachInstance(original.getNode(), VendorSystemDiagnosticAlarm::new);

    assertNotSame(original, attached);
    assertSame(original.getNode(), attached.getNode());
    assertCustomIdentity(attached, fixture);
    assertEquals(uint(73), attached.getNode().getProperty(fixture.memberName()).orElseThrow());
  }

  // Custom behavior adoption must complete missing standard state in place while retaining the
  // generated root identity and its custom TypeDefinition.
  @Test
  void adoptInstanceCompletesExistingCustomNodeInPlace() throws Exception {
    VendorType fixture = defineVendorType("Adopt", true);
    VendorSystemDiagnosticAlarm original = createVendorAlarm("Adopt", fixture);
    UaNode deletedEnabledState = requireNonNull(original.getNode().getEnabledStateNode());
    deletedEnabledState.delete();

    VendorSystemDiagnosticAlarm adopted =
        Condition.adoptInstance(
            testNamespace.getNodeContext(),
            original.getConditionId(),
            VendorSystemDiagnosticAlarmTypeNode.class,
            builder -> {},
            VendorSystemDiagnosticAlarm::new);

    assertSame(original.getNode(), adopted.getNode());
    assertNotSame(deletedEnabledState, requireNonNull(adopted.getNode().getEnabledStateNode()));
    assertCustomIdentity(adopted, fixture);
    assertEquals(uint(73), adopted.getNode().getProperty(fixture.memberName()).orElseThrow());
  }

  // A behaviorFactory returning null violates its contract; the failure must surface through the
  // documented UaException channel and roll back the already-committed instance tree.
  @Test
  void createInstanceRejectsNullBehaviorFactoryResultWithoutResidue() {
    VendorType fixture = defineVendorType("NullFactoryResult", true);
    NodeId instanceId = newNodeId("CustomConditionInstanceTest/NullFactoryResult");

    //noinspection DataFlowIssue
    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                Condition.createInstance(
                    testNamespace.getNodeContext(),
                    VendorSystemDiagnosticAlarmTypeNode.class,
                    fixture.typeId(),
                    builder ->
                        builder
                            .nodeId(instanceId)
                            .browseName(newQualifiedName("NullFactoryResult")),
                    node -> null));

    assertEquals(StatusCodes.Bad_InternalError, exception.getStatusCode().getValue());
    assertFalse(
        testNamespace.getNodeManager().containsNode(instanceId),
        "rejected instance must not remain in the address space");
  }

  // Failed creation must undo only the source wiring it added: the source must not remain in the
  // notifier hierarchy, and a HasCondition reference that existed before creation must survive.
  @Test
  void createInstanceFailureRollsBackOnlyOwnedSourceWiring() {
    VendorType fixture = defineVendorType("SourceWiringRollback", true);
    NodeId instanceId = newNodeId("CustomConditionInstanceTest/SourceWiringRollback");
    NodeId sourceId = newNodeId("CustomConditionInstanceTest/SourceWiringRollbackSource");
    UaObjectNode source =
        new UaObjectNode(
            testNamespace.getNodeContext(),
            sourceId,
            newQualifiedName("SourceWiringRollbackSource"),
            LocalizedText.english("SourceWiringRollbackSource"),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            ubyte(0));
    source.addReference(
        new Reference(
            sourceId, NodeIds.HasTypeDefinition, NodeIds.BaseObjectType.expanded(), true));
    testNamespace.getNodeManager().addNode(source);

    Reference hasCondition =
        new Reference(sourceId, NodeIds.HasCondition, instanceId.expanded(), true);
    Reference hasEventSource =
        new Reference(NodeIds.Server, NodeIds.HasEventSource, sourceId.expanded(), true);
    testNamespace.getNodeManager().addReferences(hasCondition, server.getNamespaceTable());

    assertFalse(
        server.getAddressSpaceManager().getManagedReferences(NodeIds.Server).stream()
            .anyMatch(hasEventSource::equals));

    //noinspection DataFlowIssue
    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                Condition.createInstance(
                    testNamespace.getNodeContext(),
                    VendorSystemDiagnosticAlarmTypeNode.class,
                    fixture.typeId(),
                    builder ->
                        builder
                            .nodeId(instanceId)
                            .browseName(newQualifiedName("SourceWiringRollback"))
                            .conditionSource(source),
                    node -> null));

    assertEquals(StatusCodes.Bad_InternalError, exception.getStatusCode().getValue());
    assertFalse(testNamespace.getNodeManager().containsNode(instanceId));
    assertEquals(
        1,
        testNamespace.getNodeManager().getReferences(sourceId).stream()
            .filter(hasCondition::equals)
            .count(),
        "pre-existing HasCondition must survive rollback");
    assertFalse(
        server.getAddressSpaceManager().getManagedReferences(NodeIds.Server).stream()
            .anyMatch(hasEventSource::equals),
        "rollback must remove the HasEventSource it added");
  }

  // A behaviorFactory that wraps some other node would return behavior with no control over the
  // created instance; the create must fail and delete the committed tree, leaving the foreign
  // condition untouched.
  @Test
  void createInstanceRejectsBehaviorWrappingForeignNodeWithoutResidue() throws Exception {
    VendorType fixture = defineVendorType("ForeignNode", true);
    VendorSystemDiagnosticAlarm decoy = createVendorAlarm("ForeignNodeDecoy", fixture);
    NodeId instanceId = newNodeId("CustomConditionInstanceTest/ForeignNode");

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                Condition.createInstance(
                    testNamespace.getNodeContext(),
                    VendorSystemDiagnosticAlarmTypeNode.class,
                    fixture.typeId(),
                    builder ->
                        builder.nodeId(instanceId).browseName(newQualifiedName("ForeignNode")),
                    node -> decoy));

    assertEquals(StatusCodes.Bad_InternalError, exception.getStatusCode().getValue());
    assertFalse(
        testNamespace.getNodeManager().containsNode(instanceId),
        "rejected instance must not remain in the address space");
    assertTrue(
        testNamespace.getNodeManager().containsNode(decoy.getConditionId()),
        "rollback must not touch the foreign condition");
  }

  // Part 9 requires Enable on every Condition instance; attachment must reject an incomplete
  // method surface before any behavior wraps the instance.
  @Test
  void attachInstanceRejectsInvalidMethodSurface() throws Exception {
    VendorType fixture = defineVendorType("InvalidSurface", true);
    VendorSystemDiagnosticAlarm original = createVendorAlarm("InvalidSurface", fixture);
    requireNonNull(original.getNode().getEnableMethodNode()).delete();

    UaRuntimeException exception =
        assertThrows(
            UaRuntimeException.class,
            () -> Condition.attachInstance(original.getNode(), VendorSystemDiagnosticAlarm::new));

    assertEquals(StatusCodes.Bad_InvalidState, exception.getStatusCode().getValue());
  }

  private VendorSystemDiagnosticAlarm createVendorAlarm(String name, VendorType fixture)
      throws UaException {
    return Condition.createInstance(
        testNamespace.getNodeContext(),
        VendorSystemDiagnosticAlarmTypeNode.class,
        fixture.typeId(),
        builder ->
            builder
                .nodeId(newNodeId("CustomConditionInstanceTest/" + name))
                .browseName(newQualifiedName(name)),
        VendorSystemDiagnosticAlarm::new);
  }

  private VendorType defineVendorType(String name, boolean registerNodeClass) {
    NodeId typeId = newNodeId("CustomConditionInstanceTest/" + name + "Type");
    UaObjectTypeNode typeNode =
        VendorTypeFixtures.defineVendorSubtype(
            testNamespace.getNodeContext(),
            testNamespace.getNodeManager(),
            typeId,
            newQualifiedName(name + "Type"),
            NodeIds.SystemDiagnosticAlarmType);

    QualifiedName memberName = newQualifiedName("DiagnosticCode");
    VendorTypeFixtures.declareMandatoryUInt32Member(
        testNamespace.getNodeManager(),
        typeNode,
        newNodeId("CustomConditionInstanceTest/" + name + "Type/DiagnosticCode"),
        memberName,
        NodeIds.PropertyType,
        NodeIds.HasProperty,
        uint(73));

    if (registerNodeClass) {
      server
          .getObjectTypeManager()
          .registerObjectType(
              typeId,
              VendorSystemDiagnosticAlarmTypeNode.class,
              VendorSystemDiagnosticAlarmTypeNode::new);
    }

    return new VendorType(typeId, memberName);
  }

  private static void assertCustomIdentity(Condition condition, VendorType fixture) {
    assertEquals(fixture.typeId(), condition.getNode().getEventType());
    assertTrue(
        condition.getNode().getReferences().stream()
            .anyMatch(
                reference ->
                    reference.isForward()
                        && NodeIds.HasTypeDefinition.equals(reference.getReferenceTypeId())
                        && fixture.typeId().expanded().equals(reference.getTargetNodeId())));
  }

  private record VendorType(NodeId typeId, QualifiedName memberName) {}

  private static final class VendorSystemDiagnosticAlarm extends SystemDiagnosticAlarm {

    private VendorSystemDiagnosticAlarm(VendorSystemDiagnosticAlarmTypeNode node) {
      super(node);
    }

    @Override
    public VendorSystemDiagnosticAlarmTypeNode getNode() {
      return (VendorSystemDiagnosticAlarmTypeNode) super.getNode();
    }
  }

  private static final class VendorSystemDiagnosticAlarmTypeNode
      extends SystemDiagnosticAlarmTypeNode {

    private VendorSystemDiagnosticAlarmTypeNode(
        UaNodeContext context,
        NodeId nodeId,
        QualifiedName browseName,
        LocalizedText displayName,
        LocalizedText description,
        UInteger writeMask,
        UInteger userWriteMask,
        RolePermissionType[] rolePermissions,
        RolePermissionType[] userRolePermissions,
        AccessRestrictionType accessRestrictions) {
      super(
          context,
          nodeId,
          browseName,
          displayName,
          description,
          writeMask,
          userWriteMask,
          rolePermissions,
          userRolePermissions,
          accessRestrictions);
    }
  }
}
