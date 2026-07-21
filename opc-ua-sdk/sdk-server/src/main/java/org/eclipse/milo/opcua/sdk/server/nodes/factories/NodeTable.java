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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.jspecify.annotations.Nullable;

class NodeTable {

  final LinkedHashMap<BrowsePath, NodeId> nodes = new LinkedHashMap<>();

  /**
   * Lazily-built NodeId → shallowest declaration path index for {@link #getDeclarationPath}, so
   * reference resolution does not rescan the whole {@link #nodes} map (once per non-hierarchical
   * inter-declaration reference) on every instantiation. Invalidated whenever {@code nodes}
   * changes.
   *
   * <p>Volatile because NodeTables are shared across threads via NodeFactory's static
   * InstanceDeclarationHierarchy cache; the field holds an immutable map so a racing rebuild is
   * only wasted work, and a reader always observes a fully-initialized index.
   */
  private volatile @Nullable Map<NodeId, BrowsePath> declarationPaths;

  void addNode(BrowsePath browsePath, NodeId nodeId) {
    nodes.put(browsePath, nodeId);
    declarationPaths = null;
  }

  /** Find the path of an instance declaration, excluding the root type definition itself. */
  @Nullable BrowsePath getDeclarationPath(NodeId nodeId) {
    Map<NodeId, BrowsePath> index = declarationPaths;
    if (index == null) {
      index = buildDeclarationPaths();
      declarationPaths = index;
    }
    return index.get(nodeId);
  }

  private Map<NodeId, BrowsePath> buildDeclarationPaths() {
    Map<NodeId, BrowsePath> index = new HashMap<>();
    for (Map.Entry<BrowsePath, NodeId> entry : nodes.entrySet()) {
      BrowsePath path = entry.getKey();
      if (path.parent == null) {
        continue;
      }
      // A declaration reached through a non-hierarchical reference may also be reached through its
      // actual hierarchical declaration path. Prefer that shallower canonical path instead of
      // manufacturing a nested duplicate such as ActiveState/LimitState.
      index.merge(entry.getValue(), path, NodeTable::min);
    }
    return Map.copyOf(index);
  }

  private static BrowsePath min(BrowsePath a, BrowsePath b) {
    return depth(a) <= depth(b) ? a : b;
  }

  private static int depth(BrowsePath browsePath) {
    int depth = 0;
    for (BrowsePath path = browsePath; path.parent != null; path = path.parent) {
      depth++;
    }
    return depth;
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
