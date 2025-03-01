/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.*;

/**
 * A simple {@link AddressSpaceFilter} that delegates each of the filter operations to a simple
 * filter on {@link NodeId} using the most reasonable NodeId from that operation's request as the
 * filter criteria.
 */
public abstract class SimpleAddressSpaceFilter implements AddressSpaceFilter {

  /**
   * Create a new {@link SimpleAddressSpaceFilter} that uses a {@link Predicate} on {@link NodeId}.
   *
   * @param nodeIdFilter a {@link Predicate} that tests a {@link NodeId}.
   * @return a new {@link SimpleAddressSpaceFilter} that uses a {@link Predicate} on {@link NodeId}.
   */
  public static SimpleAddressSpaceFilter create(Predicate<NodeId> nodeIdFilter) {
    return create(nodeIdFilter, nodeIdFilter);
  }

  /**
   * Create a new {@link SimpleAddressSpaceFilter} that uses a separate {@link Predicate} for {@link
   * NodeId}s and {@link MonitoredItem}s.
   *
   * @param nodeIdFilter a {@link Predicate} that tests a {@link NodeId}.
   * @param monitoredItemFilter a {@link Predicate} that tests a {@link NodeId} targeted by a {@link
   *     MonitoredItem}.
   * @return a new {@link SimpleAddressSpaceFilter} that uses a {@link Predicate} on {@link NodeId}.
   */
  public static SimpleAddressSpaceFilter create(
      Predicate<NodeId> nodeIdFilter, Predicate<NodeId> monitoredItemFilter) {

    return new SimpleAddressSpaceFilter() {
      @Override
      protected boolean filterNode(NodeId nodeId) {
        return nodeIdFilter.test(nodeId);
      }

      @Override
      protected boolean filterMonitoredItem(NodeId nodeId) {
        return monitoredItemFilter.test(nodeId);
      }
    };
  }

  /**
   * Return {@code true} if the operation {@code nodeId} belongs to should be handled this filter's
   * {@link AddressSpace}.
   *
   * <p>This is not an indication that a Node for {@code nodeId} exists, rather, it's an indication
   * that this AddressSpace would be responsible for the Node *if it did* exist.
   *
   * @param nodeId a {@link NodeId}.
   * @return {@code true} if the operation {@code nodeId} belongs to should be handled this filter's
   *     {@link AddressSpace}.
   */
  protected abstract boolean filterNode(NodeId nodeId);

  /**
   * Return {@code true} if the operation on the MonitoredItem for {@code nodeId} should be handled
   * by this filter's {@link AddressSpace}.
   *
   * @param nodeId the {@link NodeId} of {@link MonitoredItem} being operated on.
   * @return {@code true} if the operation on the MonitoredItem for {@code nodeId} should be handled
   *     by this filter's {@link AddressSpace}.
   */
  protected abstract boolean filterMonitoredItem(NodeId nodeId);

  // region ViewServices

  @Override
  public boolean filterBrowse(OpcUaServer server, NodeId nodeId) {
    return filterNode(nodeId);
  }

  @Override
  public boolean filterRegisterNode(OpcUaServer server, NodeId nodeId) {
    return filterNode(nodeId);
  }

  @Override
  public boolean filterUnregisterNode(OpcUaServer server, NodeId nodeId) {
    return filterNode(nodeId);
  }

  // endregion

  // region AttributeServices

  @Override
  public boolean filterRead(OpcUaServer server, ReadValueId readValueId) {
    return filterNode(readValueId.getNodeId());
  }

  @Override
  public boolean filterWrite(OpcUaServer server, WriteValue writeValue) {
    return filterNode(writeValue.getNodeId());
  }

  @Override
  public boolean filterHistoryRead(OpcUaServer server, HistoryReadValueId historyReadValueId) {
    return filterNode(historyReadValueId.getNodeId());
  }

