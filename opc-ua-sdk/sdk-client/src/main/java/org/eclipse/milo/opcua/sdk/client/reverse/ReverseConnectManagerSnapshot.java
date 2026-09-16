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

import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * Immutable manager state used for observability and later reverse-connect session orchestration.
 *
 * @param running {@code true} while the manager listener sockets are bound and accepting channels.
 * @param listeners snapshots for each configured listener address.
 * @param pendingCandidates decoded and verified candidates that are waiting for a selector claim.
 * @param acceptedCandidates retained candidates that have been claimed and transferred out of
 *     manager ownership.
 * @param rejectedCandidates retained candidates that reached a rejected, expired, closed, or
 *     shutdown state before they were claimed.
 * @param acceptedCount the total number of TCP sockets accepted by all listeners.
 * @param claimedCount the total number of candidates claimed by selectors.
 * @param rejectedCount the total number of candidates rejected or closed before claim.
 * @param expiredCount the total number of pending candidates that reached hold-time expiry.
 * @param lastError the latest manager-level error diagnostic, or {@code null} if none has been
 *     recorded.
 */
public record ReverseConnectManagerSnapshot(
    boolean running,
    List<ReverseConnectListenerSnapshot> listeners,
    List<ReverseConnectCandidateSnapshot> pendingCandidates,
    List<ReverseConnectCandidateSnapshot> acceptedCandidates,
    List<ReverseConnectCandidateSnapshot> rejectedCandidates,
    long acceptedCount,
    long claimedCount,
    long rejectedCount,
    long expiredCount,
    @Nullable String lastError) {

  public ReverseConnectManagerSnapshot {
    listeners = List.copyOf(listeners);
    pendingCandidates = List.copyOf(pendingCandidates);
    acceptedCandidates = List.copyOf(acceptedCandidates);
    rejectedCandidates = List.copyOf(rejectedCandidates);
  }
}
