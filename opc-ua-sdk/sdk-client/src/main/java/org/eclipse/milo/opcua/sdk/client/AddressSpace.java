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

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedFuture;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedUaFuture;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.ObjectTypeManager.ObjectNodeConstructor;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaViewNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UNumber;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressSpace {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private NodeCache nodeCache = new NodeCache();

  private BrowseOptions browseOptions = new BrowseOptions();

  private final OpcUaClient client;

  public AddressSpace(OpcUaClient client) {
    this.client = client;
  }

  /**
   * Get a {@link UaNode} instance for the Node identified by {@code nodeId}.
   *
   * @param nodeId the {@link NodeId} identifying the Node to get.
   * @return a {@link UaNode} instance for the Node identified by {@code nodeId}.
   * @throws UaException if an error occurs while creating the Node.
   */
  public UaNode getNode(NodeId nodeId) throws UaException {
    try {
      return getNodeAsync(nodeId).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Get a {@link UaNode} instance for the Node identified by {@code nodeId}.
   *
   * <p>This call completes asynchronously.
   *
   * @param nodeId the {@link NodeId} identifying the Node to get.
   * @return a CompletableFuture that completes successfully with the UaNode instance or completes
   *     exceptionally if a service-level error occurs.
   */
  public CompletableFuture<? extends UaNode> getNodeAsync(NodeId nodeId) {
    UaNode cachedNode = nodeCache.getIfPresent(nodeId);

    if (cachedNode != null) {
      return completedFuture(cachedNode);
    } else {
      return createNode(nodeId)
          .whenComplete(
              (node, ex) -> {
                if (node != null) {
                  nodeCache.put(nodeId, node);
                }
              });
    }
  }

  /**
   * Get a {@link UaObjectNode} instance for the ObjectNode identified by {@code nodeId}.
   *
   * <p>The type definition will be read when the instance is created. If this type definition is
   * registered with the {@link ObjectTypeManager} a {@link UaObjectNode} of the appropriate
   * subclass will be returned.
   *
   * @param nodeId the {@link NodeId} identifying the ObjectNode to get.
   * @return a {@link UaObjectNode} instance for the ObjectNode identified by {@code nodeId}.
   * @throws UaException if an error occurs while creating the ObjectNode.
   */
  public UaObjectNode getObjectNode(NodeId nodeId) throws UaException {
    try {
      return getObjectNodeAsync(nodeId).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Get a {@link UaObjectNode} instance for the ObjectNode identified by {@code nodeId}, assuming
   * the type definition identified by {@code typeDefinitionId}.
   *
   * <p>If this type definition is registered with the {@link ObjectTypeManager} a {@link
   * UaObjectNode} of the appropriate subclass will be returned.
   *
   * @param nodeId the {@link NodeId} identifying the ObjectNode to get.
   * @param typeDefinitionId the {@link NodeId} identifying the type definition.
   * @return a {@link UaObjectNode} instance for the ObjectNode identified by {@code nodeId}.
   * @throws UaException if an error occurs while creating the ObjectNode.
   */
  public UaObjectNode getObjectNode(NodeId nodeId, NodeId typeDefinitionId) throws UaException {
    try {
      return getObjectNodeAsync(nodeId, typeDefinitionId).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Get a {@link UaObjectNode} instance for the ObjectNode identified by {@code nodeId}.
   *
   * <p>The type definition will be read when the instance is created. If this type definition is
   * registered with the {@link ObjectTypeManager} a {@link UaObjectNode} of the appropriate
   * subclass will be returned.
   *
   * <p>This call completes asynchronously.
   *
   * @param nodeId the {@link NodeId} identifying the ObjectNode to get.
   * @return a CompletableFuture that completes successfully with a {@link UaObjectNode} instance
   *     for the ObjectNode identified by {@code nodeId} or completes exceptionally if an error
   *     occurs creating the ObjectNode.
   */
  public CompletableFuture<UaObjectNode> getObjectNodeAsync(NodeId nodeId) {
    UaNode cachedNode = nodeCache.getIfPresent(nodeId);

    if (cachedNode instanceof UaObjectNode) {
      return completedFuture((UaObjectNode) cachedNode);
    } else {
      CompletableFuture<NodeId> typeDefinitionFuture = readTypeDefinition(nodeId);

      return typeDefinitionFuture.thenCompose(
          typeDefinitionId -> getObjectNodeAsync(nodeId, typeDefinitionId));
    }
  }

  /**
   * Get a {@link UaObjectNode} instance for the ObjectNode identified by {@code nodeId}, assuming
   * the type definition identified by {@code typeDefinitionId}.
   *
   * <p>If this type definition is registered with the {@link ObjectTypeManager} a {@link
   * UaObjectNode} of the appropriate subclass will be returned.
   *
   * <p>This call completes asynchronously.
   *
   * @param nodeId the {@link NodeId} identifying the ObjectNode to get.
   * @param typeDefinitionId the {@link NodeId} identifying the type definition.
   * @return a CompletableFuture that completes successfully with a {@link UaObjectNode} instance
   *     for the ObjectNode identified by {@code nodeId} or completes exceptionally if an error
   *     occurs creating the ObjectNode.
   */
  public CompletableFuture<UaObjectNode> getObjectNodeAsync(
      NodeId nodeId, NodeId typeDefinitionId) {
    UaNode cachedNode = nodeCache.getIfPresent(nodeId);

    if (cachedNode instanceof UaObjectNode) {
      return completedFuture((UaObjectNode) cachedNode);
    } else {
      CompletableFuture<ReadResponse> future =
          readAttributes(nodeId, AttributeId.OBJECT_ATTRIBUTES);

      return future.thenCompose(
          response -> {
            DataValue[] attributeValues = requireNonNull(response.getResults());

            try {
              UaObjectNode node = newObjectNode(nodeId, typeDefinitionId, attributeValues);

              nodeCache.put(node.getNodeId(), node);

              return completedFuture(node);
            } catch (UaException e) {
              return failedFuture(e);
            }
          });
    }
  }

  /**
   * Get a {@link UaVariableNode} instance for the VariableNode identified by {@code nodeId}.
   *
   * <p>The type definition will be read when the instance is created. If this type definition is
   * registered with the {@link VariableTypeManager} a {@link UaVariableNode} of the appropriate
   * subclass will be returned.
   *
   * @param nodeId the {@link NodeId} identifying the VariableNode to get.
   * @return a {@link UaVariableNode} instance for the VariableNode identified by {@code nodeId}.
   * @throws UaException if an error occurs while creating the VariableNode.
   */
  public UaVariableNode getVariableNode(NodeId nodeId) throws UaException {
    try {
      return getVariableNodeAsync(nodeId).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Get a {@link UaVariableNode} instance for the VariableNode identified by {@code nodeId},
   * assuming the type definition identified by {@code typeDefinitionId}.
   *
   * <p>If this type definition is registered with the {@link VariableTypeManager} a {@link
   * UaVariableNode} of the appropriate subclass will be returned.
   *
   * @param nodeId the {@link NodeId} identifying the VariableNode to get.
   * @param typeDefinitionId the {@link NodeId} identifying the type definition.
   * @return a {@link UaVariableNode} instance for the VariableNode identified by {@code nodeId}.
   * @throws UaException if an error occurs while creating the VariableNode.
   */
  public UaVariableNode getVariableNode(NodeId nodeId, NodeId typeDefinitionId) throws UaException {
    try {
      return getVariableNodeAsync(nodeId, typeDefinitionId).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Get a {@link UaVariableNode} instance for the VariableNode identified by {@code nodeId}.
   *
   * <p>The type definition will be read when the instance is created. If this type definition is
   * registered with the {@link VariableTypeManager} a {@link UaVariableNode} of the appropriate
   * subclass will be returned.
   *
   * <p>This call completes asynchronously.
   *
   * @param nodeId the {@link NodeId} identifying the VariableNode to get.
   * @return a CompletableFuture that completes successfully with a {@link UaVariableNode} instance
   *     for the VariableNode identified by {@code nodeId} or completes exceptionally if an error
   *     occurs while creating the VariableNode.
   */
  public CompletableFuture<UaVariableNode> getVariableNodeAsync(NodeId nodeId) {
    UaNode cachedNode = nodeCache.getIfPresent(nodeId);

    if (cachedNode instanceof UaVariableNode) {
      return completedFuture((UaVariableNode) cachedNode);
    } else {
      CompletableFuture<NodeId> typeDefinitionFuture = readTypeDefinition(nodeId);

      return typeDefinitionFuture.thenCompose(
          typeDefinitionId -> getVariableNodeAsync(nodeId, typeDefinitionId));
    }
  }

  /**
   * Get a {@link UaVariableNode} instance for the VariableNode identified by {@code nodeId},
   * assuming the type definition identified by {@code typeDefinitionId}.
   *
   * <p>If this type definition is registered with the {@link VariableTypeManager} a {@link
   * UaVariableNode} of the appropriate subclass will be returned.
   *
   * <p>This call completes asynchronously.
   *
   * @param nodeId the {@link NodeId} identifying the VariableNode to get.
   * @param typeDefinitionId the {@link NodeId} identifying the type definition.
   * @return a CompletableFuture that completes successfully with a {@link UaVariableNode} instance
   *     for the VariableNode identified by {@code nodeId} or completes exceptionally if an error
   *     occurs while creating the VariableNode.
   */
  public CompletableFuture<UaVariableNode> getVariableNodeAsync(
      NodeId nodeId, NodeId typeDefinitionId) {
    UaNode cachedNode = nodeCache.getIfPresent(nodeId);

    if (cachedNode instanceof UaVariableNode) {
      return completedFuture((UaVariableNode) cachedNode);
    } else {
      CompletableFuture<ReadResponse> future =
          readAttributes(nodeId, AttributeId.VARIABLE_ATTRIBUTES);

      return future.thenCompose(
          response -> {
            DataValue[] variableAttributeValues = requireNonNull(response.getResults());

            try {
              UaVariableNode node =
                  newVariableNode(nodeId, typeDefinitionId, variableAttributeValues);

              nodeCache.put(node.getNodeId(), node);

              return completedFuture(node);
            } catch (UaException e) {
              return failedFuture(e);
            }
          });
    }
  }

  /**
   * Get the {@link UaObjectNode} instance for the Objects Folder Node.
   *
   * @return the {@link UaObjectNode} instance for the Objects Folder Node.
   * @throws UaException if an error occurs while creating the Objects Folder Node.
   */
  public UaObjectNode getObjectsFolderNode() throws UaException {
    return getObjectNode(NodeIds.ObjectsFolder);
  }

  /**
   * Get the {@link UaObjectNode} instance for the Root Folder Node.
   *
   * @return the {@link UaObjectNode} instance for the Root Folder Node.
   * @throws UaException if an error occurs while creating the Root Folder Node.
   */
  public UaObjectNode getRootFolderNode() throws UaException {
    return getObjectNode(NodeIds.RootFolder);
  }

  /**
   * Get the {@link UaObjectNode} instance for the Types Folder Node.
   *
   * @return the {@link UaObjectNode} instance for the Types Folder Node.
   * @throws UaException if an error occurs while creating the Types Folder Node.
   */
  public UaObjectNode getTypesFolderNode() throws UaException {
    return getObjectNode(NodeIds.TypesFolder);
  }

  /**
   * Get the {@link ServerTypeNode} instance for the Server Node.
   *
   * @return the {@link ServerTypeNode} instance for the Server Node.
   * @throws UaException if an error occurs while creating the Server Node.
   */
  public ServerTypeNode getServerNode() throws UaException {
    return (ServerTypeNode) getObjectNode(NodeIds.Server, NodeIds.ServerType);
  }

  /**
   * Call the Browse service to get a {@link UaNode}'s references using the currently configured
   * {@link BrowseOptions}.
   *
   * @param node the {@link UaNode} to browse.
   * @return a List of {@link ReferenceDescription}s.
   * @throws UaException if a service-level error occurs.
   * @see #getBrowseOptions()
   * @see #modifyBrowseOptions(Consumer)
   * @see #setBrowseOptions(BrowseOptions)
   */
  public List<ReferenceDescription> browse(UaNode node) throws UaException {
    return browse(node, getBrowseOptions());
  }

  /**
   * Call the Browse service to get a {@link UaNode}'s references.
   *
   * @param node the {@link UaNode} to browse.
   * @param browseOptions the {@link BrowseOptions} to browse with.
   * @return a List of {@link ReferenceDescription}s.
   * @throws UaException if a service-level error occurs.
   */
  public List<ReferenceDescription> browse(UaNode node, BrowseOptions browseOptions)
      throws UaException {
    try {
      return browseAsync(node, browseOptions).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Call the Browse service to get a Node's references using the currently configured {@link
   * BrowseOptions}.
   *
   * @param nodeId the {@link NodeId} of the Node to browse.
   * @return a List of {@link ReferenceDescription}s.
   * @throws UaException if a service-level error occurs.
   * @see #getBrowseOptions()
   * @see #modifyBrowseOptions(Consumer)
   * @see #setBrowseOptions(BrowseOptions)
   */
  public List<ReferenceDescription> browse(NodeId nodeId) throws UaException {
    return browse(nodeId, getBrowseOptions());
  }

  /**
   * Call the Browse service to get a Node's references.
   *
   * @param nodeId the {@link NodeId} of the Node to browse.
   * @param browseOptions the {@link BrowseOptions} to browse with.
   * @return a List of {@link ReferenceDescription}s.
   * @throws UaException if a service-level error occurs.
   */
  public List<ReferenceDescription> browse(NodeId nodeId, BrowseOptions browseOptions)
      throws UaException {
    try {
      return browseAsync(nodeId, browseOptions).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Call the Browse service to get a {@link UaNode}'s references using the currently configured
   * {@link BrowseOptions}.
   *
   * <p>This call completes asynchronously.
   *
   * @param node the {@link UaNode} to browse.
   * @return a CompletableFuture that completes successfully with the List of references or
   *     completes exceptionally if a service-level error occurs.
   * @see #getBrowseOptions()
   * @see #modifyBrowseOptions(Consumer)
   * @see #setBrowseOptions(BrowseOptions)
   */
  public CompletableFuture<List<ReferenceDescription>> browseAsync(UaNode node) {
    return browseAsync(node.getNodeId(), getBrowseOptions());
  }

  /**
   * Call the Browse service to get a {@link UaNode}'s references.
   *
   * <p>This call completes asynchronously.
   *
   * @param node the {@link UaNode} to browse.
   * @param browseOptions the {@link BrowseOptions} to browse with.
   * @return a CompletableFuture that completes successfully with the List of references or
   *     completes exceptionally if a service-level error occurs.
   */
  public CompletableFuture<List<ReferenceDescription>> browseAsync(
      UaNode node, BrowseOptions browseOptions) {
    return browseAsync(node.getNodeId(), browseOptions);
  }

  /**
   * Call the Browse service to get a Node's references using the currently configured {@link
   * BrowseOptions}.
   *
   * <p>This call completes asynchronously.
   *
   * @param nodeId the {@link NodeId} of the Node to browse.
   * @return a CompletableFuture that completes successfully with the List of references or
   *     completes exceptionally if a service-level error occurs.
   * @see #getBrowseOptions()
   * @see #modifyBrowseOptions(Consumer)
   * @see #setBrowseOptions(BrowseOptions)
   */
  public CompletableFuture<List<ReferenceDescription>> browseAsync(NodeId nodeId) {
    return browseAsync(nodeId, getBrowseOptions());
  }

  /**
   * Call the Browse service to get a Node's references.
   *
   * <p>This call completes asynchronously.
   *
   * @param nodeId the {@link NodeId} of the Node to browse.
   * @param browseOptions the {@link BrowseOptions} to browse with.
   * @return a CompletableFuture that completes successfully with the List of references or
   *     completes exceptionally if a service-level error occurs.
   */
  public CompletableFuture<List<ReferenceDescription>> browseAsync(
      NodeId nodeId, BrowseOptions browseOptions) {
    BrowseDescription browseDescription =
        new BrowseDescription(
            nodeId,
            browseOptions.getBrowseDirection(),
            browseOptions.getReferenceTypeId(),
            browseOptions.isIncludeSubtypes(),
            browseOptions.getNodeClassMask(),
            browseOptions.getResultMask());

    return BrowseHelper.browse(client, browseDescription, browseOptions.getMaxReferencesPerNode());
  }

  /**
   * Browse from {@code node} using the currently configured {@link BrowseOptions}.
   *
   * @param node the {@link UaNode} to start the browse from.
   * @return a List of {@link UaNode}s referenced by {@code node} given the currently configured
   *     {@link BrowseOptions}.
   * @throws UaException if an error occurs while browsing or creating Nodes.
   * @see #browseNodes(UaNode, BrowseOptions)
   * @see #getBrowseOptions()
   * @see #modifyBrowseOptions(Consumer)
   * @see #setBrowseOptions(BrowseOptions)
   */
  public List<? extends UaNode> browseNodes(UaNode node) throws UaException {
    return browseNodes(node, getBrowseOptions());
  }

  /**
   * Browse from {@code node} using {@code browseOptions}.
   *
   * @param node the {@link UaNode} to start the browse from.
   * @param browseOptions the {@link BrowseOptions} to use.
   * @return a List of {@link UaNode}s referenced by {@code node} given {@code browseOptions}.
   * @throws UaException if an error occurs while browsing or creating Nodes.
   */
  public List<? extends UaNode> browseNodes(UaNode node, BrowseOptions browseOptions)
      throws UaException {
    try {
      return browseNodesAsync(node, browseOptions).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Browse from {@code nodeId} using the currently configured {@link BrowseOptions}.
   *
   * @param nodeId the {@link NodeId} to start the browse from.
   * @return a List of {@link UaNode}s referenced by {@code nodeId} given the currently configured
   *     {@link BrowseOptions}.
   * @throws UaException if an error occurs while browsing or creating Nodes.
   * @see #browseNodes(UaNode, BrowseOptions)
   * @see #getBrowseOptions()
   * @see #modifyBrowseOptions(Consumer)
   * @see #setBrowseOptions(BrowseOptions)
   */
  public List<? extends UaNode> browseNodes(NodeId nodeId) throws UaException {
    return browseNodes(nodeId, getBrowseOptions());
  }

  /**
   * Browse from {@code nodeId} using {@code browseOptions}.
   *
   * @param nodeId the {@link NodeId} to start the browse from.
   * @param browseOptions the {@link BrowseOptions} to use.
   * @return a List of {@link UaNode}s referenced by {@code nodeId} given {@code browseOptions}.
   * @throws UaException if an error occurs while browsing or creating Nodes.
   */
  public List<? extends UaNode> browseNodes(NodeId nodeId, BrowseOptions browseOptions)
      throws UaException {
    try {
      return browseNodesAsync(nodeId, browseOptions).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Browse from {@code node} using the currently configured {@link BrowseOptions}.
   *
   * <p>This call completes asynchronously.
   *
   * @param node the {@link UaNode} to start the browse from.
   * @return a CompletableFuture that completes successfully with a List of {@link UaNode}s
   *     referenced by {@code node} given the currently configured {@link BrowseOptions} or
   *     completes exceptionally if a service-level error occurs.
   * @see #browseNodesAsync(UaNode, BrowseOptions)
   * @see #getBrowseOptions()
   * @see #modifyBrowseOptions(Consumer)
   * @see #setBrowseOptions(BrowseOptions)
   */
  public CompletableFuture<List<? extends UaNode>> browseNodesAsync(UaNode node) {
    return browseNodesAsync(node.getNodeId());
  }

  /**
   * Browse from {@code node} using {@code browseOptions}.
   *
   * <p>This call completes asynchronously.
   *
   * @param node the {@link UaNode} to start the browse from.
   * @param browseOptions the {@link BrowseOptions} to use.
   * @return a CompletableFuture that completes successfully with a List of {@link UaNode}s
   *     referenced by {@code node} given the currently configured {@link BrowseOptions} or
   *     completes exceptionally if a service-level error occurs.
   */
  public CompletableFuture<List<? extends UaNode>> browseNodesAsync(
      UaNode node, BrowseOptions browseOptions) {
    return browseNodesAsync(node.getNodeId(), browseOptions);
  }

  /**
   * Browse from {@code nodeId} using the currently configured {@link BrowseOptions}.
   *
   * <p>This call completes asynchronously.
   *
   * @param nodeId the {@link NodeId} to start the browse from.
   * @return a CompletableFuture that completes successfully with a List of {@link UaNode}s
   *     referenced by {@code node} given the currently configured {@link BrowseOptions} or
   *     completes exceptionally if a service-level error occurs.
   * @see #browseNodesAsync(UaNode, BrowseOptions)
   * @see #getBrowseOptions()
   * @see #modifyBrowseOptions(Consumer)
   * @see #setBrowseOptions(BrowseOptions)
   */
  public CompletableFuture<List<? extends UaNode>> browseNodesAsync(NodeId nodeId) {
    return browseNodesAsync(nodeId, getBrowseOptions());
  }

  /**
   * Browse from {@code nodeId} using {@code browseOptions}.
   *
   * <p>This call completes asynchronously.
   *
   * @param nodeId the {@link NodeId} to start the browse from.
   * @param browseOptions the {@link BrowseOptions} to use.
   * @return a CompletableFuture that completes successfully with a List of {@link UaNode}s
   *     referenced by {@code node} given the currently configured {@link BrowseOptions} or
   *     completes exceptionally if a service-level error occurs.
   */
  public CompletableFuture<List<? extends UaNode>> browseNodesAsync(
      NodeId nodeId, BrowseOptions browseOptions) {
    BrowseDescription browseDescription =
        new BrowseDescription(
            nodeId,
            browseOptions.getBrowseDirection(),
            browseOptions.getReferenceTypeId(),
            browseOptions.isIncludeSubtypes(),
            browseOptions.getNodeClassMask(),
            browseOptions.getResultMask());

    CompletableFuture<List<ReferenceDescription>> browse =
        BrowseHelper.browse(client, browseDescription, browseOptions.getMaxReferencesPerNode());

    return browse.thenCompose(
        references -> {
          List<CompletableFuture<? extends UaNode>> cfs =
              references.stream()
                  .map(
                      reference -> {
                        NodeClass nodeClass = reference.getNodeClass();
                        ExpandedNodeId xNodeId = reference.getNodeId();
                        ExpandedNodeId xTypeDefinitionId = reference.getTypeDefinition();

                        switch (nodeClass) {
                          case Object:
                          case Variable:
                            {
                              CompletableFuture<CompletableFuture<? extends UaNode>> ff =
                                  toNodeIdAsync(xNodeId)
                                      .thenCombine(
                                          toNodeIdAsync(xTypeDefinitionId),
                                          (targetNodeId, typeDefinitionId) -> {
                                            if (nodeClass == NodeClass.Object) {
                                              return getObjectNodeAsync(
                                                  targetNodeId, typeDefinitionId);
                                            } else {
                                              return getVariableNodeAsync(
                                                  targetNodeId, typeDefinitionId);
                                            }
                                          });

                              return unwrap(ff)
                                  .exceptionally(
                                      ex -> {
                                        logger.warn(
                                            "Failed to create Node from Reference to {}",
                                            reference.getNodeId(),
                                            ex);
                                        return null;
                                      });
                            }
                          default:
                            {
                              // TODO specialized getNode for other NodeClasses?
                              return toNodeIdAsync(xNodeId)
                                  .thenCompose(this::getNodeAsync)
                                  .exceptionally(
                                      ex -> {
                                        logger.warn(
                                            "Failed to create Node from Reference to {}",
                                            reference.getNodeId(),
                                            ex);
                                        return null;
                                      });
                            }
                        }
                      })
                  .collect(Collectors.toList());

          return sequence(cfs);
        });
  }

  private static CompletableFuture<List<? extends UaNode>> sequence(
      List<CompletableFuture<? extends UaNode>> cfs) {

    if (cfs.isEmpty()) {
      return completedFuture(Collections.emptyList());
    }

    @SuppressWarnings("rawtypes")
    CompletableFuture[] fa = cfs.toArray(new CompletableFuture[0]);

    return CompletableFuture.allOf(fa)
        .thenApply(
            v -> {
              List<UaNode> results = new ArrayList<>(cfs.size());

              for (CompletableFuture<? extends UaNode> cf : cfs) {
                UaNode node = cf.join();
                if (node != null) {
                  results.add(node);
                }
              }

              return results;
            });
  }

  private static CompletableFuture<? extends UaNode> unwrap(
      CompletableFuture<CompletableFuture<? extends UaNode>> future) {

    return future.thenCompose(node -> node);
  }

  /**
   * Convert {@code xni} to a {@link NodeId} in the server, reading the namespace table from the
   * server if necessary.
   *
   * <p>Returns {@link NodeId#NULL_VALUE} if the conversion could not be completed for any reason.
   *
   * @param xni the {@link ExpandedNodeId} to convert to a {@link NodeId}.
   * @return a {@link NodeId} local to the server, or {@link NodeId#NULL_VALUE} if conversion could
   *     not be completed for any reason.
   */
  public NodeId toNodeId(ExpandedNodeId xni) {
    try {
      return toNodeIdAsync(xni).get();
    } catch (ExecutionException | InterruptedException e) {
      return NodeId.NULL_VALUE;
    }
  }

  /**
   * Convert {@code xni} to a {@link NodeId} in the server, reading the namespace table from the
   * server if necessary.
   *
   * <p>Returns {@link NodeId#NULL_VALUE} if the conversion could not be completed for any reason.
   *
   * @param xni the {@link ExpandedNodeId} to convert to a {@link NodeId}.
   * @return a {@link NodeId} local to the server, or {@link NodeId#NULL_VALUE} if conversion could
   *     not be completed for any reason.
   */
  public CompletableFuture<NodeId> toNodeIdAsync(ExpandedNodeId xni) {
    // TODO should this fail with Bad_NodeIdUnknown instead of returning NodeId.NULL_VALUE?
    if (xni.isLocal()) {
      Optional<NodeId> local = xni.toNodeId(client.getNamespaceTable());

      return local
          .map(CompletableFuture::completedFuture)
          .orElse(
              client
                  .readNamespaceTableAsync()
                  .thenCompose(
                      namespaceTable ->
                          completedFuture(xni.toNodeId(namespaceTable).orElse(NodeId.NULL_VALUE)))
                  .exceptionally(e -> NodeId.NULL_VALUE));
    } else {
      return completedFuture(NodeId.NULL_VALUE);
    }
  }

  /**
   * Get the default {@link BrowseOptions} used during browse calls that don't have an explicit
   * {@link BrowseOptions} parameter.
   *
   * @return the default {@link BrowseOptions}.
   */
  public synchronized BrowseOptions getBrowseOptions() {
    return browseOptions;
  }

  /**
   * Modify the default {@link BrowseOptions} used during browse calls that don't have an explicit
   * {@link BrowseOptions} parameter.
   *
   * @param builderConsumer a {@link Consumer} that receives a {@link BrowseOptions.Builder}.
   */
  public synchronized void modifyBrowseOptions(Consumer<BrowseOptions.Builder> builderConsumer) {
    BrowseOptions.Builder builder = new BrowseOptions.Builder(browseOptions);

    builderConsumer.accept(builder);

    setBrowseOptions(builder.build());
  }

  /**
   * Set a new default {@link BrowseOptions} used during browse calls that don't have an explicit
   * {@link BrowseOptions} parameter.
   *
   * @param browseOptions the new default {@link BrowseOptions}.
   */
  public synchronized void setBrowseOptions(BrowseOptions browseOptions) {
    this.browseOptions = browseOptions;
  }

  /**
   * Get the current {@link NodeCache}.
   *
   * @return the current {@link NodeCache}.
   */
  public synchronized NodeCache getNodeCache() {
    return nodeCache;
  }

  /**
   * Set a new {@link NodeCache}.
   *
   * @param nodeCache a new {@link NodeCache}.
   */
  public synchronized void setNodeCache(NodeCache nodeCache) {
    this.nodeCache = nodeCache;
  }

  private CompletableFuture<NodeId> readTypeDefinition(NodeId nodeId) {
    // A NodeClassMask of 0 is used to work around bug in FreeOpcUa-based
    // servers that cause no references to be returned when requesting only
    // ObjectType and VariableType references.
    CompletableFuture<BrowseResult> browseFuture =
        client.browseAsync(
            new BrowseDescription(
                nodeId,
                BrowseDirection.Forward,
                NodeIds.HasTypeDefinition,
                false,
                uint(0),
                uint(BrowseResultMask.All.getValue())));

    return browseFuture.thenCompose(
        result -> {
          if (result.getStatusCode().isGood()) {
            ReferenceDescription[] references =
                requireNonNullElse(result.getReferences(), new ReferenceDescription[0]);

            Optional<ExpandedNodeId> typeDefinitionId =
                Stream.of(references)
                    .filter(r -> Objects.equals(NodeIds.HasTypeDefinition, r.getReferenceTypeId()))
                    .map(ReferenceDescription::getNodeId)
                    .findFirst();

            return typeDefinitionId
                .map(this::toNodeIdAsync)
                .orElse(completedFuture(NodeId.NULL_VALUE));
          } else {
            return completedFuture(NodeId.NULL_VALUE);
          }
        });
  }

  /**
   * Create a {@link UaNode} instance without prior knowledge of the {@link NodeClass} or type
   * definition, if applicable.
   *
   * @param nodeId the {@link NodeId} of the Node to create.
   * @return a {@link UaNode} instance for the Node identified by {@code nodeId}.
   */
  private CompletableFuture<? extends UaNode> createNode(NodeId nodeId) {
    CompletableFuture<ReadResponse> future = readAttributes(nodeId, AttributeId.BASE_ATTRIBUTES);

    return future.thenCompose(
        response -> {
          DataValue[] results = requireNonNull(response.getResults());

          return createNodeFromBaseAttributes(nodeId, results);
        });
  }

  private CompletableFuture<? extends UaNode> createNodeFromBaseAttributes(
      NodeId nodeId, DataValue[] baseAttributeValues) {

    StatusCode nodeIdStatusCode = baseAttributeValues[0].statusCode();
    if (nodeIdStatusCode.isBad()) {
      return failedUaFuture(nodeIdStatusCode);
    }

    Integer nodeClassValue = (Integer) baseAttributeValues[1].value().value();
    if (nodeClassValue == null) {
      return failedUaFuture(StatusCodes.Bad_NodeClassInvalid);
    }
    NodeClass nodeClass = NodeClass.from(nodeClassValue);
    if (nodeClass == null) {
      return failedUaFuture(StatusCodes.Bad_NodeClassInvalid);
    }

    switch (nodeClass) {
      case DataType:
        return createDataTypeNodeFromBaseAttributes(nodeId, baseAttributeValues);
      case Method:
        return createMethodNodeFromBaseAttributes(nodeId, baseAttributeValues);
      case Object:
        return createObjectNodeFromBaseAttributes(nodeId, baseAttributeValues);
      case ObjectType:
        return createObjectTypeNodeFromBaseAttributes(nodeId, baseAttributeValues);
      case ReferenceType:
        return createReferenceTypeNodeFromBaseAttributes(nodeId, baseAttributeValues);
      case Variable:
        return createVariableNodeFromBaseAttributes(nodeId, baseAttributeValues);
      case VariableType:
        return createVariableTypeNodeFromBaseAttributes(nodeId, baseAttributeValues);
      case View:
        return createViewNodeFromBaseAttributes(nodeId, baseAttributeValues);
      default:
        throw new IllegalArgumentException("NodeClass: " + nodeClass);
    }
  }

  private CompletableFuture<UaDataTypeNode> createDataTypeNodeFromBaseAttributes(
      NodeId nodeId, DataValue[] baseAttributeValues) {

    Set<AttributeId> remainingAttributes =
        Sets.difference(AttributeId.DATA_TYPE_ATTRIBUTES, AttributeId.BASE_ATTRIBUTES);

    CompletableFuture<ReadResponse> attributesFuture = readAttributes(nodeId, remainingAttributes);

    return attributesFuture.thenCompose(
        response -> {
          DataValue[] dataTypeAttributeValues = requireNonNull(response.getResults());
          DataValue[] attributeValues = concat(baseAttributeValues, dataTypeAttributeValues);

          try {
            UaDataTypeNode node = newDataTypeNode(nodeId, attributeValues);

            nodeCache.put(node.getNodeId(), node);

            return completedFuture(node);
          } catch (UaException e) {
            return failedFuture(e);
          }
        });
  }

  private CompletableFuture<UaMethodNode> createMethodNodeFromBaseAttributes(
      NodeId nodeId, DataValue[] baseAttributeValues) {

    Set<AttributeId> remainingAttributes =
        Sets.difference(AttributeId.METHOD_ATTRIBUTES, AttributeId.BASE_ATTRIBUTES);

    CompletableFuture<ReadResponse> attributesFuture = readAttributes(nodeId, remainingAttributes);

    return attributesFuture.thenCompose(
        response -> {
          DataValue[] methodAttributeValues = requireNonNull(response.getResults());
          DataValue[] attributeValues = concat(baseAttributeValues, methodAttributeValues);

          try {
            UaMethodNode node = newMethodNode(nodeId, attributeValues);

            nodeCache.put(node.getNodeId(), node);

            return completedFuture(node);
          } catch (UaException e) {
            return failedFuture(e);
          }
        });
  }

  private CompletableFuture<UaObjectNode> createObjectNodeFromBaseAttributes(
      NodeId nodeId, DataValue[] baseAttributeValues) {

    Set<AttributeId> remainingAttributes =
        Sets.difference(AttributeId.OBJECT_ATTRIBUTES, AttributeId.BASE_ATTRIBUTES);

    CompletableFuture<ReadResponse> attributesFuture = readAttributes(nodeId, remainingAttributes);
    CompletableFuture<NodeId> typeDefinitionFuture = readTypeDefinition(nodeId);

    return CompletableFuture.allOf(attributesFuture, typeDefinitionFuture)
        .thenCompose(
            ignored -> {
              ReadResponse response = attributesFuture.join();
              NodeId typeDefinitionId = typeDefinitionFuture.join();

              DataValue[] objectAttributeValues = requireNonNull(response.getResults());
              DataValue[] attributeValues = concat(baseAttributeValues, objectAttributeValues);

              try {
                UaObjectNode node = newObjectNode(nodeId, typeDefinitionId, attributeValues);

                nodeCache.put(node.getNodeId(), node);

                return completedFuture(node);
              } catch (UaException e) {
                return failedFuture(e);
              }
            });
  }

  private CompletableFuture<UaObjectTypeNode> createObjectTypeNodeFromBaseAttributes(
      NodeId nodeId, DataValue[] baseAttributeValues) {

    Set<AttributeId> remainingAttributes =
        Sets.difference(AttributeId.OBJECT_TYPE_ATTRIBUTES, AttributeId.BASE_ATTRIBUTES);

    CompletableFuture<ReadResponse> attributesFuture = readAttributes(nodeId, remainingAttributes);

    return attributesFuture.thenCompose(
        response -> {
          DataValue[] objectTypeAttributeValues = requireNonNull(response.getResults());
          DataValue[] attributeValues = concat(baseAttributeValues, objectTypeAttributeValues);

          try {
            UaObjectTypeNode node = newObjectTypeNode(nodeId, attributeValues);

            nodeCache.put(node.getNodeId(), node);

            return completedFuture(node);
          } catch (UaException e) {
            return failedFuture(e);
          }
        });
  }

  private CompletableFuture<UaReferenceTypeNode> createReferenceTypeNodeFromBaseAttributes(
      NodeId nodeId, DataValue[] baseAttributeValues) {

    Set<AttributeId> remainingAttributes =
        Sets.difference(AttributeId.REFERENCE_TYPE_ATTRIBUTES, AttributeId.BASE_ATTRIBUTES);

    CompletableFuture<ReadResponse> attributesFuture = readAttributes(nodeId, remainingAttributes);

    return attributesFuture.thenCompose(
        response -> {
          DataValue[] referenceTypeAttributeValues = requireNonNull(response.getResults());
          DataValue[] attributeValues = concat(baseAttributeValues, referenceTypeAttributeValues);

          try {
            UaReferenceTypeNode node = newReferenceTypeNode(nodeId, attributeValues);

            nodeCache.put(node.getNodeId(), node);

            return completedFuture(node);
          } catch (UaException e) {
            return failedFuture(e);
          }
        });
  }

  private CompletableFuture<UaVariableNode> createVariableNodeFromBaseAttributes(
      NodeId nodeId, DataValue[] baseAttributeValues) {

    Set<AttributeId> remainingAttributes =
        Sets.difference(AttributeId.VARIABLE_ATTRIBUTES, AttributeId.BASE_ATTRIBUTES);

    CompletableFuture<ReadResponse> attributesFuture = readAttributes(nodeId, remainingAttributes);
    CompletableFuture<NodeId> typeDefinitionFuture = readTypeDefinition(nodeId);

    return CompletableFuture.allOf(attributesFuture, typeDefinitionFuture)
        .thenCompose(
            ignored -> {
              ReadResponse response = attributesFuture.join();
              NodeId typeDefinitionId = typeDefinitionFuture.join();

              DataValue[] variableAttributeValues = requireNonNull(response.getResults());
              DataValue[] attributeValues = concat(baseAttributeValues, variableAttributeValues);

              try {
                UaVariableNode node = newVariableNode(nodeId, typeDefinitionId, attributeValues);

                nodeCache.put(node.getNodeId(), node);

                return completedFuture(node);
              } catch (UaException e) {
                return failedFuture(e);
              }
            });
  }

  private CompletableFuture<UaVariableTypeNode> createVariableTypeNodeFromBaseAttributes(
      NodeId nodeId, DataValue[] baseAttributeValues) {

    Set<AttributeId> remainingAttributes =
        Sets.difference(AttributeId.VARIABLE_TYPE_ATTRIBUTES, AttributeId.BASE_ATTRIBUTES);

    CompletableFuture<ReadResponse> attributesFuture = readAttributes(nodeId, remainingAttributes);

    return attributesFuture.thenCompose(
        response -> {
          DataValue[] variableTypeAttributeValues = requireNonNull(response.getResults());
          DataValue[] attributeValues = concat(baseAttributeValues, variableTypeAttributeValues);

          try {
            UaVariableTypeNode node = newVariableTypeNode(nodeId, attributeValues);

            nodeCache.put(node.getNodeId(), node);

            return completedFuture(node);
          } catch (UaException e) {
            return failedFuture(e);
          }
        });
  }

  private CompletableFuture<UaViewNode> createViewNodeFromBaseAttributes(
      NodeId nodeId, DataValue[] baseAttributeValues) {

    Set<AttributeId> remainingAttributes =
        Sets.difference(AttributeId.VIEW_ATTRIBUTES, AttributeId.BASE_ATTRIBUTES);

    CompletableFuture<ReadResponse> attributesFuture = readAttributes(nodeId, remainingAttributes);

    return attributesFuture.thenCompose(
        response -> {
          DataValue[] viewAttributeValues = requireNonNull(response.getResults());
          DataValue[] attributeValues = concat(baseAttributeValues, viewAttributeValues);

          try {
            UaViewNode node = newViewNode(nodeId, attributeValues);

            nodeCache.put(node.getNodeId(), node);

            return completedFuture(node);
          } catch (UaException e) {
            return failedFuture(e);
          }
        });
  }

  private CompletableFuture<ReadResponse> readAttributes(
      NodeId nodeId, Set<AttributeId> attributeIds) {
    List<ReadValueId> readValueIds =
        attributeIds.stream()
            .map(id -> new ReadValueId(nodeId, id.uid(), null, QualifiedName.NULL_VALUE))
            .collect(Collectors.toList());

    return client.readAsync(0.0, TimestampsToReturn.Neither, readValueIds);
  }

  private UaDataTypeNode newDataTypeNode(NodeId nodeId, DataValue[] attributeValues)
      throws UaException {
    DataValue nodeIdDataValue = attributeValues[0];
    StatusCode nodeIdStatusCode = nodeIdDataValue.statusCode();
    if (nodeIdStatusCode.isBad()) {
      throw new UaException(nodeIdStatusCode);
    }

    try {
      NodeClass nodeClass =
          NodeClass.from((Integer) requireNonNullElse(attributeValues[1].value().value(), 0));

      Preconditions.checkArgument(
          nodeClass == NodeClass.DataType,
          "expected NodeClass.DataType, got NodeClass." + nodeClass);

      QualifiedName browseName = (QualifiedName) attributeValues[2].value().value();
      LocalizedText displayName = (LocalizedText) attributeValues[3].value().value();
      LocalizedText description = getAttributeOrNull(attributeValues[4], LocalizedText.class);
      UInteger writeMask = getAttributeOrNull(attributeValues[5], UInteger.class);
      UInteger userWriteMask = getAttributeOrNull(attributeValues[6], UInteger.class);
      RolePermissionType[] rolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[7], RolePermissionType[].class);
      RolePermissionType[] userRolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[8], RolePermissionType[].class);
      AccessRestrictionType accessRestrictions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[9], AccessRestrictionType.class);

      Boolean isAbstract = (Boolean) attributeValues[10].value().value();
      DataTypeDefinition dataTypeDefinition =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[11], DataTypeDefinition.class);

      return new UaDataTypeNode(
          client,
          nodeId,
          nodeClass,
          browseName,
          displayName,
          description,
          writeMask,
          userWriteMask,
          rolePermissions,
          userRolePermissions,
          accessRestrictions,
          isAbstract,
          dataTypeDefinition);
    } catch (Throwable t) {
      throw UaException.extract(t).orElse(new UaException(StatusCodes.Bad_UnexpectedError, t));
    }
  }

  private UaMethodNode newMethodNode(NodeId nodeId, DataValue[] attributeValues)
      throws UaException {
    DataValue nodeIdDataValue = attributeValues[0];
    StatusCode nodeIdStatusCode = nodeIdDataValue.statusCode();
    if (nodeIdStatusCode.isBad()) {
      throw new UaException(nodeIdStatusCode);
    }

    try {
      NodeClass nodeClass =
          NodeClass.from((Integer) requireNonNullElse(attributeValues[1].value().value(), 0));

      Preconditions.checkArgument(
          nodeClass == NodeClass.Method, "expected NodeClass.Method, got NodeClass." + nodeClass);

      QualifiedName browseName = (QualifiedName) attributeValues[2].value().value();
      LocalizedText displayName = (LocalizedText) attributeValues[3].value().value();
      LocalizedText description = getAttributeOrNull(attributeValues[4], LocalizedText.class);
      UInteger writeMask = getAttributeOrNull(attributeValues[5], UInteger.class);
      UInteger userWriteMask = getAttributeOrNull(attributeValues[6], UInteger.class);
      RolePermissionType[] rolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[7], RolePermissionType[].class);
      RolePermissionType[] userRolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[8], RolePermissionType[].class);
      AccessRestrictionType accessRestrictions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[9], AccessRestrictionType.class);

      Boolean executable = (Boolean) attributeValues[10].value().value();
      Boolean userExecutable = (Boolean) attributeValues[11].value().value();

      return new UaMethodNode(
          client,
          nodeId,
          nodeClass,
          browseName,
          displayName,
          description,
          writeMask,
          userWriteMask,
          rolePermissions,
          userRolePermissions,
          accessRestrictions,
          executable,
          userExecutable);
    } catch (Throwable t) {
      throw UaException.extract(t).orElse(new UaException(StatusCodes.Bad_UnexpectedError, t));
    }
  }

  private UaObjectNode newObjectNode(
      NodeId nodeId, NodeId typeDefinitionId, DataValue[] attributeValues) throws UaException {

    DataValue nodeIdDataValue = attributeValues[0];
    StatusCode nodeIdStatusCode = nodeIdDataValue.statusCode();
    if (nodeIdStatusCode.isBad()) {
      throw new UaException(nodeIdStatusCode);
    }

    try {
      NodeClass nodeClass =
          NodeClass.from((Integer) requireNonNullElse(attributeValues[1].value().value(), 0));

      Preconditions.checkArgument(
          nodeClass == NodeClass.Object, "expected NodeClass.Object, got NodeClass." + nodeClass);

      QualifiedName browseName = (QualifiedName) attributeValues[2].value().value();
      LocalizedText displayName = (LocalizedText) attributeValues[3].value().value();
      LocalizedText description = getAttributeOrNull(attributeValues[4], LocalizedText.class);
      UInteger writeMask = getAttributeOrNull(attributeValues[5], UInteger.class);
      UInteger userWriteMask = getAttributeOrNull(attributeValues[6], UInteger.class);
      RolePermissionType[] rolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[7], RolePermissionType[].class);
      RolePermissionType[] userRolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[8], RolePermissionType[].class);
      AccessRestrictionType accessRestrictions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[9], AccessRestrictionType.class);

      UByte eventNotifier = (UByte) attributeValues[10].value().value();

      ObjectNodeConstructor constructor =
          client
              .getObjectTypeManager()
              .getNodeConstructor(typeDefinitionId)
              .orElse(UaObjectNode::new);

      return constructor.apply(
          client,
          nodeId,
          nodeClass,
          browseName,
          displayName,
          description,
          writeMask,
          userWriteMask,
          rolePermissions,
          userRolePermissions,
          accessRestrictions,
          eventNotifier);
    } catch (Throwable t) {
      throw UaException.extract(t).orElse(new UaException(StatusCodes.Bad_UnexpectedError, t));
    }
  }

  private UaObjectTypeNode newObjectTypeNode(NodeId nodeId, DataValue[] attributeValues)
      throws UaException {
    DataValue nodeIdDataValue = attributeValues[0];
    StatusCode nodeIdStatusCode = nodeIdDataValue.statusCode();
    if (nodeIdStatusCode.isBad()) {
      throw new UaException(nodeIdStatusCode);
    }

    try {
      NodeClass nodeClass =
          NodeClass.from((Integer) requireNonNullElse(attributeValues[1].value().value(), 0));

      Preconditions.checkArgument(
          nodeClass == NodeClass.ObjectType,
          "expected NodeClass.ObjectType, got NodeClass." + nodeClass);

      QualifiedName browseName = (QualifiedName) attributeValues[2].value().value();
      LocalizedText displayName = (LocalizedText) attributeValues[3].value().value();
      LocalizedText description = getAttributeOrNull(attributeValues[4], LocalizedText.class);
      UInteger writeMask = getAttributeOrNull(attributeValues[5], UInteger.class);
      UInteger userWriteMask = getAttributeOrNull(attributeValues[6], UInteger.class);
      RolePermissionType[] rolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[7], RolePermissionType[].class);
      RolePermissionType[] userRolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[8], RolePermissionType[].class);
      AccessRestrictionType accessRestrictions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[9], AccessRestrictionType.class);

      Boolean isAbstract = (Boolean) attributeValues[10].value().value();

      return new UaObjectTypeNode(
          client,
          nodeId,
          nodeClass,
          browseName,
          displayName,
          description,
          writeMask,
          userWriteMask,
          rolePermissions,
          userRolePermissions,
          accessRestrictions,
          isAbstract);
    } catch (Throwable t) {
      throw UaException.extract(t).orElse(new UaException(StatusCodes.Bad_UnexpectedError, t));
    }
  }

  private UaReferenceTypeNode newReferenceTypeNode(NodeId nodeId, DataValue[] attributeValues)
      throws UaException {

    DataValue nodeIdDataValue = attributeValues[0];
    StatusCode nodeIdStatusCode = nodeIdDataValue.statusCode();
    if (nodeIdStatusCode.isBad()) {
      throw new UaException(nodeIdStatusCode);
    }

    try {
      NodeClass nodeClass =
          NodeClass.from((Integer) requireNonNullElse(attributeValues[1].value().value(), 0));

      Preconditions.checkArgument(
          nodeClass == NodeClass.ReferenceType,
          "expected NodeClass.ReferenceType, got NodeClass." + nodeClass);

      QualifiedName browseName = (QualifiedName) attributeValues[2].value().value();
      LocalizedText displayName = (LocalizedText) attributeValues[3].value().value();
      LocalizedText description = getAttributeOrNull(attributeValues[4], LocalizedText.class);
      UInteger writeMask = getAttributeOrNull(attributeValues[5], UInteger.class);
      UInteger userWriteMask = getAttributeOrNull(attributeValues[6], UInteger.class);
      RolePermissionType[] rolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[7], RolePermissionType[].class);
      RolePermissionType[] userRolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[8], RolePermissionType[].class);
      AccessRestrictionType accessRestrictions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[9], AccessRestrictionType.class);

      Boolean isAbstract = (Boolean) attributeValues[10].value().value();
      Boolean symmetric = (Boolean) attributeValues[11].value().value();
      LocalizedText inverseName = getAttributeOrNull(attributeValues[12], LocalizedText.class);

      return new UaReferenceTypeNode(
          client,
          nodeId,
          nodeClass,
          browseName,
          displayName,
          description,
          writeMask,
          userWriteMask,
          rolePermissions,
          userRolePermissions,
          accessRestrictions,
          isAbstract,
          symmetric,
          inverseName);
    } catch (Throwable t) {
      throw UaException.extract(t).orElse(new UaException(StatusCodes.Bad_UnexpectedError, t));
    }
  }

  private UaVariableNode newVariableNode(
      NodeId nodeId, NodeId typeDefinitionId, DataValue[] attributeValues) throws UaException {

    DataValue nodeIdDataValue = attributeValues[0];
    StatusCode nodeIdStatusCode = nodeIdDataValue.statusCode();
    if (nodeIdStatusCode.isBad()) {
      throw new UaException(nodeIdStatusCode);
    }

    try {
      NodeClass nodeClass =
          NodeClass.from((Integer) requireNonNullElse(attributeValues[1].value().value(), 0));

      Preconditions.checkArgument(
          nodeClass == NodeClass.Variable,
          "expected NodeClass.Variable, got NodeClass." + nodeClass);

      QualifiedName browseName = (QualifiedName) attributeValues[2].value().value();
      LocalizedText displayName = (LocalizedText) attributeValues[3].value().value();
      LocalizedText description = getAttributeOrNull(attributeValues[4], LocalizedText.class);
      UInteger writeMask = getAttributeOrNull(attributeValues[5], UInteger.class);
      UInteger userWriteMask = getAttributeOrNull(attributeValues[6], UInteger.class);
      RolePermissionType[] rolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[7], RolePermissionType[].class);
      RolePermissionType[] userRolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[8], RolePermissionType[].class);
      AccessRestrictionType accessRestrictions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[9], AccessRestrictionType.class);

      DataValue value = attributeValues[10];
      NodeId dataType = (NodeId) attributeValues[11].value().value();
      Integer valueRank = (Integer) attributeValues[12].value().value();
      UInteger[] arrayDimensions = getAttributeOrNull(attributeValues[13], UInteger[].class);
      UByte accessLevel = (UByte) attributeValues[14].value().value();
      UByte userAccessLevel = (UByte) attributeValues[15].value().value();
      Double minimumSamplingInterval = getAttributeOrNull(attributeValues[16], Double.class);
      Boolean historizing = (Boolean) attributeValues[17].value().value();
      AccessLevelExType accessLevelEx =
          getAttributeOrNull(attributeValues[18], AccessLevelExType.class);

      VariableTypeManager.VariableNodeConstructor constructor =
          client
              .getVariableTypeManager()
              .getNodeConstructor(typeDefinitionId)
              .orElse(UaVariableNode::new);

      return constructor.apply(
          client,
          nodeId,
          nodeClass,
          browseName,
          displayName,
          description,
          writeMask,
          userWriteMask,
          rolePermissions,
          userRolePermissions,
          accessRestrictions,
          value,
          dataType,
          valueRank,
          arrayDimensions,
          accessLevel,
          userAccessLevel,
          minimumSamplingInterval,
          historizing,
          accessLevelEx);
    } catch (Throwable t) {
      throw UaException.extract(t).orElse(new UaException(StatusCodes.Bad_UnexpectedError, t));
    }
  }

  private UaVariableTypeNode newVariableTypeNode(NodeId nodeId, DataValue[] attributeValues)
      throws UaException {
    DataValue nodeIdDataValue = attributeValues[0];
    StatusCode nodeIdStatusCode = nodeIdDataValue.statusCode();
    if (nodeIdStatusCode.isBad()) {
      throw new UaException(nodeIdStatusCode);
    }

    try {
      NodeClass nodeClass =
          NodeClass.from((Integer) requireNonNullElse(attributeValues[1].value().value(), 0));

      Preconditions.checkArgument(
          nodeClass == NodeClass.VariableType,
          "expected NodeClass.VariableType, got NodeClass." + nodeClass);

      QualifiedName browseName = (QualifiedName) attributeValues[2].value().value();
      LocalizedText displayName = (LocalizedText) attributeValues[3].value().value();
      LocalizedText description = getAttributeOrNull(attributeValues[4], LocalizedText.class);
      UInteger writeMask = getAttributeOrNull(attributeValues[5], UInteger.class);
      UInteger userWriteMask = getAttributeOrNull(attributeValues[6], UInteger.class);
      RolePermissionType[] rolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[7], RolePermissionType[].class);
      RolePermissionType[] userRolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[8], RolePermissionType[].class);
      AccessRestrictionType accessRestrictions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[9], AccessRestrictionType.class);

      DataValue value = attributeValues[10];
      NodeId dataType = (NodeId) attributeValues[11].value().value();
      Integer valueRank = (Integer) attributeValues[12].value().value();
      UInteger[] arrayDimensions = getAttributeOrNull(attributeValues[13], UInteger[].class);
      Boolean isAbstract = (Boolean) attributeValues[14].value().value();

      return new UaVariableTypeNode(
          client,
          nodeId,
          nodeClass,
          browseName,
          displayName,
          description,
          writeMask,
          userWriteMask,
          rolePermissions,
          userRolePermissions,
          accessRestrictions,
          value,
          dataType,
          valueRank,
          arrayDimensions,
          isAbstract);
    } catch (Throwable t) {
      throw UaException.extract(t).orElse(new UaException(StatusCodes.Bad_UnexpectedError, t));
    }
  }

  private UaViewNode newViewNode(NodeId nodeId, DataValue[] attributeValues) throws UaException {
    DataValue nodeIdDataValue = attributeValues[0];
    StatusCode nodeIdStatusCode = nodeIdDataValue.statusCode();
    if (nodeIdStatusCode.isBad()) {
      throw new UaException(nodeIdStatusCode);
    }

    try {
      NodeClass nodeClass =
          NodeClass.from((Integer) requireNonNullElse(attributeValues[1].value().value(), 0));

      Preconditions.checkArgument(
          nodeClass == NodeClass.View, "expected NodeClass.View, got NodeClass." + nodeClass);

      QualifiedName browseName = (QualifiedName) attributeValues[2].value().value();
      LocalizedText displayName = (LocalizedText) attributeValues[3].value().value();
      LocalizedText description = getAttributeOrNull(attributeValues[4], LocalizedText.class);
      UInteger writeMask = getAttributeOrNull(attributeValues[5], UInteger.class);
      UInteger userWriteMask = getAttributeOrNull(attributeValues[6], UInteger.class);
      RolePermissionType[] rolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[7], RolePermissionType[].class);
      RolePermissionType[] userRolePermissions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[8], RolePermissionType[].class);
      AccessRestrictionType accessRestrictions =
          getAttributeOrNull(
              client.getStaticEncodingContext(), attributeValues[9], AccessRestrictionType.class);

      Boolean containsNoLoops = (Boolean) attributeValues[10].value().value();
      UByte eventNotifier = (UByte) attributeValues[11].value().value();

      return new UaViewNode(
          client,
          nodeId,
          nodeClass,
          browseName,
          displayName,
          description,
          writeMask,
          userWriteMask,
          rolePermissions,
          userRolePermissions,
          accessRestrictions,
          containsNoLoops,
          eventNotifier);
    } catch (Throwable t) {
      throw UaException.extract(t).orElse(new UaException(StatusCodes.Bad_UnexpectedError, t));
    }
  }

  @Nullable
  private static <T> T getAttributeOrNull(DataValue value, Class<T> attributeClazz) {
    StatusCode statusCode = value.statusCode();

    if (statusCode.isBad()) {
      return null;
    } else {
      Object attributeValue = value.value().value();

      try {
        return attributeClazz.cast(attributeValue);
      } catch (ClassCastException e) {
        return null;
      }
    }
  }

  @Nullable
  private static <T> T getAttributeOrNull(
      EncodingContext context, DataValue value, Class<T> attributeClazz) {

    StatusCode statusCode = value.statusCode();

    if (statusCode.isBad()) {
      return null;
    } else {
      Object o = value.value().value();

      try {
        if (o instanceof ExtensionObject) {
          ExtensionObject xo = (ExtensionObject) o;
          Object decoded = xo.decode(context);
          return attributeClazz.cast(decoded);
        } else if (o instanceof ExtensionObject[]) {
          ExtensionObject[] xos = (ExtensionObject[]) o;
          Class<?> componentType = attributeClazz.getComponentType();
          Object array = Array.newInstance(componentType, xos.length);

          for (int i = 0; i < xos.length; i++) {
            ExtensionObject xo = xos[i];
            Object decoded = xo.decode(context);
            Array.set(array, i, componentType.cast(decoded));
          }

          return attributeClazz.cast(array);
        } else if (OptionSetUInteger.class.isAssignableFrom(attributeClazz)
            && o instanceof UNumber) {
          Object optionSet = attributeClazz.getDeclaredConstructor(o.getClass()).newInstance(o);

          return attributeClazz.cast(optionSet);
        } else {
          return null;
        }
      } catch (Throwable t) {
        return null;
      }
    }
  }

  private static DataValue[] concat(DataValue[] left, DataValue[] right) {
    return Stream.concat(Stream.of(left), Stream.of(right)).toArray(DataValue[]::new);
  }

  public static class BrowseOptions {

    private final BrowseDirection browseDirection;
    private final NodeId referenceTypeId;
    private final boolean includeSubtypes;
    private final UInteger nodeClassMask;
    private final UInteger resultMask;
    private final UInteger maxReferencesPerNode;

    public BrowseOptions() {
      this(
          BrowseDirection.Forward,
          NodeIds.HierarchicalReferences,
          true,
          uint(0xFF),
          uint(0x3F),
          uint(0));
    }

    public BrowseOptions(
        BrowseDirection browseDirection,
        NodeId referenceTypeId,
        boolean includeSubtypes,
        UInteger nodeClassMask,
        UInteger resultMask,
        UInteger maxReferencesPerNode) {

      this.browseDirection = browseDirection;
      this.referenceTypeId = referenceTypeId;
      this.includeSubtypes = includeSubtypes;
      this.nodeClassMask = nodeClassMask;
      this.resultMask = resultMask;
      this.maxReferencesPerNode = maxReferencesPerNode;
    }

    public BrowseDirection getBrowseDirection() {
      return browseDirection;
    }

    public NodeId getReferenceTypeId() {
      return referenceTypeId;
    }

    public boolean isIncludeSubtypes() {
      return includeSubtypes;
    }

    public UInteger getNodeClassMask() {
      return nodeClassMask;
    }

    public UInteger getResultMask() {
      return resultMask;
    }

    public UInteger getMaxReferencesPerNode() {
      return maxReferencesPerNode;
    }

    public BrowseOptions copy(Consumer<Builder> builderConsumer) {
      Builder builder = new Builder(this);

      builderConsumer.accept(builder);

      return builder.build();
    }

    public static Builder builder() {
      return new Builder();
    }

    public static class Builder {

      private BrowseDirection browseDirection = BrowseDirection.Forward;
      private NodeId referenceTypeId = NodeIds.HierarchicalReferences;
      private boolean includeSubtypes = true;
      private UInteger nodeClassMask = uint(0xFF);
      private UInteger resultMask = uint(0x3F);
      private UInteger maxReferencesPerNode = uint(0);

      private Builder() {}

      private Builder(BrowseOptions browseOptions) {
        this.browseDirection = browseOptions.getBrowseDirection();
        this.referenceTypeId = browseOptions.getReferenceTypeId();
        this.includeSubtypes = browseOptions.isIncludeSubtypes();
        this.nodeClassMask = browseOptions.getNodeClassMask();
        this.resultMask = browseOptions.getResultMask();
        this.maxReferencesPerNode = browseOptions.getMaxReferencesPerNode();
      }

      public Builder setBrowseDirection(BrowseDirection browseDirection) {
        this.browseDirection = browseDirection;
        return this;
      }

      public Builder setReferenceType(NodeId referenceTypeId) {
        this.referenceTypeId = referenceTypeId;
        return this;
      }

      public Builder setIncludeSubtypes(boolean includeSubtypes) {
        this.includeSubtypes = includeSubtypes;
        return this;
      }

      public Builder setNodeClassMask(UInteger nodeClassMask) {
        this.nodeClassMask = nodeClassMask;
        return this;
      }

      public Builder setNodeClassMask(Set<NodeClass> nodeClasses) {
        int mask = 0;
        for (NodeClass nodeClass : nodeClasses) {
          mask |= nodeClass.getValue();
        }
        return setNodeClassMask(uint(mask));
      }

      public Builder setResultMask(UInteger resultMask) {
        this.resultMask = resultMask;
        return this;
      }

      public Builder setResultMask(Set<BrowseResultMask> resultMasks) {
        int mask = 0;
        for (BrowseResultMask result : resultMasks) {
          mask |= result.getValue();
        }
        return setResultMask(uint(mask));
      }

      public Builder setMaxReferencesPerNode(UInteger maxReferencesPerNode) {
        this.maxReferencesPerNode = maxReferencesPerNode;
        return this;
      }

      public BrowseOptions build() {
        return new BrowseOptions(
            browseDirection,
            referenceTypeId,
            includeSubtypes,
            nodeClassMask,
            resultMask,
            maxReferencesPerNode);
      }
    }
  }
}
