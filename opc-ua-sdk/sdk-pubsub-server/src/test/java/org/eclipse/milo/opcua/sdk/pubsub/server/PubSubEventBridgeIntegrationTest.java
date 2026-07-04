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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * R17 event rows through a REAL client monitored item (T5 §12.2, reusing the spike's OfType
 * mechanics): a state change driven by the minted Status methods arrives typed {@code i=15535} with
 * the informational severity band; a send failure (driven through the S17 bridge seam — no
 * deterministic in-module send-failure stimulus exists) arrives typed {@code i=15563} THROUGH the
 * {@code OfType(i=15535)} filter (the both-subtypes pin) carrying the synthetic code in {@code
 * Error} and the Error severity band; the first-failure-per-episode suppression holds over the wire
 * and re-arms when the path passes through Operational (D34); {@code close()} emits NO
 * Disabled-spray (dispose transitions are silenced, D33); a rapid bounce loop drains completely
 * with the server healthy afterwards (EV9 — the shared serialized listener executor absorbs the
 * burst without deadlock or loss); and the EV4 REAL Error-entry stimulus — a reader whose {@code
 * messageReceiveTimeout} expires once its external publisher stops — arrives typed {@code i=15535}
 * with {@code State == Error} and the Error severity band.
 *
 * <p>The single real-stimulus reception row (engine {@code runtime().disable} &rarr; client) is
 * {@link PubSubStatusEventReceptionTest}; the bridge's field-mapping unit rows are {@link
 * PubSubStatusEventBridgeTest}. This class adds the Status-method-driven cross-WP flow, the
 * client-observed suppression/re-arm/dispose rows, and the reader-timeout Error-entry row.
 */
class PubSubEventBridgeIntegrationTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "ev-conn";
  private static final String WRITER_GROUP = "evg";
  private static final String READER_CONNECTION = "ev-rconn";
  private static final String READER_GROUP = "ev-rg";
  private static final String READER = "ev-dsr";
  private static final String EXTERNAL_DATA_SET = "ev-ext-ds";

  // select-clause indices
  private static final int EVENT_TYPE = 0;
  private static final int SOURCE_NODE = 1;
  private static final int SEVERITY = 2;
  private static final int GROUP_ID = 3;
  private static final int STATE = 4;
  private static final int ERROR = 5;

  private static SksTestServer testServer;
  private static OpcUaClient client;

  private final List<ServerPubSub> attached = new ArrayList<>();

  @BeforeAll
  static void startServerAndClient() throws Exception {
    testServer = SksTestServer.create(null);
    client = connect();
  }

  @AfterAll
  static void stopEverything() throws Exception {
    if (client != null) {
      client.disconnect();
    }
    if (testServer != null) {
      testServer.close();
    }
  }

  @AfterEach
  void closeAttached() {
    attached.forEach(ServerPubSub::close);
    attached.clear();
  }

  @Test
  void statusMethodTransitionsAndCommFailuresArriveWithSeverityBands() throws Exception {
    ServerPubSub serverPubSub = attach();

    var subscription = new OpcUaSubscription(client);
    // a fast publish cycle keeps event delivery latency well inside the quiet windows
    subscription.setPublishingInterval(100.0);
    subscription.create();
    try {
      var received = new LinkedBlockingQueue<Variant[]>();
      createEventItem(subscription, received);

      NodeId groupNodeId = fragmentNodeId(CONNECTION + "/" + WRITER_GROUP);

      // EV1: Disable through the minted Status method — the group-sourced Disabled event
      // arrives typed i=15535 with GroupId == the group node and informational severity
      assertEquals(StatusCode.GOOD, callStatus("Disable").getStatusCode());

      Variant[] disabled =
          awaitEvent(
              received,
              e ->
                  groupNodeId.equals(e[SOURCE_NODE].getValue())
                      && stateOf(e) == PubSubState.Disabled.getValue());
      assertEquals(NodeIds.PubSubStatusEventType, disabled[EVENT_TYPE].getValue());
      assertEquals(groupNodeId, disabled[GROUP_ID].getValue());
      assertTrue(
          severityOf(disabled) <= 333,
          "informational transitions use the <=333 band, was " + severityOf(disabled));

      // back to Operational (also the suppression re-arm edge)
      assertEquals(StatusCode.GOOD, callStatus("Enable").getStatusCode());
      awaitEvent(
          received,
          e ->
              groupNodeId.equals(e[SOURCE_NODE].getValue())
                  && stateOf(e) == PubSubState.Operational.getValue());

      // EV5: a synthetic send failure through the S17 seam arrives typed i=15563 THROUGH the
      // OfType(i=15535) filter, Error == the synthetic code, severity in the Error band
      PubSubStatusEventBridge bridge = serverPubSub.statusEventBridge();
      assertNotNull(bridge);

      var sendFailureCode = new StatusCode(StatusCodes.Bad_ResourceUnavailable);
      bridge.onDiagnosticsEvent(sendFailure(sendFailureCode));

      Variant[] failure =
          awaitEvent(
              received,
              e -> NodeIds.PubSubCommunicationFailureEventType.equals(e[EVENT_TYPE].getValue()));
      assertEquals(groupNodeId, failure[SOURCE_NODE].getValue());
      assertEquals(sendFailureCode, failure[ERROR].getValue());
      assertTrue(
          severityOf(failure) >= 334 && severityOf(failure) <= 666,
          "communication failures use the 334-666 band, was " + severityOf(failure));

      // EV6: a second failure in the SAME episode is suppressed ...
      bridge.onDiagnosticsEvent(sendFailure(sendFailureCode));

      // ... re-arm by passing the path through Operational, then fail again — the SECOND
      // communication-failure event arrives
      assertEquals(StatusCode.GOOD, callStatus("Disable").getStatusCode());
      assertEquals(StatusCode.GOOD, callStatus("Enable").getStatusCode());
      awaitEvent(
          received,
          e ->
              groupNodeId.equals(e[SOURCE_NODE].getValue())
                  && stateOf(e) == PubSubState.Operational.getValue());

      bridge.onDiagnosticsEvent(sendFailure(sendFailureCode));
      awaitEvent(
          received,
          e -> NodeIds.PubSubCommunicationFailureEventType.equals(e[EVENT_TYPE].getValue()));

      // settle: exactly TWO communication-failure events ever arrived (both awaited above) —
      // the suppressed middle one never materializes as a straggler
      long stragglers =
          drain(received, Duration.ofMillis(750)).stream()
              .filter(
                  e -> NodeIds.PubSubCommunicationFailureEventType.equals(e[EVENT_TYPE].getValue()))
              .count();
      assertEquals(0L, stragglers, "the suppressed failure must never emit an event");
    } finally {
      subscription.delete();
    }
  }

  /**
   * EV9: absence of deadlock and unbounded queue growth under a bounce loop — the bridge, the
   * fragment's ComponentNodeListener, and the engine state listeners all share the serialized
   * listener executor, and the bridge re-enters EventFactory/EventNotifier from it. Twenty rapid
   * Disable/Enable cycles (no per-cycle await of delivery) must drain completely: every cycle's
   * Disabled and Operational-entry events arrive over the wire, and the server stays healthy
   * (further Status calls and reads still answer). Soft-ordering only; no lock-order assertions.
   */
  @Test
  void bounceLoopDrainsAllEventsAndTheServerStaysHealthy() throws Exception {
    ServerPubSub serverPubSub = attach();

    var subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(100.0);
    subscription.create();
    try {
      var received = new LinkedBlockingQueue<Variant[]>();
      // 20 cycles emit 40+ events, easily more than one publish cycle's worth: a deep item
      // queue keeps the burst from overflowing (the shared helper's 32 would not)
      createEventItem(subscription, received, 512);

      NodeId groupNodeId = fragmentNodeId(CONNECTION + "/" + WRITER_GROUP);

      // start from a settled Operational so the first Disable finds a non-Disabled state
      awaitOperational(serverPubSub, CONNECTION + "/" + WRITER_GROUP);
      drainToQuiet(received);

      // the bounce loop: back-to-back Calls with no delivery awaits in between — the engine
      // applies each transition synchronously, so the strict alternation always satisfies
      // the §9.1.10 state rules while the listener executor absorbs the burst
      int cycles = 20;
      for (int i = 0; i < cycles; i++) {
        assertEquals(StatusCode.GOOD, callStatus("Disable").getStatusCode(), "Disable cycle " + i);
        assertEquals(StatusCode.GOOD, callStatus("Enable").getStatusCode(), "Enable cycle " + i);
      }

      // all events drain: every cycle's Disabled and Operational-entry event for the group
      // arrives (other states, e.g. PreOperational hops, are tolerated and skipped)
      int disabledSeen = 0;
      int operationalSeen = 0;
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
      while (disabledSeen < cycles || operationalSeen < cycles) {
        long remaining = deadline - System.nanoTime();
        Variant[] next = received.poll(Math.max(remaining, 1L), TimeUnit.NANOSECONDS);
        assertNotNull(
            next,
            "the bounce-loop events never fully drained: %d/%d Disabled, %d/%d Operational"
                .formatted(disabledSeen, cycles, operationalSeen, cycles));
        if (!groupNodeId.equals(next[SOURCE_NODE].getValue())) {
          continue;
        }
        if (stateOf(next) == PubSubState.Disabled.getValue()) {
          disabledSeen++;
        } else if (stateOf(next) == PubSubState.Operational.getValue()) {
          operationalSeen++;
        }
      }

      // ... and nothing beyond the loop's transitions arrives afterwards
      drainToQuiet(received);

      // server healthy: the engine settled Operational, further Status calls answer, and an
      // ordinary read still works (nothing is wedged on the listener executor)
      awaitOperational(serverPubSub, CONNECTION + "/" + WRITER_GROUP);
      assertEquals(StatusCode.GOOD, callStatus("Disable").getStatusCode());
      assertEquals(StatusCode.GOOD, callStatus("Enable").getStatusCode());
      assertTrue(
          client
              .readValues(0.0, TimestampsToReturn.Neither, List.of(NodeIds.Server_NamespaceArray))
              .get(0)
              .getStatusCode()
              .isGood(),
          "the server must still answer ordinary reads after the bounce loop");
    } finally {
      subscription.delete();
    }
  }

  @Test
  void disposeIsSilentOverTheWire() throws Exception {
    ServerPubSub serverPubSub = attach();

    var subscription = new OpcUaSubscription(client);
    // a fast publish cycle keeps event delivery latency well inside the quiet windows
    subscription.setPublishingInterval(100.0);
    subscription.create();
    try {
      var received = new LinkedBlockingQueue<Variant[]>();
      createEventItem(subscription, received);

      // wait until the startup cascade has fully LANDED engine-side (connection and group
      // Operational), then drain to a full quiet window: any startup event still in the
      // serialized dispatch or the subscription's publish queue surfaces before close(), so
      // no pre-close event can masquerade as a post-close one
      awaitOperational(serverPubSub, CONNECTION + "/" + WRITER_GROUP);
      drainToQuiet(received);

      // D33/EV8: shutdown disposes every component — cause=DISPOSE transitions emit NOTHING
      serverPubSub.close();

      List<Variant[]> afterClose = drain(received, Duration.ofSeconds(1));
      var descriptions = new ArrayList<String>();
      for (Variant[] event : afterClose) {
        descriptions.add(
            "type=%s source=%s state=%s"
                .formatted(
                    event[EVENT_TYPE].getValue(), event[SOURCE_NODE].getValue(), stateOf(event)));
      }
      assertTrue(
          afterClose.isEmpty(),
          "dispose-cause transitions must be silent, got after close(): " + descriptions);
    } finally {
      subscription.delete();
    }
  }

  /**
   * EV4: the pinned deterministic REAL Error stimulus — a reader fed by an external publisher until
   * it turns Operational (Table 2: a DataSetReader completes startup only on its first key frame),
   * whose configured {@code messageReceiveTimeout} then expires when the publisher stops — arrives
   * at a REAL client as an {@code i=15535} event sourced at the reader with {@code State == Error}
   * and severity in the 334–666 Error band (previously the Error-entry band was pinned only
   * unit-side; the wire band was asserted only for the synthetic {@code i=15563} row).
   */
  @Test
  void readerTimeoutErrorEntryArrivesWithTheErrorSeverityBand() throws Exception {
    int dataPort = freeUdpPort();
    ServerPubSub serverPubSub = attachWithReader(dataPort);

    var subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(100.0);
    subscription.create();
    try {
      var received = new LinkedBlockingQueue<Variant[]>();
      createEventItem(subscription, received);

      NodeId readerNodeId = fragmentNodeId(READER_CONNECTION + "/" + READER_GROUP + "/" + READER);
      PubSubHandle reader =
          serverPubSub
              .runtime()
              .components()
              .dataSetReader(READER_CONNECTION, READER_GROUP, READER)
              .orElseThrow();

      PubSubService publisher = externalPublisher(dataPort);
      try {
        publisher.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        awaitState(serverPubSub, reader, PubSubState.Operational);
      } finally {
        publisher.shutdown().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }

      // the publisher is gone: the messageReceiveTimeout expires and the reader enters Error —
      // the event is typed i=15535, sourced at the reader, and uses the Error severity band
      Variant[] error =
          awaitEvent(
              received,
              e ->
                  readerNodeId.equals(e[SOURCE_NODE].getValue())
                      && stateOf(e) == PubSubState.Error.getValue());
      assertEquals(NodeIds.PubSubStatusEventType, error[EVENT_TYPE].getValue());
      assertTrue(
          severityOf(error) >= 334 && severityOf(error) <= 666,
          "Error entries use the 334-666 band, was " + severityOf(error));
      assertEquals(PubSubState.Error, serverPubSub.runtime().state(reader));
    } finally {
      subscription.delete();
    }
  }

  // region fixtures + helpers

  private static PubSubDiagnosticsEvent sendFailure(StatusCode code) {
    return new PubSubDiagnosticsEvent(
        CONNECTION + "/" + WRITER_GROUP,
        code,
        "synthetic send failure",
        null,
        PubSubDiagnosticsEvent.Kind.SEND_FAILURE);
  }

  private static void createEventItem(
      OpcUaSubscription subscription, LinkedBlockingQueue<Variant[]> received) throws UaException {
    // several transitions can land within one publish cycle; never rely on queue size 1
    createEventItem(subscription, received, 32);
  }

  private static void createEventItem(
      OpcUaSubscription subscription, LinkedBlockingQueue<Variant[]> received, int queueSize)
      throws UaException {

    EventFilter eventFilter =
        new EventFilter(
            new SimpleAttributeOperand[] {
              select(NodeIds.BaseEventType, "EventType"),
              select(NodeIds.BaseEventType, "SourceNode"),
              select(NodeIds.BaseEventType, "Severity"),
              select(NodeIds.PubSubStatusEventType, "GroupId"),
              select(NodeIds.PubSubStatusEventType, "State"),
              select(NodeIds.PubSubCommunicationFailureEventType, "Error")
            },
            ofType(NodeIds.PubSubStatusEventType));

    OpcUaMonitoredItem item = OpcUaMonitoredItem.newEventItem(NodeIds.Server, eventFilter);
    item.setEventValueListener((monitoredItem, eventValues) -> received.add(eventValues));
    item.setQueueSize(uint(queueSize));

    subscription.addMonitoredItem(item);
    subscription.synchronizeMonitoredItems();
  }

  /** Poll until an event matching {@code predicate} arrives; unrelated events are skipped. */
  private static Variant[] awaitEvent(
      LinkedBlockingQueue<Variant[]> received, Predicate<Variant[]> predicate)
      throws InterruptedException {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    while (true) {
      long remaining = deadline - System.nanoTime();
      Variant[] next = received.poll(Math.max(remaining, 1L), TimeUnit.NANOSECONDS);
      assertNotNull(next, "timed out waiting for the expected event");
      if (predicate.test(next)) {
        return next;
      }
    }
  }

  /** Deadline-poll until the group (and so its parents) reads Operational engine-side. */
  private static void awaitOperational(ServerPubSub serverPubSub, String groupPath) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    while (true) {
      var components = serverPubSub.runtime().components();
      var group = components.writerGroup(CONNECTION, WRITER_GROUP);
      if (group.isPresent()
          && serverPubSub.runtime().state(group.get()) == PubSubState.Operational) {
        return;
      }
      assertTrue(System.nanoTime() < deadline, "the group never reached Operational: " + groupPath);
      java.util.concurrent.locks.LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(25));
    }
  }

  /**
   * Drain until a full quiet window passes with no arrivals (deadline-bounded). The window must
   * comfortably exceed the subscription's publish cycle, or an already-fired event still waiting
   * for its publish response masquerades as a later one.
   */
  private static void drainToQuiet(LinkedBlockingQueue<Variant[]> received)
      throws InterruptedException {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    while (!drain(received, Duration.ofMillis(750)).isEmpty()) {
      assertTrue(System.nanoTime() < deadline, "the event stream never went quiet");
    }
  }

  /** Collect everything arriving within {@code window} (a bounded straggler sweep). */
  private static List<Variant[]> drain(LinkedBlockingQueue<Variant[]> received, Duration window)
      throws InterruptedException {

    var drained = new ArrayList<Variant[]>();
    long deadline = System.nanoTime() + window.toNanos();
    while (true) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        return drained;
      }
      Variant[] next = received.poll(remaining, TimeUnit.NANOSECONDS);
      if (next != null) {
        drained.add(next);
      }
    }
  }

  private static int stateOf(Variant[] event) {
    return event[STATE].getValue() instanceof Number state ? state.intValue() : -1;
  }

  private static int severityOf(Variant[] event) {
    Object severity = event[SEVERITY].getValue();
    return severity instanceof UShort value ? value.intValue() : -1;
  }

  private CallMethodResult callStatus(String methodName) throws UaException {
    NodeId objectId = fragmentNodeId(CONNECTION + "/" + WRITER_GROUP + "/Status");
    NodeId methodId = fragmentNodeId(CONNECTION + "/" + WRITER_GROUP + "/Status/" + methodName);

    CallResponse response =
        client.call(List.of(new CallMethodRequest(objectId, methodId, new Variant[0])));
    CallMethodResult[] results = response.getResults();
    assertTrue(results != null && results.length == 1);
    return results[0];
  }

  private static NodeId fragmentNodeId(String identifier) {
    UShort idx = testServer.getServer().getServerNamespace().getNamespaceIndex();
    return new NodeId(idx, "PubSub/" + identifier);
  }

  private ServerPubSub attach() throws Exception {
    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            config(),
            ServerPubSubOptions.builder()
                .exposeInformationModel(true)
                .allowRemoteConfiguration(true)
                .statusEventsEnabled(true)
                .build());
    attached.add(serverPubSub);
    serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    return serverPubSub;
  }

  /** Deadline-poll until {@code handle} reads {@code expected} engine-side. */
  private static void awaitState(
      ServerPubSub serverPubSub, PubSubHandle handle, PubSubState expected) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    while (serverPubSub.runtime().state(handle) != expected) {
      assertTrue(
          System.nanoTime() < deadline,
          "the component never reached " + expected + ": " + serverPubSub.runtime().state(handle));
      java.util.concurrent.locks.LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(25));
    }
  }

  /** Attach a runtime whose reader (short messageReceiveTimeout) listens on {@code dataPort}. */
  private ServerPubSub attachWithReader(int dataPort) throws Exception {
    PubSubConfig config =
        PubSubConfig.builder()
            .connection(
                PubSubConnectionConfig.udp(READER_CONNECTION)
                    .publisherId(PublisherId.uint16(ushort(4897)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", dataPort))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .readerGroup(
                        ReaderGroupConfig.builder(READER_GROUP)
                            .dataSetReader(
                                DataSetReaderConfig.builder(READER)
                                    .publisherId(PublisherId.uint16(ushort(4898)))
                                    .writerGroupId(ushort(1))
                                    .dataSetWriterId(ushort(1))
                                    .dataSetMetaData(externalMetaData())
                                    .metadataPolicy(MetadataPolicy.REQUIRE_CONFIGURED)
                                    // the EV4 error stimulus: once the external publisher
                                    // stops, the reader expires into Error
                                    .messageReceiveTimeout(Duration.ofMillis(750))
                                    .build())
                            .build())
                    .build())
            .build();

    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            config,
            ServerPubSubOptions.builder()
                .exposeInformationModel(true)
                .statusEventsEnabled(true)
                .build());
    attached.add(serverPubSub);
    serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    return serverPubSub;
  }

  /** A standalone publisher service whose writer matches the attached reader's filters. */
  private static PubSubService externalPublisher(int dataPort) throws SocketException {
    PublishedDataSetConfig dataSet =
        PublishedDataSetConfig.builder(EXTERNAL_DATA_SET)
            .field(
                FieldDefinition.builder("v")
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(externalFieldId())
                    .build())
            .build();

    PubSubConfig config =
        PubSubConfig.builder()
            .publishedDataSet(dataSet)
            .connection(
                PubSubConnectionConfig.udp("ev-pub")
                    .publisherId(PublisherId.uint16(ushort(4898)))
                    .address(UdpDatagramAddress.unicast("127.0.0.1", dataPort))
                    .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                    .writerGroup(
                        WriterGroupConfig.builder("grp")
                            .writerGroupId(ushort(1))
                            .publishingInterval(Duration.ofMillis(75))
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
                                    .fieldContentMask(
                                        DataSetFieldContentMask.of(
                                            DataSetFieldContentMask.Field.StatusCode))
                                    .settings(
                                        UadpDataSetWriterSettings.builder()
                                            .dataSetMessageContentMask(
                                                UadpDataSetMessageContentMask.of(
                                                    UadpDataSetMessageContentMask.Field.Timestamp,
                                                    UadpDataSetMessageContentMask.Field
                                                        .MajorVersion,
                                                    UadpDataSetMessageContentMask.Field
                                                        .MinorVersion,
                                                    UadpDataSetMessageContentMask.Field
                                                        .SequenceNumber))
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    PubSubBindings bindings =
        PubSubBindings.builder()
            .source(
                dataSet.ref(),
                context -> {
                  DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
                  builder.field("v", new DataValue(Variant.ofDouble(4.5)));
                  return builder.build();
                })
            .build();

    return PubSubService.create(config, bindings);
  }

  private static DataSetMetaDataConfig externalMetaData() {
    return DataSetMetaDataConfig.builder(EXTERNAL_DATA_SET)
        .field("v", NodeIds.Double, externalFieldId())
        .build();
  }

  private static UUID externalFieldId() {
    return new UUID(0L, 0xE4L);
  }

  /** One enabled connection with one enabled, writer-less group: transitions without traffic. */
  private static PubSubConfig config() throws SocketException {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4895)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(
                    WriterGroupConfig.builder(WRITER_GROUP)
                        .writerGroupId(ushort(24))
                        .publishingInterval(Duration.ofMillis(200))
                        .build())
                .build())
        .build();
  }

  /** A select clause for {@code browseName} declared on {@code typeDefinitionId}. */
  private static SimpleAttributeOperand select(NodeId typeDefinitionId, String browseName) {
    return new SimpleAttributeOperand(
        typeDefinitionId,
        new QualifiedName[] {new QualifiedName(0, browseName)},
        AttributeId.Value.uid(),
        null);
  }

  /** A where clause of exactly one element: {@code OfType(typeDefinitionId)}. */
  private static ContentFilter ofType(NodeId typeDefinitionId) {
    ExtensionObject operand =
        ExtensionObject.encode(
            client.getStaticEncodingContext(), new LiteralOperand(new Variant(typeDefinitionId)));

    return new ContentFilter(
        new ContentFilterElement[] {
          new ContentFilterElement(FilterOperator.OfType, new ExtensionObject[] {operand})
        });
  }

  private static OpcUaClient connect() throws UaException {
    OpcUaClient newClient =
        OpcUaClient.create(
            testServer.getEndpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(
                        e -> Objects.equals(e.getSecurityPolicyUri(), SecurityPolicy.None.getUri()))
                    .findFirst(),
            transportConfigBuilder -> {},
            clientConfigBuilder ->
                clientConfigBuilder
                    .setApplicationName(LocalizedText.english("event bridge test client"))
                    .setApplicationUri("urn:eclipse:milo:test:event-bridge-client")
                    .setIdentityProvider(new AnonymousProvider())
                    .setRequestTimeout(uint(5_000)));

    newClient.connect();

    return newClient;
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
