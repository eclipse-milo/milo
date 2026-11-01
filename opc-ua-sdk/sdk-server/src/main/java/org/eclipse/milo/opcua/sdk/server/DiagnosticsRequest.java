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

import static java.util.Objects.requireNonNull;

import org.eclipse.milo.opcua.sdk.server.servicesets.Service;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Level;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Describes where a diagnostic is about to be sent, for a {@link DiagnosticsPolicy} to decide what
 * the client may see.
 *
 * @param session the activated {@link Session} the request belongs to, or {@code null} when there
 *     is none: discovery services, CreateSession, ActivateSession, and requests that fail before a
 *     Session is identified.
 * @param service the service that is responding.
 * @param level whether the diagnostic explains the service as a whole or one of its operations.
 */
@NullMarked
public record DiagnosticsRequest(@Nullable Session session, Service service, Level level) {

  /**
   * @param session the activated {@link Session} the request belongs to, or {@code null}.
   * @param service the service that is responding.
   * @param level the level of the diagnostic.
   */
  public DiagnosticsRequest {
    requireNonNull(service, "service");
    requireNonNull(level, "level");
  }
}
