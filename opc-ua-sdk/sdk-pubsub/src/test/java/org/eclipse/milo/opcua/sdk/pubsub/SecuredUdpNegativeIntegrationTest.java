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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBufUtil;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyMaterial;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageSecurityContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Wire-level NEGATIVE rows against a live secured subscriber: hand-crafted secured datagrams are
 * injected at the dispatcher through a real UDP socket and the K6 security-drop counters are
 * asserted POSITIVELY (the green-path loopback tests only ever assert them zero).
 *
 * <ul>
 *   <li>N9 valid-replay half (K18): a byte-identical VALID secured NetworkMessage replayed at the
 *       subscriber verifies and is then dropped by the §7.2.3 window — {@code
 *       staleSequenceMessages} ticks while {@code invalidSignatureMessages} and {@code
 *       decryptionErrors} stay zero, proving the window runs AFTER verification.
 *   <li>N10 counted half (K6): an out-of-window SecurityTokenId ticks {@code unknownTokenMessages}
 *       (not {@code dataSetMessagesReceived}) and fires exactly one provider refresh.
 *   <li>DECRYPT_FAILED counter path: an encrypted NetworkMessage whose NonceLength is 0 (spliced,
 *       re-signed) verifies but cannot seed the cipher — {@code decryptionErrors} ticks.
 * </ul>
 *
 * <p>Crafted messages reuse the codec's own encode surface with a fixed nonce and fixed key data;
 * the subscriber holds the same key data under token {@value #TOKEN_ID} via a scripted, NON-static
 * provider (a static key set would suppress the unknown-token refresh by design).
 *
 * <p>Network safety: the subscriber pins an explicit loopback {@code discoveryAddress}; crafted
 * datagrams are sent to 127.0.0.1 only.
 */
class SecuredUdpNegativeIntegrationTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final PubSubSecurityPolicy POLICY = PubSubSecurityPolicy.PubSubAes128Ctr;

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4712));

  private static final String SECURITY_GROUP = "sg-negative";
  private static final SecurityGroupRef SECURITY_GROUP_REF = new SecurityGroupRef(SECURITY_GROUP);

  private static final DataSetReaderRef READER_REF =
      new DataSetReaderRef("sub-conn", "rgrp", "reader");

  private static final long TOKEN_ID = 1;

  private static final byte[] FIXED_NONCE = {
    (byte) 0xA1, (byte) 0xA2, (byte) 0xA3, (byte) 0xA4, 0x01, 0x00, 0x00, 0x00
  };

  /**
   * Offset of the SecurityHeader for the {@link #groupSettings()} mask with ONE writer: UADPFlags
   * (1) + ExtendedFlags1 (1) + PublisherId UInt16 (2) + GroupFlags (1) + WriterGroupId (2) +
   * SequenceNumber (2) + PayloadHeader count (1) + DataSetWriterId (2) = 12.
   */
  private static final int SECURITY_HEADER_OFFSET = 12;

  private static final int SIGNATURE_SIZE = 32;

  private final List<PubSubService> services = new CopyOnWriteArrayList<>();

  @AfterEach
  void tearDown() throws InterruptedException {
    for (PubSubService service : services) {
      try {
        service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        // best effort cleanup
      }
    }
    services.clear();
  }

  /**
   * N9, valid half: the first crafted (valid) datagram delivers; the byte-identical replay
   * verifies, reaches the §7.2.3 window, and drops as stale — the ordering proof is {@code
   * staleSequenceMessages == 1} with {@code invalidSignatureMessages == 0} and {@code
   * decryptionErrors == 0}.
   */
  @Test
  void validReplayDropsInTheSequenceWindowAfterVerification() throws Exception {
    int port = freeUdpPort();
    var provider = new CountingKeyProvider();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber = subscriber(port, MessageSecurityMode.Sign, provider, events);

    byte[] datagram = craftSecured(MessageSecurityMode.Sign, TOKEN_ID);

    send(datagram, port);
    DataSetReceivedEvent event = awaitEvent(events, e -> true);
    assertEquals(21.5, event.fieldsByName().get("temperature").value().value());

    // the byte-identical replay: verified, then dropped by the NetworkMessage window
    send(datagram, port);
    awaitTrue(
        () ->
            counter(
                    subscriber,
                    "sub-conn/rgrp/reader",
                    PubSubDiagnostics.ComponentDiagnostics::staleSequenceMessages)
                == 1,
        "staleSequenceMessages == 1 after the valid replay");

    // K18 ordering: only the post-verification window dropped it
    assertEquals(
        0,
        counter(
            subscriber,
            "sub-conn/rgrp",
            PubSubDiagnostics.ComponentDiagnostics::invalidSignatureMessages));
    assertEquals(
        0,
        counter(
            subscriber, "sub-conn/rgrp", PubSubDiagnostics.ComponentDiagnostics::decryptionErrors));
    assertEquals(
        0,
        counter(
            subscriber,
            "sub-conn/rgrp",
            PubSubDiagnostics.ComponentDiagnostics::unknownTokenMessages));

    // the duplicate was not delivered or counted as received
    assertEquals(
        1,
        counter(
            subscriber,
            "sub-conn/rgrp/reader",
            PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesReceived));
    assertNull(events.poll());
  }

  /**
   * N10, counted half: a secured message under an out-of-window SecurityTokenId ticks {@code
   * unknownTokenMessages} at the resolved group, delivers nothing, and fires exactly ONE
   * single-flight provider refresh.
   */
  @Test
  void unknownTokenTicksCounterAndFiresExactlyOneRefresh() throws Exception {
    int port = freeUdpPort();
    var provider = new CountingKeyProvider();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber = subscriber(port, MessageSecurityMode.Sign, provider, events);

    int fetchesBefore = provider.calls.get();
    assertEquals(1, fetchesBefore, "expected exactly the attach-time fetch before injection");

    // token 99 is far outside the {prev, current, futures} window
    byte[] datagram = craftSecured(MessageSecurityMode.Sign, 99);
    send(datagram, port);

    awaitTrue(
        () ->
            counter(
                    subscriber,
                    "sub-conn/rgrp",
                    PubSubDiagnostics.ComponentDiagnostics::unknownTokenMessages)
                == 1,
        "unknownTokenMessages == 1 after the unknown-token datagram");

    // exactly one refresh fires for the drop (single-flight, floored)
    awaitTrue(() -> provider.calls.get() == fetchesBefore + 1, "one provider refresh");
    assertEquals(fetchesBefore + 1, provider.calls.get());

    // dropped, not delivered: nothing was received or decoded
    assertEquals(
        0,
        counter(
            subscriber,
            "sub-conn/rgrp",
            PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesReceived));
    assertEquals(
        0,
        counter(
            subscriber,
            "sub-conn/rgrp/reader",
            PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesReceived));
    assertEquals(
        0,
        counter(
            subscriber,
            "sub-conn/rgrp",
            PubSubDiagnostics.ComponentDiagnostics::invalidSignatureMessages));
    assertEquals(
        0, counter(subscriber, "sub-conn", PubSubDiagnostics.ComponentDiagnostics::decodeErrors));
    assertNull(events.poll());
  }

  /**
   * The DECRYPT_FAILED counter path: an ENCRYPTED message whose NonceLength was spliced to 0 (and
   * re-signed, so verification passes) cannot seed the AES-CTR cipher — {@code decryptionErrors}
   * ticks at the group while {@code invalidSignatureMessages} stays zero.
   */
  @Test
  void zeroNonceLengthEncryptedMessageTicksDecryptionErrors() throws Exception {
    int port = freeUdpPort();
    var provider = new CountingKeyProvider();

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
    PubSubService subscriber =
        subscriber(port, MessageSecurityMode.SignAndEncrypt, provider, events);

    byte[] datagram = spliceNonceLengthToZero(craftSecured(MessageSecurityMode.SignAndEncrypt, 1));
    send(datagram, port);

    awaitTrue(
        () ->
            counter(
                    subscriber,
                    "sub-conn/rgrp",
                    PubSubDiagnostics.ComponentDiagnostics::decryptionErrors)
                == 1,
        "decryptionErrors == 1 after the zero-NonceLength datagram");

    // the signature verified: not an invalid-signature drop, and not a decode error
    assertEquals(
        0,
        counter(
            subscriber,
            "sub-conn/rgrp",
            PubSubDiagnostics.ComponentDiagnostics::invalidSignatureMessages));
    assertEquals(
        0, counter(subscriber, "sub-conn", PubSubDiagnostics.ComponentDiagnostics::decodeErrors));
    assertEquals(
        0,
        counter(
            subscriber,
            "sub-conn/rgrp",
            PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesReceived));
    assertNull(events.poll());
  }

  // region crafted datagrams

  /**
   * Encode one secured NetworkMessage matching the subscriber's reader identity — PublisherId
   * {@code 4712} (UInt16), WriterGroupId 1, DataSetWriterId 1, NetworkMessage/DataSetMessage
   * sequence number 1, ConfigurationVersion (7, 3), fields (21.5, "running") — with a fixed nonce
   * and the shared {@link #keyData()} under {@code tokenId}.
   */
  private byte[] craftSecured(MessageSecurityMode mode, long tokenId) throws Exception {
    SecurityKeyMaterial keys = SecurityKeyMaterial.split(POLICY, keyData());

    var securityContext = new MessageSecurityContext(mode, uint(tokenId), keys, FIXED_NONCE::clone);

    DataSetWriterConfig writer =
        DataSetWriterConfig.builder("writer")
            .dataSet(new PublishedDataSetRef("ds-neg"))
            .dataSetWriterId(ushort(1))
            .settings(
                UadpDataSetWriterSettings.builder()
                    .dataSetMessageContentMask(
                        UadpDataSetMessageContentMask.of(
                            UadpDataSetMessageContentMask.Field.MajorVersion,
                            UadpDataSetMessageContentMask.Field.MinorVersion,
                            UadpDataSetMessageContentMask.Field.SequenceNumber))
                    .build())
            .build();

    WriterGroupConfig group =
        WriterGroupConfig.builder("grp")
            .writerGroupId(ushort(1))
            .messageSettings(groupSettings())
            .messageSecurity(
                MessageSecurityConfig.builder()
                    .mode(mode)
                    .securityGroup(SECURITY_GROUP_REF)
                    .build())
            .dataSetWriter(writer)
            .build();

    EncodeContext context =
        EncodeContext.of(
            new DefaultEncodingContext(),
            PUBLISHER_ID,
            group,
            uint(0),
            ushort(1),
            ushort(1),
            null,
            List.of(
                DataSetMessageDraft.of(
                    writer,
                    uint(1),
                    null,
                    null,
                    new ConfigurationVersionDataType(uint(7), uint(3)),
                    false,
                    List.of(
                        new DataValue(Variant.ofDouble(21.5)),
                        new DataValue(Variant.ofString("running"))))),
            securityContext);

    List<EncodedNetworkMessage> encoded = new UadpMessageMapping().encode(context);
    assertEquals(1, encoded.size());
    byte[] message;
    try {
      message = ByteBufUtil.getBytes(encoded.get(0).data());
    } finally {
      encoded.get(0).data().release();
    }

    // sanity-pin the worked layout so the splice offsets below cannot silently drift
    assertEquals(0xF1, message[0] & 0xFF, "UADPFlags");
    assertEquals(0x11, message[1] & 0xFF, "ExtendedFlags1");
    assertEquals(
        mode == MessageSecurityMode.SignAndEncrypt ? 0x03 : 0x01,
        message[SECURITY_HEADER_OFFSET] & 0xFF,
        "SecurityFlags");
    assertEquals(8, message[SECURITY_HEADER_OFFSET + 5] & 0xFF, "NonceLength");

    return message;
  }

  /**
   * Splice the 8-byte MessageNonce out of a secured message (NonceLength := 0) and re-sign it: the
   * signature then verifies while an encrypted payload can no longer seed its cipher.
   */
  private static byte[] spliceNonceLengthToZero(byte[] secured) throws Exception {
    int nonceLengthOffset = SECURITY_HEADER_OFFSET + 5;

    byte[] modified = new byte[secured.length - 8];
    System.arraycopy(secured, 0, modified, 0, nonceLengthOffset);
    modified[nonceLengthOffset] = 0;
    System.arraycopy(
        secured,
        nonceLengthOffset + 1 + 8,
        modified,
        nonceLengthOffset + 1,
        secured.length - (nonceLengthOffset + 1 + 8));

    // re-sign with SigningKey = keyData[0, 32) over everything before the trailing signature
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(Arrays.copyOfRange(keyData().bytesOrEmpty(), 0, 32), "HmacSHA256"));
    mac.update(modified, 0, modified.length - SIGNATURE_SIZE);
    byte[] signature = mac.doFinal();
    System.arraycopy(signature, 0, modified, modified.length - SIGNATURE_SIZE, SIGNATURE_SIZE);

    return modified;
  }

  private static void send(byte[] datagram, int port) throws Exception {
    try (DatagramSocket socket = new DatagramSocket()) {
      socket.send(
          new DatagramPacket(datagram, datagram.length, InetAddress.getByName("127.0.0.1"), port));
    }
  }

  // endregion

  // region fixtures

  /**
   * Start a subscriber whose single secured reader group matches the crafted wire identity, and
   * wait for the group to complete its first key fetch (Operational) so no crafted datagram can
   * race the key-window installation into a {@code NO_KEYS} drop.
   */
  private PubSubService subscriber(
      int port,
      MessageSecurityMode mode,
      SecurityKeyProvider provider,
      BlockingQueue<DataSetReceivedEvent> events)
      throws Exception {

    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("ds-neg")
            .field("temperature", NodeIds.Double)
            .field("status", NodeIds.String)
            .configurationVersion(uint(7), uint(3))
            .build();

    DataSetReaderConfig reader =
        DataSetReaderConfig.builder("reader")
            .publisherId(PUBLISHER_ID)
            .writerGroupId(ushort(1))
            .dataSetWriterId(ushort(1))
            .dataSetMetaData(metaData)
            .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .securityGroup(
                SecurityGroupConfig.builder(SECURITY_GROUP)
                    .securityPolicyUri(POLICY.getUri())
                    .build())
            .connection(
                PubSubConnectionConfig.udp("sub-conn")
                    .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .readerGroup(
                        ReaderGroupConfig.builder("rgrp")
                            .messageSecurity(
                                MessageSecurityConfig.builder()
                                    .mode(mode)
                                    .securityGroup(SECURITY_GROUP_REF)
                                    .build())
                            .dataSetReader(reader)
                            .build())
                    .build())
            .build();

    PubSubService subscriber =
        PubSubService.create(
            config,
            PubSubBindings.builder()
                .listener(READER_REF, events::add)
                .securityKeys(SECURITY_GROUP_REF, provider)
                .build());
    services.add(subscriber);

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    PubSubHandle group = subscriber.components().readerGroup("sub-conn", "rgrp").orElseThrow();
    awaitTrue(
        () -> subscriber.state(group) == PubSubState.Operational,
        "secured reader group Operational (first key fetch complete)");

    return subscriber;
  }

  /** PublisherId | GroupHeader (WriterGroupId + SequenceNumber) | PayloadHeader. */
  private static UadpWriterGroupSettings groupSettings() {
    return UadpWriterGroupSettings.builder()
        .networkMessageContentMask(
            UadpNetworkMessageContentMask.of(
                UadpNetworkMessageContentMask.Field.PublisherId,
                UadpNetworkMessageContentMask.Field.GroupHeader,
                UadpNetworkMessageContentMask.Field.WriterGroupId,
                UadpNetworkMessageContentMask.Field.SequenceNumber,
                UadpNetworkMessageContentMask.Field.PayloadHeader))
        .build();
  }

  /** Deterministic key data ({@code 01 02 03 ...}) shared by the crafter and the subscriber. */
  private static ByteString keyData() {
    byte[] bytes = new byte[POLICY.getKeyDataLength()];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (i + 1);
    }
    return ByteString.of(bytes);
  }

  /**
   * A scripted NON-static provider (long, boring lifetimes: no rotation interferes within the test
   * window) that counts {@code getKeys} calls so the unknown-token single-flight refresh is
   * observable. A {@link org.eclipse.milo.opcua.sdk.pubsub.security.StaticSecurityKeyProvider}
   * would not do: the key manager never refreshes a static key set.
   */
  private static final class CountingKeyProvider implements SecurityKeyProvider {

    final AtomicInteger calls = new AtomicInteger(0);

    @Override
    public CompletableFuture<SecurityKeySet> getKeys(
        String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount) {

      calls.incrementAndGet();

      return CompletableFuture.completedFuture(
          new SecurityKeySet(
              POLICY.getUri(),
              uint(TOKEN_ID),
              List.of(keyData()),
              Duration.ofHours(1),
              Duration.ofHours(2)));
    }
  }

  // endregion

  // region helpers

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  /** Wait for an event matching {@code predicate}, discarding non-matching events. */
  private static DataSetReceivedEvent awaitEvent(
      BlockingQueue<DataSetReceivedEvent> events, Predicate<DataSetReceivedEvent> predicate)
      throws InterruptedException {

    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (true) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        fail("timed out waiting for a matching DataSetReceivedEvent");
      }
      DataSetReceivedEvent event =
          events.poll(Math.min(remaining, 100_000_000L), TimeUnit.NANOSECONDS);
      if (event != null && predicate.test(event)) {
        return event;
      }
    }
  }

  /** Poll {@code condition} until it holds or the deadline expires. */
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

  private static long counter(
      PubSubService service,
      String path,
      ToLongFunction<PubSubDiagnostics.ComponentDiagnostics> counter) {

    PubSubDiagnostics.ComponentDiagnostics diagnostics =
        service.diagnostics().component(path).orElse(null);

    return diagnostics == null ? 0L : counter.applyAsLong(diagnostics);
  }

  // endregion
}
