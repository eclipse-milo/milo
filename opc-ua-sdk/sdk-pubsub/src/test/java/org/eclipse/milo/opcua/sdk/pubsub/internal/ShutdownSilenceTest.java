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
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
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
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
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
 * Shutdown/restart send-failure silence: a send failure surfacing after the owning group was
 * deactivated — clean shutdown or reconfigure-removal — is teardown noise and must emit no
 * diagnostics event and tick no failure counter; the same failure while the group is active still
 * reports (the control row).
 */
class ShutdownSilenceTest {

  private static final UUID FIELD_ID = new UUID(0L, 1L);

  private static final String GROUP_PATH = "conn/WG";

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
  void sendFailureAfterShutdownIsSilent() throws Exception {
    var transport = new StubPublisherTransport();
    startPublisher(transport);

    CompletableFuture<Void> send = transport.pendingSends.poll(10, TimeUnit.SECONDS);
    assertNotNull(send, "no send observed");

    service.shutdown().get(10, TimeUnit.SECONDS);
    events.clear();

    // the pending send fails only now, after the shutdown deactivated the group
    send.completeExceptionally(new UaException(StatusCodes.Bad_CommunicationError, "torn down"));
    flushTransport();

    assertTrue(events.isEmpty(), "teardown noise must not become a diagnostics event: " + events);
    // shutdown does not unregister diagnostics paths, so the counters remain observable
    PubSubDiagnostics.ComponentDiagnostics group =
        service.diagnostics().component(GROUP_PATH).orElseThrow();
    assertEquals(0, group.failedTransmissions());
    assertNull(group.lastError());
  }

  @Test
  void sendFailureAfterReconfigureRemovalIsSilent() throws Exception {
    var transport = new StubPublisherTransport();
    startPublisher(transport);

    CompletableFuture<Void> send = transport.pendingSends.poll(10, TimeUnit.SECONDS);
    assertNotNull(send, "no send observed");

    // remove the writer group: its runtime is deactivated and its diagnostics entry discarded
    PubSubConfig withoutGroup =
        PubSubConfig.builder()
            .publishedDataSet(dataSet())
            .connection(connection(publisherPort, discoveryPort).build())
            .build();
    service.reconfigure(withoutGroup, PubSubService.ReconfigureMode.DISABLE_AFFECTED);
    events.clear();

    send.completeExceptionally(new UaException(StatusCodes.Bad_CommunicationError, "torn down"));
    flushTransport();

    assertTrue(events.isEmpty(), "teardown noise must not become a diagnostics event: " + events);
    assertTrue(service.diagnostics().component(GROUP_PATH).isEmpty());
  }

  @Test
  void sendFailureWhileActiveStillReports() throws Exception {
    var transport = new StubPublisherTransport();
    startPublisher(transport);

    CompletableFuture<Void> send = transport.pendingSends.poll(10, TimeUnit.SECONDS);
    assertNotNull(send, "no send observed");

    send.completeExceptionally(new UaException(StatusCodes.Bad_ServerNotConnected, "still up"));

    PubSubDiagnosticsEvent event = events.poll(10, TimeUnit.SECONDS);
    assertNotNull(event, "an active-group send failure must report");
    assertEquals(GROUP_PATH, event.path());
    assertEquals(new StatusCode(StatusCodes.Bad_ServerNotConnected), event.statusCode());
    assertEquals(PubSubDiagnosticsEvent.Kind.SEND_FAILURE, event.kind());
  }

  // region fixtures

  /** A publisher transport that parks every send in a pending future the test completes. */
  private static final class StubPublisherTransport implements TransportProvider {

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
      return new PublisherChannel() {
        @Override
        public CompletableFuture<Void> send(ByteBuf message) {
          message.release();
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

  private int publisherPort;
  private int discoveryPort;

  private void startPublisher(StubPublisherTransport transport) throws Exception {
    transportExecutor = Executors.newSingleThreadExecutor();
    publisherPort = freeUdpPort();
    discoveryPort = freeUdpPort();

    WriterGroupConfig group =
        WriterGroupConfig.builder("WG")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(20))
            .dataSetWriter(
                DataSetWriterConfig.builder("W1")
                    .dataSet(new PublishedDataSetRef("PDS"))
                    .dataSetWriterId(ushort(1))
                    .build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet())
            .connection(connection(publisherPort, discoveryPort).writerGroup(group).build())
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
                .build(),
            PubSubServiceConfig.builder()
                .transportProvider(transport)
                .transportExecutor(transportExecutor)
                .build());

    service.addDiagnosticsListener(events::add);
    service.startup().get(10, TimeUnit.SECONDS);
  }

  private static PublishedDataSetConfig dataSet() {
    return PublishedDataSetConfig.builder("PDS")
        .field(
            FieldDefinition.builder("F1").dataType(NodeIds.Int32).dataSetFieldId(FIELD_ID).build())
        .build();
  }

  private static UdpConnectionConfig.Builder connection(int port, int discoveryPort) {
    return PubSubConnectionConfig.udp("conn")
        .publisherId(PublisherId.uint16(ushort(1)))
        .address(UdpDatagramAddress.unicast("127.0.0.1", port))
        .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", discoveryPort));
  }

  private void flushTransport() throws Exception {
    assertNotNull(transportExecutor);
    transportExecutor.submit(() -> {}).get(10, TimeUnit.SECONDS);
  }

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
