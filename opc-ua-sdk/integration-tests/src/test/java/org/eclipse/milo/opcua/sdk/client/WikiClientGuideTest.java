/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.client.subscriptions.EventFilterBuilder;
import org.eclipse.milo.opcua.sdk.client.subscriptions.MonitoredItemSynchronizationException;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(30)
class WikiClientGuideTest extends AbstractClientServerTest {

  // Each helper below is a Wiki example. Assertions establish the advertised wire behavior.
  @Test
  void discoveryConnectReadAndDisconnectReturnsGoodTimestamp() throws Exception {
    String url = server.getConfig().getEndpoints().iterator().next().getEndpointUrl();
    DataValue value = connectAndRead(url);
    assertTrue(value.statusCode().isGood());
    assertInstanceOf(DateTime.class, value.value().value());
  }

  @Test
  void readPreservesTypeAndReportsUnknownNode() throws Exception {
    assertEquals(0, readInt32(client, newNodeId("TestInt32")));
    UaException missing =
        assertThrows(UaException.class, () -> readInt32(client, newNodeId("missing")));
    assertEquals(StatusCodes.Bad_NodeIdUnknown, missing.getStatusCode().value());
    assertThrows(
        IllegalArgumentException.class,
        () -> readInt32(client, NodeIds.Server_ServerStatus_CurrentTime));
    List<DataValue> batch =
        client
            .readValuesAsync(
                0.0, TimestampsToReturn.Both, List.of(newNodeId("TestInt32"), newNodeId("missing")))
            .get(10, TimeUnit.SECONDS);
    assertTrue(batch.get(0).statusCode().isGood());
    assertEquals(StatusCodes.Bad_NodeIdUnknown, batch.get(1).statusCode().value());
    assertFalse(batch.get(0).sourceTime().isNull());
    assertFalse(batch.get(0).serverTime().isNull());
  }

  @Test
  void writeReportsTypeMismatchAndPersistsValidInt32() throws Exception {
    NodeId id = newNodeId("TestInt32");
    try {
      writeInt32(client, id, 42);
      assertEquals(42, readInt32(client, id));
      List<StatusCode> results =
          client
              .writeValuesAsync(
                  List.of(id), List.of(DataValue.valueOnly(new Variant("wrong type"))))
              .get(10, TimeUnit.SECONDS);
      assertEquals(StatusCodes.Bad_TypeMismatch, results.get(0).value());
      UaVariableNode node = client.getAddressSpace().getVariableNode(id);
      UaException failure =
          assertThrows(UaException.class, () -> node.writeValue(new Variant("wrong type")));
      assertEquals(StatusCodes.Bad_TypeMismatch, failure.getStatusCode().value());
      assertEquals(42, readInt32(client, id));
    } finally {
      writeInt32(client, id, 0);
    }
  }

  @Test
  void browsingCollectsAllPagesAndReleasesAnAbandonedCursor() throws Exception {
    List<ReferenceDescription> references = browseProperties(client);
    assertTrue(references.size() > 1, "helper must drain multiple one-reference pages");
    assertTrue(
        references.stream().anyMatch(r -> r.getNodeId().equalTo(NodeIds.Server_NamespaceArray)));
    var description =
        new BrowseDescription(
            NodeIds.Server, BrowseDirection.Forward, NodeIds.HasProperty, true, uint(0), uint(63));
    BrowseResponse first =
        client.browse(
            new ViewDescription(NodeId.NULL_VALUE, DateTime.MIN_VALUE, uint(0)),
            uint(1),
            List.of(description));
    BrowseResult page = requireNonNull(first.getResults())[0];
    assertTrue(page.getStatusCode().isGood());
    ByteString cursor = page.getContinuationPoint();
    assertNotNull(cursor);
    assertFalse(cursor.isNullOrEmpty());
    try {
      assertEquals(1, requireNonNull(page.getReferences()).length);
    } finally {
      client.browseNext(true, List.of(cursor));
    }
    BrowseResult expired =
        requireNonNull(client.browseNext(false, List.of(cursor)).getResults())[0];
    assertEquals(StatusCodes.Bad_ContinuationPointInvalid, expired.getStatusCode().value());
  }

