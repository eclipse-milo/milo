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
import org.jspecify.annotations.Nullable;

public final class UascResponse {

  private final long requestId;
  private final UaResponseMessageType responseMessage;
  private final UaException exception;

  private UascResponse(
      long requestId,
      @Nullable UaResponseMessageType responseMessage,
      @Nullable UaException exception) {

    this.requestId = requestId;
    this.responseMessage = responseMessage;
    this.exception = exception;

    // response can't both succeed and fail, one must be null
    assert responseMessage == null || exception == null;
  }

  public long getRequestId() {
    return requestId;
  }

  public @Nullable UaResponseMessageType getResponseMessage() {
    return responseMessage;
  }

  public @Nullable UaException getException() {
    return exception;
  }

  public boolean isSuccess() {
    return responseMessage != null;
  }

  public boolean isFailure() {
    return exception != null;
  }

  public static UascResponse success(long requestId, UaResponseMessageType responseMessage) {
    return new UascResponse(requestId, responseMessage, null);
  }

  public static UascResponse failure(long requestId, UaException exception) {
    return new UascResponse(requestId, null, exception);
  }
}
