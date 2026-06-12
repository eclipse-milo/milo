/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerTopics;
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the engine's addressed publish path (E1/E3): the engine always sends through {@link
 * PublisherChannel#send(ByteBuf, MessageAddress)} with fully resolved values — on UDP connections a
 * queue-less {@code AtMostOnce} DATA address, on broker connections the resolved queue name and
 * delivery guarantee — and a writer-level queue override partitions that writer's DataSetMessages
 * into their own NetworkMessages (Part 14 §6.4.2.5.1).
 *
 * <p>The transport is an in-memory stub registered for both UDP and MQTT connection configs, so the
 * data plane never touches the network. UDP connections with writer groups open real UDP discovery
 * sockets, so those tests pin an explicit loopback {@code discoveryAddress}.
 */
class AddressedSendEngineTest {

  private static final UUID FIELD_ID = new UUID(0L, 1L);

  private @Nullable PubSubService service;
  private @Nullable ExecutorService transportExecutor;

  @AfterEach
  void shutdownService() throws Exception {
    if (service != null) {
      service.close();
      service = null;
    }
    if (transportExecutor != null) {
      transportExecutor.shutdown();
      assertTrue(transportExecutor.awaitTermination(10, TimeUnit.SECONDS));
      transportExecutor = null;
    }
  }

  // region fixture

  /** One captured send: the payload bytes and the address the engine resolved for them. */
  record Sent(byte[] payload, @Nullable MessageAddress address) {}

  /** Captures every addressed send; never touches the network. */
  private static final class CapturingTransport implements TransportProvider {

    final BlockingQueue<Sent> sent = new LinkedBlockingQueue<>();

    @Override
    public String transportProfileUri() {
      return "urn:eclipse:milo:test:capturing-transport";
    }

    @Override
    public boolean supports(PubSubConnectionConfig connection) {
      return true;
    }

    @Override
    public PublisherChannel openPublisher(PublisherTransportContext context) {
      return new PublisherChannel() {
        @Override
        public CompletableFuture<Void> send(ByteBuf message) {
          // the engine always calls the addressed form; record a null address if this is
          // ever reached so tests fail visibly on the address assertions
          return send(message, null);
        }

        @Override
        public CompletableFuture<Void> send(ByteBuf message, @Nullable MessageAddress address) {
          try {
            byte[] bytes = new byte[message.readableBytes()];
            message.readBytes(bytes);
            sent.add(new Sent(bytes, address));
          } finally {
            message.release();
          }
          return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletableFuture<Void> closeAsync() {
          return CompletableFuture.completedFuture(null);
        }
      };
    }

    @Override
    public SubscriberChannel openSubscriber(SubscriberTransportContext context) {
      return new SubscriberChannel() {
        @Override
        public CompletableFuture<Void> closeAsync() {
          return CompletableFuture.completedFuture(null);
        }
      };
    }
  }

  private static void awaitTrue(String description, BooleanSupplier condition)
      throws InterruptedException {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while (System.nanoTime() < deadline) {
      if (condition.getAsBoolean()) {
        return;
      }
      Thread.sleep(10);
    }
    fail("timed out waiting for: " + description);
  }

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  private static PublishedDataSetConfig publishedDataSet() {
    return PublishedDataSetConfig.builder("PDS")
        .field(
            FieldDefinition.builder("F1").dataType(NodeIds.Int32).dataSetFieldId(FIELD_ID).build())
        .build();
  }

  private static DataSetSnapshot snapshotOf(PublishedDataSetReadContext context) {
    return DataSetSnapshot.builder(context).field("F1", new DataValue(Variant.ofInt32(42))).build();
  }

  private static DataSetWriterConfig.Builder writer(String name, int dataSetWriterId) {
    return DataSetWriterConfig.builder(name)
        .dataSet(new PublishedDataSetRef("PDS"))
        .dataSetWriterId(ushort(dataSetWriterId));
  }

  private CapturingTransport startService(PubSubConnectionConfig connection) throws Exception {
    var transport = new CapturingTransport();
    transportExecutor = Executors.newSingleThreadExecutor();

    PubSubConfig config =
        PubSubConfig.builder().publishedDataSet(publishedDataSet()).connection(connection).build();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(new PublishedDataSetRef("PDS"), AddressedSendEngineTest::snapshotOf)
            .build();

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(transport)
            .transportExecutor(transportExecutor)
            .build();

    service = PubSubService.create(config, bindings, serviceConfig);
    service.startup().get(10, TimeUnit.SECONDS);

    return transport;
  }

