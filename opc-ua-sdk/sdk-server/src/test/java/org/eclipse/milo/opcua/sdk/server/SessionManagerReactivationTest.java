/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.channel.Channel;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.sdk.server.identity.Identity.UsernameIdentity;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransportFactory;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * When {@link SessionManager} reports a re-activated Session's identity or endpoint change to its
 * listeners.
 */
class SessionManagerReactivationTest {

  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840/test";

  private static final UserTokenPolicy ANONYMOUS_POLICY =
      new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);

  // No security policy URI: the password travels in the clear over the None channel.
  private static final UserTokenPolicy USERNAME_POLICY =
      new UserTokenPolicy("username", UserTokenType.UserName, null, null, null);

  private static final OpcServerTransportFactory NO_OP_TRANSPORTS =
      transportProfile ->
          new OpcServerTransport() {
            @Override
            public void bind(
                ServerApplicationContext applicationContext, InetSocketAddress bindAddress) {}

            @Override
            public void unbind() {}
          };

  private final RecordingListener listener = new RecordingListener();
  private final ServiceRequestContext channel = new TestServiceRequestContext(1L);
  private final ServiceRequestContext replacementChannel = new TestServiceRequestContext(2L);

  private OpcUaServer server;
  private SessionManager sessions;

  @BeforeEach
  void setUp() {
    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("localhost")
            .setBindPort(4840)
            .setHostname("localhost")
            .setPath("/test")
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .addTokenPolicy(ANONYMOUS_POLICY)
            .addTokenPolicy(USERNAME_POLICY)
            .build();

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:test:server")
            .setApplicationName(LocalizedText.english("test server"))
            .setProductUri("urn:eclipse:milo:test")
            .setCertificateManager(new DefaultCertificateManager())
            .setIdentityValidator(
                new CompositeValidator(
                    AnonymousIdentityValidator.INSTANCE,
                    new UsernameIdentityValidator(challenge -> true)))
            .setEndpoints(Set.of(endpoint))
            .build();

    server = new OpcUaServer(config, NO_OP_TRANSPORTS);
    sessions = server.getSessionManager();
    sessions.addSessionListener(listener);
  }

  @AfterEach
  void tearDown() {
    sessions.shutdown();
  }

  /**
   * Part 4 §5.7.3.1: ActivateSession on the Session's SecureChannel may replace its user identity.
   * A listener that derives anything from the identity must hear about that exactly once, after the
   * new identity is in place, and in order with the other lifecycle notifications. The first
   * activation is not an identity change; onSessionCreated already covers that Session.
   */
  @Test
  void sameChannelReactivationWithAnotherUserReportsTheIdentityChangeOnce() throws Exception {
    NodeId token = createSession();

    sessions.activateSession(channel, activateAnonymous(token));
    sessions.activateSession(channel, activateUsername(token, USERNAME_POLICY.getPolicyId()));
    closeSession();

    listener.closed.get(10, SECONDS);
    assertEquals(List.of("created", "identityChanged", "closed"), listener.events);
    UsernameIdentity identity =
        assertInstanceOf(UsernameIdentity.class, listener.identitiesSeen.get(0));
    assertEquals("alice", identity.getUsername(), "the listener sees the new identity");
  }

  // A re-activation that fails, here with a policy id the endpoint does not offer, leaves the
  // identity as it was, so there is no change to report.
  @Test
  void failedReactivationReportsNoIdentityChange() throws Exception {
    NodeId token = createSession();

    sessions.activateSession(channel, activateAnonymous(token));
    UaException failure =
        assertThrows(
            UaException.class,
            () -> sessions.activateSession(channel, activateUsername(token, "unknown-policy")));
    closeSession();

    listener.closed.get(10, SECONDS);
    assertEquals(List.of("created", "closed"), listener.events, failure.getMessage());
  }

  /**
   * Part 4 §5.7.3.1: ActivateSession may move a Session onto a replacement SecureChannel. A
   * listener that derives anything from the Session's endpoint or security mode must hear about
   * that exactly once, after the move is committed, and in order with the other notifications.
   */
  @Test
  void reactivationOnAReplacementChannelReportsTheEndpointChangeOnce() throws Exception {
    NodeId token = createSession();

    sessions.activateSession(channel, activateAnonymous(token));
    sessions.activateSession(replacementChannel, activateAnonymous(token));
    closeSession();

    listener.closed.get(10, SECONDS);
    assertEquals(List.of("created", "endpointChanged", "closed"), listener.events);
    assertEquals(
        List.of(2L), listener.channelIdsSeen, "the listener sees the Session on its new channel");
  }

  /**
   * A re-activation on another channel that fails, here because it presents another identity,
   * leaves the Session on its previous channel, so there is no change to report.
   */
  @Test
  void failedReactivationOnAReplacementChannelReportsNoEndpointChange() throws Exception {
    NodeId token = createSession();

    sessions.activateSession(channel, activateAnonymous(token));

    UaException failure =
        assertThrows(
            UaException.class,
            () ->
                sessions.activateSession(
                    replacementChannel, activateUsername(token, USERNAME_POLICY.getPolicyId())));

    closeSession();

    listener.closed.get(10, SECONDS);
    assertEquals(List.of("created", "closed"), listener.events, failure.getMessage());
  }

  private NodeId createSession() throws UaException {
    return sessions.createSession(channel, createSessionRequest()).getAuthenticationToken();
  }

  private void closeSession() {
    sessions.getAllSessions().forEach(session -> session.close(true));
  }

  private static CreateSessionRequest createSessionRequest() {
    return new CreateSessionRequest(
        requestHeader(NodeId.NULL_VALUE),
        new ApplicationDescription(
            "urn:test:client",
            "urn:test:client-product",
            LocalizedText.english("client"),
            ApplicationType.Client,
            null,
            null,
            null),
        null,
        ENDPOINT_URL,
        "test-session",
        NonceUtil.generateNonce(32),
        ByteString.NULL_VALUE,
        60_000.0,
        uint(0));
  }

  private static ActivateSessionRequest activateAnonymous(NodeId token) {
    return activateSessionRequest(token, null);
  }

  /** A username token for "alice" presented under {@code policyId}. */
  private ActivateSessionRequest activateUsername(NodeId token, String policyId) {
    var identityToken =
        new UserNameIdentityToken(
            policyId, "alice", ByteString.of("secret".getBytes(StandardCharsets.UTF_8)), null);

    return activateSessionRequest(
        token, ExtensionObject.encode(server.getStaticEncodingContext(), identityToken));
  }

  private static ActivateSessionRequest activateSessionRequest(
      NodeId token, @Nullable ExtensionObject userIdentityToken) {

    return new ActivateSessionRequest(
        requestHeader(token),
        new SignatureData(null, null),
        null,
        null,
        userIdentityToken,
        new SignatureData(null, null));
  }

  private static RequestHeader requestHeader(NodeId authToken) {
    return new RequestHeader(authToken, DateTime.now(), uint(1), uint(0), null, uint(10_000), null);
  }

  /** Records the order of notifications and what each change delivered. */
  private static final class RecordingListener implements SessionListener {

    final List<String> events = new CopyOnWriteArrayList<>();
    final List<Identity> identitiesSeen = new CopyOnWriteArrayList<>();
    final List<Long> channelIdsSeen = new CopyOnWriteArrayList<>();
    final CompletableFuture<Session> closed = new CompletableFuture<>();

    @Override
    public void onSessionCreated(Session session) {
      events.add("created");
    }

    @Override
    public void onSessionIdentityChanged(Session session) {
      events.add("identityChanged");
      identitiesSeen.add(session.getIdentity());
    }

    @Override
    public void onSessionEndpointChanged(Session session) {
      events.add("endpointChanged");
      channelIdsSeen.add(session.getSecureChannelId());
    }

    @Override
    public void onSessionClosed(Session session) {
      events.add("closed");
      closed.complete(session);
    }
  }

  /** A request arriving over an unsecured channel to the None endpoint. */
  private static final class TestServiceRequestContext implements ServiceRequestContext {

    private final ServerSecureChannel secureChannel = new ServerSecureChannel();

    TestServiceRequestContext(long channelId) {
      secureChannel.setChannelId(channelId);
      secureChannel.setSecurityPolicy(SecurityPolicy.None);
      secureChannel.setMessageSecurityMode(MessageSecurityMode.None);
    }

    @Override
    public String getEndpointUrl() {
      return ENDPOINT_URL;
    }

    @Override
    public TransportProfile getTransportProfile() {
      return TransportProfile.TCP_UASC_UABINARY;
    }

    @Override
    public Channel getChannel() {
      throw new UnsupportedOperationException();
    }

    @Override
    public SecureChannel getSecureChannel() {
      return secureChannel;
    }

    @Override
    public Optional<EndpointDescription> getEndpoint() {
      return Optional.empty();
    }

    @Override
    public Long receivedAtNanos() {
      return System.nanoTime();
    }

    @Override
    public InetAddress clientAddress() {
      return InetAddress.getLoopbackAddress();
    }
  }
}
