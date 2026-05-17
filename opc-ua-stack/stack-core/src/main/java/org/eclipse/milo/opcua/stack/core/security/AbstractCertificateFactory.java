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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;

/**
 * Base {@link CertificateFactory} for Milo-managed application certificate types.
 *
 * <p>Use this class when a {@link CertificateGroup}, such as {@link DefaultApplicationGroup},
 * should be able to create missing local application identities for more than one OPC UA
 * CertificateType. The public {@link CertificateFactory} methods route each known certificate type
 * {@link NodeId} to a protected helper for key-pair generation, certificate-chain creation, or CSR
 * creation.
 *
 * <p>This base class can generate key pairs and CSRs for RSA SHA-256 and the current ECC
 * application certificate types. It cannot know how to issue an application certificate chain,
 * because the application controls subject attributes, the application URI, DNS/IP subject
 * alternative names, validity, issuer policy, and whether the chain is self-signed or CA-issued.
 * Subclasses therefore override the certificate-chain method for each certificate type they
 * configure in a certificate group. The default ECC chain methods throw {@link UaException} with
 * {@link StatusCodes#Bad_NotSupported} so a factory does not silently claim support for identities
 * it cannot issue.
 *
 * <p>When {@link DefaultApplicationGroup#initialize()} finds that its {@link CertificateStore} is
 * missing an entry for a configured certificate type, it calls {@link #createKeyPair(NodeId)}
 * followed by {@link #createCertificateChain(NodeId, KeyPair)} for that same type. If a store is
 * pre-populated for a type, the factory is not used for that entry.
 *
 * <p>Example:
 *
 * <pre>{@code
 * String applicationUri = "urn:example:server";
 *
 * CertificateFactory certificateFactory =
 *     new AbstractCertificateFactory() {
 *       @Override
 *       protected X509Certificate[] createRsaSha256CertificateChain(KeyPair keyPair)
 *           throws Exception {
 *
 *         return issueRsaApplicationCertificateChain(keyPair, applicationUri);
 *       }
 *
 *       @Override
 *       protected X509Certificate[] createEccNistP256CertificateChain(KeyPair keyPair)
 *           throws Exception {
 *
 *         X509Certificate certificate =
 *             SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
 *                 .setCommonName("Example Server")
 *                 .setOrganization("Example")
 *                 .setApplicationUri(applicationUri)
 *                 .addDnsName("localhost")
 *                 .build();
 *
 *         return new X509Certificate[] {certificate};
 *       }
 *     };
 *
 * DefaultApplicationGroup group =
 *     DefaultApplicationGroup.createAndInitialize(
 *         trustListManager,
 *         certificateStore,
 *         certificateFactory,
 *         certificateValidator,
 *         List.of(
 *             NodeIds.RsaSha256ApplicationCertificateType,
 *             NodeIds.EccNistP256ApplicationCertificateType));
 * }</pre>
 */
public abstract class AbstractCertificateFactory implements CertificateFactory {

  private static final int DEFAULT_KEY_LENGTH = 2048;

