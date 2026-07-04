/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService.ReconfigureMode;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariablesConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ConfigDiff} (minimal, non-overlapping path diff: leaf change restarts the
 * leaf, shell change restarts the component, PublishedDataSet/standalone SubscribedDataSet changes
 * restart referencing writers/readers) and for {@code PubSubService.reconfigure} applying the diff:
 * {@link ReconfigureResult} paths, handle invalidation, and mode semantics.
 */
class ConfigDiffTest {

  private static final UUID FIELD_ID = new UUID(0L, 1L);
  private static final UUID META_FIELD_ID = new UUID(0L, 2L);

  // region config fixtures (all wire identities and field ids pinned for stable equality)

  private static PublishedDataSetConfig dataSet(String name, int minorVersion) {
    return PublishedDataSetConfig.builder(name)
        .field(
            FieldDefinition.builder("F1").dataType(NodeIds.Int32).dataSetFieldId(FIELD_ID).build())
        .configurationVersion(uint(1), uint(minorVersion))
        .build();
  }

  private static DataSetWriterConfig writer(String name, int id, int keyFrameCount) {
    return DataSetWriterConfig.builder(name)
        .dataSet(new PublishedDataSetRef("PDS"))
        .dataSetWriterId(ushort(id))
        .keyFrameCount(uint(keyFrameCount))
        .build();
  }

  private static WriterGroupConfig writerGroup(
      String name, int id, int publishingIntervalMillis, DataSetWriterConfig... writers) {

    WriterGroupConfig.Builder builder =
        WriterGroupConfig.builder(name)
            .writerGroupId(ushort(id))
            .publishingInterval(Duration.ofMillis(publishingIntervalMillis));
    for (DataSetWriterConfig writer : writers) {
      builder.dataSetWriter(writer);
    }
    return builder.build();
  }

  private static DataSetMetaDataConfig metaData(String name, int major) {
    return DataSetMetaDataConfig.builder(name)
        .field("F1", NodeIds.Int32, META_FIELD_ID)
        .configurationVersion(uint(major), uint(major))
        .build();
  }

  private static DataSetReaderConfig reader(String name, Duration messageReceiveTimeout) {
    return DataSetReaderConfig.builder(name).messageReceiveTimeout(messageReceiveTimeout).build();
  }

  private static ReaderGroupConfig readerGroup(String name, DataSetReaderConfig... readers) {
    ReaderGroupConfig.Builder builder = ReaderGroupConfig.builder(name);
    for (DataSetReaderConfig reader : readers) {
      builder.dataSetReader(reader);
    }
    return builder.build();
  }

