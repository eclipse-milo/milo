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

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldSelector;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariableConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariablesConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PubSub-into-the-address-space example: an {@link OpcUaServer} whose variable nodes are written by
 * a PubSub DataSetReader.
 *
 * <p>{@link ServerPubSub#attach} integrates a PubSub configuration with the server. The single
 * DataSetReader subscribes to UADP NetworkMessages on {@code opc.udp://127.0.0.1:15130} and maps
 * the received fields onto server nodes with a Part 14 §6.2.11.1 TargetVariables configuration:
 * field "temperature" is written to the Temperature node and field "counter" to the Counter node,
 * with received StatusCodes and source timestamps passing through to the nodes.
 *
 * <p>The server is intentionally never started: PubSub operates independently of the server's
 * client-facing transports, so this example configures no endpoints, certificates, or identity
 * validation. Add those (see server-examples ExampleServer) to also browse the target nodes with an
 * OPC UA client.
 *
 * <p>Start this example FIRST, then start {@link ServerTargetPublisherExample} in a second
 * terminal. Run from the repository root:
 *
 * <pre>{@code
 * mvn -pl milo-examples/pubsub-examples -am compile
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *   -Dexec.mainClass=org.eclipse.milo.examples.pubsub.server.ServerTargetSubscriberExample
 * }</pre>
 *
 * <p>Optional arguments: {@code [dataPort] [discoveryPort]}, defaulting to 15130 and 15131.
 *
 * <p>Expected output once the publisher is running: a "[received]" line for every delivered DataSet
 * (about two per second) showing field names and values, and a "[node]" line every second showing
 * the current values, status codes, and source timestamps of the target nodes — the same data
 * observed arriving on the wire and landing in the address space. Failed target writes, if any, are
 * logged from {@link ServerPubSub#targetWriteErrors()}. Runs until Ctrl-C.
 */
public class ServerTargetSubscriberExample {

  private static final String DATA_HOST = "127.0.0.1";
  private static final int DEFAULT_DATA_PORT = 15130;
  private static final int DEFAULT_DISCOVERY_PORT = 15131;

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(5001));
  private static final UShort WRITER_GROUP_ID = ushort(1);
  private static final UShort DATA_SET_WRITER_ID = ushort(1);

  public static void main(String[] args) throws Exception {
    int dataPort = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_DATA_PORT;
    int discoveryPort = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_DISCOVERY_PORT;

    var example = new ServerTargetSubscriberExample(dataPort, discoveryPort);
    example.startup();

    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

    future.get();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ScheduledExecutorService scheduler =
      Executors.newSingleThreadScheduledExecutor(
          runnable -> {
            Thread thread = new Thread(runnable, "target-node-logger");
            thread.setDaemon(true);
            return thread;
          });

  private final int dataPort;
  private final ServerPubSub serverPubSub;
  private final UaVariableNode temperatureNode;
  private final UaVariableNode counterNode;

  public ServerTargetSubscriberExample(int dataPort, int discoveryPort) {
    this.dataPort = dataPort;

    OpcUaServerConfig serverConfig =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:examples:pubsub:target-subscriber")
            .setApplicationName(LocalizedText.english("PubSub TargetVariables example server"))
            .setProductUri("urn:eclipse:milo:examples:pubsub:target-subscriber")
            .build();

    // The address space is usable as soon as the server is constructed; startup() is never
    // called, and with no endpoints configured it would fail anyway.
    var server =
        new OpcUaServer(
            serverConfig,
            transportProfile -> {
              throw new IllegalStateException("no transports configured: " + transportProfile);
            });

    var namespace = new DemoNamespace(server);
    namespace.startup();

    temperatureNode =
        namespace.addTargetVariable("Temperature", NodeIds.Double, Variant.ofDouble(0.0));
    counterNode = namespace.addTargetVariable("Counter", NodeIds.Int32, Variant.ofInt32(0));

    PubSubConfig pubSubConfig =
        buildPubSubConfig(
            server, dataPort, discoveryPort, temperatureNode.getNodeId(), counterNode.getNodeId());

    serverPubSub = ServerPubSub.attach(server, pubSubConfig);

    // ServerPubSub.runtime() exposes the full PubSubService; a DataSetListener shows DataSets
    // arriving on the wire, independent of the TargetVariables writes into the address space.
    serverPubSub.runtime().addDataSetListener(this::onDataSetReceived);
  }

  private void startup() throws Exception {
    serverPubSub.startup().get(10, TimeUnit.SECONDS);

    scheduler.scheduleAtFixedRate(this::logTargetNodes, 1, 1, TimeUnit.SECONDS);

    logger.info("ServerPubSub started, reader listening on opc.udp://{}:{}", DATA_HOST, dataPort);
    logger.info(
        "Start ServerTargetPublisherExample in another terminal,"
            + " then watch for [received] and [node] lines");
  }

  /**
   * Build the subscribe-side PubSub configuration: one UDP connection with one DataSetReader that
   * writes received fields into the server's address space.
   */
  private static PubSubConfig buildPubSubConfig(
      OpcUaServer server,
      int dataPort,
      int discoveryPort,
      NodeId temperatureNodeId,
      NodeId counterNodeId) {

    // The configured metadata must match the publisher's dataset: same field names and types,
    // in the same order. REQUIRE_CONFIGURED means only this metadata is ever used for decoding.
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("demo-ds")
            .field("temperature", NodeIds.Double)
            .field("counter", NodeIds.Int32)
            .build();

    // Part 14 §6.2.11.1 TargetVariables: select received fields by name and write them to the
    // Value attribute of server nodes. StatusCodes and source timestamps received on the wire
    // pass through to the target nodes (Table 80).
    TargetVariablesConfig targetVariables =
        TargetVariablesConfig.builder()
            .map(
                FieldSelector.byName("temperature"),
                TargetVariableConfig.builder()
                    .target(
                        NodeFieldAddress.of(
                            temperatureNodeId, AttributeId.Value, server.getNamespaceTable()))
                    .build())
            .map(
                FieldSelector.byName("counter"),
                TargetVariableConfig.builder()
                    .target(
                        NodeFieldAddress.of(
                            counterNodeId, AttributeId.Value, server.getNamespaceTable()))
                    .build())
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("demo-conn")
                .address(UdpDatagramAddress.unicast(DATA_HOST, dataPort))
                // Pin discovery to loopback so the engine doesn't bind UDP 4840 and join the
                // default multicast group 224.0.2.14. The port differs from the publisher's
                // discovery port so both processes can bind their loopback discovery sockets.
                .discoveryAddress(UdpDatagramAddress.unicast(DATA_HOST, discoveryPort))
                .readerGroup(
                    ReaderGroupConfig.builder("readers")
                        .dataSetReader(
                            DataSetReaderConfig.builder("reader")
                                .publisherId(PUBLISHER_ID)
                                .writerGroupId(WRITER_GROUP_ID)
                                .dataSetWriterId(DATA_SET_WRITER_ID)
                                .dataSetMetaData(metaData)
                                .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                .subscribedDataSet(targetVariables)
                                .build())
                        .build())
                .build())
        .build();
  }

  private void onDataSetReceived(DataSetReceivedEvent event) {
    String fields =
        event.fieldsByName().entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue().value().value())
            .collect(Collectors.joining(", "));

    logger.info(
        "[received] publisherId={} writerGroupId={} dataSetWriterId={} fields: {}",
        event.publisherId(),
        event.writerGroupId(),
        event.dataSetWriterId(),
        fields);
  }

  /**
   * Log the current values of the target nodes: the values the DataSetListener logged as
   * "[received]" land here via the TargetVariables mapping, status and timestamp included.
   */
  private void logTargetNodes() {
    DataValue temperature = temperatureNode.getValue();
    DataValue counter = counterNode.getValue();

    logger.info(
        "[node] Temperature={} (status={}, sourceTime={}) Counter={} (status={}, sourceTime={})",
        temperature.value().value(),
        temperature.statusCode(),
        temperature.sourceTime(),
        counter.value().value(),
        counter.statusCode(),
        counter.sourceTime());

    Map<String, Long> targetWriteErrors = serverPubSub.targetWriteErrors();
    if (!targetWriteErrors.isEmpty()) {
      logger.warn("targetWriteErrors: {}", targetWriteErrors);
    }
  }

  /** A namespace hosting the writable target variables under the Objects folder. */
  private static class DemoNamespace extends ManagedNamespaceWithLifecycle {

    private static final String NAMESPACE_URI = "urn:eclipse:milo:examples:pubsub:target";

    DemoNamespace(OpcUaServer server) {
      super(server, NAMESPACE_URI);
    }

    UaVariableNode addTargetVariable(String name, NodeId dataTypeId, Variant initialValue) {
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

      // The Part 14 Table 80 Disabled-handling and state-change rows write null Variants;
      // without allowNulls the server's AttributeWriter rejects them with Bad_TypeMismatch.
      node.setAllowNulls(true);

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
