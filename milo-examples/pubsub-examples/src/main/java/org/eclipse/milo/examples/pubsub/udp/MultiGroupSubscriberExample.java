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
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
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
 * A standalone OPC UA Part 14 PubSub subscriber demonstrating a <em>structured</em> configuration:
 * one UDP connection with a single ReaderGroup hosting <em>three</em> DataSetReaders, one per
 * writer published by {@link MultiGroupPublisherExample} — "motion" and "vibration" (250ms, packed
 * together in one NetworkMessage per cycle) and "status" (2000ms) — received on {@code
 * 127.0.0.1:15150}.
 *
 * <p>What it demonstrates beyond the getting-started pair: how readers fan a structured publisher
 * back out. Each reader matches by PublisherId + DataSetWriterId (see {@link #createConfig()} for
 * why that suffices and why no WriterGroupId filter is set — under the default masks one would be
 * silently ignored, not enforced), and each reader has its own listener, so the three streams
 * arrive in application code already separated.
 *
 * <p>This is one half of the multi-group pair. Start this subscriber in one terminal first, then
 * the publisher in a second terminal.
 *
 * <p>Build once, then run, from the repository root (keep {@code -am} on the compile step only, and
 * don't pass {@code -q} to {@code exec:java} — the example's SLF4J output routes through Maven's
 * logger, so quiet mode would suppress it):
 *
 * <pre>{@code
 * mvn -q -pl milo-examples/pubsub-examples -am compile
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *   -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.MultiGroupSubscriberExample
 * }</pre>
 *
 * <p>Expected output: one startup line, then — once the publisher is running — four "[motion]" and
 * four "[vibration]" lines per second (the fast group's 250ms cycles) and one "[status]" line every
 * two seconds (the slow group), each showing the decoded field values. Runs until Ctrl-C.
 */
public class MultiGroupSubscriberExample {

  /** The PublisherId {@link MultiGroupPublisherExample} publishes under. */
  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(7001));

  public static void main(String[] args) throws Exception {
    new MultiGroupSubscriberExample().run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private void run() throws Exception {
    PubSubService service = PubSubService.create(createConfig());

    // Listeners registered per reader: each reader's decoded DataSets are delivered only to
    // its own listener, so the three streams stay separated without any application-side
    // switching on writer id. Registering before startup means no event is missed.
    service.addDataSetListener(
        new DataSetReaderRef("subscriber-connection", "readers", "motion-reader"),
        event -> logDataSet("[motion]", event));
    service.addDataSetListener(
        new DataSetReaderRef("subscriber-connection", "readers", "vibration-reader"),
        event -> logDataSet("[vibration]", event));
    service.addDataSetListener(
        new DataSetReaderRef("subscriber-connection", "readers", "status-reader"),
        event -> logDataSet("[status]", event));

    service.startup().get();

    logger.info(
        "multi-group subscriber started: listening on opc.udp://127.0.0.1:15150 for datasets"
            + " \"motion\", \"vibration\", and \"status\" from publisher 7001, Ctrl-C to exit");

    // Run until Ctrl-C: the shutdown hook completes the future and main exits.
    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

    future.get();
  }

  private void logDataSet(String marker, DataSetReceivedEvent event) {
    String fields =
        event.fields().stream()
            // DataSetFieldValue -> DataValue -> Variant -> plain Java value
            .map(field -> field.name() + "=" + field.value().value().value())
            .collect(Collectors.joining(", "));

    logger.info("{} {}", marker, fields);
  }

  /**
   * One UDP connection bound to 127.0.0.1:15150 with a single ReaderGroup hosting three
   * DataSetReaders, one per publisher-side writer, each decoding against configured metadata.
   */
  private static PubSubConfig createConfig() {
    // REQUIRE_CONFIGURED means each reader decodes exclusively against its configured
    // metadata; field names, order, and types must match the publisher's PublishedDataSet
    // declarations.
    DataSetMetaDataConfig motionMetaData =
        DataSetMetaDataConfig.builder("motion")
            .field("position", NodeIds.Double)
            .field("velocity", NodeIds.Double)
            .build();

    DataSetMetaDataConfig vibrationMetaData =
        DataSetMetaDataConfig.builder("vibration")
            .field("rms", NodeIds.Double)
            .field("peak", NodeIds.Double)
            .build();

    DataSetMetaDataConfig statusMetaData =
        DataSetMetaDataConfig.builder("status")
            .field("uptimeSeconds", NodeIds.Int64)
            .field("state", NodeIds.String)
            .build();

    // Three readers in ONE reader group, matched to writers by PublisherId + DataSetWriterId:
    //
    //  - No .writerGroupId(...) filter is set on any reader. The publisher uses the default
    //    UADP masks, which omit the GroupHeader, so the WriterGroupId is never on the wire —
    //    and an identifier filter is applied only when the identifier is present in the
    //    message, so a configured filter would be silently ignored rather than enforced. It
    //    is left unset to avoid implying group isolation that does not exist; the publisher's
    //    "fast"/"slow" group structure is invisible here.
    //
    //  - DataSetWriterId alone disambiguates because the default masks DO include the
    //    PayloadHeader, which lists every DataSetMessage's DataSetWriterId, and the publisher
    //    assigned ids unique across both of its groups (1, 2, 3). "motion" and "vibration"
    //    arrive packed in a single NetworkMessage per fast cycle; the writer ids in the
    //    PayloadHeader are what route each DataSetMessage to its reader.
    ReaderGroupConfig readerGroup =
        ReaderGroupConfig.builder("readers")
            .dataSetReader(reader("motion-reader", 1, motionMetaData))
            .dataSetReader(reader("vibration-reader", 2, vibrationMetaData))
            .dataSetReader(reader("status-reader", 3, statusMetaData))
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("subscriber-connection")
                .address(UdpDatagramAddress.unicast("127.0.0.1", 15150))
                // All three readers share this connection's single subscriber channel —
                // reader groups own no sockets. Pin the discovery address to a loopback
                // port so nothing in this demo ever binds the Part 14 default
                // opc.udp://224.0.2.14:4840 (well-known port 4840 plus a multicast group
                // join). Two processes on one host cannot bind the same discovery port, so
                // the publisher pins 15151 and the subscriber 15152.
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", 15152))
                .readerGroup(readerGroup)
                .build())
        .build();
  }

  private static DataSetReaderConfig reader(
      String name, int dataSetWriterId, DataSetMetaDataConfig metaData) {
    return DataSetReaderConfig.builder(name)
        .publisherId(PUBLISHER_ID)
        .dataSetWriterId(ushort(dataSetWriterId))
        .dataSetMetaData(metaData)
        .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
        .build();
  }
}
