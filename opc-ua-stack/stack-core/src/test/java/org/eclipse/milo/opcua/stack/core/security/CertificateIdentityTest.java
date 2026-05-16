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
import java.util.Arrays;
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
import org.junit.jupiter.api.Test;

class CertificateIdentityTest {

  private static final NodeId GROUP_A = new NodeId(2, "group-a");
  private static final NodeId GROUP_B = new NodeId(2, "group-b");

  private final TestCertificateFactory certificateFactory = new TestCertificateFactory();

  // Endpoint resolution needs a deterministic inventory and must ignore incomplete store entries.
  @Test
  void certificateManagerEnumeratesUsableIdentitiesAcrossGroupsAndTypes() {
    CertificateManager manager =
        manager(
            group(
                GROUP_B,
                certificate(NodeIds.EccCurve25519ApplicationCertificateType),
                certificateWithoutKeyPair()),
            group(
                GROUP_A,
                certificate(NodeIds.EccNistP256ApplicationCertificateType),
                certificate(NodeIds.RsaSha256ApplicationCertificateType)));

    List<IdentityKey> identities = identityKeys(manager.getCertificateIdentities());

    assertEquals(
        List.of(
            key(GROUP_A, NodeIds.RsaSha256ApplicationCertificateType),
            key(GROUP_A, NodeIds.EccNistP256ApplicationCertificateType),
            key(GROUP_B, NodeIds.EccCurve25519ApplicationCertificateType)),
        identities);
  }

  // Legacy endpoint certificate configuration must win over generic policy/type ordering.
  @Test
  void selectorPrefersExplicitCertificateMatchBeforeStableOrdering() throws Exception {
    CertificateMaterial stable = certificate(NodeIds.RsaSha256ApplicationCertificateType);
    CertificateMaterial explicit = certificate(NodeIds.EccNistP256ApplicationCertificateType);
    CertificateManager manager = manager(group(GROUP_A, stable), group(GROUP_B, explicit));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            manager, SecurityPolicy.None.getProfile(), null, null, explicit.certificate());

    CertificateIdentity selected =
        DefaultCertificateIdentitySelector.create().select(context).orElseThrow();

