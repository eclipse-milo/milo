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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.jspecify.annotations.Nullable;

/**
 * Thrown when a server answers a request with a {@link ServiceFault}.
 *
 * <p>{@link #getDiagnostic()} returns the fault's service-level diagnostic, resolved against the
 * fault's string table, when the server sent one. A server sends one only when the request asked
 * for service-level diagnostics. The message includes the diagnostic's symbolic id and text, so a
 * logged fault shows the server's explanation.
 */
public class UaServiceFaultException extends UaException {

  private final ServiceFault serviceFault;

  public UaServiceFaultException(ServiceFault serviceFault) {
    this(serviceFault, null);
  }

  public UaServiceFaultException(ServiceFault serviceFault, @Nullable String message) {
    super(
        serviceFault.getResponseHeader().getServiceResult(),
        message,
        ResponseDiagnostics.of(serviceFault.getResponseHeader()).service().orElse(null),
        null);

    this.serviceFault = serviceFault;
  }

  public ServiceFault getServiceFault() {
    return serviceFault;
  }

  @Override
  public String getMessage() {
    return super.getMessage() + describe(getDiagnostic());
  }

  @Override
  public String toString() {
    return super.toString() + describe(getDiagnostic());
  }

  /** The symbolic id and text of {@code diagnostic}, to append to a message, or "". */
  private static String describe(Optional<Diagnostic> diagnostic) {
    if (diagnostic.isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();

    String symbolicId = diagnostic.get().symbolicId();
    if (symbolicId != null) {
      sb.append(", symbolicId=").append(symbolicId);

      String namespaceUri = diagnostic.get().namespaceUri();
      if (namespaceUri != null) {
        sb.append(" (").append(namespaceUri).append(")");
      }
    }

    LocalizedText localizedText = diagnostic.get().localizedText();
    if (localizedText != null && localizedText.text() != null) {
      sb.append(", text=").append(localizedText.text());
    }

    return sb.toString();
  }
}
