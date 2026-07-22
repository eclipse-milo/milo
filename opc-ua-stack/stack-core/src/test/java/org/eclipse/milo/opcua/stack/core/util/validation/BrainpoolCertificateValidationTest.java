/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.stack.core.security.DefaultServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Brainpool certificates cannot be signature-verified by the JDK's default JCA providers (SunEC
 * throws "Curve not supported"). These tests pin that certificate trust-path validation is
 * provider-aware: self-signed Brainpool trust anchors and CA-signed Brainpool chains must validate
 * whether or not Bouncy Castle is registered as a JCA provider, so Brainpool policies work with the
 * default certificate validators without requiring operators to insert BC at JCA position 1.
 */
@NullMarked
class BrainpoolCertificateValidationTest {

  private static final SecurityPolicyProfile BRAINPOOL_P384_PROFILE =
      SecurityPolicy.ECC_brainpoolP384r1_AesGcm.getProfile();

  // Validation must not depend on the global provider list, so snapshot whether BC was registered
  // before each test and restore that exact state afterward. configureSignatureProvider() registers
  // BC append-only for CA-signed chains, which would otherwise leak into sibling test classes.
  private boolean bcWasRegistered;

  @AfterEach
  void restoreBcRegistration() {
    boolean registeredNow = Security.getProvider("BC") != null;
    if (registeredNow && !bcWasRegistered) {
      Security.removeProvider("BC");
    } else if (!registeredNow && bcWasRegistered) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  // Without this fix the self-signed Brainpool cert never becomes a trust anchor: the provider-less
  // cert.verify(key) throws SignatureException("Curve not supported"), certificateIsSelfSigned()
  // returns false, and PKIXBuilderParameters then rejects the empty trust-anchor set.
  @Test
  void selfSignedBrainpoolIsTrustAnchorWithoutBouncyCastleRegistered() throws Exception {
    startWithBouncyCastleRemoved();

    X509Certificate selfSigned = sunDecode(generateSelfSignedBrainpoolP384());

    assertTrue(CertificateValidationUtil.certificateIsSelfSigned(selfSigned));
    assertDoesNotThrow(
        () ->
            serverValidator(selfSigned)
                .validateCertificateChain(List.of(selfSigned), null, null, BRAINPOOL_P384_PROFILE));
  }

  // The same path must also work when BC is registered as the lowest-precedence provider; SunEC
  // still wins provider selection for EC keys, so the BC-aware fallback is exercised either way.
  @Test
  void selfSignedBrainpoolIsTrustAnchorWithBouncyCastleAppended() throws Exception {
    startWithBouncyCastleAppended();

    X509Certificate selfSigned = sunDecode(generateSelfSignedBrainpoolP384());

    assertTrue(CertificateValidationUtil.certificateIsSelfSigned(selfSigned));
    assertDoesNotThrow(
        () ->
            serverValidator(selfSigned)
                .validateCertificateChain(List.of(selfSigned), null, null, BRAINPOOL_P384_PROFILE));
  }

  // CA-signed Brainpool chains fail the same way inside the SUN PKIX validator, which verifies each
  // link's signature through the default providers; signature verification must be routed to BC for
  // both path building and path validation.
  @Test
  void caSignedBrainpoolChainValidatesWithoutBouncyCastleRegistered() throws Exception {
    startWithBouncyCastleRemoved();

    BrainpoolChain chain = generateCaSignedBrainpoolP384Chain();

    assertTrue(CertificateValidationUtil.certificateIsSelfSigned(chain.root()));
    assertDoesNotThrow(
        () ->
            serverValidator(chain.root())
                .validateCertificateChain(
                    List.of(chain.leaf()), null, null, BRAINPOOL_P384_PROFILE));
  }

  @Test
  void caSignedBrainpoolChainValidatesWithBouncyCastleAppended() throws Exception {
    startWithBouncyCastleAppended();

    BrainpoolChain chain = generateCaSignedBrainpoolP384Chain();

    assertDoesNotThrow(
        () ->
            serverValidator(chain.root())
                .validateCertificateChain(
                    List.of(chain.leaf()), null, null, BRAINPOOL_P384_PROFILE));
  }

  private void startWithBouncyCastleRemoved() {
    bcWasRegistered = Security.getProvider("BC") != null;
    Security.removeProvider("BC");
  }

  private void startWithBouncyCastleAppended() {
    bcWasRegistered = Security.getProvider("BC") != null;
    if (!bcWasRegistered) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  private static DefaultServerCertificateValidator serverValidator(X509Certificate trusted) {
    MemoryTrustListManager trustListManager = new MemoryTrustListManager();
    trustListManager.addTrustedCertificate(trusted);

    // NO_OPTIONAL_CHECKS mirrors the default server validator: it isolates the signature/trust-path
    // verification under test rather than failing on absent CRLs for the CA-signed chain.
    return new DefaultServerCertificateValidator(
        trustListManager, ValidationCheck.NO_OPTIONAL_CHECKS, new MemoryCertificateQuarantine());
  }

  private static X509Certificate generateSelfSignedBrainpoolP384() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateBrainpoolP384r1KeyPair();

    return SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
        .setApplicationUri("urn:eclipse:milo:test:brainpool")
        .addDnsName("localhost")
        .build();
  }

  private static BrainpoolChain generateCaSignedBrainpoolP384Chain() throws Exception {
    KeyPair rootKeyPair = SelfSignedCertificateGenerator.generateBrainpoolP384r1KeyPair();
    X509Certificate root =
        new SelfSignedCertificateBuilder(rootKeyPair, new BrainpoolCaGenerator())
            .setCommonName("Brainpool Root CA")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:eclipse:milo:test:brainpool-root")
            .setSignatureAlgorithm(SelfSignedCertificateBuilder.SA_SHA384_ECDSA)
            .build();

    KeyPair leafKeyPair = SelfSignedCertificateGenerator.generateBrainpoolP384r1KeyPair();
    X509Certificate leaf =
        new CaSignedCertificateBuilder(leafKeyPair, root, rootKeyPair.getPrivate())
            .setCommonName("Brainpool Leaf")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:eclipse:milo:test:brainpool-leaf")
            .addDnsName("localhost")
            .setIsCa(false)
            .setKeyUsage(KeyUsage.digitalSignature)
            .setSignatureAlgorithm(SelfSignedCertificateBuilder.SA_SHA384_ECDSA)
            .build();

    // Trust-list and wire certificates are decoded by the default (Sun) CertificateFactory in
    // production, so both ends of the chain are Sun objects here.
    return new BrainpoolChain(sunDecode(root), sunDecode(leaf));
  }

  /** Re-decode a certificate via the default (Sun) {@link CertificateFactory}, as on the wire. */
  private static X509Certificate sunDecode(X509Certificate certificate) throws Exception {
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

    return (X509Certificate)
        certificateFactory.generateCertificate(new ByteArrayInputStream(certificate.getEncoded()));
  }

  /** Generates a self-signed Brainpool CA: BasicConstraints cA=true with keyCertSign + cRLSign. */
  private static final class BrainpoolCaGenerator extends SelfSignedCertificateGenerator {
    @Override
    protected void addBasicConstraints(
        X509v3CertificateBuilder certificateBuilder, BasicConstraints basicConstraints)
        throws CertIOException {

      super.addBasicConstraints(certificateBuilder, new BasicConstraints(true));
    }

    @Override
    protected void addKeyUsage(X509v3CertificateBuilder certificateBuilder) throws CertIOException {
      certificateBuilder.addExtension(
          Extension.keyUsage,
          false,
          new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign | KeyUsage.digitalSignature));
    }

    @Override
    protected void addExtendedKeyUsage(X509v3CertificateBuilder certificateBuilder) {}
  }

  private record BrainpoolChain(X509Certificate root, X509Certificate leaf) {}
}
