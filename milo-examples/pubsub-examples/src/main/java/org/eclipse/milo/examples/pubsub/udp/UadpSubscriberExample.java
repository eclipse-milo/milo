/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.pubsub.udp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A standalone OPC UA Part 14 PubSub subscriber: receives UADP NetworkMessages on UDP {@code
 * 127.0.0.1:15100} and decodes the "telemetry" DataSet (temperature, status, counter) published by
 * {@link UadpPublisherExample}.
 *
 * <p>This is one half of the getting-started pair. Start this subscriber in one terminal first,
 * then the publisher in a second terminal.
 *
 * <p>Build once, then run, from the repository root (keep {@code -am} on the compile step only, and
 * don't pass {@code -q} to {@code exec:java} — the example's SLF4J output routes through Maven's
 * logger, so quiet mode would suppress it):
 *
 * <pre>{@code
 * mvn -q -pl milo-examples/pubsub-examples -am compile
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *   -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.UadpSubscriberExample
 * }</pre>
 *
 * <p>Expected output: one startup line, then — once the publisher is running — two "[received]"
 * lines per second, each showing the decoded field names and values. Runs until Ctrl-C.
 */
public class UadpSubscriberExample {

  public static void main(String[] args) throws Exception {
    new UadpSubscriberExample().run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private void run() throws Exception {
    PubSubService service = PubSubService.create(createConfig());

    // DataSets are pushed to listeners as they are received and decoded; there is no polling.
    // This registers globally; listeners can also be registered per reader, or pre-wired with
    // PubSubBindings before startup.
    service.addDataSetListener(this::onDataSet);

    service.startup().get();

    logger.info(
        "UADP subscriber started: listening on opc.udp://127.0.0.1:15100 for dataset"
            + " \"telemetry\" from publisher 1001, Ctrl-C to exit");

    // Run until Ctrl-C: the shutdown hook completes the future and main exits.
    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

    future.get();
  }

  private void onDataSet(DataSetReceivedEvent event) {
    String fields =
        event.fieldsByName().entrySet().stream()
            .map(e -> e.getKey() + "=" + e.getValue().value().value())
            .collect(Collectors.joining(", "));

    logger.info(
        "[received] dataSet={} publisherId={} dataSetWriterId={} fields: {}",
        event.dataSetName(),
        event.publisherId().toCanonicalString(),
        event.dataSetWriterId(),
        fields);
  }

  /**
   * One UDP connection bound to 127.0.0.1:15100 with a single DataSetReader that decodes the
   * publisher's "telemetry" dataset using configured metadata.
   */
  private static PubSubConfig createConfig() {
    // REQUIRE_CONFIGURED means this reader decodes exclusively against this metadata; field
    // names, order, and types must match the publisher's PublishedDataSet declaration.
    DataSetMetaDataConfig telemetryMetaData =
        DataSetMetaDataConfig.builder("telemetry")
            .field("temperature", NodeIds.Double)
            .field("status", NodeIds.Boolean)
            .field("counter", NodeIds.UInt32)
            .build();

    ReaderGroupConfig readerGroup =
        ReaderGroupConfig.builder("demo")
            .dataSetReader(
                DataSetReaderConfig.builder("telemetry-reader")
                    // No .writerGroupId(...) filter: the publisher uses the default UADP
                    // network message mask, which omits the GroupHeader, so the WriterGroupId
                    // is never on the wire and a configured filter would match nothing. The
                    // PublisherId and DataSetWriterId filters below do work because the
                    // default mask includes the PublisherId and PayloadHeader fields.
                    .publisherId(PublisherId.uint16(ushort(1001)))
                    .dataSetWriterId(ushort(1))
                    .dataSetMetaData(telemetryMetaData)
                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                    .build())
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("subscriber-connection")
                .address(UdpDatagramAddress.unicast("127.0.0.1", 15100))
                // Pin the discovery address to a loopback port so nothing in this demo ever
                // binds the Part 14 default opc.udp://224.0.2.14:4840 (well-known port 4840
                // plus a multicast group join). Two processes on one host cannot bind the
                // same discovery port, so the publisher pins 15101 and the subscriber 15102.
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", 15102))
                .readerGroup(readerGroup)
                .build())
        .build();
  }
}