  @Test
  void browsePathResolvesNamespaceZeroPropertyAndReportsMissingName() throws Exception {
    var path =
        new BrowsePath(
            NodeIds.Server,
            new RelativePath(
                new RelativePathElement[] {
                  new RelativePathElement(
                      NodeIds.HasProperty, false, true, new QualifiedName(0, "NamespaceArray"))
                }));
    BrowsePathResult result =
        requireNonNull(client.translateBrowsePaths(List.of(path)).getResults())[0];
    assertTrue(result.getStatusCode().isGood());
    assertTrue(
        requireNonNull(result.getTargets())[0]
            .getTargetId()
            .equalTo(NodeIds.Server_NamespaceArray));
    var missing =
        new BrowsePath(
            NodeIds.Server,
            new RelativePath(
                new RelativePathElement[] {
                  new RelativePathElement(
                      NodeIds.HasProperty, false, true, new QualifiedName(0, "missing"))
                }));
    BrowsePathResult bad =
        requireNonNull(client.translateBrowsePaths(List.of(missing)).getResults())[0];
    assertEquals(StatusCodes.Bad_NoMatch, bad.getStatusCode().value());
  }

  @Test
  void nodeSynchronizationWritesAndRefreshesOneAttribute() throws Exception {
    NodeId id = newNodeId("TestInt32");
    try {
      synchronizeValue(client, id, 77);
      assertEquals(77, readInt32(client, id));
      UaVariableNode node = client.getAddressSpace().getVariableNode(id);
      assertEquals(77, node.getValue().value().value());
      node.setValue(new Variant(88));
      assertEquals(77, readInt32(client, id), "setValue must remain local until synchronized");
      node.invalidate();
      assertNotSame(node, client.getAddressSpace().getVariableNode(id));
    } finally {
      writeInt32(client, id, 0);
    }
  }

  @Test
  void callUsesObjectBindingAndReturnsArgumentErrors() throws Exception {
    assertEquals(4.0, callSquareRoot(client, NodeIds.ObjectsFolder, newNodeId("sqrt(x)"), 16.0));
    CallMethodResult bad =
        requireNonNull(
            client
                .call(
                    List.of(
                        new CallMethodRequest(
                            NodeIds.ObjectsFolder,
                            newNodeId("sqrt(x)"),
                            new Variant[] {new Variant("wrong type")})))
                .getResults())[0];
    assertEquals(StatusCodes.Bad_InvalidArgument, bad.getStatusCode().value());
    assertEquals(
        StatusCodes.Bad_TypeMismatch, requireNonNull(bad.getInputArgumentResults())[0].value());
    UaException missing =
        assertThrows(
            UaException.class,
            () -> callSquareRoot(client, NodeIds.ObjectsFolder, newNodeId("missing"), 16.0));
    assertTrue(missing.getStatusCode().isBad());
  }

  @Test
  void subscriptionDeliversGoodDataAndDeletesServerState() throws Exception {
    DataValue value = firstNotification(client, newNodeId("TestInt32"));
    assertEquals(0, value.value().value());
    assertThrows(
        MonitoredItemSynchronizationException.class,
        () -> firstNotification(client, newNodeId("missing")));
    var subscription = new OpcUaSubscription(client);
    var item = OpcUaMonitoredItem.newDataItem(newNodeId("TestInt32"));
    subscription.addMonitoredItem(item);
    try {
      subscription.create();
      subscription.synchronizeMonitoredItems();
      assertTrue(item.getRevisedSamplingInterval().isPresent());
      assertTrue(item.getRevisedQueueSize().isPresent());
      item.setSamplingInterval(200.0);
      subscription.synchronizeMonitoredItems();
      assertTrue(item.getRevisedSamplingInterval().orElseThrow() >= 200.0);
      subscription.removeMonitoredItem(item);
      subscription.synchronizeMonitoredItems();
      assertTrue(item.getMonitoredItemId().isEmpty());
    } finally {
      subscription.delete();
    }
    assertTrue(subscription.getSubscriptionId().isEmpty());
  }

  @Test
  void eventSelectClausesDetermineFieldOrder() throws Exception {
    Variant[] fields = firstEvent(client, NodeIds.Server);
    assertEquals(2, fields.length);
    assertInstanceOf(ByteString.class, fields[0].value());
    assertInstanceOf(LocalizedText.class, fields[1].value());
  }

  @Test
  void partitionedReadsPreserveOrderAndBadOperationResults() throws Exception {
    List<DataValue> values =
        readInBatches(
            client,
            List.of(
                newNodeId("TestInt32"),
                newNodeId("missing"),
                NodeIds.Server_ServerStatus_CurrentTime),
            1);
    assertEquals(3, values.size());
    assertEquals(0, values.get(0).value().value());
    assertEquals(StatusCodes.Bad_NodeIdUnknown, values.get(1).statusCode().value());
    assertInstanceOf(DateTime.class, values.get(2).value().value());
    assertThrows(IllegalArgumentException.class, () -> readInBatches(client, List.of(), 0));
    assertTrue(readInBatches(client, List.of(), 1).isEmpty());
  }

