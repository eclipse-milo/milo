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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.Objects;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * The FileType state machine driven CLIENT-SIDE over a real opc.tcp channel: the open-mode matrix,
 * the Part 20 shall-sentence locking rows (including the own-readers-block-write trap and the
 * {@code Bad_NotReadable}/{@code Bad_NotWritable} vocabulary — never {@code Bad_InvalidState} for
 * lock conflicts), read/write/position behavior, snapshot-at-Open isolation, the write-handle Close
 * abort, and the normative read-modify-write (0x03) sequence ending in CloseAndUpdate.
 *
 * <p>Two clients (two real sessions) drive the cross-session locking rows. Every test leaves zero
 * open handles; {@code @AfterEach} asserts {@code OpenCount == 0} via a real property read.
 *
 * <p>The 1-MiB read clamp boundary itself is unit territory ({@code FileHandleManagerTest}); the
 * client row here proves the {@code min(length, remaining)} clamp shape on the real file (an
 * over-long request answers Good with exactly the remaining bytes).
 */
class FileTypeStateMachineIntegrationTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "ft-conn";

  private static SksTestServer testServer;
  private static ServerPubSub serverPubSub;
  private static OpcUaClient clientA;
  private static OpcUaClient clientB;
  private static FileModelTestClient fileA;
  private static FileModelTestClient fileB;

  @BeforeAll
  static void startServerAndClients() throws Exception {
    testServer = SksTestServer.create(null);

    serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            baseConfig(),
            ServerPubSubOptions.builder()
                .exposeInformationModel(true)
                .allowRemoteConfiguration(true)
                .build());
    serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

    clientA = connect("urn:eclipse:milo:test:file-model-client-a");
    clientB = connect("urn:eclipse:milo:test:file-model-client-b");
    fileA = new FileModelTestClient(clientA);
    fileB = new FileModelTestClient(clientB);
  }

  @AfterAll
  static void stopEverything() throws Exception {
    if (clientA != null) {
      clientA.disconnect();
    }
    if (clientB != null) {
      clientB.disconnect();
    }
    if (serverPubSub != null) {
      serverPubSub.close();
    }
    if (testServer != null) {
      testServer.close();
    }
  }

  @AfterEach
  void assertNoLeakedHandles() throws Exception {
    Object openCount =
        fileA
            .readValue(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount)
            .getValue()
            .getValue();
    assertEquals(ushort(0), openCount, "a test leaked an open file handle");
  }

  // region §5.1 open modes

  @ParameterizedTest
  @ValueSource(
      ints = {0x00, 0x02, 0x04, 0x05, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0E, 0x0F, 0x10, 0x80, 0xFF})
  void illegalOpenModesAreInvalidArgument(int mode) throws Exception {
    // 0x00, bare Write, bare/combined Erase without the legal pair, every Append combination,
    // and the reserved bits 4:7 — all Bad_InvalidArgument (Part 14 overlay on Part 20 §4.2.2)
    FileModelTestClient.CallResult result = fileA.open(ubyte(mode));
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), result.status());
  }

  @Test
  void legalModesOpenAndTheReadWriteBitsGovern() throws Exception {
    // 0x01: readable, not writable
    UInteger readHandle = fileA.openOk(ubyte(0x01));
    try {
      assertEquals(
          ushort(1),
          fileA
              .readValue(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount)
              .getValue()
              .getValue());
      assertTrue(fileA.read(readHandle, 16).status().isGood());
      assertEquals(
          new StatusCode(StatusCodes.Bad_InvalidState),
          fileA.write(readHandle, ByteString.of(new byte[] {1})).status(),
          "Write on a 0x01 handle");
    } finally {
      fileA.closeOk(readHandle);
    }

    // 0x03: readable and writable
    UInteger readWriteHandle = fileA.openOk(ubyte(0x03));
    try {
      assertTrue(fileA.read(readWriteHandle, 16).status().isGood());
      assertTrue(fileA.write(readWriteHandle, ByteString.of(new byte[] {1})).status().isGood());
    } finally {
      fileA.closeOk(readWriteHandle);
    }

    // 0x06: writable, first Read answers Bad_InvalidState (no Read bit)
    UInteger writeHandle = fileA.openOk(ubyte(0x06));
    try {
      assertEquals(
          new StatusCode(StatusCodes.Bad_InvalidState),
          fileA.read(writeHandle, 16).status(),
          "Read on a 0x06 handle");
      assertTrue(fileA.write(writeHandle, ByteString.of(new byte[] {1})).status().isGood());
    } finally {
      fileA.closeOk(writeHandle);
    }
  }

  // endregion

  // region §5.2 locking

  @Test
  void lockingFollowsThePart20ShallSentences() throws Exception {
    // parallel readers are legal, including within one session
    UInteger reader1 = fileA.openOk(ubyte(0x01));
    UInteger reader2 = fileA.openOk(ubyte(0x01));
    try {
      // the trap row: the session's OWN read handles block its write-open (Bad_NotWritable,
      // never Bad_InvalidState)
      assertEquals(new StatusCode(StatusCodes.Bad_NotWritable), fileA.open(ubyte(0x03)).status());

      // another session's readers block a write-open too
      assertEquals(new StatusCode(StatusCodes.Bad_NotWritable), fileB.open(ubyte(0x06)).status());
    } finally {
      fileA.closeOk(reader1);
      fileA.closeOk(reader2);
    }

    // one write handle blocks every open: reads answer Bad_NotReadable, writes Bad_NotWritable
    UInteger writer = fileA.openOk(ubyte(0x06));
    try {
      assertEquals(new StatusCode(StatusCodes.Bad_NotReadable), fileB.open(ubyte(0x01)).status());
      assertEquals(new StatusCode(StatusCodes.Bad_NotReadable), fileA.open(ubyte(0x01)).status());
      assertEquals(new StatusCode(StatusCodes.Bad_NotWritable), fileB.open(ubyte(0x03)).status());
      assertEquals(new StatusCode(StatusCodes.Bad_NotWritable), fileB.open(ubyte(0x06)).status());
    } finally {
      fileA.closeOk(writer);
    }

    // Close released the write lock
    UInteger reopened = fileA.openOk(ubyte(0x03));
    fileA.closeOk(reopened);
  }

  // endregion

  // region §5.3 read/write/position

  @Test
  void readStreamsTheEncodedConfigurationAndEofIsGoodEmpty() throws Exception {
    UInteger handle = fileA.openOk(ubyte(0x01));
    try {
      byte[] bytes = fileA.readAll(handle);

      // the stream is the current configuration file: decodes as the 2-type, carries the
      // mediator-owned ConfigurationVersion, and holds the attach-time connection
      PubSubConfiguration2DataType decoded =
          PubSubConfigFiles.decodeDataType(bytes, clientA.getStaticEncodingContext());
      var managed = (ManagedPubSubService) serverPubSub.runtime();
      assertEquals(managed.configurationVersion(), decoded.getConfigurationVersion());
      assertEquals(1, Objects.requireNonNull(decoded.getConnections()).length);
      assertEquals(CONNECTION, decoded.getConnections()[0].getName());

      // at EOF another Read is Good + empty ByteString, not an error
      FileModelTestClient.CallResult eof = fileA.read(handle, 16);
      assertTrue(eof.status().isGood());
      assertEquals(0, ((ByteString) eof.outputs()[0].getValue()).length());

      // an over-long request clamps to the remaining bytes (min(length, remaining))
      fileA.setPosition(handle, ulong(0));
      FileModelTestClient.CallResult overLong = fileA.read(handle, bytes.length + 1000);
      assertTrue(overLong.status().isGood());
      assertEquals(bytes.length, ((ByteString) overLong.outputs()[0].getValue()).length());

      // non-positive length is Bad_InvalidArgument
      assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), fileA.read(handle, 0).status());
      assertEquals(
          new StatusCode(StatusCodes.Bad_InvalidArgument), fileA.read(handle, -1).status());
    } finally {
      fileA.closeOk(handle);
    }
  }

  @Test
  void writeAndPositionRows() throws Exception {
    UInteger handle = fileA.openOk(ubyte(0x03));
    try {
      long size = positionAfterClampToEof(handle);

      // empty write is a Good no-op: position unchanged
      fileA.setPosition(handle, ulong(2));
      assertTrue(fileA.write(handle, ByteString.of(new byte[0])).status().isGood());
      assertEquals(ulong(2), position(handle));

      // mid-file overwrite advances the position by exactly the bytes written
      fileA.setPosition(handle, ulong(0));
      assertTrue(fileA.write(handle, ByteString.of(new byte[] {9, 9, 9, 9})).status().isGood());
      assertEquals(ulong(4), position(handle));

      // writing at EOF extends the buffer
      fileA.setPosition(handle, ulong(size));
      assertTrue(fileA.write(handle, ByteString.of(new byte[] {1, 2, 3})).status().isGood());
      assertEquals(ulong(size + 3), position(handle));

      // SetPosition past the (new) EOF clamps, never errors
      assertTrue(fileA.setPosition(handle, ulong(size + 1_000_000)).status().isGood());
      assertEquals(ulong(size + 3), position(handle));
    } finally {
      // Close aborts the buffered garbage
      fileA.closeOk(handle);
    }

    // positions are per-handle: two read handles at independent positions read correct slices
    UInteger first = fileA.openOk(ubyte(0x01));
    UInteger second = fileA.openOk(ubyte(0x01));
    try {
      byte[] whole = fileA.readAll(first);

      fileA.setPosition(second, ulong(2));
      ByteString fromSecond = (ByteString) fileA.read(second, 4).outputs()[0].getValue();
      assertEquals(whole[2], fromSecond.bytesOrEmpty()[0]);
      assertEquals(ulong(6), position(second));

      // the first handle's position (EOF) was not disturbed by the second handle's reads
      FileModelTestClient.CallResult eof = fileA.read(first, 16);
      assertEquals(0, ((ByteString) eof.outputs()[0].getValue()).length());
    } finally {
      fileA.closeOk(first);
      fileA.closeOk(second);
    }
  }

  @Test
  void readSnapshotIsMaterializedAtOpen() throws Exception {
    byte[] baseline = fileA.readWholeFile();

    UInteger handle = fileA.openOk(ubyte(0x01));
    try {
      // read half, mutate the LIVE configuration through the managed runtime, read the rest
      int half = baseline.length / 2;
      ByteString firstHalf = (ByteString) fileA.read(handle, half).outputs()[0].getValue();

      serverPubSub.runtime().update(current -> current.toBuilder().enabled(false).build());

      byte[] rest = fileA.readAll(handle);

      byte[] assembled = new byte[firstHalf.length() + rest.length];
      System.arraycopy(firstHalf.bytesOrEmpty(), 0, assembled, 0, firstHalf.length());
      System.arraycopy(rest, 0, assembled, firstHalf.length(), rest.length);

      // the open handle still serves the ORIGINAL stream (materialized AT Open) ...
      assertArrayEquals(baseline, assembled);
    } finally {
      fileA.closeOk(handle);
      serverPubSub.runtime().update(current -> current.toBuilder().enabled(true).build());
    }

    // ... while a FRESH handle sees the post-mutation file (the version advanced twice)
    assertNotEquals(
        PubSubConfigFiles.decodeDataType(baseline, clientA.getStaticEncodingContext())
            .getConfigurationVersion(),
        PubSubConfigFiles.decodeDataType(fileA.readWholeFile(), clientA.getStaticEncodingContext())
            .getConfigurationVersion());
  }

  @Test
  void closeOnAWriteHandleAbortsBufferedWrites() throws Exception {
    byte[] baseline = fileA.readWholeFile();

    UInteger handle = fileA.openOk(ubyte(0x06));
    fileA.writeAll(handle, new byte[] {0x7F, 0x00, 0x55, 42});
    fileA.closeOk(handle);

    // the live configuration file is untouched (no apply, no version bump) ...
    assertArrayEquals(baseline, fileA.readWholeFile());

    // ... and the handle is gone: a second Close answers Bad_InvalidArgument
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), fileA.close(handle).status());
  }

  @Test
  void normativeReadModifyWriteSequenceEndsInAnApply() throws Exception {
    // setup: make the current file longer by adding two generously named writer groups
    applyAddGroups("ft-rw-first-generously-long-writer-group-name", 0x8801);
    applyAddGroups("ft-rw-second-generously-long-writer-group-name", 0x8802);

    // the normative sequence: Open 0x03, read all, rewrite SHORTER in place, CloseAndUpdate
    UInteger handle = fileA.openOk(ubyte(0x03));
    byte[] original = fileA.readAll(handle);

    PubSubConfig shortConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp(CONNECTION)
                    .publisherId(PublisherId.uint16(ushort(4811)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(
                        WriterGroupConfig.builder("ft-rw-first-generously-long-writer-group-name")
                            .enabled(false)
                            .writerGroupId(ushort(0x8801))
                            .publishingInterval(Duration.ofMillis(500))
                            .build())
                    .build())
            .build();
    byte[] shortBuffer =
        PubSubConfigFiles.encodeDataType(
            shortConfig.toDataType(testServer.getServer().getNamespaceTable()),
            clientA.getStaticEncodingContext());
    assertTrue(
        shortBuffer.length < original.length,
        "the modified buffer must be SHORTER than the original for the truncation row");

    fileA.setPosition(handle, ulong(0));
    fileA.writeAll(handle, shortBuffer);

    // remove the SECOND long group: the ref addresses the SHORT buffer's coordinates — a stale
    // (untruncated) tail would not change the decoded first struct, and the applied config must
    // equal the short buffer's semantics
    var removeSecond =
        new PubSubConfigurationRefDataType(
            PubSubConfigurationRefMask.of(
                PubSubConfigurationRefMask.Field.ElementRemove,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup),
            ushort(0),
            ushort(0),
            ushort(0));

    // the short buffer's group 0 is the FIRST long group; removing it leaves only the second
    FileModelTestClient.CallResult result =
        fileA.closeAndUpdate(handle, true, new PubSubConfigurationRefDataType[] {removeSecond});

    assertEquals(StatusCode.GOOD, result.status());
    assertTrue(FileModelTestClient.changesApplied(result));
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(result)[0]);

    // the applied config matches the short buffer's content: the first long group is gone,
    // the second (absent from the short buffer, untouched live) remains
    assertTrue(
        serverPubSub
            .runtime()
            .components()
            .writerGroup(CONNECTION, "ft-rw-first-generously-long-writer-group-name")
            .isEmpty());
    assertTrue(
        serverPubSub
            .runtime()
            .components()
            .writerGroup(CONNECTION, "ft-rw-second-generously-long-writer-group-name")
            .isPresent());
  }

  // endregion

  // region helpers

  private static ULong position(UInteger handle) throws UaException {
    FileModelTestClient.CallResult result = fileA.getPosition(handle);
    assertTrue(result.status().isGood());
    return (ULong) result.outputs()[0].getValue();
  }

  /** SetPosition far past EOF (clamps), then GetPosition: the file size, mode-independent. */
  private static long positionAfterClampToEof(UInteger handle) throws UaException {
    assertTrue(fileA.setPosition(handle, ulong(Long.MAX_VALUE)).status().isGood());
    return position(handle).longValue();
  }

  /** Client-driven setup apply: add a disabled writer group {@code name} under the connection. */
  private static void applyAddGroups(String name, int groupId) throws Exception {
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp(CONNECTION)
                    .publisherId(PublisherId.uint16(ushort(4811)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(
                        WriterGroupConfig.builder(name)
                            .enabled(false)
                            .writerGroupId(ushort(groupId))
                            .publishingInterval(Duration.ofMillis(500))
                            .build())
                    .build())
            .build();

    byte[] fileBytes =
        PubSubConfigFiles.encodeDataType(
            fileConfig.toDataType(testServer.getServer().getNamespaceTable()),
            clientA.getStaticEncodingContext());

    UInteger handle = fileA.openOk(ubyte(0x06));
    fileA.writeAll(handle, fileBytes);

    var addGroup =
        new PubSubConfigurationRefDataType(
            PubSubConfigurationRefMask.of(
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup),
            ushort(0),
            ushort(0),
            ushort(0));

    FileModelTestClient.CallResult result =
        fileA.closeAndUpdate(handle, true, new PubSubConfigurationRefDataType[] {addGroup});
    assertEquals(StatusCode.GOOD, result.status());
    assertTrue(FileModelTestClient.changesApplied(result), "setup apply failed: " + result);
  }

  private static PubSubConfig baseConfig() throws SocketException {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4811)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .build())
        .build();
  }

  private static OpcUaClient connect(String applicationUri) throws UaException {
    OpcUaClient client =
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
                    .setApplicationName(LocalizedText.english("file model test client"))
                    .setApplicationUri(applicationUri)
                    .setIdentityProvider(new AnonymousProvider())
                    .setRequestTimeout(uint(5_000)));

    client.connect();

    return client;
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
