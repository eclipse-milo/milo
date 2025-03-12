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

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.jspecify.annotations.Nullable;

/**
 * Operation limits of the Server, obtained by reading Variables of the OperationLimits Object.
 *
 * @see <a href="https://reference.opcfoundation.org/Core/Part5/v105/docs/6.3.11">
 *     https://reference.opcfoundation.org/Core/Part5/v105/docs/6.3.11</a>
 */
public class OperationLimits {

  private final UInteger maxNodesPerRead;
  private final UInteger maxNodesPerWrite;
  private final UInteger maxNodesPerMethodCall;
  private final UInteger maxNodesPerBrowse;
  private final UInteger maxNodesPerRegisterNodes;
  private final UInteger maxNodesPerTranslateBrowsePathsToNodeIds;
  private final UInteger maxNodesPerNodeManagement;
  private final UInteger maxMonitoredItemsPerCall;
  private final UInteger maxNodesPerHistoryReadData;
  private final UInteger maxNodesPerHistoryReadEvents;
  private final UInteger maxNodesPerHistoryUpdateData;
  private final UInteger maxNodesPerHistoryUpdateEvents;

  public OperationLimits(
      @Nullable UInteger maxNodesPerRead,
      @Nullable UInteger maxNodesPerWrite,
      @Nullable UInteger maxNodesPerMethodCall,
      @Nullable UInteger maxNodesPerBrowse,
      @Nullable UInteger maxNodesPerRegisterNodes,
      @Nullable UInteger maxNodesPerTranslateBrowsePathsToNodeIds,
      @Nullable UInteger maxNodesPerNodeManagement,
      @Nullable UInteger maxMonitoredItemsPerCall,
      @Nullable UInteger maxNodesPerHistoryReadData,
      @Nullable UInteger maxNodesPerHistoryReadEvents,
      @Nullable UInteger maxNodesPerHistoryUpdateData,
      @Nullable UInteger maxNodesPerHistoryUpdateEvents) {

    this.maxNodesPerRead = maxNodesPerRead;
    this.maxNodesPerWrite = maxNodesPerWrite;
    this.maxNodesPerMethodCall = maxNodesPerMethodCall;
    this.maxNodesPerBrowse = maxNodesPerBrowse;
    this.maxNodesPerRegisterNodes = maxNodesPerRegisterNodes;
    this.maxNodesPerTranslateBrowsePathsToNodeIds = maxNodesPerTranslateBrowsePathsToNodeIds;
    this.maxNodesPerNodeManagement = maxNodesPerNodeManagement;
    this.maxMonitoredItemsPerCall = maxMonitoredItemsPerCall;
    this.maxNodesPerHistoryReadData = maxNodesPerHistoryReadData;
    this.maxNodesPerHistoryReadEvents = maxNodesPerHistoryReadEvents;
    this.maxNodesPerHistoryUpdateData = maxNodesPerHistoryUpdateData;
    this.maxNodesPerHistoryUpdateEvents = maxNodesPerHistoryUpdateEvents;
  }

  /**
   * @return the maximum size of the nodesToRead array when a Client calls the Read Service.
   */
  public Optional<UInteger> maxNodesPerRead() {
    return Optional.ofNullable(maxNodesPerRead);
  }

  /**
   * @return the maximum size of the nodesToWrite array when a Client calls the Write Service.
   */
  public Optional<UInteger> maxNodesPerWrite() {
    return Optional.ofNullable(maxNodesPerWrite);
  }

  /**
   * @return the maximum size of the methodsToCall array when a Client calls the Call Service.
   */
  public Optional<UInteger> maxNodesPerMethodCall() {
    return Optional.ofNullable(maxNodesPerMethodCall);
  }

  /**
   * @return the maximum size of the nodesToBrowse array when a Client calls the Browse Service, or
   *     the continuationPoints array when a Client calls the BrowseNext Service.
   */
  public Optional<UInteger> maxNodesPerBrowse() {
    return Optional.ofNullable(maxNodesPerBrowse);
  }

  /**
   * @return the maximum size of the nodesToRegister array when a Client calls the RegisterNodes
   *     Service and the maximum size of the nodesToUnregister array when a Client calls the
   *     UnregisterNodes Service.
   */
  public Optional<UInteger> maxNodesPerRegisterNodes() {
    return Optional.ofNullable(maxNodesPerRegisterNodes);
  }

  /**
   * @return the maximum size of the browsePaths array when a Client calls the
   *     TranslateBrowsePathsToNodeIds Service.
   */
  public Optional<UInteger> maxNodesPerTranslateBrowsePathsToNodeIds() {
    return Optional.ofNullable(maxNodesPerTranslateBrowsePathsToNodeIds);
  }

  /**
   * @return the maximum size of the nodesToAdd, referencesToAdd, nodesToDelete, and
   *     referencesToDelete arrays when a Client calls the AddNodes, AddReferences, DeleteNodes, and
   *     DeleteReferences Services.
   */
  public Optional<UInteger> maxNodesPerNodeManagement() {
    return Optional.ofNullable(maxNodesPerNodeManagement);
  }

  /**
   * @return the maximum size of the array when a Client calls the CreateMonitoredItems,
   *     ModifyMonitoredItems, DeleteMonitoredItems, SetMonitoringMode, and SetTriggering services.
   */
  public Optional<UInteger> maxMonitoredItemsPerCall() {
    return Optional.ofNullable(maxMonitoredItemsPerCall);
  }

  /**
   * @return the maximum size of the nodesToRead array when a Client calls the HistoryRead Service
   *     for data.
   */
  public Optional<UInteger> maxNodesPerHistoryReadData() {
    return Optional.ofNullable(maxNodesPerHistoryReadData);
  }

