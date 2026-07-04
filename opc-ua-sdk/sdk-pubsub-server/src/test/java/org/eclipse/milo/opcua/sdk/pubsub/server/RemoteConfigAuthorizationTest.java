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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigFiles;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The R9/D41 authorization posture on the remote-configuration surface: {@code checkConfigure} is
 * consulted by ALL EIGHT file-model handlers (including the read side), a session-less invocation
 * answers {@code Bad_UserAccessDenied} (D29), the default authorizer's RoleMapper matrix
 * (ConfigureAdmin-or-equivalent), custom authorizer codes surfaced verbatim, and the R7 bit-11
 * rule: a {@code checkSksAdmin} denial fails each SecurityGroup reference per-element while the
 * method stays Good.
 */
class RemoteConfigAuthorizationTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final NodeId UNRELATED_ROLE = new NodeId(1, "unrelated-role");

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

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
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
  void sessionLessInvocationsAreUserAccessDeniedOnAllEightHandlers() throws Exception {
    attachAndStart(null);

    for (NodeId methodNodeId : METHOD_NODE_IDS) {
      UaMethodNode node = methodNode(methodNodeId);
      CallMethodResult result =
          node.getInvocationHandler()
              .invoke(AccessContext.INTERNAL, request(node, wellShapedInputs(methodNodeId)));
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          result.getStatusCode(),
          methodNodeId.toString());
    }
  }

  @Test
  void checkConfigureGatesAllEightHandlersIncludingTheReadSide() throws Exception {
    // a custom authorizer's code is surfaced VERBATIM on every handler (these calls are
    // well-shaped, so the argument checks pass and authorization is what answers)
    var authorizer = denyingConfigureAuthorizer();

    attachAndStart(authorizer);
    Session session = sessionWithRoles(null);

    for (NodeId methodNodeId : METHOD_NODE_IDS) {
      UaMethodNode node = methodNode(methodNodeId);
      CallMethodResult result =
          node.getInvocationHandler()
              .invoke(() -> Optional.of(session), request(node, wellShapedInputs(methodNodeId)));
      assertEquals(
          new StatusCode(StatusCodes.Bad_ConfigurationError),
          result.getStatusCode(),
          methodNodeId.toString());
    }
  }

  @Test
  void argumentValidationRunsBeforeAuthorizationOnAllEightHandlers() throws Exception {
    // S13 check order: session -> args -> authorization. A caller the authorizer denies who
    // also sends null required arguments answers Bad_InvalidArgument, not the authorizer's code
    // (null Variant values pass the base class's type checks and reach the typed invoke).
    var authorizer = denyingConfigureAuthorizer();

    attachAndStart(authorizer);
    Session session = sessionWithRoles(null);

    for (NodeId methodNodeId : METHOD_NODE_IDS) {
      UaMethodNode node = methodNode(methodNodeId);
      Variant[] nullInputs = new Variant[wellShapedInputs(methodNodeId).length];
      Arrays.fill(nullInputs, Variant.NULL_VALUE);
      CallMethodResult result =
          node.getInvocationHandler().invoke(() -> Optional.of(session), request(node, nullInputs));
      assertEquals(
          new StatusCode(StatusCodes.Bad_InvalidArgument),
          result.getStatusCode(),
          methodNodeId.toString());
    }
  }

  /** An authorizer denying {@code checkConfigure} with a distinctive, verbatim-surfaced code. */
  private static PubSubMethodAuthorizer denyingConfigureAuthorizer() {
    return new PubSubMethodAuthorizer() {
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
  }

  @Test
  void defaultAuthorizerAppliesTheRoleMapperMatrix() throws Exception {
    attachAndStart(null);

    // no RoleMapper: allowed — Open succeeds (handles are session-scoped: the same session
    // must close what it opened)
    Session noMapperSession = sessionWithRoles(null);
    CallMethodResult noMapper = invokeOpen(noMapperSession);
    assertEquals(StatusCode.GOOD, noMapper.getStatusCode());
    closeHandle(noMapperSession, noMapper);

    // RoleMapper granting ConfigureAdmin: allowed
    Session admin = sessionWithRoles(List.of(NodeIds.WellKnownRole_ConfigureAdmin));
    CallMethodResult adminResult = invokeOpen(admin);
    assertEquals(StatusCode.GOOD, adminResult.getStatusCode());
    closeHandle(admin, adminResult);

    // RoleMapper without ConfigureAdmin: denied — the whole surface is configure-gated,
    // including the read-side Open(0x01)
    CallMethodResult denied = invokeOpen(sessionWithRoles(List.of(UNRELATED_ROLE)));
    assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), denied.getStatusCode());
  }

  @Test
  void sksAdminDenialFailsSecurityGroupRefsPerElementWhileTheMethodStaysGood() throws Exception {
    var authorizer =
        new PubSubMethodAuthorizer() {
          @Override
          public StatusCode checkConfigure(Session session) {
            return StatusCode.GOOD;
          }

          @Override
          public StatusCode checkSksAdmin(Session session) {
            return new StatusCode(StatusCodes.Bad_UserAccessDenied);
          }

          @Override
          public StatusCode checkKeyAccess(Session session, String securityGroupId) {
            return StatusCode.GOOD;
          }
        };

    ServerPubSub serverPubSub = attachAndStart(authorizer);
    Session session = sessionWithRoles(null);

    // upload a file carrying a SecurityGroup element and reference it with bit 11 (Add),
    // alongside a bit-8 connection Add that must proceed
    CallMethodResult open = invokeOpen0x06(session);
    UInteger handle = (UInteger) open.getOutputArguments()[0].getValue();

    PubSubConfig fileConfig =
        PubSubConfig.builder()
            .securityGroup(
                SecurityGroupConfig.builder("auth-sg").keyLifeTime(Duration.ofHours(1)).build())
            .connection(
                PubSubConnectionConfig.udp("auth-conn2")
                    .publisherId(PublisherId.uint16(ushort(4899)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 15899))
                    .build())
            .build();
    byte[] fileBytes =
        PubSubConfigFiles.encodeDataType(
            fileConfig.toDataType(testServer.getServer().getNamespaceTable()),
            testServer.getServer().getStaticEncodingContext());

    UaMethodNode writeNode = methodNode(NodeIds.PublishSubscribe_PubSubConfiguration_Write);
    CallMethodResult write =
        writeNode
            .getInvocationHandler()
            .invoke(
                () -> Optional.of(session),
                request(
                    writeNode,
                    new Variant[] {new Variant(handle), new Variant(ByteString.of(fileBytes))}));
    assertEquals(StatusCode.GOOD, write.getStatusCode());

    var sgRef =
        new PubSubConfigurationRefDataType(
            PubSubConfigurationRefMask.of(
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceSecurityGroup),
            ushort(0),
            ushort(0),
            ushort(0));
    var connectionRef =
        new PubSubConfigurationRefDataType(
            PubSubConfigurationRefMask.of(
                PubSubConfigurationRefMask.Field.ElementAdd,
                PubSubConfigurationRefMask.Field.ReferenceConnection),
            ushort(0),
            ushort(0),
            ushort(0));

    UaMethodNode closeAndUpdateNode =
        methodNode(NodeIds.PublishSubscribe_PubSubConfiguration_CloseAndUpdate);
    CallMethodResult result =
        closeAndUpdateNode
            .getInvocationHandler()
            .invoke(
                () -> Optional.of(session),
                request(
                    closeAndUpdateNode,
                    new Variant[] {
                      new Variant(handle),
                      new Variant(false),
                      Variant.ofExtensionObjectArray(
                          new ExtensionObject[] {
                            ExtensionObject.encode(
                                testServer.getServer().getStaticEncodingContext(), sgRef),
                            ExtensionObject.encode(
                                testServer.getServer().getStaticEncodingContext(), connectionRef)
                          })
                    }));

    // method-level Good; the bit-11 ref failed per-element, the connection add applied
    assertEquals(StatusCode.GOOD, result.getStatusCode());
    StatusCode[] referencesResults = (StatusCode[]) result.getOutputArguments()[1].getValue();
    assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), referencesResults[0]);
    assertEquals(StatusCode.GOOD, referencesResults[1]);
    assertEquals(true, result.getOutputArguments()[0].getValue());
    assertTrue(serverPubSub.runtime().components().connection("auth-conn2").isPresent());
  }

  // region helpers

  private ServerPubSub attachAndStart(@Nullable PubSubMethodAuthorizer authorizer)
      throws Exception {

    ServerPubSubOptions.Builder options =
        ServerPubSubOptions.builder().exposeInformationModel(true).allowRemoteConfiguration(true);
    if (authorizer != null) {
      options.methodAuthorizer(authorizer);
    }

    ServerPubSub serverPubSub =
        ServerPubSub.attach(testServer.getServer(), config(freeUdpPort()), options.build());
    attached.add(serverPubSub);
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    return serverPubSub;
  }

  private static PubSubConfig config(int port) {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("auth-conn")
                .publisherId(PublisherId.uint16(ushort(4890)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .build())
        .build();
  }

  private static CallMethodResult invokeOpen(Session session) {
    UaMethodNode node = methodNode(NodeIds.PublishSubscribe_PubSubConfiguration_Open);
    return node.getInvocationHandler()
        .invoke(() -> Optional.of(session), request(node, new Variant[] {new Variant(ubyte(1))}));
  }

  private static CallMethodResult invokeOpen0x06(Session session) {
    UaMethodNode node = methodNode(NodeIds.PublishSubscribe_PubSubConfiguration_Open);
    CallMethodResult result =
        node.getInvocationHandler()
            .invoke(
                () -> Optional.of(session), request(node, new Variant[] {new Variant(ubyte(6))}));
    assertEquals(StatusCode.GOOD, result.getStatusCode());
    return result;
  }

  private static void closeHandle(Session session, CallMethodResult openResult) {
    UInteger handle = (UInteger) openResult.getOutputArguments()[0].getValue();
    UaMethodNode node = methodNode(NodeIds.PublishSubscribe_PubSubConfiguration_Close);
    CallMethodResult result =
        node.getInvocationHandler()
            .invoke(() -> Optional.of(session), request(node, new Variant[] {new Variant(handle)}));
    assertEquals(StatusCode.GOOD, result.getStatusCode());
  }

  private static CallMethodRequest request(UaMethodNode node, Variant[] inputs) {
    return new CallMethodRequest(
        NodeIds.PublishSubscribe_PubSubConfiguration, node.getNodeId(), inputs);
  }

  /**
   * Correctly-typed inputs per method, so the base class's argument checks pass and the handler's
   * session/authorization prefix is what answers.
   */
  private static Variant[] wellShapedInputs(NodeId methodNodeId) {
    if (NodeIds.PublishSubscribe_PubSubConfiguration_Open.equals(methodNodeId)) {
      return new Variant[] {new Variant(ubyte(1))};
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_Close.equals(methodNodeId)) {
      return new Variant[] {new Variant(UInteger.valueOf(1))};
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_Read.equals(methodNodeId)) {
      return new Variant[] {new Variant(UInteger.valueOf(1)), new Variant(16)};
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_Write.equals(methodNodeId)) {
      return new Variant[] {
        new Variant(UInteger.valueOf(1)), new Variant(ByteString.of(new byte[] {1}))
      };
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_GetPosition.equals(methodNodeId)) {
      return new Variant[] {new Variant(UInteger.valueOf(1))};
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_SetPosition.equals(methodNodeId)) {
      return new Variant[] {
        new Variant(UInteger.valueOf(1)),
        new Variant(org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong.valueOf(0))
      };
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_ReserveIds.equals(methodNodeId)) {
      return new Variant[] {
        new Variant(PubSubIdReservations.PROFILE_UDP_UADP),
        new Variant(ushort(1)),
        new Variant(ushort(1))
      };
    } else {
      return new Variant[] {
        new Variant(UInteger.valueOf(1)), new Variant(true), Variant.NULL_VALUE
      };
    }
  }

  private static UaMethodNode methodNode(NodeId nodeId) {
    return (UaMethodNode)
        testServer
            .getServer()
            .getAddressSpaceManager()
            .getManagedNode(nodeId)
            .orElseThrow(() -> new AssertionError("missing method node: " + nodeId));
  }

  /** A mocked {@link Session}; {@code roleIds} null means "no RoleMapper configured". */
  private static Session sessionWithRoles(@Nullable List<NodeId> roleIds) {
    Session session = mock(Session.class);
    when(session.getRoleIds()).thenReturn(Optional.ofNullable(roleIds));
    when(session.getSessionId()).thenReturn(new NodeId(1, "auth-session-" + UUID.randomUUID()));
    return session;
  }

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
