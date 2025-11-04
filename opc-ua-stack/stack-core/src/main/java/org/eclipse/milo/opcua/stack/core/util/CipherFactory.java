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

import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;

/**
 * Factory for creating {@link Cipher} instances configured for OPC UA security operations.
 *
 * <p>This utility class creates and initializes ciphers for asymmetric encryption and decryption
 * used in OPC UA secure channels and identity token encryption. The ciphers are configured based on
 * the {@link SecurityAlgorithm} which provides the transformation and optional algorithm
 * parameters.
 */
public class CipherFactory {

  private CipherFactory() {}

  /**
   * Creates a cipher initialized for encryption using the specified algorithm and public key.
   *
   * <p>The cipher is configured in {@link Cipher#ENCRYPT_MODE} and initialized with the provided
   * public key. If the security algorithm specifies algorithm parameters, they are included in the
   * initialization.
   *
   * @param algorithm the security algorithm defining the transformation and optional parameters.
   * @param publicKey the public key to use for encryption.
   * @return a cipher initialized for encryption.
   * @throws UaException with status code {@link StatusCodes#Bad_SecurityChecksFailed} if cipher
   *     initialization fails.
   */
  public static Cipher createForEncryption(SecurityAlgorithm algorithm, PublicKey publicKey)
      throws UaException {

    String transformation = algorithm.getTransformation();
    AlgorithmParameterSpec parameterSpec = algorithm.getAlgorithmParameterSpec();

    try {
      Cipher cipher = Cipher.getInstance(transformation);

      if (parameterSpec != null) {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, parameterSpec);
      } else {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      }

      return cipher;
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "failed to initialize cipher", e);
    }
  }

  /**
   * Creates a cipher initialized for decryption using the specified algorithm and private key.
   *
   * <p>The cipher is configured in {@link Cipher#DECRYPT_MODE} and initialized with the provided
   * private key. If the security algorithm specifies algorithm parameters, they are included in the
   * initialization.
   *
   * @param algorithm the security algorithm defining the transformation and optional parameters.
   * @param privateKey the private key to use for decryption.
   * @return a cipher initialized for decryption.
   * @throws UaException with status code {@link StatusCodes#Bad_SecurityChecksFailed} if cipher
   *     initialization fails.
   */
  public static Cipher createForDecryption(SecurityAlgorithm algorithm, PrivateKey privateKey)
      throws UaException {

    try {
      String transformation = algorithm.getTransformation();
      AlgorithmParameterSpec parameterSpec = algorithm.getAlgorithmParameterSpec();

      Cipher cipher = Cipher.getInstance(transformation);

      if (parameterSpec != null) {
        cipher.init(Cipher.DECRYPT_MODE, privateKey, parameterSpec);
      } else {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
      }

      return cipher;
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "failed to initialize cipher", e);
    }
  }
}
