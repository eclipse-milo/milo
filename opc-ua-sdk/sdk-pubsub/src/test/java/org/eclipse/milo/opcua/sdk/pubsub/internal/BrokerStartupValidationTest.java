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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the M7 startup validation additions: an enabled DataSetReader on a broker (non-UDP)
 * connection requires a configured data {@code queueName} (the §7.3.4.7.3 topic derivation needs
 * the publisher-side WriterGroup name, which the reader does not know) — startup fails with {@code
 * Bad_ConfigurationError}, and reconfiguration rejects such configs with {@code
 * UaRuntimeException(Bad_ConfigurationError)} before applying anything; and JSON message settings
 * on a broker connection start WITHOUT any user-registered mapping provider, because the {@code
 * "json"} mapping is now built in.
 */
class BrokerStartupValidationTest {

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

  /** One captured send: the payload bytes and the address the engine resolved for them. */
  record Sent(byte[] payload, @Nullable MessageAddress address) {}

  /** Captures every addressed send; never touches the network. */
  private static final class CapturingTransport implements TransportProvider {

    final BlockingQueue<Sent> sent = new LinkedBlockingQueue<>();

    @Override
    public String transportProfileUri() {
      return "urn:eclipse:milo:test:capturing-transport";
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
          try {
            byte[] bytes = new byte[message.readableBytes()];
            message.readBytes(bytes);
            sent.add(new Sent(bytes, address));
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
      return () -> CompletableFuture.completedFuture(null);
    }
  }

  private CapturingTransport createService(PubSubConfig config, PubSubBindings bindings) {
    var transport = new CapturingTransport();
    transportExecutor = Executors.newSingleThreadExecutor();

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(transport)
            .transportExecutor(transportExecutor)
            .build();

    service = PubSubService.create(config, bindings, serviceConfig);
    return transport;
  }

  private static PubSubConfig readerConfig(DataSetReaderConfig reader) {
    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .readerGroup(ReaderGroupConfig.builder("RG").dataSetReader(reader).build())
            .build();

    return PubSubConfig.builder().connection(connection).build();
  }

  private void assertStartupFailsWithConfigurationError(PubSubConfig config) {
    createService(config, PubSubBindings.builder().build());

    ExecutionException e =
        assertThrows(ExecutionException.class, () -> service.startup().get(10, TimeUnit.SECONDS));
    UaException cause = assertInstanceOf(UaException.class, e.getCause());
    assertEquals(StatusCodes.Bad_ConfigurationError, cause.getStatusCode().value());
    assertTrue(
        cause.getMessage().contains("requires a data queueName"),
        "unexpected message: " + cause.getMessage());
  }

  // endregion

  @Test
  void enabledBrokerReaderWithoutDataQueueNameFailsStartup() {
    assertStartupFailsWithConfigurationError(
        readerConfig(DataSetReaderConfig.builder("R1").build()));
  }

  @Test
  void emptyStringDataQueueNameCountsAsAbsent() {
    assertStartupFailsWithConfigurationError(
        readerConfig(
            DataSetReaderConfig.builder("R1")
                .brokerTransport(BrokerTransportSettings.builder().queueName("").build())
                .build()));
  }

  @Test
  void brokerReaderWithMetaDataQueueNameOnlyStillFailsStartup() {
    // the metadata queue is optional, the DATA queue is the required one
    assertStartupFailsWithConfigurationError(
        readerConfig(
            DataSetReaderConfig.builder("R1")
                .brokerTransport(
                    BrokerTransportSettings.builder().metaDataQueueName("md/topic").build())
                .build()));
  }

  @Test
  void disabledBrokerReaderWithoutDataQueueNameDoesNotFailStartup() throws Exception {
    createService(
        readerConfig(DataSetReaderConfig.builder("R1").enabled(false).build()),
        PubSubBindings.builder().build());

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle reader = service.components().dataSetReader("conn", "RG", "R1").orElseThrow();
    assertEquals(PubSubState.Disabled, service.state(reader));
  }

  @Test
  void brokerReaderWithDataQueueNameStarts() throws Exception {
    createService(
        readerConfig(
            DataSetReaderConfig.builder("R1")
                .brokerTransport(
                    BrokerTransportSettings.builder().queueName("factory/data").build())
                .build()),
        PubSubBindings.builder().build());

    service.startup().get(10, TimeUnit.SECONDS);

    // the reader waits for its first key frame
    PubSubHandle reader = service.components().dataSetReader("conn", "RG", "R1").orElseThrow();
    assertEquals(PubSubState.PreOperational, service.state(reader));
  }

