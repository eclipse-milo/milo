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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup.Entry;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.junit.jupiter.api.Test;

class DefaultCertificateManagerTest {

  private static final NodeId GROUP_ID_A = new NodeId(2, "group-a");
  private static final NodeId GROUP_ID_B = new NodeId(2, "group-b");
  private static final NodeId GROUP_ID_C = new NodeId(2, "group-c");

  // Endpoint configuration, Push management, and address-space code name a group by NodeId, while
  // a group's own consumers need the NodeId back; both directions must resolve after registration.
  @Test
  void registeredGroupResolvesByIdAndIdResolvesByGroup() {
    TrackingApplicationGroup group = newGroup();
    var manager = new DefaultCertificateManager();

    assertTrue(manager.getCertificateGroupId(group).isEmpty(), "unregistered group has no id");

    manager.addCertificateGroup(GROUP_ID_A, group);

    assertSame(group, manager.getCertificateGroup(GROUP_ID_A).orElseThrow());
    assertEquals(GROUP_ID_A, manager.getCertificateGroupId(group).orElseThrow());
  }

  // Registration order is the precedence between groups, so it must survive replace-in-place and
  // removal rather than reflecting insertion order into an unordered map.
  @Test
  void getCertificateGroupsPreservesRegistrationOrderAcrossAddReplaceAndRemove() {
    TrackingApplicationGroup a = newGroup();
    TrackingApplicationGroup b = newGroup();
    TrackingApplicationGroup c = newGroup();
    TrackingApplicationGroup replacementB = newGroup();
    var manager = new DefaultCertificateManager();

    manager.addCertificateGroup(GROUP_ID_A, a);
    manager.addCertificateGroup(GROUP_ID_B, b);
    manager.addCertificateGroup(GROUP_ID_C, c);
    assertEquals(List.of(a, b, c), manager.getCertificateGroups());

    manager.addCertificateGroup(GROUP_ID_B, replacementB);
    assertEquals(
        List.of(a, replacementB, c),
        manager.getCertificateGroups(),
        "replacing a group keeps its position");
    assertTrue(
        manager.getCertificateGroupId(b).isEmpty(), "replaced group is no longer registered");
    assertEquals(GROUP_ID_B, manager.getCertificateGroupId(replacementB).orElseThrow());

    manager.removeCertificateGroup(GROUP_ID_A);
    assertEquals(List.of(replacementB, c), manager.getCertificateGroups());
  }

  @Test
  void registeringUnderExistingIdReplacesAndReturnsPreviousGroup() {
    TrackingApplicationGroup original = newGroup();
    TrackingApplicationGroup replacement = newGroup();
    var manager = new DefaultCertificateManager();

    assertTrue(manager.addCertificateGroup(GROUP_ID_A, original).isEmpty());
    assertSame(original, manager.getCertificateGroup(GROUP_ID_A).orElseThrow());

    assertSame(original, manager.addCertificateGroup(GROUP_ID_A, replacement).orElseThrow());
    assertSame(replacement, manager.getCertificateGroup(GROUP_ID_A).orElseThrow());
    assertFalse(original.trustListManager.closed);
  }

  // A group registered under two ids would make getCertificateGroupId ambiguous and would list the
  // same identities twice; the registry must reject it and stay unchanged.
  @Test
  void registeringSameGroupInstanceUnderSecondIdThrows() {
    TrackingApplicationGroup group = newGroup();
    var manager = new DefaultCertificateManager();
    manager.addCertificateGroup(GROUP_ID_A, group);

    assertThrows(
        IllegalArgumentException.class, () -> manager.addCertificateGroup(GROUP_ID_B, group));

    assertEquals(List.of(group), manager.getCertificateGroups());
    assertEquals(GROUP_ID_A, manager.getCertificateGroupId(group).orElseThrow());
    assertTrue(manager.getCertificateGroup(GROUP_ID_B).isEmpty());
  }

  @Test
  void removeByIdReturnsGroupWithoutClosingApplicationResources() {
    TrackingApplicationGroup group = newGroup();
    var manager = new DefaultCertificateManager();
    manager.addCertificateGroup(GROUP_ID_A, group);

    assertSame(group, manager.removeCertificateGroup(GROUP_ID_A).orElseThrow());
    assertTrue(manager.getCertificateGroup(GROUP_ID_A).isEmpty());
    assertFalse(group.trustListManager.closed);
    assertTrue(manager.removeCertificateGroup(GROUP_ID_A).isEmpty());
  }

