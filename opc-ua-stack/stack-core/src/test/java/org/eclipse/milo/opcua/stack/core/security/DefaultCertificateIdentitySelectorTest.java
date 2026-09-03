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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelectionContext.Purpose;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

@NullMarked
class DefaultCertificateIdentitySelectorTest {

  private static final int LEGACY_KEY_USAGE = KeyUsage.digitalSignature | KeyUsage.keyEncipherment;

  // CA-issued and ad-hoc RSA certificates commonly omit nonRepudiation/dataEncipherment (and
  // keyCertSign on self-signed certs). Those bits are a remote-side legacy requirement; gating the
  // application's OWN identity on them would silently drop the only usable certificate from
  // endpoint advertisement / client connection setup. Local selection must keep it.
  @Test
  void selectsRsaIdentityMissingStrictLegacyKeyUsageBits() throws Exception {
    CertificateGroup group =
        group(rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType, LEGACY_KEY_USAGE));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            List.of(group), SecurityPolicy.Basic256Sha256.getProfile(), null, null);

    assertSelected(onlyIdentity(group), select(context));
  }

  // The selector must never silently swallow an explicitly configured certificate: if it is locally
  // compatible it must be returned (not Optional.empty), so a fixed-certificate endpoint built from
  // an external RSA cert is advertised rather than vanishing without cause.
  @Test
  void selectsExplicitlyConfiguredRsaIdentityMissingStrictLegacyKeyUsageBits() throws Exception {
    CertificateGroup group =
        group(rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType, LEGACY_KEY_USAGE));
    CertificateIdentity identity = onlyIdentity(group);

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            List.of(group),
            SecurityPolicy.Basic256Sha256.getProfile(),
            null,
            identity.certificate());

    assertSelected(identity, select(context));
  }

  // An explicit certificate pins the configured identity. If no candidate group holds it, the
  // caller must receive Optional.empty so it can report the misconfiguration or fall back to the
  // matching fixed key pair and chain.
  @Test
  void returnsEmptyWhenExplicitCertificateIsHeldByNoCandidateGroup() throws Exception {
    CertificateGroup candidate =
        group(rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType, LEGACY_KEY_USAGE));
    CertificateGroup nonCandidate =
        group(rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType, LEGACY_KEY_USAGE));

    var context =
        new CertificateIdentitySelectionContext(
            Purpose.CLIENT_CONNECTION_SETUP,
            List.of(candidate),
            SecurityPolicy.Basic256Sha256.getProfile(),
            null,
            onlyIdentity(nonCandidate).certificate());

    assertTrue(select(context).isEmpty());
  }

  // A candidate entry for the explicit certificate must not allow selection to fall through to a
  // different identity when the pinned identity is incompatible with the security policy.
  @Test
  void returnsEmptyWhenExplicitCertificateIsIncompatible() throws Exception {
    Material compatible =
        rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType, LEGACY_KEY_USAGE);
    Material explicit = rsaMaterial(NodeIds.RsaMinApplicationCertificateType, LEGACY_KEY_USAGE);
    CertificateGroup group = group(compatible, explicit);

    var context =
        new CertificateIdentitySelectionContext(
            Purpose.CLIENT_CONNECTION_SETUP,
            List.of(group),
            SecurityPolicy.Basic256Sha256.getProfile(),
            null,
            explicit.certificate());

    assertTrue(select(context).isEmpty());
  }

  // Candidate order is the application's precedence between groups; when two groups hold equally
  // suitable identities the earlier group must win, and reversing the list must reverse the choice.
  @Test
  void candidateGroupOrderDecidesBetweenEquallySuitableIdentities() throws Exception {
    CertificateGroup first = group(rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType));
    CertificateGroup second = group(rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType));
    SecurityPolicyProfile profile = SecurityPolicy.Basic256Sha256.getProfile();

    assertSelected(
        onlyIdentity(first),
        select(
            CertificateIdentitySelectionContext.forEndpointAdvertisement(
                List.of(first, second), profile, null, null)),
        "first candidate group wins");
    assertSelected(
        onlyIdentity(second),
        select(
            CertificateIdentitySelectionContext.forEndpointAdvertisement(
                List.of(second, first), profile, null, null)),
        "swapping candidate order swaps the result");
  }

  // An explicit certificate type request is a stronger signal than group precedence; otherwise an
  // endpoint configured for RsaMin would be advertised with the first group's RsaSha256 identity.
  @Test
  void requestedCertificateTypeWinsOverCandidateGroupOrder() throws Exception {
    CertificateGroup first = group(rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType));
    CertificateGroup second = group(rsaMaterial(NodeIds.RsaMinApplicationCertificateType));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forClientConnectionSetup(
            List.of(first, second),
            SecurityPolicy.Basic256.getProfile(),
            NodeIds.RsaMinApplicationCertificateType);

    assertSelected(onlyIdentity(second), select(context));
  }

  // Without a requested type the profile's preferred type still outranks group precedence, so a
  // Basic256 endpoint picks the stronger RsaSha256 identity even when a RsaMin group comes first.
  @Test
  void profilePreferredCertificateTypeWinsOverCandidateGroupOrder() throws Exception {
    CertificateGroup first = group(rsaMaterial(NodeIds.RsaMinApplicationCertificateType));
    CertificateGroup second = group(rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType));
    SecurityPolicyProfile profile = SecurityPolicy.Basic256.getProfile();
    assertEquals(
        Optional.of(NodeIds.RsaSha256ApplicationCertificateType),
        profile.preferredCertificateTypeId(),
        "precondition: Basic256 prefers RsaSha256");

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            List.of(first, second), profile, null, null);

    assertSelected(onlyIdentity(second), select(context));
  }

  // A fixed endpoint certificate must resolve to the group that actually holds it, even when an
  // equally compatible identity in an earlier group would otherwise win on precedence.
  @Test
  void explicitCertificatePinsSelectionToMatchingCandidateInLaterGroup() throws Exception {
    CertificateGroup first = group(rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType));
    CertificateGroup second = group(rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType));
    CertificateIdentity pinned = onlyIdentity(second);

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            List.of(first, second),
            SecurityPolicy.Basic256Sha256.getProfile(),
            null,
            pinned.certificate());

    assertSelected(pinned, select(context));
  }

  private static Optional<CertificateIdentity> select(CertificateIdentitySelectionContext context)
      throws UaException {

    return DefaultCertificateIdentitySelector.create().select(context);
  }

  private static void assertSelected(
      CertificateIdentity expected, Optional<CertificateIdentity> selected) {

    assertSelected(expected, selected, "expected an identity to be selected");
  }

  // KeyPair has no value equality and DefaultCertificateGroup builds a new KeyPair on every read,
  // so identities are compared by group, type, and certificate rather than with equals().
  private static void assertSelected(
      CertificateIdentity expected, Optional<CertificateIdentity> selected, String message) {

    assertTrue(selected.isPresent(), message);
    CertificateIdentity actual = selected.get();
    assertSame(expected.certificateGroup(), actual.certificateGroup(), message);
    assertEquals(expected.certificateTypeId(), actual.certificateTypeId(), message);
    assertEquals(expected.certificate(), actual.certificate(), message);
  }

  private static CertificateIdentity onlyIdentity(CertificateGroup group) {
    List<CertificateIdentity> identities = group.getCertificateIdentities();
    assertEquals(1, identities.size(), "fixture group holds exactly one identity");

    return identities.get(0);
  }

  private static CertificateGroup group(Material... materials) throws Exception {
    var certificateStore = new MemoryCertificateStore();
    for (Material material : materials) {
      certificateStore.set(
          material.certificateTypeId(),
          new CertificateStore.Entry(material.keyPair().getPrivate(), material.certificateChain()));
    }
    List<NodeId> certificateTypeIds =
        Arrays.stream(materials).map(Material::certificateTypeId).toList();

    return new DefaultCertificateGroup(
        new MemoryTrustListManager(),
        certificateStore,
        new MemoryCertificateQuarantine(),
        new CertificateValidator.InsecureCertificateValidator(),
        certificateTypeIds);
  }

  private static Material rsaMaterial(NodeId certificateTypeId) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setApplicationUri("urn:eclipse:milo:test")
            .addDnsName("localhost")
            .build();

    return new Material(certificateTypeId, keyPair, new X509Certificate[] {certificate});
  }

  private static Material rsaMaterial(NodeId certificateTypeId, int keyUsage) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair, new KeyUsageCertificateGenerator(keyUsage))
            .setApplicationUri("urn:eclipse:milo:test")
            .addDnsName("localhost")
            .build();

    return new Material(certificateTypeId, keyPair, new X509Certificate[] {certificate});
  }

  private record Material(
      NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {

    X509Certificate certificate() {
      return certificateChain[0];
    }
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