  /**
   * The M7 broker-reader queueName validation also applies to reconfiguration: a config that
   * startup would reject with {@code Bad_ConfigurationError} is rejected before any change is
   * applied, instead of being accepted silently (where the reader could never receive anything).
   */
  @Test
  void reconfigureToBrokerReaderWithoutDataQueueNameIsRejected() throws Exception {
    createService(
        readerConfig(
            DataSetReaderConfig.builder("R1")
                .brokerTransport(
                    BrokerTransportSettings.builder().queueName("factory/data").build())
                .build()),
        PubSubBindings.builder().build());

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubConfig invalid = readerConfig(DataSetReaderConfig.builder("R1").build());

    UaRuntimeException e =
        assertThrows(
            UaRuntimeException.class,
            () -> service.reconfigure(invalid, PubSubService.ReconfigureMode.DISABLE_AFFECTED));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(
        e.getMessage().contains("requires a data queueName"),
        "unexpected message: " + e.getMessage());

    // the rejected config was not applied: the original reader handle is still valid
    PubSubHandle reader = service.components().dataSetReader("conn", "RG", "R1").orElseThrow();
    assertEquals(PubSubState.PreOperational, service.state(reader));
  }

  @Test
  void jsonSettingsOnBrokerConnectionStartWithBuiltInProvider() throws Exception {
    // no MessageMappingProvider is registered: the "json" mapping resolves to the built-in
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("PDS")
            .field(
                FieldDefinition.builder("F1")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(FIELD_ID)
                    .build())
            .build();

    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.string("line-7"))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(50))
                    .messageSettings(JsonWriterGroupSettings.builder().build())
                    .dataSetWriter(
                        DataSetWriterConfig.builder("W1")
                            .dataSet(new PublishedDataSetRef("PDS"))
                            .dataSetWriterId(ushort(1))
                            .settings(JsonDataSetWriterSettings.builder().build())
                            .build())
                    .build())
            .readerGroup(
                ReaderGroupConfig.builder("RG")
                    .dataSetReader(
                        DataSetReaderConfig.builder("R1")
                            .settings(JsonDataSetReaderSettings.builder().build())
                            .brokerTransport(
                                BrokerTransportSettings.builder()
                                    .queueName("opcua/json/data/line-7/WG")
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(
                new PublishedDataSetRef("PDS"),
                (PublishedDataSetReadContext context) ->
                    DataSetSnapshot.builder(context)
                        .field("F1", new DataValue(Variant.ofInt32(42)))
                        .build())
            .build();

    CapturingTransport transport =
        createService(
            PubSubConfig.builder().publishedDataSet(dataSet).connection(connection).build(),
            bindings);

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(group));

    // the built-in JSON mapping encodes both the retained metadata and the data NetworkMessages,
    // with the JSON content type and the derived json topic tree
    Sent metaData = null;
    Sent data = null;
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while ((metaData == null || data == null) && System.nanoTime() < deadline) {
      Sent sent = transport.sent.poll(1, TimeUnit.SECONDS);
      if (sent == null) {
        continue;
      }
      assertNotNull(sent.address());
      if (sent.address().kind() == MessageAddress.Kind.METADATA && metaData == null) {
        metaData = sent;
      } else if (sent.address().kind() == MessageAddress.Kind.DATA && data == null) {
        data = sent;
      }
    }

    assertNotNull(metaData, "no metadata NetworkMessage published");
    assertEquals("opcua/json/metadata/line-7/WG/W1", metaData.address().queueName());
    assertEquals(MessageAddress.CONTENT_TYPE_JSON, metaData.address().contentTypeHint());
    assertTrue(metaData.address().retain());

    assertNotNull(data, "no data NetworkMessage published");
    assertEquals("opcua/json/data/line-7/WG", data.address().queueName());
    assertEquals(MessageAddress.CONTENT_TYPE_JSON, data.address().contentTypeHint());

    // both payloads are JSON documents
    assertTrue(new String(metaData.payload(), StandardCharsets.UTF_8).startsWith("{"));
    String dataJson = new String(data.payload(), StandardCharsets.UTF_8);
    assertTrue(dataJson.startsWith("{"), dataJson);
    assertTrue(dataJson.contains("\"ua-data\""), dataJson);
  }
}
