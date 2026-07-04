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
import java.util.function.IntPredicate;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The T5 §10.2 Status rows NOT already landed elsewhere (anti-duplication rule §15): ED8 — EVERY
 * component kind (connection, writer group, reader group, DataSetWriter, DataSetReader) answers the
 * minted Enable/Disable pair through the real Call service, exercising each kind's own
 * handle-lookup dispatch (previously only connection/WG/DSR handlers were ever invoked; a
 * wrong-handle-resolution bug in the RG or DSW arm was invisible) — and ED5 — the {@code
 * Status/State} variable of every kind REJECTS client writes (the state is engine-owned; the only
 * write path is the Method pair).
 *
 * <p>ED1-ED4 (the §9.1.10 state-rule matrix) are pinned in {@code PubSubStatusMethodsTest}, ED6 (no
 * store save) in {@code RemoteConfigPersistenceIntegrationTest}, and ED7 (no root Status pair) in
 * the info-model ns0 rows; none are duplicated here.
 */
class StatusEnableDisableIntegrationTest {

  private static final long TIMEOUT_SECONDS = 15;

  private static final String CONNECTION = "ed-conn";
  private static final String WRITER_GROUP = "wgrp";
  private static final String WRITER = "writer";
  private static final String READER_GROUP = "rgrp";
  private static final String READER = "reader";
  private static final String DATA_SET = "ed-ds";

  private static SksTestServer testServer;
  private static ServerPubSub serverPubSub;
  private static OpcUaClient client;

