/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

/**
 * Public identifiers for OPC UA SecurityPolicy URIs recognized by the stack.
 *
 * <p>Use {@link #fromUri(String)} to resolve a URI received from configuration, endpoints, or
 * SecureChannel headers. Use {@link #getProfile()} when policy metadata is needed.
 */
@SuppressWarnings("HttpUrlsUsage")
public enum SecurityPolicy {

  /** A policy that does not provide message signing, encryption, or key agreement. */
  None("http://opcfoundation.org/UA/SecurityPolicy#None"),

  /**
   * A legacy RSA policy that uses RSA-SHA1 signatures, RSA15 asymmetric encryption, and AES-128
   * symmetric encryption.
   */
  Basic128Rsa15("http://opcfoundation.org/UA/SecurityPolicy#Basic128Rsa15"),

  /**
   * A legacy RSA policy that uses RSA-SHA1 signatures, RSA-OAEP asymmetric encryption, and AES-256
   * symmetric encryption.
   */
  Basic256("http://opcfoundation.org/UA/SecurityPolicy#Basic256"),

  /**
   * An RSA policy that uses RSA-SHA256 signatures, RSA-OAEP asymmetric encryption, and AES-256
   * symmetric encryption.
   */
  Basic256Sha256("http://opcfoundation.org/UA/SecurityPolicy#Basic256Sha256"),

  /**
   * An RSA policy that uses RSA-SHA256 signatures, RSA-OAEP asymmetric encryption, and AES-128
   * symmetric encryption.
   */
  Aes128_Sha256_RsaOaep("http://opcfoundation.org/UA/SecurityPolicy#Aes128_Sha256_RsaOaep"),

  /**
   * An RSA policy that uses RSA-PSS SHA256 signatures, RSA-OAEP-SHA256 asymmetric encryption, and
   * AES-256 symmetric encryption.
   */
  Aes256_Sha256_RsaPss("http://opcfoundation.org/UA/SecurityPolicy#Aes256_Sha256_RsaPss"),

  /**
   * An ECC policy that uses NIST P-256 ECDSA authentication, P-256 ECDH key agreement, and AES-GCM
   * chunk protection.
   */
  ECC_nistP256_AesGcm("http://opcfoundation.org/UA/SecurityPolicy#ECC_nistP256_AesGcm"),

  /**
   * An ECC policy that uses NIST P-256 ECDSA authentication, P-256 ECDH key agreement, and
   * ChaCha20-Poly1305 chunk protection.
   */
  ECC_nistP256_ChaChaPoly("http://opcfoundation.org/UA/SecurityPolicy#ECC_nistP256_ChaChaPoly"),

  /**
   * An ECC policy that uses NIST P-384 ECDSA authentication, P-384 ECDH key agreement, and AES-GCM
   * chunk protection.
   */
  ECC_nistP384_AesGcm("http://opcfoundation.org/UA/SecurityPolicy#ECC_nistP384_AesGcm"),

  /**
   * An ECC policy that uses NIST P-384 ECDSA authentication, P-384 ECDH key agreement, and
   * ChaCha20-Poly1305 chunk protection.
   */
  ECC_nistP384_ChaChaPoly("http://opcfoundation.org/UA/SecurityPolicy#ECC_nistP384_ChaChaPoly"),

  /**
   * An ECC policy that uses Brainpool P-256r1 ECDSA authentication, Brainpool P-256r1 ECDH key
   * agreement, and AES-GCM chunk protection.
   */
  ECC_brainpoolP256r1_AesGcm(
      "http://opcfoundation.org/UA/SecurityPolicy#ECC_brainpoolP256r1_AesGcm"),

  /**
   * An ECC policy that uses Brainpool P-256r1 ECDSA authentication, Brainpool P-256r1 ECDH key
   * agreement, and ChaCha20-Poly1305 chunk protection.
   */
  ECC_brainpoolP256r1_ChaChaPoly(
      "http://opcfoundation.org/UA/SecurityPolicy#ECC_brainpoolP256r1_ChaChaPoly"),

  /**
   * An ECC policy that uses Ed25519 authentication, X25519 key agreement, and AES-GCM chunk
   * protection.
   */
  ECC_curve25519_AesGcm("http://opcfoundation.org/UA/SecurityPolicy#ECC_curve25519_AesGcm"),

  /**
   * An ECC policy that uses Ed25519 authentication, X25519 key agreement, and ChaCha20-Poly1305
   * chunk protection.
   */
  ECC_curve25519_ChaChaPoly("http://opcfoundation.org/UA/SecurityPolicy#ECC_curve25519_ChaChaPoly"),

  /**
   * An ECC policy that uses Ed448 authentication, X448 key agreement, and AES-GCM chunk protection.
   */
  ECC_curve448_AesGcm("http://opcfoundation.org/UA/SecurityPolicy#ECC_curve448_AesGcm"),

  /**
   * An ECC policy that uses Ed448 authentication, X448 key agreement, and ChaCha20-Poly1305 chunk
   * protection.
   */
  ECC_curve448_ChaChaPoly("http://opcfoundation.org/UA/SecurityPolicy#ECC_curve448_ChaChaPoly"),

  /**
   * An ECC policy that uses Brainpool P-384r1 ECDSA authentication, Brainpool P-384r1 ECDH key
   * agreement, and ChaCha20-Poly1305 chunk protection.
   */
  ECC_brainpoolP384r1_ChaChaPoly(
      "http://opcfoundation.org/UA/SecurityPolicy#ECC_brainpoolP384r1_ChaChaPoly"),

