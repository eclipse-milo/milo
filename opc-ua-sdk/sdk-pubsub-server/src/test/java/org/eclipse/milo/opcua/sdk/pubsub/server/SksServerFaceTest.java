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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigValidationException;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The direct-invoke authorization matrix for the SKS server face (K17.2/K17.3), plus handler
 * lifecycle (attach on startup, restore on shutdown) and capabilities values.
 *
 * <p>The handler is invoked directly with a stubbed {@link AccessContext} and a mocked {@link
 * Session}: on the real service path an unencrypted Call never reaches the handler (the access
 * controller denies it via the nodeset's AccessRestrictions first), so the handler-side checks are
 * only reachable this way.
 */
class SksServerFaceTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final String OPEN_GROUP = "open-group";
  private static final String RESTRICTED_GROUP = "restricted-group";

  private static final NodeId CUSTOM_ROLE = new NodeId(1, "custom-role");
  private static final NodeId UNRELATED_ROLE = new NodeId(1, "unrelated-role");

  private static TestPubSubServer testServer;

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  @Test
  void insufficientChannelSecurityModeIsDenied() throws Exception {
    ServerPubSub serverPubSub = attachWithSksFace();
    try {
      for (MessageSecurityMode mode : List.of(MessageSecurityMode.None, MessageSecurityMode.Sign)) {
        CallMethodResult result = invokeGetSecurityKeys(mockSession(mode, null), OPEN_GROUP);

        assertEquals(
            new StatusCode(StatusCodes.Bad_SecurityModeInsufficient), result.getStatusCode());
      }
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void openGroupIsServedToEncryptedCallersWithoutRoleMapper() throws Exception {
    ServerPubSub serverPubSub = attachWithSksFace();
    try {
      CallMethodResult result = invokeGetSecurityKeys(encryptedSession(null), OPEN_GROUP);

      assertEquals(StatusCode.GOOD, result.getStatusCode());

      Variant[] outputs = result.getOutputArguments();
      assertNotNull(outputs);
      assertEquals(5, outputs.length);

      assertEquals(PubSubSecurityPolicy.PubSubAes256Ctr.getUri(), outputs[0].getValue());
      assertEquals(uint(1), outputs[1].getValue());

      ByteString[] keys = (ByteString[]) outputs[2].getValue();
      assertNotNull(keys);
      assertEquals(1, keys.length);
      assertEquals(68, keys[0].length());

      double timeToNextKey = (Double) outputs[3].getValue();
      double keyLifetime = (Double) outputs[4].getValue();
      assertTrue(timeToNextKey > 0);
      assertEquals(Duration.ofMinutes(10).toMillis(), keyLifetime);
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void restrictedGroupIsDeniedWithoutRoleMapper() throws Exception {
    ServerPubSub serverPubSub = attachWithSksFace();
    try {
      CallMethodResult result = invokeGetSecurityKeys(encryptedSession(null), RESTRICTED_GROUP);

      assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), result.getStatusCode());
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void restrictedGroupHonorsCallPermissionForMappedRoles() throws Exception {
    ServerPubSub serverPubSub = attachWithSksFace();
    try {
      CallMethodResult allowed =
          invokeGetSecurityKeys(encryptedSession(List.of(CUSTOM_ROLE)), RESTRICTED_GROUP);
      assertEquals(StatusCode.GOOD, allowed.getStatusCode());

      CallMethodResult denied =
          invokeGetSecurityKeys(encryptedSession(List.of(UNRELATED_ROLE)), RESTRICTED_GROUP);
      assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), denied.getStatusCode());
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void openGroupRequiresTheDefaultPullRolesWhenAMapperIsConfigured() throws Exception {
    ServerPubSub serverPubSub = attachWithSksFace();
    try {
      CallMethodResult allowed =
          invokeGetSecurityKeys(
              encryptedSession(List.of(NodeIds.WellKnownRole_SecurityKeyServerAccess)), OPEN_GROUP);
      assertEquals(StatusCode.GOOD, allowed.getStatusCode());

      // a RoleMapper returning an empty role list is still "mapper configured": deny
      CallMethodResult denied = invokeGetSecurityKeys(encryptedSession(List.of()), OPEN_GROUP);
      assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), denied.getStatusCode());
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void unknownGroupIsNotFoundOnlyForAuthorizedCallers() throws Exception {
    ServerPubSub serverPubSub = attachWithSksFace();
    try {
      // no RoleMapper: authorization allows, existence check reports Bad_NotFound
      CallMethodResult noMapper = invokeGetSecurityKeys(encryptedSession(null), "no-such-group");
      assertEquals(new StatusCode(StatusCodes.Bad_NotFound), noMapper.getStatusCode());

      // mapper configured, no SKS role: denied before existence is decidable (no leak)
      CallMethodResult unauthorized =
          invokeGetSecurityKeys(encryptedSession(List.of(UNRELATED_ROLE)), "no-such-group");
      assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), unauthorized.getStatusCode());

      // mapper configured, SKS role present: authorized, so existence is reported
      CallMethodResult authorized =
          invokeGetSecurityKeys(
              encryptedSession(List.of(NodeIds.WellKnownRole_SecurityKeyServerAdmin)),
              "no-such-group");
      assertEquals(new StatusCode(StatusCodes.Bad_NotFound), authorized.getStatusCode());
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void nullOrEmptySecurityGroupIdIsNotFound() throws Exception {
    ServerPubSub serverPubSub = attachWithSksFace();
    try {
      CallMethodResult nullId = invokeGetSecurityKeys(encryptedSession(null), null);
      assertEquals(new StatusCode(StatusCodes.Bad_NotFound), nullId.getStatusCode());

      CallMethodResult emptyId = invokeGetSecurityKeys(encryptedSession(null), "");
      assertEquals(new StatusCode(StatusCodes.Bad_NotFound), emptyId.getStatusCode());
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void nullTokenArgumentsDefaultToTheCurrentKey() throws Exception {
    ServerPubSub serverPubSub = attachWithSksFace();
    try {
      CallMethodResult result =
          invokeGetSecurityKeys(encryptedSession(null), OPEN_GROUP, null, null);

      assertEquals(StatusCode.GOOD, result.getStatusCode());

      Variant[] outputs = result.getOutputArguments();
      assertNotNull(outputs);
      assertEquals(uint(1), outputs[1].getValue());
      assertEquals(1, ((ByteString[]) outputs[2].getValue()).length);
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void missingSessionIsRejected() throws Exception {
    ServerPubSub serverPubSub = attachWithSksFace();
    try {
      CallMethodResult result =
          getSecurityKeysNode()
              .getInvocationHandler()
              .invoke(AccessContext.INTERNAL, getSecurityKeysRequest(OPEN_GROUP, uint(0), uint(0)));

      assertEquals(new StatusCode(StatusCodes.Bad_SessionIdInvalid), result.getStatusCode());
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void customAuthorizerStatusIsSurfacedVerbatim() throws Exception {
    var authorizer =
        new PubSubMethodAuthorizer() {
          @Override
          public StatusCode checkConfigure(Session session) {
            return StatusCode.GOOD;
          }

          @Override
          public StatusCode checkSksAdmin(Session session) {
            return StatusCode.GOOD;
          }

          @Override
          public StatusCode checkKeyAccess(Session session, String securityGroupId) {
            return new StatusCode(StatusCodes.Bad_ConfigurationError);
          }
        };

    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            securityGroupsConfig(),
            ServerPubSubOptions.builder()
                .securityKeyServerEnabled(true)
                .methodAuthorizer(authorizer)
                .build());
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    try {
      CallMethodResult result = invokeGetSecurityKeys(encryptedSession(null), OPEN_GROUP);

      assertEquals(new StatusCode(StatusCodes.Bad_ConfigurationError), result.getStatusCode());
    } finally {
      serverPubSub.close();
    }
  }

  /**
   * A10: a custom {@link PubSubMethodAuthorizer} GRANT overrides the default posture — a caller
   * with no roles against a group carrying restrictive RolePermissions (which the default
   * authorizer denies, see {@link #restrictedGroupIsDeniedWithoutRoleMapper}) is served keys when
   * the SPI says GOOD. The channel-mode check is NOT delegated: an unencrypted caller stays denied
   * regardless of the authorizer.
   */
  @Test
  void customAuthorizerGrantOverridesTheRestrictivePosture() throws Exception {
    var authorizer =
        new PubSubMethodAuthorizer() {
          @Override
          public StatusCode checkConfigure(Session session) {
            return StatusCode.GOOD;
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

    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            securityGroupsConfig(),
            ServerPubSubOptions.builder()
                .securityKeyServerEnabled(true)
                .methodAuthorizer(authorizer)
                .build());
    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    try {
      CallMethodResult result = invokeGetSecurityKeys(encryptedSession(null), RESTRICTED_GROUP);

      assertEquals(StatusCode.GOOD, result.getStatusCode());

      Variant[] outputs = result.getOutputArguments();
      assertNotNull(outputs);
      assertEquals(5, outputs.length);
      ByteString[] keys = (ByteString[]) outputs[2].getValue();
      assertNotNull(keys);
      assertTrue(keys.length >= 1);

      // the K17.2 channel check runs BEFORE the authorizer: the grant does not override it
      CallMethodResult unencrypted =
          invokeGetSecurityKeys(mockSession(MessageSecurityMode.None, null), RESTRICTED_GROUP);
      assertEquals(
          new StatusCode(StatusCodes.Bad_SecurityModeInsufficient), unencrypted.getStatusCode());
    } finally {
      serverPubSub.close();
    }
  }

  /**
   * The K15/K16 deferral pin: with the face attached and serving GetSecurityKeys, the SKS
   * management and push methods — AddSecurityGroup, RemoveSecurityGroup (K15-deferred) and
   * SetSecurityKeys (K16 push CUT) — remain {@code NOT_IMPLEMENTED} and answer {@code
   * Bad_NotImplemented}.
   */
  @Test
  void sksManagementAndPushMethodsRemainNotImplemented() throws Exception {
    ServerPubSub serverPubSub = attachWithSksFace();
    try {
      for (NodeId methodId :
          List.of(
              NodeIds.PublishSubscribe_SecurityGroups_AddSecurityGroup,
              NodeIds.PublishSubscribe_SecurityGroups_RemoveSecurityGroup,
              NodeIds.PublishSubscribe_SetSecurityKeys)) {

        UaMethodNode methodNode =
            (UaMethodNode)
                testServer
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(methodId)
                    .orElseThrow(() -> new AssertionError("missing ns0 method node: " + methodId));

        assertSame(
            MethodInvocationHandler.NOT_IMPLEMENTED,
            methodNode.getInvocationHandler(),
            methodId.toString());

        CallMethodResult result =
            methodNode
                .getInvocationHandler()
                .invoke(
                    () -> Optional.of(encryptedSession(null)),
                    new CallMethodRequest(NodeIds.PublishSubscribe, methodId, new Variant[0]));

        assertEquals(
            new StatusCode(StatusCodes.Bad_NotImplemented),
            result.getStatusCode(),
            methodId.toString());
      }
    } finally {
      serverPubSub.close();
    }
  }

  @Test
  void handlerIsAttachedOnStartupAndRestoredOnShutdown() throws Exception {
    UaMethodNode methodNode = getSecurityKeysNode();
    assertSame(MethodInvocationHandler.NOT_IMPLEMENTED, methodNode.getInvocationHandler());

    ServerPubSub serverPubSub = attachWithSksFace();
    try {
      // exposeInformationModel is false (the default): the face attaches independently
      assertNotSame(MethodInvocationHandler.NOT_IMPLEMENTED, methodNode.getInvocationHandler());

      assertEquals(
          true, capabilityValue(NodeIds.PublishSubscribe_PubSubCapablities_SupportSecurityKeyPull));
      assertEquals(
          false,
          capabilityValue(NodeIds.PublishSubscribe_PubSubCapablities_SupportSecurityKeyPush));
      assertEquals(
          true,
          capabilityValue(NodeIds.PublishSubscribe_PubSubCapablities_SupportSecurityKeyServer));
    } finally {
      serverPubSub.close();
    }

    assertSame(MethodInvocationHandler.NOT_IMPLEMENTED, methodNode.getInvocationHandler());
    assertEquals(
        false,
        capabilityValue(NodeIds.PublishSubscribe_PubSubCapablities_SupportSecurityKeyServer));

    CallMethodResult result =
        methodNode
            .getInvocationHandler()
            .invoke(AccessContext.INTERNAL, getSecurityKeysRequest(OPEN_GROUP, uint(0), uint(0)));
    assertEquals(new StatusCode(StatusCodes.Bad_NotImplemented), result.getStatusCode());
  }

  @Test
  void olderFaceShutdownDoesNotClobberANewerAttach() {
    UaMethodNode methodNode = getSecurityKeysNode();
    assertSame(MethodInvocationHandler.NOT_IMPLEMENTED, methodNode.getInvocationHandler());

    var authorizer = new DefaultPubSubMethodAuthorizer(groupId -> null);
    var olderFace = new SksServerFace(testServer.getServer(), securityGroupsConfig(), authorizer);
    var newerFace = new SksServerFace(testServer.getServer(), securityGroupsConfig(), authorizer);

    try {
      olderFace.startup();
      MethodInvocationHandler olderHandler = methodNode.getInvocationHandler();
      assertNotSame(MethodInvocationHandler.NOT_IMPLEMENTED, olderHandler);

      // a second attach warns and replaces: only one face per server is supported
      newerFace.startup();
      MethodInvocationHandler newerHandler = methodNode.getInvocationHandler();
      assertNotSame(olderHandler, newerHandler);

      // the older face's shutdown must not restore NOT_IMPLEMENTED over the newer handler
      olderFace.shutdown();
      assertSame(newerHandler, methodNode.getInvocationHandler());
    } finally {
      newerFace.shutdown();
    }

    assertSame(MethodInvocationHandler.NOT_IMPLEMENTED, methodNode.getInvocationHandler());
  }

  @Test
  void defaultAuthorizerConfigurePosture() {
    var authorizer = new DefaultPubSubMethodAuthorizer(groupId -> null);

    // no RoleMapper: allow, consistent with the core posture (D10)
    assertEquals(StatusCode.GOOD, authorizer.checkConfigure(encryptedSession(null)));

    assertEquals(
        StatusCode.GOOD,
        authorizer.checkConfigure(encryptedSession(List.of(NodeIds.WellKnownRole_ConfigureAdmin))));

    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        authorizer.checkConfigure(encryptedSession(List.of(UNRELATED_ROLE))));
  }

  @Test
  void defaultAuthorizerSksAdminPosture() {
    var authorizer = new DefaultPubSubMethodAuthorizer(groupId -> null);

    // no RoleMapper: allow, consistent with the core posture (D10)
    assertEquals(StatusCode.GOOD, authorizer.checkSksAdmin(encryptedSession(null)));

    assertEquals(
        StatusCode.GOOD,
        authorizer.checkSksAdmin(
            encryptedSession(List.of(NodeIds.WellKnownRole_SecurityKeyServerAdmin))));

    // SecurityKeyServerAccess grants key access, not SKS administration
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        authorizer.checkSksAdmin(
            encryptedSession(List.of(NodeIds.WellKnownRole_SecurityKeyServerAccess))));
  }

  @Test
  void attachFailsOnAnUnsupportedSecurityPolicyUri() {
    PubSubConfig config =
        PubSubConfig.builder()
            .securityGroup(
                SecurityGroupConfig.builder("bad-group")
                    .securityPolicyUri("http://opcfoundation.org/UA/SecurityPolicy#Basic256Sha256")
                    .build())
            .build();

    // without the face the group is config-only and attach succeeds
    ServerPubSub withoutFace = ServerPubSub.attach(testServer.getServer(), config);
    withoutFace.close();

    assertThrows(
        PubSubConfigValidationException.class,
        () ->
            ServerPubSub.attach(
                testServer.getServer(),
                config,
                ServerPubSubOptions.builder().securityKeyServerEnabled(true).build()));
  }

  private static PubSubConfig securityGroupsConfig() {
    return PubSubConfig.builder()
        .securityGroup(
            SecurityGroupConfig.builder(OPEN_GROUP).keyLifeTime(Duration.ofMinutes(10)).build())
        .securityGroup(
            SecurityGroupConfig.builder(RESTRICTED_GROUP)
                .keyLifeTime(Duration.ofMinutes(10))
                .rolePermission(
                    new RolePermissionType(
                        CUSTOM_ROLE, PermissionType.of(PermissionType.Field.Call)))
                .build())
        .build();
  }

  private static ServerPubSub attachWithSksFace() throws Exception {
    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            securityGroupsConfig(),
            ServerPubSubOptions.builder().securityKeyServerEnabled(true).build());

    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    return serverPubSub;
  }

  /** A mocked SignAndEncrypt {@link Session}; {@code roleIds} null means "no RoleMapper". */
  private static Session encryptedSession(@Nullable List<NodeId> roleIds) {
    return mockSession(MessageSecurityMode.SignAndEncrypt, roleIds);
  }

  private static Session mockSession(MessageSecurityMode mode, @Nullable List<NodeId> roleIds) {
    EndpointDescription endpoint = mock(EndpointDescription.class);
    when(endpoint.getSecurityMode()).thenReturn(mode);

    Session session = mock(Session.class);
    when(session.getEndpoint()).thenReturn(endpoint);
    when(session.getRoleIds()).thenReturn(Optional.ofNullable(roleIds));

    return session;
  }

  private static CallMethodResult invokeGetSecurityKeys(
      Session session, @Nullable String securityGroupId) {
    return invokeGetSecurityKeys(session, securityGroupId, uint(0), uint(0));
  }

  private static CallMethodResult invokeGetSecurityKeys(
      Session session,
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount) {

    return getSecurityKeysNode()
        .getInvocationHandler()
        .invoke(
            () -> Optional.of(session),
            getSecurityKeysRequest(securityGroupId, startingTokenId, requestedKeyCount));
  }

  private static CallMethodRequest getSecurityKeysRequest(
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount) {

    return new CallMethodRequest(
        NodeIds.PublishSubscribe,
        NodeIds.PublishSubscribe_GetSecurityKeys,
        new Variant[] {
          new Variant(securityGroupId), new Variant(startingTokenId), new Variant(requestedKeyCount)
        });
  }

  private static UaMethodNode getSecurityKeysNode() {
    return (UaMethodNode)
        testServer
            .getServer()
            .getAddressSpaceManager()
            .getManagedNode(NodeIds.PublishSubscribe_GetSecurityKeys)
            .orElseThrow();
  }

  private static @Nullable Object capabilityValue(NodeId nodeId) {
    UaVariableNode node =
        (UaVariableNode)
            testServer.getServer().getAddressSpaceManager().getManagedNode(nodeId).orElseThrow();

    return node.getValue().getValue().getValue();
  }
}
