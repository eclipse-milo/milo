/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.reverse;

import java.time.Instant;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * Immutable runtime snapshot for a server-side Reverse Connect target.
 *
 * @param targetId the stable target id.
 * @param clientListenerUrl the client reverse-listener URL.
 * @param endpointUrl the server endpoint URL advertised in {@code ReverseHello}.
 * @param registrationPeriod the target registration period.
 * @param connectTimeout the TCP connect timeout.
 * @param enabled whether the target is enabled.
 * @param paused whether the target is currently paused.
 * @param nextAttemptTime the next scheduled attempt time, when one is scheduled.
 * @param lastAttemptTime the last time an attempt started.
 * @param lastSuccessTime the last time an attempt handed a channel to the server path.
 * @param activeChannelCount the number of active reverse-opened channels for this target.
 * @param lastStatusCode the last failed attempt status code, when available.
 * @param lastError the last failed attempt exception, when available.
 */
public record ReverseConnectTargetSnapshot(
    UUID targetId,
    String clientListenerUrl,
    String endpointUrl,
    UInteger registrationPeriod,
    UInteger connectTimeout,
    boolean enabled,
    boolean paused,
    @Nullable Instant nextAttemptTime,
    @Nullable Instant lastAttemptTime,
    @Nullable Instant lastSuccessTime,
    int activeChannelCount,
    @Nullable StatusCode lastStatusCode,
    @Nullable Throwable lastError) {}
