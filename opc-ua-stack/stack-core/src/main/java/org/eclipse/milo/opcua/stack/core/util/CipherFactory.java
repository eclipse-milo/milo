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

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.jspecify.annotations.NullMarked;

/** Creates RSA ciphers with the parameters required by OPC UA security algorithms. */
@NullMarked
public final class CipherFactory {

  private CipherFactory() {}

  /**
   * Creates an initialized asymmetric encryption cipher using the configured JCA providers.
   *
   * @param algorithm the asymmetric encryption algorithm.
   * @param publicKey the receiver's public key.
   * @return the initialized cipher.
   * @throws GeneralSecurityException if the cipher cannot be created or initialized.
   */
  public static Cipher createForEncryption(SecurityAlgorithm algorithm, PublicKey publicKey)
      throws GeneralSecurityException {
    return create(algorithm, Cipher.ENCRYPT_MODE, publicKey);
  }

  /**
   * Creates an initialized asymmetric decryption cipher using the configured JCA providers.
   *
   * @param algorithm the asymmetric encryption algorithm.
   * @param privateKey the receiver's private key.
   * @return the initialized cipher.
   * @throws GeneralSecurityException if the cipher cannot be created or initialized.
   */
  public static Cipher createForDecryption(SecurityAlgorithm algorithm, PrivateKey privateKey)
      throws GeneralSecurityException {
    return create(algorithm, Cipher.DECRYPT_MODE, privateKey);
  }

  private static Cipher create(SecurityAlgorithm algorithm, int mode, Key key)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(algorithm.getTransformation());
    AlgorithmParameterSpec parameters = algorithm.getAlgorithmParameterSpec();
    if (parameters != null) {
      cipher.init(mode, key, parameters);
    } else {
      cipher.init(mode, key);
    }
    return cipher;
  }
}
