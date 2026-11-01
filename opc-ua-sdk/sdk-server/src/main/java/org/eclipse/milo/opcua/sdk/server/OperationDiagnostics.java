/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.NullMarked;

/**
 * Collects the {@link Diagnostic}s an {@link AddressSpace} reports for the operations of one
 * service request.
 *
 * <p>Each operation context, such as {@link AddressSpace.WriteContext}, carries one. Read, Write,
 * HistoryRead, HistoryUpdate, Call, AddNodes, DeleteNodes, AddReferences and DeleteReferences
 * return what is reported here in the response's per-operation {@code diagnosticInfos}, when the
 * client asked for operation-level diagnostics and the server's {@link DiagnosticsPolicy} allows
 * it. The contexts of the other services carry {@link #none()}, whose {@link #isRequested()}
 * returns {@code false}.
 *
 * <p>Most address spaces never use this directly. An address space built on {@link
 * ManagedAddressSpace} reports by throwing a {@link UaException} that carries a diagnostic from a
 * node, attribute filter, value delegate, or Method handler; the SDK records it here. An address
 * space that builds its own results reports through its context:
 *
 * <pre>{@code
 * for (WriteValue writeValue : writeValues) {
 *   DeviceResult outcome = device.write(writeValue);
 *
 *   results.add(outcome.statusCode());
 *
 *   if (outcome.isBad()) {
 *     context.getOperationDiagnostics().report(writeValue, outcome.toDiagnostic());
 *   }
 * }
 * }</pre>
 *
 * <p>Operations are identified by the request item instance the address space received, not by
 * equality or position. Identity survives regrouping by composites, which pass the parent's {@code
 * OperationDiagnostics} to each child, and keeps two equal operations in one request apart. Report
 * the same instance the address space was given; a diagnostic reported for any other object is
 * ignored. A later report for an operation replaces an earlier one.
 *
 * <p>The SDK's implementation is safe for concurrent {@code report} calls for distinct operations.
 *
 * @param <T> the type of the request items, such as {@code WriteValue}.
 */
@NullMarked
public interface OperationDiagnostics<T> {

  /**
   * Check whether the client asked for any operation-level diagnostics.
   *
   * <p>When this returns {@code false}, {@link #report} does nothing. Check it first if building a
   * diagnostic is expensive.
   *
   * @return {@code true} if reported diagnostics can reach the client.
   */
  boolean isRequested();

  /**
   * Attach a diagnostic to the operation for {@code operation}.
   *
   * @param operation the request item instance this address space received.
   * @param diagnostic the diagnostic that explains the operation's result.
   */
  void report(T operation, Diagnostic diagnostic);

  /**
   * Get an {@link OperationDiagnostics} that collects nothing.
   *
   * <p>Use it for contexts that do not belong to a client request, or for services whose responses
   * carry no operation diagnostics. {@link #isRequested()} returns {@code false}.
   *
   * @param <T> the type of the request items.
   * @return an {@link OperationDiagnostics} that collects nothing.
   */
  @SuppressWarnings("unchecked")
  static <T> OperationDiagnostics<T> none() {
    return (OperationDiagnostics<T>) NoOperationDiagnostics.OPERATIONS;
  }
}
