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
import io.netty.buffer.Unpooled;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent;
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
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Startup/reconfigure/activation validation of the UADP emission features the built-in mapping does
 * not implement: the group-level PromotedFields UadpNetworkMessageContentMask bit (Part 14
 * §6.3.1.1.4) and the writer-level RawData DataSetFieldContentMask bit (§6.2.4.2, the distinct
 * §7.2.4.5.11 fixed-layout field encoding). Startup fails with {@code Bad_NotSupported}, {@code
 * reconfigure} rejects with {@code UaRuntimeException(Bad_NotSupported)} before applying anything,
 * and a component that slips past validation — disabled at startup (tolerated, round-trip posture),
 * then enabled at runtime, whether the connection, the group, or the writer is the disabled one —
 * fails its activation into {@code PubSubState.Error} with {@code Bad_NotSupported} instead of
 * sitting Operational while the encoder backstop rejects every publish cycle.
 *
 * <p>Both rules apply only when the group is UADP-mapped and the "uadp" mapping name resolves to
 * the built-in provider: a custom MessageMappingProvider shadowing "uadp" owns its wire format and
 * is never second-guessed. JSON-mapped writers are untouched — their RawData bit is a legitimate,
 * implemented Variant-representation modifier (§7.2.5.4.3), and JsonNetworkMessageContentMask has
 * no PromotedFields bit.
 */
class UnsupportedUadpFeatureValidationTest {

  private static final UUID COUNTER_FIELD_ID = new UUID(0L, 1L);

  private @Nullable PubSubService service;

  @AfterEach
  void shutdownService() throws Exception {
    if (service != null) {
      service.close();
      service = null;
    }
  }

  // region fixture

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
   * owns its wire format, so the built-in PromotedFields/RawData rules must not apply to it.
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
   * One MQTT connection, one UADP writer group, one writer on a one-field dataset with a bound
   * (constant-valued) source; the offending mask bits and enabled flags are parameters.
   */
  private static PubSubConfig uadpPublisherConfig(
      boolean promotedFields, boolean rawData, boolean groupEnabled, boolean writerEnabled) {

    return uadpPublisherConfig(promotedFields, rawData, true, groupEnabled, writerEnabled);
  }

  private static PubSubConfig uadpPublisherConfig(
      boolean promotedFields,
      boolean rawData,
      boolean connectionEnabled,
      boolean groupEnabled,
      boolean writerEnabled) {

    // the Annex A.2.2 UADP-Dynamic default bits, plus PromotedFields (bit 10) when requested
    UadpNetworkMessageContentMask networkMask =
        promotedFields
            ? UadpNetworkMessageContentMask.of(
                UadpNetworkMessageContentMask.Field.PublisherId,
                UadpNetworkMessageContentMask.Field.PayloadHeader,
                UadpNetworkMessageContentMask.Field.PromotedFields)
            : UadpNetworkMessageContentMask.of(
                UadpNetworkMessageContentMask.Field.PublisherId,
                UadpNetworkMessageContentMask.Field.PayloadHeader);

    DataSetFieldContentMask fieldMask =
        rawData
            ? DataSetFieldContentMask.of(DataSetFieldContentMask.Field.RawData)
            : DataSetFieldContentMask.of();

    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("PDS")
            .field(
                FieldDefinition.builder("counter")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(COUNTER_FIELD_ID)
                    .build())
            .build();

    MqttConnectionConfig connection =
        MqttConnectionConfig.builder("conn")
            .brokerUri(URI.create("mqtt://localhost:1883"))
            .publisherId(PublisherId.string("line-7"))
            .enabled(connectionEnabled)
            .writerGroup(
                WriterGroupConfig.builder("WG")
                    .enabled(groupEnabled)
                    .writerGroupId(ushort(1))
                    .publishingInterval(Duration.ofMillis(25))
                    .messageSettings(
                        UadpWriterGroupSettings.builder()
                            .networkMessageContentMask(networkMask)
                            .build())
                    .dataSetWriter(
                        DataSetWriterConfig.builder("W1")
                            .enabled(writerEnabled)
                            .dataSet(new PublishedDataSetRef("PDS"))
                            .dataSetWriterId(ushort(1))
                            .fieldContentMask(fieldMask)
                            .settings(UadpDataSetWriterSettings.builder().build())
                            .build())
                    .build())
            .build();

    return PubSubConfig.builder().publishedDataSet(dataSet).connection(connection).build();
  }

