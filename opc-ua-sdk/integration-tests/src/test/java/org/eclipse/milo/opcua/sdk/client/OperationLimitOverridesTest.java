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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.servicesets.AttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAttributeServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
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
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Verifies that configured OperationLimits overrides reach the SDK code that partitions requests,
 * using the ObjectTypeTree build as a representative consumer of MaxNodesPerRead.
 */
class OperationLimitOverridesTest {

  private static final long AWAIT_TIMEOUT_SECONDS = 10;

  private static final int OVERRIDE = 25;

  // Baseline for the next test: without an override, a server that omits OperationLimits forces
  // one operation per Read. This guards against the override test passing vacuously.
  @Test
  void serverWithoutOperationLimitsGetsSingletonReadsWithoutOverride() throws Exception {
    try (var fixture = new Fixture(true, null, config -> {})) {
      assertTrue(fixture.client.getOperationLimits().maxNodesPerRead().isEmpty());

      List<Integer> sizes = fixture.readObjectTypeTree();

      assertFalse(sizes.isEmpty(), "precondition: the type tree build issued Reads");
      assertTrue(sizes.stream().allMatch(size -> size == 1), "sizes: " + sizes);
    }
  }

  // A server that omits OperationLimits gives the client nothing to size requests with. The
  // override supplies the missing value, so the type tree build uses the configured Read size.
  @Test
  void overrideSuppliesMaxNodesPerReadWhenServerOmitsOperationLimits() throws Exception {
    try (var fixture =
        new Fixture(
            true,
            null,
            config ->
                config.setOperationLimitOverrides(
                    Map.of(OperationLimit.MaxNodesPerRead, uint(OVERRIDE))))) {

      OperationLimits limits = fixture.client.getOperationLimits();
      assertEquals(uint(OVERRIDE), limits.maxNodesPerRead().orElseThrow());
      assertTrue(
          limits.maxNodesPerWrite().isEmpty(),
          "a limit without an override must remain absent when the server omits it");

      List<Integer> sizes = fixture.readObjectTypeTree();

      assertTrue(sizes.stream().allMatch(size -> size <= OVERRIDE), "sizes: " + sizes);
      assertTrue(sizes.contains(OVERRIDE), "at least one full partition expected: " + sizes);
    }
  }

  // Baseline for the next test: without an override, the client trusts the advertised limit and the
  // server rejects the first oversized Read. This proves the fixture enforces its hidden limit.
  @Test
  void serverEnforcingLowerLimitRejectsReadWithoutOverride() throws Exception {
    try (var fixture = new Fixture(false, OVERRIDE, config -> {})) {
      assertEquals(
          uint(10_000), fixture.client.getOperationLimits().maxNodesPerRead().orElseThrow());

      fixture.readObjectTypeTree();

      assertTrue(fixture.attributeServiceSet.rejectedReads() > 0);
    }
  }

  // A server may advertise a higher MaxNodesPerRead than it enforces. A lower override must win,
  // so the client never sends a Read the server rejects with Bad_TooManyOperations.
  @Test
  void lowerOverrideClampsAdvertisedMaxNodesPerRead() throws Exception {
    try (var fixture =
        new Fixture(
            false,
            OVERRIDE,
            config ->
                config.setOperationLimitOverrides(
                    Map.of(OperationLimit.MaxNodesPerRead, uint(OVERRIDE))))) {

      OperationLimits limits = fixture.client.getOperationLimits();
      assertEquals(uint(OVERRIDE), limits.maxNodesPerRead().orElseThrow());
      assertEquals(
          uint(250),
          limits.maxNodesPerBrowse().orElseThrow(),
          "a limit without an override must keep the server's advertised value");

      List<Integer> sizes = fixture.readObjectTypeTree();

      assertEquals(0, fixture.attributeServiceSet.rejectedReads(), "sizes: " + sizes);
      assertTrue(sizes.contains(OVERRIDE), "at least one full partition expected: " + sizes);
    }
  }

  private static final class Fixture implements AutoCloseable {

    final OpcUaServer server;
    final OpcUaClient client;
    final RecordingAttributeServiceSet attributeServiceSet;

    /**
     * @param removeOperationLimits whether to delete the server's OperationLimits Object.
     * @param enforcedMaxNodesPerRead the largest Read the server accepts, or {@code null} to accept
     *     any size.
     * @param customizeClient customizes the client configuration.
     */
    Fixture(
        boolean removeOperationLimits,
        @Nullable Integer enforcedMaxNodesPerRead,
        Consumer<OpcUaClientConfigBuilder> customizeClient)
        throws Exception {

      server = TestServer.create().getServer();
      attributeServiceSet = new RecordingAttributeServiceSet(server, enforcedMaxNodesPerRead);

      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), attributeServiceSet);
      }

      server.startup().get(AWAIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

      if (removeOperationLimits) {
        server
            .getAddressSpaceManager()
            .getManagedNode(NodeIds.Server_ServerCapabilities_OperationLimits)
            .ifPresent(UaNode::delete);
      }

      client = TestClient.create(server, customizeClient);
      client.connect();
    }

    /**
     * Read the ObjectTypeTree and return the size of each IsAbstract Read it sent.
     *
     * <p>The OperationLimits are read before recording starts so that only the tree build's Reads
     * are recorded.
     */
    List<Integer> readObjectTypeTree() throws UaException {
      client.getOperationLimits();
      attributeServiceSet.isAbstractReadSizes.clear();

      client.readObjectTypeTree();

      return List.copyOf(attributeServiceSet.isAbstractReadSizes);
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

  /**
   * Records the size of each Read of IsAbstract attributes and optionally rejects Reads larger than
   * a hidden limit, as a server that misreports MaxNodesPerRead would.
   */
  private static final class RecordingAttributeServiceSet implements AttributeServiceSet {

    private final AttributeServiceSet delegate;
    private final @Nullable Integer enforcedMaxNodesPerRead;

    final List<Integer> isAbstractReadSizes = new CopyOnWriteArrayList<>();
    final List<Integer> rejectedReadSizes = new CopyOnWriteArrayList<>();

    RecordingAttributeServiceSet(OpcUaServer server, @Nullable Integer enforcedMaxNodesPerRead) {
      this.delegate = new DefaultAttributeServiceSet(Objects.requireNonNull(server, "server"));
      this.enforcedMaxNodesPerRead = enforcedMaxNodesPerRead;
    }

    @Override
    public ReadResponse onRead(ServiceRequestContext context, ReadRequest request)
        throws UaException {

      ReadValueId[] nodesToRead =
          Objects.requireNonNullElse(request.getNodesToRead(), new ReadValueId[0]);

      if (enforcedMaxNodesPerRead != null && nodesToRead.length > enforcedMaxNodesPerRead) {
        rejectedReadSizes.add(nodesToRead.length);
        throw new UaException(StatusCodes.Bad_TooManyOperations);
      }

      if (nodesToRead.length > 0
          && Arrays.stream(nodesToRead)
              .allMatch(r -> r.getAttributeId().equals(AttributeId.IsAbstract.uid()))) {
        isAbstractReadSizes.add(nodesToRead.length);
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

    int rejectedReads() {
      return rejectedReadSizes.size();
    }
  }
}
