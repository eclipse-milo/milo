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

import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumSet;
import java.util.List;
import org.eclipse.milo.opcua.sdk.client.AddressSpace.BrowseOptions;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.ReferenceTypes;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.junit.jupiter.api.Test;

public class AddressSpaceTest extends AbstractClientServerTest {

  @Test
  public void browse() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaNode serverNode = addressSpace.getNode(NodeIds.Server);
    List<? extends UaNode> nodes = addressSpace.browseNodes(serverNode);

    nodes.forEach(
        n -> {
          System.out.println(
              String.format(
                  "%s (%s) [%s]",
                  n.getBrowseName().toParseableString(),
                  n.getNodeId().toParseableString(),
                  n.getNodeClass()));

          if (n instanceof UaVariableNode) {
            System.out.println("└─ value = " + ((UaVariableNode) n).getValue().value());
          }
        });
  }

  @Test
  public void browseWithBrowseDirection() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    {
      UaNode serverNode = addressSpace.getNode(NodeIds.Server);
      BrowseOptions browseOptions =
          addressSpace.getBrowseOptions().copy(b -> b.setBrowseDirection(BrowseDirection.Inverse));

      List<? extends UaNode> nodes = addressSpace.browseNodes(serverNode, browseOptions);

      assertEquals(1, nodes.size());
      assertTrue(nodes.stream().anyMatch(n -> n.getNodeId().equals(NodeIds.ObjectsFolder)));
    }

    {
      UaNode objectsFolderNode = addressSpace.getNode(NodeIds.ObjectsFolder);
      BrowseOptions browseOptions =
          addressSpace.getBrowseOptions().copy(b -> b.setBrowseDirection(BrowseDirection.Both));

      List<? extends UaNode> nodes = addressSpace.browseNodes(objectsFolderNode, browseOptions);

      assertTrue(nodes.stream().anyMatch(n -> n.getNodeId().equals(NodeIds.RootFolder)));
      assertTrue(nodes.stream().anyMatch(n -> n.getNodeId().equals(NodeIds.Server)));
      assertTrue(nodes.stream().anyMatch(n -> n.getNodeId().equals(NodeIds.Aliases)));
    }
  }

  @Test
  public void browseWithReferenceType() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaNode serverNode = addressSpace.getNode(NodeIds.Server);

    BrowseOptions browseOptions =
        addressSpace.getBrowseOptions().copy(b -> b.setReferenceType(ReferenceTypes.HasProperty));

    List<? extends UaNode> nodes = addressSpace.browseNodes(serverNode, browseOptions);

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
  public void browseWithNodeClassMask() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();
    UaNode serverNode = addressSpace.getNode(NodeIds.Server);

    {
      BrowseOptions browseOptions =
          addressSpace
              .getBrowseOptions()
              .copy(b -> b.setNodeClassMask(EnumSet.of(NodeClass.Method)));

      List<? extends UaNode> nodes = addressSpace.browseNodes(serverNode, browseOptions);

      assertFalse(nodes.isEmpty());
      assertTrue(nodes.stream().allMatch(n -> n.getNodeClass() == NodeClass.Method));
    }

    {
      BrowseOptions browseOptions =
          addressSpace
              .getBrowseOptions()
              .copy(b -> b.setNodeClassMask(EnumSet.of(NodeClass.Object)));

      List<? extends UaNode> nodes = addressSpace.browseNodes(serverNode, browseOptions);

      assertFalse(nodes.isEmpty());
      assertTrue(nodes.stream().allMatch(n -> n.getNodeClass() == NodeClass.Object));
    }

    {
      BrowseOptions browseOptions =
          addressSpace
              .getBrowseOptions()
              .copy(b -> b.setNodeClassMask(EnumSet.of(NodeClass.Variable)));

      List<? extends UaNode> nodes = addressSpace.browseNodes(serverNode, browseOptions);

      assertFalse(nodes.isEmpty());
      assertTrue(nodes.stream().allMatch(n -> n.getNodeClass() == NodeClass.Variable));
    }
  }

  @Test
  public void modifyBrowseOptions() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();
    UaNode serverNode = addressSpace.getNode(NodeIds.Server);

    {
      addressSpace.modifyBrowseOptions(b -> b.setNodeClassMask(EnumSet.of(NodeClass.Method)));

      List<? extends UaNode> nodes = addressSpace.browseNodes(serverNode);

      assertFalse(nodes.isEmpty());
      assertTrue(nodes.stream().allMatch(n -> n.getNodeClass() == NodeClass.Method));
    }

    {
      addressSpace.modifyBrowseOptions(b -> b.setNodeClassMask(EnumSet.of(NodeClass.Object)));

      List<? extends UaNode> nodes = addressSpace.browseNodes(serverNode);

      assertFalse(nodes.isEmpty());
      assertTrue(nodes.stream().allMatch(n -> n.getNodeClass() == NodeClass.Object));
    }

    {
      addressSpace.modifyBrowseOptions(b -> b.setNodeClassMask(EnumSet.of(NodeClass.Variable)));

      List<? extends UaNode> nodes = addressSpace.browseNodes(serverNode);

      assertFalse(nodes.isEmpty());
      assertTrue(nodes.stream().allMatch(n -> n.getNodeClass() == NodeClass.Variable));
    }
  }

  @Test
  public void getNode() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaNode serverNode = addressSpace.getNode(NodeIds.Server);
    assertNotNull(serverNode);
    assertInstanceOf(ServerTypeNode.class, serverNode);

    UaNode serverStatusNode = addressSpace.getNode(NodeIds.Server_ServerStatus);
    assertNotNull(serverStatusNode);
    assertInstanceOf(ServerStatusTypeNode.class, serverStatusNode);
  }

  @Test
  public void getObjectNode() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();
    ServerTypeNode serverNode = (ServerTypeNode) addressSpace.getObjectNode(NodeIds.Server);

    assertNotNull(serverNode);
    assertEquals(NodeIds.Server, serverNode.getNodeId());

    // should be cached now, check instance equality
    assertSame(serverNode, addressSpace.getObjectNode(NodeIds.Server));
  }

  @Test
  public void getObjectNodeWithNonObject() {
    AddressSpace addressSpace = client.getAddressSpace();

    assertThrows(UaException.class, () -> addressSpace.getObjectNode(NodeIds.Server_ServerStatus));
  }

  @Test
  public void getVariableNode() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();
    ServerStatusTypeNode serverNode =
        (ServerStatusTypeNode) addressSpace.getVariableNode(NodeIds.Server_ServerStatus);

    assertNotNull(serverNode);
    assertEquals(NodeIds.Server_ServerStatus, serverNode.getNodeId());

    // should be cached now, check instance equality
    assertSame(serverNode, addressSpace.getVariableNode(NodeIds.Server_ServerStatus));
  }

  @Test
  public void getVariableNodeWithNonVariable() {
    AddressSpace addressSpace = client.getAddressSpace();

    assertThrows(UaException.class, () -> addressSpace.getVariableNode(NodeIds.Server));
  }

  @Test
  public void getNodeThatDoesNotExist() {
    UaException exception =
        assertThrows(
            UaException.class,
            () -> client.getAddressSpace().getNode(NodeId.parse("ns=2;s=DoesNotExist")));

    assertEquals(StatusCodes.Bad_NodeIdUnknown, exception.getStatusCode().getValue());
  }

  @Test
  public void getObjectsFolderNode() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();
    UaNode objectsFolderNode = addressSpace.getObjectsFolderNode();

    assertNotNull(objectsFolderNode);
    assertEquals(NodeIds.ObjectsFolder, objectsFolderNode.getNodeId());
  }

  @Test
  public void getRootFolderNode() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();
    UaNode rootFolderNode = addressSpace.getRootFolderNode();

    assertNotNull(rootFolderNode);
    assertEquals(NodeIds.RootFolder, rootFolderNode.getNodeId());
  }

  @Test
  public void getTypesFolderNode() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();
    UaNode typesFolderNode = addressSpace.getTypesFolderNode();

    assertNotNull(typesFolderNode);
    assertEquals(NodeIds.TypesFolder, typesFolderNode.getNodeId());
  }

  @Test
  public void getServerNode() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();
    ServerTypeNode serverNode = addressSpace.getServerNode();

    assertNotNull(serverNode);
    assertEquals(NodeIds.Server, serverNode.getNodeId());
  }
}
