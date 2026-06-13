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
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.awaitEvent;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.awaitTrue;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.lastError;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.mapSource;
import static org.eclipse.milo.opcua.sdk.pubsub.mqtt.PubSubMqttTestSupport.mqttServiceConfig;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
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
 * Broker-outage recovery: the embedded broker is stopped and a fresh instance is started on the
 * same port (CE supports this; the second instance pins the discovered port explicitly). The HiveMQ
 * clients reconnect automatically, the subscriber session re-issues its subscriptions, and data
 * flow resumes.
 *
 * <p>Timing: HiveMQ reconnect backoff starts at 1 s and doubles (±25% jitter, capped at 120 s), and
 * a broker boot takes roughly 1.5-8 s, so resumption is awaited with a generous timeout.
 */
class MqttReconnectTest {

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(777));

  private static final String DATA_TOPIC = "opcua/uadp/data/777/grp";

  /** Resumption budget: broker boot + reconnect backoff that may have grown during the boot. */
  private static final Duration RESUME_TIMEOUT = Duration.ofSeconds(60);

  @TempDir static Path tempDir;

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
  void subscriberResubscribesAndDataFlowResumesAfterBrokerRestart() throws Exception {
    EmbeddedTestBroker firstBroker = EmbeddedTestBroker.start(tempDir.resolve("broker1"));
    int port = firstBroker.port();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubService publisher;
    PubSubService subscriber;
    try {
      publisher = startPublisher(port);
      subscriber = startSubscriber(port, events);

      // data flows through the first broker instance
      awaitEvent(events, event -> true, Duration.ofSeconds(30));
    } catch (Exception e) {
      firstBroker.stop();
      throw e;
    }

    // stop the broker: sends start failing into diagnostics (the transport has no
    // connection-state callback in this version, so PubSubState is not asserted here)
    firstBroker.stop();

    awaitTrue(
        () -> lastError(publisher, "pub-conn/grp") != null,
        "publisher send failures recorded after broker stop",
        Duration.ofSeconds(30));

    events.clear();

    // a FRESH broker instance on the SAME port; in-memory persistence means all broker state
    // (sessions, retained messages) is gone, so the resumed flow proves the clients
    // reconnected and the subscriber re-issued its subscriptions
    EmbeddedTestBroker secondBroker = EmbeddedTestBroker.start(tempDir.resolve("broker2"), port);
    try {
      DataSetReceivedEvent event = awaitEvent(events, e -> true, RESUME_TIMEOUT);
      assertEquals(ushort(1), event.dataSetWriterId());
      assertEquals(21.5, event.fieldsByName().get("temperature").value().value());

      // the reader settles (back) into Operational
      PubSubHandle reader =
          subscriber.components().dataSetReader("sub-conn", "rgrp", "reader-a").orElseThrow();
      awaitTrue(
          () -> subscriber.state(reader) == PubSubState.Operational,
          "reader Operational after broker restart",
          Duration.ofSeconds(30));

      // shut down the services before the broker so teardown is clean and quiet
      subscriber.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      publisher.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
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

  private PubSubService startSubscriber(int port, LinkedBlockingQueue<DataSetReceivedEvent> events)
      throws Exception {

    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("ds-a").field("temperature", NodeIds.Double).build();

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
                                    .dataSetMetaData(metaData)
                                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                    .brokerTransport(
                                        BrokerTransportSettings.builder()
                                            .queueName(DATA_TOPIC)
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubService subscriber =
        track(
            PubSubService.create(
                config,
                PubSubBindings.builder()
                    .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader-a"), events::add)
                    .build(),
                mqttServiceConfig()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return subscriber;
  }

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  // endregion
}