  @Test
  void removeByInstanceRemovesRegisteredGroupAndReportsUnregisteredGroup() {
    TrackingApplicationGroup registered = newGroup();
    TrackingApplicationGroup unregistered = newGroup();
    var manager = new DefaultCertificateManager();
    manager.addCertificateGroup(GROUP_ID_A, registered);

    assertFalse(manager.removeCertificateGroup(unregistered), "not registered, nothing removed");
    assertEquals(List.of(registered), manager.getCertificateGroups());

    assertTrue(manager.removeCertificateGroup(registered));
    assertTrue(manager.getCertificateGroup(GROUP_ID_A).isEmpty());
    assertTrue(manager.getCertificateGroupId(registered).isEmpty());
    assertEquals(List.of(), manager.getCertificateGroups());
    assertFalse(manager.removeCertificateGroup(registered), "second removal finds nothing");
  }

  // Precedence between groups is the application's registration order, not a global sort by
  // certificate type id: RsaMin (i=12559) sorts before RsaSha256 (i=12560), so a cross-group sort
  // would reverse these two.
  @Test
  void getCertificateIdentitiesConcatenatesGroupsInRegistrationOrder() throws Exception {
    GroupFixture rsaSha256 = newPopulatedGroup(NodeIds.RsaSha256ApplicationCertificateType);
    GroupFixture rsaMin = newPopulatedGroup(NodeIds.RsaMinApplicationCertificateType);

    var manager = new DefaultCertificateManager();
    manager.addCertificateGroup(GROUP_ID_A, rsaSha256.group);
    manager.addCertificateGroup(GROUP_ID_B, rsaMin.group);

    assertEquals(
        List.of(
            new IdentityKey(rsaSha256.group, NodeIds.RsaSha256ApplicationCertificateType),
            new IdentityKey(rsaMin.group, NodeIds.RsaMinApplicationCertificateType)),
        identityKeys(manager.getCertificateIdentities()));

    var reversed = new DefaultCertificateManager();
    reversed.addCertificateGroup(GROUP_ID_B, rsaMin.group);
    reversed.addCertificateGroup(GROUP_ID_A, rsaSha256.group);

    assertEquals(
        List.of(
            new IdentityKey(rsaMin.group, NodeIds.RsaMinApplicationCertificateType),
            new IdentityKey(rsaSha256.group, NodeIds.RsaSha256ApplicationCertificateType)),
        identityKeys(reversed.getCertificateIdentities()),
        "registering in the other order lists the other group first");
  }

  // GetRejectedList is one server-wide list, but rejections are recorded per group. The union must
  // keep registration order and list a certificate rejected by two groups once, or a Push client
  // would see duplicates and an order unrelated to group precedence.
  @Test
  void getRejectedCertificatesUnionsGroupQuarantinesInOrderWithoutDuplicates() throws Exception {
    var certificateFactory = new TestCertificateFactory();
    X509Certificate first = newCertificate(certificateFactory);
    X509Certificate shared = newCertificate(certificateFactory);
    X509Certificate second = newCertificate(certificateFactory);
    TrackingApplicationGroup a = newGroup();
    TrackingApplicationGroup b = newGroup();
    a.getCertificateQuarantine().addRejectedCertificate(first);
    a.getCertificateQuarantine().addRejectedCertificate(shared);
    b.getCertificateQuarantine().addRejectedCertificate(shared);
    b.getCertificateQuarantine().addRejectedCertificate(second);

    var manager = new DefaultCertificateManager();
    manager.addCertificateGroup(GROUP_ID_A, a);
    manager.addCertificateGroup(GROUP_ID_B, b);

    assertEquals(List.of(first, shared, second), manager.getRejectedCertificates());
    assertTrue(new DefaultCertificateManager().getRejectedCertificates().isEmpty());
  }

