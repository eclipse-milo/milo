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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.eclipse.milo.opcua.sdk.pubsub.PublisherStatusListener;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.SecurityKeyInfo;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldSelector;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariableConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariablesConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * CloseAndUpdate element-kind coverage that {@code CloseAndUpdateApplierTest} and the client-driven
 * integration class do not reach: the applier's dedicated DataSetReader arms (add/modify/remove,
 * remote ids never auto-assigned), ReaderGroup Match with the fixed SecurityMode/SecurityGroupId
 * field set, PublishedDataSet and standalone SubscribedDataSet add/modify/remove — including the
 * remove-of-a-referenced-PDS per-ref validation failure and the Identifier-NULL rule for PDS/SDS
 * adds — plus the top-level residuals (populated DataSetClasses ignored, null/empty
 * DefaultSecurityKeyServices keeps the live value) and the empty-string name auto-assignment leg.
 *
 * <p>Unit-driven against live {@link PubSubConfig} values and a transform-capturing fake service,
 * mirroring {@code CloseAndUpdateApplierTest}'s conventions.
 */
class CloseAndUpdateElementKindsTest {

  private static final NodeId SESSION = new NodeId(1, "session");

  private static final Supplier<StatusCode> SKS_ALLOWED = () -> StatusCode.GOOD;

  private static final NamespaceTable NAMESPACE_TABLE = new NamespaceTable();

  private static final String CONN = "conn";
  private static final String WRITER_GROUP = "wg";
  private static final String WRITER = "dsw";
  private static final String READER_GROUP = "rg";
  private static final String READER = "dsr";
  private static final String DATA_SET = "ds";
  private static final String STANDALONE = "sds";
  private static final String SECURITY_GROUP = "sg1";

  private final FakePubSubService service = new FakePubSubService(liveConfig());
  private final PubSubIdReservations reservations = new PubSubIdReservations(4840);

  private CloseAndUpdateApplier.Outcome apply(
      boolean requireCompleteUpdate,
      PubSubConfiguration2DataType file,
      PubSubConfigurationRefDataType... references) {

    var applier = new CloseAndUpdateApplier(service, NAMESPACE_TABLE, reservations, null);
    return applier.apply(SESSION, SKS_ALLOWED, file, requireCompleteUpdate, references);
  }

  // region fixtures

  private static PublishedDataSetConfig dataSet(String name, String fieldName) {
    return PublishedDataSetConfig.builder(name)
        .field(
            FieldDefinition.builder(fieldName)
                .source(NodeFieldAddress.of(NodeIds.Server, AttributeId.Value, NAMESPACE_TABLE))
                .dataType(NodeIds.Double)
                .dataSetFieldId(new UUID(0L, 0xE1L))
                .build())
        .build();
  }

  private static DataSetReaderConfig reader(String name, Duration messageReceiveTimeout) {
    return DataSetReaderConfig.builder(name)
        .publisherId(PublisherId.uint16(ushort(9)))
        .writerGroupId(ushort(5))
        .dataSetWriterId(ushort(7))
        .dataSetMetaData(
            DataSetMetaDataConfig.builder("md")
                .field("f1", NodeIds.Int32, new UUID(0L, 0xE2L))
                .build())
        .messageReceiveTimeout(messageReceiveTimeout)
        .build();
  }

  private static StandaloneSubscribedDataSetConfig standalone(String name, String metaDataName) {
    return StandaloneSubscribedDataSetConfig.builder(name)
        .metaData(
            DataSetMetaDataConfig.builder(metaDataName)
                .field("g1", NodeIds.Double, new UUID(0L, 0xE3L))
                .build())
        .subscribedDataSet(
            TargetVariablesConfig.builder()
                .map(
                    FieldSelector.byName("g1"),
                    TargetVariableConfig.builder()
                        .target(
                            NodeFieldAddress.of(NodeIds.Server, AttributeId.Value, NAMESPACE_TABLE))
                        .build())
                .build())
        .build();
  }

