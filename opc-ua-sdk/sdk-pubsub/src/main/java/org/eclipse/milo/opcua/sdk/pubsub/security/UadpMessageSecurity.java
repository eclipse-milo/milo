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

import io.netty.buffer.ByteBuf;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;

/**
 * The single crypto choke point for UADP message security: AES-CTR payload transformation and HMAC
 * signing/verification per Part 14 v1.05 §7.2.4.4.3.
 *
 * <p>The UADP codec calls these helpers rather than instantiating {@link Cipher}/{@code Mac}
 * itself, so the cryptographic mechanics are implemented and vector-tested exactly once.
 */
public final class UadpMessageSecurity {

  private UadpMessageSecurity() {}

  /**
   * Transform (encrypt or decrypt) the readable bytes of {@code region} in place with AES-CTR.
   *
   * <p>The counter block (Part 14 Table 157) is {@code KeyNonce(4) || MessageNonce(8) ||
   * BlockCounter(4)}, with the 32-bit big-endian block counter starting at 1; CTR is symmetric, so
   * one method serves both encryption and decryption.
   *
   * <p>Buffer contract: all readable bytes of the passed view — {@code [readerIndex, writerIndex)}
   * — are transformed in place using absolute get/set through a scratch array (the buffer may be
   * direct); the reader and writer indices are unchanged on return; the scratch array and the
   * counter block are wiped in a {@code finally} block. Callers transform a sub-region by passing a
   * {@code slice(...)} of the message buffer (slices share memory, so "in place" holds).
   *
   * @param keys the {@link SecurityKeyMaterial} providing the encrypting key and key nonce.
   * @param messageNonce the MessageNonce from the SecurityHeader; must be exactly {@link
   *     PubSubSecurityPolicy#getMessageNonceLength()} (8) bytes.
   * @param region the buffer region to transform in place.
   * @throws UaException with {@code Bad_SecurityChecksFailed} if {@code messageNonce} has the wrong
   *     length or the cipher operation fails; with {@code Bad_InternalError} if the cipher
   *     transformation is unavailable.
   */
  public static void ctrTransform(SecurityKeyMaterial keys, byte[] messageNonce, ByteBuf region)
      throws UaException {

    PubSubSecurityPolicy policy = keys.getPolicy();

    if (messageNonce.length != policy.getMessageNonceLength()) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed,
          "messageNonce must be %d bytes, got %d"
              .formatted(policy.getMessageNonceLength(), messageNonce.length));
    }

    int length = region.readableBytes();
    if (length == 0) {
      return;
    }

    byte[] keyNonce = keys.getKeyNonce();

    // IV = KeyNonce(4) || MessageNonce(8) || BlockCounter(4), counter starting at 1 (big-endian).
    byte[] iv = new byte[16];
    System.arraycopy(keyNonce, 0, iv, 0, keyNonce.length);
    System.arraycopy(messageNonce, 0, iv, keyNonce.length, messageNonce.length);
    iv[15] = 0x01;

    byte[] scratch = new byte[length];
    try {
      region.getBytes(region.readerIndex(), scratch);

      Cipher cipher =
          Cipher.getInstance(policy.getSymmetricEncryptionAlgorithm().getTransformation());
      cipher.init(
          Cipher.ENCRYPT_MODE,
          new SecretKeySpec(keys.getEncryptingKey(), "AES"),
          new IvParameterSpec(iv));

      int transformed = cipher.doFinal(scratch, 0, length, scratch, 0);
      if (transformed != length) {
        throw new UaException(
            StatusCodes.Bad_InternalError,
            "AES-CTR transformed %d bytes, expected %d".formatted(transformed, length));
      }

      region.setBytes(region.readerIndex(), scratch);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new UaException(StatusCodes.Bad_InternalError, e);
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    } finally {
      Arrays.fill(scratch, (byte) 0);
      Arrays.fill(iv, (byte) 0);
    }
  }

  /**
   * Compute the HMAC signature of the given buffers with the policy's symmetric signature algorithm
   * (HMAC-SHA2-256).
   *
   * @param keys the {@link SecurityKeyMaterial} providing the signing key.
   * @param buffers the buffers to sign; each is consumed from its position to its limit.
   * @return the signature ({@link PubSubSecurityPolicy#getSignatureLength()} bytes).
   * @throws UaException if the MAC operation fails.
   */
  public static byte[] sign(SecurityKeyMaterial keys, ByteBuffer... buffers) throws UaException {
    return SignatureUtil.hmac(
        keys.getPolicy().getSymmetricSignatureAlgorithm(), keys.getSigningKey(), buffers);
  }

  /**
   * Verify an HMAC signature over the given buffers.
   *
   * <p>The comparison uses {@link MessageDigest#isEqual(byte[], byte[])}, which is constant-time.
   *
   * @param keys the {@link SecurityKeyMaterial} providing the signing key.
   * @param signature the received signature to verify.
   * @param buffers the signed buffers; each is consumed from its position to its limit.
   * @return {@code true} if {@code signature} matches the computed signature.
   * @throws UaException if the MAC operation fails.
   */
  public static boolean verify(SecurityKeyMaterial keys, byte[] signature, ByteBuffer... buffers)
      throws UaException {

    byte[] computed = sign(keys, buffers);

    return MessageDigest.isEqual(computed, signature);
  }
}