  /**
   * Create a new key pair for a Milo-managed application certificate type.
   *
   * <p>Known certificate type IDs are routed to the corresponding protected key-pair helper.
   * Unknown certificate type IDs throw {@link UnsupportedOperationException}. Override the
   * protected helper for a certificate type when key generation must use deployment-specific
   * randomness, providers, or hardware-backed keys.
   *
   * @param certificateTypeId the {@link NodeId} identifying the certificate type.
   * @return the new {@link KeyPair}.
   * @throws GeneralSecurityException if the key pair cannot be generated.
   * @throws UnsupportedOperationException if {@code certificateTypeId} is not supported by this
   *     factory.
   */
  @Override
  public KeyPair createKeyPair(NodeId certificateTypeId) throws GeneralSecurityException {
    if (certificateTypeId.equals(NodeIds.RsaSha256ApplicationCertificateType)) {
      return createRsaSha256KeyPair();
    } else if (certificateTypeId.equals(NodeIds.EccNistP256ApplicationCertificateType)) {
      return createEccNistP256KeyPair();
    } else if (certificateTypeId.equals(NodeIds.EccNistP384ApplicationCertificateType)) {
      return createEccNistP384KeyPair();
    } else if (certificateTypeId.equals(NodeIds.EccBrainpoolP256r1ApplicationCertificateType)) {
      return createEccBrainpoolP256r1KeyPair();
    } else if (certificateTypeId.equals(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)) {
      return createEccBrainpoolP384r1KeyPair();
    } else if (certificateTypeId.equals(NodeIds.EccCurve25519ApplicationCertificateType)) {
      return createEccCurve25519KeyPair();
    } else if (certificateTypeId.equals(NodeIds.EccCurve448ApplicationCertificateType)) {
      return createEccCurve448KeyPair();
    } else {
      throw new UnsupportedOperationException("certificateTypeId: " + certificateTypeId);
    }
  }

  /**
   * Create a certificate chain for a Milo-managed application certificate type.
   *
   * <p>Known certificate type IDs are routed to the corresponding protected chain helper.
   * Subclasses must override each chain helper for the certificate types they configure in a {@link
   * CertificateGroup}. The returned chain must have a leaf certificate whose public key matches
   * {@code keyPair} and whose certificate type, key usages, subject alternative names, and issuer
   * policy are valid for the endpoint policies that will select it.
   *
   * @param certificateTypeId the {@link NodeId} identifying the certificate type.
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   * @throws Exception if an error occurs while creating the certificate chain.
   * @throws UaException if the routed chain helper has not been overridden for {@code
   *     certificateTypeId}.
   * @throws UnsupportedOperationException if {@code certificateTypeId} is not a known Milo-managed
   *     certificate type.
   */
  @Override
  public X509Certificate[] createCertificateChain(NodeId certificateTypeId, KeyPair keyPair)
      throws Exception {
    if (certificateTypeId.equals(NodeIds.RsaSha256ApplicationCertificateType)) {
      return createRsaSha256CertificateChain(keyPair);
    } else if (certificateTypeId.equals(NodeIds.EccNistP256ApplicationCertificateType)) {
      return createEccNistP256CertificateChain(keyPair);
    } else if (certificateTypeId.equals(NodeIds.EccNistP384ApplicationCertificateType)) {
      return createEccNistP384CertificateChain(keyPair);
    } else if (certificateTypeId.equals(NodeIds.EccBrainpoolP256r1ApplicationCertificateType)) {
      return createEccBrainpoolP256r1CertificateChain(keyPair);
    } else if (certificateTypeId.equals(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)) {
      return createEccBrainpoolP384r1CertificateChain(keyPair);
    } else if (certificateTypeId.equals(NodeIds.EccCurve25519ApplicationCertificateType)) {
      return createEccCurve25519CertificateChain(keyPair);
    } else if (certificateTypeId.equals(NodeIds.EccCurve448ApplicationCertificateType)) {
      return createEccCurve448CertificateChain(keyPair);
    } else {
      throw new UnsupportedOperationException("certificateTypeId: " + certificateTypeId);
    }
  }

