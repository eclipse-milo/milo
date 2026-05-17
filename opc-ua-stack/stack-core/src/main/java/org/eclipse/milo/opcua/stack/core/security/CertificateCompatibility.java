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

import static java.util.Objects.requireNonNull;

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.EdECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.NamedParameterSpec;
import java.util.Objects;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.validation.CertificateValidationUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Checks whether application certificates and local identities are compatible with security policy
 * profiles.
 *
 * <p>Enhanced endpoint advertisement and SecureChannel setup should only use a certificate when the
 * certificate type ID, public key family, and end-entity key usage all match the selected profile.
 * These checks keep visually similar keys, such as NIST and Brainpool curves with the same
 * coordinate size, from being selected for endpoints that cannot complete the matching handshake.
 */
@NullMarked
public final class CertificateCompatibility {

  private static final ECParameterSpec NIST_P256 = nistP256ParameterSpec();
  private static final ECParameterSpec NIST_P384 = ecParameterSpec("secp384r1", null);
  private static final ECParameterSpec BRAINPOOL_P256R1 =
      ecParameterSpec("brainpoolP256r1", new BouncyCastleProvider());
  private static final ECParameterSpec BRAINPOOL_P384R1 =
      ecParameterSpec("brainpoolP384r1", new BouncyCastleProvider());

  private CertificateCompatibility() {}

  /**
   * Check whether a local identity can be used with {@code securityPolicyProfile}.
   *
   * @param securityPolicyProfile the profile the identity will be used with.
   * @param identity the local certificate identity to check.
   * @return {@code true} if the identity is compatible.
   */
  public static boolean isCompatible(
      SecurityPolicyProfile securityPolicyProfile, CertificateIdentity identity) {

    try {
      checkCompatible(securityPolicyProfile, identity);
      return true;
    } catch (UaException | RuntimeException e) {
      return false;
    }
  }

  /**
   * Check whether a local identity can be used with {@code securityPolicyProfile}.
   *
   * @param securityPolicyProfile the profile the identity will be used with.
   * @param identity the local certificate identity to check.
   * @throws UaException if the identity is not compatible.
   */
  public static void checkCompatible(
      SecurityPolicyProfile securityPolicyProfile, CertificateIdentity identity)
      throws UaException {

    requireNonNull(identity, "identity");
    checkCompatible(securityPolicyProfile, identity.certificateTypeId(), identity.certificate());
  }

  /**
   * Check whether a certificate can be used with {@code securityPolicyProfile}.
   *
   * <p>This overload validates the certificate public key and policy-specific application
   * certificate usage. It does not validate certificate type ID compatibility; remote certificate
   * validation paths may not know the certificate type ID that a peer associated with the
   * certificate.
   *
   * @param securityPolicyProfile the profile the certificate will be used with.
   * @param certificate the certificate to check.
   * @throws UaException if the certificate is not compatible.
   */
  public static void checkCompatible(
      SecurityPolicyProfile securityPolicyProfile, X509Certificate certificate) throws UaException {

    requireNonNull(securityPolicyProfile, "securityPolicyProfile");
    requireNonNull(certificate, "certificate");

    if (securityPolicyProfile.authAxis() == AuthAxis.NONE) {
      return;
    }

    checkPublicKey(securityPolicyProfile.authAxis(), certificate.getPublicKey());
    CertificateValidationUtil.checkEndEntityKeyUsage(certificate, securityPolicyProfile);
  }

  /**
   * Check whether a certificate type and certificate can be used with {@code
   * securityPolicyProfile}.
   *
   * @param securityPolicyProfile the profile the certificate will be used with.
   * @param certificateTypeId the certificate type ID associated with {@code certificate}.
   * @param certificate the certificate to check.
   * @throws UaException if the certificate is not compatible.
   */
  public static void checkCompatible(
      SecurityPolicyProfile securityPolicyProfile,
      NodeId certificateTypeId,
      X509Certificate certificate)
      throws UaException {

    requireNonNull(securityPolicyProfile, "securityPolicyProfile");
    requireNonNull(certificateTypeId, "certificateTypeId");
    requireNonNull(certificate, "certificate");

    if (!securityPolicyProfile.certificateTypeIds().isEmpty()
        && !securityPolicyProfile.certificateTypeIds().contains(certificateTypeId)) {

      throw new UaException(
          StatusCodes.Bad_CertificateUseNotAllowed,
          "certificate type is not compatible with security policy: "
              + securityPolicyProfile.securityPolicy().getUri());
    }

    checkCompatible(securityPolicyProfile, certificate);
  }

