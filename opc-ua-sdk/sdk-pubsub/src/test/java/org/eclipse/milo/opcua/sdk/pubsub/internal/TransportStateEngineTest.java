/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent.Cause;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportStateListener;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Engine mapping of the {@code TransportStateListener} edges, exercised without a broker via a stub
 * transport that captures the context listener: a down edge fails the connection to {@code Error}
 * with the transport's status code (children Paused, publishing stops) and emits NO diagnostics
 * event — the state transition is the whole signal; an up edge recovers it (children reactivate,
 * publishing resumes). Duplicate edges are idempotent, an up without a prior down is a no-op
 * (initial connect), and edges on a disposed runtime are inert.
 */
class TransportStateEngineTest {

  private static final UUID FIELD_ID = new UUID(0L, 1L);

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  /** How long an idempotent stimulus is given to (wrongly) produce an event before passing. */
  private static final Duration SILENCE = Duration.ofMillis(300);

  private final BlockingQueue<PubSubStateChangeEvent> stateEvents = new LinkedBlockingQueue<>();
  private final BlockingQueue<PubSubDiagnosticsEvent> diagnosticsEvents =
      new LinkedBlockingQueue<>();

  private @Nullable PubSubService service;
  private @Nullable ExecutorService transportExecutor;

  @AfterEach
  void tearDown() throws Exception {
    if (service != null) {
      service.close();
      service = null;
    }
    if (transportExecutor != null) {
      transportExecutor.shutdown();
      assertTrue(transportExecutor.awaitTermination(10, TimeUnit.SECONDS));
      transportExecutor = null;
    }
  }

  @Test
  void downEdgeFailsConnectionAndUpEdgeRecovers() throws Exception {
    var transport = new StubTransport();
    startPublisher(transport);

    TransportStateListener listener = transport.listener.get();
    assertNotNull(listener, "the data-channel context must carry the engine listener");

    PubSubHandle connection = service.components().connection("conn").orElseThrow();
    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();

    // publishing is running before the outage
    assertNotNull(transport.sends.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS), "no send observed");
    stateEvents.clear();

    // DOWN: the connection fails with the transport's status code and the children pause
    var downCode = new StatusCode(StatusCodes.Bad_ServerNotConnected);
    listener.onTransportDown(downCode);

    awaitState(connection, PubSubState.Error);
    awaitState(group, PubSubState.Paused);
    awaitState(writer, PubSubState.Paused);

    PubSubStateChangeEvent errorEvent =
        awaitEvent(e -> e.component().equals(connection) && e.newState() == PubSubState.Error);
    assertEquals(PubSubState.Operational, errorEvent.oldState());
    assertEquals(downCode, errorEvent.statusCode());
    assertEquals(Cause.ERROR, errorEvent.cause());

    PubSubStateChangeEvent pausedEvent =
        awaitEvent(e -> e.component().equals(group) && e.newState() == PubSubState.Paused);
    assertEquals(Cause.PARENT, pausedEvent.cause());

    // the writer's Paused event is the LAST of the ordered cascade: once it is consumed, no
    // down-flow event is still in flight through the serialized dispatcher
    awaitEvent(e -> e.component().equals(writer) && e.newState() == PubSubState.Paused);

    // publishing stops once the deactivation lands (an in-flight cycle may still complete)
    awaitPublishQuiescence(transport.sends);

    // the down edge is a state transition only; no diagnostics event is emitted
    assertTrue(
        diagnosticsEvents.isEmpty(),
        "the transport-down edge must not emit diagnostics events: " + diagnosticsEvents);

    // a duplicate down edge (both channels share one session) is dropped
    stateEvents.clear();
    listener.onTransportDown(downCode);
    assertNull(stateEvents.poll(SILENCE.toMillis(), TimeUnit.MILLISECONDS));

    // UP: the connection recovers and the children reactivate
    listener.onTransportUp();

