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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldSelector;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariableConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariablesConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The §9.1.10 Enable/Disable Method matrix: current-state rules against a started runtime's real
 * states (Operational, PreOperational, Paused, Disabled), the session/authorization prefix
 * (session-less {@code Bad_UserAccessDenied}, authorizer codes surfaced verbatim,
 * default-authorizer RoleMapper posture), the {@code allowRemoteConfiguration} gate (default false:
 * no method nodes minted), the root-pair absence (ns0 {@code i=17407}/{@code i=17408} are never
 * minted), and handler survival across component restarts.
 *
 * <p>Handlers are invoked directly with a stubbed {@link AccessContext} and a mocked {@link
 * Session} (the {@link SksServerFaceTest} pattern): the state rules and check order are
 * handler-side behavior.
 *
 * <p>Network safety: connections use unicast 127.0.0.1 with ephemeral ports and an explicit
 * loopback {@code discoveryAddress}, so the engine's discovery channels never touch the default
 * multicast group 224.0.2.14:4840.
 */
class PubSubStatusMethodsTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final String CONN = "sm-conn";
  private static final String CONN_OFF = "sm-conn-off";
  private static final String WRITER_GROUP = "wgrp";
  private static final String WRITER = "writer";
  private static final String READER_GROUP = "rgrp";
  private static final String READER = "reader";
  private static final String WRITER_GROUP2 = "wgrp2";
  private static final String DATA_SET = "sm-ds";

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4771));
  private static final PublisherId PUBLISHER_ID_OFF = PublisherId.uint16(ushort(4772));
  private static final PublisherId EXTERNAL_PUBLISHER_ID = PublisherId.uint16(ushort(4773));
  private static final UShort EXTERNAL_GROUP_ID = ushort(1);
  private static final UShort EXTERNAL_WRITER_ID = ushort(1);

  private static final NodeId UNRELATED_ROLE = new NodeId(1, "unrelated-role");

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

  private static TestPubSubServer testServer;
  private static NodeId sourceNodeId;
  private static NodeId targetNodeId;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();

    sourceNodeId =
        testServer.addVariable("SM_Source", NodeIds.Double, new DataValue(Variant.ofDouble(1.5)));
    targetNodeId =
        testServer.addVariable("SM_Target", NodeIds.Double, new DataValue(Variant.ofDouble(0.0)));
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  @AfterEach
  void tearDown() {
    attached.forEach(ServerPubSub::close);
    attached.clear();
  }

  @Test
  void stateRulesFollowTheSection91010CurrentStateTable() throws Exception {
    Ports ports = Ports.pick();
    attachAndStart(ports, remoteConfigOptions(), true);

    awaitState("PubSub/" + CONN, PubSubState.Operational);
    awaitState("PubSub/" + CONN + "/" + READER_GROUP + "/" + READER, PubSubState.PreOperational);
    awaitState("PubSub/" + CONN_OFF, PubSubState.Disabled);
    awaitState("PubSub/" + CONN_OFF + "/" + WRITER_GROUP2, PubSubState.Paused);

    Session session = sessionWithRoles(null);

    // Enable is rejected unless the current State is Disabled (§9.1.10.2)
    assertStatus(StatusCodes.Bad_InvalidState, invokeEnable("PubSub/" + CONN, session));
    assertStatus(
        StatusCodes.Bad_InvalidState,
        invokeEnable("PubSub/" + CONN + "/" + READER_GROUP + "/" + READER, session));
    assertStatus(
        StatusCodes.Bad_InvalidState,
        invokeEnable("PubSub/" + CONN_OFF + "/" + WRITER_GROUP2, session));

    // Disable is rejected iff the current State is Disabled (§9.1.10.3 body sentence; the
    // result-table's "not operational" description is a spec typo)
    assertStatus(StatusCodes.Bad_InvalidState, invokeDisable("PubSub/" + CONN_OFF, session));

    // Disable on Paused succeeds (the state is not Disabled)
    assertStatus(null, invokeDisable("PubSub/" + CONN_OFF + "/" + WRITER_GROUP2, session));
    awaitState("PubSub/" + CONN_OFF + "/" + WRITER_GROUP2, PubSubState.Disabled);

    // Enable on Disabled succeeds; the parent is still disabled, so the group goes Paused
    assertStatus(null, invokeEnable("PubSub/" + CONN_OFF + "/" + WRITER_GROUP2, session));
    awaitState("PubSub/" + CONN_OFF + "/" + WRITER_GROUP2, PubSubState.Paused);

    // Enable on the Disabled connection succeeds and the component leaves Disabled
    assertStatus(null, invokeEnable("PubSub/" + CONN_OFF, session));
    awaitState("PubSub/" + CONN_OFF, PubSubState.Operational);

    // Disable on Operational succeeds
    assertStatus(null, invokeDisable("PubSub/" + CONN, session));
    awaitState("PubSub/" + CONN, PubSubState.Disabled);
  }

  @Test
  void sessionAndAuthorizationChecksPrecedeTheStateRule() throws Exception {
    Ports ports = Ports.pick();
    attachAndStart(ports, remoteConfigOptions(), true);

    awaitState("PubSub/" + CONN_OFF, PubSubState.Disabled);

    String statusPath = "PubSub/" + CONN_OFF;

    // session-less invocation: Bad_UserAccessDenied, before any state evaluation
    UaMethodNode disableNode = methodNode(statusPath + "/Status/Disable");
    CallMethodResult sessionless =
        disableNode
            .getInvocationHandler()
            .invoke(AccessContext.INTERNAL, request(statusPath, disableNode.getNodeId()));
    assertStatus(StatusCodes.Bad_UserAccessDenied, sessionless);

    // no RoleMapper: allowed — Bad_InvalidState proves the authorization check passed and the
    // state rule was reached (Disable on Disabled)
    assertStatus(StatusCodes.Bad_InvalidState, invokeDisable(statusPath, sessionWithRoles(null)));

    // RoleMapper granting ConfigureAdmin: allowed, state rule reached
    assertStatus(
        StatusCodes.Bad_InvalidState,
        invokeDisable(statusPath, sessionWithRoles(List.of(NodeIds.WellKnownRole_ConfigureAdmin))));

    // RoleMapper without ConfigureAdmin: denied before the state rule
    assertStatus(
        StatusCodes.Bad_UserAccessDenied,
        invokeDisable(statusPath, sessionWithRoles(List.of(UNRELATED_ROLE))));
  }

  @Test
  void customAuthorizerStatusIsSurfacedVerbatim() throws Exception {
    var authorizer =
        new PubSubMethodAuthorizer() {
          @Override
          public StatusCode checkConfigure(Session session) {
            return new StatusCode(StatusCodes.Bad_ConfigurationError);
          }

          @Override
          public StatusCode checkSksAdmin(Session session) {
            return StatusCode.GOOD;
          }

          @Override
          public StatusCode checkKeyAccess(Session session, String securityGroupId) {
            return StatusCode.GOOD;
          }
        };

    Ports ports = Ports.pick();
    attachAndStart(
        ports,
        ServerPubSubOptions.builder()
            .exposeInformationModel(true)
            .allowRemoteConfiguration(true)
            .methodAuthorizer(authorizer)
            .build(),
        true);

    assertStatus(
        StatusCodes.Bad_ConfigurationError,
        invokeEnable("PubSub/" + CONN_OFF, sessionWithRoles(null)));
  }

  @Test
  void methodNodesAreAbsentUnlessRemoteConfigurationIsAllowed() throws Exception {
    Ports ports = Ports.pick();
    // default options: exposeInformationModel(true), allowRemoteConfiguration DEFAULT FALSE
    attachAndStart(ports, ServerPubSubOptions.builder().exposeInformationModel(true).build(), true);

    // Status/State exists, but no Enable/Disable methods were minted
    assertTrue(managedNode(fragmentNodeId("PubSub/" + CONN + "/Status/State")).isPresent());
    assertTrue(managedNode(fragmentNodeId("PubSub/" + CONN + "/Status/Enable")).isEmpty());
    assertTrue(managedNode(fragmentNodeId("PubSub/" + CONN + "/Status/Disable")).isEmpty());
    assertTrue(
        managedNode(fragmentNodeId("PubSub/" + CONN + "/" + WRITER_GROUP + "/Status/Enable"))
            .isEmpty());
  }

  @Test
  void rootStatusPairIsNeverMinted() throws Exception {
    Ports ports = Ports.pick();
    attachAndStart(ports, remoteConfigOptions(), true);

    // the ns0 root Status Enable/Disable (i=17407/i=17408) are Optional members the
    // loader omits, and the fragment must not mint them (Call dispatch by objectId routes to
    // the ns0 namespace, which cannot see fragment-held method nodes)
    assertTrue(managedNode(NodeIds.PublishSubscribe_Status).isPresent());
    assertTrue(managedNode(NodeIds.PublishSubscribe_Status_State).isPresent());
    assertTrue(managedNode(NodeIds.PublishSubscribe_Status_Enable).isEmpty());
    assertTrue(managedNode(NodeIds.PublishSubscribe_Status_Disable).isEmpty());

    // per-component pairs exist instead
    assertTrue(managedNode(fragmentNodeId("PubSub/" + CONN + "/Status/Enable")).isPresent());
    assertTrue(managedNode(fragmentNodeId("PubSub/" + CONN + "/Status/Disable")).isPresent());
  }

  @Test
  void handlersSurviveRestartsAndRemovedComponentsAnswerNotFound() throws Exception {
    Ports ports = Ports.pick();
    ServerPubSub serverPubSub = attachAndStart(ports, remoteConfigOptions(), true);

    String groupPath = "PubSub/" + CONN + "/" + WRITER_GROUP;
    awaitState(groupPath, PubSubState.Operational);

    // hold the reader's Enable node from before the reconfiguration
    String readerPath = "PubSub/" + CONN + "/" + READER_GROUP + "/" + READER;
    UaMethodNode readerEnableNode = methodNode(readerPath + "/Status/Enable");

    // restart the writer group (publishing interval change) and drop the reader
    serverPubSub.runtime().update(current -> config(ports, 150, false));

    Session session = sessionWithRoles(null);

    // the rebuilt Status object carries a fresh handler on the SAME NodeId, resolving the
    // component's new handle
    awaitState(groupPath, PubSubState.Operational);
    assertStatus(null, invokeDisable(groupPath, session));
    awaitState(groupPath, PubSubState.Disabled);
    assertStatus(null, invokeEnable(groupPath, session));

    // the removed reader's node is gone; its stale handler answers Bad_NotFound
    assertTrue(managedNode(fragmentNodeId(readerPath)).isEmpty());
    CallMethodResult stale =
        readerEnableNode
            .getInvocationHandler()
            .invoke(() -> Optional.of(session), request(readerPath, readerEnableNode.getNodeId()));
    assertStatus(StatusCodes.Bad_NotFound, stale);
  }

  // region fixtures

  private record Ports(int dataPort1, int discoveryPort1, int dataPort2, int discoveryPort2) {
    static Ports pick() throws SocketException {
      return new Ports(freeUdpPort(), freeUdpPort(), freeUdpPort(), freeUdpPort());
    }
  }

  private static ServerPubSubOptions remoteConfigOptions() {
    return ServerPubSubOptions.builder()
        .exposeInformationModel(true)
        .allowRemoteConfiguration(true)
        .build();
  }

  private ServerPubSub attachAndStart(Ports ports, ServerPubSubOptions options, boolean withReader)
      throws Exception {

    ServerPubSub serverPubSub =
        ServerPubSub.attach(testServer.getServer(), config(ports, 100, withReader), options);
    attached.add(serverPubSub);

    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    return serverPubSub;
  }

  /**
   * Two connections: {@code sm-conn} enabled (writer group + writer publishing a node-backed
   * dataset, reader group + reader filtering an external publisher that never publishes — so the
   * reader parks in PreOperational), and {@code sm-conn-off} disabled with an enabled writer group
   * ({@code wgrp2} parks in Paused).
   */
  private static PubSubConfig config(
      Ports ports, long publishingIntervalMillis, boolean withReader) {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(DATA_SET)
            .field(
                FieldDefinition.builder("value")
                    .source(nodeAddress(sourceNodeId))
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(new UUID(0L, 0xE7L))
                    .build())
            .build();

    ReaderGroupConfig.Builder readerGroup = ReaderGroupConfig.builder(READER_GROUP);
    if (withReader) {
      TargetVariablesConfig targetVariables =
          TargetVariablesConfig.builder()
              .map(
                  FieldSelector.byName("v"),
                  TargetVariableConfig.builder().target(nodeAddress(targetNodeId)).build())
              .build();

      readerGroup.dataSetReader(
          DataSetReaderConfig.builder(READER)
              .publisherId(EXTERNAL_PUBLISHER_ID)
              .writerGroupId(EXTERNAL_GROUP_ID)
              .dataSetWriterId(EXTERNAL_WRITER_ID)
              .dataSetMetaData(
                  DataSetMetaDataConfig.builder("sm-ext-ds")
                      .field("v", NodeIds.Double, new UUID(0L, 0xE8L))
                      .build())
              .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
              .subscribedDataSet(targetVariables)
              .build());
    }

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp(CONN)
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", ports.dataPort1()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", ports.discoveryPort1()))
                .writerGroup(
                    WriterGroupConfig.builder(WRITER_GROUP)
                        .writerGroupId(ushort(21))
                        .publishingInterval(Duration.ofMillis(publishingIntervalMillis))
                        .messageSettings(GROUP_SETTINGS)
                        .dataSetWriter(
                            DataSetWriterConfig.builder(WRITER)
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(31))
                                .fieldContentMask(FIELD_MASK)
                                .settings(WRITER_SETTINGS)
                                .build())
                        .build())
                .readerGroup(readerGroup.build())
                .build())
        .connection(
            PubSubConnectionConfig.udp(CONN_OFF)
                .enabled(false)
                .publisherId(PUBLISHER_ID_OFF)
                .address(UdpDatagramAddress.unicast("127.0.0.1", ports.dataPort2()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", ports.discoveryPort2()))
                .writerGroup(
                    WriterGroupConfig.builder(WRITER_GROUP2)
                        .writerGroupId(ushort(22))
                        .publishingInterval(Duration.ofMillis(200))
                        .messageSettings(GROUP_SETTINGS)
                        .build())
                .build())
        .build();
  }

  private static NodeFieldAddress nodeAddress(NodeId nodeId) {
    return NodeFieldAddress.of(
        nodeId, AttributeId.Value, testServer.getServer().getNamespaceTable());
  }

  // endregion

  // region helpers

  private static CallMethodResult invokeEnable(String componentIdentifier, Session session) {
    return invoke(componentIdentifier, "Enable", session);
  }

  private static CallMethodResult invokeDisable(String componentIdentifier, Session session) {
    return invoke(componentIdentifier, "Disable", session);
  }

  private static CallMethodResult invoke(
      String componentIdentifier, String methodName, Session session) {

    UaMethodNode node = methodNode(componentIdentifier + "/Status/" + methodName);

    return node.getInvocationHandler()
        .invoke(
            () -> Optional.of(session), request(componentIdentifier + "/Status", node.getNodeId()));
  }

  private static CallMethodRequest request(String objectIdentifier, NodeId methodId) {
    return new CallMethodRequest(fragmentNodeId(objectIdentifier), methodId, new Variant[0]);
  }

  /** Assert the result status; {@code null} expects Good. */
  private static void assertStatus(@Nullable Long expected, CallMethodResult result) {
    StatusCode expectedCode = expected != null ? new StatusCode(expected) : StatusCode.GOOD;
    assertEquals(expectedCode, result.getStatusCode());
  }

  private static UaMethodNode methodNode(String identifier) {
    return (UaMethodNode)
        managedNode(fragmentNodeId(identifier))
            .orElseThrow(() -> new AssertionError("missing method node: " + identifier));
  }

  private static java.util.Optional<UaNode> managedNode(NodeId nodeId) {
    return testServer.getServer().getAddressSpaceManager().getManagedNode(nodeId);
  }

  private static NodeId fragmentNodeId(String identifier) {
    return new NodeId(testServer.getServer().getServerNamespace().getNamespaceIndex(), identifier);
  }

  /** A mocked {@link Session}; {@code roleIds} null means "no RoleMapper configured". */
  private static Session sessionWithRoles(@Nullable List<NodeId> roleIds) {
    Session session = mock(Session.class);
    when(session.getRoleIds()).thenReturn(Optional.ofNullable(roleIds));
    return session;
  }

  /** Poll the State variable under {@code componentIdentifier}/Status until it equals state. */
  private static void awaitState(String componentIdentifier, PubSubState state)
      throws InterruptedException {

    NodeId nodeId = fragmentNodeId(componentIdentifier + "/Status/State");

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    Object lastValue = null;
    while (true) {
      UaNode node = managedNode(nodeId).orElse(null);
      if (node instanceof UaVariableNode variableNode) {
        lastValue = variableNode.getValue().value().value();
        if (state.equals(lastValue)) {
          return;
        }
      }
      if (System.nanoTime() >= deadline) {
        fail(
            "timed out waiting for State == "
                + state
                + " on "
                + componentIdentifier
                + "; last value: "
                + lastValue);
      }
      Thread.sleep(25);
    }
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
