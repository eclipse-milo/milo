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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import io.netty.channel.Channel;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.milo.opcua.sdk.server.access.AccessControlManager;
import org.eclipse.milo.opcua.sdk.server.access.AccessController;
import org.eclipse.milo.opcua.sdk.server.access.ReadAccessListener;
import org.eclipse.milo.opcua.sdk.server.access.ReadAccessScope;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransportFactory;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * How {@link AccessControlManager} creates the controller, owns the cache, and announces
 * invalidations.
 */
class AccessControlManagerTest {

  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840/test";

  private static final OpcServerTransportFactory NO_OP_TRANSPORTS =
      transportProfile ->
          new OpcServerTransport() {
            @Override
            public void bind(
                ServerApplicationContext applicationContext, InetSocketAddress bindAddress) {}

            @Override
            public void unbind() {}
          };

  private final ReadValueId serverStatus =
      new ReadValueId(new NodeId(0, 2256), AttributeId.Value.uid(), null, null);

  private OpcUaServer server;

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
            .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
            .build();

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:test:server")
            .setApplicationName(LocalizedText.english("test server"))
            .setProductUri("urn:eclipse:milo:test")
            .setCertificateManager(new DefaultCertificateManager())
            .setIdentityValidator(AnonymousIdentityValidator.INSTANCE)
            .setEndpoints(Set.of(endpoint))
            .build();

    server = new OpcUaServer(config, NO_OP_TRANSPORTS);
  }

  @AfterEach
  void tearDown() {
    server.getSessionManager().shutdown();
  }

  // A listener re-checks items in response. If the cache still held the old entries when the
  // listener was called, the re-check would read them back and store nothing new.
  @Test
  void invalidateReadAccessDropsTheEntriesBeforeCallingTheListeners() throws Exception {
    Session session = createSession();
    server
        .getAccessControlManager()
        .getReadAccessCache()
        .getOrCheck(session, List.of(serverStatus));
    assertEquals(1, server.getAccessControlManager().getReadAccessCache().size());

    var sizesAtDelivery = new CopyOnWriteArrayList<Integer>();
    var scopes = new CopyOnWriteArrayList<ReadAccessScope>();
    server
        .getAccessControlManager()
        .addReadAccessListener(
            scope -> {
              sizesAtDelivery.add(server.getAccessControlManager().getReadAccessCache().size());
              scopes.add(scope);
            });

    server.getAccessControlManager().invalidateReadAccess(serverStatus.getNodeId());

    assertEquals(List.of(0), sizesAtDelivery, "the entries are gone when the listener is called");
    assertEquals(1, scopes.size());
    assertTrue(scopes.get(0).includesNode(serverStatus.getNodeId()));
    assertEquals(Optional.empty(), scopes.get(0).session(), "a Node scope covers every Session");
  }

  // One listener's failure must not silence the others, or a refresher could miss an invalidation
  // and keep enforcing a stale answer indefinitely.
  @Test
  void aThrowingListenerDoesNotStopTheOthersOrTheInvalidation() throws Exception {
    Session session = createSession();
    server
        .getAccessControlManager()
        .getReadAccessCache()
        .getOrCheck(session, List.of(serverStatus));

    var heard = new CopyOnWriteArrayList<ReadAccessScope>();
    server
        .getAccessControlManager()
        .addReadAccessListener(
            scope -> {
              throw new IllegalStateException("listener failure");
            });
    server.getAccessControlManager().addReadAccessListener(heard::add);

    server.getAccessControlManager().invalidateReadAccess(serverStatus.getNodeId());

    assertEquals(1, heard.size(), "the second listener is still called");
    assertEquals(
        0,
        server.getAccessControlManager().getReadAccessCache().size(),
        "and the entries are still dropped");
  }

  // A removed listener is a component that has shut down; calling it afterwards would hand work
  // to something that can no longer do it.
  @Test
  void aRemovedListenerIsNotCalled() {
    var heard = new CopyOnWriteArrayList<ReadAccessScope>();
    ReadAccessListener listener = heard::add;

    server.getAccessControlManager().addReadAccessListener(listener);
    server.getAccessControlManager().removeReadAccessListener(listener);
    server.getAccessControlManager().invalidateReadAccess();

    assertEquals(List.of(), heard);
  }

  // The factory is the one way to install a controller, and everything that authorizes, the
  // service sets directly and the cache on a miss, must see the same instance.
  @Test
  void theConfiguredFactoryCreatesTheController() {
    AccessController controller = mock(AccessController.class);
    OpcUaServerConfig config =
        OpcUaServerConfig.copy(server.getConfig())
            .setAccessControllerFactory(s -> controller)
            .build();

    OpcUaServer other = new OpcUaServer(config, NO_OP_TRANSPORTS);
    try {
      assertSame(controller, other.getAccessController());
      assertSame(controller, other.getAccessControlManager().getAccessController());
    } finally {
      other.getSessionManager().shutdown();
    }
  }

  // A closed Session can never be asked for again, so its entries would only hold memory.
  @Test
  void aClosedSessionsEntriesAreDropped() throws Exception {
    Session session = createSession();
    server
        .getAccessControlManager()
        .getReadAccessCache()
        .getOrCheck(session, List.of(serverStatus));
    assertEquals(1, server.getAccessControlManager().getReadAccessCache().size());

    // The server registered its eviction listener first and the queue is serial, so by the time
    // this listener hears about the close, the eviction has run.
    var closed = new CompletableFuture<Session>();
    server
        .getSessionManager()
        .addSessionListener(
            new SessionListener() {
              @Override
              public void onSessionClosed(Session session) {
                closed.complete(session);
              }
            });

    session.close(true);

    assertEquals(session, closed.get(10, SECONDS));
    assertEquals(0, server.getAccessControlManager().getReadAccessCache().size());
  }

  private Session createSession() throws UaException {
    SessionManager sessions = server.getSessionManager();
    sessions.createSession(new TestServiceRequestContext(1L), createSessionRequest());

    return sessions.getAllSessions().iterator().next();
  }

  private static CreateSessionRequest createSessionRequest() {
    return new CreateSessionRequest(
        new RequestHeader(
            NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(10_000), null),
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
