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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Live State tracking: the fragment's Status/State variables follow the runtime's PubSubState
 * through {@code PubSubService.addStateListener}, keyed by component name path. A standalone
 * loopback publisher drives the exposed reader to Operational, and disabling components through
 * {@code runtime()} moves their State variables to Disabled.
 *
 * <p>Network safety: connections use unicast 127.0.0.1 with ephemeral ports and an explicit
 * loopback {@code discoveryAddress}, so the engine's discovery channels never touch the default
 * multicast group 224.0.2.14:4840.
 */
class PubSubInfoModelLiveStateTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final String CONNECTION = "lv-conn";
  private static final String WRITER_GROUP = "wgrp";
  private static final String WRITER = "writer";
  private static final String READER_GROUP = "rgrp";
  private static final String READER = "reader";
  private static final String DATA_SET = "lv-ds";
  private static final String EXTERNAL_DATA_SET = "lv-ext-ds";

  private static final PublisherId LOCAL_PUBLISHER_ID = PublisherId.uint16(ushort(4751));
  private static final PublisherId EXTERNAL_PUBLISHER_ID = PublisherId.uint16(ushort(4752));
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
  void stateVariablesTrackOperationalAndDisabledTransitions() throws Exception {
    int dataPort = freeUdpPort();

    NodeId sourceNodeId =
        testServer.addVariable("LV_Source", NodeIds.Double, new DataValue(Variant.ofDouble(1.5)));
    NodeId targetNodeId =
        testServer.addVariable("LV_Target", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));

    ServerPubSub serverPubSub =
        track(
            ServerPubSub.attach(
                testServer.getServer(),
                serverConfig(dataPort, sourceNodeId, targetNodeId),
                ServerPubSubOptions.builder().exposeInformationModel(true).build()));

    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // startup drives the enabled connection, writer group, and writer Operational, and the
    // listener-fed State variables follow
    awaitState("PubSub/" + CONNECTION, PubSubState.Operational);
    awaitState("PubSub/" + CONNECTION + "/" + WRITER_GROUP, PubSubState.Operational);
    awaitState("PubSub/" + CONNECTION + "/" + WRITER_GROUP + "/" + WRITER, PubSubState.Operational);
    awaitState("PubSub/" + CONNECTION + "/" + READER_GROUP, PubSubState.Operational);

    // the reader needs a key frame from the matching publisher to leave PreOperational
    PubSubService publisher = track(externalPublisher(dataPort));
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    String readerPath = "PubSub/" + CONNECTION + "/" + READER_GROUP + "/" + READER;
    awaitState(readerPath, PubSubState.Operational);

    // disabling a component through runtime() moves its State variable to Disabled
    PubSubHandle reader =
        serverPubSub
            .runtime()
            .components()
            .dataSetReader(CONNECTION, READER_GROUP, READER)
            .orElseThrow();
    serverPubSub.runtime().disable(reader);
    awaitState(readerPath, PubSubState.Disabled);

    PubSubHandle writerGroup =
        serverPubSub.runtime().components().writerGroup(CONNECTION, WRITER_GROUP).orElseThrow();
    serverPubSub.runtime().disable(writerGroup);
    awaitState("PubSub/" + CONNECTION + "/" + WRITER_GROUP, PubSubState.Disabled);

    // the connection is untouched by the component-level disables
    awaitState("PubSub/" + CONNECTION, PubSubState.Operational);
  }

  // region fixtures

  /**
   * The pinned exposure shape: one UDP connection with one writer group / writer publishing one
   * node-backed dataset and one reader group / reader with TargetVariables. The reader filters on
   * the external publisher's identifiers, so only the standalone publisher's frames match it.
   */
  private static PubSubConfig serverConfig(int dataPort, NodeId sourceNodeId, NodeId targetNodeId)
      throws SocketException {

    var fieldId = new UUID(0L, 0xE1L);

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
                PubSubConnectionConfig.udp("lv-pub")
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
    return new UUID(0L, 0xE2L);
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

  /** Poll the State variable under {@code componentIdentifier}/Status until it equals state. */
  private static void awaitState(String componentIdentifier, PubSubState state)
      throws InterruptedException {
    awaitNodeValue(componentIdentifier + "/Status/State", state::equals, "State == " + state);
  }

  /** Poll the value of the fragment variable node until it matches {@code predicate}. */
  private static void awaitNodeValue(
      String identifier, Predicate<Object> predicate, String description)
      throws InterruptedException {

    var nodeId =
        new NodeId(testServer.getServer().getServerNamespace().getNamespaceIndex(), identifier);

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    Object lastValue = null;
    while (true) {
      UaVariableNode node =
          (UaVariableNode)
              testServer.getServer().getAddressSpaceManager().getManagedNode(nodeId).orElse(null);

      if (node != null) {
        lastValue = node.getValue().value().value();
        if (predicate.test(lastValue)) {
          return;
        }
      }

      if (System.nanoTime() >= deadline) {
        fail(
            "timed out waiting for "
                + description
                + " on "
                + identifier
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
