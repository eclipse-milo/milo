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

import static org.eclipse.milo.opcua.sdk.server.servicesets.AbstractServiceSet.createResponseHeader;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.SessionManager;
import org.eclipse.milo.opcua.sdk.server.diagnostics.ServerDiagnosticsSummary;
import org.eclipse.milo.opcua.sdk.server.servicesets.SessionServiceSet;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CancelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CancelResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

public class DefaultSessionServiceSet implements SessionServiceSet {

  private final OpcUaServer server;

  public DefaultSessionServiceSet(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public CreateSessionResponse onCreateSession(
      ServiceRequestContext context, CreateSessionRequest request) throws UaException {

    ServerDiagnosticsSummary serverDiagnosticsSummary = server.getDiagnosticsSummary();

    SessionManager sessionManager = server.getSessionManager();

    try {
      CreateSessionResponse response = sessionManager.createSession(context, request);

      serverDiagnosticsSummary.getCumulatedSessionCount().increment();

      return response;
    } catch (UaException e) {
      serverDiagnosticsSummary.getRejectedSessionCount().increment();

      if (e.getStatusCode().isSecurityError()) {
        serverDiagnosticsSummary.getSecurityRejectedSessionCount().increment();
      }

      throw e;
    }
  }

  @Override
  public ActivateSessionResponse onActivateSession(
      ServiceRequestContext context, ActivateSessionRequest request) throws UaException {

    SessionManager sessionManager = server.getSessionManager();

    try {
      return sessionManager.activateSession(context, request);
    } catch (UaException e) {
      ServerDiagnosticsSummary serverDiagnosticsSummary = server.getDiagnosticsSummary();

      serverDiagnosticsSummary.getRejectedSessionCount().increment();

      if (e.getStatusCode().isSecurityError()) {
        serverDiagnosticsSummary.getSecurityRejectedSessionCount().increment();
      }

      throw e;
    }
  }

  @Override
  public CloseSessionResponse onCloseSession(
      ServiceRequestContext context, CloseSessionRequest request) throws UaException {

    SessionManager sessionManager = server.getSessionManager();

    return sessionManager.closeSession(request, context);
  }

  @Override
  public CancelResponse onCancel(ServiceRequestContext context, CancelRequest request) {
    return new CancelResponse(createResponseHeader(request), UInteger.MIN);
  }
}
