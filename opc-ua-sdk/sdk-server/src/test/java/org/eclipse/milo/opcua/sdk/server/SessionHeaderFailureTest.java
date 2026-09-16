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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.security.EnhancedUserTokenAdditionalHeader;
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
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Exercises failed header negotiation through real session creation and activation. */
class SessionHeaderFailureTest {

  private OpcUaServer server;
  private SessionManager manager;
  private ScheduledFuture<?> timeout;

  @BeforeEach
  void setUp() {
    ScheduledExecutorService scheduler = mock(ScheduledExecutorService.class);
    timeout = mock(ScheduledFuture.class);
    doReturn(timeout)
        .when(scheduler)
        .schedule(any(Runnable.class), anyLong(), eq(TimeUnit.NANOSECONDS));

    server =
        new OpcUaServer(
            OpcUaServerConfig.builder()
                .setApplicationUri("urn:test:session-header")
                .setProductUri("urn:test:product")
                .setScheduledExecutor(scheduler)
                .setEndpoints(Set.of(endpoint("/a"), endpoint("/b")))
                .build(),
            ignored -> mock(OpcServerTransport.class));
    manager = server.getSessionManager();
  }

  @AfterEach
  void tearDown() {
    manager.shutdown();
  }

  /** A failed CreateSession must release its provisional timer without publishing a session. */
  @Test
  void failedCreateCancelsProvisionalTimeout() throws Exception {
    SessionListener listener = mock(SessionListener.class);
    manager.addSessionListener(listener);

    UaException failure =
        assertThrows(
            UaException.class,
            () -> manager.createSession(context(1, "/a"), createRequest(rejectedHeader())));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, failure.getStatusCode().value());
    assertTrue(manager.getAllSessions().isEmpty());
    verify(timeout).cancel(false);
    manager.shutdown();
    verifyNoInteractions(listener);
  }

  /** The initial activation must remain pending if preparing its response fails. */
  @Test
  void failedInitialActivationDoesNotAuthorizeServices() throws Exception {
    ServiceRequestContext context = context(1, "/a");
    CreateSessionResponse created = manager.createSession(context, createRequest(null));
    Session session = manager.getAllSessions().get(0);
    ByteString nonce = session.getLastNonce();

    assertRejectedActivation(context, created.getAuthenticationToken());

    assertAll(
        () -> assertEquals(nonce, session.getLastNonce()),
        () -> assertNull(session.getIdentity()),
        () -> assertNull(session.getLocaleIds()),
        () -> assertTrue(session.getClientUserIdHistory().isEmpty()),
        () ->
            assertEquals(
                StatusCodes.Bad_SessionNotActivated,
                assertThrows(
                        UaException.class,
                        () ->
                            manager.getSession(
                                context, header(created.getAuthenticationToken(), null)))
                    .getStatusCode()
                    .value()));
  }

  /** Neither identity refresh nor channel replacement may commit before its response is ready. */
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void failedReactivationPreservesPriorState(boolean replaceChannel) throws Exception {
    ServiceRequestContext original = context(1, "/a");
    CreateSessionResponse created = manager.createSession(original, createRequest(null));
    NodeId token = created.getAuthenticationToken();
    manager.activateSession(original, activateRequest(token, null, "en-US"));
    Session session = manager.getAllSessions().get(0);
    ByteString nonce = session.getLastNonce();
    Object identity = session.getIdentity();
    Object security = session.getSecurityConfiguration();
    EndpointDescription endpoint = session.getEndpoint();
    List<String> identityHistory = session.getClientUserIdHistory();

    ServiceRequestContext candidate = replaceChannel ? context(2, "/b") : original;
    assertRejectedActivation(candidate, token);

    assertAll(
        () -> assertEquals(1L, session.getSecureChannelId()),
        () -> assertSame(endpoint, session.getEndpoint()),
        () -> assertSame(security, session.getSecurityConfiguration()),
        () -> assertSame(identity, session.getIdentity()),
        () -> assertEquals(nonce, session.getLastNonce()),
        () -> assertArrayEquals(new String[] {"en-US"}, session.getLocaleIds()),
        () -> assertEquals(identityHistory, session.getClientUserIdHistory()),
        () -> assertSame(session, manager.getSession(original, header(token, null))));

    // The same request can subsequently succeed when it omits the rejected negotiation.
    manager.activateSession(candidate, activateRequest(token, null, "de-DE"));
    assertSame(session, manager.getSession(candidate, header(token, null)));
    assertArrayEquals(new String[] {"de-DE"}, session.getLocaleIds());
    assertNotEquals(nonce, session.getLastNonce());
  }

  private void assertRejectedActivation(ServiceRequestContext context, NodeId token)
      throws Exception {
    UaException failure =
        assertThrows(
            UaException.class,
            () ->
                manager.activateSession(
                    context, activateRequest(token, rejectedHeader(), "de-DE")));
    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, failure.getStatusCode().value());
  }

  private ExtensionObject rejectedHeader() throws UaException {
    return EnhancedUserTokenAdditionalHeader.createRequest(
        server.getStaticEncodingContext(), SecurityPolicy.ECC_nistP256_AesGcm);
  }

  private ServiceRequestContext context(long channelId, String path) throws Exception {
    var channel = new ServerSecureChannel();
    channel.setChannelId(channelId);
    channel.setSecurityPolicy(SecurityPolicy.None);
    channel.setMessageSecurityMode(MessageSecurityMode.None);
    EndpointDescription endpoint =
        server.getApplicationContext().getEndpointDescriptions().stream()
            .filter(e -> e.getEndpointUrl().endsWith(path))
            .findFirst()
            .orElseThrow();
    ServiceRequestContext context = mock(ServiceRequestContext.class);
    when(context.getSecureChannel()).thenReturn(channel);
    when(context.getEndpoint()).thenReturn(Optional.of(endpoint));
    when(context.getEndpointUrl()).thenReturn(endpoint.getEndpointUrl());
    when(context.getTransportProfile()).thenReturn(TransportProfile.TCP_UASC_UABINARY);
    when(context.clientAddress()).thenReturn(InetAddress.getLoopbackAddress());
    return context;
  }

  private static EndpointConfig endpoint(String path) {
    return EndpointConfig.newBuilder()
        .setBindAddress("localhost")
        .setBindPort(4840)
        .setHostname("localhost")
        .setPath(path)
        .setSecurityPolicy(SecurityPolicy.None)
        .setSecurityMode(MessageSecurityMode.None)
        .addTokenPolicy(new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null))
        .build();
  }

  private static CreateSessionRequest createRequest(ExtensionObject additionalHeader) {
    return new CreateSessionRequest(
        header(NodeId.NULL_VALUE, additionalHeader),
        new ApplicationDescription(
            "urn:test:client",
            "urn:test:product",
            LocalizedText.english("test"),
            ApplicationType.Client,
            null,
            null,
            null),
        null,
        "opc.tcp://localhost:4840/a",
        "header-test",
        NonceUtil.generateNonce(32),
        ByteString.NULL_VALUE,
        60_000.0,
        uint(0));
  }

  private static ActivateSessionRequest activateRequest(
      NodeId token, ExtensionObject additionalHeader, String locale) {
    return new ActivateSessionRequest(
        header(token, additionalHeader),
        new SignatureData(null, null),
        null,
        new String[] {locale},
        null,
        new SignatureData(null, null));
  }

  private static RequestHeader header(NodeId token, ExtensionObject additionalHeader) {
    return new RequestHeader(
        token, DateTime.now(), uint(1), uint(0), null, uint(10_000), additionalHeader);
  }
}
