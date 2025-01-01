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

import java.util.List;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.AddNodesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.AddReferencesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.BrowseContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.CallContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.DeleteNodesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.DeleteReferencesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.HistoryReadContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.HistoryUpdateContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.RegisterNodesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.UnregisterNodesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.WriteContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;

public interface AddressSpaceFilter {

  // region ViewServices

  /**
   * Return {@code true} if the browse operation for {@code nodeId} should be handled by the {@link
   * AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param nodeId the {@link NodeId} from the browse operation.
   * @return {@code true} if the browse operation for {@code nodeId} should be handled the the
   *     {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#browse(BrowseContext, ViewDescription, List)
   */
  boolean filterBrowse(OpcUaServer server, NodeId nodeId);

  /**
   * Return {@code true} if the register node operation for {@code nodeId} should be handled by the
   * {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param nodeId the {@link NodeId} to register.
   * @return {@code true} if the register node operation for {@code nodeId} should be handled by the
   *     {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#registerNodes(RegisterNodesContext, List)
   */
  boolean filterRegisterNode(OpcUaServer server, NodeId nodeId);

  /**
   * Return {@code true} if the unregister node operation for {@code nodeId} should be handled by
   * the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param nodeId the {@link NodeId} to unregister.
   * @return {@code true} if the unregister node operation for {@code nodeId} should be handled by
   *     the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#unregisterNodes(UnregisterNodesContext, List)
   */
  boolean filterUnregisterNode(OpcUaServer server, NodeId nodeId);

  // endregion

  // region AttributeServices

  /**
   * Return {@code true} if the read operation for {@code readValueId} should be handled by the
   * {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the read operation.
   * @return {@code true} if the read operation for {@code readValueId} should be handled by the
   *     {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#read(ReadContext, Double, TimestampsToReturn, List)
   */
  boolean filterRead(OpcUaServer server, ReadValueId readValueId);

  /**
   * Return {@code true} if the write operation for {@code writeValue} should be handled by the
   * {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param writeValue the {@link WriteValue} from the write operation.
   * @return {@code true} if the write operation for {@code writeValue} should be handled by the
   *     {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#write(WriteContext, List)
   */
  boolean filterWrite(OpcUaServer server, WriteValue writeValue);

  /**
   * Return {@code true} if the history read operation for {@code historyReadValueId} should be
   * handled by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param historyReadValueId the {@link HistoryReadValueId} from the history read operation.
   * @return {@code true} if the history read operation for {@code historyReadValueId} should be
   *     handled by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#historyRead(HistoryReadContext, HistoryReadDetails, TimestampsToReturn, List)
   */
  boolean filterHistoryRead(OpcUaServer server, HistoryReadValueId historyReadValueId);

  /**
   * Return {@code true} if the history update operation for {@code historyUpdateDetails} should be
   * handled by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param historyUpdateDetails the {@link HistoryUpdateDetails} from the history update operation.
   * @return {@code true} if the history update operation for {@code historyUpdateDetails} should be
   *     handled by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#historyUpdate(HistoryUpdateContext, List)
   */
  boolean filterHistoryUpdate(OpcUaServer server, HistoryUpdateDetails historyUpdateDetails);

  // endregion

  // region MethodServices

  /**
   * Return {@code true} if the call method operation for {@code callMethodRequest} should be
   * handled by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param callMethodRequest the {@link CallMethodRequest} from the call method operation.
   * @return {@code true} if the call method operation for {@code callMethodRequest} should be
   *     handled by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#call(CallContext, List)
   */
  boolean filterCall(OpcUaServer server, CallMethodRequest callMethodRequest);

  // endregion

  // region MonitoredItemServices

  /**
   * Return {@code true} if the monitored item operation for {@code readValueId} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the monitored item operation.
   * @return {@code true} if the monitored item operation for {@code readValueId} should be handled
   *     by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#onCreateDataItem(ReadValueId, Double, UInteger)
   */
  boolean filterOnCreateDataItem(OpcUaServer server, ReadValueId readValueId);

  /**
   * Return {@code true} if the monitored item operation for {@code readValueId} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the monitored item operation.
   * @return {@code true} if the monitored item operation for {@code readValueId} should be handled
   *     by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#onModifyDataItem(ReadValueId, Double, UInteger)
   */
  boolean filterOnModifyDataItem(OpcUaServer server, ReadValueId readValueId);

  /**
   * Return {@code true} if the monitored item operation for {@code readValueId} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the monitored item operation.
   * @return {@code true} if the monitored item operation for {@code readValueId} should be handled
   *     by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#onCreateEventItem(ReadValueId, UInteger)
   */
  boolean filterOnCreateEventItem(OpcUaServer server, ReadValueId readValueId);

