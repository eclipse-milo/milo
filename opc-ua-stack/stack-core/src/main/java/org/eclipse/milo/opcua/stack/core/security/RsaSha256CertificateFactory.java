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

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;

/**
 * Base {@link CertificateFactory} for Milo-managed application certificate types.
 *
 * <p>Subclasses provide the certificate chains for generated key pairs. The default key-pair and
 * signing-request helpers support RSA SHA-256 and the current ECC application certificate types.
 */
public abstract class RsaSha256CertificateFactory implements CertificateFactory {

  private static final int DEFAULT_KEY_LENGTH = 2048;

  @Override
  public KeyPair createKeyPair(NodeId certificateTypeId) throws GeneralSecurityException {
    if (certificateTypeId.equals(NodeIds.RsaSha256ApplicationCertificateType)) {
      return createRsaSha256KeyPair();
    } else if (certificateTypeId.equals(NodeIds.EccNistP256ApplicationCertificateType)) {
      return createEccNistP256KeyPair();
    } else if (certificateTypeId.equals(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)) {
      return createEccBrainpoolP384r1KeyPair();
    } else if (certificateTypeId.equals(NodeIds.EccCurve25519ApplicationCertificateType)) {
      return createEccCurve25519KeyPair();
    } else {
      throw new UnsupportedOperationException("certificateTypeId: " + certificateTypeId);
    }
  }

  @Override
  public X509Certificate[] createCertificateChain(NodeId certificateTypeId, KeyPair keyPair)
      throws Exception {
    if (certificateTypeId.equals(NodeIds.RsaSha256ApplicationCertificateType)) {
      return createRsaSha256CertificateChain(keyPair);
    } else if (certificateTypeId.equals(NodeIds.EccNistP256ApplicationCertificateType)) {
      return createEccNistP256CertificateChain(keyPair);
    } else if (certificateTypeId.equals(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)) {
      return createEccBrainpoolP384r1CertificateChain(keyPair);
    } else if (certificateTypeId.equals(NodeIds.EccCurve25519ApplicationCertificateType)) {
      return createEccCurve25519CertificateChain(keyPair);
    } else {
      throw new UnsupportedOperationException("certificateTypeId: " + certificateTypeId);
    }
  }

  @Override
  public ByteString createSigningRequest(
      NodeId certificateTypeId,
      KeyPair keyPair,
      X500Name subjectName,
      String sanUri,
      List<String> dnsNames,
      List<String> ipAddresses)
      throws Exception {

    if (certificateTypeId.equals(NodeIds.RsaSha256ApplicationCertificateType)) {
      return createRsaSha256SigningRequest(keyPair, subjectName, sanUri, dnsNames, ipAddresses);
    } else if (certificateTypeId.equals(NodeIds.EccNistP256ApplicationCertificateType)) {
      return createEccNistP256SigningRequest(keyPair, subjectName, sanUri, dnsNames, ipAddresses);
    } else if (certificateTypeId.equals(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)) {
      return createEccBrainpoolP384r1SigningRequest(
          keyPair, subjectName, sanUri, dnsNames, ipAddresses);
    } else if (certificateTypeId.equals(NodeIds.EccCurve25519ApplicationCertificateType)) {
      return createEccCurve25519SigningRequest(keyPair, subjectName, sanUri, dnsNames, ipAddresses);
    } else {
      throw new UnsupportedOperationException("certificateTypeId: " + certificateTypeId);
    }
  }

  /**
   * Create a new {@link KeyPair} for a {@link NodeIds#RsaSha256ApplicationCertificateType} type
   * certificate. The key length must be between 2048 and 4096 bits.
   *
   * @return the new {@link KeyPair}.
   */
  protected KeyPair createRsaSha256KeyPair() throws NoSuchAlgorithmException {
    return SelfSignedCertificateGenerator.generateRsaKeyPair(DEFAULT_KEY_LENGTH);
  }

  /**
   * Create a new NIST P-256 ECDSA {@link KeyPair} for a {@link
   * NodeIds#EccNistP256ApplicationCertificateType} type certificate.
   *
   * @return the new {@link KeyPair}.
   */
  protected KeyPair createEccNistP256KeyPair() throws GeneralSecurityException {
    return SelfSignedCertificateGenerator.generateNistP256KeyPair();
  }

