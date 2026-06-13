/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.pubsub;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A self-terminating example demonstrating live reconfiguration of a running {@link PubSubService}
 * via {@link PubSubService#update update}, which applies the minimal diff between the old and new
 * config using {@link PubSubService.ReconfigureMode#DISABLE_AFFECTED}.
 *
 * <p>One JVM hosts both sides over UDP unicast loopback (data plane {@code 127.0.0.1:15140}): a
 * publisher service (PublisherId UInt16 6001) and a subscriber service whose reader logs every
 * received DataSet. The example publishes at 1000ms for ~5 seconds, then swaps in an otherwise
 * identical config with a 250ms publishing interval, logs the {@link ReconfigureResult}, runs ~5
 * more seconds at the visibly faster rate, and exits after logging a per-phase summary.
 *
 * <p>Nothing else needs to be started first; run this class by itself, from the repository root:
 *
 * <pre>{@code
 * mvn -q -pl milo-examples/pubsub-examples -am compile exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.ReconfigureExample
 * }</pre>
 *
 * <p>Expected output: about 6 {@code [received]} lines roughly 1 second apart (phase 1; the first
 * cycle fires immediately); three {@code [reconfigured]} lines reporting {@code
 * restartedPaths=[pub-conn/group]} with empty added and removed paths; a quiet ~1.5 seconds, then
 * about 14 {@code [received]} lines roughly 250ms apart (phase 2); and finally {@code [summary]}
 * lines comparing the per-phase message counts. Total runtime is about 15 seconds, then the process
 * exits 0.
 *
 * <p>The quiet gap opening phase 2 is the subscriber's Part 14 §7.2.3 sequence-number tracking at
 * work: the restarted writer group also restarted its sequence numbers at 0, so the reader silently
 * drops the first post-restart messages as stale — as many as phase 1 sent — until the restarted
 * counters overtake its windows. The drops tick only the reader's {@code staleSequenceMessages}
 * diagnostics counter; nothing is logged.
 */
public class ReconfigureExample {

  private static final String LOOPBACK = "127.0.0.1";
  private static final int DATA_PORT = 15140;
  private static final int PUBLISHER_DISCOVERY_PORT = 15141;
  private static final int SUBSCRIBER_DISCOVERY_PORT = 15142;

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(6001));

  private static final String DATA_SET_NAME = "demo";

  private static final Duration PHASE_1_INTERVAL = Duration.ofMillis(1000);
  private static final Duration PHASE_2_INTERVAL = Duration.ofMillis(250);
  private static final Duration PHASE_DURATION = Duration.ofSeconds(5);

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final DataSetReaderRef READER_REF =
      new DataSetReaderRef("sub-conn", "readers", "reader");

  /**
   * The reader filters on writerGroupId, so the publisher must include GroupHeader + WriterGroupId
   * in the NetworkMessage; the UADP default mask omits the GroupHeader.
   */
  private static final UadpWriterGroupSettings GROUP_SETTINGS =
      UadpWriterGroupSettings.builder()
          .networkMessageContentMask(
              UadpNetworkMessageContentMask.of(
                  UadpNetworkMessageContentMask.Field.PublisherId,
                  UadpNetworkMessageContentMask.Field.GroupHeader,
                  UadpNetworkMessageContentMask.Field.WriterGroupId,
                  UadpNetworkMessageContentMask.Field.SequenceNumber,
                  UadpNetworkMessageContentMask.Field.PayloadHeader))
          .build();

  /**
   * MajorVersion + MinorVersion let the {@code REQUIRE_CONFIGURED} reader verify the configuration
   * version of the metadata it decodes against.
   */
  private static final UadpDataSetWriterSettings WRITER_SETTINGS =
      UadpDataSetWriterSettings.builder()
          .dataSetMessageContentMask(
              UadpDataSetMessageContentMask.of(
                  UadpDataSetMessageContentMask.Field.Timestamp,
                  UadpDataSetMessageContentMask.Field.Status,
                  UadpDataSetMessageContentMask.Field.MajorVersion,
                  UadpDataSetMessageContentMask.Field.MinorVersion,
                  UadpDataSetMessageContentMask.Field.SequenceNumber))
          .build();

  public static void main(String[] args) throws Exception {
    new ReconfigureExample().run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicInteger phase = new AtomicInteger(1);
  private final AtomicInteger receivedCount = new AtomicInteger();
  private final AtomicLong publishedCount = new AtomicLong();

  private void run() throws Exception {
    PubSubService subscriber =
        PubSubService.create(
            subscriberConfig(),
            PubSubBindings.builder().listener(READER_REF, this::onDataSetReceived).build());

    PubSubService publisher =
        PubSubService.create(
            publisherConfig(PHASE_1_INTERVAL),
            PubSubBindings.builder()
                .source(new PublishedDataSetRef(DATA_SET_NAME), this::readDataSet)
                .build());

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    logger.info(
        "phase 1: publishing every {}ms for ~{}s",
        PHASE_1_INTERVAL.toMillis(),
        PHASE_DURATION.toSeconds());

    Thread.sleep(PHASE_DURATION.toMillis());

    // Reconfiguration is by replacement: build a config identical to the running one except
    // for the publishing interval. Because the connection shell (enabled, publisherId,
    // address, properties) is unchanged, DISABLE_AFFECTED keeps the restart scoped to the
    // writer group ("pub-conn/group"); changing a shell field would escalate the group-level
    // change to a full connection restart. (The discovery address is not part of the shell
    // comparison: a change to it alone restarts nothing and only takes effect when the
    // connection is rebuilt for another reason.)
    ReconfigureResult result = publisher.update(current -> publisherConfig(PHASE_2_INTERVAL));

    logger.info("[reconfigured] addedPaths={}", result.addedPaths());
    logger.info("[reconfigured] removedPaths={}", result.removedPaths());
    logger.info("[reconfigured] restartedPaths={}", result.restartedPaths());

    int phase1Received = receivedCount.get();
    phase.set(2);

    logger.info(
        "phase 2: publishing every {}ms for ~{}s",
        PHASE_2_INTERVAL.toMillis(),
        PHASE_DURATION.toSeconds());

    Thread.sleep(PHASE_DURATION.toMillis());

    // Diagnostics counters are keyed by component path, but a restarted component starts
    // fresh counters: the writer's count covers phase 2 only, while the reader, untouched by
    // the reconfigure, counts both phases. Capture the writer's count before shutdown.
    long dataSetsSent =
        publisher
            .diagnostics()
            .component("pub-conn/group/writer")
            .map(PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesSent)
            .orElse(0L);

    // Stop the publisher before tallying so the summary is the last thing logged.
    publisher.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    int phase2Received = receivedCount.get() - phase1Received;

    long dataSetsReceived =
        subscriber
            .diagnostics()
            .component("sub-conn/readers/reader")
            .map(PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesReceived)
            .orElse(0L);

    subscriber.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    logger.info(
        "[summary] phase 1: {} DataSets received in ~{}s at {}ms",
        phase1Received,
        PHASE_DURATION.toSeconds(),
        PHASE_1_INTERVAL.toMillis());
    logger.info(
        "[summary] phase 2: {} DataSets received in ~{}s at {}ms",
        phase2Received,
        PHASE_DURATION.toSeconds(),
        PHASE_2_INTERVAL.toMillis());
    logger.info(
        "[summary] diagnostics: writer sent {} DataSetMessages since the group restart, "
            + "reader received {} across both phases",
        dataSetsSent,
        dataSetsReceived);

    // Both services used the default service config, which borrows the Stack's shared
    // daemon executors; release them so the JVM exits promptly.
    Stack.releaseSharedResources();

    logger.info("shutdown complete, exiting");
  }

  /** The source pulled once per publish cycle; logs sparingly (first cycle, then every 10th). */
  private DataSetSnapshot readDataSet(PublishedDataSetReadContext context) {
    long n = publishedCount.incrementAndGet();
    if (n == 1L || n % 10 == 0L) {
      logger.info("published {} samples so far", n);
    }

    double temperature = Math.round((20.0 + 5.0 * Math.sin(n / 5.0)) * 100.0) / 100.0;

    return DataSetSnapshot.builder(context)
        .field("temperature", new DataValue(Variant.ofDouble(temperature)))
        .field("tick", new DataValue(Variant.ofInt32((int) n)))
        .build();
  }

  private void onDataSetReceived(DataSetReceivedEvent event) {
    receivedCount.incrementAndGet();

    String fields =
        event.fieldsByName().entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue().value().value())
            .collect(Collectors.joining(", "));

    logger.info("[received] phase={} dataSet={} {}", phase.get(), event.dataSetName(), fields);
  }

  /**
   * The publisher config: one UDP connection sending to loopback, one writer group publishing the
   * "demo" dataset at {@code publishingInterval}.
   *
   * <p>Both phases build this same config, differing only in the interval. The connection-level
   * fields (publisherId, address, properties) are identical in both, which is what keeps the
   * reconfigure restart scoped to the writer group instead of the whole connection.
   */
  private static PubSubConfig publisherConfig(Duration publishingInterval) {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(DATA_SET_NAME)
            .field(FieldDefinition.builder("temperature").dataType(NodeIds.Double).build())
            .field(FieldDefinition.builder("tick").dataType(NodeIds.Int32).build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast(LOOPBACK, DATA_PORT))
                // Pinned to loopback so the engine never binds the well-known port 4840 or
                // joins the default 224.0.2.14 multicast group.
                .discoveryAddress(UdpDatagramAddress.unicast(LOOPBACK, PUBLISHER_DISCOVERY_PORT))
                .writerGroup(
                    WriterGroupConfig.builder("group")
                        .writerGroupId(ushort(1))
                        .publishingInterval(publishingInterval)
                        .messageSettings(GROUP_SETTINGS)
                        .dataSetWriter(
                            DataSetWriterConfig.builder("writer")
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(1))
                                .settings(WRITER_SETTINGS)
                                .build())
                        .build())
                .build())
        .build();
  }

  /**
   * The subscriber config: one UDP connection listening on loopback, one reader matching the
   * publisher's ids, decoding against locally configured metadata.
   */
  private static PubSubConfig subscriberConfig() {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder(DATA_SET_NAME)
            .field("temperature", NodeIds.Double)
            .field("tick", NodeIds.Int32)
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("sub-conn")
                .address(UdpDatagramAddress.unicast(LOOPBACK, DATA_PORT))
                // Pinned to loopback so the engine never binds the well-known port 4840 or
                // joins the default 224.0.2.14 multicast group.
                .discoveryAddress(UdpDatagramAddress.unicast(LOOPBACK, SUBSCRIBER_DISCOVERY_PORT))
                .readerGroup(
                    ReaderGroupConfig.builder("readers")
                        .dataSetReader(
                            DataSetReaderConfig.builder("reader")
                                .publisherId(PUBLISHER_ID)
                                .writerGroupId(ushort(1))
                                .dataSetWriterId(ushort(1))
                                .dataSetMetaData(metaData)
                                .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                .build())
                        .build())
                .build())
        .build();
  }
}