  /** The same shape, JSON-mapped, with the RawData bit set on the writer's field content mask. */
  private static PubSubConfig jsonRawDataPublisherConfig() {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder("PDS")
            .field(
                FieldDefinition.builder("counter")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(COUNTER_FIELD_ID)
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
                            .dataSet(new PublishedDataSetRef("PDS"))
                            .dataSetWriterId(ushort(1))
                            .fieldContentMask(
                                DataSetFieldContentMask.of(DataSetFieldContentMask.Field.RawData))
                            .settings(JsonDataSetWriterSettings.builder().build())
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
                .build();

    return PubSubBindings.builder().source(new PublishedDataSetRef("PDS"), source).build();
  }

  private void createService(PubSubConfig config) {
    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder().transportProvider(new DiscardingTransport()).build();

    service = PubSubService.create(config, constantSourceBindings(), serviceConfig);
  }

  private void assertStartupFailsWithNotSupported(PubSubConfig config, String... fragments) {
    createService(config);

    ExecutionException e =
        assertThrows(ExecutionException.class, () -> service.startup().get(10, TimeUnit.SECONDS));
    UaException cause = assertInstanceOf(UaException.class, e.getCause());
    assertEquals(StatusCodes.Bad_NotSupported, cause.getStatusCode().value());
    for (String fragment : fragments) {
      assertTrue(
          cause.getMessage().contains(fragment), "unexpected message: " + cause.getMessage());
    }
  }

  private static PubSubStateChangeEvent awaitEvent(
      BlockingQueue<PubSubStateChangeEvent> events, Predicate<PubSubStateChangeEvent> predicate)
      throws InterruptedException {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while (System.nanoTime() < deadline) {
      PubSubStateChangeEvent event = events.poll(100, TimeUnit.MILLISECONDS);
      if (event != null && predicate.test(event)) {
        return event;
      }
    }
    return fail("timed out waiting for state change event");
  }

  // endregion

  @Test
  void uadpGroupWithPromotedFieldsFailsStartup() {
    assertStartupFailsWithNotSupported(
        uadpPublisherConfig(true, false, true, true), "PromotedFields", "conn/WG");
  }

  @Test
  void uadpWriterWithRawDataFailsStartup() {
    assertStartupFailsWithNotSupported(
        uadpPublisherConfig(false, true, true, true), "RawData", "conn/WG/W1");
  }

  /** Disabled components are tolerated: imported configs keep round-tripping and starting. */
  @Test
  void disabledUadpGroupWithPromotedFieldsToleratedAtStartup() throws Exception {
    createService(uadpPublisherConfig(true, true, false, true));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.Disabled, service.state(group));
  }