  // Endpoint selection remains a failure after the factory's /discovery retry.
  @Test
  void emptyEndpointSelectionReportsConfigurationError() {
    String url = server.getConfig().getEndpoints().iterator().next().getEndpointUrl();
    UaException failure =
        assertThrows(
            UaException.class,
            () ->
                OpcUaClient.create(
                    url, endpoints -> Optional.empty(), transport -> {}, config -> {}));
    assertEquals(StatusCodes.Bad_ConfigurationError, failure.getStatusCode().value());
  }

  // The convenience browse helper omits operation errors; raw Browse retains their status.
  @Test
  void browseHelperReturnsEmptyForAnUnknownNodeWhileRawBrowseReportsFailure() throws Exception {
    NodeId missing = newNodeId("missing");
    assertTrue(client.getAddressSpace().browse(missing).isEmpty());
    var description =
        new BrowseDescription(
            missing, BrowseDirection.Forward, NodeIds.References, true, uint(0), uint(63));
    BrowseResult result =
        requireNonNull(
            client
                .browse(
                    new ViewDescription(NodeId.NULL_VALUE, DateTime.MIN_VALUE, uint(0)),
                    uint(0),
                    List.of(description))
                .getResults())[0];
    assertEquals(StatusCodes.Bad_NodeIdUnknown, result.getStatusCode().value());
  }

