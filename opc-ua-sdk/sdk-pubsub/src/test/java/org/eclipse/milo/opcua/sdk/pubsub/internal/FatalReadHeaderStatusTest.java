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
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
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
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * A fatal publisher read failure surfaces as a Bad DataSetMessage header status (Part 14 §6.2.4.2:
 * "the header status is set to a bad code in a fatal error situation").
 *
 * <p>When the bound {@link PublishedDataSetSource} throws, {@code DataSetWriterRuntime} still emits
 * a DataSetMessage — every field set to {@code Bad_InternalError} — but it must mark the
 * message-level status with the failure's StatusCode rather than the otherwise-correct Good
 * (Variant/DataValue field representations carry per-field quality inline, so their header stays
 * Good on a healthy read; the aggregate-into-header behavior is reserved for the fatal case and for
 * UADP RawData, which Milo rejects). The status computation lives in the mapping-agnostic runtime,
 * so a JSON publish cycle pins it; the UADP mapping's faithful emission of a non-Good draft status
 * is covered by {@code UadpRoundTripTest}.
 */
class FatalReadHeaderStatusTest {

  private @Nullable PubSubService service;

  @AfterEach
  void shutdownService() throws Exception {
    if (service != null) {
      service.close();
      service = null;
    }
  }

  /** Captures every addressed send; never touches the network. */
  private static final class CapturingTransport implements TransportProvider {

    record Sent(MessageAddress.Kind kind, String payload) {}

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
            sent.add(
                new Sent(
                    address != null ? address.kind() : MessageAddress.Kind.DATA,
                    message.toString(StandardCharsets.UTF_8)));
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

  /**
   * One MQTT/JSON writer on a single-field dataset; default masks (so the header carries Status).
   */
  private static PubSubConfig publisherConfig() {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("PDS")
            .field(
                FieldDefinition.builder("counter")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(new UUID(0L, 1L))
                    .build())
            .build();

    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.string("line-7"))
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(25))
                    .messageSettings(JsonWriterGroupSettings.builder().build())
                    .dataSetWriter(
                        DataSetWriterConfig.builder("W1")
                            .enabled(true)
                            .dataSet(new PublishedDataSetRef("PDS"))
                            .dataSetWriterId(ushort(1))
                            .settings(JsonDataSetWriterSettings.builder().build())
                            .build())
                    .build())
            .build();

    return PubSubConfig.builder().publishedDataSet(dataSet).connection(connection).build();
  }

  @Test
  void throwingSourceMarksHeaderStatusBad() throws Exception {
    // a source that always fails the read with a specific StatusCode
    PublishedDataSetSource failing =
        context -> {
          throw new UaException(StatusCodes.Bad_NoData, "source unavailable");
        };
    PubSubBindings bindings =
        PubSubBindings.builder().source(new PublishedDataSetRef("PDS"), failing).build();

    var transport = new CapturingTransport();
    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder().transportProvider(transport).build();

    service = PubSubService.create(publisherConfig(), bindings, serviceConfig);
    service.startup().get(10, TimeUnit.SECONDS);

    JsonObject message = pollFirstDataMessage(transport);

    // §6.2.4.2: the header status carries the read failure's StatusCode (the full code in JSON)
    assertTrue(message.has("Status"), "expected a header Status on a fatal read: " + message);
    assertEquals(
        StatusCodes.Bad_NoData, message.get("Status").getAsJsonObject().get("Code").getAsLong());

    // the fields themselves are published as Bad_InternalError (Variant repr: StatusCode in slot)
    JsonObject payload = message.get("Payload").getAsJsonObject();
    assertEquals(
        StatusCodes.Bad_InternalError,
        payload.get("counter").getAsJsonObject().get("Code").getAsLong());
  }

  /** Poll the first captured DATA NetworkMessage and return its single DataSetMessage object. */
  private static JsonObject pollFirstDataMessage(CapturingTransport transport)
      throws InterruptedException {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while (System.nanoTime() < deadline) {
      CapturingTransport.Sent sent = transport.sent.poll(1, TimeUnit.SECONDS);
      if (sent == null || sent.kind() != MessageAddress.Kind.DATA) {
        continue;
      }
      JsonObject networkMessage = JsonParser.parseString(sent.payload()).getAsJsonObject();
      return networkMessage.get("Messages").getAsJsonArray().get(0).getAsJsonObject();
    }
    throw new AssertionError("no data NetworkMessage was captured");
  }
}
