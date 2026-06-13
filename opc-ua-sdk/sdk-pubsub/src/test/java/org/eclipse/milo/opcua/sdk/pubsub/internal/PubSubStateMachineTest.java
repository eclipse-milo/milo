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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
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
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PubSubStateMachine} against the Part 14 §6.2.1 Table 2 oracle (including
 * the pinned parent-Error-behaves-like-Paused resolution), plus engine-level reader lifecycle and
 * keep-alive emission semantics exercised through {@link PubSubService} with a stub in-memory
 * transport. The data plane never touches the network; connections with writer groups open real UDP
 * discovery sockets, so those tests pin the discoveryAddress to a unique loopback multicast group
 * instead of the 224.0.2.14:4840 default.
 */
class PubSubStateMachineTest {

  private static final UUID FIELD_ID = new UUID(0L, 1L);

  // region direct state machine tests

  private record Transition(String path, PubSubState from, PubSubState to, StatusCode status) {}

  private final List<Transition> transitions = new ArrayList<>();

  private final PubSubStateMachine machine =
      new PubSubStateMachine(
          new Object(),
          (component, oldState, newState, statusCode) ->
              transitions.add(new Transition(component.path(), oldState, newState, statusCode)));

  private static final class TestComponent extends AbstractComponentRuntime {

    final List<TestComponent> childComponents = new ArrayList<>();

    boolean immediateStartup = true;
    @Nullable UaException activateFailure;
    boolean activateRuntimeFailure = false;
    int activateCount = 0;
    int deactivateCount = 0;

    TestComponent(String path, @Nullable TestComponent parent, boolean enabled) {
      super(ComponentType.CONNECTION, path, parent, enabled);
      if (parent != null) {
        parent.childComponents.add(this);
      }
    }

    @Override
    List<? extends AbstractComponentRuntime> children() {
      return childComponents;
    }

    @Override
    void activate() throws UaException {
      activateCount++;
      if (activateFailure != null) {
        throw activateFailure;
      }
      if (activateRuntimeFailure) {
        throw new IllegalStateException("activation failed");
      }
    }

    @Override
    boolean startupCompletesImmediately() {
      return immediateStartup;
    }

    @Override
    void deactivate() {
      deactivateCount++;
    }
  }

  private List<Transition> transitionsOf(String path) {
    return transitions.stream().filter(t -> t.path().equals(path)).toList();
  }

  @Test
  void enabledTreeBecomesOperationalWhenRootOperational() {
    var connection = new TestComponent("conn", null, true);
    var group = new TestComponent("conn/group", connection, true);
    var writer = new TestComponent("conn/group/writer", group, true);

    machine.setRootOperational(true, List.of(connection));

    assertEquals(PubSubState.Operational, connection.state());
    assertEquals(PubSubState.Operational, group.state());
    assertEquals(PubSubState.Operational, writer.state());

    // each component went Disabled -> PreOperational -> Operational
    for (String path : List.of("conn", "conn/group", "conn/group/writer")) {
      List<Transition> observed = transitionsOf(path);
      assertEquals(2, observed.size(), path);
      assertEquals(PubSubState.Disabled, observed.get(0).from());
      assertEquals(PubSubState.PreOperational, observed.get(0).to());
      assertEquals(PubSubState.PreOperational, observed.get(1).from());
      assertEquals(PubSubState.Operational, observed.get(1).to());
    }

    assertEquals(1, connection.activateCount);
    assertEquals(1, group.activateCount);
    assertEquals(1, writer.activateCount);
  }

  @Test
  void enabledComponentUnderNonOperationalRootIsPaused() {
    var connection = new TestComponent("conn", null, true);
    var group = new TestComponent("conn/group", connection, true);

    machine.initialize(connection);

    assertEquals(PubSubState.Paused, connection.state());
    assertEquals(PubSubState.Paused, group.state());
    assertEquals(0, connection.activateCount);
  }

