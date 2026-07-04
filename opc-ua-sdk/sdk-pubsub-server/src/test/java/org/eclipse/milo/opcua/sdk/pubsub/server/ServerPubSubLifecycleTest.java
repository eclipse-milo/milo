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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * {@link ServerPubSub} lifecycle behavior: attach is legal any time after {@link
 * org.eclipse.milo.opcua.sdk.server.OpcUaServer} construction and startup never requires {@code
 * server.startup()}; startup, shutdown, and close are idempotent; close after a failed startup is
 * tolerated; a close that runs before startup marks the optional helpers stopped so a later startup
 * can never register them.
 *
 * <p>Network safety: connections use unicast 127.0.0.1 with ephemeral ports and an explicit
 * loopback {@code discoveryAddress}, so the engine's discovery channels never touch the default
 * multicast group 224.0.2.14:4840.
 */
class ServerPubSubLifecycleTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static TestPubSubServer testServer;

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  @Test
  void attachAndStartupWorkWithoutServerStartup() throws Exception {
    // the fixture server is constructed but never started: no endpoints are bound
    assertTrue(testServer.getServer().getBoundEndpoints().isEmpty());

    ServerPubSub serverPubSub = ServerPubSub.attach(testServer.getServer(), readerOnlyConfig());
    try {
      serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      PubSubHandle connection =
          serverPubSub.runtime().components().connection("lc-conn").orElseThrow();
      assertEquals(PubSubState.Operational, serverPubSub.runtime().state(connection));
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void startupIsIdempotent() throws Exception {
    ServerPubSub serverPubSub = ServerPubSub.attach(testServer.getServer(), readerOnlyConfig());
    try {
      ServerPubSub first = serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      ServerPubSub second = serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      assertSame(first, second);

      PubSubHandle connection =
          serverPubSub.runtime().components().connection("lc-conn").orElseThrow();
      assertEquals(PubSubState.Operational, serverPubSub.runtime().state(connection));
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void shutdownIsIdempotentAndCloseAfterShutdownIsTolerated() throws Exception {
    ServerPubSub serverPubSub = ServerPubSub.attach(testServer.getServer(), readerOnlyConfig());
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    serverPubSub.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    serverPubSub.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    serverPubSub.close();
  }

  @Test
  void closeIsIdempotent() throws Exception {
    ServerPubSub serverPubSub = ServerPubSub.attach(testServer.getServer(), readerOnlyConfig());
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    serverPubSub.close();
    serverPubSub.close();
  }

  @Test
  void closeBeforeStartupNeverRegistersTheFragment() throws Exception {
    ServerPubSubOptions options =
        ServerPubSubOptions.builder().exposeInformationModel(true).build();

    ServerPubSub serverPubSub =
        ServerPubSub.attach(testServer.getServer(), readerOnlyConfig(), options);

    // close first: the lifecycle state machine marks the optional components STOPPED, so the
    // subsequent
    // startup must not register them
    serverPubSub.close();

    // the startup future fails cleanly (the engine service was already shut down)
    ExecutionException e =
        assertThrows(
            ExecutionException.class,
            () -> serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS));
    assertInstanceOf(IllegalStateException.class, e.getCause());

    // the fragment never registered: no fragment-minted node is resolvable
    NodeId connectionNodeId =
        new NodeId(
            testServer.getServer().getServerNamespace().getNamespaceIndex(), "PubSub/lc-conn");
    assertTrue(
        testServer.getServer().getAddressSpaceManager().getManagedNode(connectionNodeId).isEmpty());

    // close stays idempotent after the failed startup
    serverPubSub.close();
  }

  @Test
  void closeBeforeStartupNeverAttachesTheSksFace() throws Exception {
    ServerPubSubOptions options =
        ServerPubSubOptions.builder().securityKeyServerEnabled(true).build();

    ServerPubSub serverPubSub =
        ServerPubSub.attach(testServer.getServer(), readerOnlyConfig(), options);

    serverPubSub.close();

    ExecutionException e =
        assertThrows(
            ExecutionException.class,
            () -> serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS));
    assertInstanceOf(IllegalStateException.class, e.getCause());

    // the SKS helper never attached: the ns0 GetSecurityKeys node still has no handler
    UaMethodNode methodNode =
        (UaMethodNode)
            testServer
                .getServer()
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.PublishSubscribe_GetSecurityKeys)
                .orElseThrow();
    assertSame(MethodInvocationHandler.NOT_IMPLEMENTED, methodNode.getInvocationHandler());

    serverPubSub.close();
  }

  @Test
  void closeAfterFailedStartupIsTolerated() throws Exception {
    // an enabled writer on an all-KeyFieldAddress dataset is not auto-bound and has no
    // user-supplied source, so service startup fails with Bad_ConfigurationError
    ServerPubSub serverPubSub = ServerPubSub.attach(testServer.getServer(), unboundWriterConfig());

    ExecutionException e =
        assertThrows(
            ExecutionException.class,
            () -> serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS));

    Throwable cause = e.getCause();
    while (cause != null && !(cause instanceof UaException)) {
      cause = cause.getCause();
    }
    assertNotNull(cause, "expected a UaException cause, got: " + e);
    assertEquals(StatusCodes.Bad_ConfigurationError, ((UaException) cause).getStatusCode().value());

    // close after the failed startup completes without throwing, and is idempotent
    serverPubSub.close();
    serverPubSub.close();
  }

  // region fixtures

  /** One UDP connection "lc-conn" with a single reader; nothing publishes. */
  private static PubSubConfig readerOnlyConfig() throws SocketException {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("lc-conn")
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(
                    ReaderGroupConfig.builder("grp")
                        .dataSetReader(
                            DataSetReaderConfig.builder("reader")
                                .publisherId(PublisherId.uint16(ushort(4730)))
                                .dataSetWriterId(ushort(1))
                                .build())
                        .build())
                .build())
        .build();
  }

  /**
   * One enabled writer on a dataset that is all KeyFieldAddress: not auto-bound, not user-bound.
   */
  private static PubSubConfig unboundWriterConfig() throws SocketException {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("lc-ds")
            .field(FieldDefinition.builder("key").dataType(NodeIds.Int32).build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp("lc-conn")
                .publisherId(PublisherId.uint16(ushort(4731)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(
                    WriterGroupConfig.builder("grp")
                        .writerGroupId(ushort(1))
                        .publishingInterval(Duration.ofMillis(100))
                        .dataSetWriter(
                            DataSetWriterConfig.builder("writer")
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(1))
                                .build())
                        .build())
                .build())
        .build();
  }

  // endregion

  // region helpers

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
