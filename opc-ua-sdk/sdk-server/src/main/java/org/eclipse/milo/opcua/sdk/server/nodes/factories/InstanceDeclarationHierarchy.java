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

import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;

public class InstanceDeclarationHierarchy {

  private final NodeId typeId;
  private final NodeTable nodeTable;
  private final ReferenceTable referenceTable;

  private InstanceDeclarationHierarchy(
      NodeId typeId, NodeTable nodeTable, ReferenceTable referenceTable) {

    this.typeId = typeId;
    this.nodeTable = nodeTable;
    this.referenceTable = referenceTable;
  }

  NodeTable getNodeTable() {
    return nodeTable;
  }

  ReferenceTable getReferenceTable() {
    return referenceTable;
  }

  private InstanceDeclarationHierarchy merge(InstanceDeclarationHierarchy parent) {
    NodeTable mergedNodeTable = NodeTable.merge(this.nodeTable, parent.nodeTable);

    ReferenceTable mergedReferenceTable =
        ReferenceTable.merge(this.referenceTable, parent.referenceTable);

    return new InstanceDeclarationHierarchy(typeId, mergedNodeTable, mergedReferenceTable);
  }

  public static InstanceDeclarationHierarchy create(
      AddressSpaceManager addressSpaceManager,
      NamespaceTable namespaceTable,
      NodeId typeDefinitionId) {

    Builder builder = new Builder(addressSpaceManager, namespaceTable);

    return builder.build(typeDefinitionId);
  }

  static class Builder {

    private final NodeTable nodeTable = new NodeTable();
    private final ReferenceTable referenceTable = new ReferenceTable();

    private final AddressSpaceManager addressSpaceManager;
    private final NamespaceTable namespaceTable;

    Builder(AddressSpaceManager addressSpaceManager, NamespaceTable namespaceTable) {
      this.addressSpaceManager = addressSpaceManager;
      this.namespaceTable = namespaceTable;
    }

    public InstanceDeclarationHierarchy build(NodeId typeDefinitionId) {
      Optional<InstanceDeclarationHierarchy> parentIdh =
          addressSpaceManager.getManagedReferences(typeDefinitionId).stream()
              .filter(r -> r.isInverse() && NodeIds.HasSubtype.equals(r.getReferenceTypeId()))
              .findFirst()
              .flatMap(r -> r.getTargetNodeId().toNodeId(namespaceTable))
              .map(
                  parentTypeId ->
                      InstanceDeclarationHierarchy.create(
                          addressSpaceManager, namespaceTable, parentTypeId));

      final InstanceDeclarationHierarchy idh = buildHierarchyForType(typeDefinitionId);

      return parentIdh.map(idh::merge).orElse(idh);
    }

    private InstanceDeclarationHierarchy buildHierarchyForType(NodeId typeDefinitionId) {
      BrowsePath browsePath = BrowsePath.ROOT;

      nodeTable.addNode(browsePath, typeDefinitionId);
      referenceTable.addReference(
          browsePath, NodeIds.HasTypeDefinition, typeDefinitionId.expanded());

      addModeledNodes(typeDefinitionId, browsePath);

      return new InstanceDeclarationHierarchy(typeDefinitionId, nodeTable, referenceTable);
    }

    private void addModeledNodes(NodeId typeDefinitionId, BrowsePath parentPath) {
      List<Reference> forwardReferences =
          addressSpaceManager.getManagedReferences(typeDefinitionId).stream()
              .filter(Reference::isForward)
              .toList();

      forwardReferences.forEach(
          reference ->
              addressSpaceManager
                  .getManagedNode(reference.getTargetNodeId())
                  .ifPresent(
                      node -> {
                        // All InstanceDeclarations of the InstanceDeclarationHierarchy and all
                        // Nodes referenced with a
                        // non-hierarchical Reference from such an InstanceDeclaration are added to
                        // the table.
                        // Hierarchical References to Nodes without a ModellingRule are not
                        // considered.

                        BrowsePath browsePath = new BrowsePath(parentPath, node.getBrowseName());

                        ReferenceTypeTree referenceTypeTree =
                            node.getNodeContext().getServer().getReferenceTypeTree();

                        if (isInstanceDeclaration(node)) {
                          nodeTable.addNode(browsePath, node.getNodeId());
                          referenceTable.addReference(
                              parentPath, reference.getReferenceTypeId(), browsePath);

                          node.getReferences().stream()
                              .filter(
                                  r ->
                                      referenceTypeTree.isSubtypeOf(
                                          r.getReferenceTypeId(),
                                          NodeIds.NonHierarchicalReferences))
                              .forEach(
                                  r ->
                                      referenceTable.addReference(
                                          browsePath, r.getReferenceTypeId(), r.getTargetNodeId()));

                          // Recursively add any additional instance declarations, on both this
                          // instance declaration and its type definition if applicable.

                          addModeledNodes(node.getNodeId(), browsePath);

                          Optional<ExpandedNodeId> instanceDeclarationTypeDefinitionId =
                              node.getReferences().stream()
                                  .filter(
                                      r -> NodeIds.HasTypeDefinition.equals(r.getReferenceTypeId()))
                                  .findFirst()
                                  .map(Reference::getTargetNodeId);

                          instanceDeclarationTypeDefinitionId
                              .flatMap(xni -> xni.toNodeId(namespaceTable))
                              .ifPresent(id -> addModeledNodes(id, browsePath));
                        }
                      }));
    }

    private static boolean isInstanceDeclaration(UaNode potentialMemberNode) {
      NodeClass nodeClass = potentialMemberNode.getNodeClass();

      boolean methodOrObjectOrVariable =
          nodeClass == NodeClass.Method
              || nodeClass == NodeClass.Object
              || nodeClass == NodeClass.Variable;

      return methodOrObjectOrVariable && hasModellingRule(potentialMemberNode);
    }

    private static boolean hasModellingRule(UaNode potentialMemberNode) {
      return potentialMemberNode.getReferences().stream()
          .filter(r -> NodeIds.HasModellingRule.equals(r.getReferenceTypeId()))
          .anyMatch(
              r ->
                  NodeIds.ModellingRule_Mandatory.equalTo(r.getTargetNodeId())
                      || NodeIds.ModellingRule_Optional.equalTo(r.getTargetNodeId()));
    }
  }
}
