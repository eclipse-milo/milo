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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup.Entry;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@NullMarked
class DefaultCertificateGroupTest {

  private final TestCertificateFactory certificateFactory = new TestCertificateFactory();

  @Test
  void defaultConstructorSupportsRsaSha256Only() {
    DefaultCertificateGroup group = newGroup(new MemoryCertificateStore());

    assertEquals(
        List.of(NodeIds.RsaSha256ApplicationCertificateType),
        group.getSupportedCertificateTypeIds());
  }

  @Test
  void hasCertificateIsFalseForAbsentTypeAndTrueAfterUpdate() throws Exception {
    DefaultCertificateGroup group = newGroup(new MemoryCertificateStore());
    KeyPair keyPair = certificateFactory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain =
        certificateFactory.createRsaSha256CertificateChain(keyPair);

    assertFalse(group.hasCertificate(NodeIds.RsaSha256ApplicationCertificateType));

    group.updateCertificate(NodeIds.RsaSha256ApplicationCertificateType, keyPair, certificateChain);

    assertTrue(group.hasCertificate(NodeIds.RsaSha256ApplicationCertificateType));
  }

  // A store entry under a type the group does not support is not the group's material; reporting
  // it present would contradict getKeyPair and getCertificateChain, which refuse to serve it.
  @Test
  void hasCertificateIsFalseForTypeTheGroupDoesNotSupport() throws Exception {
    var certificateStore = new MemoryCertificateStore();
    KeyPair keyPair = certificateFactory.createRsaSha256KeyPair();
    certificateStore.set(
        NodeIds.EccNistP256ApplicationCertificateType,
        new CertificateStore.Entry(
            keyPair.getPrivate(), certificateFactory.createRsaSha256CertificateChain(keyPair)));
    DefaultCertificateGroup group = newGroup(certificateStore);

    assertFalse(group.hasCertificate(NodeIds.EccNistP256ApplicationCertificateType));
  }

  // hasCertificate must distinguish "absent" from "unreadable": answering false for a store failure
  // would let provisioning overwrite an entry that exists but could not be read.
  @Test
  void hasCertificatePropagatesStoreFailureInsteadOfReportingAbsent() {
    var store = new FaultInjectingCertificateStore(NodeIds.RsaSha256ApplicationCertificateType);
    DefaultCertificateGroup group = newGroup(store);
    store.failContains = true;

    assertThrows(
        RuntimeException.class,
        () -> group.hasCertificate(NodeIds.RsaSha256ApplicationCertificateType));
  }

  @Test
  void createMissingCertificatesProvisionsEveryConfiguredTypeInSupportedOrder() throws Exception {
    List<NodeId> certificateTypeIds =
        List.of(
            NodeIds.RsaSha256ApplicationCertificateType,
            NodeIds.EccNistP256ApplicationCertificateType,
            NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
            NodeIds.EccCurve25519ApplicationCertificateType,
            NodeIds.EccCurve448ApplicationCertificateType);
    DefaultCertificateGroup group = newGroup(new MemoryCertificateStore(), certificateTypeIds);

    List<NodeId> created = new CurrentEccCertificateFactory().createMissingCertificates(group);

    assertEquals(certificateTypeIds, created, "created types are reported in supported-type order");
    assertEquals(certificateTypeIds, group.getSupportedCertificateTypeIds());
    assertEquals(5, group.getCertificateEntries().size());
    for (NodeId certificateTypeId : certificateTypeIds) {
      assertTrue(group.getKeyPair(certificateTypeId).isPresent(), "key pair " + certificateTypeId);
      assertTrue(
          group.getCertificateChain(certificateTypeId).isPresent(), "chain " + certificateTypeId);
    }
  }

