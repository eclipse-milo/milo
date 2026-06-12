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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
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
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
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
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Startup/reconfigure validation of the delta-frame mask consistency rule (Part 14 Annex
 * A.3.3.4/A.3.4.4: "If the KeyFrameCount is not 1, the MessageType bit shall be true"): a JSON
 * writer with {@code keyFrameCount > 1} requires the DataSetMessageHeader in the group's effective
 * JsonNetworkMessageContentMask and the MessageType member in its effective
 * JsonDataSetMessageContentMask — without them a delta payload would be indistinguishable from a
 * key frame on the wire. Startup fails with {@code Bad_ConfigurationError} and {@code reconfigure}
 * rejects with {@code UaRuntimeException(Bad_ConfigurationError)} before applying anything;
 * disabled writers are tolerated (round-trip posture), and a writer that slips past validation
 * (enabled after startup) degrades safely to every-cycle key frames.
 *
 * <p>The UADP counterpart: a UADP writer with {@code keyFrameCount > 1} requires ConfiguredSize 0
 * (dynamic size). Fixed-size layouts are key-frame-only (Annex A.2.1.7: "Only data key frame
 * DataSetMessages are supported", with KeyFrameCount fixed to 1), and a DataSetMessage exceeding
 * its ConfiguredSize is re-encoded header-only with the valid bit clear (§6.3.1.3.3) yet still
 * sends, so a delta lost that way would bypass the not-transmitted baseline invalidation and leave
 * subscribers silently stale. The UADP message-type bits themselves are always expressible: the
 * UADP DataSetMessage header (including DataSetFlags2, which carries them) is unconditionally
 * present — Part 14 Table 158 defines no mask bit that could disable it.
 *
 * <p>Both rules apply only when the group's mapping name resolves to the built-in provider: a
 * custom MessageMappingProvider shadowing "uadp" or "json" owns its wire format and is never
 * second-guessed by them.
 */
class DeltaFrameValidationTest {

  private static final UUID COUNTER_FIELD_ID = new UUID(0L, 1L);
  private static final UUID CONSTANT_FIELD_ID = new UUID(0L, 2L);

  /** A DataSetMessage mask able to express delta frames: MessageType present. */
  private static final JsonDataSetMessageContentMask DELTA_CAPABLE_DSM_MASK =
      JsonDataSetMessageContentMask.of(
          JsonDataSetMessageContentMask.Field.DataSetWriterId,
          JsonDataSetMessageContentMask.Field.SequenceNumber,
          JsonDataSetMessageContentMask.Field.MessageType,
          JsonDataSetMessageContentMask.Field.FieldEncoding2);

  private @Nullable PubSubService service;

  @AfterEach
  void shutdownService() throws Exception {
    if (service != null) {
      service.close();
      service = null;
    }
  }

  // region fixture

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
      return new SubscriberChannel() {
        @Override
        public CompletableFuture<Void> closeAsync() {
          return CompletableFuture.completedFuture(null);
        }
      };
    }
  }

  /**
   * Shadows the built-in "uadp" mapping (configured providers take precedence): a custom provider
   * owns its wire format, so the built-in delta-frame expressibility rules must not apply to it.
   */
  private static final class StubUadpMapping implements MessageMappingProvider {

    @Override
    public String mappingName() {
      return "uadp";
    }

    @Override
    public List<EncodedNetworkMessage> encode(EncodeContext context) {
      return List.of(new EncodedNetworkMessage(Unpooled.wrappedBuffer(new byte[] {0x00})));
    }

    @Override
    public DecodedNetworkMessage decode(DecodeContext context, ByteBuf payload) throws UaException {
      throw new UaException(StatusCodes.Bad_NotSupported, "encode-only stub");
    }
  }

  /**
   * One MQTT connection, one JSON writer group, one writer on a two-field dataset with a bound
   * (constant-valued) source.
   */
  private static PubSubConfig publisherConfig(
      JsonNetworkMessageContentMask networkMask,
      JsonDataSetMessageContentMask messageMask,
      int keyFrameCount,
      boolean writerEnabled) {

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("PDS")
            .field(
                FieldDefinition.builder("counter")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(COUNTER_FIELD_ID)
                    .build())
            .field(
                FieldDefinition.builder("constant")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(CONSTANT_FIELD_ID)
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
                    .messageSettings(
                        JsonWriterGroupSettings.builder()
                            .networkMessageContentMask(networkMask)
                            .build())
                    .dataSetWriter(
                        DataSetWriterConfig.builder("W1")
                            .enabled(writerEnabled)
                            .dataSet(new PublishedDataSetRef("PDS"))
                            .dataSetWriterId(ushort(1))
                            .keyFrameCount(uint(keyFrameCount))
                            .settings(
                                JsonDataSetWriterSettings.builder()
                                    .dataSetMessageContentMask(messageMask)
                                    .build())
                            .build())
                    .build())
            .build();

    return PubSubConfig.builder().publishedDataSet(dataSet).connection(connection).build();
  }

  /**
   * One MQTT connection, one UADP writer group, one writer on a two-field dataset with a bound
   * (constant-valued) source; same shape as {@link #publisherConfig} but UADP-mapped, for the
   * ConfiguredSize rule.
   */
  private static PubSubConfig uadpPublisherConfig(
      int keyFrameCount, int configuredSize, boolean writerEnabled) {

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("PDS")
            .field(
                FieldDefinition.builder("counter")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(COUNTER_FIELD_ID)
                    .build())
            .field(
                FieldDefinition.builder("constant")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(CONSTANT_FIELD_ID)
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
                    .messageSettings(UadpWriterGroupSettings.builder().build())
                    .dataSetWriter(
                        DataSetWriterConfig.builder("W1")
                            .enabled(writerEnabled)
                            .dataSet(new PublishedDataSetRef("PDS"))
                            .dataSetWriterId(ushort(1))
                            .keyFrameCount(uint(keyFrameCount))
                            .settings(
                                UadpDataSetWriterSettings.builder()
                                    .configuredSize(ushort(configuredSize))
                                    .build())
                            .build())
                    .build())
            .build();

    return PubSubConfig.builder().publishedDataSet(dataSet).connection(connection).build();
  }

  private static PubSubBindings constantSourceBindings() {
    PublishedDataSetSource source =
        context ->
            DataSetSnapshot.builder(context)
                .field("counter", new DataValue(Variant.ofInt32(7)))
                .field("constant", new DataValue(Variant.ofInt32(100)))
                .build();

    return PubSubBindings.builder().source(new PublishedDataSetRef("PDS"), source).build();
  }

  private CapturingTransport createService(PubSubConfig config) {
    var transport = new CapturingTransport();

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder().transportProvider(transport).build();

    service = PubSubService.create(config, constantSourceBindings(), serviceConfig);
    return transport;
  }

  private void assertStartupFailsWithConfigurationError(
      PubSubConfig config, String messageFragment) {

    createService(config);

    ExecutionException e =
        assertThrows(ExecutionException.class, () -> service.startup().get(10, TimeUnit.SECONDS));
    UaException cause = assertInstanceOf(UaException.class, e.getCause());
    assertEquals(StatusCodes.Bad_ConfigurationError, cause.getStatusCode().value());
    assertTrue(
        cause.getMessage().contains(messageFragment), "unexpected message: " + cause.getMessage());
    assertTrue(
        cause.getMessage().contains("conn/WG/W1"), "unexpected message: " + cause.getMessage());
  }

  // endregion

  /** The Milo default JSON DataSetMessage mask has no MessageType member. */
  @Test
  void jsonWriterKeyFrameCountWithDefaultMasksFailsStartup() {
    assertStartupFailsWithConfigurationError(
        publisherConfig(
            JsonNetworkMessageContentMask.of(), JsonDataSetMessageContentMask.of(), 3, true),
        "MessageType");
  }

  @Test
  void jsonWriterKeyFrameCountWithoutDataSetMessageHeaderFailsStartup() {
    // non-empty network mask without the DataSetMessageHeader bit
    var networkMask =
        JsonNetworkMessageContentMask.of(
            JsonNetworkMessageContentMask.Field.NetworkMessageHeader,
            JsonNetworkMessageContentMask.Field.PublisherId);

    assertStartupFailsWithConfigurationError(
        publisherConfig(networkMask, DELTA_CAPABLE_DSM_MASK, 3, true), "DataSetMessageHeader");
  }

  @Test
  void jsonWriterKeyFrameCountWithMessageTypeMaskStarts() throws Exception {
    createService(
        publisherConfig(JsonNetworkMessageContentMask.of(), DELTA_CAPABLE_DSM_MASK, 3, true));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(group));
  }

  /** Disabled writers are tolerated: imported configs keep round-tripping. */
  @Test
  void disabledJsonWriterKeyFrameCountToleratedAtStartup() throws Exception {
    createService(
        publisherConfig(
            JsonNetworkMessageContentMask.of(), JsonDataSetMessageContentMask.of(), 3, false));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Disabled, service.state(writer));
  }

  /**
   * The UADP counterpart of the rule: keyFrameCount > 1 cannot be combined with a non-zero
   * ConfiguredSize — the fixed-size layout is key-frame-only (Annex A.2.1.7), and an overflowing
   * delta would be valid-bit cleared (§6.3.1.3.3) yet still sent, silently losing the changed
   * values without ever triggering the not-transmitted baseline invalidation.
   */
  @Test
  void uadpWriterKeyFrameCountWithConfiguredSizeFailsStartup() {
    assertStartupFailsWithConfigurationError(uadpPublisherConfig(3, 64, true), "ConfiguredSize");
  }

  /**
   * A custom MessageMappingProvider registered under "uadp" shadows the built-in mapping and owns
   * its wire format: the built-in ConfiguredSize rule (and the JSON mask rules) must not
   * second-guess it. The same ConfiguredSize + keyFrameCount > 1 combination that {@link
   * #uadpWriterKeyFrameCountWithConfiguredSizeFailsStartup} pins as rejected for the built-in
   * mapping is accepted for the custom provider, and the writer goes Operational.
   */
  @Test
  void customUadpProviderKeyFrameCountWithConfiguredSizeStarts() throws Exception {
    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(new CapturingTransport())
            .messageMappingProvider(new StubUadpMapping())
            .build();

    service =
        PubSubService.create(
            uadpPublisherConfig(3, 64, true), constantSourceBindings(), serviceConfig);

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(writer));
  }

  /** ConfiguredSize itself stays supported: with keyFrameCount 1 (all key frames) it is fine. */
  @Test
  void uadpWriterConfiguredSizeWithKeyFrameCountOneStarts() throws Exception {
    createService(uadpPublisherConfig(1, 64, true));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(writer));
  }

  /** The UADP ConfiguredSize rule also applies to reconfiguration, rejected before applying. */
  @Test
  void reconfigureRejectsKeyFrameCountWithConfiguredSize() throws Exception {
    createService(uadpPublisherConfig(3, 0, true));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubConfig invalid = uadpPublisherConfig(3, 64, true);

    UaRuntimeException e =
        assertThrows(
            UaRuntimeException.class,
            () -> service.reconfigure(invalid, PubSubService.ReconfigureMode.DISABLE_AFFECTED));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("ConfiguredSize"), "unexpected message: " + e.getMessage());

    // the rejected config was not applied: the original writer handle is still valid
    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(writer));
  }

  /** The same rule applies to reconfiguration, rejected before anything is applied. */
  @Test
  void reconfigureRejectsKeyFrameCountWithoutMessageType() throws Exception {
    createService(
        publisherConfig(JsonNetworkMessageContentMask.of(), DELTA_CAPABLE_DSM_MASK, 3, true));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubConfig invalid =
        publisherConfig(
            JsonNetworkMessageContentMask.of(), JsonDataSetMessageContentMask.of(), 3, true);

    UaRuntimeException e =
        assertThrows(
            UaRuntimeException.class,
            () -> service.reconfigure(invalid, PubSubService.ReconfigureMode.DISABLE_AFFECTED));
    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("MessageType"), "unexpected message: " + e.getMessage());

    // the rejected config was not applied: the original writer handle is still valid
    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(writer));
  }

  /**
   * The degradation path: a writer whose masks cannot express delta frames that slips past
   * validation — disabled at startup (tolerated), then enabled at runtime — degrades to every-cycle
   * key frames. With a constant-valued source, suppression would otherwise silence it entirely and
   * a (mis-decoded) delta would carry a partial payload; instead every published DataSetMessage
   * keeps carrying all fields.
   */
  @Test
  void writerEnabledAfterStartupDegradesToEveryCycleKeyFrames() throws Exception {
    CapturingTransport transport =
        createService(
            publisherConfig(
                JsonNetworkMessageContentMask.of(), JsonDataSetMessageContentMask.of(), 3, false));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    service.enable(writer);
    assertEquals(PubSubState.Operational, service.state(writer));

    int dataMessages = 0;
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while (dataMessages < 3 && System.nanoTime() < deadline) {
      CapturingTransport.Sent sent = transport.sent.poll(1, TimeUnit.SECONDS);
      if (sent == null || sent.kind() != MessageAddress.Kind.DATA) {
        continue;
      }
      dataMessages++;

      JsonObject networkMessage = JsonParser.parseString(sent.payload()).getAsJsonObject();
      JsonArray messages = networkMessage.get("Messages").getAsJsonArray();
      assertEquals(1, messages.size());

      // every cycle publishes a full key frame payload: both fields, every time
      JsonObject payload = messages.get(0).getAsJsonObject().get("Payload").getAsJsonObject();
      assertEquals(2, payload.keySet().size(), "payload: " + payload);
      assertTrue(payload.has("counter"), "payload: " + payload);
      assertTrue(payload.has("constant"), "payload: " + payload);
    }

    assertEquals(3, dataMessages, "expected three data NetworkMessages");
    assertNotNull(service);
  }
}
