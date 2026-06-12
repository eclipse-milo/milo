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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.SocketException;
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
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
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

    var valuesA =
        new AtomicReference<Map<String, DataValue>>(
            Map.of("value-a", new DataValue(Variant.ofInt32(1))));

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
    var valuesB =
        new AtomicReference<Map<String, DataValue>>(
            Map.of("value-b", new DataValue(Variant.ofInt32(7))));
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

    var valuesA =
        new AtomicReference<Map<String, DataValue>>(
            Map.of("value-a", new DataValue(Variant.ofInt32(3))));

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
