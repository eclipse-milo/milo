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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Diagnostics exposure read by a REAL client: the per-component Diagnostics values served over the
 * wire equal the engine collector's snapshot at quiescence (never compared mid-flight), the
 * Mandatory DSW and DSR {@code FailedDataSetMessages} counter nodes are PRESENT with Error
 * classification, the token-pair LiveValues exist exactly on secured groups, the read-only {@code
 * DiagnosticsLevel} rejects client writes, and the error side is observed through the minted nodes:
 * a reader driven into Error flips its group's {@code SubError} and its {@code TotalError} equals
 * the sum of its Error-classified exposed counters.
 *
 * <p>Topology: one enabled publishing writer group fed by a callback source (loopback unicast to a
 * silent sink port), one DISABLED secured writer group (structure needs no key material), and a
 * DISABLED reader group/reader pair whose reader carries a short {@code messageReceiveTimeout}. The
 * error scenario enables the pair, feeds the reader from a short-lived external publisher until it
 * turns Operational, then stops the publisher so the timeout expires into Error (the deterministic
 * deterministic stimulus). The engine's 64-bit counters cross the exposure's UInt32 clamp ({@code
 * PubSubDiagnosticsExposure.clampUInt32}) on their way to the wire — the clamp boundary itself is
 * unit-pinned in {@code PubSubDiagnosticsComputationTest}.
 */
class DiagnosticsExposureIntegrationTest {

  private static final long TIMEOUT_SECONDS = 15;

  private static final String CONNECTION = "dx-conn";
  private static final String WRITER_GROUP = "wgrp";
  private static final String WRITER = "writer";
  private static final String SECURED_GROUP = "sgrp";
  private static final String READER_GROUP = "rgrp";
  private static final String READER = "reader";
  private static final String DATA_SET = "dx-ds";
  private static final String EXTERNAL_DATA_SET = "dx-ext-ds";
  private static final String SECURITY_GROUP = "dx-sg";

  private static final UadpWriterGroupSettings GROUP_SETTINGS =
      UadpWriterGroupSettings.builder()
          .networkMessageContentMask(
              UadpNetworkMessageContentMask.of(
                  UadpNetworkMessageContentMask.Field.PublisherId,
                  UadpNetworkMessageContentMask.Field.GroupHeader,
                  UadpNetworkMessageContentMask.Field.WriterGroupId,
                  UadpNetworkMessageContentMask.Field.SequenceNumber,
                  UadpNetworkMessageContentMask.Field.PayloadHeader))
          .build();

  private static final UadpDataSetWriterSettings WRITER_SETTINGS =
      UadpDataSetWriterSettings.builder()
          .dataSetMessageContentMask(
              UadpDataSetMessageContentMask.of(
                  UadpDataSetMessageContentMask.Field.Timestamp,
                  UadpDataSetMessageContentMask.Field.MajorVersion,
                  UadpDataSetMessageContentMask.Field.MinorVersion,
                  UadpDataSetMessageContentMask.Field.SequenceNumber))
          .build();

  private static final DataSetFieldContentMask FIELD_MASK =
      DataSetFieldContentMask.of(DataSetFieldContentMask.Field.StatusCode);

  private static SksTestServer testServer;
  private static ServerPubSub serverPubSub;
  private static OpcUaClient client;

  /** The server connection's data port; the external publisher targets it. */
  private static int dataPort;

  @BeforeAll
  static void startServerAndClient() throws Exception {
    testServer = SksTestServer.create(null);
    dataPort = freeUdpPort();

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(DATA_SET)
            .field(
                FieldDefinition.builder("value")
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(new UUID(0L, 0xDA1L))
                    .build())
            .build();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(
                dataSet.ref(),
                context -> {
                  DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
                  builder.field("value", new DataValue(Variant.ofDouble(1.5)));
                  return builder.build();
                })
            .build();

    serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            topology(dataSet),
            ServerPubSubOptions.builder()
                .exposeInformationModel(true)
                .diagnosticsEnabled(true)
                .bindings(bindings)
                .build());
    serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

    client = connect();
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