  @Test
  void enabledChildOfDisabledParentIsPaused() {
    var connection = new TestComponent("conn", null, false);
    var group = new TestComponent("conn/group", connection, true);

    machine.setRootOperational(true, List.of(connection));

    assertEquals(PubSubState.Disabled, connection.state());
    assertEquals(PubSubState.Paused, group.state());
    assertTrue(transitionsOf("conn").isEmpty());

    List<Transition> observed = transitionsOf("conn/group");
    assertEquals(1, observed.size());
    assertEquals(PubSubState.Disabled, observed.get(0).from());
    assertEquals(PubSubState.Paused, observed.get(0).to());
  }

  @Test
  void enableWithOperationalParentChainGoesPreOperationalThenOperational() {
    var connection = new TestComponent("conn", null, true);
    var group = new TestComponent("conn/group", connection, false);
    machine.setRootOperational(true, List.of(connection));
    transitions.clear();

    machine.setEnabled(group, true);

    assertEquals(PubSubState.Operational, group.state());
    List<Transition> observed = transitionsOf("conn/group");
    assertEquals(2, observed.size());
    assertEquals(PubSubState.PreOperational, observed.get(0).to());
    assertEquals(PubSubState.Operational, observed.get(1).to());
  }

  @Test
  void enableWithNonOperationalParentGoesPaused() {
    var connection = new TestComponent("conn", null, false);
    var group = new TestComponent("conn/group", connection, false);
    machine.setRootOperational(true, List.of(connection));

    machine.setEnabled(group, true);

    assertEquals(PubSubState.Paused, group.state());
    assertEquals(0, group.activateCount);
  }

  @Test
  void enableAndDisableAreIdempotent() {
    var connection = new TestComponent("conn", null, true);
    machine.setRootOperational(true, List.of(connection));
    transitions.clear();

    machine.setEnabled(connection, true);
    assertTrue(transitions.isEmpty());
    assertEquals(PubSubState.Operational, connection.state());

    machine.setEnabled(connection, false);
    assertEquals(PubSubState.Disabled, connection.state());
    transitions.clear();

    machine.setEnabled(connection, false);
    assertTrue(transitions.isEmpty());
    assertEquals(PubSubState.Disabled, connection.state());
  }

  @Test
  void disableOperationalComponentPausesChildrenAndDeactivates() {
    var connection = new TestComponent("conn", null, true);
    var group = new TestComponent("conn/group", connection, true);
    machine.setRootOperational(true, List.of(connection));

    machine.setEnabled(connection, false);

    assertEquals(PubSubState.Disabled, connection.state());
    assertEquals(PubSubState.Paused, group.state());
    assertEquals(1, connection.deactivateCount);
    assertEquals(1, group.deactivateCount);
  }

  @Test
  void rootBecomingNonOperationalPausesTree() {
    var connection = new TestComponent("conn", null, true);
    var group = new TestComponent("conn/group", connection, true);
    machine.setRootOperational(true, List.of(connection));

    machine.setRootOperational(false, List.of(connection));

    assertEquals(PubSubState.Paused, connection.state());
    assertEquals(PubSubState.Paused, group.state());
  }

  @Test
  void parentErrorPushesChildrenToPausedLikePaused() {
    // pinned ambiguity: a parent entering Error is treated like a parent in Paused
    var connection = new TestComponent("conn", null, true);
    var group = new TestComponent("conn/group", connection, true);
    var writer = new TestComponent("conn/group/writer", group, true);
    machine.setRootOperational(true, List.of(connection));
    transitions.clear();

    var statusCode = new StatusCode(StatusCodes.Bad_CommunicationError);
    machine.fail(group, statusCode);

    assertEquals(PubSubState.Operational, connection.state());
    assertEquals(PubSubState.Error, group.state());
    assertEquals(PubSubState.Paused, writer.state());

    List<Transition> groupTransitions = transitionsOf("conn/group");
    assertEquals(1, groupTransitions.size());
    assertEquals(PubSubState.Error, groupTransitions.get(0).to());
    assertEquals(statusCode, groupTransitions.get(0).status());

    // Operational -> Error stays in the active states: no deactivation of the failed component
    assertEquals(0, group.deactivateCount);
    // ... but the Paused child was deactivated
    assertEquals(1, writer.deactivateCount);
  }

