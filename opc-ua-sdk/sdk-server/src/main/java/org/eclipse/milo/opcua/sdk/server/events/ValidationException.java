/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events;

import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/** Thrown when an element or operand of an event filter is invalid. */
public class ValidationException extends UaException {

  public ValidationException(long statusCode) {
    super(statusCode);
  }

  /**
   * @param statusCode the status that explains why the filter is invalid.
   * @param diagnostic the diagnostic that explains {@code statusCode}.
   */
  public ValidationException(long statusCode, Diagnostic diagnostic) {
    super(new StatusCode(statusCode), null, diagnostic);
  }
}
