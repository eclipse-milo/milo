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
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageMappingProvider;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Send-failure diagnostics after status codes are preserved end-to-end: real status codes (never a
 * blanket {@code Bad_CommunicationError}), {@link PubSubDiagnosticsEvent.Kind} classification, and
 * the failedTransmissions / failedDataSetMessages arbitration — an oversize drop ticks both, an
 * encode failure ticks failedDataSetMessages only.
 */
class SendFailureDiagnosticsTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);
  private static final UUID FIELD_ID = new UUID(0L, 1L);

  private static final String GROUP_PATH = "conn/WG";
  private static final String WRITER_PATH = "conn/WG/W1";

  private final BlockingQueue<PubSubDiagnosticsEvent> events = new LinkedBlockingQueue<>();

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
  void asyncSendFailureSurfacesRealCodeAndTicksBothCounters() throws Exception {
    var transport = new StubPublisherTransport();
    startPublisher(transport, writerGroup(uint(0)), null);

    CompletableFuture<Void> send = transport.pendingSends.poll(10, TimeUnit.SECONDS);
    assertNotNull(send, "no send observed");

    send.completeExceptionally(new UaException(StatusCodes.Bad_ServerNotConnected, "broker down"));

    PubSubDiagnosticsEvent event = awaitEvent();
    assertEquals(GROUP_PATH, event.path());
    assertEquals(new StatusCode(StatusCodes.Bad_ServerNotConnected), event.statusCode());
    assertEquals(PubSubDiagnosticsEvent.Kind.SEND_FAILURE, event.kind());

    awaitTrue(
        "failedTransmissions at the group and failedDataSetMessages at the writer",
        () ->
            component(GROUP_PATH).failedTransmissions() >= 1
                && component(WRITER_PATH).failedDataSetMessages() >= 1);
    assertEquals(
        new StatusCode(StatusCodes.Bad_ServerNotConnected), component(GROUP_PATH).lastError());
    // the writer's own counter was not attributed to the group and vice versa
    assertEquals(0, component(WRITER_PATH).failedTransmissions());
    assertEquals(0, component(GROUP_PATH).failedDataSetMessages());
  }

  @Test
  void syncSendThrowSurfacesExtractedCodeAndTicksBothCounters() throws Exception {
    var transport = new StubPublisherTransport();
    transport.throwOnSend =
        new RuntimeException(new UaException(StatusCodes.Bad_InvalidState, "channel closed"));

    startPublisher(transport, writerGroup(uint(0)), null);

    PubSubDiagnosticsEvent event = awaitEvent();
    assertEquals(GROUP_PATH, event.path());
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidState), event.statusCode());
    assertEquals(PubSubDiagnosticsEvent.Kind.SEND_FAILURE, event.kind());

    awaitTrue(
        "failedTransmissions at the group and failedDataSetMessages at the writer",
        () ->
            component(GROUP_PATH).failedTransmissions() >= 1
                && component(WRITER_PATH).failedDataSetMessages() >= 1);
  }

  @Test
  void oversizeDropTicksBothCountersWithEncodingLimitsExceeded() throws Exception {
    var transport = new StubPublisherTransport();
    // every encoded NetworkMessage exceeds an 8-byte budget: all cycles drop at the size gate
    startPublisher(transport, writerGroup(uint(8)), null);

    PubSubDiagnosticsEvent event = awaitEvent();
    assertEquals(GROUP_PATH, event.path());
    assertEquals(new StatusCode(StatusCodes.Bad_EncodingLimitsExceeded), event.statusCode());
    assertEquals(PubSubDiagnosticsEvent.Kind.SEND_FAILURE, event.kind());

    awaitTrue(
        "an oversize skip ticks BOTH failure counters",
        () ->
            component(GROUP_PATH).failedTransmissions() >= 1
                && component(WRITER_PATH).failedDataSetMessages() >= 1);

    // the skipped message was never handed to the channel
    assertEquals(0, component(GROUP_PATH).networkMessagesSent());
    assertTrue(transport.pendingSends.isEmpty());
  }

  @Test
  void encodeFailureTicksFailedDataSetMessagesOnly() throws Exception {
    var transport = new StubPublisherTransport();
    // a custom provider registered under "uadp" shadows the built-in mapping and always throws
    var failingMapping =
        new MessageMappingProvider() {
          @Override
          public String mappingName() {
            return "uadp";
          }

          @Override
          public List<EncodedNetworkMessage> encode(EncodeContext context) throws UaException {
            throw new UaException(StatusCodes.Bad_EncodingError, "scripted encode failure");
          }

          @Override
          public DecodedNetworkMessage decode(DecodeContext context, ByteBuf payload)
              throws UaException {
            throw new UaException(StatusCodes.Bad_NotSupported, "not used");
          }
        };

    startPublisher(transport, writerGroup(uint(0)), failingMapping);

    PubSubDiagnosticsEvent event = awaitEvent();
    assertEquals(GROUP_PATH, event.path());
    assertEquals(new StatusCode(StatusCodes.Bad_EncodingError), event.statusCode());
    assertEquals(PubSubDiagnosticsEvent.Kind.OTHER_ERROR, event.kind());

    awaitTrue(
        "an encode failure ticks failedDataSetMessages at the writer",
        () -> component(WRITER_PATH).failedDataSetMessages() >= 1);

    // ... but never failedTransmissions: no NetworkMessage existed
    assertEquals(0, component(GROUP_PATH).failedTransmissions());
  }

  // region fixtures

  /** A publisher transport whose sends are controllable: pending futures, scripted sync throws. */
  private static final class StubPublisherTransport implements TransportProvider {

    final BlockingQueue<CompletableFuture<Void>> pendingSends = new LinkedBlockingQueue<>();

    volatile @Nullable RuntimeException throwOnSend;

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
          RuntimeException t = throwOnSend;
          if (t != null) {
            throw t;
          }
          var future = new CompletableFuture<Void>();
          pendingSends.add(future);
          return future;
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

  private static WriterGroupConfig writerGroup(UInteger maxNetworkMessageSize) {
    return WriterGroupConfig.builder("WG")
        .writerGroupId(ushort(1))
        .publishingInterval(Duration.ofMillis(20))
        .maxNetworkMessageSize(maxNetworkMessageSize)
        .dataSetWriter(
            DataSetWriterConfig.builder("W1")
                .dataSet(new PublishedDataSetRef("PDS"))
                .dataSetWriterId(ushort(1))
                .build())
        .build();
  }

  private void startPublisher(
      StubPublisherTransport transport,
      WriterGroupConfig group,
      @Nullable MessageMappingProvider mappingProvider)
      throws Exception {

    transportExecutor = Executors.newSingleThreadExecutor();

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("PDS")
            .field(
                FieldDefinition.builder("F1")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(FIELD_ID)
                    .build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("conn")
                    .publisherId(PublisherId.uint16(ushort(1)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(group)
                    .build())
            .build();

    PubSubServiceConfig.Builder serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(transport)
            .transportExecutor(transportExecutor);
    if (mappingProvider != null) {
      serviceConfig.messageMappingProvider(mappingProvider);
    }

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
                .build(),
            serviceConfig.build());

    service.addDiagnosticsListener(events::add);
    service.startup().get(10, TimeUnit.SECONDS);
  }

  private PubSubDiagnostics.ComponentDiagnostics component(String path) {
    assertNotNull(service);
    return service.diagnostics().component(path).orElseThrow();
  }

  private PubSubDiagnosticsEvent awaitEvent() throws InterruptedException {
    PubSubDiagnosticsEvent event = events.poll(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
    assertNotNull(event, "no diagnostics event observed");
    return event;
  }

  private static void awaitTrue(String description, BooleanSupplier condition)
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
