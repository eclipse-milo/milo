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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.objects.AcknowledgeableConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.MethodInstantiation;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.junit.jupiter.api.Test;

/** Client-visible coverage for attaching complete instances and adopting partial loaded graphs. */
public class ConditionAttachAdoptTest extends AbstractClientServerTest {

  private AcknowledgeableCondition attached;
  private ExclusiveLimitAlarm adopted;
  private UaObjectNode source;
  private NodeId adoptedRootId;
  private NodeId reusedHighLimitId;
  private UaNode deletedEnabledState;
  private NodeId removedRefreshMethodId;
  private NodeId vendorMethodId;

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    namespace.configure(
        (context, nodeManager) -> {
          source =
              new UaObjectNode(
                  context,
                  newNodeId("ConditionAttachAdoptTest/Source"),
                  newQualifiedName("ConditionAttachAdoptTest/Source"),
                  LocalizedText.english("ConditionAttachAdoptTest/Source"),
                  LocalizedText.NULL_VALUE,
                  uint(0),
                  uint(0),
                  ubyte(0));
          source.addReference(
              new Reference(
                  source.getNodeId(),
                  NodeIds.HasTypeDefinition,
                  NodeIds.BaseObjectType.expanded(),
                  true));
          nodeManager.addNode(source);

          try {
            prepareAttachedCondition(context);
            prepareAdoptedAlarm(context);
          } catch (UaException e) {
            throw new RuntimeException(e);
          }
        });
  }

  private void prepareAttachedCondition(
      org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext context) throws UaException {

    AcknowledgeableCondition created =
        AcknowledgeableCondition.create(
            context,
            builder ->
                builder
                    .nodeId(newNodeId("ConditionAttachAdoptTest/Attached"))
                    .browseName(newQualifiedName("Attached"))
                    .severity(ushort(601))
                    .withConfirm());

    AcknowledgeableConditionTypeNode node = created.getNode();
    node.getMethodNodes()
        .forEach(method -> method.setInvocationHandler(MethodInvocationHandler.NOT_IMPLEMENTED));

    UaMethodNode refreshMethod =
        addMethod(
            context,
            node,
            newNodeId("ConditionAttachAdoptTest/Attached/ConditionRefresh"),
            new QualifiedName(0, "ConditionRefresh"));
    removedRefreshMethodId = refreshMethod.getNodeId();

    UaMethodNode vendorMethod =
        addMethod(
            context,
            node,
            newNodeId("ConditionAttachAdoptTest/Attached/VendorAcknowledge"),
            newQualifiedName("Acknowledge"));
    vendorMethodId = vendorMethod.getNodeId();

    AcknowledgeableCondition.attach(node, options -> options.conditionSource(source));
    attached = AcknowledgeableCondition.attach(node, options -> options.conditionSource(source));

    server.getConditionManager().register(attached);
  }

  private void prepareAdoptedAlarm(org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext context)
      throws UaException {

    ExclusiveLimitAlarm created =
        ExclusiveLimitAlarm.create(
            context,
            builder ->
                builder
                    .nodeId(newNodeId("ConditionAttachAdoptTest/Adopted"))
                    .browseName(newQualifiedName("Adopted"))
                    .severity(ushort(602))
                    .highLimit(10.0, ushort(700))
                    .lowLimit(0.0));

    ExclusiveLimitAlarmTypeNode node = created.getNode();
    adoptedRootId = node.getNodeId();
    reusedHighLimitId = requireNonNull(node.getHighLimitNode()).getNodeId();

    deletedEnabledState = requireNonNull(node.getEnabledStateNode());
    deletedEnabledState.delete();
    requireNonNull(node.getEnableMethodNode()).delete();

    adopted = ExclusiveLimitAlarm.adopt(context, adoptedRootId, builder -> builder.highLimit(20.0));

    server.getConditionManager().register(adopted);
  }

  private UaMethodNode addMethod(
      org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext context,
      UaObjectNode parent,
      NodeId nodeId,
      QualifiedName browseName) {

    UaMethodNode method =
        UaMethodNode.build(
            context,
            builder ->
                builder
                    .setNodeId(nodeId)
                    .setBrowseName(browseName)
                    .setDisplayName(LocalizedText.english(browseName.name()))
                    .buildAndAdd());
    parent.addComponent(method);
    return method;
  }

  @Test
  void attachInstallsHandlersAndWiresSourceIdempotently() throws Exception {
    assertEquals(source.getNodeId(), attached.getNode().getSourceNode());
    assertTrue(testNamespace.getNodeManager().getNode(removedRefreshMethodId).isEmpty());
    assertTrue(testNamespace.getNodeManager().getNode(vendorMethodId).isPresent());

    long hasConditionReferences =
        source.getReferences().stream()
            .filter(reference -> NodeIds.HasCondition.equals(reference.getReferenceTypeId()))
            .filter(Reference::isForward)
            .filter(
                reference ->
                    reference.getTargetNodeId().equals(attached.getConditionId().expanded()))
            .count();
    assertEquals(1, hasConditionReferences);

    attached.setAcked(false);
    ByteString eventId = attached.currentBranch().getLastEventId();

    CallMethodResult viaInstance =
        call(
            attached.getConditionId(),
            requireNonNull(attached.getNode().getAcknowledgeMethodNode()).getNodeId(),
            new Variant(eventId),
            new Variant(LocalizedText.english("attached")));
    assertTrue(requireNonNull(viaInstance.getStatusCode()).isGood());

    attached.setConfirmed(true);
    attached.setAcked(false);
    CallMethodResult viaType =
        call(
            attached.getConditionId(),
            NodeIds.AcknowledgeableConditionType_Acknowledge,
            new Variant(attached.currentBranch().getLastEventId()),
            new Variant(LocalizedText.NULL_VALUE));
    assertTrue(requireNonNull(viaType.getStatusCode()).isGood());
  }

  @Test
  void adoptReusesDeclaredNodesCompletesMissingStructureAndPreservesValues() throws Exception {
    assertEquals(adoptedRootId, adopted.getConditionId());
    assertEquals(
        reusedHighLimitId, requireNonNull(adopted.getNode().getHighLimitNode()).getNodeId());
    assertEquals(20.0, adopted.getNode().getHighLimit());
    assertEquals(0.0, adopted.getNode().getLowLimit());
    assertEquals(ushort(602), adopted.getNode().getSeverity());
    assertEquals(ushort(700), adopted.getNode().getSeverityHigh());

    UaNode completedEnabledState = requireNonNull(adopted.getNode().getEnabledStateNode());
    assertNotNull(completedEnabledState);
    assertNotSame(deletedEnabledState, completedEnabledState);

    CallMethodResult disable = call(adoptedRootId, NodeIds.ConditionType_Disable);
    assertTrue(requireNonNull(disable.getStatusCode()).isGood());

    CallMethodResult enable =
        call(adoptedRootId, requireNonNull(adopted.getNode().getEnableMethodNode()).getNodeId());
    assertTrue(requireNonNull(enable.getStatusCode()).isGood());
  }

  @Test
  void adoptRejectsIdentityChangesBeforeMutation() {
    NodeId enabledStateId = requireNonNull(adopted.getNode().getEnabledStateNode()).getNodeId();

    assertThrows(
        IllegalArgumentException.class,
        () ->
            ExclusiveLimitAlarm.adopt(
                testNamespace.getNodeContext(),
                adoptedRootId,
                builder -> builder.nodeId(newNodeId("ConditionAttachAdoptTest/Replaced"))));

    assertEquals(
        enabledStateId, requireNonNull(adopted.getNode().getEnabledStateNode()).getNodeId());
  }

  @Test
  void attachRejectsDuplicateExpectedMethodNames() throws Exception {
    AcknowledgeableCondition duplicate =
        AcknowledgeableCondition.create(
            testNamespace.getNodeContext(),
            builder ->
                builder
                    .nodeId(newNodeId("ConditionAttachAdoptTest/Duplicate"))
                    .browseName(newQualifiedName("Duplicate")));

    addMethod(
        testNamespace.getNodeContext(),
        duplicate.getNode(),
        newNodeId("ConditionAttachAdoptTest/Duplicate/Acknowledge2"),
        new QualifiedName(0, "Acknowledge"));

    assertThrows(
        UaRuntimeException.class, () -> AcknowledgeableCondition.attach(duplicate.getNode()));
  }

  @Test
  void reAdoptionDoesNotDuplicateNodes() throws Exception {
    var before = ConditionNodeTraversal.discoverAssignedNodeIds(adopted.getNode());
    CallMethodResult disable = call(adoptedRootId, NodeIds.ConditionType_Disable);
    assertTrue(requireNonNull(disable.getStatusCode()).isGood());

    ExclusiveLimitAlarm replacement =
        ExclusiveLimitAlarm.adopt(testNamespace.getNodeContext(), adoptedRootId, builder -> {});

    var after = ConditionNodeTraversal.discoverAssignedNodeIds(replacement.getNode());
    assertEquals(before, after);
    assertEquals(20.0, replacement.getNode().getHighLimit());
    assertEquals(0.0, replacement.getNode().getLowLimit());

    server.getConditionManager().register(replacement);
    adopted = replacement;

    CallMethodResult enable = call(adoptedRootId, NodeIds.ConditionType_Enable);
    assertTrue(requireNonNull(enable.getStatusCode()).isGood());
    assertEquals(
        1,
        behaviorFilterCount(
            requireNonNull(replacement.getNode().getQualityNode()),
            Condition.BehaviorFilterKind.DISABLED_READ));
  }

  @Test
  void adoptionPreflightRejectsMissingWrongTypeAndInvalidStoredLimits() throws Exception {
    assertThrows(
        UaException.class,
        () ->
            AlarmCondition.adopt(
                testNamespace.getNodeContext(),
                newNodeId("ConditionAttachAdoptTest/Missing"),
                builder -> {}));
    assertThrows(
        UaException.class,
        () ->
            AlarmCondition.adopt(
                testNamespace.getNodeContext(), source.getNodeId(), builder -> {}));

    ExclusiveLimitAlarm invalid =
        ExclusiveLimitAlarm.create(
            testNamespace.getNodeContext(),
            builder ->
                builder
                    .nodeId(newNodeId("ConditionAttachAdoptTest/InvalidLimits"))
                    .browseName(newQualifiedName("InvalidLimits"))
                    .highLimit(10.0)
                    .lowLimit(0.0));
    invalid.getNode().setHighLimit(-1.0);
    invalid.getNode().setLowLimit(1.0);
    var before = ConditionNodeTraversal.discoverAssignedNodeIds(invalid.getNode());

    assertThrows(
        IllegalArgumentException.class,
        () ->
            ExclusiveLimitAlarm.adopt(
                testNamespace.getNodeContext(), invalid.getConditionId(), builder -> {}));

    assertEquals(before, ConditionNodeTraversal.discoverAssignedNodeIds(invalid.getNode()));
  }

  @Test
  void adoptionCompletesOptionalFeatureCompanions() throws Exception {
    AcknowledgeableCondition partial =
        AcknowledgeableCondition.create(
            testNamespace.getNodeContext(),
            builder ->
                builder
                    .nodeId(newNodeId("ConditionAttachAdoptTest/PartialConfirm"))
                    .browseName(newQualifiedName("PartialConfirm"))
                    .withConfirm());

    NodeId confirmedStateId = requireNonNull(partial.getNode().getConfirmedStateNode()).getNodeId();
    UaMethodNode deletedConfirm = requireNonNull(partial.getNode().getConfirmMethodNode());
    deletedConfirm.delete();

    AcknowledgeableCondition completed =
        AcknowledgeableCondition.adopt(
            testNamespace.getNodeContext(), partial.getConditionId(), builder -> {});

    assertEquals(
        confirmedStateId, requireNonNull(completed.getNode().getConfirmedStateNode()).getNodeId());
    assertNotSame(deletedConfirm, requireNonNull(completed.getNode().getConfirmMethodNode()));
  }

  @Test
  void baseBehaviorAdoptionPreservesGeneratedSubtypeAndEventType() throws Exception {
    ExclusiveLimitAlarm loaded =
        ExclusiveLimitAlarm.create(
            testNamespace.getNodeContext(),
            builder ->
                builder
                    .nodeId(newNodeId("ConditionAttachAdoptTest/BaseBehaviorSubtype"))
                    .browseName(newQualifiedName("BaseBehaviorSubtype"))
                    .highLimit(10.0));
    ExclusiveLimitAlarmTypeNode loadedNode = loaded.getNode();
    requireNonNull(loadedNode.getHighLimitNode()).delete();

    AlarmCondition genericBehavior =
        AlarmCondition.adopt(
            testNamespace.getNodeContext(), loaded.getConditionId(), builder -> {});

    assertEquals(AlarmCondition.class, genericBehavior.getClass());
    assertSame(loadedNode, genericBehavior.getNode());
    assertEquals(NodeIds.ExclusiveLimitAlarmType, genericBehavior.getNode().getEventType());
    assertTrue(
        genericBehavior.getNode().getReferences().stream()
            .anyMatch(
                reference ->
                    reference.isForward()
                        && NodeIds.HasTypeDefinition.equals(reference.getReferenceTypeId())
                        && NodeIds.ExclusiveLimitAlarmType.expanded()
                            .equals(reference.getTargetNodeId())));
    assertNull(((ExclusiveLimitAlarmTypeNode) genericBehavior.getNode()).getHighLimit());
  }

  @Test
  void severityOnlyAdoptionUsesStoredLimitWithoutReplacingIt() throws Exception {
    ExclusiveLimitAlarm loaded =
        ExclusiveLimitAlarm.create(
            testNamespace.getNodeContext(),
            builder ->
                builder
                    .nodeId(newNodeId("ConditionAttachAdoptTest/SeverityOnly"))
                    .browseName(newQualifiedName("SeverityOnly"))
                    .severity(ushort(725))
                    .highLimit(42.0));
    NodeId highLimitId = requireNonNull(loaded.getNode().getHighLimitNode()).getNodeId();

    ExclusiveLimitAlarm configured =
        ExclusiveLimitAlarm.adopt(
            testNamespace.getNodeContext(),
            loaded.getConditionId(),
            builder -> builder.severity(ushort(1)).highSeverity(ushort(725)));

    assertEquals(highLimitId, requireNonNull(configured.getNode().getHighLimitNode()).getNodeId());
    assertEquals(42.0, configured.getNode().getHighLimit());
    assertEquals(ushort(1), configured.getNode().getSeverity());
    assertEquals(ushort(725), configured.getNode().getSeverityHigh());
  }

  @Test
  void severityOnlyConfigurationRequiresCorrespondingStoredOrConfiguredLimit() throws Exception {
    ExclusiveLimitAlarm lowOnly =
        ExclusiveLimitAlarm.create(
            testNamespace.getNodeContext(),
            builder ->
                builder
                    .nodeId(newNodeId("ConditionAttachAdoptTest/SeverityWithoutLimit"))
                    .browseName(newQualifiedName("SeverityWithoutLimit"))
                    .lowLimit(0.0));
    var before = ConditionNodeTraversal.discoverAssignedNodeIds(lowOnly.getNode());

    assertThrows(
        IllegalArgumentException.class,
        () ->
            ExclusiveLimitAlarm.adopt(
                testNamespace.getNodeContext(),
                lowOnly.getConditionId(),
                builder -> builder.highSeverity(ushort(700))));
    assertEquals(before, ConditionNodeTraversal.discoverAssignedNodeIds(lowOnly.getNode()));
    assertNull(lowOnly.getNode().getSeverityHigh());

    assertThrows(
        IllegalArgumentException.class,
        () ->
            ExclusiveLimitAlarm.create(
                testNamespace.getNodeContext(),
                builder ->
                    builder
                        .nodeId(newNodeId("ConditionAttachAdoptTest/CreateSeverityWithoutLimit"))
                        .browseName(newQualifiedName("CreateSeverityWithoutLimit"))
                        .highSeverity(ushort(700))));
  }

  // A persisted current EventId represents the loaded acknowledgement obligation; discarding it
  // makes the first operator action fail and silently changes identity during refresh.
  @Test
  void loadedCurrentEventIdRemainsActionableAndStableAcrossFirstRefresh() throws Exception {
    ByteString loadedEventId = ByteString.of(new byte[] {1, 3, 3, 7});
    DateTime loadedTime = DateTime.now();
    AcknowledgeableConditionTypeNode node =
        rawAcknowledgeable(
            "LoadedEventIdentity", MethodInstantiation.COPY, true, loadedEventId, loadedTime);
    setTwoState(requireNonNull(node.getAckedStateNode()), false, "Unacknowledged", loadedTime);
    setTwoState(requireNonNull(node.getConfirmedStateNode()), false, "Unconfirmed", loadedTime);

    AcknowledgeableCondition condition = AcknowledgeableCondition.attach(node);
    server.getConditionManager().register(condition);

    assertTrue(condition.findBranch(loadedEventId).isPresent());

    List<ConditionEventSnapshot> snapshots = condition.createRefreshSnapshots();
    try {
      assertEquals(1, snapshots.size());
      assertEquals(loadedEventId, snapshots.get(0).getEventNode().getEventId());
      assertEquals(loadedEventId, condition.currentBranch().getLastEventId());
    } finally {
      snapshots.forEach(ConditionEventSnapshot::delete);
    }

    CallMethodResult acknowledge =
        call(
            condition.getConditionId(),
            requireNonNull(node.getAcknowledgeMethodNode()).getNodeId(),
            new Variant(loadedEventId),
            new Variant(LocalizedText.NULL_VALUE));
    assertTrue(requireNonNull(acknowledge.getStatusCode()).isGood());

    CallMethodResult confirm =
        call(
            condition.getConditionId(),
            requireNonNull(node.getConfirmMethodNode()).getNodeId(),
            new Variant(loadedEventId),
            new Variant(LocalizedText.NULL_VALUE));
    assertTrue(requireNonNull(confirm.getStatusCode()).isGood());
  }

  // Seeding the one identity visible on a loaded node must remain a pre-live operation: an
  // application may still restore the persisted full EventId window before registering behavior.
  @Test
  void attachStillAllowsFullHistoricalWindowRestoration() throws Exception {
    AcknowledgeableCondition sourceCondition =
        AcknowledgeableCondition.create(
            testNamespace.getNodeContext(),
            builder ->
                builder
                    .nodeId(newNodeId("ConditionAttachAdoptTest/SnapshotSource"))
                    .browseName(newQualifiedName("SnapshotSource")));
    sourceCondition.setAcked(false);
    sourceCondition.setComment(LocalizedText.english("historical"), "operator");
    ConditionSnapshot snapshot = sourceCondition.captureSnapshot();

    ByteString loadedEventId = ByteString.of(new byte[] {8, 6, 7, 5, 3, 0, 9});
    AcknowledgeableConditionTypeNode loadedNode =
        rawAcknowledgeable(
            "SnapshotTarget", MethodInstantiation.COPY, false, loadedEventId, DateTime.now());
    AcknowledgeableCondition restored = AcknowledgeableCondition.attach(loadedNode);

    restored.restoreSnapshot(snapshot);

    assertFalse(restored.findBranch(loadedEventId).isPresent());
    for (ConditionSnapshot.AcceptedEventId accepted :
        snapshot.trunk().orElseThrow().eventIdWindow()) {
      assertTrue(restored.findBranch(accepted.eventId()).isPresent());
    }
  }

  // MethodInstantiation.SHARE is a valid Part 3 realization: adoption must recognize the shared
  // declarations and dispatch per Condition without replacing a handler on the type Method node.
  @Test
  void adoptionSupportsTypeSharedMethodDispatchWithoutMutatingTypeMethod() throws Exception {
    AcknowledgeableConditionTypeNode node =
        rawAcknowledgeable(
            "SharedTypeMethods",
            MethodInstantiation.SHARE,
            false,
            ByteString.NULL_VALUE,
            DateTime.now());
    UaMethodNode sharedAcknowledge = requireNonNull(node.getAcknowledgeMethodNode());
    MethodInvocationHandler originalHandler = sharedAcknowledge.getInvocationHandler();

    AcknowledgeableCondition condition =
        AcknowledgeableCondition.adopt(
            testNamespace.getNodeContext(), node.getNodeId(), builder -> {});
    server.getConditionManager().register(condition);

    condition.setAcked(false);
    CallMethodResult acknowledge =
        call(
            condition.getConditionId(),
            sharedAcknowledge.getNodeId(),
            new Variant(condition.currentBranch().getLastEventId()),
            new Variant(LocalizedText.NULL_VALUE));

    assertTrue(requireNonNull(acknowledge.getStatusCode()).isGood());
    assertTrue(condition.isAcked());
    assertSame(originalHandler, sharedAcknowledge.getInvocationHandler());
  }

  // One Method may be referenced by multiple instances in the same NodeManager. Its handler cannot
  // close over whichever Condition attached last; ObjectId must select the registered behavior.
  @Test
  void sharedInstanceMethodDispatchesToEachRegisteredCondition() throws Exception {
    AcknowledgeableConditionTypeNode firstNode =
        rawAcknowledgeable(
            "SharedInstanceMethod/First",
            MethodInstantiation.COPY,
            false,
            ByteString.NULL_VALUE,
            DateTime.now());
    AcknowledgeableConditionTypeNode secondNode =
        rawAcknowledgeable(
            "SharedInstanceMethod/Second",
            MethodInstantiation.COPY,
            false,
            ByteString.NULL_VALUE,
            DateTime.now());

    UaMethodNode sharedAcknowledge = requireNonNull(firstNode.getAcknowledgeMethodNode());
    requireNonNull(secondNode.getAcknowledgeMethodNode()).delete();
    secondNode.addComponent(sharedAcknowledge);

    AcknowledgeableCondition first =
        AcknowledgeableCondition.adopt(
            testNamespace.getNodeContext(), firstNode.getNodeId(), builder -> {});
    AcknowledgeableCondition second =
        AcknowledgeableCondition.adopt(
            testNamespace.getNodeContext(), secondNode.getNodeId(), builder -> {});
    server.getConditionManager().register(first);
    server.getConditionManager().register(second);

    first.setAcked(false);
    second.setAcked(false);

    CallMethodResult firstResult =
        call(
            first.getConditionId(),
            sharedAcknowledge.getNodeId(),
            new Variant(first.currentBranch().getLastEventId()),
            new Variant(LocalizedText.NULL_VALUE));
    CallMethodResult secondResult =
        call(
            second.getConditionId(),
            sharedAcknowledge.getNodeId(),
            new Variant(second.currentBranch().getLastEventId()),
            new Variant(LocalizedText.NULL_VALUE));

    assertTrue(requireNonNull(firstResult.getStatusCode()).isGood());
    assertTrue(requireNonNull(secondResult.getStatusCode()).isGood());
    assertTrue(first.isAcked());
    assertTrue(second.isAcked());
    assertSame(MethodInvocationHandler.NOT_IMPLEMENTED, sharedAcknowledge.getInvocationHandler());
  }

  // A newer or vendor-supported Method is application-owned when it is shared and already has a
  // working handler; attaching alarm behavior must neither overwrite nor globally delete it.
  @Test
  void sharedUnsupportedAlarmMethodKeepsApplicationHandler() throws Exception {
    AlarmConditionTypeNode firstNode = rawAlarm("SharedReset2/First", false);
    AlarmConditionTypeNode secondNode = rawAlarm("SharedReset2/Second", false);

    UaMethodNode reset2 =
        addMethod(
            testNamespace.getNodeContext(),
            firstNode,
            newNodeId("SharedReset2/Method"),
            new QualifiedName(0, "Reset2"));
    MethodInvocationHandler applicationHandler =
        (accessContext, request) ->
            new CallMethodResult(
                StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[0]);
    reset2.setInvocationHandler(applicationHandler);
    secondNode.addComponent(reset2);

    AlarmCondition first = AlarmCondition.attach(firstNode);
    AlarmCondition second = AlarmCondition.attach(secondNode);
    server.getConditionManager().register(first);
    server.getConditionManager().register(second);

    CallMethodResult result = call(second.getConditionId(), reset2.getNodeId());
    assertTrue(requireNonNull(result.getStatusCode()).isGood());
    assertSame(applicationHandler, reset2.getInvocationHandler());
    assertTrue(testNamespace.getNodeManager().containsNode(reset2.getNodeId()));
  }

  // ShelvingState and MaxTimeShelved are independent Optionals. Adopting an unbounded loaded alarm
  // must not add a limit that changes OneShotShelve semantics.
  @Test
  void adoptionPreservesAbsentMaxTimeShelvedForUnboundedShelving() throws Exception {
    AlarmConditionTypeNode node = rawAlarm("UnboundedShelving", true);
    assertNull(node.getMaxTimeShelvedNode());

    AlarmCondition condition =
        AlarmCondition.adopt(testNamespace.getNodeContext(), node.getNodeId(), builder -> {});
    server.getConditionManager().register(condition);

    assertNull(condition.getNode().getMaxTimeShelvedNode());
    condition.setActive(true);

    NodeId shelvingStateId = requireNonNull(condition.getShelvingState()).getNodeId();
    CallMethodResult oneShot = call(shelvingStateId, NodeIds.ShelvedStateMachineType_OneShotShelve);
    assertTrue(requireNonNull(oneShot.getStatusCode()).isGood());
    assertEquals(Double.MAX_VALUE, readUnshelveTime(condition));
  }

  // Replacing behavior while disabled used to leave the old disabled and UnshelveTime filters at
  // the head of the chain. The old behavior could then permanently mask the replacement's state.
  @Test
  void replacementWhileDisabledLeavesOneLiveFilterAndRestoresOneShotDeadline() throws Exception {
    AlarmConditionTypeNode node = rawAlarm("ReplacementFilters", true, true);
    DateTime transitionTime = DateTime.now();
    node.setMaxTimeShelved(60_000.0);
    FiniteStateVariableTypeNode currentState =
        requireNonNull(requireNonNull(node.getShelvingStateNode()).getCurrentStateNode());
    currentState.setId(ShelvedState.ONE_SHOT_SHELVED.stateId());
    currentState.setValue(new DataValue(new Variant(LocalizedText.english("OneShotShelved"))));
    FiniteTransitionVariableTypeNode lastTransition =
        requireNonNull(requireNonNull(node.getShelvingStateNode()).getLastTransitionNode());
    lastTransition.setId(NodeIds.ShelvedStateMachineType_UnshelvedToOneShotShelved);
    lastTransition.setValue(
        new DataValue(new Variant(LocalizedText.english("UnshelvedToOneShotShelved"))));
    lastTransition.setTransitionTime(transitionTime);
    node.setSuppressedOrShelved(false);

    AlarmCondition original = AlarmCondition.attach(node);
    server.getConditionManager().register(original);
    assertTrue(requireNonNull(original.getShelvingRuntime()).hasExpiryTimerForTesting());
    assertEquals(Boolean.TRUE, node.getSuppressedOrShelved());
    double originalUnshelveTime = readUnshelveTime(original);
    assertTrue(originalUnshelveTime > 0.0 && originalUnshelveTime <= 60_000.0);

    CallMethodResult disable = call(node.getNodeId(), NodeIds.ConditionType_Disable);
    assertTrue(requireNonNull(disable.getStatusCode()).isGood());

    AlarmCondition replacement = AlarmCondition.attach(node);
    server.getConditionManager().register(replacement);

    assertFalse(requireNonNull(original.getShelvingRuntime()).hasExpiryTimerForTesting());
    assertTrue(requireNonNull(replacement.getShelvingRuntime()).hasExpiryTimerForTesting());
    assertEquals(
        1,
        behaviorFilterCount(
            requireNonNull(node.getQualityNode()), Condition.BehaviorFilterKind.DISABLED_READ));
    assertEquals(
        1,
        behaviorFilterCount(
            requireNonNull(requireNonNull(node.getShelvingStateNode()).getUnshelveTimeNode()),
            Condition.BehaviorFilterKind.UNSHELVE_TIME));

    CallMethodResult enable = call(node.getNodeId(), NodeIds.ConditionType_Enable);
    assertTrue(requireNonNull(enable.getStatusCode()).isGood());
    Object quality =
        requireNonNull(node.getQualityNode())
            .getFilterChain()
            .readAttribute(null, requireNonNull(node.getQualityNode()), AttributeId.Value);
    assertTrue(quality instanceof DataValue);

    NodeId shelvingStateId = requireNonNull(replacement.getShelvingState()).getNodeId();
    CallMethodResult unshelve = call(shelvingStateId, NodeIds.ShelvedStateMachineType_Unshelve);
    assertTrue(requireNonNull(unshelve.getStatusCode()).isGood());
    assertEquals(0.0, readUnshelveTime(replacement));
  }

  // Unregister is the behavior lifecycle boundary. Leaving its filter attached retains the entire
  // displaced behavior and can keep returning stale Bad_ConditionDisabled indefinitely.
  @Test
  void unregisterRemovesBehaviorOwnedReadFilters() throws Exception {
    AcknowledgeableConditionTypeNode node =
        rawAcknowledgeable(
            "UnregisterFilters",
            MethodInstantiation.COPY,
            false,
            ByteString.NULL_VALUE,
            DateTime.now());
    AcknowledgeableCondition condition = AcknowledgeableCondition.attach(node);
    server.getConditionManager().register(condition);

    UaVariableNode quality = requireNonNull(node.getQualityNode());
    assertEquals(1, behaviorFilterCount(quality, Condition.BehaviorFilterKind.DISABLED_READ));

    server.getConditionManager().unregister(condition);

    assertEquals(0, behaviorFilterCount(quality, Condition.BehaviorFilterKind.DISABLED_READ));
  }

  // Unknown persisted state ids cannot drive a coherent shelving runtime. Attachment normalizes
  // all correlated fields together instead of leaving SuppressedOrShelved stuck true.
  @Test
  void attachNormalizesUnknownShelvingStateAndSuppressedFlag() throws Exception {
    AlarmConditionTypeNode node = rawAlarm("UnknownShelvingState", true);
    FiniteStateVariableTypeNode currentState =
        requireNonNull(requireNonNull(node.getShelvingStateNode()).getCurrentStateNode());
    currentState.setId(new NodeId(1, "UnknownShelvingState/State"));
    currentState.setValue(new DataValue(new Variant(LocalizedText.english("Unknown"))));
    node.setSuppressedOrShelved(true);

    AlarmCondition condition = AlarmCondition.attach(node);

    assertEquals(ShelvedState.UNSHELVED.stateId(), currentState.getId());
    assertEquals(
        LocalizedText.english("Unshelved"),
        requireNonNull(currentState.getValue()).value().value());
    assertEquals(Boolean.FALSE, node.getSuppressedOrShelved());
    assertEquals(0.0, readUnshelveTime(condition));
  }

  // AbstractNodeManager stores reference occurrences in a multiset. Concurrent idempotent wiring
  // must converge inside its deduplicating commit rather than racing a contains-then-add check.
  @Test
  void concurrentSourceWiringAddsSingleReferenceOccurrence() throws Exception {
    AcknowledgeableConditionTypeNode node =
        rawAcknowledgeable(
            "ConcurrentWiring",
            MethodInstantiation.COPY,
            false,
            ByteString.NULL_VALUE,
            DateTime.now());
    UaObjectNode concurrentSource =
        new UaObjectNode(
            testNamespace.getNodeContext(),
            newNodeId("ConditionAttachAdoptTest/ConcurrentWiringSource"),
            newQualifiedName("ConcurrentWiringSource"),
            LocalizedText.english("ConcurrentWiringSource"),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            ubyte(0));
    concurrentSource.addReference(
        new Reference(
            concurrentSource.getNodeId(),
            NodeIds.HasTypeDefinition,
            NodeIds.BaseObjectType.expanded(),
            true));
    testNamespace.getNodeManager().addNode(concurrentSource);

    int taskCount = 32;
    ExecutorService executor = Executors.newFixedThreadPool(taskCount);
    CountDownLatch ready = new CountDownLatch(taskCount);
    CountDownLatch start = new CountDownLatch(1);
    List<Future<?>> futures = new ArrayList<>();
    try {
      for (int i = 0; i < taskCount; i++) {
        futures.add(
            executor.submit(
                () -> {
                  ready.countDown();
                  assertTrue(start.await(10, TimeUnit.SECONDS));
                  ConditionWiring.wire(node, concurrentSource);
                  return null;
                }));
      }

      assertTrue(ready.await(10, TimeUnit.SECONDS));
      start.countDown();
      for (Future<?> future : futures) {
        future.get(10, TimeUnit.SECONDS);
      }
    } finally {
      executor.shutdownNow();
    }

    Reference hasCondition =
        new Reference(
            concurrentSource.getNodeId(), NodeIds.HasCondition, node.getNodeId().expanded(), true);
    Reference hasEventSource =
        new Reference(
            NodeIds.Server, NodeIds.HasEventSource, concurrentSource.getNodeId().expanded(), true);

    assertEquals(
        1,
        testNamespace.getNodeManager().getReferences(concurrentSource.getNodeId()).stream()
            .filter(hasCondition::equals)
            .count());
    assertEquals(
        1,
        server.getAddressSpaceManager().getManagedReferences(NodeIds.Server).stream()
            .filter(hasEventSource::equals)
            .count());
  }

  @Test
  void attachSilentlyNormalizesTimedShelvedStateWithoutRuntimeDeadline() throws Exception {
    AlarmCondition loaded =
        AlarmCondition.create(
            testNamespace.getNodeContext(),
            builder ->
                builder
                    .nodeId(newNodeId("ConditionAttachAdoptTest/TimedShelved"))
                    .browseName(newQualifiedName("TimedShelved"))
                    .withShelving());

    var shelvingState = requireNonNull(loaded.getNode().getShelvingStateNode());
    var currentState = requireNonNull(shelvingState.getCurrentStateNode());
    currentState.setId(ShelvedState.TIMED_SHELVED.stateId());
    currentState.setValue(new DataValue(new Variant(LocalizedText.english("TimedShelved"))));
    loaded.getNode().setSuppressedOrShelved(true);
    ByteString eventId = loaded.getNode().getEventId();

    AlarmCondition normalized = AlarmCondition.attach(loaded.getNode());

    assertEquals(
        ShelvedState.UNSHELVED.stateId(),
        requireNonNull(
                requireNonNull(normalized.getNode().getShelvingStateNode()).getCurrentStateNode())
            .getId());
    assertEquals(Boolean.FALSE, normalized.getNode().getSuppressedOrShelved());
    assertEquals(eventId, normalized.getNode().getEventId());
  }

  private AcknowledgeableConditionTypeNode rawAcknowledgeable(
      String name,
      MethodInstantiation methodInstantiation,
      boolean withConfirm,
      ByteString eventId,
      DateTime time)
      throws UaException {

    InstantiationRequest.Builder<AcknowledgeableConditionTypeNode> builder =
        InstantiationRequest.of(
                AcknowledgeableConditionTypeNode.class, NodeIds.AcknowledgeableConditionType)
            .nodeId(newNodeId("ConditionAttachAdoptTest/" + name))
            .browseName(newQualifiedName(name))
            .displayName(LocalizedText.english(name))
            .target(testNamespace.getNodeManager())
            .methodInstantiation(methodInstantiation);

    if (withConfirm) {
      builder.includeOptionals(
          declaration ->
              Set.of("ConfirmedState", "Confirm", "TransitionTime")
                  .contains(declaration.browseName().name()));
    }

    AcknowledgeableConditionTypeNode node =
        server.getNodeInstantiator().instantiate(builder.build()).root();
    initializeRawCondition(node, NodeIds.AcknowledgeableConditionType, eventId, time);
    return node;
  }

  private AlarmConditionTypeNode rawAlarm(String name, boolean withShelving) throws UaException {
    return rawAlarm(name, withShelving, false);
  }

  private AlarmConditionTypeNode rawAlarm(
      String name, boolean withShelving, boolean withMaxTimeShelved) throws UaException {

    InstantiationRequest.Builder<AlarmConditionTypeNode> builder =
        InstantiationRequest.of(AlarmConditionTypeNode.class, NodeIds.AlarmConditionType)
            .nodeId(newNodeId("ConditionAttachAdoptTest/" + name))
            .browseName(newQualifiedName(name))
            .displayName(LocalizedText.english(name))
            .target(testNamespace.getNodeManager());

    if (withShelving) {
      builder.includeOptionals(
          declaration -> {
            String browseName = declaration.browseName().name();
            return "ShelvingState".equals(browseName)
                || "LastTransition".equals(browseName)
                || "TransitionTime".equals(browseName)
                || (withMaxTimeShelved && "MaxTimeShelved".equals(browseName));
          });
    }

    AlarmConditionTypeNode node = server.getNodeInstantiator().instantiate(builder.build()).root();
    initializeRawCondition(node, NodeIds.AlarmConditionType, ByteString.NULL_VALUE, DateTime.now());
    node.setInputNode(NodeId.NULL_VALUE);
    node.setSuppressedOrShelved(false);
    return node;
  }

  private void initializeRawCondition(
      AcknowledgeableConditionTypeNode node, NodeId eventType, ByteString eventId, DateTime time) {

    node.setEventType(eventType);
    node.setEventId(eventId);
    node.setTime(time);
    node.setMessage(LocalizedText.NULL_VALUE);
    node.setSeverity(ushort(500));
    node.setConditionName(node.getBrowseName().name());
    node.setConditionClassId(NodeIds.BaseConditionClassType);
    node.setConditionClassName(LocalizedText.english("BaseConditionClass"));
    node.setBranchId(NodeId.NULL_VALUE);
    node.setRetain(false);

    setTwoState(requireNonNull(node.getEnabledStateNode()), true, "Enabled", time);
    setTwoState(requireNonNull(node.getAckedStateNode()), true, "Acknowledged", time);
  }

  private static void setTwoState(
      TwoStateVariableTypeNode state, boolean id, String text, DateTime time) {
    state.setValue(new DataValue(new Variant(LocalizedText.english(text))));
    state.setId(id);
    state.setTransitionTime(time);
  }

  private static long behaviorFilterCount(UaVariableNode node, Condition.BehaviorFilterKind kind) {
    return node.getFilterChain().getFilters().stream()
        .filter(Condition.BehaviorOwnedAttributeFilter.class::isInstance)
        .map(Condition.BehaviorOwnedAttributeFilter.class::cast)
        .filter(filter -> filter.kind() == kind)
        .count();
  }

  private static double readUnshelveTime(AlarmCondition condition) {
    UaVariableNode unshelveTime =
        requireNonNull(requireNonNull(condition.getShelvingState()).getUnshelveTimeNode());
    DataValue value =
        (DataValue) unshelveTime.getFilterChain().getAttribute(unshelveTime, AttributeId.Value);
    return (Double) requireNonNull(value.value().value());
  }

  private CallMethodResult call(NodeId objectId, NodeId methodId, Variant... inputs)
      throws Exception {
    CallResponse response = client.call(List.of(new CallMethodRequest(objectId, methodId, inputs)));
    return requireNonNull(response.getResults())[0];
  }
}
