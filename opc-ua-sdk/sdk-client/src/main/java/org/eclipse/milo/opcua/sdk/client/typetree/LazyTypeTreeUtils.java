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
import java.util.Set;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OperationLimits;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Shared utilities for lazy type tree implementations. */
final class LazyTypeTreeUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(LazyTypeTreeUtils.class);

  private LazyTypeTreeUtils() {}

  /**
   * Browse inverse HasSubtype references from {@code startId} until reaching a node that exists in
   * {@code knownTypeIds}.
   *
   * @param client the OPC UA client to use for browsing.
   * @param startId the NodeId to start browsing from.
   * @param knownTypeIds the set of NodeIds that are already known/loaded.
   * @param namespaceTable the namespace table for converting ExpandedNodeIds.
   * @return path from startId to known ancestor (inclusive), or empty list if unreachable.
   */
  static List<NodeId> browseInverseUntilKnown(
      OpcUaClient client, NodeId startId, Set<NodeId> knownTypeIds, NamespaceTable namespaceTable) {

    List<NodeId> path = new ArrayList<>();
    NodeId current = startId;

    while (current != null && !knownTypeIds.contains(current)) {
      path.add(current);
      current = browseInverseParent(client, current, namespaceTable);
    }

    if (current != null && knownTypeIds.contains(current)) {
      path.add(current);
      return path;
    }

    return List.of();
  }

  /**
   * Browse the inverse HasSubtype reference from {@code nodeId} to find its parent type.
   *
   * @param client the OPC UA client to use for browsing.
   * @param nodeId the NodeId to browse from.
   * @param namespaceTable the namespace table for converting ExpandedNodeIds.
   * @return the parent NodeId, or null if not found.
   */
  static @Nullable NodeId browseInverseParent(
      OpcUaClient client, NodeId nodeId, NamespaceTable namespaceTable) {

    try {
      BrowseDescription bd =
          new BrowseDescription(
              nodeId,
              BrowseDirection.Inverse,
              NodeIds.HasSubtype,
              false,
              uint(NodeClass.DataType.getValue()),
              uint(BrowseResultMask.All.getValue()));

      BrowseResult result = client.browse(bd);

      if (result.getStatusCode().isGood()
          && result.getReferences() != null
          && result.getReferences().length > 0) {

        return result.getReferences()[0].getNodeId().toNodeId(namespaceTable).orElse(null);
      }
    } catch (UaException e) {
      LOGGER.debug("Failed to browse inverse parent for {}: {}", nodeId, e.getMessage());
    }

    return null;
  }

  /**
   * Read values with operation limits, partitioning requests as necessary.
   *
   * @param client the OPC UA client.
   * @param readValueIds the list of ReadValueIds to read.
   * @param limits the operation limits from the server.
   * @return the list of DataValues corresponding to the read requests.
   */
  static List<DataValue> readWithOperationLimits(
      OpcUaClient client, List<ReadValueId> readValueIds, OperationLimits limits) {

    return ClientBrowseUtils.readWithOperationLimits(client, readValueIds, limits);
  }

  /**
   * Browse with operation limits, partitioning requests as necessary.
   *
   * @param client the OPC UA client.
   * @param browseDescriptions the list of BrowseDescriptions.
   * @param limits the operation limits from the server.
   * @return the list of reference description lists corresponding to each browse request.
   */
  static List<List<ReferenceDescription>> browseWithOperationLimits(
      OpcUaClient client, List<BrowseDescription> browseDescriptions, OperationLimits limits) {

    return ClientBrowseUtils.browseWithOperationLimits(client, browseDescriptions, limits);
  }
}
