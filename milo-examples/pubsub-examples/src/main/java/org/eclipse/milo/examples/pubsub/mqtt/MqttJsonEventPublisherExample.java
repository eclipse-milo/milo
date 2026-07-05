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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.mqtt.MqttTransportProvider;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A standalone PubSub publisher sending JSON {@code ua-event} NetworkMessages through an MQTT
 * broker.
 *
 * <p>What it demonstrates:
 *
 * <ul>
 *   <li>the push-model event source: a {@link PublishedEventsConfig} dataset selects fields of a
 *       {@code BaseEventType} event with {@link SimpleAttributeOperand}s, and {@link
 *       PubSubService#publishEvent} pushes one event's field values (as {@link Variant}s, in
 *       dataset order) each time it is called — no address space or {@link
 *       org.eclipse.milo.opcua.sdk.server server} is involved, so the field values here are
 *       synthesized directly.
 *   <li>an event-expressible JSON writer group: the effective masks carry the {@code
 *       DataSetMessageHeader} (network level) and {@code MessageType} member (DataSetMessage
 *       level), which Part 14 Annex A.3.3.4 requires for events — without them startup fails with
 *       {@code Bad_ConfigurationError}. On the wire each event is a DataSetMessage with {@code
 *       MessageType: "ua-event"}.
 *   <li>each pushed event becomes its OWN JSON NetworkMessage (Part 14 §6.2.6.2 per-event
 *       partition), drained on the writer group's next publishing cycle.
 * </ul>
 *
 * <p>The PublisherId is UInt16 3010, so events are published to the derived data topic {@code
 * opcua/json/data/3010/events} and the retained metadata document to {@code
 * opcua/json/metadata/3010/events/writer}.
 *
 * <p>Terminal order:
 *
 * <ol>
 *   <li>Terminal 1: an MQTT broker listening at {@code mqtt://127.0.0.1:1883} — for example {@code
 *       EmbeddedBrokerExample} from this package, or an external broker such as Mosquitto.
 *   <li>Terminal 2: this publisher.
 * </ol>
 *
 * <p>Run from the repository root:
 *
 * <pre>{@code
 * mvn -pl milo-examples/pubsub-examples -am compile
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.MqttJsonEventPublisherExample
 * }</pre>
 *
 * <p>An alternative broker URI may be passed as the first argument, e.g. {@code
 * -Dexec.args="mqtt://10.0.0.5:1883"}. Do not add {@code -q}: {@code exec:java} runs in-process,
 * and Maven's quiet flag also silences the example's slf4j-simple logging.
 *
 * <p>Expected output: a startup line naming the broker URI and data topic, then one "published
 * event #N: ..." line per second (one event pushed per second). Runs until Ctrl-C.
 */
public class MqttJsonEventPublisherExample {

