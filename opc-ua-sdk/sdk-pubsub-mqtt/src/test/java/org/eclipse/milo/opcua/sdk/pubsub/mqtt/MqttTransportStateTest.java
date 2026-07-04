/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.mqtt;

import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.TIMEOUT;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.awaitTrue;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.mapSource;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.mqttServiceConfig;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * R16 transport-state rows beyond {@code MqttReconnectTest} (which owns the Error-on-stop /
 * recover-resubscribe-republish choreography and asserts the retained-metadata republish with a raw
 * probe): after a broker restart and recovery, a FRESH Milo subscriber-only service — whose broker
 * session finds only what the recovered publisher re-published — receives the DataSetMetaData
 * through the normal decode path as a {@link MetaDataReceivedEvent}; and the shutdown drain
 * guarantee holds around reconnects: once {@code shutdown()} resolves, no state or diagnostics
 * event is delivered, even when the broker comes back and a reconnect races the teardown.
 *
 * <p>Timing: HiveMQ reconnect backoff starts at 1 s and doubles (±25% jitter), and a broker boot
 * takes roughly 1.5-8 s, so resumption is awaited with a generous timeout. JDK 17/21 only (HiveMQ
 * CE does not bootstrap on 25).
 */
class MqttTransportStateTest {

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(778));

  private static final String DATA_TOPIC = "opcua/uadp/data/778/grp";

  /** The Part 14 §7.3.4.7.4 derived metadata topic of writer-a. */
  private static final String META_TOPIC = "opcua/uadp/metadata/778/grp/writer-a";

  /** Resumption budget: broker boot + reconnect backoff that may have grown during the boot. */
  private static final Duration RESUME_TIMEOUT = Duration.ofSeconds(60);

  /** How long a wrong post-shutdown delivery is given to appear before the silence row passes. */
  private static final Duration SILENCE = Duration.ofMillis(750);

  @TempDir Path tempDir;

  private final List<PubSubService> services = new CopyOnWriteArrayList<>();

  @AfterEach
  void tearDown() throws InterruptedException {
    for (PubSubService service : services) {
      try {
        service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        // best effort cleanup
      }
    }
    services.clear();
  }

  @Test
  void freshSubscriberReceivesRepublishedMetaDataAfterBrokerRestart() throws Exception {
    EmbeddedTestBroker firstBroker = EmbeddedTestBroker.start(tempDir.resolve("broker1"));
    int port = firstBroker.port();

    PubSubService publisher;
    PubSubHandle publisherConnection;
    try {
      publisher = startPublisher(port);
      publisherConnection = publisher.components().connection("pub-conn").orElseThrow();

      // the first broker instance demonstrably carries the publisher's traffic
      try (RawMqtt5Probe probe = RawMqtt5Probe.connect(port)) {
        probe.subscribe(DATA_TOPIC, MqttQos.AT_MOST_ONCE);
        probe.awaitMessage(Duration.ofSeconds(30));
      }
    } catch (Exception e) {
      firstBroker.stop();
      throw e;
    }

    // stop the broker (all in-memory state, including the retained metadata, is lost) and
    // bring a FRESH instance up on the SAME port; R16 recovery reactivates the writer and
    // re-publishes the retained metadata announce
    firstBroker.stop();
    awaitTrue(
        () -> publisher.state(publisherConnection) == PubSubState.Error,
        "publisher connection Error after broker stop",
        Duration.ofSeconds(30));

    EmbeddedTestBroker secondBroker = EmbeddedTestBroker.start(tempDir.resolve("broker2"), port);
    try {
      awaitTrue(
          () -> publisher.state(publisherConnection) == PubSubState.Operational,
          "publisher connection Operational after broker restart",
          RESUME_TIMEOUT);

      // a FRESH subscriber-only service (created only now — its broker session can only see
      // what the recovered publisher put on the fresh broker) receives the DataSetMetaData
      // through the normal decode path, not just as raw broker bytes
      var metaDataEvents = new LinkedBlockingQueue<MetaDataReceivedEvent>();

      PubSubService subscriber = startLateSubscriber(port);
      subscriber.addMetaDataListener(metaDataEvents::add);

      MetaDataReceivedEvent event = awaitMetaData(metaDataEvents, RESUME_TIMEOUT);
      assertEquals("ds-a", event.dataSetName());
      assertEquals("sub-conn/rgrp/reader-a", event.reader().path());

      // shut down the services before the broker so teardown is clean and quiet
      subscriber.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      publisher.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    } finally {
      secondBroker.stop();
    }
  }

  @Test
  void noStateEventsAreDeliveredAfterShutdownResolves() throws Exception {
    EmbeddedTestBroker firstBroker = EmbeddedTestBroker.start(tempDir.resolve("s-broker1"));
    int port = firstBroker.port();

    var stateEvents = new LinkedBlockingQueue<PubSubStateChangeEvent>();

    PubSubService publisher;
    PubSubHandle publisherConnection;
    try {
      publisher = startPublisher(port);
      publisher.addStateListener(stateEvents::add);
      publisherConnection = publisher.components().connection("pub-conn").orElseThrow();
      awaitTrue(
          () -> publisher.state(publisherConnection) == PubSubState.Operational,
          "publisher connection Operational",
          Duration.ofSeconds(30));

      // prove the MQTT session is CONNECTED (traffic flows) before stopping the broker:
      // Operational alone is granted by the engine state machine at startup completion and
      // does not prove the CONNACK arrived, while the R16 down edge is deliberately
      // connected-gated (a session that never connected is not an outage) — stopping the
      // broker inside the pre-CONNACK window would never produce the Error awaited below
      try (RawMqtt5Probe probe = RawMqtt5Probe.connect(port)) {
        probe.subscribe(DATA_TOPIC, MqttQos.AT_MOST_ONCE);
        probe.awaitMessage(Duration.ofSeconds(30));
      }
    } catch (Exception e) {
      firstBroker.stop();
      throw e;
    }

    // the outage puts the client into its reconnect loop
    firstBroker.stop();
    awaitTrue(
        () -> publisher.state(publisherConnection) == PubSubState.Error,
        "publisher connection Error after broker stop",
        Duration.ofSeconds(30));

    // bring the broker back so a reconnect races the teardown, then shut down; the shutdown
    // future resolves only after the event queue drained, so everything enqueued before the
    // drain is delivered BEFORE get() returns — and nothing may follow it
    EmbeddedTestBroker secondBroker = EmbeddedTestBroker.start(tempDir.resolve("s-broker2"), port);
    try {
      publisher.shutdown().get(RESUME_TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      stateEvents.clear();
      assertNull(
          stateEvents.poll(SILENCE.toMillis(), TimeUnit.MILLISECONDS),
          "no state event may be delivered after shutdown resolved");
    } finally {
      secondBroker.stop();
    }
  }

  // region fixtures

  private PubSubService startPublisher(int port) throws Exception {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds-a")
            .field(FieldDefinition.builder("temperature").dataType(NodeIds.Double).build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.mqtt("pub-conn")
                    .brokerUri(URI.create("mqtt://127.0.0.1:" + port))
                    .publisherId(PUBLISHER_ID)
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(100))
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer-a")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .build())
                            .build())
                    .build())
            .build();

    var values =
        new AtomicReference<>(Map.of("temperature", new DataValue(Variant.ofDouble(21.5))));

    PubSubService publisher =
        track(
            PubSubService.create(
                config,
                PubSubBindings.builder().source(dataSet.ref(), mapSource(values)).build(),
                mqttServiceConfig()));

    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return publisher;
  }

  /**
   * A subscriber whose reader has NO configured metadata: it accepts the discovered metadata
   * arriving on the derived metadata topic (retained replay of the publisher's announce).
   */
  private PubSubService startLateSubscriber(int port) throws Exception {
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt("sub-conn")
                    .brokerUri(URI.create("mqtt://127.0.0.1:" + port))
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .dataSetReader(
                                DataSetReaderConfig.builder("reader-a")
                                    .publisherId(PUBLISHER_ID)
                                    .dataSetWriterId(ushort(1))
                                    .metadataPolicy(MetadataPolicy.ACCEPT_DISCOVERED)
                                    .brokerTransport(
                                        BrokerTransportSettings.builder()
                                            .queueName(DATA_TOPIC)
                                            .metaDataQueueName(META_TOPIC)
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubService subscriber =
        track(PubSubService.create(config, PubSubBindings.builder().build(), mqttServiceConfig()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return subscriber;
  }

  private static MetaDataReceivedEvent awaitMetaData(
      LinkedBlockingQueue<MetaDataReceivedEvent> events, Duration timeout)
      throws InterruptedException {

    MetaDataReceivedEvent event = events.poll(timeout.toMillis(), TimeUnit.MILLISECONDS);
    if (event == null) {
      return fail("timed out waiting for a MetaDataReceivedEvent");
    }
    return event;
  }

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  // endregion
}
