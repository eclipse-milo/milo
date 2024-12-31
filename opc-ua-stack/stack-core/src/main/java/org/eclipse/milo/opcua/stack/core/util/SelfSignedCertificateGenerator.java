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

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
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
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.jspecify.annotations.Nullable;

public class SelfSignedCertificateGenerator {

  /**
   * Generate an RSA {@link KeyPair} of bit length {@code length}.
   *
   * @param length the length, in bits, of the key to generate.
   * @return a {@link KeyPair} of bit length {@code length}.
   * @throws NoSuchAlgorithmException if no {@link Provider} supports RSA KeyPair generation.
   */
  public static KeyPair generateRsaKeyPair(int length) throws NoSuchAlgorithmException {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(length, new SecureRandom());

    return generator.generateKeyPair();
  }

  /**
   * Generate an EC {@link KeyPair} of bit length {@code length}.
   *
   * @param length the length, in bits, of the key to generate.
   * @return a {@link KeyPair} of bit length {@code length}.
   * @throws NoSuchAlgorithmException if no {@link Provider} supports EC KeyPair generation.
   */
  public static KeyPair generateEcKeyPair(int length) throws NoSuchAlgorithmException {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
    generator.initialize(length, new SecureRandom());

    return generator.generateKeyPair();
  }

  public X509Certificate generateSelfSigned(
      KeyPair keyPair,
      Date notBefore,
      Date notAfter,
      @Nullable String commonName,
      @Nullable String organization,
      @Nullable String organizationalUnit,
      @Nullable String localityName,
      @Nullable String stateName,
      @Nullable String countryCode,
      @Nullable String applicationUri,
      List<String> dnsNames,
      List<String> ipAddresses,
      String signatureAlgorithm)
      throws Exception {

    X500NameBuilder nameBuilder = new X500NameBuilder();

    if (commonName != null) {
      nameBuilder.addRDN(BCStyle.CN, commonName);
    }
    if (organization != null) {
      nameBuilder.addRDN(BCStyle.O, organization);
    }
    if (organizationalUnit != null) {
      nameBuilder.addRDN(BCStyle.OU, organizationalUnit);
    }
    if (localityName != null) {
      nameBuilder.addRDN(BCStyle.L, localityName);
    }
    if (stateName != null) {
      nameBuilder.addRDN(BCStyle.ST, stateName);
    }
    if (countryCode != null) {
      nameBuilder.addRDN(BCStyle.C, countryCode);
    }

    X500Name name = nameBuilder.build();

    // Using the current timestamp as the certificate serial number
    BigInteger certSerialNumber = new BigInteger(Long.toString(System.currentTimeMillis()));

    SubjectPublicKeyInfo subjectPublicKeyInfo =
        SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());

    X509v3CertificateBuilder certificateBuilder =
        new X509v3CertificateBuilder(
            name,
            certSerialNumber,
            notBefore,
            notAfter,
            Locale.ENGLISH,
            name,
            subjectPublicKeyInfo);

    BasicConstraints basicConstraints = new BasicConstraints(false);

    // Authority Key Identifier
    addAuthorityKeyIdentifier(certificateBuilder, keyPair);

    // Basic Constraints
    addBasicConstraints(certificateBuilder, basicConstraints);

    // Key Usage
    addKeyUsage(certificateBuilder);

    // Extended Key Usage
    addExtendedKeyUsage(certificateBuilder);

    // Subject Alternative Name
    addSubjectAlternativeNames(certificateBuilder, keyPair, applicationUri, dnsNames, ipAddresses);

    ContentSigner contentSigner =
        new JcaContentSignerBuilder(signatureAlgorithm)
            .setProvider(new BouncyCastleProvider())
            .build(keyPair.getPrivate());

    X509CertificateHolder certificateHolder = certificateBuilder.build(contentSigner);

    return new JcaX509CertificateConverter().getCertificate(certificateHolder);
  }

  protected void addSubjectAlternativeNames(
      X509v3CertificateBuilder certificateBuilder,
      KeyPair keyPair,
      @Nullable String applicationUri,
      List<String> dnsNames,
      List<String> ipAddresses)
      throws CertIOException, NoSuchAlgorithmException {

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

    // Subject Key Identifier
    certificateBuilder.addExtension(
        Extension.subjectKeyIdentifier,
        false,
        new JcaX509ExtensionUtils().createSubjectKeyIdentifier(keyPair.getPublic()));
  }

  protected void addExtendedKeyUsage(X509v3CertificateBuilder certificateBuilder)
      throws CertIOException {
    certificateBuilder.addExtension(
        Extension.extendedKeyUsage,
        false,
        new ExtendedKeyUsage(
            new KeyPurposeId[] {KeyPurposeId.id_kp_clientAuth, KeyPurposeId.id_kp_serverAuth}));
  }

  protected void addKeyUsage(X509v3CertificateBuilder certificateBuilder) throws CertIOException {
    certificateBuilder.addExtension(
        Extension.keyUsage,
        false,
        new KeyUsage(
            KeyUsage.dataEncipherment
                | KeyUsage.digitalSignature
                | KeyUsage.keyCertSign
                | KeyUsage.keyEncipherment
                | KeyUsage.nonRepudiation));
  }

  protected void addBasicConstraints(
      X509v3CertificateBuilder certificateBuilder, BasicConstraints basicConstraints)
      throws CertIOException {

    certificateBuilder.addExtension(Extension.basicConstraints, false, basicConstraints);
  }

  protected void addAuthorityKeyIdentifier(
      X509v3CertificateBuilder certificateBuilder, KeyPair keyPair)
      throws CertIOException, NoSuchAlgorithmException {

    certificateBuilder.addExtension(
        Extension.authorityKeyIdentifier,
        false,
        new JcaX509ExtensionUtils().createAuthorityKeyIdentifier(keyPair.getPublic()));
  }
}
