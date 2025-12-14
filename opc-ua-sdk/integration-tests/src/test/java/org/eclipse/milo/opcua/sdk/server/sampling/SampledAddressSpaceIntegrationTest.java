/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceComposite;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.LifecycleManager;
import org.eclipse.milo.opcua.sdk.server.Namespace;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.SampledAddressSpaceFragmentWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.SimpleAddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for the sampled address space feature.
 *
 * <p>These tests verify that values posted via {@code sampleItems()} are correctly propagated to
 * OPC UA clients through subscriptions.
 */
public class SampledAddressSpaceIntegrationTest extends AbstractClientServerTest {

  private static final String NAMESPACE_URI = "urn:eclipse:milo:test:sampled";
  private static final String VALUE_NODE_ID = "SampledValue";
  private static final String DESCRIPTION_NODE_ID = "SampledDescription";

  private OpcUaSubscription subscription;
  private TestSampledNamespace sampledNamespace;

  @BeforeAll
  @Override
  public void startClientAndServer() throws Exception {
    super.startClientAndServer();

    sampledNamespace = new TestSampledNamespace(server);
    sampledNamespace.startup();
  }

  @AfterAll
  @Override
  public void stopClientAndServer() {
    sampledNamespace.shutdown();
    super.stopClientAndServer();
  }

  @BeforeEach
  void setUp() throws Exception {
    subscription = new OpcUaSubscription(client);
    subscription.create();
  }

  @AfterEach
  void tearDown() throws Exception {
    subscription.delete();
  }

