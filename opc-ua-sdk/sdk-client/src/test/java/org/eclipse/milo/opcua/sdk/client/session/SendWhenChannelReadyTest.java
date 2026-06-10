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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.digitalpetri.fsm.FsmContext;
import com.digitalpetri.netty.fsm.ChannelActions;
import com.digitalpetri.netty.fsm.ChannelFsm;
import com.digitalpetri.netty.fsm.ChannelFsmConfig;
import com.digitalpetri.netty.fsm.ChannelFsmFactory;
import com.digitalpetri.netty.fsm.Event;
import com.digitalpetri.netty.fsm.State;
import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies that {@link SessionFsmFactory#sendWhenChannelReady} builds channel-bound requests only
 * once the transport's channel is ready, so an enhanced-policy ActivateSession signature over
 * {@link OpcClientTransport#getChannelThumbprint()} is not built against a stale channel during a
 * slow reconnect.
 */
class SendWhenChannelReadyTest {

  private static final ByteString STALE_THUMBPRINT = ByteString.of(new byte[] {0x01});
  private static final ByteString FRESH_THUMBPRINT = ByteString.of(new byte[] {0x02});

  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

  @AfterEach
  void tearDown() {
    executor.shutdownNow();
  }

  // Reactivation may await an in-progress reconnect inside the transport, so a client signature
  // built before the handshake completes would sign over the dead channel's thumbprint and be
  // rejected with Bad_ApplicationSignatureInvalid.
  @Test
  void buildsRequestOnlyAfterChannelIsReadyAndThumbprintIsFresh() throws Exception {
    var handshakeFuture = new CompletableFuture<Channel>();
    var connectInvoked = new CountDownLatch(1);
    TestTcpTransport transport = newTcpTransport(handshakeFuture, connectInvoked);

    transport.getChannelFsm().connect();
    assertTrue(connectInvoked.await(5, TimeUnit.SECONDS));

    var thumbprintAtBuild = new AtomicReference<ByteString>();

    CompletableFuture<UaResponseMessageType> responseFuture =
        SessionFsmFactory.sendWhenChannelReady(
            transport,
            () -> {
              thumbprintAtBuild.set(transport.getChannelThumbprint());
              return readRequest();
            });

    // The handshake has not completed, so the request must not have been built or sent yet.
    assertNull(thumbprintAtBuild.get());
    assertEquals(0, transport.sentRequests.size());

    handshakeFuture.complete(new EmbeddedChannel());

    responseFuture.get(5, TimeUnit.SECONDS);

    assertEquals(FRESH_THUMBPRINT, thumbprintAtBuild.get());
    assertEquals(1, transport.sentRequests.size());
  }

  // Transports without a channel binding (e.g. HTTP, WebSocket) keep the eager build behavior;
  // their requests contain nothing that depends on channel readiness.
  @Test
  void transportWithoutChannelBindingBuildsAndSendsImmediately() throws Exception {
    var transport = new RecordingTransport();
    ReadRequest request = readRequest();

    CompletableFuture<UaResponseMessageType> responseFuture =
        SessionFsmFactory.sendWhenChannelReady(transport, () -> request);

    responseFuture.get(5, TimeUnit.SECONDS);

    assertEquals(List.of(request), transport.sentRequests);
  }

  // A request that cannot be built (e.g. signing fails) must surface its exception to the caller
  // instead of hanging or sending a partial request.
  @Test
  void supplierExceptionFailsTheResponseFuture() {
    var transport = new RecordingTransport();
    var failure = new UaException(StatusCodes.Bad_ConfigurationError, "cannot build request");

    CompletableFuture<UaResponseMessageType> responseFuture =
        SessionFsmFactory.sendWhenChannelReady(
            transport,
            () -> {
              throw failure;
            });

    ExecutionException e =
        assertThrows(ExecutionException.class, () -> responseFuture.get(5, TimeUnit.SECONDS));

    assertSame(failure, e.getCause());
    assertEquals(0, transport.sentRequests.size());
  }

  private TestTcpTransport newTcpTransport(
      CompletableFuture<Channel> handshakeFuture, CountDownLatch connectInvoked) {

    var thumbprint = new AtomicReference<>(STALE_THUMBPRINT);

    ChannelActions channelActions =
        new ChannelActions() {
          @Override
          public CompletableFuture<Channel> connect(FsmContext<State, Event> ctx) {
            connectInvoked.countDown();

            // Mirror OpcTcpClientTransport: the new thumbprint is published before the channel
            // future completes, so a build that awaits the channel observes the fresh value.
            return handshakeFuture.thenApply(
                channel -> {
                  thumbprint.set(FRESH_THUMBPRINT);
                  return channel;
                });
          }

          @Override
          public CompletableFuture<Void> disconnect(FsmContext<State, Event> ctx, Channel channel) {

            return CompletableFuture.completedFuture(null);
          }
        };

    ChannelFsmConfig fsmConfig =
        ChannelFsmConfig.newBuilder()
            .setLazy(false)
            .setMaxIdleSeconds(0)
            .setPersistent(true)
            .setChannelActions(channelActions)
            .setExecutor(executor)
            .setScheduler(executor)
            .build();

    ChannelFsm channelFsm = new ChannelFsmFactory(fsmConfig).newChannelFsm();

    return new TestTcpTransport(channelFsm, thumbprint);
  }

  private static ReadRequest readRequest() {
    var requestHeader =
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(0), uint(0), null, uint(0), null);

    return new ReadRequest(requestHeader, 0.0, TimestampsToReturn.Both, null);
  }

  /**
   * An {@link OpcTcpClientTransport} with a caller-controlled ChannelFsm and thumbprint that
   * records sends, bypassing the Netty write path.
   */
  private static class TestTcpTransport extends OpcTcpClientTransport {

    final List<UaRequestMessageType> sentRequests = new CopyOnWriteArrayList<>();

    private final ChannelFsm channelFsm;
    private final AtomicReference<ByteString> thumbprint;

    private TestTcpTransport(ChannelFsm channelFsm, AtomicReference<ByteString> thumbprint) {
      super(OpcTcpClientTransportConfig.newBuilder().build());

      this.channelFsm = channelFsm;
      this.thumbprint = thumbprint;
    }

    @Override
    public ChannelFsm getChannelFsm() {
      return channelFsm;
    }

    @Override
    public ByteString getChannelThumbprint() {
      return thumbprint.get();
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      sentRequests.add(requestMessage);
      return CompletableFuture.completedFuture(new ReadResponse(null, null, null));
    }
  }

  /** A transport with no channel binding, standing in for the HTTP/WebSocket transports. */
  private static class RecordingTransport implements OpcClientTransport {

    final List<UaRequestMessageType> sentRequests = new CopyOnWriteArrayList<>();

    @Override
    public OpcClientTransportConfig getConfig() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      sentRequests.add(requestMessage);
      return CompletableFuture.completedFuture(new ReadResponse(null, null, null));
    }
  }
}
