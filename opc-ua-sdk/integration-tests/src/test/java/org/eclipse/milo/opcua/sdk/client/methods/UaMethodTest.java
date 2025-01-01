/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.methods;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import org.eclipse.milo.opcua.sdk.client.AddressSpace;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UaMethodTest extends AbstractClientServerTest {

  OpcUaSubscription subscription;
  OpcUaMonitoredItem monitoredItem;

  @BeforeEach
  void setUp() throws Exception {
    subscription = new OpcUaSubscription(client);
    subscription.create();

    monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();
  }

  @AfterEach
  void tearDown() throws Exception {
    subscription.delete();
  }

  @Test
  public void findMethod() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaObjectNode serverNode = addressSpace.getObjectNode(NodeIds.Server);

    UaMethod getMonitoredItems = serverNode.getMethod("GetMonitoredItems");

    assertNotNull(getMonitoredItems);

    Argument[] inputArguments = getMonitoredItems.getInputArguments();
    Argument[] outputArguments = getMonitoredItems.getOutputArguments();

    assertEquals(1, inputArguments.length);
    assertEquals("SubscriptionId", inputArguments[0].getName());
    assertEquals(2, outputArguments.length);
    assertEquals("ServerHandles", outputArguments[0].getName());
    assertEquals("ClientHandles", outputArguments[1].getName());

    Variant[] outputs =
        getMonitoredItems.call(
            new Variant[] {new Variant(subscription.getSubscriptionId().orElseThrow())});

    UInteger[] expected0 = {monitoredItem.getMonitoredItemId().orElseThrow()};
    UInteger[] expected1 = {monitoredItem.getClientHandle().orElseThrow()};
    assertArrayEquals(expected0, (UInteger[]) outputs[0].getValue());
    assertArrayEquals(expected1, (UInteger[]) outputs[1].getValue());
  }

  @Test
  public void callMethod() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaObjectNode serverNode = addressSpace.getObjectNode(NodeIds.Server);

    Variant[] outputs =
        serverNode.callMethod(
            "GetMonitoredItems",
            new Variant[] {new Variant(subscription.getSubscriptionId().orElseThrow())});

    UInteger[] expected0 = {monitoredItem.getMonitoredItemId().orElseThrow()};
    UInteger[] expected1 = {monitoredItem.getClientHandle().orElseThrow()};
    assertArrayEquals(expected0, (UInteger[]) outputs[0].getValue());
    assertArrayEquals(expected1, (UInteger[]) outputs[1].getValue());
  }

  @Test
  public void callMethodException() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaObjectNode serverNode = addressSpace.getObjectNode(NodeIds.Server);

    assertThrows(
        UaMethodException.class,
        () -> serverNode.callMethod("GetMonitoredItems", new Variant[] {new Variant(uint(0))}));
  }

  @Test
  public void findMethodNotFound() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaObjectNode serverNode = addressSpace.getObjectNode(NodeIds.Server);

    assertThrows(UaException.class, () -> serverNode.getMethod("foo"));
  }

  @Test
  public void callMethodWithHasComponentReference() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaObjectNode objectsNode = addressSpace.getObjectNode(NodeIds.ObjectsFolder);

    Variant[] outputs =
        objectsNode.callMethod(new QualifiedName(2, "sqrt(x)"), new Variant[] {new Variant(16.0)});

    assertEquals(4.0, outputs[0].getValue());
  }

  @Test
  public void callMethodWithHasOrderedComponentReference() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaObjectNode objectsNode = addressSpace.getObjectNode(NodeIds.ObjectsFolder);

    Variant[] outputs =
        objectsNode.callMethod(new QualifiedName(2, "sqrt2(x)"), new Variant[] {new Variant(16.0)});

    assertEquals(4.0, outputs[0].getValue());
  }

  @Test
  public void callMethodWithNoInputsOrOutputs() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaObjectNode objectsNode = addressSpace.getObjectNode(NodeIds.ObjectsFolder);

    Variant[] outputs =
        objectsNode.callMethod(new QualifiedName(2, "hasNoInputsOrOutputs()"), new Variant[0]);

    assertEquals(0, outputs.length);
  }

  @Test
  public void throwsUaMethodException() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaObjectNode objectsNode = addressSpace.getObjectNode(NodeIds.ObjectsFolder);

    assertThrows(
        UaMethodException.class,
        () -> {
          try {
            objectsNode.callMethod(
                new QualifiedName(2, "onlyAcceptsPositiveInputs()"),
                new Variant[] {new Variant(-1)});
          } catch (UaMethodException e) {
            System.out.println("result: " + e.getStatusCode());
            System.out.println(
                "inputArgumentResults: " + Arrays.toString(e.getInputArgumentResults()));

            throw e;
          }
        });
  }
}
