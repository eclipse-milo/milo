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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataMapper;
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
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMetaDataAnnouncement;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the engine's DataSetMetaData publication on broker connections: retained metadata
 * published at writer activation before the writer's first data NetworkMessage (Part 14 §5.2.3
 * best-effort), addressed per the resolved metadata queue and QoS (Table 204: retain true, QoS 1
 * default), republished on reconfiguration when the derived metadata changed, published
 * periodically when the effective {@code MetaDataUpdateTime} is positive (writer-level settings
 * authoritative when present, including 0 = disabled; group-level is a Milo-local fallback), and
 * entirely absent on UDP connections.
 *
 * <p>Metadata sequence numbers come from the per-PublisherId announcement counter shared with UADP
 * discovery and other writers: a single writer's stream is strictly increasing but may have gaps —
 * these tests never assert gap-free sequences.
 */
class MetaDataPublisherTest {

  private static final UUID FIELD_ID = new UUID(0L, 1L);
  private static final QualifiedName MILO_SOURCE_KEY = new QualifiedName(0, "MiloSourceKey");

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
      return () -> CompletableFuture.completedFuture(null);
    }
  }

  /**
   * Fails the first {@code metaDataFailuresRemaining} METADATA-kind sends — exceptionally completed
   * futures, or synchronous throws when {@code throwSynchronously} — and captures everything else.
   * Conforming per {@link PublisherChannel#send}: the buffer is released on every path.
   */
  private static final class FailingMetaDataTransport implements TransportProvider {

    final BlockingQueue<Sent> sent = new LinkedBlockingQueue<>();
    final AtomicInteger metaDataFailuresRemaining;
    final boolean throwSynchronously;

    FailingMetaDataTransport(int metaDataFailures, boolean throwSynchronously) {
      this.metaDataFailuresRemaining = new AtomicInteger(metaDataFailures);
      this.throwSynchronously = throwSynchronously;
    }

    @Override
    public String transportProfileUri() {
      return "urn:eclipse:milo:test:failing-metadata-transport";
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
          return send(message, null);
        }

        @Override
        public CompletableFuture<Void> send(ByteBuf message, @Nullable MessageAddress address) {
          if (address != null
              && address.kind() == MessageAddress.Kind.METADATA
              && metaDataFailuresRemaining.getAndUpdate(n -> Math.max(0, n - 1)) > 0) {
            message.release();
            if (throwSynchronously) {
              throw new IllegalStateException("send failed synchronously (test)");
            }
            return CompletableFuture.failedFuture(
                new IllegalStateException("send failed (test, e.g. broker not yet connected)"));
          }
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
      return () -> CompletableFuture.completedFuture(null);
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

  private static PublishedDataSetConfig publishedDataSet(boolean withSecondField) {
    PublishedDataSetConfig.Builder builder =
        PublishedDataSetConfig.builder("PDS")
            .field(
                FieldDefinition.builder("F1")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(FIELD_ID)
                    .build());
    if (withSecondField) {
      builder.field(
          FieldDefinition.builder("F2")
              .dataType(NodeIds.String)
              .dataSetFieldId(new UUID(0L, 2L))
              .build());
    }
    return builder.build();
  }

  private static DataSetSnapshot snapshotOf(PublishedDataSetReadContext context) {
    return DataSetSnapshot.builder(context).field("F1", new DataValue(Variant.ofInt32(42))).build();
  }

  private static DataSetWriterConfig.Builder writer(String name, int dataSetWriterId) {
    return DataSetWriterConfig.builder(name)
        .dataSet(new PublishedDataSetRef("PDS"))
        .dataSetWriterId(ushort(dataSetWriterId));
  }

  private CapturingTransport startService(PubSubConfig config) throws Exception {
    return startService(config, new CapturingTransport());
  }

  private <T extends TransportProvider> T startService(PubSubConfig config, T transport)
      throws Exception {

    transportExecutor = Executors.newSingleThreadExecutor();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(new PublishedDataSetRef("PDS"), MetaDataPublisherTest::snapshotOf)
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

  private static UadpMetaDataAnnouncement decodeAnnouncement(byte[] payload) {
    ByteBuf buffer = Unpooled.wrappedBuffer(payload);
    try {
      var decoded =
          new UadpMessageMapping()
              .decodeMessage(DecodeContext.of(new DefaultEncodingContext()), buffer);
      return assertInstanceOf(UadpMetaDataAnnouncement.class, decoded);
    } finally {
      buffer.release();
    }
  }

  private static boolean isKind(Sent sent, MessageAddress.Kind kind) {
    return sent.address() != null && sent.address().kind() == kind;
  }

  private static void assertNoMiloSourceKey(DataSetMetaDataType metaData) {
    FieldMetaData[] fields = metaData.getFields();
    assertNotNull(fields);
    for (FieldMetaData field : fields) {
      KeyValuePair[] properties = field.getProperties();
      if (properties != null) {
        for (KeyValuePair property : properties) {
          if (MILO_SOURCE_KEY.equals(property.getKey())) {
            fail("published metadata leaks the MiloSourceKey property: " + field.getName());
          }
        }
      }
    }
  }

  // endregion

  @Test
  void retainedMetaDataPublishedAtActivationBeforeFirstDataMessage() throws Exception {
    PublishedDataSetConfig dataSet = publishedDataSet(false);

    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.string("line-7"))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(50))
                    .dataSetWriter(
                        writer("W1", 1)
                            .brokerTransport(
                                BrokerTransportSettings.builder()
                                    .metaDataQueueName("md/explicit")
                                    .build())
                            .build())
                    .build())
            .build();

    CapturingTransport transport =
        startService(
            PubSubConfig.builder().publishedDataSet(dataSet).connection(connection).build());

    // §5.2.3 metadata-before-data: the first send on the connection is the retained metadata
    Sent first = transport.sent.poll(10, TimeUnit.SECONDS);
    assertNotNull(first, "nothing published");

    MessageAddress address = first.address();
    assertNotNull(address, "engine did not use the addressed send form");
    assertEquals(MessageAddress.Kind.METADATA, address.kind());
    assertEquals("md/explicit", address.queueName());
    assertEquals(BrokerTransportQualityOfService.AtLeastOnce, address.deliveryGuarantee());
    assertTrue(address.retain());
    assertEquals(MessageAddress.CONTENT_TYPE_UADP, address.contentTypeHint());

    // the payload is a UADP metadata announcement for this writer
    UadpMetaDataAnnouncement announcement = decodeAnnouncement(first.payload());
    assertEquals(PublisherId.string("line-7"), announcement.publisherId());
    assertEquals(ushort(1), announcement.dataSetWriterId());
    assertEquals(StatusCode.GOOD, announcement.statusCode());
    assertEquals("PDS", announcement.metaData().getName());
    assertNotNull(announcement.metaData().getFields());
    assertEquals(1, announcement.metaData().getFields().length);
    assertEquals("F1", announcement.metaData().getFields()[0].getName());

    // the wire-bound metadata strips the reserved 0:MiloSourceKey property that the
    // configuration-model mapping carries for KeyFieldAddress-sourced fields
    DataSetMetaDataType unstripped = DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false);
    KeyValuePair[] unstrippedProperties = unstripped.getFields()[0].getProperties();
    assertNotNull(unstrippedProperties);
    assertEquals(MILO_SOURCE_KEY, unstrippedProperties[0].getKey());
    assertNoMiloSourceKey(announcement.metaData());

    // data follows the metadata
    Sent next = transport.sent.poll(10, TimeUnit.SECONDS);
    assertNotNull(next, "no data NetworkMessage published");
    assertEquals(MessageAddress.Kind.DATA, next.address().kind());

    // no update time configured anywhere: the activation publish is the only metadata send
    // (data keeps flowing, so collect over a bounded window)
    var collected = new ArrayList<Sent>();
    long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(500);
    while (System.nanoTime() < deadline) {
      Sent polled = transport.sent.poll(50, TimeUnit.MILLISECONDS);
      if (polled != null) {
        collected.add(polled);
      }
    }
    assertTrue(
        collected.stream().noneMatch(s -> isKind(s, MessageAddress.Kind.METADATA)),
        "unexpected metadata republication without an update time");
  }

  @Test
  void metaDataQueueGroupFallbackIsMiloLocalAndWriterOverrides() throws Exception {
    // W1 has no broker settings: its metadata goes to the group's (Milo-local) metaDataQueueName;
    // W2's writer-level metaDataQueueName overrides the group's. The group's ExactlyOnce delivery
    // guarantee applies to both (W2's settings carry NotSpecified, which inherits).
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.uint16(ushort(5)))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(50))
                    .brokerTransport(
                        BrokerTransportSettings.builder()
                            .metaDataQueueName("group-md")
                            .requestedDeliveryGuarantee(BrokerTransportQualityOfService.ExactlyOnce)
                            .build())
                    .dataSetWriter(writer("W1", 1).build())
                    .dataSetWriter(
                        writer("W2", 2)
                            .brokerTransport(
                                BrokerTransportSettings.builder()
                                    .metaDataQueueName("writer-md")
                                    .build())
                            .build())
                    .build())
            .build();

    CapturingTransport transport =
        startService(
            PubSubConfig.builder()
                .publishedDataSet(publishedDataSet(false))
                .connection(connection)
                .build());

    var metaSent = new ArrayList<Sent>();
    awaitTrue(
        "both writers' metadata publications",
        () -> {
          var drained = new ArrayList<Sent>();
          transport.sent.drainTo(drained);
          drained.stream()
              .filter(s -> isKind(s, MessageAddress.Kind.METADATA))
              .forEach(metaSent::add);
          return metaSent.size() >= 2;
        });

    assertEquals(2, metaSent.size());
    for (Sent sent : metaSent) {
      MessageAddress address = sent.address();
      assertTrue(address.retain());
      assertEquals(BrokerTransportQualityOfService.ExactlyOnce, address.deliveryGuarantee());

      UadpMetaDataAnnouncement announcement = decodeAnnouncement(sent.payload());
      if (announcement.dataSetWriterId().equals(ushort(1))) {
        assertEquals("group-md", address.queueName());
      } else {
        assertEquals(ushort(2), announcement.dataSetWriterId());
        assertEquals("writer-md", address.queueName());
      }
    }
  }

  @Test
  void periodicPublicationFollowsEffectiveUpdateTime() throws Exception {
    // group-level metaDataUpdateTime (Milo-local) drives writers without their own settings (W1);
    // a writer-level BrokerTransportSettings is authoritative for the update time when PRESENT —
    // including its default 0 = on-change only — so W2 never publishes periodically even though
    // the group configures 100 ms
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.uint16(ushort(5)))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(50))
                    .brokerTransport(
                        BrokerTransportSettings.builder()
                            .metaDataUpdateTime(Duration.ofMillis(100))
                            .build())
                    .dataSetWriter(writer("W1", 1).build())
                    .dataSetWriter(
                        writer("W2", 2)
                            .brokerTransport(
                                BrokerTransportSettings.builder()
                                    .metaDataQueueName("w2-md")
                                    .build())
                            .build())
                    .build())
            .build();

    CapturingTransport transport =
        startService(
            PubSubConfig.builder()
                .publishedDataSet(publishedDataSet(false))
                .connection(connection)
                .build());

    var w1Announcements = new ArrayList<UadpMetaDataAnnouncement>();
    var w2Announcements = new ArrayList<UadpMetaDataAnnouncement>();
    awaitTrue(
        "periodic metadata publications for W1",
        () -> {
          var drained = new ArrayList<Sent>();
          transport.sent.drainTo(drained);
          for (Sent sent : drained) {
            if (isKind(sent, MessageAddress.Kind.METADATA)) {
              UadpMetaDataAnnouncement announcement = decodeAnnouncement(sent.payload());
              if (announcement.dataSetWriterId().equals(ushort(1))) {
                w1Announcements.add(announcement);
              } else {
                w2Announcements.add(announcement);
              }
            }
          }
          return w1Announcements.size() >= 3;
        });

    // W1: activation publish + periodic publications; W2: activation publish only
    assertTrue(w1Announcements.size() >= 3);
    assertEquals(1, w2Announcements.size());

    // a single writer's sequence numbers are strictly increasing; the per-PublisherId counter is
    // shared with other writers, so gaps are expected and never asserted against
    for (int i = 1; i < w1Announcements.size(); i++) {
      int previous = w1Announcements.get(i - 1).sequenceNumber().intValue();
      int current = w1Announcements.get(i).sequenceNumber().intValue();
      assertTrue(
          current > previous,
          "sequence numbers not increasing: %d -> %d".formatted(previous, current));
    }
  }

  @Test
  void reconfigureRepublishesChangedDataSetMetaDataOnly() throws Exception {
    PubSubConfig initialConfig = brokerConfig(publishedDataSet(false));

    CapturingTransport transport = startService(initialConfig);

    Sent first = transport.sent.poll(10, TimeUnit.SECONDS);
    assertNotNull(first);
    assertEquals(MessageAddress.Kind.METADATA, first.address().kind());
    assertEquals(1, decodeAnnouncement(first.payload()).metaData().getFields().length);

    // a reconfiguration that changes nothing republishes nothing (data keeps flowing)
    service.reconfigure(
        brokerConfig(publishedDataSet(false)), PubSubService.ReconfigureMode.DISABLE_AFFECTED);
    long quietDeadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(500);
    while (System.nanoTime() < quietDeadline) {
      Sent polled = transport.sent.poll(50, TimeUnit.MILLISECONDS);
      if (polled != null) {
        assertEquals(
            MessageAddress.Kind.DATA,
            polled.address().kind(),
            "metadata republished without change");
      }
    }

    // a reconfiguration that changes the writer's dataset republishes its (changed) metadata
    service.reconfigure(
        brokerConfig(publishedDataSet(true)), PubSubService.ReconfigureMode.DISABLE_AFFECTED);

    var republished = new ArrayList<UadpMetaDataAnnouncement>();
    awaitTrue(
        "metadata republication after the dataset change",
        () -> {
          var drained = new ArrayList<Sent>();
          transport.sent.drainTo(drained);
          for (Sent sent : drained) {
            if (isKind(sent, MessageAddress.Kind.METADATA)) {
              republished.add(decodeAnnouncement(sent.payload()));
            }
          }
          return !republished.isEmpty();
        });

    UadpMetaDataAnnouncement announcement = republished.get(0);
    assertEquals(ushort(1), announcement.dataSetWriterId());
    assertEquals(2, announcement.metaData().getFields().length);
    assertEquals("F2", announcement.metaData().getFields()[1].getName());
  }

  @Test
  void udpConnectionsPublishNoMetaData() throws Exception {
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
                    // broker transport settings on a UDP group are inert config: no metadata
                    // publication may result from them
                    .brokerTransport(
                        BrokerTransportSettings.builder()
                            .metaDataQueueName("group-md")
                            .metaDataUpdateTime(Duration.ofMillis(50))
                            .build())
                    .dataSetWriter(writer("W1", 1).build())
                    .build())
            .build();

    CapturingTransport transport =
        startService(
            PubSubConfig.builder()
                .publishedDataSet(publishedDataSet(false))
                .connection(connection)
                .build());

    var collected = new ArrayList<Sent>();
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
      assertNull(sent.address().queueName());
    }
  }

  /**
   * A failed activation send — the common case is the activation publish racing the transport's
   * asynchronous broker connect, which fails fast until connected — must not be recorded as
   * published: the send is retried with bounded backoff until the retained metadata lands.
   */
  @Test
  void failedActivationSendIsRetriedUntilTheRetainedMetaDataLands() throws Exception {
    var transport = new FailingMetaDataTransport(2, false);

    startService(brokerConfig(publishedDataSet(false)), transport);

    // both initial failures are consumed (activation publish + first retry), then a retry succeeds
    Sent metaData = null;
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(15);
    while (metaData == null && System.nanoTime() < deadline) {
      Sent polled = transport.sent.poll(100, TimeUnit.MILLISECONDS);
      if (polled != null && isKind(polled, MessageAddress.Kind.METADATA)) {
        metaData = polled;
      }
    }

    assertNotNull(metaData, "retained metadata never landed despite retries");
    assertEquals(0, transport.metaDataFailuresRemaining.get());

    UadpMetaDataAnnouncement announcement = decodeAnnouncement(metaData.payload());
    assertEquals(ushort(1), announcement.dataSetWriterId());
    assertEquals("PDS", announcement.metaData().getName());
  }

  /**
   * A channel that throws synchronously from send (in violation of the {@link
   * PublisherChannel#send} contract's spirit but possible through the SPI) is contained: the
   * failure is diagnosed, the periodic publication task survives, and publication resumes.
   */
  @Test
  void synchronousSendThrowDoesNotCancelPeriodicPublication() throws Exception {
    var transport = new FailingMetaDataTransport(2, true);

    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.uint16(ushort(5)))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(50))
                    .brokerTransport(
                        BrokerTransportSettings.builder()
                            .metaDataQueueName("group-md")
                            .metaDataUpdateTime(Duration.ofMillis(100))
                            .build())
                    .dataSetWriter(writer("W1", 1).build())
                    .build())
            .build();

    startService(
        PubSubConfig.builder()
            .publishedDataSet(publishedDataSet(false))
            .connection(connection)
            .build(),
        transport);

    // the activation publish and the first periodic publication throw; if the throw escaped into
    // the scheduled task, scheduleAtFixedRate semantics would cancel periodic publication forever
    var announcements = new ArrayList<UadpMetaDataAnnouncement>();
    awaitTrue(
        "periodic metadata publication resumes after synchronous send throws",
        () -> {
          var drained = new ArrayList<Sent>();
          transport.sent.drainTo(drained);
          drained.stream()
              .filter(s -> isKind(s, MessageAddress.Kind.METADATA))
              .forEach(s -> announcements.add(decodeAnnouncement(s.payload())));
          return announcements.size() >= 2;
        });

    assertEquals(0, transport.metaDataFailuresRemaining.get());
    for (UadpMetaDataAnnouncement announcement : announcements) {
      assertEquals(ushort(1), announcement.dataSetWriterId());
    }
  }

  private static PubSubConfig brokerConfig(PublishedDataSetConfig dataSet) {
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.uint16(ushort(5)))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(50))
                    .dataSetWriter(
                        writer("W1", 1)
                            .brokerTransport(
                                BrokerTransportSettings.builder().metaDataQueueName("md").build())
                            .build())
                    .build())
            .build();

    return PubSubConfig.builder().publishedDataSet(dataSet).connection(connection).build();
  }
}