  @BeforeAll
  static void startServerAndClient() throws Exception {
    testServer = SksTestServer.create(null);

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(DATA_SET)
            .field(
                FieldDefinition.builder("value")
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(new UUID(0L, 0xED1L))
                    .build())
            .build();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(
                dataSet.ref(),
                context -> {
                  DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
                  builder.field("value", new DataValue(Variant.ofDouble(2.5)));
                  return builder.build();
                })
            .build();

    serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            topology(dataSet),
            ServerPubSubOptions.builder()
                .exposeInformationModel(true)
                .allowRemoteConfiguration(true)
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

  /**
   * ED8: five sub-rows of ED1/ED3, one per component kind, leaf-first so a parent's bounce never
   * interferes with a child pair already verified. Each Disable lands the kind's OWN minted handler
   * (per-kind handle lookup) and the State variable follows over the wire; each Enable takes the
   * component back out of Disabled.
   */
  @Test
  void everyComponentKindAnswersTheEnableDisablePair() throws Exception {
    List<String> paths =
        List.of(
            CONNECTION + "/" + WRITER_GROUP + "/" + WRITER,
            CONNECTION + "/" + READER_GROUP + "/" + READER,
            CONNECTION + "/" + WRITER_GROUP,
            CONNECTION + "/" + READER_GROUP,
            CONNECTION);

    for (String path : paths) {
      // precondition: the startup cascade left the component out of Disabled
      awaitState(path, state -> state != PubSubState.Disabled.getValue());

      assertEquals(
          StatusCode.GOOD,
          callStatus(path, "Disable").getStatusCode(),
          path + ": Disable on a non-Disabled component");
      awaitState(path, state -> state == PubSubState.Disabled.getValue());

      assertEquals(
          StatusCode.GOOD,
          callStatus(path, "Enable").getStatusCode(),
          path + ": Enable on a Disabled component");
      awaitState(path, state -> state != PubSubState.Disabled.getValue());
    }
  }

  /**
   * ED5: the {@code Status/State} variable is not client-writable on any kind — the write is
   * rejected ({@code Bad_NotWritable}; {@code Bad_UserAccessDenied} would equally satisfy the row's
   * "per node config" latitude) and the served state never becomes the written garbage.
   */
  @Test
  void statusStateVariablesRejectClientWrites() throws Exception {
    List<String> paths =
        List.of(
            CONNECTION,
            CONNECTION + "/" + WRITER_GROUP,
            CONNECTION + "/" + WRITER_GROUP + "/" + WRITER,
            CONNECTION + "/" + READER_GROUP,
            CONNECTION + "/" + READER_GROUP + "/" + READER);

    for (String path : paths) {
      NodeId stateNodeId = fragmentNodeId(path + "/Status/State");

      // Error is unreachable in this quiet fixture: were the write accepted, it would show
      StatusCode result =
          client
              .writeValues(
                  List.of(stateNodeId),
                  List.of(DataValue.valueOnly(new Variant(PubSubState.Error.getValue()))))
              .get(0);

      assertTrue(result.isBad(), path + ": the State write must be rejected, was " + result);
      assertTrue(
          result.getValue() == StatusCodes.Bad_NotWritable
              || result.getValue() == StatusCodes.Bad_UserAccessDenied,
          path + ": unexpected rejection code " + result);

      assertNotEquals(
          PubSubState.Error.getValue(),
          readState(path),
          path + ": the rejected write must not have changed the served State");
    }
  }

  // region fixtures + helpers

  /** All five kinds, everything enabled; the reader listens for a publisher that never exists. */
  private static PubSubConfig topology(PublishedDataSetConfig dataSet) throws SocketException {
    var metaData =
        DataSetMetaDataConfig.builder(DATA_SET)
            .field("value", NodeIds.Double, new UUID(0L, 0xED2L))
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4886)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(
                    WriterGroupConfig.builder(WRITER_GROUP)
                        .writerGroupId(ushort(26))
                        .publishingInterval(Duration.ofMillis(100))
                        .dataSetWriter(
                            DataSetWriterConfig.builder(WRITER)
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(36))
                                .build())
                        .build())
                .readerGroup(
                    ReaderGroupConfig.builder(READER_GROUP)
                        .dataSetReader(
                            DataSetReaderConfig.builder(READER)
                                .publisherId(PublisherId.uint16(ushort(4887)))
                                .writerGroupId(ushort(1))
                                .dataSetWriterId(ushort(1))
                                .dataSetMetaData(metaData)
                                .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                .build())
                        .build())
                .build())
        .build();
  }

  private CallMethodResult callStatus(String path, String methodName) throws UaException {
    NodeId objectId = fragmentNodeId(path + "/Status");
    NodeId methodId = fragmentNodeId(path + "/Status/" + methodName);

    CallResponse response =
        client.call(List.of(new CallMethodRequest(objectId, methodId, new Variant[0])));
    CallMethodResult[] results = response.getResults();
    assertTrue(results != null && results.length == 1);
    return results[0];
  }

  /** The wire value of {@code "PubSub/<path>/Status/State"} as the enumeration's int value. */
  private static int readState(String path) throws UaException {
    NodeId stateNodeId = fragmentNodeId(path + "/Status/State");
    DataValue value = client.readValues(0.0, TimestampsToReturn.Both, List.of(stateNodeId)).get(0);
    assertTrue(value.getStatusCode().isGood(), stateNodeId + ": " + value.getStatusCode());
    Object raw = value.getValue().getValue();
    assertNotNull(raw, stateNodeId.toString());
    return ((Number) raw).intValue();
  }

  /** Deadline-poll the State variable over the wire until {@code predicate} accepts it. */
  private static void awaitState(String path, IntPredicate predicate) throws UaException {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    int last = Integer.MIN_VALUE;
    while (System.nanoTime() < deadline) {
      last = readState(path);
      if (predicate.test(last)) {
        return;
      }
      Thread.onSpinWait();
    }
    fail("timed out awaiting a State change of %s; last value: %d".formatted(path, last));
  }

  private static NodeId fragmentNodeId(String identifier) {
    UShort idx = testServer.getServer().getServerNamespace().getNamespaceIndex();
    return new NodeId(idx, "PubSub/" + identifier);
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
                    .setApplicationName(LocalizedText.english("status enable/disable test client"))
                    .setApplicationUri("urn:eclipse:milo:test:status-enable-disable-client")
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
