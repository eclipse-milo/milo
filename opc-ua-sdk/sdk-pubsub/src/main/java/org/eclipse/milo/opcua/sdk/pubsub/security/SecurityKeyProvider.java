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

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * SPI providing runtime key material for a SecurityGroup, bound via {@code PubSubBindings}.
 *
 * <p>Shaped for eventual Security Key Service ({@code GetSecurityKeys}) integration; key material
 * is a runtime concern, never a static config blob. Unused by the v1 runtime, which supports {@code
 * MessageSecurityMode.None} only.
 */
@FunctionalInterface
public interface SecurityKeyProvider {

  /**
   * Get key material for a SecurityGroup.
   *
   * @param securityGroupId the id of the SecurityGroup to get keys for.
   * @param startingTokenId the id of the first token to return; 0 requests the current token.
   * @param requestedKeyCount the number of keys requested, including the starting token's key.
   * @return a {@link CompletableFuture} that completes with the requested {@link SecurityKeySet},
   *     or completes exceptionally if the keys could not be obtained.
   */
  CompletableFuture<SecurityKeySet> getKeys(
      String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount);
}