  /**
   * Return {@code true} if the monitored item operation for {@code readValueId} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the monitored item operation.
   * @return {@code true} if the monitored item operation for {@code readValueId} should be handled
   *     by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#onModifyEventItem(ReadValueId, UInteger)
   */
  boolean filterOnModifyEventItem(OpcUaServer server, ReadValueId readValueId);

  /**
   * Return {@code true} if the monitored item operation for {@code readValueId} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the monitored item operation.
   * @return {@code true} if the monitored item operation for {@code readValueId} should be handled
   *     by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#onDataItemsCreated(List)
   */
  boolean filterOnDataItemsCreated(OpcUaServer server, ReadValueId readValueId);

  /**
   * Return {@code true} if the monitored item operation for {@code readValueId} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the monitored item operation.
   * @return {@code true} if the monitored item operation for {@code readValueId} should be handled
   *     by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#onDataItemsModified(List)
   */
  boolean filterOnDataItemsModified(OpcUaServer server, ReadValueId readValueId);

  /**
   * Return {@code true} if the monitored item operation for {@code readValueId} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the monitored item operation.
   * @return {@code true} if the monitored item operation for {@code readValueId} should be handled
   *     by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#onDataItemsDeleted(List)
   */
  boolean filterOnDataItemsDeleted(OpcUaServer server, ReadValueId readValueId);

  /**
   * Return {@code true} if the monitored item operation for {@code readValueId} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the monitored item operation.
   * @return {@code true} if the monitored item operation for {@code readValueId} should be handled
   *     by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#onEventItemsCreated(List)
   */
  boolean filterOnEventItemsCreated(OpcUaServer server, ReadValueId readValueId);

  /**
   * Return {@code true} if the monitored item operation for {@code readValueId} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the monitored item operation.
   * @return {@code true} if the monitored item operation for {@code readValueId} should be handled
   *     by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#onEventItemsModified(List)
   */
  boolean filterOnEventItemsModified(OpcUaServer server, ReadValueId readValueId);

  /**
   * Return {@code true} if the monitored item operation for {@code readValueId} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the monitored item operation.
   * @return {@code true} if the monitored item operation for {@code readValueId} should be handled
   *     by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#onEventItemsDeleted(List)
   */
  boolean filterOnEventItemsDeleted(OpcUaServer server, ReadValueId readValueId);

  /**
   * Return {@code true} if the monitored item operation for {@code readValueId} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param readValueId the {@link ReadValueId} from the monitored item operation.
   * @return {@code true} if the monitored item operation for {@code readValueId} should be handled
   *     by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#onMonitoringModeChanged(List)
   */
  boolean filterOnMonitoringModeChanged(OpcUaServer server, ReadValueId readValueId);

  // endregion

  // region NodeManagementServices

  /**
   * Return {@code true} if the add nodes operation for {@code addNodesItem} should be handled by
   * the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param addNodesItem the {@link AddNodesItem} from the add nodes operation.
   * @return {@code true} if the add nodes operation for {@code addNodesItem} should be handled by
   *     the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#addNodes(AddNodesContext, List)
   */
  boolean filterAddNodes(OpcUaServer server, AddNodesItem addNodesItem);

  /**
   * Return {@code true} if the delete nodes operation for {@code deleteNodesItem} should be handled
   * by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param deleteNodesItem the {@link DeleteNodesItem} from the delete nodes operation.
   * @return Return {@code true} if the delete nodes operation for {@code deleteNodesItem} should be
   *     handled by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#deleteNodes(DeleteNodesContext, List)
   */
  boolean filterDeleteNodes(OpcUaServer server, DeleteNodesItem deleteNodesItem);

  /**
   * Return {@code true} if the add references operation for {@code addReferencesItem} should be
   * handled by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param addReferencesItem the {@link AddReferencesItem} from the add references operation.
   * @return {@code true} if the add references operation for {@code addReferencesItem} should be
   *     handled by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#addReferences(AddReferencesContext, List)
   */
  boolean filterAddReferences(OpcUaServer server, AddReferencesItem addReferencesItem);

  /**
   * Return {@code true} if the delete references operation for {@code deleteReferencesITem} should
   * be handled by the {@link AddressSpace} this filter belongs to.
   *
   * @param server the {@link OpcUaServer}.
   * @param deleteReferencesItem the {@link DeleteReferencesItem} from the delete references
   *     operation.
   * @return {@code true} if the delete references operation for {@code deleteReferencesITem} should
   *     be handled by the {@link AddressSpace} this filter belongs to.
   * @see AddressSpace#deleteReferences(DeleteReferencesContext, List)
   */
  boolean filterDeleteReferences(OpcUaServer server, DeleteReferencesItem deleteReferencesItem);

  // endregion

}
