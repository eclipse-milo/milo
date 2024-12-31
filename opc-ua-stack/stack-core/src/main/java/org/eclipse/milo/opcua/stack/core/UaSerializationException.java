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

public class UaSerializationException extends UaRuntimeException {

  public UaSerializationException(long statusCode, Throwable cause) {
    super(statusCode, cause);
  }

  public UaSerializationException(long statusCode, String message) {
    super(statusCode, message);
  }
}
