/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.sks;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
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
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
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
import org.eclipse.milo.opcua.sdk.pubsub.security.InMemoryKeyCredentialStore;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Rung-2 integration test: SKS-distributed keys end to end. A {@link SksTestServer} hosts the real
 * WP-V {@code GetSecurityKeys} face; {@link SksSecurityKeyProvider}s pull from it over real
 * SignAndEncrypt sessions; two {@link PubSubService}s on loopback UDP secure a SignAndEncrypt
 * WriterGroup/ReaderGroup pair with the pulled keys.
 *
 * <p>Covered rows (WP-T4 §4.2 tier 2 + the K17.1-dependent real-session authorization rows the
 * direct-invoke matrix in sdk-pubsub-server's {@code SksServerFaceTest} cannot reach):
 *
 * <ul>
 *   <li>happy path: end-to-end dataset delivery, reader Operational, zero security-drop counters;
 *   <li>rotation continuity: the server group's SHORT real KeyLifetime (the face's {@code
 *       InstantSource} seam is not reachable through {@code ServerPubSub}, so short-lifetime +
 *       bounded awaits it is — no raw sleeps as logic) rotates tokens while traffic flows; both
 *       providers observe a rotated FirstTokenId and delivery continues without any unknown-token
 *       or stale-key drops;
 *   <li>{@code GetSecurityKeys} over a None or Sign channel: {@code Bad_SecurityModeInsufficient}
 *       surfaces to the CLIENT's {@link CallMethodResult} (the ns0 loader ships
 *       AccessRestrictions(3) on i=15215; K17.1 propagates the real denial status);
 *   <li>unauthorized identity over SignAndEncrypt: {@code Bad_UserAccessDenied} (RoleMapper
 *       configured, caller lacks the well-known pull roles); authorized identity: {@code Good} plus
 *       the five Part 14 §8.3.2 outputs;
 *   <li>provider failover against the real face: first entry unreachable, second serves.
 * </ul>
 *
 * <p>Port and lifecycle hygiene, async-assertion helpers, and the loopback fixture shape are cloned
 * from sdk-pubsub's {@code SecuredUdpLoopbackIntegrationTest} (itself from {@code
 * UdpLoopbackIntegrationTest}): ephemeral bind-probe ports, explicit loopback {@code
 * discoveryAddress} on every UDP connection, tracked shutdowns, deadline-polling awaits.
 *
 * <p><b>Accepted real-clock exposure (the wave's only one):</b> because the server face rotates on
 * {@code InstantSource.system()}, the rotation-continuity zero-drop assertions depend on the
 * subscriber's per-second full-session refreshes keeping within the token window (~1 past + 3
 * future keys ≈ 4 × {@link #SERVER_KEY_LIFETIME} ≈ 8 s of headroom) of the wall clock. A single
 * global stall (GC pause) self-heals — the publisher can only send tokens both sides already hold —
 * so a failure needs sustained asymmetric CPU starvation of the subscriber. If this ever flakes on
 * CI, the fix is to expose the {@code SecurityGroupKeyStore} {@code InstantSource} seam through
 * {@code ServerPubSubOptions} and drive rotation deterministically — do NOT loosen the zero-drop
 * assertions.
 */
class SksSecuredLoopbackIntegrationTest {

  /** Generous: every SKS fetch is a real GetEndpoints + SignAndEncrypt session + Call. */
  private static final Duration TIMEOUT = Duration.ofSeconds(30);

  private static final Duration PUBLISHING_INTERVAL = Duration.ofMillis(50);

  /**
   * The server group's KeyLifetime: short enough that a token switch happens while the loopback
   * runs, long enough that the providers' 1×/KeyLifetime÷2 refresh sessions keep up on slow CI.
   */
  private static final Duration SERVER_KEY_LIFETIME = Duration.ofSeconds(2);

  private static final String CLIENT_APP_URI = "urn:eclipse:milo:test:sks-rung2-client";

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4713));
  private static final UShort GROUP_ID = ushort(1);

  private static final UUID TEMPERATURE_FIELD_ID = new UUID(0L, 0xE1L);
  private static final UUID STATUS_FIELD_ID = new UUID(0L, 0xE2L);

  private static final PublishedDataSetRef DATA_SET_REF = new PublishedDataSetRef("ds-sks");

  private static final String SECURITY_GROUP = "sks-rung2-group";
  private static final SecurityGroupRef SECURITY_GROUP_REF = new SecurityGroupRef(SECURITY_GROUP);

  private static final PubSubSecurityPolicy POLICY = PubSubSecurityPolicy.PubSubAes256Ctr;

  private static final DataSetReaderRef READER_REF =
      new DataSetReaderRef("sub-conn", "rgrp", "reader");

  /** GroupHeader + WriterGroupId on the wire, matching the SecuredUdpLoopback fixture. */
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

  private static SksTestServer sks;
  private static KeyPair clientKeyPair;
  private static X509Certificate clientCertificate;
  private static MemoryTrustListManager clientTrustList;

  private final List<PubSubService> services = new CopyOnWriteArrayList<>();
  private final List<SksSecurityKeyProvider> providers = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void startSksServer() throws Exception {
    clientKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    clientCertificate =
        new SelfSignedCertificateBuilder(clientKeyPair)
            .setCommonName("SKS Rung-2 Client")
            .setApplicationUri(CLIENT_APP_URI)
            .build();

    sks = SksTestServer.create(freeTcpPort(), clientCertificate, serverPubSubConfig());

    clientTrustList = new MemoryTrustListManager();
    clientTrustList.addTrustedCertificate(sks.getCertificate());
  }

  @AfterAll
  static void stopSksServer() throws Exception {
    if (sks != null) {
      sks.close();
    }
  }

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

    providers.forEach(SksSecurityKeyProvider::close);
    providers.clear();
  }

  /**
   * The rung-2 happy path plus rotation continuity, one traffic run:
   *
   * <ol>
   *   <li>both services pull keys for {@link #SECURITY_GROUP} from the real face over real
   *       SignAndEncrypt sessions (the face itself rejects anything less), secure the loopback UDP
   *       traffic with them, and deliver the dataset end to end — reader Operational, zero
   *       security-drop counters on the subscriber, zero encryptionErrors on the publisher;
   *   <li>the server group's {@link #SERVER_KEY_LIFETIME} rotation arithmetic advances the token
   *       while traffic flows; both providers observe a FirstTokenId at least one beyond their
   *       first fetch (the manager refreshes every KeyLifetime/2), a value published after the
   *       observed rotation still arrives, the publisher kept sending, and the security-drop
   *       counters are still zero — continuity across at least one token switch. (The publisher's
   *       wire-token switch mechanics are pinned by the SecurityKeyManager unit suite; this test
   *       pins the end-to-end effect over the real SKS.)
   * </ol>
   */
  @Test
  void sksDistributedKeysSecureTheLoopbackAcrossRotation() throws Exception {
    int port = freeUdpPort();

    var values =
        new AtomicReference<>(
            Map.of(
                "temperature", new DataValue(Variant.ofDouble(21.5)),
                "status", new DataValue(Variant.ofString("running"))));

    var subProvider = new RecordingSecurityKeyProvider(newSksProvider());
    var pubProvider = new RecordingSecurityKeyProvider(newSksProvider());

    var events = new LinkedBlockingQueue<DataSetReceivedEvent>();

    PubSubService subscriber =
        track(
            PubSubService.create(
                subscriberConfig(port),
                PubSubBindings.builder()
                    .listener(READER_REF, events::add)
                    .securityKeys(SECURITY_GROUP_REF, subProvider)
                    .build()));

    subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // hold the publisher back until the subscriber's first SKS fetch has landed, so no secured
    // datagram ever races a keyless reader group (the zero-drop assertions below stay exact)
    awaitTrue(() -> subProvider.maxObservedTokenId() > 0, "subscriber SKS key fetch");

    PubSubService publisher =
        track(
            PubSubService.create(
                publisherConfig(port, freeUdpPort()),
                PubSubBindings.builder()
                    .source(DATA_SET_REF, mapSource(values))
                    .securityKeys(SECURITY_GROUP_REF, pubProvider)
                    .build()));

    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    // 1: end-to-end delivery of SKS-secured traffic
    DataSetReceivedEvent event = awaitEvent(events, e -> true);
    assertEquals(PUBLISHER_ID, event.publisherId());
    assertEquals(GROUP_ID, event.writerGroupId());
    assertEquals(ushort(1), event.dataSetWriterId());
    assertEquals("ds-sks", event.dataSetName());
    assertEquals(21.5, event.fieldsByName().get("temperature").value().value());
    assertEquals("running", event.fieldsByName().get("status").value().value());

    PubSubHandle reader =
        subscriber.components().dataSetReader("sub-conn", "rgrp", "reader").orElseThrow();
    assertEquals(PubSubState.Operational, subscriber.state(reader));

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
    assertZeroSecurityDrops(subscriber, "sub-conn/rgrp");

    // 2: rotation continuity across at least one token switch
    long pubFirstToken = pubProvider.firstObservedTokenId();
    long subFirstToken = subProvider.firstObservedTokenId();
    long sentBeforeRotation =
        counter(
            publisher, "pub-conn/grp", PubSubDiagnostics.ComponentDiagnostics::networkMessagesSent);

    awaitTrue(
        () ->
            pubProvider.maxObservedTokenId() > pubFirstToken
                && subProvider.maxObservedTokenId() > subFirstToken,
        "both providers observing a rotated FirstTokenId from the SKS");

    values.set(
        Map.of(
            "temperature", new DataValue(Variant.ofDouble(42.25)),
            "status", new DataValue(Variant.ofString("rotated"))));
    awaitEvent(
        events,
        e -> Double.valueOf(42.25).equals(e.fieldsByName().get("temperature").value().value()));

    assertTrue(
        counter(
                publisher,
                "pub-conn/grp",
                PubSubDiagnostics.ComponentDiagnostics::networkMessagesSent)
            > sentBeforeRotation,
        "publisher kept sending across the token switch");
    assertEquals(PubSubState.Operational, subscriber.state(reader));
    assertEquals(
        0,
        counter(
            publisher, "pub-conn/grp", PubSubDiagnostics.ComponentDiagnostics::encryptionErrors));
    assertZeroSecurityDrops(subscriber, "sub-conn/rgrp");
  }

  /**
   * The K17.1-dependent rows (WP-V §8 A1/A2): over a None or Sign channel the access controller
   * denies the Call via i=15215's AccessRestrictions(3) before the handler runs, and the REAL
   * denial status — {@code Bad_SecurityModeInsufficient}, not the pre-K17.1 {@code
   * Bad_UserAccessDenied} collapse — reaches the client's {@link CallMethodResult}.
   */
  @Test
  void getSecurityKeysOverInsufficientChannelSurfacesToTheClient() throws Exception {
    for (MessageSecurityMode mode : List.of(MessageSecurityMode.None, MessageSecurityMode.Sign)) {
      OpcUaClient client = connectClient(mode, AnonymousProvider.INSTANCE);
      try {
        CallMethodResult result = callGetSecurityKeys(client, SECURITY_GROUP);

        assertEquals(
            new StatusCode(StatusCodes.Bad_SecurityModeInsufficient),
            result.getStatusCode(),
            "channel mode " + mode);
      } finally {
        client.disconnect();
      }
    }
  }

  /**
   * Real-session role rows over SignAndEncrypt with the harness RoleMapper configured: a username
   * mapped to no roles and an anonymous caller (mapper configured, empty roles) are both denied
   * with {@code Bad_UserAccessDenied} by the default posture.
   */
  @Test
  void unauthorizedIdentityOverSignAndEncryptIsDenied() throws Exception {
    IdentityProvider unauthorizedUser =
        new UsernameProvider(
            SksTestServer.UNAUTHORIZED_USERNAME, SksTestServer.UNAUTHORIZED_PASSWORD);

    for (IdentityProvider identity : List.of(unauthorizedUser, AnonymousProvider.INSTANCE)) {
      OpcUaClient client = connectClient(MessageSecurityMode.SignAndEncrypt, identity);
      try {
        CallMethodResult result = callGetSecurityKeys(client, SECURITY_GROUP);

        assertEquals(
            new StatusCode(StatusCodes.Bad_UserAccessDenied),
            result.getStatusCode(),
            "identity " + identity);
      } finally {
        client.disconnect();
      }
    }
  }

  /**
   * The authorized row: a username mapped to {@code SecurityKeyServerAccess} over SignAndEncrypt
   * gets {@code Good} and the five Part 14 §8.3.2 outputs, served by the real key store with the
   * group's configured policy and lifetime.
   */
  @Test
  void authorizedIdentityOverSignAndEncryptIsServedKeys() throws Exception {
    IdentityProvider authorizedUser =
        new UsernameProvider(SksTestServer.AUTHORIZED_USERNAME, SksTestServer.AUTHORIZED_PASSWORD);

    OpcUaClient client = connectClient(MessageSecurityMode.SignAndEncrypt, authorizedUser);
    try {
      CallMethodResult result = callGetSecurityKeys(client, SECURITY_GROUP);

      assertEquals(StatusCode.GOOD, result.getStatusCode());

      SecurityKeySet keySet = GetSecurityKeysResponse.parse(result);
      assertEquals(POLICY.getUri(), keySet.securityPolicyUri());
      assertTrue(keySet.firstTokenId().longValue() >= 1);
      assertEquals(1, keySet.keys().size());
      assertEquals(POLICY.getKeyDataLength(), keySet.keys().get(0).length());
      assertEquals(SERVER_KEY_LIFETIME, keySet.keyLifetime());
      assertTrue(keySet.timeToNextKey().compareTo(SERVER_KEY_LIFETIME) <= 0);
    } finally {
      client.disconnect();
    }
  }

  /**
   * Failover against the real face: the first SecurityKeyServices entry points at a dead port, the
   * second at the real SKS; the fetch fails over in array order and the real face serves the keys.
   */
  @Test
  void providerFailsOverPastUnreachableEntryToTheRealFace() throws Exception {
    String unreachableUrl = "opc.tcp://localhost:" + freeTcpPort() + "/sks";

    List<EndpointDescription> entries =
        List.of(serverEntry(unreachableUrl), serverEntry(sks.getDiscoveryUrl()));

    SksSecurityKeyProvider provider = trackProvider(providerBuilder().keyServices(entries).build());

    SecurityKeySet keySet =
        provider.getKeys(SECURITY_GROUP, uint(0), uint(1)).get(60, TimeUnit.SECONDS);

    assertEquals(POLICY.getUri(), keySet.securityPolicyUri());
    assertTrue(keySet.firstTokenId().longValue() >= 1);
    assertFalse(keySet.keys().isEmpty());
    for (ByteString key : keySet.keys()) {
      assertEquals(POLICY.getKeyDataLength(), key.length());
    }
  }

  // region fixtures

  /** The server-side config: the SecurityGroup the real face generates and rotates keys for. */
  private static PubSubConfig serverPubSubConfig() {
    return PubSubConfig.builder()
        .securityGroup(
            SecurityGroupConfig.builder(SECURITY_GROUP)
                .securityPolicyUri(POLICY.getUri())
                .keyLifeTime(SERVER_KEY_LIFETIME)
                .maxFutureKeyCount(uint(3))
                .maxPastKeyCount(uint(1))
                .build())
        .build();
  }

  /**
   * The client-side SecurityGroup for both loopback services. The keyLifeTime here only bounds the
   * key manager's 2×KeyLifetime first-fetch deadline (the real cadence is re-learned from every SKS
   * response), so it is set generously; the window counts size the manager's requested and retained
   * keys.
   */
  private static SecurityGroupConfig clientSecurityGroup() {
    return SecurityGroupConfig.builder(SECURITY_GROUP)
        .securityPolicyUri(POLICY.getUri())
        .keyLifeTime(Duration.ofSeconds(10))
        .maxFutureKeyCount(uint(3))
        .maxPastKeyCount(uint(1))
        .build();
  }

  /**
   * Publisher config: one UDP connection sending to 127.0.0.1:{@code port}, one SignAndEncrypt
   * writer group with a single writer on the two-field dataset.
   */
  private static PubSubConfig publisherConfig(int port, int discoveryPort) {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("ds-sks")
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

    WriterGroupConfig writerGroup =
        WriterGroupConfig.builder("grp")
            .writerGroupId(GROUP_ID)
            .publishingInterval(PUBLISHING_INTERVAL)
            .messageSettings(GROUP_SETTINGS)
            .messageSecurity(messageSecurity())
            .dataSetWriter(
                DataSetWriterConfig.builder("writer")
                    .dataSet(dataSet.ref())
                    .dataSetWriterId(ushort(1))
                    .settings(WRITER_SETTINGS)
                    .build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .securityGroup(clientSecurityGroup())
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
   * Subscriber config matching {@link #publisherConfig}: one SignAndEncrypt reader group with one
   * REQUIRE_CONFIGURED reader, bound to 127.0.0.1:{@code port}.
   */
  private static PubSubConfig subscriberConfig(int port) throws SocketException {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("ds-sks")
            .field("temperature", NodeIds.Double, TEMPERATURE_FIELD_ID)
            .field("status", NodeIds.String, STATUS_FIELD_ID)
            .configurationVersion(uint(7), uint(3))
            .build();

    DataSetReaderConfig reader =
        DataSetReaderConfig.builder("reader")
            .publisherId(PUBLISHER_ID)
            .writerGroupId(GROUP_ID)
            .dataSetWriterId(ushort(1))
            .dataSetMetaData(metaData)
            .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
            .build();

    ReaderGroupConfig readerGroup =
        ReaderGroupConfig.builder("rgrp")
            .messageSecurity(messageSecurity())
            .dataSetReader(reader)
            .build();

    return PubSubConfig.builder()
        .securityGroup(clientSecurityGroup())
        .connection(
            PubSubConnectionConfig.udp("sub-conn")
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(readerGroup)
                .build())
        .build();
  }

  private static MessageSecurityConfig messageSecurity() {
    return MessageSecurityConfig.builder()
        .mode(MessageSecurityMode.SignAndEncrypt)
        .securityGroup(SECURITY_GROUP_REF)
        .build();
  }

  /** A pull provider resolving the real SKS, authenticating as the authorized username. */
  private SksSecurityKeyProvider newSksProvider() throws UaException {
    return trackProvider(
        providerBuilder().keyServices(List.of(serverEntry(sks.getDiscoveryUrl()))).build());
  }

  private static SksSecurityKeyProvider.Builder providerBuilder() {
    var credentialStore = new InMemoryKeyCredentialStore();
    credentialStore.put(
        SksTestServer.APPLICATION_URI,
        SksTestServer.AUTHORIZED_USERNAME,
        SksTestServer.AUTHORIZED_PASSWORD.toCharArray());

    return SksSecurityKeyProvider.builder()
        .keyPair(clientKeyPair)
        .certificate(clientCertificate)
        .applicationUri(CLIENT_APP_URI)
        .trustListManager(clientTrustList)
        .keyCredentialStore(credentialStore)
        .requestTimeout(Duration.ofSeconds(15));
  }

  /** A Table 40 identity record for the SKS reachable at {@code discoveryUrl}. */
  private static EndpointDescription serverEntry(String discoveryUrl) {
    return new EndpointDescription(
        null,
        new ApplicationDescription(
            SksTestServer.APPLICATION_URI,
            null,
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            new String[] {discoveryUrl}),
        ByteString.NULL_VALUE,
        MessageSecurityMode.SignAndEncrypt,
        null,
        new UserTokenPolicy[] {
          new UserTokenPolicy("username", UserTokenType.UserName, null, null, null)
        },
        null,
        ubyte(0));
  }

  // endregion

  // region helpers

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  private SksSecurityKeyProvider trackProvider(SksSecurityKeyProvider provider) {
    providers.add(provider);
    return provider;
  }

  /** Connect a raw client to the endpoint with {@code securityMode}, using {@code identity}. */
  private static OpcUaClient connectClient(
      MessageSecurityMode securityMode, IdentityProvider identity) throws Exception {

    List<EndpointDescription> endpoints =
        DiscoveryClient.getEndpoints(sks.getDiscoveryUrl())
            .get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    EndpointDescription endpoint =
        endpoints.stream()
            .filter(e -> e.getSecurityMode() == securityMode)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("no endpoint with mode " + securityMode));

    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setApplicationName(LocalizedText.english("SKS rung-2 test client"))
            .setApplicationUri(CLIENT_APP_URI)
            .setKeyPair(clientKeyPair)
            .setCertificate(clientCertificate)
            .setCertificateChain(new X509Certificate[] {clientCertificate})
            .setCertificateValidator(
                new DefaultClientCertificateValidator(
                    clientTrustList, new MemoryCertificateQuarantine()))
            .setIdentityProvider(identity)
            .setRequestTimeout(uint(TIMEOUT.toMillis()))
            .build();

    OpcUaClient client = OpcUaClient.create(config);
    try {
      client.connect();
    } catch (Exception e) {
      try {
        client.disconnect();
      } catch (Exception suppressed) {
        e.addSuppressed(suppressed);
      }
      throw e;
    }
    return client;
  }

  /** Call GetSecurityKeys (i=15215 on i=14443) for the current key of {@code securityGroupId}. */
  private static CallMethodResult callGetSecurityKeys(OpcUaClient client, String securityGroupId)
      throws UaException {

    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    NodeIds.PublishSubscribe,
                    NodeIds.PublishSubscribe_GetSecurityKeys,
                    new Variant[] {
                      Variant.ofString(securityGroupId),
                      Variant.ofUInt32(uint(0)),
                      Variant.ofUInt32(uint(0))
                    })));

    CallMethodResult[] results = response.getResults();
    assertNotNull(results);
    assertEquals(1, results.length);
    return results[0];
  }

  /** Pick a currently free TCP port by binding and closing an ephemeral socket. */
  private static int freeTcpPort() throws Exception {
    try (var serverSocket = new ServerSocket(0)) {
      return serverSocket.getLocalPort();
    }
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

  /** Every K6 security-drop counter — and {@code decodeErrors} — is zero at {@code groupPath}. */
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

  /**
   * A delegating {@link SecurityKeyProvider} recording the FirstTokenId of every successful fetch,
   * in completion order — the observable the rotation-continuity assertions poll.
   */
  private static final class RecordingSecurityKeyProvider implements SecurityKeyProvider {

    private final SksSecurityKeyProvider delegate;
    private final CopyOnWriteArrayList<Long> firstTokenIds = new CopyOnWriteArrayList<>();

    private RecordingSecurityKeyProvider(SksSecurityKeyProvider delegate) {
      this.delegate = delegate;
    }

    @Override
    public CompletableFuture<SecurityKeySet> getKeys(
        String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount) {

      return delegate
          .getKeys(securityGroupId, startingTokenId, requestedKeyCount)
          .thenApply(
              keySet -> {
                firstTokenIds.add(keySet.firstTokenId().longValue());
                return keySet;
              });
    }

    /** The FirstTokenId of the first successful fetch, or 0 if none completed yet. */
    long firstObservedTokenId() {
      return firstTokenIds.isEmpty() ? 0L : firstTokenIds.get(0);
    }

    /** The largest FirstTokenId observed so far, or 0 if none completed yet. */
    long maxObservedTokenId() {
      return firstTokenIds.stream().mapToLong(Long::longValue).max().orElse(0L);
    }
  }

  // endregion
}
