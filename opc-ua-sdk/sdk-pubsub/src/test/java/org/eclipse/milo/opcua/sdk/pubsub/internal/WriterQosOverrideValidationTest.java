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

import io.netty.buffer.ByteBuf;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Part 14 §6.4.2.5.4 writer-QoS-override validation: an enabled DataSetWriter on a
 * broker (non-UDP) connection whose writer-level {@code BrokerTransportSettings} carry a
 * non-NotSpecified {@code requestedDeliveryGuarantee} without also overriding {@code queueName}
 * fails startup with {@code Bad_ConfigurationError}, and reconfiguration rejects such configs with
 * {@code UaRuntimeException(Bad_ConfigurationError)} before applying anything. Group-level values
 * without a queue name, disabled writers, writers with a queueName override, and writers on UDP
 * connections (where broker settings are inert config) are all tolerated.
 */
class WriterQosOverrideValidationTest {

  private static final UUID FIELD_ID = new UUID(0L, 1L);

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

  // region fixture

  /** Accepts every connection and swallows every send; never touches the network. */
  private static final class NullTransport implements TransportProvider {

    @Override
    public String transportProfileUri() {
      return "urn:eclipse:milo:test:null-transport";
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

  private void createService(PubSubConfig config) {
    transportExecutor = Executors.newSingleThreadExecutor();

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(new NullTransport())
            .transportExecutor(transportExecutor)
            .build();

    service = PubSubService.create(config, bindings(), serviceConfig);
  }

  private static PublishedDataSetConfig dataSet() {
    return PublishedDataSetConfig.builder("PDS")
        .field(
            FieldDefinition.builder("F1").dataType(NodeIds.Int32).dataSetFieldId(FIELD_ID).build())
        .build();
  }

  private static PubSubBindings bindings() {
    return PubSubBindings.builder()
        .source(
            new PublishedDataSetRef("PDS"),
            (PublishedDataSetReadContext context) ->
                DataSetSnapshot.builder(context)
                    .field("F1", new DataValue(Variant.ofInt32(42)))
                    .build())
        .build();
  }

  private static BrokerTransportSettings qosWithoutQueueName() {
    return BrokerTransportSettings.builder()
        .requestedDeliveryGuarantee(BrokerTransportQualityOfService.AtLeastOnce)
        .build();
  }

  private static DataSetWriterConfig.Builder writer(
      @Nullable BrokerTransportSettings brokerTransport) {

    DataSetWriterConfig.Builder builder =
        DataSetWriterConfig.builder("W1")
            .dataSet(new PublishedDataSetRef("PDS"))
            .dataSetWriterId(ushort(1));
    if (brokerTransport != null) {
      builder.brokerTransport(brokerTransport);
    }
    return builder;
  }

  private static WriterGroupConfig.Builder writerGroup(DataSetWriterConfig writer) {
    return WriterGroupConfig.builder("WG")
        .writerGroupId(ushort(1))
        .publishingInterval(Duration.ofMillis(50))
        .dataSetWriter(writer);
  }

  private static PubSubConfig brokerConfig(DataSetWriterConfig writer) {
    return brokerConfig(writerGroup(writer).build());
  }

  private static PubSubConfig brokerConfig(WriterGroupConfig group) {
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.uint16(ushort(1)))
            .writerGroup(group)
            .build();

    return PubSubConfig.builder().publishedDataSet(dataSet()).connection(connection).build();
  }

  private static PubSubConfig udpConfig(DataSetWriterConfig writer) {
    UdpConnectionConfig connection =
        PubSubConnectionConfig.udp("conn")
            .address(UdpDatagramAddress.unicast("127.0.0.1", 4840))
            .publisherId(PublisherId.uint16(ushort(1)))
            .writerGroup(writerGroup(writer).build())
            .build();

    return PubSubConfig.builder().publishedDataSet(dataSet()).connection(connection).build();
  }

  private void assertStartupFailsWithConfigurationError(PubSubConfig config) {
    createService(config);

    ExecutionException e =
        assertThrows(ExecutionException.class, () -> service.startup().get(10, TimeUnit.SECONDS));
    UaException cause = assertInstanceOf(UaException.class, e.getCause());
    assertEquals(StatusCodes.Bad_ConfigurationError, cause.getStatusCode().value());
    assertTrue(
        cause.getMessage().contains("overrides requestedDeliveryGuarantee without a queueName"),
        "unexpected message: " + cause.getMessage());
  }

  // endregion

  @Test
  void enabledBrokerWriterQosOverrideWithoutQueueNameFailsStartup() {
    assertStartupFailsWithConfigurationError(brokerConfig(writer(qosWithoutQueueName()).build()));
  }

  @Test
  void emptyStringQueueNameCountsAsAbsent() {
    assertStartupFailsWithConfigurationError(
        brokerConfig(
            writer(
                    BrokerTransportSettings.builder()
                        .queueName("")
                        .requestedDeliveryGuarantee(BrokerTransportQualityOfService.ExactlyOnce)
                        .build())
                .build()));
  }

  @Test
  void writerQosOverrideWithQueueNameStarts() throws Exception {
    createService(
        brokerConfig(
            writer(
                    BrokerTransportSettings.builder()
                        .queueName("writer/topic")
                        .requestedDeliveryGuarantee(BrokerTransportQualityOfService.AtLeastOnce)
                        .build())
                .build()));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(group));
  }

  @Test
  void groupLevelQosWithoutQueueNameStarts() throws Exception {
    // §6.4.2.5.4 constrains the writer level only: a group-level requestedDeliveryGuarantee
    // without a queue name is legal (the group publishes to the derived topic).
    createService(
        brokerConfig(
            writerGroup(writer(null).build()).brokerTransport(qosWithoutQueueName()).build()));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(group));
  }

  @Test
  void disabledWriterQosOverrideWithoutQueueNameStarts() throws Exception {
    // Disabled writers are tolerated so imported configs keep round-tripping.
    createService(brokerConfig(writer(qosWithoutQueueName()).enabled(false).build()));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Disabled, service.state(writer));
  }

  @Test
  void udpConnectionWriterQosOverrideIsInert() throws Exception {
    // On UDP connections broker transport settings are inert config kept for round-trip
    // fidelity; the combination is not rejected.
    createService(udpConfig(writer(qosWithoutQueueName()).build()));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(group));
  }

  /**
   * The writer-QoS-override validation also applies to reconfiguration: a config that startup would
   * reject with {@code Bad_ConfigurationError} is rejected before any change is applied.
   */
  @Test
  void reconfigureToWriterQosOverrideWithoutQueueNameIsRejected() throws Exception {
    createService(
        brokerConfig(
            writer(
                    BrokerTransportSettings.builder()
                        .queueName("writer/topic")
                        .requestedDeliveryGuarantee(BrokerTransportQualityOfService.AtLeastOnce)
                        .build())
                .build()));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubConfig invalid = brokerConfig(writer(qosWithoutQueueName()).build());

    UaRuntimeException e =
        assertThrows(
            UaRuntimeException.class,
            () -> service.reconfigure(invalid, PubSubService.ReconfigureMode.DISABLE_AFFECTED));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(
        e.getMessage().contains("overrides requestedDeliveryGuarantee without a queueName"),
        "unexpected message: " + e.getMessage());

    // the rejected config was not applied: the original writer group is still running
    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(group));
  }
}