  /**
   * Create a PKCS10 certificate signing request for a Milo-managed application certificate type.
   *
   * <p>Known certificate type IDs are routed to the corresponding protected CSR helper. The CSR
   * contains the requested subject, application URI, DNS names, IP addresses, and key-usage
   * extension for the selected certificate type; issuing and storing the resulting certificate
   * chain remains the caller's responsibility.
   *
   * @param certificateTypeId the {@link NodeId} identifying the certificate type.
   * @param keyPair the {@link KeyPair} to use when creating the signing request.
   * @param subjectName the {@link X500Name} to request.
   * @param sanUri the URI to request in the Subject Alternative Name of the CSR.
   * @param dnsNames the DNS names to request in the Subject Alternative Name of the CSR.
   * @param ipAddresses the IP addresses to request in the Subject Alternative Name of the CSR.
   * @return the new {@link ByteString} containing the DER-encoded PKCS10 signing request.
   * @throws Exception if an error occurs while creating the signing request.
   * @throws UnsupportedOperationException if {@code certificateTypeId} is not supported by this
   *     factory.
   */
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
    } else if (certificateTypeId.equals(NodeIds.EccNistP384ApplicationCertificateType)) {
      return createEccNistP384SigningRequest(keyPair, subjectName, sanUri, dnsNames, ipAddresses);
    } else if (certificateTypeId.equals(NodeIds.EccBrainpoolP256r1ApplicationCertificateType)) {
      return createEccBrainpoolP256r1SigningRequest(
          keyPair, subjectName, sanUri, dnsNames, ipAddresses);
    } else if (certificateTypeId.equals(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)) {
      return createEccBrainpoolP384r1SigningRequest(
          keyPair, subjectName, sanUri, dnsNames, ipAddresses);
    } else if (certificateTypeId.equals(NodeIds.EccCurve25519ApplicationCertificateType)) {
      return createEccCurve25519SigningRequest(keyPair, subjectName, sanUri, dnsNames, ipAddresses);
    } else if (certificateTypeId.equals(NodeIds.EccCurve448ApplicationCertificateType)) {
      return createEccCurve448SigningRequest(keyPair, subjectName, sanUri, dnsNames, ipAddresses);
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
   * Create a new NIST P-384 ECDSA {@link KeyPair} for a {@link
   * NodeIds#EccNistP384ApplicationCertificateType} type certificate.
   *
   * @return the new {@link KeyPair}.
   */
  protected KeyPair createEccNistP384KeyPair() throws GeneralSecurityException {
    return SelfSignedCertificateGenerator.generateNistP384KeyPair();
  }

  /**
   * Create a new Brainpool P-256r1 ECDSA {@link KeyPair} for a {@link
   * NodeIds#EccBrainpoolP256r1ApplicationCertificateType} type certificate.
   *
   * <p>Brainpool P-256r1 key generation is routed through Bouncy Castle so the generated key can be
   * used consistently for certificates, CSRs, and SecureChannel compatibility checks.
   *
   * @return the new {@link KeyPair}.
   */
  protected KeyPair createEccBrainpoolP256r1KeyPair() throws GeneralSecurityException {
    return SelfSignedCertificateGenerator.generateBrainpoolP256r1KeyPair();
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
   * Create a new Ed448 {@link KeyPair} for a {@link NodeIds#EccCurve448ApplicationCertificateType}
   * type certificate.
   *
   * <p>The Curve448 certificate type uses Ed448 application certificates. X448 is only used as an
   * ephemeral OpenSecureChannel key-agreement key.
   *
   * @return the new {@link KeyPair}.
   */
  protected KeyPair createEccCurve448KeyPair() throws NoSuchAlgorithmException {
    return SelfSignedCertificateGenerator.generateEd448KeyPair();
  }

  /**
   * Create a new {@link X509Certificate} chain of type {@link
   * NodeIds#RsaSha256ApplicationCertificateType}. The chain may be of length 1 if the certificate
   * is self-signed.
   *
   * <p>Subclasses must implement this method because RSA SHA-256 is the default application
   * certificate type used by {@link DefaultApplicationGroup}. The leaf certificate's public key
   * must match {@code keyPair}.
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
   * <p>Override this method when the owning {@link CertificateGroup} is configured to manage NIST
   * P-256 ECC application certificates. A self-signed implementation can use {@code
   * SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)}; a CA-backed implementation
   * can return the issued leaf and issuer chain. The leaf certificate's public key must match
   * {@code keyPair} and must satisfy the profile compatibility rules for NIST P-256 ECC policies.
   * The default implementation throws {@link UaException} with {@link
   * StatusCodes#Bad_NotSupported}.
   *
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   * @throws Exception if an error occurs while creating the certificate chain.
   * @throws UaException if this factory does not issue NIST P-256 certificate chains.
   */
  protected X509Certificate[] createEccNistP256CertificateChain(KeyPair keyPair) throws Exception {
    throw new UaException(
        StatusCodes.Bad_NotSupported,
        "certificateTypeId: " + NodeIds.EccNistP256ApplicationCertificateType);
  }

  /**
   * Create a new {@link X509Certificate} chain of type {@link
   * NodeIds#EccNistP384ApplicationCertificateType}.
   *
   * <p>Override this method when the owning {@link CertificateGroup} is configured to manage NIST
   * P-384 ECC application certificates. A self-signed implementation can use {@code
   * SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)}; a CA-backed implementation
   * can return the issued leaf and issuer chain. The leaf certificate's public key must match
   * {@code keyPair} and must satisfy the profile compatibility rules for NIST P-384 ECC policies.
   * The default implementation throws {@link UaException} with {@link
   * StatusCodes#Bad_NotSupported}.
   *
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   * @throws Exception if an error occurs while creating the certificate chain.
   * @throws UaException if this factory does not issue NIST P-384 certificate chains.
   */
  protected X509Certificate[] createEccNistP384CertificateChain(KeyPair keyPair) throws Exception {
    throw new UaException(
        StatusCodes.Bad_NotSupported,
        "certificateTypeId: " + NodeIds.EccNistP384ApplicationCertificateType);
  }

  /**
   * Create a new {@link X509Certificate} chain of type {@link
   * NodeIds#EccBrainpoolP256r1ApplicationCertificateType}.
   *
   * <p>Override this method when the owning {@link CertificateGroup} is configured to manage
   * Brainpool P-256r1 ECC application certificates. A self-signed implementation can use {@code
   * SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)}; a CA-backed implementation
   * can return the issued leaf and issuer chain. The leaf certificate's public key must match
   * {@code keyPair} and must satisfy the profile compatibility rules for Brainpool P-256r1 ECC
   * policies. The default implementation throws {@link UaException} with {@link
   * StatusCodes#Bad_NotSupported}.
   *
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   * @throws Exception if an error occurs while creating the certificate chain.
   * @throws UaException if this factory does not issue Brainpool P-256r1 certificate chains.
   */
  protected X509Certificate[] createEccBrainpoolP256r1CertificateChain(KeyPair keyPair)
      throws Exception {

    throw new UaException(
        StatusCodes.Bad_NotSupported,
        "certificateTypeId: " + NodeIds.EccBrainpoolP256r1ApplicationCertificateType);
  }

  /**
   * Create a new {@link X509Certificate} chain of type {@link
   * NodeIds#EccBrainpoolP384r1ApplicationCertificateType}.
   *
   * <p>Override this method when the owning {@link CertificateGroup} is configured to manage
   * Brainpool P-384r1 ECC application certificates. A self-signed implementation can use {@code
   * SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)}; a CA-backed implementation
   * can return the issued leaf and issuer chain. The leaf certificate's public key must match
   * {@code keyPair} and must satisfy the profile compatibility rules for Brainpool P-384r1 ECC
   * policies. The default implementation throws {@link UaException} with {@link
   * StatusCodes#Bad_NotSupported}.
   *
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   * @throws Exception if an error occurs while creating the certificate chain.
   * @throws UaException if this factory does not issue Brainpool P-384r1 certificate chains.
   */
  protected X509Certificate[] createEccBrainpoolP384r1CertificateChain(KeyPair keyPair)
      throws Exception {

    throw new UaException(
        StatusCodes.Bad_NotSupported,
        "certificateTypeId: " + NodeIds.EccBrainpoolP384r1ApplicationCertificateType);
  }

  /**
   * Create a new {@link X509Certificate} chain of type {@link
   * NodeIds#EccCurve25519ApplicationCertificateType}.
   *
   * <p>Override this method when the owning {@link CertificateGroup} is configured to manage
   * Curve25519 application certificates. The application certificate key is Ed25519; X25519 is only
   * an ephemeral key-agreement key for enhanced SecureChannel setup. A self-signed implementation
   * can use {@code SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)}; a CA-backed
   * implementation can return the issued leaf and issuer chain. The leaf certificate's public key
   * must match {@code keyPair} and must satisfy the profile compatibility rules for Curve25519
   * policies. The default implementation throws {@link UaException} with {@link
   * StatusCodes#Bad_NotSupported}.
   *
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   * @throws Exception if an error occurs while creating the certificate chain.
   * @throws UaException if this factory does not issue Curve25519 certificate chains.
   */
  protected X509Certificate[] createEccCurve25519CertificateChain(KeyPair keyPair)
      throws Exception {

    throw new UaException(
        StatusCodes.Bad_NotSupported,
        "certificateTypeId: " + NodeIds.EccCurve25519ApplicationCertificateType);
  }

  /**
   * Create a new {@link X509Certificate} chain of type {@link
   * NodeIds#EccCurve448ApplicationCertificateType}.
   *
   * <p>Override this method when the owning {@link CertificateGroup} is configured to manage
   * Curve448 application certificates. The application certificate key is Ed448; X448 is only an
   * ephemeral key-agreement key for enhanced SecureChannel setup. A self-signed implementation can
   * use {@code SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)}; a CA-backed
   * implementation can return the issued leaf and issuer chain. The leaf certificate's public key
   * must match {@code keyPair} and must satisfy the profile compatibility rules for Curve448
   * policies. The default implementation throws {@link UaException} with {@link
   * StatusCodes#Bad_NotSupported}.
   *
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   * @throws Exception if an error occurs while creating the certificate chain.
   * @throws UaException if this factory does not issue Curve448 certificate chains.
   */
  protected X509Certificate[] createEccCurve448CertificateChain(KeyPair keyPair) throws Exception {

    throw new UaException(
        StatusCodes.Bad_NotSupported,
        "certificateTypeId: " + NodeIds.EccCurve448ApplicationCertificateType);
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
   * NodeIds#EccNistP384ApplicationCertificateType} type certificate.
   *
   * @param keyPair the {@link KeyPair} to use when creating the signing request.
   * @param subjectName the {@link X500Name} to request.
   * @param sanUri the URI to request in the Subject Alternative Name of the CSR.
   * @param dnsNames the DNS names to request in the Subject Alternative Name of the CSR.
   * @param ipAddresses the IP addresses to request in the Subject Alternative Name of the CSR.
   * @return the new {@link ByteString} containing the DER-encoded PKCS10 signing request.
   * @throws Exception if an error occurs while creating the signing request.
   */
  protected ByteString createEccNistP384SigningRequest(
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
            null);

    return ByteString.of(csr.getEncoded());
  }

  /**
   * Create a new PKCS10 certificate signing request for a {@link
   * NodeIds#EccBrainpoolP256r1ApplicationCertificateType} type certificate.
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
  protected ByteString createEccBrainpoolP256r1SigningRequest(
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
            null,
            new BouncyCastleProvider());

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

  /**
   * Create a new PKCS10 certificate signing request for a {@link
   * NodeIds#EccCurve448ApplicationCertificateType} type certificate.
   *
   * @param keyPair the {@link KeyPair} to use when creating the signing request.
   * @param subjectName the {@link X500Name} to request.
   * @param sanUri the URI to request in the Subject Alternative Name of the CSR.
   * @param dnsNames the DNS names to request in the Subject Alternative Name of the CSR.
   * @param ipAddresses the IP addresses to request in the Subject Alternative Name of the CSR.
   * @return the new {@link ByteString} containing the DER-encoded PKCS10 signing request.
   * @throws Exception if an error occurs while creating the signing request.
   */
  protected ByteString createEccCurve448SigningRequest(
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
            "Ed448",
            KeyUsage.digitalSignature,
            null);

    return ByteString.of(csr.getEncoded());
  }
}