  private static UdpConnectionConfig.Builder connection(String name, int port) {
    return PubSubConnectionConfig.udp(name)
        .publisherId(PublisherId.uint16(ushort(7)))
        .address(UdpDatagramAddress.unicast("127.0.0.1", port));
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

  /**
   * The live configuration: {@code conn} (UDP) with {@code wg} (id 10, writer {@code dsw} id 20
   * referencing {@code ds}) and reader group {@code rg} (reader {@code dsr}); standalone dataset
   * {@code sds}; security group {@code sg1}.
   */
  private static PubSubConfig liveConfig() {
    PublishedDataSetConfig ds = dataSet(DATA_SET, "value");

    return PubSubConfig.builder()
        .publishedDataSet(ds)
        .standaloneSubscribedDataSet(standalone(STANDALONE, "md1"))
        .securityGroup(
            SecurityGroupConfig.builder(SECURITY_GROUP).keyLifeTime(Duration.ofHours(1)).build())
        .connection(
            connection(CONN, 15001)
                .writerGroup(
                    writerGroup(WRITER_GROUP, 10)
                        .dataSetWriter(writer(WRITER, 20, ds.ref()))
                        .build())
                .readerGroup(
                    ReaderGroupConfig.builder(READER_GROUP)
                        .dataSetReader(reader(READER, Duration.ofSeconds(1)))
                        .build())
                .build())
        .build();
  }

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

  /**
   * ReaderGroup Match compares the fixed field set including the security triple — an identical
   * secured pattern matches (reporting the resolved name with a NULL identifier), while a pattern
   * differing only in SecurityMode/SecurityGroupId answers {@code Bad_NoMatch}.
   */
  @Test
  void readerGroupMatchComparesTheSecurityFields() {
    SecurityGroupConfig sg =
        SecurityGroupConfig.builder(SECURITY_GROUP).keyLifeTime(Duration.ofHours(1)).build();
    MessageSecurityConfig security =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.Sign)
            .securityGroup(sg.ref())
            .build();

    service.config =
        PubSubConfig.builder()
            .securityGroup(sg)
            .connection(
                connection(CONN, 15001)
                    .readerGroup(
                        ReaderGroupConfig.builder(READER_GROUP).messageSecurity(security).build())
                    .build())
            .build();

    PubSubConfigurationRefDataType matchRef =
        ref(
            0,
            0,
            0,
            PubSubConfigurationRefMask.Field.ElementMatch,
            PubSubConfigurationRefMask.Field.ReferenceReaderGroup);

    // the secured pattern (name nulled per the Match rule) matches the live secured group
    PubSubConfiguration2DataType file = fileOf(service.config);
    file.getConnections()[0].getReaderGroups()[0] =
        withNullReaderGroupName(file.getConnections()[0].getReaderGroups()[0]);

    CloseAndUpdateApplier.Outcome matched = apply(false, file, matchRef);
    assertCode(StatusCodes.Good, matched.referencesResults()[0]);
    assertFalse(matched.changesApplied());
    // the resolved name is reported; reader groups have no wire id, so the identifier is NULL
    assertEquals(1, matched.configurationValues().length);
    assertEquals(READER_GROUP, matched.configurationValues()[0].getName());
    assertEquals(Variant.NULL_VALUE, matched.configurationValues()[0].getIdentifier());

    // a pattern identical but for the security triple (mode None, no SecurityGroupId): no match
    PubSubConfig unsecured =
        PubSubConfig.builder()
            .securityGroup(sg)
            .connection(
                connection(CONN, 15001)
                    .readerGroup(ReaderGroupConfig.builder(READER_GROUP).build())
                    .build())
            .build();
    PubSubConfiguration2DataType unsecuredFile = fileOf(unsecured);
    unsecuredFile.getConnections()[0].getReaderGroups()[0] =
        withNullReaderGroupName(unsecuredFile.getConnections()[0].getReaderGroups()[0]);

    CloseAndUpdateApplier.Outcome noMatch = apply(false, unsecuredFile, matchRef);
    assertCode(StatusCodes.Bad_NoMatch, noMatch.referencesResults()[0]);
  }

