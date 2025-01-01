/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.typetree;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.ReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceType;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.ReferenceTypes;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.jspecify.annotations.Nullable;

public class ReferenceTypeTreeBuilder {

  public static ReferenceTypeTree build(OpcUaServer server) {
    UaNode rootNode =
        server.getAddressSpaceManager().getManagedNode(NodeIds.References).orElseThrow();

    var tree =
        new Tree<ReferenceType>(
            null, new ServerReferenceType(null, (UaReferenceTypeNode) rootNode));

    addChildren(tree, server);

    return new ReferenceTypeTree(tree);
  }

  private static void addChildren(Tree<ReferenceType> tree, OpcUaServer server) {
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
            .collect(Collectors.toList());

    childNodes.forEach(
        node -> {
          if (node instanceof ReferenceTypeNode) {
            Tree<ReferenceType> childTree =
                tree.addChild(new ServerReferenceType(nodeId, (ReferenceTypeNode) node));

            addChildren(childTree, server);
          }
        });
  }

  private static class ServerReferenceType implements ReferenceType {

    private final @Nullable NodeId parentNodeId;
    private final ReferenceTypeNode node;

    private ServerReferenceType(@Nullable NodeId parentNodeId, ReferenceTypeNode node) {
      this.parentNodeId = parentNodeId;
      this.node = node;
    }

    public ReferenceTypeNode getNode() {
      return node;
    }

    @Override
    public NodeId getNodeId() {
      return node.getNodeId();
    }

    @Override
    public QualifiedName getBrowseName() {
      return node.getBrowseName();
    }

    @Override
    public Optional<String> getInverseName() {
      String name = node.getInverseName().getText();
      return Optional.ofNullable(name);
    }

    @Override
    public boolean isSymmetric() {
      return node.getSymmetric();
    }

    @Override
    public boolean isAbstract() {
      return node.getIsAbstract();
    }

    @Override
    public Optional<NodeId> getSupertypeId() {
      return Optional.ofNullable(parentNodeId);
    }
  }
}
