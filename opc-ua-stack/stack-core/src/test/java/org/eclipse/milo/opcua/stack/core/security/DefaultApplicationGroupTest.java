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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup.Entry;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

@NullMarked
class DefaultApplicationGroupTest {

  @Test
  void defaultConstructorSupportsRsaSha256Only() {
    DefaultApplicationGroup group =
        new DefaultApplicationGroup(
            new MemoryTrustListManager(),
            new MemoryCertificateStore(),
            new TestCertificateFactory(),
            new CertificateValidator.InsecureCertificateValidator());

    assertEquals(
        List.of(NodeIds.RsaSha256ApplicationCertificateType),
        group.getSupportedCertificateTypeIds());
  }

  @Test
  void configuredCertificateTypesAreInitializedAndAccessible() throws Exception {
    List<NodeId> certificateTypeIds =
        List.of(
            NodeIds.RsaSha256ApplicationCertificateType,
            NodeIds.EccNistP256ApplicationCertificateType,
            NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
            NodeIds.EccCurve25519ApplicationCertificateType,
            NodeIds.EccCurve448ApplicationCertificateType);
    DefaultApplicationGroup group =
        new DefaultApplicationGroup(
            new MemoryTrustListManager(),
            new MemoryCertificateStore(),
            new CurrentEccCertificateFactory(),
            new CertificateValidator.InsecureCertificateValidator(),
            certificateTypeIds);

    group.initialize();

    assertEquals(certificateTypeIds, group.getSupportedCertificateTypeIds());
    assertEquals(5, group.getCertificateEntries().size());
    assertTrue(group.getKeyPair(NodeIds.RsaSha256ApplicationCertificateType).isPresent());
    assertTrue(group.getKeyPair(NodeIds.EccNistP256ApplicationCertificateType).isPresent());
    assertTrue(group.getKeyPair(NodeIds.EccBrainpoolP384r1ApplicationCertificateType).isPresent());
    assertTrue(group.getKeyPair(NodeIds.EccCurve25519ApplicationCertificateType).isPresent());
    assertTrue(group.getKeyPair(NodeIds.EccCurve448ApplicationCertificateType).isPresent());
    assertTrue(
        group.getCertificateChain(NodeIds.EccNistP256ApplicationCertificateType).isPresent());
    assertTrue(
        group
            .getCertificateChain(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)
            .isPresent());
    assertTrue(
        group.getCertificateChain(NodeIds.EccCurve25519ApplicationCertificateType).isPresent());
    assertTrue(
        group.getCertificateChain(NodeIds.EccCurve448ApplicationCertificateType).isPresent());
  }

  @Test
  void updateCertificateRejectsUnsupportedType() {
    DefaultApplicationGroup group =
        new DefaultApplicationGroup(
            new MemoryTrustListManager(),
            new MemoryCertificateStore(),
            new TestCertificateFactory(),
            new CertificateValidator.InsecureCertificateValidator());
    TestCertificateFactory factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain = factory.createRsaSha256CertificateChain(keyPair);

    assertThrows(
        UaException.class,
        () ->
            group.updateCertificate(
                NodeIds.EccNistP256ApplicationCertificateType, keyPair, certificateChain));
    assertFalse(group.getKeyPair(NodeIds.EccNistP256ApplicationCertificateType).isPresent());
  }

  // A single bad certificate type (corrupt entry, bad ECC alias password) must not empty the whole
  // group: the previously-working RSA identity must remain discoverable so its secured endpoints
  // keep being advertised and thumbprint lookups keep succeeding.
  @Test
  void getCertificateEntriesSkipsFailingTypeAndKeepsHealthyTypes() throws Exception {
    var store = new FailingGetCertificateStore(NodeIds.EccNistP256ApplicationCertificateType);
    DefaultApplicationGroup group =
        new DefaultApplicationGroup(
            new MemoryTrustListManager(),
            store,
            new CurrentEccCertificateFactory(),
            new CertificateValidator.InsecureCertificateValidator(),
            List.of(
                NodeIds.RsaSha256ApplicationCertificateType,
                NodeIds.EccNistP256ApplicationCertificateType));

    group.initialize();

    // Arm the failure only after initialization has populated the store.
    store.failGet = true;

    List<Entry> entries = group.getCertificateEntries();

    assertEquals(1, entries.size());
    assertEquals(NodeIds.RsaSha256ApplicationCertificateType, entries.get(0).certificateTypeId);
    assertTrue(group.getKeyPair(NodeIds.RsaSha256ApplicationCertificateType).isPresent());
    assertFalse(group.getKeyPair(NodeIds.EccNistP256ApplicationCertificateType).isPresent());
  }

