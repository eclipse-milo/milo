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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigFiles;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationValueDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * CloseAndUpdate matrices B/C/E/F/G over the REAL wire (T5 §6.2–6.7), focused on the wire-visible
 * contract the unit-level {@code CloseAndUpdateApplierTest} cannot prove: a whole subtree added in
 * one call with the {@code ConfigurationObjects} NodeIds resolvable by a real client, {@code
 * ConfigurationValues} auto-assignment entries decoded from the wire (typed UInt64 PublisherId,
 * writer-group ids from the caller's OWN outstanding ReserveIds block, foreign sessions'
 * reservations avoided), atomic-mode nothing-applied proven by re-reading the FILE through a fresh
 * read handle, parent-failure {@code Bad_NotFound} propagation, remove-cascade with the fragment
 * subtree gone and the {@code NodeId.NULL_VALUE} remove slot, the ConfigurationVersion advance (ns0
 * {@code i=25481} and the file wire form), and the empty ConfigurationObjects shape without the
 * information model.
 *
 * <p>Per-test {@link ServerPubSub} attach for a stable baseline; two clients for the cross-session
 * reservation rows.
 */
class CloseAndUpdateApplyIntegrationTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "ap-conn";
  private static final String WRITER_GROUP = "ap-wg";
  private static final String WRITER = "ap-dsw";
  private static final String DATA_SET = "ap-ds";

  private static SksTestServer testServer;
  private static OpcUaClient clientA;
  private static OpcUaClient clientB;
  private static FileModelTestClient fileA;
  private static FileModelTestClient fileB;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();

  private int basePort;

  @BeforeAll
  static void startServerAndClients() throws Exception {
    testServer = SksTestServer.create(null);
    clientA = connect("urn:eclipse:milo:test:apply-client-a");
    clientB = connect("urn:eclipse:milo:test:apply-client-b");
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
  void addedSubtreeMaterializesWithResolvableConfigurationObjects() throws Exception {
    ServerPubSub serverPubSub = attach(true);

    // one call adds connection + group + writer with one ref PER ELEMENT. An ElementAdd maps
    // its WHOLE payload (nested children included), so each ref gets its own file entry:
    // duplicate file names are legal (§9.1.3.7 processing rules) and bind children to the
    // working state by name — PubSubConfig cannot hold duplicates, so the entries are built
    // separately and merged at the wire level
    int port = freeUdpPort();
    PubSubConnectionDataType bareConnection =
        connectionEntry("ap-conn2", port); // index 0: the connection add, no children
    PubSubConnectionDataType groupEntry =
        connectionEntry("ap-conn2", port, writerGroupBuilder("ap-wg2", 0x7001).build());
    PubSubConnectionDataType writerEntry =
        connectionEntry(
            "ap-conn2",
            port,
            writerGroupBuilder("ap-wg2", 0x7001)
                .dataSetWriter(
                    DataSetWriterConfig.builder("ap-dsw2")
                        .enabled(false)
                        .dataSet(dataSet().ref())
                        .dataSetWriterId(ushort(0x7002))
                        .build())
                .build());
    PubSubConfiguration2DataType wire =
        wireWithConnections(bareConnection, groupEntry, writerEntry);

    FileModelTestClient.CallResult result =
        applyWire(
            fileA,
            wire,
            true,
            ref(0, 0, 0, F.ElementAdd, F.ReferenceConnection),
            ref(0, 1, 0, F.ElementAdd, F.ReferenceWriterGroup),
            ref(0, 2, 0, F.ElementAdd, F.ReferenceWriter));

    assertEquals(StatusCode.GOOD, result.status());
    assertTrue(FileModelTestClient.changesApplied(result));
    for (StatusCode code : FileModelTestClient.referencesResults(result)) {
      assertEquals(StatusCode.GOOD, code);
    }

    // the runtime carries the whole subtree
    assertTrue(serverPubSub.runtime().components().connection("ap-conn2").isPresent());
    assertTrue(serverPubSub.runtime().components().writerGroup("ap-conn2", "ap-wg2").isPresent());
    assertTrue(
        serverPubSub
            .runtime()
            .components()
            .dataSetWriter("ap-conn2", "ap-wg2", "ap-dsw2")
            .isPresent());

    // ConfigurationObjects is length-matched, carries the deterministic fragment NodeIds,
    // and every id resolves for a REAL client (the model rebuilt before the method returned)
    NodeId[] objects = FileModelTestClient.configurationObjects(result);
    assertNotNull(objects);
    assertEquals(3, objects.length);

    UShort idx = testServer.getServer().getServerNamespace().getNamespaceIndex();
    assertEquals(PubSubNodeIds.componentNodeId(idx, "ap-conn2"), objects[0]);
    assertEquals(PubSubNodeIds.componentNodeId(idx, "ap-conn2/ap-wg2"), objects[1]);
    assertEquals(PubSubNodeIds.componentNodeId(idx, "ap-conn2/ap-wg2/ap-dsw2"), objects[2]);
    for (NodeId objectId : objects) {
      assertNodePresent(objectId);
    }

    // nothing was auto-assigned (explicit names and ids) — no ConfigurationValues entries
    assertTrue(fileA.configurationValues(result).isEmpty());
  }

  @Test
  void autoAssignmentHonorsOwnReservationsAndAvoidsForeignOnes() throws Exception {
    attach(true);

    // both sessions reserve; the blocks are disjoint and the outputs are typed (UDP profile
    // PublisherId defaults are UInt64)
    Set<Integer> reservedByA = new TreeSet<>();
    Set<Integer> reservedByB = new TreeSet<>();

    FileModelTestClient.CallResult reserveA =
        fileA.reserveIds(profileUdpUadp(), ushort(2), ushort(0));
    assertEquals(StatusCode.GOOD, reserveA.status());
    assertInstanceOf(ULong.class, reserveA.outputs()[0].getValue(), "DefaultPublisherId type");
    for (UShort id : (UShort[]) reserveA.outputs()[1].getValue()) {
      assertTrue(id.intValue() >= 0x8000, "server-assigned ids come from 0x8000-0xFFFF");
      reservedByA.add(id.intValue());
    }
    assertEquals(2, reservedByA.size());

    FileModelTestClient.CallResult reserveB =
        fileB.reserveIds(profileUdpUadp(), ushort(2), ushort(0));
    for (UShort id : (UShort[]) reserveB.outputs()[1].getValue()) {
      reservedByB.add(id.intValue());
    }
    assertTrue(
        reservedByA.stream().noneMatch(reservedByB::contains),
        "reservation blocks of different sessions must be disjoint");

    // session A adds a connection with null name + null PublisherId and a group with id 0 +
    // null name: the assignments surface in ConfigurationValues (self-correlating)
    PubSubConfiguration2DataType wire =
        fileConfigWithExtraConnection().toDataType(testServer.getServer().getNamespaceTable());
    wire.getConnections()[1] = withNullNameAndPublisherId(wire.getConnections()[1]);
    WriterGroupDataType[] groups =
        Objects.requireNonNull(wire.getConnections()[0].getWriterGroups());
    groups[0] = withNullNameAndId(groups[0]);

    FileModelTestClient.CallResult result =
        applyWire(
            fileA,
            wire,
            true,
            ref(0, 1, 0, F.ElementAdd, F.ReferenceConnection),
            ref(0, 0, 0, F.ElementAdd, F.ReferenceWriterGroup));

    assertEquals(StatusCode.GOOD, result.status());
    assertTrue(FileModelTestClient.changesApplied(result));
    for (StatusCode code : FileModelTestClient.referencesResults(result)) {
      assertEquals(StatusCode.GOOD, code);
    }

    List<PubSubConfigurationValueDataType> values = fileA.configurationValues(result);
    assertEquals(2, values.size(), "one entry per assignment-bearing Add ref");

    PubSubConfigurationValueDataType connectionEntry =
        values.stream()
            .filter(v -> v.getIdentifier() != null && v.getIdentifier().getValue() instanceof ULong)
            .findFirst()
            .orElseThrow(() -> new AssertionError("expected a UInt64 PublisherId entry"));
    assertNotNull(connectionEntry.getName());
    assertFalse(connectionEntry.getName().isEmpty());

    PubSubConfigurationValueDataType groupEntry =
        values.stream()
            .filter(
                v -> v.getIdentifier() != null && v.getIdentifier().getValue() instanceof UShort)
            .findFirst()
            .orElseThrow(() -> new AssertionError("expected a UInt16 WriterGroupId entry"));
    int assignedGroupId = ((UShort) groupEntry.getIdentifier().getValue()).intValue();

    // the assignment came from A's OWN outstanding reservation, never from B's block
    assertTrue(
        reservedByA.contains(assignedGroupId),
        "assigned id %d must come from the session's reservation %s"
            .formatted(assignedGroupId, reservedByA));
    assertFalse(reservedByB.contains(assignedGroupId));

    // session B's auto-assign draws from B's block (and cannot collide with A's consumed id);
    // the group shape is writer-less (a nested writer payload would carry colliding wire ids)
    PubSubConfiguration2DataType wireB =
        wireWithConnections(
            connectionEntry(
                CONNECTION, basePort, writerGroupBuilder("ap-auto-b-shape", 0x7998).build()));
    WriterGroupDataType[] groupsB =
        Objects.requireNonNull(wireB.getConnections()[0].getWriterGroups());
    groupsB[0] = withNullNameAndId(groupsB[0]);

    FileModelTestClient.CallResult resultB =
        applyWire(fileB, wireB, true, ref(0, 0, 0, F.ElementAdd, F.ReferenceWriterGroup));
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(resultB)[0]);

    List<PubSubConfigurationValueDataType> valuesB = fileB.configurationValues(resultB);
    int assignedToB =
        valuesB.stream()
            .filter(
                v -> v.getIdentifier() != null && v.getIdentifier().getValue() instanceof UShort)
            .map(v -> ((UShort) v.getIdentifier().getValue()).intValue())
            .findFirst()
            .orElseThrow(() -> new AssertionError("expected a UInt16 WriterGroupId entry"));
    assertTrue(reservedByB.contains(assignedToB));
  }

  @Test
  void atomicModeAppliesNothingOnAnyElementFailure() throws Exception {
    ServerPubSub serverPubSub = attach(true);

    byte[] baseline = fileA.readWholeFile();

    // a valid group Add alongside a duplicate-name connection Add, atomic mode; the file
    // legally carries the duplicate name (merged at the wire level — the builder cannot)
    PubSubConfiguration2DataType wire =
        wireWithConnections(
            connectionEntry(
                CONNECTION, basePort, writerGroupBuilder("ap-atomic-wg", 0x7101).build()),
            connectionEntry(CONNECTION, freeUdpPort()));

    FileModelTestClient.CallResult result =
        applyWire(
            fileA,
            wire,
            true,
            ref(0, 0, 0, F.ElementAdd, F.ReferenceWriterGroup),
            ref(0, 1, 0, F.ElementAdd, F.ReferenceConnection));

    assertEquals(StatusCode.GOOD, result.status());
    assertFalse(FileModelTestClient.changesApplied(result));

    StatusCode[] referencesResults = FileModelTestClient.referencesResults(result);
    assertEquals(2, referencesResults.length);
    assertEquals(StatusCode.GOOD, referencesResults[0], "valid refs are evaluated, not applied");
    assertEquals(new StatusCode(StatusCodes.Bad_BrowseNameDuplicated), referencesResults[1]);

    // NOTHING was applied: the configuration file is byte-identical through a fresh handle
    assertArrayEquals(baseline, fileA.readWholeFile());
    assertTrue(
        serverPubSub.runtime().components().writerGroup(CONNECTION, "ap-atomic-wg").isEmpty());
  }

  @Test
  void partialModePropagatesParentFailureAndAppliesSurvivors() throws Exception {
    ServerPubSub serverPubSub = attach(true);

    // file: [0] a duplicate-name connection (its add fails) carrying a child group, [1] the
    // live connection as a name anchor carrying a valid new group (survives); the duplicate
    // name is legal in the FILE and merged at the wire level
    PubSubConfiguration2DataType wire =
        wireWithConnections(
            connectionEntry(
                CONNECTION, freeUdpPort(), writerGroupBuilder("ap-orphan-wg", 0x7201).build()),
            connectionEntry(
                CONNECTION, basePort, writerGroupBuilder("ap-survivor-wg", 0x7202).build()));

    FileModelTestClient.CallResult result =
        applyWire(
            fileA,
            wire,
            false,
            ref(0, 0, 0, F.ElementAdd, F.ReferenceConnection),
            ref(0, 0, 0, F.ElementAdd, F.ReferenceWriterGroup),
            ref(0, 1, 0, F.ElementAdd, F.ReferenceWriterGroup));

    assertEquals(StatusCode.GOOD, result.status());
    StatusCode[] referencesResults = FileModelTestClient.referencesResults(result);
    assertEquals(new StatusCode(StatusCodes.Bad_BrowseNameDuplicated), referencesResults[0]);
    assertEquals(
        new StatusCode(StatusCodes.Bad_NotFound),
        referencesResults[1],
        "children of a failed parent add inherit Bad_NotFound");
    assertEquals(StatusCode.GOOD, referencesResults[2]);

    assertTrue(FileModelTestClient.changesApplied(result));
    assertTrue(
        serverPubSub.runtime().components().writerGroup(CONNECTION, "ap-survivor-wg").isPresent());
    assertTrue(
        serverPubSub.runtime().components().writerGroup(CONNECTION, "ap-orphan-wg").isEmpty());
  }

  @Test
  void removeCascadesAndAdvancesTheConfigurationVersion() throws Exception {
    ServerPubSub serverPubSub = attach(true);

    UShort idx = testServer.getServer().getServerNamespace().getNamespaceIndex();
    NodeId connectionNodeId = PubSubNodeIds.componentNodeId(idx, CONNECTION);
    NodeId writerNodeId =
        PubSubNodeIds.componentNodeId(idx, CONNECTION + "/" + WRITER_GROUP + "/" + WRITER);

    // the model subtree exists before the remove
    assertNodePresent(connectionNodeId);
    assertNodePresent(writerNodeId);

    UInteger versionBefore = readNs0Version();

    FileModelTestClient.CallResult result =
        applyFile(fileA, fileConfig(), true, ref(0, 0, 0, F.ElementRemove, F.ReferenceConnection));

    assertEquals(StatusCode.GOOD, result.status());
    assertTrue(FileModelTestClient.changesApplied(result));
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(result)[0]);

    // remove slots carry NodeId.NULL_VALUE
    NodeId[] objects = FileModelTestClient.configurationObjects(result);
    assertNotNull(objects);
    assertEquals(1, objects.length);
    assertEquals(NodeId.NULL_VALUE, objects[0]);

    // the cascade removed the whole subtree — runtime and information model alike
    assertTrue(serverPubSub.runtime().components().connection(CONNECTION).isEmpty());
    assertTrue(
        serverPubSub
            .runtime()
            .components()
            .dataSetWriter(CONNECTION, WRITER_GROUP, WRITER)
            .isEmpty());
    assertNodeGone(connectionNodeId);
    assertNodeGone(writerNodeId);

    // the ConfigurationVersion advanced strictly — ns0 i=25481 and the file agree
    UInteger versionAfter = readNs0Version();
    assertTrue(
        versionAfter.longValue() > versionBefore.longValue(),
        "ConfigurationVersion must be strictly greater after a successful CloseAndUpdate");

    PubSubConfiguration2DataType current =
        PubSubConfigFiles.decodeDataType(fileA.readWholeFile(), clientA.getStaticEncodingContext());
    assertEquals(versionAfter, current.getConfigurationVersion());
    assertTrue(current.getConnections() == null || current.getConnections().length == 0);
  }

  @Test
  void configurationObjectsAreEmptyWithoutTheInformationModel() throws Exception {
    attach(false);

    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .publishedDataSet(dataSet())
            .connection(
                connectionBuilder(basePort)
                    .writerGroup(writerGroupBuilder("ap-noim-wg", 0x7301).build())
                    .build())
            .build();

    FileModelTestClient.CallResult result =
        applyFile(fileA, fileConfig, true, ref(0, 0, 0, F.ElementAdd, F.ReferenceWriterGroup));

    assertEquals(StatusCode.GOOD, result.status());
    assertTrue(FileModelTestClient.changesApplied(result));

    // the all-or-nothing shape: no fragment, no NodeIds — null or empty
    NodeId[] objects = FileModelTestClient.configurationObjects(result);
    assertTrue(objects == null || objects.length == 0);
  }

  @Test
  void addedEventDataSetResolvesConfigurationObjects() throws Exception {
    attach(true);

    // one call adds an (unreferenced) event-source PublishedDataSet through the PUBLISHED_DATA_SET
    // arm; the fragment materializes a PublishedEventsType node the ConfigurationObjects slot must
    // resolve to for a real client
    PubSubConfig fileConfig =
        PubSubConfig.builder().publishedDataSet(eventDataSet("ap-evt-ds")).build();

    FileModelTestClient.CallResult result =
        applyFile(fileA, fileConfig, true, ref(0, 0, 0, F.ElementAdd, F.ReferencePubDataset));

    assertEquals(StatusCode.GOOD, result.status());
    assertTrue(FileModelTestClient.changesApplied(result));
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(result)[0]);

    // ConfigurationObjects carries the deterministic published-dataset NodeId, which resolves for
    // a real client (the model rebuilt before the method returned)
    NodeId[] objects = FileModelTestClient.configurationObjects(result);
    assertNotNull(objects);
    assertEquals(1, objects.length);

    UShort idx = testServer.getServer().getServerNamespace().getNamespaceIndex();
    assertEquals(PubSubNodeIds.publishedDataSetNodeId(idx, "ap-evt-ds"), objects[0]);
    assertNodePresent(objects[0]);

    // an explicit name assigns nothing
    assertTrue(fileA.configurationValues(result).isEmpty());
  }

  // region fixtures + helpers

  /** Shorthand for the Table 239 mask fields. */
  private static final class F {
    static final PubSubConfigurationRefMask.Field ElementAdd =
        PubSubConfigurationRefMask.Field.ElementAdd;
    static final PubSubConfigurationRefMask.Field ElementRemove =
        PubSubConfigurationRefMask.Field.ElementRemove;
    static final PubSubConfigurationRefMask.Field ReferenceWriter =
        PubSubConfigurationRefMask.Field.ReferenceWriter;
    static final PubSubConfigurationRefMask.Field ReferenceWriterGroup =
        PubSubConfigurationRefMask.Field.ReferenceWriterGroup;
    static final PubSubConfigurationRefMask.Field ReferenceConnection =
        PubSubConfigurationRefMask.Field.ReferenceConnection;
    static final PubSubConfigurationRefMask.Field ReferencePubDataset =
        PubSubConfigurationRefMask.Field.ReferencePubDataset;
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

  private UInteger readNs0Version() throws UaException {
    Object value =
        fileA.readValue(NodeIds.PublishSubscribe_ConfigurationVersion).getValue().getValue();
    assertNotNull(value, "ns0 ConfigurationVersion must be maintained");
    return (UInteger) value;
  }

  /** Read the BrowseName attribute raw — never through the client's node cache. */
  private static StatusCode readBrowseNameStatus(NodeId nodeId) throws UaException {
    ReadResponse response =
        clientA.read(
            0.0,
            TimestampsToReturn.Neither,
            List.of(
                new ReadValueId(
                    nodeId, AttributeId.BrowseName.uid(), null, QualifiedName.NULL_VALUE)));
    return Objects.requireNonNull(response.getResults())[0].getStatusCode();
  }

  private static void assertNodePresent(NodeId nodeId) throws UaException {
    assertTrue(readBrowseNameStatus(nodeId).isGood(), "expected node: " + nodeId);
  }

  private static void assertNodeGone(NodeId nodeId) throws UaException {
    assertEquals(
        new StatusCode(StatusCodes.Bad_NodeIdUnknown),
        readBrowseNameStatus(nodeId),
        "expected " + nodeId + " to be gone");
  }

  /** A single-connection wire element built in isolation (duplicate-name merging happens later). */
  private static PubSubConnectionDataType connectionEntry(
      String name, int port, WriterGroupConfig... groups) {

    UdpConnectionConfig.Builder builder =
        PubSubConnectionConfig.udp(name)
            .publisherId(PublisherId.uint16(ushort(4841)))
            .address(UdpDatagramAddress.unicast("127.0.0.1", port));
    for (WriterGroupConfig group : groups) {
      builder.writerGroup(group);
    }

    PubSubConfig config =
        PubSubConfig.builder().publishedDataSet(dataSet()).connection(builder.build()).build();
    return config.toDataType(testServer.getServer().getNamespaceTable()).getConnections()[0];
  }

  /**
   * The baseline wire with its connections array REPLACED — the only way to express the legal
   * duplicate-name file entries the processing rules allow ({@code PubSubConfig} validates
   * uniqueness and cannot hold them).
   */
  private PubSubConfiguration2DataType wireWithConnections(
      PubSubConnectionDataType... connections) {

    PubSubConfiguration2DataType template =
        fileConfig().toDataType(testServer.getServer().getNamespaceTable());
    return new PubSubConfiguration2DataType(
        template.getPublishedDataSets(),
        connections,
        template.getEnabled(),
        template.getSubscribedDataSets(),
        template.getDataSetClasses(),
        template.getDefaultSecurityKeyServices(),
        template.getSecurityGroups(),
        template.getPubSubKeyPushTargets(),
        template.getConfigurationVersion(),
        template.getConfigurationProperties());
  }

  private static FileModelTestClient.CallResult applyFile(
      FileModelTestClient driver,
      PubSubConfig fileConfig,
      boolean requireCompleteUpdate,
      PubSubConfigurationRefDataType... refs)
      throws Exception {

    return applyWire(
        driver,
        fileConfig.toDataType(testServer.getServer().getNamespaceTable()),
        requireCompleteUpdate,
        refs);
  }

  private static FileModelTestClient.CallResult applyWire(
      FileModelTestClient driver,
      PubSubConfiguration2DataType wire,
      boolean requireCompleteUpdate,
      PubSubConfigurationRefDataType... refs)
      throws Exception {

    byte[] bytes =
        PubSubConfigFiles.encodeDataType(wire, driver.client().getStaticEncodingContext());

    UInteger handle = driver.openOk(ubyte(0x06));
    driver.writeAll(handle, bytes);
    return driver.closeAndUpdate(handle, requireCompleteUpdate, refs);
  }

  private static String profileUdpUadp() {
    return PubSubIdReservations.PROFILE_UDP_UADP;
  }

  private ServerPubSub attach(boolean exposeInformationModel) throws Exception {
    basePort = freeUdpPort();
    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            fileConfig(),
            ServerPubSubOptions.builder()
                .exposeInformationModel(exposeInformationModel)
                .allowRemoteConfiguration(true)
                .build());
    attached.add(serverPubSub);
    serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    return serverPubSub;
  }

  /** The baseline: {@code ap-conn} with disabled {@code ap-wg} (writer {@code ap-dsw}). */
  private PubSubConfig fileConfig() {
    return PubSubConfig.builder()
        .publishedDataSet(dataSet())
        .connection(
            connectionBuilder(basePort)
                .writerGroup(
                    writerGroupBuilder(WRITER_GROUP, 10)
                        .dataSetWriter(
                            DataSetWriterConfig.builder(WRITER)
                                .enabled(false)
                                .dataSet(dataSet().ref())
                                .dataSetWriterId(ushort(20))
                                .build())
                        .build())
                .build())
        .build();
  }

  /** The baseline plus a second, distinctly named and addressed connection element. */
  private PubSubConfig fileConfigWithExtraConnection() throws SocketException {
    return PubSubConfig.builder()
        .publishedDataSet(dataSet())
        .connection(
            connectionBuilder(basePort)
                // the placeholder id is patched to 0 ("reads as null") on the wire form
                .writerGroup(writerGroupBuilder("ap-auto-wg-shape", 0x7999).build())
                .build())
        .connection(
            PubSubConnectionConfig.udp("ap-auto-conn-shape")
                .publisherId(PublisherId.uint16(ushort(4845)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .build())
        .build();
  }

  private static PublishedDataSetConfig dataSet() {
    return PublishedDataSetConfig.builder(DATA_SET)
        .field(
            FieldDefinition.builder("value")
                .dataType(NodeIds.Double)
                .dataSetFieldId(new java.util.UUID(0L, 0xA1L))
                .build())
        .build();
  }

  /** An event-source dataset selecting {@code Severity} from the ns0 Server notifier. */
  private static PublishedDataSetConfig eventDataSet(String name) {
    return PublishedDataSetConfig.builder(name)
        .source(
            PublishedEventsConfig.builder(
                    NodeIds.Server.expanded(testServer.getServer().getNamespaceTable()))
                .field(
                    EventFieldDefinition.builder("severity")
                        .selectedField(
                            new SimpleAttributeOperand(
                                NodeIds.BaseEventType,
                                new QualifiedName[] {new QualifiedName(0, "Severity")},
                                AttributeId.Value.uid(),
                                null))
                        .dataType(NodeIds.UInt16)
                        .dataSetFieldId(new java.util.UUID(0L, 0xE5L))
                        .build())
                .build())
        .build();
  }

  private static UdpConnectionConfig.Builder connectionBuilder(int port) {
    return PubSubConnectionConfig.udp(CONNECTION)
        .publisherId(PublisherId.uint16(ushort(4841)))
        .address(UdpDatagramAddress.unicast("127.0.0.1", port));
  }

  private static WriterGroupConfig.Builder writerGroupBuilder(String name, int id) {
    return WriterGroupConfig.builder(name)
        .enabled(false)
        .writerGroupId(ushort(id))
        .publishingInterval(Duration.ofMillis(500));
  }

  private static PubSubConnectionDataType withNullNameAndPublisherId(
      PubSubConnectionDataType value) {
    return new PubSubConnectionDataType(
        null,
        value.getEnabled(),
        Variant.NULL_VALUE,
        value.getTransportProfileUri(),
        value.getAddress(),
        value.getConnectionProperties(),
        value.getTransportSettings(),
        value.getWriterGroups(),
        value.getReaderGroups());
  }

  private static WriterGroupDataType withNullNameAndId(WriterGroupDataType value) {
    return new WriterGroupDataType(
        null,
        value.getEnabled(),
        value.getSecurityMode(),
        value.getSecurityGroupId(),
        value.getSecurityKeyServices(),
        value.getMaxNetworkMessageSize(),
        value.getGroupProperties(),
        ushort(0),
        value.getPublishingInterval(),
        value.getKeepAliveTime(),
        value.getPriority(),
        value.getLocaleIds(),
        value.getHeaderLayoutUri(),
        value.getTransportSettings(),
        value.getMessageSettings(),
        value.getDataSetWriters());
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
                    .setApplicationName(LocalizedText.english("apply integration test client"))
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
