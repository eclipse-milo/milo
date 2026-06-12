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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A standalone OPC UA Part 14 publisher that multicasts a three-field "telemetry" DataSet as UADP
 * NetworkMessages once per second, and automatically answers DataSetMetaData discovery probes.
 *
 * <p>What it demonstrates: UADP publishing over a UDP <em>multicast</em> data plane, and the
 * discovery responder. Because this connection has WriterGroups, the engine opens a responder on
 * the discovery address with no extra configuration; {@link DiscoverySubscriberExample} configures
 * no metadata at all and learns the field names by probing this responder.
 *
 * <p>Terminal order: start this example first (terminal 1), then {@link DiscoverySubscriberExample}
 * (terminal 2). Order is not critical — the subscriber retries its probes — but starting the
 * publisher first makes the demo immediate.
 *
 * <p>Run from the repository root, after building once with {@code mvn -q -DskipTests -pl
 * milo-examples/pubsub-examples -am install}:
 *
 * <pre>{@code
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.DiscoveryPublisherExample
 * }</pre>
 *
 * <p>Expected output: one startup line naming the data and discovery addresses, then a "published
 * cycle N: ..." line for the first cycle and every 10th cycle after it (about every 10 seconds).
 * Runs until Ctrl-C.
 *
 * <p>Defaults: data plane multicast group {@code opc.udp://239.255.20.1:15110}, discovery group
 * {@code opc.udp://239.255.20.2:15111}, both joined on the loopback interface only, so no traffic
 * leaves the machine. Pass a different interface address as the only argument (e.g. {@code
 * -Dexec.args="192.0.2.10"}) to publish on a real network instead.
 */
public class DiscoveryPublisherExample {

  private static final String DATA_GROUP = "239.255.20.1";
  private static final int DATA_PORT = 15110;

  private static final String DISCOVERY_GROUP = "239.255.20.2";
  private static final int DISCOVERY_PORT = 15111;

  private static final String DEFAULT_NETWORK_INTERFACE = "127.0.0.1";

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(1002));

  public static void main(String[] args) throws Exception {
    String networkInterface = args.length > 0 ? args[0] : DEFAULT_NETWORK_INTERFACE;

    new DiscoveryPublisherExample(networkInterface).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicLong cycle = new AtomicLong();

  private final String networkInterface;

  public DiscoveryPublisherExample(String networkInterface) {
    this.networkInterface = networkInterface;
  }

  public void run() throws Exception {
    // Field order defines the wire order. The configuration version is announced with the
    // metadata, letting subscribers detect dataset shape changes.
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("telemetry")
            .field(FieldDefinition.builder("temperature").dataType(NodeIds.Double).build())
            .field(FieldDefinition.builder("humidity").dataType(NodeIds.Double).build())
            .field(FieldDefinition.builder("status").dataType(NodeIds.String).build())
            .configurationVersion(uint(1), uint(0))
            .build();

    UdpDatagramAddress dataAddress =
        UdpDatagramAddress.multicast(DATA_GROUP, DATA_PORT).networkInterface(networkInterface);

    // The discovery address is shared with the subscriber. A multicast group lets both
    // processes bind the same port (SO_REUSEADDR) and join the same group, even on one host;
    // configuring it explicitly also keeps the engine off the Part 14 default 224.0.2.14:4840.
    UdpDatagramAddress discoveryAddress =
        UdpDatagramAddress.multicast(DISCOVERY_GROUP, DISCOVERY_PORT)
            .networkInterface(networkInterface);

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("udp-multicast")
                    .publisherId(PUBLISHER_ID)
                    .address(dataAddress)
                    .discoveryAddress(discoveryAddress)
                    .writerGroup(
                        WriterGroupConfig.builder("demo")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(1000))
                            .dataSetWriter(
                                DataSetWriterConfig.builder("demo-writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .build())
                            .build())
                    .build())
            .build();

    // The source is pulled once per publish cycle for a snapshot of the dataset's fields.
    PubSubBindings bindings =
        PubSubBindings.builder().source(dataSet.ref(), this::readTelemetry).build();

    PubSubService service = PubSubService.create(config, bindings);

    service.startup().get();

    logger.info(
        "publishing '{}' as publisher {} to {} (discovery responder on {})",
        dataSet.ref().name(),
        PUBLISHER_ID,
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

  /** Produce simulated telemetry values for one publish cycle. */
  private DataSetSnapshot readTelemetry(PublishedDataSetReadContext context) {
    long n = cycle.incrementAndGet();

    double temperature = 20.0 + 5.0 * Math.sin(n / 10.0);
    double humidity = 40.0 + 10.0 * Math.cos(n / 20.0);
    String status = "OK";

    if (n == 1 || n % 10 == 0) {
      logger.info(
          "published cycle {}: temperature={}, humidity={}, status={}",
          n,
          "%.2f".formatted(temperature),
          "%.2f".formatted(humidity),
          status);
    }

    return DataSetSnapshot.builder(context)
        .field("temperature", new DataValue(Variant.ofDouble(temperature)))
        .field("humidity", new DataValue(Variant.ofDouble(humidity)))
        .field("status", new DataValue(Variant.ofString(status)))
        .build();
  }
}
