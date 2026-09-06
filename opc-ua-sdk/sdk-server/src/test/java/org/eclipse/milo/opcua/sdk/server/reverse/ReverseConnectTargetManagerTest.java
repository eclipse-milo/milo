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
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttempt;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptEvent;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectParameters;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
    // The failed attempt's retry evaluation may append a further "updated" on the executor.
    assertEquals(List.of("added", "updated"), List.copyOf(events).subList(0, 2));
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

  // UUIDs identify public targets, but a removed registration must never own a replacement's
  // channel.
  @Test
  void oldHandoffCannotAttachChannelToReplacementWithSameTargetId() throws Exception {
    try (ControlledExecution fixture = new ControlledExecution()) {
      fixture.manager.startup();
      fixture.runScheduledAttempt();
      fixture.transport.handoff(0);
      fixture.manager.remove(fixture.target.getId()).get(5, TimeUnit.SECONDS);
      fixture.manager.addTarget(fixture.target);
      fixture.runScheduledAttempt();
      EmbeddedChannel oldChannel = new EmbeddedChannel();
      EmbeddedChannel newChannel = new EmbeddedChannel();
      try {
        fixture.transport.channels.get(0).complete(oldChannel);
        assertFalse(oldChannel.isOpen(), "late channel from the removed registration must close");
        assertEquals(0, fixture.manager.snapshots().get(0).activeChannelCount());
        fixture.transport.handoff(1);
        fixture.transport.channels.get(1).complete(newChannel);
        assertTrue(newChannel.isOpen());
        assertEquals(1, fixture.manager.snapshots().get(0).activeChannelCount());
      } finally {
        oldChannel.close();
        newChannel.close();
      }
    }
  }

  // Removing in the HANDOFF/future-completion interval must still close the later channel.
  @Test
  void removalClosesChannelDeliveredAfterHandoffEvent() throws Exception {
    try (ControlledExecution fixture = new ControlledExecution()) {
      fixture.manager.startup();
      fixture.runScheduledAttempt();
      fixture.transport.handoff(0);
      fixture.manager.remove(fixture.target.getId()).get(5, TimeUnit.SECONDS);
      EmbeddedChannel channel = new EmbeddedChannel();
      try {
        fixture.transport.channels.get(0).complete(channel);
        assertFalse(channel.isOpen());
      } finally {
        channel.close();
      }
    }
  }

  /**
   * Attempt startup and retry-policy evaluation can block (hostname resolution, transport startup,
   * application-supplied policies). They must run on the server executor so a slow target cannot
   * delay unrelated timers on the shared scheduler.
   */
  @Nested
  class ExecutionBoundaries {

    @Test
    void timerCallbackOnlyDispatchesAndAttemptStartsOnExecutor() throws Exception {
      try (ControlledExecution fixture = new ControlledExecution()) {
        fixture.manager.startup();
        fixture.drainExecutor();

        fixture.fireTimer();

        assertTrue(fixture.transport.attempts.isEmpty(), "timer callback must not start attempt");
        assertEquals(1, fixture.executorTasks.size(), "attempt must be handed to the executor");

        fixture.drainExecutor();

        assertEquals(1, fixture.transport.attempts.size());
        assertEquals(List.of("executor"), fixture.transport.runners);
      }
    }

    @Test
    void retryPolicyEvaluatesOnExecutorAndSchedulesPolicyDelay() throws Exception {
      RecordingRetryPolicy policy = new RecordingRetryPolicy(5_000L);
      try (ControlledExecution fixture = new ControlledExecution(target(policy))) {
        fixture.manager.startup();
        fixture.runScheduledAttempt();
        fixture.listener.drain();

        fixture.transport.fail(0);

        assertTrue(policy.runners.isEmpty(), "policy must not run on the transport thread");
        assertEquals(1, fixture.scheduledDelays.size(), "retry must not be scheduled yet");

        fixture.drainExecutor();

        assertEquals(List.of("executor"), policy.runners);
        assertEquals(List.of(0L, 5_000L), fixture.scheduledDelays);
        assertEquals(List.of("attempt:FAILED", "updated"), fixture.listener.drain());
      }
    }

    @Test
    void activeChannelCloseEvaluatesReconnectPolicyOnExecutor() throws Exception {
      RecordingRetryPolicy policy = new RecordingRetryPolicy(7_000L);
      try (ControlledExecution fixture = new ControlledExecution(target(policy))) {
        fixture.manager.startup();
        fixture.runScheduledAttempt();
        EmbeddedChannel channel = new EmbeddedChannel();
        fixture.transport.handoff(0);
        fixture.transport.channels.get(0).complete(channel);
        fixture.drainExecutor();
        fixture.listener.drain();

        channel.close();

        assertTrue(policy.runners.isEmpty(), "policy must not run on the channel thread");

        fixture.drainExecutor();

        assertEquals(List.of("executor"), policy.runners);
        assertEquals(List.of(0L, 7_000L), fixture.scheduledDelays);
        assertEquals(List.of("attempt:CLOSED", "updated"), fixture.listener.drain());
      }
    }

    @Test
    void blockedRetryPolicyDoesNotOccupyScheduler() throws Exception {
      CountDownLatch policyEntered = new CountDownLatch(1);
      CountDownLatch releasePolicy = new CountDownLatch(1);
      ReverseConnectTarget blockingTarget =
          target(
              (target, event) -> {
                policyEntered.countDown();
                await(releasePolicy);
                return 5_000L;
              });

      try (ControlledExecution fixture =
          new ControlledExecution(blockingTarget, Executors.newSingleThreadExecutor())) {
        fixture.manager.startup();
        fixture.fireTimer();
        fixture.listener.awaitNotification("updated");
        fixture.listener.awaitNotification("updated");
        assertEquals(1, fixture.transport.attempts.size());

        fixture.transport.fail(0);
        assertTrue(policyEntered.await(3, TimeUnit.SECONDS));
        fixture.scheduledSignals.drainPermits();

        // The scheduler keeps servicing other targets while the policy blocks the executor.
        ReverseConnectTarget other = target(ReverseConnectRetryPolicy.registrationPeriod());
        fixture.manager.addTarget(other);
        fixture.scheduledSignals.drainPermits();
        fixture.fireTimer();

        verify(fixture.scheduler, never()).execute(any(Runnable.class));
        assertEquals(1, fixture.transport.attempts.size(), "executor is still blocked");

        releasePolicy.countDown();

        assertTrue(fixture.scheduledSignals.tryAcquire(3, TimeUnit.SECONDS), "retry scheduled");
        assertEquals(5_000L, fixture.scheduledDelays.get(fixture.scheduledDelays.size() - 1));
        assertTrue(fixture.transport.attemptsStarted.tryAcquire(2, 3, TimeUnit.SECONDS));
        assertEquals(2, fixture.transport.attempts.size());
      }
    }

    @Test
    void blockedAttemptStartDoesNotOccupySchedulerOrHoldLock() throws Exception {
      CountDownLatch connectEntered = new CountDownLatch(1);
      CountDownLatch releaseConnect = new CountDownLatch(1);

      try (ControlledExecution fixture =
          new ControlledExecution(defaultTarget(), Executors.newSingleThreadExecutor())) {
        fixture.transport.beforeConnect =
            () -> {
              connectEntered.countDown();
              await(releaseConnect);
            };
        fixture.manager.startup();

        fixture.fireTimer();

        assertTrue(connectEntered.await(3, TimeUnit.SECONDS));
        verify(fixture.scheduler, never()).execute(any(Runnable.class));

        ReverseConnectTargetHandle handle =
            new ReverseConnectTargetHandle(fixture.manager, fixture.target.getId());
        ReverseConnectTargetSnapshot paused =
            assertTimeoutPreemptively(Duration.ofSeconds(3), () -> handle.pause().get());
        assertTrue(paused.paused());

        releaseConnect.countDown();

        assertTrue(fixture.transport.attemptsStarted.tryAcquire(3, TimeUnit.SECONDS));
        verify(fixture.transport.attempts.get(0), timeout(3_000)).close();
        assertEquals(0, handle.snapshot().orElseThrow().activeChannelCount());
      }
    }
  }

  /**
   * Cancelling a timer cannot recall work already queued on the executor, so queued attempt and
   * retry tasks must revalidate ownership, generation, and lifecycle state before acting.
   */
  @Nested
  class QueuedWorkAfterLifecycleChange {

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetManagerTest#lifecycleChanges")
    void queuedAttemptDispatchIsHarmlessAndTargetRecovers(
        String name, Consumer<ControlledExecution> change, Consumer<ControlledExecution> recover)
        throws Exception {

      try (ControlledExecution fixture = new ControlledExecution()) {
        fixture.manager.startup();
        fixture.drainExecutor();
        fixture.fireTimer();
        assertEquals(1, fixture.executorTasks.size(), "attempt dispatch is queued");

        change.accept(fixture);
        fixture.drainExecutor();

        assertTrue(fixture.transport.attempts.isEmpty(), "stale dispatch must not start attempt");

        recover.accept(fixture);
        fixture.runScheduledAttempt();

        assertEquals(1, fixture.transport.attempts.size(), "target is not stranded");
      }
    }

    @Test
    void staleRetryEvaluationSkipsPolicyButDeliversTerminalEvent() throws Exception {
      RecordingRetryPolicy policy = new RecordingRetryPolicy(5_000L);
      try (ControlledExecution fixture = new ControlledExecution(target(policy))) {
        fixture.manager.startup();
        fixture.runScheduledAttempt();
        fixture.listener.drain();

        fixture.transport.fail(0);
        new ReverseConnectTargetHandle(fixture.manager, fixture.target.getId())
            .pause()
            .get(5, TimeUnit.SECONDS);
        fixture.drainExecutor();

        assertTrue(policy.runners.isEmpty(), "policy must not run for a paused target");
        assertEquals(List.of(0L), fixture.scheduledDelays, "no retry may be scheduled");
        assertEquals(List.of("updated", "attempt:FAILED"), fixture.listener.drain());
        assertNull(
            fixture.manager.snapshot(fixture.target.getId()).orElseThrow().nextAttemptTime());
      }
    }
  }

  /**
   * The server executor may reject work when saturated or shutting down. Rejection must leave the
   * target in a defined, recoverable state and still deliver the notifications listeners rely on.
   */
  @Nested
  class ExecutorRejection {

    @Test
    void rejectedAttemptDispatchReportsFailedAttemptAndReschedules() throws Exception {
      try (ControlledExecution fixture = new ControlledExecution()) {
        fixture.manager.startup();
        fixture.drainExecutor();
        fixture.listener.drain();
        fixture.rejectExecutor.set(true);

        fixture.fireTimer();

        assertTrue(fixture.transport.attempts.isEmpty());
        assertEquals(List.of("attempt:FAILED", "updated"), fixture.listener.drain());

        ReverseConnectAttemptEvent event = fixture.listener.events.get(0);
        assertEquals(1L, event.attemptNumber());
        assertEquals(new StatusCode(StatusCodes.Bad_ResourceUnavailable), event.statusCode());
        assertInstanceOf(RejectedExecutionException.class, event.exception());

        ReverseConnectTargetSnapshot snapshot =
            fixture.manager.snapshot(fixture.target.getId()).orElseThrow();
        assertEquals(
            new StatusCode(StatusCodes.Bad_ResourceUnavailable), snapshot.lastStatusCode());
        assertNotNull(snapshot.lastError());
        assertNotNull(snapshot.nextAttemptTime(), "target must be rescheduled");
        assertEquals(
            List.of(0L, 1_000L), fixture.scheduledDelays, "fallback uses the registration period");

        fixture.rejectExecutor.set(false);
        fixture.runScheduledAttempt();

        assertEquals(1, fixture.transport.attempts.size());
        fixture.manager.trigger(fixture.target.getId()).get(5, TimeUnit.SECONDS);
        assertEquals(1, fixture.transport.attempts.size(), "attempt already in progress");
        assertTrue(fixture.scheduledTasks.isEmpty());

        fixture.transport.fail(0);
        fixture.drainExecutor();
        assertEquals(2L, fixture.listener.events.get(1).attemptNumber());
      }
    }

    @Test
    void rejectedRetryEvaluationReschedulesWithRegistrationPeriod() throws Exception {
      RecordingRetryPolicy policy = new RecordingRetryPolicy(5_000L);
      try (ControlledExecution fixture = new ControlledExecution(target(policy))) {
        fixture.manager.startup();
        fixture.runScheduledAttempt();
        fixture.listener.drain();
        fixture.rejectExecutor.set(true);

        fixture.transport.fail(0);

        assertTrue(policy.runners.isEmpty(), "policy must not run on the rejecting thread");
        assertEquals(List.of(0L, 1_000L), fixture.scheduledDelays);
        assertEquals(List.of("attempt:FAILED", "updated"), fixture.listener.drain());

        ReverseConnectTargetSnapshot snapshot = fixture.listener.lastUpdated();
        assertNotNull(snapshot.nextAttemptTime());
        assertEquals(new StatusCode(StatusCodes.Bad_ConnectionRejected), snapshot.lastStatusCode());
      }
    }

    @Test
    void rejectedPostCloseEvaluationReschedulesWithRegistrationPeriod() throws Exception {
      RecordingRetryPolicy policy = new RecordingRetryPolicy(5_000L);
      try (ControlledExecution fixture = new ControlledExecution(target(policy))) {
        fixture.manager.startup();
        fixture.runScheduledAttempt();
        EmbeddedChannel channel = new EmbeddedChannel();
        fixture.transport.handoff(0);
        fixture.transport.channels.get(0).complete(channel);
        fixture.drainExecutor();
        fixture.listener.drain();
        fixture.rejectExecutor.set(true);

        channel.close();

        assertTrue(policy.runners.isEmpty());
        assertEquals(List.of(0L, 1_000L), fixture.scheduledDelays);
        assertEquals(List.of("attempt:CLOSED", "updated"), fixture.listener.drain());
        assertNotNull(fixture.listener.lastUpdated().nextAttemptTime());
      }
    }
  }

  static Stream<Arguments> lifecycleChanges() {
    return Stream.of(
        Arguments.of(
            "pause",
            (Consumer<ControlledExecution>)
                fixture ->
                    join(
                        new ReverseConnectTargetHandle(fixture.manager, fixture.target.getId())
                            .pause()),
            (Consumer<ControlledExecution>)
                fixture ->
                    join(
                        new ReverseConnectTargetHandle(fixture.manager, fixture.target.getId())
                            .resume())),
        Arguments.of(
            "remove",
            (Consumer<ControlledExecution>)
                fixture -> join(fixture.manager.remove(fixture.target.getId())),
            (Consumer<ControlledExecution>) fixture -> fixture.manager.addTarget(fixture.target)),
        Arguments.of(
            "update",
            (Consumer<ControlledExecution>)
                fixture -> join(fixture.manager.update(replacement(fixture.target))),
            (Consumer<ControlledExecution>) fixture -> {}),
        Arguments.of(
            "shutdown",
            (Consumer<ControlledExecution>) fixture -> fixture.manager.shutdown(),
            (Consumer<ControlledExecution>) fixture -> fixture.manager.startup()));
  }

  private static ReverseConnectTarget replacement(ReverseConnectTarget target) {
    return ReverseConnectTarget.builder()
        .setId(target.getId())
        .setClientListenerUrl("opc.tcp://localhost:12699")
        .setEndpointUrl(target.getEndpointUrl())
        .setRegistrationPeriod(target.getRegistrationPeriod())
        .setConnectTimeout(target.getConnectTimeout())
        .build();
  }

  private static <T> T join(CompletableFuture<T> future) {
    return assertDoesNotThrow(() -> future.get(5, TimeUnit.SECONDS));
  }

  private static ReverseConnectTarget defaultTarget() {
    return target("opc.tcp://localhost:12686/reverse-target-test", "opc.tcp://localhost:12687");
  }

  private static ReverseConnectTarget target(ReverseConnectRetryPolicy retryPolicy) {
    return ReverseConnectTarget.builder()
        .setClientListenerUrl("opc.tcp://localhost:12687")
        .setEndpointUrl("opc.tcp://localhost:12686/reverse-target-test")
        .setRegistrationPeriod(uint(1_000))
        .setConnectTimeout(uint(100))
        .setRetryPolicy(retryPolicy)
        .build();
  }

  /**
   * Manager fixture with a manual scheduler queue and, by default, a manual executor queue. Tests
   * fire timers and drain the executor explicitly, which makes the handoff between the two visible
   * and deterministic. A real executor can be supplied for tests that need work to actually block.
   */
  private static final class ControlledExecution implements AutoCloseable {

    static final ThreadLocal<String> RUNNER = new ThreadLocal<>();

    final Queue<Runnable> executorTasks = new ConcurrentLinkedQueue<>();
    final Queue<Runnable> scheduledTasks = new ConcurrentLinkedQueue<>();
    final List<Long> scheduledDelays = new CopyOnWriteArrayList<>();
    final Semaphore scheduledSignals = new Semaphore(0);
    final AtomicBoolean rejectExecutor = new AtomicBoolean();
    final RecordingListener listener = new RecordingListener();
    final ControlledTransport transport = new ControlledTransport();
    final ScheduledExecutorService scheduler = mock(ScheduledExecutorService.class);
    final ReverseConnectTarget target;
    final ReverseConnectTargetManager manager;

    private final @Nullable ExecutorService realExecutor;

    ControlledExecution() {
      this(defaultTarget(), null);
    }

    ControlledExecution(ReverseConnectTarget target) {
      this(target, null);
    }

    ControlledExecution(ReverseConnectTarget target, @Nullable ExecutorService realExecutor) {
      this.target = target;
      this.realExecutor = realExecutor;

      when(scheduler.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class)))
          .thenAnswer(
              invocation -> {
                scheduledTasks.add(invocation.getArgument(0));
                scheduledDelays.add(invocation.getArgument(1));
                scheduledSignals.release();
                return mock(ScheduledFuture.class);
              });

      manager =
          new ReverseConnectTargetManager(
              mock(ServerApplicationContext.class),
              () -> List.of(endpointDescription(target.getEndpointUrl())),
              profile -> transport,
              "urn:eclipse:milo:test:server:reverse-targets",
              realExecutor != null ? realExecutor : manualExecutor(),
              scheduler,
              Set.of(target));
      manager.addListener(listener);
    }

    private ExecutorService manualExecutor() {
      ExecutorService executor = mock(ExecutorService.class);
      doAnswer(
              invocation -> {
                if (rejectExecutor.get()) {
                  throw new RejectedExecutionException("saturated");
                }
                executorTasks.add(invocation.getArgument(0));
                return null;
              })
          .when(executor)
          .execute(any(Runnable.class));
      return executor;
    }

    /** Run the next scheduler timer callback on the calling thread, tagged as the scheduler. */
    void fireTimer() {
      runAs("scheduler", scheduledTasks.remove());
    }

    /** Run every queued executor task on the calling thread, tagged as the executor. */
    void drainExecutor() {
      Runnable task;
      while ((task = executorTasks.poll()) != null) {
        runAs("executor", task);
      }
    }

    void runScheduledAttempt() {
      fireTimer();
      drainExecutor();
    }

    private static void runAs(String runner, Runnable task) {
      RUNNER.set(runner);
      try {
        task.run();
      } finally {
        RUNNER.remove();
      }
    }

    @Override
    public void close() {
      manager.shutdown();
      if (realExecutor != null) {
        realExecutor.shutdownNow();
      }
    }
  }

  private static final class ControlledTransport extends OpcTcpServerTransport {
    final List<OpcTcpServerReverseConnectParameters> parameters = new CopyOnWriteArrayList<>();
    final List<OpcTcpServerReverseConnectAttempt> attempts = new CopyOnWriteArrayList<>();
    final List<CompletableFuture<Channel>> channels = new CopyOnWriteArrayList<>();
    final List<String> runners = new CopyOnWriteArrayList<>();
    final Semaphore attemptsStarted = new Semaphore(0);
    volatile Runnable beforeConnect = () -> {};

    ControlledTransport() {
      super(OpcTcpServerTransportConfig.newBuilder().build());
    }

    @Override
    public OpcTcpServerReverseConnectAttempt connectReverse(
        OpcTcpServerReverseConnectParameters parameters) {
      beforeConnect.run();
      runners.add(String.valueOf(ControlledExecution.RUNNER.get()));
      OpcTcpServerReverseConnectAttempt attempt = mock(OpcTcpServerReverseConnectAttempt.class);
      CompletableFuture<Channel> channel = new CompletableFuture<>();
      when(attempt.channelFuture()).thenReturn(channel);
      when(attempt.state()).thenReturn(OpcTcpServerReverseConnectAttemptState.CONNECTING);
      this.parameters.add(parameters);
      attempts.add(attempt);
      channels.add(channel);
      attemptsStarted.release();
      return attempt;
    }

    void handoff(int index) {
      when(attempts.get(index).state()).thenReturn(OpcTcpServerReverseConnectAttemptState.HANDOFF);
      emit(index, OpcTcpServerReverseConnectAttemptState.HANDOFF, null);
    }

    void fail(int index) {
      when(attempts.get(index).state()).thenReturn(OpcTcpServerReverseConnectAttemptState.FAILED);
      emit(
          index,
          OpcTcpServerReverseConnectAttemptState.FAILED,
          new StatusCode(StatusCodes.Bad_ConnectionRejected));
    }

    private void emit(
        int index, OpcTcpServerReverseConnectAttemptState state, @Nullable StatusCode statusCode) {
      parameters
          .get(index)
          .observer()
          .onStateTransition(
              new OpcTcpServerReverseConnectAttemptEvent(
                  UUID.randomUUID(), state, Instant.now(), statusCode, null, null));
    }
  }

  private static final class RecordingRetryPolicy implements ReverseConnectRetryPolicy {
    final List<String> runners = new CopyOnWriteArrayList<>();
    private final long delayMillis;

    RecordingRetryPolicy(long delayMillis) {
      this.delayMillis = delayMillis;
    }

    @Override
    public long getRetryDelayMillis(ReverseConnectTarget target, ReverseConnectAttemptEvent event) {
      runners.add(String.valueOf(ControlledExecution.RUNNER.get()));
      return delayMillis;
    }
  }

  private static final class RecordingListener implements ReverseConnectTargetListener {
    final BlockingQueue<String> notifications = new LinkedBlockingQueue<>();
    final List<ReverseConnectAttemptEvent> events = new CopyOnWriteArrayList<>();
    final List<ReverseConnectTargetSnapshot> updated = new CopyOnWriteArrayList<>();

    @Override
    public void onTargetAdded(ReverseConnectTargetSnapshot snapshot) {
      notifications.add("added");
    }

    @Override
    public void onTargetUpdated(ReverseConnectTargetSnapshot snapshot) {
      updated.add(snapshot);
      notifications.add("updated");
    }

    @Override
    public void onTargetRemoved(ReverseConnectTargetSnapshot snapshot) {
      notifications.add("removed");
    }

    @Override
    public void onAttemptEvent(ReverseConnectAttemptEvent event) {
      events.add(event);
      notifications.add("attempt:" + event.state());
    }

    /** Block, with a bound, until the next notification equals {@code expected}. */
    void awaitNotification(String expected) {
      String actual = assertDoesNotThrow(() -> notifications.poll(3, TimeUnit.SECONDS));
      assertEquals(expected, actual);
    }

    /** Remove and return all notifications recorded so far, in delivery order. */
    List<String> drain() {
      List<String> drained = new ArrayList<>();
      notifications.drainTo(drained);
      return drained;
    }

    ReverseConnectTargetSnapshot lastUpdated() {
      return updated.get(updated.size() - 1);
    }
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
