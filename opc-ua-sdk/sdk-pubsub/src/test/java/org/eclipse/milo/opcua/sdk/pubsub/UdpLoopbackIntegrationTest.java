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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.KeyFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * End-to-end integration tests over unicast loopback UDP with ephemeral ports: a publisher service
 * and a subscriber service exchanging UADP NetworkMessages in-process.
 *
 * <p>Network safety: every UDP connection pins an explicit loopback {@code discoveryAddress}, so
 * the engine's discovery channels never bind the well-known port 4840 or join the default {@code
 * 224.0.2.14} multicast group.
 */
class UdpLoopbackIntegrationTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4711));
  private static final UShort GROUP_ID = ushort(1);

  private static final UUID TEMPERATURE_FIELD_ID = new UUID(0L, 0xA1L);
  private static final UUID STATUS_FIELD_ID = new UUID(0L, 0xA2L);
  private static final UUID COUNTER_FIELD_ID = new UUID(0L, 0xB1L);
  private static final UUID BLOB_FIELD_ID = new UUID(0L, 0xC1L);

  private static final DataSetReaderRef READER_A_REF =
      new DataSetReaderRef("sub-conn", "rgrp", "reader-a");
  private static final DataSetReaderRef READER_B_REF =
      new DataSetReaderRef("sub-conn", "rgrp", "reader-b");
  private static final DataSetReaderRef READER_BLOB_REF =
      new DataSetReaderRef("sub-conn", "rgrp", "reader-blob");

  /**
   * Group settings that put the GroupHeader with WriterGroupId on the wire so readers can apply
   * their WriterGroupId filter.
   */
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

  /**
   * Writer settings that put the full ConfigurationVersion (major and minor) on the wire so the
   * REQUIRE_CONFIGURED version check on the subscriber side is genuinely exercised.
   */
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
  void endToEndPublishSubscribe() throws Exception {
    int port = freeUdpPort();

    var valuesA =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(21.5)),
                "status", new DataValue(Variant.ofString("running"))));
    var valuesB = new AtomicReference<>(Map.of("counter", new DataValue(Variant.ofInt32(42))));

    PubSubBindings publisherBindings =
        PubSubBindings.builder()
            .source(new PublishedDataSetRef("ds-a"), mapSource(valuesA))
            .source(new PublishedDataSetRef("ds-b"), mapSource(valuesB))
            .build();

    PubSubService publisher =
        track(PubSubService.create(publisherConfig(port, uint(1)), publisherBindings));

    var eventsA = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var eventsB = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var globalEvents = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubBindings subscriberBindings =
        PubSubBindings.builder()
            .listener(READER_A_REF, eventsA::add)
            .listener(READER_B_REF, eventsB::add)
            .build();

    PubSubService subscriber =
        track(PubSubService.create(subscriberConfig(port), subscriberBindings));
    subscriber.addDataSetListener(globalEvents::add);

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    DataSetReceivedEvent eventA = awaitEvent(eventsA, event -> true);

    assertEquals(PUBLISHER_ID, eventA.publisherId());
    assertEquals(GROUP_ID, eventA.writerGroupId());
    assertEquals(ushort(1), eventA.dataSetWriterId());
    assertEquals("ds-a", eventA.dataSetName());
    assertNotNull(eventA.metaData());
    assertEquals(uint(7), eventA.metaData().getConfigurationVersionMajor());
    assertEquals(uint(3), eventA.metaData().getConfigurationVersionMinor());

    List<DataSetFieldValue> fieldsA = eventA.fields();
    assertEquals(2, fieldsA.size());
    assertEquals("temperature", fieldsA.get(0).name());
    assertEquals(0, fieldsA.get(0).index());
    assertEquals(TEMPERATURE_FIELD_ID, fieldsA.get(0).dataSetFieldId());
    assertEquals(21.5, fieldsA.get(0).value().value().value());
    assertEquals("status", fieldsA.get(1).name());
    assertEquals(1, fieldsA.get(1).index());
    assertEquals(STATUS_FIELD_ID, fieldsA.get(1).dataSetFieldId());
    assertEquals("running", fieldsA.get(1).value().value().value());

    Map<String, DataValue> byNameA = eventA.fieldsByName();
    assertEquals(21.5, byNameA.get("temperature").value().value());
    assertEquals("running", byNameA.get("status").value().value());

    DataSetReceivedEvent eventB = awaitEvent(eventsB, event -> true);
    assertEquals(ushort(2), eventB.dataSetWriterId());
    assertEquals("ds-b", eventB.dataSetName());
    assertEquals(1, eventB.fields().size());
    assertEquals(COUNTER_FIELD_ID, eventB.fields().get(0).dataSetFieldId());
    assertEquals(42, eventB.fieldsByName().get("counter").value().value());

    // both writers' DataSetMessages travel in the same WriterGroup's NetworkMessages and the
    // global listener sees events from both readers
    awaitEvent(globalEvents, event -> event.dataSetWriterId().intValue() == 1);
    awaitEvent(globalEvents, event -> event.dataSetWriterId().intValue() == 2);

    // both readers completed startup on their first decoded DataSetMessage
    PubSubHandle readerA =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader-a").orElseThrow();
    PubSubHandle readerB =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader-b").orElseThrow();
    assertEquals(PubSubState.Operational, subscriber.state(readerA));
    assertEquals(PubSubState.Operational, subscriber.state(readerB));
    assertEquals(ComponentType.DATA_SET_READER, readerA.componentType());
    assertEquals("sub-conn/rgrp/reader-a", readerA.path());

    // the source is pulled for a fresh snapshot each cycle: an updated value arrives on the wire
    valuesB.set(Map.of("counter", new DataValue(Variant.ofInt32(43))));
    awaitEvent(
        eventsB,
        event -> Integer.valueOf(43).equals(event.fieldsByName().get("counter").value().value()));

    // publisher-side diagnostics
    awaitTrue(
        () ->
            counter(
                    publisher,
                    "pub-conn/grp",
                    PubSubDiagnostics.ComponentDiagnostics::networkMessagesSent)
                > 0,
        "publisher writer group networkMessagesSent > 0");
    assertTrue(
        counter(publisher, "pub-conn", PubSubDiagnostics.ComponentDiagnostics::networkMessagesSent)
            > 0);
    assertTrue(
        counter(
                publisher,
                "pub-conn/grp/writer-a",
                PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesSent)
            > 0);
    assertTrue(
        counter(
                publisher,
                "pub-conn/grp/writer-b",
                PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesSent)
            > 0);

    // subscriber-side diagnostics
    awaitTrue(
        () ->
            counter(
                    subscriber,
                    "sub-conn/rgrp/reader-a",
                    PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesReceived)
                > 0,
        "subscriber reader dataSetMessagesReceived > 0");
    assertTrue(
        counter(
                subscriber,
                "sub-conn",
                PubSubDiagnostics.ComponentDiagnostics::networkMessagesReceived)
            > 0);

    // organic traffic puts both Part 14 §7.2.3 sequence streams on the wire (GROUP_SETTINGS and
    // WRITER_SETTINGS both include SequenceNumber) ...
    assertNotNull(eventA.networkMessageSequenceNumber());
    assertNotNull(eventA.dataSetMessageSequenceNumber());

    // ... and produces no sequence-window drops (false-positive guard for the §7.2.3 window)
    for (String readerPath : List.of("sub-conn/rgrp/reader-a", "sub-conn/rgrp/reader-b")) {
      assertEquals(
          0,
          counter(
              subscriber,
              readerPath,
              PubSubDiagnostics.ComponentDiagnostics::staleSequenceMessages));
      assertEquals(
          0,
          counter(
              subscriber,
              readerPath,
              PubSubDiagnostics.ComponentDiagnostics::invalidSequenceMessages));
    }
  }

  @Test
  void majorVersionMismatchDropsOnlyTheMismatchedReader() throws Exception {
    int port = freeUdpPort();

    var valuesA =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(1.0)),
                "status", new DataValue(Variant.ofString("ok"))));
    var valuesB = new AtomicReference<>(Map.of("counter", new DataValue(Variant.ofInt32(1))));

    // ds-b is published with ConfigurationVersion major 2, but reader-b's configured metadata
    // expects major 1: every message for reader-b must be dropped and counted
    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(port, uint(2)),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-a"), mapSource(valuesA))
                    .source(new PublishedDataSetRef("ds-b"), mapSource(valuesB))
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

    // the matching reader receives multiple events...
    for (int i = 0; i < 3; i++) {
      awaitEvent(eventsA, event -> true);
    }

    // ...while the mismatched reader's drops are counted in its decode-drop diagnostics
    awaitTrue(
        () ->
            counter(
                    subscriber,
                    "sub-conn/rgrp/reader-b",
                    PubSubDiagnostics.ComponentDiagnostics::decodeErrors)
                > 0,
        "decodeErrors on the mismatched reader");

    // the mismatched reader received no events and never completed startup
    assertNull(eventsB.poll());
    PubSubHandle readerB =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader-b").orElseThrow();
    assertEquals(PubSubState.PreOperational, subscriber.state(readerB));

    // and the matching reader still receives after the drops were observed
    eventsA.clear();
    awaitEvent(eventsA, event -> true);
  }

  @Test
  void shutdownIsIdempotentAndDeliveryQuiesces() throws Exception {
    int port = freeUdpPort();

    var valuesA =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(2.0)),
                "status", new DataValue(Variant.ofString("ok"))));
    var valuesB = new AtomicReference<>(Map.of("counter", new DataValue(Variant.ofInt32(2))));

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(port, uint(1)),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-a"), mapSource(valuesA))
                    .source(new PublishedDataSetRef("ds-b"), mapSource(valuesB))
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

    // shutdown is idempotent: repeat calls complete normally, as does close() afterwards
    CompletableFuture<Void> firstShutdown = publisher.shutdown();
    firstShutdown.get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.close();

    // no events after shutdown: drain in-flight deliveries, then observe a quiet window that
    // would have carried several publish cycles
    Thread.sleep(300);
    eventsA.clear();
    Thread.sleep(500);
    assertTrue(eventsA.isEmpty(), "no events expected after publisher shutdown");

    subscriber.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    subscriber.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // components of a shut-down service are Disabled
    PubSubHandle connection = subscriber.components().connection("sub-conn").orElseThrow();
    assertEquals(PubSubState.Disabled, subscriber.state(connection));
  }

  @Test
  void startupFailsWhenEnabledWriterHasNoSource() throws Exception {
    int port = freeUdpPort();

    // no bindings and no bindSource call: the enabled writer's dataset has no source
    PubSubService service = track(PubSubService.create(publisherConfig(port, uint(1))));

    StatusCode statusCode = assertStartupFails(service);
    assertEquals(StatusCodes.Bad_ConfigurationError, statusCode.value());
  }

  /**
   * A secured mode is configuration, not an unsupported feature (K3): a Sign group with no
   * SecurityGroup reference (and no bound key provider) fails startup with {@code
   * Bad_ConfigurationError} naming the missing piece.
   */
  @Test
  void startupFailsWhenSecurityModeIsSign() throws Exception {
    int port = freeUdpPort();

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds-a")
            .field(FieldDefinition.builder("value").dataType(NodeIds.Int32).build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("pub-conn")
                    .publisherId(PUBLISHER_ID)
                    .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(GROUP_ID)
                            .publishingInterval(Duration.ofMillis(100))
                            .messageSecurity(
                                MessageSecurityConfig.builder()
                                    .mode(MessageSecurityMode.Sign)
                                    .build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .build())
                            .build())
                    .build())
            .build();

    var values = new AtomicReference<>(Map.of("value", new DataValue(Variant.ofInt32(0))));

    PubSubService service =
        track(
            PubSubService.create(
                config, PubSubBindings.builder().source(dataSet.ref(), mapSource(values)).build()));

    UaException cause = assertStartupFailsExceptionally(service);
    assertEquals(StatusCodes.Bad_ConfigurationError, cause.getStatusCode().value());
    assertTrue(
        String.valueOf(cause.getMessage()).contains("SecurityGroup"),
        "expected the missing SecurityGroup reference to be named: " + cause.getMessage());
  }

  @Test
  void startupFailsForMqttConnectionWithoutProvider() throws Exception {
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt("mqtt-conn")
                    .brokerUri(URI.create("mqtt://127.0.0.1:1883"))
                    .build())
            .build();

    PubSubService service = track(PubSubService.create(config));

    StatusCode statusCode = assertStartupFails(service);
    assertEquals(StatusCodes.Bad_ConfigurationError, statusCode.value());
  }

  /**
   * The Annex A.2.2 "UADP-Dynamic" default settings put only the <em>minor</em> configuration
   * version on the wire. Per Part 14 the Subscriber can only check the major version when it is
   * transmitted, so a REQUIRE_CONFIGURED reader whose configured metadata matches the published
   * dataset must still receive events under the module's own defaults.
   *
   * <p>SUSPECTED PRODUCT BUG: {@code ReaderDispatcher} substitutes 0 for the absent major version
   * and treats it as a mismatch with the configured metadata's major version, so the all-default
   * configuration never delivers anything to a REQUIRE_CONFIGURED reader.
   */
  @Test
  void uadpDynamicDefaultsDeliverToRequireConfiguredReader() throws Exception {
    int port = freeUdpPort();

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds-default")
            .field(
                FieldDefinition.builder("counter")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(COUNTER_FIELD_ID)
                    .build())
            .build(); // default ConfigurationVersion (1, 1)

    // all-default UADP message settings on group and writer
    PubSubConfig publisherConfig =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("pub-conn")
                    .publisherId(PUBLISHER_ID)
                    .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(GROUP_ID)
                            .publishingInterval(Duration.ofMillis(75))
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .build())
                            .build())
                    .build())
            .build();

    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("ds-default")
            .field("counter", NodeIds.Int32, COUNTER_FIELD_ID)
            .build(); // default ConfigurationVersion (1, 1)

    // no WriterGroupId filter: the default network message content mask has no GroupHeader
    PubSubConfig subscriberConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp("sub-conn")
                    .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .dataSetReader(
                                DataSetReaderConfig.builder("reader")
                                    .publisherId(PUBLISHER_ID)
                                    .dataSetWriterId(ushort(1))
                                    .dataSetMetaData(metaData)
                                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                    .build())
                            .build())
                    .build())
            .build();

    var values = new AtomicReference<>(Map.of("counter", new DataValue(Variant.ofInt32(5))));

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig,
                PubSubBindings.builder().source(dataSet.ref(), mapSource(values)).build()));

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubService subscriber =
        track(
            PubSubService.create(
                subscriberConfig,
                PubSubBindings.builder()
                    .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader"), events::add)
                    .build()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    DataSetReceivedEvent event = awaitEvent(events, e -> true);
    assertEquals(5, event.fieldsByName().get("counter").value().value());
  }

  /**
   * Regression for the receive-side 2048-byte truncation: without an explicit receive buffer
   * allocator, Netty's datagram default truncated reads at 2048 bytes, so a larger NetworkMessage
   * could never arrive intact. The subscriber channel now sizes its receive buffer to the Part 14
   * §7.3.2.1 UDP maximum of 65535, so an ~8 KB dataset must deliver end-to-end.
   */
  @Test
  void datagramsLargerThan2048BytesDeliverEndToEnd() throws Exception {
    int port = freeUdpPort();

    byte[] blob = new byte[8000];
    Arrays.fill(blob, (byte) 0x5A);

    var values =
        new AtomicReference<>(
            Map.of("blob", new DataValue(Variant.ofByteString(ByteString.of(blob)))));

    PubSubService publisher =
        track(
            PubSubService.create(
                blobPublisherConfig(port, freeUdpPort(), uint(0)),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-blob"), mapSource(values))
                    .build()));

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubService subscriber =
        track(
            PubSubService.create(
                blobSubscriberConfig(port),
                PubSubBindings.builder().listener(READER_BLOB_REF, events::add).build()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    DataSetReceivedEvent event = awaitEvent(events, e -> true);
    ByteString received = blobOf(event);
    assertEquals(8000, received.length());
    assertArrayEquals(blob, received.bytesOrEmpty());
  }

  /**
   * Writer-side maxNetworkMessageSize enforcement (Part 14 §6.2.5.5 / Annex B.3.1): a
   * NetworkMessage whose encoded size exceeds the group's non-zero budget is skipped with {@code
   * Bad_EncodingLimitsExceeded} and never delivered, while messages within the budget keep flowing.
   */
  @Test
  void oversizePublishIsRejectedWhileSmallerMessagesKeepFlowing() throws Exception {
    int port = freeUdpPort();

    byte[] initialBlob = new byte[64];
    Arrays.fill(initialBlob, (byte) 0x01);
    byte[] oversizeBlob = new byte[8000];
    Arrays.fill(oversizeBlob, (byte) 0x02);
    byte[] resumedBlob = new byte[64];
    Arrays.fill(resumedBlob, (byte) 0x03);

    var values =
        new AtomicReference<>(
            Map.of("blob", new DataValue(Variant.ofByteString(ByteString.of(initialBlob)))));

    PubSubService publisher =
        track(
            PubSubService.create(
                blobPublisherConfig(port, freeUdpPort(), uint(2048)),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-blob"), mapSource(values))
                    .build()));

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubService subscriber =
        track(
            PubSubService.create(
                blobSubscriberConfig(port),
                PubSubBindings.builder().listener(READER_BLOB_REF, events::add).build()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // messages within the budget flow
    awaitEvent(events, event -> blobOf(event).bytesOrEmpty()[0] == 0x01);

    // an oversize message is skipped at publish time and recorded at the group path
    values.set(Map.of("blob", new DataValue(Variant.ofByteString(ByteString.of(oversizeBlob)))));
    awaitTrue(
        () -> {
          PubSubDiagnostics.ComponentDiagnostics diagnostics =
              publisher.diagnostics().component("pub-conn/grp").orElse(null);
          return diagnostics != null
              && new StatusCode(StatusCodes.Bad_EncodingLimitsExceeded)
                  .equals(diagnostics.lastError());
        },
        "writer group lastError Bad_EncodingLimitsExceeded");

    // smaller messages keep flowing afterwards, and nothing oversize was ever delivered
    values.set(Map.of("blob", new DataValue(Variant.ofByteString(ByteString.of(resumedBlob)))));
    awaitEvent(
        events,
        event -> {
          ByteString blob = blobOf(event);
          assertNotEquals(8000, blob.length(), "oversize NetworkMessage must not be delivered");
          return blob.bytesOrEmpty()[0] == 0x03;
        });
  }

  /** Part 14 §7.3.2.1: a UDP writer group's maxNetworkMessageSize must not exceed 65535. */
  @Test
  void startupFailsWhenUdpWriterGroupMaxNetworkMessageSizeExceeds65535() throws Exception {
    int port = freeUdpPort();

    var values =
        new AtomicReference<>(
            Map.of("blob", new DataValue(Variant.ofByteString(ByteString.of(new byte[16])))));

    PubSubService service =
        track(
            PubSubService.create(
                blobPublisherConfig(port, freeUdpPort(), uint(65536)),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-blob"), mapSource(values))
                    .build()));

    StatusCode statusCode = assertStartupFails(service);
    assertEquals(StatusCodes.Bad_ConfigurationError, statusCode.value());
  }

  /** §7.3.2.1 boundary: maxNetworkMessageSize of exactly 65535 is valid (the check is strict >). */
  @Test
  void startupSucceedsWhenUdpWriterGroupMaxNetworkMessageSizeIs65535() throws Exception {
    int port = freeUdpPort();

    var values =
        new AtomicReference<>(
            Map.of("blob", new DataValue(Variant.ofByteString(ByteString.of(new byte[16])))));

    PubSubService service =
        track(
            PubSubService.create(
                blobPublisherConfig(port, freeUdpPort(), uint(65535)),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-blob"), mapSource(values))
                    .build()));

    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("pub-conn", "grp").orElseThrow();
    awaitTrue(() -> service.state(group) == PubSubState.Operational, "writer group Operational");
  }

  /** Disabled-component tolerance: the §7.3.2.1 limit is only validated for enabled groups. */
  @Test
  void disabledUdpWriterGroupMayExceed65535AtStartup() throws Exception {
    int port = freeUdpPort();

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds-blob")
            .field(FieldDefinition.builder("blob").dataType(NodeIds.ByteString).build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("pub-conn")
                    .publisherId(PUBLISHER_ID)
                    .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .enabled(false)
                            .writerGroupId(GROUP_ID)
                            .maxNetworkMessageSize(uint(100_000))
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubService service = track(PubSubService.create(config));

    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("pub-conn", "grp").orElseThrow();
    assertEquals(PubSubState.Disabled, service.state(group));
  }

  /**
   * The §7.3.2.1 limit is enforced at reconfiguration too, and the invalid config is rejected
   * BEFORE anything is applied: the running writer group is untouched.
   */
  @Test
  void reconfigureRejectsUdpWriterGroupMaxNetworkMessageSizeExceeding65535() throws Exception {
    int port = freeUdpPort();
    int discoveryPort = freeUdpPort(); // reused so only maxNetworkMessageSize differs

    var values =
        new AtomicReference<>(
            Map.of("blob", new DataValue(Variant.ofByteString(ByteString.of(new byte[16])))));

    PubSubService service =
        track(
            PubSubService.create(
                blobPublisherConfig(port, discoveryPort, uint(0)),
                PubSubBindings.builder()
                    .source(new PublishedDataSetRef("ds-blob"), mapSource(values))
                    .build()));

    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    PubSubConfig invalid = blobPublisherConfig(port, discoveryPort, uint(65536));

    UaRuntimeException e =
        assertThrows(
            UaRuntimeException.class,
            () -> service.reconfigure(invalid, PubSubService.ReconfigureMode.DISABLE_AFFECTED));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(
        e.getMessage().contains("exceeds the OPC UA UDP limit"),
        "unexpected message: " + e.getMessage());

    // rejected before applying: the running writer group is still Operational
    PubSubHandle group = service.components().writerGroup("pub-conn", "grp").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(group));
  }

  // region fixtures

  /**
   * Publisher config: one UDP connection, one writer group at 75 ms with two writers on two
   * published datasets, sending to 127.0.0.1:{@code port}.
   *
   * @param dsBMajorVersion the ConfigurationVersion major version of "ds-b"; pass a value other
   *     than 1 to provoke a metadata version mismatch at the subscriber.
   */
  private static PubSubConfig publisherConfig(int port, UInteger dsBMajorVersion)
      throws SocketException {
    PublishedDataSetConfig dataSetA =
        PublishedDataSetConfig.builder("ds-a")
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
            .configurationVersion(uint(7), uint(3))
            .build();

    PublishedDataSetConfig dataSetB =
        PublishedDataSetConfig.builder("ds-b")
            .field(
                FieldDefinition.builder("counter")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(COUNTER_FIELD_ID)
                    .build())
            .configurationVersion(dsBMajorVersion, uint(1))
            .build();

    WriterGroupConfig writerGroup =
        WriterGroupConfig.builder("grp")
            .writerGroupId(GROUP_ID)
            .publishingInterval(Duration.ofMillis(75))
            .messageSettings(GROUP_SETTINGS)
            .dataSetWriter(
                DataSetWriterConfig.builder("writer-a")
                    .dataSet(dataSetA.ref())
                    .dataSetWriterId(ushort(1))
                    .settings(WRITER_SETTINGS)
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("writer-b")
                    .dataSet(dataSetB.ref())
                    .dataSetWriterId(ushort(2))
                    .settings(WRITER_SETTINGS)
                    .build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSetA)
        .publishedDataSet(dataSetB)
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(writerGroup)
                .build())
        .build();
  }

  /**
   * Subscriber config: one UDP connection bound to 127.0.0.1:{@code port}, one reader group with
   * two readers matching the writers of {@link #publisherConfig}, both REQUIRE_CONFIGURED with
   * configured metadata.
   */
  private static PubSubConfig subscriberConfig(int port) throws SocketException {
    DataSetMetaDataConfig metaDataA =
        DataSetMetaDataConfig.builder("ds-a")
            .field("temperature", NodeIds.Double, TEMPERATURE_FIELD_ID)
            .field("status", NodeIds.String, STATUS_FIELD_ID)
            .configurationVersion(uint(7), uint(3))
            .build();

    DataSetMetaDataConfig metaDataB =
        DataSetMetaDataConfig.builder("ds-b")
            .field("counter", NodeIds.Int32, COUNTER_FIELD_ID)
            .configurationVersion(uint(1), uint(1))
            .build();

    ReaderGroupConfig readerGroup =
        ReaderGroupConfig.builder("rgrp")
            .dataSetReader(
                DataSetReaderConfig.builder("reader-a")
                    .publisherId(PUBLISHER_ID)
                    .writerGroupId(GROUP_ID)
                    .dataSetWriterId(ushort(1))
                    .dataSetMetaData(metaDataA)
                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                    .build())
            .dataSetReader(
                DataSetReaderConfig.builder("reader-b")
                    .publisherId(PUBLISHER_ID)
                    .writerGroupId(GROUP_ID)
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

  /**
   * Publisher config for the large-payload tests: one writer group at 75 ms with a single writer on
   * a ByteString dataset, sending to 127.0.0.1:{@code port}.
   *
   * @param discoveryPort the discovery port, passed explicitly so a reconfiguration config can keep
   *     the connection shell identical.
   * @param maxNetworkMessageSize the writer group's maxNetworkMessageSize; 0 = no enforced limit.
   */
  private static PubSubConfig blobPublisherConfig(
      int port, int discoveryPort, UInteger maxNetworkMessageSize) {

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds-blob")
            .field(
                FieldDefinition.builder("blob")
                    .dataType(NodeIds.ByteString)
                    .dataSetFieldId(BLOB_FIELD_ID)
                    .build())
            .configurationVersion(uint(1), uint(1))
            .build();

    WriterGroupConfig writerGroup =
        WriterGroupConfig.builder("grp")
            .writerGroupId(GROUP_ID)
            .publishingInterval(Duration.ofMillis(75))
            .maxNetworkMessageSize(maxNetworkMessageSize)
            .messageSettings(GROUP_SETTINGS)
            .dataSetWriter(
                DataSetWriterConfig.builder("writer-blob")
                    .dataSet(dataSet.ref())
                    .dataSetWriterId(ushort(1))
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
                .writerGroup(writerGroup)
                .build())
        .build();
  }

  /**
   * Subscriber config matching {@link #blobPublisherConfig}: one REQUIRE_CONFIGURED reader on the
   * ByteString dataset, bound to 127.0.0.1:{@code port}.
   */
  private static PubSubConfig blobSubscriberConfig(int port) throws SocketException {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("ds-blob")
            .field("blob", NodeIds.ByteString, BLOB_FIELD_ID)
            .configurationVersion(uint(1), uint(1))
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("sub-conn")
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(
                    ReaderGroupConfig.builder("rgrp")
                        .dataSetReader(
                            DataSetReaderConfig.builder("reader-blob")
                                .publisherId(PUBLISHER_ID)
                                .writerGroupId(GROUP_ID)
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

  /** Poll {@code condition} until it holds or the deadline expires. */
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

  /** The "blob" field of an event from the {@link #blobSubscriberConfig} reader. */
  private static ByteString blobOf(DataSetReceivedEvent event) {
    DataValue value = event.fieldsByName().get("blob");
    assertNotNull(value, "no 'blob' field in event");
    return (ByteString) value.value().value();
  }

  private static long counter(
      PubSubService service,
      String path,
      ToLongFunction<PubSubDiagnostics.ComponentDiagnostics> counter) {

    PubSubDiagnostics.ComponentDiagnostics diagnostics =
        service.diagnostics().component(path).orElse(null);

    return diagnostics == null ? 0L : counter.applyAsLong(diagnostics);
  }

  /** Assert that {@code startup()} fails and return the {@link StatusCode} of its UaException. */
  private static StatusCode assertStartupFails(PubSubService service) {
    return assertStartupFailsExceptionally(service).getStatusCode();
  }

  private static UaException assertStartupFailsExceptionally(PubSubService service) {
    ExecutionException e =
        assertThrows(
            ExecutionException.class,
            () -> service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS));

    Throwable cause = e.getCause();
    while (cause != null && !(cause instanceof UaException)) {
      cause = cause.getCause();
    }
    assertNotNull(cause, "expected a UaException cause, got: " + e);
    return (UaException) cause;
  }

  // endregion
}
