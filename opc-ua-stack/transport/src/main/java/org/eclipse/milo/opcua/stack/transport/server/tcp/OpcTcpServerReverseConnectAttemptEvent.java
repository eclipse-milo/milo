/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import java.time.Instant;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * Immutable state transition emitted by an internal server reverse-connect attempt.
 *
 * @param attemptId the attempt identifier.
 * @param state the state entered by the attempt.
 * @param timestamp when the transition was emitted.
 * @param statusCode an OPC UA status associated with the transition, when available.
 * @param exception an exception associated with the transition, when available.
 * @param message a diagnostic associated with the transition, when available.
 */
record OpcTcpServerReverseConnectAttemptEvent(
    UUID attemptId,
    OpcTcpServerReverseConnectAttemptState state,
    Instant timestamp,
    @Nullable StatusCode statusCode,
    @Nullable Throwable exception,
    @Nullable String message) {}
