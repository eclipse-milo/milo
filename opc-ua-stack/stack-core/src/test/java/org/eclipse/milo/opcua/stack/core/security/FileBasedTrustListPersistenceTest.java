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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.KeyPair;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.CrlTestUtil;
import org.eclipse.milo.opcua.stack.core.util.DigestUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FileBasedTrustListPersistenceTest {

  @TempDir Path directory;

  // Every filename accepted by import must participate in removal, including duplicate files.
  // Checking a freshly opened manager distinguishes persistence from an in-memory snapshot edit.
  @ParameterizedTest
  @CsvSource({"trusted,remove", "trusted,replace", "issuer,remove", "issuer,replace"})
  void removingImportedCertificatesSurvivesRestart(String list, String operation) throws Exception {
    Material removed = material("Removed");
    Material retained = material("Retained");
    Path certificates = Files.createDirectories(directory.resolve(list).resolve("certs"));
    Path imported = certificates.resolve("operator-certificate.pem");
    Path duplicate = certificates.resolve("another-name.cer");
    Files.writeString(imported, pem("CERTIFICATE", removed.certificate().getEncoded()));
    Files.write(duplicate, removed.certificate().getEncoded());
    Files.write(certificates.resolve("retained.cer"), retained.certificate().getEncoded());

    try (var manager = FileBasedTrustListManager.createAndInitialize(directory)) {
      assertEquals(
          Set.of(removed.certificate(), retained.certificate()),
          Set.copyOf(certificates(manager, list)),
          "imports expose distinct certificate entries");
      if (operation.equals("remove")) {
        ByteString thumbprint = ByteString.of(DigestUtil.sha1(removed.certificate().getEncoded()));
        boolean changed =
            list.equals("trusted")
                ? manager.removeTrustedCertificate(thumbprint)
                : manager.removeIssuerCertificate(thumbprint);
        assertTrue(changed);
      } else if (list.equals("trusted")) {
        manager.setTrustedCertificates(List.of(retained.certificate()));
      } else {
        manager.setIssuerCertificates(List.of(retained.certificate()));
      }
      assertEquals(List.of(retained.certificate()), certificates(manager, list));
    }

    try (var reopened = FileBasedTrustListManager.createAndInitialize(directory)) {
      assertEquals(List.of(retained.certificate()), certificates(reopened, list));
      assertFalse(Files.exists(imported));
      assertFalse(Files.exists(duplicate));
    }
  }

  // One accepted CRL file can contain multiple CRLs. Removing a member must persist without
  // deleting unrelated members of that bundle or leaving another copy of the removed CRL.
  @ParameterizedTest
  @ValueSource(strings = {"trusted", "issuer"})
  void replacingImportedCrlsPreservesOtherBundleMembersAcrossRestart(String list) throws Exception {
    Material removed = material("Removed");
    Material retained = material("Retained");
    Material alsoRetained = material("AlsoRetained");
    Path crls = Files.createDirectories(directory.resolve(list).resolve("crl"));
    Path bundle = crls.resolve("operator-bundle.pem");
    Files.writeString(
        bundle,
        pem("X509 CRL", removed.crl().getEncoded())
            + pem("X509 CRL", retained.crl().getEncoded())
            + pem("X509 CRL", alsoRetained.crl().getEncoded()));
    Files.write(crls.resolve("duplicate.revocations"), removed.crl().getEncoded());
    boolean posix = bundle.getFileSystem().supportedFileAttributeViews().contains("posix");
    if (posix) {
      Files.setPosixFilePermissions(bundle, PosixFilePermissions.fromString("rw-r-----"));
    }

    try (var manager = FileBasedTrustListManager.createAndInitialize(directory)) {
      assertEquals(
          Set.of(removed.crl(), retained.crl(), alsoRetained.crl()),
          Set.copyOf(crls(manager, list)),
          "imports expose distinct CRL entries");
      if (list.equals("trusted")) {
        manager.setTrustedCrls(List.of(retained.crl(), alsoRetained.crl()));
      } else {
        manager.setIssuerCrls(List.of(retained.crl(), alsoRetained.crl()));
      }
      assertEquals(List.of(retained.crl(), alsoRetained.crl()), crls(manager, list));
    }

    try (var reopened = FileBasedTrustListManager.createAndInitialize(directory)) {
      assertEquals(List.of(retained.crl(), alsoRetained.crl()), crls(reopened, list));
      if (posix) {
        assertEquals(
            PosixFilePermissions.fromString("rw-r-----"),
            Files.getPosixFilePermissions(bundle),
            "rewriting must preserve group read access");
      }
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

  private static Material material(String name) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName(name)
            .setApplicationUri("urn:eclipse:milo:test:" + name)
            .build();
    return new Material(certificate, CrlTestUtil.generateCrl(certificate, keyPair.getPrivate()));
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

  private record Material(X509Certificate certificate, X509CRL crl) {}
}
