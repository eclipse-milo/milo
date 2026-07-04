/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubCommunicationFailureEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubStatusEventTypeNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * The D35/D46 spike gating wave 3 (R17): are ABSTRACT-typed events — PubSubStatusEventType {@code
 * i=15535} is abstract — deliverable end-to-end through Milo's server-side {@code OfType} where
 * clause and the Milo client?
 *
 * <p>Design per the WP-T5 brief §12.1, using Phase 4-era machinery only: a started {@link
 * SksTestServer}, events created by {@code EventFactory} (whose NodeFactory has no IsAbstract
 * check) and fired via {@code server.getEventNotifier().fire(...)}, and a client monitored item on
 * the Server object ({@code i=2253}) with an {@code OfType(i=15535)} where clause.
 *
 * <ul>
 *   <li>SP1 — a fired {@code i=15535} event is RECEIVED (abstract type reportable end-to-end).
 *   <li>SP2 — a fired {@code i=15563} (PubSubCommunicationFailureEventType) event is RECEIVED
 *       through the SAME {@code OfType(i=15535)} filter (subtype matching, the R17 open question;
 *       if this row fails, WP-Z falls back to a concrete event type — the bridge keeps its type id
 *       as one constant, D35).
 *   <li>SP3 — a fired unrelated BaseEventType event is NOT received (the filter actually filters);
 *       determinism via a follow-up marker event on the same FIFO pipeline.
 *   <li>SP4 — select-clause fidelity: ConnectionId/GroupId/State/Error arrive in select-clause
 *       order with correct types (the Error clause, typed {@code i=15563}, is null-filled for
 *       {@code i=15535} events).
 * </ul>
 */
class EventOfTypeSpikeTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final NodeId CONNECTION_ID = new NodeId(1, "spike-connection");
  private static final NodeId GROUP_ID = new NodeId(1, "spike-group");
  private static final NodeId MARKER_CONNECTION_ID = new NodeId(1, "spike-marker");

  // select-clause indices
  private static final int EVENT_TYPE = 0;
  private static final int CONNECTION = 1;
  private static final int GROUP = 2;
  private static final int STATE = 3;
  private static final int ERROR = 4;

  @Test
  void abstractPubSubStatusEventTypeFlowsThroughOfTypeFilter() throws Exception {
    try (SksTestServer testServer = SksTestServer.create(null)) {
      OpcUaServer server = testServer.getServer();

      OpcUaClient client = connect(testServer);
      try {
        var subscription = new OpcUaSubscription(client);
        subscription.create();

        EventFilter eventFilter =
            new EventFilter(
                new SimpleAttributeOperand[] {
                  select(NodeIds.BaseEventType, "EventType"),
                  select(NodeIds.PubSubStatusEventType, "ConnectionId"),
                  select(NodeIds.PubSubStatusEventType, "GroupId"),
                  select(NodeIds.PubSubStatusEventType, "State"),
                  select(NodeIds.PubSubCommunicationFailureEventType, "Error")
                },
                ofType(client, NodeIds.PubSubStatusEventType));

        var received = new LinkedBlockingQueue<Variant[]>();

        OpcUaMonitoredItem item = OpcUaMonitoredItem.newEventItem(NodeIds.Server, eventFilter);
        item.setEventValueListener((monitoredItem, eventValues) -> received.add(eventValues));

        subscription.addMonitoredItem(item);
        subscription.synchronizeMonitoredItems();

        try {
          // SP1: an abstract i=15535-typed event flows end-to-end
          fireEvent(server, NodeIds.PubSubStatusEventType, CONNECTION_ID, null);

          Variant[] statusEvent = received.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS);
          assertNotNull(
              statusEvent,
              "SP1 FAILED: abstract i=15535 event not delivered through OfType(i=15535)");

          // SP4: select-clause order and types
          assertEquals(NodeIds.PubSubStatusEventType, statusEvent[EVENT_TYPE].getValue());
          assertEquals(CONNECTION_ID, statusEvent[CONNECTION].getValue());
          assertEquals(GROUP_ID, statusEvent[GROUP].getValue());
          Object state = statusEvent[STATE].getValue();
          assertNotNull(state, "SP4: State field missing");
          assertEquals(
              PubSubState.Operational.getValue(), ((Number) state).intValue(), "SP4: State value");
          // the Error clause is typed i=15563; on an i=15535 event it is null-filled
          assertNull(statusEvent[ERROR].getValue(), "SP4: Error must be null on i=15535 events");

          // SP2: a concrete i=15563-typed event flows through the SAME OfType(i=15535) filter
          fireEvent(
              server,
              NodeIds.PubSubCommunicationFailureEventType,
              CONNECTION_ID,
              new StatusCode(StatusCodes.Bad_Disconnect));

          Variant[] failureEvent = received.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS);
          assertNotNull(
              failureEvent,
              "SP2 FAILED: i=15563 subtype event not delivered through OfType(i=15535)");
          assertEquals(
              NodeIds.PubSubCommunicationFailureEventType, failureEvent[EVENT_TYPE].getValue());
          assertInstanceOf(StatusCode.class, failureEvent[ERROR].getValue(), "SP4: Error type");
          assertEquals(new StatusCode(StatusCodes.Bad_Disconnect), failureEvent[ERROR].getValue());

          // SP3: an unrelated BaseEventType event is filtered out; the marker event fired
          // right after it on the same FIFO pipeline must be the NEXT delivery
          fireBaseEvent(server);
          fireEvent(server, NodeIds.PubSubStatusEventType, MARKER_CONNECTION_ID, null);

          Variant[] next = received.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS);
          assertNotNull(next, "SP3: the marker event was not delivered");
          assertEquals(
              MARKER_CONNECTION_ID,
              next[CONNECTION].getValue(),
              "SP3 FAILED: the BaseEventType event leaked through OfType(i=15535)");
        } finally {
          subscription.delete();
        }
      } finally {
        client.disconnect();
      }
    }
  }

  // region fixtures

  private static OpcUaClient connect(SksTestServer testServer) throws UaException {
    OpcUaClient client =
        OpcUaClient.create(
            testServer.getEndpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(
                        e -> Objects.equals(e.getSecurityPolicyUri(), SecurityPolicy.None.getUri()))
                    .findFirst(),
            transportConfigBuilder -> {},
            clientConfigBuilder ->
                clientConfigBuilder
                    .setApplicationName(LocalizedText.english("event spike test client"))
                    .setApplicationUri("urn:eclipse:milo:test:event-spike-client")
                    .setIdentityProvider(new AnonymousProvider())
                    .setRequestTimeout(uint(5_000)));

    client.connect();

    return client;
  }

  /** A select clause for {@code browseName} declared on {@code typeDefinitionId}. */
  private static SimpleAttributeOperand select(NodeId typeDefinitionId, String browseName) {
    return new SimpleAttributeOperand(
        typeDefinitionId,
        new QualifiedName[] {new QualifiedName(0, browseName)},
        AttributeId.Value.uid(),
        null);
  }

  /** A where clause of exactly one element: {@code OfType(typeDefinitionId)}. */
  private static ContentFilter ofType(OpcUaClient client, NodeId typeDefinitionId) {
    ExtensionObject operand =
        ExtensionObject.encode(
            client.getStaticEncodingContext(), new LiteralOperand(new Variant(typeDefinitionId)));

    return new ContentFilter(
        new ContentFilterElement[] {
          new ContentFilterElement(FilterOperator.OfType, new ExtensionObject[] {operand})
        });
  }

  /**
   * Create, fire, and delete a PubSub status event of {@code typeId} (the GenerateEventMethod
   * pattern): mandatory base fields plus ConnectionId/GroupId/State, and Error for {@code i=15563}.
   */
  private static void fireEvent(
      OpcUaServer server, NodeId typeId, NodeId connectionId, @Nullable StatusCode error)
      throws UaException {

    BaseEventTypeNode eventNode =
        server.getEventFactory().createEvent(new NodeId(1, UUID.randomUUID()), typeId);
    try {
      setBaseFields(eventNode, typeId);

      PubSubStatusEventTypeNode statusEvent = (PubSubStatusEventTypeNode) eventNode;
      statusEvent.setConnectionId(connectionId);
      statusEvent.setGroupId(GROUP_ID);
      statusEvent.setState(PubSubState.Operational);

      if (eventNode instanceof PubSubCommunicationFailureEventTypeNode failureEvent
          && error != null) {
        failureEvent.setError(error);
      }

      server.getEventNotifier().fire(eventNode);
    } finally {
      eventNode.delete();
    }
  }

  /** Create, fire, and delete an unrelated BaseEventType event (the SP3 negative). */
  private static void fireBaseEvent(OpcUaServer server) throws UaException {
    BaseEventTypeNode eventNode =
        server
            .getEventFactory()
            .createEvent(new NodeId(1, UUID.randomUUID()), NodeIds.BaseEventType);
    try {
      setBaseFields(eventNode, NodeIds.BaseEventType);
      server.getEventNotifier().fire(eventNode);
    } finally {
      eventNode.delete();
    }
  }

  private static void setBaseFields(BaseEventTypeNode eventNode, NodeId typeId) {
    eventNode.setBrowseName(new QualifiedName(1, "spike"));
    eventNode.setDisplayName(LocalizedText.english("spike"));
    eventNode.setEventId(
        ByteString.of(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
    eventNode.setEventType(typeId);
    eventNode.setSourceNode(NodeIds.PublishSubscribe);
    eventNode.setSourceName("spike");
    eventNode.setTime(DateTime.now());
    eventNode.setReceiveTime(DateTime.NULL_VALUE);
    eventNode.setMessage(LocalizedText.english("spike event"));
    eventNode.setSeverity(ushort(100));
  }

  // endregion
}
