/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.pubsub.mqtt;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.mqtt.MqttTransportProvider;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A standalone PubSub publisher sending JSON {@code ua-data} NetworkMessages through an MQTT
 * broker.
 *
 * <p>What it demonstrates:
 *
 * <ul>
 *   <li>registering the {@link MqttTransportProvider} explicitly via {@link
 *       PubSubServiceConfig.Builder#transportProvider} — there is no ServiceLoader auto-discovery.
 *   <li>selecting the built-in "json" message mapping by using {@code Json*} message settings on
 *       the writer group and dataset writer (all-default content masks).
 *   <li>pull-model publishing: a {@code PublishedDataSetSource} is invoked once per publishing
 *       cycle and returns a {@code DataSetSnapshot} of the current field values.
 *   <li>retained metadata publishing, which on broker connections happens automatically: at writer
 *       activation the engine publishes the dataset's DataSetMetaData as a retained QoS 1 {@code
 *       ua-metadata} document to {@code opcua/json/metadata/3001/demo/writer}, where late
 *       subscribers discover the field definitions (see {@code MqttJsonSubscriberExample}).
 *   <li>retained publisher status for JSON-over-MQTT: the engine publishes {@code ua-status} to
 *       {@code opcua/json/status/3001} and, because this connection uses only JSON writer groups,
 *       configures the same topic as a retained MQTT Last Will for fast publisher-death detection.
 * </ul>
 *
 * <p>The PublisherId is UInt16 3001. JSON carries publisher ids as strings, so on the wire it
 * appears as {@code "3001"} and in the derived data topic {@code opcua/json/data/3001/demo};
 * subscribers match it by canonical string form regardless of the id's configured type.
 *
 * <p>Terminal order:
 *
 * <ol>
 *   <li>Terminal 1: an MQTT broker listening at {@code mqtt://127.0.0.1:1883} — for example {@code
 *       EmbeddedBrokerExample} from this package, or an external broker such as Mosquitto.
 *   <li>Terminal 2: this publisher.
 *   <li>Terminal 3: {@code MqttJsonSubscriberExample}. (Terminals 2 and 3 may be started in either
 *       order: the broker retains the metadata and status documents.)
 * </ol>
 *
 * <p>Run from the repository root:
 *
 * <pre>{@code
 * mvn -pl milo-examples/pubsub-examples -am compile
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttJsonPublisherExample
 * }</pre>
 *
 * <p>An alternative broker URI may be passed as the first argument, e.g. {@code
 * -Dexec.args="mqtt://10.0.0.5:1883"}. Do not add {@code -q}: {@code exec:java} runs in-process,
 * and Maven's quiet flag also silences the example's slf4j-simple logging.
 *
 * <p>Expected output: a startup line naming the broker URI and data topic, then one "publish cycle
 * N: ..." line on the first cycle and every 10th cycle after that (one cycle per second). Runs
 * until Ctrl-C.
 *
 * <p>Note: per Part 14 §7.3.4.4 the MQTT ClientId defaults to the canonical PublisherId ("3001"),
 * so starting a second copy of this publisher against the same broker triggers an MQTT session
 * takeover fight — both copies will log repeated "Server sent DISCONNECT" reconnect cycles.
 */
public class MqttJsonPublisherExample {

  private static final String DEFAULT_BROKER_URI = "mqtt://127.0.0.1:1883";

  public static void main(String[] args) throws Exception {
    URI brokerUri = URI.create(args.length > 0 ? args[0] : DEFAULT_BROKER_URI);

    new MqttJsonPublisherExample().run(brokerUri);
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicLong cycle = new AtomicLong();

  private void run(URI brokerUri) throws Exception {
    // Field order defines the wire order; each field's source defaults to a key matching the
    // field name, read from the snapshot built in readDataSet() below.
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("demo-dataset")
            .field(FieldDefinition.builder("temperature").dataType(NodeIds.Double).build())
            .field(FieldDefinition.builder("status").dataType(NodeIds.String).build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.mqtt("connection")
                    .brokerUri(brokerUri)
                    // a UInt16 id; JSON puts its canonical string form "3001" on the wire
                    .publisherId(PublisherId.uint16(ushort(3001)))
                    .writerGroup(
                        WriterGroupConfig.builder("demo")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofSeconds(1))
                            // Json* settings select the "json" mapping; the empty content
                            // masks select the recommended Part 14 defaults
                            .messageSettings(JsonWriterGroupSettings.builder().build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .settings(JsonDataSetWriterSettings.builder().build())
                                    .build())
                            .build())
                    .build())
            .build();

    // No queueName is configured anywhere, so the engine derives the Part 14 topic tree:
    //   data:     opcua/json/data/3001/demo            (QoS 0, not retained)
    //   metadata: opcua/json/metadata/3001/demo/writer (QoS 1, retained, sent automatically)
    //   status:   opcua/json/status/3001               (QoS 1, retained, plus MQTT Will)
    PubSubService service =
        PubSubService.create(
            config,
            PubSubBindings.builder().source(dataSet.ref(), this::readDataSet).build(),
            PubSubServiceConfig.builder()
                .transportProvider(MqttTransportProvider.create())
                .build());

    service.startup().get();

    logger.info(
        "publisher started: broker={}, publishing 'ua-data' to topic opcua/json/data/3001/demo"
            + " every 1s",
        brokerUri);

    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  service.close();
                  future.complete(null);
                }));

    future.get();
  }

  /**
   * The {@code PublishedDataSetSource} for "demo-dataset": called by the engine once per publishing
   * cycle, on the publish thread, to read the current field values.
   */
  private DataSetSnapshot readDataSet(PublishedDataSetReadContext context) {
    long n = cycle.incrementAndGet();

    double temperature = 20.0 + 5.0 * Math.sin(Math.toRadians(n * 6.0));
    String status = n % 30 < 25 ? "running" : "idle";

    if (n == 1 || n % 10 == 0) {
      logger.info(
          "publish cycle {}: temperature={}, status={}", n, "%.2f".formatted(temperature), status);
    }

    return DataSetSnapshot.builder(context)
        .field("temperature", new DataValue(Variant.ofDouble(temperature)))
        .field("status", new DataValue(Variant.ofString(status)))
        .build();
  }
}
