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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.Channel;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.identity.X509IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.EndpointCertificateConfig;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.X509IdentityValidator;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.sdk.test.DelegatingSessionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.sdk.test.TestPortAllocator;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.EnhancedUserTokenAdditionalHeader;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * End-to-end integration tests for {@link OpcUaClient#changeIdentity(IdentityProvider)} and {@link
 * OpcUaClient#changeIdentityAsync(IdentityProvider)} against a real {@link OpcUaServer}.
 */
class ChangeIdentityIntegrationTest {

  private static final long TIMEOUT_SECONDS = 10L;
  private static final String SERVER_URI = "urn:eclipse:milo:change-identity-test:server";
  private static final String CLIENT_URI = "urn:eclipse:milo:change-identity-test:client";

  private static final Map<CertificateKey, CertificateMaterial> CERTIFICATES =
      new LinkedHashMap<>();

  @Nested
  class EndToEndLifecycle {

    // OPC 10000-4 §5.7.3.1 & §6.1.6: switching Anonymous -> Username -> X509 -> Anonymous must
    // keep the same Session (sessionId and authToken), notify onSessionIdentityChanged on each
    // switch without firing onSessionInactive/onSessionActive, and update the server's identity.
    @Test
    void changesIdentityAcrossAnonymousUsernameX509AndBackOnSameSession() throws Exception {
      TestServer testServer = TestServer.create();
      OpcUaServer server = testServer.getServer();
      server.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      var serverIdentityChanges = new LinkedBlockingQueue<String>();
      server
          .getSessionManager()
          .addSessionListener(
              new SessionListener() {
                @Override
                public void onSessionIdentityChanged(Session session) {
                  serverIdentityChanges.add(
                      session.getClientUserId() != null
                          ? session.getClientUserId()
                          : "<anonymous>");
                }
              });

      OpcUaClient client = createSecureClient(testServer, AnonymousProvider.INSTANCE);
      var clientActiveCount = new AtomicInteger();
      var clientInactiveCount = new AtomicInteger();
      var clientIdentityChanged = new LinkedBlockingQueue<NodeId>();
      var clientAttemptFailed = new AtomicInteger();

      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionActive(UaSession session) {
              clientActiveCount.incrementAndGet();
            }

            @Override
            public void onSessionInactive(UaSession session) {
              clientInactiveCount.incrementAndGet();
            }

            @Override
            public void onSessionIdentityChanged(UaSession session) {
              clientIdentityChanged.add(session.getSessionId());
            }

            @Override
            public void onSessionAttemptFailed(AttemptStep step, Throwable failure) {
              clientAttemptFailed.incrementAndGet();
            }
          });

      try {
        client.connect();
        OpcUaSession initialSession = client.getSession();
        NodeId initialSessionId = initialSession.getSessionId();
        NodeId initialAuthToken = initialSession.getAuthenticationToken();

        // Anonymous -> Username
        OpcUaSession afterUser = client.changeIdentity(new UsernameProvider("user1", "password"));
        assertEquals(initialSessionId, afterUser.getSessionId());
        assertEquals(initialAuthToken, afterUser.getAuthenticationToken());
        assertEquals(
            initialSessionId, clientIdentityChanged.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        assertEquals("user1", serverIdentityChanges.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS));

        // Username -> X509
        TestServer.TestIdentityCertificate cert1 = testServer.getIdentityCertificate1();
        OpcUaSession afterX509 =
            client.changeIdentity(
                new X509IdentityProvider(cert1.certificate(), cert1.keyPair().getPrivate()));
        assertEquals(initialSessionId, afterX509.getSessionId());
        assertEquals(initialAuthToken, afterX509.getAuthenticationToken());
        assertEquals(
            initialSessionId, clientIdentityChanged.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        assertEquals(
            cert1.certificate().getSubjectX500Principal().getName(),
            serverIdentityChanges.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS));

        // X509 -> Anonymous
        OpcUaSession afterAnon = client.changeIdentity(AnonymousProvider.INSTANCE);
        assertEquals(initialSessionId, afterAnon.getSessionId());
        assertEquals(initialAuthToken, afterAnon.getAuthenticationToken());
        assertEquals(
            initialSessionId, clientIdentityChanged.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        assertEquals("<anonymous>", serverIdentityChanges.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS));

        // None of the identity changes may tear down or re-activate the Session lifecycle.
        assertEquals(1, clientActiveCount.get(), "onSessionActive must fire only once");
        assertEquals(0, clientInactiveCount.get(), "onSessionInactive must not fire");
        assertEquals(0, clientAttemptFailed.get(), "onSessionAttemptFailed must not fire");
      } finally {
        disconnectQuietly(client);
        server.shutdown().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }

    // OPC 10000-4 §5.7.3.1: Subscriptions and MonitoredItems stay attached to the Session across
    // an identity change while the Server re-evaluates permissions on all MonitoredItems.
    @Test
    void preservesSubscriptionAndReEvaluatesMonitoredItemPermissions() throws Exception {
      TestServer testServer = TestServer.create();
      OpcUaServer server = testServer.getServer();
      TestNamespace namespace = new TestNamespace(server);
      namespace.startup();

      NodeId restrictedNodeId = new NodeId(namespace.getNamespaceIndex(), "RestrictedInt32");
      UaVariableNode restrictedNode =
          new UaVariableNode.UaVariableNodeBuilder(namespace.getNodeContext())
              .setNodeId(restrictedNodeId)
              .setAccessLevel(AccessLevel.READ_WRITE)
              .setUserAccessLevel(AccessLevel.READ_WRITE)
              .setBrowseName(new QualifiedName(namespace.getNamespaceIndex(), "RestrictedInt32"))
              .setDisplayName(LocalizedText.english("RestrictedInt32"))
              .setDataType(NodeIds.Int32)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .build();
      restrictedNode.setValue(new DataValue(Variant.ofInt32(42)));
      restrictedNode
          .getFilterChain()
          .addLast(
              new AttributeFilter() {
                @Override
                public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                    throws UaException {
                  if (attributeId == AttributeId.UserAccessLevel) {
                    boolean authenticated =
                        ctx.getSession()
                            .map(Session::getIdentity)
                            .map(id -> id instanceof Identity.UsernameIdentity)
                            .orElse(false);
                    return authenticated
                        ? AccessLevel.toValue(AccessLevel.READ_WRITE)
                        : AccessLevel.toValue(AccessLevel.NONE);
                  }
                  return ctx.readAttribute(attributeId);
                }
              });
      namespace.getNodeManager().addNode(restrictedNode);

      server.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      OpcUaClient client =
          createSecureClient(testServer, new UsernameProvider("user1", "password"));

      try {
        client.connect();
        NodeId sessionId = client.getSession().getSessionId();

        var subscription = new OpcUaSubscription(client);
        subscription.setPublishingInterval(100.0);
        subscription.create();
        UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();

        BlockingQueue<DataValue> notifications = new LinkedBlockingQueue<>();
        OpcUaMonitoredItem item = OpcUaMonitoredItem.newDataItem(restrictedNodeId);
        item.setSamplingInterval(50.0);
        item.setDataValueListener((monitoredItem, value) -> notifications.add(value));
        subscription.addMonitoredItem(item);
        subscription.synchronizeMonitoredItems();

        // Initial notification under user1 carries Good value 42.
        DataValue initial = notifications.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNotNull(initial, "expected initial notification for authorized user1");
        assertTrue(initial.statusCode().isGood());
        assertEquals(42, initial.value().value());

        // Switch to Anonymous: Subscription stays intact, MonitoredItem receives
        // Bad_UserAccessDenied after server re-evaluates read access.
        client.changeIdentity(AnonymousProvider.INSTANCE);
        assertEquals(sessionId, client.getSession().getSessionId());
        assertEquals(Optional.of(subscriptionId), subscription.getSubscriptionId());

        DataValue denied =
            awaitNotificationMatching(
                notifications,
                v -> v.statusCode().value() == StatusCodes.Bad_UserAccessDenied,
                TIMEOUT_SECONDS);
        assertNotNull(denied, "expected Bad_UserAccessDenied notification after switching to anon");
        assertEquals(
            StatusCodes.Bad_UserAccessDenied,
            client
                .readValue(0.0, TimestampsToReturn.Neither, restrictedNodeId)
                .statusCode()
                .value(),
            "direct Read under Anonymous must also fail with Bad_UserAccessDenied");

        // Switch back to user1: same Subscription and MonitoredItem resume delivering Good values.
        client.changeIdentity(new UsernameProvider("user1", "password"));
        restrictedNode.setValue(new DataValue(Variant.ofInt32(99)));

        DataValue restored =
            awaitNotificationMatching(
                notifications,
                v -> v.statusCode().isGood() && Integer.valueOf(99).equals(v.value().value()),
                TIMEOUT_SECONDS);
        assertNotNull(restored, "expected Good value 99 after switching back to user1");
        assertEquals(sessionId, client.getSession().getSessionId());
        assertEquals(Optional.of(subscriptionId), subscription.getSubscriptionId());
      } finally {
        disconnectQuietly(client);
        namespace.shutdown();
        server.shutdown().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }

    // Advisory service results such as Good_PasswordChangeRequired returned by ActivateSession
    // during changeIdentity must be exposed on the returned OpcUaSession.
    @Test
    void surfacesGoodPasswordChangeRequiredViaLastActivateSessionServiceResult() throws Exception {
      TestServer testServer = TestServer.create();
      OpcUaServer server = testServer.getServer();

      var activationCount = new AtomicInteger();
      var serviceSet =
          new DelegatingSessionServiceSet(server) {
            @Override
            public ActivateSessionResponse onActivateSession(
                ServiceRequestContext context, ActivateSessionRequest request) throws UaException {
              ActivateSessionResponse response = super.onActivateSession(context, request);
              if (activationCount.incrementAndGet() == 2) {
                return withServiceResult(
                    response, new StatusCode(StatusCodes.Good_PasswordChangeRequired));
              }
              return response;
            }
          };

      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), serviceSet);
      }

      server.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      OpcUaClient client = createSecureClient(testServer, AnonymousProvider.INSTANCE);

      try {
        client.connect();
        assertEquals(
            StatusCodes.Good,
            client.getSession().getLastActivateSessionServiceResult().orElseThrow().value());

        OpcUaSession updated = client.changeIdentity(new UsernameProvider("user1", "password"));
        assertEquals(
            StatusCodes.Good_PasswordChangeRequired,
            updated.getLastActivateSessionServiceResult().orElseThrow().value());
        assertEquals(
            StatusCodes.Good_PasswordChangeRequired,
            client.getSession().getLastActivateSessionServiceResult().orElseThrow().value());
      } finally {
        disconnectQuietly(client);
        server.shutdown().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }
  }

  @Nested
  class RefusalsAndStickyLifetime {

    // A successful changeIdentity is sticky for the client's remaining lifetime: channel-loss
    // reactivation uses the new identity (passing the server's sameIdentity check on the same
    // Session), and disconnect/reconnect uses it on the new Session, while OpcUaClientConfig stays
    // immutable.
    @Test
    void stickyIdentitySurvivesChannelDropAndDisconnectReconnectWhileConfigStaysImmutable()
        throws Exception {

      TestServer testServer = TestServer.create();
      OpcUaServer server = testServer.getServer();
      server.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      var lastServerUserId = new AtomicReference<String>();
      server
          .getSessionManager()
          .addSessionListener(
              new SessionListener() {
                @Override
                public void onSessionCreated(Session session) {
                  lastServerUserId.set(session.getClientUserId());
                }

                @Override
                public void onSessionIdentityChanged(Session session) {
                  lastServerUserId.set(session.getClientUserId());
                }
              });

      OpcUaClient client = createSecureClient(testServer, AnonymousProvider.INSTANCE);
      var activeLatch = new CountDownLatch(2);
      var identityChangedCount = new AtomicInteger();

      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionActive(UaSession session) {
              activeLatch.countDown();
            }

            @Override
            public void onSessionIdentityChanged(UaSession session) {
              identityChangedCount.incrementAndGet();
            }
          });

      try {
        client.connect();
        NodeId initialSessionId = client.getSession().getSessionId();

        client.changeIdentity(new UsernameProvider("user1", "password"));
        assertEquals(
            AnonymousProvider.INSTANCE,
            client.getConfig().getIdentityProvider(),
            "OpcUaClientConfig must remain immutable after changeIdentity");

        // Drop the TCP channel and verify Reactivating reuses the SAME Session under user1.
        closeActiveTcpChannel(client);
        assertTrue(
            activeLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS),
            "session did not reactivate after channel drop");

        assertEquals(
            initialSessionId,
            client.getSession().getSessionId(),
            "reactivation must use the sticky new identity and keep the same Session");
        assertEquals(
            1,
            identityChangedCount.get(),
            "reactivation must not fire onSessionIdentityChanged again");

        // Disconnect and reconnect: new Session must authenticate as user1 even though config still
        // holds AnonymousProvider.INSTANCE.
        client.disconnect();
        client.connect();

        NodeId reconnectedSessionId = client.getSession().getSessionId();
        assertNotEquals(initialSessionId, reconnectedSessionId);
        Session serverSession =
            server.getSessionManager().getAllSessions().stream()
                .filter(s -> s.getSessionId().equals(reconnectedSessionId))
                .findFirst()
                .orElseThrow();
        assertEquals("user1", serverSession.getClientUserId());
        assertEquals(AnonymousProvider.INSTANCE, client.getConfig().getIdentityProvider());
      } finally {
        disconnectQuietly(client);
        server.shutdown().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }

    // Refused identity changes (wrong credentials, untrusted certificate,
    // Bad_IdentityChangeNotSupported) must fail the call with the server's StatusCode, preserve
    // the previous identity and Active Session without firing lifecycle failure/inactive hooks,
    // and allow a subsequent valid identity change.
    @Test
    void refusedChangesPreserveSessionAndPreviousIdentityAndAllowSubsequentChange()
        throws Exception {

      TestServer testServer = TestServer.create();
      OpcUaServer server = testServer.getServer();

      var rejectWithNotSupported = new AtomicBoolean(false);
      var rejectWithUserAccessDenied = new AtomicBoolean(false);
      var serviceSet =
          new DelegatingSessionServiceSet(server) {
            @Override
            public ActivateSessionResponse onActivateSession(
                ServiceRequestContext context, ActivateSessionRequest request) throws UaException {
              if (rejectWithNotSupported.get()) {
                throw new UaException(StatusCodes.Bad_IdentityChangeNotSupported);
              }
              if (rejectWithUserAccessDenied.get()) {
                throw new UaException(StatusCodes.Bad_UserAccessDenied);
              }
              return super.onActivateSession(context, request);
            }
          };

      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), serviceSet);
      }

      server.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      OpcUaClient client =
          createSecureClient(testServer, new UsernameProvider("user1", "password"));
      var activeCount = new AtomicInteger();
      var inactiveCount = new AtomicInteger();
      var identityChangedCount = new AtomicInteger();
      var attemptFailedCount = new AtomicInteger();

      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionActive(UaSession session) {
              activeCount.incrementAndGet();
            }

            @Override
            public void onSessionInactive(UaSession session) {
              inactiveCount.incrementAndGet();
            }

            @Override
            public void onSessionIdentityChanged(UaSession session) {
              identityChangedCount.incrementAndGet();
            }

            @Override
            public void onSessionAttemptFailed(AttemptStep step, Throwable failure) {
              attemptFailedCount.incrementAndGet();
            }
          });

      try {
        client.connect();
        NodeId sessionId = client.getSession().getSessionId();

        // 1. Bad_UserAccessDenied from server.
        rejectWithUserAccessDenied.set(true);
        UaException denied =
            assertThrows(
                UaException.class,
                () -> client.changeIdentity(new UsernameProvider("user2", "password")));
        assertEquals(StatusCodes.Bad_UserAccessDenied, denied.getStatusCode().value());
        rejectWithUserAccessDenied.set(false);

        // 2. Bad_IdentityTokenRejected / Bad_UserAccessDenied (untrusted X509 cert).
        KeyPair untrustedKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
        X509Certificate untrustedCert =
            new SelfSignedCertificateBuilder(untrustedKeyPair)
                .setCommonName("UntrustedUser")
                .setApplicationUri("urn:eclipse:milo:test:untrusted-user")
                .build();
        UaException certRejected =
            assertThrows(
                UaException.class,
                () ->
                    client.changeIdentity(
                        new X509IdentityProvider(untrustedCert, untrustedKeyPair.getPrivate())));
        assertEquals(StatusCodes.Bad_IdentityTokenRejected, certRejected.getStatusCode().value());

        // 3. Bad_IdentityChangeNotSupported from server.
        rejectWithNotSupported.set(true);
        UaException notSupported =
            assertThrows(
                UaException.class,
                () -> client.changeIdentity(new UsernameProvider("user2", "password")));
        assertEquals(
            StatusCodes.Bad_IdentityChangeNotSupported, notSupported.getStatusCode().value());
        rejectWithNotSupported.set(false);

        // Session remained Active under user1 throughout all three refusals.
        assertEquals(State.Active, client.getSessionFsm().getState());
        assertEquals(sessionId, client.getSession().getSessionId());
        assertEquals(1, activeCount.get());
        assertEquals(0, inactiveCount.get());
        assertEquals(0, identityChangedCount.get());
        assertEquals(0, attemptFailedCount.get());

        Session serverSession =
            server.getSessionManager().getAllSessions().stream()
                .filter(s -> s.getSessionId().equals(sessionId))
                .findFirst()
                .orElseThrow();
        assertEquals("user1", serverSession.getClientUserId());

        // Subsequent valid change succeeds on the same Session.
        OpcUaSession changed = client.changeIdentity(new UsernameProvider("user2", "password"));
        assertEquals(sessionId, changed.getSessionId());
        assertEquals("user2", serverSession.getClientUserId());
      } finally {
        disconnectQuietly(client);
        server.shutdown().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }
  }

  @Nested
  class EnhancedUserTokens {

    // OPC 10000-6 §6.8.2: changing from Anonymous (no EphemeralKey negotiated at CreateSession) to
    // an enhanced ECC or RSA-DH UsernameProvider must run a two-step ActivateSession: step 1
    // re-authenticates as Anonymous requesting ECDHPolicyUri, and step 2 encrypts the password
    // with the returned EphemeralKey and requests rotation for subsequent reactivations.
    @ParameterizedTest
    @MethodSource("enhancedPolicies")
    void anonymousToEnhancedUsernameUsesTwoStepNegotiationAndSurvivesChannelDrop(
        SecurityPolicy policy, NodeId certificateTypeId) throws Exception {

      List<ActivateSessionRequest> capturedActivations =
          Collections.synchronizedList(new ArrayList<>());

      try (RunningEnhancedServer running =
          startEnhancedServer(
              policy,
              certificateTypeId,
              List.of(anonymousPolicy(), usernamePolicy("username-" + policy.name(), policy)),
              server -> recordActivateSessions(server, capturedActivations))) {

        CertificateGroup clientGroup =
            certificateGroup(certificate(certificateTypeId, "client", CLIENT_URI));
        OpcUaClient client =
            createEnhancedClient(running, policy, AnonymousProvider.INSTANCE, clientGroup);

        var activeLatch = new CountDownLatch(2);
        var identityChangedCount = new AtomicInteger();
        client.addSessionActivityListener(
            new SessionActivityListener() {
              @Override
              public void onSessionActive(UaSession session) {
                activeLatch.countDown();
              }

              @Override
              public void onSessionIdentityChanged(UaSession session) {
                identityChangedCount.incrementAndGet();
              }
            });

        try {
          client.connect();
          NodeId sessionId = client.getSession().getSessionId();
          assertEquals(1, capturedActivations.size(), "initial connect sends 1 ActivateSession");

          // Anonymous -> Enhanced Username: requires 2 ActivateSession requests (pre-negotiation +
          // identity change).
          OpcUaSession updated = client.changeIdentity(new UsernameProvider("user1", "password"));
          assertEquals(sessionId, updated.getSessionId());
          assertEquals(
              3,
              capturedActivations.size(),
              "expected 1 initial + 2 (pre-negotiate + change) ActivateSession requests");
          assertEquals(
              1,
              identityChangedCount.get(),
              "two-step E1 change must fire onSessionIdentityChanged exactly once");

          // Verify step 1 requested ECDHPolicyUri for the target policy.
          var step1Req =
              EnhancedUserTokenAdditionalHeader.decodeRequest(
                  running.server().getStaticEncodingContext(),
                  capturedActivations.get(1).getRequestHeader().getAdditionalHeader());
          var supported =
              assertInstanceOf(
                  EnhancedUserTokenAdditionalHeader.NegotiationRequest.Supported.class, step1Req);
          assertEquals(policy, supported.securityPolicy());

          // Drop TCP channel: reactivation uses the rotated EphemeralKey and sticky user1 on the
          // same Session.
          closeActiveTcpChannel(client);
          assertTrue(
              activeLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS),
              "enhanced username session did not reactivate after channel drop");
          assertEquals(sessionId, client.getSession().getSessionId());
        } finally {
          disconnectQuietly(client);
        }
      }
    }

    // When step 2 fails with wrong credentials against Milo's server, the single-use EphemeralKey
    // is cleared on the server in finally while step 1's serverNonce was already committed. The
    // client must keep step 1's nonce and discard the burned key so a subsequent retry succeeds.
    @Test
    void failedEnhancedChangeDiscardsBurnedKeyAndKeepsNonceInSyncForRetry() throws Exception {
      SecurityPolicy policy = SecurityPolicy.ECC_nistP256_AesGcm;
      NodeId certificateTypeId = NodeIds.EccNistP256ApplicationCertificateType;

      try (RunningEnhancedServer running =
          startEnhancedServer(
              policy,
              certificateTypeId,
              List.of(anonymousPolicy(), usernamePolicy("username-ecc", policy)),
              server -> {})) {

        CertificateGroup clientGroup =
            certificateGroup(certificate(certificateTypeId, "client", CLIENT_URI));
        OpcUaClient client =
            createEnhancedClient(running, policy, AnonymousProvider.INSTANCE, clientGroup);

        try {
          client.connect();
          NodeId sessionId = client.getSession().getSessionId();

          // Wrong password burns the EphemeralKey negotiated in step 1 on the server.
          UaException failed =
              assertThrows(
                  UaException.class,
                  () -> client.changeIdentity(new UsernameProvider("user1", "wrong-password")));
          assertEquals(StatusCodes.Bad_IdentityTokenInvalid, failed.getStatusCode().value());

          // Retry with valid credentials must re-negotiate a fresh key using step 1's updated
          // serverNonce and succeed on the same Session.
          OpcUaSession updated = client.changeIdentity(new UsernameProvider("user1", "password"));
          assertEquals(sessionId, updated.getSessionId());
        } finally {
          disconnectQuietly(client);
        }
      }
    }

    // Switching from one enhanced user-token policy (P1) to a different enhanced policy (P2)
    // requires E1 step 1 under the current identity (consuming P1's key and requesting P2's key)
    // followed by step 2 under the new identity encrypted with P2's key.
    @Test
    void switchesBetweenDifferentEnhancedUserTokenPolicies() throws Exception {
      SecurityPolicy policy1 = SecurityPolicy.ECC_nistP256_AesGcm;
      SecurityPolicy policy2 = SecurityPolicy.ECC_nistP256_ChaChaPoly;
      NodeId certificateTypeId = NodeIds.EccNistP256ApplicationCertificateType;

      List<ActivateSessionRequest> capturedActivations =
          Collections.synchronizedList(new ArrayList<>());

      try (RunningEnhancedServer running =
          startEnhancedServer(
              policy1,
              certificateTypeId,
              List.of(
                  anonymousPolicy(),
                  usernamePolicy("username-aesgcm", policy1),
                  usernamePolicy("username-chachapoly", policy2)),
              server -> recordActivateSessions(server, capturedActivations))) {

        CertificateGroup clientGroup =
            certificateGroup(certificate(certificateTypeId, "client", CLIENT_URI));
        UsernameProvider providerP1 =
            new UsernameProvider(
                "user1",
                "password",
                null,
                policies ->
                    policies.stream()
                        .filter(p -> policy1.getUri().equals(p.getSecurityPolicyUri()))
                        .findFirst()
                        .orElseThrow());
        UsernameProvider providerP2 =
            new UsernameProvider(
                "user2",
                "password",
                null,
                policies ->
                    policies.stream()
                        .filter(p -> policy2.getUri().equals(p.getSecurityPolicyUri()))
                        .findFirst()
                        .orElseThrow());

        OpcUaClient client = createEnhancedClient(running, policy1, providerP1, clientGroup);

        try {
          client.connect();
          NodeId sessionId = client.getSession().getSessionId();
          assertEquals(1, capturedActivations.size());

          // Switch from P1 -> P2: session holds a key for P1, not P2, so E1 runs 2 steps.
          OpcUaSession updated = client.changeIdentity(providerP2);
          assertEquals(sessionId, updated.getSessionId());
          assertEquals(
              3,
              capturedActivations.size(),
              "switching from enhanced policy P1 to P2 must run two-step E1 negotiation");

          Session serverSession =
              running.server().getSessionManager().getAllSessions().stream()
                  .filter(s -> s.getSessionId().equals(sessionId))
                  .findFirst()
                  .orElseThrow();
          assertEquals("user2", serverSession.getClientUserId());
        } finally {
          disconnectQuietly(client);
        }
      }
    }

    // Switching Enhanced -> Anonymous clears the cached ephemeral key so switching back
    // Anonymous -> Enhanced runs E1 pre-negotiation cleanly even on servers that drop ephemeral
    // keys for non-enhanced sessions.
    @Test
    void enhancedToAnonymousAndBackToEnhancedReNegotiatesFreshKey() throws Exception {
      SecurityPolicy policy = SecurityPolicy.ECC_nistP256_AesGcm;
      NodeId certificateTypeId = NodeIds.EccNistP256ApplicationCertificateType;

      List<ActivateSessionRequest> capturedActivations =
          Collections.synchronizedList(new ArrayList<>());

      try (RunningEnhancedServer running =
          startEnhancedServer(
              policy,
              certificateTypeId,
              List.of(anonymousPolicy(), usernamePolicy("username-ecc", policy)),
              server -> recordActivateSessions(server, capturedActivations))) {

        CertificateGroup clientGroup =
            certificateGroup(certificate(certificateTypeId, "client", CLIENT_URI));
        OpcUaClient client =
            createEnhancedClient(
                running, policy, new UsernameProvider("user1", "password"), clientGroup);

        try {
          client.connect();
          NodeId sessionId = client.getSession().getSessionId();
          assertEquals(1, capturedActivations.size());

          // Enhanced -> Anonymous (1 step, clears client ephemeral key).
          client.changeIdentity(AnonymousProvider.INSTANCE);
          assertEquals(2, capturedActivations.size());
          assertTrue(
              client.getSession().getUserTokenReceiverEphemeralPublicKey().isEmpty(),
              "switching to non-enhanced identity must clear cached ephemeral key");

          // Anonymous -> Enhanced (2 steps: pre-negotiate fresh key + activate as user2).
          OpcUaSession updated = client.changeIdentity(new UsernameProvider("user2", "password"));
          assertEquals(sessionId, updated.getSessionId());
          assertEquals(4, capturedActivations.size());

          Session serverSession =
              running.server().getSessionManager().getAllSessions().stream()
                  .filter(s -> s.getSessionId().equals(sessionId))
                  .findFirst()
                  .orElseThrow();
          assertEquals("user2", serverSession.getClientUserId());
        } finally {
          disconnectQuietly(client);
        }
      }
    }

    private static Stream<Arguments> enhancedPolicies() {
      return Stream.of(
          Arguments.of(
              SecurityPolicy.ECC_nistP256_AesGcm, NodeIds.EccNistP256ApplicationCertificateType),
          Arguments.of(SecurityPolicy.RSA_DH_AesGcm, NodeIds.RsaSha256ApplicationCertificateType));
    }
  }

  // region Helpers

  private static OpcUaClient createSecureClient(
      TestServer testServer, IdentityProvider initialProvider) throws Exception {

    OpcUaServer server = testServer.getServer();
    EndpointConfig secureEndpoint =
        server.getConfig().getEndpoints().stream()
            .filter(
                e ->
                    e.getSecurityPolicy() == SecurityPolicy.Basic256Sha256
                        && e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
            .findFirst()
            .orElseThrow();

    return OpcUaClient.create(
        secureEndpoint.getEndpointUrl(),
        endpoints ->
            endpoints.stream()
                .filter(
                    e ->
                        SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri())
                            && e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                .findFirst(),
        transportConfigBuilder -> {},
        cfg ->
            cfg.setApplicationName(LocalizedText.english("eclipse milo test client"))
                .setApplicationUri("urn:eclipse:milo:test:reverse:client")
                .setCertificateIdentity(
                    testServer.getClientKeyPair(), testServer.getClientCertificateChain())
                .setCertificateValidator(new CertificateValidator.InsecureCertificateValidator())
                .setIdentityProvider(initialProvider)
                .setRequestTimeout(uint(5_000)));
  }

  private static DataValue awaitNotificationMatching(
      BlockingQueue<DataValue> queue,
      java.util.function.Predicate<DataValue> predicate,
      long timeoutSeconds)
      throws InterruptedException {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(timeoutSeconds);
    while (System.nanoTime() < deadline) {
      long remainingMillis =
          Math.max(1L, TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime()));
      DataValue value = queue.poll(remainingMillis, TimeUnit.MILLISECONDS);
      if (value != null && predicate.test(value)) {
        return value;
      }
    }
    return null;
  }

  private static ActivateSessionResponse withServiceResult(
      ActivateSessionResponse response, StatusCode serviceResult) {

    var header = response.getResponseHeader();
    return new ActivateSessionResponse(
        new ResponseHeader(
            header.getTimestamp(),
            header.getRequestHandle(),
            serviceResult,
            header.getServiceDiagnostics(),
            header.getStringTable(),
            header.getAdditionalHeader()),
        response.getServerNonce(),
        response.getResults(),
        response.getDiagnosticInfos());
  }

  private static void recordActivateSessions(
      OpcUaServer server, List<ActivateSessionRequest> captured) {
    var serviceSet =
        new DelegatingSessionServiceSet(server) {
          @Override
          public ActivateSessionResponse onActivateSession(
              ServiceRequestContext context, ActivateSessionRequest request) throws UaException {
            captured.add(request);
            return super.onActivateSession(context, request);
          }
        };
    for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
      server.addServiceSet(endpoint.getPath(), serviceSet);
    }
  }

  private static RunningEnhancedServer startEnhancedServer(
      SecurityPolicy securityPolicy,
      NodeId certificateTypeId,
      List<UserTokenPolicy> tokenPolicies,
      java.util.function.Consumer<OpcUaServer> configureServer)
      throws Exception {

    int port = TestPortAllocator.allocatePort();
    CertificateMaterial serverCert = certificate(certificateTypeId, "server", SERVER_URI);
    CertificateManager certificateManager =
        new DefaultCertificateManager(certificateGroup(serverCert));

    Set<EndpointConfig> endpoints = new LinkedHashSet<>();
    EndpointConfig.Builder endpointBuilder =
        EndpointConfig.newBuilder()
            .setBindAddress("127.0.0.1")
            .setHostname("localhost")
            .setBindPort(port)
            .setPath("/enhanced")
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            .setSecurityPolicy(securityPolicy)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .setEndpointCertificateConfig(
                EndpointCertificateConfig.newBuilder()
                    .setCertificateTypeId(certificateTypeId)
                    .build());
    for (UserTokenPolicy tokenPolicy : tokenPolicies) {
      endpointBuilder.addTokenPolicy(tokenPolicy);
    }
    endpoints.add(endpointBuilder.build());

    endpoints.add(
        EndpointConfig.newBuilder()
            .setBindAddress("127.0.0.1")
            .setHostname("localhost")
            .setBindPort(port)
            .setPath("/enhanced/discovery")
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .addTokenPolicy(anonymousPolicy())
            .build());

    UsernameIdentityValidator usernameValidator =
        new UsernameIdentityValidator(
            challenge ->
                ("user1".equals(challenge.getUsername()) || "user2".equals(challenge.getUsername()))
                    && "password".equals(challenge.getPassword()));

    OpcUaServerConfig serverConfig =
        OpcUaServerConfig.builder()
            .setApplicationUri(SERVER_URI)
            .setApplicationName(LocalizedText.english("Change Identity Enhanced Test Server"))
            .setProductUri("urn:eclipse:milo:change-identity-test:product")
            .setEndpoints(endpoints)
            .setCertificateManager(certificateManager)
            .setIdentityValidator(
                new CompositeValidator(
                    AnonymousIdentityValidator.INSTANCE,
                    usernameValidator,
                    new X509IdentityValidator(cert -> true)))
            .build();

    OpcUaServer server =
        new OpcUaServer(
            serverConfig,
            transportProfile -> {
              if (transportProfile == TransportProfile.TCP_UASC_UABINARY) {
                return new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build());
              }
              throw new IllegalArgumentException("unexpected transport: " + transportProfile);
            });

    configureServer.accept(server);
    server.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

    String endpointUrl =
        endpoints.stream()
            .filter(e -> !e.getEndpointUrl().endsWith("/discovery"))
            .findFirst()
            .orElseThrow()
            .getEndpointUrl();

    return new RunningEnhancedServer(server, endpointUrl);
  }

  private static OpcUaClient createEnhancedClient(
      RunningEnhancedServer running,
      SecurityPolicy securityPolicy,
      IdentityProvider identityProvider,
      CertificateGroup clientCertificateGroup)
      throws Exception {

    return OpcUaClient.create(
        running.endpointUrl(),
        endpoints ->
            endpoints.stream()
                .filter(
                    e ->
                        securityPolicy.getUri().equals(e.getSecurityPolicyUri())
                            && e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                .findFirst(),
        transportConfigBuilder -> {},
        cfg ->
            cfg.setApplicationName(LocalizedText.english("Change Identity Enhanced Test Client"))
                .setApplicationUri(CLIENT_URI)
                .setCertificateGroup(clientCertificateGroup)
                .setCertificateValidator(new CertificateValidator.InsecureCertificateValidator())
                .setIdentityProvider(identityProvider)
                .setRequestTimeout(uint(5_000)));
  }

  private static UserTokenPolicy anonymousPolicy() {
    return new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);
  }

  private static UserTokenPolicy usernamePolicy(String policyId, SecurityPolicy securityPolicy) {
    return new UserTokenPolicy(
        policyId, UserTokenType.UserName, null, null, securityPolicy.getUri());
  }

  private static synchronized CertificateMaterial certificate(
      NodeId certificateTypeId, String commonName, String applicationUri) throws Exception {

    CertificateKey key = new CertificateKey(certificateTypeId, commonName, applicationUri);
    CertificateMaterial existing = CERTIFICATES.get(key);
    if (existing != null) {
      return existing;
    }

    KeyPair keyPair;
    SelfSignedCertificateBuilder builder;
    if (NodeIds.EccNistP256ApplicationCertificateType.equals(certificateTypeId)) {
      keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
      builder = SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair);
    } else if (NodeIds.RsaSha256ApplicationCertificateType.equals(certificateTypeId)) {
      keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      builder = new SelfSignedCertificateBuilder(keyPair);
    } else {
      throw new IllegalArgumentException("certificateTypeId: " + certificateTypeId);
    }

    X509Certificate cert =
        builder
            .setCommonName(commonName)
            .setOrganization("Eclipse Milo")
            .setApplicationUri(applicationUri)
            .addDnsName("localhost")
            .build();

    CertificateMaterial created =
        new CertificateMaterial(certificateTypeId, keyPair, new X509Certificate[] {cert});
    CERTIFICATES.put(key, created);
    return created;
  }

  private static CertificateGroup certificateGroup(CertificateMaterial... certificates) {
    return new TestCertificateGroup(List.of(certificates));
  }

  private static void closeActiveTcpChannel(OpcUaClient client) throws Exception {
    OpcTcpClientTransport transport = (OpcTcpClientTransport) client.getTransport();
    Channel channel = transport.getChannelFsm().getChannel().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    channel.close().sync();
  }

  private static void disconnectQuietly(OpcUaClient client) {
    try {
      client.disconnectAsync().get(2, TimeUnit.SECONDS);
    } catch (Exception ignored) {
      // Best-effort cleanup.
    }
  }

  private record RunningEnhancedServer(OpcUaServer server, String endpointUrl)
      implements AutoCloseable {
    @Override
    public void close() throws Exception {
      server.shutdown().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }
  }

  private record CertificateKey(
      NodeId certificateTypeId, String commonName, String applicationUri) {}

  private record CertificateMaterial(
      NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {}

  private record TestCertificateGroup(Map<NodeId, CertificateMaterial> certificates)
      implements CertificateGroup {

    private TestCertificateGroup(List<CertificateMaterial> certificates) {
      this(
          Collections.unmodifiableMap(
              certificates.stream()
                  .collect(
                      Collectors.toMap(
                          CertificateMaterial::certificateTypeId,
                          Function.identity(),
                          (left, right) -> right,
                          LinkedHashMap::new))));
    }

    @Override
    public List<NodeId> getSupportedCertificateTypeIds() {
      return List.copyOf(certificates.keySet());
    }

    @Override
    public TrustListManager getTrustListManager() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateQuarantine getCertificateQuarantine() {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Entry> getCertificateEntries() {
      return certificates.values().stream()
          .map(c -> new Entry(c.certificateTypeId(), c.certificateChain()))
          .toList();
    }

    @Override
    public boolean hasCertificate(NodeId certificateTypeId) {
      return certificates.containsKey(certificateTypeId);
    }

    @Override
    public Optional<KeyPair> getKeyPair(NodeId certificateTypeId) {
      return Optional.ofNullable(certificates.get(certificateTypeId))
          .map(CertificateMaterial::keyPair);
    }

    @Override
    public Optional<X509Certificate[]> getCertificateChain(NodeId certificateTypeId) {
      return Optional.ofNullable(certificates.get(certificateTypeId))
          .map(CertificateMaterial::certificateChain)
          .map(X509Certificate[]::clone);
    }

    @Override
    public void updateCertificate(
        NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateValidator getCertificateValidator() {
      return new CertificateValidator.InsecureCertificateValidator();
    }
  }

  // endregion
}
