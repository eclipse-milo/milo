/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

public class UaRuntimeException extends RuntimeException implements UaExceptionStatus {

  private final StatusCode statusCode;

  public UaRuntimeException(Throwable cause) {
    super(cause);

    this.statusCode = new StatusCode(StatusCodes.Bad_InternalError);
  }

  public UaRuntimeException(long statusCode) {
    this.statusCode = new StatusCode(statusCode);
  }

  public UaRuntimeException(long statusCode, String message) {
    super(message);

    this.statusCode = new StatusCode(statusCode);
  }

  public UaRuntimeException(long statusCode, Throwable cause) {
    super(cause);

    this.statusCode = new StatusCode(statusCode);
  }

  @Override
  public StatusCode getStatusCode() {
    return statusCode;
  }
}
