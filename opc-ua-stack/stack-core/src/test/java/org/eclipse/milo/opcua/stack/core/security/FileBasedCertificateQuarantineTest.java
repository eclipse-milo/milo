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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBufUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
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

  // A delete failure must log as a rejected certificate (not an issuer certificate) and must not
  // stop removal from deleting remaining copies of the same certificate.
  @Test
  void failedDeleteLogsRejectedCertificateAndContinuesDeletingOtherCopies() throws Exception {
    X509Certificate target = newCertificate();
    Path lockedCopy = Files.write(directory.resolve("00-locked.der"), target.getEncoded());
    Path deletableCopy = Files.write(directory.resolve("zz-deletable.der"), target.getEncoded());

    var quarantine =
        new FileBasedCertificateQuarantine(directory.toFile(), getMaxRejectedCertificates()) {
          @Override
          boolean deleteCertificateFile(Path path) throws IOException {
            if (path.equals(lockedCopy)) {
              throw new AccessDeniedException(path.toString());
            }
            return super.deleteCertificateFile(path);
          }
        };

    PrintStream originalErr = System.err;
    ByteArrayOutputStream capturedErr = new ByteArrayOutputStream();
    System.setErr(new PrintStream(capturedErr, true, StandardCharsets.UTF_8));
    try {
      quarantine.removeRejectedCertificate(target);
    } finally {
      System.setErr(originalErr);
    }

    assertTrue(Files.exists(lockedCopy), "unremovable copy remains on disk");
    assertFalse(Files.exists(deletableCopy), "remaining matching copy must still be deleted");

    String logs = capturedErr.toString(StandardCharsets.UTF_8);
    assertTrue(
        logs.contains("Failed to delete rejected certificate: " + lockedCopy),
        "expected rejected certificate warning in logs, got: " + logs);
    assertFalse(
        logs.contains("issuer certificate"),
        "warning must not refer to an issuer certificate, got: " + logs);
  }

  private X509Certificate newCertificate() {
    return certificateFactory
        .createRsaSha256CertificateChain(certificateFactory.createRsaSha256KeyPair())[0];
  }
}
