/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldSelector;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariableConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariablesConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Live diagnostics values against a loopback UDP runtime: counter node values (WG
 * SentNetworkMessages, RG ReceivedNetworkMessages, DSR FailedDataSetMessages) equal the clamped
 * collector snapshot (the pull model serves {@code PubSubDiagnostics.component(path)} per read),
 * TimeFirstChange appears once a counter leaves zero, the DSR MessageSequenceNumber follows
 * deliveries, the Major/MinorVersion LiveValues match the configured metadata, the DSW
 * MessageSequenceNumber serves the NEXT sequence value (D4), ResolvedAddress serves the loopback
 * URL (R15 — an IP-literal URL resolves to itself), and the Configured and Operational counts
 * follow the runtime state at both the fragment group scope and the ns0 root LiveValues (the
 * root-goes-Operational half of the {@link PubSubDiagnosticsNs0Test} root-count row lives here,
 * where the loopback fixture is).
 *
 * <p>Fixture shape copied from {@link PubSubInfoModelLiveStateTest}: the exposed runtime publishes
 * one node-backed dataset and reads from a standalone external publisher. Network safety: unicast
 * 127.0.0.1 with ephemeral ports and an explicit loopback {@code discoveryAddress}.
 */
class PubSubDiagnosticsValuesTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final String CONNECTION = "dv-conn";
  private static final String WRITER_GROUP = "wgrp";
  private static final String WRITER = "writer";
  private static final String READER_GROUP = "rgrp";
  private static final String READER = "reader";
  private static final String DATA_SET = "dv-ds";
  private static final String EXTERNAL_DATA_SET = "dv-ext-ds";

  private static final PublisherId LOCAL_PUBLISHER_ID = PublisherId.uint16(ushort(4791));
  private static final PublisherId EXTERNAL_PUBLISHER_ID = PublisherId.uint16(ushort(4792));
  private static final UShort EXTERNAL_GROUP_ID = ushort(1);
  private static final UShort EXTERNAL_WRITER_ID = ushort(1);

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

  private static final UadpDataSetWriterSettings WRITER_SETTINGS =
      UadpDataSetWriterSettings.builder()
          .dataSetMessageContentMask(
              UadpDataSetMessageContentMask.of(
                  UadpDataSetMessageContentMask.Field.Timestamp,
                  UadpDataSetMessageContentMask.Field.MajorVersion,
                  UadpDataSetMessageContentMask.Field.MinorVersion,
                  UadpDataSetMessageContentMask.Field.SequenceNumber))
          .build();

  private static final DataSetFieldContentMask FIELD_MASK =
      DataSetFieldContentMask.of(DataSetFieldContentMask.Field.StatusCode);

  private static TestPubSubServer testServer;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();
  private final List<PubSubService> services = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  @AfterEach
  void tearDown() throws InterruptedException {
    for (ServerPubSub serverPubSub : attached) {
      serverPubSub.close();
    }
    attached.clear();

    for (PubSubService service : services) {
      try {
        service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        // best effort cleanup; failures are reported by the tests themselves
      }
    }
    services.clear();
  }

  @Test
  void diagnosticsValuesTrackTheRuntime() throws Exception {
    int dataPort = freeUdpPort();

    NodeId sourceNodeId =
        testServer.addVariable("DV_Source", NodeIds.Double, new DataValue(Variant.ofDouble(1.5)));
    NodeId targetNodeId =
        testServer.addVariable("DV_Target", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));

    PubSubConfig config = serverConfig(dataPort, sourceNodeId, targetNodeId);

    ServerPubSub serverPubSub =
        track(
            ServerPubSub.attach(
                testServer.getServer(),
                config,
                ServerPubSubOptions.builder()
                    .exposeInformationModel(true)
                    .diagnosticsEnabled(true)
                    .build()));

    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    String groupPath = CONNECTION + "/" + WRITER_GROUP;
    String writerPath = groupPath + "/" + WRITER;
    String readerPath = CONNECTION + "/" + READER_GROUP + "/" + READER;

    String groupDiagnostics = "PubSub/" + groupPath + "/Diagnostics";
    String writerDiagnostics = "PubSub/" + writerPath + "/Diagnostics";
    String readerDiagnostics = "PubSub/" + readerPath + "/Diagnostics";
    String readerGroupDiagnostics = "PubSub/" + CONNECTION + "/" + READER_GROUP + "/Diagnostics";

    // the publishing group's SentNetworkMessages leaves zero...
    awaitNodeValue(
        groupDiagnostics + "/Counters/SentNetworkMessages",
        value -> value instanceof UInteger sent && sent.longValue() > 0,
        "SentNetworkMessages > 0");

    // ...serving exactly the clamped collector value (pull model; poll until no tick lands
    // between the node read and the snapshot read)
    awaitNodeValue(
        groupDiagnostics + "/Counters/SentNetworkMessages",
        value ->
            value.equals(
                PubSubDiagnosticsExposure.clampUInt32(
                    diagnostics(serverPubSub, groupPath).networkMessagesSent())),
        "node value == clamped collector value");

    // TimeFirstChange records the first departure from zero; an untouched counter stays null
    awaitNodeValue(
        groupDiagnostics + "/Counters/SentNetworkMessages/TimeFirstChange",
        value -> value instanceof DateTime,
        "TimeFirstChange != null");
    assertEquals(
        null,
        fragmentNodeValue(groupDiagnostics + "/Counters/FailedTransmissions/TimeFirstChange"));

    // Total* roll up the object's own counters by classification
    awaitNodeValue(
        groupDiagnostics + "/TotalInformation",
        value -> value instanceof UInteger total && total.longValue() > 0,
        "TotalInformation > 0");
    assertEquals(false, fragmentNodeValue("PubSub/" + CONNECTION + "/Diagnostics/SubError"));

    // the external publisher feeds the reader side
    PubSubService publisher = track(externalPublisher(dataPort));
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    awaitNodeValue(
        readerGroupDiagnostics + "/Counters/ReceivedNetworkMessages",
        value -> value instanceof UInteger received && received.longValue() > 0,
        "ReceivedNetworkMessages > 0");

    // DSR MessageSequenceNumber appears with the first delivery (null before it)
    awaitNodeValue(
        readerDiagnostics + "/LiveValues/MessageSequenceNumber",
        value -> value instanceof UShort,
        "DSR MessageSequenceNumber != null");

    // DSR FailedDataSetMessages serves the clamped collector snapshot — stably zero here
    // because the confirmed reader decodes every delivery (the DECODE_ERRORS mapping behind
    // the row is unit-pinned in PubSubDiagnosticsComputationTest)
    assertEquals(
        PubSubDiagnosticsExposure.clampUInt32(diagnostics(serverPubSub, readerPath).decodeErrors()),
        fragmentNodeValue(readerDiagnostics + "/Counters/FailedDataSetMessages"));
    assertEquals(uint(0), fragmentNodeValue(readerDiagnostics + "/Counters/FailedDataSetMessages"));

    // DSR Major/MinorVersion serve the effective (configured) metadata version
    ConfigurationVersionDataType readerVersion = configuredReaderVersion(config);
    assertEquals(
        readerVersion.getMajorVersion(),
        fragmentNodeValue(readerDiagnostics + "/LiveValues/MajorVersion"));
    assertEquals(
        readerVersion.getMinorVersion(),
        fragmentNodeValue(readerDiagnostics + "/LiveValues/MinorVersion"));

    // DSW MessageSequenceNumber serves the NEXT sequence value (>= 1 once publishing, D4)
    awaitNodeValue(
        writerDiagnostics + "/LiveValues/MessageSequenceNumber",
        value -> value instanceof UShort next && next.intValue() >= 1,
        "DSW MessageSequenceNumber >= 1");

    // DSW Major/MinorVersion serve the referenced PublishedDataSet's metadata version
    ConfigurationVersionDataType writerVersion = configuredDataSetVersion(config);
    assertEquals(
        writerVersion.getMajorVersion(),
        fragmentNodeValue(writerDiagnostics + "/LiveValues/MajorVersion"));
    assertEquals(
        writerVersion.getMinorVersion(),
        fragmentNodeValue(writerDiagnostics + "/LiveValues/MinorVersion"));

    // ResolvedAddress = the configured loopback URL (an IP literal resolves to itself)
    assertEquals(
        configuredAddressUrl(config),
        fragmentNodeValue("PubSub/" + CONNECTION + "/Diagnostics/LiveValues/ResolvedAddress"));

    // Configured*/Operational* counts follow the registry and the state listener
    assertEquals(
        ushort(1), fragmentNodeValue(groupDiagnostics + "/LiveValues/ConfiguredDataSetWriters"));
    awaitNodeValue(
        groupDiagnostics + "/LiveValues/OperationalDataSetWriters",
        ushort(1)::equals,
        "OperationalDataSetWriters == 1");

    // ...and the ns0 root LiveValues count Operational components across the whole runtime
    // (this is the loopback-Operational half of the PubSubDiagnosticsNs0Test root-count row,
    // hosted here where the loopback fixture lives)
    awaitNodeValue(
        NodeIds.PublishSubscribe_Diagnostics_LiveValues_OperationalDataSetWriters,
        ushort(1)::equals,
        "root OperationalDataSetWriters == 1");
    awaitNodeValue(
        NodeIds.PublishSubscribe_Diagnostics_LiveValues_OperationalDataSetReaders,
        ushort(1)::equals,
        "root OperationalDataSetReaders == 1");

    // ...and drop when the component is disabled through the runtime
    serverPubSub
        .runtime()
        .components()
        .writerGroup(CONNECTION, WRITER_GROUP)
        .ifPresent(handle -> serverPubSub.runtime().disable(handle));

    awaitNodeValue(
        groupDiagnostics + "/LiveValues/OperationalDataSetWriters",
        ushort(0)::equals,
        "OperationalDataSetWriters == 0 after disable");
    assertEquals(
        ushort(1), fragmentNodeValue(groupDiagnostics + "/LiveValues/ConfiguredDataSetWriters"));
  }

  // region fixtures

  private static PubSubConfig serverConfig(int dataPort, NodeId sourceNodeId, NodeId targetNodeId)
      throws SocketException {

    var fieldId = new UUID(0L, 0xF1L);

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(DATA_SET)
            .field(
                FieldDefinition.builder("value")
                    .source(nodeAddress(sourceNodeId))
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(fieldId)
                    .build())
            .build();

    TargetVariablesConfig targetVariables =
        TargetVariablesConfig.builder()
            .map(
                FieldSelector.byName("v"),
                TargetVariableConfig.builder().target(nodeAddress(targetNodeId)).build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(LOCAL_PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", dataPort))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(
                    WriterGroupConfig.builder(WRITER_GROUP)
                        .writerGroupId(ushort(21))
                        .publishingInterval(Duration.ofMillis(100))
                        .messageSettings(GROUP_SETTINGS)
                        .dataSetWriter(
                            DataSetWriterConfig.builder(WRITER)
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(31))
                                .fieldContentMask(FIELD_MASK)
                                .settings(WRITER_SETTINGS)
                                .build())
                        .build())
                .readerGroup(
                    ReaderGroupConfig.builder(READER_GROUP)
                        .dataSetReader(
                            DataSetReaderConfig.builder(READER)
                                .publisherId(EXTERNAL_PUBLISHER_ID)
                                .writerGroupId(EXTERNAL_GROUP_ID)
                                .dataSetWriterId(EXTERNAL_WRITER_ID)
                                .dataSetMetaData(externalMetaData())
                                .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                .subscribedDataSet(targetVariables)
                                .build())
                        .build())
                .build())
        .build();
  }

  /** A standalone publisher service whose writer matches the exposed reader's filters. */
  private static PubSubService externalPublisher(int dataPort) throws SocketException {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(EXTERNAL_DATA_SET)
            .field(
                FieldDefinition.builder("v")
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(externalFieldId())
                    .build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("dv-pub")
                    .publisherId(EXTERNAL_PUBLISHER_ID)
                    .address(UdpDatagramAddress.unicast("127.0.0.1", dataPort))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(EXTERNAL_GROUP_ID)
                            .publishingInterval(Duration.ofMillis(75))
                            .messageSettings(GROUP_SETTINGS)
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(EXTERNAL_WRITER_ID)
                                    .fieldContentMask(FIELD_MASK)
                                    .settings(WRITER_SETTINGS)
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(
                dataSet.ref(),
                context -> {
                  DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
                  builder.field("v", new DataValue(Variant.ofDouble(3.5)));
                  return builder.build();
                })
            .build();

    return PubSubService.create(config, bindings);
  }

  private static DataSetMetaDataConfig externalMetaData() {
    return DataSetMetaDataConfig.builder(EXTERNAL_DATA_SET)
        .field("v", NodeIds.Double, externalFieldId())
        .build();
  }

  private static UUID externalFieldId() {
    return new UUID(0L, 0xF2L);
  }

  private static NodeFieldAddress nodeAddress(NodeId nodeId) {
    return NodeFieldAddress.of(
        nodeId, AttributeId.Value, testServer.getServer().getNamespaceTable());
  }

  // endregion

  // region helpers

  private ServerPubSub track(ServerPubSub serverPubSub) {
    attached.add(serverPubSub);
    return serverPubSub;
  }

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  private static ComponentDiagnostics diagnostics(ServerPubSub serverPubSub, String path) {
    return serverPubSub
        .runtime()
        .diagnostics()
        .component(path)
        .orElseThrow(() -> new AssertionError("expected collector entry for: " + path));
  }

  private static ConfigurationVersionDataType configuredReaderVersion(PubSubConfig config) {
    PubSubConfiguration2DataType configuration = wireForm(config);

    PubSubConnectionDataType connection =
        PubSubInfoModelFragment.findConnection(configuration, CONNECTION);
    DataSetReaderDataType reader =
        PubSubInfoModelFragment.findDataSetReader(
            Objects.requireNonNull(
                PubSubInfoModelFragment.findReaderGroup(connection, READER_GROUP)),
            READER);

    DataSetMetaDataType metaData = Objects.requireNonNull(reader).getDataSetMetaData();
    return Objects.requireNonNull(metaData).getConfigurationVersion();
  }

  private static ConfigurationVersionDataType configuredDataSetVersion(PubSubConfig config) {
    PublishedDataSetDataType dataSet =
        PubSubInfoModelFragment.findPublishedDataSet(wireForm(config), DATA_SET);

    DataSetMetaDataType metaData = Objects.requireNonNull(dataSet).getDataSetMetaData();
    return Objects.requireNonNull(metaData).getConfigurationVersion();
  }

  private static String configuredAddressUrl(PubSubConfig config) {
    PubSubConnectionDataType connection =
        PubSubInfoModelFragment.findConnection(wireForm(config), CONNECTION);

    if (Objects.requireNonNull(connection).getAddress() instanceof NetworkAddressUrlDataType url) {
      return Objects.requireNonNull(url.getUrl());
    }
    throw new AssertionError("expected a NetworkAddressUrlDataType address");
  }

  private static PubSubConfiguration2DataType wireForm(PubSubConfig config) {
    return config.toDataType(testServer.getServer().getNamespaceTable());
  }

  /** The filtered value of the fragment variable node with {@code identifier}. */
  private static Object fragmentNodeValue(String identifier) {
    var nodeId =
        new NodeId(testServer.getServer().getServerNamespace().getNamespaceIndex(), identifier);

    UaVariableNode node =
        (UaVariableNode)
            testServer
                .getServer()
                .getAddressSpaceManager()
                .getManagedNode(nodeId)
                .orElseThrow(() -> new AssertionError("expected node: " + nodeId));

    return node.getValue().getValue().getValue();
  }

  /** Poll the value of the fragment variable node until it matches {@code predicate}. */
  private static void awaitNodeValue(
      String identifier, Predicate<Object> predicate, String description)
      throws InterruptedException {

    var nodeId =
        new NodeId(testServer.getServer().getServerNamespace().getNamespaceIndex(), identifier);

    awaitNodeValue(nodeId, predicate, description);
  }

  /** Poll the value of the variable node with {@code nodeId} until it matches {@code predicate}. */
  private static void awaitNodeValue(NodeId nodeId, Predicate<Object> predicate, String description)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    Object lastValue = null;
    while (true) {
      UaVariableNode node =
          (UaVariableNode)
              testServer.getServer().getAddressSpaceManager().getManagedNode(nodeId).orElse(null);

      if (node != null) {
        lastValue = node.getValue().getValue().getValue();
        if (predicate.test(lastValue)) {
          return;
        }
      }

      if (System.nanoTime() >= deadline) {
        fail(
            "timed out waiting for "
                + description
                + " on "
                + nodeId
                + "; last value: "
                + lastValue);
      }
      Thread.sleep(25);
    }
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
