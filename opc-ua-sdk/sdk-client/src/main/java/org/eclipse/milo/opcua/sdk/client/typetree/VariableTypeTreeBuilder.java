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
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.typetree.VariableType;
import org.eclipse.milo.opcua.sdk.core.typetree.VariableTypeTree;
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
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds a {@link VariableTypeTree} by recursively browsing the VariableType hierarchy starting at
 * {@link NodeIds#BaseVariableType}.
 */
public class VariableTypeTreeBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(VariableTypeTreeBuilder.class);

  /**
   * Build a {@link VariableTypeTree} by recursively browsing the VariableType hierarchy starting at
   * {@link NodeIds#BaseVariableType}.
   *
   * @param client a connected {@link OpcUaClient}.
   * @return a {@link VariableTypeTree}.
   */
  public static VariableTypeTree build(OpcUaClient client) throws UaException {
    Tree<VariableType> root =
        new Tree<>(
            null,
            new ClientVariableType(
                QualifiedName.parse("0:BaseVariableType"),
                NodeIds.BaseVariableType,
                new DataValue(Variant.NULL_VALUE),
                NodeIds.BaseDataType,
                ValueRanks.Scalar,
                null,
                true));

    NamespaceTable namespaceTable = client.readNamespaceTable();

    UInteger[] operationLimits = readOperationLimits(client);
    UInteger maxNodesPerBrowse = operationLimits[0];
    UInteger maxNodesPerRead = operationLimits[1];

    addChildren(List.of(root), client, namespaceTable, maxNodesPerBrowse, maxNodesPerRead);

    return new VariableTypeTree(root);
  }

  /**
   * Build a {@link VariableTypeTree} by recursively browsing the VariableType hierarchy starting at
   * {@link NodeIds#BaseVariableType}.
   *
   * @param client a connected {@link OpcUaClient}.
   * @return a {@link CompletableFuture} that completes successfully with a {@link
   *     VariableTypeTree}, or completes exceptionally if an error occurs.
   */
  public static CompletableFuture<VariableTypeTree> buildAsync(OpcUaClient client) {
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
      List<Tree<VariableType>> parentTypes,
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
                            uint(NodeClass.VariableType.getValue()),
                            uint(BrowseResultMask.All.getValue())))
                .collect(Collectors.toList()),
            maxNodesPerBrowse);

    var childTypes = new ArrayList<Tree<VariableType>>();

    for (int i = 0; i < parentTypes.size(); i++) {
      Tree<VariableType> tree = parentTypes.get(i);
      List<ReferenceDescription> subtypes = parentSubtypes.get(i);

      List<NodeId> variableTypeIds =
          subtypes.stream()
              .map(
                  reference ->
                      reference.getNodeId().toNodeId(namespaceTable).orElse(NodeId.NULL_VALUE))
              .collect(Collectors.toList());

      List<Attributes> variableTypeAttributes =
          readVariableTypeAttributes(client, variableTypeIds, maxNodesPerRead);

      assert subtypes.size() == variableTypeIds.size()
          && subtypes.size() == variableTypeAttributes.size();

      var variableTypes = new ArrayList<ClientVariableType>();

      for (int j = 0; j < subtypes.size(); j++) {
        QualifiedName browseName = subtypes.get(j).getBrowseName();
        NodeId variableTypeId = variableTypeIds.get(j);
        Attributes attributes = variableTypeAttributes.get(j);

        var variableType =
            new ClientVariableType(
                browseName,
                variableTypeId,
                attributes.value,
                attributes.dataType,
                attributes.valueRank,
                attributes.arrayDimensions,
                attributes.isAbstract);

        variableTypes.add(variableType);

        LOGGER.debug("Added VariableType: {}", variableType.getBrowseName().toParseableString());
      }

      for (ClientVariableType variableType : variableTypes) {
        Tree<VariableType> childNode = tree.addChild(variableType);

        childTypes.add(childNode);
      }
    }

    if (!childTypes.isEmpty()) {
      addChildren(childTypes, client, namespaceTable, maxNodesPerBrowse, maxNodesPerRead);
    }
  }

  private static List<Attributes> readVariableTypeAttributes(
      OpcUaClient client, List<NodeId> variableTypeIds, UInteger maxNodesPerRead) {

    if (variableTypeIds.isEmpty()) {
      return List.of();
    }

    var readValueIds = new ArrayList<ReadValueId>();

    for (NodeId variableTypeId : variableTypeIds) {
      readValueIds.add(
          new ReadValueId(
              variableTypeId, AttributeId.IsAbstract.uid(), null, QualifiedName.NULL_VALUE));
      readValueIds.add(
          new ReadValueId(variableTypeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE));
      readValueIds.add(
          new ReadValueId(
              variableTypeId, AttributeId.DataType.uid(), null, QualifiedName.NULL_VALUE));
      readValueIds.add(
          new ReadValueId(
              variableTypeId, AttributeId.ValueRank.uid(), null, QualifiedName.NULL_VALUE));
      readValueIds.add(
          new ReadValueId(
              variableTypeId, AttributeId.ArrayDimensions.uid(), null, QualifiedName.NULL_VALUE));
    }

    var attributes = new ArrayList<Attributes>();

    List<DataValue> values = readWithOperationLimits(client, readValueIds, maxNodesPerRead);

    for (int i = 0; i < values.size(); i += 5) {
      DataValue isAbstractValue = values.get(i);
      DataValue valueValue = values.get(i + 1);
      DataValue dataTypeValue = values.get(i + 2);
      DataValue valueRankValue = values.get(i + 3);
      DataValue arrayDimensionsValue = values.get(i + 4);

      Boolean isAbstract = false;
      DataValue value = new DataValue(Variant.NULL_VALUE);
      NodeId dataType = NodeIds.BaseDataType;
      Integer valueRank = ValueRanks.Scalar;
      UInteger[] arrayDimensions = null;

      if (isAbstractValue.statusCode().isGood()) {
        isAbstract = (Boolean) isAbstractValue.value().value();
      }

      if (valueValue.statusCode().isGood()) {
        value = valueValue;
      }

      if (dataTypeValue.statusCode().isGood()) {
        dataType = (NodeId) dataTypeValue.value().value();
      }

      if (valueRankValue.statusCode().isGood()) {
        valueRank = (Integer) valueRankValue.value().value();
      }

      if (arrayDimensionsValue.statusCode().isGood()) {
        arrayDimensions = (UInteger[]) arrayDimensionsValue.value().value();
      }

      attributes.add(new Attributes(isAbstract, value, dataType, valueRank, arrayDimensions));
    }

    return attributes;
  }

  private static class Attributes {
    final Boolean isAbstract;
    final DataValue value;
    final NodeId dataType;
    final Integer valueRank;
    final @Nullable UInteger[] arrayDimensions;

    private Attributes(
        Boolean isAbstract,
        DataValue value,
        NodeId dataType,
        Integer valueRank,
        @Nullable UInteger[] arrayDimensions) {

      this.isAbstract = isAbstract;
      this.value = value;
      this.dataType = dataType;
      this.valueRank = valueRank;
      this.arrayDimensions = arrayDimensions;
    }
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