  @Test
  void disabledUadpWriterWithRawDataToleratedAtStartup() throws Exception {
    createService(uadpPublisherConfig(false, true, true, false));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(group));
    assertEquals(PubSubState.Disabled, service.state(writer));
  }

  /**
   * The masks are valid Part 14 values and must keep round-tripping through the config mappers:
   * fail-fast lives at the service-validation layer, not in the builders or mappers.
   */
  @Test
  void promotedFieldsAndRawDataSurviveConfigRoundTrip() {
    var table = new NamespaceTable();
    PubSubConfig config = uadpPublisherConfig(true, true, false, false);

    PubSubConfig roundTripped = PubSubConfig.fromDataType(config.toDataType(table), table);

    WriterGroupConfig group = roundTripped.connection("conn").orElseThrow().writerGroups().get(0);
    var groupSettings = assertInstanceOf(UadpWriterGroupSettings.class, group.getMessageSettings());
    assertTrue(groupSettings.getNetworkMessageContentMask().getPromotedFields());
    assertTrue(group.getDataSetWriters().get(0).getFieldContentMask().getRawData());
    assertEquals(config, roundTripped);
  }

  /** Reconfigure rejects before applying: the running configuration stays untouched. */
  @Test
  void reconfigureRejectsPromotedFields() throws Exception {
    createService(uadpPublisherConfig(false, false, true, true));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubConfig invalid = uadpPublisherConfig(true, false, true, true);

    UaRuntimeException e =
        assertThrows(
            UaRuntimeException.class,
            () -> service.reconfigure(invalid, PubSubService.ReconfigureMode.DISABLE_AFFECTED));
    assertEquals(StatusCodes.Bad_NotSupported, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("PromotedFields"), "unexpected message: " + e.getMessage());
    assertTrue(e.getMessage().contains("conn/WG"), "unexpected message: " + e.getMessage());

    // the rejected config was not applied: the original writer handle is still valid
    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(writer));
  }

  @Test
  void reconfigureRejectsRawDataWriter() throws Exception {
    createService(uadpPublisherConfig(false, false, true, true));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubConfig invalid = uadpPublisherConfig(false, true, true, true);

    UaRuntimeException e =
        assertThrows(
            UaRuntimeException.class,
            () -> service.reconfigure(invalid, PubSubService.ReconfigureMode.DISABLE_AFFECTED));
    assertEquals(StatusCodes.Bad_NotSupported, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("RawData"), "unexpected message: " + e.getMessage());
    assertTrue(e.getMessage().contains("conn/WG/W1"), "unexpected message: " + e.getMessage());

    // the rejected config was not applied: the original writer handle is still valid
    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(writer));
  }

  /**
   * The activation backstop: a tolerated-disabled group enabled after startup is first seen by the
   * {@code WriterGroupRuntime.activate()} re-check and fails into {@code PubSubState.Error} with
   * {@code Bad_NotSupported} (Part 14 §6.2.1: "PubSubState shall be Error"), instead of sitting
   * Operational while the encoder backstop rejects every publish cycle.
   */
  @Test
  void groupWithPromotedFieldsEnabledAfterStartupFailsIntoError() throws Exception {
    createService(uadpPublisherConfig(true, false, false, true));

    service.startup().get(10, TimeUnit.SECONDS);

    var stateEvents = new LinkedBlockingQueue<PubSubStateChangeEvent>();
    service.addStateListener(stateEvents::add);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    service.enable(group);

    assertEquals(PubSubState.Error, service.state(group));

    PubSubStateChangeEvent event =
        awaitEvent(
            stateEvents,
            e -> e.component().path().equals("conn/WG") && e.newState() == PubSubState.Error);
    assertEquals(new StatusCode(StatusCodes.Bad_NotSupported), event.statusCode());
  }

  @Test
  void groupWithRawDataWriterEnabledAfterStartupFailsIntoError() throws Exception {
    createService(uadpPublisherConfig(false, true, false, true));

    service.startup().get(10, TimeUnit.SECONDS);

    var stateEvents = new LinkedBlockingQueue<PubSubStateChangeEvent>();
    service.addStateListener(stateEvents::add);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    service.enable(group);

    assertEquals(PubSubState.Error, service.state(group));

    PubSubStateChangeEvent event =
        awaitEvent(
            stateEvents,
            e -> e.component().path().equals("conn/WG") && e.newState() == PubSubState.Error);
    assertEquals(new StatusCode(StatusCodes.Bad_NotSupported), event.statusCode());
  }

  /**
   * The writer-level activation backstop: a tolerated-disabled RawData writer enabled while its
   * group is already Operational is first seen by the {@code DataSetWriterRuntime.activate()}
   * re-check and fails into {@code PubSubState.Error} with {@code Bad_NotSupported}, instead of
   * going Operational while the encoder backstop rejects every publish cycle; the group stays
   * Operational.
   */
  @Test
  void rawDataWriterEnabledAfterGroupOperationalFailsIntoError() throws Exception {
    createService(uadpPublisherConfig(false, true, true, false));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(group));

    var stateEvents = new LinkedBlockingQueue<PubSubStateChangeEvent>();
    service.addStateListener(stateEvents::add);

    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    service.enable(writer);

    assertEquals(PubSubState.Error, service.state(writer));
    assertEquals(PubSubState.Operational, service.state(group));

    PubSubStateChangeEvent event =
        awaitEvent(
            stateEvents,
            e -> e.component().path().equals("conn/WG/W1") && e.newState() == PubSubState.Error);
    assertEquals(new StatusCode(StatusCodes.Bad_NotSupported), event.statusCode());
  }

  /**
   * The same backstop one level up: enabling a tolerated-disabled connection that contains an
   * offending enabled group activates the connection and then fails the group's activation into
   * {@code PubSubState.Error} with {@code Bad_NotSupported}.
   */
  @Test
  void enablingConnectionFailsOffendingGroupIntoError() throws Exception {
    createService(uadpPublisherConfig(true, false, false, true, true));

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle connection = service.components().connection("conn").orElseThrow();
    PubSubHandle group = service.components().writerGroup("conn", "WG").orElseThrow();
    assertEquals(PubSubState.Disabled, service.state(connection));

    var stateEvents = new LinkedBlockingQueue<PubSubStateChangeEvent>();
    service.addStateListener(stateEvents::add);

    service.enable(connection);

    assertEquals(PubSubState.Operational, service.state(connection));
    assertEquals(PubSubState.Error, service.state(group));

    PubSubStateChangeEvent event =
        awaitEvent(
            stateEvents,
            e -> e.component().path().equals("conn/WG") && e.newState() == PubSubState.Error);
    assertEquals(new StatusCode(StatusCodes.Bad_NotSupported), event.statusCode());
  }

  /**
   * JSON regression: the RawData bit under the JSON mapping is a legitimate, implemented
   * Variant-representation modifier (Part 14 §7.2.5.4.3, Table 112) — never a reason to reject.
   */
  @Test
  void jsonWriterWithRawDataStarts() throws Exception {
    createService(jsonRawDataPublisherConfig());

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(writer));
  }

  /**
   * A custom MessageMappingProvider registered under "uadp" shadows the built-in mapping and owns
   * its wire format: it may support PromotedFields and RawData emission, so the built-in rules must
   * not second-guess it. The same configuration that {@link
   * #uadpGroupWithPromotedFieldsFailsStartup} and {@link #uadpWriterWithRawDataFailsStartup} pin as
   * rejected for the built-in mapping is accepted, and the writer goes Operational.
   */
  @Test
  void customUadpProviderWithPromotedFieldsAndRawDataStarts() throws Exception {
    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .transportProvider(new DiscardingTransport())
            .messageMappingProvider(new StubUadpMapping())
            .build();

    service =
        PubSubService.create(
            uadpPublisherConfig(true, true, true, true), constantSourceBindings(), serviceConfig);

    service.startup().get(10, TimeUnit.SECONDS);

    PubSubHandle writer = service.components().dataSetWriter("conn", "WG", "W1").orElseThrow();
    assertEquals(PubSubState.Operational, service.state(writer));
  }
}
