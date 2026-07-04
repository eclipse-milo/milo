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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.junit.jupiter.api.Test;

/**
 * The R17 end-to-end row (the in-repo half of the D35 gate, carried forward from the wave-2a
 * spike): a REAL engine state change — {@code runtime().disable(connection)} on a running {@link
 * ServerPubSub} with {@code statusEventsEnabled} — travels engine listener &rarr; {@link
 * PubSubStatusEventBridge} &rarr; {@code EventFactory}/event notifier &rarr; a Milo client's event
 * monitored item on the Server object ({@code i=2253}, D42) with an {@code OfType(i=15535)} where
 * clause, arriving typed as the abstract {@code PubSubStatusEventType} with the §6.2 field set
 * ({@code SourceNode}/{@code ConnectionId} carrying the deterministic fragment NodeIds, {@code
 * GroupId} null for the connection-sourced event, {@code State == Disabled}).
 *
 * <p>The information model is NOT exposed (D32): the event fields reference non-materialized
 * NodeIds, proving the bridge's independence from the fragment end-to-end.
 */
class PubSubStatusEventReceptionTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "ev-rx-conn";
  private static final String WRITER_GROUP = "grp";
  private static final String WRITER = "writer";
  private static final String DATA_SET = "ev-rx-ds";

  // select-clause indices
  private static final int EVENT_TYPE = 0;
  private static final int SOURCE_NODE = 1;
  private static final int CONNECTION_ID = 2;
  private static final int GROUP_ID = 3;
  private static final int STATE = 4;

  @Test
  void realStateChangeArrivesThroughOfTypeFilter() throws Exception {
    try (SksTestServer testServer = SksTestServer.create(null)) {
      OpcUaServer server = testServer.getServer();
      UShort namespaceIndex = server.getServerNamespace().getNamespaceIndex();

      PublishedDataSetConfig dataSet =
          PublishedDataSetConfig.builder(DATA_SET)
              .field(
                  FieldDefinition.builder("v")
                      .dataType(NodeIds.Double)
                      .dataSetFieldId(new UUID(0L, 0xEE1L))
                      .build())
              .build();

      PubSubBindings bindings =
          PubSubBindings.builder()
              .source(
                  dataSet.ref(),
                  context -> {
                    DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
                    builder.field("v", new DataValue(Variant.ofDouble(2.5)));
                    return builder.build();
                  })
              .build();

      ServerPubSub serverPubSub =
          ServerPubSub.attach(
              server,
              publisherConfig(dataSet),
              ServerPubSubOptions.builder().statusEventsEnabled(true).bindings(bindings).build());
      try {
        serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        OpcUaClient client = connect(testServer);
        try {
          var subscription = new OpcUaSubscription(client);
          subscription.create();

          EventFilter eventFilter =
              new EventFilter(
                  new SimpleAttributeOperand[] {
                    select(NodeIds.BaseEventType, "EventType"),
                    select(NodeIds.BaseEventType, "SourceNode"),
                    select(NodeIds.PubSubStatusEventType, "ConnectionId"),
                    select(NodeIds.PubSubStatusEventType, "GroupId"),
                    select(NodeIds.PubSubStatusEventType, "State")
                  },
                  ofType(client, NodeIds.PubSubStatusEventType));

          var received = new LinkedBlockingQueue<Variant[]>();

          OpcUaMonitoredItem item = OpcUaMonitoredItem.newEventItem(NodeIds.Server, eventFilter);
          item.setEventValueListener((monitoredItem, eventValues) -> received.add(eventValues));
          // the disable cascade fires several events within one publish cycle (connection
          // Disabled first, then the children's Paused hops); the default event queue
          // (size 1, discardOldest) would drop the connection-sourced notification
          item.setQueueSize(uint(10));

          subscription.addMonitoredItem(item);
          subscription.synchronizeMonitoredItems();

          try {
            // the real stimulus: disabling the connection through the managed runtime; the
            // Disabled transition (and the children's Paused cascade) reach the bridge on the
            // serialized listener queue and fire i=15535 events
            PubSubHandle connection =
                serverPubSub.runtime().components().connection(CONNECTION).orElseThrow();
            serverPubSub.runtime().disable(connection);

            NodeId connectionNodeId = PubSubNodeIds.componentNodeId(namespaceIndex, CONNECTION);

            // events for the cascaded children may interleave; select the connection-sourced
            // Disabled notification
            Variant[] match = awaitConnectionDisabled(received, connectionNodeId);

            assertEquals(NodeIds.PubSubStatusEventType, match[EVENT_TYPE].getValue());
            assertEquals(connectionNodeId, match[SOURCE_NODE].getValue());
            assertEquals(connectionNodeId, match[CONNECTION_ID].getValue());

            Object groupId = match[GROUP_ID].getValue();
            assertTrue(
                groupId == null || NodeId.NULL_VALUE.equals(groupId),
                "GroupId must be null for connection-sourced events, was: " + groupId);
          } finally {
            subscription.delete();
          }
        } finally {
          client.disconnect();
        }
      } finally {
        serverPubSub.close();
      }
    }
  }

  /** Poll until the connection-sourced {@code State == Disabled} notification arrives. */
  private static Variant[] awaitConnectionDisabled(
      LinkedBlockingQueue<Variant[]> received, NodeId connectionNodeId)
      throws InterruptedException {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);

    while (true) {
      long remaining = deadline - System.nanoTime();
      Variant[] next = received.poll(Math.max(remaining, 1L), TimeUnit.NANOSECONDS);
      assertNotNull(next, "timed out waiting for the connection Disabled event");

      if (Objects.equals(next[SOURCE_NODE].getValue(), connectionNodeId)
          && next[STATE].getValue() instanceof Number state
          && state.intValue() == PubSubState.Disabled.getValue()) {
        return next;
      }
    }
  }

  // region fixtures (spike-shaped client/filter helpers, LiveState-shaped loopback config)

  /**
   * A publisher-only loopback configuration: unicast 127.0.0.1 with ephemeral ports and an explicit
   * loopback {@code discoveryAddress}, so the engine never touches the default multicast group.
   */
  private static PubSubConfig publisherConfig(PublishedDataSetConfig dataSet)
      throws SocketException {

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4761)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(
                    WriterGroupConfig.builder(WRITER_GROUP)
                        .writerGroupId(ushort(21))
                        .publishingInterval(Duration.ofMillis(100))
                        .dataSetWriter(
                            DataSetWriterConfig.builder(WRITER)
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(31))
                                .build())
                        .build())
                .build())
        .build();
  }

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
                    .setApplicationName(LocalizedText.english("event reception test client"))
                    .setApplicationUri("urn:eclipse:milo:test:event-reception-client")
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

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
