/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util.validation;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fluent builder for creating X.509 certificates signed by a CA.
 *
 * <p>This builder allows creation of certificates signed by an issuer (CA) certificate, including
 * both intermediate CA certificates and end-entity certificates.
 */
public class CaSignedCertificateBuilder {

  /** Signature Algorithm for SHA256 with RSA. */
  public static final String SA_SHA256_RSA = "SHA256withRSA";

  /** Signature Algorithm for SHA256 with ECDSA. */
  public static final String SA_SHA256_ECDSA = "SHA256withECDSA";

  private Period validityPeriod = Period.ofYears(3);

  private String commonName = "";
  private String organization = "";
  private String organizationalUnit = "";
  private String localityName = "";
  private String stateName = "";
  private String countryCode = "";

  private String applicationUri = null;
  private final List<String> dnsNames = new ArrayList<>();
  private final List<String> ipAddresses = new ArrayList<>();
  private String signatureAlgorithm = SA_SHA256_RSA;

  private boolean isCa = false;
  private int pathLengthConstraint = -1;
  private Integer keyUsage = null;
  private List<KeyPurposeId> extendedKeyUsage = null;
  private boolean includeAuthorityKeyIdentifier = true;
  private boolean includeSubjectKeyIdentifier = true;

  private final KeyPair subjectKeyPair;
  private final X509Certificate issuerCertificate;
  private final PrivateKey issuerPrivateKey;

  /**
   * Create a new builder for a CA-signed certificate.
   *
   * @param subjectKeyPair the {@link KeyPair} for the certificate being created
   * @param issuerCertificate the {@link X509Certificate} of the issuer (CA)
   * @param issuerPrivateKey the {@link PrivateKey} of the issuer (CA)
   */
  public CaSignedCertificateBuilder(
      KeyPair subjectKeyPair, X509Certificate issuerCertificate, PrivateKey issuerPrivateKey) {
    this.subjectKeyPair = subjectKeyPair;
    this.issuerCertificate = issuerCertificate;
    this.issuerPrivateKey = issuerPrivateKey;

    PublicKey publicKey = subjectKeyPair.getPublic();

    if (publicKey instanceof RSAPublicKey) {
      signatureAlgorithm = SA_SHA256_RSA;

      int bitLength = ((RSAPublicKey) publicKey).getModulus().bitLength();

      if (bitLength <= 1024) {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.warn("Using legacy key size: {}", bitLength);
      }
    } else if (publicKey instanceof ECPublicKey) {
      signatureAlgorithm = SA_SHA256_ECDSA;
    }
  }

  public CaSignedCertificateBuilder setValidityPeriod(Period validityPeriod) {
    this.validityPeriod = validityPeriod;
    return this;
  }

  public CaSignedCertificateBuilder setCommonName(String commonName) {
    this.commonName = commonName;
    return this;
  }

  public CaSignedCertificateBuilder setOrganization(String organization) {
    this.organization = organization;
    return this;
  }

  public CaSignedCertificateBuilder setOrganizationalUnit(String organizationalUnit) {
    this.organizationalUnit = organizationalUnit;
    return this;
  }

  public CaSignedCertificateBuilder setLocalityName(String localityName) {
    this.localityName = localityName;
    return this;
  }

  public CaSignedCertificateBuilder setStateName(String stateName) {
    this.stateName = stateName;
    return this;
  }

  public CaSignedCertificateBuilder setCountryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

  public CaSignedCertificateBuilder setApplicationUri(String applicationUri) {
    this.applicationUri = applicationUri;
    return this;
  }

  public CaSignedCertificateBuilder addDnsName(String dnsName) {
    dnsNames.add(dnsName);
    return this;
  }

  public CaSignedCertificateBuilder addIpAddress(String ipAddress) {
    ipAddresses.add(ipAddress);
    return this;
  }

  public CaSignedCertificateBuilder setSignatureAlgorithm(String signatureAlgorithm) {
    this.signatureAlgorithm = signatureAlgorithm;
    return this;
  }

  /**
   * Set whether this certificate is a CA certificate.
   *
   * @param isCa {@code true} if this is a CA certificate
   * @return this builder
   */
  public CaSignedCertificateBuilder setIsCa(boolean isCa) {
    this.isCa = isCa;
    return this;
  }

  /**
   * Set the path length constraint for CA certificates.
   *
   * @param pathLengthConstraint the maximum number of CA certificates that may follow this
   *     certificate in a valid certification path, or -1 for no constraint
   * @return this builder
   */
  public CaSignedCertificateBuilder setPathLengthConstraint(int pathLengthConstraint) {
    this.pathLengthConstraint = pathLengthConstraint;
    return this;
  }

  /**
   * Set the Key Usage extension value.
   *
   * @param keyUsage the key usage flags (see {@link KeyUsage}) or {@code null} to omit the
   *     extension
   * @return this builder
   */
  public CaSignedCertificateBuilder setKeyUsage(@Nullable Integer keyUsage) {
    this.keyUsage = keyUsage;
    return this;
  }

  /**
   * Set the Extended Key Usage extension value.
   *
   * @param extendedKeyUsage list of key purpose IDs or {@code null} to omit the extension
   * @return this builder
   */
  public CaSignedCertificateBuilder setExtendedKeyUsage(
      @Nullable List<KeyPurposeId> extendedKeyUsage) {
    this.extendedKeyUsage = extendedKeyUsage;
    return this;
  }

