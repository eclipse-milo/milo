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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetListener;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubComponents;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateListener;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.SecurityKeyInfo;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.server.PubSubIdReservations.IdKind;
import org.eclipse.milo.opcua.sdk.pubsub.server.PubSubIdReservations.UsedReservation;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationValueDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * The CloseAndUpdate element-operation matrix, unit-driven against live {@link PubSubConfig} values
 * and a transform-capturing fake service (no server needed): operation-bit validation, index
 * bounds, Match semantics (incl. the GroupHeader guard), removes-first and parent rebinding,
 * cascades and {@code Bad_NotFound} propagation, Modify full-replacement, ElementAdd
 * auto-assignment (names, PublisherId, ids honoring reservations), atomic vs partial apply,
 * top-level field handling, bit-11 guarding, engine-validation attribution, and the
 * ConfigurationValues/ConfigurationObjects outputs. DataSetReader, ReaderGroup-Match,
 * PublishedDataSet, and standalone SubscribedDataSet references have their own applier arms,
 * covered by {@link CloseAndUpdateElementKindsTest}.
 */
class CloseAndUpdateApplierTest {

  private static final NodeId SESSION = new NodeId(1, "session");
  private static final NodeId OTHER_SESSION = new NodeId(1, "other-session");

  private static final Supplier<StatusCode> SKS_ALLOWED = () -> StatusCode.GOOD;

  private static final NamespaceTable NAMESPACE_TABLE = new NamespaceTable();

  private static final String CONN = "conn";
  private static final String WRITER_GROUP = "wg";
  private static final String WRITER = "dsw";
  private static final String READER_GROUP = "rg";
  private static final String DATA_SET = "ds";
  private static final String SECURITY_GROUP = "sg1";

  private final FakePubSubService service = new FakePubSubService(liveConfig());
  private final PubSubIdReservations reservations = new PubSubIdReservations(4840);

  private CloseAndUpdateApplier applier(@Nullable ConfigurationObjectIds objectIds) {
    return new CloseAndUpdateApplier(service, NAMESPACE_TABLE, reservations, objectIds);
  }

  private CloseAndUpdateApplier.Outcome apply(
      boolean requireCompleteUpdate,
      PubSubConfiguration2DataType file,
      PubSubConfigurationRefDataType... references) {

    return applier(null).apply(SESSION, SKS_ALLOWED, file, requireCompleteUpdate, references);
  }

  // region fixtures

  private static PublishedDataSetConfig dataSet(String name) {
    return PublishedDataSetConfig.builder(name)
        .field(
            FieldDefinition.builder("value")
                .source(NodeFieldAddress.of(NodeIds.Server, AttributeId.Value, NAMESPACE_TABLE))
                .dataType(NodeIds.Double)
                .dataSetFieldId(new UUID(0L, 0xF1L))
                .build())
        .build();
  }

  private static WriterGroupConfig.Builder writerGroup(String name, int id) {
    return WriterGroupConfig.builder(name)
        .writerGroupId(ushort(id))
        .publishingInterval(Duration.ofMillis(500));
  }

  private static DataSetWriterConfig writer(String name, int id, PublishedDataSetRef dataSet) {
    return DataSetWriterConfig.builder(name)
        .dataSet(dataSet)
        .dataSetWriterId(ushort(id))
        .fieldContentMask(DataSetFieldContentMask.of(DataSetFieldContentMask.Field.StatusCode))
        .build();
  }

  private static UdpConnectionConfig.Builder connection(String name, int port) {
    return PubSubConnectionConfig.udp(name)
        .publisherId(PublisherId.uint16(ushort(7)))
        .address(UdpDatagramAddress.unicast("127.0.0.1", port));
  }

  /**
   * The live configuration: {@code conn} (UDP) with {@code wg} (id 10, writer {@code dsw} id 20)
   * and an empty reader group {@code rg}; published dataset {@code ds}; security group {@code sg1}.
   */
  private static PubSubConfig liveConfig() {
    PublishedDataSetConfig ds = dataSet(DATA_SET);

    return PubSubConfig.builder()
        .publishedDataSet(ds)
        .securityGroup(
            SecurityGroupConfig.builder(SECURITY_GROUP).keyLifeTime(Duration.ofHours(1)).build())
        .connection(
            connection(CONN, 15001)
                .writerGroup(
                    writerGroup(WRITER_GROUP, 10)
                        .dataSetWriter(writer(WRITER, 20, ds.ref()))
                        .build())
                .readerGroup(ReaderGroupConfig.builder(READER_GROUP).build())
                .build())
        .build();
  }

  /** The live configuration in wire form: well-formed file elements at known indices. */
  private static PubSubConfiguration2DataType fileOf(PubSubConfig config) {
    return config.toDataType(NAMESPACE_TABLE);
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

  private static void assertCode(long expected, StatusCode actual) {
    assertEquals(new StatusCode(expected), actual);
  }

  // endregion

  @Test
  void exactlyFiveOperationBitRowsAreValid() {
    PubSubConfiguration2DataType file = fileOf(liveConfig());

    // valid: Add / Match / Add+Match / Modify / Remove (evaluated; match rows resolve)
    CloseAndUpdateApplier.Outcome valid =
        apply(
            false,
            file,
            // Modify conn (bound by name, identical payload)
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            // Remove rg
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementRemove,
                PubSubConfigurationRefMask.Field.ReferenceReaderGroup));

    assertCode(StatusCodes.Good, valid.referencesResults()[0]);
    assertCode(StatusCodes.Good, valid.referencesResults()[1]);
    assertTrue(valid.changesApplied());

    // invalid combinations: zero op bits, Add+Modify, Add+Remove, Match+Modify, Match+Remove,
    // Modify+Remove — all per-element Bad_InvalidArgument, never a method-level failure
    CloseAndUpdateApplier.Outcome invalid =
        apply(
            false,
            file,
            ref(0, 0, 0, PubSubConfigurationRefMask.Field.ReferenceConnection),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ElementRemove,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementMatch,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementMatch,
                PubSubConfigurationRefMask.Field.ElementRemove,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ElementRemove,
                PubSubConfigurationRefMask.Field.ReferenceConnection));