  private static DecodedNetworkMessage decode(byte[] frame) throws UaException {
    ByteBuf buffer = Unpooled.wrappedBuffer(frame);
    try {
      return new UadpMessageMapping()
          .decode(new DecodeContext(new DefaultEncodingContext()), buffer);
    } finally {
      buffer.release();
    }
  }

  /** Drain captured sends into {@code collected} until none arrive for {@code quietMillis}. */
  private static void drainUntilQuiet(
      CapturingTransport transport, List<Sent> collected, long quietMillis)
      throws InterruptedException {

    Sent next;
    while ((next = transport.sent.poll(quietMillis, TimeUnit.MILLISECONDS)) != null) {
      collected.add(next);
    }
  }

  // endregion

  @Test
  void udpDataAddressIsQueueLessAtMostOnceNotRetained() throws Exception {
    UdpConnectionConfig connection =
        UdpConnectionConfig.builder("conn")
            .publisherId(PublisherId.uint16(ushort(1)))
            .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
            // network safety: a connection with writer groups opens real UDP discovery sockets;
            // pin them to loopback, never the 224.0.2.14:4840 default
            .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(25))
                    .dataSetWriter(writer("W1", 1).build())
                    .build())
            .build();

    CapturingTransport transport = startService(connection);

    Sent first = transport.sent.poll(10, TimeUnit.SECONDS);
    assertNotNull(first, "no NetworkMessage published");

    MessageAddress address = first.address();
    assertNotNull(address, "engine did not use the addressed send form");
    assertNull(address.queueName());
    assertEquals(BrokerTransportQualityOfService.AtMostOnce, address.deliveryGuarantee());
    assertFalse(address.retain());
    assertEquals(MessageAddress.Kind.DATA, address.kind());
    assertEquals(MessageAddress.CONTENT_TYPE_UADP, address.contentTypeHint());

