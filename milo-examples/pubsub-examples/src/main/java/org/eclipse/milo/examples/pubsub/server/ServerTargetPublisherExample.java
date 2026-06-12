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

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standalone PubSub publisher feeding {@link ServerTargetSubscriberExample}: publishes a two-field
 * DataSet ("temperature" and "counter", from a simulated source) as UADP NetworkMessages over UDP
 * unicast to {@code opc.udp://127.0.0.1:15130}, where the subscriber's TargetVariables mapping
 * writes the fields into its server nodes.
 *
 * <p>The DataSetWriter's field content mask requests StatusCode + SourceTimestamp encoding, so each
 * field travels as a full DataValue and the subscriber can pass status and timestamp through into
 * its target nodes (Part 14 Table 80).
 *
 * <p>Start {@link ServerTargetSubscriberExample} FIRST in another terminal, then this example. Run
 * from the repository root:
 *
 * <pre>{@code
 * mvn -pl milo-examples/pubsub-examples -am compile
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *   -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerTargetPublisherExample
 * }</pre>
 *
 * <p>Optional arguments: {@code [dataPort] [discoveryPort]}, defaulting to 15130 and 15132. The
 * discovery port must differ from the subscriber's (15131) so both processes can bind their
 * loopback discovery sockets.
 *
 * <p>Expected output: a startup line, then one "publishing cycle" line every 10th publishing cycle
 * (about every 5 seconds); the subscriber terminal logs matching "[received]" and "[node]" lines.
 * Runs until Ctrl-C.
 */
public class ServerTargetPublisherExample {

  private static final String DATA_HOST = "127.0.0.1";
  private static final int DEFAULT_DATA_PORT = 15130;
  private static final int DEFAULT_DISCOVERY_PORT = 15132;

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(5001));
  private static final UShort WRITER_GROUP_ID = ushort(1);
  private static final UShort DATA_SET_WRITER_ID = ushort(1);

  private static final Duration PUBLISHING_INTERVAL = Duration.ofMillis(500);
  private static final int LOG_EVERY_N_CYCLES = 10;

  /**
   * Include GroupHeader + WriterGroupId in the NetworkMessage header: the subscriber's reader
   * filters on writerGroupId, and the UADP-Dynamic default mask omits the group header entirely.
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
   * Include MajorVersion + MinorVersion in each DataSetMessage so the subscriber's
   * REQUIRE_CONFIGURED reader can check them against its configured metadata version.
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

  /**
   * Encode fields as DataValues carrying StatusCode and SourceTimestamp: the subscriber's
   * TargetVariables mapping passes both through into its target nodes, so the status codes and
   * source timestamps visible on the server nodes are the ones produced by this publisher's source.
   */
  private static final DataSetFieldContentMask FIELD_MASK =
      DataSetFieldContentMask.of(
          DataSetFieldContentMask.Field.StatusCode, DataSetFieldContentMask.Field.SourceTimestamp);

  public static void main(String[] args) throws Exception {
    int dataPort = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_DATA_PORT;
    int discoveryPort = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_DISCOVERY_PORT;

    new ServerTargetPublisherExample().run(dataPort, discoveryPort);
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicLong cycle = new AtomicLong();

  private void run(int dataPort, int discoveryPort) throws Exception {
    // Field order defines the wire order; the subscriber's configured metadata lists the same
    // fields, with the same names and types, in the same order.
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("demo-ds")
            .field(FieldDefinition.builder("temperature").dataType(NodeIds.Double).build())
            .field(FieldDefinition.builder("counter").dataType(NodeIds.Int32).build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("pub-conn")
                    .publisherId(PUBLISHER_ID)
                    .address(UdpDatagramAddress.unicast(DATA_HOST, dataPort))
                    // Pin discovery to loopback so the engine doesn't bind UDP 4840 and join
                    // the default multicast group 224.0.2.14. The port differs from the
                    // subscriber's discovery port so both processes can bind their loopback
                    // discovery sockets.
                    .discoveryAddress(UdpDatagramAddress.unicast(DATA_HOST, discoveryPort))
                    .writerGroup(
                        WriterGroupConfig.builder("demo")
                            .writerGroupId(WRITER_GROUP_ID)
                            .publishingInterval(PUBLISHING_INTERVAL)
                            .messageSettings(GROUP_SETTINGS)
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(DATA_SET_WRITER_ID)
                                    .fieldContentMask(FIELD_MASK)
                                    .settings(WRITER_SETTINGS)
                                    .build())
                            .build())
                    .build())
            .build();

    // The engine pulls a snapshot from the bound source once per publishing interval.
    PubSubBindings bindings =
        PubSubBindings.builder().source(dataSet.ref(), this::readSimulatedValues).build();

    try (PubSubService service = PubSubService.create(config, bindings)) {
      service.startup().get(10, TimeUnit.SECONDS);

      logger.info(
          "Publishing \"demo-ds\" to opc.udp://{}:{} every {} ms as publisherId={}",
          DATA_HOST,
          dataPort,
          PUBLISHING_INTERVAL.toMillis(),
          PUBLISHER_ID);

      final CompletableFuture<Void> future = new CompletableFuture<>();

      Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

      future.get();
    }
  }

  /**
   * Produce the next snapshot of simulated values. Every field is a full DataValue: the GOOD status
   * and the source timestamp travel on the wire (see {@link #FIELD_MASK}) and reappear on the
   * subscriber's target nodes.
   */
  private DataSetSnapshot readSimulatedValues(PublishedDataSetReadContext context) {
    long n = cycle.getAndIncrement();
    DateTime sourceTime = DateTime.now();

    double temperature = 20.0 + 5.0 * Math.sin(n / 10.0);
    int counter = (int) n;

    if (n % LOG_EVERY_N_CYCLES == 0) {
      logger.info(
          "publishing cycle {}: temperature={}, counter={}",
          n,
          String.format("%.2f", temperature),
          counter);
    }

    return DataSetSnapshot.builder(context)
        .field(
            "temperature",
            new DataValue(Variant.ofDouble(temperature), StatusCode.GOOD, sourceTime, null))
        .field(
            "counter", new DataValue(Variant.ofInt32(counter), StatusCode.GOOD, sourceTime, null))
        .build();
  }
}
