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

import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;

public interface UascResponseHandler {

  // response successfully received and decoded
  void handleResponse(long requestId, UaResponseMessageType responseMessage);

  // failed while sending request
  void handleSendFailure(long requestId, UaException exception);

  // failed while decoding response, aborted, decode exception, ServiceFault
  void handleReceiveFailure(long requestId, UaException exception);

  void handleChannelError(UaException exception);

  // channel inactive, cancel pending requests?
  void handleChannelInactive();
}
