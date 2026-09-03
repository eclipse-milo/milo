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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
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

  @Test
  void duplicateGroupIdAtomicallyReplacesAndReturnsPreviousGroup() {
    var groupId = new NodeId(2, "dynamic");
    TrackingApplicationGroup original = newGroup(groupId);
    TrackingApplicationGroup replacement = newGroup(groupId);
    var manager = new DefaultCertificateManager(new MemoryCertificateQuarantine());

    assertTrue(manager.addCertificateGroup(original).isEmpty());
    assertSame(original, manager.getCertificateGroup(groupId).orElseThrow());

    assertSame(original, manager.addCertificateGroup(replacement).orElseThrow());
    assertSame(replacement, manager.getCertificateGroup(groupId).orElseThrow());
    assertFalse(original.trustListManager.closed);
  }

  @Test
  void removeReturnsGroupWithoutClosingApplicationResources() {
    var groupId = new NodeId(2, "dynamic");
    TrackingApplicationGroup group = newGroup(groupId);
    var manager = new DefaultCertificateManager(new MemoryCertificateQuarantine(), group);

    assertSame(group, manager.removeCertificateGroup(groupId).orElseThrow());
    assertTrue(manager.getCertificateGroup(groupId).isEmpty());
    assertFalse(group.trustListManager.closed);
    assertTrue(manager.removeCertificateGroup(groupId).isEmpty());
  }

  // A replacement can overlap a thumbprint scan. The result must stay bound to the group that
  // supplied the matching entry, not combine that entry with the replacement group's key pair.
  @Test
  void thumbprintLookupStaysBoundToMatchedGroupDuringReplacement() throws Exception {
    var groupId = new NodeId(2, "dynamic");
    GroupFixture originalFixture = newPopulatedGroup(groupId);
    GroupFixture replacementFixture = newPopulatedGroup(groupId);
    var manager =
        new DefaultCertificateManager(new MemoryCertificateQuarantine(), originalFixture.group);
    originalFixture.group.afterEntriesRead =
        () -> manager.addCertificateGroup(replacementFixture.group);
    ByteString thumbprint = CertificateUtil.thumbprint(originalFixture.certificateChain[0]);

    Optional<KeyPair> keyPair = manager.getKeyPair(thumbprint);

    assertTrue(keyPair.isPresent());
    assertEquals(originalFixture.keyPair.getPrivate(), keyPair.orElseThrow().getPrivate());
    assertSame(replacementFixture.group, manager.getCertificateGroup(groupId).orElseThrow());
  }

  private static TrackingApplicationGroup newGroup(NodeId groupId) {
    return new TrackingApplicationGroup(
        groupId,
        new TrackingTrustListManager(),
        new MemoryCertificateStore(),
        new TestCertificateFactory());
  }

  private static GroupFixture newPopulatedGroup(NodeId groupId) throws Exception {
    var certificateFactory = new TestCertificateFactory();
    KeyPair keyPair = certificateFactory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain =
        certificateFactory.createRsaSha256CertificateChain(keyPair);
    var certificateStore = new MemoryCertificateStore();
    certificateStore.set(
        NodeIds.RsaSha256ApplicationCertificateType,
        new CertificateStore.Entry(keyPair.getPrivate(), certificateChain));
    var group =
        new TrackingApplicationGroup(
            groupId, new TrackingTrustListManager(), certificateStore, certificateFactory);

    return new GroupFixture(group, keyPair, certificateChain);
  }

  private record GroupFixture(
      TrackingApplicationGroup group, KeyPair keyPair, X509Certificate[] certificateChain) {}

  private static final class TrackingApplicationGroup extends DefaultApplicationGroup {

    private final TrackingTrustListManager trustListManager;
    private Runnable afterEntriesRead = () -> {};

    private TrackingApplicationGroup(
        NodeId certificateGroupId,
        TrackingTrustListManager trustListManager,
        CertificateStore certificateStore,
        CertificateFactory certificateFactory) {

      super(
          certificateGroupId,
          trustListManager,
          certificateStore,
          certificateFactory,
          new CertificateValidator.InsecureCertificateValidator());

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
