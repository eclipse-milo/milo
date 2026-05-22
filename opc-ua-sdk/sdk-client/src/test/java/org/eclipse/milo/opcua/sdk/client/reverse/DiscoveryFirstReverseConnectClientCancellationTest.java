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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class DiscoveryFirstReverseConnectClientCancellationTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test:reverse-cancel:server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:12685/milo";

  private ScheduledExecutorService scheduler;

  @AfterEach
  void tearDown() {
    if (scheduler != null) {
      scheduler.shutdownNow();
    }
  }

  @Test
  void cancellingPublicFutureClosesDiscoveryRegistration() throws Exception {
    ReverseConnectManager manager = newManager();

    try {
      DiscoveryFirstReverseConnectClient client =
          DiscoveryFirstReverseConnectClient.builder(manager).build();

      CompletableFuture<OpcUaClient> future = client.connectAsync();

      assertEquals(1, selectorRegistrationCount(manager));

      assertTrue(future.cancel(false));

      assertTrue(future.isCancelled());
      assertEquals(0, selectorRegistrationCount(manager));
    } finally {
      manager.shutdown();
    }
  }

  @Test
  void cancellingProductionFutureDisconnectsHiddenClientRegistration() throws Exception {
    ReverseConnectManager manager = newManager();
    DirectExecutorService directExecutor = new DirectExecutorService();

    try {
      DiscoveryFirstReverseConnectClient client =
          DiscoveryFirstReverseConnectClient.builder(manager)
              .setProductionTransport(
                  transport ->
                      transport.setExecutor(directExecutor).setScheduledExecutor(scheduler()))
              .build();

      CompletableFuture<OpcUaClient> future =
          invokeConnectProductionClient(client, discovery(), endpoint());

      waitUntil(() -> selectorRegistrationCountUnchecked(manager) == 1);

      assertTrue(future.cancel(false));

      waitUntil(() -> selectorRegistrationCountUnchecked(manager) == 0);
      assertTrue(future.isCancelled());
    } finally {
      directExecutor.shutdownNow();
      manager.shutdown();
    }
  }

  @Test
  void cancellingBridgeDisconnectsCompletedProductionClient() throws Exception {
    CompletableFuture<ReverseConnectDiscoveryResult> discoveryFuture = new CompletableFuture<>();
    CompletableFuture<OpcUaClient> bridge = newDiscoveryFirstConnectFuture(discoveryFuture);
    DisconnectRecordingClient client = new DisconnectRecordingClient();
    CompletableFuture<OpcUaClient> productionFuture = CompletableFuture.completedFuture(client);

    setProductionFuture(bridge, productionFuture);

    assertTrue(bridge.cancel(false));
    assertTrue(discoveryFuture.isCancelled());
    assertEquals(1, client.disconnectCount());
  }

  @Test
  void completingProductionAfterBridgeCancellationDisconnectsClient() throws Exception {
    CompletableFuture<ReverseConnectDiscoveryResult> discoveryFuture = new CompletableFuture<>();
    CompletableFuture<OpcUaClient> bridge = newDiscoveryFirstConnectFuture(discoveryFuture);
    DisconnectRecordingClient client = new DisconnectRecordingClient();

    assertTrue(bridge.cancel(false));

    completeProduction(bridge, client);

    assertEquals(1, client.disconnectCount());
  }

  @SuppressWarnings("unchecked")
  private static CompletableFuture<OpcUaClient> newDiscoveryFirstConnectFuture(
      CompletableFuture<ReverseConnectDiscoveryResult> discoveryFuture) throws Exception {

    Class<?> futureClass =
        Class.forName(
            DiscoveryFirstReverseConnectClient.class.getName() + "$DiscoveryFirstConnectFuture");
    Constructor<?> constructor = futureClass.getDeclaredConstructor(CompletableFuture.class);
    constructor.setAccessible(true);

    return (CompletableFuture<OpcUaClient>) constructor.newInstance(discoveryFuture);
  }

  private static void setProductionFuture(
      CompletableFuture<OpcUaClient> bridge, CompletableFuture<OpcUaClient> productionFuture)
      throws Exception {

    Method method =
        bridge.getClass().getDeclaredMethod("setProductionFuture", CompletableFuture.class);
    method.setAccessible(true);

    method.invoke(bridge, productionFuture);
  }

  private static void completeProduction(CompletableFuture<OpcUaClient> bridge, OpcUaClient client)
      throws Exception {

    Method method = bridge.getClass().getDeclaredMethod("completeProduction", OpcUaClient.class);
    method.setAccessible(true);

    method.invoke(bridge, client);
  }

  @SuppressWarnings("unchecked")
  private static CompletableFuture<OpcUaClient> invokeConnectProductionClient(
      DiscoveryFirstReverseConnectClient client,
      ReverseConnectDiscoveryResult discovery,
      EndpointDescription endpoint)
      throws Exception {

    Method method =
        DiscoveryFirstReverseConnectClient.class.getDeclaredMethod(
            "connectProductionClient",
            ReverseConnectDiscoveryResult.class,
            EndpointDescription.class);
    method.setAccessible(true);

    return (CompletableFuture<OpcUaClient>) method.invoke(client, discovery, endpoint);
  }

  @SuppressWarnings("unchecked")
  private static int selectorRegistrationCount(ReverseConnectManager manager) throws Exception {
    Field field = ReverseConnectManager.class.getDeclaredField("selectorRegistrations");
    field.setAccessible(true);

    Map<UUID, ?> selectorRegistrations = (Map<UUID, ?>) field.get(manager);

    return selectorRegistrations.size();
  }

  private static int selectorRegistrationCountUnchecked(ReverseConnectManager manager) {
    try {
      return selectorRegistrationCount(manager);
    } catch (Exception e) {
      throw new AssertionError(e);
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

  private static ReverseConnectDiscoveryResult discovery() {
    return new ReverseConnectDiscoveryResult(candidate(), List.of(endpoint()));
  }

  private static ReverseConnectCandidateSnapshot candidate() {
    Instant now = Instant.now();

    return new ReverseConnectCandidateSnapshot(
        UUID.randomUUID(),
        ReverseConnectCandidateState.PENDING,
        SERVER_URI,
        ENDPOINT_URL,
        null,
        null,
        now,
        now,
        null,
        null,
        null,
        null);
  }

  private static EndpointDescription endpoint() {
    ApplicationDescription server =
        new ApplicationDescription(
            SERVER_URI,
            "urn:eclipse:milo:test:reverse-cancel:product",
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

  private static void waitUntil(BooleanSupplier condition) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);

    while (!condition.getAsBoolean() && System.nanoTime() < deadline) {
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
    }

    assertTrue(condition.getAsBoolean());
  }

  private static final class DirectExecutorService extends AbstractExecutorService {

    private boolean shutdown;

    @Override
    public void shutdown() {
      shutdown = true;
    }

    @Override
    public @NonNull List<Runnable> shutdownNow() {
      shutdown = true;
      return List.of();
    }

    @Override
    public boolean isShutdown() {
      return shutdown;
    }

    @Override
    public boolean isTerminated() {
      return shutdown;
    }

    @Override
    public boolean awaitTermination(long timeout, @NonNull TimeUnit unit) {
      return shutdown;
    }

    @Override
    public void execute(@NonNull Runnable command) {
      command.run();
    }
  }

  private static final class DisconnectRecordingClient extends OpcUaClient {

    private final AtomicInteger disconnectCount = new AtomicInteger();

    private DisconnectRecordingClient() {
      super(OpcUaClientConfig.builder().setEndpoint(endpoint()).build(), new FakeClientTransport());
    }

    @Override
    public CompletableFuture<OpcUaClient> disconnectAsync() {
      disconnectCount.incrementAndGet();

      return CompletableFuture.completedFuture(this);
    }

    private int disconnectCount() {
      return disconnectCount.get();
    }
  }

  private static final class FakeClientTransport implements OpcClientTransport {

    private final OpcClientTransportConfig config =
        OpcTcpClientTransportConfig.newBuilder().build();

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
  }
}
