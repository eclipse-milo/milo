/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.servicesets.MonitoredItemServiceSet;
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

public class DefaultMonitoredItemServiceSet implements MonitoredItemServiceSet {

  private final OpcUaServer server;

  public DefaultMonitoredItemServiceSet(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public CreateMonitoredItemsResponse onCreateMonitoredItems(
      ServiceRequestContext context, CreateMonitoredItemsRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return session.getSubscriptionManager().createMonitoredItems(context, request);
    } catch (Throwable t) {
      session.getSessionDiagnostics().getCreateMonitoredItemsCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw UaException.extract(t).orElse(new UaException(t));
    } finally {
      session.getSessionDiagnostics().getCreateMonitoredItemsCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public ModifyMonitoredItemsResponse onModifyMonitoredItems(
      ServiceRequestContext context, ModifyMonitoredItemsRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return session.getSubscriptionManager().modifyMonitoredItems(context, request);
    } catch (Throwable t) {
      session.getSessionDiagnostics().getModifyMonitoredItemsCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw UaException.extract(t).orElse(new UaException(t));
    } finally {
      session.getSessionDiagnostics().getModifyMonitoredItemsCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public DeleteMonitoredItemsResponse onDeleteMonitoredItems(
      ServiceRequestContext context, DeleteMonitoredItemsRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return session.getSubscriptionManager().deleteMonitoredItems(context, request).get();
    } catch (Throwable t) {
      session.getSessionDiagnostics().getDeleteMonitoredItemsCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw UaException.extract(t).orElse(new UaException(t));
    } finally {
      session.getSessionDiagnostics().getDeleteMonitoredItemsCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public SetMonitoringModeResponse onSetMonitoringMode(
      ServiceRequestContext context, SetMonitoringModeRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return session.getSubscriptionManager().setMonitoringMode(context, request).get();
    } catch (Throwable t) {
      session.getSessionDiagnostics().getSetMonitoringModeCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw UaException.extract(t).orElse(new UaException(t));
    } finally {
      session.getSessionDiagnostics().getSetMonitoringModeCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public SetTriggeringResponse onSetTriggering(
      ServiceRequestContext context, SetTriggeringRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return session.getSubscriptionManager().setTriggering(context, request).get();
    } catch (Throwable t) {
      session.getSessionDiagnostics().getSetTriggeringCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw UaException.extract(t).orElse(new UaException(t));
    } finally {
      session.getSessionDiagnostics().getSetTriggeringCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }
}
