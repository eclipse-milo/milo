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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigFiles;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * CloseAndUpdate matrix A (T5 §6.1) plus the method-level handle/buffer rows (matrix D, §6.4),
 * driven through the REAL wire: every reference crosses the Call service as {@code
 * ExtensionObject[]} (the struct-array decode path), the exactly-five valid Table 239 operation
 * rows answer Good, and every other operation-bit combination, zero/multiple reference bits,
 * Match-on-forbidden-kinds, and out-of-range index rows answer per-element {@code
 * Bad_InvalidArgument} with the method itself Good and full-length {@code ReferencesResults}.
 *
 * <p>Byte-level applier semantics are unit territory ({@code CloseAndUpdateApplierTest}); these
 * rows pin the wire-visible contract, including the D40 one-shot handle rule: a completed
 * CloseAndUpdate — applied, element-failed, or method-failed on the buffer plane — closes the
 * handle and releases the write lock, while a handle/mode failure ({@code Bad_InvalidState}) leaves
 * the handle open.
 */
class CloseAndUpdateRefValidationTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "rv-conn";
  private static final String WRITER_GROUP = "rv-wg";
  private static final String WRITER = "rv-dsw";
  private static final String DATA_SET = "rv-ds";

  private static SksTestServer testServer;
  private static ServerPubSub serverPubSub;
  private static OpcUaClient client;
  private static FileModelTestClient file;

  /** The well-formed file bytes sent by every invalid-mask row (never applied). */
  private static byte[] wellFormedFileBytes;

  @BeforeAll
  static void startServerAndClient() throws Exception {
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

    client = connect();
    file = new FileModelTestClient(client);

    wellFormedFileBytes =
        PubSubConfigFiles.encodeDataType(
            fileConfig().toDataType(testServer.getServer().getNamespaceTable()),
            client.getStaticEncodingContext());
  }

  @AfterAll
  static void stopEverything() throws Exception {
    if (client != null) {
      client.disconnect();
    }
    if (serverPubSub != null) {
      serverPubSub.close();
    }
    if (testServer != null) {
      testServer.close();
    }
  }

  // region matrix A — invalid rows (parameterized)

  record InvalidRow(String name, PubSubConfigurationRefDataType ref) {
    @Override
    public String toString() {
      return name;
    }
  }

  static Stream<InvalidRow> invalidRows() {
    return Stream.of(
        new InvalidRow("zero op bits + one ref bit", ref(0, 0, 0, F.ReferenceConnection)),
        new InvalidRow("mask == 0 (no bits at all)", ref(0, 0, 0)),
        new InvalidRow(
            "Add+Modify", ref(0, 0, 0, F.ElementAdd, F.ElementModify, F.ReferenceConnection)),
        new InvalidRow(
            "Add+Remove", ref(0, 0, 0, F.ElementAdd, F.ElementRemove, F.ReferenceConnection)),
        new InvalidRow(
            "Modify+Remove", ref(0, 0, 0, F.ElementModify, F.ElementRemove, F.ReferenceConnection)),
        new InvalidRow(
            "Match+Modify", ref(0, 0, 0, F.ElementMatch, F.ElementModify, F.ReferenceConnection)),
        new InvalidRow(
            "Match+Remove", ref(0, 0, 0, F.ElementMatch, F.ElementRemove, F.ReferenceConnection)),
        new InvalidRow(
            "Add+Match+Modify",
            ref(0, 0, 0, F.ElementAdd, F.ElementMatch, F.ElementModify, F.ReferenceConnection)),
        new InvalidRow(
            "all four op bits",
            ref(
                0,
                0,
                0,
                F.ElementAdd,
                F.ElementMatch,
                F.ElementModify,
                F.ElementRemove,
                F.ReferenceConnection)),
        new InvalidRow("valid op + ZERO ref bits", ref(0, 0, 0, F.ElementAdd)),
        new InvalidRow(
            "valid op + TWO ref bits",
            ref(0, 0, 0, F.ElementAdd, F.ReferenceConnection, F.ReferenceWriterGroup)),
        new InvalidRow("Match + ReferenceWriter", ref(0, 0, 0, F.ElementMatch, F.ReferenceWriter)),
        new InvalidRow("Match + ReferenceReader", ref(0, 0, 0, F.ElementMatch, F.ReferenceReader)),
        new InvalidRow(
            "Match + ReferencePubDataset", ref(0, 0, 0, F.ElementMatch, F.ReferencePubDataset)),
        new InvalidRow(
            "Match + ReferenceSubDataset", ref(0, 0, 0, F.ElementMatch, F.ReferenceSubDataset)),
        new InvalidRow(
            "Match + ReferenceSecurityGroup",
            ref(0, 0, 0, F.ElementMatch, F.ReferenceSecurityGroup)),
        new InvalidRow(
            "ConnectionIndex out of range", ref(0, 9, 0, F.ElementModify, F.ReferenceConnection)),
        new InvalidRow(
            "GroupIndex out of range", ref(0, 0, 9, F.ElementModify, F.ReferenceWriterGroup)),
        new InvalidRow(
            "ElementIndex out of range", ref(9, 0, 0, F.ElementModify, F.ReferenceWriter)));
  }

  @ParameterizedTest
  @MethodSource("invalidRows")
  void invalidMaskRowsAnswerPerElementInvalidArgument(InvalidRow row) throws Exception {
    UInteger handle = file.openOk(ubyte(0x06));
    file.writeAll(handle, wellFormedFileBytes);

    FileModelTestClient.CallResult result =
        file.closeAndUpdate(handle, false, new PubSubConfigurationRefDataType[] {row.ref()});

    // method-level Good; the failure lives in the (full-length) ReferencesResults slot
    assertEquals(StatusCode.GOOD, result.status(), row.name());
    StatusCode[] referencesResults = FileModelTestClient.referencesResults(result);
    assertEquals(1, referencesResults.length, row.name());
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), referencesResults[0], row.name());
    assertFalse(FileModelTestClient.changesApplied(result), row.name());
  }

  // endregion

  // region matrix A — the five valid rows + mixed-validity evaluation

  @Test
  void exactlyFiveValidOperationRowsSucceedOverTheWire() throws Exception {
    // Modify (identical writer group payload) + Add (a second group) in one call
    PubSubConfig withAddedGroup =
        fileConfigBuilder()
            .connection(
                connectionBuilder()
                    .writerGroup(writerGroup(WRITER_GROUP, 10, true).build())
                    .writerGroup(writerGroup("rv-added", 0x8ABC, false).build())
                    .build())
            .build();

    FileModelTestClient.CallResult first =
        applyFile(
            withAddedGroup,
            false,
            ref(0, 0, 0, F.ElementModify, F.ReferenceWriterGroup),
            ref(0, 0, 1, F.ElementAdd, F.ReferenceWriterGroup));
    assertEquals(StatusCode.GOOD, first.status());
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(first)[0]);
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(first)[1]);
    assertTrue(FileModelTestClient.changesApplied(first));
    assertTrue(serverPubSub.runtime().components().writerGroup(CONNECTION, "rv-added").isPresent());

    // Match + Add+Match against the SAME match-shaped connection element: pure match and the
    // add-if-missing idiom resolving to a match — nothing is added
    PubSubConfiguration2DataType matchFile =
        fileConfig().toDataType(testServer.getServer().getNamespaceTable());
    matchFile.getConnections()[0] = matchShaped(matchFile.getConnections()[0]);

    FileModelTestClient.CallResult second =
        applyWire(
            matchFile,
            false,
            ref(0, 0, 0, F.ElementMatch, F.ReferenceConnection),
            ref(0, 0, 0, F.ElementAdd, F.ElementMatch, F.ReferenceConnection));
    assertEquals(StatusCode.GOOD, second.status());
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(second)[0]);
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(second)[1]);

    PubSubConfiguration2DataType current =
        PubSubConfigFiles.decodeDataType(file.readWholeFile(), client.getStaticEncodingContext());
    assertEquals(1, Objects.requireNonNull(current.getConnections()).length, "match never adds");

    // Remove the added group (name-bound through the file element)
    PubSubConfig removeShape =
        fileConfigBuilder()
            .connection(
                connectionBuilder()
                    .writerGroup(writerGroup("rv-added", 0x8ABC, false).build())
                    .build())
            .build();

    FileModelTestClient.CallResult third =
        applyFile(removeShape, false, ref(0, 0, 0, F.ElementRemove, F.ReferenceWriterGroup));
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(third)[0]);
    assertTrue(serverPubSub.runtime().components().writerGroup(CONNECTION, "rv-added").isEmpty());
  }

  @Test
  void mixedValidityIsEvaluatedPerReferenceInBothModes() throws Exception {
    PubSubConfig withGroup =
        fileConfigBuilder()
            .connection(
                connectionBuilder()
                    .writerGroup(writerGroup("rv-mixed", 0x8ABD, false).build())
                    .build())
            .build();

    // atomic: the bad slot is coded, the valid slot is EVALUATED Good, nothing applied
    FileModelTestClient.CallResult atomic =
        applyFile(
            withGroup,
            true,
            ref(0, 0, 0, F.ElementAdd, F.ElementModify, F.ReferenceConnection),
            ref(0, 0, 0, F.ElementAdd, F.ReferenceWriterGroup));
    assertEquals(StatusCode.GOOD, atomic.status());
    StatusCode[] atomicResults = FileModelTestClient.referencesResults(atomic);
    assertEquals(2, atomicResults.length, "full-length results in atomic mode");
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), atomicResults[0]);
    assertEquals(StatusCode.GOOD, atomicResults[1]);
    assertFalse(FileModelTestClient.changesApplied(atomic));
    assertTrue(serverPubSub.runtime().components().writerGroup(CONNECTION, "rv-mixed").isEmpty());

    // partial: the same mix applies the survivor
    FileModelTestClient.CallResult partial =
        applyFile(
            withGroup,
            false,
            ref(0, 0, 0, F.ElementAdd, F.ElementModify, F.ReferenceConnection),
            ref(0, 0, 0, F.ElementAdd, F.ReferenceWriterGroup));
    assertEquals(StatusCode.GOOD, partial.status());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InvalidArgument),
        FileModelTestClient.referencesResults(partial)[0]);
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(partial)[1]);
    assertTrue(FileModelTestClient.changesApplied(partial));
    assertTrue(serverPubSub.runtime().components().writerGroup(CONNECTION, "rv-mixed").isPresent());

    // cleanup: remove the applied survivor so later rows see a stable baseline
    FileModelTestClient.CallResult cleanup =
        applyFile(withGroup, false, ref(0, 0, 0, F.ElementRemove, F.ReferenceWriterGroup));
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(cleanup)[0]);
  }

  // endregion

  // region matrix D — method-level handle/buffer rows

  @Test
  void unknownHandleIsInvalidArgument() throws Exception {
    FileModelTestClient.CallResult result =
        file.closeAndUpdate(
            uint(999_999),
            false,
            new PubSubConfigurationRefDataType[] {
              ref(0, 0, 0, F.ElementMatch, F.ReferenceConnection)
            });
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), result.status());
  }

  @Test
  void readOnlyHandleIsInvalidStateAndStaysOpen() throws Exception {
    UInteger handle = file.openOk(ubyte(0x01));

    FileModelTestClient.CallResult result =
        file.closeAndUpdate(
            handle,
            false,
            new PubSubConfigurationRefDataType[] {
              ref(0, 0, 0, F.ElementMatch, F.ReferenceConnection)
            });
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidState), result.status());

    // the handle/mode check failed BEFORE the D40 closure point: the handle is still open
    assertTrue(file.close(handle).status().isGood());
  }

  @Test
  void garbageBufferIsTypeMismatchAndTheHandleIsClosed() throws Exception {
    UInteger handle = file.openOk(ubyte(0x06));
    file.writeAll(handle, new byte[] {1, 2, 3, 4, 5, 6, 7, 8});

    FileModelTestClient.CallResult result =
        file.closeAndUpdate(
            handle,
            false,
            new PubSubConfigurationRefDataType[] {
              ref(0, 0, 0, F.ElementMatch, F.ReferenceConnection)
            });
    assertEquals(new StatusCode(StatusCodes.Bad_TypeMismatch), result.status());

    assertHandleClosedAndLockReleased(handle);
  }

  @Test
  void emptyAndNullReferencesAreNothingToDoAndTheHandleIsClosed() throws Exception {
    UInteger handle = file.openOk(ubyte(0x06));
    file.writeAll(handle, wellFormedFileBytes);

    FileModelTestClient.CallResult result =
        file.closeAndUpdate(handle, false, new PubSubConfigurationRefDataType[0]);
    assertEquals(new StatusCode(StatusCodes.Bad_NothingToDo), result.status());
    assertHandleClosedAndLockReleased(handle);

    // the null-array variant behaves identically
    UInteger second = file.openOk(ubyte(0x06));
    file.writeAll(second, wellFormedFileBytes);

    FileModelTestClient.CallResult nullRefs = file.closeAndUpdate(second, false, null);
    assertEquals(new StatusCode(StatusCodes.Bad_NothingToDo), nullRefs.status());
    assertHandleClosedAndLockReleased(second);
  }

  /** D40/FT §9.3: the completed call consumed the handle and released the write lock. */
  private static void assertHandleClosedAndLockReleased(UInteger handle) throws Exception {
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), file.close(handle).status());
    assertEquals(
        ushort(0),
        file.readValue(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount)
            .getValue()
            .getValue());

    UInteger reopened = file.openOk(ubyte(0x06));
    file.closeOk(reopened);
  }

  // endregion

  // region fixtures + helpers

  /** Shorthand for the Table 239 mask fields. */
  private static final class F {
    static final PubSubConfigurationRefMask.Field ElementAdd =
        PubSubConfigurationRefMask.Field.ElementAdd;
    static final PubSubConfigurationRefMask.Field ElementMatch =
        PubSubConfigurationRefMask.Field.ElementMatch;
    static final PubSubConfigurationRefMask.Field ElementModify =
        PubSubConfigurationRefMask.Field.ElementModify;
    static final PubSubConfigurationRefMask.Field ElementRemove =
        PubSubConfigurationRefMask.Field.ElementRemove;
    static final PubSubConfigurationRefMask.Field ReferenceWriter =
        PubSubConfigurationRefMask.Field.ReferenceWriter;
    static final PubSubConfigurationRefMask.Field ReferenceReader =
        PubSubConfigurationRefMask.Field.ReferenceReader;
    static final PubSubConfigurationRefMask.Field ReferenceWriterGroup =
        PubSubConfigurationRefMask.Field.ReferenceWriterGroup;
    static final PubSubConfigurationRefMask.Field ReferenceConnection =
        PubSubConfigurationRefMask.Field.ReferenceConnection;
    static final PubSubConfigurationRefMask.Field ReferencePubDataset =
        PubSubConfigurationRefMask.Field.ReferencePubDataset;
    static final PubSubConfigurationRefMask.Field ReferenceSubDataset =
        PubSubConfigurationRefMask.Field.ReferenceSubDataset;
    static final PubSubConfigurationRefMask.Field ReferenceSecurityGroup =
        PubSubConfigurationRefMask.Field.ReferenceSecurityGroup;
  }

  private static PubSubConfigurationRefDataType ref(
      int elementIndex,
      int connectionIndex,
      int groupIndex,
      PubSubConfigurationRefMask.Field... fields) {

    return new PubSubConfigurationRefDataType(
        PubSubConfigurationRefMask.of(fields),
        ushort(elementIndex),
        ushort(connectionIndex),
        ushort(groupIndex));
  }

  /** A match-shaped element: name and PublisherId null, structural fields intact (CU §3.2). */
  private static PubSubConnectionDataType matchShaped(PubSubConnectionDataType value) {
    return new PubSubConnectionDataType(
        null,
        value.getEnabled(),
        Variant.NULL_VALUE,
        value.getTransportProfileUri(),
        value.getAddress(),
        value.getConnectionProperties(),
        value.getTransportSettings(),
        null,
        null);
  }

  private static FileModelTestClient.CallResult applyFile(
      PubSubConfig fileConfig,
      boolean requireCompleteUpdate,
      PubSubConfigurationRefDataType... refs)
      throws Exception {

    return applyWire(
        fileConfig.toDataType(testServer.getServer().getNamespaceTable()),
        requireCompleteUpdate,
        refs);
  }

  private static FileModelTestClient.CallResult applyWire(
      PubSubConfiguration2DataType wire,
      boolean requireCompleteUpdate,
      PubSubConfigurationRefDataType... refs)
      throws Exception {

    byte[] bytes = PubSubConfigFiles.encodeDataType(wire, client.getStaticEncodingContext());

    UInteger handle = file.openOk(ubyte(0x06));
    file.writeAll(handle, bytes);
    return file.closeAndUpdate(handle, requireCompleteUpdate, refs);
  }

  private static int basePort;

  /**
   * The live baseline: {@code rv-conn} with writer group {@code rv-wg} (disabled writer {@code
   * rv-dsw}) and published dataset {@code rv-ds}; everything but the connection disabled.
   */
  private static PubSubConfig baseConfig() throws SocketException {
    basePort = freeUdpPort();
    return fileConfig();
  }

  /** The same shape re-encoded as the well-formed file every row uploads. */
  private static PubSubConfig fileConfig() {
    return fileConfigBuilder()
        .connection(
            connectionBuilder().writerGroup(writerGroup(WRITER_GROUP, 10, true).build()).build())
        .build();
  }

  private static PubSubConfig.Builder fileConfigBuilder() {
    return PubSubConfig.builder().publishedDataSet(dataSet());
  }

  private static PublishedDataSetConfig dataSet() {
    return PublishedDataSetConfig.builder(DATA_SET)
        .field(
            FieldDefinition.builder("value")
                .dataType(NodeIds.Double)
                .dataSetFieldId(new java.util.UUID(0L, 0xC1L))
                .build())
        .build();
  }

  private static UdpConnectionConfig.Builder connectionBuilder() {
    return PubSubConnectionConfig.udp(CONNECTION)
        .publisherId(PublisherId.uint16(ushort(4831)))
        .address(UdpDatagramAddress.unicast("127.0.0.1", basePort));
  }

  private static WriterGroupConfig.Builder writerGroup(String name, int id, boolean withWriter) {
    WriterGroupConfig.Builder builder =
        WriterGroupConfig.builder(name)
            .enabled(false)
            .writerGroupId(ushort(id))
            .publishingInterval(Duration.ofMillis(500));
    if (withWriter) {
      builder.dataSetWriter(
          DataSetWriterConfig.builder(WRITER)
              .enabled(false)
              .dataSet(dataSet().ref())
              .dataSetWriterId(ushort(20))
              .build());
    }
    return builder;
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
                    .setApplicationName(LocalizedText.english("ref validation test client"))
                    .setApplicationUri("urn:eclipse:milo:test:ref-validation-client")
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
