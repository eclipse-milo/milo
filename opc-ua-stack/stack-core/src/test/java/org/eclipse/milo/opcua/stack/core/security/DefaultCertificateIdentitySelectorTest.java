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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

@NullMarked
class DefaultCertificateIdentitySelectorTest {

  // CA-issued and ad-hoc RSA certificates commonly omit nonRepudiation/dataEncipherment (and
  // keyCertSign on self-signed certs). Those bits are a remote-side legacy requirement; gating the
  // application's OWN identity on them would silently drop the only usable certificate from
  // endpoint advertisement / client connection setup. Local selection must keep it.
  @Test
  void selectsRsaIdentityMissingStrictLegacyKeyUsageBits() throws Exception {
    CertificateIdentity identity =
        rsaIdentity(KeyUsage.digitalSignature | KeyUsage.keyEncipherment);

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            singleIdentityManager(identity),
            SecurityPolicy.Basic256Sha256.getProfile(),
            null,
            null,
            null);

    Optional<CertificateIdentity> selected =
        DefaultCertificateIdentitySelector.create().select(context);

    assertTrue(selected.isPresent());
    assertEquals(identity, selected.get());
  }

  // The selector must never silently swallow an explicitly configured certificate: if it is locally
  // compatible it must be returned (not Optional.empty), so a fixed-certificate endpoint built from
  // an external RSA cert is advertised rather than vanishing without cause.
  @Test
  void selectsExplicitlyConfiguredRsaIdentityMissingStrictLegacyKeyUsageBits() throws Exception {
    CertificateIdentity identity =
        rsaIdentity(KeyUsage.digitalSignature | KeyUsage.keyEncipherment);

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            singleIdentityManager(identity),
            SecurityPolicy.Basic256Sha256.getProfile(),
            null,
            null,
            identity.certificate());

    Optional<CertificateIdentity> selected =
        DefaultCertificateIdentitySelector.create().select(context);

    assertTrue(selected.isPresent());
    assertEquals(identity, selected.get());
  }

  private static CertificateIdentity rsaIdentity(int keyUsage) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair, new KeyUsageCertificateGenerator(keyUsage))
            .setApplicationUri("urn:eclipse:milo:test")
            .addDnsName("localhost")
            .build();

    return new CertificateIdentity(
        NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup,
        NodeIds.RsaSha256ApplicationCertificateType,
        keyPair,
        new X509Certificate[] {certificate});
  }

  private static CertificateManager singleIdentityManager(CertificateIdentity identity) {
    return new CertificateManager() {
      @Override
      public List<CertificateIdentity> getCertificateIdentities() {
        return List.of(identity);
      }

      @Override
      public Optional<KeyPair> getKeyPair(ByteString thumbprint) {
        return Optional.empty();
      }

      @Override
      public Optional<X509Certificate> getCertificate(ByteString thumbprint) {
        return Optional.empty();
      }

      @Override
      public Optional<X509Certificate[]> getCertificateChain(ByteString thumbprint) {
        return Optional.empty();
      }

      @Override
      public Optional<CertificateGroup> getCertificateGroup(ByteString thumbprint) {
        return Optional.empty();
      }

      @Override
      public Optional<CertificateGroup> getCertificateGroup(NodeId certificateGroupId) {
        return Optional.empty();
      }

      @Override
      public List<CertificateGroup> getCertificateGroups() {
        return List.of();
      }

      @Override
      public CertificateQuarantine getCertificateQuarantine() {
        throw new UnsupportedOperationException();
      }
    };
  }

  private static final class KeyUsageCertificateGenerator extends SelfSignedCertificateGenerator {
    private final int keyUsage;

    KeyUsageCertificateGenerator(int keyUsage) {
      this.keyUsage = keyUsage;
    }

    @Override
    protected void addExtendedKeyUsage(X509v3CertificateBuilder certificateBuilder) {}

    @Override
    protected void addKeyUsage(X509v3CertificateBuilder certificateBuilder) throws CertIOException {
      certificateBuilder.addExtension(Extension.keyUsage, false, new KeyUsage(keyUsage));
    }
  }
}