  @Override
  public boolean filterHistoryUpdate(
      OpcUaServer server, HistoryUpdateDetails historyUpdateDetails) {

    if (historyUpdateDetails instanceof DeleteAtTimeDetails details) {
      return filterNode(details.getNodeId());
    } else if (historyUpdateDetails instanceof DeleteEventDetails details) {
      return filterNode(details.getNodeId());
    } else if (historyUpdateDetails instanceof DeleteRawModifiedDetails details) {
      return filterNode(details.getNodeId());
    } else if (historyUpdateDetails instanceof UpdateDataDetails details) {
      return filterNode(details.getNodeId());
    } else if (historyUpdateDetails instanceof UpdateEventDetails details) {
      return filterNode(details.getNodeId());
    } else if (historyUpdateDetails instanceof UpdateStructureDataDetails details) {
      return filterNode(details.getNodeId());
    } else {
      throw new IllegalArgumentException(
          "unexpected HistoryUpdateDetails: " + historyUpdateDetails);
    }
  }

  // endregion

  // region MethodServices

  @Override
  public boolean filterCall(OpcUaServer server, CallMethodRequest callMethodRequest) {
    return filterNode(callMethodRequest.getObjectId());
  }

  // endregion

  // region MonitoredItemServices

  @Override
  public boolean filterOnCreateDataItem(OpcUaServer server, ReadValueId readValueId) {
    return filterMonitoredItem(readValueId.getNodeId());
  }

  @Override
  public boolean filterOnModifyDataItem(OpcUaServer server, ReadValueId readValueId) {
    return filterMonitoredItem(readValueId.getNodeId());
  }

  @Override
  public boolean filterOnCreateEventItem(OpcUaServer server, ReadValueId readValueId) {
    return filterMonitoredItem(readValueId.getNodeId());
  }

  @Override
  public boolean filterOnModifyEventItem(OpcUaServer server, ReadValueId readValueId) {
    return filterMonitoredItem(readValueId.getNodeId());
  }

  @Override
  public boolean filterOnDataItemsCreated(OpcUaServer server, ReadValueId readValueId) {
    return filterMonitoredItem(readValueId.getNodeId());
  }

  @Override
  public boolean filterOnDataItemsModified(OpcUaServer server, ReadValueId readValueId) {
    return filterMonitoredItem(readValueId.getNodeId());
  }

  @Override
  public boolean filterOnDataItemsDeleted(OpcUaServer server, ReadValueId readValueId) {
    return filterMonitoredItem(readValueId.getNodeId());
  }

  @Override
  public boolean filterOnEventItemsCreated(OpcUaServer server, ReadValueId readValueId) {
    return filterMonitoredItem(readValueId.getNodeId());
  }

  @Override
  public boolean filterOnEventItemsModified(OpcUaServer server, ReadValueId readValueId) {
    return filterMonitoredItem(readValueId.getNodeId());
  }

  @Override
  public boolean filterOnEventItemsDeleted(OpcUaServer server, ReadValueId readValueId) {
    return filterMonitoredItem(readValueId.getNodeId());
  }

  @Override
  public boolean filterOnMonitoringModeChanged(OpcUaServer server, ReadValueId readValueId) {
    return filterMonitoredItem(readValueId.getNodeId());
  }

  // endregion

  // region NodeManagementServices

  @Override
  public boolean filterAddNodes(OpcUaServer server, AddNodesItem addNodesItem) {
    NamespaceTable namespaceTable = server.getNamespaceTable();

    ExpandedNodeId requestedNewNodeId = addNodesItem.getRequestedNewNodeId();

    if (requestedNewNodeId.isNotNull()) {
      return requestedNewNodeId.toNodeId(namespaceTable).map(this::filterNode).orElse(false);
    } else {
      return addNodesItem
          .getParentNodeId()
          .toNodeId(namespaceTable)
          .map(this::filterNode)
          .orElse(false);
    }
  }

  @Override
  public boolean filterDeleteNodes(OpcUaServer server, DeleteNodesItem deleteNodesItem) {
    return filterNode(deleteNodesItem.getNodeId());
  }

  @Override
  public boolean filterAddReferences(OpcUaServer server, AddReferencesItem addReferencesItem) {
    return filterNode(addReferencesItem.getSourceNodeId());
  }

  @Override
  public boolean filterDeleteReferences(
      OpcUaServer server, DeleteReferencesItem deleteReferencesItem) {
    return filterNode(deleteReferencesItem.getSourceNodeId());
  }

  // endregion

}