    assertEquals(6, invalid.referencesResults().length);
    for (StatusCode result : invalid.referencesResults()) {
      assertCode(StatusCodes.Bad_InvalidArgument, result);
    }
    assertFalse(invalid.changesApplied());
  }

  @Test
  void referenceBitAndIndexValidation() {
    PubSubConfiguration2DataType file = fileOf(liveConfig());

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            false,
            file,
            // zero reference bits
            ref(0, 0, 0, PubSubConfigurationRefMask.Field.ElementAdd),
            // two reference bits
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceConnection,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup),
            // bit 12: PushTargets are unmodeled — per-ref Bad_InvalidArgument
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferencePushTarget),
            // Match on a writer reference
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementMatch,
                PubSubConfigurationRefMask.Field.ReferenceWriter),
            // Match with a non-null file element name
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementMatch,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            // ConnectionIndex out of bounds
            ref(
                0,
                9,
                0,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            // GroupIndex out of bounds
            ref(
                0,
                0,
                9,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup),
            // ElementIndex out of bounds
            ref(
                9,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceWriter));

    assertEquals(8, outcome.referencesResults().length);
    for (StatusCode result : outcome.referencesResults()) {
      assertCode(StatusCodes.Bad_InvalidArgument, result);
    }
  }

  @Test
  void malformedRefDoesNotCollapseThePartialApply() {
    // a bit-12 PushTarget ref (valid op bits, no resolvable kind) alongside a valid connection
    // Add: the malformed ref fails per-element and the survivor still applies — per-ref
    // evaluation, never early-abort
    PubSubConfig fileConfig =
        PubSubConfig.builder().connection(connection("conn2", 15002).build()).build();

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            false,
            fileOf(fileConfig),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferencePushTarget),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceConnection));

    assertCode(StatusCodes.Bad_InvalidArgument, outcome.referencesResults()[0]);
    assertCode(StatusCodes.Good, outcome.referencesResults()[1]);
    assertTrue(outcome.changesApplied());
    assertEquals(1, service.applyCount);

    PubSubConfig applied = outcome.appliedConfig();
    assertNotNull(applied);
    assertTrue(applied.connection("conn2").isPresent());
  }

  @Test
  void addConnectionAutoAssignsNameAndPublisherId() {
    // a connection with null name and null PublisherId, at file index 1
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(connection(CONN, 15001).build())
            .connection(
                UdpConnectionConfig.builder("placeholder")
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 15002))
                    .build())
            .build();
    PubSubConfiguration2DataType file = fileOf(fileConfig);
    file.getConnections()[1] = withNullNameAndPublisherId(file.getConnections()[1]);

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            true,
            file,
            ref(
                0,
                1,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceConnection));

    assertCode(StatusCodes.Good, outcome.referencesResults()[0]);
    assertTrue(outcome.changesApplied());
    assertEquals(1, service.applyCount);

    // the auto-assigned name is unique in the working scope
    PubSubConfig applied = outcome.appliedConfig();
    assertNotNull(applied);
    assertTrue(applied.connection("Connection1").isPresent());

    // the assignment is reported, self-correlating via ConfigurationElement, with the UInt64
    // default PublisherId for the datagram profile
    assertEquals(1, outcome.configurationValues().length);
    PubSubConfigurationValueDataType value = outcome.configurationValues()[0];
    assertEquals("Connection1", value.getName());
    assertInstanceOf(ULong.class, value.getIdentifier().value());
  }

  @Test
  void addWriterGroupHonorsSessionReservations() throws Exception {
    // the session reserves ids for the UDP profile; a foreign session reserves too
    var grant =
        reservations.reserve(SESSION, PubSubIdReservations.PROFILE_UDP_UADP, 1, 0, service.config);
    var foreignGrant =
        reservations.reserve(
            OTHER_SESSION, PubSubIdReservations.PROFILE_UDP_UADP, 1, 0, service.config);
    UShort reservedId = grant.writerGroupIds()[0];
    UShort foreignId = foreignGrant.writerGroupIds()[0];

    // file: the live connection plus a new writer group with id 0 (reads as null)
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001).writerGroup(writerGroup("added-wg", 1).build()).build())
            .build();
    PubSubConfiguration2DataType file = fileOf(fileConfig);
    WriterGroupDataType wireGroup = file.getConnections()[0].getWriterGroups()[0];
    file.getConnections()[0].getWriterGroups()[0] = withWriterGroupId(wireGroup, ushort(0));

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            true,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Good, outcome.referencesResults()[0]);
    // the session's outstanding reservation is preferred and reported as consumed
    assertEquals(
        List.of(new UsedReservation(IdKind.WRITER_GROUP, reservedId)),
        List.copyOf(outcome.consumedReservations()));
    assertEquals(1, outcome.configurationValues().length);
    assertEquals(reservedId, outcome.configurationValues()[0].getIdentifier().value());

    // an explicit id reserved by ANOTHER session: per-ref Bad_InvalidArgument
    service.config = liveConfig();
    PubSubConfiguration2DataType foreignFile = fileOf(fileConfig);
    foreignFile.getConnections()[0].getWriterGroups()[0] =
        withWriterGroupId(foreignFile.getConnections()[0].getWriterGroups()[0], foreignId);

    CloseAndUpdateApplier.Outcome foreignOutcome =
        apply(
            true,
            foreignFile,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Bad_InvalidArgument, foreignOutcome.referencesResults()[0]);
    assertFalse(foreignOutcome.changesApplied());
  }

  @Test
  void addDuplicateNameIsBrowseNameDuplicated() {
    PubSubConfiguration2DataType file = fileOf(liveConfig());

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            false,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceConnection));

    assertCode(StatusCodes.Bad_BrowseNameDuplicated, outcome.referencesResults()[0]);
    assertFalse(outcome.changesApplied());
    assertEquals(0, service.applyCount);
  }

  @Test
  void addDuplicateWriterGroupIdIsInvalidArgumentNotBrowseNameDuplicated() {
    // a DISTINCT name with an explicit id colliding within the publisherId scope (the live wg
    // already carries id 10): a wire-id collision, not a name collision — the contract's
    // catch-all Bad_InvalidArgument, never Bad_BrowseNameDuplicated
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(connection(CONN, 15001).writerGroup(writerGroup("wg2", 10).build()).build())
            .build();

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            false,
            fileOf(fileConfig),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Bad_InvalidArgument, outcome.referencesResults()[0]);
    assertFalse(outcome.changesApplied());
    assertEquals(0, service.applyCount);
  }

  @Test
  void wireIdCollisionInvolvingANameContainingNameTokenStaysInvalidArgument() {
    // the collision message interpolates element paths; a group literally named "my name"
    // must not flip the wire-id collision to Bad_BrowseNameDuplicated (the classification
    // anchors on the builder's exact name-duplication message shapes, not substring tokens)
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001).writerGroup(writerGroup("my name", 10).build()).build())
            .build();

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            false,
            fileOf(fileConfig),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Bad_InvalidArgument, outcome.referencesResults()[0]);
    assertFalse(outcome.changesApplied());
    assertEquals(0, service.applyCount);
  }

  @Test
  void modifyIsFullReplacementIncludingChildren() {
    // the file's wg carries NO writers: a Modify deletes the live writer
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001).writerGroup(writerGroup(WRITER_GROUP, 10).build()).build())
            .build();

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            true,
            fileOf(fileConfig),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Good, outcome.referencesResults()[0]);
    PubSubConfig applied = outcome.appliedConfig();
    assertNotNull(applied);
    WriterGroupConfig group = applied.connection(CONN).orElseThrow().writerGroups().get(0);
    assertTrue(group.getDataSetWriters().isEmpty());

    // an unmatched name answers Bad_NoMatch
    PubSubConfig unmatched =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001)
                    .writerGroup(writerGroup("no-such-group", 10).build())
                    .build())
            .build();
    CloseAndUpdateApplier.Outcome noMatch =
        apply(
            false,
            fileOf(unmatched),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));
    assertCode(StatusCodes.Bad_NoMatch, noMatch.referencesResults()[0]);
  }

  @Test
  void removesRunFirstAndReAddedParentsRebindChildren() {
    // one call: remove connection 'conn', add a replacement 'conn' (different address, one
    // payload group), and add a writer group whose file coordinates sit under the ORIGINAL
    // 'conn' file entry: parent binding is by NAME against the working state, so the child
    // binds to the NEW element (duplicate file names are legal). PubSubConfig cannot hold
    // duplicate names, so the replacement's name is patched at the wire level.
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001) // index 0: the remove binding + the child-add element
                    .writerGroup(writerGroup("extra-wg", 43).build())
                    .build())
            .connection(
                connection("conn-replacement", 19999) // index 1: the replacement, payload group
                    .writerGroup(writerGroup("new-wg", 42).build())
                    .build())
            .build();
    PubSubConfiguration2DataType file = fileOf(fileConfig);
    file.getConnections()[1] = withConnectionName(file.getConnections()[1], CONN);

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            true,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementRemove,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            ref(
                0,
                1,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    for (StatusCode result : outcome.referencesResults()) {
      assertCode(StatusCodes.Good, result);
    }
    assertTrue(outcome.changesApplied());

    PubSubConfig applied = outcome.appliedConfig();
    assertNotNull(applied);
    PubSubConnectionConfig conn = applied.connection(CONN).orElseThrow();
    // the new connection: replacement address, both the payload group and the child-added group
    assertEquals(19999, ((UdpConnectionConfig) conn).getAddress().port());
    assertEquals(2, conn.writerGroups().size());
  }

  @Test
  void cascadeRemovalAndNotFoundPropagation() {
    PubSubConfiguration2DataType file = fileOf(liveConfig());

    // removing the writer group cascades to its writer
    CloseAndUpdateApplier.Outcome cascade =
        apply(
            true,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementRemove,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));
    assertTrue(cascade.changesApplied());
    PubSubConfig applied = cascade.appliedConfig();
    assertNotNull(applied);
    assertTrue(applied.connection(CONN).orElseThrow().writerGroups().isEmpty());

    // a child whose parent add FAILED answers Bad_NotFound (dependency propagation)
    service.config = liveConfig();
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001) // duplicate: the parent add fails
                    .writerGroup(writerGroup("child-wg", 43).build())
                    .build())
            .build();

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            false,
            fileOf(fileConfig),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Bad_BrowseNameDuplicated, outcome.referencesResults()[0]);
    assertCode(StatusCodes.Bad_NotFound, outcome.referencesResults()[1]);
  }

  @Test
  void payloadFailedModifyDoesNotPropagateNotFoundToChildren() {
    // ref 0: Modify the live connection with an invalid payload (unsupported transport profile
    // -> Bad_InvalidArgument); ref 1: Add a writer group under the same file connection entry.
    // Bad_NotFound covers parents "not added or matched" ([CU 3.3]) — a payload-failed Modify
    // reverts to last-good and the live parent still exists, so the child binds by name.
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001).writerGroup(writerGroup("survivor-wg", 44).build()).build())
            .build();
    PubSubConfiguration2DataType file = fileOf(fileConfig);
    file.getConnections()[0] =
        withTransportProfileUri(file.getConnections()[0], "urn:not-a-transport-profile");

    PubSubConfigurationRefDataType badModify =
        ref(
            0,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementModify,
            PubSubConfigurationRefMask.Field.ReferenceConnection);
    PubSubConfigurationRefDataType childAdd =
        ref(
            0,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementAdd,
            PubSubConfigurationRefMask.Field.ReferenceWriterGroup);

    // atomic: the child is evaluated Good (not Bad_NotFound), nothing applied
    CloseAndUpdateApplier.Outcome atomic = apply(true, file, badModify, childAdd);
    assertCode(StatusCodes.Bad_InvalidArgument, atomic.referencesResults()[0]);
    assertCode(StatusCodes.Good, atomic.referencesResults()[1]);
    assertFalse(atomic.changesApplied());
    assertEquals(0, service.applyCount);

    // partial: the survivor applies under the live parent
    CloseAndUpdateApplier.Outcome partial = apply(false, file, badModify, childAdd);
    assertCode(StatusCodes.Bad_InvalidArgument, partial.referencesResults()[0]);
    assertCode(StatusCodes.Good, partial.referencesResults()[1]);
    assertTrue(partial.changesApplied());
    PubSubConfig applied = partial.appliedConfig();
    assertNotNull(applied);
    assertTrue(
        applied.connection(CONN).orElseThrow().writerGroups().stream()
            .anyMatch(g -> g.getName().equals("survivor-wg")));
  }

  @Test
  void matchAnchorsWithoutApplyingAndAddMatchAddsWhenUnmatched() {
    // a pure-Match connection ref: name/id null, structural fields identical to the live conn
    PubSubConfig fileConfig =
        PubSubConfig.builder().connection(connection(CONN, 15001).build()).build();
    PubSubConfiguration2DataType file = fileOf(fileConfig);
    file.getConnections()[0] = withNullNameAndPublisherId(file.getConnections()[0]);

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            true,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementMatch,
                PubSubConfigurationRefMask.Field.ReferenceConnection));

    assertCode(StatusCodes.Good, outcome.referencesResults()[0]);
    // match-only: ChangesApplied=false, the reconfigure (and any save) is SKIPPED
    assertFalse(outcome.changesApplied());
    assertEquals(0, service.applyCount);
    // the resolved name and identifier are reported
    assertEquals(1, outcome.configurationValues().length);
    assertEquals(CONN, outcome.configurationValues()[0].getName());
    assertEquals(ushort(7), outcome.configurationValues()[0].getIdentifier().value());

    // an unmatched pattern: Match fails Bad_NoMatch, Add+Match adds instead
    PubSubConfig unmatchedConfig =
        PubSubConfig.builder().connection(connection(CONN, 20000).build()).build();
    PubSubConfiguration2DataType unmatched = fileOf(unmatchedConfig);
    unmatched.getConnections()[0] = withNullNameAndPublisherId(unmatched.getConnections()[0]);

    CloseAndUpdateApplier.Outcome noMatch =
        apply(
            false,
            unmatched,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementMatch,
                PubSubConfigurationRefMask.Field.ReferenceConnection));
    assertCode(StatusCodes.Bad_NoMatch, noMatch.referencesResults()[0]);

    CloseAndUpdateApplier.Outcome addMatch =
        apply(
            false,
            unmatched,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ElementMatch,
                PubSubConfigurationRefMask.Field.ReferenceConnection));
    assertCode(StatusCodes.Good, addMatch.referencesResults()[0]);
    assertTrue(addMatch.changesApplied());
    PubSubConfig applied = addMatch.appliedConfig();
    assertNotNull(applied);
    assertEquals(2, applied.connections().size());
  }

  @Test
  void writerGroupMatchWithActiveGroupHeaderIsInvalidState() {
    // the live group's UADP NetworkMessageContentMask carries GroupHeader
    PublishedDataSetConfig ds = dataSet(DATA_SET);
    service.config =
        PubSubConfig.builder()
            .publishedDataSet(ds)
            .connection(
                connection(CONN, 15001)
                    .writerGroup(
                        writerGroup(WRITER_GROUP, 10)
                            .messageSettings(
                                UadpWriterGroupSettings.builder()
                                    .networkMessageContentMask(
                                        UadpNetworkMessageContentMask.of(
                                            UadpNetworkMessageContentMask.Field.PublisherId,
                                            UadpNetworkMessageContentMask.Field.GroupHeader,
                                            UadpNetworkMessageContentMask.Field.WriterGroupId,
                                            UadpNetworkMessageContentMask.Field.PayloadHeader))
                                    .build())
                            .build())
                    .build())
            .build();

    // the pattern mirrors the live group so the structural match succeeds
    PubSubConfiguration2DataType file = fileOf(service.config);
    file.getConnections()[0].getWriterGroups()[0] =
        withNullNameAndId(file.getConnections()[0].getWriterGroups()[0]);

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            false,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementMatch,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Bad_InvalidState, outcome.referencesResults()[0]);
  }

  @Test
  void writerGroupMatchIgnoresMiloLocalBrokerMetadataFields() {
    // the live MQTT group publishes group-level metadata — a Milo-local feature the group's
    // wire type (BrokerWriterGroupTransportDataType) cannot carry — so a file element
    // identical in every pinned field must still match
    service.config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.mqtt(CONN)
                    .publisherId(PublisherId.uint16(ushort(7)))
                    .brokerUri(URI.create("mqtt://localhost:1883"))
                    .writerGroup(
                        writerGroup(WRITER_GROUP, 10)
                            .brokerTransport(
                                BrokerTransportSettings.builder()
                                    .queueName("data-topic")
                                    .metaDataQueueName("meta-topic")
                                    .metaDataUpdateTime(Duration.ofSeconds(5))
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubConfiguration2DataType file = fileOf(service.config);
    file.getConnections()[0].getWriterGroups()[0] =
        withNullNameAndId(file.getConnections()[0].getWriterGroups()[0]);

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            false,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementMatch,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Good, outcome.referencesResults()[0]);
    assertFalse(outcome.changesApplied());
    assertEquals(1, outcome.configurationValues().length);
    assertEquals(ushort(10), outcome.configurationValues()[0].getIdentifier().value());
  }

  @Test
  void writerGroupMatchWithNonEmptyHeaderLayoutUriNeverMatches() {
    // a live UADP group without GroupHeader (so the Bad_InvalidState guard stays out of the way)
    service.config =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001)
                    .writerGroup(
                        writerGroup(WRITER_GROUP, 10)
                            .messageSettings(
                                UadpWriterGroupSettings.builder()
                                    .networkMessageContentMask(
                                        UadpNetworkMessageContentMask.of(
                                            UadpNetworkMessageContentMask.Field.PublisherId,
                                            UadpNetworkMessageContentMask.Field.WriterGroupId,
                                            UadpNetworkMessageContentMask.Field.PayloadHeader))
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubConfiguration2DataType file = fileOf(service.config);
    file.getConnections()[0].getWriterGroups()[0] =
        withNullNameAndId(file.getConnections()[0].getWriterGroups()[0]);

    PubSubConfigurationRefDataType matchRef =
        ref(
            0,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementMatch,
            PubSubConfigurationRefMask.Field.ReferenceWriterGroup);

    // control: without a HeaderLayoutUri the pattern matches the live group structurally
    CloseAndUpdateApplier.Outcome control = apply(false, file, matchRef);
    assertCode(StatusCodes.Good, control.referencesResults()[0]);

    // HeaderLayoutUri is IN the pinned [CU §3.2] field set but has no config counterpart (the
    // mapper drops it), so a live Milo group can never carry one: a pattern demanding a
    // non-empty layout differs in a pinned field and must never match (wire-level guard)
    file.getConnections()[0].getWriterGroups()[0] =
        withHeaderLayoutUri(
            file.getConnections()[0].getWriterGroups()[0],
            "http://opcfoundation.org/UA/PubSub-Layouts/UADP-Dynamic");

    CloseAndUpdateApplier.Outcome noMatch = apply(false, file, matchRef);
    assertCode(StatusCodes.Bad_NoMatch, noMatch.referencesResults()[0]);
    assertFalse(noMatch.changesApplied());

    // Add+Match with the same pattern falls through to the Add arm (the mapper drops the
    // layout, so the add is legal): a SECOND group is added instead of matching the live one
    CloseAndUpdateApplier.Outcome addMatch =
        apply(
            false,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ElementMatch,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Good, addMatch.referencesResults()[0]);
    assertTrue(addMatch.changesApplied());
    PubSubConfig applied = addMatch.appliedConfig();
    assertNotNull(applied);
    assertEquals(2, applied.connection(CONN).orElseThrow().writerGroups().size());
  }

  @Test
  void modifyIntroducingAServerRangeIdFollowsTheReservationExclusivityRule() throws Exception {
    var grant =
        reservations.reserve(SESSION, PubSubIdReservations.PROFILE_UDP_UADP, 1, 1, service.config);
    var foreignGrant =
        reservations.reserve(
            OTHER_SESSION, PubSubIdReservations.PROFILE_UDP_UADP, 1, 1, service.config);

    PubSubConfigurationRefDataType modifyWg =
        ref(
            0,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementModify,
            PubSubConfigurationRefMask.Field.ReferenceWriterGroup);

    // a Modify introducing a WriterGroupId reserved by ANOTHER session is rejected per-ref
    // (cross-session exclusivity, §9.1.3.7.5): accepting it would put the foreign grant's id
    // into the live configuration and silently deaden that session's reservation
    PubSubConfiguration2DataType file = fileOf(liveConfig());
    file.getConnections()[0].getWriterGroups()[0] =
        withWriterGroupId(
            file.getConnections()[0].getWriterGroups()[0], foreignGrant.writerGroupIds()[0]);

    CloseAndUpdateApplier.Outcome foreign = apply(true, file, modifyWg);
    assertCode(StatusCodes.Bad_InvalidArgument, foreign.referencesResults()[0]);
    assertFalse(foreign.changesApplied());
    assertEquals(0, service.applyCount);

    // the same Modify with an id from the calling session's OWN grant applies, and the
    // reservation is reported consumed (it would otherwise dangle while its id is live)
    UShort ownId = grant.writerGroupIds()[0];
    file.getConnections()[0].getWriterGroups()[0] =
        withWriterGroupId(file.getConnections()[0].getWriterGroups()[0], ownId);

    CloseAndUpdateApplier.Outcome own = apply(true, file, modifyWg);
    assertCode(StatusCodes.Good, own.referencesResults()[0]);
    assertTrue(own.changesApplied());
    assertEquals(
        List.of(new UsedReservation(IdKind.WRITER_GROUP, ownId)),
        List.copyOf(own.consumedReservations()));
    PubSubConfig applied = own.appliedConfig();
    assertNotNull(applied);
    assertEquals(
        ownId, applied.connection(CONN).orElseThrow().writerGroups().get(0).getWriterGroupId());

    // a foreign-reserved DataSetWriterId introduced by a writer Modify is rejected the same way
    service.config = liveConfig();
    PubSubConfiguration2DataType writerFile = fileOf(liveConfig());
    WriterGroupDataType wireGroup = writerFile.getConnections()[0].getWriterGroups()[0];
    wireGroup.getDataSetWriters()[0] =
        withDataSetWriterId(wireGroup.getDataSetWriters()[0], foreignGrant.dataSetWriterIds()[0]);

    CloseAndUpdateApplier.Outcome writerOutcome =
        apply(
            true,
            writerFile,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceWriter));
    assertCode(StatusCodes.Bad_InvalidArgument, writerOutcome.referencesResults()[0]);
    assertFalse(writerOutcome.changesApplied());

    // a Modify keeping the element's current id needs no reservation interplay at all
    CloseAndUpdateApplier.Outcome unchanged = apply(true, fileOf(liveConfig()), modifyWg);
    assertCode(StatusCodes.Good, unchanged.referencesResults()[0]);
    assertTrue(unchanged.consumedReservations().isEmpty());
  }

  @Test
  void atomicModeAppliesNothingOnAnyFailureWhilePartialAppliesSurvivors() {
    // ref 0: a valid remove of 'rg'; ref 1: a modify of a nonexistent connection
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001)
                    .readerGroup(ReaderGroupConfig.builder(READER_GROUP).build())
                    .build())
            .connection(connection("ghost", 15002).build())
            .build();
    PubSubConfiguration2DataType file = fileOf(fileConfig);

    PubSubConfigurationRefDataType removeRef =
        ref(
            0,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementRemove,
            PubSubConfigurationRefMask.Field.ReferenceReaderGroup);
    PubSubConfigurationRefDataType badRef =
        ref(
            0,
            1,
            0,
            PubSubConfigurationRefMask.Field.ElementModify,
            PubSubConfigurationRefMask.Field.ReferenceConnection);

    // atomic: every ref evaluated, nothing applied
    CloseAndUpdateApplier.Outcome atomic = apply(true, file, removeRef, badRef);
    assertEquals(2, atomic.referencesResults().length);
    assertCode(StatusCodes.Good, atomic.referencesResults()[0]);
    assertCode(StatusCodes.Bad_NoMatch, atomic.referencesResults()[1]);
    assertFalse(atomic.changesApplied());
    assertEquals(0, service.applyCount);
    assertTrue(service.config.connection(CONN).orElseThrow().readerGroups().size() == 1);

    // partial: the survivor applies, full-length results either way
    CloseAndUpdateApplier.Outcome partial = apply(false, file, removeRef, badRef);
    assertCode(StatusCodes.Good, partial.referencesResults()[0]);
    assertCode(StatusCodes.Bad_NoMatch, partial.referencesResults()[1]);
    assertTrue(partial.changesApplied());
    assertEquals(1, service.applyCount);
    assertTrue(service.config.connection(CONN).orElseThrow().readerGroups().isEmpty());
  }

  @Test
  void abortedAtomicApplyReportsOnlyMatchResolvedConfigurationValues() {
    // ref 0: an Add connection with null name and PublisherId — its assignment is
    // speculative; ref 1: a pure Match of the live connection; ref 2: a modify of a
    // nonexistent connection (Bad_NoMatch) that aborts the atomic apply
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                UdpConnectionConfig.builder("placeholder")
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 15002))
                    .build())
            .connection(connection(CONN, 15001).build())
            .connection(connection("ghost", 15003).build())
            .build();
    PubSubConfiguration2DataType file = fileOf(fileConfig);
    file.getConnections()[0] = withNullNameAndPublisherId(file.getConnections()[0]);
    file.getConnections()[1] = withNullNameAndPublisherId(file.getConnections()[1]);

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            true,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            ref(
                0,
                1,
                0,
                PubSubConfigurationRefMask.Field.ElementMatch,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            ref(
                0,
                2,
                0,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceConnection));

    assertCode(StatusCodes.Good, outcome.referencesResults()[0]);
    assertCode(StatusCodes.Good, outcome.referencesResults()[1]);
    assertCode(StatusCodes.Bad_NoMatch, outcome.referencesResults()[2]);
    assertFalse(outcome.changesApplied());
    assertEquals(0, service.applyCount);
    assertTrue(outcome.consumedReservations().isEmpty());

    // nothing was assigned ([CU §7.2]): the aborted Add's speculative name/PublisherId is
    // NOT reported — only the Match resolution, which names an element that exists anyway
    assertEquals(1, outcome.configurationValues().length);
    assertEquals(CONN, outcome.configurationValues()[0].getName());
    assertEquals(ushort(7), outcome.configurationValues()[0].getIdentifier().value());
  }

  @Test
  void topLevelFieldsFollowTheSection91376Rules() {
    PubSubConfig live =
        liveConfig().toBuilder()
            .property(new QualifiedName(1, "keep"), Variant.ofString("kept"))
            .property(new QualifiedName(1, "replace"), Variant.ofString("old"))
            .property(new QualifiedName(1, "delete"), Variant.ofString("dead"))
            .build();
    service.config = live;

    var endpoint =
        new EndpointDescription(
            "opc.tcp://sks:4840",
            null,
            null,
            MessageSecurityMode.SignAndEncrypt,
            null,
            null,
            null,
            org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte(0));

    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .enabled(false) // the top-level Enable field is ignored
            .connection(
                connection(CONN, 15001).writerGroup(writerGroup("extra-wg", 44).build()).build())
            .build();
    PubSubConfiguration2DataType mapped = fileOf(fileConfig);

    PubSubConfiguration2DataType file =
        new PubSubConfiguration2DataType(
            mapped.getPublishedDataSets(),
            mapped.getConnections(),
            false,
            mapped.getSubscribedDataSets(),
            null,
            new EndpointDescription[] {endpoint}, // non-empty: replaces
            mapped.getSecurityGroups(),
            null,
            UInteger.valueOf(777777), // ConfigurationVersion is ignored on input
            new KeyValuePair[] {
              new KeyValuePair(new QualifiedName(1, "replace"), Variant.ofString("new")),
              new KeyValuePair(new QualifiedName(1, "delete"), Variant.NULL_VALUE),
              new KeyValuePair(new QualifiedName(1, "insert"), Variant.ofString("inserted")),
            });

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            true,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertTrue(outcome.changesApplied());
    PubSubConfig applied = outcome.appliedConfig();
    assertNotNull(applied);

    assertTrue(applied.isEnabled(), "the top-level Enable field is ignored");
    assertEquals(List.of(endpoint), applied.defaultSecurityKeyServices());
    assertEquals(Variant.ofString("kept"), applied.properties().get(new QualifiedName(1, "keep")));
    assertEquals(
        Variant.ofString("new"), applied.properties().get(new QualifiedName(1, "replace")));
    assertEquals(
        Variant.ofString("inserted"), applied.properties().get(new QualifiedName(1, "insert")));
    assertFalse(applied.properties().containsKey(new QualifiedName(1, "delete")));
  }

  @Test
  void securityGroupRefsAreGuardedPerElementAndDanglingRemovalFails() {
    PubSubConfiguration2DataType file = fileOf(liveConfig());

    PubSubConfigurationRefDataType sgRemove =
        ref(
            0,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementRemove,
            PubSubConfigurationRefMask.Field.ReferenceSecurityGroup);
    PubSubConfigurationRefDataType rgRemove =
        ref(
            0,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementRemove,
            PubSubConfigurationRefMask.Field.ReferenceReaderGroup);

    // checkSksAdmin denial: each bit-11 ref fails per-element with the code verbatim; other
    // refs proceed and the method still applies them
    var denied = new StatusCode(StatusCodes.Bad_UserAccessDenied);
    CloseAndUpdateApplier.Outcome deniedOutcome =
        applier(null)
            .apply(
                SESSION,
                () -> denied,
                file,
                false,
                new PubSubConfigurationRefDataType[] {sgRemove, rgRemove});

    assertEquals(denied, deniedOutcome.referencesResults()[0]);
    assertCode(StatusCodes.Good, deniedOutcome.referencesResults()[1]);
    assertTrue(deniedOutcome.changesApplied());

    // allowed: removing a security group still referenced by a secured writer group fails the
    // trial build with Bad_InvalidArgument
    PublishedDataSetConfig ds = dataSet(DATA_SET);
    SecurityGroupConfig sg =
        SecurityGroupConfig.builder(SECURITY_GROUP).keyLifeTime(Duration.ofHours(1)).build();
    service.config =
        PubSubConfig.builder()
            .publishedDataSet(ds)
            .securityGroup(sg)
            .connection(
                connection(CONN, 15001)
                    .writerGroup(
                        writerGroup(WRITER_GROUP, 10)
                            .messageSecurity(
                                MessageSecurityConfig.builder()
                                    .mode(MessageSecurityMode.SignAndEncrypt)
                                    .securityGroup(sg.ref())
                                    .build())
                            .build())
                    .build())
            .build();

    CloseAndUpdateApplier.Outcome dangling = apply(false, fileOf(service.config), sgRemove);
    assertCode(StatusCodes.Bad_InvalidArgument, dangling.referencesResults()[0]);
  }

  @Test
  void engineValidationFailureIsAttributedToAllWouldBeAppliedRefs() {
    // the engine rejects the surviving configuration: every ref still marked Good inherits the
    // extracted code; the already-failed ref keeps its own
    service.throwOnApply = new UaRuntimeException(StatusCodes.Bad_NotSupported, "rejected");

    PubSubConfiguration2DataType file = fileOf(liveConfig());

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            false,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementRemove,
                PubSubConfigurationRefMask.Field.ReferenceReaderGroup),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferencePushTarget));

    assertCode(StatusCodes.Bad_NotSupported, outcome.referencesResults()[0]);
    assertCode(StatusCodes.Bad_InvalidArgument, outcome.referencesResults()[1]);
    assertFalse(outcome.changesApplied());
    assertTrue(outcome.consumedReservations().isEmpty());
  }

  @Test
  void configurationObjectsAreLengthMatchedOrEmpty() {
    var objectIds =
        new ConfigurationObjectIds() {
          @Override
          public @Nullable NodeId connectionObjectId(String connectionName) {
            return new NodeId(2, "PubSub/" + connectionName);
          }

          @Override
          public @Nullable NodeId writerGroupObjectId(String connectionName, String groupName) {
            return new NodeId(2, "PubSub/" + connectionName + "/" + groupName);
          }

          @Override
          public @Nullable NodeId dataSetWriterObjectId(String c, String g, String w) {
            return new NodeId(2, "PubSub/" + c + "/" + g + "/" + w);
          }

          @Override
          public @Nullable NodeId readerGroupObjectId(String connectionName, String groupName) {
            return new NodeId(2, "PubSub/" + connectionName + "/" + groupName);
          }

          @Override
          public @Nullable NodeId dataSetReaderObjectId(String c, String g, String r) {
            return new NodeId(2, "PubSub/" + c + "/" + g + "/" + r);
          }

          @Override
          public @Nullable NodeId publishedDataSetObjectId(String name) {
            return new NodeId(2, "PubSub/PublishedDataSets/" + name);
          }
        };

    PubSubConfiguration2DataType file = fileOf(liveConfig());

    PubSubConfigurationRefDataType modifyWg =
        ref(
            0,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementModify,
            PubSubConfigurationRefMask.Field.ReferenceWriterGroup);
    PubSubConfigurationRefDataType removeRg =
        ref(
            0,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementRemove,
            PubSubConfigurationRefMask.Field.ReferenceReaderGroup);
    PubSubConfigurationRefDataType removeSg =
        ref(
            0,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementRemove,
            PubSubConfigurationRefMask.Field.ReferenceSecurityGroup);
    PubSubConfigurationRefDataType badRef =
        ref(
            9,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementModify,
            PubSubConfigurationRefMask.Field.ReferenceWriter);

    CloseAndUpdateApplier.Outcome outcome =
        applier(objectIds)
            .apply(
                SESSION,
                SKS_ALLOWED,
                file,
                false,
                new PubSubConfigurationRefDataType[] {modifyWg, removeRg, removeSg, badRef});

    // length-matched: the modified writer group resolves; Remove slots, never-materialized
    // kinds, and failed refs are NULL_VALUE
    NodeId[] objects = outcome.configurationObjects();
    assertEquals(4, objects.length);
    assertEquals(new NodeId(2, "PubSub/" + CONN + "/" + WRITER_GROUP), objects[0]);
    assertEquals(NodeId.NULL_VALUE, objects[1]);
    assertEquals(NodeId.NULL_VALUE, objects[2]);
    assertEquals(NodeId.NULL_VALUE, objects[3]);

    // the seam absent: EMPTY, the spec opt-out arm
    service.config = liveConfig();
    CloseAndUpdateApplier.Outcome withoutSeam = apply(false, fileOf(liveConfig()), modifyWg);
    assertEquals(0, withoutSeam.configurationObjects().length);

    // ConfigurationValues self-correlate via the input ref instance and are absent for refs
    // where nothing was assigned or resolved
    assertEquals(0, withoutSeam.configurationValues().length);
  }

  // region wire patching helpers

  private static org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType
      withNullNameAndPublisherId(
          org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType value) {
    return new org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType(
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

  private static org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType
      withConnectionName(
          org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType value,
          String name) {
    return new org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType(
        name,
        value.getEnabled(),
        value.getPublisherId(),
        value.getTransportProfileUri(),
        value.getAddress(),
        value.getConnectionProperties(),
        value.getTransportSettings(),
        value.getWriterGroups(),
        value.getReaderGroups());
  }

  private static org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType
      withTransportProfileUri(
          org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType value,
          String transportProfileUri) {
    return new org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType(
        value.getName(),
        value.getEnabled(),
        value.getPublisherId(),
        transportProfileUri,
        value.getAddress(),
        value.getConnectionProperties(),
        value.getTransportSettings(),
        value.getWriterGroups(),
        value.getReaderGroups());
  }

  private static WriterGroupDataType withWriterGroupId(WriterGroupDataType value, UShort id) {
    return new WriterGroupDataType(
        value.getName(),
        value.getEnabled(),
        value.getSecurityMode(),
        value.getSecurityGroupId(),
        value.getSecurityKeyServices(),
        value.getMaxNetworkMessageSize(),
        value.getGroupProperties(),
        id,
        value.getPublishingInterval(),
        value.getKeepAliveTime(),
        value.getPriority(),
        value.getLocaleIds(),
        value.getHeaderLayoutUri(),
        value.getTransportSettings(),
        value.getMessageSettings(),
        value.getDataSetWriters());
  }

  private static WriterGroupDataType withHeaderLayoutUri(
      WriterGroupDataType value, String headerLayoutUri) {
    return new WriterGroupDataType(
        value.getName(),
        value.getEnabled(),
        value.getSecurityMode(),
        value.getSecurityGroupId(),
        value.getSecurityKeyServices(),
        value.getMaxNetworkMessageSize(),
        value.getGroupProperties(),
        value.getWriterGroupId(),
        value.getPublishingInterval(),
        value.getKeepAliveTime(),
        value.getPriority(),
        value.getLocaleIds(),
        headerLayoutUri,
        value.getTransportSettings(),
        value.getMessageSettings(),
        value.getDataSetWriters());
  }

  private static org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType
      withDataSetWriterId(
          org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType value,
          UShort id) {
    return new org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType(
        value.getName(),
        value.getEnabled(),
        id,
        value.getDataSetFieldContentMask(),
        value.getKeyFrameCount(),
        value.getDataSetName(),
        value.getDataSetWriterProperties(),
        value.getTransportSettings(),
        value.getMessageSettings());
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

  // endregion

  /** Captures {@link #update} transforms; every other method is unsupported. */
  private static final class FakePubSubService implements PubSubService {

    private PubSubConfig config;
    private int applyCount = 0;
    private @Nullable RuntimeException throwOnApply;

    private FakePubSubService(PubSubConfig config) {
      this.config = config;
    }

    @Override
    public ReconfigureResult update(UnaryOperator<PubSubConfig> transform) {
      PubSubConfig next = transform.apply(config);
      // the engine validates the transformed config before applying anything
      if (throwOnApply != null) {
        throw throwOnApply;
      }
      config = next;
      applyCount++;
      return new ReconfigureResult(List.of());
    }

    @Override
    public ReconfigureResult reconfigure(PubSubConfig newConfig, ReconfigureMode mode) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<PubSubService> startup() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Void> shutdown() {
      throw new UnsupportedOperationException();
    }

    @Override
    public PubSubComponents components() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void enable(PubSubHandle handle) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void disable(PubSubHandle handle) {
      throw new UnsupportedOperationException();
    }

    @Override
    public PubSubState state(PubSubHandle handle) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void bindSource(PublishedDataSetRef dataSet, PublishedDataSetSource source) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void publishEvent(PublishedDataSetRef dataSet, List<Variant> fields) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addDataSetListener(DataSetListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeDataSetListener(DataSetListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addDataSetListener(DataSetReaderRef reader, DataSetListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeDataSetListener(DataSetReaderRef reader, DataSetListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addStateListener(PubSubStateListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeStateListener(PubSubStateListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addMetaDataListener(MetaDataListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeMetaDataListener(MetaDataListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addDiagnosticsListener(PubSubDiagnosticsListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeDiagnosticsListener(PubSubDiagnosticsListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public PubSubDiagnostics diagnostics() {
      throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable SecurityKeyInfo securityKeyInfo(PubSubHandle group) {
      throw new UnsupportedOperationException();
    }

    @Override
    public UInteger nextDataSetMessageSequenceNumber(PubSubHandle writer) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
      throw new UnsupportedOperationException();
    }
  }
}
