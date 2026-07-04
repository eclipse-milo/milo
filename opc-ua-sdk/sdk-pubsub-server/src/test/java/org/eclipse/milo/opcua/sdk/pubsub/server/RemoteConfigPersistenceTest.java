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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigFiles;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The persistence contract: the attach-time save carries the seeded VersionTime (the {@code
 * uint(0)} placeholder is retired), every successful mediator apply saves the wire form with the
 * mediator version, Enable/Disable, ReserveIds, and match-only CloseAndUpdate calls never save, a
 * save failure is isolated (results still reported, WARN + {@link
 * ServerPubSub#lastConfigurationSaveError()}, cleared on the next good save), and a store-loaded
 * version seeds the ConfigurationVersion clock.
 */
class RemoteConfigPersistenceTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final String CONN = "ps-conn";

  private static TestPubSubServer testServer;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  @AfterEach
  void tearDown() {
    attached.forEach(ServerPubSub::close);
    attached.clear();
  }

  @Test
  void savesFollowTheR8Cadence() throws Exception {
    var store = new RecordingStore();
    ServerPubSub serverPubSub = attachAndStart(store);
    Session session = session();

    // the attach-time save carries the seeded version, not uint(0)
    assertEquals(1, store.saved.size());
    UInteger attachVersion = store.saved.get(0).getConfigurationVersion();
    assertTrue(attachVersion.longValue() != 0L, "the uint(0) placeholder is retired");
    assertNull(serverPubSub.lastConfigurationSaveError());

    // ReserveIds is not a configuration mutation: no save
    CallMethodResult reserve =
        invoke(
            session,
            NodeIds.PublishSubscribe_PubSubConfiguration_ReserveIds,
            new Variant(PubSubIdReservations.PROFILE_UDP_UADP),
            new Variant(ushort(1)),
            new Variant(ushort(1)));
    assertEquals(StatusCode.GOOD, reserve.getStatusCode());
    assertEquals(1, store.saved.size());

    // a match-only CloseAndUpdate applies nothing: no save, no version bump
    CallMethodResult matchOnly =
        closeAndUpdate(session, matchConnectionFile(), matchConnectionRef());
    assertEquals(StatusCode.GOOD, matchOnly.getStatusCode());
    assertEquals(false, matchOnly.getOutputArguments()[0].getValue());
    assertEquals(1, store.saved.size());

    // a mutating CloseAndUpdate saves the wire form carrying the mediator version
    CallMethodResult mutating = closeAndUpdate(session, addGroupFile(), addGroupRef());
    assertEquals(StatusCode.GOOD, mutating.getStatusCode());
    assertEquals(true, mutating.getOutputArguments()[0].getValue());
    assertEquals(2, store.saved.size());

    var managed = (ManagedPubSubService) serverPubSub.runtime();
    PubSubConfiguration2DataType lastSaved = store.saved.get(1);
    assertEquals(managed.configurationVersion(), lastSaved.getConfigurationVersion());
    assertTrue(lastSaved.getConfigurationVersion().longValue() >= attachVersion.longValue());

    // Enable/Disable is state, not configuration: no save, no version bump
    PubSubHandle connectionHandle =
        serverPubSub.runtime().components().connection(CONN).orElseThrow();
    UInteger versionBeforeEnable = managed.configurationVersion();
    serverPubSub.runtime().enable(connectionHandle);
    serverPubSub.runtime().disable(connectionHandle);
    assertEquals(2, store.saved.size());
    assertEquals(versionBeforeEnable, managed.configurationVersion());

    // an owner runtime() apply saves too (the "not saved automatically" limitation is gone)
    serverPubSub.runtime().update(current -> current.toBuilder().enabled(true).build());
    assertEquals(3, store.saved.size());
  }

  @Test
  void saveFailureIsIsolatedAndRetriedOnTheNextApply() throws Exception {
    var store =
        new RecordingStore() {
          boolean failing = false;

          @Override
          public void save(PubSubConfiguration2DataType value) {
            super.save(value);
            if (failing) {
              throw new RuntimeException("simulated save failure");
            }
          }
        };

    ServerPubSub serverPubSub = attachAndStart(store);
    Session session = session();

    store.failing = true;

    // the apply still happens and its results are reported; the failure is surfaced
    CallMethodResult mutating = closeAndUpdate(session, addGroupFile(), addGroupRef());
    assertEquals(StatusCode.GOOD, mutating.getStatusCode());
    assertEquals(true, mutating.getOutputArguments()[0].getValue());
    assertTrue(serverPubSub.runtime().components().writerGroup(CONN, "ps-wg2").isPresent());

    Exception saveError = serverPubSub.lastConfigurationSaveError();
    assertNotNull(saveError);
    assertEquals("simulated save failure", saveError.getMessage());

    // the next successful apply retries and clears the accessor
    store.failing = false;
    serverPubSub.runtime().update(current -> current.toBuilder().enabled(true).build());
    assertNull(serverPubSub.lastConfigurationSaveError());
  }

  @Test
  void storeLoadedVersionSeedsTheConfigurationVersionClock() throws Exception {
    UInteger storedVersion = uint(0x12345678L);

    PubSubConfiguration2DataType stored =
        PubSubConfigurationFace.withConfigurationVersion(
            config(freeUdpPort()).toDataType(testServer.getServer().getNamespaceTable()),
            storedVersion);

    var store =
        new RecordingStore() {
          @Override
          public @Nullable PubSubConfiguration2DataType load() {
            return stored;
          }
        };

    ServerPubSub serverPubSub = attachAndStart(store);

    // nothing re-saved at attach (the loaded configuration won) and the clock is seeded
    assertEquals(0, store.saved.size());
    var managed = (ManagedPubSubService) serverPubSub.runtime();
    assertEquals(storedVersion, managed.configurationVersion());
  }

  // region helpers

  private ServerPubSub attachAndStart(PubSubConfigurationStore store) throws Exception {
    lastConfig = config(freeUdpPort());
    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            lastConfig,
            ServerPubSubOptions.builder()
                .allowRemoteConfiguration(true)
                .configurationStore(store)
                .build());
    attached.add(serverPubSub);
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return serverPubSub;
  }

  private static PubSubConfig config(int port) {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONN)
                .publisherId(PublisherId.uint16(ushort(4901)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .build())
        .build();
  }

  /** A file whose connection element (null name/id) structurally matches the live connection. */
  private PubSubConfiguration2DataType matchConnectionFile() {
    PubSubConfiguration2DataType file =
        lastConfig.toDataType(testServer.getServer().getNamespaceTable());
    var connection = file.getConnections()[0];
    file.getConnections()[0] =
        new org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType(
            null,
            connection.getEnabled(),
            Variant.NULL_VALUE,
            connection.getTransportProfileUri(),
            connection.getAddress(),
            connection.getConnectionProperties(),
            connection.getTransportSettings(),
            null,
            null);
    return file;
  }

  /** The attach configuration of the last attachment; the match rows must reproduce its port. */
  private PubSubConfig lastConfig;

  private static PubSubConfigurationRefDataType matchConnectionRef() {
    return new PubSubConfigurationRefDataType(
        PubSubConfigurationRefMask.of(
            PubSubConfigurationRefMask.Field.ElementMatch,
            PubSubConfigurationRefMask.Field.ReferenceConnection),
        ushort(0),
        ushort(0),
        ushort(0));
  }

  /** A file adding writer group {@code ps-wg2} under the live connection. */
  private PubSubConfiguration2DataType addGroupFile() {
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp(CONN)
                    .publisherId(PublisherId.uint16(ushort(4901)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 15910))
                    .writerGroup(
                        WriterGroupConfig.builder("ps-wg2")
                            .writerGroupId(ushort(33))
                            .publishingInterval(Duration.ofMillis(500))
                            .build())
                    .build())
            .build();
    return fileConfig.toDataType(testServer.getServer().getNamespaceTable());
  }

  private static PubSubConfigurationRefDataType addGroupRef() {
    return new PubSubConfigurationRefDataType(
        PubSubConfigurationRefMask.of(
            PubSubConfigurationRefMask.Field.ElementAdd,
            PubSubConfigurationRefMask.Field.ReferenceWriterGroup),
        ushort(0),
        ushort(0),
        ushort(0));
  }

  private CallMethodResult closeAndUpdate(
      Session session, PubSubConfiguration2DataType file, PubSubConfigurationRefDataType ref) {

    CallMethodResult open =
        invoke(session, NodeIds.PublishSubscribe_PubSubConfiguration_Open, new Variant(ubyte(6)));
    assertEquals(StatusCode.GOOD, open.getStatusCode());
    UInteger handle = (UInteger) open.getOutputArguments()[0].getValue();

    byte[] fileBytes =
        PubSubConfigFiles.encodeDataType(file, testServer.getServer().getStaticEncodingContext());

    CallMethodResult write =
        invoke(
            session,
            NodeIds.PublishSubscribe_PubSubConfiguration_Write,
            new Variant(handle),
            new Variant(ByteString.of(fileBytes)));
    assertEquals(StatusCode.GOOD, write.getStatusCode());

    return invoke(
        session,
        NodeIds.PublishSubscribe_PubSubConfiguration_CloseAndUpdate,
        new Variant(handle),
        new Variant(false),
        Variant.ofExtensionObjectArray(
            new ExtensionObject[] {
              ExtensionObject.encode(testServer.getServer().getStaticEncodingContext(), ref)
            }));
  }

  private static CallMethodResult invoke(Session session, NodeId methodNodeId, Variant... inputs) {
    UaMethodNode node =
        (UaMethodNode)
            testServer
                .getServer()
                .getAddressSpaceManager()
                .getManagedNode(methodNodeId)
                .orElseThrow(() -> new AssertionError("missing method node: " + methodNodeId));
    var request =
        new CallMethodRequest(
            NodeIds.PublishSubscribe_PubSubConfiguration, node.getNodeId(), inputs);
    return node.getInvocationHandler().invoke(() -> Optional.of(session), request);
  }

  private static Session session() {
    Session session = mock(Session.class);
    when(session.getRoleIds()).thenReturn(Optional.empty());
    when(session.getSessionId()).thenReturn(new NodeId(1, "ps-session-" + UUID.randomUUID()));
    return session;
  }

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  /** Records every saved wire configuration. */
  private static class RecordingStore implements PubSubConfigurationStore {

    final List<PubSubConfiguration2DataType> saved = new ArrayList<>();

    @Override
    public @Nullable PubSubConfiguration2DataType load() {
      return null;
    }

    @Override
    public void save(PubSubConfiguration2DataType value) {
      saved.add(value);
    }
  }

  // endregion
}
