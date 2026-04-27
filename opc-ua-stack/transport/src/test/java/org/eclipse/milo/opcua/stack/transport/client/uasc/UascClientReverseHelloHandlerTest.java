/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.uasc;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipelineException;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.ReferenceCountUtil;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.AcknowledgeMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class UascClientReverseHelloHandlerTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test-server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private final HashedWheelTimer wheelTimer = new HashedWheelTimer();
  private final AtomicLong requestId = new AtomicLong(0L);

  @AfterEach
  void tearDown() {
    wheelTimer.stop();
  }

  @Test
  void helperInstallsPipelineAndHandsOffPreDecodedReverseHello() throws Exception {
    UascClientReverseConnectHandshake helper = new UascClientReverseConnectHandshake();
    var channel = new EmbeddedChannel();

    CompletableFuture<ClientSecureChannel> handshakeFuture =
        helper.start(
            channel,
            new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL),
            newConfig(wheelTimer),
            newApplicationContext(),
            newResponseHandler(),
            requestId::getAndIncrement,
            Set.of(SERVER_URI),
            30_000);

    channel.runPendingTasks();

    assertNotNull(
        channel.pipeline().get(InboundUascResponseHandler.DelegatingUascResponseHandler.class));
    assertNotNull(channel.pipeline().get(UascClientReverseHelloHandler.class));

    ByteBuf helloBuffer = channel.readOutbound();
    assertNotNull(helloBuffer, "Hello should be sent from the pre-decoded ReverseHello");

    HelloMessage hello = TcpMessageDecoder.decodeHello(helloBuffer);
    assertEquals(ENDPOINT_URL, hello.getEndpointUrl());
    helloBuffer.release();

    assertFalse(handshakeFuture.isDone());

    channel.close();
  }

  @Test
  void helperDoesNotStartReverseHelloTimeoutForPreDecodedReverseHello() throws Exception {
    var shortTimer = new HashedWheelTimer(1, TimeUnit.MILLISECONDS);
    try {
      UascClientReverseConnectHandshake helper = new UascClientReverseConnectHandshake();
      var channel = new EmbeddedChannel();

      CompletableFuture<ClientSecureChannel> handshakeFuture =
          helper.start(
              channel,
              new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL),
              newConfig(shortTimer),
              newApplicationContext(),
              newResponseHandler(),
              requestId::getAndIncrement,
              Set.of(SERVER_URI),
              1);

      channel.runPendingTasks();

      ByteBuf helloBuffer = channel.readOutbound();
      assertNotNull(helloBuffer);
      helloBuffer.release();

      Thread.sleep(100);

      assertFalse(handshakeFuture.isCompletedExceptionally());

      channel.close();
    } finally {
      shortTimer.stop();
    }
  }

  @Test
  void helperFailsAndClosesWhenPipelineInstallFails() {
    var alreadyInstalled = new ChannelInboundHandlerAdapter();
    UascClientReverseConnectHandshake helper =
        new UascClientReverseConnectHandshake() {
          @Override
          ChannelHandler newResponseHandler(UascResponseHandler responseHandler) {
            return alreadyInstalled;
          }
        };

    var channel = new EmbeddedChannel(alreadyInstalled);

    CompletableFuture<ClientSecureChannel> handshakeFuture =
        helper.start(
            channel,
            new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL),
            newConfig(wheelTimer),
            newApplicationContext(),
            newResponseHandler(),
            requestId::getAndIncrement,
            Set.of(SERVER_URI),
            30_000);

    channel.runPendingTasks();

    ExecutionException exception =
        assertThrows(ExecutionException.class, () -> handshakeFuture.get(1, TimeUnit.SECONDS));
    assertNotNull(findCause(exception.getCause(), ChannelPipelineException.class));
    assertFalse(channel.isOpen());
  }

  @Test
  void helperClosesWhenSetupFailsAfterFutureCompletion() {
    UascClientReverseConnectHandshake helper =
        new UascClientReverseConnectHandshake() {
          @Override
          UascClientReverseHelloHandler newReverseHelloHandler(
              UascClientConfig config,
              ClientApplicationContext application,
              Supplier<Long> requestIdSupplier,
              CompletableFuture<ClientSecureChannel> handshakeFuture,
              Set<String> allowedServerUris,
              long reverseHelloTimeoutMs) {

            return new UascClientReverseHelloHandler(
                config,
                application,
                requestIdSupplier,
                handshakeFuture,
                allowedServerUris,
                reverseHelloTimeoutMs) {
              @Override
              public void onReverseHello(
                  ChannelHandlerContext ctx, ReverseHelloMessage reverseHello) {

                handshakeFuture.completeExceptionally(
                    new UaException(StatusCodes.Bad_TcpEndpointUrlInvalid, "rejected"));
                throw new IllegalStateException("error write failed");
              }
            };
          }
        };

    var channel = new EmbeddedChannel();

    CompletableFuture<ClientSecureChannel> handshakeFuture =
        helper.start(
            channel,
            new ReverseHelloMessage("urn:unknown:server", ENDPOINT_URL),
            newConfig(wheelTimer),
            newApplicationContext(),
            newResponseHandler(),
            requestId::getAndIncrement,
            Set.of(SERVER_URI),
            30_000);

    channel.runPendingTasks();

    ExecutionException exception =
        assertThrows(ExecutionException.class, () -> handshakeFuture.get(1, TimeUnit.SECONDS));
    assertInstanceOf(UaException.class, exception.getCause());
    assertFalse(channel.isOpen());
  }

  @Test
  void helperFailsForDisallowedServerUri() throws Exception {
    UascClientReverseConnectHandshake helper = new UascClientReverseConnectHandshake();
    var channel = new EmbeddedChannel();

    CompletableFuture<ClientSecureChannel> handshakeFuture =
        helper.start(
            channel,
            new ReverseHelloMessage("urn:unknown:server", ENDPOINT_URL),
            newConfig(wheelTimer),
            newApplicationContext(),
            newResponseHandler(),
            requestId::getAndIncrement,
            Set.of(SERVER_URI),
            30_000);

    channel.runPendingTasks();

    ByteBuf errorBuffer = channel.readOutbound();
    assertNotNull(errorBuffer, "Error message should be sent for unknown ServerUri");

    ErrorMessage error = TcpMessageDecoder.decodeError(errorBuffer);
    assertEquals(new StatusCode(StatusCodes.Bad_TcpEndpointUrlInvalid), error.getError());
    errorBuffer.release();

    ExecutionException exception =
        assertThrows(ExecutionException.class, () -> handshakeFuture.get(1, TimeUnit.SECONDS));
    UaException uaException = assertInstanceOf(UaException.class, exception.getCause());
    assertEquals(StatusCodes.Bad_TcpEndpointUrlInvalid, uaException.getStatusCode().value());

    channel.close();
  }

  @Test
  void disallowedServerUriClosesAfterErrorWriteCompletes() throws Exception {
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    var handler =
        new UascClientReverseHelloHandler(
            newConfig(wheelTimer),
            newApplicationContext(),
            requestId::getAndIncrement,
            handshakeFuture,
            Set.of(SERVER_URI),
            30_000);
    var delayingOutbound = new DelayingOutboundHandler();
    var channel = new EmbeddedChannel(delayingOutbound, handler);

    var rhe = new ReverseHelloMessage("urn:unknown:server", ENDPOINT_URL);
    ByteBuf rheBuffer = TcpMessageEncoder.encode(rhe);
    channel.writeInbound(rheBuffer);

    assertNotNull(delayingOutbound.message());
    assertTrue(channel.isOpen(), "Channel should remain open until the Error write completes");

    delayingOutbound.succeedWrite();
    channel.runPendingTasks();

    assertFalse(channel.isOpen());
  }

  @Test
  void happyPathRheSendsHello() throws Exception {
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    var handler =
        new UascClientReverseHelloHandler(
            newConfig(wheelTimer),
            newApplicationContext(),
            requestId::getAndIncrement,
            handshakeFuture,
            Set.of(SERVER_URI),
            30_000);

    var channel = new EmbeddedChannel(handler);

    // Write a valid ReverseHello inbound
    var rhe = new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
    ByteBuf rheBuffer = TcpMessageEncoder.encode(rhe);
    channel.writeInbound(rheBuffer);

    // Should have sent a Hello message outbound
    ByteBuf helloBuffer = channel.readOutbound();
    assertNotNull(helloBuffer, "Hello should be sent after receiving ReverseHello");

    HelloMessage hello = TcpMessageDecoder.decodeHello(helloBuffer);
    assertEquals(ENDPOINT_URL, hello.getEndpointUrl());
    helloBuffer.release();

    // Handler should still be in the pipeline (waiting for Ack)
    assertNotNull(
        channel.pipeline().get(UascClientReverseHelloHandler.class),
        "ReverseHelloHandler should remain in pipeline while awaiting Ack");

    // handshakeFuture should not be completed yet
    assertFalse(handshakeFuture.isDone());

    channel.close();
  }

  @Test
  void helloUsesEndpointUrlFromReverseHello() throws Exception {
    String rheEndpointUrl = "opc.tcp://192.168.1.100:48400/custom";
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    var handler =
        new UascClientReverseHelloHandler(
            newConfig(wheelTimer),
            newApplicationContext(),
            requestId::getAndIncrement,
            handshakeFuture,
            Set.of(),
            30_000);

    var channel = new EmbeddedChannel(handler);

    // Write RHE with a custom EndpointUrl
    var rhe = new ReverseHelloMessage(SERVER_URI, rheEndpointUrl);
    ByteBuf rheBuffer = TcpMessageEncoder.encode(rhe);
    channel.writeInbound(rheBuffer);

    // Verify the Hello uses the EndpointUrl from RHE, not the application context
    ByteBuf helloBuffer = channel.readOutbound();
    assertNotNull(helloBuffer);

    HelloMessage hello = TcpMessageDecoder.decodeHello(helloBuffer);
    assertEquals(rheEndpointUrl, hello.getEndpointUrl());

    helloBuffer.release();
    channel.close();
  }

  @Test
  void acknowledgeInstallsMessageHandlerSynchronously() throws Exception {
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    var handler =
        new UascClientReverseHelloHandler(
            newConfig(wheelTimer),
            newApplicationContext(),
            requestId::getAndIncrement,
            handshakeFuture,
            Set.of(SERVER_URI),
            30_000);

    var channel = new EmbeddedChannel(handler);

    ByteBuf rheBuffer = TcpMessageEncoder.encode(new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL));
    channel.writeInbound(rheBuffer);

    ByteBuf helloBuffer = channel.readOutbound();
    assertNotNull(helloBuffer);
    helloBuffer.release();

    ByteBuf ackBuffer = TcpMessageEncoder.encode(new AcknowledgeMessage(0, 8192, 8192, 0, 0));
    channel.writeInbound(ackBuffer);

    assertNotNull(channel.pipeline().get(UascClientMessageHandler.class));
    assertNull(channel.pipeline().get(UascClientReverseHelloHandler.class));

    channel.close();
  }

  @Test
  void unknownServerUriSendsErrorAndCloses() throws Exception {
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    var handler =
        new UascClientReverseHelloHandler(
            newConfig(wheelTimer),
            newApplicationContext(),
            requestId::getAndIncrement,
            handshakeFuture,
            Set.of("urn:known:server"),
            30_000);

    var channel = new EmbeddedChannel(handler);

    // Write RHE with an unrecognized ServerUri
    var rhe = new ReverseHelloMessage("urn:unknown:server", ENDPOINT_URL);
    ByteBuf rheBuffer = TcpMessageEncoder.encode(rhe);
    channel.writeInbound(rheBuffer);

    // Run pending tasks for error message write
    channel.runPendingTasks();

    // Should have sent an Error message
    ByteBuf errorBuffer = channel.readOutbound();
    assertNotNull(errorBuffer, "Error message should be sent for unknown ServerUri");

    ErrorMessage error = TcpMessageDecoder.decodeError(errorBuffer);
    assertEquals(new StatusCode(StatusCodes.Bad_TcpEndpointUrlInvalid), error.getError());
    errorBuffer.release();

    // handshakeFuture should be failed
    assertTrue(handshakeFuture.isCompletedExceptionally());
    try {
      handshakeFuture.get();
    } catch (ExecutionException e) {
      assertInstanceOf(UaException.class, e.getCause());
      UaException uaException = (UaException) e.getCause();
      assertEquals(StatusCodes.Bad_TcpEndpointUrlInvalid, uaException.getStatusCode().value());
    }

    channel.close();
  }

  @Test
  void emptyAllowedServerUrisAcceptsAnyUri() throws Exception {
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    var handler =
        new UascClientReverseHelloHandler(
            newConfig(wheelTimer),
            newApplicationContext(),
            requestId::getAndIncrement,
            handshakeFuture,
            Set.of(),
            30_000);

    var channel = new EmbeddedChannel(handler);

    // Write RHE with any ServerUri — should be accepted in permissive mode
    var rhe = new ReverseHelloMessage("urn:any:server", ENDPOINT_URL);
    ByteBuf rheBuffer = TcpMessageEncoder.encode(rhe);
    channel.writeInbound(rheBuffer);

    // Should have sent Hello (not Error)
    ByteBuf helloBuffer = channel.readOutbound();
    assertNotNull(helloBuffer, "Hello should be sent in permissive mode");

    HelloMessage hello = TcpMessageDecoder.decodeHello(helloBuffer);
    assertNotNull(hello);
    helloBuffer.release();

    // handshakeFuture should NOT be failed
    assertFalse(handshakeFuture.isCompletedExceptionally());

    channel.close();
  }

  @Test
  void reverseHelloTimeoutFailsHandshakeFuture() throws Exception {
    var shortTimer = new HashedWheelTimer(1, TimeUnit.MILLISECONDS);
    try {
      var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

      var handler =
          new UascClientReverseHelloHandler(
              newConfig(shortTimer),
              newApplicationContext(),
              requestId::getAndIncrement,
              handshakeFuture,
              Set.of(),
              1);

      var channel = new EmbeddedChannel(handler);

      // Don't send anything — wait for timeout to fire
      Thread.sleep(100);

      assertTrue(handshakeFuture.isCompletedExceptionally());
      try {
        handshakeFuture.get();
      } catch (ExecutionException e) {
        assertInstanceOf(UaException.class, e.getCause());
        UaException uaException = (UaException) e.getCause();
        assertEquals(StatusCodes.Bad_Timeout, uaException.getStatusCode().value());
      }

      channel.close();
    } finally {
      shortTimer.stop();
    }
  }

  @Test
  void errorMessageFailsHandshakeFuture() throws Exception {
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    var handler =
        new UascClientReverseHelloHandler(
            newConfig(wheelTimer),
            newApplicationContext(),
            requestId::getAndIncrement,
            handshakeFuture,
            Set.of(),
            30_000);

    var channel = new EmbeddedChannel(handler);

    // Send an Error message instead of RHE
    var error = new ErrorMessage(StatusCodes.Bad_ServerNotConnected, "test error");
    ByteBuf errorBuffer = TcpMessageEncoder.encode(error);
    channel.writeInbound(errorBuffer);

    channel.runPendingTasks();

    // handshakeFuture should be failed
    assertTrue(handshakeFuture.isCompletedExceptionally());
    try {
      handshakeFuture.get();
    } catch (ExecutionException e) {
      assertInstanceOf(UaException.class, e.getCause());
    }

    channel.close();
  }

  private static UascClientConfig newConfig(HashedWheelTimer wheelTimer) {
    return new UascClientConfig() {
      @Override
      public UInteger getAcknowledgeTimeout() {
        return uint(5_000);
      }

      @Override
      public UInteger getChannelLifetime() {
        return uint(60 * 60 * 1000);
      }

      @Override
      public HashedWheelTimer getWheelTimer() {
        return wheelTimer;
      }
    };
  }

  private static UascResponseHandler newResponseHandler() {
    return new UascResponseHandler() {
      @Override
      public void handleResponse(long requestId, UaResponseMessageType responseMessage) {}

      @Override
      public void handleSendFailure(long requestId, UaException exception) {}

      @Override
      public void handleReceiveFailure(long requestId, UaException exception) {}

      @Override
      public void handleChannelError(UaException exception) {}

      @Override
      public void handleChannelInactive() {}
    };
  }

  private static <T extends Throwable> T findCause(Throwable throwable, Class<T> causeType) {
    Throwable cause = throwable;
    while (cause != null) {
      if (causeType.isInstance(cause)) {
        return causeType.cast(cause);
      }

      cause = cause.getCause();
    }

    return null;
  }

  private static final class DelayingOutboundHandler extends ChannelOutboundHandlerAdapter {

    private Object message;
    private ChannelPromise promise;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
      this.message = msg;
      this.promise = promise;
    }

    Object message() {
      return message;
    }

    void succeedWrite() {
      ReferenceCountUtil.release(message);
      promise.setSuccess();
    }
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
        return uint(5_000);
      }
    };
  }
}
