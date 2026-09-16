/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.AcknowledgeableConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.BrowsePath;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.jspecify.annotations.Nullable;

/** Structural discovery shared by condition attachment and adoption. */
final class ConditionNodeTraversal {

  private static final QualifiedName SHELVING_STATE = new QualifiedName(0, "ShelvingState");

  private ConditionNodeTraversal() {}

  record DiscoveredMethod(UaMethodNode node, boolean exclusivelyOwned) {}

  record MethodSurface(Map<QualifiedName, DiscoveredMethod> methods) {

    MethodSurface {
      methods = Map.copyOf(methods);
    }

    @Nullable DiscoveredMethod get(String browseName) {
      return methods.get(new QualifiedName(0, browseName));
    }
  }

  /**
   * Discover Method nodes in the two scopes served by condition behavior: direct children of the
   * Condition and direct children of its ShelvingState.
   *
   * <p>The surface includes shared type Methods and Methods shared by multiple instances. Each
   * entry records whether the Method is provably an instance copy owned only by its immediate
   * parent, so later lifecycle work cannot mutate or delete a shared node.
   */
  static MethodSurface discoverMethodSurface(ConditionTypeNode root) throws UaException {

    Map<QualifiedName, DiscoveredMethod> methods = new HashMap<>();
    UaNode shelvingState = null;

    for (UaNode child : directChildren(root, false)) {
      if (child instanceof UaMethodNode method) {
        addMethod(methods, root, method);
      } else if (SHELVING_STATE.equals(child.getBrowseName())) {
        shelvingState = child;
      }
    }

    if (shelvingState != null) {
      for (UaNode child : directChildren(shelvingState, false)) {
        if (child instanceof UaMethodNode method) {
          addMethod(methods, shelvingState, method);
        }
      }
    }

    return new MethodSurface(methods);
  }

  /** Validate the mandatory method surface implied by the state present on {@code root}. */
  static void validateMethodSurface(ConditionTypeNode root, MethodSurface methods)
      throws UaException {

    requireMethod(methods, "Enable");
    requireMethod(methods, "Disable");
    requireMethod(methods, "AddComment");

    if (root instanceof AcknowledgeableConditionTypeNode acknowledgeable) {
      requireMethod(methods, "Acknowledge");
      if (acknowledgeable.getConfirmedStateNode() != null) {
        requireMethod(methods, "Confirm");
      }
    }

    if (root instanceof AlarmConditionTypeNode alarm && alarm.getShelvingStateNode() != null) {
      requireMethod(methods, "TimedShelve");
      requireMethod(methods, "OneShotShelve");
      requireMethod(methods, "Unshelve");
    }
  }

  /**
   * Map same-manager Aggregate descendants to their existing BrowsePaths for in-place reuse. Shared
   * same-manager Methods and their argument Properties are included so mixed COPY/shared graphs
   * remain shared through adoption; their ownership provenance remains non-exclusive in the Method
   * surface. Duplicate paths, cycles, and foreign-manager children are rejected before adoption
   * mutates the graph.
   */
  static Map<BrowsePath, NodeId> discoverAssignedNodeIds(ConditionTypeNode root)
      throws UaException {

    Map<BrowsePath, NodeId> assignments = new LinkedHashMap<>();
    assignments.put(BrowsePath.root(), root.getNodeId());

    walk(root, BrowsePath.root(), assignments, new HashSet<>());

    return Map.copyOf(assignments);
  }