    assertEquals(key(GROUP_B, explicit.certificateTypeId()), key(selected));
  }

  // Endpoint certificate type configuration should outrank the policy default when both are usable.
  @Test
  void selectorPrefersExactCertificateTypeBeforePolicyPreferredType() throws Exception {
    CertificateMaterial policyPreferred = certificate(NodeIds.RsaSha256ApplicationCertificateType);
    CertificateMaterial exact = certificate(NodeIds.RsaMinApplicationCertificateType);
    CertificateManager manager = manager(group(GROUP_A, policyPreferred), group(GROUP_B, exact));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            manager, SecurityPolicy.Basic256.getProfile(), null, exact.certificateTypeId(), null);

    CertificateIdentity selected =
        DefaultCertificateIdentitySelector.create().select(context).orElseThrow();

    assertEquals(key(GROUP_B, exact.certificateTypeId()), key(selected));
  }

  // Certificate type selection is a preference here; compatible candidates can still fall back to
  // the policy-preferred certificate type.
  @Test
  void selectorFallsBackToPolicyPreferredTypeWhenExactTypeIsUnavailable() throws Exception {
    CertificateMaterial policyPreferred = certificate(NodeIds.RsaSha256ApplicationCertificateType);
    CertificateManager manager = manager(group(GROUP_A, policyPreferred));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            manager,
            SecurityPolicy.Basic256.getProfile(),
            null,
            NodeIds.RsaMinApplicationCertificateType,
            null);

    CertificateIdentity selected =
        DefaultCertificateIdentitySelector.create().select(context).orElseThrow();

    assertEquals(key(GROUP_A, policyPreferred.certificateTypeId()), key(selected));
  }

  // Policy profiles should keep secured endpoints from accidentally choosing a weaker fallback
  // type.
  @Test
  void selectorPrefersPolicyCertificateTypeBeforeStableOrdering() throws Exception {
    CertificateMaterial stable = certificate(NodeIds.RsaMinApplicationCertificateType);
    CertificateMaterial policyPreferred = certificate(NodeIds.RsaSha256ApplicationCertificateType);
    CertificateManager manager = manager(group(GROUP_A, stable), group(GROUP_B, policyPreferred));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            manager, SecurityPolicy.Basic256.getProfile(), null, null, null);

    CertificateIdentity selected =
        DefaultCertificateIdentitySelector.create().select(context).orElseThrow();

    assertEquals(key(GROUP_B, policyPreferred.certificateTypeId()), key(selected));
  }

  // Stable fallback ordering keeps endpoint advertisement independent from collection iteration.
  @Test
  void selectorUsesStableGroupAndTypeOrderingAsFinalTieBreaker() throws Exception {
    CertificateManager manager =
        manager(
            group(GROUP_B, certificate(NodeIds.EccNistP256ApplicationCertificateType)),
            group(
                GROUP_A,
                certificate(NodeIds.EccCurve25519ApplicationCertificateType),
                certificate(NodeIds.RsaMinApplicationCertificateType)));

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forEndpointAdvertisement(
            manager, SecurityPolicy.None.getProfile(), null, null, null);

    CertificateIdentity selected =
        DefaultCertificateIdentitySelector.create().select(context).orElseThrow();

    assertEquals(key(GROUP_A, NodeIds.RsaMinApplicationCertificateType), key(selected));
  }

  // SecureChannel OPN still identifies the local receiver certificate by SHA-1 thumbprint.
  @Test
  void managerPreservesThumbprintLookupRoundTripForSelectedIdentity() throws Exception {
    TestCertificateGroup group =
        group(GROUP_A, certificate(NodeIds.RsaSha256ApplicationCertificateType));
    CertificateManager manager = manager(group);
    CertificateIdentity identity = manager.getCertificateIdentities().get(0);

    ByteString thumbprint = identity.thumbprint();

    assertSame(identity.keyPair(), manager.getKeyPair(thumbprint).orElseThrow());
    assertEquals(identity.certificate(), manager.getCertificate(thumbprint).orElseThrow());
    assertArrayEquals(
        identity.certificateChain(), manager.getCertificateChain(thumbprint).orElseThrow());
    assertSame(group, manager.getCertificateGroup(thumbprint).orElseThrow());
  }

  private CertificateManager manager(TestCertificateGroup... groups) {
    List<CertificateGroup> certificateGroups =
        Arrays.stream(groups).map(CertificateGroup.class::cast).toList();

    return new DefaultCertificateManager(new MemoryCertificateQuarantine(), certificateGroups);
  }

  private TestCertificateGroup group(NodeId groupId, CertificateMaterial... certificates) {
    return new TestCertificateGroup(groupId, List.of(certificates));
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

  private static IdentityKey key(CertificateIdentity identity) {
    return key(identity.certificateGroupId(), identity.certificateTypeId());
  }

  private static IdentityKey key(NodeId certificateGroupId, NodeId certificateTypeId) {
    return new IdentityKey(certificateGroupId, certificateTypeId);
  }

  private static List<IdentityKey> identityKeys(List<CertificateIdentity> identities) {
    return identities.stream().map(CertificateIdentityTest::key).toList();
  }

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

  private record TestCertificateGroup(
      NodeId certificateGroupId, Map<NodeId, CertificateMaterial> certificates)
      implements CertificateGroup {
    private TestCertificateGroup(
        NodeId certificateGroupId, List<CertificateMaterial> certificates) {
      this(certificateGroupId, toCertificateMap(certificates));
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
    public NodeId getCertificateGroupId() {
      return certificateGroupId;
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
    public List<Entry> getCertificateEntries() {
      return certificates.values().stream()
          .map(
              certificate ->
                  new CertificateGroup.Entry(
                      certificateGroupId,
                      certificate.certificateTypeId(),
                      certificate.certificateChain()))
          .toList();
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
    public CertificateFactory getCertificateFactory() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateValidator getCertificateValidator() {
      throw new UnsupportedOperationException();
    }
  }
}
