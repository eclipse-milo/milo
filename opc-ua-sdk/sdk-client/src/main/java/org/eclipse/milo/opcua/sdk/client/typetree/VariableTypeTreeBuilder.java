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
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.typetree.VariableType;
import org.eclipse.milo.opcua.sdk.core.typetree.VariableTypeTree;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
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

    OperationLimits operationLimits = client.getOperationLimits();

    addChildren(List.of(root), client, namespaceTable, operationLimits);

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
                            uint(NodeClass.VariableType.getValue()),
                            uint(BrowseResultMask.All.getValue())))
                .collect(Collectors.toList()),
            operationLimits);

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
          readVariableTypeAttributes(client, variableTypeIds, operationLimits);

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
      addChildren(childTypes, client, namespaceTable, operationLimits);
    }
  }

  private static List<Attributes> readVariableTypeAttributes(
      OpcUaClient client, List<NodeId> variableTypeIds, OperationLimits operationLimits) {

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

    List<DataValue> values =
        ClientBrowseUtils.readWithOperationLimits(client, readValueIds, operationLimits);

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

  private record Attributes(
      Boolean isAbstract,
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      @Nullable UInteger[] arrayDimensions) {}
}
