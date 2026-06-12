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
 * A standalone OPC UA Part 14 PubSub publisher: every 500ms it publishes a "telemetry" DataSet
 * (temperature, status, counter) as UADP NetworkMessages over UDP unicast to {@code
 * 127.0.0.1:15100}.
 *
 * <p>This is one half of the getting-started pair; {@link UadpSubscriberExample} is the other.
 * Start the subscriber in one terminal first, then this publisher in a second terminal. (UDP is
 * connectionless, so the order is not critical — but starting the subscriber first means no
 * messages are missed.)
 *
 * <p>Build once, then run, from the repository root (keep {@code -am} on the compile step only, and
 * don't pass {@code -q} to {@code exec:java} — the example's SLF4J output routes through Maven's
 * logger, so quiet mode would suppress it):
 *
 * <pre>{@code
 * mvn -q -pl milo-examples/pubsub-examples -am compile
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *   -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.UadpPublisherExample
 * }</pre>
 *
 * <p>Expected output: one startup line, then one "[publishing]" line every 20 publish cycles (10
 * seconds) showing the field values currently going out on the wire. Runs until Ctrl-C.
 */
public class UadpPublisherExample {

  public static void main(String[] args) throws Exception {
    new UadpPublisherExample().run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /** Incremented once per publish cycle by {@link #readTelemetry}. */
  private final AtomicLong cycle = new AtomicLong(0);

  private void run() throws Exception {
    // Bind a PublishedDataSetSource to the "telemetry" dataset declared in the config.
    // The engine pulls the source for a fresh snapshot once per publish cycle; no
    // application-side threads or timers are needed to produce changing values.
    PubSubBindings bindings =
        PubSubBindings.builder().source(telemetryDataSet().ref(), this::readTelemetry).build();

    PubSubService service = PubSubService.create(createConfig(), bindings);

    service.startup().get();

    logger.info(
        "UADP publisher started: publishing dataset \"telemetry\" to opc.udp://127.0.0.1:15100"
            + " every 500ms, Ctrl-C to exit");

    // Run until Ctrl-C: the shutdown hook completes the future and main exits.
    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

    future.get();
  }

  /**
   * Reads a snapshot of the "telemetry" dataset: a sine-wave temperature, a status flag, and a
   * monotonic counter, all computed from the publish cycle number.
   */
  private DataSetSnapshot readTelemetry(PublishedDataSetReadContext context) {
    long n = cycle.getAndIncrement();

    // one full sine period every 120 cycles (60 seconds at 500ms)
    double temperature = 20.0 + 5.0 * Math.sin(2.0 * Math.PI * n / 120.0);
    boolean status = temperature >= 20.0;

    if (n % 20 == 0) {
      logger.info(
          "[publishing] cycle={} temperature={} status={} counter={}",
          n,
          String.format("%.2f", temperature),
          status,
          n);
    }

    return DataSetSnapshot.builder(context)
        .field("temperature", new DataValue(Variant.ofDouble(temperature)))
        .field("status", new DataValue(Variant.ofBoolean(status)))
        .field("counter", new DataValue(Variant.ofUInt32(uint(n))))
        .build();
  }

  /**
   * The "telemetry" PublishedDataSet: field order here defines the wire order, and the subscriber's
   * configured metadata must match it.
   */
  private static PublishedDataSetConfig telemetryDataSet() {
    return PublishedDataSetConfig.builder("telemetry")
        .field(FieldDefinition.builder("temperature").dataType(NodeIds.Double).build())
        .field(FieldDefinition.builder("status").dataType(NodeIds.Boolean).build())
        .field(FieldDefinition.builder("counter").dataType(NodeIds.UInt32).build())
        .build();
  }

  /**
   * One UDP connection with PublisherId 1001, one WriterGroup "demo" publishing the "telemetry"
   * dataset to 127.0.0.1:15100 every 500ms with default UADP message settings.
   */
  private static PubSubConfig createConfig() {
    PublishedDataSetConfig telemetry = telemetryDataSet();

    WriterGroupConfig writerGroup =
        WriterGroupConfig.builder("demo")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(500))
            .dataSetWriter(
                DataSetWriterConfig.builder("telemetry-writer")
                    .dataSet(telemetry.ref())
                    .dataSetWriterId(ushort(1))
                    .build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(telemetry)
        .connection(
            PubSubConnectionConfig.udp("publisher-connection")
                .publisherId(PublisherId.uint16(ushort(1001)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", 15100))
                // A UDP connection with writer groups also opens a discovery channel (it
                // answers metadata probes). Left unconfigured, that channel binds the Part 14
                // default opc.udp://224.0.2.14:4840 -- the well-known port 4840 plus a
                // multicast group join. Pinning a loopback port keeps the demo off the real
                // network, and since two processes on one host cannot bind the same discovery
                // port, the publisher pins 15101 and the subscriber pins 15102.
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", 15101))
                .writerGroup(writerGroup)
                .build())
        .build();
  }
}
