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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Cause attribution of the State* counters and the {@link PubSubStateChangeEvent#cause()}
 * component, exercised through {@link PubSubService} with a stub in-memory transport: startup ticks
 * neither ByMethod nor ByParent on the initiating component (descendants tick ByParent), explicit
 * enable/disable ticks ByMethod — including the final hop of a deferred startup — fail/recover tick
 * StateError and StateOperationalFromError, and dispose ticks nothing.
 */
class StateCauseAttributionTest {

  private final List<PubSubStateChangeEvent> stateEvents = new CopyOnWriteArrayList<>();

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
  void startupTicksNeitherByMethodNorByParentOnTheInitiatingComponent() throws Exception {
    StubTransport transport = startReaderService(DataSetReaderConfig.builder("R1").build());

    // the deferred reader completes startup on its first data message; its PreOperational
    // entry cascaded from the connection, so the final hop replays PARENT
    transport.inject(encodeDataMessage(10, 1));
    flushTransport();

    ComponentDiagnostics connection = component("conn");
    assertEquals(0, connection.stateOperationalByMethod());
    assertEquals(0, connection.stateOperationalByParent());
    // construction computes initial states with the root non-operational: one Paused entry
    assertEquals(1, connection.statePausedByParent());

    ComponentDiagnostics group = component("conn/RG");
    assertEquals(0, group.stateOperationalByMethod());
    assertEquals(1, group.stateOperationalByParent());
    assertEquals(1, group.statePausedByParent());

    ComponentDiagnostics reader = component("conn/RG/R1");
    assertEquals(0, reader.stateOperationalByMethod());
    assertEquals(1, reader.stateOperationalByParent());
  }

  @Test
  void enableDisableTicksByMethodIncludingTheDeferredFinalHop() throws Exception {
    StubTransport transport = startReaderService(DataSetReaderConfig.builder("R1").build());
    transport.inject(encodeDataMessage(10, 1));
    flushTransport();

    PubSubHandle reader = service.components().dataSetReader("conn", "RG", "R1").orElseThrow();

    service.disable(reader);
    assertEquals(PubSubState.Disabled, service.state(reader));
    assertEquals(1, component("conn/RG/R1").stateDisabledByMethod());

    // re-enable: the reader defers startup again; the final hop on the next message must
    // replay METHOD (the remembered trigger), not the PARENT of the original cascade
    service.enable(reader);
    assertEquals(PubSubState.PreOperational, service.state(reader));
    transport.inject(encodeDataMessage(10, 2));
    flushTransport();

    assertEquals(PubSubState.Operational, service.state(reader));
    assertEquals(1, component("conn/RG/R1").stateOperationalByMethod());

    flushTransport();
    assertCause("conn/RG/R1", PubSubState.Disabled, PubSubStateChangeEvent.Cause.METHOD);
    assertCause("conn/RG/R1", PubSubState.Operational, PubSubStateChangeEvent.Cause.METHOD);
  }

  @Test
  void failAndRecoverTickStateErrorAndOperationalFromError() throws Exception {
    StubTransport transport =
        startReaderService(
            DataSetReaderConfig.builder("R1")
                .messageReceiveTimeout(Duration.ofMillis(250))
                .build());

    PubSubHandle reader = service.components().dataSetReader("conn", "RG", "R1").orElseThrow();

    transport.inject(encodeDataMessage(10, 1));
    flushTransport();
    assertEquals(PubSubState.Operational, service.state(reader));

    // the watchdog expires: Error entry, cause ERROR
    awaitTrue("reader in Error", () -> service.state(reader) == PubSubState.Error);
    assertEquals(1, component("conn/RG/R1").stateError());

    // the next message recovers: Operational entry from Error, cause ERROR_RECOVERY — it
    // ticks StateOperationalFromError but neither ByMethod nor ByParent
    transport.inject(encodeDataMessage(10, 2));
    flushTransport();
    awaitTrue("reader recovered", () -> service.state(reader) == PubSubState.Operational);

    ComponentDiagnostics reader1 = component("conn/RG/R1");
    assertEquals(1, reader1.stateOperationalFromError());
    assertEquals(0, reader1.stateOperationalByMethod());

    flushTransport();
    assertCause("conn/RG/R1", PubSubState.Error, PubSubStateChangeEvent.Cause.ERROR);
    assertTrue(
        stateEvents.stream()
            .anyMatch(
                event ->
                    event.component().path().equals("conn/RG/R1")
                        && event.newState() == PubSubState.Operational
                        && event.oldState() == PubSubState.Error
                        && event.cause() == PubSubStateChangeEvent.Cause.ERROR_RECOVERY));
  }

  @Test
  void disposeTicksNothingAndCarriesDisposeCause() throws Exception {
    StubTransport transport = startReaderService(DataSetReaderConfig.builder("R1").build());
    transport.inject(encodeDataMessage(10, 1));
    flushTransport();
    stateEvents.clear();

    // reconfigure-removal of the reader group: dispose transitions, never StateDisabledByMethod
    service.reconfigure(
        PubSubConfig.builder()
            .connection(
                UdpConnectionConfig.builder("conn")
                    .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
                    .build())
            .build(),
        PubSubService.ReconfigureMode.DISABLE_AFFECTED);
    flushTransport();

    assertTrue(
        stateEvents.stream()
            .anyMatch(
                event ->
                    event.component().path().equals("conn/RG/R1")
                        && event.newState() == PubSubState.Disabled
                        && event.cause() == PubSubStateChangeEvent.Cause.DISPOSE),
        "removal transitions carry DISPOSE: " + stateEvents);
    assertTrue(
        stateEvents.stream()
            .noneMatch(event -> event.cause() == PubSubStateChangeEvent.Cause.METHOD));

    // shutdown teardown: dispose transitions on the surviving connection tick nothing
    stateEvents.clear();
    service.shutdown().get(10, TimeUnit.SECONDS);

    ComponentDiagnostics connection = component("conn");
    assertEquals(0, connection.stateDisabledByMethod());
    assertTrue(
        stateEvents.stream()
            .filter(event -> event.newState() == PubSubState.Disabled)
            .allMatch(event -> event.cause() == PubSubStateChangeEvent.Cause.DISPOSE),
        "shutdown transitions carry DISPOSE: " + stateEvents);
  }

  // region fixtures (stub transport + encode helpers, per-class copies)

  /** A transport that never touches the network: swallows sends, exposes datagram injection. */
  private static final class StubTransport implements TransportProvider {

    final AtomicReference<Consumer<ByteBuf>> consumer = new AtomicReference<>();

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
      consumer.set(context.messageConsumer());
      return () -> CompletableFuture.completedFuture(null);
    }

    void inject(byte[] datagram) {
      Consumer<ByteBuf> messageConsumer = consumer.get();
      assertNotNull(messageConsumer, "subscriber channel was never opened");
      ByteBuf buffer = Unpooled.wrappedBuffer(datagram);
      try {
        messageConsumer.accept(buffer);
      } finally {
        buffer.release();
      }
    }
  }

  private StubTransport startReaderService(DataSetReaderConfig reader) throws Exception {
    var transport = new StubTransport();
    transportExecutor = Executors.newSingleThreadExecutor();

    UdpConnectionConfig connection =
        UdpConnectionConfig.builder("conn")
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
            .readerGroup(ReaderGroupConfig.builder("RG").dataSetReader(reader).build())
            .build();

    PubSubConfig config = PubSubConfig.builder().connection(connection).build();

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(transport)
            .transportExecutor(transportExecutor)
            .build();

    service = PubSubService.create(config, PubSubBindings.builder().build(), serviceConfig);
    service.addStateListener(stateEvents::add);
    service.startup().get(10, TimeUnit.SECONDS);

    return transport;
  }

  private ComponentDiagnostics component(String path) {
    assertNotNull(service);
    return service.diagnostics().component(path).orElseThrow();
  }

  private void assertCause(String path, PubSubState newState, PubSubStateChangeEvent.Cause cause) {
    assertTrue(
        stateEvents.stream()
            .anyMatch(
                event ->
                    event.component().path().equals(path)
                        && event.newState() == newState
                        && event.cause() == cause),
        "expected a %s transition of '%s' with cause %s in: %s"
            .formatted(newState, path, cause, stateEvents));
  }

  private void flushTransport() throws Exception {
    assertNotNull(transportExecutor);
    transportExecutor.submit(() -> {}).get(10, TimeUnit.SECONDS);
  }

  private static void awaitTrue(String description, BooleanSupplier condition)
      throws InterruptedException {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while (System.nanoTime() < deadline) {
      if (condition.getAsBoolean()) {
        return;
      }
      Thread.sleep(10);
    }
    fail("timed out waiting for: " + description);
  }

  /** Encode one NetworkMessage with the default UADP-Dynamic masks (PublisherId+PayloadHeader). */
  private static byte[] encodeDataMessage(int dataSetWriterId, int value) throws UaException {
    WriterGroupConfig group =
        WriterGroupConfig.builder("EncodeWG").writerGroupId(ushort(1)).build();

    DataSetWriterConfig writer =
        DataSetWriterConfig.builder("EncodeW")
            .dataSet(new PublishedDataSetRef("EncodePDS"))
            .dataSetWriterId(ushort(dataSetWriterId))
            .settings(
                UadpDataSetWriterSettings.builder()
                    .dataSetMessageContentMask(UadpDataSetMessageContentMask.of())
                    .build())
            .build();

    var draft =
        DataSetMessageDraft.of(
            writer,
            uint(0),
            null,
            null,
            new ConfigurationVersionDataType(uint(0), uint(0)),
            false,
            List.of(new DataValue(Variant.ofInt32(value))));

    List<EncodedNetworkMessage> encoded =
        new UadpMessageMapping()
            .encode(
                new EncodeContext(
                    new DefaultEncodingContext(),
                    PublisherId.uint16(ushort(1)),
                    group,
                    uint(0),
                    ushort(1),
                    ushort(0),
                    null,
                    List.of(draft),
                    null));

    ByteBuf data = encoded.get(0).data();
    try {
      byte[] bytes = new byte[data.readableBytes()];
      data.readBytes(bytes);
      return bytes;
    } finally {
      data.release();
    }
  }

  // endregion
}
