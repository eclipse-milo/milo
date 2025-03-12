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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
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
  public void read() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaVariableNode testNode = (UaVariableNode) addressSpace.getNode(new NodeId(2, "TestInt32"));

    DataValue value = testNode.readValue();
    assertNotNull(value);

    QualifiedName browseName = testNode.readBrowseName();
    assertNotNull(browseName);

    DataValue descriptionValue = testNode.readAttribute(AttributeId.Description);
    assertNotNull(descriptionValue);
  }

  @Test
  public void readBaseNodeAttributes() throws ExecutionException, InterruptedException {
    NodeId nodeId = new NodeId(2, "TestInt32");

    List<ReadValueId> readValueIds =
        AttributeId.BASE_ATTRIBUTES.stream()
            .map(aid -> new ReadValueId(nodeId, aid.uid(), null, QualifiedName.NULL_VALUE))
            .collect(Collectors.toList());

    ReadResponse response = client.readAsync(0.0, TimestampsToReturn.Both, readValueIds).get();

    Arrays.stream(response.getResults()).forEach(v -> System.out.println(v.value().value()));
  }

  @Test
  public void readBaseNodeAttributes2() throws UaException {
    NodeId nodeId = new NodeId(2, "TestInt32");

    UaNode node = client.getAddressSpace().getNode(nodeId);

    assertNotNull(node.getRolePermissions());
    assertNotNull(node.getUserRolePermissions());
    assertNotNull(node.getAccessRestrictions());
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
    AddressSpace addressSpace = client.getAddressSpace();

    UaNode serverNode = addressSpace.getNode(NodeIds.Server);

    List<DataValue> values = serverNode.refresh(AttributeId.OBJECT_ATTRIBUTES);

    values.forEach(
        v -> {
          assertNotNull(v.statusCode());
          assertTrue(v.statusCode().isGood() || v.value().isNull());
        });
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