  // Externally issued material must be usable as soon as the group is constructed, without any
  // provisioning step generating a replacement certificate first.
  @Test
  void groupExposesExternallyIssuedCertificateWithoutProvisioning() throws Exception {
    var certificateStore = new MemoryCertificateStore();
    KeyPair keyPair = certificateFactory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain =
        certificateFactory.createRsaSha256CertificateChain(keyPair);
    certificateStore.set(
        NodeIds.RsaSha256ApplicationCertificateType,
        new CertificateStore.Entry(keyPair.getPrivate(), certificateChain));

    DefaultCertificateGroup group =
        newGroup(certificateStore, List.of(NodeIds.RsaSha256ApplicationCertificateType));

    List<Entry> entries = group.getCertificateEntries();
    assertEquals(1, entries.size());
    assertEquals(NodeIds.RsaSha256ApplicationCertificateType, entries.get(0).certificateTypeId());
    assertSame(certificateChain, entries.get(0).certificateChain());
  }

  @Test
  void updateCertificateRejectsUnsupportedType() {
    DefaultCertificateGroup group = newGroup(new MemoryCertificateStore());
    KeyPair keyPair = certificateFactory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain =
        certificateFactory.createRsaSha256CertificateChain(keyPair);

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
    var store = new FaultInjectingCertificateStore(NodeIds.EccNistP256ApplicationCertificateType);
    DefaultCertificateGroup group =
        newGroup(
            store,
            List.of(
                NodeIds.RsaSha256ApplicationCertificateType,
                NodeIds.EccNistP256ApplicationCertificateType));

    new CurrentEccCertificateFactory().createMissingCertificates(group);

    // Arm the failure only after provisioning has populated the store.
    store.failGet = true;

    List<Entry> entries = group.getCertificateEntries();

    assertEquals(1, entries.size());
    assertEquals(NodeIds.RsaSha256ApplicationCertificateType, entries.get(0).certificateTypeId());
    assertTrue(group.getKeyPair(NodeIds.RsaSha256ApplicationCertificateType).isPresent());
    assertFalse(group.getKeyPair(NodeIds.EccNistP256ApplicationCertificateType).isPresent());
  }

  // A failure partway through createMissingCertificates() must leave the group retryable rather
  // than latched as a silent permanent no-op; otherwise a transient store/factory error would
  // require a restart. Material created before the failure must survive it.
  @Test
  void createMissingCertificatesIsRetryableAfterFailure() throws Exception {
    var store = new FaultInjectingCertificateStore(NodeIds.EccNistP256ApplicationCertificateType);
    var factory = new CurrentEccCertificateFactory();
    DefaultCertificateGroup group =
        newGroup(
            store,
            List.of(
                NodeIds.RsaSha256ApplicationCertificateType,
                NodeIds.EccNistP256ApplicationCertificateType));

    store.failSet = true;
    assertThrows(RuntimeException.class, () -> factory.createMissingCertificates(group));
    assertTrue(
        group.getKeyPair(NodeIds.RsaSha256ApplicationCertificateType).isPresent(),
        "material created before the failing type stays installed");

    // Clear the fault and retry; the second call must do the work rather than no-op.
    store.failSet = false;
    List<NodeId> created = factory.createMissingCertificates(group);

    assertEquals(
        List.of(NodeIds.EccNistP256ApplicationCertificateType),
        created,
        "only the type that failed the first time should be created on retry");
    assertEquals(2, group.getCertificateEntries().size());
    assertTrue(group.getKeyPair(NodeIds.RsaSha256ApplicationCertificateType).isPresent());
    assertTrue(group.getKeyPair(NodeIds.EccNistP256ApplicationCertificateType).isPresent());
  }

