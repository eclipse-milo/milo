/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * A diagnostic event from a {@link PubSubService}, delivered to {@link PubSubDiagnosticsListener}s.
 * In v1 only error events are delivered.
 *
 * @param path the path of the component the event originated from, e.g. {@code
 *     "conn/group/writer"}.
 * @param statusCode a status code classifying the error.
 * @param message a human-readable description of the event.
 * @param error the underlying exception, or {@code null} if there is none.
 * @param kind the coarse classification of the error: transmission failures of messages already
 *     handed to (or destined for) a transport channel vs everything else.
 */
public record PubSubDiagnosticsEvent(
    String path, StatusCode statusCode, String message, @Nullable Throwable error, Kind kind) {

  /** Coarse classification of a diagnostics error event. */
  public enum Kind {
    /**
     * A NetworkMessage was not transmitted: an asynchronous or synchronous send failure, or an
     * oversize message skipped at the {@code maxNetworkMessageSize} gate.
     */
    SEND_FAILURE,
    /**
     * Any other error: encode failures, source-read failures, decode errors, key-fetch failures,
     * activation failures.
     */
    OTHER_ERROR
  }
}
