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

import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * A minimal embedded {@link OpcUaServer} fixture for sdk-pubsub-server tests, modeled on the
 * integration-tests TestServer/TestNamespace pattern but without endpoints, transports,
 * certificates, or identity validation.
 *
 * <p>The server is fully usable as an address-space host as soon as it is constructed (ns0 loads in
 * the {@code OpcUaServer} constructor) and is intentionally <b>never started</b>: {@code
 * OpcUaServer.startup()} would fail with no bindable endpoints, and PubSub operates independently
 * of the server's client-facing transports. Because the server is never started, {@link #close()}
 * only shuts down the test namespace; {@code OpcUaServer.shutdown()} must not be called on a
 * never-started server (its EventFactory lifecycle would throw).
 *
 * <p>Shared fixture API (also used by the T-L info-model tests):
 *
 * <ul>
 *   <li>{@link #create()} — build the server and start the variables namespace.
 *   <li>{@link #getServer()}, {@link #getNamespaceIndex()}, {@link #getNamespaceUri()}.
 *   <li>{@link #addVariable(String, NodeId, DataValue)} — a writable scalar variable
 *       (CurrentRead+CurrentWrite, {@code allowNulls=true}) hanging off the Objects folder.
 *   <li>{@link #addArrayVariable(String, NodeId, DataValue)} — same, ValueRank OneDimension.
 *   <li>{@link #addReadOnlyVariable(String, NodeId, DataValue)} — CurrentRead only.
 *   <li>{@link #getVariableNode(NodeId)}, {@link #getValue(NodeId)}, {@link #setValue(NodeId,
 *       DataValue)} — direct node access for assertions and live-value updates.
 *   <li>{@link #nodeId(String)} — a NodeId in the test namespace without creating a node.
 * </ul>
 *
 * <p>Writable variables are created with {@code allowNulls=true} because the Part 14 Table 80
 * Disabled-handling and state-change rows write null Variants, which AttributeWriter otherwise
 * rejects with {@code Bad_TypeMismatch}.
 */
final class TestPubSubServer implements AutoCloseable {

  static final String NAMESPACE_URI = "urn:eclipse:milo:pubsub:test";

  private final OpcUaServer server;
  private final TestVariablesNamespace namespace;

  private TestPubSubServer(OpcUaServer server, TestVariablesNamespace namespace) {
    this.server = server;
    this.namespace = namespace;
  }

  /**
   * Create a new {@link TestPubSubServer}: an endpoint-less, never-started {@link OpcUaServer} with
   * the test variables namespace started and registered.
   */
  static TestPubSubServer create() {
    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri("urn:eclipse:milo:pubsub:test-server")
            .setApplicationName(LocalizedText.english("sdk-pubsub-server test server"))
            .setProductUri("urn:eclipse:milo:pubsub:test-server")
            .build();

    var server =
        new OpcUaServer(
            config,
            transportProfile -> {
              throw new IllegalStateException(
                  "TestPubSubServer has no transports: " + transportProfile);
            });

    var namespace = new TestVariablesNamespace(server);
    namespace.startup();

    return new TestPubSubServer(server, namespace);
  }

  OpcUaServer getServer() {
    return server;
  }

  UShort getNamespaceIndex() {
    return namespace.getNamespaceIndex();
  }

  String getNamespaceUri() {
    return namespace.getNamespaceUri();
  }

  /** Add a writable scalar variable (CurrentRead+CurrentWrite, allowNulls) named {@code name}. */
  NodeId addVariable(String name, NodeId dataTypeId, DataValue initialValue) {
    return namespace
        .addVariable(name, dataTypeId, initialValue, AccessLevel.READ_WRITE, ValueRanks.Scalar)
        .getNodeId();
  }

  /** Add a writable one-dimensional array variable named {@code name}. */
  NodeId addArrayVariable(String name, NodeId dataTypeId, DataValue initialValue) {
    return namespace
        .addVariable(
            name, dataTypeId, initialValue, AccessLevel.READ_WRITE, ValueRanks.OneDimension)
        .getNodeId();
  }

  /** Add a read-only variable (CurrentRead, no CurrentWrite) named {@code name}. */
  NodeId addReadOnlyVariable(String name, NodeId dataTypeId, DataValue initialValue) {
    return namespace
        .addVariable(name, dataTypeId, initialValue, AccessLevel.READ_ONLY, ValueRanks.Scalar)
        .getNodeId();
  }

  /** Get the {@link UaVariableNode} with {@code nodeId} from the test namespace. */
  UaVariableNode getVariableNode(NodeId nodeId) {
    return (UaVariableNode)
        namespace
            .getNodeManager()
            .getNode(nodeId)
            .orElseThrow(() -> new IllegalArgumentException("no such node: " + nodeId));
  }

  /** Get the current value of the variable with {@code nodeId}. */
  DataValue getValue(NodeId nodeId) {
    return getVariableNode(nodeId).getValue();
  }

  /** Set the current value of the variable with {@code nodeId}. */
  void setValue(NodeId nodeId, DataValue value) {
    getVariableNode(nodeId).setValue(value);
  }

  /** A {@link NodeId} in the test namespace; no node is created. */
  NodeId nodeId(String name) {
    return new NodeId(getNamespaceIndex(), name);
  }

  @Override
  public void close() {
    namespace.shutdown();
  }

  /** The namespace hosting the fixture's variables. No client subscriptions are supported. */
  private static final class TestVariablesNamespace extends ManagedNamespaceWithLifecycle {

    TestVariablesNamespace(OpcUaServer server) {
      super(server, NAMESPACE_URI);
    }

    UaVariableNode addVariable(
        String name,
        NodeId dataTypeId,
        DataValue initialValue,
        Set<AccessLevel> accessLevel,
        int valueRank) {

      UaVariableNode node =
          new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
              .setNodeId(newNodeId(name))
              .setAccessLevel(accessLevel)
              .setUserAccessLevel(accessLevel)
              .setBrowseName(newQualifiedName(name))
              .setDisplayName(LocalizedText.english(name))
              .setDataType(dataTypeId)
              .setValueRank(valueRank)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .build();

      node.setValue(initialValue);
      node.setAllowNulls(true);

      node.addReference(
          new Reference(
              node.getNodeId(),
              NodeIds.HasComponent,
              NodeIds.ObjectsFolder.expanded(),
              Reference.Direction.INVERSE));

      getNodeManager().addNode(node);

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
}
