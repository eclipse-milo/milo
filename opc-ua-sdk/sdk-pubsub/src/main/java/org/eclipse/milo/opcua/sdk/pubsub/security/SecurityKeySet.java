/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.security;

import java.time.Duration;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * Key material for a SecurityGroup, returned by a {@link SecurityKeyProvider}.
 *
 * <p>Mirrors the outputs of the Security Key Service {@code GetSecurityKeys} method (and the inputs
 * of {@code SetSecurityKeys}). Each element of {@code keys} is one key data ByteString — {@code
 * SigningKey || EncryptingKey || KeyNonce} per Part 14 Table 155 — split with {@link
 * SecurityKeyMaterial#split}.
 *
 * <p>Contract pins:
 *
 * <ul>
 *   <li><b>Policy is authoritative:</b> {@code securityPolicyUri} is the policy the keys were
 *       generated for. Consumers must fail the fetch when it mismatches a non-null configured
 *       SecurityGroup policy URI (never silently downgrade); a null configured URI accepts any
 *       supported policy.
 *   <li><b>Token ordering:</b> {@code keys} is ordered by consecutive token id starting at {@code
 *       firstTokenId} — {@code keys.get(i)} belongs to token {@code firstTokenId + i}.
 *   <li><b>Static sentinel:</b> {@code timeToNextKey == Duration.ZERO && keyLifetime ==
 *       Duration.ZERO} marks a static key set (e.g. from {@link StaticSecurityKeyProvider}):
 *       consumers shall schedule no key rotation and apply no expiry. Exactly one of the two being
 *       zero is malformed and consumers treat the fetch as failed.
 *   <li><b>Transient transfer object:</b> the {@link ByteString} elements are immutable and are
 *       never wiped; consumers split them into {@link SecurityKeyMaterial} promptly and drop the
 *       reference (see the package documentation for the zeroization posture).
 * </ul>
 *
 * @param securityPolicyUri the URI of the security policy the keys are for.
 * @param firstTokenId the id of the security token corresponding to the first key.
 * @param keys the keys, ordered by consecutive token id starting at {@code firstTokenId}.
 * @param timeToNextKey the time until the current key is replaced by the next key.
 * @param keyLifetime the lifetime of each subsequent key.
 */
public record SecurityKeySet(
    String securityPolicyUri,
    UInteger firstTokenId,
    List<ByteString> keys,
    Duration timeToNextKey,
    Duration keyLifetime) {

  /**
   * Create a new {@link SecurityKeySet}.
   *
   * @param securityPolicyUri the URI of the security policy the keys are for.
   * @param firstTokenId the id of the security token corresponding to the first key.
   * @param keys the keys, ordered by consecutive token id starting at {@code firstTokenId}.
   * @param timeToNextKey the time until the current key is replaced by the next key.
   * @param keyLifetime the lifetime of each subsequent key.
   * @throws IllegalArgumentException if {@code keys} is empty or either duration is negative.
   * @throws NullPointerException if {@code keys} contains null elements.
   */
  public SecurityKeySet {
    keys = List.copyOf(keys);

    if (keys.isEmpty()) {
      throw new IllegalArgumentException("keys must be non-empty");
    }
    if (timeToNextKey.isNegative()) {
      throw new IllegalArgumentException("timeToNextKey must not be negative");
    }
    if (keyLifetime.isNegative()) {
      throw new IllegalArgumentException("keyLifetime must not be negative");
    }
  }

  @Override
  public String toString() {
    // Never print key bytes; the key count stands in for the keys component.
    return "SecurityKeySet{securityPolicyUri=%s, firstTokenId=%s, keys=%d, timeToNextKey=%s, keyLifetime=%s}"
        .formatted(securityPolicyUri, firstTokenId, keys.size(), timeToNextKey, keyLifetime);
  }
}
