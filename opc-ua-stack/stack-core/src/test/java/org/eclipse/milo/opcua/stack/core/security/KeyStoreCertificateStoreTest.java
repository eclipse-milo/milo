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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class KeyStoreCertificateStoreTest extends CertificateStoreTest {

  private final Path testPath = Files.createTempDirectory("KeyStoreCertificateStoreTest");
  private final Path keyStorePath = testPath.resolve("testKeyStore.pfx");

  KeyStoreCertificateStoreTest() throws IOException {}

  @AfterEach
  void deleteTestFiles() {
    try {
      Files.deleteIfExists(testPath);
      Files.deleteIfExists(keyStorePath);
    } catch (Exception ignored) {
      testPath.toFile().deleteOnExit();
      keyStorePath.toFile().deleteOnExit();
    }
  }

  @Override
  protected CertificateStore newCertificateStore() throws Exception {
    var store =
        new KeyStoreCertificateStore(
            new KeyStoreCertificateStore.Settings(
                keyStorePath, "password"::toCharArray, alias -> "password".toCharArray())) {

          @Override
          protected @Nullable String getAlias(NodeId certificateTypeId) {
            return certificateTypeId.getIdentifier().toString();
          }
        };

    store.initialize();

    return store;
  }

  @Test
  void defaultAliasesPreserveRsaAndNameCurrentEccTypes() throws IOException {
    try (var store =
        new KeyStoreCertificateStore(
            new KeyStoreCertificateStore.Settings(
                keyStorePath, "password"::toCharArray, alias -> "password".toCharArray()))) {

      assertEquals(
          KeyStoreCertificateStore.RSA_SHA256_ALIAS,
          store.getAlias(NodeIds.RsaSha256ApplicationCertificateType));
      assertEquals(
          KeyStoreCertificateStore.ECC_NIST_P256_ALIAS,
          store.getAlias(NodeIds.EccNistP256ApplicationCertificateType));
      assertEquals(
          KeyStoreCertificateStore.ECC_NIST_P384_ALIAS,
          store.getAlias(NodeIds.EccNistP384ApplicationCertificateType));
      assertEquals(
          KeyStoreCertificateStore.ECC_BRAINPOOL_P256R1_ALIAS,
          store.getAlias(NodeIds.EccBrainpoolP256r1ApplicationCertificateType));
      assertEquals(
          KeyStoreCertificateStore.ECC_BRAINPOOL_P384R1_ALIAS,
          store.getAlias(NodeIds.EccBrainpoolP384r1ApplicationCertificateType));
      assertEquals(
          KeyStoreCertificateStore.ECC_CURVE25519_ALIAS,
          store.getAlias(NodeIds.EccCurve25519ApplicationCertificateType));
      assertEquals(
          KeyStoreCertificateStore.ECC_CURVE448_ALIAS,
          store.getAlias(NodeIds.EccCurve448ApplicationCertificateType));
      assertEquals(
          new NodeId(2, "custom").toParseableString(), store.getAlias(new NodeId(2, "custom")));
    }
  }

  @Test
  void initializeIgnoresUnmanagedPrivateKeyEntries() throws Exception {
    TestCertificateFactory factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain = factory.createRsaSha256CertificateChain(keyPair);
    KeyStore keyStore = KeyStore.getInstance("pkcs12");
    keyStore.load(null, "password".toCharArray());
    keyStore.setKeyEntry(
        "unmanaged", keyPair.getPrivate(), "other-password".toCharArray(), certificateChain);

    try (var outputStream = Files.newOutputStream(keyStorePath)) {
      keyStore.store(outputStream, "password".toCharArray());
    }

    var store =
        new KeyStoreCertificateStore(
            new KeyStoreCertificateStore.Settings(
                keyStorePath,
                "password"::toCharArray,
                alias ->
                    "unmanaged".equals(alias)
                        ? "wrong-password".toCharArray()
                        : "password".toCharArray()));

    assertDoesNotThrow(store::initialize);
    assertFalse(store.contains(NodeIds.RsaSha256ApplicationCertificateType));

    store.close();
  }

  @Test
  void initializeDoesNotRequestPasswordsForMissingPreloadedAliases() throws Exception {
    TestCertificateFactory factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain = factory.createRsaSha256CertificateChain(keyPair);
    KeyStore keyStore = KeyStore.getInstance("pkcs12");
    keyStore.load(null, "password".toCharArray());
    keyStore.setKeyEntry(
        KeyStoreCertificateStore.RSA_SHA256_ALIAS,
        keyPair.getPrivate(),
        "password".toCharArray(),
        certificateChain);

    try (var outputStream = Files.newOutputStream(keyStorePath)) {
      keyStore.store(outputStream, "password".toCharArray());
    }

    var store =
        new KeyStoreCertificateStore(
            new KeyStoreCertificateStore.Settings(
                keyStorePath,
                "password"::toCharArray,
                alias -> {
                  if (!KeyStoreCertificateStore.RSA_SHA256_ALIAS.equals(alias)) {
                    throw new AssertionError("unexpected password request for alias: " + alias);
                  }

                  return "password".toCharArray();
                }));

    assertDoesNotThrow(store::initialize);
    assertTrue(store.contains(NodeIds.RsaSha256ApplicationCertificateType));
    assertFalse(store.contains(NodeIds.EccNistP256ApplicationCertificateType));
    assertFalse(store.contains(NodeIds.EccNistP384ApplicationCertificateType));
    assertFalse(store.contains(NodeIds.EccBrainpoolP256r1ApplicationCertificateType));
    assertFalse(store.contains(NodeIds.EccBrainpoolP384r1ApplicationCertificateType));
    assertFalse(store.contains(NodeIds.EccCurve25519ApplicationCertificateType));
    assertFalse(store.contains(NodeIds.EccCurve448ApplicationCertificateType));

    store.close();
  }
}