  // An entry that exists but cannot be read must never be treated as missing: replacing it would
  // silently discard a deployed identity. The failure propagates, nothing is written for that
  // type, and material created for earlier types stays installed.
  @Test
  void createMissingCertificatesPropagatesHasCertificateFailureWithoutReplacingEntry()
      throws Exception {
    var store = new FaultInjectingCertificateStore(NodeIds.EccNistP256ApplicationCertificateType);
    var factory = new CurrentEccCertificateFactory();
    KeyPair eccKeyPair = factory.createKeyPair(NodeIds.EccNistP256ApplicationCertificateType);
    X509Certificate[] eccChain =
        factory.createCertificateChain(NodeIds.EccNistP256ApplicationCertificateType, eccKeyPair);
    // Pre-populate through the delegate so only the factory's writes are recorded.
    store.delegate.set(
        NodeIds.EccNistP256ApplicationCertificateType,
        new CertificateStore.Entry(eccKeyPair.getPrivate(), eccChain));
    DefaultCertificateGroup group =
        newGroup(
            store,
            List.of(
                NodeIds.RsaSha256ApplicationCertificateType,
                NodeIds.EccNistP256ApplicationCertificateType));
    store.failContains = true;

    assertThrows(RuntimeException.class, () -> factory.createMissingCertificates(group));

    assertEquals(
        List.of(NodeIds.RsaSha256ApplicationCertificateType),
        store.setCalls,
        "the absent RSA type is created; the unreadable ECC type is never written");
    assertSame(
        eccChain,
        store.delegate.get(NodeIds.EccNistP256ApplicationCertificateType).certificateChain,
        "unreadable entry is left untouched");
  }

  // Externally provisioned material must survive createMissingCertificates(): the factory only
  // fills gaps, so an application that mixes a GDS-issued RSA identity with a Milo-generated ECC
  // identity keeps the GDS-issued one, and the return value tells the caller what was generated.
  @Test
  void createMissingCertificatesReportsOnlyTypesItCreatedAndKeepsExistingEntries()
      throws Exception {
    var certificateStore = new MemoryCertificateStore();
    var factory = new CurrentEccCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain = factory.createRsaSha256CertificateChain(keyPair);
    certificateStore.set(
        NodeIds.RsaSha256ApplicationCertificateType,
        new CertificateStore.Entry(keyPair.getPrivate(), certificateChain));

    DefaultCertificateGroup group =
        newGroup(
            certificateStore,
            List.of(
                NodeIds.RsaSha256ApplicationCertificateType,
                NodeIds.EccNistP256ApplicationCertificateType));

    List<NodeId> created = factory.createMissingCertificates(group);

    assertEquals(List.of(NodeIds.EccNistP256ApplicationCertificateType), created);
    assertSame(
        certificateChain,
        group.getCertificateChain(NodeIds.RsaSha256ApplicationCertificateType).orElseThrow(),
        "pre-existing entry must not be replaced");
    assertTrue(group.getKeyPair(NodeIds.EccNistP256ApplicationCertificateType).isPresent());

    assertEquals(
        List.of(),
        factory.createMissingCertificates(group),
        "nothing is missing on the second call, so nothing should be created");
  }

  // A group is a view over its store, not a lifecycle: if an entry disappears after the first
  // call (a KeyStore reload, an application removing it), a later call must re-create it rather
  // than treat the group as already provisioned.
  @Test
  void createMissingCertificatesRecreatesEntryRemovedFromStore() throws Exception {
    var certificateStore = new MemoryCertificateStore();
    var factory = new CurrentEccCertificateFactory();
    DefaultCertificateGroup group =
        newGroup(
            certificateStore,
            List.of(
                NodeIds.RsaSha256ApplicationCertificateType,
                NodeIds.EccNistP256ApplicationCertificateType));

    factory.createMissingCertificates(group);
    certificateStore.remove(NodeIds.EccNistP256ApplicationCertificateType);
    assertFalse(group.getKeyPair(NodeIds.EccNistP256ApplicationCertificateType).isPresent());

    List<NodeId> created = factory.createMissingCertificates(group);

    assertEquals(List.of(NodeIds.EccNistP256ApplicationCertificateType), created);
    assertTrue(group.getKeyPair(NodeIds.EccNistP256ApplicationCertificateType).isPresent());
  }

