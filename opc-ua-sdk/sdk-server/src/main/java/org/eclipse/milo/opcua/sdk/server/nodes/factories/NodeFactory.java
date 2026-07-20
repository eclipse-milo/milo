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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectTypeNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.jspecify.annotations.Nullable;
import org.slf4j.LoggerFactory;

/**
 * Legacy type-instantiation factory, retained with frozen behavior for compatibility.
 *
 * <p>Known limitations, kept as-is rather than fixed (each is a documented behavior of this
 * implementation; the replacement corrects all of them):
 *
 * <ul>
 *   <li>Excluding an optional member still creates and stores its mandatory descendants,
 *       unreachable from the instance root.
 *   <li>A member typed as a subtype of its declaring type is instantiated without the
 *       supertype-declared mandatory members of that subtype.
 *   <li>When a type declares a child with the same BrowseName as an on-declaration override, the
 *       type's entry wins and the override's attributes are discarded.
 *   <li>Optional Methods are created unconditionally; {@link
 *       InstantiationCallback#includeOptionalNode} is never consulted for them.
 *   <li>Instantiating over an existing NodeId silently replaces the node and leaves the previous
 *       node's reference rows in place.
 *   <li>Attribute propagation from declarations is incomplete: MinimumSamplingInterval,
 *       Historizing, AccessLevelEx, RolePermissions, UserRolePermissions, and AccessRestrictions
 *       are not copied.
 *   <li>The instance-declaration-hierarchy cache is JVM-global, so servers with colliding type
 *       NodeIds can observe each other's cached hierarchies.
 * </ul>
 *
 * @deprecated use {@link org.eclipse.milo.opcua.sdk.server.nodes.instantiation.NodeInstantiator}
 *     (obtained from {@code OpcUaServer.getNodeInstantiator()}) and {@link
 *     org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest}. See {@code
 *     docs/features/node-instantiation-migration.md} for a mapping of every legacy pattern to the
 *     new API.
 */
@Deprecated
public class NodeFactory {

  private static final Cache<NodeId, InstanceDeclarationHierarchy> IDH_CACHE =
      CacheBuilder.newBuilder().maximumSize(1024).expireAfterAccess(1, TimeUnit.HOURS).build();

  /**
   * Invalidate the cached {@link InstanceDeclarationHierarchy} for {@code typeDefinitionId}.
   *
   * @param typeDefinitionId the {@link NodeId} type definition to invalidate.
   */
  public static void invalidateCachedIdh(NodeId typeDefinitionId) {
    IDH_CACHE.invalidate(typeDefinitionId);
  }

  private final UaNodeContext context;
  private final ObjectTypeManager objectTypeManager;
  private final VariableTypeManager variableTypeManager;

  public NodeFactory(UaNodeContext context) {
    this(
        context,
        context.getServer().getObjectTypeManager(),
        context.getServer().getVariableTypeManager());
  }

  public NodeFactory(
      UaNodeContext context,
      ObjectTypeManager objectTypeManager,
      VariableTypeManager variableTypeManager) {

    this.context = context;
    this.objectTypeManager = objectTypeManager;
    this.variableTypeManager = variableTypeManager;
  }

  public UaNode createNode(NodeId rootNodeId, NodeId typeDefinitionId) throws UaException {

    return createNode(rootNodeId, typeDefinitionId, new InstantiationCallback() {});
  }

  public UaNode createNode(
      NodeId rootNodeId, NodeId typeDefinitionId, InstantiationCallback instantiationCallback)
      throws UaException {

    Tree<UaNode> nodeTree = createNodeTree(rootNodeId, typeDefinitionId, instantiationCallback);

    return nodeTree.getValue();
  }

  public UaNode createNode(NodeId rootNodeId, ExpandedNodeId typeDefinitionId) throws UaException {

    return createNode(rootNodeId, typeDefinitionId, new InstantiationCallback() {});
  }

  public UaNode createNode(
      NodeId rootNodeId,
      ExpandedNodeId typeDefinitionId,
      InstantiationCallback instantiationCallback)
      throws UaException {

    NodeId localTypeDefinitionId =
        typeDefinitionId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaException(
                        StatusCodes.Bad_NodeIdUnknown,
                        "typeDefinitionId not local: " + typeDefinitionId));