    // UDP connections have no MetaDataPublisher: collect a few more cycles, everything is DATA
    var collected = new ArrayList<Sent>(List.of(first));
    long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(500);
    while (System.nanoTime() < deadline) {
      Sent next = transport.sent.poll(50, TimeUnit.MILLISECONDS);
      if (next != null) {
        collected.add(next);
      }
    }
    assertTrue(collected.size() >= 2, "expected multiple publish cycles");
    for (Sent sent : collected) {
      assertNotNull(sent.address());
      assertEquals(MessageAddress.Kind.DATA, sent.address().kind());
    }
  }

  @Test
  void brokerWriterQueueOverridePartitionsNetworkMessages() throws Exception {
    // W1 publishes on the group queue; W2 has a queue (and QoS) override, so its DataSetMessages
    // are split into their own NetworkMessages addressed to its queue (§6.4.2.5.1)
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.uint16(ushort(9)))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(50))
                    .brokerTransport(
                        BrokerTransportSettings.builder()
                            .queueName("group/topic")
                            .requestedDeliveryGuarantee(BrokerTransportQualityOfService.AtLeastOnce)
                            .build())
                    .dataSetWriter(writer("W1", 1).build())
                    .dataSetWriter(
                        writer("W2", 2)
                            .brokerTransport(
                                BrokerTransportSettings.builder()
                                    .queueName("writer/topic")
                                    .requestedDeliveryGuarantee(
                                        BrokerTransportQualityOfService.ExactlyOnce)
                                    .build())
                            .build())
                    .build())
            .build();

    CapturingTransport transport = startService(connection);

    // collect until both partitions were observed, then stop the publisher and drain
    var collected = new ArrayList<Sent>();
    awaitTrue(
        "data NetworkMessages on both the group queue and the writer queue",
        () -> {
          transport.sent.drainTo(collected);
          return collected.stream().anyMatch(s -> dataOn(s, "group/topic"))
              && collected.stream().anyMatch(s -> dataOn(s, "writer/topic"));
        });

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    service.disable(group);
    drainUntilQuiet(transport, collected, 300);

    List<Sent> dataSent =
        collected.stream().filter(s -> isKind(s, MessageAddress.Kind.DATA)).toList();
    List<Sent> metaSent =
        collected.stream().filter(s -> isKind(s, MessageAddress.Kind.METADATA)).toList();

    int sharedCount = 0;
    int overrideCount = 0;
    for (Sent sent : dataSent) {
      MessageAddress address = sent.address();
      assertNotNull(address);
      assertFalse(address.retain());
      assertEquals(MessageAddress.CONTENT_TYPE_UADP, address.contentTypeHint());

      DecodedNetworkMessage decoded = decode(sent.payload());
      assertEquals(1, decoded.messages().size(), "one DataSetMessage per partition NetworkMessage");

      if ("group/topic".equals(address.queueName())) {
        // the shared partition: W1's DataSetMessages at the group's delivery guarantee
        sharedCount++;
        assertEquals(BrokerTransportQualityOfService.AtLeastOnce, address.deliveryGuarantee());
        assertEquals(ushort(1), decoded.messages().get(0).dataSetWriterId());
      } else if ("writer/topic".equals(address.queueName())) {
        // the override partition: only W2's DataSetMessages, at W2's delivery guarantee
        overrideCount++;
        assertEquals(BrokerTransportQualityOfService.ExactlyOnce, address.deliveryGuarantee());
        assertEquals(ushort(2), decoded.messages().get(0).dataSetWriterId());
      } else {
        fail("unexpected data queue: " + address.queueName());
      }
    }
    assertTrue(sharedCount >= 1, "no shared-partition NetworkMessage");
    assertTrue(overrideCount >= 1, "no override-partition NetworkMessage");

    // one retained metadata publication per writer activation, nothing periodic
    assertEquals(2, metaSent.size(), "expected one metadata publication per writer");

    // diagnostics: networkMessagesSent ticks PER NetworkMessage sent
    int expectedGroupNetworkMessages = dataSent.size();
    int expectedConnectionNetworkMessages = dataSent.size() + metaSent.size();
    awaitTrue(
        "diagnostics counters to settle",
        () -> {
          var snapshot = service.diagnostics().snapshot();
          return snapshot.get("conn/WG").networkMessagesSent() == expectedGroupNetworkMessages
              && snapshot.get("conn").networkMessagesSent() == expectedConnectionNetworkMessages;
        });

    var snapshot = service.diagnostics().snapshot();
    assertEquals(dataSent.size(), snapshot.get("conn/WG").dataSetMessagesSent());
    assertEquals(sharedCount, snapshot.get("conn/WG/W1").dataSetMessagesSent());
    assertEquals(overrideCount, snapshot.get("conn/WG/W2").dataSetMessagesSent());
  }

  @Test
  void brokerSharedQueueDerivedFromTopicTreeWithPrefixProperty() throws Exception {
    // no queue names configured anywhere: the engine derives the §7.3.4.7 topics, with the
    // <Prefix> level from the 0:MqttTopicPrefix connection property
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.uint16(ushort(7)))
            .property(BrokerTopics.MQTT_TOPIC_PREFIX_PROPERTY, Variant.ofString("factory"))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(50))
                    .dataSetWriter(writer("W1", 1).build())
                    .build())
            .build();

    CapturingTransport transport = startService(connection);

    var collected = new ArrayList<Sent>();
    awaitTrue(
        "a data and a metadata NetworkMessage",
        () -> {
          transport.sent.drainTo(collected);
          return collected.stream().anyMatch(s -> isKind(s, MessageAddress.Kind.DATA))
              && collected.stream().anyMatch(s -> isKind(s, MessageAddress.Kind.METADATA));
        });

    Sent data =
        collected.stream()
            .filter(s -> isKind(s, MessageAddress.Kind.DATA))
            .findFirst()
            .orElseThrow();
    assertEquals("factory/uadp/data/7/WG", data.address().queueName());
    assertEquals(BrokerTransportQualityOfService.AtMostOnce, data.address().deliveryGuarantee());
    assertFalse(data.address().retain());

    Sent metaData =
        collected.stream()
            .filter(s -> isKind(s, MessageAddress.Kind.METADATA))
            .findFirst()
            .orElseThrow();
    assertEquals("factory/uadp/metadata/7/WG/W1", metaData.address().queueName());
    assertEquals(
        BrokerTransportQualityOfService.AtLeastOnce, metaData.address().deliveryGuarantee());
    assertTrue(metaData.address().retain());
  }

  @Test
  void uadpEncodeAttributesContributingWritersInPayloadOrder() throws Exception {
    // E1: encode returns a list and every EncodedNetworkMessage carries writer attribution;
    // the UADP mapping returns a singleton attributed with every draft's writer in payload order
    WriterGroupConfig group = WriterGroupConfig.builder("WG").writerGroupId(ushort(1)).build();

    List<EncodedNetworkMessage> encoded =
        new UadpMessageMapping()
            .encode(
                EncodeContext.of(
                    new DefaultEncodingContext(),
                    PublisherId.uint16(ushort(1)),
                    group,
                    uint(0),
                    ushort(1),
                    ushort(0),
                    null,
                    List.of(draft("W1", 1, 11), draft("W2", 2, 22))));

    assertEquals(1, encoded.size());
    try {
      assertEquals(
          List.of(
              new EncodedNetworkMessage.Writer("W1", ushort(1)),
              new EncodedNetworkMessage.Writer("W2", ushort(2))),
          encoded.get(0).writers());
    } finally {
      encoded.forEach(message -> message.data().release());
    }
  }

  /**
   * Boundary of the writer-side maxNetworkMessageSize budget (Part 14 §6.2.5.5; enforcement is
   * strict greater-than): a NetworkMessage whose encoded size EXACTLY equals the group's budget is
   * sent. The size of the fixed message — fixed-width headers and a fixed-width Int32 value, so
   * every cycle encodes to the same size — is captured first under no limit, then the service is
   * reconfigured with that exact size as the budget.
   */
  @Test
  void networkMessageExactlyAtMaxNetworkMessageSizeIsSent() throws Exception {
    int port = freeUdpPort();
    int discoveryPort = freeUdpPort();

    CapturingTransport transport = startService(sizedConnection(port, discoveryPort, uint(0)));

    Sent first = transport.sent.poll(10, TimeUnit.SECONDS);
    assertNotNull(first, "no NetworkMessage published");
    int encodedSize = first.payload().length;

    // reconfigure with the budget set to exactly the captured size; the ports are reused so only
    // maxNetworkMessageSize differs
    PubSubConfig exactBudget =
        PubSubConfig.builder()
            .publishedDataSet(publishedDataSet())
            .connection(sizedConnection(port, discoveryPort, uint(encodedSize)))
            .build();
    service.reconfigure(exactBudget, PubSubService.ReconfigureMode.DISABLE_AFFECTED);
    transport.sent.clear();

    // publishing is periodic, so multiple post-reconfigure cycles must keep sending the
    // exactly-at-budget message (a backlog message captured mid-reconfigure has the same size,
    // but cannot account for more than one poll)
    for (int i = 0; i < 3; i++) {
      Sent atLimit = transport.sent.poll(10, TimeUnit.SECONDS);
      assertNotNull(atLimit, "NetworkMessage exactly at maxNetworkMessageSize was not sent");
      assertEquals(encodedSize, atLimit.payload().length);
    }

    // nothing was skipped: no Bad_EncodingLimitsExceeded recorded at the group path
    assertNull(service.diagnostics().snapshot().get("conn/WG").lastError());
  }

  /** A UDP connection whose single writer group "WG" carries the given maxNetworkMessageSize. */
  private static UdpConnectionConfig sizedConnection(
      int port, int discoveryPort, UInteger maxNetworkMessageSize) {

    return UdpConnectionConfig.builder("conn")
        .publisherId(PublisherId.uint16(ushort(1)))
        .address(UdpDatagramAddress.unicast("127.0.0.1", port))
        // network safety: pin discovery to loopback, never the 224.0.2.14:4840 default
        .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", discoveryPort))
        .writerGroup(
            WriterGroupConfig.builder("WG")
                .writerGroupId(ushort(1))
                .publishingInterval(Duration.ofMillis(25))
                .maxNetworkMessageSize(maxNetworkMessageSize)
                .dataSetWriter(writer("W1", 1).build())
                .build())
        .build();
  }

  private static DataSetMessageDraft draft(String writerName, int dataSetWriterId, int value) {
    return new DataSetMessageDraft(
        writer(writerName, dataSetWriterId).build(),
        ushort(0),
        null,
        null,
        new ConfigurationVersionDataType(uint(0), uint(0)),
        false,
        List.of(new DataValue(Variant.ofInt32(value))));
  }

  private static boolean isKind(Sent sent, MessageAddress.Kind kind) {
    return sent.address() != null && sent.address().kind() == kind;
  }

  private static boolean dataOn(Sent sent, String queueName) {
    return isKind(sent, MessageAddress.Kind.DATA) && queueName.equals(sent.address().queueName());
  }
}
