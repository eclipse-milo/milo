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

import java.security.MessageDigest;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

public enum SecurityAlgorithm {
  None("", ""),

  /** Symmetric Signature; transformation to be used with {@link Mac#getInstance(String)}. */
  HmacSha1("http://www.w3.org/2000/09/xmldsig#hmac-sha1", "HmacSHA1"),

  /** Symmetric Signature; transformation to be used with {@link Mac#getInstance(String)}. */
  HmacSha256("http://www.w3.org/2000/09/xmldsig#hmac-sha256", "HmacSHA256"),

  /** Symmetric Signature; transformation to be used with {@link Mac#getInstance(String)}. */
  HmacSha384("http://www.w3.org/2001/04/xmldsig-more#hmac-sha384", "HmacSHA384"),

  /** Symmetric Encryption; transformation to be used with {@link Cipher#getInstance(String)}. */
  Aes128("http://www.w3.org/2001/04/xmlenc#aes128-cbc", "AES/CBC/NoPadding"),

  /** Symmetric Encryption; transformation to be used with {@link Cipher#getInstance(String)}. */
  Aes256("http://www.w3.org/2001/04/xmlenc#aes256-cbc", "AES/CBC/NoPadding"),

  /** Asymmetric Signature; transformation to be used with {@link Signature#getInstance(String)}. */
  RsaSha1("http://www.w3.org/2000/09/xmldsig#rsa-sha1", "SHA1withRSA"),

  /** Asymmetric Signature; transformation to be used with {@link Signature#getInstance(String)}. */
  RsaSha256("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", "SHA256withRSA"),

  /**
   * Asymmetric Signature; transformation to be used with {@link Signature#getInstance(String)}.
   *
   * <p>Requires the explicit PSS parameters from {@link #getAlgorithmParameterSpec()}.
   */
  RsaSha256Pss("http://opcfoundation.org/UA/security/rsa-pss-sha2-256", "RSASSA-PSS"),

  /** Asymmetric Encryption; transformation to be used with {@link Cipher#getInstance(String)}. */
  Rsa15("http://www.w3.org/2001/04/xmlenc#rsa-1_5", "RSA/ECB/PKCS1Padding"),

  /**
   * Asymmetric Encryption; transformation to be used with {@link Cipher#getInstance(String)}.
   *
   * <p>{@link #getAlgorithmParameterSpec()} returns the JDK default OAEP parameters (SHA-1 for both
   * the digest and MGF1), pinned explicitly so provider defaults cannot change the wire format.
   */
  RsaOaepSha1("http://www.w3.org/2001/04/xmlenc#rsa-oaep", "RSA/ECB/OAEPWithSHA-1AndMGF1Padding"),

  /**
   * Asymmetric Encryption; transformation to be used with {@link Cipher#getInstance(String)}.
   *
   * <p>Requires the explicit OAEP parameters from {@link #getAlgorithmParameterSpec()} so both the
   * digest and MGF1 use SHA-256 regardless of provider defaults.
   */
  RsaOaepSha256(
      "http://opcfoundation.org/UA/security/rsa-oaep-sha2-256",
      "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"),

  /** Asymmetric Key Wrap */
  KwRsa15("http://www.w3.org/2001/04/xmlenc#rsa-1_5", ""),

  /** Asymmetric Key Wrap */
  KwRsaOaep("http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p", ""),

  /** Key Derivation */
  PSha1("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1", ""),

  /** Key Derivation */
  PSha256("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha256", ""),

  /**
   * Cryptographic Hash; transformation to be used with {@link MessageDigest#getInstance(String)}.
   */
  Sha1("http://www.w3.org/2000/09/xmldsig#sha1", "SHA-1"),

  /**
   * Cryptographic Hash; transformation to be used with {@link MessageDigest#getInstance(String)}.
   */
  Sha256("http://www.w3.org/2001/04/xmlenc#sha256", "SHA-256"),

  /**
   * Cryptographic Hash; transformation to be used with {@link MessageDigest#getInstance(String)}.
   */
  Sha384("http://www.w3.org/2001/04/xmldsig-more#sha384", "SHA-384");

  private static final PSSParameterSpec PSS_SHA256 =
      new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);

  private static final OAEPParameterSpec OAEP_SHA256 =
      new OAEPParameterSpec(
          "SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);

  private final String uri;
  private final String transformation;

  SecurityAlgorithm(String uri, String transformation) {
    this.uri = uri;
    this.transformation = transformation;
  }

  /**
   * @return The URI identifying this security algorithm.
   */
  public String getUri() {
    return uri;
  }

  /**
   * @return The transformation string to use with the appropriate provider SPI.
   */
  public String getTransformation() {
    return transformation;
  }

  /**
   * Returns the parameters required by this algorithm, or {@code null} if none are needed.
   *
   * <p>RSA-PSS uses SHA-256, MGF1/SHA-256, a 32-byte salt, and trailer field 1. RSA-OAEP uses the
   * named digest for both OAEP and MGF1, with an empty label. Callers creating JCA operations
   * directly must apply these parameters when initializing a cipher or configuring a signature;
   * {@link org.eclipse.milo.opcua.stack.core.util.SignatureFactory} and {@link
   * org.eclipse.milo.opcua.stack.core.util.CipherFactory} do this.
   *
   * @return the algorithm parameters, or {@code null} if none are needed.
   */
  public @Nullable AlgorithmParameterSpec getAlgorithmParameterSpec() {
    return switch (this) {
      case RsaSha256Pss -> PSS_SHA256;
      case RsaOaepSha256 -> OAEP_SHA256;
      case RsaOaepSha1 -> OAEPParameterSpec.DEFAULT;
      default -> null;
    };
  }

  public static SecurityAlgorithm fromUri(String securityAlgorithmUri) throws UaException {
    for (SecurityAlgorithm algorithm : values()) {
      if (algorithm.getUri().equals(securityAlgorithmUri)) {
        return algorithm;
      }
    }

    throw new UaException(
        StatusCodes.Bad_SecurityChecksFailed,
        "unknown securityAlgorithmUri: " + securityAlgorithmUri);
  }
}
