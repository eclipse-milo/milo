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

import static org.eclipse.milo.opcua.stack.core.util.DigestUtil.sha1;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBufUtil;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.KeyPair;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.CrlTestUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FileBasedTrustListPersistenceFailureTest {

  private static final Material RETAINED = material("Retained");
  private static final Material SECOND = material("Second");
  private static final Material THIRD = material("Third");

  @TempDir Path baseDir;

  @Nested
  class CertificateWriteFailures {

    // A certificate whose file cannot be moved into place must never appear in the published
    // snapshot, must not be accepted by a validator, and must leave no temporary files behind.
    @ParameterizedTest
    @ValueSource(strings = {"trusted", "issuer"})
    void addCertificateWriteFailureThrowsAndMatchesDisk(String list) throws Exception {
      Path certsDir = Files.createDirectories(baseDir.resolve(list).resolve("certs"));
      Files.write(certsDir.resolve("retained.der"), RETAINED.certificate().getEncoded());
      blockTargetWithNonEmptyDirectory(
          certsDir.resolve(thumbprintHex(SECOND.certificate().getEncoded()) + ".der"));

      try (var manager = FileBasedTrustListManager.createAndInitialize(baseDir)) {
        var validator =
            new DefaultClientCertificateValidator(manager, new MemoryCertificateQuarantine());
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        assertThrows(
            UncheckedIOException.class,
            () -> {
              if (list.equals("trusted")) {
                manager.addTrustedCertificate(SECOND.certificate());
              } else {
                manager.addIssuerCertificate(SECOND.certificate());
              }
            });

        assertEquals(List.of(RETAINED.certificate()), certificates(manager, list));
        assertEquals(before, manager.getLastUpdateTime());
        assertNoTempFiles(certsDir);

        if (list.equals("trusted")) {
          assertDoesNotThrow(
              () ->
                  validator.validateCertificateChain(List.of(RETAINED.certificate()), null, null));
          assertThrows(
              UaException.class,
              () -> validator.validateCertificateChain(List.of(SECOND.certificate()), null, null));
        }
      }
    }

    // Encoding failures must fail before mutating the directory or publishing the snapshot across
    // both setTrustedCertificates and update.
    @Test
    void unencodableCertificateFailsSetAndUpdateWithoutChangingSnapshot() throws Exception {
      Path certsDir = Files.createDirectories(baseDir.resolve("trusted").resolve("certs"));
      Path retainedFile = certsDir.resolve("retained.der");
      Files.write(retainedFile, RETAINED.certificate().getEncoded());
      var unencodable = new UnencodableCertificate();

      try (var manager = FileBasedTrustListManager.createAndInitialize(baseDir)) {
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        UncheckedIOException setFailure =
            assertThrows(
                UncheckedIOException.class,
                () -> manager.setTrustedCertificates(List.of(unencodable)));
        assertInstanceOf(CertificateEncodingException.class, setFailure.getCause().getCause());
        assertEquals(List.of(RETAINED.certificate()), manager.getTrustedCertificates());
        assertEquals(before, manager.getLastUpdateTime());
        assertTrue(Files.exists(retainedFile), "existing file must not be deleted on write error");

        UncheckedIOException updateFailure =
            assertThrows(
                UncheckedIOException.class,
                () ->
                    manager.update(
                        current -> current.withTrustedCertificates(List.of(unencodable))));
        assertInstanceOf(CertificateEncodingException.class, updateFailure.getCause().getCause());
        assertEquals(List.of(RETAINED.certificate()), manager.getTrustedCertificates());
        assertEquals(before, manager.getLastUpdateTime());
        assertNoTempFiles(certsDir);
      }
    }

    // Newly created certificate and CRL files must inherit the process umask rather than forcing
    // 0600, while replacing an existing file must preserve its custom POSIX permissions.
    @Test
    void atomicWriteInheritsUmaskForNewFilesAndPreservesExistingPosixPermissions()
        throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path existing = dirs.trustedCerts().resolve("existing.der");
      Files.write(existing, RETAINED.certificate().getEncoded());

      boolean posix = existing.getFileSystem().supportedFileAttributeViews().contains("posix");
      Set<PosixFilePermission> expectedUmaskMode = Set.of();
      if (posix) {
        Files.setPosixFilePermissions(existing, PosixFilePermissions.fromString("rw-r-----"));
        Path probe = baseDir.resolve("umask-probe.tmp");
        try (OutputStream out = Files.newOutputStream(probe)) {
          out.write(0);
        }
        expectedUmaskMode = Files.getPosixFilePermissions(probe);
      }

      try (var manager = createManager(dirs)) {
        // Exercise the package-private atomic-replacement helper before initialize() starts the
        // directory watcher; in production writeFileAtomically runs under mutationLock, and calling
        // it unlocked while the watcher is active can race with NTFS file reads on Windows.
        manager.writeFileAtomically(existing, ".cert-", SECOND.certificate().getEncoded());
        manager.initialize();
        manager.addTrustedCertificate(THIRD.certificate());
        manager.setTrustedCrls(List.of(THIRD.crl()));

        Path newCert =
            dirs.trustedCerts().resolve(thumbprintHex(THIRD.certificate().getEncoded()) + ".der");
        Path newCrl = dirs.trustedCrl().resolve(thumbprintHex(THIRD.crl().getEncoded()) + ".crl");

        assertTrue(Files.exists(newCert));
        assertTrue(Files.exists(newCrl));
        assertNoTempFiles(dirs.trustedCerts());
        assertNoTempFiles(dirs.trustedCrl());
        if (posix) {
          assertEquals(
              PosixFilePermissions.fromString("rw-r-----"),
              Files.getPosixFilePermissions(existing),
              "atomic replacement must preserve existing permissions");
          assertEquals(
              expectedUmaskMode,
              Files.getPosixFilePermissions(newCert),
              "new certificate file must inherit process umask permissions");
          assertEquals(
              expectedUmaskMode,
              Files.getPosixFilePermissions(newCrl),
              "new CRL file must inherit process umask permissions");
        }
      }
    }
  }

  @Nested
  class CertificateDeleteFailures {

    // When removing or replacing a certificate fails to delete its file on disk, the in-memory
    // snapshot must reconcile back to disk immediately so the validator reflects reality.
    @ParameterizedTest
    @CsvSource({"trusted,remove", "trusted,replace", "issuer,remove", "issuer,replace"})
    void failedCertificateDeleteReconcilesSnapshotToDisk(String list, String operation)
        throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path certsDir = list.equals("trusted") ? dirs.trustedCerts() : dirs.issuerCerts();
      Path retainedFile = certsDir.resolve("retained.der");
      Files.write(retainedFile, RETAINED.certificate().getEncoded());

      try (var manager =
          new FaultInjectingTrustListManager(dirs, retainedFile::equals, p -> false, p -> false)) {
        manager.initialize();
        var validator =
            new DefaultClientCertificateValidator(manager, new MemoryCertificateQuarantine());
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        assertThrows(
            UncheckedIOException.class,
            () -> {
              if (operation.equals("remove")) {
                ByteString thumbprint = CertificateUtil.thumbprint(RETAINED.certificate());
                if (list.equals("trusted")) {
                  manager.removeTrustedCertificate(thumbprint);
                } else {
                  manager.removeIssuerCertificate(thumbprint);
                }
              } else if (list.equals("trusted")) {
                manager.setTrustedCertificates(List.of());
              } else {
                manager.setIssuerCertificates(List.of());
              }
            });

        assertTrue(Files.exists(retainedFile));
        assertEquals(List.of(RETAINED.certificate()), certificates(manager, list));
        assertEquals(before, manager.getLastUpdateTime());

        if (list.equals("trusted")) {
          assertDoesNotThrow(
              () ->
                  validator.validateCertificateChain(List.of(RETAINED.certificate()), null, null));
        }
      }
    }

    // When multiple files hold the same certificate and deleting one fails, remaining copies must
    // still be deleted before reporting failure and reconciling to the surviving file.
    @Test
    void failedDeleteOfOneDuplicateStillDeletesOtherCopyAndReconciles() throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path undeletable = dirs.trustedCerts().resolve("a-undeletable.der");
      Path deletable = dirs.trustedCerts().resolve("b-deletable.der");
      Files.write(undeletable, RETAINED.certificate().getEncoded());
      Files.write(deletable, RETAINED.certificate().getEncoded());

      try (var manager =
          new FaultInjectingTrustListManager(dirs, undeletable::equals, p -> false, p -> false)) {
        manager.initialize();
        assertEquals(List.of(RETAINED.certificate()), manager.getTrustedCertificates());
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        ByteString thumbprint = CertificateUtil.thumbprint(RETAINED.certificate());
        assertThrows(
            UncheckedIOException.class, () -> manager.removeTrustedCertificate(thumbprint));

        assertTrue(Files.exists(undeletable));
        assertFalse(Files.exists(deletable), "deletable duplicate copy must still be removed");
        assertEquals(List.of(RETAINED.certificate()), manager.getTrustedCertificates());
        assertEquals(
            before,
            manager.getLastUpdateTime(),
            "surviving duplicate leaves the trusted certificate set unchanged");
      }
    }

    // A file that cannot be read must not stop a removal: the removed certificate leaves memory and
    // disk at once, and the unreadable file, which holds another certificate, is left alone.
    @ParameterizedTest
    @CsvSource({"trusted,remove", "trusted,replace", "issuer,remove", "issuer,replace"})
    void removalBesideAnUnreadableFileRemovesCertificateFromMemoryAndDisk(
        String list, String operation) throws Exception {

      Dirs dirs = createDirs(baseDir);
      Path certsDir = list.equals("trusted") ? dirs.trustedCerts() : dirs.issuerCerts();
      Path removedFile = certsDir.resolve("removed.der");
      Path unreadable = certsDir.resolve("unreadable.der");
      Files.write(removedFile, RETAINED.certificate().getEncoded());
      Files.write(unreadable, SECOND.certificate().getEncoded());

      var blockRead = new AtomicBoolean(false);

      try (var manager =
          new FaultInjectingTrustListManager(
              dirs,
              p -> false,
              p -> false,
              p -> false,
              path -> blockRead.get() && path.equals(unreadable))) {
        manager.initialize();
        var validator =
            new DefaultClientCertificateValidator(manager, new MemoryCertificateQuarantine());
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        blockRead.set(true);
        ByteString thumbprint = CertificateUtil.thumbprint(RETAINED.certificate());
        if (operation.equals("remove")) {
          boolean removed =
              list.equals("trusted")
                  ? manager.removeTrustedCertificate(thumbprint)
                  : manager.removeIssuerCertificate(thumbprint);
          assertTrue(removed);
        } else if (list.equals("trusted")) {
          manager.setTrustedCertificates(List.of(SECOND.certificate()));
        } else {
          manager.setIssuerCertificates(List.of(SECOND.certificate()));
        }

        assertFalse(Files.exists(removedFile));
        assertTrue(Files.exists(unreadable), "the unreadable file must be left alone");
        assertEquals(List.of(SECOND.certificate()), certificates(manager, list));
        assertTrue(manager.getLastUpdateTime().getUtcTime() > before.getUtcTime());

        if (list.equals("trusted")) {
          assertThrows(
              UaException.class,
              () ->
                  validator.validateCertificateChain(List.of(RETAINED.certificate()), null, null));
        }
      }

      try (var reopened = FileBasedTrustListManager.createAndInitialize(baseDir)) {
        assertEquals(List.of(SECOND.certificate()), certificates(reopened, list));
      }
    }

    // A file that cannot be read cannot be checked, so a removal leaves it on disk even when it
    // holds a copy of the certificate. The removal still takes effect for every readable copy and
    // for memory, so the validator stops trusting the certificate at once.
    @Test
    void unreadableCopyIsLeftOnDiskWhileRemovalUntrustsCertificate() throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path unreadable = dirs.trustedCerts().resolve("a-unreadable.der");
      Path deletable = dirs.trustedCerts().resolve("b-deletable.der");
      Files.write(unreadable, RETAINED.certificate().getEncoded());
      Files.write(deletable, RETAINED.certificate().getEncoded());

      var blockRead = new AtomicBoolean(false);

      try (var manager =
          new FaultInjectingTrustListManager(
              dirs,
              p -> false,
              p -> false,
              p -> false,
              path -> blockRead.get() && path.equals(unreadable))) {
        manager.initialize();
        var validator =
            new DefaultClientCertificateValidator(manager, new MemoryCertificateQuarantine());
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        blockRead.set(true);
        ByteString thumbprint = CertificateUtil.thumbprint(RETAINED.certificate());
        assertTrue(manager.removeTrustedCertificate(thumbprint));

        assertTrue(Files.exists(unreadable), "the unreadable copy must be left on disk");
        assertFalse(Files.exists(deletable), "the readable copy must be deleted");
        assertEquals(List.of(), manager.getTrustedCertificates());
        assertTrue(manager.getLastUpdateTime().getUtcTime() > before.getUtcTime());
        assertThrows(
            UaException.class,
            () -> validator.validateCertificateChain(List.of(RETAINED.certificate()), null, null));
      }
    }

    // A metadata lookup failure (e.g., stat denied before reading bytes) is a file that cannot be
    // read: the removal leaves that file on disk, deletes the readable copy, and untrusts the
    // certificate.
    @Test
    void metadataLookupFailureLeavesCopyOnDiskWhileRemovalUntrustsCertificate() throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path unstatable = dirs.trustedCerts().resolve("a-unstatable.der");
      Path deletable = dirs.trustedCerts().resolve("b-deletable.der");
      Files.write(unstatable, RETAINED.certificate().getEncoded());
      Files.write(deletable, RETAINED.certificate().getEncoded());

      var blockAttributes = new AtomicBoolean(false);

      try (var manager =
          new FaultInjectingTrustListManager(
              dirs,
              p -> false,
              p -> false,
              p -> false,
              p -> false,
              path -> blockAttributes.get() && path.equals(unstatable))) {
        manager.initialize();
        var validator =
            new DefaultClientCertificateValidator(manager, new MemoryCertificateQuarantine());
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        blockAttributes.set(true);
        ByteString thumbprint = CertificateUtil.thumbprint(RETAINED.certificate());
        assertTrue(manager.removeTrustedCertificate(thumbprint));

        assertTrue(Files.exists(unstatable), "the unreadable copy must be left on disk");
        assertFalse(Files.exists(deletable), "the readable copy must be deleted");
        assertEquals(List.of(), manager.getTrustedCertificates());
        assertTrue(manager.getLastUpdateTime().getUtcTime() > before.getUtcTime());
        assertThrows(
            UaException.class,
            () -> validator.validateCertificateChain(List.of(RETAINED.certificate()), null, null));
      }
    }
  }

  @Nested
  class CrlPersistenceFailures {

    // A CRL whose file cannot be written must fail the update, leave existing CRLs intact, and
    // leave no temporary files behind.
    @ParameterizedTest
    @ValueSource(strings = {"trusted", "issuer"})
    void crlWriteFailureThrowsAndLeavesSnapshotMatchingDisk(String list) throws Exception {
      Path crlDir = Files.createDirectories(baseDir.resolve(list).resolve("crl"));
      Files.write(crlDir.resolve("retained.crl"), RETAINED.crl().getEncoded());
      blockTargetWithNonEmptyDirectory(
          crlDir.resolve(thumbprintHex(SECOND.crl().getEncoded()) + ".crl"));

      try (var manager = FileBasedTrustListManager.createAndInitialize(baseDir)) {
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        assertThrows(
            UncheckedIOException.class,
            () -> {
              if (list.equals("trusted")) {
                manager.setTrustedCrls(List.of(RETAINED.crl(), SECOND.crl()));
              } else {
                manager.setIssuerCrls(List.of(RETAINED.crl(), SECOND.crl()));
              }
            });

        assertEquals(List.of(RETAINED.crl()), crls(manager, list));
        assertEquals(before, manager.getLastUpdateTime());
        assertNoTempFiles(crlDir);
      }
    }

    // An unencodable CRL must surface CRLException via UncheckedIOException without changing disk
    // or in-memory state.
    @Test
    void unencodableCrlFailsWithoutChangingSnapshot() throws Exception {
      Path crlDir = Files.createDirectories(baseDir.resolve("trusted").resolve("crl"));
      Files.write(crlDir.resolve("retained.crl"), RETAINED.crl().getEncoded());
      var unencodable = new UnencodableCrl();

      try (var manager = FileBasedTrustListManager.createAndInitialize(baseDir)) {
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        UncheckedIOException thrown =
            assertThrows(
                UncheckedIOException.class,
                () -> manager.setTrustedCrls(List.of(RETAINED.crl(), unencodable)));

        assertInstanceOf(CRLException.class, thrown.getCause().getCause());
        assertEquals(List.of(RETAINED.crl()), manager.getTrustedCrls());
        assertEquals(before, manager.getLastUpdateTime());
        assertNoTempFiles(crlDir);
      }
    }

    // Failing to delete a CRL file must keep that CRL in the published snapshot immediately.
    @ParameterizedTest
    @ValueSource(strings = {"trusted", "issuer"})
    void crlDeleteFailureReconcilesSnapshotToDisk(String list) throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path crlDir = list.equals("trusted") ? dirs.trustedCrl() : dirs.issuerCrl();
      Path crlFile = crlDir.resolve("retained.crl");
      Files.write(crlFile, RETAINED.crl().getEncoded());

      try (var manager =
          new FaultInjectingTrustListManager(dirs, crlFile::equals, p -> false, p -> false)) {
        manager.initialize();
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        assertThrows(
            UncheckedIOException.class,
            () -> {
              if (list.equals("trusted")) {
                manager.setTrustedCrls(List.of());
              } else {
                manager.setIssuerCrls(List.of());
              }
            });

        assertTrue(Files.exists(crlFile));
        assertEquals(List.of(RETAINED.crl()), crls(manager, list));
        assertEquals(before, manager.getLastUpdateTime());
      }
    }

    // When rewriting a multi-CRL bundle fails, the original bundle must stay intact on disk with
    // all its members and permissions, and memory must reconcile back to those members.
    @Test
    void failedCrlBundleRewritePreservesSurvivingBundleOnDiskAndInMemory() throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path bundle = dirs.trustedCrl().resolve("operator-bundle.pem");
      Files.writeString(
          bundle,
          pem("X509 CRL", RETAINED.crl().getEncoded())
              + pem("X509 CRL", SECOND.crl().getEncoded()));

      boolean posix = bundle.getFileSystem().supportedFileAttributeViews().contains("posix");
      if (posix) {
        Files.setPosixFilePermissions(bundle, PosixFilePermissions.fromString("rw-r-----"));
      }

      try (var manager =
          new FaultInjectingTrustListManager(dirs, p -> false, bundle::equals, p -> false)) {
        manager.initialize();
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        assertThrows(
            UncheckedIOException.class, () -> manager.setTrustedCrls(List.of(SECOND.crl())));

        assertEquals(Set.of(RETAINED.crl(), SECOND.crl()), Set.copyOf(manager.getTrustedCrls()));
        assertEquals(before, manager.getLastUpdateTime());
        assertNoTempFiles(dirs.trustedCrl());
        if (posix) {
          assertEquals(
              PosixFilePermissions.fromString("rw-r-----"), Files.getPosixFilePermissions(bundle));
        }
      }

      try (var reopened = FileBasedTrustListManager.createAndInitialize(baseDir)) {
        assertEquals(Set.of(RETAINED.crl(), SECOND.crl()), Set.copyOf(reopened.getTrustedCrls()));
      }
    }

    // A file that cannot be read must not stop a CRL removal: the removed CRL leaves memory and
    // disk at once, and the unreadable file, which holds another CRL, is left alone.
    @ParameterizedTest
    @ValueSource(strings = {"trusted", "issuer"})
    void removalBesideAnUnreadableFileRemovesCrlFromMemoryAndDisk(String list) throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path crlDir = list.equals("trusted") ? dirs.trustedCrl() : dirs.issuerCrl();
      Path removedFile = crlDir.resolve("removed.crl");
      Path unreadable = crlDir.resolve("unreadable.crl");
      Files.write(removedFile, RETAINED.crl().getEncoded());
      Files.write(unreadable, SECOND.crl().getEncoded());

      var blockRead = new AtomicBoolean(false);

      try (var manager =
          new FaultInjectingTrustListManager(
              dirs,
              p -> false,
              p -> false,
              p -> false,
              path -> blockRead.get() && path.equals(unreadable))) {
        manager.initialize();
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        blockRead.set(true);
        if (list.equals("trusted")) {
          manager.setTrustedCrls(List.of(SECOND.crl()));
        } else {
          manager.setIssuerCrls(List.of(SECOND.crl()));
        }

        assertFalse(Files.exists(removedFile));
        assertTrue(Files.exists(unreadable), "the unreadable file must be left alone");
        assertEquals(List.of(SECOND.crl()), crls(manager, list));
        assertTrue(manager.getLastUpdateTime().getUtcTime() > before.getUtcTime());
      }

      try (var reopened = FileBasedTrustListManager.createAndInitialize(baseDir)) {
        assertEquals(List.of(SECOND.crl()), crls(reopened, list));
      }
    }

    // A file that cannot be read cannot be checked, so a CRL removal leaves it on disk even when it
    // holds a copy of the CRL. The removal still takes effect for every readable copy and for
    // memory.
    @Test
    void unreadableCopyIsLeftOnDiskWhileRemovalDropsCrl() throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path unreadable = dirs.trustedCrl().resolve("a-unreadable.crl");
      Path deletable = dirs.trustedCrl().resolve("b-deletable.crl");
      Files.write(unreadable, RETAINED.crl().getEncoded());
      Files.write(deletable, RETAINED.crl().getEncoded());

      var blockRead = new AtomicBoolean(false);

      try (var manager =
          new FaultInjectingTrustListManager(
              dirs,
              p -> false,
              p -> false,
              p -> false,
              path -> blockRead.get() && path.equals(unreadable))) {
        manager.initialize();
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        blockRead.set(true);
        manager.setTrustedCrls(List.of());

        assertTrue(Files.exists(unreadable), "the unreadable copy must be left on disk");
        assertFalse(Files.exists(deletable), "the readable copy must be deleted");
        assertEquals(List.of(), manager.getTrustedCrls());
        assertTrue(manager.getLastUpdateTime().getUtcTime() > before.getUtcTime());
      }
    }

    // A metadata lookup failure is a file that cannot be read: the CRL removal leaves that file on
    // disk, deletes the readable copy, and drops the CRL from memory.
    @Test
    void metadataLookupFailureLeavesCopyOnDiskWhileRemovalDropsCrl() throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path unstatable = dirs.trustedCrl().resolve("a-unstatable.crl");
      Path deletable = dirs.trustedCrl().resolve("b-deletable.crl");
      Files.write(unstatable, RETAINED.crl().getEncoded());
      Files.write(deletable, RETAINED.crl().getEncoded());

      var blockAttributes = new AtomicBoolean(false);

      try (var manager =
          new FaultInjectingTrustListManager(
              dirs,
              p -> false,
              p -> false,
              p -> false,
              p -> false,
              path -> blockAttributes.get() && path.equals(unstatable))) {
        manager.initialize();
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        blockAttributes.set(true);
        manager.setTrustedCrls(List.of());

        assertTrue(Files.exists(unstatable), "the unreadable copy must be left on disk");
        assertFalse(Files.exists(deletable), "the readable copy must be deleted");
        assertEquals(List.of(), manager.getTrustedCrls());
        assertTrue(manager.getLastUpdateTime().getUtcTime() > before.getUtcTime());
      }
    }
  }

  @Nested
  class PartialUpdatesAndReconciliation {

    // When one entry in a multi-item update persists and a later entry fails, the reconciled
    // snapshot must reflect the persisted entry and advance lastUpdateTime.
    @Test
    void partialMultiItemUpdateReconcilesPersistedEntryAndAdvancesLastUpdateTime()
        throws Exception {
      Path certsDir = Files.createDirectories(baseDir.resolve("trusted").resolve("certs"));
      blockTargetWithNonEmptyDirectory(
          certsDir.resolve(thumbprintHex(SECOND.certificate().getEncoded()) + ".der"));

      try (var manager = FileBasedTrustListManager.createAndInitialize(baseDir)) {
        var validator =
            new DefaultClientCertificateValidator(manager, new MemoryCertificateQuarantine());
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        assertThrows(
            UncheckedIOException.class,
            () ->
                manager.setTrustedCertificates(
                    List.of(RETAINED.certificate(), SECOND.certificate())));

        assertEquals(List.of(RETAINED.certificate()), manager.getTrustedCertificates());
        assertTrue(
            manager.getLastUpdateTime().getUtcTime() > before.getUtcTime(),
            "partial on-disk persistence must advance lastUpdateTime");
        assertNoTempFiles(certsDir);

        assertDoesNotThrow(
            () -> validator.validateCertificateChain(List.of(RETAINED.certificate()), null, null));
        assertThrows(
            UaException.class,
            () -> validator.validateCertificateChain(List.of(SECOND.certificate()), null, null));
      }
    }

    // If re-reading one dirty directory fails during reconciliation, the error is attached as
    // suppressed and the other dirty directory still reconciles to disk.
    @Test
    void reconciliationPreservesReadableDirectoryWhenAnotherRereadFails() throws Exception {
      Dirs dirs = createDirs(baseDir);
      Files.write(dirs.trustedCerts().resolve("retained.der"), RETAINED.certificate().getEncoded());
      blockTargetWithNonEmptyDirectory(
          dirs.trustedCerts().resolve(thumbprintHex(SECOND.certificate().getEncoded()) + ".der"));

      Thread callerThread = Thread.currentThread();
      var failTrustedRead = new AtomicBoolean(false);

      try (var manager =
          new FaultInjectingTrustListManager(
              dirs,
              p -> false,
              p -> false,
              dir ->
                  dir.equals(dirs.trustedCerts())
                      && Thread.currentThread() == callerThread
                      && failTrustedRead.get())) {
        manager.initialize();
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        UncheckedIOException thrown;
        failTrustedRead.set(true);
        try {
          thrown =
              assertThrows(
                  UncheckedIOException.class,
                  () ->
                      manager.update(
                          current ->
                              current
                                  .withIssuerCertificates(List.of(THIRD.certificate()))
                                  .withTrustedCertificates(
                                      List.of(RETAINED.certificate(), SECOND.certificate()))));
        } finally {
          failTrustedRead.set(false);
        }

        assertEquals(1, thrown.getCause().getSuppressed().length);
        assertInstanceOf(IOException.class, thrown.getCause().getSuppressed()[0]);
        assertEquals(List.of(THIRD.certificate()), manager.getIssuerCertificates());
        assertEquals(List.of(RETAINED.certificate()), manager.getTrustedCertificates());
        assertTrue(manager.getLastUpdateTime().getUtcTime() > before.getUtcTime());
      }
    }

    // If an individual file cannot be read during failure reconciliation, that directory must
    // retain its previous in-memory entries instead of silently dropping the unreadable file.
    @Test
    void unreadableFileDuringReconciliationRetainsPreviousEntries() throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path unreadable = dirs.trustedCerts().resolve("retained.der");
      Files.write(unreadable, RETAINED.certificate().getEncoded());
      blockTargetWithNonEmptyDirectory(
          dirs.trustedCerts().resolve(thumbprintHex(SECOND.certificate().getEncoded()) + ".der"));

      var blockRead = new AtomicBoolean(false);

      try (var manager =
          new FaultInjectingTrustListManager(
              dirs,
              p -> false,
              p -> false,
              p -> false,
              path -> blockRead.get() && path.equals(unreadable))) {
        manager.initialize();
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        blockRead.set(true);
        UncheckedIOException thrown =
            assertThrows(
                UncheckedIOException.class,
                () -> manager.addTrustedCertificate(SECOND.certificate()));

        assertEquals(1, thrown.getCause().getSuppressed().length);
        assertInstanceOf(AccessDeniedException.class, thrown.getCause().getSuppressed()[0]);
        assertEquals(List.of(RETAINED.certificate()), manager.getTrustedCertificates());
        assertEquals(before, manager.getLastUpdateTime());
      }
    }

    // A metadata lookup failure during reconciliation or watcher reload must retain previous
    // entries rather than publishing an empty list.
    @Test
    void metadataLookupFailureDuringReconciliationAndSynchronizeRetainsPreviousEntries()
        throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path unstatable = dirs.trustedCerts().resolve("retained.der");
      Files.write(unstatable, RETAINED.certificate().getEncoded());
      blockTargetWithNonEmptyDirectory(
          dirs.trustedCerts().resolve(thumbprintHex(SECOND.certificate().getEncoded()) + ".der"));

      var blockAttributes = new AtomicBoolean(false);

      try (var manager =
          new FaultInjectingTrustListManager(
              dirs,
              p -> false,
              p -> false,
              p -> false,
              p -> false,
              path -> blockAttributes.get() && path.equals(unstatable))) {
        manager.initialize();
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        blockAttributes.set(true);
        UncheckedIOException thrown =
            assertThrows(
                UncheckedIOException.class,
                () -> manager.addTrustedCertificate(SECOND.certificate()));

        assertEquals(1, thrown.getCause().getSuppressed().length);
        assertInstanceOf(AccessDeniedException.class, thrown.getCause().getSuppressed()[0]);
        assertEquals(List.of(RETAINED.certificate()), manager.getTrustedCertificates());
        assertEquals(before, manager.getLastUpdateTime());

        manager.synchronize(
            "trusted certificates",
            current ->
                current.withTrustedCertificates(manager.readCertificates(dirs.trustedCerts())));
        assertEquals(List.of(RETAINED.certificate()), manager.getTrustedCertificates());
        assertEquals(before, manager.getLastUpdateTime());
      }
    }

    // A mid-stream I/O error while decoding must propagate as IOException even when CertificateUtil
    // wraps it in UaException, so a reload keeps the previous entries instead of dropping the file
    // as malformed, while genuinely malformed certificate data is still skipped.
    @Test
    void midStreamReadFailurePropagatesAsIoExceptionWhileCorruptFileIsSkipped() throws Exception {
      Dirs dirs = createDirs(baseDir);
      Path failingCert = dirs.trustedCerts().resolve("a-failing-stream.der");
      Path corruptCert = dirs.trustedCerts().resolve("corrupt.der");
      Files.write(failingCert, RETAINED.certificate().getEncoded());
      Files.writeString(corruptCert, "not a valid certificate");

      Path failingCrl = dirs.trustedCrl().resolve("a-failing-stream.crl");
      Files.write(failingCrl, RETAINED.crl().getEncoded());

      var failMidStream = new AtomicBoolean(false);

      try (var manager =
          new FaultInjectingTrustListManager(
              dirs,
              p -> false,
              p -> false,
              p -> false,
              p -> false,
              p -> false,
              path ->
                  failMidStream.get() && (path.equals(failingCert) || path.equals(failingCrl)))) {
        manager.initialize();
        assertEquals(List.of(RETAINED.certificate()), manager.getTrustedCertificates());
        assertEquals(List.of(RETAINED.crl()), manager.getTrustedCrls());
        DateTime before = manager.getLastUpdateTime();
        awaitClockTickAfter(before);

        failMidStream.set(true);

        IOException certFailure =
            assertThrows(IOException.class, () -> manager.readCertificates(dirs.trustedCerts()));
        assertTrue(certFailure.getMessage().contains("simulated mid-stream read failure"));

        IOException crlFailure =
            assertThrows(IOException.class, () -> manager.readCrls(dirs.trustedCrl()));
        assertTrue(crlFailure.getMessage().contains("simulated mid-stream read failure"));

        manager.synchronize(
            "trusted certificates",
            current ->
                current.withTrustedCertificates(manager.readCertificates(dirs.trustedCerts())));
        manager.synchronize(
            "trusted CRLs",
            current -> current.withTrustedCrls(manager.readCrls(dirs.trustedCrl())));

        assertEquals(List.of(RETAINED.certificate()), manager.getTrustedCertificates());
        assertEquals(List.of(RETAINED.crl()), manager.getTrustedCrls());
        assertEquals(before, manager.getLastUpdateTime());
      }
    }
  }

  private static Dirs createDirs(Path baseDir) throws IOException {
    Path issuerCerts = Files.createDirectories(baseDir.resolve("issuer").resolve("certs"));
    Path issuerCrl = Files.createDirectories(baseDir.resolve("issuer").resolve("crl"));
    Path trustedCerts = Files.createDirectories(baseDir.resolve("trusted").resolve("certs"));
    Path trustedCrl = Files.createDirectories(baseDir.resolve("trusted").resolve("crl"));
    return new Dirs(issuerCerts, issuerCrl, trustedCerts, trustedCrl);
  }

  private static FileBasedTrustListManager createManager(Dirs dirs) {
    return new FileBasedTrustListManager(
        dirs.issuerCerts(), dirs.issuerCrl(), dirs.trustedCerts(), dirs.trustedCrl());
  }

  private static void blockTargetWithNonEmptyDirectory(Path target) throws IOException {
    Files.createDirectories(target);
    Files.writeString(target.resolve("blocker"), "occupied");
  }

  private static void assertNoTempFiles(Path directory) throws IOException {
    try (Stream<Path> entries = Files.list(directory)) {
      List<Path> leftovers =
          entries.filter(path -> path.getFileName().toString().endsWith(".tmp")).toList();
      assertEquals(List.of(), leftovers, "no temporary files may remain in " + directory);
    }
  }

  private static void awaitClockTickAfter(DateTime time) {
    while (DateTime.now().getUtcTime() <= time.getUtcTime()) {
      Thread.onSpinWait();
    }
  }

  private static List<X509Certificate> certificates(
      FileBasedTrustListManager manager, String list) {
    return list.equals("trusted")
        ? manager.getTrustedCertificates()
        : manager.getIssuerCertificates();
  }

  private static List<X509CRL> crls(FileBasedTrustListManager manager, String list) {
    return list.equals("trusted") ? manager.getTrustedCrls() : manager.getIssuerCrls();
  }

  private static String thumbprintHex(byte[] encoded) {
    return ByteBufUtil.hexDump(sha1(encoded));
  }

  private static Material material(String name) {
    try {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      X509Certificate certificate =
          new SelfSignedCertificateBuilder(keyPair)
              .setCommonName(name)
              .setApplicationUri("urn:eclipse:milo:test:" + name)
              .build();
      return new Material(certificate, CrlTestUtil.generateCrl(certificate, keyPair.getPrivate()));
    } catch (Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private static String pem(String type, byte[] bytes) {
    return "-----BEGIN "
        + type
        + "-----\n"
        + Base64.getMimeEncoder(64, new byte[] {'\n'}).encodeToString(bytes)
        + "\n-----END "
        + type
        + "-----\n";
  }

  private record Dirs(Path issuerCerts, Path issuerCrl, Path trustedCerts, Path trustedCrl) {}

  private record Material(X509Certificate certificate, X509CRL crl) {}

  private static final class FaultInjectingTrustListManager extends FileBasedTrustListManager {

    private final Predicate<Path> failDelete;
    private final Predicate<Path> failWrite;
    private final Predicate<Path> failReadCerts;
    private final Predicate<Path> failReadFile;
    private final Predicate<Path> failReadAttributes;
    private final Predicate<Path> failMidStreamRead;

    FaultInjectingTrustListManager(
        Dirs dirs,
        Predicate<Path> failDelete,
        Predicate<Path> failWrite,
        Predicate<Path> failReadCerts) {
      this(dirs, failDelete, failWrite, failReadCerts, p -> false, p -> false, p -> false);
    }

    FaultInjectingTrustListManager(
        Dirs dirs,
        Predicate<Path> failDelete,
        Predicate<Path> failWrite,
        Predicate<Path> failReadCerts,
        Predicate<Path> failReadFile) {
      this(dirs, failDelete, failWrite, failReadCerts, failReadFile, p -> false, p -> false);
    }

    FaultInjectingTrustListManager(
        Dirs dirs,
        Predicate<Path> failDelete,
        Predicate<Path> failWrite,
        Predicate<Path> failReadCerts,
        Predicate<Path> failReadFile,
        Predicate<Path> failReadAttributes) {
      this(
          dirs, failDelete, failWrite, failReadCerts, failReadFile, failReadAttributes, p -> false);
    }

    FaultInjectingTrustListManager(
        Dirs dirs,
        Predicate<Path> failDelete,
        Predicate<Path> failWrite,
        Predicate<Path> failReadCerts,
        Predicate<Path> failReadFile,
        Predicate<Path> failReadAttributes,
        Predicate<Path> failMidStreamRead) {
      super(dirs.issuerCerts(), dirs.issuerCrl(), dirs.trustedCerts(), dirs.trustedCrl());
      this.failDelete = failDelete;
      this.failWrite = failWrite;
      this.failReadCerts = failReadCerts;
      this.failReadFile = failReadFile;
      this.failReadAttributes = failReadAttributes;
      this.failMidStreamRead = failMidStreamRead;
    }

    @Override
    void deleteFile(Path file) throws IOException {
      if (failDelete.test(file)) {
        throw new IOException("simulated delete failure: " + file);
      }
      super.deleteFile(file);
    }

    @Override
    void writeFileAtomically(Path target, String tempPrefix, byte[] bytes) throws IOException {
      if (failWrite.test(target)) {
        throw new IOException("simulated write failure: " + target);
      }
      super.writeFileAtomically(target, tempPrefix, bytes);
    }

    @Override
    List<X509Certificate> readCertificates(Path directory) throws IOException {
      if (failReadCerts.test(directory)) {
        throw new IOException("simulated read failure: " + directory);
      }
      return super.readCertificates(directory);
    }

    @Override
    BasicFileAttributes readFileAttributes(Path path) throws IOException {
      if (failReadAttributes.test(path)) {
        throw new AccessDeniedException(path.toString());
      }
      return super.readFileAttributes(path);
    }

    @Override
    InputStream openFileInputStream(Path path) throws IOException {
      if (failReadFile.test(path)) {
        throw new AccessDeniedException(path.toString());
      }
      InputStream stream = super.openFileInputStream(path);
      if (failMidStreamRead.test(path)) {
        return new MidStreamFailingInputStream(stream, path);
      }
      return stream;
    }
  }

  private static final class MidStreamFailingInputStream extends FilterInputStream {

    private final Path path;
    private int remaining = 8;

    MidStreamFailingInputStream(InputStream in, Path path) {
      super(in);
      this.path = path;
    }

    @Override
    public int read() throws IOException {
      if (remaining <= 0) {
        throw new IOException("simulated mid-stream read failure: " + path);
      }
      remaining--;
      return in.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      if (remaining <= 0) {
        throw new IOException("simulated mid-stream read failure: " + path);
      }
      int toRead = Math.min(len, remaining);
      int read = in.read(b, off, toRead);
      if (read > 0) {
        remaining -= read;
      }
      return read;
    }
  }

  @SuppressWarnings("deprecation")
  private static final class UnencodableCertificate extends X509Certificate {

    @Override
    public byte[] getEncoded() throws CertificateEncodingException {
      throw new CertificateEncodingException("simulated certificate encoding failure");
    }

    @Override
    public boolean equals(Object other) {
      return this == other;
    }

    @Override
    public int hashCode() {
      return System.identityHashCode(this);
    }

    @Override
    public String toString() {
      return "UnencodableCertificate";
    }

    @Override
    public void checkValidity() {}

    @Override
    public void checkValidity(Date date) {}

    @Override
    public int getVersion() {
      return 3;
    }

    @Override
    public BigInteger getSerialNumber() {
      return BigInteger.ONE;
    }

    @Override
    public Principal getIssuerDN() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Principal getSubjectDN() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Date getNotBefore() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Date getNotAfter() {
      throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getTBSCertificate() {
      throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getSignature() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getSigAlgName() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getSigAlgOID() {
      throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getSigAlgParams() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean[] getIssuerUniqueID() {
      return null;
    }

    @Override
    public boolean[] getSubjectUniqueID() {
      return null;
    }

    @Override
    public boolean[] getKeyUsage() {
      return null;
    }

    @Override
    public int getBasicConstraints() {
      return -1;
    }

    @Override
    public void verify(PublicKey key) {}

    @Override
    public void verify(PublicKey key, String sigProvider) {}

    @Override
    public PublicKey getPublicKey() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasUnsupportedCriticalExtension() {
      return false;
    }

    @Override
    public Set<String> getCriticalExtensionOIDs() {
      return Set.of();
    }

    @Override
    public Set<String> getNonCriticalExtensionOIDs() {
      return Set.of();
    }

    @Override
    public byte[] getExtensionValue(String oid) {
      return null;
    }
  }

  @SuppressWarnings("deprecation")
  private static final class UnencodableCrl extends X509CRL {

    @Override
    public byte[] getEncoded() throws CRLException {
      throw new CRLException("simulated CRL encoding failure");
    }

    @Override
    public boolean equals(Object other) {
      return this == other;
    }

    @Override
    public int hashCode() {
      return System.identityHashCode(this);
    }

    @Override
    public String toString() {
      return "UnencodableCrl";
    }

    @Override
    public void verify(PublicKey key) {}

    @Override
    public void verify(PublicKey key, String sigProvider) {}

    @Override
    public int getVersion() {
      return 2;
    }

    @Override
    public Principal getIssuerDN() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Date getThisUpdate() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Date getNextUpdate() {
      throw new UnsupportedOperationException();
    }

    @Override
    public X509CRLEntry getRevokedCertificate(BigInteger serialNumber) {
      return null;
    }

    @Override
    public Set<? extends X509CRLEntry> getRevokedCertificates() {
      return Set.of();
    }

    @Override
    public byte[] getTBSCertList() {
      throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getSignature() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getSigAlgName() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getSigAlgOID() {
      throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getSigAlgParams() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRevoked(Certificate cert) {
      return false;
    }

    @Override
    public boolean hasUnsupportedCriticalExtension() {
      return false;
    }

    @Override
    public Set<String> getCriticalExtensionOIDs() {
      return Set.of();
    }

    @Override
    public Set<String> getNonCriticalExtensionOIDs() {
      return Set.of();
    }

    @Override
    public byte[] getExtensionValue(String oid) {
      return null;
    }
  }
}
