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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.junit.jupiter.api.Test;

/**
 * The §9.1.11 per-component Diagnostics subtree structure minted by {@link
 * PubSubDiagnosticsExposure} (R13/R18): type definitions per subtype, counter nodes typed
 * PubSubDiagnosticsCounterType ({@code i=19725}) with the §4.3 Active/Classification/
 * DiagnosticsLevel properties, the LiveValue presence rules (token nodes only on secured groups —
 * D47; no StatusCode, no ReceivedInvalidNetworkMessages — R14), deterministic {@code
 * "PubSub/<path>/Diagnostics/..."} NodeId strings, and the {@code diagnosticsEnabled=false} gate
 * (no Diagnostics nodes, ns0 root untouched, {@code i=17421} stays {@code Bad_NotImplemented}).
 *
 * <p>Each test uses a fresh {@link TestPubSubServer}: the ns0 Diagnostics backing is server-global
 * state. All configured components are disabled — structure derives from configuration, not runtime
 * state — so no key material or dataset sources are needed.
 *
 * <p>Network safety: connections use unicast 127.0.0.1 with ephemeral ports and an explicit
 * loopback {@code discoveryAddress}.
 */
class PubSubDiagnosticsNodeTreeTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "dt-conn";
  private static final String SECURED_WRITER_GROUP = "sgrp";
  private static final String PLAIN_WRITER_GROUP = "pgrp";
  private static final String SECURED_READER_GROUP = "srgrp";
  private static final String PLAIN_READER_GROUP = "rgrp";
  private static final String WRITER = "writer";
  private static final String READER = "reader";
  private static final String DATA_SET = "dt-ds";
  private static final String SECURITY_GROUP = "dt-sg";

  @Test
  void diagnosticsSubtreesFollowTheSpecShape() throws Exception {
    try (TestPubSubServer testServer = TestPubSubServer.create()) {
      ServerPubSub serverPubSub =
          ServerPubSub.attach(
              testServer.getServer(),
              treeConfig(),
              ServerPubSubOptions.builder()
                  .exposeInformationModel(true)
                  .diagnosticsEnabled(true)
                  .build());
      try {
        serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        OpcUaServer server = testServer.getServer();
        UShort idx = server.getServerNamespace().getNamespaceIndex();

        // one Diagnostics object per component, type-defined per §9.1.11.7-9.1.11.12
        assertTypeDefinition(
            server, idx, "PubSub/" + CONNECTION, NodeIds.PubSubDiagnosticsConnectionType);
        assertTypeDefinition(
            server,
            idx,
            "PubSub/" + CONNECTION + "/" + SECURED_WRITER_GROUP,
            NodeIds.PubSubDiagnosticsWriterGroupType);
        assertTypeDefinition(
            server,
            idx,
            "PubSub/" + CONNECTION + "/" + PLAIN_READER_GROUP,
            NodeIds.PubSubDiagnosticsReaderGroupType);
        assertTypeDefinition(
            server,
            idx,
            "PubSub/" + CONNECTION + "/" + SECURED_WRITER_GROUP + "/" + WRITER,
            NodeIds.PubSubDiagnosticsDataSetWriterType);
        assertTypeDefinition(
            server,
            idx,
            "PubSub/" + CONNECTION + "/" + PLAIN_READER_GROUP + "/" + READER,
            NodeIds.PubSubDiagnosticsDataSetReaderType);

        String securedGroupDiagnostics =
            "PubSub/" + CONNECTION + "/" + SECURED_WRITER_GROUP + "/Diagnostics";

        // base members (Table 310) under deterministic ids
        assertEquals(
            DiagnosticsLevel.Basic,
            nodeValue(server, idx, securedGroupDiagnostics + "/DiagnosticsLevel"));
        assertEquals(false, nodeValue(server, idx, securedGroupDiagnostics + "/SubError"));
        assertEquals(
            uint(0), nodeValue(server, idx, securedGroupDiagnostics + "/TotalInformation"));
        assertEquals(uint(0), nodeValue(server, idx, securedGroupDiagnostics + "/TotalError"));
        assertTrue(
            managedNode(server, new NodeId(idx, securedGroupDiagnostics + "/Reset"))
                instanceof UaMethodNode);

        // counter nodes are typed i=19725 with the §4.3 property rows
        String sent = securedGroupDiagnostics + "/Counters/SentNetworkMessages";
        assertTrue(
            hasForwardReference(
                server,
                new NodeId(idx, sent),
                NodeIds.HasTypeDefinition,
                NodeIds.PubSubDiagnosticsCounterType));
        assertEquals(uint(0), nodeValue(server, idx, sent));
        assertEquals(true, nodeValue(server, idx, sent + "/Active"));
        assertEquals(
            PubSubDiagnosticsCounterClassification.Information,
            nodeValue(server, idx, sent + "/Classification"));
        assertEquals(DiagnosticsLevel.Basic, nodeValue(server, idx, sent + "/DiagnosticsLevel"));
        assertNull(nodeValue(server, idx, sent + "/TimeFirstChange"));

        String failed = securedGroupDiagnostics + "/Counters/FailedTransmissions";
        assertEquals(
            PubSubDiagnosticsCounterClassification.Error,
            nodeValue(server, idx, failed + "/Classification"));

        // Advanced-level rows are served Active=true as documented surplus (D36)
        String encryption = securedGroupDiagnostics + "/Counters/EncryptionErrors";
        assertEquals(
            DiagnosticsLevel.Advanced, nodeValue(server, idx, encryption + "/DiagnosticsLevel"));
        assertEquals(true, nodeValue(server, idx, encryption + "/Active"));

        // the six Table 311 State* counters exist on every object. The enabled connection took
        // the engine's attach-time Disabled->Paused hop (built enabled while the root is not
        // yet operational), and every Paused entry counts as parent-caused (Table 311), so
        // StatePausedByParent reads 1; the other five never ticked
        for (String counter :
            new String[] {
              "StateError",
              "StateOperationalByMethod",
              "StateOperationalByParent",
              "StateOperationalFromError",
              "StateDisabledByMethod"
            }) {
          assertEquals(
              uint(0),
              nodeValue(server, idx, "PubSub/" + CONNECTION + "/Diagnostics/Counters/" + counter),
              counter);
        }
        assertEquals(
            uint(1),
            nodeValue(
                server, idx, "PubSub/" + CONNECTION + "/Diagnostics/Counters/StatePausedByParent"));

        // token LiveValues only on secured groups (D47)
        assertNodePresent(
            server, new NodeId(idx, securedGroupDiagnostics + "/LiveValues/SecurityTokenID"));
        assertNodePresent(
            server, new NodeId(idx, securedGroupDiagnostics + "/LiveValues/TimeToNextTokenID"));

        String plainGroupDiagnostics =
            "PubSub/" + CONNECTION + "/" + PLAIN_WRITER_GROUP + "/Diagnostics";
        assertNodeAbsent(
            server, new NodeId(idx, plainGroupDiagnostics + "/LiveValues/SecurityTokenID"));
        assertNodeAbsent(
            server, new NodeId(idx, plainGroupDiagnostics + "/LiveValues/TimeToNextTokenID"));

        // a reader inherits its GROUP's security posture
        String securedReaderDiagnostics =
            "PubSub/" + CONNECTION + "/" + SECURED_READER_GROUP + "/" + READER + "/Diagnostics";
        assertNodePresent(
            server, new NodeId(idx, securedReaderDiagnostics + "/LiveValues/SecurityTokenID"));

        String plainReaderDiagnostics =
            "PubSub/" + CONNECTION + "/" + PLAIN_READER_GROUP + "/" + READER + "/Diagnostics";
        assertNodeAbsent(
            server, new NodeId(idx, plainReaderDiagnostics + "/LiveValues/SecurityTokenID"));

        // DSW LiveValues: MessageSequenceNumber + Major/MinorVersion always minted (D47/D4);
        // the version values are static from the referenced PublishedDataSet's metadata
        String writerDiagnostics =
            "PubSub/" + CONNECTION + "/" + SECURED_WRITER_GROUP + "/" + WRITER + "/Diagnostics";
        assertNodePresent(
            server, new NodeId(idx, writerDiagnostics + "/LiveValues/MessageSequenceNumber"));

        ConfigurationVersionDataType version = configuredDataSetVersion(testServer);
        assertEquals(
            version.getMajorVersion(),
            nodeValue(server, idx, writerDiagnostics + "/LiveValues/MajorVersion"));
        assertEquals(
            version.getMinorVersion(),
            nodeValue(server, idx, writerDiagnostics + "/LiveValues/MinorVersion"));

        // omitted Optional rows stay omitted: StatusCode has no feed, and
        // ReceivedInvalidNetworkMessages cannot be attributed to a group (R14)
        assertNodeAbsent(server, new NodeId(idx, writerDiagnostics + "/LiveValues/StatusCode"));
        assertNodeAbsent(
            server, new NodeId(idx, plainReaderDiagnostics + "/LiveValues/StatusCode"));
        assertNodeAbsent(
            server,
            new NodeId(
                idx,
                "PubSub/"
                    + CONNECTION
                    + "/"
                    + PLAIN_READER_GROUP
                    + "/Diagnostics/Counters/ReceivedInvalidNetworkMessages"));

        // group-scoped LiveValue counts derive from the registry
        assertEquals(
            ushort(1),
            nodeValue(
                server, idx, securedGroupDiagnostics + "/LiveValues/ConfiguredDataSetWriters"));
        assertEquals(
            ushort(0),
            nodeValue(server, idx, plainGroupDiagnostics + "/LiveValues/ConfiguredDataSetWriters"));
        assertEquals(
            ushort(1),
            nodeValue(
                server,
                idx,
                "PubSub/"
                    + CONNECTION
                    + "/"
                    + PLAIN_READER_GROUP
                    + "/Diagnostics/LiveValues/ConfiguredDataSetReaders"));
      } finally {
        serverPubSub.close();
      }
    }
  }

  @Test
  void diagnosticsDisabledMintsNothingAndLeavesNs0Untouched() throws Exception {
    // diagnostics exposure is opt-in; the default is false
    assertFalse(ServerPubSubOptions.builder().build().isDiagnosticsEnabled());

    try (TestPubSubServer testServer = TestPubSubServer.create()) {
      ServerPubSub serverPubSub =
          ServerPubSub.attach(
              testServer.getServer(),
              treeConfig(),
              ServerPubSubOptions.builder().exposeInformationModel(true).build());
      try {
        serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        OpcUaServer server = testServer.getServer();
        UShort idx = server.getServerNamespace().getNamespaceIndex();

        // no per-component Diagnostics nodes
        assertNodeAbsent(server, new NodeId(idx, "PubSub/" + CONNECTION + "/Diagnostics"));
        assertNodeAbsent(
            server,
            new NodeId(idx, "PubSub/" + CONNECTION + "/" + SECURED_WRITER_GROUP + "/Diagnostics"));

        // the ns0 root subtree keeps its loader-null values
        assertNull(
            ((UaVariableNode)
                    managedNode(server, NodeIds.PublishSubscribe_Diagnostics_DiagnosticsLevel))
                .getValue()
                .getValue()
                .getValue());
        assertNull(
            ((UaVariableNode)
                    managedNode(server, NodeIds.PublishSubscribe_Diagnostics_TotalInformation))
                .getValue()
                .getValue()
                .getValue());

        // and the ns0 Reset stays unimplemented
        UaMethodNode resetNode =
            (UaMethodNode) managedNode(server, NodeIds.PublishSubscribe_Diagnostics_Reset);
        assertSame(MethodInvocationHandler.NOT_IMPLEMENTED, resetNode.getInvocationHandler());
      } finally {
        serverPubSub.close();
      }
    }
  }

  // region fixtures

  /**
   * One connection with a secured writer group (one writer), a plain writer group, a secured reader
   * group (one reader), and a plain reader group (one reader). Everything but the connection is
   * disabled: structure derives from configuration; disabled secured groups need no key material
   * and disabled writers need no dataset source.
   */
  private static PubSubConfig treeConfig() throws SocketException {
    var metaData =
        DataSetMetaDataConfig.builder(DATA_SET)
            .field("value", NodeIds.Double, new UUID(0L, 0xD1L))
            .build();

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(DATA_SET)
            .field(
                FieldDefinition.builder("value")
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(new UUID(0L, 0xD2L))
                    .build())
            .build();

    MessageSecurityConfig security =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.Sign)
            .securityGroup(new SecurityGroupRef(SECURITY_GROUP))
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .securityGroup(
            SecurityGroupConfig.builder(SECURITY_GROUP).keyLifeTime(Duration.ofMinutes(10)).build())
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4771)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(
                    WriterGroupConfig.builder(SECURED_WRITER_GROUP)
                        .enabled(false)
                        .writerGroupId(ushort(21))
                        .publishingInterval(Duration.ofMillis(100))
                        .messageSecurity(security)
                        .dataSetWriter(
                            DataSetWriterConfig.builder(WRITER)
                                .enabled(false)
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(31))
                                .build())
                        .build())
                .writerGroup(
                    WriterGroupConfig.builder(PLAIN_WRITER_GROUP)
                        .enabled(false)
                        .writerGroupId(ushort(22))
                        .publishingInterval(Duration.ofMillis(100))
                        .build())
                .readerGroup(
                    ReaderGroupConfig.builder(SECURED_READER_GROUP)
                        .enabled(false)
                        .messageSecurity(security)
                        .dataSetReader(readerConfig(metaData).toBuilder().enabled(false).build())
                        .build())
                .readerGroup(
                    ReaderGroupConfig.builder(PLAIN_READER_GROUP)
                        .enabled(false)
                        .dataSetReader(readerConfig(metaData).toBuilder().enabled(false).build())
                        .build())
                .build())
        .build();
  }

  private static DataSetReaderConfig readerConfig(DataSetMetaDataConfig metaData) {
    return DataSetReaderConfig.builder(READER)
        .publisherId(PublisherId.uint16(ushort(4772)))
        .writerGroupId(ushort(1))
        .dataSetWriterId(ushort(1))
        .dataSetMetaData(metaData)
        .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
        .build();
  }

  /** The ConfigurationVersion the wire form derives for the configured PublishedDataSet. */
  private static ConfigurationVersionDataType configuredDataSetVersion(TestPubSubServer testServer)
      throws SocketException {

    var configuration = treeConfig().toDataType(testServer.getServer().getNamespaceTable());

    var dataSet = PubSubInfoModelFragment.findPublishedDataSet(configuration, DATA_SET);
    if (dataSet == null || dataSet.getDataSetMetaData() == null) {
      throw new AssertionError("expected PublishedDataSet metadata in the wire form");
    }
    return dataSet.getDataSetMetaData().getConfigurationVersion();
  }

  // endregion

  // region helpers

  private static UaNode managedNode(OpcUaServer server, NodeId nodeId) {
    return server
        .getAddressSpaceManager()
        .getManagedNode(nodeId)
        .orElseThrow(() -> new AssertionError("expected node: " + nodeId));
  }

  private static Object nodeValue(OpcUaServer server, UShort idx, String identifier) {
    UaNode node = managedNode(server, new NodeId(idx, identifier));
    return ((UaVariableNode) node).getValue().getValue().getValue();
  }

  private static void assertNodePresent(OpcUaServer server, NodeId nodeId) {
    assertTrue(
        server.getAddressSpaceManager().getManagedNode(nodeId).isPresent(),
        "expected node: " + nodeId);
  }

  private static void assertNodeAbsent(OpcUaServer server, NodeId nodeId) {
    assertTrue(
        server.getAddressSpaceManager().getManagedNode(nodeId).isEmpty(),
        "expected NO node: " + nodeId);
  }

  private static void assertTypeDefinition(
      OpcUaServer server, UShort idx, String componentIdentifier, NodeId typeDefinitionId) {

    NodeId diagnosticsNodeId = new NodeId(idx, componentIdentifier + "/Diagnostics");
    assertNodePresent(server, diagnosticsNodeId);

    assertTrue(
        hasForwardReference(server, diagnosticsNodeId, NodeIds.HasTypeDefinition, typeDefinitionId),
        "expected HasTypeDefinition " + typeDefinitionId + " on " + diagnosticsNodeId);

    // grafted via HasComponent so the component's subtree delete cascades over it
    assertTrue(
        hasForwardReference(
            server, new NodeId(idx, componentIdentifier), NodeIds.HasComponent, diagnosticsNodeId),
        "expected HasComponent from " + componentIdentifier + " to its Diagnostics object");
  }

  private static boolean hasForwardReference(
      OpcUaServer server, NodeId sourceNodeId, NodeId referenceTypeId, NodeId target) {

    return server.getAddressSpaceManager().getManagedReferences(sourceNodeId).stream()
        .anyMatch(
            r ->
                r.isForward()
                    && r.getReferenceTypeId().equals(referenceTypeId)
                    && r.getTargetNodeId().equals(target.expanded()));
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
