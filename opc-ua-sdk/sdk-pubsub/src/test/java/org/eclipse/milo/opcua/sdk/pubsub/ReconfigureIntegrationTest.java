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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.KeyFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
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
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.NodeIds;
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
 * Integration tests for {@link PubSubService#reconfigure} against a running loopback UDP
 * publisher/subscriber pair: reconfiguration by replacement, with traffic observed before and
 * after.
 *
 * <p>Network safety: every UDP connection pins an explicit loopback {@code discoveryAddress}, so
 * the engine's discovery channels never bind the well-known port 4840 or join the default {@code
 * 224.0.2.14} multicast group, and unsolicited reconfigure announcements never leave loopback. The
 * publisher's discovery port is picked once per test and reused for the reconfigured config: the
 * discovery address is part of the connection shell, and changing it would turn a group-level
 * reconfigure into a connection restart.
 */
class ReconfigureIntegrationTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4712));

  private static final UUID FIELD_A_ID = new UUID(0L, 0xC1L);
  private static final UUID FIELD_B_ID = new UUID(0L, 0xC2L);
  private static final UUID FIELD_COUNTER_ID = new UUID(0L, 0xC3L);
  private static final UUID FIELD_CONSTANT_ID = new UUID(0L, 0xC4L);

  private static final DataSetReaderRef READER_A_REF =
      new DataSetReaderRef("sub-conn", "rgrp", "reader-a");
  private static final DataSetReaderRef READER_B_REF =
      new DataSetReaderRef("sub-conn", "rgrp", "reader-b");

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
                  UadpDataSetMessageContentMask.Field.Timestamp,
                  UadpDataSetMessageContentMask.Field.Status,
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
  void disableAffectedRestartsChangedGroupAndAddsNewWriter() throws Exception {
    int port = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var valuesA = new AtomicReference<>(Map.of("value-a", new DataValue(Variant.ofInt32(1))));

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(port, discoveryPort, Duration.ofMillis(80), false),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-a"), mapSource(valuesA))
                    .build()));

    var eventsA = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var eventsB = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubService subscriber =
        track(
            PubSubService.create(
                subscriberConfig(port),
                PubSubBindings.builder()
                    .listener(READER_A_REF, eventsA::add)
                    .listener(READER_B_REF, eventsB::add)
                    .build()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    awaitEvent(eventsA, event -> true);

    PubSubHandle groupABefore =
        publisher.components().writerGroup("pub-conn", "grp-a").orElseThrow();

    // change grp-a's publishingInterval and add ds-b plus a writer for it to grp-b
    ReconfigureResult result =
        publisher.reconfigure(
            publisherConfig(port, discoveryPort, Duration.ofMillis(40), true),
            PubSubService.ReconfigureMode.DISABLE_AFFECTED);

    // the changed group is restarted; the added dataset and writer are reported as added
    assertEquals(List.of("pub-conn/grp-a"), result.restartedPaths());
    assertTrue(result.removedPaths().isEmpty(), "removedPaths: " + result.removedPaths());
    assertEquals(2, result.addedPaths().size(), "addedPaths: " + result.addedPaths());
    assertTrue(result.addedPaths().contains("ds-b"));
    assertTrue(result.addedPaths().contains("pub-conn/grp-b/writer-b"));

    // the restarted group got a new handle; the old one is invalidated
    assertThrows(IllegalArgumentException.class, () -> publisher.state(groupABefore));
    PubSubHandle groupAAfter =
        publisher.components().writerGroup("pub-conn", "grp-a").orElseThrow();
    assertNotSame(groupABefore, groupAAfter);

    // traffic for the unchanged writer continues after the reconfigure
    eventsA.clear();
    awaitEvent(eventsA, event -> true);

    // the new writer's events arrive once its source is bound
    var valuesB = new AtomicReference<>(Map.of("value-b", new DataValue(Variant.ofInt32(7))));
    publisher.bindSource(new PublishedDataSetRef("ds-b"), mapSource(valuesB));

    DataSetReceivedEvent eventB =
        awaitEvent(
            eventsB,
            event ->
                Integer.valueOf(7).equals(event.fieldsByName().get("value-b").value().value()));
    assertEquals(ushort(2), eventB.dataSetWriterId());
    assertEquals("ds-b", eventB.dataSetName());
    assertEquals(FIELD_B_ID, eventB.fields().get(0).dataSetFieldId());
  }

  @Test
  void stopAndRestartRestartsTheWholeConnection() throws Exception {
    int port = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var valuesA = new AtomicReference<>(Map.of("value-a", new DataValue(Variant.ofInt32(3))));

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(port, discoveryPort, Duration.ofMillis(60), false),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-a"), mapSource(valuesA))
                    .build()));

    var eventsA = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubService subscriber =
        track(
            PubSubService.create(
                subscriberConfig(port),
                PubSubBindings.builder().listener(READER_A_REF, eventsA::add).build()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    awaitEvent(eventsA, event -> true);

    // a group-level change restarts the whole containing connection under STOP_AND_RESTART
    ReconfigureResult result =
        publisher.reconfigure(
            publisherConfig(port, discoveryPort, Duration.ofMillis(90), false),
            PubSubService.ReconfigureMode.STOP_AND_RESTART);

    assertEquals(List.of("pub-conn"), result.restartedPaths());
    assertTrue(result.addedPaths().isEmpty(), "addedPaths: " + result.addedPaths());
    assertTrue(result.removedPaths().isEmpty(), "removedPaths: " + result.removedPaths());

    // traffic resumes after the connection restart
    eventsA.clear();
    awaitEvent(eventsA, event -> true);
  }

  /**
   * Delta-frame baseline reset and sequence-number preservation across a writer-affecting
   * reconfigure: the writer runtime is recreated, its DataSetMessage sequence number CONTINUES
   * where the replaced runtime left off (path-stable restarts preserve sequence numbers), the first
   * post-restart message is a key frame, and delta emission resumes correctly against the fresh
   * baseline under the new cadence. Observed on the raw UDP wire with a plain DatagramSocket.
   */
  @Test
  void writerAffectingReconfigureResetsDeltaBaseline() throws Exception {
    int port = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var counter = new AtomicInteger(0);
    PublishedDataSetSource countingSource =
        context ->
            DataSetSnapshot.builder(context)
                .field("counter", new DataValue(Variant.ofInt32(counter.incrementAndGet())))
                .field("constant", new DataValue(Variant.ofInt32(100)))
                .build();

    PubSubService publisher =
        track(
            PubSubService.create(
                deltaPublisherConfig(port, discoveryPort, 3),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-delta"), countingSource)
                    .build()));

    try (DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName("127.0.0.1"))) {
      socket.setSoTimeout(1_000);

      publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      // before reconfigure: the keyFrameCount = 3 cadence is live — key frames carry both fields,
      // deltas
      // carry only the changing field with its explicit index
      boolean sawKeyFrame = false;
      boolean sawDeltaFrame = false;
      long deadline = System.nanoTime() + TIMEOUT.toNanos();
      while ((!sawKeyFrame || !sawDeltaFrame) && System.nanoTime() < deadline) {
        DecodedDataSetMessage message = receiveDataSetMessage(socket);
        if (message == null) {
          continue;
        }
        if (message.kind() == DataSetMessageKind.KEY_FRAME) {
          assertEquals(2, message.fields().size());
          sawKeyFrame = true;
        } else if (message.kind() == DataSetMessageKind.DELTA_FRAME) {
          assertEquals(1, message.fields().size());
          assertEquals(0, message.fields().get(0).index());
          sawDeltaFrame = true;
        }
      }
      assertTrue(sawKeyFrame, "no key frame observed before the reconfigure");
      assertTrue(sawDeltaFrame, "no delta frame observed before the reconfigure");

      // after reconfigure: a writer-level change (keyFrameCount 3 -> 4) recreates the writer
      // runtime;
      // the path-stable restart PRESERVES the DataSetMessage sequence number, so the stream
      // continues seamlessly and the restart is visible only through the cadence change
      publisher.reconfigure(
          deltaPublisherConfig(port, discoveryPort, 4),
          PubSubService.ReconfigureMode.DISABLE_AFFECTED);

      // scan for the restart marker: a key frame followed by three delta frames (impossible
      // under the old keyFrameCount = 3 cadence, which allows at most two deltas between key
      // frames); sequence numbers must stay monotonic and never reset to 0 across the restart
      DecodedDataSetMessage restartKeyFrame = null;
      DecodedDataSetMessage lastKeyFrame = null;
      Integer previousSequence = null;
      int deltasSinceKeyFrame = -1; // -1 = no key frame observed yet in this scan
      deadline = System.nanoTime() + TIMEOUT.toNanos();
      while (restartKeyFrame == null && System.nanoTime() < deadline) {
        DecodedDataSetMessage message = receiveDataSetMessage(socket);
        if (message == null || message.sequenceNumber() == null) {
          continue;
        }

        int sequence = message.sequenceNumber().intValue();
        if (previousSequence != null) {
          assertTrue(
              sequence > previousSequence,
              "sequence continues across the restart: %d after %d"
                  .formatted(sequence, previousSequence));
        }
        previousSequence = sequence;

        if (message.kind() == DataSetMessageKind.KEY_FRAME) {
          assertEquals(2, message.fields().size());
          lastKeyFrame = message;
          deltasSinceKeyFrame = 0;
        } else if (message.kind() == DataSetMessageKind.DELTA_FRAME) {
          assertEquals(1, message.fields().size());
          assertEquals(0, message.fields().get(0).index());
          if (deltasSinceKeyFrame >= 0 && ++deltasSinceKeyFrame == 3) {
            restartKeyFrame = lastKeyFrame;
          }
        }
      }
      assertNotNull(restartKeyFrame, "no post-restart cadence (key frame + 3 deltas) observed");

      // the run of three deltas proves the new keyFrameCount = 4 cadence took effect, its
      // opening key frame proves the delta baseline did not survive the restart, and the
      // monotonic sequence scan above proves the counter did — a restart at 0 would have
      // appeared as a backward jump
      assertTrue(restartKeyFrame.sequenceNumber().intValue() > 0);
    }
  }

  /**
   * Sequence and counter preservation for a path-stable group restart: a group-shell change
   * restarts the group; its diagnostic counters, TimeFirstChange, the writer's DataSetMessage
   * sequence number, and the group's NetworkMessage sequence number all continue where the replaced
   * runtime left off (removal still zeroes: see {@link
   * #removalAndReAddZeroesCountersAndRestartsSequenceAtZero}).
   */
  @Test
  void pathStableGroupRestartPreservesCountersAndSequenceNumbers() throws Exception {
    int port = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var valuesA = new AtomicReference<>(Map.of("value-a", new DataValue(Variant.ofInt32(1))));

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(port, discoveryPort, Duration.ofMillis(40), false),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-a"), mapSource(valuesA))
                    .build()));

    try (DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName("127.0.0.1"))) {
      socket.setSoTimeout(1_000);

      publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      PubSubHandle writerBefore =
          publisher.components().dataSetWriter("pub-conn", "grp-a", "writer-a").orElseThrow();
      awaitTrue(
          "writer published at least five DataSetMessages",
          () -> publisher.nextDataSetMessageSequenceNumber(writerBefore).intValue() >= 5);
      // the sequence number advances at draft creation but networkMessagesSent ticks at channel
      // hand-off later in the same cycle, so the counter can trail the sequence by one — await it
      // separately instead of assuming the two advance together
      awaitTrue(
          "group counted at least five NetworkMessages sent",
          () ->
              publisher
                  .diagnostics()
                  .component("pub-conn/grp-a")
                  .map(diagnostics -> diagnostics.networkMessagesSent() >= 5)
                  .orElse(false));

      PubSubDiagnostics.ComponentDiagnostics before =
          publisher.diagnostics().component("pub-conn/grp-a").orElseThrow();
      assertTrue(before.networkMessagesSent() >= 5);
      DateTime timeFirstChangeBefore = before.timeFirstChange(PubSubCounter.NETWORK_MESSAGES_SENT);
      assertNotNull(timeFirstChangeBefore);
      long sequenceBefore = publisher.nextDataSetMessageSequenceNumber(writerBefore).longValue();

      // a group-shell change (publishingInterval) restarts grp-a path-stably
      ReconfigureResult result =
          publisher.reconfigure(
              publisherConfig(port, discoveryPort, Duration.ofMillis(80), false),
              PubSubService.ReconfigureMode.DISABLE_AFFECTED);

      assertEquals(List.of("pub-conn/grp-a"), result.restartedPaths());
      assertEquals(1, result.changes().size());
      ReconfigureResult.Change change = result.changes().get(0);
      assertEquals(ReconfigureResult.Kind.CHANGED, change.kind());
      assertEquals(ReconfigureResult.Scope.WRITER_GROUP, change.scope());
      assertEquals("pub-conn", change.connectionName());
      assertEquals("grp-a", change.groupName());

      // counters and TimeFirstChange survive the restart
      PubSubDiagnostics.ComponentDiagnostics after =
          publisher.diagnostics().component("pub-conn/grp-a").orElseThrow();
      assertTrue(after.networkMessagesSent() >= before.networkMessagesSent());
      assertEquals(
          timeFirstChangeBefore, after.timeFirstChange(PubSubCounter.NETWORK_MESSAGES_SENT));

      // the replacement writer (fresh handle: the restart invalidated the old one) continues
      // the DataSetMessage sequence where its predecessor left off
      assertThrows(
          IllegalArgumentException.class,
          () -> publisher.nextDataSetMessageSequenceNumber(writerBefore));
      PubSubHandle writerAfter =
          publisher.components().dataSetWriter("pub-conn", "grp-a", "writer-a").orElseThrow();
      assertTrue(
          publisher.nextDataSetMessageSequenceNumber(writerAfter).longValue() >= sequenceBefore);

      // and the NetworkMessage sequence continues on the wire: frames observed across the
      // restart boundary keep strictly increasing (a reset would jump backward to 0)
      Integer previousSequence = null;
      int observed = 0;
      long deadline = System.nanoTime() + TIMEOUT.toNanos();
      while (observed < 20 && System.nanoTime() < deadline) {
        DecodedNetworkMessage message = receiveNetworkMessage(socket);
        if (message == null || message.sequenceNumber() == null) {
          continue;
        }
        int sequence = message.sequenceNumber().intValue();
        if (previousSequence != null) {
          assertTrue(
              sequence > previousSequence,
              "NetworkMessage sequence continues across the restart: %d after %d"
                  .formatted(sequence, previousSequence));
        }
        previousSequence = sequence;
        observed++;
      }
      assertEquals(20, observed, "expected 20 NetworkMessages across the restart boundary");
    }
  }

  /** Removal plus re-addition (across two reconfigures) zeroes counters and sequence numbers. */
  @Test
  void removalAndReAddZeroesCountersAndRestartsSequenceAtZero() throws Exception {
    int port = freeUdpPort();
    int discoveryPort = freeUdpPort();

    var valuesA = new AtomicReference<>(Map.of("value-a", new DataValue(Variant.ofInt32(1))));

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(port, discoveryPort, Duration.ofMillis(40), false),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-a"), mapSource(valuesA))
                    .build()));

    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    PubSubHandle writerBefore =
        publisher.components().dataSetWriter("pub-conn", "grp-a", "writer-a").orElseThrow();
    awaitTrue(
        "writer published at least five DataSetMessages",
        () -> publisher.nextDataSetMessageSequenceNumber(writerBefore).intValue() >= 5);

    // remove grp-a entirely: its diagnostics entry disappears with it
    ReconfigureResult removal =
        publisher.reconfigure(
            publisherConfigWithoutGroupA(port, discoveryPort),
            PubSubService.ReconfigureMode.DISABLE_AFFECTED);
    assertTrue(removal.removedPaths().contains("pub-conn/grp-a"));
    assertTrue(publisher.diagnostics().component("pub-conn/grp-a").isEmpty());

    // re-add it with a huge publishing interval so at most the immediate first cycle runs
    // before sampling: everything starts from zero (no preservation across removal + re-add)
    ReconfigureResult reAdd =
        publisher.reconfigure(
            publisherConfig(port, discoveryPort, Duration.ofHours(1), false),
            PubSubService.ReconfigureMode.DISABLE_AFFECTED);
    assertTrue(reAdd.addedPaths().contains("pub-conn/grp-a"));

    PubSubHandle writerAfter =
        publisher.components().dataSetWriter("pub-conn", "grp-a", "writer-a").orElseThrow();
    assertTrue(publisher.nextDataSetMessageSequenceNumber(writerAfter).longValue() <= 1);

    PubSubDiagnostics.ComponentDiagnostics after =
        publisher.diagnostics().component("pub-conn/grp-a").orElseThrow();
    assertTrue(after.networkMessagesSent() <= 1);
  }

  /**
   * A restart that switches the mapping's sequence-number wire width (UADP UInt16 to JSON UInt32)
   * resets the writer's DataSetMessage sequence to 0: masking a value across widths would fabricate
   * a backward jump with none of the restart-recovery guarantees.
   *
   * <p>Runs on an MQTT connection over an in-memory stub broker transport: the mapping switch is
   * only reachable there (JSON message settings on a UDP connection are rejected by config
   * validation).
   */
  @Test
  void mappingWidthChangeRestartsSequenceAtZero() throws Exception {
    var valuesA = new AtomicReference<>(Map.of("value-a", new DataValue(Variant.ofInt32(1))));

    PubSubService publisher =
        track(
            PubSubService.create(
                widthPublisherConfig(false, Duration.ofMillis(40)),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-a"), mapSource(valuesA))
                    .build(),
                PubSubServiceConfig.builder()
                    .transportProvider(new StubBrokerTransport())
                    .build()));

    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    PubSubHandle writerBefore =
        publisher.components().dataSetWriter("pub-conn", "grp-a", "writer-a").orElseThrow();
    awaitTrue(
        "writer published at least five DataSetMessages",
        () -> publisher.nextDataSetMessageSequenceNumber(writerBefore).intValue() >= 5);

    // switching the group to the JSON mapping is a group-shell change: a path-stable restart,
    // but the counter width changes 16 -> 32, so the sequence restarts at 0 (huge interval:
    // at most the immediate first cycle runs before sampling)
    ReconfigureResult result =
        publisher.reconfigure(
            widthPublisherConfig(true, Duration.ofHours(1)),
            PubSubService.ReconfigureMode.DISABLE_AFFECTED);
    assertEquals(List.of("pub-conn/grp-a"), result.restartedPaths());

    PubSubHandle writerAfter =
        publisher.components().dataSetWriter("pub-conn", "grp-a", "writer-a").orElseThrow();
    assertTrue(publisher.nextDataSetMessageSequenceNumber(writerAfter).longValue() <= 1);
  }

  // region fixtures

  /**
   * Publisher config: one UDP connection with two writer groups; "grp-a" carries "writer-a" on
   * "ds-a", "grp-b" starts empty and gains "writer-b" on "ds-b" when {@code writerBAdded}.
   *
   * @param discoveryPort the loopback discovery port; must be the same for the initial and the
   *     reconfigured config so the connection shell stays unchanged.
   * @param groupAInterval the publishing interval of "grp-a"; changing it between two configs
   *     changes the group shell, restarting the group on reconfigure.
   * @param writerBAdded whether "ds-b" and "grp-b"/"writer-b" are part of the config.
   */
  private static PubSubConfig publisherConfig(
      int port, int discoveryPort, Duration groupAInterval, boolean writerBAdded) {

    PublishedDataSetConfig dataSetA =
        PublishedDataSetConfig.builder("ds-a")
            .field(
                FieldDefinition.builder("value-a")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(FIELD_A_ID)
                    .build())
            .build();

    WriterGroupConfig groupA =
        WriterGroupConfig.builder("grp-a")
            .writerGroupId(ushort(1))
            .publishingInterval(groupAInterval)
            .messageSettings(GROUP_SETTINGS)
            .dataSetWriter(
                DataSetWriterConfig.builder("writer-a")
                    .dataSet(dataSetA.ref())
                    .dataSetWriterId(ushort(1))
                    .settings(WRITER_SETTINGS)
                    .build())
            .build();

    WriterGroupConfig.Builder groupB =
        WriterGroupConfig.builder("grp-b")
            .writerGroupId(ushort(2))
            .publishingInterval(Duration.ofMillis(80))
            .messageSettings(GROUP_SETTINGS);

    PubSubConfig.Builder config = PubSubConfig.builder().publishedDataSet(dataSetA);

    if (writerBAdded) {
      PublishedDataSetConfig dataSetB =
          PublishedDataSetConfig.builder("ds-b")
              .field(
                  FieldDefinition.builder("value-b")
                      .dataType(NodeIds.Int32)
                      .dataSetFieldId(FIELD_B_ID)
                      .build())
              .build();

      config.publishedDataSet(dataSetB);

      groupB.dataSetWriter(
          DataSetWriterConfig.builder("writer-b")
              .dataSet(dataSetB.ref())
              .dataSetWriterId(ushort(2))
              .settings(WRITER_SETTINGS)
              .build());
    }

    return config
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", discoveryPort))
                .writerGroup(groupA)
                .writerGroup(groupB.build())
                .build())
        .build();
  }

  /**
   * Publisher config for the delta-baseline test: one UDP connection, one writer group "grp-delta"
   * carrying "writer-delta" on the two-field dataset "ds-delta" with the given keyFrameCount.
   */
  private static PubSubConfig deltaPublisherConfig(int port, int discoveryPort, int keyFrameCount) {

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds-delta")
            .field(
                FieldDefinition.builder("counter")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(FIELD_COUNTER_ID)
                    .build())
            .field(
                FieldDefinition.builder("constant")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(FIELD_CONSTANT_ID)
                    .build())
            .build();

    WriterGroupConfig group =
        WriterGroupConfig.builder("grp-delta")
            .writerGroupId(ushort(7))
            .publishingInterval(Duration.ofMillis(50))
            .messageSettings(GROUP_SETTINGS)
            .dataSetWriter(
                DataSetWriterConfig.builder("writer-delta")
                    .dataSet(dataSet.ref())
                    .dataSetWriterId(ushort(7))
                    .keyFrameCount(uint(keyFrameCount))
                    .settings(WRITER_SETTINGS)
                    .build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", discoveryPort))
                .writerGroup(group)
                .build())
        .build();
  }

  /** The {@link #publisherConfig} connection with "grp-a" removed ("grp-b" only). */
  private static PubSubConfig publisherConfigWithoutGroupA(int port, int discoveryPort) {
    PublishedDataSetConfig dataSetA =
        PublishedDataSetConfig.builder("ds-a")
            .field(
                FieldDefinition.builder("value-a")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(FIELD_A_ID)
                    .build())
            .build();

    WriterGroupConfig groupB =
        WriterGroupConfig.builder("grp-b")
            .writerGroupId(ushort(2))
            .publishingInterval(Duration.ofMillis(80))
            .messageSettings(GROUP_SETTINGS)
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSetA)
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", discoveryPort))
                .writerGroup(groupB)
                .build())
        .build();
  }

  /**
   * Publisher config for the width-change test: one MQTT connection (JSON message settings are only
   * legal on broker connections), one group "grp-a" with "writer-a" on "ds-a", mapped UADP or JSON
   * — switching the mapping is a group-shell change that also changes the DataSetMessage
   * sequence-number wire width (UInt16 vs UInt32).
   */
  private static PubSubConfig widthPublisherConfig(boolean json, Duration interval) {
    PublishedDataSetConfig dataSetA =
        PublishedDataSetConfig.builder("ds-a")
            .field(
                FieldDefinition.builder("value-a")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(FIELD_A_ID)
                    .build())
            .build();

    WriterGroupConfig group =
        WriterGroupConfig.builder("grp-a")
            .writerGroupId(ushort(1))
            .publishingInterval(interval)
            .messageSettings(json ? JsonWriterGroupSettings.builder().build() : GROUP_SETTINGS)
            .dataSetWriter(
                DataSetWriterConfig.builder("writer-a")
                    .dataSet(dataSetA.ref())
                    .dataSetWriterId(ushort(1))
                    .settings(json ? JsonDataSetWriterSettings.builder().build() : WRITER_SETTINGS)
                    .build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSetA)
        .connection(
            PubSubConnectionConfig.mqtt("pub-conn")
                .publisherId(PUBLISHER_ID)
                .brokerUri(URI.create("mqtt://127.0.0.1:1883"))
                .writerGroup(group)
                .build())
        .build();
  }

  /**
   * An in-memory broker transport for the width-change test: swallows publisher sends, opens no
   * sockets. The width test observes sequence numbers through {@link
   * PubSubService#nextDataSetMessageSequenceNumber}, never the wire.
   */
  private static final class StubBrokerTransport implements TransportProvider {

    @Override
    public String transportProfileUri() {
      return "urn:eclipse:milo:test:stub-broker";
    }

    @Override
    public boolean supports(PubSubConnectionConfig connection) {
      return connection instanceof MqttConnectionConfig;
    }

    @Override
    public PublisherChannel openPublisher(PublisherTransportContext context) {
      return new PublisherChannel() {
        @Override
        public CompletableFuture<Void> send(ByteBuf message) {
          message.release();
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
   * Receive and decode one datagram, returning its single DataSetMessage, or {@code null} when the
   * receive timed out.
   */
  private static @Nullable DecodedDataSetMessage receiveDataSetMessage(DatagramSocket socket)
      throws Exception {

    DecodedNetworkMessage decoded = receiveNetworkMessage(socket);
    return decoded != null && decoded.messages().size() == 1 ? decoded.messages().get(0) : null;
  }

  /**
   * Receive and decode one datagram as a NetworkMessage, or {@code null} when the receive timed
   * out.
   */
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

  /**
   * Subscriber config: readers for both writers, including the writer that only exists after the
   * reconfigure; "reader-b" simply sees no traffic until then.
   */
  private static PubSubConfig subscriberConfig(int port) throws SocketException {
    DataSetMetaDataConfig metaDataA =
        DataSetMetaDataConfig.builder("ds-a").field("value-a", NodeIds.Int32, FIELD_A_ID).build();

    DataSetMetaDataConfig metaDataB =
        DataSetMetaDataConfig.builder("ds-b").field("value-b", NodeIds.Int32, FIELD_B_ID).build();

    ReaderGroupConfig readerGroup =
        ReaderGroupConfig.builder("rgrp")
            .dataSetReader(
                DataSetReaderConfig.builder("reader-a")
                    .publisherId(PUBLISHER_ID)
                    .writerGroupId(ushort(1))
                    .dataSetWriterId(ushort(1))
                    .dataSetMetaData(metaDataA)
                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                    .build())
            .dataSetReader(
                DataSetReaderConfig.builder("reader-b")
                    .publisherId(PUBLISHER_ID)
                    .writerGroupId(ushort(2))
                    .dataSetWriterId(ushort(2))
                    .dataSetMetaData(metaDataB)
                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                    .build())
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("sub-conn")
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(readerGroup)
                .build())
        .build();
  }

  // endregion

  // region helpers

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
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

  // endregion
}
