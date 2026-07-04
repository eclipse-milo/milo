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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import java.net.DatagramSocket;
import java.net.SocketException;
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
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent.Cause;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportStateListener;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Cascade half of the R12 State* attribution matrix (D2/D30/D33), on a publisher tree
 * connection/group/writer: an explicit {@code disable}/{@code enable} of a NON-leaf component ticks
 * ByMethod on exactly that component while every descendant ticks the *ByParent counters (cause
 * PARENT on the cascaded events, final-hop rule: exactly one Operational-entry tick per component),
 * and a transport-driven Error/recovery cycle ticks StateError and StateOperationalFromError on the
 * connection only — descendants pause and recover with PARENT attribution and never tick the
 * FromError or ByMethod counters.
 *
 * <p>Complements {@code StateCauseAttributionTest} (startup, leaf-method, watchdog-error, and
 * dispose rows on a reader tree) and {@code TransportStateEngineTest} (R16 edge semantics and event
 * causes, no counter rows).
 */
class StateCauseCascadeAttributionTest {

  private static final UUID FIELD_ID = new UUID(0L, 1L);

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final String CONNECTION = "conn";
  private static final String GROUP = "conn/WG";
  private static final String WRITER = "conn/WG/W1";

  private final BlockingQueue<PubSubStateChangeEvent> stateEvents = new LinkedBlockingQueue<>();

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
  void connectionDisableEnableTicksByMethodOnItAndByParentOnDescendants() throws Exception {
    startPublisher(new StubTransport());

    PubSubHandle connection = service.components().connection("conn").orElseThrow();
    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();

    ComponentDiagnostics connBefore = component(CONNECTION);
    ComponentDiagnostics groupBefore = component(GROUP);
    ComponentDiagnostics writerBefore = component(WRITER);

    // disable the CONNECTION: it ticks StateDisabledByMethod, its descendants pause with
    // PARENT attribution — never StateDisabledByMethod on a cascaded component
    service.disable(connection);
    awaitState(connection, PubSubState.Disabled);
    awaitState(group, PubSubState.Paused);
    awaitState(writer, PubSubState.Paused);

    awaitCounters(
        "disable(connection) attribution",
        () ->
            component(CONNECTION).stateDisabledByMethod() == connBefore.stateDisabledByMethod() + 1
                && component(GROUP).statePausedByParent() == groupBefore.statePausedByParent() + 1
                && component(WRITER).statePausedByParent()
                    == writerBefore.statePausedByParent() + 1);
    assertEquals(groupBefore.stateDisabledByMethod(), component(GROUP).stateDisabledByMethod());
    assertEquals(writerBefore.stateDisabledByMethod(), component(WRITER).stateDisabledByMethod());

    awaitEvent(
        e ->
            e.component().equals(connection)
                && e.newState() == PubSubState.Disabled
                && e.cause() == Cause.METHOD);
    awaitEvent(
        e ->
            e.component().equals(writer)
                && e.newState() == PubSubState.Paused
                && e.cause() == Cause.PARENT);

    // enable it again: exactly ONE Operational-entry tick per component (final-hop rule),
    // METHOD on the connection, PARENT on the cascaded descendants
    service.enable(connection);
    awaitState(connection, PubSubState.Operational);
    awaitState(group, PubSubState.Operational);
    awaitState(writer, PubSubState.Operational);

    awaitCounters(
        "enable(connection) attribution",
        () ->
            component(CONNECTION).stateOperationalByMethod()
                    == connBefore.stateOperationalByMethod() + 1
                && component(GROUP).stateOperationalByParent()
                    == groupBefore.stateOperationalByParent() + 1
                && component(WRITER).stateOperationalByParent()
                    == writerBefore.stateOperationalByParent() + 1);

    // the initiating component never ticks ByParent, cascaded components never tick ByMethod
    assertEquals(
        connBefore.stateOperationalByParent(), component(CONNECTION).stateOperationalByParent());
    assertEquals(
        groupBefore.stateOperationalByMethod(), component(GROUP).stateOperationalByMethod());
    assertEquals(
        writerBefore.stateOperationalByMethod(), component(WRITER).stateOperationalByMethod());

    awaitEvent(
        e ->
            e.component().equals(connection)
                && e.newState() == PubSubState.Operational
                && e.cause() == Cause.METHOD);
    awaitEvent(
        e ->
            e.component().equals(group)
                && e.newState() == PubSubState.Operational
                && e.cause() == Cause.PARENT);
  }