  /**
   * An ECC policy that uses Brainpool P-384r1 ECDSA authentication, Brainpool P-384r1 ECDH key
   * agreement, and AES-GCM chunk protection.
   */
  ECC_brainpoolP384r1_AesGcm(
      "http://opcfoundation.org/UA/SecurityPolicy#ECC_brainpoolP384r1_AesGcm"),

  /**
   * An RSA-DH policy that uses RSA-SHA256 authentication, RFC 7919 ffdhe3072 key agreement, and
   * AES-GCM chunk protection.
   */
  RSA_DH_AesGcm("http://opcfoundation.org/UA/SecurityPolicy#RSA_DH_AesGcm"),

  /**
   * An RSA-DH policy that uses RSA-SHA256 authentication, RFC 7919 ffdhe3072 key agreement, and
   * ChaCha20-Poly1305 chunk protection.
   */
  RSA_DH_ChaChaPoly("http://opcfoundation.org/UA/SecurityPolicy#RSA_DH_ChaChaPoly");

  private final String securityPolicyUri;

  SecurityPolicy(String securityPolicyUri) {
    this.securityPolicyUri = securityPolicyUri;
  }

  public String getUri() {
    return securityPolicyUri;
  }

  /**
   * Get the profile metadata for this security policy.
   *
   * @return the registered profile for this security policy.
   */
  public SecurityPolicyProfile getProfile() {
    return SecurityPolicyProfiles.get(this);
  }

  /**
   * Get the legacy symmetric signature algorithm for this policy.
   *
   * @return the symmetric signature algorithm. {@link SecurityPolicy#None} returns {@link
   *     SecurityAlgorithm#None}; secured policies return {@code null} when they do not use a legacy
   *     symmetric signature algorithm.
   */
  public @Nullable SecurityAlgorithm getSymmetricSignatureAlgorithm() {
    return getProfile().symmetricSignatureAlgorithm();
  }

  /**
   * Get the legacy symmetric encryption algorithm for this policy.
   *
   * @return the symmetric encryption algorithm. {@link SecurityPolicy#None} returns {@link
   *     SecurityAlgorithm#None}; secured policies return {@code null} when they do not use a legacy
   *     symmetric encryption algorithm.
   */
  public @Nullable SecurityAlgorithm getSymmetricEncryptionAlgorithm() {
    return getProfile().symmetricEncryptionAlgorithm();
  }

  /**
   * Get the asymmetric signature algorithm for this policy.
   *
   * @return the asymmetric signature algorithm. {@link SecurityPolicy#None} returns {@link
   *     SecurityAlgorithm#None}; secured policies return {@link SecurityAlgorithm#None} when
   *     signing is described by profile metadata instead.
   */
  public SecurityAlgorithm getAsymmetricSignatureAlgorithm() {
    return getProfile().asymmetricSignatureAlgorithm();
  }

  /**
   * Get the asymmetric encryption algorithm for this policy.
   *
   * @return the asymmetric encryption algorithm. {@link SecurityPolicy#None} returns {@link
   *     SecurityAlgorithm#None}; secured policies return {@link SecurityAlgorithm#None} when no OPN
   *     encryption algorithm applies.
   */
  public SecurityAlgorithm getAsymmetricEncryptionAlgorithm() {
    return getProfile().asymmetricEncryptionAlgorithm();
  }

  /**
   * Get the asymmetric key-wrap algorithm for this policy.
   *
   * @return the asymmetric key-wrap algorithm. {@link SecurityPolicy#None} returns {@link
   *     SecurityAlgorithm#None}; secured policies return {@code null} when they do not use
   *     asymmetric key wrapping.
   */
  public @Nullable SecurityAlgorithm getAsymmetricKeyWrapAlgorithm() {
    return getProfile().asymmetricKeyWrapAlgorithm();
  }

  /**
   * Get the legacy key-derivation algorithm for this policy.
   *
   * @return the key-derivation algorithm. {@link SecurityPolicy#None} returns {@link
   *     SecurityAlgorithm#None}; secured policies return {@code null} when they do not use a legacy
   *     key-derivation algorithm.
   */
  public @Nullable SecurityAlgorithm getKeyDerivationAlgorithm() {
    return getProfile().keyDerivationAlgorithm();
  }

  /**
   * Get the legacy certificate signature algorithm for this policy.
   *
   * @return the certificate signature algorithm. {@link SecurityPolicy#None} returns {@link
   *     SecurityAlgorithm#None}; secured policies return {@code null} when certificate
   *     compatibility is described by profile metadata instead.
   */
  public @Nullable SecurityAlgorithm getCertificateSignatureAlgorithm() {
    return getProfile().certificateSignatureAlgorithm();
  }

  /**
   * Resolve a security policy URI.
   *
   * @param securityPolicyUri the policy URI to resolve.
   * @return the matching security policy.
   * @throws UaException if {@code securityPolicyUri} is unknown.
   */
  public static SecurityPolicy fromUri(String securityPolicyUri) throws UaException {
    for (SecurityPolicy securityPolicy : values()) {
      if (securityPolicy.getUri().equals(securityPolicyUri)) {
        return securityPolicy;
      }
    }

    throw new UaException(
        StatusCodes.Bad_SecurityPolicyRejected, "unknown securityPolicyUri: " + securityPolicyUri);
  }

  /**
   * Resolve a security policy URI without throwing for unknown policies.
   *
   * @param securityPolicyUri the policy URI to resolve.
   * @return the matching policy, or an empty optional when the URI is unknown.
   */
  public static Optional<SecurityPolicy> fromUriSafe(String securityPolicyUri) {
    try {
      return Optional.of(fromUri(securityPolicyUri));
    } catch (Throwable t) {
      return Optional.empty();
    }
  }
}
