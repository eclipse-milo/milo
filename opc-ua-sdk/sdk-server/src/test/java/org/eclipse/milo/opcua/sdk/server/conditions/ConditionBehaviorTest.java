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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.EventListener;
import org.eclipse.milo.opcua.sdk.server.EventNotifier;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.AcknowledgeableConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ConditionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Unit tests for the {@link Condition}/{@link AcknowledgeableCondition} behavior rules: Retain
 * recomputation, the event-emission rules of §5.5.2, the bounded accepted-EventId window, the
 * NULL-comment convention, and LastSeverity maintenance.
 *
 * <p>The condition node tree is built by hand against a mocked server so behavior is exercised
 * without the NodeFactory or a running server; a capturing {@link EventNotifier} records every
 * fired event.
 */
public class ConditionBehaviorTest {

  record CapturedEvent(
      ByteString eventId, DateTime time, @Nullable String message, boolean retain) {}

  private final List<CapturedEvent> events = new ArrayList<>();

  private UaNodeContext nodeContext;
  private UaNodeManager nodeManager;

  @BeforeEach
  void setUp() {
    events.clear();

    OpcUaServer server = Mockito.mock(OpcUaServer.class);

    AddressSpaceManager addressSpaceManager = new AddressSpaceManager(server);
    NamespaceTable namespaceTable = new NamespaceTable();

    Mockito.when(server.getNamespaceTable()).thenReturn(namespaceTable);
    Mockito.when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    Mockito.when(server.getObjectTypeManager()).thenReturn(new ObjectTypeManager());
    Mockito.when(server.getVariableTypeManager()).thenReturn(new VariableTypeManager());

    var capturingNotifier =
        new EventNotifier() {
          @Override
          public void fire(BaseEventTypeNode event) {
            Boolean retain = ((AcknowledgeableConditionTypeNode) event).getRetain();
            LocalizedText message = event.getMessage();

            events.add(
                new CapturedEvent(
                    event.getEventId(),
                    event.getTime(),
                    message != null ? message.text() : null,
                    retain != null && retain));
          }

          @Override
          public void register(EventListener eventListener) {}

          @Override
          public void unregister(EventListener eventListener) {}
        };

    Mockito.when(server.getEventNotifier()).thenReturn(capturingNotifier);

    nodeManager = new UaNodeManager();
    addressSpaceManager.register(nodeManager);

    nodeContext =
        new UaNodeContext() {
          @Override
          public OpcUaServer getServer() {
            return server;
          }

          @Override
          public NodeManager<UaNode> getNodeManager() {
            return nodeManager;
          }
        };
  }

  private AcknowledgeableCondition newCondition(boolean withConfirm) {
    return new AcknowledgeableCondition(buildConditionNode(withConfirm));
  }

  private AcknowledgeableConditionTypeNode buildConditionNode(boolean withConfirm) {
    var node =
        new AcknowledgeableConditionTypeNode(
            nodeContext,
            new NodeId(1, "TestCondition"),
            new QualifiedName(1, "TestCondition"),
            LocalizedText.english("TestCondition"),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            null,
            null,
            null);

    nodeManager.addNode(node);

    addTwoStateVariable(node, "EnabledState");
    addTwoStateVariable(node, "AckedState");
    if (withConfirm) {
      addTwoStateVariable(node, "ConfirmedState");
    }

    addConditionVariable(node, "Quality", NodeIds.StatusCode);
    addConditionVariable(node, "LastSeverity", NodeIds.UInt16);
    addConditionVariable(node, "Comment", NodeIds.LocalizedText);

    return node;
  }

