/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.CurrentChannelProvider;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ReverseConnectAcceptorTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test:reverse-acceptor:server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:12685/milo";

  private ScheduledExecutorService scheduler;

  @AfterEach
  void tearDown() {
    if (scheduler != null) {
      scheduler.shutdownNow();
    }
  }

  @Test
  void preDeliveryFailureReleasesKeyAndScansDuplicatePendingCandidates() throws Exception {
    ReverseConnectManager manager = newManager();
    List<UUID> failedCandidateIds = new ArrayList<>();
    UUID firstCandidateId = addPendingCandidate(manager, new EmbeddedChannel());
    UUID duplicateCandidateId = addPendingCandidate(manager, new EmbeddedChannel());
    ReverseConnectAcceptor acceptor =
        ReverseConnectAcceptor.builder(manager)
            .setDiscoveryTransport(
                transport -> {
                  throw new IllegalStateException("discovery failed");
                })
            .setErrorListener((candidate, failure) -> failedCandidateIds.add(candidate.id()))
            .build();

    try {
      acceptor.start();

      waitUntil(() -> manager.snapshot().pendingCandidates().isEmpty());

      assertTrue(failedCandidateIds.contains(firstCandidateId));
      assertTrue(failedCandidateIds.contains(duplicateCandidateId));
      assertEquals(0, activeKeyCount(acceptor));
    } finally {
      acceptor.stop();
      manager.shutdown();
    }
  }

  @Test
  void alreadyDisconnectedProductionTransportReleasesActiveKey() throws Exception {
    ReverseConnectManager manager = newManager();
    ReverseConnectAcceptor acceptor = ReverseConnectAcceptor.builder(manager).build();
    String key = "production-key";
    FakeClientTransport transport = new FakeClientTransport(null);
    OpcUaClient client = new OpcUaClient(clientConfig(), transport);

    try {
      acceptor.start();
      addActiveKey(acceptor, key);

      boolean transportLive = invokeObserveProductionTransportForKeyRelease(acceptor, key, client);

      assertFalse(
          transportLive,
          "observeProductionTransportForKeyRelease should report inactive transport");
      assertEquals(1, transport.listenerCount());
      assertEquals(0, activeKeyCount(acceptor));
    } finally {
      acceptor.stop();
      manager.shutdown();
    }
  }

  private ReverseConnectManager newManager() {
    return ReverseConnectManager.builder()
        .addBindAddress(new InetSocketAddress("127.0.0.1", 0))
        .setExecutor(Runnable::run)
        .setScheduler(scheduler())
        .build();
  }

  private ScheduledExecutorService scheduler() {
    if (scheduler == null) {
      scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    return scheduler;
  }

  @SuppressWarnings("unchecked")
  private static UUID addPendingCandidate(ReverseConnectManager manager, Channel channel)
      throws Exception {

    Field listenerStatesField = declaredField(ReverseConnectManager.class, "listenerStates");
    List<Object> listenerStates = (List<Object>) listenerStatesField.get(manager);
    Object listenerState = listenerStates.get(0);

    Class<?> candidateClass =
        Class.forName("org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager$Candidate");
    Constructor<?> constructor =
        candidateClass.getDeclaredConstructor(listenerState.getClass(), Channel.class);
    constructor.setAccessible(true);
    Object candidate = constructor.newInstance(listenerState, channel);

    setDeclaredField(candidate, "state", ReverseConnectCandidateState.PENDING);
    setDeclaredField(candidate, "serverUri", SERVER_URI);
    setDeclaredField(candidate, "endpointUrl", ENDPOINT_URL);
    setDeclaredField(candidate, "reverseHelloReceivedAt", Instant.now());

    UUID candidateId = (UUID) declaredField(candidateClass, "id").get(candidate);
    Map<UUID, Object> candidates =
        (Map<UUID, Object>) declaredField(ReverseConnectManager.class, "candidates").get(manager);
    Map<UUID, Object> pendingCandidates =
        (Map<UUID, Object>)
            declaredField(ReverseConnectManager.class, "pendingCandidates").get(manager);

    candidates.put(candidateId, candidate);
    pendingCandidates.put(candidateId, candidate);

    return candidateId;
  }

  @SuppressWarnings("unchecked")
  private static int activeKeyCount(ReverseConnectAcceptor acceptor) throws Exception {
    Set<String> activeKeys =
        (Set<String>) declaredField(ReverseConnectAcceptor.class, "activeKeys").get(acceptor);

    return activeKeys.size();
  }

  @SuppressWarnings("unchecked")
  private static void addActiveKey(ReverseConnectAcceptor acceptor, String key) throws Exception {
    Set<String> activeKeys =
        (Set<String>) declaredField(ReverseConnectAcceptor.class, "activeKeys").get(acceptor);

    activeKeys.add(key);
  }

  private static boolean invokeObserveProductionTransportForKeyRelease(
      ReverseConnectAcceptor acceptor, String key, OpcUaClient client) throws Exception {

    Method method =
        ReverseConnectAcceptor.class.getDeclaredMethod(
            "observeProductionTransportForKeyRelease", String.class, OpcUaClient.class);
    method.setAccessible(true);
    return (boolean) method.invoke(acceptor, key, client);
  }

  private static OpcUaClientConfig clientConfig() {
    return OpcUaClientConfig.builder().setEndpoint(endpoint()).build();
  }

  private static EndpointDescription endpoint() {
    ApplicationDescription server =
        new ApplicationDescription(
            SERVER_URI,
            "urn:eclipse:milo:test:reverse-acceptor:product",
            LocalizedText.english("test server"),
            ApplicationType.Server,
            null,
            null,
            null);

    UserTokenPolicy anonymousPolicy =
        new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);

    return new EndpointDescription(
        ENDPOINT_URL,
        server,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[] {anonymousPolicy},
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }

  private static void setDeclaredField(Object target, String name, Object value) throws Exception {
    Field field = declaredField(target.getClass(), name);
    field.set(target, value);
  }

  private static Field declaredField(Class<?> targetClass, String name) throws Exception {
    Field field = targetClass.getDeclaredField(name);
    field.setAccessible(true);
    return field;
  }

  private static void waitUntil(BooleanSupplier condition) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);

    while (!condition.getAsBoolean() && System.nanoTime() < deadline) {
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
    }

    assertTrue(condition.getAsBoolean());
  }

  private static final class FakeClientTransport
      implements OpcClientTransport, ChannelStateObservable, CurrentChannelProvider {

    private final OpcClientTransportConfig config =
        OpcTcpClientTransportConfig.newBuilder().build();
    private final List<TransitionListener> listeners = new ArrayList<>();
    private final @Nullable Channel currentChannel;

    private FakeClientTransport(@Nullable Channel currentChannel) {
      this.currentChannel = currentChannel;
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
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      return CompletableFuture.failedFuture(new UnsupportedOperationException());
    }

    @Override
    public void addTransitionListener(TransitionListener listener) {
      listeners.add(listener);
    }

    @Override
    public void removeTransitionListener(TransitionListener listener) {
      listeners.remove(listener);
    }

    @Override
    public @Nullable Channel getCurrentChannel() {
      return currentChannel;
    }

    private int listenerCount() {
      return listeners.size();
    }
  }
}