  @Test
  void groupDisableEnableLeavesTheConnectionUntouched() throws Exception {
    startPublisher(new StubTransport());

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();

    ComponentDiagnostics connBefore = component(CONNECTION);
    ComponentDiagnostics groupBefore = component(GROUP);
    ComponentDiagnostics writerBefore = component(WRITER);

    service.disable(group);
    awaitState(group, PubSubState.Disabled);
    awaitState(writer, PubSubState.Paused);

    service.enable(group);
    awaitState(group, PubSubState.Operational);
    awaitState(writer, PubSubState.Operational);

    awaitCounters(
        "group bounce attribution",
        () ->
            component(GROUP).stateDisabledByMethod() == groupBefore.stateDisabledByMethod() + 1
                && component(GROUP).stateOperationalByMethod()
                    == groupBefore.stateOperationalByMethod() + 1
                && component(WRITER).statePausedByParent() == writerBefore.statePausedByParent() + 1
                && component(WRITER).stateOperationalByParent()
                    == writerBefore.stateOperationalByParent() + 1);

    // the writer is a cascade, not a method target
    assertEquals(writerBefore.stateDisabledByMethod(), component(WRITER).stateDisabledByMethod());
    assertEquals(
        writerBefore.stateOperationalByMethod(), component(WRITER).stateOperationalByMethod());

    // the PARENT of the method target is untouched on all six State* counters
    ComponentDiagnostics connAfter = component(CONNECTION);
    assertEquals(connBefore.stateError(), connAfter.stateError());
    assertEquals(connBefore.stateOperationalByMethod(), connAfter.stateOperationalByMethod());
    assertEquals(connBefore.stateOperationalByParent(), connAfter.stateOperationalByParent());
    assertEquals(connBefore.stateOperationalFromError(), connAfter.stateOperationalFromError());
    assertEquals(connBefore.statePausedByParent(), connAfter.statePausedByParent());
    assertEquals(connBefore.stateDisabledByMethod(), connAfter.stateDisabledByMethod());
  }

  @Test
  void transportOutageTicksErrorAndRecoveryCountersOnTheConnectionOnly() throws Exception {
    var transport = new StubTransport();
    startPublisher(transport);

    TransportStateListener listener = transport.listener.get();
    assertNotNull(listener, "the data-channel context must carry the engine listener");

    PubSubHandle connection = service.components().connection("conn").orElseThrow();
    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();

    ComponentDiagnostics connBefore = component(CONNECTION);
    ComponentDiagnostics groupBefore = component(GROUP);
    ComponentDiagnostics writerBefore = component(WRITER);

    // DOWN: the connection enters Error (StateError), descendants pause (StatePausedByParent)
    listener.onTransportDown(new StatusCode(StatusCodes.Bad_ServerNotConnected));
    awaitState(connection, PubSubState.Error);
    awaitState(group, PubSubState.Paused);
    awaitState(writer, PubSubState.Paused);

    awaitCounters(
        "outage attribution",
        () ->
            component(CONNECTION).stateError() == connBefore.stateError() + 1
                && component(GROUP).statePausedByParent() == groupBefore.statePausedByParent() + 1
                && component(WRITER).statePausedByParent()
                    == writerBefore.statePausedByParent() + 1);
    // the paused descendants did not enter Error themselves
    assertEquals(groupBefore.stateError(), component(GROUP).stateError());
    assertEquals(writerBefore.stateError(), component(WRITER).stateError());

    // UP: the connection recovers FromError; descendants recover as a PARENT cascade — they
    // came from Paused, so they never tick StateOperationalFromError
    listener.onTransportUp();
    awaitState(connection, PubSubState.Operational);
    awaitState(group, PubSubState.Operational);
    awaitState(writer, PubSubState.Operational);

    awaitCounters(
        "recovery attribution",
        () ->
            component(CONNECTION).stateOperationalFromError()
                    == connBefore.stateOperationalFromError() + 1
                && component(GROUP).stateOperationalByParent()
                    == groupBefore.stateOperationalByParent() + 1
                && component(WRITER).stateOperationalByParent()
                    == writerBefore.stateOperationalByParent() + 1);

    ComponentDiagnostics connAfter = component(CONNECTION);
    assertEquals(connBefore.stateOperationalByMethod(), connAfter.stateOperationalByMethod());
    assertEquals(connBefore.stateOperationalByParent(), connAfter.stateOperationalByParent());
    assertEquals(
        groupBefore.stateOperationalFromError(), component(GROUP).stateOperationalFromError());
    assertEquals(
        writerBefore.stateOperationalFromError(), component(WRITER).stateOperationalFromError());
  }

  // region fixtures (stub transport + helpers, per-class copies)

  /**
   * A transport that captures the engine's {@link TransportStateListener} from the data-channel
   * context (discovery contexts carry none) and completes every send successfully.
   */
  private static final class StubTransport implements TransportProvider {

    final AtomicReference<@Nullable TransportStateListener> listener = new AtomicReference<>();

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
          return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletableFuture<Void> closeAsync() {
          return CompletableFuture.completedFuture(null);
        }
      };
    }

    @Override
    public SubscriberChannel openSubscriber(SubscriberTransportContext context) {
      return () -> CompletableFuture.completedFuture(null);
    }
  }

  private void startPublisher(StubTransport transport) throws Exception {
    transportExecutor = Executors.newSingleThreadExecutor();

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
                    .writerGroup(
                        WriterGroupConfig.builder("WG")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(50))
                            .dataSetWriter(
                                DataSetWriterConfig.builder("W1")
                                    .dataSet(new PublishedDataSetRef("PDS"))
                                    .dataSetWriterId(ushort(1))
                                    .build())
                            .build())
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
    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
  }

  private ComponentDiagnostics component(String path) {
    assertNotNull(service);
    return service.diagnostics().component(path).orElseThrow();
  }

  private void awaitState(PubSubHandle handle, PubSubState expected) throws InterruptedException {
    awaitTrue(
        "state of " + handle + " to become " + expected, () -> service.state(handle) == expected);
  }

  private void awaitCounters(String description, BooleanSupplier condition)
      throws InterruptedException {
    awaitTrue(description, condition);
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

  private static void awaitTrue(String description, BooleanSupplier condition)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      if (condition.getAsBoolean()) {
        return;
      }
      Thread.sleep(10);
    }
    fail("timed out waiting for: " + description);
  }

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
