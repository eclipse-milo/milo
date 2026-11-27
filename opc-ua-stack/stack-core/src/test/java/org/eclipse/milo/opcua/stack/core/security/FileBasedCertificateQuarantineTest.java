/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import io.netty.buffer.ByteBufUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileBasedCertificateQuarantineTest extends CertificateQuarantineTest {

  @TempDir Path directory;

  @Override
  protected CertificateQuarantine newCertificateQuarantine(int maxRejectedCertificates) {
    return new FileBasedCertificateQuarantine(directory.toFile(), maxRejectedCertificates);
  }

  @Override
  protected int getMaxRejectedCertificates() {
    return 3;
  }

  // Finder writes a .DS_Store file into folders it displays. Entries like that, and directories,
  // are not rejected certificates: they must not take a slot under the limit, and pruning must not
  // delete them as the oldest entries.
  @Test
  void nonCertificateEntriesAreNotCountedOrPruned() throws Exception {
    Path dsStore = Files.write(directory.resolve(".DS_Store"), new byte[] {0, 0, 0, 1});
    Files.setLastModifiedTime(dsStore, FileTime.fromMillis(0));
    Path subdirectory = Files.createDirectory(directory.resolve("subdirectory"));
    Files.setLastModifiedTime(subdirectory, FileTime.from(Instant.now().plus(1, ChronoUnit.DAYS)));

    List<X509Certificate> rejected = new ArrayList<>();
    for (int i = 0; i < getMaxRejectedCertificates(); i++) {
      X509Certificate certificate =
          certificateFactory
              .createRsaSha256CertificateChain(certificateFactory.createRsaSha256KeyPair())[0];
      certificateQuarantine.addRejectedCertificate(certificate);
      rejected.add(certificate);
    }

    assertEquals(
        Set.copyOf(rejected),
        Set.copyOf(certificateQuarantine.getRejectedCertificates()),
        "every certificate up to the limit is kept");
    assertTrue(Files.exists(dsStore), ".DS_Store is not pruned");
    assertTrue(Files.exists(subdirectory), "subdirectory is not pruned");
  }

  // A hidden file is never part of the quarantine, even when its contents decode as a certificate.
  @Test
  void hiddenFilesAreNotReturned() throws Exception {
    X509Certificate hidden =
        certificateFactory
            .createRsaSha256CertificateChain(certificateFactory.createRsaSha256KeyPair())[0];
    Files.write(directory.resolve(".hidden.der"), hidden.getEncoded());

    assertEquals(List.of(), certificateQuarantine.getRejectedCertificates());
  }

  // Rejected files must be named by SHA-1(DER) thumbprint, matching CertificateUtil.thumbprint()
  // and FileBasedTrustListManager, rather than SHA-1 of the signature bytes.
  @Test
  void addRejectedCertificateNamesFileByThumbprint() throws Exception {
    X509Certificate certificate = newCertificate();
    String expectedThumbprint =
        ByteBufUtil.hexDump(CertificateUtil.thumbprint(certificate).bytesOrEmpty());
    String legacySignatureHash = ByteBufUtil.hexDump(sha1(certificate.getSignature()));
    assertNotEquals(expectedThumbprint, legacySignatureHash);

    certificateQuarantine.addRejectedCertificate(certificate);

    Path expectedFile = directory.resolve(expectedThumbprint + ".der");
    Path legacyFile = directory.resolve(legacySignatureHash + ".der");
    assertTrue(Files.isRegularFile(expectedFile), "file must be named by DER thumbprint");
    assertFalse(Files.exists(legacyFile), "file must not be named by signature hash");

    // Re-adding the same certificate overwrites the same thumbprint file rather than duplicating.
    certificateQuarantine.addRejectedCertificate(certificate);
    try (var entries = Files.list(directory)) {
      assertEquals(List.of(expectedFile), entries.toList());
    }
  }

  // After an upgrade from a signature-hash filename or when an operator places another copy under
  // a different filename, listing deduplicates the certificate and removal deletes every matching
  // copy while leaving unrelated entries untouched.
  @Test
  void duplicateCopiesAreDeduplicatedAndAllDeletedOnRemove() throws Exception {
    X509Certificate target = newCertificate();
    X509Certificate other = newCertificate();

    String targetThumbprint =
        ByteBufUtil.hexDump(CertificateUtil.thumbprint(target).bytesOrEmpty());
    String legacySignatureHash = ByteBufUtil.hexDump(sha1(target.getSignature()));

    Path legacyCopy =
        Files.write(directory.resolve(legacySignatureHash + ".der"), target.getEncoded());
    Path customCopy = Files.write(directory.resolve("custom-copy.der"), target.getEncoded());
    Path corruptFile = Files.write(directory.resolve("corrupt.der"), new byte[] {1, 2, 3, 4});
    Path hiddenCopy = Files.write(directory.resolve(".hidden-copy.der"), target.getEncoded());

    var quarantine = new FileBasedCertificateQuarantine(directory.toFile(), 10);

    quarantine.addRejectedCertificate(target);
    quarantine.addRejectedCertificate(other);

    Path thumbprintCopy = directory.resolve(targetThumbprint + ".der");
    assertTrue(Files.exists(thumbprintCopy));

    List<X509Certificate> listed = quarantine.getRejectedCertificates();
    assertEquals(2, listed.size(), "duplicate files must not list the same certificate twice");
    assertEquals(Set.of(target, other), Set.copyOf(listed));

    quarantine.removeRejectedCertificate(target);

    assertFalse(Files.exists(thumbprintCopy), "thumbprint copy must be deleted");
    assertFalse(Files.exists(legacyCopy), "legacy signature-hash copy must be deleted");
    assertFalse(Files.exists(customCopy), "arbitrarily named copy must be deleted");
    assertTrue(Files.exists(corruptFile), "undecodable files must be left untouched");
    assertTrue(Files.exists(hiddenCopy), "hidden files must be left untouched");
    assertEquals(List.of(other), quarantine.getRejectedCertificates());
  }

  // A delete failure must log as a rejected certificate (not an issuer certificate), continue
  // deleting remaining copies of the same certificate, and report the failure via
  // UncheckedIOException.
  @Test
  void failedDeleteLogsRejectedCertificateAndContinuesDeletingOtherCopies() throws Exception {
    X509Certificate target = newCertificate();
    Path firstCopy = Files.write(directory.resolve("copy-a.der"), target.getEncoded());
    Path secondCopy = Files.write(directory.resolve("copy-b.der"), target.getEncoded());

    // Fail whichever matching copy Files.list() visits first, so the test always proves that
    // removal continues to delete the remaining copy regardless of filesystem directory order.
    var attempts = new AtomicInteger(0);
    var failedCopy = new AtomicReference<Path>();

    var quarantine =
        new FileBasedCertificateQuarantine(directory.toFile(), getMaxRejectedCertificates()) {
          @Override
          boolean deleteCertificateFile(Path path) throws IOException {
            if (attempts.getAndIncrement() == 0) {
              failedCopy.set(path);
              throw new AccessDeniedException(path.toString());
            }
            return super.deleteCertificateFile(path);
          }
        };

    UncheckedIOException thrown;
    PrintStream originalErr = System.err;
    ByteArrayOutputStream capturedErr = new ByteArrayOutputStream();
    System.setErr(new PrintStream(capturedErr, true, StandardCharsets.UTF_8));
    try {
      thrown =
          assertThrows(
              UncheckedIOException.class, () -> quarantine.removeRejectedCertificate(target));
    } finally {
      System.setErr(originalErr);
    }

    Path kept = failedCopy.get();
    Path removed = kept.equals(firstCopy) ? secondCopy : firstCopy;

    assertInstanceOf(AccessDeniedException.class, thrown.getCause());
    assertEquals(2, attempts.get(), "removal must attempt to delete every matching copy");
    assertTrue(Files.exists(kept), "unremovable copy remains on disk");
    assertFalse(Files.exists(removed), "remaining matching copy must still be deleted");

    String logs = capturedErr.toString(StandardCharsets.UTF_8);
    assertTrue(
        logs.contains("Failed to delete rejected certificate: " + kept),
        "expected rejected certificate warning in logs, got: " + logs);
    assertFalse(
        logs.contains("issuer certificate"),
        "warning must not refer to an issuer certificate, got: " + logs);
  }

  @Nested
  class DirectoryAndRemovalFailures {

    // Callers must be able to distinguish "nothing is rejected" from a missing or unmounted
    // rejected directory on both listing and removal.
    @Test
    void missingDirectoryFailsGetAndRemoveWithUncheckedIoException() throws Exception {
      Path subDir = Files.createDirectory(directory.resolve("rejected"));
      var quarantine = new FileBasedCertificateQuarantine(subDir.toFile(), 10);
      X509Certificate cert = newCertificate();
      quarantine.addRejectedCertificate(cert);

      assertEquals(List.of(cert), quarantine.getRejectedCertificates());

      try (var entries = Files.list(subDir)) {
        for (Path entry : entries.toList()) {
          Files.delete(entry);
        }
      }
      Files.delete(subDir);

      UncheckedIOException listFailure =
          assertThrows(UncheckedIOException.class, quarantine::getRejectedCertificates);
      assertInstanceOf(NoSuchFileException.class, listFailure.getCause());

      UncheckedIOException removeFailure =
          assertThrows(
              UncheckedIOException.class, () -> quarantine.removeRejectedCertificate(cert));
      assertInstanceOf(NoSuchFileException.class, removeFailure.getCause());
    }

    // An unreadable directory must not read as empty and must fail removal instead of silently
    // leaving the rejected certificate on disk.
    @Test
    void unreadableDirectoryFailsGetAndRemoveOnPosix() throws Exception {
      assumePosixAndNotRoot();

      X509Certificate cert = newCertificate();
      certificateQuarantine.addRejectedCertificate(cert);
      assertEquals(List.of(cert), certificateQuarantine.getRejectedCertificates());

      Set<PosixFilePermission> originalPermissions = Files.getPosixFilePermissions(directory);
      Files.setPosixFilePermissions(directory, PosixFilePermissions.fromString("-wx------"));
      try {
        UncheckedIOException listFailure =
            assertThrows(
                UncheckedIOException.class, certificateQuarantine::getRejectedCertificates);
        assertInstanceOf(AccessDeniedException.class, listFailure.getCause());

        UncheckedIOException removeFailure =
            assertThrows(
                UncheckedIOException.class,
                () -> certificateQuarantine.removeRejectedCertificate(cert));
        assertInstanceOf(AccessDeniedException.class, removeFailure.getCause());
      } finally {
        Files.setPosixFilePermissions(directory, originalPermissions);
      }

      assertEquals(
          List.of(cert),
          certificateQuarantine.getRejectedCertificates(),
          "certificate must remain rejected after failed removal");
    }

    // A read-only directory prevents file deletion; removal must report the failure rather than
    // returning normally while the file remains on disk.
    @Test
    void readOnlyDirectoryFailsRemoveOnPosix() throws Exception {
      assumePosixAndNotRoot();

      X509Certificate cert = newCertificate();
      certificateQuarantine.addRejectedCertificate(cert);

      Set<PosixFilePermission> originalPermissions = Files.getPosixFilePermissions(directory);
      Files.setPosixFilePermissions(directory, PosixFilePermissions.fromString("r-xr-xr-x"));
      try {
        UncheckedIOException removeFailure =
            assertThrows(
                UncheckedIOException.class,
                () -> certificateQuarantine.removeRejectedCertificate(cert));
        assertInstanceOf(AccessDeniedException.class, removeFailure.getCause());
      } finally {
        Files.setPosixFilePermissions(directory, originalPermissions);
      }

      assertEquals(
          List.of(cert),
          certificateQuarantine.getRejectedCertificates(),
          "certificate must remain rejected after failed delete");
    }

    // Removing a certificate that is not present in a readable directory is a normal no-op.
    @Test
    void removingAbsentCertificateReturnsNormally() {
      X509Certificate absent = newCertificate();

      assertDoesNotThrow(() -> certificateQuarantine.removeRejectedCertificate(absent));
      assertEquals(List.of(), certificateQuarantine.getRejectedCertificates());
    }

    // When multiple matching copies fail to delete, removal must still attempt every copy, delete
    // the copies that can be deleted, and attach secondary delete errors as suppressed exceptions.
    @Test
    void multipleDeleteFailuresAttemptAllCopiesAndAttachSuppressedExceptions() throws Exception {
      X509Certificate target = newCertificate();
      Path undeletableA = Files.write(directory.resolve("a-undeletable.der"), target.getEncoded());
      Path deletable = Files.write(directory.resolve("b-deletable.der"), target.getEncoded());
      Path undeletableC = Files.write(directory.resolve("c-undeletable.der"), target.getEncoded());

      var attempts = new AtomicInteger(0);
      var quarantine =
          new FaultInjectingQuarantine(
              directory.toFile(),
              10,
              path -> {
                attempts.incrementAndGet();
                return path.equals(undeletableA) || path.equals(undeletableC);
              },
              p -> false,
              p -> false,
              p -> false);

      UncheckedIOException thrown =
          assertThrows(
              UncheckedIOException.class, () -> quarantine.removeRejectedCertificate(target));

      assertEquals(3, attempts.get(), "every matching copy must be attempted");
      assertInstanceOf(AccessDeniedException.class, thrown.getCause());
      assertEquals(1, thrown.getCause().getSuppressed().length);
      assertInstanceOf(AccessDeniedException.class, thrown.getCause().getSuppressed()[0]);
      assertTrue(Files.exists(undeletableA), "first undeletable copy remains");
      assertTrue(Files.exists(undeletableC), "second undeletable copy remains");
      assertFalse(Files.exists(deletable), "deletable copy must still be removed");
    }

    // An unencodable certificate cannot be matched against the quarantine and must fail loudly
    // via UncheckedIOException without disturbing existing rejected files.
    @Test
    void unencodableCertificateFailsRemoveWithUncheckedIoException() throws Exception {
      X509Certificate existing = newCertificate();
      certificateQuarantine.addRejectedCertificate(existing);

      UncheckedIOException thrown =
          assertThrows(
              UncheckedIOException.class,
              () -> certificateQuarantine.removeRejectedCertificate(new UnencodableCertificate()));

      assertInstanceOf(CertificateEncodingException.class, thrown.getCause().getCause());
      assertEquals(List.of(existing), certificateQuarantine.getRejectedCertificates());
    }
  }

  @Nested
  class PerFileIoErrors {

    // A file that cannot be opened due to an I/O error must propagate as UncheckedIOException on
    // both listing and removal, while removal still deletes other readable copies.
    @Test
    void openIoErrorPropagatesOnGetAndRemoveWhileDeletingReadableCopies() throws Exception {
      X509Certificate target = newCertificate();
      Path unreadable = Files.write(directory.resolve("a-unreadable.der"), target.getEncoded());
      Path deletable = Files.write(directory.resolve("b-deletable.der"), target.getEncoded());

      var blockOpen = new AtomicBoolean(false);
      var quarantine =
          new FaultInjectingQuarantine(
              directory.toFile(),
              10,
              p -> false,
              p -> false,
              path -> blockOpen.get() && path.equals(unreadable),
              p -> false);

      assertEquals(List.of(target), quarantine.getRejectedCertificates());

      blockOpen.set(true);

      UncheckedIOException listFailure =
          assertThrows(UncheckedIOException.class, quarantine::getRejectedCertificates);
      assertInstanceOf(AccessDeniedException.class, listFailure.getCause());

      UncheckedIOException removeFailure =
          assertThrows(
              UncheckedIOException.class, () -> quarantine.removeRejectedCertificate(target));
      assertInstanceOf(AccessDeniedException.class, removeFailure.getCause());
      assertTrue(Files.exists(unreadable));
      assertFalse(Files.exists(deletable), "readable matching copy must still be deleted");
    }

    // A metadata lookup (stat) I/O error must propagate as UncheckedIOException on listing and
    // removal instead of silently treating the entry as a non-regular file.
    @Test
    void statIoErrorPropagatesOnGetAndRemoveWhileDeletingReadableCopies() throws Exception {
      X509Certificate target = newCertificate();
      Path unstatable = Files.write(directory.resolve("a-unstatable.der"), target.getEncoded());
      Path deletable = Files.write(directory.resolve("b-deletable.der"), target.getEncoded());

      var blockStat = new AtomicBoolean(false);
      var quarantine =
          new FaultInjectingQuarantine(
              directory.toFile(),
              10,
              p -> false,
              path -> blockStat.get() && path.equals(unstatable),
              p -> false,
              p -> false);

      assertEquals(List.of(target), quarantine.getRejectedCertificates());

      blockStat.set(true);

      UncheckedIOException listFailure =
          assertThrows(UncheckedIOException.class, quarantine::getRejectedCertificates);
      assertInstanceOf(AccessDeniedException.class, listFailure.getCause());

      UncheckedIOException removeFailure =
          assertThrows(
              UncheckedIOException.class, () -> quarantine.removeRejectedCertificate(target));
      assertInstanceOf(AccessDeniedException.class, removeFailure.getCause());
      assertTrue(Files.exists(unstatable));
      assertFalse(Files.exists(deletable), "readable matching copy must still be deleted");
    }

    // A mid-stream read IOException must propagate even when wrapped by JCA/CertificateUtil,
    // while genuinely corrupt or empty files continue to be logged and skipped.
    @Test
    void midStreamReadErrorPropagatesWhileCorruptAndEmptyFilesAreSkipped() throws Exception {
      X509Certificate target = newCertificate();
      Path failing = Files.write(directory.resolve("a-failing-stream.der"), target.getEncoded());
      Path deletable = Files.write(directory.resolve("b-deletable.der"), target.getEncoded());
      Path corrupt = Files.writeString(directory.resolve("corrupt.der"), "not a valid certificate");
      Path empty = Files.write(directory.resolve("empty.der"), new byte[0]);

      var failMidStream = new AtomicBoolean(false);
      var quarantine =
          new FaultInjectingQuarantine(
              directory.toFile(),
              10,
              p -> false,
              p -> false,
              p -> false,
              path -> failMidStream.get() && path.equals(failing));

      // Control: corrupt and empty files are skipped when all readable files succeed.
      assertEquals(List.of(target), quarantine.getRejectedCertificates());

      failMidStream.set(true);

      UncheckedIOException listFailure =
          assertThrows(UncheckedIOException.class, quarantine::getRejectedCertificates);
      assertTrue(
          listFailure.getCause().getMessage().contains("simulated mid-stream read failure"),
          "expected mid-stream read IOException, got: " + listFailure.getCause());

      UncheckedIOException removeFailure =
          assertThrows(
              UncheckedIOException.class, () -> quarantine.removeRejectedCertificate(target));
      assertTrue(
          removeFailure.getCause().getMessage().contains("simulated mid-stream read failure"),
          "expected mid-stream read IOException, got: " + removeFailure.getCause());
      assertTrue(Files.exists(failing));
      assertFalse(Files.exists(deletable), "readable matching copy must still be deleted");
      assertTrue(Files.exists(corrupt), "corrupt file must be left untouched");
      assertTrue(Files.exists(empty), "empty file must be left untouched");
    }

    // Entries that disappear between directory listing and stat or open (NoSuchFileException) are
    // expected under concurrent access and must be skipped without throwing.
    @Test
    void vanishedEntriesDuringStatOrOpenAreSkippedOnGetAndRemove() throws Exception {
      X509Certificate kept = newCertificate();
      Path vanishedOnStat =
          Files.write(directory.resolve("a-vanished-stat.der"), kept.getEncoded());
      Path vanishedOnOpen =
          Files.write(directory.resolve("b-vanished-open.der"), kept.getEncoded());
      Path present = Files.write(directory.resolve("c-present.der"), kept.getEncoded());

      var quarantine =
          new FileBasedCertificateQuarantine(directory.toFile(), 10) {
            @Override
            BasicFileAttributes readFileAttributes(Path path) throws IOException {
              if (path.equals(vanishedOnStat)) {
                throw new NoSuchFileException(path.toString());
              }
              return super.readFileAttributes(path);
            }

            @Override
            InputStream openFileInputStream(Path path) throws IOException {
              if (path.equals(vanishedOnOpen)) {
                throw new NoSuchFileException(path.toString());
              }
              return super.openFileInputStream(path);
            }
          };

      assertEquals(List.of(kept), quarantine.getRejectedCertificates());

      assertDoesNotThrow(() -> quarantine.removeRejectedCertificate(kept));
      assertFalse(Files.exists(present), "present matching file must be deleted");
    }
  }

  @Nested
  class AddRejectedCertificateResilience {

    // addRejectedCertificate is called during certificate validation failure handling, so missing
    // directories, unencodable certificates, and pruning I/O errors must be logged rather than
    // thrown.
    @Test
    void addRejectedCertificateDoesNotThrowWhenDirectoryIsMissingOrCertificateIsUnencodable()
        throws Exception {
      Path subDir = Files.createDirectory(directory.resolve("gone"));
      var quarantine = new FileBasedCertificateQuarantine(subDir.toFile(), 2);
      Files.delete(subDir);

      assertDoesNotThrow(() -> quarantine.addRejectedCertificate(newCertificate()));
      assertDoesNotThrow(
          () -> certificateQuarantine.addRejectedCertificate(new UnencodableCertificate()));
    }

    // Pruning failures (unstatable entries or failed deletes of old entries) must not prevent
    // addRejectedCertificate from returning normally and attempting to write the new certificate.
    @Test
    void addRejectedCertificateSwallowsPruningErrorsAndWritesNewCertificate() throws Exception {
      X509Certificate first = newCertificate();
      X509Certificate second = newCertificate();
      X509Certificate third = newCertificate();

      Path firstFile = Files.write(directory.resolve("first.der"), first.getEncoded());
      Files.setLastModifiedTime(firstFile, FileTime.fromMillis(1_000));
      Path secondFile = Files.write(directory.resolve("second.der"), second.getEncoded());
      Files.setLastModifiedTime(secondFile, FileTime.fromMillis(2_000));
      Path unstatableFile = Files.write(directory.resolve("unstatable.der"), second.getEncoded());

      var quarantine =
          new FaultInjectingQuarantine(
              directory.toFile(),
              2,
              firstFile::equals,
              unstatableFile::equals,
              p -> false,
              p -> false);

      assertDoesNotThrow(() -> quarantine.addRejectedCertificate(third));

      String thirdThumbprint =
          ByteBufUtil.hexDump(CertificateUtil.thumbprint(third).bytesOrEmpty());
      assertTrue(
          Files.exists(directory.resolve(thirdThumbprint + ".der")),
          "new rejected certificate must still be written when pruning fails");
    }

    // Unchecked storage failures (UncheckedIOException or SecurityException) during pruning must
    // also be swallowed so addRejectedCertificate still attempts the write and never replaces the
    // validator's UaException.
    @Test
    void addRejectedCertificateSwallowsUncheckedPruningErrorsAndWritesNewCertificate()
        throws Exception {
      X509Certificate first = newCertificate();
      X509Certificate second = newCertificate();
      X509Certificate third = newCertificate();

      Path firstFile = Files.write(directory.resolve("first.der"), first.getEncoded());
      Files.setLastModifiedTime(firstFile, FileTime.fromMillis(1_000));
      Path secondFile = Files.write(directory.resolve("second.der"), second.getEncoded());
      Files.setLastModifiedTime(secondFile, FileTime.fromMillis(2_000));
      Path unstatableFile = Files.write(directory.resolve("unstatable.der"), second.getEncoded());

      var quarantine =
          new FileBasedCertificateQuarantine(directory.toFile(), 2) {
            @Override
            BasicFileAttributes readFileAttributes(Path path) throws IOException {
              if (path.equals(unstatableFile)) {
                throw new UncheckedIOException(new AccessDeniedException(path.toString()));
              }
              return super.readFileAttributes(path);
            }

            @Override
            boolean deleteCertificateFile(Path path) throws IOException {
              if (path.equals(firstFile)) {
                throw new SecurityException("simulated security manager denial: " + path);
              }
              return super.deleteCertificateFile(path);
            }
          };

      assertDoesNotThrow(() -> quarantine.addRejectedCertificate(third));

      String thirdThumbprint =
          ByteBufUtil.hexDump(CertificateUtil.thumbprint(third).bytesOrEmpty());
      assertTrue(
          Files.exists(directory.resolve(thirdThumbprint + ".der")),
          "new rejected certificate must still be written when unchecked pruning errors occur");
    }
  }

  private void assumePosixAndNotRoot() {
    assumeTrue(directory.getFileSystem().supportedFileAttributeViews().contains("posix"));
    assumeFalse("root".equals(System.getProperty("user.name")), "root ignores permissions");
  }

  private X509Certificate newCertificate() {
    return certificateFactory
        .createRsaSha256CertificateChain(certificateFactory.createRsaSha256KeyPair())[0];
  }

  private static final class FaultInjectingQuarantine extends FileBasedCertificateQuarantine {

    private final Predicate<Path> failDelete;
    private final Predicate<Path> failStat;
    private final Predicate<Path> failOpen;
    private final Predicate<Path> failMidStream;

    FaultInjectingQuarantine(
        File rejectedDir,
        int maxRejectedCertificates,
        Predicate<Path> failDelete,
        Predicate<Path> failStat,
        Predicate<Path> failOpen,
        Predicate<Path> failMidStream) {
      super(rejectedDir, maxRejectedCertificates);
      this.failDelete = failDelete;
      this.failStat = failStat;
      this.failOpen = failOpen;
      this.failMidStream = failMidStream;
    }

    @Override
    boolean deleteCertificateFile(Path path) throws IOException {
      if (failDelete.test(path)) {
        throw new AccessDeniedException(path.toString());
      }
      return super.deleteCertificateFile(path);
    }

    @Override
    BasicFileAttributes readFileAttributes(Path path) throws IOException {
      if (failStat.test(path)) {
        throw new AccessDeniedException(path.toString());
      }
      return super.readFileAttributes(path);
    }

    @Override
    InputStream openFileInputStream(Path path) throws IOException {
      if (failOpen.test(path)) {
        throw new AccessDeniedException(path.toString());
      }
      InputStream stream = super.openFileInputStream(path);
      if (failMidStream.test(path)) {
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
}
