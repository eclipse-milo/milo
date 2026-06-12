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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpDiscoveryProbe;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for UADP discovery (OPC UA Part 14 §7.2.4.6) between two in-process services
 * over loopback UDP: a publisher service whose responder answers DataSetMetaData probes, and a
 * subscriber service whose {@link MetadataPolicy#REQUEST_IF_MISSING} readers emit them.
 *
 * <p>Network safety: the data plane is unicast 127.0.0.1 with an ephemeral port; discovery uses a
 * unique organization-local multicast group (239.255.71.1) on a unique non-4840 port per test,
 * joined on the loopback interface only. Every connection configures its discoveryAddress
 * explicitly so the engine never falls back to the 224.0.2.14:4840 default.
 *
 * <p>Timeouts are generous because the first probe is randomly delayed 100-500 ms and retried with
 * doubling intervals (§7.2.4.6.12.2).
 */
class DiscoveryEndToEndTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(15);

  private static final String DISCOVERY_GROUP = "239.255.71.1";
  private static final String LOOPBACK = "127.0.0.1";

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(21101));

  private static final UUID TEMPERATURE_FIELD_ID = new UUID(0L, 0x711L);
  private static final UUID STATUS_FIELD_ID = new UUID(0L, 0x712L);

  private final List<PubSubService> services = new CopyOnWriteArrayList<>();
  private final List<AutoCloseable> sockets = new CopyOnWriteArrayList<>();

  @AfterEach
  void tearDown() throws Exception {
    for (PubSubService service : services) {
      try {
        service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        // best effort cleanup; failures are reported by the tests themselves
      }
    }
    services.clear();

    for (AutoCloseable socket : sockets) {
      socket.close();
    }
    sockets.clear();
  }

  // region fixtures

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  private static UdpDatagramAddress discoveryAddress(int discoveryPort) {
    return UdpDatagramAddress.multicast(DISCOVERY_GROUP, discoveryPort).networkInterface(LOOPBACK);
  }

  private static PublishedDataSetConfig dataSet() {
    return PublishedDataSetConfig.builder("ds-e2e")
        .field(
            FieldDefinition.builder("temperature")
                .dataType(NodeIds.Double)
                .dataSetFieldId(TEMPERATURE_FIELD_ID)
                .build())
        .field(
            FieldDefinition.builder("status")
                .dataType(NodeIds.String)
                .dataSetFieldId(STATUS_FIELD_ID)
                .build())
        .configurationVersion(uint(5), uint(2))
        .build();
  }

  /**
   * Publisher config: one writer with the given id on "ds-e2e", default UADP-Dynamic masks
   * (PublisherId and PayloadHeader on the wire), explicit loopback discovery address.
   */
  private static PubSubConfig publisherConfig(int dataPort, int discoveryPort, int writerId) {
    PublishedDataSetConfig dataSet = dataSet();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast(LOOPBACK, dataPort))
                .discoveryAddress(discoveryAddress(discoveryPort))
                .writerGroup(
                    WriterGroupConfig.builder("grp")
                        .writerGroupId(ushort(1))
                        .publishingInterval(Duration.ofMillis(100))
                        .dataSetWriter(
                            DataSetWriterConfig.builder("writer")
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(writerId))
                                .build())
                        .build())
                .build())
        .build();
  }

  private static PubSubConfig subscriberConfig(
      int dataPort, int discoveryPort, DataSetReaderConfig reader) {

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("sub-conn")
                .address(UdpDatagramAddress.unicast(LOOPBACK, dataPort))
                .discoveryAddress(discoveryAddress(discoveryPort))
                .readerGroup(ReaderGroupConfig.builder("rgrp").dataSetReader(reader).build())
                .build())
        .build();
  }

  /** A source that supplies a placeholder value for every field of its dataset. */
  private static DataSetSnapshot anySnapshot(PublishedDataSetReadContext context) {
    DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
    for (FieldDefinition field : context.fields()) {
      builder.field(field.getName(), new DataValue(Variant.ofDouble(1.0)));
    }
    return builder.build();
  }

  private PubSubService startPublisher(PubSubConfig config) throws Exception {
    PubSubService publisher =
        PubSubService.create(
            config,
            PubSubBindings.builder()
                .source(dataSet().ref(), DiscoveryEndToEndTest::anySnapshot)
                .build());
    services.add(publisher);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return publisher;
  }

  private PubSubService startSubscriber(PubSubConfig config) throws Exception {
    PubSubService subscriber = PubSubService.create(config);
    services.add(subscriber);
    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return subscriber;
  }

  // endregion

  // region raw probe capture

  /** A capture-only socket joined to the discovery group on the loopback interface. */
  private final class RawDiscoveryCapture implements AutoCloseable {

    final DatagramChannel channel;

    RawDiscoveryCapture(int port) throws IOException {
      NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByName(LOOPBACK));
      assertNotNull(ni, "no loopback network interface for " + LOOPBACK);

      channel = DatagramChannel.open(StandardProtocolFamily.INET);
      channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
      channel.bind(new InetSocketAddress(port));
      channel.join(InetAddress.getByName(DISCOVERY_GROUP), ni);
      channel.configureBlocking(false);

      sockets.add(this);
    }

    @Nullable UadpDiscoveryProbe pollProbe() throws IOException {
      ByteBuffer buffer = ByteBuffer.allocate(65535);
      if (channel.receive(buffer) == null) {
        return null;
      }
      buffer.flip();
      byte[] bytes = new byte[buffer.remaining()];
      buffer.get(bytes);

      ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
      try {
        if (new UadpMessageMapping()
                .decodeMessage(DecodeContext.of(new DefaultEncodingContext()), byteBuf)
            instanceof UadpDiscoveryProbe probe) {
          return probe;
        }
        return null;
      } finally {
        byteBuf.release();
      }
    }

    UadpDiscoveryProbe awaitProbe() throws IOException, InterruptedException {
      long deadline = System.nanoTime() + TIMEOUT.toNanos();
      while (System.nanoTime() < deadline) {
        UadpDiscoveryProbe probe = pollProbe();
        if (probe != null) {
          return probe;
        }
        Thread.sleep(10);
      }
      return fail("timed out waiting for a discovery probe on the discovery group");
    }

    void assertNoProbe(long millis, String description) throws IOException, InterruptedException {
      long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(millis);
      while (System.nanoTime() < deadline) {
        UadpDiscoveryProbe probe = pollProbe();
        if (probe != null) {
          fail("unexpected discovery probe (" + description + "): " + probe);
        }
        Thread.sleep(10);
      }
    }

    @Override
    public void close() throws IOException {
      channel.close();
    }
  }

  // endregion

  // region helpers

  private static <T> T awaitEvent(BlockingQueue<T> events, Predicate<T> predicate)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (true) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        return fail("timed out waiting for a matching event");
      }
      T event = events.poll(Math.min(remaining, 100_000_000L), TimeUnit.NANOSECONDS);
      if (event != null && predicate.test(event)) {
        return event;
      }
    }
  }

  private static void awaitTrue(BooleanSupplier condition, String description)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (!condition.getAsBoolean()) {
      if (System.nanoTime() >= deadline) {
        fail("timed out waiting for: " + description);
      }
      Thread.sleep(25);
    }
  }

  // endregion

  /**
   * The full REQUEST_IF_MISSING loop: a reader without configured metadata probes the discovery
   * address, the publisher answers with a DataSetMetaData announcement, the metadata is applied
   * (MetaDataReceivedEvent), decoded fields carry the configured names instead of the positional
   * fallbacks, and the reader completes startup.
   */
  @Test
  void requestIfMissingReaderObtainsMetaDataEndToEnd() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    startPublisher(publisherConfig(dataPort, discoveryPort, 1));

    DataSetReaderConfig reader =
        DataSetReaderConfig.builder("reader")
            .publisherId(PUBLISHER_ID)
            .dataSetWriterId(ushort(1))
            .metadataPolicy(MetadataPolicy.REQUEST_IF_MISSING)
            .build(); // no configured metadata

    PubSubService subscriber =
        PubSubService.create(
            subscriberConfig(dataPort, discoveryPort, reader), PubSubBindings.builder().build());
    services.add(subscriber);

    var dataEvents = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var metaDataEvents = new LinkedBlockingQueue<MetaDataReceivedEvent>();
    subscriber.addDataSetListener(
        new DataSetReaderRef("sub-conn", "rgrp", "reader"), dataEvents::add);
    subscriber.addMetaDataListener(metaDataEvents::add);

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // probe -> announcement -> metadata applied
    MetaDataReceivedEvent metaDataEvent = awaitEvent(metaDataEvents, event -> true);
    assertEquals("ds-e2e", metaDataEvent.dataSetName());
    assertEquals(uint(5), metaDataEvent.configurationVersion().getMajorVersion());
    assertEquals(uint(2), metaDataEvent.configurationVersion().getMinorVersion());
    assertEquals("sub-conn/rgrp/reader", metaDataEvent.reader().path());

    // decoded fields carry the announced (configured) names, not the Field_N fallbacks
    DataSetReceivedEvent dataEvent =
        awaitEvent(
            dataEvents,
            event ->
                !event.fields().isEmpty() && "temperature".equals(event.fields().get(0).name()));

    assertEquals("ds-e2e", dataEvent.dataSetName());
    assertNotNull(dataEvent.metaData());
    assertEquals(2, dataEvent.fields().size());
    assertEquals("temperature", dataEvent.fields().get(0).name());
    assertEquals(TEMPERATURE_FIELD_ID, dataEvent.fields().get(0).dataSetFieldId());
    assertEquals("status", dataEvent.fields().get(1).name());
    assertEquals(STATUS_FIELD_ID, dataEvent.fields().get(1).dataSetFieldId());

    // the reader completed startup (a key frame was received along the way)
    PubSubHandle readerHandle =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader").orElseThrow();
    assertEquals(PubSubState.Operational, subscriber.state(readerHandle));
  }

  /**
   * A REQUEST_IF_MISSING reader with wildcard PublisherId and DataSetWriterId filters cannot form a
   * targeted probe: probing is deferred until the first matching data NetworkMessage reveals the
   * identifiers, then probes carry the learned ids.
   */
  @Test
  void wildcardReaderDefersProbingUntilDataRevealsIdentifiers() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var capture = new RawDiscoveryCapture(discoveryPort);

    DataSetReaderConfig reader =
        DataSetReaderConfig.builder("reader")
            .metadataPolicy(MetadataPolicy.REQUEST_IF_MISSING)
            .build(); // wildcard PublisherId and DataSetWriterId, no configured metadata

    PubSubService subscriber =
        PubSubService.create(
            subscriberConfig(dataPort, discoveryPort, reader), PubSubBindings.builder().build());
    services.add(subscriber);

    var metaDataEvents = new LinkedBlockingQueue<MetaDataReceivedEvent>();
    subscriber.addMetaDataListener(metaDataEvents::add);

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // no data has revealed the identifiers yet: well past the maximum randomized first-probe
    // delay (500 ms), no probe may appear on the discovery group
    capture.assertNoProbe(1200, "no identifiers learned yet");

    // a publisher appears; its data NetworkMessages reveal (PublisherId, DataSetWriterId)
    startPublisher(publisherConfig(dataPort, discoveryPort, 7));

    UadpDiscoveryProbe probe = capture.awaitProbe();
    assertEquals(PUBLISHER_ID, probe.targetPublisherId());
    assertEquals(List.of(ushort(7)), probe.dataSetWriterIds());
    assertEquals(UadpDiscoveryProbe.PROBE_TYPE_PUBLISHER_INFORMATION, probe.probeType());
    assertEquals(UadpDiscoveryProbe.INFORMATION_TYPE_DATA_SET_META_DATA, probe.informationType());

    // the learned-id probe completes the loop: the announcement is applied
    MetaDataReceivedEvent metaDataEvent = awaitEvent(metaDataEvents, event -> true);
    assertEquals("ds-e2e", metaDataEvent.dataSetName());
  }

  /**
   * §7.2.4.6.12.2: a Bad-status announcement is a denial that terminates the retry loop. The denial
   * is recorded in the reader's diagnostics and no further probes are emitted.
   */
  @Test
  void denialStopsProbingAndRecordsReaderDiagnostics() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var capture = new RawDiscoveryCapture(discoveryPort);

    // the publisher owns writer id 1; the reader requests metadata for writer id 99
    startPublisher(publisherConfig(dataPort, discoveryPort, 1));

    DataSetReaderConfig reader =
        DataSetReaderConfig.builder("reader")
            .publisherId(PUBLISHER_ID)
            .dataSetWriterId(ushort(99))
            .metadataPolicy(MetadataPolicy.REQUEST_IF_MISSING)
            .build();

    PubSubService subscriber =
        PubSubService.create(
            subscriberConfig(dataPort, discoveryPort, reader), PubSubBindings.builder().build());
    services.add(subscriber);

    var metaDataEvents = new LinkedBlockingQueue<MetaDataReceivedEvent>();
    subscriber.addMetaDataListener(metaDataEvents::add);

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    UadpDiscoveryProbe probe = capture.awaitProbe();
    assertEquals(PUBLISHER_ID, probe.targetPublisherId());
    assertEquals(List.of(ushort(99)), probe.dataSetWriterIds());

    // the Bad_NotFound denial is recorded as a diagnostics error on the reader path
    awaitTrue(
        () -> {
          PubSubDiagnostics.ComponentDiagnostics diagnostics =
              subscriber.diagnostics().component("sub-conn/rgrp/reader").orElse(null);
          return diagnostics != null
              && new StatusCode(StatusCodes.Bad_NotFound).equals(diagnostics.lastError());
        },
        "Bad_NotFound denial in the reader diagnostics");

    // probing stopped: drain anything in flight, then observe silence for more than twice the
    // 500 ms base retry interval
    while (capture.pollProbe() != null) {
      // drain
    }
    capture.assertNoProbe(1300, "after the denial");

    // a denial is not applied metadata: no MetaDataReceivedEvent was delivered
    assertNull(metaDataEvents.poll());
  }
}
