/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.KeyFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * D44/R14 rows beyond {@link ReconfigureIntegrationTest}: NetworkMessage sequence continuity and
 * counter/TimeFirstChange preservation hold across MULTIPLE consecutive path-stable restarts
 * (observed on the raw UDP wire, strictly monotonic across every restart boundary), and a live Milo
 * subscriber accepts post-restart traffic seamlessly — its Part 14 §7.2.3 sequence windows record
 * ZERO stale/invalid drops because the preserved publisher sequence never jumps backward (a restart
 * at 0 would trip the restart-rejection window).
 *
 * <p>Width-change reset and removal+re-add zeroing are covered by {@code
 * ReconfigureIntegrationTest} and are deliberately not duplicated here.
 *
 * <p>Network safety: explicit loopback addresses and bind-probed ephemeral ports everywhere; the
 * discovery port is picked once and reused so the connection shell stays unchanged across
 * reconfigures.
 */
class CounterPreservationReconfigureTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4713));

  private static final UUID FIELD_A_ID = new UUID(0L, 0xD1L);

  private static final UadpWriterGroupSettings GROUP_SETTINGS =
      UadpWriterGroupSettings.builder()
          .networkMessageContentMask(
              UadpNetworkMessageContentMask.of(
                  UadpNetworkMessageContentMask.Field.PublisherId,
                  UadpNetworkMessageContentMask.Field.GroupHeader,
                  UadpNetworkMessageContentMask.Field.WriterGroupId,
                  UadpNetworkMessageContentMask.Field.SequenceNumber,
                  UadpNetworkMessageContentMask.Field.PayloadHeader))
          .build();

  private static final UadpDataSetWriterSettings WRITER_SETTINGS =
      UadpDataSetWriterSettings.builder()
          .dataSetMessageContentMask(
              UadpDataSetMessageContentMask.of(
                  UadpDataSetMessageContentMask.Field.MajorVersion,
                  UadpDataSetMessageContentMask.Field.MinorVersion,
                  UadpDataSetMessageContentMask.Field.SequenceNumber))
          .build();

  private final List<PubSubService> services = new CopyOnWriteArrayList<>();

  @AfterEach
  void tearDown() throws InterruptedException {
    for (PubSubService service : services) {
      try {
        service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        // best effort cleanup; failures are reported by the tests themselves
      }
    }
    services.clear();
  }

  @Test
  void wireSequenceAndCountersSurviveMultiplePathStableRestarts() throws Exception {
    withBindRetry(this::wireSequenceScenario);
  }

  private void wireSequenceScenario() throws Exception {
    int port = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var values = new AtomicReference<>(Map.of("value-a", new DataValue(Variant.ofInt32(1))));

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(port, discoveryPort, Duration.ofMillis(40)),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-a"), mapSource(values))
                    .build()));

    try (DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName("127.0.0.1"))) {
      socket.setSoTimeout(1_000);

      publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      // phase 0: traffic flows, the sequence scan starts, and a TimeFirstChange baseline forms
      Integer previousSequence = observeMonotonicSequence(socket, null, 5);

      awaitTrue(
          "group counted at least five NetworkMessages sent",
          () ->
              publisher
                  .diagnostics()
                  .component("pub-conn/grp-a")
                  .map(d -> d.networkMessagesSent() >= 5)
                  .orElse(false));

      PubSubDiagnostics.ComponentDiagnostics initial =
          publisher.diagnostics().component("pub-conn/grp-a").orElseThrow();
      DateTime timeFirstChange = initial.timeFirstChange(PubSubCounter.NETWORK_MESSAGES_SENT);
      assertNotNull(timeFirstChange);
      long countersFloor = initial.networkMessagesSent();

      // two consecutive path-stable restarts (group-shell publishingInterval changes); after
      // each, the wire sequence continues strictly monotonic across the boundary and the
      // counters plus their TimeFirstChange baseline carry over
      Duration[] intervals = {Duration.ofMillis(70), Duration.ofMillis(55)};
      for (Duration interval : intervals) {
        ReconfigureResult result =
            publisher.reconfigure(
                publisherConfig(port, discoveryPort, interval),
                PubSubService.ReconfigureMode.DISABLE_AFFECTED);
        assertEquals(List.of("pub-conn/grp-a"), result.restartedPaths());

        previousSequence = observeMonotonicSequence(socket, previousSequence, 10);

        PubSubDiagnostics.ComponentDiagnostics after =
            publisher.diagnostics().component("pub-conn/grp-a").orElseThrow();
        assertTrue(
            after.networkMessagesSent() >= countersFloor,
            "counters continue across restart: %d after %d"
                .formatted(after.networkMessagesSent(), countersFloor));
        assertEquals(
            timeFirstChange,
            after.timeFirstChange(PubSubCounter.NETWORK_MESSAGES_SENT),
            "TimeFirstChange survives every path-stable restart");
        countersFloor = after.networkMessagesSent();
      }
    }
  }

  @Test
  void subscriberRecordsNoSequenceDropsAcrossPathStableRestart() throws Exception {
    withBindRetry(this::subscriberNoDropsScenario);
  }

  private void subscriberNoDropsScenario() throws Exception {
    int port = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var values = new AtomicReference<>(Map.of("value-a", new DataValue(Variant.ofInt32(1))));

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(port, discoveryPort, Duration.ofMillis(40)),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-a"), mapSource(values))
                    .build()));

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber =
        track(
            PubSubService.create(
                subscriberConfig(port),
                PubSubBindings.builder()
                    .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader-a"), events::add)
                    .build()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    awaitEvent(events, event -> true);

    String readerPath = "sub-conn/rgrp/reader-a";
    long receivedBefore = readerDiagnostics(subscriber, readerPath).dataSetMessagesReceived();
    assertEquals(0, readerDiagnostics(subscriber, readerPath).staleSequenceMessages());
    assertEquals(0, readerDiagnostics(subscriber, readerPath).invalidSequenceMessages());

    // path-stable restart of the publishing group (D44: DSM and NM sequences preserved)
    ReconfigureResult result =
        publisher.reconfigure(
            publisherConfig(port, discoveryPort, Duration.ofMillis(60)),
            PubSubService.ReconfigureMode.DISABLE_AFFECTED);
    assertEquals(List.of("pub-conn/grp-a"), result.restartedPaths());

    // post-restart traffic is delivered...
    events.clear();
    awaitEvent(events, event -> true);
    awaitEvent(events, event -> true);
    awaitEvent(events, event -> true);

    awaitTrue(
        "the reader keeps counting received DataSetMessages after the restart",
        () -> readerDiagnostics(subscriber, readerPath).dataSetMessagesReceived() > receivedBefore);

    // ... and NOTHING was dropped by the §7.2.3 sequence windows: preservation means the
    // subscriber never saw a backward jump, so no restart-recovery window was ever entered
    assertEquals(0, readerDiagnostics(subscriber, readerPath).staleSequenceMessages());
    assertEquals(0, readerDiagnostics(subscriber, readerPath).invalidSequenceMessages());
  }

  // region fixtures

  /** Publisher config: one UDP connection, one group "grp-a" with "writer-a" on "ds-a". */
  private static PubSubConfig publisherConfig(int port, int discoveryPort, Duration interval) {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds-a")
            .field(
                FieldDefinition.builder("value-a")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(FIELD_A_ID)
                    .build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", discoveryPort))
                .writerGroup(
                    WriterGroupConfig.builder("grp-a")
                        .writerGroupId(ushort(1))
                        .publishingInterval(interval)
                        .messageSettings(GROUP_SETTINGS)
                        .dataSetWriter(
                            DataSetWriterConfig.builder("writer-a")
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(1))
                                .settings(WRITER_SETTINGS)
                                .build())
                        .build())
                .build())
        .build();
  }

  private static PubSubConfig subscriberConfig(int port) throws SocketException {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("ds-a").field("value-a", NodeIds.Int32, FIELD_A_ID).build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("sub-conn")
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(
                    ReaderGroupConfig.builder("rgrp")
                        .dataSetReader(
                            DataSetReaderConfig.builder("reader-a")
                                .publisherId(PUBLISHER_ID)
                                .writerGroupId(ushort(1))
                                .dataSetWriterId(ushort(1))
                                .dataSetMetaData(metaData)
                                .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                .build())
                        .build())
                .build())
        .build();
  }

  // endregion

  // region helpers

  private interface Scenario {
    void run() throws Exception;
  }

  /**
   * Run a scenario, retrying (with entirely fresh ports, picked inside the scenario) when a UDP
   * bind lost the accepted {@code freeUdpPort()} probe-to-bind race. Assertion failures and any
   * other error propagate on first occurrence.
   */
  private void withBindRetry(Scenario scenario) throws Exception {
    for (int attempt = 1; ; attempt++) {
      try {
        scenario.run();
        return;
      } catch (Exception e) {
        if (attempt < 3 && isBindFailure(e)) {
          // release anything the failed attempt started, then retry with fresh ports
          tearDown();
          continue;
        }
        throw e;
      }
    }
  }

  private static boolean isBindFailure(Throwable t) {
    for (Throwable cause = t; cause != null; cause = cause.getCause()) {
      if (cause instanceof BindException) {
        return true;
      }
      if (cause instanceof UaException uaException
          && uaException.getStatusCode().value() == StatusCodes.Bad_CommunicationError
          && String.valueOf(uaException.getMessage()).contains("failed to bind")) {
        return true;
      }
    }
    return false;
  }

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  private static PubSubDiagnostics.ComponentDiagnostics readerDiagnostics(
      PubSubService service, String path) {
    return service.diagnostics().component(path).orElseThrow();
  }

  /**
   * Receive {@code count} NetworkMessages carrying a sequence number, asserting the sequence stays
   * strictly monotonic (starting from {@code previousSequence} when non-null); returns the last
   * observed sequence number.
   */
  private static Integer observeMonotonicSequence(
      DatagramSocket socket, @Nullable Integer previousSequence, int count) throws Exception {

    int observed = 0;
    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (observed < count && System.nanoTime() < deadline) {
      DecodedNetworkMessage message = receiveNetworkMessage(socket);
      if (message == null || message.sequenceNumber() == null) {
        continue;
      }
      int sequence = message.sequenceNumber().intValue();
      if (previousSequence != null) {
        assertTrue(
            sequence > previousSequence,
            "NetworkMessage sequence continues across restarts: %d after %d"
                .formatted(sequence, previousSequence));
      }
      previousSequence = sequence;
      observed++;
    }
    assertEquals(count, observed, "expected %d NetworkMessages on the wire".formatted(count));
    return previousSequence;
  }

  /** Receive and decode one datagram as a NetworkMessage, or {@code null} on receive timeout. */
  private static @Nullable DecodedNetworkMessage receiveNetworkMessage(DatagramSocket socket)
      throws Exception {

    byte[] buffer = new byte[65535];
    var packet = new DatagramPacket(buffer, buffer.length);
    try {
      socket.receive(packet);
    } catch (SocketTimeoutException e) {
      return null;
    }

    ByteBuf data = Unpooled.wrappedBuffer(packet.getData(), 0, packet.getLength());
    try {
      return new UadpMessageMapping().decode(DecodeContext.of(new DefaultEncodingContext()), data);
    } finally {
      data.release();
    }
  }

  /**
   * Pick a currently free UDP port by binding and closing an ephemeral socket. The small race
   * between closing and re-binding is accepted.
   */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  /** A source that reads the current values of an AtomicReference-backed map by field key. */
  private static PublishedDataSetSource mapSource(AtomicReference<Map<String, DataValue>> values) {
    return context -> {
      DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
      Map<String, DataValue> currentValues = values.get();
      for (FieldDefinition field : context.fields()) {
        String key =
            field.getSource() instanceof KeyFieldAddress keyAddress
                ? keyAddress.key()
                : field.getName();
        DataValue value = currentValues.get(key);
        if (value != null) {
          builder.field(field.getName(), value);
        }
      }
      return builder.build();
    };
  }

  /** Wait for an event matching {@code predicate}, discarding non-matching events. */
  private static DataSetReceivedEvent awaitEvent(
      BlockingQueue<DataSetReceivedEvent> events, Predicate<DataSetReceivedEvent> predicate)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (true) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        fail("timed out waiting for a matching DataSetReceivedEvent");
      }
      DataSetReceivedEvent event =
          events.poll(Math.min(remaining, 100_000_000L), TimeUnit.NANOSECONDS);
      if (event != null && predicate.test(event)) {
        return event;
      }
    }
  }

  /** Wait for {@code condition} to become true, polling every 10 ms. */
  private static void awaitTrue(String description, BooleanSupplier condition)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      if (condition.getAsBoolean()) {
        return;
      }
      Thread.sleep(10);
    }
    fail("timed out waiting for: " + description);
  }

  // endregion
}
