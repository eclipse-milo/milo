/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * The Properties of a Server's OperationLimits Object.
 *
 * <p>Each constant identifies one limit and the well-known NodeId of the Property that advertises
 * it under {@code Server.ServerCapabilities.OperationLimits}. Use these constants to look up a
 * value in {@link OperationLimits#get(OperationLimit)} or to configure an override with {@link
 * OpcUaClientConfigBuilder#setOperationLimitOverrides(java.util.Map)}.
 *
 * @see <a href="https://reference.opcfoundation.org/Core/Part5/v105/docs/6.3.11">
 *     https://reference.opcfoundation.org/Core/Part5/v105/docs/6.3.11</a>
 */
public enum OperationLimit {

  /** The maximum size of the nodesToRead array in a Read request. */
  MaxNodesPerRead(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerRead),

  /** The maximum size of the nodesToWrite array in a Write request. */
  MaxNodesPerWrite(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerWrite),

  /** The maximum size of the methodsToCall array in a Call request. */
  MaxNodesPerMethodCall(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerMethodCall),

  /**
   * The maximum size of the nodesToBrowse array in a Browse request, or the continuationPoints
   * array in a BrowseNext request.
   */
  MaxNodesPerBrowse(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerBrowse),

  /**
   * The maximum size of the nodesToRegister array in a RegisterNodes request and the
   * nodesToUnregister array in an UnregisterNodes request.
   */
  MaxNodesPerRegisterNodes(
      NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerRegisterNodes),

  /** The maximum size of the browsePaths array in a TranslateBrowsePathsToNodeIds request. */
  MaxNodesPerTranslateBrowsePathsToNodeIds(
      NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds),

  /**
   * The maximum size of the nodesToAdd, referencesToAdd, nodesToDelete, and referencesToDelete
   * arrays in AddNodes, AddReferences, DeleteNodes, and DeleteReferences requests.
   */
  MaxNodesPerNodeManagement(
      NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerNodeManagement),

  /**
   * The maximum size of the operation array in CreateMonitoredItems, ModifyMonitoredItems,
   * DeleteMonitoredItems, SetMonitoringMode, and SetTriggering requests.
   */
  MaxMonitoredItemsPerCall(
      NodeIds.Server_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall),

  /** The maximum size of the nodesToRead array in a HistoryRead request for data. */
  MaxNodesPerHistoryReadData(
      NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadData),

  /** The maximum size of the nodesToRead array in a HistoryRead request for events. */
  MaxNodesPerHistoryReadEvents(
      NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadEvents),

  /** The maximum size of the historyUpdateDetails array in a HistoryUpdate request for data. */
  MaxNodesPerHistoryUpdateData(
      NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateData),

  /** The maximum size of the historyUpdateDetails array in a HistoryUpdate request for events. */
  MaxNodesPerHistoryUpdateEvents(
      NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateEvents);

  private final NodeId nodeId;

  OperationLimit(NodeId nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Get the NodeId of the Property that advertises this limit in the Server's address space.
   *
   * @return the NodeId of the Property that advertises this limit.
   */
  public NodeId getNodeId() {
    return nodeId;
  }
}
