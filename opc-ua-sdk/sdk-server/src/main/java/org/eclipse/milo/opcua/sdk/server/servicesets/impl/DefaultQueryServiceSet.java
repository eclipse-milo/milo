/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import org.eclipse.milo.opcua.sdk.server.servicesets.QueryServiceSet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryFirstRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryFirstResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryNextRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryNextResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

public class DefaultQueryServiceSet implements QueryServiceSet {

  @Override
  public QueryFirstResponse onQueryFirst(ServiceRequestContext context, QueryFirstRequest request)
      throws UaException {

    throw new UaException(StatusCodes.Bad_ServiceUnsupported);
  }

  @Override
  public QueryNextResponse onQueryNext(ServiceRequestContext context, QueryNextRequest request)
      throws UaException {

    throw new UaException(StatusCodes.Bad_ServiceUnsupported);
  }
}
