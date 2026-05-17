/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import static java.util.Objects.requireNonNull;

import java.security.GeneralSecurityException;
import java.security.Provider;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * RFC 5869 HKDF helpers for security profiles that derive keys from negotiated secrets.
 *
 * <p>This utility implements the extract-and-expand operation only. Profile-specific salt and
 * {@code info} construction belongs at the security-policy layer because those bytes are part of
 * the OPC UA wire contract, not part of generic HKDF.
 */
@NullMarked
public final class HkdfUtil {

  private static final String HMAC_SHA256 = "HmacSHA256";
  private static final String HMAC_SHA384 = "HmacSHA384";
  private static final int SHA256_LENGTH = 32;
  private static final int SHA384_LENGTH = 48;

  private HkdfUtil() {}

  /**
   * Derive key material with HKDF-SHA-256.
   *
   * <p>Use this overload when normal JCA provider lookup is acceptable.
   *
   * @param ikm the input keying material.
   * @param salt the HKDF salt; an empty salt uses the RFC 5869 default.
   * @param info the HKDF context information.
   * @param length the number of output bytes to derive.
   * @return the derived output keying material.
   * @throws UaException if the configured provider cannot complete HMAC-SHA-256.
   */
  public static byte[] hkdfSha256(byte[] ikm, byte[] salt, byte[] info, int length)
      throws UaException {

    return hkdfSha256(ikm, salt, info, length, null);
  }

  /**
   * Derive key material with HKDF-SHA-256 using a specific JCA provider.
   *
   * <p>Use this overload when policy/provider resolution has already selected the provider family
   * that should perform HMAC-SHA-256.
   *
   * @param ikm the input keying material.
   * @param salt the HKDF salt; an empty salt uses the RFC 5869 default.
   * @param info the HKDF context information.
   * @param length the number of output bytes to derive.
   * @param provider the provider to use for HMAC-SHA-256, or {@code null} for normal JCA lookup.
   * @return the derived output keying material.
   * @throws UaException if the configured provider cannot complete HMAC-SHA-256.
   */
  public static byte[] hkdfSha256(
      byte[] ikm, byte[] salt, byte[] info, int length, @Nullable Provider provider)
      throws UaException {

    return hkdf(HMAC_SHA256, SHA256_LENGTH, ikm, salt, info, length, provider);
  }

  /**
   * Derive key material with HKDF-SHA-384.
   *
   * <p>Use this overload when normal JCA provider lookup is acceptable.
   *
   * @param ikm the input keying material.
   * @param salt the HKDF salt; an empty salt uses the RFC 5869 default.
   * @param info the HKDF context information.
   * @param length the number of output bytes to derive.
   * @return the derived output keying material.
   * @throws UaException if the configured provider cannot complete HMAC-SHA-384.
   */
  public static byte[] hkdfSha384(byte[] ikm, byte[] salt, byte[] info, int length)
      throws UaException {

    return hkdfSha384(ikm, salt, info, length, null);
  }

  /**
   * Derive key material with HKDF-SHA-384 using a specific JCA provider.
   *
   * <p>Use this overload when policy/provider resolution has already selected the provider family
   * that should perform HMAC-SHA-384.
   *
   * @param ikm the input keying material.
   * @param salt the HKDF salt; an empty salt uses the RFC 5869 default.
   * @param info the HKDF context information.
   * @param length the number of output bytes to derive.
   * @param provider the provider to use for HMAC-SHA-384, or {@code null} for normal JCA lookup.
   * @return the derived output keying material.
   * @throws UaException if the configured provider cannot complete HMAC-SHA-384.
   */
  public static byte[] hkdfSha384(
      byte[] ikm, byte[] salt, byte[] info, int length, @Nullable Provider provider)
      throws UaException {

    return hkdf(HMAC_SHA384, SHA384_LENGTH, ikm, salt, info, length, provider);
  }

  private static byte[] hkdf(
      String transformation,
      int hashLength,
      byte[] ikm,
      byte[] salt,
      byte[] info,
      int length,
      @Nullable Provider provider)
      throws UaException {

    requireNonNull(ikm, "ikm");
    requireNonNull(salt, "salt");
    requireNonNull(info, "info");

    if (length < 0 || length > 255 * hashLength) {
      throw new IllegalArgumentException("length: " + length);
    }

    try {
      byte[] effectiveSalt = salt.length > 0 ? salt : new byte[hashLength];
      byte[] prk = hmac(transformation, effectiveSalt, ikm, provider);
      try {
        byte[] okm = new byte[length];
        byte[] previous = new byte[0];
        int offset = 0;

        for (int i = 1; offset < length; i++) {
          Mac mac = mac(transformation, provider);
          mac.init(new SecretKeySpec(prk, transformation));
          mac.update(previous);
          mac.update(info);
          mac.update((byte) i);

          previous = mac.doFinal();

          int toCopy = Math.min(previous.length, length - offset);
          System.arraycopy(previous, 0, okm, offset, toCopy);
          offset += toCopy;
        }

        return okm;
      } finally {
        Arrays.fill(prk, (byte) 0);
      }
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  private static byte[] hmac(
      String transformation, byte[] key, byte[] input, @Nullable Provider provider)
      throws GeneralSecurityException {

    Mac mac = mac(transformation, provider);

    mac.init(new SecretKeySpec(key, transformation));

    return mac.doFinal(input);
  }

  private static Mac mac(String transformation, @Nullable Provider provider)
      throws GeneralSecurityException {

    return provider != null
        ? Mac.getInstance(transformation, provider)
        : Mac.getInstance(transformation);
  }
}