  // The advertised server limit must bind even when the caller chooses a larger application cap.
  @Test
  void effectiveReadLimitPartitionsWhenRawBulkReadExceedsTheServerLimit() throws Exception {
    var fixture =
        TestServer.create(
            new OpcUaServerConfigLimits() {
              @Override
              public UInteger getMaxNodesPerRead() {
                return uint(1);
              }
            });
    var limitedServer = fixture.getServer();
    var namespace = new TestNamespace(limitedServer);
    limitedServer.addLifecycleParticipant(namespace);
    OpcUaClient limitedClient = null;
    try {
      limitedServer.startup().get(10, TimeUnit.SECONDS);
      limitedClient = TestClient.create(limitedServer, config -> {});
      limitedClient.connectAsync().get(10, TimeUnit.SECONDS);
      var ids =
          List.of(
              new NodeId(namespace.getNamespaceIndex(), "TestInt32"),
              new NodeId(namespace.getNamespaceIndex(), "missing"),
              NodeIds.Server_ServerStatus_CurrentTime);
      assertEquals(uint(1), limitedClient.getOperationLimits().maxNodesPerRead().orElseThrow());
      List<DataValue> values = readInBatches(limitedClient, ids, 10);
      assertEquals(3, values.size());
      assertEquals(0, values.get(0).value().value());
      assertEquals(StatusCodes.Bad_NodeIdUnknown, values.get(1).statusCode().value());
      assertInstanceOf(DateTime.class, values.get(2).value().value());
      OpcUaClient connected = limitedClient;
      UaException unpartitioned =
          assertThrows(
              UaException.class, () -> connected.readValues(0.0, TimestampsToReturn.Both, ids));
      assertEquals(StatusCodes.Bad_TooManyOperations, unpartitioned.getStatusCode().value());
    } finally {
      try {
        if (limitedClient != null) limitedClient.disconnectAsync().get(10, TimeUnit.SECONDS);
      } finally {
        limitedServer.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }

  // snippet:connect:start
  static DataValue connectAndRead(String discoveryUrl) throws Exception {
    OpcUaClient client =
        OpcUaClient.create(
            discoveryUrl,
            endpoints ->
                endpoints.stream()
                    .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
                    .filter(e -> e.getSecurityMode() == MessageSecurityMode.None)
                    .findFirst(),
            transport -> {},
            config ->
                config.setIdentityProvider(new AnonymousProvider()).setRequestTimeout(uint(5_000)));
    try {
      client.connectAsync().get(10, TimeUnit.SECONDS);
      DataValue value =
          client.readValue(0.0, TimestampsToReturn.Both, NodeIds.Server_ServerStatus_CurrentTime);
      if (!value.statusCode().isGood()) throw new UaException(value.statusCode());
      return value;
    } finally {
      client.disconnectAsync().get(10, TimeUnit.SECONDS);
    }
  }

  // snippet:connect:end

  // snippet:read:start
  static int readInt32(OpcUaClient client, NodeId nodeId) throws UaException {
    DataValue value = client.readValue(0.0, TimestampsToReturn.Both, nodeId);
    if (!value.statusCode().isGood()) throw new UaException(value.statusCode());
    Object body = value.value().value();
    if (!(body instanceof Integer integer)) {
      throw new IllegalArgumentException("Expected an Int32, got " + body);
    }
    return integer;
  }

  // snippet:read:end

  // snippet:write:start
  static void writeInt32(OpcUaClient client, NodeId nodeId, int value) throws UaException {
    List<StatusCode> results =
        client.writeValues(List.of(nodeId), List.of(DataValue.valueOnly(new Variant(value))));
    if (results.size() != 1) throw new IllegalStateException("Missing write result");
    if (!results.get(0).isGood()) throw new UaException(results.get(0));
  }

  // snippet:write:end

  // snippet:browse:start
  static List<ReferenceDescription> browseProperties(OpcUaClient client) throws UaException {
    AddressSpace.BrowseOptions options =
        AddressSpace.BrowseOptions.builder()
            .setReferenceType(NodeIds.HasProperty)
            .setIncludeSubtypes(true)
            .setMaxReferencesPerNode(uint(1))
            .build();
    return client.getAddressSpace().getNode(NodeIds.Server).browse(options);
  }

  // snippet:browse:end

  // snippet:node:start
  static void synchronizeValue(OpcUaClient client, NodeId nodeId, int value) throws UaException {
    UaVariableNode node = client.getAddressSpace().getVariableNode(nodeId);
    node.setValue(new Variant(value));
    List<StatusCode> writes = node.synchronize(EnumSet.of(AttributeId.Value));
    if (writes.size() != 1) throw new IllegalStateException("Missing write result");
    if (!writes.get(0).isGood()) throw new UaException(writes.get(0));
    List<DataValue> reads = node.refresh(EnumSet.of(AttributeId.Value));
    if (reads.size() != 1) throw new IllegalStateException("Missing read result");
    if (!reads.get(0).statusCode().isGood()) throw new UaException(reads.get(0).statusCode());
  }

  // snippet:node:end

  // snippet:method:start
  static double callSquareRoot(OpcUaClient client, NodeId objectId, NodeId methodId, double input)
      throws UaException {
    CallMethodResult result =
        requireNonNull(
            client
                .call(
                    List.of(
                        new CallMethodRequest(
                            objectId, methodId, new Variant[] {new Variant(input)})))
                .getResults())[0];
    if (!result.getStatusCode().isGood()) throw new UaException(result.getStatusCode());
    Variant[] outputs = result.getOutputArguments();
    if (outputs == null || outputs.length != 1 || !(outputs[0].value() instanceof Double)) {
      throw new IllegalStateException("Expected one Double output");
    }
    return (Double) outputs[0].value();
  }

  // snippet:method:end

  // snippet:data_subscription:start
  static DataValue firstNotification(OpcUaClient client, NodeId nodeId) throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(100.0);
    var firstValue = new CompletableFuture<DataValue>();
    var item = OpcUaMonitoredItem.newDataItem(nodeId);
    item.setSamplingInterval(100.0);
    item.setDataValueListener((ignored, value) -> firstValue.complete(value));
    subscription.addMonitoredItem(item);
    try {
      subscription.create();
      subscription.synchronizeMonitoredItems();
      DataValue value = firstValue.get(10, TimeUnit.SECONDS);
      if (!value.statusCode().isGood()) throw new UaException(value.statusCode());
      return value;
    } finally {
      subscription.delete();
    }
  }

  // snippet:data_subscription:end

  // snippet:event_subscription:start
  static Variant[] firstEvent(OpcUaClient client, NodeId notifierId) throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(100.0);
    EventFilter filter =
        new EventFilterBuilder()
            .select(NodeIds.BaseEventType, new QualifiedName(0, "EventId"))
            .select(NodeIds.BaseEventType, new QualifiedName(0, "Message"))
            .build();
    var item = OpcUaMonitoredItem.newEventItem(notifierId, filter);
    var firstFields = new CompletableFuture<Variant[]>();
    item.setEventValueListener((ignored, fields) -> firstFields.complete(fields));
    subscription.addMonitoredItem(item);
    try {
      subscription.create();
      subscription.synchronizeMonitoredItems();
      return firstFields.get(10, TimeUnit.SECONDS);
    } finally {
      subscription.delete();
    }
  }

  // snippet:event_subscription:end

  // snippet:partition:start
  static List<DataValue> readInBatches(
      OpcUaClient client, List<NodeId> nodeIds, int applicationBatchSize) throws UaException {
    if (applicationBatchSize < 1) throw new IllegalArgumentException("Batch size must be positive");
    long limit = client.getOperationLimits().maxNodesPerRead().map(UInteger::longValue).orElse(0L);
    int size = limit > 0 ? (int) Math.min(limit, applicationBatchSize) : applicationBatchSize;
    List<DataValue> values = new ArrayList<>();
    for (int start = 0; start < nodeIds.size(); ) {
      int end = start + Math.min(size, nodeIds.size() - start);
      List<DataValue> batch =
          client.readValues(0.0, TimestampsToReturn.Both, nodeIds.subList(start, end));
      if (batch.size() != end - start) throw new IllegalStateException("Missing read results");
      values.addAll(batch);
      start = end;
    }
    return values;
  }
  // snippet:partition:end
}
