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

import java.time.Duration;
import java.time.Instant;
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
 * A standalone OPC UA Part 14 PubSub publisher demonstrating a <em>structured</em> configuration:
 * one UDP connection (PublisherId 7001) carrying two WriterGroups with different publishing
 * intervals — "fast" (250ms) with <em>two</em> dataset writers ("motion": position/velocity, and
 * "vibration": rms/peak) and "slow" (2000ms) with one ("status": uptimeSeconds/state) — as UADP
 * NetworkMessages over UDP unicast to {@code 127.0.0.1:15150}.
 *
 * <p>What it demonstrates beyond the getting-started pair: how writers and groups compose. The two
 * "fast" writers are sampled back-to-back in the same publish cycle and their DataSetMessages
 * travel together in a single NetworkMessage per cycle, while the "slow" group runs on its own
 * independent schedule over the same UDP channel. See the comments in {@link #createConfig()} for
 * the packing and scheduling semantics.
 *
 * <p>{@link MultiGroupSubscriberExample} is the other half. Start the subscriber in one terminal
 * first, then this publisher in a second terminal. (UDP is connectionless, so the order is not
 * critical — but starting the subscriber first means no messages are missed.)
 *
 * <p>Build once, then run, from the repository root (keep {@code -am} on the install step only, and
 * don't pass {@code -q} to {@code exec:java} — the example's SLF4J output routes through Maven's
 * logger, so quiet mode would suppress it):
 *
 * <pre>{@code
 * mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *   -Dexec.mainClass=org.eclipse.milo.examples.pubsub.udp.MultiGroupPublisherExample
 * }</pre>
 *
 * <p>Expected output: one startup line, then one "[summary]" line every 10 fast cycles (2.5
 * seconds) showing the values all three sources most recently published. Runs until Ctrl-C.
 */
public class MultiGroupPublisherExample {

  public static void main(String[] args) throws Exception {
    new MultiGroupPublisherExample().run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /** Incremented once per "fast" group publish cycle by {@link #readMotion}. */
  private final AtomicLong fastCycle = new AtomicLong(0);

  /** Wall-clock start time, the basis for the "status" dataset's uptimeSeconds field. */
  private final Instant startedAt = Instant.now();

  // Last values published by each source, kept only so the periodic "[summary]" log line can
  // report all three datasets at once instead of logging every cycle.
  private volatile double lastRms;
  private volatile double lastPeak;
  private volatile long lastUptimeSeconds;
  private volatile String lastState = "STARTING";

  private void run() throws Exception {
    // One source per PublishedDataSet. Each WriterGroup runs its own publish task: every
    // cycle the group's writers are read in list order, so "motion" and "vibration" are
    // sampled back-to-back every 250ms while "status" is sampled every 2000ms. A failing
    // source never aborts a cycle: the engine substitutes Bad_InternalError values for that
    // writer's fields and the other writers in the group still publish normally, so
    // subscribers see Bad-status fields rather than a gap.
    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(motionDataSet().ref(), this::readMotion)
            .source(vibrationDataSet().ref(), this::readVibration)
            .source(statusDataSet().ref(), this::readStatus)
            .build();

    PubSubService service = PubSubService.create(createConfig(), bindings);

    service.startup().get();

    logger.info(
        "multi-group publisher started: groups \"fast\" (motion + vibration, 250ms) and"
            + " \"slow\" (status, 2000ms) publishing to opc.udp://127.0.0.1:15150 as"
            + " publisher 7001, Ctrl-C to exit");

    // Run until Ctrl-C: the shutdown hook completes the future and main exits.
    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

    future.get();
  }

  /**
   * Reads a snapshot of the "motion" dataset: a point moving in a circle, one revolution every 120
   * fast cycles (30 seconds at 250ms). Also emits the periodic summary log line.
   */
  private DataSetSnapshot readMotion(PublishedDataSetReadContext context) {
    long n = fastCycle.getAndIncrement();

    double angle = 2.0 * Math.PI * n / 120.0;
    double position = 100.0 * Math.sin(angle);
    double velocity = 100.0 * Math.cos(angle); // derivative shape, arbitrary units

    if (n % 10 == 0) {
      logger.info(
          "[summary] fastCycle={} motion(position={}, velocity={})"
              + " vibration(rms={}, peak={}) status(uptimeSeconds={}, state={})",
          n,
          String.format("%.2f", position),
          String.format("%.2f", velocity),
          String.format("%.3f", lastRms),
          String.format("%.3f", lastPeak),
          lastUptimeSeconds,
          lastState);
    }

    return DataSetSnapshot.builder(context)
        .field("position", new DataValue(Variant.ofDouble(position)))
        .field("velocity", new DataValue(Variant.ofDouble(velocity)))
        .build();
  }

  /**
   * Reads a snapshot of the "vibration" dataset. This source is pulled in the same "fast" publish
   * cycle as {@link #readMotion} (writers are read in list order, "motion" first), so it keys off
   * the cycle counter that readMotion just incremented.
   */
  private DataSetSnapshot readVibration(PublishedDataSetReadContext context) {
    long n = fastCycle.get();

    double rms = 0.8 + 0.4 * Math.abs(Math.sin(2.0 * Math.PI * n / 40.0));
    double peak = rms * 2.8;

    lastRms = rms;
    lastPeak = peak;

    return DataSetSnapshot.builder(context)
        .field("rms", new DataValue(Variant.ofDouble(rms)))
        .field("peak", new DataValue(Variant.ofDouble(peak)))
        .build();
  }

  /**
   * Reads a snapshot of the "status" dataset: seconds since startup and a coarse state string.
   * Pulled every 2000ms by the "slow" group's own publish task, independent of the fast cycles.
   */
  private DataSetSnapshot readStatus(PublishedDataSetReadContext context) {
    long uptimeSeconds = Duration.between(startedAt, Instant.now()).toSeconds();
    String state = uptimeSeconds % 60 < 45 ? "RUNNING" : "MAINTENANCE";

    lastUptimeSeconds = uptimeSeconds;
    lastState = state;

    return DataSetSnapshot.builder(context)
        .field("uptimeSeconds", new DataValue(Variant.ofInt64(uptimeSeconds)))
        .field("state", new DataValue(Variant.ofString(state)))
        .build();
  }

  /**
   * The "motion" PublishedDataSet: field order here defines the wire order, and the subscriber's
   * configured metadata must match it.
   */
  private static PublishedDataSetConfig motionDataSet() {
    return PublishedDataSetConfig.builder("motion")
        .field(FieldDefinition.builder("position").dataType(NodeIds.Double).build())
        .field(FieldDefinition.builder("velocity").dataType(NodeIds.Double).build())
        .build();
  }

  /** The "vibration" PublishedDataSet, published by the second writer in the "fast" group. */
  private static PublishedDataSetConfig vibrationDataSet() {
    return PublishedDataSetConfig.builder("vibration")
        .field(FieldDefinition.builder("rms").dataType(NodeIds.Double).build())
        .field(FieldDefinition.builder("peak").dataType(NodeIds.Double).build())
        .build();
  }

  /** The "status" PublishedDataSet, published by the "slow" group's only writer. */
  private static PublishedDataSetConfig statusDataSet() {
    return PublishedDataSetConfig.builder("status")
        .field(FieldDefinition.builder("uptimeSeconds").dataType(NodeIds.Int64).build())
        .field(FieldDefinition.builder("state").dataType(NodeIds.String).build())
        .build();
  }

  /**
   * One UDP connection with PublisherId 7001 carrying two WriterGroups: "fast" (250ms, two writers)
   * and "slow" (2000ms, one writer).
   */
  private static PubSubConfig createConfig() {
    PublishedDataSetConfig motion = motionDataSet();
    PublishedDataSetConfig vibration = vibrationDataSet();
    PublishedDataSetConfig status = statusDataSet();

    // Two writers in one group: each publish cycle the engine collects one DataSetMessage
    // per writer, in writer list order ("motion" then "vibration"). On UDP a group's
    // writers always share one partition, so the two DataSetMessages travel together in a
    // single UADP NetworkMessage per cycle. The default-on PayloadHeader is what makes that
    // demultiplexable: it carries the DataSetMessage count plus each DataSetWriterId, in
    // writer order. (Writer-level broker queue overrides, which can split a group's writers
    // into separate messages, apply only to broker transports — never UDP.)
    WriterGroupConfig fastGroup =
        WriterGroupConfig.builder("fast")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(250))
            .dataSetWriter(
                DataSetWriterConfig.builder("motion-writer")
                    .dataSet(motion.ref())
                    .dataSetWriterId(ushort(1))
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("vibration-writer")
                    .dataSet(vibration.ref())
                    .dataSetWriterId(ushort(2))
                    .build())
            .build();

    // A second group on the same connection gets its own publish task at its own interval;
    // groups never share a schedule, and each group keeps its own NetworkMessage sequence
    // number (DataSetMessage sequence numbers are per writer). DataSetWriterIds, however,
    // are deliberately unique across BOTH groups (1, 2, 3): the default UADP masks omit the
    // GroupHeader, so the WriterGroupId never appears on the wire and subscribers can only
    // tell streams apart by PublisherId + DataSetWriterId.
    WriterGroupConfig slowGroup =
        WriterGroupConfig.builder("slow")
            .writerGroupId(ushort(2))
            .publishingInterval(Duration.ofMillis(2000))
            .dataSetWriter(
                DataSetWriterConfig.builder("status-writer")
                    .dataSet(status.ref())
                    .dataSetWriterId(ushort(3))
                    .build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(motion)
        .publishedDataSet(vibration)
        .publishedDataSet(status)
        .connection(
            PubSubConnectionConfig.udp("publisher-connection")
                .publisherId(PublisherId.uint16(ushort(7001)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", 15150))
                // Groups own no sockets: both writer groups send through this connection's
                // single publisher channel. The connection also answers metadata discovery
                // probes; pinning the discovery channel to a loopback port keeps the demo
                // off the Part 14 default opc.udp://224.0.2.14:4840 (well-known port 4840
                // plus a multicast group join). Two processes on one host cannot bind the
                // same discovery port, so the publisher pins 15151 and the subscriber 15152.
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", 15151))
                .writerGroup(fastGroup)
                .writerGroup(slowGroup)
                .build())
        .build();
  }
}
