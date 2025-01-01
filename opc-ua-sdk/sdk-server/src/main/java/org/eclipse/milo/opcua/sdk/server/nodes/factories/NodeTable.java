/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.factories;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Tree;

class NodeTable {

  final LinkedHashMap<BrowsePath, NodeId> nodes = new LinkedHashMap<>();

  void addNode(BrowsePath browsePath, NodeId nodeId) {
    nodes.put(browsePath, nodeId);
  }

  Tree<BrowsePath> getBrowsePathTree() {
    BrowsePath parentBrowsePath =
        nodes.keySet().stream().filter(b -> b.parent == null).findFirst().orElse(null);

    Map<BrowsePath, List<BrowsePath>> grouping =
        nodes.keySet().stream()
            .filter(b -> b.parent != null)
            .collect(Collectors.groupingBy(b -> b.parent));

    Tree<BrowsePath> root = new Tree<>(null, parentBrowsePath);

    addChildren(grouping, root);

    return root;
  }

  private static void addChildren(
      Map<BrowsePath, List<BrowsePath>> grouping, Tree<BrowsePath> parent) {
    BrowsePath parentPath = parent.getValue();

    List<BrowsePath> childPaths = grouping.get(parentPath);

    if (childPaths != null) {
      childPaths.forEach(
          childPath -> {
            Tree<BrowsePath> child = parent.addChild(childPath);

            addChildren(grouping, child);
          });
    }
  }

  static NodeTable merge(NodeTable table1, NodeTable table2) {
    NodeTable mergedNodeTable = new NodeTable();

    // Put all the second table's nodes first
    mergedNodeTable.nodes.putAll(table2.nodes);

    // Allow the first table's nodes to overwrite
    mergedNodeTable.nodes.putAll(table1.nodes);

    return mergedNodeTable;
  }
}