  @Test
  void wireValuesEqualTheCollectorSnapshotAtQuiescence() throws Exception {
    String groupPath = CONNECTION + "/" + WRITER_GROUP;
    String writerPath = groupPath + "/" + WRITER;

    // drive real traffic until the group has sent a few NetworkMessages
    await(
        "publisher traffic",
        () -> {
          ComponentDiagnostics diagnostics = snapshotOf(groupPath);
          return diagnostics != null && diagnostics.networkMessagesSent() >= 3;
        });

    // QUIESCE before comparing (trap 5): disable the connection and wait for the counter to
    // stop moving
    PubSubHandle connection =
        serverPubSub.runtime().components().connection(CONNECTION).orElseThrow();
    serverPubSub.runtime().disable(connection);
    awaitStable(groupPath);

    ComponentDiagnostics group = Objects.requireNonNull(snapshotOf(groupPath));
    ComponentDiagnostics writer = Objects.requireNonNull(snapshotOf(writerPath));
    ComponentDiagnostics conn = Objects.requireNonNull(snapshotOf(CONNECTION));

    // the wire values equal the snapshot, through the exposure's UInt32 clamp
    assertEquals(
        PubSubDiagnosticsExposure.clampUInt32(group.networkMessagesSent()),
        goodValue(diagnosticsNodeId(groupPath, "Counters/SentNetworkMessages")),
        "SentNetworkMessages");
    assertEquals(
        PubSubDiagnosticsExposure.clampUInt32(group.failedTransmissions()),
        goodValue(diagnosticsNodeId(groupPath, "Counters/FailedTransmissions")),
        "FailedTransmissions");
    assertEquals(
        PubSubDiagnosticsExposure.clampUInt32(writer.failedDataSetMessages()),
        goodValue(diagnosticsNodeId(writerPath, "Counters/FailedDataSetMessages")),
        "writer FailedDataSetMessages");

    // the six Table 311 State* counters on the connection object reflect the collector —
    // including the StateDisabledByMethod tick from the disable above (METHOD cause is
    // caller-agnostic)
    assertTrue(conn.stateDisabledByMethod() >= 1);
    assertEquals(
        PubSubDiagnosticsExposure.clampUInt32(conn.stateDisabledByMethod()),
        goodValue(diagnosticsNodeId(CONNECTION, "Counters/StateDisabledByMethod")));
    assertEquals(
        PubSubDiagnosticsExposure.clampUInt32(conn.stateError()),
        goodValue(diagnosticsNodeId(CONNECTION, "Counters/StateError")));
    assertEquals(
        PubSubDiagnosticsExposure.clampUInt32(conn.stateOperationalByMethod()),
        goodValue(diagnosticsNodeId(CONNECTION, "Counters/StateOperationalByMethod")));
    assertEquals(
        PubSubDiagnosticsExposure.clampUInt32(conn.stateOperationalByParent()),
        goodValue(diagnosticsNodeId(CONNECTION, "Counters/StateOperationalByParent")));
    assertEquals(
        PubSubDiagnosticsExposure.clampUInt32(conn.stateOperationalFromError()),
        goodValue(diagnosticsNodeId(CONNECTION, "Counters/StateOperationalFromError")));
    assertEquals(
        PubSubDiagnosticsExposure.clampUInt32(conn.statePausedByParent()),
        goodValue(diagnosticsNodeId(CONNECTION, "Counters/StatePausedByParent")));
  }

  @Test
  void mandatoryFailedDataSetMessagesCounterNodesArePresent() throws Exception {
    // the Mandatory DSW and DSR FailedDataSetMessages rows exist over the wire with the
    // §9.1.11 Error classification (previously unasserted — the flagged review gap)
    String writerCounter =
        CONNECTION
            + "/"
            + WRITER_GROUP
            + "/"
            + WRITER
            + "/Diagnostics/Counters/FailedDataSetMessages";
    String readerCounter =
        CONNECTION
            + "/"
            + READER_GROUP
            + "/"
            + READER
            + "/Diagnostics/Counters/FailedDataSetMessages";

    for (String identifier : List.of(writerCounter, readerCounter)) {
      NodeId counterNodeId = fragmentNodeId(identifier);

      DataValue value = readValue(counterNodeId);
      assertTrue(value.getStatusCode().isGood(), identifier + ": " + value.getStatusCode());
      assertNotNull(value.getValue().getValue(), identifier);

      Object classification =
          goodValue(new NodeId(namespaceIndex(), "PubSub/" + identifier + "/Classification"));
      assertEquals(
          PubSubDiagnosticsCounterClassification.Error.getValue(),
          ((Number) classification).intValue(),
          identifier + " classification");
    }
  }

  @Test
  void tokenPairLiveValuesExistExactlyOnSecuredGroups() throws Exception {
    // SecurityTokenID/TimeToNextTokenID minted for the SECURED group ...
    for (String member : List.of("SecurityTokenID", "TimeToNextTokenID")) {
      NodeId securedNodeId =
          fragmentNodeId(CONNECTION + "/" + SECURED_GROUP + "/Diagnostics/LiveValues/" + member);
      assertNotEquals(
          new StatusCode(StatusCodes.Bad_NodeIdUnknown),
          readValue(securedNodeId).getStatusCode(),
          member + " must exist on the secured group");

      // ... and absent on the unsecured group
      NodeId plainNodeId =
          fragmentNodeId(CONNECTION + "/" + WRITER_GROUP + "/Diagnostics/LiveValues/" + member);
      assertEquals(
          new StatusCode(StatusCodes.Bad_NodeIdUnknown),
          readValue(plainNodeId).getStatusCode(),
          member + " must not exist on the plain group");
    }
  }

