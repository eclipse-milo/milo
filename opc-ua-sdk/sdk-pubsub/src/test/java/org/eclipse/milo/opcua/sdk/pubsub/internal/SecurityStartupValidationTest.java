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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
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
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.StaticSecurityKeyProvider;
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * The K3 message security gate at startup, reconfigure, and activation: a secured mode on a
 * JSON-mapped group is rejected with {@code Bad_ConfigurationError} pointing at transport security
 * ({@code BrokerSecurityConfig}) — JSON NetworkMessages have no message security in OPC UA 1.05
 * (Part 14 §7.3.4.1) — and a secured UADP group requires a resolvable SecurityGroup reference, a
 * supported PubSub security policy, and a bound {@link SecurityKeyProvider}, else {@code
 * Bad_ConfigurationError} naming the missing piece. Group-level mode {@code Invalid} is treated
 * like None (D1). A fully bound secured group is accepted and — with a static key provider —
 * completes startup into {@code Operational}.
 */
class SecurityStartupValidationTest {

  private static final SecurityGroupRef SG_REF = new SecurityGroupRef("SG");

  private @Nullable PubSubService service;

  @AfterEach
  void shutdownService() throws Exception {
    if (service != null) {
      service.close();
      service = null;
    }
  }

  // region fixtures

  /** Discards every send; never touches the network. */
  private static final class DiscardingTransport implements TransportProvider {

    @Override
    public String transportProfileUri() {
      return "urn:eclipse:milo:test:discarding-transport";
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
          return send(message, null);
        }

        @Override
        public CompletableFuture<Void> send(ByteBuf message, @Nullable MessageAddress address) {
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

  private static SecurityGroupConfig securityGroup(@Nullable String securityPolicyUri) {
    SecurityGroupConfig.Builder builder =
        SecurityGroupConfig.builder("SG").keyLifeTime(Duration.ofSeconds(60));
    if (securityPolicyUri != null) {
      builder.securityPolicyUri(securityPolicyUri);
    }
    return builder.build();
  }

  private static MessageSecurityConfig security(
      MessageSecurityMode mode, @Nullable SecurityGroupRef ref) {

    MessageSecurityConfig.Builder builder = MessageSecurityConfig.builder().mode(mode);
    if (ref != null) {
      builder.securityGroup(ref);
    }
    return builder.build();
  }

  private static PublishedDataSetConfig dataSet() {
    return PublishedDataSetConfig.builder("PDS")
        .field(FieldDefinition.builder("counter").dataType(NodeIds.Int32).build())
        .build();
  }

  /** One MQTT connection with one UADP writer group carrying {@code messageSecurity}. */
  private static PubSubConfig uadpPublisherConfig(
      MessageSecurityConfig messageSecurity,
      @Nullable SecurityGroupConfig securityGroup,
      boolean groupEnabled) {

    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.string("line-7"))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .enabled(groupEnabled)
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(25))
                    .messageSettings(UadpWriterGroupSettings.builder().build())
                    .messageSecurity(messageSecurity)
                    .dataSetWriter(
                        DataSetWriterConfig.builder("W1")
                            .dataSet(new PublishedDataSetRef("PDS"))
                            .dataSetWriterId(ushort(1))
                            .settings(UadpDataSetWriterSettings.builder().build())
                            .build())
                    .build())
            .build();