    awaitState(connection, PubSubState.Operational);
    awaitState(group, PubSubState.Operational);
    awaitState(writer, PubSubState.Operational);

    PubSubStateChangeEvent recoveredEvent =
        awaitEvent(
            e -> e.component().equals(connection) && e.newState() == PubSubState.Operational);
    assertEquals(PubSubState.Error, recoveredEvent.oldState());
    assertEquals(Cause.ERROR_RECOVERY, recoveredEvent.cause());

    // drain the ordered recovery cascade through its final event (writer Operational)
    awaitEvent(e -> e.component().equals(writer) && e.newState() == PubSubState.Operational);

    // publishing resumes
    transport.sends.clear();
    assertNotNull(
        transport.sends.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS),
        "publishing must resume after recovery");

    // a duplicate up edge is dropped
    stateEvents.clear();
    listener.onTransportUp();
    assertNull(stateEvents.poll(SILENCE.toMillis(), TimeUnit.MILLISECONDS));
  }

  @Test
  void upWithoutDownIsNoOp() throws Exception {
    var transport = new StubTransport();
    startPublisher(transport);

    TransportStateListener listener = transport.listener.get();
    assertNotNull(listener);

    PubSubHandle connection = service.components().connection("conn").orElseThrow();
    awaitState(connection, PubSubState.Operational);
    stateEvents.clear();

    // the initial connect delivers an up edge with no prior down: nothing to recover
    listener.onTransportUp();

    assertNull(stateEvents.poll(SILENCE.toMillis(), TimeUnit.MILLISECONDS));
    assertEquals(PubSubState.Operational, service.state(connection));
  }

  @Test
  void initialBrokerUpPublishesRetainedJsonStatusWithoutStateTransition() throws Exception {
    var transport = new StubTransport();
    startJsonMqttPublisher(transport);

    TransportStateListener listener = transport.listener.get();
    assertNotNull(listener);

    PubSubHandle connection = service.components().connection("conn").orElseThrow();
    awaitState(connection, PubSubState.Operational);
    stateEvents.clear();
    transport.sends.clear();

    listener.onTransportUp();

    Send statusSend = awaitSend(transport.sends, MessageAddress.Kind.STATUS);
    MessageAddress address = statusSend.address();
    assertNotNull(address);
    assertEquals("opcua/json/status/1", address.queueName());
    assertEquals(BrokerTransportQualityOfService.AtLeastOnce, address.deliveryGuarantee());
    assertTrue(address.retain());
    assertEquals(MessageAddress.CONTENT_TYPE_JSON, address.contentTypeHint());

    assertNull(stateEvents.poll(SILENCE.toMillis(), TimeUnit.MILLISECONDS));
    assertEquals(PubSubState.Operational, service.state(connection));
  }

  @Test
  void staleStatusSendCompletionDoesNotCancelCurrentStateRetry() throws Exception {
    var transport = new StubTransport();
    transport.pendingStatusSends = true;
    startJsonMqttPublisher(transport);

    TransportStateListener listener = transport.listener.get();
    assertNotNull(listener);

    PubSubHandle connection = service.components().connection("conn").orElseThrow();
    awaitState(connection, PubSubState.Operational);
    transport.sends.clear();

    listener.onTransportUp();
    Send operational = awaitSend(transport.sends, MessageAddress.Kind.STATUS);
    assertNotNull(operational.result());

    listener.onTransportDown(new StatusCode(StatusCodes.Bad_ServerNotConnected));
    awaitState(connection, PubSubState.Error);
    Send error = awaitSend(transport.sends, MessageAddress.Kind.STATUS);
    assertNotNull(error.result());

    error
        .result()
        .completeExceptionally(new UaException(StatusCodes.Bad_ServerNotConnected, "offline"));
    operational.result().complete(null);

    Send retry = awaitSend(transport.sends, MessageAddress.Kind.STATUS);
    MessageAddress retryAddress = retry.address();
    assertNotNull(retryAddress);
    assertEquals(MessageAddress.Kind.STATUS, retryAddress.kind());
    assertEquals("opcua/json/status/1", retryAddress.queueName());
  }

  @Test
  void downAfterShutdownIsInert() throws Exception {
    var transport = new StubTransport();
    startPublisher(transport);

    TransportStateListener listener = transport.listener.get();
    assertNotNull(listener);

    service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    stateEvents.clear();
    diagnosticsEvents.clear();

    listener.onTransportDown(new StatusCode(StatusCodes.Bad_ServerNotConnected));
    listener.onTransportUp();

    assertNull(stateEvents.poll(SILENCE.toMillis(), TimeUnit.MILLISECONDS));
    assertTrue(diagnosticsEvents.isEmpty());
  }

  // region fixtures

  /**
   * A transport that captures the engine's {@link TransportStateListener} from the data-channel
   * context (discovery contexts carry none) and completes every send successfully, counting them.
   */
  private static final class StubTransport implements TransportProvider {

    final AtomicReference<@Nullable TransportStateListener> listener = new AtomicReference<>();
    final BlockingQueue<Send> sends = new LinkedBlockingQueue<>();
    volatile boolean pendingStatusSends = false;

    @Override
    public String transportProfileUri() {
      return "urn:eclipse:milo:test:stub-transport";
    }

    @Override
    public boolean supports(PubSubConnectionConfig connection) {
      return true;
    }

    @Override
    public PublisherChannel openPublisher(PublisherTransportContext context) {
      TransportStateListener contextListener = context.transportStateListener();
      if (contextListener != null) {
        listener.set(contextListener);
      }
      return new PublisherChannel() {
        @Override
        public CompletableFuture<Void> send(ByteBuf message) {
          message.release();
          sends.add(new Send(null, null));
          return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletableFuture<Void> send(ByteBuf message, MessageAddress address) {
          message.release();
          CompletableFuture<Void> result =
              pendingStatusSends && address.kind() == MessageAddress.Kind.STATUS
                  ? new CompletableFuture<>()
                  : CompletableFuture.completedFuture(null);
          sends.add(new Send(address, result));
          return result;
        }

        @Override
        public CompletableFuture<Void> closeAsync() {
          return CompletableFuture.completedFuture(null);
        }
      };
    }

    @Override
    public SubscriberChannel openSubscriber(SubscriberTransportContext context) {
      TransportStateListener contextListener = context.transportStateListener();
      if (contextListener != null) {
        listener.set(contextListener);
      }
      return () -> CompletableFuture.completedFuture(null);
    }
  }

  private record Send(@Nullable MessageAddress address, @Nullable CompletableFuture<Void> result) {}

  private void startPublisher(StubTransport transport) throws Exception {
    transportExecutor = Executors.newSingleThreadExecutor();

    WriterGroupConfig group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(20))
            .dataSetWriter(
                DataSetWriterConfig.builder("W1")
                    .dataSet(new PublishedDataSetRef("PDS"))
                    .dataSetWriterId(ushort(1))
                    .build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(
                PublishedDataSetConfig.builder("PDS")
                    .field(
                        FieldDefinition.builder("F1")
                            .dataType(NodeIds.Int32)
                            .dataSetFieldId(FIELD_ID)
                            .build())
                    .build())
            .connection(
                PubSubConnectionConfig.udp("conn")
                    .publisherId(PublisherId.uint16(ushort(1)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(group)
                    .build())
            .build();

    service =
        PubSubService.create(
            config,
            PubSubBindings.builder()
                .source(
                    new PublishedDataSetRef("PDS"),
                    context ->
                        DataSetSnapshot.builder(context)
                            .field("F1", new DataValue(Variant.ofInt32(42)))
                            .build())
                .build(),
            PubSubServiceConfig.builder()
                .transportProvider(transport)
                .transportExecutor(transportExecutor)
                .build());

    service.addStateListener(stateEvents::add);
    service.addDiagnosticsListener(diagnosticsEvents::add);
    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    drainStartupEvents();
    diagnosticsEvents.clear();
  }

  private void startJsonMqttPublisher(StubTransport transport) throws Exception {
    transportExecutor = Executors.newSingleThreadExecutor();

    WriterGroupConfig group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(20))
            .messageSettings(JsonWriterGroupSettings.builder().build())
            .dataSetWriter(
                DataSetWriterConfig.builder("W1")
                    .dataSet(new PublishedDataSetRef("PDS"))
                    .dataSetWriterId(ushort(1))
                    .settings(JsonDataSetWriterSettings.builder().build())
                    .build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(
                PublishedDataSetConfig.builder("PDS")
                    .field(
                        FieldDefinition.builder("F1")
                            .dataType(NodeIds.Int32)
                            .dataSetFieldId(FIELD_ID)
                            .build())
                    .build())
            .connection(
                PubSubConnectionConfig.mqtt("conn")
                    .brokerUri(URI.create("mqtt://127.0.0.1:1883"))
                    .publisherId(PublisherId.uint16(ushort(1)))
                    .writerGroup(group)
                    .build())
            .build();

    service =
        PubSubService.create(
            config,
            PubSubBindings.builder()
                .source(
                    new PublishedDataSetRef("PDS"),
                    context ->
                        DataSetSnapshot.builder(context)
                            .field("F1", new DataValue(Variant.ofInt32(42)))
                            .build())
                .build(),
            PubSubServiceConfig.builder()
                .transportProvider(transport)
                .transportExecutor(transportExecutor)
                .build());

    service.addStateListener(stateEvents::add);
    service.addDiagnosticsListener(diagnosticsEvents::add);
    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    drainStartupEvents();
    diagnosticsEvents.clear();
  }

  private void drainStartupEvents() throws InterruptedException {
    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    awaitEvent(e -> e.component().equals(writer) && e.newState() == PubSubState.Operational);
    stateEvents.clear();
  }

  private void awaitState(PubSubHandle handle, PubSubState expected) throws InterruptedException {
    awaitTrue(
        () -> service.state(handle) == expected,
        "state of " + handle + " to become " + expected + ", is " + service.state(handle));
  }

  private PubSubStateChangeEvent awaitEvent(Predicate<PubSubStateChangeEvent> predicate)
      throws InterruptedException {

    var seen = new CopyOnWriteArrayList<PubSubStateChangeEvent>();
    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      PubSubStateChangeEvent event = stateEvents.poll(100, TimeUnit.MILLISECONDS);
      if (event != null) {
        if (predicate.test(event)) {
          return event;
        }
        seen.add(event);
      }
    }
    return fail("timed out waiting for a matching state event; saw " + List.copyOf(seen));
  }

  private static void awaitTrue(BooleanSupplier condition, String description)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (!condition.getAsBoolean()) {
      if (System.nanoTime() >= deadline) {
        fail("timed out waiting for: " + description);
      }
      Thread.sleep(25);
    }
  }

  /** Wait until the cancelled publish task quiesces: no send for a full silence window. */
  private static void awaitPublishQuiescence(BlockingQueue<Send> sends)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      if (sends.poll(SILENCE.toMillis(), TimeUnit.MILLISECONDS) == null) {
        return;
      }
    }
    fail("publishing did not stop after the transport-down edge");
  }

  private static Send awaitSend(BlockingQueue<Send> sends, MessageAddress.Kind kind)
      throws InterruptedException {

    var seen = new CopyOnWriteArrayList<Send>();
    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      Send send = sends.poll(100, TimeUnit.MILLISECONDS);
      if (send == null) {
        continue;
      }

      MessageAddress address = send.address();
      if (address != null && address.kind() == kind) {
        return send;
      }
      seen.add(send);
    }
    return fail("timed out waiting for " + kind + " send; saw " + List.copyOf(seen));
  }

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
