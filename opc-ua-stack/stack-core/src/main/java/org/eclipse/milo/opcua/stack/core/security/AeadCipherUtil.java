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

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile;

/**
 * Creates provider-resolved AEAD ciphers for SecureChannel chunk protection.
 *
 * <p>The caller supplies the OPC UA key, per-chunk nonce, and associated-data bytes for exactly one
 * chunk. The returned {@link Cipher} is already initialized; callers should use it once and then
 * discard it because AEAD policies require a fresh nonce for every encrypted chunk.
 */
public final class AeadCipherUtil {

  /** The 16-byte authentication tag size required by current OPC UA AEAD profiles. */
  public static final int TAG_LENGTH_BITS = 128;

  private AeadCipherUtil() {}

  /**
   * Initializes an AES-GCM cipher for one SecureChannel chunk.
   *
   * @param providerProfile the resolved provider profile for the selected security policy.
   * @param mode the JCA cipher mode, such as {@link Cipher#ENCRYPT_MODE} or {@link
   *     Cipher#DECRYPT_MODE}.
   * @param key the directional symmetric encryption key for the current token.
   * @param nonce the 12-byte nonce built from the token id and AEAD sequence state.
   * @param aad the authenticated, unencrypted chunk header bytes.
   * @return an initialized cipher ready for one chunk operation.
   * @throws UaException when the provider cannot create or initialize the cipher.
   */
  public static Cipher initAesGcm(
      ProviderProfile providerProfile, int mode, byte[] key, byte[] nonce, ByteBuffer aad)
      throws UaException {

    return init(
        providerProfile,
        "AES/GCM/NoPadding",
        "AES",
        mode,
        key,
        new GCMParameterSpec(TAG_LENGTH_BITS, nonce),
        aad);
  }

  /**
   * Initializes a ChaCha20-Poly1305 cipher for one SecureChannel chunk.
   *
   * @param providerProfile the resolved provider profile for the selected security policy.
   * @param mode the JCA cipher mode, such as {@link Cipher#ENCRYPT_MODE} or {@link
   *     Cipher#DECRYPT_MODE}.
   * @param key the directional symmetric encryption key for the current token.
   * @param nonce the 12-byte nonce built from the token id and AEAD sequence state.
   * @param aad the authenticated, unencrypted chunk header bytes.
   * @return an initialized cipher ready for one chunk operation.
   * @throws UaException when the provider cannot create or initialize the cipher.
   */
  public static Cipher initChaCha20Poly1305(
      ProviderProfile providerProfile, int mode, byte[] key, byte[] nonce, ByteBuffer aad)
      throws UaException {

    return init(
        providerProfile,
        "ChaCha20-Poly1305",
        "ChaCha20",
        mode,
        key,
        new IvParameterSpec(nonce),
        aad);
  }

  private static Cipher init(
      ProviderProfile providerProfile,
      String transformation,
      String keyAlgorithm,
      int mode,
      byte[] key,
      AlgorithmParameterSpec parameterSpec,
      ByteBuffer aad)
      throws UaException {

    try {
      Cipher cipher =
          SecurityProviderSupport.withProviderProfile(
              providerProfile,
              transformation + " Cipher",
              p -> Cipher.getInstance(transformation, p));

      cipher.init(mode, new SecretKeySpec(key, keyAlgorithm), parameterSpec);
      cipher.updateAAD(aad.duplicate());

      return cipher;
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }
}
