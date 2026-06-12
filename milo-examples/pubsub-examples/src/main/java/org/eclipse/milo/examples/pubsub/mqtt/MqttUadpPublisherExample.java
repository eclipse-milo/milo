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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.mqtt.MqttTransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerTopics;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A standalone OPC UA PubSub publisher sending UADP NetworkMessages through an MQTT broker.
 *
 * <p>What it demonstrates:
 *
 * <ul>
 *   <li>registering the MQTT transport with the engine via {@link PubSubServiceConfig} (transports
 *       are an explicit registry; MQTT is not auto-discovered).
 *   <li>selecting the "uadp" message mapping over the broker with explicit {@link
 *       UadpWriterGroupSettings} and {@link UadpDataSetWriterSettings}.
 *   <li>a {@link PublishedDataSetSource} producing changing telemetry values every publish cycle.
 *   <li>the Part 14 §7.3.4.7 derived data topic, {@code opcua/uadp/data/2001/demo}, that
 *       subscribers must be configured with.
 * </ul>
 *
 * <p>Start order, one terminal each:
 *
 * <ol>
 *   <li>{@link EmbeddedBrokerExample} (or any MQTT broker listening on {@code
 *       mqtt://127.0.0.1:1883}).
 *   <li>this publisher.
 *   <li>{@link MqttUadpSubscriberExample}.
 * </ol>
 *
 * <p>Build once, then run from the repository root:
 *
 * <pre>{@code
 * mvn -q -pl milo-examples/pubsub-examples -am compile
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttUadpPublisherExample
 * }</pre>
 *
 * <p>(Don't pass {@code -q} to {@code exec:java}: under Maven the example's SLF4J output is routed
 * through Maven's logger, and {@code -q} silences it.)
 *
 * <p>An optional first argument overrides the broker URI, e.g. {@code
 * -Dexec.args="mqtt://10.0.0.5:1883"}; with no arguments it connects to {@code
 * mqtt://127.0.0.1:1883}.
 *
 * <p>Expected output: a startup line naming the broker URI and the derived data topic, then a
 * "publish cycle" line every 10th cycle (roughly every 5 seconds). Runs until Ctrl-C.
 */
public class MqttUadpPublisherExample {

  private static final String DEFAULT_BROKER_URI = "mqtt://127.0.0.1:1883";

  static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(2001));

  public static void main(String[] args) throws Exception {
    URI brokerUri = URI.create(args.length > 0 ? args[0] : DEFAULT_BROKER_URI);

    new MqttUadpPublisherExample(brokerUri).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicLong cycle = new AtomicLong();

  private final URI brokerUri;

  public MqttUadpPublisherExample(URI brokerUri) {
    this.brokerUri = brokerUri;
  }

  public void run() throws Exception {
    // The dataset "telemetry": field order is wire order. The configuration version is carried
    // in every DataSetMessage (the writer mask below includes MajorVersion|MinorVersion) and
    // checked by REQUIRE_CONFIGURED subscribers, so it must match what they configure.
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("telemetry")
            .field(FieldDefinition.builder("temperature").dataType(NodeIds.Double).build())
            .field(FieldDefinition.builder("counter").dataType(NodeIds.Int32).build())
            .field(FieldDefinition.builder("status").dataType(NodeIds.String).build())
            .configurationVersion(uint(1), uint(0))
            .build();

    // The settings TYPE selects the message mapping used over the broker: Uadp* settings select
    // the "uadp" mapping (binary NetworkMessages and the opcua/uadp/... topic tree); Json*
    // settings would select "json". GroupHeader|WriterGroupId let subscribers filter on
    // writerGroupId; MajorVersion|MinorVersion enable the subscriber-side version check.
    UadpWriterGroupSettings groupSettings =
        UadpWriterGroupSettings.builder()
            .networkMessageContentMask(
                UadpNetworkMessageContentMask.of(
                    UadpNetworkMessageContentMask.Field.PublisherId,
                    UadpNetworkMessageContentMask.Field.GroupHeader,
                    UadpNetworkMessageContentMask.Field.WriterGroupId,
                    UadpNetworkMessageContentMask.Field.SequenceNumber,
                    UadpNetworkMessageContentMask.Field.PayloadHeader))
            .build();

    UadpDataSetWriterSettings writerSettings =
        UadpDataSetWriterSettings.builder()
            .dataSetMessageContentMask(
                UadpDataSetMessageContentMask.of(
                    UadpDataSetMessageContentMask.Field.Timestamp,
                    UadpDataSetMessageContentMask.Field.Status,
                    UadpDataSetMessageContentMask.Field.MajorVersion,
                    UadpDataSetMessageContentMask.Field.MinorVersion,
                    UadpDataSetMessageContentMask.Field.SequenceNumber))
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.mqtt("pub-conn")
                    .brokerUri(brokerUri)
                    .publisherId(PUBLISHER_ID)
                    .writerGroup(
                        WriterGroupConfig.builder("demo")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(500))
                            .messageSettings(groupSettings)
                            .dataSetWriter(
                                DataSetWriterConfig.builder("telemetry-writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .settings(writerSettings)
                                    .build())
                            .build())
                    .build())
            .build();

    // MQTT is NOT auto-discovered: registering MqttTransportProvider on the service config is
    // the one line that wires the MQTT transport in. Without it, startup fails with
    // Bad_ConfigurationError ("no TransportProvider").
    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder().transportProvider(MqttTransportProvider.create()).build();

    PubSubBindings bindings =
        PubSubBindings.builder().source(dataSet.ref(), telemetrySource()).build();

    PubSubService service = PubSubService.create(config, bindings, serviceConfig);

    logger.info(
        "connecting to MQTT broker at {} (start EmbeddedBrokerExample or another broker first)...",
        brokerUri);

    service.startup().get();

    // The engine derives the data topic per Part 14 §7.3.4.7:
    // <prefix>/<mapping>/data/<publisherId>/<writerGroupName> = opcua/uadp/data/2001/demo
    String dataTopic =
        BrokerTopics.dataTopic(BrokerTopics.DEFAULT_TOPIC_PREFIX, "uadp", PUBLISHER_ID, "demo");
    logger.info("publishing dataset \"telemetry\" every 500ms on topic {}", dataTopic);

    // run until Ctrl-C; the shutdown hook stops the service, then completes the future
    final CompletableFuture<Void> shutdownComplete = new CompletableFuture<>();

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  try {
                    service.shutdown().get(10, TimeUnit.SECONDS);
                  } catch (Exception e) {
                    // best-effort cleanup on Ctrl-C
                  }
                  shutdownComplete.complete(null);
                }));

    shutdownComplete.get();
  }

  /**
   * A {@link PublishedDataSetSource} invoked by the engine once per publish cycle (the pull model).
   * It produces changing values and logs every 10th cycle so the console stays calm.
   */
  private PublishedDataSetSource telemetrySource() {
    return context -> {
      long n = cycle.incrementAndGet();

      double temperature = 20.0 + 5.0 * Math.sin(n / 10.0);
      int counter = (int) n;
      String status = "running";

      if (n == 1L || n % 10 == 0) {
        logger.info(
            "publish cycle {}: temperature={}, counter={}, status={}",
            n,
            "%.2f".formatted(temperature),
            counter,
            status);
      }

      return DataSetSnapshot.builder(context)
          .field("temperature", new DataValue(Variant.ofDouble(temperature)))
          .field("counter", new DataValue(Variant.ofInt32(counter)))
          .field("status", new DataValue(Variant.ofString(status)))
          .build();
    };
  }
}
