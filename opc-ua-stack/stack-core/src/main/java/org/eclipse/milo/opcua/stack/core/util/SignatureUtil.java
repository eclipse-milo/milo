/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;

public class SignatureUtil {

  /**
   * Sign the contents of the provided buffers using the provided {@link SecurityAlgorithm}. Note
   * that only the bytes between position and limit of each buffer are considered.
   *
   * @param securityAlgorithm the {@link SecurityAlgorithm}.
   * @param privateKey the {@link PrivateKey} to sign with.
   * @param buffers the data to sign.
   * @return the signature bytes.
   * @throws UaException if the signature fails for any reason.
   */
  public static byte[] sign(
      SecurityAlgorithm securityAlgorithm, PrivateKey privateKey, ByteBuffer... buffers)
      throws UaException {

    String transformation = securityAlgorithm.getTransformation();

    try {
      Signature signature = Signature.getInstance(transformation);
      signature.initSign(privateKey);

      for (ByteBuffer buffer : buffers) {
        signature.update(buffer);
      }

      return signature.sign();
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_InternalError, e);
    }
  }

  /**
   * Verify that {@code signatureBytes} is a valid signature produced by the private key of {@code
   * certificate} having signed {@code dataBytes} using {@code algorithm}.
   *
   * @param algorithm the {@link SecurityAlgorithm} used to create the signature.
   * @param certificate the {@link X509Certificate} used to verify the signature.
   * @param dataBytes the bytes the {@code signatureBytes} was produced from.
   * @param signatureBytes the signature created by signing {@code dataBytes}.
   * @throws UaException if verification fails for any reason.
   */
  public static void verify(
      SecurityAlgorithm algorithm,
      X509Certificate certificate,
      byte[] dataBytes,
      byte[] signatureBytes)
      throws UaException {

    try {
      Signature signature = Signature.getInstance(algorithm.getTransformation());
      signature.initVerify(certificate);

      signature.update(dataBytes);

      if (!signature.verify(signatureBytes)) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "could not verify signature");
      }
    } catch (NoSuchAlgorithmException | SignatureException e) {
      throw new UaException(StatusCodes.Bad_InternalError, e);
    } catch (InvalidKeyException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  /**
   * Compute the HMAC of the provided buffers.
   *
   * @param securityAlgorithm the {@link SecurityAlgorithm} that provides the transformation for
   *     {@link Mac#getInstance(String)}}.
   * @param secretKey the secret key.
   * @param buffers the buffers to use.
   * @return the computed HMAC.
   * @throws UaException if the HMAC operation fails for any reason.
   */
  public static byte[] hmac(
      SecurityAlgorithm securityAlgorithm, byte[] secretKey, ByteBuffer... buffers)
      throws UaException {

    String transformation = securityAlgorithm.getTransformation();

    try {
      Mac mac = getMacInstance(transformation);

      mac.init(new SecretKeySpec(secretKey, transformation));

      for (ByteBuffer buffer : buffers) {
        mac.update(buffer);
      }

      return mac.doFinal();
    } catch (NoSuchAlgorithmException e) {
      throw new UaException(StatusCodes.Bad_InternalError, e);
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  private static final ThreadLocal<Map<String, Mac>> MAC_INSTANCES = new ThreadLocal<>();

  private static Mac getMacInstance(String transformation) throws NoSuchAlgorithmException {
    Map<String, Mac> macs = MAC_INSTANCES.get();
    if (macs == null) {
      macs = new HashMap<>();
      MAC_INSTANCES.set(macs);
    }

    Mac mac = macs.get(transformation);
    if (mac == null) {
      mac = Mac.getInstance(transformation);
      macs.put(transformation, mac);
    }
    return mac;
  }
}