  // Provisioning has to be atomic per group: two startup or retry threads racing on an empty group
  // would otherwise both find the type absent and install competing key material, leaving an
  // endpoint advertising a certificate whose private key is no longer in the store.
  @Test
  void createMissingCertificatesSerializesConcurrentCallsOnTheSameGroup() throws Exception {
    var factory = new TestCertificateFactory();
    DefaultCertificateGroup group = newGroup(new MemoryCertificateStore());
    int threadCount = 4;
    var barrier = new CyclicBarrier(threadCount);
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    var created = new ArrayList<NodeId>();
    try {
      var futures = new ArrayList<Future<List<NodeId>>>();
      for (int i = 0; i < threadCount; i++) {
        futures.add(
            executor.submit(
                () -> {
                  barrier.await(30, TimeUnit.SECONDS);
                  return factory.createMissingCertificates(group);
                }));
      }
      for (Future<List<NodeId>> future : futures) {
        created.addAll(future.get(60, TimeUnit.SECONDS));
      }
    } finally {
      executor.shutdownNow();
    }

    assertEquals(
        List.of(NodeIds.RsaSha256ApplicationCertificateType),
        created,
        "exactly one caller may create material for the type");

    KeyPair keyPair = group.getKeyPair(NodeIds.RsaSha256ApplicationCertificateType).orElseThrow();
    X509Certificate[] certificateChain =
        group.getCertificateChain(NodeIds.RsaSha256ApplicationCertificateType).orElseThrow();
    assertEquals(
        keyPair.getPublic(),
        certificateChain[0].getPublicKey(),
        "the stored key pair and certificate must belong to the same identity");
  }

  // Clients configure one externally issued identity and never name its certificate type, so the
  // group must derive the type from the leaf and expose exactly the material it was given.
  @ParameterizedTest(name = "{0}")
  @MethodSource("inferableIdentities")
  void forIdentityInfersCertificateTypeAndExposesGivenMaterial(
      String name, KeyPair keyPair, X509Certificate[] certificateChain, NodeId expectedTypeId) {

    DefaultCertificateGroup group =
        DefaultCertificateGroup.forIdentity(
            keyPair,
            certificateChain,
            new MemoryTrustListManager(),
            new MemoryCertificateQuarantine(),
            new CertificateValidator.InsecureCertificateValidator());

    assertEquals(List.of(expectedTypeId), group.getSupportedCertificateTypeIds());

    List<CertificateIdentity> identities = group.getCertificateIdentities();
    assertEquals(1, identities.size());
    CertificateIdentity identity = identities.get(0);
    assertEquals(expectedTypeId, identity.certificateTypeId());
    assertEquals(keyPair.getPublic(), identity.keyPair().getPublic());
    assertEquals(keyPair.getPrivate(), identity.keyPair().getPrivate());
    assertArrayEquals(certificateChain, identity.certificateChain());
  }

  static Stream<Arguments> inferableIdentities() throws Exception {
    var factory = new TestCertificateFactory();
    KeyPair rsaKeyPair = factory.createRsaSha256KeyPair();
    KeyPair eccKeyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();

    return Stream.of(
        Arguments.of(
            "RSA SHA-256",
            rsaKeyPair,
            factory.createRsaSha256CertificateChain(rsaKeyPair),
            NodeIds.RsaSha256ApplicationCertificateType),
        Arguments.of(
            "NIST P-256",
            eccKeyPair,
            new X509Certificate[] {
              SelfSignedCertificateBuilder.forEccApplicationCertificate(eccKeyPair)
                  .setApplicationUri("urn:eclipse:milo:test")
                  .addDnsName("localhost")
                  .build()
            },
            NodeIds.EccNistP256ApplicationCertificateType));
  }

