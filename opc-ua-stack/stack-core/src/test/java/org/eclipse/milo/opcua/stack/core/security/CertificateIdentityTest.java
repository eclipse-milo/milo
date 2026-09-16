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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

@NullMarked
class CertificateIdentityTest {

  private static final NodeId GROUP_A = new NodeId(2, "group-a");
  private static final NodeId GROUP_B = new NodeId(2, "group-b");

  private final TestCertificateFactory certificateFactory = new TestCertificateFactory();

  // Endpoint resolution needs a deterministic inventory: groups in registration order, types in
  // type-id order within a group, and incomplete store entries left out.
  @Test
  void certificateManagerEnumeratesUsableIdentitiesInRegistrationOrderAcrossGroupsAndTypes() {
    DefaultCertificateManager manager =
        manager(
            registered(
                GROUP_B,
                group(
                    certificate(NodeIds.EccCurve25519ApplicationCertificateType),
                    certificateWithoutKeyPair())),
            registered(
                GROUP_A,
                group(
                    certificate(NodeIds.EccNistP256ApplicationCertificateType),
                    certificate(NodeIds.RsaSha256ApplicationCertificateType))));

    List<IdentityKey> identities = identityKeys(manager, manager.getCertificateIdentities());

    assertEquals(
        List.of(
            key(GROUP_B, NodeIds.EccCurve25519ApplicationCertificateType),
            key(GROUP_A, NodeIds.RsaSha256ApplicationCertificateType),
            key(GROUP_A, NodeIds.EccNistP256ApplicationCertificateType)),
        identities,
        "GROUP_B was registered first, so it lists first despite its higher type id");
  }

  // Legacy endpoint certificate configuration must win over generic policy/type ordering.
  @Test
  void selectorPrefersExplicitCertificateMatchBeforeRegistrationOrdering() throws Exception {
    CertificateMaterial stable = certificate(NodeIds.RsaSha256ApplicationCertificateType);
    CertificateMaterial explicit = certificate(NodeIds.EccNistP256ApplicationCertificateType);
    DefaultCertificateManager manager =
        manager(registered(GROUP_A, group(stable)), registered(GROUP_B, group(explicit)));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            manager.getCertificateGroups(),
            SecurityPolicy.None.getProfile(),
            null,
            explicit.certificate());

    CertificateIdentity selected =
        DefaultCertificateIdentitySelector.create().select(context).orElseThrow();

