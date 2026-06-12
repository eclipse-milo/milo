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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.server.ServerPubSub;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Publishes live {@link OpcUaServer} address-space values over OPC UA PubSub (UADP over UDP
 * unicast) using {@link ServerPubSub}'s automatic address-space source.
 *
 * <p>What it demonstrates: a small namespace of writable variable nodes is updated by a background
 * task every 500 ms, and because <b>every</b> field of the PublishedDataSet is addressed by {@link
 * NodeFieldAddress}, {@link ServerPubSub#attach} auto-binds a source that reads the live node
 * values each publish cycle — no {@code PublishedDataSetSource} code is required. Subscribers see
 * the changing values with per-field StatusCodes and source timestamps.
 *
 * <p>The {@link OpcUaServer} built here is intentionally minimal and <b>never started</b>: it has
 * no endpoints, transports, or certificates, keeping the example focused on PubSub. {@link
 * ServerPubSub} works exactly the same attached to your real, started server — attach is legal any
 * time after {@code OpcUaServer} construction, and the PubSub runtime operates independently of the
 * server's client-facing transports.
 *
 * <p>Terminal order: start this publisher in terminal 1, then {@link ServerSourceSubscriberExample}
 * in terminal 2 (either order works; DataSets published before the subscriber starts are simply not
 * received).
 *
 * <p>Build the examples module once, then run from the repository root:
 *
 * <pre>{@code
 * mvn -q -pl milo-examples/pubsub-examples -am install -DskipTests
 *
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerSourcePublisherExample
 * }</pre>
 *
 * <p>Optional arguments override the data port (default {@code 15120}) and this side's discovery
 * port (default {@code 15121}), e.g. {@code -Dexec.args="15120 15121"}; the defaults match {@link
 * ServerSourceSubscriberExample}.
 *
 * <p>Expected output: one startup line announcing the publisher is running, then an {@code update
 * #N: Temperature=..., Pressure=..., Counter=...} line every 10th node update (about every 5
 * seconds), until Ctrl-C. The published values appear as {@code [received]} lines in the
 * subscriber's terminal.
 */
public class ServerSourcePublisherExample {

  private static final String DATA_HOST = "127.0.0.1";
  private static final int DEFAULT_DATA_PORT = 15120;
  private static final int DEFAULT_DISCOVERY_PORT = 15121;

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4001));
  private static final UShort WRITER_GROUP_ID = ushort(1);
  private static final UShort DATA_SET_WRITER_ID = ushort(1);

  private static final Duration UPDATE_INTERVAL = Duration.ofMillis(500);

  public static void main(String[] args) throws Exception {
    int dataPort = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_DATA_PORT;
    int discoveryPort = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_DISCOVERY_PORT;

    new ServerSourcePublisherExample(dataPort, discoveryPort).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicLong updateCount = new AtomicLong(0);

  private final int dataPort;
  private final int discoveryPort;

  public ServerSourcePublisherExample(int dataPort, int discoveryPort) {
    this.dataPort = dataPort;
    this.discoveryPort = discoveryPort;
  }

  private void run() throws Exception {
    // An endpoint-less OpcUaServer that is never started: the address space (including ns0)
    // loads in the constructor, which is all ServerPubSub needs. Attaching to a real, started
    // server works exactly the same way.
    OpcUaServerConfig serverConfig =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:examples:pubsub:server-source")
            .setApplicationName(LocalizedText.english("PubSub server-source example"))
            .setProductUri("urn:eclipse:milo:examples:pubsub")
            .build();

    var server =
        new OpcUaServer(
            serverConfig,
            transportProfile -> {
              throw new IllegalStateException(
                  "this example server has no transports: " + transportProfile);
            });

    var namespace = new DemoNamespace(server);
    namespace.startup();

    UaVariableNode temperatureNode =
        namespace.addVariable("Temperature", NodeIds.Double, Variant.ofDouble(20.0));
    UaVariableNode pressureNode =
        namespace.addVariable("Pressure", NodeIds.Double, Variant.ofDouble(1013.25));
    UaVariableNode counterNode =
        namespace.addVariable("Counter", NodeIds.Int32, Variant.ofInt32(0));

    PubSubConfig config =
        buildPubSubConfig(
            server, temperatureNode.getNodeId(), pressureNode.getNodeId(), counterNode.getNodeId());

    // Because every field of "demo-nodes" is a NodeFieldAddress, attach() auto-binds an
    // address-space source for it: each publish cycle pulls a fresh snapshot of the live
    // node values. attach() also eagerly resolves every NodeFieldAddress against the
    // server's NamespaceTable, failing fast on an unresolvable namespace.
    ServerPubSub serverPubSub = ServerPubSub.attach(server, config);

    serverPubSub.startup().get();

    logger.info(
        "publishing \"demo-nodes\" (publisherId={}, writerGroupId={}, dataSetWriterId={})"
            + " to {}:{} every {} ms",
        PUBLISHER_ID.toCanonicalString(),
        WRITER_GROUP_ID,
        DATA_SET_WRITER_ID,
        DATA_HOST,
        dataPort,
        UPDATE_INTERVAL.toMillis());

    ScheduledExecutorService updater =
        Executors.newSingleThreadScheduledExecutor(
            r -> {
              Thread thread = new Thread(r, "example-node-updater");
              thread.setDaemon(true);
              return thread;
            });

    updater.scheduleAtFixedRate(
        () -> updateNodeValues(temperatureNode, pressureNode, counterNode),
        UPDATE_INTERVAL.toMillis(),
        UPDATE_INTERVAL.toMillis(),
        TimeUnit.MILLISECONDS);

    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

    future.get();

    updater.shutdownNow();
    serverPubSub.close();
    namespace.shutdown();
    // the OpcUaServer was never started, so there is no server.shutdown() to call
  }

  /**
   * Build the PubSub configuration: one PublishedDataSet whose fields are all {@link
   * NodeFieldAddress} (the trigger for ServerPubSub's automatic address-space source), published by
   * one writer in writer group "demo" over UDP unicast.
   */
  private PubSubConfig buildPubSubConfig(
      OpcUaServer server, NodeId temperatureNodeId, NodeId pressureNodeId, NodeId counterNodeId) {

    // Field order is wire order, and ALL fields use NodeFieldAddress: a dataset with mixed or
    // key-only field addresses would not be auto-bound and would need an explicit source.
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("demo-nodes")
            .field(
                FieldDefinition.builder("Temperature")
                    .source(nodeAddress(server, temperatureNodeId))
                    .dataType(NodeIds.Double)
                    .build())
            .field(
                FieldDefinition.builder("Pressure")
                    .source(nodeAddress(server, pressureNodeId))
                    .dataType(NodeIds.Double)
                    .build())
            .field(
                FieldDefinition.builder("Counter")
                    .source(nodeAddress(server, counterNodeId))
                    .dataType(NodeIds.Int32)
                    .build())
            .build();

    // GroupHeader + WriterGroupId let the subscriber filter on writerGroupId (the default UADP
    // mask omits the GroupHeader entirely, which would defeat that filter).
    UadpWriterGroupSettings groupSettings =
        UadpWriterGroupSettings.builder()
            .networkMessageContentMask(
                UadpNetworkMessageContentMask.of(
                    UadpNetworkMessageContentMask.Field.PublisherId,
                    UadpNetworkMessageContentMask.Field.GroupHeader,
                    UadpNetworkMessageContentMask.Field.WriterGroupId,
                    UadpNetworkMessageContentMask.Field.SequenceNumber,
                    UadpNetworkMessageContentMask.Field.PayloadHeader))
            .build();

    // Major/MinorVersion let the subscriber's REQUIRE_CONFIGURED reader version-check its
    // configured metadata against what is on the wire.
    UadpDataSetWriterSettings writerSettings =
        UadpDataSetWriterSettings.builder()
            .dataSetMessageContentMask(
                UadpDataSetMessageContentMask.of(
                    UadpDataSetMessageContentMask.Field.Timestamp,
                    UadpDataSetMessageContentMask.Field.Status,
                    UadpDataSetMessageContentMask.Field.MajorVersion,
                    UadpDataSetMessageContentMask.Field.MinorVersion,
                    UadpDataSetMessageContentMask.Field.SequenceNumber))
            .build();

    // DataValue field encoding: per-field StatusCodes and source timestamps travel on the wire.
    DataSetFieldContentMask fieldMask =
        DataSetFieldContentMask.of(
            DataSetFieldContentMask.Field.StatusCode,
            DataSetFieldContentMask.Field.SourceTimestamp);

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp("server-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast(DATA_HOST, dataPort))
                // Pin the discovery plane to a loopback unicast port; otherwise the engine
                // binds UDP 4840 and joins multicast group 224.0.2.14. The subscriber pins a
                // different port (15122) because both sides bind a discovery socket on this
                // host.
                .discoveryAddress(UdpDatagramAddress.unicast(DATA_HOST, discoveryPort))
                .writerGroup(
                    WriterGroupConfig.builder("demo")
                        .writerGroupId(WRITER_GROUP_ID)
                        .publishingInterval(UPDATE_INTERVAL)
                        .messageSettings(groupSettings)
                        .dataSetWriter(
                            DataSetWriterConfig.builder("writer")
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(DATA_SET_WRITER_ID)
                                .fieldContentMask(fieldMask)
                                .settings(writerSettings)
                                .build())
                        .build())
                .build())
        .build();
  }

  private static NodeFieldAddress nodeAddress(OpcUaServer server, NodeId nodeId) {
    return NodeFieldAddress.of(nodeId, AttributeId.Value, server.getNamespaceTable());
  }

  /**
   * Write new values into the variable nodes; the next publish cycle picks them up automatically.
   * Logs every 10th update so the terminal stays readable.
   */
  private void updateNodeValues(
      UaVariableNode temperatureNode, UaVariableNode pressureNode, UaVariableNode counterNode) {

    long cycle = updateCount.incrementAndGet();

    // rounded to two decimals so both terminals log tidy values
    double temperature =
        Math.round((20.0 + 5.0 * Math.sin(2.0 * Math.PI * cycle / 60.0)) * 100.0) / 100.0;
    double pressure =
        Math.round((1013.25 + 2.5 * Math.cos(2.0 * Math.PI * cycle / 120.0)) * 100.0) / 100.0;
    int counter = (int) cycle;

    temperatureNode.setValue(new DataValue(Variant.ofDouble(temperature)));
    pressureNode.setValue(new DataValue(Variant.ofDouble(pressure)));
    counterNode.setValue(new DataValue(Variant.ofInt32(counter)));

    if (cycle == 1 || cycle % 10 == 0) {
      logger.info(
          "update #{}: Temperature={}, Pressure={}, Counter={}",
          cycle,
          "%.2f".formatted(temperature),
          "%.2f".formatted(pressure),
          counter);
    }
  }

  /**
   * A namespace hosting the example's writable variable nodes, hung off the Objects folder. No
   * client subscriptions are supported (the server is never started, so none can exist).
   */
  private static final class DemoNamespace extends ManagedNamespaceWithLifecycle {

    static final String NAMESPACE_URI = "urn:eclipse:milo:examples:pubsub:server-source";

    DemoNamespace(OpcUaServer server) {
      super(server, NAMESPACE_URI);
    }

    /** Add a writable scalar variable named {@code name} under the Objects folder. */
    UaVariableNode addVariable(String name, NodeId dataTypeId, Variant initialValue) {
      UaVariableNode node =
          new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
              .setNodeId(newNodeId(name))
              .setAccessLevel(AccessLevel.READ_WRITE)
              .setUserAccessLevel(AccessLevel.READ_WRITE)
              .setBrowseName(newQualifiedName(name))
              .setDisplayName(LocalizedText.english(name))
              .setDataType(dataTypeId)
              .setValueRank(ValueRanks.Scalar)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .build();

      node.setValue(new DataValue(initialValue));

      node.addReference(
          new Reference(
              node.getNodeId(),
              NodeIds.HasComponent,
              NodeIds.ObjectsFolder.expanded(),
              Reference.Direction.INVERSE));

      getNodeManager().addNode(node);

      return node;
    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {}

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {}

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {}

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {}
  }
}
