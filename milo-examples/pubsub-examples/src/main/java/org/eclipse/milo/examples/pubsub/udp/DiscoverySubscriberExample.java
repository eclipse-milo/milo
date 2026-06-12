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

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A standalone OPC UA Part 14 subscriber that joins a UDP multicast group and obtains the
 * DataSetMetaData it needs for decoding via discovery, instead of configuration.
 *
 * <p>What it demonstrates: UADP subscribing over a UDP <em>multicast</em> data plane, and the
 * {@link MetadataPolicy#REQUEST_IF_MISSING} discovery loop. The reader configures <em>no</em>
 * DataSetMetaData: it probes the shared discovery address, {@link DiscoveryPublisherExample}'s
 * responder answers with an announcement, and the announced metadata is applied to decoding. The
 * field names in the "[received]" output are the proof — without discovery the engine could only
 * label fields positionally ({@code Field_0}, {@code Field_1}, ...).
 *
 * <p>Terminal order: start {@link DiscoveryPublisherExample} first (terminal 1), then this example
 * (terminal 2). Order is not critical — probes are retried — but starting the publisher first makes
 * the demo immediate.
 *
 * <p>Run from the repository root, after building once with {@code mvn -q -DskipTests -pl
 * milo-examples/pubsub-examples -am install}:
 *
 * <pre>{@code
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.DiscoverySubscriberExample
 * }</pre>
 *
 * <p>Expected output: a startup line, then a "discovery delivered metadata ..." line naming the
 * publisher's fields (possibly twice: the responder announces on both the discovery and the data
 * address), then one "[received] ..." line per second with named field values, e.g.:
 *
 * <pre>
 * [received] dataSet=telemetry temperature=22.4, humidity=49.1, status=OK
 * </pre>
 *
 * <p>The first probe is randomly delayed 100-500 ms and retried with doubling intervals (Part 14
 * §7.2.4.6.12.2), so the first named DataSet can take a few seconds to appear; if a data message
 * beats the announcement, early "[received]" lines may carry positional {@code Field_N} names until
 * the metadata is applied. Runs until Ctrl-C.
 *
 * <p>Defaults match the publisher: data plane multicast group {@code opc.udp://239.255.20.1:15110}
 * and discovery group {@code opc.udp://239.255.20.2:15111}, both joined on the loopback interface
 * only. Pass a different interface address as the only argument (e.g. {@code
 * -Dexec.args="192.0.2.10"}) to subscribe on a real network instead.
 */
public class DiscoverySubscriberExample {

  private static final String DATA_GROUP = "239.255.20.1";
  private static final int DATA_PORT = 15110;

  private static final String DISCOVERY_GROUP = "239.255.20.2";
  private static final int DISCOVERY_PORT = 15111;

  private static final String DEFAULT_NETWORK_INTERFACE = "127.0.0.1";

  /** The PublisherId {@link DiscoveryPublisherExample} publishes under. */
  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(1002));

  public static void main(String[] args) throws Exception {
    String networkInterface = args.length > 0 ? args[0] : DEFAULT_NETWORK_INTERFACE;

    new DiscoverySubscriberExample(networkInterface).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String networkInterface;

  public DiscoverySubscriberExample(String networkInterface) {
    this.networkInterface = networkInterface;
  }

  public void run() throws Exception {
    // No dataSetMetaData is configured: REQUEST_IF_MISSING makes the reader probe the
    // discovery address for it. The first probe is randomly delayed 100-500 ms and retried
    // with doubling intervals, so metadata (and with it the first fully-named DataSet) can
    // take a few seconds to arrive.
    //
    // No writerGroupId filter is set: the publisher uses the default UADP-Dynamic masks,
    // which omit the GroupHeader (and with it the WriterGroupId) from the wire, so filtering
    // on it would discard every message.
    DataSetReaderConfig reader =
        DataSetReaderConfig.builder("demo-reader")
            .publisherId(PUBLISHER_ID)
            .dataSetWriterId(ushort(1))
            .metadataPolicy(MetadataPolicy.REQUEST_IF_MISSING)
            .build();

    UdpDatagramAddress dataAddress =
        UdpDatagramAddress.multicast(DATA_GROUP, DATA_PORT).networkInterface(networkInterface);

    // The discovery address is shared with the publisher. A multicast group lets both
    // processes bind the same port (SO_REUSEADDR) and join the same group, even on one host;
    // configuring it explicitly also keeps the engine off the Part 14 default 224.0.2.14:4840.
    UdpDatagramAddress discoveryAddress =
        UdpDatagramAddress.multicast(DISCOVERY_GROUP, DISCOVERY_PORT)
            .networkInterface(networkInterface);

    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp("udp-multicast")
                    .address(dataAddress)
                    .discoveryAddress(discoveryAddress)
                    .readerGroup(ReaderGroupConfig.builder("demo").dataSetReader(reader).build())
                    .build())
            .build();

    PubSubService service = PubSubService.create(config);

    // Listeners are push-style and may be registered before startup so no event is missed:
    // one for metadata announcements arriving via discovery, one for decoded DataSets.
    service.addMetaDataListener(this::onMetaDataReceived);
    service.addDataSetListener(
        new DataSetReaderRef("udp-multicast", "demo", "demo-reader"), this::onDataSetReceived);

    service.startup().get();

    logger.info(
        "subscribed to {} as reader 'demo-reader', probing {} for metadata",
        dataAddress.url(),
        discoveryAddress.url());

    final var future = new CompletableFuture<Void>();

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  service.close();
                  future.complete(null);
                }));

    future.get();
  }

  private void onMetaDataReceived(MetaDataReceivedEvent event) {
    FieldMetaData[] fields = event.metaData().getFields();

    String fieldNames =
        fields == null
            ? "(none)"
            : Arrays.stream(fields).map(FieldMetaData::getName).collect(Collectors.joining(", "));

    logger.info(
        "discovery delivered metadata: dataSet={}, version={}.{}, fields=[{}]",
        event.dataSetName(),
        event.configurationVersion().getMajorVersion(),
        event.configurationVersion().getMinorVersion(),
        fieldNames);
  }

  private void onDataSetReceived(DataSetReceivedEvent event) {
    String fields =
        event.fields().stream()
            // DataSetFieldValue -> DataValue -> Variant -> plain Java value
            .map(field -> field.name() + "=" + field.value().value().value())
            .collect(Collectors.joining(", "));

    logger.info("[received] dataSet={} {}", event.dataSetName(), fields);
  }
}
