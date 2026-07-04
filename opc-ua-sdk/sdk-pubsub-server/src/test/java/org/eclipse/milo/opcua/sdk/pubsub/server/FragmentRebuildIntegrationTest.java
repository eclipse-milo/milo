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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Integration coverage for information-model rebuild behavior not already covered by local unit
 * tests:
 *
 * <ul>
 *   <li>Raced startup/close loop: {@code startup()} and {@code close()} released by one latch on
 *       two threads, looped, must leave NO fragment registration, NO attached SKS or
 *       remote-configuration handler, and let no unexpected exception escape (concurrent regression
 *       coverage for the CAS-vs-close race fix; {@link ServerPubSubLifecycleTest} covers the
 *       sequential orderings).
 *   <li>A path-stable Modify rebuild preserves the fragment Diagnostics counter values at the node
 *       layer, not just in the engine snapshot covered by {@code
 *       CounterPreservationReconfigureTest}, and the surviving node keeps counting after the
 *       rebuild.
 * </ul>
 *
 * <p>Adjacent rebuild scenarios are covered by {@code ServerPubSubReconfigureModelTest}, {@code
 * PubSubInfoModelLiveStateTest}, and {@code ServerPubSubTargetVariablesReconfigureTest}. Network
 * safety: unicast 127.0.0.1 with ephemeral ports and explicit loopback {@code discoveryAddress}
 * everywhere.
 */
class FragmentRebuildIntegrationTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(15);

  private static final int RACE_ITERATIONS = 20;

  private static final String RACE_CONNECTION = "rb-conn";
  private static final String RB3_CONNECTION = "rb3-conn";
  private static final String RB3_GROUP = "wgrp";

  private static TestPubSubServer testServer;

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  /**
   * Loop raced {@code startup()}/{@code close()} pairs (a {@link CountDownLatch} releases both
   * threads together) and assert after every iteration that the world is residue-free — the
   * lifecycle state machine must serialize ANY interleaving into "never registered" or "registered,
   * then fully torn down".
   */
  @Test
  void racedStartupAndCloseLoopLeavesNoFaceResidue() throws Exception {
    ServerPubSubOptions options =
        ServerPubSubOptions.builder()
            .exposeInformationModel(true)
            .securityKeyServerEnabled(true)
            .allowRemoteConfiguration(true)
            .build();

    for (int i = 0; i < RACE_ITERATIONS; i++) {
      ServerPubSub serverPubSub =
          ServerPubSub.attach(testServer.getServer(), raceConfig(), options);

      var failures = new CopyOnWriteArrayList<Throwable>();
      var start = new CountDownLatch(1);

      Thread starter =
          new Thread(
              () -> {
                try {
                  start.await();
                  serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
                } catch (ExecutionException e) {
                  // a startup that loses the race fails CLEANLY (the engine service was
                  // already shut down; the optional components were marked STOPPED) — tolerated,
                  // the
                  // guarantee under test is registration hygiene, not startup success
                } catch (Throwable t) {
                  failures.add(t);
                }
              },
              "rb7-startup-" + i);

      Thread closer =
          new Thread(
              () -> {
                try {
                  start.await();
                  serverPubSub.close();
                } catch (Throwable t) {
                  failures.add(t);
                }
              },
              "rb7-close-" + i);

      starter.start();
      closer.start();
      start.countDown();

      starter.join(TIMEOUT.toMillis());
      closer.join(TIMEOUT.toMillis());
      assertFalse(starter.isAlive(), "iteration " + i + ": the startup thread never finished");
      assertFalse(closer.isAlive(), "iteration " + i + ": the close thread never finished");

      // whatever interleaving happened, a final close is legal and idempotent — after it the
      // teardown of a race-winning startup is guaranteed complete
      serverPubSub.close();

      assertTrue(
          failures.isEmpty(), "iteration " + i + ": exceptions escaped the race: " + failures);

      // no fragment registration remains: the fragment-minted connection node is unresolvable
      NodeId connectionNodeId = new NodeId(namespaceIndex(), "PubSub/" + RACE_CONNECTION);
      assertTrue(
          testServer
              .getServer()
              .getAddressSpaceManager()
              .getManagedNode(connectionNodeId)
              .isEmpty(),
          "iteration " + i + ": a fragment node survived the raced lifecycle");

      // the optional helpers restored the ns0
      // NOT_IMPLEMENTED handlers (never-attached and attached-then-detached look identical)
      assertSame(
          MethodInvocationHandler.NOT_IMPLEMENTED,
          methodNode(NodeIds.PublishSubscribe_GetSecurityKeys).getInvocationHandler(),
          "iteration " + i + ": the SKS helper left its GetSecurityKeys handler attached");
      assertSame(
          MethodInvocationHandler.NOT_IMPLEMENTED,
          methodNode(NodeIds.PublishSubscribe_PubSubConfiguration_Open).getInvocationHandler(),
          "iteration " + i + ": the configuration helper left its Open handler attached");
    }
  }

  /**
   * Node-layer cross-check: after a path-stable Modify (group {@code publishingInterval} change),
   * the SAME fragment Diagnostics counter node serves the PRESERVED value — read immediately after
   * the apply (the rebuild hooks run synchronously inside the reconfigure), a restarted counter
   * would still be near zero — and keeps counting live traffic afterwards.
   */
  @Test
  void diagnosticsNodeValuesContinueAcrossAPathStableRebuild() throws Exception {
    int dataPort = freeUdpPort();
    int discoveryPort = freeUdpPort();

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("rb3-ds")
            .field(FieldDefinition.builder("value").dataType(NodeIds.Double).build())
            .build();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(
                dataSet.ref(),
                context -> {
                  DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
                  builder.field("value", new DataValue(Variant.ofDouble(3.5)));
                  return builder.build();
                })
            .build();

    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            rb3Config(dataSet, dataPort, discoveryPort, Duration.ofMillis(100)),
            ServerPubSubOptions.builder()
                .exposeInformationModel(true)
                .diagnosticsEnabled(true)
                .bindings(bindings)
                .build());
    try {
      serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      String counterId =
          "PubSub/%s/%s/Diagnostics/Counters/SentNetworkMessages"
              .formatted(RB3_CONNECTION, RB3_GROUP);

      awaitNodeValue(
          counterId,
          value -> value instanceof UInteger count && count.longValue() >= 5,
          "at least five sent NetworkMessages before the rebuild");
      long floor = ((UInteger) fragmentNodeValue(counterId)).longValue();

      // the path-stable Modify: only the group shell's publishingInterval changes
      ReconfigureResult result =
          serverPubSub
              .runtime()
              .reconfigure(
                  rb3Config(dataSet, dataPort, discoveryPort, Duration.ofMillis(140)),
                  PubSubService.ReconfigureMode.DISABLE_AFFECTED);
      assertEquals(List.of(RB3_CONNECTION + "/" + RB3_GROUP), result.restartedPaths());

      // at the NODE layer: the surviving node serves the preserved count right away
      Object immediately = fragmentNodeValue(counterId);
      assertTrue(
          immediately instanceof UInteger count && count.longValue() >= floor,
          "the fragment counter node must serve the preserved value across the rebuild: "
              + immediately
              + " (floor "
              + floor
              + ")");

      // the rebuilt group bounces back to Operational and the SAME node keeps counting —
      // the exposure re-registered against the surviving path
      PubSubHandle group =
          serverPubSub.runtime().components().writerGroup(RB3_CONNECTION, RB3_GROUP).orElseThrow();
      await(
          "the rebuilt group returns to Operational",
          () -> serverPubSub.runtime().state(group) == PubSubState.Operational);
      awaitNodeValue(
          counterId,
          value -> value instanceof UInteger count && count.longValue() > floor,
          "counting continues on the surviving node after the rebuild");
    } finally {
      serverPubSub.close();
    }
  }

  // region fixtures

  /** Reader-only connection plus one SecurityGroup: substance for the fragment and SKS helper. */
  private static PubSubConfig raceConfig() throws SocketException {
    return PubSubConfig.builder()
        .securityGroup(
            SecurityGroupConfig.builder("rb-sg").keyLifeTime(Duration.ofMinutes(10)).build())
        .connection(
            PubSubConnectionConfig.udp(RACE_CONNECTION)
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(
                    ReaderGroupConfig.builder("grp")
                        .dataSetReader(
                            DataSetReaderConfig.builder("reader")
                                .publisherId(PublisherId.uint16(ushort(4741)))
                                .dataSetWriterId(ushort(1))
                                .build())
                        .build())
                .build())
        .build();
  }

  /** One publishing group to a silent loopback sink; only {@code interval} varies per build. */
  private static PubSubConfig rb3Config(
      PublishedDataSetConfig dataSet, int dataPort, int discoveryPort, Duration interval) {
    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp(RB3_CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4742)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", dataPort))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", discoveryPort))
                .writerGroup(
                    WriterGroupConfig.builder(RB3_GROUP)
                        .writerGroupId(ushort(1))
                        .publishingInterval(interval)
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

  private static UShort namespaceIndex() {
    return testServer.getServer().getServerNamespace().getNamespaceIndex();
  }

  private static UaMethodNode methodNode(NodeId nodeId) {
    return (UaMethodNode)
        testServer.getServer().getAddressSpaceManager().getManagedNode(nodeId).orElseThrow();
  }

  /** The current value of a fragment-minted variable node, through its attribute filters. */
  private static Object fragmentNodeValue(String identifier) {
    UaVariableNode node =
        (UaVariableNode)
            testServer
                .getServer()
                .getAddressSpaceManager()
                .getManagedNode(new NodeId(namespaceIndex(), identifier))
                .orElseThrow(() -> new IllegalArgumentException("no such node: " + identifier));
    return node.getValue().getValue().getValue();
  }

  /** Deadline-poll the value of a fragment node until {@code predicate} holds. */
  private static void awaitNodeValue(
      String identifier, Predicate<Object> predicate, String description) {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    Object lastValue = null;
    while (System.nanoTime() < deadline) {
      lastValue = fragmentNodeValue(identifier);
      if (predicate.test(lastValue)) {
        return;
      }
      Thread.onSpinWait();
    }
    fail("timed out awaiting %s; last value: %s".formatted(description, lastValue));
  }

  /** Deadline-poll until {@code condition} holds. */
  private static void await(String description, Supplier<Boolean> condition) {
    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (!condition.get()) {
      if (System.nanoTime() > deadline) {
        fail("timed out awaiting " + description);
      }
      Thread.onSpinWait();
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
