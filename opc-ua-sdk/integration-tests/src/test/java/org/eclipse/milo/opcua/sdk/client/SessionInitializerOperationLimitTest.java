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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.servicesets.AttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAttributeServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

class SessionInitializerOperationLimitTest {

  private static final long AWAIT_TIMEOUT_SECONDS = 10;

  private static final List<NodeId> TABLE_NODES =
      List.of(NodeIds.Server_NamespaceArray, NodeIds.Server_ServerArray);

  // NamespaceArray and ServerArray are independent values. A server whose hidden Read limit is
  // one must still populate both client tables after rejecting the optimistic two-node request.
  @Test
  void tooManyOperationsRetriesNamespaceAndServerArraysIndividually() throws Exception {
    try (Fixture fixture = new Fixture(StatusCodes.Bad_TooManyOperations)) {
      assertArrayEquals(
          fixture.server.getNamespaceTable().toArray(),
          fixture.client.getNamespaceTable().toArray());
      assertArrayEquals(
          fixture.server.getServerTable().toArray(), fixture.client.getServerTable().toArray());

      assertEquals(TABLE_NODES, fixture.attributeServiceSet.tableReads.get(0));
      assertEquals(
          1, fixture.attributeServiceSet.countReadsOf(List.of(NodeIds.Server_NamespaceArray)));
      assertEquals(
          1, fixture.attributeServiceSet.countReadsOf(List.of(NodeIds.Server_ServerArray)));
      assertEquals(3, fixture.attributeServiceSet.tableReads.size());
    }
  }

  // A failure unrelated to request cardinality gives no assurance that replaying the Read is safe.
  // The best-effort initializer must leave it alone and let session establishment continue.
  @Test
  void nonTooManyOperationsFailureDoesNotRetryTableReads() throws Exception {
    try (Fixture fixture = new Fixture(StatusCodes.Bad_Timeout)) {
      assertEquals(List.of(TABLE_NODES), fixture.attributeServiceSet.tableReads);
    }
  }

  // The two fallback Reads are independent. Failure to read one optional local table must not
  // discard the other table's successful result or fail session establishment.
  @Test
  void oneFailedSingletonDoesNotDiscardTheOtherTable() throws Exception {
    try (Fixture fixture =
        new Fixture(StatusCodes.Bad_TooManyOperations, NodeIds.Server_ServerArray)) {

      assertArrayEquals(
          fixture.server.getNamespaceTable().toArray(),
          fixture.client.getNamespaceTable().toArray());
      assertArrayEquals(new String[0], fixture.client.getServerTable().toArray());
      assertEquals(3, fixture.attributeServiceSet.tableReads.size());
    }
  }

  // OperationLimits describe the active server Session. Reusing the previous Session's lazy value
  // after a reconnect can make every SDK-owned partition use a stale limit.
  @Test
  void newSessionInvalidatesCachedOperationLimits() throws Exception {
    try (Fixture fixture = new Fixture(StatusCodes.Good)) {
      OperationLimits initialLimits = fixture.client.getOperationLimits();
      assertEquals(uint(10_000), initialLimits.maxNodesPerRead().orElseThrow());

      UaVariableNode maxNodesPerRead =
          (UaVariableNode)
              fixture
                  .server
                  .getAddressSpaceManager()
                  .getManagedNode(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerRead)
                  .orElseThrow();
      maxNodesPerRead.setValue(new DataValue(new Variant(uint(17))));

      assertEquals(
          uint(10_000),
          fixture.client.getOperationLimits().maxNodesPerRead().orElseThrow(),
          "precondition: the first Session's OperationLimits were not cached");

      fixture.client.disconnect();
      fixture.client.connect();

      assertEquals(uint(17), fixture.client.getOperationLimits().maxNodesPerRead().orElseThrow());
    }
  }

  private static final class Fixture implements AutoCloseable {

    final OpcUaServer server;
    final OpcUaClient client;
    final TableReadAttributeServiceSet attributeServiceSet;

    Fixture(long bootstrapFailure) throws Exception {
      this(bootstrapFailure, NodeId.NULL_VALUE);
    }

    Fixture(long bootstrapFailure, NodeId singletonFailureNode) throws Exception {
      server = TestServer.create().getServer();
      attributeServiceSet =
          new TableReadAttributeServiceSet(server, bootstrapFailure, singletonFailureNode);

      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), attributeServiceSet);
      }

      server.startup().get(AWAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

      client =
          TestClient.create(
              server, config -> config.setKeepAliveInterval(uint(TimeUnit.MINUTES.toMillis(1))));
      client.connect();
    }

    @Override
    public void close() throws Exception {
      try {
        client.disconnectAsync().get(AWAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(AWAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
    }
  }

  /** Records table Reads and scripts the service result of the optimistic two-node request. */
  private static final class TableReadAttributeServiceSet implements AttributeServiceSet {

    private final AttributeServiceSet delegate;
    private final long bootstrapFailure;
    private final NodeId singletonFailureNode;

    final List<List<NodeId>> tableReads = new CopyOnWriteArrayList<>();

    TableReadAttributeServiceSet(
        OpcUaServer server, long bootstrapFailure, NodeId singletonFailureNode) {
      this.delegate = new DefaultAttributeServiceSet(Objects.requireNonNull(server, "server"));
      this.bootstrapFailure = bootstrapFailure;
      this.singletonFailureNode = singletonFailureNode;
    }

    @Override
    public ReadResponse onRead(ServiceRequestContext context, ReadRequest request)
        throws UaException {

      List<NodeId> nodeIds = tableNodeIds(request);
      if (!nodeIds.isEmpty()) {
        tableReads.add(nodeIds);

        if (nodeIds.equals(TABLE_NODES) && bootstrapFailure != StatusCodes.Good) {
          throw new UaException(bootstrapFailure);
        }
        if (nodeIds.equals(List.of(singletonFailureNode))) {
          throw new UaException(StatusCodes.Bad_Timeout);
        }
      }

      return delegate.onRead(context, request);
    }

    @Override
    public HistoryReadResponse onHistoryRead(
        ServiceRequestContext context, HistoryReadRequest request) throws UaException {
      return delegate.onHistoryRead(context, request);
    }

    @Override
    public WriteResponse onWrite(ServiceRequestContext context, WriteRequest request)
        throws UaException {
      return delegate.onWrite(context, request);
    }

    @Override
    public HistoryUpdateResponse onHistoryUpdate(
        ServiceRequestContext context, HistoryUpdateRequest request) throws UaException {
      return delegate.onHistoryUpdate(context, request);
    }

    int countReadsOf(List<NodeId> expected) {
      return (int) tableReads.stream().filter(expected::equals).count();
    }

    private static List<NodeId> tableNodeIds(ReadRequest request) {
      ReadValueId[] nodesToRead = request.getNodesToRead();
      if (nodesToRead == null || nodesToRead.length == 0) {
        return List.of();
      }

      List<NodeId> nodeIds = Arrays.stream(nodesToRead).map(ReadValueId::getNodeId).toList();

      return nodeIds.stream().allMatch(TABLE_NODES::contains) ? nodeIds : List.of();
    }
  }
}