  /**
   * Create a new Brainpool P-384r1 ECDSA {@link KeyPair} for a {@link
   * NodeIds#EccBrainpoolP384r1ApplicationCertificateType} type certificate.
   *
   * <p>Brainpool P-384r1 key generation is routed through Bouncy Castle so the generated key can be
   * used consistently for certificates, CSRs, and SecureChannel compatibility checks.
   *
   * @return the new {@link KeyPair}.
   */
  protected KeyPair createEccBrainpoolP384r1KeyPair() throws GeneralSecurityException {
    return SelfSignedCertificateGenerator.generateBrainpoolP384r1KeyPair();
  }

  /**
   * Create a new Ed25519 {@link KeyPair} for a {@link
   * NodeIds#EccCurve25519ApplicationCertificateType} type certificate.
   *
   * <p>The Curve25519 certificate type uses Ed25519 application certificates. X25519 is only used
   * as an ephemeral OpenSecureChannel key-agreement key.
   *
   * @return the new {@link KeyPair}.
   */
  protected KeyPair createEccCurve25519KeyPair() throws NoSuchAlgorithmException {
    return SelfSignedCertificateGenerator.generateEd25519KeyPair();
  }

  /**
   * Create a new {@link X509Certificate} chain of type {@link
   * NodeIds#RsaSha256ApplicationCertificateType}. The chain may be of length 1 if the certificate
   * is self-signed.
   *
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   * @throws Exception if an error occurs while creating the certificate chain.
   */
  protected abstract X509Certificate[] createRsaSha256CertificateChain(KeyPair keyPair)
      throws Exception;

  /**
   * Create a new {@link X509Certificate} chain of type {@link
   * NodeIds#EccNistP256ApplicationCertificateType}.
   *
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   * @throws Exception if an error occurs while creating the certificate chain.
   */
  @SuppressWarnings({"unused", "RedundantThrows"})
  protected X509Certificate[] createEccNistP256CertificateChain(KeyPair keyPair) throws Exception {
    throw new UnsupportedOperationException(
        "certificateTypeId: " + NodeIds.EccNistP256ApplicationCertificateType);
  }

  /**
   * Create a new {@link X509Certificate} chain of type {@link
   * NodeIds#EccBrainpoolP384r1ApplicationCertificateType}.
   *
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   * @throws Exception if an error occurs while creating the certificate chain.
   */
  @SuppressWarnings({"unused", "RedundantThrows"})
  protected X509Certificate[] createEccBrainpoolP384r1CertificateChain(KeyPair keyPair)
      throws Exception {

    throw new UnsupportedOperationException(
        "certificateTypeId: " + NodeIds.EccBrainpoolP384r1ApplicationCertificateType);
  }

  /**
   * Create a new {@link X509Certificate} chain of type {@link
   * NodeIds#EccCurve25519ApplicationCertificateType}.
   *
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   * @throws Exception if an error occurs while creating the certificate chain.
   */
  @SuppressWarnings({"unused", "RedundantThrows"})
  protected X509Certificate[] createEccCurve25519CertificateChain(KeyPair keyPair)
      throws Exception {

    throw new UnsupportedOperationException(
        "certificateTypeId: " + NodeIds.EccCurve25519ApplicationCertificateType);
  }

  /**
   * Create a new PKCS10 certificate signing request for a {@link
   * NodeIds#RsaSha256ApplicationCertificateType} type certificate.
   *
   * @param keyPair the {@link KeyPair} to use when creating the signing request.
   * @param subjectName the {@link X500Name} to request.
   * @param sanUri the URI to request in the Subject Alternative Name of the CSR.
   * @param dnsNames the DNS names to request in the Subject Alternative Name of the CSR.
   * @param ipAddresses the IP addresses to request in the Subject Alternative Name of the CSR.
   * @return the new {@link ByteString} containing the DER-encoded PKCS10 signing request.
   * @throws Exception if an error occurs while creating the signing request.
   */
  protected ByteString createRsaSha256SigningRequest(
      KeyPair keyPair,
      X500Name subjectName,
      String sanUri,
      List<String> dnsNames,
      List<String> ipAddresses)
      throws Exception {

    PKCS10CertificationRequest csr =
        CertificateUtil.generateCsr(
            keyPair, subjectName, sanUri, dnsNames, ipAddresses, "SHA256withRSA");

    return ByteString.of(csr.getEncoded());
  }