  private static void walk(
      UaNode parent, BrowsePath parentPath, Map<BrowsePath, NodeId> assignments, Set<NodeId> active)
      throws UaException {

    if (!active.add(parent.getNodeId())) {
      throw invalid("cycle in owned condition graph at " + parentPath);
    }

    for (UaNode child : directChildren(parent, true)) {
      if (child instanceof UaMethodNode method
          && !isExclusivelyOwnedBy(parent, method)
          && child.getNodeManager() != parent.getNodeManager()) {
        continue;
      }
      if (child.getNodeManager() != parent.getNodeManager()) {
        throw invalid(
            "foreign NodeManager child " + child.getNodeId() + " under " + parent.getNodeId());
      }

      BrowsePath childPath = parentPath.append(child.getBrowseName());
      NodeId previous = assignments.putIfAbsent(childPath, child.getNodeId());
      if (previous != null && !previous.equals(child.getNodeId())) {
        throw invalid(
            "duplicate BrowsePath "
                + childPath
                + " resolves to both "
                + previous
                + " and "
                + child.getNodeId());
      }
      if (previous == null) {
        walk(child, childPath, assignments, active);
      }
    }

    active.remove(parent.getNodeId());
  }

  private static Set<UaNode> directChildren(UaNode parent, boolean rejectUnresolved)
      throws UaException {

    Set<UaNode> children = new HashSet<>();
    NodeManager<UaNode> owner = parent.getNodeManager();
    AddressSpaceManager addressSpaceManager =
        parent.getNodeContext().getServer().getAddressSpaceManager();

    for (Reference reference : parent.getReferences()) {
      if (!reference.isForward() || !isAggregate(parent, reference.getReferenceTypeId())) {
        continue;
      }

      var child =
          owner
              .getNode(
                  reference.getTargetNodeId(),
                  parent.getNodeContext().getServer().getNamespaceTable())
              .or(
                  () ->
                      addressSpaceManager.getManagedNode(reference.getTargetNodeId()).stream()
                          .filter(UaNode.class::isInstance)
                          .map(UaNode.class::cast)
                          .findFirst());

      if (child.isPresent()) {
        children.add(child.get());
      } else if (rejectUnresolved) {
        throw invalid(
            "unresolved child " + reference.getTargetNodeId() + " under " + parent.getNodeId());
      }
    }

    return children;
  }

  private static boolean isExclusivelyOwnedBy(UaNode parent, UaMethodNode method) {
    if (method.getNodeManager() != parent.getNodeManager()) {
      return false;
    }

    var aggregateParents =
        method.getReferences().stream()
            .filter(Reference::isInverse)
            .filter(reference -> isAggregate(method, reference.getReferenceTypeId()))
            .toList();

    if (aggregateParents.size() != 1
        || !aggregateParents.get(0).getTargetNodeId().equals(parent.getNodeId().expanded())) {
      return false;
    }

    return !(parent instanceof UaObjectTypeNode);
  }

  static boolean isAggregate(UaNode node, NodeId referenceTypeId) {
    if (NodeIds.HasComponent.equals(referenceTypeId)
        || NodeIds.HasProperty.equals(referenceTypeId)
        || NodeIds.HasOrderedComponent.equals(referenceTypeId)) {
      return true;
    }

    ReferenceTypeTree referenceTypeTree = node.getNodeContext().getServer().getReferenceTypeTree();

    return referenceTypeTree != null
        && referenceTypeTree.isSubtypeOf(referenceTypeId, NodeIds.Aggregates);
  }

  private static void addMethod(
      Map<QualifiedName, DiscoveredMethod> methods, UaNode parent, UaMethodNode method)
      throws UaException {

    QualifiedName browseName = method.getBrowseName();
    if (browseName.namespaceIndex().intValue() != 0) {
      return;
    }

    DiscoveredMethod discovered =
        new DiscoveredMethod(method, isExclusivelyOwnedBy(parent, method));
    DiscoveredMethod previous = methods.putIfAbsent(browseName, discovered);
    if (previous != null && !previous.node().getNodeId().equals(method.getNodeId())) {
      throw invalid(
          "duplicate instance Method "
              + browseName
              + ": "
              + previous.node().getNodeId()
              + " and "
              + method.getNodeId());
    }
  }

  private static void requireMethod(MethodSurface methods, String browseName) throws UaException {
    QualifiedName name = new QualifiedName(0, browseName);
    if (!methods.methods().containsKey(name)) {
      throw invalid("missing instance Method " + name);
    }
  }

  private static UaException invalid(String message) {
    return new UaException(StatusCodes.Bad_InvalidState, message);
  }
}
