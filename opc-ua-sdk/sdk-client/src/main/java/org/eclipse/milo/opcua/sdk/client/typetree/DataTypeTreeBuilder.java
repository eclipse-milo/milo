/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds a {@link DataTypeTree} by recursively browsing the DataType hierarchy starting at {@link
 * NodeIds#BaseDataType}.
 */
public class DataTypeTreeBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataTypeTreeBuilder.class);

  /**
   * Build a {@link DataTypeTree} by recursively browsing the DataType hierarchy starting at {@link
   * NodeIds#BaseDataType}.
   *
   * @param client a connected {@link OpcUaClient}.
   * @return a {@link DataTypeTree}.
   */
  public static DataTypeTree build(OpcUaClient client) throws UaException {
    Tree<DataType> root =
        new Tree<>(
            null,
            new ClientDataType(
                QualifiedName.parse("0:BaseDataType"),
                NodeIds.BaseDataType,
                null,
                null,
                null,
                null,
                true));

    NamespaceTable namespaceTable = client.readNamespaceTable();

    UInteger[] operationLimits = readOperationLimits(client);
    UInteger maxNodesPerBrowse = operationLimits[0];
    UInteger maxNodesPerRead = operationLimits[1];

    addChildren(List.of(root), client, namespaceTable, maxNodesPerBrowse, maxNodesPerRead);

    return new DataTypeTree(root);
  }

  /**
   * Build a {@link DataTypeTree} by recursively browsing the DataType hierarchy starting at {@link
   * NodeIds#BaseDataType}.
   *
   * @param client a connected {@link OpcUaClient}.
   * @return a {@link CompletableFuture} that completes successfully with a {@link DataTypeTree}, or
   *     completes exceptionally if an error occurs.
   */
  public static CompletableFuture<DataTypeTree> buildAsync(OpcUaClient client) {
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
      List<Tree<DataType>> parentTypes,
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
                            uint(NodeClass.DataType.getValue()),
                            uint(BrowseResultMask.All.getValue())))
                .collect(Collectors.toList()),
            maxNodesPerBrowse);

    var childTypes = new ArrayList<Tree<DataType>>();

    for (int i = 0; i < parentTypes.size(); i++) {
      Tree<DataType> tree = parentTypes.get(i);
      List<ReferenceDescription> subtypes = parentSubtypes.get(i);

      List<NodeId> dataTypeIds =
          subtypes.stream()
              .map(
                  reference ->
                      reference.getNodeId().toNodeId(namespaceTable).orElse(NodeId.NULL_VALUE))
              .collect(Collectors.toList());

      List<List<ReferenceDescription>> encodingReferences =
          browseEncodings(client, dataTypeIds, maxNodesPerBrowse);

      List<Attributes> dataTypeAttributes =
          readDataTypeAttributes(client, dataTypeIds, maxNodesPerRead);

      assert subtypes.size() == dataTypeIds.size()
          && subtypes.size() == encodingReferences.size()
          && subtypes.size() == dataTypeAttributes.size();

      var dataTypes = new ArrayList<ClientDataType>();

      for (int j = 0; j < subtypes.size(); j++) {
        QualifiedName browseName = subtypes.get(j).getBrowseName();
        NodeId dataTypeId = dataTypeIds.get(j);
        List<ReferenceDescription> encodings = encodingReferences.get(j);
        DataTypeDefinition dataTypeDefinition = dataTypeAttributes.get(j).definition;
        Boolean isAbstract = dataTypeAttributes.get(j).isAbstract;

        NodeId binaryEncodingId = null;
        NodeId xmlEncodingId = null;
        NodeId jsonEncodingId = null;

        for (ReferenceDescription r : encodings) {
          // Observed multiple servers at IOP using the wrong namespace index...
          // Be lenient and also allow matching on the unqualified browse name.

          if (r.getBrowseName().equals(DataTypeEncoding.BINARY_ENCODING_NAME)
              || Objects.equals(r.getBrowseName().name(), "Default Binary")) {

            binaryEncodingId = r.getNodeId().toNodeId(namespaceTable).orElse(null);
          } else if (r.getBrowseName().equals(DataTypeEncoding.XML_ENCODING_NAME)
              || Objects.equals(r.getBrowseName().name(), "Default XML")) {

            xmlEncodingId = r.getNodeId().toNodeId(namespaceTable).orElse(null);
          } else if (r.getBrowseName().equals(DataTypeEncoding.JSON_ENCODING_NAME)
              || Objects.equals(r.getBrowseName().name(), "Default JSON")) {

            jsonEncodingId = r.getNodeId().toNodeId(namespaceTable).orElse(null);
          }
        }

        var dataType =
            new ClientDataType(
                browseName,
                dataTypeId,
                binaryEncodingId,
                xmlEncodingId,
                jsonEncodingId,
                dataTypeDefinition,
                isAbstract);

        dataTypes.add(dataType);
      }

      for (ClientDataType dataType : dataTypes) {
        Tree<DataType> childNode = tree.addChild(dataType);

        childTypes.add(childNode);
      }
    }

    if (!childTypes.isEmpty()) {
      addChildren(childTypes, client, namespaceTable, maxNodesPerBrowse, maxNodesPerRead);
    }
  }

  private static List<List<ReferenceDescription>> browse(
      OpcUaClient client, List<BrowseDescription> browseDescriptions) {

    if (browseDescriptions.isEmpty()) {
      return List.of();
    }

    final List<List<ReferenceDescription>> references = new ArrayList<>();

    try {
      client
          .browse(browseDescriptions)
          .forEach(
              result -> {
                if (result.getStatusCode().isGood()) {
                  ReferenceDescription[] rds =
                      requireNonNullElse(result.getReferences(), new ReferenceDescription[0]);
                  references.add(List.of(rds));
                } else {
                  references.add(List.of());
                }
              });
    } catch (UaException e) {
      references.addAll(Collections.nCopies(browseDescriptions.size(), List.of()));
    }

    return references;
  }

  private static List<List<ReferenceDescription>> browseEncodings(
      OpcUaClient client, List<NodeId> dataTypeIds, UInteger maxNodesPerBrowse) {

    List<BrowseDescription> browseDescriptions =
        dataTypeIds.stream()
            .map(
                dataTypeId ->
                    new BrowseDescription(
                        dataTypeId,
                        BrowseDirection.Forward,
                        NodeIds.HasEncoding,
                        false,
                        uint(NodeClass.Object.getValue()),
                        uint(BrowseResultMask.All.getValue())))
            .collect(Collectors.toList());

    return browseWithOperationLimits(client, browseDescriptions, maxNodesPerBrowse);
  }

  private static List<@Nullable Attributes> readDataTypeAttributes(
      OpcUaClient client, List<NodeId> dataTypeIds, UInteger maxNodesPerRead) {

    if (dataTypeIds.isEmpty()) {
      return List.of();
    }

    var readValueIds = new ArrayList<ReadValueId>();

    for (NodeId dataTypeId : dataTypeIds) {
      readValueIds.add(
          new ReadValueId(
              dataTypeId, AttributeId.IsAbstract.uid(), null, QualifiedName.NULL_VALUE));
      readValueIds.add(
          new ReadValueId(
              dataTypeId, AttributeId.DataTypeDefinition.uid(), null, QualifiedName.NULL_VALUE));
    }

    var attributes = new ArrayList<Attributes>();

    List<DataValue> values = readWithOperationLimits(client, readValueIds, maxNodesPerRead);

    for (int i = 0; i < values.size(); i += 2) {
      DataValue isAbstractValue = values.get(i);
      DataValue definitionValue = values.get(i + 1);

      Boolean isAbstract = false;
      DataTypeDefinition definition = null;

      if (isAbstractValue.getStatusCode() != null && isAbstractValue.getStatusCode().isGood()) {
        isAbstract = (Boolean) isAbstractValue.getValue().getValue();
      }

      if (definitionValue.getStatusCode() != null && definitionValue.getStatusCode().isGood()) {
        Object o = definitionValue.getValue().getValue();
        if (o instanceof ExtensionObject xo) {
          try {
            Object decoded = xo.decode(client.getStaticEncodingContext());

            definition = (DataTypeDefinition) decoded;
          } catch (Exception e) {
            LOGGER.debug("Error decoding DataTypeDefinition: {}", e.getMessage());
          }
        }
      }

      attributes.add(new Attributes(isAbstract, definition));
    }

    return attributes;
  }

  private static class Attributes {
    final Boolean isAbstract;
    final DataTypeDefinition definition;

    private Attributes(Boolean isAbstract, DataTypeDefinition definition) {
      this.isAbstract = isAbstract;
      this.definition = definition;
    }
  }

  private static UInteger[] readOperationLimits(OpcUaClient client) throws UaException {
    client.getOperationLimits();
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
    if (maxNodesPerBrowse.getStatusCode() != null
        && maxNodesPerBrowse.getStatusCode().isGood()
        && maxNodesPerBrowse.getValue().getValue() instanceof UInteger) {

      operationLimits[0] = (UInteger) maxNodesPerBrowse.getValue().getValue();
    }

    DataValue maxNodesPerRead = dataValues.get(1);
    if (maxNodesPerRead.getStatusCode() != null
        && maxNodesPerRead.getStatusCode().isGood()
        && dataValues.get(1).getValue().getValue() instanceof UInteger) {

      operationLimits[1] = (UInteger) maxNodesPerRead.getValue().getValue();
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
