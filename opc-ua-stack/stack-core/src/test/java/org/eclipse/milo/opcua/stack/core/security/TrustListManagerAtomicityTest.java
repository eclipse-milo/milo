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
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.util.CrlTestUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TrustListManagerAtomicityTest {

  private static final Generation GENERATION_A = createGeneration("generation-a");
  private static final Generation GENERATION_B = createGeneration("generation-b");

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

  // A certificate dropped into a directory by an operator must appear in that list without
  // disturbing the other three, and the reload must arrive as one coherent snapshot.
  @Test
  void fileWatcherReloadsOnlyTheChangedDirectory() throws Exception {
    Path baseDir = Files.createTempDirectory(tempDir, "pki");

    try (var manager = FileBasedTrustListManager.createAndInitialize(baseDir)) {
      manager.replaceAll(GENERATION_A.snapshot());

      Files.write(
          baseDir.resolve("trusted/certs/generation-b.der"),
          GENERATION_B.certificate().getEncoded());

      TrustListSnapshot reloaded =
          awaitSnapshot(manager, s -> s.trustedCertificates().contains(GENERATION_B.certificate()));

      assertEquals(
          Set.of(GENERATION_A.certificate(), GENERATION_B.certificate()),
          Set.copyOf(reloaded.trustedCertificates()));
      assertEquals(GENERATION_A.certificates(), reloaded.issuerCertificates());
      assertEquals(GENERATION_A.crls(), reloaded.issuerCrls());
      assertEquals(GENERATION_A.crls(), reloaded.trustedCrls());
    }
  }

  // Two writers changing different lists through update() must both see their change land; a
  // read-modify-write outside the manager would let one overwrite the other.
  @Test
  void memoryManagerUpdatePreservesConcurrentChangesToOtherLists() throws Exception {
    try (var manager = new MemoryTrustListManager()) {
      assertConcurrentUpdatesAreNotLost(manager);
    }
  }

  @Test
  void fileManagerUpdatePreservesConcurrentChangesToOtherLists() throws Exception {
    try (var manager =
        FileBasedTrustListManager.createAndInitialize(Files.createTempDirectory(tempDir, "pki"))) {

      assertConcurrentUpdatesAreNotLost(manager);
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

  // Trust lists have set semantics, so every manager must publish the same normalized contents
  // when callers supply duplicate entries.
  @Test
  void snapshotRemovesDuplicateEntries() {
    var snapshot =
        new TrustListSnapshot(
            List.of(GENERATION_A.certificate(), GENERATION_A.certificate()),
            List.of(GENERATION_A.crl(), GENERATION_A.crl()),
            List.of(GENERATION_A.certificate(), GENERATION_A.certificate()),
            List.of(GENERATION_A.crl(), GENERATION_A.crl()),
            DateTime.MIN_VALUE);

    GENERATION_A.assertLists(snapshot);
  }

  @Test
  void memoryManagerRejectsNullReplacementWithoutChangingState() {
    var manager = new MemoryTrustListManager();
    manager.replaceAll(GENERATION_A.snapshot());
    TrustListSnapshot before = manager.getSnapshot();

    assertThrows(NullPointerException.class, () -> manager.replaceAll(null));
    assertThrows(NullPointerException.class, () -> manager.update(current -> null));

    assertEquals(before, manager.getSnapshot());
  }

  // The committed snapshot carries the commit time, not whatever time the caller supplied.
  @Test
  void managersStampLastUpdateTimeOnCommit() throws Exception {
    try (var manager = new MemoryTrustListManager()) {
      DateTime before = DateTime.now();

      manager.replaceAll(GENERATION_A.snapshot());

      assertTrue(manager.getLastUpdateTime().getUtcTime() >= before.getUtcTime());
    }
  }

  // Existing third-party implementations inherit functional defaults without implementing new
  // methods, even though only implementations that override them can promise atomicity.
  @Test
  void legacyManagerUsesSnapshotDefaults() {
    var manager = new LegacyTrustListManager();
    TrustListSnapshot replacement = GENERATION_A.snapshot();

    manager.replaceAll(replacement);
    TrustListSnapshot captured = manager.getSnapshot();

    GENERATION_A.assertLists(captured);
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

  private static void assertConcurrentUpdatesAreNotLost(TrustListManager manager) throws Exception {

    manager.replaceAll(GENERATION_A.snapshot());

    var bothInside = new CountDownLatch(2);
    ExecutorService executor = Executors.newFixedThreadPool(2);

    try {
      // Each operator parks until both threads have read a snapshot, so at least one of them
      // must observe the other's commit (or be retried) for its own change to survive.
      Future<?> issuerWriter =
          executor.submit(
              () ->
                  manager.update(
                      current -> {
                        bothInside.countDown();
                        awaitOrTimeout(bothInside);
                        return current.withIssuerCertificates(GENERATION_B.certificates());
                      }));
      Future<?> trustedWriter =
          executor.submit(
              () ->
                  manager.update(
                      current -> {
                        bothInside.countDown();
                        awaitOrTimeout(bothInside);
                        return current.withTrustedCrls(GENERATION_B.crls());
                      }));

      issuerWriter.get(30, TimeUnit.SECONDS);
      trustedWriter.get(30, TimeUnit.SECONDS);
    } finally {
      executor.shutdownNow();
      assertTrue(executor.awaitTermination(30, TimeUnit.SECONDS));
    }

    TrustListSnapshot result = manager.getSnapshot();
    assertEquals(GENERATION_B.certificates(), result.issuerCertificates());
    assertEquals(GENERATION_B.crls(), result.trustedCrls());
    assertEquals(GENERATION_A.crls(), result.issuerCrls());
    assertEquals(GENERATION_A.certificates(), result.trustedCertificates());
  }

  /**
   * Like {@link #await(CountDownLatch)} but bounded to a short wait, because a lock-based manager
   * runs the two operators one after the other and the second never sees the first inside.
   */
  private static void awaitOrTimeout(CountDownLatch latch) {
    try {
      latch.await(1, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
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

  /**
   * Poll until the manager publishes a snapshot matching {@code condition}. The bound is generous
   * because the JDK's polling {@link java.nio.file.WatchService} on macOS scans every 10 seconds.
   */
  private static TrustListSnapshot awaitSnapshot(
      TrustListManager manager, Predicate<TrustListSnapshot> condition) {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);

    while (System.nanoTime() < deadline) {
      TrustListSnapshot snapshot = manager.getSnapshot();
      if (condition.test(snapshot)) {
        return snapshot;
      }
      Thread.onSpinWait();
    }

    throw new AssertionError("watcher did not reload the trust lists within 30 seconds");
  }

  private static void await(CountDownLatch latch) {
    try {
      assertTrue(latch.await(30, TimeUnit.SECONDS));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  private static Generation createGeneration(String commonName) {
    try {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      X509Certificate certificate =
          new SelfSignedCertificateBuilder(keyPair)
              .setCommonName(commonName)
              .setApplicationUri("urn:eclipse:milo:test:" + commonName)
              .build();
      X509CRL crl = CrlTestUtil.generateCrl(certificate, keyPair.getPrivate());

      return new Generation(certificate, crl);
    } catch (Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private record Generation(X509Certificate certificate, X509CRL crl) {

    List<X509Certificate> certificates() {
      return List.of(certificate);
    }

    List<X509CRL> crls() {
      return List.of(crl);
    }

    TrustListSnapshot snapshot() {
      return new TrustListSnapshot(
          certificates(), crls(), certificates(), crls(), DateTime.MIN_VALUE);
    }

    boolean matches(TrustListSnapshot snapshot) {
      return certificates().equals(snapshot.issuerCertificates())
          && crls().equals(snapshot.issuerCrls())
          && certificates().equals(snapshot.trustedCertificates())
          && crls().equals(snapshot.trustedCrls());
    }

    void assertLists(TrustListSnapshot snapshot) {
      assertEquals(certificates(), snapshot.issuerCertificates());
      assertEquals(crls(), snapshot.issuerCrls());
      assertEquals(certificates(), snapshot.trustedCertificates());
      assertEquals(crls(), snapshot.trustedCrls());
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
