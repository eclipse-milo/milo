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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
}