  // OPN receiver thumbprints can identify a certificate held by any registered group; a lookup
  // that only scanned the first group would break SecureChannels opened against later groups.
  @Test
  void thumbprintLookupsFindIdentityInSecondRegisteredGroup() throws Exception {
    GroupFixture first = newPopulatedGroup(NodeIds.RsaSha256ApplicationCertificateType);
    GroupFixture second = newPopulatedGroup(NodeIds.RsaSha256ApplicationCertificateType);
    var manager = new DefaultCertificateManager();
    manager.addCertificateGroup(GROUP_ID_A, first.group);
    manager.addCertificateGroup(GROUP_ID_B, second.group);
    ByteString thumbprint = CertificateUtil.thumbprint(second.certificateChain[0]);

    assertEquals(
        second.keyPair.getPrivate(), manager.getKeyPair(thumbprint).orElseThrow().getPrivate());
    assertEquals(second.certificateChain[0], manager.getCertificate(thumbprint).orElseThrow());
    assertArrayEquals(
        second.certificateChain, manager.getCertificateChain(thumbprint).orElseThrow());
    assertSame(second.group, manager.getCertificateGroup(thumbprint).orElseThrow());
  }

  // A replacement can overlap a thumbprint scan. The result must stay bound to the group that
  // supplied the matching entry, not combine that entry with the replacement group's key pair.
  @Test
  void thumbprintLookupStaysBoundToMatchedGroupDuringReplacement() throws Exception {
    GroupFixture originalFixture = newPopulatedGroup(NodeIds.RsaSha256ApplicationCertificateType);
    GroupFixture replacementFixture =
        newPopulatedGroup(NodeIds.RsaSha256ApplicationCertificateType);
    var manager = new DefaultCertificateManager();
    manager.addCertificateGroup(GROUP_ID_A, originalFixture.group);
    originalFixture.group.afterEntriesRead =
        () -> manager.addCertificateGroup(GROUP_ID_A, replacementFixture.group);
    ByteString thumbprint = CertificateUtil.thumbprint(originalFixture.certificateChain[0]);

    Optional<KeyPair> keyPair = manager.getKeyPair(thumbprint);

    assertTrue(keyPair.isPresent());
    assertEquals(originalFixture.keyPair.getPrivate(), keyPair.orElseThrow().getPrivate());
    assertSame(replacementFixture.group, manager.getCertificateGroup(GROUP_ID_A).orElseThrow());
  }

  private static TrackingApplicationGroup newGroup() {
    return new TrackingApplicationGroup(
        new TrackingTrustListManager(),
        new MemoryCertificateStore(),
        List.of(NodeIds.RsaSha256ApplicationCertificateType));
  }

  private static X509Certificate newCertificate(TestCertificateFactory certificateFactory)
      throws Exception {

    return certificateFactory
        .createRsaSha256CertificateChain(certificateFactory.createRsaSha256KeyPair())[0];
  }

  private static GroupFixture newPopulatedGroup(NodeId certificateTypeId) throws Exception {
    var certificateFactory = new TestCertificateFactory();
    KeyPair keyPair = certificateFactory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain =
        certificateFactory.createRsaSha256CertificateChain(keyPair);
    var certificateStore = new MemoryCertificateStore();
    certificateStore.set(
        certificateTypeId, new CertificateStore.Entry(keyPair.getPrivate(), certificateChain));
    var group =
        new TrackingApplicationGroup(
            new TrackingTrustListManager(), certificateStore, List.of(certificateTypeId));

    return new GroupFixture(group, keyPair, certificateChain);
  }

  private static List<IdentityKey> identityKeys(List<CertificateIdentity> identities) {
    return identities.stream()
        .map(identity -> new IdentityKey(identity.certificateGroup(), identity.certificateTypeId()))
        .toList();
  }

  private record IdentityKey(CertificateGroup certificateGroup, NodeId certificateTypeId) {}

  private record GroupFixture(
      TrackingApplicationGroup group, KeyPair keyPair, X509Certificate[] certificateChain) {}

  private static final class TrackingApplicationGroup extends DefaultCertificateGroup {

    private final TrackingTrustListManager trustListManager;
    private Runnable afterEntriesRead = () -> {};

    private TrackingApplicationGroup(
        TrackingTrustListManager trustListManager,
        CertificateStore certificateStore,
        List<NodeId> supportedCertificateTypeIds) {

      super(
          trustListManager,
          certificateStore,
          new MemoryCertificateQuarantine(),
          new CertificateValidator.InsecureCertificateValidator(),
          supportedCertificateTypeIds);

      this.trustListManager = trustListManager;
    }

    @Override
    public List<Entry> getCertificateEntries() {
      List<Entry> entries = super.getCertificateEntries();

      afterEntriesRead.run();
      afterEntriesRead = () -> {};

      return entries;
    }
  }

  private static final class TrackingTrustListManager extends MemoryTrustListManager {

    private boolean closed;

    @Override
    public void close() {
      closed = true;
    }
  }
}
