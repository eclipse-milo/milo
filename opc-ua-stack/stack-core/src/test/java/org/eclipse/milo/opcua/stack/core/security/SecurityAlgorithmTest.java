/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class SecurityAlgorithmTest {

  @Test
  void aes128CtrUriAndTransformation() {
    assertEquals(
        "http://opcfoundation.org/UA/security/aes128-ctr", SecurityAlgorithm.Aes128Ctr.getUri());
    assertEquals("AES/CTR/NoPadding", SecurityAlgorithm.Aes128Ctr.getTransformation());
  }

  @Test
  void aes256CtrUriAndTransformation() {
    assertEquals(
        "http://opcfoundation.org/UA/security/aes256-ctr", SecurityAlgorithm.Aes256Ctr.getUri());
    assertEquals("AES/CTR/NoPadding", SecurityAlgorithm.Aes256Ctr.getTransformation());
  }

  @Test
  void ctrConstantsDifferFromCbcConstants() {
    // The pre-existing Aes128/Aes256 constants are AES-CBC, which is the wrong mode for
    // OPC UA PubSub message security; the CTR constants must remain distinct from them.
    assertNotEquals(SecurityAlgorithm.Aes128, SecurityAlgorithm.Aes128Ctr);
    assertNotEquals(SecurityAlgorithm.Aes256, SecurityAlgorithm.Aes256Ctr);

    assertNotEquals(SecurityAlgorithm.Aes128.getUri(), SecurityAlgorithm.Aes128Ctr.getUri());
    assertNotEquals(SecurityAlgorithm.Aes256.getUri(), SecurityAlgorithm.Aes256Ctr.getUri());

    assertNotEquals(
        SecurityAlgorithm.Aes128.getTransformation(),
        SecurityAlgorithm.Aes128Ctr.getTransformation());
    assertNotEquals(
        SecurityAlgorithm.Aes256.getTransformation(),
        SecurityAlgorithm.Aes256Ctr.getTransformation());
  }

  @Test
  void fromUriResolvesCtrConstants() throws Exception {
    assertEquals(
        SecurityAlgorithm.Aes128Ctr,
        SecurityAlgorithm.fromUri("http://opcfoundation.org/UA/security/aes128-ctr"));
    assertEquals(
        SecurityAlgorithm.Aes256Ctr,
        SecurityAlgorithm.fromUri("http://opcfoundation.org/UA/security/aes256-ctr"));
  }
}