  /**
   * A DataSetReader Add auto-assigns ONLY the name (reported with a NULL identifier — "only …
   * PubSubConnection, WriterGroup or DataSetWriter" carry identifiers); the reader's
   * WriterGroupId/DataSetWriterId identify the REMOTE publisher's components and are never
   * auto-assigned — a wire 0 stays unset instead of drawing from 0x8000–0xFFFF.
   */
  @Test
  void dataSetReaderAddAutoAssignsOnlyTheName() {
    // the live rg is empty; the file carries the reader payload under the same group name
    service.config =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001)
                    .readerGroup(ReaderGroupConfig.builder(READER_GROUP).build())
                    .build())
            .build();

    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001)
                    .readerGroup(
                        ReaderGroupConfig.builder(READER_GROUP)
                            .dataSetReader(reader("placeholder", Duration.ofSeconds(1)))
                            .build())
                    .build())
            .build();
    PubSubConfiguration2DataType file = fileOf(fileConfig);
    DataSetReaderDataType wireReader =
        file.getConnections()[0].getReaderGroups()[0].getDataSetReaders()[0];
    // null name (auto-assign) and DataSetWriterId 0 (wire null — must NOT be auto-assigned)
    file.getConnections()[0].getReaderGroups()[0].getDataSetReaders()[0] =
        withReaderNameAndDataSetWriterId(wireReader, null, ushort(0));

    CloseAndUpdateApplier.Outcome outcome =
        apply(
            true,
            file,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceReader));

    assertCode(StatusCodes.Good, outcome.referencesResults()[0]);
    assertTrue(outcome.changesApplied());

    PubSubConfig applied = outcome.appliedConfig();
    assertNotNull(applied);
    DataSetReaderConfig added =
        applied.connection(CONN).orElseThrow().readerGroups().get(0).getDataSetReaders().get(0);
    assertEquals("DataSetReader1", added.getName());
    assertEquals(ushort(5), added.getWriterGroupId(), "remote ids pass through unchanged");
    assertNull(added.getDataSetWriterId(), "a reader's wire 0 reads as null, never auto-assigned");
    assertTrue(outcome.consumedReservations().isEmpty());

    // the assignment entry carries the assigned name and a NULL identifier
    assertEquals(1, outcome.configurationValues().length);
    assertEquals("DataSetReader1", outcome.configurationValues()[0].getName());
    assertEquals(Variant.NULL_VALUE, outcome.configurationValues()[0].getIdentifier());
  }

  /** DataSetReader Modify is a by-name full replacement; Remove drops the reader. */
  @Test
  void dataSetReaderModifyAndRemoveBindByName() {
    // Modify: same reader name, a different messageReceiveTimeout
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001)
                    .readerGroup(
                        ReaderGroupConfig.builder(READER_GROUP)
                            .dataSetReader(reader(READER, Duration.ofSeconds(5)))
                            .build())
                    .build())
            .build();

    CloseAndUpdateApplier.Outcome modified =
        apply(
            true,
            fileOf(fileConfig),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceReader));

    assertCode(StatusCodes.Good, modified.referencesResults()[0]);
    PubSubConfig applied = modified.appliedConfig();
    assertNotNull(applied);
    assertEquals(
        Duration.ofSeconds(5),
        applied
            .connection(CONN)
            .orElseThrow()
            .readerGroups()
            .get(0)
            .getDataSetReaders()
            .get(0)
            .getMessageReceiveTimeout());

    // Remove: binds the same name against the (now modified) live state
    CloseAndUpdateApplier.Outcome removed =
        apply(
            true,
            fileOf(fileConfig),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementRemove,
                PubSubConfigurationRefMask.Field.ReferenceReader));

    assertCode(StatusCodes.Good, removed.referencesResults()[0]);
    PubSubConfig afterRemove = removed.appliedConfig();
    assertNotNull(afterRemove);
    assertTrue(
        afterRemove
            .connection(CONN)
            .orElseThrow()
            .readerGroups()
            .get(0)
            .getDataSetReaders()
            .isEmpty());
  }

  /**
   * PublishedDataSet Add (explicit and auto-assigned names, identifier always NULL), Modify as a
   * by-name full replacement, and Remove of an UNREFERENCED dataset.
   */
  @Test
  void publishedDataSetAddModifyAndUnreferencedRemove() {
    // Modify: same name, a different field definition
    PubSubConfig modifyFile =
        PubSubConfig.builder().publishedDataSet(dataSet(DATA_SET, "value2")).build();

    CloseAndUpdateApplier.Outcome modified =
        apply(
            true,
            fileOf(modifyFile),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferencePubDataset));

    assertCode(StatusCodes.Good, modified.referencesResults()[0]);
    PubSubConfig applied = modified.appliedConfig();
    assertNotNull(applied);
    PublishedDataSetConfig modifiedDs =
        applied.publishedDataSets().stream()
            .filter(ds -> ds.getName().equals(DATA_SET))
            .findFirst()
            .orElseThrow();
    assertEquals("value2", modifiedDs.getFields().get(0).getName());

    // Add with an explicit name: applied, and no ConfigurationValues entry (nothing assigned)
    PubSubConfig addFile = PubSubConfig.builder().publishedDataSet(dataSet("ds2", "value")).build();

    CloseAndUpdateApplier.Outcome added =
        apply(
            true,
            fileOf(addFile),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferencePubDataset));

    assertCode(StatusCodes.Good, added.referencesResults()[0]);
    assertNotNull(added.appliedConfig());
    assertTrue(
        added.appliedConfig().publishedDataSets().stream()
            .anyMatch(ds -> ds.getName().equals("ds2")));
    assertEquals(0, added.configurationValues().length);

    // a null-name Add auto-assigns and reports the name with a NULL identifier
    PubSubConfiguration2DataType nullNameFile = fileOf(addFile);
    nullNameFile.getPublishedDataSets()[0] =
        withPublishedDataSetName(nullNameFile.getPublishedDataSets()[0], null);

    CloseAndUpdateApplier.Outcome autoNamed =
        apply(
            true,
            nullNameFile,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferencePubDataset));

    assertCode(StatusCodes.Good, autoNamed.referencesResults()[0]);
    assertEquals(1, autoNamed.configurationValues().length);
    assertEquals("PublishedDataSet1", autoNamed.configurationValues()[0].getName());
    assertEquals(Variant.NULL_VALUE, autoNamed.configurationValues()[0].getIdentifier());

    // Remove of the UNREFERENCED ds2: applied
    CloseAndUpdateApplier.Outcome removed =
        apply(
            true,
            fileOf(addFile),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementRemove,
                PubSubConfigurationRefMask.Field.ReferencePubDataset));

    assertCode(StatusCodes.Good, removed.referencesResults()[0]);
    assertNotNull(removed.appliedConfig());
    assertTrue(
        removed.appliedConfig().publishedDataSets().stream()
            .noneMatch(ds -> ds.getName().equals("ds2")));
  }

  /**
   * Removing a PublishedDataSet still referenced by a live writer fails per-ref with the
   * config-validation code ({@code Bad_InvalidArgument} — a dangling reference, not a name
   * duplication) and nothing is applied.
   */
  @Test
  void removeOfAReferencedPublishedDataSetFailsPerRef() {
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
                PubSubConfigurationRefMask.Field.ReferencePubDataset));

    assertCode(StatusCodes.Bad_InvalidArgument, outcome.referencesResults()[0]);
    assertFalse(outcome.changesApplied());
    assertEquals(0, service.applyCount);
    // the live configuration keeps the referenced dataset
    assertTrue(
        service.config.publishedDataSets().stream().anyMatch(ds -> ds.getName().equals(DATA_SET)));
  }

  /**
   * Standalone SubscribedDataSet Add (explicit and auto-assigned names, identifier always NULL),
   * Modify as a by-name full replacement, and Remove.
   */
  @Test
  void standaloneSubscribedDataSetAddModifyAndRemove() {
    // Modify: same name, replacement metadata
    PubSubConfig modifyFile =
        PubSubConfig.builder().standaloneSubscribedDataSet(standalone(STANDALONE, "md2")).build();

    CloseAndUpdateApplier.Outcome modified =
        apply(
            true,
            fileOf(modifyFile),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementModify,
                PubSubConfigurationRefMask.Field.ReferenceSubDataset));

    assertCode(StatusCodes.Good, modified.referencesResults()[0]);
    PubSubConfig applied = modified.appliedConfig();
    assertNotNull(applied);
    assertEquals("md2", applied.standaloneSubscribedDataSets().get(0).getMetaData().getName());

    // Add with an explicit name
    PubSubConfig addFile =
        PubSubConfig.builder().standaloneSubscribedDataSet(standalone("sds2", "md3")).build();

    CloseAndUpdateApplier.Outcome added =
        apply(
            true,
            fileOf(addFile),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceSubDataset));

    assertCode(StatusCodes.Good, added.referencesResults()[0]);
    assertNotNull(added.appliedConfig());
    assertTrue(
        added.appliedConfig().standaloneSubscribedDataSets().stream()
            .anyMatch(sds -> sds.getName().equals("sds2")));
    assertEquals(0, added.configurationValues().length);

    // a null-name Add auto-assigns and reports a NULL identifier
    PubSubConfiguration2DataType nullNameFile = fileOf(addFile);
    nullNameFile.getSubscribedDataSets()[0] =
        withSubscribedDataSetName(nullNameFile.getSubscribedDataSets()[0], null);

    CloseAndUpdateApplier.Outcome autoNamed =
        apply(
            true,
            nullNameFile,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceSubDataset));

    assertCode(StatusCodes.Good, autoNamed.referencesResults()[0]);
    assertEquals(1, autoNamed.configurationValues().length);
    assertEquals("SubscribedDataSet1", autoNamed.configurationValues()[0].getName());
    assertEquals(Variant.NULL_VALUE, autoNamed.configurationValues()[0].getIdentifier());

    // Remove
    CloseAndUpdateApplier.Outcome removed =
        apply(
            true,
            fileOf(addFile),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementRemove,
                PubSubConfigurationRefMask.Field.ReferenceSubDataset));

    assertCode(StatusCodes.Good, removed.referencesResults()[0]);
    assertNotNull(removed.appliedConfig());
    assertTrue(
        removed.appliedConfig().standaloneSubscribedDataSets().stream()
            .noneMatch(sds -> sds.getName().equals("sds2")));
  }

  /**
   * A populated top-level DataSetClasses array is ignored, and a null (then empty)
   * DefaultSecurityKeyServices leaves the live value untouched — only the non-empty replace
   * direction writes it.
   */
  @Test
  void populatedDataSetClassesAreIgnoredAndNullOrEmptyKeyServicesKeepTheLiveValue() {
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
    service.config = liveConfig().toBuilder().defaultSecurityKeyService(endpoint).build();

    var dataSetClass =
        new DataSetMetaDataType(
            null,
            null,
            null,
            null,
            "klass",
            LocalizedText.NULL_VALUE,
            new FieldMetaData[0],
            new UUID(0L, 0xC1L),
            new ConfigurationVersionDataType(uint(1), uint(1)));

    // one mutating ref so the top-level fields are actually applied
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001).writerGroup(writerGroup("extra-wg", 44).build()).build())
            .build();

    // populated DataSetClasses, null DefaultSecurityKeyServices
    CloseAndUpdateApplier.Outcome outcome =
        apply(
            true,
            withTopLevel(fileOf(fileConfig), new DataSetMetaDataType[] {dataSetClass}, null),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Good, outcome.referencesResults()[0]);
    PubSubConfig applied = outcome.appliedConfig();
    assertNotNull(applied);
    assertEquals(
        List.of(endpoint),
        applied.defaultSecurityKeyServices(),
        "a null DefaultSecurityKeyServices must keep the live value");
    DataSetMetaDataType[] mappedClasses = applied.toDataType(NAMESPACE_TABLE).getDataSetClasses();
    assertTrue(
        mappedClasses == null || mappedClasses.length == 0,
        "a populated DataSetClasses array is ignored");

    // an empty DefaultSecurityKeyServices array keeps the live value too
    PubSubConfig secondFileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001).writerGroup(writerGroup("extra-wg2", 45).build()).build())
            .build();

    CloseAndUpdateApplier.Outcome second =
        apply(
            true,
            withTopLevel(fileOf(secondFileConfig), null, new EndpointDescription[0]),
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Good, second.referencesResults()[0]);
    assertNotNull(second.appliedConfig());
    assertEquals(List.of(endpoint), second.appliedConfig().defaultSecurityKeyServices());
  }

  /** An empty-string name — like null — triggers auto-assignment (nullOrEmpty's other leg). */
  @Test
  void emptyStringNameTriggersAutoAssignment() {
    // a connection named "" at file index 1
    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(connection(CONN, 15001).build())
            .connection(
                UdpConnectionConfig.builder("placeholder")
                    .publisherId(PublisherId.uint16(ushort(8)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 15002))
                    .build())
            .build();
    PubSubConfiguration2DataType file = fileOf(fileConfig);
    file.getConnections()[1] = withConnectionName(file.getConnections()[1], "");

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
    PubSubConfig applied = outcome.appliedConfig();
    assertNotNull(applied);
    assertTrue(applied.connection("Connection1").isPresent());
    assertEquals(1, outcome.configurationValues().length);
    assertEquals("Connection1", outcome.configurationValues()[0].getName());

    // and the same leg for a group kind: a writer group named "" gets "WriterGroup1"
    PubSubConfig groupFileConfig =
        PubSubConfig.builder()
            .connection(
                connection(CONN, 15001).writerGroup(writerGroup("gone", 0x7005).build()).build())
            .build();
    PubSubConfiguration2DataType groupFile = fileOf(groupFileConfig);
    groupFile.getConnections()[0].getWriterGroups()[0] =
        withWriterGroupName(groupFile.getConnections()[0].getWriterGroups()[0], "");

    CloseAndUpdateApplier.Outcome groupOutcome =
        apply(
            true,
            groupFile,
            ref(
                0,
                0,
                0,
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup));

    assertCode(StatusCodes.Good, groupOutcome.referencesResults()[0]);
    assertNotNull(groupOutcome.appliedConfig());
    assertTrue(
        groupOutcome.appliedConfig().connection(CONN).orElseThrow().writerGroups().stream()
            .anyMatch(g -> g.getName().equals("WriterGroup1")));
  }

  // region wire patching helpers

  private static ReaderGroupDataType withNullReaderGroupName(ReaderGroupDataType value) {
    return new ReaderGroupDataType(
        null,
        value.getEnabled(),
        value.getSecurityMode(),
        value.getSecurityGroupId(),
        value.getSecurityKeyServices(),
        value.getMaxNetworkMessageSize(),
        value.getGroupProperties(),
        value.getTransportSettings(),
        value.getMessageSettings(),
        value.getDataSetReaders());
  }

  private static DataSetReaderDataType withReaderNameAndDataSetWriterId(
      DataSetReaderDataType value,
      @Nullable String name,
      org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort dataSetWriterId) {
    return new DataSetReaderDataType(
        name,
        value.getEnabled(),
        value.getPublisherId(),
        value.getWriterGroupId(),
        dataSetWriterId,
        value.getDataSetMetaData(),
        value.getDataSetFieldContentMask(),
        value.getMessageReceiveTimeout(),
        value.getKeyFrameCount(),
        value.getHeaderLayoutUri(),
        value.getSecurityMode(),
        value.getSecurityGroupId(),
        value.getSecurityKeyServices(),
        value.getDataSetReaderProperties(),
        value.getTransportSettings(),
        value.getMessageSettings(),
        value.getSubscribedDataSet());
  }

  private static PublishedDataSetDataType withPublishedDataSetName(
      PublishedDataSetDataType value, @Nullable String name) {
    return new PublishedDataSetDataType(
        name,
        value.getDataSetFolder(),
        value.getDataSetMetaData(),
        value.getExtensionFields(),
        value.getDataSetSource());
  }

  private static StandaloneSubscribedDataSetDataType withSubscribedDataSetName(
      StandaloneSubscribedDataSetDataType value, @Nullable String name) {
    return new StandaloneSubscribedDataSetDataType(
        name, value.getDataSetFolder(), value.getDataSetMetaData(), value.getSubscribedDataSet());
  }

  private static PubSubConnectionDataType withConnectionName(
      PubSubConnectionDataType value, String name) {
    return new PubSubConnectionDataType(
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

  private static org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType
      withWriterGroupName(
          org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType value,
          String name) {
    return new org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType(
        name,
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
        value.getHeaderLayoutUri(),
        value.getTransportSettings(),
        value.getMessageSettings(),
        value.getDataSetWriters());
  }

  /** Rebuild the file with the given top-level DataSetClasses and DefaultSecurityKeyServices. */
  private static PubSubConfiguration2DataType withTopLevel(
      PubSubConfiguration2DataType value,
      DataSetMetaDataType @Nullable [] dataSetClasses,
      EndpointDescription @Nullable [] defaultSecurityKeyServices) {
    return new PubSubConfiguration2DataType(
        value.getPublishedDataSets(),
        value.getConnections(),
        value.getEnabled(),
        value.getSubscribedDataSets(),
        dataSetClasses,
        defaultSecurityKeyServices,
        value.getSecurityGroups(),
        value.getPubSubKeyPushTargets(),
        UInteger.valueOf(0),
        value.getConfigurationProperties());
  }

  // endregion

  /** Captures {@link #update} transforms; every other method is unsupported. */
  private static final class FakePubSubService implements PubSubService {

    private PubSubConfig config;
    private int applyCount = 0;

    private FakePubSubService(PubSubConfig config) {
      this.config = config;
    }

    @Override
    public ReconfigureResult update(UnaryOperator<PubSubConfig> transform) {
      PubSubConfig next = transform.apply(config);
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
    public void addPublisherStatusListener(PublisherStatusListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removePublisherStatusListener(PublisherStatusListener listener) {
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
