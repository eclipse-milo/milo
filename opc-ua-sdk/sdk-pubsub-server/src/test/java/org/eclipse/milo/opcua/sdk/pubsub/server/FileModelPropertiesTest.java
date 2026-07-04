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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
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
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The i=25451 property rows over the REAL wire (T5 §5.4): the three D19-created optional properties
 * (MimeType/MaxByteStringLength/LastModifiedTime) answer real client Reads while the face is up and
 * are ABSENT ({@code Bad_NodeIdUnknown}) after shutdown; {@code Size} always serves the real
 * encoded file length (D43) and refreshes per apply; {@code Writable}/{@code UserWritable} are
 * capability values that stay {@code true} while a write lock is held; {@code OpenCount} (UInt16)
 * tracks opens/closes/CloseAndUpdate; {@code LastModifiedTime} strictly advances on a successful
 * apply; and the D20/AUTH11 disabled posture: with {@code allowRemoteConfiguration = false} the
 * loader property VALUES stay null and the D19 nodes are never created.
 *
 * <p>Per-test {@link ServerPubSub} attach (the lifecycle rows shut the face down), one class-level
 * started {@link SksTestServer} and one anonymous None-endpoint client.
 */
class FileModelPropertiesTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "fp-conn";

  private static SksTestServer testServer;
  private static OpcUaClient client;
  private static FileModelTestClient file;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();

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
  void createdAndMandatoryPropertiesServeTheR3Values() throws Exception {
    ServerPubSub serverPubSub = attach(true);

    // the three D19-created optional properties answer REAL Reads while the face is up
    assertEquals(
        PubSubConfigFiles.MIME_TYPE,
        goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_MimeType));

    Object maxByteStringLength =
        goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_MaxByteStringLength);
    assertEquals(
        uint(testServer.getServer().getConfig().getLimits().getMaxByteStringLength().longValue()),
        maxByteStringLength);

    assertTrue(
        goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_LastModifiedTime)
            instanceof DateTime);

    // Size is the REAL encoded length of the current configuration file (D43)
    byte[] fileBytes = file.readWholeFile();
    assertEquals(
        ulong(fileBytes.length), goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_Size));

    // Writable/UserWritable are capability values with the flag enabled
    assertEquals(true, goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_Writable));
    assertEquals(true, goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_UserWritable));

    // ... and they STAY true while a write lock is held (capability, not lock state)
    UInteger writeHandle = file.openOk(ubyte(0x06));
    try {
      assertEquals(true, goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_Writable));
      assertEquals(true, goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_UserWritable));

      // while a write handle is open, a Size read must not fault ("might not be accurate")
      assertTrue(
          file.readValue(NodeIds.PublishSubscribe_PubSubConfiguration_Size)
              .getStatusCode()
              .isGood());
    } finally {
      file.closeOk(writeHandle);
    }

    // OpenCount (UInt16, served as UShort) tracks two readers up and down
    assertEquals(ushort(0), goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));
    UInteger first = file.openOk(ubyte(0x01));
    UInteger second = file.openOk(ubyte(0x01));
    assertEquals(ushort(2), goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));
    file.closeOk(first);
    file.closeOk(second);
    assertEquals(ushort(0), goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));

    serverPubSub.close();
  }

  @Test
  void sizeAndLastModifiedTimeFollowASuccessfulApply() throws Exception {
    attach(true);

    DateTime before =
        (DateTime) goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_LastModifiedTime);
    ULong sizeBefore = (ULong) goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_Size);

    applyAddGroup();

    // LastModifiedTime strictly advances to the apply instant (monotonic advance only —
    // never exact-instant assertions); OpenCount dropped back (CloseAndUpdate closed the
    // handle); Size tracks the new, longer file
    DateTime after =
        (DateTime) goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_LastModifiedTime);
    assertTrue(
        after.getUtcTime() > before.getUtcTime(),
        "LastModifiedTime must advance on a successful CloseAndUpdate");

    assertEquals(ushort(0), goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));

    ULong sizeAfter = (ULong) goodValue(NodeIds.PublishSubscribe_PubSubConfiguration_Size);
    assertTrue(sizeAfter.longValue() > sizeBefore.longValue());
    assertEquals(ulong(file.readWholeFile().length), sizeAfter);
  }

  @Test
  void d19PropertiesDisappearAtShutdownAndLoaderValuesAreRestored() throws Exception {
    ServerPubSub serverPubSub = attach(true);

    assertTrue(
        file.readValue(NodeIds.PublishSubscribe_PubSubConfiguration_MimeType)
            .getStatusCode()
            .isGood());

    serverPubSub.close();

    // the three created nodes are GONE from ns0 (structural carve-out reverted, D19)
    for (NodeId createdNodeId :
        List.of(
            NodeIds.PublishSubscribe_PubSubConfiguration_MimeType,
            NodeIds.PublishSubscribe_PubSubConfiguration_MaxByteStringLength,
            NodeIds.PublishSubscribe_PubSubConfiguration_LastModifiedTime)) {
      assertEquals(
          new StatusCode(StatusCodes.Bad_NodeIdUnknown),
          file.readValue(createdNodeId).getStatusCode(),
          createdNodeId.toString());
    }

    // the loader-built property nodes remain but their values are restored to loader-null
    for (NodeId loaderNodeId :
        List.of(
            NodeIds.PublishSubscribe_PubSubConfiguration_Size,
            NodeIds.PublishSubscribe_PubSubConfiguration_Writable,
            NodeIds.PublishSubscribe_PubSubConfiguration_UserWritable,
            NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount)) {
      assertNull(nullableValue(loaderNodeId), loaderNodeId.toString());
    }
  }

  @Test
  void disabledRemoteConfigurationKeepsNs0Untouched() throws Exception {
    // D20/AUTH11: attach with allowRemoteConfiguration = false
    attach(false);

    // the methods keep the loader default over the wire
    assertEquals(new StatusCode(StatusCodes.Bad_NotImplemented), file.open(ubyte(0x01)).status());

    // the D19 optional properties are never created
    assertEquals(
        new StatusCode(StatusCodes.Bad_NodeIdUnknown),
        file.readValue(NodeIds.PublishSubscribe_PubSubConfiguration_MimeType).getStatusCode());

    // the loader property VALUES stay null (not false/zero — D20's amendment)
    assertNull(nullableValue(NodeIds.PublishSubscribe_PubSubConfiguration_Writable));
    assertNull(nullableValue(NodeIds.PublishSubscribe_PubSubConfiguration_UserWritable));
    assertNull(nullableValue(NodeIds.PublishSubscribe_PubSubConfiguration_Size));
    assertNull(nullableValue(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));
  }

  // region helpers

  /** Read {@code nodeId} over the wire, assert Good, and return the value. */
  private static Object goodValue(NodeId nodeId) throws UaException {
    DataValue value = file.readValue(nodeId);
    assertTrue(value.getStatusCode().isGood(), nodeId + " read: " + value.getStatusCode());
    Object result = value.getValue().getValue();
    assertNotNull(result, nodeId.toString());
    return result;
  }

  /** Read {@code nodeId} over the wire and return the (possibly null) value. */
  private static @Nullable Object nullableValue(NodeId nodeId) throws UaException {
    return file.readValue(nodeId).getValue().getValue();
  }

  /** A client-driven mutating apply: add one disabled writer group. */
  private void applyAddGroup() throws Exception {
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp(CONNECTION)
                    .publisherId(PublisherId.uint16(ushort(4821)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(
                        WriterGroupConfig.builder("fp-added-group")
                            .enabled(false)
                            .writerGroupId(ushort(0x8901))
                            .publishingInterval(Duration.ofMillis(500))
                            .build())
                    .build())
            .build();

    byte[] fileBytes =
        PubSubConfigFiles.encodeDataType(
            fileConfig.toDataType(testServer.getServer().getNamespaceTable()),
            client.getStaticEncodingContext());

    UInteger handle = file.openOk(ubyte(0x06));
    file.writeAll(handle, fileBytes);

    var addGroup =
        new PubSubConfigurationRefDataType(
            PubSubConfigurationRefMask.of(
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup),
            ushort(0),
            ushort(0),
            ushort(0));

    FileModelTestClient.CallResult result =
        file.closeAndUpdate(handle, true, new PubSubConfigurationRefDataType[] {addGroup});
    assertEquals(StatusCode.GOOD, result.status());
    assertTrue(FileModelTestClient.changesApplied(result), "apply failed: " + result);
  }

  private ServerPubSub attach(boolean allowRemoteConfiguration) throws Exception {
    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            config(),
            ServerPubSubOptions.builder()
                .exposeInformationModel(true)
                .allowRemoteConfiguration(allowRemoteConfiguration)
                .build());
    attached.add(serverPubSub);
    serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    return serverPubSub;
  }

  private static PubSubConfig config() throws SocketException {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4821)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .build())
        .build();
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
                    .setApplicationName(LocalizedText.english("file properties test client"))
                    .setApplicationUri("urn:eclipse:milo:test:file-properties-client")
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

  // endregion
}
