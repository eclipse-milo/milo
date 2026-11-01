/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * The context of one group of operations from a service request, handed to an {@link AddressSpace}.
 *
 * <p>It identifies the server and the calling {@link Session}, carries the request header's audit
 * entry id, timeout hint and additional header, and collects the {@link OperationDiagnostics} the
 * address space reports for the operations it was given.
 *
 * @param <T> the type of the request items, such as {@code ReadValueId}.
 */
public class ServiceOperationContext<T> implements AccessContext {

  private final OpcUaServer server;
  private final Session session;
  private final OperationDiagnostics<T> operationDiagnostics;

  private final String auditEntryId;

  private final UInteger timeoutHint;

  private final ExtensionObject additionalHeader;

  /**
   * Create a context that does not belong to a client request, with {@link
   * OperationDiagnostics#none()}.
   *
   * @param server the {@link OpcUaServer}.
   * @param session the calling {@link Session}, or {@code null}.
   */
  public ServiceOperationContext(OpcUaServer server, @Nullable Session session) {
    this(server, session, OperationDiagnostics.none(), "", uint(0), null);
  }

  /**
   * Create a context for operations from a service request.
   *
   * @param server the {@link OpcUaServer}.
   * @param session the calling {@link Session}, or {@code null}.
   * @param operationDiagnostics the collector for the request's operation diagnostics. A composite
   *     passes its own context's collector to every child context.
   * @param auditEntryId the request header's audit entry id, or {@code null}.
   * @param timeoutHint the request header's timeout hint.
   * @param additionalHeader the request header's additional header.
   */
  public ServiceOperationContext(
      OpcUaServer server,
      @Nullable Session session,
      OperationDiagnostics<T> operationDiagnostics,
      @Nullable String auditEntryId,
      UInteger timeoutHint,
      ExtensionObject additionalHeader) {

    this.server = server;
    this.session = session;
    this.operationDiagnostics = operationDiagnostics;
    this.auditEntryId = auditEntryId;
    this.timeoutHint = timeoutHint;
    this.additionalHeader = additionalHeader;
  }

  public OpcUaServer getServer() {
    return server;
  }

  /** {@inheritDoc} */
  @Override
  public Optional<Session> getSession() {
    return Optional.ofNullable(session);
  }

  /**
   * Get the collector for the diagnostics of the operations in this context.
   *
   * @return the {@link OperationDiagnostics} for this context.
   * @see OperationDiagnostics
   */
  public OperationDiagnostics<T> getOperationDiagnostics() {
    return operationDiagnostics;
  }

  public @Nullable String getAuditEntryId() {
    return auditEntryId;
  }

  public UInteger getTimeoutHint() {
    return timeoutHint;
  }

  public ExtensionObject getAdditionalHeader() {
    return additionalHeader;
  }
}