    PubSubConfig.Builder builder = PubSubConfig.builder().publishedDataSet(dataSet());
    if (securityGroup != null) {
      builder.securityGroup(securityGroup);
    }
    return builder.connection(connection).build();
  }

  /** The same shape, JSON-mapped. */
  private static PubSubConfig jsonPublisherConfig(MessageSecurityConfig messageSecurity) {
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.string("line-7"))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(25))
                    .messageSettings(JsonWriterGroupSettings.builder().build())
                    .messageSecurity(messageSecurity)
                    .dataSetWriter(
                        DataSetWriterConfig.builder("W1")
                            .dataSet(new PublishedDataSetRef("PDS"))
                            .dataSetWriterId(ushort(1))
                            .settings(JsonDataSetWriterSettings.builder().build())
                            .build())
                    .build())
            .build();

    return PubSubConfig.builder()
        .publishedDataSet(dataSet())
        .securityGroup(securityGroup(null))
        .connection(connection)
        .build();
  }

  /** One MQTT connection with one secured reader group whose single reader is JSON-mapped. */
  private static PubSubConfig jsonReaderGroupConfig(MessageSecurityConfig messageSecurity) {
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .readerGroup(
                ReaderGroupConfig.builder("RG")
                    .messageSecurity(messageSecurity)
                    .dataSetReader(
                        DataSetReaderConfig.builder("R1")
                            .publisherId(PublisherId.string("line-7"))
                            .dataSetWriterId(ushort(1))
                            .settings(JsonDataSetReaderSettings.builder().build())
                            .brokerTransport(
                                BrokerTransportSettings.builder().queueName("data/queue").build())
                            .build())
                    .build())
            .build();

    return PubSubConfig.builder().securityGroup(securityGroup(null)).connection(connection).build();
  }

  private static PubSubBindings sourceBindings() {
    PublishedDataSetSource source =
        context ->
            DataSetSnapshot.builder(context)
                .field("counter", new DataValue(Variant.ofInt32(7)))
                .build();

    return PubSubBindings.builder().source(new PublishedDataSetRef("PDS"), source).build();
  }

  private PubSubService createService(PubSubConfig config, PubSubBindings bindings) {
    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder().transportProvider(new DiscardingTransport()).build();

    service = PubSubService.create(config, bindings, serviceConfig);
    return service;
  }

  private UaException assertStartupFailsWithConfigurationError(
      PubSubConfig config, PubSubBindings bindings, String... fragments) {

    createService(config, bindings);

    ExecutionException e =
        assertThrows(ExecutionException.class, () -> service.startup().get(10, TimeUnit.SECONDS));
    UaException cause = assertInstanceOf(UaException.class, e.getCause());
    assertEquals(StatusCodes.Bad_ConfigurationError, cause.getStatusCode().value());
    for (String fragment : fragments) {
      assertTrue(
          String.valueOf(cause.getMessage()).contains(fragment),
          "unexpected message: " + cause.getMessage());
    }
    return cause;
  }

  private static void awaitTrue(String description, BooleanSupplier condition) throws Exception {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while (System.nanoTime() < deadline) {
      if (condition.getAsBoolean()) {
        return;
      }
      Thread.sleep(10);
    }
    fail("timed out waiting for " + description);
  }

  private static SecurityKeyProvider staticKeyProvider() {
    byte[] keyData = new byte[52]; // PubSub-Aes128-CTR key data length
    for (int i = 0; i < keyData.length; i++) {
      keyData[i] = (byte) i;
    }
    try {
      return StaticSecurityKeyProvider.of(
          PubSubSecurityPolicy.PubSubAes128Ctr, ByteString.of(keyData));
    } catch (UaException e) {
      throw new AssertionError(e);
    }
  }

  // endregion

  @Test
  void jsonGroupWithSignFailsStartupPointingAtBrokerSecurityConfig() {
    assertStartupFailsWithConfigurationError(
        jsonPublisherConfig(security(MessageSecurityMode.Sign, SG_REF)),
        sourceBindings(),
        "BrokerSecurityConfig",
        "conn/WG");
  }

  @Test
  void securedReaderGroupWithJsonReaderFailsStartup() {
    assertStartupFailsWithConfigurationError(
        jsonReaderGroupConfig(security(MessageSecurityMode.SignAndEncrypt, SG_REF)),
        PubSubBindings.builder().securityKeys(SG_REF, staticKeyProvider()).build(),
        "BrokerSecurityConfig",
        "conn/RG");
  }

  @Test
  void uadpSignWithoutSecurityGroupRefFailsStartup() {
    assertStartupFailsWithConfigurationError(
        uadpPublisherConfig(security(MessageSecurityMode.Sign, null), null, true),
        sourceBindings(),
        "requires a SecurityGroup",
        "conn/WG");
  }

  @Test
  void uadpSignWithoutBoundProviderFailsStartup() {
    assertStartupFailsWithConfigurationError(
        uadpPublisherConfig(security(MessageSecurityMode.Sign, SG_REF), securityGroup(null), true),
        sourceBindings(),
        "no SecurityKeyProvider",
        "SG",
        "conn/WG");
  }

  @Test
  void uadpSignWithUnsupportedPolicyUriFailsStartup() {
    PubSubBindings bindings =
        sourceBindings().toBuilder().securityKeys(SG_REF, staticKeyProvider()).build();

    assertStartupFailsWithConfigurationError(
        uadpPublisherConfig(
            security(MessageSecurityMode.Sign, SG_REF),
            securityGroup("http://opcfoundation.org/UA/SecurityPolicy#Basic256Sha256"),
            true),
        bindings,
        "not a supported PubSub SecurityPolicy",
        "conn/WG");
  }

  @Test
  void groupLevelInvalidModeIsTreatedLikeNone() throws Exception {
    // Invalid mode with no SecurityGroup at all: passes the gate (D1) and starts
    PubSubService service =
        createService(
            uadpPublisherConfig(security(MessageSecurityMode.Invalid, null), null, true),
            sourceBindings());

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    awaitTrue("writer group Operational", () -> service.state(group) == PubSubState.Operational);
  }

  @Test
  void fullyBoundSecuredGroupStartsAndCompletesStartupOnStaticKeys() throws Exception {
    PubSubBindings bindings =
        sourceBindings().toBuilder().securityKeys(SG_REF, staticKeyProvider()).build();

    PubSubService service =
        createService(
            uadpPublisherConfig(
                security(MessageSecurityMode.SignAndEncrypt, SG_REF), securityGroup(null), true),
            bindings);

    service.startup().get(10, TimeUnit.SECONDS);

    // the group defers startup completion to the first key fetch; the static provider completes
    // immediately, so it reaches Operational
    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    awaitTrue("writer group Operational", () -> service.state(group) == PubSubState.Operational);
  }

  @Test
  void reconfigureRejectsSecuredGroupWithoutProvider() throws Exception {
    PubSubService service =
        createService(
            uadpPublisherConfig(
                security(MessageSecurityMode.None, null), securityGroup(null), true),
            sourceBindings());

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubConfig secured =
        uadpPublisherConfig(security(MessageSecurityMode.Sign, SG_REF), securityGroup(null), true);

    UaRuntimeException e =
        assertThrows(
            UaRuntimeException.class,
            () -> service.reconfigure(secured, PubSubService.ReconfigureMode.DISABLE_AFFECTED));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(
        String.valueOf(e.getMessage()).contains("no SecurityKeyProvider"),
        "unexpected message: " + e.getMessage());
  }

  /**
   * The N19 reconfigure half of the K3 JSON gate: a service running an UNsecured JSON group rejects
   * a reconfiguration INTO a secured mode on that group with {@code Bad_ConfigurationError} naming
   * BrokerSecurityConfig — even with a key provider bound, because JSON NetworkMessages have no
   * message security in OPC UA 1.05 (Part 14 §7.3.4.1).
   */
  @Test
  void reconfigureIntoSecuredJsonGroupFailsPointingAtBrokerSecurityConfig() throws Exception {
    PubSubBindings bindings =
        sourceBindings().toBuilder().securityKeys(SG_REF, staticKeyProvider()).build();

    PubSubService service =
        createService(jsonPublisherConfig(security(MessageSecurityMode.None, null)), bindings);

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubConfig secured = jsonPublisherConfig(security(MessageSecurityMode.Sign, SG_REF));

    UaRuntimeException e =
        assertThrows(
            UaRuntimeException.class,
            () -> service.reconfigure(secured, PubSubService.ReconfigureMode.DISABLE_AFFECTED));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(
        String.valueOf(e.getMessage()).contains("BrokerSecurityConfig"),
        "unexpected message: " + e.getMessage());
    assertTrue(
        String.valueOf(e.getMessage()).contains("conn/WG"),
        "unexpected message: " + e.getMessage());
  }

  @Test
  void securedGroupEnabledAfterStartupFailsActivationIntoError() throws Exception {
    // disabled at startup (tolerated: round-trip posture), enabled later without a provider:
    // the activation-time gate copy fails the group into Error with Bad_ConfigurationError
    PubSubService service =
        createService(
            uadpPublisherConfig(
                security(MessageSecurityMode.Sign, SG_REF), securityGroup(null), false),
            sourceBindings());

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.Disabled, service.state(group));

    service.enable(group);

    awaitTrue("writer group Error", () -> service.state(group) == PubSubState.Error);

    var diagnostics = service.diagnostics().component("conn/WG").orElseThrow();
    assertTrue(diagnostics.lastError() != null, "expected a lastError");
    assertEquals(StatusCodes.Bad_ConfigurationError, diagnostics.lastError().value());
  }
}