  private void addTwoStateVariable(AcknowledgeableConditionTypeNode parent, String name) {
    var variable =
        new TwoStateVariableTypeNode(
            nodeContext,
            new NodeId(1, "TestCondition/" + name),
            new QualifiedName(0, name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            null,
            null,
            null,
            new DataValue(Variant.NULL_VALUE),
            NodeIds.LocalizedText,
            -1,
            null);

    nodeManager.addNode(variable);
    parent.addComponent(variable);
  }

  private void addConditionVariable(
      AcknowledgeableConditionTypeNode parent, String name, NodeId dataType) {
    var variable =
        new ConditionVariableTypeNode(
            nodeContext,
            new NodeId(1, "TestCondition/" + name),
            new QualifiedName(0, name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            null,
            null,
            null,
            new DataValue(Variant.NULL_VALUE),
            dataType,
            -1,
            null);

    nodeManager.addNode(variable);
    parent.addComponent(variable);
  }

  @Test
  void retainRecomputationAndEmissionRules() {
    AcknowledgeableCondition condition = newCondition(false);

    assertFalse(condition.isRetained());
    assertTrue(events.isEmpty());

    // Not retained: state changes are silent (§5.5.2).
    condition.setQuality(StatusCode.GOOD);
    assertTrue(events.isEmpty());

    // A state needing acknowledgement: Retain becomes true, an event is generated.
    condition.setAcked(false);
    assertTrue(condition.isRetained());
    assertEquals(1, events.size());
    assertTrue(events.get(0).retain());

    // Changes while retained each generate an event.
    condition.setSeverity(ushort(500));
    assertEquals(2, events.size());

    // Acknowledged (no ConfirmedState): Retain true→false, the single retraction event.
    condition.setAcked(true);
    assertFalse(condition.isRetained());
    assertEquals(3, events.size());
    assertFalse(events.get(2).retain());

    // Back to not retained: silent again.
    condition.setQuality(new StatusCode(StatusCode.GOOD.value()));
    condition.setSeverity(ushort(600));
    assertEquals(3, events.size());
  }

  @Test
  void confirmedStateContributesToRetain() {
    AcknowledgeableCondition condition = newCondition(true);
    assertTrue(condition.hasConfirmedState());

    condition.setAcked(false);
    assertTrue(condition.isRetained());

    // Acked but unconfirmed: still retained.
    condition.setAcked(true);
    condition.setConfirmed(false);
    assertTrue(condition.isRetained());

    // Confirmed: retraction.
    condition.setConfirmed(true);
    assertFalse(condition.isRetained());
    assertFalse(events.get(events.size() - 1).retain());
  }

  @Test
  void eventIdWindowEvictsOldestBeyond32() {
    AcknowledgeableCondition condition = newCondition(false);

    condition.setAcked(false);
    assertEquals(1, events.size());

    ByteString firstEventId = events.get(0).eventId();
    assertTrue(condition.findBranch(firstEventId).isPresent());

    // 32 more events: the 33rd id evicts the 1st.
    for (int i = 0; i < 32; i++) {
      condition.setSeverity(ushort(100 + i));
    }
    assertEquals(33, events.size());

    assertFalse(condition.findBranch(firstEventId).isPresent());
    assertTrue(condition.findBranch(events.get(1).eventId()).isPresent());
    assertTrue(condition.findBranch(events.get(32).eventId()).isPresent());
  }

  @Test
  void noOpMutationsAreSilentWhileRetained() {
    AcknowledgeableCondition condition = newCondition(false);

    condition.setAcked(false);
    condition.setSeverity(ushort(500));
    assertEquals(2, events.size());

    // Re-asserting unchanged values while retained generates no events and consumes no EventId
    // window slots (§5.5.2 keys event generation to changes).
    condition.setQuality(StatusCode.GOOD);
    condition.setSeverity(ushort(500));
    condition.setAcked(false);
    condition.setComment(LocalizedText.NULL_VALUE, "user");
    assertEquals(2, events.size());

    // A genuine change still fires.
    condition.setSeverity(ushort(600));
    assertEquals(3, events.size());
  }

  @Test
  void constructorRecomputesRetainFromWrappedState() {
    AcknowledgeableConditionTypeNode node = buildConditionNode(false);

    // A pre-existing unacknowledged state whose stored Retain property disagrees (unset/false).
    TwoStateVariableTypeNode ackedState = node.getAckedStateNode();
    ackedState.setValue(new DataValue(new Variant(LocalizedText.english("Unacknowledged"))));
    ackedState.setId(false);

    var condition = new AcknowledgeableCondition(node);

    assertTrue(condition.isRetained());
    assertEquals(Boolean.TRUE, node.getRetain());
    // Construction is silent: reconciling state must not generate events.
    assertTrue(events.isEmpty());
  }

  @Test
  void findBranchRejectsUnknownEventId() {
    AcknowledgeableCondition condition = newCondition(false);

    condition.setAcked(false);

    assertFalse(condition.findBranch(ByteString.of(new byte[] {1, 2, 3})).isPresent());
    assertTrue(condition.findBranch(condition.currentBranch().getLastEventId()).isPresent());
  }

  @Test
  void nullCommentConventionPreservesAndClears() {
    AcknowledgeableCondition condition = newCondition(false);

    condition.setComment(LocalizedText.english("hello"), "user1");
    assertEquals("hello", commentText(condition));
    assertEquals("user1", condition.getNode().getClientUserId());

    // NULL comment: a complete no-op — stored Comment and ClientUserId unchanged (§5.5.6).
    condition.setComment(LocalizedText.NULL_VALUE, "user2");
    assertEquals("hello", commentText(condition));
    assertEquals("user1", condition.getNode().getClientUserId());

    // Clearing requires empty text with a locale (§5.5.6).
    condition.setComment(new LocalizedText("en", ""), "user3");
    assertEquals("", commentText(condition));
  }

  @Test
  void lastSeverityTracksPreviousSeverity() {
    AcknowledgeableCondition condition = newCondition(false);

    condition.setSeverity(ushort(100));
    assertEquals(ushort(100), condition.getNode().getSeverity());

    condition.setSeverity(ushort(200));
    assertEquals(ushort(200), condition.getNode().getSeverity());
    assertEquals(ushort(100), condition.getNode().getLastSeverity());

    condition.setSeverity(ushort(300));
    assertEquals(ushort(200), condition.getNode().getLastSeverity());
  }

  @Test
  void eventsCarryUniqueEventIdsAndRecordOnTrunk() {
    AcknowledgeableCondition condition = newCondition(false);

    condition.setAcked(false);
    condition.setSeverity(ushort(200));
    condition.setSeverity(ushort(300));

    assertEquals(3, events.size());
    assertEquals(3, events.stream().map(CapturedEvent::eventId).distinct().count());

    CapturedEvent last = events.get(2);
    assertEquals(last.eventId(), condition.currentBranch().getLastEventId());
    assertEquals(last.time(), condition.currentBranch().getLastEventTime());
  }

  private static @Nullable String commentText(Condition condition) {
    LocalizedText comment = condition.getNode().getComment();
    return comment != null ? comment.text() : null;
  }
}
