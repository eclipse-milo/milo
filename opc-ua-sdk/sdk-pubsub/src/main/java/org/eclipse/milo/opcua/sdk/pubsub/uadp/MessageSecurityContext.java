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

import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyMaterial;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;

/**
 * The message security material for encoding one secured UADP NetworkMessage: the mode, the active
 * security token, the split key material, and the per-message nonce supplier.
 *
 * <p>Carried into the codec via {@link EncodeContext#security()}; a {@code null} context component
 * means security mode None (today's wire format, bit for bit). The engine resolves this context
 * once per publish cycle from the publisher key state and never retains it beyond the cycle; the
 * codec never invents security.
 *
 * <p>{@link #keys()} follows the {@link SecurityKeyMaterial} borrow contract: the key manager owns
 * the material and guarantees it is not destroyed while an encode using this context is in flight.
 * The policy (and thus all lengths) is available via {@code keys().getPolicy()}.
 *
 * <p>{@link #nonceSupplier()} is invoked exactly once per encoded NetworkMessage — see {@link
 * MessageNonceSupplier} for the uniqueness contract.
 *
 * @param mode the security mode; {@link MessageSecurityMode#Sign} or {@link
 *     MessageSecurityMode#SignAndEncrypt}, never None or Invalid.
 * @param securityTokenId the id of the active security token, stamped into the SecurityHeader.
 * @param keys the split key material of the active token, borrowed from the key manager.
 * @param nonceSupplier supplies the 8-byte MessageNonce; invoked exactly once per NetworkMessage.
 */
public record MessageSecurityContext(
    MessageSecurityMode mode,
    UInteger securityTokenId,
    SecurityKeyMaterial keys,
    MessageNonceSupplier nonceSupplier) {

  /**
   * Create a new {@link MessageSecurityContext}.
   *
   * @param mode the security mode; {@link MessageSecurityMode#Sign} or {@link
   *     MessageSecurityMode#SignAndEncrypt}, never None or Invalid.
   * @param securityTokenId the id of the active security token, stamped into the SecurityHeader.
   * @param keys the split key material of the active token, borrowed from the key manager.
   * @param nonceSupplier supplies the 8-byte MessageNonce; invoked exactly once per NetworkMessage.
   * @throws IllegalArgumentException if {@code mode} is not Sign or SignAndEncrypt.
   */
  public MessageSecurityContext {
    if (mode != MessageSecurityMode.Sign && mode != MessageSecurityMode.SignAndEncrypt) {
      throw new IllegalArgumentException("mode must be Sign or SignAndEncrypt: " + mode);
    }
  }
}
