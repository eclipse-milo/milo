/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.uasc;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

class UascServerReverseHelloHandlerTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test-server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  @Test
  void channelActiveSendsReverseHello() throws Exception {
    var handler = newHandler();
    var channel = new EmbeddedChannel(handler);

    ByteBuf outbound = channel.readOutbound();
    assertNotNull(outbound, "ReverseHello should be written on channelActive");

    ReverseHelloMessage rhe = TcpMessageDecoder.decodeReverseHello(outbound);
    assertEquals(SERVER_URI, rhe.serverUri());
    assertEquals(ENDPOINT_URL, rhe.endpointUrl());

    outbound.release();
    channel.close();
  }

  @Test
  void reverseHelloContainsConfiguredValues() throws Exception {
    String serverUri = "urn:custom:server";
    String endpointUrl = "opc.tcp://192.168.1.100:48400/custom";

    var handler =
        new UascServerReverseHelloHandler(
            newConfig(),
            newApplicationContext(endpointUrl),
            TransportProfile.TCP_UASC_UABINARY,
            serverUri,
            endpointUrl);

    var channel = new EmbeddedChannel(handler);

    ByteBuf outbound = channel.readOutbound();
    assertNotNull(outbound);

    ReverseHelloMessage rhe = TcpMessageDecoder.decodeReverseHello(outbound);
    assertEquals(serverUri, rhe.serverUri());
    assertEquals(endpointUrl, rhe.endpointUrl());

    outbound.release();
    channel.close();
  }

  @Test
  void helloReceivedAfterReverseHelloSendsAck() throws Exception {
    var handler = newHandler();
    var channel = new EmbeddedChannel(handler);

    // Consume the ReverseHello
    ByteBuf rheBuffer = channel.readOutbound();
    assertNotNull(rheBuffer);
    rheBuffer.release();

    // Send a Hello message from the client
    var hello = new HelloMessage(0, 65536, 65536, 0, 0, ENDPOINT_URL);
    ByteBuf helloBuffer = TcpMessageEncoder.encode(hello);
    channel.writeInbound(helloBuffer);

    // Run any pending tasks (the Ack write is submitted to the executor)
    channel.runPendingTasks();

    // Should receive an Acknowledge message
    ByteBuf ackBuffer = channel.readOutbound();
    assertNotNull(ackBuffer, "Ack should be sent after Hello is received");

    var ack = TcpMessageDecoder.decodeAcknowledge(ackBuffer);
    assertNotNull(ack);

    ackBuffer.release();
    channel.close();
  }

  @Test
  void pipelineReplacedAfterHello() throws Exception {
    var handler = newHandler();
    var channel = new EmbeddedChannel(handler);

    // Consume the ReverseHello
    ByteBuf rheBuffer = channel.readOutbound();
    assertNotNull(rheBuffer);
    rheBuffer.release();

    // Send Hello
    var hello = new HelloMessage(0, 65536, 65536, 0, 0, ENDPOINT_URL);
    ByteBuf helloBuffer = TcpMessageEncoder.encode(hello);
    channel.writeInbound(helloBuffer);
    channel.runPendingTasks();

    // The handler should have been replaced with UascServerAsymmetricHandler
    assertNull(
        channel.pipeline().get(UascServerReverseHelloHandler.class),
        "ReverseHelloHandler should be removed from pipeline after Hello");
    assertNotNull(
        channel.pipeline().get(UascServerAsymmetricHandler.class),
        "AsymmetricHandler should be installed after Hello");

    channel.close();
  }

  @Test
  void channelClosedWhenNoHelloReceived() throws Exception {
    UascServerConfig config =
        new UascServerConfig() {
          @Override
          public ExecutorService getExecutor() {
            return ForkJoinPool.commonPool();
          }

          @Override
          public UInteger getHelloDeadline() {
            return uint(1); // 1ms deadline to trigger quickly
          }

          @Override
          public UInteger getMinimumSecureChannelLifetime() {
            return uint(60_000);
          }

          @Override
          public UInteger getMaximumSecureChannelLifetime() {
            return uint(600_000);
          }
        };

    var handler =
        new UascServerReverseHelloHandler(
            config,
            newApplicationContext(ENDPOINT_URL),
            TransportProfile.TCP_UASC_UABINARY,
            SERVER_URI,
            ENDPOINT_URL);

    var channel = new EmbeddedChannel(handler);

    // Consume the ReverseHello
    ByteBuf rheBuffer = channel.readOutbound();
    assertNotNull(rheBuffer);
    rheBuffer.release();

    // Advance past the deadline and run the scheduled task
    channel.advanceTimeBy(10, TimeUnit.MILLISECONDS);
    channel.runScheduledPendingTasks();

    // Run any remaining pending tasks triggered by the close
    channel.runPendingTasks();

    // Channel should be closed after deadline fires without receiving Hello
    assertFalse(channel.isOpen(), "Channel should be closed after Hello deadline");
  }

  private static UascServerReverseHelloHandler newHandler() {
    return new UascServerReverseHelloHandler(
        newConfig(),
        newApplicationContext(ENDPOINT_URL),
        TransportProfile.TCP_UASC_UABINARY,
        SERVER_URI,
        ENDPOINT_URL);
  }

  private static UascServerConfig newConfig() {
    return new UascServerConfig() {
      @Override
      public ExecutorService getExecutor() {
        return ForkJoinPool.commonPool();
      }

      @Override
      public UInteger getHelloDeadline() {
        return uint(10_000);
      }

      @Override
      public UInteger getMinimumSecureChannelLifetime() {
        return uint(60_000);
      }

      @Override
      public UInteger getMaximumSecureChannelLifetime() {
        return uint(600_000);
      }
    };
  }

  private static ServerApplicationContext newApplicationContext(String endpointUrl) {
    return new ServerApplicationContext() {
      private final AtomicLong secureChannelId = new AtomicLong(0L);
      private final AtomicLong secureChannelTokenId = new AtomicLong(0L);

      @Override
      public List<EndpointDescription> getEndpointDescriptions() {
        return List.of(
            new EndpointDescription(
                endpointUrl,
                new ApplicationDescription(
                    SERVER_URI,
                    "productUri",
                    LocalizedText.NULL_VALUE,
                    ApplicationType.Server,
                    null,
                    null,
                    new String[] {endpointUrl}),
                ByteString.NULL_VALUE,
                MessageSecurityMode.None,
                "http://opcfoundation.org/UA/SecurityPolicy#None",
                new UserTokenPolicy[] {
                  new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
                },
                TransportProfile.TCP_UASC_UABINARY.getUri(),
                ubyte(0)));
      }

      @Override
      public CertificateManager getCertificateManager() {
        throw new UnsupportedOperationException();
      }

      @Override
      public EncodingContext getEncodingContext() {
        return DefaultEncodingContext.INSTANCE;
      }

      @Override
      public Long getNextSecureChannelId() {
        return secureChannelId.getAndIncrement();
      }

      @Override
      public Long getNextSecureChannelTokenId() {
        return secureChannelTokenId.getAndIncrement();
      }

      @Override
      public CompletableFuture<UaResponseMessageType> handleServiceRequest(
          ServiceRequestContext context, UaRequestMessageType requestMessage) {
        return CompletableFuture.failedFuture(new UnsupportedOperationException());
      }
    };
  }
}
