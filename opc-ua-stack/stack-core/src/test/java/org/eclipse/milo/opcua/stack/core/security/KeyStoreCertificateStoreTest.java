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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Stream;
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

  private KeyStoreCertificateStore newKeyStoreCertificateStore(Supplier<char[]> keyStorePassword) {
    return new KeyStoreCertificateStore(
        new KeyStoreCertificateStore.Settings(
            keyStorePath, keyStorePassword, alias -> "password".toCharArray())) {

      @Override
      protected @Nullable String getAlias(NodeId certificateTypeId) {
        return certificateTypeId.getIdentifier().toString();
      }
    };
  }

  private static CertificateStore.Entry newEntry() {
    var factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();

    return new CertificateStore.Entry(
        keyPair.getPrivate(), factory.createRsaSha256CertificateChain(keyPair));
  }
}
