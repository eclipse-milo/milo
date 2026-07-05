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
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublisherStatusReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.mqtt.MqttTransportProvider;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A standalone PubSub subscriber receiving JSON {@code ua-data} NetworkMessages from an MQTT
 * broker, with field definitions discovered from the broker's retained metadata.
 *
 * <p>What it demonstrates — broker metadata and status paths:
 *
 * <ul>
 *   <li>registering the {@link MqttTransportProvider} explicitly via {@link
 *       PubSubServiceConfig.Builder#transportProvider} — there is no ServiceLoader auto-discovery.
 *   <li>a DataSetReader with <b>no configured field list</b>: instead of {@code dataSetMetaData} it
 *       uses {@code MetadataPolicy.ACCEPT_DISCOVERED} plus a {@code BrokerTransportSettings} with
 *       both the data {@code queueName} and the {@code metaDataQueueName}. The retained {@code
 *       ua-metadata} document the publisher left at the broker supplies the dataset name and field
 *       definitions through the normal decode path. This is the MQTT substitute for the {@code
 *       REQUEST_IF_MISSING} discovery probing used on UDP connections: there is nothing to probe
 *       for because the broker retains the metadata.
 *   <li>broker readers must always configure the data {@code queueName} explicitly — they cannot
 *       derive the publisher's topic (it embeds the publisher's WriterGroup name).
 *   <li>cross-type PublisherId matching: the reader filters on UInt16 3001 while the JSON wire
 *       carries the string {@code "3001"}; ids match by canonical string form.
 *   <li>logging metadata arrival via {@code addMetaDataListener}, publisher health through {@code
 *       addPublisherStatusListener}, and received DataSets via a pre-wired per-reader listener.
 * </ul>
 *
 * <p>Terminal order:
 *
 * <ol>
 *   <li>Terminal 1: an MQTT broker listening at {@code mqtt://127.0.0.1:1883} — for example {@code
 *       EmbeddedBrokerExample} from this package, or an external broker such as Mosquitto.
 *   <li>Terminal 2: {@code MqttJsonPublisherExample}.
 *   <li>Terminal 3: this subscriber. (Terminals 2 and 3 may be started in either order: the broker
 *       retains the metadata and status documents.)
 * </ol>
 *
 * <p>Run from the repository root:
 *
 * <pre>{@code
 * mvn -pl milo-examples/pubsub-examples -am compile
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttJsonSubscriberExample
 * }</pre>
 *
 * <p>An alternative broker URI may be passed as the first argument, e.g. {@code
 * -Dexec.args="mqtt://10.0.0.5:1883"}. Do not add {@code -q}: {@code exec:java} runs in-process,
 * and Maven's quiet flag also silences the example's slf4j-simple logging.
 *
 * <p>Expected output: a startup line, one "metadata received: ..." line when the retained {@code
 * ua-metadata} document is replayed, one "publisher status: ..." line when the retained {@code
 * ua-status} document is replayed, then one "[received] ..." line per second naming the
 * "temperature" and "status" fields with their current values — field names this reader never
 * configured, proving the discovered metadata was applied. Runs until Ctrl-C.
 */
public class MqttJsonSubscriberExample {

  private static final String DEFAULT_BROKER_URI = "mqtt://127.0.0.1:1883";

  // The publisher's derived Part 14 topics. These must match MqttJsonPublisherExample
  // (PublisherId 3001, writer group "demo", writer "writer").
  private static final String DATA_TOPIC = "opcua/json/data/3001/demo";
  private static final String META_TOPIC = "opcua/json/metadata/3001/demo/writer";
  private static final String STATUS_TOPIC = "opcua/json/status/3001";

  public static void main(String[] args) throws Exception {
    URI brokerUri = URI.create(args.length > 0 ? args[0] : DEFAULT_BROKER_URI);

    new MqttJsonSubscriberExample().run(brokerUri);
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private void run(URI brokerUri) throws Exception {
    DataSetReaderConfig reader =
        DataSetReaderConfig.builder("reader")
            // UInt16 filter matched against the JSON wire string "3001" by canonical form
            .publisherId(PublisherId.uint16(ushort(3001)))
            // no writerGroupId filter: the default JSON NetworkMessage content mask does not
            // put the WriterGroupId on the wire
            .dataSetWriterId(ushort(1))
            .settings(JsonDataSetReaderSettings.builder().build())
            // no dataSetMetaData configured: field definitions come from the retained
            // ua-metadata document replayed from the metadata queue below
            .metadataPolicy(MetadataPolicy.ACCEPT_DISCOVERED)
            .brokerTransport(
                BrokerTransportSettings.builder()
                    .queueName(DATA_TOPIC)
                    .metaDataQueueName(META_TOPIC)
                    .build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt("connection")
                    .brokerUri(brokerUri)
                    .readerGroup(ReaderGroupConfig.builder("demo").dataSetReader(reader).build())
                    .build())
            .build();

    PubSubService service =
        PubSubService.create(
            config,
            PubSubBindings.builder()
                .listener(new DataSetReaderRef("connection", "demo", "reader"), this::onDataSet)
                .build(),
            PubSubServiceConfig.builder()
                .transportProvider(MqttTransportProvider.create())
                .build());

    service.addMetaDataListener(this::onMetaData);
    service.addPublisherStatusListener(this::onPublisherStatus);

    service.startup().get();

    logger.info(
        "subscriber started: broker={}, data topic={}, metadata topic={}, status topic={}",
        brokerUri,
        DATA_TOPIC,
        META_TOPIC,
        STATUS_TOPIC);

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
   * Called when a DataSetMetaData document is received; with {@code ACCEPT_DISCOVERED} it is also
   * applied as the reader's effective decode metadata.
   */
  private void onMetaData(MetaDataReceivedEvent event) {
    FieldMetaData[] fields = event.metaData().getFields();

    String fieldNames =
        fields == null
            ? "[]"
            : Arrays.stream(fields)
                .map(FieldMetaData::getName)
                .collect(Collectors.joining(", ", "[", "]"));

    logger.info(
        "metadata received: dataSet={} version={}.{} fields={}",
        event.dataSetName(),
        event.configurationVersion().getMajorVersion(),
        event.configurationVersion().getMinorVersion(),
        fieldNames);
  }

  /**
   * Called for retained or live remote-publisher status messages. Status events report publisher
   * health without changing this subscriber connection or reader state.
   */
  private void onPublisherStatus(PublisherStatusReceivedEvent event) {
    logger.info(
        "publisher status: publisherId={} status={} timeout={} topic={} correlatedReaders={}",
        event.publisherId().toCanonicalString(),
        event.status(),
        event.timeout(),
        event.topic(),
        event.readers().size());
  }

  /**
   * Called for each decoded DataSet. The field names logged here were never configured locally;
   * they come from the discovered metadata.
   */
  private void onDataSet(DataSetReceivedEvent event) {
    String fields =
        event.fieldsByName().entrySet().stream()
            .map(e -> e.getKey() + "=" + e.getValue().value().value())
            .collect(Collectors.joining(", "));

    logger.info(
        "[received] publisherId={} dataSet={} {}",
        event.publisherId().toCanonicalString(),
        event.dataSetName(),
        fields);
  }
}
