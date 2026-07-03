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

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;

/**
 * The SecurityPolicies applicable to PubSub message security and their derived parameter tables.
 *
 * <p>OPC UA Part 7 defines exactly two PubSub SecurityPolicies (profiles 1593 and 1594); the
 * parameters here are derived from Part 14 v1.05 Tables 155/156/157 and the profile conformance
 * units:
 *
 * <table border="1">
 *   <caption>PubSub SecurityPolicy parameters</caption>
 *   <tr><th></th><th>PubSub-Aes128-CTR</th><th>PubSub-Aes256-CTR</th></tr>
 *   <tr><td>SigningKey length</td><td>32 B (HMAC-SHA2-256)</td><td>32 B</td></tr>
 *   <tr><td>EncryptingKey length</td><td>16 B</td><td>32 B</td></tr>
 *   <tr><td>KeyNonce length</td><td>4 B</td><td>4 B</td></tr>
 *   <tr><td>Key data ({@code Keys[i]} ByteString)</td><td>52 B</td><td>68 B</td></tr>
 *   <tr><td>MessageNonce length (wire)</td><td>8 B</td><td>8 B</td></tr>
 *   <tr><td>Signature length (wire)</td><td>32 B</td><td>32 B</td></tr>
 * </table>
 *
 * <p>Neither policy uses a SecurityFooter.
 *
 * <p>There is no constant for {@code MessageSecurityMode.None}: mode None uses no PubSub
 * SecurityPolicy. Client/server policies (Basic256Sha256, ...) do not apply to PubSub message
 * security.
 *
 * <p>Part 14 §7.2.4.4.3.2 notes "Other SecurityPolicy may specify different key lengths or
 * cryptography algorithms" — consumers must read lengths from this table rather than hardcoding
 * them.
 */
public enum PubSubSecurityPolicy {

  /** PubSub-Aes128-CTR: AES-128 in CTR mode with HMAC-SHA2-256 signatures (profile 1593). */
  PubSubAes128Ctr(
      "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes128-CTR",
      SecurityAlgorithm.Aes128Ctr,
      16),

  /** PubSub-Aes256-CTR: AES-256 in CTR mode with HMAC-SHA2-256 signatures (profile 1594). */
  PubSubAes256Ctr(
      "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes256-CTR",
      SecurityAlgorithm.Aes256Ctr,
      32);

  private static final int SIGNING_KEY_LENGTH = 32;
  private static final int KEY_NONCE_LENGTH = 4;
  private static final int MESSAGE_NONCE_LENGTH = 8;
  private static final int SIGNATURE_LENGTH = 32;

  private final String uri;
  private final SecurityAlgorithm symmetricEncryptionAlgorithm;
  private final int encryptingKeyLength;

  PubSubSecurityPolicy(
      String uri, SecurityAlgorithm symmetricEncryptionAlgorithm, int encryptingKeyLength) {

    this.uri = uri;
    this.symmetricEncryptionAlgorithm = symmetricEncryptionAlgorithm;
    this.encryptingKeyLength = encryptingKeyLength;
  }

  /**
   * Get the URI identifying this SecurityPolicy.
   *
   * @return the SecurityPolicy URI.
   */
  public String getUri() {
    return uri;
  }

  /**
   * Get the symmetric encryption algorithm of this policy.
   *
   * @return {@link SecurityAlgorithm#Aes128Ctr} or {@link SecurityAlgorithm#Aes256Ctr}.
   */
  public SecurityAlgorithm getSymmetricEncryptionAlgorithm() {
    return symmetricEncryptionAlgorithm;
  }

  /**
   * Get the symmetric signature algorithm of this policy.
   *
   * @return {@link SecurityAlgorithm#HmacSha256} (both policies).
   */
  public SecurityAlgorithm getSymmetricSignatureAlgorithm() {
    return SecurityAlgorithm.HmacSha256;
  }

  /**
   * Get the length in bytes of the SigningKey portion of the key data.
   *
   * @return 32 (both policies).
   */
  public int getSigningKeyLength() {
    return SIGNING_KEY_LENGTH;
  }

  /**
   * Get the length in bytes of the EncryptingKey portion of the key data.
   *
   * @return 16 for {@link #PubSubAes128Ctr}, 32 for {@link #PubSubAes256Ctr}.
   */
  public int getEncryptingKeyLength() {
    return encryptingKeyLength;
  }

  /**
   * Get the length in bytes of the KeyNonce portion of the key data.
   *
   * @return 4 (both policies).
   */
  public int getKeyNonceLength() {
    return KEY_NONCE_LENGTH;
  }

  /**
   * Get the total length in bytes of one key data ByteString as returned by {@code
   * GetSecurityKeys}: {@code SigningKey || EncryptingKey || KeyNonce} (Part 14 Table 155).
   *
   * @return 52 for {@link #PubSubAes128Ctr}, 68 for {@link #PubSubAes256Ctr}.
   */
  public int getKeyDataLength() {
    return SIGNING_KEY_LENGTH + encryptingKeyLength + KEY_NONCE_LENGTH;
  }

  /**
   * Get the length in bytes of the MessageNonce carried in the SecurityHeader (Part 14 Table 156).
   *
   * @return 8 (both policies).
   */
  public int getMessageNonceLength() {
    return MESSAGE_NONCE_LENGTH;
  }

  /**
   * Get the length in bytes of the signature appended to a signed NetworkMessage.
   *
   * @return 32 (both policies; HMAC-SHA2-256).
   */
  public int getSignatureLength() {
    return SIGNATURE_LENGTH;
  }

  /**
   * Look up a policy by its SecurityPolicy URI.
   *
   * <p>Returns empty rather than throwing: a null or unrecognized configured URI is a key-fetch
   * failure condition for the consumer to handle, not a parse-time error.
   *
   * @param uri the SecurityPolicy URI.
   * @return the matching {@link PubSubSecurityPolicy}, or empty if {@code uri} matches neither
   *     policy.
   */
  public static Optional<PubSubSecurityPolicy> fromUri(String uri) {
    for (PubSubSecurityPolicy policy : values()) {
      if (policy.uri.equals(uri)) {
        return Optional.of(policy);
      }
    }
    return Optional.empty();
  }

  /**
   * Look up a policy by the length of a key data ByteString.
   *
   * <p>This is the de-facto static-key selection rule used by S2OPC and OPC Labs tooling: 52 bytes
   * selects PubSub-Aes128-CTR, 68 bytes selects PubSub-Aes256-CTR.
   *
   * @param length the key data length in bytes.
   * @return the matching {@link PubSubSecurityPolicy}, or empty if {@code length} matches neither
   *     policy.
   */
  public static Optional<PubSubSecurityPolicy> fromKeyDataLength(int length) {
    for (PubSubSecurityPolicy policy : values()) {
      if (policy.getKeyDataLength() == length) {
        return Optional.of(policy);
      }
    }
    return Optional.empty();
  }
}
