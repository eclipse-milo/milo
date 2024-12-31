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
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.ReferenceTypes;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.jspecify.annotations.Nullable;

public class DataTypeTreeBuilder {

  /**
   * Build a {@link DataTypeTree} by recursively traversing the {@link UaDataTypeNode} hierarchy
   * starting at {@link NodeIds#BaseDataType}.
   *
   * @param server the {@link OpcUaServer} to build the tree for.
   * @return the {@link DataTypeTree}.
   */
  public static DataTypeTree build(OpcUaServer server) {
    UaNode rootNode =
        server.getAddressSpaceManager().getManagedNode(NodeIds.BaseDataType).orElseThrow();

    var tree =
        new Tree<DataType>(null, new ServerDataType((UaDataTypeNode) rootNode, null, null, null));

    addChildren(tree, server);

    return new DataTypeTree(tree);
  }

  private static void addChildren(Tree<DataType> tree, OpcUaServer server) {
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

    for (UaNode childNode : childNodes) {
      UaDataTypeNode dataTypeNode = (UaDataTypeNode) childNode;

      NodeId binaryEncodingId = null;
      NodeId xmlEncodingId = null;
      NodeId jsonEncodingId = null;

      for (Reference reference : dataTypeNode.getReferences()) {
        if (reference.getReferenceTypeId().equals(NodeIds.HasEncoding)) {
          QualifiedName browseName =
              server
                  .getAddressSpaceManager()
                  .getManagedNode(reference.getTargetNodeId())
                  .map(UaNode::getBrowseName)
                  .orElse(null);

          if (DataTypeEncoding.BINARY_ENCODING_NAME.equals(browseName)) {
            binaryEncodingId =
                reference.getTargetNodeId().toNodeId(server.getNamespaceTable()).orElse(null);
          } else if (DataTypeEncoding.XML_ENCODING_NAME.equals(browseName)) {
            xmlEncodingId =
                reference.getTargetNodeId().toNodeId(server.getNamespaceTable()).orElse(null);
          } else if (DataTypeEncoding.JSON_ENCODING_NAME.equals(browseName)) {
            jsonEncodingId =
                reference.getTargetNodeId().toNodeId(server.getNamespaceTable()).orElse(null);
          }
        }
      }

      tree.addChild(
          new ServerDataType(dataTypeNode, binaryEncodingId, xmlEncodingId, jsonEncodingId));
    }

    for (Tree<DataType> child : tree.getChildren()) {
      addChildren(child, server);
    }
  }

  private static class ServerDataType implements DataType {

    private final UaDataTypeNode node;
    private final @Nullable NodeId binaryEncodingId;
    private final @Nullable NodeId xmlEncodingId;
    private final @Nullable NodeId jsonEncodingId;

    public ServerDataType(
        UaDataTypeNode node,
        @Nullable NodeId binaryEncodingId,
        @Nullable NodeId xmlEncodingId,
        @Nullable NodeId jsonEncodingId) {

      this.node = node;
      this.binaryEncodingId = binaryEncodingId;
      this.xmlEncodingId = xmlEncodingId;
      this.jsonEncodingId = jsonEncodingId;
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
    public @Nullable NodeId getBinaryEncodingId() {
      return binaryEncodingId;
    }

    @Override
    public @Nullable NodeId getXmlEncodingId() {
      return xmlEncodingId;
    }

    @Override
    public @Nullable NodeId getJsonEncodingId() {
      return jsonEncodingId;
    }

    @Override
    public DataTypeDefinition getDataTypeDefinition() {
      return node.getDataTypeDefinition();
    }

    @Override
    public Boolean isAbstract() {
      return node.getIsAbstract();
    }
  }
}
