/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetListener;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubComponents;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService.ReconfigureMode;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateListener;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.SecurityKeyInfo;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.sdk.server.EventListener;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ElementOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Coverage for {@link PubSubEventNotifierBinding}, the server adapter that bridges the server's
 * global event bus into the PubSub runtime. All tests need real {@code EventFactory}-created event
 * nodes and synchronous {@code EventContentFilter} evaluate/select, so they run against a STARTED
 * {@link SksTestServer} (the {@link PubSubStatusEventBridgeTest} precedent); {@code
 * TestPubSubServer} is never started and cannot host these.
 *
 * <p>Most tests drive the binding directly — constructed with a capturing {@link PubSubService}
 * fake whose {@code publishEvent} records the ref and selected field Variants — and fire events
 * through {@code server.getEventNotifier().fire(...)}. The dataset-level active/inactive flag is
 * read back through the package-private {@link PubSubEventNotifierBinding#bindings()}. Because no
 * client ever connects, the event bus is quiet, so captured counts are exact (same posture as
 * {@link PubSubStatusEventBridgeTest}).
 *
 * <p>The primary end-to-end (UDP/UADP) instead attaches a real {@link ServerPubSub} publisher and a
 * separate {@link PubSubService} subscriber over unicast loopback UDP and proves a fired {@code
 * BaseEvent} arrives at the reader as a {@link DataSetMessageKind#EVENT} DataSetMessage carrying
 * the selected field values.
 */
class PubSubEventNotifierBindingTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4762));

  private static final UUID SOURCE_NAME_FIELD_ID = new UUID(0L, 0xE1L);
  private static final UUID SEVERITY_FIELD_ID = new UUID(0L, 0xE2L);

  private static SksTestServer testServer;
  private static OpcUaServer server;
  private static NotifierFixtureNamespace notifierNamespace;

  private CapturingPubSubService service;
  private final List<PubSubEventNotifierBinding> startedBindings = new ArrayList<>();

  @BeforeAll
  static void startServer() throws Exception {
    testServer = SksTestServer.create(null);
    server = testServer.getServer();

    notifierNamespace = new NotifierFixtureNamespace(server);
    notifierNamespace.startup();
    notifierNamespace.build();
  }

  @AfterAll
  static void stopServer() throws Exception {
    notifierNamespace.shutdown();
    testServer.close();
  }

  @BeforeEach
  void setUp() {
    service = new CapturingPubSubService();
  }

  @AfterEach
  void tearDown() {
    for (PubSubEventNotifierBinding binding : startedBindings) {
      binding.shutdown();
    }
    startedBindings.clear();
  }

  // region primary end-to-end (UDP/UADP)

  /**
   * The primary end-to-end path for the server adapter: a {@code BaseEvent} fired under the {@code
   * Server} notifier on a running {@link ServerPubSub} publisher is selected by the notifier
   * binding, pushed to the runtime via {@code publishEvent}, and arrives at a separate {@link
   * PubSubService} subscriber over unicast loopback UDP as a {@link DataSetMessageKind#EVENT}
   * DataSetMessage carrying the selected {@code SourceName}/{@code Severity} field values.
   */
  @Test
  void firedServerEventReachesSubscriberAsEventKind() throws Exception {
    int port = freeUdpPort();

    PublishedDataSetConfig dataSet =
        eventDataSet(
            "evt-ds",
            NodeIds.Server.expanded(),
            null,
            eventField("sourceName", "SourceName", NodeIds.String),
            eventField("severity", "Severity", NodeIds.UInt16));

    ServerPubSub serverPubSub = ServerPubSub.attach(server, publisherConfig(dataSet, port));
    try {
      serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

      var events = new LinkedBlockingQueue<DataSetReceivedEvent>();
      PubSubService subscriber =
          PubSubService.create(
              subscriberConfig(port),
              PubSubBindings.builder()
                  .listener(new DataSetReaderRef("sub-conn", "rgrp", "reader"), events::add)
                  .build());
      try {
        subscriber.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

        // event publishing is one-shot (unlike cyclic data): re-fire until a match is received so a
        // single lost loopback datagram cannot flake the test
        DataSetReceivedEvent event = null;
        long deadline = System.nanoTime() + TIMEOUT.toNanos();
        while (event == null && System.nanoTime() < deadline) {
          fireEvent(NodeIds.BaseEventType, NodeIds.Server, "kaboom", ushort(700));
          event = events.poll(250, TimeUnit.MILLISECONDS);
        }

        assertNotNull(event, "fired server event never arrived at the subscriber");
        assertEquals(DataSetMessageKind.EVENT, event.kind());
        assertEquals("kaboom", event.fieldsByName().get("sourceName").value().value());
        assertEquals(ushort(700), event.fieldsByName().get("severity").value().value());
      } finally {
        subscriber.close();
      }
    } finally {
      serverPubSub.close();
    }
  }

  // endregion

  // region notifier validation

  /**
   * A notifier that is a managed {@code Object} but whose {@code EventNotifier} attribute lacks the
   * {@code SubscribeToEvents} bit yields an inactive binding: startup succeeds, no WARN aborts it,
   * and no event is ever published.
   */
  @Test
  void notifierWithoutSubscribeToEventsBitYieldsInactiveBinding() {
    PublishedDataSetConfig dataSet =
        eventDataSet(
            "no-bit",
            notifierNamespace.noBitNotifier.expanded(server.getNamespaceTable()),
            null,
            eventField("sourceName", "SourceName", NodeIds.String));

    PubSubEventNotifierBinding binding = startBinding(dataSet);

    assertFalse(binding.bindings().get("no-bit").active());

    fireEvent(NodeIds.BaseEventType, NodeIds.Server, "s", ushort(1));
    assertTrue(service.published.isEmpty(), "an inactive binding must not publish");
  }

  /**
   * A notifier {@code ExpandedNodeId} that resolves to no managed node yields an inactive binding.
   */
  @Test
  void missingNotifierNodeYieldsInactiveBinding() {
    PublishedDataSetConfig dataSet =
        eventDataSet(
            "missing",
            ExpandedNodeId.of(NotifierFixtureNamespace.URI, "NoSuchNode"),
            null,
            eventField("sourceName", "SourceName", NodeIds.String));

    PubSubEventNotifierBinding binding = startBinding(dataSet);

    assertFalse(binding.bindings().get("missing").active());

    fireEvent(NodeIds.BaseEventType, NodeIds.Server, "s", ushort(1));
    assertTrue(service.published.isEmpty(), "an inactive binding must not publish");
  }

  // endregion

  // region filter validation

  /**
   * A where-clause whose only element carries an {@link ElementOperand} referencing an
   * out-of-bounds element index validates as Good but would throw {@code ArrayIndexOutOfBounds} on
   * evaluate, so the binding is inactive (the bounds check {@code EventContentFilter.validate} does
   * not make).
   */
  @Test
  void outOfBoundsElementOperandYieldsInactiveBinding() {
    ExtensionObject operand =
        ExtensionObject.encode(server.getStaticEncodingContext(), new ElementOperand(uint(1)));
    ContentFilter whereClause =
        new ContentFilter(
            new ContentFilterElement[] {
              new ContentFilterElement(FilterOperator.Not, new ExtensionObject[] {operand})
            });

    PublishedDataSetConfig dataSet =
        eventDataSet(
            "oob",
            NodeIds.Server.expanded(),
            whereClause,
            eventField("sourceName", "SourceName", NodeIds.String));

    PubSubEventNotifierBinding binding = startBinding(dataSet);

    assertFalse(binding.bindings().get("oob").active());

    fireEvent(NodeIds.BaseEventType, NodeIds.Server, "s", ushort(1));
    assertTrue(service.published.isEmpty(), "an inactive binding must not publish");
  }

  /**
   * A where-clause whose root element's operator is not Boolean-producing ({@code Cast} returns a
   * value, not a {@code Boolean}) yields an inactive binding — {@code EventContentFilter.evaluate}
   * requires a Boolean root.
   */
  @Test
  void nonBooleanRootOperatorYieldsInactiveBinding() {
    ExtensionObject value =
        ExtensionObject.encode(
            server.getStaticEncodingContext(), new LiteralOperand(Variant.ofInt32(1)));
    ExtensionObject targetType =
        ExtensionObject.encode(
            server.getStaticEncodingContext(), new LiteralOperand(new Variant(NodeIds.Double)));
    ContentFilter whereClause =
        new ContentFilter(
            new ContentFilterElement[] {
              new ContentFilterElement(
                  FilterOperator.Cast, new ExtensionObject[] {value, targetType})
            });

    PublishedDataSetConfig dataSet =
        eventDataSet(
            "cast",
            NodeIds.Server.expanded(),
            whereClause,
            eventField("sourceName", "SourceName", NodeIds.String));

    PubSubEventNotifierBinding binding = startBinding(dataSet);

    assertFalse(binding.bindings().get("cast").active());

    fireEvent(NodeIds.BaseEventType, NodeIds.Server, "s", ushort(1));
    assertTrue(service.published.isEmpty(), "an inactive binding must not publish");
  }

  /**
   * A valid where-clause selectively filters events: an {@code OfType(AuditEventType)} filter
   * admits only events whose type is a subtype of {@code AuditEventType}, so a fired {@code
   * AuditEventType} event is published while a plain {@code BaseEventType} event is not.
   */
  @Test
  void whereClauseSelectivelyFiltersEvents() {
    ExtensionObject operand =
        ExtensionObject.encode(
            server.getStaticEncodingContext(),
            new LiteralOperand(new Variant(NodeIds.AuditEventType)));
    ContentFilter whereClause =
        new ContentFilter(
            new ContentFilterElement[] {
              new ContentFilterElement(FilterOperator.OfType, new ExtensionObject[] {operand})
            });

    PublishedDataSetConfig dataSet =
        eventDataSet(
            "of-type",
            NodeIds.Server.expanded(),
            whereClause,
            eventField("sourceName", "SourceName", NodeIds.String));

    PubSubEventNotifierBinding binding = startBinding(dataSet);
    assertTrue(binding.bindings().get("of-type").active());

    fireEvent(NodeIds.AuditEventType, NodeIds.Server, "audit", ushort(1));
    fireEvent(NodeIds.BaseEventType, NodeIds.Server, "base", ushort(1));

    assertEquals(1, service.published.size(), "only the matching event is published");
    assertEquals("audit", service.published.get(0).fields().get(0).value());
  }

  // endregion

  // region scoping

  /** The {@code Server} notifier ({@code i=2253}) is unscoped: every fired event passes. */
  @Test
  void serverNotifierIsUnscopedAndPassesAllEvents() {
    PublishedDataSetConfig dataSet =
        eventDataSet(
            "unscoped",
            NodeIds.Server.expanded(),
            null,
            eventField("sourceName", "SourceName", NodeIds.String));

    PubSubEventNotifierBinding binding = startBinding(dataSet);
    assertTrue(binding.bindings().get("unscoped").active());

    // two events with unrelated SourceNodes both pass — scope is null (unscoped)
    fireEvent(NodeIds.BaseEventType, NodeIds.ObjectsFolder, "a", ushort(1));
    fireEvent(NodeIds.BaseEventType, notifierNamespace.scopedChild, "b", ushort(1));

    assertEquals(2, service.published.size(), "an unscoped notifier passes every event");
  }

  /**
   * A non-{@code Server} notifier with the {@code SubscribeToEvents} bit scopes events to its
   * {@code HasNotifier}/{@code HasEventSource} subtree: only events whose {@code SourceNode} is the
   * notifier itself or a node reachable through that subtree pass; others are dropped.
   */
  @Test
  void nonServerNotifierScopesEventsToItsEventSourceSubtree() {
    PublishedDataSetConfig dataSet =
        eventDataSet(
            "scoped",
            notifierNamespace.scopedNotifier.expanded(server.getNamespaceTable()),
            null,
            eventField("sourceName", "SourceName", NodeIds.String));

    PubSubEventNotifierBinding binding = startBinding(dataSet);
    assertTrue(binding.bindings().get("scoped").active());

    // in scope: the notifier's HasNotifier child, and the notifier node itself
    fireEvent(NodeIds.BaseEventType, notifierNamespace.scopedChild, "child", ushort(1));
    fireEvent(NodeIds.BaseEventType, notifierNamespace.scopedNotifier, "notifier", ushort(1));
    // out of scope: an unrelated node
    fireEvent(NodeIds.BaseEventType, NodeIds.ObjectsFolder, "outside", ushort(1));

    Set<Object> sourceNames = new HashSet<>();
    for (CapturingPubSubService.Published published : service.published) {
      sourceNames.add(published.fields().get(0).value());
    }
    assertEquals(Set.of("child", "notifier"), sourceNames);
  }

  // endregion

  // region synchronous selection

  /**
   * Fields are selected synchronously inside {@code onEvent}, before the transient event node is
   * deleted: the fire helper deletes the node the instant {@code fire} returns, so the captured
   * field values can only be correct if selection ran during the synchronous {@code onEvent}
   * delivery.
   */
  @Test
  void fieldsAreSelectedSynchronouslyBeforeEventNodeDeletion() {
    PublishedDataSetConfig dataSet =
        eventDataSet(
            "sync",
            NodeIds.Server.expanded(),
            null,
            eventField("sourceName", "SourceName", NodeIds.String),
            eventField("severity", "Severity", NodeIds.UInt16));

    startBinding(dataSet);

    fireEvent(NodeIds.BaseEventType, NodeIds.Server, "synchronous", ushort(321));

    assertEquals(1, service.published.size());
    List<Variant> fields = service.published.get(0).fields();
    assertEquals("synchronous", fields.get(0).value());
    assertEquals(ushort(321), fields.get(1).value());
  }

  // endregion

  // region exception containment

  /**
   * A binding whose selection/publish step throws is deactivated after a single WARN and does not
   * break the event bus: a second, independent listener still receives both events, and the binding
   * stops publishing after the failure (only the first event was attempted).
   */
  @Test
  void bindingIsDeactivatedAfterASelectionFailureWithoutBreakingTheBus() {
    PublishedDataSetConfig dataSet =
        eventDataSet(
            "boom",
            NodeIds.Server.expanded(),
            null,
            eventField("sourceName", "SourceName", NodeIds.String));

    PubSubEventNotifierBinding binding = startBinding(dataSet);
    service.throwOnPublish = true;

    var otherListenerCount = new int[1];
    EventListener otherListener = node -> otherListenerCount[0]++;
    server.getEventNotifier().register(otherListener);
    try {
      // first event: onEvent selects and calls publishEvent, which throws; the binding contains the
      // Throwable, deactivates itself, and never lets it escape into the producer thread
      fireEvent(NodeIds.BaseEventType, NodeIds.Server, "first", ushort(1));
      assertFalse(binding.bindings().get("boom").active(), "binding deactivated after the throw");
      assertEquals(1, service.published.size(), "publish was attempted exactly once");

      // second event: the now-inactive binding publishes nothing more...
      fireEvent(NodeIds.BaseEventType, NodeIds.Server, "second", ushort(1));
      assertEquals(1, service.published.size(), "no further publishes after deactivation");

      // ...yet the bus stayed healthy: the independent listener saw both events
      assertEquals(2, otherListenerCount[0], "the failing binding must not break other listeners");
    } finally {
      server.getEventNotifier().unregister(otherListener);
    }
  }

  // endregion

  // region fixtures

  private PubSubEventNotifierBinding startBinding(PublishedDataSetConfig dataSet) {
    PubSubConfig config = PubSubConfig.builder().publishedDataSet(dataSet).build();
    var binding = new PubSubEventNotifierBinding(server, service);
    binding.startup(config);
    startedBindings.add(binding);
    return binding;
  }

  /**
   * An event field selecting the BaseEventType field {@code browseName} under the name {@code
   * name}.
   */
  private static EventFieldDefinition eventField(String name, String browseName, NodeId dataType) {
    return EventFieldDefinition.builder(name)
        .selectedField(
            new SimpleAttributeOperand(
                NodeIds.BaseEventType,
                new QualifiedName[] {new QualifiedName(0, browseName)},
                AttributeId.Value.uid(),
                null))
        .dataType(dataType)
        .build();
  }

  private static PublishedDataSetConfig eventDataSet(
      String name,
      ExpandedNodeId eventNotifier,
      @Nullable ContentFilter filter,
      EventFieldDefinition... fields) {

    PublishedEventsConfig.Builder events = PublishedEventsConfig.builder(eventNotifier);
    for (EventFieldDefinition field : fields) {
      events.field(field);
    }
    if (filter != null) {
      events.filter(filter);
    }
    return PublishedDataSetConfig.builder(name).source(events.build()).build();
  }

  /** Create, fire, and delete a BaseEventType-shaped event of {@code eventType}. */
  private void fireEvent(NodeId eventType, NodeId sourceNode, String sourceName, UShort severity) {
    BaseEventTypeNode eventNode;
    try {
      eventNode = server.getEventFactory().createEvent(new NodeId(1, UUID.randomUUID()), eventType);
    } catch (UaException e) {
      throw new RuntimeException(e);
    }
    try {
      eventNode.setBrowseName(new QualifiedName(1, "evt"));
      eventNode.setDisplayName(LocalizedText.english("evt"));
      eventNode.setEventId(
          ByteString.of(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
      eventNode.setEventType(eventType);
      eventNode.setSourceNode(sourceNode);
      eventNode.setSourceName(sourceName);
      eventNode.setTime(DateTime.now());
      eventNode.setReceiveTime(DateTime.NULL_VALUE);
      eventNode.setMessage(LocalizedText.english(sourceName));
      eventNode.setSeverity(severity);

      server.getEventNotifier().fire(eventNode);
    } finally {
      eventNode.delete();
    }
  }

  /**
   * A publisher-only loopback configuration for the event dataset: unicast 127.0.0.1 with ephemeral
   * ports and an explicit loopback discovery address, one 50 ms writer group with a single event
   * writer, GroupHeader/WriterGroupId/SequenceNumber on the wire so the reader can filter and
   * sequence.
   */
  private static PubSubConfig publisherConfig(PublishedDataSetConfig dataSet, int port)
      throws SocketException {

    return PubSubConfig.builder()
        .publishedDataSet(dataSet)
        .connection(
            PubSubConnectionConfig.udp("pub-conn")
                .publisherId(PUBLISHER_ID)
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(
                    WriterGroupConfig.builder("grp")
                        .writerGroupId(ushort(1))
                        .publishingInterval(Duration.ofMillis(50))
                        .messageSettings(
                            UadpWriterGroupSettings.builder()
                                .networkMessageContentMask(
                                    UadpNetworkMessageContentMask.of(
                                        UadpNetworkMessageContentMask.Field.PublisherId,
                                        UadpNetworkMessageContentMask.Field.GroupHeader,
                                        UadpNetworkMessageContentMask.Field.WriterGroupId,
                                        UadpNetworkMessageContentMask.Field.SequenceNumber,
                                        UadpNetworkMessageContentMask.Field.PayloadHeader))
                                .build())
                        .dataSetWriter(
                            DataSetWriterConfig.builder("writer")
                                .dataSet(dataSet.ref())
                                .dataSetWriterId(ushort(1))
                                .settings(
                                    UadpDataSetWriterSettings.builder()
                                        .dataSetMessageContentMask(
                                            UadpDataSetMessageContentMask.of(
                                                UadpDataSetMessageContentMask.Field.SequenceNumber))
                                        .build())
                                .build())
                        .build())
                .build())
        .build();
  }

  /**
   * Subscriber matching {@link #publisherConfig}: one REQUIRE_CONFIGURED reader on the event
   * dataset.
   */
  private static PubSubConfig subscriberConfig(int port) throws SocketException {
    DataSetMetaDataConfig metaData =
        DataSetMetaDataConfig.builder("evt-ds")
            .field("sourceName", NodeIds.String, SOURCE_NAME_FIELD_ID)
            .field("severity", NodeIds.UInt16, SEVERITY_FIELD_ID)
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("sub-conn")
                .address(UdpDatagramAddress.unicast("127.0.0.1", port))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .readerGroup(
                    ReaderGroupConfig.builder("rgrp")
                        .dataSetReader(
                            DataSetReaderConfig.builder("reader")
                                .publisherId(PUBLISHER_ID)
                                .writerGroupId(ushort(1))
                                .dataSetWriterId(ushort(1))
                                .dataSetMetaData(metaData)
                                .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                .build())
                        .build())
                .build())
        .build();
  }

  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion

  // region test doubles

  /**
   * A {@link PubSubService} that records every {@code publishEvent} call (ref + selected field
   * Variants) and, when {@link #throwOnPublish} is set, records the call then throws to exercise
   * the binding's exception containment. Every other method is unused by the binding and
   * unsupported.
   */
  private static final class CapturingPubSubService implements PubSubService {

    record Published(PublishedDataSetRef ref, List<Variant> fields) {}

    final List<Published> published = new CopyOnWriteArrayList<>();
    volatile boolean throwOnPublish = false;

    @Override
    public void publishEvent(PublishedDataSetRef dataSet, List<Variant> fields) {
      published.add(new Published(dataSet, List.copyOf(fields)));
      if (throwOnPublish) {
        throw new RuntimeException("simulated publish failure");
      }
    }

    @Override
    public CompletableFuture<PubSubService> startup() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Void> shutdown() {
      throw new UnsupportedOperationException();
    }

    @Override
    public PubSubComponents components() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void enable(PubSubHandle handle) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void disable(PubSubHandle handle) {
      throw new UnsupportedOperationException();
    }

    @Override
    public org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState state(
        PubSubHandle handle) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ReconfigureResult reconfigure(PubSubConfig newConfig, ReconfigureMode mode) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ReconfigureResult update(UnaryOperator<PubSubConfig> transform) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void bindSource(PublishedDataSetRef dataSet, PublishedDataSetSource source) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addDataSetListener(DataSetListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeDataSetListener(DataSetListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addDataSetListener(DataSetReaderRef reader, DataSetListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeDataSetListener(DataSetReaderRef reader, DataSetListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addStateListener(PubSubStateListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeStateListener(PubSubStateListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addMetaDataListener(MetaDataListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeMetaDataListener(MetaDataListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addDiagnosticsListener(PubSubDiagnosticsListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeDiagnosticsListener(PubSubDiagnosticsListener listener) {
      throw new UnsupportedOperationException();
    }

    @Override
    public PubSubDiagnostics diagnostics() {
      throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable SecurityKeyInfo securityKeyInfo(PubSubHandle group) {
      throw new UnsupportedOperationException();
    }

    @Override
    public UInteger nextDataSetMessageSequenceNumber(PubSubHandle writer) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * A managed namespace minting the object notifiers the non-{@code Server} tests need: a notifier
   * lacking the {@code SubscribeToEvents} bit, and a scoped notifier (bit set) with a {@code
   * HasNotifier} child object defining its event-source subtree. {@code TestVariablesNamespace} has
   * no such tree, so these are minted here (per the D-11 fixture note).
   */
  private static final class NotifierFixtureNamespace extends ManagedNamespaceWithLifecycle {

    static final String URI = "urn:eclipse:milo:pubsub:test:notifiers";

    final NodeId noBitNotifier;
    final NodeId scopedNotifier;
    final NodeId scopedChild;

    NotifierFixtureNamespace(OpcUaServer server) {
      super(server, URI);
      this.noBitNotifier = newNodeId("NoBitNotifier");
      this.scopedNotifier = newNodeId("ScopedNotifier");
      this.scopedChild = newNodeId("ScopedChild");
    }

    void build() {
      UaObjectNode noBit = objectNode(noBitNotifier, "NoBitNotifier", ubyte(0));
      UaObjectNode notifier = objectNode(scopedNotifier, "ScopedNotifier", ubyte(1));
      UaObjectNode child = objectNode(scopedChild, "ScopedChild", null);

      getNodeManager().addNode(noBit);
      getNodeManager().addNode(notifier);
      getNodeManager().addNode(child);

      // forward HasNotifier (a HasEventSource subtype) from the notifier defines the scope subtree;
      // the scope walk only follows forward references, so the inverse is not needed
      notifier.addReference(
          new Reference(
              scopedNotifier,
              NodeIds.HasNotifier,
              scopedChild.expanded(),
              Reference.Direction.FORWARD));
    }

    private UaObjectNode objectNode(NodeId nodeId, String name, @Nullable UByte eventNotifier) {
      UaObjectNode node =
          new UaObjectNode.UaObjectNodeBuilder(getNodeContext())
              .setNodeId(nodeId)
              .setBrowseName(newQualifiedName(name))
              .setDisplayName(LocalizedText.english(name))
              .setTypeDefinition(NodeIds.BaseObjectType)
              .build();
      if (eventNotifier != null) {
        node.setEventNotifier(eventNotifier);
      }
      node.addReference(
          new Reference(
              nodeId,
              NodeIds.Organizes,
              NodeIds.ObjectsFolder.expanded(),
              Reference.Direction.INVERSE));
      return node;
    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {}

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {}

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {}

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {}
  }

  // endregion
}