  // A mismatched private key would produce OPN and session signatures the peer cannot verify;
  // rejecting it at construction turns a confusing handshake failure into a configuration error.
  @Test
  void forIdentityRejectsKeyPairThatDoesNotMatchLeafCertificate() {
    KeyPair certificateKeyPair = certificateFactory.createRsaSha256KeyPair();
    KeyPair otherKeyPair = certificateFactory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain =
        certificateFactory.createRsaSha256CertificateChain(certificateKeyPair);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            DefaultCertificateGroup.forIdentity(
                otherKeyPair,
                certificateChain,
                new MemoryTrustListManager(),
                new MemoryCertificateQuarantine(),
                new CertificateValidator.InsecureCertificateValidator()));
  }

  // Rejection is a per-group trust decision: a certificate one group's validator rejects belongs
  // in that group's rejected list (Part 12 GetRejectedList) and must not leak into another group's.
  @Test
  void rejectedCertificateLandsOnlyInValidatingGroupQuarantine() {
    DefaultCertificateGroup groupA = newGroupWithClientValidator();
    DefaultCertificateGroup groupB = newGroupWithClientValidator();
    X509Certificate untrusted =
        certificateFactory
            .createRsaSha256CertificateChain(certificateFactory.createRsaSha256KeyPair())[0];

    assertThrows(
        UaException.class,
        () ->
            groupA
                .getCertificateValidator()
                .validateCertificateChain(List.of(untrusted), null, null));

    assertEquals(List.of(untrusted), groupA.getCertificateQuarantine().getRejectedCertificates());
    assertEquals(
        List.of(),
        groupB.getCertificateQuarantine().getRejectedCertificates(),
        "the other group's quarantine is untouched");
  }

  private static DefaultCertificateGroup newGroup(CertificateStore certificateStore) {
    return new DefaultCertificateGroup(
        new MemoryTrustListManager(),
        certificateStore,
        new MemoryCertificateQuarantine(),
        new CertificateValidator.InsecureCertificateValidator());
  }

  private static DefaultCertificateGroup newGroup(
      CertificateStore certificateStore, List<NodeId> certificateTypeIds) {

    return new DefaultCertificateGroup(
        new MemoryTrustListManager(),
        certificateStore,
        new MemoryCertificateQuarantine(),
        new CertificateValidator.InsecureCertificateValidator(),
        certificateTypeIds);
  }

  /** A group whose validator and quarantine are wired together on an empty trust list. */
  private static DefaultCertificateGroup newGroupWithClientValidator() {
    var trustListManager = new MemoryTrustListManager();
    var certificateQuarantine = new MemoryCertificateQuarantine();

    return new DefaultCertificateGroup(
        trustListManager,
        new MemoryCertificateStore(),
        certificateQuarantine,
        new DefaultClientCertificateValidator(trustListManager, certificateQuarantine));
  }

  /**
   * A {@link CertificateStore} that can be armed to throw from {@link #contains}, {@link #get}, or
   * {@link #set} for one configured type, and that records every {@link #set} call.
   */
  private static final class FaultInjectingCertificateStore implements CertificateStore {

    private final MemoryCertificateStore delegate = new MemoryCertificateStore();
    private final List<NodeId> setCalls = new ArrayList<>();
    private final NodeId failingTypeId;
    private volatile boolean failContains = false;
    private volatile boolean failGet = false;
    private volatile boolean failSet = false;

    private FaultInjectingCertificateStore(NodeId failingTypeId) {
      this.failingTypeId = failingTypeId;
    }

    @Override
    public boolean contains(NodeId certificateTypeId) throws Exception {
      if (failContains && certificateTypeId.equals(failingTypeId)) {
        throw new RuntimeException("simulated contains failure for " + certificateTypeId);
      }
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
      if (failSet && certificateTypeId.equals(failingTypeId)) {
        throw new RuntimeException("simulated write failure for " + certificateTypeId);
      }
      setCalls.add(certificateTypeId);
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