  /**
   * Create a new PKCS10 certificate signing request for a {@link
   * NodeIds#EccNistP256ApplicationCertificateType} type certificate.
   *
   * @param keyPair the {@link KeyPair} to use when creating the signing request.
   * @param subjectName the {@link X500Name} to request.
   * @param sanUri the URI to request in the Subject Alternative Name of the CSR.
   * @param dnsNames the DNS names to request in the Subject Alternative Name of the CSR.
   * @param ipAddresses the IP addresses to request in the Subject Alternative Name of the CSR.
   * @return the new {@link ByteString} containing the DER-encoded PKCS10 signing request.
   * @throws Exception if an error occurs while creating the signing request.
   */
  protected ByteString createEccNistP256SigningRequest(
      KeyPair keyPair,
      X500Name subjectName,
      String sanUri,
      List<String> dnsNames,
      List<String> ipAddresses)
      throws Exception {

    PKCS10CertificationRequest csr =
        CertificateUtil.generateCsr(
            keyPair,
            subjectName,
            sanUri,
            dnsNames,
            ipAddresses,
            "SHA256withECDSA",
            KeyUsage.digitalSignature,
            null);

    return ByteString.of(csr.getEncoded());
  }

  /**
   * Create a new PKCS10 certificate signing request for a {@link
   * NodeIds#EccBrainpoolP384r1ApplicationCertificateType} type certificate.
   *
   * <p>The CSR signer uses Bouncy Castle explicitly because default JCA provider lookup may choose
   * a provider that does not understand the Brainpool curve carried by {@code keyPair}.
   *
   * @param keyPair the {@link KeyPair} to use when creating the signing request.
   * @param subjectName the {@link X500Name} to request.
   * @param sanUri the URI to request in the Subject Alternative Name of the CSR.
   * @param dnsNames the DNS names to request in the Subject Alternative Name of the CSR.
   * @param ipAddresses the IP addresses to request in the Subject Alternative Name of the CSR.
   * @return the new {@link ByteString} containing the DER-encoded PKCS10 signing request.
   * @throws Exception if an error occurs while creating the signing request.
   */
  protected ByteString createEccBrainpoolP384r1SigningRequest(
      KeyPair keyPair,
      X500Name subjectName,
      String sanUri,
      List<String> dnsNames,
      List<String> ipAddresses)
      throws Exception {

    PKCS10CertificationRequest csr =
        CertificateUtil.generateCsr(
            keyPair,
            subjectName,
            sanUri,
            dnsNames,
            ipAddresses,
            "SHA384withECDSA",
            KeyUsage.digitalSignature,
            null,
            new BouncyCastleProvider());

    return ByteString.of(csr.getEncoded());
  }

  /**
   * Create a new PKCS10 certificate signing request for a {@link
   * NodeIds#EccCurve25519ApplicationCertificateType} type certificate.
   *
   * @param keyPair the {@link KeyPair} to use when creating the signing request.
   * @param subjectName the {@link X500Name} to request.
   * @param sanUri the URI to request in the Subject Alternative Name of the CSR.
   * @param dnsNames the DNS names to request in the Subject Alternative Name of the CSR.
   * @param ipAddresses the IP addresses to request in the Subject Alternative Name of the CSR.
   * @return the new {@link ByteString} containing the DER-encoded PKCS10 signing request.
   * @throws Exception if an error occurs while creating the signing request.
   */
  protected ByteString createEccCurve25519SigningRequest(
      KeyPair keyPair,
      X500Name subjectName,
      String sanUri,
      List<String> dnsNames,
      List<String> ipAddresses)
      throws Exception {

    PKCS10CertificationRequest csr =
        CertificateUtil.generateCsr(
            keyPair,
            subjectName,
            sanUri,
            dnsNames,
            ipAddresses,
            "Ed25519",
            KeyUsage.digitalSignature,
            null);

    return ByteString.of(csr.getEncoded());
  }
}
