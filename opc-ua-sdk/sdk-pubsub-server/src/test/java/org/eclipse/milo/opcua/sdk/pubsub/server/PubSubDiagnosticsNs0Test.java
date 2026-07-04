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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubCounter;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.junit.jupiter.api.Test;

/**
 * The ns0 halves of the diagnostics exposure: the loader-built root Diagnostics subtree ({@code
 * i=17409}) backed with values/filters and restored at shutdown (never minted, values and behavior
 * only; the loader omits the Optional TimeFirstChange counter properties, asserted absent), the
 * fragment-local root State* counters (ticked through the test seam — nothing ticks them in
 * production because the root Status Enable/Disable pair is not minted), and the {@code
 * PubSubCapablities} population, which is gated by {@code exposeInformationModel} alone,
 * independent of {@code diagnosticsEnabled}.
 *
 * <p>Root LiveValues here cover the Configured* counts on a never-published fixture; the
 * go-Operational-with-a-loopback-publisher half of that row is hosted in {@link
 * PubSubDiagnosticsValuesTest}, which owns the loopback fixture.
 *
 * <p>Each test uses a fresh {@link TestPubSubServer}: the ns0 values are server-global state.
 */
class PubSubDiagnosticsNs0Test {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "dn-conn";

  @Test
  void ns0RootIsBackedWhileActiveAndRestoredAtShutdown() throws Exception {
    try (TestPubSubServer testServer = TestPubSubServer.create()) {
      OpcUaServer server = testServer.getServer();

      ServerPubSub serverPubSub =
          ServerPubSub.attach(
              server,
              readerOnlyConfig(),
              ServerPubSubOptions.builder()
                  .exposeInformationModel(true)
                  .diagnosticsEnabled(true)
                  .build());
      try {
        serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // object-level DiagnosticsLevel is read-only Basic
        assertEquals(
            DiagnosticsLevel.Basic,
            ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_DiagnosticsLevel));

        // the root counters serve zero with the Table 311 properties
        assertEquals(
            uint(0), ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_TotalInformation));
        assertEquals(uint(0), ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_TotalError));
        assertEquals(
            true, ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_TotalError_Active));
        assertEquals(
            PubSubDiagnosticsCounterClassification.Error,
            ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_TotalError_Classification));

        assertEquals(
            uint(0), ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_Counters_StateError));
        assertEquals(
            PubSubDiagnosticsCounterClassification.Error,
            ns0Value(
                server, NodeIds.PublishSubscribe_Diagnostics_Counters_StateError_Classification));
        assertEquals(
            DiagnosticsLevel.Basic,
            ns0Value(
                server, NodeIds.PublishSubscribe_Diagnostics_Counters_StateError_DiagnosticsLevel));
        // the loader-built ns0 Diagnostics instance omits the Optional TimeFirstChange counter
        // properties; they have no ns0 surface (the fragment-minted
        // per-component counters still expose TimeFirstChange)
        assertTrue(
            server
                .getAddressSpaceManager()
                .getManagedNode(
                    NodeIds.PublishSubscribe_Diagnostics_Counters_StateError_TimeFirstChange)
                .isEmpty());
        assertTrue(
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.PublishSubscribe_Diagnostics_TotalError_TimeFirstChange)
                .isEmpty());

        assertEquals(false, ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_SubError));

        // root LiveValues count the registered components (one reader, no writers)
        assertEquals(
            ushort(0),
            ns0Value(
                server, NodeIds.PublishSubscribe_Diagnostics_LiveValues_ConfiguredDataSetWriters));
        assertEquals(
            ushort(1),
            ns0Value(
                server, NodeIds.PublishSubscribe_Diagnostics_LiveValues_ConfiguredDataSetReaders));

        // i=17421 carries a working handler while the exposure is active
        UaMethodNode resetNode =
            (UaMethodNode)
                server
                    .getAddressSpaceManager()
                    .getManagedNode(NodeIds.PublishSubscribe_Diagnostics_Reset)
                    .orElseThrow();
        assertNotSame(MethodInvocationHandler.NOT_IMPLEMENTED, resetNode.getInvocationHandler());

        // a fragment-local root tick surfaces on the ns0 counter and the root TotalError
        // roll-up
        PubSubInfoModelFragment fragment = serverPubSub.infoModelFragment();
        assertNotNull(fragment);
        PubSubDiagnosticsExposure exposure = fragment.diagnosticsExposure();
        assertNotNull(exposure);
        exposure.tickRootStateCounter(PubSubCounter.STATE_ERROR);

        assertEquals(
            uint(1), ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_Counters_StateError));
        assertEquals(uint(1), ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_TotalError));

        // shutdown removes the filters and statics and restores the loader posture
        serverPubSub.shutdown().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertNull(ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_DiagnosticsLevel));
        assertNull(ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_TotalInformation));
        assertNull(ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_Counters_StateError));
        assertNull(
            ns0Value(server, NodeIds.PublishSubscribe_Diagnostics_Counters_StateError_Active));
        assertNull(
            ns0Value(
                server, NodeIds.PublishSubscribe_Diagnostics_LiveValues_ConfiguredDataSetReaders));
        assertSame(MethodInvocationHandler.NOT_IMPLEMENTED, resetNode.getInvocationHandler());
      } finally {
        serverPubSub.close();
      }
    }
  }

  @Test
  void capabilitiesArePopulatedWheneverTheModelIsExposed() throws Exception {
    try (TestPubSubServer testServer = TestPubSubServer.create()) {
      OpcUaServer server = testServer.getServer();

      // diagnosticsEnabled is deliberately FALSE: capabilities ride exposeInformationModel alone
      ServerPubSub serverPubSub =
          ServerPubSub.attach(
              server,
              readerOnlyConfig(),
              ServerPubSubOptions.builder().exposeInformationModel(true).build());
      try {
        serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        // every Max* is 0 = no limit; the ReserveIds allocator bounds ids, not counts
        NodeId[] maxProperties = {
          NodeIds.PublishSubscribe_PubSubCapablities_MaxPubSubConnections,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxWriterGroups,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxReaderGroups,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxDataSetWriters,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxDataSetReaders,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxFieldsPerDataSet,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxDataSetWritersPerGroup,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxNetworkMessageSizeDatagram,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxNetworkMessageSizeBroker,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxSecurityGroups,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxPushTargets,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxPublishedDataSets,
          NodeIds.PublishSubscribe_PubSubCapablities_MaxStandaloneSubscribedDataSets,
        };
        for (NodeId nodeId : maxProperties) {
          assertEquals(uint(0), ns0Value(server, nodeId), nodeId.toString());
        }

        // pull support exists engine-side regardless of the SKS helper; push is not implemented;
        // Server tracks the securityKeyServerEnabled option (false here)
        assertEquals(
            true,
            ns0Value(server, NodeIds.PublishSubscribe_PubSubCapablities_SupportSecurityKeyPull));
        assertEquals(
            false,
            ns0Value(server, NodeIds.PublishSubscribe_PubSubCapablities_SupportSecurityKeyPush));
        assertEquals(
            false,
            ns0Value(server, NodeIds.PublishSubscribe_PubSubCapablities_SupportSecurityKeyServer));
      } finally {
        serverPubSub.close();
      }
    }
  }

  // region fixtures

  /** A reader-only config: ns0 backing needs no published datasets or writers. */
  private static PubSubConfig readerOnlyConfig() throws SocketException {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("dn-ds")
            .field("value", NodeIds.Double, new UUID(0L, 0xA1L))
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(
                    ReaderGroupConfig.builder("rgrp")
                        .dataSetReader(
                            DataSetReaderConfig.builder("reader")
                                .publisherId(PublisherId.uint16(ushort(4781)))
                                .writerGroupId(ushort(1))
                                .dataSetWriterId(ushort(1))
                                .dataSetMetaData(metaData)
                                .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                .build())
                        .build())
                .build())
        .build();
  }

  // endregion

  // region helpers

  /** The filtered value of the ns0 variable node with {@code nodeId}. */
  private static Object ns0Value(OpcUaServer server, NodeId nodeId) {
    UaVariableNode node =
        (UaVariableNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(nodeId)
                .orElseThrow(() -> new AssertionError("expected ns0 node: " + nodeId));
    return node.getValue().getValue().getValue();
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