  /**
   * @return the maximum size of the nodesToRead array when a Client calls the HistoryRead Service
   *     for events.
   */
  public Optional<UInteger> maxNodesPerHistoryReadEvents() {
    return Optional.ofNullable(maxNodesPerHistoryReadEvents);
  }

  /**
   * @return the maximum size of the historyUpdateDetails array when a Client calls the
   *     HistoryUpdate Service for data.
   */
  public Optional<UInteger> maxNodesPerHistoryUpdateData() {
    return Optional.ofNullable(maxNodesPerHistoryUpdateData);
  }

  /**
   * @return the maximum size of the historyUpdateDetails array when a Client calls the
   *     HistoryUpdate Service for events.
   */
  public Optional<UInteger> maxNodesPerHistoryUpdateEvents() {
    return Optional.ofNullable(maxNodesPerHistoryUpdateEvents);
  }

  private static final List<NodeId> OPERATION_LIMITS_NODES =
      List.of(
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerRead,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerWrite,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerMethodCall,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerBrowse,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerRegisterNodes,
          NodeIds
              .Server_ServerCapabilities_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerNodeManagement,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadData,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadEvents,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateData,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateEvents);

  static OperationLimits read(OpcUaClient client) throws UaException {
    try {
      return readAllNodes(client);
    } catch (UaException e) {
      if (e.getStatusCode().getValue() == StatusCodes.Bad_TooManyOperations) {
        // Whelp, the read operation limit is so low we can't read all the nodes at once.
        // Read them individually instead.
        return readIndividualNodes(client);
      } else {
        throw e;
      }
    }
  }

  private static OperationLimits readAllNodes(OpcUaClient client) throws UaException {
    List<DataValue> values =
        client.readValues(0.0, TimestampsToReturn.Neither, OPERATION_LIMITS_NODES);

    UInteger maxNodesPerRead = (UInteger) values.get(0).value().value();
    UInteger maxNodesPerWrite = (UInteger) values.get(1).value().value();
    UInteger maxNodesPerMethodCall = (UInteger) values.get(2).value().value();
    UInteger maxNodesPerBrowse = (UInteger) values.get(3).value().value();
    UInteger maxNodesPerRegisterNodes = (UInteger) values.get(4).value().value();
    UInteger maxNodesPerTranslateBrowsePathsToNodeIds = (UInteger) values.get(5).value().value();
    UInteger maxNodesPerNodeManagement = (UInteger) values.get(6).value().value();
    UInteger maxMonitoredItemsPerCall = (UInteger) values.get(7).value().value();
    UInteger maxNodesPerHistoryReadData = (UInteger) values.get(8).value().value();
    UInteger maxNodesPerHistoryReadEvents = (UInteger) values.get(9).value().value();
    UInteger maxNodesPerHistoryUpdateData = (UInteger) values.get(10).value().value();
    UInteger maxNodesPerHistoryUpdateEvents = (UInteger) values.get(11).value().value();

    return new OperationLimits(
        maxNodesPerRead,
        maxNodesPerWrite,
        maxNodesPerMethodCall,
        maxNodesPerBrowse,
        maxNodesPerRegisterNodes,
        maxNodesPerTranslateBrowsePathsToNodeIds,
        maxNodesPerNodeManagement,
        maxMonitoredItemsPerCall,
        maxNodesPerHistoryReadData,
        maxNodesPerHistoryReadEvents,
        maxNodesPerHistoryUpdateData,
        maxNodesPerHistoryUpdateEvents);
  }

  private static OperationLimits readIndividualNodes(OpcUaClient client) throws UaException {
    Function<NodeId, UInteger> read =
        nodeId -> {
          try {
            return (UInteger)
                client.readValue(0.0, TimestampsToReturn.Neither, nodeId).value().value();
          } catch (UaException e) {
            return null;
          }
        };

    // checkstyle:off
    UInteger maxNodesPerRead =
        read.apply(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerRead);
    UInteger maxNodesPerWrite =
        read.apply(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerWrite);
    UInteger maxNodesPerMethodCall =
        read.apply(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerMethodCall);
    UInteger maxNodesPerBrowse =
        read.apply(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerBrowse);
    UInteger maxNodesPerRegisterNodes =
        read.apply(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerRegisterNodes);
    UInteger maxNodesPerTranslateBrowsePathsToNodeIds =
        read.apply(
            NodeIds
                .Server_ServerCapabilities_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds);
    UInteger maxNodesPerNodeManagement =
        read.apply(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerNodeManagement);
    UInteger maxMonitoredItemsPerCall =
        read.apply(NodeIds.Server_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall);
    UInteger maxNodesPerHistoryReadData =
        read.apply(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadData);
    UInteger maxNodesPerHistoryReadEvents =
        read.apply(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadEvents);
    UInteger maxNodesPerHistoryUpdateData =
        read.apply(NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateData);
    UInteger maxNodesPerHistoryUpdateEvents =
        read.apply(
            NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateEvents);
    // checkstyle:on

    return new OperationLimits(
        maxNodesPerRead,
        maxNodesPerWrite,
        maxNodesPerMethodCall,
        maxNodesPerBrowse,
        maxNodesPerRegisterNodes,
        maxNodesPerTranslateBrowsePathsToNodeIds,
        maxNodesPerNodeManagement,
        maxMonitoredItemsPerCall,
        maxNodesPerHistoryReadData,
        maxNodesPerHistoryReadEvents,
        maxNodesPerHistoryUpdateData,
        maxNodesPerHistoryUpdateEvents);
  }
}
