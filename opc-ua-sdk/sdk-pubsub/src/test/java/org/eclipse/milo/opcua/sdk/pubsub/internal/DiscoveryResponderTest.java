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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpDecodedMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpDiscoveryProbe;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMetaDataAnnouncement;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the publisher-side UADP discovery responder (OPC UA Part 14 §7.2.4.6) against real
 * loopback sockets: hand-encoded DataSetMetaData probes are injected with raw UDP sends and the
 * responder's announcements are captured on both the discovery multicast group and the connection's
 * data address.
 *
 * <p>Network safety: a unique organization-local multicast group (239.255.72.1) on a unique
 * non-4840 port per test, joined on the loopback interface only; the data address is unicast
 * 127.0.0.1 with an ephemeral port. The connection's discoveryAddress is always configured
 * explicitly so the engine never falls back to the 224.0.2.14:4840 default.
 */
class DiscoveryResponderTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(15);

  private static final String DISCOVERY_GROUP = "239.255.72.1";
  private static final String LOOPBACK = "127.0.0.1";

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(21201));

  private static final UUID TEMPERATURE_FIELD_ID = new UUID(0L, 0x721L);
  private static final UUID STATUS_FIELD_ID = new UUID(0L, 0x722L);
  private static final UUID COUNTER_FIELD_ID = new UUID(0L, 0x723L);

  private static final QualifiedName MILO_SOURCE_KEY = new QualifiedName(0, "MiloSourceKey");

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

  // region raw socket harness

  private static NetworkInterface loopbackInterface() throws IOException {
    NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByName(LOOPBACK));
    assertNotNull(ni, "no loopback network interface for " + LOOPBACK);
    return ni;
  }

  /**
   * A raw discovery socket: joins the discovery group on the loopback interface (alongside the
   * engine's own discovery socket, courtesy of SO_REUSEADDR) to capture announcements, and sends
   * probe datagrams to the group with loopback multicast egress.
   */
  private final class RawDiscoverySocket implements AutoCloseable {

    final DatagramChannel channel;
    final InetSocketAddress groupAddress;

    RawDiscoverySocket(int port) throws IOException {
      NetworkInterface ni = loopbackInterface();
      InetAddress group = InetAddress.getByName(DISCOVERY_GROUP);

      channel = DatagramChannel.open(StandardProtocolFamily.INET);
      channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
      channel.bind(new InetSocketAddress(port));
      channel.join(group, ni);
      channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
      channel.configureBlocking(false);

      groupAddress = new InetSocketAddress(group, port);
      sockets.add(this);
    }

    void send(byte[] datagram) throws IOException {
      channel.send(ByteBuffer.wrap(datagram), groupAddress);
    }

    byte @Nullable [] poll() throws IOException {
      ByteBuffer buffer = ByteBuffer.allocate(65535);
      if (channel.receive(buffer) == null) {
        return null;
      }
      buffer.flip();
      byte[] bytes = new byte[buffer.remaining()];
      buffer.get(bytes);
      return bytes;
    }

    @Override
    public void close() throws IOException {
      channel.close();
    }
  }

  /** A raw capture socket bound to the connection's unicast data address. */
  private final class RawDataSocket implements AutoCloseable {

    final DatagramChannel channel;

    RawDataSocket(int port) throws IOException {
      channel = DatagramChannel.open(StandardProtocolFamily.INET);
      channel.bind(new InetSocketAddress(LOOPBACK, port));
      channel.configureBlocking(false);
      sockets.add(this);
    }

    byte @Nullable [] poll() throws IOException {
      ByteBuffer buffer = ByteBuffer.allocate(65535);
      if (channel.receive(buffer) == null) {
        return null;
      }
      buffer.flip();
      byte[] bytes = new byte[buffer.remaining()];
      buffer.get(bytes);
      return bytes;
    }

    @Override
    public void close() throws IOException {
      channel.close();
    }
  }

  private interface DatagramPoller {
    byte @Nullable [] poll() throws IOException;
  }

  private static UadpDecodedMessage decode(byte[] datagram) {
    ByteBuf buffer = Unpooled.wrappedBuffer(datagram);
    try {
      return new UadpMessageMapping()
          .decodeMessage(DecodeContext.of(new DefaultEncodingContext()), buffer);
    } finally {
      buffer.release();
    }
  }

  /** Wait for an announcement matching {@code predicate}, discarding everything else. */
  private static UadpMetaDataAnnouncement awaitAnnouncement(
      DatagramPoller poller, Predicate<UadpMetaDataAnnouncement> predicate)
      throws IOException, InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      byte[] datagram = poller.poll();
      if (datagram == null) {
        Thread.sleep(10);
        continue;
      }
      if (decode(datagram) instanceof UadpMetaDataAnnouncement announcement
          && predicate.test(announcement)) {
        return announcement;
      }
    }
    return fail("timed out waiting for a matching DataSetMetaData announcement");
  }

  /** Assert that no announcement at all is observed for {@code millis}. */
  private static void assertNoAnnouncement(DatagramPoller poller, long millis, String description)
      throws IOException, InterruptedException {

    long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(millis);
    while (System.nanoTime() < deadline) {
      byte[] datagram = poller.poll();
      if (datagram != null && decode(datagram) instanceof UadpMetaDataAnnouncement announcement) {
        fail("unexpected announcement (" + description + "): " + announcement);
      }
      Thread.sleep(10);
    }
  }

  private static byte[] encodeProbe(PublisherId targetPublisherId, List<UShort> dataSetWriterIds)
      throws UaException {

    EncodedNetworkMessage encoded =
        new UadpMessageMapping()
            .encodeProbe(
                new DefaultEncodingContext(),
                UadpDiscoveryProbe.of(targetPublisherId, dataSetWriterIds));

    ByteBuf data = encoded.data();
    try {
      byte[] bytes = new byte[data.readableBytes()];
      data.readBytes(bytes);
      return bytes;
    } finally {
      data.release();
    }
  }

  // endregion

  // region fixtures

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  private static PublishedDataSetConfig dataSetResp(int minorVersion, String firstFieldName) {
    return PublishedDataSetConfig.builder("ds-resp")
        .field(
            FieldDefinition.builder(firstFieldName)
                .dataType(NodeIds.Double)
                .dataSetFieldId(TEMPERATURE_FIELD_ID)
                .build())
        .field(
            FieldDefinition.builder("status")
                .dataType(NodeIds.String)
                .dataSetFieldId(STATUS_FIELD_ID)
                .build())
        .configurationVersion(uint(3), uint(minorVersion))
        .build();
  }

  private static PublishedDataSetConfig dataSetTwo() {
    // the dataSetFieldId must be pinned: a random (default) id would make every rebuilt config
    // a genuine metadata change
    return PublishedDataSetConfig.builder("ds-two")
        .field(
            FieldDefinition.builder("counter")
                .dataType(NodeIds.Int32)
                .dataSetFieldId(COUNTER_FIELD_ID)
                .build())
        .configurationVersion(uint(2), uint(4))
        .build();
  }

  /** Publisher config: writers 1 (ds-resp) and 2 (ds-two) on one group, explicit discovery. */
  private static PubSubConfig publisherConfig(
      int dataPort, int discoveryPort, PublishedDataSetConfig dataSetA) {

    WriterGroupConfig group =
        WriterGroupConfig.builder("grp")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(100))
            .dataSetWriter(
                DataSetWriterConfig.builder("writer-a")
                    .dataSet(dataSetA.ref())
                    .dataSetWriterId(ushort(1))
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("writer-b")
                    .dataSet(new PublishedDataSetRef("ds-two"))
                    .dataSetWriterId(ushort(2))
                    .build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSetA)
        .publishedDataSet(dataSetTwo())
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast(LOOPBACK, dataPort))
                .discoveryAddress(
                    UdpDatagramAddress.multicast(DISCOVERY_GROUP, discoveryPort)
                        .networkInterface(LOOPBACK))
                .writerGroup(group)
                .build())
        .build();
  }

  /** A source that supplies a placeholder value for every field of its dataset. */
  private static DataSetSnapshot anySnapshot(PublishedDataSetReadContext context) {
    DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
    for (FieldDefinition field : context.fields()) {
      builder.field(field.getName(), new DataValue(Variant.ofInt32(0)));
    }
    return builder.build();
  }

  private PubSubService startPublisher(PubSubConfig config) throws Exception {
    PubSubService publisher =
        PubSubService.create(
            config,
            PubSubBindings.builder()
                .source(new PublishedDataSetRef("ds-resp"), DiscoveryResponderTest::anySnapshot)
                .source(new PublishedDataSetRef("ds-two"), DiscoveryResponderTest::anySnapshot)
                .build());
    services.add(publisher);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return publisher;
  }

  private static void assertNoMiloSourceKey(DataSetMetaDataType metaData) {
    FieldMetaData[] fields = metaData.getFields();
    assertNotNull(fields);
    for (FieldMetaData field : fields) {
      KeyValuePair[] properties = field.getProperties();
      if (properties != null) {
        for (KeyValuePair property : properties) {
          assertNotEquals(
              MILO_SOURCE_KEY,
              property.getKey(),
              "MiloSourceKey must be stripped from announced metadata");
        }
      }
    }
  }

  // endregion

  @Test
  void metaDataProbeIsAnsweredOnBothDiscoveryAndDataAddress() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var dataCapture = new RawDataSocket(dataPort);
    var discovery = new RawDiscoverySocket(discoveryPort);

    startPublisher(publisherConfig(dataPort, discoveryPort, dataSetResp(9, "temperature")));

    discovery.send(encodeProbe(PUBLISHER_ID, List.of(ushort(1))));

    UadpMetaDataAnnouncement announcement =
        awaitAnnouncement(discovery::poll, a -> ushort(1).equals(a.dataSetWriterId()));

    // header and Table 170 body
    assertEquals(PUBLISHER_ID, announcement.publisherId());
    assertEquals(ushort(0), announcement.sequenceNumber());
    assertEquals(ushort(1), announcement.dataSetWriterId());
    assertEquals(StatusCode.GOOD, announcement.statusCode());

    // metadata content derives from the published dataset, MiloSourceKey stripped
    DataSetMetaDataType metaData = announcement.metaData();
    assertEquals("ds-resp", metaData.getName());
    assertEquals(uint(3), metaData.getConfigurationVersion().getMajorVersion());
    assertEquals(uint(9), metaData.getConfigurationVersion().getMinorVersion());

    FieldMetaData[] fields = metaData.getFields();
    assertNotNull(fields);
    assertEquals(2, fields.length);
    assertEquals("temperature", fields[0].getName());
    assertEquals(TEMPERATURE_FIELD_ID, fields[0].getDataSetFieldId());
    assertEquals("status", fields[1].getName());
    assertEquals(STATUS_FIELD_ID, fields[1].getDataSetFieldId());
    assertNoMiloSourceKey(metaData);

    // the same announcement (same bytes, same sequence number) also arrives on the data address
    UadpMetaDataAnnouncement onDataAddress =
        awaitAnnouncement(dataCapture::poll, a -> ushort(1).equals(a.dataSetWriterId()));
    assertEquals(announcement, onDataAddress);
  }

  @Test
  void probeForMultipleWritersYieldsOneAnnouncementPerWriterId() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var discovery = new RawDiscoverySocket(discoveryPort);

    startPublisher(publisherConfig(dataPort, discoveryPort, dataSetResp(9, "temperature")));

    // Table 180: one announcement NetworkMessage per requested DataSetWriterId
    discovery.send(encodeProbe(PUBLISHER_ID, List.of(ushort(1), ushort(2))));

    var announcements = new ArrayList<UadpMetaDataAnnouncement>();
    announcements.add(awaitAnnouncement(discovery::poll, a -> true));
    announcements.add(awaitAnnouncement(discovery::poll, a -> true));

    Set<UShort> writerIds = new HashSet<>();
    Set<UShort> sequenceNumbers = new HashSet<>();
    for (UadpMetaDataAnnouncement announcement : announcements) {
      assertEquals(StatusCode.GOOD, announcement.statusCode());
      writerIds.add(announcement.dataSetWriterId());
      sequenceNumbers.add(announcement.sequenceNumber());
    }

    assertEquals(Set.of(ushort(1), ushort(2)), writerIds);
    // the per-PublisherId sequence number increments by one per announcement
    assertEquals(Set.of(ushort(0), ushort(1)), sequenceNumbers);

    UadpMetaDataAnnouncement forWriterTwo =
        announcements.stream()
            .filter(a -> ushort(2).equals(a.dataSetWriterId()))
            .findFirst()
            .orElseThrow();
    assertEquals("ds-two", forWriterTwo.metaData().getName());
  }

  @Test
  void unknownWriterIdIsDeniedWithBadNotFoundAndEmptyMetaData() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var discovery = new RawDiscoverySocket(discoveryPort);

    startPublisher(publisherConfig(dataPort, discoveryPort, dataSetResp(9, "temperature")));

    discovery.send(encodeProbe(PUBLISHER_ID, List.of(ushort(42))));

    UadpMetaDataAnnouncement denial =
        awaitAnnouncement(discovery::poll, a -> ushort(42).equals(a.dataSetWriterId()));

    assertEquals(new StatusCode(StatusCodes.Bad_NotFound), denial.statusCode());
    assertEquals(PUBLISHER_ID, denial.publisherId());

    // the metadata structure is on the wire (Table 170 has no slot for omitting it) but empty
    DataSetMetaDataType metaData = denial.metaData();
    assertTrue(metaData.getName() == null || metaData.getName().isEmpty());
    assertTrue(metaData.getFields() == null || metaData.getFields().length == 0);
    assertEquals(uint(0), metaData.getConfigurationVersion().getMajorVersion());
    assertEquals(uint(0), metaData.getConfigurationVersion().getMinorVersion());
  }

  @Test
  void probeTargetingForeignPublisherIdIsIgnored() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var discovery = new RawDiscoverySocket(discoveryPort);

    startPublisher(publisherConfig(dataPort, discoveryPort, dataSetResp(9, "temperature")));

    // the PublisherId in a probe identifies the probed Publisher; a foreign id is not ours
    discovery.send(encodeProbe(PublisherId.uint16(ushort(60000)), List.of(ushort(1))));

    assertNoAnnouncement(discovery::poll, 800, "probe targeted a foreign PublisherId");
  }

  @Test
  void emptyDataSetWriterIdListIsNoOp() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var discovery = new RawDiscoverySocket(discoveryPort);

    startPublisher(publisherConfig(dataPort, discoveryPort, dataSetResp(9, "temperature")));

    discovery.send(encodeProbe(PUBLISHER_ID, List.of()));

    assertNoAnnouncement(discovery::poll, 800, "probe with an empty DataSetWriterIds list");
  }

  /**
   * §7.2.4.6.12.2: repeat answers for the same (probe type, DataSetWriterId) are suppressed for at
   * least 500 ms; after the window a new probe is answered again.
   */
  @Test
  void repeatProbeWithinSuppressionWindowIsUnanswered() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var discovery = new RawDiscoverySocket(discoveryPort);

    startPublisher(publisherConfig(dataPort, discoveryPort, dataSetResp(9, "temperature")));

    byte[] probe = encodeProbe(PUBLISHER_ID, List.of(ushort(1)));

    discovery.send(probe);
    UadpMetaDataAnnouncement first =
        awaitAnnouncement(discovery::poll, a -> ushort(1).equals(a.dataSetWriterId()));
    assertEquals(ushort(0), first.sequenceNumber());
    long answeredAtNanos = System.nanoTime();

    // an identical probe inside the suppression window goes unanswered
    discovery.send(probe);
    assertNoAnnouncement(discovery::poll, 250, "repeat probe inside the suppression window");

    // wait until the window has certainly elapsed (it opened at or before the first answer)
    long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - answeredAtNanos);
    Thread.sleep(Math.max(0, 800 - elapsedMillis));

    discovery.send(probe);
    UadpMetaDataAnnouncement second =
        awaitAnnouncement(discovery::poll, a -> ushort(1).equals(a.dataSetWriterId()));

    // the suppressed probe consumed no sequence number
    assertEquals(ushort(1), second.sequenceNumber());
  }

  /**
   * §7.2.4.6.4: when a reconfiguration changes the metadata of a live writer's dataset, the changed
   * metadata is announced exactly once, unsolicited; unchanged writers are not announced.
   */
  @Test
  void reconfigureWithChangedDataSetAnnouncesChangedWriterOnce() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var discovery = new RawDiscoverySocket(discoveryPort);

    PubSubService publisher =
        startPublisher(publisherConfig(dataPort, discoveryPort, dataSetResp(9, "temperature")));

    // startup itself announces nothing; reconfigure ds-resp with a bumped minor version and a
    // renamed field while ds-two stays identical
    publisher.reconfigure(
        publisherConfig(dataPort, discoveryPort, dataSetResp(10, "temperature2")),
        PubSubService.ReconfigureMode.DISABLE_AFFECTED);

    UadpMetaDataAnnouncement announcement = awaitAnnouncement(discovery::poll, a -> true);

    assertEquals(ushort(1), announcement.dataSetWriterId());
    assertEquals(StatusCode.GOOD, announcement.statusCode());
    assertEquals(ushort(0), announcement.sequenceNumber());

    DataSetMetaDataType metaData = announcement.metaData();
    assertEquals("ds-resp", metaData.getName());
    assertEquals(uint(3), metaData.getConfigurationVersion().getMajorVersion());
    assertEquals(uint(10), metaData.getConfigurationVersion().getMinorVersion());
    assertNotNull(metaData.getFields());
    assertEquals("temperature2", metaData.getFields()[0].getName());

    // exactly one announcement: nothing for the unchanged writer, no repeat for the changed one
    assertNoAnnouncement(discovery::poll, 800, "after the one unsolicited change announcement");
  }

  /**
   * §7.2.4.6.4 across a connection restart: under STOP_AND_RESTART a dataset change restarts the
   * whole containing connection, replacing its DiscoveryRuntime. The changed metadata must still be
   * announced once — diffed against the predecessor runtime's baseline, not the rebuilt runtime's
   * channel-open baseline, which already derives from the new config.
   */
  @Test
  void connectionRestartingReconfigureWithChangedDataSetStillAnnounces() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var discovery = new RawDiscoverySocket(discoveryPort);

    PubSubService publisher =
        startPublisher(publisherConfig(dataPort, discoveryPort, dataSetResp(9, "temperature")));

    // the dataset change induces a writer change, which STOP_AND_RESTART escalates to a restart
    // of the whole connection
    publisher.reconfigure(
        publisherConfig(dataPort, discoveryPort, dataSetResp(10, "temperature2")),
        PubSubService.ReconfigureMode.STOP_AND_RESTART);

    UadpMetaDataAnnouncement announcement = awaitAnnouncement(discovery::poll, a -> true);

    assertEquals(ushort(1), announcement.dataSetWriterId());
    assertEquals(StatusCode.GOOD, announcement.statusCode());
    // the per-PublisherId sequence counter lives on the service and survives the restart
    assertEquals(ushort(0), announcement.sequenceNumber());

    DataSetMetaDataType metaData = announcement.metaData();
    assertEquals("ds-resp", metaData.getName());
    assertEquals(uint(3), metaData.getConfigurationVersion().getMajorVersion());
    assertEquals(uint(10), metaData.getConfigurationVersion().getMinorVersion());
    assertNotNull(metaData.getFields());
    assertEquals("temperature2", metaData.getFields()[0].getName());

    // exactly one announcement: nothing for the unchanged writer ds-two
    assertNoAnnouncement(discovery::poll, 800, "after the one unsolicited change announcement");
  }
}