  @Test
  void recoverFromErrorReturnsToOperationalAndLiftsChildren() {
    var connection = new TestComponent("conn", null, true);
    var group = new TestComponent("conn/group", connection, true);
    var writer = new TestComponent("conn/group/writer", group, true);
    machine.setRootOperational(true, List.of(connection));
    machine.fail(group, new StatusCode(StatusCodes.Bad_CommunicationError));

    machine.recover(group);

    assertEquals(PubSubState.Operational, group.state());
    assertEquals(PubSubState.Operational, writer.state());
    // the child re-ran its startup steps after being lifted out of Paused
    assertEquals(2, writer.activateCount);
  }

  @Test
  void failIsIgnoredUnlessActive() {
    var disabled = new TestComponent("disabled", null, false);
    machine.initialize(disabled);

    var paused = new TestComponent("paused", null, true);
    machine.initialize(paused); // root not operational -> Paused
    transitions.clear();

    machine.fail(disabled, new StatusCode(StatusCodes.Bad_InternalError));
    machine.fail(paused, new StatusCode(StatusCodes.Bad_InternalError));

    assertTrue(transitions.isEmpty());
    assertEquals(PubSubState.Disabled, disabled.state());
    assertEquals(PubSubState.Paused, paused.state());
  }

  @Test
  void failFromErrorStateIsIgnored() {
    var connection = new TestComponent("conn", null, true);
    machine.setRootOperational(true, List.of(connection));
    machine.fail(connection, new StatusCode(StatusCodes.Bad_Timeout));
    transitions.clear();

    machine.fail(connection, new StatusCode(StatusCodes.Bad_InternalError));

    assertTrue(transitions.isEmpty());
    assertEquals(PubSubState.Error, connection.state());
  }

  @Test
  void recoverIsIgnoredUnlessError() {
    var connection = new TestComponent("conn", null, true);
    machine.setRootOperational(true, List.of(connection));
    transitions.clear();

    machine.recover(connection);

    assertTrue(transitions.isEmpty());
    assertEquals(PubSubState.Operational, connection.state());
  }

  @Test
  void disableFromErrorGoesDisabledAndDeactivates() {
    var connection = new TestComponent("conn", null, true);
    machine.setRootOperational(true, List.of(connection));
    machine.fail(connection, new StatusCode(StatusCodes.Bad_Timeout));

    machine.setEnabled(connection, false);

    assertEquals(PubSubState.Disabled, connection.state());
    assertEquals(1, connection.deactivateCount);
  }

  @Test
  void deferredStartupStaysPreOperationalUntilStartupCompleted() {
    var connection = new TestComponent("conn", null, true);
    var reader = new TestComponent("conn/reader", connection, true);
    reader.immediateStartup = false;

    machine.setRootOperational(true, List.of(connection));

    assertEquals(PubSubState.PreOperational, reader.state());
    assertEquals(1, reader.activateCount);

    machine.startupCompleted(reader);
    assertEquals(PubSubState.Operational, reader.state());

    transitions.clear();
    machine.startupCompleted(reader);
    assertTrue(transitions.isEmpty());
  }

  @Test
  void activationUaExceptionMovesComponentToErrorWithItsStatusCode() {
    var connection = new TestComponent("conn", null, true);
    var group = new TestComponent("conn/group", connection, true);
    connection.activateFailure = new UaException(StatusCodes.Bad_CommunicationError, "bind failed");

    machine.setRootOperational(true, List.of(connection));

    assertEquals(PubSubState.Error, connection.state());
    // children of the Error parent are Paused, per the pinned ambiguity
    assertEquals(PubSubState.Paused, group.state());

    List<Transition> observed = transitionsOf("conn");
    assertEquals(PubSubState.Error, observed.get(observed.size() - 1).to());
    assertEquals(
        new StatusCode(StatusCodes.Bad_CommunicationError),
        observed.get(observed.size() - 1).status());
  }

