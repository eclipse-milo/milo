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

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigFiles;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The R8 persistence cadence driven ENTIRELY by a real client (T5 §9) — the wire-level layer over
 * the direct-invoke {@link RemoteConfigPersistenceTest}: a client CloseAndUpdate that mutates saves
 * exactly once and the payload round-trips with the real VersionTime; a match-only client call
 * saves nothing; Enable/Disable THROUGH THE MINTED STATUS METHODS saves nothing (the row a prior
 * review flagged as untested — the direct suite only covered {@code runtime().enable}); the P3
 * element-failure cadence (a partial-mode call mixing a failed ref with an applied survivor saves
 * exactly once with the survivor in the payload, while a call whose refs ALL fail saves nothing);
 * and a store save failure is isolated (the client still sees applied results) and surfaced via
 * {@link ServerPubSub#lastConfigurationSaveError()}, with the next successful client apply
 * persisting the CUMULATIVE configuration and clearing the accessor.
 */
class RemoteConfigPersistenceIntegrationTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "pi-conn";
  private static final String ADDED_GROUP = "pi-wg-added";
  private static final String SECOND_GROUP = "pi-wg-second";

  private static SksTestServer testServer;
  private static OpcUaClient client;
  private static FileModelTestClient file;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();

  private int basePort;

  @BeforeAll
  static void startServerAndClient() throws Exception {
    testServer = SksTestServer.create(null);
    client = connect();
    file = new FileModelTestClient(client);
  }

  @AfterAll
  static void stopEverything() throws Exception {
    if (client != null) {
      client.disconnect();
    }
    if (testServer != null) {
      testServer.close();
    }
  }

  @AfterEach
  void closeAttached() {
    attached.forEach(ServerPubSub::close);
    attached.clear();
  }

  @Test
  void clientDrivenSaveCadenceFollowsR8() throws Exception {
    var store = new RecordingStore();
    ServerPubSub serverPubSub = attach(store);

    // the attach-time save with a real (non-zero) VersionTime
    assertEquals(1, store.saved.size());
    assertTrue(store.saved.get(0).getConfigurationVersion().longValue() != 0L);

    // a mutating client CloseAndUpdate saves exactly once more, and the payload round-trips
    FileModelTestClient.CallResult mutating =
        applyFile(fileWithGroup(ADDED_GROUP, 0x7501), addGroupRef());
    assertEquals(StatusCode.GOOD, mutating.status());
    assertTrue(FileModelTestClient.changesApplied(mutating));
    assertEquals(2, store.saved.size());

    var managed = (ManagedPubSubService) serverPubSub.runtime();
    PubSubConfiguration2DataType saved = store.saved.get(1);
    assertEquals(managed.configurationVersion(), saved.getConfigurationVersion());
    WriterGroupDataType[] savedGroups =
        Objects.requireNonNull(Objects.requireNonNull(saved.getConnections())[0].getWriterGroups());
    assertTrue(
        Arrays.stream(savedGroups).anyMatch(g -> ADDED_GROUP.equals(g.getName())),
        "the saved wire form must round-trip the applied change");

    // a match-only client CloseAndUpdate applies nothing: no save, no version bump
    UInteger versionBeforeMatch = managed.configurationVersion();
    FileModelTestClient.CallResult matchOnly =
        applyWire(matchConnectionFile(), matchConnectionRef());
    assertEquals(StatusCode.GOOD, matchOnly.status());
    assertEquals(false, matchOnly.outputs()[0].getValue());
    assertEquals(2, store.saved.size());
    assertEquals(versionBeforeMatch, managed.configurationVersion());

    // THE FLAGGED ROW: Enable/Disable through the minted Status methods is state, not
    // configuration — no save, no version bump
    assertEquals(StatusCode.GOOD, callStatus("Disable").getStatusCode());
    assertEquals(StatusCode.GOOD, callStatus("Enable").getStatusCode());
    assertEquals(2, store.saved.size(), "Enable/Disable must never save");
    assertEquals(versionBeforeMatch, managed.configurationVersion());

    // P3: a partial-mode call mixing a failed ref with an applied survivor mutated the
    // configuration — exactly one more save, and the payload carries the survivor
    FileModelTestClient.CallResult partial =
        applyFile(fileWithGroup(SECOND_GROUP, 0x7502), zeroOpBitsRef(), addGroupRef());
    assertEquals(StatusCode.GOOD, partial.status());
    assertTrue(FileModelTestClient.changesApplied(partial));
    StatusCode[] partialResults = FileModelTestClient.referencesResults(partial);
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), partialResults[0]);
    assertEquals(StatusCode.GOOD, partialResults[1]);
    assertEquals(3, store.saved.size(), "a partial apply with survivors saves exactly once");
    WriterGroupDataType[] partialGroups =
        Objects.requireNonNull(
            Objects.requireNonNull(store.saved.get(2).getConnections())[0].getWriterGroups());
    assertTrue(
        Arrays.stream(partialGroups).anyMatch(g -> SECOND_GROUP.equals(g.getName())),
        "the saved wire form must carry the partial-mode survivor");

    // P3: a call whose refs ALL fail applies nothing — no save, no version bump
    UInteger versionBeforeFailed = managed.configurationVersion();
    FileModelTestClient.CallResult allFailed =
        applyFile(fileWithGroup("pi-wg-unused", 0x7503), zeroOpBitsRef());
    assertEquals(StatusCode.GOOD, allFailed.status());
    assertEquals(false, FileModelTestClient.changesApplied(allFailed));
    assertEquals(
        new StatusCode(StatusCodes.Bad_InvalidArgument),
        FileModelTestClient.referencesResults(allFailed)[0]);
    assertEquals(3, store.saved.size(), "an all-failed call must never save");
    assertEquals(versionBeforeFailed, managed.configurationVersion());
    assertNull(serverPubSub.lastConfigurationSaveError());
  }

  @Test
  void saveFailureIsIsolatedFromTheClientAndRetriedCumulatively() throws Exception {
    var store =
        new RecordingStore() {
          volatile boolean failing = false;

          @Override
          public void save(PubSubConfiguration2DataType value) {
            super.save(value);
            if (failing) {
              throw new RuntimeException("simulated save failure");
            }
          }
        };

    ServerPubSub serverPubSub = attach(store);
    store.failing = true;

    // the client still sees APPLIED results; the failure is surfaced on the owner accessor
    FileModelTestClient.CallResult first =
        applyFile(fileWithGroup(ADDED_GROUP, 0x7501), addGroupRef());
    assertEquals(StatusCode.GOOD, first.status());
    assertTrue(FileModelTestClient.changesApplied(first));
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(first)[0]);
    assertTrue(
        serverPubSub.runtime().components().writerGroup(CONNECTION, ADDED_GROUP).isPresent());

    Exception saveError = serverPubSub.lastConfigurationSaveError();
    assertNotNull(saveError);
    assertEquals("simulated save failure", saveError.getMessage());

    // the next successful client mutation persists the CUMULATIVE configuration and clears it
    store.failing = false;
    FileModelTestClient.CallResult second =
        applyFile(fileWithGroup(SECOND_GROUP, 0x7502), addGroupRef());
    assertTrue(FileModelTestClient.changesApplied(second));
    assertNull(serverPubSub.lastConfigurationSaveError());

    PubSubConfiguration2DataType lastSaved = store.saved.get(store.saved.size() - 1);
    WriterGroupDataType[] groups =
        Objects.requireNonNull(
            Objects.requireNonNull(lastSaved.getConnections())[0].getWriterGroups());
    assertTrue(Arrays.stream(groups).anyMatch(g -> ADDED_GROUP.equals(g.getName())));
    assertTrue(Arrays.stream(groups).anyMatch(g -> SECOND_GROUP.equals(g.getName())));
  }

  // region fixtures + helpers

  private CallMethodResult callStatus(String methodName) throws UaException {
    UShort idx = testServer.getServer().getServerNamespace().getNamespaceIndex();
    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    new NodeId(idx, "PubSub/" + CONNECTION + "/Status"),
                    new NodeId(idx, "PubSub/" + CONNECTION + "/Status/" + methodName),
                    new Variant[0])));
    CallMethodResult[] results = response.getResults();
    assertTrue(results != null && results.length == 1);
    return results[0];
  }

  private FileModelTestClient.CallResult applyFile(
      PubSubConfig fileConfig, PubSubConfigurationRefDataType... refs) throws Exception {

    return applyWire(fileConfig.toDataType(testServer.getServer().getNamespaceTable()), refs);
  }

  private FileModelTestClient.CallResult applyWire(
      PubSubConfiguration2DataType wire, PubSubConfigurationRefDataType... refs) throws Exception {

    byte[] bytes = PubSubConfigFiles.encodeDataType(wire, client.getStaticEncodingContext());

    UInteger handle = file.openOk(ubyte(0x06));
    file.writeAll(handle, bytes);
    return file.closeAndUpdate(handle, false, refs);
  }

  /** A file whose connection element (null name/id) structurally matches the live connection. */
  private PubSubConfiguration2DataType matchConnectionFile() {
    PubSubConfiguration2DataType wire =
        config().toDataType(testServer.getServer().getNamespaceTable());
    PubSubConnectionDataType connection = wire.getConnections()[0];
    wire.getConnections()[0] =
        new PubSubConnectionDataType(
            null,
            connection.getEnabled(),
            Variant.NULL_VALUE,
            connection.getTransportProfileUri(),
            connection.getAddress(),
            connection.getConnectionProperties(),
            connection.getTransportSettings(),
            null,
            null);
    return wire;
  }

  private static PubSubConfigurationRefDataType matchConnectionRef() {
    return new PubSubConfigurationRefDataType(
        PubSubConfigurationRefMask.of(
            PubSubConfigurationRefMask.Field.ElementMatch,
            PubSubConfigurationRefMask.Field.ReferenceConnection),
        ushort(0),
        ushort(0),
        ushort(0));
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

  /** A ref carrying an element-kind bit but ZERO operation bits: per-element failure. */
  private static PubSubConfigurationRefDataType zeroOpBitsRef() {
    return new PubSubConfigurationRefDataType(
        PubSubConfigurationRefMask.of(PubSubConfigurationRefMask.Field.ReferenceWriterGroup),
        ushort(0),
        ushort(0),
        ushort(0));
  }

  /** A file adding one disabled writer group {@code name} under the live connection. */
  private PubSubConfig fileWithGroup(String name, int groupId) {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4871)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", basePort))
                .writerGroup(
                    WriterGroupConfig.builder(name)
                        .enabled(false)
                        .writerGroupId(ushort(groupId))
                        .publishingInterval(Duration.ofMillis(500))
                        .build())
                .build())
        .build();
  }

  private PubSubConfig config() {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4871)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", basePort))
                .build())
        .build();
  }

  private ServerPubSub attach(PubSubConfigurationStore store) throws Exception {
    basePort = freeUdpPort();
    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            config(),
            ServerPubSubOptions.builder()
                // the Status methods are minted only with the information model exposed
                .exposeInformationModel(true)
                .allowRemoteConfiguration(true)
                .configurationStore(store)
                .build());
    attached.add(serverPubSub);
    serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    return serverPubSub;
  }

  private static OpcUaClient connect() throws UaException {
    OpcUaClient newClient =
        OpcUaClient.create(
            testServer.getEndpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(
                        e -> Objects.equals(e.getSecurityPolicyUri(), SecurityPolicy.None.getUri()))
                    .findFirst(),
            transportConfigBuilder -> {},
            clientConfigBuilder ->
                clientConfigBuilder
                    .setApplicationName(LocalizedText.english("persistence test client"))
                    .setApplicationUri("urn:eclipse:milo:test:persistence-client")
                    .setIdentityProvider(new AnonymousProvider())
                    .setRequestTimeout(uint(5_000)));

    newClient.connect();

    return newClient;
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  /** Records every saved wire configuration; saves may run on the method-handler thread. */
  private static class RecordingStore implements PubSubConfigurationStore {

    final List<PubSubConfiguration2DataType> saved = new CopyOnWriteArrayList<>();

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