    return createNode(rootNodeId, localTypeDefinitionId, instantiationCallback);
  }

  /**
   * @deprecated use {@link org.eclipse.milo.opcua.sdk.server.nodes.instantiation.NodeInstantiator}
   *     and inspect the returned {@code InstantiationResult} instead of a node tree.
   */
  @Deprecated
  public Tree<UaNode> createNodeTree(
      NodeId rootNodeId, NodeId typeDefinitionId, InstantiationCallback instantiationCallback)
      throws UaException {

    AddressSpaceManager addressSpaceManager = context.getServer().getAddressSpaceManager();

    if (addressSpaceManager.getManagedNode(typeDefinitionId).isEmpty()) {
      throw new UaException(
          StatusCodes.Bad_NodeIdUnknown, "unknown type definition: " + typeDefinitionId);
    }

    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();

    InstanceDeclarationHierarchy idh;

    try {
      idh =
          IDH_CACHE.get(
              typeDefinitionId,
              () -> {
                LoggerFactory.getLogger(NodeFactory.class)
                    .debug(
                        "InstanceDeclarationHierarchy cache " + "miss for typeDefinitionId={}",
                        typeDefinitionId);

                return InstanceDeclarationHierarchy.create(
                    addressSpaceManager, namespaceTable, typeDefinitionId);
              });
    } catch (ExecutionException e) {
      throw new UaException(StatusCodes.Bad_InternalError, e);
    }

    NodeTable nodeTable = idh.getNodeTable();
    ReferenceTable referenceTable = idh.getReferenceTable();

    Map<BrowsePath, UaNode> nodes = new HashMap<>();

    for (Map.Entry<BrowsePath, NodeId> entry : nodeTable.nodes.entrySet()) {
      BrowsePath browsePath = entry.getKey();
      NodeId nodeId = entry.getValue();

      UaNode node = addressSpaceManager.getManagedNode(nodeId).orElse(null);

      if (browsePath.parent == null) {
        // Root Node of hierarchy will be the ObjectType or VariableType to be instantiated

        if (node instanceof UaObjectTypeNode) {
          UaNode instance = instanceFromTypeDefinition(rootNodeId, (UaObjectTypeNode) node);

          nodes.put(browsePath, instance);
        } else if (node instanceof UaVariableTypeNode) {
          UaNode instance = instanceFromTypeDefinition(rootNodeId, (UaVariableTypeNode) node);

          nodes.put(browsePath, instance);
        } else {
          throw new UaException(StatusCodes.Bad_InternalError);
        }
      } else {
        // Non-root Nodes are all instance declarations.
        // Iteration is parent-before-child, so if the parent declaration was an optional the
        // callback declined, none of its children are instantiated either; instantiating them
        // would register orphaned nodes that belong to no hierarchy.
        if (!nodes.containsKey(browsePath.parent)) {
          continue;
        }

        NodeId instanceNodeId = instanceNodeId(rootNodeId, browsePath);

        if (node instanceof UaMethodNode declaration) {
          UaMethodNode instance =
              new UaMethodNode(
                  context,
                  instanceNodeId,
                  declaration.getBrowseName(),
                  declaration.getDisplayName(),
                  declaration.getDescription(),
                  declaration.getWriteMask(),
                  declaration.getUserWriteMask(),
                  declaration.isExecutable(),
                  declaration.isUserExecutable());

          nodes.put(browsePath, instance);
        } else if (node instanceof UaObjectNode declaration) {
          ExpandedNodeId instanceTypeDefinitionId = getTypeDefinition(referenceTable, browsePath);

          UaNode typeDefinitionNode =
              addressSpaceManager.getManagedNode(instanceTypeDefinitionId).orElse(null);

          if (typeDefinitionNode instanceof ObjectTypeNode) {
            boolean optional = isOptionalDeclaration(declaration);

            if (!optional
                || instantiationCallback.includeOptionalNode(
                    typeDefinitionNode.getNodeId(), declaration.getBrowseName())) {

              UaObjectNode instance =
                  instanceFromTypeDefinition(instanceNodeId, (ObjectTypeNode) typeDefinitionNode);

              instance.setBrowseName(declaration.getBrowseName());
              instance.setDisplayName(declaration.getDisplayName());
              instance.setDescription(declaration.getDescription());
              instance.setWriteMask(declaration.getWriteMask());
              instance.setUserWriteMask(declaration.getUserWriteMask());
              instance.setEventNotifier(declaration.getEventNotifier());

              nodes.put(browsePath, instance);
            }
          } else {
            throw new UaException(
                StatusCodes.Bad_InternalError,
                "expected type definition for " + instanceTypeDefinitionId);
          }
        } else if (node instanceof UaVariableNode declaration) {
          ExpandedNodeId instanceTypeDefinitionId = getTypeDefinition(referenceTable, browsePath);

          UaNode typeDefinitionNode =
              addressSpaceManager.getManagedNode(instanceTypeDefinitionId).orElse(null);

          if (typeDefinitionNode instanceof VariableTypeNode) {
            boolean optional = isOptionalDeclaration(declaration);

            if (!optional
                || instantiationCallback.includeOptionalNode(
                    typeDefinitionNode.getNodeId(), declaration.getBrowseName())) {
              UaVariableNode instance =
                  instanceFromTypeDefinition(instanceNodeId, (VariableTypeNode) typeDefinitionNode);

              instance.setBrowseName(declaration.getBrowseName());
              instance.setDisplayName(declaration.getDisplayName());
              instance.setDescription(declaration.getDescription());
              instance.setWriteMask(declaration.getWriteMask());
              instance.setUserWriteMask(declaration.getUserWriteMask());
              instance.setValue(declaration.getValue());
              instance.setDataType(declaration.getDataType());
              instance.setValueRank(declaration.getValueRank());
              instance.setArrayDimensions(declaration.getArrayDimensions());
              instance.setAccessLevel(declaration.getAccessLevel());
              instance.setUserAccessLevel(declaration.getUserAccessLevel());

              nodes.put(browsePath, instance);
            }
          } else {
            throw new UaException(
                StatusCodes.Bad_InternalError,
                "expected type definition for " + instanceTypeDefinitionId);
          }
        } else {
          throw new UaException(
              StatusCodes.Bad_InternalError, "not an instance declaration: " + node);
        }
      }
    }

    nodes.forEach(
        (browsePath, node) -> {
          List<ReferenceTable.RefRow> references = referenceTable.getReferences(browsePath);

          references.forEach(
              t -> {
                NodeId referenceTypeId = t.nodeId;
                ReferenceTable.RefTarget target = t.target;

                if (!NodeIds.HasModellingRule.equals(referenceTypeId)) {
                  if (target.targetNodeId != null) {
                    node.addReference(
                        new Reference(
                            node.getNodeId(), referenceTypeId, target.targetNodeId, true));
                  } else {
                    BrowsePath targetPath = target.targetPath;

                    UaNode targetNode = nodes.get(targetPath);

                    if (targetNode != null) {
                      node.addReference(
                          new Reference(
                              node.getNodeId(),
                              referenceTypeId,
                              targetNode.getNodeId().expanded(),
                              true));
                    }
                  }
                }
              });

          context.getNodeManager().addNode(node);
        });

    Tree<UaNode> nodeTree = nodeTable.getBrowsePathTree().map(nodes::get);

    notifyInstantiationCallback(nodeTree, instantiationCallback);

    return nodeTree;
  }

  protected void notifyInstantiationCallback(
      Tree<UaNode> nodeTree, InstantiationCallback instantiationCallback) {
    nodeTree.traverseWithParent(
        (node, parentNode) -> {
          if (parentNode instanceof UaObjectNode && node instanceof UaMethodNode methodNode) {
            instantiationCallback.onMethodAdded((UaObjectNode) parentNode, methodNode);
          } else if (node instanceof UaObjectNode objectNode) {
            ObjectTypeNode objectTypeNode = objectNode.getTypeDefinitionNode();

            instantiationCallback.onObjectAdded(parentNode, objectNode, objectTypeNode.getNodeId());
          } else if (node instanceof UaVariableNode variableNode) {
            VariableTypeNode variableTypeNode = variableNode.getTypeDefinitionNode();

            instantiationCallback.onVariableAdded(
                parentNode, variableNode, variableTypeNode.getNodeId());
          }
        });
  }

  /**
   * Return an appropriate {@link NodeId} for the instance being created.
   *
   * @param rootNodeId the root {@link NodeId}.
   * @param browsePath the relative {@link BrowsePath} to the instance being created.
   * @return a {@link NodeId} for the instance being created.
   */
  protected NodeId instanceNodeId(NodeId rootNodeId, BrowsePath browsePath) {
    Object rootIdentifier = rootNodeId.getIdentifier();

    String instanceIdentifier = String.format("%s%s", rootIdentifier, browsePath.join());

    return new NodeId(rootNodeId.getNamespaceIndex(), instanceIdentifier);
  }

  protected UaObjectNode instanceFromTypeDefinition(
      NodeId nodeId, ObjectTypeNode typeDefinitionNode) {

    NodeId typeDefinitionId = typeDefinitionNode.getNodeId();

    // Use a specialized instance if one is registered, otherwise fallback to UaObjectNode.
    ObjectTypeManager.ObjectNodeConstructor ctor =
        objectTypeManager.getNodeConstructor(typeDefinitionId).orElse(UaObjectNode::new);

    return ctor.apply(
        context,
        nodeId,
        typeDefinitionNode.getBrowseName(),
        typeDefinitionNode.getDisplayName(),
        typeDefinitionNode.getDescription(),
        typeDefinitionNode.getWriteMask(),
        typeDefinitionNode.getUserWriteMask(),
        typeDefinitionNode.getRolePermissions(),
        typeDefinitionNode.getUserRolePermissions(),
        typeDefinitionNode.getAccessRestrictions());
  }

  protected UaVariableNode instanceFromTypeDefinition(
      NodeId nodeId, VariableTypeNode typeDefinitionNode) {

    NodeId typeDefinitionId = typeDefinitionNode.getNodeId();

    // Use a specialized instance if one is registered, otherwise fallback to UaVariableNode.
    VariableTypeManager.VariableNodeConstructor ctor =
        variableTypeManager.getNodeConstructor(typeDefinitionId).orElse(UaVariableNode::new);

    return ctor.apply(
        context,
        nodeId,
        typeDefinitionNode.getBrowseName(),
        typeDefinitionNode.getDisplayName(),
        typeDefinitionNode.getDescription(),
        typeDefinitionNode.getWriteMask(),
        typeDefinitionNode.getUserWriteMask(),
        typeDefinitionNode.getRolePermissions(),
        typeDefinitionNode.getUserRolePermissions(),
        typeDefinitionNode.getAccessRestrictions(),
        typeDefinitionNode.getValue(),
        typeDefinitionNode.getDataType(),
        typeDefinitionNode.getValueRank(),
        typeDefinitionNode.getArrayDimensions());
  }

  protected boolean isOptionalDeclaration(UaNode node) {
    return node.getReferences().stream()
        .filter(r -> NodeIds.HasModellingRule.equals(r.getReferenceTypeId()))
        .anyMatch(r -> NodeIds.ModellingRule_Optional.equalTo(r.getTargetNodeId()));
  }

  private static ExpandedNodeId getTypeDefinition(
      ReferenceTable referenceTable, BrowsePath browsePath) {
    return referenceTable.getReferences(browsePath).stream()
        .filter(t -> t.nodeId.equals(NodeIds.HasTypeDefinition))
        .map(t -> t.target.targetNodeId)
        .findFirst()
        .orElse(ExpandedNodeId.NULL_VALUE);
  }

  /**
   * @deprecated the replacement API configures optional-member selection declaratively on {@code
   *     InstantiationRequest} (include/exclude paths, {@code includeAllOptionals}, or a selection
   *     predicate) and replaces the added-node callbacks with {@code onNode} and {@code bindMethod}
   *     hooks that run against the staged graph before publication. See {@code
   *     docs/features/node-instantiation-migration.md}.
   */
  @Deprecated
  public interface InstantiationCallback {

    /**
     * Called determine whether the optional member named {@code browseName} should be added to an
     * instantiated Node of the type identified by {@code typeDefinitionId}
     *
     * @param typeDefinitionId the type definition id of the {@link UaNode} being instantiated.
     * @param browseName the {@link QualifiedName} of the optional member.
     * @return {@code true} if the optional member named {@code browseName} should be added.
     */
    default boolean includeOptionalNode(NodeId typeDefinitionId, QualifiedName browseName) {
      return false;
    }

    /**
     * Called when a {@link UaMethodNode} has been added to a {@link UaObjectNode} somewhere in the
     * instance hierarchy.
     *
     * @param parent the {@link UaObjectNode} the method was added to.
     * @param instance the {@link UaMethodNode} instance.
     */
    default void onMethodAdded(@Nullable UaObjectNode parent, UaMethodNode instance) {}

    /**
     * Called when a {@link UaObjectNode} has been added to a parent {@link UaNode} by a
     * hierarchical reference somewhere in the instance hierarchy.
     *
     * <p>If {@code parent} is {@code null} then {@code instance} is the root of the instance
     * hierarchy.
     *
     * @param parent the parent {@link UaNode}.
     * @param instance the {@link UaObjectNode} instance.
     * @param typeDefinitionId the {@link NodeId} of the ObjectTypeDefinition.
     */
    default void onObjectAdded(
        @Nullable UaNode parent, UaObjectNode instance, NodeId typeDefinitionId) {}

    /**
     * Called when a {@link UaVariableNode} has been added to a parent {@link UaNode} by a
     * hierarchical reference somewhere in the instance hierarchy.
     *
     * <p>If {@code parent} is {@code null} then {@code instance} is the root of the instance
     * hierarchy.
     *
     * @param parent the parent {@link UaNode}.
     * @param instance the {@link UaVariableNode} instance.
     * @param typeDefinitionId the {@link NodeId} of the VariableTypeDefinition.
     */
    default void onVariableAdded(
        @Nullable UaNode parent, UaVariableNode instance, NodeId typeDefinitionId) {}
  }
}