  /**
   * Error side, observed through real nodes: a reader failure ticks its Error-classified {@code
   * StateError}, its {@code TotalError} equals the sum of its Error-classified exposed counters,
   * and the reader GROUP's {@code SubError} flips true (the §9.1.11.2 direct-child rollup).
   *
   * <p>The root {@code i=17409} SubError rolls up only its OWN direct child layer — the
   * connections' Error-classified counters — per the same direct-child rule, so a reader failure is
   * architecturally unable to flip it; the group-scope flip is the reachable end-to-end
   * observation.
   */
  @Test
  void readerFailureFlipsTheGroupsSubErrorAndTotalErrorSums() throws Exception {
    String groupPath = CONNECTION + "/" + READER_GROUP;
    String readerPath = groupPath + "/" + READER;

    // baseline: no reader has failed yet, so the group's SubError serves false
    assertEquals(
        Boolean.FALSE, goodValue(diagnosticsNodeId(groupPath, "SubError")), "SubError baseline");

    // deterministic error stimulus: make sure the connection is running
    // (another row disables it), enable the reader pair, and feed the reader from an external
    // publisher until it turns Operational (Table 2: a DataSetReader completes startup only on
    // its first key frame) — then STOP the publisher, so the configured messageReceiveTimeout
    // expires and the reader enters Error, ticking its Error-classified StateError counter
    PubSubHandle connection =
        serverPubSub.runtime().components().connection(CONNECTION).orElseThrow();
    if (serverPubSub.runtime().state(connection) == PubSubState.Disabled) {
      serverPubSub.runtime().enable(connection);
    }
    PubSubHandle readerGroup =
        serverPubSub.runtime().components().readerGroup(CONNECTION, READER_GROUP).orElseThrow();
    PubSubHandle reader =
        serverPubSub
            .runtime()
            .components()
            .dataSetReader(CONNECTION, READER_GROUP, READER)
            .orElseThrow();
    serverPubSub.runtime().enable(readerGroup);
    serverPubSub.runtime().enable(reader);

    try {
      PubSubService publisher = externalPublisher();
      try {
        publisher.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        await(
            "reader Operational on the external traffic",
            () -> serverPubSub.runtime().state(reader) == PubSubState.Operational);
      } finally {
        publisher.shutdown().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }

      await("reader Error entry", () -> serverPubSub.runtime().state(reader) == PubSubState.Error);

      // one consistent read set per attempt: should another Error tick land between the
      // reads of one attempt, the next attempt sees the settled values
      await(
          "error-side Total/SubError rollup at the node layer",
          () -> {
            try {
              long stateError =
                  longValue(goodValue(diagnosticsNodeId(readerPath, "Counters/StateError")));
              long failedDataSetMessages =
                  longValue(
                      goodValue(diagnosticsNodeId(readerPath, "Counters/FailedDataSetMessages")));
              long totalError = longValue(goodValue(diagnosticsNodeId(readerPath, "TotalError")));
              Object subError = goodValue(diagnosticsNodeId(groupPath, "SubError"));

              return stateError >= 1
                  && totalError == stateError + failedDataSetMessages
                  && Boolean.TRUE.equals(subError);
            } catch (UaException e) {
              return false;
            }
          });
    } finally {
      // restore the disabled posture the other rows assume; the counters (and with them the
      // flipped SubError) deliberately survive — Disable never resets diagnostics
      serverPubSub.runtime().disable(reader);
      serverPubSub.runtime().disable(readerGroup);
    }
  }

  @Test
  void diagnosticsLevelIsReadOnlyBasicOverTheWire() throws Exception {
    NodeId levelNodeId =
        fragmentNodeId(CONNECTION + "/" + WRITER_GROUP + "/Diagnostics/DiagnosticsLevel");

    // reads Basic (0) ...
    Object level = goodValue(levelNodeId);
    assertEquals(0, ((Number) level).intValue(), "DiagnosticsLevel Basic");

    // ... and rejects writes
    List<StatusCode> writeResults =
        client.writeValues(List.of(levelNodeId), List.of(DataValue.valueOnly(new Variant(1))));
    assertTrue(writeResults.get(0).isBad(), "DiagnosticsLevel write must be rejected");
  }

  // region fixtures + helpers

