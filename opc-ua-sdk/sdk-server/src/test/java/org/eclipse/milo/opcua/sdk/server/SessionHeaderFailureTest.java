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
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

  private static RequestHeader header(NodeId token, ExtensionObject additionalHeader) {
    return new RequestHeader(
        token, DateTime.now(), uint(1), uint(0), null, uint(10_000), additionalHeader);
  }
}
