/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * Supplies the MessageNonce written to the SecurityHeader of one secured UADP NetworkMessage (OPC
 * UA Part 14 §7.2.4.4.3.2, Table 156).
 *
 * <p>Contract (binding):
 *
 * <ul>
 *   <li>The encoder invokes {@link #nextNonce()} <b>exactly once per NetworkMessage</b>. The seam
 *       is a supplier — not a pre-resolved nonce on the {@link MessageSecurityContext} — since one
 *       publish cycle can emit multiple NetworkMessages (partitioned writer groups, multi-NM
 *       mappings); a fixed per-cycle nonce would be catastrophically reused.
 *   <li>The returned array is exactly 8 bytes: {@code Random[4] || NonceSequenceNumber} (UInt32,
 *       little-endian). The 4 random bytes are drawn fresh per call (pseudo-random is sufficient
 *       per Table 156); the sequence number is per-key, reset to 1 on every token/key switch, and
 *       incremented by exactly one per NetworkMessage.
 *   <li><b>A (key, nonce) pair is never reused</b> — AES-CTR keystream reuse is a total
 *       confidentiality break. Implementations throw {@link UaException} with {@code
 *       Bad_InternalError} rather than let the 32-bit sequence number wrap within one token.
 *   <li>Implementations are thread-safe: one SecurityGroup can back several writer groups
 *       publishing concurrently.
 * </ul>
 *
 * @see MessageSecurityContext
 */
@FunctionalInterface
public interface MessageNonceSupplier {

  /**
   * Get the MessageNonce for the next NetworkMessage.
   *
   * @return exactly 8 bytes: {@code Random[4] || NonceSequenceNumber} (UInt32, little-endian).
   * @throws UaException with {@code Bad_InternalError} if a unique nonce cannot be produced, e.g.
   *     the per-key sequence number would wrap.
   */
  byte[] nextNonce() throws UaException;
}