  // A failure partway through initialize() must leave the group retryable rather than latched as
  // a silent permanent no-op; otherwise a transient store/factory error would require a restart.
  @Test
  void initializeIsRetryableAfterFailure() throws Exception {
    var store = new FailingSetCertificateStore(NodeIds.EccNistP256ApplicationCertificateType);
    DefaultApplicationGroup group =
        new DefaultApplicationGroup(
            new MemoryTrustListManager(),
            store,
            new CurrentEccCertificateFactory(),
            new CertificateValidator.InsecureCertificateValidator(),
            List.of(
                NodeIds.RsaSha256ApplicationCertificateType,
                NodeIds.EccNistP256ApplicationCertificateType));

    store.failSet = true;
    assertThrows(RuntimeException.class, group::initialize);

    // Clear the fault and retry; the second call must do the work rather than no-op.
    store.failSet = false;
    group.initialize();

    assertEquals(2, group.getCertificateEntries().size());
    assertTrue(group.getKeyPair(NodeIds.RsaSha256ApplicationCertificateType).isPresent());
    assertTrue(group.getKeyPair(NodeIds.EccNistP256ApplicationCertificateType).isPresent());
  }

  /** A {@link CertificateStore} whose {@link #get} throws for one configured type when armed. */
  private static final class FailingGetCertificateStore implements CertificateStore {

    private final MemoryCertificateStore delegate = new MemoryCertificateStore();
    private final NodeId failingTypeId;
    private volatile boolean failGet = false;

    private FailingGetCertificateStore(NodeId failingTypeId) {
      this.failingTypeId = failingTypeId;
    }

    @Override
    public boolean contains(NodeId certificateTypeId) throws Exception {
      return delegate.contains(certificateTypeId);
    }

    @Override
    public CertificateStore.@Nullable Entry get(NodeId certificateTypeId) throws Exception {
      if (failGet && certificateTypeId.equals(failingTypeId)) {
        throw new RuntimeException("simulated read failure for " + certificateTypeId);
      }
      return delegate.get(certificateTypeId);
    }

    @Override
    public CertificateStore.@Nullable Entry remove(NodeId certificateTypeId) throws Exception {
      return delegate.remove(certificateTypeId);
    }

    @Override
    public void set(NodeId certificateTypeId, CertificateStore.Entry entry) throws Exception {
      delegate.set(certificateTypeId, entry);
    }
  }

  /** A {@link CertificateStore} whose {@link #set} throws for one configured type when armed. */
  private static final class FailingSetCertificateStore implements CertificateStore {

    private final MemoryCertificateStore delegate = new MemoryCertificateStore();
    private final NodeId failingTypeId;
    private volatile boolean failSet = false;

    private FailingSetCertificateStore(NodeId failingTypeId) {
      this.failingTypeId = failingTypeId;
    }

    @Override
    public boolean contains(NodeId certificateTypeId) throws Exception {
      return delegate.contains(certificateTypeId);
    }

    @Override
    public CertificateStore.@Nullable Entry get(NodeId certificateTypeId) throws Exception {
      return delegate.get(certificateTypeId);
    }

    @Override
    public CertificateStore.@Nullable Entry remove(NodeId certificateTypeId) throws Exception {
      return delegate.remove(certificateTypeId);
    }

    @Override
    public void set(NodeId certificateTypeId, CertificateStore.Entry entry) throws Exception {
      if (failSet && certificateTypeId.equals(failingTypeId)) {
        throw new RuntimeException("simulated write failure for " + certificateTypeId);
      }
      delegate.set(certificateTypeId, entry);
    }
  }

  private static final class CurrentEccCertificateFactory extends TestCertificateFactory {

    @Override
    public KeyPair createKeyPair(NodeId nodeId) {
      try {
        if (nodeId.equals(NodeIds.EccNistP256ApplicationCertificateType)) {
          return createEccNistP256KeyPair();
        } else if (nodeId.equals(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)) {
          return createEccBrainpoolP384r1KeyPair();
        } else if (nodeId.equals(NodeIds.EccCurve25519ApplicationCertificateType)) {
          return createEccCurve25519KeyPair();
        } else if (nodeId.equals(NodeIds.EccCurve448ApplicationCertificateType)) {
          return createEccCurve448KeyPair();
        } else {
          return super.createKeyPair(nodeId);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public X509Certificate[] createCertificateChain(NodeId nodeId, KeyPair keyPair) {
      if (nodeId.equals(NodeIds.EccNistP256ApplicationCertificateType)
          || nodeId.equals(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)
          || nodeId.equals(NodeIds.EccCurve25519ApplicationCertificateType)
          || nodeId.equals(NodeIds.EccCurve448ApplicationCertificateType)) {

        return createEccApplicationCertificateChain(keyPair);
      } else {
        return super.createCertificateChain(nodeId, keyPair);
      }
    }

    private X509Certificate[] createEccApplicationCertificateChain(KeyPair keyPair) {
      try {
        X509Certificate certificate =
            SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
                .setApplicationUri("urn:eclipse:milo:test")
                .addDnsName("localhost")
                .build();

        return new X509Certificate[] {certificate};
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
