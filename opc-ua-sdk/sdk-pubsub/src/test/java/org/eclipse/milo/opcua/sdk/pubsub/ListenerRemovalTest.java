/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
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
 * The {@code remove*Listener} surface of {@link PubSubService}: removed listeners receive nothing
 * further, listeners still registered are unaffected, and removing a never-added listener is a
 * no-op — for all five listener kinds. Runs against a stub in-memory transport with injected
 * datagrams; no network.
 */
class ListenerRemovalTest {

  private static final DataSetReaderRef READER_REF = new DataSetReaderRef("conn", "RG", "R1");

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
  void removedGlobalDataSetListenerReceivesNothing() throws Exception {
    StubTransport transport = startReaderService();

    var removed = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var kept = new LinkedBlockingQueue<DataSetReceivedEvent>();
    DataSetListener removedListener = removed::add;
    service.addDataSetListener(removedListener);
    service.addDataSetListener(kept::add);

    service.removeDataSetListener(removedListener);

    transport.inject(encodeDataMessage(10, 1));
    flushTransport();

    assertNotNull(kept.poll(), "the still-registered listener receives the DataSet");
    assertTrue(removed.isEmpty(), "the removed listener receives nothing");
  }

  @Test
  void removedPerReaderDataSetListenerReceivesNothingAndReAddWorks() throws Exception {
    StubTransport transport = startReaderService();

    var removed = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var kept = new LinkedBlockingQueue<DataSetReceivedEvent>();
    DataSetListener removedListener = removed::add;
    DataSetListener keptListener = kept::add;
    service.addDataSetListener(READER_REF, removedListener);
    service.addDataSetListener(READER_REF, keptListener);

    service.removeDataSetListener(READER_REF, removedListener);
    // removing a never-added listener (or one for a foreign ref) is a no-op
    service.removeDataSetListener(READER_REF, event -> {});
    service.removeDataSetListener(new DataSetReaderRef("conn", "RG", "R2"), removedListener);

    transport.inject(encodeDataMessage(10, 1));
    flushTransport();

    assertNotNull(kept.poll(), "the still-registered per-reader listener receives the DataSet");
    assertTrue(removed.isEmpty(), "the removed per-reader listener receives nothing");

    // removing the last listener empties (and drops) the per-ref registration; a fresh add
    // re-creates it
    service.removeDataSetListener(READER_REF, keptListener);
    var reAdded = new LinkedBlockingQueue<DataSetReceivedEvent>();
    service.addDataSetListener(READER_REF, reAdded::add);

    transport.inject(encodeDataMessage(10, 2));
    flushTransport();

    assertNotNull(reAdded.poll(), "a listener added after the entry emptied receives events");
    assertTrue(kept.isEmpty(), "the emptied entry's former listener receives nothing");
  }

  @Test
  void removedStateListenerReceivesNothing() throws Exception {
    StubTransport transport = startReaderService();

    var removed = new LinkedBlockingQueue<PubSubStateChangeEvent>();
    var kept = new LinkedBlockingQueue<PubSubStateChangeEvent>();
    PubSubStateListener removedListener = removed::add;
    service.addStateListener(removedListener);
    service.addStateListener(kept::add);

    service.removeStateListener(removedListener);
    // removing a never-added listener is a no-op
    service.removeStateListener(event -> {});

    // the first data message completes reader startup: a state change fires
    transport.inject(encodeDataMessage(10, 1));
    flushTransport();

    PubSubStateChangeEvent event = kept.poll(10, TimeUnit.SECONDS);
    assertNotNull(event, "the still-registered listener receives the state change");
    assertEquals(PubSubState.Operational, event.newState());
    assertTrue(removed.isEmpty(), "the removed listener receives nothing");
  }

  @Test
  void removedDiagnosticsListenerReceivesNothing() throws Exception {
    StubTransport transport = startReaderService();

    var removed = new LinkedBlockingQueue<PubSubDiagnosticsEvent>();
    var kept = new LinkedBlockingQueue<PubSubDiagnosticsEvent>();
    PubSubDiagnosticsListener removedListener = removed::add;
    service.addDiagnosticsListener(removedListener);
    service.addDiagnosticsListener(kept::add);

    service.removeDiagnosticsListener(removedListener);
    // removing a never-added listener is a no-op
    service.removeDiagnosticsListener(event -> {});

    // a UADP message that fails mid-decode raises a decode-error diagnostics event at the
    // connection (input failing the version nibble would be tolerantly skipped without one)
    transport.inject(
        new byte[] {
          0x01, // byte 0: version 1
          0x01, // DataSetFlags1: valid | Variant encoding
          0x02,
          0x00, // FieldCount = 2, but only one field follows
          0x06,
          0x2A,
          0x00,
          0x00,
          0x00 // Variant Int32 = 42
        });
    flushTransport();

    assertNotNull(
        kept.poll(10, TimeUnit.SECONDS),
        "the still-registered listener receives the diagnostics event");
    assertTrue(removed.isEmpty(), "the removed listener receives nothing");
  }

  @Test
  void metaDataListenerAddAndRemoveAreIdempotentAndIsolated() throws Exception {
    StubTransport transport = startReaderService();

    MetaDataListener listener = event -> {};
    service.addMetaDataListener(listener);
    service.removeMetaDataListener(listener);
    // removing again (now never-added) is a no-op
    service.removeMetaDataListener(listener);

    // the other listener kinds are unaffected by metadata listener churn
    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    service.addDataSetListener(events::add);
    transport.inject(encodeDataMessage(10, 1));
    flushTransport();
    assertFalse(events.isEmpty());
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

  private StubTransport startReaderService() throws Exception {
    var transport = new StubTransport();
    transportExecutor = Executors.newSingleThreadExecutor();

    UdpConnectionConfig connection =
        UdpConnectionConfig.builder("conn")
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
            .readerGroup(
                ReaderGroupConfig.builder("RG")
                    .dataSetReader(DataSetReaderConfig.builder("R1").build())
                    .build())
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

  private void flushTransport() throws Exception {
    assertNotNull(transportExecutor);
    transportExecutor.submit(() -> {}).get(10, TimeUnit.SECONDS);
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
                    org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId.uint16(ushort(1)),
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
