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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Stream;
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
      Files.deleteIfExists(keyStorePath);
      Files.deleteIfExists(testPath);
    } catch (Exception ignored) {
      testPath.toFile().deleteOnExit();
      keyStorePath.toFile().deleteOnExit();
    }
  }

  @Override
  protected CertificateStore newCertificateStore() throws Exception {
    KeyStoreCertificateStore store = newKeyStoreCertificateStore("password"::toCharArray);

    store.initialize();

    return store;
  }

  /**
   * Opening a second store over the same file is the only way to tell that {@link
   * CertificateStore#set(NodeId, CertificateStore.Entry)} actually wrote anything; the assertions
   * inherited from {@link CertificateStoreTest} all pass against the in-memory KeyStore alone.
   */
  @Test
  void entriesAreWrittenToDisk() throws Exception {
    var certificateTypeId = new NodeId(2, "persisted");

    certificateStore.set(certificateTypeId, newEntry());

    assertNotNull(newCertificateStore().get(certificateTypeId));
  }

  @Test
  void failedWriteLeavesKeyStoreIntact() throws Exception {
    var certificateTypeId = new NodeId(2, "unwritable");
    var failWrite = new AtomicBoolean(false);

    KeyStoreCertificateStore store =
        newKeyStoreCertificateStore(
            () -> {
              if (failWrite.get()) {
                throw new IllegalStateException("KeyStore password unavailable");
              }
              return "password".toCharArray();
            });

    store.initialize();
    store.set(new NodeId(2, "survivor"), newEntry());

    failWrite.set(true);
    assertThrows(IllegalStateException.class, () -> store.set(certificateTypeId, newEntry()));
    failWrite.set(false);

    // The failed write must be rolled back in memory...
    assertFalse(store.contains(certificateTypeId));

    // ...must not have left a partially written temporary file behind...
    try (Stream<Path> files = Files.list(testPath)) {
      assertEquals(List.of(keyStorePath), files.toList());
    }

    // ...and must not have damaged what was already on disk.
    CertificateStore reopened = newCertificateStore();
    assertTrue(reopened.contains(new NodeId(2, "survivor")));
    assertFalse(reopened.contains(certificateTypeId));
  }

  /**
   * Reloading has to re-read the file: {@code loadEntries()} alone only re-queries the KeyStore
   * already held in memory, which cannot see anything another writer has done.
   */
  @Test
  void reloadPicksUpExternalChanges() throws Exception {
    var certificateTypeId = new NodeId(2, "external");

    KeyStoreCertificateStore store = newKeyStoreCertificateStore("password"::toCharArray);
    store.initialize();

    // A second store over the same file stands in for anything that rewrites the KeyStore
    // out from under this one.
    newCertificateStore().set(certificateTypeId, newEntry());
    assertFalse(store.contains(certificateTypeId));

    store.reload(keyStorePath);

    assertTrue(store.contains(certificateTypeId));
    assertNotNull(store.get(certificateTypeId));
  }

  // Application-owned entries may be committed after this store initializes. A later certificate
  // update must merge that completed write without needing the foreign entry's password.
  @Test
  void setPreservesCompletedExternalChanges() throws Exception {
    var certificateTypeId = new NodeId(2, "managed");
    CertificateStore.Entry externalEntry = newEntry();

    writeExternalEntry("application-pending", externalEntry, "external-password".toCharArray());

    certificateStore.set(certificateTypeId, newEntry());

    KeyStore persisted = loadKeyStore();
    assertTrue(persisted.isKeyEntry("managed"));
    assertNotNull(persisted.getKey("application-pending", "external-password".toCharArray()));
  }

  // An operator may delete the KeyStore file while the store is running. A write then has nothing
  // to merge and must recreate the file from memory rather than fail.
  @Test
  void setRecreatesDeletedKeyStoreFile() throws Exception {
    Files.delete(keyStorePath);

    certificateStore.set(new NodeId(2, "recreated"), newEntry());

    KeyStore persisted = loadKeyStore();
    assertTrue(persisted.isKeyEntry("test"));
    assertTrue(persisted.isKeyEntry("recreated"));
  }

  // A removal after external file deletion must update both the recreated file and the in-memory
  // cache, or subsequent reads keep returning the deleted certificate.
  @Test
  void removeRecreatesDeletedKeyStoreFileWithoutLeavingStaleCacheEntry() throws Exception {
    Files.delete(keyStorePath);

    assertNotNull(certificateStore.remove(new NodeId(2, "test")));

    assertFalse(certificateStore.contains(new NodeId(2, "test")));
    assertFalse(loadKeyStore().containsAlias("test"));
  }

  // Removal is also a whole-file rewrite, so it must preserve external entries committed before
  // the removal begins while deleting only its own target alias.
  @Test
  void removePreservesCompletedExternalChanges() throws Exception {
    CertificateStore.Entry externalEntry = newEntry();

    writeExternalEntry("application-pending", externalEntry, "external-password".toCharArray());

    assertNotNull(certificateStore.remove(new NodeId(2, "test")));

    KeyStore persisted = loadKeyStore();
    assertFalse(persisted.containsAlias("test"));
    assertNotNull(persisted.getKey("application-pending", "external-password".toCharArray()));
  }

  @Test
  void watchEventsResolveAgainstTheWatchedDirectory() {
    Path watchedDirectory = keyStorePath.getParent();

    // A directory watch reports the file name relative to the directory it was registered on,
    // not a path that can be resolved against the working directory.
    assertTrue(
        KeyStoreCertificateStore.isKeyStoreEvent(
            watchEvent(StandardWatchEventKinds.ENTRY_CREATE, keyStorePath.getFileName()),
            watchedDirectory,
            keyStorePath));

    assertFalse(
        KeyStoreCertificateStore.isKeyStoreEvent(
            watchEvent(StandardWatchEventKinds.ENTRY_MODIFY, Path.of(".keystore123.tmp")),
            watchedDirectory,
            keyStorePath));

    // Nothing is known about what was dropped, so an overflow has to be treated as a change.
    assertTrue(
        KeyStoreCertificateStore.isKeyStoreEvent(
            watchEvent(StandardWatchEventKinds.OVERFLOW, null), watchedDirectory, keyStorePath));
  }

  @Test
  void watchForChangesReloadsTheKeyStore() throws Exception {
    var certificateTypeId = new NodeId(2, "watched");

    KeyStoreCertificateStore store = newKeyStoreCertificateStore("password"::toCharArray, true);
    store.initialize();

    try {
      newCertificateStore().set(certificateTypeId, newEntry());

      // The KeyStore is replaced by a rename, which arrives as a creation rather than a
      // modification, and on platforms without a native watcher it arrives only when the polling
      // interval next elapses.
      assertTrue(
          awaitContains(store, certificateTypeId),
          "watchForChanges did not reload the KeyStore within the timeout");
    } finally {
      store.close();
    }
  }

  @Test
  void defaultAliasesPreserveRsaAndNameCurrentEccTypes() throws IOException {
    try (var store =
        new KeyStoreCertificateStore(
            new KeyStoreCertificateStore.Settings(
                keyStorePath, "password"::toCharArray, alias -> "password".toCharArray()))) {

      assertEquals(
          "server-rsa-sha256", store.getAlias(NodeIds.RsaSha256ApplicationCertificateType));
      assertEquals(
          "server-ecc-nistp256", store.getAlias(NodeIds.EccNistP256ApplicationCertificateType));
      assertEquals(
          "server-ecc-nistp384", store.getAlias(NodeIds.EccNistP384ApplicationCertificateType));
      assertEquals(
          "server-ecc-brainpoolp256r1",
          store.getAlias(NodeIds.EccBrainpoolP256r1ApplicationCertificateType));
      assertEquals(
          "server-ecc-brainpoolp384r1",
          store.getAlias(NodeIds.EccBrainpoolP384r1ApplicationCertificateType));
      assertEquals(
          "server-ecc-curve25519", store.getAlias(NodeIds.EccCurve25519ApplicationCertificateType));
      assertEquals(
          "server-ecc-curve448", store.getAlias(NodeIds.EccCurve448ApplicationCertificateType));
      assertEquals(
          new NodeId(2, "custom").toParseableString(), store.getAlias(new NodeId(2, "custom")));
    }
  }

  @Test
  void configuredAliasPrefixNamesStandardCertificateTypes() throws IOException {
    try (var store =
        new KeyStoreCertificateStore(
            new KeyStoreCertificateStore.Settings(
                keyStorePath,
                "password"::toCharArray,
                alias -> "password".toCharArray(),
                "client-"))) {

      assertEquals(
          "client-rsa-sha256", store.getAlias(NodeIds.RsaSha256ApplicationCertificateType));
      assertEquals(
          "client-ecc-nistp256", store.getAlias(NodeIds.EccNistP256ApplicationCertificateType));
      assertEquals(
          "client-ecc-nistp384", store.getAlias(NodeIds.EccNistP384ApplicationCertificateType));
      assertEquals(
          "client-ecc-brainpoolp256r1",
          store.getAlias(NodeIds.EccBrainpoolP256r1ApplicationCertificateType));
      assertEquals(
          "client-ecc-brainpoolp384r1",
          store.getAlias(NodeIds.EccBrainpoolP384r1ApplicationCertificateType));
      assertEquals(
          "client-ecc-curve25519", store.getAlias(NodeIds.EccCurve25519ApplicationCertificateType));
      assertEquals(
          "client-ecc-curve448", store.getAlias(NodeIds.EccCurve448ApplicationCertificateType));
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
        "server-rsa-sha256", keyPair.getPrivate(), "password".toCharArray(), certificateChain);

    try (var outputStream = Files.newOutputStream(keyStorePath)) {
      keyStore.store(outputStream, "password".toCharArray());
    }

    var store =
        new KeyStoreCertificateStore(
            new KeyStoreCertificateStore.Settings(
                keyStorePath,
                "password"::toCharArray,
                alias -> {
                  if (!"server-rsa-sha256".equals(alias)) {
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

  private static boolean awaitContains(CertificateStore store, NodeId certificateTypeId)
      throws Exception {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);

    while (System.nanoTime() < deadline) {
      if (store.contains(certificateTypeId)) {
        return true;
      }

      Thread.sleep(50);
    }

    return store.contains(certificateTypeId);
  }

  private static WatchEvent<?> watchEvent(WatchEvent.Kind<?> kind, @Nullable Object context) {
    @SuppressWarnings("unchecked")
    var typedKind = (WatchEvent.Kind<Object>) kind;

    return new WatchEvent<>() {
      @Override
      public WatchEvent.Kind<Object> kind() {
        return typedKind;
      }

      @Override
      public int count() {
        return 1;
      }

      @Override
      public @Nullable Object context() {
        return context;
      }
    };
  }

  private KeyStoreCertificateStore newKeyStoreCertificateStore(Supplier<char[]> keyStorePassword) {
    return newKeyStoreCertificateStore(keyStorePassword, false);
  }

  private KeyStoreCertificateStore newKeyStoreCertificateStore(
      Supplier<char[]> keyStorePassword, boolean watchForChanges) {

    return new KeyStoreCertificateStore(
        new KeyStoreCertificateStore.Settings(
            keyStorePath, keyStorePassword, alias -> "password".toCharArray(), watchForChanges)) {

      @Override
      protected @Nullable String getAlias(NodeId certificateTypeId) {
        return certificateTypeId.getIdentifier().toString();
      }
    };
  }

  private void writeExternalEntry(String alias, CertificateStore.Entry entry, char[] aliasPassword)
      throws Exception {

    KeyStore keyStore = loadKeyStore();
    keyStore.setKeyEntry(alias, entry.privateKey, aliasPassword, entry.certificateChain);

    try (var outputStream = Files.newOutputStream(keyStorePath)) {
      keyStore.store(outputStream, "password".toCharArray());
    }
  }

  private KeyStore loadKeyStore() throws Exception {
    KeyStore keyStore = KeyStore.getInstance("pkcs12");

    try (var inputStream = Files.newInputStream(keyStorePath)) {
      keyStore.load(inputStream, "password".toCharArray());
    }

    return keyStore;
  }

  private static CertificateStore.Entry newEntry() {
    var factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();

    return new CertificateStore.Entry(
        keyPair.getPrivate(), factory.createRsaSha256CertificateChain(keyPair));
  }
}
