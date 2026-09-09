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
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FileBasedTrustListManagerTest {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  @TempDir Path directory;

  // Files may have been hand-copied into the directory or written by an older Milo version under
  // a different naming scheme. Removal must find them by content, or the certificate comes back
  // the next time the directory is synchronized. Reopening the manager checks the on-disk result
  // rather than the in-memory set.
  @ParameterizedTest
  @CsvSource({"trusted,remove", "trusted,replace", "issuer,remove", "issuer,replace"})
  void removingCertificateWithArbitraryFilenameDeletesItsFiles(String list, String operation)
      throws Exception {
    Material removed = material("Removed");
    Material retained = material("Retained");
    Path certs = Files.createDirectories(directory.resolve(list).resolve("certs"));
    Path imported = certs.resolve("operator-certificate.pem");
    Path duplicate = certs.resolve("another-name.cer");
    Files.writeString(imported, pem("CERTIFICATE", removed.certificate().getEncoded()));
    Files.write(duplicate, removed.certificate().getEncoded());
    Files.write(certs.resolve("retained.cer"), retained.certificate().getEncoded());

    try (var manager = FileBasedTrustListManager.createAndInitialize(directory)) {
      assertEquals(
          Set.of(removed.certificate(), retained.certificate()),
          Set.copyOf(certificates(manager, list)));

      if (operation.equals("remove")) {
        ByteString thumbprint = CertificateUtil.thumbprint(removed.certificate());
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

    assertFalse(Files.exists(imported));
    assertFalse(Files.exists(duplicate));

    try (var reopened = FileBasedTrustListManager.createAndInitialize(directory)) {
      assertEquals(List.of(retained.certificate()), certificates(reopened, list));
    }
  }

  // A single CRL file can bundle several CRLs. Removing one member must not delete the file and
  // take the other members with it, and must not leave a second copy of the removed CRL behind.
  @ParameterizedTest
  @ValueSource(strings = {"trusted", "issuer"})
  void removingCrlFromBundlePreservesOtherMembers(String list) throws Exception {
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
    Path duplicate = crls.resolve("duplicate.revocations");
    Files.write(duplicate, removed.crl().getEncoded());

    boolean posix = bundle.getFileSystem().supportedFileAttributeViews().contains("posix");
    if (posix) {
      Files.setPosixFilePermissions(bundle, PosixFilePermissions.fromString("rw-r-----"));
    }

    try (var manager = FileBasedTrustListManager.createAndInitialize(directory)) {
      assertEquals(
          Set.of(removed.crl(), retained.crl(), alsoRetained.crl()),
          Set.copyOf(crls(manager, list)));

      if (list.equals("trusted")) {
        manager.setTrustedCrls(List.of(retained.crl(), alsoRetained.crl()));
      } else {
        manager.setIssuerCrls(List.of(retained.crl(), alsoRetained.crl()));
      }

      assertEquals(Set.of(retained.crl(), alsoRetained.crl()), Set.copyOf(crls(manager, list)));
    }

    assertFalse(Files.exists(duplicate));
    assertTrue(Files.exists(bundle));
    if (posix) {
      assertEquals(
          PosixFilePermissions.fromString("rw-r-----"), Files.getPosixFilePermissions(bundle));
    }

    try (var reopened = FileBasedTrustListManager.createAndInitialize(directory)) {
      assertEquals(Set.of(retained.crl(), alsoRetained.crl()), Set.copyOf(crls(reopened, list)));
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

    return new Material(certificate, generateCrl(certificate, keyPair.getPrivate()));
  }

  private static X509CRL generateCrl(X509Certificate issuer, PrivateKey privateKey)
      throws Exception {
    X509v2CRLBuilder builder =
        new X509v2CRLBuilder(
            X500Name.getInstance(issuer.getSubjectX500Principal().getEncoded()), new Date());
    builder.setNextUpdate(new Date(System.currentTimeMillis() + 60_000));

    JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
    signerBuilder.setProvider("BC");

    X509CRLHolder holder = builder.build(signerBuilder.build(privateKey));

    JcaX509CRLConverter converter = new JcaX509CRLConverter();
    converter.setProvider("BC");

    return converter.getCRL(holder);
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
