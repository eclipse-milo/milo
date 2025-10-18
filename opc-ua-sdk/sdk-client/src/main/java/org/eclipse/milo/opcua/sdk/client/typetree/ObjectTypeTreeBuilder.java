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

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.Lists.partition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.typetree.ObjectType;
import org.eclipse.milo.opcua.sdk.core.typetree.ObjectTypeTree;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
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

    UInteger[] operationLimits = readOperationLimits(client);
    UInteger maxNodesPerBrowse = operationLimits[0];
    UInteger maxNodesPerRead = operationLimits[1];

    addChildren(List.of(root), client, namespaceTable, maxNodesPerBrowse, maxNodesPerRead);

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
      UInteger maxNodesPerBrowse,
      UInteger maxNodesPerRead) {

    List<List<ReferenceDescription>> parentSubtypes =
        browseWithOperationLimits(
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
            maxNodesPerBrowse);

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
          readIsAbstractAttributes(client, objectTypeIds, maxNodesPerRead);

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
      addChildren(childTypes, client, namespaceTable, maxNodesPerBrowse, maxNodesPerRead);
    }
  }

  private static List<Boolean> readIsAbstractAttributes(
      OpcUaClient client, List<NodeId> objectTypeIds, UInteger maxNodesPerRead) {

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

    List<DataValue> values = readWithOperationLimits(client, readValueIds, maxNodesPerRead);

    for (DataValue value : values) {
      Boolean isAbstract = false;

      if (value.statusCode().isGood()) {
        isAbstract = (Boolean) value.value().value();
      }

      isAbstractValues.add(isAbstract);
    }

    return isAbstractValues;
  }

  private static List<List<ReferenceDescription>> browse(
      OpcUaClient client, List<BrowseDescription> browseDescriptions) {

    if (browseDescriptions.isEmpty()) {
      return List.of();
    }

    final var referenceDescriptionLists = new ArrayList<List<ReferenceDescription>>();

    try {
      client
          .browse(browseDescriptions)
          .forEach(
              result -> {
                if (result.getStatusCode().isGood()) {
                  var references = new ArrayList<ReferenceDescription>();

                  ReferenceDescription[] refs =
                      requireNonNullElse(result.getReferences(), new ReferenceDescription[0]);
                  Collections.addAll(references, refs);

                  ByteString continuationPoint = result.getContinuationPoint();
                  List<ReferenceDescription> nextRefs = maybeBrowseNext(client, continuationPoint);
                  references.addAll(nextRefs);

                  referenceDescriptionLists.add(references);
                } else {
                  referenceDescriptionLists.add(List.of());
                }
              });
    } catch (UaException e) {
      referenceDescriptionLists.addAll(Collections.nCopies(browseDescriptions.size(), List.of()));
    }

    return referenceDescriptionLists;
  }

  private static List<ReferenceDescription> maybeBrowseNext(
      OpcUaClient client, ByteString continuationPoint) {

    var references = new ArrayList<ReferenceDescription>();

    while (continuationPoint != null && continuationPoint.isNotNull()) {
      try {
        BrowseNextResponse response = client.browseNext(false, List.of(continuationPoint));

        BrowseResult result = requireNonNull(response.getResults())[0];

        ReferenceDescription[] rds =
            requireNonNullElse(result.getReferences(), new ReferenceDescription[0]);

        references.addAll(List.of(rds));

        continuationPoint = result.getContinuationPoint();
      } catch (Exception e) {
        LOGGER.warn("BrowseNext failed: {}", e.getMessage(), e);
        return references;
      }
    }

    return references;
  }

  private static UInteger[] readOperationLimits(OpcUaClient client) throws UaException {
    UInteger[] operationLimits = new UInteger[2];
    operationLimits[0] = uint(10);
    operationLimits[1] = uint(100);

    List<DataValue> dataValues =
        client.readValues(
            0.0,
            TimestampsToReturn.Neither,
            List.of(
                NodeIds.OperationLimitsType_MaxNodesPerBrowse,
                NodeIds.OperationLimitsType_MaxNodesPerRead));

    DataValue maxNodesPerBrowse = dataValues.get(0);
    if (maxNodesPerBrowse.statusCode().isGood()
        && maxNodesPerBrowse.value().value() instanceof UInteger) {

      operationLimits[0] = (UInteger) maxNodesPerBrowse.value().value();
    }

    DataValue maxNodesPerRead = dataValues.get(1);
    if (maxNodesPerRead.statusCode().isGood()
        && dataValues.get(1).value().value() instanceof UInteger) {

      operationLimits[1] = (UInteger) maxNodesPerRead.value().value();
    }

    return operationLimits;
  }

  private static List<DataValue> readWithOperationLimits(
      OpcUaClient client, List<ReadValueId> readValueIds, UInteger maxNodesPerRead) {

    if (readValueIds.isEmpty()) {
      return List.of();
    }

    LOGGER.debug("readWithOperationLimits: {}", readValueIds.size());

    int partitionSize =
        maxNodesPerRead.longValue() > Integer.MAX_VALUE
            ? Integer.MAX_VALUE
            : maxNodesPerRead.intValue();

    if (partitionSize == 0) {
      partitionSize = Integer.MAX_VALUE;
    }

    var values = new ArrayList<DataValue>();

    partition(readValueIds, partitionSize)
        .forEach(
            partition -> {
              try {
                ReadResponse response = client.read(0.0, TimestampsToReturn.Neither, partition);
                DataValue[] results = response.getResults();
                Collections.addAll(values, requireNonNull(results));
              } catch (UaException e) {
                var value = new DataValue(e.getStatusCode());
                values.addAll(Collections.nCopies(partition.size(), value));
              }
            });

    return values;
  }

  private static List<List<ReferenceDescription>> browseWithOperationLimits(
      OpcUaClient client, List<BrowseDescription> browseDescriptions, UInteger maxNodesPerBrowse) {

    if (browseDescriptions.isEmpty()) {
      return List.of();
    }

    LOGGER.debug("browseWithOperationLimits: {}", browseDescriptions.size());

    int partitionSize =
        maxNodesPerBrowse.longValue() > Integer.MAX_VALUE
            ? Integer.MAX_VALUE
            : maxNodesPerBrowse.intValue();

    if (partitionSize == 0) {
      partitionSize = Integer.MAX_VALUE;
    }

    var references = new ArrayList<List<ReferenceDescription>>();

    partition(browseDescriptions, partitionSize)
        .forEach(partition -> references.addAll(browse(client, partition)));

    return references;
  }
}
