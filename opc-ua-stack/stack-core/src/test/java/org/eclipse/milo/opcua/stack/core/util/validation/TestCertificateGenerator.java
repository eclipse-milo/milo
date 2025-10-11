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

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;

/**
 * Utility class for generating test certificates for {@link CertificateValidationUtilTest}.
 *
 * <p>This class generates all the certificates and keys needed for validation testing, including CA
 * certificates, intermediate certificates, and end-entity certificates with various configurations.
 */
public class TestCertificateGenerator {

  /** Self-signed root CA certificate with KeyUsage and BasicConstraints CA=true. */
  public static final String ALIAS_CA_ROOT = "ca-root";

  /** Intermediate CA certificate signed by ca-root with BasicConstraints CA=true, pathlen=0. */
  public static final String ALIAS_CA_INTERMEDIATE = "ca-intermediate";

  /** Self-signed end-entity certificate with OPC UA-compliant extensions. */
  public static final String ALIAS_LEAF_SELF_SIGNED = "leaf-self-signed";

  /** End-entity certificate signed by ca-intermediate with OPC UA-compliant extensions. */
  public static final String ALIAS_LEAF_INTERMEDIATE_SIGNED = "leaf-intermediate-signed";

  /** Self-signed certificate with a URI containing spaces in the SubjectAlternativeName. */
  public static final String ALIAS_URI_WITH_SPACES = "uri-with-spaces";

  /** Certificate with KeyUsage extension present and BasicConstraints CA=true. */
  public static final String ALIAS_YES_KEY_USAGE_YES_CA = "yes-key-usage-yes-ca";

  /** Certificate without KeyUsage extension but with BasicConstraints CA=true. */
  public static final String ALIAS_NO_KEY_USAGE_YES_CA = "no-key-usage-yes-ca";

  /** Certificate with KeyUsage extension present but BasicConstraints CA=false. */
  public static final String ALIAS_YES_KEY_USAGE_NO_CA = "yes-key-usage-no-ca";

  /** Certificate without KeyUsage extension and BasicConstraints CA=false. */
  public static final String ALIAS_NO_KEY_USAGE_NO_CA = "no-key-usage-no-ca";

  /** Container for a certificate and its associated private key. */
  public record CertificateKeyPair(X509Certificate certificate, PrivateKey privateKey) {}

  /** Container for all generated test certificates. */
  public static class TestCertificates {
    private final Map<String, CertificateKeyPair> certificates = new HashMap<>();

    public void put(String alias, X509Certificate certificate, PrivateKey privateKey) {
      certificates.put(alias, new CertificateKeyPair(certificate, privateKey));
    }

    public X509Certificate getCertificate(String alias) {
      CertificateKeyPair pair = certificates.get(alias);
      return pair != null ? pair.certificate() : null;
    }

    public PrivateKey getPrivateKey(String alias) {
      CertificateKeyPair pair = certificates.get(alias);
      return pair != null ? pair.privateKey() : null;
    }
  }

  /**
   * Generate all test certificates needed for {@link CertificateValidationUtilTest}.
   *
   * @return {@link TestCertificates} containing all generated certificates and keys
   * @throws Exception if certificate generation fails
   */
  public static TestCertificates generateAll() throws Exception {
    TestCertificates testCerts = new TestCertificates();

    // Generate ca-root (self-signed root CA)
    KeyPair caRootKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate caRoot = createSelfSignedCertificate(caRootKeyPair, "Test Root CA", true, true);

    testCerts.put(ALIAS_CA_ROOT, caRoot, caRootKeyPair.getPrivate());

    // Generate ca-intermediate (signed by ca-root)
    KeyPair caIntermediateKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate caIntermediate =
        new CaSignedCertificateBuilder(caIntermediateKeyPair, caRoot, caRootKeyPair.getPrivate())
            .setCommonName("Test Intermediate CA")
            .setOrganization("Eclipse Milo")
            .setOrganizationalUnit("Test")
            .setLocalityName("Test City")
            .setStateName("Test State")
            .setCountryCode("US")
            .setIsCa(true)
            .setPathLengthConstraint(0)
            .setKeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign)
            .build();

    testCerts.put(ALIAS_CA_INTERMEDIATE, caIntermediate, caIntermediateKeyPair.getPrivate());

    // Generate leaf-self-signed (self-signed end-entity)
    KeyPair leafSelfSignedKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate leafSelfSigned =
        new SelfSignedCertificateBuilder(leafSelfSignedKeyPair)
            .setCommonName("Test Leaf Self-Signed")
            .setOrganization("Eclipse Milo")
            .setOrganizationalUnit("Test")
            .setApplicationUri("urn:eclipse:milo:test:leaf-self-signed")
            .addDnsName("localhost")
            .addIpAddress("127.0.0.1")
            .build();

    testCerts.put(ALIAS_LEAF_SELF_SIGNED, leafSelfSigned, leafSelfSignedKeyPair.getPrivate());

