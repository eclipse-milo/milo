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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.ToLongFunction;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubCounter;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * The Reset Method handlers of the diagnostics exposure, direct-invoked with mocked {@link
 * Session}s ({@code SksServerFaceTest} pattern — direct invocation bypasses the AccessController,
 * which is the point: the handler-side checks are the subject).
 *
 * <p>Coverage: the authorization matrix (session-less &rarr; {@code Bad_UserAccessDenied}; no
 * RoleMapper &rarr; allowed; RoleMapper without ConfigureAdmin &rarr; denied; with &rarr; allowed;
 * no channel-security minimum — a None-mode session passes), the §9.1.11.3 per-object scope
 * (resetting a group leaves its readers' counters), and the root special case ({@code i=17421}
 * zeroes only the fragment-local root counters, never touching engine counters or {@code
 * targetWriteErrors()}).
 *
 * <p>Counters are driven without traffic: {@code runtime().disable(group)} ticks the group's
 * StateDisabledByMethod (METHOD cause) and cascades its enabled reader to Paused
 * (StatePausedByParent).
 */
class PubSubDiagnosticsResetTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "dr-conn";
  private static final String READER_GROUP = "rgrp";
  private static final String READER = "reader";

  @Test
  void resetAuthorizationMatrixAndPerObjectScope() throws Exception {
    try (TestPubSubServer testServer = TestPubSubServer.create()) {
      ServerPubSub serverPubSub =
          ServerPubSub.attach(
              testServer.getServer(),
              readerConfig(),
              ServerPubSubOptions.builder()
                  .exposeInformationModel(true)
                  .diagnosticsEnabled(true)
                  .build());
      try {
        serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        String groupPath = CONNECTION + "/" + READER_GROUP;
        String readerPath = groupPath + "/" + READER;

        // an explicit disable ticks the group's StateDisabledByMethod (METHOD cause) and
        // cascades the reader to Paused (StatePausedByParent; startup transitions may have
        // ticked it already, so capture the settled value rather than pinning it)
        disableReaderGroup(serverPubSub);
        awaitCounter(serverPubSub, groupPath, ComponentDiagnostics::stateDisabledByMethod, 1);
        long readerPaused =
            awaitCounterAtLeast(
                serverPubSub, readerPath, ComponentDiagnostics::statePausedByParent, 1);

        UaMethodNode resetNode =
            fragmentMethodNode(testServer, "PubSub/" + groupPath + "/Diagnostics/Reset");

        // session-less invocations answer Bad_UserAccessDenied
        CallMethodResult sessionLess = invokeReset(resetNode, null);
        assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), sessionLess.getStatusCode());
        assertEquals(
            1,
            diagnostics(serverPubSub, groupPath).stateDisabledByMethod(),
            "a denied Reset must not touch counters");

        // a RoleMapper without ConfigureAdmin denies; the authorizer's code surfaces verbatim
        CallMethodResult denied =
            invokeReset(resetNode, mockSession(List.of(new NodeId(1, "unrelated-role"))));
        assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), denied.getStatusCode());

        // no RoleMapper (empty roleIds Optional): allowed by the opt-in posture
        CallMethodResult allowed = invokeReset(resetNode, mockSession(null));
        assertEquals(StatusCode.GOOD, allowed.getStatusCode());

        // per-object scope: the group's counters and TimeFirstChange reset; the reader's stand
        ComponentDiagnostics group = diagnostics(serverPubSub, groupPath);
        assertEquals(0, group.stateDisabledByMethod());
        assertNull(group.timeFirstChange(PubSubCounter.STATE_DISABLED_BY_METHOD));
        assertEquals(readerPaused, diagnostics(serverPubSub, readerPath).statePausedByParent());

        // re-tick, then a RoleMapper WITH ConfigureAdmin is allowed too
        enableReaderGroup(serverPubSub);
        disableReaderGroup(serverPubSub);
        awaitCounter(serverPubSub, groupPath, ComponentDiagnostics::stateDisabledByMethod, 1);

        CallMethodResult admin =
            invokeReset(resetNode, mockSession(List.of(NodeIds.WellKnownRole_ConfigureAdmin)));
        assertEquals(StatusCode.GOOD, admin.getStatusCode());
        assertEquals(0, diagnostics(serverPubSub, groupPath).stateDisabledByMethod());
      } finally {
        serverPubSub.close();
      }
    }
  }

  @Test
  void rootResetZeroesOnlyTheFragmentLocalCounters() throws Exception {
    try (TestPubSubServer testServer = TestPubSubServer.create()) {
      ServerPubSub serverPubSub =
          ServerPubSub.attach(
              testServer.getServer(),
              readerConfig(),
              ServerPubSubOptions.builder()
                  .exposeInformationModel(true)
                  .diagnosticsEnabled(true)
                  .build());
      try {
        serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

        String groupPath = CONNECTION + "/" + READER_GROUP;

        // engine-side counters tick...
        disableReaderGroup(serverPubSub);
        awaitCounter(serverPubSub, groupPath, ComponentDiagnostics::stateDisabledByMethod, 1);

        // ...and the fragment-local root counters tick through the test seam (nothing ticks
        // them in production: the root Status Enable/Disable pair is not minted)
        PubSubInfoModelFragment fragment = serverPubSub.infoModelFragment();
        assertNotNull(fragment);
        PubSubDiagnosticsExposure exposure = fragment.diagnosticsExposure();
        assertNotNull(exposure);
        exposure.tickRootStateCounter(PubSubCounter.STATE_DISABLED_BY_METHOD);

        UaMethodNode rootResetNode =
            (UaMethodNode)
                testServer
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(NodeIds.PublishSubscribe_Diagnostics_Reset)
                    .orElseThrow();

        // same session gate as the per-component handlers
        CallMethodResult deniedResult = invokeReset(rootResetNode, null);
        assertEquals(
            new StatusCode(StatusCodes.Bad_UserAccessDenied), deniedResult.getStatusCode());

        CallMethodResult result = invokeReset(rootResetNode, mockSession(null));
        assertEquals(StatusCode.GOOD, result.getStatusCode());

        // root Reset is fragment-local: the ns0 counter zeroes...
        assertEquals(
            uint(0),
            ns0CounterValue(
                testServer, NodeIds.PublishSubscribe_Diagnostics_Counters_StateDisabledByMethod));

        // ...while engine counters and the ServerPubSub-owned vendor map stay untouched
        assertEquals(1, diagnostics(serverPubSub, groupPath).stateDisabledByMethod());
        assertTrue(serverPubSub.targetWriteErrors().isEmpty());
      } finally {
        serverPubSub.close();
      }
    }
  }

  // region fixtures

  /** One enabled reader group with one enabled reader; no writers, no traffic needed. */
  private static PubSubConfig readerConfig() throws SocketException {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("dr-ds")
            .field("value", NodeIds.Double, new UUID(0L, 0xB1L))
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(
                    ReaderGroupConfig.builder(READER_GROUP)
                        .dataSetReader(
                            DataSetReaderConfig.builder(READER)
                                .publisherId(PublisherId.uint16(ushort(4801)))
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

  private static void disableReaderGroup(ServerPubSub serverPubSub) {
    PubSubHandle handle =
        serverPubSub.runtime().components().readerGroup(CONNECTION, READER_GROUP).orElseThrow();
    serverPubSub.runtime().disable(handle);
  }

  private static void enableReaderGroup(ServerPubSub serverPubSub) {
    PubSubHandle handle =
        serverPubSub.runtime().components().readerGroup(CONNECTION, READER_GROUP).orElseThrow();
    serverPubSub.runtime().enable(handle);
  }

  private static ComponentDiagnostics diagnostics(ServerPubSub serverPubSub, String path) {
    return serverPubSub
        .runtime()
        .diagnostics()
        .component(path)
        .orElseThrow(() -> new AssertionError("expected collector entry for: " + path));
  }

  /** Poll the collector until {@code counter} of {@code path} equals {@code expected}. */
  private static void awaitCounter(
      ServerPubSub serverPubSub,
      String path,
      ToLongFunction<ComponentDiagnostics> counter,
      long expected)
      throws InterruptedException {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    long lastValue = -1;
    while (true) {
      lastValue = counter.applyAsLong(diagnostics(serverPubSub, path));
      if (lastValue == expected) {
        return;
      }
      if (System.nanoTime() >= deadline) {
        fail("timed out waiting for counter == " + expected + " on " + path + ": " + lastValue);
      }
      Thread.sleep(25);
    }
  }

  /** Poll until {@code counter} of {@code path} is at least {@code minimum}; returns the value. */
  private static long awaitCounterAtLeast(
      ServerPubSub serverPubSub,
      String path,
      ToLongFunction<ComponentDiagnostics> counter,
      long minimum)
      throws InterruptedException {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    long lastValue = -1;
    while (true) {
      lastValue = counter.applyAsLong(diagnostics(serverPubSub, path));
      if (lastValue >= minimum) {
        return lastValue;
      }
      if (System.nanoTime() >= deadline) {
        fail("timed out waiting for counter >= " + minimum + " on " + path + ": " + lastValue);
      }
      Thread.sleep(25);
    }
  }

  private static UaMethodNode fragmentMethodNode(TestPubSubServer testServer, String identifier) {
    var nodeId =
        new NodeId(testServer.getServer().getServerNamespace().getNamespaceIndex(), identifier);

    return (UaMethodNode)
        testServer
            .getServer()
            .getAddressSpaceManager()
            .getManagedNode(nodeId)
            .orElseThrow(() -> new AssertionError("expected method node: " + nodeId));
  }

  private static Object ns0CounterValue(TestPubSubServer testServer, NodeId nodeId) {
    var node =
        (UaVariableNode)
            testServer
                .getServer()
                .getAddressSpaceManager()
                .getManagedNode(nodeId)
                .orElseThrow(() -> new AssertionError("expected ns0 node: " + nodeId));
    return node.getValue().getValue().getValue();
  }

  /** Invoke a zero-argument Reset method node with {@code session} (or session-less when null). */
  private static CallMethodResult invokeReset(UaMethodNode node, @Nullable Session session) {
    return node.getInvocationHandler()
        .invoke(
            () -> Optional.ofNullable(session),
            new CallMethodRequest(NodeId.NULL_VALUE, node.getNodeId(), new Variant[0]));
  }

  /**
   * A session whose {@code getRoleIds()} reflects the RoleMapper posture: {@code null} = no
   * RoleMapper configured; a list = mapped roles. No channel-security stub: Reset has no
   * channel-mode minimum.
   */
  private static Session mockSession(@Nullable List<NodeId> roleIds) {
    Session session = mock(Session.class);
    when(session.getRoleIds()).thenReturn(Optional.ofNullable(roleIds));
    return session;
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
