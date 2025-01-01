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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;

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
}
