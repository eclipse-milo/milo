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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.util.CrlTestUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TrustListManagerAtomicityTest {

  private static final Generation GENERATION_A = createGeneration("generation-a", new DateTime(1));
  private static final Generation GENERATION_B = createGeneration("generation-b", new DateTime(2));

  @TempDir Path tempDir;

  // A validator must never combine certificates and CRLs from different replacement generations.
  @Test
  void memoryManagerPublishesOnlyCompleteSnapshotsDuringConcurrentReplacement() throws Exception {
    try (var manager = new MemoryTrustListManager()) {
      assertAtomicReplacement(manager, 10_001);
    }
  }

  // File writes must finish before one complete replacement becomes visible to in-memory readers.
  @Test
  void fileManagerPublishesOnlyCompleteSnapshotsDuringConcurrentReplacement() throws Exception {
    Path baseDir = Files.createTempDirectory(tempDir, "pki");
    Path issuerCertsDir = Files.createDirectories(baseDir.resolve("issuer/certs"));
    Path issuerCrlDir = Files.createDirectories(baseDir.resolve("issuer/crl"));
    Path trustedCertsDir = Files.createDirectories(baseDir.resolve("trusted/certs"));
    Path trustedCrlDir = Files.createDirectories(baseDir.resolve("trusted/crl"));

    try (var manager =
        new FileBasedTrustListManager(
            issuerCertsDir, issuerCrlDir, trustedCertsDir, trustedCrlDir)) {

      assertAtomicReplacement(manager, 25);
    }
  }

  // Writer-generated events must reload all four directories before the watcher publishes state.
  @Test
  void fileWatcherPublishesACompleteReload() throws Exception {
    try (var manager =
        FileBasedTrustListManager.createAndInitialize(Files.createTempDirectory(tempDir, "pki"))) {

      manager.replaceAll(GENERATION_A.snapshot());

      TrustListSnapshot reloaded = awaitWatcherReload(manager, GENERATION_A.lastUpdateTime());
      assertTrue(GENERATION_A.matchesLists(reloaded));
    }
  }

  // A snapshot stays coherent even if a caller reuses and mutates its constructor inputs.
  @Test
  void snapshotDefensivelyCopiesItsLists() {
    var certificates = new ArrayList<>(GENERATION_A.certificates());
    var crls = new ArrayList<>(GENERATION_A.crls());
    var snapshot =
        new TrustListSnapshot(certificates, crls, certificates, crls, DateTime.MIN_VALUE);

    certificates.clear();
    crls.clear();

    assertEquals(GENERATION_A.certificates(), snapshot.issuerCertificates());
    assertEquals(GENERATION_A.crls(), snapshot.issuerCrls());
    assertEquals(GENERATION_A.certificates(), snapshot.trustedCertificates());
    assertEquals(GENERATION_A.crls(), snapshot.trustedCrls());
    assertThrows(
        UnsupportedOperationException.class,
        () -> snapshot.trustedCertificates().add(GENERATION_B.certificate()));
  }

  @Test
  void memoryManagerRejectsNullReplacementWithoutChangingState() {
    var manager = new MemoryTrustListManager();
    manager.replaceAll(GENERATION_A.snapshot());

    assertThrows(NullPointerException.class, () -> manager.replaceAll((TrustListSnapshot) null));

    assertEquals(GENERATION_A.snapshot(), manager.getSnapshot());
  }

  // Existing third-party implementations inherit functional defaults without implementing new
  // methods, even though only implementations that override them can promise atomicity.
  @Test
  void legacyManagerUsesSnapshotDefaults() {
    var manager = new LegacyTrustListManager();
    TrustListSnapshot replacement = GENERATION_A.snapshot();

    manager.replaceAll(replacement);
    TrustListSnapshot captured = manager.getSnapshot();

    assertTrue(GENERATION_A.matchesLists(captured));
    assertEquals(DateTime.MIN_VALUE, captured.lastUpdateTime());
  }

  private static void assertAtomicReplacement(TrustListManager manager, int replacements)
      throws Exception {

    manager.replaceAll(GENERATION_A.snapshot());

    var readerReady = new CountDownLatch(1);
    var writerStarted = new CountDownLatch(1);
    var readerLooping = new CountDownLatch(1);
    var writerDone = new AtomicBoolean();
    var sawGenerationA = new AtomicBoolean();
    var sawGenerationB = new AtomicBoolean();
    ExecutorService executor = Executors.newFixedThreadPool(2);

    try {
      Future<?> writer =
          executor.submit(
              () -> {
                await(readerReady);
                writerStarted.countDown();
                await(readerLooping);

                try {
                  for (int i = 0; i < replacements; i++) {
                    manager.replaceAll(
                        (i & 1) == 0 ? GENERATION_B.snapshot() : GENERATION_A.snapshot());
                  }
                } finally {
                  writerDone.set(true);
                }
              });
      Future<?> reader =
          executor.submit(
              () -> {
                observe(manager.getSnapshot(), sawGenerationA, sawGenerationB);
                readerReady.countDown();
                await(writerStarted);
                readerLooping.countDown();

                do {
                  observe(manager.getSnapshot(), sawGenerationA, sawGenerationB);
                } while (!writerDone.get());

                observe(manager.getSnapshot(), sawGenerationA, sawGenerationB);
              });

      writer.get(30, TimeUnit.SECONDS);
      reader.get(30, TimeUnit.SECONDS);
      assertTrue(sawGenerationA.get(), "reader did not observe generation A");
      assertTrue(sawGenerationB.get(), "reader did not observe generation B");
    } finally {
      executor.shutdownNow();
      assertTrue(executor.awaitTermination(30, TimeUnit.SECONDS));
    }
  }

  private static void observe(
      TrustListSnapshot snapshot, AtomicBoolean sawGenerationA, AtomicBoolean sawGenerationB) {

    boolean generationA = GENERATION_A.matches(snapshot);
    boolean generationB = GENERATION_B.matches(snapshot);

    if (generationA) {
      sawGenerationA.set(true);
    }
    if (generationB) {
      sawGenerationB.set(true);
    }

    assertTrue(
        generationA || generationB, "observed a snapshot containing mixed trust-list generations");
  }

  private static TrustListSnapshot awaitWatcherReload(
      TrustListManager manager, DateTime replacementTime) {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);

    while (System.nanoTime() < deadline) {
      TrustListSnapshot snapshot = manager.getSnapshot();
      if (!replacementTime.equals(snapshot.lastUpdateTime())) {
        return snapshot;
      }
      Thread.onSpinWait();
    }

    throw new AssertionError("watcher did not reload the trust lists within 10 seconds");
  }

  private static void await(CountDownLatch latch) {
    try {
      assertTrue(latch.await(30, TimeUnit.SECONDS));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  private static Generation createGeneration(String commonName, DateTime lastUpdateTime) {
    try {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      X509Certificate certificate =
          new SelfSignedCertificateBuilder(keyPair)
              .setCommonName(commonName)
              .setApplicationUri("urn:eclipse:milo:test:" + commonName)
              .build();
      X509CRL crl = CrlTestUtil.generateCrl(certificate, keyPair.getPrivate());

      return new Generation(certificate, crl, lastUpdateTime);
    } catch (Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private record Generation(X509Certificate certificate, X509CRL crl, DateTime lastUpdateTime) {

    List<X509Certificate> certificates() {
      return List.of(certificate);
    }

    List<X509CRL> crls() {
      return List.of(crl);
    }

    TrustListSnapshot snapshot() {
      return new TrustListSnapshot(certificates(), crls(), certificates(), crls(), lastUpdateTime);
    }

    boolean matches(TrustListSnapshot snapshot) {
      return matchesLists(snapshot) && lastUpdateTime.equals(snapshot.lastUpdateTime());
    }

    boolean matchesLists(TrustListSnapshot snapshot) {
      return certificates().equals(snapshot.issuerCertificates())
          && crls().equals(snapshot.issuerCrls())
          && certificates().equals(snapshot.trustedCertificates())
          && crls().equals(snapshot.trustedCrls());
    }
  }

  /**
   * This fixture intentionally implements only the interface that existed before snapshot support.
   */
  private static final class LegacyTrustListManager implements TrustListManager {

    private List<X509Certificate> issuerCertificates = List.of();
    private List<X509CRL> issuerCrls = List.of();
    private List<X509Certificate> trustedCertificates = List.of();
    private List<X509CRL> trustedCrls = List.of();

    @Override
    public List<X509CRL> getIssuerCrls() {
      return issuerCrls;
    }

    @Override
    public List<X509CRL> getTrustedCrls() {
      return trustedCrls;
    }

    @Override
    public List<X509Certificate> getIssuerCertificates() {
      return issuerCertificates;
    }

    @Override
    public List<X509Certificate> getTrustedCertificates() {
      return trustedCertificates;
    }

    @Override
    public void setIssuerCrls(List<X509CRL> issuerCrls) {
      this.issuerCrls = List.copyOf(issuerCrls);
    }

    @Override
    public void setTrustedCrls(List<X509CRL> trustedCrls) {
      this.trustedCrls = List.copyOf(trustedCrls);
    }

    @Override
    public void setIssuerCertificates(List<X509Certificate> issuerCertificates) {
      this.issuerCertificates = List.copyOf(issuerCertificates);
    }

    @Override
    public void setTrustedCertificates(List<X509Certificate> trustedCertificates) {
      this.trustedCertificates = List.copyOf(trustedCertificates);
    }

    @Override
    public void addIssuerCertificate(X509Certificate certificate) {
      issuerCertificates = append(issuerCertificates, certificate);
    }

    @Override
    public void addTrustedCertificate(X509Certificate certificate) {
      trustedCertificates = append(trustedCertificates, certificate);
    }

    @Override
    public boolean removeIssuerCertificate(ByteString thumbprint) {
      return false;
    }

    @Override
    public boolean removeTrustedCertificate(ByteString thumbprint) {
      return false;
    }

    @Override
    public DateTime getLastUpdateTime() {
      return DateTime.MIN_VALUE;
    }

    private static List<X509Certificate> append(
        List<X509Certificate> certificates, X509Certificate certificate) {

      var updated = new ArrayList<>(certificates);
      updated.add(certificate);
      return List.copyOf(updated);
    }
  }
}
