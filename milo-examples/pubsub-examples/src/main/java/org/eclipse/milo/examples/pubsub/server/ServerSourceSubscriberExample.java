/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetFieldValue;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A standalone PubSub subscriber that receives the DataSets published by {@link
 * ServerSourcePublisherExample} (UADP over UDP unicast).
 *
 * <p>What it demonstrates: a plain {@link PubSubService} — no OPC UA server or client involved —
 * with one DataSetReader that matches the publisher's identity triple (publisherId 4001, writer
 * group "demo" id 1, dataSetWriterId 1) and decodes against <b>configured</b> metadata ({@code
 * MetadataPolicy.REQUIRE_CONFIGURED}): UADP key frames carry values only, so the field names and
 * types come entirely from this side's {@link DataSetMetaDataConfig}, which must match the
 * publisher's PublishedDataSet in name order and type. Received DataSets are delivered push-style
 * to a listener bound via {@link PubSubBindings} — no polling.
 *
 * <p>Terminal order: start {@link ServerSourcePublisherExample} in terminal 1, then this subscriber
 * in terminal 2 (either order works; the subscriber just waits until DataSets arrive).
 *
 * <p>Build the examples module once, then run from the repository root:
 *
 * <pre>{@code
 * mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
 *
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerSourceSubscriberExample
 * }</pre>
 *
 * <p>Optional arguments override the data port (default {@code 15120}) and this side's discovery
 * port (default {@code 15122}), e.g. {@code -Dexec.args="15120 15122"}; the defaults match {@link
 * ServerSourcePublisherExample}.
 *
 * <p>Expected output: a startup line, then one {@code [received]} line roughly every 500 ms until
 * Ctrl-C, e.g.:
 *
 * <pre>
 * [received] dataSet=demo-nodes publisherId=4001 writerGroupId=1 dataSetWriterId=1
 *     Temperature=22.5, Pressure=1015.67, Counter=42
 * </pre>
 */
public class ServerSourceSubscriberExample {

  private static final String DATA_HOST = "127.0.0.1";
  private static final int DEFAULT_DATA_PORT = 15120;
  private static final int DEFAULT_DISCOVERY_PORT = 15122;

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4001));
  private static final UShort WRITER_GROUP_ID = ushort(1);
  private static final UShort DATA_SET_WRITER_ID = ushort(1);

  public static void main(String[] args) throws Exception {
    int dataPort = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_DATA_PORT;
    int discoveryPort = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_DISCOVERY_PORT;

    new ServerSourceSubscriberExample(dataPort, discoveryPort).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final int dataPort;
  private final int discoveryPort;

  public ServerSourceSubscriberExample(int dataPort, int discoveryPort) {
    this.dataPort = dataPort;
    this.discoveryPort = discoveryPort;
  }

  private void run() throws Exception {
    // Configured metadata: names, order, and types must match the publisher's "demo-nodes"
    // PublishedDataSet. With REQUIRE_CONFIGURED, discovered metadata is never applied; this
    // configuration is the single source of truth for decoding.
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("demo-nodes")
            .field("Temperature", NodeIds.Double)
            .field("Pressure", NodeIds.Double)
            .field("Counter", NodeIds.Int32)
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp("sub-conn")
                    // the subscriber binds this address to receive the publisher's datagrams
                    .address(UdpDatagramAddress.unicast(DATA_HOST, dataPort))
                    // Pin the discovery plane to a loopback unicast port; otherwise the engine
                    // binds UDP 4840 and joins multicast group 224.0.2.14. The publisher pins a
                    // different port (15121) because both sides bind a discovery socket on this
                    // host.
                    .discoveryAddress(UdpDatagramAddress.unicast(DATA_HOST, discoveryPort))
                    .readerGroup(
                        ReaderGroupConfig.builder("demo")
                            .dataSetReader(
                                DataSetReaderConfig.builder("reader")
                                    // filtering on writerGroupId works because the publisher's
                                    // UADP mask includes GroupHeader + WriterGroupId
                                    .publisherId(PUBLISHER_ID)
                                    .writerGroupId(WRITER_GROUP_ID)
                                    .dataSetWriterId(DATA_SET_WRITER_ID)
                                    .dataSetMetaData(metaData)
                                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                    .build())
                            .build())
                    .build())
            .build();

    // bind the listener to the reader by name before startup; events are pushed as they arrive
    PubSubBindings bindings =
        PubSubBindings.builder()
            .listener(new DataSetReaderRef("sub-conn", "demo", "reader"), this::onDataSetReceived)
            .build();

    PubSubService service = PubSubService.create(config, bindings);

    service.startup().get();

    logger.info(
        "listening on {}:{} for publisherId={} writerGroupId={} dataSetWriterId={}",
        DATA_HOST,
        dataPort,
        PUBLISHER_ID.toCanonicalString(),
        WRITER_GROUP_ID,
        DATA_SET_WRITER_ID);

    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

    future.get();

    service.shutdown().get(10, TimeUnit.SECONDS);
  }

  private void onDataSetReceived(DataSetReceivedEvent event) {
    String fields =
        event.fields().stream()
            .map(ServerSourceSubscriberExample::formatField)
            .collect(Collectors.joining(", "));

    logger.info(
        "[received] dataSet={} publisherId={} writerGroupId={} dataSetWriterId={} {}",
        event.dataSetName(),
        event.publisherId().toCanonicalString(),
        event.writerGroupId(),
        event.dataSetWriterId(),
        fields);
  }

  private static String formatField(DataSetFieldValue field) {
    DataValue value = field.value();
    String s = field.name() + "=" + value.value().value();
    return value.statusCode().isGood() ? s : s + " [" + value.statusCode() + "]";
  }
}
