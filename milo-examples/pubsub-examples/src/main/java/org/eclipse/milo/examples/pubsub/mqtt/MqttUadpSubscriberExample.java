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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.mqtt.MqttTransportProvider;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A standalone OPC UA PubSub subscriber receiving UADP NetworkMessages through an MQTT broker.
 *
 * <p>What it demonstrates:
 *
 * <ul>
 *   <li>registering the MQTT transport with the engine via {@link PubSubServiceConfig} (transports
 *       are an explicit registry; MQTT is not auto-discovered).
 *   <li>configuring the broker data topic on a reader with {@link BrokerTransportSettings}:
 *       subscriber-side configs carry no writer group, so the engine cannot derive the publisher's
 *       topic — it must be configured.
 *   <li>decoding against locally configured metadata with {@link
 *       MetadataPolicy#REQUIRE_CONFIGURED}, including the configuration version check.
 *   <li>push-style delivery of decoded DataSets to a listener bound via {@link PubSubBindings}.
 * </ul>
 *
 * <p>Start order, one terminal each:
 *
 * <ol>
 *   <li>{@link EmbeddedBrokerExample} (or any MQTT broker listening on {@code
 *       mqtt://127.0.0.1:1883}).
 *   <li>{@link MqttUadpPublisherExample}.
 *   <li>this subscriber.
 * </ol>
 *
 * <p>Build once, then run from the repository root:
 *
 * <pre>{@code
 * mvn -q -pl milo-examples/pubsub-examples -am compile
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttUadpSubscriberExample
 * }</pre>
 *
 * <p>(Don't pass {@code -q} to {@code exec:java}: under Maven the example's SLF4J output is routed
 * through Maven's logger, and {@code -q} silences it.)
 *
 * <p>An optional first argument overrides the broker URI, e.g. {@code
 * -Dexec.args="mqtt://10.0.0.5:1883"}; with no arguments it connects to {@code
 * mqtt://127.0.0.1:1883}.
 *
 * <p>Expected output: a startup line naming the broker URI and the subscribed topic, then one
 * "[received]" line per DataSet (about twice per second while the publisher runs) with the field
 * names and values. Runs until Ctrl-C.
 */
public class MqttUadpSubscriberExample {

  private static final String DEFAULT_BROKER_URI = "mqtt://127.0.0.1:1883";

  /**
   * The publisher's data topic, derived per the Part 14 §7.3.4.7 topic tree (see {@code
   * BrokerTopics}): {@code <prefix>/<mapping>/data/<publisherId>/<writerGroupName>} with the
   * default prefix "opcua", the "uadp" mapping, publisher id 2001, and writer group "demo". A
   * reader cannot derive this — the subscriber config knows nothing about the publisher's writer
   * group — so the exact topic is configured below via {@link
   * BrokerTransportSettings.Builder#queueName(String)}.
   */
  static final String DATA_TOPIC = "opcua/uadp/data/2001/demo";

  public static void main(String[] args) throws Exception {
    URI brokerUri = URI.create(args.length > 0 ? args[0] : DEFAULT_BROKER_URI);

    new MqttUadpSubscriberExample(brokerUri).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final URI brokerUri;

  public MqttUadpSubscriberExample(URI brokerUri) {
    this.brokerUri = brokerUri;
  }

  public void run() throws Exception {
    // REQUIRE_CONFIGURED decodes only against this locally configured metadata: field order is
    // wire order, and the configuration version must match the version the publisher stamps
    // into each DataSetMessage (MqttUadpPublisherExample publishes version 1.0).
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("telemetry")
            .field("temperature", NodeIds.Double)
            .field("counter", NodeIds.Int32)
            .field("status", NodeIds.String)
            .configurationVersion(uint(1), uint(0))
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                // no publisherId here: this connection only subscribes
                PubSubConnectionConfig.mqtt("sub-conn")
                    .brokerUri(brokerUri)
                    .readerGroup(
                        ReaderGroupConfig.builder("readers")
                            .dataSetReader(
                                // the reader's default message settings are UADP, matching the
                                // publisher's explicit Uadp* settings and selecting the "uadp"
                                // mapping for decoding
                                DataSetReaderConfig.builder("telemetry-reader")
                                    .publisherId(PublisherId.uint16(ushort(2001)))
                                    .writerGroupId(ushort(1))
                                    .dataSetWriterId(ushort(1))
                                    .dataSetMetaData(metaData)
                                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                    // broker readers MUST configure the data topic; startup
                                    // fails with Bad_ConfigurationError without a queueName
                                    .brokerTransport(
                                        BrokerTransportSettings.builder()
                                            .queueName(DATA_TOPIC)
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    // decoded DataSets are pushed to listeners; bind one to this reader by name
    PubSubBindings bindings =
        PubSubBindings.builder()
            .listener(
                new DataSetReaderRef("sub-conn", "readers", "telemetry-reader"), this::onDataSet)
            .build();

    // MQTT is NOT auto-discovered: registering MqttTransportProvider on the service config is
    // the one line that wires the MQTT transport in. Without it, startup fails with
    // Bad_ConfigurationError ("no TransportProvider").
    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder().transportProvider(MqttTransportProvider.create()).build();

    PubSubService service = PubSubService.create(config, bindings, serviceConfig);

    logger.info(
        "connecting to MQTT broker at {} (start EmbeddedBrokerExample or another broker first)...",
        brokerUri);

    service.startup().get();

    logger.info("subscribed to topic {}, waiting for DataSets...", DATA_TOPIC);

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

  private void onDataSet(DataSetReceivedEvent event) {
    String fields =
        event.fields().stream()
            .map(field -> field.name() + "=" + field.value().value().value())
            .collect(Collectors.joining(", "));

    logger.info(
        "[received] dataSet={} publisherId={} writerGroupId={} dataSetWriterId={} fields: {}",
        event.dataSetName(),
        event.publisherId().toCanonicalString(),
        event.writerGroupId(),
        event.dataSetWriterId(),
        fields);
  }
}