  @Test
  void activationRuntimeExceptionMovesComponentToErrorWithInternalError() {
    var connection = new TestComponent("conn", null, true);
    connection.activateRuntimeFailure = true;

    machine.setRootOperational(true, List.of(connection));

    assertEquals(PubSubState.Error, connection.state());
    List<Transition> observed = transitionsOf("conn");
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError), observed.get(observed.size() - 1).status());
  }

  @Test
  void disposeSubtreeDisablesChildrenFirstAndDeactivatesActiveComponents() {
    var connection = new TestComponent("conn", null, true);
    var group = new TestComponent("conn/group", connection, true);
    var writer = new TestComponent("conn/group/writer", group, true);
    machine.setRootOperational(true, List.of(connection));
    transitions.clear();

    machine.disposeSubtree(connection);

    assertEquals(PubSubState.Disabled, connection.state());
    assertEquals(PubSubState.Disabled, group.state());
    assertEquals(PubSubState.Disabled, writer.state());
    assertTrue(!connection.isEnabled() && !group.isEnabled() && !writer.isEnabled());

    assertEquals(1, connection.deactivateCount);
    assertEquals(1, group.deactivateCount);
    assertEquals(1, writer.deactivateCount);

    // children are disabled before their parents
    assertEquals(
        List.of("conn/group/writer", "conn/group", "conn"),
        transitions.stream().map(Transition::path).toList());
  }

  // endregion

  // region engine tests through PubSubService with a stub transport

  private @Nullable PubSubService service;
  private @Nullable ExecutorService transportExecutor;

  @AfterEach
  void shutdownService() throws Exception {
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

  /** A transport that never touches the network: captures sends, exposes datagram injection. */
  private static final class StubTransport implements TransportProvider {

    final BlockingQueue<byte[]> sent = new LinkedBlockingQueue<>();
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
          try {
            byte[] bytes = new byte[message.readableBytes()];
            message.readBytes(bytes);
            sent.add(bytes);
          } finally {
            message.release();
          }
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

  private static PublishedDataSetConfig publishedDataSet() {
    return PublishedDataSetConfig.builder("PDS")
        .field(
            FieldDefinition.builder("F1").dataType(NodeIds.Int32).dataSetFieldId(FIELD_ID).build())
        .build();
  }

  private static DataSetSnapshot snapshotOf(PublishedDataSetReadContext context) {
    return DataSetSnapshot.builder(context).field("F1", new DataValue(Variant.ofInt32(42))).build();
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
    service.startup().get(10, TimeUnit.SECONDS);

    return transport;
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

    return encode(group, draft);
  }

  private static byte[] encodeKeepAliveMessage(int dataSetWriterId) throws UaException {
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
            true,
            List.of());

    return encode(group, draft);
  }

  private static byte[] encode(WriterGroupConfig group, DataSetMessageDraft draft)
      throws UaException {

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
                    List.of(draft)));

    ByteBuf data = encoded.get(0).data();
    try {
      byte[] bytes = new byte[data.readableBytes()];
      data.readBytes(bytes);
      return bytes;
    } finally {
      data.release();
    }
  }

  private static DecodedNetworkMessage decode(byte[] frame) throws UaException {
    ByteBuf buffer = Unpooled.wrappedBuffer(frame);
    try {
      return new UadpMessageMapping()
          .decode(new DecodeContext(new DefaultEncodingContext()), buffer);
    } finally {
      buffer.release();
    }
  }

  @Test
  void readerGoesOperationalOnFirstKeyFrame() throws Exception {
    // Part 14 §6.2.1 Table 2: the first key frame (or event) completes reader startup; the
    // delta/keep-alive non-transitions are covered in ReaderOperationalGateTest
    StubTransport transport = startReaderService(DataSetReaderConfig.builder("R1").build());

    PubSubHandle reader = service.components().dataSetReader("conn", "RG", "R1").orElseThrow();
    assertEquals(PubSubState.PreOperational, service.state(reader));

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    service.addDataSetListener(new DataSetReaderRef("conn", "RG", "R1"), events::add);

    transport.inject(encodeDataMessage(10, 7));
    flushTransport();

    assertEquals(PubSubState.Operational, service.state(reader));

    DataSetReceivedEvent event = events.poll();
    assertNotNull(event);
    assertEquals(ushort(10), event.dataSetWriterId());
    assertEquals(1, event.fields().size());
    assertEquals("Field_0", event.fields().get(0).name());
    assertEquals(Variant.ofInt32(7), event.fields().get(0).value().getValue());
  }

  @Test
  void keepAliveDoesNotCompleteReaderStartup() throws Exception {
    StubTransport transport = startReaderService(DataSetReaderConfig.builder("R1").build());

    PubSubHandle reader = service.components().dataSetReader("conn", "RG", "R1").orElseThrow();
    assertEquals(PubSubState.PreOperational, service.state(reader));

    transport.inject(encodeKeepAliveMessage(10));
    flushTransport();

    // the keep-alive was delivered to the reader...
    assertEquals(1, service.diagnostics().snapshot().get("conn/RG/R1").dataSetMessagesReceived());
    // ... but does not complete startup
    assertEquals(PubSubState.PreOperational, service.state(reader));

    transport.inject(encodeDataMessage(10, 1));
    flushTransport();

    assertEquals(PubSubState.Operational, service.state(reader));
  }

  @Test
  void messageReceiveTimeoutMovesReaderToErrorAndNextMessageRecovers() throws Exception {
    StubTransport transport =
        startReaderService(
            DataSetReaderConfig.builder("R1")
                .messageReceiveTimeout(Duration.ofMillis(250))
                .build());

    PubSubHandle reader = service.components().dataSetReader("conn", "RG", "R1").orElseThrow();

    transport.inject(encodeDataMessage(10, 1));
    flushTransport();
    assertEquals(PubSubState.Operational, service.state(reader));

    // no more messages: the watchdog expires and moves the reader to Error
    awaitTrue(
        "reader to enter Error after messageReceiveTimeout",
        () -> service.state(reader) == PubSubState.Error);

    assertEquals(
        new StatusCode(StatusCodes.Bad_Timeout),
        service.diagnostics().snapshot().get("conn/RG/R1").lastError());

    // the next message recovers the reader
    transport.inject(encodeDataMessage(10, 2));
    flushTransport();
    awaitTrue(
        "reader to recover to Operational on next message",
        () -> service.state(reader) == PubSubState.Operational);
  }

  @Test
  void keepAliveEmittedPerNonOperationalWriterWithoutSequenceIncrement() throws Exception {
    var transport = new StubTransport();
    transportExecutor = Executors.newSingleThreadExecutor();

    WriterGroupConfig group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(25))
            .keepAliveTime(Duration.ofMillis(75))
            .dataSetWriter(
                DataSetWriterConfig.builder("W1")
                    .dataSet(new PublishedDataSetRef("PDS"))
                    .dataSetWriterId(ushort(1))
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("W2")
                    .dataSet(new PublishedDataSetRef("PDS"))
                    .dataSetWriterId(ushort(2))
                    .build())
            .build();

    UdpConnectionConfig connection =
        UdpConnectionConfig.builder("conn")
            .publisherId(PublisherId.uint16(ushort(99)))
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
            // a connection with writer groups opens real UDP discovery sockets: keep them on a
            // unique loopback multicast group, never the 224.0.2.14:4840 default
            .discoveryAddress(
                UdpDatagramAddress.multicast("239.255.74.1", 24741).networkInterface("127.0.0.1"))
            .writerGroup(group)
            .build();

    PubSubConfig config =
        PubSubConfig.builder().publishedDataSet(publishedDataSet()).connection(connection).build();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(new PublishedDataSetRef("PDS"), PubSubStateMachineTest::snapshotOf)
            .build();

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(transport)
            .transportExecutor(transportExecutor)
            .build();

    service = PubSubService.create(config, bindings, serviceConfig);
    service.startup().get(10, TimeUnit.SECONDS);

    // every cycle publishes one NetworkMessage containing both writers' key frames
    byte[] first = transport.sent.poll(10, TimeUnit.SECONDS);
    assertNotNull(first);
    DecodedNetworkMessage firstDecoded = decode(first);
    assertEquals(2, firstDecoded.messages().size());

    int lastW1DataSeq = -1;
    for (DecodedDataSetMessage message : firstDecoded.messages()) {
      assertEquals(DataSetMessageKind.KEY_FRAME, message.kind());
      if (ushort(1).equals(message.dataSetWriterId())) {
        lastW1DataSeq = message.sequenceNumber().intValue();
      }
    }
    assertTrue(lastW1DataSeq >= 0);

    PubSubHandle w1 = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    service.disable(w1);
    assertEquals(PubSubState.Disabled, service.state(w1));

    // scan subsequent frames: W2 keeps publishing key frames; W1 produces only keep-alives
    // after keepAliveTime, at the next expected sequence number, without incrementing it
    DecodedDataSetMessage firstKeepAlive = null;
    DecodedDataSetMessage secondKeepAlive = null;
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);

    while (secondKeepAlive == null && System.nanoTime() < deadline) {
      byte[] frame = transport.sent.poll(1, TimeUnit.SECONDS);
      if (frame == null) {
        continue;
      }
      for (DecodedDataSetMessage message : decode(frame).messages()) {
        if (ushort(2).equals(message.dataSetWriterId())) {
          assertEquals(DataSetMessageKind.KEY_FRAME, message.kind());
        } else {
          assertEquals(ushort(1), message.dataSetWriterId());
          if (message.kind() == DataSetMessageKind.KEY_FRAME) {
            // a cycle in flight while disabling; track its sequence number
            assertNull(firstKeepAlive, "data frame after keep-alive");
            lastW1DataSeq = message.sequenceNumber().intValue();
          } else {
            assertEquals(DataSetMessageKind.KEEP_ALIVE, message.kind());
            if (firstKeepAlive == null) {
              firstKeepAlive = message;
            } else {
              secondKeepAlive = message;
            }
          }
        }
      }
    }

    assertNotNull(firstKeepAlive, "no keep-alive observed");
    assertNotNull(secondKeepAlive, "only one keep-alive observed");

    // keep-alive carries the next expected sequence number and does not increment it
    assertEquals((lastW1DataSeq + 1) & 0xFFFF, firstKeepAlive.sequenceNumber().intValue());
    assertEquals(firstKeepAlive.sequenceNumber(), secondKeepAlive.sequenceNumber());
  }

  @Test
  void noKeepAliveWithoutKeepAliveTimeAndNoEmptyNetworkMessages() throws Exception {
    var transport = new StubTransport();
    transportExecutor = Executors.newSingleThreadExecutor();

    WriterGroupConfig group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(25))
            .dataSetWriter(
                DataSetWriterConfig.builder("W1")
                    .dataSet(new PublishedDataSetRef("PDS"))
                    .dataSetWriterId(ushort(1))
                    .build())
            .build();

    UdpConnectionConfig connection =
        UdpConnectionConfig.builder("conn")
            .publisherId(PublisherId.uint16(ushort(99)))
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
            // a connection with writer groups opens real UDP discovery sockets: keep them on a
            // unique loopback multicast group, never the 224.0.2.14:4840 default
            .discoveryAddress(
                UdpDatagramAddress.multicast("239.255.74.2", 24742).networkInterface("127.0.0.1"))
            .writerGroup(group)
            .build();

    PubSubConfig config =
        PubSubConfig.builder().publishedDataSet(publishedDataSet()).connection(connection).build();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(new PublishedDataSetRef("PDS"), PubSubStateMachineTest::snapshotOf)
            .build();

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(transport)
            .transportExecutor(transportExecutor)
            .build();

    service = PubSubService.create(config, bindings, serviceConfig);
    service.startup().get(10, TimeUnit.SECONDS);

    assertNotNull(transport.sent.poll(10, TimeUnit.SECONDS), "no data frame published");

    PubSubHandle w1 = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    service.disable(w1);

    // drain frames already in flight: once 150 ms pass without a frame the publisher is idle
    while (transport.sent.poll(150, TimeUnit.MILLISECONDS) != null) {
      // keep draining
    }

    // a group whose only writer is disabled and that has no keepAliveTime sends nothing
    assertNull(transport.sent.poll(300, TimeUnit.MILLISECONDS));

    PubSubHandle groupHandle = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(groupHandle));
  }

  // endregion
}
