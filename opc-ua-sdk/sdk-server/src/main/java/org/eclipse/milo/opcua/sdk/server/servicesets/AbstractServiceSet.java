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

import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;

public abstract class AbstractServiceSet {

  public static ResponseHeader createResponseHeader(UaRequestMessageType request) {
    return createResponseHeader(request, StatusCode.GOOD);
  }

  public static ResponseHeader createResponseHeader(UaRequestMessageType request, long statusCode) {
    return createResponseHeader(request, new StatusCode(statusCode));
  }

  public static ResponseHeader createResponseHeader(
      UaRequestMessageType request, StatusCode serviceResult) {
    return new ResponseHeader(
        DateTime.now(),
        request.getRequestHeader().getRequestHandle(),
        serviceResult,
        null,
        null,
        null);
  }

  public static ResponseHeader createResponseHeader(
      UaRequestMessageType request, DiagnosticInfo[] diagnosticInfos) {
    // TODO use DiagnosticInfo to create crazy header...
    return createResponseHeader(request, StatusCode.GOOD);
  }
}