    // Generate leaf-intermediate-signed (signed by ca-intermediate)
    KeyPair leafIntermediateSignedKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate leafIntermediateSigned =
        new CaSignedCertificateBuilder(
                leafIntermediateSignedKeyPair, caIntermediate, caIntermediateKeyPair.getPrivate())
            .setCommonName("Test Leaf Intermediate-Signed")
            .setOrganization("Eclipse Milo")
            .setOrganizationalUnit("Test")
            .setApplicationUri("urn:eclipse:milo:test:leaf-intermediate-signed")
            .addDnsName("localhost")
            .addIpAddress("127.0.0.1")
            .setIsCa(false)
            .setKeyUsage(
                KeyUsage.digitalSignature
                    | KeyUsage.nonRepudiation
                    | KeyUsage.keyEncipherment
                    | KeyUsage.dataEncipherment)
            .setExtendedKeyUsage(
                List.of(KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth))
            .build();

    testCerts.put(
        ALIAS_LEAF_INTERMEDIATE_SIGNED,
        leafIntermediateSigned,
        leafIntermediateSignedKeyPair.getPrivate());

    // Generate uri-with-spaces
    KeyPair uriWithSpacesKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate uriWithSpaces =
        new SelfSignedCertificateBuilder(uriWithSpacesKeyPair)
            .setCommonName("Test URI with Spaces")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("this URI has spaces")
            .build();

    testCerts.put(ALIAS_URI_WITH_SPACES, uriWithSpaces, uriWithSpacesKeyPair.getPrivate());

    // Generate yes-key-usage-yes-ca
    KeyPair yesKeyUsageYesCaKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate yesKeyUsageYesCa =
        createSelfSignedCertificate(
            yesKeyUsageYesCaKeyPair, "Test Yes-KeyUsage Yes-CA", true, true);

    testCerts.put(
        ALIAS_YES_KEY_USAGE_YES_CA, yesKeyUsageYesCa, yesKeyUsageYesCaKeyPair.getPrivate());

    // Generate no-key-usage-yes-ca
    KeyPair noKeyUsageYesCaKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate noKeyUsageYesCa =
        createSelfSignedCertificate(noKeyUsageYesCaKeyPair, "Test No-KeyUsage Yes-CA", false, true);

    testCerts.put(ALIAS_NO_KEY_USAGE_YES_CA, noKeyUsageYesCa, noKeyUsageYesCaKeyPair.getPrivate());

    // Generate yes-key-usage-no-ca
    KeyPair yesKeyUsageNoCaKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate yesKeyUsageNoCa =
        createSelfSignedCertificate(yesKeyUsageNoCaKeyPair, "Test Yes-KeyUsage No-CA", true, false);

    testCerts.put(ALIAS_YES_KEY_USAGE_NO_CA, yesKeyUsageNoCa, yesKeyUsageNoCaKeyPair.getPrivate());

    // Generate no-key-usage-no-ca
    KeyPair noKeyUsageNoCaKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate noKeyUsageNoCa =
        createSelfSignedCertificate(noKeyUsageNoCaKeyPair, "Test No-KeyUsage No-CA", false, false);

    testCerts.put(ALIAS_NO_KEY_USAGE_NO_CA, noKeyUsageNoCa, noKeyUsageNoCaKeyPair.getPrivate());

    return testCerts;
  }

  /**
   * Create a self-signed certificate with specific KeyUsage and BasicConstraints configurations.
   *
   * @param keyPair the key pair for the certificate
   * @param commonName the common name for the certificate
   * @param hasKeyUsage whether to include KeyUsage extension
   * @param isCa whether BasicConstraints should indicate this is a CA
   * @return the generated certificate
   * @throws Exception if certificate generation fails
   */
  private static X509Certificate createSelfSignedCertificate(
      KeyPair keyPair, String commonName, boolean hasKeyUsage, boolean isCa) throws Exception {

    SelfSignedCertificateBuilder builder =
        new SelfSignedCertificateBuilder(
            keyPair, new CustomSelfSignedCertificateGenerator(hasKeyUsage, isCa));

    return builder
        .setCommonName(commonName)
        .setOrganization("Eclipse Milo")
        .setApplicationUri("urn:eclipse:milo:test:" + commonName.toLowerCase().replace(" ", "-"))
        .build();
  }

  /** Custom certificate generator that allows control over KeyUsage and BasicConstraints. */
  private static class CustomSelfSignedCertificateGenerator extends SelfSignedCertificateGenerator {
    private final boolean hasKeyUsage;
    private final boolean isCa;

    public CustomSelfSignedCertificateGenerator(boolean hasKeyUsage, boolean isCa) {
      this.hasKeyUsage = hasKeyUsage;
      this.isCa = isCa;
    }

    @Override
    protected void addKeyUsage(X509v3CertificateBuilder certificateBuilder) throws CertIOException {
      if (hasKeyUsage) {
        // Add CA-appropriate key usage
        if (isCa) {
          certificateBuilder.addExtension(
              Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));
        } else {
          super.addKeyUsage(certificateBuilder);
        }
      }
      // Otherwise, omit KeyUsage extension
    }

    @Override
    protected void addBasicConstraints(
        X509v3CertificateBuilder certificateBuilder, BasicConstraints basicConstraints)
        throws CertIOException {

      // Override the BasicConstraints to match our requirement
      super.addBasicConstraints(certificateBuilder, new BasicConstraints(isCa));
    }
  }
}
