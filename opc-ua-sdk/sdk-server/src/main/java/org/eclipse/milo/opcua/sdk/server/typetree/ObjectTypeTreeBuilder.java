/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.typetree;

import java.util.List;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ObjectType;
import org.eclipse.milo.opcua.sdk.core.typetree.ObjectTypeTree;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.ReferenceTypes;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.util.Tree;

public class ObjectTypeTreeBuilder {

  /**
   * Build an {@link ObjectTypeTree} by recursively traversing the {@link UaObjectTypeNode}
   * hierarchy starting at {@link NodeIds#BaseObjectType}.
   *
   * @param server the {@link OpcUaServer} to build the tree for.
   * @return the {@link ObjectTypeTree}.
   */
  public static ObjectTypeTree build(OpcUaServer server) {
    UaNode rootNode =
        server.getAddressSpaceManager().getManagedNode(NodeIds.BaseObjectType).orElseThrow();

    var tree = new Tree<ObjectType>(null, new ServerObjectType((UaObjectTypeNode) rootNode));

    addChildren(tree, server);

    return new ObjectTypeTree(tree);
  }

  private static void addChildren(Tree<ObjectType> tree, OpcUaServer server) {
    NodeId nodeId = tree.getValue().getNodeId();

    List<Reference> references =
        server
            .getAddressSpaceManager()
            .getManagedReferences(
                nodeId,
                r -> r.isForward() && r.getReferenceTypeId().equals(ReferenceTypes.HasSubtype));

    List<UaNode> childNodes =
        references.stream()
            .flatMap(
                r -> server.getAddressSpaceManager().getManagedNode(r.getTargetNodeId()).stream())
            .toList();

    for (UaNode childNode : childNodes) {
      UaObjectTypeNode objectTypeNode = (UaObjectTypeNode) childNode;

      tree.addChild(new ServerObjectType(objectTypeNode));
    }

    for (Tree<ObjectType> child : tree.getChildren()) {
      addChildren(child, server);
    }
  }

  private static class ServerObjectType implements ObjectType {

    private final UaObjectTypeNode node;

    public ServerObjectType(UaObjectTypeNode node) {
      this.node = node;
    }

    @Override
    public QualifiedName getBrowseName() {
      return node.getBrowseName();
    }

    @Override
    public NodeId getNodeId() {
      return node.getNodeId();
    }

    @Override
    public Boolean isAbstract() {
      return node.getIsAbstract();
    }
  }
}