  private static PubSubConfig topology(PublishedDataSetConfig dataSet) throws SocketException {
    MessageSecurityConfig security =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.Sign)
            .securityGroup(new SecurityGroupRef(SECURITY_GROUP))
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .securityGroup(
            SecurityGroupConfig.builder(SECURITY_GROUP).keyLifeTime(Duration.ofMinutes(10)).build())
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4881)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", dataPort))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(
                    WriterGroupConfig.builder(WRITER_GROUP)
                        .writerGroupId(ushort(21))
                        .publishingInterval(Duration.ofMillis(100))
                        .dataSetWriter(
                            DataSetWriterConfig.builder(WRITER)
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(31))
                                .build())
                        .build())
                .writerGroup(
                    WriterGroupConfig.builder(SECURED_GROUP)
                        .enabled(false)
                        .writerGroupId(ushort(22))
                        .publishingInterval(Duration.ofMillis(100))
                        .messageSecurity(security)
                        .build())
                .readerGroup(
                    ReaderGroupConfig.builder(READER_GROUP)
                        .enabled(false)
                        .dataSetReader(
                            DataSetReaderConfig.builder(READER)
                                .enabled(false)
                                .publisherId(PublisherId.uint16(ushort(4882)))
                                .writerGroupId(ushort(1))
                                .dataSetWriterId(ushort(1))
                                .dataSetMetaData(externalMetaData())
                                .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                // the error stimulus: once the external publisher
                                // stops, the reader expires into Error
                                .messageReceiveTimeout(Duration.ofMillis(750))
                                .build())
                        .build())
                .build())
        .build();
  }

  /** A standalone publisher service whose writer matches the exposed reader's filters. */
  private static PubSubService externalPublisher() throws SocketException {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(EXTERNAL_DATA_SET)
            .field(
                FieldDefinition.builder("v")
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(externalFieldId())
                    .build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("dx-pub")
                    .publisherId(PublisherId.uint16(ushort(4882)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", dataPort))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(75))
                            .messageSettings(GROUP_SETTINGS)
                            .dataSetWriter(
                                DataSetWriterConfig.builder("writer")
                                    .dataSet(dataSet.ref())
                                    .dataSetWriterId(ushort(1))
                                    .fieldContentMask(FIELD_MASK)
                                    .settings(WRITER_SETTINGS)
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(
                dataSet.ref(),
                context -> {
                  DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
                  builder.field("v", new DataValue(Variant.ofDouble(3.5)));
                  return builder.build();
                })
            .build();

    return PubSubService.create(config, bindings);
  }

  private static DataSetMetaDataConfig externalMetaData() {
    return DataSetMetaDataConfig.builder(EXTERNAL_DATA_SET)
        .field("v", NodeIds.Double, externalFieldId())
        .build();
  }

  private static UUID externalFieldId() {
    return new UUID(0L, 0xDA2L);
  }

  private static ComponentDiagnostics snapshotOf(String path) {
    return serverPubSub.runtime().diagnostics().snapshot().get(path);
  }

  /** Deadline-poll until {@code condition} holds. */
  private static void await(String description, Supplier<Boolean> condition) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    while (!condition.get()) {
      if (System.nanoTime() > deadline) {
        fail("timed out awaiting " + description);
      }
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(25));
    }
  }

  /** Deadline-poll until the path's sent counter stops moving (post-disable quiescence). */
  private static void awaitStable(String path) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    while (true) {
      ComponentDiagnostics first = Objects.requireNonNull(snapshotOf(path));
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(250));
      ComponentDiagnostics second = Objects.requireNonNull(snapshotOf(path));
      if (first.networkMessagesSent() == second.networkMessagesSent()) {
        return;
      }
      if (System.nanoTime() > deadline) {
        fail("the path never quiesced: " + path);
      }
    }
  }

  private static UShort namespaceIndex() {
    return testServer.getServer().getServerNamespace().getNamespaceIndex();
  }

  private static NodeId fragmentNodeId(String identifier) {
    return new NodeId(namespaceIndex(), "PubSub/" + identifier);
  }

  private static NodeId diagnosticsNodeId(String componentPath, String member) {
    return fragmentNodeId(componentPath + "/Diagnostics/" + member);
  }

  private static DataValue readValue(NodeId nodeId) throws UaException {
    return client.readValues(0.0, TimestampsToReturn.Both, List.of(nodeId)).get(0);
  }

  private static Object goodValue(NodeId nodeId) throws UaException {
    DataValue value = readValue(nodeId);
    assertTrue(value.getStatusCode().isGood(), nodeId + ": " + value.getStatusCode());
    Object result = value.getValue().getValue();
    assertNotNull(result, nodeId.toString());
    return result;
  }

  private static long longValue(Object value) {
    return ((Number) value).longValue();
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
                    .setApplicationName(LocalizedText.english("diagnostics exposure test client"))
                    .setApplicationUri("urn:eclipse:milo:test:diagnostics-exposure-client")
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
