/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.ReferenceCountUtil;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.AcknowledgeMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentity;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.SecureChannelHandshakeException;
import org.eclipse.milo.opcua.stack.transport.client.uasc.InboundUascResponseHandler.DelegatingUascResponseHandler;
import org.junit.jupiter.api.Test;

class UascClientAcknowledgeHandlerTest {

  // When a server closes the connection before sending Acknowledge, the client must fail the
  // handshake immediately with Bad_ConnectionClosed and cancel the acknowledge timer instead of
  // waiting out the acknowledge timeout.
  @Test
  void channelInactiveBeforeAcknowledgeFailsHandshakeWithBadConnectionClosedAndCancelsTimeout() {
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();
    var downstreamInactive = new AtomicBoolean(false);

    try {
      var handler =
          new UascClientAcknowledgeHandler(
              config(wheelTimer, 60_000),
              application(),
              new AtomicLong(1L)::getAndIncrement,
              handshakeFuture);

      channel.pipeline().addLast(handler);
      channel.pipeline().addLast(new InactiveObserver(downstreamInactive));
      drainOutbound(channel);

      assertFalse(handshakeFuture.isDone(), "handshake must remain pending until peer responds");
      assertEquals(
          1L, wheelTimer.pendingTimeouts(), "acknowledge timeout must be armed after Hello");

      channel.close().syncUninterruptibly();

      assertTrue(
          handshakeFuture.isCompletedExceptionally(),
          "channelInactive must fail the handshake immediately");

      ExecutionException failure =
          assertThrows(ExecutionException.class, () -> handshakeFuture.get(1, TimeUnit.SECONDS));

      assertFalse(
          failure.getCause() instanceof SecureChannelHandshakeException,
          "closure before Acknowledge must not be marked as a SecureChannel handshake failure");
      assertEquals(
          StatusCodes.Bad_ConnectionClosed,
          UaException.extractStatusCode(failure).orElseThrow().value());
      assertTrue(
          downstreamInactive.get(),
          "channelInactive must still propagate to downstream pipeline handlers");
      assertTrue(
          wheelTimer.stop().isEmpty(),
          "channelInactive must cancel the pending acknowledge timeout");
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  // exceptionCaught() records the specific failure before closing the channel; the subsequent
  // channelInactive() must not overwrite that cause with generic Bad_ConnectionClosed.
  @Test
  void exceptionCaughtBeforeChannelInactivePreservesOriginalCause() {
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    try {
      var handler =
          new UascClientAcknowledgeHandler(
              config(wheelTimer, 60_000),
              application(),
              new AtomicLong(1L)::getAndIncrement,
              handshakeFuture);

      channel.pipeline().addLast(handler);
      drainOutbound(channel);

      var originalCause = new IOException("connection reset by peer");
      channel.pipeline().fireExceptionCaught(originalCause);
      channel.runPendingTasks();

      ExecutionException failure =
          assertThrows(ExecutionException.class, () -> handshakeFuture.get(1, TimeUnit.SECONDS));

      assertSame(
          originalCause,
          failure.getCause(),
          "exceptionCaught cause must not be overwritten by channelInactive");
      assertFalse(channel.isOpen(), "exceptionCaught must close the channel");
      assertTrue(
          wheelTimer.stop().isEmpty(),
          "closing the channel after exceptionCaught must cancel the acknowledge timeout");
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  // An OPC UA Error message carries a specific server status code before closing the channel; the
  // subsequent channelInactive() must preserve that error and cancel the acknowledge timer.
  @Test
  void errorMessageBeforeChannelInactivePreservesServerError() throws Exception {
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    try {
      var handler =
          new UascClientAcknowledgeHandler(
              config(wheelTimer, 60_000),
              application(),
              new AtomicLong(1L)::getAndIncrement,
              handshakeFuture);

      channel.pipeline().addLast(handler);
      drainOutbound(channel);

      channel.writeInbound(
          TcpMessageEncoder.encode(new ErrorMessage(StatusCodes.Bad_TcpServerTooBusy, "busy")));
      channel.runPendingTasks();

      ExecutionException failure =
          assertThrows(ExecutionException.class, () -> handshakeFuture.get(1, TimeUnit.SECONDS));

      assertFalse(
          failure.getCause() instanceof SecureChannelHandshakeException,
          "error before Acknowledge must not be marked as a SecureChannel handshake failure");
      assertEquals(
          StatusCodes.Bad_TcpServerTooBusy,
          UaException.extractStatusCode(failure).orElseThrow().value());
      assertFalse(channel.isOpen(), "onError must close the channel");
      assertTrue(
          wheelTimer.stop().isEmpty(),
          "closing the channel after Error must cancel the acknowledge timeout");
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  // After Acknowledge, UascClientAcknowledgeHandler remains in the pipeline until OPN succeeds.
  // Closing during OPN must preserve the SecureChannelHandshakeException phase marker.
  @Test
  void channelInactiveAfterAcknowledgePreservesSecureChannelHandshakePhase() throws Exception {
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    try {
      channel.pipeline().addLast(new DelegatingUascResponseHandler(NoopResponseHandler.INSTANCE));
      channel
          .pipeline()
          .addLast(
              new UascClientAcknowledgeHandler(
                  config(wheelTimer, 60_000),
                  application(),
                  new AtomicLong(1L)::getAndIncrement,
                  handshakeFuture));
      drainOutbound(channel);

      channel.writeInbound(TcpMessageEncoder.encode(new AcknowledgeMessage(0, 65535, 65535, 0, 0)));
      channel.runPendingTasks();
      drainOutbound(channel);

      assertNotNull(
          channel.pipeline().get(UascClientMessageHandler.class),
          "Acknowledge must install UascClientMessageHandler");

      channel.close().syncUninterruptibly();

      ExecutionException failure =
          assertThrows(ExecutionException.class, () -> handshakeFuture.get(1, TimeUnit.SECONDS));

      assertInstanceOf(
          SecureChannelHandshakeException.class,
          failure.getCause(),
          "closure after Acknowledge must carry SecureChannelHandshakeException");
      assertEquals(
          StatusCodes.Bad_ConnectionClosed,
          UaException.extractStatusCode(failure).orElseThrow().value());
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  // UascClientAcknowledgeHandler is removed asynchronously after OPN succeeds; if channelInactive
  // runs before removal, it must leave the completed handshake intact and still propagate inactive.
  @Test
  void channelInactiveAfterHandshakeCompletedLeavesSuccessIntact() throws Exception {
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();
    var downstreamInactive = new AtomicBoolean(false);

    try {
      var handler =
          new UascClientAcknowledgeHandler(
              config(wheelTimer, 60_000),
              application(),
              new AtomicLong(1L)::getAndIncrement,
              handshakeFuture);

      channel.pipeline().addLast(handler);
      channel.pipeline().addLast(new InactiveObserver(downstreamInactive));
      drainOutbound(channel);

      var secureChannel = new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
      secureChannel.setChannel(channel);
      handshakeFuture.complete(secureChannel);

      channel.close().syncUninterruptibly();

      assertSame(secureChannel, handshakeFuture.get(1, TimeUnit.SECONDS));
      assertTrue(downstreamInactive.get());
      assertTrue(wheelTimer.stop().isEmpty());
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  // Two Acknowledge frames in one inbound buffer must not queue a second handoff or install
  // UascClientMessageHandler after the protocol violation closes the channel.
  @Test
  void duplicateAcknowledgeInSameBufferFailsHandshakeWithoutInstallingMessageHandler()
      throws Exception {
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    try {
      channel.pipeline().addLast(new DelegatingUascResponseHandler(NoopResponseHandler.INSTANCE));
      channel
          .pipeline()
          .addLast(
              new UascClientAcknowledgeHandler(
                  config(wheelTimer, 60_000),
                  application(),
                  new AtomicLong(1L)::getAndIncrement,
                  handshakeFuture));
      drainOutbound(channel);

      ByteBuf ack1 = TcpMessageEncoder.encode(new AcknowledgeMessage(0, 65535, 65535, 0, 0));
      ByteBuf ack2 = TcpMessageEncoder.encode(new AcknowledgeMessage(0, 65535, 65535, 0, 0));
      ByteBuf combined = Unpooled.wrappedBuffer(ack1, ack2);

      channel.writeInbound(combined);
      channel.runPendingTasks();

      ExecutionException failure =
          assertThrows(ExecutionException.class, () -> handshakeFuture.get(1, TimeUnit.SECONDS));

      assertFalse(
          failure.getCause() instanceof SecureChannelHandshakeException,
          "duplicate Acknowledge before handoff must not carry SecureChannelHandshakeException");
      assertEquals(
          StatusCodes.Bad_TcpMessageTypeInvalid,
          UaException.extractStatusCode(failure).orElseThrow().value());
      assertFalse(channel.isOpen(), "duplicate Acknowledge must close the channel");
      assertNull(
          channel.pipeline().get(UascClientMessageHandler.class),
          "queued handoff must not install UascClientMessageHandler after duplicate Acknowledge");
      assertNull(
          channel.readOutbound(),
          "no OpenSecureChannelRequest should be written after duplicate Acknowledge");
      assertTrue(
          wheelTimer.stop().isEmpty(),
          "no acknowledge or secure-channel timeout may remain armed after duplicate Acknowledge");
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  // Removing the handler after Hello was sent but before handoff cancels its acknowledge timer, so
  // it must also fail the handshake and close the channel instead of leaving connect() hanging.
  @Test
  void handlerRemovedBeforeAcknowledgeFailsHandshakeCancelsTimeoutAndClosesChannel() {
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    try {
      var handler =
          new UascClientAcknowledgeHandler(
              config(wheelTimer, 60_000),
              application(),
              new AtomicLong(1L)::getAndIncrement,
              handshakeFuture);

      channel.pipeline().addLast(handler);
      drainOutbound(channel);

      assertEquals(
          1L, wheelTimer.pendingTimeouts(), "acknowledge timeout must be armed after Hello");

      channel.pipeline().remove(handler);

      ExecutionException failure =
          assertThrows(ExecutionException.class, () -> handshakeFuture.get(1, TimeUnit.SECONDS));

      assertFalse(
          failure.getCause() instanceof SecureChannelHandshakeException,
          "removal before handoff must not carry SecureChannelHandshakeException");
      assertEquals(
          StatusCodes.Bad_ConnectionClosed,
          UaException.extractStatusCode(failure).orElseThrow().value());
      assertFalse(channel.isOpen(), "handlerRemoved before handoff must close the channel");
      assertTrue(
          wheelTimer.stop().isEmpty(),
          "handlerRemoved must cancel an uncompleted acknowledge timeout");
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  // Once UascClientMessageHandler has taken over the handshake, removing the acknowledge handler
  // must remain silent and leave both the channel and the in-flight OpenSecureChannel intact.
  @Test
  void handlerRemovedAfterHandoffDoesNotFailHandshakeOrCloseChannel() throws Exception {
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    try {
      var handler =
          new UascClientAcknowledgeHandler(
              config(wheelTimer, 60_000),
              application(),
              new AtomicLong(1L)::getAndIncrement,
              handshakeFuture);

      channel.pipeline().addLast(new DelegatingUascResponseHandler(NoopResponseHandler.INSTANCE));
      channel.pipeline().addLast(handler);
      drainOutbound(channel);

      channel.writeInbound(TcpMessageEncoder.encode(new AcknowledgeMessage(0, 65535, 65535, 0, 0)));
      channel.runPendingTasks();
      drainOutbound(channel);

      assertNotNull(
          channel.pipeline().get(UascClientMessageHandler.class),
          "Acknowledge must install UascClientMessageHandler");

      channel.pipeline().remove(handler);

      assertFalse(
          handshakeFuture.isDone(),
          "removing UascClientAcknowledgeHandler after handoff must not fail OPN");
      assertTrue(
          channel.isOpen(),
          "removing UascClientAcknowledgeHandler after handoff must not close the channel");
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  // If the handler is removed before Hello is ever sent (for example, during teardown of a failed
  // TCP connect), it must not overwrite the transport's connect error with Bad_ConnectionClosed.
  @Test
  void handlerRemovedBeforeHelloSentLeavesHandshakeUncompleted() {
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();

    try {
      var handler =
          new UascClientAcknowledgeHandler(
              config(wheelTimer, 60_000),
              application(),
              new AtomicLong(1L)::getAndIncrement,
              handshakeFuture,
              () -> "opc.tcp://localhost:0/test",
              false);

      channel.pipeline().addLast(handler);
      channel.pipeline().remove(handler);

      assertFalse(
          handshakeFuture.isDone(),
          "removal before Hello was sent must leave handshake completion to the connect listener");
      assertTrue(channel.isOpen());
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  // On already-active channels (such as WebSocket upgrades), handlerAdded() sends Hello
  // immediately. If Hello construction fails locally (for example, an oversized endpoint URL),
  // handshakeFuture must keep that original cause instead of being masked by Bad_ConnectionClosed
  // from handlerRemoved(), and the armed acknowledge timer must be cancelled.
  @Test
  void handlerAddedWithOversizedEndpointUrlPreservesCauseAndCancelsTimeout() {
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    var handshakeFuture = new CompletableFuture<ClientSecureChannel>();
    String oversizedUrl = "opc.tcp://localhost:4840/" + "a".repeat(4096);

    try {
      var handler =
          new UascClientAcknowledgeHandler(
              config(wheelTimer, 60_000),
              application(),
              new AtomicLong(1L)::getAndIncrement,
              handshakeFuture,
              () -> oversizedUrl,
              true);

      channel.pipeline().addLast(handler);

      ExecutionException failure =
          assertThrows(ExecutionException.class, () -> handshakeFuture.get(1, TimeUnit.SECONDS));

      assertTrue(
          failure.getCause() instanceof IllegalArgumentException,
          "expected original Hello validation IllegalArgumentException, got: "
              + failure.getCause());
      assertTrue(
          failure.getCause().getMessage().contains("4096"),
          "expected oversized endpointUrl validation message, got: "
              + failure.getCause().getMessage());
      assertFalse(channel.isOpen(), "local Hello failure must close the channel");
      assertTrue(
          wheelTimer.stop().isEmpty(),
          "local Hello failure must cancel the armed acknowledge timeout");
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  private static void drainOutbound(EmbeddedChannel channel) {
    Object outbound;
    while ((outbound = channel.readOutbound()) != null) {
      ReferenceCountUtil.release(outbound);
    }
  }

  private static UascClientConfig config(HashedWheelTimer wheelTimer, long acknowledgeTimeoutMs) {
    return new UascClientConfig() {
      @Override
      public UInteger getAcknowledgeTimeout() {
        return uint(acknowledgeTimeoutMs);
      }

      @Override
      public UInteger getChannelLifetime() {
        return uint(3_600_000);
      }

      @Override
      public HashedWheelTimer getWheelTimer() {
        return wheelTimer;
      }
    };
  }

  private static ClientApplicationContext application() {
    EndpointDescription endpoint =
        new EndpointDescription(
            "opc.tcp://localhost:0/test",
            new ApplicationDescription(
                "urn:eclipse:milo:test",
                "urn:eclipse:milo:test",
                LocalizedText.english("test"),
                ApplicationType.Client,
                null,
                null,
                null),
            ByteString.NULL_VALUE,
            MessageSecurityMode.None,
            SecurityPolicy.None.getUri(),
            null,
            TransportProfile.TCP_UASC_UABINARY.getUri(),
            ubyte(0));

    return new ClientApplicationContext() {
      @Override
      public EndpointDescription getEndpoint() {
        return endpoint;
      }

      @Override
      public Optional<CertificateIdentity> getCertificateIdentity(SecurityPolicyProfile profile) {
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
        return uint(60_000);
      }
    };
  }

  private static final class InactiveObserver extends ChannelInboundHandlerAdapter {

    private final AtomicBoolean inactiveSeen;

    private InactiveObserver(AtomicBoolean inactiveSeen) {
      this.inactiveSeen = inactiveSeen;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      inactiveSeen.set(true);
      super.channelInactive(ctx);
    }
  }

  private enum NoopResponseHandler implements UascResponseHandler {
    INSTANCE;

    @Override
    public void handleResponse(long requestId, UaResponseMessageType responseMessage) {}

    @Override
    public void handleSendFailure(long requestId, UaException exception) {}

    @Override
    public void handleReceiveFailure(long requestId, UaException exception) {}

    @Override
    public void handleChannelError(Channel channel, UaException exception) {}

    @Override
    public void handleChannelInactive(Channel channel) {}
  }
}
