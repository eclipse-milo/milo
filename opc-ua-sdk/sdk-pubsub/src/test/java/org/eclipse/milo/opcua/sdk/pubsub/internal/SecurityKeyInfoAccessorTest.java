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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.SecurityKeyInfo;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.StaticSecurityKeyProvider;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Public feed accessors on {@link PubSubService}: {@code securityKeyInfo(handle)} (null while the
 * group holds no key state, token metadata once attached and fetched, null again after detach) and
 * {@code nextDataSetMessageSequenceNumber(handle)} (the NEXT value, advancing with publishes);
 * foreign and wrong-type handles are rejected with {@link IllegalArgumentException}. Runs against a
 * stub transport with a {@link StaticSecurityKeyProvider}; no network.
 */
class SecurityKeyInfoAccessorTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);
  private static final UUID FIELD_ID = new UUID(0L, 1L);

  private static final PubSubSecurityPolicy POLICY = PubSubSecurityPolicy.PubSubAes128Ctr;
  private static final String SECURITY_GROUP = "SG";
  private static final SecurityGroupRef SECURITY_GROUP_REF = new SecurityGroupRef(SECURITY_GROUP);

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
  void securityKeyInfoLifecycle() throws Exception {
    createSecuredPublisher();

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();

    // before startup the secured group holds no key state
    assertNull(service.securityKeyInfo(group));

    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    awaitTrue(
        "secured group Operational after the first key fetch",
        () -> service.state(group) == PubSubState.Operational);

    SecurityKeyInfo info = service.securityKeyInfo(group);
    assertNotNull(info);
    assertEquals(SECURITY_GROUP, info.securityGroupId());
    assertEquals(POLICY.getUri(), info.securityPolicyUri());
    assertTrue(info.securityTokenId().longValue() >= 1, "real token ids start at 1");
    // the static provider pins non-rotating keys: no next key switch
    assertNull(info.timeToNextKey());

    // disabling the group detaches it from the key manager: no key state again
    service.disable(group);
    awaitTrue("group Disabled", () -> service.state(group) == PubSubState.Disabled);
    assertNull(service.securityKeyInfo(group));
  }

  @Test
  void nextDataSetMessageSequenceNumberServesTheNextValueAndAdvances() throws Exception {
    createSecuredPublisher();

    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();

    // never published: the next value is the §7.2.3 starting value 0
    assertEquals(0, service.nextDataSetMessageSequenceNumber(writer).longValue());

    service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    awaitTrue(
        "the next sequence number advances with publishes",
        () -> service.nextDataSetMessageSequenceNumber(writer).longValue() >= 2);
  }

  @Test
  void foreignAndWrongTypeHandlesAreRejected() throws Exception {
    createSecuredPublisher();

    PubSubHandle connection = service.components().connection("conn").orElseThrow();
    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();

    // wrong component types
    assertThrows(IllegalArgumentException.class, () -> service.securityKeyInfo(connection));
    assertThrows(IllegalArgumentException.class, () -> service.securityKeyInfo(writer));
    assertThrows(
        IllegalArgumentException.class, () -> service.nextDataSetMessageSequenceNumber(group));
    assertThrows(
        IllegalArgumentException.class, () -> service.nextDataSetMessageSequenceNumber(connection));

    // a handle this service never issued
    var foreign = new PubSubHandle(ComponentType.WRITER_GROUP, "conn/WG");
    assertThrows(IllegalArgumentException.class, () -> service.securityKeyInfo(foreign));
    assertThrows(
        IllegalArgumentException.class, () -> service.nextDataSetMessageSequenceNumber(foreign));
  }

  // region fixtures

  /** A transport that never touches the network: swallows sends. */
  private static final class StubTransport implements TransportProvider {

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
      return () -> CompletableFuture.completedFuture(null);
    }
  }

  private void createSecuredPublisher() throws Exception {
    transportExecutor = Executors.newSingleThreadExecutor();

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("PDS")
            .field(
                FieldDefinition.builder("F1")
                    .dataType(org.eclipse.milo.opcua.stack.core.NodeIds.Int32)
                    .dataSetFieldId(FIELD_ID)
                    .build())
            .build();

    WriterGroupConfig group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(20))
            .messageSecurity(
                MessageSecurityConfig.builder()
                    .mode(MessageSecurityMode.Sign)
                    .securityGroup(SECURITY_GROUP_REF)
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("W1")
                    .dataSet(dataSet.ref())
                    .dataSetWriterId(ushort(1))
                    .build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .securityGroup(
                SecurityGroupConfig.builder(SECURITY_GROUP)
                    .securityPolicyUri(POLICY.getUri())
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
                .securityKeys(SECURITY_GROUP_REF, StaticSecurityKeyProvider.of(POLICY, keyData()))
                .build(),
            PubSubServiceConfig.builder()
                .transportProvider(new StubTransport())
                .transportExecutor(transportExecutor)
                .build());
  }

  /** Deterministic key data ({@code 01 02 03 ...}, Table 155 layout). */
  private static ByteString keyData() {
    byte[] bytes = new byte[POLICY.getKeyDataLength()];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (i + 1);
    }
    return ByteString.of(bytes);
  }

  private void awaitTrue(String description, BooleanSupplier condition)
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
