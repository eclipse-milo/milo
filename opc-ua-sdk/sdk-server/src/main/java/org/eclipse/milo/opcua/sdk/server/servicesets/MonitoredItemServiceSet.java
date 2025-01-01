/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets;

import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetTriggeringRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetTriggeringResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

public interface MonitoredItemServiceSet {

  CreateMonitoredItemsResponse onCreateMonitoredItems(
      ServiceRequestContext context, CreateMonitoredItemsRequest request) throws UaException;

  ModifyMonitoredItemsResponse onModifyMonitoredItems(
      ServiceRequestContext context, ModifyMonitoredItemsRequest request) throws UaException;

  DeleteMonitoredItemsResponse onDeleteMonitoredItems(
      ServiceRequestContext context, DeleteMonitoredItemsRequest request) throws UaException;

  SetMonitoringModeResponse onSetMonitoringMode(
      ServiceRequestContext context, SetMonitoringModeRequest request) throws UaException;

  SetTriggeringResponse onSetTriggering(ServiceRequestContext context, SetTriggeringRequest request)
      throws UaException;
}
