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
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Un-flattened send-failure status codes at the sites {@code SendFailureDiagnosticsTest} does not
 * reach: the {@code MetaDataPublisher} async/sync sends (real code at the writer path, {@code
 * networkMessagesSent} ticked at the connection path at HAND-OFF, before the async outcome — the
 * outlier fix) and the {@code statusCodeOf} fallback: a failure carrying no {@code UaException}
 * anywhere in its chain surfaces as {@code Bad_InternalError}, never a blanket {@code
 * Bad_CommunicationError}. Metadata failures never charge the WriterGroup failure counters. The two
 * {@code DiscoveryRuntime} sites are covered through the shared un-flatten helper only — discovery
 * channels bypass the transport SPI, so no fake can fail their sends (see the test Javadoc).
 */
class SendFailureStatusCodeTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);
  private static final UUID FIELD_ID = new UUID(0L, 1L);

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(1));

  private final BlockingQueue<PubSubDiagnosticsEvent> events = new LinkedBlockingQueue<>();
  private final List<PubSubService> services = new CopyOnWriteArrayList<>();

  @AfterEach
  void tearDown() {
    for (PubSubService service : services) {
      service.close();
    }
    services.clear();
  }

  @Test
  void nonUaExceptionAsyncFailureFallsBackToBadInternalError() throws Exception {
    var transport = new PendingSendsTransport();
    startUdpPublisher(transport);

    CompletableFuture<Void> send = transport.pendingSends.poll(10, TimeUnit.SECONDS);
    assertNotNull(send, "no send observed");

    // no UaException anywhere in the chain: statusCodeOf falls back to Bad_InternalError
    send.completeExceptionally(new RuntimeException(new IllegalStateException("boom")));

    PubSubDiagnosticsEvent event =
        awaitEvent(e -> e.kind() == PubSubDiagnosticsEvent.Kind.SEND_FAILURE);
    assertEquals("conn/WG", event.path());
    assertEquals(new StatusCode(StatusCodes.Bad_InternalError), event.statusCode());

    awaitTrue(
        "fallback failure ticks the WriterGroup failure counter",
        () -> component("conn/WG").failedTransmissions() >= 1);
    assertEquals(new StatusCode(StatusCodes.Bad_InternalError), component("conn/WG").lastError());
  }

  @Test
  void metaDataAsyncSendFailureSurfacesRealCodeAndTicksNetworkMessagesAtHandOff() throws Exception {
    var transport = new MetaDataCapturingBrokerTransport();
    startMqttPublisher(transport);

    CompletableFuture<Void> metaDataSend = transport.metaDataSends.poll(10, TimeUnit.SECONDS);
    assertNotNull(metaDataSend, "no metadata send observed");

    // HAND-OFF: the connection-path counter ticks once the message is handed to the channel,
    // BEFORE the send outcome is known (only metadata ticks the connection path on a broker
    // connection — data cycles tick the group path)
    awaitTrue(
        "networkMessagesSent ticks at the connection path at hand-off",
        () -> component("conn").networkMessagesSent() >= 1);

    metaDataSend.completeExceptionally(
        new UaException(StatusCodes.Bad_ResourceUnavailable, "broker queue full"));

    PubSubDiagnosticsEvent event =
        awaitEvent(e -> e.kind() == PubSubDiagnosticsEvent.Kind.SEND_FAILURE);
    assertEquals("conn/WG/W1", event.path());
    assertEquals(new StatusCode(StatusCodes.Bad_ResourceUnavailable), event.statusCode());

    // metadata failures never charge the WriterGroup failure counters
    assertEquals(0, component("conn/WG").failedTransmissions());
    assertEquals(0, component("conn/WG/W1").failedDataSetMessages());
  }

  @Test
  void metaDataSyncSendThrowSurfacesExtractedCode() throws Exception {
    var transport = new MetaDataCapturingBrokerTransport();
    transport.throwOnMetaDataSend =
        new RuntimeException(new UaException(StatusCodes.Bad_ConnectionClosed, "session closed"));

    startMqttPublisher(transport);

    PubSubDiagnosticsEvent event =
        awaitEvent(e -> e.kind() == PubSubDiagnosticsEvent.Kind.SEND_FAILURE);
    assertEquals("conn/WG/W1", event.path());
    assertEquals(new StatusCode(StatusCodes.Bad_ConnectionClosed), event.statusCode());

    assertEquals(0, component("conn/WG").failedTransmissions());
    assertEquals(0, component("conn/WG/W1").failedDataSetMessages());
  }

  /**
   * The discovery sites ({@code DiscoveryRuntime} announcement and probe sends) are NOT reachable
   * through a transport fake: discovery channels deliberately bypass the transport SPI and always
   * open on the built-in UDP provider ({@code PubSubServiceImpl.getUdpTransportProvider} — "zero
   * transport-SPI change"), and a real loopback UDP send cannot be failed deterministically. This
   * test covers those sites as far as they can go: both route their throwable through the same
   * shared {@code DiagnosticsCollector.statusCodeOf} un-flatten helper asserted here directly —
   * nested {@code UaException} extraction and the {@code Bad_InternalError} fallback.
   */
  @Test
  void statusCodeOfUnFlattensNestedUaExceptionsAndFallsBackToInternalError() {
    assertEquals(
        new StatusCode(StatusCodes.Bad_ResourceUnavailable),
        DiagnosticsCollector.statusCodeOf(
            new UaException(StatusCodes.Bad_ResourceUnavailable, "refused")));

    // the code is extracted through arbitrary wrapping, as the discovery whenComplete sites see it
    assertEquals(
        new StatusCode(StatusCodes.Bad_ServerNotConnected),
        DiagnosticsCollector.statusCodeOf(
            new java.util.concurrent.CompletionException(
                new RuntimeException(
                    new UaException(StatusCodes.Bad_ServerNotConnected, "not connected")))));

    // no UaException anywhere: never a blanket Bad_CommunicationError, always Bad_InternalError
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        DiagnosticsCollector.statusCodeOf(new IllegalStateException("boom")));
  }

  // region fixtures

  /** A UDP publisher transport whose data sends park as pending futures the test completes. */
  private static final class PendingSendsTransport implements TransportProvider {

    final BlockingQueue<CompletableFuture<Void>> pendingSends = new LinkedBlockingQueue<>();

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
      boolean dataChannel = context.transportStateListener() != null;
      return new PublisherChannel() {
        @Override
        public CompletableFuture<Void> send(ByteBuf message) {
          message.release();
          if (!dataChannel) {
            return CompletableFuture.completedFuture(null);
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

  /**
   * An in-memory broker transport: DATA sends succeed, METADATA sends are captured as pending
   * futures (or throw when scripted). Opens no sockets.
   */
  private static final class MetaDataCapturingBrokerTransport implements TransportProvider {

    final BlockingQueue<CompletableFuture<Void>> metaDataSends = new LinkedBlockingQueue<>();

    volatile @Nullable RuntimeException throwOnMetaDataSend;

    @Override
    public String transportProfileUri() {
      return "urn:eclipse:milo:test:stub-broker";
    }

    @Override
    public boolean supports(PubSubConnectionConfig connection) {
      return connection instanceof MqttConnectionConfig;
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
        public CompletableFuture<Void> send(ByteBuf message, MessageAddress address) {
          message.release();
          if (address.kind() != MessageAddress.Kind.METADATA) {
            return CompletableFuture.completedFuture(null);
          }
          RuntimeException t = throwOnMetaDataSend;
          if (t != null) {
            throw t;
          }
          var future = new CompletableFuture<Void>();
          metaDataSends.add(future);
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

  private void startUdpPublisher(TransportProvider transport) throws Exception {
    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet())
            .connection(
                PubSubConnectionConfig.udp("conn")
                    .publisherId(PUBLISHER_ID)
                    .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(writerGroup())
                    .build())
            .build();

    startPublisher(config, transport);
  }

  private void startMqttPublisher(TransportProvider transport) throws Exception {
    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet())
            .connection(
                PubSubConnectionConfig.mqtt("conn")
                    .publisherId(PUBLISHER_ID)
                    .brokerUri(URI.create("mqtt://127.0.0.1:1883"))
                    .writerGroup(writerGroup())
                    .build())
            .build();

    startPublisher(config, transport);
  }

  private void startPublisher(PubSubConfig config, TransportProvider transport) throws Exception {
    PubSubService publisher =
        track(
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
                PubSubServiceConfig.builder().transportProvider(transport).build()));

    publisher.addDiagnosticsListener(events::add);
    publisher.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);
  }

  private static PublishedDataSetConfig dataSet() {
    return PublishedDataSetConfig.builder("PDS")
        .field(
            FieldDefinition.builder("F1").dataType(NodeIds.Int32).dataSetFieldId(FIELD_ID).build())
        .build();
  }

  private static WriterGroupConfig writerGroup() {
    return WriterGroupConfig.builder("WG")
        .writerGroupId(ushort(1))
        .publishingInterval(Duration.ofMillis(50))
        .dataSetWriter(
            DataSetWriterConfig.builder("W1")
                .dataSet(new PublishedDataSetRef("PDS"))
                .dataSetWriterId(ushort(1))
                .build())
        .build();
  }

  private PubSubService track(PubSubService service) {
    services.add(service);
    return service;
  }

  private ComponentDiagnostics component(String path) {
    return componentOf(services.get(services.size() - 1), path);
  }

  private static ComponentDiagnostics componentOf(PubSubService service, String path) {
    return service.diagnostics().component(path).orElseThrow();
  }

  private PubSubDiagnosticsEvent awaitEvent(Predicate<PubSubDiagnosticsEvent> predicate)
      throws InterruptedException {

    var seen = new CopyOnWriteArrayList<PubSubDiagnosticsEvent>();
    long deadline = System.nanoTime() + TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      PubSubDiagnosticsEvent event = events.poll(100, TimeUnit.MILLISECONDS);
      if (event != null) {
        if (predicate.test(event)) {
          return event;
        }
        seen.add(event);
      }
    }
    return fail("timed out waiting for a matching diagnostics event; saw " + List.copyOf(seen));
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
