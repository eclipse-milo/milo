/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.eclipse.milo.opcua.sdk.client.EndpointConfiguration;
import org.eclipse.milo.opcua.sdk.client.EndpointResolver;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.identity.X509IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.EndpointCertificateConfig;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.X509IdentityValidator;
import org.eclipse.milo.opcua.sdk.test.TestPortAllocator;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class ServerCertificateRotationTest {
  private static final String SERVER_URI = "urn:milo:rotation:server";
  private static final String CLIENT_URI = "urn:milo:rotation:client";

  // Part 4 6.7: keep the Java client and recover sessions/subscriptions after live rotation.
  @ParameterizedTest(name = "{0}, sameKey={1}, retainedSession={2}, response={3}")
  @MethodSource("rotations")
  void recoversAfterLiveRotation(
      SecurityPolicy policy, boolean sameKey, boolean retainSession, ThumbprintResponse response)
      throws Exception {
    try (var fixture = new Fixture(policy, response)) {
      fixture.connect();
      OpcUaSession original = fixture.client.getSession();
      Channel originalChannel = fixture.channel();
      var boundChannel =
          ((OpcTcpClientTransport) fixture.client.getTransport()).getSecureChannel().orElseThrow();
      X509Certificate originalCertificate = boundChannel.getRemoteCertificate();
      fixture.subscribe();
      assertNotNull(fixture.notifications.poll(5, TimeUnit.SECONDS));
      fixture.activeSessions.clear();
      long started = System.nanoTime();
      X509Certificate replacement = fixture.rotate(sameKey, true);
      // The established channel continues using A until it closes, even after B is advertised.
      fixture.read();
      assertEquals(originalCertificate, boundChannel.getRemoteCertificate());
      assertEquals(
          original.getServerCertificate(), fixture.client.getSession().getServerCertificate());
      if (!retainSession)
        fixture.server.getSessionManager().killSession(original.getSessionId(), false);
      originalChannel.close().sync();
      OpcUaSession recovered = fixture.activeSessions.poll(25, TimeUnit.SECONDS);
      assertNotNull(recovered, "same client must recover without an application retry loop");
      if (retainSession) assertSame(original, recovered);
      else assertNotEquals(original.getSessionId(), recovered.getSessionId());
      assertNotSame(originalChannel, fixture.channel());
      assertEquals(
          replacement,
          ((OpcTcpClientTransport) fixture.client.getTransport())
              .getSecureChannel()
              .orElseThrow()
              .getRemoteCertificate());
      assertEquals(originalCertificate, boundChannel.getRemoteCertificate());
      if (!retainSession)
        assertEquals(ByteString.of(replacement.getEncoded()), recovered.getServerCertificate());
      else
        assertNotEquals(ByteString.of(replacement.getEncoded()), recovered.getServerCertificate());
      fixture.read();
      fixture.notifications.clear();
      assertNotNull(fixture.notifications.poll(6, TimeUnit.SECONDS), "notifications must resume");
      assertEquals(2, fixture.discoveries.get(), "initial discovery plus one refresh");
      assertTrue(
          fixture.unknownThumbprints.get() >= 1, "must exercise stale-certificate handshake");
      System.out.printf(
          "rotation policy=%s sameKey=%s retained=%s response=%s recoveryMs=%d discoveries=%d%n",
          policy,
          sameKey,
          retainSession,
          response,
          TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - started),
          fixture.discoveries.get());
    }
  }

  // Recovery must tolerate servers that disguise a stale thumbprint or send no error response.
  @ParameterizedTest
  @EnumSource(
      value = ThumbprintResponse.class,
      names = {"UNEXPECTED_ERROR", "SILENT_CLOSE", "NO_RESPONSE"})
  void recoversWithoutCertificateStatus(ThumbprintResponse response) throws Exception {
    try (var f = new Fixture(SecurityPolicy.Basic256Sha256, response, "anonymous", false, 1000)) {
      f.connect();
      var handshakeFailure = new CompletableFuture<Throwable>();
      ((ChannelStateObservable) f.client.getTransport())
          .addTransitionListener(
              new ChannelStateObservable.TransitionListener() {
                @Override
                public void onStateTransition(boolean connected) {}

                @Override
                public void onConnectFailure(Throwable failure) {
                  handshakeFailure.complete(failure);
                }
              });
      OpcUaSession original = f.client.getSession();
      f.activeSessions.clear();
      X509Certificate replacement = f.rotate(false, true);
      f.channel().close().sync();
      assertSame(original, f.activeSessions.poll(15, TimeUnit.SECONDS));
      f.read();
      assertEquals(
          replacement,
          ((OpcTcpClientTransport) f.client.getTransport())
              .getSecureChannel()
              .orElseThrow()
              .getRemoteCertificate());
      long expectedStatus =
          switch (response) {
            case CERTIFICATE_INVALID -> StatusCodes.Bad_CertificateInvalid;
            case SECURITY_CHECKS_FAILED -> StatusCodes.Bad_SecurityChecksFailed;
            case UNEXPECTED_ERROR -> StatusCodes.Bad_UnexpectedError;
            case SILENT_CLOSE -> StatusCodes.Bad_ConnectionClosed;
            case NO_RESPONSE -> StatusCodes.Bad_Timeout;
          };
      assertEquals(
          expectedStatus,
          UaException.extractStatusCode(handshakeFailure.get(2, TimeUnit.SECONDS))
              .orElseThrow()
              .value());
      assertTrue(f.unknownThumbprints.get() >= 1);
      assertEquals(2, f.discoveries.get(), "initial discovery plus one refresh");
    }
  }

  enum ThumbprintResponse {
    CERTIFICATE_INVALID,
    SECURITY_CHECKS_FAILED,
    UNEXPECTED_ERROR,
    SILENT_CLOSE,
    NO_RESPONSE
  }

  // Network loss alone does not suggest a changed server certificate.
  @ParameterizedTest
  @MethodSource("policies")
  void ordinaryReconnectDoesNotDiscover(SecurityPolicy policy) throws Exception {
    try (var f = new Fixture(policy)) {
      f.connect();
      OpcUaSession original = f.client.getSession();
      f.activeSessions.clear();
      f.channel().close().sync();
      assertSame(original, f.activeSessions.poll(15, TimeUnit.SECONDS));
      f.read();
      assertEquals(1, f.discoveries.get());
    }
  }

  // Removing the listener before rotation models a server restart, without external processes.
  @ParameterizedTest
  @MethodSource("policies")
  void recoversAfterServerDownRotation(SecurityPolicy policy) throws Exception {
    try (var f = new Fixture(policy)) {
      f.connect();
      f.activeSessions.clear();
      f.server.shutdown().get(5, TimeUnit.SECONDS);
      assertNull(f.activeSessions.poll(2, TimeUnit.SECONDS));
      assertEquals(1, f.discoveries.get(), "network failure alone must not trigger discovery");
      f.rotate(false, true);
      f.server =
          new OpcUaServer(f.server.getConfig(), profile -> new OpcTcpServerTransport(f.tcpConfig));
      f.server.startup().get(5, TimeUnit.SECONDS);
      assertNotNull(f.activeSessions.poll(25, TimeUnit.SECONDS));
      f.read();
      assertEquals(2, f.discoveries.get());
    }
  }

  // Failed or unchanged discovery must leave later retries eligible without a discovery flood.
  @ParameterizedTest(name = "{0}, discovery={1}")
  @MethodSource("discoveryFailures")
  void recoversAfterTemporaryDiscoveryFailure(SecurityPolicy policy, String failure)
      throws Exception {
    try (var f = new Fixture(policy)) {
      f.connect();
      EndpointConfiguration cached = f.initial;
      var attempted = new CompletableFuture<Throwable>();
      f.noMatchingEndpoint = failure.equals("no-match");
      f.discoveryUnavailable = failure.equals("outage");
      f.discoveryBehavior =
          discovered ->
              discovered
                  .<CompletableFuture<EndpointConfiguration>>handle(
                      (value, error) -> {
                        attempted.complete(error);
                        if (error != null)
                          return CompletableFuture.<EndpointConfiguration>failedFuture(error);
                        return switch (failure) {
                          case "unchanged" -> CompletableFuture.completedFuture(cached);
                          case "no-match" ->
                              CompletableFuture.failedFuture(
                                  new UaException(
                                      StatusCodes.Bad_ConfigurationError, "no endpoint selected"));
                          default ->
                              CompletableFuture.failedFuture(
                                  new UaException(StatusCodes.Bad_Timeout));
                        };
                      })
                  .thenCompose(Function.identity());
      f.activeSessions.clear();
      f.rotate(false, true);
      f.channel().close().sync();
      Throwable resolutionError = attempted.get(10, TimeUnit.SECONDS);
      if (!failure.equals("unchanged"))
        assertNotNull(
            resolutionError, "resolver must fail at its actual discovery/selection boundary");
      assertEquals(2, f.discoveries.get());
      // Restoring discovery must suffice; no new client, reconnect call or refresh event is needed.
      f.discoveryUnavailable = false;
      f.noMatchingEndpoint = false;
      f.discoveryBehavior = discovered -> discovered;
      OpcUaSession recovered = f.activeSessions.poll(30, TimeUnit.SECONDS);
      assertNotNull(recovered);
      f.read();
      assertEquals(3, f.discoveries.get());
      assertEquals(policy.getUri(), recovered.getEndpoint().orElseThrow().getSecurityPolicyUri());
    }
  }

  // GetEndpoints is unauthenticated metadata. It must never make an untrusted B acceptable.
  @ParameterizedTest
  @MethodSource("policies")
  void rejectsUntrustedReplacement(SecurityPolicy policy) throws Exception {
    try (var f = new Fixture(policy)) {
      f.connect();
      f.activeSessions.clear();
      X509Certificate replacement = f.rotate(false, false);
      f.channel().close().sync();
      assertEquals(replacement, f.rejectedCertificates.poll(12, TimeUnit.SECONDS));
      assertNull(f.activeSessions.poll(2, TimeUnit.SECONDS));
      assertTrue(f.discoveries.get() >= 2, "rediscovery must not grant trust in the replacement");
    }
  }

  // Disconnecting during a refresh must neither open a Session nor leave a channel behind.
  @Test
  void closeDuringRefreshDiscardsLateResult() throws Exception {
    try (var f = new Fixture(SecurityPolicy.Basic256Sha256)) {
      f.connect();
      var entered = new CompletableFuture<EndpointConfiguration>();
      var late =
          new CompletableFuture<EndpointConfiguration>() {
            @Override
            public boolean cancel(boolean mayInterrupt) {
              return false;
            }
          };
      f.discoveryBehavior =
          discovered -> {
            discovered.thenAccept(entered::complete);
            return late;
          };
      f.activeSessions.clear();
      f.rotate(false, true);
      f.channel().close().sync();
      EndpointConfiguration replacement = entered.get(10, TimeUnit.SECONDS);
      f.client.disconnectAsync().get(8, TimeUnit.SECONDS);
      late.complete(replacement);
      assertNull(f.activeSessions.poll(1, TimeUnit.SECONDS));
      assertEquals(State.Inactive, f.client.getSessionFsm().getState());
      assertTrue(((OpcTcpClientTransport) f.client.getTransport()).getSecureChannel().isEmpty());
    }
  }

  // Retained sessions keep their credential encryption/signature inputs across channel rotation.
  @ParameterizedTest(name = "{0}, identity={1}")
  @MethodSource("credentialPolicies")
  void reactivatesRetainedSessionWithCredentials(SecurityPolicy policy, String identity)
      throws Exception {
    try (var f = new Fixture(policy, identity)) {
      f.connect();
      OpcUaSession original = f.client.getSession();
      f.activeSessions.clear();
      f.rotate(false, true);
      f.channel().close().sync();
      assertSame(original, f.activeSessions.poll(20, TimeUnit.SECONDS));
      f.read();
      assertEquals(2, f.discoveries.get());
    }
  }

  // Part 6 6.7.4: certificate replacement must not rebind renewal of an established channel.
  @ParameterizedTest
  @MethodSource("policies")
  void renewalKeepsEstablishedCertificateBinding(SecurityPolicy policy) throws Exception {
    try (var f = new Fixture(policy, "anonymous", true)) {
      f.connect();
      var bound =
          ((OpcTcpClientTransport) f.client.getTransport()).getSecureChannel().orElseThrow();
      X509Certificate original = bound.getRemoteCertificate();
      var originalToken = bound.getChannelSecurity().getCurrentToken();
      // Keep A advertised while observing renewal. The live-rotation matrix invalidates the
      // endpoint cache and tests recovery on a replacement channel instead.
      f.rotate(false, true, false);
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(6);
      while (bound.getChannelSecurity().getCurrentToken().equals(originalToken)
          && System.nanoTime() < deadline) {
        f.read();
        CompletableFuture.runAsync(
                () -> {}, CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS))
            .get();
      }
      assertNotEquals(originalToken, bound.getChannelSecurity().getCurrentToken());
      assertSame(
          bound,
          ((OpcTcpClientTransport) f.client.getTransport()).getSecureChannel().orElseThrow());
      assertEquals(original, bound.getRemoteCertificate());
      assertEquals(1, f.discoveries.get());
      f.read();
    }
  }

  // Part 4 6.7 requires a new Session when the client's application certificate changes.
  @ParameterizedTest
  @MethodSource("policies")
  void reconnectWithNewClientIdentityCreatesNewSession(SecurityPolicy policy) throws Exception {
    try (var f = new Fixture(policy)) {
      f.connect();
      OpcUaSession original = f.client.getSession();
      var originalChannel =
          ((OpcTcpClientTransport) f.client.getTransport()).getSecureChannel().orElseThrow();
      X509Certificate originalCertificate = originalChannel.getLocalCertificate();
      KeyPair key = f.keyPair();
      X509Certificate replacement = signedCertificate(key, CLIENT_URI, f.caKey, f.ca);
      f.client
          .getConfig()
          .getCertificateGroup()
          .orElseThrow()
          .updateCertificate(f.certificateType, key, new X509Certificate[] {replacement, f.ca});
      // An explicit connect refreshes the selected local identity without closing the Session.
      // The established channel remains bound to A until the replacement connection uses B.
      f.client.connectAsync().get(5, TimeUnit.SECONDS);
      assertSame(original, f.client.getSession());
      f.activeSessions.clear();
      f.channel().close().sync();
      OpcUaSession recovered = f.activeSessions.poll(15, TimeUnit.SECONDS);
      assertNotNull(recovered);
      assertNotEquals(original.getSessionId(), recovered.getSessionId());
      f.read();
      assertEquals(originalCertificate, originalChannel.getLocalCertificate());
      assertEquals(
          replacement,
          ((OpcTcpClientTransport) f.client.getTransport())
              .getSecureChannel()
              .orElseThrow()
              .getLocalCertificate());
      assertEquals(1, f.discoveries.get());
    }
  }

  // The URL factory must keep the same selection strategy after its initial discovery.
  @ParameterizedTest
  @MethodSource("policies")
  void urlFactoryRetainsResolverAcrossRotation(SecurityPolicy policy) throws Exception {
    try (var f = new Fixture(policy)) {
      var selections = new AtomicInteger();
      OpcUaClient client =
          OpcUaClient.create(
              f.discoveryUrl,
              endpoints -> {
                selections.incrementAndGet();
                return endpoints.stream()
                    .filter(e -> policy.getUri().equals(e.getSecurityPolicyUri()))
                    .filter(e -> e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                    .map(e -> EndpointUtil.updateUrl(e, "localhost"))
                    .findFirst();
              },
              b -> b.setConnectTimeout(uint(1000)),
              b ->
                  b.setApplicationUri(CLIENT_URI)
                      .setCertificateGroup(f.client.getConfig().getCertificateGroup().orElseThrow())
                      .setSessionEndpointValidationEnabled(true)
                      .setRequestTimeout(uint(3000)));
      var active = new LinkedBlockingQueue<UaSession>();
      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionActive(UaSession session) {
              active.add(session);
            }

            @Override
            public void onSessionInactive(UaSession session) {}
          });
      try {
        client.connectAsync().get(10, TimeUnit.SECONDS);
        UaSession original = active.poll(5, TimeUnit.SECONDS);
        assertNotNull(original);
        X509Certificate replacement = f.rotate(false, true);
        f.server.getSessionManager().killSession(original.getSessionId(), false);
        ((OpcTcpClientTransport) client.getTransport())
            .getChannelFsm()
            .getChannel()
            .get()
            .close()
            .sync();
        UaSession recovered = active.poll(20, TimeUnit.SECONDS);
        assertNotNull(recovered);
        assertNotEquals(original.getSessionId(), recovered.getSessionId());
        assertEquals(ByteString.of(replacement.getEncoded()), recovered.getServerCertificate());
        assertTrue(
            client
                .readValuesAsync(
                    0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_CurrentTime))
                .get(5, TimeUnit.SECONDS)
                .get(0)
                .getStatusCode()
                .isGood());
        assertEquals(2, selections.get());
      } finally {
        client.disconnectAsync().get(8, TimeUnit.SECONDS);
      }
    }
  }

  static Stream<Arguments> credentialPolicies() {
    return policies()
        .flatMap(
            policy ->
                Stream.of("username", "certificate")
                    .map(identity -> Arguments.of(policy, identity)));
  }

  static Stream<Arguments> discoveryFailures() {
    return policies()
        .flatMap(
            policy ->
                Stream.of("outage", "unchanged", "no-match")
                    .map(failure -> Arguments.of(policy, failure)));
  }

  static Stream<SecurityPolicy> policies() {
    return Stream.of(SecurityPolicy.Basic256Sha256, SecurityPolicy.ECC_nistP256_AesGcm);
  }

  static Stream<Arguments> rotations() {
    return policies()
        .flatMap(
            policy ->
                Stream.of(false, true)
                    .flatMap(
                        sameKey ->
                            Stream.of(false, true)
                                .flatMap(
                                    retain ->
                                        Stream.of(
                                                ThumbprintResponse.CERTIFICATE_INVALID,
                                                ThumbprintResponse.SECURITY_CHECKS_FAILED)
                                            .map(
                                                response ->
                                                    Arguments.of(
                                                        policy, sameKey, retain, response)))));
  }

  private static final class Fixture implements AutoCloseable {
    final SecurityPolicy policy;
    final NodeId certificateType;
    final KeyPair caKey;
    final X509Certificate ca;
    final DefaultCertificateGroup serverGroup;
    OpcUaServer server;
    final OpcTcpServerTransportConfig tcpConfig;
    final OpcUaClient client;
    final AtomicInteger discoveries = new AtomicInteger();
    final AtomicInteger unknownThumbprints = new AtomicInteger();
    final LinkedBlockingQueue<OpcUaSession> activeSessions = new LinkedBlockingQueue<>();
    final LinkedBlockingQueue<DataValue> notifications = new LinkedBlockingQueue<>();
    final LinkedBlockingQueue<X509Certificate> rejectedCertificates = new LinkedBlockingQueue<>();
    final EndpointResolver resolver;
    final EndpointConfiguration initial;
    final String discoveryUrl;
    volatile boolean discoveryUnavailable;
    volatile boolean noMatchingEndpoint;
    final Set<Channel> discoveryChannels = ConcurrentHashMap.newKeySet();
    volatile UnaryOperator<CompletableFuture<EndpointConfiguration>> discoveryBehavior = f -> f;
    KeyPair serverKey;

    Fixture(SecurityPolicy policy) throws Exception {
      this(policy, ThumbprintResponse.CERTIFICATE_INVALID);
    }

    Fixture(SecurityPolicy policy, ThumbprintResponse response) throws Exception {
      this(policy, response, "anonymous", false, 3000);
    }

    Fixture(SecurityPolicy policy, String identity) throws Exception {
      this(policy, identity, false);
    }

    Fixture(SecurityPolicy policy, String identity, boolean renew) throws Exception {
      this(policy, ThumbprintResponse.CERTIFICATE_INVALID, identity, renew, 3000);
    }

    Fixture(
        SecurityPolicy policy,
        ThumbprintResponse response,
        String identity,
        boolean renew,
        int requestTimeout)
        throws Exception {
      this.policy = policy;
      certificateType =
          policy == SecurityPolicy.Basic256Sha256
              ? NodeIds.RsaSha256ApplicationCertificateType
              : NodeIds.EccNistP256ApplicationCertificateType;
      caKey = keyPair();
      ca = caCertificate(caKey);
      serverKey = keyPair();
      serverGroup = group(serverKey, signedCertificate(serverKey, SERVER_URI, caKey, ca));
      var certificateManager = new DefaultCertificateManager(serverGroup);
      KeyPair clientKey = keyPair();
      X509Certificate clientCertificate = signedCertificate(clientKey, CLIENT_URI, caKey, ca);
      int port = TestPortAllocator.allocatePort();
      int discoveryPort = TestPortAllocator.allocatePort();
      UserTokenPolicy anonymous =
          new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);
      EndpointConfig secure =
          EndpointConfig.newBuilder()
              .setBindAddress("localhost")
              .setHostname("advertised.invalid")
              .setBindPort(port)
              .setPath("/rotation")
              .setEndpointCertificateConfig(
                  EndpointCertificateConfig.newBuilder()
                      .setCertificateTypeId(certificateType)
                      .build())
              .setSecurityPolicy(policy)
              .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
              .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
              .addTokenPolicies(
                  anonymous,
                  new UserTokenPolicy(
                      "username", UserTokenType.UserName, null, null, policy.getUri()),
                  new UserTokenPolicy(
                      "certificate", UserTokenType.Certificate, null, null, policy.getUri()))
              .build();
      EndpointConfig discovery =
          EndpointConfig.newBuilder()
              .setBindAddress("localhost")
              .setHostname("localhost")
              .setBindPort(discoveryPort)
              .setPath("/discovery")
              .setSecurityPolicy(SecurityPolicy.None)
              .setSecurityMode(MessageSecurityMode.None)
              .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
              .addTokenPolicy(anonymous)
              .build();
      var config =
          OpcUaServerConfig.builder()
              .setApplicationUri(SERVER_URI)
              .setApplicationName(LocalizedText.english("Rotation test server"))
              .setProductUri("urn:milo:rotation")
              .setEndpoints(Set.of(secure, discovery))
              .setCertificateManager(certificateManager)
              .setIdentityValidator(
                  new CompositeValidator(
                      AnonymousIdentityValidator.INSTANCE,
                      new UsernameIdentityValidator(
                          auth ->
                              "user".equals(auth.getUsername())
                                  && "password".equals(auth.getPassword())),
                      new X509IdentityValidator(clientCertificate::equals)))
              .build();
      tcpConfig =
          OpcTcpServerTransportConfig.newBuilder()
              .setMinimumSecureChannelLifetime(uint(1000))
              .setChannelPipelineCustomizer(
                  p -> {
                    p.addFirst(
                        new ChannelInboundHandlerAdapter() {
                          @Override
                          public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            if (((InetSocketAddress) ctx.channel().localAddress()).getPort()
                                == discoveryPort) {
                              discoveryChannels.add(ctx.channel());
                              ctx.channel()
                                  .closeFuture()
                                  .addListener(future -> discoveryChannels.remove(ctx.channel()));
                              if (discoveryUnavailable) {
                                ctx.close();
                                return;
                              }
                            }
                            super.channelActive(ctx);
                          }
                        });
                    p.addFirst(
                        new ChannelOutboundHandlerAdapter() {
                          @Override
                          public void write(
                              ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
                              throws Exception {
                            if (msg instanceof ByteBuf buf
                                && buf.readableBytes() >= 12
                                && buf.getByte(0) == 'E'
                                && buf.getByte(1) == 'R'
                                && buf.getByte(2) == 'R'
                                && buf.getUnsignedIntLE(8) == StatusCodes.Bad_CertificateInvalid) {
                              unknownThumbprints.incrementAndGet();
                              switch (response) {
                                case CERTIFICATE_INVALID -> {}
                                case SECURITY_CHECKS_FAILED ->
                                    buf.setIntLE(8, (int) StatusCodes.Bad_SecurityChecksFailed);
                                case UNEXPECTED_ERROR ->
                                    buf.setIntLE(8, (int) StatusCodes.Bad_UnexpectedError);
                                case SILENT_CLOSE -> {
                                  buf.release();
                                  ctx.close();
                                  promise.setSuccess();
                                  return;
                                }
                                case NO_RESPONSE -> {
                                  buf.release();
                                  // Leave the write pending until the client times out and closes.
                                  ctx.channel()
                                      .closeFuture()
                                      .addListener(f -> promise.trySuccess());
                                  return;
                                }
                              }
                            }
                            super.write(ctx, msg, promise);
                          }
                        });
                  })
              .build();
      server = new OpcUaServer(config, profile -> new OpcTcpServerTransport(tcpConfig));
      server.startup().get(5, TimeUnit.SECONDS);
      discoveryUrl = "opc.tcp://localhost:" + discoveryPort + "/discovery";
      resolver =
          EndpointResolver.create(
              discoveryUrl,
              endpoints ->
                  endpoints.stream()
                      .filter(e -> !noMatchingEndpoint)
                      .filter(e -> policy.getUri().equals(e.getSecurityPolicyUri()))
                      .filter(e -> e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                      .filter(e -> secure.getEndpointUrl().equals(e.getEndpointUrl()))
                      .map(e -> EndpointUtil.updateUrl(e, "localhost"))
                      .findFirst(),
              b -> b.setConnectTimeout(uint(1000)).setAcknowledgeTimeout(uint(1000)),
              Duration.ofSeconds(3));
      // Counts and scripts every resolution, including the one that seeds the configuration.
      EndpointResolver refreshing =
          new EndpointResolver() {
            @Override
            public CompletableFuture<EndpointConfiguration> resolve() {
              discoveries.incrementAndGet();
              return discoveryBehavior.apply(resolver.resolve());
            }

            @Override
            public Duration getTimeout() {
              return resolver.getTimeout();
            }

            // Short enough that the discovery-failure cases recover in seconds, long enough that
            // a refresh cannot start before the test restores discovery.
            @Override
            public Duration getRefreshCooldown() {
              return Duration.ofSeconds(2);
            }
          };
      initial = refreshing.resolve().get(10, TimeUnit.SECONDS);
      var clientGroup = group(clientKey, clientCertificate);
      var clientConfig =
          OpcUaClientConfig.builder()
              .setApplicationUri(CLIENT_URI)
              .setEndpoint(initial.endpoint())
              .setDiscoveryEndpoints(initial.discoveryEndpoints())
              .setEndpointResolver(refreshing)
              .setIdentityProvider(
                  switch (identity) {
                    case "username" -> new UsernameProvider("user", "password");
                    case "certificate" ->
                        new X509IdentityProvider(clientCertificate, clientKey.getPrivate());
                    default -> new AnonymousProvider();
                  })
              .setCertificateGroup(clientGroup)
              .setRequestTimeout(uint(requestTimeout))
              .setSessionEndpointValidationEnabled(true)
              .setKeepAliveInterval(uint(1000))
              .setKeepAliveTimeout(uint(1000))
              .build();
      client =
          OpcUaClient.create(clientConfig, b -> b.setChannelLifetime(uint(renew ? 2000 : 3600000)));
      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionActive(UaSession session) {
              activeSessions.add((OpcUaSession) session);
            }

            @Override
            public void onSessionInactive(UaSession session) {}
          });
    }

    void connect() throws Exception {
      client.connectAsync().get(10, TimeUnit.SECONDS);
      assertNotNull(activeSessions.poll(5, TimeUnit.SECONDS));
      CompletableFuture<?>[] closed =
          discoveryChannels.stream()
              .map(
                  channel -> {
                    var result = new CompletableFuture<Void>();
                    channel.closeFuture().addListener(future -> result.complete(null));
                    return result;
                  })
              .toArray(CompletableFuture[]::new);
      CompletableFuture.allOf(closed).get(3, TimeUnit.SECONDS);
      read();
    }

    Channel channel() throws Exception {
      return ((OpcTcpClientTransport) client.getTransport())
          .getChannelFsm()
          .getChannel()
          .get(5, TimeUnit.SECONDS);
    }

    void read() throws Exception {
      List<DataValue> values =
          client
              .readValuesAsync(
                  0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_CurrentTime))
              .get(5, TimeUnit.SECONDS);
      assertTrue(values.get(0).getStatusCode().isGood());
    }

    void subscribe() throws Exception {
      var subscription = new OpcUaSubscription(client, 100);
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onDataReceived(
                OpcUaSubscription s, List<OpcUaMonitoredItem> items, List<DataValue> values) {
              notifications.addAll(values);
            }
          });
      subscription.create();
      var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
      item.setSamplingInterval(100);
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();
    }

    X509Certificate rotate(boolean sameKey, boolean trusted) throws Exception {
      return rotate(sameKey, trusted, true);
    }

    X509Certificate rotate(boolean sameKey, boolean trusted, boolean invalidateEndpoints)
        throws Exception {
      X509Certificate previous = serverGroup.getCertificateChain(certificateType).orElseThrow()[0];
      if (!sameKey) serverKey = keyPair();
      KeyPair issuerKey = trusted ? caKey : keyPair();
      X509Certificate issuer = trusted ? ca : caCertificate(issuerKey);
      X509Certificate replacement = signedCertificate(serverKey, SERVER_URI, issuerKey, issuer);
      serverGroup.updateCertificate(
          certificateType, serverKey, new X509Certificate[] {replacement, issuer});
      assertTrue(
          server
              .getConfig()
              .getCertificateManager()
              .getCertificate(CertificateUtil.thumbprint(previous))
              .isEmpty());
      if (invalidateEndpoints) server.resetEndpointDescriptionCache();
      return replacement;
    }

    DefaultCertificateGroup group(KeyPair key, X509Certificate certificate) throws Exception {
      var trust = new MemoryTrustListManager();
      trust.setTrustedCertificates(List.of(ca));
      var quarantine =
          new MemoryCertificateQuarantine() {
            @Override
            public void addRejectedCertificate(X509Certificate rejected) {
              rejectedCertificates.add(rejected);
              super.addRejectedCertificate(rejected);
            }
          };
      var validator =
          new DefaultClientCertificateValidator(
              trust,
              EnumSet.of(ValidationCheck.APPLICATION_URI, ValidationCheck.HOSTNAME),
              quarantine);
      var group =
          new DefaultCertificateGroup(
              trust, new MemoryCertificateStore(), quarantine, validator, List.of(certificateType));
      group.updateCertificate(certificateType, key, new X509Certificate[] {certificate, ca});
      return group;
    }

    KeyPair keyPair() throws Exception {
      return policy == SecurityPolicy.Basic256Sha256
          ? SelfSignedCertificateGenerator.generateRsaKeyPair(2048)
          : SelfSignedCertificateGenerator.generateNistP256KeyPair();
    }

    @Override
    public void close() throws Exception {
      try {
        client.disconnectAsync().get(8, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
  }

  private static X509Certificate caCertificate(KeyPair key) throws Exception {
    var name = new X500Name("CN=Rotation test CA");
    var builder =
        new JcaX509v3CertificateBuilder(
            name,
            serial(),
            new Date(System.currentTimeMillis() - 60000),
            new Date(System.currentTimeMillis() + 86400000),
            name,
            key.getPublic());
    builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
    builder.addExtension(
        Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));
    return sign(builder, key);
  }

  private static X509Certificate signedCertificate(
      KeyPair key, String uri, KeyPair caKey, X509Certificate ca) throws Exception {
    SelfSignedCertificateBuilder leafBuilder =
        key.getPublic().getAlgorithm().equals("RSA")
            ? new SelfSignedCertificateBuilder(key)
            : SelfSignedCertificateBuilder.forEccApplicationCertificate(key);
    X509Certificate template =
        leafBuilder
            .setCommonName("Rotation application")
            .setApplicationUri(uri)
            .addDnsName("localhost")
            .addDnsName("advertised.invalid")
            .build();
    var holder = new X509CertificateHolder(template.getEncoded());
    var builder =
        new JcaX509v3CertificateBuilder(
            ca,
            serial(),
            template.getNotBefore(),
            template.getNotAfter(),
            template.getSubjectX500Principal(),
            key.getPublic());
    for (var oid : holder.getExtensions().getExtensionOIDs())
      builder.addExtension(holder.getExtension(oid));
    return sign(builder, caKey);
  }

  private static X509Certificate sign(X509v3CertificateBuilder builder, KeyPair key)
      throws Exception {
    String algorithm =
        key.getPublic().getAlgorithm().equals("RSA") ? "SHA256withRSA" : "SHA256withECDSA";
    return new JcaX509CertificateConverter()
        .getCertificate(
            builder.build(new JcaContentSignerBuilder(algorithm).build(key.getPrivate())));
  }

  private static BigInteger serial() {
    return new BigInteger(120, new SecureRandom());
  }
}
