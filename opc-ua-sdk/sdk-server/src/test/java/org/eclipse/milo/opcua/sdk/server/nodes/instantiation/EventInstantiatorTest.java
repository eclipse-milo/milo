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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for {@link EventInstantiator}, the replacement for the legacy {@code EventFactory}: events
 * are created with all optionals included into the component's private NodeManager, the EventType
 * Property is set as part of the instantiation, and an incompatible root class fails at plan time
 * with no residue — the legacy factory's cast failed after its nodes were already stored.
 */
public class EventInstantiatorTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventInstantiatorTest.class);

  private TypeFixtures fx;
  private EventInstantiator eventInstantiator;

  @BeforeEach
  void setUp() {
    fx = TypeFixtures.create();

    ObjectTypeInitializer.initialize(fx.namespaceTable(), fx.objectTypeManager());
    VariableTypeInitializer.initialize(fx.namespaceTable(), fx.variableTypeManager());

    // Everything a stubbing statement needs is resolved up front: creating the instantiator and
    // fetching the AddressSpaceManager both invoke the same server mock, which is illegal while
    // another stubbing is in progress.
    NodeInstantiator instantiator = fx.instantiator();
    AddressSpaceManager addressSpaceManager = fx.server().getAddressSpaceManager();

    Mockito.when(fx.server().getNodeInstantiator()).thenReturn(instantiator);

    // The mocked AddressSpaceManager's register() must actually register, so node-level
    // navigation of created events resolves against the private manager like on a real server.
    Mockito.doAnswer(
            invocation -> {
              fx.registerWithAddressSpace(invocation.getArgument(0));
              return null;
            })
        .when(addressSpaceManager)
        .register(Mockito.<NodeManager<UaNode>>any());

    eventInstantiator = new EventInstantiator(fx.server());
    eventInstantiator.startup();
  }

  /**
   * The replacement reproduces the legacy contract: the result is a {@link BaseEventTypeNode},
   * every Optional member is included (BaseEventType's Optional {@code LocalTime} property is
   * instantiated), the EventType Property carries the type definition id, and every created node
   * lands in the component's private NodeManager — not in the server's source manager.
   */
  @Test
  public void createEventIncludesOptionalsAndSetsEventType() throws UaException {
    NodeId nodeId = new NodeId(1, UUID.randomUUID());

    BaseEventTypeNode eventNode = eventInstantiator.createEvent(nodeId, NodeIds.BaseEventType);

    assertNotNull(eventNode);
    assertEquals(nodeId, eventNode.getNodeId());
    assertEquals(NodeIds.BaseEventType, eventNode.getEventType());
    assertNotNull(eventNode.getLocalTimeNode(), "Optional LocalTime property expected");

    assertTrue(eventInstantiator.getNodeManager().containsNode(nodeId));
    assertFalse(fx.nodeManager().containsNode(nodeId));

    eventNode.delete();
    assertTrue(
        eventInstantiator.getNodeManager().getNodes().isEmpty(),
        "delete() removes the event's nodes from the private manager");
  }

  /**
   * Typed plan-time preflight replacing the legacy cast: creating an "event" of a type whose
   * resolved Java class is not a {@link BaseEventTypeNode} fails before any node is created,
   * leaving no residue in the private manager. The legacy factory threw {@link ClassCastException}
   * after its nodes were already stored.
   */
  @Test
  public void incompatibleRootClassFailsAtPlanTimeWithNoResidue() {
    NodeId nodeId = new NodeId(1, UUID.randomUUID());

    InstantiationException e =
        assertThrows(
            InstantiationException.class,
            () -> eventInstantiator.createEvent(nodeId, NodeIds.FolderType));

    assertTrue(
        e.getDiagnostics().stream()
            .anyMatch(d -> d.code() == InstantiationDiagnostic.Code.INVALID_ROOT_CLASS));
    assertTrue(
        eventInstantiator.getNodeManager().getNodes().isEmpty(),
        "no nodes stored for a failed plan");
  }

  /**
   * Sanity-profiles per-event creation against the legacy {@code EventFactory} — events are the
   * high-frequency path, so the replacement must stay in the same order of magnitude. Numbers are
   * logged as evidence, not asserted: absolute timings vary by machine, and the legacy factory
   * additionally benefits from its JVM-global hierarchy cache.
   */
  @Test
  @SuppressWarnings("deprecation")
  public void timingSanityAgainstLegacyEventFactory() throws UaException {
    var legacyFactory =
        new org.eclipse.milo.opcua.sdk.server.nodes.factories.EventFactory(
            fx.server(), fx.objectTypeManager(), fx.variableTypeManager());
    legacyFactory.startup();

    int warmup = 50;
    int iterations = 200;

    for (int i = 0; i < warmup; i++) {
      legacyFactory.createEvent(new NodeId(1, UUID.randomUUID()), NodeIds.BaseEventType).delete();
      eventInstantiator
          .createEvent(new NodeId(1, UUID.randomUUID()), NodeIds.BaseEventType)
          .delete();
    }

    long legacyStart = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      legacyFactory.createEvent(new NodeId(1, UUID.randomUUID()), NodeIds.BaseEventType).delete();
    }
    long legacyNanos = System.nanoTime() - legacyStart;

    long replacementStart = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      eventInstantiator
          .createEvent(new NodeId(1, UUID.randomUUID()), NodeIds.BaseEventType)
          .delete();
    }
    long replacementNanos = System.nanoTime() - replacementStart;

    LOGGER.info(
        "per-event creation over {} iterations: legacy EventFactory {} us/event,"
            + " EventInstantiator {} us/event",
        iterations,
        legacyNanos / iterations / 1_000,
        replacementNanos / iterations / 1_000);

    legacyFactory.shutdown();
  }
}
