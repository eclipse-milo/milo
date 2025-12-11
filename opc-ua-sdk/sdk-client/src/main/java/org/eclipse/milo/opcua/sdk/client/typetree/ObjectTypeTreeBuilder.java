/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OperationLimits;
import org.eclipse.milo.opcua.sdk.core.typetree.ObjectType;
import org.eclipse.milo.opcua.sdk.core.typetree.ObjectTypeTree;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds an {@link ObjectTypeTree} by recursively browsing the ObjectType hierarchy starting at
 * {@link NodeIds#BaseObjectType}.
 */
public class ObjectTypeTreeBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(ObjectTypeTreeBuilder.class);

  /**
   * Build an {@link ObjectTypeTree} by recursively browsing the ObjectType hierarchy starting at
   * {@link NodeIds#BaseObjectType}.
   *
   * @param client a connected {@link OpcUaClient}.
   * @return an {@link ObjectTypeTree}.
   */
  public static ObjectTypeTree build(OpcUaClient client) throws UaException {
    Tree<ObjectType> root =
        new Tree<>(
            null,
            new ClientObjectType(
                QualifiedName.parse("0:BaseObjectType"), NodeIds.BaseObjectType, false));

    NamespaceTable namespaceTable = client.readNamespaceTable();

    OperationLimits operationLimits = ClientBrowseUtils.getOperationLimits(client);

    addChildren(List.of(root), client, namespaceTable, operationLimits);

    return new ObjectTypeTree(root);
  }

  /**
   * Build an {@link ObjectTypeTree} by recursively browsing the ObjectType hierarchy starting at
   * {@link NodeIds#BaseObjectType}.
   *
   * @param client a connected {@link OpcUaClient}.
   * @return a {@link CompletableFuture} that completes successfully with an {@link ObjectTypeTree},
   *     or completes exceptionally if an error occurs.
   */
  public static CompletableFuture<ObjectTypeTree> buildAsync(OpcUaClient client) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            return build(client);
          } catch (UaException e) {
            throw new CompletionException(e);
          }
        },
        client.getTransport().getConfig().getExecutor());
  }

  private static void addChildren(
      List<Tree<ObjectType>> parentTypes,
      OpcUaClient client,
      NamespaceTable namespaceTable,
      OperationLimits operationLimits) {

    List<List<ReferenceDescription>> parentSubtypes =
        ClientBrowseUtils.browseWithOperationLimits(
            client,
            parentTypes.stream()
                .map(
                    tree ->
                        new BrowseDescription(
                            tree.getValue().getNodeId(),
                            BrowseDirection.Forward,
                            NodeIds.HasSubtype,
                            true,
                            uint(NodeClass.ObjectType.getValue()),
                            uint(BrowseResultMask.All.getValue())))
                .collect(Collectors.toList()),
            operationLimits);

    var childTypes = new ArrayList<Tree<ObjectType>>();

    for (int i = 0; i < parentTypes.size(); i++) {
      Tree<ObjectType> tree = parentTypes.get(i);
      List<ReferenceDescription> subtypes = parentSubtypes.get(i);

      List<NodeId> objectTypeIds =
          subtypes.stream()
              .map(
                  reference ->
                      reference.getNodeId().toNodeId(namespaceTable).orElse(NodeId.NULL_VALUE))
              .collect(Collectors.toList());

      List<Boolean> isAbstractValues =
          readIsAbstractAttributes(client, objectTypeIds, operationLimits);

      assert subtypes.size() == objectTypeIds.size() && subtypes.size() == isAbstractValues.size();

      var objectTypes = new ArrayList<ClientObjectType>();

      for (int j = 0; j < subtypes.size(); j++) {
        QualifiedName browseName = subtypes.get(j).getBrowseName();
        NodeId objectTypeId = objectTypeIds.get(j);
        Boolean isAbstract = isAbstractValues.get(j);

        var objectType = new ClientObjectType(browseName, objectTypeId, isAbstract);

        objectTypes.add(objectType);

        LOGGER.debug("Added ObjectType: {}", objectType.getBrowseName().toParseableString());
      }

      for (ClientObjectType objectType : objectTypes) {
        Tree<ObjectType> childNode = tree.addChild(objectType);

        childTypes.add(childNode);
      }
    }

    if (!childTypes.isEmpty()) {
      addChildren(childTypes, client, namespaceTable, operationLimits);
    }
  }

  private static List<Boolean> readIsAbstractAttributes(
      OpcUaClient client, List<NodeId> objectTypeIds, OperationLimits operationLimits) {

    if (objectTypeIds.isEmpty()) {
      return List.of();
    }

    var readValueIds = new ArrayList<ReadValueId>();

    for (NodeId objectTypeId : objectTypeIds) {
      readValueIds.add(
          new ReadValueId(
              objectTypeId, AttributeId.IsAbstract.uid(), null, QualifiedName.NULL_VALUE));
    }

    var isAbstractValues = new ArrayList<Boolean>();

    List<DataValue> values =
        ClientBrowseUtils.readWithOperationLimits(client, readValueIds, operationLimits);

    for (DataValue value : values) {
      Boolean isAbstract = false;

      if (value.statusCode().isGood()) {
        isAbstract = (Boolean) value.value().value();
      }

      isAbstractValues.add(isAbstract);
    }

    return isAbstractValues;
  }
}
