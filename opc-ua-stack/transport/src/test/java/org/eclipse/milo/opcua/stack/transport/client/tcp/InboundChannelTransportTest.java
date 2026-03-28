/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.HashedWheelTimer;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.InboundUascResponseHandler.DelegatingUascResponseHandler;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientReverseHelloHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class InboundChannelTransportTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test-server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private final HashedWheelTimer wheelTimer = new HashedWheelTimer();

  @AfterEach
  void tearDown() {
    wheelTimer.stop();
  }

  @Test
  void connectInstallsHandlersAndSendsHello() throws Exception {
    var channel = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    OpcTcpMultiplexedReverseConnectTransportConfig config = newTransportConfig();

    var transport = new InboundChannelTransport(config, channel, rhe);
    CompletableFuture<?> connectFuture = transport.connect(newApplicationContext());

    // Execute the eventLoop task scheduled by connect()
    channel.runPendingTasks();

    // Pipeline should contain both handlers
    assertNotNull(
        channel.pipeline().get(DelegatingUascResponseHandler.class),
        "DelegatingUascResponseHandler should be in pipeline");
    assertNotNull(
        channel.pipeline().get(UascClientReverseHelloHandler.class),
        "UascClientReverseHelloHandler should be in pipeline");

    // Should have sent a Hello message outbound
    ByteBuf helloBuffer = channel.readOutbound();
    assertNotNull(helloBuffer, "Hello should be sent after connect()");

    HelloMessage hello = TcpMessageDecoder.decodeHello(helloBuffer);
    assertEquals(ENDPOINT_URL, hello.getEndpointUrl());
    helloBuffer.release();

    // Connect future should not be completed yet (waiting for Ack)
    assertFalse(connectFuture.isDone());

    channel.close();
  }

  @Test
  void disconnectClosesChannel() {
    var channel = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    OpcTcpMultiplexedReverseConnectTransportConfig config = newTransportConfig();

    var transport = new InboundChannelTransport(config, channel, rhe);
    transport.disconnect();

    assertFalse(channel.isActive());
  }

  @Test
  void getChannelBeforeConnectReturnsFailed() throws Exception {
    var channel = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    OpcTcpMultiplexedReverseConnectTransportConfig config = newTransportConfig();

    var transport = new InboundChannelTransport(config, channel, rhe);

    // getChannel() before connect() should fail with IllegalStateException
    CompletableFuture<?> future = transport.getChannel();
    assertTrue(future.isCompletedExceptionally());
    try {
      future.get();
    } catch (ExecutionException e) {
      assertInstanceOf(IllegalStateException.class, e.getCause());
    }

    channel.close();
  }

  @Test
  void channelInactiveDuringHandshakeCompletesConnectExceptionally() throws Exception {
    var channel = new EmbeddedChannel();
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    OpcTcpMultiplexedReverseConnectTransportConfig config = newTransportConfig();

    var transport = new InboundChannelTransport(config, channel, rhe);
    CompletableFuture<?> connectFuture = transport.connect(newApplicationContext());

    // Execute the eventLoop task so the handshake starts
    channel.runPendingTasks();

    // Connect future should not be completed yet
    assertFalse(connectFuture.isDone());

    // Close the channel before handshake completes
    channel.close();
    channel.runPendingTasks();

    // Connect future should now be completed exceptionally
    assertTrue(connectFuture.isCompletedExceptionally());
  }

  private OpcTcpMultiplexedReverseConnectTransportConfig newTransportConfig() {
    return OpcTcpMultiplexedReverseConnectTransportConfig.newBuilder()
        .setWheelTimer(wheelTimer)
        .build();
  }

  private static ClientApplicationContext newApplicationContext() {
    return new ClientApplicationContext() {
      @Override
      public EndpointDescription getEndpoint() {
        return new EndpointDescription(
            ENDPOINT_URL,
            null,
            ByteString.NULL_VALUE,
            MessageSecurityMode.None,
            SecurityPolicy.None.getUri(),
            null,
            TransportProfile.TCP_UASC_UABINARY.getUri(),
            ubyte(0));
      }

      @Override
      public Optional<KeyPair> getKeyPair() {
        return Optional.empty();
      }

      @Override
      public Optional<X509Certificate> getCertificate() {
        return Optional.empty();
      }

      @Override
      public Optional<X509Certificate[]> getCertificateChain() {
        return Optional.empty();
      }

      @Override
      public CertificateValidator getCertificateValidator() {
        return new CertificateValidator.InsecureCertificateValidator();
      }

      @Override
      public EncodingContext getEncodingContext() {
        return DefaultEncodingContext.INSTANCE;
      }

      @Override
      public UInteger getRequestTimeout() {
        return UInteger.valueOf(5_000);
      }
    };
  }
}
