/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import java.util.Objects;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.servicesets.MonitoredItemServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultMonitoredItemServiceSet;
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

/**
 * A {@link MonitoredItemServiceSet} that forwards every operation to a delegate.
 *
 * <p>Mirrors {@link DelegatingSubscriptionServiceSet}. Subclass and override individual {@code on*}
 * methods to observe the MonitoredItem requests a client sends, or to fail them deterministically,
 * while leaving the remainder of the real server behavior intact. Register with {@code
 * server.addServiceSet(path, serviceSet)}; a later registration replaces the default handlers for
 * that path.
 */
public class DelegatingMonitoredItemServiceSet implements MonitoredItemServiceSet {

  private final MonitoredItemServiceSet delegate;

  public DelegatingMonitoredItemServiceSet(OpcUaServer server) {
    this(new DefaultMonitoredItemServiceSet(server));
  }

  public DelegatingMonitoredItemServiceSet(MonitoredItemServiceSet delegate) {
    this.delegate = Objects.requireNonNull(delegate, "delegate");
  }

  @Override
  public CreateMonitoredItemsResponse onCreateMonitoredItems(
      ServiceRequestContext context, CreateMonitoredItemsRequest request) throws UaException {
    return delegate.onCreateMonitoredItems(context, request);
  }

  @Override
  public ModifyMonitoredItemsResponse onModifyMonitoredItems(
      ServiceRequestContext context, ModifyMonitoredItemsRequest request) throws UaException {
    return delegate.onModifyMonitoredItems(context, request);
  }

  @Override
  public DeleteMonitoredItemsResponse onDeleteMonitoredItems(
      ServiceRequestContext context, DeleteMonitoredItemsRequest request) throws UaException {
    return delegate.onDeleteMonitoredItems(context, request);
  }

  @Override
  public SetMonitoringModeResponse onSetMonitoringMode(
      ServiceRequestContext context, SetMonitoringModeRequest request) throws UaException {
    return delegate.onSetMonitoringMode(context, request);
  }

  @Override
  public SetTriggeringResponse onSetTriggering(
      ServiceRequestContext context, SetTriggeringRequest request) throws UaException {
    return delegate.onSetTriggering(context, request);
  }
}
