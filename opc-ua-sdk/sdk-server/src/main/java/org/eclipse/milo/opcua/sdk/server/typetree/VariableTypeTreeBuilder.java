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
import org.eclipse.milo.opcua.sdk.core.typetree.VariableType;
import org.eclipse.milo.opcua.sdk.core.typetree.VariableTypeTree;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.ReferenceTypes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Tree;

public class VariableTypeTreeBuilder {

  /**
   * Build a {@link VariableTypeTree} by recursively traversing the {@link UaVariableTypeNode}
   * hierarchy starting at {@link NodeIds#BaseVariableType}.
   *
   * @param server the {@link OpcUaServer} to build the tree for.
   * @return the {@link VariableTypeTree}.
   */
  public static VariableTypeTree build(OpcUaServer server) {
    UaNode rootNode =
        server.getAddressSpaceManager().getManagedNode(NodeIds.BaseVariableType).orElseThrow();

    var tree = new Tree<VariableType>(null, new ServerVariableType((UaVariableTypeNode) rootNode));

    addChildren(tree, server);

    return new VariableTypeTree(tree);
  }

  private static void addChildren(Tree<VariableType> tree, OpcUaServer server) {
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
      UaVariableTypeNode variableTypeNode = (UaVariableTypeNode) childNode;

      tree.addChild(new ServerVariableType(variableTypeNode));
    }

    for (Tree<VariableType> child : tree.getChildren()) {
      addChildren(child, server);
    }
  }

  private static class ServerVariableType implements VariableType {

    private final UaVariableTypeNode node;

    public ServerVariableType(UaVariableTypeNode node) {
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
    public DataValue getValue() {
      return node.getValue();
    }

    @Override
    public NodeId getDataType() {
      return node.getDataType();
    }

    @Override
    public Integer getValueRank() {
      return node.getValueRank();
    }

    @Override
    public UInteger[] getArrayDimensions() {
      return node.getArrayDimensions();
    }

    @Override
    public Boolean isAbstract() {
      return node.getIsAbstract();
    }
  }
}
