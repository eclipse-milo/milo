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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.jspecify.annotations.NullMarked;

/** Creates RSA signatures with the parameters required by OPC UA security algorithms. */
@NullMarked
public final class SignatureFactory {

  private SignatureFactory() {}

  /**
   * Creates a signature initialized for signing using the configured JCA providers.
   *
   * @param algorithm the asymmetric signature algorithm.
   * @param privateKey the signer's private key.
   * @return the initialized signature.
   * @throws GeneralSecurityException if the signature cannot be created or initialized.
   */
  public static Signature createForSigning(SecurityAlgorithm algorithm, PrivateKey privateKey)
      throws GeneralSecurityException {
    Signature signature = Signature.getInstance(algorithm.getTransformation());
    signature.initSign(privateKey);
    applyParameters(signature, algorithm);
    return signature;
  }

  /**
   * Creates a signature initialized for verification using the configured JCA providers.
   *
   * @param algorithm the asymmetric signature algorithm.
   * @param publicKey the signer's public key.
   * @return the initialized signature.
   * @throws GeneralSecurityException if the signature cannot be created or initialized.
   */
  public static Signature createForVerification(SecurityAlgorithm algorithm, PublicKey publicKey)
      throws GeneralSecurityException {
    Signature signature = Signature.getInstance(algorithm.getTransformation());
    signature.initVerify(publicKey);
    applyParameters(signature, algorithm);
    return signature;
  }

  /**
   * Creates a signature initialized for verification using the configured JCA providers.
   *
   * <p>Unlike the {@link PublicKey} overload, this rejects X.509 certificates whose critical key
   * usage extension excludes digital signatures.
   *
   * @param algorithm the asymmetric signature algorithm.
   * @param certificate the signer's certificate.
   * @return the initialized signature.
   * @throws GeneralSecurityException if the signature cannot be created or initialized.
   */
  public static Signature createForVerification(
      SecurityAlgorithm algorithm, Certificate certificate) throws GeneralSecurityException {
    Signature signature = Signature.getInstance(algorithm.getTransformation());
    signature.initVerify(certificate);
    applyParameters(signature, algorithm);
    return signature;
  }

  // Parameters are applied after init so delayed provider selection can consider the key type.
  private static void applyParameters(Signature signature, SecurityAlgorithm algorithm)
      throws GeneralSecurityException {
    AlgorithmParameterSpec parameters = algorithm.getAlgorithmParameterSpec();
    if (parameters != null) {
      signature.setParameter(parameters);
    }
  }
}