  /**
   * Set whether to include the Authority Key Identifier extension.
   *
   * @param include {@code true} to include the extension
   * @return this builder
   */
  public CaSignedCertificateBuilder setIncludeAuthorityKeyIdentifier(boolean include) {
    this.includeAuthorityKeyIdentifier = include;
    return this;
  }

  /**
   * Set whether to include the Subject Key Identifier extension.
   *
   * @param include {@code true} to include the extension
   * @return this builder
   */
  public CaSignedCertificateBuilder setIncludeSubjectKeyIdentifier(boolean include) {
    this.includeSubjectKeyIdentifier = include;
    return this;
  }

  /**
   * Build the certificate.
   *
   * @return the signed {@link X509Certificate}
   * @throws Exception if certificate generation fails
   */
  public X509Certificate build() throws Exception {
    // Calculate start and end date based on validity period
    LocalDate now = LocalDate.now();
    LocalDate expiration = now.plus(validityPeriod);

    Date notBefore = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date notAfter = Date.from(expiration.atStartOfDay(ZoneId.systemDefault()).toInstant());

    // Build subject name
    X500NameBuilder subjectNameBuilder = new X500NameBuilder();

    if (!commonName.isEmpty()) {
      subjectNameBuilder.addRDN(BCStyle.CN, commonName);
    }
    if (!organization.isEmpty()) {
      subjectNameBuilder.addRDN(BCStyle.O, organization);
    }
    if (!organizationalUnit.isEmpty()) {
      subjectNameBuilder.addRDN(BCStyle.OU, organizationalUnit);
    }
    if (!localityName.isEmpty()) {
      subjectNameBuilder.addRDN(BCStyle.L, localityName);
    }
    if (!stateName.isEmpty()) {
      subjectNameBuilder.addRDN(BCStyle.ST, stateName);
    }
    if (!countryCode.isEmpty()) {
      subjectNameBuilder.addRDN(BCStyle.C, countryCode);
    }

    X500Name subjectName = subjectNameBuilder.build();
    X500Name issuerName =
        X500Name.getInstance(issuerCertificate.getSubjectX500Principal().getEncoded());

    // Generate serial number
    BigInteger certSerialNumber =
        new BigInteger(Long.toString(System.currentTimeMillis()))
            .add(BigInteger.valueOf(new SecureRandom().nextInt(10000)));

    SubjectPublicKeyInfo subjectPublicKeyInfo =
        SubjectPublicKeyInfo.getInstance(subjectKeyPair.getPublic().getEncoded());

    X509v3CertificateBuilder certificateBuilder =
        new X509v3CertificateBuilder(
            issuerName,
            certSerialNumber,
            notBefore,
            notAfter,
            Locale.ENGLISH,
            subjectName,
            subjectPublicKeyInfo);

    // Basic Constraints
    BasicConstraints basicConstraints;
    if (isCa) {
      if (pathLengthConstraint >= 0) {
        basicConstraints = new BasicConstraints(pathLengthConstraint);
      } else {
        basicConstraints = new BasicConstraints(true);
      }
    } else {
      basicConstraints = new BasicConstraints(false);
    }
    certificateBuilder.addExtension(Extension.basicConstraints, true, basicConstraints);

    // Key Usage
    if (keyUsage != null) {
      certificateBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(keyUsage));
    }

    // Extended Key Usage
    if (extendedKeyUsage != null && !extendedKeyUsage.isEmpty()) {
      certificateBuilder.addExtension(
          Extension.extendedKeyUsage,
          false,
          new ExtendedKeyUsage(extendedKeyUsage.toArray(new KeyPurposeId[0])));
    }

    // Subject Alternative Names
    if (applicationUri != null || !dnsNames.isEmpty() || !ipAddresses.isEmpty()) {
      List<GeneralName> generalNames = new ArrayList<>();

      if (applicationUri != null) {
        generalNames.add(new GeneralName(GeneralName.uniformResourceIdentifier, applicationUri));
      }

      dnsNames.stream()
          .distinct()
          .map(s -> new GeneralName(GeneralName.dNSName, s))
          .forEach(generalNames::add);

      ipAddresses.stream()
          .distinct()
          .map(s -> new GeneralName(GeneralName.iPAddress, s))
          .forEach(generalNames::add);

      certificateBuilder.addExtension(
          Extension.subjectAlternativeName,
          false,
          new GeneralNames(generalNames.toArray(new GeneralName[] {})));
    }

    // Subject Key Identifier
    if (includeSubjectKeyIdentifier) {
      certificateBuilder.addExtension(
          Extension.subjectKeyIdentifier,
          false,
          new JcaX509ExtensionUtils().createSubjectKeyIdentifier(subjectKeyPair.getPublic()));
    }

    // Authority Key Identifier
    if (includeAuthorityKeyIdentifier) {
      certificateBuilder.addExtension(
          Extension.authorityKeyIdentifier,
          false,
          new JcaX509ExtensionUtils().createAuthorityKeyIdentifier(issuerCertificate));
    }

    ContentSigner contentSigner =
        new JcaContentSignerBuilder(signatureAlgorithm).build(issuerPrivateKey);

    X509CertificateHolder certificateHolder = certificateBuilder.build(contentSigner);

    return new JcaX509CertificateConverter().getCertificate(certificateHolder);
  }
}
