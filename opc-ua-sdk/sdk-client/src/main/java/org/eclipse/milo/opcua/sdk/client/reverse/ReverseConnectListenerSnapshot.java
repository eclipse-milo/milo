/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.jspecify.annotations.Nullable;

/**
 * Immutable view of one client-side reverse-connect listener.
 *
 * <p>The manager creates listener snapshots when reporting bind/unbind events and when building a
 * full {@link ReverseConnectManagerSnapshot}. Counters are cumulative for the lifetime of the
 * manager instance and are not reset when the listener unbinds during shutdown.
 *
 * @param bindAddress the configured address the listener attempts to bind.
 * @param boundAddress the actual local address once bound, which may include an ephemeral port, or
 *     {@code null} before bind or after unbind.
 * @param bound {@code true} when the listener's server channel is currently open.
 * @param acceptedCount the number of TCP sockets accepted by this listener.
 * @param claimedCount the number of this listener's candidates claimed by selectors.
 * @param rejectedCount the number of this listener's candidates rejected or closed before claim.
 * @param expiredCount the number of this listener's pending candidates that reached hold-time
 *     expiry.
 * @param pendingCount the number of this listener's decoded candidates currently waiting for a
 *     selector claim.
 * @param lastError the latest listener-specific error diagnostic, or {@code null} if none has been
 *     recorded.
 */
public record ReverseConnectListenerSnapshot(
    InetSocketAddress bindAddress,
    @Nullable SocketAddress boundAddress,
    boolean bound,
    long acceptedCount,
    long claimedCount,
    long rejectedCount,
    long expiredCount,
    int pendingCount,
    @Nullable String lastError) {}
