/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;

public class NonceUtilTest {

  @Test
  public void testSecureChannelNonceGeneration() {
    for (SecurityPolicy securityPolicy : SecurityPolicy.values()) {
      ByteString nonce = NonceUtil.generateNonce(securityPolicy);

      switch (securityPolicy) {
        case None:
          assertEquals(0, nonce.length());
          break;
        case Basic128Rsa15:
          assertEquals(16, nonce.length());
          break;
        default:
          assertEquals(32, nonce.length());
      }
    }
  }

  @Test
  public void testNonceGeneration() throws UaException {
    for (int i = 32; i < 256; i++) {
      ByteString nonce = NonceUtil.generateNonce(i);
      assertEquals(i, nonce.length());
      NonceUtil.validateNonce(nonce);
    }
  }

  @Test
  public void testShortNonceThrows() {
    ByteString nonce = NonceUtil.generateNonce(NonceUtil.MINIMUM_NONCE_LENGTH - 1);

    assertThrows(UaException.class, () -> NonceUtil.validateNonce(nonce));
  }

  @Test
  public void testZeroLengthNonceValidation() throws UaException {
    // an empty nonce is valid in the secure channel layer when no security is used.
    NonceUtil.validateNonce(ByteString.of(new byte[0]), 0);
  }

  @Test
  public void blockUntilSecureRandomSeeded() throws Exception {
    NonceUtil.blockUntilSecureRandomSeeded();

    assertTrue(NonceUtil.isSecureRandomSeeded());
  }
}