  private static UdpConnectionConfig connection(
      String name,
      int publisherId,
      List<WriterGroupConfig> writerGroups,
      List<ReaderGroupConfig> readerGroups) {

    UdpConnectionConfig.Builder builder =
        UdpConnectionConfig.builder(name)
            .publisherId(PublisherId.uint16(ushort(publisherId)))
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840));
    writerGroups.forEach(builder::writerGroup);
    readerGroups.forEach(builder::readerGroup);
    return builder.build();
  }

  private static StandaloneSubscribedDataSetConfig standalone(String name, int major) {
    return StandaloneSubscribedDataSetConfig.builder(name)
        .metaData(metaData(name + "-MD", major))
        .subscribedDataSet(TargetVariablesConfig.builder().build())
        .build();
  }

  /** The baseline: C1 with WG/W1 and RG/R1, publishing PDS. */
  private static PubSubConfig baseConfig() {
    return configWith(dataSet("PDS", 1), writer("W1", 1, 1), 1000, reader("R1", Duration.ZERO));
  }

  private static PubSubConfig configWith(
      PublishedDataSetConfig dataSet,
      DataSetWriterConfig writer,
      int publishingIntervalMillis,
      DataSetReaderConfig reader) {

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            connection(
                "C1",
                1,
                List.of(writerGroup("WG", 1, publishingIntervalMillis, writer)),
                List.of(readerGroup("RG", reader))))
        .build();
  }

  private static ConfigDiff.Change findChange(
      ConfigDiff.Result result, ConfigDiff.Kind kind, ConfigDiff.Level level, String path) {

    for (ConfigDiff.Change change : result.changes()) {
      if (change.kind() == kind && change.level() == level && change.path().equals(path)) {
        return change;
      }
    }
    return fail(
        "no change %s/%s at '%s'; actual changes: %s"
            .formatted(kind, level, path, result.changes()));
  }

  // endregion

  // region ConfigDiff unit tests

  @Test
  void identicalConfigsProduceEmptyDiff() {
    ConfigDiff.Result result = ConfigDiff.diff(baseConfig(), baseConfig());

    assertFalse(result.rootEnabledChanged());
    assertTrue(result.changes().isEmpty(), result.changes()::toString);
  }

  @Test
  void rootEnabledFlagChangeIsDetected() {
    PubSubConfig disabled = baseConfig().toBuilder().enabled(false).build();

    ConfigDiff.Result result = ConfigDiff.diff(baseConfig(), disabled);

    assertTrue(result.rootEnabledChanged());
    assertTrue(result.changes().isEmpty());
  }

  @Test
  void leafWriterChangeProducesSingleWriterChange() {
    PubSubConfig changed =
        configWith(dataSet("PDS", 1), writer("W1", 1, 5), 1000, reader("R1", Duration.ZERO));

    ConfigDiff.Result result = ConfigDiff.diff(baseConfig(), changed);

    assertEquals(1, result.changes().size());
    ConfigDiff.Change change =
        findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.DATA_SET_WRITER, "C1/WG/W1");
    assertEquals("C1", change.connectionName());
    assertEquals("WG", change.groupName());
    assertEquals("W1", change.componentName());
  }

  @Test
  void leafReaderChangeProducesSingleReaderChange() {
    PubSubConfig changed =
        configWith(
            dataSet("PDS", 1), writer("W1", 1, 1), 1000, reader("R1", Duration.ofSeconds(5)));

    ConfigDiff.Result result = ConfigDiff.diff(baseConfig(), changed);

    assertEquals(1, result.changes().size());
    findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.DATA_SET_READER, "C1/RG/R1");
  }

  /**
   * Broker subscriber channels compute their SUBSCRIBE set from the connection config captured at
   * channel open, so reader-side changes on a broker (non-UDP) connection escalate to a
   * connection-level CHANGED (the connection is rebuilt and resubscribes the new queue set).
   */
  @Test
  void brokerReaderSideChangeEscalatesToConnectionChange() {
    PubSubConfig old = mqttConfigWithReaderQueue("data/t1");
    PubSubConfig changed = mqttConfigWithReaderQueue("data/t2");

    ConfigDiff.Result result = ConfigDiff.diff(old, changed);

    assertEquals(1, result.changes().size());
    findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.CONNECTION, "M1");
  }

  /** A reader added to a live broker connection also escalates to a connection rebuild. */
  @Test
  void brokerReaderAdditionEscalatesToConnectionChange() {
    PubSubConfig old = mqttConfig(mqttReaderGroup(brokerReader("R1", "data/t1")));
    PubSubConfig changed =
        mqttConfig(mqttReaderGroup(brokerReader("R1", "data/t1"), brokerReader("R2", "data/t2")));

    ConfigDiff.Result result = ConfigDiff.diff(old, changed);

    assertEquals(1, result.changes().size());
    findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.CONNECTION, "M1");
  }

  /**
   * Writer-side changes on a broker connection keep the granular diff (publisher addresses are
   * resolved per send, not captured at channel open).
   */
  @Test
  void brokerWriterSideChangeKeepsGranularDiff() {
    PubSubConfig old =
        PubSubConfig.builder()
            .publishedDataSet(dataSet("PDS", 1))
            .connection(mqttConnection(List.of(writerGroup("WG", 1, 1000, writer("W1", 1, 1)))))
            .build();
    PubSubConfig changed =
        PubSubConfig.builder()
            .publishedDataSet(dataSet("PDS", 1))
            .connection(mqttConnection(List.of(writerGroup("WG", 1, 1000, writer("W1", 1, 5)))))
            .build();

    ConfigDiff.Result result = ConfigDiff.diff(old, changed);

    assertEquals(1, result.changes().size());
    findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.DATA_SET_WRITER, "M1/WG/W1");
  }

  private static PubSubConfig mqttConfigWithReaderQueue(String queueName) {
    return mqttConfig(mqttReaderGroup(brokerReader("R1", queueName)));
  }

  private static PubSubConfig mqttConfig(ReaderGroupConfig readerGroup) {
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("M1")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .readerGroup(readerGroup)
            .build();
    return PubSubConfig.builder().connection(connection).build();
  }

  private static MqttConnectionConfig mqttConnection(List<WriterGroupConfig> writerGroups) {
    MqttConnectionConfig.Builder builder =
        MqttConnectionConfig.builder("M1")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.uint16(ushort(9)));
    writerGroups.forEach(builder::writerGroup);
    return builder.build();
  }

  private static ReaderGroupConfig mqttReaderGroup(DataSetReaderConfig... readers) {
    ReaderGroupConfig.Builder builder = ReaderGroupConfig.builder("RG");
    for (DataSetReaderConfig reader : readers) {
      builder.dataSetReader(reader);
    }
    return builder.build();
  }

  private static DataSetReaderConfig brokerReader(String name, String queueName) {
    return DataSetReaderConfig.builder(name)
        .brokerTransport(BrokerTransportSettings.builder().queueName(queueName).build())
        .build();
  }

  @Test
  void groupShellChangeProducesGroupChangeWithoutDescendingIntoLeaves() {
    PubSubConfig changed =
        configWith(dataSet("PDS", 1), writer("W1", 1, 1), 500, reader("R1", Duration.ZERO));

    ConfigDiff.Result result = ConfigDiff.diff(baseConfig(), changed);

    assertEquals(1, result.changes().size());
    ConfigDiff.Change change =
        findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.WRITER_GROUP, "C1/WG");
    assertEquals("C1", change.connectionName());
    assertEquals("WG", change.groupName());
  }

  @Test
  void connectionShellChangeProducesConnectionChangeOnly() {
    PubSubConfig changed =
        PubSubConfig.builder()
            .publishedDataSet(dataSet("PDS", 1))
            .connection(
                connection(
                    "C1",
                    2, // changed PublisherId: a connection "shell" property
                    List.of(writerGroup("WG", 1, 1000, writer("W1", 1, 1))),
                    List.of(readerGroup("RG", reader("R1", Duration.ZERO)))))
            .build();

    ConfigDiff.Result result = ConfigDiff.diff(baseConfig(), changed);

    assertEquals(1, result.changes().size());
    findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.CONNECTION, "C1");
  }

  @Test
  void addedAndRemovedConnectionsAreReported() {
    PubSubConfig withExtra =
        baseConfig().toBuilder().connection(connection("C2", 2, List.of(), List.of())).build();

    ConfigDiff.Result added = ConfigDiff.diff(baseConfig(), withExtra);
    assertEquals(1, added.changes().size());
    findChange(added, ConfigDiff.Kind.ADDED, ConfigDiff.Level.CONNECTION, "C2");

    ConfigDiff.Result removed = ConfigDiff.diff(withExtra, baseConfig());
    assertEquals(1, removed.changes().size());
    findChange(removed, ConfigDiff.Kind.REMOVED, ConfigDiff.Level.CONNECTION, "C2");
  }

  @Test
  void addedAndRemovedGroupsAreReported() {
    PubSubConfig withExtraGroup =
        PubSubConfig.builder()
            .publishedDataSet(dataSet("PDS", 1))
            .connection(
                connection(
                    "C1",
                    1,
                    List.of(
                        writerGroup("WG", 1, 1000, writer("W1", 1, 1)),
                        writerGroup("WG2", 2, 1000)),
                    List.of(readerGroup("RG", reader("R1", Duration.ZERO)))))
            .build();

    ConfigDiff.Result added = ConfigDiff.diff(baseConfig(), withExtraGroup);
    assertEquals(1, added.changes().size());
    ConfigDiff.Change change =
        findChange(added, ConfigDiff.Kind.ADDED, ConfigDiff.Level.WRITER_GROUP, "C1/WG2");
    assertEquals("WG2", change.groupName());

    ConfigDiff.Result removed = ConfigDiff.diff(withExtraGroup, baseConfig());
    assertEquals(1, removed.changes().size());
    findChange(removed, ConfigDiff.Kind.REMOVED, ConfigDiff.Level.WRITER_GROUP, "C1/WG2");
  }

  @Test
  void addedAndRemovedLeavesAreReported() {
    PubSubConfig withExtraReader =
        PubSubConfig.builder()
            .publishedDataSet(dataSet("PDS", 1))
            .connection(
                connection(
                    "C1",
                    1,
                    List.of(writerGroup("WG", 1, 1000, writer("W1", 1, 1))),
                    List.of(
                        readerGroup(
                            "RG", reader("R1", Duration.ZERO), reader("R2", Duration.ZERO)))))
            .build();

    ConfigDiff.Result added = ConfigDiff.diff(baseConfig(), withExtraReader);
    assertEquals(1, added.changes().size());
    ConfigDiff.Change change =
        findChange(added, ConfigDiff.Kind.ADDED, ConfigDiff.Level.DATA_SET_READER, "C1/RG/R2");
    assertEquals("R2", change.componentName());

    ConfigDiff.Result removed = ConfigDiff.diff(withExtraReader, baseConfig());
    assertEquals(1, removed.changes().size());
    findChange(removed, ConfigDiff.Kind.REMOVED, ConfigDiff.Level.DATA_SET_READER, "C1/RG/R2");
  }

  @Test
  void publishedDataSetChangeInducesReferencingWriterChange() {
    PubSubConfig changed =
        configWith(dataSet("PDS", 2), writer("W1", 1, 1), 1000, reader("R1", Duration.ZERO));

    ConfigDiff.Result result = ConfigDiff.diff(baseConfig(), changed);

    assertEquals(2, result.changes().size());
    findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.PUBLISHED_DATA_SET, "PDS");
    findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.DATA_SET_WRITER, "C1/WG/W1");
  }

  @Test
  void inducedWriterChangeIsNotAddedWhenAncestorAlreadyChanged() {
    // both the dataset and the writer group shell change: the induced writer restart is
    // covered by the group change and must not be reported twice
    PubSubConfig changed =
        configWith(dataSet("PDS", 2), writer("W1", 1, 1), 500, reader("R1", Duration.ZERO));

    ConfigDiff.Result result = ConfigDiff.diff(baseConfig(), changed);

    assertEquals(2, result.changes().size());
    findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.PUBLISHED_DATA_SET, "PDS");
    findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.WRITER_GROUP, "C1/WG");
  }

  @Test
  void standaloneChangeInducesReaderChangeOnlyForReadersWithoutOwnMetadata() {
    DataSetReaderConfig readerWithoutMetadata =
        DataSetReaderConfig.builder("R1")
            .subscribedDataSet(new StandaloneSubscribedDataSetRef("SDS"))
            .build();
    DataSetReaderConfig readerWithMetadata =
        DataSetReaderConfig.builder("R2")
            .dataSetMetaData(metaData("MD", 1))
            .subscribedDataSet(new StandaloneSubscribedDataSetRef("SDS"))
            .build();

    PubSubConfig oldConfig =
        PubSubConfig.builder()
            .standaloneSubscribedDataSet(standalone("SDS", 1))
            .connection(
                connection(
                    "C1",
                    1,
                    List.of(),
                    List.of(readerGroup("RG", readerWithoutMetadata, readerWithMetadata))))
            .build();

    PubSubConfig newConfig =
        PubSubConfig.builder()
            .standaloneSubscribedDataSet(standalone("SDS", 2))
            .connection(
                connection(
                    "C1",
                    1,
                    List.of(),
                    List.of(readerGroup("RG", readerWithoutMetadata, readerWithMetadata))))
            .build();

    ConfigDiff.Result result = ConfigDiff.diff(oldConfig, newConfig);

    assertEquals(2, result.changes().size());
    findChange(
        result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.STANDALONE_SUBSCRIBED_DATA_SET, "SDS");
    // only R1 resolves its metadata through the standalone SubscribedDataSet
    findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.DATA_SET_READER, "C1/RG/R1");
  }

  @Test
  void securityGroupChangeIsReportedAsOtherOnly() {
    PubSubConfig oldConfig =
        baseConfig().toBuilder().securityGroup(SecurityGroupConfig.builder("SG").build()).build();
    PubSubConfig newConfig =
        baseConfig().toBuilder()
            .securityGroup(
                SecurityGroupConfig.builder("SG").keyLifeTime(Duration.ofMinutes(5)).build())
            .build();

    ConfigDiff.Result result = ConfigDiff.diff(oldConfig, newConfig);

    assertEquals(1, result.changes().size());
    findChange(result, ConfigDiff.Kind.CHANGED, ConfigDiff.Level.SECURITY_GROUP, "SG");
  }

  @Test
  void addedAndRemovedDataSetsAreReportedAsOther() {
    PubSubConfig withExtra = baseConfig().toBuilder().publishedDataSet(dataSet("PDS2", 1)).build();

    ConfigDiff.Result added = ConfigDiff.diff(baseConfig(), withExtra);
    assertEquals(1, added.changes().size());
    findChange(added, ConfigDiff.Kind.ADDED, ConfigDiff.Level.PUBLISHED_DATA_SET, "PDS2");

    ConfigDiff.Result removed = ConfigDiff.diff(withExtra, baseConfig());
    assertEquals(1, removed.changes().size());
    findChange(removed, ConfigDiff.Kind.REMOVED, ConfigDiff.Level.PUBLISHED_DATA_SET, "PDS2");
  }

  // endregion

  // region reconfigure through PubSubService (never started: no transports required)

  private @Nullable PubSubService service;

  @AfterEach
  void closeService() {
    if (service != null) {
      service.close();
      service = null;
    }
  }

  @Test
  void reconfigureReplacesChangedWriterAndInvalidatesItsHandle() {
    service = PubSubService.create(baseConfig());

    PubSubHandle oldHandle = service.components().dataSetWriter("C1", "WG", "W1").orElseThrow();

    PubSubConfig changed =
        configWith(dataSet("PDS", 1), writer("W1", 1, 5), 1000, reader("R1", Duration.ZERO));

    ReconfigureResult result = service.reconfigure(changed, ReconfigureMode.DISABLE_AFFECTED);

    assertEquals(List.of("C1/WG/W1"), result.restartedPaths());
    assertTrue(result.addedPaths().isEmpty());
    assertTrue(result.removedPaths().isEmpty());

    // the old handle is invalidated...
    assertThrows(IllegalArgumentException.class, () -> service.state(oldHandle));

    // ... and the path resolves to a new handle
    PubSubHandle newHandle = service.components().dataSetWriter("C1", "WG", "W1").orElseThrow();
    assertNotSame(oldHandle, newHandle);
    // enabled but the service is not started: the replacement component is Paused
    assertEquals(PubSubState.Paused, service.state(newHandle));

    // unaffected components keep their handles
    PubSubHandle readerHandle = service.components().dataSetReader("C1", "RG", "R1").orElseThrow();
    assertEquals(PubSubState.Paused, service.state(readerHandle));
  }

  @Test
  void stopAndRestartModeRestartsTheWholeContainingConnection() {
    service = PubSubService.create(baseConfig());

    PubSubHandle oldConnection = service.components().connection("C1").orElseThrow();
    PubSubHandle oldReader = service.components().dataSetReader("C1", "RG", "R1").orElseThrow();

    PubSubConfig changed =
        configWith(dataSet("PDS", 1), writer("W1", 1, 5), 1000, reader("R1", Duration.ZERO));

    ReconfigureResult result = service.reconfigure(changed, ReconfigureMode.STOP_AND_RESTART);

    assertEquals(List.of("C1"), result.restartedPaths());
    assertTrue(result.addedPaths().isEmpty());
    assertTrue(result.removedPaths().isEmpty());

    // the entire connection subtree was replaced, even the unchanged reader
    assertThrows(IllegalArgumentException.class, () -> service.state(oldConnection));
    assertThrows(IllegalArgumentException.class, () -> service.state(oldReader));
    assertNotSame(oldConnection, service.components().connection("C1").orElseThrow());
  }

  @Test
  void removedConnectionInvalidatesItsSubtreeHandles() {
    service = PubSubService.create(baseConfig());

    PubSubHandle connectionHandle = service.components().connection("C1").orElseThrow();
    PubSubHandle groupHandle = service.components().writerGroup("C1", "WG").orElseThrow();
    PubSubHandle writerHandle = service.components().dataSetWriter("C1", "WG", "W1").orElseThrow();

    PubSubConfig withoutConnection =
        PubSubConfig.builder().publishedDataSet(dataSet("PDS", 1)).build();

    ReconfigureResult result =
        service.reconfigure(withoutConnection, ReconfigureMode.DISABLE_AFFECTED);

    assertEquals(List.of("C1"), result.removedPaths());
    assertThrows(IllegalArgumentException.class, () -> service.state(connectionHandle));
    assertThrows(IllegalArgumentException.class, () -> service.state(groupHandle));
    assertThrows(IllegalArgumentException.class, () -> service.state(writerHandle));
    assertTrue(service.components().connection("C1").isEmpty());
  }

  @Test
  void addedReaderIsResolvableAfterReconfigure() {
    service = PubSubService.create(baseConfig());

    PubSubConfig withExtraReader =
        PubSubConfig.builder()
            .publishedDataSet(dataSet("PDS", 1))
            .connection(
                connection(
                    "C1",
                    1,
                    List.of(writerGroup("WG", 1, 1000, writer("W1", 1, 1))),
                    List.of(
                        readerGroup(
                            "RG", reader("R1", Duration.ZERO), reader("R2", Duration.ZERO)))))
            .build();

    ReconfigureResult result =
        service.reconfigure(withExtraReader, ReconfigureMode.DISABLE_AFFECTED);

    assertEquals(List.of("C1/RG/R2"), result.addedPaths());
    assertTrue(result.removedPaths().isEmpty());
    assertTrue(result.restartedPaths().isEmpty());

    PubSubHandle r2 = service.components().dataSetReader("C1", "RG", "R2").orElseThrow();
    assertEquals(PubSubState.Paused, service.state(r2));
  }

  @Test
  void publishedDataSetChangeRestartsReferencingWriterThroughReconfigure() {
    service = PubSubService.create(baseConfig());

    PubSubHandle oldWriter = service.components().dataSetWriter("C1", "WG", "W1").orElseThrow();

    PubSubConfig changed =
        configWith(dataSet("PDS", 2), writer("W1", 1, 1), 1000, reader("R1", Duration.ZERO));

    ReconfigureResult result = service.reconfigure(changed, ReconfigureMode.DISABLE_AFFECTED);

    assertEquals(List.of("C1/WG/W1"), result.restartedPaths());
    assertThrows(IllegalArgumentException.class, () -> service.state(oldWriter));
    assertNotSame(oldWriter, service.components().dataSetWriter("C1", "WG", "W1").orElseThrow());
  }

  @Test
  void reconfigureAfterShutdownThrowsIllegalStateException() {
    PubSubService localService = PubSubService.create(baseConfig());
    localService.close();

    assertThrows(
        IllegalStateException.class,
        () -> localService.reconfigure(baseConfig(), ReconfigureMode.DISABLE_AFFECTED));
  }

  @Test
  void foreignHandleIsRejected() {
    service = PubSubService.create(baseConfig());

    var foreign = new PubSubHandle(ComponentType.DATA_SET_WRITER, "C1/WG/W1");

    assertThrows(IllegalArgumentException.class, () -> service.state(foreign));
    assertThrows(IllegalArgumentException.class, () -> service.enable(foreign));
    assertThrows(IllegalArgumentException.class, () -> service.disable(foreign));
  }

  // endregion
}
