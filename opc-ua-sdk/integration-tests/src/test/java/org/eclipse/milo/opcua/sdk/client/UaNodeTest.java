/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.List;
import org.eclipse.milo.opcua.sdk.client.AddressSpace.BrowseOptions;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.ReferenceTypes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.junit.jupiter.api.Test;

public class UaNodeTest extends AbstractClientServerTest {

  @Test
  public void browse() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaNode serverNode = addressSpace.getNode(NodeIds.Server);

    BrowseOptions browseOptions =
        BrowseOptions.builder().setReferenceType(ReferenceTypes.HasProperty).build();

    List<ReferenceDescription> references = serverNode.browse(browseOptions);
    assertEquals(7, references.size());
    assertTrue(
        references.stream().anyMatch(n -> n.getNodeId().equalTo(NodeIds.Server_ServerArray)));
    assertTrue(
        references.stream().anyMatch(n -> n.getNodeId().equalTo(NodeIds.Server_NamespaceArray)));
    assertTrue(
        references.stream().anyMatch(n -> n.getNodeId().equalTo(NodeIds.Server_ServiceLevel)));
    assertTrue(references.stream().anyMatch(n -> n.getNodeId().equalTo(NodeIds.Server_Auditing)));
    assertTrue(
        references.stream()
            .anyMatch(n -> n.getNodeId().equalTo(NodeIds.Server_EstimatedReturnTime)));
    assertTrue(
        references.stream().anyMatch(n -> n.getNodeId().equalTo(NodeIds.Server_UrisVersion)));
    assertTrue(references.stream().anyMatch(n -> n.getNodeId().equalTo(NodeIds.Server_LocalTime)));
  }

  @Test
  public void browseNodes() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaNode serverNode = addressSpace.getNode(NodeIds.Server);

    BrowseOptions browseOptions =
        BrowseOptions.builder().setReferenceType(ReferenceTypes.HasProperty).build();

    List<? extends UaNode> nodes = serverNode.browseNodes(browseOptions);

    assertEquals(7, nodes.size());
    assertTrue(nodes.stream().anyMatch(n -> n.getNodeId().equals(NodeIds.Server_ServerArray)));
    assertTrue(nodes.stream().anyMatch(n -> n.getNodeId().equals(NodeIds.Server_NamespaceArray)));
    assertTrue(nodes.stream().anyMatch(n -> n.getNodeId().equals(NodeIds.Server_ServiceLevel)));
    assertTrue(nodes.stream().anyMatch(n -> n.getNodeId().equals(NodeIds.Server_Auditing)));
    assertTrue(
        nodes.stream().anyMatch(n -> n.getNodeId().equals(NodeIds.Server_EstimatedReturnTime)));
    assertTrue(nodes.stream().anyMatch(n -> n.getNodeId().equals(NodeIds.Server_UrisVersion)));
    assertTrue(nodes.stream().anyMatch(n -> n.getNodeId().equals(NodeIds.Server_LocalTime)));
  }

  @Test
  public void readIgnoresDataEncoding() throws UaException {
    NodeId nodeId = new NodeId(2, "TestInt32");

    List<ReadValueId> readValueIds =
        List.of(
            new ReadValueId(
                nodeId, AttributeId.Value.uid(), null, new QualifiedName(0, "Default Binary")),
            new ReadValueId(nodeId, AttributeId.Value.uid(), null, new QualifiedName(0, "Modbus")),
            new ReadValueId(
                nodeId, AttributeId.Value.uid(), null, new QualifiedName(9999, "Default Binary")),
            new ReadValueId(
                nodeId,
                AttributeId.BrowseName.uid(),
                null,
                new QualifiedName(0, "Default Binary")));

    ReadResponse response = client.read(0.0, TimestampsToReturn.Both, readValueIds);
    DataValue[] results = requireNonNull(response.getResults());

    assertEquals(4, results.length);

    Object expectedValue = results[0].value().value();

    for (int i = 0; i < 3; i++) {
      assertTrue(results[i].statusCode().isGood());
      assertEquals(expectedValue, results[i].value().value());
      assertNotNull(results[i].sourceTime());
      assertNotNull(results[i].serverTime());
    }

    assertTrue(results[3].statusCode().isGood());
    assertEquals(new QualifiedName(2, "TestInt32"), results[3].value().value());
    assertEquals(DateTime.NULL_VALUE, results[3].sourceTime());
    assertNotNull(results[3].serverTime());
  }

  @Test
  public void write() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaVariableNode testNode = (UaVariableNode) addressSpace.getNode(new NodeId(2, "TestInt32"));

    Integer i1 = (Integer) testNode.readValue().value().value();

    testNode.writeValue(new Variant(i1 + 1));

    Integer i2 = (Integer) testNode.readValue().value().value();

    assertEquals(i1 + 1, i2);

    StatusCode statusCode =
        testNode.writeAttribute(AttributeId.Value, DataValue.valueOnly(new Variant(42)));

    assertTrue(statusCode.isGood());
  }

  @Test
  public void refresh() throws UaException {
    NodeId nodeId = newNodeId("TestInt32");
    UaNode node = client.getAddressSpace().getNode(nodeId);
    var serverNode = testNamespace.getNodeManager().getNode(nodeId).orElseThrow();
    LocalizedText original = serverNode.getDescription();
    LocalizedText updated = LocalizedText.english("Updated description");
    try {
      serverNode.setDescription(updated);
      assertEquals(original, node.getDescription());

      List<DataValue> values = node.refresh(EnumSet.of(AttributeId.Description));

      assertEquals(1, values.size());
      assertEquals(StatusCode.GOOD, values.get(0).statusCode());
      assertEquals(updated, values.get(0).value().value());
      assertEquals(updated, node.getDescription());
    } finally {
      serverNode.setDescription(original);
      node.refresh(EnumSet.of(AttributeId.Description));
    }
  }

  @Test
  public void synchronize() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaVariableNode testNode = (UaVariableNode) addressSpace.getNode(new NodeId(2, "TestInt32"));

    testNode.setValue(new Variant(42));

    testNode.synchronize(EnumSet.of(AttributeId.Value));

    assertEquals(42, testNode.readValue().value().value());
  }

  @Test
  public void serverNode_ServerStatusNode_BuildInfo() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    ServerTypeNode serverNode = (ServerTypeNode) addressSpace.getNode(NodeIds.Server);

    BuildInfo buildInfo1 = serverNode.getServerStatusNode().getBuildInfo();
    assertNotNull(buildInfo1);

    BuildInfo buildInfo2 = serverNode.getServerStatusNode().readBuildInfo();
    assertNotNull(buildInfo2);

    assertEquals(buildInfo1, buildInfo2);
  }

  @Test
  public void canonicalize() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    ServerTypeNode serverNode = (ServerTypeNode) addressSpace.getNode(NodeIds.Server);
    assertSame(serverNode, serverNode.canonicalize());
    assertSame(serverNode, serverNode.canonicalize());
    assertSame(serverNode, addressSpace.getNode(NodeIds.Server));
  }

  @Test
  public void invalidate() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    ServerTypeNode serverNode1 = (ServerTypeNode) addressSpace.getNode(NodeIds.Server);
    serverNode1.invalidate();

    ServerTypeNode serverNode2 = (ServerTypeNode) addressSpace.getNode(NodeIds.Server);

    assertNotSame(serverNode1, serverNode2);
  }
}
