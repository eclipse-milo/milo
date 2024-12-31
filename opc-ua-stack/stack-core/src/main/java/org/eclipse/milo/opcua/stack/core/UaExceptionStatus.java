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

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

public interface UaExceptionStatus {

  /**
   * @return the message associated with the exception.
   */
  String getMessage();

  /**
   * @return the {@link StatusCode} associated with the exception.
   */
  StatusCode getStatusCode();

  /**
   * If {@code ex} is a {@link UaExceptionStatus}, or if a {@link UaExceptionStatus} can be found by
   * walking up the exception cause chain, return it.
   *
   * @param ex the {@link Throwable} to extract from.
   * @return a {@link UaExceptionStatus} if one was present in the exception chain.
   */
  static Optional<UaExceptionStatus> extract(Throwable ex) {
    if (ex instanceof UaExceptionStatus) {
      return Optional.of((UaExceptionStatus) ex);
    } else {
      Throwable cause = ex.getCause();
      return cause != null ? extract(cause) : Optional.empty();
    }
  }
}
