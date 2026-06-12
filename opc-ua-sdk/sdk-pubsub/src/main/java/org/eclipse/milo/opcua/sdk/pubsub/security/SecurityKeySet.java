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
 * <p>Minimal v1 placeholder mirroring the result of the Security Key Service {@code
 * GetSecurityKeys} method; it may grow when message security arrives.
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
   */
  public SecurityKeySet {
    keys = List.copyOf(keys);
  }
}
