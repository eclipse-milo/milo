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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Pins the full {@link PubSubSecurityPolicy} parameter table against OPC UA Part 14 v1.05 Tables
 * 155/156/157 and Part 7 profiles 1593 (PubSub-Aes128-CTR) / 1594 (PubSub-Aes256-CTR).
 *
 * <p>Every expected value is hardcoded independently of the production table so a drive-by edit to
 * either side fails here:
 *
 * <ul>
 *   <li>SigningKey length 32 B: SymmetricSignatureAlgorithm_HMAC-SHA2-256 key length 256 bits.
 *   <li>EncryptingKey length 16/32 B: SymmetricEncryptionAlgorithm key length 128/256 bits.
 *   <li>KeyNonce length 4 B: "SymmetricEncryption Nonce Length: 4 Byte" (profile Limits CUs).
 *   <li>Key data 52/68 B: SigningKey || EncryptingKey || KeyNonce (Table 155).
 *   <li>MessageNonce length 8 B: "For AES-CTR mode the length of the SecurityHeader Nonce shall be
 *       8 Bytes" (Table 157).
 *   <li>Signature length 32 B: HMAC-SHA2-256.
 * </ul>
 */
class PubSubSecurityPolicyTest {

  private static final String AES128_URI =
      "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes128-CTR";
  private static final String AES256_URI =
      "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes256-CTR";

  @Test
  void aes128CtrParameterTable() {
    PubSubSecurityPolicy policy = PubSubSecurityPolicy.PubSubAes128Ctr;

    assertEquals(AES128_URI, policy.getUri());
    assertEquals(SecurityAlgorithm.Aes128Ctr, policy.getSymmetricEncryptionAlgorithm());
    assertEquals(SecurityAlgorithm.HmacSha256, policy.getSymmetricSignatureAlgorithm());
    assertEquals(32, policy.getSigningKeyLength());
    assertEquals(16, policy.getEncryptingKeyLength());
    assertEquals(4, policy.getKeyNonceLength());
    assertEquals(52, policy.getKeyDataLength());
    assertEquals(8, policy.getMessageNonceLength());
    assertEquals(32, policy.getSignatureLength());
  }

  @Test
  void aes256CtrParameterTable() {
    PubSubSecurityPolicy policy = PubSubSecurityPolicy.PubSubAes256Ctr;

    assertEquals(AES256_URI, policy.getUri());
    assertEquals(SecurityAlgorithm.Aes256Ctr, policy.getSymmetricEncryptionAlgorithm());
    assertEquals(SecurityAlgorithm.HmacSha256, policy.getSymmetricSignatureAlgorithm());
    assertEquals(32, policy.getSigningKeyLength());
    assertEquals(32, policy.getEncryptingKeyLength());
    assertEquals(4, policy.getKeyNonceLength());
    assertEquals(68, policy.getKeyDataLength());
    assertEquals(8, policy.getMessageNonceLength());
    assertEquals(32, policy.getSignatureLength());
  }

  /**
   * The referenced stack-core algorithm constants must carry the PubSub CTR algorithm URIs (CU
   * 5503/5501) and the JCE CTR transformation — not the XML-enc CBC values of the pre-existing
   * {@code Aes128}/{@code Aes256} constants.
   */
  @Test
  void encryptionAlgorithmUrisAndTransformations() {
    SecurityAlgorithm aes128 =
        PubSubSecurityPolicy.PubSubAes128Ctr.getSymmetricEncryptionAlgorithm();
    assertEquals("http://opcfoundation.org/UA/security/aes128-ctr", aes128.getUri());
    assertEquals("AES/CTR/NoPadding", aes128.getTransformation());

    SecurityAlgorithm aes256 =
        PubSubSecurityPolicy.PubSubAes256Ctr.getSymmetricEncryptionAlgorithm();
    assertEquals("http://opcfoundation.org/UA/security/aes256-ctr", aes256.getUri());
    assertEquals("AES/CTR/NoPadding", aes256.getTransformation());

    assertEquals(SecurityAlgorithm.Aes128Ctr, aes128);
    assertEquals(SecurityAlgorithm.Aes256Ctr, aes256);
  }

  @Test
  void signatureAlgorithmTransformation() {
    for (PubSubSecurityPolicy policy : PubSubSecurityPolicy.values()) {
      assertEquals("HmacSHA256", policy.getSymmetricSignatureAlgorithm().getTransformation());
    }
  }

  @Test
  void thereAreExactlyTwoPolicies() {
    // Part 7 defines exactly two PubSub SecurityPolicies; mode None has no policy constant.
    assertEquals(2, PubSubSecurityPolicy.values().length);
  }

  @Test
  void fromUriResolvesBothPolicies() {
    assertEquals(
        Optional.of(PubSubSecurityPolicy.PubSubAes128Ctr),
        PubSubSecurityPolicy.fromUri(AES128_URI));
    assertEquals(
        Optional.of(PubSubSecurityPolicy.PubSubAes256Ctr),
        PubSubSecurityPolicy.fromUri(AES256_URI));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "",
        "http://opcfoundation.org/UA/SecurityPolicy#None",
        "http://opcfoundation.org/UA/SecurityPolicy#Basic256Sha256",
        "http://opcfoundation.org/UA/SecurityPolicy#Aes256_Sha256_RsaPss",
        "http://opcfoundation.org/UA/security/aes128-ctr",
        "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes128-Ctr"
      })
  void fromUriReturnsEmptyForUnknownUris(String uri) {
    // Includes client/server policy URIs, the algorithm (not policy) URI, and a
    // case-mismatched variant: lookups are exact, non-throwing.
    assertTrue(PubSubSecurityPolicy.fromUri(uri).isEmpty());
  }

  @Test
  void fromKeyDataLengthResolvesBothPolicies() {
    assertEquals(
        Optional.of(PubSubSecurityPolicy.PubSubAes128Ctr),
        PubSubSecurityPolicy.fromKeyDataLength(52));
    assertEquals(
        Optional.of(PubSubSecurityPolicy.PubSubAes256Ctr),
        PubSubSecurityPolicy.fromKeyDataLength(68));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 51, 53, 67, 69, 120})
  void fromKeyDataLengthReturnsEmptyForOtherLengths(int length) {
    assertTrue(PubSubSecurityPolicy.fromKeyDataLength(length).isEmpty());
  }
}