    assertEquals(key(GROUP_B, explicit.certificateTypeId()), key(manager, selected));
  }

  // Endpoint certificate type configuration should outrank the policy default when both are usable.
  @Test
  void selectorPrefersExactCertificateTypeBeforePolicyPreferredType() throws Exception {
    CertificateMaterial policyPreferred = certificate(NodeIds.RsaSha256ApplicationCertificateType);
    CertificateMaterial exact = certificate(NodeIds.RsaMinApplicationCertificateType);
    DefaultCertificateManager manager =
        manager(registered(GROUP_A, group(policyPreferred)), registered(GROUP_B, group(exact)));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            manager.getCertificateGroups(),
            SecurityPolicy.Basic256.getProfile(),
            exact.certificateTypeId(),
            null);

    CertificateIdentity selected =
        DefaultCertificateIdentitySelector.create().select(context).orElseThrow();

    assertEquals(key(GROUP_B, exact.certificateTypeId()), key(manager, selected));
  }

  // Certificate type selection is a preference here; compatible candidates can still fall back to
  // the policy-preferred certificate type.
  @Test
  void selectorFallsBackToPolicyPreferredTypeWhenExactTypeIsUnavailable() throws Exception {
    CertificateMaterial policyPreferred = certificate(NodeIds.RsaSha256ApplicationCertificateType);
    DefaultCertificateManager manager = manager(registered(GROUP_A, group(policyPreferred)));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            manager.getCertificateGroups(),
            SecurityPolicy.Basic256.getProfile(),
            NodeIds.RsaMinApplicationCertificateType,
            null);

    CertificateIdentity selected =
        DefaultCertificateIdentitySelector.create().select(context).orElseThrow();

    assertEquals(key(GROUP_A, policyPreferred.certificateTypeId()), key(manager, selected));
  }

  // Policy profiles should keep secured endpoints from accidentally choosing a weaker fallback
  // type.
  @Test
  void selectorPrefersPolicyCertificateTypeBeforeRegistrationOrdering() throws Exception {
    CertificateMaterial stable = certificate(NodeIds.RsaMinApplicationCertificateType);
    CertificateMaterial policyPreferred = certificate(NodeIds.RsaSha256ApplicationCertificateType);
    DefaultCertificateManager manager =
        manager(registered(GROUP_A, group(stable)), registered(GROUP_B, group(policyPreferred)));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            manager.getCertificateGroups(), SecurityPolicy.Basic256.getProfile(), null, null);

    CertificateIdentity selected =
        DefaultCertificateIdentitySelector.create().select(context).orElseThrow();

    assertEquals(key(GROUP_B, policyPreferred.certificateTypeId()), key(manager, selected));
  }

  // Registration order is the final tie-breaker, so the first registered group wins even though
  // the second group's RsaMin type (i=12559) would sort ahead of every ECC type (i=2353x) in a
  // global sort. Within the winning group the type-id order still applies.
  @Test
  void selectorUsesRegistrationOrderThenTypeOrderAsFinalTieBreaker() throws Exception {
    DefaultCertificateManager manager =
        manager(
            registered(
                GROUP_B,
                group(
                    certificate(NodeIds.EccCurve25519ApplicationCertificateType),
                    certificate(NodeIds.EccNistP256ApplicationCertificateType))),
            registered(GROUP_A, group(certificate(NodeIds.RsaMinApplicationCertificateType))));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            manager.getCertificateGroups(), SecurityPolicy.None.getProfile(), null, null);

    CertificateIdentity selected =
        DefaultCertificateIdentitySelector.create().select(context).orElseThrow();

    assertEquals(
        key(GROUP_B, NodeIds.EccNistP256ApplicationCertificateType), key(manager, selected));
  }

  // getCertificateEntries() and getKeyPair() read the store independently, so a rotation can
  // interleave and pair the old chain with the new key pair. The default getCertificateIdentities()
  // must omit such a mismatch instead of emitting a CertificateIdentity that violates its own
  // public-key invariant, which would otherwise surface as confusing OPN/session signature
  // failures.
  @Test
  void getCertificateIdentitiesOmitsChainAndKeyPairMismatchFromRotationRace() {
    CertificateMaterial staleChain = certificate(NodeIds.RsaSha256ApplicationCertificateType);
    KeyPair rotatedKeyPair = certificateFactory.createRsaSha256KeyPair();

    CertificateGroup group = new RotationRaceCertificateGroup(staleChain, rotatedKeyPair);

    assertEquals(List.of(), group.getCertificateIdentities());
  }

  // SecureChannel OPN still identifies the local receiver certificate by SHA-1 thumbprint.
  @Test
  void managerPreservesThumbprintLookupRoundTripForSelectedIdentity() throws Exception {
    TestCertificateGroup group = group(certificate(NodeIds.RsaSha256ApplicationCertificateType));
    DefaultCertificateManager manager = manager(registered(GROUP_A, group));
    CertificateIdentity identity = manager.getCertificateIdentities().get(0);

    ByteString thumbprint = identity.thumbprint();

    assertSame(identity.keyPair(), manager.getKeyPair(thumbprint).orElseThrow());
    assertEquals(identity.certificate(), manager.getCertificate(thumbprint).orElseThrow());
    assertArrayEquals(
        identity.certificateChain(), manager.getCertificateChain(thumbprint).orElseThrow());
    assertSame(group, manager.getCertificateGroup(thumbprint).orElseThrow());
  }

  private static DefaultCertificateManager manager(Registered... registrations) {
    var manager = new DefaultCertificateManager();
    for (Registered registration : registrations) {
      manager.addCertificateGroup(registration.groupId(), registration.group());
    }
    return manager;
  }

  private static Registered registered(NodeId groupId, CertificateGroup group) {
    return new Registered(groupId, group);
  }

  private static TestCertificateGroup group(CertificateMaterial... certificates) {
    return new TestCertificateGroup(List.of(certificates));
  }

  private CertificateMaterial certificate(NodeId certificateTypeId) {
    KeyPair keyPair = certificateFactory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain =
        certificateFactory.createRsaSha256CertificateChain(keyPair);

    return new CertificateMaterial(certificateTypeId, keyPair, certificateChain, true);
  }

  private CertificateMaterial certificateWithoutKeyPair() {
    KeyPair keyPair = certificateFactory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain =
        certificateFactory.createRsaSha256CertificateChain(keyPair);

    return new CertificateMaterial(
        NodeIds.EccNistP384ApplicationCertificateType, keyPair, certificateChain, false);
  }

  private static IdentityKey key(CertificateManager manager, CertificateIdentity identity) {
    NodeId groupId = manager.getCertificateGroupId(identity.certificateGroup()).orElseThrow();

    return key(groupId, identity.certificateTypeId());
  }

  private static IdentityKey key(NodeId certificateGroupId, NodeId certificateTypeId) {
    return new IdentityKey(certificateGroupId, certificateTypeId);
  }

  private static List<IdentityKey> identityKeys(
      CertificateManager manager, List<CertificateIdentity> identities) {

    return identities.stream().map(identity -> key(manager, identity)).toList();
  }

  private record Registered(NodeId groupId, CertificateGroup group) {}

  private record IdentityKey(NodeId certificateGroupId, NodeId certificateTypeId) {}

  private record CertificateMaterial(
      NodeId certificateTypeId,
      KeyPair keyPair,
      X509Certificate[] certificateChain,
      boolean keyPairAvailable) {

    X509Certificate certificate() {
      return certificateChain[0];
    }
  }

  private record TestCertificateGroup(Map<NodeId, CertificateMaterial> certificates)
      implements CertificateGroup {

    private TestCertificateGroup(List<CertificateMaterial> certificates) {
      this(toCertificateMap(certificates));
    }

    private static Map<NodeId, CertificateMaterial> toCertificateMap(
        List<CertificateMaterial> certificates) {

      Map<NodeId, CertificateMaterial> certificateMap =
          certificates.stream()
              .collect(
                  Collectors.toMap(
                      CertificateMaterial::certificateTypeId,
                      Function.identity(),
                      (left, right) -> right,
                      LinkedHashMap::new));

      return Collections.unmodifiableMap(certificateMap);
    }

    @Override
    public List<NodeId> getSupportedCertificateTypeIds() {
      return List.copyOf(certificates.keySet());
    }

    @Override
    public TrustListManager getTrustListManager() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateQuarantine getCertificateQuarantine() {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Entry> getCertificateEntries() {
      return certificates.values().stream()
          .map(
              certificate ->
                  new CertificateGroup.Entry(
                      certificate.certificateTypeId(), certificate.certificateChain()))
          .toList();
    }

    @Override
    public boolean hasCertificate(NodeId certificateTypeId) {
      return certificates.containsKey(certificateTypeId);
    }

    @Override
    public Optional<KeyPair> getKeyPair(NodeId certificateTypeId) {
      return Optional.ofNullable(certificates.get(certificateTypeId))
          .filter(CertificateMaterial::keyPairAvailable)
          .map(CertificateMaterial::keyPair);
    }

    @Override
    public Optional<X509Certificate[]> getCertificateChain(NodeId certificateTypeId) {
      return Optional.ofNullable(certificates.get(certificateTypeId))
          .map(CertificateMaterial::certificateChain)
          .map(X509Certificate[]::clone);
    }

    @Override
    public void updateCertificate(
        NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateValidator getCertificateValidator() {
      throw new UnsupportedOperationException();
    }
  }

  // Simulates the read interleaving: getCertificateEntries() returns the pre-rotation chain while
  // getKeyPair() returns the post-rotation key pair, so their public keys do not match.
  private record RotationRaceCertificateGroup(
      CertificateMaterial staleChain, KeyPair rotatedKeyPair) implements CertificateGroup {

    @Override
    public List<NodeId> getSupportedCertificateTypeIds() {
      return List.of(staleChain.certificateTypeId());
    }

    @Override
    public TrustListManager getTrustListManager() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateQuarantine getCertificateQuarantine() {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Entry> getCertificateEntries() {
      return List.of(
          new CertificateGroup.Entry(
              staleChain.certificateTypeId(), staleChain.certificateChain()));
    }

    @Override
    public boolean hasCertificate(NodeId certificateTypeId) {
      return certificateTypeId.equals(staleChain.certificateTypeId());
    }

    @Override
    public Optional<KeyPair> getKeyPair(NodeId certificateTypeId) {
      return Optional.of(rotatedKeyPair);
    }

    @Override
    public Optional<X509Certificate[]> getCertificateChain(NodeId certificateTypeId) {
      return Optional.of(staleChain.certificateChain());
    }

    @Override
    public void updateCertificate(
        NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateValidator getCertificateValidator() {
      throw new UnsupportedOperationException();
    }
  }
}
