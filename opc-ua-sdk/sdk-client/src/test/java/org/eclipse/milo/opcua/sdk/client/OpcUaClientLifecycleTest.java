/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.HashedWheelTimer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.session.State;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpcUaClientLifecycleTest {

  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private ExecutorService executor;
  private ScheduledExecutorService scheduledExecutor;
  private NioEventLoopGroup eventLoop;
  private HashedWheelTimer wheelTimer;
  private OpcClientTransportConfig transportConfig;

  @BeforeEach
  void setUp() {
    executor = Executors.newSingleThreadExecutor();
    scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    eventLoop = new NioEventLoopGroup(1);
    wheelTimer = new HashedWheelTimer();
    transportConfig =
        OpcTcpClientTransportConfig.newBuilder()
            .setExecutor(executor)
            .setScheduledExecutor(scheduledExecutor)
            .setEventLoop(eventLoop)
            .setWheelTimer(wheelTimer)
            .build();
  }

  @AfterEach
  void tearDown() throws Exception {
    executor.shutdownNow();
    scheduledExecutor.shutdownNow();
    eventLoop.shutdownGracefully().get(5, TimeUnit.SECONDS);
    wheelTimer.stop();
  }

  @Test
  void connectAsyncPropagatesTransportConnectFailure() {
    var failure = new UaException(StatusCodes.Bad_ConnectionRejected, "bind failed");
    var transport = new FakeTransport(transportConfig, CompletableFuture.failedFuture(failure));
    var client = new OpcUaClient(newClientConfig(5_000), transport);

    ExecutionException ex =
        assertThrows(
            ExecutionException.class, () -> client.connectAsync().get(1, TimeUnit.SECONDS));

    assertSame(failure, ex.getCause());
    assertEquals(0, transport.requestCount());
  }

  @Test
  void disconnectAsyncReachesTransportDisconnectWhenSessionCloseCannotComplete() throws Exception {
    var transport =
        new FakeTransport(transportConfig, CompletableFuture.completedFuture(Unit.VALUE));
    var client = new OpcUaClient(newClientConfig(50), transport);

    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();
    assertTrue(transport.awaitRequest());
    assertFalse(connectFuture.isDone());

    CompletableFuture<OpcUaClient> disconnectFuture = client.disconnectAsync();

    assertSame(client, disconnectFuture.get(5, TimeUnit.SECONDS));
    assertTrue(transport.disconnectCalled());
  }

  @Test
  void disconnectAsyncReachesTransportDisconnectWhenActivationCannotComplete() throws Exception {
    var transport = new ScriptedSessionTransport(transportConfig, true);
    var client = new OpcUaClient(newClientConfig(50), transport);

    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();
    awaitState(client, State.Activating);

    assertFalse(connectFuture.isDone());

    CompletableFuture<OpcUaClient> disconnectFuture = client.disconnectAsync();

    assertSame(client, disconnectFuture.get(5, TimeUnit.SECONDS));
    assertTrue(transport.disconnectCalled());
  }

  @Test
  void disconnectAsyncReachesTransportDisconnectWhenReactivationCannotComplete() throws Exception {
    var transport = new ScriptedSessionTransport(transportConfig, false);
    var client = new OpcUaClient(newClientConfig(50), transport);

    assertSame(client, client.connectAsync().get(5, TimeUnit.SECONDS));

    transport.fireConnectionStateChange(false);
    awaitState(client, State.Reactivating);

    CompletableFuture<OpcUaClient> disconnectFuture = client.disconnectAsync();

    assertSame(client, disconnectFuture.get(5, TimeUnit.SECONDS));
    assertTrue(transport.disconnectCalled());
  }

  @Test
  void reverseConnectDiscoveryTimeoutCleansUpWhenGetEndpointsDoesNotComplete() throws Exception {
    var transport =
        new FakeTransport(transportConfig, CompletableFuture.completedFuture(Unit.VALUE));
    var discoveryClient = new DiscoveryClient(endpoint(), transport);

    var future = OpcUaClient.discoverReverseConnectEndpoints(discoveryClient, transport, 50);

    assertTrue(transport.awaitRequest());

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> future.get(5, TimeUnit.SECONDS));

    assertInstanceOf(TimeoutException.class, ex.getCause());
    assertEquals(1, transport.requestCount());
    assertTrue(transport.disconnectCalled());
  }

  private static OpcUaClientConfig newClientConfig(long requestTimeout) {
    return OpcUaClientConfig.builder()
        .setEndpoint(endpoint())
        .setRequestTimeout(uint(requestTimeout))
        .build();
  }

  private static EndpointDescription endpoint() {
    var server =
        new ApplicationDescription(
            "urn:eclipse:milo:test-server",
            "urn:eclipse:milo:test-product",
            LocalizedText.english("test server"),
            ApplicationType.Server,
            null,
            null,
            new String[0]);

    return new EndpointDescription(
        ENDPOINT_URL,
        server,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[] {
          new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
        },
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }

  private static void awaitState(OpcUaClient client, State expected) throws Exception {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
    while (System.nanoTime() < deadline && client.getSessionFsm().getState() != expected) {
      Thread.sleep(25);
    }

    assertEquals(expected, client.getSessionFsm().getState());
  }

  private static ResponseHeader responseHeader() {
    return new ResponseHeader(DateTime.now(), uint(0), StatusCode.GOOD, null, null, null);
  }

  private static CreateSessionResponse createSessionResponse() {
    return new CreateSessionResponse(
        responseHeader(),
        new NodeId(1, "session"),
        new NodeId(1, "auth"),
        60_000.0,
        ByteString.of(new byte[] {1, 2, 3, 4}),
        ByteString.NULL_VALUE,
        new EndpointDescription[0],
        null,
        new SignatureData(null, null),
        uint(0));
  }

  private static ActivateSessionResponse activateSessionResponse() {
    return new ActivateSessionResponse(
        responseHeader(), ByteString.of(new byte[] {5, 6, 7, 8}), null, null);
  }

  private static ReadResponse readResponse() {
    return new ReadResponse(
        responseHeader(),
        new DataValue[] {
          new DataValue(new Variant(new String[] {"http://opcfoundation.org/UA/"})),
          new DataValue(new Variant(new String[] {"urn:eclipse:milo:test-server"}))
        },
        null);
  }

  private static final class FakeTransport implements OpcClientTransport {

    private final OpcClientTransportConfig config;
    private final CompletableFuture<Unit> connectFuture;
    private final CompletableFuture<UaResponseMessageType> requestFuture =
        new CompletableFuture<>();
    private final CountDownLatch requestSent = new CountDownLatch(1);
    private final AtomicInteger requestCount = new AtomicInteger();
    private final AtomicBoolean disconnectCalled = new AtomicBoolean();

    private FakeTransport(OpcClientTransportConfig config, CompletableFuture<Unit> connectFuture) {

      this.config = config;
      this.connectFuture = connectFuture;
    }

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      return connectFuture;
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      disconnectCalled.set(true);

      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      requestCount.incrementAndGet();
      requestSent.countDown();

      return requestFuture;
    }

    private boolean awaitRequest() throws InterruptedException {
      return requestSent.await(5, TimeUnit.SECONDS);
    }

    private int requestCount() {
      return requestCount.get();
    }

    private boolean disconnectCalled() {
      return disconnectCalled.get();
    }
  }

  private static final class ScriptedSessionTransport
      implements OpcClientTransport, ChannelStateObservable {

    private final OpcClientTransportConfig config;
    private final boolean stallFirstActivation;
    private final CopyOnWriteArrayList<ChannelStateObservable.TransitionListener>
        transitionListeners = new CopyOnWriteArrayList<>();
    private final AtomicInteger activationRequests = new AtomicInteger();
    private final AtomicBoolean disconnectCalled = new AtomicBoolean();
    private final CompletableFuture<UaResponseMessageType> stalledActivation =
        new CompletableFuture<>();

    private ScriptedSessionTransport(
        OpcClientTransportConfig config, boolean stallFirstActivation) {

      this.config = config;
      this.stallFirstActivation = stallFirstActivation;
    }

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      disconnectCalled.set(true);

      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      if (requestMessage instanceof CreateSessionRequest) {
        return CompletableFuture.completedFuture(createSessionResponse());
      } else if (requestMessage instanceof ActivateSessionRequest) {
        int request = activationRequests.incrementAndGet();
        if (stallFirstActivation || request > 1) {
          return stalledActivation;
        }

        return CompletableFuture.completedFuture(activateSessionResponse());
      } else if (requestMessage instanceof ReadRequest) {
        return CompletableFuture.completedFuture(readResponse());
      }

      return new CompletableFuture<>();
    }

    @Override
    public void addTransitionListener(ChannelStateObservable.TransitionListener listener) {
      transitionListeners.add(listener);
    }

    @Override
    public void removeTransitionListener(ChannelStateObservable.TransitionListener listener) {
      transitionListeners.remove(listener);
    }

    private void fireConnectionStateChange(boolean connected) {
      transitionListeners.forEach(listener -> listener.onConnectionStateChange(connected));
    }

    private boolean disconnectCalled() {
      return disconnectCalled.get();
    }
  }
}
