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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.udp.UdpTransportProvider;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.junit.jupiter.api.Test;

/**
 * Population of the existing ns0 PublishSubscribe nodes: with {@code exposeInformationModel(true)},
 * startup sets SupportedTransportProfiles ({@code i=17481}), Status/State ({@code i=17406}),
 * ConfigurationVersion ({@code i=25481}), and ConfigurationProperties ({@code i=32404}); shutdown
 * sets the root state Disabled. With the default {@code exposeInformationModel(false)}, the ns0
 * null skeleton is left untouched and no fragment nodes exist.
 *
 * <p>Each test uses a fresh {@link TestPubSubServer} because the ns0 PublishSubscribe values are
 * server-global state that earlier exposures would otherwise leak into later assertions.
 *
 * <p>Network safety: connections use unicast 127.0.0.1 with ephemeral ports and an explicit
 * loopback {@code discoveryAddress}, so the engine's discovery channels never touch the default
 * multicast group 224.0.2.14:4840.
 */
class PubSubInfoModelNs0Test {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "ns0-conn";

  @Test
  void ns0ValuesArePopulatedAtStartupAndRootStateIsDisabledAtShutdown() throws Exception {
    try (TestPubSubServer testServer = TestPubSubServer.create()) {
      var propertyName = new QualifiedName(0, "VendorProperty");

      PubSubConfig config =
          readerOnlyConfig().toBuilder().property(propertyName, Variant.ofString("milo")).build();

      ServerPubSub serverPubSub =
          ServerPubSub.attach(
              testServer.getServer(),
              config,
              ServerPubSubOptions.builder().exposeInformationModel(true).build());
      try {
        serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        OpcUaServer server = testServer.getServer();

        // SupportedTransportProfiles i=17481 contains the UDP UADP profile URI
        String[] profiles =
            (String[]) ns0Value(server, NodeIds.PublishSubscribe_SupportedTransportProfiles);
        assertTrue(
            Arrays.asList(profiles).contains(UdpTransportProvider.TRANSPORT_PROFILE_URI),
            "expected the UDP UADP profile URI in " + Arrays.toString(profiles));

        // Status/State i=17406 is Operational for an enabled configuration
        assertEquals(
            PubSubState.Operational, ns0Value(server, NodeIds.PublishSubscribe_Status_State));

        // ConfigurationProperties i=32404 carries the configured properties
        KeyValuePair[] properties =
            (KeyValuePair[]) ns0Value(server, NodeIds.PublishSubscribe_ConfigurationProperties);
        assertTrue(
            Arrays.asList(properties)
                .contains(new KeyValuePair(propertyName, Variant.ofString("milo"))),
            "expected the configured property in " + Arrays.toString(properties));

        // ConfigurationVersion i=25481 is an exposure-owned VersionTime
        UInteger configurationVersion =
            assertInstanceOf(
                UInteger.class, ns0Value(server, NodeIds.PublishSubscribe_ConfigurationVersion));
        assertTrue(configurationVersion.longValue() > 0L, "VersionTime must be positive");

        // shutdown sets the root state Disabled
        serverPubSub.shutdown().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertEquals(PubSubState.Disabled, ns0Value(server, NodeIds.PublishSubscribe_Status_State));
      } finally {
        serverPubSub.close();
      }
    }
  }

  @Test
  void disabledConfigurationShowsRootStateDisabled() throws Exception {
    try (TestPubSubServer testServer = TestPubSubServer.create()) {
      PubSubConfig config = readerOnlyConfig().toBuilder().enabled(false).build();

      ServerPubSub serverPubSub =
          ServerPubSub.attach(
              testServer.getServer(),
              config,
              ServerPubSubOptions.builder().exposeInformationModel(true).build());
      try {
        serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        assertEquals(
            PubSubState.Disabled,
            ns0Value(testServer.getServer(), NodeIds.PublishSubscribe_Status_State));
      } finally {
        serverPubSub.close();
      }
    }
  }

  @Test
  void exposeInformationModelFalseLeavesNs0UntouchedAndBuildsNoNodes() throws Exception {
    // exposing the information model is opt-in; the default is false
    assertFalse(ServerPubSubOptions.builder().build().isExposeInformationModel());

    try (TestPubSubServer testServer = TestPubSubServer.create()) {
      ServerPubSub serverPubSub = ServerPubSub.attach(testServer.getServer(), readerOnlyConfig());
      try {
        serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        OpcUaServer server = testServer.getServer();

        // no "PubSub/..." nodes exist
        NodeId connectionNodeId =
            new NodeId(server.getServerNamespace().getNamespaceIndex(), "PubSub/" + CONNECTION);
        assertTrue(
            server.getAddressSpaceManager().getManagedNode(connectionNodeId).isEmpty(),
            "no fragment nodes may exist with exposeInformationModel(false)");

        // and i=14443 gained no HasPubSubConnection references
        assertTrue(
            server.getAddressSpaceManager().getManagedReferences(NodeIds.PublishSubscribe).stream()
                .noneMatch(
                    r ->
                        r.isForward()
                            && r.getReferenceTypeId().equals(NodeIds.HasPubSubConnection)),
            "no HasPubSubConnection references may exist with exposeInformationModel(false)");

        // the ns0 skeleton values stay null
        assertNull(ns0Value(server, NodeIds.PublishSubscribe_SupportedTransportProfiles));
        assertNull(ns0Value(server, NodeIds.PublishSubscribe_Status_State));
        assertNull(ns0Value(server, NodeIds.PublishSubscribe_ConfigurationVersion));
        assertNull(ns0Value(server, NodeIds.PublishSubscribe_ConfigurationProperties));
      } finally {
        serverPubSub.close();
      }
    }
  }

  // region fixtures

  /** A reader-only config: ns0 population needs no published datasets or writers. */
  private static PubSubConfig readerOnlyConfig() throws SocketException {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("ns0-ds")
            .field("value", NodeIds.Double, new UUID(0L, 0x91L))
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
                                .publisherId(PublisherId.uint16(ushort(4761)))
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

  /** The unwrapped Variant value of the ns0 variable node with {@code nodeId}. */
  private static Object ns0Value(OpcUaServer server, NodeId nodeId) {
    UaVariableNode node =
        (UaVariableNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(nodeId)
                .orElseThrow(() -> new AssertionError("expected ns0 node: " + nodeId));
    return node.getValue().value().value();
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
