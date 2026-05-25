/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.reverse;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.netty.channel.embedded.EmbeddedChannel;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttempt;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectParameters;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ReverseConnectTargetManagerTest {

  private ExecutorService listenerExecutor;
  private ScheduledExecutorService scheduler;

  @AfterEach
  void tearDown() {
    if (listenerExecutor != null) {
      listenerExecutor.shutdownNow();
    }
    if (scheduler != null) {
      scheduler.shutdownNow();
    }
  }

  @Test
  void updateWithScheduledAttemptAdvancesGenerationOnce() throws Exception {
    String endpointUrl = "opc.tcp://localhost:12686/reverse-target-test";

    ReverseConnectTarget target = target(endpointUrl, "opc.tcp://localhost:12687");
    ReverseConnectTarget replacement = target(endpointUrl, "opc.tcp://localhost:12688");
    replacement =
        ReverseConnectTarget.builder()
            .setId(target.getId())
            .setClientListenerUrl(replacement.getClientListenerUrl())
            .setEndpointUrl(replacement.getEndpointUrl())
            .setRegistrationPeriod(replacement.getRegistrationPeriod())
            .setConnectTimeout(replacement.getConnectTimeout())
            .build();

    EndpointDescription endpointDescription = endpointDescription(endpointUrl);

    listenerExecutor = Executors.newSingleThreadExecutor();
    scheduler = mock(ScheduledExecutorService.class);
    ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);
    when(scheduler.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class)))
        .thenAnswer(invocation -> scheduledFuture);

    ReverseConnectTargetManager manager =
        new ReverseConnectTargetManager(
            mock(ServerApplicationContext.class),
            () -> List.of(endpointDescription),
            transportProfile ->
                new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()),
            "urn:eclipse:milo:test:server:reverse-targets",
            listenerExecutor,
            scheduler,
            Set.of(target));

    manager.startup();
    long generationBeforeUpdate = generation(manager, target.getId());

    manager.update(replacement).get(5, TimeUnit.SECONDS);

    assertEquals(generationBeforeUpdate + 1L, generation(manager, target.getId()));
  }

  @Test
  void pauseWithScheduledAttemptAdvancesGenerationOnce() throws Exception {
    String endpointUrl = "opc.tcp://localhost:12686/reverse-target-test";

    ReverseConnectTarget target = target(endpointUrl, "opc.tcp://localhost:12687");
    EndpointDescription endpointDescription = endpointDescription(endpointUrl);

    listenerExecutor = Executors.newSingleThreadExecutor();
    scheduler = mock(ScheduledExecutorService.class);
    ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);
    when(scheduler.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class)))
        .thenAnswer(invocation -> scheduledFuture);

    ReverseConnectTargetManager manager =
        new ReverseConnectTargetManager(
            mock(ServerApplicationContext.class),
            () -> List.of(endpointDescription),
            transportProfile ->
                new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()),
            "urn:eclipse:milo:test:server:reverse-targets",
            listenerExecutor,
            scheduler,
            Set.of(target));

    manager.startup();
    long generationBeforePause = generation(manager, target.getId());

    new ReverseConnectTargetHandle(manager, target.getId()).pause().get(5, TimeUnit.SECONDS);

    assertEquals(generationBeforePause + 1L, generation(manager, target.getId()));
  }

  @Test
  void resumeValidatesPausedTargetBeforeScheduling() {
    String configuredEndpointUrl = "opc.tcp://localhost:12686/reverse-target-test";
    String invalidEndpointUrl = "opc.tcp://localhost:12686/not-configured";

    ReverseConnectTarget target =
        ReverseConnectTarget.builder()
            .setClientListenerUrl("opc.tcp://localhost:12687")
            .setEndpointUrl(invalidEndpointUrl)
            .setRegistrationPeriod(uint(1_000))
            .setConnectTimeout(uint(100))
            .setPaused(true)
            .build();

    EndpointDescription endpointDescription = mock(EndpointDescription.class);
    when(endpointDescription.getEndpointUrl()).thenReturn(configuredEndpointUrl);
    when(endpointDescription.getTransportProfileUri())
        .thenReturn(TransportProfile.TCP_UASC_UABINARY.getUri());

    listenerExecutor = Executors.newSingleThreadExecutor();
    scheduler = mock(ScheduledExecutorService.class);

    ReverseConnectTargetManager manager =
        new ReverseConnectTargetManager(
            mock(ServerApplicationContext.class),
            () -> List.of(endpointDescription),
            transportProfile ->
                new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()),
            "urn:eclipse:milo:test:server:reverse-targets",
            listenerExecutor,
            scheduler,
            Set.of(target));

    manager.startup();

    ReverseConnectTargetHandle handle = new ReverseConnectTargetHandle(manager, target.getId());

    ExecutionException exception = assertFailedFuture(handle::resume);
    assertInstanceOf(IllegalArgumentException.class, exception.getCause());
    assertTrue(exception.getCause().getMessage().contains(invalidEndpointUrl));

    ReverseConnectTargetSnapshot snapshot = handle.snapshot().orElseThrow();
    assertTrue(snapshot.paused());
    assertNull(snapshot.nextAttemptTime());

    verify(scheduler, never()).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
  }

  @Test
  void resumeValidatesPausedTargetEvenWithActiveChannel() throws Exception {
    String configuredEndpointUrl = "opc.tcp://localhost:12686/reverse-target-test";
    String invalidEndpointUrl = "opc.tcp://localhost:12686/not-configured";

    ReverseConnectTarget target = target(configuredEndpointUrl, "opc.tcp://localhost:12687");
    ReverseConnectTarget replacement =
        ReverseConnectTarget.builder()
            .setId(target.getId())
            .setClientListenerUrl(target.getClientListenerUrl())
            .setEndpointUrl(invalidEndpointUrl)
            .setRegistrationPeriod(target.getRegistrationPeriod())
            .setConnectTimeout(target.getConnectTimeout())
            .setPaused(true)
            .build();

    listenerExecutor = Executors.newSingleThreadExecutor();
    scheduler = mock(ScheduledExecutorService.class);
    ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);
    when(scheduler.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class)))
        .thenAnswer(invocation -> scheduledFuture);

    ReverseConnectTargetManager manager =
        new ReverseConnectTargetManager(
            mock(ServerApplicationContext.class),
            () -> List.of(endpointDescription(configuredEndpointUrl)),
            transportProfile ->
                new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()),
            "urn:eclipse:milo:test:server:reverse-targets",
            listenerExecutor,
            scheduler,
            Set.of(target));
    EmbeddedChannel activeChannel = new EmbeddedChannel();

    try {
      manager.startup();
      addActiveChannel(manager, target.getId(), activeChannel);

      manager.update(replacement).get(5, TimeUnit.SECONDS);

      ReverseConnectTargetHandle handle = new ReverseConnectTargetHandle(manager, target.getId());
      ExecutionException exception = assertFailedFuture(handle::resume);

      assertInstanceOf(IllegalArgumentException.class, exception.getCause());
      assertTrue(exception.getCause().getMessage().contains(invalidEndpointUrl));

      ReverseConnectTargetSnapshot snapshot = handle.snapshot().orElseThrow();
      assertTrue(snapshot.paused());
      assertEquals(1, snapshot.activeChannelCount());
    } finally {
      activeChannel.close();
    }
  }

  @Test
  void shutdownClearsPendingHandoffAttempts() throws Exception {
    String endpointUrl = "opc.tcp://localhost:12686/reverse-target-test";

    ReverseConnectTarget target = target(endpointUrl, "opc.tcp://localhost:12687");
    EndpointDescription endpointDescription = endpointDescription(endpointUrl);

    listenerExecutor = Executors.newSingleThreadExecutor();
    scheduler = mock(ScheduledExecutorService.class);
    ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);
    when(scheduler.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class)))
        .thenAnswer(invocation -> scheduledFuture);

    ReverseConnectTargetManager manager =
        new ReverseConnectTargetManager(
            mock(ServerApplicationContext.class),
            () -> List.of(endpointDescription),
            transportProfile ->
                new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()),
            "urn:eclipse:milo:test:server:reverse-targets",
            listenerExecutor,
            scheduler,
            Set.of(target));

    manager.startup();

    addPendingHandoffAttempt(manager, target.getId(), 1L, 0L);
    assertEquals(1, pendingHandoffAttemptCount(manager, target.getId()));

    manager.shutdown();

    assertEquals(
        0,
        pendingHandoffAttemptCount(manager, target.getId()),
        "shutdown must clear pendingHandoffAttempts so a subsequent startup can schedule the"
            + " target again");
  }

  @Test
  void targetAddedCannotBeOvertakenByFirstUpdateOnConcurrentExecutor() throws Exception {
    String endpointUrl = "opc.tcp://localhost:12686/reverse-target-test";
    ReverseConnectTarget target = target(endpointUrl, "opc.tcp://localhost:12687");
    AtomicReference<Runnable> scheduledAttempt = new AtomicReference<>();
    CountDownLatch addedEntered = new CountDownLatch(1);
    CountDownLatch releaseAdded = new CountDownLatch(1);
    CountDownLatch updatedEntered = new CountDownLatch(1);
    List<String> events = new CopyOnWriteArrayList<>();

    listenerExecutor = Executors.newFixedThreadPool(2);
    scheduler = mock(ScheduledExecutorService.class);
    ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);
    when(scheduler.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class)))
        .thenAnswer(
            invocation -> {
              scheduledAttempt.set(invocation.getArgument(0));
              return scheduledFuture;
            });

    ReverseConnectTargetManager manager =
        new ReverseConnectTargetManager(
            mock(ServerApplicationContext.class),
            () -> List.of(endpointDescription(endpointUrl)),
            transportProfile -> new FailingReverseTransport(),
            "urn:eclipse:milo:test:server:reverse-targets",
            listenerExecutor,
            scheduler,
            Set.of());

    manager.addListener(
        new ReverseConnectTargetListener() {
          @Override
          public void onTargetAdded(ReverseConnectTargetSnapshot snapshot) {
            addedEntered.countDown();
            await(releaseAdded);
            events.add("added");
          }

          @Override
          public void onTargetUpdated(ReverseConnectTargetSnapshot snapshot) {
            events.add("updated");
            updatedEntered.countDown();
          }
        });

    manager.startup();
    manager.addTarget(target);

    assertTrue(addedEntered.await(3, TimeUnit.SECONDS));
    assertNotNull(scheduledAttempt.get());

    scheduledAttempt.get().run();

    assertFalse(updatedEntered.await(200, TimeUnit.MILLISECONDS));

    releaseAdded.countDown();

    assertTrue(updatedEntered.await(3, TimeUnit.SECONDS));
    assertEquals(List.of("added", "updated"), events);
  }

  @Test
  void removedHandleMethodsReturnFailedFutures() throws Exception {
    ReverseConnectTarget target =
        ReverseConnectTarget.builder()
            .setClientListenerUrl("opc.tcp://localhost:12687")
            .setEndpointUrl("opc.tcp://localhost:12686/reverse-target-test")
            .setRegistrationPeriod(uint(1_000))
            .setConnectTimeout(uint(100))
            .build();

    listenerExecutor = Executors.newSingleThreadExecutor();
    scheduler = Executors.newSingleThreadScheduledExecutor();

    ReverseConnectTargetManager manager =
        new ReverseConnectTargetManager(
            mock(ServerApplicationContext.class),
            List::of,
            transportProfile ->
                new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()),
            "urn:eclipse:milo:test:server:reverse-targets",
            listenerExecutor,
            scheduler,
            Set.of(target));

    ReverseConnectTargetHandle handle = new ReverseConnectTargetHandle(manager, target.getId());

    handle.remove().get(5, TimeUnit.SECONDS);

    assertUnknownTargetFailure(handle::pause, target.getId());
    assertUnknownTargetFailure(handle::resume, target.getId());
    assertUnknownTargetFailure(handle::trigger, target.getId());
    assertUnknownTargetFailure(handle::remove, target.getId());
  }

  private static void assertUnknownTargetFailure(
      Supplier<CompletableFuture<ReverseConnectTargetSnapshot>> operation, Object targetId) {

    ExecutionException exception = assertFailedFuture(operation);
    assertInstanceOf(IllegalArgumentException.class, exception.getCause());
    assertTrue(exception.getCause().getMessage().contains("unknown Reverse Connect target id"));
    assertTrue(exception.getCause().getMessage().contains(targetId.toString()));
  }

  private static ExecutionException assertFailedFuture(
      Supplier<CompletableFuture<ReverseConnectTargetSnapshot>> operation) {

    CompletableFuture<ReverseConnectTargetSnapshot> future = assertDoesNotThrow(operation::get);

    return assertThrows(ExecutionException.class, () -> future.get(5, TimeUnit.SECONDS));
  }

  private static void await(CountDownLatch latch) {
    try {
      latch.await(3, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new AssertionError(e);
    }
  }

  private static ReverseConnectTarget target(String endpointUrl, String clientListenerUrl) {
    return ReverseConnectTarget.builder()
        .setClientListenerUrl(clientListenerUrl)
        .setEndpointUrl(endpointUrl)
        .setRegistrationPeriod(uint(1_000))
        .setConnectTimeout(uint(100))
        .build();
  }

  private static EndpointDescription endpointDescription(String endpointUrl) {
    EndpointDescription endpointDescription = mock(EndpointDescription.class);
    when(endpointDescription.getEndpointUrl()).thenReturn(endpointUrl);
    when(endpointDescription.getTransportProfileUri())
        .thenReturn(TransportProfile.TCP_UASC_UABINARY.getUri());
    return endpointDescription;
  }

  private static long generation(ReverseConnectTargetManager manager, UUID targetId)
      throws ReflectiveOperationException {

    Object record = record(manager, targetId);
    Field generationField = record.getClass().getDeclaredField("generation");
    generationField.setAccessible(true);

    return generationField.getLong(record);
  }

  @SuppressWarnings("unchecked")
  private static void addActiveChannel(
      ReverseConnectTargetManager manager, UUID targetId, EmbeddedChannel channel)
      throws ReflectiveOperationException {

    Object record = record(manager, targetId);
    Field activeChannelsField = record.getClass().getDeclaredField("activeChannels");
    activeChannelsField.setAccessible(true);
    Map<String, EmbeddedChannel> activeChannels =
        (Map<String, EmbeddedChannel>) activeChannelsField.get(record);

    activeChannels.put(channel.id().asLongText(), channel);
  }

  @SuppressWarnings("unchecked")
  private static void addPendingHandoffAttempt(
      ReverseConnectTargetManager manager, UUID targetId, long number, long generation)
      throws ReflectiveOperationException {

    Object record = record(manager, targetId);
    Field pendingField = record.getClass().getDeclaredField("pendingHandoffAttempts");
    pendingField.setAccessible(true);
    Set<Object> pending = (Set<Object>) pendingField.get(record);

    Class<?> attemptKeyClass =
        Class.forName(ReverseConnectTargetManager.class.getName() + "$AttemptKey");
    var ctor = attemptKeyClass.getDeclaredConstructor(long.class, long.class);
    ctor.setAccessible(true);
    Object attemptKey = ctor.newInstance(number, generation);

    pending.add(attemptKey);
  }

  @SuppressWarnings("unchecked")
  private static int pendingHandoffAttemptCount(ReverseConnectTargetManager manager, UUID targetId)
      throws ReflectiveOperationException {

    Object record = record(manager, targetId);
    Field pendingField = record.getClass().getDeclaredField("pendingHandoffAttempts");
    pendingField.setAccessible(true);
    Set<Object> pending = (Set<Object>) pendingField.get(record);

    return pending.size();
  }

  @SuppressWarnings("unchecked")
  private static Object record(ReverseConnectTargetManager manager, UUID targetId)
      throws ReflectiveOperationException {

    Field recordsField = ReverseConnectTargetManager.class.getDeclaredField("records");
    recordsField.setAccessible(true);

    Map<UUID, ?> records = (Map<UUID, ?>) recordsField.get(manager);

    return records.get(targetId);
  }

  private static final class FailingReverseTransport extends OpcTcpServerTransport {

    private FailingReverseTransport() {
      super(OpcTcpServerTransportConfig.newBuilder().build());
    }

    @Override
    public OpcTcpServerReverseConnectAttempt connectReverse(
        OpcTcpServerReverseConnectParameters parameters) {

      throw new IllegalStateException("reverse attempt intentionally disabled");
    }
  }
}
