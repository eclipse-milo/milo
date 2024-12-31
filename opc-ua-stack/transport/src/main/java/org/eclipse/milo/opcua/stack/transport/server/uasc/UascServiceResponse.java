/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.uasc;

import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.transport.server.ServiceResponse;

public class UascServiceResponse extends ServiceResponse {

  private final long requestId;

  public UascServiceResponse(UaResponseMessageType responseMessage, long requestId) {
    super(responseMessage);

    this.requestId = requestId;
  }

  public long getRequestId() {
    return requestId;
  }
}
