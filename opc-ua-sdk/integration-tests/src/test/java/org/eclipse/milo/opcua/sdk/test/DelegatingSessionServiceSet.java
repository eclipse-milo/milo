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
import org.eclipse.milo.opcua.sdk.server.servicesets.SessionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultSessionServiceSet;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CancelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CancelResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

public class DelegatingSessionServiceSet implements SessionServiceSet {

  private final SessionServiceSet delegate;

  public DelegatingSessionServiceSet(OpcUaServer server) {
    this(new DefaultSessionServiceSet(server));
  }

  public DelegatingSessionServiceSet(SessionServiceSet delegate) {
    this.delegate = Objects.requireNonNull(delegate, "delegate");
  }

  @Override
  public CreateSessionResponse onCreateSession(
      ServiceRequestContext context, CreateSessionRequest request) throws UaException {
    return delegate.onCreateSession(context, request);
  }

  @Override
  public ActivateSessionResponse onActivateSession(
      ServiceRequestContext context, ActivateSessionRequest request) throws UaException {
    return delegate.onActivateSession(context, request);
  }

  @Override
  public CloseSessionResponse onCloseSession(
      ServiceRequestContext context, CloseSessionRequest request) throws UaException {
    return delegate.onCloseSession(context, request);
  }

  @Override
  public CancelResponse onCancel(ServiceRequestContext context, CancelRequest request)
      throws UaException {
    return delegate.onCancel(context, request);
  }
}
