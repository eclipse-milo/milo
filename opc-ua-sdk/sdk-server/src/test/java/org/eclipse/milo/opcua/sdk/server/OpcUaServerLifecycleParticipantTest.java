/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class OpcUaServerLifecycleParticipantTest {

  private final List<OpcUaServer> servers = new ArrayList<>();

  @AfterEach
  void tearDown() throws Exception {
    // Teardown is a real assertion: any server still on this list must shut down cleanly. Tests
    // that deliberately fail shutdown assert on the failure themselves and then remove the server.
    for (OpcUaServer server : servers) {
      server.shutdown().get(5, TimeUnit.SECONDS);
    }
  }

  // The participant seam exists specifically to finish application initialization before a
  // transport can expose the server.
  @Test
  void participantStartupCompletesBeforeTransportBind() throws Exception {
    AtomicBoolean participantStarted = new AtomicBoolean();
    RecordingTransport transport = new RecordingTransport();
    transport.onBind = () -> assertTrue(participantStarted.get());
    OpcUaServer server = newServer(transport);

    server.addLifecycleParticipant(
        participant(
            () -> {
              assertTrue(server.getEventInstantiator().isRunning());
              assertTrue(
                  server
                      .getAddressSpaceManager()
                      .isRegistered(server.getOpcUaNamespace().getNodeManager()));
              participantStarted.set(true);
            },
            () -> {}));

    server.startup().get(5, TimeUnit.SECONDS);

    assertTrue(transport.bound);
  }

  @Test
  void participantsStartInRegistrationOrder() throws Exception {
    List<String> events = new ArrayList<>();
    OpcUaServer server = newServer(new RecordingTransport());
    server.addLifecycleParticipant(participant(() -> events.add("start-a"), () -> {}));
    server.addLifecycleParticipant(participant(() -> events.add("start-b"), () -> {}));
    server.addLifecycleParticipant(participant(() -> events.add("start-c"), () -> {}));

    server.startup().get(5, TimeUnit.SECONDS);

    assertEquals(List.of("start-a", "start-b", "start-c"), events);
  }

  // Shutdown must remove external visibility before application components release address-space
  // state, and reverse ordering lets dependent participants unwind safely.
  @Test
  void shutdownUnbindsTransportThenStopsParticipantsInReverseOrder() throws Exception {
    List<String> events = new ArrayList<>();
    RecordingTransport transport = new RecordingTransport(events);
    OpcUaServer server = newServer(transport);
    server.addLifecycleParticipant(
        participant(
            () -> {},
            () -> {
              assertTrue(
                  server
                      .getAddressSpaceManager()
                      .isRegistered(server.getOpcUaNamespace().getNodeManager()));
              events.add("stop-a");
            }));
    server.addLifecycleParticipant(
        participant(
            () -> {},
            () -> {
              assertTrue(
                  server
                      .getAddressSpaceManager()
                      .isRegistered(server.getOpcUaNamespace().getNodeManager()));
              events.add("stop-b");
            }));

    server.startup().get(5, TimeUnit.SECONDS);
    events.clear();
    server.shutdown().get(5, TimeUnit.SECONDS);

    assertEquals(List.of("unbind", "stop-b", "stop-a"), events);
  }

  @Test
  void participantStartupFailureRollsBackOnlySuccessfulParticipants() throws Exception {
    List<String> events = new ArrayList<>();
    RuntimeException startupFailure = new RuntimeException("start-b");
    RecordingTransport transport = new RecordingTransport(events);
    OpcUaServer server = newServer(transport);
    server.addLifecycleParticipant(
        participant(() -> events.add("start-a"), () -> events.add("stop-a")));
    server.addLifecycleParticipant(
        participant(
            () -> {
              events.add("start-b");
              throw startupFailure;
            },
            () -> events.add("stop-b")));
    server.addLifecycleParticipant(
        participant(() -> events.add("start-c"), () -> events.add("stop-c")));

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> server.startup().get(5, TimeUnit.SECONDS));

    assertSame(startupFailure, ex.getCause());
    assertEquals(List.of("start-a", "start-b", "stop-a"), events);
    assertFalse(transport.bound);

    server.shutdown().get(5, TimeUnit.SECONDS);
    assertEquals(List.of("start-a", "start-b", "stop-a"), events);
  }

  @Test
  void transportStartupFailureRollsBackStartedParticipants() throws Exception {
    List<String> events = new ArrayList<>();
    RecordingTransport transport = new RecordingTransport(events);
    transport.bindFailure = new Exception("bind");
    OpcUaServer server = newServer(transport);
    server.addLifecycleParticipant(
        participant(() -> events.add("participant-start"), () -> events.add("participant-stop")));

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> server.startup().get(5, TimeUnit.SECONDS));

    UaException cause = assertInstanceOf(UaException.class, ex.getCause());
    assertEquals(StatusCodes.Bad_ConfigurationError, cause.getStatusCode().value());
    assertEquals(List.of("participant-start", "bind", "unbind", "participant-stop"), events);
  }

  // The startup exception is the actionable failure; rollback exceptions remain available without
  // preventing later participants from being cleaned up.
  @Test
  void startupRollbackPreservesPrimaryFailureAndSuppressesCleanupFailures() {
    RuntimeException cleanupA = new RuntimeException("cleanup-a");
    RuntimeException cleanupB = new RuntimeException("cleanup-b");
    RuntimeException startupFailure = new RuntimeException("start-c");
    List<String> events = new ArrayList<>();
    OpcUaServer server = newServer(new RecordingTransport());
    server.addLifecycleParticipant(
        participant(
            () -> events.add("start-a"),
            () -> {
              events.add("stop-a");
              throw cleanupA;
            }));
    server.addLifecycleParticipant(
        participant(
            () -> events.add("start-b"),
            () -> {
              events.add("stop-b");
              throw cleanupB;
            }));
    server.addLifecycleParticipant(
        participant(
            () -> {
              throw startupFailure;
            },
            () -> {}));

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> server.startup().get(5, TimeUnit.SECONDS));

    assertSame(startupFailure, ex.getCause());
    assertEquals(List.of(cleanupB, cleanupA), List.of(startupFailure.getSuppressed()));
    assertEquals(List.of("start-a", "start-b", "stop-b", "stop-a"), events);
  }

  @Test
  void shutdownAttemptsEveryParticipantAndAggregatesFailures() throws Exception {
    RuntimeException cleanupA = new RuntimeException("cleanup-a");
    RuntimeException cleanupB = new RuntimeException("cleanup-b");
    List<String> events = new ArrayList<>();
    OpcUaServer server = newServer(new RecordingTransport());
    server.addLifecycleParticipant(
        participant(
            () -> {},
            () -> {
              events.add("stop-a");
              throw cleanupA;
            }));
    server.addLifecycleParticipant(
        participant(
            () -> {},
            () -> {
              events.add("stop-b");
              throw cleanupB;
            }));
    server.startup().get(5, TimeUnit.SECONDS);

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> server.shutdown().get(5, TimeUnit.SECONDS));

    assertSame(cleanupB, ex.getCause());
    assertEquals(List.of(cleanupA), List.of(cleanupB.getSuppressed()));
    assertEquals(List.of("stop-b", "stop-a"), events);

    // Shutdown ran every teardown step and its single-flight future is permanently failed; remove
    // the server so tearDown stays a strict success assertion for every other test.
    servers.remove(server);
  }

  @Test
  void registrationAndRemovalAreRejectedOnceStartupBegins() throws Exception {
    CountDownLatch startupEntered = new CountDownLatch(1);
    CountDownLatch releaseStartup = new CountDownLatch(1);
    Lifecycle blockingParticipant =
        participant(
            () -> {
              startupEntered.countDown();
              await(releaseStartup);
            },
            () -> {});
    Lifecycle lateParticipant = participant(() -> {}, () -> {});
    OpcUaServer server = newServer(new RecordingTransport());
    server.addLifecycleParticipant(blockingParticipant);

    //noinspection resource
    ExecutorService executor = Executors.newSingleThreadExecutor();
    try {
      Future<CompletableFuture<OpcUaServer>> firstStartup = executor.submit(server::startup);
      assertTrue(startupEntered.await(5, TimeUnit.SECONDS));

      assertThrows(
          IllegalStateException.class, () -> server.addLifecycleParticipant(lateParticipant));
      assertThrows(
          IllegalStateException.class,
          () -> server.removeLifecycleParticipant(blockingParticipant));

      releaseStartup.countDown();
      firstStartup.get(5, TimeUnit.SECONDS).get(5, TimeUnit.SECONDS);

      assertThrows(
          IllegalStateException.class, () -> server.addLifecycleParticipant(lateParticipant));
      assertThrows(
          IllegalStateException.class,
          () -> server.removeLifecycleParticipant(blockingParticipant));
    } finally {
      releaseStartup.countDown();
      executor.shutdownNow();
    }
  }

  @Test
  void removalBeforeStartupReturnsOwnershipToCaller() throws Exception {
    AtomicInteger starts = new AtomicInteger();
    AtomicInteger stops = new AtomicInteger();
    Lifecycle participant = participant(starts::incrementAndGet, stops::incrementAndGet);
    OpcUaServer server = newServer(new RecordingTransport());
    server.addLifecycleParticipant(participant);

    assertThrows(IllegalArgumentException.class, () -> server.addLifecycleParticipant(participant));
    assertTrue(server.removeLifecycleParticipant(participant));
    assertFalse(server.removeLifecycleParticipant(participant));

    server.startup().get(5, TimeUnit.SECONDS);
    server.shutdown().get(5, TimeUnit.SECONDS);

    assertEquals(0, starts.get());
    assertEquals(0, stops.get());
  }

  // A concurrent caller must observe the in-progress operation rather than starting participants a
  // second time.
  @Test
  void concurrentStartupCallsShareOneFutureAndStartParticipantsOnce() throws Exception {
    CountDownLatch startupEntered = new CountDownLatch(1);
    CountDownLatch releaseStartup = new CountDownLatch(1);
    AtomicInteger starts = new AtomicInteger();
    OpcUaServer server = newServer(new RecordingTransport());
    server.addLifecycleParticipant(
        participant(
            () -> {
              starts.incrementAndGet();
              startupEntered.countDown();
              await(releaseStartup);
            },
            () -> {}));

    //noinspection resource
    ExecutorService executor = Executors.newSingleThreadExecutor();
    try {
      Future<CompletableFuture<OpcUaServer>> firstCall = executor.submit(server::startup);
      assertTrue(startupEntered.await(5, TimeUnit.SECONDS));

      CompletableFuture<OpcUaServer> concurrentCall = server.startup();
      releaseStartup.countDown();
      CompletableFuture<OpcUaServer> originalCall = firstCall.get(5, TimeUnit.SECONDS);

      assertSame(originalCall, concurrentCall);
      assertEquals(server, concurrentCall.get(5, TimeUnit.SECONDS));
      assertEquals(1, starts.get());
    } finally {
      releaseStartup.countDown();
      executor.shutdownNow();
    }
  }

  @Test
  void shutdownBeforeStartupDoesNotInvokeParticipantsAndPreventsLaterStartup() throws Exception {
    AtomicInteger starts = new AtomicInteger();
    AtomicInteger stops = new AtomicInteger();
    OpcUaServer server = newServer(new RecordingTransport());
    server.addLifecycleParticipant(participant(starts::incrementAndGet, stops::incrementAndGet));

    server.shutdown().get(5, TimeUnit.SECONDS);

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> server.startup().get(5, TimeUnit.SECONDS));
    assertInstanceOf(IllegalStateException.class, ex.getCause());
    assertEquals(0, starts.get());
    assertEquals(0, stops.get());
    assertThrows(
        IllegalStateException.class,
        () -> server.addLifecycleParticipant(participant(() -> {}, () -> {})));
  }

  @Test
  void shutdownDuringStartupRollsBackParticipantsBeforeBinding() throws Exception {
    CountDownLatch startupEntered = new CountDownLatch(1);
    CountDownLatch releaseStartup = new CountDownLatch(1);
    List<String> events = new ArrayList<>();
    RecordingTransport transport = new RecordingTransport(events);
    OpcUaServer server = newServer(transport);
    server.addLifecycleParticipant(
        participant(() -> events.add("start-a"), () -> events.add("stop-a")));
    server.addLifecycleParticipant(
        participant(
            () -> {
              events.add("start-b");
              startupEntered.countDown();
              await(releaseStartup);
            },
            () -> events.add("stop-b")));

    //noinspection resource
    ExecutorService executor = Executors.newSingleThreadExecutor();
    try {
      Future<CompletableFuture<OpcUaServer>> startupCall = executor.submit(server::startup);
      assertTrue(startupEntered.await(5, TimeUnit.SECONDS));

      CompletableFuture<OpcUaServer> shutdown = server.shutdown();
      releaseStartup.countDown();

      ExecutionException startupFailure =
          assertThrows(
              ExecutionException.class,
              () -> startupCall.get(5, TimeUnit.SECONDS).get(5, TimeUnit.SECONDS));
      assertInstanceOf(IllegalStateException.class, startupFailure.getCause());
      assertEquals(server, shutdown.get(5, TimeUnit.SECONDS));
      assertEquals(List.of("start-a", "start-b", "stop-b", "stop-a"), events);
      assertFalse(transport.bound);
    } finally {
      releaseStartup.countDown();
      executor.shutdownNow();
    }
  }

  // A session listener that is the first shutdown caller while startup is still in flight must not
  // receive the real shutdown future: the deferred teardown later drains the listener queue from
  // the startup thread, so a callback blocked on that future would deadlock the drain.
  @Test
  void shutdownFromSessionListenerCallbackDuringStartupReturnsCompletedFuture() throws Exception {
    CountDownLatch startupEntered = new CountDownLatch(1);
    CountDownLatch releaseStartup = new CountDownLatch(1);
    OpcUaServer server = newServer(new RecordingTransport());
    server.addLifecycleParticipant(
        participant(
            () -> {
              startupEntered.countDown();
              await(releaseStartup);
            },
            () -> {}));

    //noinspection resource
    ExecutorService executor = Executors.newSingleThreadExecutor();
    try {
      Future<CompletableFuture<OpcUaServer>> startupCall = executor.submit(server::startup);
      assertTrue(startupEntered.await(5, TimeUnit.SECONDS));

      List<CompletableFuture<OpcUaServer>> callbackResult = new ArrayList<>();
      server
          .getSessionManager()
          .withSessionListenerCallback(() -> callbackResult.add(server.shutdown()));

      assertTrue(
          callbackResult.get(0).isDone(),
          "a listener callback must not receive a future it can block the listener drain on");

      releaseStartup.countDown();
      ExecutionException startupFailure =
          assertThrows(
              ExecutionException.class,
              () -> startupCall.get(5, TimeUnit.SECONDS).get(5, TimeUnit.SECONDS));
      assertInstanceOf(IllegalStateException.class, startupFailure.getCause());

      // The real single-flight future, observed from outside the callback, still completes.
      server.shutdown().get(5, TimeUnit.SECONDS);
    } finally {
      releaseStartup.countDown();
      executor.shutdownNow();
    }
  }

  // Namespace registration is an earlier child lifecycle and must be removed on later failure.
  @Test
  void failedNamespaceStartupUnregistersItsNodeManager() {
    OpcUaServer server = newServer(new RecordingTransport());
    var failure = new IllegalStateException("namespace child startup");
    var namespace =
        new ManagedNamespaceWithLifecycle(server, "urn:test:failed-namespace") {
          @Override
          public void onDataItemsCreated(List<DataItem> dataItems) {}

          @Override
          public void onDataItemsModified(List<DataItem> dataItems) {}

          @Override
          public void onDataItemsDeleted(List<DataItem> dataItems) {}

          @Override
          public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {}
        };
    namespace
        .getLifecycleManager()
        .addStartupTask(
            () -> {
              throw failure;
            });

    assertSame(failure, assertThrows(IllegalStateException.class, namespace::startup));
    assertFalse(server.getAddressSpaceManager().isRegistered(namespace.getNodeManager()));
    namespace.shutdown();
  }

  private OpcUaServer newServer(RecordingTransport transport) {
    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("127.0.0.1")
            .setBindPort(4840)
            .setHostname("localhost")
            .setPath("/milo")
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
            .build();

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:test:server")
            .setApplicationName(LocalizedText.english("test server"))
            .setProductUri("urn:eclipse:milo:test")
            .setCertificateManager(new DefaultCertificateManager())
            .setEndpoints(Set.of(endpoint))
            .build();

    OpcUaServer server = new OpcUaServer(config, transportProfile -> transport);
    servers.add(server);
    return server;
  }

  private static Lifecycle participant(Runnable startup, Runnable shutdown) {
    return new Lifecycle() {
      @Override
      public void startup() {
        startup.run();
      }

      @Override
      public void shutdown() {
        shutdown.run();
      }
    };
  }

  private static void await(CountDownLatch latch) {
    try {
      if (!latch.await(5, TimeUnit.SECONDS)) {
        throw new AssertionError("timed out waiting for latch");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new AssertionError("interrupted waiting for latch", e);
    }
  }

  private static final class RecordingTransport implements OpcServerTransport {

    private final List<String> events;
    private boolean bound;
    private Runnable onBind = () -> {};
    private Exception bindFailure;

    private RecordingTransport() {
      this(new ArrayList<>());
    }

    private RecordingTransport(List<String> events) {
      this.events = events;
    }

    @Override
    public void bind(ServerApplicationContext applicationContext, InetSocketAddress bindAddress)
        throws Exception {
      events.add("bind");
      onBind.run();
      if (bindFailure != null) {
        throw bindFailure;
      }
      bound = true;
    }

    @Override
    public void unbind() {
      events.add("unbind");
      bound = false;
    }
  }
}
