/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

public class InvalidArgumentException extends UaException {

  private final StatusCode[] inputArgumentResults;
  private final DiagnosticInfo[] inputArgumentDiagnosticInfos;

  public InvalidArgumentException(StatusCode[] inputArgumentResults) {
    this(inputArgumentResults, new DiagnosticInfo[0]);
  }

  public InvalidArgumentException(
      StatusCode[] inputArgumentResults, DiagnosticInfo[] inputArgumentDiagnosticInfos) {

    super(StatusCodes.Bad_InvalidArgument, "one or more of the provided arguments is invalid");

    this.inputArgumentResults = inputArgumentResults;
    this.inputArgumentDiagnosticInfos = inputArgumentDiagnosticInfos;
  }

  /**
   * Get the corresponding {@link StatusCode}s for each input argument value.
   *
   * @return the corresponding {@link StatusCode}s for each input argument value.
   */
  public StatusCode[] getInputArgumentResults() {
    return inputArgumentResults;
  }

  /**
   * Get the corresponding {@link DiagnosticInfo}s for each input argument value, if supported.
   *
   * @return the corresponding {@link DiagnosticInfo}s for each input argument value, if supported.
   */
  public DiagnosticInfo[] getInputArgumentDiagnosticInfos() {
    return inputArgumentDiagnosticInfos;
  }
}