  private static final String DEFAULT_BROKER_URI = "mqtt://127.0.0.1:1883";

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(3010));

  private static final Duration PUBLISH_INTERVAL = Duration.ofSeconds(1);

  public static void main(String[] args) throws Exception {
    URI brokerUri = URI.create(args.length > 0 ? args[0] : DEFAULT_BROKER_URI);

    new MqttJsonEventPublisherExample().run(brokerUri);
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicLong eventCount = new AtomicLong();

  private void run(URI brokerUri) throws Exception {
    // An event-source dataset: each field selects a standard BaseEventType field by browse name.
    // Field order is wire order and is the order publishEvent expects its Variants in. The
    // eventNotifier is metadata only on this standalone push path (no server bus is consulted); it
    // is authored in canonical ns0 form.
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("demo-events")
            .source(
                PublishedEventsConfig.builder(NodeIds.Server.expanded())
                    .field(eventField("sourceName", "SourceName", NodeIds.String))
                    .field(eventField("severity", "Severity", NodeIds.UInt16))
                    .field(eventField("message", "Message", NodeIds.LocalizedText))
                    .build())
            .build();
    PublishedDataSetRef eventRef = dataSet.ref();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.mqtt("connection")
                    .brokerUri(brokerUri)
                    // a UInt16 id; JSON puts its canonical string form "3010" on the wire
                    .publisherId(PUBLISHER_ID)
                    .writerGroup(
                        WriterGroupConfig.builder("events")
                            .writerGroupId(ushort(1))
                            .publishingInterval(PUBLISH_INTERVAL)
                            // events require the DataSetMessageHeader at the network level and the
                            // MessageType member at the DataSetMessage level (Part 14 A.3.3.4)
                            .messageSettings(
                                JsonWriterGroupSettings.builder()
                                    .networkMessageContentMask(
                                        JsonNetworkMessageContentMask.of(
                                            JsonNetworkMessageContentMask.Field
                                                .NetworkMessageHeader,
                                            JsonNetworkMessageContentMask.Field
                                                .DataSetMessageHeader,
                                            JsonNetworkMessageContentMask.Field.PublisherId))
                                    .build())
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(eventRef)
                                    .dataSetWriterId(ushort(1))
                                    .settings(
                                        JsonDataSetWriterSettings.builder()
                                            .dataSetMessageContentMask(
                                                JsonDataSetMessageContentMask.of(
                                                    JsonDataSetMessageContentMask.Field
                                                        .DataSetWriterId,
                                                    JsonDataSetMessageContentMask.Field
                                                        .SequenceNumber,
                                                    JsonDataSetMessageContentMask.Field.MessageType,
                                                    JsonDataSetMessageContentMask.Field.Timestamp,
                                                    JsonDataSetMessageContentMask.Field.Status))
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    // Event datasets need no PublishedDataSetSource: values arrive through publishEvent, not a
    // per-cycle pull. No queueName is configured, so the engine derives the Part 14 topic tree:
    //   data:     opcua/json/data/3010/events            (QoS 0, not retained)
    //   metadata: opcua/json/metadata/3010/events/writer (QoS 1, retained, sent automatically)
    PubSubService service =
        PubSubService.create(
            config,
            PubSubBindings.builder().build(),
            PubSubServiceConfig.builder()
                .transportProvider(MqttTransportProvider.create())
                .build());

    service.startup().get();

    logger.info(
        "publisher started: broker={}, publishing 'ua-event' to topic opcua/json/data/3010/events"
            + " every {} ms",
        brokerUri,
        PUBLISH_INTERVAL.toMillis());

    ScheduledExecutorService publisher =
        Executors.newSingleThreadScheduledExecutor(
            r -> {
              Thread thread = new Thread(r, "example-event-publisher");
              thread.setDaemon(true);
              return thread;
            });

    publisher.scheduleAtFixedRate(
        () -> publishEvent(service, eventRef),
        PUBLISH_INTERVAL.toMillis(),
        PUBLISH_INTERVAL.toMillis(),
        TimeUnit.MILLISECONDS);

    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

    future.get();

    publisher.shutdownNow();
    service.close();
  }

  /**
   * Push one synthetic {@code BaseEventType} event: the Variants are supplied in the dataset's
   * field order (sourceName, severity, message). The next publishing cycle drains it as its own
   * {@code ua-event} NetworkMessage.
   */
  private void publishEvent(PubSubService service, PublishedDataSetRef eventRef) {
    long n = eventCount.incrementAndGet();

    String sourceName = "demo-source";
    UShort severity = ushort(100 + (int) (n % 9) * 100);
    String message = "synthetic event #" + n;

    service.publishEvent(
        eventRef,
        List.of(
            Variant.ofString(sourceName),
            Variant.ofUInt16(severity),
            new Variant(LocalizedText.english(message))));

    logger.info(
        "published event #{}: sourceName={}, severity={}, message={}",
        n,
        sourceName,
        severity,
        message);
  }

  /**
   * An event field selecting the BaseEventType field {@code browseName} under the name {@code
   * name}.
   */
  private static EventFieldDefinition eventField(String name, String browseName, NodeId dataType) {
    return EventFieldDefinition.builder(name)
        .selectedField(
            new SimpleAttributeOperand(
                NodeIds.BaseEventType,
                new QualifiedName[] {new QualifiedName(0, browseName)},
                AttributeId.Value.uid(),
                null))
        .dataType(dataType)
        .build();
  }
}
