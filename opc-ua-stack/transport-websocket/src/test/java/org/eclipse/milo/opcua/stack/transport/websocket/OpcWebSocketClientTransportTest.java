/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.websocket;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.util.HashedWheelTimer;
import java.net.ServerSocket;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentity;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable.ChannelState;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable.TransitionListener;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * Milo has no WebSocket Server, so these tests cover the transport's channel state without a
 * channel ever becoming ready: each connects to a local port where nothing listens.
 */
class OpcWebSocketClientTransportTest {

  // The client abandons a Session over a DISCONNECTED transport instead of reactivating it, and
  // reports no failed attempt over it. A transport that keeps reconnecting on its own after a
  // failed connect may still come back, so it must not read as DISCONNECTED until the application
  // disconnects it.
  @Test
  void channelStateIsReconnectingAfterARefusedConnectUntilTheApplicationDisconnects()
      throws Exception {

    var transport = new OpcWebSocketClientTransport(new TestTransportConfig());

    assertEquals(
        ChannelState.DISCONNECTED,
        transport.getChannelState(),
        "a transport not yet connected is disconnected");

    CompletableFuture<Unit> connected =
        transport.connect(new TestApplicationContext(endpointNothingListensAt()));

    try {
      assertThrows(
          ExecutionException.class,
          () -> connected.get(10, SECONDS),
          "the connect must have failed, with nothing listening at the endpoint");

      assertEquals(
          ChannelState.RECONNECTING,
          transport.getChannelState(),
          "a transport whose connect was refused reconnects on its own");
    } finally {
      transport.disconnect().get(10, SECONDS);
    }

    assertEquals(
        ChannelState.DISCONNECTED,
        transport.getChannelState(),
        "a transport the application disconnected is disconnected");
  }

  // The client refreshes its endpoint when a transport reports a failed connect, so a transport
  // that is a ChannelStateObservable must report each one, with its cause.
  @Test
  void refusedConnectIsReportedToTransitionListenersWithItsCause() throws Exception {
    var transport = new OpcWebSocketClientTransport(new TestTransportConfig());

    var reported = new CompletableFuture<Throwable>();
    transport.addTransitionListener(
        new TransitionListener() {
          @Override
          public void onStateTransition(boolean connected) {}

          @Override
          public void onConnectFailure(Throwable failure) {
            reported.complete(failure);
          }
        });

    transport.connect(new TestApplicationContext(endpointNothingListensAt()));

    try {
      Throwable failure = reported.get(10, SECONDS);

      assertEquals(
          Optional.of(StatusCodes.Bad_ConnectionRejected),
          UaException.extractStatusCode(failure).map(StatusCode::value));
    } finally {
      transport.disconnect().get(10, SECONDS);
    }
  }

  // region helpers

  /** An endpoint at a local port that was free a moment ago, so a connection to it is refused. */
  private static EndpointDescription endpointNothingListensAt() throws Exception {
    int port;
    try (var socket = new ServerSocket(0)) {
      port = socket.getLocalPort();
    }

    return new EndpointDescription(
        "opc.ws://localhost:%d/".formatted(port),
        null,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        null,
        TransportProfile.WSS_UASC_UABINARY.getUri(),
        ubyte(0));
  }

  private static final class TestTransportConfig implements OpcWebSocketClientTransportConfig {

    @Override
    public UInteger getAcknowledgeTimeout() {
      return uint(5_000);
    }

    @Override
    public UInteger getChannelLifetime() {
      return uint(60_000);
    }

    @Override
    public ExecutorService getExecutor() {
      return Stack.sharedExecutor();
    }

    @Override
    public ScheduledExecutorService getScheduledExecutor() {
      return Stack.sharedScheduledExecutor();
    }

    @Override
    public EventLoopGroup getEventLoop() {
      return Stack.sharedEventLoop();
    }

    @Override
    public HashedWheelTimer getWheelTimer() {
      return Stack.sharedWheelTimer();
    }

    @Override
    public Consumer<Bootstrap> getBootstrapCustomizer() {
      return bootstrap -> {};
    }

    @Override
    public Consumer<ChannelPipeline> getChannelPipelineCustomizer() {
      return pipeline -> {};
    }
  }

  private static final class TestApplicationContext implements ClientApplicationContext {

    private final EndpointDescription endpoint;

    TestApplicationContext(EndpointDescription endpoint) {
      this.endpoint = endpoint;
    }

    @Override
    public EndpointDescription getEndpoint() {
      return endpoint;
    }

    @Override
    public Optional<CertificateIdentity> getCertificateIdentity(
        SecurityPolicyProfile securityPolicyProfile) {

      return Optional.empty();
    }

    @Override
    public CertificateValidator getCertificateValidator() {
      return new CertificateValidator.InsecureCertificateValidator();
    }

    @Override
    public EncodingContext getEncodingContext() {
      return new DefaultEncodingContext();
    }

    @Override
    public UInteger getRequestTimeout() {
      return uint(5_000);
    }
  }

  // endregion
}
