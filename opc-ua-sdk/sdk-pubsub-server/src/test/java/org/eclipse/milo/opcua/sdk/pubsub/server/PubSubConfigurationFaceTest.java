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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigFiles;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The remote-configuration helper against a real server and runtime: attach/startup with {@code
 * allowRemoteConfiguration} (no throw), handler attachment and shutdown restore, the three optional
 * properties created in ns0's node manager and readable through the composite (a real Read — trap
 * 1: in-process {@code getManagedNode} lookups would deceive), the mandatory property values, the
 * untouched-ns0 contract when the flag is off, the full
 * Open&rarr;Read&rarr;Write&rarr;CloseAndUpdate flow with wire-shaped Variants mutating a real
 * {@link org.eclipse.milo.opcua.sdk.pubsub.PubSubService}, the {@code Bad_NothingToDo}
 * handle-closure rule, ReserveIds output typing, and session-close eviction.
 */
class PubSubConfigurationFaceTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final String CONN = "rc-conn";
  private static final String WRITER_GROUP = "rc-wg";
  private static final String WRITER = "rc-dsw";
  private static final String DATA_SET = "rc-ds";

  private static final List<NodeId> METHOD_NODE_IDS =
      List.of(
          NodeIds.PublishSubscribe_PubSubConfiguration_Open,
          NodeIds.PublishSubscribe_PubSubConfiguration_Close,
          NodeIds.PublishSubscribe_PubSubConfiguration_Read,
          NodeIds.PublishSubscribe_PubSubConfiguration_Write,
          NodeIds.PublishSubscribe_PubSubConfiguration_GetPosition,
          NodeIds.PublishSubscribe_PubSubConfiguration_SetPosition,
          NodeIds.PublishSubscribe_PubSubConfiguration_ReserveIds,
          NodeIds.PublishSubscribe_PubSubConfiguration_CloseAndUpdate);

  private static TestPubSubServer testServer;
  private static NodeId sourceNodeId;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
    sourceNodeId =
        testServer.addVariable("RC_Source", NodeIds.Double, new DataValue(Variant.ofDouble(1.0)));
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
  void faceAttachesHandlersCreatesPropertiesAndRestoresOnShutdown() throws Exception {
    ServerPubSub serverPubSub = attachAndStart();

    for (NodeId methodNodeId : METHOD_NODE_IDS) {
      UaMethodNode node = methodNode(methodNodeId);
      assertFalse(
          node.getInvocationHandler() == MethodInvocationHandler.NOT_IMPLEMENTED,
          "no handler attached: " + methodNodeId);
    }

    // the three optional properties are readable through the COMPOSITE (real service routing);
    // fragment-hosted ns0-id nodes would answer Bad_NodeIdUnknown here
    List<DataValue> values =
        readValues(
            NodeIds.PublishSubscribe_PubSubConfiguration_MimeType,
            NodeIds.PublishSubscribe_PubSubConfiguration_MaxByteStringLength,
            NodeIds.PublishSubscribe_PubSubConfiguration_LastModifiedTime);

    assertEquals(PubSubConfigFiles.MIME_TYPE, values.get(0).value().value());
    assertEquals(
        testServer.getServer().getConfig().getLimits().getMaxByteStringLength(),
        values.get(1).value().value());
    assertInstanceOf(DateTime.class, values.get(2).value().value());

    // the four mandatory property values are initialized on the loader-built nodes
    ULong size =
        assertInstanceOf(ULong.class, ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_Size));
    assertTrue(size.longValue() > 0, "Size serves the real encoded length");
    assertEquals(true, ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_Writable));
    assertEquals(true, ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_UserWritable));
    assertEquals(ushort(0), ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));

    serverPubSub.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // handlers restored, created properties removed (absent through the composite), values null
    for (NodeId methodNodeId : METHOD_NODE_IDS) {
      assertSame(
          MethodInvocationHandler.NOT_IMPLEMENTED, methodNode(methodNodeId).getInvocationHandler());
    }
    List<DataValue> afterShutdown =
        readValues(NodeIds.PublishSubscribe_PubSubConfiguration_MimeType);
    assertEquals(new StatusCode(StatusCodes.Bad_NodeIdUnknown), afterShutdown.get(0).statusCode());
    assertNull(ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_Size));
    assertNull(ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));
  }

  @Test
  void disabledFlagLeavesNs0Untouched() throws Exception {
    // allowRemoteConfiguration defaults to FALSE: no helper, loader defaults everywhere
    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(), config(15877, 15879), ServerPubSubOptions.builder().build());
    attached.add(serverPubSub);

    for (NodeId methodNodeId : METHOD_NODE_IDS) {
      assertSame(
          MethodInvocationHandler.NOT_IMPLEMENTED, methodNode(methodNodeId).getInvocationHandler());
    }

    // methods answer Bad_NotImplemented; property values stay loader-null (never false)
    CallMethodResult result =
        invoke(
            NodeIds.PublishSubscribe_PubSubConfiguration_Open,
            session(),
            new Variant[] {new Variant(ubyte(1))});
    assertEquals(new StatusCode(StatusCodes.Bad_NotImplemented), result.getStatusCode());

    assertNull(ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_Writable));
    assertNull(ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_UserWritable));

    List<DataValue> optional = readValues(NodeIds.PublishSubscribe_PubSubConfiguration_MimeType);
    assertEquals(new StatusCode(StatusCodes.Bad_NodeIdUnknown), optional.get(0).statusCode());
  }

  @Test
  void fullFileFlowReadsAndMutatesTheRuntime() throws Exception {
    ServerPubSub serverPubSub = attachAndStart();
    Session session = session();

    // Open(0x01) and read the whole file in chunks; decode and verify it reflects the runtime
    UInteger readHandle = openHandle(session, 0x01);
    assertEquals(ushort(1), ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));

    var content = new ByteArrayOutputStream();
    while (true) {
      CallMethodResult read =
          invoke(
              NodeIds.PublishSubscribe_PubSubConfiguration_Read,
              session,
              new Variant[] {new Variant(readHandle), new Variant(64)});
      assertGood(read);
      ByteString chunk = (ByteString) read.getOutputArguments()[0].getValue();
      if (chunk == null || chunk.length() == 0) {
        break;
      }
      content.writeBytes(chunk.bytesOrEmpty());
    }

    PubSubConfiguration2DataType snapshot =
        PubSubConfigFiles.decodeDataType(
            content.toByteArray(), testServer.getServer().getStaticEncodingContext());
    assertEquals(CONN, snapshot.getConnections()[0].getName());
    assertTrue(snapshot.getConfigurationVersion().longValue() != 0, "the version is patched in");

    assertGood(
        invoke(
            NodeIds.PublishSubscribe_PubSubConfiguration_Close,
            session,
            new Variant[] {new Variant(readHandle)}));

    // Open(0x06), write a file adding a writer group under the live connection, CloseAndUpdate
    UInteger writeHandle = openHandle(session, 0x06);

    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp(CONN)
                    .publisherId(PublisherId.uint16(ushort(4881)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 15878))
                    .writerGroup(
                        WriterGroupConfig.builder("rc-wg2")
                            .writerGroupId(ushort(31))
                            .publishingInterval(Duration.ofMillis(500))
                            .build())
                    .build())
            .build();
    byte[] fileBytes =
        PubSubConfigFiles.encodeDataType(
            fileConfig.toDataType(testServer.getServer().getNamespaceTable()),
            testServer.getServer().getStaticEncodingContext());

    assertGood(
        invoke(
            NodeIds.PublishSubscribe_PubSubConfiguration_Write,
            session,
            new Variant[] {new Variant(writeHandle), new Variant(ByteString.of(fileBytes))}));

    // GetPosition/SetPosition are usable mid-flow (the normative read-modify-write sequence)
    CallMethodResult position =
        invoke(
            NodeIds.PublishSubscribe_PubSubConfiguration_GetPosition,
            session,
            new Variant[] {new Variant(writeHandle)});
    assertGood(position);
    assertEquals(ULong.valueOf(fileBytes.length), position.getOutputArguments()[0].getValue());

    // wire-shaped input: the ConfigurationReferences arrive as ExtensionObject[] and the
    // base-class fix decodes and substitutes them
    var addGroupRef =
        new PubSubConfigurationRefDataType(
            PubSubConfigurationRefMask.of(
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceWriterGroup),
            ushort(0),
            ushort(0),
            ushort(0));

    CallMethodResult closeAndUpdate =
        invoke(
            NodeIds.PublishSubscribe_PubSubConfiguration_CloseAndUpdate,
            session,
            new Variant[] {
              new Variant(writeHandle),
              new Variant(true),
              Variant.ofExtensionObjectArray(
                  new ExtensionObject[] {
                    ExtensionObject.encode(
                        testServer.getServer().getStaticEncodingContext(), addGroupRef)
                  })
            });

    assertGood(closeAndUpdate);
    assertEquals(true, closeAndUpdate.getOutputArguments()[0].getValue());
    StatusCode[] referencesResults =
        (StatusCode[]) closeAndUpdate.getOutputArguments()[1].getValue();
    assertEquals(1, referencesResults.length);
    assertEquals(StatusCode.GOOD, referencesResults[0]);

    // the runtime was mutated through the mediator; the handle is closed
    assertTrue(serverPubSub.runtime().components().writerGroup(CONN, "rc-wg2").isPresent());
    assertEquals(ushort(0), ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));

    // the fragment rebuilt synchronously: ConfigurationObjects resolves the new group's node
    NodeId[] configurationObjects = (NodeId[]) closeAndUpdate.getOutputArguments()[3].getValue();
    assertEquals(1, configurationObjects.length);
    assertFalse(configurationObjects[0].isNull());
  }

  @Test
  void nothingToDoStillClosesTheHandle() throws Exception {
    attachAndStart();
    Session session = session();

    UInteger handle = openHandle(session, 0x06);
    assertEquals(ushort(1), ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));

    CallMethodResult result =
        invoke(
            NodeIds.PublishSubscribe_PubSubConfiguration_CloseAndUpdate,
            session,
            new Variant[] {new Variant(handle), new Variant(true), Variant.NULL_VALUE});

    assertEquals(new StatusCode(StatusCodes.Bad_NothingToDo), result.getStatusCode());
    // the handle closed anyway; a subsequent Close answers Bad_InvalidArgument
    assertEquals(ushort(0), ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));
    CallMethodResult close =
        invoke(
            NodeIds.PublishSubscribe_PubSubConfiguration_Close,
            session,
            new Variant[] {new Variant(handle)});
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), close.getStatusCode());
  }

  @Test
  void reserveIdsAnswersTypedOutputsAndSessionCloseEvicts() throws Exception {
    ServerPubSub serverPubSub = attachAndStart();
    Session session = session();

    CallMethodResult reserve =
        invoke(
            NodeIds.PublishSubscribe_PubSubConfiguration_ReserveIds,
            session,
            new Variant[] {
              new Variant(PubSubIdReservations.PROFILE_UDP_UADP),
              new Variant(ushort(2)),
              new Variant(ushort(1))
            });

    assertGood(reserve);
    assertInstanceOf(ULong.class, reserve.getOutputArguments()[0].getValue());
    UShort[] writerGroupIds = (UShort[]) reserve.getOutputArguments()[1].getValue();
    UShort[] dataSetWriterIds = (UShort[]) reserve.getOutputArguments()[2].getValue();
    assertEquals(2, writerGroupIds.length);
    assertEquals(1, dataSetWriterIds.length);

    // the JSON profile answers a decimal String DefaultPublisherId
    CallMethodResult json =
        invoke(
            NodeIds.PublishSubscribe_PubSubConfiguration_ReserveIds,
            session,
            new Variant[] {
              new Variant(PubSubIdReservations.PROFILE_MQTT_JSON),
              new Variant(ushort(0)),
              new Variant(ushort(0))
            });
    assertGood(json);
    assertInstanceOf(String.class, json.getOutputArguments()[0].getValue());

    // session-close eviction: open handles are released and reservations dropped
    UInteger handle = openHandle(session, 0x01);
    assertEquals(ushort(1), ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));

    PubSubConfigurationFace face = serverPubSub.configurationFace();
    assertNotNull(face);
    face.evictSession(session);

    assertEquals(ushort(0), ns0Value(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount));
    CallMethodResult read =
        invoke(
            NodeIds.PublishSubscribe_PubSubConfiguration_Read,
            session,
            new Variant[] {new Variant(handle), new Variant(16)});
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), read.getStatusCode());
  }

  // region helpers

  private ServerPubSub attachAndStart() throws Exception {
    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            config(freeUdpPort(), freeUdpPort()),
            ServerPubSubOptions.builder()
                .exposeInformationModel(true)
                .allowRemoteConfiguration(true)
                .build());
    attached.add(serverPubSub);
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return serverPubSub;
  }

  private static PubSubConfig config(int dataPort, int discoveryPort) {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(DATA_SET)
            .field(
                FieldDefinition.builder("value")
                    .source(
                        NodeFieldAddress.of(
                            sourceNodeId,
                            AttributeId.Value,
                            testServer.getServer().getNamespaceTable()))
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(new UUID(0L, 0xD1L))
                    .build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp(CONN)
                .publisherId(PublisherId.uint16(ushort(4881)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", dataPort))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", discoveryPort))
                .writerGroup(
                    WriterGroupConfig.builder(WRITER_GROUP)
                        .writerGroupId(ushort(21))
                        .publishingInterval(Duration.ofMillis(200))
                        .dataSetWriter(
                            org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig.builder(
                                    WRITER)
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(41))
                                .fieldContentMask(
                                    DataSetFieldContentMask.of(
                                        DataSetFieldContentMask.Field.StatusCode))
                                .build())
                        .build())
                .build())
        .build();
  }

  private static UInteger openHandle(Session session, int mode) {
    CallMethodResult open =
        invoke(
            NodeIds.PublishSubscribe_PubSubConfiguration_Open,
            session,
            new Variant[] {new Variant(ubyte(mode))});
    assertGood(open);
    return (UInteger) open.getOutputArguments()[0].getValue();
  }

  private static CallMethodResult invoke(NodeId methodNodeId, Session session, Variant[] inputs) {
    UaMethodNode node = methodNode(methodNodeId);
    var request =
        new CallMethodRequest(
            NodeIds.PublishSubscribe_PubSubConfiguration, node.getNodeId(), inputs);
    return node.getInvocationHandler().invoke(() -> Optional.of(session), request);
  }

  private static void assertGood(CallMethodResult result) {
    assertEquals(StatusCode.GOOD, result.getStatusCode(), () -> String.valueOf(result));
  }

  private static UaMethodNode methodNode(NodeId nodeId) {
    return (UaMethodNode)
        testServer
            .getServer()
            .getAddressSpaceManager()
            .getManagedNode(nodeId)
            .orElseThrow(() -> new AssertionError("missing method node: " + nodeId));
  }

  private static @Nullable Object ns0Value(NodeId nodeId) {
    UaNode node =
        testServer.getServer().getAddressSpaceManager().getManagedNode(nodeId).orElseThrow();
    return ((UaVariableNode) node).getValue().value().value();
  }

  /** A real Read through the AddressSpaceComposite: routing decides, not in-process lookups. */
  private static List<DataValue> readValues(NodeId... nodeIds) {
    var readContext = new ReadContext(testServer.getServer(), null);
    List<ReadValueId> readValueIds =
        List.of(nodeIds).stream()
            .map(id -> new ReadValueId(id, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE))
            .toList();
    return testServer
        .getServer()
        .getAddressSpaceManager()
        .read(readContext, 0.0, TimestampsToReturn.Both, readValueIds);
  }

  /** A mocked Session: no RoleMapper (allow) and a stable session id. */
  private static Session session() {
    Session session = mock(Session.class);
    when(session.getRoleIds()).thenReturn(Optional.empty());
    when(session.getSessionId())
        .thenReturn(new NodeId(1, "face-test-session-" + UUID.randomUUID()));
    return session;
  }

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