  private static void checkPublicKey(AuthAxis authAxis, PublicKey publicKey) throws UaException {
    switch (authAxis) {
      case NONE -> {}
      case RSA_PKCS1_SHA1, RSA_PKCS1_SHA256, RSA_PSS_SHA256 -> {
        if (!(publicKey instanceof RSAPublicKey)) {
          throw incompatiblePublicKey(authAxis, publicKey);
        }
      }
      case ECDSA_NIST_P256_SHA256 -> {
        if (!(publicKey instanceof ECPublicKey ecPublicKey)
            || isDifferentParameterSpec(NIST_P256, ecPublicKey.getParams())) {

          throw incompatiblePublicKey(authAxis, publicKey);
        }
      }
      case ECDSA_NIST_P384_SHA384 -> {
        if (!(publicKey instanceof ECPublicKey ecPublicKey)
            || isDifferentParameterSpec(NIST_P384, ecPublicKey.getParams())) {

          throw incompatiblePublicKey(authAxis, publicKey);
        }
      }
      case ECDSA_BRAINPOOL_P256R1_SHA256 -> {
        if (!(publicKey instanceof ECPublicKey ecPublicKey)
            || isDifferentParameterSpec(BRAINPOOL_P256R1, ecPublicKey.getParams())) {

          throw incompatiblePublicKey(authAxis, publicKey);
        }
      }
      case ECDSA_BRAINPOOL_P384R1_SHA384 -> {
        if (!(publicKey instanceof ECPublicKey ecPublicKey)
            || isDifferentParameterSpec(BRAINPOOL_P384R1, ecPublicKey.getParams())) {

          throw incompatiblePublicKey(authAxis, publicKey);
        }
      }
      case ED25519 -> {
        if (!isEd25519(publicKey)) {
          throw incompatiblePublicKey(authAxis, publicKey);
        }
      }
    }
  }

  private static UaException incompatiblePublicKey(AuthAxis authAxis, PublicKey publicKey) {
    return new UaException(
        StatusCodes.Bad_CertificateUseNotAllowed,
        String.format(
            "certificate public key algorithm '%s' is not compatible with %s",
            publicKey.getAlgorithm(), authAxis));
  }

  private static boolean isEd25519(PublicKey publicKey) {
    if (publicKey instanceof EdECPublicKey edPublicKey) {
      return NamedParameterSpec.ED25519.getName().equals(edPublicKey.getParams().getName());
    } else {
      return "Ed25519".equalsIgnoreCase(publicKey.getAlgorithm());
    }
  }

  private static ECParameterSpec nistP256ParameterSpec() {
    return ecParameterSpec("secp256r1", null);
  }

  private static ECParameterSpec ecParameterSpec(String curveName, @Nullable Provider provider) {
    try {
      AlgorithmParameters parameters =
          provider != null
              ? AlgorithmParameters.getInstance("EC", provider)
              : AlgorithmParameters.getInstance("EC");

      parameters.init(new ECGenParameterSpec(curveName));
      return parameters.getParameterSpec(ECParameterSpec.class);
    } catch (GeneralSecurityException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private static boolean isDifferentParameterSpec(
      ECParameterSpec expected, ECParameterSpec parameterSpec) {

    return !Objects.equals(expected.getCurve(), parameterSpec.getCurve())
        || !Objects.equals(expected.getGenerator(), parameterSpec.getGenerator())
        || !Objects.equals(expected.getOrder(), parameterSpec.getOrder())
        || expected.getCofactor() != parameterSpec.getCofactor();
  }
}
