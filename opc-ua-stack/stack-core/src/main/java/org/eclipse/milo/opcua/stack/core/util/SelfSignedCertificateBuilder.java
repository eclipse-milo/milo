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

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.EdECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.NamedParameterSpec;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fluent builder for self-signed OPC UA application instance certificates.
 *
 * <p>Use {@link #forRsaApplicationCertificate(KeyPair)} or {@link
 * #forEccApplicationCertificate(KeyPair)} when the certificate will be stored as a local
 * application identity and matched against OPC UA certificate type and security policy profile
 * rules. The direct constructors are available for legacy callers and for tests or tooling that
 * need a custom {@link SelfSignedCertificateGenerator}.
 *
 * <p>Builder instances are mutable and intended for single-threaded setup followed by {@link
 * #build()}. Calling {@code build()} creates a new certificate using the current system date as the
 * start of the configured validity period.
 */
public class SelfSignedCertificateBuilder {

  /**
   * Signature algorithm name for SHA1 with RSA.
   *
   * <p>SHA1 was broken in 2017 and this algorithm should not be used.
   */
  public static final String SA_SHA1_RSA = "SHA1withRSA";

  /** Signature algorithm name for SHA256 with RSA. */
  public static final String SA_SHA256_RSA = "SHA256withRSA";

  /**
   * Signature algorithm name for SHA256 with ECDSA.
   *
   * <p>May only be used with EC-based key pairs and security profiles.
   */
  public static final String SA_SHA256_ECDSA = "SHA256withECDSA";

  /**
   * Signature algorithm name for SHA384 with ECDSA.
   *
   * <p>May only be used with EC-based key pairs and security profiles.
   */
  public static final String SA_SHA384_ECDSA = "SHA384withECDSA";

  /** Signature algorithm name for Ed25519. */
  public static final String SA_ED25519 = "Ed25519";

  /** Signature algorithm name for Ed448. */
  public static final String SA_ED448 = "Ed448";

  private Period validityPeriod = Period.ofYears(3);

  private String commonName = "not configured";
  private String organization = "not configured";
  private String organizationalUnit = null;
  private String localityName = null;
  private String stateName = null;
  private String countryCode = null;

  private String applicationUri = "";
  private final List<String> dnsNames = new ArrayList<>();
  private final List<String> ipAddresses = new ArrayList<>();
  private String signatureAlgorithm = SA_SHA256_RSA;

  private final KeyPair keyPair;
  private final SelfSignedCertificateGenerator generator;

  /**
   * Create a builder using the default application-certificate generator.
   *
   * <p>For RSA application certificates, this constructor uses the same certificate defaults as
   * {@link #forRsaApplicationCertificate(KeyPair)}. For ECC and Edwards-curve application
   * certificates, prefer {@link #forEccApplicationCertificate(KeyPair)} so the generated KeyUsage
   * and ExtendedKeyUsage extensions match the ECC application-certificate profile.
   *
   * @param keyPair the key pair whose public key is placed in the certificate and whose private key
   *     signs it.
   */
  public SelfSignedCertificateBuilder(KeyPair keyPair) {
    this(keyPair, new SelfSignedCertificateGenerator());
  }

  /**
   * Create a builder with a caller-supplied certificate generator.
   *
   * <p>Use this constructor when tests, migration tooling, or custom certificate profiles need to
   * control the X.509 extensions emitted by {@link SelfSignedCertificateGenerator}. The builder
   * still owns the subject fields, subject alternative names, validity period, and signature
   * algorithm passed to the generator.
   *
   * @param keyPair the key pair whose public key is placed in the certificate and whose private key
   *     signs it.
   * @param generator the generator that writes the X.509 certificate extensions.
   */
  public SelfSignedCertificateBuilder(KeyPair keyPair, SelfSignedCertificateGenerator generator) {
    this.keyPair = keyPair;
    this.generator = generator;

    PublicKey publicKey = keyPair.getPublic();

    if (publicKey instanceof RSAPublicKey) {
      signatureAlgorithm = SA_SHA256_RSA;

      int bitLength = ((RSAPublicKey) keyPair.getPublic()).getModulus().bitLength();

      if (bitLength <= 1024) {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.warn("Using legacy key size: {}", bitLength);
      }
    } else if (keyPair.getPublic() instanceof ECPublicKey ecPublicKey) {
      signatureAlgorithm =
          ecPublicKey.getParams().getOrder().bitLength() > 256 ? SA_SHA384_ECDSA : SA_SHA256_ECDSA;
    } else if (isEd25519PublicKey(keyPair.getPublic())) {
      signatureAlgorithm = SA_ED25519;
    } else if (isEd448PublicKey(keyPair.getPublic())) {
      signatureAlgorithm = SA_ED448;
    }
  }

  /**
   * Create a builder for an RSA application instance certificate.
   *
   * <p>This factory validates that {@code keyPair} is RSA and then uses RSA application-certificate
   * defaults: the legacy OPC UA application certificate KeyUsage bits are generated and
   * ExtendedKeyUsage includes clientAuth and serverAuth.
   *
   * @param keyPair an RSA key pair.
   * @return a builder configured for an RSA application certificate.
   * @throws IllegalArgumentException if {@code keyPair} is not an RSA key pair.
   */
  public static SelfSignedCertificateBuilder forRsaApplicationCertificate(KeyPair keyPair) {
    PublicKey publicKey = keyPair.getPublic();

    if (!(publicKey instanceof RSAPublicKey)) {
      throw new IllegalArgumentException("RSA application certificates require an RSA key pair");
    }

    return new SelfSignedCertificateBuilder(keyPair);
  }

  /**
   * Create a builder for an ECC or Edwards-curve application instance certificate.
   *
   * <p>This factory validates that {@code keyPair} is an EC, Ed25519, or Ed448 key pair and then
   * uses ECC application-certificate defaults: only the required self-signed KeyUsage bits are
   * generated and ExtendedKeyUsage is omitted.
   *
   * @param keyPair an EC, Ed25519, or Ed448 key pair.
   * @return a builder configured for an ECC application certificate.
   * @throws IllegalArgumentException if {@code keyPair} is not an EC, Ed25519, or Ed448 key pair.
   */
  public static SelfSignedCertificateBuilder forEccApplicationCertificate(KeyPair keyPair) {
    PublicKey publicKey = keyPair.getPublic();

    if (!(publicKey instanceof ECPublicKey)
        && !isEd25519PublicKey(publicKey)
        && !isEd448PublicKey(publicKey)) {
      throw new IllegalArgumentException(
          "ECC application certificates require an EC, Ed25519, or Ed448 key pair");
    }

    return new SelfSignedCertificateBuilder(
        keyPair, SelfSignedCertificateGenerator.eccApplicationCertificateGenerator());
  }

  /**
   * Set how long the generated certificate is valid.
   *
   * <p>The validity starts at the current local date when {@link #build()} is called and ends after
   * this period has elapsed.
   *
   * @param validityPeriod the validity period to apply.
   * @return this builder.
   */
  public SelfSignedCertificateBuilder setValidityPeriod(Period validityPeriod) {
    this.validityPeriod = validityPeriod;
    return this;
  }

  /**
   * Set the X.500 Common Name attribute.
   *
   * <p>For OPC UA application certificates this is usually the application name, or another
   * operator-readable name for the application instance.
   *
   * @param commonName the Common Name value.
   * @return this builder.
   */
  public SelfSignedCertificateBuilder setCommonName(String commonName) {
    this.commonName = commonName;
    return this;
  }

  /**
   * Set the X.500 Organization attribute.
   *
   * @param organization the organization that operates the application instance.
   * @return this builder.
   */
  public SelfSignedCertificateBuilder setOrganization(String organization) {
    this.organization = organization;
    return this;
  }

  /**
   * Set the X.500 Organizational Unit attribute.
   *
   * @param organizationalUnit the organizational unit value.
   * @return this builder.
   */
  public SelfSignedCertificateBuilder setOrganizationalUnit(String organizationalUnit) {
    this.organizationalUnit = organizationalUnit;
    return this;
  }

  /**
   * Set the X.500 Locality attribute.
   *
   * @param localityName the locality value.
   * @return this builder.
   */
  public SelfSignedCertificateBuilder setLocalityName(String localityName) {
    this.localityName = localityName;
    return this;
  }

  /**
   * Set the X.500 State or Province attribute.
   *
   * @param stateName the state or province value.
   * @return this builder.
   */
  public SelfSignedCertificateBuilder setStateName(String stateName) {
    this.stateName = stateName;
    return this;
  }

  /**
   * Set the X.500 Country attribute.
   *
   * @param countryCode the country code value.
   * @return this builder.
   */
  public SelfSignedCertificateBuilder setCountryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

  /**
   * Set the OPC UA application URI.
   *
   * <p>The URI is written to the Subject Alternative Name extension as a uniformResourceIdentifier.
   *
   * @param applicationUri the application URI value.
   * @return this builder.
   */
  public SelfSignedCertificateBuilder setApplicationUri(String applicationUri) {
    this.applicationUri = applicationUri;
    return this;
  }

  /**
   * Add a DNS subject alternative name.
   *
   * <p>Servers should include the host names clients use to reach the application instance.
   *
   * @param dnsName the DNS name to add.
   * @return this builder.
   */
  public SelfSignedCertificateBuilder addDnsName(String dnsName) {
    dnsNames.add(dnsName);
    return this;
  }

  /**
   * Add an IP-address subject alternative name.
   *
   * @param ipAddress the textual IP address to add.
   * @return this builder.
   */
  public SelfSignedCertificateBuilder addIpAddress(String ipAddress) {
    ipAddresses.add(ipAddress);
    return this;
  }

  /**
   * Override the JCA signature algorithm used to sign the certificate.
   *
   * <p>The default is selected from the key pair: SHA256withRSA for RSA, SHA256withECDSA or
   * SHA384withECDSA for EC keys, Ed25519 for Ed25519 keys, and Ed448 for Ed448 keys. Override this
   * only when the selected algorithm is compatible with the key pair and the security profile that
   * will use the certificate.
   *
   * @param signatureAlgorithm the JCA signature algorithm name.
   * @return this builder.
   */
  public SelfSignedCertificateBuilder setSignatureAlgorithm(String signatureAlgorithm) {
    this.signatureAlgorithm = signatureAlgorithm;
    return this;
  }

  /**
   * Generate the self-signed X.509 certificate from the current builder state.
   *
   * <p>The returned certificate is signed with this builder's private key, contains the configured
   * subject and subject alternative names, and has a validity window starting on the current local
   * date.
   *
   * @return the generated certificate.
   * @throws Exception if key encoding, extension generation, signature creation, or certificate
   *     conversion fails.
   */
  public X509Certificate build() throws Exception {
    LocalDate now = LocalDate.now();
    LocalDate expiration = now.plus(validityPeriod);

    Date notBefore = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date notAfter = Date.from(expiration.atStartOfDay(ZoneId.systemDefault()).toInstant());

    return generator.generateSelfSigned(
        keyPair,
        notBefore,
        notAfter,
        commonName,
        organization,
        organizationalUnit,
        localityName,
        stateName,
        countryCode,
        applicationUri,
        dnsNames,
        ipAddresses,
        signatureAlgorithm);
  }

  private static boolean isEd25519PublicKey(PublicKey publicKey) {
    if (publicKey instanceof EdECPublicKey edPublicKey) {
      return NamedParameterSpec.ED25519.getName().equals(edPublicKey.getParams().getName());
    } else {
      return "Ed25519".equalsIgnoreCase(publicKey.getAlgorithm());
    }
  }

  private static boolean isEd448PublicKey(PublicKey publicKey) {
    if (publicKey instanceof EdECPublicKey edPublicKey) {
      return NamedParameterSpec.ED448.getName().equals(edPublicKey.getParams().getName());
    } else {
      return "Ed448".equalsIgnoreCase(publicKey.getAlgorithm());
    }
  }
}