  @Test
  void testBasicValueSampling() throws Exception {
    NodeId nodeId = sampledNamespace.newNodeId(VALUE_NODE_ID);

    var receivedValues = new CopyOnWriteArrayList<DataValue>();
    var initialLatch = new CountDownLatch(1);

    OpcUaMonitoredItem monitoredItem = OpcUaMonitoredItem.newDataItem(nodeId);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          initialLatch.countDown();
        });

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    // Wait for the initial value
    assertTrue(initialLatch.await(5, TimeUnit.SECONDS), "Initial value not received");
    assertEquals(1, receivedValues.size());
    receivedValues.clear();

    // Configure the namespace to return a new value on the next sample
    var updateLatch = new CountDownLatch(1);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          updateLatch.countDown();
        });

    sampledNamespace.setNextValue(nodeId, new Variant(42));

    // Wait for the updated value
    assertTrue(updateLatch.await(5, TimeUnit.SECONDS), "Updated value not received");
    assertEquals(1, receivedValues.size());
    assertEquals(42, receivedValues.get(0).value().value());
  }

  @Test
  void testValueAttributeVsOtherAttributes() throws Exception {
    // This test verifies that AttributeId.Value is wrapped in DataValue correctly
    // while other attributes receive their raw values (preventing ClassCastException).

    NodeId nodeId = sampledNamespace.newNodeId(DESCRIPTION_NODE_ID);

    // Monitor the Description attribute (not Value)
    var readValueId =
        new ReadValueId(nodeId, AttributeId.Description.uid(), null, QualifiedName.NULL_VALUE);

    var receivedValues = new CopyOnWriteArrayList<DataValue>();
    var initialLatch = new CountDownLatch(1);

    OpcUaMonitoredItem monitoredItem = new OpcUaMonitoredItem(readValueId);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          initialLatch.countDown();
        });

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    // Wait for the initial value - should succeed without ClassCastException
    assertTrue(initialLatch.await(5, TimeUnit.SECONDS), "Initial description not received");
    assertEquals(1, receivedValues.size());

    // Verify the description value is accessible
    Object descriptionValue = receivedValues.get(0).value().value();
    assertNotNull(descriptionValue);
    assertInstanceOf(
        LocalizedText.class,
        descriptionValue,
        "Description should be LocalizedText, got: " + descriptionValue.getClass());
  }

  @Test
  void testStatusCodePropagation() throws Exception {
    NodeId nodeId = sampledNamespace.newNodeId(VALUE_NODE_ID);

    var receivedValues = new CopyOnWriteArrayList<DataValue>();
    var initialLatch = new CountDownLatch(1);

    OpcUaMonitoredItem monitoredItem = OpcUaMonitoredItem.newDataItem(nodeId);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          initialLatch.countDown();
        });

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    // Wait for the initial value
    assertTrue(initialLatch.await(5, TimeUnit.SECONDS), "Initial value not received");
    assertEquals(1, receivedValues.size());
    receivedValues.clear();

    // Configure the namespace to return a bad status code
    var errorLatch = new CountDownLatch(1);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          errorLatch.countDown();
        });

    StatusCode badStatus = new StatusCode(StatusCodes.Bad_CommunicationError);
    sampledNamespace.setNextValueWithStatus(nodeId, Variant.NULL_VALUE, badStatus);

    // Wait for the error value
    assertTrue(errorLatch.await(5, TimeUnit.SECONDS), "Error value not received");
    assertEquals(1, receivedValues.size());
    assertEquals(badStatus, receivedValues.get(0).statusCode());
  }

  @Test
  void testSourceTimestampPresent() throws Exception {
    // This test verifies that source timestamps are present on received DataValues.
    NodeId nodeId = sampledNamespace.newNodeId(VALUE_NODE_ID);

    var receivedValues = new CopyOnWriteArrayList<DataValue>();
    var initialLatch = new CountDownLatch(1);

    OpcUaMonitoredItem monitoredItem = OpcUaMonitoredItem.newDataItem(nodeId);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          initialLatch.countDown();
        });

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    // Wait for the initial value
    assertTrue(initialLatch.await(5, TimeUnit.SECONDS), "Initial value not received");
    assertEquals(1, receivedValues.size());

    DataValue received = receivedValues.get(0);

    // Verify the source timestamp is present and valid
    assertNotNull(received.sourceTime(), "Source timestamp should not be null");
    assertTrue(received.sourceTime().isValid(), "Source timestamp should be valid");

    // Verify the status code is good
    assertEquals(StatusCode.GOOD, received.statusCode());
  }

  @Test
  void testImmediateSamplingOnItemCreation() throws Exception {
    NodeId nodeId = sampledNamespace.newNodeId(VALUE_NODE_ID);

    // Set up a value to be returned on immediate sampling
    sampledNamespace.setNextValue(nodeId, new Variant(123));

    var sampleLatch = sampledNamespace.expectSampleCall();

    var receivedValues = new CopyOnWriteArrayList<DataValue>();
    var initialLatch = new CountDownLatch(1);

    OpcUaMonitoredItem monitoredItem = OpcUaMonitoredItem.newDataItem(nodeId);
    monitoredItem.setDataValueListener(
        (item, value) -> {
          receivedValues.add(value);
          initialLatch.countDown();
        });

    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    // Verify that sampleItems was called (immediate sampling)
    assertTrue(sampleLatch.await(5, TimeUnit.SECONDS), "sampleItems was not called");

    // Verify the initial value was received
    assertTrue(initialLatch.await(5, TimeUnit.SECONDS), "Initial value not received");
    assertEquals(1, receivedValues.size());
  }

  /**
   * A test namespace that contains a sampled address space fragment.
   *
   * <p>This follows the same pattern as {@code ServerNamespace}: extends {@link
   * AddressSpaceComposite} and implements {@link Lifecycle} and {@link Namespace}.
   */
  static class TestSampledNamespace extends AddressSpaceComposite implements Lifecycle, Namespace {

    private final LifecycleManager lifecycleManager = new LifecycleManager();
    private final TestSampledFragment fragment;

    private final String namespaceUri;
    private final UShort namespaceIndex;

    TestSampledNamespace(OpcUaServer server) {
      super(server);

      this.namespaceUri = NAMESPACE_URI;
      this.namespaceIndex = server.getNamespaceTable().add(namespaceUri);

      this.fragment = new TestSampledFragment(server, this, namespaceIndex);

      lifecycleManager.addLifecycle(
          new Lifecycle() {
            @Override
            public void startup() {
              server.getAddressSpaceManager().register(TestSampledNamespace.this);
            }

            @Override
            public void shutdown() {
              server.getAddressSpaceManager().unregister(TestSampledNamespace.this);
            }
          });

      lifecycleManager.addLifecycle(fragment);

      lifecycleManager.addStartupTask(
          () -> {
            fragment.createTestNode(VALUE_NODE_ID, 0);
            fragment.createTestNode(DESCRIPTION_NODE_ID, 0);
          });
    }

    @Override
    public void startup() {
      lifecycleManager.startup();
    }

    @Override
    public void shutdown() {
      lifecycleManager.shutdown();
    }

    @Override
    public String getNamespaceUri() {
      return namespaceUri;
    }

    @Override
    public UShort getNamespaceIndex() {
      return namespaceIndex;
    }

    NodeId newNodeId(String id) {
      return new NodeId(namespaceIndex, id);
    }

    void setNextValue(NodeId nodeId, Variant value) {
      fragment.setNextValue(nodeId, value);
    }

    void setNextValueWithStatus(NodeId nodeId, Variant value, StatusCode statusCode) {
      fragment.setNextValueWithStatus(nodeId, value, statusCode);
    }

    CountDownLatch expectSampleCall() {
      return fragment.expectSampleCall();
    }
  }

  /**
   * A test implementation of SampledAddressSpaceFragmentWithLifecycle that allows test control over
   * sampled values.
   */
  static class TestSampledFragment extends SampledAddressSpaceFragmentWithLifecycle {

    private final Map<NodeId, PendingValue> nextValues = new ConcurrentHashMap<>();
    private final List<CountDownLatch> sampleLatches = new CopyOnWriteArrayList<>();

    private final UShort namespaceIndex;

    TestSampledFragment(
        OpcUaServer server, AddressSpaceComposite composite, UShort namespaceIndex) {
      super(server, composite);

      this.namespaceIndex = namespaceIndex;
    }

    @Override
    public AddressSpaceFilter getFilter() {
      return SimpleAddressSpaceFilter.create(getNodeManager()::containsNode);
    }

    void createTestNode(String identifier, int initialValue) {
      NodeId nodeId = new NodeId(namespaceIndex, identifier);

      UaVariableNode node =
          UaVariableNode.build(
              getNodeContext(),
              b -> {
                b.setNodeId(nodeId);
                b.setBrowseName(new QualifiedName(namespaceIndex, identifier));
                b.setDisplayName(LocalizedText.english(identifier));
                b.setDescription(LocalizedText.english("Test node: " + identifier));
                b.setDataType(NodeIds.Int32);
                b.setAccessLevel(AccessLevel.READ_WRITE);
                b.setUserAccessLevel(AccessLevel.READ_WRITE);
                return b.buildAndAdd();
              });

      node.setValue(new DataValue(new Variant(initialValue)));

      node.addReference(
          new Reference(
              node.getNodeId(),
              NodeIds.HasComponent,
              NodeIds.ObjectsFolder.expanded(),
              Reference.Direction.INVERSE));
    }

    void setNextValue(NodeId nodeId, Variant value) {
      nextValues.put(nodeId, new PendingValue(value, null, null));
    }

    void setNextValueWithStatus(NodeId nodeId, Variant value, StatusCode statusCode) {
      nextValues.put(nodeId, new PendingValue(value, statusCode, null));
    }

    CountDownLatch expectSampleCall() {
      CountDownLatch latch = new CountDownLatch(1);
      sampleLatches.add(latch);
      return latch;
    }

    @Override
    protected void sampleItems(
        SamplingGroup group, org.eclipse.milo.opcua.sdk.server.sampling.SampleSink sink) {
      DateTime sourceTime = DateTime.now();
      List<SampledValue> values = new ArrayList<>();

      for (SampledItem item : group.getActiveItems()) {
        NodeId nodeId = item.getNodeId();
        PendingValue pending = nextValues.remove(nodeId);

        if (pending != null) {
          values.add(new SampledValue(item, pending.value, pending.statusCode, pending.sourceTime));
        }
      }

      if (!values.isEmpty()) {
        sink.post(new Sample(sourceTime, values));
      }

      for (CountDownLatch latch : sampleLatches) {
        latch.countDown();
      }
      sampleLatches.clear();
    }

    record PendingValue(Variant value, StatusCode statusCode, DateTime sourceTime) {}
  }
}
