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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Dispatch-level tests for chunk reassembly (Part 14 §7.2.4.4.4) through {@link ReaderDispatcher}:
 * which chunk NetworkMessages may enter the connection's {@link ChunkReassembler}, which readers
 * receive the reassembled payload, and what a reader sees when chunks received with different
 * security modes, repeated chunk sets, or chunks no reader accepts are mixed into a stream.
 *
 * <p>Crafted chunk NetworkMessages are injected through a stub in-memory transport into a {@link
 * PubSubService} with one UDP connection "conn", one reader group "RG" (mode None, or Sign with a
 * static key set), and reader "R1" (plus "R2" where a test needs two) for WriterGroupId 258 /
 * DataSetWriterId 5. Chunk construction follows Part 14 Tables 158/159 and the signed envelope of
 * {@code SecuredChunkTamperTest}, here with SecurityFlags "signed" only so the Table 159 fields
 * stay in plaintext.
 */
class ChunkReassemblyDispatchTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final PubSubSecurityPolicy POLICY = PubSubSecurityPolicy.PubSubAes256Ctr;
  private static final String SECURITY_GROUP = "sg-chunks";
  private static final SecurityGroupRef SECURITY_GROUP_REF = new SecurityGroupRef(SECURITY_GROUP);
  private static final int TOKEN_ID = 7;

  private static final int WRITER_ID = 5;

  private @Nullable PubSubService service;
  private @Nullable ExecutorService transportExecutor;
  private @Nullable StubTransport transport;

  private final Map<String, BlockingQueue<DataSetReceivedEvent>> eventsByReader =
      new ConcurrentHashMap<>();

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

  /**
   * A chunk received with mode None cannot contribute bytes to a payload delivered as Sign: the
   * reader group is configured for Sign, so the unsecured chunk is dropped at the receive-mode gate
   * and never enters reassembly, even though it pre-fills the tail range of the payload the signed
   * chunks then cover in order. The payload completes only once the signed tail arrives.
   */
  @Test
  void unsecuredChunkBytesAreNotDeliveredInASignedPayload() throws Exception {
    startService(MessageSecurityMode.Sign, "R1");

    byte[] payload = dataSetMessage(42);

    // the unsecured tail [5, 8) carries other bytes than the signed tail
    injectAndFlush(unsecuredChunk(WRITER_ID, null, 7, 5, payload.length, bytes(0x00, 0x00, 0x01)));
    assertEquals(1, diagnostics("conn/RG").staleKeyMessages(), "dropped at the receive-mode gate");

    injectAndFlush(signedChunk(1, 7, 0, payload.length, Arrays.copyOfRange(payload, 0, 3)));
    injectAndFlush(signedChunk(2, 7, 3, payload.length, Arrays.copyOfRange(payload, 3, 5)));
    assertEquals(0, eventCount("R1"), "the unsecured tail must not complete the signed payload");

    injectAndFlush(signedChunk(3, 7, 5, payload.length, Arrays.copyOfRange(payload, 5, 8)));
    assertEquals(1, eventCount("R1"));
    assertEquals(Variant.ofInt32(42), takeValue("R1"));
  }

  /**
   * Unsecured chunks with the same MessageSequenceNumber and another TotalSize, or with a newer
   * MessageSequenceNumber, do not restart or discard the in-progress signed payload of a Sign
   * reader group: they are dropped at the receive-mode gate and the signed payload still completes.
   */
  @Test
  void unsecuredChunksDoNotResetOrBlockASignedPayload() throws Exception {
    startService(MessageSecurityMode.Sign, "R1");

    byte[] payload = dataSetMessage(42);

    injectAndFlush(signedChunk(1, 7, 0, payload.length, Arrays.copyOfRange(payload, 0, 3)));

    injectAndFlush(unsecuredChunk(WRITER_ID, null, 7, 0, 20, bytes(9, 9)));
    injectAndFlush(unsecuredChunk(WRITER_ID, null, 8, 0, payload.length, bytes(1, 2, 3)));
    assertEquals(2, diagnostics("conn/RG").staleKeyMessages(), "dropped at the receive-mode gate");

    injectAndFlush(signedChunk(2, 7, 3, payload.length, Arrays.copyOfRange(payload, 3, 5)));
    injectAndFlush(signedChunk(3, 7, 5, payload.length, Arrays.copyOfRange(payload, 5, 8)));

    assertEquals(1, eventCount("R1"), "the signed payload must still complete");
    assertEquals(Variant.ofInt32(42), takeValue("R1"));
  }

  /**
   * A repeated copy of a complete chunk set is delivered once. The chunk NetworkMessages carry no
   * NetworkMessage SequenceNumber and the reassembled DataSetMessage carries no DataSetMessage
   * SequenceNumber, so neither §7.2.3 reader window can reject the copy: only the chunk
   * MessageSequenceNumber (always present, Table 159) identifies it as already delivered.
   */
  @Test
  void repeatedChunkSetWithoutSequenceNumbersIsDeliveredOnce() throws Exception {
    startService(MessageSecurityMode.None, "R1");

    byte[] payload = dataSetMessage(42);
    byte[] first =
        unsecuredChunk(WRITER_ID, null, 7, 0, payload.length, Arrays.copyOfRange(payload, 0, 4));
    byte[] second =
        unsecuredChunk(WRITER_ID, null, 7, 4, payload.length, Arrays.copyOfRange(payload, 4, 8));

    injectAndFlush(first);
    injectAndFlush(second);
    assertEquals(1, eventCount("R1"));
    assertEquals(Variant.ofInt32(42), takeValue("R1"));

    injectAndFlush(first);
    injectAndFlush(second);
    assertEquals(0, eventCount("R1"), "the repeated chunk set must not be delivered again");

    // the next payload is still delivered
    byte[] next = dataSetMessage(43);
    injectAndFlush(
        unsecuredChunk(WRITER_ID, null, 8, 0, next.length, Arrays.copyOfRange(next, 0, 4)));
    injectAndFlush(
        unsecuredChunk(WRITER_ID, null, 8, 4, next.length, Arrays.copyOfRange(next, 4, 8)));
    assertEquals(1, eventCount("R1"));
    assertEquals(Variant.ofInt32(43), takeValue("R1"));
  }

  /**
   * Chunks that no reader on the connection accepts do not create reassembly state: a run of chunks
   * for DataSetWriterIds no reader matches, long enough to exceed the stream cap, does not evict
   * the in-progress payload of the reader's own stream.
   */
  @Test
  void chunksNoReaderAcceptsDoNotEvictAnInProgressPayload() throws Exception {
    startService(MessageSecurityMode.None, "R1");

    byte[] payload = dataSetMessage(42);

    injectAndFlush(
        unsecuredChunk(WRITER_ID, null, 7, 0, payload.length, Arrays.copyOfRange(payload, 0, 4)));

    for (int i = 0; i < ChunkReassembler.MAX_STREAMS; i++) {
      injectAndFlush(unsecuredChunk(1000 + i, null, 7, 0, 6, bytes(1, 2, 3, 4)));
    }

    injectAndFlush(
        unsecuredChunk(WRITER_ID, null, 7, 4, payload.length, Arrays.copyOfRange(payload, 4, 8)));
    assertEquals(1, eventCount("R1"), "the reader's own payload must still complete");
    assertEquals(Variant.ofInt32(42), takeValue("R1"));
  }

  /**
   * A chunk NetworkMessage whose NetworkMessage SequenceNumber the reader's §7.2.3 window rejects
   * as stale does not enter reassembly: a stale chunk cannot complete the payload, while a later
   * chunk with a NEW NetworkMessage SequenceNumber can.
   */
  @Test
  void chunkRejectedByTheNetworkMessageWindowDoesNotEnterReassembly() throws Exception {
    startService(MessageSecurityMode.None, "R1");

    byte[] payload = dataSetMessage(42);

    injectAndFlush(
        unsecuredChunk(WRITER_ID, 10, 7, 0, payload.length, Arrays.copyOfRange(payload, 0, 4)));
    injectAndFlush(
        unsecuredChunk(WRITER_ID, 5, 7, 4, payload.length, Arrays.copyOfRange(payload, 4, 8)));
    assertEquals(0, eventCount("R1"), "a stale chunk NetworkMessage must not complete the payload");

    injectAndFlush(
        unsecuredChunk(WRITER_ID, 11, 7, 4, payload.length, Arrays.copyOfRange(payload, 4, 8)));
    assertEquals(1, eventCount("R1"));
    assertEquals(Variant.ofInt32(42), takeValue("R1"));
  }

  /**
   * The reassembled payload reaches only the readers that accepted every contributing chunk. Two
   * readers match the same stream; R2 is disabled while chunks with NetworkMessage SequenceNumbers
   * 10 and 11 are delivered, so only R1's §7.2.3 window records them. After R2 is enabled, chunks
   * for a new payload reuse SequenceNumbers 10 and 11: R1's window rejects them as stale while R2's
   * (unseeded) window accepts them, and the payload is delivered to R2 only. A later payload with
   * fresh SequenceNumbers reaches both.
   */
  @Test
  void reassembledPayloadReachesOnlyReadersThatAcceptedEveryChunk() throws Exception {
    startService(MessageSecurityMode.None, "R1", "R2");

    PubSubHandle r2 = service.components().dataSetReader("conn", "RG", "R2").orElseThrow();
    service.disable(r2);
    awaitReaderState("R2", PubSubState.Disabled);

    byte[] first = dataSetMessage(42);
    injectAndFlush(
        unsecuredChunk(WRITER_ID, 10, 7, 0, first.length, Arrays.copyOfRange(first, 0, 4)));
    injectAndFlush(
        unsecuredChunk(WRITER_ID, 11, 7, 4, first.length, Arrays.copyOfRange(first, 4, 8)));
    assertEquals(Variant.ofInt32(42), takeValue("R1"));
    assertEquals(0, eventCount("R2"), "R2 is disabled");

    service.enable(r2);
    awaitReaderState("R2", PubSubState.PreOperational, PubSubState.Operational);

    // the same NetworkMessage SequenceNumbers again, carrying a newer payload
    byte[] second = dataSetMessage(43);
    injectAndFlush(
        unsecuredChunk(WRITER_ID, 10, 8, 0, second.length, Arrays.copyOfRange(second, 0, 4)));
    injectAndFlush(
        unsecuredChunk(WRITER_ID, 11, 8, 4, second.length, Arrays.copyOfRange(second, 4, 8)));
    assertEquals(0, eventCount("R1"), "R1's window rejected both chunks");
    assertEquals(Variant.ofInt32(43), takeValue("R2"));

    byte[] third = dataSetMessage(44);
    injectAndFlush(
        unsecuredChunk(WRITER_ID, 12, 9, 0, third.length, Arrays.copyOfRange(third, 0, 4)));
    injectAndFlush(
        unsecuredChunk(WRITER_ID, 13, 9, 4, third.length, Arrays.copyOfRange(third, 4, 8)));
    assertEquals(Variant.ofInt32(44), takeValue("R1"));
    assertEquals(Variant.ofInt32(44), takeValue("R2"));
  }

  /**
   * A retransmitted range counts for the readers that accepted the retransmission: R2 misses the
   * first chunk (disabled), then accepts a retransmission of it that R1's window rejects, and both
   * accept the last chunk. R2 accepted every chunk whose bytes are in the payload, so the payload
   * is delivered to R2 and not to R1.
   */
  @Test
  void retransmittedRangeIsDeliveredToReadersThatAcceptedTheRetransmission() throws Exception {
    startService(MessageSecurityMode.None, "R1", "R2");

    PubSubHandle r2 = service.components().dataSetReader("conn", "RG", "R2").orElseThrow();
    service.disable(r2);
    awaitReaderState("R2", PubSubState.Disabled);

    byte[] payload = dataSetMessage(42);
    byte[] head =
        unsecuredChunk(WRITER_ID, 10, 7, 0, payload.length, Arrays.copyOfRange(payload, 0, 4));
    injectAndFlush(head);

    service.enable(r2);
    awaitReaderState("R2", PubSubState.PreOperational, PubSubState.Operational);

    // the retransmission reuses NetworkMessage SequenceNumber 10: stale for R1, NEW for R2
    injectAndFlush(head);
    injectAndFlush(
        unsecuredChunk(WRITER_ID, 11, 7, 4, payload.length, Arrays.copyOfRange(payload, 4, 8)));

    assertEquals(0, eventCount("R1"), "R1's window rejected the chunk that wrote [0, 4)");
    assertEquals(Variant.ofInt32(42), takeValue("R2"));
  }

  /**
   * A chunk with TotalSize 0 completes an empty payload for the readers that accepted it, so the
   * empty DataSetMessage still fails decode and ticks decodeErrors at the connection, as any other
   * undecodable reassembled payload does.
   */
  @Test
  void zeroSizeChunkCompletesAnEmptyPayloadThatFailsDecode() throws Exception {
    startService(MessageSecurityMode.None, "R1");

    injectAndFlush(unsecuredChunk(WRITER_ID, null, 7, 0, 0, bytes()));

    assertEquals(0, eventCount("R1"));
    assertEquals(1, diagnostics("conn").decodeErrors());
  }

  // region fixture

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

  /**
   * Start a service with reader group "RG" in the given mode (Sign binds a static key set for
   * {@link #SECURITY_GROUP_REF}) and the named readers, each for WriterGroupId 258 /
   * DataSetWriterId 5.
   */
  private void startService(MessageSecurityMode mode, String... readerNames) throws Exception {
    transport = new StubTransport();
    transportExecutor = Executors.newSingleThreadExecutor();

    ReaderGroupConfig.Builder group = ReaderGroupConfig.builder("RG");
    for (String name : readerNames) {
      group.dataSetReader(
          DataSetReaderConfig.builder(name)
              .writerGroupId(ushort(258))
              .dataSetWriterId(ushort(WRITER_ID))
              .build());
    }
    boolean secured = mode != MessageSecurityMode.None;
    if (secured) {
      group.messageSecurity(
          MessageSecurityConfig.builder().mode(mode).securityGroup(SECURITY_GROUP_REF).build());
    }

    UdpConnectionConfig connection =
        UdpConnectionConfig.builder("conn")
            .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
            .readerGroup(group.build())
            .build();

    PubSubConfig.Builder config = PubSubConfig.builder().connection(connection);
    if (secured) {
      config.securityGroup(
          SecurityGroupConfig.builder(SECURITY_GROUP).securityPolicyUri(POLICY.getUri()).build());
    }

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(transport)
            .transportExecutor(transportExecutor)
            .build();

    PubSubBindings.Builder bindings = PubSubBindings.builder();
    for (String name : readerNames) {
      var queue = new LinkedBlockingQueue<DataSetReceivedEvent>();
      eventsByReader.put(name, queue);
      bindings.listener(new DataSetReaderRef("conn", "RG", name), queue::add);
    }
    if (secured) {
      bindings.securityKeys(
          SECURITY_GROUP_REF,
          (securityGroupId, startingTokenId, requestedKeyCount) ->
              CompletableFuture.completedFuture(
                  new SecurityKeySet(
                      POLICY.getUri(),
                      uint(TOKEN_ID),
                      List.of(ByteString.of(keyData())),
                      Duration.ofHours(1),
                      Duration.ofHours(2))));
    }

    service = PubSubService.create(config.build(), bindings.build(), serviceConfig);
    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    if (secured) {
      PubSubHandle handle = service.components().readerGroup("conn", "RG").orElseThrow();
      awaitTrue(
          () -> service.state(handle) == PubSubState.Operational,
          "secured reader group Operational (first key fetch complete)");
    }
  }

  private void injectAndFlush(byte[] frame) throws Exception {
    assertNotNull(transport);
    assertNotNull(transportExecutor);
    transport.inject(frame);
    transportExecutor.submit(() -> {}).get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
  }

  private int eventCount(String readerName) {
    return eventsByReader.get(readerName).size();
  }

  private DataSetReceivedEvent takeEvent(String readerName) {
    DataSetReceivedEvent event = eventsByReader.get(readerName).poll();
    assertNotNull(event, "no event for reader " + readerName);
    return event;
  }

  /** The Int32 value of the one field of the next event for {@code readerName}. */
  private Variant takeValue(String readerName) {
    return takeEvent(readerName).fields().get(0).value().getValue();
  }

  private void awaitReaderState(String readerName, PubSubState... states) throws Exception {
    assertNotNull(service);
    PubSubHandle handle =
        service.components().dataSetReader("conn", "RG", readerName).orElseThrow();
    awaitTrue(
        () -> List.of(states).contains(service.state(handle)),
        "reader " + readerName + " in " + List.of(states));
  }

  private PubSubDiagnostics.ComponentDiagnostics diagnostics(String path) {
    assertNotNull(service);
    PubSubDiagnostics.ComponentDiagnostics diagnostics = service.diagnostics().snapshot().get(path);
    assertNotNull(diagnostics, "no diagnostics for " + path);
    return diagnostics;
  }

  private static void awaitTrue(java.util.function.BooleanSupplier condition, String description)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (!condition.getAsBoolean()) {
      if (System.nanoTime() >= deadline) {
        fail("timed out waiting for: " + description);
      }
      Thread.sleep(25);
    }
  }

  // endregion

  // region chunk construction (Part 14 Tables 154/158/159)

  /** One valid key-frame DataSetMessage without a SequenceNumber: 1 field, Variant Int32. */
  private static byte[] dataSetMessage(int value) {
    return bytes(0x01, 0x01, 0x00, 0x06, value & 0xFF, 0x00, 0x00, 0x00);
  }

  /**
   * One mode-None chunk NetworkMessage: GroupHeader with WriterGroupId 258 (and the NetworkMessage
   * SequenceNumber when {@code networkMessageSequence} is non-null), chunk PayloadHeader with the
   * given DataSetWriterId, then the Table 159 fields.
   */
  private static byte[] unsecuredChunk(
      int dataSetWriterId,
      @Nullable Integer networkMessageSequence,
      int messageSequenceNumber,
      int chunkOffset,
      int totalSize,
      byte[] chunkData) {

    byte[] groupHeader =
        networkMessageSequence != null
            ? bytes(
                0x09, // GroupFlags: WriterGroupId | SequenceNumber
                0x02,
                0x01, // WriterGroupId = 258
                networkMessageSequence & 0xFF,
                (networkMessageSequence >> 8) & 0xFF)
            : bytes(
                0x01, // GroupFlags: WriterGroupId
                0x02, 0x01); // WriterGroupId = 258

    byte[] header =
        concat(
            bytes(
                0xE1, // byte 0: version 1 | GroupHeader 0x20 | PayloadHeader 0x40 | ExtFlags1 0x80
                0x80, // ExtendedFlags1: ExtendedFlags2 present
                0x01), // ExtendedFlags2: chunk, type Data
            groupHeader,
            bytes(
                dataSetWriterId & 0xFF,
                (dataSetWriterId >> 8) & 0xFF)); // PayloadHeader (chunk form, Table 158)

    return concat(header, chunkFields(messageSequenceNumber, chunkOffset, totalSize, chunkData));
  }

  /**
   * One signed (not encrypted) chunk NetworkMessage for WriterGroupId 258 / DataSetWriterId 5 with
   * the given NetworkMessage SequenceNumber: SecurityHeader (signed, token {@value #TOKEN_ID}),
   * plaintext Table 159 fields, HMAC-SHA256 signature over the whole message.
   */
  private static byte[] signedChunk(
      int networkMessageSequence,
      int messageSequenceNumber,
      int chunkOffset,
      int totalSize,
      byte[] chunkData)
      throws Exception {

    byte[] header =
        bytes(
            0xE1, // byte 0: version 1 | GroupHeader 0x20 | PayloadHeader 0x40 | ExtFlags1 0x80
            0x90, // ExtendedFlags1: SecurityHeader 0x10 | ExtendedFlags2 present 0x80
            0x01, // ExtendedFlags2: chunk, type Data
            0x09, // GroupFlags: WriterGroupId | SequenceNumber
            0x02,
            0x01, // WriterGroupId = 258
            networkMessageSequence & 0xFF,
            (networkMessageSequence >> 8) & 0xFF, // NetworkMessage SequenceNumber
            WRITER_ID,
            0x00, // PayloadHeader (chunk form, Table 158): DataSetWriterId = 5
            0x01, // SecurityFlags: NetworkMessage signed
            TOKEN_ID,
            0x00,
            0x00,
            0x00, // SecurityTokenId UInt32 LE
            0x08, // NonceLength = 8
            0xB1,
            0xB2,
            0xB3,
            0xB4,
            networkMessageSequence & 0xFF,
            0x00,
            0x00,
            0x00); // MessageNonce

    byte[] unsigned =
        concat(header, chunkFields(messageSequenceNumber, chunkOffset, totalSize, chunkData));

    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(Arrays.copyOfRange(keyData(), 0, 32), "HmacSHA256"));
    return concat(unsigned, mac.doFinal(unsigned));
  }

  /** The Table 159 chunk fields: MessageSequenceNumber, ChunkOffset, TotalSize, ChunkData. */
  private static byte[] chunkFields(
      int messageSequenceNumber, int chunkOffset, int totalSize, byte[] chunkData) {

    return concat(
        bytes(
            messageSequenceNumber & 0xFF,
            (messageSequenceNumber >> 8) & 0xFF,
            chunkOffset & 0xFF,
            (chunkOffset >> 8) & 0xFF,
            (chunkOffset >> 16) & 0xFF,
            (chunkOffset >> 24) & 0xFF,
            totalSize & 0xFF,
            (totalSize >> 8) & 0xFF,
            (totalSize >> 16) & 0xFF,
            (totalSize >> 24) & 0xFF,
            chunkData.length & 0xFF,
            (chunkData.length >> 8) & 0xFF,
            (chunkData.length >> 16) & 0xFF,
            (chunkData.length >> 24) & 0xFF),
        chunkData);
  }

  /** The fixed 68-byte key data, {@code 01 02 03 ...} (Table 155 layout); signing key first. */
  private static byte[] keyData() {
    byte[] keyData = new byte[POLICY.getKeyDataLength()];
    for (int i = 0; i < keyData.length; i++) {
      keyData[i] = (byte) (i + 1);
    }
    return keyData;
  }

  private static byte[] bytes(int... values) {
    byte[] bs = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bs[i] = (byte) values[i];
    }
    return bs;
  }

  private static byte[] concat(byte[]... arrays) {
    int length = 0;
    for (byte[] array : arrays) {
      length += array.length;
    }
    byte[] result = new byte[length];
    int offset = 0;
    for (byte[] array : arrays) {
      System.arraycopy(array, 0, result, offset, array.length);
      offset += array.length;
    }
    return result;
  }

  // endregion
}
