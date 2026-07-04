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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.KeyFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
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
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.sdk.pubsub.security.StaticSecurityKeyProvider;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * End-to-end SECURED integration tests over unicast loopback UDP with ephemeral ports: a publisher
 * service and a subscriber service exchanging signed / signed-and-encrypted UADP NetworkMessages
 * in-process, with a {@link StaticSecurityKeyProvider} pinning the same key data on both sides
 * (static sentinel: no rotation, no expiry).
 *
 * <p>Coverage includes both PubSub security policies for {@code Sign} and {@code SignAndEncrypt},
 * the §7.2.4.3 receive-mode gate (drop lower, process higher), tampered-datagram rejection (invalid
 * signature counted, clean traffic unaffected), the missing-provider startup gate, and a
 * secured-group reconfigure smoke test. Rollover, token windows, and negative codec cases live in
 * the SecurityKeyManager and codec test suites; this class focuses on green-path behavior and the
 * wire-level checks that need real sockets.
 *
 * <p>Network safety (same rules as {@link UdpLoopbackIntegrationTest}): every UDP connection pins
 * an explicit loopback {@code discoveryAddress}, so the engine's discovery channels never bind the
 * well-known port 4840 or join the default {@code 224.0.2.14} multicast group.
 */
class SecuredUdpLoopbackIntegrationTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);
  private static final Duration PUBLISHING_INTERVAL = Duration.ofMillis(50);

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4712));
  private static final UShort GROUP_ID = ushort(1);

  private static final UUID TEMPERATURE_FIELD_ID = new UUID(0L, 0xD1L);
  private static final UUID STATUS_FIELD_ID = new UUID(0L, 0xD2L);

  private static final PublishedDataSetRef DATA_SET_REF = new PublishedDataSetRef("ds-sec");

  private static final String SECURITY_GROUP = "sg-loopback";
  private static final SecurityGroupRef SECURITY_GROUP_REF = new SecurityGroupRef(SECURITY_GROUP);

  private static final DataSetReaderRef READER_REF =
      new DataSetReaderRef("sub-conn", "rgrp", "reader");

  /** Both PubSub security policies sign with HMAC-SHA256: 32 trailing signature bytes. */
  private static final int SIGNATURE_LENGTH = 32;

  /**
   * Group settings that put the GroupHeader with WriterGroupId on the wire so readers can apply
   * their WriterGroupId filter and both §7.2.3 sequence streams are exercised under security.
   */
  private static final UadpWriterGroupSettings GROUP_SETTINGS =
      UadpWriterGroupSettings.builder()
          .networkMessageContentMask(
              UadpNetworkMessageContentMask.of(
                  UadpNetworkMessageContentMask.Field.PublisherId,
                  UadpNetworkMessageContentMask.Field.GroupHeader,
                  UadpNetworkMessageContentMask.Field.WriterGroupId,
                  UadpNetworkMessageContentMask.Field.SequenceNumber,
                  UadpNetworkMessageContentMask.Field.PayloadHeader))
          .build();

  /** Writer settings putting the full ConfigurationVersion on the wire (REQUIRE_CONFIGURED). */
  private static final UadpDataSetWriterSettings WRITER_SETTINGS =
      UadpDataSetWriterSettings.builder()
          .dataSetMessageContentMask(
              UadpDataSetMessageContentMask.of(
                  UadpDataSetMessageContentMask.Field.Timestamp,
                  UadpDataSetMessageContentMask.Field.Status,
                  UadpDataSetMessageContentMask.Field.MajorVersion,
                  UadpDataSetMessageContentMask.Field.MinorVersion,
                  UadpDataSetMessageContentMask.Field.SequenceNumber))
          .build();

  private final List<PubSubService> services = new CopyOnWriteArrayList<>();

  @AfterEach
  void tearDown() throws InterruptedException {
    for (PubSubService service : services) {
      try {
        service.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        // best effort cleanup; failures are reported by the tests themselves
      }
    }
    services.clear();
  }

  static Stream<Arguments> securedRows() {
    return Stream.of(
        Arguments.of(MessageSecurityMode.SignAndEncrypt, PubSubSecurityPolicy.PubSubAes128Ctr),
        Arguments.of(MessageSecurityMode.SignAndEncrypt, PubSubSecurityPolicy.PubSubAes256Ctr),
        Arguments.of(MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes128Ctr),
        Arguments.of(MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes256Ctr));
  }

  /**
   * Secured publisher → secured subscriber end-to-end, correct field values (including a fresh
   * value published mid-test, proving ongoing verify/decrypt of new ciphertext), reader
   * Operational, and zero security-drop and sequence-drop counters.
   */
  @ParameterizedTest(name = "{0} x {1}")
  @MethodSource("securedRows")
  void securedEndToEndPublishSubscribe(MessageSecurityMode mode, PubSubSecurityPolicy policy)
      throws Exception {

    int port = freeUdpPort();

    var values =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(21.5)),
                "status", new DataValue(Variant.ofString("running"))));

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(port, freeUdpPort(), PUBLISHING_INTERVAL, mode, policy),
                PubSubBindings.builder()
                    .source(DATA_SET_REF, mapSource(values))
                    .securityKeys(SECURITY_GROUP_REF, provider(policy))
                    .build()));

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubService subscriber =
        track(
            PubSubService.create(
                subscriberConfig(port, mode, policy),
                PubSubBindings.builder()
                    .listener(READER_REF, events::add)
                    .securityKeys(SECURITY_GROUP_REF, provider(policy))
                    .build()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    DataSetReceivedEvent event = awaitEvent(events, e -> true);
    assertEquals(PUBLISHER_ID, event.publisherId());
    assertEquals(GROUP_ID, event.writerGroupId());
    assertEquals(ushort(1), event.dataSetWriterId());
    assertEquals("ds-sec", event.dataSetName());
    assertEquals(21.5, event.fieldsByName().get("temperature").value().value());
    assertEquals("running", event.fieldsByName().get("status").value().value());

    // fresh ciphertext keeps verifying/decrypting: an updated value arrives end-to-end
    values.set(
        Map.of(
            "temperature", new DataValue(Variant.ofDouble(23.25)),
            "status", new DataValue(Variant.ofString("running"))));
    awaitEvent(
        events,
        e -> Double.valueOf(23.25).equals(e.fieldsByName().get("temperature").value().value()));

    // the reader completed startup on its first decoded (secured) DataSetMessage
    PubSubHandle reader =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader").orElseThrow();
    assertEquals(PubSubState.Operational, subscriber.state(reader));

    // publisher side: secured cycles ran, none were skipped for want of keys
    assertTrue(
        counter(
                publisher,
                "pub-conn/grp",
                PubSubDiagnostics.ComponentDiagnostics::networkMessagesSent)
            > 0);
    assertEquals(
        0,
        counter(
            publisher, "pub-conn/grp", PubSubDiagnostics.ComponentDiagnostics::encryptionErrors));

    // subscriber side: zero security drops of any kind ...
    assertZeroSecurityDrops(subscriber, "sub-conn/rgrp");

    // ... and organic secured traffic produces no sequence-window drops either
    assertEquals(
        0,
        counter(
            subscriber,
            "sub-conn/rgrp/reader",
            PubSubDiagnostics.ComponentDiagnostics::staleSequenceMessages));
    assertEquals(
        0,
        counter(
            subscriber,
            "sub-conn/rgrp/reader",
            PubSubDiagnostics.ComponentDiagnostics::invalidSequenceMessages));
  }

  /**
   * The §7.2.4.3 receive-mode gate, both directions at once, against live SignAndEncrypt traffic:
   *
   * <ul>
   *   <li>SHALL drop lower: a mode-None reader group matching the same traffic receives nothing,
   *       ticks {@code staleKeyMessages} (the as-built mode-mismatch counter — never {@code
   *       decodeErrors}), and its reader stays PreOperational.
   *   <li>MAY process higher: a Sign-configured reader group holding the SecurityGroup's keys
   *       processes the SignAndEncrypt traffic end-to-end.
   * </ul>
   */
  @Test
  void modeNoneGroupDropsSecuredTrafficWhileSignGroupProcessesIt() throws Exception {
    int port = freeUdpPort();
    PubSubSecurityPolicy policy = PubSubSecurityPolicy.PubSubAes128Ctr;

    var values =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(21.5)),
                "status", new DataValue(Variant.ofString("running"))));

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(
                    port,
                    freeUdpPort(),
                    PUBLISHING_INTERVAL,
                    MessageSecurityMode.SignAndEncrypt,
                    policy),
                PubSubBindings.builder()
                    .source(DATA_SET_REF, mapSource(values))
                    .securityKeys(SECURITY_GROUP_REF, provider(policy))
                    .build()));

    var signEvents = new LinkedBlockingQueue<DataSetReceivedEvent>();
    var noneEvents = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubService subscriber =
        track(
            PubSubService.create(
                mixedModeSubscriberConfig(port, policy),
                PubSubBindings.builder()
                    .listener(
                        new DataSetReaderRef("sub-conn", "rgrp-sign", "reader"), signEvents::add)
                    .listener(
                        new DataSetReaderRef("sub-conn", "rgrp-none", "reader"), noneEvents::add)
                    .securityKeys(SECURITY_GROUP_REF, provider(policy))
                    .build()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // MAY row: the Sign-configured group processes the higher SignAndEncrypt traffic
    DataSetReceivedEvent event = awaitEvent(signEvents, e -> true);
    assertEquals(21.5, event.fieldsByName().get("temperature").value().value());
    for (int i = 0; i < 2; i++) {
      awaitEvent(signEvents, e -> true);
    }

    // SHALL row: the mode-None group's matching traffic is dropped and counted
    awaitTrue(
        () ->
            counter(
                    subscriber,
                    "sub-conn/rgrp-none",
                    PubSubDiagnostics.ComponentDiagnostics::staleKeyMessages)
                > 0,
        "staleKeyMessages on the mode-None reader group");

    // the mode-None reader received no events and never completed startup
    assertNull(noneEvents.poll());
    PubSubHandle noneReader =
        subscriber.components().dataSetReader("sub-conn", "rgrp-none", "reader").orElseThrow();
    assertEquals(PubSubState.PreOperational, subscriber.state(noneReader));
    assertEquals(
        0,
        counter(
            subscriber,
            "sub-conn/rgrp-none/reader",
            PubSubDiagnostics.ComponentDiagnostics::dataSetMessagesReceived));

    PubSubHandle signReader =
        subscriber.components().dataSetReader("sub-conn", "rgrp-sign", "reader").orElseThrow();
    assertEquals(PubSubState.Operational, subscriber.state(signReader));

    // the mode-mismatch drops are security drops, never decodeErrors, and only on the None group
    assertEquals(
        0,
        counter(
            subscriber,
            "sub-conn/rgrp-none",
            PubSubDiagnostics.ComponentDiagnostics::decodeErrors));
    assertZeroSecurityDrops(subscriber, "sub-conn/rgrp-sign");
  }

  /**
   * Tampered-datagram injection: a genuine secured datagram is captured off the wire with a raw
   * socket, one payload (ciphertext) byte is flipped, and the result is replayed at the subscriber.
   * The forgery ticks {@code invalidSignatureMessages} (verify fails BEFORE decrypt —
   * encrypt-then-sign, so {@code decryptionErrors} stays zero), produces no DataSet event, and
   * leaves no corrupted state behind: subsequent clean traffic still delivers.
   */
  @Test
  void tamperedDatagramIsCountedAndCleanTrafficStillDelivered() throws Exception {
    PubSubSecurityPolicy policy = PubSubSecurityPolicy.PubSubAes128Ctr;
    int subscriberPort = freeUdpPort();
    int capturePort = freeUdpPort();

    var values =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(21.5)),
                "status", new DataValue(Variant.ofString("running"))));

    try (DatagramSocket rawSocket =
        new DatagramSocket(new InetSocketAddress("127.0.0.1", capturePort))) {
      rawSocket.setSoTimeout((int) TIMEOUT.toMillis());

      // capture one genuine secured datagram: a publisher identical to the live one below sends
      // to the raw socket's port (publisher channels bind only ephemeral local ports, so the raw
      // socket owns the destination port without conflict)
      PubSubService capturePublisher =
          track(
              PubSubService.create(
                  publisherConfig(
                      capturePort,
                      freeUdpPort(),
                      PUBLISHING_INTERVAL,
                      MessageSecurityMode.SignAndEncrypt,
                      policy),
                  PubSubBindings.builder()
                      .source(DATA_SET_REF, mapSource(values))
                      .securityKeys(SECURITY_GROUP_REF, provider(policy))
                      .build()));
      capturePublisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      var packet = new DatagramPacket(new byte[65535], 65535);
      rawSocket.receive(packet);
      byte[] captured = Arrays.copyOf(packet.getData(), packet.getLength());
      assertTrue(captured.length > SIGNATURE_LENGTH + 8, "captured datagram implausibly short");

      capturePublisher.shutdown().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      // live secured pair
      PubSubService publisher =
          track(
              PubSubService.create(
                  publisherConfig(
                      subscriberPort,
                      freeUdpPort(),
                      PUBLISHING_INTERVAL,
                      MessageSecurityMode.SignAndEncrypt,
                      policy),
                  PubSubBindings.builder()
                      .source(DATA_SET_REF, mapSource(values))
                      .securityKeys(SECURITY_GROUP_REF, provider(policy))
                      .build()));

      var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
      var allEvents = new CopyOnWriteArrayList<DataSetReceivedEvent>();

      PubSubService subscriber =
          track(
              PubSubService.create(
                  subscriberConfig(subscriberPort, MessageSecurityMode.SignAndEncrypt, policy),
                  PubSubBindings.builder()
                      .listener(
                          READER_REF,
                          event -> {
                            allEvents.add(event);
                            events.add(event);
                          })
                      .securityKeys(SECURITY_GROUP_REF, provider(policy))
                      .build()));

      subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      awaitEvent(events, e -> true);

      // flip one payload byte: the last ciphertext byte, just before the trailing 32-byte HMAC
      byte[] tampered = captured.clone();
      tampered[tampered.length - SIGNATURE_LENGTH - 1] ^= 0x01;
      rawSocket.send(
          new DatagramPacket(
              tampered, tampered.length, InetAddress.getByName("127.0.0.1"), subscriberPort));

      // the forgery is counted as an invalid signature against the group whose keys were used
      awaitTrue(
          () ->
              counter(
                      subscriber,
                      "sub-conn/rgrp",
                      PubSubDiagnostics.ComponentDiagnostics::invalidSignatureMessages)
                  >= 1,
          "invalidSignatureMessages after tampered datagram injection");

      // verify failed before decrypt ran, and the drop was a security drop, not a decode error
      assertEquals(
          0,
          counter(
              subscriber,
              "sub-conn/rgrp",
              PubSubDiagnostics.ComponentDiagnostics::decryptionErrors));
      assertEquals(
          0,
          counter(
              subscriber, "sub-conn/rgrp", PubSubDiagnostics.ComponentDiagnostics::decodeErrors));
      assertEquals(
          0, counter(subscriber, "sub-conn", PubSubDiagnostics.ComponentDiagnostics::decodeErrors));

      // the §7.2.3 sequence windows run AFTER verification, so a
      // tampered replay ticks ONLY invalidSignatureMessages — it never reaches the windows and
      // the sequence-drop counters stay zero (organic loopback traffic is ordered, so it
      // contributes nothing either)
      assertEquals(
          0,
          counter(
              subscriber,
              "sub-conn/rgrp/reader",
              PubSubDiagnostics.ComponentDiagnostics::staleSequenceMessages));
      assertEquals(
          0,
          counter(
              subscriber,
              "sub-conn/rgrp/reader",
              PubSubDiagnostics.ComponentDiagnostics::invalidSequenceMessages));

      // no state corruption: subsequent clean traffic still delivers and the reader stays up
      events.clear();
      awaitEvent(events, e -> true);
      PubSubHandle reader =
          subscriber.components().dataSetReader("sub-conn", "rgrp", "reader").orElseThrow();
      assertEquals(PubSubState.Operational, subscriber.state(reader));

      // no DataSet event ever came from the tampered datagram: every delivered event carries the
      // plaintext the live publisher actually published
      for (DataSetReceivedEvent event : allEvents) {
        assertEquals(21.5, event.fieldsByName().get("temperature").value().value());
        assertEquals("running", event.fieldsByName().get("status").value().value());
      }
    }
  }

  /**
   * Live token rollover on the wire: publisher and subscriber each pull from the same scripted
   * rotating "SKS" (TimeToNextKey ≈ 500 ms, KeyLifetime ≈ 1 s, several tokens) and must stay in
   * sync across ≥ 2 token switches with zero security drops of any kind.
   *
   * <p>The switches are OBSERVED on the wire, not assumed from wall-clock time: a witness writer
   * group on the same SecurityGroup publishes to a raw socket, and the test parses the plaintext
   * SecurityTokenId out of each witness datagram until token {@code firstToken + 2} has been seen.
   * Only then is a fresh value published and awaited end-to-end — proving the subscriber decrypts
   * post-rollover ciphertext — before asserting zero {@code unknownTokenMessages} (and every other
   * security drop counter) on the subscriber.
   */
  @Test
  void liveTokenRolloverKeepsDeliveryWithZeroUnknownTokenDrops() throws Exception {
    PubSubSecurityPolicy policy = PubSubSecurityPolicy.PubSubAes128Ctr;
    int subscriberPort = freeUdpPort();
    int witnessPort = freeUdpPort();

    Duration timeToFirstSwitch = Duration.ofMillis(500);
    Duration keyLifetime = Duration.ofSeconds(1);
    long epochNanos = System.nanoTime();

    var values =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(21.5)),
                "status", new DataValue(Variant.ofString("running"))));

    try (DatagramSocket witnessSocket =
        new DatagramSocket(new InetSocketAddress("127.0.0.1", witnessPort))) {
      witnessSocket.setSoTimeout(50);

      // publisher and subscriber bind their own provider instances against the same scripted
      // SKS: shared rotation epoch, shared per-token key derivation
      PubSubService publisher =
          track(
              PubSubService.create(
                  rolloverPublisherConfig(subscriberPort, witnessPort, policy),
                  PubSubBindings.builder()
                      .source(DATA_SET_REF, mapSource(values))
                      .securityKeys(
                          SECURITY_GROUP_REF,
                          new RotatingKeyProvider(
                              policy, epochNanos, timeToFirstSwitch, keyLifetime))
                      .build()));

      var events = new LinkedBlockingQueue<DataSetReceivedEvent>();

      PubSubService subscriber =
          track(
              PubSubService.create(
                  subscriberConfig(subscriberPort, MessageSecurityMode.SignAndEncrypt, policy),
                  PubSubBindings.builder()
                      .listener(READER_REF, events::add)
                      .securityKeys(
                          SECURITY_GROUP_REF,
                          new RotatingKeyProvider(
                              policy, epochNanos, timeToFirstSwitch, keyLifetime))
                      .build()));

      subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
      publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      // pre-rollover delivery under the first token
      awaitEvent(events, e -> true);

      // watch the wire until two switches past the FIRST observed token have demonstrably
      // happened (first → first+1 → first+2; anchoring on the first observed token keeps the
      // test deterministic even if a slow start misses the very first schedule slot)
      var observedTokens = new HashSet<Long>();
      Long firstToken = null;
      long deadline = System.nanoTime() + TIMEOUT.toNanos();
      var packet = new DatagramPacket(new byte[65535], 65535);
      while (firstToken == null || !observedTokens.contains(firstToken + 2)) {
        if (System.nanoTime() >= deadline) {
          fail("timed out waiting for two token switches; observed tokens: " + observedTokens);
        }
        try {
          witnessSocket.receive(packet);
        } catch (SocketTimeoutException e) {
          continue;
        }
        Long tokenId = witnessTokenId(Arrays.copyOf(packet.getData(), packet.getLength()));
        if (tokenId != null) {
          observedTokens.add(tokenId);
          if (firstToken == null) {
            firstToken = tokenId;
          }
        }
      }
      assertTrue(
          observedTokens.containsAll(List.of(firstToken, firstToken + 1, firstToken + 2)),
          observedTokens.toString());

      // post-rollover ciphertext still decrypts end-to-end: a fresh value published under the
      // switched token arrives
      values.set(
          Map.of(
              "temperature", new DataValue(Variant.ofDouble(99.5)),
              "status", new DataValue(Variant.ofString("rolled"))));
      awaitEvent(
          events,
          e -> Double.valueOf(99.5).equals(e.fieldsByName().get("temperature").value().value()));

      // zero drops across the rollover: no unknown tokens, no stale keys, nothing
      assertZeroSecurityDrops(subscriber, "sub-conn/rgrp");
      assertEquals(
          0,
          counter(
              publisher, "pub-conn/grp", PubSubDiagnostics.ComponentDiagnostics::encryptionErrors));
      assertEquals(
          0,
          counter(
              publisher,
              "pub-conn-witness/grp-witness",
              PubSubDiagnostics.ComponentDiagnostics::encryptionErrors));
    }
  }

  /**
   * Missing-provider gate at integration level: an enabled secured group whose SecurityGroup has no
   * bound {@link org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyProvider} fails startup with
   * {@code Bad_ConfigurationError} naming the missing provider — on both the writer-group and the
   * reader-group side.
   */
  @Test
  void startupFailsWhenNoSecurityKeyProviderIsBound() throws Exception {
    PubSubSecurityPolicy policy = PubSubSecurityPolicy.PubSubAes128Ctr;

    var values =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(0.0)),
                "status", new DataValue(Variant.ofString("idle"))));

    // publisher side: source bound, but no securityKeys binding for the referenced group
    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(
                    freeUdpPort(),
                    freeUdpPort(),
                    PUBLISHING_INTERVAL,
                    MessageSecurityMode.Sign,
                    policy),
                PubSubBindings.builder().source(DATA_SET_REF, mapSource(values)).build()));

    UaException publisherCause = assertStartupFailsExceptionally(publisher);
    assertEquals(StatusCodes.Bad_ConfigurationError, publisherCause.getStatusCode().value());
    assertTrue(
        String.valueOf(publisherCause.getMessage()).contains("SecurityKeyProvider"),
        "expected the missing SecurityKeyProvider to be named: " + publisherCause.getMessage());

    // subscriber side: the same gate applies to secured reader groups
    PubSubService subscriber =
        track(
            PubSubService.create(
                subscriberConfig(freeUdpPort(), MessageSecurityMode.Sign, policy)));

    UaException subscriberCause = assertStartupFailsExceptionally(subscriber);
    assertEquals(StatusCodes.Bad_ConfigurationError, subscriberCause.getStatusCode().value());
    assertTrue(
        String.valueOf(subscriberCause.getMessage()).contains("SecurityKeyProvider"),
        "expected the missing SecurityKeyProvider to be named: " + subscriberCause.getMessage());
  }

  /**
   * Reconfigure smoke test: a benign change (publishingInterval) on the secured writer group
   * restarts it; the restarted group re-attaches to the key manager, returns to Operational, and
   * traffic resumes SECURED — the SignAndEncrypt-configured reader group only ever accepts received
   * SignAndEncrypt messages, so post-reconfigure delivery proves the wire stayed encrypted.
   *
   * <p>Sequence-window counters are deliberately NOT asserted here: this test's concern is that the
   * restarted group's traffic stays secured. A path-stable group restart preserves the
   * NetworkMessage and DataSetMessage sequence numbers, so there is no drop window (see {@link
   * PubSubService#reconfigure}); that continuity is asserted by {@code
   * ReconfigureIntegrationTest#pathStableGroupRestartPreservesCountersAndSequenceNumbers}.
   */
  @Test
  void reconfiguredSecuredGroupResumesSecuredTraffic() throws Exception {
    int port = freeUdpPort();
    int discoveryPort = freeUdpPort(); // reused so only the group's publishingInterval differs
    PubSubSecurityPolicy policy = PubSubSecurityPolicy.PubSubAes256Ctr;

    var values =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(21.5)),
                "status", new DataValue(Variant.ofString("running"))));

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(
                    port,
                    discoveryPort,
                    PUBLISHING_INTERVAL,
                    MessageSecurityMode.SignAndEncrypt,
                    policy),
                PubSubBindings.builder()
                    .source(DATA_SET_REF, mapSource(values))
                    .securityKeys(SECURITY_GROUP_REF, provider(policy))
                    .build()));

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubService subscriber =
        track(
            PubSubService.create(
                subscriberConfig(port, MessageSecurityMode.SignAndEncrypt, policy),
                PubSubBindings.builder()
                    .listener(READER_REF, events::add)
                    .securityKeys(SECURITY_GROUP_REF, provider(policy))
                    .build()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    awaitEvent(events, e -> true);

    // benign change on the secured group: only the publishingInterval differs
    ReconfigureResult result =
        publisher.reconfigure(
            publisherConfig(
                port,
                discoveryPort,
                Duration.ofMillis(90),
                MessageSecurityMode.SignAndEncrypt,
                policy),
            PubSubService.ReconfigureMode.DISABLE_AFFECTED);
    assertEquals(List.of("pub-conn/grp"), result.restartedPaths());

    // the restarted secured group re-attaches to the key manager and reaches Operational again
    PubSubHandle group = publisher.components().writerGroup("pub-conn", "grp").orElseThrow();
    awaitTrue(
        () -> publisher.state(group) == PubSubState.Operational,
        "secured writer group Operational after reconfigure");

    // secured traffic resumes: a fresh value published after the reconfigure arrives end-to-end
    values.set(
        Map.of(
            "temperature", new DataValue(Variant.ofDouble(42.0)),
            "status", new DataValue(Variant.ofString("resumed"))));
    events.clear();
    awaitEvent(
        events,
        e -> Double.valueOf(42.0).equals(e.fieldsByName().get("temperature").value().value()));

    // still zero security drops on the subscriber and zero skipped cycles on the publisher
    assertZeroSecurityDrops(subscriber, "sub-conn/rgrp");
    assertEquals(
        0,
        counter(
            publisher, "pub-conn/grp", PubSubDiagnostics.ComponentDiagnostics::encryptionErrors));
  }

  // region fixtures

  /**
   * Publisher config: one UDP connection sending to 127.0.0.1:{@code port}, one secured writer
   * group with a single writer on the two-field dataset.
   *
   * @param discoveryPort passed explicitly so a reconfigured config can keep the connection shell
   *     identical.
   */
  private static PubSubConfig publisherConfig(
      int port,
      int discoveryPort,
      Duration publishingInterval,
      MessageSecurityMode mode,
      PubSubSecurityPolicy policy) {

    PublishedDataSetConfig dataSet = securedDataSet();

    WriterGroupConfig writerGroup =
        WriterGroupConfig.builder("grp")
            .writerGroupId(GROUP_ID)
            .publishingInterval(publishingInterval)
            .messageSettings(GROUP_SETTINGS)
            .messageSecurity(messageSecurity(mode))
            .dataSetWriter(
                DataSetWriterConfig.builder("writer")
                    .dataSet(dataSet.ref())
                    .dataSetWriterId(ushort(1))
                    .settings(WRITER_SETTINGS)
                    .build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .securityGroup(securityGroup(policy))
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", discoveryPort))
                .writerGroup(writerGroup)
                .build())
        .build();
  }

  /**
   * Subscriber config matching {@link #publisherConfig}: one secured reader group with one
   * REQUIRE_CONFIGURED reader, bound to 127.0.0.1:{@code port}.
   */
  private static PubSubConfig subscriberConfig(
      int port, MessageSecurityMode mode, PubSubSecurityPolicy policy) throws SocketException {

    ReaderGroupConfig readerGroup =
        ReaderGroupConfig.builder("rgrp")
            .messageSecurity(messageSecurity(mode))
            .dataSetReader(readerConfig("reader"))
            .build();

    return PubSubConfig.builder()
        .securityGroup(securityGroup(policy))
        .connection(
            PubSubConnectionConfig.udp("sub-conn")
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(readerGroup)
                .build())
        .build();
  }

  /**
   * Subscriber config for the receive-mode matrix: TWO reader groups matching the same secured
   * traffic — "rgrp-none" with no message security (declared first, proving secured routing skips
   * it) and "rgrp-sign" configured Sign with the SecurityGroup's keys.
   */
  private static PubSubConfig mixedModeSubscriberConfig(int port, PubSubSecurityPolicy policy)
      throws SocketException {

    ReaderGroupConfig noneGroup =
        ReaderGroupConfig.builder("rgrp-none").dataSetReader(readerConfig("reader")).build();

    ReaderGroupConfig signGroup =
        ReaderGroupConfig.builder("rgrp-sign")
            .messageSecurity(messageSecurity(MessageSecurityMode.Sign))
            .dataSetReader(readerConfig("reader"))
            .build();

    return PubSubConfig.builder()
        .securityGroup(securityGroup(policy))
        .connection(
            PubSubConnectionConfig.udp("sub-conn")
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(noneGroup)
                .readerGroup(signGroup)
                .build())
        .build();
  }

  private static PublishedDataSetConfig securedDataSet() {
    return PublishedDataSetConfig.builder("ds-sec")
        .field(
            FieldDefinition.builder("temperature")
                .dataType(NodeIds.Double)
                .dataSetFieldId(TEMPERATURE_FIELD_ID)
                .build())
        .field(
            FieldDefinition.builder("status")
                .dataType(NodeIds.String)
                .dataSetFieldId(STATUS_FIELD_ID)
                .build())
        .configurationVersion(uint(7), uint(3))
        .build();
  }

  private static DataSetReaderConfig readerConfig(String name) {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("ds-sec")
            .field("temperature", NodeIds.Double, TEMPERATURE_FIELD_ID)
            .field("status", NodeIds.String, STATUS_FIELD_ID)
            .configurationVersion(uint(7), uint(3))
            .build();

    return DataSetReaderConfig.builder(name)
        .publisherId(PUBLISHER_ID)
        .writerGroupId(GROUP_ID)
        .dataSetWriterId(ushort(1))
        .dataSetMetaData(metaData)
        .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
        .build();
  }

  private static SecurityGroupConfig securityGroup(PubSubSecurityPolicy policy) {
    return SecurityGroupConfig.builder(SECURITY_GROUP).securityPolicyUri(policy.getUri()).build();
  }

  private static MessageSecurityConfig messageSecurity(MessageSecurityMode mode) {
    return MessageSecurityConfig.builder().mode(mode).securityGroup(SECURITY_GROUP_REF).build();
  }

  /**
   * A static provider pinning the deterministic {@link #keyData} under token id 1; both sides of
   * every test bind an equal provider for {@link #SECURITY_GROUP_REF}.
   */
  private static StaticSecurityKeyProvider provider(PubSubSecurityPolicy policy)
      throws UaException {
    return StaticSecurityKeyProvider.of(policy, keyData(policy));
  }

  /** Deterministic key data ({@code 01 02 03 ...}, Table 155 layout) shared by both sides. */
  private static ByteString keyData(PubSubSecurityPolicy policy) {
    byte[] bytes = new byte[policy.getKeyDataLength()];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (i + 1);
    }
    return ByteString.of(bytes);
  }

  /**
   * The rollover publisher: the regular secured connection ("pub-conn"/"grp", matching {@link
   * #subscriberConfig}) plus a witness connection whose SignAndEncrypt writer group shares the same
   * SecurityGroup — and therefore the same token schedule — but publishes to the witness port where
   * a raw socket reads the plaintext SecurityTokenId off the wire.
   */
  private static PubSubConfig rolloverPublisherConfig(
      int subscriberPort, int witnessPort, PubSubSecurityPolicy policy) throws SocketException {

    PublishedDataSetConfig dataSet = securedDataSet();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .securityGroup(securityGroup(policy))
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", subscriberPort))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(rolloverWriterGroup("grp", GROUP_ID, ushort(1), dataSet))
                .build())
        .connection(
            PubSubConnectionConfig.udp("pub-conn-witness")
                .publisherId(PublisherId.uint16(ushort(4713)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", witnessPort))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(rolloverWriterGroup("grp-witness", ushort(2), ushort(2), dataSet))
                .build())
        .build();
  }

  private static WriterGroupConfig rolloverWriterGroup(
      String name, UShort writerGroupId, UShort dataSetWriterId, PublishedDataSetConfig dataSet) {

    return WriterGroupConfig.builder(name)
        .writerGroupId(writerGroupId)
        .publishingInterval(PUBLISHING_INTERVAL)
        .messageSettings(GROUP_SETTINGS)
        .messageSecurity(messageSecurity(MessageSecurityMode.SignAndEncrypt))
        .dataSetWriter(
            DataSetWriterConfig.builder("writer")
                .dataSet(dataSet.ref())
                .dataSetWriterId(dataSetWriterId)
                .settings(WRITER_SETTINGS)
                .build())
        .build();
  }

  /**
   * Parse the plaintext SecurityTokenId out of one witness datagram, or {@code null} if the bytes
   * are not the expected {@link #GROUP_SETTINGS} secured layout: UADPFlags {@code 0xF1} |
   * ExtendedFlags1 {@code 0x11} (PublisherId UInt16 + SecurityHeader) | PublisherId(2) | GroupFlags
   * {@code 0x09} | WriterGroupId(2) | SequenceNumber(2) | PayloadHeader count(1) + id(2) |
   * SecurityFlags(1) | SecurityTokenId(4, LE).
   */
  private static @Nullable Long witnessTokenId(byte[] datagram) {
    if (datagram.length < 17) {
      return null;
    }
    if ((datagram[0] & 0xFF) != 0xF1
        || (datagram[1] & 0xFF) != 0x11
        || (datagram[4] & 0xFF) != 0x09
        || (datagram[12] & 0x01) == 0) {
      return null;
    }
    return (datagram[13] & 0xFFL)
        | (datagram[14] & 0xFFL) << 8
        | (datagram[15] & 0xFFL) << 16
        | (datagram[16] & 0xFFL) << 24;
  }

  /**
   * A scripted rotating "SKS": the current token id and TimeToNextKey are pure functions of the
   * shared rotation epoch — token 1 until {@code timeToFirstSwitch}, then one switch per {@code
   * keyLifetime} — and key data is a pure function of (policy, token id), so independent provider
   * instances handed to the publisher and the subscriber always agree.
   */
  private static final class RotatingKeyProvider implements SecurityKeyProvider {

    private final PubSubSecurityPolicy policy;
    private final long epochNanos;
    private final long timeToFirstSwitchNanos;
    private final long keyLifetimeNanos;

    RotatingKeyProvider(
        PubSubSecurityPolicy policy,
        long epochNanos,
        Duration timeToFirstSwitch,
        Duration keyLifetime) {

      this.policy = policy;
      this.epochNanos = epochNanos;
      this.timeToFirstSwitchNanos = timeToFirstSwitch.toNanos();
      this.keyLifetimeNanos = keyLifetime.toNanos();
    }

    @Override
    public CompletableFuture<SecurityKeySet> getKeys(
        String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount) {

      long elapsed = System.nanoTime() - epochNanos;

      long currentToken;
      long timeToNextNanos;
      if (elapsed < timeToFirstSwitchNanos) {
        currentToken = 1;
        timeToNextNanos = timeToFirstSwitchNanos - elapsed;
      } else {
        long sinceFirstSwitch = elapsed - timeToFirstSwitchNanos;
        currentToken = 2 + sinceFirstSwitch / keyLifetimeNanos;
        timeToNextNanos = keyLifetimeNanos - (sinceFirstSwitch % keyLifetimeNanos);
      }

      // current + a generous future tail so publisher/subscriber skew stays inside the window
      int count = Math.max(1, requestedKeyCount.intValue()) + 2;
      var keys = new ArrayList<ByteString>(count);
      for (long token = currentToken; token < currentToken + count; token++) {
        keys.add(keyDataForToken(token));
      }

      return CompletableFuture.completedFuture(
          new SecurityKeySet(
              policy.getUri(),
              uint(currentToken),
              keys,
              Duration.ofNanos(Math.max(1L, timeToNextNanos)),
              Duration.ofNanos(keyLifetimeNanos)));
    }

    /** Deterministic per-token key data, identical across provider instances. */
    private ByteString keyDataForToken(long token) {
      byte[] bytes = new byte[policy.getKeyDataLength()];
      for (int i = 0; i < bytes.length; i++) {
        bytes[i] = (byte) (31 * token + i + 7);
      }
      return ByteString.of(bytes);
    }
  }

  // endregion

  // region helpers

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  /**
   * Pick a currently free UDP port by binding and closing an ephemeral socket. The small race
   * between closing and re-binding is accepted.
   */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  /** A source that reads the current values of an AtomicReference-backed map by field key. */
  private static PublishedDataSetSource mapSource(AtomicReference<Map<String, DataValue>> values) {
    return context -> {
      DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
      Map<String, DataValue> currentValues = values.get();
      for (FieldDefinition field : context.fields()) {
        String key =
            field.getSource() instanceof KeyFieldAddress keyAddress
                ? keyAddress.key()
                : field.getName();
        DataValue value = currentValues.get(key);
        if (value != null) {
          builder.field(field.getName(), value);
        }
      }
      return builder.build();
    };
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

  /** Every security-drop counter — and {@code decodeErrors} — is zero at {@code groupPath}. */
  private static void assertZeroSecurityDrops(PubSubService subscriber, String groupPath) {
    assertEquals(
        0,
        counter(
            subscriber,
            groupPath,
            PubSubDiagnostics.ComponentDiagnostics::invalidSignatureMessages));
    assertEquals(
        0,
        counter(subscriber, groupPath, PubSubDiagnostics.ComponentDiagnostics::decryptionErrors));
    assertEquals(
        0,
        counter(
            subscriber, groupPath, PubSubDiagnostics.ComponentDiagnostics::unknownTokenMessages));
    assertEquals(
        0,
        counter(subscriber, groupPath, PubSubDiagnostics.ComponentDiagnostics::staleKeyMessages));
    assertEquals(
        0, counter(subscriber, groupPath, PubSubDiagnostics.ComponentDiagnostics::decodeErrors));
  }

  private static UaException assertStartupFailsExceptionally(PubSubService service) {
    ExecutionException e =
        assertThrows(
            ExecutionException.class,
            () -> service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS));

    Throwable cause = e.getCause();
    while (cause != null && !(cause instanceof UaException)) {
      cause = cause.getCause();
    }
    assertNotNull(cause, "expected a UaException cause, got: " + e);
    return (UaException) cause;
  }

  // endregion
}
