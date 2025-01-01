/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server;

import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;

public class ServiceResponse {

  private final UaResponseMessageType responseMessage;

  public ServiceResponse(UaResponseMessageType responseMessage) {
    this.responseMessage = responseMessage;
  }

  public UaResponseMessageType getResponseMessage() {
    return responseMessage;
  }
}
