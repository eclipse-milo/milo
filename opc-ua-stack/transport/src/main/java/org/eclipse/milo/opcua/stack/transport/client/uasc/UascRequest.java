/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.uasc;

import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;

public final class UascRequest {

  private final long requestId;
  private final UaRequestMessageType requestMessage;

  public UascRequest(long requestId, UaRequestMessageType requestMessage) {
    this.requestId = requestId;
    this.requestMessage = requestMessage;
  }

  public long getRequestId() {
    return requestId;
  }

  public UaRequestMessageType getRequestMessage() {
    return requestMessage;
  }
}
