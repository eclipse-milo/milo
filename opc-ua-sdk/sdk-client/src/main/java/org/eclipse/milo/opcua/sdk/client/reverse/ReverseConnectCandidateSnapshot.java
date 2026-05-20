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

import java.net.SocketAddress;
import java.time.Instant;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * Immutable view of a reverse-connect candidate.
 *
 * <p>Snapshots are copied out of the manager under its private lock and can be safely retained by
 * listeners, verifiers, selectors, tests, and later connection code without exposing mutable
 * manager state.
 *
 * @param id the manager-assigned identifier for this candidate.
 * @param state the candidate lifecycle state at the moment the snapshot was created.
 * @param serverUri the {@code ServerUri} decoded from {@code ReverseHello}, or {@code null} before
 *     that message is available.
 * @param endpointUrl the {@code EndpointUrl} decoded from {@code ReverseHello}, or {@code null}
 *     before that message is available.
 * @param remoteAddress the remote socket address for the server-opened channel, if Netty exposes
 *     one.
 * @param localAddress the local listener address for the accepted channel, if Netty exposes one.
 * @param acceptedAt the time the manager accepted the TCP socket.
 * @param reverseHelloReceivedAt the time {@code ReverseHello} was decoded, or {@code null} while
 *     the candidate is still waiting for its first message.
 * @param completedAt the time the candidate reached a terminal manager state, or {@code null} while
 *     it can still be matched or claimed.
 * @param rejectionReason the manager-level reason the candidate was rejected, expired, or closed,
 *     or {@code null} for non-terminal and successfully claimed candidates.
 * @param rejectionStatusCode the TCP error status associated with a rejected or expired candidate,
 *     or {@code null} when no error status applies.
 * @param diagnostic a human-readable diagnostic associated with rejection, expiry, or close events,
 *     or {@code null} when there is no diagnostic.
 */
public record ReverseConnectCandidateSnapshot(
    UUID id,
    ReverseConnectCandidateState state,
    @Nullable String serverUri,
    @Nullable String endpointUrl,
    @Nullable SocketAddress remoteAddress,
    @Nullable SocketAddress localAddress,
    Instant acceptedAt,
    @Nullable Instant reverseHelloReceivedAt,
    @Nullable Instant completedAt,
    @Nullable ReverseConnectRejectionReason rejectionReason,
    @Nullable StatusCode rejectionStatusCode,
    @Nullable String diagnostic) {}
